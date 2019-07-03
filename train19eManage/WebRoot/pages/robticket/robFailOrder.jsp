<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>卡单管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
		<script type="text/javascript">

	function submitForm(){
		if($.trim($("#order_id").val())==""){
		$("#order_id").focus();
			alert("订单号不能为空！");
			return;
		}
		$("#updateForm").submit();
	}
	
		</script>
  </head>
  
  <body>
  <form action="/acquire/acquireFailOrderPage.do" method="post" name="updateForm" id="updateForm">
        <hr><center><br><br><br><br>
        <ul class="order_num oz">
  		
  		<li><font size="4" >订单号：</font>&nbsp;<input type="text" name="order_id" id="order_id"  size="30"
        			/>
        	</li>
        	
        </ul><br><br><br><br>
		<input type="button" value="切换人工处理" id="btnSubmit" onclick="submitForm()"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#228B22;cursor:pointer;width:100px;height:30px;"/>
		<input type="button" value="返回" onclick="backPage();"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#FFA500;cursor:pointer;width:100px;height:30px;"/>						
		<br><br><br><br>
		</center>
  </form>
	
  </body>
</html>
