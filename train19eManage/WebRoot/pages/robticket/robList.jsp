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
<title>抢票管理页面</title>
<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
<script language="javascript" src="/js/layer/layer.js"></script>
<script language="javascript" src="/js/mylayer.js"></script>
<script language="javascript" src="/js/json2.js"></script>
<script type="text/javascript">
	
</script>

<style>
#refresh_span a:link, #refresh_span a:visited {
	color: #2ea6d8;
}

#refresh_span a:hover {
	text-decoration: underline;
}

#hint {
	width: 700px;
	border: 1px solid #C1C1C1;
	background: #F0FAC1;
	position: fixed;
	z-index: 9;
	padding: 6px;
	line-height: 17px;
	text-align: left;
	overflow: auto;
}

* {
	padding: 0;
	margin: 0;
	border: 0;
}

table {
	border-collapse: collapse;
	border-spacing: 0;
}

.content {
	width: 1000px;
	margin: 0 auto;
}

.content table, .content table td {
	border: 1px solid #ddd;
}

.content table {
	table-layout: fixed;
	width: 100%;
}

.inlinetxt {
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}
</style>

</head>
<body>
	<div></div>
	<div class="book_manage oz">
		<form action="/rob/queryRobPage.do" method="post" name="queryFrm"
			id="queryFrm">
			<div style="border: 0px solid #00CC00; margin: 10px;">
				<ul class="order_num oz" style="margin-top: 10px;">
					<li>订单号：&nbsp;&nbsp;&nbsp; <input type="text" class="text"
						name="order_id" value="${order_id}" /> <input type="hidden"
						id="travel_time_px" value="" /> <input type="hidden"
						id="out_ticket_time_px" value="" /> <input type="hidden"
						id="create_time_px" value="" />
					</li>
					<li>开始时间：&nbsp; <input type="text" class="text"
						name="begin_info_time" id="begin_info_time" readonly="readonly"
						value="${begin_info_time}"
						onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})"
						id="defaultStartDate1" />
					</li>
					<li>结束时间：&nbsp; <input type="text" class="text"
						name="end_info_time" readonly="readonly" value="${end_info_time}"
						onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})" />
					</li>
					<li></li>
					<li>12306单号： <input type="text" class="text"
						name="out_ticket_billno" value="${out_ticket_billno}" />
					</li>
					
				</ul>
				<ul class="order_num oz" style="margin-top: 10px;">
					<li>携程单号：&nbsp;&nbsp; <input type="text" class="text"
							name="ctrip_order_id" value="${ctrip_order_id}" />
					</li>
				</ul>

				<div style="margin-top: 20px; padding-left: 35px"></div>
				<dl class="oz" style="padding-top: 10px;" id="statuslist">
					<dt style="float: left; padding-left: 40px; line-height: 50px;">
						状态：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</dt>

					<dd style="float: left; width: 80px; line-height: 20px;">
						<div class="ser-item"
							style="float: left; white-space: nowrap; padding-right: 20px;">
							<input type="checkbox" onclick="selectAllStatus()"
								name="controlAll" style="" id="controlAll" /><label
								for="controlAll">&nbsp;全部&nbsp;&nbsp;&nbsp;&nbsp;</label>
						</div>
					</dd>
					<c:forEach items="${statusMap}" var="s" varStatus="index">
						<dd style="float: left; width: 80px; line-height: 20px;">
							<div class="ser-item"
								style="float: left; white-space: nowrap; padding-right: 20px;">
								<input type="checkbox" id="order_status${index.count }"
									name="order_status" value="${s.key}"
									<c:if test="${fn:contains(statusStr, s.key ) }">checked="checked"</c:if> />
								<label>${s.value }</label>
							</div>
						</dd>
					</c:forEach>
				</dl>
				<dl class="oz" style="padding-top: 10px;">
					<dt
						style="float: left; padding-left: 38px; line-height: 80px; padding-bottom: 20px;">
						渠道：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</dt>
					<dd style="float: left; width: 80px; line-height: 20px;">
						<div class="ser-item"
							style="float: left; white-space: nowrap; width: 70px;">
							<input type="checkbox" onclick="selectAllChannel()"
								name="controlAllChannel" style="" id="controlAllChannel" /><label
								for="controlAllChannel">&nbsp;全部&nbsp;&nbsp;</label>
						</div>
					</dd>
					<c:forEach items="${channel_types}" var="s" varStatus="index">
						<dd style="float: left; width: 80px; line-height: 20px;">
							<div class="ser-item"
								style="float: left; white-space: nowrap; width: 70px;">
								<input type="checkbox" id="channel${index.count }"
									name="channel" value="${s.key }"
									<c:if test="${fn:contains(channelStr, s.key ) }">checked="checked"</c:if> />
								<label>${s.value}</label>
							</div>
						</dd>
					</c:forEach>
				</dl>
				<p>
					<input type="submit" value="查 询" class="btn" />
					<input type="button" value="人工操作" onclick="manualQuery();" class="btn" /> 
					<input type="button" value="Redis" onclick="goRedis();" class="btn" /> 
					<input type="button" value="导出Excel" class="btn" onclick="exportExcel()">
					<input type="hidden" name="form" value="1" />
				</p>

			</div>
			<div id="hint" class="pub_con" style="display: none"></div>
			<table>
				<tr style="background: #EAEAEA;">
					<th class="inlinetxt">全选 <br /> <input type="checkbox"
						id="selectAll" name="" onclick="checkChnRetRuleAll();" />
					</th>
					<th class="inlinetxt">NO</th>
					<th class="inlinetxt">订单号</th>
					<th class="inlinetxt">携程单号</th>
					<th class="inlinetxt">出到站</th>
					<th class="inlinetxt">车次</th>
					<th class="inlinetxt">乘车时间</th>
					<th class="inlinetxt">首选坐席</th>
					<th class="inlinetxt">备选坐席</th>
					<th class="inlinetxt">票价</th>
					<th class="inlinetxt">实价</th>
					<th class="inlinetxt">SVIP服务费</th>
					<th class="inlinetxt">抢票截止时间</th>
					<th class="inlinetxt">状态</th>
					<th class="inlinetxt">创建时间</th>
					<th class="inlinetxt">出票时间</th>
					<th class="inlinetxt">12306单号</th>
					<th class="inlinetxt">操作人</th>
					<th class="inlinetxt">流程日志</th>
					<th class="inlinetxt">退票状态</th>
					<th class="inlinetxt">操作</th>
				</tr>
				<c:forEach var="order" items="${orders}" varStatus="status">
					<tr>
						<td><input type="checkbox" name="item" /></td>
						<td>${status.count}</td>
						<td>${order.order_id}</td>
						<td>${order.ctrip_order_id}</td>
						<td>${order.fromTo_zh}</td>
						<td>${order.train_no}</td>
						<td>${order.from_time}</td>
						<td>${seatMap[order.seat_type]}</td>
						<td><c:forEach
								items="${fn:split(order.seat_type_accept,'|')}" var="seat"
								varStatus="sta">
								${seatMap[seat]}|
							</c:forEach></td>
						<td>${order.pay_money}</td>
						<td>${order.buy_money}</td>
						<td>${order.buy_money_ext}</td>
						<td>${order.leak_cut_offTime}</td>
						<td>${statusMap[order.order_status]}</td>
						<td>${order.create_time}</td>
						<td>${order.out_ticket_time}</td>
						<td>${order.out_ticket_billno}</td>
						<td>${order.opt_ren}</td>
						<td><a href="javascript:void(0);"
							onclick="showHis(this,'${order.order_id}','${order.ctrip_order_id}');">流程日志</a></td>
						<td>${order.refund_status}</td>
						<td><c:if test="${order.order_status eq '44'}">
								<a href="">下单</a>&nbsp
							</c:if> <c:if test="${order.order_status eq '61'}">
								<a href="">支付</a>
								<br />
							</c:if> <c:if test="${order.order_status eq '86'}">
								<a href="">取消</a>&nbsp
							</c:if> <c:if test="${order.refund_status eq '开始退票'}">
								<a
									href="/rob/refundManul.do?cp_id=${order.cp_id}&order_id=${order.order_id}">人工退票</a>
							</c:if> <a href="/rob/rediskey.do?key=${order.ctrip_order_id}">redis</a>
						</td>
					</tr>
				</c:forEach>
			</table>
			<jsp:include page="/pages/common/paging.jsp" />
			<div id="historyDiv" style="display: show; margin-top: 30px;">
				<div id="adultDiv" style="display: show;margin-left: 15px;">
					
				</div>
				<div id="childDiv" style="display: none;margin-left: 15px;margin-top: 5px">
					
				</div>
				<table class="pub_table" style="width: 680px; margin: 10px 10px;"
					id="historyTable">
					<tr>
						<th style="width: 30px;">序号</th>
						<th style="width: 130px;">单号</th>
						<th style="width: 450px;">操作日志</th>
						<th style="width: 130px;">操作时间</th>
						<th style="width: 70px;">操作人</th>
					</tr>
				</table>
			</div>
		</form>
	</div>
	<script>
		function checkChnRetRuleAll() {
			var order_id = document.getElementsByName("item");
			var tag1 = document.getElementById("selectAll").checked;
			if (tag1) {
				for (var i = 0; i < order_id.length; i++) {
					order_id[i].checked = true;
				}
			} else {
				for (var i = 0; i < order_id.length; i++) {
					order_id[i].checked = false;
				}
			}
		}

		function showHis(obj,obj1,obj2) {
			$("tr").css('background-color','white');
			$.get("/rob/showHistory.do?order_id=" + obj1+"&ctrip_id="+obj2, function(data) {
				var jsonObj = eval('(' + data + ')');
				var histories = jsonObj.histories;
				var children = jsonObj.children;
				var adult = jsonObj.adult;
				if(children && children.length>0){
					$("#childDiv").show();
					$("#childDiv").html("<span>儿童票信息:</span><br/>");
					for (var i = 0; i < children.length; i++) {
						var c = children[i];
						var html = "<span>坐席:"+c.seat_no+"--票价:"+c.OrderTicketPrice+"--坐席类别:"+c.OrderTicketSeat+"--车次:"+c.train_no+"</span><br/>";
						$("#childDiv").append(html);
					}
				}
				if(adult && adult.length>0){
					$("#adultDiv").show();
					$("#adultDiv").html("<span>成人票信息:</span><br/>");
					for (var j = 0; j < adult.length; j++) {
						var a = adult[j];
						var html = "<span>坐席:"+a.seat_no+"--票价:"+a.OrderTicketPrice+"--坐席类别:"+a.OrderTicketSeat+"--车次:"+a.train_no+"</span><br/>";
						$("#adultDiv").append(html);
					}
					
				}
				$("#historyTable tr:not(:eq(0))").remove();
				for (var i = 0; i < histories.length; i++) {
					var history = histories[i];
					var str = "<tr>" + "<td style='width: 30px;'>" + i
							+ "</td>";
					str += "<td style='width: 130px;'>" + history.order_id
							+ "</td>";
					str += "<td style='width: 450px;'>" + history.order_optlog
							+ "</td>";
					str += "<td style='width: 130px;'>"
							+ getLocalTime(history.create_time) + "</td>";
					str += "<td style='width: 70px;'>" + history.opter
							+ "</td></tr>";
					$("#historyTable tr:last").after(str);
				}
			});
			
			$(obj).parents('tr').css('background-color','#A4D3EE');

		}

		function getLocalTime(nS) {
			//shijianchuo是整数，否则要parseInt转换
			var time = new Date(parseInt(nS));
			var y = time.getFullYear();
			var m = time.getMonth() + 1;
			var d = time.getDate();
			var h = time.getHours();
			var mm = time.getMinutes();
			var s = time.getSeconds();
			return y + '-' + m + '-' + d + ' ' + h + ':' + mm + ':' + s;
		}

		function goRedis() {
			window.location = "/rob/ctrip_callback.do";
		}

		function selectAllChannel() {
			var checklist = document.getElementsByName("channel");
			if (document.getElementById("controlAllChannel").checked) {
				for (var i = 0; i < checklist.length; i++) {
					checklist[i].checked = 1;
				}
			} else {
				for (var j = 0; j < checklist.length; j++) {
					checklist[j].checked = 0;
				}

			}
		}
		function selectAllStatus() {
			var checklist = document.getElementsByName("order_status");
			if (document.getElementById("controlAll").checked) {
				for (var i = 0; i < checklist.length; i++) {
					checklist[i].checked = 1;
				}
			} else {
				for (var j = 0; j < checklist.length; j++) {
					checklist[j].checked = 0;
				}

			}
		}
		function exportExcel() {
			$("form:first").attr("action", "/rob/exportexcel.do");
			$("form:first").submit();
			$("form:first").attr("action", "/rob/queryRobPage.do");
		}
		
		function manualQuery(){
			$(":checkbox[name='order_status']").each(function(){
				if(this.value =="44"||this.value =="61"
						||this.value =="86"||this.value =="72"
						||this.value =="87"||this.value =="70" ){
					this.checked = true;
				}
			});
			$("form:first").attr("action", "/rob/queryRobPage.do");
			$("form:first").submit();
		}
	</script>
</body>
</html>
