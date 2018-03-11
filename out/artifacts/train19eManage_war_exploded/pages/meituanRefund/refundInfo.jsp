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
		<%
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String user_level = loginUserVo.getUser_level();
		%>
		
		
			function refund(orderid, refundseq, refund_status) {
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
				//	alert(orderId);
					var refundMoney=$("#refund_money").val();
				//	alert(refundMoney);
					var streamId=$("#streamId").val();
				//	alert("streamId="+streamId);
					var url = "/meituanRefund/queryRefundMoney.do?orderId="+orderId+"&cpId="+cpId+"&refundMoney="+refundMoney+"&streamId="+streamId+"&version="+new Date();
						$.get(url,function(data){
							if(data == "no" ){
					alert("退款金额不能大于票面金额！");
					return;
					}else{
				
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
   				var statusList = $("#statusList").val();
   				var notifyList = $("#notifyList").val();
				var pageIndex = $("#pageIndex").val();
				var channel = $("#channel").val();
				//var our_remark = $("#our_remark").val();
				$("form:first").attr("action","/meituanRefund/refund.do?orderid=" + orderid + "&refundseq=" + refundseq + "&refund_status=" + refund_status
				+ "&statusList=" + statusList+ "&notifyList=" + notifyList+ "&pageIndex=" + pageIndex + "&channel=" + channel );
				$("form:first").submit();
				}
			});
			}
			function refuseRefund(orderid, refundseq) {
				var our_remark = $("#our_remark").val();
				var refuse_reason = $("#refuse_reason").val();
				if(refuse_reason == "0") {
					alert("拒绝退款原因必须选择一个！");
					return;
				}
				var statusList = $("#statusList").val();
				var notifyList = $("#notifyList").val();
				var pageIndex = $("#pageIndex").val();
				$("form:first").attr("action","/meituanRefund/refuse.do?orderid=" + orderid + "&refundseq=" + refundseq+ 
				"&statusList=" + statusList+ "&notifyList=" + notifyList+ "&pageIndex=" + pageIndex );
				$("form:first").submit();
			}
			
		// 线下退款
		
		function offlineRefund(orderid, refundseq, refund_status,type) {
				var str = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
				if($.trim($("#refund_money_id").val())==""){
					$("#refund_money_id").focus();
					alert("退款金额不能为空！");
					return;
				}
				//限制金额不大于票面金额减去已退款金额
					var orderId=$("#orderId").val();
				//	alert(orderId);
					var cpId=$("#cpId").val();
					var refundMoney=$("#refund_money_id").val();
				//	alert(refundMoney);
					var streamId=$("#streamId").val();
				//	alert("streamId="+streamId);
					var url = "/meituanRefund/queryRefundMoney.do?orderId="+orderId+"&cpId="+cpId+"&refundMoney="+refundMoney+"&streamId="+
					streamId+"&version="+new Date();
					$.get(url,function(data){
					if(data == "no" && type=="refund"){
					alert("退款金额不能大于票面金额！");
					return;
					}else{
						if(!(str.test($.trim($("#refund_money_id").val())))){
							$("#refund_money_id").focus();
							alert("请输入符合规范的金额！");
							return;
						}
						c = parseFloat($.trim($("#refund_money_id").val())).toFixed(2)*100/100;
						if(c<0){
						alert("退款金额不能小于0");
						return;
						}
						var statusList = $("#statusList").val();
						var notifyList = $("#notifyList").val();
						var pageIndex = $("#pageIndex").val();
						var channel = $("#channel").val();
						$("form:first").attr("action","/meituanRefund/refund.do?orderid=" + orderid + "&refundseq=" + refundseq + "&refund_status=" 
						+ refund_status+ "&statusList=" + statusList+ "&notifyList=" + notifyList+ "&pageIndex=" + pageIndex + "&channel=" + channel  );
						$("form:first").submit();
				}
			});
			}
			function offlineRefuseRefund(orderid, refundseq) {
					alert("确认拒绝退款？");
				var statusList = $("#statusList").val();
				var notifyList = $("#notifyList").val();
				var pageIndex = $("#pageIndex").val();
				$("form:first").attr("action","/meituanRefund/refuse.do?orderid=" + orderid + "&refundseq=" + refundseq+ "&statusList=" + statusList+ "&notifyList=" + notifyList+ "&pageIndex=" + pageIndex );
				$("form:first").submit();
			}
			//搁置订单
			function gotoGezhi(order_id,refund_seq){
				var statusList = $("#statusList").val();
				var notifyList = $("#notifyList").val();
				var pageIndex = $("#pageIndex").val();
				
				var uri = "/meituanRefund/updateOrderstatusToRobotGai.do?order_id="+order_id+"&refund_seq="+refund_seq+"&refund_status=44&version="+new Date();
				$.post(uri,function(data){
					if(data=="yes"){
						$("form:first").attr("action","/meituanRefund/queryRefundTicketList.do?refund_status=03&refund_status=07&refund_status=33&refund_status=72&refund_status=73");
						$("form:first").submit();
					//	window.location="/meituanRefund/queryRefundTicketList.do?refund_status=03&refund_status=07&refund_status=33";
					//	window.location.submit();
					}else{
						alert("搁置订单失败");
					}
				});
			}
		//重新通知
		function notifyAgain(stream_id,order_id,cp_id,refund_type){
				var statusList = $("#statusList").val();
				var notifyList = $("#notifyList").val();
				var pageIndex = $("#pageIndex").val();
				var refundMoney="";
				if(refund_type=="11"){
				refundMoney=$("#refund_money").val();
				}else if(refund_type=="22"){
				refundMoney=$("#refund_money_id").val();
				}else if(refund_type=="33"){
				refundMoney=$("#refund_station_money_id").val();
				}
				var url = "/meituanRefund/queryRefundMoney.do?orderId="+order_id+"&cpId="+cp_id+"&refundMoney="+refundMoney+"&streamId="+stream_id+"&version="+new Date();
						$.get(url,function(data){
							if(data == "no" ){
					alert("退款金额不能大于票面金额！");
					return;
					}else{
					if(confirm("确认重新通知？")){
					$("form:first").attr("action","/meituanRefund/notifyAgain.do?stream_id=" + stream_id + "&order_id=" + order_id+"&cp_id="+cp_id
						+"&refundMoney="+refundMoney+ "&statusList=" + statusList+ "&notifyList=" + notifyList+ "&pageIndex=" + pageIndex );
					$("form:first").submit();
							}
						}
					});
		}
		
		//无效订单--》拒绝
		function deleteOrder(stream_id,order_id,cp_id,refund_seq){
				var statusList = $("#statusList").val();
				var notifyList = $("#notifyList").val();
				var pageIndex = $("#pageIndex").val();
				if(confirm("确认删除订单？")){
				$("form:first").attr("action","/meituanRefund/deleteOrder.do?stream_id=" + stream_id + "&order_id="+ order_id+"&cp_id="+cp_id 
				+"&refund_seq="+refund_seq +"&statusList=" + statusList+ "&notifyList=" + notifyList+ "&pageIndex=" + pageIndex );
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
										<span>${orderInfo.order_id}</span>
												<input type="hidden" name="orderId" id="orderId" value="${orderInfo.order_id}"/>
												<input type="hidden" name="channel" id="channel" value="${orderInfo.channel}"/>
												<input type="hidden" name="cpId" id="cpId" value="${cp_id}"/>
												<input type="hidden" name="streamId" id="streamId" value="${refundInfo.stream_id}"/>
												<input type="hidden" name="statusList" id="statusList" value="${statusList}"/>
												<input type="hidden" name="notifyList" id="notifyList" value="${notifyList}"/>
												<input type="hidden" name="pageIndex" id="pageIndex" value="${pageIndex}"/>
									</td>
									<td>
										订单状态：
										<span>${orderstatus[orderInfo.order_status]}</span>
									</td>
								</tr>
								<tr>
									<td>
										支付金额：
										<span style="font-weight: bold; color: red; font-size: 20px;">${orderInfo.pay_money}</span>
										元
									</td>
									<td>
										支付时间：
										<span>${orderInfo.order_time}</span>
									</td>
								</tr>
								<tr>
									<td>
										渠&nbsp;&nbsp;&nbsp;&nbsp;道：
										<span>美团</span>
									</td>
									<td>
										出票时间：
										<span>${orderInfo.out_ticket_time}</span>
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
										<strong>${orderInfo.train_no}</strong>
										<br />
										车次
									</td>
									<td colspan="2">
										出 发/到 达：
										<span>${orderInfo.from_city}（${fn:substring(orderInfo.from_time,11,16)}）</span>—
										<span>${orderInfo.to_city}（${fn:substring(orderInfo.to_time,11,16)}）</span>
									</td>
								</tr>
								<tr>
									<td width="234" colspan="2">
										日 期：
										<span>${fn:substring(orderInfo.from_time, 0,10)}</span>
									</td>
								</tr>
								<tr>
									<td width="234">
										坐 席：${seattype[fn:substring(orderInfo.seat_type,0,1)] }
									</td>
									<td>
										数 量：
										<span>${fn:length(cpInfo)}</span> 张
									</td>
								</tr>
								<tr>
									<td>
										票 价：
										<span style="font-weight: bold; color: red;">${orderInfo.buy_money}</span>
										元
									</td>
									<td>
										总 计：
										<span style="font-weight: bold; color: red; font-size: 20px;">${orderInfo.buy_money}</span>
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
							<c:forEach var="cp" items="${cpInfo}" varStatus="idx">
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
										</td>
										<td>
											坐席：
											<span>${seattype[cp.seat_type]}</span>
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
					<c:if test="${refundInfo.refund_type eq '11' || refundInfo.refund_type eq '55'}">
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
													<input type="text" value="${seattype[cp.alter_seat_type]}" id="alter_seat_type_${cp.cp_id}" name="alter_seat_type_${cp.cp_id}" style="width:50px;" />
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
						<div class="pub_self_take_mes oz mb10_all">
							<h4>
								退款信息
							</h4>
							<div class="pub_con">
								
									<table class="pub_table">
										<tr>
											<td class="pub_yuliu"></td>
											<td>
												12306账号：
												<span style="color: red; font-weight: bold;">${account}</span>
											</td>
											<td>
												12306单号：
												<span>${orderInfo.out_ticket_billno }</span>
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
												代理退款：<span style="font-weight: bold; color: red;">
													<input type="text" style="width:60px;" id="refund_money" name="refund_money" value="${refundInfo.refund_money}" /> </span>
												&nbsp;&nbsp;改签差额： 
													<input type="text" style="width:60px;" id="detail_alter_tickets" name="detail_alter_tickets" value="${refundInfo.detail_alter_tickets}" />
												&nbsp;&nbsp;12306退款：
													<input type="text" style="width:60px;" id="detail_refund" name="detail_refund" value="${refundInfo.detail_refund }" />
											</td>
											<td></td>
										</tr>
										<tr>
											<td class="pub_yuliu"></td>
											<td>
												拒绝退款原因：
												<span style="font-weight: bold; color: red;"> 
												<select name="refuse_reason" id="refuse_reason">
														<option value="0">
															请选择
														</option>
															<option value="3"
																<c:if test="${'3' eq allRefundInfo.refuse_reason }">selected="selected"</c:if>>
																未查询到该车票 
															</option>
															<option value="9"
																<c:if test="${'9' eq allRefundInfo.refuse_reason }">selected="selected"</c:if>>
																退票操作异常，请与客服联系 
															</option>
															<option value="31"
																<c:if test="${'1' eq allRefundInfo.refuse_reason }">selected="selected"</c:if>>
																已改签
															</option>
															<option value="32"
																<c:if test="${'2' eq allRefundInfo.refuse_reason }">selected="selected"</c:if>>
																已退票
															</option>
															<option value="33"
																<c:if test="${'1' eq allRefundInfo.refuse_reason }">selected="selected"</c:if>>
																已出票，只能在窗口办理退票
															</option>
															<option value="39"
																<c:if test="${'2' eq allRefundInfo.refuse_reason }">selected="selected"</c:if>>
																不可退票
															</option>
													</select> 
												</span>
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
												<span style="font-weight: bold; color: red;">
												<input type="text" value="${refundInfo.user_remark}" name="user_remark" /> </span>
											</td>
											<td>
												出票方备注：
												<span style="font-weight: bold; color: red;">
													<input type="text" value="${refundInfo.our_remark}" id="our_remark" name="our_remark" /> </span>
											</td>
											<td></td>
										</tr>
										<!-- 
										<tr>
											<td class="pub_yuliu"></td>
											<td>
												改签明细：
												<span>
													<c:if test="${refundInfo.refund_status !=null && refundInfo.refund_status eq '00'}">
														<input type="text" name="change_ticket_info" id="change_ticket_info" value="${refundInfo.change_ticket_info}"/>
													</c:if>
													<c:if test="${fn:contains('11,22', refundInfo.refund_status)}">
				                        				${refundInfo.change_ticket_info}
				                        			</c:if>
												</span>
											</td>
										</tr> 
										 -->
									</table>
							</div>
						</div>
						</c:if>
						
						<c:if test="${refundInfo.refund_type eq '22'}">
			       				<div class="pub_self_take_mes oz mb10_all">
			           				<h4>账号信息</h4>
			           				<div class="pub_con">
			               				<table class="pub_table">
			               					<tr>
			               						<td class="pub_yuliu"><br /></td>
												<td>
													12306账号：
													<span style="color:red;">
													${account}
													</span>
												</td>
			               					</tr>
			               				</table>
			               			</div>
			               		</div>
			        	</c:if>	
			        	
						<c:if test="${refundInfo.refund_type eq '22'|| refundInfo.refund_type eq '44' || refundInfo.refund_type eq '55'}">
			       				<div class="pub_self_take_mes oz mb10_all">
			           				<h4>线下退款</h4>
			           				<input type="hidden" name="stream_id" value="${stream_id }"/>
									<input type="hidden" name="order_id" value="${order_id }"/>
									<input type="hidden" name="cp_id" value="${cp_id }"/>
									<input type="hidden" name="create_time" value="${refundTicketInfo.create_time }"/>
									<input type="hidden" id="statusList" name="statusList" value="${statusList }"/>
									<input type="hidden" name="notifyList" id="notifyList" value="${notifyList}"/>
			               			<div class="pub_con">
			               				<table class="pub_table">
			               					<tr>
			               						<td class="pub_yuliu"><br /></td>
			               						<td>
			               							成本价格：
			               							<span style="color:red;">
			       										${orderInfo.buy_money }
			               							</span>
			               						</td>
			               						<td>
			               							退款状态：
			               							<span style="color:red;">
			               								${refund_status[refundInfo.refund_status]}
			               							</span>
			               						</td>
			               					</tr>
			               					<tr>
			               						<td class="pub_yuliu"><br /></td>
			               						<td>
			               							<span>线下退款：
			               								<input type="text" name="refund_money" id="refund_money_id" value="${refundInfo.refund_money}" />
			               							</span>
			               						</td>
			               						<td>
			               							<span>12306退款：
			               								<input type="text" name="actual_refund_money" id="actual_refund_money_id" value="${refundInfo.actual_refund_money}" />
			               							</span>
			               						</td>
			               					</tr>
				               				<c:if test="${refundInfo.refund_status eq '72' ||refundInfo.refund_status eq '73'}">
				               					<tr>
				               						<td class="pub_yuliu"><br /></td>
				               						<td>
				               							<span style="color:red;">用户退款：
				               								${sumYhRefundMoney }
				               							</span>
				               						</td>
				               					</tr>
				               					<tr>
				               						<td class="pub_yuliu"><br /></td>
				               						<td>
				               							<span style="color:red;">线下退款：
				               								${sumXxRefundMoney - refundInfo.refund_money}
				               							</span>
				               						</td>
				               					</tr>
				               					<tr>
				               						<td class="pub_yuliu"><br /></td>
				               						<td>
				               							<span style="color:red;">当前票总退款金额为：
				               								${sumRefundMoney - refundInfo.refund_money}&nbsp;(已退款+退款中)
				               							</span>
				               						</td>
				               					</tr>
				               				</c:if>
				               				<c:if test="${refundInfo.refund_status ne '72' && refundInfo.refund_status ne '73'}">
				               					<tr>
				               						<td class="pub_yuliu"><br /></td>
				               						<td>
				               							<span style="color:red;">用户退款：
				               								${sumYhRefundMoney }
				               							</span>
				               						</td>
				               					</tr>
				               					<tr>
				               						<td class="pub_yuliu"><br /></td>
				               						<td>
				               							<span style="color:red;">线下退款：
				               								${sumXxRefundMoney}
				               							</span>
				               						</td>
				               					</tr>
				               					<tr>
				               						<td class="pub_yuliu"><br /></td>
				               						<td>
				               							<span style="color:red;">当前票总退款金额为：
			               								${sumRefundMoney}&nbsp;(已退款+退款中)
			               								</span>
				               						</td>
				               					</tr>
				               				</c:if>
			               				</table>
			               			</div>
			               		</div>
			        	</c:if>	
			        	
			        	
			        	<c:if test="${refundInfo.refund_type eq '33'}">
			       				<div class="pub_self_take_mes oz mb10_all">
			           				<h4>车站退票</h4>
			           				<input type="hidden" name="stream_id" value="${stream_id }"/>
									<input type="hidden" name="order_id" value="${order_id }"/>
									<input type="hidden" name="cp_id" value="${cp_id }"/>
									<input type="hidden" name="create_time" value="${refundTicketInfo.create_time }"/>
									<input type="hidden" id="statusList" name="statusList" value="${statusList }"/>
									<input type="hidden" name="notifyList" id="notifyList" value="${notifyList}"/>
			               			<div class="pub_con">
			               				<table class="pub_table">
			               					<tr>
			               						<td class="pub_yuliu"><br /></td>
			               						<td>
			               							成本价格：
			               							<span style="color:red;">
			       										${orderInfo.buy_money }
			               							</span>
			               						</td>
			               						<td>
			               							退款状态：
			               							<span style="color:red;">
			               								${refund_status[refundInfo.refund_status]}
			               							</span>
			               						</td>
			               					</tr>
			               					<tr>
			               						<td class="pub_yuliu"><br /></td>
			               						<td>
			               							<span>车站退款：
			               								<input type="text" name="refund_money" id="refund_station_money_id" value="${refundInfo.refund_money}" />
			               							</span>
			               						</td>
			               						<td>
			               							<span>12306退款：
			               								<input type="text" name="actual_refund_money" id="actual_station_refund_money_id" value="${refundInfo.actual_refund_money}" />
			               							</span>
			               						</td>
			               					</tr>
			               				</table>
			               			</div>
			               		</div>
			        	</c:if>	
			        	
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
								<!-- 	<input type="button" value="搁置订单" class="btn"
										onclick="gotoGezhi('${refundInfo.order_id}','${refundInfo.refund_seq}')" />-->
								</c:if>
								<c:if test="${refundInfo.refund_status eq '03'}">
									<input type="button" value="拒绝退款" class="btn"
										onclick="refuseRefund('${refundInfo.order_id}', '${refundInfo.refund_seq}')" />
									<input type="button" value="立即退款" class="btn"
										onclick="refund('${refundInfo.order_id}','${refundInfo.refund_seq}','03')" />
								<!--	<input type="button" value="搁置订单" class="btn"
										onclick="gotoGezhi('${refundInfo.order_id}','${refundInfo.refund_seq}')" />-->
								</c:if>
								<c:if test="${refundInfo.refund_status eq '33'}">
									<input type="button" value="退款完成" class="btn"
										onclick="refund('${refundInfo.order_id}', '${refundInfo.refund_seq}','33')" />
								</c:if>
								<c:if test="${refundInfo.refund_status eq '72' ||refundInfo.refund_status eq '73'}">
									<input type="button" value="拒绝退款" class="btn"
										onclick="offlineRefuseRefund('${refundInfo.order_id}', '${refundInfo.refund_seq}')" />
									<input type="button" value="线下退款" class="btn"
										onclick="offlineRefund('${refundInfo.order_id}','${refundInfo.refund_seq}','${refundInfo.refund_status }','refund')" />
									<input type="button" value="搁置订单" class="btn"
										onclick="gotoGezhi('${refundInfo.order_id}','${refundInfo.refund_seq}')" />
									<%
										if ("2".equals(loginUserVo.getUser_level()) ) {
				           			%>
									<input type="button" value="改签退票" class="btn" style="background: orange;"
										onclick="offlineRefund('${refundInfo.order_id}','${refundInfo.refund_seq}','${refundInfo.refund_status }','alter')" />
									<%} %>
								</c:if>
									<c:if test="${refundInfo.refund_status eq '44'}">
									<input type="button" value="拒绝退款" class="btn"
										onclick="offlineRefuseRefund('${refundInfo.order_id}', '${refundInfo.refund_seq}')" />
									<input type="button" value="线下退款" class="btn"
										onclick="offlineRefund('${refundInfo.order_id}','${refundInfo.refund_seq}','${refundInfo.refund_status }','refund')" />
								</c:if>
								<c:if test="${refundInfo.notify_status ne '22'}">
									<input type="button" value="重新通知" class="btn"
									   onclick="notifyAgain('${refundInfo.stream_id}','${refundInfo.order_id}','${cp_id}','${refundInfo.refund_type}')"/>
								    <input type="button" value="删除订单" class="btn"
									   onclick="deleteOrder('${refundInfo.stream_id}','${refundInfo.order_id}','${cp_id}','${refundInfo.refund_seq}')"/>
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
