<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/tld/trainUtil.tld" prefix="tn"%>
<!doctype html>
<html>
	<head>
		<%@ page language="java" contentType="text/html; charset=UTF-8"%>
		<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
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
.order_con {
	padding: 0 0;
}

.order_item dd {
	line-height: 25px;
	margin: 0 2% 0 2%;
}

.text_item {
	font-size: 16px;
	color: #888;
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
	height:25px;
	vertical-align:bottom;
}

.text_item_black {
	font-size: 14px;
	color: #000;
}

.text_item_header {
	font-size: 16px;
	color: #888;
	padding-left: 5%;
	margin: 0 0 0 0px;
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

.rebook_btn {
	height: 50px;
	margin-bottom: 0;
	background: #ff7200;
	text-align: center;
	width: 100%;
	font-size: bigger;
}

.order_status {
	color: #ff5400;
	height: 2em;
	line-height: 1em;
	padding-top: 5px;
	padding-left: 10px;
	margin-top: 10px;
	margin-bottom: 20px;
}

html {
	-webkit-text-size-adjust: 100%;
	-ms-text-size-adjust: 100%;
	background: #f5f6f6;
}
</style>
		<script type="text/javascript">
	
	var userId = "${agentId}"+"_hcp";
	var cookieValue = ""; 	//编码后的cookie值
	var cookieRealValue	= "";	//cookieJson解析后需要使用的数据
	var param_product_id = "";	//传递给后台的保险ID值

	//座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧 5、软卧 6、硬卧 7、 软座 8、硬座 9、无座 10、其他
	function judgeSeatType(seat_type){
		if(seat_type=="商务座"){
			seat_type = 0;
		}else if(seat_type=="特等座"){
			seat_type = 1;
		}else if(seat_type=="一等座"){
			seat_type = 2;
		}else if(seat_type=="二等座"){
			seat_type = 3;
		}else if(seat_type=="高级软卧"){
			seat_type = 4;
		}else if(seat_type=="软卧"){
			seat_type = 5;
		}else if(seat_type=="硬卧"){
			seat_type = 6;
		}else if(seat_type=="软座"){
			seat_type = 7;
		}else if(seat_type=="硬座"){
			seat_type = 8;
		}else if(seat_type=="无座"){
			seat_type = 9;
		}else{
			seat_type = 10;
		}
		return seat_type;
	}

	//车票类型0：成人票 1：儿童票
	function judgeTicketType(ticket_type){
		if(ticket_type=="0"){
			ticket_type = "成人票";
		}else if(ticket_type=="1"){
			ticket_type = "儿童票";
		}
		return ticket_type;
	}

	//证件类型１、一代身份证、２、二代身份证、３、港澳通行证、４、台湾通行证、５、护照
	function judgeIdsType(ids_type){
		if(ids_type=="1"){
			ids_type = "一代身份证";
		}else if(ids_type=="2"){
			ids_type = "二代身份证";
		}else if(ids_type=="3"){
			ids_type = "港澳通行证";
		}else if(ids_type=="4"){
			ids_type = "台湾通行证";
		}else if(ids_type=="5"){
			ids_type = "护照";
		}
		return ids_type;
	}						
		function showErrMsg(id, _width, msg){
			$("#"+id+"_errMsg").remove();
			var offset = $("#"+id).offset();
			$obj=$("#tip").clone().attr("id", id+"_errMsg")
				.css({'position':'absolute', 'top':offset.top-30, 'left':offset.left, 'width':_width}).appendTo("body");
			$obj.find(".errMsg").text(msg).end().show();
		}
	    function hideErrMsg(id){
				$("#"+id+"_errMsg").remove();
			}


	function submitData(from_city, to_city, travel_time){
		window.location = "/buyTicket/queryByStation.jhtml?from_city=" + from_city + "&to_city=" + to_city + "&travel_time=" + travel_time;
	}
	
	function refund(order_id, refund_status) {
		//window.location="/refund/refunding.jhtml?refund.order_id=" + order_id + "&refund_status=" + refund_status;
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
			//$(".aui_titleBar").hide();
			$("#trainForm").attr("action", "/refund/refund.jhtml?cp_id_str="+cp_id_str);
			$("#trainForm").submit();
		}
		
		//$("form:#reRefundForm").submit();
	}

	//验证姓名是否有效
	function checkName(val){
	    var pat=new RegExp("[^a-zA-Z\_\u4e00-\u9fa5]","i"); 
	    //var forbidArr  = new Array('成人','成人票','学生票','一张');
	    if(pat.test(val)==true){
	        return false; 
	    }else{
	        var check = /^[A-Za-z]+/;
	        if(check.test(val)){
	            if(val.length<3){
	                return false;
	            }
	        }
	        var checkCName = /^[\u4e00-\u9fa5]+/;
	        if(checkCName.test(val)){
	            if(val.length<2){
	                return false;
	            }
	        }
	        return true;
	    }
	}

	
	//显示数字
	function getNumbers(obj,type){
		var offset=$(obj).offset();
		var content="";
		if(type=="idcard"){
			content=$(obj).val().replace(/(^[\w][\w][\w])([\w]{0,3})([\w]{0,4})([\w]{0,4})/g,"$1 $2 $3 $4 ");
		}else{//phone
			content=$(obj).val().replace(/(^[\w][\w][\w])([\w]{0,4})([\w]{0,4})/g,"$1 $2 $3 ");
		}
		var count=content.length;
		var _width=0;
		if(count>3){
			_width=count*12;
		}else{
			_width=36;
		}
		$('#num_tip .numMsg').text(content);
		$('#num_tip').css({'position':'absolute', 'top':offset.top-38, 'left':offset.left, 'width':_width,'z-index':'9999'}).appendTo("body").show();
		if($.trim($(obj).val())==""){
			$('#num_tip').hide();
		}
	}	
	
	function goToPay(order_id, count) {
		var bankAbbr = $("#bankAbbr").val();
		if(confirm("确认前往支付该订单吗？")){
		$("form:first").attr("action", "/order/orderCmpay.jhtml?order_id="+order_id+"&bankAbbr="+bankAbbr+"&productNum="+count);
		//消息框	
		var dialog = art.dialog({
			lock: true,
			fixed: true,
			left: '50%',
			top: '250px',
		    title: 'Loading...',
		    icon: "/images/loading.gif",
		    content: '正在前往支付页面，请稍候！'
		});
		$(".aui_titleBar").hide();
		$("form:first").submit();
	    }
	}

	$().ready(function(){
		var create_time = document.getElementById("create_time").value;//订单提交时间
		var from_time = document.getElementById("fromTimeHour").value;
		//alert("create_time:"+create_time);
		var dt1 = new Date(Date.parse(create_time.replace(/-/g, '/')));
		var dt2 = new Date(Date.parse(from_time.replace(/-/g, '/')));
		var minutes1 = (new Date().getTime() - dt1.getTime())/1000/60;//现在距订单提交时间分钟数
		var minutes2 = (new Date().getTime() - dt2.getTime())/1000/60/60;
		if(minutes1 > 20){	//现在距订单提交时间分钟数超过20分钟，重新购票
			//alert("aa");
			$('#nowToPayDiv').css('display','none');//立即支付
			$('#reOrderDiv').css('display','block');//重新购票

			$('#reToPayDiv').css('display','none');//重新支付
			$('#reOrderBookDiv').css('display','block');//重新购票
			
		}
		//alert(from_time);
		if(minutes2 > 48){  //控制在发车后12 or 24 or 48小时后申请退票按钮隐藏
							//默认控制在48小时
			$('#refundDiv').css('display','none');// 申请退款
		}
		/**
		else{  //否则判断该订单信息是否还有车票
			var train_no = document.getElementById("train_no").value;
			var from_city = document.getElementById("from_city").value;
			var to_city = document.getElementById("to_city").value;
			var from_time = document.getElementById("from_time").value;
			var to_time = document.getElementById("to_time").value;
			var danjia = document.getElementById("danjia").value;
			var seat_type = document.getElementById("seat_type").value;
			var ticketCount = document.getElementById("ticketCount").value;
			var travelTime = document.getElementById("travelTime").value;
			var seatTypeMap = document.getElementById("seatTypeMap").value;
			var url="/buyTicket/queryTrainInfo.jhtml?travelTime="+travelTime+"&trainCode="+train_no+"&startCity="+from_city
			+"&endCity="+to_city+"&startTime="+from_time+"&endTime="+to_time+"&costTime=&seatMsg="+seatTypeMap_${list.rz}_${list.rz_num};
			window.location = encodeURI(url);
		}
		*/
	});
