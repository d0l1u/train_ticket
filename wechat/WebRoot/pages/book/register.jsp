<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML>
<html>
	<head>

		<title>注册19e-火车票账户</title>
		<meta charset="utf-8">
		<meta name="viewport"
			content="width=device-width, initial-scale=1.0, maximum-scale=1.0,minimum-scale=1.0">
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="注册19e-火车票账户">
		<link rel="stylesheet" type="text/css" href="/css/base.css">
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript">
			function check() {
				var password = $.trim($("#password").val());
				var username = $.trim($("#username").val());
				var confirmPassword = $.trim($("#confirmPassword").val());
				if(username == "") {
					alert("请输入用户名！");
					return false;
				}
				if(username == "") {
					alert("请输入密码！");
					return false;
				}
				if(!password.equals(confirmPassword)){
					alert("密码和确认密码不一致，请重新输入！")
					return false;
				}
				return true;
			}
			
	function GetRequest() {
   		var url = location.search; //获取url中"?"符后的字串
   		var theRequest = new Object();
   		if (url.indexOf("?") != -1) {
      		var str = url.substr(1);
      		strs = str.split("&");
      		for(var i = 0; i < strs.length; i ++) {
         	theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);
	      	}
	   	}
	   	return theRequest;
	}
	$().ready(function() {
		var Request = new Object();
		Request = GetRequest();
		var openID = Request['openID'];
		$("#openID").val(openID);
	});
		</script>
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
		<style>
html {
	background: #f5f6f6;
}

.inputDiv {
	margin: 10px 10px auto 10px;
	background: #FFFFFF;
	line-height: 40px;
}

.inputLabel {
	padding-top: 10px;
	font-size: 18px;
	padding-left: 5px;
	max-width: 80px;
}

.inputtext {
	padding-top: 10px;
	color: #000;
	position: absolute;
	left: 6em;
	right: 10px;
	font-size: 18px;
	border: none;
	max-width: 100%;
	border-left: 1px solid;
	padding-left: 5px;
	font-family: "Microsoft yahei";
}

.findpassword {
	position: absolute;
	color: #00007f;
	right: 10px;
	text-decoration: underline;
	margin: 10px 5px 10px;
	text-decoration: underline;
}

.submitDiv {
	position: relative;
	margin: 30px 10px auto 10px;
	height: 48px;
}

.registerbutton {
	background: #ff7200;
	border-radius: 3px;
	width: 100%;
	height: 100%;
	line-height: 48px;
	color: #fff;
	font-size: 20px;
	font-family: "Microsoft yahei";
	border: none;
}
</style>
	</head>

	<body>
		<div class="wrap">
			<header id="bar">
			<a href="javascript:window.history.back();" class="m19e_ret"></a>
			<h1>
				注册
			</h1>
			</header>

			<form name="registerForm" id="registerForm" method="post"
				action="/register/register.jhtml" onsubmit=" return check();">
				<div class="inputDiv">
					<input type="hidden" name="location" id="location"
						value="${location }" />
					<div width="100%">
						<label for="username" class="inputLabel">
							用户名
						</label>
						<input type="text" name="username" id="username" class="inputtext"
							placeholder="请填写您的手机号">
					</div>
					<hr />
					<label for="password" class="inputLabel">
						密码
					</label>

					<input type="password" name="password" id="password"
						class="inputtext" placeholder="请输入密码">
					<hr />
					<label for="confirmpassword" class="inputLabel">
						确认密码
					</label>

					<input type="password" name="confirmPassword" id="confirmPassword"
						class="inputtext" placeholder="请再次输入密码">
				</div>
				<input type="hidden" id="openID" name="openID" value="${openID}">
				<input type="hidden" id="order_id" name="order_id" value="${order_id}">
				<input type="hidden" id="totalPay4Show" name="totalPay4Show" value="${totalPay4Show}">
				<div class="submitDiv">
					<input type="submit" value="注册" class="registerbutton">
				</div>
			</form>
		</div>
	</body>
</html>
