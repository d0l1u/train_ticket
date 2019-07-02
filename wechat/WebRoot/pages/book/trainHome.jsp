<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<html>
	<head>
		<meta charset="utf-8">
		<meta name="viewport"
			content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
		<title>酷游旅游</title>
		<meta name="keyword"
			content="19e,便民服务,数字便民,移动客户端,手机客户端,话费充值,手机充值,游戏充值,QQ充值,机票预订,水费,电费,煤气费,取暖费,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
		<meta name="description"
			content="提供19e数字便民移动终端下载,随时随地办理便民业务,支持移动客户端话费充值,手机充值,游戏充值,QQ充值,机票预订,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
		<link rel="stylesheet" type="text/css" href="/css/base.css">
		<link rel="shortcut icon" href="/images/favicon.ico"
			type="image/x-icon" />
		<link rel="stylesheet" href="/css/style.css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript" src="/js/getUrlParam.js"></script>
		<script type="text/javascript" src="/js/train_station.js?version=1008"></script>
		<script type="text/javascript" src="/js/artDialog.js"></script>
		<script type="text/javascript">  	
	//页面加载时执行
	$().ready(function() { 
		var fromZh = localStorage.getItem("startCity");//将出发城市本地存储
		var toZh = localStorage.getItem("endCity");
		if(fromZh==""||fromZh==undefined){
			document.getElementById("fromZh").value = "出发城市";
		}else{
		//	document.getElementById("fromZh").value = fromZh;
		}
		if(toZh=="" || fromZh==undefined){
			document.getElementById("toZh").value = "到达城市";
		}else{
		//	document.getElementById("toZh").value = toZh;
		}
		var now= new Date();
		var year = now.getFullYear();
		var month = (now.getMonth()+1)<10 ? "0"+(now.getMonth()+1):(now.getMonth()+1);
		var dayDate = (now.getDate()+2)<10 ? "0"+(now.getDate()+2):(now.getDate()+2);
		var day = year+"-"+month+"-"+dayDate;//默认显示两天后的riqi
		localStorage.setItem("travelTime",day);
		var travel_time = localStorage.getItem("travelTime");//出发时间
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
	

	function swapCity(){
		var fromCity = $("#fromZh").val().replace("出发城市", "");
		var toCity = $("#toZh").val().replace("到达城市", "");
		$("#fromZh").val(toCity);
		$("#toZh").val(fromCity);
	}
	var limitDay = 20;
	function showDate() {
		$(".sel_date_box").show();
	}
	
	//点击搜索按钮
	function queryTrainInfo(from_station,arrive_station){
		var travel_time = document.getElementById("travel_time").value;//出发时间本地存储
		localStorage.setItem("travelTime",travel_time);
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
					//showErrMsg("toZh", "110px", "请输入到达城市！");
					//isValid=false;
					alert("请选择到城市！");
					return;
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
				id:'homequery',
				lock: false,
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
	window.unonload=art.dialog({
				id:'homequery'}).close();
</script>
		<script>
	$(function(){
		var dataHome= "";
		$('.canbuy').live('click',function(){
			var time = $(this).attr('travel_time');
			setTime(time);
			$(".sel_date_box").hide();
		})
		
		var indexTime = new Date();
		var indexMonth = indexTime.getMonth()+1;
		var indexDay = indexTime.getDate()+2;
		indexMonth = (indexTime.getMonth()+1)< 10 ?'0'+indexMonth: indexMonth;
		indexDay = (indexTime.getDate()+2)< 10 ?'0'+indexDay: indexDay;
		if(dataHome=='' || dataHome==null || dataHome==undefined ){
			setTime(indexTime.getFullYear()+''+indexMonth+''+indexDay);
		}else{
			dataArr=dataHome.split('-');
			if(dataArr[0]=='' || dataArr[0]==null || dataArr[0]==undefined || dataArr[1]=='' || dataArr[1]==null || dataArr[1]==undefined || dataArr[2]=='' || dataArr[2]==null || dataArr[2]==undefined  ){
				setTime(indexTime.getFullYear()+''+indexMonth+''+indexDay);
			}else{
				setTime(dataArr[0]+''+dataArr[1]+''+dataArr[2]);
			}
		}
		function setTime(time){
			//alert(time);
			var year = time.substring(0,4);
			var moth = time.substring(4,6);
			var day = time.substring(6,8);
			var objDay = new Date(year,moth-1,day);
			var week = objDay.getDay();
			moth = objDay.getMonth()+1;
			moth = (objDay.getMonth()+1)< 10 ?'0'+moth: objDay.getMonth()+1;
			day = objDay.getDate() < 10 ? '0'+objDay.getDate() : objDay.getDate();
			var travel_time = moth+'月'+day+'日';
			var arrWeek = Array('星期天','星期一','星期二','星期三','星期四','星期五','星期六');
			var strWeek = arrWeek[week];
			$('#travel_time').html(travel_time+'<em>'+strWeek+'</em>');
			var month = objDay.getMonth()+1;
			$('#travel_time').attr('value',year+'-'+moth+'-'+day);
		}
	
	});
</script>
		<script>
	
$(function(){
	objDate = new objDateTab();
	objDate.setTitle();
	objDate.addDateTab();
	objDate.upTime();
})
var objDateTab = function(){
	_this = this;
	this.objDate = new Date();
}
objDateTab.prototype = {
	//设置头部
	setTitle : function(){
	var month = this.objDate.getMonth()+1;
		var title = this.objDate.getFullYear()+'年'+month+'月';
		$('#title').html(title);
		return title;
	},
	//获取一个月中的最大一天
	getMaxDate : function(){
		var objDate = new Date(this.objDate.getFullYear(),this.objDate.getMonth()+1,0);
		return objDate.getDate();
	},
	//添加tbl
	addDateTab : function(){
		$('#date_tab').html('');
		var objThisTime =  new Date();
		//当前日期
		var now = parseInt(this.getNow(objThisTime));
		// 30天以后
		var lastTime = parseInt(this.getLastTime(objThisTime));
		//---获取当前月的第一天是星期几
		var oneObj = new Date(this.objDate.getFullYear(),this.objDate.getMonth(),1);
		var oneDay = parseInt(oneObj.getDay());
		if(oneObj.getDay() == 0){
			oneDay = 7;
		}else{
			oneDay = oneObj.getDay();
		}
		var maxDate = this.getMaxDate();
		var t = 0,nul = 0;
		for($i=1;$i<=maxDate+oneDay;$i++){
			if($i >= oneDay) t++;
			//列表时间 201212  yestoday
			var TblMonth = this.turnNum(this.objDate.getMonth()+1);
			//列表日期 意思就是用户 上一月 下一月 等 操作的日期
			var travel_time = parseInt(this.objDate.getFullYear()+''+TblMonth+''+ this.turnNum(t));
			if( $i%7 == 1 ) var tr = $("<tr></tr>");
			//判断是否是周6 7
			var bkg = 0,className='',textName='';
		
			if($i%7 == 0 || $i==6 || $i==13 || $i==20 || $i==27 || $i==34){
				bkg = 1;
			}
			if(t > 0){
			if(t<=maxDate){
			var nn = t;
				// 当小于当前时间 大于一个月以后 颜色设置成淡黄色
				if(travel_time < now || travel_time >= lastTime){
				className = bkg == 1 ?'weekend':'yestoday';
				tr.append("<td class="+className+">"+t+'</td>');
				}else{
				//把周6 7颜色换掉
					if(travel_time == now){
						className = bkg == 1 ? 'today weekend canbuy' : 'today canbuy';
						textName = '今天';
					}else{
						className = bkg == 1 ? 'weekend canbuy' : 'canbuy';
						textName = t;
					}
					tr.append("<td travel_time="+travel_time+" class='"+ className +"'> <a href='javascript:void(0)'>"+textName+'</a></td>');
				}
			}
			}else{
				nul++; 
				className = bkg == 1 ? 'weekend': 'yestoday';
				tr.append("<td class='"+className+"'><a href='javascript:void(0)'>"+'</a></td>');
			}
			
			$('#date_tab').append(tr);
		}
		//填补空的TD
		var nulId = 7-(nul+nn)%7;
		if(nulId == 7) nulId = 0;
		var weekend = nulId - 2;
		for(var r=0;r<nulId;r++){
			if(r >= weekend){
				tr.append("<td class='weekend'>&nbsp;</td>");
			}else{
				tr.append("<td>&nbsp;</td>");
			}
			$('#date_tab').append(tr);
		}
	},
	//获取当前时间+30天
	getLastTime : function(objThisTime){	
		objThisTime.setDate(objThisTime.getDate()+limitDay);
		var year = objThisTime.getFullYear();
		var month = this.turnNum(objThisTime.getMonth()+1);
		var date = this.turnNum(objThisTime.getDate());
		// 当前时间+30天的年月日组合 如 2012920    用于跟之后的日期进行对比
		var strTime = year+''+month+''+date;
		return strTime;
	},
	//今天日期  201298
	 getNow : function(objThisTime){
		var year = objThisTime.getFullYear();
		var month = objThisTime.getMonth()+1;
		var	date = objThisTime.getDate();
		return year+''+this.turnNum(month)+''+this.turnNum(date);
	},
	//转换日期 如 9 转换成 09 
	turnNum : function(num){
		return num > 9 ? num : 0+''+num;
	}
	,
	upTime : function(){
		$('.canbuy').live('click',function(){
			$('#backTime').trigger('click');
		})
		$('.change').live('click',function(){
			
			var name = $(this).attr('name');
			var ints = name == 'up' ? -1: 1;

			var lobjdate = new Date(_this.objDate.getFullYear(),_this.objDate.getMonth()+ints);
			var thisMonth = lobjdate.getFullYear()*100+lobjdate.getMonth();
			var laseTime = _this.getLastTime(new Date());
			var nowTime = _this.getNow(new Date());
			//	alert(thisMonth+'  '+laseTime+'   '+nowTime);
			//	start  至 end 是可预定日期
			var startTime = nowTime.substr(0,6);
			
			var endTime = laseTime.substr(0,6);
			var nextMonth,upMonth;
			$('.up_mouth').html(' ');
			$('.down_mouth').html(' ');
			if(thisMonth+2 <=endTime){
				 nextMonth = '<a href="javascript:void(0)" name="next" class="date_down fr change">下月</a>';
			}else{
				 //nextMonth = '<a href="javascript:void(0)" name="next" class="date_down change">下月</a>';
				upMonth = '<a href="javascript:void(0)" name="up" class="date_up fl change">上月</a>';
			}
			/* if(thisMonth  >=startTime){
				 upMonth = '<a href="javascript:void(0)" name="up" class="date_up fl change">上月</a>';
			}else{
				 upMonth = '<a href="javascript:void(0)" name="up" class="date_up fl change">上月</a>';
			} */
			//这块控制的是上下月的样式
			//$('#date_mouth').prepend(nextMonth+upMonth);
			$('.up_mouth').prepend(upMonth);
			$('.down_mouth').prepend(nextMonth);
			
			var strMonth = thisMonth.toString();
		
			if ((thisMonth+1 >= startTime && thisMonth+1 <= endTime)){
				_this.objDate.setFullYear(strMonth.substr(0,4));
				_this.objDate.setMonth(strMonth.substr(4,2),1);
				_this.setTitle();
				_this.addDateTab();
			}
		}
		)
	}
}


</script>
		<style type="text/css">
.search_date .search_date_table th {
	background: #3FA8E5;
	height: 25px;
	color: #fff;
	font-size: 16px;
	text-align: center;
	line-height: 25px;
}

.curr_date {
	vertical-align: middle;
	background: #3FA8E5;
	color: #fff;
	font-size: 16px;
}

.search_date {
	width: 94%;
	background: #FFF;
	position: absolute;
	z-index: 20;
	border: 1px solid #d9d8d7;
	margin: 0;
	margin-left: 3%;
	left: -1px;
}

.search_date .search_date_table td {
	height: 35px;
	line-height: 35px;
	color: #CCC;
	text-align: center;
}

.search_date .search_date_table {
	width: 100%;
	border: 0;
}

.search_date .search_date_table th a {
	width: 100%;
	display: inline-block;
	color: #fff;
	font-weight: normal;
}

.search_date .search_date_table th a:hover {
	background: #2F9ACC;
}

.shouy_ser {
	border: none;
}

.login_border {
	position: absolute;
	right: 8px;
	line-height: 18px;
	color: #fff;
	border: 1px solid #fff;
	height: 18px;
	top: 11px;
	width: 3em;
	text-align: center;
}

.shouy_ser .text1 {
	width: 75%;
	height: 45px;
	line-height: 45px;
	margin-bottom: 3px;
}

.datapick_cf {
	font-size: 18px;
	color: #888;
	font-family: "Microsoft yahei";
	height: 100%;
	width: 100%;
	border: none;
	line-height: 45px;
	text-align: center;
}
</style>
	</head>

	<body>
		<div>
			<form id="trainForm" action="/buyTicket/queryByStation.jhtml"
				method="post" class="screen">
				<input name="from" class="from" type="hidden" />
				<input name="to" class="to" type="hidden" />
				<!-- start -->
				<div class="wrap1">
					<header id="bar">

					<h1>
						火车票查询
					</h1>
					<c:choose>

						<c:when test="${empty user_phone}">
							<a href="/login/turnToLogin.jhtml?openID=${openID}"
								class="login_border">登陆</a>
						</c:when>

						<c:otherwise>
							<a href="/pages/book/menuNew.jsp" class="m19e_home"></a>
						</c:otherwise>
					</c:choose>

					</header>
					<section id="main">
					<div id="city" class="city">

						<div class="shouy_ser city_sel_c">
							<span class="datapick_cf">出发 </span>
							<input type="text" class="text1 city_s datapick_txt" id="fromZh"
								name="from_city" onfocus="showCity('fromZh')"
								onblur="hideCity()" autocomplete="off" placeholder="出发城市"
								value="${param.from_city }" />
							<br />
							<span class="datapick_cf">到达 </span>
							<input type="text" class="text1 city_s datapick_txt" id="toZh"
								name="to_city" onfocus="showCity('toZh')" onblur="hideCity()"
								autocomplete="off" placeholder="到达城市" value="${param.to_city }" />
							<!-- 
                    <input type="text" class="text1 city_s datapick_txt" id="fromZh" name="from_city" 
                    	onclick="fromCity();" autocomplete="off" placeholder="出发城市" />	
                    <br/>
                    <input type="text" class="text1 city_s datapick_txt" id="toZh" name="to_city"  
                    	onclick="toCity();" autocomplete="off" placeholder="到达城市" />
                     -->
						</div>
						<h2 class="city_sel_t" onclick="javascript:swapCity();">
							城市选择
						</h2>
					</div>

					<div id="datapick" class="datapick">
						<label for="travel_time"
							style="vertical-align: middle; color: #888; font-family: 'Microsoft yahei'; font-size: 18px; height: 25px; line-height: 25px;">
							出发日期
						</label>
						<input type="text" class="datapick_txt" onclick="showDate()"
							id="travel_time" name="travel_time" readonly="true" style="height:25px;line-height:25px;"
							autocomplete="off" placeholder="出发日期"
							value="${param.travel_time }" />
						<article class="search_date sel_date_box" style="display:none">
						<table class="search_date_table " border="0">
							<tr>
								<th class="up_mouth"></th>
								<th colspan="5">
									<strong id="title" class="curr_date"></strong>
								</th>
								<th class="down_mouth"></th>
							</tr>
							<tr>
								<th>
									周一
								</th>
								<th>
									周二
								</th>
								<th>
									周三
								</th>
								<th>
									周四
								</th>
								<th>
									周五
								</th>
								<th>
									周六
								</th>
								<th>
									周日
								</th>
							</tr>
							<tbody id="date_tab" class="date_tab"></tbody>
						</table>

						</article>

					</div>
					<div class="ticket_ser_w">
						<input type="button" value="搜索" class="ticket_ser"
							onclick="javascript:queryTrainInfo('','');">
					</div>
					</section>
				</div>
				<!-- end -->
			</form>
		</div>
		<script>
	

	var $objDate = new Date();
	var $m = $objDate.getMonth();
	var $objTdate = new Date($objDate.getFullYear(),$objDate.getMonth(),$objDate.getDate()+limitDay);
	
	if($objTdate.getMonth() != $m){
		//$('#date_mouth').prepend('<span name="up" class="fl change">上一月</span><a href="javascript:void(0)" name="next" class="fr change">下一月</a>');
		//$('.up_mouth').prepend('<a href="javascript:void(0)" name="up" class="date_up fl change">上月</a>');
		$('.down_mouth').prepend('<a href="javascript:void(0)" name="next" class="date_down fr change">下月</a>');
		
	}else{
		//$('#date_mouth').prepend('<span name="up" class="fl">上一月</span><span name="next" class="fr">下一月</a>');
		$('.up_mouth').prepend('<a href="javascript:void(0)" name="up" class="date_up fl">上月</a>');
		//$('.down_mouth').prepend('<a href="javascript:void(0)" name="next" class="date_down fr">下月</a>');
	}
</script>
	</body>
</html>
