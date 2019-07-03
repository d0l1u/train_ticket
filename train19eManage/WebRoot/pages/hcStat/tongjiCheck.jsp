<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo"%>
<%@ page import="com.l9e.transaction.vo.PageVo"%>
<%@ page import="java.util.*"%>
<%@ page import="com.l9e.util.JSONUtil"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>核验页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
		<script language="javascript" src="/js/json2.js"></script>
	<style>
	.table{border:2px solid #dadada;width:800px;text-align:center;color:#333;margin-left: 50px;}
	.th{width:15%; height: 50px;border:1px solid #dadada;}
	.td{width:15%; height: 50px;border:1px solid #dadada;}
	.fanhui{text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#2C99FF;cursor:pointer;width:100px;height:30px;margin-left: 40%;}
	</style>
	</head>
	<body>
		<div style="margin: 20px auto;">
		<div style="margin-left: 80%;margin-bottom: 5px;"><a href="javascript:backPage();" >返回</a></div>
		<table class="table">
		<tr style="background: #EAEAEA;height: 50px;">
			<th>
				渠道
			</th>
			<th style="width:20%;">
				日期
			</th>
			<th class="th">核验次数</th>
			<th class="th">成功数</th>
			<th class="th">失败数</th>
			<th class="th">订单数</th>
		</tr>
		<c:forEach items="${countList }" var="t" varStatus="index">
		<tr style="height: 50px;">
			<td class="td" > 
				${t.channel}
			</td>
			<td class="td">
				${t.date}
			</td>
			<td class="td" <c:if test="${t.check_num ne 0 }">style="font-weight:bolder;color:#F37022;"</c:if><c:if test="${t.check_num eq 0 }">style="font-weight:bolder;color:#989B9B;"</c:if>>
				${t.check_num}
			</td>
			<td class="td" <c:if test="${t.success_num ne 0 }">style="font-weight:bolder;color:#F37022;"</c:if><c:if test="${t.success_num eq 0 }">style="font-weight:bolder;color:#989B9B;"</c:if>>
				${t.success_num}
			</td>
			<td class="td" <c:if test="${t.fail_num ne 0 }">style="font-weight:bolder;color:#F37022;"</c:if><c:if test="${t.fail_num eq 0 }">style="font-weight:bolder;color:#989B9B;"</c:if>>
				${t.fail_num}
			</td>
			<td class="td" <c:if test="${t.order_num ne 0 }">style="font-weight:bolder;color:#F37022;"</c:if><c:if test="${t.order_num eq 0 }">style="font-weight:bolder;color:#989B9B;"</c:if>>
				${t.order_num}
			</td>
		</tr>
		</c:forEach>
		</table>
		</div>
	</body>
</html>
