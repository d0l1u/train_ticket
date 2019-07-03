<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'tuniuUrgeRefundUpdate.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="/js/jquery.js"></script>
	<script language="javascript" src="/js/layer/layer.js"></script>
	<script language="javascript" src="/js/mylayer.js"></script>
		
	<script type="text/javascript">
		function updateUrgeRefundMoney(){
			var urge_refund_id = $("#urge_refund_id").val();
			var refund_money = $("#refund_money").val().trim();
			var order_id= $("#order_id").val();
			var cp_id= $("#cp_id").val();
			var remark = $("#remark").val();
			$.ajax({
				url:"/tuniuUrgeRefund/updateUrgeRefundInfo.do",
				data:{
					"urge_refund_id":urge_refund_id,
					"refund_money":refund_money,
					"order_id":order_id,
					"cp_id":cp_id,
					"urge_status":"22",
					"remark":remark
				},
				type: "POST",
				cache: false,
				dataType: "json",
				async: true,
				success: function(data){
					if(data==-1){
						parent.layer.alert("还未生成退款记录，请退款后再操作！",2,"提示");
					}else if(data>0){
						parent.layer.alert("操作成功",1,"提示");
						window.open("/tuniuUrgeRefund/queryUrgeRefundInfo.do?order_id="+order_id+"&cp_id="+cp_id,"mainConent");
					}
					else{
						parent.layer.alert("操作失败，请稍后再试！",2,"提示");
					}
				},
				error: function (data){
					alert("网络繁忙，请稍后再试");
				}
			});
		}
</script>
  </head>
  
  <body>
  <input type="hidden" name="urge_refund_id" value="${urge_refund_id}" id="urge_refund_id" />
  <input type="hidden" name="order_id" value="${order_id}" id="order_id"/>
  <input type="hidden" name="cp_id" value="${cp_id}" id="cp_id"/>
     <br>&nbsp;退款金额：<input type="text" id="refund_money"/><br>
     <br>&nbsp;&nbsp;&nbsp; 备注：<input type="text" id="remark"/><br><br>
     <input type="button" onclick="updateUrgeRefundMoney();" style="margin-left:100px;width:80px" value="确认">
  </body>
</html>
