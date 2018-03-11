<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>同程上传对账表格页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script type="text/javascript" src="jquery-1.3.2.js"></script>
		<script type="text/javascript">
		<%
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user_level = loginUserVo.getUser_level();
		%>
	
	function toAddCheck(){
			$("form:first").attr("action","/elongExcel/tcAddCheckAdds.do");
			$("form:first").submit();
		}
	$().ready(function(){
		var value = document.getElementById("alert").value;
		if(value =='1' ){
			alert("勿重复导入表格，请检查表格是否已经插入！");
		}
	});
	
</script>
<style>		
	.liancheng {color: red;}
	tr:hover {background: #ecffff;}
	#hint{width:700px;border:1px solid #C1C1C1;background:#F0FAC1;position:fixed;z-index:9;padding:6px;line-height:17px;text-align:left;overflow:auto;} 
</style>
	</head>
	<body><div></div>
		<div class="book_manage oz">
			<form action="/elongExcel/tcAddCheckList.do" method="post">
				<ul class="order_num oz" style="margin-top: 10px;">
					<li>
						订单号：&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="order_id"  value="${order_id}" />
						<input type="hidden" name="alert" id="alert" value="${alert}" />
					</li>
					<li>
						开始时间：&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="begin_info_time"
							readonly="readonly" value="${begin_info_time }"
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>
					<li>
						结束时间：&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="end_info_time"
							readonly="readonly" value="${end_info_time }"
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>

				</ul>
				<ul class="order_state oz">
					<li>
						&nbsp;&nbsp;&nbsp;通知状态：
					</li>
					<c:forEach items="${notifyStatus}" var="d" varStatus="index">
						<li>
							<input type="checkbox" id="notify_status${index.count }"
								name="notify_status" value="${d.key }"
								<c:if test="${fn:contains(notifyStr,d.key) }">checked="checked"</c:if> />
							<label for="notify_status${index.count }">
								${d.value }
							</label>
						</li>
					</c:forEach>
				</ul>
				<p>
					<input type="submit" value="查 询" class="btn" />
					<input type="button" value="批量导入" class="btn"  onclick="javascript:toAddCheck();"/>
				</p>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #EAEAEA;">
							<th>
								序号
							</th>
							<th>
								订单号
							</th>
							<th>
								12306订单号
							</th>
							<th>
								结算类型
							</th>
							<th>
								结算金额
							</th>
							<th>
								通知状态
							</th>
							<th>
								张数
							</th>
							<th>
								交易时间
							</th>
							<th>
								结算日期
							</th>
							<th>
								余额
							</th>
							<th>
								创建时间
							</th>
							<th>
								操作人
							</th>
							
						</tr>
						<c:forEach var="list" items="${checkList}" varStatus="idx">
							<tr
								<c:if test="${list.notify_status eq '33'}">
									style="background: pink;"
								</c:if>
							>		
							<td>
								${idx.index+1}
							</td>
							<td>
								${list.order_id}
							</td>
							<td>
								${list.out_ticket_billno}
							</td>
							<td>
								${checkType[list.settlement_type] }
							</td>
							<td>
								${list.amount}
							</td>
							<td>
								${notifyStatus[list.notify_status] }
							</td>
							<td>
								${list.quantity}
							</td>
							<td>
								${fn:substringBefore(list.trade_date, ' ')}
								<br />
								${fn:substringAfter(list.trade_date, ' ')}
							</td>
							<td>
								${list.settlement_date}
							</td>
							<td>
								${list.account_balance}
							</td>
							<td>
								${fn:substringBefore(list.create_time, ' ')}
								<br />
								${fn:substringAfter(list.create_time, ' ')}
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
