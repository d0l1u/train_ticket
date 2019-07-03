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
		<div style="width:expression(document.body.clientWidth + 'px')">
		<div style="padding:20px">
			<br/> 
			<table style="border:none;">
				<tr style="border:none;">
			
			<!-- 15日以内打码统计图 -->
			<td>
				<p align="center">
					<c:if test="${status eq '15dayCode'}">
						<div id="15dayCode" >
						<% String strXML2 = (String)request.getAttribute("strXML2");%>                           
							<script type="text/javascript">                             
								var myChart1 = new FusionCharts("../../../Charts/MSLine.swf", "", "1100", "450", "0", "0");                              
								myChart1.setDataXML("<%=strXML2%>");                             
								myChart1.render("15dayCode");                          
							</script>  
						</div> 
					</c:if>
				</p>
			</td>
			
			<!-- 15日以内的用户打码统计图 -->
			<td>
				<p align="center">
					<c:if test="${status eq 'peruserCode'}">
						<div id="peruserCode" >
						<% String strXML2 = (String)request.getAttribute("strXML2");%>                           
							<script type="text/javascript">                             
								var myChart1 = new FusionCharts("../../../Charts/MSLine.swf", "", "1100", "450", "0", "0");                              
								myChart1.setDataXML("<%=strXML2%>");                             
								myChart1.render("peruserCode");                          
							</script>  
						</div> 
					</c:if>
				</p>
			</td>
			
			<!-- 3日以内小时打码成功统计图 -->
			<td>
				<p align="center">
					<c:if test="${status eq 'codeSuccess'}">
						<div id="codeSuccess" >
						<% String strXML2 = (String)request.getAttribute("strXML2");%>                           
							<script type="text/javascript">                             
								var myChart1 = new FusionCharts("../../../Charts/MSLine.swf", "", "1100", "450", "0", "0");                              
								myChart1.setDataXML("<%=strXML2%>");                             
								myChart1.render("codeSuccess");                          
							</script>  
						</div> 
					</c:if>
				</p>
			</td>
			
			<td>
				<p aling="center">
					<c:if test="${status eq 'showUserCodeSuccess'}">
						<div id="showUserCodeSuccess">
						<% String strXML1 = (String)request.getAttribute("strXML1");%>                           
							<script type="text/javascript">                             
								var myChart1 = new FusionCharts("../../../Charts/Line.swf", "", "1100", "450", "0", "0");                              
								myChart1.setDataXML("<%=strXML1%>");                             
								myChart1.render("showUserCodeSuccess");                          
							</script>  
						</div> 
					</c:if>
				</p>
			</td>
			
			
			<!-- 15日以内某部门打码统计图 -->
			<td>
				<p align="center">
					<c:if test="${status eq '15dayDepartCode'}">
					<p>
						所在部门：<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">	
									${userDepartment[department] }
								</span>
					</p>
						<div id="15dayDepartCode" >
						<% String strXML2 = (String)request.getAttribute("strXML2");%>                           
							<script type="text/javascript">                             
								var myChart1 = new FusionCharts("../../../Charts/MSLine.swf", "", "1100", "450", "0", "0");                              
								myChart1.setDataXML("<%=strXML2%>");                             
								myChart1.render("15dayDepartCode");                          
							</script>  
						</div> 
					</c:if>
				</p>
			</td>
			
			<td>
				<p align="center">
					<c:if test="${status eq 'showDepartCodeSuccess'}">
						<p>
							所在部门：<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">	
										${userDepartment[department] }
									</span>
						</p>
						<div id="showDepartCodeSuccess">
						<% String strXML1 = (String)request.getAttribute("strXML1");%>                           
							<script type="text/javascript">                             
								var myChart1 = new FusionCharts("../../../Charts/Line.swf", "", "1100", "450", "0", "0");                              
								myChart1.setDataXML("<%=strXML1%>");                             
								myChart1.render("showDepartCodeSuccess");                          
							</script>  
						</div> 
					</c:if>
				</p>
			</td>
			
			</tr>
			</table>
		
		<!-- <br/>	
			<div class="pub_debook_mes  oz mb10_all">
				<input type="button" value="返 回" class="btn btn_normal"onclick="javascript:history.back(-1);" />
			</div>    -->
		<br />
		</div>
		</div>
	</body>
</html>
