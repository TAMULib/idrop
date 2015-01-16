/**
 * Javascript for home page and data browser (refactor data browser?)
 * 
 * author: Mike Conway - DICE
 */

/**
 * Global var holds jquery ref to the dataTree
 */

/**
 * ref to the jtree
 */
var dataTree;

/**
 * root path (which could be a multi-element path, such as a home directory
 */

var dataTreeView = "";
var dataTreePath = "";

var browseOptionVal = "info";
var selectedPath = null;
var requestedSelectedPath = null;
var selectedNode = null;
var fileUploadUI = null;
var aclDialogMessageSelector = "#aclDialogMessageArea";
var aclMessageAreaSelector = "#aclMessageArea";
/**
 * presets for url's
 */

var aclUpdateUrl = '/sharing/updateAcl';
var aclAddUrl = '/sharing/addAcl';
var aclAddUserDialogUrl = '/sharing/processAddAclDialog';
var aclDeleteUrl = '/sharing/deleteAcl';
var aclTableLoadUrl = '/sharing/renderAclDetailsTable';
var idropLiteUrl = '/idropLite/appletLoader';
var thumbnailLoadUrl = '/image/generateThumbnail';

var folderAddUrl = '/file/createFolder';
var fileDeleteUrl = '/file/deleteFileOrFolder';
var deleteBulkActionUrl = '/file/deleteBulkAction';
var fileRenameUrl = '/file/renameFile';
var fileMoveUrl = '/file/moveFile';
var fileCopyUrl = '/file/copyFile';
var fileStarUrl = '/browse/starFile'

/**
 * Initialize the tree control for the first view by issuing an ajax directory
 * browser request for the root directory.
 * 
 * type is optional, and can either be root, home, or path root - go to root of
 * dir home - go to user home dir path - set provided path as the base path
 * detect - useful for an initial display will choose based on 'strictACL'
 * setting
 * 
 * @return
 */
function retrieveBrowserFirstView(type, path, pathSelectedInTree) {
	if (dataTree != null) {
		dataTree = null;
		$j("#dataTreeDiv").html("");
	}
	
	// with no type, go into detect mode and show the root
	if (type == null || type == "") {
		type = "detect";
	}
	
	if (type == 'root') {
		path = '';
	} else if (type == 'home') {
		path = "";
	} else if (type == "path") {
		if (path == null || path == "") {
			path = baseAbsPath;
		}
	} else if (type == 'detect') {
		path = "";
	} else {
		throw "invalid type parameter:" + type;
	}


	var state = {};

	state["treeView"] = type;
	state["treeViewPath"] = path;
	
	dataTreeView = type;
	dataTreePath = path;
	requestedSelectedPath = pathSelectedInTree;
	
	//alert("requestedSelectedPath:" + requestedSelectedPath);
	
	$j.bbq.pushState(state);

	var parms = {
		dir : path,
		type : type
	}

	if (dataTree == null) {
		var url = "/browse/ajaxDirectoryListingUnderParent";
		lcSendValueAndCallbackWithJsonAfterErrorCheck(url, parms,
				"#dataTreeDiv", browserFirstViewRetrieved);
	}
}

/**
 * Upon initial load of data for the iRODS tree view, set up the tree control.
 * This will be called as a call back method when the initial AJAX load request
 * returns.
 * 
 * @param data
 *            ajax response from browse controller containing the JSON
 *            representation of the collections and files underneath the given
 *            path
 * @return
 */
function browserFirstViewRetrieved(data) {

	baseAbsPath = data[0].attr.absPath;
	baseAbsPathAsArrayOfPathElements = baseAbsPath.split("/");
	dataTree = $j("#dataTreeDiv")
			.jstree(
					{
						"plugins" : [ "themes", "contextmenu", "json_data",
								"types", "ui", "crrm", "dnd" ],
						"core" : {
							"initially_open" : [ baseAbsPath ]
						},
						"json_data" : {
							"data" : [ data ],

							"progressive_render" : true,
							"ajax" : {
								"url" : context
										+ "/browse/ajaxDirectoryListingUnderParent",
								"cache" : false,
								"data" : function(n) {
									dir = n.attr("id");
									return "type=list&dir="
											+ encodeURIComponent(dir)
											+ incorporatePaging(dir);
								},
								"error" : function(n) {
									if (n.statusText == "success"
											|| n.statusText == "OK") {
										// ok
									} else {
										setMessage(n.responseText);
										return false;
										// refreshTree();
									}
								}
							}
						},
						"contextmenu" : {

							"items" : customMenu
						},
						"types" : {
							"types" : {
								"file" : {
									"valid_children" : "none",
									"icon" : {
										"image" : context + "/images/file.png"
									}
								},
								"folder" : {
									"valid_children" : [ "default", "folder",
											"file" ],
									"icon" : {
										"image" : context
												+ "/images/folder.png"
									}
								}
							}

						},
						"ui" : {
							"select_limit" : 1,
							"initially_select" : [ "phtml_2" ]
						},
						"dnd" : {
							"copy_modifier" : "shift"
						},
						"themes" : {
							"theme" : "default",
							"url" : context + "/css/style.css",
							"dots" : false,
							"icons" : true
						},
						"crrm" : {

						}

					});
	
    
	$j("#dataTreeDiv").bind("loaded.jstree", function(e, data) {
		
		//alert("tree loaded stuff now");
		if (requestedSelectedPath) {
			selectTreePathFromIrodsPath(requestedSelectedPath);
		}
	});
	
		    
	$j("#dataTreeDiv").bind("select_node.jstree", function(e, data) {
		nodeSelected(e, data.rslt.obj);
	});

	$j("#dataTreeDiv").bind("create.jstree", function(e, data) {
		nodeAdded(e, data.rslt.obj);
	});

	$j("#dataTreeDiv").bind("remove.jstree", function(e, data) {
		nodeRemoved(e, data.rslt.obj);
	});

	$j("#dataTreeDiv").bind("rename.jstree", function(e, data) {
		nodeRenamed(e, data);
	});

	$j("#dataTreeDiv").bind("move_node.jstree", function(e, data) {
		var copy = false;
		if (data.args[3] == true) {
			copy = true;
		}

		var targetId = data.args[0].cr[0].id;
		var sourceId = data.args[0].o[0].id;
		var msg = "";
		if (copy) {
			msg += "Copy ";
		} else {
			msg += "Move ";
		}

		msg = msg + " from:" + sourceId + " to:" + targetId;

		var answer = confirm(msg); // FIXME: i18n

		if (!answer) {
			$j.jstree.rollback(data.rlbk);
			return false;
		}

		// move/copy confirmed, process...

		if (copy) {
			copyFile(sourceId, targetId);
		} else {
			moveFile(sourceId, targetId);
		}

	});

	/*
	 * selected path is set if a node in the tree was selected by user. If it's there it's a selection, so show that path in the tree
	 * 
	 * if the selected path is not there, see if I am asking for a particular path to be revealed, this is passed by the index.gsp for
	 * the browse view and is used to initialize the requestedSelectedPath.  In this case, select the path by finding it in the tree,
	 * causing the browse view to also be updated
	 * 
	 * otherwise, just use the root of the tree as the initial selection
	 */
	//alert("path stuff now");
	if (selectedPath) {
		updateBrowseDetailsForPathBasedOnCurrentModel(selectedPath);
	} else if (requestedSelectedPath) {
		// wait and do this after the tree loads, look at above function bound to load event
	} else {
		updateBrowseDetailsForPathBasedOnCurrentModel(baseAbsPath);
	}

}

/**
 * Add ability to display pages for large collections
 * @param dir directory being displayed
 * @returns {String}
 */
function incorporatePaging(dir) {
	if (dir == null) {
		return "";
	}

	//alert("incorporate paging:" + dir);
	return("&partialStart=" + displayIndex + "&splitMode=" + splitMode);
	
}


/**
 * Handling of actions and format of pop-up menu for browse tree.
 * 
 * @param node
 * @returns
 */
