<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:if test="${lastestOrderList!=null && fn:length(lastestOrderList)>0}">
<div class="order_list mb10">
	<h2>
		<span class="nav_mid_bg"><strong><a href="/chunqiu/query/queryIndex.jhtml">更多 >></a></strong>最新订单</span>
	</h2>
	<ul class="oz">
	    <c:forEach items="${lastestOrderList}" var="order" varStatus="idx">
			<li>
				<ul class="oz">
					<li>
						<a href="/chunqiu/query/queryOrderDetail.jhtml?order_id=${order.order_id}&type=detail">${order.from_city}-${order.to_city}</a>
					</li>
					<li>
						<span>${order.travel_time}</span>车次：${order.train_no}
					</li>
				</ul>
			</li>
		</c:forEach>
	</ul>
</div>
</c:if>