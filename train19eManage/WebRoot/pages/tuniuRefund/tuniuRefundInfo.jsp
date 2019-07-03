<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>退款订单明细页</title>
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
									<span>${orderInfo.order_id}</span>
											<input type="hidden" name="orderId" id="orderId" value="${orderInfo.order_id}"/>
											<input type="hidden" name="streamId" id="streamId" value="${refundTicketInfo.stream_id}"/>
											<input type="hidden" id="statusList" name="statusList" value="${statusList }"/>
											<input type="hidden" id="pageIndex" name="pageIndex" value="${pageIndex }"/>
								</td>
								<td>
									订单状态：
									<span>${order_statuses[orderInfo.order_status] } </span>
								</td>
							</tr>
							<tr>
								<td>
									支付金额：
									<span style="font-weight: bold; color: red; font-size: 20px;">${orderInfo.pay_money}</span>
									元
								</td>
							</tr>
								<tr>
									<td>
										出票时间：
										<span>${orderInfo.out_ticket_time}</span>
									</td>
									<td>
										退款时间：
										<span>${orderInfo.refund_time}</span>
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
								<td class="pub_yuliu" rowspan="5">
									<strong>${orderInfo.train_no}</strong>
									<br />
									车次
								</td>
								<td width="500" colspan="2">
									出 发/到 达：
									<span>${orderInfo.from_city}（${orderInfo.from_time}）</span>—
									<span>${orderInfo.to_city}（${orderInfo.to_time}）</span>
								</td>
							</tr>
							<tr>
								<td width="234">
									坐 席：${seat_types[orderInfo.seat_type] }
								</td>
								<td>
									数 量：
									<span>${fn:length(cpList)}</span> 张
								</td>
							</tr>
							<tr>
								<td>
									票价 ：
									<span style="font-weight: bold; color: red;">${orderInfo.pay_money}</span>
									元 
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
						<c:forEach var="cp" items="${cpList}" varStatus="idx">
							<c:if test="${idx.index != 0}">
								<%
                      			out.println("<div class=\"oz pas1\"><p class=\"border_top\"></p>");
                    			  %>
							</c:if>
									<table class="pub_table" <c:if test="${cp_id !=null && cp_id eq cp.cp_id}">style="background: #FFEEDD; width: 98%; margin: 10px auto;"</c:if>>
										<tr>
											<td class="pub_yuliu" rowspan="6">
											</td>
											<td colspan="2"> 
												车票ID：<span>${cp.cp_id}</span>
											</td>
										</tr>
										<tr>
											<td width="234">
												姓 名：
												<span>${cp.user_name}</span>
											</td>
											<td>
												类 型：
												<span>${ticket_types[cp.ticket_type] }</span>
											</td>
										</tr>
										<tr>
											<td width="234">
												证件类型：
												<span>${ids_types[cp.ids_type] }</span>
											</td>
											<td>
												证件号码：
												<span>${cp.user_ids}</span>
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
												<span>${cp.pay_money }</span>
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
					<input type="hidden" name="stream_id" value="${stream_id }"/>
					<input type="hidden" name="order_id" value="${order_id }"/>
					<input type="hidden" name="cp_id" value="${cp_id }"/>
					<input type="hidden" name="refund_total" value="${orderInfo.refund_total }"/>
					<input type="hidden" name="create_time" value="${refundTicketInfo.create_time }"/>
					<input type="hidden" id="statusList" name="statusList" value="${statusList }"/>
					<input type="hidden" id="pageIndex" name="pageIndex" value="${pageIndex }"/>
					
	            	<h4>用户退款</h4>
		                <div class="pub_con">
		                	<table class="pub_table">
		                        <tr>
		                        	<td class="pub_yuliu"></td>
		                        	<td>创建时间：<span>${refundTicketInfo.create_time}</span></td>
		                        	<td>实际退款时间：<span>${refundTicketInfo.verify_time}</span></td>
		                        	<td></td>
		                        </tr>
		                         <tr>
		                        	<td class="pub_yuliu"></td>
		                        	<td colspan="2">退订金额：
			                        	<span>
				                        		${refundTicketInfo.refund_money }元
			                        	</span>
		                        	</td>
		                        </tr>
		                        <tr>
		                        	<td class="pub_yuliu"></td>
		                        	<td colspan="2">
		                        		<span style="color: red; font-weight: bold;">改签差价：&nbsp;
				                        		${refundTicketInfo.detail_alter}元
		                        		</span>
		                        	</td>
		                        </tr>
		                        <tr>
		                        	<td class="pub_yuliu"></td>
		                        	<td colspan="2">
		                        		<span style="color: red; font-weight: bold;">12306退款：
				                        		${refundTicketInfo.detail_refund }元
		                        		</span>
		                        	</td>
		                        </tr>
		                        <tr>
									<td class="pub_yuliu"></td>
									<td>退款状态：<span style="color:red;">${refund_statuses[refundTicketInfo.refund_status] }</span></td>
		                        </tr>
		                        <tr>
		                        	<td class="pub_yuliu"></td>
		                        	<td colspan="3">退款流水号：${refundTicketInfo.refund_seq }
		                        	<input type="hidden" name="refund_seq" value="${refundTicketInfo.refund_seq }"/>
		                        	</td>
		                        </tr>
			                        <tr><td class="pub_yuliu"></td>
			                        	<td colspan="3">12306单号：<span>${orderInfo.out_ticket_billno }</span></td>	
			                        </tr>
		                       
		                        <tr><td class="pub_yuliu"></td>
		                        	<td>12306退款流水号：
			                        	<span>
			                       			 	<span>${refundTicketInfo.refund_12306_seq }</span>
			                        	</span>
		                        	</td>
		                        </tr>
		                    </table>
		                </div>
		        	</div>
	        	</form>
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
								<table class="pub_table"
									style="width: 500px; margin: 20px auto;">
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
					</div>
				<!--左边内容 end-->
			</div>
	</body>
</html>
