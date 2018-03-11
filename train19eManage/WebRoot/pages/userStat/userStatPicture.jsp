<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>图表展示页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	    <script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
	    <script type="text/javascript" src="/js/FusionCharts.js"></script>
	    <script type="text/javascript" src="/js/FusionChartsExportComponent.js"></script>
  	</head>
	<body>
		<div style="padding:20px">
			<br />
			<table style="border:none;">
				<tr  style="border:none;">
				<td>
					<p align="center">
						<c:if test="${status eq '15dayUserStat'}">
							<div id="15dayUserStat">
							<% String strXML1 = (String)request.getAttribute("strXML1");%>                           
							<script type="text/javascript">                             
								var myChart1 = new FusionCharts("../../../Charts/Line.swf", "", "1200", "600", "0", "0");                              
								myChart1.setDataXML("<%=strXML1%>");                             
								myChart1.render("15dayUserStat");                          
							</script>  
						</div> 
						</c:if>
					</p>
				</td>
				<td>
					<p align="center">
						<c:if test="${status eq '15dayUserStatInfo'}">
							<div id="15dayUserStatInfo">
							<% String strXML1 = (String)request.getAttribute("strXML1");%>                           
							<script type="text/javascript">                             
								var myChart1 = new FusionCharts("../../../Charts/Line.swf", "", "1200", "600", "0", "0");                              
								myChart1.setDataXML("<%=strXML1%>");                             
								myChart1.render("15dayUserStatInfo");                          
							</script> 
							</div> 
						</c:if>
					</p>
				</td>
				<td>
					<p align="center">
						<c:if test="${status eq '15dayUserStatActive'}">
							<div id="15dayUserStatActive">
							<% String strXML1 = (String)request.getAttribute("strXML1");%>                           
							<script type="text/javascript">                             
								var myChart1 = new FusionCharts("../../../Charts/Line.swf", "", "1200", "600", "0", "0");                              
								myChart1.setDataXML("<%=strXML1%>");                             
								myChart1.render("15dayUserStatActive");                          
							</script>  
							</div>
						</c:if>
					</p>
				</td>
				<td>
					<p align="center">
						<c:if test="${status eq 'userStatBar'}">
							<div id="userStatBar">
							<% String strXML1 = (String)request.getAttribute("strXML1");%>                           
							<script type="text/javascript">                             
								var myChart1 = new FusionCharts("../../../Charts/ScrollColumn2D.swf", "", "1200", "600", "0", "0");                              
								myChart1.setDataXML("<%=strXML1%>");                             
								myChart1.render("userStatBar");                          
							</script>
							</div>
						</c:if>
					</p>
				</td>
			
			</tr>
			</table>
		<br/>
			<div class="pub_debook_mes  oz mb10_all">
				<input type="button" value="返 回" class="btn btn_normal"onclick="javascript:history.back(-1);" />
			</div>
		<br />
		</div>
	</body>
</html>
