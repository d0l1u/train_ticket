
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>订单统计页面Hc</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<style>
			.book_manage .ser{margin:10px 0px;}
		</style>
		<script type="text/javascript" src="/js/jquery.js"></script>
	    <script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
	    <script type="text/javascript" src="/js/jquery.cookie.js"></script>
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
		<script language="javascript" src="/js/json2.js"></script>
	    <script type="text/javascript">

	function getYesterday(){
		//获取系统时间
		var LSTR_ndate = new Date();
		var LSTR_Year = LSTR_ndate.getYear();
		var LSTR_Month = LSTR_ndate.getMonth();
		var LSTR_Date = LSTR_ndate.getDate();
		//处理
		var uom = new Date(LSTR_Year, LSTR_Month, LSTR_Date);
		//获取系统时间的前一天（负数前几天，正数后几天）
		uom.setDate(uom.getDate()-1);
		var LINT_MM = uom.getMonth();
		LINT_MM ++;
		var LSTR_MM = LINT_MM > 10?	LINT_MM:("0" + LINT_MM)
		var LINT_DD = uom.getDate();
		var LSTR_DD = LINT_DD > 10? LINT_DD:("0" + LINT_DD)
		//得到最终结果
		yesterday = uom.getFullYear() + "-" + LSTR_MM + "-" + LSTR_DD;
		//alert(yesterday);
		window.location.href="/jfree/showHourPicture.do?order_time=" + yesterday + "&channel=qunar"
	}
	/** 
	function selectAllChannel(){
		var checklist = document.getElementsByName("channel");
		if(document.getElementById("controlAllChannel").checked){
			for(var i=0; i<checklist.length; i++){
				checklist[i].checked = 1;
			}
		}else{
			for(var j=0; j<checklist.length; j++){
				checklist[j].checked = 0;
			}
		}
		var checklist = document.getElementsByName("sort");
		if(document.getElementById("controlAllChannel").checked){
			for(var i=0; i<checklist.length; i++){
				checklist[i].checked = 1;
			}
		}else{
			for(var j=0; j<checklist.length; j++){
				checklist[j].checked = 0;
			}
		}
	}*/
	function selectAllChannel(){
		var checklist = document.getElementsByName("channel");
		if(document.getElementById("controlAllChannel").checked){
			for(var i=0; i<checklist.length; i++){
				checklist[i].checked = 1;
			}
		}else{
			for(var j=0; j<checklist.length; j++){
				checklist[j].checked = 0;
			}

		}
	}
	function div() {/**类别：11公司 companyDiv 、22商户 merchantDiv 、33内嵌 innerDiv 、44B2C b2cDiv 、55代理 agencyDiv */
	    var cheLength = document.getElementsByName("sort");
	    var controlAllChannel = document.getElementsByName("controlAllChannel");
	   // alert(controlAllChannel+"  "+$("#controlAllChannel").attr('checked'));
	    for(var i=0; i<cheLength.length; i++) {
		    if($("#controlAllChannel").attr('checked')==undefined){//全选---未选中
		    		if(cheLength[i].checked && cheLength[i].value == '11'){
			           document.getElementById('companyDiv').style.display = "block";
			        } else if(!cheLength[i].checked && cheLength[i].value == '11'){
			           document.getElementById('companyDiv').style.display = "none";
			        } else if(cheLength[i].checked && cheLength[i].value == '22'){
				        document.getElementById('merchantDiv').style.display = "block";
				    } else if(!cheLength[i].checked && cheLength[i].value == '22'){
				        document.getElementById('merchantDiv').style.display = "none";
				    } else if(cheLength[i].checked && cheLength[i].value == '33'){
				        document.getElementById('innerDiv').style.display = "block";
				    } else if(!cheLength[i].checked && cheLength[i].value == '33'){
				        document.getElementById('innerDiv').style.display = "none";
				    } else if(cheLength[i].checked && cheLength[i].value == '44'){
				        document.getElementById('b2cDiv').style.display = "block";
				    } else if(!cheLength[i].checked && cheLength[i].value == '44'){
				        document.getElementById('b2cDiv').style.display = "none";
				    } else if(cheLength[i].checked && cheLength[i].value == '55'){
				        document.getElementById('agencyDiv').style.display = "block";
				    } else if(!cheLength[i].checked && cheLength[i].value == '55'){
				        document.getElementById('agencyDiv').style.display = "none";
				    }    
			}else{
				document.getElementById('companyDiv').style.display = "none";
				document.getElementById('merchantDiv').style.display = "none";
				document.getElementById('innerDiv').style.display = "none";
				document.getElementById('b2cDiv').style.display = "none";
				document.getElementById('agencyDiv').style.display = "none";
			}
	    }
	  }

	$(document).ready(function(){ 
		if( $.cookie("one") == "yes"){
			//根据Value值设置checkbox为选中值
			var channelStr = $.cookie("channel");
			if(channelStr==null){
				$("input[name='channel']").attr("checked",false);  
			}else{
				var channel = new Array(); //定义一数组
				channel = channelStr.split(",");
				//alert("第一次加载页面时从cookie中取到的值是："+channel+"，页面该渠道被选中状态");
				$("input[name='channel']").each(function(){
					for (i=0;i<channel.length ;i++ ){
						//alert(channel[i]);
						$("input:checkbox[value='"+ channel[i] +"']").attr('checked','true');
					}    
				}); 
			}
		} 
		$.cookie("one", "no", {expires : 7}); 
	});
	function submitForm(){
		//遍历被选中CheckBox元素的集合 得到Value值
		var channelStr = "";
		$("input[name='channel']:checkbox:checked").each(function(){ 
			channelStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
		}); 
		channelStr = channelStr.substring(0, channelStr.length-1);
		//alert("提交表单时存入cookie的值是："+channelStr);
		$.cookie("channel", channelStr, {expires : 7}); //cookie有效时间是7天
		$("#myform").submit();
	}
	
	
	
	function exportExcel() {
			$("form:first").attr("action","/hcStat/exportexcel.do");
			$("form:first").submit();
			$("form:first").attr("action","/hcStat/queryHcStatList.do");
		}
	//分时统计表
		function tongjiTable(type){
			var channel ="";
			var channelNum = 0;
			$("input[name='channel']:checkbox:checked").each(function(){ //遍历被选中CheckBox元素的集合 得到Value值
				channel = $(this).val();
				channelNum++;
			});
			if(channelNum==0){
				alert("请选中一个渠道!");
				return false;
			}else if(channelNum>1){
				alert("只能选中一个渠道!");
				return false;
			}
			tongjiTableShow(channel,type);
	}
	function tongjiTableShow(channel,type){
		var begin_time=	$('#begin_time').val(); 
		var end_time=	$('#end_time').val();
		if(!begin_time){
		alert("请选择开始时间！");
		return;
		}else{
			if(!end_time){
		        var now = new Date();
		        var year = now.getFullYear();       //年
		        var month = now.getMonth() + 1;     //月
		        var day = now.getDate();            //日
		        var clock = year + "-";
		        if(month < 10)
		            clock += "0";
		        clock += month + "-";
		        if(day < 10)
		            clock += "0";
		        clock += day;
				end_time=clock;
			}
        var result = Date.parse(end_time.replace(/-/g,"/"))- Date.parse(begin_time.replace(/-/g,"/"));
        var days=result/ 86400000;
		if(days<=8){
			var url="/hcStat/queryTongjiPage.do?begin_time="+begin_time+"&end_time="+end_time+"&days="+days+"&channel="+channel+"&type="+type+"&version="+new Date();
			if(type==1){
			showlayer('票数分时统计页面:',url,'1000px','700px')
			}
			if(type==2){
			showlayer('订单分时统计页面:',url,'1000px','700px')
			}
		}else{
			alert("选择时间差不能超过8天！");
		}
		}
					
	}
	//多渠道分时
	function tongjiTableToday(){
			var channel ="";
			var channelNum = 0;
			var channelStr = "";
			$("input[name='channel']:checkbox:checked").each(function(){ //遍历被选中CheckBox元素的集合 得到Value值
				channelStr += $(this).val()+",";
				channelNum++;
			});
			channelStr = channelStr.substring(0, channelStr.length-1);
			if(channelNum==0){
				alert("请至少选择一个渠道!");
				return false;
			}else if(channelNum>5){
				alert("不能超过5个渠道!");
				return false;
			}
			tongjiTableTodayShow(channelStr);
	}
	function tongjiTableTodayShow(channelStr){
		var begin_time=	$('#begin_time').val(); 
		var end_time=	$('#end_time').val();
		if(!begin_time){
		alert("请选择开始时间！");
		return;
		}else{
			if(!end_time){
		        var now = new Date();
		        var year = now.getFullYear();       //年
		        var month = now.getMonth() + 1;     //月
		        var day = now.getDate();            //日
		        var clock = year + "-";
		        if(month < 10)
		            clock += "0";
		        clock += month + "-";
		        if(day < 10)
		            clock += "0";
		        clock += day;
				end_time=clock;
				
				var d2=Date.parse(end_time.replace(/-/g,"/"))- (1*86400000);
				var s2 = new Date(d2);
				var year2 = s2.getFullYear();       //年
				var month2 = s2.getMonth() + 1;     //月
			    var day2 = s2.getDate();            //日
				var clock2 = year2 + "-";
				if(month2 < 10)
				     clock2 += "0";
				clock2 += month2 + "-";
				if(day2 < 10)
				     clock2 += "0";
				clock2 += day2;
				begin_time=clock2;
			}
        var result = Date.parse(end_time.replace(/-/g,"/"))- Date.parse(begin_time.replace(/-/g,"/"));
        var days=result/ 86400000;
		if(days==1){
			var url="/hcStat/queryTongjiTodayPage.do?begin_time="+begin_time+"&end_time="+end_time+"&channelList="+channelStr+"&version="+new Date();
			showlayer('多渠道分时:',url,'1000px','700px')
		}else{
			alert("选择时间差只能是2天！");
		}
		}
	}

	//时段
	function shiduan(end_time,channel,type){
	var d=Date.parse(end_time.replace(/-/g,"/"))- (7*86400000);
		var s = new Date(d);
		var year = s.getFullYear();       //年
		var month = s.getMonth() + 1;     //月
	    var day = s.getDate();            //日
		var clock = year + "-";
		if(month < 10)
		     clock += "0";
		clock += month + "-";
		if(day < 10)
		     clock += "0";
		clock += day;
	var	begin_time=clock;
	var url="/hcStat/queryTongjiPage.do?begin_time="+begin_time+"&end_time="+end_time+"&days="+7+"&channel="+channel+"&type="+type+"&version="+new Date();
			if(type==1){
			showlayer('票数分时统计页面:',url,'1000px','700px')
			}
			if(type==2){
			showlayer('订单分时统计页面:',url,'1000px','700px')
			}
	}
	
	//核验接口
	function tongjiCheck(){
		var channelNum = 0;
		var begin_time=	$('#begin_time').val(); 
		var end_time=$('#end_time').val();
		var url="";
			var channelStr = "";
			$("input[name='channel']:checkbox:checked").each(function(){ //遍历被选中CheckBox元素的集合 得到Value值
				channelStr += $(this).val()+",";
				channelNum++;
			});
			channelStr = channelStr.substring(0, channelStr.length-1);
			if(channelNum==1 && (channelStr=="elong"||channelStr=="qunar"||channelStr=="tongcheng")){
				if(!end_time){
				url="/hcStat/tongjiCheck.do?channel="+channelStr;
				showlayer('核验接口:',url,'1000px','700px')
				}else{
     	 	 		 var result = Date.parse(end_time.replace(/-/g,"/"))- Date.parse(begin_time.replace(/-/g,"/"));
       				 var days=result/ 86400000;
					if(days<7){
						url="/hcStat/tongjiCheck.do?channel="+channelStr+"&begin_time="+begin_time+"&end_time="+end_time+"&days="+days;
						showlayer('核验接口:',url,'1000px','700px')
					}else{
						alert("选择单个渠道，时间范围不大于1周！");
					}
				}
			}else{
				if(!end_time){
				url="/hcStat/tongjiCheck.do";
				showlayer('核验接口:',url,'1000px','700px')
				}else{
				 	var result = Date.parse(end_time.replace(/-/g,"/"))- Date.parse(begin_time.replace(/-/g,"/"));
       			 	var days=result/ 86400000;
					if(days<5){
						url="/hcStat/tongjiCheck.do?begin_time="+begin_time+"&end_time="+end_time+"&days="+days;
						showlayer('核验接口:',url,'1000px','700px')
					}else{
						alert("多渠道查询，时间范围不大于5天！");
					}
				}
			}
			
	}
		
	//19e统计表
	function tongjil9e(){
		var begin_time=	$('#begin_time').val(); 
		var end_time=$('#end_time').val();
		var url="";
		if(!end_time){
		url="/hcStat/tongji19e.do";
		}else{
		url="/hcStat/tongji19e.do?begin_time="+begin_time+"&end_time="+end_time;
		}
		showlayer('19e统计表:',url,'1000px','700px')
	}
	function tongjil9eOne(end_time){
		var url="/hcStat/tongji19e.do?end_time="+end_time;
		showlayer('19e统计表:',url,'1000px','700px')
	}
	//验证码统计
	function tongjiCode(){
		url="/hcStat/tongjiCode.do";
		showlayer('验证码统计:',url,'1000px','700px')
	}
	</script>
  	</head>
  
  <body onload="div();"><div></div>
    	<div class="book_manage oz">
			<form action="/hcStat/queryHcStatList.do" method="post" name="myform" id="myform">
				<div style="border: 0px solid #00CC00; margin: 10px;">
						<ul class="oz" style="margin-top: 14px;">
							<li>
								开始时间：
								<input type="text" class="text" id="begin_time" name="begin_time" readonly="readonly" value="${begin_time }"
										onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})"/> 
							</li>
							<li>
								结束时间：
								<input type="text" class="text" id="end_time" name="end_time" readonly="readonly" value="${end_time }"
									onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" /> 
							</li>
							<li>
								<a href="/hcStat/queryHcStat.do">19e图表</a>
							</li>
							<li>
								<a href="/jfree/showPictureLine.do">19e15日统计图</a>
							</li>
							<li>
								<a href="/jfree/showDayHourDetail.do">19e分时统计图</a>
							</li> 
						<!--	<li>
							 <a href="/jfree/showPictureLineQunar.do">qunar15日统计图</a>
							</li>
							<li>
								<a href="javascript:getYesterday();">qunar小时统计图</a>
							</li> -->	
							</ul>
							
							<dl class="oz" style="padding-top:20px;">
								<dt style="float:left;padding-left:40px;line-height:22px;">渠道：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</dt>
								<dd  style="float:left;width:910px;">
									<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
										<input type="checkbox" onclick="selectAllChannel()" name="controlAllChannel" style="controlAllChannel" id="controlAllChannel"/><label for="controlAllChannel">&nbsp;全部</label>
									</div>
								<c:forEach items="${channel_types }" var="s" varStatus="index">
								<c:if test="${s.key ne '301016' && s.key ne '30101601' && s.key ne '30101602'}">
									<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
										<input type="checkbox" id="channel${index.count }" name="channel" value="${s.key }" value="1"
											<c:if test="${fn:contains(channelStr, s.key ) }">checked="checked"</c:if> />
										<label for="channel${index.count }">
											${s.value }
										</label>
									</div>
									</c:if>
								</c:forEach>
								<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
									<input type="checkbox" id="channel00" name="channel" value="mobile"
										<c:if test="${fn:contains(channelStr, 'mobile')}">checked="checked"</c:if> />
										<label for="channel00">掌铺</label>
								</div>
								<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
									<input type="checkbox" id="channel01" name="channel" value="30101612"
										<c:if test="${fn:contains(channelStr, '30101612')}">checked="checked"</c:if> />
										<label for="channel01">利安</label>
								</div>
								<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
									<input type="checkbox" id="channel02" name="channel" value="elong1"
										<c:if test="${fn:contains(channelStr, 'elong1')}">checked="checked"</c:if> />
										<label for="channel02">艺龙1</label>
								</div>
								<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
									<input type="checkbox" id="channel03" name="channel" value="elong2"
										<c:if test="${fn:contains(channelStr, 'elong2')}">checked="checked"</c:if> />
										<label for="channel03">艺龙2</label>
								</div>
								</dd>
							</dl>
							
							<!-- 
							<ul class="ser oz">
								<li>
									类&nbsp;&nbsp;&nbsp;&nbsp;别：
									<input type="checkbox" onclick="selectAllChannel();div();" name="controlAllChannel" style="controlAllChannel" id="controlAllChannel" <c:if test="${controlAllChannel eq 'on' }">checked="checked"</c:if> /><label for="controlAllChannel">&nbsp;全部</label>
									<c:forEach items="${sort_channel}" var="s" varStatus="index">
										<input type="checkbox" id="sort${index.count }"
												name="sort" value="${s.key }" onclick="div();"
													<c:if test="${fn:contains(sort, s.key ) }">checked="checked"</c:if> />
											<label for="sort${index.count }">
												${s.value }
											</label>
									</c:forEach>
								</li>
							</ul>
							
							<div id="companyDiv" style="display:none">
							<ul class="ser oz">
								<li>
								<label style="float:left;"> 公司渠道：&nbsp;</label>
								<table style="width:870px;display:inline;border:none;"><tr style="border:none;"><td style="text-align:left;border:none;">
									<c:forEach items="${company_channel }" var="s" varStatus="index">
											<input type="checkbox" id="companychannel${index.count }"
												name="channel" value="${s.key }"
												<c:if test="${fn:contains(channelStr, s.key ) }">checked="checked"</c:if> />
											<label for="companychannel${index.count }">
												${s.value }
											</label>
									</c:forEach>
								</td></tr></table>
								</li>
					        </ul>
					        </div>
					        <div id="merchantDiv" style="display:none">
							<ul class="ser oz">
								<li>
								<label style="float:left;"> 商户渠道：&nbsp;</label>
								<table style="width:900px;display:inline;border:none;"><tr style="border:none;"><td style="text-align:left;border:none;">
									<c:forEach items="${merchant_channel }" var="s" varStatus="index">
											<input type="checkbox" id="merchantchannel${index.count }"
												name="channel" value="${s.key }"
												<c:if test="${fn:contains(channelStr, s.key ) }">checked="checked"</c:if> />
											<label for="merchantchannel${index.count }">
												${s.value }
											</label>
									</c:forEach>
								</td></tr></table>
								</li>
					        </ul>
					        </div>
					        <div id="innerDiv" style="display:none">
							<ul class="ser oz">
								<li>
								<label style="float:left;"> 内嵌渠道：&nbsp;</label>
								<table style="width:870px;display:inline;border:none;"><tr style="border:none;"><td style="text-align:left;border:none;">
									<c:forEach items="${inner_channel }" var="s" varStatus="index">
											<input type="checkbox" id="innerchannel${index.count }"
												name="channel" value="${s.key }"
												<c:if test="${fn:contains(channelStr, s.key ) }">checked="checked"</c:if> />
											<label for="innerchannel${index.count }">
												${s.value }
											</label>
									</c:forEach>
								</td></tr></table>
								</li>
					        </ul>
					        </div>
					        <div id="b2cDiv" style="display:none">
							<ul class="ser oz">
								<li>
								<label style="float:left;"> B2C&nbsp;渠道：&nbsp;</label>
								<table style="width:870px;display:inline;border:none;"><tr style="border:none;"><td style="text-align:left;border:none;">
									<c:forEach items="${b2c_channel }" var="s" varStatus="index">
											<input type="checkbox" id="b2cchannel${index.count }"
												name="channel" value="${s.key }"
												<c:if test="${fn:contains(channelStr, s.key ) }">checked="checked"</c:if> />
											<label for="b2cchannel${index.count }">
												${s.value }
											</label>
									</c:forEach>
								</td></tr></table>
								</li>
					        </ul>
					        </div>
					        <div id="agencyDiv" style="display:none">
							<ul class="ser oz">
								<li>
								<label style="float:left;"> 代理渠道：&nbsp;</label>
								<table style="width:870px;display:inline;border:none;"><tr style="border:none;"><td style="text-align:left;border:none;">
									<c:forEach items="${agency_channel }" var="s" varStatus="index">
											<input type="checkbox" id="agencychannel${index.count }"
												name="channel" value="${s.key }"
												<c:if test="${fn:contains(channelStr, s.key ) }">checked="checked"</c:if> />
											<label for="agencychannel${index.count }">
												${s.value }
											</label>
									</c:forEach>
								</td></tr></table>
								</li>
					        </ul>
					        </div>
					         -->
					</div>
				<p>
					<input type="button" value="查 询" class="btn" onclick="submitForm();" />
					 <%LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
						String user_level = loginUserVo.getUser_level(); 
					 if ("2".equals(loginUserVo.getUser_level()) ) {%>
					<input type="button" value="导出Excel" class="btn" onclick="exportExcel()" />
					<%} %>
					<input type="button" value="票数分时统计" class="btn" onclick="tongjiTable('1')" />
					<input type="button" value="订单分时统计" class="btn" onclick="tongjiTable('2')" />
					<input type="button" value="多渠道分时" class="btn" onclick="tongjiTableToday()" />
					<input type="button" value="核验接口" class="btn" onclick="tongjiCheck()" />
					<input type="button" value="19e统计表" class="btn" onclick="tongjil9e()" />
					<input type="button" value="验证码统计" class="btn" onclick="tongjiCode()" />
				</p>
				<c:if test="${!empty isShowList}">
					<table id="table_list">
						<tr style="background: #f0f0f0;">
							<th style="width:8px;">
								NO
							</th>
							<th>
								渠道
							</th>
							<th>
								订单时间
							</th>
						<!--		<th>
								活跃数
							</th>
							<th style="width:45px;">
								代理商登陆数
							</th>-->
							<th>
								总订单
							</th>
							<th style="width:30px;">
								成功订单
							</th>
							<th style="width:30px;">
								失败订单
							</th>
							<th style="width:45px;">
								超时未支付
							</th>
							<th style="width:30px;">
								用户取消
							</th>
							<th>
								预下单
							</th>
							<!--	<th>
								支付失败
							</th>
							<th style="width:30px;">
								超时订单
							</th>-->
							<th>
								退款成功
							</th>
							<th>
								票数总计
							</th>
							<th>
								成功金额
							</th>
							<th>
								失败金额
							</th>
							<th>
								退款金额
							</th>
				<!--		<th>
								差价金额
							</th>   -->	
							<th>
								保险
							</th> 
							<th style="width:30px;">
								保险失败
							</th>
							<th>
								SVIP数
							</th>
							<th style="width:30px;">
								SVIP失败
							</th>
							<th>
								利润
							</th>
							<!--	<th>
								出票效率
							</th>	-->
							<th>
								成功率
							</th>
						 	<th>
								失败率
							</th>
							<th style="width:30px;">
								占座成功
							</th>
							<th style="width:30px;">
								占座失败
							</th>
						<!--	<th>
								VIP失败率
							</th> 
							<th>
								转换率
							</th>-->
							<th style="width:30px;">
								预订时长
							</th>
						 	<th style="width:30px;">
								支付时长
							</th>
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${hcStat_List}" varStatus="idx">
							<tr 
								<c:if test="${fn:contains('01,03,05,07,09,11,13,15,17,19,21,23,25,27,29,31', list.order_time_day )}">
									style="background: #BEE0FC;"
								</c:if>
							>
								<td>
									${idx.index+1}
								</td>
								<td>
									<c:choose>
										<c:when test="${list.channel eq 'qunar1'}">
											19旅
										</c:when>
										<c:when test="${list.channel eq 'ext'}">
											商户
										</c:when>
										<c:when test="${list.channel eq '30101612'}">
											利安
										</c:when>
										<c:when test="${list.channel eq 'elong1'}">
											艺龙1
										</c:when>
										<c:when test="${list.channel eq 'elong2'}">
											艺龙2
										</c:when>
										<c:when test="${list.channel eq 'mobile'}">
											掌铺
										</c:when>
										<c:otherwise>
											${channel_types[list.channel] }
										</c:otherwise>
									</c:choose>
									
								</td>
								<td>
									${list.order_time }
								</td>
							<!--		<td>
									${list.active }
								</td>
								<td>
									${list.agent_login_num }
								</td>-->
								<td>
									${list.order_count }
								</td>
								<td>
									${list.out_ticket_succeed }
								</td>
								<td>
									${list.out_ticket_defeated }
								</td>
								<td>
									${list.out_time_order }
								</td>
								<td>
									${list.cancel_order }
								</td>
								<td>
									${list.preparative_count }
								</td>
								<!--<td>
									${list.pay_defeated }
								</td> 
								<td>
									${list.over_time }
								</td> -->
								<td>
									${list.refund_count }
								</td>
								<td>
									${list.ticket_count }
								</td>
								<td>
									${list.succeed_money }
								</td>
								<td>
									${list.defeated_money }
								</td>
								<td>
									${list.refund_money }
								</td>
					<!--		<td>
									${list.change_money }
								</td>  -->	
								<td>
									${list.bx_count }
								</td>
								<td>
									${list.vip_lose }
								</td>
								<td>
									${list.svip_num }
								</td>
								<td>
									${list.svip_lose }
								</td>
								<td>
									${list.bx_countMoney }
								</td>
								<!--<td>
									${list.out_ticket_XL }
								</td>-->
								<td>
									${list.succeed_cgl }
								</td>
								<td>
									${list.succeed_sbl }
								</td>
								<td>
									${list.holdseat_cgl }
								</td>
								<td>
									${list.holdseat_sbl }
								</td>
						<!--  	<td>
									${list.succeed_vip_sbl }
								</td> 
								<td>
									${list.succeed_odds }
								</td>-->
								<td>
									${list.out_ticket_XL }
								</td>
								<td>
									${list.pay_time }
								</td>
								<td>
					<!--  	<a href="/jfree/showHourPicture.do?order_time=${list.order_time }&channel=${list.channel }">图表</a>-->
							<a href="javascript:shiduan('${list.order_time }','${list.channel }','1');">票数</a>
							<a href="javascript:shiduan('${list.order_time }','${list.channel }','2');">订单</a>
									<c:choose>
										<c:when test="${list.channel eq '19e'}">
										<a href="javascript:tongjil9eOne('${list.order_time }');">统计</a>
											<c:if test="${idx.index ne 0 }">
											<br/><a href="/hcStat/queryThisDayInfo.do?create_time=${list.order_time }">各省</a>
											</c:if>
										</c:when>
									</c:choose>
								</td>
							</tr>
						</c:forEach>
					</table>
					<script type="text/javascript">
				//	$().ready(function(){
				//		var cobj=document.getElementById("table_list").rows;
   				//		for (i=1;i< cobj.length ;i++) {
				//			if((parseInt((i-1)/5))%2==0){
				//				cobj[i].style.background = "#FFEEDD";
				//			}else{
				//				cobj[i].style.background = "#FFFFFF";
				//			}
   				//		}
				//	});	
   						//var cobj=document.getElementById("table_list").rows;
   						//for (i=1;i< cobj.length ;i++) {
   	   					//	for (j=0;j<=i;j++){
						//		if((i%4)==0){cobj[i].style.background = "#FFEEDD";}
						//		if((i%4)==1){cobj[i].style.background = "#FFFFFF";}
   	   	   				//	}
   						//}
					</script>
					<jsp:include page="/pages/common/paging.jsp" />
				</c:if>
			</form>
		</div>
  </body>
</html>
