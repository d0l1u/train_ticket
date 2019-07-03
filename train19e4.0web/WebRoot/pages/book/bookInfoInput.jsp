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
<link rel="stylesheet" href="/css/jquery.autocomplete.css" type="text/css" />

<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/js/artDialog.js"></script>
<script type="text/javascript" src="/js/idCard.js"></script>
<script type="text/javascript" src="/js/json2.js"></script>
<script type="text/javascript" src="/js/jquery.autocomplete.js"></script>
<script type="text/javascript" src="/js/city.js"></script>
</head>

<body>
	<div class="content oz">
		<div class="index_all">
		
<!--左边内容 start-->
	<jsp:include flush="true" page="/pages/common/menu.jsp">
					<jsp:param name="menuId" value="book" />
				</jsp:include>
<!--左边内容 end-->
    	<!--右边内容 start-->
    	<div class="infoinput-right oz">
    		<div class="tip_term oz" style="width:785px;margin:0 0 10px 0;">
            <p class="price_tip"></p>
            </div>
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
            	<input type="hidden" name="choose_ext" id="choose_ext" />
            	<input type="hidden" name="choose_type_value" id="choose_type_value" value="choose_type_11"/>
            	
            <div class="pub_order_mes ticket_mes oz mb10_all" style="margin:0;">
            	<div class="pub_ord_tit">
                    <h4 class="fl">车次信息</h4>
                    <p class="tit_tip">（数据仅作参考，请以实际出票情况为准）</p>
                    <!-- <input type="button" class="btn" value="重选车次" onclick="javascript:history.back(-1);"/> -->
                </div>
                <div class="new_pub_con">
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
	                       <c:if test="${ps_order_status eq '11'}"><br />在下方“取票方式”处选择“送票上门”时，可选下铺。</c:if></p></td>
	                      	<!-- <br />假如硬座余票不足并勾选了<同意硬座无票时购买无座>，将会给用户出无座票,否则出票失败。</p></td> -->   	
                     	</tr>
                    </table>
                </div>
                
            </div>
            
            <div class="book_passager_mes oz mb10_all">
            	<div class="pub_ord_tit">
                    <h4 class="fl" id="passenger_info">乘客信息</h4>
                    <p class="tit_tip">（一个订单最多可代购5张票，请务必填写乘车人真实信息，<span onclick="javascript:showGpDesc();">购票说明</span>）</p>
                </div>
                <div class="new_pub_con delivery_pos oz">
                	<div class="usual-passenger" id="pax_passenger" style="border:0;background-color:#EAF2FC;">
		                <ul id="useManager" class="oz">
		                </ul>
		                <div class="down-arrow" id="ser-more"><a href="javascript:void(0);">更多</a></div>
	            	</div>
	                <div class="delivery_pos_child oz">
	                	<div style="padding-bottom:10px;">
		                	<table class="table_list" cellpadding="0" cellspacing="0" id="train_ticket_list" style="position:relative;z-index:999;width:97%;margin:10px;">
		                    	<tr>
		                    		<td width="25">&nbsp;</td>
		                        	<td width="15"></td>
		                        	<td width="100">姓 名</td>
		                            <td width="90">类 型</td>
		                            <td width="120">证件类型</td>
		                            <td width="135">证件号码</td>
		                            <td width="110">&nbsp;</td>
		                            <td width="90">&nbsp;</td>
		                        </tr>
		                        <tr class="adult" style="display: none;">
		                        	<td><span class="delPerson fl" id="delPerson_index_source"><a href="javascript:void(0);"><s></s></a></span></td>
		                        	<td class="indexTr" id="index_index_source">index_source</td>
		                            <td><input style="width:90px;border:1px solid #dadada;" type="text" name="bookDetailInfoList_source.user_name" id="user_name_index_source" 
		                            		class="text text_name user_name_text" title="姓名" onblur="hideMsg(this);" onkeyup="value=value.replace(/(^\s*)|(\s*$)/g,'');" /></td>
		                            <td>
		                            	<select style="width:70px;" name="bookDetailInfoList_source.ticket_type" id="ticket_type_index_source" class="ticket_type_select" onchange="toggleTip(index_source);">
		                                	<option value="0">成人票</option>
		                                	<option value="1">儿童票</option>
		                                	<option value="3">学生票</option>
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
		                            		id="user_ids_index_source" title="证件号码" onblur="hideMsg(this);hideNumMsg(this);toggleTip(index_source);"
		                            		onfocus="getNumbers(this,'idcard');" onkeyup="value=value.replace(/(^\s*)|(\s*$)/g,'');getNumbers(this,'idcard');" /></td>
		                            <td>
		                            	<div class="savePaxDiv fl"><input id="savePax_index_source" class="savePax_index_source" type="checkbox" /><label for="savePax_index_source" style="cursor:hand;">保存为常用乘客</label></div>
		                            </td>
		                            <td nowrap="nowrap">
		                            	<span class="fl addChildren" style="display:inline;" id="addChildren_index_source"><a href="javascript:void(0);">添加随行儿童</a></span>
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
                           		<span class="add_adult"></span>
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
                	</div>
                </div>
                
               <div class="book_passager_mes oz mb10_all">
						<div class="book_passager_mes oz">
							<div class="pub_ord_tit">
								<h4 class="fl">12306账号预订</h4>
								<p class="tit_tip">（提高出票成功率，避免由于身份信息被冒用和待核验出票失败，非必选项，建议使用乘客本人帐号预定）</p>
							</div>
						</div>
						<div class="new_pub_con">
							<div class="insurance_wrap oz">
								<dl class="oz">
									<dd>
									<span style="color: red;"></span>12306用户名 ：<span><input
											type="text" class="link_text width120" 
											name="userName12306" /> </span>
									<span style="color: red;"></span>12306密码 ：<span><input
											type="text" class="link_text width120" 
											name="password12306" 
											style="inline-height: 22px;" /></span>
									</dd>
								</dl>
							</div>
						</div>

					</div>
					
				<div class="book_passager_mes oz mb10_all" id="stu_div">
						<div class="book_passager_mes oz">
							<div class="pub_ord_tit">
								<h4 class="fl">学生信息:</h4>
								<p class="tit_tip">（若本次订单中存在学生票,请您如实填写学生信息）</p>
							</div>
						</div>
						
						<div class="new_pub_con" id="stu_info_div_1" >
							<div style="margin-top: 10px; text-align: center;" id="stu_bt_div">
								<input type="button" value="填写学生信息" id="showStuGrid"
								class="infoinput_submit" onclick="showStu();" />
							</div>
						</div>
					</div>

						<input type="hidden" id="stu_infos" name="stu_infos" value="">
				
               <div class="book_passager_mes oz mb10_all">
                	<div class="book_passager_mes oz">
		            	<div class="pub_ord_tit">
		                    <h4 class="fl">交通意外险</h4>
		                    <p class="tit_tip">（若购买保险，每人一份）</p>
		                </div>
		            </div>
		            <div class="new_pub_con">
	                <div class="insurance_wrap oz">
				        <dl class="oz">
				            <dd>
				            	<div class="info_txt1">
				                	<div class="info-select oz">
					                	<c:forEach items="${productList}" var="product" varStatus="status">
					                		<!-- 
	                                		<span class="train_product" type="radio" id="train_product${status.index}" value="${product.product_id}"> <label>${product.name}</label></span>
	                                		 -->
	                                		<input id="train_product_${status.index}" value="${product.product_id}" type="hidden"/>
	                                		<span class="train_product" type="radio" id="product_${status.index}" > <label style="margin-right:35px;">${product.name}</label></span>
	                            		</c:forEach>
                            		</div>
				                </div>
				                <div class="info_txt1 oz" id="free_service">
				                	<table style="border:0px; margin:0px; padding:0px; height:50px;">
				                		<tr style="height:25px; border:0px; margin:0px; padding:0px;">
				                			<td style="height:25px; border:0px; margin:0px; padding:0px;"><p class="info_icon1 fl">极速出票：如遇排队优先处理</p></td>
				                			<td style="height:25px; border:0px; margin:0px; padding:0px;"><p class="info_icon1 fl">短信通知：购票失败有短信通知服务</p></td>
				                		</tr>
				                		<tr  style="height:25px; border:0px; margin:0px; padding:0px;">
				                			<td style="height:25px; border:0px; margin:0px; padding:0px;"><p class="info_icon1 fl">人工服务：提供人工退票服务</p></td>
				                			<td style="height:25px; border:0px; margin:0px; padding:0px;"><p class="info_icon1 fl">赠送保险：每笔订单赠送10万元交通意外险</p></td>
				                		</tr>
				                	</table> 
				                	<!-- 
				                	<p class="info_icon1 fl">提前预售：可提前30天预售购买火车票</p>
				                    <p class="info_icon1 fl" style="MARGIN: 6px 10px 2px;">短信通知：购票失败有短信通知服务</p>
				                    <p class="info_icon1 fl" style="MARGIN: 6px 10px 2px;">人工服务：提供人工退票服务</p>
				                    <p class="info_icon1 fl" style="MARGIN: 6px 10px 2px;">赠送保险：每笔订单赠送10万元交通意外险</p> -->
				                </div>
				                <div class="info_txt2" id="visitant_service">
				                	<p class="info_icon1 fl">高额赔付：人均<span id="gepf"></span></p>
				                    <p class="info_icon1 fl">出票加速：极速优先处理订单</p>
				                    <p class="info_icon1 fl">人工服务：提供人工退票服务</p>
				                </div>
				                <!-- 您前面约有${wait_amount}人， -->
				                <div class="info_txt2" id="normal_service">
				               <!--
				                <table style="border:0px; margin:0px; padding:0px; height:50px;">
				                		<tr style="height:25px; border:0px; margin:0px; padding:0px;">
				                			<td style="height:25px; border:0px; margin:0px; padding:0px;">
				                			<p class="info_icon1 fl">该免费险由中国平安提供；</p></td>
				                		</tr>
				                		<tr  style="height:25px; border:0px; margin:0px; padding:0px;">
				                			<td style="height:25px; border:0px; margin:0px; padding:0px;">
				                			<p class="info_icon1 fl">免费增险：领取年龄为25-45周岁，每人限领一份；</p></td>
				                		</tr>
				                		<tr  style="height:25px; border:0px; margin:0px; padding:0px;">
				                			<td style="height:25px; border:0px; margin:0px; padding:0px;">
				                			<p class="info_icon1 fl">信息变更：此增险不可受理任何变更，投保时请正确填写投保信息；</p></td>
				                		</tr>
				                		<tr style="height:25px; border:0px; margin:0px; padding:0px;">
				                			<td style="height:25px; border:0px; margin:0px; padding:0px;">
				                			<p class="info_icon1 fl">您可通过中国平安网站或客服电话95511进行保单查询；</p></td>
				                		</tr>
				                		<tr  style="height:25px; border:0px; margin:0px; padding:0px;">
				                			<td style="height:25px; border:0px; margin:0px; padding:0px;">
				                			<p class="info_icon1 fl">平安会在5个工作日之内将保单发送到您的手机上，请注意查收；</p></td>
				                		</tr>
				                		<tr  style="height:25px; border:0px; margin:0px; padding:0px;">
				                			<td style="height:25px; border:0px; margin:0px; padding:0px;">
				                			<p class="info_icon1 fl">中国平安之后会给您致电做相关的服务或者咨询，敬请留意。</p></td>
				                		</tr>
				                		<tr  style="height:25px; border:0px; margin:0px; padding:0px;">
				                			<td style="height:25px; border:0px; margin:0px; padding:0px;">
				                			 <p  style="background:none;">&nbsp;您可以选择SVIP服务，升级服务<a  class="vip_btn" id="upgrade_service">升级SVIP服务</a></p>
				                		</tr>
				                	</table> -->
				                	<p class="info_icon2">&nbsp;优先购票服务：您选择的是不含保险或服务的普通排队代购，订单处理约需<span style="color:red;font-weight:bold;">${wait_time}</span>，最迟<span style="color:red;font-weight:bold;">次日22点前</span>回复是否代购成功</p>
									<p class="info_icon2">&nbsp;提前70天预约购票服务 </p>
				                    <p class="info_icon2">&nbsp;提供人工退票服务 </p>
				                    <p class="info_icon2">&nbsp;购票失败短信通知服务 </p>
				                    <!-- 
				                    <p class="info_icon2">服务提醒：①不提供30天预约购买火车票服务 ②不提供人工退票 ③购票失败不提供短信通知服务 </p>
				                    <p style="background:none;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;②不提供人工退票</p>
				                    <p style="background:none;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;③购票失败不提供短信通知服务</p>
				                     -->
				                      <!--
				                    <p  style="background:none;">&nbsp;您可以选择SVIP服务，升级服务<a  class="vip_btn" id="upgrade_service">升级SVIP服务</a></p>
				               			-->
				                </div>
				            </dd>
				        </dl>
				     </div>
                </div>

            </div>
			<div class="pub_delivery_mes oz mb10_all" id="tickets-way">
			   	<div class="pub_ord_tit">
        	    	<h4 class="fl">取票方式</h4>
	                <p class="tit_tip">（取票规则：开车前可在任何火车站或代售点自由取票，<span onclick="javascript:showQpDesc();" id="qupiao">取票说明</span>）</p>
                </div>
				<!--  
            	<h5><span id="outtickettype_22">送票上门</span><span class="current" id="outtickettype_11">车站自提</span></h5>
            	-->
            	
            			<style>
                    	.type_22 td{padding-left:80px;line-height: 0px;}
                    	.type_class{ font-size: 14px; cursor:pointer;}
                    	.checked_type{BACKGROUND: url(../images/radio-checked.png) no-repeat;display:inline-block;height: 17px;width: 17px;background-position: center;}
                    	.unchecked_type{BACKGROUND: url(../images/radio-unchecked.png) no-repeat; display:inline-block;height: 17px;width: 17px;background-position: center;}
                    	</style>
                <div class="new_pub_con">
                    <table class="pub_table current"  style="margin:10px 0px 10px 40px;">
                    	<tr>
                    	<td colspan="3" class="height30">
                    	<span  class="type_class" id="check_class_11"><span class="checked_type" id="check_type_11"></span><label>车站自提</label></span> 
                    	  <c:if test="${ps_order_status eq '11'}">
                    	  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="/images/iconred.gif">自取票卧铺铺位是随机分配的，如需购买下铺票，请选择送票上门。
                    	   </c:if>
                    	  </td>
                    	</tr>
                    	<tr class="type_11"  id="table_type_11">
                        	<td width="234"><span style="color:red;">*</span>联 系 人：<span><input type="text" class="link_text width120" id="link_name" name="link_name" /></span></td>
                        	<td><span style="color:red;">*</span>手 机：<span><input type="text" class="link_text width120" id="link_phone" name="link_phone"  
                        						onblur="hideNumMsg(this);" onkeyup="getNumbers(this,'phone');" onfocus="getNumbers(this,'phone');" style="inline-height:22px;"/></span>
                        						</td>
                        	<!-- <td>邮 箱：<span><input type="text" class="link_text width120" id="link_mail" name="link_mail" /></span></td>-->
                       		
                        </tr>
                        <tr>
                        <c:if test="${ps_order_status eq '11'}">
                    	<td colspan="3" class="height30">
                    	<span  class="type_class" id="check_class_22">
                    	<span class="unchecked_type" id="check_type_22"></span><label>送票上门</label></span> 
                    	<img src="/images/icon_new.gif">&nbsp;<img src="/images/iconred.gif">目前仅支持北京地区送票上门。
                    	</td>
                        </c:if>
                    	</tr>
                    	
                    	 <!-- 
                        <tr>
                        	<td colspan="4">
                        		<p class="price_tip" style="margin-top: 15px;">手机号是接收订票成功短信通知的，请务必填写真实有效的手机号。</p>
                        	</td>
                        </tr>
                         -->
                        <tr id="addressInput" style="display: none;">
                        	<td colspan="3">收件地址：<span><input type="text" class="link_text width280" name="link_address" value="北京海淀区鼎好大厦"  /></span></td>
                        </tr>
                    </table>
                      <table class="pub_table current type_22"  style="margin:10px 0px 10px 40px;display: none;" id="table_type_22">
                    	<tr id="choose_type">
                    	<td colspan="3">购买下铺&nbsp;&nbsp;&nbsp;&nbsp;
                    	<select name="choose_seat_num" id="choose_seat_num">
                            	<option value="1">1张</option>
                            	<option value="2">2张</option>
                            	<option value="3">3张</option>
                            	<option value="4">4张</option>
                            	<option value="5">5张</option>
                            </select>
                        	&nbsp;下铺&nbsp;&nbsp;&nbsp;&nbsp;
                        <input type="checkbox" id="choose_agree" >同意下铺不足时，接受上中铺。</td>
                    	</tr>
                    	<tr id="choose_type_alert">
	                    	<td colspan="3">
	                    	&nbsp;&nbsp;&nbsp;&nbsp;<p class="price_tip">我们将根据您的选择为您购票，下铺票不足时，将会导致购票失败。</p>
	                    	</td>
                    	</tr>
                    	<tr>
	                    	<td colspan="3">
	                    	联系人&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" class="link_text width120" id="link_name_ps" name="link_name_ps" />
	                    	</td>
                    	</tr>
                    	<tr>
	                    	<td colspan="3">
	                    	手机号&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span><input type="text" class="link_text width120" id="link_phone_ps" name="link_phone_ps"  
                        						onblur="hideNumMsg(this);" onkeyup="getNumbers(this,'phone');" onfocus="getNumbers(this,'phone');" style="inline-height:22px;"/></span>
	                    	</td>
                    	</tr>
                    	<tr>
	                    	<td colspan="3">
	                    	所在省市&nbsp;&nbsp;&nbsp;&nbsp;
	                    	<select name="province" id="province" disabled="disabled" style="background-color:#FFFFCD;">   
							<option value="0">请选择省份</option> 
							</select> 
							<select name="city" id="city" disabled="disabled" style="background-color:#FFFFCD;">  
							<option value="0">请选择城市</option> 
							</select> 
							<select name="district" id="district" style="background-color:#FFFFCD;">   
							<option value="0">请选择区县</option> 
							</select> 
							
							<input type="hidden" value="110000" id="pre_province"/> 
							<input type="hidden" value="110100" id="pre_city"/> 
							<!--下列为初始值(可选,编辑表单时设置)
							<input type="hidden" value="110108" id="pre_district"/> 
							--> 
								                    	
	                    	</td>
                    	</tr>
                    	<tr>
	                    	<td colspan="3">
	                    	详细地址&nbsp;&nbsp;&nbsp;&nbsp;<span><input type="text" class="link_text" style="width:380px;color:#aaa" id="ps_address" name="ps_address" 
                        		value="请填写可以收件的真实地址（包括省市区）" onfocus="if(this.value=='请填写可以收件的真实地址（包括省市区）'){this.value=''};this.style.color='#333';" 
                    			onblur="if(this.value==''||this.value=='请填写可以收件的真实地址（包括省市区）'){this.value='请填写可以收件的真实地址（包括省市区）';this.style.color='#aaa';}"/></span>
	                    	</td>
                    	</tr>
                    	<tr>
	                    	<td colspan="3">
	                    	<p class="price_tip">配送费<font style="font-weight: bold; color: red;">￥20</font> + 纸质票费<font style="font-weight: bold; color: red;">￥5</font>，今天16点前完成支付，明天17点前送到！</p>
	                    	</td>
                    	</tr>
                        <!-- 
                        <tr>
                        	<td colspan="4">
                        		<p class="price_tip" style="margin-top: 15px;">手机号是接收订票成功短信通知的，请务必填写真实有效的手机号。</p>
                        	</td>
                        </tr>
                         -->
                        <tr id="addressInput" style="display: none;">
                        	<td colspan="3">收件地址：<span><input type="text" class="link_text width280" name="link_address" value="北京海淀区鼎好大厦"  /></span></td>
                        </tr>
                    </table>
                      <!-- 
                       <input type="button" value="提交保险" onclick="javascript:submitInsurance();">
                       -->
                </div>
        	</div>
        	
        	<div class="pub_delivery_mes oz mb10_all" id="fp_content" style="display:none;">
			   	<div class="pub_ord_tit">
        	    	<h4 class="fl">保险信息</h4>
	                <p class="tit_tip"></p>
                </div>
                <div class="new_pub_con">
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
        	 	<span style="color:#0081cc;cursor:pointer;line-height:24px;" onclick="window.open('<%=basePath%>pages/guide/daiGou.jsp');">《火车票线下代购服务协议》</span>、
        	 	<span style="color:#0081cc;cursor:pointer;line-height:24px;" onclick="window.open('<%=basePath%>pages/guide/chengnuoPage.jsp');">《平台购票承诺书》</span>
        	 	<span id="bx_confirm">和<span style="color:#0081cc;cursor:pointer;line-height:24px;" onclick="window.open('http://www.unionlife.com.cn/tab674/');">《保险说明》</span></span>
        	 </p>
             <p class="tijiao"><input type="button" value="提交订单" id="btnSubmit" class="infoinput_submit" /></p>
            </form>
        </div>
        <!--右边内容 end-->
    </div>
    </div>
