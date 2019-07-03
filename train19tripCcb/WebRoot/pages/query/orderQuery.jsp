<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预订查询页</title>
<link rel="stylesheet" href="/css/style.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/print.js"></script>
</head>

<body>
	<div class="content oz">
		<!--导航条 start-->
	    <div class="main_nav">
        	<ul class="oz">
            	<jsp:include flush="true" page="/pages/common/menu.jsp">
					<jsp:param name="menuId" value="query" />
				</jsp:include>
            </ul>
            <div class="slogan"></div>
        </div>
        <!--导航条 end-->
    	<!--左边内容 start-->
    	<div class="left_con oz">
            <form action="/query/queryOrderList.jhtml" method="post">
            <!--订单号查询模块 start-->
            <dl class="order_num oz">
	                <dt style="padding-left:30px;">订单号</dt>
	                <dd class="dd_text"><input type="text" name="order_id" value="${order_id}" 
	                	onkeyup="value=value.replace(/(^\s*)|(\s*$)/g,'');" /></dd>
	                
			</dl>
			<dl class="order_num oz" style="padding-bottom:10px;">
	               	<dt>取票订单号</dt>
	                <dd class="dd_text"><input type="text" name="out_ticket_billno" value="${out_ticket_billno}" 
	                	onkeyup="value=value.replace(/(^\s*)|(\s*$)/g,'');" /></dd>
	                <dd class="dd_btn"><input type="submit" value="" /></dd>
            </dl>	
            	
            <!--订单号查询模块 end-->
         	<div class="debook oz">
            	<h2>
                	<span class="nav_left_bg"></span>
                	<span class="nav_mid_bg">订单详情</span>
                	<span class="nav_right_bg"></span>
                </h2>
                <dl class="oz">
                	<dt></dt>
                	<dd></dd>
                </dl>
                <table>
                    <tr class="tit">
                		<td class="pad1">序 号</td>
                		<td width="90">出发-到达/方式</td>
                		
                		<td>订单号/支付金额</td>
                	
                		<td>创建时间/支付时间</td>
                		<td>出票时间/订单状态</td>
                		
                		<td>操 作</td>
                	</tr>
                	<c:forEach items="${orderList}" var="order" varStatus="idx">
              			
	                    <tr class="con">
	                    	<td>${idx.index+1}</td>
	                    	<td>${order.from_city}-${order.to_city}</td>
	                    	<td>${order.order_id}</td>
	                    	
	                    	<td>${order.create_time }</td>
	                    	
	                        <td>${order.out_ticket_time}</td>
	                        <td>
	                        <input type="button" class="btn" value="明细" onclick="javascript:window.location='/query/queryOrderDetail.jhtml?order_id=${order.order_id}&type=detail'" />
	                        <input type="button" class="btn" value="投诉" onclick="javascript:window.location='/complain/complainIndex.jhtml?question=${order.order_id}&ques_Id=0'"/>
	                        <c:if test="${fn:contains('44', order.order_status )}"><!-- 12,22,33,44 -->
	                        		<c:choose>
	                        			<c:when test="${order.order_status eq '44' and order.out_ticket_type eq '22' }">
	                        			</c:when>
	                        			<c:when test="${order.can_refund != null and order.can_refund ne '1'}">
	                        			</c:when>
	                        			<c:when test="${order.is_deadline eq '1' and order.deadline_ignore ne '1'}">
	                        			</c:when>
	                        			<c:otherwise>
	                        				<input type="button" class="btn" value="退款"  onclick="javascript:window.location='/query/queryOrderRefund.jhtml?order_id=${order.order_id}&type=refund'" />
	                        			</c:otherwise>
	                        		</c:choose>
	                        </c:if>
	                        </td>
	                    </tr>
	                    <tr class="order_num_state">
	                    	<td></td>
	                    	<td><c:if test="${order.out_ticket_type eq '11'}">自 提</c:if>
	                			<c:if test="${order.out_ticket_type eq '22'}">配 送</c:if></td>
	                		<td nowrap="nowrap">
	                			<font style="font-weight:bold;color:red;"><fmt:formatNumber value="${order.ticket_pay_money + order.bx_pay_money}" type="currency" pattern="#0.00"/></font>
	                		</td>
	                		<td>${order.pay_time}</td>
	                		
	                		<td>${orderStatusMap[order.order_status]}<c:if test="${!empty order.refund_status}">/${rsStatusMap[order.refund_status]}</c:if></td>
	                		<td></td>
	                	</tr>
	                   
                    </c:forEach>
                </table>
                <c:if test="${isShowList == 1}">
                	<jsp:include page="/pages/common/paging.jsp" />
                </c:if>
            </div>
            </form>
        
        	<!--业务所有标注 start-->
	        <div class="business-provider">
	        	
	        </div>
	        <!--业务所有标注 end-->
        </div>
        <!--左边内容 end-->
    </div>
</body>
</html>
