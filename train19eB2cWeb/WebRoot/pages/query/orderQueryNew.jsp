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
//重新支付------没用
function rePayOrder(orderId){
	$.ajax({
		url:"/order/orderRepay.jhtml?order_id="+orderId,
		type: "POST",
		cache: false,
		success: function(res){
			if(res=='success'){
				window.location='/order/orderComfirm.jhtml?order_id='+orderId;
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
//账户安全度颜色显示
$().ready(function(){
	var pwd = $("#user_password").val();
	O_color = "#eeeeee";
	L_color = "#FF0000";
	M_color = "#FF9900";
	H_color = "#33CC00";
	if (pwd == null || pwd == '') {
		Lcolor = Mcolor = Hcolor = O_color;
	} else {
		S_level = checkStrong(pwd);
		switch (S_level) {
		case 0:
			Lcolor = Mcolor = Hcolor = O_color;
		case 1:
			Lcolor = L_color;
			Mcolor = Hcolor = O_color;
			break;
		case 2:
			Lcolor = Mcolor = M_color;
			Hcolor = O_color;
			break;
		default:
			Lcolor = Mcolor = Hcolor = H_color;
		}
	}
	document.getElementById("strength_L").style.background = Lcolor;
	document.getElementById("strength_M").style.background = Mcolor;
	document.getElementById("strength_H").style.background = Hcolor;
	return;
});
//计算密码模式  
function bitTotal(num) {
	modes = 0;
	for (i = 0; i < 4; i++) {
		if (num & 1)
			modes++;
		num >>>= 1;
	}
	return modes;
}
//返回强度级别  
function checkStrong(sPW) {
	if (sPW.length <= 4)
		return 0; //密码太短  
	Modes = 0;
	for (i = 0; i < sPW.length; i++) {
		//密码模式  
		Modes |= CharMode(sPW.charCodeAt(i));
	}
	return bitTotal(Modes);
}
//判断输入密码的类型  
function CharMode(iN) {
	if (iN >= 48 && iN <= 57) //数字  
		return 1;
	if (iN >= 65 && iN <= 90) //大写  
		return 2;
	if (iN >= 97 && iN <= 122) //小写  
		return 4;
	else
		return 8;
}

//申请退款
function refund(orderId){
	window.location='/query/queryOrderRefund.jhtml?order_id='+orderId+'&type=refund';
}
//立即支付
function toPay(orderId){
	window.location='/order/orderComfirm.jhtml?order_id='+orderId;
}


//<input type="hidden" id="finish_time_${order.order_id }" value="${order.finish_time }" />
//支付剩余时间：<strong class="remaining" id="ltime_${order.order_id }">19:59</strong>
$(document).ready(function(){
	//timer();
});

function timer(){
	$(".remaining:visible").each(function(){
		var id_hidden = $(this).attr("id");//ltime_BC1412180933391004
		var order_id = id_hidden.substring(6);//BC1412180933391004
		var finish_time = $("#finish_time_"+order_id).val();//2014-12-18 10:33:35
		etime = parseInt(finish_time.substring(11, 13))*3600 + parseInt(finish_time.substring(14, 16))*60 + parseInt(finish_time.substring(17,19));
		etime+=12000;
		checktime("'"+order_id+"'");//BC1412171650091001
	});
}

function checktime(order_id){
	var time= new Date();
	hours= time.getHours();
	mins= time.getMinutes();
	secs= time.getSeconds();
	ctime=hours*3600+mins*60+secs;
	if(ctime>=etime){
		expired(order_id);
	}else{
		display(order_id);
	}
}

function display(order_id){
	rtime=etime-ctime;
	if (rtime>60){
		m=parseInt(rtime/60);
	}else{
		m=0;
	}
	s=parseInt(rtime-m*60);
	if(s<10){
		s="0"+s;
	}
	//document.getElementById("ltime_"+order_id).innerText=m+":"+s;
	$("#ltime_"+order_id).text(m+":"+s);
	window.setTimeout("checktime("+order_id+")",1000);
}

function expired(order_id){
	//alert("时间到");
	//location.href="http://www.sharejs.com";  //填写要转向的网页
}
/**
function settimes(){
	var time= "2014-12-17 13:18:42";
	etime=13*3600+08*60+42;
	etime+=12000;  
	alert(etime);
	checktime();
}*/
</script>
</head>

<body onload="timer();">
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
    	<div class="hello_k">
			<dl>
        	<dd><img src="/images/hellomonkey.jpg" width="128" height="128" /></dd>
        	<dt>
                <p>你好，<strong>${user_phone }</strong>！<a href="/login/myInfo.jhtml">[完善个人信息]</a></p>
                <input type="hidden" id="user_password" value="${user_password }" />
            	<span class="safe">账户安全度:</span>	
            	<!--以下是密码强弱框-->          
	            <ul class="password_strength clearfix" style="margin:0; float:right;">	
		            <li><p id="strength_L"></p><span>弱</span></li>
		    		<li><p id="strength_M"></p><span>中</span></li>
		    		<li><p id="strength_H"></p><span>强</span></li>
				</ul>
	  		    <!--以上是密码强弱框-->          
			</dt>
        	</dl>
        </div>	
        
		<!--以下是我的旅行正文内容下面订单部分-->
   		<ul class="trainOrder">
   			<li>火车票订单</li>
   		</ul>
   		<c:if test="${count != 0}">
		<div class="trainO_con">
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
			    	<c:if test="${(order.order_status eq '33') and order.deffer_time eq '0'}">
			    		<input type="hidden" id="finish_time_${order.order_id }" value="${order.finish_time }" />
			    		支付剩余时间：<strong class="remaining" id="ltime_${order.order_id }">19:59</strong>
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
                       			<c:when test="${fn:contains('22, 44', order.refund_status ) || order.refund_status eq '' }">
                       				<input type="button" onclick="refund('${order.order_id}');" value="申请退款" class="btn3"/>
                       			</c:when>
                       			<c:when test="${order.can_refund != null and order.can_refund ne '1'}">
                       			</c:when>
                       			<c:when test="${order.is_deadline eq '1' and order.deadline_ignore ne '1'}">
                       			</c:when>
                       			<c:otherwise>
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
	</div>
  	<!--右边内容 end-->
</div>
<!--以下是我的旅行正文内容travel_con部分end -->

<!-- footer start -->
<%@ include file="/pages/common/footer.jsp"%>
<!-- footer end -->
</body>
</html>
