<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>19trip旅行</title>
<link rel="stylesheet" href="/css/reset-min.css" type="text/css" />
<link rel="stylesheet" href="/css/sreachbar.css" type="text/css"/>
<link rel="stylesheet" href="/css/login.css" type="text/css"/>
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
function timer(){ 
	var old=document.getElementById("mytime").innerText; 
	document.getElementById("mytime").innerText = parseInt(old) - 1; 
	if(parseInt(old)==1){ 
		//window.location = "www.baidu.com";
		window.history.go(-2); //返回注册前的页面
	} 
	settime = setTimeout("timer()",1000); 
} 
</script>
</head>
<body onload="timer()">
<!--以下是头部logo部分 -->
<div id="header">
	<div class="logo" onclick="window.location='/buyTicket/bookIndex.jhtml'">
    <img src="/images/logo.jpg" width="257" height="59" /></div> 
</div>

<!--以下是注册成功页面registerOk内容部分 -->
<div class="reFill_all">
	<h3 class="message_tit">注册新会员</h3>
  	<div class="reFill_con" style="height:220px;">        	
    <dl class="successdl">
		<dt>
			<strong></strong>
		</dt>
		<dd class="successdd">
			<p class="successp">恭喜，您已成功注册！</p>
			<p>为了方便您更好享用19旅行，建议您完善个人资料。&nbsp;&nbsp;&nbsp;&nbsp;<a href="#">我要去完善&gt;&gt;</a></p>
			<p><b id="mytime">10</b>秒后自动为您跳转至之前浏览页面，您也可以点击&nbsp;&nbsp;
			<span class="btn3" onclick="javascript:history.go(-2);">立即跳转</span></p>
		</dd>
	</dl>
    </div>
</div>

<!-- footer start  -->
<%@ include file="/pages/common/footer.jsp"%> 
<!-- footer end --> 

</body>
</html>
