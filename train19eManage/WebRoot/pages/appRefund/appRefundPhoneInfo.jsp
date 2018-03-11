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

	function sumbitRefund(){
		var str = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
		if($.trim($("#refund_money").val())==""){
			$("#refund_money").focus();
			alert("退款金额不能为空！");
			return;
		}
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
	function refuseRefund(){
		if($.trim($("#our_remark").val())==""){
			$("#our_remark").focus();
			alert("退款备注不能为空！");
			return;
		}
		if(confirm("确认拒绝退款？并且在备注中说明拒绝原因。")){
		
			$("#refundForm").attr("action","/appRefund/updateRefuseRefund.do");
			$("#refundForm").submit();
			
		}
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
		if(confirm("确认差额退款？并且核对退款金额。")){		
			$("#differForm").submit();
		}
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
		if(confirm("确认全额退款？并且核对退款金额。")){		
			$("#out_ticketForm").submit();
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
						退款明细
					</h4>
					<c:if test="${orderInfo.refund_status eq '11'}">
					<div class="pub_con">
						<table class="pub_table">
							<tr>
								<td class="pub_yuliu" rowspan="12"></td>
								<td>创建时间：</td><td><span>${orderInfo.create_time }</span></td>
							</tr>
							<tr>
								<td>退款类型：</td><td><span>${refund_types[orderInfo.refund_type] }</span></td>
							</tr>
							<tr>
								<td>订 单 号：</td><td><span>${orderInfo.order_id}</span></td>
							</tr>
							<tr>
								<td>订单状态：</td><td><span>${refund_statuses[orderInfo.refund_status] }</span></td>
							</tr>
							<tr>
								<td>12306退款流水号：</td><td><span>${orderInfo.refund_12306_seq}</span></td>
							</tr>
							<tr>
								<td>12306退款金额：</td><td><span>${orderInfo.actual_refund_money }</span></td>
							</tr>
							<tr>
								<td>改签差价：</td><td><span>${orderInfo.alter_tickets_money}</span></td>
							</tr>
							<tr>
								<td>退款金额(代理商)：</td><td><span>${orderInfo.refund_money}</span></td>
							</tr>
							<tr>
								<td>银行账户姓名：</td><td><span>${orderInfo.bank_username}</span></td>
							</tr>
							<tr>
								<td>乘客银行类型：</td><td><span>${orderInfo.bank_type}</span></td>
							</tr>
							<tr>
								<td>乘客银行账号：</td><td><span>${orderInfo.bank_account}</span></td>
							</tr>
							<tr>
								<td>开户行名称：</td><td><span>${orderInfo.bank_openName}</span></td>
							</tr>
						</table>
					</div>
					</c:if>
					<c:if test="${orderInfo.refund_status eq '22'}">
					<div class="pub_con">
						<table class="pub_table">
							<tr>
								<td class="pub_yuliu" rowspan="6"></td>
								<td>创建时间：</td>
								<td>${orderInfo.create_time }</td>
							</tr>
							<tr>
								<td>退款类型：</td>
								<td>${refund_types[orderInfo.refund_type] }</td>
							</tr>
							<tr>
								<td>订 单 号：</td>
								<td>${orderInfo.order_id}</td>
							</tr>
							<tr>
								<td>订单状态：</td>
								<td>${refund_statuses[orderInfo.refund_status] }</td>
							</tr>
							<tr>
								<td>拒绝退款原因：</td>
								<td>${orderInfo.our_remark}</td>
							</tr>
						</table>
					</div>
					</c:if>
			</div>
			<div class="pub_debook_mes  oz mb10_all">
				<p>
				<c:if test="${orderInfo.refund_status eq '22'}">
					<input type="button" value="修改" class="btn" onclick="location.href='/appRefund/toAddPhoneRefundTicket.do?order_id=${orderInfo.order_id}&refund_type=${orderInfo.refund_type}&refund=1'" />
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
			</div>
	</body>
</html>
