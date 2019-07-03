<!doctype html>
<html>
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<meta charset="utf-8">
<title>掌上19e-火车票</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<meta name="keyword" content="19e,便民服务,数字便民,移动客户端,手机客户端,话费充值,手机充值,游戏充值,QQ充值,机票预订,水费,电费,煤气费,取暖费,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
<meta name="description" content="提供19e数字便民移动终端下载,随时随地办理便民业务,支持移动客户端话费充值,手机充值,游戏充值,QQ充值,机票预订,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
<meta http-equiv="pragma" content="no-cache">
<link rel="stylesheet" type="text/css" href="/css/base.css">
<link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/artDialog.js"></script>
<style type="text/css">
.screen {width:480px; margin:0 auto;}
.order_con {padding:0 0;}
.order_item dd {line-height:30px;}
td.icon{vertical-align:middle;align:center;width:30%;}
td.right{}
.text_item {font-size:14px;color:#888;}
.linkinput {font-size:16px;width:40%;}
.linktr{border:1px solid #cbcbcb;border-collapse:collapse;}
.input_hidden_border {font-size:14px;color:#888;width:70px;border:0;font-family:Microsoft YaHei, calibri, verdana;}
.pass_inp, .pass_sel {width:100%; height:35px; margin:3px 0; display:block; border-radius:3px;padding-left:10px;}
.pass_sel{ width:130px; height:35px;padding-left:10px;}
.pass_ziti{width:100%;}
.passenger_del {position:absolute; right:0; top:0; height:40px; width:30px; background:url(/images/cancel-icon.png) no-repeat center center; text-indent:-9999px;}
.passenger_delete{position:relative;  background:url(/images/cancel-icon.png) no-repeat center center;}
.passenger_add {position:absolute; right:25px; top:0; padding-right:30px; background:url(/images/add-btn.png) no-repeat right center; color:#0f63b8;}
.wrap {position:relative;}
.passenger_tip{padding:5px 5px 5px 5px;font-size:16px;color:#f00;display:none;}
.passenger_tip_block {padding:5px 5px 5px 5px;font-size:16px;color:red;display:block;}
</style>
<script type="text/javascript">

	var userId = "${agentId}"+"_hcp";
	var cookieValue = ""; 	//编码后的cookie值
	var cookieRealValue	= "";	//cookieJson解析后需要使用的数据
	var param_product_id = "";	//传递给后台的保险ID值
	
	$().ready(function() { //页面加载时执行
		localStorage.setItem("linkersStatus", "1");
		var baoxian = localStorage.getItem("baoxian");
		var fpNeed = localStorage.getItem("fpNeed");
		var fp_receiver = localStorage.getItem("fp_receiver");
		var fp_phone = localStorage.getItem("fp_phone");
		var fp_address = localStorage.getItem("fp_address");
		var fp_zip_code = localStorage.getItem("fp_zip_code");
		var link_name = localStorage.getItem("link_name");
		var link_phone = localStorage.getItem("link_phone");
		$("#link_phone").val(link_phone);
		$("#link_name").val(link_name);
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
		
		
		param_product_id = baoxian;
		document.getElementById('trainCode').innerHTML += ("<strong>"+localStorage.getItem("trainCode")+"</strong>");
		document.getElementById('travel_time').innerHTML += (localStorage.getItem("travelTime")+" "+localStorage.getItem("day"));
		document.getElementById('startCity').innerHTML += localStorage.getItem("startCity");
		document.getElementById('endCity').innerHTML += localStorage.getItem("endCity");
		document.getElementById('startTime').innerHTML += localStorage.getItem("startTime");
		document.getElementById('endTime').innerHTML += localStorage.getItem("endTime");
		document.getElementById('seatType').innerHTML += localStorage.getItem("seat_type");
		document.getElementById('seatPrice').innerHTML += ("￥"+localStorage.getItem("seat_price"));
		
		//var jsonStr = '[{"name":"李延华","id":"371525198505056727","idType":"2","ticketType":"0"},{"name":"王世刚","id":"220102196910211816","idType":"2","ticketType":"0"}]';
		var jsonStr = localStorage.getItem("passengers"); //alert(jsonStr);
		//alert(jsonStr);
		var myobj;
		if(jsonStr != null &&jsonStr != ""){
			myobj=eval('('+jsonStr+')'); 
		}else{
			myobj = new Array();
		}
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
		//alert(tableStr);
		$("#train_ticket_list").html(tableStr);   
		
		if( baoxian=="0" ){
			$("#meal_insurance").html("不购买保险<a href='/pages/book/orderMeal.jsp' id='add_insurance' class='insurance_choose text_item'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>");
			document.getElementById('shouldPayMoney').innerHTML += ("应付金额:<strong>￥"+(Number(localStorage.getItem("seat_price"))*myobj.length)+"</strong>"); 
			document.getElementById("product_id").value = "PT000000005";
			document.getElementById("bx_pay_money").value = 0;
		}else{
			//有几个乘客，买几份保险
			$("#meal_insurance").html('交通意外险&nbsp;￥20*'+myobj.length+'<a href="/pages/book/orderMeal.jsp" id="add_insurance" class="insurance_choose text_item">&nbsp;&nbsp;&nbsp;</a>');
			document.getElementById('shouldPayMoney').innerHTML += ("应付金额:<strong>￥"+((Number(localStorage.getItem("seat_price"))+20)*myobj.length)+"</strong>");
			document.getElementById("product_id").value = "PT000000001";
			document.getElementById("bx_pay_money").value = 20*myobj.length;
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
              }
             }
	//添加通行成人
	function adddiv(){		
		//var count = $(".adult:visible,.child:visible").length;	
		var table = document.getElementById("train_ticket_list");        
		var count = table.rows.length;//行数   
	
		if(count>=5){
			//dialogAlter("一个订单最多可代购5张票！");
			return;
		}
		var replaceStr = "bookDetailInfoList["+count+"]";		
		var html = $(".adult:visible").html().replace(/index_source/g, count+1);
		//alert($(".adult:visible").html());
		
		html = html.replace(/bookDetailInfoList_source/g, replaceStr);		
		//alert("html:"+html);
		$("<tr>" + html + "</tr>").css("background-color","")
			.appendTo("#train_ticket_list").show();
		if(count==4){
			$("#add_person").attr("class","unableAdd");
		}
	}
	
	//删除乘客
		$(".passenger_delete").live("click", function(){
			/*var table = document.getElementById("train_ticket_list");  
			if(table.rows.length==1){
				return false;
			}
				//alert(table.rows.length); 	
			var name = "";
			var card_num = "";	
			//$(this).parents("tr").css('background-color', 'red');	
		  $(this).parents("tr").parents("table").parents("tr").find(":text").each(function(){
				
				if($(this).attr("id").indexOf("name")>0){
					name = $("#"+$(this).attr("id")).val();
				};
				if($(this).attr("id").indexOf("ids")>0){
					card_num = $("#"+$(this).attr("id")).val();
				};
			});
			//删除同行乘客的对应选框清空
			removePaxAttr(name,card_num);
			//$(this).parents("tr").parents("table").parents("tr").prev().remove();
			$(this).parents("tr").parents("table").parents("tr").remove();
			
			$(this).parents("tr").parents("table").parents("tr").find(":text").each(function(){
				hideErrMsg($(this).attr("id"));
			});
			//重新设置index
			$(".adult:visible,.child:visible").each(function(index){
				var newIndex = index+1;
				$(this).html($(this).html().replace(/bookDetailInfoList\[\d+\]/g, "bookDetailInfoList["+index+"]"))
					.find(".indexTr").html(newIndex);
				$(this).html($(this).html().replace(/\_\d/g, "_"+newIndex));
			});	
			$("#add_person").attr("class","passenger_add");
			//alert($("#add_person").html());
			//兼容IE
			$("#add_person").attr("style","");**/
			//alert("jinrujs");
			var data = $(this).parents("tr").parents("table").parents("tr").text();
			//alert(data);
			var id = data.slice(data.lastIndexOf("：") + 1);
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
		$("#meal_insurance").live("click", function(){
			var link_phone = $.trim($("#link_phone").val());
			var link_name = $.trim($("#link_name").val());
			localStorage.setItem("link_name", link_name);
			localStorage.setItem("link_phone", link_phone);
			window.location='/pages/book/orderMeal.jsp';
		});

	  //提交订单
		$("#btnSubmit").live("click", function(){
			if(checkForm()){
				//无座备选
				//selectWz();
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
								$("form:first").attr("action", "/order/createOrder.jhtml?fpNeed=1");
							}else{
								$("form:first").attr("action", "/order/createOrder.jhtml");
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
			
			if($.trim($("#link_name").val())==""){
				$("#link_name").focus();
				alert("请填写联系人！");
				//return false;
				return false;
			}else if(!checkName($.trim($("#link_name").val()))){
				$("#link_name").focus();
				alert("请填写正确的联系人！");
				return false;
			}else{
				hideErrMsg("link_name");
			}
			
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
            	<input type="hidden" name="bx_pay_money" id="bx_pay_money" value=""/>
            	<input type="hidden" name="out_ticket_type" id="out_ticket_type" value="11" />
            	<input type="hidden" name="ps_pay_money" value="20" />
            	<input type="hidden" name="wz_ext" id="wz_ext" />
<div>
	
	<!-- start -->
	<div class="wrap">
		<header id="bar">
			<a href="javascript:window.history.back();" class="m19e_ret"></a>
			<h1>填写订单</h1>
		</header>
		<section id="order_main">
			
			<div class="order_con">
				<!-- 车票信息 -->
				<dl class="order_item">
					<dt><!-- <a href="" class="order_ticket_s">取票退票说明</a> --></dt>
					<dd line-height="30px">
					<!-- 
						<p>2014年1月27日</p>
						<p>G2132 天津(06:46)—北京南(07:19)</p>
						<p>二等座<span class="org">￥54.50</span>元</p>
						<p class="cons_tim">耗时:1小时20分</p>
					 -->
					 <table style="width:100%;margin:0 0 0 0;padding:0 0 0 0;">
					 	<tr style="background:#0f63b8;height:30px;width:100%;" >
					 		<td>&nbsp;</td>
					 		<td style="font-size:24px;color:#FFFAFA;width:90px;" id="trainCode"></td>
					 		<td colspan="2" align="right" style="font-size:20px;color:#FFFAFA;width:65%;"  id="travel_time"></td>
					 		<td>&nbsp;</td>
					 	</tr>
					 	<tr >
					 		<td></td>
					 		<td class="font_18 b" id="startCity"></td>
					 		<td rowspan="2" class="icon"><span class="from_to_icon" ></span></td>
					 		<td align="center" class="font_18 b right" id="endCity"></td>
					 		<td></td>
					 	</tr>
					 	<tr line-height="20px">
					 		<td></td>
					 		<td class="text_item" id="startTime"></td>
					 		<td align="center" class="text_item right" id="endTime"></td>
					 		<td></td>
					 	</tr>
					 	<tr>
					 		<td></td>
					 		<td class="text_item" id="seatType"></td>
					 		<td class="text_item"></td>
					 		<td align="center" class="text_item right" id="seatPrice"></td>
					 		<td></td>
					 	</tr>
					 </table>
					</dd>
				</dl>
				<!-- 乘客信息 -->
				<dl class="order_item order_infor">
					<!-- <dt>选择乘客<a href="javascript:void(0);" onclick="adddiv()" id="add_person" class="passenger_choose text_item">&nbsp;&nbsp;&nbsp;</a></dt> -->
					<dt onclick="window.location='/pages/book/oftenLinkers.jsp';">乘客信息<a  id="add_person" class="passenger_choose text_item">&nbsp;&nbsp;&nbsp;</a></dt>
					<dd>
					<div id="tip"></div>
					<div id="orderInfo" style="display:block;">
						<table style="width:100%;margin:0 0 0 0;padding:0 0 0 0;" id="train_ticket_list">
						<!-- 
						<tr class="adult" >
					 		<td>
						
					 	
					 		<table style="width:100%;margin:0 0 0 0;padding:0 0 0 0;">
							 	<tr>
							 		<td>&nbsp;&nbsp;<input type="hidden" value="index_source"/></td>
							 		<td class="text_item">
							 			姓名：
							 			<input class="user_name_text input_hidden_border" type="text" name="bookDetailInfoList_source.user_name" id="user_name_index_source" 
		                            		title="姓名" value="孙薇" />
							 		</td>
							 		
							 		<td class="text_item" >
							 			<input class="input_hidden_border" type="text" name="bookDetailInfoList_source.ticket_type" id="ticket_type_index_source" 
		                            		 value="0" />
									</td>
							 		<td><a href="javascript:void(0);" class="passenger_delete">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
							 	</tr>
							 	<tr>
							 		<td></td>
							 		<td class="text_item" colspan="2">
							 			<input style="width:92px;" class="input_hidden_border" type="text" name="bookDetailInfoList_source.ids_type" 
							 			id="ids_type_index_source"  value="2" />
							 			：
							 			<input style="width:250px;" class="idcard_text input_hidden_border" type="text" name="bookDetailInfoList_source.user_ids" 
		                            		id="user_ids_index_source" title="证件号码" value="211202198812091527" />
							 		</td>
							 		<td></td>
						 		</tr>
						 	
						  </table>
						  
						</td>
					 	</tr>
					 	--> 
					  </table>
					</div>
					</dd>
					
				</dl>
				
				<!-- 保险信息 -->
				<dl class="order_item">
					<dt>
						<p>套餐类型：
							<!-- 
							<span id="meal_insurance" class="org">交通意外险&nbsp;￥20*1<a href="/pages/book/orderMeal.jsp" id="add_insurance" class="insurance_choose text_item">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></span>
							 -->
							<span id="meal_insurance" class="org" >交通意外险&nbsp;￥20*1</span>
						</p>
					</dt>
					<hr/>
				</dl>
				
			  <!-- 车站自提 -->
				<dl class="order_item">
					<dt>车站自提</dt>
					<dd>
					<div id="InsuranceInfo">
						<table style="width:100%;margin:0 0 0 0;padding:0 0 0 0;">
							 	<tr class="linktr">
							 		<td class="linkinput">
							 			<input type="text" name="link_name" id="link_name" class="pass_ziti pass_inp text_item" autocomplete="off" placeholder="姓名（必填）">
							 		</td>
							 		
							 	</tr>
							 	<tr class="linktr">
							 		<td align="right" class="linkinput" >
							 				<input type="text" class="pass_ziti pass_inp text_item" autocomplete="off" placeholder="手机号（必填）" name="link_phone" id="link_phone">
									</td>
							 		
							 	</tr>
						 	
						  </table>
						  <p style="font-size:14px;color:#888;">&nbsp;&nbsp;&nbsp;手机号用于接收通知短信，请真实有效</p>
					</div>
					</dd>
					<br/>
					<br/>
					<dt>
					<p id="bx_confirm" style="font-size:14px;"><span class="display_mid"><input type="checkbox" id="chk_bxconfirm" checked="checked"/></span>阅读并同意<span style="color:#0081cc;cursor:pointer;line-height:24px;" onclick="window.open('/pages/common/protocol.html');">19e旅行服务协议</span>和<span style="color:#0081cc;cursor:pointer;line-height:24px;" onclick="window.open('/pages/common/insurance.html');">保险说明</span></p>
					</dt>
				</dl>
				
			</div>
			<div class="order_foot">
				<div class="order_d" style="background:#d13b00;">
					<span class="order_pay" id="shouldPayMoney"></span>
				</div>
				<div class="order_d" style="background:#ff7200;">
					<span class="order_btn"><input type="button" value="提交订单" id="btnSubmit" class="order_submit" /></span>
				</div>
			</div>	
		</section>
	</div>
	<!-- end -->


</div>	
</form>
</body>
</html>		
