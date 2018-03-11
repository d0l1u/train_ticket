<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>差额退款页</title>
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
	var actualRefundMoney = false;
	var cpId = false;
	var refund12306Seq = false;
	var FormSub = false;
	function isDisabled(){
		if((orderId == true) && (refundMoney == true) && (userRemark == true) && (actualRefundMoney == true) && (cpId == true) && (refund12306Seq == true)){
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
		if($.trim($("#cp_id").val())==""){
			$("#cp_id").focus();
			alert("车票不能为空！");
			return;
		}
		//if($.trim($("#merchant_order_id").val())==""){
		//	$("#merchant_order_id").focus();
		//	alert("商户订单号不能为空！");
		//	return;
		//}
		if($.trim($("#actual_refund_money").val())==""){
			$("#actual_refund_money").focus();
			alert("实际退款金额不能为空！");
			return;
		}
		if($.trim($("#refund_money").val())==""){
			$("#refund_money").focus();
			alert("商户退款金额不能为空！");
			return;
		}
		if($.trim($("#refund_12306_seq").val())==""){
			$("#refund_12306_seq").focus();
			alert("12306退款流水号不能为空！");
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
			$("#order_id").focus();
			$("#order_id_info").text("订单号不能为空").css("color","#f00");
			orderId = false;
		}else{
			var url = "/extRefund/queryRefundTicketAdd.do?order_id="+order_id+"&version="+new Date();
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
			var url = "/extRefund/queryRefundStationTicketCpId.do?cp_id="+cp_id+"&order_id="+order_id+"&version="+new Date();
			$.get(url,function(data){
				if(data == "no"){
					$("#cp_id_info").text("该订单号的该车票号不存在，请重新输入").css("color","#f00");
					cpId = false;
					$("#btnSubmit").attr("disabled", true);
				}
				if(data == "exist"){
					$("#cp_id_info").text("该车票号已经生成退款").css("color","#f00");
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
	/**
	function onblurMerchantOrderId(){
		var str = $("#order_id").val();
		var order_id = $.trim(str);
		if(order_id==""){
			alert("订单号不能为空");
			$("#order_id").focus();
			$("#order_id_info").text("订单号不能为空").css("color","#f00");
			orderId = false;
		}
		var str1 = $("#merchant_order_id").val();
		var merchant_order_id = $.trim(str1);
		if(merchant_order_id==""){
			$("#merchant_order_id_info").text("合作商户订单号不能为空").css("color","#f00");
			merchantOrderId = false;
		}else{
			//查看merchant_order_id是否正确
			var url = "/extRefund/queryRefundStationMerchantOrderId.do?order_id="+order_id+"&version="+new Date();
			$.get(url,function(data){
				if(data == "no"){
					$("#merchant_order_id_info").text("合作商户订单号不存在，请重新输入").css("color","#f00");
					merchantOrderId = false;
					$("#btnSubmit").attr("disabled", true);
				}
				if(data == "yes"){
					$("#merchant_order_id_info").text("合作商户订单号正确").css("color","#0b0");
					merchantOrderId = true;
				}
			});
		}
	}
	*/

	function onblurActualRefundMoney(){
		var actual_refund_money = $.trim($("#actual_refund_money").val());
		$("#actual_refund_money_info").text("");
		var reg = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
		if(actual_refund_money==""){
			$("#actual_refund_money_info").text("退款金额不能为空").css("color","#f00");
			actualRefundMoney = false;
			$("#btnSubmit").attr("disabled", true);
		}else if( (!actual_refund_money=="") && (reg.test(actual_refund_money))==false ){
			$("#actual_refund_money_info").text("格式不正确，请输入有效的金额").css("color","#f00");
			actualRefundMoney = false;
			$("#btnSubmit").attr("disabled", true);
		} else{
			$("#actual_refund_money_info").text("输入正确!").css("color","#0b0");
			actualRefundMoney = true;
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
		}else if( (!refund_money=="") && (reg.test(refund_money))==false ){
			$("#refund_money_info").text("格式不正确，请输入有效的金额").css("color","#f00");
			refundMoney = false;
			$("#btnSubmit").attr("disabled", true);
		} else{
			$("#refund_money_info").text("退款金额正确").css("color","#0b0");
			refundMoney = true;
		}
		/**
		if((!refund_money=="") && (reg.test(refund_money))==true ){
			var order_id = $("#order_id").val();
			var refund_money = $("#refund_money").val();
			var url = "/extRefund/queryBuymoneyAndTicketpaymoney.do?order_id="+order_id+"&refund_money="+refund_money;
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
		*/
	}
	
	function onblurRefund12306Seq(){
		var str = $("#refund_12306_seq").val();
		var refund_12306_seq = $.trim(str);
		if(refund_12306_seq==""){
			$("#refund_12306_seq_info").text("12306退款流水号不能为空").css("color","#f00");
			refund12306Seq = false;
			$("#btnSubmit").attr("disabled", true);
		}else{
			$("#refund_12306_seq_info").text("12306退款流水号填写正确").css("color","#0b0");
			refund12306Seq = true;
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
	<div class="book_manage account_manage oz" style="margin-top:100px;width:700px;">
	<form action="/extRefund/refundStationTicketAdd.do" method="post" name="updateForm" id="updateForm" onsubmit="return checkSubmit();">
        <ul class="order_num oz">
        	<li>订&nbsp;&nbsp;单&nbsp;&nbsp;号：&nbsp;<input type="text" name="order_id" id="order_id" style="width: 200px;" 
        			onblur="onblurOrderId()"/><span id="order_id_info"></span>
        	</li>
        	<li>车&nbsp;&nbsp;票&nbsp;&nbsp;ID：&nbsp;<input type="text" name="cp_id" id="cp_id" style="width: 200px;"
            		onblur="onblurCpId()"/><span id="cp_id_info"></span>
            </li>
            <!-- 
        	<li>商户订单号：&nbsp;<input type="text" name="merchant_order_id" id="merchant_order_id" style="width: 200px;"
            		onblur="onblurMerchantOrderId()"/><span id="merchant_order_id_info"></span>
            </li>
             -->
            <li>退款类型&nbsp;&nbsp;：&nbsp;用户车站退票
            </li>
            <li>退款手续费：
            		<select name="refund_percent" id="refund_percent" style="width: 207px;">
            		    <option value="0%" >0%</option>
		        		<option value="5%" >5%</option>
		        		<option value="10%" >10%</option>
		        		<option value="20%" >20%</option>
		       		</select>
            </li>
            <li>合作商户&nbsp;&nbsp;：
            		<select name="channel" id="channel" style="width: 207px;">
            			<c:forEach var="list" items="${merchantList}" varStatus="idx">
		        			<option value="${list.merchant_id }" >${list.merchant_name }</option>
						</c:forEach>
		       		</select>
            </li>
            <li>实际退款金额：&nbsp;<input type="text" name="actual_refund_money" id="actual_refund_money" style="width: 189px;"
            		onblur="onblurActualRefundMoney()"/><span id="actual_refund_money_info"></span>
            </li>
            <li>商户退款金额：&nbsp;<input type="text" name="refund_money" id="refund_money" style="width: 189px;"
            		onblur="onblurRefundMoney()"/><span id="refund_money_info"></span>
            </li>
            <li>12306退款流水号：&nbsp;<input type="text" name="refund_12306_seq" id="refund_12306_seq" style="width: 171px;"
            		onblur="onblurRefund12306Seq()"/><span id="refund_12306_seq_info"></span>
            </li>
            <li class="oz">
            	<label style="float:left;"> 退款原因&nbsp;&nbsp;：&nbsp;</label><textarea rows="5" name="user_remark" id="user_remark" style="width: 209px; height:50px;border:1px solid #7F9DB9;"
            	 	onblur="onblurUserRemark()" ></textarea><span id="user_remark_info"></span>
            </li>
        </ul>
        <p><input type="button" value="提 交" class="btn" id="btnSubmit" onclick="submitForm()"/>
           <input type="button" value="返 回" class="btn" onclick="javascript:history.back(-1);"/></p>
    </form>
	</div>
</body>
</html>
