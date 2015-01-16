<head>
<meta name="layout" content="mainNoSidebar" />
</head>
<div id="delayExecQueueDiv">
		<g:render template="ruleDelayExecQueueDetails" />
</div>
<script>
$j(document).ready(function() {

		$j.ajaxSetup({
			cache : false
		});
		$j("#topbarTools").addClass("active");
	});


function deleteRulesBulkAction() {

	var formData = $j("#delayExecForm").serializeArray();
	showBlockingPanel();

	var jqxhr = $j.post(context + "/rule/deleteDelayExecQueue", formData, "html")
			.success(function(returnedData, status, xhr) {
				var continueReq = checkForSessionTimeout(returnedData, xhr);
				if (!continueReq) {
					return false;
				}
				
				setMessage("Delete action successful");
				$j("#delayExecQueueDiv").html(returnedData);
				unblockPanel();
			}).error(function(xhr, status, error) {
				setErrorMessage(xhr.responseText);
				unblockPanel();
			});

}

</script>