<!doctype html>
<html>
	<head>
		<%@ page language="java" contentType="text/html; charset=UTF-8"%>
		<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
		<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
		<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
		<meta charset="utf-8">
		<title>掌上19e-火车票</title>
		<meta name="viewport"
			content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
		<meta name="keyword"
			content="19e,便民服务,数字便民,移动客户端,手机客户端,话费充值,手机充值,游戏充值,QQ充值,机票预订,水费,电费,煤气费,取暖费,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
		<meta name="description"
			content="提供19e数字便民移动终端下载,随时随地办理便民业务,支持移动客户端话费充值,手机充值,游戏充值,QQ充值,机票预订,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
		<link rel="stylesheet" type="text/css" href="/css/base.css">
		<link rel="shortcut icon" href="/images/favicon.ico"
			type="image/x-icon" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript" src="/js/artDialog.js"></script>
		<style type="text/css">
.text_item_header {
	font-size: 16px;
	color: #888;
	padding-left: 5%;
}

.screen {
	width: 480px;
	margin: 0 auto;
}

.order_con {
	padding: 0 0;
}

td.icon {
	vertical-align: middle;
	width: 40%;
	position: absolute;
	left: 50%;
	margin-left: -20%;
	height: 70px;
	text-align: center;
	line-height: 20px;
	font-size: 14px;
}

#trainCode {
	font-size: 14px;
	height: 25px;
	vertical-align: bottom;
}

.order_item dd {
	line-height: 25px;
	margin: 0 2% 0 2%;
}

.text_item {
	font-size: 16px;
	color: #888;
}

.text_item_black {
	font-size: 14px;
	color: #000;
}

.text_item .small {
	font-size: 12px;
	color: #888;
}

#trainCode {
	font-size: 20px;
	color: #FFFAFA;
	width: 30%;
	padding-left: 10px;
}

.input_hidden_border {
	font-size: 18px;
	color: #888;
	width: 70px;
	border: 0;
	font-family: Microsoft YaHei, calibri, verdana;
}

.pass_inp,.pass_sel {
	width: 240px;
	height: 35px;
	margin: 3px 0;
	display: block;
	border-radius: 3px;
	padding-left: 10px;
}

.pass_sel {
	width: 130px;
	height: 35px;
	padding-left: 10px;
}

.pass_ziti {
	width: 200px;
}

.passenger_del {
	position: absolute;
	right: 0;
	top: 0;
	height: 40px;
	width: 30px;
	background: url(/images/cancel-icon.png) no-repeat center center;
	text-indent: -9999px;
}

.passenger_delete {
	position: relative;
	right: 0;
	top: 0;
	height: 40px;
	width: 30px;
	background: url(/images/cancel-icon.png) no-repeat center center;
	text-indent: -9999px;
}

.passenger_add {
	position: absolute;
	right: 25px;
	top: 0;
	padding-right: 30px;
	background: url(/images/add-btn.png) no-repeat right center;
	color: #0f63b8;
}

html {
	-webkit-text-size-adjust: 100%;
	-ms-text-size-adjust: 100%;
}
</style>
		<script type="text/javascript">
$().ready(function(){
	$("#all_chk").click(function(){//all_chk  全选
		if($(this).attr("checked")){
			$(".chk_refund").attr("checked", true);
		}else{
			$(".chk_refund").attr("checked", false);
		}
	});
	
	$(".chk_refund").click(function(){
		if($(".chk_refund:checked").length==$(".chk_refund").length){
			$("#all_chk").attr("checked", true);
		}else{
			$("#all_chk").attr("checked", false);
		}
	});
	var from_time = document.getElementById("fromTimeHour").value;
	var dt2 = new Date(Date.parse(from_time.replace(/-/g, '/')));
	var minutes2 = (new Date().getTime() - dt2.getTime())/1000/60/60;
	if(minutes2 > 48){  //控制在发车后12 or 24 or 48小时后申请退票按钮隐藏
						//默认控制在48小时
		$('#refundDiv').css('display','none');// 申请退款
	}
	
});



