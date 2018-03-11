<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>改签详情页面</title>
		<link rel="stylesheet" href="/css/style.css" type="text/css" />
		<style type="text/css">
			.pub_table td{height:25px;}
			.pub_yuliu {width:130px;}
		</style>
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript">
			function alter(change_id,alter_status) {
			var num_error = 0;
			if(alter_status == "14"){
				$("input[name='cpIdList']").each(function(){ 
				var cp_id = $(this).val();
				var money = document.getElementById("alter_buy_money_"+cp_id).value;
				var box = document.getElementById("alter_train_box_"+cp_id).value;
				var seat = document.getElementById("alter_seat_no_"+cp_id).value;
				var pay_limit_time = document.getElementById("pay_limit_time").value;
				var merchant_id = document.getElementById("merchant_id").value;
				
				
				var reg=/^[-\+]?\d+(\.\d+)?$/;
				if(!reg.test(money) || Number(money)==0){
					alert("改签后票价填写异常！");
					num_error+=1;
				}
				if(box.replace(/\s/g, "").length == 0 ){
					alert("车厢号不为空！");
					num_error+=1;
				}
				if(seat.replace(/\s/g, "").length == 0 ){
					alert("座位号不为空！");
					num_error+=1;
				}
				if(merchant_id == '301030' && pay_limit_time.replace(/\s/g, "").length == 0 ){
					alert("支付截止时间不得为空！");
					num_error+=1;
				}
				});
				if(num_error>0){
					return;
				}
			}
			if((alter_status == "15" || alter_status == "35") && $("#fail_reason").val() == ''){
					alert("请选择失败原因！");
					return;
			}
				
				
			 submitForm(change_id,alter_status);
			
			}
	     //提交表单
		function submitForm(change_id,alter_status){
			
			var statusList = $("#statusList").val();
  				var notifyList = $("#notifyList").val();
			var pageIndex = $("#pageIndex").val();
			var order_id = $("#orderId").val();	
			$("form:first").attr("action","/elongAlter/alter.do?change_id=" + change_id  + "&order_id=" + order_id+ "&alter_status=" + alter_status
				+ "&statusList=" + statusList+ "&notifyList=" + notifyList+ "&pageIndex=" + pageIndex );
			
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
										<span>${alterInfo.order_id}</span>
												<input type="hidden" name="orderId" id="orderId" value="${alterInfo.order_id}"/>
												<input type="hidden" name="statusList" id="statusList" value="${statusList}"/>
												<input type="hidden" name="notifyList" id="notifyList" value="${notifyList}"/>
												<input type="hidden" name="pageIndex" id="pageIndex" value="${pageIndex}"/>
									</td>
									<td>
										订单状态：
										<span>${alterStatus[alterInfo.change_status]}</span>
									</td>
								</tr>
								<tr>
									<td colspan="2">
										改签差额(新票小于旧票)：
										<span style="font-weight: bold; color: red; font-size: 20px;">${alterInfo.change_diff_money}</span>
										元
									</td>
								</tr>
								<tr>
									<td colspan="2">
										退还旧票金额(新票大于旧票)：
										<span style="font-weight: bold; color: red; font-size: 20px;">${alterInfo.change_refund_money}</span>
										元
									</td>
								</tr>
								<tr>
									<td colspan="2">
										收取新票金额(新票大于旧票)：
										<span style="font-weight: bold; color: red; font-size: 20px;">${alterInfo.change_receive_money}</span>
										元
									</td>
								</tr>
							</table>
						</div>
					</div>
					<div class="pub_order_mes  oz mb10_all">
						<h4>
							车票信息
						</h4>
						<div class="pub_con">
							<table class="pub_table">
								<tr>
									<td class="pub_yuliu" rowspan="4">
										<strong>${alterInfo.train_no}</strong>
										<br />
										车次
									</td>
									<td colspan="2">
										出 发/到 达：
										<span>${originOrderInfo.from_city}（${fn:substring(alterInfo.from_time,11,16)}）</span>—
										<span>${originOrderInfo.to_city}</span>
									</td>
								</tr>
								<tr>
									<td width="234" colspan="2">
										日 期：
										<span>${fn:substring(alterInfo.from_time, 0,10)}</span>
									</td>
								</tr>
								<tr>
									<td width="234">
										数 量：
										<span>${fn:length(cpInfo)}</span> 张
									</td>
								</tr>
								<tr>
									<td></td>

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
											<span>${ticketType[cp.ticket_type]}</span>
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
											<input type="hidden" id="cpIdList" name="cpIdList" value="${cp.cp_id}"/>
										</td>
										<td>
											坐席：
											<span>${seattype[cp.seat_type]}</span>
										</td>
									</tr>
									<tr>
										<td>
											新票号：
											<span style="color:#f60;">${cp.new_cp_id}</span>
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
						<div class="pub_self_take_mes oz mb10_all">
							<h4>
								
								<input type="hidden" name="ischangeto" value="${alterInfo.ischangeto}"/>
								<c:if test="${alterInfo.ischangeto=='1'}">变更到站信息</c:if>
								<c:if test="${alterInfo.ischangeto=='0'}"> 改签信息</c:if>
							</h4>
							<div class="pub_con">
							<span style="color:red;font-weight: bold;">是否支持无座票: ${alterInfo.hasSeat eq "0" ?"是":"否"}</span>
							<br/>
							<span style="color:red;font-weight: bold;font-size: 15px;">客户预选的座位号：
									<c:choose>
										<c:when test="${alterInfo.isChooseSeats eq '0'}">
											无选座信息
										</c:when>
										<c:otherwise>
										 	${alterInfo.chooseSeats}
										</c:otherwise>
									</c:choose>
							</span>
							
							<c:forEach var="cp" items="${cpInfo}" varStatus="idx">
								<c:if test="${idx.index != 0}">
									<%
										out.println("<div class=\"oz pas1\"><p class=\"border_top\"></p>");
									%>
								</c:if>
									<table class="pub_table">
										<tr>
											<td class="pub_yuliu" rowspan="6">
												<span>${cp.user_name}</span>
											</td>
											<td>
												改签车次：
												${alterInfo.change_train_no}
											</td>
											<td>
												改签日期：
												${fn:substringBefore(alterInfo.change_travel_time, ' ')}
											</td>
											<td>
												改签发车时间：
												${fn:substringAfter(alterInfo.change_from_time, ' ')}
											</td>
										</tr>
										<tr>
											<td>出发站：${alterInfo.from_city}</td>
											<td>到达站：${alterInfo.to_city}</td>
											<td>类型：${ticketType[cp.ticket_type]}</td>
										</tr>
										<tr>
											<td>
												改签后票价：
												<span>
													<input type="text" value="${cp.change_buy_money}" id="alter_buy_money_${cp.cp_id}" name="alter_buy_money_${cp.cp_id}" style="width:70px;" />
												</span>
												元
											</td>
											
											<td colspan="2">
												改签坐席：
												<span>
													<input type="text" value="${cp.change_train_box}" id="alter_train_box_${cp.cp_id}" name="alter_train_box_${cp.cp_id}" style="width:30px;" />
													车厢-
													<input type="text" value="${cp.change_seat_no}" id="alter_seat_no_${cp.cp_id}" name="alter_seat_no_${cp.cp_id}" style="width:30px;" />
													座位-
													${seattype[cp.change_seat_type]}
													<!-- 
													<input type="text" value="${seattype[cp.change_seat_type]}" id="alter_seat_type_${cp.cp_id}" name="alter_seat_type_${cp.cp_id}" style="width:50px;" />
													-->
												</span>
											</td>
										</tr>
										<input type="hidden" value= "${alterInfo.merchant_id}" id="merchant_id"/>
										<tr>
											<td colspan="2">
												改签后支付截止时间：
												<span>
													<input type="text" value="${alterInfo.pay_limit_time}" id="pay_limit_time" name="pay_limit_time" style="width:150px;" />
												</span>
											</td>
											<td><span style="color:red">例：2010-10-10 08:00:00</span></td>
										</tr>
										<tr>
											<td colspan="3">
												
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
					<!--
						<div class="pub_self_take_mes oz mb10_all">
							<h4>
								改签明细
							</h4>
							<div class="pub_con">
								<table>
										<tr>
										<td rowspan="5">
										</td>
										</tr>
										<tr>
											<td></td><td></td>
											<td style="color: red; font-weight: bold;" colspan="3">
												改签差额(新票小于旧票)：&nbsp;&nbsp;&nbsp;&nbsp;
												<span>
													${alterInfo.change_diff_money}
												</span>
												元
											</td>
										</tr>
										<tr>
										<td></td><td></td>
											<td style="color: blue; font-weight: bold;"colspan="3">
												退还旧票金额(新票大于旧票)：
												<span>
													${alterInfo.change_refund_money}
												</span>
												元
											</td>
										</tr>
										<tr><td></td><td></td>
											<td style="color: blue; font-weight: bold;"colspan="3">
												收取新票金额(新票大于旧票)：
												<span>
													${alterInfo.change_receive_money}
												</span>
												元
											</td>
										</tr>
								</table>
							</div>
						</div>  -->
						<input type="hidden" name="alterChangeId" value="${alterInfo.change_id }"/>
						<input type="hidden" name="account" value="${account }"/>
			       				<div class="pub_self_take_mes oz mb10_all">
			           				<h4>账号信息</h4>
			           				<div class="pub_con">
			               				<table class="pub_table">
			               					<tr>
			               						<td class="pub_yuliu"><br /></td>
												<td>
													12306账号：
													<span style="color:red;">
													${account.acc_username}/${account.acc_password }
													</span>
												</td>
			               					</tr>
			               				</table>
			               			</div>
			               		</div>
			             <div class="pub_self_take_mes oz mb10_all">
			           				<h4>失败原因</h4>
			           				<div class="pub_con">
			               				<table class="pub_table">
			               					<tr>
			               						<td class="pub_yuliu"><br /></td>
												<td>
													改签预订失败原因：
												<span style="font-weight: bold; color: red;"> 
												<select	name="fail_reason" id="fail_reason">
														<option value="">
															请选择
														</option>
														<c:forEach items="${failReason }" var="f" varStatus="index">
														 <option value="${f.key }"	
														 <c:if test="${alterInfo.fail_reason eq f.key }">selected="selected"</c:if>>
															${f.value }
														</option>
														</c:forEach>
													<!-- <option value="401"	<c:if test="${alterInfo.fail_reason eq '401' }">selected="selected"</c:if>>
															请求时间已超时，出票失败
														</option>
														<option value="403"	<c:if test="${alterInfo.fail_reason eq '403' }">selected="selected"</c:if>>
															  账户余额不足
														</option>
														<option value="301"	<c:if test="${alterInfo.fail_reason eq '301' }">selected="selected"</c:if>>
															 没有余票
														</option>
														<option value="305"	<c:if test="${alterInfo.fail_reason eq '305' }">selected="selected"</c:if>>
															乘客已经预定过该车次
														</option>
														<option value="308"	<c:if test="${alterInfo.fail_reason eq '308' }">selected="selected"</c:if>>
															乘客身份信息未通过验证
														</option>
														<option value="309"	<c:if test="${alterInfo.fail_reason eq '309' }">selected="selected"</c:if>>
															没有足够的票
														</option>
														<option value="310"	<c:if test="${alterInfo.fail_reason eq '310' }">selected="selected"</c:if>>
															本次购票与其他订单行程冲突
														</option>
														<option value="313"	<c:if test="${alterInfo.fail_reason eq '313' }">selected="selected"</c:if>>
															12306提示其他异常
														</option>-->	
													</select> 
													
													</span>
												</td>
			               					</tr>
			               				</table>
			               			</div>
			               		</div>
			        	
						<div class="pub_debook_mes  oz mb10_all">
							<p>
								<input type="button" value="返 回" class="btn"
									onclick="javascript:history.back(-1);" />
							<c:if test="${opt_type eq '1'}">
							<input type="button" value="改签成功" class="btn"
										onclick="alter('${alterInfo.change_id}','14')" />
							<input type="button" value="改签失败" class="btn"
										onclick="alter('${alterInfo.change_id}','15')" />
							</c:if>
							<c:if test="${opt_type eq '2'}">
							<input type="button" value="支付成功" class="btn"
										onclick="alter('${alterInfo.change_id}','34')" />
							<input type="button" value="支付失败" class="btn"
										onclick="alter('${alterInfo.change_id}','35')" />					
							</c:if>
							</p>
						</div>

						<div class="pub_passager_mes oz mb10_all">
							<h4>
								历史操作
							</h4>
							<div class="pub_con">
								<table class="pub_table" style="width: 650px; margin: 20px 20px;">
									<tr>
										<th style="width: 50px;">
											序号
										</th>
										<th style="width: 400px;">
											操作日志
										</th>
										<th style="width: 100px;">
											操作时间
										</th>
										<th style="width: 100px;">
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
						<!--左边内容 end-->
					</div>
				</div>
			</div>
		</form>
	</body>
</html>
