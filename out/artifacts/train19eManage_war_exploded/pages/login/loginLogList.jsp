<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>用户登录账号管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
		<script type="text/javascript" src="/js/jquery.js"></script>
	<script type="text/javascript">
	function submit(url){
		if(confirm("是否提交？")){
			location.href = url; 
		}
	}
	</script>
</head>
	<body>
		<div class="book_manage oz">
			<form action="/login/loginLog.do" method="post">
			<br /><br />
				<ul class="order_num oz">
					<li>
						账号名称：&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="user_name" value="${user_name }"/>
					</li>
				</ul>
				<ul class="oz" style="margin-top: 14px;">
					<li>
						开始时间
						 
						<input type="text" class="text" name="begin_time" readonly="readonly" value="${begin_time }"
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})"/> 
					</li>
					<li>
						结束时间
						<input type="text" class="text" name="end_time" readonly="readonly" value="${end_time}"
						onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" /> 
					</li>
				</ul>
				<br />
				<p>
					<input type="submit" value="查 询" class="btn" />
					&nbsp;&nbsp;&nbsp;
				</p>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #EAEAEA;">
							<th>
								序号
							</th>
							<th>
								用户
							</th>
							<th>
								账号
							</th>
							<th>
								登录时间
							</th>
							<th>
								登录IP
							</th>
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${loginLogs_List}" varStatus="idx">
							<tr>
								<td>
									${idx.index+1}
								</td>
								<td>
									${list.real_name}
								</td>
								<td>
									${list.user_name}
								</td>
								<td>
									${list.login_time}
								</td>
								<td>
									${list.login_ip}
								</td>
								<td>
								</td>
							</tr>
						</c:forEach>
					</table>
					<jsp:include page="/pages/common/paging.jsp" />
				</c:if>
			</form>
		</div>
	</body>
</html>
