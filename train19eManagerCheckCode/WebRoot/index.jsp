
<!DOCTYPE HTML>
<meta http-equiv="Cache-Control" content="no-store,no-cache,must-revalidate">
<meta HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
<meta http-equiv="Expires" CONTENT="-1">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<html>
	<head>
		<title>验证码服务系统</title>

	</head>
	  <%  
    		 LoginUserVo loginUserVo= (LoginUserVo)request.getSession().getAttribute("loginUserVo");
    		 if(loginUserVo==null){
    		 	response.sendRedirect("/login.jsp");
    		 	return;
    		 }
      %>

	<frameset rows="100,*" frameborder="no" frameborder="no" border="0" framespacing="0">
		<frame name="top" scrolling="no" noresize="noresize" src="./top.jsp" /> 
		<frameset cols="140,*" frameborder="yes" border="0" framespacing="0" id="">
			<frame name="left" scrolling="auto" noresize="noresize" src="./menu.jsp" frameborder="0">
			<frame name="mainConent" scrolling="auto" noresize="noresize" src="" frameborder="0">
		</frameset>
	</frameset> 



	
</html>