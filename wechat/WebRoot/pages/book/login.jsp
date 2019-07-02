<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
	<head>

		<title>酷游旅游</title>
		<meta charset="utf-8">
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<meta name="viewport"
			content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
		<link rel="stylesheet" type="text/css" href="/css/base.css">
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript" src="/js/artDialog.js"></script>
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
		<script type="text/javascript">
		function checkSubmit(){
			var phone = $.trim($("#phone").val());
			var password=$.trim($("#password").val());
			if(phone.length==0) {
			alert(phone.length);
				var dialog = art.dialog({
					lock: false,
					fixed: true,
					left: '50%',
					top: '50%',
				    title: '提示',
				    okVal: '确认',
				    icon: "/images/warning.png",
				    content: '请输入用户名！',
				    ok: function(){}
				});	
			}
		}
		
		$("#phone").focus(function(){
			$("#errorMsg").html("");
		});
	</script>
		<style type="text/css">
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
	width: 30%;
	max-width: 80px;
	font-size: 18px;
	padding-left: 5px;
	vertical-align: middle;
}

.inputtext {
	padding-top: 10px;
	color: #000;
	padding-left: 5px;
	min-width: 70%;
	max-width: 90%;
	font-size: 18px;
	float: right;
	border: none;
	border-left: 1px solid;
	padding-right: 5px;
	padding-left: 5px;
	vertical-align: middle;
	display: inline-block;
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

.loginbutton {
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

.errorMsg {
	margin-top:20px;
	margin-left:10px;
	font-size:18px;
	color:red;
	position:relative;
}
</style>

	</head>

	<body>
		<div class="wrap">
			<header id="bar">
			<a href="javascript:window.history.back();" class="m19e_ret"></a>
			<h1>
				登陆
			</h1>
			</header>
			<span id="errorMsg" class="errorMsg">${errorMsg }</span>
			<form name="loginForm" id="loginForm" method="post" action="/login/trainIndex.jhtml"
				onsubmit="return checkSubmit();">
				<div class="inputDiv">
					<label for="phone" class="inputLabel">
						用户名
					</label>
					<input type="text" name="phone" id="phone" class="inputtext"
						value="${phone}" placeholder="请填写您的手机号">
					<hr />
					<label for="password" class="inputLabel">
						密码
					</label>

					<input type="password" name="password" id="password"
						class="inputtext" placeholder="请输入密码">
				</div>
				<a href="/register/turnToChangePwd.jhtml?openID=${openID}"
					class="findpassword">忘记密码？</a>
				<div class="submitDiv">

					<input type="submit" value="登陆" class="loginbutton">

				</div>
			</form>
			<span style="color: red; font-size: 18px">${errorMsg}</span>
		</div>
	</body>
</html>
