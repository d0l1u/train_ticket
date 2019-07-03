<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo"%>
<%@ page import="com.l9e.transaction.vo.PageVo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>账号统计页面</title>
<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/js/jquery.cookie.js"></script>
<script language="javascript" src="/js/layer/layer.js"></script>
<script language="javascript" src="/js/mylayer.js"></script>
<script type="text/javascript">
	
<%PageVo pageObject = (PageVo) request.getAttribute("pageBean");
			int pageIndex = pageObject.getPageIndex();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String user_level = loginUserVo.getUser_level();%>
	function exportExcel() {
		$("form:first").attr("action", "/accounttj/exportAccounttjexcel.do");
		$("form:first").submit();
		$("form:first").attr("action", "/accounttj/queryAccounttjList.do");
	}
	function mingxi(end_info_time) {
		var url = "/accounttj/queryAccounttjInfo.do?end_info_time="
				+ end_info_time;
		showlayer('账号统计明细:', url, '1000px', '700px')
	}

	$(function() {
		var value = $("#queryType").val();
		if (value == undefined || value == null || value == "") {
			//默认选中日   background-color:#00ff00;
			$("#div_day").attr("style","background-color:#199ED8");
			$("#queryType").val("1");
		}else{
			//选中对应的 
			if(value=="3"){
				$("#div_month").attr("style","background-color:#199ED8");
			}else if(value=="2"){
				$("#div_week").attr("style","background-color:#199ED8");
			}else{
				$("#div_day").attr("style","background-color:#199ED8");
			}
			
		}
	});
	
	function queryListByType(type){
		if(type=="3"){
			$("#div_month").attr("style","background-color:#199ED8");
			$("#div_week").removeAttr("style");
			$("#div_day").removeAttr("style");
		}else if(type=="2"){
			$("#div_week").attr("style","background-color:#199ED8");
			$("#div_month").removeAttr("style");
			$("#div_day").removeAttr("style");
		}else{
			$("#div_day").attr("style","background-color:#199ED8");
			$("#div_month").removeAttr("style");
			$("#div_week").removeAttr("style");
		}
		$("#queryType").val(type);
		$("#myform").submit(); 
	};
	
</script>
<style>
</style>
</head>
<body>
	<div></div>
	<div class="book_manage oz">
		<form action="/accounttj/queryAccounttjList.do" method="post"
			name="myform" id="myform">

			<ul class="order_num oz" style="margin-top: 10px;">
				<li>开始时间：&nbsp;&nbsp;&nbsp; 
					<input type="text" class="text" name="begin_info_time" readonly="readonly" value="${begin_info_time }"
					onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
				</li>
				<li>结束时间：&nbsp;&nbsp;&nbsp; 
					<input type="text" class="text" name="end_info_time" readonly="readonly" value="${end_info_time }"
					onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					<input id="queryType" name = "queryType" type="hidden" value="${queryType}" />
				</li> 
				
			</ul>
			<p>
				<input type="submit" value="查 询" class="btn"
					style="margin-top: 10px;" />
				<%
					if ("2".equals(user_level)) {
				%>
				<input type="button" value="导出Excel" class="btn"
					onclick="exportExcel()" />
				<%
					}
				%>
			</p>
			<div id="hint" class="pub_con" style="display: none"></div>

			<c:if test="${!empty isShowList}">
				<div style="height: 40px">
					<div style="float: left; width: 120px;line-height:27px;">
						<strong>总账号:</strong> <font
							style="color: #f60; width: 20px; border: 0;">${account.sumTotal}</font>
					</div>
					<div style="float: left; width: 120px;line-height:27px;">
						<strong>停用账号:</strong> <font
							style="color: #f60; width: 20px; border: 0;">${account.stopTotal}</font>
					</div>
					<div style="float: left; width: 120px;line-height:27px;">
						<strong>目前空闲:</strong> <font
							style="color: #f60; width: 20px; border: 0;">${account.freeTotal}</font>
					</div>
					<div id="div_month" onclick="queryListByType(3)">月</div>
					<div id="div_week" onclick="queryListByType(2)">周</div>
					<div id="div_day" onclick="queryListByType(1)">日</div>
				</div>

				<table>
					<tr style="background: #EAEAEA;">
						<th width="6.25%" rowspan="2">日期</th>
						<th width="6.25%" rowspan="2">新增帐号</th>
						<th width="6.25%" rowspan="2">白名单总数</th>
						<th width="6.25%" rowspan="2">剩余联系人</th>
						<th width="6.25%" rowspan="2">总票数</th>
						<th width="6.25%" rowspan="2">白名单匹配率</th>
						<th width="6.25%" rowspan="2">新增白名单</th>
						<th width="25%" colspan="4">停用帐号</th>
						<th width="31.25%" colspan="5">订单人数X占订单总数比例</th>
					</tr>
					<tr style="background: #EAEAEA;">
						<th width="6.25%">联系人达上线</th>
						<th width="6.25%">用户信息待核验</th>
						<th width="6.25%">手机待核验</th>
						<th width="6.25%">其他</th>
						<th width="6.25%">X=1</th>
						<th width="6.25%">X=2</th>
						<th width="6.25%">X=3</th>
						<th width="6.25%">X=4</th>
						<th width="6.25%">X=5</th>
					</tr>
					<c:forEach var="obj" items="${accounttjList}" varStatus="idx">
						<c:if test="${!empty obj.strStatisticsDate}">
							<tr>
								<td>${obj.strStatisticsDate}</td>
								<td>${obj.addAccount}</td>
								<td>${obj.whiteList}</td>
								<td>${obj.surplusPassenger}</td>
								<td>${obj.ticketSum}</td>
								<td>${obj.whiteListRate}%</td>
								<td>${obj.addWhiteList}</td>
								<td>${obj.upperLimit}</td>
								<td>${obj.userInfo}</td>
								<td>${obj.phoneVerifi}</td>
								<td>${obj.otherSum}</td>
								<td>${obj.x1Rate}%</td>
								<td>${obj.x2Rate}%</td>
								<td>${obj.x3Rate}%</td>
								<td>${obj.x4Rate}%</td>
								<td>${obj.x5Rate}%</td>
							</tr>
						</c:if>
					</c:forEach>
				</table>
				<jsp:include page="/pages/common/paging.jsp" />
			</c:if>
		</form>
	</div>
</body>
</html>
