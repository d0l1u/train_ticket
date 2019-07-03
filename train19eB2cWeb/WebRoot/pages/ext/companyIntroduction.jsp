<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="x-ua-compatible" content="ie=7" />
<title>19trip旅行</title>
<link rel="stylesheet" href="/css/reset-min.css" type="text/css" />
<link rel="stylesheet" href="/css/sreachbar.css" type="text/css"/>
<link rel="stylesheet" href="/css/base.css" type="text/css"/>
<link rel="stylesheet" href="/css/style.css" type="text/css"/>
<link rel="stylesheet" href="/css/travel.css" type="text/css"/>
<script type="text/javascript" src="/js/jquery.js"></script>
</head>
<body>
<!--以下是头部logo部分start -->
<jsp:include flush="true" page="/pages/common/headerNav.jsp">
	<jsp:param name="menuId" value="sh" />
</jsp:include>
<!--以下是头部logo部分end -->

<!--以下是我的旅行正文内容travel_con部分start -->
<div class="travel_con">
	<div class="left_nav">
    	<ul>
    		<li class="title">
            	<span>商户合作</span>
            </li>
    		<li class="con current">
            	<a href="/pages/ext/companyIntroduction.jsp">公司介绍</a>
            </li>
    		<li  class="con">
            	<a href="/pages/ext/advantage.jsp">合作优势</a>
            </li>
    		<li class="con">
            	<a href="/pages/ext/successfulCase.jsp">成功案例</a>
            </li>
    		<li class="con">
            	<a href="/pages/ext/businessContact.jsp">业务联系</a>
            </li>
    	</ul>
    </div>
    
    
	<!--以下是我的旅行正文内容下面订单部分start-->
	<div class="right_con">     
    	<ul class="MyOrder">
    		<li>公司介绍</li>
    	</ul>
		<div class="trainO_con">
	        <p class="coop_p">北京酷游航空服务有限公司成立于2009年，是北京一九易电子商务有限公司旗下的子公司，致力于为用户提供更便捷的出行服务和更顺畅的旅游体验。经过5年的发展，已成为国内领先的机票、火车票专业代购服务平台。</p>
	        <p class="coop_p">北京酷游航空服务有限公司专业从事国际国内机票、旅游酒店、火车票及景点门票等相关商旅服务。经过中国民航总局的批准，已与国内南航、国航、东航、深航等几十家航空公司签署机票售票协议，建立了长期稳定的合作关系。同时其特有的全国火车票在线代购服务自上线运营至今已经成功与去哪儿网、中国移动、中国建设银行等多家知名企业建立良好的合作关系，是国内领先的网上火车票代购服务专业平台。</p>
    	</div>
	</div>
	<!--以下是我的旅行正文内容下面订单部分end-->
	
	
</div>
<!--以下是我的旅行正文内容travel_con部分end -->

<!-- footer start -->
<%@ include file="/pages/common/footer.jsp"%>
<!-- footer end -->

</body>
</html>
