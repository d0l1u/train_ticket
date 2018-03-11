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
		<title>分时统计页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
		<script language="javascript" src="/js/json2.js"></script>
	<style>
	.table{border:2px solid #dadada;width:800px;text-align:center;color:#333;margin-left: 50px;}
	.th{width:50px; height: 50px;border:1px solid #dadada;}
	.fanhui{text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#2C99FF;cursor:pointer;width:100px;height:30px;margin-left: 40%;}
	</style>
	</head>
	<body>
		<div style="margin: 20px auto;">
		<div style="margin-left: 80%;margin-bottom: 5px;"><a href="javascript:backPage();" >返回</a></div>
		<table class="table">
		<tr style="background: #EAEAEA;height: 50px;">
			<th style="width:100px;">
			<c:choose>
				<c:when test="${channel eq 'ext'}">
					          商户
				</c:when>
				<c:when test="${channel eq '30101612'}">
						利安
				</c:when>
				<c:otherwise>
						${channel_types[channel] }
				</c:otherwise>
			</c:choose>
			</th>
			<th class="th">0</th>
			<th class="th">1</th>
			<th class="th">2</th>
			<th class="th">3</th>
			<th class="th">4</th>
			<th class="th">5</th>
			<th class="th">6</th>
			<th class="th">7</th>
			<th class="th">8</th>
			<th class="th">9</th>
			<th class="th">10</th>
			<th class="th">11</th>
			<th class="th">12</th>
			<th class="th">13</th>
			<th class="th">14</th>
			<th class="th">15</th>
			<th class="th">16</th>
			<th class="th">17</th>
			<th class="th">18</th>
			<th class="th">19</th>
			<th class="th">20</th>
			<th class="th">21</th>
			<th class="th">22</th>
			<th class="th">23</th>
			<th style="width: 80px;">截止当前</th>
			<th class="th">
			<c:if test="${type eq '1'}">票数</c:if>
			<c:if test="${type eq '2'}">订单</c:if>
			总计</th>
		</tr>
		<c:forEach items="${timeList }" var="t" varStatus="index">
		<tr>
			<th style="border:1px solid #dadada;">${t.time }</th>
			<c:forEach items="${t.countList }" var="s" varStatus="index">
			<c:choose>
				<c:when test="${s.tongji ne 0}">
				<th class="th" style="color:#F37022;">${s.tongji}</th>
				</c:when>
				<c:otherwise>
				<th class="th" style="color:#EAEAEA;">${s.tongji}</th>
				</c:otherwise>
			</c:choose>
			</c:forEach>
			<td style="border:1px solid #dadada;">
				${t.nowAmount }
			</td>
			<td style="border:1px solid #dadada;">
				${t.amount }
			</td>
		</tr>
		</c:forEach>
		</table>
		</div>
	</body>
</html>
