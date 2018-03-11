<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="com.l9e.transaction.vo.SystemSettingVo" contentType="text/html; charset=UTF-8"%>

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
		function changeChannel(channel){
			if(confirm("确认切换吗？")){
				$("form:first").attr("action", "/systemSetting/updateInterfaceChannel.do?channelId=" + channel);
				$("form:first").submit();
			}
		}
		function changeConTimeout(conTimeout) {
			var ch = $("#con_timeout").val();
			if($.trim(ch)==""){
				alert("连接超时不能为空！");
				return;
			}
			if(confirm("确认修改吗?")) {
				$("form:first").attr("action", "/systemSetting/updateConTimeout.do?conTimeoutId=" + conTimeout);
				$("form:first").submit();
			}
		}
		function changeReadTimeout(readTimeout) {
			var ch = $("#read_timeout").val();
			if($.trim(ch)==""){
				alert("读超时不能为空！");
				return;
			}
			if(confirm("确认修改吗?")) {
				$("form:first").attr("action", "/systemSetting/updateReadTimeout.do?readTimeoutId=" + readTimeout);
				$("form:first").submit();
			}
		}
		//修改12306旧接口地址
		function changeURL(urlId) {
			var ch = $("#"+urlId).val();
			if($.trim(ch)==""){
				alert("12306接口地址不能为空！");
				return;
			}
			if(confirm("确认修改12306接口地址吗?")) {
				$("form:first").attr("action", "/systemSetting/updateInterface12306RUL.do?urlId=" + urlId);
				$("form:first").submit();
			}
		}
		//切换12306旧接口地址
		function changeStatus(urlId) {
			if(confirm("确认切换12306接口地址吗？")) {
				$("form:first").attr("action", "/systemSetting/changeURLStatus.do?urlId=" +urlId);
				$("form:first").submit();
			}
		}
		//删除12306旧接口地址
		function deleteUrl(urlId) {
			if(confirm("确认删除12306接口地址吗？")) {
				$("form:first").attr("action", "/systemSetting/deleteURL.do?urlId=" +urlId);
				$("form:first").submit();
			}
		}
		//增加12306旧接口地址
		function addURL() {
			$("form:first").attr("action","/systemSetting/turnToAddURLPage.do");
			$("form:first").submit();
		}
		//修改12306新接口地址
		function changeNewURL(urlId) {
			var ch = $("#"+urlId).val();
			if($.trim(ch)==""){
				alert("12306新接口地址不能为空！");
				return;
			}
			if(confirm("确认修改12306新接口地址吗?")) {
				$("form:first").attr("action", "/systemSetting/updateInterface12306NewRUL.do?urlId=" + urlId);
				$("form:first").submit();
			}
		}
		//切换12306新接口地址
		function changeNewStatus(urlId) {
			if(confirm("确认切换12306新接口地址吗？")) {
				$("form:first").attr("action", "/systemSetting/changeNewURLStatus.do?urlId=" +urlId);
				$("form:first").submit();
			}
		}
		//删除12306新接口地址
		function deleteNewUrl(urlId) {
			if(confirm("确认删除12306新接口地址吗？")) {
				$("form:first").attr("action", "/systemSetting/deleteNewURL.do?urlId=" +urlId);
				$("form:first").submit();
			}
		}
		//增加12306新接口地址
		function addNewURL() {
			$("form:first").attr("action","/systemSetting/turnToAddNewURLPage.do");
			$("form:first").submit();
		}
		function changePayCtrl(id){
			if(confirm("确认切换吗？")){
				$("form:first").attr("action", "/systemSetting/updatePayCtrl.do?setting_id=" + id);
				$("form:first").submit();
			}
		}
		function changeBxChannel(id) {
			if(confirm("确认切换保险渠道吗？")) {
				$("form:first").attr("action", "/systemSetting/updateBxChannel.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		function changeMsgChannel(id) {
			if(confirm("确认切换短信渠道吗？")) {
				$("form:first").attr("action", "/systemSetting/updateMsgChannel.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		function changeTtlChannel(id){
			if(confirm("确认切换默认乘车日期吗？")) {
				$("form:first").attr("action", "/systemSetting/updateTtlChannel.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		function changeStaChannel(id){
			var sv = $("#sta_value").val();
			if($.trim(sv)==""){
				alert("余票阀值不能为空！");
				return;
			}
			if(confirm("确认切换余票阀值吗？")) {
				$("form:first").attr("action", "/systemSetting/updateStaChannel.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		function changeQtaChannel(id){
			if(confirm("确认修改默认排队等候买票人数吗？")) {
				$("form:first").attr("action", "/systemSetting/updateQtaChannel.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		function changeQttChannel(id){
			if(confirm("确认修改排队等候时间吗？")) {
				$("form:first").attr("action", "/systemSetting/updateQttChannel.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		function changeBookRobot(id){
			if(confirm("确认切换预定机器人版本吗？")) {
				$("form:first").attr("action", "/systemSetting/updateBookRobotChannel.do?setting_id=" +id);
				$("form:first").submit();
			}
		}

		//切换网站是否可预订功能
		function changeSysBook(id) {
			if(confirm("确认切换前台终止预订功能吗？")) {
				$("form:first").attr("action", "/systemSetting/changeSysBook.do?setting_id=" +id);
				$("form:first").submit();
			}
		}

		function opt_rizhi(){
			var url="/systemSetting/querySystemSetList.do";
			showlayer('操作日志',url,'950px','600px')
		}
		//切换打码方式：0人工打码  1机器识别
		function changeCodeType(id){
			if(confirm("确认切换打码方式吗？")) {
				$("form:first").attr("action", "/systemSetting/changeCodeType.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		//切换改退方式：0人工改退  1机器改退
		function changeRefundAndAlert(id){
			if(confirm("确认切换打码方式吗？")) {
				$("form:first").attr("action", "/systemSetting/changeRefundAndAlert.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		
		function changeBuyTime(id){
			if(confirm("确认切换开车前购票时间吗？")) {
				$("form:first").attr("action", "/systemSetting/updateBuytime.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		function changeBeforeRefundTime(id){
			if(confirm("确认切换开车前退票时间吗？")) {
				$("form:first").attr("action", "/systemSetting/updateBeforeRefundtime.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		function changeRefundTime(id){
			if(confirm("确认切换开车后退票时间吗？")) {
				$("form:first").attr("action", "/systemSetting/updateRefundtime.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		//修改预订机器人一次处理订单量
		function changeRapnChannel(id){
			if(confirm("确认修改预订机器人一次处理订单量吗？")) {
				$("form:first").attr("action", "/systemSetting/updateRapnChannel.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		//修改支付机器人一次处理订单量
		function changeRppnChannel(id){
			if(confirm("确认修改支付机器人一次处理订单量吗？")) {
				$("form:first").attr("action", "/systemSetting/updateRppnChannel.do?setting_id=" +id);
				$("form:first").submit();
			}
		}	
		//修改预购天数
		function changeBdnChannel(id){
			if(confirm("确认修改预约购票天数吗？")) {
				$("form:first").attr("action", "/systemSetting/updateBdnChannel.do?setting_id=" +id);
				$("form:first").submit();
			}
		}		
		
	</script>
	<style>
		.outer{
			width:1000px;
			padding:0 20px;
		}
		.book_manage{width:1000px;margin:20px auto;}
		.book_manage table{border:1px solid #dadada;width:800px;text-align:center;}
		.book_manage th,.book_manage td,.book_manage tr{border:1px solid #dadada;}
		.book_manage th{height:30px;font:bold 13px/30px "宋体";}
		.book_manage td{line-height:20px;}
		td{
			padding: 5px 0;
		}
		.setting_text{
			width:220px;
		}
	</style>
  </head>
  
  <body>
  	<div class="outer">
  	<div class="book_manage oz">
  	<form action="/systemSetting/getSystemSetting.do" method="post">
  		
	  	<table>
  			<tr>
	    		<th style="width: 170px;">key</th>
	    		<th style="width: 300px;">内容</th>
	    		<th>操作</th>
	    	</tr>
		    <c:forEach items="${systemSetting}" var="ss">
		    <c:set value="ss" scope="request" var="ss"/>
		    	<c:if test="${ss.setting_name eq 'INTERFACE_CHANNEL'}">
		    		<tr>
			    		<td>接口：</td>
			    		<td>
			    			<!-- 	<input type="radio" name="channel" 
			    				<c:if test="${ss.setting_value eq '1'}">
			    					checked
			    				</c:if>
			    				value="1"/>机器人旧接口   -->
		    				<input type="radio" name="channel"
			    				<c:if test="${ss.setting_value eq '3'}">
			    					checked
			    				</c:if>
			    				value="3"/>内部新接口
			    			<input type="radio" name="channel"
			    				<c:if test="${ss.setting_value eq '2'}">
			    					checked
			    				</c:if>
			    				value="2"/>第三方接口
			    		</td>
			    		<td><input type="button" class="btn" value="切换" onclick="changeChannel('${ss.setting_id}')"/></td>
		    	     </tr>	
		    	</c:if>
		    	
		    	<c:if test="${ss.setting_name eq 'INTERFACE_CON_TIMEOUT'}">
		    		<tr>
		    			<td>${ss.setting_desc}：</td>
		    			<td><input type="text" name="con_timeout" id="con_timeout" value="${ss.setting_value}"/></td>
		    			<td><input type="button" class="btn" value="修改" onclick="changeConTimeout('${ss.setting_id}')"/></td>
		    		</tr>
		    	</c:if>
		    	
		    	<c:if test="${ss.setting_name eq 'INTERFACE_READ_TIMEOUT'}">
		    		<tr>
		    			<td>${ss.setting_desc}：</td>
		    			<td><input type="text" name="read_timeout" id="read_timeout" value="${ss.setting_value}"/></td>
		    			<td><input type="button" class="btn" value="修改" onclick="changeReadTimeout('${ss.setting_id}')"/></td>
		    		</tr>
		    	</c:if>
		    	
		    	<c:if test="${ss.setting_name eq 'INTERFACE_12306_URL'}">
		    		<tr>
		    			<td>${ss.setting_desc}：</td>
		    			<td><input type="text" class="setting_text" name="${ss.setting_id}" id="${ss.setting_id}" value="${ss.setting_value}"/></td>
		    			<td>
		    			<input type="button" class="btn" value="添加" onclick="addURL()"/>
		    			<input type="button" class="btn" value="修改" onclick="changeURL('${ss.setting_id}')"/>
		    			<c:choose>
		    				<c:when test="${ss.setting_status eq '1'}">
		    					<input type="button" class="btn" value="禁用" onclick="changeStatus('${ss.setting_id}')"/>
		    				</c:when>
		    				<c:when test="${ss.setting_status eq '0'}">
		    					<input type="button" class="btn" value="启用" onclick="changeStatus('${ss.setting_id}')"/>
		    				</c:when>
		    			</c:choose>
		    			<input type="button" class="btn" value="删除" onclick="deleteUrl('${ss.setting_id}')"/>
		    			</td>
		    		</tr>
		    	</c:if>
		    	
		    	<c:if test="${ss.setting_name eq 'INTERFACE_12306_NEW_URL'}">
		    		<tr>
		    			<td>${ss.setting_desc}：</td>
		    			<td><input type="text" class="setting_text" name="${ss.setting_id}" id="${ss.setting_id}" value="${ss.setting_value}"/></td>
		    			<td>
		    			<input type="button" class="btn" value="添加" onclick="addNewURL()"/>
		    			<input type="button" class="btn" value="修改" onclick="changeNewURL('${ss.setting_id}')"/>
		    			<c:choose>
		    				<c:when test="${ss.setting_status eq '1'}">
		    					<input type="button" class="btn" value="禁用" onclick="changeNewStatus('${ss.setting_id}')"/>
		    				</c:when>
		    				<c:when test="${ss.setting_status eq '0'}">
		    					<input type="button" class="btn" value="启用" onclick="changeNewStatus('${ss.setting_id}')"/>
		    				</c:when>
		    			</c:choose>
		    			<input type="button" class="btn" value="删除" onclick="deleteNewUrl('${ss.setting_id}')"/>
		    			</td>
		    		</tr>
		    	</c:if>
		    	
		    	<c:if test="${ss.setting_name eq 'robot_pay_ctrl'}">
		    		<tr>
		    			<td>机器人支付控制：</td>
				    	<td>
			    			<input type="radio" name="robot_pay_ctrl"
			    				<c:if test="${ss.setting_value eq '1'}">
			    					checked
			    				</c:if>
			    				value="1"/>机器人支付
			    			<input type="radio" name="robot_pay_ctrl"
			    				<c:if test="${ss.setting_value eq '0'}">
			    					checked
			    				</c:if>
			    				value="0"/>人工支付
			    		</td>
			    		<td><input type="button" class="btn" value="切换" onclick="changePayCtrl('${ss.setting_id}')"/></td>
		    		</tr>
		    	</c:if>
		    	
		    	<c:if test="${ss.setting_name eq 'bx_channel'}">
		    		<tr>
		    			<td>保险渠道：</td>
		    			<td>
		    				<input type="radio" name="bx_value"
			    				<c:if test="${ss.setting_value eq '1'}">
			    					checked
			    				</c:if>
			    				value="1"/>快保
			    			<input type="radio" name="bx_value"
			    				<c:if test="${ss.setting_value eq '2'}">
			    					checked
			    				</c:if>
			    				value="2"/>合众
		    			</td>
		    			<td>
		    				<input type="button" class="btn" value="切换" onclick="changeBxChannel('${ss.setting_id}')"/>
		    			</td>
		    		</tr>
				</c:if>
		    	
		    	<c:if test="${ss.setting_name eq 'mobile_msg_channel'}">
		    		<tr>
		    			<td>短信渠道：</td>
		    			<td>
		    				<input type="radio" name="msg_value"
			    				<c:if test="${ss.setting_value eq '1'}">
			    					checked
			    				</c:if>
			    				value="1"/>19e
			    			<input type="radio" name="msg_value"
			    				<c:if test="${ss.setting_value eq '2'}">
			    					checked
			    				</c:if>
			    				value="2"/>鼎鑫亿动
		    			</td>
		    			<td>
		    				<input type="button" class="btn" value="切换" onclick="changeMsgChannel('${ss.setting_id}')"/>
		    			</td>
		    		</tr>
		    	</c:if>
		    	
		    	<c:if test="${ss.setting_name eq 'ticketing_time_limit'}">
		    		<tr>
		    			<td>默认乘车日期：</td>
		    			<td>
		    				<input type="radio" name="ttl_value"
			    				<c:if test="${ss.setting_value eq '0'}">
			    					checked
			    				</c:if>
			    				value="0"/>当天
			    			<input type="radio" name="ttl_value"
			    				<c:if test="${ss.setting_value eq '2'}">
			    					checked
			    				</c:if>
			    				value="2"/>3天
		    			</td>
		    			<td>
		    				<input type="button" class="btn" value="切换" onclick="changeTtlChannel('${ss.setting_id}')"/>
		    			</td>
		    		</tr>	
		    	</c:if>
		    	
		    	<c:if test="${ss.setting_name eq 'spare_ticket_amount'}">
		    		<tr>
		    			<td>余票阀值：</td>
		    			<td>
		    				<input type="text" id="sta_value" name="sta_value" value="${ss.setting_value}" maxlength="3" onpropertychange='if(/[^\-?\d*\.?\d{0,2}]/.test(this.value))   this.value=this.value.replace(/[^\-?\d*\.?\d{0,2}]/,"")'/>
		    			</td>
		    			<td>
		    				<input type="button" class="btn" value="修改" onclick="changeStaChannel('${ss.setting_id}')"/>
		    			</td>
		    		</tr>	
		    	</c:if>
		    	
		    	<c:if test="${ss.setting_name eq 'queuing_tickets_amount'}">
		    		<tr>
		    			<td>默认排队人数：</td>
		    			<td>
		    				<input type="text" id="qta_value" name="qta_value" value="${ss.setting_value}" maxlength="4" onpropertychange='if(/[^\-?\d*\.?\d{0,2}]/.test(this.value))   this.value=this.value.replace(/[^\-?\d*\.?\d{0,2}]/,"")'/>
		    			</td>
		    			<td>
		    				<input type="button" class="btn" value="修改" onclick="changeQtaChannel('${ss.setting_id}')"/>
		    			</td>
		    		</tr>	
		    	</c:if>
		    	
		    	<c:if test="${ss.setting_name eq 'queuing_tickets_time'}">
		    		<tr>
		    			<td>排队等待时间：</td>
		    			<td>
		    				<input type="text" id="qtt_value" name="qtt_value" value="${ss.setting_value}"/>
		    			</td>
		    			<td>
		    				<input type="button" class="btn" value="修改" onclick="changeQttChannel('${ss.setting_id}')"/>
		    			</td>
		    		</tr>	
		    	</c:if>
		    	
		    	<c:if test="${ss.setting_name eq 'book_robot_version'}">
		    		<tr>
		    			<td>预订机器人版本：</td>
				    	<td>
			    			<input type="radio" name="book_robot_version"
			    				<c:if test="${ss.setting_value eq '0'}">
			    					checked
			    				</c:if>
			    				value="0"/>旧版预定机器人
			    			<input type="radio" name="book_robot_version"
			    				<c:if test="${ss.setting_value eq '1'}">
			    					checked
			    				</c:if>
			    				value="1"/>新版预订机器人
			    		</td>
			    		<td><input type="button" class="btn" value="切换" onclick="changeBookRobot('${ss.setting_id}')"/></td>
		    		</tr>	
		    	</c:if>
		    	
		    	<c:if test="${ss.setting_name eq 'sys_weather_book'}">
		    		<tr>
		    			<td>终止前台预订功能：</td>
				    	<td>
			    			<input type="radio" name="weather_book"
			    				<c:if test="${ss.setting_value eq '0'}">
			    					checked
			    				</c:if>
			    				value="0"/>停止预订
			    			<input type="radio" name="weather_book"
			    				<c:if test="${ss.setting_value eq '1'}">
			    					checked
			    				</c:if>
			    				value="1"/>开启预订
			    		</td>
			    		<td><input type="button" class="btn" value="切换" onclick="changeSysBook('${ss.setting_id}')"/></td>
		    		</tr>	
		    	</c:if>
		    	
		    	<c:if test="${ss.setting_name eq 'rand_code_type'}">
		    		<tr>
		    			<td>打码方式：</td>
				    	<td>
			    			<input type="radio" name="code_type"
			    				<c:if test="${ss.setting_value eq '0'}">
			    					checked
			    				</c:if>
			    				value="0"/>手动打码
			    			<input type="radio" name="code_type"
			    				<c:if test="${ss.setting_value eq '1'}">
			    					checked
			    				</c:if>
			    				value="1"/>机器识别
			    		</td>
			    		<td><input type="button" class="btn" value="切换" onclick="changeCodeType('${ss.setting_id}')"/></td>
		    		</tr>	
		    	</c:if>
		    	<c:if test="${ss.setting_name eq 'refund_and_alert'}">
		    		<tr>
		    			<td>改退方式：</td>
				    	<td>
			    			<input type="radio" name="refund_and_alert"
			    				<c:if test="${ss.setting_value eq '0'}">
			    					checked
			    				</c:if>
			    				value="0"/>人工改退
			    			<input type="radio" name="refund_and_alert"
			    				<c:if test="${ss.setting_value eq '1'}">
			    					checked
			    				</c:if>
			    				value="1"/>机器改退
			    		</td>
			    		<td><input type="button" class="btn" value="切换" onclick="changeRefundAndAlert('${ss.setting_id}')"/></td>
		    		</tr>	
		    	</c:if>
		    	<!-- 
		    	<c:if test="${ss.setting_name eq 'stop_buyTicket_time'}">
		    		<tr>
		    			<td>开车前订票时间：</td>
		    			<td>
		    				<input type="text" id="btt_value" name="btt_value" value="${ss.setting_value}"/>&nbsp;小时
		    			</td>
		    			<td>
		    				<input type="button" class="btn" value="修改" onclick="changeBuyTime('${ss.setting_id}')"/>
		    			</td>
		    		</tr>	
		    	</c:if>
		    	 -->
		    	 <c:if test="${ss.setting_name eq 'stop_buyTicket_time'}">
		    		<tr>
		    			<td>开车前订票时间：</td>
				    	<td>
				    		<input type="radio" name="btt_value"
			    				<c:if test="${ss.setting_value eq '0.5'}">
			    					checked
			    				</c:if>
			    				value="0.5"/>0.5小时
				    		<input type="radio" name="btt_value"
			    				<c:if test="${ss.setting_value eq '2'}">
			    					checked
			    				</c:if>
			    				value="2"/>2小时
			    			<input type="radio" name="btt_value"
			    				<c:if test="${ss.setting_value eq '3'}">
			    					checked
			    				</c:if>
			    				value="3"/>3小时
			    			<input type="radio" name="btt_value"
			    				<c:if test="${ss.setting_value eq '4'}">
			    					checked
			    				</c:if>
			    				value="4"/>4小时
			    			<input type="radio" name="btt_value"
			    				<c:if test="${ss.setting_value eq '5'}">
			    					checked
			    				</c:if>
			    				value="5"/>5小时
			    			<input type="radio" name="btt_value"
			    				<c:if test="${ss.setting_value eq '6'}">
			    					checked
			    				</c:if>
			    				value="6"/>6小时
			    		</td>
						<td>
		    				<input type="button" class="btn" value="切换" onclick="changeBuyTime('${ss.setting_id}')"/>
		    			</td>		    		
		    		</tr>	
		    	</c:if>
		    	
		    	<c:if test="${ss.setting_name eq 'before_refundTicket_time'}">
		    		<tr>
		    			<td>开车前退票时间：</td>
				    	<td>
			    			<input type="radio" name="brtt_value"
			    				<c:if test="${ss.setting_value eq '2'}">
			    					checked
			    				</c:if>
			    				value="2"/>2小时
			    			<input type="radio" name="brtt_value"
			    				<c:if test="${ss.setting_value eq '3'}">
			    					checked
			    				</c:if>
			    				value="3"/>3小时
			    			<input type="radio" name="brtt_value"
			    				<c:if test="${ss.setting_value eq '4'}">
			    					checked
			    				</c:if>
			    				value="4"/>4小时
			    		</td>
						<td>
		    				<input type="button" class="btn" value="切换" onclick="changeBeforeRefundTime('${ss.setting_id}')"/>
		    			</td>		    		
		    		</tr>	
		    	</c:if>
		    	
		    	<c:if test="${ss.setting_name eq 'stop_refundTicket_time'}">
		    		<tr>
		    			<td>开车后退票时间：</td>
				    	<td>
			    			<input type="radio" name="srtt_value"
			    				<c:if test="${ss.setting_value eq '12'}">
			    					checked
			    				</c:if>
			    				value="12"/>12小时
			    			<input type="radio" name="srtt_value"
			    				<c:if test="${ss.setting_value eq '24'}">
			    					checked
			    				</c:if>
			    				value="24"/>24小时
			    			<input type="radio" name="srtt_value"
			    				<c:if test="${ss.setting_value eq '48'}">
			    					checked
			    				</c:if>
			    				value="48"/>48小时
			    				
			    			<input type="radio" name="srtt_value"
			    				<c:if test="${ss.setting_value eq '1440'}">
			    					checked
			    				</c:if>
			    				value="1440"/>两个月
			    		</td>
						<td>
		    				<input type="button" class="btn" value="切换" onclick="changeRefundTime('${ss.setting_id}')"/>
		    			</td>		    		
		    		</tr>	
		    	</c:if>
		    	
		    	<c:if test="${ss.setting_name eq 'robot_app_product_num'}">
		    		<tr>
		    			<td>预订机器人一次处理订单量：</td>
		    			<td>
		    				<input type="text" id="rapn_value" name="rapn_value" 
		    				value="${ss.setting_value}" maxlength="4" 
		    				onpropertychange='if(/[^\-?\d*\.?\d{0,2}]/.test(this.value))   this.value=this.value.replace(/[^\-?\d*\.?\d{0,2}]/,"")'/>
		    			</td>
		    			<td>
		    				<input type="button" class="btn" value="修改" onclick="changeRapnChannel('${ss.setting_id}')"/>
		    			</td>
		    		</tr>	
		    	</c:if>
		    	
		    	<c:if test="${ss.setting_name eq 'robot_pay_product_num'}">
		    		<tr>
		    			<td>支付机器人一次处理订单量：</td>
		    			<td>
		    				<input type="text" id="rppn_value" name="rppn_value" 
		    				value="${ss.setting_value}" maxlength="4" 
		    				onpropertychange='if(/[^\-?\d*\.?\d{0,2}]/.test(this.value))   this.value=this.value.replace(/[^\-?\d*\.?\d{0,2}]/,"")'/>
		    			</td>
		    			<td>
		    				<input type="button" class="btn" value="修改" onclick="changeRppnChannel('${ss.setting_id}')"/>
		    			</td>
		    		</tr>	
		    	</c:if>
		    	
		    	<c:if test="${ss.setting_name eq 'book_day_num'}">
		    		<tr>
		    			<td>预购天数：</td>
		    			<td>
		    				<input type="text" id="bdn_value" name="bdn_value" 
		    				value="${ss.setting_value}" maxlength="4" 
		    				onpropertychange='if(/[^\-?\d*\.?\d{0,2}]/.test(this.value))   this.value=this.value.replace(/[^\-?\d*\.?\d{0,2}]/,"")'/>
		    			</td>
		    			<td>
		    				<input type="button" class="btn" value="修改" onclick="changeBdnChannel('${ss.setting_id}')"/>
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
