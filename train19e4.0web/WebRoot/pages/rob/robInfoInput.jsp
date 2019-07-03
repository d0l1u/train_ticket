<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>抢单信息页</title>
		<link rel="stylesheet" href="/css/style.css" type="text/css" />
		<link rel="stylesheet" href="/css/default.css" type="text/css" />
		<link rel="stylesheet" href="/css/attach_style.css" type="text/css" />
		<link rel="stylesheet" href="/css/jquery.autocomplete.css"
			type="text/css" />
		<style>
.type_22 td {
	padding-left: 80px;
	line-height: 0px;
}

.type_class {
	font-size: 14px;
	cursor: pointer;
}

.checked_type {
	BACKGROUND: url(../images/radio-checked.png) no-repeat;
	display: inline-block;
	height: 17px;
	width: 17px;
	background-position: center;
}

.unchecked_type {
	BACKGROUND: url(../images/radio-unchecked.png) no-repeat;
	display: inline-block;
	height: 17px;
	width: 17px;
	background-position: center;
}
</style>
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script type="text/javascript" src="/js/artDialog.js"></script>
		<script type="text/javascript" src="/js/idCard.js"></script>
		<script type="text/javascript" src="/js/json2.js"></script>
		<script type="text/javascript" src="/js/jquery.autocomplete.js"></script>
		<script type="text/javascript" src="/js/city.js"></script>
		<script type="text/javascript">
		var mapper = eval('('+'${seatPrizeMapper}'+')');
		</script>
		<script type="text/javascript" src="/js/qiangpiao.js?time=20161211"></script>
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
					<div class="tip_term oz" style="width: 785px; margin: 0 0 10px 0;">
						<!--<p class="price_tip">温馨提醒：火车票订票时间为早7:00-晚23:00，23:00之后的订单将在次日早7:00之后处理。</p>  -->
						<p class="price_tip">
							温馨提醒：提前70天预约购票功能仅为VIP、SVIP订单专享，在平台预约购票后待铁路系统发售时订购，我们会将购票结果短信通知乘客。
						</p>
					</div>
					<form id="trainForm" action="/rob/createRobOrder.jhtml"
						method="post">
						<input type="hidden" id="train_no" name="train_no"
							value="${trainCode}" />
						<input type="hidden" id=from_city name="from_city"
							value="${startCity}" />
						<input type="hidden" id="to_city" name="to_city"
							value="${endCity}" />
						<input type="hidden" id="from_time" name="from_time"
							value="${travelTime} ${startTime}" />
						<input type="hidden" id="to_time" name="to_time"
							value="${travelTime} ${endTime}" />
						<input type="hidden" name="travelTime" id="travelTime"
							value="${travelTime}" />
						<input type="hidden" name="max12306Price" id="max12306Price"
							value="" />
						<input type="hidden" name="out_ticket_type" id="out_ticket_type"
							value="11" />
						<input type="hidden" name="ps_pay_money" value="20" />
						<input type="hidden" name="wz_ext" id="wz_ext" />
						<input type="hidden" name="choose_ext" id="choose_ext" />
						<input type="hidden" name="choose_type_value"
							id="choose_type_value" value="choose_type_11" />

						<div class="pub_order_mes ticket_mes oz mb10_all"
							style="margin: 0;">
							<div class="pub_ord_tit">
								<h4 class="fl">
									车次信息
								</h4>
								<p class="tit_tip">
									（数据仅作参考，请以实际出票情况为准）
								</p>
							</div>
							<div class="new_pub_con">
								<table class="pub_table">
									<tr>
										<td class="pub_yuliu" rowspan="5">
											<strong>${trainCode}</strong>
											<br />
											<span class="checi">${trainTypeCn}</span>
										</td>
										<td class="addr" colspan="2">
											<strong>${startCity}<span>（${startTime}）</span> </trong>—<strong>${endCity}<span>（${endTime}）</span>
											</strong>
										</td>
									</tr>
									<tr>
										<td width="280">
											日 期：
											<span>${travelTime}</span>
										</td>
										<td rowspan="3" width="100" style="padding-left: 120px;">
											<input type="button" class="btn" value="重选"
												onclick="javascript:history.back(-1);" />
										</td>
									</tr>
									<tr>
										<td style="width: 66px;">
											已选坐席：
											<c:forEach items="${seatInfoList}" var="seat">
												<c:if test="${seat.seatSelect eq 'select'}">
													${seat.seatName} ¥ ${seat.price}
											<input type="hidden" id="defaultSeat" name="defaultSeat"
														value="${seat.seatType}_${seat.price}_${seat.seatName}" />
												</c:if>
											</c:forEach>

										</td>
									</tr>
									<tr>
										<td style="width: 100px;">
											<input type="hidden" id="selectSeats" name="selectSeats"
												value="" />
											选择备选座席：
											<c:forEach items="${seatInfoList}" var="seat">
												<c:if test="${seat.seatSelect eq 'unSelect'}">
													<input type="checkbox" name="bkseattype"
														value="${seat.seatType}_${seat.price}_${seat.seatName}">
											${seat.seatName} ¥ ${seat.price}
												</c:if>
											</c:forEach>
										</td>

									</tr>
								</table>
							</div>

						</div>
						<div class="book_passager_mes oz mb10_all">
							<div class="pub_ord_tit">
								<h4 class="fl" id="passenger_info">
									乘客信息
								</h4>
								<p class="tit_tip">
									（一个订单最多可代购5张票，请务必填写乘车人真实信息，
									<span onclick="javascript:showGpDesc();">购票说明</span>）
								</p>
							</div>
							<div class="new_pub_con delivery_pos oz">
								<div class="usual-passenger" id="pax_passenger"
									style="border: 0; background-color: #EAF2FC;">
									<ul id="useManager" class="oz">
									</ul>
									<div class="down-arrow" id="ser-more">
										<a href="javascript:void(0);">更多</a>
									</div>
								</div>
								<div class="delivery_pos_child oz">
									<div style="padding-bottom: 10px;">
										<table class="table_list" cellpadding="0" cellspacing="0"
											id="train_ticket_list"
											style="position: relative; z-index: 999; width: 97%; margin: 10px;">
											<tr>
												<td width="25">
													&nbsp;
												</td>
												<td width="15"></td>
												<td width="100">
													姓 名
												</td>
												<td width="90">
													类 型
												</td>
												<td width="120">
													证件类型
												</td>
												<td width="135">
													证件号码
												</td>
												<td width="110">
													&nbsp;
												</td>
												<td width="90">
													&nbsp;
												</td>
											</tr>
											<tr class="adult" style="display: none;">
												<td>
													<span class="delPerson fl" id="delPerson_index_source"><a
														href="javascript:void(0);"><s></s> </a> </span>
												</td>
												<td class="indexTr" id="index_index_source">
													index_source
												</td>
												<td>
													<input style="width: 90px; border: 1px solid #dadada;"
														type="text" name="bookDetailInfoList_source.user_name"
														id="user_name_index_source"
														class="text text_name user_name_text" title="姓名"
														onblur="hideMsg(this);"
														onkeyup="value=value.replace(/(^\s*)|(\s*$)/g,'');" />
												</td>
												<td>
													<select style="width: 70px;"
														name="bookDetailInfoList_source.ticket_type"
														id="ticket_type_index_source" class="ticket_type_select"
														onchange="toggleTip(index_source);">
														<option value="0">
															成人票
														</option>
														<option value="1">
															儿童票
														</option>
													</select>
												</td>
												<td>
													<select style="width: 100px;"
														name="bookDetailInfoList_source.ids_type"
														id="ids_type_index_source" class="ids_type_select">
														<option value="2">
															二代身份证
														</option>
														<!--<option value="1">一代身份证</option>
														<option value="3">
															港澳通行证
														</option>
														<option value="4">
															台湾通行证
														</option>-->
														<option value="5">
															护照
														</option>
													</select>
												</td>
												<td>
													<input style="width: 130px; border: 1px solid #dadada;"
														type="text" class="text text_id idcard_text"
														name="bookDetailInfoList_source.user_ids"
														id="user_ids_index_source" title="证件号码"
														onblur="hideMsg(this);hideNumMsg(this);toggleTip(index_source);"
														onfocus="getNumbers(this,'idcard');"
														onkeyup="value=value.replace(/(^\s*)|(\s*$)/g,'');getNumbers(this,'idcard');" />
												</td>
												<td>
													<div class="savePaxDiv fl">
														<input id="savePax_index_source"
															class="savePax_index_source" type="checkbox" />
														<label for="savePax_index_source" style="cursor: hand;">
															保存为常用乘客
														</label>
													</div>
												</td>
												<td nowrap="nowrap">
													<span class="fl addChildren" style="display: inline;"
														id="addChildren_index_source"><a
														href="javascript:void(0);">添加随行儿童</a> </span>
												</td>
											</tr>
										</table>
										<div class="add_person_statement oz"
											style="width: 97%; margin: 10px; color: #ea0000;">
											<p
												style="float: left; padding-left: 26px; width: 60px; line-height: 24px;">
												温馨提示：
											</p>
											<p
												style="float: left; text-align: left; color: #ea0000; width: 600px; line-height: 24px;">
												<span>1、若2-5人一同出行，请添加同行乘客后一起提交，可提高连座几率；</span>
												<br />
												<span>2、所填写的乘客姓名和证件号码必须与证件上信息保持一致，若有错别字或号码不准确均会导致出票失败。</span>
											</p>
										</div>
									</div>
									<div id="add_person">
										<div class="ableAdd" style="float: left;">
											<span class="add_adult"></span>
										</div>
									</div>
								</div>
								<!-- 购买儿童票提示 -->
								<div class="show_tip" id="child_tip" style="display: none;">
									<p class="price_tip">
										1.为确保儿童出行安全，儿童票须跟成人票一起购买，乘车儿童有有效身份证件的，填写儿童有效身份证件信息；乘车儿童没有有效身份证件的，应使用同行成年人的有效身份证件信息；购票时不受前条限制，
										但购票后、开车前须办理换票手续方可进站乘车。
										<br />
										2.儿童身高为1.2-1.5米可购买儿童票，超过1.5米须购买成人票。一名成年乘客可以免费携带一名身高不足1.2米的儿童。身高不足1.2米的儿童超过一名时，一名儿童免费，其他儿童需购买儿童票。
										<br />
										3.儿童票暂收成人票价，
										<font style="color: #EA0000;">出票后根据实际票价退还差价。</font>
									</p>
								</div>
							</div>
						</div>
						<!-- 抢票信息开始 -->
						<div class="book_passager_mes oz mb10_all">
							<div class="pub_ord_tit">
								<h4 class="fl" id="passenger_info">
									抢票设置
								</h4>
								<p class="tit_tip">
									抢票提示文字：
								</p>
							</div>
							<div class="new_pub_con delivery_pos oz">
								<div class="usual-passenger" id="pax_passenger"
									style="border: 0; background-color: #EAF2FC;">
									<ul id="useManager" class="oz">
									</ul>
								</div>
								<input type="hidden" id="leakcutStr" name ="leakcutStr" value="">
								<div class="yun_time"
									style="margin-left: 35px; margin-top: 10px;">
									<strong>停止抢票时间为</strong>
									<select style="width: 159px;" id="yun_qiangpiao"
										name="yun_qiangpiao">
										<option valuename="连续抢票5天" value="120">
											
										</option>
										<option valuename="连续抢票3天" value="72">
											
										</option>
										<option valuename="连续抢票2天" value="48">
											
										</option>
										<option valuename="连续抢票1天" value="24">
											
										</option>
										<option valuename="抢至发车前一小时" value="-1" selected="true">
											抢至:发车前一小时
										</option>
									</select>
									<br>

									<div style="margin-top: 12px">
									</div>
									<div class="yun_tit">
										云抢票成功率评估：
										<strong class="yun_tag_red" id="ticketEfficiency">抢票成功率<span>一般</span>
										</strong>
									</div>
								</div>
								<div class="delivery_pos_child oz">
									<div style="padding-bottom: 10px;">
										<div class="add_person_statement oz"
											style="width: 97%; margin: 10px; color: #ea0000;">
											<p
												style="float: left; padding-left: 26px; width: 60px; line-height: 24px;">
												温馨提示：
											</p>
											<p
												style="float: left; text-align: left; color: #ea0000; width: 600px; line-height: 24px;">
												<span>1、仅有1名乘客，抢到票的概率高</span>
												<br />
												<span>2、将为您抢票抢至发车前一小时，时间充足，容易抢到票</span>
												<br />
												<span>3、您已选择同时抢1个坐席和1个车次，抢票成功率提高了1倍</span>
												<br />
												<span>4、余票大数据显示发车前三天平均每天有35张新的余票放出，抢到票的概率很高</span>
												<br />
											</p>
										</div>
									</div>
								</div>
							</div>
						</div>
						<!-- 抢票信息结束 -->

						<div class="book_passager_mes oz mb10_all">
							<div class="book_passager_mes oz">
								<div class="pub_ord_tit">
									<h4 class="fl">
										SVIP服务费:
									</h4>
									<p class="tit_tip">
										（极速抢票，赠送保险！）
									</p>
								</div>
							</div>
							<div class="new_pub_con">
								<div class="insurance_wrap oz">



									<div class="info-select oz">
										<input type="hidden" id="svip_price" value=""
											name="svip_price" />
										<span class="train_product_checked" type="radio"
											id="svip_price_span"> <label
												style="margin-right: 35px;">
												SVIP服务费合计：
											</label> </span>
									</div>
								</div>
							</div>
						</div>

						<div class="pub_delivery_mes oz mb10_all" id="tickets-way">
							<div class="pub_ord_tit">
								<h4 class="fl">
									取票方式
								</h4>
								<p class="tit_tip">
									（取票规则：开车前可在任何火车站或代售点自由取票，
									<span onclick="javascript:showQpDesc();" id="qupiao">取票说明</span>）
								</p>
							</div>
							<div class="new_pub_con">
								<table class="pub_table current"
									style="margin: 10px 0px 10px 40px;">
									<tr>
										<td colspan="3" class="height30">
											<span class="type_class" id="check_class_11"><span
												class="checked_type" id="check_type_11"></span> <label>
													车站自提
												</label> </span>
											<c:if test="${ps_order_status eq '11'}">
                    	  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img
													src="/images/iconred.gif">自取票卧铺铺位是随机分配的，如需购买下铺票，请选择送票上门。
                    	   </c:if>
										</td>
									</tr>
									<tr class="type_11" id="table_type_11">
										<td width="234">
											<span style="color: red;">*</span>联 系 人：
											<span><input type="text" class="link_text width120"
													id="link_name" name="link_name" /> </span>
										</td>
										<td>
											<span style="color: red;">*</span>手 机：
											<span><input type="text" class="link_text width120"
													id="link_phone" name="link_phone"
													onblur="hideNumMsg(this);"
													onkeyup="getNumbers(this,'phone');"
													onfocus="getNumbers(this,'phone');"
													style="inline-height: 22px;" /> </span>
										</td>
									</tr>
								</table>
							</div>
						</div>

						<!--协议-->
						<p style="padding-left: 153px;">
							<span class="display_mid"> <input type="checkbox"
									id="chk_bxconfirm" checked="checked" /> </span> 我已阅读并同意
							<span style="color: #0081cc; cursor: pointer; line-height: 24px;"
								onclick="window.open('<%=basePath%>pages/guide/daiGou.jsp');">《火车票线下代购服务协议》</span>、
							<span style="color: #0081cc; cursor: pointer; line-height: 24px;"
								onclick="window.open('<%=basePath%>pages/guide/chengnuoPage.jsp');">《平台购票承诺书》</span>
							<span id="bx_confirm">和<span
								style="color: #0081cc; cursor: pointer; line-height: 24px;"
								onclick=window.open('http://www.unionlife.com.cn/tab674/');;
>《保险说明》</span>
							</span>
						</p>
						<p class="tijiao">
							<input type="button" value="确认提交" id="btnSubmit"
								class="infoinput_submit" />
						</p>
					</form>
				</div>
				<!--右边内容 end-->
			</div>
		</div>
		<div id="tip" class="tip_wrap" style="display: none;">
			<div class="tip">
				<b></b><span class="errMsg"></span>
			</div>
		</div>
		<div id="num_tip" class="num_tip_wrap" style="display: none;">
			<div class="tip">
				<b></b><span class="numMsg"></span>
			</div>
		</div>
	</body>
</html>
