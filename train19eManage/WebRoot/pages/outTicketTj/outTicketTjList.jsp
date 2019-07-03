<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<%@ page import="com.l9e.transaction.vo.PageVo"%>
<%
	String device_type = (String)request.getAttribute("device_type");
	String create_time = (String)request.getAttribute("create_time");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>出票效率统计页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		 <script type="text/javascript" src="/js/jquery.cookie.js"></script>
		<script type="text/javascript">
	function submitForm(){
	var create_time=document.getElementById("create_time").value;
		if(document.getElementById("create_time").value ==''){
		alert("时间不能为空！");
		}else if(create_time  <"2015-05-07"){
		alert("没有更早的数据了！");
		}else{
		
		$("#myform").submit();
		}
	};
	$(function(){
		//console.log(" 颜色  ");
		//$(".auto").css("color","#FF0000");
		var device_type = "<%=device_type %>";
		var create_time = "<%=create_time%>";
		if(device_type=="pc"){
			$(".at").addClass("auto");
		}
		if(device_type=="app"){
			$(".app").addClass("auto");
			}
		$(".app").click(function(){
			$(".at").removeClass("auto");
			$(this).addClass("auto");
			document.location.href="/outTicketTj/queryOutTicketTjPage.do?type=app&create_time="+create_time;
			})
		$(".at").click(function(){
			$(".app").removeClass("auto");
			$(this).addClass("auto");
			document.location.href="/outTicketTj/queryOutTicketTjPage.do?type=pc&create_time="+create_time;
			})	
		})
	
</script>
  <style>
	    	.book_manage{width:1100px; margin-bottom:20px;}
			.book_manage TABLE{float: left;}
			.book_manage td{line-height: 33px;}
			.book_manage th{line-height: 33px;background: #f0f0f0;}
		</style>
	</head>
	<body><div></div>
		<div class="book_manage oz">
			<form action="/outTicketTj/queryOutTicketTjList.do?type=<%=device_type %>" method="post" name="myform" id="myform">

				<ul class="order_num oz" style="margin-top: 10px;">
					<li><br/>
						查询时间：&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="create_time" id="create_time"
							readonly="readonly" value="${create_time }"
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>
					<li style="margin-top: 20px; width: 80px;"><span class="at" style="cursor: pointer;">pc</span>/<span class="app" style="cursor: pointer;">app</span></li>
					<li style="margin-top: 10px; ">
					<p>
					<input style="margin-left: -80px;" type="button" value="查 询" class="btn" onclick="submitForm();" />
					</p>
					</li>
					
				</ul>
				
				<c:forEach var="ss" varStatus="index" begin="0"  end="2">
					<div style="overflow:auto; margin-top: 10px;margin-bottom: 10px;">
					<table style="width:60px;background-color: #2C99FF;">
						<tr>
							<td>
								<c:if test="${ index.index eq 0 }">
									艺龙
								</c:if>
								<c:if test="${ index.index eq 1 }">
									同程
								</c:if>
								<c:if test="${ index.index eq 2 }">
									途牛
								</c:if>
							</td>
						</tr>
					</table>
				
					<table style="width:125px;">
						<tr>
							<th>
								效率\时段
							</th>
						</tr>
						<tr>
							<td>
								预订效率
							</td>
						</tr>
						<tr>
							<td>
								收单时长
							</td>
						</tr>
						<tr>
							<td>
								分发时长
							</td>
						</tr>
						<tr>
							<td>
								预订时长
							</td>
						</tr>
						<tr>
							<td>
								预订通知
							</td>
						</tr>
						<tr>
							<td>
								支付效率
							</td>
						</tr>
						<c:if test="${create_time ge '2015-05-19'}">
						<tr id ="notify_pay">
							<td>
								支付通知
							</td>
						</tr>
						</c:if>
						<tr>
							<td>
								出票效率
							</td>
						</tr>
					</table>
						<c:forEach var="list" items="${outTicketTjList }" varStatus="idx">
						<c:if test="${(list.channel eq 'elong' && index.index eq 0 ) || (list.channel eq 'tongcheng' && index.index eq 1 ) || (list.channel eq 'tuniu' && index.index eq 2 )}">
						<table style="width:35px;">
						<c:if test="${list.hour eq null}">
								<tr><th>总</th></tr>
						</c:if>
						<c:if test="${list.hour ne null}">
								<tr><th>${list.hour }</th></tr>
						</c:if>
								
								<tr><td 
									<c:if test="${list.hour gt hour && fn:substringBefore(list.create_time, ' ') eq now }">
									style="color:#D6D6D6;"
									</c:if>
									>${list.book_xl }</td></tr>
								<tr><td <c:if test="${list.hour gt hour && fn:substringBefore(list.create_time, ' ') eq now }">
									style="color:#D6D6D6;"
									</c:if>
									>${list.shoudan }</td></tr>
								<tr><td <c:if test="${list.hour gt hour && fn:substringBefore(list.create_time, ' ') eq now }">
									style="color:#D6D6D6;"
									</c:if>
									>${list.fenfa }</td></tr>
								<tr><td <c:if test="${list.hour gt hour && fn:substringBefore(list.create_time, ' ') eq now }">
									style="color:#D6D6D6;"
									</c:if>
									>${list.book }</td></tr>
								<tr><td <c:if test="${list.hour gt hour && fn:substringBefore(list.create_time, ' ') eq now }">
									style="color:#D6D6D6;"
									</c:if>
									>${list.notify }</td></tr>
								<tr><td <c:if test="${list.hour gt hour && fn:substringBefore(list.create_time, ' ') eq now }">
									style="color:#D6D6D6;"
									</c:if>
									>${list.pay_xl }</td></tr>
								<tr><td <c:if test="${list.hour gt hour && fn:substringBefore(list.create_time, ' ') eq now }">
									style="color:#D6D6D6;"
									</c:if>
									>${list.notify_pay }</td></tr>
								<tr><td <c:if test="${list.hour gt hour && fn:substringBefore(list.create_time, ' ') eq now }">
									style="color:#D6D6D6;"
									</c:if>
									>${list.outticket_xl }</td></tr>
						</table>
					
						</c:if>
						</c:forEach>
						
				</div>
			</c:forEach>		
		</form>
	</div>
</body>
</html>
