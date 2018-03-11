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
    <base href="<%=basePath%>"></base>
    
    <title>系统设置管理</title>
    <link rel="stylesheet" href="/css/back_style.css" type="text/css" />
	<script type="text/javascript" src="/js/jquery.js"></script>
	<script type="text/javascript" src="/js/datepicker/WdatePicker.js"></script>
	<script language="javascript" src="/js/layer/layer.js"></script>
	<script language="javascript" src="/js/mylayer.js"></script>
	<script type="text/javascript">
		function changeChannel(channel){
			if(confirm("确认切换吗？")){
				$("form:first").attr("action", "/appSystemSetting/updateInterfaceChannel.do?channelId=" + channel);
				$("form:first").submit();
			}
		}
		function changeBxChannel(id) {
			if(confirm("确认切换保险渠道吗？")) {
				$("form:first").attr("action", "/appSystemSetting/updateBxChannel.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		function changeMsgChannel(id) {
			if(confirm("确认切换短信渠道吗？")) {
				$("form:first").attr("action", "/appSystemSetting/updateMsgChannel.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
		function changeTtlChannel(id){
			if(confirm("确认切换默认乘车日期吗？")) {
				$("form:first").attr("action", "/appSystemSetting/updateTtlChannel.do?setting_id=" +id);
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
				$("form:first").attr("action", "/appSystemSetting/updateStaChannel.do?setting_id=" +id);
				$("form:first").submit();
			}
		}

		//系统预定：启用和禁用
		function changeSystemBookStatus(id,setting_status) {
			if(confirm("确认改变系统预定功能吗？")) {
				$("form:first").attr("action", "/appSystemSetting/changeSysBook.do?setting_id=" +id + "&setting_status=" + setting_status);
				$("form:first").submit();
			}
		}
		//修改12306新接口地址
		function changeNewURL(urlId) {
			var ch = $("#"+urlId).val();
			if($.trim(ch)==""){
				alert("12306新接口地址不能为空！");
				return;
			}
			if(confirm("确认修改12306新接口地址吗?")) {
				$("form:first").attr("action", "/appSystemSetting/updateInterface12306NewRUL.do?urlId=" + urlId);
				$("form:first").submit();
			}
		}
		//切换12306新接口地址
		function changeNewStatus(urlId) {
			if(confirm("确认切换12306新接口地址吗？")) {
				$("form:first").attr("action", "/appSystemSetting/changeNewURLStatus.do?urlId=" +urlId);
				$("form:first").submit();
			}
		}
		//删除12306新接口地址
		function deleteNewUrl(urlId) {
			if(confirm("确认删除12306新接口地址吗？")) {
				$("form:first").attr("action", "/appSystemSetting/deleteNewURL.do?urlId=" +urlId);
				$("form:first").submit();
			}
		}
		//增加12306新接口地址
		function addNewURL() {
			$("form:first").attr("action","/appSystemSetting/turnToAddNewURLPage.do");
			$("form:first").submit();
		}
		function opt_rizhi(){
			var url="/appSystemSetting/querySystemSetList.do";
			showlayer('操作日志',url,'950px','600px')
		}

		function changeBuyTime(id){
			if(confirm("确认切换开车前购票时间吗？")) {
				$("form:first").attr("action", "/appSystemSetting/updateBuytime.do?setting_id=" +id);
				$("form:first").submit();
			}
		}
	</script>
	<style>
		.outer{
			width:1000px;
			padding:0 20px;
		}
		.book_manage{width:1000px;margin:40px 20px;}
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
  	<form action="/appSystemSetting/getSystemSetting.do" method="post">
  		
	  	<table>
  			<tr>
	    		<th style="width: 130px;">key</th>
	    		<th style="width: 300px;">内容</th>
	    		<th>操作</th>
	    	</tr>
		    <c:forEach items="${systemSetting}" var="ss">
		    <c:set value="ss" scope="request" var="ss"/>
		    	<c:if test="${ss.setting_name eq 'bx_channel'}">
		    		<tr>
		    			<td>保险渠道：</td>
		    			<td>
		    				<input type="radio" name="bx_value" id="bx_value1"
			    				<c:if test="${ss.setting_value eq '1'}">
			    					checked
			    				</c:if>
			    				value="1"/><label for="bx_value1">快保</label>&nbsp;&nbsp;
			    			<input type="radio" name="bx_value" id="bx_value2"
			    				<c:if test="${ss.setting_value eq '2'}">
			    					checked
			    				</c:if>
			    				value="2"/><label for="bx_value2">合众</label>
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
		    				<input type="radio" name="msg_value" id="msg_value1"
			    				<c:if test="${ss.setting_value eq '1'}">
			    					checked
			    				</c:if>
			    				value="1"/><label for="msg_value1">19e</label>&nbsp;&nbsp;
			    			<input type="radio" name="msg_value" id="msg_value2"
			    				<c:if test="${ss.setting_value eq '2'}">
			    					checked
			    				</c:if>
			    				value="2"/><label for="msg_value2">鼎鑫亿动</label>&nbsp;&nbsp;
			    			<input type="radio" name="msg_value" id="msg_value3"
			    				<c:if test="${ss.setting_value eq '3'}">
			    					checked
			    				</c:if>
			    				value="3"/><label for="msg_value3">企信通</label>
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
		    				<input type="radio" name="ttl_value" id="ttl_value0"
			    				<c:if test="${ss.setting_value eq '0'}">
			    					checked
			    				</c:if>
			    				value="0"/><label for="ttl_value0">当天</label>&nbsp;
			    			<input type="radio" name="ttl_value" id="ttl_value1"
			    				<c:if test="${ss.setting_value eq '1'}">
			    					checked
			    				</c:if>
			    				value="1"/><label for="ttl_value1">1天</label>&nbsp;
			    			<input type="radio" name="ttl_value" id="ttl_value2"
			    				<c:if test="${ss.setting_value eq '2'}">
			    					checked
			    				</c:if>
			    				value="2"/><label for="ttl_value2">2天</label>&nbsp;
			    			<input type="radio" name="ttl_value" id="ttl_value3"
			    				<c:if test="${ss.setting_value eq '3'}">
			    					checked
			    				</c:if>
			    				value="3"/><label for="ttl_value3">3天</label>&nbsp;
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
			    			<input type="radio" name="btt_value" id="btt_value3"
			    				<c:if test="${ss.setting_value eq '3'}">
			    					checked
			    				</c:if>
			    				value="3"/><label for="btt_value3">3小时</label>
			    			<input type="radio" name="btt_value" id="btt_value4"
			    				<c:if test="${ss.setting_value eq '4'}">
			    					checked
			    				</c:if>
			    				value="4"/><label for="btt_value4">4小时</label>
			    			<input type="radio" name="btt_value" id="btt_value5"
			    				<c:if test="${ss.setting_value eq '5'}">
			    					checked
			    				</c:if>
			    				value="5"/><label for="btt_value5">5小时</label>
			    			<input type="radio" name="btt_value" id="btt_value6"
			    				<c:if test="${ss.setting_value eq '6'}">
			    					checked
			    				</c:if>
			    				value="6"/><label for="btt_value6">6小时</label>
			    		</td>
						<td>
		    				<input type="button" class="btn" value="切换" onclick="changeBuyTime('${ss.setting_id}')"/>
		    			</td>		    		
		    		</tr>	
		    	</c:if>
		    	<!-- 
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
			    		</td>
						<td>
		    				<input type="button" class="btn" value="切换" onclick="changeRefundTime('${ss.setting_id}')"/>
		    			</td>		    		
		    		</tr>	
		    	</c:if>
		    	 -->
		    	 <c:if test="${ss.setting_name eq 'system_book'}">
		    		<tr>
		    			<td>终止前台预订功能：</td>
				    	<td>
			    			<c:forEach items="${systemBooks }" var="s" varStatus="index">
								
									<input type="checkbox" id="system_book${index.count }" name="system_book" value="${s.key }"
									<c:if test="${fn:contains(ss.setting_value, s.key ) }">checked="checked"</c:if> />
									<label for="system_book${index.count }">${s.value }</label>&nbsp;
							</c:forEach>
			    		</td>
			    		<td>
			    			<input type="button" class="btn" value="启用" onclick="changeSystemBookStatus('${ss.setting_id}','00')"/>&nbsp;&nbsp;
			    			<input type="button" class="btn" value="禁用" onclick="changeSystemBookStatus('${ss.setting_id}','11')"/>
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
