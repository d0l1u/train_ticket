<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>通知公告页面</title>
<link rel="stylesheet" href="/css/style.css"  />
<script type="text/javascript" src="/js/jquery.js"></script>
</head>

<body>
<%@include file="/pages/common/height.jsp"%>
	<div class="notice_wrap oz">
    	<h3>${noticeInfo.notice_name}<span><!--<a href="javascript:history.back(-1);">返回</a>  --></span></h3>
        <div class="times"><span><fmt:formatDate value="${noticeInfo.pub_time}" pattern="yyyy/MM/dd" /></span></div>
        <div class="notice_con oz">
        	${noticeInfo.notice_content}
        </div>
        <div class="back"><input type="button" class="btn" value="返回" onclick="javascript:history.back(-1);" /></div>
    	
    </div>
<script type="text/javascript" language="JavaScript">
	function closeWin(){
		window.close();
	}
</script>
</body>
</html>
