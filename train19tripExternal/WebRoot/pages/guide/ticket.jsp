<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<!-- InstanceBegin template="/Templates/业务介绍.dwt" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>火车票业务用户帮助指南--票务篇</title>
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
        <li class="current"><a href="ticket.jsp">票务篇</a></li>
        <li><a href="infoSearch.jsp">信息查询篇</a></li>
        <li><a href="refund.jsp">退票篇</a></li>
        <li><a href="namePhrase.jsp">名词解释篇</a></li>
      </ul>
    </div>
  </div>
  <div class="yew_r_con oz"><!-- InstanceBeginEditable name="EditRegion3" -->
    <h2>火车票业务用户帮助指南</h2>
    <div class="con_con">
      <h6>票务篇</h6>
      <ul>
        <li>
          <h3>火车票---订票步骤</h3>
        </li>
        <li>
          <p> 如果是开通审批后的用户，可以根据查询到的余票信息，点击“订购”按钮进行订购，进入订购页面录入详细的订购信息，点击“确认”按钮后，到支付页面进行支付，出票成功后我们会短信通知给订票用户。您也可以通过“订单查询”查询订单的状态。</p>
          <img src="/images/ser.jpg" alt="查询" width="650" height="426" /> <img src="/images/order.jpg" alt="订购" width="650" height="327" /> <img src="/images/mes.jpg" width="650" height="162" alt="填写信息" /> <img src="/images/pay.jpg" width="650" height="244" alt="支付订单" /> </li>
        <li>
          <h3>火车票---怎么取票</h3>
        </li>
        <li>
          <p>如果您购买的是电子票，出票成功后，可凭身份证直接到火车站或者规定的取票点进行取票。</p>
        </li>
        <li>
          <h3>火车票---可购什么类型的车票</h3>
        </li>
        <li>
          <p>目前平台暂只支持成人票、儿童票的订购。</p>
        </li>
        <li>
          <h3>火车票---异地票手续费</h3>
        </li>
        <li>
          <p> 异地票在代售点或者车站窗口取票时手续费为5元；而在自动售票机上取票时，是不收取手续费的。</p>
          <p>注：所谓异地票是指票面始发地和取票地不一致。如：王女士在北京购买一张郑州到西安的火车票，若在北京取票则为异地取票；若在郑州取票则不是异地票。</p>
        </li>
        <li>
          <h3>火车票---购买卧铺票</h3>
        </li>
        <li>
          <p>在网上订购卧铺车票时，上铺、中铺、或下铺的坐席是随机分配的，不能具体指定。平台是按下铺的价格进行扣款，如果订到上铺或者中铺会产生铺位差价；这个差价我们将后返至代理商19e账户。</p>
        </li>
        <li>
          <h3>火车票---购买儿童票</h3>
        </li>
        <li>
          <p>儿童票订票步骤和成人票是一样的，但有以下几点要求，请注意：</p>
          <p><p>①儿童原则上不能单独乘车，儿童票须跟成人票一起购买;</p>
            <p>②乘车儿童没有有效身份证件的，可使用同行成年人的有效身份证件信息进行购票；乘车儿童有有效身份证件的，可填写儿童有效身份证件信息：如儿童未办理居民身份证，而使用居民户口簿上的身份证号码购买儿童票的，可凭其户口簿原件领取车票;</p>
            <p>③儿童身高为1.2-1.5米可购买儿童票，超过1.5米须购买成人票。一名成年乘客可以免费携带一名身高不足1.2米的儿童。身高不足1.2米的儿童超过一名时，一名儿童免费，其他儿童需购买儿童票;</p>
            <p>儿童票暂收成人票价，出票后根据实际票价再返差价，差价后返至代理商19e账户中。</p>
        </li>
      </ul>
      
    </div>
    <!-- InstanceEndEditable --></div>
  <!--内容 end--> 
</div>
</body>
<!-- InstanceEnd -->
</html>
