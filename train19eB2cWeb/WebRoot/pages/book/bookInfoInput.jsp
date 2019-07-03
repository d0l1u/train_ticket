<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%     
	String path = request.getContextPath();     
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";     
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>19trip旅行</title>
<link rel="stylesheet" href="/css/default.css" type="text/css" />
<link rel="stylesheet" href="/css/reset-min.css" type="text/css" />
<link rel="stylesheet" href="/css/style.css" type="text/css"/>

<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/artDialog.js"></script>
<script type="text/javascript" src="/js/idCard.js"></script>
<script type="text/javascript" src="/js/json2.js"></script>
<script type="text/javascript" src="/js/jquery.form.js"></script>
<style type="text/css">
/****订单弹出框*****/
#drawBill{ position:fixed;_position: absolute;_top: expression(documentElement.scrollTop + 340 + "px");
	 background:#fff; width:400px; height:300px; border:1px solid #86CBFF; top:50%; left:50%; 
	 margin: -200px 0 0 -250px; overflow:hidden; z-index:999999; font-size:14px; color:#000; display:none;}
#drawBxBill{ position:fixed;_position: absolute;_top: expression(documentElement.scrollTop + 340 + "px");
	 background:#fff; width:400px; height:300px; border:1px solid #86CBFF; top:50%; left:50%; 
	 margin: -200px 0 0 -250px; overflow:hidden; z-index:999999; font-size:14px; color:#000; display:none;}
* html,* html body{background-image:url(about:blank);background-attachment:fixed} 
* html .ie6fixedTL{position:absolute;left:expression(eval(document.documentElement.scrollLeft));top:expression(eval(document.documentElement.scrollTop))} 
* html .ie6fixedBR{position:absolute;left:expression(eval(document.documentElement.scrollLeft+document.documentElement.clientWidth-this.offsetWidth)-(parseInt(this.currentStyle.marginLeft,10)||0)-(parseInt(this.currentStyle.marginRight,10)||0));top:expression(eval(document.documentElement.scrollTop+document.documentElement.clientHeight-this.offsetHeight-(parseInt(this.currentStyle.marginTop,10)||0)-(parseInt(this.currentStyle.marginBottom,10)||0)))} 
</style>
<script type="text/javascript" src="/js/dialog.js"></script>

<script type="text/javascript">
/**************省市区三级联动 start***************/
function selectCity(){
	$.ajax({
		url:"/buyTicket/queryGetCity.jhtml",
		type: "POST",
		data: {provinceid:$("#province_id").val()},
        dataType: "json",
		success: function(data){
			$("#city_id").empty(); 
	    	$("#city_id").append("<option value='' selected='selected'>请选择市</option>");
	    	$("#district_id").empty(); 
	    	$("#district_id").append("<option value='' selected='selected'>请选择区/县</option>");
	    	var obj = eval(data);
			$(obj).each(function(index){
				var val = obj[index];
				$("#city_id").append("<option value='"+val.area_no+"'>"+val.area_name+"</option>");
			});
		}
	});
}
function selectDistrict(){
	var url = "/buyTicket/queryGetArea.jhtml?cityid="+$("#city_id").val();
	$.post(url,function(data,status){
    	$("#district_id").empty(); 
    	$("#district_id").append("<option value='' selected='selected'>请选择区/县</option>");
    	var obj = eval(data);
		$(obj).each(function(index){
			var val = obj[index];
			$("#district_id").append("<option value='"+val.area_no+"'>"+val.area_name+"</option>");
		});
  	});
}
/**************省市区三级联动 end***************/




function timer(defSec){ 
	defSec = defSec-1;
	document.getElementById("drawBxBill_time").innerText = (parseInt(defSec)); 
    if(defSec <= 60 && defSec%10 ==0){
    	$.ajax({
			url:"/order/queryOrderResult.jhtml?order_id=" + $("#order_id").val(),
			type: "POST",
			cache: true,
			async: false,
			success: function(res){
    			if(res=='SUCCESS'){
    				var url = "/order/toOrderPay.jhtml?fpNeed=1&order_id=" + $("#order_id").val()
					window.location = encodeURI(url);
        		}else if(res.indexOf("FAILURE") != -1){
            		var error_info = res.replace("FAILURE","");
        			art.dialog.confirm('由于'+error_info+',您确认重新购票吗?',function(){
        				var url = "/buyTicket/bookIndex.jhtml";
        				window.location = encodeURI(url);
            		},function(){});
            	}
			}
		});
    }
    if(defSec <= 0){
    	var url="/order/orderComfirm.jhtml?order_id=" + $("#order_id").val() + "&totalPay4Show=" + $("#totalPay4Show").val()
		window.location = encodeURI(url);
    }else{
        window.setTimeout("timer("+defSec+")",1000);
    }
} 

</script>
</head>

<body>
<!--以下是头部logo部分start -->
<%@ include file="/pages/common/header.jsp"%>
<!--以下是头部logo部分end -->

<!--以下是头部步骤head_step部分 -->
<div class="head_step">
	<ul>
		<li class="on">填写订单</li>
		<li>在线支付</li>
		<li>等待出票</li>
	</ul>
</div>


<!--以下是infoInput正文内容部分 -->
<div id="infoInput_all">

<!--以下是infoInput左边内容部分 -->

	<div class="info_left">
	
<form id="trainForm" action="/order/createOrder.jhtml" method="post">
            	<input type="hidden" id="train_no" name="train_no" value="${trainCode}" />
            	<input type="hidden" id=from_city name="from_city" value="${startCity}" />
            	<input type="hidden" id="to_city" name="to_city" value="${endCity}" />
            	<input type="hidden" id="from_time" name="from_time" value="${travelTime} ${startTime}" />
            	<input type="hidden" id="to_time" name="to_time" value="${travelTime} ${endTime}" />
            	<input type="hidden" name="travelTime" id="travelTime" value="${travelTime}" />
            	<input type="hidden" name="out_ticket_type" id="out_ticket_type" value="11" />
            	<input type="hidden" name="ps_pay_money" value="20" />
            	<input type="hidden" name="wz_ext" id="wz_ext" />
            	
            	<input type="hidden" id="weather_goto" name="weather_goto" value="11" />
            	<input type="hidden" id="order_id" name="order_id"/>
            	<input type="hidden" id="totalPay4Show" name="totalPay4Show" />
            	<input type="hidden" name="fpNeed" id="fpNeed" />
            	<input type="hidden" name="create_order" id="create_order" value="${create_order}"/>	
<!--以下是infoInput车次信息部分 -->
    	<div class="message">
    		<h3 class="message_tit" style="line-height:40px;;"> 车次信息</h3>
            <table class="message_c">
            <tr>
            	<td width="12%"><strong>${trainCode}</strong></td>
            	<td width="14%"><span>${startCity}</span><br /><br />${startTime}</td>
            	<td width="10%">${travelTime}<br /> <b class="line_arrow"></b><br /></td>
            	<td width="14%"><span>${endCity}</span><br /><br />${endTime}</td>
            	<td width="47%" style="text-align:left; padding-left:20px;">
                    	<select name="seat_type" id="seat_type" style="display:none;">
                           	<c:forEach items="${seatInfoList}" var="seat" >
                           		<c:if test="${seat.seatSelect eq 'select'}">
                           			<option selected="selected"  value="${seat.seatType}">${seat.seatName}</option>
                           		</c:if>
                           		<c:if test="${seat.seatSelect eq 'unSelect'}">
                           			<option value="${seat.seatType}">${seat.seatName}</option>
                           		</c:if>
                           	</c:forEach>
                        </select>
            	<c:forEach items="${seatInfoList}" var="seat" >
                    <c:if test="${seat.seatSelect eq 'select'}">
                    	坐席：${seat.seatName}  
                    	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						车票单价：<span id="danjia_show" style="color:#f90;">￥${seat.price}</span> 元         	
		                <br /><br />
						   	
						<input type="hidden" id="danjia" name="danjia" value="${seat.price}" />
							<span id="wz_agree" style="display: none; font-size:14px;">
		                        <input type="checkbox" id="wz_chk" />
		                        <label for="wz_chk">&nbsp;若该坐席无座，我愿意接受无座票</label>
		                    </span>
		                    <span id="wz_agree1" style="display: none; font-size:14px;">
		                        <input type="checkbox" id="wz_chk1" />若该坐席无座，我愿意接受无座票
		                    </span>
		                    <span id="wz_agree2" style="display: none; font-size:14px;">
		                        <input type="checkbox" id="wz_chk2" />
		                        <label for="wz_chk2">&nbsp;若该坐席无座，我愿意接受无座票</label>
		                    </span>
                    </c:if>
                </c:forEach>
                <br /></td>
            </tr>  
            </table>
        </div>
