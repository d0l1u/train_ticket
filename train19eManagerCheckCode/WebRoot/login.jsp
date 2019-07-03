<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<%@ page language="java" contentType="text/html; charset=UTF-8"%>
	<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>登陆页面</title>
		<!--[if IE 6]>
<script language="javascript" src="/js/png.js"></script>
<script type="text/javascript">
   EvPNG.fix('*');
</script>
<![endif]-->
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript" src="/js/jquery.cookie.js"></script>
		<script type="text/javascript">
function submitForm(){
	 //保存用户信息  
	//if ($("#rmbUser").attr("checked") == "checked") {  
	   var userName = $("#user_name").val();  
	   var passWord = $("#pwd").val();  
	   $.cookie("rmbUser", "true", {expires : 7});
	   $.cookie("userName", userName, {expires : 7});
	   $.cookie("passWord", passWord, {expires : 7}); 
	 // } else {  
	   //$.cookie("rmbUser", "false");  
	  // $.cookie("userName", null);  
	  // $.cookie("passWord", null);  
	  //}  
	
	
	   if($.trim($("#user_name").val())==""){
			$("#user_name").focus();
				alert("登录名不能为空！");
				return;
			}
			if($.trim($("#pwd").val())==""){
			$("#pwd").focus();
				alert("登录密码不能为空！");
				return;
			}
			if($.trim($("#user_name").val())!="" && $.trim($("#pwd").val())!="" ){
				var str = document.getElementById("user_name").value ;
				var user_name = $.trim(str);
				var str1 = document.getElementById("pwd").value ;
				var pwd = $.trim(str1);
				
				var url = "/loginManager/loginManager.do?user_name="+user_name+"&pwd="+pwd+"&version="+new Date();
				$.get(url,function(data){
					if(data == "yes")
						window.location="/index.jsp?version="+new Date();
					else
						alert("用户名或密码错误");
				});
			}
	}
	
	//function refresh(obj){
	  // obj.src = "/imageServlet?"+Math.random();   
	 //}
	 
	$(document).ready(function() {  
	 if ($.cookie("rmbUser") == "true") {
	  //$("#rmbUser").attr("checked", true);  
	  $("#user_name").val($.cookie("userName"));  
	  $("#pwd").val($.cookie("passWord"));  
	 }  
	}); 

	function keyLogin(){
		  if (event.keyCode==13)   //回车键的键值为13
		     document.getElementById("input1").click();  //调用登录按钮的登录事件
		}
			


</script>
		<style type="text/css">
body {
	width: 100%;
	margin: 0;
	padding: 0;
	background: url(images/bg_line.jpg) repeat-x;
}

ul,li,p,input {
	margin: 0;
	padding: 0;
}

li {
	list-style: none;
}

.btn {
	background: url(images/btn.png) no-repeat;
	width: 175px;
	height: 40px;
	border: none;
	cursor: default;
}

*:focus {
	outline: none;
}

.content {
	position: absolute;
	top: 170px;
	left: 50%;
	margin-left: -420px;
	width: 813px;
	height: 300px;
	background: url(images/login_bg.jpg) no-repeat top center;
}

.login-wrap {
	position: relative;
	width: 813px;
	height: 300px;
}

.text-box {
	position: absolute;
	left: 354px;
	width: 137px;
	height: 19px;
}

.text-box input {
	width: 133px;
	padding-left: 4px;
	height: 19px;
	border: none;
	color: #333;
	font: normal 12px/ 19px arial, "Simsun";
}

.user-box {
	top: 124px;
}

.pwd-box {
	top: 158px;
}

.btn-box {
	position: absolute;
	left: 511px;
	top: 137px;
	width: 54px;
	height: 40px;
}

.btn-box input {
	width: 54px;
	height: 40px;
	background: none;
	border: none;
	cursor: pointer;
}
</style>
	</head>
	<body onkeydown="keyLogin();">
		<div class="content">
			<div class="login-wrap">
				<div class="text-box user-box">
					<input type="text" class="text" name="user_name" id="user_name" value="zyx"/>
				</div>
				<div class="text-box pwd-box">
					<input type="password" class="text" name="pwd" id="pwd" />
				</div>
				<div class="btn-box">
					<input type="button" onclick="submitForm()" id="input1" />
				</div>
			</div>
		</div>
	</body>
</html>

