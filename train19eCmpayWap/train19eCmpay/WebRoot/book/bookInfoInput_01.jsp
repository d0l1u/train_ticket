<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预订信息页</title>
<link rel="stylesheet" href="/css/style.css" type="text/css" />
<link rel="stylesheet" href="/css/default.css" type="text/css" />
<link rel="stylesheet" href="/css/attach_style.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/js/artDialog.js"></script>
<script type="text/javascript" src="/js/idCard.js"></script>
</head>

<body>
	<div class="content oz">
		<!--导航条 start-->
	    <div class="main_nav">
        	<ul class="oz">
            	<jsp:include flush="true" page="/pages/common/menu.jsp">
					<jsp:param name="menuId" value="book" />
				</jsp:include>
            </ul>
            <div class="slogan"></div>
        </div>
        <!--导航条 end-->
    	<!--左边内容 start-->
    	<div class="left_con oz">
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
            <div class="pub_order_mes ticket_mes oz mb10_all">
            	<div class="oz">
                    <h4 class="fl">车票信息</h4>
                    <p class="tit_tip">（数据仅作参考，请以实际出票情况为准）</p>
                </div>
                <div class="pub_con">
                	<table class="pub_table">
                    	<tr>
                        	<td class="pub_yuliu" rowspan="5">
                            	<strong>${trainCode}</strong><br />
                            	<span class="checi">${trainTypeCn}</span>
                            </td>
                            <td class="addr" colspan="2"><strong>${startCity}<span>（${startTime}）</span></strong>—<strong>${endCity}<span>（${endTime}）</span></strong></td>
                        	
                        </tr>
                        <tr>
                        	<td width="270">日 期：<span>${travelTime}</span></td>
                        	<td rowspan="3" width="100"><input type="button" class="btn" value="重选" onclick="javascript:history.back(-1);"/></td>
                        </tr>
                        <tr>
                        	<td width="320">坐 席：<select name="seat_type" id="seat_type">
                                <option value="" selected="selected">请选择</option>
                            	<c:forEach items="${seatInfoList}" var="seat">
                            		<option value="${seat.seatType}">${seat.seatName}</option>
                            	</c:forEach>
                            </select>
                           	<span id="wz_agree" style="display: none;">
                           	<span class="display_mid" style="padding-left: 0px;">
                           	<input type="checkbox" id="wz_chk" />
                           	</span> <label for="wz_chk" style="color:#0081cc;">同意硬座无票时购买无座</label>
                           	</span>
                           	 <span id="wz_agree1" style="display: none;">
                           	 <span class="display_mid" style="padding-left: 0px;">
                           	 <input type="checkbox" id="wz_chk1" /></span>
                           	 <label for="wz_chk1" style="color:#0081cc;">同意一等座无票时购买无座</label>
                           	 </span>
                           	 <span id="wz_agree2" style="display: none;">
                           	 <span class="display_mid" style="padding-left: 0px;">
                           	 <input type="checkbox" id="wz_chk2" /></span>
                           	 <label for="wz_chk2" style="color:#0081cc;">同意二等座无票时购买无座</label>
                           	 </span>
                            </td>
                        </tr>
                          <tr>
                        	<td>票面价：
								<span id="danjia_show">0.00</span> 元            	
								<input type="hidden" id="danjia" name="danjia" value="0.00" />
							</td>   	
                        </tr>
                          <tr>
                        	<td id="sleeper_alter" colspan="2"><p class="price_tip" style="color:#EA0000;">卧铺铺位上中下是随机的，我们暂收下铺价格，出票后根据实际票价退还差价。</p></td>
                        </tr>
                    </table>
                </div>
                
            </div>
            
            <div class="book_passager_mes oz mb10_all">
            	<div class="oz">
                    <h4 class="fl">乘客信息</h4>
                    <p class="tit_tip">（一个订单最多可代购5张票，请务必填写乘车人真实信息，<span onclick="javascript:showGpDesc();">购票说明</span>）</p>
                </div>
                <div class="usual-passenger" id="pax_passenger">
	                <ul id="useManager" class="oz">
	                </ul>
            	</div>
                <div class="pub_con delivery_pos oz">
	                <div class="delivery_pos_child oz">
	                	<div style="padding-bottom:10px;">
		                	<table class="table_list" cellpadding="0" cellspacing="0" id="train_ticket_list" style="position:relative;z-index:999;width:97%;margin:10px;">
		                    	<tr>
		                        	<td width="60"></td>
		                        	<td width="100">姓 名</td>
		                            <td width="100">类 型</td>
		                            <td width="120">证件类型</td>
		                            <td width="140">证件号码</td>
		                            <td>&nbsp;</td>
		                        </tr>
		                        <tr class="adult" style="display: none;">
		                        	<td class="indexTr">index_source</td>
		                            <td><input style="width:90px;border:1px solid #dadada;" type="text" name="bookDetailInfoList_source.user_name" id="user_name_index_source" 
		                            		class="text text_name user_name_text" title="姓名" onblur="hideMsg(this);" onkeyup="value=value.replace(/(^\s*)|(\s*$)/g,'');" /></td>
		                            <td>
		                            	<select style="width:70px;" name="bookDetailInfoList_source.ticket_type" id="ticket_type_index_source" class="ticket_type_select" onchange="toggleTip();">
		                                	<option value="0">成人票</option>
		                                	<option value="1">儿童票</option>
		                            	</select>
		                            </td>
		                            <td>
		                            	<select style="width:100px;" name="bookDetailInfoList_source.ids_type" id="ids_type_index_source"  class="ids_type_select">
		                                	<option value="2">二代身份证</option>
		                                	<!--<option value="1">一代身份证</option>-->
		                                	<option value="3">港澳通行证</option>
		                                	<option value="4">台湾通行证</option>
		                                	<option value="5">护照</option>
		                                </select>
		                            </td>
		                            <td><input style="width:130px;border:1px solid #dadada;" type="text" class="text text_id idcard_text" name="bookDetailInfoList_source.user_ids" 
		                            		id="user_ids_index_source" title="证件号码" onblur="hideMsg(this);hideNumMsg(this);" 
		                            		onfocus="getNumbers(this,'idcard');" onkeyup="value=value.replace(/(^\s*)|(\s*$)/g,'');getNumbers(this,'idcard');" /></td>
		                            <td nowrap="nowrap">
		                            	<span class="delPerson fl"><a href="javascript:void(0);"><s></s><label>删除</label></a></span>
		                            	<div class="savePaxDiv fl"><input id="savePax_index_source" class="savePax_index_source" type="checkbox" /><label for="savePax_index_source">保存为常用乘客</label></div>
		                            </td>
		                        </tr>
		                     </table>
		                 </div> 
	                     <div id="add_person" class="ableAdd">
                           	<span class="add_adult"></span>
                         </div>
	                     
	                </div>
	                <!-- 购买儿童票提示 -->
	                <div class="show_tip" id="child_tip" style="display: none;">
                		<p class="price_tip" >1.为确保儿童出行安全，儿童票须跟成人票一起购买，乘车儿童有有效身份证件的，填写儿童有效身份证件信息；乘车儿童没有有效身份证件的，应使用同行成年人的有效身份证件信息；购票时不受前条限制，
