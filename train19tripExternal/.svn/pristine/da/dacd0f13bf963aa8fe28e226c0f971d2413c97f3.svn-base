<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

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
<%@include file="/pages/common/height.jsp"%>

<c:choose>
	<c:when test="${menuDisplay eq 'all'}">
		<li <% if (isMyMenu(request, "book")) { %> class="current no_lborder" <% } %> >
			<a href="/buyTicket/bookIndex.jhtml">车票预订</a>
		</li>
		<li <% if (isMyMenu(request, "join")) { %> class="current no_lborder" <% } %> >
			<a href="/joinUs/joinIndex.jhtml">我要开通</a>
		</li>
		<li <% if (isMyMenu(request, "query")) { %> class="current no_lborder" <% } %> >
			<a href="/query/queryOrderList.jhtml">订单/退款</a>
		</li>
		<li <% if (isMyMenu(request, "complain")) { %> class="current no_lborder" <% } %> >
			<a href="/complain/complainIndex.jhtml">投诉/建议</a>
		</li>
		<li <% if (isMyMenu(request, "help")) { %> class="current no_lborder" <% } %> >
			<a href="/pages/help/protocol.jsp">业务介绍</a>
		</li>
		<li <% if (isMyMenu(request, "guide")) { %> class="current no_lborder" <% } %> >
			<a href="/pages/guide/business.jsp">帮助指南</a>
		</li>
	</c:when>
	<c:when test="${menuDisplay eq 'bookAndJoin'}">
		<li <% if (isMyMenu(request, "book")) { %> class="current no_lborder" <% } %> >
			<a href="/buyTicket/bookIndex.jhtml">车票预订</a>
		</li>
		<li <% if (isMyMenu(request, "join")) { %> class="current no_lborder" <% } %> >
			<a href="/joinUs/joinIndex.jhtml">我要开通</a>
		</li>
		<li <% if (isMyMenu(request, "complain")) { %> class="current no_lborder" <% } %> >
			<a href="/complain/complainIndex.jhtml">投诉/建议</a>
		</li>
		<li <% if (isMyMenu(request, "help")) { %> class="current no_lborder" <% } %> >
			<a href="/pages/help/protocol.jsp">业务介绍</a>
		</li>
		<li <% if (isMyMenu(request, "guide")) { %> class="current no_lborder" <% } %> >
			<a href="/pages/guide/business.jsp">帮助指南</a>
		</li>
	</c:when>
	<c:when test="${menuDisplay eq 'bookOnly'}">
		<li <% if (isMyMenu(request, "book")) { %> class="current no_lborder" <% } %> >
			<a href="/buyTicket/bookIndex.jhtml">车票预订</a>
		</li>
		<li <% if (isMyMenu(request, "complain")) { %> class="current no_lborder" <% } %> >
			<a href="/complain/complainIndex.jhtml">投诉/建议</a>
		</li>
		<li <% if (isMyMenu(request, "help")) { %> class="current no_lborder" <% } %> >
			<a href="/pages/help/protocol.jsp">业务介绍</a>
		</li>
		<li <% if (isMyMenu(request, "guide")) { %> class="current no_lborder" <% } %> >
			<a href="/pages/guide/business.jsp">帮助指南</a>
		</li>
	</c:when>
</c:choose>