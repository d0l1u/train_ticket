<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/tld/trainUtil.tld" prefix="tn"%>
<!doctype html>
<html>
	<head>
		<meta charset="utf-8">
		<title>掌上19e-火车票</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
		<meta name="keyword"
			content="19e,便民服务,数字便民,移动客户端,手机客户端,话费充值,手机充值,游戏充值,QQ充值,机票预订,水费,电费,煤气费,取暖费,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
		<meta name="description"
			content="提供19e数字便民移动终端下载,随时随地办理便民业务,支持移动客户端话费充值,手机充值,游戏充值,QQ充值,机票预订,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
		<link rel="stylesheet" type="text/css" href="/css/base.css">
		<link rel="shortcut icon" href="/images/favicon.ico"
			type="image/x-icon" />
	</head>

	<style type="text/css">
.screen {
	width: 480px;
	height: 700px;
	margin: 0 auto;
}
html{-webkit-text-size-adjust:100%; -ms-text-size-adjust:100%;}
</style>
	<script>
	function queryNearDay(travel_time, from_city, to_city,train_code, day){
		var date = stringToJsTime(travel_time);
		date.setDate(date.getDate() + day);
		if(date.getTime() <= new Date().getTime()){
			alert("请选择正确的时间！")
			return;
		}
		var month = date.getMonth() + 1;
		if(month < 10){
			month = "0" + month;
		}
		var days = date.getDate();
		if(days < 10) {
			days = "0" + days;
		}
		var travelTime = date.getFullYear() + "-" + month + "-" + days;
		var url = "/buyTicket/queryNearTrainInfo.jhtml?travel_time=" + travelTime + "&from_city=" + from_city + "&to_city=" + to_city + "&train_code=" + train_code;
		window.location = encodeURI(url);
	}
			
	//将字符串转换为js的时间对象，  字符串格式yyyy-MM-dd
	function stringToJsTime(time) {      
		var y = time.substring(0,4);      
		var m = time.substring(5,7)-1;      
		var d = time.substring(8,10); 
		var date = new Date(y,m,d);      
		return date;      
	}  

	//进入下单页面
	function gotoBookOrder(travelTime, trainCode, startCity, endCity, startTime, endTime, costTime, seat_type, seat_price, day){
		localStorage.setItem("trainCode",trainCode);
		localStorage.setItem("travelTime",travelTime); 
		localStorage.setItem("day",day);
		localStorage.setItem("startCity",startCity);
		localStorage.setItem("endCity",endCity);
		localStorage.setItem("startTime",startTime);
		localStorage.setItem("endTime",endTime);
		localStorage.setItem("seat_type",seat_type);
		localStorage.setItem("seat_price",seat_price);
		var is_buyable="${is_buyable}";
		if(is_buyable == "00"){
			alert("对不起，我们真的好忙，别着急等会再订吧！\nSorry,we are very busy now,please wait a minute!");
			return false;
		}
		var url="/buyTicket/gotoBookOrder.jhtml?travelTime="+travelTime+"&trainCode="+trainCode+"&startCity="+startCity+"&endCity="+endCity+"&startTime="+startTime+"&endTime="+endTime+"&costTime="+costTime+"&seat_type="+seat_type+"&seat_price="+seat_price+"&day="+day;
		window.location = encodeURI(url);
	} 
