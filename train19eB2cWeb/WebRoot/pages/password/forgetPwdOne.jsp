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
//定义全局变量
var userPhone = false;//手机号码
var userCode = false;//验证码
//校验手机号码
function user_phone_reg_info(){
	var user_phone_reg = $.trim($("#user_phone_reg").val()); 
	if(user_phone_reg=="" || user_phone_reg=="请输入正确的手机号码"){
		//$("#user_phone_reg").focus();
		$("#user_phone_reg_info").html("<span></span>手机号码不能为空");
		userPhone = false;
		return;
	}else if(!/^1(3[\d]|4[57]|5[\d]|8[\d]|7[08])\d{8}$/g.test(user_phone_reg)){
		$("#user_phone_reg_info").html("<span></span>请填写正确的手机号码");
		userPhone = false;
		return;
	}else{//看手机号码是否已经注册过
		var url = "/login/userPhoneCanUse.jhtml?user_phone="+user_phone_reg+"&version="+new Date();
		$.post(url,function(data){
			if(data == "no"){
				$("#user_phone_reg_info").html("<span class='yes'></span>手机号码填写正确");
				userPhone = true;
			}else{
				$("#user_phone_reg_info").html("<span></span>该手机号码还未注册");
				userPhone = false;
				return;
			}
		});
	}
}
//校验验证码
function check_code_info(){
	var check_code = $.trim( document.getElementById("check_code").value );
	if(check_code=="" || check_code=="请输入验证码"){
		$("#check_code_info").html("<span></span>验证码不能为空");
		userCode = false;
	}else{
		var url = "/login/checkCode.jhtml?check_code="+check_code+"&version="+new Date();
		$.post(url,function(data){
			if(data == "yes"){
				$("#check_code_info").html("<span class='yes'></span>验证码填写正确");
				userCode = true;
			}else if(data == "checkCodeNo"){
				$("#check_code_info").html("<span></span>验证码输入有误");
				userCode = false;
			}
		});
	}
}
//刷新验证码
function refresh(){
	var obj = document.getElementById("verCode");
	obj.src = "/imageServlet?"+Math.random();   
}
//下一步 按钮
function submitForm(){
	user_phone_reg_info();
	check_code_info();
	var user_phone_reg = $.trim($("#user_phone_reg").val());
	if(userPhone == true && userCode == true){
		window.location="/login/forgetPwdTwo.jhtml?user_phone="+user_phone_reg+"&version="+new Date();
	}
}
</script>
</head>
<body>
<!--以下是头部logo部分 -->
<div id="header">
	<div class="logo" onclick="window.location='/buyTicket/bookIndex.jhtml'">
    <img src="/images/logo.jpg" width="257" height="59" /></div> 
</div>

<!--以下是头部步骤head_step部分 -->
<div class="pass_step">
	<dl>
    	<dt class="on">输入用户名</dt>
        <dd class="on"></dd>
    </dl>
	<dl>
    	<dt>验证身份</dt>
        <dd></dd>
    </dl>
	<dl>
    	<dt>重置密码</dt>
        <dd></dd>
    </dl>
	<dl>
    	<dt>完成</dt>
        <dd></dd>
    </dl>
</div>

<!--以下是注册页面填写信息registerFill内容部分 -->
<div class="reFill_all">
	<h3 class="message_tit">重置密码</h3>
  	<div class="reFill_con">
   		<dl>
    		<dt>
            	<span  class="span"  style="width:100px;"><b class="red">*</b>&nbsp;&nbsp;请输入登陆名 </span>
            	<input type="text" id="user_phone_reg" name="user_phone_reg" value="请输入正确的手机号码" onfocus="if(this.value=='请输入正确的手机号码'){this.value=''};" 
    				onblur="if(this.value==''||this.value=='请输入正确的手机号码'){this.value='请输入正确的手机号码';}  user_phone_reg_info();" />
            </dt>	
            <dd id="user_phone_reg_info"><span></span>请输入正确的手机号码</dd>
        </dl>
        <dl class="reF_test">
   		  	<dt>
          		<span  class="span" style="width:100px;"><b class="red">*</b>&nbsp;&nbsp;请输入验证码</span>
          		<input type="text" id="check_code" name="check_code" value="请输入验证码" onfocus="if(this.value=='请输入验证码'){this.value=''};" 
    					onblur="if(this.value==''||this.value=='请输入验证码'){this.value='请输入验证码';} check_code_info(); " 
    					onkeydown="if (event.keyCode==13) submitForm();" />	
	      	</dt>
          	<dd>
          		<img src="/imageServlet" class="reF_testspan" alt="" title="点击更换" id="verCode" onclick="refresh();" />
          	</dd>
          	<dd id="check_code_info"><span></span>请输入验证码</dd>
   	  	</dl>
		<div class="btn1" style="margin-left:113px;" onclick="submitForm();">下一步</div>
    </div>
</div>

<!-- footer start  -->
<%@ include file="/pages/common/footer.jsp"%> 
<!-- footer end --> 

</body>
</html>
