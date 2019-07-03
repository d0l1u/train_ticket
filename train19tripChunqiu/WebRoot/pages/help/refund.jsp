<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>退票规定页面</title>
<!-- InstanceEndEditable -->
<link rel="stylesheet" href="/css/style.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<!-- 自适应高度 -->
<script type="text/javascript" src="/js/trendsHeight.js"></script>
<!-- 自适应高度 -->
<!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
</head>

<body>
	<div class="content oz">
    	<!--导航条 start-->
    	<div class="main_nav">
        	<ul class="oz">
        	    <jsp:include flush="true" page="/pages/common/menu.jsp">
					<jsp:param name="menuId" value="help" />
				</jsp:include>
            </ul>
            <div class="slogan"></div>
        </div>
        <!--导航条 end-->
        <!--左边内容 start-->
    	<div class="left_con oz">
        <div class="tip_term oz" style="margin:10px auto 0;">
        	<p class="price_tip">
        		<span>客服电话：400-688-2666&nbsp;&nbsp;转2号键</span>
        		<span style="padding-left:400px;">业务提供方：19旅行</span>
        	</p>
        </div>
    	<!--内容 start-->
        <div class="bread_nav mb10">
        	您所在的位置：<a href="#">业务介绍</a> > <span>退票规定</span>
        	
        </div>
        <div class="yew_l_con oz">
        	<div class="left_nav">
            	<h2>业务介绍</h2>
            	<ul>
                    <li><a href="protocol.jsp">代购服务协议</a></li>
                	<li><a href="trueFalse.jsp">车票真伪识别</a></li>
                    <li class="current"><a href="refund.jsp">退票规定</a></li>
                    <li><a href="operation.jsp">操作演示</a></li>
                    <li><a href="oftenQuestion.jsp">常见问题</a></li>
                    <!--  <li><a href="bxProtocol.jsp">购票保险协议</a></li>-->
                </ul>
            </div>
        </div>
        <div class="yew_r_con oz"><!-- InstanceBeginEditable name="EditRegion3" -->
          <h2>退票规定</h2>
          <div class="con_con">
          	<h5>退票规定</h5>
            <ul>
              <li><p>根据梯次退票方案，9月1日起，票面乘车站开车前48小时以上的，退票时收取票价5%的退票费；开车前24小时以上、不足48小时的，退票时收取票价10%的退票费；开车前不足24小时的，退票时收取票价20%退票费。（注：此9月1日为火车票面发车时间）。并且退的车票的出发站必须是该车站或者是该车站售出的火车票（异地票）。团体旅客必须在开车48小时以前办理退票。</p></li>
              <li><p>旅客要求退票时，应当在购票地车站或票面发售站办理。在发售站开车前，特殊情况也可在开车后2小时内，退还全部票价，核收退票费。团体旅客必须在开车48小时以前办理。</p></li>
              <li><p>旅客开始旅行后不能退票。但如因伤、病不能继续旅行时，经站、车证实，可退还已收票价与已乘区间票价差额；已乘区间不足起码里程时，按起码里程计算；同行人同样办理。</p></li>
              <li><p>退还带有“行”字戳迹的车票时，应先办理行李变更手续。</p></li>
              <li><p>站台票售出不退。</p></li>
              <li><p>必要时，铁路运输企业可以临时调整退票办法，请咨询当地车站或关注车站公告。</p></li>
              <li><p>因承运人责任致使旅客退票时，在发站，退还全部票价；在中途站，退还已收票价与已乘区间票价差额，已乘区间不足起码里程时，退还全部票价；在到站，退还已收票价与已使用部分票价差额，未使用部分不足起码里程按起码里程计算；空调列车因空调设备故障在运行过程中不能修复时，应退还未使用区间的空调票价。均不收退票费。</p></li>
              <li><h3>电子票退票规定：</h3></li>
              <li>1)成功出票后，需要在24小时未换取纸质车票的进行退票， 需要用户在平台上发起退票申请后平台审核后进行退票，会把钱打到用户账户里。<br />2)成功出票后，如果换取纸质车票需要到火车站进行退票后，拿小票提交到平台上，平台审核后进行退款。</li>
            </ul>
          </div>
        <!-- InstanceEndEditable --></div>
        <!--内容 end-->
        </div>
        <!--左边内容 end-->
    </div>
	<!--业务所有标注 start-->
    <div class="business-provider">
    	
    </div>
    <!--业务所有标注 end-->
</body>
<!-- InstanceEnd --></html>
