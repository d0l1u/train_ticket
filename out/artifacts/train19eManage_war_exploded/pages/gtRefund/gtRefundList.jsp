<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<%@ page import="com.l9e.transaction.vo.PageVo"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>退票管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	    <script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
	    <script type="text/javascript">
	    <%
			PageVo pageObject = (PageVo) request.getAttribute("pageBean");
			int pageIndex = pageObject.getPageIndex();
		%>
		    function selectAllrefund_type(){
				var checklist = document.getElementsByName("refund_type");
				if(document.getElementById("controlAllrefund_type").checked){
					for(var i=0; i<checklist.length; i++){
						checklist[i].checked = 1;
					}
				}else{
					for(var j=0; j<checklist.length; j++){
						checklist[j].checked = 0;
					}
				}
			}
			function selectAllrefund_status(){
				var checklist = document.getElementsByName("refund_status");
				if(document.getElementById("controlAllrefund_status").checked){
					for(var i=0; i<checklist.length; i++){
						checklist[i].checked = 1;
					}
				}else{
					for(var j=0; j<checklist.length; j++){
						checklist[j].checked = 0;
					}
				}
			}
			
	    	function gotoRefundTicketInfo(order_id,cp_id,stream_id){
		    	var url="/gtRefund/queryRfundTicketIsLock.do?stream_id="+stream_id;
		    		$.get(url,function(data){
					if(data != null && data != ""){
						var temp = data ;
						var str1 = temp.split("&") ;
						var str =str1[1]; 
						
						alert("此退款已经锁定，锁定人为"+str);
						return;
					 }else{
						var channelStr = "";
						$("input[name='refund_status']:checkbox:checked").each(function(){ 
							channelStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
						}); 
						channelStr = channelStr.substring(0, channelStr.length-1);
						$("form:first").attr("action","/gtRefund/queryRefundTicketInfo.do?order_id="+order_id + "&cp_id=" + cp_id +"&stream_id="+stream_id+ "&isActive=1&statusList="+channelStr+"&pageIndex="+<%=pageIndex%>);
						$("form:first").submit();
				     }
				    	//window.location="/gtRefund/queryRefundTicketInfo.do?order_id="+order_id+"&cp_id="+cp_id+"&stream_id="+stream_id;
				});
			    
	    	}    
	    	function exportExcelRefund() {
				$("form:first").attr("action","/orderForExcel/excelexportForGtRefund.do");
				$("form:first").submit();
				$("form:first").attr("action","/gtRefund/queryRefundTicketList.do");
			}
			
	    	//全选notify
		function selectAllNotify(){
			var checklist = document.getElementsByName("notify_status");
			if(document.getElementById("controlAllNotify").checked){
				for(var i=0; i<checklist.length; i++){
					checklist[i].checked = 1;
				}
			}else{
				for(var j=0; j<checklist.length; j++){
					checklist[j].checked = 0;
				}
			}
		}
		
	var heightDiv = 0; 
	function showdiv(order_id){  
	     var oSon = window.document.getElementById("hint");   
	     if (oSon == null) return;   
	     with (oSon){   
	 		 $.ajax({
				url:"/gtBooking/queryOrderOperHistory.do?order_id="+order_id,
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
							$("#historyTable").append("<tr line-height='15px'align='center' ><td id='index_'"+index+"''>"+index+"</td><td align='left'   style='word-break:break-all;'>"+data[i].order_optlog+"</td>"+
									"<td>"+data[i].create_time+"</td><td>"+data[i].opter+"</td></tr>");
							if(data[i].order_optlog.length>44){
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
	
	
	
	//机器改签
		function gotoRobotGai(order_id, refund_seq, order_type){
			//ajax验证是否锁
			var url = "/gtRefund/queryPayIsLock.do?order_id="+order_id+ "&refund_seq=" + refund_seq+"&version="+new Date();
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
				 
					var uri = "/gtRefund/updateOrderstatusToRobotGai.do?order_id="+order_id+"&refund_seq="+refund_seq+"&refund_status=01&statusList="+channelStr+"&version="+new Date();
					$.post(uri,function(data){
						if(data=="yes"){
							$("form:first").attr("action","/gtRefund/queryRefundTicketList.do?pageIndex="+<%=pageIndex%>);
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
			var url = "/gtRefund/queryPayIsLock.do?order_id="+order_id+ "&refund_seq=" + refund_seq+"&version="+new Date();
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
				 
					var uri = "/gtRefund/updateOrderstatusToRobotGai.do?order_id="+order_id+"&refund_seq="+refund_seq+"&refund_status=05&statusList="+channelStr+"&version="+new Date();
					$.post(uri,function(data){
						if(data=="yes"){
							$("form:first").attr("action","/gtRefund/queryRefundTicketList.do?pageIndex="+<%=pageIndex%>);
							$("form:first").submit();
						}else{
							alert("机器退票失败");
						}
					});
				 }
			});
		}
		
		function gotoOrderRefund(order_id,cp_id,stream_id){
		//ajax验证是否锁
		var url = "/gtRefund/queryPayIsLock.do?order_id="+order_id+"&cp_id="+cp_id+"&version="+new Date();
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
				$("form:first").attr("action","/gtRefund/queryRefundTicketInfo.do?order_id="+order_id +"&cp_id="+cp_id +"&stream_id="+stream_id  + "&isActive=1&statusList="+channelStr+"&pageIndex="+<%=pageIndex%>);
				$("form:first").submit();
				//$("form:first").attr("action","/gtRefund/queryRefundTicketList.do");
			 }
		 	 
		});
	}
	
	function updateGtRefundNotifyNum (order_id,notify_id){
				var channelStr = "";
						$("input[name='refund_status']:checkbox:checked").each(function(){ 
							channelStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
						}); 
				channelStr = channelStr.substring(0, channelStr.length-1);
				$("form:first").attr("action","/gtRefund/updateGtRefundNotifyNum.do?order_id="+order_id +"&notify_id="+notify_id +"&statusList="+channelStr+"&pageIndex="+<%=pageIndex%>);
				$("form:first").submit();
			//	$("form:first").attr("action","/gtRefund/queryRefundTicketList.do");
	
	}
	
	function uploadAddRefund(){
		$("form:first").attr("action","/gtRefund/uploadAddRefund.do");
		$("form:first").submit();
	}
	    </script>
	    <style>
	    #hint{width:700px;border:1px solid #C1C1C1;background:#F0FAC1;position:fixed;z-index:9;padding:6px;line-height:17px;text-align:left;overflow:auto;} 
	    </style>
	</head>
	<body><div></div>
		<div class="book_manage oz">
			<form action="/gtRefund/queryRefundTicketList.do" method="post" name="myform" id="myform">
				<div style="border: 0px solid #00CC00; margin: 10px;">
				
					<ul class="order_num oz" style="margin-top: 30px;">
						<li>订单号：&nbsp;&nbsp;&nbsp; 
							<input type="text" class="text" name="order_id" value="${order_id }" /> 
						</li>
						<li>
							商户订单号：
						<input type="text" class="text" name="merchant_order_id" value="${merchant_order_id}"/>
						</li>
						<li>
							退款流水号：
							<input type="text" class="text" name="refund_seq" value="${refund_seq }" />
						</li>
						<!-- 
						<li>
							12306退款流水单号：
							<input type="text" class="text" name="refund_12306_seq" value="${refund_12306_seq }" />
						</li>
						 -->
					</ul>
					<ul class="order_num oz" style="margin-top: 10px;">
						<li>
							开始时间：&nbsp;
							<!-- <input type="text" class="text" name="beginInfo_time" value="${beginInfo_time }"/> -->
							<input type="text" class="text" name="begin_create_time" readonly="readonly" value="${begin_create_time }"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})"/> 
						</li>
						<!-- <li><span>时间格式为: yyyy - MM - dd</span></li> -->
						<li>
							结束时间：&nbsp;&nbsp;
							<!-- <input type="text" class="text" name="endInfo_time" value="${endInfo_time}"/> -->
								<input type="text" class="text" name="end_create_time" readonly="readonly" value="${end_create_time}"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" /> 
						</li>
						<li>
							操作人：
							<input type="text" class="text" name="opt_person" value="${opt_person }" />
						</li>
					</ul>
					<ul class="ser order_num oz" style="margin-top: 10px;">
						<li>
							退款类型：
							<input type="checkbox" onclick="selectAllrefund_type()" 
							name="controlAllrefund_type" style="controlAllrefund_type" 
							id="controlAllrefund_type"/>
							<label for="">全部</label>
							<c:forEach items="${refund_types }" var="s" varStatus="index">
								<input type="checkbox" id="refund_type${index.count }"
									name="refund_type" value="${s.key }"
									<c:if test="${fn:contains(refund_typeStr, s.key ) }">checked="checked"</c:if> />
								<label for="refund_type${index.count }">
									${s.value }&nbsp;&nbsp;
								</label>
							</c:forEach>
						</li>
					</ul>
					<ul class="ser order_num oz" style="margin-top: 10px;">
					<li>
						退款状态：
					<input type="checkbox" onclick="selectAllrefund_status()" name="controlAllrefund_status" 
					style="controlAllrefund_status" id="controlAllrefund_status"/><label for="controlAllrefund_status">&nbsp;全部</label>
					<input type="checkbox" id="refund_status1" name="refund_status" value="00"
								<c:if test="${fn:contains(refund_status, '00')}">checked="checked"</c:if> />
								<label for="refund_status1">等待退票&nbsp;&nbsp;</label>
					<input type="checkbox" id="refund_status1" name="refund_status" value="012"
								<c:if test="${fn:contains(refund_status, '012')}">checked="checked"</c:if> />
								<label for="refund_status1">正在改签&nbsp;&nbsp;</label>
					<input type="checkbox" id="refund_status2" name="refund_status" value="03"
								<c:if test="${fn:contains(refund_status, '03')}">checked="checked"</c:if> />
								<label for="refund_status2">人工改签&nbsp;&nbsp;</label>
					<input type="checkbox" id="refund_status3" name="refund_status" value="456"
								<c:if test="${fn:contains(refund_status, '456')}">checked="checked"</c:if> />
								<label for="refund_status3">正在退票&nbsp;&nbsp;</label>
					<input type="checkbox" id="refund_status4" name="refund_status" value="07"
								<c:if test="${fn:contains(refund_status, '07')}">checked="checked"</c:if> />
								<label for="refund_status4">人工退票&nbsp;&nbsp;</label>
					<input type="checkbox" id="refund_status5" name="refund_status" value="11"
								<c:if test="${fn:contains(refund_status, '11')}">checked="checked"</c:if> />
								<label for="refund_status5">同意退票&nbsp;&nbsp;</label>	
					<input type="checkbox" id="refund_status6" name="refund_status" value="22"
								<c:if test="${fn:contains(refund_status, '22')}">checked="checked"</c:if> />
								<label for="refund_status6">拒绝退票&nbsp;&nbsp;</label>	
					<input type="checkbox" id="refund_status7" name="refund_status" value="33"
								<c:if test="${fn:contains(refund_status, '33')}">checked="checked"</c:if> />
								<label for="refund_status7">退票完成&nbsp;&nbsp;</label>	
					<input type="checkbox" id="refund_status8" name="refund_status" value="44"
								<c:if test="${fn:contains(refund_status, '44')}">checked="checked"</c:if> />
								<label for="refund_status8">退票失败&nbsp;&nbsp;</label>	
					<input type="checkbox" id="refund_status9" name="refund_status" value="55"
								<c:if test="${fn:contains(refund_status, '55')}">checked="checked"</c:if> />
								<label for="refund_status9">审核退款&nbsp;&nbsp;</label>
					<input type="checkbox" id="refund_status10" name="refund_status" value="99"
								<c:if test="${fn:contains(refund_status, '99')}">checked="checked"</c:if> />
								<label for="refund_status10">搁置订单&nbsp;&nbsp;</label></li>	
				</ul>
				<ul class="ser order_num oz" style="margin-top: 10px;">
					<li>
						通知状态：
					<input type="checkbox" onclick="selectAllNotify()" name="controlAllNotify" 
					style="controlAllNotify" id="controlAllNotify"/>
					<label for="controlAllNotify">全部</label>
					<c:forEach items="${notifyStatus}" var="d" varStatus="index">
							<input type="checkbox" id="notify_status${index.count }"
								name="notify_status" value="${d.key }"
								<c:if test="${fn:contains(notify_status,d.key) }">checked="checked"</c:if> />
							<label for="notify_status${index.count }">
								${d.value }&nbsp;&nbsp;
							</label>
					</c:forEach>
					</li>
				</ul>
				</div>
				<p>
					<input type="submit" value="查 询" class="btn" />
					<%	LoginUserVo loginUserVo= (LoginUserVo)request.getSession().getAttribute("loginUserVo");
						if(("2".equals(loginUserVo.getUser_level()))){%>
					<input type="button" value="导出Excel" class="btn" onclick="exportExcelRefund()"/>  
					<input type="button" value="生成线下退款" class="btn" onclick="location.href = '/gtRefund/toAddRefundPage.do?refund_type=2'"/>
					<input type="button" value="生成改签退款" class="btn" onclick="location.href = '/gtRefund/toAddRefundPage.do?refund_type=3'"/>
					
					<input type="button" value="批量车站退票" class="btn" onclick="uploadAddRefund()" />
						<%} %>
		    		<a href="/gtRefund/queryRefundTicketList.do?refund_status=012&refund_status=456">正在处理</a>
				</p>
				<div id="hint" class="pub_con" style="display:none"></div>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #f0f0f0;">
							<th>
								序号
							</th>
							<th>
								订单号
							</th>
							<th>
								票数
							</th>
							<th>
								商户订单号
							</th>
							<th>
								车票ID
							</th>
							<th>
								退款类型
							</th>
							<th>
								退款金额
							</th>
							<th>
								实际退款
							</th>
							<th>
								手续费比例
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
								操作人
							</th>
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${refundTicketList}" varStatus="idx">
						<tr
		            		<c:if test="${fn:contains('00', list.refund_status )}">
		            		 	style="background:#E0F3ED; "
		            		</c:if>
		            		<c:if test="${fn:contains('11,22', list.refund_status )}">
		            		 	style="background:#FFDAC8; "
		            		</c:if>
		            		<c:if test="${fn:contains('33', list.refund_status )}">
		            		 	style="background:#FFB5B5; "
		            		</c:if>
		            		<c:if test="${fn:contains('55', list.refund_status )}">
		            		 	style="background:#FCFCFC; "
		            		</c:if>
            			>
							<td>
								${idx.index+1}
							</td>
							<c:choose>
								<c:when test="${!empty list.refund_note }">
									<td style="color:red;">
										<b>
										${list.order_id }
										</b>
									</td>
								</c:when>
								<c:otherwise>
									<td>
									${list.order_id }
									</td>
								</c:otherwise>
							</c:choose>
							<td>
								${list.ticket_num }
							</td>
							<td>
								${list.merchant_order_id}
							</td>
							<td>
								${list.cp_id}
							</td>
							<c:choose>
								<c:when test="${list.refund_type !=null && list.refund_type eq '2'}">
									<td>
										${refund_types[list.refund_type] }
									</td>
								</c:when>
								<c:when test="${list.refund_type !=null && list.refund_type eq '3'}">
									<td style="color:#f00;">
										${refund_types[list.refund_type] }
									</td>
								</c:when>
								<c:otherwise>
								<td>
									${refund_types[list.refund_type] }
								</td>
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${list.refund_percent !=null && list.refund_percent eq '20%' && list.refund_money >=50}">
										<td style="color:#f00;">
											<strong>${list.refund_money }</strong>
										</td>
								</c:when>
								<c:otherwise>    
								<td>
									${list.refund_money }
								</td>
								</c:otherwise>
							</c:choose>
							<td>
								${list.actual_refund_money }
							</td>
							<td>
								${list.refund_percent }
							</td>
							<td>
								${fn:substringBefore(list.create_time, ' ')}
								<br />
								${fn:substringAfter(list.create_time, ' ')}
							</td>
							<td>
								${fn:substringBefore(list.from_time, ' ')}
								<br />
								${fn:substringAfter(list.from_time, ' ')}
							</td>
							<td>
								${refund_statuses[list.refund_status] }
							</td>
							<td>
								${notifyStatus[list.notify_status]}
							</td>
							<td>
								${list.opt_person }
							</td>
							<td>
								<span>
								
									<%if(!"1.2".equals(loginUserVo.getUser_level())){ %>
									<c:if test="${list.refund_status eq '00' && list.refund_type eq '1'}">
										<a href="javascript:gotoRefundTicketInfo('${list.order_id}','${list.cp_id  }','${list.stream_id }');">用户退款</a>
									</c:if>
									<c:if test="${!empty list.notify_num && list.notify_num eq '10' && list.notify_status!=33  }">
										<a href="javascript:updateGtRefundNotifyNum('${list.order_id}','${list.notify_id  }');">重新通知</a>
									<!--	<a href="/gtRefund/updateGtRefundNotifyNum.do?notify_id=${list.notify_id  }&order_id=${list.order_id }">重新通知</a>  -->
									</c:if>
									<c:if test="${list.refund_status eq '99'&& list.refund_type eq '1'}">
										<a href="javascript:gotoRefundTicketInfo('${list.order_id}','${list.cp_id  }','${list.stream_id }');">搁置订单</a>
									</c:if>
									<c:if test="${list.refund_status eq '00'&& list.refund_type eq '4'}">
										<font style="color : #f60;">
										退款中
										</font>
										<!-- 
										<a href="javascript:gotoRefundTicketInfo('${list.order_id}','${list.cp_id  }','${list.stream_id }');">差额退款</a>
										 -->
									</c:if>
									<c:if test="${list.refund_status eq '00'&& list.refund_type eq '5'}">
										退款中
										<!-- 
										<a href="javascript:gotoRefundTicketInfo('${list.order_id}','${list.cp_id  }','${list.stream_id }');">出票失败退款</a>
										 -->
									</c:if>
									<c:choose> 
									<c:when test="${fn:contains('03', list.refund_status)}"> 
										<a href="javascript:gotoOrderRefund('${list.order_id}','${list.cp_id}','${list.stream_id }');">人工改签</a> 
										<c:if test="${refund_and_alert eq '1'}">
										<a href="javascript:gotoRobotGai('${list.order_id}','${list.refund_seq}','0');">机器改签</a> 	
										</c:if>
									</c:when> 
									<c:when test="${fn:contains('07', list.refund_status)}"> 
										<a href="javascript:gotoOrderRefund('${list.order_id}','${list.cp_id}','${list.stream_id }');">人工退票</a> 
										<c:if test="${refund_and_alert eq '1'}">
										<a href="javascript:gotoRobotRefund('${list.order_id}','${list.refund_seq}','0');">机器退票</a>  
										</c:if>
									</c:when> 
									<c:when test="${fn:contains('55', list.refund_status)}"> 
										<a href="javascript:gotoOrderRefund('${list.order_id}','${list.cp_id}','${list.stream_id }');">审核退款</a> 
									</c:when> 
								</c:choose>  
									<%} %>
									<br/>	<a href="/gtRefund/queryRefundTicketInfo.do?
										order_id=${list.order_id }&cp_id=${list.cp_id }&stream_id=${list.stream_id }&query_type=1" 
 										onmouseover="showdiv('${list.order_id}')" onmouseout="hidediv()" >明细</a>
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