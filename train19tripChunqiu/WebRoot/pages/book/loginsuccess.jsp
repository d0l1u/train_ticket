<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/tld/trainUtil.tld" prefix="tn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>登录成功页</title>
<script type="text/javascript" src="/js/jquery.js"></script>
</head>

<body>
<script type="text/javascript" language="JavaScript">
	try{
		//parent.parent.CHLoginCallback();
		var url="/chunqiu/buyTicket/putSession.jhtml?user_id="+<%=request.getParameter("user_id") %>;
		//初始化打印方式
		$.ajax({
				url: url,
				type: "POST",
				cache: false,
				success: function(data){
					if(data=='true'){
						parent.parent.CHLoginCallback();
					}
				}
			});
		//window.location = encodeURI(url);
		//url="/chunqiu/buyTicket/gotoBookOrder.jhtml?travelTime=2014-11-08&trainCode=K7727&startCity=%E5%8C%97%E4%BA%AC%E8%A5%BF&endCity=%E5%A4%A9%E6%B4%A5&startTime=00:42&endTime=02:38&costTime=116&seatMsg=%E6%97%A0%E5%BA%A7_19.5_445,%E7%A1%AC%E5%BA%A7_19.5_417,%E8%BD%AF%E5%BA%A7_-_-,%E4%B8%80%E7%AD%89%E5%BA%A7_-_-,%E4%BA%8C%E7%AD%89%E5%BA%A7_-_-,%E7%A1%AC%E5%8D%A7_73.5_191,%E8%BD%AF%E5%8D%A7_-_-,%E9%AB%98%E7%BA%A7%E8%BD%AF%E5%8D%A7_-_-,%E7%89%B9%E7%AD%89%E5%BA%A7_-_-,%E5%95%86%E5%8A%A1%E5%BA%A7_-_-&defaultSelect=%E7%A1%AC%E5%BA%A7"+"&user_id="+<%=request.getParameter("user_id") %>;
		//window.location.href = "http:\/\/118.244.193.40:18076/pages/book/loginsuccess.jsp?user_id=18600091735";//模拟登录
	}catch(e){}
</script>
</body>
</html>
