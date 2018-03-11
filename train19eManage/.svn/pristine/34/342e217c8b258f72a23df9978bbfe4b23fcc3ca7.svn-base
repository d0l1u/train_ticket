<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo"%>
<%@ page import="com.l9e.transaction.vo.PageVo"%>
<%@ page import="java.util.*"%>
<%@ page import="com.l9e.util.JSONUtil"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>配送上门 出票管理页面</title>
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
	
	$().ready(function(){
		//乘车时间排序箭头变化
		var travel=document.getElementById("travel_time_px").value;
		if(travel=="up"){
		document.getElementById("travel_time_img").src="/images/up.png";
		}else if(travel=="down"){
		document.getElementById("travel_time_img").src="/images/down.png";
		}else if(travel==""){
		document.getElementById("travel_time_img").src="/images/normal.png";
		}
		//创建时间排序箭头变化
		var create=document.getElementById("create_time_px").value;
		if(create=="up"){
		document.getElementById("create_time_img").src="/images/up.png";
		}else if(create=="down"){
		document.getElementById("create_time_img").src="/images/down.png";
		}else if(create==""){
		document.getElementById("create_time_img").src="/images/normal.png";
		}
		//发车时间排序箭头变化
		var out_ticket=document.getElementById("out_ticket_time_px").value;
		if(out_ticket=="up"){
		document.getElementById("out_ticket_time_img").src="/images/up.png";
		}else if(out_ticket=="down"){
		document.getElementById("out_ticket_time_img").src="/images/down.png";
		}else if(out_ticket==""){
		document.getElementById("out_ticket_time_img").src="/images/normal.png";
		}
	});
	
	//人工出票
	function gotoPsOrder(order_id){
		//ajax验证是否锁
		var url = "/psOrder/queryPayIsLock.do?order_id="+order_id+"&version="+new Date();
			$.get(url,function(data){
			if(data != null && data != ""){
				var temp = data ;
				var str1 = temp.split("&") ;
				var str =str1[1]; 
				alert("此订单已经锁定，锁定人为"+str);
				return;
			 }else{
				 url2="/psOrder/queryPsOrderInfo.do?order_id="+order_id+"&query_type=2";
				 showlayer('人工出票',url2,'1000px','800px')
			 }
		});
	}
	
	function queryInfo(order_id){
		var url="/psOrder/queryPsOrderInfo.do?order_id="+order_id+"&query_type=mingxi";
		showlayer('明细',url,'1000px','800px')
		}

	function submitForm(){
		$("form:first").attr("action","/psOrder/queryPsOrderList.do");
		$("form:first").submit();
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
				url:"/psOrder/queryOrderOperHistory.do?order_id="+order_id,
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
	
	//排序
	function changeInTurn(type){
		if(type=="travel"){
		var value=null;
		var px=document.getElementById("travel_time_px").value;
		if(px=="" || px=="down"){
			value="up";
		}else if(px=="up"){
			value="down";
		}
		$("form:first").attr("action","/psOrder/queryPsOrderList.do?travel_time_px="+value);
		$("form:first").submit();
		}else if(type=="create"){
		var value=null;
		var px=document.getElementById("create_time_px").value;
		if(px=="" || px=="down"){
			value="up";
		}else if(px=="up"){
			value="down";
		}
		$("form:first").attr("action","/psOrder/queryPsOrderList.do?create_time_px="+value);
		$("form:first").submit();
		}else{
		var value=null;
		var px=document.getElementById("out_ticket_time_px").value;
		if(px=="" || px=="down"){
			value="up";
		}else if(px=="up"){
			value="down";
		}
		$("form:first").attr("action","/psOrder/queryPsOrderList.do?out_ticket_time_px="+value);
		$("form:first").submit();
		}
	}
	
		
function exportExcel() {
			$("form:first").attr("action","/psOrder/exportexcel.do");
			$("form:first").submit();
			$("form:first").attr("action","/psOrder/queryPsOrderList.do");
		}


</script>
<style>
	#refresh_span a:link,#refresh_span a:visited{color:#2ea6d8;}
	#refresh_span a:hover{text-decoration:underline;}
	#hint{width:700px;border:1px solid #C1C1C1;background:#F0FAC1;position:fixed;z-index:9;padding:6px;line-height:17px;text-align:left;overflow:auto;} 
</style>

	</head>

	<body>
		<div></div>
		<div class="book_manage oz">
			<form action="/psOrder/queryPsOrderList.do" method="post" name="queryFrm">
				<div style="border: 0px solid #00CC00; margin: 10px;">
					<ul class="order_num oz" style="margin-top: 10px;">
						<li>
							订单号：&nbsp;&nbsp;&nbsp;
							<input type="text" class="text" name="order_id"
								value="${order_id }" />
						</li>
						<li></li>
					</ul>
					
					<ul class="order_num oz" style="margin-top: 10px;">
						<li>
							开始时间：&nbsp;
							<input type="text" class="text" name="begin_info_time"
								readonly="readonly" value="${begin_info_time }"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" id="defaultStartDate1"/>
						</li>
						<li>
							结束时间：&nbsp;
							<input type="text" class="text" name="end_info_time"
								readonly="readonly" value="${end_info_time }"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
						</li>
						<li></li>
					</ul>
					<dl class="oz" style="padding-top:20px;">
						<dt style="float:left;padding-left:40px;line-height:22px;">状态：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</dt>
						<dd  style="float:left;width:900px;">
							<div class="ser-item" style="float:left;white-space:nowrap;padding-right:20px;">
								<input type="checkbox" onclick="selectAll()" name="controlAll" style="controlAll" id="controlAll"/><label for="controlAll">&nbsp;全部&nbsp;&nbsp;&nbsp;&nbsp;</label>
							</div>
						<c:forEach items="${psOrderStatus }" var="s" varStatus="index">
							<div class="ser-item" style="float:left;white-space:nowrap;padding-right:20px;">
								<input type="checkbox" id="order_status${index.count }" name="order_status" value="${s.key }" value="1"
									<c:if test="${fn:contains(statusStr, s.key ) }">checked="checked"</c:if> />
								<label for="order_status${index.count }">
									${s.value }
								</label>
							</div>
						</c:forEach>
						</dd>
					</dl>
		<br/>
        <p>
         <input type="button" value="查 询" class="btn" onclick="submitForm();"/>
		 <%		if ("p1".equals(loginUserVo.getUser_level()) || "2".equals(loginUserVo.getUser_level()) ) {%>
		<!-- 
		 <input type="button" value="导出Excel" class="btn" onclick="exportExcel()" />
		 -->
		<%} %>
        </p>
				</div>
				<div id="hint" class="pub_con" style="display:none"></div>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #EAEAEA;">
							<th>
								NO
							</th>
							<th width="110px">
								订单号
							</th>
							<th width="65px">
								出发/到达
							</th>
							<th>
								车次
							</th>
							<th width="68px">
							<span id="px"  onclick="changeInTurn('travel')"style="cursor:pointer">
								乘车时间
								<img src="" alt="" name="travel_time_img" id="travel_time_img"  style=" display:inline-block; margin: auto -8px;"/>
								<input type="hidden" id="travel_time_px" value="${travel_time_px }" />
							</span>
							</th>
							<th width="40px">
								坐席
							</th>
							<th>
								支付金额
							</th>
							<th>
								票价
							</th>
							<th width="50px">
								状态
							</th>
							<th width="68px">
							<span id="px"  onclick="changeInTurn('create')" style="cursor:pointer">
								创建时间
								<img src="" alt="" name="create_time_img" id="create_time_img"  style=" display:inline-block;margin: auto -8px;"/>
								<input type="hidden" id="create_time_px" value="${create_time_px }" />
							</span>
							</th>
							<th width="30px">
								时长
							</th>
							<th width="68px">
							<span id="px"  onclick="changeInTurn('out_ticket')"style="cursor:pointer">
								预订时间
								<img src="" alt="" name="out_ticket_time_img" id="out_ticket_time_img" style=" display:inline-block;margin: auto -8px;"/>
								<input type="hidden" id="out_ticket_time_px" value="${out_ticket_time_px }" />
							</span>
							</th>
							<th width="60px">
								出票时间
							</th>
							<th>
								12306单号
							</th>
							<th width="45px">
								操作人
							</th>
							<th width="120px">
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${psOrderList}" varStatus="idx">
							<tr
								<c:if test="${fn:contains('00,11', list.order_status )}">
									style="background: #E0F3ED;"
								</c:if>
								<c:if test="${fn:contains('23', list.order_status )}">
									style="background: #BEE0FC;"
								</c:if>
								>
							<td>
								${idx.index+1}
							</td>
							<td>
								${list.order_id}
							</td>
							<td>
								${list.from_city}/${list.to_city}
							</td>
							<td>
								${list.train_no }
							</td>
							<td>
							<c:if test="${list.from_time ne null}">
								${fn:substringBefore(list.from_time, ' ')}
								<br />
								${fn:substringAfter(list.from_time, ' ')}
							</c:if>
							<c:if test="${list.from_time eq null}">
								${list.travel_time}
							</c:if>
							
							</td>
							<td>
								${seat_Types[list.seat_type] }
							</td>
							<c:choose>
								<c:when
									test="${list.buy_money != null && list.buy_money!= list.pay_money}">
									<c:choose>
										<c:when
											test="${list.buy_money != null && list.buy_money > list.pay_money}">
											<td style="color: #f00;">
												<strong>${list.pay_money}</strong>
											</td>
											<td style="color: #f00;">
												<strong>${list.buy_money}</strong>
											</td>
										</c:when>
										<c:otherwise>
											<td style="color: #00f;">
												${list.pay_money}
											</td>
											<td style="color: #00f;">
												${list.buy_money}
											</td>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<td>
										${list.pay_money}
									</td>
									<td>
										${list.buy_money}
									</td>
								</c:otherwise>
							</c:choose>
							<td>
								${psOrderStatus[list.order_status] }
							</td>
							<td>
								${fn:substringBefore(list.create_time, ' ')}
								<br />
								${fn:substringAfter(list.create_time, ' ')}
							</td>
							<c:choose>
								<c:when test="${list.time_out ge '5.0' && list.time_out le '1440.0'}">
									<td style="color:red;">
									${list.time_out}
									</td>
								</c:when>
								<c:when test="${list.time_out ge '1440.0'}">
									<td>
									超时
									</td>
								</c:when>
								<c:otherwise>
									<td>
									${list.time_out}
									</td>
								</c:otherwise>
							</c:choose>
							<td>
								${fn:substringBefore(list.out_ticket_time, ' ')}
								<br />
								${fn:substringAfter(list.out_ticket_time, ' ')}
							</td>
							<td>
								${fn:substringBefore(list.pay_time, ' ')}
								<br />
								${fn:substringAfter(list.pay_time, ' ')}
							</td>
							<td>
								${list.out_ticket_billno}
							</td>
							<td>
								${list.opt_ren}
							</td>
							<td>
								<span>  
								 <%		if ("p1".equals(loginUserVo.getUser_level()) || "p1".equals(loginUserVo.getUser_level()) 
								 || "zhangjc".equals(loginUserVo.getUser_name())) {%>
									<c:choose> 
											<c:when test="${fn:contains('00', list.order_status)}"> 
												<a href="javascript:gotoPsOrder('${list.order_id}');">人工出票</a> 
											</c:when> 
											<c:when test="${fn:contains('11,21', list.order_status)}"> 
												<a href="javascript:gotoPsOrder('${list.order_id}');">人工配送</a> 
											</c:when> 
									</c:choose>  
									<%} %>
									</span>
									<div><span>
										<a href="javaScript:void(0)" onclick="queryInfo('${list.order_id}');" onmouseover="showdiv('${list.order_id}')" onmouseout="hidediv()" >明细</a> 
									</span></div>
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
