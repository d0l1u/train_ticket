<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo"%>
<%@ page import="com.l9e.transaction.vo.PageVo"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html>
<head>
<base href="<%=basePath%>"></base>

<title>机器管理new页面</title>
<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
<meta http-equiv="pragma" content="no-cache"></meta>
<meta http-equiv="cache-control" content="no-cache"></meta>
<meta http-equiv="expires" content="0"></meta>
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"></meta>
<meta http-equiv="description" content="This is my page"></meta>
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/datepicker/WdatePicker.js"></script>
<script language="javascript" src="/js/layer/layer.js"></script>
<script language="javascript" src="/js/mylayer.js"></script>
<script language="javascript" src="/js/json2.js"></script>
<script type="text/javascript">
		<%
		PageVo pageObject = (PageVo) request.getAttribute("pageBean");
		int pageIndex = pageObject.getPageIndex();%>
	
	$().ready(function() { 
		statusColor();
	});
	
	 function queryInfo(worker_id){
		var url="/robotSetNew/queryRobotSetInfo.do?worker_id="+worker_id+"&version="+new Date();
		showlayer('明细:',url,'900px','600px');
	 }
	 
	 //验证码
	 function queryVerificationCode(worker_id){
		var url="/robotSetNew/queryRobotCodeInfo.do?worker_id="+worker_id+"&version="+new Date();
		showlayer('验证码:',url,'900px','600px');
	 }
	 
	 
	//增加机器人
		function addNewRobotURL() {
			$("form:first").attr("action","/robotSetNew/turnToAddNewRobotURLPage.do?pageIndex="+<%=pageIndex%>);
			$("form:first").submit();
		}
	 
	 //选中变色
     function statusColor(){
		var a=document.getElementsByName("worker_type_a");
		var check = document.getElementsByName("worker_type");
			for(var i=0;i<a.length;i++){
				a[i].style.color="grey";
				if(check[i].checked){
				a[i].style.color="#FEFEFB";
				a[i].style.backgroundColor="#2C99FF";
				//a[i].style.background='url(/images/pro.png) -90px no-repeat';
				}
		}
	}
	
	function queryByType(type){
		var check = document.getElementsByName("worker_type");
		for(var i=0;i<check.length;i++){
					check[i].checked = 0;
				if(check[i].value == type){
					check[i].checked = 1;
				}
			}
		$("form:first").attr("action","/robotSetNew/queryRobotSetList.do");
		$("form:first").submit();
	}
	
	function changeStatus(worker_id,worker_status){
		var url = "";
		if(worker_status == '22'){
		var stop_reason = document.getElementById("stop_reason_"+worker_id).value;
			url ="/robotSetNew/changeStatus.do?worker_id="+worker_id+"&worker_status="+worker_status+"&stop_reason="+stop_reason+"&version="+new Date();
			if(stop_reason == ''){
				alert("停用原因不能为空！");
				return;
			}
		}else{
			url ="/robotSetNew/changeStatus.do?worker_id="+worker_id+"&worker_status="+worker_status+"&version="+new Date();
		}
		 $.get(url,function(data){
			if(data == "success"){
				$("form:first").attr("action", "/robotSetNew/queryRobotSetList.do?pageIndex="+<%=pageIndex%>);
				$("form:first").submit();
			}
		});
	}
	
	
	function deleteByWorkId(worker_id,worker_name){
		var url ="/robotSetNew/deleteByWorkId.do?worker_id="+worker_id+"&version="+new Date();
		if(confirm("确认删除【"+worker_name+"】吗？")) {
		 $.get(url,function(data){
			if(data == "success"){
				$("form:first").attr("action", "/robotSetNew/queryRobotSetList.do?pageIndex="+<%=pageIndex%>
	);
					$("form:first").submit();
				}
			});
		}
	}

	//鼠标悬浮于“明细”，显示该订单的操作日志
	var heightDiv = 0;
	function showdiv(worker_id) {
		var oSon = window.document.getElementById("hint");
		if (oSon == null)
			return;
		with (oSon) {
			$
					.ajax({
						url : "/robotSetNew/queryWorkerLog.do?worker_id="
								+ worker_id,
						type : "POST",
						cache : false,
						dataType : "json",
						async : true,
						success : function(data) {
							if (data == "" || data == null) {
								return false;
							}
							var size = data.length;
							heightDiv = 0;
							for (var i = 0; i < size; i++) {
								var index = (parseInt(i) + 1);
								if ($("#index_" + index).innerText != index) {
									$("#historyTable")
											.append(
													"<tr line-height='15px'align='center' ><td id='index_'"+index+"''>"
															+ index
															+ "</td><td align='left'   style='word-break:break-all;'>"
															+ data[i].content
															+ "</td>"
															+ "<td>"
															+ data[i].create_time
															+ "</td><td>"
															+ data[i].opt_person
															+ "</td></tr>");
									if (data[i].content.length > 44) {
										heightDiv = heightDiv + 30;
									} else {
										heightDiv = heightDiv + 15;
									}
								}
							}
						}
					});
			innerHTML = historyDiv.innerHTML;
			style.display = "block";
			heightDiv = heightDiv + 106;//106为div中表格边距以及表头的高度86+20
			style.pixelLeft = window.event.clientX
					+ window.document.body.scrollLeft - 750;
			style.pixelTop = window.event.clientY
					+ window.document.body.scrollTop - heightDiv;
		}
	}
	//鼠标离开“明细”，隐藏该订单的操作日志
	function hidediv() {
		var oSon = window.document.getElementById("hint");
		if (oSon == null)
			return;
		oSon.style.display = "none";
	}

	//全选
	function selectAll() {
		var checklist = document.getElementsByName("worker_status");
		if (document.getElementById("controlAll").checked) {
			for (var i = 0; i < checklist.length; i++) {
				checklist[i].checked = 1;
			}
		} else {
			for (var i = 0; i < checklist.length; i++) {
				checklist[i].checked = 0;
			}
		}
	}

	//全选
	function selectAllReason() {
		var checklist = document.getElementsByName("stop_reason");
		if (document.getElementById("controlAllReason").checked) {
			for (var i = 0; i < checklist.length; i++) {
				checklist[i].checked = 1;
			}
		} else {
			for (var i = 0; i < checklist.length; i++) {
				checklist[i].checked = 0;
			}
		}
	}
	//全选
	function selectAllVendor() {
		var checklist = document.getElementsByName("worker_vendor");
		if (document.getElementById("controlAllVendor").checked) {
			for (var i = 0; i < checklist.length; i++) {
				checklist[i].checked = 1;
			}
		} else {
			for (var i = 0; i < checklist.length; i++) {
				checklist[i].checked = 0;
			}
		}
	}
	
	//全选
	function selectAllRegion() {
		var checklist = document.getElementsByName("worker_region");
		if (document.getElementById("controlAllRegion").checked) {
			for (var i = 0; i < checklist.length; i++) {
				checklist[i].checked = 1;
			}
		} else {
			for (var i = 0; i < checklist.length; i++) {
				checklist[i].checked = 0;
			}
		}
	}
	//全选
	function selectAllLanguage() {
		var checklist = document.getElementsByName("worker_language");
		if (document.getElementById("controlAllLanguage").checked) {
			for (var i = 0; i < checklist.length; i++) {
				checklist[i].checked = 1;
			}
		} else {
			for (var i = 0; i < checklist.length; i++) {
				checklist[i].checked = 0;
			}
		}
	}

	//全选操作
	function checkChnRetRuleAll() {
		var worker_id = document.getElementsByName("worker_id");
		var tag1 = document.getElementById("tag1").value;
		if (tag1 == 0) {
			for (var i = 0; i < worker_id.length; i++) {
				worker_id[i].checked = true;
			}
			document.getElementById("tag1").value = 1;
		} else if (tag1 == 1) {
			for (var i = 0; i < worker_id.length; i++) {
				worker_id[i].checked = false;
			}
			document.getElementById("tag1").value = 0;
		}
	}

	//批量修改
	function changeWorkerStatus(type) {
		var str = "";
		if (type == "22")
			str = "停用";
		if (type == "00")
			str = "启用";

		var workerIdStr = "";
		var workerIdNum = 0;
		$("input[name='worker_id']:checkbox:checked").each(function() { //遍历被选中CheckBox元素的集合 得到Value值
			var worker_id = $(this).val();
			workerIdStr += worker_id + ",";
			workerIdNum++;
		});
		if (workerIdNum == 0) {
			alert("请选择需要" + str + "的机器人!");
			return false;
		}
		var stopReason = document.getElementById("stopReason").value;
		workerIdStr = workerIdStr.substring(0, workerIdStr.length - 1);
		var url = "/robotSetNew/changeWorkerStatus.do?workerIdStr="
				+ workerIdStr + "&worker_status=" + type + "&version="
				+ new Date();
		if (type == "22" && stopReason == "") {
			alert("请选择批量停用原因!");
			return false;
		} else {
			url = "/robotSetNew/changeWorkerStatus.do?workerIdStr="
					+ workerIdStr + "&worker_status=" + type + "&stop_reason="
					+ stopReason + "&version=" + new Date();
		}
		if (confirm("确认批量" + str + "吗 ？")) {
			$.get(url, function(data) {
				if (data == "success") {
					$("form:first").submit();
				}
			});
		}
	}

	//批量更换支付宝账号
	function changeAlipayAccount() {

		var workerIdStr = "";
		var workerIdNum = 0;
		$("input[name='worker_id']:checkbox:checked").each(function() { //遍历被选中CheckBox元素的集合 得到Value值
			var worker_id = $(this).val();
			workerIdStr += worker_id + ",";
			workerIdNum++;
		});
		if (workerIdNum <= 1) {
			alert("请至少选择两个需要更换的支付宝账号!");
			return false;
		}
		workerIdStr = workerIdStr.substring(0, workerIdStr.length - 1);
		var url = "/robotSetNew/changeAlipayAccount.do?workerIdStr="
				+ workerIdStr + "&version=" + new Date();
		if (confirm("确认批量更换支付宝账号吗 ？")) {
			$.get(url, function(data) {
				if (data == "success") {
					$("form:first").submit();
				}
			});
		}
	}

	//批量修改
