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
<style type="text/css">
.screen {width:480px; height:700px; margin:0 auto;}
.order_con {padding:0 0;}
.order_item dd {line-height:30px;padding: 10px 10px;}
.text_item {font-size:14px;color:#888;}
.text_baoxian {font-size:18px;color:#0f63b8;}
.pass_inp, .pass_sel {font-size:14px;width:240px; height:35px; margin:3px 0; display:block; border-radius:3px;padding-left:10px;}
.pass_sel{ width:130px; height:35px;padding-left:10px;}
.pass_ziti{width:96%; margin-left:20px;}
.passenger_del {position:absolute; right:0; top:0; height:40px; width:30px; background:url(/images/pas_del_bg.gif) no-repeat center center; text-indent:-9999px;}
.passenger_delete{position:relative; right:0; top:0; height:40px; width:30px; background:url(/images/pas_del_bg.gif) no-repeat center center; text-indent:-9999px;}
.passenger_add {position:absolute; right:25px; top:0; padding-right:30px; background:url(/images/pas_add_bg.gif) no-repeat right center; color:#0f63b8;}
.passenger_check {position:absolute; right:25px; top:0; padding-right:10px; color:#0f63b8;}
input[type="checkbox"] { font-size: 12px; color: #000000; border: 1px;zoom:150%;background-color: #DBFBF7;}
input[type="radio"] { 
	
	font-size: 12px; 
	color: #000000; 
	border: 1px #000;
	zoom:150%;
	background-color: #DBFBF7; 
}
</style>
<script type="text/javascript">
	function showdiv(divname) {     
		var radios = document.getElementsByName("baoxian");
		for ( var i = 0; i < radios.length; i++) {
			if (radios[0].checked==true) {
				if ($("#" + divname).css("display") == "none") {       //显示div
	                 $("#" + divname).slideDown(500);
	             }
		   	} else {
		   		if ($("#" + divname).css("display") == "block") {       //隐藏div
	             	$("#" + divname).slideUp(500);
	             } 
			}
		}
		     /**
             if ($("#" + divname).css("display") == "none") {       //显示div
                 $("#" + divname).slideDown(500);
             } else if ($("#" + divname).css("display") == "block") {       //隐藏div
             	$("#" + divname).slideUp(500);
             } else  {
                $("#" + divname).slideDown(500);
                alert("执行出错");
             }
        	*/
         }

    function showXuyaoDiv(divname){
    	var checkboxs = document.getElementsByName("check");
    	for ( var i = 0; i < checkboxs.length; i++) {
    	if (checkboxs[0].checked==true) {
			if ($("#" + divname).css("display") == "none") {       //显示div
                 $("#" + divname).slideDown(500);
             }
	   	} else {
	   		if ($("#" + divname).css("display") == "block") {       //隐藏div
             	$("#" + divname).slideUp(500);
             } 
		}
    	}
    }

    function submitForm(){
    	if( $("#train_product0").attr("checked") ){
    		localStorage.setItem("baoxian","1");//购买保险
        	if( $("#check").attr("checked")){
        		localStorage.setItem("fpNeed",1);//需要邮寄发票
        		localStorage.setItem("fp_receiver",document.getElementById("fp_receiver").value);//发票姓名
        		localStorage.setItem("fp_phone",document.getElementById("fp_phone").value);//发票电话
        		localStorage.setItem("fp_address",document.getElementById("fp_address").value);//发票地址
        		localStorage.setItem("fp_zip_code",document.getElementById("fp_zip_code").value);//发票邮编
            } else {
            	localStorage.setItem("fpNeed",0);//不需要邮寄发票
            }
        }else{
        	localStorage.setItem("baoxian","0");//不购买保险
		}
    	window.location.href="bookInfoInput.jsp";
    }
    $().ready(function(){
    	var baoxian = localStorage.getItem("baoxian");
    	if($.trim(baoxian) == "0") {
    		$("#train_product1").attr("checked", true);
    		$("#train_product0").attr("checked", false);
    	}else {
    		$("#train_product0").attr("checked", true);
    		$("#train_product1").attr("checked", false);
    	}
    });
</script>
</head>

<body>
<div>
	<!-- start -->
	<div class="wrap1">
		<header id="bar">
			<a href="javascript:window.history.back();" class="m19e_ret"></a>
			<h1>套餐选择</h1>
		</header>
		<section id="order_main">
			
			<div class="order_con">
				<!-- 保险信息 -->
				<dl class="order_item">
					<dt>保险信息<!-- <a href="" class="order_ticket_s">取票退票说明</a> --></dt>
					<dd line-height="24px">
					
					 <table style="width:100%;margin:0 0 0 0;padding:0 0 0 0;">
					 	<tr style="height:30px;width:100%;" >
					 		<td class="text_baoxian">
								<input class="train_product" type="radio" id="train_product0" name="baoxian" checked=true onclick="showdiv('insuranceMail');"/>
								<label for="train_product0">购买交通意外险&nbsp;&nbsp; <span class="org">￥20元/份</span></label>
							</td>
					 	</tr>
					 	<tr class="text_item" style="line-height:30px;">
					 		<td>
					 			<label for="train_product0">VIP客户：优先处理订单享受人工退票改签服务</label>
					 		</td>
					 	</tr>
					 	<tr height="2px">
					 		<td><hr/></td>
					 	</tr>
					 	<tr line-height="30px">
					 		<td class="text_baoxian">
								<input class="train_product" type="radio" id="train_product1" name="baoxian" onclick="showdiv('insuranceMail');"/> 
								<label for="train_product1">不购买保险</label>
							</td>
					 	</tr>
					 	<tr class="text_item" style="line-height:30px;">
					 		<td>
					 			<label for="train_product1">排队代购：订单处理约2小时不提供人工退改签</label>
					 		</td>
					 	</tr>
					 </table>
					</dd>
				</dl>
				<!-- 邮寄保险发票 -->
				<div id="insuranceMail">
				<dl class="order_item order_infor">
					<dt>邮寄保险发票<a href="javascript:void(0);" id="add_person" class="passenger_check text_item"><label for="check">需要</label><input type="checkbox" id="check" name="check" onclick="showXuyaoDiv('orderInfo')"/></a></dt>
					<dd>
					<div id="orderInfo" style="display:none;">
						<table style="width:100%;margin:0 0 0 0;padding:0 0 0 0;">
							 	<tr>
							 		<td></td>
							 		<td class="text_item">
							 			<input type="text" name="fp_receiver" id="fp_receiver" class="pass_ziti pass_inp text_item" autocomplete="off" placeholder="联系人（必填）">
							 		</td>
							 		<td align="right" class="text_item" >
							 				<input type="text" class="pass_ziti pass_inp" autocomplete="off" placeholder="手机号（必填）" name="fp_phone" id="fp_phone">
									</td>
							 		<td></td>
							 	</tr>
						 		<tr>
							 		<td></td>
							 		<td class="text_item">
							 			<input type="text" name="fp_address" id="fp_address" class="pass_ziti pass_inp text_item" autocomplete="off" placeholder="联系地址（必填）">
							 		</td>
							 		<td align="right" class="text_item" >
							 				<input type="text" class="pass_ziti pass_inp" autocomplete="off" placeholder="邮编（必填）" name="fp_zip_code" id="fp_zip_code">
									</td>
							 		<td></td>
							 	</tr>
						  </table>
						  
					</div>
					</dd>
				</dl>
				</div>
			</div>
			<br/><br/><br/><br/>
			<div class="order_foot">
				<div class="order_d" style="width:90%;margin-left:5%;margin-right:5%;">
					<input type="button" value="确定" onclick="submitForm();" class="order_submit ticket_ser" style="font-size:26px;" />
				</div>
				
			</div>	
		</section>
	</div>
	<!-- end -->


</div>	
</body>
</html>		
