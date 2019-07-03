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
		document.getElementById("agreeRefundbutton").click();
		
	});
	$().ready(function(){
	    var timer = setInterval("isDisabled();",500);
	    var timerRefuse = setInterval("refuseIsDisabled();",500);
	});
	var orderId = false;
	var refund12306Seq = false;
	var actualRefundMoney = false;
	var alterTicketsMoney = false;
	var refundMoney = false;
	var bankUsername = false;
	var bankType1 = true;
	var bankAccount = false;
	var bankOpenName = false;
	var FormSub = false;
	var FormRefundSub = false;
	function isDisabled(){
		if((orderId == true) && (refundMoney == true) && (actualRefundMoney == true) && (refund12306Seq == true)
				&& (alterTicketsMoney == true) && (bankUsername == true) && (bankOpenName == true) && (bankAccount == true) && (bankType1 == true)){
			FormSub = true;
		}else{
			FormSub = false;
		}
		//alert("orderId:"+orderId+"--refundMoney:"+refundMoney+"--actualRefundMoney:"+actualRefundMoney+"--refund12306Seq:"+refund12306Seq
		//		+"--alterTicketsMoney:"+alterTicketsMoney+"--bankUsername:"+bankUsername+"--bankOpenName:"+bankOpenName
		//		+"--bankAccount:"+bankAccount+"--bankType1:"+bankType1+"--FormSub:"+FormSub);
		if(FormSub == true){
			$("#btnSubmit").attr("disabled", false);
		}
		if((refundMoney == true) && (actualRefundMoney == true) && (refund12306Seq == true)
				&& (alterTicketsMoney == true) && (bankUsername == true) && (bankOpenName == true) && (bankAccount == true) && (bankType1 == true)){
			FormRefundSub = true;
		}else{
			FormRefundSub = false;
		}
		if(FormRefundSub == true){
			$("#btnRefundSubmit").attr("disabled", false);
		}
	}
	var userRemark = false;
	var orderId1 = false;
	var refuseForm = false;
	function refuseIsDisabled(){
		if((orderId1 == true) && (userRemark == true)){
			refuseForm = true;
		}else{
			refuseForm = false;
		}
		if(refuseForm == true){
			$("#btnSubmit1").attr("disabled", false);
		}
	}
	//同意退款
	function submitForm(){
		if($.trim($("#order_id").val())==""){
			$("#order_id").focus();
			alert("订单号不能为空！");
			return;
		}
		if($.trim($("#refund_12306_seq").val())==""){
			$("#refund_12306_seq").focus();
			alert("12306退款流水号不能为空！");
			return;
		}
		if($.trim($("#actual_refund_money").val())==""){
			$("#actual_refund_money").focus();
			alert("12306退款金额不能为空！");
			return;
		}
		if($.trim($("#alter_tickets_money").val())==""){
			$("#alter_tickets_money").focus();
			alert("改签差价不能为空！");
			return;
		}
		
		if($.trim($("#refund_money").val())==""){
			$("#refund_money").focus();
			alert("退款金额不能为空！");
			return;
		}
		if($.trim($("#bank_username").val())==""){
			$("#bank_username").focus();
			alert("银行账户姓名不能为空！");
			return;
		}
		if(($.trim($("#bank_type").val())=="") && ($.trim($("#bank_type1").val())=="")){
			//$("#bank_username").focus();
			alert("乘客银行类型不能为空！");
			return;
		}
		if($.trim($("#bank_account").val())==""){
			$("#bank_account").focus();
			alert("乘客银行账号不能为空！");
			return;
		}
		if($.trim($("#bank_openName").val())==""){
			$("#bank_openName").focus();
			alert("开户行名称不能为空！");
			return;
		}
		$("form:first").attr("action","/appRefund/addPhoneRefundTicket.do?type=agree");
		$("form:first").submit();
	}
	//拒绝退款
	function submitrefuseForm(){
		if($.trim($("#order_id1").val())==""){
			$("#order_id1").focus();
			alert("订单号不能为空！");
			return;
		}
		if($.trim($("#user_remark").val())==""){
			$("#user_remark").focus();
			alert("退款原因不能为空！");
			return;
		}
		$("form:first").attr("action","/appRefund/addPhoneRefundTicket.do?type=refuse");
		$("form:first").submit();
	}
	//同意退款
	function submitRefundForm(){
		if($.trim($("#refund_12306_seq").val())==""){
			$("#refund_12306_seq").focus();
			alert("12306退款流水号不能为空！");
			return;
		}
		if($.trim($("#actual_refund_money").val())==""){
			$("#actual_refund_money").focus();
			alert("12306退款金额不能为空！");
			return;
		}
		if($.trim($("#alter_tickets_money").val())==""){
			$("#alter_tickets_money").focus();
			alert("改签差价不能为空！");
			return;
		}
		if($.trim($("#refund_money").val())==""){
			$("#refund_money").focus();
			alert("退款金额不能为空！");
			return;
		}
		if($.trim($("#bank_username").val())==""){
			$("#bank_username").focus();
			alert("银行账户姓名不能为空！");
			return;
		}
		if(($.trim($("#bank_type").val())=="") && ($.trim($("#bank_type1").val())=="")){
			//$("#bank_username").focus();
			alert("乘客银行类型不能为空！");
			return;
		}
		if($.trim($("#bank_account").val())==""){
			$("#bank_account").focus();
			alert("乘客银行账号不能为空！");
			return;
		}
		if($.trim($("#bank_openName").val())==""){
			$("#bank_openName").focus();
			alert("开户行名称不能为空！");
			return;
		}
		$("form:first").attr("action","/appRefund/addPhoneRefundTicket.do?type=agree&refundAgain=1");
		$("form:first").submit();
	}
	function onblurOrderId(){
		var str = $("#order_id").val();
		var order_id = $.trim(str);
		if(order_id==""){
			$("#order_id").focus();
			$("#order_id_info").text("订单号不能为空").css("color","#f00");
			orderId = false;
		}else{
			var url = "/appRefund/queryRefundTicketAdd.do?order_id="+order_id+"&version="+new Date();
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
			var url = "/appRefund/queryRefundStationTicketCpId.do?cp_id="+cp_id+"&order_id="+order_id+"&version="+new Date();
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
			var url = "/appRefund/queryRefundStationMerchantOrderId.do?order_id="+order_id+"&version="+new Date();
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
			$("#actual_refund_money_info").text("12306退款金额不能为空").css("color","#f00");
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

	function onblurAlterTicketsMoney(){
		var alter_tickets_money = $.trim($("#alter_tickets_money").val());
		$("#alter_tickets_money_info").text("");
		var reg = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
		if(alter_tickets_money==""){
			$("#alter_tickets_money_info").text("改签差价不能为空").css("color","#f00");
			alterTicketsMoney = false;
			$("#btnSubmit").attr("disabled", true);
		}else if( (!alter_tickets_money=="") && (reg.test(alter_tickets_money))==false ){
			$("#alter_tickets_money_info").text("格式不正确，请输入有效的金额").css("color","#f00");
			alterTicketsMoney = false;
			$("#btnSubmit").attr("disabled", true);
		} else{
			$("#alter_tickets_money_info").text("输入正确!").css("color","#0b0");
			alterTicketsMoney = true;
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
			var url = "/appRefund/queryBuymoneyAndTicketpaymoney.do?order_id="+order_id+"&refund_money="+refund_money;
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

	function onblurBankUsername(){
		var str = $("#bank_username").val();
		var bank_username = $.trim(str);
		if(bank_username==""){
			$("#bank_username_info").text("银行账户姓名不能为空").css("color","#f00");
			bankUsername = false;
			$("#btnSubmit").attr("disabled", true);
		}else{
			$("#bank_username_info").text("银行账户姓名填写正确").css("color","#0b0");
			bankUsername = true;
		}
	}

	function onblurBankAccount(){
		var str = $("#bank_account").val();
		var bank_account = $.trim(str);
		if(bank_account==""){
			$("#bank_account_info").text("乘客银行账号不能为空").css("color","#f00");
			bankAccount = false;
			$("#btnSubmit").attr("disabled", true);
		}else{
			$("#bank_account_info").text("乘客银行账号填写正确").css("color","#0b0");
			bankAccount = true;
		}
	}

	function onblurBankOpenName(){
		var str = $("#bank_openName").val();
		var bank_openName = $.trim(str);
		if(bank_openName==""){
			$("#bank_openName_info").text("开户行名称不能为空").css("color","#f00");
			bankOpenName = false;
			$("#btnSubmit").attr("disabled", true);
		}else{
			$("#bank_openName_info").text("开户行名称填写正确").css("color","#0b0");
			bankOpenName = true;
		}
	}
	
	function onblurOrderId1(){
		var str = $("#order_id1").val();
		var order_id1 = $.trim(str);
		if(order_id1==""){
			$("#order_id1").focus();
			$("#order_id_info1").text("订单号不能为空").css("color","#f00");
			orderId1 = false;
		}else{
			var url = "/appRefund/queryRefundTicketAdd.do?order_id="+order_id1+"&version="+new Date();
			$.get(url,function(data){
				if(data == "no"){
					$("#order_id_info1").text("该订单号不存在，请重新输入").css("color","#f00");
					orderId1 = false;
					$("#btnSubmit1").attr("disabled", true);
				}
				if(data == "yes"){
					$("#order_id_info1").text("订单号正确").css("color","#0b0");
					orderId1 = true;
				}
			});
		}
	}
	function onblurUserRemark(){
		var str = $("#user_remark").val();
		var user_remark = $.trim(str);
		if(user_remark==""){
			$("#user_remark_info").text("退款原因不能为空").css("color","#f00");
			userRemark = false;
			$("#btnSubmit1").attr("disabled", true);
		}else{
			$("#user_remark_info").text("退款原因填写正确").css("color","#0b0");
			userRemark = true;
		}
	}

	function agreeRefund(){
		$("#agreeRefundbutton").css("backgroundColor","#2C99FF");
		$("#refuseRefundbutton").css("backgroundColor","#ECE9D8");
		 //document.getElementById("agreeRefundbutton").style.backgroundColor = "#2C99FF";
		document.getElementById('agreeRefund').style.display = "block";
		document.getElementById('refuseRefund').style.display = "none";
	}

	function refuseRefund(){
		$("#refuseRefundbutton").css("backgroundColor","#2C99FF");
		$("#agreeRefundbutton").css("backgroundColor","#ECE9D8");
		document.getElementById('agreeRefund').style.display = "none";
		document.getElementById('refuseRefund').style.display = "block";
	}
	$(function() {
        $('#bank_type').change(function() {
            if (this.value == '其他') {
                $('#bank_type1').show();
                setInterval('onblurBankType1()',500);
            } else {
                $('#bank_type1').hide();
                $("#bank_type_info").text("乘客银行类型填写正确").css("color","#0b0");
    			bankType1 = true;
            }
        });
    });
	function onblurBankType1(){
		var str = $("#bank_type1").val();
		var bank_type1 = $.trim(str);
		if(bank_type1==""){
			$("#bank_type_info").text("乘客银行类型不能为空").css("color","#f00");
			bankType1 = false;
			$("#btnSubmit").attr("disabled", true);
		}else{
			$("#bank_type_info").text("乘客银行类型填写正确").css("color","#0b0");
			bankType1 = true;
		}
	}
</script>
<style>
	.refund{text-align:center;font-size:26px;font-weight:bolder;border:none;color:#fff;cursor:pointer;width:350px;height:60px;}
</style>
</head>

<body>
	<div class="book_manage account_manage oz" style="margin-top:70px;width:710px;border:1px solid grey;">
	<input type="button" value="同意退款" id="agreeRefundbutton" onclick="agreeRefund();" class="refund" />
	<c:if test="${!(refund eq '1')}">
	<input type="button" value="拒绝退款" id="refuseRefundbutton" onclick="refuseRefund();" class="refund" />
	</c:if>
	<form action="/appRefund/addPhoneRefundTicket.do" method="post" name="updateForm" id="updateForm" onsubmit="return checkSubmit();">
    <div class="book_manage account_manage oz" id="agreeRefund" style="display:none;margin:10px auto;width:600px;">
        <ul class="order_num oz">
        	<li></li>
        	<c:choose>
        		<c:when test="${refund eq '1'}">
        			<input type="hidden" name="order_id" id="order_id" value="${order_id }"/>
        			<input type="hidden" name="refund_type" id="refund_type" value="${refund_type }"/>
        			<li>
		        		订&nbsp;&nbsp;单&nbsp;&nbsp;号：&nbsp;${order_id }
		        	</li>
		        	<li>
		        		退款类型&nbsp;&nbsp;：&nbsp;${refund_types[refund_type] }
		        	</li>
        		</c:when>
				<c:otherwise>
					<li>订&nbsp;&nbsp;单&nbsp;&nbsp;号：&nbsp;<input type="text" name="order_id" id="order_id" style="width: 200px;" 
		        			onblur="onblurOrderId()"/><span id="order_id_info"></span>
		        	</li>
		            <li>退款类型&nbsp;&nbsp;：
		            		<select name="refund_type" id="refund_type" style="width: 207px;">
				        		<option value="3" >用户电话退票</option>
				        		<option value="4" >差额退款</option>
				       		</select>
		            </li>
				</c:otherwise>        		
        	</c:choose>
        	
            <!-- 
            <li>退款手续费：
            		<select name="refund_percent" id="refund_percent" style="width: 207px;">
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
             -->
            <li>12306退款流水号：&nbsp;<input type="text" name="refund_12306_seq" id="refund_12306_seq" style="width: 171px;"
            		onblur="onblurRefund12306Seq()"/><span id="refund_12306_seq_info"></span>
            </li>
            <li>12306退款金额：&nbsp;<input type="text" name="actual_refund_money" id="actual_refund_money" style="width: 182px;"
            		onblur="onblurActualRefundMoney()"/><span id="actual_refund_money_info"></span>
            </li>
            <li>改签差价：&nbsp;<input type="text" name="alter_tickets_money" id="alter_tickets_money" style="width: 213px;"
            		onblur="onblurAlterTicketsMoney()"/><span id="alter_tickets_money_info"></span>
            </li>
            <li>退款金额(代理商)：&nbsp;<input type="text" name="refund_money" id="refund_money" style="width: 165px;"
            		onblur="onblurRefundMoney()"/><span id="refund_money_info"></span>
            </li>
            <li>银行账户姓名：&nbsp;<input type="text" name="bank_username" id="bank_username" style="width: 189px;"
            		onblur="onblurBankUsername()"/><span id="bank_username_info"></span>
            </li>
            <li>乘客银行类型：
            		<select name="bank_type" id="bank_type">
		        		<option value="工商银行" >工商银行</option>
		        		<option value="农业银行" >农业银行</option>
		        		<option value="招商银行" >招商银行</option>
		        		<option value="建设银行" >建设银行</option>
		        		<option value="中国银行" >中国银行</option>
		        		<option value="浦发银行" >浦发银行</option>
		        		<option value="民生银行" >民生银行</option>
		        		<option value="深发展银行" >深发展银行</option>
		        		<option value="兴业银行" >兴业银行</option>
		        		<option value="平安银行" >平安银行</option>
		        		<option value="交通银行" >交通银行</option>
		        		<option value="中信银行" >中信银行</option>
		        		<option value="光大银行" >光大银行</option>
		        		<option value="上海银行" >上海银行</option>
		        		<option value="华夏银行" >华夏银行</option>
		        		<option value="广发银行" >广发银行</option>
		        		<option value="北京银行" >北京银行</option>
		        		<option value="其他" >其他</option>
		       		</select>
		       		<input type="text" name="bank_type1" id="bank_type1" style="width: 150px;display:none;"/><span id="bank_type_info"></span>
            </li>
            <li>乘客银行账号：&nbsp;<input type="text" name="bank_account" id="bank_account" style="width: 189px;"
            		onblur="onblurBankAccount()"/><span id="bank_account_info"></span>
            </li>
            <li>开户行名称：&nbsp;<input type="text" name="bank_openName" id="bank_openName" style="width: 201px;"
            		onblur="onblurBankOpenName()"/><span id="bank_openName_info"></span>
            </li>
            
            
        </ul>
        <p>
        	<c:choose>
        		<c:when test="${refund eq '1'}">
        			<input type="button" value="同意退款" class="btn" id="btnRefundSubmit" onclick="submitRefundForm()"/>
        		</c:when>
        		<c:otherwise>
        			<input type="button" value="同意退款" class="btn" id="btnSubmit" onclick="submitForm()"/>
        		</c:otherwise>		
        	</c:choose>
            <input type="button" value="返 回" class="btn" onclick="javascript:history.back(-1);"/></p>
    </div>
    
    <div class="book_manage account_manage oz" id="refuseRefund" style="display:none;margin:10px auto;width:600px;">
        <ul class="order_num oz">
        	<li></li>
        	<li>订&nbsp;&nbsp;单&nbsp;&nbsp;号&nbsp;&nbsp;：&nbsp;<input type="text" name="order_id1" id="order_id1" style="width: 200px;" 
        			onblur="onblurOrderId1();"/><span id="order_id_info1"></span>
        	</li>
            <li>退款类型&nbsp;&nbsp;&nbsp;&nbsp;：
					<select name="refund_type1" id="refund_type1" style="width: 207px;">
		        		<option value="3" >用户电话退票</option>
		        		<option value="4" >差额退款</option>
		       		</select>
            </li>
            <li class="oz">
            	<label style="float:left;"> 拒绝退款原因：&nbsp;</label><textarea rows="5" name="user_remark" id="user_remark" style="width:209px;height:50px;border:1px solid #7F9DB9;"
            	 	onblur="onblurUserRemark()" ></textarea><span id="user_remark_info"></span>
            </li>
        </ul>
        <p><input type="button" value="拒绝退款" class="btn" id="btnSubmit1" onclick="submitrefuseForm()"/>
           <input type="button" value="返 回" class="btn" onclick="javascript:history.back(-1);"/></p>
    </div>
    </form>
	</div>
</body>
</html>
