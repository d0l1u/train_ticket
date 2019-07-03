<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>各省次日销售统计</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
		<script type="text/javascript">
			function exportExcelProvinceStat(create_time) {
				window.location.href="/orderForExcel/excelexportProvinceStat.do?create_time="+create_time;
				//window.location.href="/hcStat/queryThisDayInfo.do?create_time="+create_time;
			}
		</script>
	</head>
	<body>
			<div class="book_manage oz">
				<table class="pub_table" style="margin-top: 20px;" id="table_list">
					<tr class="tit" style="background: #f0f0f0;">
						<td>
							序号
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
						<!-- <td>
							申请数
						</td>
						<td>
							未通过数
						</td>
						<td>
							待审核数
						</td> -->
						<td>
							当月订单数
						</td>
						<td>
							上月总订单数
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
							总票数
						</td>
						<td>
							总金额
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
					<c:forEach var="list" items="${statInfo_provinceList}" varStatus="idx">
						<tr>
							<td>
								${idx.index+1}
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
							<!-- <td>
								${list.apply_count }
							</td>
							<td>
								${list.not_pass }
							</td>
							<td>
								${list.wait_pass }
							</td> -->
							<td>
								${list.mouth_count }
							</td>
							<td>
								${list.last_mouth_count }
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
								${list.total_Ticket }
							</td>
							<td>
								${list.total_Money }
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
								<a href="/jfree/showProvinceSellChart.do?province_id=${list.province_id }&create_time=${list.order_time }&area_no=${list.province_id }">本省分时段销售图表</a> 
								
								<a href="/jfree/query15DaysActive.do?province_id=${list.province_id }&create_time=${list.order_time }">15日本省销售统计图表</a>
							</td>
						</tr>
					</c:forEach>
				</table>
			</div>
				<script type="text/javascript">
						var cobj=document.getElementById("table_list").rows;
   						for (i=1;i< cobj.length ;i++) {
     					(i%2==0)?(cobj[i].style.background = "#FFEEDD"):(cobj[i].style.background = "#FFFFFF");
   						 }
				</script>
		<div class="pub_debook_mes  oz mb10_all">
			<p>
				<input type="button" value="导出Excel" class="btn btn_normal" style="font-size:12px;" onclick="exportExcelProvinceStat('${create_time}')"/>
				<input type="button" value="返 回" class="btn btn_normal" style="font-size:12px;" onclick="javascript:history.back(-1);" />
			</p>
		</div>
	</body>
</html>