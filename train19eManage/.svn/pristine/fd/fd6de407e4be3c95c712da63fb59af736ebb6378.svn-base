<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>联程订单退票明细页面</title>
		<link rel="stylesheet" href="/css/style.css" type="text/css" />
		<style type="text/css">
			.pub_yuliu {width:130px;}
		</style>
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript">
			function refund(orderid, refundseq, refund_status) {
				var pageIndex= $("#pageIndex").val();
				var statusList = $("#statusList").val();
				var refund_12306_seq = $("#refund_12306_seq").val();
				var str = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
				if($.trim($("#refund_money").val())==""){
					$("#refund_money").focus();
					alert("退款金额不能为空！");
					return;
				}
				
				if($.trim($("#detail_refund").val())==""){
					$("#detail_refund").focus();
					alert("12306退款不能为空！");
					return;
				}
				if(!(str.test($.trim($("#refund_money").val())))){
					$("#refund_money").focus();
					alert("请输入符合规范的金额！");
					return;
				}
				if($.trim($("#detail_refund").val()).indexOf("，")>=0){
					$("#detail_refund").focus();
					alert("请采用英文逗号！");
					return;
				}
				
				if($.trim($("#detail_alter_tickets").val()).indexOf("，")>=0){
					$("#detail_alter_tickets").focus();
					alert("请采用英文逗号！");
					return;
				}
				
				c = parseFloat($.trim($("#refund_money").val())).toFixed(2)*100/100;
				if(c<0){
				alert("退款金额不能小于0");
				return;
				}
   	
				var our_remark = $("#our_remark").val();
				$("form:first").attr("action","/qunarrefund/refund.do?orderid=" + orderid + "&refundseq=" + refundseq + "&refund_status=" + refund_status+"&statusList="+statusList+"&pageIndex="+pageIndex);
				$("form:first").submit();
			}
			function refuseRefund(orderid, refundseq) {
				var pageIndex= $("#pageIndex").val();
				var statusList = $("#statusList").val();
				var our_remark = $("#our_remark").val();
				var refuse_reason = $("#refuse_reason").val();
				if(refuse_reason == "0") {
					alert("拒绝退款原因必须选择一个！");
					return;
				}
				alert("拒接退款");
				$("form:first").attr("action","/qunarrefund/refuse.do?orderid=" + orderid + "&refundseq=" + refundseq+ "&statusList="+statusList+"&pageIndex="+pageIndex);
				$("form:first").submit();
			}
		</script>
	</head>
	<body>
		<form id="myform" name="myform" method="post">
			<div class="content1 oz">
				<!--左边内容 start-->
				<div class="left_con oz">
					<div class="pub_order_mes oz mb10_all">
						<h4>
							订单信息
						</h4>
						<div class="pub_con">
							<table class="pub_table">
								<tr>
									<td class="pub_yuliu" rowspan="6"></td>
									<td width="234">
										订 单 号：
										<span>${bookVo.order_id}</span>
									</td>
									<td>
										订单状态：
										<span>${bookStatus[bookVo.order_status]}</span>
										<input type="hidden" id="statusList" value="${statusList }"/>
										<input type="hidden" id="pageIndex" value="${pageIndex }" />
									</td>
								</tr>
								<tr>
									<td>
										支付金额：
										<span style="font-weight: bold; color: red; font-size: 20px;">${bookVo.pay_money}</span>
										元
									</td>
									<td>
										支付时间：
										<span>${bookVo.order_time}</span>
									</td>
								</tr>
								<tr>
									<td>
										出票方式：
										<span><c:if test="${bookVo.out_ticket_type eq '11'}">电子票</c:if>
											<c:if test="${bookVo.out_ticket_type eq '22'}">配送票</c:if> </span>
									</td>
									<td>
										渠道：
										<span>${bookVo.channel}</span>
									</td>

								</tr>
								<c:if test="${fn:contains('55,66,77', list.refund_status)}">
									<tr>
										<td>
											出票金额：
											<span>${bookVo.buy_money}</span>
										</td>
										<td>
											出票时间：
											<span>${bookVo.out_ticket_time}</span>
										</td>
									</tr>
								</c:if>
								<tr>
									<td>
										12306单号：
										<span>${out_ticket_billno }</span>
									</td>
									<td>
										订单来源：
										<span>${qunarChannel[bookVo.order_source]}</span>
									</td>
								</tr>
							</table>
						</div>

					</div>
					<c:forEach var="lianchengOrderInfo" items="${lianchengOrderInfoList}" varStatus="idx">
						<h1>
							第${idx.index+1 }程
						</h1>
					订单号：
						<strong>${lianchengOrderInfo.trip_id}</strong>
						<c:forEach var="lianchengCpInfo" items="${lianchengDetailCpInfoList}">
							<c:if
								test="${lianchengOrderInfo.trip_id eq lianchengCpInfo[0].order_id}">
								<c:set var="cpInfo" value="${lianchengCpInfo}" />
							</c:if>
						</c:forEach>
						<div class="pub_order_mes oz mb10_all">
							<h4>
								车票信息
							</h4>
							<div class="pub_con">
								<table class="pub_table">

									<tr>
										<td class="pub_yuliu" rowspan="4">
											<strong>${lianchengOrderInfo.train_no}</strong>
											<br />
											车次
										</td>
										<td width="234" colspan="2">
											出 发/到 达：
											<span>${lianchengOrderInfo.from_city}（${fn:substring(lianchengOrderInfo.from_time,
												11,16)}）</span>—
											<span>${lianchengOrderInfo.to_city}(${fn:substring(lianchengOrderInfo.to_time,
												11,16)}）</span>
										</td>
									</tr>
									<tr>
										<td width="234">
											日 期：
											<span>${fn:substring(lianchengOrderInfo.from_time, 0,10)}</span>
										</td>
										<td>
											坐 席：${seatType[fn:substring(lianchengOrderInfo.seat_type,0,1)] }
										</td>
									</tr>
									<tr>
										<td width="234">
											12306订单号：
											<span>${lianchengOrderInfo.out_ticket_billno}</span>
										</td>
									</tr>
									<tr>
										<td width="234">
											票价 ：
											<span style="font-weight: bold; color: red;">${lianchengOrderInfo.buy_money}</span>
											元
										</td>
										<td>
											数 量：
											<span>${fn:length(cpInfo)}</span> 张
										</td>

									</tr>
								</table>
							</div>

						</div>
						<div class="pub_passager_mes oz mb10_all">
							<h4>
								乘客信息
							</h4>
							<div class="pub_con">
								<c:forEach var="cp" items="${cpInfo}" varStatus="idx">
									<c:if test="${idx.index != 0}">
										<%
											out.println("<div class=\"oz pas1\"><p class=\"border_top\"></p>");
										%>
									</c:if>
									<table class="pub_table">
										<tr>
											<td class="pub_yuliu" rowspan="5">

											</td>
											<td width="234">
												姓 名：
												<span>${cp.user_name}</span>
											</td>
											<td>
												类 型：
												<span>${ticketType[cp.ticket_type] }</span>
											</td>
										</tr>
										<tr>
											<td width="234">
												证件类型：
												<span>${idstype[cp.ids_type] }</span>
											</td>
											<td>
												证件号码：
												<span>${cp.user_ids}</span>
											</td>
										</tr>
										<tr>
											<td>
												车票号：
												<span>${cp.cp_id}</span>
											</td>
											<td>
												坐席：
												<span>${seatType[cp.seat_type]}</span>
											</td>
										</tr>

										<tr>
											<td>
												座位：
												<span>${cp.train_box }</span>&nbsp;车&nbsp;
												<span>${cp.seat_no }</span>&nbsp;
											</td>
											<td>
												票面价：
												<span>${cp.buy_money}元</span>
											</td>
										</tr>
									</table>
									<c:if test="${idx.index != 0}">
										<%
											out.println("</div>");
										%>
									</c:if>
								</c:forEach>
							</div>
							
							<div class="pub_passager_mes oz mb10_all">
								<h4>
									付款信息
								</h4>
								<div class="pub_con">
									<table class="pub_table">
										<tr>
											<td class="pub_yuliu" rowspan="4"></td>
											<td>
												12306账号：
												<c:forEach var="account" items="${accountMap}">
													<c:if
														test="${fn:contains(account.key, lianchengOrderInfo.trip_id)}">
													${account.value}
												</c:if>
												</c:forEach>
											</td>
										</tr>
										<tr>
											<td>
												付款银行账号：${lianchengOrderInfo.out_ticket_account}

											</td>
										</tr>
										<tr>
											<td>
												付款流水号：${lianchengOrderInfo.bank_pay_seq}
											</td>
										</tr>
										<tr>
											<td>
												12306单号：${lianchengOrderInfo.out_ticket_billno}
											</td>
										</tr>
										<tr>
											<td class="pub_yuliu" rowspan="4"></td>
											<td>
												付款金额：${lianchengOrderInfo.pay_money}
											</td>
										</tr>
									</table>
								</div>
							</div>
						</div>
						<div class="pub_self_take_mes oz mb10_all">
							<h4>
								改签信息
							</h4>
							<div class="pub_con">
								<c:forEach var="cp" items="${cpInfo}" varStatus="idx">
								<c:if test="${idx.index != 0}">
									<%
										out.println("<div class=\"oz pas1\"><p class=\"border_top\"></p>");
									%>
								</c:if>
									<table class="pub_table">
										<tr>
											<td class="pub_yuliu" rowspan="2">
												<span>${cp.user_name}</span>
											</td>
											<td width="200">
												改签车次&nbsp;：
												<span>
													<input type="text" value="${cp.alter_train_no}" id="alter_train_no_${cp.cp_id}" name="alter_train_no_${cp.cp_id}" style="width:100px;" />
													<input type="hidden" value="${cp.cp_id}" id="cp_id_${cp.cp_id}" name="cp_id_${cp.cp_id}" />
												</span>
											</td>
											<td>
												改签坐席：
												<span>
													<input type="text" value="${cp.alter_train_box}" id="alter_train_box_${cp.cp_id}" name="alter_train_box_${cp.cp_id}" style="width:30px;" />
													车厢-
													<input type="text" value="${cp.alter_seat_no}" id="alter_seat_no_${cp.cp_id}" name="alter_seat_no_${cp.cp_id}" style="width:30px;" />
													座位-
													<input type="text" value="${seatType[cp.alter_seat_type]}" id="alter_seat_type_${cp.cp_id}" name="alter_seat_type_${cp.cp_id}" style="width:50px;" />
												</span>
											</td>
										</tr>
										<tr>
											<td style="color: red; font-weight: bold;">
												改签差额：
												<span>
													<input type="text" value="${cp.alter_money}" id="alter_money_${cp.cp_id}" name="alter_money_${cp.cp_id}" style="width:100px;" />
												</span>
											</td>
											<td style="color: red; font-weight: bold;">
												12306退款：
												<span>
													<input type="text" value="${cp.refund_12306_money}" id="refund_12306_money_${cp.cp_id}" name="refund_12306_money_${cp.cp_id}" style="width:100px;" />
												</span>
											</td>
										</tr>
									</table>
								<c:if test="${idx.index != 0}">
									<%
										out.println("</div>");
									%>
								</c:if>
							</c:forEach>
							</div>
						</div>
					</c:forEach>
					
					<div class="pub_self_take_mes oz mb10_all">
						<h4>
							退款信息
						</h4>
						<div class="pub_con">

							<table class="pub_table">
								<tr>
									<td class="pub_yuliu"></td>
									<td>
										12306账号：<span style="color: red; font-weight: bold;">${account}</span>
									</td>
									<td>
										12306单号：<span>${out_ticket_billno }</span>
									</td>
								</tr>
								<tr>
									<td class="pub_yuliu"></td>
									<td>
										退款状态：
										<span style="font-weight: bold; color: red;">${refund_status[refundInfo.refund_status]}</span>
									</td>
									<td>
										创建时间：
										<span>${refundInfo.create_time}</span>
									</td>
								</tr>
								<tr>
									<td class="pub_yuliu"></td>
									<td style="font-weight: bold; color: red;" colspan="2">
										去哪退款：
										<span style="font-weight: bold; color: red;"><input type="text" style="width:60px;" id="refund_money" name="refund_money" value="${refundInfo.refund_money}" /> </span>
										改签差额：
										<input type="text" style="width:60px;" id="detail_alter_tickets" name="detail_alter_tickets" value="${refundInfo.detail_alter_tickets}" />
										12306退款：
										<input type="text" style="width:60px;" id="detail_refund" name="detail_refund" value="${refundInfo.detail_refund }" />
									</td>

									<td></td>
								</tr>
								<tr>
									<td class="pub_yuliu"></td>
									<td>
										拒绝退款原因：
										<span style="font-weight: bold; color: red;"> <select
												name="refuse_reason" id="refuse_reason">
												<option value="0">
													请选择
												</option>
												<c:forEach var="reason" items="${refuse_Reason }">
													<option value="${reason.key}"
														<c:if test="${reason.key eq refundInfo.refuse_reason }">
														selected="selected"
														</c:if>>
														${reason.value }
													</option>
												</c:forEach>
											</select> </span>
									</td>
									<td>
										12306退款流水号：
										<input type="text" value="${refundInfo.refund_12306_seq}"
											name="refund_12306_seq" id="refund_12306_seq" />
									</td>
									<td></td>
								</tr>
								<tr>
									<td class="pub_yuliu"></td>
									<td>
										用户备注：
										<span style="font-weight: bold; color: red;"><input
												type="text" value="${refundInfo.user_remark}"
												name="user_remark" /> </span>
									</td>
									<td>
										出票方备注：
										<span style="font-weight: bold; color: red;"><input
												type="text" value="${refundInfo.our_remark}" id="our_remark"
												name="our_remark" /> </span>
									</td>
									<td></td>
								</tr>
							</table>


						</div>
					</div>

					<div class="pub_debook_mes  oz mb10_all">
						<p>
								<input type="button" value="返 回" class="btn"
									onclick="javascript:history.back(-1);" />
							<c:if test="${isActive eq '1'}">
								<c:if test="${refundInfo.refund_status eq '07'}">
									<input type="button" value="拒绝退款" class="btn"
										onclick="refuseRefund('${refundInfo.order_id}', '${refundInfo.refund_seq}')" />
									<input type="button" value="立即退款" class="btn"
										onclick="refund('${refundInfo.order_id}','${refundInfo.refund_seq}','07')" />
								</c:if>
								<c:if test="${refundInfo.refund_status eq '03'}">
									<input type="button" value="拒绝退款" class="btn"
										onclick="refuseRefund('${refundInfo.order_id}', '${refundInfo.refund_seq}')" />
									<input type="button" value="立即退款" class="btn"
										onclick="refund('${refundInfo.order_id}','${refundInfo.refund_seq}','03')" />
								</c:if>
								<c:if test="${refundInfo.refund_status eq '33'}">
									<input type="button" value="退款完成" class="btn"
										onclick="refund('${refundInfo.order_id}', '${refundInfo.refund_seq}','33')" />
								</c:if>
								<c:if test="${refundInfo.refund_status eq '44'}">
									<input type="button" value="拒绝退款" class="btn"
										onclick="refuseRefund('${refundInfo.order_id}', '${refundInfo.refund_seq}')" />
									<input type="button" value="立即退款" class="btn"
										onclick="refund('${refundInfo.order_id}','${refundInfo.refund_seq}','44')" />
								</c:if>
								
								<c:if test="${refundInfo.refund_status eq '00' ||refundInfo.refund_status eq '01' ||refundInfo.refund_status eq '02'}">
									<input type="button" value="拒绝退款" class="btn"
										onclick="refuseRefund('${refundInfo.order_id}', '${refundInfo.refund_seq}')" />
									<input type="button" value="立即退款" class="btn"
										onclick="refund('${refundInfo.order_id}','${refundInfo.refund_seq}','07')" />
								</c:if>
							</c:if>
						</p>
					</div>


					<div class="pub_passager_mes oz mb10_all">
						<h4>
							历史操作
						</h4>
						<div class="pub_con">
							<table class="pub_table" style="width: 500px; margin: 20px auto;">
								<tr>
									<th>
										序号
									</th>
									<th>
										操作日志
									</th>
									<th>
										操作时间
									</th>
									<th>
										操作人
									</th>
								</tr>
								<c:forEach var="hs" items="${history}" varStatus="idx">
									<tr align="center">
										<td>
											${idx.index+1 }
										</td>
										<td align="left" style="word-break:break-all;width: 300px; ">
											${hs.content}
										</td>
										<td>
											${hs.create_time }
										</td>
										<td>
											${hs.opt_person }
										</td>
									</tr>
								</c:forEach>
							</table>
						</div>
					</div>
				</div>
				<!--左边内容 end-->
			</div>
		</form>
	</body>
</html>
