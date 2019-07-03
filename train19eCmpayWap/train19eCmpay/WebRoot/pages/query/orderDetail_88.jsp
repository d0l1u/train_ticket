<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/tld/trainUtil.tld" prefix="tn"%>
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
<link rel="stylesheet" type="text/css" href="/css/base.css">
<link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/artDialog.js"></script>
<style type="text/css">
.screen {width:480px; margin:0 auto;}
.order_con {padding:0 0;}
.order_item dd {line-height:30px;}
.text_item {font-size:18px;color:#888;}
.input_hidden_border {font-size:18px;color:#888;width:70px;border:0;font-family:Microsoft YaHei, calibri, verdana;}
.pass_inp, .pass_sel {width:240px; height:35px; margin:3px 0; display:block; border-radius:3px;padding-left:10px;}
.pass_sel{ width:130px; height:35px;padding-left:10px;}
.pass_ziti{width:200px;}
.passenger_del {position:absolute; right:0; top:0; height:40px; width:30px; background:url(/images/cancel-icon.png) no-repeat center center; text-indent:-9999px;}
.passenger_delete{position:relative; right:0; top:0; height:40px; width:30px; background:url(/images/cancel-icon.png) no-repeat center center; text-indent:-9999px;}
.passenger_add {position:absolute; right:25px; top:0; padding-right:30px; background:url(/images/add-btn.png) no-repeat right center; color:#0f63b8;}
.rebook_btn{height:50px; margin-bottom:0;background:#ff7200;text-align:center; width:100%;font-size:bigger;}
.order_status {width:100%; overflow:hidden; color:#ff5400;height:2.5em;text-align:center;line-height:2.5em;}

</style>
<script type="text/javascript">

	var userId = "${agentId}"+"_hcp";
	var cookieValue = ""; 	//编码后的cookie值
	var cookieRealValue	= "";	//cookieJson解析后需要使用的数据
	var param_product_id = "";	//传递给后台的保险ID值

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


	function submitData(from_city, to_city, travel_time){
		window.location = "/buyTicket/queryByStation.jhtml?from_city=" + from_city + "&to_city=" + to_city + "&travel_time=" + travel_time;
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
            	
            	<input type="hidden" name="out_ticket_type" id="out_ticket_type" value="11" />
            	<input type="hidden" name="ps_pay_money" value="20" />
            	<input type="hidden" name="wz_ext" id="wz_ext" />
<div>
	
	<!-- start -->
	<div class="wrap">
		<header id="bar">
			<a href="javascript:window.history.back();" class="m19e_ret"></a>
			<h1>订单详情</h1>
		</header>
		<section id="order_main">
			
			<div class="order_con">
				<!-- 车票信息 -->
				<dl class="order_item">
					<dt>车票信息<!-- <a href="" class="order_ticket_s">取票退票说明</a> --></dt>
					<dd line-height="30px">
					<c:choose>
						<c:when test="${orderInfo.order_status eq '00' || orderInfo.order_status eq '22' || orderInfo.order_status eq '99'}">
						</c:when>
						<c:otherwise>
							<p>12306订单号：${orderInfo.out_ticket_billno}</p>
						</c:otherwise>
					</c:choose>
					<!-- 
						<p>2014年1月27日</p>
						<p>G2132 天津(06:46)—北京南(07:19)</p>
						<p>二等座<span class="org">￥54.50</span>元</p>
						<p class="cons_tim">耗时:1小时20分</p>
					 -->
					 <table style="width:100%;margin:0 0 0 0;padding:0 0 0 0;">
					 	<tr style="background:#0f63b8;height:30px;width:100%;" >
					 		<td>&nbsp;&nbsp;</td>
					 		<td style="font-size:26px;color:#FFFAFA" id="trainCode"><strong>${orderInfo.train_no }</strong></td>
					 	
					 		<td colspan="2" align="right" style="font-size:20px;color:#FFFAFA" width="40%" id="travel_time">${orderInfo.travel_time } ${day }</td>
					 		<td>&nbsp;&nbsp;</td>
					 	</tr>
					 	<tr>
					 		<td></td>
					 		<td class="font_24 b"  id="startCity">${orderInfo.from_city }</td>
					 		<td  rowspan="2" style="vertical-align:middle;align:right;" ><span class="from_to_icon" align="right"></span></td>
					 		<td class="font_24 b" id="endCity">${orderInfo.to_city }</td>
					 		<td></td>
					 	</tr>
					 	<tr line-height="20px">
					 		<td></td>
					 		<td class="text_item" id="startTime">${orderInfo.from_time}</td>
					 		<td align="center" class="text_item" id="endTime">${orderInfo.to_time}</td>
					 		<td></td>
					 	</tr>
					 	<tr>
					 		<td></td>
					 		<td class="text_item" id="seatType" >${seatTypeMap[orderInfo.seat_type]}</td>
					 		<td></td>
					 		<td class="text_item" align="center">${fn:length(detailList)}张</td>
					 		<td></td>
					 	</tr>
					 	<tr>
					 	<td colspan="5">
					 	<hr color=#888 size="3"/>	
					 	</td>
					 	</tr>
					 	<tr>
					 		<td>&nbsp;&nbsp;</td>
					 		<td colspan="2" id="orderId">订单号:${orderInfo.order_id }</td>
					 		<td>&nbsp;&nbsp;</td>
					 	</tr>
					<c:choose>
						<c:when test="${orderInfo.order_status eq '00' || orderInfo.order_status eq '22' || orderInfo.order_status eq '99'}">
						</c:when>
						<c:otherwise>
							<tr>
					 		<td>&nbsp;&nbsp;</td>
					 		<td colspan="2" id="orderId">下单时间:${orderInfo.pay_time }</td>
					 		<td>&nbsp;&nbsp;</td>
					 		</tr>
						</c:otherwise>
					</c:choose>
					 	
					 </table>
					</dd>
				</dl>
				<!-- 乘客信息 -->
				<dl class="order_item order_infor">
					<!-- <dt>选择乘客<a href="javascript:void(0);" onclick="adddiv()" id="add_person" class="passenger_choose text_item">&nbsp;&nbsp;&nbsp;</a></dt> -->
					<dt>乘客信息</dt>
					
					<dd>
					<div id="orderInfo" style="display:block;">
						<table style="width:100%;margin:0 0 0 0;padding:0 0 0 0;" id="train_ticket_list">
						<c:forEach items="${detailList}" var="detail" varStatus="idx">
						<c:if test="${idx.index != 0}">
            				<tr><td colspan="4"><hr/></td></tr>
            			</c:if>
						<tr class="adult" >
					 		<td>
					 		<table style="width:100%;margin:0 0 0 0;padding:0 0 0 0;">
							 	<tr>
							 		<td>&nbsp;&nbsp;<input type="hidden" value="index_source"/></td>
							 		<td class="text_item">
							 			<input type="hidden" name="user_name" id="user_name" value="${detail.user_name}"> 
							 			姓名：${detail.user_name}
							 		</td>
							 		
							 		<td class="text_item" >${ticketTypeMap[detail.ticket_type]}
									</td>
							 		<!-- <td><a href="javascript:void(0);" class="passenger_delete">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></td> -->
							 	</tr>
							 	<tr>
							 		<td></td>
							 		<td class="text_item" colspan="2">
							 			${idsTypeMap[detail.ids_type]}
							 			：
							 			${detail.user_ids}
							 		</td>
							 		<td></td>
						 		</tr>
						 		<tr>
						 			<td></td>
						 			<td class="text_item" colspan="3">
						 				保险：<c:if test="${detail.bx_id eq '' || (empty detail.bx_id)}">无</c:if>
						 				<c:if test="${!empty detail.bx_id}">单号${detail.bx_id}  &nbsp;&nbsp;&nbsp;￥${detail.bx_pay_money}</c:if>
						 			</td>
						 		</tr>
						 		
						  </table>
						  
						</td>
					 	</tr>
					 	
					 	</c:forEach>
					  </table>
					</div>
					</dd>
					
				</dl>
				
				
			  <!-- 订单状态 -->
				<dl class="order_status">
					<dt>
					<c:choose>
						<c:when test="${!empty detailInfo.refund_status}">
							${rsStatusMap[detailInfo.refund_status]}
						</c:when>
						<c:otherwise>
							${orderStatusMap[orderInfo.order_status] }
						</c:otherwise>
					</c:choose>
					</dt>
				</dl>
			</div>
			
			<c:if test="${orderInfo.order_status eq '99'}">
			<div class="ticket_ser_w">
				<input type="button" value="重新购票" id="btnSubmit" class="ticket_ser" onclick="submitData('${orderInfo.from_city}', '${orderInfo.to_city}', '${orderInfo.travel_time}');">	
			</div>
			</c:if>
		</section>
		<!-- 
		<c:if test="${orderInfo.order_status eq '99'}">
			<input type="button" value="重新购票" id="btnSubmit" class="rebook_btn" onclick="submitData('${orderInfo.from_city}', '${orderInfo.to_city}', '${orderInfo.travel_time}');" />
		</c:if>
		 -->
	</div>
	<!-- end -->


</div>	
</form>
</body>
</html>		
