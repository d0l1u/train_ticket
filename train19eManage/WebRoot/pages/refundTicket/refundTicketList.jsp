<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<%@ page import="com.l9e.transaction.vo.PageVo"%>
<%@ page import="java.util.*"%>
<%@ page import="com.l9e.util.JSONUtil"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>退票管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	    <script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
	    <script language="javascript" src="/js/json2.js"></script>
	    <script type="text/javascript">
	    	//全选
		    	
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
			
			
			function selectAllrefund_statuses(){
			var checklist = document.getElementsByName("refund_status");
			if(document.getElementById("controlAllrefund_statuses").checked){
				for(var i=0; i<checklist.length; i++){
					checklist[i].checked = 1;
				}
			}else{
				for(var j=0; j<checklist.length; j++){
					checklist[j].checked = 0;
				}
			}
		}
	    	
	    <%
			PageVo pageObject = (PageVo) request.getAttribute("pageBean");
			int pageIndex = pageObject.getPageIndex();
		%>
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
		shuaXinAll();	shuaXinAllAuto();
});	
	    	
	    	
	    	function gotoRefundTicketInfo(order_id,cp_id,stream_id){
		    	var url="/refundTicket/queryRfundTicketIsLock.do?order_id="+order_id+"&stream_id="+stream_id+ "&cp_id=" + cp_id+"&version="+new Date();
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
						var typeStr = "";
						$("input[name='refund_type']:checkbox:checked").each(function(){ 
							typeStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
						}); 
						typeStr = typeStr.substring(0, typeStr.length-1);
						$("form:first").attr("action","/refundTicket/queryRefundTicketInfo.do?order_id="+order_id + "&cp_id=" + cp_id +"&stream_id="+stream_id+ "&isActive=1&statusList="+channelStr +"&typeList="+typeStr+"&pageIndex="+<%=pageIndex%>);
						$("form:first").submit();
						$("form:first").attr("action","/refundTicket/queryRefundTicketList.do");
				     }
				    //window.location="/refundTicket/queryRefundTicketInfo.do?order_id="+order_id+"&cp_id="+cp_id+"&stream_id="+stream_id;
				});
			    
	    	}
	    	function exportExcelRefund() {
				$("form:first").attr("action","/orderForExcel/excelexportForRefund.do");
				$("form:first").submit();
				$("form:first").attr("action","/refundTicket/queryRefundTicketList.do");
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
			function gotoRobotGai(stream_id, order_id, cp_id, refund_seq, order_type){
				//ajax验证是否锁
				var url="/refundTicket/queryRfundTicketIsLock.do?stream_id="+stream_id +"&version="+new Date();
				$.get(url,function(data){
					if(data != null && data != ""){
						var temp = data ;
						var str1 = temp.split("&") ;
						var str =str1[1]; 
						alert("此订单已经锁定，锁定人为"+str);
						return;
					 }else{
						var uri = "/refundTicket/updateOrderstatusToRobotGai.do?stream_id="+stream_id+"&order_id="+order_id+"&cp_id="+cp_id+"&refund_seq="+refund_seq+"&refund_status=01&version="+new Date();
						$.post(uri,function(data){
							if(data=="yes"){
								$("form:first").attr("action","/refundTicket/queryRefundTicketList.do?pageIndex="+<%=pageIndex%>);
								$("form:first").submit();
							}else{
								alert("机器改签失败");
							}
						});
						//window.location= "/refundTicket/updateGezhiRefund.do?stream_id="+stream_id+"&order_id="+order_id+"&cp_id="+cp_id+"&refund_seq="+refund_seq+"&refund_status=01&version="+new Date();
					 }
				});
			}
			//机器退票
			function gotoRobotRefund(stream_id, order_id, cp_id, refund_seq, order_type){
				//ajax验证是否锁
				var url="/refundTicket/queryRfundTicketIsLock.do?stream_id="+stream_id +"&version="+new Date();
				$.get(url,function(data){
					if(data != null && data != ""){
						var temp = data ;
						var str1 = temp.split("&") ;
						var str =str1[1]; 
						alert("此订单已经锁定，锁定人为"+str);
						return;
					 }else{
						 var uri = "/refundTicket/updateOrderstatusToRobotGai.do?stream_id="+stream_id+"&order_id="+order_id+"&cp_id="+cp_id+"&refund_seq="+refund_seq+"&refund_status=05&version="+new Date();
						 $.post(uri,function(data){
							if(data=="yes"){
								$("form:first").attr("action","/refundTicket/queryRefundTicketList.do?pageIndex="+<%=pageIndex%>);
								$("form:first").submit();
							}else{
								alert("机器退票失败");
							}
						});
					 }
				});
			}

			//鼠标悬浮于“明细”，显示该订单的操作日志
			var heightDiv = 0; 
			function showdiv(order_id,cp_id,refund_type){  
			     var oSon = window.document.getElementById("hint");   
			     if (oSon == null) return;   
			     with (oSon){   
			 		 $.ajax({
						url:"/refundTicket/queryRefundHistoryInfo.do?order_id="+order_id+"&cp_id="+cp_id+"&refund_type="+refund_type,
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
									$("#historyTable").append("<tr line-height='15px' align='center'><td id='index_'"+index+"''>"+index+"</td><td align='left'   style='word-break:break-all;'>"+data[i].order_optlog+"</td>"+
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
			
			function gotoNotifyAgain(order_id,cp_id,stream_id){
			  if(confirm("确认重新通知么？")) {
			  var url ="/refundTicket/gotoNotifyAgain.do?order_id=" +order_id+"&cp_id="+cp_id+"&stream_id="+stream_id;
			 	  $.get(url,function(data){
						if(data == "true"){
							$("form:first").submit();
							}
					});
			  }
			 }
			 
			 
			 
			 
	var jsonArr = [];　
	//批量机器处理  
	function submitBatchToRobot(){
		var orderIdStr = "";
		var orderIdNum = 0;
		var str = "";
		$("input[name='stream_id']:checkbox:checked").each(function(){ //遍历被选中CheckBox元素的集合 得到Value值
			var stream_id = $(this).val();
			if($("#orderStatus_"+stream_id).val()==00 && $("#orderType_"+stream_id).val()==3){//订单状态为出票失败退款的准备退款
				//验证订单是否加锁
				var url = "/refundTicket/queryRfundTicketIsLock.do?stream_id="+$(this).val()+"&version="+new Date();
				$.get(url,function(data){
					if(data != null && data != ""){
						var temp = data ;
						var str1 = temp.split("&") ;
						var str =str1[1]; 
						//alert("订单"+$(this).val()+"已经锁定，锁定人为"+str);
						//continue;
					 }else{
						var create_time = $("#createTime_"+stream_id).val();//得到该订单的创建时间
						var json = {"stream_id": stream_id, "create_time": create_time};
						jsonArr.push(JSON.stringify(json));
					 }
				});
				orderIdNum++;
			}
		}); 
		if(orderIdNum==0){
			alert("请选中一个状态为【出票失败退款】的订单!");
			return false;
		}
		if(confirm("确认批量出票失败退款处理吗 ？")){
			document.myform.action="/refundTicket/updateBatchToRobot.do?pageIndex="+<%=pageIndex%>+"&jsonArr="+jsonArr+"&version="+new Date();
			document.myform.submit();
		}
	}
	
	var jsonArr2 = [];　
	//批量机器处理  
	function submitBatchToRobot2(){
		var orderIdStr = "";
		var orderIdNum = 0;
		var str = "";
		$("input[name='stream_id']:checkbox:checked").each(function(){ //遍历被选中CheckBox元素的集合 得到Value值
			var stream_id = $(this).val();
			if($("#orderStatus_"+stream_id).val()==00 && $("#orderType_"+stream_id).val()==2){//订单状态为出票失败退款的准备退款
				//验证订单是否加锁
				var url = "/refundTicket/queryRfundTicketIsLock.do?stream_id="+$(this).val()+"&version="+new Date();
				$.get(url,function(data){
					if(data != null && data != ""){
						var temp = data ;
						var str1 = temp.split("&") ;
						var str =str1[1]; 
					 }else{
						var create_time = $("#createTime_"+stream_id).val();//得到该订单的创建时间
						var json = {"stream_id": stream_id, "create_time": create_time};
						jsonArr2.push(JSON.stringify(json));
					 }
				});
				orderIdNum++;
			}
		}); 
		if(orderIdNum==0){
			alert("请选中一个状态为【差额退款】的订单!");
			return false;
		}
		if(confirm("确认批量差额退款处理吗 ？")){
			document.myform.action="/refundTicket/updateBatchToRobot2.do?pageIndex="+<%=pageIndex%>+"&jsonArr="+jsonArr2+"&version="+new Date();
			document.myform.submit();
		}
	}
	
//全选操作
	function checkChnRetRuleAll(){
	      var order_id = document.getElementsByName("stream_id");
	      var tag1 = document.getElementById("tag1").value;
	      if(tag1 == 0 ){
	         for(var i = 0 ; i < order_id.length ; i++){
	        	 order_id[i].checked = true ;
	         }
	         document.getElementById("tag1").value = 1;
	      }else if(tag1 == 1){
	         for(var i = 0 ; i < order_id.length ; i++){
	        	 order_id[i].checked = false ;
	         }
	         document.getElementById("tag1").value = 0;
	      }
	}
	
	function uploadAddRefund(){
		$("form:first").attr("action","/refundTicket/uploadAddRefund.do");
		$("form:first").submit();
	}
	
</script>
<style>
		#hint{width:700px;border:1px solid #C1C1C1;background:#F0FAC1;position:fixed;z-index:9;padding:6px;line-height:17px;text-align:left;overflow:auto;} 
</style>
	</head>
	<body onload="shuaXinAll();"><div></div>
		<div class="book_manage oz">
			<form action="/refundTicket/queryRefundTicketList.do" method="post" name="myform" id="myform">
				<div style="border: 0px solid #00CC00; margin: 10px;">
					<ul class="order_num oz" style="margin-top: 10px;">
						<li>
							订&nbsp;&nbsp;单&nbsp;&nbsp;号：
							<input type="text" class="text" name="order_id" value="${order_id }" />
						</li>
						<li>
							EOP订单号：
							<input type="text" class="text" name="eop_order_id" value="${eop_order_id }" />
						</li>
					
		       				<li>
							退&nbsp;款&nbsp;流&nbsp;水&nbsp;号&nbsp;：
							<input type="text" class="text" name="refund_seq" value="${refund_seq }" />
						</li>
					</ul>
					<ul class="order_num oz" style="margin-top: 10px;">
						<li>
							开始时间&nbsp;&nbsp;：
							<!-- <input type="text" class="text" name="beginInfo_time" value="${beginInfo_time }"/> -->
							<input type="text" class="text" name="begin_create_time" readonly="readonly" value="${begin_create_time }"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})"/> 
						</li>
						<li>
							结束时间&nbsp;：
							<!-- <input type="text" class="text" name="endInfo_time" value="${endInfo_time}"/> -->
								<input type="text" class="text" name="end_create_time" readonly="readonly" value="${end_create_time}"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" /> 
						</li>
						<li>
							12306退款流水号：
							<input type="text" class="text" name="refund_12306_seq" value="${refund_12306_seq }" />
						</li>
						<li>
							操作人：
							<input type="text" class="text" name="opt_person" value="${opt_person }" />
						</li>
					</ul>
					<ul class="order_num oz" style="margin-top: 10px;">
						<li>退款类型：
						
							<input type="checkbox" onclick="selectAllrefund_type()" 
							name="controlAllrefund_type" style="controlAllrefund_type" 
							id="controlAllrefund_type"/>
							<label for="">&nbsp;全部</label>
							</li>
							<c:forEach items="${refund_types }" var="s" varStatus="index">
							<li>
								<input type="checkbox" id="refund_type${index.count }"
									name="refund_type" value="${s.key }"
									<c:if test="${fn:contains(refund_typeStr, s.key ) }">checked="checked"</c:if> />
								<label for="refund_type${index.count }">
									${s.value }
								</label>
							</li>
						</c:forEach>
						
					</ul>
					
					<dl class="oz" style="padding-top:10px;">
						<dt style="float:left;padding-left:40px;line-height:22px;">退款状态：&nbsp;</dt>
						<dd  style="float:left;width:1050px;">
							<div class="ser-item" style="float:left;white-space:nowrap;width:80px;">
								<input type="checkbox" onclick="selectAll()" name="controlAll" style="controlAll" id="controlAll"/><label for="controlAll">&nbsp;&nbsp;全部</label>
							</div>
							<c:forEach items="${refund_statuses }" var="s" varStatus="index">
								<div class="ser-item" style="float:left;white-space:nowrap;width:80px;">
									<input type="checkbox" id="refund_status${index.count }"
										name="refund_status" value="${s.key }"
										<c:if test="${fn:contains(refund_statusStr, s.key ) }">checked="checked"</c:if> />
									<label for="refund_status${index.count }">
										${s.value }
									</label>
								</div>
							</c:forEach>
							<div class="ser-item" style="float:left;white-space:nowrap;width:80px;">
								<input type="checkbox" id="refund_status01" name="refund_status" value="012"
								<c:if test="${fn:contains(refund_statusStr, '012')}">checked="checked"</c:if> />
								<label for="refund_status01">正在改签</label>
							</div>
							<div class="ser-item" style="float:left;white-space:nowrap;width:80px;">
								<input type="checkbox" id="refund_status02" name="refund_status" value="456"
								<c:if test="${fn:contains(refund_statusStr, '456')}">checked="checked"</c:if> />
								<label for="refund_status02">正在退票</label>
							</div>
						</dd>
					</dl>
			
					<ul class="order_num oz" style="margin-top: 10px;">
					<li>订单来源：
					<c:forEach items="${orderSource }" var="s" varStatus="index">
							<input type="checkbox" id="order_source${index.count }"
								name="order_source" value="${s.key }" value="1"
								<c:if test="${fn:contains(orderSourceStr, s.key ) }">checked="checked"</c:if> />
							<label for="order_source${index.count }">
								${s.value }
							</label>
					</c:forEach>
					</li>
				</ul>
				<ul class="order_num oz" style="margin-top: 10px;">
						<li><b><font color="red">通知失败</font></b>：
						&nbsp;&nbsp;预订
								【	去哪：<input value="" id="bookQunarCount" style="color:#f60;width:20px;border:0;"/>
			       					艺龙：<input value="" id="bookElongCount" style="color:#f60;width:20px;border:0;"/>
			       					同程：<input value="" id="bookTcCount" style="color:#f60;width:20px;border:0;"/>
			       					商户：<input value="" id="bookExtCount" style="color:#f60;width:20px;border:0;"/>
			       				】
						&nbsp;&nbsp;&nbsp;&nbsp;退款
								【	去哪：<input value="" id="refundQunarCount" style="color:#f60;width:20px;border:0;"/>
			       					艺龙：<input value="" id="refundElongCount" style="color:#f60;width:20px;border:0;"/>
			       					同程：<input value="" id="refundTcCount" style="color:#f60;width:20px;border:0;"/>
			       					商户：<input value="" id="refundExtCount" style="color:#f60;width:20px;border:0;"/>
							】
						</li>
							<li>
						 <%LoginUserVo loginUserVo= (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		       			 	if("2".equals(loginUserVo.getUser_level())){%>
		       			 	<a href="/refundTicket/updateRefreshNotice.do">刷新退款通知次数</a>
		       			 	<%} %>
		       			</li> 
					</ul>
				<ul class="order_num oz" style="margin-top: 10px;">
						<li>	
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
						</li>
				</ul>
				</div>
				<br/>
			
				<p>
					<input type="submit" value="查 询" class="btn" />
					<%if(("2".equals(loginUserVo.getUser_level())) ){%>
						<input type="button" value="导出Excel" class="btn" onclick="exportExcelRefund()"/>
					<%}
					  if("2".equals(loginUserVo.getUser_level())){%>
		       			<input type="button" value="生成差额退款" class="btn" onclick="location.href = '/refundTicket/AddrefundTicket.do?type=2'"/>
		       			<input type="button" value="生成线下退款" class="btn" onclick="location.href = '/refundTicket/AddrefundTicket.do?type=8'"/>
		       		<%} %>
		       			<input type="button" value="批量失败退款" onclick="submitBatchToRobot();" class="btn" />
		       			<input type="button" value="批量差额退款" onclick="submitBatchToRobot2();" class="btn" />
		       		<%
						if ("2".equals(loginUserVo.getUser_level()) ) {
           			%>
						<input type="button" value="批量车站退票" class="btn" onclick="uploadAddRefund()" />
					<%} %>	 
		       		&nbsp;<a href="/refundTicket/queryRefundTicketList.do?refund_status=012&refund_status=456">正在处理</a>
				
				</p>
				<div id="hint" class="pub_con" style="display:none"></div>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #f0f0f0;">
						<th style="width:30px;">全选 <br/><input type="checkbox" id="checkChnRetRulAll" name="checkChnRetRulAll" onclick="checkChnRetRuleAll()"/></th>
							<th>
								序号
							</th>
							<th>
								订单号
							</th>
							<th>
								EOP订单号
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
							<th width="70px">
								创建时间
							</th>
							<th width="70px">
								发车时间
							</th>
							<th>
								退款状态
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
		            		<c:if test="${fn:contains('00,03,07,09', list.refund_status )}">
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
		            		<c:if test="${fn:contains('01,02,04,05,06', list.refund_status )}">
								style="background: #BEE0FC;"
							</c:if>
		            		>
            				<td>
								<input type="checkbox" id="stream_id" name="stream_id" value="${list.stream_id }"/>
								<input type="hidden" id="orderStatus_${list.stream_id }" value="${list.refund_status }" />
								<input type="hidden" id="createTime_${list.stream_id }" value="${list.create_time }" />
								<input type="hidden" id="orderType_${list.stream_id }" value="${list.refund_type }" />
							</td>
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
								${list.eop_order_id }
							</td>
							<td>
								${list.cp_id }
							</td>
							<c:choose>
								<c:when test="${list.refund_type !=null && list.refund_type eq '2'}">
									<td style="color:#0ff;">
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
								${refundStatuses[list.refund_status] }
							</td>
							<td>
								${list.opt_person }
							</td>
							<td>
								${returnOptlog[list.return_optlog] }
							</td>
							<td>
									<%if(!"1.2".equals(loginUserVo.getUser_level())){ %>
									<c:if test="${list.refund_status eq '00' && list.refund_type eq '1'}">
										<a href="javascript:gotoRefundTicketInfo('${list.order_id}','${list.cp_id  }','${list.stream_id }');">用户退款</a>
									</c:if>
									<c:if test="${list.refund_status eq '03' && list.refund_type eq '1'}">
										<a href="javascript:gotoRefundTicketInfo('${list.order_id}','${list.cp_id  }','${list.stream_id }');">人工改签</a>
										<c:if test="${refund_and_alert eq '1'}">
											<a href="javascript:gotoRobotGai('${list.stream_id }', '${list.order_id}','${list.cp_id  }','${list.refund_seq}');">机器改签</a> 
										</c:if>
									</c:if>
									<c:if test="${list.refund_status eq '07' && list.refund_type eq '1'}">
										<a href="javascript:gotoRefundTicketInfo('${list.order_id}','${list.cp_id  }','${list.stream_id }');">人工退票</a>
										<c:if test="${refund_and_alert eq '1'}">
											<a href="javascript:gotoRobotRefund('${list.stream_id }','${list.order_id}','${list.cp_id  }','${list.refund_seq}');">机器退票</a> 
										</c:if>
									</c:if>
									<c:if test="${list.refund_status eq '09' && list.refund_type eq '1'}">
										<a href="javascript:gotoRefundTicketInfo('${list.order_id}','${list.cp_id  }','${list.stream_id }');">审核退款</a>
									</c:if>
									<c:if test="${list.refund_status eq '99'&& list.refund_type eq '1'}">
								<!--	<a href="javascript:gotoRefundTicketInfo('${list.order_id}','${list.cp_id  }','${list.stream_id }');">搁置订单</a>  -->
									</c:if>
									<c:if test="${list.refund_status eq '00'&& list.refund_type eq '2'}">
										<a href="javascript:gotoRefundTicketInfo('${list.order_id}','${list.cp_id  }','${list.stream_id }');">差额退款</a>
									</c:if>
									<c:if test="${list.refund_status eq '00'&& list.refund_type eq '8'}">
										<a href="javascript:gotoRefundTicketInfo('${list.order_id}','${list.cp_id  }','${list.stream_id }');">线下退款</a>
									</c:if>
									<c:if test="${list.refund_status eq '00'&& list.refund_type eq '3'}">
										<a href="javascript:gotoRefundTicketInfo('${list.order_id}','${list.cp_id  }','${list.stream_id }');">出票失败退款</a>
									</c:if>
									<c:if test="${list.refund_status eq '00'&& list.refund_type eq '7'}">
										<a href="javascript:gotoRefundTicketInfo('${list.order_id}','${list.cp_id  }','${list.stream_id }');">取消预约退款</a>
									</c:if>
									<%} %><br/>
										<a href="/refundTicket/queryRefundTicketInfo.do?
										order_id=${list.order_id }&cp_id=${list.cp_id }&stream_id=${list.stream_id }&refund_type=${list.refund_type }&query_type=1" onmouseover="showdiv('${list.order_id}','${list.cp_id }','${list.refund_type }')" onmouseout="hidediv()">明细</a>
									<c:if test="${list.refund_status eq '22'}">
										<a href="javascript:gotoNotifyAgain('${list.order_id}','${list.cp_id  }','${list.stream_id }');">重新通知</a>
									</c:if>
							</td>
							</tr>
						</c:forEach>
					</table>
					<jsp:include page="/pages/common/paging.jsp" />
					<input type="hidden" id="tag1" name="tag1" value="0" />
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