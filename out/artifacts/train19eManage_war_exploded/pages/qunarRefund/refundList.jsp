<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<%@ page import="com.l9e.transaction.vo.PageVo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>退款管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script type="text/javascript">
		<%
			PageVo pageObject = (PageVo) request.getAttribute("pageBean");
			int pageIndex = pageObject.getPageIndex();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String user_level = loginUserVo.getUser_level();
		%>
		function exportExcel() {
			$("form:first").attr("action","/qunarbook/exportrefundexcel.do");
			$("form:first").submit();
			$("form:first").attr("action","/qunarrefund/queryRefundTicketList.do");
		}
		function gotoOrderRefund(order_id, refund_seq, order_type){
		var order_source =  document.getElementById("order_source").value;
		//ajax验证是否锁
		var url = "/qunarrefund/queryPayIsLock.do?order_id="+order_id+ "&refund_seq=" + refund_seq+"&version="+new Date();
			$.get(url,function(data){
			if(data != null && data != ""){
				var temp = data ;
				var str1 = temp.split("&") ;
				var str =str1[1]; 
				
				alert("此订单已经锁定，锁定人为"+str);
				return;
			 }else{
			 	var channelStr = "";
				$("input[name='refund_status']:checkbox:checked").each(function(){ 
					channelStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
				}); 
				channelStr = channelStr.substring(0, channelStr.length-1);
				var notifyStr = "";
				$("input[name='notify_status']:checkbox:checked").each(function(){ 
					notifyStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
				}); 
				notifyStr = notifyStr.substring(0, notifyStr.length-1);
				if(order_type=='0'){
					$("form:first").attr("action","/qunarrefund/ticketInfo.do?order_id="+order_id + "&refund_seq=" + refund_seq + "&isActive=1&statusList="+channelStr+ "&notifyList=" + notifyStr +"&order_source="+order_source+"&pageIndex="+<%=pageIndex%>);
					$("form:first").submit();
					$("form:first").attr("action","/qunarrefund/queryRefundTicketList.do");
				}
				if(order_type=='1') {
					$("form:first").attr("action","/qunarrefund/lianchengTicketInfo.do?order_id="+order_id + "&refund_seq=" + refund_seq + "&isActive=1&statusList="+channelStr+ "&notifyList=" + notifyStr +"&pageIndex="+<%=pageIndex%>);
					$("form:first").submit();
					$("form:first").attr("action","/qunarrefund/queryRefundTicketList.do");
				}
			 }
		 	 
		});
	}
		
		//鼠标悬浮于“明细”，显示该订单的操作日志
		var heightDiv = 0; 
		function showdiv(order_id){  
		     var oSon = window.document.getElementById("hint");   
		     if (oSon == null) return;   
		     with (oSon){   
		 		 $.ajax({
					url:"/qunarrefund/queryOrderOperHistory.do?order_id="+order_id,
					type: "POST",
					cache: false,
					dataType: "json",
					async: true,
					success: function(data){
						if(data=="" || data == null){
							return false;
						}
						var size = data.length;
						heightDiv = 0;
						for(var i=0; i<size; i++){
							var index = (parseInt(i)+1);
							if($("#index_"+index).innerText!=index){
								$("#historyTable").append("<tr  align='center'><td id='index_'"+index+"''>"+index+"</td><td align='left' style='word-break:break-all;'>"+data[i].content+"</td>"+
										"<td>"+data[i].create_time+"</td><td>"+data[i].opt_person+"</td></tr>");
								if(data[i].content.length>44){
									heightDiv = heightDiv + 30;
								}else{
									heightDiv = heightDiv + 15;
								}
							}
						}
					}
				});
		 		innerHTML = historyDiv.innerHTML; 
			    style.display = "block"; 
			    heightDiv = heightDiv + 106;//106为div中表格边距以及表头的高度86+20
			    style.pixelLeft = window.event.clientX + window.document.body.scrollLeft - 750;   
			    style.pixelTop = window.event.clientY + window.document.body.scrollTop - heightDiv;   
		    }   
		}   
		//鼠标离开“明细”，隐藏该订单的操作日志
		function hidediv(){   
		    var oSon = window.document.getElementById("hint");   
		    if(oSon == null) return;   
		    oSon.style.display="none";   
		} 
		//全选
		function selectAll(){
			var checklist = document.getElementsByName("refund_status");
			if(document.getElementById("controlAll").checked){
				for(var i=0; i<checklist.length; i++){
					checklist[i].checked = 1;
				}
			}else{
				for(var j=0; j<checklist.length; j++){
					checklist[j].checked = 0;
				}
			}
		}
		//机器改签
		function gotoRobotGai(order_id, refund_seq, order_type){
			//ajax验证是否锁
			var url = "/qunarrefund/queryPayIsLock.do?order_id="+order_id+ "&refund_seq=" + refund_seq+"&version="+new Date();
			$.get(url,function(data){
				if(data != null && data != ""){
					var temp = data ;
					var str1 = temp.split("&") ;
					var str =str1[1]; 
					alert("此订单已经锁定，锁定人为"+str);
					return;
				 }else{
					var uri = "/qunarrefund/updateOrderstatusToRobotGai.do?order_id="+order_id+"&refund_seq="+refund_seq+"&refund_status=01&version="+new Date();
					$.post(uri,function(data){
						if(data=="yes"){
							$("form:first").attr("action","/qunarrefund/queryRefundTicketList.do?pageIndex="+<%=pageIndex%>);
							$("form:first").submit();
						}else{
							alert("机器改签失败");
						}
					});
				 }
			});
		}

		//机器退票
		function gotoRobotRefund(order_id, refund_seq, order_type){
			//ajax验证是否锁
			var url = "/qunarrefund/queryPayIsLock.do?order_id="+order_id+ "&refund_seq=" + refund_seq+"&version="+new Date();
			$.get(url,function(data){
				if(data != null && data != ""){
					var temp = data ;
					var str1 = temp.split("&") ;
					var str =str1[1]; 
					alert("此订单已经锁定，锁定人为"+str);
					return;
				 }else{
					var uri = "/qunarrefund/updateOrderstatusToRobotGai.do?order_id="+order_id+"&refund_seq="+refund_seq+"&refund_status=05&version="+new Date();
					$.post(uri,function(data){
						if(data=="yes"){
							$("form:first").attr("action","/qunarrefund/queryRefundTicketList.do?pageIndex="+<%=pageIndex%>);
							$("form:first").submit();
						}else{
							alert("机器退票失败");
						}
					});
				 }
			});
		}

				
		function shuaXinAllAuto(){
				setInterval("shuaXinAll();",60000);//一分钟刷新一次
			}
		function shuaXinAll(){
		var url = "/remind/queryCount.do?&version ="+new Date();
		$.get(url,function(data){
			var strJSON = data;//得到的JSON
			var obj = new Function("return" + strJSON)();//转换后的JSON对象
			document.getElementById("19e").value = obj.l9eCount;
			document.getElementById("qunar").value = obj.qunarCount;
			document.getElementById("ext").value = obj.extCount;
			document.getElementById("B2C").value = obj.B2CCount;
			document.getElementById("inner").value = obj.innerCount;
			document.getElementById("elong").value = obj.elongCount;
			document.getElementById("tongcheng").value = obj.tcCount;
			document.getElementById("complain").value = obj.complain;
			document.getElementById("bookExtCount").value = obj.bookExtCount; 
			document.getElementById("bookQunarCount").value = obj.bookQunarCount;  
			document.getElementById("bookElongCount").value = obj.bookElongCount;  
			document.getElementById("bookTcCount").value = obj.bookTcCount;  
			document.getElementById("refundExtCount").value = obj.refundExtCount;  
			document.getElementById("refundQunarCount").value = obj.refundQunarCount;  
			document.getElementById("refundElongCount").value = obj.refundElongCount;  
			document.getElementById("refundTcCount").value = obj.refundTcCount; 
		});
		}
		$().ready(function(){
					shuaXinAll();
					shuaXinAllAuto();
				});
				
		//通知成功 
		function queren(order_id,refund_seq) {
			 if(confirm("你确认修改为通知成功吗？")){
			 var channelStr = "";
				$("input[name='refund_status']:checkbox:checked").each(function(){ 
					channelStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
				}); 
				channelStr = channelStr.substring(0, channelStr.length-1);
				var notifyStr = "";
				$("input[name='notify_status']:checkbox:checked").each(function(){ 
					notifyStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
				}); 
				notifyStr = notifyStr.substring(0, notifyStr.length-1);
				var order_source =  document.getElementById("order_source").value;
				 $("form:first").attr("action","/qunarrefund/notify.do?order_id=" + order_id+"&refund_seq="+refund_seq+"&&statusList="+channelStr+ "&notifyList=" + notifyStr +"&order_source="+order_source+"&pageIndex="+<%=pageIndex%>);
				$("form:first").submit();
			 }
		}
		
		//明细
		function gotomixi(order_id,refund_seq,order_type){
		 		var channelStr = "";
				$("input[name='refund_status']:checkbox:checked").each(function(){ 
					channelStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
				}); 
				channelStr = channelStr.substring(0, channelStr.length-1);
				var notifyStr = "";
				$("input[name='notify_status']:checkbox:checked").each(function(){ 
					notifyStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
				}); 
				notifyStr = notifyStr.substring(0, notifyStr.length-1);
				var order_source =  document.getElementById("order_source").value;
			if(order_type=='0'){
					$("form:first").attr("action","/qunarrefund/ticketInfo.do?order_id="+order_id + "&refund_seq=" + refund_seq + "&isActive=0&statusList="+channelStr+ "&notifyList=" + notifyStr +"&order_source="+order_source+"&pageIndex="+<%=pageIndex%>);
					$("form:first").submit();
					$("form:first").attr("action","/qunarrefund/queryRefundTicketList.do");
				}
				if(order_type=='1') {
					$("form:first").attr("action","/qunarrefund/lianchengTicketInfo.do?order_id="+order_id + "&refund_seq=" + refund_seq + "&isActive=0&statusList="+channelStr+ "&notifyList=" + notifyStr +"&pageIndex="+<%=pageIndex%>);
					$("form:first").submit();
					$("form:first").attr("action","/qunarrefund/queryRefundTicketList.do");
				}
		
		}
