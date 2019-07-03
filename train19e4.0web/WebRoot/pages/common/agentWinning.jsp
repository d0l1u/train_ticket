<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


	<div class="buy_info oz" id="winning_in">
		<h2 class="nav_mid_bg">
			<span class=nav_mid_bgin>中奖信息</span>
		</h2>
		<jsp:useBean id="now" class="java.util.Date" />
		<marquee direction="up" scrollamount="2" height="250" onmouseover="stop();" onmouseout="start();">
			<ul class="oz" style="padding-right: 5px;padding-left: 5px;">
			<!--0、金牌；1、银牌；2、VIP；3、SVIP -->
			<c:if test="${agentWinningList0!=null && fn:length(agentWinningList0)>0}">
			<li><span style="color:#333;text-size:30px;line-height:24px;">金牌用户中奖名单：</span></li>
			<c:forEach items="${agentWinningList0}" var="winning" varStatus="idx">
			 <c:if test="${winning.agent_type eq 0 }">
			 	<div class="add_style">
			 		<c:if test="${idx.index le 4}">
						<c:if test="${winning.agent_type eq 0 }">
							<li class="new"><s><img src="/images/icon_new.gif" width="23" height="15" alt="" /></s>金牌用户：${winning.dealer_name}</li>
						</c:if>
						
					</c:if>
					<c:if test="${idx.index gt 4}">
						<c:if test="${winning.agent_type eq 0 }">
							<li >金牌用户：${winning.dealer_name}</li>
						</c:if>
					</c:if>
					<li>
						免费订购：${winning.from_city}-${winning.to_city}
					</li>
					<li>
					<c:if test="${winning.seat_type eq 0}">商务座</c:if>
					<c:if test="${winning.seat_type eq 1}">特等座</c:if>
					<c:if test="${winning.seat_type eq 2}">一等座</c:if>
					<c:if test="${winning.seat_type eq 3}">二等座</c:if>
					<c:if test="${winning.seat_type eq 4}">高级软卧</c:if>
					<c:if test="${winning.seat_type eq 5}">软卧</c:if>
					<c:if test="${winning.seat_type eq 6}">硬卧</c:if>
					<c:if test="${winning.seat_type eq 7}">软座</c:if>
					<c:if test="${winning.seat_type eq 8}">硬座</c:if>
					<c:if test="${winning.seat_type eq 9}">无座</c:if>
					<c:if test="${winning.seat_type eq 10}">其他</c:if>
						一张&nbsp;&nbsp;&nbsp;车次：${winning.train_no}
					</li>
					<li>
						下单时间:${winning.order_time}￥：<fmt:formatNumber value="${winning.winning_money}" type="currency" pattern="#0.0" />元
					</li>
					</div>
			</c:if>
			</c:forEach>
			</c:if>
			<c:if test="${agentWinningList1!=null && fn:length(agentWinningList1)>0}">
			<li><span style="color:#333;text-size:30px;line-height:24px;">银牌用户中奖名单：</span></li>
			<c:forEach items="${agentWinningList1}" var="winning" varStatus="idx">
			 <c:if test="${winning.agent_type eq 1 }">
			 	<div class="add_style">
			 		<c:if test="${idx.index le 4}">
						<c:if test="${winning.agent_type eq 1 }">
							<li class="new"><s><img src="/images/icon_new.gif" width="23" height="15" alt="" /></s>银牌用户：${winning.dealer_name}</li>
						</c:if>
						
					</c:if>
					<c:if test="${idx.index gt 4}">
						<c:if test="${winning.agent_type eq 1 }">
							<li >银牌用户：${winning.dealer_name}</li>
						</c:if>
					</c:if>
					<li>
						免费订购：${winning.from_city}-${winning.to_city}
					</li>
					<li>
					<c:if test="${winning.seat_type eq 0}">商务座</c:if>
					<c:if test="${winning.seat_type eq 1}">特等座</c:if>
					<c:if test="${winning.seat_type eq 2}">一等座</c:if>
					<c:if test="${winning.seat_type eq 3}">二等座</c:if>
					<c:if test="${winning.seat_type eq 4}">高级软卧</c:if>
					<c:if test="${winning.seat_type eq 5}">软卧</c:if>
					<c:if test="${winning.seat_type eq 6}">硬卧</c:if>
					<c:if test="${winning.seat_type eq 7}">软座</c:if>
					<c:if test="${winning.seat_type eq 8}">硬座</c:if>
					<c:if test="${winning.seat_type eq 9}">无座</c:if>
					<c:if test="${winning.seat_type eq 10}">其他</c:if>
						一张&nbsp;&nbsp;&nbsp;车次：${winning.train_no}
					</li>
					<li>
						下单时间:${winning.order_time}￥：<fmt:formatNumber value="${winning.winning_money}" type="currency" pattern="#0.0" />元
					</li>
					</div>
			</c:if>
			</c:forEach>
			</c:if>
			<c:if test="${agentWinningList2!=null && fn:length(agentWinningList2)>0}">
			<li><span style="color:#333;text-size:30px;line-height:24px;">VIP、SVIP用户中奖名单：</span></li>
			 <c:forEach items="${agentWinningList2}" var="winning" varStatus="idx">
			 <c:if test="${winning.agent_type eq 2 ||winning.agent_type eq 3 }">
			 	<div class="add_style">
						<c:if test="${winning.agent_type eq 2 }">
							<li class="new"><s><img src="/images/icon_new.gif" width="23" height="15" alt="" /></s>VIP用户：${winning.dealer_name}</li>
						</c:if>
						<c:if test="${winning.agent_type eq 3 }">
							<li class="new"><s><img src="/images/icon_new.gif" width="23" height="15" alt="" /></s>SVIP用户：${winning.dealer_name}</li>
						</c:if>
					<li>
						免费订购：${winning.from_city}-${winning.to_city}
					</li>
					<li>
					<c:if test="${winning.seat_type eq 0}">商务座</c:if>
					<c:if test="${winning.seat_type eq 1}">特等座</c:if>
					<c:if test="${winning.seat_type eq 2}">一等座</c:if>
					<c:if test="${winning.seat_type eq 3}">二等座</c:if>
					<c:if test="${winning.seat_type eq 4}">高级软卧</c:if>
					<c:if test="${winning.seat_type eq 5}">软卧</c:if>
					<c:if test="${winning.seat_type eq 6}">硬卧</c:if>
					<c:if test="${winning.seat_type eq 7}">软座</c:if>
					<c:if test="${winning.seat_type eq 8}">硬座</c:if>
					<c:if test="${winning.seat_type eq 9}">无座</c:if>
					<c:if test="${winning.seat_type eq 10}">其他</c:if>
						一张&nbsp;&nbsp;&nbsp;车次：${winning.train_no}
					</li>
					<li>
						下单时间:${winning.order_time}￥：<fmt:formatNumber value="${winning.winning_money}" type="currency" pattern="#0.0" />元
					</li>
					</div>
			</c:if>
			</c:forEach>
			</c:if>
			</ul>
		</marquee>
	</div>

