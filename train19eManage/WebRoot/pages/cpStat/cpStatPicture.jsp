<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="java.util.*"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>统计图表</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	   	<script type="text/javascript">
	   		
	   	</script>

  	</head>
	<body>
		<table>
			<tr>
				<td>
					<p align="center">
						<c:if test="${status eq '15dayCpStat'}">
							<span> 
								<img src="/jchart/jfreePictureCpStat${date15 }.jpg" border=0 usemap="#imgMap" /> 
							</span>
						</c:if>
					</p>
				</td>
			</tr>
		</table>
		<div class="pub_debook_mes  oz mb10_all">
			<input type="button" value="返 回" class="btn btn_normal"
				onclick="javascript:history.back(-1);" />
		</div>
	</body>
</html>
