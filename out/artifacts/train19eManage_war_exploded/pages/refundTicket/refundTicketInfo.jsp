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
		<style type="text/css">
			.pub_table td{height:25px;}
			.pub_yuliu {width:130px;}
		</style>
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript">

	function sumbitRefund(){
		var str = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
		if($.trim($("#refund_money").val())==""){
			$("#refund_money").focus();
			alert("退款金额不能为空！");
			return;
		}
		//限制金额不大于票面金额减去已退款金额
		var orderId=$("#orderId").val();
	//	alert(orderId);
		var refundMoney=$("#refund_money").val();
	//	alert(refundMoney);
		var streamId=$("#streamId").val();
	//	alert("streamId="+streamId);
		var url = "/refundTicket/queryRefundMoney.do?orderId="+orderId+"&refundMoney="+refundMoney+"&streamId="+streamId+"&version="+new Date();
			$.get(url,function(data){
				//alert(data);
				if(data == "no" ){
					alert("退款金额不能大于票面金额！");
					return;
				}else if(data =="yes"){
		
		if($.trim($("#actual_refund_money").val())==""){
			$("#actual_refund_money").focus();
			alert("12306退款金额不能为空！");
			return;
		}
		if(($.trim($("#alter_tickets_money").val())!="") && (!(str.test($.trim($("#alter_tickets_money").val()))))){
			$("#alter_tickets_money").focus();
			alert("请输入符合规范的金额！");
			return;
		}
		if(!(str.test($.trim($("#actual_refund_money").val())))){
			$("#actual_refund_money").focus();
			alert("请输入符合规范的金额！");
			return;
		}
		if(!(str.test($.trim($("#refund_money").val())))){
			$("#refund_money").focus();
			alert("请输入符合规范的金额！");
			return;
		}
		
		c = parseFloat($.trim($("#refund_money").val())).toFixed(2)*100/100;
		if(c<=0){
		alert("退款金额不能小于0或等于0");
		return;
		}
		d = parseFloat($.trim($("#actual_refund_money").val())).toFixed(2)*100/100;
		if(d<=0){
		alert("退款金额不能小于0或等于0");
		return;
		}
   			
		if(confirm("是否已经核对12306，并且退款？")){
			$("#refundForm").submit();
		}
		
	}
	});
	}
