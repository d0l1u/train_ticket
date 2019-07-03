<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>操作人操作效率统计</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
		<script type="text/javascript" src="/js/jquery.js"></script>
	<script type="text/javascript">
	function submit(url){
		if(confirm("是否提交？")){
			location.href = url; 
		}
	}
	function exportExcelOpter(){
		$("form:first").attr("action","/orderForExcel/excelexportOpter.do");
		$("form:first").submit();
		$("form:first").attr("action","/opterStat/queryOpterStatList.do");
	}
	</script>
	</head>
	<body><div style="margin-top: 20px;"></div>
		<div class="book_manage oz">
			<form action="/opterStat/queryOpterStatList.do" method="post">
				<ul class="order_num oz">
					<li>
						真实姓名：
						<input type="text" class="text" name="real_name" value="${real_name }"/>
					</li>
					<li>
						开始时间：
						<input type="text" class="text" name="begin_time" readonly="readonly" value="${begin_time }"
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})"/> 
					</li>
					<li>
						结束时间：
						<input type="text" class="text" name="end_time" readonly="readonly" value="${end_time}"
						onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" /> 
					</li>
				</ul>
				<br />
				<p>
					<input type="submit" value="查 询" class="btn" />
					&nbsp;&nbsp;&nbsp;
					<input type="button" value="导出Excel" class="btn" onclick="exportExcelOpter()"/>
				</p>
				<c:if test="${!empty isShowList}">
					<table id="table_list">
						<tr style="background: #EAEAEA;">
							<th>
								序号
							</th>
							<th>
								统计人员
							</th>
							<th>
								统计日期
							</th>
							<th>
								订单数
							</th>
							<th>
								退款数
							</th>
							<th>
								拒绝退票数
							</th>
							<th>
								搁置订单数
							</th>
							<!-- 
							<th>
								19e退款
							</th>
							<th>
								去哪退款
							</th>
							<th>
								艺龙退款
							</th>
							<th>
								同程退款
							</th>
							<th>
								美团退款
							</th>
							<th>
								内嵌退款
							</th>
							<th>
								B2C退款
							</th>
							<th>
								商户退款
							</th>
							<th>
								出票失败退款
							</th>
							<th>
								差额退款
							</th>
							<th>
								审核退款
							</th> -->
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${opter_List}" varStatus="idx">
							<tr>
								<td>
									${idx.index+1}
								</td>
								<td>
									${list.opt_person}
								</td>
								<td>
									${list.tj_time}
								</td>
								<td>
									${list.out_ticket_total}
								</td>
								<td>
									${list.refund_total}
								</td><!-- 
								<td>
									${list.refund_total_19e }
								</td>
								<td>
									${list.refund_total_qunar}
								</td>
								<td>
									${list.refund_total_elong}
								</td>
								<td>
									${list.refund_total_tongcheng}
								</td>
								<td>
									${list.refund_total_meituan}
								</td>
								<td>
									${list.refund_total_inner}
								</td>
								<td>
									${list.refund_total_app}
								</td>
								<td>
									${list.refund_total_ext }
								</td>
								<td>
									${list.refund_total_failure}
								</td>
								<td>
									${list.refund_total_differ }
								</td>
								<td>
									${list.refund_total_verify }
								</td>-->
								<td>
									${list.refund_total_refuse }
								</td>
								<td>
									${list.refund_total_holdon }
								</td>
								<td>
									<a href="/opterStat/queryOpterInfo.do?tj_id=${list.tj_id}">明细</a>
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
