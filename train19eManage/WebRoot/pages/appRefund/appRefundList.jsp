<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>退票管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	    <script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
	    <script type="text/javascript">
	    	
	    	function gotoRefundTicketInfo(order_id,cp_id,stream_id){
		    	var url="/appRefund/queryRfundTicketIsLock.do?stream_id="+stream_id;
		    		$.get(url,function(data){
					if(data != null && data != ""){
						var temp = data ;
						var str1 = temp.split("&") ;
						var str =str1[1]; 
						
						alert("此退款已经锁定，锁定人为"+str);
						return;
					 }
				    	window.location="/appRefund/queryRefundTicketInfo.do?order_id="+order_id+"&cp_id="+cp_id+"&stream_id="+stream_id;
				});
			    
	    	}    
	    	function exportExcelRefund() {
				$("form:first").attr("action","/orderForExcel/excelexportForAppRefund.do");
				$("form:first").submit();
				$("form:first").attr("action","/appRefund/queryRefundTicketList.do");
			}
			
		//全选refund
		function selectAllType(){
			var checklist = document.getElementsByName("refund_type");
			if(document.getElementById("controlAllType").checked){
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
		function selectAllStatus(){
			var checklist = document.getElementsByName("refund_status");
			if(document.getElementById("controlAllStatus").checked){
				for(var i=0; i<checklist.length; i++){
					checklist[i].checked = 1;
				}
			}else{
				for(var j=0; j<checklist.length; j++){
					checklist[j].checked = 0;
				}
			}
		}
		
	//鼠标悬浮于“明细”，显示该订单的操作日志
	var heightDiv = 0; 
	function showdiv(stream_id,order_id,cp_id,type){  
	     var oSon = window.document.getElementById("hint");   
	     if (oSon == null) return;   
	     with (oSon){   
	 		 $.ajax({
				url:"/appRefund/queryOrderOperHistory.do?stream_id="+stream_id+"&order_id="+order_id+"&cp_id="+cp_id+"&type="+type,
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
		
	    </script>
	    <style>
	    #hint{width:700px;border:1px solid #C1C1C1;background:#F0FAC1;position:fixed;z-index:9;padding:6px;line-height:17px;text-align:left;overflow:auto;} 
	    </style>
	</head>
	<body><div></div>
		<div class="book_manage oz">
			<form action="/appRefund/queryRefundTicketList.do" method="post" name="myform" id="myform">
				<div style="border: 0px solid #00CC00; margin: 10px;">
					<ul class="order_num oz" style="margin-top: 10px;">
							<li>订单号：&nbsp;&nbsp;&nbsp;&nbsp; 
							<input type="text" class="text" name="order_id" value="${order_id }" /> 
						</li>
						<li>
							注册电话：&nbsp;&nbsp;
							<input type="text" class="text" name="user_phone" value="${user_phone }" />
						</li>
						<li>
						 <%LoginUserVo loginUserVo= (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		       			 	if("2".equals(loginUserVo.getUser_level())){%>
		       			 	<!-- 
		       			 	<a href="/appRefund/updateRefreshNotice.do">刷新退款通知次数</a>
		       			 	 -->
		       			 	<%} %>
		       			 </li>
					</ul>
					<ul class="order_num oz" style="margin-top: 10px;">
						<li>
							开始时间：&nbsp;&nbsp;
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
					</ul>
						<ul class="order_num oz" style="margin-top: 10px;">
						<li>
							退款流水号：
							<input type="text" class="text" name="refund_seq" value="${refund_seq }" />
						</li>
						<li>
							12306退款流水单号：
							<input type="text" class="text" name="refund_12306_seq" value="${refund_12306_seq }" />
						</li>
						<li>
							操作人：
							<input type="text" class="text" name="opt_person" value="${opt_person }" />
						</li>
					</ul>
					<ul class="ser order_num oz" style="margin-top: 10px;">
						<li>	
						退款类型：
						<input type="checkbox" onclick="selectAllType()" name="controlAllType" 
							style="controlAllType" id="controlAllType"/>
							<label for="controlAllType">全部</label>
						<c:forEach items="${refund_types }" var="s" varStatus="index">
								<input type="checkbox" id="refund_type${index.count }"
									name="refund_type" value="${s.key }"
									<c:if test="${fn:contains(refund_typeStr, s.key ) }">checked="checked"</c:if> />
								<label for="refund_type${index.count }">
									${s.value }
								</label>
						</c:forEach>
						</li>
					</ul>
					<ul class="ser order_num oz" style="margin-top: 10px;">
						<li>	
						退款状态：
						<input type="checkbox" onclick="selectAllStatus()" name="controlAllStatus" 
							style="controlAllStatus" id="controlAllStatus"/>
							<label for="controlAllStatu">全部</label>
						<c:forEach items="${refund_statuses }" var="s" varStatus="index">
								<input type="checkbox" id="refund_status${index.count }"
									name="refund_status" value="${s.key }"
									<c:if test="${fn:contains(refund_statusStr, s.key ) }">checked="checked"</c:if> />
								<label for="refund_status${index.count }">
									${s.value }
								</label>
						</c:forEach>
						</li>
					</ul>
					<ul class="ser order_num oz" style="margin-top: 10px;">
						<li>	
						渠&nbsp;&nbsp;&nbsp;&nbsp;道： &nbsp;
						<c:forEach items="${channelTypes }" var="s" varStatus="index">
								<input type="checkbox" id="channel${index.count }"
									name="channel" value="${s.key }"
									<c:if test="${fn:contains(channelStr, s.key ) }">checked="checked"</c:if> />
								<label for="channel${index.count }">
									${s.value }
								</label>
						</c:forEach>
						</li>
					</ul>
				</div>
				<p>
					<input type="submit" value="查 询" class="btn" />
					<%if(("2".equals(loginUserVo.getUser_level())) ){%>
					<input type="button" value="导出Excel" class="btn" onclick="exportExcelRefund()"/>
					<!-- 
						<input type="button" value="生成车站退票" class="btn" onclick="location.href = '/appRefund/toAddStationRefundTicket.do'"/>
					 -->  
		       		<input type="button" value="生成电话退票" class="btn" onclick="location.href = '/appRefund/toAddPhoneRefundTicket.do'"/>	 	
					<%}if("2".equals(loginUserVo.getUser_level())){%>
					<!-- 
		       			 <input type="button" value="生成差额退款" class="btn" onclick="location.href = '/extRefund/AddrefundTicket.do'"/>
		       		 -->	 
		       		<%} %>
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
						<!-- 	<th>
								EOP订单号
							</th>  -->
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
								注册电话
							</th>
							<th>
								渠道
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
						<c:choose>
		            		<c:when test="${fn:contains('00', list.refund_status )}">
		            		 	<tr style="background:#E0F3ED; ">
		            		</c:when>
		            		<c:when test="${fn:contains('11', list.refund_status )}">
		            		 	<tr style="background:#FFDAC8; ">
		            		</c:when>
		            		<c:when test="${fn:contains('22', list.refund_status )}">
		            		 	<tr style="background:#FFB5B5; ">
		            		</c:when>		        
		            		<c:otherwise>
		            			<tr>
		            		</c:otherwise>
            			</c:choose>
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
						<!-- 	<td>
								${list.eop_order_id }
							</td>   -->
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
								${list.create_time }
							</td>
							<td>
								${list.from_time }
							</td>
							<td>
								${refund_statuses[list.refund_status] }
							</td>
							<td>
								${list.user_phone}
							</td>
							<td>
								${channelTypes[list.channel] }
							</td>
							<td>
								${list.opt_person }
							</td>
							<td>
								${returnOptlog[list.return_optlog] }
							</td>
							<td>
								<span>
								
									<%if(!"1.2".equals(loginUserVo.getUser_level())){ %>
									<c:if test="${list.refund_status eq '00' && (list.refund_type eq '1' || list.refund_type eq '3')}">
										<a href="javascript:gotoRefundTicketInfo('${list.order_id}','${list.cp_id  }','${list.stream_id }');">用户退款</a>
									</c:if>
									<c:if test="${!empty list.notify_num && list.notify_num eq '10' && list.notify_status!=33  }">
										<a href="/appRefund/updateExtRefundNotifyNum.do?notify_id=${list.notify_id  }&order_id=${list.order_id }">重新通知</a>
									</c:if>
									<c:if test="${list.refund_status eq '00'&& list.refund_type eq '4'}">
										<a href="javascript:gotoRefundTicketInfo('${list.order_id}','${list.cp_id  }','${list.stream_id }');">差额退款</a>
									</c:if>
									<c:if test="${list.refund_status eq '00'&& list.refund_type eq '5'}">
										<a href="javascript:gotoRefundTicketInfo('${list.order_id}','${list.cp_id  }','${list.stream_id }');">出票失败退款</a>
									</c:if>
									<%} %>
									<c:choose>
										<c:when test="${(list.refund_type eq '1') || (list.refund_type eq '2') || (list.refund_type eq '4') || (list.refund_type eq '5') || (list.refund_type eq '6')}">
											<a href="/appRefund/queryRefundTicketInfo.do?
											order_id=${list.order_id }&cp_id=${list.cp_id }&stream_id=${list.stream_id }&query_type=1"  
											onmouseover="showdiv('${list.stream_id}','${list.order_id}','${list.cp_id}',0)" onmouseout="hidediv()" >明细</a>
										</c:when>
										<c:otherwise>
											<a href="/appRefund/queryRefundPhoneInfo.do?order_id=${list.order_id }&stream_id=${list.stream_id }&query_type=1"  
											onmouseover="showdiv('${list.stream_id}','${list.order_id}','${list.cp_id}',1)" onmouseout="hidediv()" >明细</a>
										</c:otherwise>
									</c:choose>
										
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