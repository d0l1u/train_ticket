<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="com.l9e.transaction.vo.SystemSettingVo" contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
  <head>

		 <title>系统设置管理</title>
		    <link rel="stylesheet" href="/css/back_style.css" type="text/css" />
			<script type="text/javascript" src="/js/jquery.js"></script>
			<script type="text/javascript" src="/js/datepicker/WdatePicker.js"></script>
			<script language="javascript" src="/js/layer/layer.js"></script>
			<script language="javascript" src="/js/mylayer.js"></script>
			<script type="text/javascript">
		function changeChannel(setting_id,channel){
			if(confirm("确认切换吗？")){
				$("form:first").attr("action", "/elongSetting/updateSetting.do?setting_id="+setting_id+"&channel="+channel);
				$("form:first").submit();
			}
		}
		function updateSettingCheck(setting_id,channel){
			if(confirm("确认切换吗？")){
				$("form:first").attr("action", "/elongSetting/updateSettingCheck.do?setting_id="+setting_id+"&channel="+channel);
				$("form:first").submit();
			}
		}
		function opt_rizhi(){
			var url="/elongSetting/querySystemSetList.do";
			showlayer('操作日志',url,'950px','600px')
		}
		
		function changeTcGetOrder(id){
			if(confirm("确认切换同程收单吗？")) {
				$("form:first").attr("action", "/elongSetting/changeTcGetOrder.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		
		function updateSettingCheckAuto(setting_id){
			if(confirm("确认切换吗？")){
				$("form:first").attr("action", "/elongSetting/updateSettingCheckAuto.do?setting_id="+setting_id);
				$("form:first").submit();
			}
		}
	</script>
<style>
.outer {width: 1000px; padding: 0 20px;}
.book_manage {width: 1000px; margin: 20px auto;}
.book_manage table {border: 1px solid #dadada; width: 800px; text-align: center;}
.book_manage th,.book_manage td,.book_manage tr {border: 1px solid #dadada;}
.book_manage th {height: 30px; font: bold 13px/ 30px "宋体";}
.book_manage td {line-height: 20px;}
td {padding: 5px 0;}
.setting_text {width: 220px;}
</style>
	</head>

	<body>
		<div class="outer">
			<div class="book_manage oz">
				<form action="/elongSetting/getElongSetting.do" method="post">
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
						
						<c:forEach items="${elongsetting}" var="ss">
						<c:set value="ss" scope="request" var="ss"/>
						<c:if test="${ss.setting_name eq 'out_notify_elong'}">
						<tr>
							<td>
								通知艺龙：
							</td>
							<td>
								<input type="radio" name="out_notify_elong"
									<c:if test="${ss.setting_value eq '1'}"> checked </c:if>
									value="1" />
								预定成功通知
								<input type="radio" name="out_notify_elong"
									<c:if test="${ss.setting_value eq '0'}"> checked </c:if>
									value="0" />
								出票成功通知
							</td>
							<td>
								<input type="button" class="btn" value="切换"
									onclick="changeChannel('${ss.setting_id}','elong')" />
							</td>
						</tr>
						</c:if>
						<c:if test="${ss.setting_name eq 'elong_check'}">
						<tr>
							<td>
								艺龙核验：
							</td>
							<td>
								<input type="radio" name="elong_check"
									<c:if test="${ss.setting_value eq '1'}"> checked </c:if>
									value="1" />
								开启核验
								<input type="radio" name="elong_check"
									<c:if test="${ss.setting_value eq '0'}"> checked </c:if>
									value="0" />
								关闭核验
							</td>
							<td>
								<input type="button" class="btn" value="切换"
									onclick="updateSettingCheck('${ss.setting_id}','elong')" />
							</td>
						</tr>
						</c:if>
						<c:if test="${ss.setting_name eq 'out_notify_tongcheng'}">
						<tr>
							<td>
								通知同程：
							</td>
							<td>
								<input type="radio" name="out_notify_tongcheng"
									<c:if test="${ss.setting_value eq '1'}"> checked </c:if>
									value="1" />
								预定成功通知
								<input type="radio" name="out_notify_tongcheng"
									<c:if test="${ss.setting_value eq '0'}"> checked </c:if>
									value="0" />
								出票成功通知
							</td>
							<td>
								<input type="button" class="btn" value="切换"
									onclick="changeChannel('${ss.setting_id}','tongcheng')" />
							</td>
						</tr>
						</c:if>
						<c:if test="${ss.setting_name eq 'tongcheng_check'}">
						<tr>
							<td>
								同程核验：
							</td>
							<td>
								<input type="radio" name="tongcheng_check"
									<c:if test="${ss.setting_value eq '1'}"> checked </c:if>
									value="1" />
								开启核验
								<input type="radio" name="tongcheng_check"
									<c:if test="${ss.setting_value eq '0'}"> checked </c:if>
									value="0" />
								关闭核验
							</td>
							<td>
								<input type="button" class="btn" value="切换"
									onclick="updateSettingCheck('${ss.setting_id}','tongcheng')" />
							</td>
						</tr>
						</c:if>
						
						<c:if test="${ss.setting_name eq 'tongcheng_check_auto'}">
						<tr>
							<td>
								自动开关核验：
							</td>
							<td>
								<input type="radio" name="tongcheng_check_auto"
									<c:if test="${ss.setting_value eq '1'}"> checked </c:if>
									value="1" />
								开启
								<input type="radio" name="tongcheng_check_auto"
									<c:if test="${ss.setting_value eq '0'}"> checked </c:if>
									value="0" />
								关闭
							</td>
							<td>
								<input type="button" class="btn" value="切换"
									onclick="updateSettingCheckAuto('${ss.setting_id}')" />
							</td>
						</tr>
						</c:if>
						
				<c:if test="${ss.setting_name eq 'tongcheng_get_order'}">
		    		<tr>
		    			<td>同程收单：</td>
				    	<td>
			    			<input type="radio" name="tcgo_value"
			    				<c:if test="${ss.setting_value eq '1'}">
			    					checked
			    				</c:if>
			    				value="1"/>收单
			    			<input type="radio" name="tcgo_value"
			    				<c:if test="${ss.setting_value eq '0'}">
			    					checked
			    				</c:if>
			    				value="0"/>停止收单
			    		</td>
						<td>
		    				<input type="button" class="btn" value="切换" onclick="changeTcGetOrder('${ss.setting_id}')"/>
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
