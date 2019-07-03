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
			<%
				LoginUserVo loginUserVo = (LoginUserVo) request.getSession()
						.getAttribute("loginUserVo");
				if ("2".equals(loginUserVo.getUser_level())) {
			%>
			<form action="/login/loginUserList.do" method="post">
			<br /><br />
				<ul class="order_num oz">
					<li>
						账号名称：&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="user_name" value="${user_name}"/>
					</li>
					<li><a href="/login/loginLogPage.do">查看登录日志</a></li>
				</ul>
				<br />
				<p>
					<input type="submit" value="查 询" class="btn" />
					&nbsp;&nbsp;&nbsp;
					<input type="button" value="添加"
						onclick="location.href = '/login/addUserInfo.do'" class="btn" />
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
								邮箱
							</th>
							<th>
								手机
							</th>
							<th>
								是否启用
							</th>
							<th>
								审核状态
							</th>
							<th>
								用户级别
							</th>
							<th>
								负责省份
							</th>
							<th>
								最后登录时间
							</th>
							<th>
								最后登录IP
							</th>
							<th>
								登录次数
							</th>
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${loginUser_List}" varStatus="idx">
							<tr
								<c:if test="${fn:contains('2', list.user_level )}">
									style="background: #FFAAD5;"
								</c:if>
								<c:if test="${fn:contains('1', list.user_level )}">
									style="background: #ECFFFF;"
								</c:if>
								<c:if test="${fn:contains('1.1', list.user_level )}">
									style="background: #FFFFCC;"
								</c:if>
								<c:if test="${fn:contains('1.2', list.user_level )}">
									style="background: #BBFFBB;"
								</c:if>
							>
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
									${list.email}
								</td>
								<td>
									${list.user_phone}
								</td>
								<td>
									${user_Isopen[list.user_IsOpen] }
								</td>
								<td>
									${user_Status[list.user_status]}
								</td>
								<td>
									${user_Types[list.user_level]}
								</td>
								<td>
									${list.supervise_name }
								</td>
								<td>
									${list.last_login_time }
								</td>
								<td>
									${list.last_login_ip }
								</td>
								<td>
									${list.loginTotal }
								</td>
								<td>
									<c:if test="${list.user_IsOpen eq '1'}">
										<a
											href="javascript:submit('/login/deleteUserAccount.do?user_id=${list.user_id }')">删除</a>
										<a
											href="javascript:submit('/login/updateUserIsOpen.do?user_IsOpen=0&user_id=${list.user_id }')">启用</a>
									</c:if>
									<c:if test="${list.user_IsOpen eq '0'}">
										<a
											href="javascript:submit('/login/queryUpdateUserInfo.do?user_id=${list.user_id }')">修改</a>
										<a
											href="javascript:submit('/login/updateUserIsOpen.do?user_IsOpen=1&user_id=${list.user_id }')">停用</a>
									</c:if>
								</td>
							</tr>
						</c:forEach>
					</table>
					<jsp:include page="/pages/common/paging.jsp" />
				</c:if>
				<br />
				<p></p>
			</form>
			<%
				} else {
			%>
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
						邮箱
					</th>
					<th>
						手机
					</th>
					<th>
						审核状态
					</th>
					<th>
						用户级别
					</th>
					<th>
						负责省份
					</th>
					<th>
						最后登录时间
					</th>
					<th>
						最后登录IP
					</th>
					<th>
						操作
					</th>
				</tr>
				<c:forEach var="list" items="${loginUser_List}" varStatus="idx">
					<tr 
						<c:if test="${fn:contains('2', list.user_level )}">
							style="background: #FFAAD5;"
						</c:if>
						>
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
						${list.email}
					</td>
					<td>
						${list.user_phone}
					</td>
					<td>
						${user_Status[list.user_status]}
					</td>
					<td>
						${user_Types[list.user_level]}
					</td>
					<td>
						${list.supervise_name }
					</td>
					<td>
						${list.last_login_time }
					</td>
					<td>
						${list.last_login_ip }
					</td>
					
					<td>
					<c:if test="${list.user_IsOpen eq '0'}">
						<a
							href="javascript:submit('/login/queryUpdateUserInfo.do?user_id=${list.user_id }')">修改</a>
					</c:if>
					</td>
					</tr>
				</c:forEach>
				<%
					}
				%>
				</table>
			</div>
	</body>
</html>
