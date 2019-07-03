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
<link rel="stylesheet" href="/css/style.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/getUrlParam.js"></script>
<script type="text/javascript" src="/js/train_station.js?version=1008"></script>
<script type="text/javascript"> 
		localStorage.setItem("toZh",document.getElementById('toZh').value); 	 	
	    function mymove(){
					var titleObj = document.getElementById("titleDiv");
					var recordsObj = document.getElementById("recordsDiv");
					var scrollWidth = recordsObj.scrollLeft;
					titleObj.scrollLeft = scrollWidth;
		}
		function buttonOk(){
			//var fromZh=UrlParm.parm("fromZh");  
			//var toZh = document.getElementById('toZh').value;
			//if($.trim($("#toZh").val())==""){
			//	$("#toZh").focus();
			//	return;
			//}
			//window.location.href="index.html?toZh="+toZh+"&fromZh="+fromZh;
			
			if($.trim($("#toZh").val())==""){
				$("#toZh").focus();
				return;
			}else{
				var toZh = document.getElementById('toZh').value;
				localStorage.setItem("toZh",toZh); 
				//document.write("toZh: " + localStorage.getItem("toZh"));
			}
			window.location.href="index.jsp";
		}
</script>
<style type="text/css">
	.shouy_ser{
		border:none;
		width:100%;
		height:100%;
		background-color:#87CEFA;
	}
	.shouy_ser .text1{
		width:92%;
		height:40%;
		margin-left:4%;
	}
	.screen {width:480px; height:700px; margin:0 auto;}
</style>
</head>
<body>
<div class="screen">

	<!-- start -->
	<div class="wrap">
		<header id="bar">
			<a href="javascript:window.history.back();" class="m19e_ret"></a>
			<h1>到达城市</h1>
			<a href="javascript:void(0);" onclick="buttonOk();" class="m19e_ok"></a>
		</header>

		<section id="ser_main">
			<div class="ser_data shouy_ser" id="titleDiv">
				<input type="text" class="text1 city_s datapick_txt" id="toZh" name="to_city"  
                    	onfocus="showCity('toZh')" onblur="hideCity()" autocomplete="off" placeholder="到达城市" />
			</div>


		</section>

	</div>
	<!-- end -->

</div>	
</body>
</html>		
