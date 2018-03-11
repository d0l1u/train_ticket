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
	.th{width:10%; height: 50px;border:1px solid #dadada;font-weight:bolder;}
	.td{width:10%; height: 50px;border:1px solid #dadada;font-weight:bolder;}
	.fanhui{text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#2C99FF;cursor:pointer;width:100px;height:30px;margin-left: 40%;}
	</style>
	</head>
	<body>
		<div style="margin: 20px auto;">
		<div style="margin-left: 80%;margin-bottom: 5px;"><a href="javascript:backPage();" >返回</a></div>
		<table class="table">
		<tr style="background: #EAEAEA;height: 50px;">
			<th>
				19e
			</th>
			<th class="th">登陆数</th>
			<th class="th">活跃数</th>
			<th class="th">新增数</th>
			<th class="th">填写实名</th>
			<th class="th">通过实名</th>
			<th class="th">待核验</th>
			<th class="th">已实名</th>
			<th class="th">信息错误</th>
		</tr>
		<c:forEach items="${countList }" var="t" varStatus="index">
		<tr style="height: 50px;">
			<td class="td">
				${t.date}
			</td>
				<c:choose>
					<c:when test="${t.agent_login_num ne 0}">
					<td class="td" style="color:#F37022;">${t.agent_login_num}</td>
					</c:when>
					<c:otherwise>
					<td class="td" style="color:#989B9B;">${t.agent_login_num}</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${t.active ne 0}">
					<td class="td" style="color:#F37022;">${t.active}</td>
					</c:when>
					<c:otherwise>
					<td class="td" style="color:#989B9B;">${t.active}</td>
					</c:otherwise>
				</c:choose>
					<c:choose>
					<c:when test="${t.add_user_num ne 0}">
					<td class="td" style="color:#F37022;">${t.add_user_num}</td>
					</c:when>
					<c:otherwise>
					<td class="td" style="color:#989B9B;">${t.add_user_num}</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${t.add_regist_num ne 0}">
					<td class="td" style="color:#F37022;">${t.add_regist_num}</td>
					</c:when>
					<c:otherwise>
					<td class="td" style="color:#989B9B;">${t.add_regist_num}</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${t.regist_num ne 0}">
					<td class="td" style="color:#F37022;">${t.regist_num}</td>
					</c:when>
					<c:otherwise>
					<td class="td" style="color:#989B9B;">${t.regist_num}</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${t.regist_num_sh ne 0}">
					<td class="td" style="color:#F37022;">${t.regist_num_sh}</td>
					</c:when>
					<c:otherwise>
					<td class="td" style="color:#989B9B;">${t.regist_num_sh}</td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${t.regist_num_sm ne 0}">
					<td class="td" style="color:#F37022;">${t.regist_num_sm}</td>
					</c:when>
					<c:otherwise>
					<td class="td" style="color:#989B9B;">${t.regist_num_sm}</td>
					</c:otherwise>
				</c:choose>        
				<c:choose>
					<c:when test="${t.regist_num_error ne 0}">
					<td class="td" style="color:#F37022;">${t.regist_num_error}</td>
					</c:when>
					<c:otherwise>
					<td class="td" style="color:#989B9B;">${t.regist_num_error}</td>
					</c:otherwise>
				</c:choose>
		</tr>
		</c:forEach>
		</table>
		</div>
	</body>
</html>
