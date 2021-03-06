<%@ page import="org.apache.commons.lang.RandomStringUtils" %>
<div id="browseDetailsMessageArea"></div>
<div id=browseDetailsDialogArea" style-"height:0px;></div>
<g:hiddenField id="browseDetailsAbsPath" name="absolutePath" value="${parent.collectionName}" />

<div style="overflow: visible; position: relative;">
	<div id="idropUploadrArea">
		<!--  area to show idrop lite 1 -->	
		<asset:javascript src="uploadr.manifest.js"/>
    	<asset:stylesheet href="uploadr.manifest.css"/>
    	
    	<!-- <% def epath = new File("${updir}") %> -->
    		
		<uploadr:add name="idropUploadr" path="${updir}" direction="up" maxVisible="10" colorPicker="true" noSound="true" maxSize="0" downloadable="false" >
			<!--
			<% epath.listFiles().each { efile -> %>
			
    			<uploadr:file name="${efile.name}">
        			<uploadr:fileSize>${efile.size()}</uploadr:fileSize>
        			<uploadr:fileModified>${efile.lastModified()}</uploadr:fileModified>
        			<uploadr:fileId>myId-${RandomStringUtils.random(32, true, true)}</uploadr:fileId>
    			</uploadr:file>
			
			<% } %>
			-->
			<uploadr:onSuccess>
	
				var file_name = file.fileName;
				var collection = $j("#browseDetailsAbsPath").val();
				
				var parameters = {
					"file": file_name,
					"path": collection
				};
								
				//alert("Uploaded " + file_name + ", Collection: " + collection);
				$j.ajax({
    				url:"${g.createLink(controller:'uploadr',action:'handle')}",
    					dataType: 'json',
    					data: parameters,
    					success: function(data) {
        					//alert(data)
    					},
    					error: function(request, status, error) {
        					alert(error)
    					},
    					complete: function() {
    					}
				});
				
				callback();
				
			</uploadr:onSuccess>
		</uploadr:add>
		<div id='bulkUploadMenu' class='fg-buttonset fg-buttonset-single' style='float:none'>
			<button type='button' id='toggleMenuButton' class='ui-state-default ui-corner-all' value='closeIdropUploadr' onclick='closeIdropUploadrArea()')>Close Bulk Upload</button>			
		</div>
	</div>
	<div id="toggleHtmlArea">
		<div id="infoDialogArea">
			<!--  no empty divs -->
		</div>
		<!--  render the paging controls if needed -->
		<g:if test="${pagingActions.paging}">
			<g:render template="/browse/browseDetailsToolbar" />
		</g:if>

		<div id="detailsTopSection">
			<form id="browseDetailsForm" name="browseDetailsForm">
				<table class="table table-striped table-hover" cellspacing="0"
					cellpadding="0" border="0" id="browseDataDetailsTable">
					<thead>
						<tr>
							<th></th>
							<th>

								<div class="btn-group">
									<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">Action<span
										class="caret"></span></a>
									<ul class="dropdown-menu">
										<li id="menuAddToCartDetails"><a
											href="#addAllToCartDetails" onclick="addSelectedToCart()"><g:message
													code="text.add.all.to.cart" /></a></li>
										<li id="menuDeleteDetails"><a href="#deleteAllDetails"
											onclick="deleteSelected()"><g:message
													code="text.delete.all" /></a></li>
										<!-- dropdown menu links -->
									</ul>
								</div>

							</th>
							<th><Jsoup:clean><g:message code="text.name" /></Jsoup:clean></th>
							<th><g:message code="text.type" /></th>
							<th><g:message code="text.modified" /></th>
							<th><g:message code="text.length" /></th>
						</tr>
					</thead>
					<tbody>
						<g:each in="${collection}" var="entry">
							<tr id="${entry.formattedAbsolutePath}" class="draggableFile">

								<td><span
									class="ui-icon-circle-plus browse_detail_icon ui-icon"></span>
								</td>
								<td><g:checkBox name="selectDetail"
										value="${entry.formattedAbsolutePath}" checked="false" /> <g:if
										test="${entry.objectType.toString() == 'COLLECTION'}">
										<i class="icon-info-sign"
											onclick="infoHere('${entry.formattedAbsolutePath}')" ></i>
									</g:if> <g:else>
										<i class="icon-info-sign"
											onclick="infoHere('${entry.formattedAbsolutePath}')" ></i>
									</g:else></td>
								<td><g:if
										test="${entry.objectType.toString() == 'COLLECTION'}">
										<a href="#" id="${entry.formattedAbsolutePath}"
											onclick="clickOnPathInBrowseDetails(this.id)">
											<Jsoup:clean>${entry.nodeLabelDisplayValue}</Jsoup:clean>
										</a>

									</g:if> <g:else>
										${entry.nodeLabelDisplayValue}

										
										<i class="icon-download"
											onclick="downloadViaToolbarGivenPath('${entry.formattedAbsolutePath}')" ></i>
										
									</g:else></td>
								<td>
									${entry.objectType}
								</td>
								<td>
									${entry.modifiedAt}
								</td>
								<td>
									${entry.displayDataSize}
								</td>
							</tr>
						</g:each>

					</tbody>

					<tfoot>
						<tr>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
					</tfoot>
				</table>
			</form>
		</div>
	</div>
</div>
<script>
	var dataTable;
	
	$j("#idropUploadrArea").animate({
		height : 'hide'
	}, 'fast');

	tableParams = {
		"bJQueryUI" : true,
		"bLengthChange" : false,
		"bFilter" : false,
		"iDisplayLength" : 500,
		"sDom" : "<'row'<'span10'l><'span8'f>r>t<'row'<'span10'i><'span10'p>>",
		"aoColumns" : [ {
			'sWidth' : '20px',
			'bSortable' : false
		}, {
			'sWidth' : '20px',
			'bSortable' : false
		}, {
			'sWidth' : '120px'
		}, {
			'sWidth' : '30px'
		}, {
			'sWidth' : '40px'
		}, {
			'sWidth' : '40px'
		}

		],

		"fnInitComplete" : function() {
			this.fnAdjustColumnSizing(true);
		}

	}

	$j(function() {
		//alert("building table ");
		dataTable = lcBuildTableInPlace("#browseDataDetailsTable",
				browseDetailsClick, ".browse_detail_icon", tableParams);
		$j("#infoDiv").resize();
		$j.extend($j.fn.dataTableExt.oStdClasses, {
			"sSortAsc" : "header headerSortDown",
			"sSortDesc" : "header headerSortUp",
			"sSortable" : "header"
		});
	});

	function showLegend() {
		$j("#legend").show("slow");
	}

	function hideLegend() {
		$j("#legend").hide("slow");
	}

	function infoHere(path) {
		setDefaultView("info");
		selectTreePathFromIrodsPath(path);
	}
</script>