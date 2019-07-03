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
	//页面加载时执行
	$().ready(function() { 
		var fromZh = localStorage.getItem("fromZh");//将出发城市本地存储
		var toZh = localStorage.getItem("toZh");
		if(fromZh==""||fromZh==undefined){
			document.getElementById("fromZh").value = "出发城市";
		}else{
			document.getElementById("fromZh").value = fromZh;
		}
		if(toZh=="" || fromZh==undefined){
			document.getElementById("toZh").value = "到达城市";
		}else{
			document.getElementById("toZh").value = toZh;
		}
		var travel_time = localStorage.getItem("travel_time");//出发时间
		document.getElementById("travel_time").value = travel_time;
	});  
	 
	//出发城市
	function fromCity(){
		var toZh = document.getElementById('toZh').value;
		window.location.href="/pages/book/fromCity.jsp";
	}
	
	//到达城市
	function toCity(){
		var fromZh = document.getElementById('fromZh').value;
		window.location.href="/pages/book/toCity.jsp";
	}
	
	//点击搜索按钮
	function queryTrainInfo(from_station,arrive_station){
		var travel_time = document.getElementById("travel_time").value;//出发时间本地存储
		localStorage.setItem("travel_time",travel_time);
		//window.location.href="train_list.html";
		
		
		var isValid=true;
			if(from_station!=null && from_station!=""){
				$("#fromZh").val(from_station);
			}else{
				if($.trim($("#fromZh").val())=="" || $.trim($("#fromZh").val())=="出发城市"){
					showErrMsg("fromZh", "110px", "请输入出发城市！");
					isValid=false;
				}else{
					hideErrMsg("fromZh");
				}
			}

			if(arrive_station!=null&&arrive_station!=""){
				$("#toZh").val(arrive_station);
			}else{
				if($.trim($("#toZh").val())=="" || $.trim($("#toZh").val())=="到达城市"){
					showErrMsg("toZh", "110px", "请输入到达城市！");
					isValid=false;
				}else{
					hideErrMsg("toZh");
				}
			}
			
			if($("#travel_time").val()==""){
				showErrMsg("travel_time", "110px", "请输入出发日期！");
				isValid=false;
			}else{
				hideErrMsg("travel_time");
			}
			if(isValid==false){
				return;
			}
			
			//消息框	
			var dialog = art.dialog({
				lock: true,
				fixed: true,
				left: '50%',
				top: '250px',
			    title: 'Loading...',
			    icon: "/images/loading.gif",
			    content: '正在查询，请稍候！'
			});
			$(".aui_titleBar").hide();
			$("form:first").submit();
		
		
	}
</script>
<style type="text/css">
	.shouy_ser{
		border:none;
	}
	.shouy_ser .text1{
		width:100%;
		height:45%;
		margin-bottom:3px;
	}
	.screen {width:480px; height:700px; margin:0 auto;}
</style>
</head>

<body>
<div class="screen">
	<form id="trainForm" action="/buyTicket/queryByStation.jhtml" method="post" class="screen">
                <input name="from" class="from" type="hidden" />
                <input name="to" class="to" type="hidden" />
	<!-- start -->
	<div class="wrap">
		<header id="bar">
			<a href="" class="m19e_ret"></a>
			<h1>火车票查询</h1>
			<a href="/pages/book/menuNew.jsp" class="m19e_home"></a>
		</header>
		<section id="main">
			<div id="city" class="city">
				<h2 class="city_sel_t">城市选择</h2>
				<div class="shouy_ser city_sel_c">
                    <input type="text" class="text1 city_s datapick_txt" id="fromZh" name="from_city" 
                    	onclick="fromCity();" autocomplete="off" placeholder="出发城市" />		
                    <br/>
                    <input type="text" class="text1 city_s datapick_txt" id="toZh" name="to_city"  
                    	onclick="toCity();" autocomplete="off" placeholder="到达城市" />
                                
        		</div>
			</div>
			<div id="datapick" class="datapick">
				<input type="date" class="datapick_txt" id="travel_time" name="travel_time" autocomplete="off" placeholder="出发日期">
			</div>
			<div class="ticket_ser_w">
				<input type="submit" value="搜索" class="ticket_ser" onclick="javascript:queryTrainInfo('','');">	
			</div>
		</section>
	</div>
	<!-- end -->
	</form>
</div>	
</body>
</html>		
