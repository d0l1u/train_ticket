<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>线下退款页</title>
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
	var ourRemark = false;
	var RefundMoney = false;
	var cpId = false;
	var FormSub = false;
	function isDisabled(){
		if((orderId == true) &&  (ourRemark == true) && (RefundMoney == true) && (cpId == true) ){
			FormSub = true;
		}else{
			FormSub = false;
		}
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
		if($.trim($("#cp_id").val())==""){
			$("#cp_id").focus();
			alert("车票不能为空！");
			return;
		}
		if($.trim($("#refund_money").val())==""){
			$("#refund_money").focus();
			alert("实际退款金额不能为空！");
			return;
		}
		if($.trim($("#refund_type").val())==""){
			$("#refund_type").focus();
			alert("退款类型不能为空！");
			return;
		}
		if($.trim($("#our_remark").val())==""){
			$("#our_remark").focus();
			alert("退款原因不能为空！");
			return;
		}
		$("#updateForm").submit();
	}
	
	function onblurOrderId(){
		var str = $("#order_id").val();
		var order_id = $.trim(str);
		if(order_id==""){
			$("#order_id").focus();
			$("#order_id_info").text("订单号不能为空").css("color","#f00");
			orderId = false;
		}else{
			var url = "/allRefund/queryRefundTicketAdd.do?order_id="+order_id+"&version="+new Date();
			$.get(url,function(data){
				if(data == "no"){
					$("#order_id_info").text("该订单号不存在，请重新输入").css("color","#f00");
					orderId = false;
					$("#btnSubmit").attr("disabled", true);
				}
				if(data == "yes"){
					$("#order_id_info").text("订单号正确").css("color","#0b0");
					orderId = true;
				}
			});
		}
	}

	function onblurCpId(){
		var str = $("#order_id").val();
		var order_id = $.trim(str);
		if(order_id==""){
			alert("订单号不能为空");
			$("#order_id").focus();
			$("#order_id_info").text("订单号不能为空").css("color","#f00");
			orderId = false;
		}
		var str1 = $("#cp_id").val();
		var cp_id = $.trim(str1);
		if(cp_id==""){
			$("#cp_id_info").text("车票号不能为空").css("color","#f00");
			cpId = false;
		}else{
			//查看order_id下面有没该cp_id
			var url = "/allRefund/queryRefundTicketCpId.do?cp_id="+cp_id+"&order_id="+order_id+"&version="+new Date();
			$.get(url,function(data){
				if(data == "no"){
					$("#cp_id_info").text("该订单号的该车票号不存在，请重新输入").css("color","#f00");
					cpId = false;
					$("#btnSubmit").attr("disabled", true);
				}
				if(data == "yes"){
					$("#cp_id_info").text("车票号正确").css("color","#0b0");
					cpId = true;
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
			RefundMoney = false;
			$("#btnSubmit").attr("disabled", true);
		}else if( (!refund_money=="") && (reg.test(refund_money))==false ){
			$("#refund_money_info").text("格式不正确，请输入有效的金额").css("color","#f00");
			RefundMoney = false;
			$("#btnSubmit").attr("disabled", true);
		} else{
			$("#refund_money_info").text("输入正确!").css("color","#0b0");
			//限制金额不大于票面金额减去已退款金额
			var orderId=$("#order_id").val();
			var cpId=$("#cp_id").val();
			//alert(orderId);
			var refundMoney=$("#refund_money").val();
			//alert(refundMoney);
			var url = "/allRefund/queryRefundMoney.do?orderId="+orderId+"&cpId="+cpId+"&refundMoney="+refundMoney+"&version="+new Date();
			$.get(url,function(data){
				if(data == "no" ){
				$("#refund_money_info").text("退款金额不能大于票面金额!").css("color","#f00");
				RefundMoney = false;
				$("#btnSubmit").attr("disabled", true);
				}else{
				$("#refund_money_info").text("当前已退款总金额："+data ).css("color","#f00");
				}
			});
		
			RefundMoney = true;
		}
		
	}
	
	function onblurOurRemark(){
		var str = $("#our_remark").val();
		var our_remark = $.trim(str);
		if(our_remark==""){
			$("#our_remark_info").text("退款原因不能为空").css("color","#f00");
			ourRemark = false;
			$("#btnSubmit").attr("disabled", true);
		}else{
			$("#our_remark_info").text("退款原因填写正确").css("color","#0b0");
			ourRemark = true;
		}
	}
	
</script>
</head>

<body>
	<div class="book_manage account_manage oz" style="margin-top:100px;width:700px;">
	<form action="/allRefund/addRefundTicket.do" method="post" name="updateForm" id="updateForm" onsubmit="return checkSubmit();">
        <ul class="order_num oz">
       		<li>渠&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;道：&nbsp;${channel }
        	</li>
        	<li>订&nbsp;&nbsp;单&nbsp;&nbsp;号：&nbsp;<input type="text" name="order_id" id="order_id" style="width: 200px;" 
        			onblur="onblurOrderId()"/><span id="order_id_info"></span>
        	</li>
        	<li>车&nbsp;&nbsp;票&nbsp;&nbsp;ID：&nbsp;<input type="text" name="cp_id" id="cp_id" style="width: 200px;"
            		onblur="onblurCpId()"/><span id="cp_id_info"></span>
            </li>
            
            <li>退款类型&nbsp;&nbsp;：<input type="radio" id="refund_type" name="refund_type" />
            </li>
            <li>12306退款流水号：&nbsp;<input type="text" name="refund_12306_seq" id="refund_12306_seq" style="width: 171px;"
            	/><span id="refund_12306_seq_info"></span>
            </li>
             <li>退款金额&nbsp;&nbsp;：&nbsp;<input type="text" name="refund_money" id="refund_money" style="width: 200px;"
            		onblur="onblurRefundMoney()"/><span id="refund_money_info"></span>
            </li>
            <li>12306退款金额：<input type="text" name="actual_refund_money" id="actual_refund_money" style="width: 190px;" />
            </li>
            <li class="oz">
            	<label style="float:left;"> 退款原因&nbsp;&nbsp;：&nbsp;</label>
            	<textarea rows="5" name="our_remark" id="our_remark" style="width: 209px; height:50px;border:1px solid #7F9DB9;"
            	 	onblur="onblurOurRemark()" ></textarea><span id="our_remark_info"></span>
            </li>
        </ul>
        <p><input type="button" value="提 交" class="btn" id="btnSubmit" onclick="submitForm()"/>
           <input type="button" value="返 回" class="btn" onclick="javascript:history.back(-1);"/></p>
    </form>
	</div>
</body>
</html>
