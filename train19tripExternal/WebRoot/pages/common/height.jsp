<script type="text/javascript" language="JavaScript">	
	$().ready(function(){
		var iframeSetHeightPath='${sessionScope.iframeSetHeightPath}';
		var windowHeight = document.body.scrollHeight+30;
	    var urlRecordHeight=iframeSetHeightPath+"/40/pages/common/setiframeheight.jsp?documentHeight="+windowHeight+"&iframeName=portal30_right";
	    $("#iframeSetHeight").attr("src",urlRecordHeight);
	});
	
	function changeHeight(len){
		try{
	    	var path = $("#iframeSetHeight").attr("src");
	    	var begin = path.indexOf("documentHeight=") + ("documentHeight=".length);
	    	var end = path.indexOf("&iframeName=");
	    	var height = parseInt(path.substring(begin, end)) + len;
	    	var prePath = path.substring(0, begin);
	    	var sufPath = path.substring(end);
	    	$("#iframeSetHeight").attr("src",prePath+height+sufPath);
    	}catch(e){}
    }
</script>

<iframe id="iframeSetHeight" name="iframeSetHeight" src="" width="0" height="0" style="display:none;" ></iframe>
