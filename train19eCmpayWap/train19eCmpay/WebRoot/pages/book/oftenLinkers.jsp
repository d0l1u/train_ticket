<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta name="viewport"
			content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
		<meta http-equiv="content-type" content="text/html;charset=utf-8">
		<title>掌上19e-火车票</title>
		<meta name="keyword"
			content="19e,便民服务,数字便民,移动客户端,手机客户端,话费充值,手机充值,游戏充值,QQ充值,机票预订,水费,电费,煤气费,取暖费,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
		<meta name="description"
			content="提供19e数字便民移动终端下载,随时随地办理便民业务,支持移动客户端话费充值,手机充值,游戏充值,QQ充值,机票预订,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
		<link rel="stylesheet" type="text/css" href="/css/base.css">
		<link rel="shortcut icon" href="/images/favicon.ico"
			type="image/x-icon" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<style type="text/css">
.screen {
	width: 480px;
	height: 700px;
	margin: 0 auto;
}

.order_con {
	padding: 0 0;
}

.order_item dd {
	line-height: 30px;
	padding: 10px 15px;
}

.text_item {
	font-size: 16px;
	color: #888;
}

.pass_inp,.pass_sel {
	width: 240px;
	height: 35px;
	margin: 3px 0;
	display: block;
	border-radius: 3px;
	padding-left: 10px;
}

.pass_sel {
	width: 130px;
	height: 35px;
	padding-left: 10px;
}

.pass_ziti {
	width: 200px;
}

.passenger_edit {
	position: relative;			
	background: url(/images/edit-icon.png)  no-repeat center center;
	background-size:90%;
}

.passenger_del {
	position: absolute;
	right: 0;
	top: 0;
	background: url(/images/cancel-icon.png) no-repeat center center;
	text-indent: -9999px;
}

.passenger_delete {
	
	margin-top:5px;
	position: relative;
	
	background: url(/images/cancel-icon.png) no-repeat center center;
	background-size:90%;
	
}

