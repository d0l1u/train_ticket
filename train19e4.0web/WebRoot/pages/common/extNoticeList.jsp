<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<script type="text/javascript">
	function shouTitle(notice_id){
		//alert(notice_id);
		var obj=document.getElementById(notice_id);
		var mytitle=obj.innerHTML;
		mytitle = mytitle.toLowerCase(); 
		mytitle = mytitle.replace('<b style="color: red">', '');
		mytitle = mytitle.replace('</b>','');
		//alert("title:"+mytitle);		
		obj.title = mytitle;
	}
</script>

<div id="new_ggao">
		<h2>
			<span>公告</span>
			<a href="/extShiji/queryNoticeAllList.jhtml">更多
			</a>
		</h2>
		<div class=nggao_con>
<ul>
	<c:forEach items="${noticeList}" var="item" varStatus="idx">
	<c:if test="${idx.index<3}">
    <li>
    	<span style="width:145px; display:block; white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">
        <a href="/extShiji/queryNoticeInfo_no.jhtml?noticeId=${item.notice_id}" id="${item.notice_id}" onmouseover="shouTitle('${item.notice_id}');" >
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