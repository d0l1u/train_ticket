<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>自提订单明细页</title>
		<link rel="stylesheet" href="/css/style.css" type="text/css" />
		
		<style type="text/css">
			.pub_table td{height:20px;}
		</style>
		
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
		<script type="text/javascript">

</script>
	</head>
	<body>
		<div class="content1 oz">
			<!--左边内容 start-->
			<form action="" method="post" id="updateForm">
			<div class="left_con oz">
				<div class="pub_order_mes oz mb10_all">
					<h4>
						订单信息
					</h4>
					<div class="pub_con">
						<table class="pub_table" >
							<tr>
								<td class="pub_yuliu" rowspan="5"></td>
								<td width="234">
									订 单 号：
									<span>${orderInfo.order_id}</span>
									<input type="hidden" name="budan" value="${budan }"/>
								</td>
								<td>
									订单状态：
									<span>${acquireStatus[orderInfo.order_status] } </span>
								</td>
							</tr>
							<tr>
								<td>
									支付金额：
									<span>${orderInfo.pay_money}</span> 元
								</td>
								<td>
									支付时间：
									<span>${orderInfo.pay_time}</span>
								</td>
							</tr>
							<tr>
								<td>
									出票方式：
									<span><c:if test="${orderInfo.out_ticket_type eq '11'}">电子票</c:if>
										<c:if test="${orderInfo.out_ticket_type eq '22'}">配送票</c:if> </span>
								</td>
								<td>
									来源渠道：
									<span>
										${channel_types[orderInfo.channel] }
									</span>
								</td>
							</tr>
							<tr>
								<td>
									出票金额：
									<span> 
									<c:choose>
										<c:when test="${orderInfo.order_status ne '82'}">
										<c:choose>
											<c:when test="${orderInfo.pay_money<orderInfo.buy_money }">
												<a style="font-weight: bold; color: red;">${orderInfo.buy_money}</a>
											</c:when>
											<c:when test="${empty orderInfo.buy_money }">
												<input type="text" name="buy_money_total" id="buy_money_total" value="${orderInfo.buy_money}" />
											</c:when>
											<c:otherwise>
												<a style="font-weight: bold; ">${orderInfo.buy_money}</a>
											</c:otherwise>
										</c:choose> 
										</c:when>
										<c:otherwise>
											<input type="text" name="buy_money_total" id="buy_money_total" value="${orderInfo.buy_money}" />
										</c:otherwise>
									</c:choose>
									</span>
								</td>
								<td>
									出票时间：
									<span>${orderInfo.out_ticket_time}</span>
								</td>
							</tr>
							<tr>
							<c:if test="${orderInfo.order_status eq '10'}">
								<c:choose>
									<c:when test="${orderInfo.channel eq 'qunar'}">
										<td colspan="2">
												<span>失败原因：
													<c:choose>
														<c:when test="${orderInfo.error_info == 0}">
															<span style="color:red;">
																其他
															</span>
														</c:when>
														<c:when test="${orderInfo.error_info == 1}">
															<span style="color:red;">
																所购买的车次坐席已无票
															</span>
														</c:when>
														<c:when test="${orderInfo.error_info == 2}">
															<span style="color:red;">
																身份证件已经实名制购票，不能再次购买同日期同车次的车票
															</span>
														</c:when>
														<c:when test="${orderInfo.error_info == 3}">
															<span style="color:red;">
																${channel_types[orderInfo.channel]}票价和12306不符
															</span>
														</c:when>
														<c:when test="${orderInfo.error_info == 4}">
															<span style="color:red;">
																车次数据与12306不一致
															</span>
														</c:when>
														<c:when test="${orderInfo.error_info == 5}">
															<span style="color:red;">
																乘客信息错误
															</span>
														</c:when>
														<c:when test="${orderInfo.error_info == 6}">
															<span style="color:red;">
																12306乘客身份信息核验失败
															</span>
														</c:when>
														<c:otherwise></c:otherwise>
													</c:choose>
												</span>	
											</td>
									</c:when>
									<c:otherwise>
										<c:forEach items="${channel_type }" var="s" varStatus="index">
											<c:choose>
												<c:when test="${fn:contains(orderInfo.channel, s.key ) }">
													<td colspan="2">
														<span>失败原因：
															<c:choose>
																<c:when test="${orderInfo.error_info == 1}">
																	<span style="color:red;">
																		所购买的车次坐席已无票
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 2}">
																	<span style="color:red;">
																		身份证件已经实名制购票
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 3}">
																	<span style="color:red;">
																		票价和12306不符
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 4}">
																	<span style="color:red;">
																		乘车时间异常
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 5}">
																	<span style="color:red;">
																		证件错误
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 6}">
																	<span style="color:red;">
																		用户要求取消订单
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 7}">
																	<span style="color:red;">
																		未通过12306实名认证
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 8}">
																	<span style="color:red;">
																		乘客身份信息待核验
																	</span>
																</c:when>
																<c:otherwise></c:otherwise>
															</c:choose>
														</span>	
													</td>
												</c:when>
												<c:otherwise>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</c:otherwise>
								</c:choose>
								
							</c:if>
							</tr>
						</table>
					</div>
				</div>

				<div class="pub_order_mes  oz mb10_all">
					<h4>
						车票信息
					</h4>
					<div class="pub_con">
						<table class="pub_table"  style="width:90%;">
							<tr>
								<td class="pub_yuliu" rowspan="7">
									<strong>${orderInfo.train_no}</strong>
									<br />
									车次
								</td>
								<td  colspan="2"  style="color: red; font-weight: bold; font-size: 15px;">
									出 发/到 达：
									<span>${orderInfo.from_city}（${orderInfo.from_time}）</span>—
									<span>${orderInfo.to_city}（${orderInfo.to_time}）</span>
								</td>
							</tr>
							<tr>
								<td  width="234" style="color: red; font-weight: bold; font-size: 15px;">
									日 期：
									<span>${orderInfo.from_time_all}</span>
								</td>
								<td style="color: red; font-weight: bold; font-size: 15px;">
									价 格：
									<span>${orderInfo.pay_money}</span> 元
								</td>
							</tr>
							<tr>
								<td  style="color: red; font-weight: bold;">
									坐 席：${seattype[orderInfo.seat_type] }
								</td>
								<td style="color: red; font-weight: bold;">
									数 量：
									<span>${fn:length(cpList)}</span> 张
								</td>
							</tr>
							<c:choose>
								<c:when test="${fn:contains('61,82,44,AA', orderInfo.order_status)}">
									<tr>
										<td colspan="2">
											<span style="color:red;font-weight: bold;">
											备选坐席：
											</span>
											<input type="hidden" name="ext_seattypeStr" value="${orderInfo.ext_seattype }"/>
											<c:forEach var="seat" items="${seatList}"  varStatus="index">
												<input type="radio" id="ext_seattype${index.count }" name="ext_seattype" value="${seat.s_type}_${seat.money}"
												<c:if test="${ext_type eq seat.s_type  }">checked="checked"</c:if>/>
												<label for="ext_seattype${index.count }">
													${seat_type_qunar[seat.s_type]}${seat.money}元
												</label>
											</c:forEach>
											<c:if test="${!empty seatList && query_type eq '1'}">
												<input type="button" value="切换" onclick="changeSeatType('${orderInfo.create_time }');" />
											</c:if>
										</td>
									 </tr>
								</c:when>
								<c:otherwise>
									<tr>
										<td colspan="2">
											<span style="color:red;font-weight: bold;">
											备选坐席：
											</span>
											<input type="hidden" name="ext_seattypeStr" value="${orderInfo.ext_seattype }"/>
											<c:forEach var="seat" items="${seatList}"  varStatus="index">
													${seat_type_qunar[seat.s_type]}&nbsp;&nbsp;${seat.money}元
											</c:forEach>
										</td>
									 </tr>
								</c:otherwise>
							</c:choose>
							
							 <tr>
								<!-- 明细中显示12306单号 -->
								<td>
									<c:if test="${fn:contains('11,10,99', orderInfo.order_status)}">
									12306单号：
									<span>${orderInfo.out_ticket_billno }</span>
									</c:if>
								</td>
							</tr>
							<tr>
								<td colspan="2"><span style="color:red;font-weight: bold;">预订信息：</span>${cpIndoSb }</td>
							</tr>
							<!--  
							<tr>
								<td colspan="2"><input type="button" value="锁定12306账号" onclick="copyBookInfo('${orderInfo.acc_username }')"/></td>
							</tr>-->
						</table>
					</div>
				</div>
				<div class="pub_passager_mes oz mb10_all">
					<h4>
						乘客信息
					</h4>
					<div class="pub_con">
						<c:forEach var="cp" items="${cpList}" varStatus="idx">
							<c:if test="${idx.index != 0}">
								<%
                      				out.println("<div class=\"oz pas1\"><p class=\"border_top\"></p>");
                      			%>
							</c:if>
							<table class="pub_table">
								<tr>
									<td class="pub_yuliu" rowspan="4">

									</td>
									<td width="234" style="color: red; font-weight: bold;">
										姓 名：
										<span>${cp.user_name}</span>
									</td>
									<td>
										类 型：
										<span>${tickettype[cp.ticket_type] }</span>
									</td>
								</tr>
								<tr>
									<td width="234"
										style="color: red; font-weight: bold; font-size: 15px;">
										证件类型：
										<span>${idstype[cp.cert_type] }</span>
									</td>
									<td style="color: red; font-weight: bold; font-size: 15px;">
										证件号码：
										<span>${cp.cert_no}</span>
									</td>
								</tr>
								<c:if test="${fn:contains('11,10,99,66,61', orderInfo.order_status)}">
									<tr>
										<td width="234">
											车厢：
											<span>${cp.train_box }号</span>
										</td>
										<td>
											座位号：
											<span>${cp.seat_no }</span>
										</td>
									</tr>
									<tr>
										<td>
											12306票价：
											<span>${cp.buy_money }</span>
										</td>
										<td>
											车票单价:
											<span>${cp.pay_money }</span>&nbsp;&nbsp;&nbsp;
							
							<c:if test="${cp.buyMoney > cp.payMoney}">
								<input type="button" value="更新票价" 
								onclick="updatePrice('${orderInfo.order_id}','${orderInfo.train_no}','${orderInfo.from_city}','${orderInfo.to_city}','${orderInfo.from_time}','${orderInfo.to_time}','${orderInfo.seat_type}','${cp.seat_no}','${cp.buy_money}','${cp.pay_money}');"
								style="text-align:center;border:none;color:#fff;background-color:#2E8B57;cursor:pointer;width:50px;height:20px;"/>
							</c:if>
										</td>
									</tr>
								</c:if>
								<c:if test="${fn:contains('82,44,AA', orderInfo.order_status)}">
									<tr>
										<td width="234">
											<span>
												<input type="hidden" name="cp_id" value="${cp.cp_id}" />
											</span>
											车厢：&nbsp;&nbsp;&nbsp;&nbsp;
											<span><input type="text" size="4" id="train_box_id" class="train_box_class" name="train_box" value="${cp.train_box }"/>号</span>
										</td>
										<td>
											座位号：
											<span><input type="text" id="seat_no_id" class="seat_no_class" name="seat_no" value="${cp.seat_no }"/></span>
										</td>
									</tr>
									<tr>
										<td>
											12306票价：
											<span><input type="text" size="4" id="buy_money_id" class="buy_money_class" name="per_buy_money" value="${cp.buy_money}"/>元</span>
										</td>
										
										<td style="color: red; font-weight: bold; font-size: 15px;">
											车票单价:
											<span>${cp.pay_money }</span>&nbsp;&nbsp;&nbsp;
							<c:if test="${cp.buyMoney > cp.payMoney}">
											<input type="button" value="更新票价" 
								onclick="updatePrice('${orderInfo.order_id}','${orderInfo.train_no}','${orderInfo.from_city}','${orderInfo.to_city}','${orderInfo.from_time}','${orderInfo.to_time}','${orderInfo.seat_type}','${cp.seat_no}','${cp.buy_money}','${cp.pay_money}');"
								style="text-align:center;border:none;color:#fff;background-color:#2E8B57;cursor:pointer;width:50px;height:20px;"/>
							</c:if>
										</td>
										 
									</tr>
								</c:if>
								<!-- <tr>
                        	<td>保 险：<span>20元保险保50万?</span></td>
                        	<td>状  态：<span>订购成功?</span></td>
                        </tr> -->
							</table>
							<c:if test="${idx.index != 0}">
								<%
                      				out.println("</div>");
                     			 %>
							</c:if>
						</c:forEach>
					</div>
						<input type="hidden" name="create_time"
							value="${orderInfo.create_time }" />
						<input type="hidden" name="order_id"
							value="${orderInfo.order_id }" />
						<input type="hidden" name="account_id"
							value="${orderInfo.acc_id }" />
						<input type="hidden" name="acc_username"
							value="${orderInfo.acc_username }" />
						<input type="hidden" name="order_status" id="order_status" />
					<c:if test="${fn:contains('11,44,AA,99,10', orderInfo.order_status)}">
							<div class="pub_passager_mes oz mb10_all">
								<h4>
									出票账号
								</h4>
								<div class="pub_con">

									<table class="pub_table">
										<tr>
											<td class="pub_yuliu" rowspan="3"></td>
											<td width="234">
												登陆账号：
												<span>${orderInfo.acc_username }</span>
											</td>
											<td>
												登陆密码：
												<span>${orderInfo.acc_password }</span>
											</td>
										</tr>
										<tr>
											<c:if test="${(orderInfo.order_status eq '44'||orderInfo.order_status eq 'AA') && query_type eq '1'}">
												<td>
									<select name="stop_reason" id="stop_reason" style="width: 100px;" id="stop_reason">
					        			<option value="2" >取消订单过多</option>
										<option value="1" >账号被封</option>
										<option value="3" >联系人达上限</option>
										<option value="4" >未实名制</option>
										<option value="6" >用户取回</option>
					       			</select><input type="checkbox" name="checkStopAccount" id="checkStopAccount"/>是否<strong style="color:red;">停用</strong>账号
												</td>
												<td>
												<!-- 
													<a
														href="/acquire/updateAccount.do?
														order_id=${orderInfo.order_id  }&account_id=${orderInfo.acc_id }&channel=${orderInfo.channel }
														&acc_username=${orderInfo.acc_username}&acc_password=${orderInfo.acc_password }&create_time=${orderInfo.create_time }">
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;切换账号</a> -->
													<input type="button" value="切换" class="btn" style="width:47px;height:22px;background:url(../images/sprites.png) no-repeat -109px -173px;color:#fff;font:normal 12px/22px 'Simsun';" 
													onclick="updateAccount('${orderInfo.order_id  }','${orderInfo.acc_id }','${orderInfo.channel }','${orderInfo.acc_username }','${orderInfo.acc_password }','${orderInfo.create_time }')"/>
												</td>
											</c:if>
										</tr>
									</table>
								</div>
							</div>
						</c:if>
						<c:if test="${fn:contains('61', orderInfo.order_status)}">
							<div class="pub_passager_mes oz mb10_all">
								<h4>
									人工付款
								</h4>
								<div class="pub_con">

									<table class="pub_table">

										<tr>
											<td class="pub_yuliu" rowspan="3">
											</td>
											<td width="234">
												12306单号：
												<span>${orderInfo.out_ticket_billno }</span>
											</td>
											<td>
												付款金额：
												<span>${orderInfo.buy_money }</span>
											</td>
										</tr>
										<tr>
											<td width="234">
												付款账号：
												<span> 
													<select name="out_ticket_account" id="out_ticket_account">
														<c:forEach items="${bank_type}" var="type">
															<option value="${type.key }">
																${type.value }
															</option>
														</c:forEach>
													</select> 
												</span>
											</td>
											
											<td width="234">
												支付流水号：
												<span>
													<input type="text" name="bank_pay_seq" 
													id="bank_pay_seq_61"
													value="${orderInfo.bank_pay_seq }"/>
												</span>
											</td>
										</tr>
										<tr>
											<td>
												登陆账号：
												<span>${orderInfo.acc_username }</span>
											</td>
											<td>
												登陆密码：
												<span>${orderInfo.acc_password }</span>
											</td>
										</tr>
									</table>
								</div>
							</div>
						</c:if>
						<c:if test="${fn:contains('66', orderInfo.order_status)}">
							<div class="pub_passager_mes oz mb10_all">
								<h4>
									人工付款
								</h4>
								<div class="pub_con">

									<table class="pub_table">

										<tr>
											<td class="pub_yuliu" rowspan="3">
											</td>
											<td width="234">
												12306单号：
												<span>${orderInfo.out_ticket_billno }</span>
											</td>
											
										</tr>
										<tr>
											<td width="234">
												付款账号：
												<span> 
													<select name="out_ticket_account" id="out_ticket_account">
														<c:forEach items="${bank_type}" var="type">
															<option value="${type.key }">
																${type.value }
															</option>
														</c:forEach>
													</select> 
												</span>
											</td>
											<td>
												付款金额：
												<span>${orderInfo.buy_money }</span>
											</td>
										</tr>
										<tr>
											<td>
												登陆账号：
												<span>${orderInfo.acc_username }</span>
											</td>
											<td>
												登陆密码：
												<span>${orderInfo.acc_password }</span>
											</td>
										</tr>
									</table>
								</div>
							</div>
						</c:if>
						
						<c:if test="${fn:contains('82,77,83', orderInfo.order_status)}">
							<div class="pub_passager_mes oz mb10_all">
								<h4>
									人工付款
								</h4>
								<div class="pub_con">

									<table class="pub_table">

										<tr>
											<td class="pub_yuliu" rowspan="3">
											</td>
											<td width="234">
												12306单号：
												<span>
													<input type="text" name="out_ticket_billno" class="out_ticket_billno_calss" value="${orderInfo.out_ticket_billno }"/>
												</span>
											</td>
											<td width="234">
												支付流水号：
												<span>
													<input type="text" name="bank_pay_seq" class="bank_pay_seq_calss" value="${orderInfo.bank_pay_seq }"/>
												</span>
											</td>
										</tr>
										<tr>
											<td width="234">
												付款账号：&nbsp;&nbsp;
												<span> 
													<select name="out_ticket_account" id="out_ticket_account">
														<c:forEach items="${bank_type}" var="type">
															<option value="${type.key }">
																${type.value }
															</option>
														</c:forEach>
													</select> 
												</span>
											</td>
											<td>
												付款金额：
												<span>${orderInfo.buy_money }</span>
											</td>
										</tr>
										<tr>
											<td>
												登陆账号：
												<span>${orderInfo.acc_username }</span>
											</td>
											<td>
												登陆密码：
												<span>${orderInfo.acc_password }</span>
											</td>
										</tr>
										<tr>
										<td class="pub_yuliu"><br /></td>
											<td>
												<span style="float:left;line-height:20px;padding-right:12px;">
												失败原因:
												</span>
												<select name="error_info" id="error_info" style="float:left;">
													<option value="000">请选择</option>
													<c:choose>
														<c:when test="${orderInfo.channel eq 'qunar' }">
															<option value="0">其他</option>
															<option value="1">所购买的车次坐席已无票</option>
															<option value="2">身份证件已经实名制购票，不能再次购买同日期同车次的车票</option>
															<option value="3">${channel_types[orderInfo.channel]}票价和12306不符</option>
															<option value="4">车次数据与12306不一致</option>
															<option value="5">乘客信息错误</option>
															<option value="6">12306乘客身份信息核验失败</option>
														</c:when>
														<c:otherwise>
															<c:forEach items="${channel_type }" var="s" varStatus="index">
																<c:choose>
																	<c:when test="${fn:contains(orderInfo.channel, s.key ) }">
																		<option value="1">所购买的车次坐席已无票</option>
																		<option value="2">身份证件已经实名制购票</option>
																		<option value="3">${channel_types[orderInfo.channel]}票价和12306不符 </option>
																		<option value="4">乘车时间异常</option>
																		<option value="5">证件错误 </option>
																		<option value="6">用户要求取消订单 </option>
																		<option value="7">未通过12306实名认证 </option>
																		<option value="8">乘客身份信息待核验</option>
																	</c:when>
																	<c:otherwise>
																		
																	</c:otherwise>
																</c:choose>
															</c:forEach>
														</c:otherwise>
													</c:choose>
												</select>
											</td>
										</tr>
									</table>
								</div>
							</div>
						</c:if>
						<c:if test="${fn:contains('99', orderInfo.order_status)}">
							<div class="pub_passager_mes oz mb10_all">
								<h4>
									付款明细
								</h4>
								<div class="pub_con">
									<table class="pub_table">
										<tr>
											<td class="pub_yuliu" rowspan="3">
												<br />
											</td>
											<td width="234">
												登陆账号：
												<span>${orderInfo.acc_username }</span>
											</td>
											<td>
												登陆密码：
												<span>${orderInfo.acc_password }</span>
											</td>
										</tr>
										<tr>
											<td width="234">
												付款账号：
												<span>${orderInfo.out_ticket_account }</span>
											</td>
											<td>
												付款金额：
												<span>${orderInfo.buy_money }</span>
											</td>
										</tr>
										<tr>
											<td>
												12306单号:
												<span>${orderInfo.out_ticket_billno }</span>
											</td>
											<td>
												支付流水号:
												<span>${orderInfo.bank_pay_seq }</span>
											</td>
										</tr>
									</table>
								</div>
							</div>
						</c:if>
						<c:if test="${fn:contains('44,AA', orderInfo.order_status)}">
							<div class="pub_passager_mes oz mb10_all">
								<h4>
									人工付款
								</h4>
								<div class="pub_con">
									<table class="pub_table" style="width:90%;">
										<tr>
											<td class="pub_yuliu" rowspan="3">
											<br /></td>
											<td width="234">
												12306单号:
												<span> 
													<input type="text" name="out_ticket_billno" id="out_ticket_billno"
														value="${orderInfo.out_ticket_billno }" /> 
												</span>
											</td>
										</tr>
										<tr>
											<td>
												<span style="float:left;line-height:20px;padding-right:12px;">
												失败原因:
												</span>
												<select name="error_info" id="error_info" style="float:left;">
													<option value="000">请选择</option>
													<c:choose>
														<c:when test="${orderInfo.channel eq 'qunar' }">
															<option value="0">其他</option>
															<option value="1">所购买的车次坐席已无票</option>
															<option value="2">身份证件已经实名制购票，不能再次购买同日期同车次的车票</option>
															<option value="3">${channel_types[orderInfo.channel]}票价和12306不符</option>
															<option value="4">车次数据与12306不一致</option>
															<option value="5">乘客信息错误</option>
															<option value="6">12306乘客身份信息核验失败</option>
														</c:when>
														<c:otherwise>
															<c:forEach items="${channel_type }" var="s" varStatus="index">
																<c:choose>
																	<c:when test="${fn:contains('qunar', s.key ) }">
																	</c:when>
																	<c:when test="${fn:contains(orderInfo.channel, s.key ) }">
																		<option value="1">所购买的车次坐席已无票</option>
																		<option value="2">身份证件已经实名制购票</option>
																		<option value="3">${channel_types[orderInfo.channel]}票价和12306不符 </option>
																		<option value="4">乘车时间异常</option>
																		<option value="5">证件错误 </option>
																		<option value="6">用户要求取消订单 </option>
																		<option value="7">未通过12306实名认证 </option>
																		<option value="8">乘客身份信息待核验</option>
																	</c:when>
																	<c:otherwise>
																		
																	</c:otherwise>
																</c:choose>
															</c:forEach>
														</c:otherwise>
													</c:choose>
												</select>
											</td>
										</tr>	
									</table>
								</div>
							</div>
						</c:if>
					</div>
					</div>
					</form>
					
					<c:if test="${query_type eq 'mingxi'}">
						<div class="pub_debook_mes  oz mb10_all">
							<p>
								<input type="button" value="返回" onclick="backPage();"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#FFA500;cursor:pointer;width:100px;height:30px;"/>
							</p>
						</div>
					</c:if>
				</div>
			</body>
</html>
