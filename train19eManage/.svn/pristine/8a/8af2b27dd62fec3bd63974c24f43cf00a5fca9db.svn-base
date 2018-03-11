<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>订单统计页面CP</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	    <script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
  	</head>
  
  <body>
    	<div class="book_manage oz">
			<form action="/cpStat/queryCpStatList.do" method="post" name="myform" id="myform">
				<table style="border:none;">
					<tr style="border:none;">
						<td style="border:none;">
						<ul class="oz" style="margin-top: 14px;">
								<li>
									开始时间
									<input type="text" class="text" name="begin_time" readonly="readonly" value="${begin_time }"
										onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})"/> 
								</li>
								<li>
									结束时间
									<input type="text" class="text" name="end_time" readonly="readonly" value="${end_time}"
									onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" /> 
								</li>
								<li>
									<a href="/cpStat/showPictureCpLine.do">15日内销售统计图表</a>
								</li>
							</ul>
							<ul class="ser oz">
					        	<li>
					        		渠道: 
					        	</li>
					        	<li>
					        		<c:forEach items="${channel_Type }" var="s" varStatus="index">
						        		<li><input type="checkbox" id="channel${index.count }" 
						        		name="channel" value="${s.key }" 
						        		<c:if test="${fn:contains(channelStr, s.key ) }">checked="checked"</c:if>/>
						        		<label for="channel${index.count }">${s.value }</label></li>
						        	</c:forEach>
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
								渠道
							</th>
							<th>
								订单成功
							</th>
							<th>
								订单失败
							</th>
							<th>
								总订单
							</th>
							<th>
								票数总计
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
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${cpStatListCount}" varStatus="idx">
							<tr>
								<td>
									${idx.index+1}
								</td>
								<td>
									${list.order_time }
								</td>
								<td>
									${list.channel }
								</td>
								<td>
									${list.order_succeed }
								</td>
								<td>
									${list.order_defeated }
								</td>
								<td>
									${list.order_count }
								</td>
								<td>
									${list.ticket_count }
								</td>
								<td>
									${list.succeed_money }
								</td>
								<td>
									${list.defeated_money }
								</td>
								<td>
									${list.cgl }
								</td>
								<td>
									${list.sbl }
								</td>
								<td>
								
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
