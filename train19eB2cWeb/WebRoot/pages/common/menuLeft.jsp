<%@ page contentType="text/html; charset=UTF-8"%>
<%!
	String getParam(HttpServletRequest request, String param) {
		return request.getParameter(param) == null ? "" : request.getParameter(
				param).toString().trim();
	}

	boolean isMyMenu(HttpServletRequest request, String menuId) {
		if (menuId.equals(getParam(request, "menuId"))) {
			return true;
		} else {
			return false;
		}
	}
%>
<div class="left_nav">
	<ul>
		<li class="title">
			<span>我的订单</span>
		</li>
			<li class="con <% if (isMyMenu(request, "hcpOrder")) { %> current <%} %>">
				<a href="/query/queryOrderList.jhtml?type=hcp">火车票订单</a>
			</li>
		<li class="title">
			<span>个人中心</span>
		</li>
			<li class="con <% if (isMyMenu(request, "myInfo")) { %> current <%} %>">
				<a href="/login/myInfo.jhtml">我的信息</a>
			</li>
			<li class="con <% if (isMyMenu(request, "safePass")) { %> current <%} %>">
				<a href="/login/toUpdatePassword.jhtml">安全中心</a>
			</li>
		<li class="title">
			<span>常用信息</span>
		</li>
			<li class="con <% if (isMyMenu(request, "passInfo")) { %> current <%} %>">
				<a href="/login/queryPassenger.jhtml">常用旅客</a>
			</li>
			<li class="con <% if (isMyMenu(request, "postAddress")) { %> current <%} %>">
				<a href="/login/queryAddress.jhtml">邮寄地址</a>
			</li>
	</ul>
</div>