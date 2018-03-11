<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<%@ page import="com.l9e.transaction.vo.PageVo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>竞价页面</title>
	<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
	<script type="text/javascript" src="/js/jquery.js"></script>
	<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
	<script language="javascript" src="/js/layer/layer.js"></script>
	<script language="javascript" src="/js/mylayer.js"></script>
	<script type="text/javascript">
	 function addCompete(){
	 		$("form:first").attr("action","/compete/addCompetePage.do");
			$("form:first").submit();
	 }
	 function exportExcel(){
	 		$("form:first").attr("action","/compete/exportExcel.do");
			$("form:first").submit();
			$("form:first").attr("action","/compete/querycompeteList.do");
	 }
	 function addhistory(num){
	 		$("form:first").attr("action","/compete/addhistory.do");
			$("form:first").submit();
	 }
	 function historyPage(){
	 		var url="/compete/queryHistory.do";
			showlayer('操作日志:',url,'1000px','700px')
	 }
	 function todayCompete(){
	 		var url="/compete/todayCompete.do";
			showlayer('当天竞价:',url,'1150px','800px')
	 }
	 
	</script>
	<style>
	span{color:#f50;}
	</style>
	</head>
	<body><div></div>
		<div class="book_manage oz">
			<form action="/compete/queryCompeteList.do" method="post">
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
					<li>
							操作人：
							<input type="text" class="text" name="opt_ren" value="${opt_ren }" />
					</li>
				</ul>
				<ul class="order_state oz">
					<li>
						&nbsp;&nbsp;&nbsp;渠道：
					</li>
					<c:forEach items="${competeChannel}" var="d" varStatus="index">
						<li>
							<input type="checkbox" id="compete_channel${index.count }"
								name="compete_channel" value="${d.key }"
								<c:if test="${fn:contains(compete_channel,d.key) }">checked="checked"</c:if> />
							<label for="compete_channel${index.count }">
								${d.value }
							</label>
						</li>
					</c:forEach>
				</ul>
				<table style="width:900px;margin-left: 30px;margin-bottom: 10px;cellspacing=1;">
				<tr>
					<td>当前竞价:</td>
					<c:forEach var="h" items="${now_compete}" varStatus="idx" begin="0" end="0">
					<td>【19旅行:CDG<span>&nbsp;${h.compete_money_1 }&nbsp;&nbsp;</span>
					非CDG<span>&nbsp;${h.compete_money_un_1 }</span>&nbsp;】</td>
					<td>【九九商旅:CDG<span>&nbsp;${h.compete_money_2 }&nbsp;&nbsp;</span>
					非CDG<span>&nbsp;${h.compete_money_un_2 }</span>&nbsp;】</td>
					<td>操作人:</td><td>${h.opt_ren }</td>
					<td>操作时间:</td><td>${h.create_time }</td>
					</c:forEach>
				</tr>
				<tr>
					<td>下一小时竞价:</td>
					<c:forEach var="h" items="${history}" varStatus="idx" begin="0" end="0">
					<td>【19旅行:CDG&nbsp;<input type="text" name="next_compete_money_1" id="next_compete_money_1" value="${h.compete_money_1 }" style="width: 30px;color: #f50;" onchange="addhistory(1)"/>
					非CDG&nbsp;<input type="text" name="next_compete_money_un_1" id="next_compete_money_un_1" value="${h.compete_money_un_1 }" style="width: 30px;color: #f50;"onchange="addhistory(2)"/>】</td>
					<td>【九九商旅:CDG&nbsp;<input type="text" name="next_compete_money_2" id="next_compete_money_2" value="${h.compete_money_2 }" style="width: 30px;color: #f50;"onchange="addhistory(3)"/>
					非CDG&nbsp;<input type="text" name="next_compete_money_un_2" id="next_compete_money_un_2" value="${h.compete_money_un_2 }" style="width: 31px;color: #f50;"onchange="addhistory(4)"/>】</td>
					
					<td>操作人:</td><td>${h.opt_person }</td>
					<td>操作时间:</td><td>${h.create_time }</td>
					</c:forEach>
				</tr>
				</table>
				<p>
					<input type="submit" value="查 询" class="btn" />
					<input type="button" value="新增竞价" class="btn" onclick="addCompete()" />
					<input type="button" value="导出Excel" class="btn" onclick="exportExcel()" />
					<input type="button" value="操作日志" class="btn" onclick="historyPage()" />
					<input type="button" value="当天竞价" class="btn" onclick="todayCompete()" />
				</p>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #EAEAEA;">
							<th>
								序号
							</th>
							<th width="80px">
								竞价日期
							</th>
							<th width="80px">
								竞价时段
							</th>
							<th>
								渠道
							</th>
							<th>
								产品
							</th>
							<th>
								CDG竞价
							</th>
							<th>
								非CDG竞价
							</th>
							<th>
								CDG排名
							</th>
							<th>
								非CDG排名
							</th>
							<th>
								票数
							</th>
							<th>
								产品
							</th>
							<th>
								CDG竞价
							</th>
							<th>
								非CDG竞价
							</th>
							<th>
								CDG排名
							</th>
							<th>
								非CDG排名
							</th>
							<th>
								票数
							</th>
							<th width="80px">
								创建时间
							</th>
							<th>
								操作人
							</th>
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${competeList}" varStatus="idx">
						<tr
								<c:if test="${fn:contains('07:00-07:59,09:00-09:59,11:00-11:59,13:00-13:59,15:00-15:59,17:00-17:59,19:00-19:59,21:00-21:59', list.compete_time )}">
									 style="background: #BEE0FC;"
								</c:if>
							>
							<td>
								${idx.index+1}
							</td>
							<td width="80px">
								${list.compete_date}
							</td>
							<td width="80px">
								${list.compete_time}
							</td>
							<td>
								${competeChannel[list.compete_channel]}
							</td>
							<td>
								${competeGoods[list.compete_goods_1] }
							</td>
							<td>
								${list.compete_money_1}
							</td>
							<td>
								${list.compete_money_un_1}
							</td>
							<td>
								${list.compete_ranking_1}
							</td>
							<td>
								${list.compete_ranking_un_1}
							</td>
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_1 }
							</td>
							<td>
								${competeGoods[list.compete_goods_2] }
							</td>
							<td>
								${list.compete_money_2}
							</td>
							<td>
								${list.compete_money_un_2}
							</td>
							<td>
								${list.compete_ranking_2}
							</td>
							<td>
								${list.compete_ranking_un_2}
							</td>
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_2 }
							</td>
							<td width="80px">
								${list.create_time}
							</td>
							<td>
								${list.opt_ren}
							</td>
							<td>
								<span>
								<a href="/compete/updateCompetePage.do?compete_id=${list.compete_id}">明细</a>
								</span>
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
