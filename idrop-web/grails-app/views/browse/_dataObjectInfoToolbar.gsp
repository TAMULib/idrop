<div id="dataObjectInfoToolbar" >

	<div id="dataObjectInfoToolbarMenu" class="btn-toolbar">
		
		<div id="dataObjectInfoButtonGroup1" class="btn-group">
			<g:if  test="${irodsStarredFileOrCollection}">
				<button id="unstarDataObject" onclick="dibUnstarFile()"><i class="icon-star"></i><g:message
					code="text.unstar" /></button>
			</g:if>
			<g:else>
				<button id="starDataobject" onclick="dibStarFile()"><i class="icon-star-empty"></i><g:message
					code="text.star.file" /></button>
			</g:else>
			<button id="downloadFile" onclick="dibDownloadFile()"><i class="icon-download"></i><g:message
					code="text.download" /></button>
			<button id="addToCart" onclick="addToCartViaToolbar()"><i class="icon-shopping-cart"></i><g:message
					code="text.add.to.cart" /></button>			
		</div>
		<div id="dataObjectInfoButtonGroup2" class="btn-group">
			<button id="renameDataObject" onclick="dibRenameFile()"><i class="icon-pencil"></i><g:message
					code="text.rename" /></button>
			<button id="deleteDataObject" onclick="dibDeleteFile()"><i class="icon-trash"></i><g:message
					code="text.delete" /></button>
		</div>
</div>

<script type="text/javascript">

function dibDownloadFile() {
	var path = $j("#infoAbsPath").val();
	//alert("path from infoAbsPath:" + path);
	if (path == null) {
		showErrorMessage($j.i18n.prop('msg.path.missing'));
		return false;
	}

	downloadViaToolbarGivenPath(path);
}

/**
* Delete the file currently displayed in the info view
*/
function dibRenameFile() {
	var path = $j("#infoAbsPath").val();
	if (path == null) {
		showErrorMessage($j.i18n.prop('msg.path.missing'));
		return false;
	}

	renameViaToolbarGivenPath(path);
}

/**
* call delete using the path in the info panel
*/
function dibDeleteFile() {
	var path = $j("#infoAbsPath").val();
	if (path == null) {
		showErrorMessage($j.i18n.prop('msg.path.missing'));
		return false;
	}

	deleteViaToolbarGivenPath(path);
}

/**
* favorite, or 'star' the data object currently displayed in the info view
*/
function dibStarFile() {
	var path = $j("#infoAbsPath").val();
	if (path == null) {
		showErrorMessage($j.i18n.prop('msg.path.missing'));
		return false;
	}

	if (path == null) {
		setErrorMessage("No path was selected, use the tree to select an iRODS collection or file to star"); // FIXME:
		// i18n
		return;
	}

	lcShowBusyIconInDiv("#infoDialogArea");
	var url = "/browse/prepareStarDialog";

	var params = {
		absPath : path
	}

	lcSendValueWithParamsAndPlugHtmlInDiv(url, params, "#infoDialogArea", null);
}

/**
* unfavorite favorite, or 'star' the collection currently displayed in the info view
*/
function dibUnstarFile() {
	var path = $j("#infoAbsPath").val();
	if (path == null) {
		showErrorMessage($j.i18n.prop('msg.path.missing'));
		return false;
	}

	if (path == null) {
		setErrorMessage("No path was selected, use the tree to select an iRODS collection or file to unstar"); // FIXME:
		// i18n
		return;
	}

	showBlockingPanel();
	var url = "/browse/unstarFile";

	var params = {
			absPath : path
	}

	var jqxhr = $j.post(context + url, params,
			function(data, status, xhr) {
			}, "html").success(
			function(returnedData, status, xhr) {
				var continueReq = checkForSessionTimeout(returnedData, xhr);
				if (!continueReq) {
					return false;
				}
				setMessage($j.i18n.prop('msg_file_unstarred'));
				updateBrowseDetailsForPathBasedOnCurrentModel(selectedPath);
				closeStarDialog();
				unblockPanel();
			}).error(function(xhr, status, error) {
				setErrorMessage(xhr.responseText);
				closeStarDialog();
				unblockPanel();
			});
}


		
</script>