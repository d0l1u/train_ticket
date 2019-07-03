<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/tld/trainUtil.tld" prefix="tn"%>
<!doctype html>
<html>
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
		<title>掌上19e-火车票</title>
		<meta name="keyword"
			content="19e,便民服务,数字便民,移动客户端,手机客户端,话费充值,手机充值,游戏充值,QQ充值,机票预订,水费,电费,煤气费,取暖费,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
		<meta name="description"
			content="提供19e数字便民移动终端下载,随时随地办理便民业务,支持移动客户端话费充值,手机充值,游戏充值,QQ充值,机票预订,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
		<link rel="stylesheet" type="text/css" href="/css/base.css">
		<link rel="shortcut icon" href="/images/favicon.ico"
			type="image/x-icon" />
	</head>
	<script>
			function gotoBookOrder(travelTime, trainCode, startCity, endCity, startTime, endTime, costTime, seatMsg){
				var is_buyable="${is_buyable}";
				if(is_buyable == "00"){
					alert("对不起，我们真的好忙，别着急等会再订吧！\nSorry,we are very busy now,please wait a minute!");
					return false;
				}
				var url="/buyTicket/queryTrainInfo.jhtml?travelTime="+travelTime+"&trainCode="+trainCode+"&startCity="+startCity+"&endCity="+endCity+"&startTime="+startTime+"&endTime="+endTime+"&costTime="+costTime+"&seatMsg="+seatMsg;
				window.location = encodeURI(url);
			}
			function queryNearDay(travel_time, from_city, to_city , day){
				var date = stringToJsTime(travel_time);
				date.setDate(date.getDate() + day);
				var month = date.getMonth() + 1;
				if(month < 10){
					month = "0" + month;
				}
				var days = date.getDate();
				if(days < 10) {
					days = "0" + days;
				}
				var travelTime = date.getFullYear() + "-" + month + "-" + days;
				var gaotie = localStorage.getItem("gaotie");
				var url = "/buyTicket/queryByStation.jhtml?travel_time=" + travelTime + "&from_city=" + from_city + "&to_city=" + to_city + "&gaotie=" +gaotie;
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
						
		</script>

<style type="text/css">	
.duration {width:15%;text-align:center;}
.train_time {width:12%;text-align:center;}
.train_number {width:30%;}
.train_price {text-align:center;}

.leixing {font-size:12px;}
</style>

	<body>
		<div>

			<!-- start -->
			<div class="wrap">
				<header id="bar">
				<a href="/pages/book/trainHome.jsp" class="m19e_ret"></a>
				<h1>
					${paramMap.from_city}-${paramMap.to_city}
				</h1>
				
				</header>

				<section id="ser_main">
				<div class="book_datapcik">
					<a
						href="javascript:queryNearDay('${paramMap.travel_time}', '${paramMap.from_city}', '${paramMap.to_city}', -1)"
						class="book_prev"></a>
					<span class="book_data"><strong>${paramMap.travel_time}</strong>${paramMap.day}</span>
					<a
						href="javascript:queryNearDay('${paramMap.travel_time}', '${paramMap.from_city}', '${paramMap.to_city}', 1)"
						class="book_next"></a>
				</div>

				<!--<div class="ser_condit">
				<a href="" class="start_time">出发时间</a>
				<a href="" class="screening">筛选</a>
				<a href="" class="consum_time">耗时</a>
			</div>	
			-->
				</section>

				<section id="train_main">
				<c:if test="${otVo ==null && unBookList==null && osnd!=null}">
					<c:if
						test="${osnd.datajson == null || fn:length(osnd.datajson)==0}">
						<tr>
							<td colspan="6"
								style="font: bold 16px/ 60px 'microsoft yahei', '宋体'; color: #f60;">
								对不起，暂无您查询的车次信息！
							</td>
						</tr>
					</c:if>
					<table cellspacing="0" cellpadding="0" border="0" id="train_list"
						class="train_list">
						<c:forEach items="${osnd.datajson}" var="list">
							<c:if test="${!empty list.station_train_code}">
							<!-- 
								<c:if
									test="${list.yz ne '0' || list.rz ne '0' || list.yz ne '0' || list.ze ne '0' || list.yws ne '0' ||list.rws ne '0'}">
									<tr onclick="gotoBookOrder('${osnd.sdate}','${list.station_train_code}','${list.from_station_name}','${list.to_station_name}','${list.start_time}','${list.arrive_time}','${list.lishi}','无座_${list.yz eq '-'?list.ze:list.yz}_${list.wz_num},硬座_${list.yz}_${list.yz_num},软座_${list.rz}_${list.rz_num},一等座_${list.zy}_${list.zy_num},二等座_${list.ze}_${list.ze_num},硬卧_${list.ywx}_${list.yw_num},软卧_${list.rwx}_${list.rw_num},高级软卧_${list.gwx}_${list.gr_num},特等座_${list.tdz}_${list.tz_num},商务座_${list.swz}_${list.swz_num}');">
								</c:if>
								<c:if
									test="${list.yz eq '0' && list.rz eq '0' && list.yz eq '0' && list.ze eq '0' && list.yws eq '0' && list.rws eq '0'}">
									<tr class="none">
								</c:if>
							 -->
								<c:choose>
				                        	<c:when test="${list.canBook eq '1'}">
				                        		<table class="infotable" onclick="gotoBookOrder('${osnd.sdate}','${list.station_train_code}','${list.from_station_name}','${list.to_station_name}','${list.start_time}','${list.arrive_time}','${list.lishi}','无座_${list.yz eq '-'?list.ze:list.yz}_${list.wz_num},硬座_${list.yz}_${list.yz_num},软座_${list.rz}_${list.rz_num},一等座_${list.zy}_${list.zy_num},二等座_${list.ze}_${list.ze_num},硬卧_${list.ywx}_${list.yw_num},软卧_${list.rwx}_${list.rw_num},高级软卧_${list.gwx}_${list.gr_num},特等座_${list.tdz}_${list.tz_num},商务座_${list.swz}_${list.swz_num}');">
				                        	</c:when>
				                        	<c:otherwise>
				                        		<table class="none">
				                        	</c:otherwise>
				                </c:choose>
				                <tr >
				                	<td class="checi" colspan="4">
									<span>${list.station_train_code}(
										
										<c:choose>
	
											<c:when test="${fn:contains(list.station_train_code, 'G')}">
												高速动车	
											</c:when>
											<c:when test="${fn:contains(list.station_train_code, 'C')}">
												城际动车	
											</c:when>
											<c:when test="${fn:contains(list.station_train_code, 'D')}">
												动车	
											</c:when>
											<c:when test="${fn:contains(list.station_train_code, 'T')}">
												特快	
											</c:when>
											<c:when test="${fn:contains(list.station_train_code, 'K')}">
												快车	
											</c:when>
											<c:when test="${fn:contains(list.station_train_code, 'Z')}">
												直达
											</c:when>
											<c:otherwise>
												其他
											</c:otherwise>
										</c:choose>
										
										)
									</span>
									</td>
								
								</tr>
							<tr>
								<td class="train_time">
									<span><strong>${list.start_time}</strong> <br>${list.arrive_time}</span>
								</td>
								<td class="train_number" width="33%">
								<!-- 	<span>${list.station_train_code}(
										
										<c:choose>
	
											<c:when test="${fn:contains(list.station_train_code, 'G')}">
												高速动车	
											</c:when>
											<c:when test="${fn:contains(list.station_train_code, 'C')}">
												城际动车	
											</c:when>
											<c:when test="${fn:contains(list.station_train_code, 'D')}">
												动车	
											</c:when>
											<c:when test="${fn:contains(list.station_train_code, 'T')}">
												特快	
											</c:when>
											<c:when test="${fn:contains(list.station_train_code, 'K')}">
												快车	
											</c:when>
											<c:when test="${fn:contains(list.station_train_code, 'Z')}">
												直达
											</c:when>
											<c:otherwise>
												其他
											</c:otherwise>
										</c:choose>
										
										)
									</span>
								 -->
									<c:if
										test="${list.from_station_name eq list.start_station_name}">
										<span id="${list.train_no}_${list.from_station_name}"
											class="st"><em>始</em>${list.from_station_name}</span>
										
									</c:if>
									<c:if
										test="${list.from_station_name ne list.start_station_name}">
										<span class="ps"
											id="${list.train_no}_${list.from_station_name}"><em>过</em>${list.from_station_name}</span>
										
									</c:if>
									<c:if test="${list.to_station_name eq list.end_station_name}">
										<span class="ed" id="${list.train_no}_${list.to_station_name}"><em>终</em>${list.to_station_name}</span>
									</c:if>
									<c:if test="${list.to_station_name ne list.end_station_name}">
										<span class="ps" id="${list.train_no}_${list.to_station_name}"><em>过</em>${list.to_station_name}</span>
									</c:if>
								</td>
								<td class="duration">
									<span><c:if test="${fn:substring(list.lishi, 0,2) ne '00'}">${fn:substring(list.lishi, 0,2)}时</c:if><br/>${fn:substring(list.lishi, 3, 5) }分</span>
								</td>
								<td class="train_price">
									<c:if test="${!empty list.yz && list.yz ne '-'}">硬座<br><strong>￥${list.yz}</strong>
										<br />
									</c:if>
									<c:if test="${empty list.yz || list.yz eq '-'}">
										<c:if test="${!empty list.ze && list.ze ne '-'}">二等座<br><strong>￥${list.ze}</strong>
											<br />
										</c:if>
										<c:if test="${empty list.ze || list.ze eq '-'}">
											<c:if test="${!empty list.rz && list.rz ne '-'}">软座<br><strong>￥${list.rz}</strong>
												<br />
											</c:if>
											<c:if test="${!empty list.zy && list.zy ne '-'}">一等座<br><strong>￥${list.zy}</strong>
												<br />
											</c:if>

											<c:if test="${!empty list.yws && list.yws ne '-'}">硬卧<br><strong>￥${list.ywx}<strong><br />
											</c:if>
											<c:if test="${empty list.yws || list.yws eq '-'}">
												<c:if test="${!empty list.rws && list.rws ne '-'}">软卧<br><strong>￥${list.rwx}<strong><br />
												</c:if>
											</c:if>
										</c:if>
									</c:if>
								</td>

								<!--<td class="train_price"><strong>￥553</strong><br>二等座</td>-->
								</tr>

							</c:if>
						</c:forEach>
					<!-- 
						<tr>
							<td class="train_time">
								<span><strong>07:00</strong> <br>12:36</span>
							</td>
							<td class="train_number">
								<span>G101（高速动车）</span>
								<span class="st"><em>始</em>北京南</span>
								<span class="ed"><em>终</em>上海虹桥</span>
							</td>
							<td class="duration">
								<span>5小时36分</span>
							</td>
							<td class="train_price">
								<strong>￥553</strong>
								<br>
								二等座
							</td>
						</tr>

						<tr class="none">
							<td class="train_time">
								<span><strong>07:00</strong> <br>12:36</span>
							</td>
							<td class="train_number">
								<span>G101（高速动车）</span>
								<span class="st"><em>始</em>北京南</span>
								<span class="ed"><em>终</em>上海虹桥</span>
							</td>
							<td class="duration">
								<span>5小时36分</span>
							</td>
							<td class="train_price">
								<strong>￥553</strong>
								<br>
								二等座
							</td>
						</tr>
				 --><tr>
				 	</table>
					</table>
				</c:if>
				</section>
			</div>
			<!-- end -->

		</div>
	</body>
</html>
