<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>抢票查询列表页</title>
<link rel="stylesheet" href="/css/style.css" type="text/css" />
<link rel="stylesheet" href="/css/default.css" type="text/css" />
<script type="text/javascript" src="/js/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/js/artDialog.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/print.js"></script>
<style >
.inlinetxt{overflow:hidden;text-overflow:ellipsis;white-space: nowrap;}
</style>
</head>

<body>
	<div class="content oz">
		<div class="index_all">
			<!--左边内容 start-->
			<jsp:include flush="true" page="/pages/common/menu.jsp">
				<jsp:param name="menuId" value="query" />
			</jsp:include>
			<!--左边内容 end-->
			<!--右边内容 start-->
			<div class="infoinput-right oz">
				<div class="query_type oz">
					<ul class="query_type_ul" id="robOrderTab">
						<li
							class="<c:if test="${selectType eq '0' or empty selectType}">current</c:if> current1">
							<a href="javascript:void(0);" onclick="robOrderList(0);">全部订单</a>
						</li>
						<li
							class="<c:if test="${selectType eq '1'}">current</c:if> current1">
							<a href="javascript:void(0);" onclick="robOrderList(1);">待支付(${status['noPayNum']})</a>
						</li>
						<li
							class="<c:if test="${selectType eq '2'}">current</c:if> current1">
							<a href="javascript:void(0);" onclick="robOrderList(2);">出票中(${status['waitingNum']})</a>
						</li>
						<li
							class="<c:if test="${selectType eq '3'}">current</c:if> current1">
							<a href="javascript:void(0);" onclick="robOrderList(3);">出票成功(${status['successNum']})</a>
						</li>
						<li
							class="<c:if test="${selectType eq '4'}">current</c:if> current1">
							<a href="javascript:void(0);" onclick="robOrderList(4);">出票失败(${status['failNum']})</a>
						</li>
						<li
							class="<c:if test="${selectType eq '5'}">current</c:if> current1">
							<a href="javascript:void(0);" onclick="robOrderList(5);">退款结果(${status['refundNum']})</a>
						</li>
					</ul>
				</div>
				<form action="/rob/robOrderList.jhtml" method="post" id="robForm">
					<input type="hidden" name="selectType" id="selectType" value="" />
					<p style="margin-top: 20px;">
						<span> <select style="width: 110px;" name="oneMonthOrder"
							id="oneMonthOrder">
								<option value="0"
									<c:if test="${oneMonthOrder==0}">selected</c:if>>
									一个月内的订单&nbsp;&nbsp;&nbsp;&nbsp;</option>
								<option value="1"
									<c:if test="${oneMonthOrder==1}"> selected</c:if>>
									一个月前的订单&nbsp;&nbsp;&nbsp;&nbsp;</option>
						</select>
						</span> <span style="margin-left: 5px;">按订票日期查：<input
							style="width: 105px;" class="text text2" type="text"
							id="create_time" name="createTime" value="${createTime}"
							readonly="readonly"
							onfocus="WdatePicker({doubleCalendar: false ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
						</span> <span style="margin-left: 5px;">按订单号查：<input
							style="width: 105px;" type="text" name="orderId"
							value="${orderId}" onkeyup=value = value.replace(/(^\s*)|(\s*$)/g, '');;
/>
						</span> <span style="margin-left: 5px;">按取票单号查：<input
							style="width: 105px;" type="text" name="outTicketBillno"
							value="${outTicketBillno}" onkeyup=value = value.replace(/(^\s*)|(\s*$)/g, '');;