</script>
<style>
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
/*.dataTable{width:100%;border:1px solid #999;}
 .dataTable th,.dataTable td{word-break: keep-all; white-space:nowrap;} */
</style>
</head>
<body>
	<div></div>
	<div class="book_manage oz" style="width:1390px">
		<form action="/robotSetNew/queryRobotSetList.do" method="post"
			name="myform" id="myform">
			<div style="margin-top:30px;"></div>
			<ul class="order_num oz" style="margin-top: 10px;">
				<li>机器名称：&nbsp;&nbsp; <input name="worker_name"
					id="worker_name" value="${worker_name }" />
				</li>
			</ul>
			<ul class="order_state oz">
				<li>&nbsp;&nbsp;&nbsp;机器状态：</li>
				<li><input type="checkbox" onclick="selectAll()"
					name="controlAll" style="float:left;white-space:nowrap;width:25px;"
					id="controlAll" /> <label for="controlAll">全部</label></li>
				<c:forEach items="${workerStatus }" var="i" varStatus="index">
					<li><input type="checkbox" id="worker_status${index.count }"
						name="worker_status" value="${i.key }"
						style="float:left;white-space:nowrap;width:25px;"
						<c:if test="${fn:contains(workerStatusStr, i.key ) }">checked="checked"</c:if> />
						<label for="worker_status${index.count }"> ${i.value } </label></li>
				</c:forEach>

			</ul>
			<ul class="order_state oz">
				<li>&nbsp;&nbsp;&nbsp;机器语言：</li>
				<li><input type="checkbox" onclick="selectAllLanguage()"
					name="controlAllLanguage"
					style="float:left;white-space:nowrap;width:25px;"
					id="controlAllLanguage" /> <label for="controlAllLanguage">全部</label></li>
				<c:forEach items="${worker_languageList}" var="i" varStatus="index">
					<li><input type="checkbox" id="worker_language${index.count }"
						name="worker_language" value="${i.key }"
						style="float:left;white-space:nowrap;width:25px;"
						<c:if test="${fn:contains(workerLanguageStr, i.key ) }">checked="checked"</c:if> />
						<label for="worker_language${index.count }"> ${i.value } </label></li>
				</c:forEach>
			</ul>
			<ul class="order_state oz">
				<li>&nbsp;&nbsp;&nbsp;机器提供商：</li>
				<li><input type="checkbox" onclick="selectAllVendor()"
					name="controlAllVendor"
					style="float:left;white-space:nowrap;width:25px;"
					id="controlAllVendor" /> <label for="controlAllVendor">全部</label></li>
				<c:forEach items="${worker_vendorList}" var="i" varStatus="index">
					<li><input type="checkbox" id="worker_vendor${index.count }"
						name="worker_vendor" value="${i.key }"
						style="float:left;white-space:nowrap;width:25px;"
						<c:if test="${fn:contains(workerVendorStr, i.key ) }">checked="checked"</c:if> />
						<label for="worker_vendor${index.count }"> ${i.value } </label></li>
				</c:forEach>
			</ul>
	
			<dl class="order_state oz">
				<dt style="float:left;padding-left:38px;line-height:45px;padding-bottom: 1px;">
				&nbsp;&nbsp;机器区域：
				</dt>
				<dd style="float:left;width:910px;">
				<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
				<input type="checkbox" onclick="selectAllRegion()"
					name="controlAllRegion"
					style="float:left;white-space:nowrap;width:25px;"
					id="controlAllRegion" /> <label for="controlAllRegion">全部</label></div>		
				<c:forEach items="${worker_regionList}" var="i" varStatus="index">
					<div class="ser-item" style="float:left;white-space:nowrap;width:110px;line-height:20px;">
						<input type="checkbox" id="worker_region${index.count}"
							name="worker_region" value="${i.key}"
							style="float:left;white-space:nowrap;width:25px;"
							<c:if test="${fn:contains(workerRegionStr, i.key ) }">checked="checked"</c:if> />
						<label for="worker_region${index.count }"> ${i.value } </label>
					</div>
				</c:forEach>
				</dd>
			</dl>
			
			<ul class="order_state oz">
				<li>&nbsp;&nbsp;&nbsp;停用原因：</li>
				<li><input type="checkbox" onclick="selectAllReason()"
					name="controlAllReason"
					style="float:left;white-space:nowrap;width:25px;"
					id="controlAllReason" /> <label for="controlAllReason">全部</label></li>
				<c:forEach items="${stopReason }" var="i" varStatus="index">
					<li><input type="checkbox" id="stop_reason${index.count }"
						name="stop_reason" value="${i.key }"
						style="float:left;white-space:nowrap;width:25px;"
						<c:if test="${fn:contains(stopReasonStr, i.key ) }">checked="checked"</c:if> />
						<label for="stop_reason${index.count }"> ${i.value } </label></li>
				</c:forEach>
			</ul>
			<div style="padding-left: 40px;">
				<c:forEach items="${workerType }" var="s" varStatus="index">
					<div style="float:left;white-space:nowrap;padding-left:10px;">
						<input type="checkbox" id="worker_type${index.count }"
							name="worker_type" value="${s.key }" style="display:none;"
							<c:if test="${worker_type eq s.key }">checked="checked"</c:if> />
						<a href="javascript:queryByType('${s.key }');"
							id="worker_type${index.count }" name="worker_type_a"
							style="font-size: 18px;font-weight: bolder;">${s.value }</a>
					</div>
				</c:forEach>
			</div>

			<p style="padding-top: 30px;">
				<br /> <input type="submit" value="查 询" class="btn" />
				<%
					LoginUserVo loginUserVo = (LoginUserVo) request.getSession()
							.getAttribute("loginUserVo");
					if ("2".equals(loginUserVo.getUser_level())) {
				%>

				<input type="button" value="添加机器人" class="btn"
					onclick="addNewRobotURL()" />

				<c:if test="${worker_type eq 3 or worker_type eq 1}">
					<input type="button" value="批量启用"
						onclick="changeWorkerStatus('00');" class="btn" />
					<input type="button" value="批量停用"
						onclick="changeWorkerStatus('22');" class="btn" />
					<c:if test="${worker_type eq 3 }">
					<input type="button" value="批量换账号" onclick="changeAlipayAccount();"
						class="btn" />
					</c:if>
					<select id="stopReason" style="width:80px;text-align: center;">
						<option value="">请选择</option>
						<option value="11">需重启</option>
						<option value="22">IP被封</option>
						<option value="33">余额不足</option>
						<option value="44">其他</option>
						<option value="55">人工</option>
					</select>
				</c:if>
				<%
					}
				%>
			</p>

			<div id="hint" class="pub_con" style="display:none"></div>
			<c:if test="${!empty isShowList}">
				<table  class="dataTable" style="width:1390px">
					<tr style="background: #EAEAEA;">
						<c:if test="${worker_type eq 3 or worker_type eq 1}">
							<th style="width:30px;">全选 <br /> <input type="checkbox"
								id="checkChnRetRulAll" name="checkChnRetRulAll"
								onclick="checkChnRetRuleAll()" /></th>
						</c:if>
						<th>序号</th>
						<th>名称</th>
						<c:if test="${worker_type eq 3}">
							<th>支付宝</th>
						</c:if>
						<th>状态</th>
						<th>订单数</th>
						<th>效率</th>
						<th>昨日处理</th>
						<th>当天处理</th>
						<th>操作</th>
						<th>请求地址</th>
						<th>公网IP</th>
				 	    <th>名单类型</th> 
						<th>机器语言</th>
						<th>机器提供商</th>
						<th>机器区域</th>
						<th>机器描述</th>
						<c:if test="${worker_type eq 3}">
							<th>支付类型</th>
						</c:if>
						<th>查看详情</th>

					</tr>
					<c:forEach var="list" items="${robotList}" varStatus="idx">
						<tr
							<c:if test="${fn:contains('11', list.worker_status )}">
									 style="background: #E0F3ED;"
							</c:if>
							<c:if test="${fn:contains('22', list.worker_status )}">
									 style="background:#FFB5B5;"
							</c:if>
							<c:if test="${fn:contains('33', list.worker_status )}">
									 style="background: #BEE0FC;"
							</c:if>>

							<c:if test="${worker_type eq 3 or worker_type eq 1}">
								<td><input type="checkbox" id="worker_id" name="worker_id"
									value="${list.worker_id }" /></td>
							</c:if>

							<td>${idx.count }</td>
							<td>${list.worker_name }</td>

							<c:if test="${worker_type eq 3}">
								<td>${robotMap[list.worker_id]}</td>
							</c:if>

							<td>${workerStatus[list.worker_status] }</td>
							<td>${list.order_num }</td>
							<td>${list.robot_xl }</td>
							<td>${list.yestoday_num }</td>
							<td>${list.today_num }</td>
							<td><c:if test="${list.worker_status eq '22'}">
									<a href="javascript:void(0);"
										onclick="javascript:changeStatus('${list.worker_id}','33');">备用</a>
									<input type="text" value="${stopReason[list.stop_reason] }"
										style="width:65px;" disabled="disabled" />
								</c:if> <c:if
									test="${list.worker_status eq '33' || list.worker_status eq '99'}">
									<a href="javascript:void(0);"
										onclick="javascript:changeStatus('${list.worker_id}','00');">启用</a>
								</c:if> <c:if test="${list.worker_status ne'22'}">
									<a href="javascript:void(0);"
										onclick="javascript:changeStatus('${list.worker_id}','22');">停用</a>
									<select id="stop_reason_${list.worker_id }"
										style="width:65px;text-align: center;">
										<option value=""
											<c:if test="${empty list.stop_reason}">selected</c:if>>
											--</option>
										<option value="11"
											<c:if test="${list.stop_reason eq '11'}">selected</c:if>>
											需重启</option>
										<option value="22"
											<c:if test="${list.stop_reason eq '22'}">selected</c:if>>
											IP被封</option>
										<option value="33"
											<c:if test="${list.stop_reason eq '33'}">selected</c:if>>
											余额不足</option>
										<option value="44"
											<c:if test="${list.stop_reason eq '44'}">selected</c:if>>
											其他</option>
										<option value="55"
											<c:if test="${list.stop_reason eq '55'}">selected</c:if>>
											人工</option>
									</select>
									<br />
								</c:if></td>
							<td>${list.worker_ext }</td>
							<td>${list.public_ip }</td>
							 <td><c:if test="${list.app_valid eq '0'}">白名单</c:if> <c:if
									test="${list.app_valid eq '1'}">黑名单</c:if></td>
							<td><select id="worker_languageList" name="worker_languageList"
								disabled="disabled" style="width:75px;" onchange="">
									<option value=""
										<c:if test="${empty list.worker_language_type}">selected</c:if>>
										空</option>
									<c:forEach items="${worker_languageList}" var="p"
										varStatus="index">
										<option value="${p.key}"
											<c:if test="${!empty worker_languageList && p.key eq list.worker_language_type}">selected</c:if>>
											${p.value}</option>
									</c:forEach>
							</select></td>
							<td><select id="worker_vendorList" name="worker_vendorList"
								disabled="disabled" style="width:75px;" onchange="">
									<option value=""
										<c:if test="${empty list.worker_vendor}">selected</c:if>>
										空</option>
									<c:forEach items="${worker_vendorList}" var="p"
										varStatus="index">
										<option value="${p.key}"
											<c:if test="${!empty worker_vendorList && p.key eq list.worker_vendor}">selected</c:if>>
											${p.value}</option>
									</c:forEach>
							</select></td>
							<td><select id="worker_regionList" name="worker_regionList"
								disabled="disabled" style="width:110px;" onchange="">
									<option value=""
										<c:if test="${empty list.worker_region}">selected</c:if>>
										空</option>
									<c:forEach items="${worker_regionList}" var="p"
										varStatus="index">
										<option value="${p.key}"
											<c:if test="${!empty worker_regionList && p.key eq list.worker_region}">selected</c:if>>
											${p.value}</option>
									</c:forEach>
							</select></td>
							<td>${list.worker_describe}</td>
							<c:if test="${worker_type eq 3}">
								<td><c:if test="${list.pay_device_type eq '0'}">PC支付</c:if>
									<c:if test="${list.pay_device_type eq '1'}">APP支付</c:if></td>
							</c:if>
							<td><a href="javaScript:void(0)"
								onclick="queryInfo('${list.worker_id}');"
								onmouseover="showdiv('${list.worker_id}')"
								onmouseout="hidediv()">明细</a> <c:if test="${worker_type eq 3}">
									<a href="javaScript:void(0)"
										onclick="queryVerificationCode('${list.worker_id}');">验证码</a>
								</c:if> <%
 	String username = loginUserVo.getUser_name();
 			if (username.equals("zhangjc") || username.equals("mzs")
 					|| username.equals("maorw")) {
 %> <a href="javascript:void(0);"
								onclick="javascript:deleteByWorkId('${list.worker_id}','${list.worker_name }');"
								style="#f60;">删除</a> <%
 	}
 %> <%
 	LoginUserVo loginUserVo1 = (LoginUserVo) request
 					.getSession().getAttribute("loginUserVo");
 			if ("2".equals(loginUserVo1.getUser_level())) {
 %> <a href="javaScript:void(0)"
								onclick="change12306Host('${list.worker_ext}','${list.worker_id}');">HOST</a>
								<%
									}
								%></td>
						</tr>
					</c:forEach>
				</table>
				<jsp:include page="/pages/common/paging.jsp" />
				<input type="hidden" id="tag1" name="tag1" value="0" />
				<div id="historyDiv" style="display:none;">
					<table class="pub_table" style="width: 680px; margin: 10px 10px;"
						id="historyTable">
						<tr>
							<th style="width: 30px;">序号</th>
							<th style="width: 450px;">操作日志</th>
							<th style="width: 130px;">操作时间</th>
							<th style="width: 70px;">操作人</th>
						</tr>
					</table>
				</div>

			</c:if>
		</form>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
	</div>

	<script>
		// 修改 12306 域名指向实际 IP
		function change12306Host(work_ext, worker_id) {
			var arr = work_ext.split(":");
			var host = arr[1].replace("//", "");
			var ip = prompt("请输入您需要映射的快速IP", "");
			if(ip == null){
				return
			}
			ip=$.trim(ip);  
			
			if(ip != null && ip != ""){//ip为空，清除dns,非空校验IP是否合法
				var exp=/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/; 
				if(ip.match(exp) == null) {
				   alert("请输入正确的IP地址！");
				   return;
				}
			}
			     
		    //if (ip != null && ip != "") {
				var param = {
					work_ext : work_ext,
					worker_id : worker_id,
					ip : ip
				};
				var url = "/robotSetNew/modify12306Host.do";
				$.ajax({
					type : "post",
					data : param,
					url : url,
					//async: false,
					success : function(data) {
						if (data.indexOf('SUCCESS') != -1) {

							alert(host + "修改成功！");

						} else {

							alert(host + "修改失败1，稍后再点一次！" + data);

						}
					},
					error : function(data) {
						alert(host + "修改失败2，稍后再点一次！" + data);
					}
				});
			}

	//	}
	</script>
</body>
</html>
