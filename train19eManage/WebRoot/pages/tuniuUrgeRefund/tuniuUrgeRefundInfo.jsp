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
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
		<script type="text/javascript">
			function updateUrgeRefundMoney(){
				var urge_refund_id = $("#urge_refund_id").val();
				var order_id = $("#order_id").val();
				var cp_id = $("#cp_id").val();
				var url="/tuniuUrgeRefund/toUpdateUrgeRefundInfo.do?urge_refund_id="+urge_refund_id+"&order_id="+order_id+"&cp_id="+cp_id;
				showlayer('退款金额',url,'300px','200px')
			}
			function noMoney(urge_refund_id){
			 $.ajax({
				url:"/tuniuUrgeRefund/updateUrgeRefundInfo.do",
				data:{
					"urge_refund_id":urge_refund_id,
					"remark":"暂未核实到退款",
					"urge_status":"44"
				},
				type: "POST",
				cache: false,
				dataType: "json",
				async: true,
				success: function(data){
					if(data>0){
						layer.alert("操作成功！",1,"提示",function(){
		            		layer.close();
		                    location.reload(); 
		            	}); 
					}else{
						layer.alert("操作失败！",2,"提示"); 
					}
				}
			});
		}
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
				<div class="pub_order_mes  oz mb10_all">
					<h4>
						催退款信息
					</h4>
					<div class="pub_con">
						<table class="pub_table">
							<tr>
								<td class="pub_yuliu" rowspan="3">
								</td>
								<td>
									车票ID：<span>${urgeRefundInfo.cp_id}</span>
								</td>
								<td>
									退款金额 ：<span>${urgeRefundInfo.refund_money}</span>元 
								</td>
							</tr>
							<tr>
								<td width="234">
									创建时间：${urgeRefundInfo.create_time}
								</td>
								<td>
									状态：<span>${urge_status_list[urgeRefundInfo.urge_status]}</span>
								</td>
							</tr>
							<tr>
								<td>
									退款时间 ：<span>${urgeRefundInfo.refund_time}</span>元 
								</td>
								<td>
									备注 ：
									<span >${urgeRefundInfo.remark}</span>
									
								</td>
							</tr>
						</table>
					</div>
				</div>
        	</div>
				<div class="pub_debook_mes  oz mb10_all">
					<p>
					
						<c:if test="${urgeRefundInfo.urge_status eq '11'}">
							<input type="hidden" name="urge_refund_id" value="${urgeRefundInfo.urge_refund_id}" id="urge_refund_id"/>
							<input type="hidden" name="order_id" value="${urgeRefundInfo.order_id}" id="order_id"/>
							<input type="hidden" name="cp_id" value="${urgeRefundInfo.cp_id}" id="cp_id"/>
							<input type="button" value="已退款" class="btn" onclick="updateUrgeRefundMoney();" />
							<input type="button" value="暂未核实到退款" class="btn" onclick="noMoney(${urgeRefundInfo.urge_refund_id})" />
						</c:if>
	   					<input type="button" value="返 回" class="btn" onclick="javascript:history.back(-1);" />
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
	</body>
</html>
