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
		<title>京东退款管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script language="javascript" src="/js/json2.js"></script>
		<script type="text/javascript">
		<%
			PageVo pageObject = (PageVo) request.getAttribute("pageBean");
			int pageIndex = pageObject.getPageIndex();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String user_level = loginUserVo.getUser_level();
		%>
		function exportExcel() {
			$("form:first").attr("action","/jdRefund/exportrefundexcel.do");
			$("form:first").submit();
			$("form:first").attr("action","/jdRefund/queryJDRefundList.do");
		}
		
		function gotoOrderRefund(order_id,cp_id, refund_seq, channel, order_type){
		//ajax验证是否锁
		var url = "/jdRefund/queryPayIsLock.do?order_id="+order_id+"&cp_id="+cp_id+ "&refund_seq="+ refund_seq+"&version="+new Date();
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
			 			
				$("form:first").attr("action","/jdRefund/queryJDRefundInfo.do?order_id="+order_id +"&cp_id="+cp_id+ "&refund_seq=" + refund_seq + "&isActive=1&statusList="+channelStr+"&pageIndex="+<%=pageIndex%>);
				$("form:first").submit();
			 }
		 	 
		});
	}
	
		
		//鼠标悬浮于“明细”，显示该订单的操作日志
		var heightDiv = 0; 
		function showdiv(order_id,cp_id){  
		     var oSon = window.document.getElementById("hint");   
		     if (oSon == null) return;   
		     with (oSon){   
		 		 $.ajax({
					url:"/jdRefund/queryOrderOperHistory.do?order_id="+order_id+"&cp_id="+cp_id,
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
								$("#historyTable").append("<tr line-height='15px' align='center'><td id='index_'"+index+"''>"+index+
								"</td><td align='left' style='word-break:break-all;'>"+data[i].order_optlog+"</td>"+
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
		

		//机器退票
		function gotoRobotRefund(order_id, cp_id, refund_seq,channel, order_type){
			//ajax验证是否锁
			var url = "/jdRefund/queryPayIsLock.do?order_id="+order_id+ "&refund_seq=" + refund_seq+"&version="+new Date();
			$.get(url,function(data){
				if(data != null && data != ""){
					var temp = data ;
					var str1 = temp.split("&") ;
					var str =str1[1]; 
					alert("此订单已经锁定，锁定人为"+str);
					return;
				 }else{
					var uri = "/jdRefund/updateOrderstatusToRobot.do?order_id="+order_id+"&cp_id="+cp_id+"&refund_status=01&version="+new Date();
					$.post(uri,function(data){
						if(data=="yes"){
							$("form:first").attr("action","/jdRefund/queryJDRefundList.do?pageIndex="+<%=pageIndex%>);
							$("form:first").submit();
						}else{
							alert("机器退票失败");
						}
					});
				 }
			});
		}

$().ready(function(){
		changecolor();
});	
function changecolor(){
	var a=document.getElementById("urgent_order").value;
	if(a!=""){
	document.getElementById("urgent_order_color").style.color="red";
	}
}
function submitForm(){
	document.getElementById("urgent_order").value="";
	$("form:first").attr("action","/jdRefund/queryJDRefundList.do");
	$("form:first").submit();
}

function urgertOrder(){
	$("form:first").attr("action","/jdRefund/queryRefundPage.do?urgent_order=right_now");
	$("form:first").submit();
}

	
	//全选操作
	function checkChnRetRuleAll(){
	      var order_id = document.getElementsByName("refund_seq");
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

</script>
<style>
.liancheng {color: red;}
tr:hover {background: #ecffff;}
#hint{width:700px;border:1px solid #C1C1C1;background:#F0FAC1;position:fixed;z-index:9;padding:6px;line-height:17px;text-align:left;overflow:auto;} 
</style>
</head>
	<body>
		<div></div>
		<div class="book_manage oz">
			<form action="/jdRefund/queryJDRefundList.do" method="post">

				<ul class="order_num oz" style="margin-top: 10px;">
					<li>
						订单号：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="order_id"
							value="${order_id}" />
					</li>
					<li>
						京东流水号：&nbsp;
						<input type="text" class="text" name="jd_order_id"
							value="${jd_order_id}" />
					</li>
			
				</ul>
				<ul class="order_num oz" style="margin-top: 10px;">
					<li>
						开始时间：&nbsp;&nbsp;&nbsp;
						<!-- <input type="text" class="text" name="begin_info_time" value="${begin_info_time }"/> -->
						<input type="text" class="text" name="begin_info_time"
							readonly="readonly" value="${begin_info_time}"
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>
					<li>
						结束时间：&nbsp;&nbsp;&nbsp;
						<!-- <input type="text" class="text" name="end_info_time" value="${end_info_time }"/> -->
						<input type="text" class="text" name="end_info_time"
							readonly="readonly" value="${end_info_time}"
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>
				
				</ul>
		
			
				<ul class="order_state oz">
					<li>
						&nbsp;&nbsp;&nbsp;退款状态：
					</li>
					<li><input type="checkbox" onclick="selectAll()" name="controlAll" style="controlAll" id="controlAll"/><label for="controlAll">全部</label></li>
					<li><input type="checkbox" id="refund_status1" name="refund_status" value="02"
								<c:if test="${fn:contains(refund_status, '02')}">checked="checked"</c:if> />
								<label for="refund_status1">正在退票</label></li>
					<li><input type="checkbox" id="refund_status2" name="refund_status" value="06"
								<c:if test="${fn:contains(refund_status, '06')}">checked="checked"</c:if> />
								<label for="refund_status2">退票结果查询中</label></li>
				
					<li><input type="checkbox" id="refund_status4" name="refund_status" value="09"
								<c:if test="${fn:contains(refund_status, '09')}">checked="checked"</c:if> />
								<label for="refund_status4">人工退票</label></li>
					<li><input type="checkbox" id="refund_status5" name="refund_status" value="10"
					            <c:if test="${fn:contains(refund_status, '10')}">checked="checked"</c:if> />
					            <label for="refund_status5">退票失败</label></li>	
					<li><input type="checkbox" id="refund_status6" name="refund_status" value="11"
								<c:if test="${fn:contains(refund_status, '11')}">checked="checked"</c:if> />
								<label for="refund_status6">退票完成</label></li>	
					 
				</ul>
		
				<input type="hidden" value="${urgent_order}" id="urgent_order" name="urgent_order"/>
				<p>
					<input type="button" value="查 询" class="btn" onclick="submitForm();" />
					<%
						if ("2".equals(loginUserVo.getUser_level()) ) {
           			%>
						<input type="button" value="导出Excel" class="btn" onclick="exportExcel()" />
					<%} %>
					&nbsp;&nbsp; 	 
					<a  href="/jdRefund/queryJDRefundList.do?refund_status=02&refund_status=06">正在处理</a>&nbsp;&nbsp;	
					&nbsp;&nbsp; 
					 	 
					<a id="urgent_order_color" href="javascript:urgertOrder();">紧急订单</a>
					&nbsp;&nbsp;
					
					<a  href="/jdRefund/queryJDRefundList.do?refund_status=09">人工处理</a>&nbsp;&nbsp;	
					&nbsp;&nbsp; 	 
						
				</p>
				<div id="hint" class="pub_con" style="display:none"></div>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #EAEAEA;">
							<th style="width:30px;">全选 <br/><input type="checkbox" id="checkChnRetRulAll" name="checkChnRetRulAll" onclick="checkChnRetRuleAll()"/></th>
							<th>
								序号
							</th>
							<th>
								订单号
							</th>
							<th>
								车票号
							</th>

							<th>
								乘客姓名
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
								退票类型
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
						<c:forEach var="list" items="${jdRefundList}" varStatus="idx">
							<tr 
								<c:if test="${fn:contains('02,06', list.order_status )}">
									style="background: #BEE0FC;"
								</c:if>
								<c:if test="${fn:contains('09', list.order_status )}">
									style="background: #E0F3ED;"
								</c:if>
								<c:if test="${fn:contains('10', list.order_status )}">
									style="background:#FFB5B5;"
								</c:if>
								>
							<td>
								<input type="checkbox" id="refund_seq" name="refund_seq" value="${list.refund_seq}"/>
								<input type="hidden" id="orderStatus_${list.refund_seq }" value="${list.order_status }" />
								<input type="hidden" id="orderId_${list.refund_seq }" value="${list.order_id }" />
								<input type="hidden" id="cpId_${list.refund_seq }" value="${list.cp_id }" />
								<input type="hidden" id="refundSeq_${list.refund_seq }" value="${list.refund_seq}" />
							</td>
							<td>
								${idx.index+1}
							</td>
							
							<td >
								${list.order_id}
							</td>
							<td>
								${list.cp_id}
							</td>
							<td>
								${list.user_name}
							</td>
							<td>
								${list.refund_money }
							</td>
							<td>
								${list.refund_12306_money }
							</td>
							<td>
								${list.refund_percent}
							</td>
							<td width="70px">
								${list.create_time}
							</td>
							<td width="70px">
							<c:choose>
							<c:when test="${list.from_time le now2}"><font color="#FF6600;">${list.from_time}</font></c:when>
							<c:when test="${list.from_time le now4 && list.from_time gt now2}"><font color="#FF0000;"><b>${list.from_time}</b></font></c:when>
							<c:otherwise>${list.from_time}</c:otherwise>
							</c:choose>
							</td>
							<td>
								${refundStatus[list.order_status]}
							</td>
							<td>
							<c:choose>
							<c:when test="${list.refund_type  eq '22'}"><font color="#FF0000;">线下退票</font></c:when>
							<c:otherwise>线上退票</c:otherwise>
							</c:choose>
							</td>
							<td>
								${merchantMap[list.channel]}
							</td>
							<td>
								${list.opt_ren}
							</td>
							<td>
								${returnlogMap[list.return_optlog] }
							</td>
							<td>
								<span>
								<c:choose> 
									
									<c:when test="${fn:contains('09', list.order_status)}"> 
										<a href="javascript:gotoOrderRefund('${list.order_id}','${list.cp_id}','${list.refund_seq}','${list.channel}','0');">人工退票</a> 
										
										<a href="javascript:gotoRobotRefund('${list.order_id}','${list.cp_id}','${list.refund_seq}','${list.channel}','0');">机器退票</a>  
									
									</c:when> 
					
								</c:choose>  
								</span>
								<br/>
								<span>
								<a href="/jdRefund/queryJDRefundInfo.do?order_id=${list.order_id}&cp_id=${list.cp_id }&refund_seq=${list.refund_seq}&isActive=0" onmouseover="showdiv('${list.order_id}','${list.cp_id}')" onmouseout="hidediv()">明细</a>
								</span>
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
