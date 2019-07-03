<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!-- 
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
 -->

<% 
	if(pageContext.getAttribute("path") == null){
		String contextPath= request.getContextPath();
		pageContext.setAttribute("path", contextPath);
	}
%>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    
    <title>测试接口</title>
  </head>
  
  <body>
		<span>新余票查询：</span>
	    <form action="${path}/external/queryNewLeftTicket"  method="post" id="newQueryLeft">
	    	发车日期：<input type="text" value="" name="travel_time" id="travel_time">
			出发站：<input type="text" value="" name="from_station" id="from_station">
			到达站: <input type="text" value="" name="arrive_station" id="arrive_station">
			</br>
			<input type="submit" value="查询 "/>
		</form>
		</br>
		</br>
  </body>
</html>
