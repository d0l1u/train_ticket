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
//注册处的js
//定义全局变量
var userPhone = false;//手机号码
var userPhoneCode = false;//手机验证码
var userPassword = false;//用户密码
var userPwdAgain = false;//确认密码
var userEmail = false;//用户邮箱
var userCode = false;//验证码

//刷新验证码
function refresh(){
	var obj = document.getElementById("verCode");
	obj.src = "/imageServlet?"+Math.random();   
}
//密码框
function changeRegOne(obj){ 
    if($("#user_password_reg").is(":visible") && $.trim($("#user_password_reg").val())!=""){
        return;
    } 
	obj.style.display = "none";  
	if(obj.type=="text")  {    
		document.getElementById('user_password_reg').style.display = "inline-block";    
		document.getElementById('user_password_reg').focus();//加上    
		if(this.value==''||this.value=='6-20位数字、字母和符号'){this.value='6-20位数字、字母和符号';}
	}else{     
		document.getElementById('user_password_reg_text').style.display = "inline-block";  
		if(this.value=='6-20位数字、字母和符号'){this.value='';}   
	}  
}
//确认密码框
function changeRegTwo(obj){ 
    if($("#user_password_ok").is(":visible") && $.trim($("#user_password_ok").val())!=""){
        return;
    } 
	obj.style.display = "none";  
	if(obj.type=="text")  {    
		document.getElementById('user_password_ok').style.display = "inline-block";    
		document.getElementById('user_password_ok').focus();//加上    
		if(this.value==''||this.value=='请再次输入密码'){this.value='请再次输入密码';}
	}else{
		document.getElementById('user_password_ok_text').style.display = "inline-block";  
		if(this.value=='请再次输入密码'){this.value='';}   
	}  
}



//判断输入密码的类型  
function CharMode(iN) {
	if (iN >= 48 && iN <= 57) //数字  
		return 1;
	if (iN >= 65 && iN <= 90) //大写  
		return 2;
	if (iN >= 97 && iN <= 122) //小写  
		return 4;
	else
		return 8;
}
//bitTotal函数  
//计算密码模式  
function bitTotal(num) {
	modes = 0;
	for (i = 0; i < 4; i++) {
		if (num & 1)
			modes++;
		num >>>= 1;
	}
	return modes;
}
//返回强度级别  
function checkStrong(sPW) {
	if (sPW.length <= 4)
		return 0; //密码太短  
	Modes = 0;
	for (i = 0; i < sPW.length; i++) {
		//密码模式  
		Modes |= CharMode(sPW.charCodeAt(i));
	}
	return bitTotal(Modes);
}

//显示颜色  
function pwStrength(pwd) {
	O_color = "#eeeeee";
	L_color = "#FF0000";
	M_color = "#FF9900";
	H_color = "#33CC00";
	if (pwd == null || pwd == '') {
		Lcolor = Mcolor = Hcolor = O_color;
	} else {
		S_level = checkStrong(pwd);
		switch (S_level) {
		case 0:
			Lcolor = Mcolor = Hcolor = O_color;
		case 1:
			Lcolor = L_color;
			Mcolor = Hcolor = O_color;
			break;
		case 2:
			Lcolor = Mcolor = M_color;
			Hcolor = O_color;
			break;
		default:
			Lcolor = Mcolor = Hcolor = H_color;
		}
	}
	document.getElementById("strength_L").style.background = Lcolor;
	document.getElementById("strength_M").style.background = Mcolor;
	document.getElementById("strength_H").style.background = Hcolor;
	return;
}
//校验密码的长度
function userPasswordInfo(){
	var pwd = $.trim($("#user_password_reg").val());
	if (pwd == null || pwd == '' || pwd=='6-20位数字、字母和符号') {
		$("#user_password_reg_info").html("<span></span>密码不能为空");
		userPassword = false;
	} else{ 
		if(pwd.length>5 && pwd.length<21){
			$("#user_password_reg_info").html("<span class='yes'></span>密码填写正确");
			userPassword = true;
		}else{
			$("#user_password_reg_info").html("<span></span>密码为6-20位数字、字母和符号");
			userPassword = false;
		}
	}
}
//判断两次输入的密码是否一致
function passIsSame(){
 	var user_password_reg = $.trim($("#user_password_reg").val());
 	var user_password_ok = $.trim($("#user_password_ok").val());
 	if(user_password_reg=="" || user_password_reg=="6-20位数字、字母和符号"){
 		$("#user_password_reg_info").html("<span></span>密码不能为空");
 		userPassword = false;
 	}
 	if(user_password_ok=="" || user_password_ok=="请再次输入密码"){
 		$("#user_password_ok_info").html("<span></span>确认密码不能为空");
 		userPwdAgain = false;
 	}
 	if(user_password_reg!="" && user_password_reg!="6-20位数字、字母和符号" && user_password_ok!="" && user_password_ok!="请再次输入密码"){
 		if(user_password_ok != user_password_reg){
 	 		$("#user_password_ok_info").html("<span></span>两次输入密码不一致，请重新输入");
 	 		$("#user_password_ok").val("");
 	 	 	$("#user_password_ok").focus();
 	 	 	userPwdAgain = false;
 	 	}else{
 	 		$("#user_password_ok_info").html("<span class='yes'></span>确认密码填写正确");
 	 		userPwdAgain = true;
 	 	}
 	}
}