function customMenu(node) {

	selectedNode = node;
	selectedPath = node[0].idd;

	// The default set of all items FIXME: i18n
	var items = {
		refreshItem : { // The "refresh" menu item
			label : "Refresh",
			action : function() {
				$j.jstree._reference(dataTree).refresh();
			}
		},
		renameItem : { // The "rename" menu item
			label : "Rename",
			action : function() {
				$j.jstree._reference(dataTree).rename(node);
			}
		},
		deleteItem : { // The "delete" menu item
			label : "Delete",
			action : function() {

				var answer = confirm("Delete selected file?");
				if (answer) {
					$j.jstree._reference(dataTree).remove(node);
				}
			}
		},
		newFolderItem : { // The "new" menu item
			label : "New Folder",
			action : function() {
				$j.jstree._reference(dataTree).create(null, "inside", {
					data : name,
					state : "closed",
					attr : {
						rel : "folder"
					}
				}, function(data) {
				}, false);
			}
		},
		infoItem : { // The "info" menu item
			label : "Info",
			"separator_before" : true, // Insert a separator before the item
			action : function() {
				lcSendValueAndCallbackHtmlAfterErrorCheck(
						"/browse/fileInfo?absPath="
								+ encodeURIComponent(node[0].id), "#infoDiv",
						"#infoDiv", null);
			}
		},
		cutItem : { // The "cut" menu item
			label : "Cut",
			"separator_before" : true, // Insert a separator before the item
			action : function() {
				$j.jstree._reference(dataTree).cut(node[0]);
				setMessage("File cut and placed in clipboard:" + node[0].id);
			}
		},
		copyItem : { // The "copy" menu item
			label : "Copy",
			action : function() {
				$j.jstree._reference(dataTree).copy(node[0]);
				setMessage("File copied and placed in clipboard:" + node[0].id);
			}
		},
		pasteItem : { // The "paste" menu item
			label : "Paste",
			action : function() {
				$j.jstree._reference(dataTree).paste(node[0]);
			}
		}

	};

	return items;
}

/**
 * Event callback when a tree node has finished loading.
 * 
 * @return
 */
function nodeLoadedCallback() {
}

/**
 * called when a tree node is selected. Toggle the node as appropriate, and if
 * necessary retrieve data from iRODS to create the children
 * 
 * @param event
 *            javascript event containing a reference to the selected node
 * @return
 */
function nodeSelected(event, data) {
	// given the path, put in the node data
	var id = data[0].id;
	selectedPath = id;
	selectedNode = data[0];
	updateBrowseDetailsForPathBasedOnCurrentModel(id);
}

/**
 * called when a tree node is added.
 * 
 * @param event
 *            javascript event containing a reference to the selected node
 * @return
 */
function nodeAdded(event, data) {

	var parent = $j.trim(data[0].parentNode.parentNode.id);

	/*
	 * Firefox bug allows add of node outside of tree, so if my parent is
	 * 'dataTreeDiv' then rollback and set an error message
	 */

	if (parent == "dataTreeDiv") {
		$j.jstree._reference(dataTree).refresh();
		setMessage("Cannot create a new folder outside of root of tree, select a tree node first");
		return false;
	}

	var name = $j.trim(data[0].innerText);

	if (name == "") {
		/*
		 * If I can't find the name, this may be Firefox, which seems to behave
		 * strangely, so look for it by manipulating the node I got back
		 */
		var node = $j(data[0]);
		var children = node.children();
		var target = $j(children).filter("a:last-child");
		target = $j(target).html();
		var pos = target.indexOf("</ins>");
		if (pos > -1) {
			name = target.substr(pos + 6);
		}
	}

	var params = {
		parent : parent,
		name : name
	}

	showBlockingPanel();

	var jqxhr = $j.post(context + folderAddUrl, params,
			function(data, status, xhr) {
			}, "html").success(function(returnedData, status, xhr) {
		var continueReq = checkForSessionTimeout(returnedData, xhr);
		if (!continueReq) {
			return false;
		}
		setMessage("new folder created:" + xhr.responseText);
		data[0].id = xhr.responseText;
		updateBrowseDetailsForPathBasedOnCurrentModel(parent);
		unblockPanel();
	}).error(function(xhr, status, error) {
		setErrorMessage(xhr.responseText);
		refreshTree();
		unblockPanel();
	});
}

/**
 * called when a tree node is deleted. Toggle the node as appropriate, and if
 * necessary retrieve data from iRODS to create the children
 * 
 * @param event
 *            javascript event containing a reference to the selected node
 * @return
 */
function nodeRemoved(event, data) {
	// given the path, put in the node data
	var id = data[0].id;

	var params = {
		absPath : id
	}

	showBlockingPanel();

	var jqxhr = $j.post(context + fileDeleteUrl, params,
			function(data, status, xhr) {
			}, "html").success(function(returnedData, status, xhr) {
		var continueReq = checkForSessionTimeout(returnedData, xhr);
		if (!continueReq) {
			return false;
		}
		setMessage("file deleted:" + id);
		selectedPath = xhr.responseText;
		updateBrowseDetailsForPathBasedOnCurrentModel(selectedPath);
		unblockPanel();
	}).error(function(xhr, status, error) {
		setErrorMessage(xhr.responseText);
		refreshTree();
		unblockPanel();
	});
}

/**
 * called when a tree node is renamed. Rename the file in iRODS
 * 
 * @param event
 *            javascript event containing a reference to the selected node
 * @return
 */
function nodeRenamed(event, data) {
	// given the path, put in the node data

	var newName = data.rslt.new_name;
	var prevAbsPath = data.rslt.obj[0].id;

	var params = {
		prevAbsPath : prevAbsPath,
		newName : newName
	}

	showBlockingPanel();

	var jqxhr = $j.post(context + fileRenameUrl, params,
			function(data, status, xhr) {
			}, "html").success(function(returnedData, status, xhr) {
		var continueReq = checkForSessionTimeout(returnedData, xhr);
		if (!continueReq) {
			return false;
		}

		// alert("xhr.response:" + xhr.responseText);
		// alert("new name:" + newName);
		var nodeRenamedTo = xhr.responseText + "/" + newName;

		// safari seems to have a hard time handling rename to self, this check
		// traps that
		if (xhr.responseText == prevAbsPath) {
			setMessage("Rename ignored, name was not changed");
		} else {
			setMessage("file renamed to:" + nodeRenamedTo);
			selectedPath = nodeRenamedTo;
			data.rslt.obj[0].id = nodeRenamedTo;
			data.rslt.obj[0].abspath = nodeRenamedTo;
			// refresh this node
			$j.jstree._reference(dataTree).refresh(data.rslt.obj[0]);
			updateBrowseDetailsForPathBasedOnCurrentModel(nodeRenamedTo);
		}

		// reloadAndSelectTreePathBasedOnIrodsAbsolutePath(nodeRenamedTo);
		unblockPanel();
	}).error(function(xhr, status, error) {
		setErrorMessage(xhr.responseText);
		refreshTree();
		unblockPanel();
	});

}

/**
 * Given a source and target absolute path, do a move
 * 
 * @param sourcePath
 * @param targetPath
 */
function moveFile(sourcePath, targetPath) {

	if (sourcePath == null || targetPath == null) {
		alert("cannot move, source and target path must be specified"); // FIXME:
		// i18n
		return;
	}

	var params = {
		sourceAbsPath : sourcePath,
		targetAbsPath : targetPath
	}

	showBlockingPanel();

	var jqxhr = $j.post(context + fileMoveUrl, params,
			function(data, status, xhr) {
			}, "html").success(function(returnedData, status, xhr) {
		var continueReq = checkForSessionTimeout(returnedData, xhr);
		if (!continueReq) {
			return false;
		}
		setMessage("file moved to:" + xhr.responseText);
		selectedPath = targetPath;

		/*
		 * delete the node from the tree, select the parent node and update the
		 * display to the parent node
		 */
		refreshTree();
		unblockPanel();
	}).error(function(xhr, status, error) {
		setErrorMessage(xhr.responseText);
		refreshTree();
		unblockPanel();
	});
}

/**
 * Given a source and target absolute path, do a copy
 * 
 * @param sourcePath
 * @param targetPath
 */
function copyFile(sourcePath, targetPath) {

	if (sourcePath == null || targetPath == null) {
		alert("cannot copy, source and target path must be specified"); // FIXME:
		// i18n
		return;
	}

	var params = {
		sourceAbsPath : sourcePath,
		targetAbsPath : targetPath
	}

	showBlockingPanel();

	var jqxhr = $j.post(context + fileCopyUrl, params,
			function(data, status, xhr) {
			}, "html").success(function(returnedData, status, xhr) {
		var continueReq = checkForSessionTimeout(returnedData, xhr);
		if (!continueReq) {
			return false;
		}
		setMessage("file copied to:" + xhr.responseText);
		unblockPanel();

		// refreshTree();
		reloadAndSelectTreePathBasedOnIrodsAbsolutePath(targetPath);
		// updateBrowseDetailsForPathBasedOnCurrentModel(targetPath);

	}).error(function(xhr, status, error) {
		setErrorMessage(xhr.responseText);
		refreshTree();
		unblockPanel();
	});
}

/**
 * On selection of a browser mode (from the top bar of the browse view), set the
 * option such that selected directories in the tree result in the given view in
 * the right hand pane
 */
function setBrowseMode() {
	browseOptionVal = $j("#browseDisplayOption").val();
	updateBrowseDetailsForPathBasedOnCurrentModel(selectedPath);
}

/**
 * Upon selection of a collection or data object from the tree, display the
 * content on the right-hand side. The type of detail shown is contingent on the
 * 'browseOption' that is set in the drop-down above the browse area.
 */
