<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
  <head>
    <link rel="stylesheet" href="/css/back_style.css" type="text/css" />
    <title>���12306�ӿ�ҳ��</title>

  </head>
  
  <body>
	<form action="/extSystemSetting/addURL.do" method="post">
		<h1>���12306�ӿ�</h1>
		�ӿ�URL��<input type="text" name="url"/><br/>
		<input type="submit" value="����"/>
	</form>
  </body>
</html>
