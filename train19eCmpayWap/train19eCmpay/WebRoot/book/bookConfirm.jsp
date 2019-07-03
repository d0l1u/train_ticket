<!doctype html>
<html>
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<meta charset="utf-8">
<title>掌上19e-火车票</title>
<meta name="keyword" content="19e,便民服务,数字便民,移动客户端,手机客户端,话费充值,手机充值,游戏充值,QQ充值,机票预订,水费,电费,煤气费,取暖费,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
<meta name="description" content="提供19e数字便民移动终端下载,随时随地办理便民业务,支持移动客户端话费充值,手机充值,游戏充值,QQ充值,机票预订,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
<link rel="stylesheet" type="text/css" href="/css/base.css">
<link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon" />
<script type="text/javascript" src="/js/jquery.js"></script>
<style type="text/css">
.screen {width:480px; margin:0 auto;}
.order_con {padding:0 0;}
.order_item dd {line-height:30px;}
.text_item {font-size:18px;color:#888;}
.pass_inp, .pass_sel {width:240px; height:35px; margin:3px 0; display:block; border-radius:3px;padding-left:10px;}
.pass_sel{ width:130px; height:35px;padding-left:10px;}
.pass_ziti{width:180px;}
.passenger_del {position:absolute; right:0; top:0; height:40px; width:30px; background:url(../images/pas_del_bg.gif) no-repeat center center; text-indent:-9999px;}
.passenger_delete{position:relative; right:0; top:0; height:40px; width:30px; background:url(../images/pas_del_bg.gif) no-repeat center center; text-indent:-9999px;}
.passenger_add {position:absolute; right:25px; top:0; padding-right:30px; background:url(../images/pas_add_bg.gif) no-repeat right center; color:#0f63b8;}
</style>
<script type="text/javascript">
	var userId = "${agentId}"+"_hcp";
	var cookieValue = ""; 	//编码后的cookie值
	var cookieRealValue	= "";	//cookieJson解析后需要使用的数据

	var param_product_id = "";	//传递给后台的保险ID值
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
			var table = document.getElementById("train_ticket_list");  
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
			$("#add_person").attr("style","");
			
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
</script>
</head>

<body>

<div class="screen">
	<!-- start -->
	<div class="wrap">
		<header id="bar">
			<a href="javascript:window.history.back();" class="m19e_ret"></a>
			<h1>订单预定</h1>
		</header>
		<section id="order_main">
			
			<div class="order_con">
				<!-- 车票信息 -->
				<dl class="order_item">
					<dt>车票信息<!-- <a href="" class="order_ticket_s">取票退票说明</a> --></dt>
					<dd line-height="30px">
					<!-- 
						<p>2014年1月27日</p>
						<p>G2132 天津(06:46)—北京南(07:19)</p>
						<p>二等座<span class="org">￥54.50</span>元</p>
						<p class="cons_tim">耗时:1小时20分</p>
					 -->
					 <table style="width:100%;margin:0 0 0 0;padding:0 0 0 0;">
					 	<tr style="background:#a0d8f2;height:30px;width:100%;" >
					 		<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
					 		<td colspan="2" style="font-size:26px;color:#FFFAFA"><strong>${trainCode}</strong></td>
					 		<td align="right" style="font-size:20px;color:#FFFAFA">${travelTime} ${day }</td>
					 		<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
					 	</tr>
					 	<tr>
					 		<td></td>
					 		<td class="font_24 b">${startCity}</td>
					 		<td rowspan="2" style="vertical-align:middle;align:center;">======></td>
					 		<td align="right" class="font_24 b">${endCity}</td>
					 		<td></td>
					 	</tr>
					 	<tr line-height="20px">
					 		<td></td>
					 		<td class="text_item">${startTime}</td>
					 		<td align="right" class="text_item">${endTime}</td>
					 		<td></td>
					 	</tr>
					 	<tr>
					 		<td></td>
					 		<td class="text_item">${seat_type}</td>
					 		<td class="text_item"></td>
					 		<td align="right" class="text_item">￥${seat_price}</td>
					 		<td></td>
					 	</tr>
					 </table>
					</dd>
				</dl>
				<!-- 乘客信息 -->
				<dl class="order_item order_infor">
					<dt>乘客信息<a href="javascript:void(0);" onclick="adddiv()" id="add_person" class="passenger_add text_item">新增乘客</a></dt>
					<dd>
					<div id="orderInfo" style="display:block;">
						<table style="width:100%;margin:0 0 0 0;padding:0 0 0 0;" id="train_ticket_list">
					 	<tr class="adult" >
					 		<td>
					 		<table style="width:100%;margin:0 0 0 0;padding:0 0 0 0;">
					 			<tr><hr/></tr>
							 	<tr>
							 		<td>&nbsp;&nbsp;&nbsp;&nbsp;<input type="hidden" value="index_source"/></td>
							 		<td class="text_item">
							 			<input type="text" name="bookDetailInfoList_source.user_name" id="user_name_index_source" class="pass_inp text_item  text_name" autocomplete="off" placeholder="姓名（必填）">
							 		</td>
							 		
							 		<td align="right" class="text_item" >
							 				<select name="bookDetailInfoList_source.ticket_type" id="ticket_type_index_source" class="pass_sel text_item">
												<option>成人票</option>
												<option>儿童票</option>
											</select>
									</td>
							 		<td><a href="javascript:void(0);" class="passenger_delete">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
							 	</tr>
							 	<tr>
							 		<td></td>
							 		<td class="text_item">
							 			<input type="text" class="pass_inp" autocomplete="off" placeholder="证件号（必填）" name="bookDetailInfoList_source.user_ids" id="user_ids_index_source">
							 		</td>
							 		<td align="right" class="text_item">
							 			<!-- 
							 				<select class="pass_sel text_item" name="bookDetailInfoList_source.ids_type" id="ids_type_index_source">
												<option>二代身份证</option>
												<option>港澳通行证</option>
												<option>台湾通行证</option>
												<option>护照</option>
											</select>
										 -->
										 <input type="text" class="pass_sel" autocomplete="off" placeholder="二代身份证" name="bookDetailInfoList_source.ids_type" id="ids_type_index_source" readonly="readonly">
							 		</td>
							 		<td></td>
						 		</tr>
						 	
						  </table>
						</td>
					 	</tr>
					 	
					  </table>
					</div>
					</dd>
				</dl>
				
				<!-- 保险信息 -->
				<dl class="order_item">
					<dt>保险信息</dt>
					<dd>
					<div id="InsuranceInfo">
						<table style="width:100%;margin:0 0 0 0;padding:0 0 0 0;">
					 	<tr>
					 		<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
					 		<td class="text_item">
					 			<input class="train_product" type="radio" id="train_product0" name="baoxian" checked="checked" /> <label for="train_product0">20元/份，保额65万</label>
					 		</td>
					 		<td align="right" class="text_item">
					 			<input class="train_product" type="radio" id="train_product1" name="baoxian"/> <label for="train_product1">不购买保险</label>
					 		</td>
					 		<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
					 	</tr>
					 </table>
					</div>
					</dd>
				</dl>
				
			  <!-- 车站自提 -->
				<dl class="order_item">
					<dt>车站自提</dt>
					<dd>
					<div id="InsuranceInfo">
						<table style="width:100%;margin:0 0 0 0;padding:0 0 0 0;">
							 	<tr>
							 		<td>&nbsp;&nbsp;&nbsp;&nbsp;<input type="hidden" value="index_source"/></td>
							 		<td class="text_item">
							 			<input type="text" name="link_name" id="link_name" class="pass_ziti pass_inp text_item" autocomplete="off" placeholder="联系人（必填）">
							 		</td>
							 		<td align="right" class="text_item" >
							 				<input type="text" class="pass_ziti pass_inp" autocomplete="off" placeholder="手机号（必填）" name="link_phone" id="link_phone">
									</td>
							 		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
							 	</tr>
						 	
						  </table>
						  <p style="font-size:18px;color:#888;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;手机号用于接收通知短信</p>
					</div>
					</dd>
				</dl>
				
			</div>
			<div class="order_foot">
				<div class="order_d" style="background:#d13b00;">
					<span class="order_pay">应付金额:<strong>￥74.50</strong></span>
				</div>
				<div class="order_d" style="background:#ff7200;">
					<span class="order_btn"><input type="submit" value="提交订单" class="order_submit" onclick="location.href='order_result.html'"></span>
				</div>
			</div>	
		</section>
	</div>
	<!-- end -->


</div>	
</body>
</html>		
