<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>订单统计页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	    <script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
	    

  	</head>
  
  <body>
    	<div class="book_manage oz">
			<form action="/orderStat/queryOrderStatList.do" method="post" name="myform" id="myform">
				<table style="border:none;">
					<tr style="border:none;">
						<td style="border:none;">
						<ul class="oz" style="margin-top: 14px;">
								<li>
									开始时间
									 
									<input type="text" class="text" name="begin_time" readonly="readonly" value="${begin_time }"
										onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})"/> 
								</li>
								<!-- <li><span>时间格式为: yyyy - MM - dd</span></li> -->
								<li>
									结束时间
									<input type="text" class="text" name="end_time" readonly="readonly" value="${end_time}"
									onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" /> 
								</li>
								<li>
									<a href="/orderStat/showPictureLine.do">15日内销售统计图表</a>
								</li>
								<li>
									<a href="/orderStat/showDayHourDetail.do">日期小时销售统计图表</a>
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
						<tr style="background: #f0f0f0;">
							<th>
								序号
							</th>
							<th>
								订单时间
							</th>
							<th>
								活跃用户数
							</th>
							<th>
								出票成功
							</th>
							<th>
								出票失败
							</th>
							<th>
								预下单
							</th>
							<th>
								支付失败
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
								总保险
							</th>
							<th>
								总保险金额
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
						<c:forEach var="list" items="${orderStatList}" varStatus="idx">
						<tr>
							<td>
								${idx.index+1}
							</td>
							<td>
								${list.order_time }
							</td>
							<td>
								${list.active }
							</td>
							<td>
								${list.out_ticket_succeed }
							</td>
							<td>
								${list.out_ticket_defeated }
							</td>
							<td>
								${list.preparative_count }
							</td>
							<td>
								${list.pay_defeated }
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
								${list.bx_count }
							</td>
							<td>
								${list.bx_countMoney }
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
							<a href="/orderStat/showHourPicture.do?order_time=${list.order_time }">图表</a>
							<a href="/orderStat/queryThisDayInfo.do?create_time=${list.order_time }">详细</a>
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
