<!doctype html>
<html>
	<head>
		<%@ page language="java" contentType="text/html; charset=UTF-8"%>
		<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
		<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
		<meta charset="utf-8">
		<title>酷游旅游</title>
		<meta name="viewport"
			content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
		<meta name="keyword"
			content="19e,便民服务,数字便民,移动客户端,手机客户端,话费充值,手机充值,游戏充值,QQ充值,机票预订,水费,电费,煤气费,取暖费,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
		<meta name="description"
			content="提供19e数字便民移动终端下载,随时随地办理便民业务,支持移动客户端话费充值,手机充值,游戏充值,QQ充值,机票预订,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
		<meta http-equiv="pragma" content="no-cache">
		<link rel="stylesheet" type="text/css" href="/css/base.css">
		<link href="/css/style1.css" rel="stylesheet" />
		<link rel="shortcut icon" href="/images/favicon.ico"
			type="image/x-icon" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript" src="/js/artDialog.js"></script>
		<style type="text/css">
.screen {
	width: 480px;
	margin: 0 auto;
}

.order_con {
	padding: 0 0;
}

span.indexTr {
	color: #000;
}

.order_item dd {
	line-height: 25px;
}

.shadow {
	position: fixed;
	top: 0;
	left: 0;
	height: 100%;
	width: 100%;
	background: #000;
	filter: Alpha(opacity =             60);
	-moz-opacity: 0.6;
	opacity: 0.6;
	z-index: 30;
}

.passenger_popup,.login_popup,.loading_popup,.tip_popup {
	width: 310px;
	background: #FFF;
	position: fixed;
	z-index: 50;
	top: 30%;
	left: 50%;
	margin-left: -155px;
}

.passenger_popup dt,.explain_popup dt,.tip_popup dt {
	height: 35px;
	line-height: 35px;
	color: #fff;
	text-indent: 15px;
	background: #3FA8E5;
}

.passenger_popup dd a {
	width: 100%;
	height: 50px;
	display: block;
	color: #333;
}

.passenger_popup dt a.close,.explain_popup dt a.close,.tip_popup dt a.close{
	width: 25px;
	height: 25px;
	float: right;
	margin: 5px 5px 0 0;
	background: url(/images/cancel-icon.png)
		no-repeat;
	background-size: 100% auto;
}

a {
	text-decoration: none;
	color: #32a4d9;
}

.passenger_popup dd {
	min-height: 50px;
	background: #fff;
	border-bottom: 1px solid #d9d8d7;
	line-height: 50px;
	text-indent: 15px;
}

td.icon {
	vertical-align: middle;
	width: 40%;
	position: absolute;
	left: 50%;
	margin-left: -25%;
	height: 60px;
}

td.right {
	
}

.blank {
	margin-top: 10px;
}

.text_item {
	font-size: 14px;
	color: #888;
}

.linkinput {
	font-size: 16px;
	width: 40%;
}

.linktr {
	border: 1px solid #cbcbcb;
	border-collapse: collapse;
}

.input_hidden_border {
	font-size: 14px;
	color: #888;
	width: 70px;
	border: 0;
	font-family: Microsoft YaHei, calibri, verdana;
}

.pass_inp,.pass_sel {
	width: 240px;
	height: 27px;
	margin: 0 0;
	display: block;
	border-radius: 3px;
	padding-left: 0;
	border: 0px solid #e9e9e9;
	background: none;
}

.pass_sel {
	width: 130px;
	height: 35px;
	padding-left: 10px;
}

.pass_ziti {
	width: 100%;
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
	background: url(/images/cancel-icon.png) no-repeat center center;
	background-size: 90%;
}

.passenger_add {
	position: absolute;
	right: 8px;
	top: 0;
	padding-right: 30px;
	background: url(/images/add-btn.png) no-repeat right 30%;
	color: #0f63b8;
	background-size: 50%;
}

.wrap {
	position: relative;
}

.passenger_tip {
	padding: 5px 5px 5px 5px;
	font-size: 16px;
	color: #f00;
	display: none;
}

.passenger_tip_block {
	padding: 5px 5px 5px 5px;
	font-size: 16px;
	color: red;
	display: block;
}

.table_passenger {
	border-bottom: 15px solid #f5f6f6;
}

