<head>

<g:javascript library="codemirror" />
<g:javascript library="addon/search/searchcursor" />
<g:javascript library="codemirror-ui" />


<g:javascript library="clike" />
<link rel="stylesheet" href="${resource(dir:'css',file:'codemirror.css')}" />
<link rel="stylesheet" href="${resource(dir:'css',file:'codemirror-ui.css')}" />

<link rel="stylesheet" href="${resource(dir:'css',file:'eclipse.css')}" />

</head>
<h3><a ><g:message code="text.rule" /></a></h3>
<div id="detailsTopSection">

	<div id="detailsToolbar" >
			<button type="button" id="reloadRuleButton" class="ruleEdit"
				value="reloadRule"
				onclick="callReloadRule()">
				<i class="icon-refresh"></i><g:message code="default.button.reload.label" />
			</button>
			<span id="saveRuleButton"><button type="button" id="saveRuleButton" class="ruleEdit"
				value="saveRule"
				onclick="callSaveRule()"><i class="icon-ok"></i>
				<g:message code="text.update" />
			</button></span>
				<span id="runRuleButton"><button type="button" id="runRuleButton"
				value="runRule"
				onclick="callRunRule()"><i class="icon-play"></i>
				<g:message code="text.run.rule" />
			</button></span>
			</button></span>
				<span id="showRuleButton"><button hidden type="button" id="showRuleButton" class="ruleResultView"
				value="showRule"
				onclick="callShowRule()"><i class="icon-edit"></i>
				<g:message code="text.edit" />
			</button></span>
		</div>
	</div>

	<div id="ruleDetailDiv">
		<!-- div for audit table -->
		<g:render template="/rule/ruleDetails" />
	</div>
	
	<div id="ruleResultDiv">
		<!--  result of rule exec -->
	
	
	</div>
	
	<script type="text/javascript">
	

	function deleteInputParam(param) {

		if (!param) {
			return false;
		}

		var absPath = $j("#ruleAbsPath").val();
		if (absPath == null || absPath == "") {
			setErrorMessage("no absPath for rule");
			return false;
		}
		
		var params = {
				ruleAbsPath : absPath,
				inputParamName : param
				}

		
		var jqxhr = $j.post(context + "/rule/deleteRuleInputParameter", params, "html").success(
				function(returnedData, status, xhr) {
					var continueReq = checkForSessionTimeout(returnedData, xhr);
					if (!continueReq) {
						return false;
					}
					setMessage("Parameter deleted");
					$j("#ruleDetailDiv").html(returnedData);
				}).error(function(xhr, status, error) {
			setErrorMessage(xhr.responseText);
		});
	}


	function deleteOutputParam(param) {
		var absPath = $j("#ruleAbsPath").val();
		if (absPath == null || absPath == "") {
			setErrorMessage("no absPath for rule");
			return false;
		}
		
		var params = {
				ruleAbsPath : absPath,
				outputParamName : param
				}

		
		var jqxhr = $j.post(context + "/rule/deleteRuleOutputParameter", params, "html").success(
				function(returnedData, status, xhr) {
					var continueReq = checkForSessionTimeout(returnedData, xhr);
					if (!continueReq) {
						return false;
					}
					setMessage("Parameter deleted");
					$j("#ruleDetailDiv").html(returnedData);
				}).error(function(xhr, status, error) {
			setErrorMessage(xhr.responseText);
		});	}

	function callSaveRule() {
		editor.mirror.save();
		var formData = $j("#ruleDetailsForm").serializeArray();
		var jqxhr = $j.post(context + "/rule/updateRule", formData, "html").success(
				function(returnedData, status, xhr) {
					var continueReq = checkForSessionTimeout(returnedData, xhr);
					if (!continueReq) {
						return false;
					}
					setMessage("rule saved");
					$j("#ruleDetailDiv").html(returnedData);
				}).error(function(xhr, status, error) {
			setErrorMessage(xhr.responseText);
		});

		
	}

	function callShowRule() {

		$j("#ruleDetailDiv").show("slow");
		$j(".ruleEdit").show("slow");
		$j("#ruleResultDiv").html("");
		$j("#ruleResultDiv").hide("slow");
		$j(".ruleResultView").hide("slow");
		
		}

	function callReloadRule(absPath) {
		var absPath = $j("#ruleAbsPath").val();
		if (absPath == null || absPath == "") {
			showError("no absPath for rule");
			return false;
		}
		
		var params = {
				absPath : absPath

				}
		var jqxhr = $j.get(context + "/rule/reloadRule", params, "html").success(
				function(returnedData, status, xhr) {
					var continueReq = checkForSessionTimeout(returnedData, xhr);
					if (!continueReq) {
						return false;
					}
					$j("#ruleDetailDiv").html(returnedData);
				}).error(function(xhr, status, error) {
			setErrorMessage(xhr.responseText);
		});

		
	}

	function callRunRule() {
		editor.mirror.save();
		var formData = $j("#ruleDetailsForm").serializeArray();
		$j("#ruleDetailDiv").hide("slow");
		$j(".ruleEdit").hide("slow");
		$j("#ruleResultDiv").html("");
		$j("#ruleResultDiv").show("slow");
		$j(".ruleResultView").show("slow");

		showBlockingPanel();
		
		var jqxhr = $j.post(context + "/rule/runRule", formData, "html").success(
				function(returnedData, status, xhr) {
					var continueReq = checkForSessionTimeout(returnedData, xhr);
					if (!continueReq) {
						unblockPanel();
						return false;
					}
					$j("#ruleResultDiv").html(returnedData);
					unblockPanel();
					 

					
				}).error(function(xhr, status, error) {
					unblockPanel();
					
			setErrorMessage(xhr.responseText);
		});
	}

	function callAddInputParameter() {
		
		var absPath = $j("#ruleAbsPath").val();
		if (absPath == null || absPath == "") {
			setErrorMessage("no absPath for rule");
			return false;
		}
		
		var params = {
				ruleAbsPath : absPath
				}
		var url = "/rule/addRuleInputParameterDialog";

		lcSendValueWithParamsAndPlugHtmlInDiv(url, params, "#addParamDialogDiv", null);
	}

	function callAddOutputParameter() {
		var absPath = $j("#ruleAbsPath").val();
		if (absPath == null || absPath == "") {
			setErrorMessage("no absPath for rule");
			return false;
		}
		
		var params = {
				ruleAbsPath : absPath
				}
		var url = "/rule/addRuleOutputParameterDialog";

		lcSendValueWithParamsAndPlugHtmlInDiv(url, params, "#addParamDialogDiv", null);
	}

	
	</script>
