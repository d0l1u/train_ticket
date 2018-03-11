<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>美团线下退款页</title>
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
		
	$().ready(function(){
		$("#updateForm").validate(); 
		var str = $("#order_id").val();
		var order_id = $.trim(str);
		if(order_id!=""){
			orderId = true;
			$("#cpidAndName").show();	
		}
	});
	$().ready(function(){
	    var timer=setInterval("isDisabled();",500);
	});
	var orderId = false;
	//var ourRemark = false;
	var RefundMoney = false;
	//var cpId = false;
	//var refund12306Seq = false;
	var FormSub = false;
	function isDisabled(){
	
		//if((orderId == true) &&  (ourRemark == true) && (RefundMoney == true)  ){
		if((orderId == true) &&  (RefundMoney == true)  ){
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
		var	cp_id;
		 $("input[name='cpid']:radio:checked").each(function(){ 
				cp_id= $(this).val();
		});
		if(cp_id=="" || cp_id==null){
				alert("未选择车票！");
				return;
		}
		if($.trim($("#refund_money").val())==""){
			$("#refund_money").focus();
			alert("实际退款金额不能为空！");
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
			var url = "/meituanRefund/queryRefundTicketAdd.do?order_id="+order_id+"&version="+new Date();
			$.get(url,function(data){
				if(data == "no"){
					$("#order_id_info").text("该订单号不存在，请重新输入").css("color","#f00");
					orderId = false;
					$("#btnSubmit").attr("disabled", true);
				}
				if(data == "yes"){
					$("#order_id_info").text("订单号正确").css("color","#0b0");
					showCpId(order_id);
					orderId = true;
				}
			});
		}
	}

	function showCpId(order_id){
			$("#cpidAndName").show();	
			$("form:first").attr("action","/meituanRefund/toAddRefundPage.do?order_id="+order_id);
			$("form:first").submit();
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
			var	cpId=null;
			 $("input[name='cpid']:radio:checked").each(function(){ 
				cpId= $(this).val();
				}); 
			var refundMoney=$("#refund_money").val();
			//alert(refundMoney);
			var url = "/meituanRefund/queryRefundMoney.do?orderId="+orderId+"&cpId="+cpId+"&refundMoney="+refundMoney+"&version="+new Date();
			$.get(url,function(data){
				if(data == "no" ){
				$("#refund_money_info").text("退款金额不能大于票面金额!").css("color","#f00");
				RefundMoney = false;
				$("#btnSubmit").attr("disabled", true);
				 document.getElementById('alterSubmit').style.display = "block";
				}else{
				$("#refund_money_info").text("当前已退款总金额："+data ).css("color","#f00");
				}
			});
		
			RefundMoney = true;
		}
		
	}
	/*
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
	*/
</script>
</head>

<body>
	<div class="book_manage account_manage oz" style="margin-top:100px;width:700px;">
	<form action="/meituanRefund/refundTicketAdd.do" method="post" name="updateForm" id="updateForm" onsubmit="return checkSubmit();">
        <ul class="order_num oz">
        	<li>订&nbsp;&nbsp;单&nbsp;&nbsp;号：&nbsp;<input type="text" name="order_id" id="order_id" style="width: 200px;" 
        			onchange="onblurOrderId()" value="${order_id }"/><span id="order_id_info"></span>
        	</li>
            <li id="cpidAndName" style="display: none;">
            	<c:forEach var="list" items="${cpidList}" varStatus="idx" >
            	<input type="radio" id="cpid"name="cpid" value="${list.cp_id }" 
            	<c:if test="${list.cp_id eq cpid }">checked="checked"</c:if> 
            	onchange="onblurOrderId();"/>
            	<input type="hidden" id="channel"name="channel" value="${list.channel }" 
            	<c:if test="${list.cp_id eq cpid }">checked="checked"</c:if> />
							<label for="${index.count }">
							姓名：<input value="${list.user_name }" type="text" id="AddUserName_${idx.count }" 
							style="color:#f60;width:50px;border:0;"/>
			            	车票号：<input value="${list.cp_id }" type="text" id="AddCpId_${idx.count }" 
			            	style="color:#f60;width:150px;border:0;"/>
			            	票价：<input value="${list.change_buy_money }" type="text" id="AddbuyMoney_${idx.count }" 
			            	style="color:#f60;width:50px;border:0;"/>元
			            	<br/>
							</label>
            	</c:forEach>
            </li>
            
            <li>退款类型&nbsp;&nbsp;：&nbsp;
            <input value="${refund_type}" type="hidden" name="refund_type" id="refund_type" style="color:#f60;width:50px;border:0;"/>
            <c:if test="${refund_type eq '22' }">线下退款
			</c:if> 
            <c:if test="${refund_type eq '55' }"><font color="#f60;">改签退款</font>
			</c:if>     
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
            	<!-- 
            	<label style="float:left;"> 退款原因&nbsp;&nbsp;：&nbsp;</label><textarea rows="5" name="our_remark" id="our_remark" style="width: 209px; height:50px;border:1px solid #7F9DB9;"
            	 	onblur="onblurOurRemark()" ></textarea><span id="our_remark_info"></span> -->
            	 	<label style="float:left;"> 退款原因&nbsp;&nbsp;：&nbsp;</label><textarea rows="5" name="our_remark" id="our_remark" style="width: 209px; height:50px;border:1px solid #7F9DB9;"
            	 	 ></textarea>
            </li>
        </ul>
        <p><input type="button" value="提 交" class="btn" id="btnSubmit" onclick="submitForm()"/>
           <input type="button" value="返 回" class="btn" onclick="javascript:location='/meituanRefund/queryRefundTicketPage.do'"/>
           <input type="button" value="改签退票" class="btn" id="alterSubmit" onclick="submitForm()" style="display:none;margin-left:280px;margin-top: -35px;background: orange;"/>
           </p>
    </form>
	</div>
</body>
</html>
