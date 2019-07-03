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

<script type="text/javascript" src="/js/dialog.js"></script>
<style type="text/css">
/****订单弹出框*****/
#drawBill{ position:fixed;_position: absolute;_top: expression(documentElement.scrollTop + 340 + "px"); 
	background:#fff; width:400px; height:280px; border:1px solid #86CBFF; top:50%; left:50%; 
	margin: -200px 0 0 -250px; overflow:hidden; z-index:999999; font-size:14px; color:#000; display:none;}
* html,* html body{background-image:url(about:blank);background-attachment:fixed} 
* html .ie6fixedTL{position:absolute;left:expression(eval(document.documentElement.scrollLeft));top:expression(eval(document.documentElement.scrollTop))} 
* html .ie6fixedBR{position:absolute;left:expression(eval(document.documentElement.scrollLeft+document.documentElement.clientWidth-this.offsetWidth)-(parseInt(this.currentStyle.marginLeft,10)||0)-(parseInt(this.currentStyle.marginRight,10)||0));top:expression(eval(document.documentElement.scrollTop+document.documentElement.clientHeight-this.offsetHeight-(parseInt(this.currentStyle.marginTop,10)||0)-(parseInt(this.currentStyle.marginBottom,10)||0)))} 
</style>

<script type="text/javascript">
function toSumbitRefund(order_id){
	var dialog = new popup("land_on","drawBill","land_off");
	$("#land_on").click();
}
function sumbitRefund(order_id){
	/**
	var count=$(".chk_refund:checked").length;
	if(count==0){
		$(".chk_area").css({border:"2px solid red"});
		alert("请勾选需要退款的车票！");
		return;
	}
	var cp_id="",addr="",index=0,suffix="",user_remark="",isValid=true;
	var cp_id_str="";
	$(".chk_refund").each(function(index){
		cp_id=$(this).val();
		cp_id_str+=cp_id;
		if(index<count-1){
			cp_id_str+=",";
		}
	});
	if(isValid==false){
		return;
	}*/
	
	$("#refundForm").attr("action", "/refund/refund.jhtml?order_id="+order_id);
	$("#refundForm").submit();
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
        <form action="/refund/refund.jhtml" method="post" name="refundForm" id="refundForm" enctype="multipart/form-data">
        		<input type="hidden" name="order_id" value="${orderInfo.order_id }"/>
		    	<input type="hidden" name="token" value="${token}" />
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
	                <td  width="22%">&nbsp;车票单价：<span style="color:#f90;">￥<fmt:formatNumber value="${orderInfo.cp_pay_money }" type="currency" pattern="#0.0" /></span>元 <br /><br /><br /></td>
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
		        	<td class="chk_refund">
		        		${detailInfo.user_name}
		        		<input type="hidden" name="cp_id_${detailInfo.cp_id}" value="${detailInfo.cp_id}" />
	                   	<input type="hidden" name="refund_money_${detailInfo.cp_id}" value="${detailInfo.cp_refund_money}" />
	                   	<input type="hidden" name="refund_status_${detailInfo.cp_id}" value="${detailInfo.refund_status}" />
	                   	<input type="hidden" name="refund_percent_${detailInfo.cp_id}" value="${detailInfo.refund_percent}" />
		        	</td>
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
	         <c:if test="${orderInfo.order_status eq '44'}">
           	 	<c:if test="${empty orderInfo.can_refund or orderInfo.can_refund eq '1'}">
           	 		<div class="btn1" style="float:left;" onclick="toSumbitRefund('${orderInfo.order_id}');">申请退款</div>
           	 	</c:if>
           	 </c:if>
	        <div class="btn1" style="float:left;" onclick="javascript:history.back(-1);">返&nbsp;回</div>
		</div>
		
	</div>
		</form>
	</div>
  	<!--右边内容 end-->
</div>
<!--以下是我的旅行正文内容travel_con部分end -->

<!-- 退票弹框  -->
<div class="return" id="drawBill">
	<p>您确认提交该笔订单的退票申请吗？</p>
	<input type="button" class="btn13" onclick="sumbitRefund('${orderInfo.order_id}');" value="确&nbsp;&nbsp;定" />
	<input type="button" class="btn13" id="land_off" value="取&nbsp;&nbsp;消" />
	<input type="hidden" id="land_on" />
	<dl>
		<dt>温馨提示：</dt>
		<dd>若已换取纸质车票，请携带购票时的有效证件去车站退票窗口办理，网上无法办理。</dd>
	</dl>
</div>

		
<!-- footer start -->
<%@ include file="/pages/common/footer.jsp"%>
<!-- footer end -->

</body>
</html>
