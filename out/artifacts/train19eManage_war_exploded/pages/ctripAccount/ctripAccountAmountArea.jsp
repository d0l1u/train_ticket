<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>携程账号出票金额设置页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	    <script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
	    <script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
	    <script type="text/javascript">
	   		 function editArea(id){
				$.ajax({
					type:"POST",
					url:"/ctripAccount/editAmountArea.do",
					data:{
						"id":id,
						"limit_begin":$("#limit_begin"+id).val(),
						"limit_end":$("#limit_end"+id).val()
						},
					success:function(res){
						if(res=='success'){
							alert("操作成功");
						}else {
							alert("操作失败");
						}
					}
				});
			}
	    </script>
	    <style>
	    	.book_manage{width:880px;}
			.book_manage TABLE{width:860px;}
		</style>
	</head>
	<body>
		<div class="book_manage oz">
			<div style="border: 0px solid #00CC00; margin: 10px;">
				<form>
					<table>
						<tr>
							<th>账号级别</th>
							<th>充值卡金额</th>
							<th>金额</th>
							<th>建议区间</th>
							<th>操作</th>
						</tr>
						<c:forEach var="list" items="${amountAreaList}" varStatus="idx">
						<tr style="background:#E0F3ED; ">
							<td>${list.acc_degree}</td>
							<td>
								<c:if test="${list.acc_degree eq '1'}">100</c:if>
								<c:if test="${list.acc_degree eq '2'}">500</c:if>
								<c:if test="${list.acc_degree eq '3'}">1000</c:if>
								<c:if test="${list.acc_degree eq '4'}">2000</c:if>
								<c:if test="${list.acc_degree eq '5'}">5000</c:if>
							</td>
							<td>
								<input type="text" name ="limit_begin"  id="limit_begin${list.id}" value="${list.limit_begin}"/>
								-<input type="text" name ="limit_end" id="limit_end${list.id}" value ="${list.limit_end}"/>
								<input type="hidden" name ="id" id="id" value ="${list.id}"/>
							</td>
							<td>
								<c:if test="${list.acc_degree eq '1'}">100</c:if>
								<c:if test="${list.acc_degree eq '2'}">500</c:if>
								<c:if test="${list.acc_degree eq '3'}">1000</c:if>
								<c:if test="${list.acc_degree eq '4'}">2000</c:if>
								<c:if test="${list.acc_degree eq '5'}">5000</c:if>
							</td>
							<td><a href="javascript:editArea('${list.id}');"> 修改</a></td>
						</tr>
						</c:forEach>
					</table>
				</form>
				<br/>
				<p>
					<input type="button" value="返回" class="btn" onclick="javascript:history.go(-1);" />
				</p>
			</div>
		</div>
	</body>
			
</html>