function updateBrowseDetailsForPathBasedOnCurrentModel(absPath) {

	if (absPath == null) {
		absPath = baseAbsPath;
	}

	if (browseOptionVal == null || browseOptionVal == "") {
		browseOptionVal = "info";
	}

	setPathCrumbtrail(absPath);

	if (browseOptionVal == "browse") {
		showBrowseView(absPath);
	} else if (browseOptionVal == "info") {
		showInfoView(absPath);
	} else if (browseOptionVal == "gallery") {
		showGalleryView(absPath);
	}
}

/**
 * Show the browse view
 * 
 * @param absPath
 *            absolute path to browse to
 */
function showBrowseView(absPath) {

	if (absPath == null) {
		absPath = baseAbsPath;
	}
	
	var state = {};
	state["absPath"] = absPath;
	state["browseOptionVal"] = "browse";
	$j.bbq.pushState(state);


        showBlockingPanel();

	lcShowBusyIconInDiv("#infoDiv");


	var jqxhr = $j.get(
			context + "/browse/displayBrowseGridDetails?absPath="
					+ encodeURIComponent(absPath), null,
			function(data, status, xhr) {
			}, "html").success(function(data, status, xhr) {
		var continueReq = checkForSessionTimeout(data, xhr);
		if (!continueReq) {
			return false;
		}

		$j("#infoDiv").html(data);
                unblockPanel();
	}).error(function(xhr, status, error) {
		setInfoDivNoData();
                unblockPanel();
    });
}

/**
 * Show the audit view specifying a target div
 * 
 * @param absPath
 * @returns {Boolean}
 */
function showAuditView(absPath, targetDiv) {
	if (absPath == null) {
		absPath = baseAbsPath;
	}

	if (targetDiv == null) {
		targetDiv = "#infoDiv";

	}

	try {

		lcSendValueAndCallbackHtmlAfterErrorCheckThrowsException(
				"/audit/auditList?absPath=" + encodeURIComponent(absPath),
				targetDiv, function(data) {
					// alert("data is:" + data);
					$j(targetDiv).html(data);
				}, function() {
					setInfoDivNoData();
				});
	} catch (err) {
		setInfoDivNoData();
	}

	/*
	 * lcSendValueAndCallbackHtmlAfterErrorCheckPreserveMessage(
	 * "/audit/auditList?absPath=" + encodeURIComponent(absPath), targetDiv,
	 * targetDiv, null);
	 */

}

/**
 * Set a no data message in the div
 */
function setInfoDivNoData() {
	$j("#infoDiv").html("<h2>No data to display</h2>"); // FIXME: i18n

}

/**
 * Show the sharing view
 * 
 * @param absPath
 * @returns {Boolean}
 */
function showSharingView(absPath, targetDiv) {
	if (absPath == null) {
		absPath = baseAbsPath;
	}

	if (targetDiv == null) {
		targetDiv = "#infoDiv";
	}

	try {

		lcSendValueAndCallbackHtmlAfterErrorCheckThrowsException(
				"/sharing/showAclDetails?absPath="
						+ encodeURIComponent(absPath), targetDiv,
				function(data) {
					// alert("data is:" + data);
					$j(targetDiv).html(data);
				}, function() {
					setInfoDivNoData();
				});
	} catch (err) {
		setInfoDivNoData();
	}

}

/**
 * Show the metadata view
 * 
 * @param absPath
 * @returns {Boolean}
 */
function showMetadataView(absPath, targetDiv) {
	if (absPath == null) {
		absPath = baseAbsPath;
	}

	if (targetDiv == null) {
		targetDiv = "#infoDiv";
		// I am not embedded, so manipulate the toolbars
	}

	try {

		lcSendValueAndCallbackHtmlAfterErrorCheckThrowsException(
				"/metadata/showMetadataDetails?absPath="
						+ encodeURIComponent(absPath), targetDiv,
				function(data) {
					// alert("data is:" + data);
					$j(targetDiv).html(data);
				}, function() {
					setInfoDivNoData();
				});
	} catch (err) {
		setInfoDivNoData();
	}

	/*
	 * 
	 * lcSendValueAndCallbackHtmlAfterErrorCheckPreserveMessage(
	 * "/metadata/showMetadataDetails?absPath=" + encodeURIComponent(absPath),
	 * targetDiv, targetDiv, null);
	 */

}

/**
 * Show the info view
 * 
 * @param absPath
 * @returns {Boolean}
 */
function showInfoView(absPath) {
	if (absPath == null) {
		absPath = baseAbsPath;
	}
	
	var state = {};
	state["absPath"] = absPath;
	state["browseOptionVal"] = "info";
	$j.bbq.pushState(state);


	
	lcSendValueAndCallbackHtmlAfterErrorCheckPreserveMessage(
			"/browse/fileInfo?absPath=" + encodeURIComponent(absPath),
			"#infoDiv", "#infoDiv", null);
}

/**
 * Show the gallery view
 * 
 * @param absPath
 * @returns {Boolean}
 */
function showGalleryView(absPath) {
	if (absPath == null) {
		absPath = baseAbsPath;
	}
	

	var state = {};
	state["absPath"] = absPath;
	state["browseOptionVal"] = "gallery";
	$j.bbq.pushState(state);
	targetDiv = "#infoDiv";

	try {

		lcSendValueAndCallbackHtmlAfterErrorCheckThrowsException(
				"/browse/galleryView?absPath=" + encodeURIComponent(absPath),
				targetDiv, function(data) {
					// alert("data is:" + data);
					$j("#infoDiv").html(data);
				}, function() {
					setInfoDivNoData();
				});
	} catch (err) {
		setInfoDivNoData();
	}

}

/**
 * Show the ticket view
 * 
 * @param absPath
 * @returns {Boolean}
 */
function showTicketView(absPath, targetDiv) {
	if (absPath == null) {
		absPath = baseAbsPath;
	}

	if (targetDiv == null) {
		targetDiv = "#infoDiv";
	}

	try {

		lcSendValueAndCallbackHtmlAfterErrorCheckThrowsException(
				"/ticket/index?absPath=" + encodeURIComponent(absPath),
				targetDiv, function(data) {
					// alert("data is:" + data);
					$j(targetDiv).html(data);
				}, function() {
					setInfoDivNoData();
				});
	} catch (err) {
		setInfoDivNoData();
	}

}

/*
* Show the rule view
* 
* @param absPath
* @returns {Boolean}
*/
function showRuleView(absPath, targetDiv) {
	if (absPath == null) {
		absPath = baseAbsPath;
	}

	if (targetDiv == null) {
		targetDiv = "#infoDiv";
	}

	try {

		lcSendValueAndCallbackHtmlAfterErrorCheckThrowsException(
				"/rule/index?absPath=" + encodeURIComponent(absPath),
				targetDiv, function(data) {
					// alert("data is:" + data);
					$j(targetDiv).html(data);
				}, function() {
					setInfoDivNoData();
				});
	} catch (err) {
		setInfoDivNoData();
	}

}

/**
 * Show the dialog to allow upload of data in quick upload mode
 */
function showQuickUploadDialog() {
	
	var url = "/file/prepareQuickUploadDialog";
	

	lcSendValueWithParamsAndPlugHtmlInDiv(url, null, "", function(data) {
		fillInQuickUploadDialog(data);
	});

}

/**
 * Show the dialog to allow upload of data
 */
function showUploadDialog() {
	if (selectedPath == null) {
		alert("No path was selected, use the tree to select an iRODS collection to upload the file to");
		return;
	}

	showUploadDialogUsingPath(selectedPath);

}

/**
 * Show the dialog to upload from the browse details view
 */
function showBrowseDetailsUploadDialog() {
	var path = $j("#browseDetailsAbsPath").val();
	showUploadDialogUsingPath(path);
}

/**
 * Show the dialog to allow upload of data
 * 
 * @param path
 *            path of collection to upload to
 */
function showUploadDialogUsingPath(path) {

	if (path == null) {
		setErrorMessage("No path was selected, use the tree to select an iRODS collection to upload the file to");
		return;
	}

	var url = "/file/prepareUploadDialog";
	var params = {
		irodsTargetCollection : path
	}

	lcSendValueWithParamsAndPlugHtmlInDiv(url, params, "", function(data) {
		fillInUploadDialog(data);
	});
}

/**
 * On load of upload dialog, this will be called when the pre-set data is
 * available
 * 
 * @param data
 */
function fillInUploadDialog(data) {

	if (data == null) {
		return;
	}

	$j('#uploadDialog').remove();

	var $jdialog = $j('<div id="uploadDialog"></div>').html(data).dialog({
		autoOpen : false,
		modal : true,
		width : 500,
		title : 'Upload to iRODS',
		create : function(event, ui) {
			initializeUploadDialogAjaxLoader();
		}
	});

	$jdialog.dialog('open');
}

