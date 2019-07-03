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
    		<li  class="con">
            	<a href="/pages/ext/advantage.jsp">合作优势</a>
            </li>
    		<li class="con current">
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
    		<li>成功案例</li>
    	</ul>
    	<!--以下是成功案例内容start-->
    	<div class="trainO_con">
        	<h3 class="case_h3">一、中国移动</h3>
        	<dl class="case01">
        		<dt></dt>
        		<dd>
	                <h4>中国移动</h4>
	                <p>中国移动开放了便民服务和包网站。2014年1月25日，北京酷游航空服务有限公司和中国移动合作，为中国移动火车票业务提供便民服务，延伸到火车票盈利模式。</p>
				</dd>
        	</dl>
        	<h3 class="case_h3">二、中国建设银行</h3>
        	<dl class="case02">
        		<dt></dt>
        		<dd>
	                <h4>中国建设银行</h4>
	                <p>中国建设银行针对建行用户提供便民服务。2014年中旬，中国酷游航空服务有限公司与中国建设银行悦生活频道合作，以内嵌方式为悦生活频道提供在线订票服务。</p>
	            </dd>
        	</dl>
        	<h3 class="case_h3">三、春秋航空</h3>
        	<dl class="case03">
        		<dt></dt>
        		<dd>
	                <h4>春秋航空</h4>
	                <p>春秋航空为用户提供机票、旅游、火车票服务。2014年10月11日，北京酷游航空服务有限公司和春秋航空公司通过内嵌的合作方式，为用户提供方便优质的火车票订购服务。延伸火车票盈利模式。</p>
				</dd>
        	</dl>
    	</div>
    	<!--以下是成功案例内容end-->
	</div>
	<!--以下是我的旅行正文内容下面订单部分end-->
	
	
</div>
<!--以下是我的旅行正文内容travel_con部分end -->

<!-- footer start -->
<%@ include file="/pages/common/footer.jsp"%>
<!-- footer end -->

</body>
</html>
