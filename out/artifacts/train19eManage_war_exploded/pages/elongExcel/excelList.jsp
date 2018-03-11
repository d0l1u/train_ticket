<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>下载表格页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script type="text/javascript" src="jquery-1.3.2.js"></script>
		<script type="text/javascript">
		<%
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user_level = loginUserVo.getUser_level();
	%>
	
	//得到当前日期，时间格式为"yyyy-MM-dd"
	function TodayFormat(){
		var date = new Date();
		var year = date.getFullYear();
		var month = date.getMonth()+1; //js从0开始取 
		month = month<10 ? "0"+month : month;
		var date1 = date.getDate()-1; 
		date1 = date1<10 ? "0"+date1 : date1;
		var now = year+"-"+month+"-"+date1;
		//alert(date+" | "+now );
		return now;
	}
	
		function gotoDelete(excel_id,excel_url,excel_name) {
		var yestoday = TodayFormat();
		if(yestoday == excel_name){
		alert("不可以删除前一天表格！");
		return;
		}
			if(confirm("你确认删除吗？")){
			 var url = "/elongExcel/deleteExcelById.do?excel_id="+excel_id+"&excel_url="+excel_url+"&version="+new Date();
				 $.get(url,function(data){
					if(data == "true"){
					$("form:first").submit();
					}
				});
			}
		}		

	function gotoUpload(excel_url) {
		$("form:first").attr("action","/elongExcel/uploadExcel.do?excel_url="+excel_url);
		$("form:first").submit();
		$("form:first").attr("action","/elongExcel/queryExcelList.do");
		}
</script>
<style>		
	.liancheng {color: red;}
	tr:hover {background: #ecffff;}
	#hint{width:700px;border:1px solid #C1C1C1;background:#F0FAC1;position:fixed;z-index:9;padding:6px;line-height:17px;text-align:left;overflow:auto;} 
</style>
	</head>
	<body><div></div>
		<div class="book_manage oz">
			<form action="/elongExcel/queryExcelList.do" method="post">
				<ul class="order_num oz" style="margin-top: 10px;">
					<li>
						开始时间：&nbsp;&nbsp;&nbsp;
						<!-- <input type="text" class="text" name="begin_info_time" value="${begin_info_time }"/> -->
						<input type="text" class="text" name="begin_info_time"
							readonly="readonly" value="${begin_info_time }"
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>
					<li>
						结束时间：&nbsp;&nbsp;&nbsp;&nbsp;
						<!-- <input type="text" class="text" name="end_info_time" value="${end_info_time }"/> -->
						<input type="text" class="text" name="end_info_time"
							readonly="readonly" value="${end_info_time }"
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>

				</ul>
				<ul class="order_state oz">
					<li>
						&nbsp;&nbsp;&nbsp;渠&nbsp;&nbsp;&nbsp;&nbsp;道：
					</li>
					<c:forEach items="${Channel}" var="d" varStatus="index">
						<li>
							<input type="checkbox" id="channel${index.count }"
								name="channel" value="${d.key }"
								<c:if test="${fn:contains(channelStr,d.key) }">checked="checked"</c:if> />
							<label for="channel${index.count }">
								${d.value }
							</label>
						</li>
					</c:forEach>
				</ul>
				<p>
					<input type="submit" value="查 询" class="btn" />
				</p>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #EAEAEA;">
							<th>
								序号
							</th>
							<th>
								日期
							</th>
							<th>
								渠道
							</th>
							<th>
								类型
							</th>
							<th>
								创建时间
							</th>
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${excelList}" varStatus="idx">
							<tr
								<c:if test="${list.excel_channel eq 'elong'}">
									style="background: #e0f3ed;"
								</c:if>
								<c:if test="${list.excel_channel eq 'tongcheng'}">
									style="background: #BEE0FC;"
								</c:if>
								<c:if test="${list.excel_channel eq 'check'}">
									style="background: pink;"
								</c:if>
								<c:if test="${list.excel_channel eq 'allrefund'}">
									style="background: #A0E225;"
								</c:if>
							>		
							<td>
								${idx.index+1}
							</td>
							<th style="color:red;">
								${list.excel_name}
							</th>
							<td>
							<c:if test="${list.excel_channel eq 'check' || list.excel_channel eq 'allrefund'}">对账表格</c:if>
							<c:if test="${list.excel_channel eq '301030'}">高铁管家</c:if>	
							<c:if test="${list.excel_channel eq '301031'}">携程</c:if>	
							<c:if test="${list.excel_channel eq 'meituan'}">美团</c:if>	
							<c:if test="${list.excel_channel eq 'tuniu'}">途牛</c:if>	
								${Channel[list.excel_channel] }
							</td>
							<td>
							<c:if test="${list.excel_type eq '11'}">艺龙预订</c:if>
							<c:if test="${list.excel_type eq '22'}">艺龙退款</c:if>	
							<c:if test="${list.excel_type eq '33'}">同程对账</c:if>	
							<c:if test="${list.excel_type eq '44'}">出票对账</c:if>	
							<c:if test="${list.excel_type eq '55'}">退票对账</c:if>	
							<c:if test="${list.excel_type eq 'G1'}">高铁预订</c:if>	
							<c:if test="${list.excel_type eq 'G2'}">高铁退票</c:if>	
							<c:if test="${list.excel_type eq 'M1'}">美团预订</c:if>	
							<c:if test="${list.excel_type eq 'M2'}">美团退票</c:if>
							<c:if test="${list.excel_type eq 'M3'}">美团对账</c:if>	
							<c:if test="${list.excel_type eq 'X1'}">携程预订</c:if>	
							<c:if test="${list.excel_type eq 'X2'}">携程退票</c:if>	
							<c:if test="${list.excel_type eq 'T1'}">途牛预订</c:if>	
							<c:if test="${list.excel_type eq 'T2'}">途牛退票</c:if>	
							<c:if test="${list.excel_type eq 'T3'}">途牛对账</c:if>	
							</td>
							<td>
								${fn:substringBefore(list.create_time, ' ')}
								<br />
								${fn:substringAfter(list.create_time, ' ')}
							</td>
							<td>
								<a href="javascript:gotoUpload('${list.excel_url}');">下载</a>  
								<a href="javascript:gotoDelete('${list.excel_id}','${list.excel_url }','${list.excel_name }');">删除</a>
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