<div class="insurance_wrap oz" id="stu_div_0" style="display: none;">
							<span>学生姓名：<input type="text"
												class="link_text auto" id="stu_name_0" name="stu_name_0"
												style="inline-height: 22px;" value="@@" /></span>
								<table class="pub_table current"
									style="margin: 10px 0px 10px 40px;">
									<tr class="type_11" id="table_type_11">
										<td align="right"><span style="color: red;">*</span>学校省份：</td>
										<td align="left"><span><input type="text"
												class="link_text width120 auto" id="province_name_0" name="province_name_0"
												style="inline-height: 22px;" /></span></td>
												<td align="right"><span style="color: red;">*</span>学校名称：</td>
										<td align="left"><span><input type="text"
												class="link_text width120 auto" id="school_name_0" name="school_name_0"
												style="inline-height: 22px;" /></span></td>
												<td align="right"><span style="color: red;">*</span>学号：</td>
										<td align="left"><span><input type="text"
												class="link_text width120" id="student_no_0" name="student_no_0"
												style="inline-height: 22px;" /></span></td>
									</tr>
									<tr class="type_11" id="table_type_11">
										<td align="right"><span style="color: red;">*</span>入学年份：</td>
										<td align="left"><span><input type="text"
												class="link_text width120 auto" id="enter_year_0" name="enter_year_0"
												style="inline-height: 22px;" /></span></td>
										<td align="right"><span style="color: red;">*</span>优惠区间：</td>
										<td align="left"><span>
												<input type="text"
												class="link_text auto" id="preference_from_station_name_0" name="preference_from_station_name_0"
												style="width: 50px;" />--<input type="text" 
												class="link_text auto" id="preference_to_station_name_0" name="preference_to_station_name_0"
												style="width: 50px;" />
												</span></td>
												<td align="right"><span style="color: red;">*</span>学制：</td>
										<td align="left"><span><input type="text"
												class="link_text width120 auto" id="school_system_0" name="school_system_0"
												style="inline-height: 22px;" /></span></td>
									</tr>
								</table>
							</div>
    <div id="tip" class="tip_wrap" style="display: none;"><div class="tip"><b></b><span class="errMsg"></span></div></div>
    <div id="num_tip" class="num_tip_wrap" style="display: none;"><div class="tip"><b></b><span class="numMsg"></span></div></div>
