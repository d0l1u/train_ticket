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
		$("form:first").attr("action","/orderForExcel/excelExportAppBooking.do");
		$("form:first").submit();
		$("form:first").attr("action","/appbooking/queryAppBookList.do");
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
	
	//鼠标悬浮于“明细”，显示该订单的操作日志
	var heightDiv = 0; 
	function showdiv(order_id){  
	     var oSon = window.document.getElementById("hint");   
	     if (oSon == null) return;   
	     with (oSon){   
	 		 $.ajax({
				url:"/appbooking/queryOrderOperHistory.do?order_id="+order_id,
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

	tr:hover{background:#ecffff;}
#hint{width:700px;border:1px solid #C1C1C1;background:#F0FAC1;position:fixed;z-index:9;padding:6px;line-height:17px;text-align:left;overflow:auto;} 

</style>
	</head>

	<body><div></div>
		<div class="book_manage oz">
			<form action="/appbooking/queryAppBookList.do" method="post">
				<ul class="order_num oz" style="margin-top:10px;">
					<li>
						订单号：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="order_id" value="${order_id}"/>
					</li>
					<li>12306单号：&nbsp;&nbsp;
						<input type="text" class="text" name="out_ticket_billno" value="${out_ticket_billno}"/>
					</li>
					<li>用户电话：&nbsp;&nbsp;
						<input type="text" class="text" name="user_phone" value="${user_phone }"/>
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
						<input type="checkbox" onclick="selectAll()" name="controlAll" style="controlAll" id="controlAll"/><label for="">&nbsp;全部&nbsp;&nbsp;</label>
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
						&nbsp;&nbsp;&nbsp;支付方式：
					</li>
					<c:forEach items="${payTypes }" var="s" varStatus="index">
						<li>
							<input type="checkbox" id="pay_type${index.count }"
								name="pay_type" value="${s.key }"
								<c:if test="${fn:contains(payTypeStr, s.key ) }">checked="checked"</c:if> />
							<label for="pay_type${index.count }">
								${s.value }
							</label>
						</li>
					</c:forEach>
				</ul>
				<ul class="order_state oz">
					<li>
						&nbsp;&nbsp;&nbsp;渠&nbsp;&nbsp;&nbsp;&nbsp;道：
					</li>
					<c:forEach items="${channelTypes }" var="s" varStatus="index">
						<li>
							<input type="checkbox" id="channel${index.count }"
								name="channel" value="${s.key }"
								<c:if test="${fn:contains(channelStr, s.key ) }">checked="checked"</c:if> />
							<label for="channel${index.count }">
								${s.value }
							</label>
						</li>
					</c:forEach>
				</ul>
				<p>
					<input type="submit" value="查 询" class="btn" />
					<%
						if ("2".equals(loginUserVo.getUser_level()) ) {
           			%>
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
								支付方式
							</th>
							<th>
								用户电话
							</th>
							<th>
								渠道
							</th>
							<th>
								操作人
							</th>
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${bookList}" varStatus="idx">
							<c:choose>
								<c:when test="${list.order_status != null && list.order_status=='11'}">
									<tr style="background: #BEE0FC;">
								</c:when>
								<c:when test="${list.order_status != null && list.order_status=='12'}">
									<tr style="background: #BEE0FC;">
								</c:when>
								<c:when test="${list.order_status != null && list.order_status=='22'}">
									<tr style="background: #BEE0FC;">
								</c:when>
								<c:when test="${list.refund_status != null && list.refund_status =='66' }">
									<tr style="background: #E0F3ED;">
								</c:when>
								<c:when test="${list.refund_status != null && list.refund_status =='67' }">
									<tr style="background: #E0F3ED;">
								</c:when>
								<c:when test="${list.refund_status != null && list.refund_status =='55' }">
									<tr style="background: #EA7500;">
								</c:when>
								<c:otherwise>
									<tr>
								</c:otherwise>
							</c:choose>
							<td>
								${idx.index+1}
							</td>
							<td>
								${list.order_id}
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
								${payTypes[list.pay_type] }
							</td>
							<td>
								${list.app_phone}
							</td>
							<td>
								${channelTypes[list.channel] }
							</td>
							<td>
								${list.opt_ren}
							</td>
							<td>
								<span><a href="/appbooking/appQueryBookInfo.do?order_id=${list.order_id}"  onmouseover="showdiv('${list.order_id}')" onmouseout="hidediv()" >明细</a>
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