<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0, minimum-scale=1.0">
		<title>系统报错页</title>
		<link rel="stylesheet" href="/css/style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<style type="text/css">
			td.errorinfo{}
		</style>
	</head>

	<body>
		<div class="content oz">
			<!--导航条 start-->
			
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
							
							<td colspan="2">
								<input type="button" value="返回上页" class="btnBlue"
									onclick="javascript:history.back(-1);" />
								<span class="pl30"><input type="button" value="返回首页"
										class="btnWhite"
										onclick="javascript:window.location='/buyTicket/bookIndex.jhtml'" />
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
