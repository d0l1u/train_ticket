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
    		<li class="con">
            	<a href="/pages/ext/successfulCase.jsp">成功案例</a>
            </li>
    		<li class="con current">
            	<a href="/pages/ext/businessContact.jsp">业务联系</a>
            </li>
    	</ul>
    </div>
    
    
	<!--以下是我的旅行正文内容下面订单部分start-->
	<div class="right_con">     
    	<ul class="MyOrder">
    		<li>业务联系</li>
    	</ul>
    	<!--以下是业务联系内容start-->
		<div class="trainO_con">
	        <div class="contact_l">
	        	<h3>北京酷游航空服务有限公司</h3>
	            <p>联系电话：<span>400-688-2666</span>转<span>2</span>号键。</p>
	            <p>地址：北京市海淀区农大南路88号院1号楼万霖大厦2层。</p>
	        </div>
	        <div class="contact_r">
	        </div>
    	</div>
    	<!--以下是业务联系内容end-->
	</div>
	<!--以下是我的旅行正文内容下面订单部分end-->
	
	
</div>
<!--以下是我的旅行正文内容travel_con部分end -->

<!-- footer start -->
<%@ include file="/pages/common/footer.jsp"%>
<!-- footer end -->

</body>
</html>
