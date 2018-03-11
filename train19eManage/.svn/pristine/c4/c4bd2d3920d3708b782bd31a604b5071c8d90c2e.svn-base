<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>退票详情页面</title>
		<link rel="stylesheet" href="/css/style.css" type="text/css" />
		<style type="text/css">
			.pub_table td{height:25px;}
			.pub_yuliu {width:130px;}
		</style>
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript">
		
			function refund(orderid,cpid,refundseq,refund_status) {
				var refund_12306_seq = $("#refund_12306_seq").val();
				var str = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
				if($.trim($("#refund_money").val())==""){
					$("#refund_money").focus();
					alert("退款金额不能为空！");
					return;
				}
				//限制金额不大于票面金额减去已退款金额
				var orderId=$("#orderId").val();
				var cpId=$("#cpId").val();
				var refundMoney=$("#refund_money").val();
				//alert(refundMoney);
				
				var refund12306Money=$("#refund_12306_money").val();
				//alert(refund12306Money);

				if(parseFloat(refundMoney).toFixed(2) < parseFloat(refund12306Money).toFixed(2)){
				 alert("退款金额不能大于票面金额！");
				 return;
				}else{
				
					if($.trim($("#refund_12306_money").val())==""){
						$("#refund_12306_money").focus();
						alert("12306退款不能为空！");
						return;
					}
					if(!(str.test($.trim($("#refund_money").val())))){
						$("#refund_money").focus();
						alert("请输入符合规范的金额！");
						return;
					}
					if($.trim($("#refund_12306_money").val()).indexOf("，")>=0){
						$("#refund_12306_money").focus();
						alert("请采用英文逗号！");
						return;
					}
					
					c = parseFloat($.trim($("#refund_money").val())).toFixed(2)*100/100;
					if(c<0){
					alert("退款金额不能小于0");
					return;
					}
	   				var statusList = $("#statusList").val();
					var pageIndex = $("#pageIndex").val();
					if($.trim($("#refund_12306_money").val())=="0" || $.trim($("#refund_12306_money").val())=="0.0" ||$.trim($("#refund_12306_money").val())=="0.00"){
					if(confirm("12306退款金额为0,确定退款吗?")){
					$("form:first").attr("action","/jdRefund/refund.do?orderId=" + orderId+"&cpId="+cpId + "&refundseq=" + refundseq + 
					"&refund_status=" + refund_status+ "&statusList=" + statusList+"&pageIndex=" + pageIndex);
					$("form:first").submit();
					}
					}else{
						$("form:first").attr("action","/jdRefund/refund.do?orderId=" + orderId+"&cpId="+cpId + "&refundseq=" + refundseq + 
						"&refund_status=" + refund_status+ "&statusList=" + statusList+"&pageIndex=" + pageIndex);
						$("form:first").submit();
					}
				}
			}
			
			function refuseRefund(orderid,cpid, refundseq) {
				var refuse_reason = $("#refuse_reason").val();
				if(refuse_reason == "0") {
					alert("退款失败原因必须选择一个！");
					return;
				}
				
				if(refuse_reason == "41" && $("#our_remark").val() == ""){
					alert("【同程】渠道选择拒绝退款原因为【旅游旺季】时，出票方备注不能为空！");
					return;
				}else{
					var statusList = $("#statusList").val();
					var pageIndex = $("#pageIndex").val();
					$("form:first").attr("action","/jdRefund/refuse.do?orderId=" + orderid+"&cpId="+cpid + "&refundseq=" + refundseq+"&statusList=" + statusList+"&pageIndex=" + pageIndex);
					$("form:first").submit();
				}
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
										<span>${jdRefundInfo.order_id}</span>
												<input type="hidden" name="orderId" id="orderId" value="${jdRefundInfo.order_id}"/>
												<input type="hidden" name="cpId" id="cpId" value="${cp_id}"/>
												<input type="hidden" name="channel" id="channel" value="${jdRefundInfo.channel}"/>
												<input type="hidden" name="streamId" id="streamId" value="${jdRefundInfo.stream_id}"/>
												<input type="hidden" name="channelList" id="channelList" value="${channelList}"/>
												<input type="hidden" name="statusList" id="statusList" value="${statusList}"/>
												<input type="hidden" name="notifyList" id="notifyList" value="${notifyList}"/>
												<input type="hidden" name="pageIndex" id="pageIndex" value="${pageIndex}"/>
												<input type="hidden" name="refund_total" value="${orderInfo.refund_total }"/>
									</td>
									<td>
										订单状态：
										<span>${refundStatus[jdRefundInfo.order_status]}</span>
									</td>
								</tr>
								<tr>
									<td>
										渠&nbsp;&nbsp;&nbsp;&nbsp;道：
										<span>${merchantMap[jdRefundInfo.channel] }</span>
									</td>
									<td>
										出票时间：
										<span>${jdRefundInfo.out_ticket_time}</span>
									</td>
								</tr>
								<tr>
									<td>
										支付金额：
										<span style="font-weight: bold; color: red; font-size: 20px;">${jdRefundInfo.buy_money}</span>
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
										<strong>${jdRefundInfo.train_no}</strong>
										<br />
										车次
									</td>
									<td colspan="2">
										出 发/到 达：
									<!--<span>${allRefundInfo.from_station}（${fn:substring(allRefundInfo.from_time,11,16)}）</span>—
										<span>${allRefundInfo.arrive_station}（${fn:substring(allRefundInfo.to_time,11,16)}）</span>  -->
										<span>${jdRefundInfo.from_station}&nbsp;</span>—
										<span>${jdRefundInfo.arrive_station}</span>
									</td>
								</tr>
								<tr>
									<td width="234" colspan="2">
										日 期：
										<span>${jdRefundInfo.from_time}</span>
									</td>
								</tr>
								<tr>
									<td width="234">
										坐 席：${seat_types[jdRefundInfo.seat_type] }
									</td>
									
								</tr>
								<tr>
									<td>
										票 价：
										<span style="font-weight: bold; color: red;">${jdRefundInfo.buy_money}</span>
										元
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
							<c:forEach var="cp" items="${cpList}" varStatus="idx">
							<c:if test="${idx.index != 0}">
								<%
                      			out.println("<div class=\"oz pas1\"><p class=\"border_top\"></p>");
                    			  %>
							</c:if>
								<table class="pub_table" <c:if test="${cp_id !=null && cp_id eq cp.cp_id}">style="background: #FFEEDD; width: 98%; margin: 10px auto;"</c:if>>
									<tr>
										<td class="pub_yuliu" rowspan="5">

										</td>
										<td width="234">
											姓 名：
											<span>${cp.user_name}</span>
										</td>
										<td>
											类 型：
											<span>${ticket_types[cp.ticket_type]}</span>
										</td>
									</tr>
									<tr>
										<td width="234">
											证件类型：
											<span>${ids_types[cp.cert_type] }</span>
										</td>
										<td>
											证件号码：
											<span>${cp.cert_no}</span>
										</td>
									</tr>
									<tr>
										<td>
											车票号：
											<span>${cp.cp_id}</span>
										</td>
										<td>
											坐席：
											<span>${seat_types[cp.seat_type]}</span>
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
								退款信息
							</h4>
							<div class="pub_con">
								
									<table class="pub_table">
										<tr>
											<td class="pub_yuliu"></td>
											<td>
												京东账号：
												<span style="color: red; font-weight: bold;">${jdRefundInfo.jd_account_name}|${jdRefundInfo.jd_account_pwd}</span>
											</td>
											<td>
												京东流水号：
												<span>${jdRefundInfo.jd_order_id}|</span>
											</td>
										</tr>
										<tr>
											<td class="pub_yuliu"></td>
											<td>
												退款状态：
												<span style="font-weight: bold; color: red;">${refundStatus[jdRefundInfo.order_status]}</span>
											</td>
											<td>
												创建时间：
												<span>${jdRefundInfo.create_time}</span>
											</td>
										</tr>
										<tr>
											<td class="pub_yuliu"></td>
											<td style="font-weight: bold; color: red;" colspan="2">
												退订金额：<span style="font-weight: bold; color: red;">
													<input type="text" style="width:60px;" id="refund_money" name="refund_money" value="${jdRefundInfo.refund_money}" /> </span>
												
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												12306退款：
													<input type="text" style="width:60px;" id="refund_12306_money" name="refund_12306_money" value="${jdRefundInfo.refund_12306_money}" />
											</td>
											<td></td>
										</tr>
										<tr>
											<td class="pub_yuliu"></td>
											<td>
												京东退款流水号：
												<input type="text" value="${jdRefundInfo.jd_order_id}"
													name="refund_12306_seq" id="refund_12306_seq" />
											</td>
											<td>
												用户备注：
												${jdRefundInfo.user_remark}
											</td>
										</tr>
										<tr>
											<td class="pub_yuliu"></td>
				                        	<td colspan="3">
												退款失败原因：
												<span style="font-weight: bold; color: red;"> 
												<select name="refuse_reason" id="refuse_reason">
													<option value="0">
															请选择
													</option>
													<c:forEach var="reason" items="${refuse_Reason}">
														<c:choose>
															<c:when test="${reason.key eq jdRefundInfo.channel}">
																	<c:forEach var="rs" items="${reason.value}">
																	<option value="${rs.key}"
																		<c:if test="${rs.key eq jdRefundInfo.refuse_reason }">selected="selected"</c:if>>
																		${rs.value }
																	</option>
																	</c:forEach>
															</c:when>
															<c:when test="${!fn:contains('tongcheng,meituan,301030,tuniu',jdRefundInfo.channel)}">
																<c:if test="${reason.key eq 'OTHER'}">
																<c:forEach var="rs" items="${reason.value }">
																	<c:if test="${jdRefundInfo.channel eq 'elong'}">
																	<c:if test="${ rs.key ne '4' && rs.key ne '6'  }">
																		<option value="${rs.key}"
																			<c:if test="${rs.key eq jdRefundInfo.refuse_reason }">selected="selected"</c:if>>
																			${rs.value }
																		</option>
																	</c:if>
																	</c:if>
																	
																	
																	<c:if test="${jdRefundInfo.channel ne 'elong' && jdRefundInfo.channel ne 'qunar'}">
																		<option value="${rs.key}"
																			<c:if test="${rs.key eq jdRefundInfo.refuse_reason }">selected="selected"</c:if>>
																			${rs.value }
																		</option>
																	</c:if>
																</c:forEach>
																</c:if>
															</c:when>
														</c:choose>
													</c:forEach>
												</select>
											 </span>
											</td>
										</tr>
										<tr>
											<td class="pub_yuliu"></td>
				                        	<td colspan="3">出票方备注：
					                        	<span>
					                        		<c:if test="${(jdRefundInfo.order_status ne '00' && jdRefundInfo.order_status ne '09')}">
						                        			${jdRefundInfo.our_remark}
						                        	</c:if>
						                        	<c:if test="${(jdRefundInfo.order_status eq '00' || jdRefundInfo.order_status eq '09')}">
														<!--
														<select name="our_remark" style="width: 380px;" id="our_remark">
														    <option value="0" > </option>
															<option value="1" >已无法在网上办理退票，请自行去车站办理</option>
															<option value="2" >请先到车站办理退票，然后填写用户备注为：已退票，再提交退款。</option>
															<option value="3" >已与铁道部核实，乘客未退票</option>
															<option value="5" >其它</option>
														</select>
														 -->
														 <input name="our_remark" style="width: 380px;" id="our_remark" value=""/>
													</c:if>
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
							<c:if test="${isActive eq '1'}">
								<c:if test="${jdRefundInfo.order_status eq '09'}">
							
									<input type="button" value="退款失败" class="btn"
										onclick="refuseRefund('${jdRefundInfo.order_id}', '${jdRefundInfo.cp_id}', '${jdRefundInfo.refund_seq}')" />
									<input type="button" value="立即退款" class="btn"
										onclick="refund('${jdRefundInfo.order_id}', '${jdRefundInfo.cp_id}','${jdRefundInfo.refund_seq}','11')" />
								</c:if>

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
												${hs.order_optlog}
											</td>
											<td>
												${hs.create_time }
											</td>
											<td>
												${hs.opter }
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
