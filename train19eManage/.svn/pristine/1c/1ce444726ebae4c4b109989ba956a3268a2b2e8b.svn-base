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
		<title>订单撤销页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
		<script language="javascript" src="/js/json2.js"></script>
		<script type="text/javascript">
		<%
		PageVo pageObject = (PageVo) request.getAttribute("pageBean");
		int pageIndex = pageObject.getPageIndex();
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user_level = loginUserVo.getUser_level();
	%>
		//明细锁 
		function gotoOrderLock(order_id, order_type){
			//ajax验证是否锁
			var url = "/elongBook/queryOrderIsLock.do?order_id="+order_id+"&version="+new Date();
				$.get(url,function(data){
				if(data != null && data != ""){
					var temp = data ;
					var str1 = temp.split("&") ;
					var str =str1[1]; 
					alert("此订单已经锁定，锁定人为"+str);
					return;
				 }else{
					$("form:first").attr("action","/elongBook/bookInfo.do?order_id="+order_id);
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

	
	//全选操作
	function checkChnRetRuleAll(){
	      var order_id = document.getElementsByName("order_id");
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
	
	var jsonArr = [];　
	//批量撤销订单  
	function submitCheXiao(){
		//记录订单状态
		var channelStr = "";
			$("input[name='order_status']:checkbox:checked").each(function(){ 
			channelStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
			});
			channelStr = channelStr.substring(0, channelStr.length-1);
		var orderIdStr = "";
		var orderIdNum = 0;
		var str = "";
		$("input[name='order_id']:checkbox:checked").each(function(){ //遍历被选中CheckBox元素的集合 得到Value值
			var order_id = $(this).val();
			if($("#notifyStatus_"+order_id).val()!=22 && $("#orderStatus_"+order_id).val()!=33 && $("#orderStatus_"+order_id).val()!=44 && $("#orderStatus_"+order_id).val()!=51 ){//订单状态不是订单成功或者订单失败，切不能是撤销中
				//验证订单是否加锁
				var url = "/elongBook/queryOrderIsLock.do?order_id="+order_id+"&version="+new Date();
				$.get(url,function(data){
					if(data != null && data != ""){
						var temp = data ;
						var str1 = temp.split("&") ;
						var str =str1[1]; 
						//alert("订单"+$(this).val()+"已经锁定，锁定人为"+str);
						//continue;
					 }else{//订单被选中，状态不是订单成功、失败和撤销中，未加锁的订单
						var create_time = $("#createTime_"+order_id).val();//得到该订单的创建时间
						var json = {"order_id": order_id, "create_time": create_time};
						jsonArr.push(JSON.stringify(json));
					 }
				});
				orderIdNum++;
			}
		}); 
		if(orderIdNum==0){
			alert("选中订单中，没有符合可撤销条件的订单！");
			return false;
		}
		if(confirm("确认批量撤销订单吗 ？")){
			document.queryFrm.action="/elongBook/updateCheXiao.do?statusList="+channelStr+"&jsonArr="+jsonArr+"&version="+new Date();
			document.queryFrm.submit();
		}
	}
	
	//撤销订单
	function gotoCheXiao(order_id,create_time){
		//ajax验证是否锁
		var url = "/elongBook/queryOrderIsLock.do?order_id="+order_id+"&version="+new Date();
			$.get(url,function(data){
			if(data != null && data != ""){
				var temp = data ;
				var str1 = temp.split("&") ;
				var str =str1[1]; 
				
				alert("此订单已经锁定，锁定人为"+str);
				return;
			 }else{
				var  uri = "/elongBook/updateGotoCheXiao.do?order_id="+order_id+"&version="+new Date();
				$.post(uri,function(data){
					if(data == "yes"){
						$("form:first").attr("action","/elongBook/queryBookList.do?chexiao=1&pageIndex="+<%=pageIndex%>);
						$("form:first").submit();
					}else{
						alert("撤销请求失败");
					}
				});
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
				url:"/elongBook/queryOrderOperHistory.do?order_id="+order_id,
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
</script>
<style>		
	.liancheng {color: red;}
	tr:hover {background: #ecffff;}
#hint{width:700px;border:1px solid #C1C1C1;background:#F0FAC1;position:fixed;z-index:9;padding:6px;line-height:17px;text-align:left;overflow:auto;} 
</style>
	</head>

	<body>
		<div class="book_manage oz">
			<form action="/elongBook/queryBookList.do?chexiao=1" method="post" name="queryFrm">
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
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})" />
					</li>
					<li>
						结束时间：&nbsp;&nbsp;&nbsp;&nbsp;
						<!-- <input type="text" class="text" name="end_info_time" value="${end_info_time }"/> -->
						<input type="text" class="text" name="end_info_time"
							readonly="readonly" value="${end_info_time }"
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})" />
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
					<input type="button" value="批量撤销" onclick="submitCheXiao();" class="btn" />
				</p>
				<div id="hint" class="pub_con" style="display:none"></div>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #EAEAEA;">
						<th style="width:30px;">全选 <br/><input type="checkbox" id="checkChnRetRulAll" name="checkChnRetRulAll" onclick="checkChnRetRuleAll();"/></th>
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
								<input type="checkbox" id="order_id" name="order_id" value="${list.order_id }"/>
								<input type="hidden" id="orderStatus_${list.order_id }" value="${list.order_status }" />
								<input type="hidden" id="notifyStatus_${list.order_id }" value="${list.notify_status }" />
								<input type="hidden" id="createTime_${list.order_id }" value="${list.create_time }" />
							</td>
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
								${list.opt_ren}
							</td>
							<td>
								${notifyStatus[list.notify_status]}
							</td>
							<td>
								<a href="/elongBook/bookInfo.do?order_id=${list.order_id}" onmouseover="showdiv('${list.order_id}')" onmouseout="hidediv()">明细</a> 
								<c:if test="${ list.notify_status ne '22' && list.order_status ne '33'&& list.order_status ne '44' && list.order_status ne '51' }">
								<a href="javascript:gotoCheXiao('${list.order_id}','${list.create_time}');">撤销订单</a>
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
