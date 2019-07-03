<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>火车票预售期</title>
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
    	<!--内容 start-->
        <div class="bread_nav mb10">
        	您所在的位置：<a href="#">业务介绍</a> > <span>购票常识</span>
        	
        </div>
        <div class="yew_l_con oz">
        	<div class="left_nav">
            	<h2>业务介绍</h2>
            	<ul>
                    <li><a href="protocol.jsp">代购服务协议</a></li>
                	<li><a href="trueFalse.jsp">车票真伪识别</a></li>
                    <li><a href="refund.jsp">退票规定</a></li>
                    <li class="current"><a href="bookPeriod.jsp">车票预定期</a></li>
                    <!--  <li><a href="bxProtocol.jsp">购票保险协议</a></li>-->
                </ul>
            </div>
        </div>
        <div class="yew_r_con oz"><!-- InstanceBeginEditable name="EditRegion3" -->
          <h2>火车票预售期</h2>
          <div class="con_con">
            <ul>
              <li><h3>北京、天津、河北（北京铁路局）预售期<b>5</b>天</h3></li>
              <li>普通车次预售期5天，高铁（G字头）、动车组（D字头）、城际（C字头）、直达列车（Z字头）列车预售期不变，为10天，起售时间为早上9：00</li>
              <li><h3>江苏、浙江、上海、安徽（上海铁路局）预售期<b>5</b>天</h3></li>
              <li>普通车次预售期5天，起售时间为早上8：00，高铁（G字头）、动车组（D字头）、直达列车（Z字头）、九龙车车次预售期不变
              </li>
              <li><h3>广东、湖南（广州铁路集团）预售期<b>5</b>天</h3></li>
              <li>普通列车预售期5天，高铁预售期不变</li>
              <li><h3>四川、贵州、重庆（成都铁路局）预售期<b>5</b>天</h3></li>
              <li>普通列车预售期5天，高铁预售期不变</li>
              <li><h3>湖北（武汉铁路局）预售期<b>5-10</b>天</h3></li>
              <li>普通列车预售期6天，起售时间为早上8：30，高铁预售期为10天</li>
              <li><h3>云南（昆明铁路局）预售期<b>5</b>天</h3></li>
              <li>火车预售期5天</li>
              <li><h3>新疆（乌鲁木齐铁路局）预售期<b>5</b>天</h3></li>
              <li>火车票预售期5天</li>
              <li><h3>河南（郑州铁路局）预售期<b>5</b>天</h3></li>
              <li>郑州列车预售期调整为5天，直达（Z字头）列车预售期不变。D字头和G字头动车组部分二等车厢预售期限定为5天</li>
              <li><h3>辽宁、吉林（沈阳铁路局）预售期<b>5</b>天</h3></li>
              <li>列车预售期调整为5天</li>
              <li><h3>山东（济南铁路局）预售期<b>5-11</b>天</h3></li>
              <li>列车预售期调整为5天，高铁（G字头）和动车组（D字头）列车车票按现行预售期不变,仍为提前11天,8:00开始发售。</li>
              <li><h3>广西（南宁铁路局）预售期<b>5</b>天</h3></li>
              <li>火车票预售期5天，起售时间为早上8：00</li>
              <li><h3>陕西（西安铁路局）预售期<b>5</b>天</h3>
              </li>
              <li>火车票预售期5天，起售时间为早上10：00</li>
              <li><p class="declare"><strong>特别声明：</strong>全国路局车票预售期都会随时更新，如铁道部或当地铁路局临时调整应以各路局为准。以上车票预售期数据仅供参考，如因此造成的损失，19e无须承担任何责任。</p></li>
            </ul>
          </div>
        <!-- InstanceEndEditable --></div>
        <!--内容 end-->
    </div>
	<!--业务所有标注 start-->
    <div class="business-provider">
    	
    </div>
    <!--业务所有标注 end-->
</body>
<!-- InstanceEnd --></html>
