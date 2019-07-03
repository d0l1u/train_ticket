<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<html>
<head>
<meta charset="utf-8">
<title>掌上19e-火车票</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<meta name="keyword" content="19e,便民服务,数字便民,移动客户端,手机客户端,话费充值,手机充值,游戏充值,QQ充值,机票预订,水费,电费,煤气费,取暖费,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
<meta name="description" content="提供19e数字便民移动终端下载,随时随地办理便民业务,支持移动客户端话费充值,手机充值,游戏充值,QQ充值,机票预订,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
<link rel="stylesheet" type="text/css" href="/css/base.css">
<link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon" />
<link rel="stylesheet" href="/css/style.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/getUrlParam.js"></script>
<script type="text/javascript" src="/js/train_station.js?version=1008"></script>
<script type="text/javascript">  	
	window.onload = function(){
		var memory = window.localStorage || (window.UserDataStorage && UserDataStorage()) || new cookieStorage();
		memory.setItem("linkersStatus", "0");
		
	}
</script>
<style type="text/css">
	.screen {width:480px; height:700px; margin:0 auto;}
	ul li{
		height:40px;
		cursor:pointer;
		margin-top:11px;
	}
	ul li a span,.linkPhone{
		color:#888;
		text-decoration:none;
		font-size:22px;
		text-align:left;
		font-family:Microsoft yahei;
		padding-top:11px;
	}
	hr {
        margin: 0em 1.5em 1em 0em;
        height: 1px;
        width: 100%;
        border-top: 1px #C0C0C0 solid;
        border-left: none;
        border-right: none;
        border-bottom: none;
    }
</style>
</head>

<body>
<div>

	<!-- start -->
	<div class="wrap">
		<header id="bar">
			<a href="javascript:window.history.back();" class="m19e_ret"></a>
			<h1>个人中心</h1>
		</header>
		<section>
			<!-- 
			<ul style="width:100%;">
				<br/>
				<li style="width:100%;padding:0 0;"><a href="trainHome.jsp" style="width:100%;background-color:red;"><span>&nbsp;&nbsp;车票预订</span></a></li>
				<hr />
				<li><a href="/query/queryOrderList.jhtml">&nbsp;&nbsp;查询订单/退款</a></li>
				<hr />
				
				<li><a href="/pages/book/oftenLinkers.jsp">&nbsp;&nbsp;常用联系人</a></li>
				<hr />
			</ul>
			 -->
			 
			<div>
				<table style="margin:0 0 0 0;padding:0 0 0 0;border:none;width:100%;">
					<tr style="line-height:50px;" onclick="location.href='trainHome.jsp';">
						<td class="linkPhone">&nbsp;&nbsp;车票预订</td>
					</tr>
					<tr>
						<td style="line-height:1px;"><hr /></td>
					</tr>
					<tr style="line-height:35px;" onclick="location.href='/query/queryOrderList.jhtml';">
						<td class="linkPhone">&nbsp;&nbsp;查询订单/退款</td>
					</tr>
					<tr>
						<td style="line-height:1px;"><hr /></td>
					</tr>
					<tr style="line-height:35px;" onclick="location.href='/pages/book/oftenLinkers.jsp';">
						<td class="linkPhone">&nbsp;&nbsp;常用联系人</td>
					</tr>
					<tr>
						<td style="line-height:1px;"><hr /></td>
					</tr>
				</table>
			</div>
			
			<div>
				<table style="margin:0 0 0 0;padding:0 0 0 0;border:none;">
					<tr style="line-height:30px;">
						<td class="linkPhone">&nbsp;&nbsp;客服电话:</td>
						<td class="linkPhone">027-87618711</td>
					</tr>
					<tr style="line-height:30px;">
						<td class="linkPhone"></td>
						<td class="linkPhone">027-87618633</td>
					</tr>
				</table>
				<!-- 
				&nbsp;&nbsp;客服电话:&nbsp;027-87618711 
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;027-87618633
				 -->
			</div>
		</section>
	</div>
	<!-- end -->

</div>	
</body>
</html>		
