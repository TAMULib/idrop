/**
 * Javascript for ticket functions
 * 
 * author: Mike Conway - DICE
 */

/**
 * Global var
 */

var ticketTableUrl = '/ticket/listTickets';
var ticketUpdateUrl = '/ticket/update';
var ticketPulldownUrl = '/ticket/ticketPulldown';
var deleteTicketsActionUrl = '/ticket/deleteTickets';

var ticketTable;

function reloadTicketTable(absPath) {
	if (absPath == null) {
		throw "null absPath";
	}
	
	lcClearDivAndDivClass("#ticketTableDiv");
	lcShowBusyIconInDiv("#ticketTableDiv");
	
	var params = {
		absPath : absPath
	}

	showBlockingPanel();

	var jqxhr = $j.get(context + ticketTableUrl, params,
			function(data, status, xhr) {
				$j('#ticketTableDiv').html(data);
				var continueReq = checkForSessionTimeout(data, xhr);
				if (!continueReq) {
					return false;
				}
				buildTicketTableInPlace();
			}, "html")
			.error(function(xhr, status, error) {
				var message = $j.i18n.prop('msg_ticket_error');
				displayMessageAsBootstrapAlert(message, "#infoAccordionTicketsInner");
				 setErrorMessage(xhr.responseText);
				 unblockPanel();
			});
	
}

/**
 * Given that the ticket details table data has been retrieved (as html table),
 * make it a dataTable
 * 
 * @returns
 */
function buildTicketTableInPlace() {

	  tableParams = {"bJQueryUI" : true,
          	"bLengthChange": false,
          	"bFilter": false,
          	"iDisplayLength" : 500,
          	 "aoColumns" : [
        	                {'sWidth': '20px', 'bSortable':false},
        	                {'sWidth': '20px', 'bSortable':false},
        	                null,
        	                null,
        	                null  
        	            ]
          }
	 ticketTable = lcBuildTableInPlace("#ticketDetailsTable", ticketDetailsClick, ".browse_detail_icon", tableParams);
	  unblockPanel();

	return ticketTable;
}

/* click twistie to open details table info */
function ticketDetailsClick(minMaxIcon) {
		
        var nTr = minMaxIcon.parentNode.parentNode;

        if (minMaxIcon.parentNode.innerHTML.match('circle-minus')) {
                lcCloseTableNodes(ticketTable);
        } else {
                try {
                        browseTicketDetailsFunction(minMaxIcon, nTr);
                } catch (err) {
                        showError("error in detailsClick():" + err);
                }

        }
}

/** called by browseDetailsClick() when it is decided that the details table row should be opened, go 
to server and get the details.
*/
function browseTicketDetailsFunction(clickedIcon, rowActionIsOn) {
        /* Open this row */
        lcCloseTableNodes(ticketTable);
        // nTr points to row and has ticket string in id
        var ticketString = $j(rowActionIsOn).attr('id');
        //alert("ticketString:" + ticketString);
        var detailsId = "details_" + ticketString;
        var detailsHtmlDiv = "details_html_" + ticketString;
        var buildDetailsLayoutVal = buildTicketDetailsLayout(ticketString);
        clickedIcon.setAttribute("class", "ui-icon ui-icon-circle-minus");
        newRowNode = ticketTable.fnOpen(rowActionIsOn,
        		buildDetailsLayoutVal, 'ticketDetails');
        newRowNode.setAttribute("id", detailsId);
        askForTicketDetailsPulldown(ticketString, detailsId)
        
}

/** The table row is being opened, and the query has returned from the server with the data, fill in the table row
 */
 function buildTicketDetailsLayout(detailsId) {
         var td = document.createElement("TD");
         td.setAttribute("colspan", "5");

         var detailsPulldownDiv = document.createElement("DIV");
         detailsPulldownDiv.setAttribute("id", detailsId);
         detailsPulldownDiv.setAttribute("class", "ticketDetails");
         var img = document.createElement('IMG');
         img.setAttribute("src", context + "/images/ajax-loader.gif");
         detailsPulldownDiv.appendChild(img);
         td.appendChild(detailsPulldownDiv);
         return $j(td).html();
 }
 
 function askForTicketDetailsPulldown(ticketString, detailsId) {
	 var absPath = $j("#ticketDetailsAbsPath").val();
	 
	
     var params = {
                     absPath:absPath,
                     ticketString:ticketString
             }
	 try {
     lcSendValueWithParamsAndPlugHtmlInDiv(ticketPulldownUrl, params, ".ticketDetails",
                     null);
	 } catch(err) {
		 showErrorMessage(err);
		 return false
	 }

}


/**
 * Reload the tickets table
 */

function reloadTickets() {
	var absPath = $j("#ticketDetailsAbsPath").val();
	reloadTicketTable(absPath);
}

/**
 * Show ticket details dialog area
 * 
 * @param create -
 *            is this a create or edit
 */

