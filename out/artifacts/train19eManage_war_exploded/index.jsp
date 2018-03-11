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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>火车票管理后台</title>

	</head>
	  <%  
    		 LoginUserVo loginUserVo= (LoginUserVo)request.getSession().getAttribute("loginUserVo");
    		 if(loginUserVo==null){
    		 	response.sendRedirect("/pages/login/login.jsp");
    		 	return;
    		 }
      %>

	<frameset cols="140,*" frameborder="yes" border="0" framespacing="0"
		id="">
		<frame name="left" scrolling="auto" noresize="noresize"
			src="./menu.jsp" frameborder="0">
			 <% if ("77".equals(loginUserVo.getUser_level()) || "78".equals(loginUserVo.getUser_level())){ %>
			<frame name="mainConent" scrolling="auto" noresize="noresize" src="/manual/queryManualPage.do"
			frameborder="0" />
			<% }else if ("p1".equals(loginUserVo.getUser_level()) || "p2".equals(loginUserVo.getUser_level())){ %>
			<frame name="mainConent" scrolling="auto" noresize="noresize" src="/psOrder/queryPsorderPage.do"
			frameborder="0" />
			<%}else{ %>
			<frame name="mainConent" scrolling="auto" noresize="noresize" src="/acquire/queryAcquirePage.do"
			frameborder="0" />
			<%} %>
	</frameset>
</html>