<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>增加商户页</title>
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
	    setInterval("isDisabled();",500);
	});
	var MerchantId = false;
	var MerchantName = false;
	var SpareTicketAmount = false;
	var FormSub = false;
	function isDisabled(){
		if((MerchantId == true) && (MerchantName == true) && (SpareTicketAmount == true)){
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
		if($.trim($("#merchant_name").val())==""){
			$("#merchant_name").focus();
			alert("商户名称不能为空！");
			return;
		}
		var merchant_id = $.trim($("#merchant_id").val());
		if(merchant_id==""){
			$("#merchant_id").focus();
			alert("商户编号不能为空！");
			return;
		} 
		if($.trim($("#spare_ticket_amount").val())==""){
			$("#spare_ticket_amount").focus();
			alert("余票阀值不能为空！");
			return;
		}
		var pay_type = $('input:radio[name="pay_type"]:checked').val();   
		if(pay_type == null){   
			alert("支付方式不能为空！");   
			return;   
		}   
		var bx_company = $('input:radio[name="bx_company"]:checked').val();   
		if(bx_company == null){   
			alert("保险单位不能为空！");   
			return;   
		}   
		var sms_channel = $('input:radio[name="sms_channel"]:checked').val();   
		if(sms_channel == null){   
			alert("短信渠道不能为空！");   
			return;   
		}   
		var stop_buyTicket_time = $('input:radio[name="stop_buyTicket_time"]:checked').val();   
		if(stop_buyTicket_time == null){   
			alert("开车前订票时间不能为空！");   
			return;   
		}  
		$("#updateForm").submit();
	}

	function onblurMerchantId(){
		var str = $("#merchant_id").val();
		var merchant_id = $.trim(str);
		if(merchant_id==""){
			$("#merchant_id").focus();
			$("#merchant_id_info").text("商户编号不能为空").css("color","#f00");
			MerchantId = false;
		}else{
			var url = "/extSetting/queryMarchantId.do?merchant_id="+merchant_id+"&version="+new Date();
			$.get(url,function(data){
				if(data == "yes"){
					$("#merchant_id_info").text("该商户编号已经存在，请重新输入").css("color","#f00");
					MerchantId = false;
					$("#btnSubmit").attr("disabled", true);
				}
				if(data == "no"){
					$("#merchant_id_info").text("商户编号正确").css("color","#0b0");
					MerchantId = true;
				}
			});
		}
	}

	function onblurMerchantName(){
		var str = $("#merchant_name").val();
		var merchant_name = $.trim(str);
		if(merchant_name==""){
			$("#merchant_name_info").text("商户名称不能为空").css("color","#f00");
			$("#btnSubmit").attr("disabled", true);
			MerchantName = false;
		}else{
			$("#merchant_name_info").text("商户名称填写正确").css("color","#0b0");
			MerchantName = true;
		}
	}

	function onblurSpareTicketAmount(){
		var str = $("#spare_ticket_amount").val();
		var spare_ticket_amount = $.trim(str);
		if(spare_ticket_amount==""){
			$("#spare_ticket_amount_info").text("余票阀值不能为空").css("color","#f00");
			SpareTicketAmount = false;
			$("#btnSubmit").attr("disabled", true);
		}else{
			$("#spare_ticket_amount_info").text("余票阀值填写正确").css("color","#0b0");
			SpareTicketAmount = true;
		}
	}
</script>
</head>
<body>
	<div class="book_manage account_manage oz" style="margin-top:100px;width:700px;">
	<form action="/extSetting/addMarchantInfo.do" method="post" name="updateForm" id="updateForm" onsubmit="return checkSubmit();">
        <ul class="order_num oz">
        	<li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;商户名称：&nbsp;
        		<input type="text" name="merchant_name" id="merchant_name" style="width: 200px;" 
					onblur="onblurMerchantName()"/><span id="merchant_name_info"></span>
        	</li>
        	<li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;商户编号：&nbsp;
        		<input type="text" name="merchant_id" id="merchant_id" style="width: 200px;" 
					onblur="onblurMerchantId()"/><span id="merchant_id_info"></span>
            </li>
            <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;余票阀值：&nbsp;
        		<input type="text" name="spare_ticket_amount" id="spare_ticket_amount" style="width: 200px;" 
					onblur="onblurSpareTicketAmount()"/><span id="spare_ticket_amount_info"></span>
            </li>
            <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;扣费方式：&nbsp;
            	<!-- 
            	<div style="display:inline;border:1px red solid;">
            		<c:forEach items="${merchantFees }" var="s" varStatus="index">
						<input type="radio" id="merchant_fee${index.count }"
								name="merchant_fee" value="${s.key }" />
							<label for="merchant_fee${index.count }">
									${s.value }
							</label>
            		<c:if test="${index.count % 7 == 0}"><br/></c:if>
					</c:forEach>
				</div>
				 -->	
            		<select name="merchant_fee" id=merchant_fee style="width: 207px;">
            			<c:forEach var="list" items="${merchantFees}" varStatus="idx">
		        			<option value="${list.key }" >${list.value }</option>
						</c:forEach>
		       		</select>
            </li>
            <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;支付方式：
            		<c:forEach items="${payTypes }" var="s" varStatus="index">
						<input type="radio" id="pay_type${index.count }"
								name="pay_type" value="${s.key }" />
							<label for="pay_type${index.count }">
									${s.value }
							</label>
					</c:forEach>
            </li>
            <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;保险单位：
            		<c:forEach items="${bxCompanys }" var="s" varStatus="index">
						<input type="radio" id="bx_company${index.count }"
								name="bx_company" value="${s.key }" />
							<label for="bx_company${index.count }">
									${s.value }
							</label>
					</c:forEach>
            </li>
            <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;短信渠道：
		       		<c:forEach items="${smsChannels }" var="s" varStatus="index">
						<input type="radio" id="sms_channel${index.count }"
								name="sms_channel" value="${s.key }" />
							<label for="sms_channel${index.count }">
									${s.value }
							</label>
					</c:forEach>
            </li>
            <li>开车前订票时间：
            		<input type="radio" id="stop_buyTicket_time1" name="stop_buyTicket_time" value="3" />
						<label for="stop_buyTicket_time1">3小时</label>
					<input type="radio" id="stop_buyTicket_time2" name="stop_buyTicket_time" value="4" />
						<label for="stop_buyTicket_time2">4小时</label>
					<input type="radio" id="stop_buyTicket_time3" name="stop_buyTicket_time" value="5" />
						<label for="stop_buyTicket_time3">5小时</label>
					<input type="radio" id="stop_buyTicket_time4" name="stop_buyTicket_time" value="6" />
						<label for="stop_buyTicket_time4">6小时</label>
					<!-- 	
            		<select name="stop_buyTicket_time" id=stop_buyTicket_time style="width: 207px;">
		        		<option value="3" >3小时</option>
		        		<option value="4" >4小时</option>
		        		<option value="5" >5小时</option>
		        		<option value="6" >6小时</option>
		       		</select>
		       		 -->
            </li>
        </ul>
        <br/>
        <p>&nbsp;&nbsp;&nbsp;&nbsp;
        	<input type="button" value="提 交" class="btn" id="btnSubmit" onclick="submitForm()"/>
           <input type="button" value="返 回" class="btn" onclick="javascript:history.back(-1);"/></p>
    </form>
	</div>
</body>
</html>