/>
						</span> <span style="margin-left: 0px;"> <input type="submit"
							class="btn search_order_btn" value="查询" />
						</span>
					</p>
					<!--订单号查询模块 end-->
					<!-- 页面显示模块，0全部订单显示 -->

					<div class="myorder oz">
						<input type="hidden" name="selectType" value="0" />
						<table>
							<tr style="background: #EAEAEA;">
								<th class="inlinetxt">序号</th>
								<th class="inlinetxt">订单号</th>
								<th class="inlinetxt">出发-到达</th>
								<th class="inlinetxt">支付金额</th>
								<th class="inlinetxt">支付时间</th>
								<th class="inlinetxt">出票时间</th>
								<th class="inlinetxt">退款时间</th>
								<th class="inlinetxt">订单类型</th>
								<th class="inlinetxt">订单状态</th>
								<th class="inlinetxt">退款状态</th>
								<th class="inlinetxt">操作</th>
							</tr>
							<c:forEach items="${orderList}" var="order" varStatus="idx">
								<tr>
									<td class="inlinetxt">${idx.index+1}</td>
									<td class="inlinetxt">${order.orderId}</td>
									<td class="inlinetxt">${order.fromCity}/${order.toCity}</td>
									<td class="inlinetxt">${order.payMoney}</td>
									<td >
										<p style="line-height: 22px; margin: 0;">
											<fmt:formatDate value="${order.payTime}" type="both" pattern="yyyy-MM-dd HH:mm"/>
										</p>
									</td>
									<td >
										<p style="line-height: 22px; margin: 0;">
										<fmt:formatDate value="${order.outTicketTime}"
												type="both" pattern="yyyy-MM-dd HH:mm"/>
										</p>
									</td>
									<td >
										<p style="line-height: 22px; margin: 0;">
										 <fmt:formatDate value="${order.refundTime}"
											type="both" pattern="yyyy-MM-dd HH:mm"/>
										</p>
									</td>
									<td class="inlinetxt">抢票</td>
									<td class="inlinetxt"><c:if test="${order.orderStatus eq '00'}">未支付</c:if> <c:if
											test="${order.orderStatus ne '00'}">${orderStatusMap[order.orderStatus]}</c:if>
									</td>
									<td class="inlinetxt">
										<c:if test="${order.orderStatus eq '71'}">退票成功</c:if>
										<c:if test="${order.orderStatus eq '70'}">退票中</c:if>
										<c:if test="${order.orderStatus eq '72'}">退票失败</c:if>
									</td>
									<td class="inlinetxt">
										<p style="line-height: 22px; margin: 0;">
											<a style="text-decoration: none;"
												href="/rob/queryOrderDetail.jhtml?orderId=${order.orderId}"><font
												style="color: #0d77c1;">明细</font> </a>
											<c:if test="${order.orderStatus eq '00'}">
												<a style="text-decoration: none;" href="#"
													onclick="javascript:deleteOrder('${order.orderId}','${selectType }');"><font
													style="color: #0d77c1;">删除</font> </a>
												<a style="text-decoration: none;" href="#"
													onclick="javascript:pay('${order.orderId}','${selectType }');"><font
													style="color: #0d77c1;">重新支付</font> </a>
											</c:if>
										</p> <!-- 出票成功后可以退款 --> <c:if test="${order.orderStatus eq '99' or order.orderStatus eq '70' or order.orderStatus eq '71' }">
											<p style="line-height: 22px; margin: 0;">
												<a style="text-decoration: none;"
													href="/rob/refundDetail.jhtml?orderId=${order.orderId}"><font
													style="color: #0d77c1;">申请退票</font> </a>
											</p>
										</c:if> <!-- 用户已经支付,但是没有 成功出票 都可以取消 --> <c:if
											test="${order.orderStatus eq '88'}">
											<p style="line-height: 22px; margin: 0;">
												<a style="text-decoration: none;"
													href="/rob/cancel.jhtml?orderId=${order.orderId}"><font
													style="color: #0d77c1;">取消抢票</font> </a>
											</p>
										</c:if>
									</td>
								</tr>
							</c:forEach>
						</table>
						<c:if test="${isShowList == 1}">
							<jsp:include page="/pages/common/paging.jsp" />
						</c:if>
					</div>
				</form>
			</div>
			<!--右边内容 end-->
		</div>
	</div>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#oneMonthOrder").focus();
		});
		function rePayOrder(orderId) {
			$.ajax({
				url : "/rob/orderRepay.jhtml?orderId=" + orderId,
				type : "POST",
				cache : false,
				success : function(res) {
					if (res == 'success') {
						window.location = '/rob/orderComfirm.jhtml?orderId='
								+ orderId;
					} else {
						var dialog = art.dialog({
							lock : true,
							fixed : true,
							left : '50%',
							top : '40%',
							title : '提示',
							okVal : '确认',
							icon : "/images/warning.png",
							content : '对不起，您所选的车次已无票，请您重新购票！',
							ok : function() {
							}
						});
						return false;
					}
				}
			});
		}

		function deleteOrder(order_id, selectType) {
			var dialog = art
					.dialog({
						lock : true,
						fixed : true,
						left : '50%',
						top : '20%',
						title : '提示',
						okVal : '确认',
						icon : "/images/warning.png",
						content : '删除后无法恢复，依然删除该订单？',
						ok : function() {
							$
									.get(
											"/rob/deleteOrder.jhtml?orderId="
													+ order_id,
											function(data) {
												if (data == "success") {
													window.location = '/rob/robOrderList.jhtml?selectType='
															+ selectType;
												} else {
													alert("删除失败，请联系客服！");
												}
											});
						}
					});
		}

		function robOrderList(index) {
			$("#selectType").val(index);
			$("#robForm").submit();
		}

		// 重新支付
		function pay(orderId, selectType) {
			window.location = "/rob/payAgain.jhtml?orderId=" + orderId;
		}
	</script>

</body>
</html>
