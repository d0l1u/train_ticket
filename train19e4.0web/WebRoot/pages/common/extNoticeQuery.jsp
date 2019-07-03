<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>火车票公告页</title>
<link rel="stylesheet" href="/css/style.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
</head>

<body>
	<div class="content shouy oz">
		<div class="index_all">
		<!--左边内容 start-->
			<jsp:include flush="true" page="/pages/common/menu.jsp">
					<jsp:param name="menuId" value="extquery" />
				</jsp:include>
		<!--左边内容 end-->
    	<!--右边内容 start-->
    	<div class="infoinput-right oz">
            <div class="bread_nav mb10">
                <span class="back"><a href="javascript:history.back(-1);">返回</a></span>您所在的位置：<a href="/extShiji/queryExtShijiOrderList.jhtml">首页</a> > <span>最新公告</span>
            </div>
            <div class="notice_list" style="BORDER-TOP: 0px solid;">
                <h2 class="advice_h2"><span style="color:black;FLOAT: right;FONT: bold 12px/30px arial,Verdana,'Simsun';PADDING-RIGHT: 10px;">发表时间</span><span>标题</span></h2>
				<div class="con1"  style="BORDER-TOP: #32B1F0 2px solid;">
				<ul class="con_list oz">
					<c:forEach items="${noticeList}" var="item" varStatus="idx">
						<c:if test="${item.is_new eq '1'}">
	                		<li><a href="/extShiji/queryNoticeInfo_no.jhtml?noticeId=${item.notice_id}" class="new">${item.notice_name}<s><img src="/images/icon_new.gif" width="26" height="15" alt="" /></s></a><span><fmt:formatDate value="${item.pub_time}" pattern="yyyy-MM-dd" /></span></li>
	                	</c:if>
	                	<c:if test="${item.is_new eq '0'}">
	                    	<li><a href="/extShiji/queryNoticeInfo_no.jhtml?noticeId=${item.notice_id}">${item.notice_name}</a><span><fmt:formatDate value="${item.pub_time}" pattern="yyyy-MM-dd" /></span></li>
	                    </c:if>
					</c:forEach>
                </ul>
                </div>
            </div>
        </div>
        
        <!--右边内容 end-->
        
    </div>
	</div>
</body>
</html>
