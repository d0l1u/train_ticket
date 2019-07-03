<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>分时统计页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	    <script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
	    <script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
	    <script type="text/javascript">
	    
	    </script>
	    <style>
	    	.book_manage{width:960px;}
			.book_manage TABLE{width:940px;}
			table tbody tr td{height:30px; font-weight:bolder;}
			.th{width:40px;}
		</style>
	</head>
	<body>
		<div class="book_manage oz">
			<form action="/picture/queryFailCodeHour.do" method="post" name="myform" id="myform">
				<input type="hidden" id="begin_time" name="begin_time" value="${begin_time }" />
				<input type="hidden" id="end_time" name="end_time" value="${end_time }" />
				<!-- 
				<input type="hidden" id="department" name="department" value="${department }" />
				 -->
				<div style="border: 0px solid #00CC00; margin: 10px;">
				<c:if test="${!empty isShowList}">
					<table>
						<thead style="background: #EAEAEA;height: 50px;">
						<tr>
							<th style="width:110px;">打码失败数</th>
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
							<th class="th" style="width: 90px;">截止当前</th>
							<th class="th" style="width: 90px;">打码总计</th>
						</tr>
						</thead>
						<tbody>
						<c:forEach items="${codeList }" var="t" varStatus="index">
							<tr>
								<td style="border:1px solid #dadada;">${t.day_stat }</td>
								<c:choose>
									<c:when test="${t.hour07 ne 0}">
										<td class="th" style="color:#F37022;">${t.hour07}</td>
									</c:when>
									<c:otherwise>
										<td class="th" style="color:#EAEAEA;">${t.hour07}</td>
									</c:otherwise>
								</c:choose>
								
								<c:choose>
									<c:when test="${t.hour08 ne 0}">
										<td class="th" style="color:#F37022;">${t.hour08}</td>
									</c:when>
									<c:otherwise>
										<td class="th" style="color:#EAEAEA;">${t.hour08}</td>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${t.hour09 ne 0}">
										<td class="th" style="color:#F37022;">${t.hour09}</td>
									</c:when>
									<c:otherwise>
										<td class="th" style="color:#EAEAEA;">${t.hour09}</td>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${t.hour10 ne 0}">
										<td class="th" style="color:#F37022;">${t.hour10}</td>
									</c:when>
									<c:otherwise>
										<td class="th" style="color:#EAEAEA;">${t.hour10}</td>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${t.hour11 ne 0}">
										<td class="th" style="color:#F37022;">${t.hour11}</td>
									</c:when>
									<c:otherwise>
										<td class="th" style="color:#EAEAEA;">${t.hour11}</td>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${t.hour12 ne 0}">
										<td class="th" style="color:#F37022;">${t.hour12}</td>
									</c:when>
									<c:otherwise>
										<td class="th" style="color:#EAEAEA;">${t.hour12}</td>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${t.hour13 ne 0}">
										<td class="th" style="color:#F37022;">${t.hour13}</td>
									</c:when>
									<c:otherwise>
										<td class="th" style="color:#EAEAEA;">${t.hour13}</td>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${t.hour14 ne 0}">
										<td class="th" style="color:#F37022;">${t.hour14}</td>
									</c:when>
									<c:otherwise>
										<td class="th" style="color:#EAEAEA;">${t.hour14}</td>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${t.hour15 ne 0}">
										<td class="th" style="color:#F37022;">${t.hour15}</td>
									</c:when>
									<c:otherwise>
										<td class="th" style="color:#EAEAEA;">${t.hour15}</td>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${t.hour16 ne 0}">
										<td class="th" style="color:#F37022;">${t.hour16}</td>
									</c:when>
									<c:otherwise>
										<td class="th" style="color:#EAEAEA;">${t.hour16}</td>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${t.hour17 ne 0}">
										<td class="th" style="color:#F37022;">${t.hour17}</td>
									</c:when>
									<c:otherwise>
										<td class="th" style="color:#EAEAEA;">${t.hour17}</td>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${t.hour18 ne 0}">
										<td class="th" style="color:#F37022;">${t.hour18}</td>
									</c:when>
									<c:otherwise>
										<td class="th" style="color:#EAEAEA;">${t.hour18}</td>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${t.hour19 ne 0}">
										<td class="th" style="color:#F37022;">${t.hour19}</td>
									</c:when>
									<c:otherwise>
										<td class="th" style="color:#EAEAEA;">${t.hour19}</td>
									</c:otherwise>
								</c:choose>
								
								<c:choose>
									<c:when test="${t.hour20 ne 0}">
										<td class="th" style="color:#F37022;">${t.hour20}</td>
									</c:when>
									<c:otherwise>
										<td class="th" style="color:#EAEAEA;">${t.hour20}</td>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${t.hour21 ne 0}">
										<td class="th" style="color:#F37022;">${t.hour21}</td>
									</c:when>
									<c:otherwise>
										<td class="th" style="color:#EAEAEA;">${t.hour21}</td>
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${t.hour22 ne 0}">
										<td class="th" style="color:#F37022;">${t.hour22}</td>
									</c:when>
									<c:otherwise>
										<td class="th" style="color:#EAEAEA;">${t.hour22}</td>
									</c:otherwise>
								</c:choose>
								<td style="border:1px solid #dadada;">${t.codeNowCount }</td>
								<td style="border:1px solid #dadada;">${t.code_count }</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
					<jsp:include page="/pages/common/paging.jsp" />
					<br/>
					<input type="button" value="返回" class="btn btn_normal" style="font-size:12px;" onclick="backPage();" />
					
				</c:if>
				</div>
			</form>
		</div>
	</body>
</html>