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
		<title>改签管理页面</title>
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
		

		//鼠标悬浮于“明细”，显示该订单的操作日志
		var heightDiv = 0; 
		function showdiv(order_id){  
		     var oSon = window.document.getElementById("hint");   
		     if (oSon == null) return;   
		     with (oSon){   
		 		 $.ajax({
					url:"/elongAlter/queryOrderOperHistory.do?order_id="+order_id,
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
								if($.trim(data[i].content)!=""){
									$("#historyTable").append("<tr line-height='15px' align='center'><td id='index_'"+index+"''>"+index+"</td><td align='left'   style='word-break:break-all;'>##"
									+data[i].content+"</td><td>"+data[i].create_time+"</td><td>"+data[i].opt_person+"</td></tr>");
									if(data[i].content.length>44){
										heightDiv = heightDiv + 30;
									}else{
										heightDiv = heightDiv + 15;
									}
								}else{
								$("#historyTable").append("<tr line-height='15px' align='center'><td id='index_'"+index+"''>"+index+"</td><td align='left'   style='word-break:break-all;'>##"
									+"null</td><td>"+data[i].create_time+"</td><td>"+data[i].opt_person+"</td></tr>");
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
		//全选refund
		function selectAll(){
			var checklist = document.getElementsByName("change_status");
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
		
		//全选notify
		function selectAllNotify(){
			var checklist = document.getElementsByName("change_notify_status");
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
	    //全选渠道
		function selectAllChannel(){
			var checklist = document.getElementsByName("change_channel");
			if(document.getElementById("controlAllChannel").checked){
				for(var i=0; i<checklist.length; i++){
					checklist[i].checked = 1;
				}
			}else{
				for(var j=0; j<checklist.length; j++){
					checklist[j].checked = 0;
				}
			}
		}
	   function gotoOrderRefund(change_id,order_id ,opt_type){
	   //ajax验证是否锁
		var url = "/elongAlter/queryPayIsLock.do?order_id=" + order_id + "&change_id=" +change_id + "&version=" + new Date();
			$.get(url,function(data){
			if(data != null && data != ""){
				var temp = data ;
				var str1 = temp.split("&") ;
				var str =str1[1]; 
				
				alert("此订单已经锁定，锁定人为"+str);
				return;
			 }else{
				 var channelStr = "";
			 	$("input[name='change_status']:checkbox:checked").each(function(){ 
							channelStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
						}); 
						channelStr = channelStr.substring(0, channelStr.length-1);
				 var notifyStr = "";
			 	$("input[name='change_notify_status']:checkbox:checked").each(function(){ 
							notifyStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
						}); 
						notifyStr = notifyStr.substring(0, notifyStr.length-1);
				$("form:first").attr("action","/elongAlter/ticketInfo.do?change_id="+change_id + "&order_id=" + order_id+ "&opt_type=" + opt_type + "&statusList="+channelStr+ "&notifyList="+ notifyStr+"&pageIndex="+<%=pageIndex%>);
				$("form:first").submit();
			 }
		});
	}
	
	function exportExcel() {
			$("form:first").attr("action","/orderForExcel/excelElongAlter.do");
			$("form:first").submit();
			$("form:first").attr("action","/elongAlter/queryAlterTicketList.do");
	}
	
	//机器改签
	function gotoRobot(change_id,order_id,change_status){
			//ajax验证是否锁
			var url = "/elongAlter/queryPayIsLock.do?order_id="+order_id+"&change_id="+change_id+"&version="+ new Date();
			$.get(url,function(data){
			if(data != null && data != ""){
				var temp = data ;
				var str1 = temp.split("&") ;
				var str =str1[1]; 
				alert("此订单已经锁定，锁定人为"+str);
				return;
			 }else{
					var uri = "/elongAlter/updateRobotAlter.do?change_id="+change_id+"&order_id="+order_id+"&change_status="+change_status+"&version="+new Date();
					$.post(uri,function(data){
						if(data=="yes"){
						 var channelStr = "";
			 			$("input[name='change_status']:checkbox:checked").each(function(){ 
							channelStr += "&change_status="+$(this).val();//遍历被选中CheckBox元素的集合 得到Value值
						}); 
						channelStr = channelStr.substring(0, channelStr.length-1);
						 var notifyStr = "";
			 			$("input[name='change_notify_status']:checkbox:checked").each(function(){ 
							notifyStr += "&change_notify_status="+$(this).val();//遍历被选中CheckBox元素的集合 得到Value值
						}); 
						notifyStr = notifyStr.substring(0, notifyStr.length-1);
							$("form:first").attr("action","/elongAlter/queryAlterTicketList.do?pageIndex="+<%=pageIndex%>+channelStr+notifyStr);
							$("form:first").submit();
						}else{
							alert("机器改签失败");
						}
					});
				 }
			});
		}
</script>
<style>
.liancheng {color: red;}
tr:hover {background: #ecffff;}
#hint{width:700px;border:1px solid #C1C1C1;background:#F0FAC1;position:fixed;z-index:9;padding:6px;line-height:17px;text-align:left;overflow:auto;} 
</style>
	</head>
	<body><div></div>
		<div class="book_manage oz">
			<form action="/elongAlter/queryAlterTicketList.do" method="post">

				<ul class="order_num oz" style="margin-top: 10px;">
					<li>
						订单号：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="order_id"
							value="${order_id}" />
					</li>
					<li>
						改签流水号：&nbsp;
						<input type="text" class="text" name="refund_seq"
							value="${refund_seq}" />
					</li>
					<li>
						12306单号：
						<input type="text" class="text" name="out_ticket_billno"
							value="${out_ticket_billno}" />
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
							操作人：
							<input type="text" class="text" name="opt_person" value="${opt_person }" />
					</li>
				</ul>
				
				<ul class="order_state oz">
					<li>
						&nbsp;&nbsp;&nbsp;改签状态：
					</li>
					<li><input type="checkbox" onclick="selectAll()" name="controlAll" style="controlAll" id="controlAll"/><label for="controlAll">全部</label></li>
					<c:forEach items="${alterStatus}" var="d" varStatus="index">
						<li>
							<input type="checkbox" id="change_status${index.count }"
								name="change_status" value="${d.key }"
								<c:if test="${fn:contains(change_status,d.key) }">checked="checked"</c:if> />
							<label for="change_status${index.count }">
								${d.value }
							</label>
						</li>
					</c:forEach>													
				</ul>
				<ul class="order_state oz">
					<li>
						&nbsp;&nbsp;&nbsp;通知状态：
					</li>
					<li><input type="checkbox" onclick="selectAllNotify()" name="controlAllNotify" 
					style="controlAllNotify" id="controlAllNotify"/>
					<label for="controlAllNotify">全部</label></li>
					<c:forEach items="${notifyStatus}" var="d" varStatus="index">
						<li>
							<input type="checkbox" id="change_notify_status${index.count }"
								name="change_notify_status" value="${d.key }"
								<c:if test="${fn:contains(change_notify_status,d.key) }">checked="checked"</c:if> />
							<label for="change_notify_status${index.count }">
								${d.value }
							</label>
						</li>
					</c:forEach>
				</ul>
				<ul class="order_state oz">
					<li>
						&nbsp;&nbsp;&nbsp;渠道：
					</li>
					<li><input type="checkbox" onclick="selectAllChannel()" name="controlAllChannel" 
					style="controlAllChannel" id="controlAllChannel"/>
					<label for="controlAllChannel">全部</label></li>
					<c:forEach items="${alterChannel}" var="d" varStatus="index">
						<li>
							<input type="checkbox" id="change_channel${index.count }"
								name="change_channel" value="${d.key }"
								<c:if test="${fn:contains(change_channel,d.key) }">checked="checked"</c:if> />
							<label for="change_channel${index.count }">
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
					 <a href="/elongAlter/queryAlterTicketList.do?change_status=12&change_status=31&change_status=32">正在处理</a>&nbsp;
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
								12306单号
							</th>
							<th width="70px">
								创建时间
							</th>
							<th>
								流水号
							</th>
							<th width="70px">
								出发/到达
							</th>
							<th width="70px">
								预订时间
							</th>
							<th>
								改签差额
							</th>
							<th>
								改签状态
							</th>
							<th>
								车次
							</th>
							<th width="70px">
								发车时间
							</th>
							<th width="70px">
								乘车日期
							</th>
							<th width="90px">
								预订失败原因
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
						<c:forEach var="list" items="${alterTicketList}" varStatus="idx">
							<tr
								<c:if test="${fn:contains('11,12,22,31,32', list.change_status )}">
									style="background: #BEE0FC;"
								</c:if>
								<c:if test="${fn:contains('13, 33', list.change_status )}">
									style="background: #E0F3ED;"
								</c:if>
								<c:if test="${fn:contains('15,24,35', list.change_status )}">
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
								${list.out_ticket_billno}
							</td>
							<td>
								${fn:substringBefore(list.create_time, ' ')}
								<br />
								${fn:substringAfter(list.create_time, ' ')}
							</td>
							<td>
								<c:if test="${!empty list.old_ticket_change_serial}">
								<font color="grey">${list.old_ticket_change_serial }<br/></font>
								</c:if>
								<c:if test="${!empty list.new_ticket_change_serial}">
								<font color="red">${list.new_ticket_change_serial }<br/></font>
								</c:if>
								<c:if test="${!empty list.ticket_price_diff_change_serial}">
								<font color="blue">${list.ticket_price_diff_change_serial }<br/></font>
								</c:if>
							</td>
							<td>
								${list.from_city}/${list.to_city}
							</td>
							<td>
								${fn:substringBefore(list.book_ticket_time, ' ')}
								<br />
								${fn:substringAfter(list.book_ticket_time, ' ')}
							</td>
							<td>
								<c:if test="${!empty list.change_diff_money}">
								${list.change_diff_money}
								</c:if>
								<c:if test="${empty list.change_diff_money}">
								${list.change_refund_money}/${list.change_receive_money }
								</c:if>
							</td>
							<td>
								${alterStatus[list.change_status]}
							</td>
							<td>
								<font color="grey">${list.train_no}</font><br/>
								<font color="red">${list.change_train_no }</font>
							</td>
							<td>
								<font color="grey">${fn:substringAfter(list.from_time, ' ')}</font><br/>
								<font color="red">${fn:substringAfter(list.change_from_time, ' ')}</font>
							</td>
							<td>
								<font color="grey">${fn:substringBefore(list.travel_time, ' ')}</font><br/>
								<font color="red">${fn:substringBefore(list.change_travel_time , ' ')}</font>
							</td>
							<td>
								${failReason[list.fail_reason]}
							</td>
							
							<td>
								${notifyStatus[list.change_notify_status]}
							</td>
							<td>
								${list.opt_ren}
							</td>
							<td>
								<span>
								<c:choose> 
									<c:when test="${fn:contains('13', list.change_status)}"> 
										<a href="javascript:gotoOrderRefund('${list.change_id}','${list.order_id }','1');">人工改签</a> 
										<c:if test="${refund_and_alert eq '1'}">
												<a href="javascript:gotoRobot('${list.change_id}','${list.order_id}','${list.change_status}');">机器改签</a> 	
										</c:if>
									</c:when> 
									
									<c:when test="${fn:contains('33', list.change_status)}"> 
										<a href="javascript:gotoOrderRefund('${list.change_id}','${list.order_id }','2');">人工支付</a> 
										<c:if test="${refund_and_alert eq '1'}">
												<a href="javascript:gotoRobot('${list.change_id}','${list.order_id}','${list.change_status}');">机器支付</a> 	
										</c:if>
									</c:when> 
								</c:choose>  
								</span>
								<br/>
								<span>
								<a href="/elongAlter/ticketInfo.do?change_id=${list.change_id}&order_id=${list.order_id }&opt_type=0" onmouseover="showdiv('${list.order_id}')" onmouseout="hidediv()">明细</a>
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
