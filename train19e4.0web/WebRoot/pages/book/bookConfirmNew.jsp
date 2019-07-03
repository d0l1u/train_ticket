<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>确认订单页</title>
<link rel="stylesheet" href="/css/style.css" type="text/css" />
<link rel="stylesheet" href="/css/default.css" type="text/css" />
<link rel="stylesheet" href="/css/attach_style.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/artDialog.js"></script>
<script type="text/javascript" src="/js/idCard.js"></script>
</head>

<body>
	<div class="content oz">
		<div class="index_all">
				
		<!--左边内容 start-->
			<jsp:include flush="true" page="/pages/common/menu.jsp">
					<jsp:param name="menuId" value="book" />
				</jsp:include>
		<!--左边内容 end-->
		</div>
    	<!--左边内容 start-->
    	<form name="content" method="post" action="/order/orderCmpay.jhtml">
    	<input type="hidden" id="order_id" value="${orderInfo.order_id}"/>
    	<input type="hidden" id="mid_min" value="${mid_min}"/>
    	<input type="hidden" id="mid_req" value="${mid_req}"/>
    	<input type="hidden" id="order_status" value="${orderInfo.order_status}"/>
    	<input type="hidden" id="bookResult" value="${bookResult}"/>
    	<input type="hidden" id="totalPay4Show" value="${totalPay4Show}"/>
    	<div id="order_con">
			<div class="order_top">
		    	<p id="has_result">订单正在等待付款，请在&nbsp;<b>${mid_min}</b>&nbsp;分钟内完成支付，支付剩余时间：<b id="minute_id"></b><b id="second_id"></b></span></p>
		    	<p id="no_result" style="line-height:26px;color:#f60;">尊敬的用户您好，由于网络问题，暂时获取不到出票结果信息，您可以先支付该订单，或者<a href="#" onclick="gotoGetResult();">点击此处</a>继续获取预定结果信息。</p>
		    </div>
		    
		    <div class="pub_self_take_mes oz mb10_all">
		    	<div class="pub_ord_tit">  <h4 class="fl">订单信息</h4></div>
		    <div class="order_in">
		    	<div class="order_xinxi">
		        	<span>${orderInfo.travel_time}</span>
		        	<span>${orderInfo.train_no}次</span>
		        	<strong>${orderInfo.from_city}</strong>&nbsp;&nbsp;&nbsp;&nbsp;站
		            （<strong>${orderInfo.from_time}&nbsp;</strong>开）&nbsp;&nbsp;&nbsp;&nbsp;
		           	<strong>${orderInfo.to_city}</strong>&nbsp;&nbsp;站
		            （${orderInfo.to_time}&nbsp;到）
		        </div>
		        <table>
		    	<tr class="order_tit">
				<th width="8%">序号</th>    
				<th width="10%">乘车人姓名</th>    
				<th width="10%">乘客类型</th>    
				<th width="10%">证件类型</th>    
				<th width="20%">证件号码</th>    
				<th width="8%">坐席类型</th>    
				<th width="6%">车厢</th>    
				<th width="6%">座位号</th>    
				<th width="10%">票价（元）</th>    
				<th width="10%">保险（元）</th>    
		    </tr>
		    <tbody>
		    	<c:forEach var="detailInfo" items="${detailList}" varStatus="idx">
		    		<tr>
		    			<td>${idx.index}</td>
		    			<td>${detailInfo.user_name}</td>
		    			<td>${ticketTypeMap[detailInfo.ticket_type]}</td>
		    			<td>${idsTypeMap[detailInfo.ids_type]}</td>
		    			<td>${detailInfo.user_ids}</td>
		    			<td>
		    				${seatTypeMap[detailInfo.seat_type]}
                        </td>
		    			<td>${detailInfo.train_box}</td>
		    			<td>${detailInfo.seat_no}</td>
		    			<td>${detailInfo.cp_buy_money}</td>
		    			<td>
		    				<c:choose>
	                        	<c:when test="${!empty detailInfo.bx_name}">
	                        		${detailInfo.bx_pay_money}
	                        	</c:when>
	                        	<c:otherwise>
	                        		0
	                        	</c:otherwise>
                        	</c:choose>
                        </td>
		    		</tr>
		    	</c:forEach>
		    	
		    </tbody>
		    </table>
		 <!--   <span class="yingfu" style="line-height:44px; margin-right:58px;">应付金额：<b>￥${totalPay4Show}</b>
    		<div class="gotoPay">
    			<input onclick="gotoPay()" type="button" class="btn" 
                   		value="立即支付" />
    		</div>  
		    </span>-->
		    <br/><br/><br/>
		    </div>
		  </div>
		    <div class="pub_self_take_mes oz mb10_all">
       			<div class="pub_ord_tit">
	        	    <h4 class="fl">支付信息</h4>
                    <p class="tit_tip">（火车票票源紧张，支付后不保证100%出票）</p>
                </div>
                <div class="new_pub_con">
                	<table class="pub_table">
                    	<tr>
                        	<td style="width:110px;"></td>
                        	<td>票 价：<span style="font-weight: bold;color: red;"><fmt:formatNumber value="${orderInfo.ticket_pay_money}" type="currency" pattern="#0.00元"/></span></td>
                        	<td>保 险：<span style="font-weight: bold;color: red;"><fmt:formatNumber value="${orderInfo.bx_pay_money}" type="currency" pattern="#0.00元"/></span></td>
                        	<c:if test="${engineeringCost eq '5'}">
                        		<td>SVIP服务费：<span style="font-weight: bold;color: red;"><fmt:formatNumber value="${engineeringCost}" type="currency" pattern="#0.00元"/></span></td>
                        	</c:if>
                        	<td>总 计：<span style="font-weight: bold;font-size: 20px;color: red;"><fmt:formatNumber value="${totalPay4Show}" type="currency" pattern="#0.00元"/></span></td>
                        </tr>
                    </table>
                <p style="padding-left:80px;padding-bottom:20px;">
					<iframe src="${pay_url}" frameborder="0" scrolling="no"  width="550px;" height="140px" style="background: #F0F7FF;"></iframe>
	  				<br /><span style="margin-left:80px;">*&nbsp;&nbsp;请确认以上信息，支付后将无法修改</span>
				</p>
				</div>
        	</div>
	</div>
