<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>普通用户信息明细页</title>
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

	
	function submitForm(){
		if($.trim($("#password").val())==""){
			$("#password").focus();
				alert("登录密码不能为空！");
				return;
			}
			if(($.trim($("#password").val()))!= ($.trim($("#password1").val()))){
				alert("登录密码与确认密码不一致！");
				return;
			}
			
		$("#updateForm").submit();
		
	}
	function onblurUsername(){
		var str = document.getElementById("user_name").value ;
		var name = $.trim(str);
		if(name==""){
		$("#username_info").text("用户名不能为空!").css("color","#f00");
		}else{
			var url = "/loginManager/queryUsername.do?user_name="+name;
			$.get(url,function(data){
				if(data == "yes"){
					$("#username_info").text("恭喜!用户名可用!").css("color","#0b0");
					$("#btnModify").attr("disabled", false);
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
		if(name==""){
		$("#realname_info").text("真实姓名不能为空!").css("color","#f00");
		}else{
			var url = "/loginManager/queryRealname.do?real_name="+name;
			$.get(url,function(data){
				if(data == "yes"){
					$("#btnModify").attr("disabled", false);
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
		}
	}
	function onblurEmail(){
		$("#email_info").text("");
		var email=$.trim( document.getElementById("email").value );
		var reg = /^[a-zA-Z0-9_-]+(\.([a-zA-Z0-9_-])+)*@[a-zA-Z0-9_-]+[.][a-zA-Z0-9_-]+([.][a-zA-Z0-9_-]+)*$/;
		if(email==""){
			//$("#email_info").text("邮箱不能为空!").css("color","#f00");
		}else if((!email=="") && (reg.test(email))==false){
			$("#email_info").text("邮箱格式不正确!").css("color","#f00");
		}
	}
	function onblurTelephone(){
		$("#telephone_info").text("");
		var tel=$.trim( document.getElementById("user_phone").value );
		var reg = /^[1]\d{10}$/;
		//var reg = /^[1]\d+$/;
		if(tel==""){
			//$("#telephone_info").text("手机不能为空!").css("color","#f00");
		}else if((!tel=="") && (reg.test(tel))==false){
			$("#telephone_info").text("手机格式不正确!").css("color","#f00");
		}
	}
	function focusUsername(){
	$("#username_info").text("");
	}
	function focusRealname(){
	$("#realname_info").text("");
	}
	
</script>
</head>

<body>
	<div style="width:expression(document.body.clientWidth + 'px')">
	<div class="book_manage account_manage oz">
	<form action="/user/updateUser.do?user_id=${userMap.user_id }" method="post" name="updateForm" id="updateForm" onsubmit="return checkSubmit();">
        <ul class="order_num oz">
        	<br/><br/><br/>
        	<li><input type="hidden" name="user_id" value="${userMap.user_id }"/>
        		<input type="hidden" name="username" value="${userMap.username }"/></li>
        	<li><span style="color:red">*</span>登陆账号：&nbsp;${userMap.username }
        			<span id="username_info"style="width: 140px;"></span></li>
        	<li><span style="color:red">*</span>真实姓名：&nbsp;${userMap.real_name }
        			<span id="realname_info"></span></li>
            <li><span style="color:red">*</span>登录密码：&nbsp;<input type="password" name="password" id="password" value="${userMap.pwd }" onblur="onblurPassword()"style="width: 140px;"/>
            		<span id="password_info"></span></li>
            <li><span style="color:red">*</span>确认密码：&nbsp;<input type="password" name="password1" id="password1" value="${userMap.pwd }" onblur="onblurPassword1()"style="width: 140px;"/>
            		<span id="password1_info"></span></li>
        	<li><span style="color:red">*</span>所在部门：
        			<span style="color:#f60;font-weight:bold;font-family:arial;">	
							${userDepartment[userMap.department] }
					</span>
        	</li>
        	<li><span style="color:red">*</span>邮&nbsp;&nbsp;&nbsp;&nbsp;箱：&nbsp;<input type="text" name="email" id="email" value="${userMap.email }" onblur="onblurEmail()" style="width: 140px;"/>
        			<span id="email_info"></span></li>
        	<li><span style="color:red">*</span>手&nbsp;&nbsp;&nbsp;&nbsp;机：&nbsp;<input type="text" name="user_phone" id="user_phone" value="${userMap.telephone }" onblur="onblurTelephone()" style="width: 140px;"/>
        			<span id="telephone_info"></span></li>
        	
        </ul>
        <p>	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        	<input type="button" value="修改" class="btn btn_normal" id="btnModify" onclick="submitForm()"/>
        </p>
        </form>
	</div>
	</div>
</body>
</html>
