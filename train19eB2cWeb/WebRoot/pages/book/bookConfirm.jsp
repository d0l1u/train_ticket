<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>19trip旅行</title>
<link rel="stylesheet" href="/css/default.css" type="text/css" />
<link rel="stylesheet" href="/css/reset-min.css" type="text/css" />
<link rel="stylesheet" href="/css/style.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/artDialog.js"></script>
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
	//立即支付
	function gotoPayBx(order_id){
		window.location = "/order/gotoPay.jhtml?order_id="+order_id+'&buyBx=yes';
	}

	$(document).ready(function(){
		var mid_req = $("#mid_req").val();
		if("false" == mid_req){
			$("#no_result").show();
			$("#has_result").hide();
		}else{
			$("#no_result").hide();
			$("#has_result").show();
		}
		//屏蔽   Alt+   方向键   ←   屏蔽   Alt+   方向键   →   
	    if((window.event.altKey)&&((window.event.keyCode==37)||(window.event.keyCode==39))){       
	          event.returnValue=false;   
	    }   
	    //屏蔽退格删除键,屏蔽   F5   刷新键,Ctrl   +   R   
	    if((event.keyCode==116)||(event.ctrlKey   &&   event.keyCode==82)){     
	          event.keyCode=0;   
	          event.returnValue=false;   
	    }     
	    //屏蔽   Ctrl+n   
	    if((event.ctrlKey)&&(event.keyCode==78)){       
	       event.returnValue=false;   
	    }   
	    //屏蔽   shift+F10   
	    if((event.shiftKey)&&(event.keyCode==121)){     
	          event.returnValue=false;   
	    }   
	     //屏蔽Alt+F4   
	    if((window.event.altKey)&&(window.event.keyCode==115)){   
			window.showModelessDialog("about:blank","","dialogWidth:1px;dialogheight:1px");   
			return false;   
	    }   
	    //屏蔽Ctrl+A   
	    if((event.ctrlKey)&&(event.keyCode==65)){   
			return false;   
	    }   
	    var mid_min = $("#mid_min").val();
		var order_status = $("#order_status").val();
		if(order_status=="45"){
			var false_dialog = art.dialog({
				lock: true,
				fixed: true,
				left: '50%',
				top: '40%',
			    icon: "/images/loading.gif",
			    content: '该订单出票失败， <b style="color:#f90; font-size:24px; font-weight:600;" id="false_count"></b> 秒将自动跳转到我的订单页面！'
			});
			falseCounter(3);
		}
		
		var result = $("#bookResult").val();
		if(result=="true"){
			minuteCounter(mid_min);
			secondCounter(60);
		}
	});
	function gotoGetResult(){
		//消息框	
		var ajax_dialog = art.dialog({
			lock: true,
			fixed: true,
			left: '50%',
			top: '40%',
		    icon: "/images/loading.gif",
		    content: '系统正在获取您预定的车票信息，请再等候 <b style="color:#f90; font-size:24px; font-weight:600;" id="time_count"></b> 秒！'
		});
		resendCounter(29);
	}

	function minuteCounter(defSec) {
        $("#minute_id").html((defSec--) +"分");
        if(defSec < 0){
        	$("#minute_id").html("");
        }else{
            window.setTimeout("minuteCounter("+defSec+")",60*1000);
        }
     }

	function secondCounter(defSec) {
        $("#second_id").html(defSec-- +"秒");
        if(defSec <= 0 && $("#minute_id").html()==""){
            // alert("时间到");
            var url = "/order/orderCancle.jhtml?order_id=" + $("#order_id").val();
        	window.location = url;
        }else if(defSec <= 0 && !$("#minute_id").html()==""){
        	window.setTimeout("secondCounter(60)",1000);
        }else{
        	window.setTimeout("secondCounter("+defSec+")",1000);
        }
     }

	function resendCounter(defSec) {
        $("#time_count").html(defSec--);
        if(defSec%5 == 0){
        	$.ajax({
				url:"/order/queryOrderResult.jhtml?order_id=" + $("#order_id").val(),
				type: "POST",
				cache: true,
				async: false,
				success: function(res){
        			if(res=='SUCCESS'){
        				var url="/order/orderComfirm.jhtml?order_id=" + $("#order_id").val();
                		window.location = encodeURI(url);
            		}else{
            			window.setTimeout("resendCounter("+defSec+")",1000);
                	}
				}
			});
        }else if(defSec <= 0){
    		var url = "/order/orderComfirm.jhtml?order_id=" + $("#order_id").val();
        	window.location = url;
        }else {
            window.setTimeout("resendCounter("+defSec+")",1000);
        }
     }
	function falseCounter(defSec) {
        $("#false_count").html(defSec--);
        if(defSec <= 0){
            var url = "/query/queryOrderList.jhtml";
            window.location = url;
        }else{
            window.setTimeout("falseCounter("+defSec+")",1000);
        }
     }