<!--以下是infoInput乘客信息部分 -->
    	<div class="message">
    		<div class="message_tit" id="message_titBx">
    		<h3>乘客信息</h3>
			    <dl class="bx_select" id="bx_select">
					<dt id="bx_select_text">20元保65万</dt>
					<dd>
						<ul>
							<li><span>20元保65万</span></li>
							<li><span>10元保20万</span></li>
							<li><span>普通购票</span></li>
						</ul>
					</dd>
				</dl>
				<!-- 不购买保险的弹框start -->
				<div class="drawBill" id="drawBillBx" style="display:none;">
					<span class="dBill_off" style=" margin:0;" id="dBill_off" onclick="closeBx();"></span>
				    <div class="dBill_con">
				    	<div class="btn1" style="margin:15px 5px" id="upgrade_service">极速预订</div>
						<ul>	
				    	<li>您可专享：</li>
						<li>&nbsp;1.&nbsp;<b class="red">不支付</b>就能看到车厢座位信息</li>
						<li>&nbsp;2.&nbsp;优先处理订单</li>
						<li>&nbsp;3.&nbsp;需买交通意外险¥20/人</li>
						<li>&nbsp;4.&nbsp;出票失败有短信提醒</li>
						<li>&nbsp;5.&nbsp;提供人工退票服务</li>
				    	</ul>
				    </div>
				</div>
				<!-- 不购买保险的弹框end -->
            </div>
            
            <table class="table_list" cellpadding="0" cellspacing="0" id="train_ticket_list">
		        <tr>
			        <th width="13">&nbsp;</th>
			        <th width="25"></th>
			        <th width="70">类 型</th>
			        <th width="170"><b class="red">*</b>&nbsp;姓 名</th>
			        <th width="110"><b class="red">*</b>&nbsp;证件类型</th>
			        <th width="190"><b class="red">*</b>&nbsp;证件号码</th>
			        <th width="110">&nbsp;</th>
			        <th width="110">&nbsp;</th>
		        </tr>
                <tr class="adult" style="display:none;">
			        <td class="indexTr" id="index_index_source"></td>
			        <td><span class="delPerson" id="delPerson_index_source" ></span></td>
			        <td><span id="text_ticket_type_index_source">成人票</span><input type="hidden" name="bookDetailInfoList_source.ticket_type" value="0" id="ticket_type_index_source" class="ticket_type_select"/></td>
			        <td><input type="text" name="bookDetailInfoList_source.user_name" id="user_name_index_source" style="height:25px;"
                    		class="text text_name user_name_text" title="姓名" onblur="hideMsg(this);" onkeyup="value=value.replace(/(^\s*)|(\s*$)/g,'');"/></td>
			        <td>
			       	 	<select style="width:100px;height:25px;" name="bookDetailInfoList_source.ids_type" id="ids_type_index_source"  class="ids_type_select">
                        	<option value="2">二代身份证</option>
                        	<option value="3">港澳通行证</option>
                        	<option value="4">台湾通行证</option>
                        	<option value="5">护照</option>
                        </select> 
					</td>
			        <td><input type="text" style="width:170px;" class="text text_id idcard_text" name="bookDetailInfoList_source.user_ids" 
                    		id="user_ids_index_source" title="证件号码" onblur="hideMsg(this);hideNumMsg(this);" 
                    		onfocus="getNumbers(this,'idcard');" onkeyup="value=value.replace(/(^\s*)|(\s*$)/g,'');getNumbers(this,'idcard');" />
                    </td>
			        <td>
			        	<div class="savePaxDiv fl" style="display:none;"><input id="savePax_index_source" class="savePax_index_source" type="checkbox" checked="checked"/>
			        		<label for="savePax_index_source" style="cursor:hand;">保存常用乘客</label>
			        	</div>
			        	<span class="ableAdd" id="add_person_index_source"><span class="add_adult"><a href="javascript:void(0);">添加同行成人</a></span></span>
			        </td>
			        <td>
			        	
			        	<span class="addChildren" id="addChildren_index_source"><a href="javascript:void(0);">添加随行儿童</a></span>&nbsp;
			        	
			        </td>
		        </tr>
            </table>
            <!-- <div class="btn3_ok" id="btnSubmit">确认乘客信息</div> -->
              <p style="color:red;padding-left:20px;">*请认真核实乘客信息，确认后不能修改乘客信息</p>
              <input type="submit" id="btnSubmit" value="确认乘客信息" class="btn3_ok" title="*请认真核实乘客信息，确认后不能修改乘客信息"  />
        </div>
</form>

<!--以下是infoInput联系人信息部分 -->
<form id="trainLinkForm" action="/order/createOrderLink.jhtml" method="post">
    	<div class="message">
    		<h3 class="message_tit" style="line-height:40px; position:static;">联系人信息</h3> 
        <div class="message_pho">
        	<span class="message_co" style="width:250px;">
        	<b class="red">*</b>&nbsp;姓 名&nbsp;&nbsp;&nbsp;
        		<input type="text" style="font-size:14px;" id="link_name" name="link_name" value="请填写真实姓名" 
        			onfocus="if(this.value=='请填写真实姓名'){this.value='';this.style.color='#333';};" 
    				onblur="if(this.value==''||this.value=='请填写真实姓名'){this.value='请填写真实姓名';this.style.color='#ccc';}else{hideErrMsg('link_name');}; "
    				onkeyup="value=value.replace(/(^\s*)|(\s*$)/g,'');" />
    		</span>
    		<span class="message_co" style="width:380px;">
        	<b class="red">*</b>&nbsp;手机号码&nbsp;&nbsp;&nbsp;
        		<input type="text" style="font-size:14px;" class="input_p" id="link_phone" name="link_phone" value="请填写正确手机号码以接收出票通知短信"
                	onblur="if(this.value==''||this.value=='请填写正确手机号码以接收出票通知短信'){this.value='请填写正确手机号码以接收出票通知短信';this.style.color='#ccc';}else{hideErrMsg('link_phone');};  " 
                	onkeyup="value=value.replace(/(^\s*)|(\s*$)/g,''); getNumbers(this,'phone'); " 
                	onfocus="if(this.value=='请填写正确手机号码以接收出票通知短信'){this.value='';this.style.color='#333';}; getNumbers(this,'phone'); hideNumMsg(this);"/>
        	</span>
        </div>  
       </div>
         
        <!--以下是infoInput报销凭证部分 -->
    	<div class="message">
    		<h3 class="message_tit" style="line-height:40px;">报销凭证</h3> 
        <div class="message_apply">
        	<input type="checkbox" id="chk_fp" onclick="checkFp();" />&nbsp;邮寄发票
        	<span class="fp_item" style="display: none;color:#aaa;">（将在火车发车后第二个工作日由承保的保险公司用平信免费邮寄寄出）</span>
        	<table style="margin-top:20px;">
        		<tr class="fp_item" style="display:none;height:30px;">
                	<td style="width:110px; text-align:right;"><b class="red">*</b>&nbsp;收件人姓名：&nbsp;</td>
                	<td style="width:140px; text-align:left;">
                		<span><input type="text" class="link_text" style="width: 130px; height:20px; line-height:20px;" id="fp_receiver" onblur="hideErrMsg('fp_receiver');" name="fp_receiver" /></span>
                	</td>
                	<td style="width:163px; text-align:right;"><b class="red">*</b>&nbsp;手机号码：&nbsp;</td>
                	<td style="width:140px; text-align:left;">
                		<span><input type="text" class="link_text" style="width: 130px; height:20px; line-height:20px;" id="fp_phone" name="fp_phone" 
                				 onkeyup="getNumbers(this,'phone');" onblur="hideNumMsg(this); hideErrMsg('fp_phone');" /></span>
                	</td>
                </tr>
                <tr class="fp_item" style="display: none;height:30px;">
                	<td style="width:110px; text-align:right;"><b class="red">*</b>&nbsp;所在地区：&nbsp;</td>
                	<td colspan="3" style="text-align:left;">
						<select name="province_id" id="province_id" onchange="selectCity(); hideErrMsg('province_id');" style="width: 135px;">
							<c:forEach items="${province }" var="p">
								<option value="${p.area_no }">
										${p.area_name}
								</option>
							</c:forEach>
						</select>
						&nbsp;&nbsp;
						<select name="city_id" id="city_id" onchange="selectDistrict(); hideErrMsg('city_id');" style="width: 135px;">
							<c:choose>
								<c:when test="${empty city }">
									<option value="" selected="selected">请选择市</option>
								</c:when>
								<c:otherwise>
									<c:if test="${empty city_id}">
										<option value="" selected="selected">请选择市</option>
									</c:if>
									<c:forEach items="${city }" var="p">
										<option value="${p.area_no }">
												${p.area_name}
										</option>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</select>
						&nbsp;&nbsp;
						<select name="district_id" id="district_id" style="width: 135px;" onchange="hideErrMsg('district_id');">
							<c:choose>
								<c:when test="${empty district}">
									<option value="" selected="selected">请选择区/县</option>
								</c:when>
								<c:otherwise>
									<c:if test="${empty district_id}">
										<option value="" selected="selected">请选择区/县</option>
									</c:if>
									<c:forEach items="${district}" var="p">
										<option value="${p.area_no }">
												${p.area_name }
										</option>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</select>
                	</td>
                </tr>
                <tr class="fp_item" style="display: none;height:30px;">
                	<td style="width:110px; text-align:right;"></td>
                	<td colspan="3" style="text-align:left;">
                		<span><input type="text" class="link_text" style="width:433px;color:#aaa; height:20px; line-height:20px;" id="detail_address" name="detail_address" 
                		value="请填写详细地址，不需要重复填写省市区/县" onfocus="if(this.value=='请填写详细地址，不需要重复填写省市区/县'){this.value=''};this.style.color='#333'; " 
            			onblur="if(this.value==''||this.value=='请填写详细地址，不需要重复填写省市区/县'){this.value='请填写详细地址，不需要重复填写省市区/县';this.style.color='#aaa';}else{hideErrMsg('detail_address');}; "/></span>
            			<input type="hidden" id="fp_address" name="fp_address" />
                	</td>
                </tr>
                <tr class="fp_item" style="display: none;height:30px;">
                	<td style="width:110px; text-align:right;"><b class="red">*</b>&nbsp;邮编：&nbsp;</td>
                	<td colspan="3" style="text-align:left;">
                		<span><input type="text" class="link_text" style="width: 130px; height:20px; line-height:20px;" id="fp_zip_code" name="fp_zip_code" onblur="hideErrMsg('fp_zip_code');" /></span>
                	</td>
                </tr>
        	</table>
        </div>  
        </div>
        <div class="info_last">
        <span><input type="checkbox" id="chk_bxconfirm" checked="checked"/></span>
        <a href="/pages/guide/wufu.jsp" target="_blank">《火车票线下代购服务协议》</a> 和<a href="javascript:void(0);" onclick="window.open('http://www.unionlife.com.cn/tab674/');">《保险说明》</a>
        <!-- <div class="btn4" id="btnLinkSubmit">同意以上协议条款，提交订单</div> --><br/>
        <input type="submit" class="btn4" value="同意以上协议条款，提交订单" />
        </div>
