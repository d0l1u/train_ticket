<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>系统报错页</title>
		<link rel="stylesheet" href="/css/style.css" type="text/css" />
		<!-- 自适应高度 -->
		<script type="text/javascript" src="/js/trendsHeight.js"></script>
		<!-- 自适应高度 -->
		<script type="text/javascript" src="/js/jquery.js"></script>
	</head>

	<body>
		<div class="content oz">
			<!--导航条 start-->
			<div class="main_nav">
		    	<ul class="oz">
		    	    <jsp:include flush="true" page="/pages/common/menu.jsp">
						<jsp:param name="menuId" value="query" />
					</jsp:include>
		        </ul>
		        <div class="slogan"></div>
		    </div>
		    <!--导航条 end-->
			<!--左边内容 start-->
			<div class="left_con oz">
				<div class="globalWarningWrap">
					<table border="0" align="center" cellpadding="0" cellspacing="10">
						<tr>
							<td>
								<img src="/images/iconWrong1.gif" />
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
								<input type="button" value="返回上页" class="btnBlue"
									onclick="javascript:history.back(-1);" />
								<span class="pl30"><input type="button" value="返回首页"
										class="btnWhite"
										onclick="javascript:window.location='/chunqiu/buyTicket/bookIndex.jhtml'" />
								</span>
							</td>
						</tr>
					</table>
				</div>
				<div style="display:none;">  
					<c:out value="${showErr}"></c:out>  
				</div>  

			</div>

	</body>
</html>
