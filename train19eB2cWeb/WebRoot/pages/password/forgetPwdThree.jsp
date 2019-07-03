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
var userPassword = false;//用户密码
var userPwdAgain = false;//确认密码
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
		$("#user_password_reg_info").html("<span class='reset_s'></span>密码不能为空");
		userPassword = false;
	} else{ 
		if(pwd.length>5 && pwd.length<21){
			$("#user_password_reg_info").html("<span class='yes'></span>密码填写正确");
			userPassword = true;
		}else{
			$("#user_password_reg_info").html("<span class='reset_s'></span>密码为6-20位数字、字母和符号");
			userPassword = false;
		}
	}
}
//判断两次输入的密码是否一致
function passIsSame(){
 	var user_password_reg = $.trim($("#user_password_reg").val());
 	var user_password_ok = $.trim($("#user_password_ok").val());
 	if(user_password_reg=="" || user_password_reg=="6-20位数字、字母和符号"){
 		$("#user_password_reg_info").html("<span class='reset_s'></span>密码不能为空");
 		userPassword = false;
 	}
 	if(user_password_ok=="" || user_password_ok=="请再次输入密码"){
 		$("#user_password_ok_info").html("<span class='reset_s'></span>确认密码不能为空");
 		userPwdAgain = false;
 	}
 	if(user_password_reg!="" && user_password_reg!="6-20位数字、字母和符号" && user_password_ok!="" && user_password_ok!="请再次输入密码"){
 		if(user_password_ok != user_password_reg){
 	 		$("#user_password_ok_info").html("<span class='reset_s'></span>两次输入密码不一致，请重新输入");
 	 		$("#user_password_ok").val("");
 	 	 	$("#user_password_ok").focus();
 	 	 	userPwdAgain = false;
 	 	}else{
 	 		$("#user_password_ok_info").html("<span class='yes'></span>确认密码填写正确");
 	 		userPwdAgain = true;
 	 	}
 	}
}
//下一步 按钮
function submitForm(user_phone){
	userPasswordInfo();
	passIsSame();
	var user_password_ok = $.trim($("#user_password_ok").val());
	if(userPassword == true && userPwdAgain == true){
		window.location="/login/forgetPwdUpdate.jhtml?user_phone="+user_phone+"&user_password="+user_password_ok+"&version="+new Date();
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
    	<dt class="on">重置密码</dt>
        <dd class="on"></dd>
    </dl>
	<dl>
    	<dt>完成</dt>
        <dd></dd>
    </dl>
</div>

<div class="reFill_all">
	<h3 class="message_tit">重置密码</h3>
  	<div class="reFill_con">
   		<dl>
	    	<dt style="margin:0;">
	        	<span  class="span"> 登录名:</span>
	        </dt>
	        <dd>${user_phone }</dd>
        </dl>
        
    	<dl>
    		<dt>
            	<span  class="span">新密码:</span>
            	<input type="text" id="user_password_reg_text" name="user_password_reg_text" value="6-20位数字、字母和符号" onfocus="changeRegOne(this);" />
    			<input type="password" id="user_password_reg" name="user_password_reg" value="" style="display:none;" onblur="userPasswordInfo(); changeRegOne(this); " onkeyup="pwStrength(this.value);" />
            </dt>
    		<dd id="user_password_reg_info">
            	<span class="reset_s"></span>
            	请重新设置新密码
            </dd>
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
	            <span  class="span">确认新密码:</span>
	            <input type="text" id="user_password_ok_text" name="user_password_ok_text" value="请再次输入密码" onfocus="changeRegTwo(this);" />
    			<input type="password" id="user_password_ok" name="user_password_ok" value="" style="display:none;" onblur="changeRegTwo(this);passIsSame();" />
            </dt>
			<dd id="user_password_ok_info"><span class="reset_s"></span>请再次输入密码</dd>    	
		</dl>
      	<div class="btn1" onclick="submitForm('${user_phone }');">完成</div>
    </div>
</div>



<!-- footer start  -->
<%@ include file="/pages/common/footer.jsp"%> 
<!-- footer end --> 

</body>
</html>