/**
 * On load of quick upload dialog, this will be called when the pre-set data is
 * available
 * 
 * @param data
 */
function fillInQuickUploadDialog(data) {

	if (data == null) {
		return;
	}

	$j('#uploadDialog').remove();

	var $jdialog = $j('<div id="uploadDialog"></div>').html(data).dialog({
		autoOpen : false,
		modal : true,
		width : 500,
		title : 'Upload to iRODS',
		create : function(event, ui) {
			initializeQuickUploadDialogAjaxLoader();
		}
	});

	$jdialog.dialog('open');
}


/**
 * Create the upload dialog for web (http) uploaded.
 */
function initializeUploadDialogAjaxLoader() {

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
							setMessage("Upload complete");

							/*
							 * Find the uploaded file name, refresh the parent
							 * node and select the file If for some reason a
							 * psychotic quirk won't let me do this, just reload
							 * the parent
							 */

							var uploadParent = $j("#collectionParentName").val();

							if (uploadParent != null) {
								reloadAndSelectTreePathBasedOnIrodsAbsolutePath(uploadParent);
								updateBrowseDetailsForPathBasedOnCurrentModel(uploadParent);
							} else {
								reloadAndSelectTreePathBasedOnIrodsAbsolutePath(selectedPath);
								updateBrowseDetailsForPathBasedOnCurrentModel(selectedPath);
							}

							$j('#uploadDialog').dialog('close');
							$j('#uploadDialog').remove();

						},
						onError : function(event, files, index, xhr, handler) {
							setErrorMessage(xhr.responseText);
						}
					});

}

/**
 * Create the quick upload dialog for web (http) uploaded.
 */
function initializeQuickUploadDialogAjaxLoader() {

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
							setMessage("Upload complete");

							$j('#uploadDialog').dialog('close');
							$j('#uploadDialog').remove();

						},
						onError : function(event, files, index, xhr, handler) {
							setErrorMessage(xhr.responseText);
						}
					});

}

/**
 * Called by data table upon submit of an acl change
 */
function aclUpdate(value, settings, userName) {

	if (selectedPath == null) {
		throw "no collection or data object selected";
	}

	lcShowBusyIconInDiv(messageAreaSelector);

	var params = {
		absPath : selectedPath,
		acl : value,
		userName : userName
	}

	var jqxhr = $j.post(context + aclAddUrl, params,
			function(data, status, xhr) {
				lcClearDivAndDivClass(messageAreaSelector);
			}, "html").error(function(xhr, status, error) {
		setErrorMessage(xhr.responseText);
	}).complete(function() {
		setMessage("File sharing update successful");
	});

	return value;

}

/**
 * Prepare the dialog to allow create of ACL data
 */
function prepareAclDialog(isNew) {

	if (selectedPath == null) {
		setErrorMessage("No path is selected, Share cannot be set");
		return;
	}

	if (isNew == null) {
		isNew = true;
	}

	var url = "/sharing/prepareAclDialog";
	var params = {
		absPath : selectedPath,
		create : true
	}

	lcSendValueWithParamsAndPlugHtmlInDiv(url, params, "", function(data) {
		showAclDialog(data);
	});

}

/**
 * The ACL dialog is prepared and ready to display as a JQuery dialog box, show
 * it
 * 
 * @param data
 */
function showAclDialog(data) {
	$j("#aclDetailsArea").hide("slow");
	$j("#aclDialogArea").html(data).show("slow");

}

/**
 * Submit the ACL dialog form to create a new ACL. The dialog will either have a
 * single user name, or it may have a pick list to send. If it has neither, it's
 * an error.
 */
function submitAclDialog() {

	var permissionVal = $j('[name=acl]').val();
	if (permissionVal == null || permissionVal == "" || permissionVal == "NONE") {
		setErrorMessage("Please select a permission value in the drop-down");
		return false;
	}

	if (selectedPath == null) {
		setErrorMessage("no collection or data object selected");
		return false;
	}

	// see if there is form data (users in a pick list) that are selected
	var formData = $j("#userDialogForm").serializeArray();

	if (formData == null) {
		setErrorMessage("Please select a user to share data with");
		return false;
	}

	var isCreate = $j('[name=isCreate]').val();

	showBlockingPanel();

	var jqxhr = $j.post(context + aclAddUserDialogUrl, formData,
			function(data, status, xhr) {
			}, "html").success(function(data, status, xhr) {
		var continueReq = checkForSessionTimeout(data, xhr);
		if (!continueReq) {
			return false;
		}

		reloadAclTable(selectedPath);
		closeAclAddDialog();
		setMessage("Sharing permission saved successfully"); // FIXME:
		// i18n
		unblockPanel();

	}).error(function(xhr, status, error) {
		setErrorMessage(xhr.responseText);
		unblockPanel();
	});
}

/**
 * Close the dialog for adding ACL's, reshow the acl details area
 */
function closeAclAddDialog() {
	try {
		$j("#aclDialogArea").hide("slow", new function() {
			$j("#aclDialogArea").html("");
			$j("#aclDetailsArea").css('display', 'block');
			$j("#aclDetailsArea").show("slow");
		});

	} catch (e) {

	}

}

/**
 * Retrieve the Acl information from iRODS for the given path as an HTML table,
 * this will subsequently be turned into a JTable.
 * 
 * Note that this clears the acl message area, so it should be called before
 * setting any message if used in any methods that update the acl area.
 * 
 * @param absPath
 */
function reloadAclTable(absPath) {

	lcClearDivAndDivClass(aclMessageAreaSelector);

	$j("#aclTableDiv").empty();
	lcShowBusyIconInDiv("#aclTableDiv");

	var params = {
		absPath : absPath
	}

	var jqxhr = $j.get(context + aclTableLoadUrl, params,
			function(data, status, xhr) {

			}, "html").error(function(xhr, status, error) {
		setErrorMessage(xhr.responseText);
	}).success(function(data, status, xhr) {
		var continueReq = checkForSessionTimeout(data, xhr);
		if (!continueReq) {
			return false;
		}
		$j('#aclTableDiv').html(data);
		buildAclTableInPlace();
	});

}

/**
 * Given an acl details html table, wrap it in a jquery dataTable
 */
function buildAclTableInPlace() {
	tableParams = {
		"bJQueryUI" : true,
		"bLengthChange" : false,
		"bFilter" : false,
		"iDisplayLength" : 500,
		"aoColumns" : [ {
			'sWidth' : '20px',
			'bSortable' : false
		}, {
			'sWidth' : '30px'
		}, {
			'sWidth' : '40px'
		}

		]

	}

	dataTable = lcBuildTableInPlace("#aclDetailsTable", null, null, tableParams);

	try {

		$j('.forSharePermission', dataTable.fnGetNodes()).editable(
				function(value, settings) {
					var userName = this.parentNode.getAttribute('id');
					return aclUpdate(value, settings, userName);
				}, {
					"callback" : function(sValue, y) {
						var aPos = dataTable.fnGetPosition(this);
						dataTable.fnUpdate(sValue, aPos[0], aPos[1]);
					},
					'data' : "{'OWN':'OWN','READ':'READ','WRITE':'WRITE'}",
					'type' : 'select',
					'submit' : 'OK',
					'cancel' : 'Cancel',
					'onblur' : 'ignore',
					'indicator' : 'Saving'
				});
	} catch (e) {
		// ignore
	}
}

/**
 * Deprecated
 * 
 * @param userName
 * @param permission
 */
function addRowToAclDetailsTable(userName, permission) {
	var idxs = $j("#aclDetailsTable")
			.dataTable()
			.fnAddData(
					[
							"<input id=\"selectedAcl\" type=\"checkbox\" name=\"selectedAcl\">",
							userName, permission ], true);
	var newNode = $j("#aclDetailsTable").dataTable().fnGetNodes()[idxs[0]];
	$j(newNode).attr("id", userName);
}

/**
 * Delete share selected in details dialog toolbar, send the data to delete the
 * selected elements
 */
function deleteAcl() {

	if (!confirm('Are you sure you want to delete?')) {
		setMessage("Delete cancelled"); // FIXME:
		// i18n
		return;
	}

	var formFields = $j("#aclDetailsForm").serializeArray();
	var pathInfo = new Object();
	pathInfo.name = "absPath";
	pathInfo.value = selectedPath;

	formFields.push(pathInfo);

	var jqxhr = $j.post(context + aclDeleteUrl, formFields,
			function(data, status, xhr) {

			}, "html").error(function(xhr, status, error) {
		setErrorMessage(xhr.responseText);
	}).success(function(data, status, xhr) {
		var continueReq = checkForSessionTimeout(data, xhr);
		if (!continueReq) {
			return false;
		}
		reloadAclTable(selectedPath);
		setMessage("Delete successful"); // FIXME:
		// i18n
	});
}

