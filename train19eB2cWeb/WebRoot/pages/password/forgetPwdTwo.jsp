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
var userPhoneCode = false;//手机验证码
//获取手机验证码
var wait = 60;
function sendVerfityCode(user_phone_reg){
	if (wait == 0) {
	    $("#sendCode").attr("class","btn5");
	    $("#sendCode").val("获取验证码重置");
	    wait = 60;
	} else {
		$("#sendCode").attr("class","btn5_on");
		$("#sendCode").prop("disabled", true);//设置为只读属性
		$("#sendCode").val(wait + "秒后重新获取");
		wait--;
		setTimeout(function () {
			sendVerfityCode();
		},
		1000)
	}
}
//向手机下发验证码短信
function sendCodeFun(user_phone_reg){
	if($("#sendCode").val() == '60秒后重新获取'){//向填写的手机号码发送短信
		var url = "/login/sendRegisterMsn.jhtml?user_phone="+user_phone_reg+"&version="+new Date();
		$.post(url,function(data){
			if(data == "no"){ 
				$("#phone_code_info").html("<span></span>获取验证码失败，请重新获取");
				userPhoneCode = false;
				return;
			}else{
				var yzm = data.split(':')[1];
				$("#phoneCode").val(yzm);
				$("#phone_code").focus();
			}
		});
	}
}
//校验手机验证码是否正确
function phone_code_info(){
	var phone_code = $("#phone_code").val();
	if(phone_code==""){
		$("#phone_code_info").html("<span></span>手机验证码不能为空");
		userPhoneCode = false;
	}else if($("#phoneCode").val()!=phone_code){
		$("#phone_code_info").html("<span></span>手机验证码输入错误");
		userPhoneCode = false;
	}else{
		var url = "/login/judgeRegisterMsn.jhtml?phone_code="+phone_code+"&version="+new Date();
		$.post(url,function(data){
			if(data == "no"){
				$("#phone_code_info").html("<span></span>手机验证码已过期");
				userPhoneCode = false;
			}else{//验证码发送成功
				$("#phone_code_info").html("<span class='yes'></span>手机验证码填写正确");
				userPhoneCode = true;
			}
		});
	}
}
//下一步 按钮
function submitForm(user_phone){
	phone_code_info();
	if(userPhoneCode == true){
		window.location="/login/forgetPwdThree.jhtml?user_phone="+user_phone+"&version="+new Date();
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
    	<dt>重置密码</dt>
        <dd></dd>
    </dl>
	<dl>
    	<dt>完成</dt>
        <dd></dd>
    </dl>
</div>

<!--以下是注册页面填写信息registerSfen内容部分 -->
<div class="reFill_all">
	<input type="hidden" id="phoneCode" value="" />
	<h3 class="message_tit">重置密码</h3>
  	<div class="reFill_con">
    	<p class="Sfen_p">登录名:${user_phone }</p> 
        <div class="Sfen_d">
        	<b></b>
        	<p>
        	通过您的注册手机号&nbsp;<strong>${user_phone_info }</strong>&nbsp;校验会员身份
        	</p>
        	<!-- <span class="btn5" id="sendCode" style="display:inline-block; margin-top:15px;" onclick="sendVerfityCode('${user_phone }');sendCodeFun('${user_phone }');">获取验证码重置</span> -->
        	<input type="button" class="btn5" id="sendCode" style="display:inline-block; margin-top:15px;" onclick="sendVerfityCode('${user_phone }');sendCodeFun('${user_phone }');" value="获取验证码重置"  />
        </div>
        
		<div class="Sfen_div">
        	<dl>
        		<dt>
        			<p>手机验证码
        			<input type="text" id="phone_code" name="phone_code" onblur="phone_code_info();" class="input"/>
					</p>
        		</dt>
        		<dd style="padding-top:20px;" id="phone_code_info"><span></span>请输入手机验证码</dd>
        	</dl>
        	<div class="btn1" style="margin-top:25px;margin-left:110px;" onclick="submitForm('${user_phone }');">完成校验</div>
        </div>
        
    </div>
</div>



<!-- footer start  -->
<%@ include file="/pages/common/footer.jsp"%> 
<!-- footer end --> 

</body>
</html>