</script>

	<body>
		<div>

			<!-- start -->
			<div class="wrap" style="background: #fff;">
				<header id="bar">
				<a href="javascript:window.history.back();" class="m19e_ret"></a>
				<h1>
					${from_city}-${to_city }
				</h1>
				</header>
				<section id="book_main">
				<h2 class="book_title">
					${train_code} 历时
					<c:if test="${fn:indexOf(cost_time,':') >= 0}">
						<c:if test="${fn:substring(cost_time, 0,2) ne '00'}">
							${fn:substring(cost_time, 0,2)}时
						</c:if>
							${fn:substring(cost_time, 3, 5) }分
					</c:if>
					<c:if test="${fn:indexOf(cost_time,':') < 0}">
						<fmt:formatNumber value="${(cost_time-cost_time%60)/60}" pattern="0"/>时${cost_time%60 }分
					</c:if>
				</h2>
				<div class="book_datapcik">
					<a
						href="javascript:queryNearDay('${travel_time}', '${from_city}', '${to_city }','${train_code}', -1)"
						class="book_prev"></a>
					<span class="book_data">${travel_time } ${day }</span>
					<a
						href="javascript:queryNearDay('${travel_time}', '${from_city}', '${to_city }','${train_code}', 1)"
						class="book_next"></a>
				</div>
				<table cellspacing="0" cellpadding="0" border="0" id="book_list"
					class="book_list">
					<c:forEach var="seat" items="${seat_info}">
						<c:if test='${empty seat.seat_num || seat.seat_num eq "" || (seat.seat_num eq "0")}'>
							<tr class="noseat none" disabled="true">
								<td class="book_seat">
									${seat.seat_type}
								</td>
								<td class="book_price">
									￥${seat.seat_price }
								</td>
								<td class="book_remain">
									${seat.seat_num}张
								</td>
								<td class="book_btn">
									<a style="background:#8f8f8f;">无票</a>
								</td>
							</tr>
						</c:if>
						<c:if test='${!empty seat.seat_num && seat.seat_num ne "" && (seat.seat_num ne "0")}'>
							<tr>
								<td class="book_seat">
									${seat.seat_type}
								</td>
								<td class="book_price">
									￥${seat.seat_price }
								</td>
								<td class="book_remain">
									${seat.seat_num}张
								</td>
								<td class="book_btn">
									<a href="javascript:void(0);" 
									onclick="gotoBookOrder('${travel_time }','${train_code}','${from_city}','${to_city}','${startTime}','${endTime}','${cost_time}','${seat.seat_type}','${seat.seat_price }','${day }');">预订</a>
								</td>
							</tr>
						</c:if>

					</c:forEach>
					
					<!-- 
					<tr class="noseat">
						<td class="book_seat">
							二等座
						</td>
						<td class="book_price">
							￥553
						</td>
						<td class="book_remain">
							0张
						</td>
						<td class="book_btn">
							<a href="">上车补票</a>
						</td>
					</tr>

					<tr>
						<td class="book_seat">
							一等座
						</td>
						<td class="book_price">
							￥933
						</td>
						<td class="book_remain">
							6张
						</td>
						<td class="book_btn">
							<a href="">预订</a>
						</td>
					</tr>
					 -->
					
				</table>
				<table cellspacing="0" cellpadding="0" border="0" id="station_list"
					class="station_list">
					<thead style="background: #dedede;">
						<tr>
							<td>
								站次
							</td>
							<td>
								车站
							</td>
							<td>
								到时
							</td>
							<td>
								发时
							</td>
							<td>
								停留
							</td>
						</tr>
					</thead>
					<c:forEach items="${stationList}" var="station">
						<tr>
							<td>
								${station.stationno }
							</td>
							<td>
								${station.name }
							</td>
							<td>
								<c:if test="${station.distance eq '0'}">
								始发站
							</c:if>
								<c:if test="${station.distance ne '0'}">
								${station.arrtime}
							</c:if>
							</td>
							<td>
								<c:if
									test="${station.interval eq '---' && station.stationno ne '1'}">
								终点站
							</c:if>
								<c:if
									test="${station.interval ne '---' || station.stationno eq '1'}">
								${station.starttime}
							</c:if>
							</td>
							<td>
								${station.interval }
							</td>
						</tr>
					</c:forEach>
					<!-- 
					<tr>
						<td>
							1
						</td>
						<td>
							北京南
						</td>
						<td>
							始发站
						</td>
						<td>
							07:00
						</td>
						<td>
							-
						</td>
					</tr>
					<tr>
						<td>
							2
						</td>
						<td>
							沧州西
						</td>
						<td>
							07:52
						</td>
						<td>
							08:43
						</td>
						<td>
							3分钟
						</td>
					</tr>
					<tr>
						<td>
							3
						</td>
						<td>
							沧州西
						</td>
						<td>
							07:52
						</td>
						<td>
							08:43
						</td>
						<td>
							3分钟
						</td>
					</tr>
					<tr>
						<td>
							4
						</td>
						<td>
							沧州西
						</td>
						<td>
							07:52
						</td>
						<td>
							08:43
						</td>
						<td>
							3分钟
						</td>
					</tr>
					 -->
				</table>
				</section>
			</div>
			<!-- end -->


		</div>
	</body>
</html>
