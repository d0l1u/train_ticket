<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>出票统计页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	    <script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
	    <script type="text/javascript" src="/js/FusionCharts.js"></script>
	    <script type="text/javascript" src="/js/FusionChartsExportComponent.js"></script>
	    <script type="text/javascript" src="/js/jquery.cookie.js"></script>
	    <script type="text/javascript">
	    	function gotoAcquireList(){
	    		var channelStr = $.cookie("channel");
	    		//alert("进入列表页面时从cookie中取到的值是："+channelStr);
	    		if(channelStr==null){
	    			$("form:first").attr("action","/hcStat/queryHcStatList.do");
					$("form:first").submit(); 
		    	}else{
					var channel = new Array(); //定义一数组
		    		channel = channelStr.split(",");
		    		var str = "";
		    		for (var i=0;i<channel.length ;i++ ){
			    		if(i==0){
			    			str += "?channel="+channel[i];
				    	}else{
				    		str += "&channel="+channel[i];
					    }
		    		} 
		    		//alert("channelStr:"+channelStr+"   数组是："+channel+"   连接是："+str);
		    		$("form:first").attr("action","/hcStat/queryHcStatList.do"+str);
					$("form:first").submit(); 
			    }
	    		
				$.cookie("one", "yes", {expires : 7});   
		    }
	    </script>
  	</head>
  
  <body>
    	<div class="book_manage oz">
			<form action="/hcStat/queryHcStatList.do" method="post" name="myform" id="myform">
				<table style="border:none;">
					<tr style="border:none;">
						<td style="border:none;font-size:14px;padding-bottom:10px;">
							历史最高出票量：<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${out_ticket_succeed }</span>张
						</td>
						<td style="border:none;font-size:14px;padding-bottom:10px;">
							历史最高交易量：<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${succeed_money }</span>元
						</td>
						<td style="border:none;font-size:14px;padding-bottom:10px;">
							平均成功率：<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${succeed_cgl }%</span>
						</td>
					</tr>
					<tr style="border:none;">
						<td style="border:none;">
							昨日出票数：<span style="color:#f60;font-weight:bold;font-family:arial;">${ticket_count }</span>张
						</td>
						<td style="border:none;">
							昨日交易量：<span style="color:#f60;font-weight:bold;font-family:arial;">${buy_money }</span>  元
						</td>
						<td style="border:none;">
							昨日成功率：<span style="color:#f60;font-weight:bold;font-family:arial;">${cgl }</span>
						</td>
					</tr>
					<tr style="border:none;">
						<td align="left" colspan="3" style="border:none;">                     
						<div id="divDay15SaleLine">
						<% String strXML1 = (String)request.getAttribute("strXML1");%>                           
							<script type="text/javascript">                             
								var myChart1 = new FusionCharts("../../../Charts/MSCombiDY2D.swf", "", "1200", "600", "0", "0");                              
								myChart1.setDataXML("<%=strXML1%>");                             
								myChart1.render("divDay15SaleLine");                          
							</script>  
						</div> 
						<script type="text/javascript">  
							<% String prePath = (String)request.getAttribute("prePath");%> 
 							var myExportComponent = new FusionChartsExportObject("fcExporter1", "<%=prePath%>../Charts/FCExporter.swf");  
 							myExportComponent.Render("fcexpDiv");  
  						</script>  
						
						</td>
					</tr>
					<tr style="border:none;">
						<td align="right" colspan="3" style="border:none;">
							<!-- <a href="/hcStat/queryHcStatList.do">每日销售明细</a>
							<a href="javascript:gotoAcquireList();">每日销售明细</a> -->
							<br/>
						</td>
					</tr>
					<tr style="border:none;">
						<td align="left" colspan="3" style="border:none;">                     
						<div id="divDayHourSaleLine" >
						<% String strXML2 = (String)request.getAttribute("strXML2");%>                           
							<script type="text/javascript">                             
								var myChart1 = new FusionCharts("../../../Charts/MSLine.swf", "", "1200", "600", "0", "0");                              
								myChart1.setDataXML("<%=strXML2%>");                             
								myChart1.render("divDayHourSaleLine");                          
							</script>  
						</div> 
						</td>
					</tr>
					<tr style="border:none;">
						<td align="right" colspan="3" style="border:none;">
							&nbsp;							
						</td>
					</tr>
					<tr style="border:none;">
						<td align="left" colspan="3" style="border:none;">                     
						<div id="divSBLHourSaleLine" >
						<% String strXML3 = (String)request.getAttribute("strXML3");%>                           
							<script type="text/javascript">                             
								var myChart1 = new FusionCharts("../../../Charts/MSLine.swf", "", "1200", "600", "0", "0");                              
								myChart1.setDataXML("<%=strXML3%>");                             
								myChart1.render("divSBLHourSaleLine");                          
							</script>  
						</div> 
						</td>
					</tr>
				</table>
				<br/>
				<div class="pub_debook_mes  oz mb10_all">
					<input type="button" value="返 回" class="btn btn_normal"onclick="javascript:history.back(-1);" />
				</div>
			</form>
		</div>
  </body>
</html>