</script>
<style>
.liancheng {color: red;}
tr:hover {background: #ecffff;}
#hint{width:700px;border:1px solid #C1C1C1;background:#F0FAC1;position:fixed;z-index:9;padding:6px;line-height:17px;text-align:left;overflow:auto;} 
</style>
	</head>
	<body  onload="shuaXinAll();"><div></div>
		<div class="book_manage oz">
			<form action="/qunarrefund/queryRefundTicketList.do" method="post">

				<ul class="order_num oz" style="margin-top: 10px;">
					<li>
						订单号：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="order_id"
							value="${order_id}" />
					</li>
					<li>
						退款流水号：&nbsp;
						<input type="text" class="text" name="refund_seq"
							value="${refund_seq}" />
					</li>
					<li>
						12306退款流水号：
						<input type="text" class="text" name="refund_12306_seq"
							value="${refund_12306_seq}" />
					</li>
					<li>
							订单类型：
							<select name="order_type" style="width:170px;">
								<option value="" <c:if test="${empty order_type}">selected</c:if>>
									全部
								</option>
								<option value="0"
									<c:if test="${!empty order_type && order_type eq '0'}">selected</c:if>>
									普通
								</option>
								<option value="1"
									<c:if test="${!empty order_type && order_type eq '1'}">selected</c:if>>
									联程
								</option>
							</select>
						</li>
				</ul>
				<ul class="order_num oz" style="margin-top: 10px;">
					<li>
						开始时间：&nbsp;&nbsp;&nbsp;
						<!-- <input type="text" class="text" name="begin_info_time" value="${begin_info_time }"/> -->
						<input type="text" class="text" name="begin_info_time"
							readonly="readonly" value="${begin_info_time }"
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>
					<li>
						结束时间：&nbsp;&nbsp;&nbsp;
						<!-- <input type="text" class="text" name="end_info_time" value="${end_info_time }"/> -->
						<input type="text" class="text" name="end_info_time"
							readonly="readonly" value="${end_info_time }"
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>
					<li>
						订单来源：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
						<select name="order_source" id="order_source" style="width: 170px;">
			        		<option value="" <c:if test="${order_source eq ''}">selected</c:if>>所有</option>
			        		<option value="qunar1" <c:if test="${order_source eq 'qunar1'}">selected</c:if> >19旅行</option>
			        		<option value="qunar2" <c:if test="${order_source eq 'qunar2'}">selected</c:if> >久久商旅</option>
			       		</select>
					</li>
					<li>
							操作人：&nbsp;&nbsp;
							<input type="text" class="text" name="opt_person" value="${opt_person }" />
					</li>
				</ul>
				<ul class="order_num oz" style="margin-top: 10px;">
						<li><b><font color="red">通知失败</font></b>：
						&nbsp;&nbsp;&nbsp;预订
								【	去哪：<input value="" id="bookQunarCount" style="color:#f60;width:20px;border:0;"/>
			       					艺龙：<input value="" id="bookElongCount" style="color:#f60;width:20px;border:0;"/>
			       					同程：<input value="" id="bookTcCount" style="color:#f60;width:20px;border:0;"/>
			       					商户：<input value="" id="bookExtCount" style="color:#f60;width:20px;border:0;"/>
			       				】
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;退款
								【	去哪：<input value="" id="refundQunarCount" style="color:#f60;width:20px;border:0;"/>
			       					艺龙：<input value="" id="refundElongCount" style="color:#f60;width:20px;border:0;"/>
			       					同程：<input value="" id="refundTcCount" style="color:#f60;width:20px;border:0;"/>
			       					商户：<input value="" id="refundExtCount" style="color:#f60;width:20px;border:0;"/>
			       				】
						
						</li>
					</ul>
				<ul class="order_state oz">
					<li>
						&nbsp;&nbsp;&nbsp;退款状态：
					</li>
					<li><input type="checkbox" onclick="selectAll()" name="controlAll" style="controlAll" id="controlAll"/><label for="controlAll">全部</label></li>
					<li><input type="checkbox" id="refund_status1" name="refund_status" value="012"
								<c:if test="${fn:contains(refund_status, '012')}">checked="checked"</c:if> />
								<label for="refund_status1">正在改签</label></li>
					<li><input type="checkbox" id="refund_status2" name="refund_status" value="03"
								<c:if test="${fn:contains(refund_status, '03')}">checked="checked"</c:if> />
								<label for="refund_status2">人工改签</label></li>
					<li><input type="checkbox" id="refund_status3" name="refund_status" value="456"
								<c:if test="${fn:contains(refund_status, '456')}">checked="checked"</c:if> />
								<label for="refund_status3">正在退票</label></li>
					<li><input type="checkbox" id="refund_status4" name="refund_status" value="07"
								<c:if test="${fn:contains(refund_status, '07')}">checked="checked"</c:if> />
								<label for="refund_status4">人工退票</label></li>
					<li><input type="checkbox" id="refund_status5" name="refund_status" value="11"
								<c:if test="${fn:contains(refund_status, '11')}">checked="checked"</c:if> />
								<label for="refund_status5">退票完成</label></li>	
					<li><input type="checkbox" id="refund_status6" name="refund_status" value="22"
								<c:if test="${fn:contains(refund_status, '22')}">checked="checked"</c:if> />
								<label for="refund_status6">拒绝退票</label></li>	
					<li><input type="checkbox" id="refund_status7" name="refund_status" value="33"
								<c:if test="${fn:contains(refund_status, '33')}">checked="checked"</c:if> />
								<label for="refund_status7">审核退款</label></li>
					<c:forEach items="${refundStatus}" var="r" varStatus="index">
					<!-- 
						<li>
							<input type="checkbox" id="refund_status${index.count }"
								name="refund_status" value="${r.key}"
								<c:if test="${fn:contains(refund_status, r.key)}">checked="checked"</c:if> />
							<label for="refund_status${index.count}">
								${r.value }
							</label>
						</li>
						 -->
					</c:forEach>
				</ul>
				<ul class="order_state oz">
					<li>
						&nbsp;&nbsp;&nbsp;通知状态：
					</li>
					<c:forEach items="${notifyStatus}" var="d" varStatus="index">
						<li>
							<input type="checkbox" id="notify_status${index.count }"
								name="notify_status" value="${d.key }"
								<c:if test="${fn:contains(notify_status,d.key) }">checked="checked"</c:if> />
							<label for="notify_status${index.count }">
								${d.value }
							</label>
						</li>
					</c:forEach>
				</ul>
				
				<p>
					<input type="submit" value="查 询" class="btn" />
					<%
						if ("2".equals(loginUserVo.getUser_level()) ) {
           			%>
						<input type="button" value="导出Excel" class="btn" onclick="exportExcel()" />
					<%} %>
					&nbsp;&nbsp;<a href="/qunarrefund/queryRefundTicketList.do?refund_status=012&refund_status=456">正在处理</a>&nbsp;
					投诉：<input value="" type="text" id="complain" style="color:#f60;width:20px;border:0;"/>
							未退款【
							19e：<input value="" id="19e" style="color:#f60;width:20px;border:0;"/>
							去哪：<input value="" id="qunar" style="color:#f60;width:20px;border:0;"/>
							艺龙：<input value="" id="elong" style="color:#f60;width:20px;border:0;"/>
							同程：<input value="" id="tongcheng" style="color:#f60;width:20px;border:0;"/>
							商户：<input value="" type="text" id="ext" style="color:#f60;width:20px;border:0;"/>
							B2C：<input value="" type="text" id="B2C" style="color:#f60;width:20px;border:0;"/>
							内嵌：<input value="" type="text" id="inner" style="color:#f60;width:20px;border:0;"/>
						】
		</p>
				<div id="hint" class="pub_con" style="display:none"></div>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #EAEAEA;">
							<th>
								序号
							</th>
							<th>
								订单号
							</th>
							<th>
								退款流水号
							</th>
							<th>
								退款金额
							</th>

							<th>
								创建时间
							</th>
							<th>
								发车时间
							</th>
							<th>
								退款状态
							</th>
							<th>
								通知状态
							</th>
							<th>
								订单类型
							</th>
							<th>
								订单来源
							</th>
							<th>
								操作人
							</th>
							<th>
								返回日志
							</th>
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${refundTicketList}" varStatus="idx">
							<tr 
								<c:if test="${fn:contains('00,01,02,04,05,06', list.refund_status )}">
									style="background: #BEE0FC;"
								</c:if>
								<c:if test="${fn:contains('03, 07, 33, 44', list.refund_status )}">
									style="background: #E0F3ED;"
								</c:if>
								<c:if test="${fn:contains('22', list.refund_status )}">
									style="background:#FFB5B5;"
								</c:if>
							>
							
							<td>
								${idx.index+1}
							</td>
							<td>
								${list.order_id}
							</td>
							<td>
								${list.refund_seq}
							</td>
							<td>
								${list.refund_money }
							</td>
							<td>
								${list.create_time}
							</td>
							<td>
								${list.from_time}
							</td>
							<td>
								${refundStatus[list.refund_status]}
							</td>
							<td>
								${notifyStatus[list.notify_status]}
							</td>
							<td>
								<c:if test="${empty list.order_type}">普通</c:if>
								<c:if test="${list.order_type eq '0'}">普通</c:if>
								<c:if test="${list.order_type eq '1' }">
									<div class="liancheng">
										联程
									</div>
								</c:if>
							</td>
							<td>
								${qunarChannel[list.order_source] }
							</td>
							<td>
								${list.opt_person}
							</td>
							<td>
								${returnOptlog[list.return_optlog] }
							</td>
							<td>
								<span>
								<c:choose> 
									<c:when test="${fn:contains('03', list.refund_status) && list.order_type eq '0'}"> 
										<a href="javascript:gotoOrderRefund('${list.order_id}','${list.refund_seq}','0');">人工改签</a> 
										<c:if test="${refund_and_alert eq '1'}">
								<!--			<a href="javascript:gotoRobotGai('${list.order_id}','${list.refund_seq}','0');">机器改签</a> -->
										</c:if>
									</c:when> 
									<c:when test="${fn:contains('03', list.refund_status) && list.order_type eq '1'}"> 
										<a href="javascript:gotoOrderRefund('${list.order_id}','${list.refund_seq}','1');">人工改签</a> 
										<c:if test="${refund_and_alert eq '1'}">
									<!--		<a href="javascript:gotoRobotGai('${list.order_id}','${list.refund_seq}','1');">机器改签</a> -->
										</c:if>
									</c:when> 
									<c:when test="${fn:contains(('07'), list.refund_status) && list.order_type eq '0'}"> 
										<a href="javascript:gotoOrderRefund('${list.order_id}','${list.refund_seq}','0');">人工退票</a> 
										<c:if test="${refund_and_alert eq '1'}">
									<!--		<a href="javascript:gotoRobotRefund('${list.order_id}','${list.refund_seq}','0');">机器退票</a> -->
										</c:if>
									</c:when> 
									<c:when test="${fn:contains('07', list.refund_status) && list.order_type eq '1'}"> 
										<a href="javascript:gotoOrderRefund('${list.order_id}','${list.refund_seq}','1');">人工退票</a> 
										<c:if test="${refund_and_alert eq '1'}">
									<!--		<a href="javascript:gotoRobotRefund('${list.order_id}','${list.refund_seq}','1');">机器退票</a> -->
										</c:if>
									</c:when> 
									<c:when test="${fn:contains('33', list.refund_status) && list.order_type eq '0'}"> 
										<a href="javascript:gotoOrderRefund('${list.order_id}','${list.refund_seq}','0');">审核退款</a> 
									</c:when> 
									<c:when test="${fn:contains('33', list.refund_status) && list.order_type eq '1'}"> 
										<a href="javascript:gotoOrderRefund('${list.order_id}','${list.refund_seq}','1');">审核退款</a>  
									</c:when>
									
									<c:when test="${(list.refund_status eq '00' ||list.refund_status eq '01'|| list.refund_status eq '02'  ) && list.order_type eq '1'}"> 
										<a href="javascript:gotoOrderRefund('${list.order_id}','${list.refund_seq}','1');">人工处理</a> 
									</c:when> 
								</c:choose>  
								</span>
								<br/>
								<span>
								<a href="javascript:gotomixi('${list.order_id}','${list.refund_seq}','${list.order_type }');"  onmouseover="showdiv('${list.order_id}')" onmouseout="hidediv()">明细</a>
								<c:if test="${list.notify_status eq '33'}">
									<a href="javascript:queren('${list.order_id}','${list.refund_seq }');">通知成功</a>
								</c:if>
								</span>
							</td>
							</tr>
						</c:forEach>
					</table>
					<jsp:include page="/pages/common/paging.jsp" />
					<div id="historyDiv" style="display:none;">
						<table class="pub_table" style="width: 680px; margin: 10px 10px;" id="historyTable">
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
		</div>
	</body>
</html>
