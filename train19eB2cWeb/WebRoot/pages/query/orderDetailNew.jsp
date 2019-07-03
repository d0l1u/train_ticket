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
//立即支付
function toPay(orderId){
	window.location='/order/orderComfirm.jhtml?order_id='+orderId;
}

function getRTime(){
	var stop_pay_time = $("#stop_pay_time").val();
    var EndTime= new Date(stop_pay_time); //  2014/12/29 17:00:00
    var NowTime = new Date();
    var t =EndTime.getTime() - NowTime.getTime();
	if(t>0){
		var m=Math.floor(t/1000/60%60);
	    var s=Math.floor(t/1000%60);
	    m<10 ? m="0"+m : m=m;
	    s<10 ? s="0"+s : s=s;
	    //document.getElementById("t_d").innerHTML = d + "天";
	    //document.getElementById("t_h").innerHTML = h + "时";
	    document.getElementById("t_m").innerHTML = m;
	    document.getElementById("t_s").innerHTML = s;
	}else{
		var url = "/order/orderCancle.jhtml?order_id=" + $("#order_id").val()+"&type=detail&version="+new Date();
    	window.location = url;
	}
    
}

$().ready(function(){
	var deffer_time = $("#deffer_time").val();
	var order_status = $("#order_status").val();
	//alert(deffer_time+"  "+order_status);
	if(deffer_time=='0' && order_status=='33'){
		setInterval(getRTime,1000);
	}
});
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
        <form action="queryOrderDetail.jhtml" method="post">
        <input type="hidden" id="stop_pay_time" value="${orderInfo.stop_pay_time}" />
        <input type="hidden" id="order_id" value="${orderInfo.order_id}" />
        <input type="hidden" id="deffer_time" value="${orderInfo.deffer_time}" />
        <input type="hidden" id="order_status" value="${orderInfo.order_status}" />
		<!--以下是我的支付订单详情MyBepaidbx正文部分-->
	<!-- <ul class="trainOrder"><li>订单详情</li></ul>-->
		<div class="trainO_con">
    		<div class="message">
    			<h3 class="message_tit bepaid_tit">车次信息</h3>
	            <table class="message_ck">
	            <tr>
	            	<td width="14%"><strong>${orderInfo.train_no}次</strong></td>
	            	<td width="15%"><span>${orderInfo.from_city}</span><br /><br />${orderInfo.from_time}</td>
	            	<td width="12%">${orderInfo.travel_time}<br /> <b class="line_arrow"></b><br /></td>
	            	<td width="15%"><span>${orderInfo.to_city}</span><br /><br />${orderInfo.to_time}</td>
	            	<td width="22%">坐席：&nbsp;${seatTypeMap[orderInfo.seat_type]}<c:if test="${!empty wz_ext && wz_ext eq '1'}">[备选无座]</c:if><br /> <br /><br /></td>
	                <td width="22%">&nbsp;车票单价：<span style="color:#f90;">￥<fmt:formatNumber value="${orderInfo.cp_pay_money }" type="currency" pattern="#0.0" /></span>元 <br /><br /><br /></td>
	            </tr>  
	            </table>
	        </div>

	<!--以下是MyBepaidbx订单信息部分 -->
    	<div class="message">
    		<h3 class="message_tit bepaid_tit"> 订单信息</h3> 
        	<table class="message_pho bepaid_tb">
            <tr>
	        	<td>订单号：&nbsp;&nbsp;${orderInfo.order_id}</td>
	        	<td>预订时间：&nbsp;&nbsp;${orderInfo.create_time }</td>
            </tr>
            <tr>
	        	<td>取票单号：&nbsp;&nbsp;${orderInfo.out_ticket_billno}</td>
	        	<td>支付金额：&nbsp;&nbsp;<b>￥<fmt:formatNumber value="${orderInfo.pay_money}" type="currency" pattern="#0.0" /></b>（总票价￥<fmt:formatNumber value="${orderInfo.ticket_pay_money}" type="currency" pattern="#0.0" />+总保险金额￥<fmt:formatNumber value="${orderInfo.bx_pay_money}" type="currency" pattern="#0.0" />）</td>
            </tr>
            <c:forEach items="${rsList}" var="rs" varStatus="idx">
	            <c:if test="${!empty rs.refund_type && rs.refund_type eq '5' && !empty rs.user_remark}">
	            <tr>
	            	<td colspan="2">失败原因：&nbsp;&nbsp;${outFailReasonMap[rs.user_remark]}</td>
	            </tr>
	           </c:if>
         	</c:forEach>
            <tr>
	            <td></td>
	            <td class="nopaid_zt">订单状态：<strong>
	            	<c:choose>
	            		<c:when test="${orderInfo.refund_status eq '00'}">
		            		${orderStatusMap[orderInfo.order_status]}
		            	</c:when>
		            	<c:otherwise>
		            		${refundStatusMap[orderInfo.refund_status] }
		            	</c:otherwise>
	            	</c:choose>
	            </strong></td>
            </tr>
        </table> 
        </div>
       
    <c:if test="${bx eq 'no' }">    
	<!--以下是MyBepaidbx乘客信息部分 -->
    	<div class="message">
    		<h3 class="message_tit bepaid_tit">乘客信息</h3>
    		<c:forEach var="detailInfo" items="${detailList}" varStatus="idx">
        	<table class="message_pho bepaid_tb">
        		<c:if test="${idx.index != 0}">
                     <%
                     	out.println("<div class=\"oz pas1\"><p class=\"border_top\"></p>");
                     %>
           		</c:if>
	            <tr>
		        	<td>&nbsp;姓&nbsp;名：&nbsp;&nbsp;${detailInfo.user_name}</td>
		        	<td>&nbsp;乘客类型：&nbsp;&nbsp;${ticketTypeMap[detailInfo.ticket_type]}</td>
	            </tr>
	            <tr>
		        	<td>&nbsp;证件类型：&nbsp;&nbsp;${idsTypeMap[detailInfo.ids_type]}</td>
		        	<td>&nbsp;证件号码：&nbsp;&nbsp;${detailInfo.user_ids}</td>
	            </tr>
	            <c:if test="${idx.index != 0}">
                     <%
                     	out.println("</div>");
                     %>
           		</c:if>
	        </table>
	        </c:forEach>
	    </div>
	</c:if>

	        
  	<c:if test="${bx eq 'yes' }">    
	<!--以下是MyBepaidbx乘客信息部分 -->
    	<div class="message">
    		<h3 class="message_tit bepaid_tit">乘客信息</h3>
        	<table class="bepaid_messtb">
		    	<tr class="order_tit">
					<th width="10%">乘车人姓名</th>    
					<th width="10%">乘客类型</th>    
					<th width="8%">证件类型</th>    
					<th width="20%">证件号码</th>    
					<th width="8%">车厢</th>    
					<th width="8%">座位号</th>    
					<th width="10%">交通意外险</th>    
					<th width="18%">保险单号</th>    
			    </tr>
		    <tbody>
		    <c:forEach var="detailInfo" items="${detailList}" varStatus="idx">
		    	<tr>
		        	<td>${detailInfo.user_name}</td>
		        	<td>${ticketTypeMap[detailInfo.ticket_type]}</td>
		        	<td>${idsTypeMap[detailInfo.ids_type]}</td>
		        	<td>${detailInfo.user_ids}</td>
		        	<td>${detailInfo.train_box}</td>
		        	<td>${detailInfo.seat_no}</td>
		        	<td><c:choose>
	                        	<c:when test="${!empty detailInfo.bx_name}">
	                        		已购买
	                        	</c:when>
	                        	<c:otherwise>
	                        		未购买
	                        	</c:otherwise>
                        	</c:choose></td>
		        	<td>${detailInfo.bx_code}</td>
		        </tr>
		    </c:forEach>
		    </tbody>
		    </table>
     	</div>  
	</c:if>
           
	<!--以下是MyBepaidbx联系人信息部分 -->
    	<div class="message">
    		<h3 class="message_tit bepaid_tit"> 联系人信息</h3>
            <table class="message_pho bepaid_tb">
	            <tr>
		        	<td>&nbsp;姓&nbsp;名：&nbsp;&nbsp;${orderInfoPs.link_name}</td>
		        	<td>&nbsp;手机号码：&nbsp;&nbsp;${orderInfoPs.link_phone}</td>
	            </tr>
       		</table>
		</div>
