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
        <li><a href="infoSearch.jsp">信息查询篇</a></li>
        <li class="current"><a href="refund.jsp">退票篇</a></li>
        <li><a href="namePhrase.jsp">名词解释篇</a></li>
      </ul>
    </div>
  </div>
  <div class="yew_r_con oz"><!-- InstanceBeginEditable name="EditRegion3" -->
    <h2>火车票业务用户帮助指南</h2>
    <div class="con_con">
      <h6>退票篇</h6>
      <div class="con_wrap">
      <ul>
        <li>
          <h3>退票操作方法</h3>
        </li>
        <li>
          <p>登陆平台进入火车票业务，点击“订单/退款”下“查询”按钮，即可看到该代理商已订购的所有火车票的具体信息。然后请选定所退车票，在“操作”中点击“退款”。找到退款小票上传处，点击“浏览”将退款小票上传至平台即可。</p>
          <p>
          	<img src="/images/ser_order.jpg" class="no_mar" width="650" height="308" alt="订单查询" />
          </p>
          <p>
            <img src="/images/liulan.jpg" class="no_mar" width="650" height="194" alt="退票" />
          </p>
          
          </li>
      </ul>
      </div>
      <div class="con_wrap">
      <ul>
          <li>
          	<h3>退票受理说明（<span class="blue_w">新增</span>）</h3>
          </li>
          <li>
          <p>1.保险问题</p>
          <p class="mb10">一旦购票成功，申请退票时，保险是不退的。</p>
          <p>2.退款手续费</p>
          <p class="mb10">根据梯次退票方案，9月1日起，票面乘车站开车前48小时以上的，退票时收取票价5%的退票费；开车前24小时以上、不足48小时的，退票时收取票价<span class="red">10%</span>的退票费；开车前不足24小时的，退票时收取票价<span class="red">20%</span>退票费。（注：此9月1日为火车票面发车时间）</p>
           <p>3、退款受理时间</p>
           <p> 19e火车票业务受理时间为<span class="blue_w">7:00-23:00</span>，晚23:00之后平台仍提供订票功能，但此类订单的受理和出票时间为<span class="blue_w">次日早7:00之后</span>，请注意！<span class="red">若因23:00之后下单未处理而造成的无法乘车等损失，平台不承担相应责任。</span></p>
          </li>
        </ul>
        </div>
        <div class="con_wrap">
        <ul>
          <li><h3>退票处理方法（<span class="blue_w">已作更新</span>）</h3></li>
          <li><p>1、如果由于系统原因车票出票失败，自动进行退款请求。</p>
          <p class="mb10">处理方法：我们会在<span class="red">24小时内</span>进行退款，不扣除任何费用，自动退回到代理商资金账户中。</p>
          <p>2、如果未换取纸质车票，在发车前<span class="red">大于6小时</span>，发起退款请求</p>
          <p class="mb10">处理方法：用户可以拿身份证到代理商，代理商帮助用户提交退款请求，退款审核后我们将于<span class="red">24小时之内</span>，扣除相应手续费退还到代理商帐户内。</p>
          <p>3、如果未换取纸质车票，在发车前<span class="red">小于6小时</span>，发起退款请求</p>
          <p class="mb10">处理方法：乘客必须去车站拿到退票小票后到代理商网点，代理商需将退票小票上传到退款申请界面后，方可发起退款申请，退款审核后，我们将于15个工作日将<span class="red">扣除20%手续费</span>的退款金额退还到代理商账户内。</p>
          <p>4、如果用户<span class="red">已换取纸质车票</span>后，发起的退款请求</p>
            <p>处理方法：需要用户到火车站出票窗口进行退票，拿到车站的退票小票后到代理商网点，代理商需将退票小票
            上传到退款申请界面后，方可发起退款申请，退款审核后，我们将于<span class="red">15个工作日</span>将扣除相应手续费的退款金额退还到代理商账户内。</p>
        </li>
        </ul>
        </div>
        <div class="con_wrap">
            <ul>
              <li>
                <h3>补充说明：</h3>
              </li>
              <li>
                <p>①发车后12小时后，系统将无法提交退款申请，请联系退款专员进行办理。</p>
                <p>②关于改签，乘客可到任意火车站窗口办理改签。</p>
              </li>
          </ul>
      </div>
      <div class="debook_ask">
          <strong>退款咨询电话：</strong>
    　　<span>400-698-6666</span>
      </div>
    </div>
    <!-- InstanceEndEditable --></div>
  <!--内容 end--> 
</div>
</body>
<!-- InstanceEnd -->
</html>
