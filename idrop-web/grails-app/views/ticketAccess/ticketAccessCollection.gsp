<head>
<meta name="layout" content="basic" />
</head>
<div id="uploadToTicketCollectionDialogDiv"><!--  div for an upload dialog --></div>
<div  class="wrapper"
	style="height: 820px;">
	<div class="roundedContainer">
	<h1>Landing page for a collection</h1>
	<h2>This ticket allows you to upload a file to an iRODS location.  Click the button below to select a file to you local file system and send it to iRODS</h2>
	</div>
	<div id="container" style="height:auto;width:100%;">
				<g:hiddenField name='ticketString' id='ticketString' value='${ticketString}'/>
				<g:hiddenField name='irodsURI' id='irodsURI' value='${irodsURI}'/>
				<div>
					<div style="width:20%;"><label><g:message code="text.ticket.string" />:</label></div>
					<div>${ticketString}</div>
				</div>
				<div>
					<div style="width:20%;"><label><g:message code="text.ticket.type" />:</label></div>
					<div>${ticketType}</div>
				</div>
				<div>
					<div style="width:20%;"><label><g:message code="text.irods.uri" />:</label></div>
					<div>${irodsURI}</div>
				</div>
				<div>
					<div style="width:20%;"><label><g:message code="text.actions" />:</label></div>
					<div><button type="button" id="uploadToCollectionButton"
								onclick="uploadToCollectionButton()")><g:message code="text.upload" /></button></div>
				</div>
	</div>
			
</div>
<script>

var fileUploadUI;

function uploadToCollectionButton() {
	var ticketString = $j("#ticketString").val();
	var irodsURI = $j("#irodsURI").val();
	var url = "/ticketAccess/prepareUploadDialog";
	var params = {
		irodsURI : irodsURI,
		ticketString: ticketString
	}

	lcSendValueWithParamsAndPlugHtmlInDiv(url, params, "", function(data) {
		fillInUploadToCollectionDialog(data);
	});}

function fillInUploadToCollectionDialog(data) {

	if (data == null) {
		return;
	}

	//$j('#uploadToTicketCollectionDialogDiv').remove();

	var $jdialog = $j("#uploadToTicketCollectionDialogDiv").html(data).dialog({
		autoOpen : false,
		modal : true,
		width : 500,
		title : 'Upload to iRODS',
		create : function(event, ui) {
			initializeUploadToTicketCollectionDialogAjaxLoader();
		}
	});

	$jdialog.dialog('open');
}

/**
 * Create the upload dialog for web (http) uploaded.
 */
function initializeUploadToTicketCollectionDialogAjaxLoader() {

	if (fileUploadUI != null) {
		$j("#fileUploadForm").remove;
	}

	fileUploadUI = $j('#uploadForm')
			.fileUploadUI(
					{
						uploadTable : $j('#files'),
						downloadTable : $j('#files'),

						buildUploadRow : function(files, index) {
							$j("#upload_message_area").html("");
							$j("#upload_message_area").removeClass();
							return $j('<tr><td>'
									+ files[index].name
									+ '<\/td>'
									+ '<td class="file_upload_progress"><div><\/div><\/td>'
									+ '<\/tr>');
						},
						buildDownloadRow : function(file) {
							return $j('<tr><td>' + file.name + '<\/td><\/tr>');
						},
						onComplete : function(event, files, index, xhr, handler) {
							setMessage(jQuery.i18n.prop('msg_upload_complete'));

							$j('#uploadToTicketCollectionDialogDiv').dialog('close');
							$j('#uploadToTicketCollectionDialogDiv').remove();

						},
						onError : function(event, files, index, xhr, handler) {
							setErrorMessage(xhr.responseText);
						}
					});

}



</script>