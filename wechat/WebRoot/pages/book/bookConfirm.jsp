<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ page import="com.l9e.client.ResponseHandler"%>
<%@ page import="com.l9e.client.RequestHandler"%>
<%@page import="java.util.TreeMap"%>
<%@ page import="com.l9e.client.TenpayHttpClient"%>
<%@page import="java.util.SortedMap"%>
<%@page import="com.l9e.weixin.util.Sha11Util"%>
<%@ page import="com.l9e.weixin.util.TenpayUtil"%>
<%@ page import="com.l9e.weixin.util.MD5Util"%>
<%@ page import="java.io.BufferedWriter"%>
<%@ page import="java.io.BufferedOutputStream"%>
<%@ page import="java.io.OutputStream"%>
<%@ page import="com.google.gson.Gson"%>
<%@ include file="../config/config.jsp"%>

<%
	//=================================
	//jsapi接口
	//=================================
	//初始化

	RequestHandler reqHandler = new RequestHandler(request, response);
	TenpayHttpClient httpClient = new TenpayHttpClient();

	TreeMap<String, String> outParams = new TreeMap<String, String>();
	//初始化 
	reqHandler.init();
	reqHandler.init(APP_ID, APP_SECRET, APP_KEY, PARTNER_KEY);

	//当前时间 yyyyMMddHHmmss
	String currTime = TenpayUtil.getCurrTime();
	//8位日期
	String strTime = currTime.substring(8, currTime.length());
	//四位随机数
	String strRandom = TenpayUtil.buildRandom(4) + "";
	//10位序列号,可以自行调整。
	String strReq = strTime + strRandom;
	//订单号，此处用时间加随机数生成，商户根据自己情况调整，只要保持全局唯一就行
	String out_trade_no = (String)request.getAttribute("order_id");
	String total_fee = (String)request.getAttribute("totalFee");

	//获取提交的商品价格
	String order_price = request.getParameter("order_price");
	//获取提交的商品名称
	String product_name = request.getParameter("product_name");

	//设置package订单参数
	SortedMap<String, String> packageParams = new TreeMap<String, String>();
	packageParams.put("bank_type", "WX"); //支付类型   
	packageParams.put("body", "火车票"); //商品描述   
	packageParams.put("fee_type", "1"); //银行币种
	packageParams.put("input_charset", "GBK"); //字符集    
	packageParams.put("notify_url", NOTIFY_URL); //通知地址  
	packageParams.put("out_trade_no", out_trade_no); //商户订单号  
	packageParams.put("partner", PARTNER); //设置商户号
	packageParams.put("total_fee", total_fee); //商品总金额,以分为单位
	packageParams.put("spbill_create_ip", request.getRemoteAddr()); //订单生成的机器IP，指用户浏览器端IP

	//获取package包
	String packageValue = reqHandler.genPackage(packageParams);
	String noncestr = Sha11Util.getNonceStr();
	String timestamp = Sha11Util.getTimeStamp();

	//设置支付参数
	SortedMap<String, String> signParams = new TreeMap<String, String>();
	signParams.put("appid", APP_ID);
	signParams.put("noncestr", noncestr);
	signParams.put("package", packageValue);
	signParams.put("timestamp", timestamp);
	signParams.put("appkey", APP_KEY);
	//生成支付签名，要采用URLENCODER的原始值进行SHA1算法！
	String sign = Sha11Util.createSHA1Sign(signParams);

	//增加非参与签名的额外参数
	signParams.put("paySign", sign);
	signParams.put("signType", "sha1");
%>

<!doctype html>
<html>
	<head>
		
		<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
		<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
		<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
		<meta charset="utf-8">
		<title>酷游旅游</title>
		<meta name="viewport"
			content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
		<meta name="keyword"
			content="19e,便民服务,数字便民,移动客户端,手机客户端,话费充值,手机充值,游戏充值,QQ充值,机票预订,水费,电费,煤气费,取暖费,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
		<meta name="description"
			content="提供19e数字便民移动终端下载,随时随地办理便民业务,支持移动客户端话费充值,手机充值,游戏充值,QQ充值,机票预订,水电煤缴费,有线电视缴费,供暖缴费,彩票,手机号卡,信用卡还款 ,医疗挂号,交通罚款缴纳,实物批发,酒店团购。">
		<link rel="stylesheet" type="text/css" href="/css/base.css">
		<link rel="shortcut icon" href="/images/favicon.ico"
			type="image/x-icon" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript" src="/js/artDialog.js"></script>
		<style type="text/css">
.text_item {
	font-size: 14px;
	font-family: Microsoft YaHei, calibri, verdana;
}

