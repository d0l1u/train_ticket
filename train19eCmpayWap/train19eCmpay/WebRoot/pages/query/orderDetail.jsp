
<!DOCTYPE html>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/tld/trainUtil.tld" prefix="tn"%>
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
<script type="text/javascript" src="/js/artDialog.js"></script>
<style type="text/css">
.screen {width:480px; margin:0 auto;}
.order_con {padding:0 0;}
.order_item dd {line-height:30px;margin:0 2% 0 2%;}
.text_item {font-size:16px;color:#888;}
td.icon{vertical-align:middle;align:center;width:30%;}
.text_item_black{font-size:14px;color:#000;}
#trainCode{font-size:20px;color:#FFFAFA;width:35%; padding-left:10px;}
#travel_time {font-size:18px;color:#FFFAFA;width:65%; padding-right:10px; text-align:right;}
.text_item_header {font-size:16px;color:#888;padding-left:5%;margin:0 0 0 20px;}
.input_hidden_border {font-size:18px;color:#888;width:70px;border:0;font-family:Microsoft YaHei, calibri, verdana;}
.pass_inp, .pass_sel {width:240px; height:35px; margin:3px 0; display:block; border-radius:3px;padding-left:10px;}
.pass_sel{ width:130px; height:35px;padding-left:10px;}
.pass_ziti{width:200px;}
.passenger_del {position:absolute; right:0; top:0; height:40px; width:30px; background:url(/images/cancel-icon.png) no-repeat center center; text-indent:-9999px;}
.passenger_delete{position:relative; right:0; top:0; height:40px; width:30px; background:url(/images/cancel-icon.png) no-repeat center center; text-indent:-9999px;}
.passenger_add {position:absolute; right:25px; top:0; padding-right:30px; background:url(/images/add-btn.png) no-repeat right center; color:#0f63b8;}
.rebook_btn{height:50px; margin-bottom:0;background:#ff7200;text-align:center; width:100%;font-size:bigger;}
.order_status { color:#ff5400;height:2em;line-height:2em;padding-top:5px;padding-left:10px;margin-top:10px;margin-bottom:20px;margin-left:20px;}
html{-webkit-text-size-adjust:100%; -ms-text-size-adjust:100%;background:#f5f6f6;}

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
	
	function refund(order_id, refund_status) {
		//window.location="/refund/refunding.jhtml?refund.order_id=" + order_id + "&refund_status=" + refund_status;
		$("form:#reRefundForm").submit();
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
	
	function goToPay(order_id, count) {
		var bankAbbr = $("#bankAbbr").val();
		if(confirm("确认前往支付该订单吗？")){
		$("form:first").attr("action", "/order/orderCmpay.jhtml?order_id="+order_id+"&bankAbbr="+bankAbbr+"&productNum="+count);
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
		$("form:first").submit();
	}
	}
</script>
</head>

<body>

<div>
	<div class="wrap">
		<header id="bar">
			<a href="javascript:window.history.back();" class="m19e_ret"></a>
			<h1>订单详情</h1>
			<c:if test="${orderInfo.order_status eq '99'}">
				<a href="/pages/book/menuNew.jsp" class="m19e_home"><span></span></a>
			</c:if>
		</header>
		<section id="order_main">
			<form id="reRefundForm" name="reRefundForm" method="post" action="/refund/refunding.jhtml" enctype="multipart/form-data">
			<div class="order_con">

				<dl class="order_item">
					<dt class="text_item_header">订单信息</dt>
					<dd line-height="30px">
					<c:choose>
						<c:when test="${orderInfo.order_status eq '00' || orderInfo.order_status eq '22' || orderInfo.order_status eq '99'}">
						</c:when>
						<c:otherwise>
							<p class="text_item_black">12306订单号：${orderInfo.out_ticket_billno}</p>
						</c:otherwise>
					</c:choose>
					
					 <table style="width:100%;margin:0 0 0 0;padding:0 0 0 0;">
					 	<tr style="background:#0f63b8;height:30px;width:100%;" >
					 		
					 		<td id="trainCode"><strong>${orderInfo.train_no }</strong></td>
					 		<td colspan="2" id="travel_time">${orderInfo.travel_time } ${day }</td>
					 	</tr>
					 	<tr>
					 	
					 		<td class="font_18 b" align="center" id="startCity">${orderInfo.from_city }</td>
					 		<td rowspan="2" class="icon"><span class="from_to_icon"></span></td>
					 		<td class="font_18 b" width="35%" id="endCity" align="center">${orderInfo.to_city }</td>
					 	</tr>
					 	<tr line-height="20px">
					 		<td class="text_item" align="center" id="startTime">${orderInfo.from_time}</td>
					 		<td align="center" class="text_item" id="endTime">${orderInfo.to_time}</td>
					 	</tr>
					 	<tr>
					 		<td class="text_item" align="center" id="seatType" >${seatTypeMap[orderInfo.seat_type]}</td>
					 		<td></td>
					 		<td class="text_item" align="center">${fn:length(detailList)}张</td>
					 	</tr>
					 	<tr>
					 	<td colspan="5">
					 	<hr color=#888 size="1"/>	
					 	</td>
					 	</tr>
					 	<tr>
					 		
					 		<td colspan="3" id="orderId" class="text_item_black">订单号:${orderInfo.order_id }</td>
					 		
					 	</tr>
					 	<c:choose>
					 	<c:when test="${orderInfo.order_status eq '00' || orderInfo.order_status eq '99'}">
					 	</c:when>
					 	<c:otherwise>
					 		<tr>
						 		<td colspan="3" id="orderId" class="text_item_black">下单时间:${orderInfo.pay_time }</td>
						 	</tr>
					 	</c:otherwise>
					 	</c:choose>
					 </table>
					</dd>
				</dl>
				
				<dl class="order_item">
					
					<dt class="text_item_header">乘客信息</dt>
					
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
							 		
							 		<td class="text_item_black" width="45%">
							 			<input type="hidden" name="user_name" id="user_name" value="${detail.user_name}"> 
							 			姓名：${detail.user_name}
							 		</td>
							 		
							 		<td class="text_item_black" width="20%">${ticketTypeMap[detail.ticket_type]}
									</td>
									<c:if test="${!empty detailInfo.cp_pay_money and (detailInfo.cp_pay_money ne '0')}">
									<td class="text_item_black" width="20%">￥${detailInfo.cp_pay_money }</td>
									</c:if>
								</tr>
								<tr>
								
								
									<c:if test="${!empty detail.train_box && (!empty detail.seat_no)}"><td class="text_item" colspan="6">座位号：${detail.train_box}车${detail.seat_no}</td></c:if>
							 		
							 	</tr>
							 	<tr>
							 	
							 		<td class="text_item_black" colspan="3">
							 			${idsTypeMap[detail.ids_type]}：${detail.user_ids}
							 		</td>
							 		
						 		</tr>
						 	
						 		<tr>
						 		
						 			<td class="text_item_black" colspan="3">
						 				保险金额：<c:if test="${detail.bx_id eq '' || (empty detail.bx_id)}">0</c:if>
						 				<c:if test="${!empty detail.bx_id}"> ￥${detail.bx_pay_money}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<c:if test="${!empty detail.bx_code}">单号${detail.bx_code}</c:if>  </c:if>
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
				
				
				<dl class="order_status">
					<dt>
				
					<c:choose>
						<c:when test="${!empty detailInfo.refund_status}">
							${rsStatusMap[detailInfo.refund_status]}
							<c:if test="${detailInfo.refund_status eq '44'}">
								退款金额：￥${detailInfo.refund_money}
							</c:if>
						</c:when>
						<c:when test="${orderInfo.order_status eq '45'}">
							<span class="red"><em class="order_fail">&nbsp;&nbsp;&nbsp;&nbsp;</em>购票失败</span>
							<c:if test="${!empty rsList.user_remark and rsList.refund_type eq '3'}"><br><span class="text_item_black">失败原因：${outFailReasonMap[rsList.user_remark]}</span></c:if>
						</c:when>
						<c:when test="${orderInfo.order_status eq '99'}">
							<span class="red"><em class="order_fail">&nbsp;&nbsp;&nbsp;&nbsp;</em>${orderStatusMap[orderInfo.order_status] }</span>
						</c:when>
						<c:when test="${orderInfo.order_status eq '00'}">
							<span class="blue_icon"><em class="order_to_pay">&nbsp;&nbsp;&nbsp;&nbsp;</em>${orderStatusMap[orderInfo.order_status] }</span>
						</c:when>
						<c:when test="${orderInfo.order_status eq '22'}">
							<span class="blue_icon"><em class="order_to_pay">&nbsp;&nbsp;&nbsp;&nbsp;</em>正在购票</span>
						</c:when>
						<c:when test="${orderInfo.order_status eq '44'&& (!empty rsList.refund_status)}">
							<c:choose>
								<c:when test="${rsList.refund_status eq '00' || rsList.refund_status eq '11' || rsList.refund_status eq '22' || rsList.refund_status eq '33'}">
									<span class="blue_icon"><em class="order_to_pay">&nbsp;&nbsp;&nbsp;&nbsp;</em>${rsStatusMap[rsList.refund_status]}</span>
								</c:when>
								<c:when test="${rsList.refund_status eq '44'}">
									<span class="green"><em class="order_succeed">&nbsp;&nbsp;&nbsp;&nbsp;</em>${rsStatusMap[rsList.refund_status]}</span><br/>
									<span class="grey">退款金额：<span class="org">
										￥<fmt:formatNumber value="${rsList.refund_money}" type="currency" pattern="#0.0"/>
									</span></span>
								</c:when>
								<c:when test="${rsList.refund_status eq '55'}">
									<span class="red"><em class="order_fail">&nbsp;&nbsp;&nbsp;&nbsp;</em>${rsStatusMap[rsList.refund_status]}
									<c:if test="${!empty rsList.our_remark}">(${rsList.our_remark })</c:if></span>
								</c:when>
								<c:otherwise>
									<span class="green"><em class="order_succeed">&nbsp;&nbsp;&nbsp;&nbsp;</em>购票成功</span>
								</c:otherwise>
							</c:choose>
						</c:when>
						
						<c:otherwise>
							<span class="green">${orderStatusMap[order.order_status]}</span>
						</c:otherwise>
					</c:choose>
					</dt>
				</dl>
			</div>
			
			<c:if test="${orderInfo.order_status eq '99' || orderInfo.order_status eq '00'}">
				<div class="ticket_ser_w">
					<input type="button" value="重新购票" id="btnSubmit" class="ticket_ser" onclick="submitData('${orderInfo.from_city}', '${orderInfo.to_city}', '${orderInfo.travel_time}');">	
				</div>
			</c:if>
			<c:if test="${orderInfo.order_status eq '44' && (!empty rsList.refund_status) && (rsList.refund_status eq '55')}">
				<input type="hidden" name="order_id" value="${orderInfo.order_id }"/>
				<input type="hidden" id="old_refund_status" value="${rsList.refund_status}" />
				<div class="ticket_ser_w">
					<input type="button" value="重新申请退款" id="btnRefund" class="ticket_ser" onclick="refund('${orderInfo.order_id}', '${detailInfo.refund_status}');">	
				</div>
				
			</c:if>
			</form>
		</section>
		
	</div>
	


</div>	
</body>
</html>		
