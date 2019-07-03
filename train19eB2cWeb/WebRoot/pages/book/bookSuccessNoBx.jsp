<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/tld/trainUtil.tld" prefix="tn"%> 
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>19trip旅行</title>
<link rel="stylesheet" href="/css/reset-min.css" type="text/css" />
<link rel="stylesheet" href="/css/style.css" type="text/css"/>
<script type="text/javascript" src="/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
function timer(){ 
	var old=document.getElementById("mytime").innerText; 
	document.getElementById("mytime").innerText = parseInt(old) - 1; 
	if(parseInt(old)==1){ 
		//window.location = "www.baidu.com"; 
	} 
	settime = setTimeout("timer()",1000); 
} 
</script>
</head>

<body onload="timer()">
<!--以下是头部logo部分start -->
<%@ include file="/pages/common/header.jsp"%>
<!--以下是头部logo部分end -->

<!--以下是头部步骤head_step部分 -->
<div class="head_step">
	<ul>
		<li class="on">填写订单</li>
		<li class="on">在线支付</li>
		<li class="on">订单完成</li>
	</ul>
</div>

<div id="order_con">
    
    <div class="order_in">
    	<div class="order_intit">订单信息</div>
        <div class="success_con">
        	<dl class="successdl">
        		<dt><img src="/images/0005.gif" width="128" height="128" /></dt>
        		<dd>
                <strong></strong>
                </dd>
                <dd class="successdd">
               	支付已成功！我们正在努力为您出票<br />
            	您的订单号：<span>${orderInfo.order_id }</span><br />
            	支付金额：<b>￥<fmt:formatNumber value="${orderInfo.pay_money }" type="currency" pattern="#0.0"/></b>
                </dd>
        	</dl>
            <p class="succ_last"> <b class="red"><span id="mytime">11</span>秒</b>  后自动跳转至&nbsp;&nbsp;&nbsp;&nbsp; <a href="#">我的订单&gt;&gt;</a></p>
        </div>
  </div>
</div>

<!-- footer start  -->
<%@ include file="/pages/common/footer.jsp"%> 
<!-- footer end --> 
</body>
</html>
