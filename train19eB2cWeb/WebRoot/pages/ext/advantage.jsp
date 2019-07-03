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
    		<li class="con">
            	<a href="/pages/ext/companyIntroduction.jsp">公司介绍</a>
            </li>
    		<li  class="con current">
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
    		<li>合作优势</li>
    	</ul>
		<!--以下是合作优势内容start-->
		<div class="advantage_con">
            <dl class="information01">
            	<dt>市场广阔</dt>
            	<dd>火车出行需求量大，成为市场主流交通趋势。</dd>
            </dl>
            <dl class="information02">
            	<dt>方式灵活</dt>
            	<dd>提供多样化合作方式，合作伙伴自由选择。</dd>
            </dl>
            <dl class="information03">
            	<dt>业务优势</dt>
            	<dd>出票系统自动化，全国范围一站式服务。</dd>
            </dl>
            <dl class="information04">
            	<dt>精准时刻</dt>
            	<dd>火车时刻数据实时更新，准确可靠。</dd>
            </dl>
            <dl class="information05">
            	<dt>结算方便</dt>
            	<dd>结算流程灵活方便。</dd>
            </dl>
            <dl class="information06">
            	<dt>技术支持</dt>
            	<dd>超强技术团队，后期定期维护数据。</dd>
            </dl>
    	</div>
    	<!--以下是合作优势内容end-->
	</div>
	<!--以下是我的旅行正文内容下面订单部分end-->
	
	
</div>
<!--以下是我的旅行正文内容travel_con部分end -->

<!-- footer start -->
<%@ include file="/pages/common/footer.jsp"%>
<!-- footer end -->

</body>
</html>