但购票后、开车前须办理换票手续方可进站乘车。<br />
2.儿童身高为1.2-1.5米可购买儿童票，超过1.5米须购买成人票。一名成年乘客可以免费携带一名身高不足1.2米的儿童。身高不足1.2米的儿童超过一名时，一名儿童免费，其他儿童需购买儿童票。<br />
3.儿童票暂收成人票价，<font style="color:#EA0000;">出票后根据实际票价退还差价。</font></p>
                	</div>
                	
                	<div class="book_passager_mes oz">
		            	<div class="oz">
		                    <h4 class="fl">交通意外险</h4>
		                    <p class="tit_tip">（若购买保险，每人一份）</p>
		                </div>
		            </div>
	                <div class="insurance_wrap oz">
				        <dl class="oz">
				            <dd>
				            	<div class="info_txt1">
				                	<div class="info-select oz">
					                	<c:forEach items="${productList}" var="product" varStatus="status">
	                                		<span class="train_product" type="radio" id="train_product${status.index}" value="${product.product_id}"> <label>${product.name}</label></span>
	                            		</c:forEach>
                            		</div>
				                </div>
				                <div class="info_txt1 oz" id="visitant_service">
				                	<p class="info_icon1 fl">高额赔付：人均<span id="gepf"></span></p>
				                    <p class="info_icon1 fl">出票加速：极速优先处理订单</p>
				                    <p class="info_icon1 fl">人工服务：提供人工改签</p>
				                </div>
				                <div class="info_txt2" id="normal_service">
				                    <p class="info_icon2">时间提醒：您选择的是不含保险的普通排队代购，您前面约有${wait_amount}人，订单处理约需${wait_time}</p>
				                    <p class="info_icon2">服务提醒：不提供人工改签</p>
				                    <p class="info_icon2">您可以选购1份/人保险，升级为VIP服务<a  class="vip_btn" id="upgrade_service">升级VIP服务</a></p>
				                </div>
				            </dd>
				        </dl>
				     </div>
                	<!-- 
                	<div style="color:#888;line-height:18px;border-top:solid 1px #dadada;">
	                	<p style="padding-left:30px;">保险信息：</p>
						<p style="padding-left:40px;">1、请您根据需求选择保险，如不需要保险，请选择不购买保险。</p>
						<p style="padding:0 10px 0 40px;">2、如果您选择投保，则必须为本人投保，投保人、乘车人（即被保险人）同意购买且认可保险金额，并已阅读<span style="color:#0081cc;cursor:pointer;line-height:24px;" onclick="window.location='http://www.unionlife.com.cn/Portals/0/xzhywjttk(2013).pdf';">《合众综合交通工具意外伤害保险(2013)》</span>的全部内容，理解并接受条款中责任免除、退保在内的重要事项。<span id="bxDes">保险金额：65万元/份，保险费：20元/份。</span></p>
						<p style="padding-left:40px;">3、本保险产品要求被保险人年龄为18至65周岁，请在购买前确认被保险人的实际年龄。</p>
						<p style="padding-left:40px;">4、在您提交订单并成功付款后，会由合众人寿保险股份有限公司发送投保成功的短信至您的手机，请注意查收。</p>
                	</div>
                	 -->
                </div>

            </div>
			<div class="pub_delivery_mes oz mb10_all" id="tickets-way">
			   	<div class="oz">
        	    	<h4 class="fl">车站自提</h4>
	                <p class="tit_tip">（取票规则：开车前可在任何火车站或代售点自由取票，<span onclick="javascript:showQpDesc();">取票说明</span>）</p>
                </div>
				<!--  
            	<h5><span id="outtickettype_22">送票上门</span><span class="current" id="outtickettype_11">车站自提</span></h5>
            	-->
                <div class="pub_con">
                    <table class="pub_table current add_bg1">
                    	<tr>
                        	<td class="pub_yuliu" rowspan="2"></td>
                        	<td width="234">联 系 人：<span><input type="text" class="link_text width90" id="link_name" name="link_name" /></span></td>
                        	<td>手 机：<span><input type="text" class="link_text width120" id="link_phone" name="link_phone" 
                        						onblur="hideNumMsg(this);" onkeyup="getNumbers(this,'phone');" onfocus="getNumbers(this,'phone');" /></span></td>
                        	<!-- <td>邮 箱：<span><input type="text" class="link_text width120" id="link_mail" name="link_mail" /></span></td>-->
                        </tr>
                        <tr>
                        	<td colspan="4">
                        		<p class="price_tip" style="margin-top: 15px;">手机号是接收订票成功短信通知的，请务必填写真实有效的手机号。</p>
                        	</td>
                        </tr>
                        <tr id="addressInput" style="display: none;">
                        	<td colspan="3">收件地址：<span><input type="text" class="link_text width280" name="link_address" value="北京海淀区鼎好大厦" /></span></td>
                        </tr>
                    </table>
                    
                </div>
        	</div>
        	
        	<div class="pub_delivery_mes oz mb10_all" id="fp_content">
			   	<div class="oz">
        	    	<h4 class="fl">保险信息</h4>
	                <p class="tit_tip"></p>
                </div>
                <div class="pub_con">
                    <table class="pub_table current">
	                    <tr>
		                    <td class="pub_yuliu" rowspan="1">
		                    </td>
		                    <td colspan="3">
		                  	 	<span class="display_mid"><input type="checkbox" id="chk_fp" /></span>保险发票（将在火车发车后第二个工作日由承保的保险公司用平信免费邮寄寄出）
		                    </td>
	                    </tr>
                    	<tr class="fp_item" style="display: none;">
                        	<td class="pub_yuliu" rowspan="3"></td>
                        	<td width="234">收件人：<span><input type="text" class="link_text width90" id="fp_receiver" name="fp_receiver" /></span></td>
                        	<td>手 机：<span><input type="text" class="link_text width120" id="fp_phone" name="fp_phone" 
                        						onblur="hideNumMsg(this);" onkeyup="getNumbers(this,'phone');" onfocus="getNumbers(this,'phone');" /></span></td>
                        </tr>
                        <tr class="fp_item" style="display: none;">
                        	<td colspan="3">邮&nbsp;&nbsp;编：<span><input type="text" class="link_text width120" id="fp_zip_code" name="fp_zip_code" /></span></td>
                        </tr>
                        <tr class="fp_item" style="display: none;">
                        	<td colspan="3">地&nbsp;&nbsp;址：<span><input type="text" class="link_text" style="width:380px;color:#aaa" id="fp_address" name="fp_address" 
                        		value="请填写可以收件的真实地址（包括省市区）" onfocus="if(this.value=='请填写可以收件的真实地址（包括省市区）'){this.value=''};this.style.color='#333';" 
                    			onblur="if(this.value==''||this.value=='请填写可以收件的真实地址（包括省市区）'){this.value='请填写可以收件的真实地址（包括省市区）';this.style.color='#aaa';}"/></span></td>
                        </tr>
                    </table>
                    
                </div>
        	</div>
        	
        	 <p id="bx_confirm" style="padding-left:153px;"><span class="display_mid"><input type="checkbox" id="chk_bxconfirm" checked="checked"/></span>本人已确认购买合众综合交通工具意外伤害保险（2013）保险，阅读并同意<span style="color:#0081cc;cursor:pointer;line-height:24px;" onclick="window.open('http://www.unionlife.com.cn/tab674/');">《保险说明》</span></p>
        	
             <p class="tijiao"><input type="button" value="提交订单" id="btnSubmit" class="btn inside" /></p>
            </form>
            <!--业务所有标注 start-->
	        <div class="business-provider">
	        	<p>业务提供方：19旅行</p>
	        </div>
	        <!--业务所有标注 end-->
        </div>
        <!--左边内容 end-->
    </div>
    <div id="tip" class="tip_wrap" style="display: none;"><div class="tip"><b></b><span class="errMsg"></span></div></div>
    <div id="num_tip" class="num_tip_wrap" style="display: none;"><div class="tip"><b></b><span class="numMsg"></span></div></div>
