<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>预订管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script type="text/javascript">
	<%
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user_level = loginUserVo.getUser_level();
	%>
	function exportExcel() {
		$("form:first").attr("action","/orderForExcel/excelExportGtBooking.do");
		$("form:first").submit();
		$("form:first").attr("action","/gtBooking/queryGtBookList.do");
	}
	$().ready(function(){
		if($("#defaultStartDate").val()==null || $("#defaultStartDate").val()==""){
			$("#defaultStartDate").val(GetDateStr(-15));
		}
	});
	function GetDateStr(AddDayCount){     
        var dd = new Date();     
        dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期     
        var y = dd.getFullYear();     
        var m = dd.getMonth()+1;//获取当前月份的日期  
        if(m<10){     
			m="0"+m;  
		}      
        var d = dd.getDate();
          if(d<10){     
			 d="0"+d;  
			}       
          return y+"-"+m+"-"+d;     
     }
	function selectAll(){
		var checklist = document.getElementsByName("order_status");
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
	
	function selectAllMerchant(){
		var checklist = document.getElementsByName("merchant_id");
		if(document.getElementById("controlAllMerchant").checked){
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
		
//鼠标悬浮于“明细”，显示该订单的操作日志
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
	
	
	//出票系统重新通知出票系统
	function cpNoticeAgain(order_id){  
		$.ajax({
			url:"/gtBooking/cpNoticeAgain.do?order_id="+order_id,
			type: "POST",
			cache: false,
			async: true,
			success: function(data){
				if(data!="success" || data == null){
					alert("操作失败，请重试！！");
				}else{
					alert("操作成功");
				}
			}
		});
	 		
		   
	}   	
</script>
<style>

	tr:hover{background:#ecffff;}
#hint{width:700px;border:1px solid #C1C1C1;background:#F0FAC1;position:fixed;z-index:9;padding:6px;line-height:17px;text-align:left;overflow:auto;} 

</style>
	</head>

	<body><div></div>
		<div class="book_manage oz">
			<form action="/gtBooking/queryGtBookList.do" method="post">
				<ul class="order_num oz" style="margin-top:10px;">
					<li>
						订单号：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="order_id" value="${order_id}"/>
					</li>
					<li>
						商户订单号：&nbsp;
						<input type="text" class="text" name="merchant_order_id" value="${merchant_order_id}"/>
					</li>
					 <li>12306单号：&nbsp;&nbsp;
						<input type="text" class="text" name="out_ticket_billno" value="${out_ticket_billno}"/>
					</li>
				</ul>
				<ul class="order_num oz" style="margin-top:10px;">
					<li>
						开始时间：&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="begin_info_time"
							readonly="readonly" value="${begin_info_time }"
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" id="defaultStartDate1"/>
					</li>
					<li>结束时间：&nbsp;&nbsp;&nbsp;
						<!-- <input type="text" class="text" name="end_info_time" value="${end_info_time }"/> -->
						<input type="text" class="text" name="end_info_time" readonly="readonly" value="${end_info_time }"
										onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>
				</ul>
				<ul class="order_state oz">
					<li>
						&nbsp;&nbsp;&nbsp;订单状态：
					</li>
					<li>
						<input type="checkbox" onclick="selectAll()" name="controlAll" style="controlAll" 
						id="controlAll"/><label for="controlAll">&nbsp;全部&nbsp;&nbsp;</label>
					</li>
					<c:forEach items="${bookStatus }" var="s" varStatus="index">
						<li>
							<input type="checkbox" id="order_status${index.count }"
								name="order_status" value="${s.key }"
								<c:if test="${fn:contains(statusStr, s.key ) }">checked="checked"</c:if> />
							<label for="order_status${index.count }">
								${s.value }
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
					<%if(("2".equals(loginUserVo.getUser_level()))){%>
					<input type="button" value="导出Excel" class="btn" onclick="exportExcel()"/>
					<%} %>
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
								商户订单号
							</th>
							<th>
								票数
							</th>
							<th>
								12306单号
							</th>
							<th>
								出发/到达
							</th>
							<th>
								车次
							</th>
							<th>
								出发时间
							</th>
							<th>
								票价
							</th>
							<th>
								保险
							</th>
							<th>
								总计
							</th>
							<th>
								订单状态
							</th>
							<th>
								创建时间
							</th>
							<th>
								支付时间
							</th>
							<th>
								出票时间
							</th>
							<th>
							  渠道名称
							</th>
							<th>
								手续费
							</th>
							<th>
								操作人
							</th>
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${bookList}" varStatus="idx">
							<tr
								<c:if test="${list.order_status != null && list.order_status=='11'}">
									style="background: #BEE0FC;"
								</c:if>
								<c:if test="${list.order_status != null && list.order_status=='12'}">
									style="background: #BEE0FC;"
								</c:if>
								<c:if test="${list.order_status != null && list.order_status=='22'}">
									style="background: #BEE0FC;"
								</c:if>
								<c:if test="${list.refund_status != null && list.refund_status =='66' }">
									style="background: #E0F3ED;"
								</c:if>
								<c:if test="${list.refund_status != null && list.refund_status =='67' }">
									style="background: #E0F3ED;"
								</c:if>
								<c:if test="${list.refund_status != null && list.refund_status =='55' }">
									style="background: #EA7500;"
								</c:if>
							>
							<td>
								${idx.index+1}
							</td>
							<td>
								${list.order_id}
							</td>
							<td>
								${list.merchant_order_id}
							</td>
							<td>
								${list.ticket_num }
							</td>
							<td>
								${list.out_ticket_billno }
							</td>
							<td>
								${list.from_city}/${list.to_city}
							</td>
							<td>
								${list.train_no }
							</td>
							<td>
								${fn:substringBefore(list.from_time, ' ')}
								<br />
								${fn:substringAfter(list.from_time, ' ')}
							</td>
							<td>
								${list.ticket_pay_money}
							</td>
							<td>
								${list.bx_pay_money}
							</td>
							
							<td>
								${list.pay_money}
							</td>
							<td>
								${bookStatus[list.order_status] }
							</td>
							<td>
								${fn:substringBefore(list.create_time, ' ')}
								<br />
								${fn:substringAfter(list.create_time, ' ')}
							</td>
							<td>
								${fn:substringBefore(list.pay_time, ' ')}
								<br />
								${fn:substringAfter(list.pay_time, ' ')}
							</td>
							<td>
								${fn:substringBefore(list.out_ticket_time, ' ')}
								<br />
								${fn:substringAfter(list.out_ticket_time, ' ')}
							</td>
							<td>
								${list.merchant_name}
							</td>
							<td>
								<c:choose>
									<c:when test="${list.merchant_fee eq '19e_bx'}">
										VIP
									</c:when>
									<c:when test="${list.merchant_fee eq 'merchant_bx'}">
										SVIP
									</c:when>
									<c:otherwise>
										${list.merchant_fee}
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								${list.opt_ren}
							</td>
							<td>
								<span><a href="/gtBooking/gtQueryBookInfo.do?order_id=${list.order_id}"  onmouseover="showdiv('${list.order_id}')" onmouseout="hidediv()" >明细</a>
								</span>
								<!-- 预定成功有些订单出票系统会通知不到订单系统，解决方案 2016年5月6日 -->
								<br/>
								<c:if test="${list.order_status != null && list.order_status=='33'}">
									<span>
									<a href="javascript:cpNoticeAgain('${list.order_id }');"  >重新通知</a>
									</span>
								</c:if>
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