</script>
	</head>

	<body>
		<form id="trainForm" action="/order/createOrder.jhtml" method="post"
			enctype="multipart/form-data">
			<input type="hidden" id="baoxian" name="baoxian" value="" />
			<input type="hidden" id=fpNeed name="fpNeed" value="" />
			<input type="hidden" id="fp_receiver" name="fp_receiver" value="" />
			<input type="hidden" id="fp_phone" name="fp_phone" value="" />
			<input type="hidden" id="fp_address" name=fp_address value="" />
			<input type="hidden" name="fp_zip_code" id="fp_zip_code" value="" />
			<input type="hidden" name="product_id" id="product_id" value="" />

			<input type="hidden" id="train_no" name="train_no"
				value="${orderInfo.train_no }" />
			<input type="hidden" id=from_city name="from_city"
				value="${orderInfo.from_city }" />
			<input type="hidden" id="to_city" name="to_city"
				value="${orderInfo.to_city }" />
			<input type="hidden" id="from_time" name="from_time"
				value="${orderInfo.from_time}" />
			<input type="hidden" id="to_time" name="to_time"
				value="${orderInfo.to_time}" />
			<input type="hidden" name="travelTime" id="travelTime"
				value="${orderInfo.travel_time }" />
			<input type="hidden" name="danjia" id="danjia" value="${perCpMoney }" />
			<input type="hidden" name="seat_type" id="seat_type"
				value="${orderInfo.seat_type }" />
			<input type="hidden" name="ticketCount" id="ticketCount"
				value="${fn:length(detailList)}" />
			<input type="hidden" name="seatTypeMap" id="seatTypeMap"
				value="${seatTypeMap[orderInfo.seat_type]}" />

			<input type="hidden" name="out_ticket_type" id="out_ticket_type"
				value="11" />
			<input type="hidden" name="ps_pay_money" value="20" />
			<input type="hidden" name="wz_ext" id="wz_ext" />
			<input type="hidden" name="order_id" value="${orderInfo.order_id }" />
			<input type="hidden" id="isNeedTip" value="${isNeedTip}" />
			<input type="hidden" name="token" value="${token}" />
			<input type="hidden" name="create_time" id="create_time"
				value="${create_time }" />
			<input type="hidden" name="fromTimeHour" id="fromTimeHour"
				value="${from_time }" />
			<c:if
				test="${orderInfo.order_status eq '44' && !empty rsList.refund_status}">
				<input type="hidden" id="old_refund_status"
					value="${rsList.refund_status}" />
			</c:if>
			<div>

				<!-- start -->
				<div class="wrap">
					<header id="bar">
					<a href="javascript:window.history.back();" class="m19e_ret"></a>
					<h1>
						<c:choose>
							<c:when test="${!empty detailInfo.refund_status}">
							${rsStatusMap[detailInfo.refund_status]}
						</c:when>
							<c:when test="${orderInfo.order_status eq '45'}">
								<!-- 购票失败 -->
							购票失败
						</c:when>
							<c:when test="${orderInfo.order_status eq '99'}">
								<!-- 支付失败 -->
							${orderStatusMap[orderInfo.order_status] }
						</c:when>
							<c:when test="${orderInfo.order_status eq '00'}">
								<!-- 待支付 -->
							${orderStatusMap[orderInfo.order_status] }
						</c:when>
							<c:when test="${orderInfo.order_status eq '22'}">
								<!-- 正在购票 -->
							正在购票
						</c:when>
							<c:when test="${orderInfo.order_status eq '44'}">
								<!-- 购票成功 -->
								<c:choose>
									<c:when
										test="${rsList.refund_status eq '00' || rsList.refund_status eq '11' || rsList.refund_status eq '22' || rsList.refund_status eq '33'}">
									${rsStatusMap[rsList.refund_status]}
								</c:when>
									<c:when test="${rsList.refund_status eq '44'}">
									${rsStatusMap[rsList.refund_status]}
								</c:when>
									<c:when test="${rsList.refund_status eq '55'}">
									${rsStatusMap[rsList.refund_status]}
								</c:when>
									<c:otherwise>
									购票成功
								</c:otherwise>
								</c:choose>
							</c:when>

							<c:otherwise>
							${orderStatusMap[order.order_status]}
						</c:otherwise>
						</c:choose>
					</h1>
					<c:if test="${orderInfo.order_status eq '99'}">
						<a href="/pages/book/menuNew.jsp" class="m19e_home"><span></span>
						</a>
					</c:if>
					</header>
					<section id="order_main">

					<div class="order_con">
						<!-- 车票信息 -->
						<dl class="order_item">
							<dt class="text_item_header">
								订单信息
							</dt>
							<dd line-height="30px">
								<c:choose>
									<c:when
										test="${orderInfo.order_status eq '00' || orderInfo.order_status eq '22' || orderInfo.order_status eq '99'}">
									</c:when>
									<c:otherwise>
										<p class="text_item_black">
											12306订单号：${orderInfo.out_ticket_billno}
										</p>
									</c:otherwise>
								</c:choose>
								<!-- 
						<p>2014年1月27日</p>
						<p>G2132 天津(06:46)—北京南(07:19)</p>
						<p>二等座<span class="org">￥54.50</span>元</p>
						<p class="cons_tim">耗时:1小时20分</p>
					 -->
								<table style="width: 100%; margin: 0 0 0 0; padding: 0 0 0 0;">
									
									<tr>
										<td style="width: 10px;"></td>
										<td class="font_17 b" id="startCity">
											${orderInfo.from_station }
										</td>
										<td></td>
										<td class="icon" rowspan="3">
											<span id="trainCode">${orderInfo.train_no }</span><br>
											<span class="from_to_icon"></span>
											<span id="travel_time" style="height:25px; vertical-align:top;">${orderInfo.travel_time } ${day }</span>
										</td>
										<td class="font_17 b right" id="endCity">
											${orderInfo.arrive_station }
										</td>
										<td style="width: 10px;"></td>
									</tr>
									<tr line-height="20px">
										<td style="width: 10px;"></td>
										<td class="text_item" id="startTime">
											${orderInfo.from_time}
										</td>
										<td></td>
										<td class="text_item" style="float:right;" id="endTime">
											${orderInfo.arrive_time}
										</td>
										<td style="width: 10px;"></td>
									</tr>
									<tr>
										<td style="width: 10px;"></td>
										<td class="text_item" id="seatType">
											${seatTypeMap[orderInfo.seat_type]}
										</td>
										<td></td>
										<td class="text_item" style="float:right;">
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
							<!-- <dt>选择乘客<a href="javascript:void(0);" onclick="adddiv()" id="add_person" class="passenger_choose text_item">&nbsp;&nbsp;&nbsp;</a></dt> -->
							<dt class="text_item_header">
								乘客信息
							</dt>

							<dd>
								<div id="orderInfo" style="display: block;">
									<table style="width: 100%; margin: 0 0 0 0; padding: 0 0 0 0;"
										id="train_ticket_list">
										<c:forEach items="${detailList}" var="detail" varStatus="idx">
											<c:if test="${idx.index != 0}">
												<tr style="border-top: 1px solid #C0C0C0">
													<td colspan="4"></td>
												</tr>
											</c:if>
											<tr class="adult">
												<td>
													<table
														style="width: 100%; margin: 0 0 0 0; padding: 0 0 0 0;">
														<input type="hidden" name="cp_id_${detail.cp_id}"
															value="${detail.cp_id}" />
														<input type="hidden" name="refund_money_${detail.cp_id}"
															value="${detail.cp_refund_money}" />
														<input type="hidden" name="refund_percent_${detail.cp_id}"
															value="${detail.refund_percent}" />
														<tr>
															<td class="text_item_black" width="60%">
																<input type="hidden" name="user_name" id="user_name"
																	value="${detail.user_name}">
																<c:if
																	test="${orderInfo.order_status eq '44' && !empty detail.refund_status}">
																	<c:choose>
																		<c:when
																			test="${detail.refund_status eq '00' || detail.refund_status eq '11' || detail.refund_status eq '22' || detail.refund_status eq '33'}">
																		</c:when>
																		<c:when test="${detail.refund_status eq '44'}">
																		</c:when>
																		<c:when test="${detail.refund_status eq '55'}">
																			<input type="checkbox" class="chk_refund"
																				id="chk_${detailInfo.cp_id}"
																				value="${detailInfo.cp_id}" />
																		</c:when>
																	</c:choose>
																</c:if>
																<c:if
																	test="${orderInfo.order_status eq '44' && empty detail.refund_status}">
																	<input type="checkbox" class="chk_refund"
																		id="chk_${detail.cp_id}" value="${detail.cp_id}" />
																</c:if>
																${detail.user_name}
																<c:if test="${!empty detail.refund_status}">
																	<c:choose>
																		<c:when
																			test="${detail.refund_status eq '00' || detail.refund_status eq '11' || detail.refund_status eq '22' || detail.refund_status eq '33'}">
																			<span class="blue_icon">(${rsStatusMap[detail.refund_status]})</span>
																		</c:when>
																		<c:when test="${detail.refund_status eq '44'}">
																			<span class="green">(${rsStatusMap[detail.refund_status]})</span>
																		</c:when>
																		<c:when test="${detail.refund_status eq '55'}">
																			<span class="red">(${rsStatusMap[detail.refund_status]})</span>
																		</c:when>
																	</c:choose>
																</c:if>
															</td>
															<td class="text_item_black" width="40%">
																${ticketTypeMap[detail.ticket_type]}
															</td>

														</tr>
														<tr>
															<td class="text_item_black">
																${detail.user_ids}
															</td>
															<td class="text_item_black">
																保险：
																<c:if
																	test="${detail.bx_id eq '' || (empty detail.bx_id)}">无</c:if>
																<c:if test="${!empty detail.bx_id}"> ￥${detail.bx_pay_money}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </c:if>
															</td>
														</tr>
														<tr>
															<c:if
																test="${!empty detail.train_box && (!empty detail.seat_no)}">
																<td class="text_item_black">
																	座位号：${detail.train_box}车${detail.seat_no}
																</td>
															</c:if>

															<c:if
																test="${!empty detail.cp_pay_money and (detail.cp_pay_money ne '0')}">
																<td class="text_item_black">
																	票价:￥${detail.cp_pay_money }
																</td>
															</c:if>


														</tr>
														<c:if test="${!empty detail.bx_code}">
															<tr>
																<td class="text_item_black" colspan="2">
																	单号:${detail.bx_code}
																</td>
															</tr>
														</c:if>
													</table>

												</td>
											</tr>

										</c:forEach>
									</table>
								</div>
							</dd>

						</dl>


						<!-- 订单状态 -->
						<dl class="order_status">
							<dt>

								<c:choose>
									<c:when test="${!empty detailInfo.refund_status}">
							${rsStatusMap[detailInfo.refund_status]}
							<c:if test="${detailInfo.refund_status eq '44'}">
								退款金额：￥${detailInfo.refund_money}
							</c:if>
									</c:when>
									<c:when test="${orderInfo.order_status eq '45'}">
										<!-- 购票失败 -->
										<span class="red"><em class="order_fail">&nbsp;&nbsp;&nbsp;&nbsp;</em>购票失败</span>
										<c:if
											test="${!empty rsList.user_remark and rsList.refund_type eq '3'}">
											<br>
											<span class="text_item_black">失败原因：${outFailReasonMap[rsList.user_remark]}</span>
										</c:if>
									</c:when>
									<c:when test="${orderInfo.order_status eq '99'}">
										<!-- 支付失败 -->
										<span class="red"><em class="order_fail">&nbsp;&nbsp;&nbsp;&nbsp;</em>${orderStatusMap[orderInfo.order_status]
											}</span>
									</c:when>
									<c:when test="${orderInfo.order_status eq '00'}">
										<!-- 待支付 -->
										<span class="blue_icon"><em class="order_to_pay">&nbsp;&nbsp;&nbsp;&nbsp;</em>${orderStatusMap[orderInfo.order_status]
											}</span>
									</c:when>
									<c:when test="${orderInfo.order_status eq '22'}">
										<!-- 正在购票 -->
										<span class="blue_icon"><em class="order_to_pay">&nbsp;&nbsp;&nbsp;&nbsp;</em>正在购票</span>
									</c:when>
									<c:when test="${orderInfo.order_status eq '44'}">
										<!-- 购票成功 -->
										<c:choose>
											<c:when
												test="${rsList.refund_status eq '00' || rsList.refund_status eq '11' || rsList.refund_status eq '22' || rsList.refund_status eq '33'}">
												<span class="blue_icon"><em class="order_to_pay">&nbsp;&nbsp;&nbsp;&nbsp;</em>${rsStatusMap[rsList.refund_status]}</span>
											</c:when>
											<c:when test="${rsList.refund_status eq '44'}">
												<span class="green"><em class="order_succeed">&nbsp;&nbsp;&nbsp;&nbsp;</em>${rsStatusMap[rsList.refund_status]}</span>
												<br />
												<span class="grey">退款金额：<span class="org"> ￥<fmt:formatNumber
															value="${rsList.refund_money}" type="currency"
															pattern="#0.0" /> </span> </span>
											</c:when>
											<c:when test="${rsList.refund_status eq '55'}">
												<span class="red"><em class="order_fail">&nbsp;&nbsp;&nbsp;&nbsp;</em>${rsStatusMap[rsList.refund_status]}
													<c:if test="${!empty rsList.our_remark}">(${rsList.our_remark })</c:if>
												</span>
											</c:when>
											<c:otherwise>
												<span class="green"><em class="order_succeed">&nbsp;&nbsp;&nbsp;&nbsp;</em>购票成功</span>
											</c:otherwise>
										</c:choose>
									</c:when>

									<c:otherwise>
										<span class="green">${orderStatusMap[order.order_status]}</span>
									</c:otherwise>
								</c:choose>
							</dt>
						</dl>
					</div>

					<c:if test="${orderInfo.order_status eq '00'}">
						<div class="ticket_ser_w" id="nowToPayDiv" style="display: block;">
							<input type="button" value="立即支付" id="btnNowPaySubmit"
								class="ticket_ser" onclick="">
						</div>
						<div class="ticket_ser_w" id="reOrderDiv" style="display: none;">
							<input type="button" value="重新购票" id="btnSubmit"
								class="ticket_ser"
								onclick="submitData('${orderInfo.from_city}', '${orderInfo.to_city}', '${orderInfo.travel_time}');">
						</div>
					</c:if>
					<c:if test="${orderInfo.order_status eq '99'}">
						<div class="ticket_ser_w" id="reToPayDiv" style="display: block;">
							<input type="button" value="重新支付" id="btnRePaySubmit"
								class="ticket_ser" onclick="">
						</div>
						<div class="ticket_ser_w" id="reOrderBookDiv"
							style="display: none;">
							<input type="button" value="重新购票" id="btnSubmit"
								class="ticket_ser"
								onclick="submitData('${orderInfo.from_city}', '${orderInfo.to_city}', '${orderInfo.travel_time}');">
						</div>
					</c:if>

					<c:if test="${flag eq 'yes'}">
						<div class="ticket_ser_w" id="refundDiv" style="display: block;">
							<input type="button" value="申请退票" id="btnRefund"
								class="ticket_ser"
								onclick="refund('${orderInfo.order_id}', '${detailInfo.refund_status}');">
						</div>
					</c:if>
					</section>

				</div>
				<!-- end -->



			</div>
		</form>
		<!-- 
<div style="display:none">
	<form id="reRefundForm" name="reRefundForm" method="post" action="/refund/refunding.jhtml" enctype="multipart/form-data">
		<input type="hidden" name="order_id" value="${orderInfo.order_id }"/>
		<input type="hidden" id="isNeedTip" value="${isNeedTip}" />
		<input type="hidden" name="token" value="${token}" />
		<c:if test="${orderInfo.order_status eq '44' && !empty rsList.refund_status}">
			<input type="hidden" id="old_refund_status" value="${rsList.refund_status}" />
		</c:if>
	</form>
</div>
 -->
	</body>
</html>
