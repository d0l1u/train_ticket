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
		<title>车站退票手动导入明细</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
		<script type="text/javascript">
	<%
		PageVo pageObject = (PageVo) request.getAttribute("pageBean");
		int pageIndex = pageObject.getPageIndex();
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user_level = loginUserVo.getUser_level();
	%>
	
	function submitForm(){
		var refund_seq = $("#refund_seq").val();
		var begin_time = $("#begin_time").val();
		$("form:first").attr("action","/gtStation/queryGtStationInfo.do?refund_seq="+refund_seq+"&begin_time="+begin_time);
		$("form:first").submit();
		}

	function exportExcel() {
		var refund_seq = $("#refund_seq").val();
		var begin_time = $("#begin_time").val();
			$("form:first").attr("action","/gtStation/exportGtStationexcel.do?refund_seq="+refund_seq+"&begin_time="+begin_time);
			$("form:first").submit();
			$("form:first").attr("action","/gtStation/queryGtStationInfo.do");
		}
</script>
</head>
	<body onload="div();"><div></div>
		<div class="book_manage oz">
			<form action="/gtStation/queryGtStationInfo.do" method="post">
				<div style="border: 0px solid #00CC00;">
					<ul class="order_num oz" style="margin-top: 10px;">
						<li>
							订单号：&nbsp;&nbsp;&nbsp;
							<input type="text" class="text" name="order_id"
								value="${order_id }" />
						</li>
					</ul>
					<ul class="order_state oz">
					<li>
						&nbsp;&nbsp;&nbsp;订单类型：
					</li>
					<c:forEach items="${orderStatus}" var="d" varStatus="index">
						<li>
							<input type="checkbox" id="order_status${index.count }"
								name="order_status" value="${d.key }"
								<c:if test="${fn:contains(order_status,d.key) }">checked="checked"</c:if> />
							<label for="order_status${index.count }">
								${d.value }
							</label>
						</li>
					</c:forEach>
				</ul>
					
					
					<br/>
        <p><input type="button" value="查 询" class="btn" onclick="submitForm();"/>
        
		 <% if ("2".equals(loginUserVo.getUser_level()) ) {%>
		<input type="button" value="导出Excel" class="btn" onclick="exportExcel()" />
		<%} %>
		<input type="button" value="返 回" class="btn" onclick="javascript:window.location.href='/gtStation/queryGtStationPage.do';"/>
        </p>
        
				</div>
				<div id="hint" class="pub_con" style="display:none"></div>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #EAEAEA;">
							<th>
								NO
								<input type="hidden" id="refund_seq" name="refund_seq" value="${refund_seq }" />
								<input type="hidden" id="begin_time" name="begin_time" value="${begin_time }" />
							</th>
							<th>
								订单号
							</th>
							<th>
								车票号
							</th>
							<th>
								乘客姓名
							</th>
							<th width="65">
								发车时间
							</th>
							<th>
								票价
							</th>
							<th>
								车票数
							</th>
							<th width="65">
								创建时间
							</th>
							<th>
								状态
							</th>
							<th>
								操作人
							</th>
						</tr>
						<c:forEach var="list" items="${gtStationList}" varStatus="idx">
						<tr <c:if test="${list.order_status eq '22' }">style="background:#FFB5B5;" </c:if>>
							<td>
								${idx.index+1}
							</td>
							<td>
								${list.order_id}
							</td>
							<td>
								${list.cp_id}
							</td>
							<td>
								${list.user_name}
							</td>
							<td>
								${list.travel_time}
							</td>
							<td>
								${list.pay_money}
							</td>
							<td>
								${list.ticket_num }
							</td>
							<td>
								${fn:substringBefore(list.create_time, ' ')}
								<br />
								${fn:substringAfter(list.create_time, ' ')}
							</td>
							<td>
								${orderStatus[list.order_status] }
							</td>
							<td>
								${list.opt_person}
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
