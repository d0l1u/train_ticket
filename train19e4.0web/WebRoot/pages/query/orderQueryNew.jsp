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
<link rel="stylesheet" href="/css/default.css" type="text/css" />
<script type="text/javascript" src="/js/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/js/artDialog.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/print.js"></script>
</head>

<body>
	<div class="content oz">
		<div class="index_all">
		<!--左边内容 start-->
			<jsp:include flush="true" page="/pages/common/menu.jsp">
					<jsp:param name="menuId" value="query" />
				</jsp:include>
		<!--左边内容 end-->
    	<!--右边内容 start-->
    	<div class="infoinput-right oz">
    	     <div class="query_type oz">
            	<ul class="query_type_ul">
            		<c:if test="${selectType eq '0'}">
	                	<li class="current current1"><a href="/query/queryOrderList.jhtml?selectType=0&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">全部订单</a></li>
	                	<li class="current1"><a href="/query/queryOrderList.jhtml?selectType=5&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">待支付(${noPayNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=1&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票中(${waitingNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=2&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票成功(${successNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=3&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票失败(${failNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=4&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">退款结果(${refundNum})</a></li>
                    </c:if>
                    <c:if test="${selectType eq '1'}">
	                	<li class="current1"><a href="/query/queryOrderList.jhtml?selectType=0&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">全部订单</a></li>
	                	<li class="current1"><a href="/query/queryOrderList.jhtml?selectType=5&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">待支付(${noPayNum})</a></li>
	                    <li class="current current1"><a href="/query/queryOrderList.jhtml?selectType=1&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票中(${waitingNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=2&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票成功(${successNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=3&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票失败(${failNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=4&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">退款结果(${refundNum})</a></li>
                    </c:if>
                    <c:if test="${selectType eq '2'}">
	                	<li class="current1"><a href="/query/queryOrderList.jhtml?selectType=0&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">全部订单</a></li>
	                	<li class="current1"><a href="/query/queryOrderList.jhtml?selectType=5&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">待支付(${noPayNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=1&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票中(${waitingNum})</a></li>
	                    <li class="current current1"><a href="/query/queryOrderList.jhtml?selectType=2&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票成功(${successNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=3&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票失败(${failNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=4&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">退款结果(${refundNum})</a></li>
                    </c:if>
                    <c:if test="${selectType eq '3'}">
	                	<li class="current1"><a href="/query/queryOrderList.jhtml?selectType=0&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">全部订单</a></li>
	                	<li class="current1"><a href="/query/queryOrderList.jhtml?selectType=5&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">待支付(${noPayNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=1&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票中(${waitingNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=2&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票成功(${successNum})</a></li>
	                    <li class="current current1"><a href="/query/queryOrderList.jhtml?selectType=3&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票失败(${failNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=4&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">退款结果(${refundNum})</a></li>
                    </c:if>
                    <c:if test="${selectType eq '4'}">
	                	<li class="current1"><a href="/query/queryOrderList.jhtml?selectType=0&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">全部订单</a></li>
	                	<li class="current1"><a href="/query/queryOrderList.jhtml?selectType=5&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">待支付(${noPayNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=1&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票中(${waitingNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=2&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票成功(${successNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=3&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票失败(${failNum})</a></li>
	                    <li class="current current1"><a href="/query/queryOrderList.jhtml?selectType=4&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">退款结果(${refundNum})</a></li>
                    </c:if>
                    <c:if test="${selectType eq '5'}">
	                	<li class="current1"><a href="/query/queryOrderList.jhtml?selectType=0&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">全部订单</a></li>
	                    <li class="current  current1"><a href="/query/queryOrderList.jhtml?selectType=5&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">待支付(${noPayNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=1&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票中(${waitingNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=2&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票成功(${successNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=3&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票失败(${failNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=4&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">退款结果(${refundNum})</a></li>
                    </c:if>
                </ul>
         	</div>
            <form action="/query/queryOrderList.jhtml" method="post">
            <p style="margin-top:20px;">
	            <span >	
	            <select style="width:110px;" name="oneMonthOrder" id="oneMonthOrder" >
	            	<option value="0" <c:if test="${oneMonthOrder==0}">selected</c:if>>一个月内的订单&nbsp;&nbsp;&nbsp;&nbsp;</option>
	            	<option value="1" <c:if test="${oneMonthOrder==1}"> selected</c:if>>一个月前的订单&nbsp;&nbsp;&nbsp;&nbsp;</option>
	            </select>
	            </span>
				 <span style="margin-left:5px;">按订票日期查：<input style="width:105px;" class="text text2" type="text" id="create_time" name="create_time" 
			                    		value="${create_time}" readonly="readonly" 
								  		onfocus="WdatePicker({doubleCalendar: false ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
				</span>
				 <span style="margin-left:5px;">按订单号查：<input style="width:105px;" type="text" name="order_id" value="${order_id}" 
		                	onkeyup="value=value.replace(/(^\s*)|(\s*$)/g,'');" />
				</span>
				<span style="margin-left:5px;">按取票单号查：<input style="width:105px;" type="text" name="out_ticket_billno" value="${out_ticket_billno}" 
		                	onkeyup="value=value.replace(/(^\s*)|(\s*$)/g,'');" />
				</span>
				 <span style="margin-left:0px;">
				 <input  type="submit" class="btn search_order_btn" value="查询" />
				</span>  
           </p>
            <!--订单号查询模块 end-->
            <!-- 页面显示模块，0全部订单显示 -->
            <c:if test="${selectType eq '0'}">
	         	<div class="myorder oz">
	         	<input  type="hidden" name="selectType" value="0"/>
	                <table >
	                	<tr style="background: #EAEAEA;">
	                	<th>序号</th>
	                	<th>订单号</th>
	                	<th>出发-到达</th>
	                	<th>支付金额</th>
	                	<th>支付时间</th>
	                	<th>出票时间</th>
	                	<th>退款时间</th>
	                	<!-- 
						<th>票数</th>
						 -->
						<th>订单类型</th>
						<th>订单状态</th>
						<th>退款状态</th>
						<th>操作</th>                	
	                	</tr>
	                	<c:forEach items="${orderList}" var="order" varStatus="idx">
		                    <tr >
		                    	<td>${idx.index+1}</td>
		                    	<td>${order.order_id}</td>
		                    	<td>${order.from_city}-${order.to_city}</td>
		                    	<td>
		                    	<c:if  test="${order.ticket_ps_type eq '11'}">
		                    	${order.ticket_pay_money + order.bx_pay_money + order.server_pay_money+25}
		                    	</c:if>
		                    	<c:if  test="${order.ticket_ps_type ne '11'}">
		                    	${order.ticket_pay_money + order.bx_pay_money + order.server_pay_money}
		                    	</c:if>
		                    	元</td>
		                    	<td><p style="line-height:22px;margin: 0;">${order.pay_time_start}</p><p style="line-height:22px;margin: 0;"> ${order.pay_time_end}</p></td>
		                        <td><p style="line-height:22px;margin: 0;">${order.out_ticket_time_start}</p><p style="line-height:22px;margin: 0;">${order.out_ticket_time_end}</p></td>
		                        <td><p style="line-height:22px;margin: 0;">${order.refund_time_start}</p><p style="line-height:22px;margin: 0;">${order.refund_time_end}</p></td>
		                        <!-- 
		                        <td>${order.cpCount}</td>
		                         -->
		                        <td>
		                        	<c:choose>
		                        		<c:when test="${order.server_pay_money eq '5.000'}">
		                        			SVIP
		                        		</c:when>
		                        		<c:otherwise>
		                        			<c:choose>
		                        				<c:when test="${order.bx_pay_money eq '0.000'}">
		                        					普通
		                        				</c:when>
		                        				<c:otherwise>
		                        					VIP
		                        				</c:otherwise>
		                        			</c:choose>
		                        		</c:otherwise>
		                        	</c:choose>
		                        </td>
		                        <td>${orderStatusMap[order.order_status]}</td>
		                        <td>
		                        <c:if test="${!empty order.refund_status}">
		                        <c:choose>
		                        	<c:when test="${order.refund_type eq '2' && (order.refund_status eq '00' || order.refund_status eq '11' || order.refund_status eq '22' || order.refund_status eq '33')}">
		                        		差额退款中
		                        	</c:when>
		                        	<c:when test="${order.refund_type eq '2' && order.refund_status eq '44'}">
		                        		已差额退款
		                        	</c:when>
		                        	<c:otherwise>
		                        		${rsStatusMap[order.refund_status]}
		                        	</c:otherwise>
		                        </c:choose>
		                        </c:if>
		                        <!-- 
			                        <c:if test="${!empty order.refund_status}">
			                			${rsStatusMap[order.refund_status]}
			                        </c:if>
			                     -->
		                        </td>
		                        <td>
		                        <p style="line-height:22px;margin: 0;"><a style="text-decoration:none;" href="/query/queryOrderDetail.jhtml?order_id=${order.order_id}&type=detail"><font style="color:#0d77c1;">明细</font></a>
		                       <c:if test="${order.order_status eq '00'}">
		                        <a style="text-decoration:none;" href="#" onclick="javascript:deleteOrder('${order.order_id}','${selectType }');"><font style="color:#0d77c1;">删除</font></a>
			                   </c:if>
			                     <c:if test="${order.order_status eq '44'}">
			                     	<a style="text-decoration:none;" href="javascript:_printIT('${order.order_id}');" ><font style="color:#0d77c1;">打印</font></a>
	                			 </c:if>
	                			 </p>
			                        <c:if test="${fn:contains('44', order.order_status )}"><!-- 12,22,33,44 -->
		                        		<c:choose>
		                        			<c:when test="${order.order_status eq '44' and order.out_ticket_type eq '22' }">
		                        			</c:when>
		                        			<c:when test="${order.can_refund != null and order.can_refund ne '1'}">
		                        			</c:when>
		                        			<c:when test="${order.is_before eq '1' and order.deadline_ignore ne '1'}">
	                        				</c:when>
		                        			<c:when test="${order.is_deadline eq '1' and order.deadline_ignore ne '1'}">
		                        			</c:when>
		                        			<c:when test="${order.ticket_ps_type eq '11'}">
		                        			</c:when>
		                        			<c:otherwise>
		                        			<p style="line-height:22px;margin: 0;"><a style="text-decoration:none;" href="/query/queryOrderRefund.jhtml?order_id=${order.order_id}&type=refund"><font style="color:#0d77c1;">申请退款</font></a></p>
		                        			</c:otherwise>
		                        		</c:choose>
			                        </c:if>
			                        <c:if test="${order.order_status eq '00' or order.order_status eq '99'}">
			                        	<c:if test="${order.is_repay eq '1'}">
			                        		<p ><a style="text-decoration:none;" href="#" onclick="javascript:rePayOrder('${order.order_id}');"><font style="color:#0d77c1;">重新支付</font></a></p>
			                        	</c:if>
			                        </c:if>
			                         <c:if test="${order.order_status eq '77' }">
			   							 <p style="line-height:22px;margin: 0;text-decoration:none;"><a style="text-decoration:none;" href="/query/queryOrderDetail.jhtml?order_id=${order.order_id}&type=detail"><font style="color:#0d77c1;">取消预约</font></a>
			                       </c:if>
		                        </td>
		                   </tr>
	                    </c:forEach>
	                </table>
	                <c:if test="${isShowList == 1}">
	                	<jsp:include page="/pages/common/paging.jsp" />
	                </c:if>
	            </div>
            </c:if>
            <!-- 显示待支付页面 -->
            <c:if test="${selectType eq '5'}">
	         	<div class="myorder oz">
	         	<input  type="hidden" name="selectType" value="1"/>
	                <table >
	                	<tr style="background: #EAEAEA;">
	                	<th>序号</th>
	                	<th>订单号</th>
	                	<th>出发-到达</th>
	                	<th>支付金额</th>
	                	<th>支付时间</th>
	                	<th>出票时间</th>
						<!-- 
						<th>票数</th>
						 -->
						<th>订单类型</th>
						<th>订单状态</th>
						<th>操作</th>                	
	                	</tr>
	                	<c:forEach items="${orderList}" var="order" varStatus="idx">
		                    <tr >
		                    	<td>${idx.index+1}</td>
		                    	<td>${order.order_id}</td>
		                    	<td>${order.from_city}-${order.to_city}</td>
		                    	<td>
		                    	<c:if  test="${order.ticket_ps_type eq '11'}">
		                    	${order.ticket_pay_money + order.bx_pay_money + order.server_pay_money+25}
		                    	</c:if>
		                    	<c:if  test="${order.ticket_ps_type ne '11'}">
		                    	${order.ticket_pay_money + order.bx_pay_money + order.server_pay_money}
		                    	</c:if>
		                    	元</td>
		            		    <td><p >${order.pay_time_start}</p><p style="line-height:22px;margin: 0;"> ${order.pay_time_end}</p></td>
		                        <td><p >${order.out_ticket_time_start}</p><p style="line-height:22px;margin: 0;">${order.out_ticket_time_end}</p></td>
		                        <!-- 
		                        <td>${order.cpCount}</td>
		                         -->
		                        <td>
		                        	<c:choose>
		                        		<c:when test="${order.server_pay_money eq '5.000'}">
		                        			SVIP
		                        		</c:when>
		                        		<c:otherwise>
		                        			<c:choose>
		                        				<c:when test="${order.bx_pay_money eq '0.000'}">
		                        					普通
		                        				</c:when>
		                        				<c:otherwise>
		                        					VIP
		                        				</c:otherwise>
		                        			</c:choose>
		                        		</c:otherwise>
		                        	</c:choose>
		                        </td>
		                        <td>${orderStatusMap[order.order_status]}</td>
		                        <td>
			                     <p style="line-height:22px;margin: 0;text-decoration:none;"><a style="text-decoration:none;" href="/query/queryOrderDetail.jhtml?order_id=${order.order_id}&type=detail"><font style="color:#0d77c1;">明细</font></a>
			                     <a style="text-decoration:none;" href="#" onclick="javascript:deleteOrder('${order.order_id}','${selectType }');"><font style="color:#0d77c1;">删除</font></a>
			                     </p>
		                         <p ><a style="text-decoration:none;" href="#" onclick="javascript:rePayOrder('${order.order_id}');"><font style="color:#0d77c1;">重新支付</font></a></p>
			                   </td>
		                   </tr>
	                    </c:forEach>
	                </table>
	                <c:if test="${isShowList == 1}">
	                	<jsp:include page="/pages/common/paging.jsp" />
	                </c:if>
	            </div>
            </c:if>
            <!-- 显示出票中页面 -->
             <c:if test="${selectType eq '1'}">
	         	<div class="myorder oz">
	         	<input  type="hidden" name="selectType" value="1"/>
	                <table >
	                	<tr style="background: #EAEAEA;">
	                	<th>序号</th>
	                	<th>订单号</th>
	                	<th>出发-到达</th>
	                	<th>支付金额</th>
	                	<th>支付时间</th>
	                	<th>出票时间</th>
	                	<th>退款时间</th>
						<!-- 
						<th>票数</th>
						 -->
						<th>订单类型</th>
						<th>订单状态</th>
						<th>退款状态</th>
						<th>操作</th>                	
	                	</tr>
	                	<c:forEach items="${orderList}" var="order" varStatus="idx">
		                    <tr >
		                    	<td>${idx.index+1}</td>
		                    	<td>${order.order_id}</td>
		                    	<td>${order.from_city}-${order.to_city}</td>
		                    	<td>
		                    	<c:if  test="${order.ticket_ps_type eq '11'}">
		                    	${order.ticket_pay_money + order.bx_pay_money + order.server_pay_money+25}
		                    	</c:if>
		                    	<c:if  test="${order.ticket_ps_type ne '11'}">
		                    	${order.ticket_pay_money + order.bx_pay_money + order.server_pay_money}
		                    	</c:if>
		                    	元</td>
		            		    <td><p >${order.pay_time_start}</p><p style="line-height:22px;margin: 0;"> ${order.pay_time_end}</p></td>
		                        <td><p >${order.out_ticket_time_start}</p><p style="line-height:22px;margin: 0;">${order.out_ticket_time_end}</p></td>
		                        <td><p style="line-height:22px;margin: 0;">${order.refund_time_start}</p><p style="line-height:22px;margin: 0;">${order.refund_time_end}</p></td>
		                        <!-- 
		                        <td>${order.cpCount}</td>
		                         -->
		                        <td>
		                        	<c:choose>
		                        		<c:when test="${order.server_pay_money eq '5.000'}">
		                        			SVIP
		                        		</c:when>
		                        		<c:otherwise>
		                        			<c:choose>
		                        				<c:when test="${order.bx_pay_money eq '0.000'}">
		                        					普通
		                        				</c:when>
		                        				<c:otherwise>
		                        					VIP
		                        				</c:otherwise>
		                        			</c:choose>
		                        		</c:otherwise>
		                        	</c:choose>
		                        </td>
		                        <td>${orderStatusMap[order.order_status]}</td>
		                        <td>
		                        <c:if test="${!empty order.refund_status}">
		                			${rsStatusMap[order.refund_status]}
		                        </c:if>
		                        </td>
		                        <td>
			                     <p style="line-height:22px;margin: 0;"><a style="text-decoration:none;" href="/query/queryOrderDetail.jhtml?order_id=${order.order_id}&type=detail"><font style="color:#0d77c1;">明细</font></a>
			                        </p>
			                        <c:if test="${fn:contains('44', order.order_status )}"><!-- 12,22,33,44 -->
		                        		<c:choose>
		                        			<c:when test="${order.order_status eq '44' and order.out_ticket_type eq '22' }">
		                        			</c:when>
		                        			<c:when test="${order.can_refund != null and order.can_refund ne '1'}">
		                        			</c:when>
		                        			<c:when test="${order.is_before eq '1' and order.deadline_ignore ne '1'}">
	                        				</c:when>
		                        			<c:when test="${order.is_deadline eq '1' and order.deadline_ignore ne '1'}">
		                        			</c:when>
		                        			<c:when test="${order.ticket_ps_type eq '11'}">
		                        			</c:when>
		                        			<c:otherwise>
		                        			<p ><a style="text-decoration:none;" href="/query/queryOrderRefund.jhtml?order_id=${order.order_id}&type=refund"><font style="color:#0d77c1;">申请退款</font></a></p>
		                        			</c:otherwise>
		                        		</c:choose>
			                        </c:if>
			                         <c:if test="${order.order_status eq '77' }">
			                          <p style="line-height:22px;margin: 0;text-decoration:none;"><a style="text-decoration:none;" href="/query/queryOrderDetail.jhtml?order_id=${order.order_id}&type=detail"><font style="color:#0d77c1;">取消预约</font></a>
			                       </c:if>
			                   </td>
		                   </tr>
	                    </c:forEach>
	                </table>
	                <c:if test="${isShowList == 1}">
	                	<jsp:include page="/pages/common/paging.jsp" />
	                </c:if>
	            </div>
            </c:if>
            <!-- 显示出票成功页面 -->
            <c:if test="${selectType eq '2'}">
	         	<div class="myorder oz">
	         	<input  type="hidden" name="selectType" value="2"/>
	                <table >
	                	<tr style="background: #EAEAEA;">
	                	<th>序号</th>
	                	<th>订单号</th>
	                	<th>出发-到达</th>
	                	<th>支付金额</th>
	                	<th>支付时间</th>
	                	<th>出票时间</th>
	                	<th>退款时间</th>
						<!-- 
						<th>票数</th>
						 -->
						<th>订单类型</th>
						<th>订单状态</th>
						<th>退款状态</th>
						<th>操作</th>                	
	                	</tr>
	                	<c:forEach items="${orderList}" var="order" varStatus="idx">
		                    <tr >
		                    	<td>${idx.index+1}</td>
		                    	<td>${order.order_id}</td>
		                    	<td>${order.from_city}-${order.to_city}</td>
		                    	<td>
		                    	<c:if  test="${order.ticket_ps_type eq '11'}">
		                    	${order.ticket_pay_money + order.bx_pay_money + order.server_pay_money+25}
		                    	</c:if>
		                    	<c:if  test="${order.ticket_ps_type ne '11'}">
		                    	${order.ticket_pay_money + order.bx_pay_money + order.server_pay_money}
		                    	</c:if>
		                    	元</td>
		                		<td><p style="line-height:22px;margin: 0;">${order.pay_time_start}</p><p style="line-height:22px;margin: 0;"> ${order.pay_time_end}</p></td>
		                        <td><p style="line-height:22px;margin: 0;">${order.out_ticket_time_start}</p><p style="line-height:22px;margin: 0;">${order.out_ticket_time_end}</p></td>
		                        <td><p style="line-height:22px;margin: 0;">${order.refund_time_start}</p><p style="line-height:22px;margin: 0;">${order.refund_time_end}</p></td>
		                        <!-- 
		                        <td>${order.cpCount}</td>
		                         -->
		                        <td>
		                        	<c:choose>
		                        		<c:when test="${order.server_pay_money eq '5.000'}">
		                        			SVIP
		                        		</c:when>
		                        		<c:otherwise>
		                        			<c:choose>
		                        				<c:when test="${order.bx_pay_money eq '0.000'}">
		                        					普通
		                        				</c:when>
		                        				<c:otherwise>
		                        					VIP
		                        				</c:otherwise>
		                        			</c:choose>
		                        		</c:otherwise>
		                        	</c:choose>
		                        </td>
		                        <td>${orderStatusMap[order.order_status]}</td>
		                        <td>
		                        <c:if test="${!empty order.refund_status}">
		                        <c:choose>
		                        	<c:when test="${order.refund_type eq '2' && (order.refund_status eq '00' || order.refund_status eq '11' || order.refund_status eq '22' || order.refund_status eq '33')}">
		                        		差额退款中
		                        	</c:when>
		                        	<c:when test="${order.refund_type eq '2' && order.refund_status eq '44'}">
		                        		已差额退款
		                        	</c:when>
		                        	<c:otherwise>
		                        		${rsStatusMap[order.refund_status]}
		                        	</c:otherwise>
		                        </c:choose>
		                        </c:if>
		                        </td>
		                        <td>
			                    <p >
			                    	<a style="text-decoration:none;" href="/query/queryOrderDetail.jhtml?order_id=${order.order_id}&type=detail"><font style="color:#0d77c1;">明细</font></a>
			                        <c:if test="${order.order_status eq '44'}">
			                     	<a style="text-decoration:none;" href="javascript:_printIT('${order.order_id}');"><font style="color:#0d77c1;">打印</font></a>
	                				</c:if>
			                     </p>
		                        <c:if test="${fn:contains('44', order.order_status )}"><!-- 12,22,33,44 -->
	                        		<c:choose>
	                        			<c:when test="${order.order_status eq '44' and order.out_ticket_type eq '22' }">
	                        			</c:when>
	                        			<c:when test="${order.can_refund != null and order.can_refund ne '1'}">
	                        			</c:when>
	                        			<c:when test="${order.is_before eq '1' and order.deadline_ignore ne '1'}">
	                        			</c:when>
	                        			<c:when test="${order.is_deadline eq '1' and order.deadline_ignore ne '1'}">
	                        			</c:when>
	                        			<c:when test="${order.ticket_ps_type eq '11'}">
		                        			</c:when>
	                        			<c:otherwise>
	                        			<p ><a style="text-decoration:none;" href="/query/queryOrderRefund.jhtml?order_id=${order.order_id}&type=refund"><font style="color:#0d77c1;">申请退款</font></a></p>
	                        			</c:otherwise>
	                        		</c:choose>
		                        </c:if>
			                   </td>
		                   </tr>
	                    </c:forEach>
	                </table>
	                <c:if test="${isShowList == 1}">
	                	<jsp:include page="/pages/common/paging.jsp" />
	                </c:if>
	            </div>
            </c:if>
        	<!-- 显示出票失败页面 -->
            <c:if test="${selectType eq '3'}">
	         	<div class="myorder oz">
	         	<input  type="hidden" name="selectType" value="3"/>
	                <table >
	                	<tr style="background: #EAEAEA;">
	                	<th>序号</th>
	                	<th>订单号</th>
	                	<th>出发-到达</th>
	                	<th>支付金额</th>
	                	<th>支付时间</th>
	                	<th>出票时间</th>
	                	<th>退款时间</th>
						<!-- 
						<th>票数</th>
						 -->
						<th>订单类型</th>
						<th>订单状态</th>
						<th>退款状态</th>
						<th>操作</th>                	
	                	</tr>
	                	<c:forEach items="${orderList}" var="order" varStatus="idx">
		                    <tr >
		                    	<td>${idx.index+1}</td>
		                    	<td>${order.order_id}</td>
		                    	<td>${order.from_city}-${order.to_city}</td>
		                    	<td>
		                    	<c:if  test="${order.ticket_ps_type eq '11'}">
		                    	${order.ticket_pay_money + order.bx_pay_money + order.server_pay_money+25}
		                    	</c:if>
		                    	<c:if  test="${order.ticket_ps_type ne '11'}">
		                    	${order.ticket_pay_money + order.bx_pay_money + order.server_pay_money}
		                    	</c:if>
		                    	元</td>
		                		<td><p style="line-height:22px;margin: 0;">${order.pay_time_start}</p><p style="line-height:22px;margin: 0;"> ${order.pay_time_end}</p></td>
		                        <td><p style="line-height:22px;margin: 0;">${order.out_ticket_time_start}</p><p style="line-height:22px;margin: 0;">${order.out_ticket_time_end}</p></td>
		                        <td><p style="line-height:22px;margin: 0;">${order.refund_time_start}</p><p style="line-height:22px;margin: 0;">${order.refund_time_end}</p></td>
		                        <!-- 
		                        <td>${order.cpCount}</td>
		                         -->
		                        <td>
		                        	<c:choose>
		                        		<c:when test="${order.server_pay_money eq '5.000'}">
		                        			SVIP
		                        		</c:when>
		                        		<c:otherwise>
		                        			<c:choose>
		                        				<c:when test="${order.bx_pay_money eq '0.000'}">
		                        					普通
		                        				</c:when>
		                        				<c:otherwise>
		                        					VIP
		                        				</c:otherwise>
		                        			</c:choose>
		                        		</c:otherwise>
		                        	</c:choose>
		                        </td>
		                        <td>${orderStatusMap[order.order_status]}</td>
		                        <td>
		                        <c:if test="${!empty order.refund_status}">
		                			${rsStatusMap[order.refund_status]}
		                        </c:if>
		                        </td>
		                        <td>
			                    <p ><a style="text-decoration:none;" href="/query/queryOrderDetail.jhtml?order_id=${order.order_id}&type=detail"><font style="color:#0d77c1;">明细</font></a>
			                        <c:if test="${order.order_status eq '44'}">
			                     	<a style="text-decoration:none;" href="javascript:_printIT('${order.order_id}');"><font style="color:#0d77c1;">打印</font></a>
	                				</c:if>
			                     </p>
		                        <c:if test="${fn:contains('44', order.order_status )}"><!-- 12,22,33,44 -->
	                        		<c:choose>
	                        			<c:when test="${order.order_status eq '44' and order.out_ticket_type eq '22' }">
	                        			</c:when>
	                        			<c:when test="${order.can_refund != null and order.can_refund ne '1'}">
	                        			</c:when>
	                        			<c:when test="${order.is_before eq '1' and order.deadline_ignore ne '1'}">
	                        			</c:when>
	                        			<c:when test="${order.is_deadline eq '1' and order.deadline_ignore ne '1'}">
	                        			</c:when>
	                        			<c:when test="${order.ticket_ps_type eq '11'}">
		                        			</c:when>
	                        			<c:otherwise>
	                        			<p ><a style="text-decoration:none;" href="/query/queryOrderRefund.jhtml?order_id=${order.order_id}&type=refund"><font style="color:#0d77c1;">申请退款</font></a></p>
	                        			</c:otherwise>
	                        		</c:choose>
		                        </c:if>
			                  </td>
		                   </tr>
	                    </c:forEach>
	                </table>
	                <c:if test="${isShowList == 1}">
	                	<jsp:include page="/pages/common/paging.jsp" />
	                </c:if>
	            </div>
            </c:if>
            
               	<!-- 显示退款结果页面 -->
            <c:if test="${selectType eq '4'}">
	         	<div class="myorder oz">
	         	<input  type="hidden" name="selectType" value="4"/>
	                <table >
	                	<tr style="background: #EAEAEA;">
	                	<th>序号</th>
	                	<th>订单号</th>
	                	<th>出发-到达</th>
	                	<th>支付金额</th>
	                	<th>支付时间</th>
	                	<th>出票时间</th>
	                	<th>退款时间</th>
						<!-- 
						<th>票数</th>
						 -->
						<th>订单类型</th>
						<th>订单状态</th>
						<th>退款状态</th>
						<th>操作</th>                	
	                	</tr>
	                	<c:forEach items="${orderList}" var="order" varStatus="idx">
	                	<c:if test="${!empty order.order_status}">
		                    <tr >
		                    	<td>${idx.index+1}</td>
		                    	<td>${order.order_id}</td>
		                    	<td>${order.from_city}-${order.to_city}</td>
		                    	<td>
		                    	<c:if  test="${order.ticket_ps_type eq '11'}">
		                    	${order.ticket_pay_money + order.bx_pay_money + order.server_pay_money+25}
		                    	</c:if>
		                    	<c:if  test="${order.ticket_ps_type ne '11'}">
		                    	${order.ticket_pay_money + order.bx_pay_money + order.server_pay_money}
		                    	</c:if>
		                    	元</td>
		                    	<td><p style="line-height:22px;margin: 0;">${order.pay_time_start}</p><p style="line-height:22px;margin: 0;"> ${order.pay_time_end}</p></td>
		                        <td><p style="line-height:22px;margin: 0;">${order.out_ticket_time_start}</p><p style="line-height:22px;margin: 0;">${order.out_ticket_time_end}</p></td>
		                        <td><p style="line-height:22px;margin: 0;">${order.refund_time_start}</p><p style="line-height:22px;margin: 0;">${order.refund_time_end}</p></td>
		                        <!-- 
		                        <td>${order.cpCount}</td>
		                         -->
		                        <td>
		                        	<c:choose>
		                        		<c:when test="${order.server_pay_money eq '5.000'}">
		                        			SVIP
		                        		</c:when>
		                        		<c:otherwise>
		                        			<c:choose>
		                        				<c:when test="${order.bx_pay_money eq '0.000'}">
		                        					普通
		                        				</c:when>
		                        				<c:otherwise>
		                        					VIP
		                        				</c:otherwise>
		                        			</c:choose>
		                        		</c:otherwise>
		                        	</c:choose>
		                        </td>
		                        <td>${orderStatusMap[order.order_status]}</td>
		                        <td>
		                        <c:if test="${!empty order.refund_status}">
		                        <c:choose>
		                        	<c:when test="${order.refund_type eq '2' && (order.refund_status eq '00' || order.refund_status eq '11' || order.refund_status eq '22' || order.refund_status eq '33')}">
		                        		差额退款中
		                        	</c:when>
		                        	<c:when test="${order.refund_type eq '2' && order.refund_status eq '44'}">
		                        		已差额退款
		                        	</c:when>
		                        	<c:otherwise>
		                        		${rsStatusMap[order.refund_status]}
		                        	</c:otherwise>
		                        </c:choose>
		                        </c:if>
		                        </td>
		                        <td>
		                        <p ><a style="text-decoration:none;" href="/query/queryOrderDetail.jhtml?order_id=${order.order_id}&type=detail"><font style="color:#0d77c1;">明细</font></a>
									<c:if test="${order.order_status eq '44'}">
			                     	<a style="text-decoration:none;" href="javascript:_printIT('${order.order_id}');"><font style="color:#0d77c1;">打印</font></a>
	                				</c:if>
			                    </p>
		                        <c:if test="${fn:contains('44', order.order_status )}"><!-- 12,22,33,44 -->
	                        		<c:choose>
	                        			<c:when test="${order.order_status eq '44' and order.out_ticket_type eq '22' }">
	                        			</c:when>
	                        			<c:when test="${order.can_refund != null and order.can_refund ne '1'}">
	                        			</c:when>
	                        			<c:when test="${order.is_before eq '1' and order.deadline_ignore ne '1'}">
	                        			</c:when>
	                        			<c:when test="${order.is_deadline eq '1' and order.deadline_ignore ne '1'}">
	                        			</c:when>
	                        			<c:when test="${order.ticket_ps_type eq '11'}">
		                        			</c:when>
	                        			<c:otherwise>
	                        			<p ><a style="text-decoration:none;" href="/query/queryOrderRefund.jhtml?order_id=${order.order_id}&type=refund"><font style="color:#0d77c1;">申请退款</font></a></p>
	                        			</c:otherwise>
	                        		</c:choose>
		                        </c:if>
		                       </td>
		                   </tr>
		                </c:if>
	                    </c:forEach>
	                </table>
	                <c:if test="${isShowList == 1}">
	                	<jsp:include page="/pages/common/paging.jsp" />
	                </c:if>
	            </div>
            </c:if>
            
            </form>
        </div>
        <!--右边内容 end-->
    </div>
</div>
<script type="text/javascript">
	  $(document).ready(function(){
    	$("#oneMonthOrder").focus();
	  });
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
      
      function deleteOrder(order_id,selectType){
				var dialog = art.dialog({
					lock: true,
					fixed: true,
					left: '50%',
					top: '20%',
				    title: '提示',
				    okVal: '确认',
				    icon: "/images/warning.png",
				    content: '删除后无法恢复，依然删除该订单？',
				    ok: function(){
						$.ajax({
							url:"/order/deleteOrder.jhtml?order_id="+order_id,
							type: "POST",
							cache: false,
							success: function(res){
								if(res=='success'){
										window.location='/query/queryOrderList.jhtml?selectType='+selectType;
								}else{
									alert("删除失败，请联系客服！");
								}
								}
				  		});
					 }
			});
      }
</script>    
    
</body>
</html>