<script type="text/javascript">
	var jsonLinkList = JSON.stringify(${jsonLinkList})+"";

	var linkJsonInfo = "";	//jsonLinkList解析后的json集合
	var linkList = ${jsonList};
	
	var param_product_id = "";	//传递给后台的保险ID值
	
	 $("#check_class_11").click(function(){
		document.getElementById("check_type_11").className='checked_type';
		document.getElementById("check_type_22").className='unchecked_type';
		document.getElementById("table_type_11").style.display='block';
		document.getElementById("table_type_22").style.display='none';
		document.getElementById("choose_type_value").value='choose_type_11';
		hideErrMsg("choose_seat_num");
		hideErrMsg("link_name_ps");	
		hideErrMsg("link_phone_ps");
		hideErrMsg("province");
		hideErrMsg("city");
		hideErrMsg("district");
		hideErrMsg("ps_address");
	});
	
	$("#check_class_22").click(function(){
		showPsCity();
		document.getElementById("check_type_22").className='checked_type';
		document.getElementById("check_type_11").className='unchecked_type';
		document.getElementById("table_type_22").style.display='block';
		document.getElementById("table_type_11").style.display='none';
		document.getElementById("choose_type_value").value='choose_type_22';
		var count = $(".adult:visible,.child:visible").length;
		document.getElementById("choose_seat_num").value = count;
		hideErrMsg("link_name");
		hideErrMsg("link_phone");
	});
	
    $(document).ready(function(){
    	changeHeight(330);//增加130px的高度（普通购票过长的高度）
    	$("#bx_confirm").hide();
    //	var travelTime = $.trim($("#travelTime").val());//出发时间
		//		//30天后的日期是
			//	if( adddays(1)>travelTime ){
				//	$("#free_service").hide();
					//$("#normal_service").show();
					//}
    	$("#seat_type").focus();
        //是否显示卧铺提醒
        $("#seat_type").click(function(){
			var seatType = $(this).val();
			if(seatType=='4' || seatType=='5' || seatType=='6'){
				$("#sleeper_alter").show();
				$("#choose_type").show();
				$("#choose_type_alert").show();
			}else{
				$("#sleeper_alter").hide();
				$("#choose_type").hide();
				$("#choose_type_alert").hide();
			}
        }).trigger("click");
    	//$("#train_product0").attr("class","train_product_checked");
    	//param_product_id = $("#train_product0").val();
    	//$("#gepf").html($("#train_product0").text());
    	var travelTime = $.trim($("#travelTime").val());//出发时间
				//30天后的日期是
	//			if( adddays(1)>travelTime ){
    //	$("#product_2").attr("class","train_product_checked");
    //	param_product_id = $("#train_product_2").val();
    //	$("#gepf").html($("#product_2").text());
    //	}else{
    	$("#product_0").attr("class","train_product_checked");
    	param_product_id = $("#train_product_0").val();
    	$("#gepf").html($("#product_0").text());
    //	}
    	
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
    	//交通意外险选框触发
    	$(".info-select span").click(function(){
			$(this).siblings("span").attr("class","train_product");
			$(this).attr("class","train_product_checked");
			var str = $.trim($(this).text());
			//param_product_id = $(this).val();
			var product_id = $(this).attr("id");
			var train_product_id ="train_"+product_id;
			param_product_id = $("#"+train_product_id).val();
			
			$("#gepf").html(str);
			if(str=='普通购票'){//不购买保险
				$("#visitant_service").hide();
				$("#free_service").hide();
				$("#normal_service").show();
				//保险发票隐藏
				$("#fp_content").hide();
				$("#bx_confirm").hide();
			}else if(str=='20元/份，保额65万'){
				$("#visitant_service").show();
				$("#normal_service").hide();
				$("#free_service").hide();
				//保险发票显示
				$("#fp_content").show();
				$("#bx_confirm").show();
			}else{//赠送保险
				$("#free_service").show();
				$("#visitant_service").hide();
				$("#normal_service").hide();
				//保险发票隐藏
				$("#fp_content").hide();
				$("#bx_confirm").hide();
			}
        });
		//升级VIP服务
        $("#upgrade_service").click(function(){
        	//$("#train_product0").siblings("span").attr("class","train_product");
        	//$("#train_product0").attr("class","train_product_checked");
        	//$("#gepf").html($("#train_product0").text());
        	//param_product_id = $("#train_product0").val();
        	$("#product_0").siblings("span").attr("class","train_product");
			$("#product_0").attr("class","train_product_checked");
			$("#gepf").html($("#product_0").text());
			param_product_id = $("#train_product_0").val();
        	
        	$("#free_service").show();
        	$("#visitant_service").hide();
			$("#normal_service").hide();
			//保险发票显示
			$("#fp_content").hide();
			$("#bx_confirm").hide();
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
		$(".add_adult").click(function(){
			var count = $(".adult:visible,.child:visible").length;
			//for(var i=1;i<=count;i++){
			//	$("#addChildren_"+count).hide();
			//}
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
			var html = $(".adult:hidden").html().replace(/index_source/g, count+1);
			html = html.replace(/bookDetailInfoList_source/g, replaceStr);
			$("<tr class=\"adult\">" + html + "</tr>").css("background-color","")
				.appendTo("#train_ticket_list").show();
			//每添加一行增加30px整体高度
			changeHeight(35);
			if(count==4){
				$("#add_person").attr("class","unableAdd");
			}
			//自动补全姓名
			$(".user_name_text").autocomplete(linkList, {
	            max: 12,    //列表里的条目数
	            minChars: 1,    //自动完成激活之前填入的最小字符
	            width: 90,     //提示的宽度，溢出隐藏
	            scrollHeight: 300,   //提示的高度，溢出显示滚动条
	            matchContains: true,    //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示
	            autoFill: false,    //自动填充
	            formatItem: function(row, i, max) {
	                return row.link_name;
	            },
	            formatMatch: function(row, i, max) {
	                return row.link_name;
	            },
	            formatResult: function(row) {
	                return row.link_name;
	            }
	        }).result(function(event, row, formatted) {
	            var id = $(this).attr("id");
	            var app = id.substring(id.length-1,id.length);
	            $("#ticket_type_"+app).val(row.ticket_type);
	            $("#ids_type_"+app).val(row.ids_type);
	            $("#user_ids_"+app).val(row.user_ids);
	        });
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
			//兼容IE
			$("#add_person").attr("style","");
			$(".savePaxDiv").attr("style","padding-top:4px;*+padding-top:0px;_padding-top:0px;");
		}).trigger("click");
		
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
			$(".savePaxDiv").attr("style","padding-top:4px;*+padding-top:0px;_padding-top:0px;");
		}
		
		//添加随行儿童
		$(".addChildren").live("click", function(){
			var id = $(this).attr("id").split('_')[1];//addChildren_1
			var name = $("#user_name_"+id).val(); 
			var ticket_type = "1";
			var ids_type = $("#ids_type_"+id).val();
			var user_ids = $("#user_ids_"+id).val();
			if(name.length==0){
				$("#user_name_"+id).focus();
				showErrMsg("user_name_"+id, "120px", "请输入乘客姓名！");
				return;
			}else if(user_ids.length==0){
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
			//alert("表格为："+$("#train_ticket_list").html());
			//每添加一行增加30px整体高度
			changeHeight(35);
			if(count==4){
				$("#add_person").attr("class","unableAdd");
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
					toggleTip(i);
				}
			}
			
			$("#user_name_"+(parseInt(id)+1)).val(name);
			$("#ticket_type_"+(parseInt(id)+1)).val("1");
			$("#ids_type_"+(parseInt(id)+1)).val(ids_type);
			$("#user_ids_"+(parseInt(id)+1)).val(user_ids);
			$("#user_name_"+(parseInt(id)+1)).prop("disabled", true);//设置为只读属性
			$("#ticket_type_"+(parseInt(id)+1)).prop("disabled", true);//设置为只读属性
			$("#ids_type_"+(parseInt(id)+1)).prop("disabled", true);//设置为只读属性
			$("#user_ids_"+(parseInt(id)+1)).prop("disabled", true);//设置为只读属性
			$("#child_tip").show();//添加随行儿童温馨提示--显示
			changeHeight(130);//增加100px整体高度
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
			//兼容IE
			$("#add_person").attr("style","");
			$(".savePaxDiv").attr("style","padding-top:4px;*+padding-top:0px;_padding-top:0px;");
		});
		
		//删除乘客
		$(".delPerson").live("click", function(){
			var id = $(this).attr("id").split('_')[1];//delPerson_1
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
			$(this).parents("tr").find(":text").each(function(){
				hideErrMsg($(this).attr("id"));
			});
			
			//重新设置index
			$(".adult:visible,.child:visible").each(function(index){
				var newIndex = index+1;
				$(this).html($(this).html().replace(/bookDetailInfoList\[\d+\]/g, "bookDetailInfoList["+index+"]"))
					.find(".indexTr").html(newIndex);
				$(this).html($(this).html().replace(/\_\d/g, "_"+newIndex));
				toggleTip(newIndex);//判断是否显示“儿童票温馨提示”
			});	
			$("#add_person").attr("class","ableAdd");
			//兼容IE
			$("#add_person").attr("style","");
			if($(".adult:visible,.child:visible").length>1){
				addChildrenDisplay();
			}
			
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
	   //出发日期大于20天则隐藏“普通购票”功能-----没用 
	    function serverSVIP(){
				//在【提交订单】页面如果代理商选择出发日期＞20天，隐藏“普通购票”功能
				//param_product_id 
				var travelTime = $.trim($("#travelTime").val());//出发时间
				//30天后的日期是
				if( adddays(1)<travelTime ){
					$("#free_service").show();
					
					//alert("222222 "+adddays(1)+"  "+travelTime+"   "+(adddays(1)<travelTime)+"   "+param_product_id);
					/**
					var dialog = art.dialog({
						lock: true
						,fixed: true
						,follow: document.getElementById('qupiao')
					    ,okVal: '我要参加'
						,cancel: true
						,cancelVal: '暂不参加'
					    ,content: '<p style="font:normal 12px/30px Simsun;">该预售30天车票功能仅限vip和svip用户使用！</p>'
						,ok: function(){
							//点击“我要参加”则在支付时加5元服务费
					    	$("#train_product0").siblings("span").attr("class","train_product");
				        	$("#train_product0").attr("class","train_product_checked");
				        	$("#gepf").html($("#train_product0").text());
				        	param_product_id = $("#train_product0").val();
				        	$("#free_service").show();
				        	$("#visitant_service").hide();
							$("#normal_service").hide();
							//保险发票显示
							$("#fp_content").show();
							$("#bx_confirm").show();

							submitForm();
						}
						,cancel: function(){
							//continueSub();
							return true;
						}
					});*/
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
				var jsonstr="["; 
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
					url:"/userIdsCardInfo/checkUserIdsCardInfo.jhtml",
					type: "POST",
					cache: false,
					data: {'data':jsonstr}, 
					success: function(res){
						if(res=='SUCCESS'){
							dialog.content('验证成功，可以订购！');
							//保存常用乘客信息
							savePaxInfo();
							var newCount = $(".adult:visible,.child:visible").length;
							for(var i=1;i<=newCount;i++){
								$("#user_name_"+i).prop("disabled", false);//取消只读属性
								$("#ticket_type_"+i).prop("disabled", false);//取消只读属性
								$("#ids_type_"+i).prop("disabled", false);//取消只读属性
								$("#user_ids_"+i).prop("disabled", false);//取消只读属性
							}
							
							var choose_type_value =$("#choose_type_value").val();
							if($("#chk_fp:visible").length>0 && $("#chk_fp").attr("checked")){
								$("form:first").attr("action", "/order/createOrder.jhtml?fpNeed=1&product_id="+param_product_id+"&choose_type_value="+choose_type_value);
							}else{
								$("form:first").attr("action", "/order/createOrder.jhtml?product_id="+param_product_id+"&choose_type_value="+choose_type_value);
							}
							$("form:first").submit();
						}else{
							var dataObj=eval("("+res+")");
							var strAll = "<p style='width:370px;color:#555;font: 12px/18px Simsun;padding-bottom:6px;'>";
							var errStr = "";
							$.each(dataObj.errorData,function(idx,item){
								if(item.status=='1'){
									errStr+=item.userName;
									strAll+=idx+1+"."+item.userName+"("+item.ids_card+") &nbsp;&nbsp;身份信息待核验<br />";
								}else{
									errStr+=item.userName;
									strAll+=idx+1+"."+item.userName+"("+item.ids_card+") &nbsp;&nbsp;身份信息未通过审核<br />";
								}
							});
							strAll+="<br />&nbsp;&nbsp;&nbsp;&nbsp;自2014年3月1日起，铁道部门规定未通过身份信息核验的用户无法网上购票，必须携带二代身份证到火车站核验之后方可网上购票!</p>";
							if(strAll!=''){
								dialog.content(strAll);
								dialog.title("提示");
								dialog.button({name: '确认',callback: function(){}});
								$(".aui_titleBar").show();
							}else{
								dialog.content('验证失败，请重试！');
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
				if($(".adult:visible,.child:visible").length==5){
					$("#addChildren_5").hide();
				}
			}

		});
		
		
		//验证数据
		function checkForm(){
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
			//根据取票方式核验信息
			if($("#choose_type_value").val()=="choose_type_11"){
				if($.trim($("#link_name").val())==""){
					showErrMsg("link_name", "110px", "请填写联系人！");
					return false;
				}else if(!checkName($.trim($("#link_name").val()))){
					showErrMsg("link_name", "150px", "请填写正确的联系人！");
					return false;
				}else{
					hideErrMsg("link_name");
				}
				if($.trim($("#link_phone").val())==""){
					showErrMsg("link_phone", "90px", "请填写手机！");
					return false;
				}else if(!/^1(3[\d]|4[57]|5[\d]|8[\d]|7[0678])\d{8}$/g.test($.trim($("#link_phone").val()))){
					showErrMsg("link_phone", "150px", "请填写正确的手机号！");
					return false;
				}else{
					hideErrMsg("link_phone");
				}
			//	submitInsurance();
			}else if($("#choose_type_value").val()=="choose_type_22"){
				if($("#seat_type").val()=='4' || $("#seat_type").val()=='5' || $("#seat_type").val()=='6'){
					if($(".adult:visible,.child:visible").length<$("#choose_seat_num").val()){
					$("#choose_seat_num").focus();
					showErrMsg("choose_seat_num", "160px", "下铺张数不能大于购票数！");
					return false;
					}else{
					hideErrMsg("choose_seat_num");
					}
					if($("#choose_type:visible").length>0 && $("#choose_agree").attr("checked")){
						$("#choose_ext").val("11");
					}else{
						$("#choose_ext").val("00");
					}
				}
				//alert($("#choose_ext").val());
				if($.trim($("#link_name_ps").val())==""){
					showErrMsg("link_name_ps", "110px", "请填写联系人！");
					return false;
				}else if(!checkName($.trim($("#link_name_ps").val()))){
					showErrMsg("link_name_ps", "150px", "请填写正确的联系人！");
					return false;
				}else{
					hideErrMsg("link_name_ps");
				}
				if($.trim($("#link_phone_ps").val())==""){
					showErrMsg("link_phone_ps", "90px", "请填写手机！");
					return false;
				}else if(!/^1(3[\d]|4[57]|5[\d]|8[\d]|7[0678])\d{8}$/g.test($.trim($("#link_phone_ps").val()))){
					showErrMsg("link_phone_ps", "150px", "请填写正确的手机号！");
					return false;
				}else{
					hideErrMsg("link_phone_ps");
				}
				if($("#province").val()=="0"){
					showErrMsg("province", "90px", "请选择省份！");
					return false;
				}else{
					hideErrMsg("province");
				}
				if($("#city").val()=="0"){
					showErrMsg("city", "90px", "请选择城市！");
					return false;
				}else{
					hideErrMsg("city");
				}
				if($("#district").val()=="0"){
					showErrMsg("district", "90px", "请选择区县！");
					return false;
				}else{
					hideErrMsg("district");
				}
				if($.trim($("#ps_address").val())=="" || $.trim($("#ps_address").val())=="请填写可以收件的真实地址（包括省市区）"){
					showErrMsg("ps_address", "110px", "请填写地址！");
					return false;
				}else{
					hideErrMsg("ps_address");
				}
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
			
			// 学生票
			if(!checkStu()){
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
				//增加100px整体高度
				changeHeight(100);
			}else{
				//减少100px整体高度
				changeHeight(-100);
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
			top: '25%',
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
			top: '70%',
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
						//$("#"+personId).children("label").attr("class","current");
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
							changeHeight(35);
							if(count==4){
								$("#add_person").attr("class","unableAdd");
								for(var i=1;i<=count;i++){
									$("#addChildren_"+i).hide();
								}
							}
							//兼容IE
							$("#add_person").attr("style","");
							$(".savePaxDiv").attr("style","padding-top:4px;*+padding-top:3px;_padding-top:0px;");
							
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
			if($("#cookie_"+id).attr("class")=='current_hidden'){
				$("#cookie_"+id).attr("class","pass_hidden");
			}else{
				$("#cookie_"+id).removeAttr("class");
			}
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
			jsonstr = jsonstr.substring(0,jsonstr.lastIndexOf(',')); 
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
        
    //统计获取验证码数
	function tjInsurance(){
		$.ajax({
			url:"/tjInsurance/clickNumAdd.jhtml?type=22&version="+new Date(),
			type: "POST",
			cache: false,
			dataType: "text",
			async: true,
			success: function(data){
			if(data != "success"){
			}
		 }
		});
	}
//购买保险
function submitInsurance(){
	tjInsurance();
	var names= "";var idCards= "";
	$(".adult:visible :input:text").each(function(){
			if($(this).attr("title")=="姓名" && $(this).parents("tr").find(".ids_type_select").val()=="2"){
					names+=$(this).val()+"@@";
				}else if($(this).attr("title")=="证件号码" 
					&& $(this).parents("tr").find(".ids_type_select").val()=="2"){
					idCards+=$(this).val()+"&&";
				}
			});
		//alert(names);alert(idCards);
		var name = $("#link_name").val();
		var phone = $("#link_phone").val();
		var idCard = "";
		var arr = new Array();arr = names.split("@@");
		var arr2 = new Array();arr2 = idCards.split("&&");
		if(names.indexOf(name) > -1) {
			if(arr.length > 1){	
				for(var i=0;i<arr.length;i++){
					if(arr[i]==name){
					idCard = arr2[i];
					}
				}
			}
		}else{
		name = arr[0];
		idCard = arr2[0];
		}
		//alert(name+"###"+idCard);
		$.ajax({
			url:"/tjInsurance/submitInsurance.jhtml",
			type: "POST",
			cache: false,
			dataType: "text",
			async: true,
			data:{name:name,phone:phone,idCard:idCard},
			success: function(data1){
			if(data1 != "success"){
			}
		 }
		});
	}
	
	
	  //显示取票说明
	function showPsCity(){
		//消息框
		var dialog = art.dialog({
			lock: true,
			fixed: true,
			left: '50%',
			top: '70%',
		    title: '配送说明',
		    okVal: '确认',
		    content: '<p style="width:250px;height:40px;line-height:20px;margin:5px;">1、目前该功能仅支持<font color="red">北京地区</font>配送。</p>'
		    		+'<p style="width:250px;height:40px;line-height:20px;margin:5px;">2、配送费<font color="red">￥20</font> + 纸质票费<font color="red">￥5</font>，今天16点前完成支付，明天17点前送到！</p>',
		    ok: function(){}
		});
	}
	  
	
	  
	 // 学生票逻辑 
	 function showStu(){
		hideErrMsg("showStuGrid");
		var stu_arr = new Array();	
		$(".ticket_type_select").each(function(){
			if(this.value=="2"){
				stu_arr.push(this);
			}
		});
		var stu_len = $("#stu_info_div_1 .insurance_wrap").length;
		if(stu_len==stu_arr.length){
			return ;
		}
		if(stu_arr.length>stu_len){
			for(var i= stu_len;i<stu_arr.length;i++){
				
				var obj = stu_arr[i];
				var index = obj.id.replace("ticket_type_","");
				if($("#user_name_"+index).val()==""){
					showErrMsg("user_name_"+index, "110px", "请填写姓名！");
					return false;
				}
				var html = $("#stu_div_0")[0].outerHTML.replace(/_0/g,"_"+index).replace("@@",$("#user_name_"+index).val());
				$("#stu_info_div_1").append(html);
				$("#stu_info_div_1 .insurance_wrap").show();
			}
			return ;
		}
		$("#stu_info_div_1 .insurance_wrap").remove();
		
	}
	var pro_array =["北京", "天津", "河北", "山西", "内蒙古", "辽宁", "吉林", "黑龙江", "上海", "江苏", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北", "湖南", "广东", "广西", "海南", "重庆", "四川", "贵州", "云南", "西藏", "陕西", "甘肃", "青海", "宁夏", "新疆"]
	var arr_entry_year =["2008","2009","2010","2011","2012","2013","2014","2015","2016","2017"];
	var arr_school_sys = ["1年","2年","3年","4年","5年","6年","7年","8年","9年"];
	var school_arr = [];
	var city_arr = [];
	$(function(){
		$.ajax({
			type : "post",
			url :  "/js/rob/school.json",
			success : function(data){
				var arr = data.data;
				for (var i = 0; i < arr.length; i++) {
					var obj = arr[i]
					var name = obj.chineseName;
					school_arr.push(name);
				}
			}
		});
		$.ajax({
			type : "post",
			url :  "/js/rob/city.json",
			success : function(data){
				var arr = data.data;
				for (var i = 0; i < arr.length; i++) {
					var obj = arr[i]
					var name = obj.chineseName;
					city_arr.push(name);
				}
			}
		});
		$("#stu_info_div_1").on("focus",".auto",function(){
			
			var op ={
				minChars: 0,
				matchContains: true,
				width:120
			}
			var id = this.id;
			hideErrMsg(id);
			if(id.indexOf("school_system")!=-1){
				$(this).autocomplete(arr_school_sys);
			}
			if(id.indexOf("enter_year")!=-1){
				$(this).autocomplete(arr_entry_year);
			}
			if(id.indexOf("province_name")!=-1){
				op.max = 40;
				$(this).autocomplete(pro_array,op);
			}
			if(id.indexOf("school_name")!=-1){
				op.width = 130;
				$(this).autocomplete(school_arr,op);
			}
			if(id.indexOf("preference_from_station_name")!=-1){
				op.width = 130;
				$(this).autocomplete(city_arr,op);
			}
			if(id.indexOf("preference_to_station_name")!=-1){
				op.width = 100;
				$(this).autocomplete(city_arr,op);
			}
			
			
		});
	})
	var stuinfos = [];
	function checkStu(){
		var stu_arr = new Array();	
		$(".ticket_type_select").each(function(){
			if(this.value=="2"){
				stu_arr.push(this);
			}
		});
		// 没有学生票
		if(stu_arr.length == 0){
			return true;
		}
		var stu_len = $("#stu_info_div_1 .insurance_wrap").length;
		if(stu_arr.length!=stu_len){
			showErrMsg("showStuGrid", 110, "点我完善学生票信息!");
			return false;
		}
		
		for (var i = 0; i < stu_arr.length; i++) {
			var obj = stu_arr[i];
			var index = obj.id.replace("ticket_type_","");
			if($("#user_name_"+index).val()!=$("#stu_name_"+index).val()){
				showErrMsg("stu_name_"+index, "110px", "请与乘客信息名字一致!");
				return false;
			}
		}
		
		var input_arr = $("#stu_info_div_1 input[type=text]");
		for (var i = 0; i < input_arr.length; i++) {
			var value = $(input_arr[i]).val();
			if($.trim(value)==""){
				showErrMsg($(input_arr[i])[0].id, 110, "请输入相关信息!");
				return false;
			}
		}
		
		for (var i = 0; i < stu_arr.length; i++) {
			var obj = stu_arr[i];
			var index = obj.id.replace("ticket_type_","");
			var DBStudentInfo = {
					"stu_name":$.trim($("#stu_name_"+index).val()),
					"province_name":$.trim($("#province_name_"+index).val()),
					"school_name":$.trim($("#school_name_"+index).val()),
					"student_no":$.trim($("#student_no_"+index).val()),
					"enter_year":$.trim($("#enter_year_"+index).val()),
					"preference_from_station_name":$.trim($("#preference_from_station_name_"+index).val()),
					"preference_to_station_name":$.trim($("#preference_to_station_name_"+index).val()),
					"school_system":$.trim($("#school_system_"+index).val().replace("年",""))
			};
			stuinfos.push(DBStudentInfo);
		}
		$("#stu_infos").val(JSON.stringify(stuinfos));
		return true;
	}
	// 学生票逻辑 

		


     
	
</script>
</body>
</html>
