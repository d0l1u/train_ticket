<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加管理员信息明细页</title>
<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" type="text/javascript" src="/js/jquery.validate.js"></script>
<script language="javascript" type="text/javascript" src="/js/jquery.metadata.js"></script>
<script type="text/javascript">
	var checkSubmitFlg = false;//设置全局变量，只允许表单提交一次
	function checkSubmit() {
	   if (checkSubmitFlg == true) {
	        return false;
	   }
	   checkSubmitFlg = true;
	   return true;
	}
	document.ondblclick = function docondblclick() {
	    window.event.returnValue = false;
	}
	document.onclick = function doconclick() {
	   if (checkSubmitFlg) {
	       window.event.returnValue = false;
	   }
	}


	$().ready(function() { 
		$("#updateForm").validate(); 
	});
	$().ready(function(){
	    var timer=setInterval("isDisabled();",500);
	});
	var userName = false;
	var realName = false;
	var password = false;
	var password1 = false;
	var email = false;
	var userPhone = false;
	var FormSub = false;
	function isDisabled(){
		if((userName == true) && (realName == true) && (password == true) && (password1 == true) && (email == true) && (userPhone == true)){
			FormSub = true;
			$("#btnModify").attr("disabled", false);
		}else{
			FormSub = false;
			$("#btnModify").attr("disabled", true);
		}
		//alert("userName:"+userName+"--realName:"+realName+"--password:"+password+"--password1:"+password1+"--email:"+email+"--userPhone:"+userPhone+"--FormSub:"+FormSub);
	}

	
	function submitForm(){
		if($.trim($("#user_name").val())==""){
		$("#user_name").focus();
			alert("登录账号不能为空！");
			return;
		}
		if($.trim($("#real_name").val())==""){
		$("#real_name").focus();
			alert("真实姓名不能为空！");
			return;
		}
		if($.trim($("#password").val())==""){
		$("#password").focus();
			alert("登录密码不能为空！");
			return;
		}
		if(($.trim($("#password").val()))!= ($.trim($("#password1").val()))){
			alert("登录密码与确认密码不一致！");
			return;
		}
		if($.trim($("#email").val())==""){
		$("#email").focus();
			alert("邮箱不能为空！");
			return;
		}
		if($.trim($("#user_phone").val())==""){
		$("#user_phone").focus();
			alert("手机号不能为空！");
			return;
		}
		
		$("#updateForm").submit();
		
	}
	function onblurUsername(){
		var str = document.getElementById("user_name").value ;
		var name = $.trim(str);
		$("#username_info").text("");
		if(name==""){
			$("#username_info").text("用户名不能为空!").css("color","#f00");
		}else{
			var url = "/loginManager/queryUsername.do?user_name="+name+"&version="+new Date();
			$.get(url,function(data){
				if(data == "yes"){
					$("#username_info").text("恭喜!用户名可用!").css("color","#0b0");
					userName = true;
				}else{
					$("#username_info").text("用户名已被占用！").css("color","#f00");
					$("#btnModify").attr("disabled", true);
				}
			
			});
		}
	}
	function onblurRealname(){
		var str = document.getElementById("real_name").value ;
		var name=$.trim(str);
		$("#realname_info").text("");
		if(name==""){
			$("#realname_info").text("真实姓名不能为空!").css("color","#f00");
		}else{
			var url = "/loginManager/queryRealname.do?real_name="+name;
			$.get(url,function(data){
				if(data == "yes"){
					realName = true;
				}else{
					$("#realname_info").text("姓名重复，请添加_等标识符，以便区分！").css("color","#f00");
					$("#btnModify").attr("disabled", true);
				}
			});
		}
	}
	function onblurPassword(){
		$("#password_info").text("");
		var str = document.getElementById("password").value ;
		var name=$.trim(str);
		if(name==""){
			$("#password_info").text("密码不能为空!").css("color","#f00");
		}else{
			password = true;
		}
	}
	function onblurPassword1(){
		$("#password1_info").text("");
		var pass1=$.trim( document.getElementById("password1").value );
		var pass=$.trim( document.getElementById("password").value );
		if(pass1==""){
			$("#password1_info").text("请确认密码!").css("color","#f00");
		}else if(pass1 != pass){
			$("#password1_info").text("登录密码与确认密码不一致!").css("color","#f00");
			password1 = false;
		}else{
			password1 = true;
		}
	}
	function onblurEmail(){
		$("#email_info").text("");
		var email1=$.trim( document.getElementById("email").value );
		if(email1==""){
			$("#email_info").text("邮箱不能为空!").css("color","#f00");
		}else{
			email = true;
		}
	}
	function onblurTelephone(){
		$("#telephone_info").text("");
		var email=$.trim( document.getElementById("user_phone").value );
		if(email==""){
			$("#telephone_info").text("电话不能为空!").css("color","#f00");
		}else{
			userPhone = true;
		}
	}
	
</script>
</head>

<body>
	<div style="width:expression(document.body.clientWidth + 'px')">
	<div class="book_manage account_manage oz">
	<form action="/loginManager/addAdminPage.do" method="post" name="updateForm" id="updateForm" onsubmit="return checkSubmit();" >
        <ul class="order_num oz">
        	<br/><br/><br/>
        	<li><input type="hidden" name="user_id" value="${userInfo_Map.user_id }"/></li>
        	<li><span style="color:red">*</span>登陆账号：&nbsp;<input type="text" name="user_name" id="user_name" onblur="onblurUsername()" style="width: 140px;"/><span id="username_info"style="width: 140px;"></span></li>
        	<li><span style="color:red">*</span>真实姓名：&nbsp;<input type="text" name="real_name" id="real_name" onblur="onblurRealname()" style="width: 140px;"/><span id="realname_info"></span></li>
            <li><span style="color:red">*</span>登录密码：&nbsp;<input type="password" name="password" id="password" onblur="onblurPassword()"style="width: 140px;"/><span id="password_info"></span></li>
            <li><span style="color:red">*</span>确认密码：&nbsp;<input type="password" name="password1" id="password1" onblur="onblurPassword1()"style="width: 140px;"/><span id="password1_info"></span></li>
        	<li><span style="color:red">*</span>所在部门：
        		<select name="department" id="department" style="width: 145px;">
        			<c:forEach items="${userDepartment }" var="t">
            			<option value="${t.key }">${t.value}</option>
            		</c:forEach>
       			</select>
        	</li>
        	<li><span style="color:red">*</span>邮&nbsp;&nbsp;&nbsp;&nbsp;箱：&nbsp;<input type="text" name="email" id="email" onblur="onblurEmail()"style="width: 140px;"/><span id="email_info"></span></li>
        	<li><span style="color:red">*</span>手&nbsp;&nbsp;&nbsp;&nbsp;机：&nbsp;<input type="text" name="user_phone" id="user_phone" onblur="onblurTelephone()"style="width: 140px;"/><span id="telephone_info"></span></li>
        	<li><span style="color:red">*</span>用户等级：
        		<select name="user_level" id="user_level" style="width: 145px;">
        			<c:forEach items="${userType }" var="t">
            			<option value="${t.key }">${t.value}</option>
            		</c:forEach>
       			</select>
        	</li>
        	
        </ul>
        <p>
        	<input type="reset" value="重置" class="btn btn_normal" />
        	<input type="button" value="提 交" class="btn" id="btnModify" onclick="submitForm()"/>
        </p>
        </form>
	</div>
	</div>
</body>
</html>