</form>
</div>

<!--以下是infoInput右边内容部分 -->
	<div class="info_right">
    <!--以下是infoInput上边支付信息内容部分 -->
    	<div class="info_right_top">
        	<h3 class="info_right_tit">支付信息</h3>
        	<ul>
        		<c:forEach items="${seatInfoList}" var="seat" >
                <c:if test="${seat.seatSelect eq 'select'}">
        		<li>
                <span class="tit_left">票价金额</span>
                <span class="tit_right" id="ticket_money">￥${seat.price}×1</span>
                </li>
        		<li>
                <span class="tit_left">保险金额</span>
                <span class="tit_right" id="bx_money">￥20×1</span>
                </li>
        		<li style="border:0">
                <span class="tit_left"><b style="font-size:16px;">应付总额</b></span>
                <span class="tit_right">
                <b class="font_b" id="sum_money">￥${seat.price + 20}</b></span>
                </li>
                </c:if></c:forEach>
        	</ul>
        </div>
    <!--以下是infoInput下边温馨提醒内容部分 -->
    	<div class="info_right_down">
        	<h3 class="info_right_tit">温馨提醒</h3>
        	<ul class="warnul">
            <li>1.&nbsp;2014年3月1日起，铁路互联网购票实行实名核验，用户只能为通过实名核验的乘客在线购票。</li>
            <li>2.&nbsp;支付成功后，我们会及时短信通知您购票 结果；您也可以到订单中心查看出票况。</li>
			<li>3.&nbsp;出票失败后，无特殊情况会在2个工作日全额退款至您的支付账户中。</li>
			<li>4.&nbsp;填写乘客信息后，请认真核实。点击确认后不能修改乘客信息。</li>
			</ul>
        </div>
    </div>
</div>
<!-- 不购买保险的弹框 -->
<div id="drawBill" class="drawBill">
	<div class="draw_icon"></div>
	<p>正在提交订单，请稍后...</p>
	<input type="hidden" id="land_off" />
	<input type="hidden" id="land_on" />
</div>

<!-- 购买保险的弹框 -->
<div class="drawBill" id="drawBxBill"> 
	<div class="draw_icon"></div>
	<p>尊敬的乘客您好，我们正在努力为您出票，请您耐心等待，请稍后...</p>
    <p><span id="drawBxBill_time">21</span><span>&nbsp;秒</span></p>
    <input type="hidden" id="landBx_off" />
	<input type="hidden" id="landBx_on" />
</div>




<!-- footer start -->
<%@ include file="/pages/common/footer.jsp"%>
<!-- footer end -->

    <div id="tip" class="tip_wrap" style="display: none;"><div class="tip"><b></b><span class="errMsg"></span></div></div>
    <div id="num_tip" class="num_tip_wrap" style="display: none;"><div class="tip"><b></b><span class="numMsg"></span></div></div>
