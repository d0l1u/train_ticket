<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>账号管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<!-- <script language="javascript" src="/js/datepicker/WdatePicker.js"></script>-->
		<script type="text/javascript">

	function submit(url){
	
		if(confirm("是否提交？")){
			location.href = url; 
		}
	}
	function update(url){
			location.href = url; 
	}

</script>
		<style>
tr:hover {
	background: #ecffff;
}
</style>
	</head>

	<body>
		<div class="book_manage oz">
			<form action="/notice/queryNoticeList.do" method="post" name="updateForm" id="updateForm">
				<br />
				<p></p>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #EAEAEA;">
							<th>
								序号
							</th>
							<th>
								标题
							</th>
							<th>
								创建时间
							</th>
							<th>
								生效时间
							</th>
							<th>
								失效时间
							</th>
							<!--  <th>发布区域</th>  -->
							<th>
								状态
							</th>
							<th>
								创建人
							</th>
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${noticeList}" varStatus="idx">
							<tr>
								<td>
									${idx.index+1}
								</td>
								<td>
									${list.notice_name}
								</td>
								<td>
									${list.create_time}
								</td>
								<td>
									${list.pub_time}
								</td>
								<td>
									${list.stop_time}
								</td>
								<!-- <td>${list.provinces}</td> -->
								<td>
									${noticeStatusMap[list.notice_status]}
								</td>
								<td>
									${list.opt_ren}
								</td>
								<td>
									<span> 
									<%LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
										if ("2".equals(loginUserVo.getUser_level())){ %>
									<a href="javascript:submit('/notice/deleteNotice.do?notice_id=${list.notice_id }')">删除</a>
									<a href="javascript:update('/notice/updatePreNotice.do?notice_id=${list.notice_id }')">修改</a>
									<a href="javascript:submit('/notice/updateNotice.do?notice_status=22&notice_id=${list.notice_id }')">停用</a>
									<% } %>
									<a href="/notice/queryNotice.do?notice_id=${list.notice_id }">查看</a>
									</span>
								</td>
							</tr>
						</c:forEach>
					</table>
					<jsp:include page="/pages/common/paging.jsp" />
				</c:if>
				<br />
				<p>
				<%  LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
					if ("2".equals(loginUserVo.getUser_level())){ %>
					<input type="button" value="添加" onclick="location.href = '/notice/addPreNotice.do'" class="btn" />
				<% } %>
				</p>
			</form>	
		</div>
	</body>
</html>
