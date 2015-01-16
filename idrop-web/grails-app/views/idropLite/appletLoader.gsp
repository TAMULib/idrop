<div id="appletMenu" class="pull-right">
			<button type="button" id="toggleMenuButton"
				value="closeIdropApplet"
				onclick="closeApplet()")>Close iDrop Lite</button>
</div>
<div id="appletLoadDiv">

<!--  area for idrop lite to load -->


</div>

<script type="text/javascript">

function closeApplet() {
	$j("#idropLiteArea").animate({ height: 'hide', opacity: 'hide' }, 'slow');
	$j("#toggleBrowseDataDetailsTable").show('slow');
	$j("#toggleBrowseDataDetailsTable").height="100%";
	$j("#toggleBrowseDataDetailsTable").width="100%";
	dataLayout.resizeAll();
}

</script>