</script>
</head>

<body>
<!--以下是头部logo部分start -->
<%@ include file="/pages/common/header.jsp"%>
<!--以下是头部logo部分end -->

<!--以下是头部步骤head_step部分 -->
<div class="head_step">
	<ul>
		<li class="on">填写订单</li>
		<li class="on">在线支付</li>
		<li>订单完成</li>
	</ul>
</div>

<form name="content" method="post" action="/order/gotoPay.jhtml">
	<input type="hidden" id="order_id" value="${orderInfo.order_id}"/>
   	<input type="hidden" id="mid_min" value="${mid_min}"/>
   	<input type="hidden" id="mid_req" value="${mid_req}"/>
   	<input type="hidden" id="order_status" value="${orderInfo.order_status}"/>
   	<input type="hidden" id="bookResult" value="${bookResult}"/>
   	<input type="hidden" id="totalPay4Show" value="${totalPay4Show}"/>
<div id="order_con">
	<div class="order_top">
    	<!-- <p>订单正在等待付款，请在&nbsp;<b>20</b>&nbsp;分钟内完成支付，支付剩余时间：<span id="ltime"></span></p> -->
    	<p id="has_result">订单正在等待付款，请在&nbsp;<b>${mid_min}</b>&nbsp;分钟内完成支付，支付剩余时间：<b id="minute_id"></b><b id="second_id"></b></p>
		<p id="no_result" style="line-height:26px;color:#f60;">尊敬的用户您好，由于网络问题，暂时获取不到出票结果信息，您可以先支付该订单，或者<a href="javascript:void(0);" onclick="gotoGetResult();">点击此处</a>继续获取预定结果信息。</p>
    </div>
    
    <div class="order_in">
    	<div class="order_intit">订单信息</div>
    	<div class="order_xinxi">
        	<span>${orderInfo.travel_time}（${week }）</span>
        	<span>${orderInfo.train_no}次</span>
        	<strong>${orderInfo.from_city}</strong>&nbsp;&nbsp;&nbsp;&nbsp;站
           	 	（<strong>${orderInfo.from_time}</strong>开）&nbsp;&nbsp;&nbsp;&nbsp;
           	<strong>${orderInfo.to_city}</strong>&nbsp;&nbsp;站
            	（${orderInfo.to_time}到）
        </div>
        <table>
    	<tr class="order_tit">
		<th width="8%">序号</th>    
		<th width="10%">乘车人姓名</th>    
		<th width="10%">乘客类型</th>    
		<th width="8%">证件类型</th>    
		<th width="20%">证件号码</th>    
		<th width="8%">坐席类型</th>    
		<th width="8%">车厢</th>    
		<th width="8%">座位号</th>    
		<th width="10%">票价（元）</th>    
		<th width="10%">保险（元）</th>    
    </tr>
    <tbody>
    <c:forEach var="detailInfo" items="${detailList}" varStatus="idx">
    	<tr>
        	<td>${idx.index+1}</td>
        	<td>${detailInfo.user_name}</td>
        	<td>${ticketTypeMap[detailInfo.ticket_type]}</td>
        	<td>${idsTypeMap[detailInfo.ids_type]}</td>
        	<td>${detailInfo.user_ids}</td>
        	<td>${seatTypeMap[orderInfo.seat_type]}</td>
        	<td>${detailInfo.train_box}</td>
        	<td>${detailInfo.seat_no}</td>
        	<td>￥<fmt:formatNumber value="${detailInfo.cp_pay_money}" type="currency" pattern="#0.0"/></td>
        	<td>￥<fmt:formatNumber value="${detailInfo.bx_pay_money}" type="currency" pattern="#0.0"/></td>
        </tr>
    </c:forEach>
    </tbody>
    </table>
    <span class="yingfu">应付金额：<b>￥<fmt:formatNumber value="${totalPay4Show}" type="currency" pattern="#0.0"/></b>
    </span>
    </div>
    <div class="btn1" style="margin:10px auto; float:left;" onclick="gotoPayBx('${orderInfo.order_id}');">银联支付</div>
    <div class="btn1" style="margin:10px auto; float:left;" onclick="gotoPayZfbBx('${orderInfo.order_id}');">支付宝支付</div>
</div>
</form>

<!-- footer start -->
<%@ include file="/pages/common/footer.jsp"%>
<!-- footer end -->

</body>
</html>
