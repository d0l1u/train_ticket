<!doctype html>
<html>
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<meta charset="utf-8">
<title>掌上19e-火车票</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<meta name="keyword" content="19e,便民服务,数字便民,移动客户端,手机客户端,话费充值,手机充值,游戏充值,QQ充值,机票预订,水费,电费,煤气费,取暖费,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
<meta name="description" content="提供19e数字便民移动终端下载,随时随地办理便民业务,支持移动客户端话费充值,手机充值,游戏充值,QQ充值,机票预订,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
<link rel="stylesheet" type="text/css" href="/css/base.css">
<link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/artDialog.js"></script>
<style type="text/css">
	.text_item {font-size:16px;}
	.input_hidden_border {font-size:14px;width:70px;border:0;font-family:Microsoft YaHei, calibri, verdana;}
	.adult {line-height:28px;}
	.wrap {position:relative;}
	td.icon{vertical-align:middle;width:50%;}
</style>
<script type="text/javascript">
$().ready(function() { //页面加载时执行
	var baoxian = localStorage.getItem("baoxian");
	var train_no = localStorage.getItem("trainCode");
	var from_city = localStorage.getItem("startCity");
	var to_city = localStorage.getItem("endCity");
	var travelTime = localStorage.getItem("travelTime");
	var from_time = localStorage.getItem("startTime");
	var endTime = localStorage.getItem("endTime");
	var seat_price = localStorage.getItem("seat_price");
	var seat_type = localStorage.getItem("seat_type");
	
	
	param_product_id = baoxian;
	
	//var jsonStr = '[{"name":"李延华","id":"371525198505056727","idType":"2","ticketType":"0"},{"name":"王世刚","id":"220102196910211816","idType":"2","ticketType":"0"}]';
	var jsonStr = localStorage.getItem("passengers"); 
	var myobj=eval(jsonStr); 
	var table = document.getElementById("train_ticket_list"); 
	var tableStr = "";   
	for(var i=0;i<myobj.length;i++){
	    //alert("name:"+myobj[i].name+",id:"+myobj[i].id+",idType:"+myobj[i].idType+",ticketType:"+myobj[i].ticketType);   
		var m = i+1;
		if(i>0){
			tableStr = tableStr+'<tr><td></td></tr>';
		}
	    tableStr = tableStr+'<tr class="adult" ><td><table style="width:100%;margin:0 0 0 0;padding:0 0 0 0;">'+
							 	'<tr>'+
							 		'<td><input type="hidden" value="'+i+'"/></td>'+
							 		'<td class="text_item">'+
							 			'<input class="user_name_text input_hidden_border" style="font-size:14px;" type="text" name="bookDetailInfoList['+i+'].user_name" id="user_name_'+m+'" title="姓名" value="'+myobj[i].name+'" />'+
							 		'</td>'+
							 		
							 		'<td class="text_item" >'+
							 		'	<input class="input_hidden_border" type="hidden" name="bookDetailInfoList['+i+'].ticket_type" id="ticket_type_'+m+'" value="'+myobj[i].ticketType+'" />'+
									'<span style="font-size:14px;">'+judgeTicketType(myobj[i].ticketType)+'</span></td>'+
							 	'</tr>'+
							 	'<tr>'+
							 		'<td></td>'+
							 		'<td class="text_item" colspan="2">'+
							 			'<input style="width:91px;" class="input_hidden_border" type="hidden" name="bookDetailInfoList['+i+'].ids_type" id="ids_type_'+m+'"  value="'+myobj[i].idType+'" />'+
							 			'<span style="font-size:14px;">'+judgeIdsType(myobj[i].idType)+'</span>'+
							 			'&nbsp;&nbsp;<input style="width:65%;" style="font-size:14px;" class="idcard_text input_hidden_border" type="text" name="bookDetailInfoList['+i+'].user_ids" id="user_ids_'+m+'" title="证件号码" value="'+myobj[i].id+'" />'+
							 		'</td>'+
						 		'</tr>'+
					
						 ' </table></td></tr>';
		 //alert(tableStr);
	} 
	$("#train_ticket_list").html(tableStr);   
	
	if( baoxian=="0" ){
		$("#meal_insurance").html("不购买保险");
		document.getElementById('shouldPayMoney').innerHTML += ("应付金额:<strong>￥"+(Number(localStorage.getItem("seat_price"))*myobj.length)+"</strong>"); 
	}else{
		//有几个乘客，买几份保险
		$("#meal_insurance").html('20*'+myobj.length+'=<em class="red">'+(20*myobj.length)+'</em>元');
		document.getElementById('shouldPayMoney').innerHTML += ("应付金额:<strong>￥"+((Number(localStorage.getItem("seat_price"))+20)*myobj.length)+"</strong>");
	}
});  
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
//确认支付
function gotoPay(){
	var count = $(".adult:visible").length;
	//var bankAbbr = $("#bankAbbr").val();
	var bankAbbr = "";
	var order_id = $("#order_id").val();
	if(confirm("确认前往支付该订单吗？")){
		//$("form:first").attr("action", "/order/orderCmpay.jhtml?order_id="+order_id+"&bankAbbr="+bankAbbr+"&productNum"+count);
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
		//$("form:first").submit();
		//alert("orderid:::" + order_id);
		window.location="/order/orderCmpay.jhtml?order_id="+order_id+"&bankAbbr="+bankAbbr+"&productNum="+count;
		localStorage.setItem("passengers",""); 
	}
}
</script>
</head>

<style type="text/css">
.screen {width:480px;  margin:0 auto;}
</style>

<body>

<div>

	<!-- start -->
	<div class="wrap">
		<header id="bar">
			<a onclick="window.location='/pages/book/bookInfoInput.jsp';" class="m19e_ret"></a>
			<h1>订单支付</h1>
			<a href="/pages/book/menuNew.jsp" class="m19e_home"></a>
		</header>
		<section id="order_result">
			
			<div class="order_res_box">
				<h2 style="height:50px;line-height:50px;">订单已提交</h2>
				<ul class="order_res_c">
					<li><span>车次:</span><strong><em class="org">${orderInfo.train_no}</em>${orderInfo.from_city}</strong><span>(${orderInfo.from_time})</span>--<strong>${orderInfo.to_city}</strong><span>(${orderInfo.to_time})</span></li>
					<li><span>日期:</span>${orderInfo.travel_time}（${day }）</li>
					<li><span>乘客:</span><table style="width:85%;margin:-10% 0 0 15%;padding:0 0 0 0;" id="train_ticket_list"></table>
					</li>
					<li><span>坐席:</span>${seatTypeMap[orderInfo.seat_type]}</span></li>
					<li><span>保险:</span><span id="meal_insurance"></span></li>
					<input type="hidden" id="order_id" value="${order_id }" name="order_id" >
					<li><span>订单号:</span>${order_id }</li>
					<li><span>订单状态:</span><em class="red">等待支付</em></li>
				</ul>
			</div>
		 	<div>
		 		<br/><br/><br/><br/><br/>
				<p align="center" id="bx_confirm" >请在45分钟内支付，以免订单失效</p><br/>
		 	</div>
		 	
			<div class="order_foot">
				<div class="order_d" style="background:#d13b00;">
					<span class="order_pay" id="shouldPayMoney"></span>
				</div>
				<div class="order_d" style="background:#ff7200;">
					<span class="order_btn"><a href="javascript:gotoPay();" class="order_submit">支付</a></span>
				</div>
			</div>
			
			
		</section>
	</div>
	<!-- end -->

</div>	
</body>
</html>		
