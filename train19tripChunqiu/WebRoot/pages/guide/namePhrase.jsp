<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<!-- InstanceBegin template="/Templates/业务介绍.dwt" codeOutsideHTMLIsLocked="false" -->
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
  <!--导航条 end--> 
  <!--内容 start-->
  <div class="bread_nav mb10"> 您所在的位置：<a href="#">帮助指南</a> > <span>购票常识</span> </div>
  <div class="yew_l_con oz">
    <div class="left_nav">
      <h2>帮助指南</h2>
      <ul>
        <li><a href="business.jsp">业务篇</a></li>
        <li><a href="ticket.jsp">票务篇</a></li>
        <li><a href="infoSearch.jsp">信息查询篇</a></li>
        <li><a href="refund.jsp">退票篇</a></li>
        <li class="current"><a href="namePhrase.jsp">名词解释篇</a></li>
      </ul>
    </div>
  </div>
  <div class="yew_r_con oz"><!-- InstanceBeginEditable name="EditRegion3" -->
    <h2>火车票业务用户帮助指南</h2>
    <div class="con_con">
      <h6>状态名词解释篇</h6>
      <div class="con_wrap">
    	<ul>
            <li><h3>火车票---名词解释（订单状态）</h3></li>
<li><p>【正在审核】：此状态表明您的业务开通申请已提交成功，请耐心等待。</p>
<p>【业务试用】：此状态表明您的业务开通申请已受理，进入10天的业务使用期，可正常订票。</p>
<p>【审核通过】：此状态表明业务开通申请已通过审核，可正式免费使用火车票业务。</p>
<p>【支付成功】：此状态表明所订车票已完成付款，即票价已从您的19e账户内扣除。</p>
<p>【出票中】：  此状态表明机器人正在处理您所订购的订单，请稍候。</p>
<p>【预订成功】：此状态表明您所订购的车票已成功预订，等待机器人付款。此时乘客会收到短信提示车票预订成功。</p>
<p>【出票成功】：此状态表明机器人已完成付款，此时乘客会收到短信提示出票成功。此时标志着车票已成功订购，用户可去<span style="padding-left:107px;color:#333;">窗口取票。</span></p>
<p>【出票失败】：此状态表明车票订购失败，我们会在24小时内将退款返回至您的19e账户中，请注意查看！</p>
<p>【退款申请中】：此状态表明您的退款申请已提交，等待退款专员处理。</p>
<p>【正在退款】：此状态表明您的退款申请已处理，等待退款（退款会在15天返还至您的19e账户）</p>
<p>【退款完成】：此状态表明退款已经返还至您的19e账户中，请查看资金变动。</p>
<p>【车站小票】：当乘客要求退票时，请先告知其到火车站窗口进行退票，办理退票后乘客就会拿到一张退款凭证—即车站小<span style="padding-left:107px;color:#333;">票，乘客须将车站小票交给代理商，然后由代理商将小票拍照，并将照片传至平台，这样才可以成功提交退款</span><span style="padding-left:107px;color:#333;">申请。</span></p></li>
        </ul>
    </div>
    </div>
    <!-- InstanceEndEditable --></div>
  <!--内容 end--> 
</div>
</body>
<!-- InstanceEnd -->
</html>