.order_infor .org {
	color: #ff5400;
}
</style>
		<script type="text/javascript">

	var userId = "${agentId}"+"_hcp";
	var cookieValue = ""; 	//编码后的cookie值
	var cookieRealValue	= "";	//cookieJson解析后需要使用的数据
	var param_product_id = "";	//传递给后台的保险ID值
	var baoxian = localStorage.getItem("baoxian");
	//alert("前"+baoxian);
	if(baoxian=="" || baoxian==null){
		baoxian=1;
	}
	//alert("后"+baoxian);
	$().ready(function() { //页面加载时执行
		var table = document.getElementById("train_ticket_list");       
		var count = table.rows.length;//行数   
		//document.getElementById("index_source").value = count;
		//var index_source = document.getElementById("index_source_id").value;
		if(count==1){
			$("#passenger_delete").attr("class","unableAdd");
		}
		
		localStorage.setItem("linkersStatus", "1");
		//var baoxian = localStorage.getItem("baoxian");
		var fpNeed = localStorage.getItem("fpNeed");
		var fp_receiver = localStorage.getItem("fp_receiver");
		var fp_phone = localStorage.getItem("fp_phone");
		var fp_address = localStorage.getItem("fp_address");
		var fp_zip_code = localStorage.getItem("fp_zip_code");
		document.getElementById("baoxian").value = baoxian;
		document.getElementById("fpNeed").value = fpNeed;
		document.getElementById("fp_receiver").value = fp_receiver;
		document.getElementById("fp_phone").value = fp_phone;
		document.getElementById("fp_address").value = fp_address;
		document.getElementById("fp_zip_code").value = fp_zip_code;
		
		var train_no = localStorage.getItem("trainCode");
		document.getElementById("train_no").value = train_no;
		var from_city = localStorage.getItem("startCity");
		document.getElementById("from_city").value = from_city;
		var to_city = localStorage.getItem("endCity");
		document.getElementById("to_city").value = to_city;
		var travelTime = localStorage.getItem("travelTime");
		document.getElementById("travelTime").value = travelTime;
		var from_time = localStorage.getItem("startTime");
		document.getElementById("from_time").value = travelTime+" "+from_time;
		var endTime = localStorage.getItem("endTime");
		document.getElementById("to_time").value = travelTime+" "+endTime;
		var seat_price = localStorage.getItem("seat_price");
		document.getElementById("danjia").value = seat_price;
		var seat_type = localStorage.getItem("seat_type");
		document.getElementById("seat_type").value = judgeSeatType(seat_type);
		document.getElementById('location').value=window.location.href;
		
		
		document.getElementById('trainCode').innerHTML += ("<strong>"+localStorage.getItem("trainCode")+"</strong>");
		document.getElementById('travel_time').innerHTML += (localStorage.getItem("travelTime")+" "+localStorage.getItem("day"));
		document.getElementById('startCity').innerHTML += localStorage.getItem("startCity");
		document.getElementById('endCity').innerHTML += localStorage.getItem("endCity");
		document.getElementById('startTime').innerHTML += localStorage.getItem("startTime");
		document.getElementById('endTime').innerHTML += localStorage.getItem("endTime");
		document.getElementById('seatType').innerHTML += localStorage.getItem("seat_type");
		document.getElementById('seatPrice').innerHTML += ("￥"+localStorage.getItem("seat_price"));

		/**
		//var jsonStr = '[{"name":"李延华","id":"371525198505056727","idType":"2","ticketType":"0"},{"name":"王世刚","id":"220102196910211816","idType":"2","ticketType":"0"}]';
		var jsonStr = localStorage.getItem("passengers"); //alert(jsonStr);
		//alert(jsonStr);
		var myobj=eval('('+jsonStr+')'); 
		var table = document.getElementById("train_ticket_list"); 
		var tableStr = "";   
		for(var i=0;i<myobj.length;i++){
		    //alert("name:"+myobj[i].name+",id:"+myobj[i].id+",idType:"+myobj[i].idType+",ticketType:"+myobj[i].ticketType);   
			var m = i+1;
			if(i>0){
				tableStr = tableStr+'<tr><td><hr/></td></tr>';
			}
			if(myobj[i].ticketType == "0"){
		    	tableStr = tableStr+'<tr class="adult" ><td><table style="width:100%;margin:0 0 0 0;padding:0 0 0 0;">';
		    }
		    if(myobj[i].ticketType == "1"){
		    	tableStr = tableStr+'<tr class="child" ><td><table style="width:100%;margin:0 0 0 0;padding:0 0 0 0;">';
		    }
					tableStr = tableStr + '<tr>'+
								 		
								 		'<td class="text_item" style="width:60%;padding-left:10px;">'+
								 			'姓名：'+
								 			'<input class="user_name_text input_hidden_border" type="text" name="bookDetailInfoList['+i+'].user_name" id="user_name_'+m+'" title="姓名" value="'+myobj[i].name+'" readonly="true" />'+
								 		'</td>'+
								 		
								 		'<td class="text_item" style="width:30%;text-align:center;">'+
								 		'	<input class="input_hidden_border ticket_type_select" type="hidden" name="bookDetailInfoList['+i+'].ticket_type" id="ticket_type_'+m+'" value="'+myobj[i].ticketType+'" readonly="true" />'+
										'<span>'+judgeTicketType(myobj[i].ticketType)+'</span></td>'+
								 		'<td style="width:12%;align:center" rowspan="2" onclick="javascript:void(0);" class="passenger_delete">&nbsp;&nbsp;&nbsp;&nbsp;</td>'+
								 	'</tr>'+
								 	'<tr>'+
								 		'<td class="text_item" colspan="2"  style="padding-left:10px;">'+
								 			'<input style="width:92px;" class="input_hidden_border" type="hidden" name="bookDetailInfoList['+i+'].ids_type" id="ids_type_'+m+'"  value="'+myobj[i].idType+'" readonly="true"/>'+
								 			'<span>'+judgeIdsType(myobj[i].idType)+'</span>';
								 			//alert(myobj[i].id);
								 tableStr = tableStr + '：<input style="width:160px" class="idcard_text input_hidden_border" type="text" name="bookDetailInfoList['+i+'].user_ids" id="user_ids_'+m+'" title="证件号码" value="'+myobj[i].id+'" readonly="true" />'+
								 		'</td><td></td>'+
							 		'</tr>'+
									
							 ' </table></td></tr>';
							 if(baoxian=='1') {
							 	tableStr = tableStr + '<input type="hidden" name="bookDetailInfoList[' + i + '].product_id" id="product_id_' + m + '" value="PT000000001">';
							 	tableStr = tableStr + '<input type="hidden" name="bookDetailInfoList[' + i + '].sale_price" id="sale_price_' + m + '" value="20">';
							 }else if(baoxian=='0'){
							 	tableStr = tableStr + "<input type='hidden' name='bookDetailInfoList[" + i + "].product_id' id='product_id_" + m + "' value='PT000000005'>";
							 	tableStr = tableStr + "<input type='hidden' name='bookDetailInfoList[" + i + "].sale_price' id='sale_price_" + m + "' value='0'>";
							 }
			 //alert(tableStr);
		} 
		$("#train_ticket_list").html(tableStr);   
		*/
		var baoxianStr = "";
		//alert("ready baoxian=="+baoxian);
		if( baoxian=="0" ){
			$("#meal_insurance").html("不购买保险<a href='/pages/book/orderMeal.jsp' id='add_insurance' class='insurance_choose text_item'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>");
			document.getElementById('shouldPayMoney').innerHTML += ("￥"+(Number(localStorage.getItem("seat_price"))*count)); 
			document.getElementById("product_id").value = "PT000000005";
			param_product_id = "PT000000005";
			baoxianStr = baoxianStr + "<input type='hidden' name='bookDetailInfoList[" + 0 + "].product_id' id='product_id_" + 1 + "' value='PT000000005'>";
			baoxianStr = baoxianStr + "<input type='hidden' name='bookDetailInfoList[" + 0 + "].sale_price' id='sale_price_" + 1 + "' value='0'>";
		}else{
			var table = document.getElementById("train_ticket_list");        
			var count = table.rows.length;
			//有几个乘客，买几份保险
			$("#meal_insurance").html('交通意外险&nbsp;￥20*'+count+'<a href="/pages/book/orderMeal.jsp" id="add_insurance" class="insurance_choose text_item">&nbsp;&nbsp;&nbsp;</a>');
			document.getElementById('shouldPayMoney').innerHTML += ("￥"+((Number(localStorage.getItem("seat_price"))+20)*count));
			document.getElementById("product_id").value = "PT000000001";
			param_product_id = "PT000000001";
			baoxianStr = baoxianStr + '<input type="hidden" name="bookDetailInfoList[' + 0 + '].product_id" id="product_id_' + 1 + '" value="PT000000001">';
			baoxianStr = baoxianStr + '<input type="hidden" name="bookDetailInfoList[' + 0 + '].sale_price" id="sale_price_' + 1 + '" value="20">';
		}
		$(baoxianStr).appendTo("#train_ticket_list").show();
		//document.getElementById("bx_pay_money").value = 20*myobj.length;
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
			ids_type = "身份证";
		}else if(ids_type=="3"){
			ids_type = "港澳通行证";
		}else if(ids_type=="4"){
			ids_type = "台湾通行证";
		}else if(ids_type=="5"){
			ids_type = "护照";
		}
		return ids_type;
	}
	
	function showdiv(divname) {      
             //alert($("#" + divname).css("display")+divname );
             if ($("#" + divname).css("display") == "none") {       //显示div
                 $("#" + divname).slideDown(500);
              
                 //SetCookie(divname, true);
                 
               //  if (document.cookie != "") alert(getCookie("youhui"));
             }
             else if ($("#" + divname).css("display") == "block") {       //隐藏div
             	$("#" + divname).slideUp(500);
             	//SetCookie(divname, false);
            	//delcookie(divname);
            // alert(getCookie(divname));
              }
            else  {
                $("#" + divname).slideDown(500);
                //SetCookie(divname, true);
                alert("执行出错");
              }
             }
	//添加通行成人
	function adddiv(){		
		//var count = $(".adult:visible,.child:visible").length;	
		var table = document.getElementById("train_ticket_list");    
		var count = table.rows.length;   
		if(count==4){
			$("#add_person").attr("class","unableAdd");
		}
		var baoxian_str = "";
		//alert("add baoxian=="+baoxian);
		if(baoxian=='1') {
			document.getElementById('shouldPayMoney').innerHTML = ("￥"+((Number(localStorage.getItem("seat_price"))+20)*(count+1)));
			baoxian_str = baoxian_str+ '<input type="hidden" name="bookDetailInfoList[' + count + '].product_id" id="product_id_' + (count+1) + '" value="PT000000001">';
			baoxian_str = baoxian_str+ '<input type="hidden" name="bookDetailInfoList[' + count + '].sale_price" id="sale_price_' + (count+1) + '" value="20">';
		}else if(baoxian=='0'){
			document.getElementById('shouldPayMoney').innerHTML = ("￥"+(Number(localStorage.getItem("seat_price"))*(count+1))); 
			baoxian_str = baoxian_str+ "<input type='hidden' name='bookDetailInfoList[" + count + "].product_id' id='product_id_" + (count+1) + "' value='PT000000005'>";
			baoxian_str = baoxian_str+ "<input type='hidden' name='bookDetailInfoList[" + count + "].sale_price' id='sale_price_" + (count+1) + "' value='0'>";
		}		
		var passengerMsg = '<tr class="adult blank" >'+
					 		'<td>'+
							'<table style="width:100%;margin:0 0 0 0;padding:0 0 0 0;border:0px solid red;border-top:10px solid #f5f6f6;">'+
							 	'<tr style=" border-bottom:1px solid #C0C0C0">'+
							 		'<td class="text_item" style="color:black;">购票类型&nbsp;&nbsp;&nbsp;&nbsp;<input type="hidden" id="'+(count+1)+'_id" value="'+(count+1)+'"/></td>'+
							 		'<td class="text_item" style="text-align:right;"><input type="hidden" name="bookDetailInfoList['+count+'].ticket_type" id="ticket_type_'+(count+1)+'" value="0"/>'+
							 			'第<span class="indexTr">' + (count+1) + '</span>位&nbsp;&nbsp;成人票<a href="javascript:void(0);" id="passenger_delete" class="passenger_delete">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;'+
							 	'</tr>'+
							 	
							 	'<tr style=" border-bottom:1px solid #C0C0C0">'+
							 		'<td class="text_item" style="color:black;">乘车人姓名&nbsp;&nbsp;</td>'+
							 		'<td class="text_item" >'+
							 			'<input type="text" name="bookDetailInfoList[' + (count ) + '].user_name" id="user_name_'+(count+1)+'" class="pass_ziti pass_inp text_item user_name_text" autocomplete="off" placeholder="请填写真实姓名以免取不出票"  />'+
									'</td>'+
							 	'</tr>'+
							 	'<tr>'+
							 		'<td class="text_item" style="color:black;"><input type="hidden" name="bookDetailInfoList['+count+'].ids_type" id="ids_type_'+(count+1)+'" value="2"/>'+
							 			'二代身份证&nbsp;&nbsp;'+baoxian_str+'</td>'+
							 		'<td class="text_item" >'+
							 			'<input type="text" name="bookDetailInfoList[' + (count ) + '].user_ids" id="user_ids_'+(count+1)+'" class="pass_ziti pass_inp text_item" autocomplete="off" placeholder="请填写证件号码"/>'+
									'</td>'+
									
							 	'</tr>'
						    '</table>'+
						'</td>'+
						'</tr>';
		 		
			$(passengerMsg).appendTo("#train_ticket_list").show();
	}
	function openwindow(){ 
         window.open ("orderMeal.jsp","Sample","fullscreen=no,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=no, copyhistory=no,width=50%,height=50%,left=25%,top=25%");
       }  
	
	//删除乘客
	$(".passenger_delete").live("click", function(){
		var table = document.getElementById("train_ticket_list");  
		if(table.rows.length==1){
			$("#passenger_delete").attr("class","unableAdd");
			//document.getElementById("index_source").value = table.rows.length; 	
			return false;
		}
			//alert(table.rows.length);
		var name = "";
		var card_num = "";	
		//$(this).parents("tr").css('background-color', 'red');	
	    $(this).parents("tr").parents("table").parents("tr").find(":text").each(function(){
		    //alert($(this).attr("id"));
			if($(this).attr("id").indexOf("name")>0){
				name = $("#"+$(this).attr("id")).val();
			};
			if($(this).attr("id").indexOf("ids")>0){
				card_num = $("#"+$(this).attr("id")).val();
			};
			
		});
		
		
		removePaxAttr(name,card_num);
		//$(this).parents("tr").parents("table").parents("tr").prev().remove();
		$(this).parents("tr").parents("table").parents("tr").remove();
		//$(this).parents("tr").parents("table").parents("tr").find(":text").each(function(){
		//	hideErrMsg($(this).attr("id"));
		//});
		
		
		//重新设置index
		$(".adult:visible,.child:visible").each(function(index){
			var newIndex = index+1;//alert(newIndex);
			var sname = "";
			var scard = "";
			$(this).find(":text").each(function(){
			    //alert($(this).attr("id"));
				if($(this).attr("id").indexOf("name")>0){
					sname = $("#"+$(this).attr("id")).val();
				};
				if($(this).attr("id").indexOf("ids")>0){
					scard = $("#"+$(this).attr("id")).val();
				};
				
			});
			$(this).html($(this).html().replace(/bookDetailInfoList\[\d+\]/g, "bookDetailInfoList["+index+"]"))
				.find(".indexTr").html(newIndex);
			//alert("前面"+$(this).html());
			$(this).html($(this).html().replace(/\_\d/g, "_"+newIndex));
			//alert("num为:"+$("#num_" + newIndex).val());
			$("#num_" + newIndex).val(newIndex);
			$(this).find(":text").each(function(){
			    //alert($(this).attr("id"));
				if($(this).attr("id").indexOf("name")>0){
					$("#"+$(this).attr("id")).val(sname);
				};
				if($(this).attr("id").indexOf("ids")>0){
					$("#"+$(this).attr("id")).val(scard);
				};
				
			});
			//alert("后面"+$(this).html());
		});	
		$("#add_person").attr("class","passenger_add");
		
		//兼容IE
		$("#add_person").attr("style","");
		var table1 = document.getElementById("train_ticket_list");        
		var count = table1.rows.length;
		//alert("delete baoxian=="+baoxian);
		if( baoxian=="0" ){
			document.getElementById('shouldPayMoney').innerHTML = ("￥"+(Number(localStorage.getItem("seat_price"))*count)); 
		}else{
			document.getElementById('shouldPayMoney').innerHTML = ("￥"+((Number(localStorage.getItem("seat_price"))+20)*count));
		}
		
	});
	
	function popup(msg,timer) {
		var 
		h = $(document).scrollTop() +$(window).height() ;
		w = $(window).width();
		lw = Math.floor(w/2-200/2);
		var ht = 
		'<div id="popup" style="display:none;position:absolute;top:100%;left:'+
		lw+
		'px;text-align:center;background:#f6f9cc;border:1px solid #999;width:200px;z-index:1200;box-shadow:0 0 5px rgba(0,0,0,.4)">' +
		'<div class="popup-content" style="padding:15px"></div>' +
		'<a href="javascript:;" class="close-popup" style="display:inline-block;line-height:20px;position:absolute;right:5px;top:5px;width:20px;text-decoration:none;height:20px;font-family:Arial;font-size:28px;font-weight:bold;text-align:center;color:#777;border-radius:50px;">&times;</a>' +
		'</div>';
		$('#popup').remove();
		$('body').append(ht);
		var t ;
		function popout() {
			$('#popup').animate({
				top:'100%'
			},500,function() {
				$('#popup').hide();
			});
		}
		
		$('#popup .close-popup').click(function() {
			clearTimeout(t);
			popout();
			
		}).hover(function() {
			$(this).css({
				color:'#000'
			})
		},function() {
			$(this).css({
				color:'#777'
			})
		});
		$('#popup .popup-content').html(msg);
		$('#popup').show().animate({
			top:h/2
		},500,function() {
			t = setTimeout(function() {
				popout();
			},timer? parseInt(timer):5500)
		});
		
	}
	
	/*
	function openBx(){
		var content = 'hello, world!';
		popup(content);
	}
	
	function selfMessage(baoxian){
		if(baoxian=="0"){
		
	}
*/
		//从localstorage删除存储的联系人
	function removePassenger(name, id, ticketType){
		var passenger_data = JSON.parse(localStorage.getItem("passengers"));
		var passenger_array = eval(passenger_data);
		for(var i = 0;i < passenger_array.length;i++){
			var arr = passenger_array[i];
			if(arr.name != name && (arr.id != id) && (arr.ticketType != ticketType)){
				//passenger_data.push(arr);
			}else {
				//if(confirm("确定要删除该联系人？")){
					//alert("已删除该联系人");
					passenger_array.splice(i, 1);
					break;
				//}
			}
		}
		localStorage.setItem("passengers", JSON.stringify(passenger_array));
		//showdiv();
		window.location.reload();
	}
		
		//从通行乘客中删除常用乘客的同时清空该常用乘客的选中按钮
	    function removePaxAttr(name,card_num){
		    if(cookieRealValue!=null && cookieRealValue!=undefined){
		    	var size = cookieRealValue.length;
				for(var i=0; i<size; i++){
					if(name == cookieRealValue[i].name && card_num == cookieRealValue[i].card_num){
						$("#cookie_"+i).removeAttr("class");
						$(".cookie_"+i).removeAttr("checked");
					}else{
						continue;
					}
				}
			}
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

	//检测是否登录
	function checkLogin() {
		var user_id=$.trim($("#user_id").val());
		var user_phone=$.trim(${"#user_phone").val());
		var openID=$.trim($("#openID").val());
		$.ajax({
             type: "GET",
             url: "/register/chekLogin.jhtml",
             data: {user_id:user_id, user_phone:user_phone, openID:openID},
             dataType: "text",
             success: function(data){
                        if(data=="0"){
                        
                        }
                        
                      }
         });
	}
	
	  //提交订单
		$("#btnSubmit").live("click", function(){
			if(checkForm()){
				//无座备选
				//selectWz();
				checkLogin();
				submitForm();
			}
			
			function submitForm(){
				//消息框	
				
				var dialog = art.dialog({
					lock: true,
					fixed: true,
					left: '50%',
					top: '40%',
				    title: 'Loading...',
				    //icon: "/images/loading.gif",
				    content: '正在验证乘客和余票信息，请稍候！'
				});
				
				$(".aui_titleBar").hide();
				//保存常用乘客信息
				//savePaxInfo();
				//验证余票信息开始
				//var params = {};
				//params["from_city"]=$("#from_city").val();
				//params["to_city"]=$("#to_city").val();
				//params["travel_time"]=$("#travelTime").val();
				//params["train_no"]=$("#train_no").val();
				//params["seat_type"]=$("#seat_type").val();
				//$.ajax({
				//	url:"/buyTicket/checkTicketEnoughAjax.jhtml",
				//验证余票信息结束
				//异步验证联系人是否审核通过
				//获取联系人姓名和身份证号
				var obj=[];
				var jsonstr='';
				jsonstr="["; 
				$(".adult:visible").each(function(){
					var userName=$.trim($(this).find(".user_name_text").val());
					var userIds=$.trim($(this).find(".idcard_text").val());
					jsonstr += "{\"userName\""+ ":" + "\"" + userName + "\","; 
					jsonstr += "\"userIds\""+ ":" + "\"" + userIds + "\","; 
					jsonstr = jsonstr.substring(0,jsonstr.lastIndexOf(',')); 
					jsonstr += "},";
				});
				jsonstr = jsonstr.substring(0,jsonstr.lastIndexOf(',')); 
				jsonstr += "]";
				$.ajax({
					url:"/userIdsCardInfo/checkUserIdsCardInfo.jhtml",
					type: "POST",
					cache: false,
					data: {'data':jsonstr},
					success: function(res){
						if(res=='SUCCESS'){
							dialog.content('验证成功，可以订购！');
							if($("#chk_fp:visible").length>0 && $("#chk_fp").attr("checked")){
								$("form:first").attr("action", "/order/createOrder.jhtml?fpNeed=1&product_id="+param_product_id);
							}else{
								$("form:first").attr("action", "/order/createOrder.jhtml?product_id="+param_product_id);
							}
							$("form:first").submit();
						}else if(res=='FAIL'){
							dialog.content('验证失败，请重试！');
							dialog.title("提示");
							dialog.button({name: '确认',callback: function(){}});
							$(".aui_titleBar").show();
						}else{
							var dataObj=eval("("+res+")");
							var strAll='';
							$.each(dataObj.errorData,function(idx,item){
								var first;
								first=idx+1;
								if(item.status=='1'){
									strAll+="<p style='width:360px;color:#555;font: 12px/18px Simsun;padding-bottom:6px;'>"+first+"."+item.userName+"("+item.ids_card+")<br />&nbsp;&nbsp;身份信息待核验，请乘客到火车站窗口实名认证后，方可购票！</p>"
								}else{
									strAll+="<p style='width:360px;color:#555;font: 12px/18px Simsun;padding-bottom:6px;'>"+first+"."+item.userName+"("+item.ids_card+")<br />&nbsp;&nbsp;身份信息未通过审核，无法购票！</p>"
								}
							});
							if(strAll!=''){
								dialog.content(strAll);
								dialog.title("提示");
								//dialog.icon("/images/warning.png");
								dialog.button({name: '确认',callback: function(){}});
								$(".aui_titleBar").show();
							}else{
								dialog.content('验证失败，清重试！');
								dialog.title("提示");
								dialog.button({name:'确认',focus: true,callback: function(){}});
								$(".aui_titleBar").show();
							}
						}
					},
					error: function(res){
							dialog.content('验证失败，请重试！');
							dialog.title("提示");
							dialog.button({name: '确认',callback: function(){}});
							$(".aui_titleBar").show();
	           		}
				});
			}
		});

	//验证数据
	function checkForm(){
			//保存常用乘客超出系统限制数量
			var count = $(".adult:visible,.child:visible").length;
			
			var size = "";
			var amount = 0;
			/*for(var i =1 ; i<=count;i++){
				if($(".savePax_"+i).attr("checked")=='checked'||$(".savePax_"+i).prop("checked")==true){
					if(checkCookiePax($("#user_name_"+i).val(),$("#ticket_type_"+i).val(),$("#user_ids_"+i).val())){
						amount +=1;
					}
				}
			}
			
			if(cookieRealValue!=null && cookieRealValue!=undefined){
				size = cookieRealValue.length;
				if(count>10-size){
					if(amount>10-size){
						dialogAlter("系统最多储存10名常用乘客信息，请先删除"+(amount+size-10)+"名常用乘客");
						//return false;
						return;
					}		
				}
			}*/
			//alert("乘客数：" + count);
			if(count <= 0) {
				$("#tip").attr("class", "passenger_tip_block").html("请选择至少一名乘客！")
				return;
			}
			var isValid=true;
			$(":input").each(function(){
				//alert("2934");
				if($.trim($(this).val())==""){
					////$(this).focus();
					isValid=false;
					showErrMsg($(this).attr("id"), "110px", "请输入"+$(this).attr("title")+"！");
					//return false;
					//alert("1");
					return;
				}else if($(this).attr("title")=="姓名" 
					&& !checkName($.trim($(this).val()))){
					//$(this).focus();
					isValid=false;
					showErrMsg($(this).attr("id"), "130px", "请填写正确的姓名！");
					//return false;
					//alert("2");
					return;
				}
				/*else if($(this).attr("title")=="证件号码" 
					&& $(this).parents("tr").find(".ids_type_select").val()=="2"
					&& !valiIdCard($.trim($(this).val()))){
					$(this).focus();
					isValid=false;
					showErrMsg($(this).attr("id"), "180px", "请填写正确的二代身份证号！");
					//return false;
					return;
				}*/
				else{
					hideErrMsg($(this).attr("id"));
				}
			});
			
			/*if(!isValid){
				alert("3")
				return false;
			}
			*/
			
			if($.trim($("#link_phone").val())==""){
				$("#link_phone").focus();
				alert("请填写手机！");
				return false;
			}else if(!/^1(3[\d]|4[57]|5[012356789]|8[012356789])\d{8}$/g.test($.trim($("#link_phone").val()))){
				$("#link_phone").focus();
				alert("请填写正确的手机号！");
				return false;
			}else{
				hideErrMsg("link_phone");
			}
			
			//儿童不能单独乘车
			var child=0,adult=0,count=0;
			$(".ticket_type_select:visible").each(function(){
				if($(this).val()=="0"){
					adult++;
				}else if($(this).val()=="1"){
					child++;
				}
				count++;
			});
			//alert(adult);
			//alert(child);
			//alert("儿童数目：" + child);
			//alert("成人数目:" + adult);
			if(child>0 && adult==0){
				//消息框
				var dialog = art.dialog({
					lock: true,
					fixed: true,
					left: '50%',
					top: '50%',
				    title: '提示',
				    okVal: '确认',
				    icon: "/images/warning.png",
				    content: '儿童乘车需要成人陪同，请添加成人乘客！',
				    ok: function(){}
				});
				return false;
			}
			
			//姓名不能重复
			var isNameDup=false;
			var idNameArray=new Array('1');
			$(".adult:visible").each(function(){
				var user_name = $.trim($(this).find(".user_name_text").val());
				if($.inArray(user_name, idNameArray)==-1){
					idNameArray.push(user_name);
				}else{
					isNameDup=true;
					return false;
				}
			});
			//姓名重复
			if(isNameDup){
				//消息框
				var dialog = art.dialog({
					lock: true,
					fixed: true,
					left: '50%',
					top: '50%',
				    title: '提示',
				    okVal: '确认',
				    icon: "/images/warning.png",
				    content: '乘客姓名不能重复，请修改！',
				    ok: function(){}
				});
				return false;
			}
			
			//成人证件号不能重复
			var isDup=false;
			var idCardArray=new Array('1');
			$(".adult:visible").each(function(){
				if($(this).find(".ticket_type_select").val()=='0'){//成人
					var idcard = $.trim($(this).find(".idcard_text").val());
					if($.inArray(idcard, idCardArray)==-1){
						idCardArray.push(idcard);
					}else{
						isDup=true;
						return false;
					}
				}
			});
			//证件号重复
			if(isDup){
				//消息框
				var dialog = art.dialog({
					lock: true,
					fixed: true,
					left: '50%',
					top: '50%',
				    title: '提示',
				    okVal: '确认',
				    icon: "/images/warning.png",
				    content: '成人乘客证件号不能重复，请修改！',
				    ok: function(){}
				});
				return false;
			}

			//保险发票
			if($("#chk_fp:visible").length>0 && $("#chk_fp").attr("checked")){
				if($.trim($("#fp_receiver").val())==""){
					showErrMsg("fp_receiver", "110px", "请填写收件人！");
					return false;
				}else if(!checkName($.trim($("#fp_receiver").val()))){
					showErrMsg("fp_receiver", "150px", "请填写正确的收件人！");
					return false;
				}else{
					hideErrMsg("fp_receiver");
				}
				if($.trim($("#fp_phone").val())==""){
					showErrMsg("fp_phone", "110px", "请填写手机！");
					return false;
				}else if(!/^1(3[\d]|4[57]|5[012356789]|8[012356789])\d{8}$/g.test($.trim($("#fp_phone").val()))){
					showErrMsg("fp_phone", "150px", "请填写正确的手机号！");
					return false;
				}else{
					hideErrMsg("fp_phone");
				}
				if($.trim($("#fp_zip_code").val())==""){
					showErrMsg("fp_zip_code", "110px", "请填写邮编！");
					return false;
				}else if(!checkZipCode($.trim($("#fp_zip_code").val()))){
					showErrMsg("fp_zip_code", "150px", "请填写正确的邮编！");
					return false;
				}else{
					hideErrMsg("fp_zip_code");
				}
				if($.trim($("#fp_address").val())=="" || $.trim($("#fp_address").val())=="请填写可以收件的真实地址（包括省市区）"){
					showErrMsg("fp_address", "110px", "请填写地址！");
					return false;
				}else{
					hideErrMsg("fp_address");
				}
			}
			
			//同意购买保险
			if($("#bx_confirm:visible").length>0 && !$("#chk_bxconfirm").attr("checked")){
				showErrMsg("chk_bxconfirm", "150px", "请勾选确认购买保险！");
				return false;
			}else{
				hideErrMsg("chk_bxconfirm");
			}

		return true;	
		
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
	        /**
	        if($.inArray(val.trim(),forbidArr)>=0){
	            return false;
	        }**/
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
</script>
	</head>

	<body>
		<form id="trainForm" action="/order/createOrder.jhtml" method="post">
			<input type="hidden" id="openID" name="openID" value="${openID}" />
			<input type="hidden" id="user_id" name="user_id"
				value="${loginUser.user_id}" />
			<input type="hidden" id="user_phone" name="user_phone"
				value="${loginUser.user_phone}" />
			<input type="hidden" id="baoxian" name="baoxian" value="" />
			<input type="hidden" id=fpNeed name="fpNeed" value="" />
			<input type="hidden" id="fp_receiver" name="fp_receiver" value="" />
			<input type="hidden" id="fp_phone" name="fp_phone" value="" />
			<input type="hidden" id="fp_address" name=fp_address value="" />
			<input type="hidden" name="fp_zip_code" id="fp_zip_code" value="" />
			<input type="hidden" name="product_id" id="product_id" value="" />

			<input type="hidden" id="train_no" name="train_no" value="" />
			<input type="hidden" id=from_city name="from_city" value="" />
			<input type="hidden" id="to_city" name="to_city" value="" />
			<input type="hidden" id="from_time" name="from_time" value="" />
			<input type="hidden" id="to_time" name="to_time" value="" />
			<input type="hidden" name="travelTime" id="travelTime" value="" />
			<input type="hidden" name="danjia" id="danjia" value="" />
			<input type="hidden" name="seat_type" id="seat_type" value="" />
			<input type="hidden" name="bx_pay_money" id="bx_pay_money" value="" />
			<input type="hidden" name="out_ticket_type" id="out_ticket_type"
				value="11" />
			<input type="hidden" name="ps_pay_money" value="20" />
			<input type="hidden" name="wz_ext" id="wz_ext" />
			<input type="hidden" name="location" id="location" value="" />
			<div>

				<!-- start -->
				<div class="wrap">
					<header id="bar">
					<a href="javascript:window.history.back();" class="m19e_ret"></a>
					<h1>
						填写订单
					</h1>
					</header>
					<section id="order_main">

					<div class="order_con">
						<!-- 车票信息 -->
						<dl class="order_item">
							<!--<dt> <a href="" class="order_ticket_s">取票退票说明</a> </dt>-->
							<dd style="line-height: 30px">

								<table style="width: 100%; margin: 0 0 0 0; padding: 0 0 0 0;">
									<tr style="background: #0f63b8; height: 30px; width: 100%;">
										<td style="width: 10px;">
											&nbsp;
										</td>
										<td style="font-size: 24px; color: #FFFAFA; width: 90px;"
											id="trainCode"></td>
										<td colspan="2" align="right"
											style="font-size: 20px; color: #FFFAFA; width: 70%;"
											id="travel_time"></td>
										<td style="width: 10px;">
											&nbsp;
										</td>
									</tr>
									<tr>
										<td style="width: 10px;"></td>
										<td class="font_18 b" id="startCity"></td>
										<td></td>
										<td rowspan="2" class="icon">
											<span class="from_to_icon"></span>
										</td>
										<td class="font_18 b right" id="endCity"></td>
										<td style="width: 10px"></td>
									</tr>
									<tr style="line-height: 20px;">
										<td style="width: 10px;"></td>
										<td class="text_item" id="startTime"></td>
										<td></td>
										<td class="text_item right" id="endTime"></td>
										<td style="width: 10px;"></td>
									</tr>
									<tr style="line-height: 20px; border-bottom: 1px solid #C0C0C0">
										<td style="width: 10px;"></td>
										<td class="text_item" id="seatType"></td>
										<td class="text_item"></td>
										<td style="float: right;" class="text_item right"
											id="seatPrice"></td>
										<td style="width: 10px;"></td>
									</tr>
								</table>

								<!--取退票说明 start-->
								<div class="tickets-intro" onclick="showdiv('intro')">
									<h4>
										取票、退票说明
										<br />
										<span class="slide-down"></span>
									</h4>
									<!-- <h4>取票、退票说明<br /><span class="slide-up"></span></h4> -->
									<div class=intro-con id="intro" name="intro"
										style="display: none;">
										<section>
										<h5>
											取票说明：
										</h5>
										<p style="color: #888; font-size: 12px;">
											凭购票时的有效证件和电子订单号，可在全国任意火车站或代售点取票。代售点收取代售费5元/张，另外车站售票窗口取异地票，火车站将收取代售费5元一张。
										</p>
										<h5>
											退票说明：
										</h5>
										<p style="color: #888; font-size: 12px;">
											自取票在线退票时间：09:00-20:00（其他时间请去火车站办理）在线退票要求：根据铁路局退票规定，发车前4小时，且未取票的订单，19旅行可以代为受理。
										</p>
										<h5>
											退票手续费：
										</h5>
										<p style="color: #888; font-size: 12px;">
											即日起铁路局将对每张车票按梯次收取：开车前48小时以上，手续费5%；开车前24-48小时之间，手续费10%；开车前24小时内，手续费20%；最终退款以铁路局实退为准。
										</p>
										</section>
									</div>
								</div>
								<!--取退票说明 end-->

							</dd>
						</dl>
						<!-- 乘客信息 -->
						<dl class="order_item order_infor">
							<!-- <dt>选择乘客<a href="javascript:void(0);" onclick="adddiv()" id="add_person" class="passenger_choose text_item">&nbsp;&nbsp;&nbsp;</a></dt> -->
							<dt>
								乘客信息
								<a href="javascript:void(0);" onclick="adddiv()" id="add_person"
									class="passenger_choose text_item">&nbsp;&nbsp;&nbsp;</a>
							</dt>
							<dd>

								<div id="tip"></div>
								<div id="orderInfo" style="display: block;">
									<table style="width: 100%; margin: 0 0 0 0; padding: 0 0 0 0;"
										id="train_ticket_list">
										<tr class="adult">
											<td>
												<table
													style="width: 100%; margin: 0 0 0 0; padding: 0 0 0 0;">
													<tr style="border-bottom: 1px solid #C0C0C0">
														<td class="text_item" style="color: black;">
															购票类型
															<input type="hidden" id="index_source_id"
																value="index_source" />
														</td>
														<td class="text_item" style="text-align: right;">
															<input type="hidden"
																name="bookDetailInfoList[0].ticket_type"
																id="ticket_type_1" value="0" />
															第
															<span class="indexTr">1</span>位&nbsp;&nbsp;成人票
															<a href="javascript:void(0);" id="passenger_delete"
																class="unableAdd">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>&nbsp;&nbsp;




















														
													</tr>

													<tr style="border-bottom: 1px solid #C0C0C0">
														<td class="text_item" style="color: black;">
															乘车人姓名
														</td>
														<td class="text_item">
															<input type="text" name="bookDetailInfoList[0].user_name"
																id="user_name_1"
																class="pass_ziti pass_inp text_item user_name_text"
																autocomplete="off" placeholder="请填写真实姓名以免取不出票"
																value="${bookDetailInfoList[0].user_name}" />
														</td>
													</tr>
													<tr>
														<td class="text_item" style="color: black;">
															<input type="hidden"
																name="bookDetailInfoList[0].ids_type" id="ids_type_1"
																value="2" />
															二代身份证
														</td>
														<td class="text_item">
															<input type="text" name="bookDetailInfoList[0].user_ids"
																id="user_ids_1" class="pass_ziti pass_inp text_item"
																autocomplete="off" placeholder="请填写证件号码"
																value="${bookDetailInfoList[0].user_ids}" />
														</td>
													</tr>
												</table>
											</td>
										</tr>

									</table>
								</div>
								<div
									style='background: #F2F2F2; height: 8px; width: 120%; display: block;'>
									&nbsp;
								</div>
								<div>
									<!--弹层@证件类型-->
									<article class="zjlx" style="display:none">
									<div class="shadow"></div>
									<dl class="passenger_popup">
										<dt>
											选择证件类型
											<a href="javascript:void(0);" class="close"
												onclick="hideZj()"></a>
										</dt>
										<dd>
											<a href="javascript:void(0);" class="zjlxchildren">二代身份证</a>
										</dd>
										<dd>
											<a href="javascript:void(0);" class="zjlxchildren">台湾通行证</a>
										</dd>
										<dd>
											<a href="javascript:void(0);" class="zjlxchildren">港澳通行证</a>
										</dd>
										<dd>
											<a href="javascript:void(0);" class="zjlxchildren">护照</a>
										</dd>
									</dl>
									</article>
									<p
										style="font-size: 16px; line-height: 20px; padding-top: 8px;">
										套餐类型：
										<span id="meal_insurance" class="org"
											onclick="javascript:openwindow();">交通意外险&nbsp;￥20*1</span>
									</p>
								</div>
							</dd>

						</dl>

						<!-- 保险信息
				<dl class="order_item">
					<dt line-height="30px">
						<p font-size="16px">套餐类型：
							<span id="meal_insurance" class="org">交通意外险&nbsp;￥20*1<a href="/pages/book/orderMeal.jsp" id="add_insurance" class="insurance_choose text_item">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></span>
							<span id="meal_insurance" class="org" onclick="savePassengers();window.location='/pages/book/orderMeal.jsp';">交通意外险&nbsp;￥20*1</span>
						</p>
					</dt>
					<hr/>
				</dl>
				 -->
						<!-- 车站自提 -->
						<div class="order_item"
							style="margin-bottom: 90px; margin-top: 20px;">
							<div
								style="font-size: 16px; display: inline-block; background: #fff; width: 100%;">
								<span
									style="width: 25%; float: left; padding-top: 5px; padding-left: 10px;">联系人手机</span>
								<div
									style="margin-left: 3px; background-color: #fff; float: right; width: 65%;">
									<input type="text" class="pass_ziti pass_inp text_item"
										autocomplete="off" placeholder="用于接收购票通知短信" name="link_phone"
										id="link_phone">
								</div>
							</div>

							<!-- 
					<br/><br/>
					<p id="bx_confirm" style="font-size:14px;"><span class="display_mid"><input type="checkbox" id="chk_bxconfirm" checked="checked"/></span>阅读并同意<span style="color:#0081cc;cursor:pointer;line-height:24px;" onclick="window.open('/pages/common/protocol.html');">19e旅行服务协议</span>和<span style="color:#0081cc;cursor:pointer;line-height:24px;" onclick="window.open('/pages/common/insurance.html');">保险说明</span></p>
					</dt>
					 -->
						</div>

					</div>

					<div class="oz"
						style="position: fixed; bottom: 42px; background-color: #fff;">
						<input type="checkbox" checked="checked" id="chk_bxconfirm">
						<span class="fl">阅读并同意<span
							style="color: #0081cc; cursor: pointer; line-height: 24px;"
							onclick="window.open('/pages/common/protocol.html');">19e旅行服务协议</span>和<span
							style="color: #0081cc; cursor: pointer; line-height: 24px;"
							onclick="window.open('/pages/common/insurance.html');">保险说明</span>
						</span>
					</div>
					<!-- 订单合计 start -->
					<div class="order-sum oz">
						<div class="order-box">
							<span class="all-money">合计：<b id="shouldPayMoney"></b> </span>
							<input class="btn submit-order" type="button" value="提交订单"
								id="btnSubmit"
								style="color: #fff; font-weight: bolder; border: none;">

						</div>
					</div>
					<!-- 订单合计 end -->
					<!-- 
			<div class="order_foot">
				<div class="order_d" style="background:#d13b00;">
					<span class="order_pay" id="shouldPayMoney"></span>
				</div>
				<div class="order_d" style="background:#ff7200;">
					<span class="order_btn"><input type="button" value="提交订单" id="btnSubmit" class="order_submit" /></span>
				</div>
			</div>	
			 -->
					</section>
				</div>
				<!-- end -->


			</div>
		</form>


		<script>
/**
	var db = openDatabase("bookinfo", "1.0", "预订信息", 1024 * 1024);
	if (!db) {
    	alert("数据库创建失败！");
	} else {
   	 	alert("数据库创建成功！");
	}
	//this.createTable=function() {
  	db.transaction( function(tx) { 
    	tx.executeSql(
       		 "create table if not exists passengers (name TEXT, user_id TEXT, ids_type TEXT, primary key(name, user_id))", 
        	[], 
        	function(tx,result){ alert('创建passengers表成功'); }, 
        	function(tx, error){ alert('创建passengers表失败:' + error.message); }
        );
 	});
 	
 	db.transaction( function(tx) { 
    	tx.executeSql(
       		 "create table if not exists linkers (name TEXT, phone TEXT, primary key(name, phone))", 
        	[], 
        	function(tx,result){ alert('创建passengers表成功'); }, 
        	function(tx, error){ alert('创建passengers表失败:' + error.message); }
        );
 	});
 	
	//}
	
	function savePassengers() {
		var table = document.getElementById("train_ticket_list");    
		var count = table.rows.length;
		alert(count);
		for(var i = 1;i <= count;i++ ){
			var name = $.trim(document.getElementById("user_name_" + i).value);
			var user_id = $.trim(document.getElementById("user_ids_" + i).value);
			var ids_type= $.trim(document.getElementById("ids_type_" + i).value);
			if(("" == name) || ( "" == user_id) || ("" == ids_type)){
				break;
			}
			db.transaction( function(tx) { 
		    	tx.executeSql(
		       		"insert  into  passengers (name, user_id, ids_type) values('" + name + "', '"+ user_id + "', '"+ ids_type + "')"
       			 );
		        
		 	});
	 	}
	 	var linker_name= document.getElementById("link_name").value;
	 	var linker_phone = document.getElementById("link_phone").value;
	 	//alert( "insert  into  linkers (name, phone) values('" + linker_name + "', '" + linker_phone + "')");
	 	if((""!= linker_name) && ("" !=linker_phone)) {
	 		db.transaction(function(tx){
			 	tx.executeSql(
			     	"select name, phone from  linkers",
	        		[],
	        		function (tx, result) { 
	        			if(result.rows.length == 0){
	        				db.transaction( function(tx) { 
						    	tx.executeSql(
						       		 "insert  into  linkers (name, phone) values('" + linker_name + "', '" + linker_phone + "')"
				       			 );
						 	});
	        			}else {
	        				db.transaction( function(tx) { 
						    	tx.executeSql(
						       		 "update  linkers set name='" + linker_name + "', phone='" + linker_phone + "' where name='" + result.rows.item(0)['name'] + "' and phone= '" + result.rows.item(0)['phone'] + "'"
				       			 );
						 	});
	        			}
	        		},
	       			function (tx, error) { alert('加载数据失败: ' + error.message); } 
	       		); 
			 });
	 		
	 	}
	 	
	}
	
	function checkPassenger(count) {
		var name = document.getElementById("user_name_" + i).value;
		var user_id = document.getElementById("user_ids_" + i).value;
		var ids_type= document.getElementById("ids_type_" + i).value;
		
		if("" == name){
			//alert("请输入姓名");
			return;
		}
		id = id.toUpperCase();
		var len, re; 
		len = id.length; 
		
		if (len != 18) {
			//alert("身份证长度不正确，请重新输入！");
			return;
		}
		
		var checkFlag = new clsIDCard(id);
     	if(!checkFlag.IsValid()){
     		//alert("请输入合法的身份证号！");
           	return false;
      	}
      	savePassenger(name, user_id, ids_type);
	}
	
	function savePassenger(name, user_id, ids_type) {
		
		db.transaction( function(tx) { 
		   	tx.executeSql(
		     	"insert  into  passengers (name, user_id, ids_type) values('" + name + "', '"+ user_id + "', '"+ ids_type + "')"
        		//,[name, user_id, ids_type],
        		//function () { alert('添加数据成功'); },
       			//function (tx, error) { alert('添加数据失败: ' + error.message); } 
       		);     
		 });
	}
	
	function deletePassenger(name, user_ids) {
		alert(name, user_ids);
		db.transaction( function(tx) { 
		    	tx.executeSql(
		       		 "delete  from  passengers where name='" + name + "' and user_id='" + user_ids + "'"
       			 );
		        
		 });
	}
	
	$().ready(function() { //页面加载时执行
		var table = document.getElementById("train_ticket_list");       
		var count = (table.rows.length+1)/2;//行数   
		//document.getElementById("index_source").value = count;
		//var index_source = document.getElementById("index_source_id").value;
		if(count==1){
			$("#passenger_delete").attr("class","unableAdd");
		}

		
		localStorage.setItem("linkersStatus", "1");
		
		db.transaction( function(tx) { 
		   	tx.executeSql(
		     	"select name, user_id, ids_type from  passengers",
        		[],
        		function (tx, result) { 
        			alert("web sql中存储数据有" + result.rows.length);
        			for(var j = 0;j < result.rows.length;j++){
        				var passenger_name = result.rows.item(j)['name'];
        				var user_id = result.rows.item(j)['user_id'];
        				var ids_type = result.rows.item(j)['ids_type'];
        				if(j == 0){
        					document.getElementById("user_name_1").value=passenger_name;
        					document.getElementById("user_ids_1").value=user_id;
        				}else {
        					adddiv(passenger_name, user_id, ids_type);
        				}
        			}
        		},
       			function (tx, error) { alert('加载数据失败: ' + error.message); } 
       		);     
		 });
		 
		 db.transaction(function(tx){
		 	tx.executeSql(
		     	"select name, phone from  linkers",
        		[],
        		function (tx, result) { 
        			if(result.rows.length != 0){
        				var linker_name = result.rows.item(0)['name'];
        				var linker_phone = result.rows.item(0)['phone'];
        				document.getElementById("link_name").value=linker_name;
        				document.getElementById("link_phone").value=linker_phone;
        			}
        		},
       			function (tx, error) { alert('加载数据失败: ' + error.message); } 
       		); 
		 });
		 
		 
	});  
	*/
</script>
	</body>
</html>
