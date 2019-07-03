<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo"%>
<!DOCTYPE HTML>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Cache-Control" content="no-store,no-cache,must-revalidate">
		<meta HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
		<meta http-equiv="Expires" CONTENT="-1">
		<title>验证码服务系统</title>
		<link rel="stylesheet" href="/css/menuStyle.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript">
			$().ready(function(){
				$(".side-nav a").click(function(){
					$(".side-nav li").removeClass("current");
					$(this).parent("li").addClass("current");
				});
				//alert(document.body.clientWidth);
			})
		</script>
	</head>
	<body>
		<div style="text-align:center;border:0px solid #dadada;width:document.body.clientWidth;height:100px;margin:0px auto;padding:0px 0;
					background:url(/images/logo.jpg) no-repeat center top;background-position:0 0;  background-size:document.body.clientWidth 120px;">
		<!-- <strong>
			<!-- <br/><br/><br/><br/><br/>
				<span style="font-size:18px;padding-right:80px;color:#0053ff;float:right;">
				  	<font face=隶书>欢迎您：${sessionScope.loginUserVo.real_name}！</font>
				<input type="button" onclick="javascript:window.location.href='/loginManager/logOutUser.do';" value="退出"/>&nbsp;  
			</span>
		</strong> -->
		</div>
	</body>
</html>