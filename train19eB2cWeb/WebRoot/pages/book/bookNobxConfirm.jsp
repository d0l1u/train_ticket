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
function gotoPayNoBx(order_id){
	window.location = "/order/gotoPay.jhtml?order_id="+order_id+'&buyBx=no';
	//$("form:first").attr("action", "/order/gotoPay.jhtml?order_id="+order_id+'&buyBx=no');
	//$("form:first").submit();
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

<!--以下是bookNobxConfirm正文内容部分 -->
<div id="infoInput_all">

<!--以下是bookNobxConfirm左边内容部分 -->

	<div class="info_left">
<!--以下是bookNobxConfirm车次信息部分 -->
    	<div class="message">
    		<h3 class="message_tit" style="line-height:40px;">车次信息</h3>
            <table class="message_c">
            <tr>
            	<td width="14%"><strong>${orderInfo.train_no}次</strong></td>
            	<td width="15%"><span>${orderInfo.from_city}</span><br /><br />
                	${orderInfo.from_time}
                </td>
            	<td width="12%">${orderInfo.travel_time}<br /> <b class="line_arrow"></b><br />
                	</td>
            	<td width="15%"><span>${orderInfo.to_city}</span><br /><br />
                	${orderInfo.to_time}</td>
            	<td width="22%">坐席:&nbsp;${seatTypeMap[orderInfo.seat_type]}<br /> <br /><br /></td>

                <td  width="22%">&nbsp;车票单价:<span style="color:#f90;">￥<fmt:formatNumber value="${detailList[0].cp_pay_money}" type="currency" pattern="#0.0"/></span>元 <br /><br /><br /></td>

            </tr>  
            </table>
        </div>
<!--以下是bookNobxConfirm乘客信息部分 -->
    	<div class="message">
    		<h3 class="message_tit" style="line-height:40px;">乘客信息</h3>
            <c:forEach var="detailInfo" items="${detailList}" varStatus="idx">
            	<c:if test="${idx.index != 0}">
                    <%
                      	out.println("<div class=\"oz pas1\"><p class=\"border_top\"></p></div>");
                    %>
            	</c:if>
	           
	            <table class="message_pho">
			        <tr>
			        	<td>姓名：${detailInfo.user_name}</td>
			        	<td>&nbsp;乘客类型：${ticketTypeMap[detailInfo.ticket_type]}</td>
			        </tr>
			        <tr>
			        	<td>证件类型：${idsTypeMap[detailInfo.ids_type]}</td>
			        	<td>&nbsp;证件号码：${detailInfo.user_ids}</td>
			        </tr>
		        </table>
	           
            </c:forEach>
        </div>
        
<!--以下是bookNobxConfirm联系人信息部分 -->
    	<div class="message">
    		<h3 class="message_tit" style="line-height:40px;">联系人信息</h3> 
	        <table class="message_pho">
		        <tr>
		        	<td>姓 名：${orderInfoPs.link_name}</td>
		        	<td>&nbsp;手机号码：${orderInfoPs.link_phone}</td>
		        </tr>
	        </table>    
       </div>
        
         
        <!--以下是bookNobxConfirm立即支付按钮部分 -->
    	
        <div class="info_last" style="margin-bottom:150px;">
        <div class="btn1" style="float:left;" onclick="gotoPayNoBx('${orderInfo.order_id}');">银联支付</div>
        <div class="btn1" style="float:left;" onclick="gotoPayZfbNoBx('${orderInfo.order_id}');">支付宝支付</div>
        </div>

</div>

<!--以下是bookNobxConfirm右边内容部分 -->

	<div class="info_right">
    <!--以下是infoInput上边支付信息内容部分 -->

    	<div class="info_right_top">
        	<h3 class="info_right_tit">支付信息</h3>
        	<ul>
        		<li>
                <span class="tit_left">票价金额</span>
                <span class="tit_right">￥<fmt:formatNumber value="${detailList[0].cp_pay_money}" type="currency" pattern="#0.0"/>×${count }</span>
                </li>
                <li>
                <span class="tit_left">保险金额</span>
                <span class="tit_right">￥<fmt:formatNumber value="${detailList[0].bx_pay_money}" type="currency" pattern="#0.0"/>×${count }</span>
                </li>
        		<li style="border:0">
                <span class="tit_left"><b style="font-size:16px;">应付总额</b></span>
                <span class="tit_right">
                <b class="font_b">￥<fmt:formatNumber value="${totalPay4Show}" type="currency" pattern="#0.0"/></b></span>
                </li>
        	</ul>
        
        </div>
    <!--以下是infoInput下边温馨提醒内容部分 -->
    	<div class="info_right_down">
        	<h3 class="info_right_tit">温馨提醒</h3>
        	<ul class="warnul">
            <li>1.&nbsp;2014年3月1日起，铁路互联网购票实行实名核验，用户只能为通过实名核验的乘客在线购票。</li>
            <li>2.&nbsp;支付成功后，我们会及时短信通知您购票 结果；您也可以到订单中心查看出票况。</li>
			<li>3.&nbsp;出票失败后，无特殊情况会在2个工作日全额退款至您的支付账户中。</li>
			<li>4.&nbsp;填写乘客信息后，请认真核实。点击确认后不能修改乘客信息。</li>
			</ul>
        </div>
    </div>
</div>




<!-- footer start -->
<%@ include file="/pages/common/footer.jsp"%>
<!-- footer end -->

</body>
</html>
