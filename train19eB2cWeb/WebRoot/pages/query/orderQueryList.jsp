<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>19trip旅行</title>
<link rel="stylesheet" href="/css/reset-min.css" type="text/css" />
<link rel="stylesheet" href="/css/style.css" type="text/css"/>
<link rel="stylesheet" href="/css/sreachbar.css" type="text/css"/>
<link rel="stylesheet" href="/css/travel.css" type="text/css"/>
<script type="text/javascript" src="/js/artDialog.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">
//重新支付
function rePayOrder(order_id){
	$.ajax({
		url:"/order/orderRepay.jhtml?order_id="+order_id,
		type: "POST",
		cache: false,
		success: function(res){
			if(res=='success'){
				window.location='/order/orderComfirm.jhtml?order_id='+order_id;
			}else{
				var dialog = art.dialog({
					lock: true,
					fixed: true,
					left: '50%',
					top: '40%',
				    title: '提示',
				    okVal: '确认',
				    icon: "/images/warning.png",
				    content: '对不起，您所选的车次已无票，请您重新购票！',
				    ok: function(){}
				});
				return false;
			}
		}
	});
}
//申请退款
function refund(order_id){
	window.location='/query/queryOrderRefund.jhtml?order_id='+order_id+'&type=refund';
}
//立即支付
function toPay(order_id){
	window.location='/order/orderComfirm.jhtml?order_id='+order_id;
}

function submintForm(){
	$("#trainForm").submit();
}
</script>
</head>

<body>
<!--以下是头部logo部分start -->
<jsp:include flush="true" page="/pages/common/headerNav.jsp">
	<jsp:param name="menuId" value="lx" />
</jsp:include>
<!--以下是头部logo部分end -->


<!--以下是我的旅行正文内容travel_con部分start -->
<div class="travel_con">
	<!--左边内容 start-->
			<jsp:include flush="true" page="/pages/common/menuLeft.jsp">
				<jsp:param name="menuId" value="hcpOrder" />
			</jsp:include>
	<!--左边内容 end-->
    
    
    <!--右边内容 start-->
	<div class="right_con">
        <form id="trainForm" action="/query/queryOrderList.jhtml?type=hcp" method="post">
		<!--以下是我的旅行正文内容下面订单部分-->
   		<ul class="trainOrder">
   			<li>火车票订单</li>
   		</ul>
   		<c:if test="${count != 0}">
		<div class="trainO_con">
			<div class="ordersearch">
	        	<p>按乘车人姓名查询：&nbsp;&nbsp;
	            <input type="text" id="user_name" name="user_name" value="${user_name }" />
	            </p>
	            <button type="button" class="btn1" onclick="submintForm();">查&nbsp;询</button>
	        </div>
	      	<ul class="condi_table condi_ul">
	      		<li style="width:300px">车次信息</li >
	      		<li style="width:250px">出发时间</li >
		      	<li style="width:90px">坐席</li >
		      	<li style="width:90px">支付金额</li >
		      	<li style="width:90px"">订单状态</li >
		      	<li style="width:165px">操作</li>
	      	</ul>
	      	
	      	<c:forEach items="${orderList}" var="order" varStatus="idx">
	  		<div class="condi_xinxi">
      			<table class="condi_table trainO_tb" cellpadding="0" cellspacing="0">
      			<tr>
			      	<th style="width:300px"></th>
			      	<th style="width:250px"></th>
			      	<th style="width:90px"></th>
			      	<th style="width:90px"></th>
			      	<th style="width:90px"></th>
			      	<th style="width:165px"></th>
   	 			</tr>
    
			    <tr class="title">
			    	<td>订单号：<a href="/query/queryOrderDetail.jhtml?order_id=${order.order_id}&type=detail">${order.order_id}  [ 订单详情 ]</a></td>
			    	<td>预定时间：${order.create_time}</td>
			    	<td></td>
			    	<td></td>
			    	<td></td>
			    	<td>
			    	<!-- 
			    	<c:if test="${(order.order_status eq '00' or order.order_status eq '99') and order.is_repay eq '1' }">
			    		支付剩余时间：<strong>19:59</strong>
			    	</c:if>
			    	 -->
			    	</td>
				</tr>   
			    <tr class="content">
			    	<td>${order.from_city}→${order.to_city}&nbsp;&nbsp;${order.train_no}次</td>
			    	<td>${order.travel_time }</td>
				    <td>${seatType[order.seat_type]}</td>
				    <td>￥${order.pay_money }</td>
				    <td>
				    	<c:choose>
				    		<c:when test="${(order.order_status eq '44' || order.order_status eq '45') && order.refund_status != null}">
				    			${rsStatusMap[order.refund_status]}
				    		</c:when>
				    		<c:otherwise>
				    			${orderStatusMap[order.order_status]}
				    		</c:otherwise>
				    	</c:choose>
					</td>
				    <td style="padding-left:50px;">
				    	<c:if test="${fn:contains('44', order.order_status )}"><!-- 12,22,33,44 -->
                       		<c:choose>
                       			<c:when test="${order.order_status eq '44' and order.out_ticket_type eq '22' }">
                       			</c:when>
                       			<c:when test="${order.can_refund != null and order.can_refund ne '1'}">
                       			</c:when>
                       			<c:when test="${order.is_deadline eq '1' and order.deadline_ignore ne '1'}">
                       			</c:when>
                       			<c:otherwise>
                       			<input type="button" onclick="refund('${order.order_id}');" value="申请退款" class="btn3"/>
                       			</c:otherwise>
                       		</c:choose>
                        </c:if>
                        <c:if test="${(order.order_status eq '33') and order.deffer_time eq '0'}">
                        	<input type="button" onclick="toPay('${order.order_id}');" value="立即支付" class="btn3"/>
                        </c:if>
				    </td>
			    </tr>
			    </table>
			</div>
			</c:forEach>
			
		</div>
    	</c:if>
    	
    	<c:if test="${count == 0}">
	    <div class="trainNOrder">
	    	<span></span>
	    	<p>您目前没有火车票订单哦~</p>
	    </div>
		</c:if>
		</form>
	</div>
  	<!--右边内容 end-->
</div>
<!--以下是我的旅行正文内容travel_con部分end -->

<!-- footer start -->
<%@ include file="/pages/common/footer.jsp"%>
<!-- footer end -->
</body>
</html>