function buildFormFromACLDetailsTable() {
	var formData = $j("#aclDetailsForm").serializeArray();
	formData.push({
		name : 'absPath',
		value : selectedPath
	});
	return formData;
}

/**
 * Close the iDrop lite applet area
 */
function closeIdropUploadrArea() {
	$j("#idropUploadrArea").animate({
		height : 'hide'
	}, 'slow');
	$j("#toggleHtmlArea").show('slow');
	$j("#toggleHtmlArea").height = "100%";
	$j("#toggleHtmlArea").width = "100%";
	//$j("#idropUploadrArea").empty();

	if (selectedPath == "" || selectedPath == null) {
		// ignore reload tree
	} else {
		reloadAndSelectTreePathBasedOnIrodsAbsolutePath(selectedPath);
	}
}

/**
 * Display the iDrop lite gui, passing in the given irods base collection name
 */
function showIdropLite() {

	var myPath = selectedPath;
	if (selectedPath == null) {
		myPath = "/";
	}

	showIdropLiteGivenPath(myPath, 2);
}

/**
 * Display the iDrop lite gui, passing in the given irods base collection name
 */
function showIdropLiteLocalAndIrods() {

	var myPath = selectedPath;
	if (selectedPath == null) {
		myPath = "/";
	}

	showIdropLiteGivenPath(myPath, 1);
}

/**
 * Display the iDrop lite gui, passing in the given irods base collection name,
 * from the browseDetails view
 */
function showBrowseDetailsIdropLite() {

	var path = selectedPath;// $j("#browseDetailsAbsPath").val();

	if (path == null) {
		path = "/";
	}

	showIdropLiteGivenPath(path, 2);
}

/**
 * Display the iDrop lite gui, displaying in a local tree/irods tree view
 * (display mode 1)
 */
function showBrowseDetailsIdropLiteLocalAndIrods() {

	var path = selectedPath;

	if (path == null) {
		path = "/";
	}
	showIdropLiteGivenPath(path, 1);
}

/**
 * Given a path which is the parent collection to display in iDrop lite, show
 * the iDrop-lite applet
 * 
 * @param path
 *            parent path to which files will be uploaded in iDrop-lite
 * @param displayMode
 *            1=local/irods tree, 2=bulk upload
 */
function showIdropLiteGivenPath(path, displayMode) {
	var idropLiteSelector = "#idropUploadrArea";
	if (path == null) {
		alert("No path was selected, use the tree to select an iRODS collection to upload the file to");
		return;
	}

	// close the shopping cart mode if open
	closeShoppingCartApplet();

	// first hide Browse Data Details table
	$j("#toggleHtmlArea").hide('slow');
	$j("#toggleHtmlArea").width = "0%";
	$j("#toggleHtmlArea").height = "0%";
	
	var params = {
		absPath : path
	}
}

/**
 * Ask for a thumbnail image for a selected path to be displayed on an info
 * panel. These use a consistent naming scheme for the various divs and data
 * elements.
 */
function requestThumbnailImageForInfoPane() {
	var absPath = $j("#infoAbsPath").val();
	absPath = encodeURIComponent(absPath);
	var url = scheme + "://" + host + ":" + port + context + thumbnailLoadUrl
			+ "?absPath=" + absPath;
	var oImg = document.createElement("img");
	oImg.setAttribute('src', url);
	oImg.setAttribute('alt', 'na');
	oImg.setAttribute('class', 'thumb');
	$j("#infoThumbnailLoadArea").append(oImg);

}

/**
 * Refresh the browse tree
 */
function refreshTree() {
	$j.jstree._reference(dataTree).refresh();
}

/**
 * The download button has been selected from an info view, download the given
 * file
 */
function downloadViaToolbar() {
	var infoAbsPath = $j("#infoAbsPath").val();
	window.open(context + '/file/download' + escape(infoAbsPath), '_self');

}

/**
 * Do a donwload action with a provided path
 * 
 * @param path
 */
function downloadViaToolbarGivenPath(path) {
	if (path == null) {
		showErrorMessage(jQuery.i18n.prop('msg.path.missing'));
		return false;
	}
	
	showBlockingPanel();

	var params = {
		absPath : path
	}

	var jqxhr = $j
			.post(context + "/file/screenForDownloadRights", params, null, "html")
			.success(
					function(returnedData, status, xhr) {
						var continueReq = checkForSessionTimeout(
								returnedData, xhr);

						if (!continueReq) {
							return false;
						}

						unblockPanel();
						doActualDownload(path);
						

					}).error(function(xhr, status, error) {
						setErrorMessage(xhr.responseText);
						unblockPanel();
			});
}

function doActualDownload(path) {
	
	if (path == null) {
		setErrorMessage(jQuery.i18n.prop('msg.path.missing'));
		return false;
	}
	
	path = escape(path);
	path = path.replace('+', '%2B');
	window.open(context + '/file/download?path=' + escape(path), '_self');
}

/**
 * The rename button has been selected from an info view, show the rename dialog
 */
function renameViaToolbar() {
	var infoAbsPath = $j("#infoAbsPath").val();
	renameViaToolbarGivenPath(infoAbsPath);
}

/**
 * The rename button has been selected from the browse details view, show the
 * rename dialog
 */
function renameViaBrowseDetailsToolbar() {
	// var path = $j("#browseDetailsAbsPath").val();
	renameViaToolbarGivenPath(selectedPath);
}

/**
 * Given a path for the file/collection to be renamed, show the rename dialog
 * 
 * @param path
 */
function renameViaToolbarGivenPath(path) {

	if (path == null) {
		setErrorMessage("No path was selected, use the tree to select an iRODS collection or file to rename"); // FIXME:
		// i18n
		return;
	}

	lcShowBusyIconInDiv("#infoDialogArea");
	var url = "/browse/prepareRenameDialog";

	var params = {
		absPath : path
	}

	lcSendValueWithParamsAndPlugHtmlInDiv(url, params, "#infoDialogArea", null);

}

/**
 * Delete was selected on the toolbar
 */
function deleteViaToolbar() {

	var infoDivAbsPath = $j("#infoAbsPath").val();

	if (infoDivAbsPath != null) {
		deleteViaToolbarGivenPath(infoDivAbsPath);
	}

}

/**
 * Delete was selected on the browse details toolbar
 */
function deleteViaBrowseDetailsToolbar() {
	deleteViaToolbarGivenPath(selectedPath);
}

/**
 * Delete was selected from a toolbar, process given theabsolute path to delete
 * 
 * @param path
 *            absolute path to delete
 */
function deleteViaToolbarGivenPath(path) {

	if (path == null) {
		showErrorMessage(jQuery.i18n.prop("msg.path.missing"));
		// i18n
		return false;
	}

	var answer = confirm("Delete selected file?"); // FIXME: i18n

	if (answer) {

		showBlockingPanel();

		var params = {
			absPath : path
		}

		var jqxhr = $j
				.post(context + fileDeleteUrl, params, null, "html")
				.success(
						function(returnedData, status, xhr) {
							var continueReq = checkForSessionTimeout(
									returnedData, xhr);

							if (!continueReq) {
								return false;
							}

							setMessage("file deleted:" + path);
							var selectedPath = xhr.responseText;
							reloadAndSelectTreePathBasedOnIrodsAbsolutePath(selectedPath);
							unblockPanel();

						}).error(function(xhr, status, error) {
					// refreshTree();
					setErrorMessage(xhr.responseText);
					unblockPanel();
				});
	}
}

/**
 * new folder was selected from the toolbar
 */
function newFolderViaToolbar() {
	var infoAbsPath = $j("#infoAbsPath").val();
	newFolderViaToolbarGivenPath(infoAbsPath);
}

/**
 * new folder was selected from the browse details toolbar
 */
function newFolderViaBrowseDetailsToolbar() {
	newFolderViaToolbarGivenPath(selectedPath);
}

/**
 * Given a path, show the new folder dialog
 * 
 * @param path
 *            path for the parent of the new folder.
 */
function newFolderViaToolbarGivenPath(path) {

	if (path == null) {
		alert("No path was selected, use the tree to select an iRODS collection to upload the file to");
		return;
	}

	lcShowBusyIconInDiv("#infoDialogArea");
	var url = "/browse/prepareNewFolderDialog";

	var params = {
		absPath : path
	}

	lcSendValueWithParamsAndPlugHtmlInDiv(url, params, "#infoDialogArea", null);
}

/**
 * Close the rename dialog that would have been opened by pressing the 'rename'
 * button on the toolbar.
 */
function closeRenameDialog() {
	$j("#renameDialog").dialog('close');
	$j("#renameDialog").remove();
}

/**
 * Close the star dialog that would have been opened by pressing the 'rename'
 * button on the toolbar.
 */
function closeStarDialog() {
	$j("#starDialog").dialog('close');
	$j("#starDialog").remove();
}

