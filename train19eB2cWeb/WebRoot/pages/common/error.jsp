<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>19trip旅行</title>
		<link rel="stylesheet" href="/css/default.css" type="text/css" />
		<link rel="stylesheet" href="/css/reset-min.css" type="text/css" />
		<link rel="stylesheet" href="/css/travel.css" type="text/css" />
		<link rel="stylesheet" href="/css/style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	</head>

	<body>
		<!--以下是头部logo部分start -->
		<jsp:include flush="true" page="/pages/common/headerNav.jsp">
			<jsp:param name="menuId" value="lx" />
		</jsp:include>
		<!--以下是头部logo部分end -->
		
			<!--右边内容 start-->
				<div class="globalWarningWrap" style="height:480px;">
				<br/><br/><br/>
					<table border="0" align="center" cellpadding="0" cellspacing="10">
						<tr>
							<td>
								<img src="/images/hellomonkey.jpg" />
							</td>
							<td>
								<h3 class="lightRed">
									<c:out value="${errMsg}" default="很抱歉，系统发生错误，请重试！" escapeXml="false"></c:out>
								</h3>
							</td>
						</tr>
						<tr>
							<td>
								&nbsp;
							</td>
							<td>
								<input type="button" value="返回上页" class="btn13"
									onclick="javascript:history.back(-1);" />
								<span class="pl30"><input type="button" value="返回首页"
										class="btn13"
										onclick="javascript:window.location='/buyTicket/bookIndex.jhtml'" />
								</span>
							</td>
						</tr>
					</table>
				</div>
				<div style="display:none;">  
					<c:out value="${showErr}"></c:out>  
				</div>  

<!-- footer start -->
<%@ include file="/pages/common/footer.jsp"%>
<!-- footer end -->
	</body>
</html>