//	function refuseRefund(){
//		if($.trim($("#our_remark").val())==""){
//			$("#our_remark").focus();
//			alert("退款备注不能为空！");
//			return;
//		}
//		if(confirm("确认拒绝退款？并且在备注中说明拒绝原因。")){
//		
//			$("#refundForm").attr("action","/refundTicket/updateRefuseRefund.do");
//			$("#refundForm").submit();
//			
//		}
//	}

	function refuseRefund(){
		var our_remark = $("#our_remark").val(); 
		//alert(our_remark);
		if(our_remark == "0"){
			alert("请选择退款备注！");
			return false;
		}
		if(confirm("确认拒绝退款？")){
		$("#refundForm").attr("action","/refundTicket/updateRefuseRefund.do");
		$("#refundForm").submit();}
	}
	
	function differRefund(){
		if($.trim($("#refund_money_id").val())==""){
			$("#refund_money_id").focus();
			alert("退款金额不能为空！");
			return;
		}
		
		var str = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
		if(!(str.test($.trim($("#refund_money_id").val())))){
			$("#refund_money_id").focus();
			alert("请输入符合规范的金额！");
			return;
		}
		
		c = parseFloat($.trim($("#refund_money_id").val())).toFixed(2)*100/100;
		if(c<=0){
		alert("退款金额不能小于0或等于0");
		return;
		}
		//if(confirm("确认差额退款？并且核对退款金额。")){		
			$("#differForm").submit();
		//}
	}
	
	/**线下退款*/
		function xianxiaRefund(){
		if($.trim($("#refund_money_xx_id").val())==""){
			$("#refund_money_xx_id").focus();
			alert("退款金额不能为空！");
			return;
		}
		var str = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
		if(!(str.test($.trim($("#refund_money_xx_id").val())))){
			$("#refund_money_xx_id").focus();
			alert("请输入符合规范的金额！");
			return;
		}
		if(!(str.test($.trim($("#actual_refund_money_xx_id").val())))){
			$("#actual_refund_money_xx_id").focus();
			alert("请输入符合规范的12306退款金额！");
			return;
		}
		c = parseFloat($.trim($("#refund_money_xx_id").val())).toFixed(2)*100/100;
		if(c<=0){
		alert("退款金额不能小于0或等于0");
		return;
		}
			$("#xianxiaForm").submit();
	}
	
	function out_TicketRefund(){
		if($.trim($("#refund_money_OutTicket").val())==""){
			$("#refund_money_OutTicket").focus();
			alert("全额退款金额不能为空！");
			return;
		}
		var str = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
		if(!(str.test($.trim($("#refund_money_OutTicket").val())))){
			$("#refund_money_OutTicket").focus();
			alert("请输入符合规范的金额！");
			return;
		}
			
		c = parseFloat($.trim($("#refund_money_OutTicket").val())).toFixed(2)*100/100;
		if(c<=0){
		alert("退款金额不能小于0或等于0");
		return;
		}
		//if(confirm("确认全额退款？并且核对退款金额。")){		
			$("#out_ticketForm").submit();
		//}
	}
	
	//取消预约退款
	function cancel_BookRefund(){
		if($.trim($("#refund_money_OutTicket").val())==""){
			$("#refund_money_OutTicket").focus();
			alert("取消预约退款金额不能为空！");
			return;
		}
		var str = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
		if(!(str.test($.trim($("#refund_money_OutTicket").val())))){
			$("#refund_money_OutTicket").focus();
			alert("请输入符合规范的金额！");
			return;
		}
			
		c = parseFloat($.trim($("#refund_money_OutTicket").val())).toFixed(2)*100/100;
		if(c<=0){
		alert("退款金额不能小于0或等于0");
		return;
		}
		//if(confirm("确认全额退款？并且核对退款金额。")){		
			$("#out_ticketForm").submit();
		//}
	}
	
	
	//搁置订单 
	function sumbitGezhi(refund_status){
		if(confirm("确认搁置订单？")){
			$("#refundForm").attr("action","/refundTicket/updateGezhiRefund.do?refund_status="+refund_status);
			$("#refundForm").submit();
		}
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
								</td>
								<td>
									订单状态：
									<span>${order_statuses[orderInfo.order_status] } </span>
								</td>
							</tr>
							<tr>
								<td width="234">
									代理商姓名：
									<span>${orderInfo.dealer_name}</span>
								</td>
								<td>
									代理商账号：
									<span>${orderInfo.user_phone}</span>
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
									EOP单号：
									<span>${orderInfo.eop_order_id }</span>
								</td>
							</tr>
							<c:if test="${fn:contains('55,66,77', list.refund_status)}">
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
								<tr>
									<td>
										12306单号：
										<span>${orderInfo.out_ticket_billno }</span>
									</td>
								</tr>
							</c:if>
							
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
								<td width="234" colspan="2">
									出 发/到 达：
									<span>${orderInfo.from_city}（${orderInfo.from_time}）</span>—
									<span>${orderInfo.to_city}（${orderInfo.to_time}）</span>
								</td>
							</tr>
							<tr>
								<td width="234" colspan="2">
									日 期：
									<span>${orderInfo.travel_time}</span>
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
									<span style="font-weight: bold; color: red;">${orderInfo.ticket_pay_money}</span>
									元 保 险：
									<span style="font-weight: bold; color: red;">${orderInfo.bx_pay_money}</span>
									元
								</td>
								<td>
									服务：
									<span style="font-weight: bold; color: red;">${orderInfo.server_pay_money}
									</span> 元
								</td>
							</tr>
							<tr>
								<td>
									总 计：
									<span style="font-weight: bold; color: red; font-size: 20px;">${orderInfo.pay_money}</span>
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
											<td class="pub_yuliu" rowspan="5">
											</td>
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
												保 险：
												<span>${cp.name_type }</span>
											</td>
											<td>
												保险单号：
												<span>${cp.bx_code }</span>
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
				
				<!-- 退款流程开始 -->
				<c:if test="${refundTicketInfo.refund_type eq '1' || refundTicketInfo.refund_type eq '6'}">
					<form action="/refundTicket/updateRefundTicket.do?
						cp_id=${cp_id }&order_id=${order_id }&stream_id=${stream_id }&statusList=${statusList }&typeList=${typeList }&pageIndex=${pageIndex }" 
						method="post" name="refundForm" id="refundForm">
					<div class="pub_self_take_mes oz mb10_all">
							<h4>
								改签信息
							</h4>
							<div class="pub_con">
								<c:forEach var="cp" items="${cpList}" varStatus="idx">
								<c:if test="${cp_id !=null && cp_id eq cp.cp_id}">
									<table class="pub_table">
										<tr>
											<td class="pub_yuliu" rowspan="2">
												<span>${cp.user_name}</span>
												<input type="hidden" value="${cp.cp_id}" id="cp_id_${cp.cp_id}" name="cp_id_${cp.cp_id}" />
												<input type="hidden" id="statusList" name="statusList" value="${statusList }"/>
												<input type="hidden" id="typeList" name="typeList" value="${typeList }"/>
												<input type="hidden" id="pageIndex" name="pageIndex" value="${pageIndex }"/>
											</td>
											<td width="200">
												改签车次&nbsp;：
												<span>
													<input type="text" value="${cp.alter_train_no}" id="alter_train_no_${cp.cp_id}" name="alter_train_no_${cp.cp_id}" style="width:100px;" />
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
	            	<h4>用户退款</h4>
		                <div class="pub_con">
		                	<table class="pub_table">
		                		<!-- 
		                    	<tr>
		                        	<td class="pub_yuliu"></td>
		                        	<td colspan="2">车站小票：<span><img src="${refundTicketInfo.refund_purl }" alt="小票" 
		                        	width="300px"/></span></td>
		                        	<td></td>
		                        </tr>
		                         -->
		                        <c:if test="${refundTicketInfo.refund_status !=null && (refundTicketInfo.refund_status eq '00' || refundTicketInfo.refund_status eq '03' || refundTicketInfo.refund_status eq '07' || refundTicketInfo.refund_status eq '09' || refundTicketInfo.refund_status eq '99')}">
			                        <tr>
			                        	<td class="pub_yuliu"></td>
			                    		<td>12306账号：<span style="color: red; font-weight: bold;">${account }</span></td>	
			                    		<td>12306单号：<span>${orderInfo.out_ticket_billno }</span></td>
			                        </tr>
		                        </c:if>
		                        <tr>
		                        	<td class="pub_yuliu"></td>
		                        	<td>创建时间：<span>${refundTicketInfo.create_time}</span></td>
		                        	<td>实际退款时间：<span>${refundTicketInfo.refund_time}</span></td>
		                        </tr>
		                         <tr>
		                        	<td class="pub_yuliu"></td>
		                        	<td>退订金额：
			                        	<span>
				                        	<c:if test="${refundTicketInfo.refund_status !=null && (refundTicketInfo.refund_status eq '00' || refundTicketInfo.refund_status eq '03' || refundTicketInfo.refund_status eq '07' || refundTicketInfo.refund_status eq '09' || refundTicketInfo.refund_status eq '99') }">
				                        		<input type="text" name="refund_money" id="refund_money" value="${refundTicketInfo.refund_money }"/>
				                        	</c:if>
				                        	<c:if test="${fn:contains('11,22,33,44,55', refundTicketInfo.refund_status)  }">
				                        		${refundTicketInfo.refund_money }元
				                        	</c:if>（代理商）
			                        	</span>
		                        	</td>
		                        	<td>
		                        		<span style="color: red; font-weight: bold;">改签差价：&nbsp;
		                        			<c:if test="${refundTicketInfo.refund_status !=null && (refundTicketInfo.refund_status eq '00' || refundTicketInfo.refund_status eq '03' || refundTicketInfo.refund_status eq '07'|| refundTicketInfo.refund_status eq '09' || refundTicketInfo.refund_status eq '99')  }">
		                        				<input type="text" size="10" name="alter_tickets_money" id="alter_tickets_money" value="${refundTicketInfo.alter_tickets_money }"/>元
		                        			</c:if>
		                        			<c:if test="${fn:contains('11,22,33,44,55', refundTicketInfo.refund_status)  }">
				                        		${refundTicketInfo.alter_tickets_money }元
				                        	</c:if>
		                        		</span>
		                        	</td>
		                        </tr>
		                        <tr>
		                        	<td class="pub_yuliu"></td>
		                        	<td colspan="2">
		                        		<span style="color: red; font-weight: bold;">12306退款：
		                        			<c:if test="${refundTicketInfo.refund_status !=null && (refundTicketInfo.refund_status eq '00' || refundTicketInfo.refund_status eq '03' || refundTicketInfo.refund_status eq '07'|| refundTicketInfo.refund_status eq '09' || refundTicketInfo.refund_status eq '99')}">
		                        				<input type="text" size="10" name="actual_refund_money" id="actual_refund_money" value="${refundTicketInfo.actual_refund_money }"/>元
		                        			</c:if>
		                        			<c:if test="${fn:contains('11,22,33,44,55', refundTicketInfo.refund_status)  }">
				                        		${refundTicketInfo.actual_refund_money }元
				                        	</c:if>
		                        		</span>
	                       				<c:if test="${refundTicketInfo.refund_percent eq '20%' && refundTicketInfo.refund_money >=50}">
		                        			<span style="color:red;">※：此条退款若乘客未取纸质车票，需先改签后再退款 </span>
	                      			 	</c:if>
		                        	</td>
		                        </tr>
		                        <tr>
		                        	<td class="pub_yuliu">
		                        	</td>
		                       		<td colspan="2">计划退款期限：
		                        		<span>
			                        		<c:if test="${refundTicketInfo.refund_status !=null && (refundTicketInfo.refund_status eq '00' || refundTicketInfo.refund_status eq '03' || refundTicketInfo.refund_status eq '07'|| refundTicketInfo.refund_status eq '09' || refundTicketInfo.refund_status eq '99')}">
			                        			<label for="now">立即退款</label><input type="radio" name="refund_limit" value="1" id="now" checked="checked" />
			                        			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			                        			<label for="15">15天后退款 </label> <input type="radio" name="refund_limit" value="15" id="15" />
			                        		</c:if>
			                        		<c:if test="${fn:contains('11,22,33,44', refundTicketInfo.refund_status)  }">
			                        			${refundTicketInfo.refund_plan_time }
			                        		</c:if>
		                        		</span>
		                       		</td>
		                        </tr>
		                        <tr>
									<td class="pub_yuliu"></td>
									<td>用户备注：${refundTicketInfo.user_remark }</td>
									<td>退款状态：<span style="color:red;">${refund_statuses[refundTicketInfo.refund_status] }</span></td>
		                        </tr>
		                   <!--       <c:if test="${refundTicketInfo.refund_status !=null && (refundTicketInfo.refund_status eq '00' || refundTicketInfo.refund_status eq '99')}">
			                        <tr>
			                        	<td class="pub_yuliu"></td>
			                    		<td colspan="3">12306账号：<span style="color: red; font-weight: bold;">${account }</span></td>	
			                        </tr>
			                        <tr>
				                        <td class="pub_yuliu"></td>
			                    		<td colspan="3">
											12306单号：
											<span>${orderInfo.out_ticket_billno }</span>
										</td>
									</tr>
		                        </c:if>
		                       
		                        <tr>
		                        	<td class="pub_yuliu"></td>
		                        	<td>备注：
			                        	<span>
				                        	<c:if test="${refundTicketInfo.refund_status !=null && refundTicketInfo.refund_status eq '00'}">
				                        		<input type="text" name="our_remark" id="our_remark" value="${refundTicketInfo.our_remark }"/>
				                        	</c:if>
				                        	<c:if test="${fn:contains('11,22,33,44,55', refundTicketInfo.refund_status)  }">
				                        			${our_remark[refundTicketInfo.our_remark]}
				                        	</c:if>
			                        	</span>
		                        	</td>
		                        </tr>
		                         -->
		                        <tr><td class="pub_yuliu"></td>
		                        	<td>退款流水号：
			                        	<span>
			                       			 <c:if test="${refundTicketInfo.refund_status !=null && (refundTicketInfo.refund_status eq '00' || refundTicketInfo.refund_status eq '03' || refundTicketInfo.refund_status eq '07'|| refundTicketInfo.refund_status eq '09' || refundTicketInfo.refund_status eq '99')}">
			                       				 <input type="text" name="refund_12306_seq" id="refund_12306_seq"/>
			                       			 </c:if>
			                       			 <c:if test="${refundTicketInfo.refund_status!=null && (refundTicketInfo.refund_status != '03' && refundTicketInfo.refund_status != '07'&& refundTicketInfo.refund_status != '09' && refundTicketInfo.refund_status != '99')}">
			                       			 	<span>${refundTicketInfo.refund_12306_seq }</span>
			                       			 </c:if>
			                        	</span>
		                        	</td>
		                        	<td>
										改签明细：
										<span>
											<c:if test="${refundTicketInfo.refund_status !=null && (refundTicketInfo.refund_status eq '00' || refundTicketInfo.refund_status eq '03' || refundTicketInfo.refund_status eq '07'|| refundTicketInfo.refund_status eq '09' || refundTicketInfo.refund_status eq '99')}">
												<input type="text" name="change_ticket_info" id="change_ticket_info" value="${refundTicketInfo.change_ticket_info}" style="width: 130px;"/>
											</c:if>
											<c:if test="${fn:contains('11,22,33,44,55', refundTicketInfo.refund_status)}">
			                       				${refundTicketInfo.change_ticket_info}
			                       			</c:if>
										</span>
									</td>
		                        </tr>
		                        <tr ><td class="pub_yuliu"></td>
		                        <td colspan="3">备注：
			                        	<span>
			                        		<c:if test="${fn:contains('11,22,33,44,55', refundTicketInfo.refund_status)  }">
				                        			${refundTicketInfo.our_remark }
				                        	</c:if>
				                        	<c:if test="${(refundTicketInfo.refund_status eq '00' || refundTicketInfo.refund_status eq '03' || refundTicketInfo.refund_status eq '07' || refundTicketInfo.refund_status eq '09' || refundTicketInfo.refund_status eq '99')}">
												<select name="our_remark" style="width: 380px;" id="our_remark">
												    <option value="0" > </option>
													<option value="已无法在网上办理退票，请自行去车站办理" >已无法在网上办理退票，请自行去车站办理</option>
													<option value="请先到车站办理退票，然后填写用户备注为：已退票，再提交退款" >请先到车站办理退票，然后填写用户备注为：已退票，再提交退款。</option>
													<option value="已与铁道部核实，乘客未退票" >已与铁道部核实，乘客未退票</option>
													<option value="用户来电取消退票" >用户来电取消退票</option>
													<option value="其它" >其它</option>
												</select>
											</c:if>
										</span>
									</td> 
		                        </tr>
		                    </table>
		                </div>
		        	</div>
	        	</form>
        	</c:if>
        	<c:if test="${refundTicketInfo.refund_type != null && refundTicketInfo.refund_type eq '2' 
        		&& fn:contains('11,22,33,44', refundTicketInfo.refund_status)}">
	        	<div class="pub_self_take_mes oz mb10_all">
	           				<h4>差额退款</h4>
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
	               							<span>退款金额：
	               								${refundTicketInfo.refund_money}
	               							</span>
	               						</td>
	               						<td>
	               							退款状态：
	               							<span style="color:red;">
	               								${refund_statuses[refundTicketInfo.refund_status]}
	               							</span>
	               						</td>
	               					</tr>
	               				</table>
	               			</div>
	               		</div>
               		</c:if>
            <!-- 差额退款开始 -->
        	<c:if test="${refundTicketInfo.refund_type != null && refundTicketInfo.refund_type eq '2'
        	&& (refundTicketInfo.refund_status eq '00' || refundTicketInfo.refund_status eq '99') }">
        		<form action="/refundTicket/updateDifferRefund.do?statusList=${statusList }&typeList=${typeList }&pageIndex=${pageIndex } " method="post" name="differForm" id="differForm">
       				<div class="pub_self_take_mes oz mb10_all">
           				<h4>差额退款</h4>
           				<input type="hidden" name="stream_id" value="${stream_id }"/>
						<input type="hidden" name="order_id" value="${order_id }"/>
						<input type="hidden" name="cp_id" value="${cp_id }"/>
						<input type="hidden" name="refund_type" value="${refund_type }"/>
						<input type="hidden" name="create_time" value="${refundTicketInfo.create_time }"/>
						<input type="hidden" id="statusList" name="statusList" value="${statusList }"/>
						<input type="hidden" id="typeList" name="typeList" value="${typeList }"/>
						<input type="hidden" id="pageIndex" name="pageIndex" value="${pageIndex }"/>
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
               								${refund_statuses[refundTicketInfo.refund_status]}
               							</span>
               						</td>
               					</tr>
               					<tr>
               						<td class="pub_yuliu"><br /></td>
               						<td>
               							<span>差额退款：
               								<input type="text" name="refund_money" id="refund_money_id" value="${refundTicketInfo.refund_money}" />
               							</span>
               						</td>
               					</tr>
               				</table>
               			</div>
               		</div>
                </form>
        	</c:if>	
        	<c:if test="${refundTicketInfo.refund_type != null && refundTicketInfo.refund_type eq '8' 
        		&& fn:contains('11,22,33,44', refundTicketInfo.refund_status)}">
	        	<div class="pub_self_take_mes oz mb10_all">
	           				<h4>线下退款</h4>
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
	               							<span>退款金额：
	               								${refundTicketInfo.refund_money}
	               							</span>
	               						</td>
	               						<td>
	               							退款状态：
	               							<span style="color:red;">
	               								${refund_statuses[refundTicketInfo.refund_status]}
	               							</span>
	               						</td>
	               					</tr>
	               				</table>
	               			</div>
	               		</div>
               		</c:if>
            <!-- 线下退款开始 -->
        	<c:if test="${refundTicketInfo.refund_type != null && refundTicketInfo.refund_type eq '8'
        	&& (refundTicketInfo.refund_status eq '00' || refundTicketInfo.refund_status eq '99') }">
        		<form action="/refundTicket/updateXianXiaRefund.do?statusList=${statusList }&typeList=${typeList }&pageIndex=${pageIndex } " method="post" name="xianxiaForm" id="xianxiaForm">
       				<div class="pub_self_take_mes oz mb10_all">
           				<h4>线下退款</h4>
           				<input type="hidden" name="stream_id" value="${stream_id }"/>
						<input type="hidden" name="order_id" value="${order_id }"/>
						<input type="hidden" name="cp_id" value="${cp_id }"/>
						<input type="hidden" name="create_time" value="${refundTicketInfo.create_time }"/>
						<input type="hidden" id="statusList" name="statusList" value="${statusList }"/>
						<input type="hidden" id="typeList" name="typeList" value="${typeList }"/>
						<input type="hidden" id="pageIndex" name="pageIndex" value="${pageIndex }"/>
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
               								${refund_statuses[refundTicketInfo.refund_status]}
               							</span>
               						</td>
               					</tr>
               					<tr>
               						<td class="pub_yuliu"><br /></td>
               						<td>
               							<span>线下退款：
               								<input type="text" name="refund_money" id="refund_money_xx_id" value="${refundTicketInfo.refund_money}" />
               							</span>
               						</td>
               						<td>
			               				<span>12306退款：
			               					<input type="text" name="actual_refund_money" id="actual_refund_money_xx_id" value="${refundTicketInfo.actual_refund_money}" />
			               				</span>
			               			</td>
               					</tr>
               				</table>
               			</div>
               		</div>
                </form>
        	</c:if>	
            <c:if test="${refundTicketInfo.refund_type != null && refundTicketInfo.refund_type eq '3' 
        			&& (refundTicketInfo.refund_status eq '00' || refundTicketInfo.refund_status eq '99') }">
        		<form action="/refundTicket/updateOut_Ticket_Refund.do?statusList=${statusList }&typeList=${typeList }&pageIndex=${pageIndex }"  method="post" name="out_ticketForm" id="out_ticketForm">
       				<div class="pub_self_take_mes oz mb10_all">
           				<h4>出票失败退款</h4>
           				<input type="hidden" name="stream_id" value="${stream_id }"/>
						<input type="hidden" name="order_id" value="${order_id }"/>
						<input type="hidden" name="cp_id" value="${cp_id }"/>
						<input type="hidden" name="refund_type" value="${refund_type }"/>
						<input type="hidden" name="create_time" value="${refundTicketInfo.create_time }"/>
						<input type="hidden" id="statusList" name="statusList" value="${statusList }"/>
						<input type="hidden" id="typeList" name="typeList" value="${typeList }"/>
						<input type="hidden" id="pageIndex" name="pageIndex" value="${pageIndex }"/>
               			<div class="pub_con">
               				<table class="pub_table">
               					<tr>
               						<td class="pub_yuliu"><br /></td>
               						<td>
               								用户支付价格：
               							<span style="color:red;">
       										${refundTicketInfo.refund_money }
               							</span>
               						</td>
               						<td>
               							<span>退款金额：
               								<input type="text" name="refund_money" id="refund_money_OutTicket" value="${refundTicketInfo.refund_money}" />
               							</span>
               						</td>
               						<td>
               							退款状态：
               							<span style="color:red;">
               								${refund_statuses[refundTicketInfo.refund_status]}
               							</span>
               						</td>
               					</tr>
               				<c:if test="${refundTicketInfo.refund_status !=null && (refundTicketInfo.refund_status eq '00' || refundTicketInfo.refund_status eq '03' || refundTicketInfo.refund_status eq '07'|| refundTicketInfo.refund_status eq '09' || refundTicketInfo.refund_status eq '99')}">
                      			 <tr>
		                        	<td class="pub_yuliu"></td>
		                    		<td colspan="3">12306账号：<span style="color: red; font-weight: bold;">${account }</span></td>	
                       			 </tr>
                       		 </c:if>
               				</table>
               			</div>
               		</div>
                </form>
        	</c:if>	
        		<c:if test="${refundTicketInfo.refund_type != null &&  refundTicketInfo.refund_type eq '7'
        			&& (refundTicketInfo.refund_status eq '00' || refundTicketInfo.refund_status eq '99') }">
        		<form action="/refundTicket/updateCancel_Book_Refund.do?statusList=${statusList }&typeList=${typeList }&pageIndex=${pageIndex }" " method="post" name="out_ticketForm" id="out_ticketForm">
       				<div class="pub_self_take_mes oz mb10_all">
           				<h4>取消预约退款</h4>
           				<input type="hidden" name="stream_id" value="${stream_id }"/>
						<input type="hidden" name="order_id" value="${order_id }"/>
						<input type="hidden" name="cp_id" value="${cp_id }"/>
						<input type="hidden" name="create_time" value="${refundTicketInfo.create_time }"/>
						<input type="hidden" id="statusList" name="statusList" value="${statusList }"/>
						<input type="hidden" id="typeList" name="typeList" value="${typeList }"/>
						<input type="hidden" id="pageIndex" name="pageIndex" value="${pageIndex }"/>
               			<div class="pub_con">
               				<table class="pub_table">
               					<tr>
               						<td class="pub_yuliu"><br /></td>
               						<td>
               								用户支付价格：
               							<span style="color:red;">
       										${orderInfo.pay_money }
               							</span>
               						</td>
               						<td>
               							<span>退款金额：
               								<input type="text" name="refund_money" id="refund_money_OutTicket" value="${refundTicketInfo.refund_money}" />
               							</span>
               						</td>
               						<td>
               							退款状态：
               							<span style="color:red;">
               								${refund_statuses[refundTicketInfo.refund_status]}
               							</span>
               						</td>
               					</tr>
               				</table>
               			</div>
               		</div>
                </form>
        	</c:if>
        	<c:if test="${refundTicketInfo.refund_type != null && refundTicketInfo.refund_type eq '7'
        			&& fn:contains('11,22,33,44', refundTicketInfo.refund_status)}">
        		<div class="pub_self_take_mes oz mb10_all">
           				<h4>取消预约退款</h4>
           				<input type="hidden" name="stream_id" value="${stream_id }"/>
						<input type="hidden" name="order_id" value="${order_id }"/>
						<input type="hidden" name="cp_id" value="${cp_id }"/>
               			<div class="pub_con">
               				<table class="pub_table">
               					<tr>
               						<td class="pub_yuliu"><br /></td>
               						<td>
               								用户支付价格：
               							<span style="color:red;">
       										${orderInfo.pay_money }
               							</span>
               						</td>
               						<td>
               							<span>退款金额：
               								${refundTicketInfo.refund_money}
               							</span>
               						</td>
               						<td>
               							退款状态：
               							<span style="color:red;">
               								${refund_statuses[refundTicketInfo.refund_status]}
               							</span>
               						</td>
               					</tr>
               				</table>
               			</div>
               		</div>
        		</c:if>
        			
        	<c:if test="${refundTicketInfo.refund_type != null && refundTicketInfo.refund_type eq '3' 
        			&& fn:contains('11,22,33,44', refundTicketInfo.refund_status)}">
        		<div class="pub_self_take_mes oz mb10_all">
           				<h4>出票失败退款</h4>
           				<input type="hidden" name="stream_id" value="${stream_id }"/>
						<input type="hidden" name="order_id" value="${order_id }"/>
						<input type="hidden" name="cp_id" value="${cp_id }"/>
               			<div class="pub_con">
               				<table class="pub_table">
               					<tr>
               						<td class="pub_yuliu"><br /></td>
               						<td>
               								用户支付价格：
               							<span style="color:red;">
       										${refundTicketInfo.refund_money }
               							</span>
               						</td>
               						<td>
               							<span>退款金额：
               								${refundTicketInfo.refund_money}
               							</span>
               						</td>
               						<td>
               							退款状态：
               							<span style="color:red;">
               								${refund_statuses[refundTicketInfo.refund_status]}
               							</span>
               						</td>
               					</tr>
               				</table>
               			</div>
               		</div>
        		</c:if>
						<div class="pub_debook_mes  oz mb10_all">
							<p>
								
								<c:if test="${empty query_type && refundTicketInfo != null}">
									<c:if test="${(refundTicketInfo.refund_status eq '00' || refundTicketInfo.refund_status eq '03' || refundTicketInfo.refund_status eq '07') && refundTicketInfo.refund_type eq '1'}">
										<input type="button" value="退款" class="btn"
											onclick="sumbitRefund();" />
										<input type="button" value="拒绝退款" class="btn"
											onclick="refuseRefund();"
											style="font: bold 14px/ 35px '宋体'; color: #ff0;" />
										<input type="button" value="搁置订单" class="btn"
											onclick="sumbitGezhi('99');" />
									</c:if>
									<c:if test="${refundTicketInfo.refund_status eq '09' && refundTicketInfo.refund_type eq '1'}">
										<input type="button" value="退款完成" class="btn"
											onclick="sumbitRefund();" />
									</c:if>
									<c:if test="${refundTicketInfo.refund_status eq '99' && refundTicketInfo.refund_type eq '1'}">
										<input type="button" value="退款" class="btn"
											onclick="sumbitRefund();" />
										<input type="button" value="拒绝退款" class="btn"
											onclick="refuseRefund();"
											style="font: bold 14px/ 35px '宋体'; color: #ff0;" />
									</c:if>
									<c:if test="${refundTicketInfo.refund_status eq '00' && refundTicketInfo.refund_type eq '3' }">
				   		 				<input type="button" value="全额退款" class="btn" onclick="out_TicketRefund();"/>
				   					</c:if>
				   					<c:if test="${refundTicketInfo.refund_status eq '00' && refundTicketInfo.refund_type eq '7' }">
				   		 				<input type="button" value="取消预约退款" class="btn" onclick="cancel_BookRefund();"/>
				   					</c:if>
				   					<c:if test="${refundTicketInfo.refund_status eq '00' && refundTicketInfo.refund_type eq '2'}">
				   		 				<input type="button" value="差额退款" class="btn" onclick="differRefund();"/>
				   					</c:if>
				   					<c:if test="${refundTicketInfo.refund_status eq '00' && refundTicketInfo.refund_type eq '8'}">
				   		 				<input type="button" value="线下退款" class="btn" onclick="xianxiaRefund();"/>
				   					</c:if>
			   					</c:if>
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
									style="width: 650px; margin: 10px auto;">
									<tr>
										<th style="width: 50px;">
											NO
										</th>
										<th  style="width: 400px;">
											操作日志
										</th>
										<th  style="width: 100px;">
											操作时间
										</th>
										<th  style="width: 100px;">
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
