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

<li <% if (isMyMenu(request, "book")) { %> class="current no_lborder" <% } %> ><a href="/chunqiu/buyTicket/bookIndex.jhtml">车票预订</a>
</li>
<li <% if (isMyMenu(request, "query")) { %> class="current no_lborder" <% } %> ><a href="/chunqiu/query/queryOrderList.jhtml">我的订单</a>
</li>
<li <% if (isMyMenu(request, "complain")) { %> class="current no_lborder" <% } %> ><a href="/chunqiu/complain/complainIndex.jhtml">投诉/建议</a>
</li>
<li <% if (isMyMenu(request, "help")) { %> class="current no_lborder" <% } %> ><a href="/pages/help/protocol.jsp">帮助</a>
</li>
	