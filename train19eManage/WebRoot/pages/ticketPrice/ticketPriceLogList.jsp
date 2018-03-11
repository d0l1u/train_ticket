<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>票价管理操作日志页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	    <script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
	    <script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
	    <script type="text/javascript">
	    
	    </script>
	    <style>
	    	.book_manage{width:880px;}
			.book_manage TABLE{width:860px;}
		</style>
	</head>
	<body>
		<div class="book_manage oz">
			<form action="/ticketPrice/queryTicketPriceLogList.do" method="post" name="myform" id="myform">
				<div style="border: 0px solid #00CC00; margin: 10px;">
				<c:if test="${!empty isShowList}">
					<table style="table-layout:fixed">
						<tr style="background: #f0f0f0;">
							<th style="width:40px;">
								序号
							</th>
							<th style="width:70px;">
								时间
							</th>
							<th style="width:60px;">
								操作人
							</th>
							<th style="width:70px;">
								车次
							</th>
							<th style="width:70px;">
								发站
							</th>
							<th style="width:70px;">
								到站
							</th>
							<th style="width:40px;">
								操作
							</th>
							<th style="width:220px;">
								操作前数据
							</th>
							<th style="width:220px;">
								操作后数据
							</th>
						</tr>
						
						<c:forEach var="list" items="${logList}" varStatus="idx">
						<tr style="background:#E0F3ED; height:90px">
							<td>
								${idx.index+1}
							</td>
							<td>
								${fn:substringBefore(list.create_time, ' ')}
								<br />
								${fn:substringAfter(list.create_time, ' ')}
							</td>	
							<td>
								${list.opt_name }
							</td>
							<td>
								${list.cc }
							</td>
							<td>
								${list.fz }
							</td>
							<td>
								${list.dz }
							</td>
							<td>
								<c:choose>
									<c:when test="${list.opt_type=='insert'}">
										添加
									</c:when>
									<c:when test="${list.opt_type=='update'}">
										修改
									</c:when>
									<c:otherwise>
										其他
									</c:otherwise>
								</c:choose>
							</td>
							<td align="left" style="word-break:break-all;">
								${list.before_data }
							</td>
							<td align="left" style="word-break:break-all;">
								${list.after_data }
							</td>
						</tr>
						</c:forEach>
					</table>
					<jsp:include page="/pages/common/paging.jsp" />
					<br/>
					<p>
						<input type="button" value="返回" class="btn" onclick="backPage();" />
					</p>
					
				</c:if>
				</div>
			</form>
		</div>
	</body>
</html>