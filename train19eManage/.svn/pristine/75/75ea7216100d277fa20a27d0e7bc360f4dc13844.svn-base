<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo"%>
<%@ page import="com.l9e.transaction.vo.PageVo"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<html>
<head>
<base href="<%=basePath%>"></base>

<title>代理IP管理页面</title>
<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
<meta http-equiv="pragma" content="no-cache"></meta>
<meta http-equiv="cache-control" content="no-cache"></meta>
<meta http-equiv="expires" content="0"></meta>
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"></meta>
<meta http-equiv="description" content="This is my page"></meta>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/datepicker/WdatePicker.js"></script>
<script language="javascript" src="/js/layer/layer.js"></script>
<script language="javascript" src="/js/mylayer.js"></script>
<script language="javascript" src="/js/json2.js"></script>

<script type="text/javascript">

$(function(){
	var queryCheckAllFlag =  $("#queryCheckAllFlag").val();
	//alert(queryCheckAllFlag)
	if(queryCheckAllFlag == "" || queryCheckAllFlag == null){
		 $("#queryCheckAllFlag").val("8");
	}else if(queryCheckAllFlag == "9"){
		 $("#queryCheckAll").attr('checked', true);
		 $("#queryCheckDisable").attr('checked', true);
		 $("#queryCheckEnable").attr('checked', true);
	}else if(queryCheckAllFlag == "0"){
		 $("#queryCheckDisable").attr('checked', true);
	}else if(queryCheckAllFlag == "1"){
		 $("#queryCheckEnable").attr('checked', true);
	}
});

//新增代理IP
function gotoAddNewProxyIpPage() {
	$("form:first").attr("action","/ip/gotoAddNewProxyIp.do");
	$("form:first").submit();
}


//全选操作
function checkboxAll(){
	var proxyIds = document.getElementsByName("proxy_id");
	var checkAllFlag = document.getElementById("checkAllFlag").value;
	if(checkAllFlag == 0 ){
		for(var i = 0 ; i < proxyIds.length ; i++){
			proxyIds[i].checked = true ;
		}
		document.getElementById("checkAllFlag").value = 1;
	}else if(checkAllFlag == 1){
		for(var i = 0 ; i < proxyIds.length ; i++){
			proxyIds[i].checked = false ;
		}
		document.getElementById("checkAllFlag").value = 0;
	}
}

function batchHandle(handleType){
	var proxyIps = $("input[name='proxy_id']:checkbox:checked");
	var length = proxyIps.length
	if(length == 0){
		alert("没有选中符合条件的订单!");
		return false;
	}
	
	var data = {}
	var arr = [];
	proxyIps.each(function(){
		var proxyip = $(this).val();
		arr.push(parseInt(proxyip));
	});
	data.autoIds = arr;
	
	if(handleType == "Renew"){
		var endTime = prompt("请输入到期日期!格式:yyyy-MM-dd HH:mm:ss","")
		endTime = $.trim(endTime);
		if (endTime != null && endTime != ""){
			data.endTime = endTime;
		}else{
			alert("输入日期格式不合法!");
			return false;
		}
	}else if( handleType == "Disable"){
		data.status = 0;
	}else if( handleType == "Enable"){
		data.status = 1;
	}
	
	var url = "/ip/batch"+handleType+".do";
	
	$.post(url,JSON.stringify(data),function(result){
		$("form:first").submit();
	},"json");
}

function _queryCheckAll(obj){
	if($(obj).is(':checked')) {
		$("input[name='queryCheck']").each(function(){
			$(this).attr('checked', true);
		});
		$("#queryCheckAllFlag").val("9");
	}else{
		$("input[name='queryCheck']").each(function(){
			$(this).attr('checked', false);
		});
		$("#queryCheckAllFlag").val("8");
	}
}

function _queryCheckEnable(obj){
	var isEnable = $(obj).is(':checked');
	var isDisable = $("#queryCheckDisable").is(':checked');
	if(isEnable){
		if(isDisable){
			 $("#queryCheckAll").attr('checked', true);
			 $("#queryCheckAllFlag").val("9");
		}else{
			$("#queryCheckAll").attr('checked', false);
			$("#queryCheckAllFlag").val("1");
		}
	}else{
		if(isDisable){
			 $("#queryCheckAll").attr('checked', false);
			 $("#queryCheckAllFlag").val("0");
		}else{
			$("#queryCheckAllFlag").val("8");
			$("#queryCheckAll").attr('checked', false);
		}
	}
	//alert($("#queryCheckAllFlag").val())
}

function _queryCheckDisable(obj){
	var isDisable= $(obj).is(':checked');
	var isEnable = $("#queryCheckEnable").is(':checked');
	if(isDisable){
		if(isEnable){
			 $("#queryCheckAll").attr('checked', true);
			 $("#queryCheckAllFlag").val("9");
		}else{
			$("#queryCheckAll").attr('checked', false);
			$("#queryCheckAllFlag").val("0");
		}
	}else{
		if(isEnable){
			 $("#queryCheckAll").attr('checked', false);
			 $("#queryCheckAllFlag").val("1");
		}else{
			 $("#queryCheckAllFlag").val("8");
			 $("#queryCheckAll").attr('checked', false);
		}
	}
	//alert($("#queryCheckAllFlag").val())
}

