<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>19trip旅行</title>
<link rel="stylesheet" href="/css/reset-min.css" type="text/css" />
<link rel="stylesheet" href="/css/style.css" type="text/css"/>
<link rel="stylesheet" href="/css/sreachbar.css" type="text/css"/>
<link rel="stylesheet" href="/css/travel.css" type="text/css"/>
<link rel="stylesheet" href="/css/login.css" type="text/css"/>
<script type="text/javascript" src="/js/artDialog.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery.form.js"></script>
<script type="text/javascript" src="/js/dialog.js"></script>
<style type="text/css">
/****订单弹出框*****/
#drawBill{ position:fixed;_position: absolute;_top: expression(documentElement.scrollTop + 340 + "px");
	 background:#fff; width:400px; height:280px; border:1px solid #86CBFF; top:50%; left:50%; 
	 margin: -200px 0 0 -250px; overflow:hidden; z-index:999999; font-size:14px; color:#000; display:none;}
* html,* html body{background-image:url(about:blank);background-attachment:fixed} 
* html .ie6fixedTL{position:absolute;left:expression(eval(document.documentElement.scrollLeft));top:expression(eval(document.documentElement.scrollTop))} 
* html .ie6fixedBR{position:absolute;left:expression(eval(document.documentElement.scrollLeft+document.documentElement.clientWidth-this.offsetWidth)-(parseInt(this.currentStyle.marginLeft,10)||0)-(parseInt(this.currentStyle.marginRight,10)||0));top:expression(eval(document.documentElement.scrollTop+document.documentElement.clientHeight-this.offsetHeight-(parseInt(this.currentStyle.marginTop,10)||0)-(parseInt(this.currentStyle.marginBottom,10)||0)))} 
</style>

<script type="text/javascript">
//定义全局变量
var userPasswordOld = false;//用户旧密码
var userPassword = false;//用户新密码
var userPwdAgain = false;//确认新密码

$().ready(function(){

	var optionsUser = {
	   target: '#output',          //把服务器返回的内容放入id为output的元素中    
	   beforeSubmit: showRequest,
	   success: showResponse,      //提交后的回调函数
	   //url: url,                 //默认是form的action， 如果申明，则会覆盖
	   dataType : 'json',
	   timeout: 5000,               //限制请求的时间，当请求大于3秒后，跳出请求
	   failure : function(xhr,msg){ 
			alert("很抱歉，修改用户信息失败！"); 
			return false;
	   } 
	}
	function showRequest(){
		userPasswordOldInfo();
		userPasswordInfo();
		passIsSame();
		if(userPasswordOld == true && userPassword == true && userPwdAgain == true){
			return true;
		}else{
			return false;
		}
	}
	//提交后的回调函数
	function showResponse(responseText, statusText){
		var result = responseText.result;
		if (responseText.result == "SUCCESS") { 
			var dialog = new popup("land_on","drawBill","land_off");
			$("#land_on").click();
	    }else{ 
	    	alert("很抱歉，修改用户密码失败！"); 
			return false;
	    } 
	}
	
	//保存用户信息
	$("#trainForm").submit(function() {
		 //$("#trainForm").attr("action", "/login/saveUserinfo.jhtml");
		 $(this).ajaxSubmit(optionsUser); 
	     return false; 
	}); 
});


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
//校验旧密码
function userPasswordOldInfo(){
	var pwd = $.trim($("#user_password_old").val());
	var user_password = $.trim($("#user_password").val());
	if (pwd == null || pwd == '') {
		$("#user_password_old_info").attr("<span class='reset_s'></span>请输入当前使用的密码");
		userPasswordOld = false;
	}else{ 
		if(pwd==user_password){
			$("#user_password_old_info").html("<span class='yes'></span>当前使用的密码填写正确");
			userPasswordOld = true;
		}else{
			$("#user_password_old_info").html("<span class='reset_s'></span>当前使用的密码填写错误");
			userPasswordOld = false;
		}
	}
}
//校验密码的长度
function userPasswordInfo(){
	var pwd = $.trim($("#user_password_reg").val());
	if (pwd == null || pwd == '') {
		$("#user_password_reg_info").attr("<span class='reset_s'></span>新密码不能为空");
		userPassword = false;
	} else{ 
		if(pwd.length>5 && pwd.length<21){
			$("#user_password_reg_info").html("<span class='yes'></span>新密码填写正确");
			userPassword = true;
		}else{
			$("#user_password_reg_info").html("<span class='reset_s'></span>新密码为6-20位数字、字母和符号");
			userPassword = false;
		}
	}
}
//判断两次输入的密码是否一致
function passIsSame(){
 	var user_password_reg = $.trim($("#user_password_reg").val());
 	var user_password_ok = $.trim($("#user_password_ok").val());
 	if(user_password_reg=="" || user_password_reg=="6-20位数字、字母和符号"){
 		$("#user_password_reg_info").html("<span class='reset_s'></span>新密码不能为空");
 		userPassword = false;
 	}
 	if(user_password_ok=="" || user_password_ok=="请再次输入密码"){
 		$("#user_password_ok_info").html("<span class='reset_s'></span>确认新密码不能为空");
 		userPwdAgain = false;
 	}
 	if(user_password_reg!="" && user_password_reg!="6-20位数字、字母和符号" && user_password_ok!="" && user_password_ok!="请再次输入新密码"){
 		if(user_password_ok != user_password_reg){
 	 		$("#user_password_ok_info").html("<span class='reset_s'></span>两次输入密码不一致，请重新输入");
 	 		$("#user_password_ok").val("");
 	 	 	$("#user_password_ok").focus();
 	 	 	userPwdAgain = false;
 	 	}else{
 	 		$("#user_password_ok_info").html("<span class='yes'></span>确认新密码填写正确");
 	 		userPwdAgain = true;
 	 	}
 	}
}
	
