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
<script type="text/javascript" src="/js/jquery.blockUI.js"></script>

<style type="text/css">
    .hideDlg
    {
        width:400px;
        display:none;
    }
    .showDlg
    {
        background-color:#ffffdd;
        width:400px;
        position:relative;
        z-index:5;
    }
    .showDeck {
        display:block;
        top:0px;
        left:0px;
        margin:0px;
        padding:0px;
        width:100%;
        height:100%;
        position:absolute;
        z-index:3;
        background-color:transparent;
        filter:"alpha(opacity=80)";
        opacity:"80/100";
        MozOpacity:"80/100";
    }
    .hideDeck {
        display:none;
    }
}
</style>
</head>

<body>
	<div type="hidden" id="weather_login" value="${weatherLogin}">
	<div type="hidden" id="verify_code_hide" value="">
	
	<!--短信发送 start-->
	<div id="divBox" class="hideDlg" style="" >
	    <div class="pop-window-wrap pop-window-mes-wrap oz">
	    	<h2><span class="close-btn" id="close_btn" title="关闭"></span>用户登录</h2>
	    	<div class="mes-con">
		        <dl class="unit-mod">
	            	<dt>手机号：</dt>
	                <dd><input name="user_phone" type="text" id="user_phone" class="text" placeholder="请输入您的手机号" ></dd>
					<dd class="yzm" id="getVerify" ><input type="button" value="获取验证码" class="get-yzm verify_phone"  size="10" onclick="getVerify();"/></dd>
					<dd style="padding-top:5px;display:none;" class="yzm" id="notice_code" ><a class="cannot-get-yzm" href="#"><b id="time_count"></b>s后重新发送</a></dd>
	            </dl>
	            <dl class="unit-mod">
	            	<dt>验证码：</dt>
	                <dd><input name="verify_code" class="text" type="text" id="verify_code" disabled="disabled"/></dd>
	            </dl>
		        <p class="btn-box" style="padding-bottom:10px;">
		        	<input type="button" value="登录" class="login-btn btn" id="reSearch" size="10" onclick="reSearch();"/>&nbsp;&nbsp;
	                <input type="button" value="取消" class="cancel-btn btn" id="cancle" size="10" onclick="cancel();" />
		        </p>
		         <p style="text-align:left;padding-left:20px;font-size:14px;line-height:20px;">
		        	<span style="font-weight:bold;">温馨提醒：</span><br />
		        	<span style="padding-left:24px;">1.请输入下单时所填写的联系人手机号，获取验证码登录</span><br />
		        	<span style="padding-left:24px;">2.若多次尝试后仍无法收到短信，可拨打客服热线咨询订单详情。</span>
		        </p>
	        </div>
	    </div>
    </div>
    <!--短信发送 end-->
	<div class="content oz" id="content_order">
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
	        <div class="tip_term oz" style="margin:10px auto 0;">
	        	<p class="price_tip">
	        		<span>客服电话：400-688-2666&nbsp;&nbsp;转2号键</span>
	        		<span style="padding-left:400px;">业务提供方：19旅行</span>
	        	</p>
	        </div>
    	<br />
    	     <div class="order_type oz">
            	<ul class="order_type_ul">
            		<c:if test="${selectType eq '0'}">
	                	<li class="current current1"><a href="/query/queryOrderList.jhtml?selectType=0&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">全部订单</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=1&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票中(${waitingNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=2&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票成功(${successNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=3&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票失败(${failNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=4&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">退款结果(${refundNum})</a></li>
                    </c:if>
                    	<c:if test="${selectType eq '1'}">
	                	<li class="current1"><a href="/query/queryOrderList.jhtml?selectType=0&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">全部订单</a></li>
	                    <li class="current current1"><a href="/query/queryOrderList.jhtml?selectType=1&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票中(${waitingNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=2&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票成功(${successNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=3&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票失败(${failNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=4&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">退款结果(${refundNum})</a></li>
                    </c:if>
                    	<c:if test="${selectType eq '2'}">
	                	<li class="current1"><a href="/query/queryOrderList.jhtml?selectType=0&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">全部订单</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=1&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票中(${waitingNum})</a></li>
	                    <li class="current current1"><a href="/query/queryOrderList.jhtml?selectType=2&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票成功(${successNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=3&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票失败(${failNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=4&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">退款结果(${refundNum})</a></li>
                    </c:if>
                    	<c:if test="${selectType eq '3'}">
	                	<li class="current1"><a href="/query/queryOrderList.jhtml?selectType=0&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">全部订单</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=1&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票中(${waitingNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=2&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票成功(${successNum})</a></li>
	                    <li class="current current1"><a href="/query/queryOrderList.jhtml?selectType=3&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票失败(${failNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=4&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">退款结果(${refundNum})</a></li>
                    </c:if>
                    	<c:if test="${selectType eq '4'}">
	                	<li class="current1"><a href="/query/queryOrderList.jhtml?selectType=0&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">全部订单</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=1&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票中(${waitingNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=2&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票成功(${successNum})</a></li>
	                    <li class="current1"><a href="/query/queryOrderList.jhtml?selectType=3&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">出票失败(${failNum})</a></li>
	                    <li class="current current1"><a href="/query/queryOrderList.jhtml?selectType=4&oneMonthOrder=${oneMonthOrder}&create_time=${create_time}&order_id=${order_id}&out_ticket_billno=${out_ticket_billno}">退款结果(${refundNum})</a></li>
                    </c:if>
                </ul>
         	</div>
            <form action="/query/queryOrderList.jhtml" method="post">
            <p style="margin-top:20px;">
				 <span style="margin-left:15px;">按订票日期查询：<input style="width:110px;" class="text text2" type="text" id="start_time" name="start_time" 
			                    		value="${start_time}" readonly="readonly" 
								  		onfocus="WdatePicker({doubleCalendar: false ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
				</span>
				<span style="margin-left:15px;">至</span>
				<span style="margin-left:15px;"><input style="width:110px;" type="text" id="end_time" name="end_time" value="${end_time}" 
				 		readonly="readonly"	onfocus="WdatePicker({doubleCalendar: false ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
				</span>
				<span style="margin-left:15px;">按取票单号查询：<input style="width:110px;" type="text" id="out_ticket_billno" name="out_ticket_billno" value="${out_ticket_billno}" 
		                	onkeyup="value=value.replace(/(^\s*)|(\s*$)/g,'');" />
				</span>
				 <span style="margin-left:15px;">
				 <input  type="button" class="btn search_order_btn" onclick="searchOrderList();" value="查询" />
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
	                	<th>票价</th>
	                	<th>支付时间</th>
	                	<th>出票时间</th>
						<th>订单状态</th>
						<th>退款状态</th>
						<th>操作</th>                	
	                	</tr>
	                	<c:forEach items="${orderList}" var="order" varStatus="idx">
		                    <tr >
		                    	<td>${idx.index+1}</td>
		                    	<td>${order.order_id}</td>
		                    	<td>${order.from_city}-${order.to_city}</td>
		                    	<td>${order.ticket_pay_money + order.bx_pay_money}元</td>
		                    	<td><p style="line-height:22px;margin: 0;">${order.pay_time_start}</p><p style="line-height:22px;margin: 0;"> ${order.pay_time_end}</p></td>
		                        <td><p style="line-height:22px;margin: 0;">${order.out_ticket_time_start}</p><p style="line-height:22px;margin: 0;">${order.out_ticket_time_end}</p></td>
		                        <td>${orderStatusMap[order.order_status]}</td>
		                        <td>&nbsp;
		                        <c:if test="${!empty order.refund_status}">
		                			${rsStatusMap[order.refund_status]}
		                        </c:if>
		                        </td>
		                        <td>
		                        <p style="line-height:22px;margin: 0;"><a href="/query/queryOrderDetail.jhtml?order_id=${order.order_id}&type=detail"><font style="color:#0d77c1;">明细</font></a>
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
		                        			<c:otherwise>
		                        			<p style="line-height:22px;margin: 0;"><a href="/query/queryOrderRefund.jhtml?order_id=${order.order_id}&type=refund"><font style="color:#0d77c1;">申请退款</font></a></p>
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
            
            <!-- 显示出票中页面 -->
             <c:if test="${selectType eq '1'}">
	         	<div class="myorder oz">
	         	<input  type="hidden" name="selectType" value="1"/>
	                <table >
	                	<tr style="background: #EAEAEA;">
	                	<th>序号</th>
	                	<th>订单号</th>
	                	<th>出发-到达</th>
	                	<th>票价</th>
	                	<th>支付时间</th>
	                	<th>出票时间</th>
						<th>订单状态</th>
						<th>退款状态</th>
						<th>操作</th>                	
	                	</tr>
	                	<c:forEach items="${orderList}" var="order" varStatus="idx">
		                    <tr >
		                    	<td>${idx.index+1}</td>
		                    	<td>${order.order_id}</td>
		                    	<td>${order.from_city}-${order.to_city}</td>
		                    	<td>${order.ticket_pay_money + order.bx_pay_money}元</td>
		            		    <td><p >${order.pay_time_start}</p><p style="line-height:22px;margin: 0;"> ${order.pay_time_end}</p></td>
		                        <td><p >${order.out_ticket_time_start}</p><p style="line-height:22px;margin: 0;">${order.out_ticket_time_end}</p></td>
		                        <td>${orderStatusMap[order.order_status]}</td>
		                        <td>&nbsp;
		                        <c:if test="${!empty order.refund_status}">
		                			${rsStatusMap[order.refund_status]}
		                        </c:if>
		                        </td>
		                        <td>
			                     <p style="line-height:22px;margin: 0;"><a href="/query/queryOrderDetail.jhtml?order_id=${order.order_id}&type=detail"><font style="color:#0d77c1;">明细</font></a>
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
		                        			<c:otherwise>
		                        			<p ><a href="/query/queryOrderRefund.jhtml?order_id=${order.order_id}&type=refund"><font style="color:#0d77c1;">申请退款</font></a></p>
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
            <!-- 显示出票成功页面 -->
            <c:if test="${selectType eq '2'}">
	         	<div class="myorder oz">
	         	<input  type="hidden" name="selectType" value="2"/>
	                <table >
	                	<tr style="background: #EAEAEA;">
	                	<th>序号</th>
	                	<th>订单号</th>
	                	<th>出发-到达</th>
	                	<th>票价</th>
	                	<th>支付时间</th>
	                	<th>出票时间</th>
						<th>订单状态</th>
						<th>退款状态</th>
						<th>操作</th>                	
	                	</tr>
	                	<c:forEach items="${orderList}" var="order" varStatus="idx">
		                    <tr >
		                    	<td>${idx.index+1}</td>
		                    	<td>${order.order_id}</td>
		                    	<td>${order.from_city}-${order.to_city}</td>
		                    	<td>${order.ticket_pay_money + order.bx_pay_money}元</td>
		                		<td><p style="line-height:22px;margin: 0;">${order.pay_time_start}</p><p style="line-height:22px;margin: 0;"> ${order.pay_time_end}</p></td>
		                        <td><p style="line-height:22px;margin: 0;">${order.out_ticket_time_start}</p><p style="line-height:22px;margin: 0;">${order.out_ticket_time_end}</p></td>
		                        <td>${orderStatusMap[order.order_status]}</td>
		                        <td>&nbsp;
		                        <c:if test="${!empty order.refund_status}">
		                			${rsStatusMap[order.refund_status]}
		                        </c:if>
		                        </td>
		                        <td>
			                    <p >
			                    	<a href="/query/queryOrderDetail.jhtml?order_id=${order.order_id}&type=detail"><font style="color:#0d77c1;">明细</font></a>
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
	                        			<c:otherwise>
	                        			<p ><a href="/query/queryOrderRefund.jhtml?order_id=${order.order_id}&type=refund"><font style="color:#0d77c1;">申请退款</font></a></p>
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
	                	<th>票价</th>
	                	<th>支付时间</th>
	                	<th>出票时间</th>
						<th>订单状态</th>
						<th>退款状态</th>
						<th>操作</th>                	
	                	</tr>
	                	<c:forEach items="${orderList}" var="order" varStatus="idx">
		                    <tr >
		                    	<td>${idx.index+1}</td>
		                    	<td>${order.order_id}</td>
		                    	<td>${order.from_city}-${order.to_city}</td>
		                    	<td>${order.ticket_pay_money + order.bx_pay_money}元</td>
		                		<td><p style="line-height:22px;margin: 0;">${order.pay_time_start}</p><p style="line-height:22px;margin: 0;"> ${order.pay_time_end}</p></td>
		                        <td><p style="line-height:22px;margin: 0;">${order.out_ticket_time_start}</p><p style="line-height:22px;margin: 0;">${order.out_ticket_time_end}</p></td>
		                        <td>${orderStatusMap[order.order_status]}</td>
		                        <td>&nbsp;
		                        <c:if test="${!empty order.refund_status}">
		                			${rsStatusMap[order.refund_status]}
		                        </c:if>
		                        </td>
		                        <td>
			                    <p ><a href="/query/queryOrderDetail.jhtml?order_id=${order.order_id}&type=detail"><font style="color:#0d77c1;">明细</font></a>
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
	                        			<c:otherwise>
	                        			<p ><a href="/query/queryOrderRefund.jhtml?order_id=${order.order_id}&type=refund"><font style="color:#0d77c1;">申请退款</font></a></p>
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
	                	<th>票价</th>
	                	<th>支付时间</th>
	                	<th>出票时间</th>
						<th>订单状态</th>
						<th>退款状态</th>
						<th>操作</th>                	
	                	</tr>
	                	<c:forEach items="${orderList}" var="order" varStatus="idx">
		                    <tr >
		                    	<td>${idx.index+1}</td>
		                    	<td>${order.order_id}</td>
		                    	<td>${order.from_city}-${order.to_city}</td>
		                    	<td>${order.ticket_pay_money + order.bx_pay_money}元</td>
		                    	<td><p style="line-height:22px;margin: 0;">${order.pay_time_start}</p><p style="line-height:22px;margin: 0;"> ${order.pay_time_end}</p></td>
		                        <td><p style="line-height:22px;margin: 0;">${order.out_ticket_time_start}</p><p style="line-height:22px;margin: 0;">${order.out_ticket_time_end}</p></td>
		                        <td>${orderStatusMap[order.order_status]}</td>
		                        <td>&nbsp;
		                        <c:if test="${!empty order.refund_status}">
		                			${rsStatusMap[order.refund_status]}
		                        </c:if>
		                        </td>
		                        <td>
		                        <p ><a href="/query/queryOrderDetail.jhtml?order_id=${order.order_id}&type=detail"><font style="color:#0d77c1;">明细</font></a>
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
	                        			<c:otherwise>
	                        			<p ><a href="/query/queryOrderRefund.jhtml?order_id=${order.order_id}&type=refund"><font style="color:#0d77c1;">申请退款</font></a></p>
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
            </form>
        </div>
        <!--左边内容 end-->
    </div>

<script type="text/javascript">
  	$(document).ready(function(){
	  var al = $("#weather_login").val();
	  if(al=='false'){
		  autoBlockFormAndSetWH("divBox",250,250,"close_btn","#ffffff",true,400,245);
	  }
  	});

  	function searchOrderList(){
		var start_time = $("#start_time").val();
		var end_time = $("#end_time").val();
		var search = true;
		if((start_time!=''&& start_time!=undefined) && (end_time!='' && end_time!=undefined)){
			start_time =  start_time.replace(/-/g,"/");
		      //// str =  str.replace("T"," "); 
		   	var start = new Date(start_time);

		   	end_time =  end_time.replace(/-/g,"/");
		      //// str =  str.replace("T"," "); 
		   	var end = new Date(end_time);
		   	if(Date.parse(start)>Date.parse(end)){
		   		var dialog = art.dialog({
					lock: true,
					fixed: true,
					left: '50%',
					top: '50%',
				    title: '提示',
				    okVal: '确认',
				    icon: "/images/warning.png",
				    content: "开始时间不能大于结束时间！",
				    ok: function(){}
				});
				search = false;
				return;
			}
		}
		if(!search){
			return false;
		}
		$("form:first").attr("action","/query/queryOrderList.jhtml");
		$("form:first").submit();
  	}
  	function getVerify(){
		  var phone = $("#user_phone").val();
		  if(phone=='' || phone ==undefined){
			  var dialog = art.dialog({
					lock: true,
					fixed: true,
					left: '50%',
					top: '50%',
				    title: '提示',
				    okVal: '确认',
				    icon: "/images/warning.png",
				    content: "手机号不能为空！",
				    ok: function(){}
				});
				return false;
		  }else if(!/^1(3[\d]|4[57]|5[\d]|8[\d]|70)\d{8}$/g.test($.trim(phone))){
			var dialog = art.dialog({
				lock: true,
				fixed: true,
				left: '50%',
				top: '50%',
			    title: '提示',
			    okVal: '确认',
			    icon: "/images/warning.png",
			    content: "请填写正确的手机号！",
			    ok: function(){}
			});
			return false;
		  }
		  $("#getVerify").hide();
		  $.ajax({
				url:"/query/getVerifyCode.jhtml?user_phone="+phone,
				type: "POST",
				cache: false,
				success: function(res){
					if(res!="FAILURE"){
						$("#user_phone").attr("disabled","disabled");
						$("#verify_code").removeAttr("disabled");
						$("#verify_code_hide").val(res);
						$("#notice_code").show();
						secondCounter(120);
					}
				}
		});
  	}
	
  	function secondCounter(defSec) {
        $("#time_count").html(defSec--);
        if(defSec < 0){
        	$("#verify_code_hide").val('');
        	$("#getVerify").show();
			$("#notice_code").hide();
        	$("#user_phone").removeAttr("disabled");
        }else{
            window.setTimeout("secondCounter("+defSec+")",1000);
        }
     }

     
  	function reSearch(){
		var verify_code = $("#verify_code").val();
		if(verify_code==$("#verify_code_hide").val()){
			$("form:first").attr("action", "/query/queryOrderList.jhtml?user_id="+$("#user_phone").val());
			$("#verify_code_hide").val("");
			$("form:first").submit();
		}else{
			//消息框
			var dialog = art.dialog({
				lock: true,
				fixed: true,
				left: '50%',
				top: '50%',
			    title: '提示',
			    okVal: '确认',
			    icon: "/images/warning.png",
			    content: "验证码输入错误！",
			    ok: function(){}
			});
		}
	  }
    function cancel()
    {
    	setTimeout($.unblockUI,15);
    }

</script>    
    
</body>
</html>
