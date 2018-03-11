<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>内嵌差额退款页</title>
<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" type="text/javascript" src="/js/jquery.validate.js"></script>
<script language="javascript" type="text/javascript" src="/js/jquery.metadata.js"></script>
<script type="text/javascript">
	var checkSubmitFlg = false;//设置全局变量，只允许表单提交一次
	function checkSubmit() {
	   if (checkSubmitFlg == true) {
	        return false;
	   }
	   checkSubmitFlg = true;
	   return true;
	}
	document.ondblclick = function docondblclick() {
	    window.event.returnValue = false;
	}
	document.onclick = function doconclick() {
	   if (checkSubmitFlg) {
	       window.event.returnValue = false;
	   }
	}
		
	$().ready(function() { 
		$("#updateForm").validate(); 
	});
	$().ready(function(){
	    var timer=setInterval("isDisabled();",500);
	});
	var orderId = false;
	var refundMoney = false;
	var userRemark = false;
	var FormSub = false;
	function isDisabled(){
		if((orderId == true) && (refundMoney == true) && (userRemark == true)){
			FormSub = true;
		}else{
			FormSub = false;
		}
		//alert("orderId:"+orderId+"--refundMoney:"+refundMoney+"--FormSub_userRemark:"+FormSub+"--FormSub:"+FormSub);
		if(FormSub == true){
			$("#btnSubmit").attr("disabled", false);
		}
	}
	
	function submitForm(){
		if($.trim($("#order_id").val())==""){
		$("#order_id").focus();
			alert("订单号不能为空！");
			return;
		}
		if($.trim($("#refund_money").val())==""){
		$("#refund_money").focus();
			alert("退款差额不能为空！");
			return;
		}
		if($.trim($("#user_remark").val())==""){
		$("#user_remark").focus();
			alert("退款原因不能为空！");
			return;
		}
		$("#updateForm").submit();
	}
	
	function onblurOrderId(){
		var str = $("#order_id").val();
		var order_id = $.trim(str);
		if(order_id==""){
			$("#order_id_info").text("订单号不能为空").css("color","#f00");
			orderId = false;
		}else{
			var url = "/innerRefund/queryRefundTicketAdd.do?order_id="+order_id;
			$.get(url,function(data){
				if(data == "no"){
					$("#order_id_info").text("该订单号不存在，请重新输入").css("color","#f00");
					orderId = false;
					$("#btnSubmit").attr("disabled", true);
				}
				//if(data == "exists"){
				//	$("#order_id_info").text("该订单号已经生成差额退款").css("color","#f00");
				//	orderId = false;
				//	$("#btnSubmit").attr("disabled", true);
				//}
				if(data == "yes"){
					$("#order_id_info").text("订单号正确").css("color","#0b0");
					orderId = true;
				}
			});
		}
		
	}
	
	function onblurRefundMoney(){
		var refund_money = $.trim($("#refund_money").val());
		$("#refund_money_info").text("");
		var reg = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
		if(refund_money==""){
			$("#refund_money_info").text("退款金额不能为空").css("color","#f00");
			refundMoney = false;
			$("#btnSubmit").attr("disabled", true);
		}
		if( (!refund_money=="") && (reg.test(refund_money))==false ){
			$("#refund_money_info").text("格式不正确，请输入有效的金额").css("color","#f00");
			refundMoney = false;
			$("#btnSubmit").attr("disabled", true);
		} 
		if((!refund_money=="") && (reg.test(refund_money))==true ){
			var order_id = $("#order_id").val();
			var refund_money = $("#refund_money").val();
			var url = "/innerRefund/queryBuymoneyAndTicketpaymoney.do?order_id="+order_id+"&refund_money="+refund_money;
			$.post(url,function(data){
				if(data=="no"){	//验证
					$("#refund_money_info").text("差额退款金额数目不正确").css("color","#f00");
					refundMoney = false;
					$("#btnSubmit").attr("disabled", true);
				}else if(data=="exception"){
					alert("此订单总票价与支付价格其中某个为空！");
					refundMoney = false;
					$("#btnSubmit").attr("disabled", true);
				}
			});
			$("#refund_money_info").text("退款金额正确").css("color","#0b0");	
			refundMoney = true;
		}
	}
	
	function onblurUserRemark(){
		var str = $("#user_remark").val();
		var user_remark = $.trim(str);
		if(user_remark==""){
			$("#user_remark_info").text("退款原因不能为空").css("color","#f00");
			userRemark = false;
			$("#btnSubmit").attr("disabled", true);
		}else{
			$("#user_remark_info").text("退款原因填写正确").css("color","#0b0");
			userRemark = true;
		}
	}
	
</script>
</head>

<body>
	<div class="book_manage account_manage oz" style="margin-top:100px">
	<form action="/innerRefund/refundTicketAdd.do" method="post" name="updateForm" id="updateForm" onsubmit="return checkSubmit();">
        <ul class="order_num oz">
        	<li>订单号：&nbsp;&nbsp;<input type="text" name="order_id" id="order_id"  
        			onblur="onblurOrderId()"/><span id="order_id_info"></span>
        	</li>
            <li>退款金额：<input type="text" name="refund_money" id="refund_money" 
            		onblur="onblurRefundMoney()"/><span id="refund_money_info"></span>
            </li>
            <li class="oz">
            	<label style="float:left;"> 退款原因：</label><textarea rows="5" cols="25" name="user_remark" id="user_remark" 
            		style="width:200px;height:50px;border:1px solid #7F9DB9;"
            	 	onblur="onblurUserRemark()" ></textarea><span id="user_remark_info"></span>
            </li>
        </ul>
        <p><input type="button" value="提 交" class="btn" id="btnSubmit" onclick="submitForm()"/>
           <input type="button" value="返 回" class="btn" onclick="javascript:history.back(-1);"/></p>
    </form>
	</div>
</body>
</html>
