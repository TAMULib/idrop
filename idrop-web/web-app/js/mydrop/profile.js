/**
 * Javascript for profile functions
 */


/**
 * Update the profile information
 */
function updateUserProfile() {

	/*
	var params = {
			nickName : $j("#nickName").val(),
			description : $j("#description").val(),
			email : $j("#email").val()
		}
*/
		showBlockingPanel();
		$j("#userProfileForm").submit();

		/*
		var jqxhr = $j.post(context + "/profile/updateProfile", params,
				function(data, status, xhr) {
				}, "html").success(function(returnedData, status, xhr) {
			var continueReq = checkForSessionTimeout(returnedData, xhr);
			if (!continueReq) {
				return false;
			}
			setMessage(jQuery.i18n.prop('msg_profile_update_successful'));
			$j("#profileDataArea").html(returnedData);
			unblockPanel();
		}).error(function(xhr, status, error) {
			setErrorMessage(xhr.responseText);
			unblockPanel();
		});
		*/
}


/**
 * load the profile details information
 */
function loadProfileData() {
	/*var targetDiv = "#profileDataArea";
	lcSendValueAndCallbackHtmlAfterErrorCheckPreserveMessage(
			"/profile/loadProfileData",
			targetDiv, targetDiv, null);*/
	 window.location=context + '/profile/index';
}



