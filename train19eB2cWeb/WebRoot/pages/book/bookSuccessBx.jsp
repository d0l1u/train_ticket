<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/tld/trainUtil.tld" prefix="tn"%> 
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>19trip旅行</title>
<link rel="stylesheet" href="/css/reset-min.css" type="text/css" />
<link rel="stylesheet" href="/css/style.css" type="text/css"/>
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
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
		<li class="on">订单完成</li>
	</ul>
</div>

<div id="order_con">
    <div class="order_in">
    	<div class="order_intit">订单信息</div>
        <div class="successPay">
        	<span>交易已成功！</span>
			您的订单号：<b>${orderInfo.order_id }</b>
        </div>
    	<div class="order_xinxi">
        	<span>${orderInfo.travel_time }（${week }）</span>
        	<span>${orderInfo.train_no }次</span>
        	<strong>${orderInfo.from_city }</strong>&nbsp;&nbsp;&nbsp;&nbsp;站
            	（<strong>${orderInfo.from_time }</strong>开）&nbsp;&nbsp;&nbsp;&nbsp;
           	<strong>${orderInfo.to_city }</strong>&nbsp;&nbsp;站
           		（${orderInfo.to_time }到）
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
    <span class="yingfu">应付金额：<b>￥<fmt:formatNumber value="${orderInfo.pay_money }" type="currency" pattern="#0.0"/></b></span>
    </div>
    <div class="sPay_btn">
    <span class="btn1" style="display:inline-block; margin-left:400px; margin-right:20px;">继续购票</span>
    <span class="btn1"  style="display:inline-block; margin-left:0px;">查询订单详情</span>
    </div>
</div>

<!-- footer start  -->
<%@ include file="/pages/common/footer.jsp"%> 
<!-- footer end --> 
</body>
</html>
