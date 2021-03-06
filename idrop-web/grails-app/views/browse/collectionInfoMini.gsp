<%@ page import="org.apache.commons.lang.RandomStringUtils" %>
<g:hiddenField id="infoAbsPath" name="absolutePath" value="${collection.collectionName}" />
	<div id="infoMessageArea">
		<!--  -->
	</div>

	<div id="idropUploadrArea">
		<!--  area to show idrop lite 3 -->
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
				var collection = $j("#infoAbsPath").val();
				
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
		<div id="infoDialogArea"><!--  no empty divs --></div>
		
		<!-- display area lays out info in a main and side column -->
	<div id="infoDisplayLayout" style="display:table;width:100%;height:100%;">
	<!--
	<div style="display:table-row;">
		<div id="infoThumbnailAndMediaDisplay"  style="display:table-cell;">
			<div id="infoThumbnailLoadArea"><image src="<g:resource dir="images" file="folder.png" alt="folder icon" />"/></div>
		</div>
	</div>
	-->
	<div style="display:table-row;">
		<div id="infoDisplayMain"  style="display:table-cell;">
		
		<!-- inner table for general data -->
		
			<div id="container" style="height:100%;width:100%;">
		
				<div>
					<div style="width:20%;"><label>Collection:</label></div>
					<div style="overflow:auto;">${collection.collectionName}</div>
				</div>
				<div>
					<div><label>Tags:</label></div>
					<div><g:textField id="infoTags" name="tags"
					value="${tags.spaceDelimitedTagsForDomain}" /></div>
				</div>
				<div>
					<div><label>Comment:</label></div>
					<div><g:textArea id="infoComment" name="comment" rows="5" cols="80"
					value="${comment}" /></div>
				</div>
				<div>
					<div></div>

					<div><button type="button" id="updateTags" value="updateTags" onclick="updateTagsFromCollectionInfoMini()">Update Tags</button></div>

				</div>
				<div>
					<div><label>Created At:</label></div>
					<div>${collection.createdAt}</div>
				</div>
				<div>
					<div><label>Updated At:</label></div>
					<div>${collection.modifiedAt}</div>
				</div>
				<div>
					<div><label>Owner:</label></div>
					<div>${collection.collectionOwnerName}</div>
				</div>
				<div>
					<div><label>Owner Zone:</label></div>
					<div>${collection.collectionOwnerZone}</div>
				</div>
				<div>
					<div><label>Collection Type:</label></div>
					<div>${collection.specColType}</div>
				</div>
				<div>
					<div><label>Object Path:</label></div>
					<div>${collection.objectPath}</div>
				</div>
				<div>
					<div><label>Description:</label></div>
					<div>${collection.comments}</div>
				</div>
				<div>
					<div><label>Info1:</label></div>
					<div>${collection.info1}</div>
				</div>
				<div>
					<div><label>Info2:</label></div>
					<div>${collection.info2}</div>
				</div>
				
			</div>
			
		</div><!-- cell -->
	</div><!-- row -->
	
	
</div><!-- table -->
</div><!--  toggle html area -->

<script>

$j("#idropUploadrArea").animate({
	height : 'hide'
}, 'fast');

function updateTagsFromCollectionInfoMini() {
	var infoTagsVal = $j("#infoTags").val();
	var infoCommentVal = $j("#infoComment").val();
	var absPathVal = $j("#infoAbsPath").val();
	
	updateTagsAtPath(absPathVal, infoTagsVal, infoCommentVal);
}

</script>
