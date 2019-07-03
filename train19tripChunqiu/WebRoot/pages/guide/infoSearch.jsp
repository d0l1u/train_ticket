<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>火车票业务用户帮助指南</title>
<!-- InstanceEndEditable -->
<link rel="stylesheet" href="/css/style.css" />
<!--[if IE 6]>
  <script src="js/png.js"></script>
  <script>
      PNG.fix('*');
  </script>
  <![endif]-->
<!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
<script type="text/javascript" src="/js/jquery.js"></script>
<!-- 自适应高度 -->
<script type="text/javascript" src="/js/trendsHeight.js"></script>
<!-- 自适应高度 -->
</head>

<body>
<div class="content oz"> 
	<!--导航条 start-->
	<div class="main_nav">
    	<ul class="oz">
    	    <jsp:include flush="true" page="/pages/common/menu.jsp">
				<jsp:param name="menuId" value="guide" />
			</jsp:include>
        </ul>
        <div class="slogan"></div>
    </div>
  <!--导航条 end-->
  <!--内容 start-->
  <div class="bread_nav mb10"> 您所在的位置：<a href="#">帮助指南</a> > <span>购票常识</span> </div>
  <div class="yew_l_con oz">
    <div class="left_nav">
      <h2>帮助指南</h2>
      <ul>
        <li><a href="business.jsp">业务篇</a></li>
        <li><a href="ticket.jsp">票务篇</a></li>
        <li class="current"><a href="infoSearch.jsp">信息查询篇</a></li>
        <li><a href="refund.jsp">退票篇</a></li>
        <li><a href="namePhrase.jsp">名词解释篇</a></li>
      </ul>
    </div>
  </div>
  <div class="yew_r_con oz"><!-- InstanceBeginEditable name="EditRegion3" -->
    <h2>火车票业务用户帮助指南</h2>
    <div class="con_con">
      <h6>信息查询篇</h6>

      <ul>
        <li>
          <h3>火车票---查询订单</h3>
        <li>
          <p>登陆平台进入火车票业务，点击“订单/退款”下“查询”按钮，即可看到该代理商已订购的所有火车票的具体信息。</p>
          <img src="/images/order_ser.jpg" width="650" height="289" alt="" />
        </li>
        <li>
          <h3>火车票---查询账户资金情况</h3>
        <li>
          <p>登陆平台进入火车票业务，点击页面右上角“资金变动”按钮，即可看到代理商19e账户的资金周转情况。</p>
          <img src="/images/money_change.jpg" width="650" height="110" alt="资金变动" />
        </li>
      </ul>
     
    </div>
    <!-- InstanceEndEditable --></div>
  <!--内容 end--> 
</div>
</body>
<!-- InstanceEnd -->
</html>