function prepareTicketDetailsDialog(ticketString) {
	var absPath = $j("#ticketDetailsAbsPath").val();
	var url = "/ticket/ticketDetailsDialog";
	
	var create = false;
	
	if (!ticketString) {
		create = true;
		ticketString = "";
	}
	
	var params = {
		ticketString : ticketString,
		create : create,
		irodsAbsolutePath : absPath
	}

	lcSendValueWithParamsAndPlugHtmlInDiv(url, params, "", function(data) {
		showTicketDetailsDialog(data);
	});

}

/*
 *The ticket in the pulldown should be updated 
 */
function updateTicketFromPulldown() {
	var formData = $j("#ticketPulldownDetailsForm").serializeArray();
	if (formData == null) {
		setErrorMessage($j.i18n.prop('msg_no_ticket_data'));
		return false;
	}
	
        showBlockingPanel();

	lcShowBusyIconInDiv("#ticketPulldownDiv");

	var jqxhr = $j.post(context + ticketUpdateUrl, formData,
			function(data, status, xhr) {
			}, "html").success(function(data, status, xhr) {
				var continueReq = checkForSessionTimeout(data, xhr);
				if (!continueReq) {
					return false;
				} 
				
	$j(".ticketDetails").html(data);
        unblockPanel();
				
	}).error(function(xhr, status, error) {
		reloadTicketTable(selectedPath);
		setErrorMessage(xhr.responseText);
                unblockPanel();
	});
}

/**
 * Show the dialog with the provided data
 * 
 * @param data -
 *            html data
 */
function showTicketDetailsDialog(data) {
	//$j("#ticketTableDiv").fadeOut('slow');
	$j("#ticketDetailsTableArea").fadeOut('slow');
	$j("#ticketDialogArea").html(data).fadeIn('slow');
}

/*
 *Reload the ticket pulldown data 
 */
function cancelTicketFromPulldown() {
	var ticketString = $j("#ticketString").val();
	
	if (ticketString == null) {
		setErrorMessage($j.i18n.prop('msg_ticket_no_data'));
		return false;
	}
	
	askForTicketDetailsPulldown(ticketString, ".ticketDetails");
}

/**
 * Cause the add ticket dialog to be closed
 */
function closeTicketDialog() {
	
	$j("#ticketDialogArea").fadeOut('slow');
	$j("#ticketDialogArea").html("");
	$j("#ticketDetailsTableArea").fadeIn('slow');
	reloadTicketTable(selectedPath);
	
	// setting width and height?
	
}


/**
 * Handle form submit to add a ticket
 */
function submitTicketDialog() {
	
		var selectedPath = $j("#ticketDetailsAbsPath").val();
	
		if (selectedPath == null) {
			setErrorMessage("no collection or data object selected");
			return false;
		}
		
		// see if there is form data (users in a pick list) that are selected
		var formData = $j("#ticketPulldownDetailsForm").serializeArray();
	
		if (formData == null) {
			setErrorMessage($j.i18n.prop('msg_no_ticket_data'));
			return false;
		}
	
		var jqxhr = $j.post(context + ticketUpdateUrl, formData,
				function(data, status, xhr) {
				}, "html").success(function(data, status, xhr) {
					var continueReq = checkForSessionTimeout(data, xhr);
					if (!continueReq) {
						return false;
					} 
					
					//closeTicketDialog();
					//setMessage($j.i18n.prop('msg_ticket_update_successful'));
					//reloadTicketTable(selectedPath);
					$j("#ticketDialogArea").html(data);

		}).error(function(xhr, status, error) {
			closeTicketDialog();
			reloadTicketTable(selectedPath);
			setErrorMessage(xhr.responseText);
		});
		
}

function deleteTicket() {
	
	if (!confirm($j.i18n.prop('msg_delete_confirm'))) {
        // your deletion code
		return false;
	}
    
	var formData = $j("#ticketTableForm").serializeArray();
	showBlockingPanel();

	var jqxhr = $j.post(context + deleteTicketsActionUrl, formData, "html")
			.success(function(returnedData, status, xhr) {
				var continueReq = checkForSessionTimeout(returnedData, xhr);
				if (!continueReq) {
					return false;
				}
				reloadTicketTable(selectedPath);
				setMessage($j.i18n.prop('msg_delete_successful'));
				unblockPanel();
			}).error(function(xhr, status, error) {
				setErrorMessage(xhr.responseText);
				unblockPanel();
			});

}

/**
 * Edit was selected, see if there is a ticket to edit and show the edit dialog
 */
function editTicketDialog() {
	// find first selected ticket in table, if none selected show message and ignore
	var edited =  $j(":checked").filter("#selectedTicket");
	if (!edited.length > 0) {
		setMessage($j.i18n.prop('msg_nothing_selected_for_edit'));
		return;
	}
	
	var selectedTR = $j(edited[0]).parent().parent();
	var ticketString = $j(selectedTR).attr('id');
	prepareTicketDetailsDialog(ticketString);

}