</script>
<style>
tr:hover{background:#ecffff;}
#hint {
	width: 700px;
	border: 1px solid #C1C1C1;
	background: #F0FAC1;
	position: fixed;
	z-index: 9;
	padding: 6px;
	line-height: 17px;
	text-align: left;
	overflow: auto;
}
</style>
</head>
<body>
	<div class="proxy_manage oz" >
		<form action="/ip/queryProxyIpList.do" method="post">
			<ul class="order_num oz" style="margin-top: 20px;">
				<li style="width: 5%">订单号:</li>
				<li style="width: 30%">
					<input type="text" class="text" name="orderId" value="${orderId}" />
				</li>
				<li style="width: 5%">IP:</li>
				<li style="width: 30%">
					<input type="text" class="text" name="ip" value="${ip}" />
				</li>
			</ul>
			
			<ul class="order_num oz" style="margin-top: 10px;">
				<li style="width: 5%">创建时间:</li>
				<li style="width: 30%">
					<input type="text" class="text"name="beginCreateTime" readonly="readonly" value="${beginCreateTime}"
					onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})" />
					- 
					<input type="text" class="text" name="endCreateTime" readonly="readonly" value="${endCreateTime}"
					onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})" />
				</li>
				<li style="width: 5%">到期时间:</li>
				<li style="width: 30%">
					<input type="text" class="text" name="beginEndTime" readonly="readonly" value="${beginEndTime}"
					onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
					- 
					<input type="text" class="text" name="endEndTime" readonly="readonly" value="${endEndTime}"
					onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
				</li>
			</ul>
			
			<ul class="order_num oz" style="margin-top: 10px;">
				<li style="width: 5%">代理IP状态:</li>
				<li >
					<label>
					<input type="checkbox" name="queryCheck" id="queryCheckAll" onclick="_queryCheckAll(this)" style="float:left;white-space:nowrap;width:25px;"/> 
					全部</label>
				</li>
				<li>
					<label>
					<input type="checkbox" name="queryCheck" id="queryCheckEnable" onclick="_queryCheckEnable(this)" style="float:left;white-space:nowrap;width:25px;" /> 
					启用 </label>
				</li>
				<li>
					<label>
					<input type="checkbox" name="queryCheck" id="queryCheckDisable" onclick="_queryCheckDisable(this)" style="float:left;white-space:nowrap;width:25px;"/> 
					禁用</label>
				</li>
			</ul>
			<input type="hidden" id="queryCheckAllFlag" name="queryCheckAllFlag" value="${queryCheckAllFlag}" />
			<p style="margin-top: 20px;">
				<input type="submit" value="查 询" class="btn" />
				&nbsp;&nbsp;
				<input type="button" value="添加代理IP" class="btn" onclick="gotoAddNewProxyIpPage()" />
				&nbsp;&nbsp;
				<input type="button" value="批量禁用" class="btn" onclick="batchHandle('Disable')" /> 
				&nbsp;&nbsp; 
				<input type="button" value="批量启用" class="btn" onclick="batchHandle('Enable')" />
				&nbsp;&nbsp;
				<input type="button" value="批量续费" class="btn" onclick="batchHandle('Renew')" />
				&nbsp;&nbsp;
				<input type="button" value="批量删除" class="btn" onclick="batchHandle('Delete')" />
			</p>
			<div id="hint" class="pub_con" style="display:none"></div>
			<input type="hidden" name="isShow" value="1"/>
			
			<table style="margin-left: 20px;">
				<tr style="background: #EAEAEA;">
					<th style="width:50px;"><label>全选 <br/><input type="checkbox" id="checkAll" name="checkAll" onclick="checkboxAll()"/></label></th>
					<th>序号</th> 
					<th>IP</th> 
					<th>端口</th>
					<th>账户名</th>
					<th>密码</th>
					<th>订单号</th>
					<th>创建时间</th>
					<th>到期时间</th>
					<th>状态</th>
					<th>操作</th>
				</tr>
				<c:forEach var="list" items="${proxyIpList}" varStatus="idx">
					<tr>
						<td><input type="checkbox" id="proxy_id" name="proxy_id" value="${list.autoId}"/></td> 
						<td>${idx.index+1}</td>
						<td>${list.ip}</td>
						<td>${list.port}</td>
						<td>${list.username}</td>
						<td>${list.password}</td>
						<td>${list.orderId}</td>
						<td><fmt:formatDate value="${list.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td><fmt:formatDate value="${list.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td>
							<c:if test="${list.status == 1 }">启用</c:if>
							<c:if test="${list.status == 0 }">停用</c:if>
						</td>
						<td>
							<c:if test="${list.status == 0 }"><a href="/ip/enable.do?autoId=${list.autoId}">启用</a></c:if>
							<c:if test="${list.status == 1 }"><a href="/ip/disable.do?autoId=${list.autoId}">停用</a></c:if>
							|
							<a href="/ip/delete.do?autoId=${list.autoId}">删除</a>
						</td>
					</tr>
				</c:forEach>
			</table>
			
			<!-- 全选变量 -->
			<input type="hidden" id="checkAllFlag" name="checkAllFlag" value="0" />
			
			<jsp:include page="/pages/common/paging_2.jsp" /> 
		</form>
	</div>
</body>



</html>
