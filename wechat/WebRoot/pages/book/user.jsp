<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
	<head>
		<title>酷游旅游</title>
		<meta charset="utf-8">
		<meta name="viewport"
			content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" type="text/css" href="/css/base.css">
		<script type="text/javascript" src="/js/jquery.js"></script>
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
		<script type="text/javascript">
		function getVertify(){
		
		var user_phone=$.trim($("#phone").val());
		var openID=$.trim($("#openID").val());
		$("#mesg").html("");
		alert("手机号码为：" + user_phone);
		alert("openId: " + openID);
		if("" == user_phone) {
			$("#mesg").html("请输入本机手机号码！");
			return;
		}
		$("#code_btn").attr("disabled", true);
		$.ajax({
					url:"/register/sendSms.jhtml",
					type: "POST",
					async: true,
					cache: false,
					data: {user_phone:user_phone, openID:openID},
					dataType: "text",
					success: function(res){
						var i = 180;
						remainTime(i);
						$('#error').text('');
					},
					error: function(res){
	           		}
				});
		
	}
	
	function remainTime(i){  
	    if(i==0){  
	        $("#code_btn").attr("disabled", false);
	        $("#code_btn").html("获得验证码");
	        $("#mesg").text("");
	        return;
	    }  
	    $("#code_btn").html("<span>请在<em style='color:red;text-align:center;'>" + i-- + "</em>后获取</span>"); 
	    $("#mesg").text("验证码" + i + "秒内有效，超过" + i + "秒请重新获取"); 
	    setTimeout("remainTime(" + i + " )",1000);  
	}  
	
	function checkCode(){
		var user_phone=$.trim($("#phone").val());
		var openID=$.trim($("#openID").val());
		var code = $.trim($("#code").val());
		alert("手机号码为：" + user_phone);
		alert("openId: " + openID);
		if("" == user_phone) {
			$("#error").text("请输入本机手机号码！");
			return;
		}
		if(code.length!=6){
			$("#error").text("请填写正确的验证码");
			return;		
		}
		$.ajax({
			url:"/register/register.jhtml",
			type: "POST",
			async: false,
			cache: false,
			data: {user_phone:user_phone, openID:openID, saved_vertify_code:code},
			dataType: "text",
			success: function(res){
				if(res=="0"){
					$("#code_ok").hide();
					$("#password_area").show();
				}
				if(res=="-1"){
					$("#error").text("验证码错误，请重新获得！");
				}
			},
			error: function(res){
	        }
		});
	}
	
	
	function checkSubmit(){
		var confirm_pwd_input = $.trim($("#confirm_pwd_input").val());
		var pwd_input = $.trim($("#pwd_input"));
		if(pwd_input.length==0) {
			alert("请填写密码！")
			return false;
		}
		if(pwd_input != confirm_pwd_input) {
			alert("新密码和确认密码不一样！");
			return false;
		}
		
		return true;
	}
	</script>
		<style type="text/css">
.code_btn {
	width: 40%;
	min-width: 8em;
	height: 30px;
	position: absolute;
	right: 5%;
	line-height: 30px;
	text-align: center;
	font-size: 16px;
	background-color: #3FA8E5;
	color: #fff;
	border-radius: 5px;
	font-weight: normal;
	margin-right: 0;
}

.code_ok {
	width: 90%;
	margin-top: 30px;
	height: 30px;
	font-size: 16px;
	margin-left: 5%;
	border-radius: 5px;
	background-color: #3FA8E5;
	color: #fff;
	font-weight: normal;
}

.error {
	margin-left: 5%;
	color: red;
	padding-top: 10px;
}

.phone_input {
	width: 90%;
	margin-left: 5%;
	margin-top: 15px;
	height: 25px;
	line-height: 30px;
	border-radius: 5px;
	font-size: 16px;
}

#code_area,#password_area {
	margin-top: 10px;
	line-height: 30px;
	margin-left: 5%;
	width: 90%;
	height: 30px;
}

.code_input {
	width: 50%;
	height: 30px;
	border-radius: 5px;
	font-size: 16px;
	line-height: 30px;
}

.message {
	color: red;
	position: relative;
	font-size: 12px;
}

.pwd_input {
	margin-top: 20px;
	width: 100%;
	height: 25px;
	border-radius: 5px;
}
</style>

	</head>

	<body>
		<div class="wrap">
			<header id="bar">
			<h1>
				${title}
			</h1>
			</header>
			<div id="error" class="error">error</div>
			<form action="post" method="/register/updateWeChatUser.jhtml"
				onsubmit="return checkSubmit();">
				<article id="vertify_code" class="vertify_code">
				<input type="hidden" name="openID" id="openID" value="${openID }" />
				<input type="text" id="phone" name="phone" class="phone_input"
					placeholder="请输入手机号" onfocus="$('#error').text('');">
				<div id="code_area">
					<input type="text" id="code" name="code" class="code_input"
						placeholder="请输入验证码" onfocus="$('#error').text('');">
					<button type="button" id="code_btn" class="code_btn"
						onclick="getVertify();">
						获得验证码
					</button>
					<br>
					<span id="mesg" class="message">error</span>

				</div>
				<button type="button" id="code_ok" name="code_ok" class="code_ok"
					onclick="checkCode();">
					确定
				</button>

				</article>
				<article id="password_area" class="password_area"
					style="display:none">
				<input type="password" name="password" id="pwd_input"
					class="pwd_input" placeholder="请输入新密码" onfocus="$('#error').text('');">
				<input type="password" name="confirm_pwd_input"
					id="confirm_pwd_input" class="pwd_input" placeholder="请输入确认密码" onfocus="$('#error').text('');">

				<button type="submit" id="change_btn" name="change_btn"
					class="code_ok">
					确定
				</button>
				</article>
			</form>
			<div>
	</body>
</html>
