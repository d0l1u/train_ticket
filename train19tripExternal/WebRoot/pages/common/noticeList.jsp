<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<h3><a href="/notice/queryNoticeAllList.jhtml">更多<b>>></b></a>公告</h3>
<ul>
	<c:forEach items="${noticeList}" var="item" varStatus="idx">
	<c:if test="${idx.index<3}">
    <li>
    	<span class="notice_time">
    	<fmt:formatDate value="${item.pub_time}" pattern="yyyy/MM/dd" />
    	</span>
    	<span class="list">
    	<a href="/notice/queryNoticeInfo_no.jhtml?noticeId=${item.notice_id}"
    			title="${item.notice_name}">${item.notice_name}</a>
        </span>
    </li>
    </c:if>
    </c:forEach>
</ul>