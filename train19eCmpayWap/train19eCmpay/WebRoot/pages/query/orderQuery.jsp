<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta charset="utf-8">
<title>掌上19e-火车票</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<meta name="keyword" content="19e,便民服务,数字便民,移动客户端,手机客户端,话费充值,手机充值,游戏充值,QQ充值,机票预订,水费,电费,煤气费,取暖费,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
<meta name="description" content="提供19e数字便民移动终端下载,随时随地办理便民业务,支持移动客户端话费充值,手机充值,游戏充值,QQ充值,机票预订,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
<link rel="stylesheet" type="text/css" href="/css/base.css">
<link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/print.js"></script>
<style type="text/css">
.qtr_number {width:65%;}
.train_status {padding:0 10px 0 10px;}
html{-webkit-text-size-adjust:100%; -ms-text-size-adjust:100%;}
.qtr_number span em {font-size:20px;color:#0f63b8;font-weight:bold;font-style: normal;}
.train_status span {display:block;line-height:33px;width:100%; -webkit-box-sizing:border-box;text-align:center;}
.qtr_number span{display:block; line-height:33px; -webkit-box-sizing:border-box;margin:2px 0 2px 0;padding-left:8px;}
.qtr_number span .grey{font-size:12px;}
span {font-size:16px;color:black;}
span .grey{font-size:12px;}
.qtrain_list {width:100%; font-size:16px; color:#909090; font-family:"Microsoft yahei"; background:#fff;border:none;}
</style>
<script type="text/javascript">
</script>
</head>


<body>
<div>

	<!-- start -->
	<div class="wrap1">
		<header id="bar">
			<a href="javascript:window.history.back();" class="m19e_ret"></a>
			<h1 id="trainList">我的订单</h1>
			<a href="/pages/book/menuNew.jsp" class="m19e_home"><span></span></a>
		</header>

		<section id="train_main">
		<c:forEach items="${orderList}" var="order" varStatus="idx">
			<table id="train_list" class="qtrain_list">
				<tr>
					<td class="qtr_number">	
						<span>${order.travel_time}&nbsp;&nbsp;&nbsp;&nbsp;${order.from_time} 发车</span>
						<span><em>${order.train_no}</em>${order.from_city}—${order.to_city}</span>
						<span class="grey">${order.passengerList }</span>	
					</td>
					<td class="train_status">
					<c:choose>
						<c:when test="${order.order_status eq '45'}">
							<span class="red"><em class="order_fail">&nbsp;&nbsp;&nbsp;&nbsp;</em>购票失败</span>
							<span>￥<fmt:formatNumber value="${order.ticket_pay_money + order.bx_pay_money}" type="currency" pattern="#0.0"/></span>
							<span class="border_style" onclick="javascript:window.location='/query/queryOrderDetail.jhtml?order_id=${order.order_id}&type=detail'">&nbsp;详&nbsp;情&nbsp;</span>
						</c:when>
						<c:when test="${order.order_status eq '99' }">
							<span class="red"><em class="order_fail">&nbsp;&nbsp;&nbsp;&nbsp;</em>${orderStatusMap[order.order_status]}</span>
							<span>￥<fmt:formatNumber value="${order.ticket_pay_money + order.bx_pay_money}" type="currency" pattern="#0.0"/></span>
							<span class="border_style" onclick="javascript:window.location='/query/queryOrderDetail.jhtml?order_id=${order.order_id}&type=detail'">&nbsp;详&nbsp;情&nbsp;</span>
						</c:when>
						<c:when test="${order.order_status eq '00'}">
							<span class="blue_pay">待支付</span>
							<span>￥<fmt:formatNumber value="${order.ticket_pay_money + order.bx_pay_money}" type="currency" pattern="#0.0"/></span>
							<span class="border_style" onclick="javascript:window.location='/query/queryOrderDetail.jhtml?order_id=${order.order_id}&type=detail'">&nbsp;详&nbsp;情&nbsp;</span>
	                    </c:when>
	                    <c:when test="${order.order_status eq '22'}">
							<span class="blue_pay">正在购票</span>
							<span>￥<fmt:formatNumber value="${order.ticket_pay_money + order.bx_pay_money}" type="currency" pattern="#0.0"/></span>
							<span class="border_style" onclick="javascript:window.location='/query/queryOrderDetail.jhtml?order_id=${order.order_id}&type=detail'">&nbsp;详&nbsp;情&nbsp;</span>
	                    </c:when>
	                    <c:when test="${order.order_status eq '44'}">
							<c:choose>
								<c:when test="${order.refund_status eq '00' || order.refund_status eq '11' || order.refund_status eq '22' || order.refund_status eq '33'}">
									<span class="blue_pay">${rsStatusMap[order.refund_status]}</span>
									<span>￥<fmt:formatNumber value="${order.ticket_pay_money + order.bx_pay_money}" type="currency" pattern="#0.0"/></span>
									<span class="border_style" onclick="javascript:window.location='/query/queryOrderDetail.jhtml?order_id=${order.order_id}&type=detail'">&nbsp;详&nbsp;情&nbsp;</span>
								</c:when>
								<c:when test="${order.refund_status eq '44'}">
									<span class="green">${rsStatusMap[order.refund_status]}</span>
									<span>￥<fmt:formatNumber value="${order.ticket_pay_money + order.bx_pay_money}" type="currency" pattern="#0.0"/></span>
									<span class="border_style" onclick="javascript:window.location='/query/queryOrderDetail.jhtml?order_id=${order.order_id}&type=detail'">&nbsp;详&nbsp;情&nbsp;</span>
								</c:when>
								<c:when test="${order.refund_status eq '55'}">
									<span class="red">${rsStatusMap[order.refund_status]}</span>
									<span>￥<fmt:formatNumber value="${order.ticket_pay_money + order.bx_pay_money}" type="currency" pattern="#0.0"/></span>
									<span class="border_style" onclick="javascript:window.location='/query/queryOrderDetail.jhtml?order_id=${order.order_id}&type=detail'">&nbsp;详&nbsp;情&nbsp;</span>
								</c:when>
								<c:otherwise>
									<span class="green">购票成功</span>
									<span>￥<fmt:formatNumber value="${order.ticket_pay_money + order.bx_pay_money}" type="currency" pattern="#0.0"/></span>
									<span class="border_style" onclick="javascript:window.location='/query/queryOrderRefund.jhtml?order_id=${order.order_id}&type=refund'">&nbsp;退&nbsp;票&nbsp;</span>
								</c:otherwise>
							</c:choose>
	                    </c:when>
	                    
	                    <c:otherwise>
	                    	<span class="green">${orderStatusMap[order.order_status]}</span>
							<span>￥<fmt:formatNumber value="${order.ticket_pay_money + order.bx_pay_money}" type="currency" pattern="#0.0"/></span>
							<span class="border_style" onclick="javascript:window.location='/query/queryOrderDetail.jhtml?order_id=${order.order_id}&type=detail'">&nbsp;详&nbsp;情&nbsp;</span>
	                    </c:otherwise>
	                </c:choose>
					</td>
				</tr>
				<tr><td colspan="5"><hr/></td></tr>
			</table>
		</c:forEach>
		</section>
	</div>
	<!-- end -->

</div>	
</body>
</html>		
