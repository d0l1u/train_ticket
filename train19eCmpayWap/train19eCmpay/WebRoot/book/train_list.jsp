<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<html>
<head>
<meta charset="utf-8">
<title>掌上19e-火车票</title>
<meta name="keyword" content="19e,便民服务,数字便民,移动客户端,手机客户端,话费充值,手机充值,游戏充值,QQ充值,机票预订,水费,电费,煤气费,取暖费,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
<meta name="description" content="提供19e数字便民移动终端下载,随时随地办理便民业务,支持移动客户端话费充值,手机充值,游戏充值,QQ充值,机票预订,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
<link rel="stylesheet" type="text/css" href="/css/base.css">
<link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon" />
<script type="text/javascript" src="/js/jquery.js"></script>
<style type="text/css">
.screen {width:480px; height:700px; margin:0 auto;}
</style>
<script type="text/javascript">
    $().ready(function() {
		var fromZh=localStorage.getItem("fromZh");
		var toZh=localStorage.getItem("toZh");
		if(fromZh=="出发城市" || fromZh=="" || toZh=="" || toZh=="到达城市"){
			document.getElementById("trainList").innerText="车票查询";
		}else{
			document.getElementById("trainList").innerText=fromZh+"-"+toZh;
		}
		var travel_time = localStorage.getItem("travel_time");
		document.getElementById("today").innerText = travel_time;
	});
	
	function preDay(){
		var travel_time = localStorage.getItem("travel_time");
	}
</script>
</head>


<body>
<div class="screen">

	<!-- start -->
	<div class="wrap">
		<header id="bar">
			<a href="javascript:window.history.back();" class="m19e_ret"></a>
			<h1 id="trainList">车票查询</h1>
			<a href="/pages/book/menuNew.jsp" class="m19e_home"></a>
		</header>

		<section id="ser_main">
			<div class="ser_data">
				<a href="javascript:void(0);" onclick="preDay();" class="prev_data">前一天</a>
				<span href="" class="cur_data" id="today"><strong>2014-1-27</strong>周一</span>
				<a href="javascript:void(0);" onclick="nextDay();" class="next_data">后一天</a>
			</div>
			<!-- 
			<div class="ser_condit">
				<a href="" class="start_time">出发时间</a>
				<a href="" class="screening">筛选</a>
				<a href="" class="consum_time">耗时</a>
			</div>	
			 -->
		</section>

		<section id="train_main">
			<table cellspacing="0" cellpadding="0" border="0" id="train_list" class="train_list">
				<tr onclick="location.href='booking.html'">
					<td class="train_time"><span><strong>07:00</strong><br>12:36</span></td>
					<td class="train_number">	
						<span>G101（高速动车）</span>
						<span class="st"><em>始</em>北京南</span>
						<span class="ed"><em>终</em>上海虹桥</span>	
					</td>
					<td class="duration"><span>5小时36分</span></td>
					<td class="train_price"><strong>￥553</strong><br>二等座</td>
				</tr>

				<tr onclick="location.href='booking.html'">
					<td class="train_time"><span><strong>07:00</strong><br>12:36</span></td>
					<td class="train_number">	
						<span>G101（高速动车）</span>
						<span class="st"><em>始</em>北京南</span>
						<span class="ed"><em>终</em>上海虹桥</span>	
					</td>
					<td class="duration"><span>5小时36分</span></td>
					<td class="train_price"><strong>￥553</strong><br>二等座</td>
				</tr>

				<tr class="none">
					<td class="train_time"><span><strong>07:00</strong><br>12:36</span></td>
					<td class="train_number">	
						<span>G101（高速动车）</span>
						<span class="st"><em>始</em>北京南</span>
						<span class="ed"><em>终</em>上海虹桥</span>	
					</td>
					<td class="duration"><span>5小时36分</span></td>
					<td class="train_price"><strong>￥553</strong><br>二等座</td>
				</tr>			


			</table>
		</section>
	</div>
	<!-- end -->

</div>	
</body>
</html>		
