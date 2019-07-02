
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>掌上19e-火车票</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<meta name="keyword" content="19e,便民服务,数字便民,移动客户端,手机客户端,话费充值,手机充值,游戏充值,QQ充值,机票预订,水费,电费,煤气费,取暖费,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
<meta name="description" content="提供19e数字便民移动终端下载,随时随地办理便民业务,支持移动客户端话费充值,手机充值,游戏充值,QQ充值,机票预订,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
<link rel="stylesheet" type="text/css" href="/css/base.css">
<link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">
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
	$(".add_adult").click(function(){
		var count = $(".adult:visible,.child:visible").length;
		if(count>=5){
			//dialogAlter("一个订单最多可代购5张票！");
			return;
		}
		var replaceStr = "bookDetailInfoList["+count+"]";
		var html = $(".adult:hidden").html().replace(/index_source/g, count+1);
		html = html.replace(/bookDetailInfoList_source/g, replaceStr);
		$("<tr class=\"adult\">" + html + "</tr>").css("background-color","")
			.appendTo("#train_ticket_list").show();
		if(count==4){
			$("#add_person").attr("class","unableAdd");
		}
		//兼容IE
		$("#add_person").attr("style","");
		$(".savePaxDiv").attr("style","padding-top:4px;*+padding-top:3px;_padding-top:10px;");
	}).trigger("click");
</script>
</head>

<style type="text/css">
.screen {width:480px; height:700px; margin:0 auto;}
.order_con {padding:0 0;}
.order_item dd {line-height:30px;}
.text_item {font-size:18px;color:#888;}
</style>

<body>

<div>

	<!-- start -->
	<div class="wrap">
		<header id="bar">
			<a href="javascript:window.history.back();" class="m19e_ret"></a>
			<h1>订单预定</h1>
		</header>
		<section id="order_main">
			
			<div class="order_con">
				<dl class="order_item">
					<dt>车票信息<!-- <a href="" class="order_ticket_s">取票退票说明</a> --></dt>
					<dd line-height="30px">
					 <table style="width:100%;margin:0 0 0 0;padding:0 0 0 0;">
					 	<tr style="background:#0f63b8;height:30px;width:100%;" >
					 		<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
					 		<td colspan="2" style="font-size:26px;color:#FFFAFA"><strong>${trainCode}</strong></td>
					 		<td align="right" style="font-size:20px;color:#FFFAFA">${travelTime}</td>
					 		<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
					 	</tr>
					 	<tr>
					 		<td></td>
					 		<td class="font_20 b">${startCity}</td>
					 		<td rowspan="2" style="vertical-align:middle;align:center;"><span class="from_to_icon"></span></td>
					 		<td align="right" class="font_20 b">${endCity}</td>
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
					 		<td class="text_item">硬座</td>
					 		<td class="text_item"></td>
					 		<td align="right" class="text_item">￥54.5</td>
					 		<td></td>
					 	</tr>
					 </table>
					</dd>
				</dl>

				<dl class="order_item order_infor">
					<dt>乘客信息<a href="javascript:void(0);" onclick="adddiv('newPasser')" class="passenger_add">新增乘客</a></dt>
					<dd>
					<div id="orderInfo" style="display:block;">
						<input type="text" class="pass_inp" autocomplete="off" placeholder="姓名（必填）">
						<select name="" id="" class="pass_sel">
							<option>二代身份证</option>
							<option>港澳通行证</option>
							<option>台湾通行证</option>
							<option>护照</option>
						</select>	
						<select name="" id="" class="pass_sel">
							<option>成人票</option>
							<option>儿童票</option>
						</select>	
						<input type="text" class="pass_inp" autocomplete="off" placeholder="证件号（必填）">
					</div>
					</dd>
				</dl>
				
				<dl class="order_item order_infor" id="add_person">
                    <span class="add_adult"></span>
                </dl>
				
				<dl class="order_item order_infor" id="newPasser" style="display:none;">
					<dt>乘客信息<a href="javascript:void(0);" onclick="showdiv('orderInfo')" class="passenger_del">折叠</a></dt>
					<dd>
					<div id="orderInfo" style="display:block;">
						<input type="text" class="pass_inp" autocomplete="off" placeholder="姓名（必填）">
						<select name="" id="" class="pass_sel">
							<option>二代身份证</option>
							<option>港澳通行证</option>
							<option>台湾通行证</option>
							<option>护照</option>
						</select>	
						<select name="" id="" class="pass_sel">
							<option>成人票</option>
							<option>儿童票</option>
						</select>	
						<input type="text" class="pass_inp" autocomplete="off" placeholder="证件号（必填）">
					</div>
					</dd>
				</dl>

				<dl class="order_item">
					<dt>保险信息<a href="javascript:void(0);" onclick="showdiv('InsuranceInfo')" class="passenger_del">折叠</a></dt>
					<dd>
					<div id="InsuranceInfo" style="display:none;">
						<table style="width:100%;margin:0 0 0 0;padding:0 0 0 0;">
					 	<tr>
					 		<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
					 		<td class="text_item">bx123542314</td>
					 		<td class="text_item">成人险</td>
					 		<td align="right" class="text_item">20*1</td>
					 		<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
					 	</tr>
					 	<tr>
					 		<td></td>
					 		<td colspan="2" class="text_item">2014-04-01下单</td>
					 		<td align="right" class="text_item">￥20.0</td>
					 		<td></td>
					 	</tr>
					 </table>
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
