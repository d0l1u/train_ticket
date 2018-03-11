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
			//全选refund
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
	    	function exportExcelRefund() {
				$("form:first").attr("action","/orderForExcel/excelexportForTuniuRefund.do");
				$("form:first").submit();
				$("form:first").attr("action","/tuniuRefund/queryRefundTicketList.do");
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
				url:"/tuniuBooking/queryOrderOperHistory.do?order_id="+order_id,
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
	
	
	function updateTuniuRefundNotifyNum (order_id,notify_id){
				var channelStr = "";
						$("input[name='refund_status']:checkbox:checked").each(function(){ 
							channelStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
						}); 
				channelStr = channelStr.substring(0, channelStr.length-1);
				$("form:first").attr("action","/tuniuRefund/updateTuniuRefundNotifyNum.do?order_id="+order_id +"&notify_id="+notify_id +"&statusList="+channelStr+"&pageIndex="+<%=pageIndex%>);
				$("form:first").submit();
			//	$("form:first").attr("action","/tuniuRefund/queryRefundTicketList.do");
	
	}
	
	function uploadAddRefund(){
		$("form:first").attr("action","/tuniuRefund/uploadAddRefund.do");
		$("form:first").submit();
	}
	
	    </script>
	    <style>
	    #hint{width:700px;border:1px solid #C1C1C1;background:#F0FAC1;position:fixed;z-index:9;padding:6px;line-height:17px;text-align:left;overflow:auto;} 
	    </style>
	</head>
	<body><div></div>
		<div class="book_manage oz">
			<form action="/tuniuRefund/queryRefundTicketList.do" method="post" name="myform" id="myform">
				<div style="border: 0px solid #00CC00; margin: 10px;">
				
					<ul class="order_num oz" style="margin-top: 30px;">
						<li>订单号：&nbsp;&nbsp;&nbsp; 
							<input type="text" class="text" name="order_id" value="${order_id }" /> 
						</li>
						<li>
							退款流水号：
							<input type="text" class="text" name="refund_seq" value="${refund_seq }" />
						</li>
					</ul>
					<ul class="order_num oz" style="margin-top: 10px;">
						<li>
							开始时间：&nbsp;
							<input type="text" class="text" name="begin_create_time" readonly="readonly" value="${begin_create_time }"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})"/> 
						</li>
						<li>
							结束时间：&nbsp;&nbsp;
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
							退款类型：&nbsp;&nbsp;
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
					<li><input type="checkbox" id="refund_status8" name="refund_status" value="44"
								<c:if test="${fn:contains(refund_status, '44')}">checked="checked"</c:if> />
								<label for="refund_status8">搁置订单</label></li>	
					<li><input type="checkbox" id="refund_status9" name="refund_status" value="73"
								<c:if test="${fn:contains(refund_status, '73')}">checked="checked"</c:if> />
								<label for="refund_status9">申请线下</label></li>	
					<li><input type="checkbox" id="refund_status10" name="refund_status" value="72"
								<c:if test="${fn:contains(refund_status, '72')}">checked="checked"</c:if> />
								<label for="refund_status10">审核线下退款</label></li>							
					<li><input type="checkbox" id="refund_status11" name="refund_status" value="71"
								<c:if test="${fn:contains(refund_status, '71')}">checked="checked"</c:if> />
								<label for="refund_status11">线下退款中</label></li><br/>
					<li style="padding-left: 118px;"><input type="checkbox" id="refund_status12" name="refund_status" value="81"
								<c:if test="${fn:contains(refund_status, '81')}">checked="checked"</c:if> />
								<label for="refund_status12">车站退票</label></li>														
				</ul>
				<ul class="ser order_num oz" style="margin-top: 10px;">
					<li>
						通知状态：&nbsp;&nbsp;
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
						<input type="button" value="生成线下退款" class="btn" onclick="location.href = '/tuniuRefund/toAddRefundPage.do?refund_type=22'"/>
						<input type="button" value="生成改签退款" class="btn" onclick="location.href = '/tuniuRefund/toAddRefundPage.do?refund_type=55'"/>
						<input type="button" value="批量车站退票" class="btn" onclick="uploadAddRefund()" />
						<%} %>
		    		<a href="/tuniuRefund/queryRefundTicketList.do?refund_status=012&refund_status=456">正在处理</a>
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
								车票ID
							</th>
							<th>
							    12306单号
							</th>
							<th>
								退款金额
							</th>
							<th>
								12306退款金额
							</th>
							<th>
							          改签差价
							</th>	
							<th>
								创建时间
							</th>
							<th>
								<strong>审核时间</strong>
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
								票数
							</th>
							<th>
								退款类型
							</th>
							<th>
								渠道
							</th>
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${refundTicketList}" varStatus="idx">
						<tr
		            		<c:if test="${fn:contains('00', list.refund_status )}">
		            		 	style="background:#E0F3ED;"
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
							<td>
								${list.order_id }
							</td>
							<td>
								${list.cp_id}
							</td>
							<td>
							    ${list.out_ticket_billno}
							</td>
							<td>
								${list.refund_money }
							</td>
							<td>
								${list.detail_refund}
							</td>
							<td>
							    ${list.detail_alter}
							</td>
							<td>
								${fn:substringBefore(list.create_time, ' ')}
								<br />
								${fn:substringAfter(list.create_time, ' ')}
							</td>
							   <td>
								${fn:substringBefore(list.verify_time, ' ')}
								<br />
								${fn:substringAfter(list.verify_time, ' ')}
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
								${list.ticket_num }
							</td>
							<td>
								${refund_types[list.refund_type] }
							</td>
							<td>
							 途牛
							</td>
							<td>
								<span>
									<c:if test="${!empty list.notify_num && list.notify_num eq '10' && list.notify_status!=33  }">
										<a href="javascript:updateTuniuRefundNotifyNum('${list.order_id}','${list.notify_id  }');">重新通知</a>
									</c:if>
									<br/>	<a href="/tuniuRefund/queryRefundTicketInfo.do?
										order_id=${list.order_id }&cp_id=${list.cp_id }&refund_id=${list.refund_id }&query_type=1" 
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