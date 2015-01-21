<div class="navbar navbar-fixed-top navbar-inverse">
<div class="navbar-inner">
    <a class="brand" style="margin-left: 10px;" href="#">iDrop</a>
   
    <ul class="nav">
    	 <g:ifAuthenticated>
    	 
	    	 <!--  menu items shown if user has been authenticated -->
	    	 
		      <li id="topbarHome" class="topbarItem"><g:link controller="home" action="index"><g:message code="text.home" /></g:link></li>
		      <li id="topbarBrowser" class="topbarItem"><g:link controller="browse" action="index"><g:message code="text.browse" /></g:link></li>
		     
		     <g:ifNotGuestAccount>
			      <g:if test="${grailsApplication.config.idrop.config.use.userprofile==true}">
					 <li id="topbarPreferences" class="topbarItem"><g:link controller="profile" action="index">Profile</g:link></li>
				  </g:if>
			  </g:ifNotGuestAccount>
			 <li id="topbarSearch" class="dropdown">
				 <a href="#" class="dropdown-toggle" data-toggle="dropdown">
      				<g:message code="text.search" /><b class="caret"></b></a>
      				 <ul class="dropdown-menu">
	      					<!--  <li><a href="#" id="searchFileName" onclick="xxx()")>Search By File Name</a></li>  -->
	      					 <li><a href="#" id="searchTag"><g:link controller="tags" action="index"><g:message code="text.tags" /></g:link></li>
	      					<!--  <li><a href="#" id="searchMetadata" onclick="xxx()")>Search By Metadata</a></li>  -->
      				  </ul>
			</li>
			<li id="topbarTools" class="dropdown" style="display: none;">
				 <a href="#" class="dropdown-toggle" data-toggle="dropdown">
      				<g:message code="text.tools" /><b class="caret"></b></a>
      				 <ul class="dropdown-menu">
	      					 <li><a href="https://github.com/DICE-UNC/idrop/wiki/iDrop-Installers" target="_blank">iDrop Desktop</a></li>
	      					  <li id="topbarRule" class="topbarItem"><g:link controller="rule" action="delayExecQueue">User Rules</g:link></li>
      				  </ul>
			</li>
					
		</g:ifAuthenticated>
		
			<li id="topbarAccount" class="dropdown">
				 <a href="#" class="dropdown-toggle" data-toggle="dropdown">
      				<g:message code="text.account" /> <g:ifAuthenticated> <g:ifNotGuestAccount>( <span id="accountZoneAndUserDisplay"><g:accountInfo /></span> )<b class="caret"></b> </g:ifNotGuestAccount></g:ifAuthenticated></a>
      				 <ul class="dropdown-menu">
	      				<g:ifAuthenticated>
	      					 <li><g:link controller="login" action="logout"><g:message code="text.logout" /></g:link></li>
	      					 <g:ifNotGuestAccount>
	      					  	<li><a href="#" id="changePasswordButton"><g:link controller="login" action="changePasswordForm"><g:message code="text.change.password" /></g:link></a></li>
	      					 </g:ifNotGuestAccount>
	      					 <li><a href="#" id="setDefaultResourceButton"><g:link controller="login" action="defaultResource"><g:message code="text.set.default.resource" /></g:link></a></li>
	      				</g:ifAuthenticated>
	      				<g:ifNotAuthenticated>
	      				 <li><g:link href="#" id="loginButton" controller="login" action="login"><g:message code="text.login" /></g:link></li>
	      				</g:ifNotAuthenticated>
      				
      				  </ul>
			</li>
			
			 <g:ifAuthenticated>
			 	  <li id="topbarShoppingCart" class="topbarItem"><g:link class="pull-right" controller="shoppingCart" action="index"><span id="shoppingCartToolbarLabel"><g:message code="text.shopping.cart" /></span></g:link></li>
			 </g:ifAuthenticated>
			 
		</ul>
		
	<div style="float:right; margin-right:20px; margin-top: 10px;">
        <g:message code="text.host" />
  	</div>
		
		
  </div>  
</div>
 <g:ifAuthenticated>
<script>
	var currentZone = "${irodsAccount?.host}";
	var currentZone = "${irodsAccount?.zone}";
	var currentUser = "${irodsAccount?.userName}";
	//$j(function() {
	//	$j("#accountZoneAndUserDisplay").html(host + ":" + currentZone + ":" + currentUser);
	//});
</script>
 </g:ifAuthenticated>






