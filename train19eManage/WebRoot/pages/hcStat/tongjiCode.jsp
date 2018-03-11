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
		<title>支付宝验证码统计页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
	<style>
	.table{border:2px solid #dadada;width:900px;text-align:center;color:#333;margin-left: 50px;}
	.th{width:50px; height: 50px;border:1px solid #dadada;font-weight:bolder;}
	.td{width:50px; height: 50px;border:1px solid #dadada;font-weight:bolder;}
	</style>
	<script type="text/javascript" src="/js/jquery.js"></script>
	<script type="text/javascript">

	function selectCode(){
		var date = $("#date").val();
		if(date!=null && date!=''){
			window.location.href="/hcStat/tongjiCode.do?date=" + date ;
		}else{
			alert("日期不能为空");
		}
	}
	</script>
	</head>
	<body>
		<div style="margin: 20px auto;">
			<div class="query" style="margin-left:50px;margin-bottom:10px;">
				<input style="font-size:14px;" type="text" class="text" id="date" name="date" value="${date}" /> 
				<input style="font-size:16px;margin-left:10px"  type="button" value="查 询" class="btn" onclick="selectCode();" />
				<font style="color:red">请注意时间格式为年-月-日</font>
			</div>
			
			<table class="table">
				<tr style="background: #EAEAEA;height: 50px;">
					<th></th>
					<c:forEach items="${timeList}" var="time">
						<th class="th">${time} </th>
					</c:forEach>
					
				</tr>
				<c:forEach items="${alipayList}" var="alipay" >
					<tr style="height: 50px;">
						<td class="td">
							${alipay} 
						</td>
						<c:forEach items="${timeList}" var="time">
							<td class="td" style="color:${resultMap[alipay][time] ne 0?'red':'#989B9B'};">
								${resultMap[alipay][time]}
							</td>
						</c:forEach>
					</tr>
				</c:forEach>
			</table>
		</div>
	</body>
</html>
