<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="com.l9e.transaction.vo.SystemSettingVo" contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>系统设置管理</title>
    <link rel="stylesheet" href="/css/back_style.css" type="text/css" />
	<script type="text/javascript" src="/js/jquery.js"></script>
	<script type="text/javascript" src="/js/datepicker/WdatePicker.js"></script>
	<script language="javascript" src="/js/layer/layer.js"></script>
	<script language="javascript" src="/js/mylayer.js"></script>
	<script type="text/javascript">
		function changeChannel(channel){
			if(confirm("确认切换吗？")){
				$("form:first").attr("action", "/trainSystemSetting/updateInterfaceChannel.do?channelId=" + channel);
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
				$("form:first").attr("action", "/trainSystemSetting/updateConTimeout.do?conTimeoutId=" + conTimeout);
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
				$("form:first").attr("action", "/trainSystemSetting/updateReadTimeout.do?readTimeoutId=" + readTimeout);
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
				$("form:first").attr("action", "/trainSystemSetting/updateInterface12306RUL.do?urlId=" + urlId);
				$("form:first").submit();
			}
		}
		//切换12306旧接口地址
		function changeStatus(urlId) {
			if(confirm("确认切换12306接口地址吗？")) {
				$("form:first").attr("action", "/trainSystemSetting/changeURLStatus.do?urlId=" +urlId);
				$("form:first").submit();
			}
		}
		//删除12306旧接口地址
		function deleteUrl(urlId) {
			if(confirm("确认删除12306接口地址吗？")) {
				$("form:first").attr("action", "/trainSystemSetting/deleteURL.do?urlId=" +urlId);
				$("form:first").submit();
			}
		}
		//增加12306旧接口地址
		function addURL() {
			$("form:first").attr("action","/trainSystemSetting/turnToAddURLPage.do");
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
				$("form:first").attr("action", "/trainSystemSetting/updateInterface12306NewRUL.do?urlId=" + urlId);
				$("form:first").submit();
			}
		}
		//切换12306新接口地址
		function changeNewStatus(urlId) {
			if(confirm("确认切换12306新接口地址吗？")) {
				$("form:first").attr("action", "/trainSystemSetting/changeNewURLStatus.do?urlId=" +urlId);
				$("form:first").submit();
			}
		}
		//删除12306新接口地址
		function deleteNewUrl(urlId) {
			if(confirm("确认删除12306新接口地址吗？")) {
				$("form:first").attr("action", "/trainSystemSetting/deleteNewURL.do?urlId=" +urlId);
				$("form:first").submit();
			}
		}
		//增加12306新接口地址
		function addNewURL() {
			$("form:first").attr("action","/trainSystemSetting/turnToAddNewURLPage.do");
			$("form:first").submit();
		}
		function changePayCtrl(id){
			if(confirm("确认切换吗？")){
				$("form:first").attr("action", "/trainSystemSetting/updatePayCtrl.do?setting_id=" + id);
				$("form:first").submit();
			}
		}
		function changeBxChannel(id) {
			if(confirm("确认切换保险渠道吗？")) {
				$("form:first").attr("action", "/trainSystemSetting/updateBxChannel.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		function changeMsgChannel(id) {
			if(confirm("确认切换短信渠道吗？")) {
				$("form:first").attr("action", "/trainSystemSetting/updateMsgChannel.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		function changeTtlChannel(id){
			if(confirm("确认切换默认乘车日期吗？")) {
				$("form:first").attr("action", "/trainSystemSetting/updateTtlChannel.do?setting_id=" +id);
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
				$("form:first").attr("action", "/trainSystemSetting/updateStaChannel.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		function changeQtaChannel(id){
			if(confirm("确认修改默认排队等候买票人数吗？")) {
				$("form:first").attr("action", "/trainSystemSetting/updateQtaChannel.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		function changeQttChannel(id){
			if(confirm("确认修改排队等候时间吗？")) {
				$("form:first").attr("action", "/trainSystemSetting/updateQttChannel.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		function changeBookRobot(id){
			if(confirm("确认切换预定机器人版本吗？")) {
				$("form:first").attr("action", "/trainSystemSetting/updateBookRobotChannel.do?setting_id=" +id);
				$("form:first").submit();
			}
		}

		//切换网站是否可预订功能
		function changeSysBook(id) {
			if(confirm("确认切换前台终止预订功能吗？")) {
				$("form:first").attr("action", "/trainSystemSetting/changeSysBook.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		//查看操作日志
		function opt_rizhi(){
			var url="/trainSystemSetting/querySystemSetList.do";
			showlayer('操作日志',url,'950px','600px')
		}
		//查看返回日志
		function return_log(){
			$("form:first").attr("action", "/trainSystemSetting/gototrain_return_optlog.do" );
			$("form:first").submit();
		}
		
		
		//切换打码方式：0人工打码  1机器识别
		function changeCodeType(id){
			if(confirm("确认切换打码方式吗？")) {
				$("form:first").attr("action", "/trainSystemSetting/changeCodeType.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		//切换手动打码渠道：11人工打码  22联众打码 99 人工+联众
		function changeCodeTypeChannel(id){
			if(confirm("确认切换手动打码渠道吗？")) {
				$("form:first").attr("action", "/trainSystemSetting/changeCodeTypeChannel.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		//切换改退方式：0人工改退  1机器改退
		function changeRefundAndAlert(id){
			if(confirm("确认切换打码方式吗？")) {
				$("form:first").attr("action", "/trainSystemSetting/changeRefundAndAlert.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		
		function changeBuyTime(id){
			if(confirm("确认切换开车前购票时间吗？")) {
				$("form:first").attr("action", "/trainSystemSetting/updateBuytime.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		function changeRefundTime(id){
			if(confirm("确认切换开车后退票时间吗？")) {
				$("form:first").attr("action", "/trainSystemSetting/updateRefundtime.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		//修改预订机器人一次处理订单量
		function changeRapnChannel(id){
			if(confirm("确认修改预订机器人一次处理订单量吗？")) {
				$("form:first").attr("action", "/trainSystemSetting/updateRapnChannel.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		
		//修改预购天数
		function changeBdnChannel(id){
			if(confirm("确认修改预约购票天数吗？")) {
				$("form:first").attr("action", "/trainSystemSetting/updateBdnChannel.do?setting_id=" +id);
				$("form:first").submit();
			}
		}	
		
		//切换去哪打码方式
		function changequnarPlayCode(id) {
			if(confirm("确认切换去哪打码方式吗？")) {
				$("form:first").attr("action", "/trainSystemSetting/changequnarPlayCode.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		
		//切换同程打码方式
		function changetcPlayCode(id) {
			if(confirm("确认切换同程打码方式吗？")) {
				$("form:first").attr("action", "/trainSystemSetting/changetcPlayCode.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		
		//切换艺龙打码方式
		function changeElongPlayCode(id) {
			if(confirm("确认切换艺龙打码方式吗？")) {
				$("form:first").attr("action", "/trainSystemSetting/changeElongPlayCode.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		//切换机器人停用短信通知电话
		function changephoneMsgList(id){
			if(confirm("确认修改吗？")) {
				$("form:first").attr("action", "/trainSystemSetting/changephoneMsgList.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		//切换人工出票占比
		function changeManualZb(id){
			if(!isNaN(document.getElementById("manual_order_zb").value)){ 
				if(confirm("确认修改人工出票占比吗？")){
				$("form:first").attr("action", "/trainSystemSetting/changeManualZb.do?setting_id=" +id);
				$("form:first").submit();
				}
			}else{
			   alert("请在人工出票占比中输入数字类型值");
			}
		}
		//切换人工出票总数
		function changeManualNum(id){
			if(!isNaN(document.getElementById("manual_order_num").value)){ 
				if(confirm("确认修改人工出票总数吗？")){
				$("form:first").attr("action", "/trainSystemSetting/changeManualNum.do?setting_id=" +id);
				$("form:first").submit();
				}
			}else{
			   alert("请在人工出票总数中输入数字类型值");
			}
		}
		
		//修改打码
		function changeCodeSet(urlId) {
			var ch = $("#"+urlId).val();
			if($.trim(ch)==""){
				alert("不能为空！");
				return;
			}
			if(confirm("确认修改吗?")) {
				$("form:first").attr("action", "/trainSystemSetting/changeCodeSet.do?urlId=" + urlId);
				$("form:first").submit();
			}
		}
		//切换打码器版本状态
		function changeCodeSetStatus(urlId) {
			if(confirm("确认切换状态吗？")) {
				$("form:first").attr("action", "/trainSystemSetting/changeCodeSetStatus.do?urlId=" +urlId);
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
  	<form action="/trainSystemSetting/getSystemSetting.do" method="post">
  		
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
		    	<c:if test="${ss.setting_name eq 'code_type_channel'}">
		    		<tr>
		    			<td>手动打码渠道：</td>
				    	<td>
			    			<input type="radio" name="code_type_channel"
			    				<c:if test="${ss.setting_value eq '11'}">
			    					checked
			    				</c:if>
			    				value="11"/>人工打码
			    		<!-- 
			    		 	<input type="radio" name="code_type_channel"
			    				<c:if test="${ss.setting_value eq '22'}">
			    					checked
			    				</c:if>
			    				value="22"/>联众打码
			    			<input type="radio" name="code_type_channel"
			    				<c:if test="${ss.setting_value eq '99'}">
			    					checked
			    				</c:if>
			    				value="99"/>联众+人工打码
			    			<input type="radio" name="code_type_channel"
			    				<c:if test="${ss.setting_value eq '33'}">
			    					checked
			    				</c:if>
			    				value="33"/>打码兔打码
			    		 -->
			    			<input type="radio" name="code_type_channel"
			    				<c:if test="${ss.setting_value eq '44'}">
			    					checked
			    				</c:if>
			    				value="44"/>人工+打码兔打码
			    		</td>
			    		<td><input type="button" class="btn" value="切换" onclick="changeCodeTypeChannel('${ss.setting_id}')"/></td>
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
		    	<c:if test="${ss.setting_name eq 'stop_refundTicket_time'}">
		    		<tr>
		    			<td>开车后退票时间：</td>
				    	<td>
			    			<input type="radio" name="srtt_value"
			    				<c:if test="${ss.setting_value eq '3'}">
			    					checked
			    				</c:if>
			    				value="3"/>3小时
			    			<input type="radio" name="srtt_value"
			    				<c:if test="${ss.setting_value eq '4'}">
			    					checked
			    				</c:if>
			    				value="4"/>4小时
			    			<input type="radio" name="srtt_value"
			    				<c:if test="${ss.setting_value eq '5'}">
			    					checked
			    				</c:if>
			    				value="5"/>5小时
			    				
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
		    	
		    	<c:if test="${ss.setting_name eq 'notify_cp_interface_url'}">
		    		<tr>
		    			<td>${ss.setting_desc}：</td>
		    			<td><input type="text" class="setting_text" name="${ss.setting_id}" id="${ss.setting_id}" value="${ss.setting_value}"/></td>
		    			<td>
		    			<input type="button" class="btn" value="修改" onclick="changeNewURL('${ss.setting_id}')"/>
		    			<c:choose>
		    				<c:when test="${ss.setting_status eq '1'}">
		    					<input type="button" class="btn" value="禁用" onclick="changeNewStatus('${ss.setting_id}')"/>
		    				</c:when>
		    				<c:when test="${ss.setting_status eq '0'}">
		    					<input type="button" class="btn" value="启用" onclick="changeNewStatus('${ss.setting_id}')"/>
		    				</c:when>
		    			</c:choose>
		    			</td>
		    		</tr>
		    	</c:if>
		    	<c:if test="${ss.setting_name eq 'notify_cp_pay_url'}">
		    		<tr>
		    			<td>${ss.setting_desc}：</td>
		    			<td><input type="text" class="setting_text" name="${ss.setting_id}" id="${ss.setting_id}" value="${ss.setting_value}"/></td>
		    			<td>
		    			<input type="button" class="btn" value="修改" onclick="changeNewURL('${ss.setting_id}')"/>
		    			<c:choose>
		    				<c:when test="${ss.setting_status eq '1'}">
		    					<input type="button" class="btn" value="禁用" onclick="changeNewStatus('${ss.setting_id}')"/>
		    				</c:when>
		    				<c:when test="${ss.setting_status eq '0'}">
		    					<input type="button" class="btn" value="启用" onclick="changeNewStatus('${ss.setting_id}')"/>
		    				</c:when>
		    			</c:choose>
		    			</td>
		    		</tr>
		    	</c:if>
		    	
		    	<c:if test="${ss.setting_name eq 'qunar_play_code'}">
		    		<tr>
		    			<td>去哪打码方式：</td>
				    	<td>
			    			<input type="radio" name="qunar_play_code"
			    				<c:if test="${ss.setting_value eq '00'}">
			    					checked
			    				</c:if>
			    				value="00"/>去哪打码
			    			<input type="radio" name="qunar_play_code"
			    				<c:if test="${ss.setting_value eq '11'}">
			    					checked
			    				</c:if>
			    				value="11"/>我们打码
			    			<input type="radio" name="qunar_play_code"
			    				<c:if test="${ss.setting_value eq '22'}">
			    					checked
			    				</c:if>
			    				value="22"/>去哪+我们打码
			    			<input type="radio" name="qunar_play_code"
			    				<c:if test="${ss.setting_value eq '33'}">
			    					checked
			    				</c:if>
			    				value="33"/>打码兔
			    		</td>
			    		<td><input type="button" class="btn" value="切换" onclick="changequnarPlayCode('${ss.setting_id}')"/></td>
		    		</tr>	
		    	</c:if>
		    	
		    	<c:if test="${ss.setting_name eq 'tc_play_code'}">
		    		<tr>
		    			<td>同程打码方式：</td>
				    	<td>
			    			<input type="radio" name="tc_play_code"
			    				<c:if test="${ss.setting_value eq '00'}">
			    					checked
			    				</c:if>
			    				value="00"/>同程打码
			    			<input type="radio" name="tc_play_code"
			    				<c:if test="${ss.setting_value eq '11'}">
			    					checked
			    				</c:if>
			    				value="11"/>同程+我们打码
			    			<input type="radio" name="tc_play_code"
			    				<c:if test="${ss.setting_value eq '22'}">
			    					checked
			    				</c:if>
			    				value="22"/>同程帮我们打码
			    		</td>
			    		<td><input type="button" class="btn" value="切换" onclick="changetcPlayCode('${ss.setting_id}')"/></td>
		    		</tr>	
		    	</c:if>
		    	
		    	<c:if test="${ss.setting_name eq 'elong_play_code'}">
		    	<!-- 
		    	
		    		<tr>
		    			<td>艺龙打码方式：</td>
				    	<td>
			    			<input type="radio" name="elong_play_code"
			    				<c:if test="${ss.setting_value eq '00'}">
			    					checked
			    				</c:if>
			    				value="00"/>艺龙打自己码
			    			<input type="radio" name="elong_play_code"
			    				<c:if test="${ss.setting_value eq '11'}">
			    					checked
			    				</c:if>
			    				value="11"/>我们+艺龙打码
			    			<input type="radio" name="elong_play_code"
			    				<c:if test="${ss.setting_value eq '22'}">
			    					checked
			    				</c:if>
			    				value="22"/>艺龙给我们打码
			    			<input type="radio" name="elong_play_code"
			    				<c:if test="${ss.setting_value eq '33'}">
			    					checked
			    				</c:if>
			    				value="33"/>打码兔打码
			    			<input type="radio" name="elong_play_code"
			    				<c:if test="${ss.setting_value eq '44'}">
			    					checked
			    				</c:if>
			    				value="44"/>我们+打码兔打码

			    		</td>
			    		<td><input type="button" class="btn" value="切换" onclick="changeElongPlayCode('${ss.setting_id}')"/></td>
		    		</tr>	
		    	 -->
		    	</c:if>
		    	
		    	
		    	<c:if test="${ss.setting_name eq 'phone_msg_list'}">
		    		<tr>
		    			<td>机器人停用短信通知：</td>
				    	<td>
			    			<input type="checkbox" name="phone_msg_list"
			    				<c:if test="${fn:contains(ss.setting_value,'15201169346')}">
			    					checked
			    				</c:if>
			    				value="15201169346"/>李海潮
			    			<input type="checkbox" name="phone_msg_list"
			    				<c:if test="${fn:contains(ss.setting_value, '15911165681')}">
			    					checked
			    				</c:if>
			    				value="15911165681"/>郭娜
			    			<input type="checkbox" name="phone_msg_list"
			    				<c:if test="${fn:contains(ss.setting_value, '18611775601')}">
			    					checked
			    				</c:if>
			    				value="18611775601"/>刘毅
			    			<input type="checkbox" name="phone_msg_list"
			    				<c:if test="${fn:contains(ss.setting_value, '18892257687')}">
			    					checked
			    				</c:if>
			    				value="18892257687"/>张俊驰
			    			<input type="checkbox" name="phone_msg_list"
			    				<c:if test="${fn:contains(ss.setting_value, '15801025130')}">
			    					checked
			    				</c:if>
			    				value="15801025130"/>王东霓
			    			<input type="checkbox" name="phone_msg_list"
			    				<c:if test="${fn:contains(ss.setting_value, '18611339439')}">
			    					checked
			    				</c:if>
			    				value="18611339439"/>马天马
			    		</td>
			    		<td><input type="button" class="btn" value="切换" onclick="changephoneMsgList('${ss.setting_id}')"/></td>
		    		</tr>	
		    	</c:if>
		    	
		    	<c:if test="${ss.setting_name eq 'manual_order_zb'}">
		    		<tr>
		    			<td>人工出票占比：</td>
		    			<td><input type="text" class="manual_order_zb" name="manual_order_zb" id="manual_order_zb" value="${ss.setting_value}"/></td>
		    			<td>
		    			<input type="button" class="btn" value="修改" onclick="changeManualZb('${ss.setting_id}')"/>
		    			<c:choose>
		    				<c:when test="${ss.setting_status eq '1'}">
		    					<input type="button" class="btn" value="禁用" onclick="changeNewStatus('${ss.setting_id}')"/>
		    				</c:when>
		    				<c:when test="${ss.setting_status eq '0'}">
		    					<input type="button" class="btn" value="启用" onclick="changeNewStatus('${ss.setting_id}')"/>
		    				</c:when>
		    			</c:choose>
		    			</td>
		    		</tr>
		    	</c:if>
		    	<c:if test="${ss.setting_name eq 'manual_order_num'}">
		    		<tr>
		    			<td>人工出票最大数：</td>
		    			<td><input type="text" class="manual_order_num" name="manual_order_num" id="manual_order_num" value="${ss.setting_value}"/></td>
		    			<td>
		    			<input type="button" class="btn" value="修改" onclick="changeManualNum('${ss.setting_id}')"/>
		    			<c:choose>
		    				<c:when test="${ss.setting_status eq '1'}">
		    					<input type="button" class="btn" value="禁用" onclick="changeNewStatus('${ss.setting_id}')"/>
		    				</c:when>
		    				<c:when test="${ss.setting_status eq '0'}">
		    					<input type="button" class="btn" value="启用" onclick="changeNewStatus('${ss.setting_id}')"/>
		    				</c:when>
		    			</c:choose>
		    			</td>
		    		</tr>
		    	</c:if>
		    	
		    	<c:if test="${ss.setting_name eq 'code_version'}">
		    		<tr>
		    			<td>${ss.setting_desc}：</td>
		    			<td><input type="text" class="setting_text" name="${ss.setting_id}" id="${ss.setting_id}" value="${ss.setting_value}"/></td>
		    			<td>
		    			<input type="button" class="btn" value="修改" onclick="changeCodeSet('${ss.setting_id}')"/>
		    			<c:choose>
		    				<c:when test="${ss.setting_status eq '1'}">
		    					<input type="button" class="btn" value="禁用" onclick="changeCodeSetStatus('${ss.setting_id}')"/>
		    				</c:when>
		    				<c:when test="${ss.setting_status eq '0'}">
		    					<input type="button" class="btn" value="启用" onclick="changeCodeSetStatus('${ss.setting_id}')"/>
		    				</c:when>
		    			</c:choose>
		    			</td>
		    		</tr>
		    	</c:if>
		    	<c:if test="${ss.setting_name eq 'code_time_limit'}">
		    		<tr>
		    			<td>${ss.setting_desc}：</td>
		    			<td><input type="text" class="setting_text" name="${ss.setting_id}" id="${ss.setting_id}" value="${ss.setting_value}"/></td>
		    			<td>
		    			<input type="button" class="btn" value="修改" onclick="changeCodeSet('${ss.setting_id}')"/>
		    			</td>
		    		</tr>
		    	</c:if>
		    	<c:if test="${ss.setting_name eq 'code_back_result'}">
		    		<tr>
		    			<td>${ss.setting_desc}：</td>
		    			<td><input type="text" class="setting_text" name="${ss.setting_id}" id="${ss.setting_id}" value="${ss.setting_value}"/></td>
		    			<td>
		    			<input type="button" class="btn" value="修改" onclick="changeCodeSet('${ss.setting_id}')"/>
		    			</td>
		    		</tr>
		    	</c:if>
		    </c:forEach>
	    </table>
	    <br/>
	    <p>
			<input type="button" value="操作日志" class="btn" id="rizhi" onclick="opt_rizhi()" />
			<input type="button" value="查看返回日志" class="btn" id="fanhui" onclick="return_log()" />
		</p>    
    </form>
    </div>
    </div>
  </body>
</html>
