<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>省级代理商显示页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script type="text/javascript">
		</script>
		<style>
td {
	padding: 5px 0;
}

.tit {
	background: #ddd;
}

.book_manage .ser_mingxi {
	width: 1000px;
	margin: 0 auto;
	border: none;
}

.book_manage .ser_mingxi tr,.book_manage .ser_mingxi td {
	border: none;
	line-height: 15px;
}

.book_manage .ser_mingxi span {
	color: #f00;
}

.book_manage td {
	line-height: 15px;
}
</style>
	</head>

	<body>
		<div class="book_manage oz">
			<form action="/orderStat/querySuperviseAreaList.do" method="post" name="myform"
				id="myform">
				<table>
					<tr>
						<td>
							<ul class="ser oz">
								<li>
									&nbsp; 省
									<select name="province_id" id="province_id"
										onchange="selectCity();" style="width: 160px;">
										<option value="-1">全部</option>
										<c:forEach items="${province }" var="p">
											<option value="${p.area_no }"
												<c:if test="${province_id eq p.area_no }">selected="selected"</c:if>>
												${p.area_name}
											</option>
										</c:forEach>
									</select>
								</li>
							</ul>
							<ul class="oz" style="margin-top: 14px;">
								<li>
									开始时间
									<input type="text" class="text" name="begin_time"
										readonly="readonly" value="${begin_time }"
										onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
								</li>
								<li>
									结束时间
									<input type="text" class="text" name="end_time"
										readonly="readonly" value="${end_time}"
										onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
								</li>
							</ul>
						</td>
					</tr>
				</table>
				<br />
				<br />
				<p>
					<input type="submit" value="查 询" class="btn" />
				</p>
				<c:if test="${!empty isShowList}">
					<table id="table_list">
						<tr class="tit" style="background: #f0f0f0;">
							<td>
								序号
							</td>
							<td>
								时间
							</td>
							<td>
								省
							</td>
							<td>
								总用户
							</td>
							<td>
								活跃用户
							</td>
							<td>
								申请数
							</td>
							<td>
								未通过数
							</td>
							<td>
								待审核数
							</td>
							<td>
								总订单数
							</td>
							<td>
								订单成功
							</td>
							<td>
								订单失败
							</td>
							<td>
								预下单
							</td>
							<td>
								支付失败
							</td>
							<td>
								票数
							</td>
							<td>
								成功率
							</td>
							<td>
								失败率
							</td>
							<td>
								转化率
							</td>
							<td>
								操作
							</td>
						</tr>
						<c:forEach var="list" items="${superviseAreaList}"
							varStatus="idx">
							<tr>
								<td>
									${idx.index+1}
								</td>
								<td>
									${list.order_time }
								</td>
								<td>
									${list.province_name }
								</td>
								<td>
									${list.user_count }
								</td>
								<td>
									${list.activeAgent }
								</td>
								<td>
									${list.apply_count }
								</td>
								<td>
									${list.not_pass }
								</td>
								<td>
									${list.wait_pass }
								</td>
								<td>
									${list.order_count }
								</td>
								<td>
									${list.succeed_count }
								</td>
								<td>
									${list.defeated_count }
								</td>
								<td>
									${list.want_outTicket }
								</td>
								<td>
									${list.pay_fall }
								</td>
								<td>
									${list.ticket_count }
								</td>
								<td>
									${list.cgl }
								</td>
								<td>
									${list.sbl }
								</td>
								<td>
									${list.zhl }
								</td>
								<td>
									<a
										href="/orderStat/showProvinceSellChart.do?province_id=${list.province_id }&create_time=${list.order_time }&area_no=${list.province_id }">本省销售图表</a>
								</td>
							</tr>
						</c:forEach>
					</table>
					<script type="text/javascript">
						var cobj=document.getElementById("table_list").rows;
   						for (i=1;i< cobj.length ;i++) {
     					(i%2==0)?(cobj[i].style.background = "#FFEEDD"):(cobj[i].style.background = "#FFFFFF");
   						 }
					</script>
					<jsp:include page="/pages/common/paging.jsp" />
				</c:if>
			</form>
		</div>
	</body>
</html>
