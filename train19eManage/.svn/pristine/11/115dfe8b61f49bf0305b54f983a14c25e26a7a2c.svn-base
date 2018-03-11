<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>联程订单明细页面</title>
		<link rel="stylesheet" href="/css/style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript">


</script>

	</head>

	<body>
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
									<span>${bookVo.out_ticket_billno }</span>
								</td>
								<td>
									订单来源：
									<span>${qunarChannel[bookVo.order_source] }</span>
								</td>
							</tr>
						</table>
					</div>

				</div>
				<c:forEach var="lianchengOrderInfo"
					items="${lianchengOrderInfoList}" varStatus="idx">
					<h1>
						第${idx.index+1 }程
					</h1>
					订单号：
							<strong>${lianchengOrderInfo.trip_id}</strong>
					<c:forEach var="lianchengCpInfo"
						items="${lianchengDetailCpInfoList}">
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
										<span>${fn:substring(lianchengOrderInfo.from_time,
											0,10)}</span>
									</td>
									<td>
										坐 席：${seatType[fn:substring(lianchengOrderInfo.seat_type,0,1)]
										}
									</td>
								</tr>
								<tr>
									<td width="234">
										12306订单号：
										<span>${lianchengOrderInfo.out_ticket_billno }</span>
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
								<tr>
									<!-- <td width="234">
										备选坐席：
										<c:forEach var="ext" items="${ext_seat}">
									${seatType[fn:substringBefore(ext, ',')]}(${fn:substringAfter(ext, ',')}元)
									</c:forEach>
									</td>
									<td>

									</td> -->
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
										out
															.println("<div class=\"oz pas1\"><p class=\"border_top\"></p>");
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
											12306单号：${bookVo.out_ticket_billno}
										</td>
									</tr>
									<tr>
										<td class="pub_yuliu" rowspan="4"></td>
										<td>
											付款金额：${bookVo.pay_money}
										</td>
									</tr>
								</table>
							</div>
						</div>
					</div>
				</c:forEach>
				<div class="pub_debook_mes  oz mb10_all">
					<p>
						<input type="button" value="返 回" class="btn"
							onclick="javascript:history.back(-1);" />
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
	</body>
</html>