<c:if test="${fpMap != null}">	
	<!--以下是MyBepaidbx报销凭证部分 -->
    	<div class="message">
    		<h3 class="message_tit bepaid_tit">报销凭证</h3> 
        	<table class="message_pho bepaid_tb">
	            <tr>
	        		<td>&nbsp;姓&nbsp;名：&nbsp;&nbsp;${fpMap.fp_receiver }</td>
	        		<td>&nbsp;手机号码：&nbsp;&nbsp;${fpMap.fp_phone }</td>
	            </tr>
	            <tr>
		        	<td>&nbsp;地&nbsp;址：&nbsp;&nbsp;${fpMap.fp_address }</td>
		        	<td>&nbsp;邮&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;编：&nbsp;&nbsp;${fpMap.fp_zip_code }</td>
	            </tr>
	        </table> 
        </div>         
</c:if>
         
	<!--以下是MyBepaidbx立即支付按钮部分 -->
        <div class="Bepaid_last">
        	<c:if test="${(orderInfo.order_status eq '33') and orderInfo.deffer_time eq '0'}">
	        	<div class="btn1" style="float:left;" onclick="toPay('${orderInfo.order_id}');">立即支付</div>
	        </c:if>
	        <c:if test="${orderInfo.order_status eq '21' }">
	        	<div class="btn1" style="float:left;" onclick="window.location='/buyTicket/bookIndex.jhtml'">重新购票</div>
	        </c:if>
	        <div class="btn1" style="float:left;" onclick="javascript:history.back(-1);">返&nbsp;回</div>
	        <c:if test="${(orderInfo.order_status eq '33') and orderInfo.deffer_time eq '0'}">
				<p class="time_show">剩余支付时间：<span><span id="t_m">19</span>:<span id="t_s">59</span></span></p>
			</c:if>
			
		</div>
		
	</div>
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
