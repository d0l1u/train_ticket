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
function timer(user_phone){ 
	var user_phone = $("#userPhone").val();
	var old=document.getElementById("mytime").innerText; 
	document.getElementById("mytime").innerText = parseInt(old) - 1; 
	if(parseInt(old)==1){ 
		window.location = "/login/toUserLogin.jhtml?user_phone="+user_phone+"&version="+new Date();
	} 
	settime = setTimeout("timer()",1000); 
} 
</script>
</head>
<body onload="timer()">
<input type="hidden" id="userPhone" value='${user_phone }' />
<!--以下是头部logo部分 -->
<div id="header">
	<div class="logo" onclick="window.location='/buyTicket/bookIndex.jhtml'">
    <img src="/images/logo.jpg" width="257" height="59" /></div> 
</div>

<!--以下是头部步骤pass_step部分 -->
<div class="pass_step">
	<dl>
    	<dt class="on">输入用户名</dt>
        <dd class="on"></dd>
    </dl>
	<dl>
    	<dt class="on">验证身份</dt>
        <dd class="on"></dd>
    </dl>
	<dl>
    	<dt class="on">重置密码</dt>
        <dd class="on"></dd>
    </dl>
	<dl>
    	<dt class="on">完成</dt>
        <dd class="on"></dd>
    </dl>
</div>


<div class="reFill_all">
	<h3 class="message_tit">重置密码</h3>
  	<div class="reFill_con">
    	<dl class="successdl" style="height:170px;">
			<dt>
                <strong></strong>
			</dt>
            <dd class="successdd">
                <p class="successp">恭喜您，密码修改成功！</p>
            	<p><b id="mytime">5</b> 秒后自动为您跳转至<a href="/login/toUserLogin.jhtml?user_phone=${user_phone }">19旅行登陆页</a></p>
            </dd>
		</dl>  
    </div>
</div>

<!-- footer start  -->
<%@ include file="/pages/common/footer.jsp"%> 
<!-- footer end --> 

</body>
</html>
