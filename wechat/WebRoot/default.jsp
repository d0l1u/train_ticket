<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>My JSP 'default.jsp' starting page</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

	</head>

	<body>

		<button
			style="width: 100px; height: 20px; font-size: 12px; background-color: #3a92e2; font-color: #000;">
			<span style="font-color: #fff;">获取验证码</span>
		</button>

		<hr>
		<img src="images/btn1.png" name=chooseReason width="23" height="21"
			border="0" align=top onClick='openReasonWin()'>
		//输入框，用来显示回传过来的退票理由编号
		<input type="text" name="reason" id="reason">
		<hr>
		<script type="text/javascript">
		//打开选择理由窗口的函数 
		function openReasonWin() { 
			//新窗口的文档名称 
			var srcFile ="reason.jsp"; 
			//高度，位置等 
			var winFeatures = "dialogHeight:550px;dialogLeft:300px;"; 
			//把输入页面的reason input传给弹出窗口 
			var obj =document.getElementById("reason"); 
			//将input作为对象传递给新窗口 
			//ok,open new windows /* 设置传给子窗口的值 obj.value="要传递的值"; */
			window.showModalDialog(srcFile, obj, winFeatures);
		}
		</script>
	<hr></hr>
	系统将会在<strong id="endtime"></strong>秒后跳转到登录页！ 
		<script type="text/javascript">  
var i = 10;  
function remainTime(){  
    if(i==0){  
        location.href='<%=path%>';  
    }  
    document.getElementById('endtime').innerHTML=i--;  
    setTimeout("remainTime()",1000);  
}  
remainTime();  
</script>
	</body>
</html>