.input_hidden_border {
	font-size: 14px;
	width: 70px;
	border: 0;
	font-family: Microsoft YaHei, calibri, verdana;
}

.adult {
	line-height: 28px;
}

.wrap {
	position: relative;
}

td.icon {
	vertical-align: middle;
	width: 50%;
}
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
	var table = document.getElementById("train_ticket_list");       
	var count = table.rows.length;//行数   
	//alert(table.innerHTML);
	if( baoxian=="0" ){
		$("#meal_insurance").html("不购买保险");
		//document.getElementById('shouldPayMoney').innerHTML += ("应付金额:<strong>￥"+(Number(localStorage.getItem("seat_price"))*myobj.length)+"</strong>");
		document.getElementById('shouldPayMoney').innerHTML += ("应付金额:<strong>￥"+$("#total_money").text()+"</strong>"); 
	}else{
		//有几个乘客，买几份保险
		$("#meal_insurance").html('20*'+count+'=<em class="red">'+(20*count)+'</em>元');
		//document.getElementById('shouldPayMoney').innerHTML += ("应付金额:<strong>￥"+((Number(localStorage.getItem("seat_price"))+20)*myobj.length)+"</strong>");
		document.getElementById('shouldPayMoney').innerHTML += ("应付金额:<strong>￥"+$("#total_money").text()+"</strong>");
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
function change(val) {
		alert("选择的支付方式：" + val);
		if(val=="1"){
		WeixinJSBridge.invoke('getNetworkType',{},function(e){
    		// 在这里拿到e.err_msg，这里面就包含了所有的网络类型
    		alert(e.err_msg);
		});
		alert($.trim($("#package").val()));
			//document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
                //公众号支付
                $('a#order_submit').live('click',function(e){
						WeixinJSBridge.invoke('getBrandWCPayRequest',{
						"appId" : "<%=APP_ID%>","timeStamp" : "<%=timestamp%>", "nonceStr" : "<%=noncestr%>", "package" : "<%=packageValue%>","signType" : "SHA1", "paySign" : "<%=sign%>" 
						},function(res){
							WeixinJSBridge.log(res);
							// 返回 res.err_msg,取值
							// get_brand_wcpay_request:cancel 用户取消
							// get_brand_wcpay_request:fail 发送失败
							// get_brand_wcpay_request:ok 发送成功
							WeixinJSBridge.log(res.err_msg);
							alert(res.err_code+res.err_desc);
						});
				});
				WeixinJSBridge.log('yo~ ready.');
                                      
                //}, false)
            }
            if(val=="2") {
            	$('a#order_submit').click(function(e){
	            	var count = $(".adult:visible").length;
	            	var bankAbbr = "";
					var order_id = $("#order_id").val();
					if(confirm("确认前往支付该订单吗？")){
	            		window.location="/order/orderUpay.jhtml?order_id="+order_id+"&bankAbbr="+bankAbbr+"&productNum="+count;
	            	}
	            });
            }
            
            if(val=="0") {
            	$('a#order_submit').click(function(e){
            		alert("请选择支付方式");
            		return;
            	});
            }

}
//确认支付
function gotoPay(){
	var count = $(".adult:visible").length;
	//var bankAbbr = $("#bankAbbr").val();
	var payType=$.trim($("#payType").val());
	var appId = $.trim($("#appId").val());
	var package = $.trim($("#package").val());
	var nonceStr = $.trim($("#nonceStr").val());
	var timeStamp = $.trim($("#timeStamp").val());
	var paySign = $.trim($("#paySign").val());
	if(payType=="" || payType==null || payType=="0") {
		alert("请选择支付方式！");
		return;
	}
	var bankAbbr = "";
	var order_id = $("#order_id").val();
	if(confirm("确认前往支付该订单吗？")){
		//$("form:first").attr("action", "/order/orderCmpay.jhtml?order_id="+order_id+"&bankAbbr="+bankAbbr+"&productNum"+count);
		if(payType=="2"){
			window.location="/order/orderUpay.jhtml?order_id="+order_id+"&bankAbbr="+bankAbbr+"&productNum="+count;
		}
		if(payType=="1"){alert("paySign:" + paySign);
			document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
                   //公众号支付
                   jQuery('a#getBrandWCPayRequest').click(function(e){
						WeixinJSBridge.invoke('getBrandWCPayRequest',{
						"appId" : $.trim($("#appId").val()), //公众号名称，由商户传入
						"timeStamp" : $.trim($("#timeStamp").val()), //时间戳 这里随意使用了一个值
						"nonceStr" : $.trim($("#nonceStr").val()), //随机串
						"package" : $.trim($("#package").val()),
						//扩展字段，由商户传入
						"signType" : "SHA1", //微信签名方式:sha1
						"paySign" : $.trim($("#paySign").val()) //微信签名
						},function(res){
							WeixinJSBridge.log(res);
							// 返回 res.err_msg,取值
							// get_brand_wcpay_request:cancel 用户取消
							// get_brand_wcpay_request:fail 发送失败
							// get_brand_wcpay_request:ok 发送成功
							WeixinJSBridge.log(res.err_msg);
							alert(res.err_code+res.err_desc);
						});
					 });
				 WeixinJSBridge.log('yo~ ready.');
                                      
                 }, false)
		}
		//消息框	
		var dialog = art.dialog({
			id:'turntopay',
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
		
		localStorage.setItem("passengers",""); 
	}
	localStorage.removeItem("baoxian");
}
window.unonload=art.dialog({id:'turntopay'}).close();
</script>
	</head>

	<style type="text/css">
.screen {
	width: 480px;
	margin: 0 auto;
}
</style>

	<body>

		<div>

			<!-- start -->
			<div class="wrap">
				<header id="bar">
				<a onclick="window.location='/pages/book/bookInfoInput.jsp';"
					class="m19e_ret"></a>
				<h1>
					订单支付
				</h1>
				<a href="/pages/book/menuNew.jsp" class="m19e_home"></a>
				</header>
				<section id="order_result">

				<div class="order_res_box">
					<h2 style="height: 50px; line-height: 50px;">
						订单已提交
					</h2>
					<ul class="order_res_c">
						<li>
							<span>车次:</span><strong><em class="org">${orderInfo.train_no}</em>${orderInfo.from_station}</strong><span>(${orderInfo.from_time})</span>--
							<strong>${orderInfo.arrive_station}</strong><span>(${orderInfo.arrive_time})</span>
						</li>
						<li>
							<span>日期:</span>${orderInfo.travel_time}（${day }）
						</li>
						<li>
							<span>乘客:</span>
							<table
								style="width: 85%; margin: -10% 0 0 15%; padding: 0 0 0 0;"
								id="train_ticket_list">
								<c:forEach var="detailInfo" items="${detailList}"
									varStatus="idx">
									<tr class="adult">
										<td>
											<table
												style="width: 100%; margin: 0 0 0 0; padding: 0 0 0 0;">
												<tr>
													<td class="text_item">
														${detailInfo.user_name}
													</td>

													<td class="text_item">
														${ticketTypeMap[detailInfo.ticket_type]}
													</td>
												</tr>
												<tr>
													<td class="text_item">
														${idsTypeMap[detailInfo.ids_type]}
													</td>

													<td class="text_item">
														${detailInfo.user_ids}
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</c:forEach>
							</table>
						</li>
						<li>
							<span>坐席:</span>${seatTypeMap[orderInfo.seat_type]}
							</span>
						</li>
						<li>
							<span>保险:</span><span id="meal_insurance"></span>
						</li>
						<input type="hidden" id="order_id" value="${order_id }"
							name="order_id">
						<li>
							<span>订单号:</span>${order_id }
						</li>
						<li>
							<span>订单状态:</span><em class="red">等待支付</em>
						</li>
						<li>
							<span>应付金额:</span>￥
							<em class="org" id="total_money">${totalPay4Show}</em>
						</li>
						<input type="hidden" name="ip" id="ip" value="${ip}" />
						<input type="hidden" name="package" id="package"
							value='${package}' />
						<input type="hidden" name="pageSign" id="paySign"
							value="${paySign}" />
						<input type="hidden" name="timeStamp" id="timeStamp"
							value="${timeStamp}" />
						<input type="hidden" name="nonceStr" id="nonceStr"
							value="${nonceStr}" />
						<input type="hidden" name="appId" id="appId" value="${appId}" />
						<select name="payType" id="payType" style="width: 100%;"
							onchange="change(this.options[this.options.selectedIndex].value);">
							<option value="0" style="width: 100%;">
								请选择支付方式
							</option>
							<option value="1" style="width: 100%;">
								微信支付
							</option>
							<option value="2" style="width: 100%;">
								联动优势
							</option>
						</select>

					</ul>
				</div>
				<div>
					<br />
					<br />
					<br />
					<br />
					<br />
					<p align="center" id="bx_confirm">
						请在45分钟内支付，以免订单失效
					</p>
					<br />
				</div>

				<div class="order_foot">
					<div class="order_d" style="background: #d13b00;">
						<span class="order_pay" id="shouldPayMoney"></span>
					</div>
					<div class="order_d" style="background: #ff7200;">
						<span class="order_btn"><a href="javascript:void(0);"
							class="order_submit" id="order_submit">支付</a> </span>
					</div>
				</div>


				</section>
			</div>
			<!-- end -->

		</div>
	</body>
</html>
