<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>超时订单管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script type="text/javascript">
	<%
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user_level = loginUserVo.getUser_level();
	%>
	function exportExcel() {
		$("form:first").attr("action","/orderForExcel/excelExportTuniuBooking.do");
		$("form:first").submit();
		$("form:first").attr("action","/tuniuBooking/queryTuniuBookList.do");
	}

	function selectAll(){
		var checklist = document.getElementsByName("status");
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
		var booklist = document.getElementsByName("dealStatus");

		if(document.getElementById("controlAllNotify").checked){
			for(var i=0; i<booklist.length; i++){
				booklist[i].checked = 1;}
			
		}else{
			for(var j=0; j<booklist.length; j++){
				booklist[j].checked = 0; }
		
		}
	}
		
	//鼠标悬浮于“明细”，显示该订单的操作日志
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
	
	function  changeDealStatus(orderId,changeId,status,deal_status){
	
		var  url = "/tuniuTimeOutOrder/changeDealStatus.do?version="+new Date();
			$.post(url,{
				    orderId:orderId,
				    changeId:changeId,
				    deal_status:deal_status,
				    status:status
			    },function(data){
					if(data=="yes"){
						/* $("form:first").attr("action","/account/queryAccountList.do?pageIndex="+1);
						$("form:first").submit(); */
						
						$.each($("#timeOder tbody tr"),function(index,value){
						    var a=false,b=false,c=false;
						    $(this).children("td").each(function(index){
						    
						    if(index==1){
						        var cc=$(this).text();
						    	if($.trim(cc)==orderId){
						    	 a=true;
						    	
						     }
						    }
						    
						    if(index==2){
						     var cc=$(this).text();
						    	if($.trim(cc)==changeId){
						    	b=true;
						    	
						     }
						    }
						    if(index==3){
						        var cc=$(this).text();
						             
						       if($.trim(cc)==status)
						    	c=true;
						    
						      
						    }
						     
						    });
						    
						    if(a&&b&&c){
						    
						    $(this).css({background:"#FFFFFF"});
						    	
						    	
						    	$(this).children("td").each(function(index){
						    
									    if(index==6){
									    	
									    	$(this).html('已完成');
									     }
						    
						    	    if(index==10){
									    	
									    	$(this).children("#a_deal_status").remove();
									     }
						    
						     }
						    );
						    }
						    
						    
						});
						
						
					
					}else{
						alert("操作失败");
					}
					}
			);
			return true;
	}
	
		
</script>
<style>

	tr:hover{background:#ecffff;}
#hint{width:700px;border:1px solid #C1C1C1;background:#F0FAC1;position:fixed;z-index:9;padding:6px;line-height:17px;text-align:left;overflow:auto;} 

</style>
	</head>

	<body><div></div>
		<div class="book_manage oz">
			<form action="/tuniuTimeOutOrder/queryTuniuTimeOutList.do" method="post">
				<ul class="order_num oz" style="margin-top:10px;">
					<li>
						订单号：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="order_id" value="${order_id}"/>
					</li>
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
						&nbsp;&nbsp;&nbsp;超时订单类型：
					</li>
					<li>
						<input type="checkbox" onclick="selectAll()" name="controlAll"
						id="controlAll"/><label for="controlAll">&nbsp;全部&nbsp;&nbsp;</label>
					</li>
					<c:forEach items="${Status}" var="s" varStatus="index">
						<li>
							<input type="checkbox" id="status${index.count }"
								name="status" value="${s.key }"
								<c:if test="${fn:contains(statusStr, s.key ) }">checked="checked"</c:if> />
							<label for="status${index.count }">
								${s.value }
							</label>
						</li>
					</c:forEach>
					</ul>
					<ul class="order_state oz">
					<li>
						&nbsp;&nbsp;&nbsp;处理状态：
					</li>
					<li><input type="checkbox" onclick="selectAllNotify()" name="controlAllNotify" 
					style="controlAllNotify" id="controlAllNotify"/>
					<label for="controlAllNotify">全部</label></li>
					<c:forEach items="${Deal_Status}" var="d" varStatus="index">
						<li>
							<input type="checkbox" id="dealStatus${index.count }"
								name="dealStatus" value="${d.key }"
								<c:if test="${fn:contains(dealStatusStr,d.key) }">checked="checked"</c:if> />
							<label for="dealStatus${index.count }">
								${d.value}
							</label>
						</li>
					</c:forEach>
				</ul>
				<p>
					<input type="submit" value="查 询" class="btn" />
				</p>
			<div id="hint" class="pub_con" style="display:none"></div>
				<c:if test="${!empty isShowList}">
					<table id="timeOder">
					<thead>
						<tr style="background: #EAEAEA;">
							<th>
								序号
							</th>
							<th>
								订单号
							</th>
							<th>
								改签流水号
							</th>
							<th>
								超时订单类型
							</th>
							<th>
								备注信息
							</th>
							<th>
								状态变更时间
							</th>
							<th>
								处理状态
							</th>
							<th>
								创建时间
							</th>
							<th>
							           主键id
							</th>
							<th>
								操作人
							</th>
							<th>
							           操作
							</th>
						</tr>
						</thead>				
						<tbody>
						<c:forEach var="list" items="${TuniuTimeOutList}" varStatus="idx">
							<tr
								<c:choose>
								  <c:when test="${list.deal_status != null && list.deal_status=='00'}">
								  	style="background: #BEE0FC;"
								  </c:when>
									<c:when test="${list.deal_status != null && list.deal_status=='22'}">
										style="background: #FFEBCD;"
									</c:when>
									<c:when test="${list.deal_status != null && list.deal_status=='33'}">
										style="background: #F5DEB3;"
									</c:when>
									 
								</c:choose>
						    >
							
							<td>
								${idx.index+1}
							</td>
							<td>
								${list.order_id}
							</td>
							<td>
								${list.changeId}
							</td>
							<td>
								${Status[list.status]}
							</td>
							<td>
								${list.msg }
							</td>
							<td>
								${list.updateTime}
							</td>
							<td>
								${Deal_Status[list.deal_status]}
							</td>
							<td>
								${list.create_time}
							</td>
							<td>
								${list.push_id}
							</td>
							<td>
								${list.opt_person}
							</td>
							<td>
								<span>
									<a href="/tuniuBooking/tuniuQueryBookInfo.do?order_id=${list.order_id}" onmouseover="showdiv('${list.order_id}')" onmouseout="hidediv()" >明细</a>
								</span>
								<span id ="a_deal_status">
								<c:if test="${list.deal_status !='11'}">
								 <a href="javascript:void(0)" onclick="changeDealStatus('${list.order_id}','${list.changeId !=null && not empty list.changeId?list.changeId:''}','${Status[list.status]}','11');">标记完成</a> 
								</c:if>
								</span>
							</td>
							</tr>
						</c:forEach>
						</tbody>
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
