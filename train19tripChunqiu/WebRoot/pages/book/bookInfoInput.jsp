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
<title>预订信息页</title>
<link rel="stylesheet" href="/css/style.css" type="text/css" />
<link rel="stylesheet" href="/css/default.css" type="text/css" />
<link rel="stylesheet" href="/css/attach_style.css" type="text/css" />
<!-- 自适应高度 -->
<script type="text/javascript" src="/js/trendsHeight.js"></script>
<!-- 自适应高度 -->
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/js/artDialog.js"></script>
<script type="text/javascript" src="/js/idCard.js"></script>
<script type="text/javascript" src="/js/json2.js"></script>
<style>
.dialog_p{margin:10px 0px;}
	/* select */
.select{position:absolute;float:right;margin:0 10px;width:114px;padding-left:550px;*+padding-left:0px;z-index:10;cursor:pointer;}
.select dt{height:27px;display:inline-block;border:1px solid #d2ccc4;
	background:#fcfcfb url(/images/ico.gif) no-repeat 97px center;line-height:28px;
	padding-left:10px;cursor:pointer;width:90px;padding-right:12px;
	white-space:nowrap;text-overflow:ellipsis;overflow:hidden;}
.select dt:hover,.select dt.cur{border:1px solid #078F5F;box-shadow:0 0 1px #078F5F;cursor:pointer;}
.select dd{float:left;border:1px solid #d2ccc4;background:#fff;widht:112px;padding-left:0;display:none;padding-top:0px;cursor:pointer;}
.select dd ul{width:112px;max-height:250px;overflow:auto;background:#fff;cursor:pointer;}
.select dd ul li span{line-height:27px;display:block;margin:0;padding:0;color:#555;padding-left:10px;background:#fff;cursor:pointer;}
.select dd ul li span:hover{background:#f5f5f5;cursor:pointer;}
</style>
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
    		<div class="tip_term oz" style="margin:10px auto 0;">
            	<p class="price_tip">
            		<span>客服电话：400-688-2666&nbsp;&nbsp;转2号键</span>
            		<span style="padding-left:400px;">业务提供方：19旅行</span>
            	</p>
            </div>
            <form id="trainForm" action="/chunqiu/order/createOrder.jhtml" method="post">
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
                            <td class="addr" colspan="2"><strong>${startCity}<span>（${startTime}）</span></trong>—<strong>${endCity}<span>（${endTime}）</span></strong></td>
                        	
                        </tr>
                        <tr>
                        	<td width="280">日 期：<span>${travelTime}</span></td>
                        	<td rowspan="3" width="100" style="padding-left:80px;"><input type="button" class="btn" value="重选" onclick="javascript:history.back(-1);"/></td>
                        </tr>
                        <tr>
                        	<td width="320">坐 席：<select name="seat_type" id="seat_type">
                            	<c:forEach items="${seatInfoList}" var="seat" >
                            		<c:if test="${seat.seatSelect eq 'select'}">
                            			<option selected="selected"  value="${seat.seatType}">${seat.seatName}</option>
                            		</c:if>
                            		<c:if test="${seat.seatSelect eq 'unSelect'}">
                            			<option value="${seat.seatType}">${seat.seatName}</option>
                            		</c:if>
                            	</c:forEach>
                            </select>
                            <span id="wz_agree" style="display: none;">
                            <span class="display_mid" style="padding-left: 20px;">
                            <input type="checkbox" id="wz_chk" />
                            </span>
                            <label for="wz_chk" style="color:#0081cc;">同意无票时购买无座</label>
                            </span>
                            <span id="wz_agree1" style="display: none;">
                           	 <span class="display_mid" style="padding-left: 0px;">
                           	 <input type="checkbox" id="wz_chk1" /></span>
                           	 <label for="wz_chk1" style="color:#0081cc;">同意无票时购买无座</label>
                           	 </span>
                           	 <span id="wz_agree2" style="display: none;">
                           	 <span class="display_mid" style="padding-left: 0px;">
                           	 <input type="checkbox" id="wz_chk2" /></span>
                           	 <label for="wz_chk2" style="color:#0081cc;">同意无票时购买无座</label>
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
	                      	<td id="sleeper_alter" colspan="2"><p class="price_tip" style="color:#EA0000;">卧铺铺位上中下是随机的，我们暂收下铺价格，出票后根据实际票价退还差价。
	                      	<!-- <br />假如硬座余票不足并勾选了<同意硬座无票时购买无座>，将会给用户出无座票,否则出票失败。</p></td> -->   	
                     	</tr>
                    </table>
                </div>
                
            </div>
            
            <div class="book_passager_mes oz mb10_all">
            	<div class="oz">
                    <h4 class="fl" id="passenger_info">乘客信息</h4>
                    <p class="tit_tip">（一个订单最多可代购5张票，请务必填写乘车人真实信息，<span onclick="javascript:showGpDesc();">购票说明</span>）</p>
                </div>
                
                <div class="pub_con delivery_pos oz">
                	<div class="usual-passenger" id="pax_passenger" style="border:0;background-color:#EAF2FC;">
		                <ul id="useManager" class="oz">
		                </ul>
		                <div class="down-arrow" id="ser-more"><a href="javascript:void(0);">更多</a></div>
	            	</div>
	                <div class="delivery_pos_child oz">
	                	<div style="padding-bottom:10px;">
		                	<table class="table_list" cellpadding="0" cellspacing="0" id="train_ticket_list" style="position:relative;z-index:999;width:97%;margin:10px;">
		                    	<tr>
		                        	<td width="60"></td>
		                        	<td width="90">姓 名</td>
		                            <td width="90">类 型</td>
		                            <td width="120">证件类型</td>
		                            <td width="140">证件号码</td>
		                            <td>&nbsp;</td>
		                        </tr>
		                        <tr class="adult" style="display: none;">
		                        	<td class="indexTr">index_source</td>
		                            <td><input style="width:80px;border:1px solid #dadada;" type="text" name="bookDetailInfoList_source.user_name" id="user_name_index_source" 
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
	                      	 <div class="add_person_statement oz" style="width:97%;margin:10px;color:#ea0000;">
	                      	 	<p style="float:left;padding-left:26px;width:60px;line-height:24px;">温馨提示：</p>
	                      	 	<p style="float:left;text-align:left;color:#ea0000;width:600px;line-height:24px;">
	                      	 		<span>1、若2-5人一同出行，请添加同行乘客后一起提交，可提高连座几率；</span><br />
	                      	 		<span>2、所填写的乘客姓名和证件号码必须与证件上信息保持一致，若有错别字或号码不准确均会导致出票失败。</span> 
	                      	 	</p>
	                      	 	
	                          </div>
		                 </div>
	                     <div id="add_person" >
                           	<div class="ableAdd" style="float:left;">
                           		<span class="add_adult">添加同行乘客</span>
                           	</div>
                         </div>
                         <!-- 
                         	<div class="addPassenger" style="float:left;">
                           		<span class="add_pass"></span>
                           	</div>
                          -->
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
		                    <p class="tit_tip">（若购买保险，每人一份，<span onclick="javascript:showBxDesc();">保险说明</span>）</p>
		                </div>
		            </div>
	                <div class="insurance_wrap oz">
				        <dl class="oz">
				            <dd>
				                <input type="hidden" id="train_product" value="BX_20"/>
				                <div class="info_txt1 oz" id="visitant_service">
				                	<p class="info_icon1 fl" style="margin:6px 6px 2px;">高额赔付：人均20元保额65万</p>
				                    <p class="info_icon1 fl" style="margin:6px 6px 2px;">出票加速：极速优先处理订单</p>
				                    <p class="info_icon1 fl" style="margin:6px 5.5px 2px;">人工服务：提供人工改退</p>
				                    
				                    <dl class="select">
										<dt id="content_bx">20元保额65万</dt>
										<dd>
											<ul>
												<li><span>20元保额65万</span></li>
												<li><span>不购买保险</span></li>
											</ul>
										</dd>
									</dl>
								
				                </div>
				                <div class="info_txt2" id="normal_service">
				                    <p class="info_icon2">时间提醒：您选择的是不含保险的普通排队代购，您前面约有${wait_amount}人，订单处理约需${wait_time}</p>
				                    <p class="info_icon2">服务提醒：不提供人工改退</p>
				                    <p class="info_icon2">您可以选购1份/人保险，升级为VIP服务<a  style="cursor:pointer" class="vip_btn" id="upgrade_service">升级VIP服务</a></p>
				                </div>
				            </dd>
				        </dl>
				     </div>
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
        	
        	 <p style="padding-left:153px;">
        	 	<span class="display_mid">
        	 		<input type="checkbox" id="chk_bxconfirm" checked="checked"/>
        	 	</span>
        	 	我已阅读并同意
        	 	<span style="color:#0081cc;cursor:pointer;line-height:24px;" onclick="javascript:showDaiGouDesc();">《火车票线下代购服务协议》</span>
        	 	<span id="bx_confirm">和<span style="color:#0081cc;cursor:pointer;line-height:24px;" onclick="javascript:showBaoXianDesc();">《保险说明》</span></span>
        	 </p>
             <p class="tijiao"><input type="button" value="提交订单" id="btnSubmit" class="btn inside" /></p>
            </form>
        </div>
        <!--左边内容 end-->
    </div>
    <div id="tip" class="tip_wrap" style="display: none;"><div class="tip"><b></b><span class="errMsg"></span></div></div>
    <div id="num_tip" class="num_tip_wrap" style="display: none;"><div class="tip"><b></b><span class="numMsg"></span></div></div>
<script type="text/javascript">
	var jsonLinkList = JSON.stringify(${jsonLinkList})+"";
	var linkJsonInfo = "";	//jsonLinkList解析后的json集合
	var linkList = ${jsonList};

	var param_product_id = $("#train_product").val();	//传递给后台的保险ID值
    $(document).ready(function(){
    	$("#seat_type").focus();
        //是否显示卧铺提醒
        $("#seat_type").click(function(){
			var seatType = $(this).val();
			if(seatType=='4' || seatType=='5' || seatType=='6'){
				$("#sleeper_alter").show();
			}else{
				$("#sleeper_alter").hide();
			}
        }).trigger("click");
    	//$("#product_0").attr("class","train_product_checked");
    	//param_product_id = $("#train_product").val();
    	//$("#gepf").html($("#product_0").text());
    	
    	//获取cookie中常用乘客 信息，若存在将常用乘客展示出来，不存在则新建cookie
    	//排列常用乘客信息展示
   		passengerShow();
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

    	//交通意外险选框触发(不用)
    	$(".info-select span").click(function(){
			$(this).siblings("span").attr("class","train_product");
			$(this).attr("class","train_product_checked");
			var str = $.trim($(this).text());
			var product_id = $(this).attr("id");
			var train_product_id ="train_"+product_id;
			param_product_id = $("#"+train_product_id).val();
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
			$(this).css('background-color','#F6F6F6').siblings().css("background-color","");
		});	
		
		//提交乘客信息
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
    			url:"/chunqiu/bookInfo/addPassenger.jhtml",
    			type: "POST",
    			cache: false,
    			success: function(data){
    			}
    		});
		})
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
		}).trigger("change");
		
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
				//异步验证联系人是否审核通过
				//获取联系人姓名和身份证号
				var obj=[];
				var jsonstr='';
				jsonstr="["; 
				$(".adult:visible").each(function(){
					var userName=$.trim($(this).find(".user_name_text").val());
					var userIds=$.trim($(this).find(".idcard_text").val());
					var cert_type=$.trim($(this).find(".ids_type_select").val());
					jsonstr += "{\"user_name\""+ ":" + "\"" + userName + "\","; 
					jsonstr += "\"cert_no\""+ ":" + "\"" + userIds + "\","; 
					jsonstr += "\"cert_type\""+ ":" + "\"" + cert_type + "\","; 
					jsonstr = jsonstr.substring(0,jsonstr.lastIndexOf(',')); 
					jsonstr += "},";
				});
				jsonstr = jsonstr.substring(0,jsonstr.lastIndexOf(',')); 
				jsonstr += "]";
				$.ajax({
					url:"/chunqiu/userIdsCardInfo/checkUserIdsCardInfo.jhtml",
					type: "POST",
					cache: false,
					data: {'data':jsonstr},
					success: function(res){
						if(res=='SUCCESS'){
							dialog.content('验证成功，可以订购！');
							//保存常用乘客信息
							savePaxInfo();
							if($("#chk_fp:visible").length>0 && $("#chk_fp").attr("checked")){
								$("form:first").attr("action", "/chunqiu/order/createOrder.jhtml?fpNeed=1&product_id="+param_product_id);
							}else{
								$("form:first").attr("action", "/chunqiu/order/createOrder.jhtml?product_id="+param_product_id);
							}
							$("form:first").submit();
						}else{
							var dataObj=eval("("+res+")");
							var strAll='';
							$.each(dataObj.errorData,function(idx,item){
								var first;
								first=idx+1;
								if(item.status=='1'){
									strAll+="<p style='width:360px;color:#555;font: 12px/18px Simsun;padding-bottom:6px;'>"+first+"."+item.userName+"("+item.ids_card+")<br />&nbsp;&nbsp;身份信息待核验，请乘客到火车站窗口实名认证后，方可网上购票！</p>"
								}else{
									strAll+="<p style='width:360px;color:#555;font: 12px/18px Simsun;padding-bottom:6px;'>"+first+"."+item.userName+"("+item.ids_card+")<br />&nbsp;&nbsp;身份信息未通过审核，无法购票！</p>"
								}
							});
							if(strAll!=''){
								dialog.content(strAll);
								dialog.title("提示");
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
			}else if(!/^1(3[\d]|4[57]|5[\d]|8[\d]|7[0678])\d{8}$/g.test($.trim($("#link_phone").val()))){
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
				}else if(!/^1(3[\d]|4[57]|5[\d]|8[\d]|70)\d{8}$/g.test($.trim($("#fp_phone").val()))){
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
			
			//勾选我已阅读并同意《保险说明》和《火车票线下代购服务协议》
			if(!$("#chk_bxconfirm").attr("checked")){
				showErrMsg("chk_bxconfirm", "150px", "请认真阅读后勾选！");
				//一秒后消失
				setTimeout(function(){
					hideErrMsg("chk_bxconfirm")},
					1000
				); 
				return false;
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
	//《火车票线下代购服务协议》
	function showDaiGouDesc(){
		//消息框
		var dialog = art.dialog({
			lock: true
			,fixed: true
			,width: '80%'
			,height: '60%'
			,modal: true //蒙层
			,left: '50%'
			,top: '50%'
			,title: '火车票线下代购服务协议'
			,okVal: '我知道了'
			,content: '<p class="dialog_p" style="font-weight:bold;">一、协议的完善和修改</p>'
				    +'<p class="dialog_p">&nbsp;&nbsp;&nbsp;&nbsp;北京一九易电子商务有限公司（19旅行）有权在必要时根据互联网的发展和中华人民共和国有关法律、法规的变化，不断地完善服务质量并依此修改本协议的条款，修改后的协议条款将在相关页面上提示修改的内容。用户如果没有提出疑问，则被视为接受修改后的协议条款。</p>'
				    +'<p class="dialog_p" style="font-weight:bold;">二、服务内容</p>'
				    +'<p class="dialog_p">&nbsp;&nbsp;&nbsp;&nbsp;19旅行是将有资质的火车票代售点和互联网提供的火车票服务信息汇集于平台，为19旅行乘客提供互联网信息查询服务。在涉及到具体服务的过程中的问题，我们会将尽力协助用户与相关服务提供商进行协商，不能协商解决的，用户自己可以向消费者协会投诉或通过法律途径解决。</p>'
				    +'<p class="dialog_p" style="font-weight:bold;">三、服务风险提示</p>'
				    +'<p class="dialog_p">&nbsp;&nbsp;&nbsp;&nbsp;19旅行的火车票代购服务提供给用户在预售期以内的火车票代购服务，由于火车票的购买不可预见性，本公司并不保证代购系统一定可以完全满足用户周边用户的需求，公司会尽量协调相关资源提供给用户优质的服务。</p>'
				    +'<p class="dialog_p" style="font-weight:bold;">四、关于服务中涉及的款项问题的说明</p>'
				    +'<p class="dialog_p">&nbsp;&nbsp;&nbsp;&nbsp;在使用业务期间发生的支付、退款和补款过程，均在本公司提供的账户系统下操作完成。</p>'
				    +'<p class="dialog_p">&nbsp;&nbsp;&nbsp;&nbsp;<font color="#0088CC">关于支付：</font>用户通过平台进行的每一次交易，均按账户系统规定流程与机制处理完成，且在交易结束后会产生一个交易号，用户可通过交易号可以查询该次交易的详情信息。</p>'
				    +'<p class="dialog_p">&nbsp;&nbsp;&nbsp;&nbsp;<font color="#0088CC">关于补款：</font>火车票存在多种坐席及车次可以满足用户的需求，所以存在购票过程中会产生差价的问题。其中差价的处理本着多退少补的原则，当使用了19旅行所提供的相关服务，就表示该用户接受此项原则，并能及时地进行补款操作以保证服务的正常进行。对未能及时补款造成的后果由用户承担。</p>'
				    +'<p class="dialog_p">&nbsp;&nbsp;&nbsp;&nbsp;<font color="#0088CC">关于退款：</font>未出票成功的订单公司系统会24小时内可退至原支付账户中；出票成功后的退款需要在15个工作日内抛去5%的手续费的金额退还给账户中。 对退款过程中产生的延时不予负责。</p>'
				    +'<p class="dialog_p">&nbsp;&nbsp;&nbsp;&nbsp;<font color="#0088CC">关于保险：</font>在购票过程中，部分车票有保险套餐，代理商可以帮助用户选择是否需要购买交通意外险。收费标准： 20元保额65万，具体价格以平台为准；每一位乘车人最多可购买5份。最后在订票成功以后，19旅行会以短信形式发送保单号和订单号到用户在平台上预留的手机上。</p>'
				    +'<p class="dialog_p" style="font-weight:bold;">五、免责条款</p>'
				    +'<p class="dialog_p">&nbsp;&nbsp;&nbsp;&nbsp;在系统服务期间，19旅行将有权根据需要，随时对产品的服务内容进行更新或删除，且无需另行通知或取得用户的同意。</p>'
				    +'<p class="dialog_p">&nbsp;&nbsp;&nbsp;&nbsp;19旅行提供的火车票信息、列车时刻信息、客票余额信息、座位信息、票价等信息全部来自互联网，在代购的火车票的车次、票价、始发时间、到站时间等以实际为准，本公司对此等信息的准确性、完整性、合法性或真实性均不承担责任。</p>'
				    +'<p class="dialog_p" style="font-weight:bold;">六、其他</p>'
				    +'<p class="dialog_p">&nbsp;&nbsp;&nbsp;&nbsp;1.如本协议的任何条款被视作无效或无法执行，则上述条款可被分离，其余部分则仍具有法律效力。</p>'
				    +'<p class="dialog_p">&nbsp;&nbsp;&nbsp;&nbsp;2.此版本为19旅行所有并享有一切解释权利。</p>'
				    
			,ok: function(){
				return true;
			}
		});
	} 

	//显示《保险说明》 
	function showBaoXianDesc(){
		//消息框
		var dialog = art.dialog({
			lock: true
			,fixed: true
			,width: '80%'
			,height: '40%'
			,modal: true //蒙层
			,left: '50%'
			,top: '50%'
			,title: '保险说明'
			,okVal: '我知道了'
			,content: '<p class="dialog_p" style="font-weight:bold;text-align:center;">“合众人寿网销火车意外险传世1号”保险说明</p>'
				    +'<p class="dialog_p">1.保险名称：合众人寿网销火车意外险传世1号</p>'
				    +'<p class="dialog_p">2.保额：火车意外伤害65万元</p>'
				    +'<p class="dialog_p">3.保险费：20元</p>'
				    +'<p class="dialog_p">4.保险生效时间： 单次火车（仅限当日当次），可当天投保当天生效（投保时间必须早于保险责任生效时间2个小时），或投保时所指定的生效时间，可提前30天投保。</p>'
				    +'<p class="dialog_p">5.限购份数：1份/人</p>'
				    +'<p class="dialog_p">6.保障年龄：18-65周岁</p>'
				    +'<p class="dialog_p">7.投保人与被保险人关系：仅限本人</p>'
				    +'<p class="dialog_p">8.证件类型：身份证、护照、军人证</p>'
				    +'<p class="dialog_p">9.受益人：法定</p>'
				    +'<p class="dialog_p">10.退保规则: 保险责任正式开始之前可全额退保</p>'
				    +'<p class="dialog_p">11.法律效力：数据电文是合法的合同表现形式，电子保单与纸质保单具有同等法律效力，请妥善保存。电子保单可在保险公司网站上查询和下载。</p>'
				    +'<p class="dialog_p">12.保单验真：投保人可根据保险单号及投保人姓名登陆以下网址<a href="http://www.unionlife.com.cn/tab147/" target="_blank">http://www.unionlife.com.cn/tab147/</a>进行保单验真，您也可根据保险单号登陆以下网址<a href="http://www.unionlife.com.cn/tab144/" target="_blank">http://www.unionlife.com.cn/tab144/</a>查询保单相关信息。您可凭保单号登陆以下网址<a href="http://www.unionlife.com.cn/tab570/" target="_blank">http://www.unionlife.com.cn/tab570/</a>下载相关保险电子凭证。</p>'
				    +'<p class="dialog_p">13.本产品详细条款请以<a href="http://www.unionlife.com.cn/Portals/0/hzsjasjt2013.pdf" target="_blank">《合众世纪安顺交通工具意外伤害保险（2013修订）条款》</a>为准。</p>'
				    
			,ok: function(){
				return true;
			}
		});
	} 
	
    //显示保险说明
	function showBxDesc(){
		//消息框
		var dialog = art.dialog({
			lock: true,
			fixed: true,
			left: '50%',
			top: '50%',
		    title: '保险说明',
		    okVal: '确认',
		    content: '<p style="width:250px;height:60px;line-height:20px;">温馨提示：我们将为您提供代购交通意外险服务。如您出票成功后，再进行申请退款的同时，保险金额将不予退还，还请谅解。</p>',
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
		$('#num_tip').css({'position':'absolute', 'top':offset.top-38, 'left':offset.left, 'width':_width,'z-index':'9998'}).appendTo("body").show();
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
			var name = linkJsonInfo[id].link_name;
			var ticket_type = linkJsonInfo[id].ticket_type;
			var card_type = linkJsonInfo[id].ids_type;
			var card_num = linkJsonInfo[id].user_ids;
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
		//验证Cookie中是否已经存在身份证号为num乘客
		function checkCookiePax(name,ticket_type,num){
			var result = false;
			if(linkJsonInfo==null||linkJsonInfo==undefined){
				return result;
			}
			var size = linkJsonInfo.length;
			for(var i=0; i<size; i++){
				if(ticket_type==1){	//儿童乘客若num和name都存在则存在
					if(num == linkJsonInfo[i].card_num && name == linkJsonInfo[i].name){
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
			jsonstr = jsonstr.substring(0,jsonstr.lastIndexOf(',')); 
			jsonstr += "]";
			$.ajax({
				url:"/chunqiu/userIdsCardInfo/saveUserIdsCardInfo.jhtml",
				type: "POST",
				cache: false,
				data: {'data':jsonstr},
				success: function(res){
				}
			});
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
	            //-------------
    			var jsonstr = "{\"link_name\""+ ":" + linkJsonInfo[id].link_name + ",";
				jsonstr += "\"ids_type\""+ ":" + linkJsonInfo[id].ids_type + ",";
				jsonstr += "\"user_ids\""+ ":" + linkJsonInfo[id].user_ids + ",";
				jsonstr += "\"ticket_type\""+ ":" + linkJsonInfo[id].ticket_type + "}";
				//--------------
				//删除该乘客信息
    			$.ajax({
    				url:"/chunqiu/userIdsCardInfo/deleteUserIdsCardInfo.jhtml",
    				type: "POST",
    				cache: false,
    				//data: {'data':cookieStr},
    				data: {'data':jsonstr},
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
        
    	$(function(){
    		$(".select").each(function(){
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
        			if($(this).html()=="不购买保险"){
	        			$("#train_product").val('BX_NO');
	        			param_product_id = 'BX_NO';
	        			$("#visitant_service").hide();
	    				$("#normal_service").show();
	    				//保险发票隐藏
	    				$("#fp_content").hide();
	    				$("#bx_confirm").hide();
            		}else{
            			$("#train_product").val('BX_20');
            			param_product_id = 'BX_20';
            			$("#visitant_service").show();
        				$("#normal_service").hide();
        				//保险发票显示
        				$("#fp_content").show();
        				$("#bx_confirm").show();
                	}
        			_hide();
        		});     //选择效果（如需要传值，可自定义参数，在此处返回对应的“value”值 ）
    			$("body").click(function(i){ !$(i.target).parents(".select").first().is(s) ? _hide():"";});
    		})
    	})
    	
		//升级VIP服务
        $("#upgrade_service").click(function(){
        	$("#product_0").siblings("span").attr("class","train_product");
        	$("#product_0").attr("class","train_product_checked");
        	//$("#gepf").html($("#product_0").text());
        	$("#train_product").val('BX_20');
        	$("#content_bx").html("20元保额65万");
        	param_product_id = $("#train_product").val();
        	$("#visitant_service").show();
			$("#normal_service").hide();
			//保险发票显示
			$("#fp_content").show();
			$("#bx_confirm").show();
        });

    	//排列常用乘客信息展示
    	function passengerShow(){
			$(".usual-passenger ul.oz li").remove(); 
	       	if(jsonLinkList!="" && jsonLinkList!=undefined && jsonLinkList!=null){
	           	linkJsonInfo = JSON.parse(jsonLinkList);
	       		var size = linkJsonInfo.length;
	       		if(size >= 5){
	       			$("#ser-more").show();
			    }else{
			    	$("#ser-more").hide();
				}
	   	       	for(var i=0;i<size;i++){
	   		       	var newCard = "cookie_"+i;
	   		       	if(i<=4){
	   		       		$(".usual-passenger ul.oz").append("<li id=\""+ newCard +"\"><input type=\"checkbox\" id=\""+newCard+"_for\" class=\""+newCard+"\" onclick='addCommonManager(\""+ newCard +"\");'/><label for=\""+ newCard +"_for\">"+linkJsonInfo[i].link_name+"</label><a title=\"删除乘客\" href='javascript:delHtml("+i+")'></a></li>");
		   		    }else{
		   		    	$(".usual-passenger ul.oz").append("<li class=\"pass_hidden\" style=\"display:none;\" id=\""+ newCard +"\"><input type=\"checkbox\" id=\""+newCard+"_for\" class=\""+newCard+"\" onclick='addCommonManager(\""+ newCard +"\");'/><label for=\""+ newCard +"_for\">"+linkJsonInfo[i].link_name+"</label><a title=\"删除乘客\" href='javascript:delHtml("+i+")'></a></li>");
			   		}
	           	}
	   	       	$("#pax_passenger").show();
	        }else{
	           	$("#pax_passenger").hide();
	        }
		}
</script>
</body>
</html>