.passenger_add {
	position: absolute;
	right: 25px;
	top: 0;
	padding-right: 30px;
	background: url(/images/pas_add_bg.gif) no-repeat right center;
	color: #0f63b8;
}
</style>
		<script type="text/javascript">
	var userId = "${agentId}"+"_hcp";
	var cookieValue = ""; 	//编码后的cookie值
	var cookieRealValue	= "";	//cookieJson解析后需要使用的数据
	var memory = window.localStorage || (window.UserDataStorage && UserDataStorage()) || new cookieStorage();
	var param_product_id = "";	//传递给后台的保险ID值
	var storageofpassengers = memory.getItem("passengers");
	window.onload = function showdiv() {   
		var linkersStatus = memory.getItem("linkersStatus");   
		var passenger_data = JSON.parse(memory.getItem("passenger_data"));
		//alert(JSON.stringify(passenger_data));
		var mainobject = '';
		if(passenger_data == null || passenger_data == "" || passenger_data ==NaN){
			mainobject = "";
		}else {
		
			for(var i = 0;i < passenger_data.length;i++){
					 	mainobject = mainobject + '<tr class="adult" >'+
					 		'<td>'+
					 	  '<table style="width:100%;margin:0 0 0 0;padding:0 0 0 0;">'+
					 		//'<table>'
							 	'<tr>';
						if(storageofpassengers != null && storageofpassengers != "" && storageofpassengers!=NaN){
							//alert(storageofpassengers);
							var passen_array = JSON.parse(storageofpassengers);
							var checked = false;
							for(var j = 0;j < passen_array.length;j++){
							//alert(passenger_data);
								if($.trim(passenger_data[i].name) == $.trim(passen_array[j].name) && $.trim(passenger_data[i].id) == $.trim(passen_array[j].id) && passenger_data[i].ticketType == passen_array[j].ticketType){
									checked = true;
									break;
								}
							}
							if(checked == true){
								mainobject = mainobject + '<td style="line-height:60px;"><input type="hidden" value="index_source"/><input name="selectItem" type="checkbox" checked/></td>';
							}else {
								mainobject = mainobject + '<td style="line-height:60px;"><input type="hidden" value="index_source"/><input name="selectItem" type="checkbox"/></td>';
							}
						}
						else{
							mainobject = mainobject + '<td style="line-height:60px;"><input type="hidden" value="index_source"/><input name="selectItem" type="checkbox"/></td>';
						}
						//alert(mainobject);
						mainobject = mainobject + '<td class="text_item">'+
							 			'姓名：' + passenger_data[i].name+
							 		'</td>'+
							 		'<td class="text_item" >';
							 			if(passenger_data[i].ticketType=='0'){
							 				mainobject = mainobject + '成人票';
							 			}
							 			if(passenger_data[i].ticketType=='1'){
							 				mainobject = mainobject + '儿童票';
							 			}
									mainobject = mainobject + '</td>'+
							 		'<td><a href="javascript:void(0);" class="passenger_edit ">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a><br/>'+
							 						  '</td>'+
							 	'</tr>'+
							 	'<tr>'+
							 	
							 		'<td class="text_item" colspan="3">';
							 		if(passenger_data[i].idType == '2'){
							 			mainobject = mainobject + '身份证：' + passenger_data[i].id;
							 		}
							 		if(passenger_data[i].idType == '3'){
							 			mainobject = mainobject + '港澳通行证：' + passenger_data[i].id;
							 		}
							 		if(passenger_data[i].idType == '4'){
							 			mainobject = mainobject + '台湾通行证：' + passenger_data[i].id ;
							 		}
							 		if(passenger_data[i].idType == '5'){
							 			mainobject = mainobject + '护照：' + passenger_data[i].id;
							 		}
							 		mainobject = mainobject + '</td>'+
							 		'<td><a href="javascript:void(0);" class="passenger_delete ">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></td>'+
						 		'</tr><tr><td colspan=4><hr/></td></tr></table>';
		}
		}
		//alert(mainobject);
		$("#train_ticket_list").html(mainobject);
		if(linkersStatus == "1"){
			$(".m19e_ok").attr("class","m19e_ok");  //class="m19e_ok"
			$(".m19e_ok").attr("href","javascript:submitData()");//href="javascript:submitData()"
		}
		//alert($("#train_ticket_list").val());
		//alert
             //alert($("#" + divname).css("display")+divname );
             /*if ($("#" + divname).css("display") == "none") {       //显示div
                 $("#" + divname).slideDown(500);
              
                 SetCookie(divname, true);
                 
               //  if (document.cookie != "") alert(getCookie("youhui"));
             }
             else if ($("#" + divname).css("display") == "block") {       //隐藏div
             $("#" + divname).slideUp(500);
             SetCookie(divname, false);
             delcookie(divname);
            // alert(getCookie(divname));
              }
            else  {
                $("#" + divname).slideDown(500);
                SetCookie(divname, true);
                alert("执行出错");
              }*/
      }
	//添加通行成人
	function addPasser(){	
		/*var passenger_data = JSON.parse(memory.getItem("passenger_data"));
		alert(passenger_data)	
		//var count = $(".adult:visible,.child:visible").length;	
		var table = document.getElementById("train_ticket_list");        
		var count = table.rows.length;//行数   
	
		//if(count>=5){
			//dialogAlter("一个订单最多可代购5张票！");
			//return;
		//}
		var replaceStr = "bookDetailInfoList["+count+"]";		
		var html = $(".adult:visible").html().replace(/index_source/g, count+1);
		//alert($(".adult:visible").html());
		
		html = html.replace(/bookDetailInfoList_source/g, replaceStr);		
		//alert("html:"+html);
		$("<tr><td><hr/></td></tr><tr>" + html + "</tr>").css("background-color","")
			.appendTo("#train_ticket_list").show();
		if(count==4){
			$("#add_person").attr("class","unableAdd");
		}*/
		memory.setItem("spassenger", "");
		window.location="/pages/book/addNewPassenger.jsp";
	}
	
	//编辑常用联系人
	$(".passenger_edit").live("click", function(){
		var data = $.trim($(this).parents("td").text());
		//alert(data);
		var id = $.trim(data.slice(data.lastIndexOf("：") + 1));
		var ticketType = "";
		var name = "";
		var idType = "";
		if(data.indexOf("成人票") >=0){
			ticketType = "0";
			name=data.substring(data.indexOf("：") + 1, data.indexOf("成人票"));
		}
		if(data.indexOf("儿童票") >= 0){
			ticketType="1";
			name=data.substring(data.indexOf("：") + 1, data.indexOf("儿童票"));
		}

		if(data.indexOf("身份证") >= 0) {
			idType="2";
		}
		if(data.indexOf("港澳通行证") >= 0) {
			idType="3"
		}
		if(data.indexOf("台湾通行证") >= 0){
			idType="4";
		}
		if(data.indexOf("护照")>= 0){
			idType="5";
		}
		var spassenger = {
							"name":name,
							"id":id,
							"idType":idType,
							"ticketType":ticketType
						}
						memory.setItem("spassenger", JSON.stringify(spassenger));
		window.location="/pages/book/addNewPassenger.jsp";
	});
	
	//从localstorage删除存储的联系人
	function removePassenger(name, id, ticketType){
		//alert(name);
		//alert(id);
		//alert(ticketType);
		var passenger_data = JSON.parse(memory.getItem("passenger_data"));
		var passenger_array = eval(passenger_data);
		for(var i = 0;i < passenger_array.length;i++){
			var arr = passenger_array[i];
			if(arr.name.trim() != name || (arr.id.trim() != id) || (arr.ticketType.trim() != ticketType)){
				//passenger_data.push(arr);
			}else {
				//alert(JSON.stringify(arr));
				//if(confirm("确定要删除该联系人？")){
					//alert("已删除该联系人");
					passenger_array.splice(i, 1);
					break;
				//}
			}
		}
		memory.setItem("passenger_data", JSON.stringify(passenger_array));
		//showdiv();
		window.location.reload();
	}
	
	//删除乘客
		$(".passenger_delete").live("click", function(){
			var table = document.getElementById("train_ticket_list");  
			if(table.rows.length==0){
				return false;
			}
				//alert(table.rows.length); 	
			var name = "";
			var card_num = "";	
			//$(this).parents("tr").css('background-color', 'red');	
		  $(this).parents("tr").parents("table").parents("tr").find(":text").each(function(){
				
				if($(this).attr("id").indexOf("name")>0){
					name = $("#"+$(this).attr("id")).val();
				//	alert(name);
				};
				if($(this).attr("id").indexOf("ids")>0){
					card_num = $("#"+$(this).attr("id")).val();
				};
			});
			//删除同行乘客的对应选框清空
			removePaxAttr(name,card_num);
			$(this).parents("tr").parents("table").parents("tr").prev().remove();
			var data = $(this).parents("tr").parents("table").parents("tr").text();
			//alert(data);
			var id = data.slice(data.lastIndexOf("：") + 1)
			var ticketType = "";
			var name = "";
			if(data.indexOf("成人票") >=0){
				ticketType = "0";
				name=data.substring(data.indexOf("：") + 1, data.indexOf("成人票"));
			}
			if(data.indexOf("儿童票") >= 0){
				ticketType="1";
				name=data.substring(data.indexOf("：") + 1, data.indexOf("儿童票"));
			}
			//alert(ticketType);
			//alert(name);
			
			removePassenger(name.trim(), id.trim(), ticketType);
		
		});
		
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
	    function hideErrMsg(id){
				$("#"+id+"_errMsg").remove();
		}
			
		function submitData(){
			//alert("submitData");
			var passenger_data = JSON.parse(memory.getItem("passenger_data"));
			var array = new Array();
			$("[type=checkbox]").each(function(index){
				//var passenger_data = JSON.parse(memory.getItem("passenger_data"));
				if($(this).attr("checked") == "checked"){
					var data = $(this).parents("td").text()
					//alert(data);
					var id = $.trim(data.slice(data.lastIndexOf("：") + 1));
					var ticketType = "";
					var name = "";
					var idType = "";
					if(data.indexOf("成人票") >=0){
						ticketType = "0";
						name=data.substring(data.indexOf("：") + 1, data.indexOf("成人票"));
					}
					if(data.indexOf("儿童票") >= 0){
						ticketType="1";
						name=$.trim(data.substring(data.indexOf("：") + 1, data.indexOf("儿童票")));
					}
					if(data.indexOf("身份证") >= 0) {
						idType="2"
					}
					if(data.indexOf("港澳通行证") >= 0) {
						idType="3"
					}
					if(data.indexOf("台湾通行证") >= 0){
						idType="4";
					}
					if(data.indexOf("护照")>= 0){
						idType="5";
					}
					var passenger = {
						"name":name,
						"id":id,
						"idType":idType,
						"ticketType":ticketType
					}
					alert(idType);
					array.push(passenger);
				}
				
			});
			var storageofpassengers = memory.getItem("passengers");
			//alert(JSON.stringify(array));
			memory.setItem("passengers", JSON.stringify(array));
			window.location="/pages/book/bookInfoInput.jsp";
			//alert(JSON.stringify(JSON.parse(memory.getItem("passengers"))))
		}
		
</script>
	</head>

	<body>

		<div>
			<!-- start -->
			<div class="wrap1">
				<header id="bar">
				<a href="javascript:window.history.back();" class="m19e_ret"></a>
				<h1>
					常用联系人
				</h1>
				<a class="m19e_ok"><h6>确定</h6> </a>
				</header>
				<section id="order_main">

				<div class="order_con">

					<!-- 乘客信息 -->
					<dl class="order_item order_infor">
						<dt onclick="addPasser()">
							新增乘客
							<a id="add_person" class="passenger_choose text_item">&nbsp;&nbsp;&nbsp;</a>
						</dt>
						<dd>
							<div id="orderInfo" style="display: block;">
								<table style="width: 100%; margin: 0 0 0 0; padding: 0 0 0 0;"
									id="train_ticket_list">
								</table>
							</div>

						</dd>
					</dl>



				</div>

				</section>
			</div>
			<!-- end -->


		</div>
	</body>
</html>
