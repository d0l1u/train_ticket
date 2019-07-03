<!doctype html>
<html>
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<title>掌上19e-火车票</title>
<meta name="keyword" content="19e,便民服务,数字便民,移动客户端,手机客户端,话费充值,手机充值,游戏充值,QQ充值,机票预订,水费,电费,煤气费,取暖费,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
<meta name="description" content="提供19e数字便民移动终端下载,随时随地办理便民业务,支持移动客户端话费充值,手机充值,游戏充值,QQ充值,机票预订,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
<link rel="stylesheet" type="text/css" href="/css/base.css">
<script src="/js/idCard.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon" />
<style type="text/css">
	html{-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;}
	.screen {width:480px; height:700px; margin:0 auto;}
</style>
<script>
	
	function addNewPasser(){
		
		var days = 30;
		//alert("jinrujs");
		var name=$.trim($("#name").val());
		var id=$.trim($("#id").val());
		var idType=$.trim($("#idType").val());
		var ticketType=$.trim($("#ticketType").val());
		//alert("name:::" + name);
		//alert("id:::" + id);
		if("" == name){
			alert("请输入姓名");
			return;
		}
		id = id.toUpperCase();
		var len, re; 
		len = id.length; 
		
		if (len != 18) {
			alert("身份证长度不正确，请重新输入！");
			return;
		}
		
		var checkFlag = new clsIDCard(id);
     	if(!checkFlag.IsValid()){
     		alert("请输入合法的身份证号！");
           	return false;
      	}

		
		//var passInfo = getCookies("passenger");
		//var passengerInfo = new Passenger(name,id, idType, ticketType).passengerInfo();
		var passenger  = 
	  	{ 
	  		"name" : name, 
	  		"id" : id,
	  		"idType" : idType,
	  		"ticketType" : ticketType,
	  	}
		var memory = window.localStorage || (window.UserDataStorage && UserDataStorage()) || new cookieStorage();
	
		var storage_data = memory.getItem("passenger_data");
		
		//var passenger_data = JSON.parse(memory.getItem("passenger_data"));
		//alert(JSON.stringify(passenger_data));
		var spassenger_data = memory.getItem("spassenger");
		
		if(spassenger_data != ""){
			var spassenger = JSON.parse(spassenger_data);
			var passenger_array;
			if(storage_data!=null){
				passenger_array = eval(JSON.parse(storage_data));
			}else {
				passenger_array = new Array();
			}
			for(var i = 0;i < passenger_array.length;i++){
				var obj = passenger_array[i];
				//alert(spassenger.name + "====" + obj.name);
				//alert(spassenger.id + "====" + obj.id);
				//alert(spassenger.idType + "====" + obj.idType);
				//alert(spassenger.ticketType + "====" + obj.ticketType);
				if(spassenger.name == $.trim(obj.name) && (spassenger.id == $.trim(obj.id)) && (spassenger.idType== $.trim(obj.idType)) && (spassenger.ticketType == $.trim(obj.ticketType))) {
					passenger_array.splice(i,1,passenger);
					break;
				}else{
					//passenger_array.push(obj);
				}
			}
			memory.setItem("passenger_data", JSON.stringify(passenger_array));
			memory.setItem("spassenger", "");
			window.location="/pages/book/oftenLinkers.jsp";
			return;
		}
		
		var passenger_array;
		if(storage_data!=null){
			passenger_array = eval(JSON.parse(storage_data));
		}else {
			passenger_array = new Array();
		}
		//alert("passenger_array:"+passenger_array);
		if(storage_data != "" && (storage_data != null) && (passenger_array != NaN)){
			var flag = false;
			//alert(passenger_array);
			for(var i = 0;i < passenger_array.length;i++){
				var obj = passenger_array[i];
			//alert(obj);
				if(passenger.name == $.trim(obj.name) && (passenger.id == $.trim(obj.id)) && (passenger.idType== $.trim(obj.idType)) && (passenger.ticketType == $.trim(obj.ticketType))) {
					//passenger_array.splice(i,1,passenger);
					flag = true;
					alert("常用联系人中存在" + passenger.name);
					break;
				}
			}
			if(flag == false){
				passenger_array.push(passenger);
			}
	
		}else{
			//alert(passenger_array);
			//passenger_array = new Array();
			passenger_array.push(passenger);
			
		}
		/*for(var i = 0;i < passenger_array.length;i++){
				alert(passenger_array[i].name);
			}*/
		memory.setItem("passenger_data", JSON.stringify(passenger_array));
		window.location="/pages/book/oftenLinkers.jsp";
		
	}
	
	function getCookies(name) {
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
    if(arr=document.cookie.match(reg))
        return unescape(arr[2]); 
    else 
        return null; 
	}
	function Passenger(name, id, idType,ticketType){
		this.name=name;
		this.id=id;
		this.idType=idType;
		this.ticketType = ticketType;
		if (typeof passengerInfo != "function" ){ 
			Passenger.prototype.passengerInfo= function(){ 
				return "{name:" + name+",id:" + id + ",idType:" + idType + ",ticketType:" + ticketType + "}"; 
			}; 
		}
	}
</script>
</head>
<body>
<!-- <div class="screen"> -->

	<!-- start -->
	<div class="wrap1" style="background:#fff;">
		<header id="bar">
			<a href="javascript:window.history.back();" class="m19e_ret"></a>
			<h1 id="header">新增乘客</h1>
		</header>
		<section id="order_main">
			<div class="order_con">
				
				<dl class="order_item">
				
					<dd>
						<input type="text" class="pass_inp" name="name" id="name" autocomplete="off" placeholder="姓名（必填）">
						<select name="idType" id="idType" class="pass_sel" disabled="true">
							<option value="3">港澳通行证</option>
							<option value="4">台湾通行证</option>
							<option value="5">护照</option>
							<option selected="selected" value="2">二代身份证</option>
						</select>	
						<select name="ticketType" id="ticketType" class="pass_sel">
							<option value="0">成人票</option>
							<option value="1">儿童票</option>
						</select>	
						<input type="text" class="pass_inp" autocomplete="off" name="id" id="id" placeholder="证件号（必填）">
					</dd>
				</dl>

			</div>
			<br/>
				<div class="ticket_ser_w">
					
					<input type="button" value="确定" class="ticket_add" onclick="addNewPasser();">
				</div>
		
		</section>
	</div>
	<!-- end -->

<script>  
    window.onload=function(){
    	var memory = window.localStorage || (window.UserDataStorage && UserDataStorage()) || new cookieStorage();
    	var passengerStr = memory.getItem("spassenger");
    	if(passengerStr != ""){
			var spassenger = JSON.parse(passengerStr);
	    	document.getElementById("name").value = spassenger.name;   
	   		document.getElementById("id").value = spassenger.id;   
	  	 	//document.getElementById("idType").value = spassenger.idType;
	   		document.getElementById("ticketType").value = spassenger.ticketType;
	   		document.getElementById("header").innerText = "修改联系人信息";
   		}
    };
   
  </script>
<!-- </div> -->	
</body>
</html>		
