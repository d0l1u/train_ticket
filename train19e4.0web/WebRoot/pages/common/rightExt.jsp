<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<div class="right_con oz">
	<!-- 对外商户公告 -->
	<jsp:include flush="true" page="/extShiji/queryNoticeList.jhtml">
		<jsp:param name="" value=""/>
	</jsp:include>

	<!-- 业务常识 -->
	<div class="exper mb10">
		<h2>
			<span class="nav_left_bg"></span>
			<span class="nav_mid_bg">业务常识</span>
			<span class="nav_right_bg"></span>
		</h2>
		<ul class="oz">
        	<li><a href="/pages/guide/extTuiPiaoGuiDing.jsp">退票规定</a></li>
	        <li><a href="/pages/guide/extGaiQianGuiDing.jsp">改签规定</a></li>
	        <li><a href="/pages/guide/extTrainYesNo.jsp">火车票真伪识别</a></li>
		</ul>
	</div>
	
	
</div>