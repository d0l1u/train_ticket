<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page  import="java.util.*" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>
<c:set var="path" value="${pageContext.request.contextPath}"> </c:set>
<c:choose>
<c:when test="${printStyle eq '1'}">大票打印预览
</c:when><c:otherwise>小票打印预览</c:otherwise></c:choose></title>
<link rel="stylesheet" href="/css/globalStyle.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<style type="text/css">
.xuxian2{border-top:2px dashed #cccccc;height:1.5px;overflow:hidden; margin:10px 0px;}
.td-height2 td{line-height:35px;font-size:12px;font-weight:bold;_font-size:11px;}
</style>
<script type="text/javascript">

//打印小票
function _print() {
	$("#print_small").hide();
	$("#print").show();
	window.print();
	window.close();
}
</script>

</head>

<body>
<input type="hidden" name="orderId" id="orderId" value="${orderInfo.order_id}"/>
<!-- 展示票据信息 -->
<div id="print_small" >
<div class="center" style="margin:auto;height:400px; width:320px; border:2px solid #333;">
		<div class="center" style="height:25px;padding-top:10px;padding-bottom:10px;">
			<div style="float:left;width:90px;text-align:right"><img src="${path}/images/logoPrint.gif" class="mid" /></div>
			<div style="float:left;padding:3px 3px;" class="font20 b yahei">数字便民-交易凭证</div>
		</div>
		<div class="font14 center yahei" style="height:20px;padding-top:3px;">
			<div style="float:left;width:90px;text-align:right;">网点名称：</div>
			<div style="float:left;width:150px;text-align:left;padding-left:10px;">
				【<c:if test="${agentInfo.shop_short_name!=null}">
	     			${agentInfo.shop_short_name}
	     		 </c:if>
	     		 <c:if test="${agentInfo.shop_short_name==null}">
	     			19e小店
	     		 </c:if>】
			</div>
	 	</div>
	 	<div class="font14 center yahei" style="height:20px;padding-top:3px;">
				<div style="float:left;width:90px;text-align:right;">电 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话：</div>
				<div style="float:left;width:150px;text-align:left;padding-left:10px;">
					<c:if test="${agentInfo.user_phone!=null}">
		     		${agentInfo.user_phone}
			     	</c:if>
				</div>
	 	</div>
	 	<div class="dotDiv" style="height:1px;padding:0px;"></div>
	 	<div class="font16 center b yahei" style="height:40px;_height:40px;">
			<div style="float: left;width:100px;text-align:center;padding-top:21px;">
		     	${orderInfo.from_city}
     		</div>	
     		<div style="float: left;width:100px;height:30px;">
     			<div class="font16 center yahei" style="padding-top:13px;height:12px;line-height:12px;">
				     	${orderInfo.train_no}
			 	</div>
			 	<div class="font16 center yahei" style="font-weight:bold;height:8px;line-height:8px;">
						&nbsp;—————>
			 	</div>
     		</div>
     		<div style="float: left;width:100px;text-align:center;padding-top:20px;">
		     	${orderInfo.to_city}
     		</div> 		
	 	</div>
	 	<div class="font14 center yahei b" style="text-align:center;padding-top:8px;height:15px;">
			<div>${orderInfo.travel_time}  ${orderInfo.from_time}  开</div>
	 	</div>
	 	<div class="dotDiv" style="height:1px;padding:0px;"></div>
	 	<div class="font14 center yahei" style="padding:8px 0 0px;height:20px;">
			<div style="float:left;width:90px;text-align:right;">取 &nbsp;票 &nbsp;号：</div>
			<div class="b" style="float:left;width:150px;text-align:left;padding-left:10px;">${orderInfo.out_ticket_billno}</div>
	 	</div>
	 	<div class="font14 center yahei" style="padding-top:3px;height:20px;">
	 		<div style="float:left;width:90px;text-align:right;">总 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;价：</div>
			<div style="float:left;width:150px;text-align:left;padding-left:10px;">￥${orderInfo.ticket_pay_money + orderInfo.bx_pay_money}</div>
   		</div>
   		
   		<div class="font14 center yahei" style="padding-top:3px;height:20px;">
		 		<div style="float:left;width:90px;text-align:right;">坐 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;席：</div>
				<div style="float:left;width:150px;text-align:left;padding-left:10px;">
		     	<c:choose>
				    <c:when test="${orderInfo.seat_type==0}">
				    	商务座
				    </c:when>
				    <c:when test="${orderInfo.seat_type==1}">
				    	特等座
				    </c:when>
				    <c:when test="${orderInfo.seat_type==2}">
				    	一等座
				    </c:when>
				    <c:when test="${orderInfo.seat_type==3}">
				    	二等座
				    </c:when>
				    <c:when test="${orderInfo.seat_type==4}">
				    	高级软卧
				    </c:when>
				    <c:when test="${orderInfo.seat_type==5}">
				    	软卧
				    </c:when>
				    <c:when test="${orderInfo.seat_type==6}">
				    	硬卧
				    </c:when>
				    <c:when test="${orderInfo.seat_type==7}">
				    	软座
				    </c:when>
				    <c:when test="${orderInfo.seat_type==8}">
				    	硬座
				    </c:when>
				    <c:when test="${orderInfo.seat_type==9}">
				    	无座
				    </c:when>
				    <c:otherwise> 
				    	其他
				    </c:otherwise>
			   </c:choose>
	   		</div>
	   	</div>
	<!--    	
 	<c:forEach var="detailInfo" items="${detailList}" varStatus="idx">
	   	<div class="font14 center yahei" style="padding-top:8px;height:20px;">
	 		<div style="float:left;width:90px;text-align:right;">${idx.index+1} &nbsp;&nbsp;${detailInfo.user_name}</div>
			<div style="float:left;width:150px;text-align:left;padding-left:10px;">${detailInfo.train_box}车厢&nbsp;${detailInfo.seat_no}</div>
   		</div>
   	</c:forEach>
   	 -->
   	<div class="dotDiv" style="height:1px;padding:0px;"></div>
   	<table style="padding-left:20px;padding-top:5px;">
   		<c:forEach var="detailInfo" items="${detailList}" varStatus="idx">
	   	<tr>
	   		<td style="width:15px;">${idx.index+1}</td>
	   		<td style="width:70px;">${detailInfo.user_name}</td>
	   		<td style="width:120px;">${detailInfo.train_box}车厢&nbsp;${detailInfo.seat_no}</td>
	   	</tr>
	   	</c:forEach>
	</table>
	   	<div class="dotDiv"></div>
	   	<div class="font14 left yahei" style="padding-top:5px;padding-bottom:15px;">
	   		<div style="float:left;width:90px;text-align:right;">温馨提示：</div>
			<div style="float:left;width:210px;text-align:left;padding-left:5px;">
				根据取票号到火车站换取纸质车票
			</div>
			<div style="float:left;width:210px;text-align:left;padding-left:20px;padding-top:5px;">
				后乘车，本票不能作为乘车凭证！
			</div>
			
	   	</div>
</div>
   <table width="400" border="0" cellpadding="0" cellspacing="20" style="margin:auto;">
   		<tr>
	     <td>	 
		 <div class="left">打印时间：${printTime}</div>
		 <div class="right">19e站-您身边的数字生活便利店!</div>
		 </td>
	   	</tr>
   	<tr>
     <td align="center">
	 <input type="button" value="打印" class="btnBlue" onclick="_print();"/>
	 <span class="pl10"><input type="button" value="关闭" class="btnWhite" onclick="window.close()"/></span>
	 <!-- 
	 	<span class="pl10"><input type="button" value="切换至大票" class="btnGreen" onclick="toBigPrint('${orderInfo.order_id }')"/></span>
	  -->
	 </td>
   </tr>
   </table> 
</div>

<!-- 打印小票 -->
<div id="print" class="center" style="margin:auto;float:center; width:220px; background:#bcbcbc; display:none;">
<div style="width:100%;height:1cm;"></div>
<div align="left" id="msgDetail">
<hr class="xuxian2"/>
<c:if test="${not empty orderInfo}">
<div class="center" style="margin:auto;float:center; width:216px; height:360px;border:2px solid #333;">
		<div class="center" style="height:15px;">
			<div style="float:left;width:60px;text-align:right"><img src="${path}/images/logoPrint.gif" class="mid" /></div>
			<div style="float:left;padding-top:5px;padding-left:3px;" class="font14 b yahei">数字便民-交易凭证</div>
		</div>
		<div class="font12 center yahei" style="padding-top:10px;height:10px;">
				<div style="float:left;width:80px;text-align:right;">网点名称：</div>
				<div style="float:left;width:120px;text-align:left;padding-left:7px;">
					【<c:if test="${agentInfo.shop_short_name!=null}">
		     			${agentInfo.shop_short_name}
		     		 </c:if>
		     		 <c:if test="${agentInfo.shop_short_name==null}">
		     			19e小店
		     		 </c:if>】
				</div>
	 	</div>
	 	<div class="font12 center yahei" style="padding-top:7px;padding-bottom:5px;height:10px;">
				<div style="float:left;width:80px;text-align:right;">电话：</div>
				<div style="float:left;width:120px;text-align:left;padding-left:10px;">
					<c:if test="${agentInfo.user_phone!=null}">
		     		${agentInfo.user_phone}
			     	</c:if>
				</div>
	 	</div>
	 	<div class="dotDiv" style="padding:0px;"></div>
	 	
	 	<div class="font14 center b yahei" style="float:center;height:30px;_height:25px;">
			<div style="float: left;width:75px;text-align:center;padding-top:21px;">
		     	${orderInfo.from_city}
     		</div>	
     		<div style="float: left;width:60px;height:30px;">
     			<div class="font14 center yahei" style="padding-top:13px;height:12px;line-height:12px;">
				     	${orderInfo.train_no}
			 	</div>
			 	<div class="font14 center yahei" style="font-weight:bold;height:5px;line-height:5px;">
			 	---------->
			 	</div>
     		</div>
     		<div style="float: left;width:80px;text-align:center;padding-top:21px;">
		     	${orderInfo.to_city}
     		</div> 		
	 	</div>
	 	<div class="font12 center yahei b" style="float:left;text-align:center;padding-top:10px;">
			<div style="float:left;width:180px;text-align:left;padding-left:50px;">${orderInfo.travel_time}  ${orderInfo.from_time}  开</div>
	 	</div>
	 	<div class="dotDiv"></div>
	 	<!-- 
	 	<div class="font12 center yahei">
				<div style="float:left;width:80px;text-align:right;_padding-top:5px;">取票号：</div>
				<div class="b" style="float:left;width:120px;text-align:left;padding-left:10px;">${orderInfo.out_ticket_billno }</div>
	 	</div>
	 	<div class="font12 center yahei" style="padding-top:15px;_padding-top:5px;">
		 	<div style="float: left;padding-top:5px;">
		 		<div style="float:left;width:80px;text-align:right;">价格：</div>
				<div style="float:left;width:120px;text-align:left;padding-left:10px;">￥${orderInfo.ticket_pay_money + orderInfo.bx_pay_money}</div>
	   		</div>
   		</div>
   		<div class="font12 center yahei" style="padding-top:15px;_padding-top:5px;">
		 	<div style="float: left;padding-top:5px;padding-bottom:3px;">
		 		<div style="float:left;width:80px;text-align:right;">坐席：</div>
				<div style="float:left;width:120px;text-align:left;padding-left:10px;">
		     	<c:choose>
				    <c:when test="${orderInfo.seat_type==0}">
				    	商务座
				    </c:when>
				    <c:when test="${orderInfo.seat_type==1}">
				    	特等座
				    </c:when>
				    <c:when test="${orderInfo.seat_type==2}">
				    	一等座
				    </c:when>
				    <c:when test="${orderInfo.seat_type==3}">
				    	二等座
				    </c:when>
				    <c:when test="${orderInfo.seat_type==4}">
				    	高级软卧
				    </c:when>
				    <c:when test="${orderInfo.seat_type==5}">
				    	软卧
				    </c:when>
				    <c:when test="${orderInfo.seat_type==6}">
				    	硬卧
				    </c:when>
				    <c:when test="${orderInfo.seat_type==7}">
				    	软座
				    </c:when>
				    <c:when test="${orderInfo.seat_type==8}">
				    	硬座
				    </c:when>
				    <c:when test="${orderInfo.seat_type==9}">
				    	无座
				    </c:when>
				    <c:otherwise> 
				    	其他
				    </c:otherwise>
			   </c:choose>
			   </div>
	   		</div>
	   	</div>
	   	-->
	<table style="padding-left:10px;padding-top:5px;">
	   	<tr>
	   		<td style="width:80px;text-align:right;">取票号：</td>
	   		<td class="b" style="width:120px;text-align:left;">${orderInfo.out_ticket_billno }</td>
	   	</tr>
	   	<tr>
	   		<td style="width:80px;text-align:right;">总价：</td>
	   		<td style="width:120px;text-align:left;">￥${orderInfo.ticket_pay_money + orderInfo.bx_pay_money}</td>
	   	</tr>
	   	<tr>
	   		<td style="width:80px;text-align:right;">坐席：</td>
	   		<td style="width:120px;text-align:left;">
				<c:choose>
				    <c:when test="${orderInfo.seat_type==0}">
				    	商务座
				    </c:when>
				    <c:when test="${orderInfo.seat_type==1}">
				    	特等座
				    </c:when>
				    <c:when test="${orderInfo.seat_type==2}">
				    	一等座
				    </c:when>
				    <c:when test="${orderInfo.seat_type==3}">
				    	二等座
				    </c:when>
				    <c:when test="${orderInfo.seat_type==4}">
				    	高级软卧
				    </c:when>
				    <c:when test="${orderInfo.seat_type==5}">
				    	软卧
				    </c:when>
				    <c:when test="${orderInfo.seat_type==6}">
				    	硬卧
				    </c:when>
				    <c:when test="${orderInfo.seat_type==7}">
				    	软座
				    </c:when>
				    <c:when test="${orderInfo.seat_type==8}">
				    	硬座
				    </c:when>
				    <c:when test="${orderInfo.seat_type==9}">
				    	无座
				    </c:when>
				    <c:otherwise> 
				    	其他
				    </c:otherwise>
			   </c:choose>
			</td>
	   	</tr>
	</table>
	   	<div class="dotDiv" style="height:1px;padding:0px;"></div>
   	<table style="padding-left:10px;padding-top:5px;">
   		<c:forEach var="detailInfo" items="${detailList}" varStatus="idx">
	   	<tr>
	   		<td style="width:15px;">${idx.index+1}</td>
	   		<td style="width:70px;">${detailInfo.user_name}</td>
	   		<td style="width:110px;">${detailInfo.train_box}车厢&nbsp;${detailInfo.seat_no}</td>
	   	</tr>
	   	</c:forEach>
	</table>
	   	<div class="dotDiv"></div>
	   	<div class="font12 left yahei" style="padding-top:10px;padding-bottom:10px;">
	   		<div style="float:center;text-align:center;">温馨提示：</div>
			<div style="float:center;text-align:center;">
				根据取票号到火车站换取纸质
			</div>
			<div style="padding-left:10px;text-align:center;">
				车票后乘车，本票不能作为乘车凭证！
			</div>
	   	</div>
</div>
</c:if>        
    
<hr class="xuxian2"/>
<span style="padding-right:15px;font-weight:bold;font-family:'黑体'";>
打印时间：${printTime}
</span>
</div>
<div style="width:100%;height:1cm;"></div>
</div>
</body>
</html>