<script type="text/javascript">
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
    if((window.event.altKey)&&(window.event.keyCode==115))   
    {   
		window.showModelessDialog("about:blank","","dialogWidth:1px;dialogheight:1px");   
		return   false;   
    }   
      
    //屏蔽Ctrl+A   
    if((event.ctrlKey)&&(event.keyCode==65))   
    {   
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

	//确认支付
	function gotoPay(){
		var count = $(".ticketInfo:visible").length;
		var bankAbbr = $("#bankAbbr").val();
		var order_id = $("#order_id").val();
		$("form:first").attr("action", "/order/orderCmpay.jhtml?order_id="+order_id+"&bankAbbr="+bankAbbr+"&productNum"+count);
		//消息框	
		var dialog = art.dialog({
			lock: true,
			fixed: true,
			left: '50%',
			top: '250px',
		    icon: "/images/loading.gif",
		    content: '正在前往支付页面，请稍候！'
		});
		$(".aui_titleBar").hide();
		$("form:first").submit();
	}
	//显示取票说明
	function showQpDesc(){
		//消息框
		var dialog = art.dialog({
			lock: true,
			fixed: true,
			left: '50%',
			top: '50%',
		    title: '取票说明',
		    okVal: '确认',
		    content: '<p style="width:250px;height:40px;line-height:20px;">1、凭购票时的有效证件和电子订单号，发车前可在全国任意火车站或代售点取票。</p>'
		    		+'<p style="width:250px;height:40px;line-height:20px;margin-top:15px;">2、代售点收取代售费5元/张，另外车站售票窗口取异地票，火车站将收取代售费5元/张。</p>',
		    ok: function(){}
		});
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
        				var url="/order/orderComfirm.jhtml?order_id=" + $("#order_id").val() + "&totalPay4Show=" + $("#totalPay4Show").val()
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
</body>
</html>
