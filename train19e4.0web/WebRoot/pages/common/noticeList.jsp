<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div id="new_ggao">
		<h2>
			<span>公告</span>
			<a href="/notice/queryNoticeAllList.jhtml">更多
			</a>
		</h2>
		<div class=nggao_con>
			<ul style="padding-right: 8px;">
				<c:forEach items="${noticeList}" var="item" varStatus="idx">
				<c:if test="${idx.index<5}">
    				<li>
    				<span style="width:145px; display:block; white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">
        		<a title="${item.notice_name}" href="/notice/queryNoticeInfo_no.jhtml?noticeId=${item.notice_id}">
        	${item.notice_name}
        		</a>
        		</span>
				<span class="notice_time">
    			<fmt:formatDate value="${item.pub_time}" pattern="yyyy/MM/dd" />
    			</span>
    				</li>
    </c:if>
    </c:forEach>
</ul>
</div>
</div>