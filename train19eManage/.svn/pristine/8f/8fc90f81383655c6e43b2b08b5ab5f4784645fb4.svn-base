<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<%@ page import="com.l9e.transaction.vo.PageVo"%>
<jsp:directive.page import="java.text.NumberFormat" />
<jsp:directive.page import="java.text.DecimalFormat" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>手动导入车站退票页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		 <script type="text/javascript" src="/js/jquery.cookie.js"></script>
		 	<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
		<script type="text/javascript">
		<%
			PageVo pageObject = (PageVo) request.getAttribute("pageBean");
			int pageIndex = pageObject.getPageIndex();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String user_level = loginUserVo.getUser_level();
		%>
		
		function mingxi(refund_seq,create_time){
			$("form:first").attr("action","/elongStation/queryElongStationInfo.do?refund_seq="+refund_seq+"&order_status=11&begin_time="+create_time);
			$("form:first").submit();
		}
		
		function addRefunds(){
		$("form:first").attr("action","/elongStation/elongAddRefundsPage.do");
		$("form:first").submit();
		}
					
</script>
<style>
</style>
	</head>
	<body><div></div>
		<div class="book_manage oz">
			<form action="/elongStation/queryElongStationList.do" method="post" name="myform" id="myform">

				<ul class="order_num oz" style="margin-top: 10px;">
					<li>
						开始时间：&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="begin_info_time"
							readonly="readonly" value="${begin_info_time }"
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>
					<li>
						结束时间：&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="end_info_time"
							readonly="readonly" value="${end_info_time }"
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>
					
				</ul>
				
				<p>
					<input type="submit" value="查 询" class="btn" style="margin-top: 10px;"/>
					<%
						if ("2".equals(loginUserVo.getUser_level()) ) {
           			%>
           			<input type="button" value="手动上传" class="btn" onclick="addRefunds()"/>  
					<%} %>
				</p>
				<div id="hint" class="pub_con" style="display:none"></div>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #EAEAEA;">
							<th style="10px;">
								NO
							</th>
							<th width="70px">
								日期
							</th>
							<th>
								上传总数
							</th>
							<th >
								导入总数
							</th>
							<th >
								导入成功
							</th>
							<th>
								重复导入
							</th>
							<th >
								操作人
							</th>
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${elongStationList}" varStatus="idx">
						<tr <c:if test="${fn:contains('1,3,5,7,9,11,13,15,17,19',idx.count)}">style="background: #BEE0FC;"</c:if> >
							<td>
								${idx.count }
							</td>
							<td>
								${list.create_time2}
							</td>
							<td>
								${list.count }
							</td>
							<td>
								${list.success_num+list.again_num }
							</td>
							<td>
								${list.success_num }
							</td>
							<td>
								${list.again_num }
							</td>
							<td>
								${list.opt_person}
							</td>
							<td>
								<a href="javascript:mingxi('${list.refund_seq }','${list.create_time2 }')">明细</a>
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
