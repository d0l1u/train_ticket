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
<script type="text/javascript" src="/js/jquery.cookie.js"></script>
<script type="text/javascript">
//登录处的js
$(document).ready(function() {  
	if ($.cookie("rmbUser") == "true") {
		$("#rmbUser").attr("checked", true);  
	    $("#user_phone").val($.cookie("user_phone"));  
	    $("#user_password").val($.cookie("user_password")); 
	    if($.trim($("#user_password").val())!=""){
			document.getElementById('user_password').style.display = "block";
			document.getElementById('user_password_text').style.display = "none";
        } 
	}
	if($("#user_phone_pwd").val()!=""){
		$("#user_phone").val($("#user_phone_pwd").val());
	}
}); 
//密码框
function changeLogin(obj){ 
    if($("#user_password").is(":visible") && $.trim($("#user_password").val())!=""){
        return;
    } 
	obj.style.display = "none";  
	if(obj.type=="text")  {    
		document.getElementById('user_password').style.display = "block";    
		document.getElementById('user_password').focus();//加上    
		if(this.value==''||this.value=='请输入密码'){this.value='请输入密码';}
	}else{     
		document.getElementById('user_password_text').style.display = "block";  
		if(this.value=='请输入密码'){this.value='';}   
	}  
} 
//提交登录  
function submitLoginForm(){
	var user_phone = $.trim($("#user_phone").val());  
	var user_password = $.trim($("#user_password").val());
	var sessionVal = "no";
	//保存用户信息    两星期
	if($("#rmbUser").attr("checked") == "checked") {  
		$.cookie("rmbUser", "true", {expires : 14});
		$.cookie("user_phone", user_phone, {expires : 14});
		$.cookie("user_password", user_password, {expires : 14});
		sessionVal = "yes";
	} else {
	    $.cookie("rmbUser", "false");  
	    $.cookie("user_phone", null);  
	    $.cookie("user_password", null);  
	}
	//校验手机号码
	if(user_phone=="" || user_phone=="请输入手机号码"){
		$("#user_phone").focus();
		$("#user_phone_verify").addClass("land_div_p");
		$("#user_phone_verify").html("<span></span>手机号码不能为空！");
		return false;
	}else if(!/^1(3[\d]|4[57]|5[\d]|8[\d]|7[08])\d{8}$/g.test(user_phone)){
		$("#user_phone").focus();
		$("#user_phone_verify").addClass("land_div_p");
		$("#user_phone_verify").html("<span></span>请填写正确的手机号！");
		return false;
	}else{
		$("#user_phone_verify").removeClass("land_div_p");
		$("#user_phone_verify").text("");
	}
	//校验登录密码  
	if(user_password=="" || user_password=="请输入密码"){
		$("#user_password").focus();
		$("#user_password_verify").addClass("land_div_p");
		$("#user_password_verify").html("<span></span>登录密码不能为空！");
		return;
	}else{
		$("#user_password_verify").removeClass("land_div_p");
		$("#user_password_verify").text("");
	}
	//数据库校验用户的账号和密码是否存在
	if(user_phone!="" && user_phone!="请输入手机号码" && user_password!="" && user_password!="请输入密码"){
		var url = "/login/userLogin.jhtml?user_phone="+user_phone+"&user_password="+user_password+"&sessionVal="+sessionVal+"&version="+new Date();
		$.post(url,function(data){
			if(data == "yes"){
				//登陆成功进入火车票首页
				window.location="/index.jsp?version="+new Date();
				$("#user_password_verify").removeClass("land_div_p");
				$("#user_password_verify").html("");
			}else{
				$("#user_password_verify").addClass("land_div_p");
				$("#user_password_verify").html("<span></span>用户名或密码错误！");
			}
		});
	}
}
</script>
</head>
<body>
<!--以下是头部logo部分 -->
<div id="header">
	<div class="logo" onclick="window.location='/buyTicket/bookIndex.jhtml'"><img src="/images/logo.jpg" width="257" height="59" /></div> 
</div>

<form id="land_form" action="/login/userLogin.jhtml" method="post">
<input type="hidden" id="user_phone_pwd" value="${user_phone }" />
<div class="login_con">
	<div class="login_ban">
	</div>
    <div class="land_con">
    	<div class="land_tit">
        	<b>会员登陆</b>
            <a href="/login/toUserRegister.jhtml">免费注册&gt;&gt;</a>
    	</div>
			
        <div class="land_div">
        	<dl style="margin:0;">
        		<dt></dt>
        		<dd>
                	<input type="text" id="user_phone" name="user_phone" value="请输入手机号码" onfocus="if(this.value=='请输入手机号码'){this.value=''};" 
    					onblur="if(this.value==''||this.value=='请输入手机号码'){this.value='请输入手机号码';}" />
                </dd>
        	</dl>
            <p id="user_phone_verify"><span></span></p>

        </div>
            
        <div class="land_div">
        	<dl  style="margin:0;">
        		<dt class="lock"></dt>
        		<dd>
                	<input type="text" id="user_password_text" name="user_password_text" value="请输入密码" onfocus="changeLogin(this);" />
    				<input type="password" id="user_password" name="user_password" value="" style="display:none;"
    					onblur="changeLogin(this);" onkeydown="if (event.keyCode==13) submitLoginForm();" />
                </dd>
        	</dl>
            <p id="user_password_verify"><span></span></p>
        </div>
            
        <div class="free_login">
            <input id="rmbUser" type="checkbox"  />两周内免登录
            <a href="/login/forgetPwd.jhtml">忘记密码？</a>        
        </div>
            
        <div class="btn4" onclick="submitLoginForm();">登&nbsp;&nbsp;&nbsp;陆</div>
    </div>
</div>
</form>

<!-- footer start  -->
<%@ include file="/pages/common/footer.jsp"%> 
<!-- footer end --> 

</body>
</html>