//校验手机号码
function user_phone_reg_info(){
	var user_phone_reg = $.trim($("#user_phone_reg").val()); 
	var reg = /^1(3[d]|4[57]|5[d]|8[d]|7[08])d{8}$/g;
	if(user_phone_reg=="" || user_phone_reg=="请输入正确的手机号方便登录和找回密码"){
		//$("#user_phone_reg").focus();
		$("#user_phone_reg_info").html("<span></span>手机号码不能为空");
		userPhone = false;
		$("#sendCode").attr("class","btn5_on");
		$("#sendCode").prop("disabled", true);//设置为只读属性
		return;
	}else if(!/^1(3[\d]|4[57]|5[\d]|8[\d]|7[08])\d{8}$/g.test(user_phone_reg)){
		$("#user_phone_reg_info").html("<span></span>请填写正确的手机号码");
		userPhone = false;
		$("#sendCode").attr("class","btn5_on");
		$("#sendCode").prop("disabled", true);//设置为只读属性
		return;
	}else{//看手机号码是否已经注册过
		var url = "/login/userPhoneCanUse.jhtml?user_phone="+user_phone_reg+"&version="+new Date();
		$.post(url,function(data){
			if(data == "no"){
				$("#user_phone_reg_info").html("<span></span>该手机号码已经注册过");
				userPhone = false;
				$("#sendCode").attr("class","btn5_on");
				$("#sendCode").prop("disabled", true);//设置为只读属性
				return;
			}else{
				$("#user_phone_reg_info").html("<span class='yes'></span>手机号码填写正确");
				userPhone = true;
				$("#sendCode").attr("class","btn5");
				$("#sendCode").prop("disabled", false);//取消只读属性
			}
		});
	}
}

