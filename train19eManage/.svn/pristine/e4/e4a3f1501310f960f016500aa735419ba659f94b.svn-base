<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>自提订单明细页</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script type="text/javascript">
		</script>
	</head>
	<body>
		<div class="book_manage account_manage oz">
	        <ul class="order_num oz">
	        	<li><input type="hidden" name="notice_id" value="${notice.notice_id }"/></li>
	        	<li><strong>标题：</strong>${notice.notice_name}</li>
	            <li><strong>内容:</strong>${notice.notice_content}</li>
	            <li><strong>渠道:</strong>${inner_channel[notice.inner_channel] }</li>
	            <li><strong>发布状态:</strong>${noticeStatusMap[notice.notice_status] }</li>
	            <li><strong>生效时间:</strong>${notice.pub_time }</li>
	            <li><strong>到期时间:</strong>${notice.stop_time }</li>
	          <!--   <li><strong>发布区域:</strong>${notice.provinces } </li>   -->
	        </ul>
	        <p><input type="button" value="返回" class="btn" onclick="history.back(-1)"/></p>
		</div>
	</body>
</html>
