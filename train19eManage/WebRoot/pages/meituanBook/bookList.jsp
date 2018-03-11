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
		<script type="text/javascript" src="jquery-1.3.2.js"></script>
		<script type="text/javascript">
		<%
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user_level = loginUserVo.getUser_level();
	%>
		function exportExcel() {
			//document.getElementById("center").style.display="block";
			//doProgress(); 
			//$("#exportExcel1").attr("disabled",true); 
			$("form:first").attr("action","/meituanBook/exportexcel.do");
			$("form:first").submit();
			$("form:first").attr("action","/meituanBook/queryBookList.do");
		}
		
		
		
		function queryFailure() {
			$("form:first").attr("action","/meituanBook/queryNotifyList.do?notify_status=33");
			$("form:first").submit();
			$("form:first").attr("action","/meituanBook/queryBookList.do");
		}
		//通知成功锁 
		function queren(order_id) {
			var url = "/meituanBook/queryOrderIsLock.do?order_id="+order_id+"&version="+new Date();
			$.get(url,function(data){
				if(data != null && data != ""){
					var temp = data ;
					var str1 = temp.split("&") ;
					var str =str1[1]; 
					alert("此订单已经锁定，锁定人为"+str);
					return;
				 }else{
					 if(confirm("你确认修改为通知成功吗？")){
					  var url ="/meituanBook/notify.do?order_id=" + order_id;
					 	  $.get(url,function(data){
								if(data == "true"){
									$("form:first").submit();
									}
							});
					 }
				 }
			});
		}
		//明细锁 
		function gotoOrderLock(order_id){
			//ajax验证是否锁
			var url = "/meituanBook/queryOrderIsLock.do?order_id="+order_id+"&version="+new Date();
				$.get(url,function(data){
				if(data != null && data != ""){
					var temp = data ;
					var str1 = temp.split("&") ;
					var str =str1[1]; 
					alert("此订单已经锁定，锁定人为"+str);
					return;
				 }else{
					$("form:first").attr("action","/meituanBook/bookInfo.do?order_id="+order_id+"&IsLock=1");
					$("form:first").submit();
				 }
			});
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
	//重新通知
	function notifyAgain(order_id){
	 var url ="/meituanBook/notifyAgain.do?order_id=" +order_id;
		 	  $.get(url,function(data){
					if(data == "true"){
						$("form:first").submit();
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
				url:"/meituanBook/queryOrderOperHistory.do?order_id="+order_id,
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
							$("#historyTable").append("<tr line-height='15px'align='center' ><td id='index_'"+index+"''>"+index+"</td><td align='left'   style='word-break:break-all;'>"+data[i].content+"</td>"+
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
	
 
	var progress_id = "loading"; 
	function SetProgress(progress) { 
	if (progress) { 
	$("#" + progress_id + " > div").css("width", String(progress) + "%"); //控制#loading div宽度 
	$("#" + progress_id + " > div").html(String(progress) + "%"); //显示百分比 
	} 
	} 
	var i = 0; 
	function doProgress() { 
	if (i > 100) { 
	$("#message").html("加载完毕！").fadeIn("slow");//加载完毕提示 
	$("#exportExcel1").attr("disabled",false); 
	return; 
	} 
	if (i <= 100) { 
	setTimeout("doProgress()", 100); 
	SetProgress(i); 
	i++; 
	} 
	} 
	
</script>
<style>		
	.liancheng {color: red;}
	tr:hover {background: #ecffff;}
	#hint{width:700px;border:1px solid #C1C1C1;background:#F0FAC1;position:fixed;z-index:9;padding:6px;line-height:17px;text-align:left;overflow:auto;} 

#center{ 
position:fixed;
z-index:9;
width:400px; 
margin-top: 20%;
margin-left: 30%;
display: none;
} 
#loading{ 
width:397px; 
height:49px; 
background:url(../images/bak.png) no-repeat; 
} 
#loading div{ 
width:0px; 
height:48px; 
background:url(../images/pro.png) no-repeat; 
color:#fff; 
text-align:center; 
font-family:Tahoma; 
font-size:18px; 
line-height:48px; 
} 
#message{ 
width:200px; 
height:35px; 
font-family:Tahoma; 
font-size:12px; 
background-color:#d8e7f0; 
border:1px solid #187CBE; 
display:none; 
line-height:35px; 
text-align:center; 
margin-bottom:10px; 
margin-left:50px; 


</style>
	</head>
	<body><div></div>
	<div id="center"> 
					<div id="message"></div> 
					<div id="loading"><div></div></div> 
					</div>
		<div class="book_manage oz">
			<form action="/meituanBook/queryBookList.do" method="post">
				<ul class="order_num oz" style="margin-top: 10px;">
					<li>
						订单号：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="order_id"
							value="${order_id}" />
					</li>
					<li>
						12306单号：&nbsp;&nbsp;&nbsp;
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
						结束时间：&nbsp;&nbsp;&nbsp;&nbsp;
						<!-- <input type="text" class="text" name="end_info_time" value="${end_info_time }"/> -->
						<input type="text" class="text" name="end_info_time"
							readonly="readonly" value="${end_info_time }"
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>

				</ul>
				<ul class="order_state oz">
					<li>
						&nbsp;&nbsp;&nbsp;订单状态：
					</li>
					<li>
						<input type="checkbox" onclick="selectAll()" name="controlAll" 
						style="controlAll" id="controlAll"/>
						<label for="controlAll">&nbsp;全部&nbsp;&nbsp;</label>
					</li>
					<c:forEach items="${bookStatus}" var="s" varStatus="index">
						<li>
							<input type="checkbox" id="order_status${index.count }"
								name="order_status" value="${s.key }"
								<c:if test="${fn:contains(order_status, s.key)}">checked="checked"</c:if> />
							<label for="order_status${index.count}">
								${s.value}
							</label>
						</li>
					</c:forEach>
				</ul>
				<ul class="order_state oz">
					<li>
						&nbsp;&nbsp;&nbsp;通知状态：
					</li>
					<li>
						<input type="checkbox" onclick="selectAllNotify()" name="controlAllNotify" 
						style="controlAllNotify" id="controlAllNotify"/>
						<label for="controlAllNotify">&nbsp;全部&nbsp;&nbsp;</label>
					</li>
					<c:forEach items="${notifyStatus}" var="s" varStatus="index">
						<li>
							<input type="checkbox" id="notify_status${index.count }"
								name="notify_status" value="${s.key }"
								<c:if test="${fn:contains(notify_status, s.key)}">checked="checked"</c:if> />
							<label for="notify_status${index.count}">
								${s.value}
							</label>
						</li>
					</c:forEach>
				</ul>
				<p>
					<input type="submit" value="查 询" class="btn" />
					<%
						if ("2".equals(loginUserVo.getUser_level()) ) {
           			%>
					<input type="button" id="exportExcel1" value="导出Excel"  class="btn" onclick="exportExcel();"/>
					<%} %>
					<input type="button" value="查询通知失败" class="btn" onclick="queryFailure()"/> 
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
								票数
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
								支付价格
							</th>
							<th>
								成本价格
							</th>
							<th>
								订单状态
							</th>
							<th>
								客户下单时间
							</th>
							<th>
								创建时间
							</th>
							<th>
								出票时间
							</th>
							<th>
								12306单号
							</th>
							<th>
								渠道
							</th>
							<th>
								操作人
							</th>
							<th>
								通知状态
							</th>
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${bookList}" varStatus="idx">
							<tr
								<c:if test="${list.order_status != null && (list.order_status=='11' || list.order_status=='22') && list.notify_status eq '22'}">
									style="background: #BEE0FC;"
								</c:if>
								<c:if test="${list.notify_status eq '33'}">
									style="background: #e0f3ed;"
								</c:if>
							>		
							<td>
								${idx.index+1}
							</td>
							<td>
								${list.order_id}
							</td>
							<td>
								${list.ticket_num }
							</td>
							<td>
								${list.startAndEnd}
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
								${list.pay_money}
							</td>
							<td>
								${list.buy_money}
							</td>
							<td>
								${bookStatus[list.order_status] }
								<br />
							</td>
							<td>
								${fn:substringBefore(list.order_time, ' ')}
								<br />
								${fn:substringAfter(list.order_time, ' ')}
							</td>
							<td>
								${fn:substringBefore(list.create_time, ' ')}
								<br />
								${fn:substringAfter(list.create_time, ' ')}
							</td>
							<td>
								${fn:substringBefore(list.out_ticket_time, ' ')}
								<br />
								${fn:substringAfter(list.out_ticket_time, ' ')}
							</td>
							<td>
								${list.out_ticket_billno}
							</td>
							<td>
								${Channel[list.channel]}
							</td>
							<td>
								${list.opt_ren}
							</td>
							<td>
								${notifyStatus[list.notify_status]}
							</td>
							<td>
								<c:if test="${list.notify_status eq '33'}">
								<a href="javascript:gotoOrderLock('${list.order_id}');">人工操作</a>
									<a href="javascript:queren('${list.order_id}');">通知成功</a><br/>
								</c:if>
								<a href="/meituanBook/bookInfo.do?order_id=${list.order_id}"  onmouseover="showdiv('${list.order_id}')" onmouseout="hidediv()">明细</a> 
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
