<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>

		<title>系统设置管理</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
		<script type="text/javascript">
		function changeChannel(setting_id){
			if(confirm("确认切换吗？")){
				$("form:first").attr("action", "/qunarsetting/updateSetting.do?setting_id="+setting_id);
				$("form:first").submit();
			}
		}
		function updateSettingCheck(setting_id){
			if(confirm("确认切换吗？")){
				$("form:first").attr("action", "/qunarsetting/updateSettingCheck.do?setting_id="+setting_id);
				$("form:first").submit();
			}
		}
		
		function opt_rizhi(){
			var url="/qunarsetting/querySystemSetList.do";
			showlayer('操作日志',url,'950px','600px')
		}
	</script>
		<style>
.outer {
	width: 1000px;
	padding: 0 20px;
}

.book_manage {
	width: 1000px;
	margin: 20px auto;
}

.book_manage table {
	border: 1px solid #dadada;
	width: 800px;
	text-align: center;
}

.book_manage th,.book_manage td,.book_manage tr {
	border: 1px solid #dadada;
}

.book_manage th {
	height: 30px;
	font: bold 13px/ 30px "宋体";
}

.book_manage td {
	line-height: 20px;
}

td {
	padding: 5px 0;
}

.setting_text {
	width: 220px;
}
</style>
	</head>

	<body>
		<div class="outer">
			<div class="book_manage oz">
				<form action="/qunarsetting/getQunarSetting.do" method="post">
					<table>
						<tr>
							<th style="width: 100px;">
								key
							</th>
							<th style="width: 300px;">
								内容
							</th>
							<th>
								操作
							</th>
						</tr>
						<c:forEach items="${qunarsetting}" var="ss">
						<c:set value="ss" scope="request" var="ss"/>
						<c:if test="${ss.setting_name eq 'out_notify_qunar'}">
						<tr>
							<td>
								通知去哪儿：
							</td>
							<td>
								<input type="radio" name="out_notify_qunar"
									<c:if test="${ss.setting_value eq '1'}"> checked </c:if>
									value="1" />
								预定成功通知
								<input type="radio" name="out_notify_qunar"
									<c:if test="${ss.setting_value eq '0'}"> checked </c:if>
									value="0" />
								出票成功通知
							</td>
							<td>
								<input type="button" class="btn" value="切换"
									onclick="changeChannel('${ss.setting_id}')" />
							</td>
						</tr>
						</c:if>
						<c:if test="${ss.setting_name eq 'qunar_check'}">
						<tr>
							<td>
								去哪核验：
							</td>
							<td>
								<input type="radio" name="qunar_check"
									<c:if test="${ss.setting_value eq '1'}"> checked </c:if>
									value="1" />
								开启核验
								<input type="radio" name="qunar_check"
									<c:if test="${ss.setting_value eq '0'}"> checked </c:if>
									value="0" />
								关闭核验
							</td>
							<td>
								<input type="button" class="btn" value="切换"
									onclick="updateSettingCheck('${ss.setting_id}')" />
							</td>
						</tr>
						</c:if>
						</c:forEach>
					</table>
					<br/>
					<p>
						<input type="button" value="操作日志" class="btn" id="rizhi" onclick="opt_rizhi()" />
					</p> 
				</form>
			</div>
		</div>
	</body>
</html>
