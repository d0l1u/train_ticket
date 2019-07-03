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
<script type="text/javascript" src="/js/jquery.js"></script>
<style type="text/css">
html{ _background:url(j); }
/*图片展示头部 start*/
.header{height:632px;background:#fff url(../images/app/bg.jpg) no-repeat center top;}
.header-con{position:relative;width:960px;margin:0 auto;}
.header01{height:218px;background:url(../images/app/header01.png) no-repeat;}
.header02{height:213px;background:url(../images/app/header02.png) no-repeat;}
.header03{height:203px;background:url(../images/app/header03.png) no-repeat;}
.ios-download,.android-download{position:absolute;top:450px;width:219px;height:60px;background:url(../images/app/show-sprite.png) no-repeat;}
.ios-download{left:24px;background-position: 0 -70px;}
.android-download{left:268px;background-position: 0 0;}
.header a{display:block;width:219px;height:60px;text-indent: -9999px;}
/*图片展示头部 end*/
.attention-us{width:960px;height:214px;margin:0 auto 10px;}
/*footer start*/
.footer{border-top:1px solid #D3D3D3;padding:10px 0 30px;width:980px;margin:0 auto;font:normal 12px/24px arial,"Simsun";}
.footer_nav{text-align:center;}
.footer_nav a:link,.footer_nav a:visited{margin:0 7px;color:#5a5a5a;}
.footer_nav a:hover,.footer_nav a:active{color:#444;text-decoration:underline;}
.footer_nav b{font-weight:400;color:#d3d3d3;}
.copyright{text-align:center;line-height:18px;}
.copyright span{padding:0 10px;color:#5a5a5a;}
/*footer end*/

/*悬浮 start*/
.piao-nav{position:fixed;top:69px;left:50px;_position:absolute;_top:expression(eval(document.documentElement.scrollTop+69));width:111px;height:137px;background:url(images/app/float.png) no-repeat;}
.piao-nav a:link,.piao-nav a:visited{display:block;width:111px;height:42px;padding-top:95px;text-align:center;font-size:14px;color:#fff;}
.piao-nav a:hover,.piao-nav a:active{color:#f60;}
/*悬浮 end*/
</style>
</head>
<body>
<!--以下是头部logo部分start -->
<jsp:include flush="true" page="/pages/common/headerNav.jsp">
	<jsp:param name="menuId" value="sj" />
</jsp:include>
<!--以下是头部logo部分end -->

<!-- 景点门票入口 start
<div class="piao-nav">
    <a href="http://piao.19trip.com">景点门票</a>
</div> -->
<!-- 景点门票入口 end -->
<div class="header">
    <div class="header-con">
        <p class="header01"></p>
        <p class="header02"></p>
        <p class="header03"></p>
        <p class="ios-download"><a href="https://itunes.apple.com/cn/app/gao-tie-jing-ling/id882395891?mt=8" target="_blank">ios下载</a></p>
        <p class="android-download"><a href="/download/gaotie-spirit.apk">Android下载</a></p>
    </div>
</div>
<div class="attention-us">
    <img src="/images/app/attention-us.png" alt="关注我们" width="960" height="214" />
</div>

<!-- footer start -->
<%@ include file="/pages/common/footer.jsp"%>
<!-- footer end -->

</body>
</html>