/**
 * Close the ne folder dialog that would have been opened by pressing the 'new
 * folder' button on the toolbar.
 */
function closeNewFolderDialog() {
	$j("#newFolderDialog").dialog('close');
	$j("#newFolderDialog").remove();
}

/**
 * Process a rename operation requested from the toolbar by processing the
 * submitted rename dialog
 */
function submitRenameDialog() {
	lcClearDivAndDivClass("#renameDialogMessageArea");
	var absPath = $j("#renameDialogAbsPath").val();
	var parentPath = $j("#renameDialogParentPath").val();
	var newName = $j.trim($j("#fileName").val());
	// name must be entered
	if (newName == null || newName.length == 0) {
		setMessage("Please enter a new name");
		return;
	}

	var params = {
		prevAbsPath : absPath,
		newName : newName
	}

	closeRenameDialog();
	showBlockingPanel();

	var jqxhr = $j.post(context + fileRenameUrl, params,
			function(data, status, xhr) {
			}, "html").success(function(returnedData, status, xhr) {
		var continueReq = checkForSessionTimeout(returnedData, xhr);
		if (!continueReq) {
			return false;
		}
		setMessage("file renamed to:" + xhr.responseText);
		selectedPath = xhr.responseText;

		// refreshTree();
		reloadAndSelectTreePathBasedOnIrodsAbsolutePath(parentPath);
		// selectTreePathFromIrodsPath(selectedPath);
		updateBrowseDetailsForPathBasedOnCurrentModel(selectedPath);
		unblockPanel();
	}).error(function(xhr, status, error) {
		refreshTree();
		setErrorMessage(xhr.responseText);
		unblockPanel();
	});

}

/**
 * Process a new folder operation requested from the toolbar by processing the
 * submitted new folder dialog
 */
function submitNewFolderDialog() {

	lcClearDivAndDivClass("#newFolderDialogMessageArea");
	var absPath = $j.trim($j("#newFolderDialogAbsPath").val());
	var newName = $j.trim($j("#fileName").val());
	// name must be entered
	if (newName == null || newName.length == 0) {
		setErrorMessage("Please enter a new folder name");
		return;
	}

	var params = {
		parent : absPath,
		name : newName
	}

	closeNewFolderDialog();
	showBlockingPanel();

	var jqxhr = $j.post(context + folderAddUrl, params,
			function(data, status, xhr) {
			}, "html").success(function(returnedData, status, xhr) {
		var continueReq = checkForSessionTimeout(returnedData, xhr);
		if (!continueReq) {
			return false;
		}
		setMessage("New folder created:" + xhr.responseText);
		// selectedPath = xhr.responseText;

		// refresh the parent node and open
		addANodeToTheParentInTheTree(absPath, newName);
		// refreshTree();
		updateBrowseDetailsForPathBasedOnCurrentModel(absPath);
		unblockPanel();

	}).error(function(xhr, status, error) {
		refreshTree();
		setErrorMessage(xhr.responseText);
		unblockPanel();
	});

}

/**
 * Delete files based on inputs in the browse details table. Note that
 * confirmation has already been provided.
 */
function deleteFilesBulkAction() {

	var formData = $j("#browseDetailsForm").serializeArray();
	showBlockingPanel();

	var jqxhr = $j.post(context + deleteBulkActionUrl, formData, "html")
			.success(function(returnedData, status, xhr) {
				var continueReq = checkForSessionTimeout(returnedData, xhr);
				if (!continueReq) {
					return false;
				}
				refreshTree();
				updateBrowseDetailsForPathBasedOnCurrentModel(selectedPath);
				setMessage("Delete action successful");
				unblockPanel();
			}).error(function(xhr, status, error) {
				setErrorMessage(xhr.responseText);
				unblockPanel();
			});

}

function addANodeToTheParentInTheTree(parentAbsolutePath, childRelativeName) {

	if (parentAbsolutePath == null || parentAbsolutePath.length == 0) {
		throw ("no path provided, cannot add a node to the tree");
	}

	if (childRelativeName == null || childRelativeName.length == 0) {
		throw ("no childRelativeName provided, cannot add a node to the tree");
	}

	// handle add under root
	if (parentAbsolutePath == "/") {

		var childAbsolutepathUnderRoot = childRelativeName;

		// find and open the parent node, then add the child to it
		splitPathAndPerformOperationAtGivenTreePath(parentAbsolutePath, null,
				null, function(path, dataTree, currentNode) {

					if ($j.jstree._reference(dataTree)._is_loaded(currentNode)) {

						// parent node was already loaded, so it makes sense to
						// add
						// the node to
						// the parent

						var icon = "folder";
						var state = "closed";
						var type = "folder";

						var attrBuf = new Object();
						attrBuf.id = "/" + childAbsolutepathUnderRoot;
						attrBuf.rel = type;
						attrBuf.absPath = "/" + childAbsolutepathUnderRoot;

						var nodeProps = new Object();
						nodeProps.data = childAbsolutepathUnderRoot;
						nodeProps.attr = attrBuf;
						nodeProps.state = state;

						$j.jstree._reference(dataTree).create_node(currentNode,
								"inside", nodeProps, null, false);

					}

					// select and open this new node
					selectTreePathFromIrodsPath(childAbsolutepathUnderRoot);

				});

	} else {

		var childAbsolutePath = parentAbsolutePath + "/" + childRelativeName;

		// find and open the parent node, then add the child to it
		splitPathAndPerformOperationAtGivenTreePath(parentAbsolutePath, null,
				null, function(path, dataTree, currentNode) {

					if ($j.jstree._reference(dataTree)._is_loaded(currentNode)) {

						// parent node was already loaded, so it makes sense to
						// add
						// the node to
						// the parent

						var icon = "folder";
						var state = "closed";
						var type = "folder";

						var attrBuf = new Object();
						attrBuf.id = childAbsolutePath;
						attrBuf.rel = type;
						attrBuf.absPath = childAbsolutePath;

						var nodeProps = new Object();
						nodeProps.data = childRelativeName;
						nodeProps.attr = attrBuf;
						nodeProps.state = state;

						$j.jstree._reference(dataTree).create_node(currentNode,
								"inside", nodeProps, null, false);

					}

					// select and open this new node
					selectTreePathFromIrodsPath(childAbsolutePath);

				});
	}
}

/**
 * Given an iRODS absolute path to a node, find and select that node
 * 
 * @param irodsAbsolutePath
 *            irods absolute path is a string with a path, in the form /a/path/to/something
 */
function selectTreePathFromIrodsPath(irodsAbsolutePath) {

	if (irodsAbsolutePath == null || irodsAbsolutePath.length == 0) {
		throw "irodsAbsolutePath is missing";
	}

	if (irodsAbsolutePath == "/") {
		return false;
	}
	
	var splitPath = irodsAbsolutePath.split("/");

	selectTreePath(splitPath, null, null);

}

/**
 * Find the given iRODS absolute path in the tree, clear the children and reload
 * 
 * @param path
 */
function refreshOpenNodeAtAbsolutePath(path) {

	if (path == null) {
		throw "No path provided";
	}

	splitPath = path.split("/");

	performOperationAtGivenTreePath(splitPath, null, null, function(thisPath,
			dataTree, currentNode) {

		$j.jstree._reference(dataTree).refresh(currentNode);
		$j.jstree._reference(dataTree).select_node(currentNode, true);

	});
}

/**
 * Find the given iRODS absolute path in the tree, clear the children and reload
 * 
 * @param path
 */
function reloadAndSelectTreePathBasedOnIrodsAbsolutePath(path) {

	if (path == null) {
		throw "No path provided";
	}

	splitPath = path.split("/");

	performOperationAtGivenTreePath(splitPath, null, null, function(thisPath,
			dataTree, currentNode) {

		$j.jstree._reference(dataTree).refresh(currentNode);
		$j.jstree._reference(dataTree).open_node(currentNode);
		$j.jstree._reference(dataTree).select_node(currentNode, true);

	});
}

/**
 * Find the given iRODS absolute path in the tree, clear the children and
 * reload, do not select or open
 * 
 * @param path
 */
function reloadTreePathBasedOnIrodsAbsolutePath(path) {

	if (path == null) {
		throw "No path provided";
	}

	splitPath = path.split("/");

	performOperationAtGivenTreePath(splitPath, null, null, function(thisPath,
			dataTree, currentNode) {

		$j.jstree._reference(dataTree).refresh(currentNode);

	});
}

/**
 * Given the tree path in the text box, recursively open the nodes in the tree
 * based on the path.
 * 
 * If called with no parameters, it assumes this is the top node, it will open
 * the top node.
 * 
 * If called with a last
 * 
 * 
 * @param lastIndex
 * @param currentNode
 * @returns {Boolean}
 */