function sumbitRefund(){
	var count=$(".chk_refund:checked").length;
	if(count==0){
		$(".chk_area").css({border:"2px solid red"});
		alert("请勾选需要退款的车票！");
		return;
	}
	var cp_id="",addr="",index=0,suffix="",isValid=true;
	var cp_id_str="";
	$(".chk_refund:checked").each(function(index){
		cp_id=$(this).val();
		cp_id_str+=cp_id;
		if(index<count-1){
			cp_id_str+=",";
		}
		//addr=$("#stationPic_"+cp_id).val();//stationPic车站小票
		//if($("#isNeedTip").val()=="1" && addr==""){
		////	isValid=false;
		//	alert("请上传车站小票！");
		//	return false;
		//}
		//if(addr!=""){
		//	var index = addr.lastIndexOf(".");
		//	if(index>0){
		//		suffix = addr.substring(index + 1).toLowerCase();
		//		if(suffix!="jpg" && suffix!="jpeg" && suffix!="bmp" && suffix!="png"){
		//			alert("车站小票只能为jpg、jpeg、png或bmp格式的图片！");
		//			isValid=false;
		//			return false;
		//		}
		//	}
		//}
	});
	if(isValid==false){
		return;
	}

	if(confirm("您是否确认对勾选的"+count+"张车票进行退票？")){
		//消息框	
		var dialog = art.dialog({
			lock: true,
			fixed: true,
			left: '50%',
			top: '80%',
		    title: 'Loading...',
		    icon: "/images/loading.gif",
		    content: '正在提交您的退款申请，请稍候！'
		});
		$(".aui_titleBar").hide();
		$("#refundForm").attr("action", "/refund/refund.jhtml?cp_id_str="+cp_id_str);
		$("#refundForm").submit();
	}
}
</script>
	</head>

	<body>
		<div>

			<!-- start -->
			<div class="wrap">
				<header id="bar">
				<a href="javascript:window.history.back();" class="m19e_ret"></a>
				<h1>
					购票成功
				</h1>
				</header>
				<section id="order_main">

				<div class="order_con">
					<!-- 车票信息 -->
					<dl class="order_item">
						<dt class="text_item_header">
							订单信息

						</dt>
						<!-- 
						<span class="order_succeed">&nbsp;&nbsp;&nbsp;&nbsp;</span><span class="green">恭喜您购票成功</span>
					 -->
						<input type="hidden" name="fromTimeHour" id="fromTimeHour"
							value="${from_time }" />
						<dd line-height="30px">
							<p class="text_item_black">
								12306订单号：${orderInfo.out_ticket_billno}
							</p>
							<table style="width: 100%; margin: 0 0 0 0; padding: 0 0 0 0;">

								<tr>
									<td style="width: 10px"></td>
									<td class="font_18 b left" id="startCity">
										${orderInfo.from_station }
									</td>
									<td></td>
									<td rowspan="3" class="icon">
									<td class="icon" rowspan="3">
										<span id="trainCode">${orderInfo.train_no}</span>
										<br>
										<span class="from_to_icon"></span>
										<span id="travel_time"
											style="height: 25px; vertical-align: top;">${orderInfo.travel_time
											} ${day }</span>
									</td>
									</td>
									<td class="font_18 b right" id="endCity" align="center">
										${orderInfo.arrive_station }
									</td>
									<td style="width: 10px"></td>
								</tr>
								<tr line-height="20px">
									<td style="width: 10px"></td>
									<td class="text_item" id="startTime">
										${orderInfo.from_time}
									</td>
									<td></td>
									<td style="float: right" class="text_item" id="endTime">
										${orderInfo.arrive_time}
									</td>
									<td style="width: 10px"></td>
								</tr>
								<tr>
									<td style="width: 10px;"></td>
									<td class="text_item" id="seatType">
										${seatTypeMap[orderInfo.seat_type]}
									</td>
									<td></td>
									<td class="text_item" style="float: right">
										${fn:length(detailList)}张
									</td>
									<td style="width: 10px;"></td>
								</tr>
								<tr style="border-top: 1px solid #C0C0C0">
									<td colspan="10" id="orderId" class="text_item_black">
										订单号：${orderInfo.order_id }
									</td>
								</tr>
								<c:choose>
									<c:when
										test="${orderInfo.order_status eq '00' || orderInfo.order_status eq '99' || orderInfo.order_status eq '22'}">
									</c:when>
									<c:otherwise>
										<tr>
											<td colspan="10" id="orderId" class="text_item_black">
												下单时间：${orderInfo.pay_time }
											</td>
										</tr>
									</c:otherwise>
								</c:choose>
								<tr>
									<td colspan="10" id="payMoney" class="text_item_black">
										总计金额：￥
										<span
											style="color: #f60; font-weight: bold; font-family: arial; font-size: 16px;">${orderInfo.pay_money
											}</span>
									</td>
								</tr>
							</table>
						</dd>
					</dl>

					<dl class="order_item">
						<dt class="text_item_header">
							乘客信息
						</dt>
						<dd>
							<form action="/refund/refund.jhtml" method="post"
								name="refundForm" id="refundForm" enctype="multipart/form-data">
								<input type="hidden" name="order_id"
									value="${orderInfo.order_id }" />
								<input type="hidden" id="isNeedTip" value="${isNeedTip}" />
								<input type="hidden" name="token" value="${token}" />
								<c:forEach var="detailInfo" items="${detailList}"
									varStatus="idx">
									<c:if test="${idx.index != 0}">
										<HR
											style="FILTER: alpha(opacity =                       100, finishopacity =                       0, style =                       3)"
											width="100%" color=#C0C0C0 SIZE=1>
										<%
											out
															.println("<div class=\"oz pas1\"><p class=\"border_top\"></p>");
										%>
									</c:if>
									<table style="width: 100%; margin: 0 0 0 0; padding: 0 0 0 0;">
										<input type="hidden" name="cp_id_${detailInfo.cp_id}"
											value="${detailInfo.cp_id}" />
										<input type="hidden" name="refund_money_${detailInfo.cp_id}"
											value="${detailInfo.cp_refund_money}" />
										<input type="hidden" name="refund_percent_${detailInfo.cp_id}"
											value="${detailInfo.refund_percent}" />
										<tr>
											<td class="text_item_black" width="70%">
												<c:if test="${!empty detailInfo.refund_status}">
													<c:choose>
														<c:when
															test="${detailInfo.refund_status eq '00' || detailInfo.refund_status eq '11' || detailInfo.refund_status eq '22' || detailInfo.refund_status eq '33'}">
														</c:when>
														<c:when test="${detailInfo.refund_status eq '44'}">
														</c:when>
														<c:when test="${detailInfo.refund_status eq '55'}">
															<input type="checkbox" class="chk_refund"
																id="chk_${detailInfo.cp_id}" value="${detailInfo.cp_id}" />
														</c:when>
													</c:choose>
												</c:if>
												<c:if test="${empty detailInfo.refund_status}">
													<input type="checkbox" class="chk_refund"
														id="chk_${detailInfo.cp_id}" value="${detailInfo.cp_id}" />
												</c:if>

												<input type="hidden" name="user_name" id="user_name"
													value="${detailInfo.user_name}">
												${detailInfo.user_name}
												<c:if test="${!empty detailInfo.refund_status}">
													<c:choose>
														<c:when
															test="${detailInfo.refund_status eq '00' || detailInfo.refund_status eq '11' || detailInfo.refund_status eq '22' || detailInfo.refund_status eq '33'}">
															<span class="blue_icon">(${rsStatusMap[detailInfo.refund_status]})</span>
														</c:when>
														<c:when test="${detailInfo.refund_status eq '44'}">
															<span class="green">(${rsStatusMap[detailInfo.refund_status]})</span>
														</c:when>
														<c:when test="${detailInfo.refund_status eq '55'}">
															<span class="red">(${rsStatusMap[detailInfo.refund_status]})</span>
														</c:when>
													</c:choose>
												</c:if>
											</td>
											<td class="text_item_black" width="30%">
												${ticketTypeMap[detailInfo.ticket_type]}
											</td>

										</tr>
										<tr>
											<td class="text_item_black">
												身份证:${detailInfo.user_ids}
											</td>
											<td class="text_item_black">
												保险:
												<c:if
													test="${detailInfo.bx_id eq '' || (empty detailInfo.bx_id )}">无</c:if>
												<c:if test="${!empty detailInfo.bx_id }"> ${detailInfo.bx_pay_money} </c:if>
											</td>
										</tr>
										<tr>
											<c:if
												test="${!empty detailInfo.train_box && (!empty detailInfo.seat_no)}">
												<td class="text_item_black">
													座位号:${detailInfo.train_box}车${detailInfo.seat_no}号
												</td>
											</c:if>

											<c:if
												test="${!empty detailInfo.cp_pay_money and (detailInfo.cp_pay_money ne '0')}">
												<td class="text_item_black">
													票价:￥${detailInfo.cp_pay_money }
												</td>
											</c:if>
										</tr>
									</table>

									<c:if test="${idx.index != 0}">
										<%
											out.println("</div>");
										%>
									</c:if>
								</c:forEach>
							</form>
						</dd>
					</dl>
					<dl class="order_item">
						<dt class="text_item_header">
							温馨提示
						</dt>
						<dd>
							<p class="text_item">
								<span class="small">如果您已取票，请去车站窗口办理退票</span>
							</p>
							<p class="text_item">
								<span class="small">如果您主动发起退票申请，保险金额不退</span>
							</p>
						</dd>
					</dl>

				</div>
				<!-- 
			<div class="order_foot ticket_ser_w">
				 
				<input type="button" value="申请退款" onclick="sumbitRefund();" 
					style="margin-left:130%;text-align:center;font-family:'Microsoft yahei';font-size:20px;border:none;color:#fff;background-color:#FF7200;cursor:pointer;width:120px;height:40px;"/>
				
				<input type="button" value="申请退款" class="ticket_ser" onclick="sumbitRefund();">	
			</div>	
			 -->

				<div class="ticket_ser_w" id="refundDiv">
					<input type="button" value="申请退款" class="ticket_ser"
						onclick="sumbitRefund();">
				</div>
				<!--  
			 <div class="order_foot">
				<div class="order_d" style="background:#d13b00;">
					<span class="order_pay">退票金额：${orderInfo.ticket_pay_money - orderInfo.buy_money}</span>
				</div>
				<div class="order_d" style="background:#ff7200;">
					<span class="order_btn"><input type="button" onclick="sumbitRefund();" value="申请退款" id="btnSubmit" class="order_submit" /></span>
				</div>
			</div>	
			 -->
				</section>
			</div>
			<!-- end -->


		</div>
	</body>
</html>
