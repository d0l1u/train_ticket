<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>修改商户页</title>
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
		if(SpareTicketAmount == true){
			FormSub = true;
		}else{
			FormSub = false;
		}
		if(FormSub == true){
			$("#btnSubmit").attr("disabled", false);
		}
	}

	function submitForm(){		
		if($.trim($("#spare_ticket_amount").val())==""){
			$("#spare_ticket_amount").focus();
			alert("余票阀值不能为空！");
			return;
		}		
		$("#updateForm").submit();
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
<style>
	.book_manage TABLE{width:700px;}
	.book_manage TABLE tr td{padding-top:10px;padding-bottom:10px;}
</style>
</head>
<body>
	<div class="book_manage account_manage oz" style="margin-top:100px;width:700px;">
	<form action="/extSetting/updateMarchantInfo.do" method="post" name="updateForm" id="updateForm" onsubmit="return checkSubmit();">
        <input type="hidden" name="merchant_id" value="${merchantInfo.merchant_id }" />
        <input type="hidden" name="merchant_name" value="${merchantInfo.merchant_name }" />
        <table style="border:#dadada 0px solid;">
        	<tr>
        		<td style="text-align:right;">商户名称：</td>
        		<td style="text-align:left;">&nbsp;${merchantInfo.merchant_name }</td>
        	</tr>
        	<tr>
        		<td style="text-align:right;">商户编号：</td>
        		<td style="text-align:left;">&nbsp;${merchantInfo.merchant_id }</td>
        	</tr>
        	<tr>
        		<td style="text-align:right;">余票阀值：</td>
        		<td style="text-align:left;">
					<input type="text" name="spare_ticket_amount" id="spare_ticket_amount" style="width: 200px;" value="${merchantInfo.spare_ticket_amount }"
					onblur="onblurSpareTicketAmount()"/><span id="spare_ticket_amount_info"></span>
				</td>
        	</tr>
        	<tr>
        		<td style="text-align:right;">扣费方式：</td>
        		<td style="text-align:left;">
        			<c:forEach items="${merchantFees }" var="s" varStatus="index">
						<input type="radio" id="merchant_fee${index.count }" name="merchant_fee" value="${s.key }" 
							<c:if test="${fn:contains(merchantInfo.merchant_fee, s.key ) }">checked="checked"</c:if> />
							<label for="merchant_fee${index.count }">
									${s.value }
							</label>
            		<c:if test="${index.count % 7 == 0}"><br/></c:if>
					</c:forEach>
				</td>
        	</tr>
        	<tr>
        		<td style="text-align:right;">支付方式：</td>
        		<td style="text-align:left;">
        			<c:forEach items="${payTypes }" var="s" varStatus="index">
						<input type="radio" id="pay_type${index.count }" name="pay_type" value="${s.key }"
							<c:if test="${fn:contains(merchantInfo.pay_type, s.key ) }">checked="checked"</c:if> />
							<label for="pay_type${index.count }">
									${s.value }
							</label>
					</c:forEach>
				</td>
        	</tr>
        	<tr>
        		<td style="text-align:right;">保险单位：</td>
        		<td style="text-align:left;">
        			<c:forEach items="${bxCompanys }" var="s" varStatus="index">
						<input type="radio" id="bx_company${index.count }" name="bx_company" value="${s.key }" 
							<c:if test="${fn:contains(merchantInfo.bx_company, s.key ) }">checked="checked"</c:if> />
							<label for="bx_company${index.count }">
									${s.value }
							</label>
					</c:forEach>
				</td>
        	</tr>
        	<tr>
        		<td style="text-align:right;">短信渠道：</td>
        		<td style="text-align:left;">
        			<c:forEach items="${smsChannels }" var="s" varStatus="index">
						<input type="radio" id="sms_channel${index.count }" name="sms_channel" value="${s.key }" 
							<c:if test="${fn:contains(merchantInfo.sms_channel, s.key ) }">checked="checked"</c:if> />
							<label for="sms_channel${index.count }">
									${s.value }
							</label>
					</c:forEach>
				</td>
        	</tr>
        	<tr>
        		<td style="text-align:right;">开车前订票时间：</td>
        		<td style="text-align:left;">
        		  <input type="radio" id="stop_buyTicket_time0.5" name="stop_buyTicket_time" value="0.5" 
        				<c:if test="${merchantInfo.stop_buyTicket_time eq 0.5  }">checked="checked"</c:if> />
						<label for="stop_buyTicket_time0.5">30分钟</label>
        		    <input type="radio" id="stop_buyTicket_time0" name="stop_buyTicket_time" value="2" 
        				<c:if test="${merchantInfo.stop_buyTicket_time eq 2  }">checked="checked"</c:if> />
						<label for="stop_buyTicket_time0">2小时</label>
        			<input type="radio" id="stop_buyTicket_time1" name="stop_buyTicket_time" value="3" 
        				<c:if test="${merchantInfo.stop_buyTicket_time eq 3  }">checked="checked"</c:if> />
						<label for="stop_buyTicket_time1">3小时</label>
					<input type="radio" id="stop_buyTicket_time2" name="stop_buyTicket_time" value="4" 
						<c:if test="${merchantInfo.stop_buyTicket_time eq 4  }">checked="checked"</c:if> />
						<label for="stop_buyTicket_time2">4小时</label>
					<input type="radio" id="stop_buyTicket_time3" name="stop_buyTicket_time" value="5" 
						<c:if test="${merchantInfo.stop_buyTicket_time eq 5  }">checked="checked"</c:if> />
						<label for="stop_buyTicket_time3">5小时</label>
					<input type="radio" id="stop_buyTicket_time4" name="stop_buyTicket_time" value="6" 
						<c:if test="${merchantInfo.stop_buyTicket_time eq 6  }">checked="checked"</c:if> />
						<label for="stop_buyTicket_time4">6小时</label>
				</td>
        	</tr>
        	
        	<tr>
        		<td style="text-align:right;">验证状态：</td>
        		<td style="text-align:left;">
        			<input type="radio" id="verify_status00" name="verify_status" value="00" 
        				<c:if test="${merchantInfo.verify_status eq '00'  }">checked="checked"</c:if> />
						<label for="verify_status00">停用</label>
        		    <input type="radio" id="verify_status11" name="verify_status" value="11" 
        				<c:if test="${merchantInfo.verify_status eq '11'  }">checked="checked"</c:if> />
						<label for="verify_status11">开启</label>
        		</td>
        	</tr>
        	<tr>
        		<td style="text-align:right;">验证总数：</td>
        		<td style="text-align:left;">
					${merchantInfo.verify_total_num }
        		</td>
        	</tr>
        </table>
        
        <br/><br/>
        <p>&nbsp;&nbsp;&nbsp;&nbsp;
        	<input type="button" value="提 交" class="btn" id="btnSubmit" onclick="submitForm()"/>
            <input type="button" value="返 回" class="btn" onclick="javascript:history.back(-1);"/></p>
    </form>
	</div>
</body>
</html>