function selectTreePath(path, currentNode, currentIndex) {

	if (currentIndex == null) {
		currentIndex = 0;
	}

	if (path == null) {
		var val = $j("#searchTerm").val();
		if (val == "") {
			setMessage("enter a path to search");
			return false;
		}

		if (val.charAt(val.length - 1) == "/") {
			val = val.substring(0, val.length - 1);
		}

		// alert("select tree path:" + val);
		path = val.split("/");
	}

	performOperationAtGivenTreePath(path, null, null, function(path, dataTree,
			currentNode) {
		$j.jstree._reference(dataTree).open_node(currentNode);
		
		// see if this node should be selected by comparing paths
		
		if (currentNode != null && currentNode[0] != null && currentNode[0].id != null) {
			var currentId = currentNode[0].id;
			var idPath = currentId.split("/");
			if (path.length == idPath.length) {
				$j.jstree._reference(dataTree).select_node(currentNode, true);
			}
		}
		
		
		
	});
}

/**
 * among the children in the given tree node, find the node who's title is the
 * given target path
 * 
 * @param childNodes
 * @param targetPath
 * @returns
 */
function getPathInNode(childNodes, targetPath) {
	var foundChild = null;
	var nodeText = null;
	$j.each(childNodes, function(index, value) {
		var theChild = $j.jstree._reference(dataTree)._get_node(value);
		nodeText = $j.jstree._reference(dataTree).get_text(theChild);

		if (nodeText == targetPath) {
			foundChild = theChild;
			return;
		} else if (nodeText == "/" && targetPath == "") {
			// this matches the root node
			foundChild = theChild;
			return;
		}

	});

	return foundChild;

}

/**
 * Given an iRODS absolute path in string form, find that path in the tree and
 * perform the given operation on that path. The passed in function wil be
 * called with the params(path, tree, currentNode)
 * 
 * @param path
 * @param currentNode
 * @param currentIndex
 * @param operationToPerform
 */
function splitPathAndPerformOperationAtGivenTreePath(path, currentNode,
		currentIndex, operationToPerform) {
	splitPath = path.split("/");
	performOperationAtGivenTreePath(splitPath, currentNode, currentIndex,
			operationToPerform);
}

/**
 * Given the tree path in the text box, call the given function, passing in the
 * path array, tree and the current node as arugments to the function
 * 
 * @param path
 *            array of strings for each part of the path
 * @param currentNode
 *            current tree node (especially if calling recursively, this will
 *            match the 'lastIndex' in the path
 * @param lastIndex
 *            ponter to position in tree path array that is an absolute path to
 *            the curentNode
 * @param operationToPerform
 *            a function(path, tree, currentNode) that will be called when at
 *            the end of the path
 * @returns {Boolean}
 */
function performOperationAtGivenTreePath(path, currentNode, currentIndex,
		operationToPerform) {

	if (dataTree == null) {
		return false;
	}

	if (currentIndex == null) {
		currentIndex = 0;
	}

	if (path == null) {
		var val = $j("#searchTerm").val();

		// alert("select tree path:" + val);
		path = val.split("/");
	}

	// if called with no params, get the root node, open it, and process the
	// children
	if (currentNode == null) {
		currentNode = $j.jstree._reference(dataTree).get_container();
		var children = $j.jstree._reference(dataTree)._get_children(currentNode);
		currentNode = children[0];
		// fix for a bug in ie9 that surfaces on initial load, otherwise does
		// infinite recursion...
		if (currentNode == null) {
			// alert("currentNode is null");
			return;
		} else if (currentNode == undefined) {
			// alert("currentNode is undefined");
			return;
		}

		if (path.length == 2) {
			if (path[0] == "" && path[1] == "") {
				// working under root of tree, add right there
				operationToPerform(path, dataTree, currentNode.children[2]);
				return false;
			}
		}

		performOperationAtGivenTreePath(path, currentNode, currentIndex,
				operationToPerform);
		return;
	}

	var skip = false;
	var end = false;
	$j.each(path,
			function(index, value) {
				if (skip) {
					return;
				}

				if (index < currentIndex) {
					return;
				}

				if (value == "") {
					return;
				}

				/**
				 * I might have a root that is not really '/', I could have a
				 * tree root that is a node inside of the actual iRODS tree. Use
				 * the baseAbsPathAsArrayOfPathElements to account for this
				 */
				if (baseAbsPath.length > 1
						&& index < (baseAbsPathAsArrayOfPathElements.length)) {
					return;
				}

				// } else {

				// if (value > "") {
				var loaded = $j.jstree._reference(dataTree)._is_loaded(
						currentNode);
				if (!loaded) {
					skip = true;
					$j.jstree._reference(dataTree)
							.open_node(
									currentNode,
									function() {
										performOperationAtGivenTreePath(path,
												currentNode, index,
												operationToPerform);
									}, false);
					return;
				} else {
					$j.jstree._reference(dataTree).open_node(currentNode, false,
							false);
				}

				var children = $j.jstree._reference(dataTree)._get_children(
						currentNode);
				currentNode = getPathInNode(children, value);
				if (currentNode == null) {
					//setMessage("Path not found in tree, please reload");
					return false;
				} /*
					 * else { if (index == path.length - 1) { end = true; } }
					 */
				// }
			});

	// need to set end to true if current node is a preset dir /x/y/z and
	// matches path

	if (currentNode != null) { // && end) {
		operationToPerform(path, dataTree, currentNode);
	}

}

/**
 * Process hashChange events through the bbq plug in for back button support for the browser
 * @param state
 * @returns {Boolean}
 */
function processStateChange(state) {
	var statePath = state["absPath"];
	var view = state["browseOptionVal"];
	var treeView = state["treeView"];
	var treeViewPath = state["treeViewPath"];

	var tempTreeViewFromGlobal = dataTreeView;
	var tempTreePathFromGlobal = dataTreePath;
	
	if (treeView != null) {
		if (treeView == tempTreeViewFromGlobal && tempTreePathFromGlobal == treeViewPath) {
			// no alteration of tree view
		} else {
			retrieveBrowserFirstView(treeView, treeViewPath);
			return false;
		}
	}

	if (statePath && view == null && browseOptionVal == "info") {
		browseOptionVal = "info";
		selectTreePathFromIrodsPath(statePath);
	} else if (view != browseOptionVal && statePath == selectedPath) {
		// view change only
		browseOptionVal = view;
		updateBrowseDetailsForPathBasedOnCurrentModel(selectedPath);
	} else if (statePath != selectedPath) {
		if (statePath != null) {
			browseOptionVal = view;
			selectTreePathFromIrodsPath(statePath);
		}
	}

}

/**
 * Set the default storage resource in the IRODSAccount for subsequent transfers
 */
function setDefaultStorageResource(resource) {
	if (resource == null) {
		return false;
	}

	var params = {
		resource : resource
	}

	showBlockingPanel();

	var jqxhr = $j.post(context + "/browse/setDefautlResourceForAccount",
			params, "html").success(function(returnedData, status, xhr) {
		var continueReq = checkForSessionTimeout(returnedData, xhr);
		if (!continueReq) {
			return false;
		}
		setMessage(jQuery.i18n.prop('msg_resource_changed'));
		unblockPanel();
	}).error(function(xhr, status, error) {
		setErrorMessage(xhr.responseText);
		unblockPanel();
	});

}

/**
 * Set the root of the tree to the user home directory and reload
 */
function setTreeToUserHome() {
	retrieveBrowserFirstView("home", "");

}

/**
 * set the root of the tree to the given path and reload
 */

function setTreeToGivenPath(path) {

	if (path == null || path == "") {
		path = "/";
	}

	retrieveBrowserFirstView("path", path);

}

/**
 * Set the root of the tree to the user home directory and reload
 */
function setTreeToRoot() {
	retrieveBrowserFirstView("root", "");

}

/* click twistie to open details table info */
function browseDetailsClick(minMaxIcon) {

	var parentOfIcon = minMaxIcon.parentNode;

	/*
	 * if (parentOfIcon == null) { alert("parentOfIcon is null!"); } else {
	 * alert("parentOfIcon is:" + parentOfIcon); }
	 */

	var nTr = parentOfIcon.parentNode;

	/*
	 * if (nTr == null) { alert("nTr is null!"); } else { alert("nTr is:" +
	 * nTr); }
	 */

	if (minMaxIcon.parentNode.innerHTML.match('circle-minus')) {
		lcCloseTableNodes(dataTable);
	} else {
		try {
			browseDataDetailsFunction(minMaxIcon, nTr);
		} catch (err) {
			console.log("error in detailsClick():" + err);
		}

	}
}

/**
 * called by browseDetailsClick() when it is decided that the details table row
 * should be opened, go to server and get the details.
 */
