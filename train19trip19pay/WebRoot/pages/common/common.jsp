<!-- 19pay提供的解决下拉问题的页面 40 -->
<iframe id="iframeSetHeight" name="iframeSetHeight" src="" width="0" height="0" style="display:none;" ></iframe>
<script type="text/javascript">
function fitHeight(h){
	var windowHeight = h || document.body.scrollHeight+40;
	//alert(windowHeight);
	var urlRecordHeight="http://192.168.32.14:8090/emshop/pages/setiframeheight.jsp?documentHeight="+windowHeight+"&iframeName=framepage"; 
	//var urlRecordHeight="http://dl4.19e.cn/40/pages/common/setiframeheight.jsp?documentHeight="+windowHeight+"&iframeName=portal30_right";
	$("#iframeSetHeight").attr("src",urlRecordHeight);
}
$(document).ready(function() {
	fitHeight();
});
</script>