<script type="text/javascript">
	

	var jsonLinkList = JSON.stringify(${jsonLinkList})+"";
	var linkJsonInfo = "";	//jsonLinkList解析后的json集合
	var linkList = ${jsonList}+"";
	
	var param_product_id = "BX_20";	//传递给后台的保险ID值
	var baoxian = "20";//保险单价
	
    $(document).ready(function(){
    	/*************以下是鼠标移上bookInfoInput页面左侧始终在页面上方******************/
    	var mydis=$('.info_right_top').offset().top;
    	$(window).scroll(function(e) {
            var dis=$(window).scrollTop();	
    		if(dis>mydis){
        		$('.info_right_top').css('position','fixed').css('top','10px');
        		$('.info_right_down').css('position','fixed').css('top','244px');
        	}else{
        		$('.info_right_top').css('position','static');
        		$('.info_right_down').css('position','static');
        	}
        });	

    	showExtWz();
    	
    	//排列常用乘客信息展示
   		//passengerShow();
   		//更多
		$("#ser-more").click(function(){
			if($(this).attr("class")=="down-arrow"){
				$(".pass_hidden").show();
				$(this).attr("class","up-arrow");
			}else{
				$(".pass_hidden").hide();
				$(this).attr("class","down-arrow");
			}
		});
    	//交通意外险选框触发
    	$(".bx_select").each(function(){
   			var s=$(this);
   			var z=parseInt(s.css("z-index"));
   			var dt=$(this).children("dt");
   			var dd=$(this).children("dd");
   			var _show=function(){dd.slideDown(1);dt.addClass("cur");s.css("z-index",z+1);};   //展开效果
   			var _hide=function(){dd.slideUp(1);dt.removeClass("cur");s.css("z-index",z);};    //关闭效果
   			dt.click(function(){dd.is(":hidden")?_show():_hide();});
   			dd.find("span").click(function(){
       			dt.html($(this).html());
       			//下拉框式选择保险
       			if($(this).html()=="普通购票"){
       				param_product_id = 'BX_NO';
       				baoxian = 0;
       				$("#drawBillBx").show();
           		}else if($(this).html()=="10元保20万"){
           			param_product_id = 'BX_10';
           			baoxian = 10;
               	}else{
           			param_product_id = 'BX_20';
           			baoxian = 20;
                }
       			addPerson();
       			_hide();
       		});
       		//选择效果（如需要传值，可自定义参数，在此处返回对应的“value”值 ）
   			$("body").click(function(i){ !$(i.target).parents(".bx_select").first().is(s) ? _hide():"";});
   		});
		//升级VIP服务
        $("#upgrade_service").click(function(){
        	$(".bx_select dt").html("20元保65万");
        	param_product_id = 'BX_20';
        	$("#drawBillBx").hide();
        });

		//var mapper = eval('('+'${seatPrizeMapper}'+')');
     	 
     	 //imagePreview();
     	 
     	 //异常情况加载坐席价格
     	 /**
     	 if($("#seat_type").val()!=""){
     	 	var key = "seatType" + $("#seat_type").val();
     	 	var price = mapper[key];
			$("#danjia_show").text(price);
			$("#danjia").val(price);
     	 }*/
     
		//行变色
		/**
		$(".table_list tr.adult,.table_list tr.child").live("mouseover", function(){
			$(this).css('background-color','#daeff8').siblings().css("background-color","");
		});	*/
		
		//提交乘客信息
		/**
		$(".add_pass").click(function(){
			$(".adult:visible").each(function(){
				if($(this).attr("id").indexOf("name")>0){
					name = $("#"+$(this).attr("id")).val();
				};
				if($(this).attr("id").indexOf("ids")>0){
					card_num = $("#"+$(this).attr("id")).val();
				};
			});

			$.ajax({
    			url:"/bookInfo/addPassenger.jhtml",
    			type: "POST",
    			cache: false,
    			success: function(data){
    			}
    		});
		})
		*/
		//添加通行成人
		$(".add_adult").live("click",function(){
			var count = $(".adult:visible,.child:visible").length;
			if(count>=5){
				//dialogAlter("一个订单最多可代购5张票！");
				$("#addChildren_"+count).hide();
				$("#addChildren_5").hide();
				return;
			}
			var isValid=true;
			$(".adult:visible :input:text").each(function(){
				if($.trim($(this).val())==""){
					$(this).focus();
					isValid=false;
					showErrMsg($(this).attr("id"), "110px", "请输入"+$(this).attr("title")+"！");
					return false;
				}else if($(this).attr("title")=="姓名" && !checkName($.trim($(this).val()))){
					$(this).focus();
					isValid=false;
					showErrMsg($(this).attr("id"), "110px", "请填写姓名！");
					return false;
				}else if($(this).attr("title")=="证件号码" 
					&& $(this).parents("tr").find(".ids_type_select").val()=="2"
						&& !valiIdCard($.trim($(this).val()))){
						$(this).focus();
						isValid=false;
						showErrMsg($(this).attr("id"), "180px", "请填写正确的二代身份证号！");
						return false;
				}else{
					hideErrMsg($(this).attr("id"));
				}
			});
			if(!isValid){
				return;
			}
			var replaceStr = "bookDetailInfoList["+count+"]";
			//alert("aaaaa---"+$(".adult:hidden").html());
			var html = $(".adult:hidden").html().replace(/index_source/g, count+1);
			html = html.replace(/bookDetailInfoList_source/g, replaceStr);
			$("<tr class=\"adult\">" + html + "</tr>").css("background-color","").appendTo("#train_ticket_list").show();
			if(count<4){
				addChildrenDisplay();
			}
			var newCount = $(".adult:visible,.child:visible").length;
			for(var i=1;i<=newCount;i++){
				if($("#ticket_type_"+i).val()=="1"){//儿童票
					$("#addChildren_"+i).hide();
					$("#user_name_"+i).prop("disabled", true);//设置为只读属性
					$("#ticket_type_"+i).prop("disabled", true);//设置为只读属性
					$("#ids_type_"+i).prop("disabled", true);//设置为只读属性
					$("#user_ids_"+i).prop("disabled", true);//设置为只读属性
				}else{
					$("#addChildren_"+i).show();
					$("#user_name_"+i).prop("disabled", false);//取消只读属性
					$("#ticket_type_"+i).prop("disabled", false);//取消只读属性
					$("#ids_type_"+i).prop("disabled", false);//取消只读属性
					$("#user_ids_"+i).prop("disabled", false);//取消只读属性
				}
				if(newCount == 5 && $("#ticket_type_"+i).val()=="0"){
					$("#addChildren_"+i).hide();
				}
			}
			addPerson();
			//兼容IE
			$("#add_person").attr("style","");
		}).trigger("click");

		//是否显示“添加同行成人”按钮:只有最后一个显示;并且更换右边的票价值以及保险值
		function addPerson(){
			var newCount = $(".adult:visible,.child:visible").length;
			for(var i=1;i<=newCount;i++){
				if(i<5){
					for(var j=1;j<=i;j++){
						$("#add_person_"+j).attr("class","ableAdd");//添加同行成人
						//alert($.trim($("#ticket_type_"+j).val()));
						if($.trim($("#ticket_type_"+j).val())=="0"){//有儿童票
							$("#addChildren_"+j).show();//“添加随行儿童”显示
							$("#user_name_"+(j)).prop("disabled", false);//取消只读属性
							$("#ticket_type_"+(j)).prop("disabled", false);//取消只读属性
							$("#ids_type_"+(j)).prop("disabled", false);//取消只读属性
							$("#user_ids_"+(j)).prop("disabled", false);//取消只读属性
						}else{
							$("#addChildren_"+j).hide();//“添加随行儿童”不显示
							$("#user_name_"+(j)).prop("disabled", true);//设置为只读属性
							$("#ticket_type_"+(j)).prop("disabled", true);//设置为只读属性
							$("#ids_type_"+(j)).prop("disabled", true);//设置为只读属性
							$("#user_ids_"+(j)).prop("disabled", true);//设置为只读属性
						}
					}
				}else if(i==5){
					for(var j=1;j<=i;j++){
						$("#add_person_"+j).attr("class","unableAdd");
						$("#addChildren_"+j).hide();//“添加随行儿童”不显示
					}
				}
			}
			var danjia = $("#danjia").val();//车票单价
			
			var ticket_money = '￥'+danjia+'×'+newCount;
			$("#ticket_money").text(ticket_money);
			var bx_money = '￥'+baoxian+'×'+newCount;
			$("#bx_money").text(bx_money);
			var sum_money =((parseFloat(danjia)+parseFloat(baoxian))*parseFloat(newCount));   
			$("#sum_money").text('￥'+sum_money);
		}
		
		//添加随行儿童显示
		function addChildrenDisplay(){
			var count = $(".adult:visible,.child:visible").length;
			var replaceStr = "bookDetailInfoList["+count+"]";
			var num = count+1;
			//var html = $(".adult:hidden").html().replace(/index_source/g, num);
			//html = html.replace(/bookDetailInfoList_source/g, replaceStr);
			for(var i=0;i<=count;i++){
				var name = $("#user_name_"+i).val();
				var ticket_type = $("#ticket_type_"+i).val();
				var user_ids = $("#user_ids_"+i).val();
				if(name != "" && user_ids!="" && ticket_type=="0"){//有儿童票
					$("#addChildren_"+i).show();//“添加随行儿童”显示
					$("#user_name_"+(i)).prop("disabled", false);//取消只读属性
					$("#ticket_type_"+(i)).prop("disabled", false);//取消只读属性
					$("#ids_type_"+(i)).prop("disabled", false);//取消只读属性
					$("#user_ids_"+(i)).prop("disabled", false);//取消只读属性
				}else{
					$("#addChildren_"+i).hide();//“添加随行儿童”不显示
					$("#user_name_"+(i)).prop("disabled", true);//设置为只读属性
					$("#ticket_type_"+(i)).prop("disabled", true);//设置为只读属性
					$("#ids_type_"+(i)).prop("disabled", true);//设置为只读属性
					$("#user_ids_"+(i)).prop("disabled", true);//设置为只读属性
				}
			}
			//兼容IE
			$("#add_person").attr("style","");
		}
		
		//添加随行儿童
		$(".addChildren").live("click", function(){
			var id = $(this).attr("id").split('_')[1];//addChildren_1
			var name = $("#user_name_"+id).val(); 
			var ticket_type = "1";
			var ids_type = $("#ids_type_"+id).val();
			var user_ids = $("#user_ids_"+id).val();
			if(name.length==0 || name == null){
				$("#user_name_"+id).focus();
				showErrMsg("user_name_"+id, "120px", "请输入乘客姓名！");
				return;
			}else if(user_ids.length==0 || user_ids == null){
				$("#user_ids_"+id).focus();
				showErrMsg("user_ids_"+id, "150px", "请输入乘客身份证号码！");
				return;
			}
			var count = $(".adult:visible,.child:visible").length;
			var replaceStr = "bookDetailInfoList["+count+"]";
			if(count==4){
				for(var i=1;i<=count;i++){
					$("#addChildren_"+i).hide();
				}
			}
			if(count>=5){
				return;
			}
			var num = parseInt(id)+1;
		
			if(parseInt(id) <= count){
				num = parseInt(count)+1;
			}
			var html = $(".adult:hidden").html().replace(/index_source/g, num);
			html = html.replace(/bookDetailInfoList_source/g, replaceStr);
			$("<tr class=\"adult\">" + html + "</tr>").css("background-color","")
				.appendTo("#train_ticket_list").show();
			if(num==5){
				$("#addChildren_"+num).hide();
			}
			if(count==4){
				for(var i=1;i<=count;i++){
					$("#addChildren_"+i).hide();
					
				}
			}
			//将后续的乘客index_source加1（往下移一行）
			var next = parseInt(id)+2;
			var count1 = parseInt(count)+1;
			if(parseInt(id) < count){
				if(count1>5){
					return;
				}
				if(count1==4){
					for(var i=1;i<=count1;i++){
						$("#addChildren_"+i).hide();
					}
				}
				for(var i=count1;i>=next;i--){
					//得到原本第parseInt(id)+1行的数据，将此行数据往下移一行
					if(i==3){
						$("#index_"+(i-1)).parent().html().replace(/2/g,"3");
					}else if(i==4){
						$("#index_"+(i-1)).parent().html().replace(/3/g,"4");
					}else if(i==5){
						$("#index_"+(i-1)).parent().html().replace(/4/g,"5");
					}
					var userName = $("#user_name_"+(i-1)).val();
					$("#user_name_"+(i)).val(userName);
					$("#ticket_type_"+(i)).val($("#ticket_type_"+(i-1)).val());
					$("#ids_type_"+(i)).val($("#ids_type_"+(i-1)).val());
					$("#user_ids_"+(i)).val($("#user_ids_"+(i-1)).val());
					if($("#ticket_type_"+(i)).val()=='1'){
						$("#text_ticket_type_"+(i)).text("儿童票");
					}else{
						$("#text_ticket_type_"+(i)).text("成人票");
					}
					toggleTip(i);
				}
			}
			
			$("#user_name_"+(parseInt(id)+1)).val(name);
			$("#ticket_type_"+(parseInt(id)+1)).val("1");
			$("#ids_type_"+(parseInt(id)+1)).val(ids_type);
			$("#user_ids_"+(parseInt(id)+1)).val(user_ids);
			$("#text_ticket_type_"+(parseInt(id)+1)).text("儿童票");
			
			$("#user_name_"+(parseInt(id)+1)).prop("disabled", true);//设置为只读属性
			$("#ticket_type_"+(parseInt(id)+1)).prop("disabled", true);//设置为只读属性
			$("#ids_type_"+(parseInt(id)+1)).prop("disabled", true);//设置为只读属性
			$("#user_ids_"+(parseInt(id)+1)).prop("disabled", true);//设置为只读属性
			$("#child_tip").show();//添加随行儿童温馨提示--显示
			//changeHeight(130);//增加100px整体高度
			//若为儿童票，则不显示“添加随行儿童”；若为成人票，显示“添加随行儿童按钮”
			if(count<4){
				addChildrenDisplay();
			}
			var newCount = $(".adult:visible,.child:visible").length;
			for(var i=1;i<=newCount;i++){
				if($("#ticket_type_"+i).val()=="1"){//儿童票
					$("#addChildren_"+i).hide();
					$("#user_name_"+i).prop("disabled", true);//设置为只读属性
					$("#ticket_type_"+i).prop("disabled", true);//设置为只读属性
					$("#ids_type_"+i).prop("disabled", true);//设置为只读属性
					$("#user_ids_"+i).prop("disabled", true);//设置为只读属性
				}else{
					$("#addChildren_"+i).show();
					$("#user_name_"+i).prop("disabled", false);//取消只读属性
					$("#ticket_type_"+i).prop("disabled", false);//取消只读属性
					$("#ids_type_"+i).prop("disabled", false);//取消只读属性
					$("#user_ids_"+i).prop("disabled", false);//取消只读属性
				}
				if(newCount == 5 && $("#ticket_type_"+i).val()=="0"){
					$("#addChildren_"+i).hide();
				}
			}
			addPerson();
			//兼容IE
			$("#add_person").attr("style","");
			//$(".savePaxDiv").attr("style","padding-top:4px;*+padding-top:0px;_padding-top:0px;");
		});
		
		//删除乘客
		$(".delPerson").live("click", function(){
			var id = $(this).attr("id").split('_')[1];//delPerson_1     id=1
			var count = $(".adult:visible,.child:visible").length;
			if($(".adult:visible,.child:visible").length==1){
				removePaxAttr($("#user_name_1").val(), $("#user_ids_1").val());//删除时，乘客信息变为不选中，背景颜色去掉
				$("#user_name_1").val("");
				$("#user_ids_1").val("");
				$("#ticket_type_1").val("0");
				$("#ids_type_1").val("2");
				return false;
			}
			var name = "";
			var card_num = "";
			$(this).parents("tr").find(":text").each(function(){
				if($(this).attr("id").indexOf("name")>0){
					name = $("#"+$(this).attr("id")).val();
				};
				if($(this).attr("id").indexOf("ids")>0){
					card_num = $("#"+$(this).attr("id")).val();
				};
			});
			if($("#ticket_type_"+id).val()=="0"){//成人票
				for(var i=parseInt(id)+1;i<=count;i++){//删除乘客的同事删除该乘客的随行儿童
					if($("#user_name_"+i).val()==name && $("#user_ids_"+i).val()==card_num && $("#ticket_type_"+i).val()=="1"){
						removePaxAttr($("#user_name_"+i).val(), $("#user_ids_"+i).val());
						$("#delPerson_"+i).parents("tr").remove();
						$("#delPerson_"+i).parents("tr").find(":text").each(function(){
							hideErrMsg($("#delPerson_"+i).attr("id"));
						});
					}
				}
				removePaxAttr(name,card_num);//若该乘客为常用联系人，删除乘客时同时“常用联系人”不选中，清除背景色
			}
			
			//删除同行乘客的对应选框清空
			var countAfter = $(".adult:visible,.child:visible").length;
			if(countAfter<=1){//一行数据时，清空文本框内容
				$("#user_name_1").val("");
				$("#user_ids_1").val("");
				$("#ticket_type_1").val("0");
				$("#ids_type_1").val("2");
			}else{//多行数据时，删除该行数据
				$(this).parents("tr").remove();
			}
			//重新设置index
			$(".adult:visible,.child:visible").each(function(index){
				var newIndex = index+1;
				$(this).html($(this).html().replace(/bookDetailInfoList\[\d+\]/g, "bookDetailInfoList["+index+"]"));
					//.find(".indexTr").html(newIndex);
				$(this).html($(this).html().replace(/\_\d/g, "_"+newIndex));
				toggleTip(newIndex);//判断是否显示“儿童票温馨提示”
			});	
			
			addPerson();
			//兼容IE
			$("#add_person").attr("style","");
			if($(".adult:visible,.child:visible").length>1){
				addChildrenDisplay();
			}
		});
		
		function showExtWz(){
			if($("#seat_type").val()=='8'){//硬座显示无座备选
				document.getElementById('wz_agree').style.display = "inline-block"; 
			}else{
				document.getElementById('wz_agree').style.display = "none"; 
			}
			if($("#seat_type").val()=='2'){//一等座显示无座备选
				document.getElementById('wz_agree1').style.display = "inline-block"; 
			}else{
				document.getElementById('wz_agree').style.display = "none"; 
			}
			if($("#seat_type").val()=='3'){//二等座显示无座备选
				document.getElementById('wz_agree2').style.display = "inline-block"; 
			}else{
				document.getElementById('wz_agree2').style.display = "none"; 
			}
		}
		
		//无座选择
		function selectWz(){
			if($("#wz_agree:visible").length>0 && $("#wz_chk").attr("checked")){
				$("#wz_ext").val("1");
			}else if($("#wz_agree1:visible").length>0 && $("#wz_chk1").attr("checked")){
				$("#wz_ext").val("1");
			}else if($("#wz_agree2:visible").length>0 && $("#wz_chk2").attr("checked")){
				$("#wz_ext").val("1");
			}else{
				$("#wz_ext").val("0");
			}
		}

		/**
		 * 获取n天后的日期
	     */        
	     function adddays(n) {   
	    	 var newdate=new Date();   
	    	 var newtimems=newdate.getTime()+(n*24*60*60*1000);   
	    	 newdate.setTime(newtimems);   
	    	 var month = newdate.getMonth()+1;
	    	 month<10 ? month='0'+month : month=month;
	    	 //只得到年月日的   
	    	 var newDay = newdate.getFullYear()+"-"+month+"-"+newdate.getDate();
	    	 return newDay;
	    }  
	    
/**提交订单(检查联系人信息和报销凭证信息)start**/
 		var optionsLink = {
			   target: '#outputLink',          //把服务器返回的内容放入id为output的元素中    
			   beforeSubmit: showLinkRequest,
			   success: showLinkResponse,      //提交后的回调函数
			   dataType : 'json',
			   timeout: 5000,               //限制请求的时间，当请求大于3秒后，跳出请求
			   failure : function(xhr,msg){ 
					alert("处理超时请重新提交"); 
					return false;
			   } 
		}
 		//提交前的执行函数	
		function showLinkRequest(){
 			if($("#btnSubmit").attr("disabled")!="disabled"){
				alert("请先点击'确认乘客信息'按钮！");
				$("#btnSubmit").focus();
				return false;
			}
			
 			var baoxiao = false;//全局
			var linkName = false;
			var linkPhone = false;
			
			//验证联系人信息的数据
			if($.trim($("#link_name").val())=="" || $.trim($("#link_name").val())=="请填写真实姓名"){
				$("#link_name").focus();
				showErrMsg("link_name", "110px", "请填写联系人！");
				linkName = false;
				return false;
			}else if(!checkName($.trim($("#link_name").val()))){
				$("#link_name").focus();
				showErrMsg("link_name", "150px", "请填写正确的联系人！");
				linkName = false;
				return false;
			}else{
				hideErrMsg("link_name");
				linkName = true;
			}

			if($.trim($("#link_phone").val())=="" || $.trim($("#link_phone").val())=="请填写正确手机号码以接收出票通知短信"){
				$("#link_phone").focus();
				showErrMsg("link_phone", "90px", "请填写手机！");
				linkPhone = false;
				return false;
			}else if(!/^1(3[\d]|4[57]|5[\d]|8[\d]|7[08])\d{8}$/g.test($.trim($("#link_phone").val()))){
				$("#link_phone").focus();
				showErrMsg("link_phone", "150px", "请填写正确的手机号！");
				linkPhone = false;
				return false;
			}else{
				hideErrMsg("link_phone");
				linkPhone = true;
			}


			//判断是否勾选同意协议
			if(!$("#chk_bxconfirm").attr("checked")){
				showErrMsg("chk_bxconfirm", "150px", "请认真阅读后勾选！");
				//一秒后消失
				setTimeout(function(){
					hideErrMsg("chk_bxconfirm")},
					1000
				); 
				baoxiao = false;
				//return false;
			}else{
				baoxiao = true;
			}

			//alert("linkName="+linkName+",linkPhone="+linkPhone+",baoxiao="+baoxiao);
			
			if(!$("#chk_fp").attr("checked")){//没有选中保险发票
				if(linkName==true && linkPhone==true && baoxiao==true){
					return true;
				}else{
					return false;
				}
			}else{
				//验证报销凭证的数据-----保险发票
				var fpReceiver = false;
				var fpPhone = false;
				var provinceId = false;
				var cityId = false;
				var districtId = false;
				var detailAddress = false;
				var fpZipCode = false;
				
				if($.trim($("#fp_receiver").val())==""){
					showErrMsg("fp_receiver", "110px", "请填写姓名！");
					$("#fp_receiver").focus();
					fpReceiver = false;
					return false;
				}else if(!checkName($.trim($("#fp_receiver").val()))){
					showErrMsg("fp_receiver", "150px", "请填写正确姓名！");
					$("#fp_receiver").focus();
					fpReceiver = false;
					return false;
				}else{
					hideErrMsg("fp_receiver");
					fpReceiver = true;
				}

				if($.trim($("#fp_phone").val())==""){
					showErrMsg("fp_phone", "110px", "请填写手机号码！");
					$("#fp_phone").focus();
					fpPhone = false;
					return false;
				}else if(!/^1(3[\d]|4[57]|5[\d]|8[\d]|7[08])\d{8}$/g.test($.trim($("#fp_phone").val()))){
					showErrMsg("fp_phone", "150px", "请填写正确手机号码！");
					$("#fp_phone").focus();
					fpPhone = false;
					return false;
				}else{
					hideErrMsg("fp_phone");
					fpPhone = true;
				}

				if($.trim($("#province_id").val())=="" || $.trim($("#province_id").val())=="请选择省" || $.trim($("#province_id").val())=="000000"){
					showErrMsg("province_id", "110px", "请选择省！");
					$("#province_id").focus();
					provinceId = false;
					return false;
				}else{
					hideErrMsg("province_id");
					provinceId = true;
				}

				if($.trim($("#city_id").val())=="" || $.trim($("#city_id").val())=="请选择市"){
					showErrMsg("city_id", "110px", "请选择市！");
					$("#city_id").focus();
					cityId = false;
					return false;
				}else{
					hideErrMsg("city_id");
					cityId = true;
				}

				if($.trim($("#district_id").val())=="" || $.trim($("#district_id").val())=="请选择区/县"){
					showErrMsg("district_id", "110px", "请选择区/县！");
					$("#district_id").focus();
					districtId = false;
					return false;
				}else{
					hideErrMsg("district_id");
					districtId = true;
				}

				if($.trim($("#detail_address").val())=="" || $.trim($("#detail_address").val())=="请填写详细地址，不需要重复填写省市区/县"){
					showErrMsg("detail_address", "110px", "请填写地址！");
					$("#detail_address").focus();
					detailAddress = false;
					return false;
				}else{
					hideErrMsg("detail_address");
					detailAddress = true;
				}

				if($.trim($("#fp_zip_code").val())==""){
					showErrMsg("fp_zip_code", "110px", "请填写邮编！");
					$("#fp_zip_code").focus();
					fpZipCode = false;
					return false;
				}else if(!checkZipCode($.trim($("#fp_zip_code").val()))){
					showErrMsg("fp_zip_code", "150px", "请填写正确的邮编！");
					$("#fp_zip_code").focus();
					fpZipCode = false;
					return false;
				}else{
					hideErrMsg("fp_zip_code");
					fpZipCode = true;
				}
				//判断是否勾选同意协议
				if(!$("#chk_bxconfirm").attr("checked")){
					showErrMsg("chk_bxconfirm", "150px", "请认真阅读后勾选！");
					//一秒后消失
					setTimeout(function(){
						hideErrMsg("chk_bxconfirm")},
						1000
					); 
					baoxiao = false;
					return false;
				}else{
					baoxiao = true;
				}
				//选中保险发票
				if(linkName==true && linkPhone==true && baoxiao==true && fpReceiver==true && fpPhone==true && provinceId==true && cityId==true && districtId==true && detailAddress==true && fpZipCode==true){
					var fp_address = $("#province_id").find("option:selected").text() + $("#city_id").find("option:selected").text() 
									+ $("#district_id").find("option:selected").text() +  $.trim($("#detail_address").val());
					$("#fp_address").val(fp_address);
					return true;
				}else{
					return false;
				}
			}
			//alert("linkName="+linkName+",linkPhone="+linkPhone+",baoxiao="+baoxiao+",fpReceiver="+fpReceiver+",fpPhone="+fpPhone+",provinceId="+provinceId+",cityId="+cityId+",districtId="+districtId+",detailAddress="+detailAddress+",fpZipCode="+fpZipCode);
		}
 		//提交后的回调函数
		function showLinkResponse(responseText, statusText){
			var result = responseText.result;
			var order_id = responseText.order_id;
			if (responseText.result == "SUCCESS") { 
				if(param_product_id=='BX_NO'){//没有购买保险
					var dialog = new popup("land_on","drawBill","land_off");
					$("#land_on").click();
					var url = "/order/toOrderPay.jhtml?fpNeed=1&order_id=" + $("#order_id").val()
					window.location = encodeURI(url);
				}else{//已经购买保险
					var dialog = new popup("landBx_on","drawBxBill","landBx_off");
					$("#landBx_on").click();
					timer(10);//购买保险提交订单的倒计时
				}
            } else { 
                alert('error occurs!'); 
            } 
		}
		//提交乘客信息
		$("#trainLinkForm").submit(function() {
			//更新该订单的联系人信息和报销凭证信息
			if($("#chk_fp").attr("checked")=="checked" || $("#chk_fp").attr("checked")==true){
				//alert(1);
				$("#trainLinkForm").attr("action", "/order/createOrderLink.jhtml?fpNeed=1&order_id=" + $("#order_id").val());
			}else{
				//alert(2);
				$("#trainLinkForm").attr("action", "/order/createOrderLink.jhtml?order_id=" + $("#order_id").val());
			}
			//$("#trainLinkForm").submit();
			$(this).ajaxSubmit(optionsLink); 
	        return false; 
	    }); 

/**提交订单(检查联系人信息和报销凭证信息)end**/

/**提交乘客信息开始预订车票start**/
		var options = {
			   target: '#output',          //把服务器返回的内容放入id为output的元素中    
			   beforeSubmit: showRequest,
			   success: showResponse,      //提交后的回调函数
			   //url: url,                 //默认是form的action， 如果申明，则会覆盖
			   dataType : 'json',
			   timeout: 5000,               //限制请求的时间，当请求大于3秒后，跳出请求
			   failure : function(xhr,msg){ 
					//ajax_dialog.close();
					alert("处理超时请重新提交"); 
					return false;
			   } 
		}
		//提交后的回调函数
		function showResponse(responseText, statusText){
			var result = responseText.result;
			var totalPay4Show = responseText.totalPay4Show;
			var order_id = responseText.order_id;
			$("#order_id").val(order_id);
			$("#totalPay4Show").val(totalPay4Show);
			if (responseText.result == "SUCCESS") { 
				$("#btnSubmit").prop("disabled", true);//“确认乘客信息”按钮变灰
				$("#btnSubmit").attr("class","btn3_pass");
				readonlyPasser();
            } else { 
                alert('error occurs!'); 
            } 
		}
		//提交前的执行函数	
		function showRequest(){
			if(checkForm()){
				//无座备选
				selectWz();
				submitForm();
			}else{
				return false;
			}
			function submitForm(){
				//保存常用乘客信息
				// savePaxInfo();
				if($("#weather_goto").val()=="11"){
					return true;
				}else{
					return false;
				}
			}
		}
		//提交乘客信息
		$("#trainForm").submit(function() {
			$("form:first").attr("action", "/order/createOrder.jhtml?product_id="+param_product_id);
			$(this).ajaxSubmit(options); 
	        return false; 
	    }); 
/**提交乘客信息开始预订车票end**/
		
		//验证乘客信息数据
		function checkForm(){
			var isValid=true;
			$(".adult:visible :input:text").each(function(){
				if($.trim($(this).val())==""){
					$(this).focus();
					isValid=false;
					showErrMsg($(this).attr("id"), "110px", "请输入"+$(this).attr("title")+"！");
					return false;
				}else if($(this).attr("title")=="姓名" 
					&& !checkName($.trim($(this).val()))){
					$(this).focus();
					isValid=false;
					showErrMsg($(this).attr("id"), "130px", "请填写正确的姓名！");
					return false;
				}else if($(this).attr("title")=="证件号码" 
					&& $(this).parents("tr").find(".ids_type_select").val()=="2"
					&& !valiIdCard($.trim($(this).val()))){
					$(this).focus();
					isValid=false;
					showErrMsg($(this).attr("id"), "180px", "请填写正确的二代身份证号！");
					return false;
				}else{
					hideErrMsg($(this).attr("id"));
				}
			});
			if(!isValid){
				return false;
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

			//成人姓名和身份证号码不能重复
			var isNameDup=false;
			var idNameArray=new Array('1');
			$(".adult:visible").each(function(){
				if($(this).find(".ticket_type_select").val()=='0'){//成人
					var user_name = $.trim($(this).find(".user_name_text").val());
					var idcard = $.trim($(this).find(".idcard_text").val());
					var nameAndIdCard="";
					nameAndIdCard=user_name+idcard;
					if($.inArray(nameAndIdCard, idNameArray)==-1){
						idNameArray.push(nameAndIdCard);
					}else{
						isNameDup=true;
						return false;
					}
				}
			});
			//成人 姓名及身份证重复
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
				    content: '成人乘客姓名和身份证号不能重复，请修改！',
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
			return true;
		}
     });
	/************* ready结束 ****************/




    




    
     
   	function showErrMsg(id, _width, msg){
		$("#"+id+"_errMsg").remove();
		var offset = $("#"+id).offset();
		$obj=$("#tip").clone().attr("id", id+"_errMsg")
			.css({'position':'absolute', 'top':offset.top-30, 'left':offset.left, 'width':_width,'z-index':'9999'}).appendTo("body");
		$obj.find(".errMsg").text(msg).end().show();
	}
	
	function hideErrMsg(id){
		$("#"+id+"_errMsg").remove();
	}
     
    function hideMsg(obj){
    	$(".adult:visible :input:text,.child:visible :input:text").each(function(){
			if($.trim($(this).val())!=""){
				hideErrMsg($(this).attr("id"));
			}
		});
    }
    
    //验证身份证是否有效
	function valiIdCard(idCard){
		if(idCard.length!=18){
			return false;
		}
		var checkFlag = new clsIDCard(idCard);
		if(!checkFlag.IsValid()){
			return false;
		}else{
			return true;
		}
	}
	
	//验证姓名是否有效
	function checkName(val){
	    var pat=new RegExp("[^a-zA-Z\_\u4e00-\u9fa5]","i"); 
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
		$('#num_tip').css({'position':'absolute', 'top':offset.top-38, 'left':offset.left, 'width':_width,'z-index':'9998'}).appendTo("body").show();
		if($.trim($(obj).val())==""){
			$('#num_tip').hide();
		}
	}
	
	function hideNumMsg(obj){
		$('#num_tip').hide();
	}
	
	//显示儿童票购买提示
	function toggleTip(id){
		if($("#ticket_type_"+id).val()=="1"){//儿童票
			$("#addChildren_"+id).hide();
		}else{
			$("#addChildren_"+id).show();
		}
		var haveChild = false;
		var preName = "";
		if(id > 1){
			preName = $("#user_name_"+(parseInt(id)-1)).val();
			preIds = $("#user_ids_"+(parseInt(id)-1)).val();
		}
		if(id >= 5){
			$("#addChildren_5").hide();
		}
		$(".ticket_type_select").each(function(){
			if($(this).val()=="1"){
				haveChild = true;
				if( ($("#user_name_"+id).val().length>0) && (preName==$("#user_name_"+id).val()) && ($("#user_ids_"+id).val().length>0) && (preIds==$("#user_ids_"+id).val()) ){
					$("#user_name_"+(id)).prop("disabled", true);//设置为只读属性
					$("#ticket_type_"+(id)).prop("disabled", true);//设置为只读属性
					$("#ids_type_"+(id)).prop("disabled", true);//设置为只读属性
					$("#user_ids_"+(id)).prop("disabled", true);//设置为只读属性
				}
				return false;
			}else{
				$("#user_name_"+(id)).prop("disabled", false);//取消只读属性
				$("#ticket_type_"+(id)).prop("disabled", false);//取消只读属性
				$("#ids_type_"+(id)).prop("disabled", false);//取消只读属性
				$("#user_ids_"+(id)).prop("disabled", false);//取消只读属性
			}
		});
		if(haveChild){//儿童票
			$("#child_tip").show();
			//增加130px整体高度
			//changeHeight(130);
		}else{
			//减少130px整体高度
			//changeHeight(-130);
			$("#child_tip").hide();
		}
	}

	//设置为只读属性
	function readonlyPasser(){
		var count = $(".adult:visible,.child:visible").length;;
		for(var i=1;i<=count;i++){
			$("#user_name_"+(i)).prop("disabled", true);//设置为只读属性
			$("#ticket_type_"+(i)).prop("disabled", true);//设置为只读属性
			$("#ids_type_"+(i)).prop("disabled", true);//设置为只读属性
			$("#user_ids_"+(i)).prop("disabled", true);//设置为只读属性
			$("#delPerson_"+(i)).css('display','none');
			$("#add_person_"+(i)).css('display','none');
			$("#addChildren_"+(i)).css('display','none');
		}
		//$("#bx_select").css('display','none');
		$("#bx_select_text").css('background-color','#ddd');
		$("#bx_select_text").prop("disabled", true);
	}
	
	//验证邮编
	function checkZipCode(code){
		var re = /^[0-9]{6}$/
		if(re.test(code)){
			return true;
		}else{
			return false;
		}
	}
	
	this.imagePreview = function(){ 
		/* CONFIG */
		   xOffset = -10;
		   yOffset = -30;   
		   //var msg = "购买火车票意外险，出行更安全！<br />每份保险最高赔付65万！";
		   var msg = "购买保险，出票速度更快哟！";
		/* END CONFIG */
		
		$("a.preview").hover(function(e){
		   $("body").appendTo("<div id='preview'>" + msg + "</div>");         
		   $("#preview")
		    .css("top",(e.pageY - xOffset) + "px")
		    .css("left",(e.pageX + yOffset) + "px")
		    .fadeIn("slow");      
		    },
		function(){
		   $("#preview").fadeOut("fast");
		    }); 
			$("a.preview").mousemove(function(e){
			   $("#preview")
			    .css("top",(e.pageY - xOffset) + "px")
			    .css("left",(e.pageX + yOffset) + "px");
			});  
	 };
		
    	//通用弹出信息为str的消息弹框
		function dialogAlter(str){
			//消息框
			var dialog = art.dialog({
				lock: true,
				fixed: true,
				left: '50%',
				top: '50%',
			    title: '提示',
			    okVal: '确认',
			    icon: "/images/warning.png",
			    content: str,
			    ok: function(){}
			});
			return;
		}

		function passengerShow(){
			$(".usual-passenger ul.oz li").remove(); 
	       	if(jsonLinkList!="" && jsonLinkList!=undefined && jsonLinkList!=null){
	           	linkJsonInfo = JSON.parse(jsonLinkList);
	       		var size = linkJsonInfo.length;
	       		if(size >= 6){
	       			$("#ser-more").show();
			    }else{
			    	$("#ser-more").hide();
				}
	   	       	for(var i=0;i<size;i++){  
	   		       	var newCard = "cookie_"+i;
	   		       	if(i<=6){
	   		       		$(".usual-passenger ul.oz").appendTo("<li id=\""+ newCard +"\"><input type=\"checkbox\" id=\""+newCard+"_for\" class=\""+newCard+"\" onclick='addCommonManager(\""+ newCard +"\");'/><label for=\""+ newCard +"_for\">"+linkJsonInfo[i].link_name+"</label><a title=\"删除乘客\" href='javascript:delHtml("+i+")'></a></li>");
		   		    }else{
		   		    	$(".usual-passenger ul.oz").appendTo("<li class=\"pass_hidden\" style=\"display:none;\" id=\""+ newCard +"\"><input type=\"checkbox\" id=\""+newCard+"_for\" class=\""+newCard+"\" onclick='addCommonManager(\""+ newCard +"\");'/><label for=\""+ newCard +"_for\">"+linkJsonInfo[i].link_name+"</label><a title=\"删除乘客\" href='javascript:delHtml("+i+")'></a></li>");
			   		}
	           	}
	   	       	$("#pax_passenger").show();
	        }else{
	           	$("#pax_passenger").hide();
	        }
		}

    	//将常用乘客添加到同行乘客中
		function addCommonManager(personId){
			var count = $(".adult:visible,.child:visible").length;
			var id = personId.split('_')[1];//cookie_1
			//新增乘车人信息赋值
			var name = linkJsonInfo[id].link_name;
			var ticket_type = linkJsonInfo[id].ticket_type;
			var card_type = linkJsonInfo[id].ids_type;
			var card_num = linkJsonInfo[id].user_ids;
		 	if($('.'+personId).attr('checked')=='checked'){
					if(checkPaxInfo(name,ticket_type,card_num,count)){
						dialogAlter("该乘客已经添加到同行乘客中！");
						$("."+personId).removeAttr("checked");
						return;
					}else{
						var replaceStr = "bookDetailInfoList["+count+"]";
						var result = true;
						//同行乘客信息存在空行则填充最近空行，若不存在空行，则添加一行并填充选中乘客信息
						for(var i=1; i<=5; i++){
							if($("#user_name_"+i).val()=='' && $("#user_ids_"+i).val()==''){
								$("#user_name_"+i).val(name);
								$("#ticket_type_"+i).val(ticket_type);
								$("#ids_type_"+i).val(card_type);
								$("#user_ids_"+i).val(card_num);
								result = false;
								if($("#user_name_"+i).val() != "" && $("#user_ids_"+i).val()!="" && $("#ticket_type_"+i).val()=="0"){
									$("#addChildren_"+i).show();
								}else{
									$("#addChildren_"+i).hide();
								}
								break;
							}
						}
						var old_class = $("#"+personId).attr("class");
						if(old_class=='pass_hidden'){
							$("#"+personId).attr("class","current_hidden");
						}else{
							$("#"+personId).attr("class","current");
						}
						if(result){
							if(count>=5){
								//dialogAlter("一个订单最多可代购5张票！");
								$("."+personId).removeAttr("checked");
								return;
							}	
							var num = count+1;
							var html = $(".adult:hidden").html().replace(/index_source/g, num);
							html = html.replace(/bookDetailInfoList_source/g, replaceStr);
							$("<tr class=\"adult\">" + html + "</tr>").css("background-color","")
								.appendTo("#train_ticket_list").show();
							//每添加一行增加30px整体高度
							//changeHeight(35);
							if(count==4){
								for(var i=1;i<=count;i++){
									$("#addChildren_"+i).hide();
								}
							}
							//兼容IE
							$("#add_person").attr("style","");
							//$(".savePaxDiv").attr("style","padding-top:4px;*+padding-top:3px;_padding-top:0px;");
							
							$("#user_name_"+num).val(name);
							$("#ticket_type_"+num).val(ticket_type);
							$("#ids_type_"+num).val(card_type);
							$("#user_ids_"+num).val(card_num);
							if($("#user_name_"+num).val() != "" && $("#user_ids_"+num).val()!="" && $("#ticket_type_"+num).val()=="0"){
								$("#addChildren_"+num).show();
							}else{
								$("#addChildren_"+num).hide();
							}
						}
					}
					var newCount = $(".adult:visible,.child:visible").length;		
					if(newCount>=5){
						$("#addChildren_5").hide();
					}		
				}else{
					//选框未选中，删除同行乘客中的该乘客信息
					delPaxInfo(count,name,card_num,id);
				}
		 	addPerson();
		 	
		}
		//选框未选中，删除同行乘客中的该乘客信息
		function delPaxInfo(count,name,card_num,id){
			if(count==1){
				if($("#user_name_"+count).val()==name && $("#user_ids_"+count).val()==card_num){
					$("#user_name_"+count).val("");
					$("#ticket_type_"+count).val(0);
					$("#ids_type_"+count).val(2);
					$("#user_ids_"+count).val("");
				}
			}else{
				for(var i=1; i<=count; i++){
					if($("#user_name_"+i).val()==name && $("#user_ids_"+i).val()==card_num){
						$("#user_name_"+i).val("");
						$("#ticket_type_"+i).val(0);
						$("#ids_type_"+i).val(2);
						$("#user_ids_"+i).val("");
						$("#user_name_"+i).parents("tr").remove();
						//重新设置index
						$(".adult:visible,.child:visible").each(function(index){
							var newIndex = index+1;
							$(this).html($(this).html().replace(/bookDetailInfoList\[\d+\]/g, "bookDetailInfoList["+index+"]"));
								//.find(".indexTr").html(newIndex);
							$(this).html($(this).html().replace(/\_\d/g, "_"+newIndex));
						});	
						//兼容IE
						$("#add_person").attr("style","");
					}
				}
			}
			if($("#cookie_"+id).attr("class")=='current_hidden'){
				$("#cookie_"+id).attr("class","pass_hidden");
			}else{
				$("#cookie_"+id).removeAttr("class");
			}
			addPerson();
			//$("#cookie_"+id).children("label").removeAttr("class");
		}
		//从通行乘客中删除常用乘客的同时清空该常用乘客的选中按钮
	    function removePaxAttr(name,card_num){
		    if(linkJsonInfo!=null && linkJsonInfo!=undefined){
		    	var size = linkJsonInfo.length;
				for(var i=0; i<size; i++){
					if(linkJsonInfo[i].link_name==name && linkJsonInfo[i].user_ids==card_num){
						if($("#cookie_"+i).attr("class")=='current_hidden'){
							$("#cookie_"+i).attr("class","pass_hidden");
						}else{
							$("#cookie_"+i).removeAttr("class");
						}
						$(".cookie_"+i).removeAttr("checked");
					}
				}
			}
	    }
    
		//判断通行乘客是否已经存在身份证号为card_num的乘客
		function checkPaxInfo(name,ticket_type,card_num,count){
			var result = false;
			for(var i=1; i<=count; i++){
				var existCard = $("#user_ids_"+i).val();
				if(ticket_type==1){	//儿童乘客 需要姓名和证件号都匹配
					if(existCard==card_num && name==$("#user_name_"+i).val()){
						result = true;
					}else{
						continue;
					}
				}else{
					if(existCard==card_num){
						result = true;
					}else{
						continue;
					}
				}
				
				return result;
			}
		}
		//验证json中是否已经存在身份证号为num乘客
		function checkCookiePax(name,ticket_type,num){
			var result = false;
			if(linkJsonInfo==null||linkJsonInfo==undefined){
				return result;
			}
			var size = linkJsonInfo.length;
			for(var i=0; i<size; i++){
				if(ticket_type==1){	//儿童乘客若num和name都存在则存在
					if(num == linkJsonInfo[i].user_ids && name == linkJsonInfo[i].link_name){
						result = true;
					}else{
						continue;
					}
				}else{
					if(num == linkJsonInfo[i].user_ids){
						result = true;
					}else{
						continue;
					}
				}
			}
			return result;
		}


    	//新增选中需要保存为常用乘客的信息
		function savePaxInfo(){
			var count = $(".adult:visible,.child:visible").length;
			var jsonstr="[";
			for(var i=1; i<=count;i++){
				if($(".savePax_"+i).attr("checked")=='checked'||$(".savePax_"+i).prop("checked")==true){
					var name = $.trim($("#user_name_"+i).val());
					var card_type = $("#ids_type_"+i).val();
					var card_num = $.trim($("#user_ids_"+i).val());
					
					jsonstr += "{\"user_name\""+ ":" + "\"" + name + "\",";
					jsonstr += "\"card_type\""+ ":" + "\"" + card_type + "\",";  
					jsonstr += "\"card_num\""+ ":" + "\"" + card_num + "\","; 
					jsonstr = jsonstr.substring(0,jsonstr.lastIndexOf(',')); 
					jsonstr += "},";
				}
			}
			jsonstr = "["+jsonstr.substring(0,jsonstr.lastIndexOf(',')); 
			jsonstr += "]";
			$.ajax({
				url:"/userIdsCardInfo/saveUserIdsCardInfo.jhtml",
				type: "POST",
				cache: false,
				data: {'data':jsonstr},
				success: function(res){
				}
			});
		};

		function delHtml(id){
        	//删除提示框
        	//var id = $(this).parents("li").attr("id").split("_")[1];
    		art.dialog.confirm('您确认删除该常用乘客信息？',function(){
	            delCookiePaxInfo(id);
    		},function(){});
        };
       
      //Json转换为字符串
    	function json2str(obj)
    	{
    	  var S = [];
    	  for(var i in obj){
    	  obj[i] = typeof obj[i] == 'string'?'"'+obj[i]+'"':(typeof obj[i] == 'object'?json2str(obj[i]):obj[i]);
    	  S.push(i+':'+obj[i]); 
    	  }
    	  var result =  '{'+S.join(',')+'}';
    	  return result.replace(/\"/g,"'");
    	}
     	//删除Cookie中某个乘车人
    	function delCookiePaxInfo(id){
        	var name = linkJsonInfo[id].link_name;
        	var card_num = linkJsonInfo[id].user_ids;
        	var count = $(".adult:visible,.child:visible").length;
        	//选框未选中，删除同行乘客中的该乘客信息
			delPaxInfo(count,name,card_num,id);
			var arr = new Array();
			//获取选中状态的index
			$(".usual-passenger ul.oz li").each(function(index){
				if($(".cookie_"+index).attr('checked')=='checked'){
					if(id!=index){
						index = id<index ? index-1 : index;
						arr.push(index);
					}
				}				
			});	
			//删除常用乘客信息的html
			$("li").remove("#cookie_"+id);
			var obj = JSON.stringify(linkJsonInfo[id])+"";
    		var cookieStr = json2str(linkJsonInfo[id]);//要删除的乘客信息（字符串）
    		var linkListStr = json2str(linkList);//常用乘客的信息（字符串）
    		var size = linkJsonInfo.length;
    		if(linkListStr.indexOf(cookieStr)>0){
    			if(id==size-1){
    				jsonLinkList = jsonLinkList.replace(","+obj,"");
	        	}else{
	        		jsonLinkList = jsonLinkList.replace(obj+",","");	
	            }
				//删除该乘客信息
    			$.ajax({
    				url:"/userIdsCardInfo/deleteUserIdsCardInfo.jhtml",
    				type: "POST",
    				cache: false,
    				data: {'data':cookieStr},
    				success: function(res){
    				}
    			});
        	}
            $(".adult:visible,.child:visible").each(function(index){
				$(this).html($(this).html().replace(/cookie\_\d/g, "cookie_"+index));
			});	
			//排列常用乘客信息展示
    		passengerShow();	
			for(var i=0; i<arr.length; i++){
				$(".cookie_"+arr[i]).attr('checked','checked');
				$("#cookie_"+arr[i]).attr('class','current');
			}
        }

    	function closeBx(){
    		$("#drawBillBx").hide();
        }
    	//保险发票
		function checkFp(){
			if($("#chk_fp").attr("checked")=="checked"  || $("#chk_fp").attr("checked")==true){
				$(".fp_item").show();
			}else{
				$(".fp_item").hide();
			}
		}
</script>
</body>
</html>