function browseDataDetailsFunction(clickedIcon, rowActionIsOn) {
	/* Open this row */
	lcCloseTableNodes(dataTable);
	// nTr points to row and has absPath in id
	var absPath = $j(rowActionIsOn).attr('id');
	// alert("absPath:" + absPath);
	var detailsId = "details_" + absPath;
	var detailsHtmlDiv = "details_html_" + absPath;
	var buildDetailsLayoutVal = buildDetailsLayout(detailsId);
	clickedIcon.setAttribute("class", "ui-icon ui-icon-circle-minus");
	newRowNode = dataTable.fnOpen(rowActionIsOn, buildDetailsLayoutVal,
			'details');
	newRowNode.setAttribute("id", detailsId);
	askForBrowseDetailsPulldown(absPath, detailsId)

}

/**
 * The table row is being opened, and the query has returned from the server
 * with the data, fill in the table row
 */
function buildDetailsLayout(detailsId) {
	var td = document.createElement("TD");
	td.setAttribute("colspan", "4");

	var detailsPulldownDiv = document.createElement("DIV");
	detailsPulldownDiv.setAttribute("id", detailsId);
	detailsPulldownDiv.setAttribute("class", "detailsPulldown");
	var img = document.createElement('IMG');
	img.setAttribute("src", context + "/images/ajax-loader.gif");
	detailsPulldownDiv.appendChild(img);
	td.appendChild(detailsPulldownDiv);
	return $j(td).html();
}

function askForBrowseDetailsPulldown(absPath, detailsId) {

	var url = "/browse/miniInfo";
	absPath = absPath;
	var params = {
		absPath : absPath
	}

	lcSendValueWithParamsAndPlugHtmlInDiv(url, params, ".details", null);

}

function clickOnPathInBrowseDetails(data) {
	if (data == null) {
		throw new Exception("no absolute path provided");
	}
	// show main browse tab

	splitPathAndPerformOperationAtGivenTreePath(data, null, null, function(
			path, dataTree, currentNode) {

		$j.jstree._reference(dataTree).open_node(currentNode);
		$j.jstree._reference(dataTree).select_node(currentNode, true);

	});
}

/**
 * Close the public link dialog
 */
function closePublicLinkDialog() {

	$j("#browseDialogArea").hide("slow");
	$j("#browseDialogArea").html();
}


/**
 * Set a no data message in the div
 */
function setInfoDivNoData() {
	$j("#infoDiv").html("<h2>No data to display</h2>"); // FIXME: i18n

}

/**
 * Process a star operation requested from the toolbar by processing the
 * submitted dialog
 */
function submitStarDialog() {
	var absPath = $j("#absPath").val();
	var description = $j.trim($j("#description").val());

	var params = {
		absPath : absPath,
		description : description
	}

	showBlockingPanel();

	var jqxhr = $j.post(context + fileStarUrl, params,
			function(data, status, xhr) {
			}, "html").success(function(returnedData, status, xhr) {
		var continueReq = checkForSessionTimeout(returnedData, xhr);
		if (!continueReq) {
			return false;
		}
		setMessage(jQuery.i18n.prop('msg_file_starred'));
		updateBrowseDetailsForPathBasedOnCurrentModel(selectedPath);
		closeStarDialog();
		unblockPanel();
	}).error(function(xhr, status, error) {
		setErrorMessage(xhr.responseText);
		closeStarDialog();
		unblockPanel();
	});

}

/*
* Cause a dialog to appear that can create a share for this folder
*/
function addShareAtPath() {
	$j("#sharingPanelContainingDiv").html();
	var path = selectedPath;
	if (selectedPath == null) {
		return false;
	}

	var url = "/sharing/prepareAddShareDialog";
	var params = {
		absPath : path
	}

	lcSendValueWithParamsAndPlugHtmlInDiv(url, params, "", function(data) {
		fillInShareDialog(data);
	});
}


/*
* Edit the share at the given path
*/
function editShareAtPath() {
	$j("#sharingPanelContainingDiv").html();
	var path = $j("#infoAbsPath").val();
	if (path == null) {
		setMessage(jQuery.i18n.prop('msg_path_missing'));
		unblockPanel();		
		return false;
	}

	var url = "/sharing/prepareEditShareDialog";
	var params = {
		absPath : path
	}

	lcSendValueWithParamsAndPlugHtmlInDiv(url, params, "", function(data) {
		fillInShareDialog(data);
	});
}

/**
 * remove a named share
 */
function removeShareAtPath() {
	var path = $j("#aclDetailsAbsPath").val();
	
	if (path == null) {
		setMessage(jQuery.i18n.prop('msg_path_missing'));
		unblockPanel();		
		return false;
	}
	
	var params = {
			absPath : path
		}
		
	var answer = confirm(jQuery.i18n.prop('msg_delete_share')); 
	
	if (!answer) {
		return false;
	}
	
	showBlockingPanel();
	
	var jqxhr = $j.post(context + "/sharing/removeShare", params,
			function(data, status, xhr) {
		
		var continueReq = checkForSessionTimeout(data, xhr);
		if (!continueReq) {
			return false;
		}
		
		$j("#sharingPanelContainingDiv").empty().append( data );
		unblockPanel();

	}).fail(function(xhr, status, error) {
		setErrorMessage(xhr.responseText);
		unblockPanel();
	});

}

/*
* put content into the share dialog
*/
function fillInShareDialog(data) {

	$j("#sharingPanelContainingDiv").html(data);
	
}

/**
 * Submit the dialog to add or update a share
 */
function updateNamedShare() {
	var path = $j("#aclDetailsAbsPath").val();
	var shareName = $j("#shareName").val();
	var formAction = $j("#formAction").val();
	showBlockingPanel();
	
	if (path == null) {
		setMessage(jQuery.i18n.prop('msg_path_missing'));
		unblockPanel();		
		return false;
	}
	
	if (formAction == null) {
		setErrorMessage(jQuery.i18n.prop('msg_action_missing'));
		unblockPanel();
		return false;
	}
	
	
	var params = {
			absPath : path,
			shareName: shareName,
			formAction : formAction
		}
		
	var jqxhr = $j.post(context + "/sharing/processUpdateShareDialog", params,
			function(data, status, xhr) {
		
		var continueReq = checkForSessionTimeout(data, xhr);
		if (!continueReq) {
			return false;
		}
		
		$j("#sharingPanelContainingDiv").empty().append( data );
		unblockPanel();

	}).fail(function(xhr, status, error) {
		setErrorMessage(xhr.responseText);
		unblockPanel();
	});

}

/**
 * Close the add/update share dialog by reloading the share info
 */
function closeNamedShareDialog() {
	
		var path = selectedPath;
		if (selectedPath == null) {
			return false;
		}

		var url = "/sharing/getSharingDialogInfo";
		var params = {
			absPath : path
		}

		lcSendValueWithParamsAndPlugHtmlInDiv(url, params, "#sharingPanelContainingDiv", null);
		
}

/**
 * Close the add/update share dialog
 */
function closeShareDialog() {
	
		$j("#aclDialogArea").hide("slow");
		$j("#aclDialogArea").html();
		$j("#aclDetailsArea").show("slow");
}

/*
* Cause a dialog to appear that has a link for a public path for the current path
*/
function makePublicLinkAtPath() {
	$j("#aclDialogArea").html();
	var path = selectedPath;
	if (selectedPath == null) {
		return false;
	}

	// show the public link dialog
	var url = "/browse/preparePublicLinkDialog";
	var params = {
		absPath : path
	}

	lcSendValueWithParamsAndPlugHtmlInDiv(url, params, "", function(data) {
		fillInACLDialog(data);
	});
}

/**
 * Close the public link dialog 
 */
function closePublicLinkDialog() {
	
		$j("#aclDialogArea").hide("slow");
		$j("#aclDialogArea").html();
		var path = $j("#publicLinkDialogAbsPath").val();
		reloadAclTable(path);
		$j("#aclDetailsArea").show("slow");
}

/**
 * Grant public (anonymous access) via the public link dialog.  Submit dialog and present the response
 */
function grantPublicLink() {
	var path = $j("#publicLinkDialogAbsPath").val();
	showBlockingPanel();
	if (path == null) {
		setMessage(jQuery.i18n.prop('msg.path.missing'));
		unblockPanel();		
	}
	
	var params = {
			absPath : path
		}
	
	var jqxhr = $j.get(context + "/browse/updatePublicLinkDialog", params,
			function(data, status, xhr) {
		
		var continueReq = checkForSessionTimeout(data, xhr);
		if (!continueReq) {
			return false;
		}
		
		$j("#publicLinkDialog").empty().append( data );
		unblockPanel();

	}).fail(function(xhr, status, error) {
		setErrorMessage(xhr.responseText);
		unblockPanel();
	});
}
        
/*
*Given the contents of the 'create public link' dialog, 
*/
function fillInACLDialog(data) {
	$j("#aclDetailsArea").hide("slow");
	$j("#aclDialogArea").html(data);
	$j("#aclDialogArea").show("slow");
}