<script type="text/javascript">
	var userId = "${agentId}"+"_hcp";
	var cookieValue = ""; 	//编码后的cookie值
	var cookieRealValue	= "";	//cookieJson解析后需要使用的数据

	var param_product_id = "";	//传递给后台的保险ID值
    $(document).ready(function(){
        //是否显示卧铺提醒
        $("#seat_type").click(function(){
			var seatType = $(this).val();
			if(seatType=='4' || seatType=='5' || seatType=='6'){
				$("#sleeper_alter").show();
			}else{
				$("#sleeper_alter").hide();
			}
        }).trigger("click");
        
    	$("#train_product0").attr("class","train_product_checked");
    	param_product_id = $("#train_product0").val();
    	$("#gepf").html($("#train_product0").text());
    	
    	//获取cookie中常用乘客 信息，若存在将常用乘客展示出来，不存在则新建cookie
    	getCookiePaxInfo(userId,30);

    	
    	//交通意外险选框触发
    	$(".info-select span").click(function(){
			$(this).siblings("span").attr("class","train_product");
			$(this).attr("class","train_product_checked");
			var str = $.trim($(this).text());
			param_product_id = $(this).val();
			$("#gepf").html(str);
			if(str=='不购买保险'){
				$("#visitant_service").hide();
				$("#normal_service").show();
				//保险发票隐藏
				$("#fp_content").hide();
				$("#bx_confirm").hide();
			}else{
				$("#visitant_service").show();
				$("#normal_service").hide();
				//保险发票显示
				$("#fp_content").show();
				$("#bx_confirm").show();
			}
        });
        
		//升级VIP服务
        $("#upgrade_service").click(function(){
        	$("#train_product0").siblings("span").attr("class","train_product");
        	$("#train_product0").attr("class","train_product_checked");
        	$("#gepf").html($("#train_product0").text());
        	param_product_id = $("#train_product0").val();
        	$("#visitant_service").show();
			$("#normal_service").hide();
			//保险发票显示
			$("#fp_content").show();
			$("#bx_confirm").show();
        });

		var mapper = eval('('+'${seatPrizeMapper}'+')');
     	 
     	 imagePreview();
     	 
     	 //异常情况加载坐席价格
     	 if($("#seat_type").val()!=""){
     	 	var key = "seatType" + $("#seat_type").val();
     	 	var price = mapper[key];
			$("#danjia_show").text(price);
			$("#danjia").val(price);
     	 }
     
         $("#tickets-way h5 span").click(function(){
         	 var index = $("#tickets-way h5 span").index($(this)[0]);
             $(this).addClass('current').siblings().removeClass('current');
             //$("#tickets-way .pub_table:eq("+index+")").addClass('current').siblings().removeClass('current');
             if($(this).attr("id")=="outtickettype_11"){//车站自提 则隐藏地址
             	$("#addressInput").hide();
             }else{//送票上门 则显示
             	$("#addressInput").show();
             }
             
             //设置出票方式
             var type_id = $(this).attr("id").split("_")[1];
             $("#out_ticket_type").val(type_id);
         }); 
         
		//行变色
		$(".table_list tr.adult,.table_list tr.child").live("mouseover", function(){
			$(this).css('background-color','#daeff8').siblings().css("background-color","");
		});	
		
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

		//删除乘客
		$(".delPerson").live("click", function(){
			if($(".adult:visible,.child:visible").length==1){
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
			//删除同行乘客的对应选框清空
			removePaxAttr(name,card_num);
			$(this).parents("tr").remove();
			$(this).parents("tr").find(":text").each(function(){
				hideErrMsg($(this).attr("id"));
			});
			//重新设置index
			$(".adult:visible,.child:visible").each(function(index){
				var newIndex = index+1;
				$(this).html($(this).html().replace(/bookDetailInfoList\[\d+\]/g, "bookDetailInfoList["+index+"]"))
					.find(".indexTr").html(newIndex);
				$(this).html($(this).html().replace(/\_\d/g, "_"+newIndex));
			});	
			$("#add_person").attr("class","ableAdd");
			//兼容IE
			$("#add_person").attr("style","");
		});
		
		$("#seat_type").change(function(){
			if($(this).val()==""){
				$("#danjia_show").text("0.00");
				$("#danjia").val("0.00");
				return;
			}
			var key = "seatType" + $(this).val();
			var price = mapper[key];
			$("#danjia_show").text(price);
			$("#danjia").val(price);
			
			if($(this).val()=='8'){//硬座显示无座备选
				$("#wz_agree").show();
			}else{
				$("#wz_agree").hide();
			}
			
			if($(this).val()=='2'){//一等座显示无座备选
				$("#wz_agree1").show();
			}else{
				$("#wz_agree1").hide();
			}
			if($(this).val()=='3'){//二等座显示无座备选
				$("#wz_agree2").show();
			}else{
				$("#wz_agree2").hide();
			}
		});
		
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

		//提交订单
		$("#btnSubmit").click(function(){
			if(checkForm()){
				//无座备选
				selectWz();
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
				    icon: "/images/loading.gif",
				    content: '正在验证乘客和余票信息，请稍候！'
				});
				$(".aui_titleBar").hide();
				//保存常用乘客信息
				savePaxInfo();
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
								$("form:first").attr("action", "/order/createOrder.jhtml?fpNeed=1&product_id="+param_product_id);
							}else{
								$("form:first").attr("action", "/order/createOrder.jhtml?product_id="+param_product_id);
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
			for(var i =1 ; i<=count;i++){
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
						return false;
					}		
				}
			}
			
			if($("#seat_type").val()==""){
				showErrMsg("seat_type", "90px", "请选择坐席！");
				$("#seat_type").focus();
				return false;
			}else{
				hideErrMsg("seat_type");
			}
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
			if($.trim($("#link_name").val())==""){
				$("#link_name").focus();
				showErrMsg("link_name", "110px", "请填写联系人！");
				return false;
			}else if(!checkName($.trim($("#link_name").val()))){
				$("#link_name").focus();
				showErrMsg("link_name", "150px", "请填写正确的联系人！");
				return false;
			}else{
				hideErrMsg("link_name");
			}
			if($.trim($("#link_phone").val())==""){
				$("#link_phone").focus();
				showErrMsg("link_phone", "90px", "请填写手机！");
				return false;
			}else if(!/^1(3[\d]|4[57]|5[012356789]|8[012356789])\d{8}$/g.test($.trim($("#link_phone").val()))){
				$("#link_phone").focus();
				showErrMsg("link_phone", "150px", "请填写正确的手机号！");
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

		
		artDialog.confirm = function (content, yes, no) {
		    return artDialog({
		        id: 'Confirm',
		        icon: "/images/warning.png",
		        lock: true,
				fixed: true,
				left: '50%',
				top: '50%',
				title: '提示',
		        content: content,
		        ok: function (here) {
		            return yes.call(this, here);
		        },
		        cancel: function (here) {
		            return no && no.call(this, here);
		        }
		    });
		};
		
		$("#seat_type").change(function(){
			if(($("#seat_type").val()!="")){
				hideErrMsg("seat_type");
			}
		});
		
		$("#link_name").blur(function(){
			if($.trim($("#link_name").val())!=""){
				hideErrMsg("link_name");
			}
		});
		
		$("#link_phone").blur(function(){
			if($.trim($("#link_name").val())!=""){
				hideErrMsg("link_name");
			}
		});
		
		//保险发票
		$("#chk_fp").click(function(){
			if($("#chk_fp").attr("checked")){
				$(".fp_item").show();
			}else{
				$(".fp_item").hide();
			}
		});
     });
     
    //显示购票说明
	function showGpDesc(){
		//消息框
		var dialog = art.dialog({
			lock: true,
			fixed: true,
			left: '50%',
			top: '50%',
		    title: '购票说明',
		    okVal: '确认',
		    content: '<p style="width:250px;height:60px;line-height:20px;">温馨提示：乘车人进站乘车时须凭本人有效身份证件原件，票、证、人一致的方可进站乘车。如遇填写错误，只能在网站退票，并按规定收取退票费。</p>',
		    ok: function(){}
		});
	}
     
    //显示取票说明
	function showQpDesc(){
		//消息框
		var dialog = art.dialog({
			lock: true,
			fixed: true,
			left: '50%',
			top: '50%',
		    title: '取票说明',
		    okVal: '确认',
		    content: '<p style="width:250px;height:40px;line-height:20px;">1、凭购票时的有效证件和电子订单号，发车前可在全国任意火车站或代售点取票。</p>'
		    		+'<p style="width:250px;height:40px;line-height:20px;margin-top:15px;">2、代售点收取代售费5元/张，另外车站售票窗口取异地票，火车站将收取代售费5元/张。</p>',
		    ok: function(){}
		});
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
     
    function hideMsg(obj){
    	$(".adult:visible :input:text,.child:visible :input:text").each(function(){
			if($.trim($(this).val())!=""){
				hideErrMsg($(this).attr("id"));
			}
		});
    }
    
    //验证身份证是否有效
	function valiIdCard(idCard){
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
	
	function hideNumMsg(obj){
		$('#num_tip').hide();
	}
	
	//显示儿童票购买提示
	function toggleTip(){
		var haveChild = false;
		$(".ticket_type_select").each(function(){
			if($(this).val()=="1"){
				haveChild = true;
				return false;
			}
		});
		if(haveChild){//儿童票
			$("#child_tip").show();
		}else{
			$("#child_tip").hide();
		}
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
		   $("body").append("<div id='preview'>" + msg + "</div>");         
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
		
		//将常用乘客添加到同行乘客中
		function addCommonManager(personId){
			var count = $(".adult:visible,.child:visible").length;
			var id = personId.split('_')[1];
			//新增乘车人信息赋值
			var name = cookieRealValue[id].name;
			var ticket_type = cookieRealValue[id].ticket_type;
			var card_type = cookieRealValue[id].card_type;
			var card_num = cookieRealValue[id].card_num;
		 	if($('.'+personId).attr('checked')=='checked'){
					$("#"+personId).attr("class","current");
					if(checkPaxInfo(name,ticket_type,card_num,count)){
						dialogAlter("该乘客已经添加到通行乘客中！");
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
								break;
							}
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
							if(count==4){
								$("#add_person").attr("class","unableAdd");
							}
							//兼容IE
							$("#add_person").attr("style","");
							$(".savePaxDiv").attr("style","padding-top:4px;*+padding-top:3px;_padding-top:10px;");
							
							$("#user_name_"+num).val(name);
							$("#ticket_type_"+num).val(ticket_type);
							$("#ids_type_"+num).val(card_type);
							$("#user_ids_"+num).val(card_num);
						}
					}				
				}else{
					//选框未选中，删除同行乘客中的该乘客信息
					delPaxInfo(count,name,card_num,id);
					
				}
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
							$(this).html($(this).html().replace(/bookDetailInfoList\[\d+\]/g, "bookDetailInfoList["+index+"]"))
								.find(".indexTr").html(newIndex);
							$(this).html($(this).html().replace(/\_\d/g, "_"+newIndex));
						});	
						$("#add_person").attr("class","ableAdd");
						//兼容IE
						$("#add_person").attr("style","");
					}
				}
			}
			$("#cookie_"+id).removeAttr("class");
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
		//验证Cookie中是否已经存在身份证号为num乘客
		function checkCookiePax(name,ticket_type,num){
			var result = false;
			if(cookieRealValue==null||cookieRealValue==undefined){
				return result;
			}
			var size = cookieRealValue.length;
			for(var i=0; i<size; i++){
				if(ticket_type==1){	//儿童乘客若num和name都存在则存在
					if(num == cookieRealValue[i].card_num && name == cookieRealValue[i].name){
						result = true;
					}else{
						continue;
					}
				}else{
					if(num == cookieRealValue[i].card_num){
						result = true;
					}else{
						continue;
					}
				}
			}
			return result;
		}

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

		function getCookiePaxInfo(userId,days){
	    	getCookie(userId);
        	if(cookieValue==""||cookieValue==undefined){
            	addCookie(userId,"",days);
        	}else if(cookieRealValue!="" && cookieRealValue!=undefined){
            	//排列常用乘客信息展示
        		cookiePaxInfoShow();
            }
    	}
    	//排列常用乘客信息展示
    	function cookiePaxInfoShow(){
    		$(".usual-passenger ul.oz li").remove();
        	if(cookieRealValue!="" && cookieRealValue!=undefined && cookieRealValue!=null){
        		var size = cookieRealValue.length;
    	       	for(var i=0;i<size;i++){  
    		       	var newCard = "cookie_"+i;
    		       	$(".usual-passenger ul.oz").append("<li id=\""+ newCard +"\"><input type=\"checkbox\" id=\""+newCard+"_for\" class=\""+newCard+"\" onclick='addCommonManager(\""+ newCard +"\");'/><label for=\""+ newCard +"_for\">"+cookieRealValue[i].name+"</label><a title=\"删除乘客\" href='javascript:delHtml("+i+")'></a></li>");
            	}
    	       	$("#pax_passenger").show();
            }else{
            	$("#pax_passenger").hide();
            }
        }
    	//查询指定Cookie，如果cookie不存在，则创建新的Cookie
        function getCookie(userId){
        	//获取cookie字符串 
	       	 var strCookie=document.cookie; 
	       	 //将多cookie切割为多个名/值对 
	       	 var arrCookie=strCookie.split("; "); 
	       	 //遍历cookie数组，处理每个cookie对 
	       	 for(var i=0;i<arrCookie.length;i++){
	       	 	var arr=arrCookie[i].split("="); 
	       	 	//找到名称为userId的cookie，并返回它的值 
	   	    	if(userId==arr[0]){
	   	    		cookieValue = unescape(arr[1]);
	   	    		cookieRealValue = eval(cookieValue);
	   	    	} 
	       	 }
        }

      //新增cookie并赋值
    	function addCookie(userId,objValue,days){
			 var str = userId + "=" + escape(objValue);  
			 if(days > 0){  
	          var date = new Date();  
	          var ms = days*3600*24*1000; 
	          date.setTime(date.getTime() + ms);  
	          str += ";expires=" + date.toGMTString(); 
			 }  
			 document.cookie = str;
		}
		//删除Cookie
    	function delCookie(userId){//为了删除指定名称的cookie，可以将其过期时间设定为一个过去的时间 
    		//$.cookie(name, null); 
    		var date = new Date(); 
    		date.setTime(date.getTime() - 10000); 
    		document.cookie = userId + "=a; expires=" + date.toGMTString();
    	} 


    	//新增选中需要保存为常用乘客的信息
		function savePaxInfo(){
			var count = $(".adult:visible,.child:visible").length;
			for(var i=1; i<=count;i++){
				if($(".savePax_"+i).attr("checked")=='checked'||$(".savePax_"+i).prop("checked")==true){
					var name = $("#user_name_"+i).val();
					var ticket_type = $("#ticket_type_"+i).val();
					var card_type = $("#ids_type_"+i).val();
					var card_num = $("#user_ids_"+i).val();

					var oldStr = "";
					var newStr = "";
					if(cookieRealValue==""||cookieRealValue==undefined||cookieRealValue==null){
						newStr += "[{name:'"; 
					}else{
						if(checkCookiePax(name,ticket_type,card_num)){
							continue;
						}
						oldStr = cookieValue.substring(0,cookieValue.length-1);
						newStr += ",{name:'"; 
					}
					newStr += name;
					newStr += "',";
					newStr += "ticket_type:'"; 
					newStr += ticket_type+"";
					newStr += "',";
					newStr += "card_type:'"; 
					newStr += card_type+"";
					newStr += "',";
					newStr += "card_num:'"; 
					newStr += card_num;
					newStr += "'}]";
					var newCookieValue = oldStr + newStr;
					addCookie(userId,newCookieValue,30);
					getCookie(userId);
				}
			}
		};

		//zuoyuxing
     	function delHtml(id){
        	//删除提示框
        	//var id = $(this).parents("li").attr("id").split("_")[1];
    		art.dialog.confirm('您确认删除该常用乘客信息？',function(){
	            delCookiePaxInfo(id);
    		},function(){});
        };
       

      //删除Cookie中某个乘车人
    	function delCookiePaxInfo(id){
        	var name = cookieRealValue[id].name;
        	var card_num = cookieRealValue[id].card_num;
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
    		var cookieStr = json2str(cookieRealValue[id]);
    		var newCookieValue = "";
    		var size = cookieRealValue.length;
    		if(cookieValue.indexOf(cookieStr)>0){
				if(size==1){
					delCookie(userId);
				}else{
					if(id==size-1){
						newCookieValue = cookieValue.replace(","+cookieStr,"");
		        	}else{
		        		newCookieValue = cookieValue.replace(cookieStr+",","");	
		            }
				}
        	}
            addCookie(userId,newCookieValue,30);
            getCookie(userId);
            //$(".adult:visible,.child:visible").each(function(index){
				//$(this).html($(this).html().replace(/cookie\_\d/g, "cookie_"+index));
			//});	
			//排列常用乘客信息展示
    		cookiePaxInfoShow();
			for(var i=0; i<arr.length; i++){
				$(".cookie_"+arr[i]).attr('checked','checked');
				$("#cookie_"+arr[i]).attr('class','current');
			}
        }
        
        
</script>
</body>
</html>