</script>
</head>

<body>
<!--以下是头部logo部分start -->
<jsp:include flush="true" page="/pages/common/headerNav.jsp">
	<jsp:param name="menuId" value="lx" />
</jsp:include>
<!--以下是头部logo部分end -->


<!--以下是我的旅行正文内容travel_con部分start -->
<div class="travel_con">
	<!--左边内容 start-->
			<jsp:include flush="true" page="/pages/common/menuLeft.jsp">
				<jsp:param name="menuId" value="safePass" />
			</jsp:include>
	<!--左边内容 end-->
    
    
    <!--右边内容 start-->
	<div class="right_con">
	<form id="trainForm" action="/login/saveUserPassword.jhtml" method="post">
		<input type="hidden" id="user_id" name="user_id" value="${loginMap.user_id }" />
		<input type="hidden" id="user_password" name="user_password" value="${loginMap.user_password }" />
		<ul class="MyOrder">
    		<li>我的信息</li>
    	</ul>
        <div class="reFill_all">
			<h3 class="message_tit">修改密码</h3>
  			<div class="reFill_con">
		   		<dl>
		    		<dt>
			        	<span class="span">当前密码:</span>
			            <input type="password" id="user_password_old" name="user_password_old" onblur="userPasswordOldInfo();" />
		        	</dt>
		        	<dd id="user_password_old_info">
		            	<span class="reset_s"></span>请输入当前使用的密码
		            </dd>
		        </dl>
        
		    	<dl>
		    		<dt>
			            <span  class="span">新密码:</span>
			            <input type="password" id="user_password_reg" name="user_password_reg" onblur="userPasswordInfo();" onkeyup="pwStrength(this.value);" />
		            </dt>
		    		<dd id="user_password_reg_info">
		            	<span class="reset_s"></span>请输入新密码
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
			            <input type="password" id="user_password_ok" name="user_password_ok" onblur="passIsSame();" />
		            </dt>
					<dd id="user_password_ok_info"><span class="reset_s"></span>请输入确认密码</dd>    	
	            </dl>
      
      			<!-- <div class="btn1">保&nbsp;存</div> -->
      			<input type="submit" class="btn1" value="保&nbsp;&nbsp;存" />
      		</div>

		</div>
        <br/><br/><br/>
    </form>
	</div>
  	<!--右边内容 end-->
</div>
<!--以下是我的旅行正文内容travel_con部分end -->

<!-- 密码修改成功弹框start -->
  	<div class="password" id="drawBill">
    	<dl>
			<dt>
				<strong></strong>
			</dt>
			<dd>
                <p>恭喜您，修改密码成功！</p>
            </dd>
		</dl>
		<input type="hidden" id="land_off" />
		<input type="hidden" id="land_on" />
		<button type="button" class="btn13" onclick="window.location='/login/myInfo.jhtml'">确&nbsp;&nbsp;定</button>
    </div>
<!-- 密码修改成功弹框end -->

<!-- footer start -->
<%@ include file="/pages/common/footer.jsp"%>
<!-- footer end -->
</body>
</html>
