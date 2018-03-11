<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>去哪儿订单统计页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		
	<script type="text/javascript">

	function getYesterday(){
		//获取系统时间
		var LSTR_ndate = new Date();
		var LSTR_Year = LSTR_ndate.getYear();
		var LSTR_Month = LSTR_ndate.getMonth();
		var LSTR_Date = LSTR_ndate.getDate();
		//处理
		var uom = new Date(LSTR_Year, LSTR_Month, LSTR_Date);
		//获取系统时间的前一天（负数前几天，正数后几天）
		uom.setDate(uom.getDate()-1);
		var LINT_MM = uom.getMonth();
		LINT_MM ++;
		var LSTR_MM = LINT_MM > 10?	LINT_MM:("0" + LINT_MM)
		var LINT_DD = uom.getDate();
		var LSTR_DD = LINT_DD > 10? LINT_DD:("0" + LINT_DD)
		//得到最终结果
		yesterday = uom.getFullYear() + "-" + LSTR_MM + "-" + LSTR_DD;
		alert(yesterday);
		window.location.href="/jfree/showHourPicture.do?order_time=" + yesterday + "&channel=qunar"
	}
	

	</script>
	</head>

	<body >
		<div class="book_manage oz">
			<form action="/qunarstat/queryStat.do" method="post" name="myform"  id="myform">
				<ul class="order_num oz" style="margin-top: 10px;">
					<li>
						开始时间
						<input type="text" class="text" name="begin_time" readonly="readonly" value="${begin_time }"
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>
					<!-- <li><span>时间格式为: yyyy - MM - dd</span></li> -->
					<li>
						结束时间
						<input type="text" class="text" name="end_time" readonly="readonly" value="${end_time}"
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>
					
					<li>
					<!-- 	<a href="/qunarstat/showPictureLine.do">15日内销售统计图表</a> -->
						<a href="/jfree/showPictureLineQunar.do">15日内销售统计图表</a>
					</li>
					<li>
						<a href="javascript:getYesterday();">日期小时销售统计图表</a>
					</li>
				</ul>
				<br />
				<br />
				<p>
					<input type="submit" value="查 询" class="btn" />
				</p>

				<c:if test="${!empty isShowList}">
					<table id="table_list">
						<tr style="background: #f0f0f0;">
							<th>
								序号
							</th>
							<th>
								订单时间
							</th>
							<th>
								出票成功
							</th>
							<th>
								出票失败
							</th>

							<th>
								退款成功
							</th>
							<th>
								票数总计
							</th>
							<th>
								总订单
							</th>
							<th>
								成功金额
							</th>
							<th>
								失败金额
							</th>
							<th>
								成功率
							</th>
							<th>
								失败率
							</th>
							<th>
								转换率
							</th>
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${statList}" varStatus="idx">
							<tr>
								<td>
									${idx.index+1}
								</td>
								<td>
									${list.order_time }
								</td>
								<td>
									${list.out_ticket_succeed }
								</td>
								<td>
									${list.out_ticket_defeated }
								</td>

								<td>
									${list.refund_count }
								</td>
								<td>
									${list.ticket_count }
								</td>
								<td>
									${list.order_count }
								</td>
								<td>
									${list.succeed_money }
								</td>
								<td>
									${list.defeated_money }
								</td>
								<td>
									${list.succeed_cgl }
								</td>
								<td>
									${list.succeed_sbl }
								</td>
								<td>
									${list.succeed_odds }
								</td>

								<td>
									<a href="/jfree/showHourPicture.do?order_time=${list.order_time }&channel=qunar">图表</a>
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