//获取手机验证码
var wait = 60;
function sendVerfityCode(){
	var user_phone_reg = $.trim($("#user_phone_reg").val()); 
	if(user_phone_reg!="" && user_phone_reg!="请输入正确的手机号方便登录和找回密码" && /^1(3[\d]|4[57]|5[\d]|8[\d]|7[08])\d{8}$/g.test(user_phone_reg)){
		if (wait == 0) {
		    $("#sendCode").attr("class","btn5");
		    $("#sendCode").val("免费获取验证码");
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
}
//向手机下发验证码短信
function sendCodeFun(){
	user_phone_reg_info();
	var user_phone_reg = $.trim($("#user_phone_reg").val()); 
	if(user_phone_reg!="" && user_phone_reg!="请输入正确的手机号方便登录和找回密码" && /^1(3[\d]|4[57]|5[\d]|8[\d]|7[08])\d{8}$/g.test(user_phone_reg)){
		if($("#sendCode").val() == '60秒后重新获取'){//向填写的手机号码发送短信
			var url = "/login/sendRegisterMsn.jhtml?user_phone="+user_phone_reg+"&version="+new Date();
			$.post(url,function(data){
				if(data == "no"){ 
					$("#phone_code_info").html("<span></span>获取验证码失败，请重新获取");
					userPhoneCode = false;
					return;
				}else{//验证码发送成功
					var yzm = data.split(':')[1];
					$("#phoneCode").val(yzm);
					$("#phone_code").focus();
				}
			});
		}
	}else{
		$("#user_phone_reg").focus();
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
//校验邮箱地址
function user_email_reg_info(){
	var email = $.trim( document.getElementById("user_email_reg").value ); 
	var reg = /^[a-zA-Z0-9_-]+(\.([a-zA-Z0-9_-])+)*@[a-zA-Z0-9_-]+[.][a-zA-Z0-9_-]+([.][a-zA-Z0-9_-]+)*$/;
	if(email=="" || email=="请输入常用邮箱"){
		$("#user_email_reg_info").html("<span></span>邮箱不能为空");
		userEmail = false;
	}else if((!email=="") && (reg.test(email))==false){
		$("#user_email_reg_info").html("<span></span>邮箱格式不正确");
		userEmail = false;
	}else{
		$("#user_email_reg_info").html("<span class='yes'></span>邮箱填写正确");
		userEmail = true;
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

//提交注册
function submitRegisterForm(){
	var user_phone_reg = $.trim($("#user_phone_reg").val());  
	var user_password_reg = $.trim($("#user_password_reg").val());
	var user_password_ok = $.trim($("#user_password_ok").val());
	var check_code = $.trim($("#check_code").val());
	var user_email_reg = $.trim($("#user_email_reg").value ); 
	 	
	user_phone_reg_info();
	phone_code_info();
	userPasswordInfo();
	passIsSame();
	user_email_reg_info();
	check_code_info();
	//数据库校验用户的账号和密码是否存在
	if(!$("#agree").attr("checked")){
		alert("请勾选同意《19旅行网服务条款》");
		return false;
	}
	if($("#agree").attr("checked") && userPhone == true && userPhoneCode == true && userPassword == true && userPwdAgain == true && userEmail == true && userCode == true){
		var url = "/login/userRegister.jhtml?user_phone="+user_phone_reg+"&user_password="+user_password_reg+"&user_email="+user_email_reg+"&check_code="+check_code+"&version="+new Date();
		$.post(url,function(data){
			if(data == "yes"){
				//跳转至注册成功页面
				//window.location="/index.jsp?version="+new Date();
				window.location="/login/registerSuccess.jhtml?version="+new Date();
			}else if(data == "checkCodeNo"){
				$("#check_code_info").html("<span></span>验证码输入有误");
			}
		});
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

<!--以下是注册页面填写信息registerFill内容部分 -->
<form id="enroll_form" action="/login/userRegister.jhtml" method="post">
<input type="hidden" id="phoneCode" value="" />
<div class="reFill_all">
	<h3 class="message_tit">注册新会员</h3>
  	<div class="reFill_con">
   		<dl>
    		<dt>
            	<span class="span"><b class="red">*</b>&nbsp;&nbsp;手机号码:</span>
            	<input type="text" id="user_phone_reg" name="user_phone_reg" value="请输入正确的手机号方便登录和找回密码" onfocus="if(this.value=='请输入正确的手机号方便登录和找回密码'){this.value=''};" 
    				onblur="if(this.value==''||this.value=='请输入正确的手机号方便登录和找回密码'){this.value='请输入正确的手机号方便登录和找回密码';}  user_phone_reg_info();" />
            </dt>
    		<dd id="user_phone_reg_info"></dd>
        </dl>
        
   		<dl>
    		<dt>
	            <span class="span"><b class="red">*</b>&nbsp;&nbsp;手机验证码:</span>
	            <input type="text" id="phone_code" name="phone_code" value="请输入手机验证码" onfocus="if(this.value=='请输入手机验证码'){this.value=''};" 
    				onblur="if(this.value==''||this.value=='请输入手机验证码'){this.value='请输入手机验证码';}  phone_code_info();" style="width:176px;" />
    		</dt>
    		<dd><!-- <p class="btn5" style="margin:0;" id="sendCode" onclick="sendVerfityCode();sendCode();">免费获取验证码</p> -->
    			<input type="button" class="btn5" style="margin:0;" id="sendCode" onclick="sendVerfityCode(); sendCodeFun();" value="免费获取验证码"  />
    		</dd>
    		<dd style="padding-left:20px;" id="phone_code_info"></dd>
        </dl>
        
    	<dl>
    		<dt>
    			<span class="span"><b class="red">*</b>&nbsp;&nbsp;登录密码:</span>
            	<input type="text" id="user_password_reg_text" name="user_password_reg_text" value="6-20位数字、字母和符号" onfocus="changeRegOne(this);" />
    			<input type="password" id="user_password_reg" name="user_password_reg" value="" style="display:none;" onblur="userPasswordInfo(); changeRegOne(this); " onkeyup="pwStrength(this.value);" />
            </dt>
    		<dd id="user_password_reg_info"></dd>
    	</dl>
        <!--以下是密码强弱框-->          
        <ul class="password_strength clearfix">			
            <li><p id="strength_L"></p><span>弱</span></li>
    		<li><p id="strength_M"></p><span>中</span></li>
    		<li><p id="strength_H"></p><span>强</span></li>
		 </ul>
 		 <!--以上是密码强弱框-->          
 
		 <dl>
    		<dt>
	            <span class="span"><b class="red">*</b>&nbsp;&nbsp;确认密码:</span>
	            <input type="text" id="user_password_ok_text" name="user_password_ok_text" value="请再次输入密码" onfocus="changeRegTwo(this);" />
    			<input type="password" id="user_password_ok" name="user_password_ok" value="" style="display:none;" onblur="changeRegTwo(this);passIsSame();" />
            </dt>
    		<dd id="user_password_ok_info"></dd>
    	 </dl>
            
    	 <dl>
    		<dt>
	            <span class="span"><b class="red">*</b>&nbsp;&nbsp;常用邮箱:</span>
	            <input type="text" id="user_email_reg" name="user_email_reg" value="请输入常用邮箱" onfocus="if(this.value=='请输入常用邮箱'){this.value=''};" 
    				onblur="if(this.value==''||this.value=='请输入常用邮箱'){this.value='请输入常用邮箱';}  user_email_reg_info();" />
            </dt>
    		<dd id="user_email_reg_info"></dd>
   	    </dl>
        
        <dl class="reF_test">
   		  <dt>
	          <span class="span"><b class="red">*</b>&nbsp;&nbsp;验证码:</span>
	          <input type="text" id="check_code" name="check_code" value="请输入验证码" onfocus="if(this.value=='请输入验证码'){this.value=''};" 
    					onblur="if(this.value==''||this.value=='请输入验证码'){this.value='请输入验证码';} check_code_info(); " 
    					onkeydown="if (event.keyCode==13) submitRegisterForm();" />	
	      </dt>
          <dd>
          <img src="/imageServlet" class="reF_testspan" alt="" title="点击更换" id="verCode" onclick="refresh();" />
            	<!-- 看不清?&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:refresh();" >换一张</a> --></dd>
          <dd id="check_code_info"></dd>
   	  </dl>
      <div class="reg_sure">
	      <input id="agree" type="checkbox"/>&nbsp;&nbsp;&nbsp;&nbsp;<a href="/pages/guide/daiGou.jsp" target="_blank">同意《19旅行网服务条款》</a>
	      <div class="btn4" style="margin:10px 0 10px 0;" onclick="submitRegisterForm();">同意服务条款并注册</div>
      </div>
    </div>
</div>
</form>

<!-- footer start  -->
<%@ include file="/pages/common/footer.jsp"%> 
<!-- footer end --> 

</body>
</html>
