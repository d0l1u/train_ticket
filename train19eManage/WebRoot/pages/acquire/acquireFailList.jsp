<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo"%>
<%@ page import="com.l9e.transaction.vo.PageVo"%>
<%@ page import="java.util.*"%>
<%@ page import="com.l9e.util.JSONUtil"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>出票失败原因管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
		<script type="text/javascript">
	<%
		PageVo pageObject = (PageVo) request.getAttribute("pageBean");
		int pageIndex = pageObject.getPageIndex();
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user_level = loginUserVo.getUser_level();
	%>
	
	$().ready(function(){
		if($("#defaultStartDate").val()==null || $("#defaultStartDate").val()==""){
			$("#defaultStartDate").val(GetDateStr(-15));
		}
	});
//	function currentCode(){
	//	var url = "/acquire/queryCurrentCode.do?&version="+new Date();
		
	//	$.get(url,function(data){
			//alert(data);
	//		var strJSON = data;//得到的JSON
	//		var obj = new Function("return" + strJSON)();//转换后的JSON对象
			//alert("totalPerson"+obj.totalPerson);//json name
	//		document.getElementById("totalPerson").value = obj.totalPerson;
	//		document.getElementById("codeCountToday").value = obj.codeCountToday;
	//		document.getElementById("codeSuccessToday").value = obj.codeSuccessToday;
	//		document.getElementById("curcodePerMin").value = obj.curcodePerMin;
	//		document.getElementById("hiscodePerMin").value = obj.hiscodePerMin;
	//		document.getElementById("uncodeCount").value = obj.uncodeCount;
						
			
		//$("totalPerson").val=="1"
	//	});
//	}	
    function GetDateStr(AddDayCount){     
            var dd = new Date();     
            dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期     
            var y = dd.getFullYear();     
            var m = dd.getMonth()+1;//获取当前月份的日期  
            if(m<10){     
				m="0"+m;  
			}      
            var d = dd.getDate();
              if(d<10){     
				 d="0"+d;  
				}       
              return y+"-"+m+"-"+d;     
         }
    
	
	function submitUrl(url){
		if(confirm("是否交给机器人处理？")){
			location.href = url; 
		}
	}
	//订单锁 
	function gotoOrderPay(order_id){
		//ajax验证是否锁
		var url = "/acquire/queryPayIsLock.do?order_id="+order_id+"&version="+new Date();
		
			$.get(url,function(data){
			if(data != null && data != ""){
				var temp = data ;
				var str1 = temp.split("&") ;
				var str =str1[1]; 
				
				alert("此订单已经锁定，锁定人为"+str);
				return;
			 }else{
				 url="/acquire/queryAcquireInfo.do?order_id="+order_id+"&query_type=1";
				 showlayer('人工处理',url,'1000px','800px')
			 }
		});
	}
	//订单锁 
	function gotoRobot(order_id,create_time){
		//ajax验证是否锁
		var url = "/acquire/queryPayIsLock.do?order_id="+order_id+"&version="+new Date();
			$.get(url,function(data){
			if(data != null && data != ""){
				var temp = data ;
				var str1 = temp.split("&") ;
				var str =str1[1]; 
				
				alert("此订单已经锁定，锁定人为"+str);
				return;
			 }else{
			// window.location="/acquire/updateAcquireInfo.do?order_id="+order_id+"&order_status=01&create_time="+create_time;
				
				var  uri = "/acquire/updateAcquireToRobot.do?order_id="+order_id+"&order_status=01&create_time="+create_time+"&version="+new Date();
				$.post(uri,function(data){
					if(data=="yes"){
						//window.location="/acquire/queryAcquireList.do?pageIndex="+<%=pageIndex%>;
						$("form:first").attr("action","/acquire/queryAcquireList.do?pageIndex="+<%=pageIndex%>);
						$("form:first").submit();
						//window.location.reload();
					}else{
						alert("机器人处理请求失败");
					}
				});
			 }
		 	 
		});
	}

	//支付锁 
	function gotoOrderPayLock(order_id){
		//ajax验证是否锁
		var url = "/acquire/queryPayIsPayLock.do?order_id="+order_id+"&version="+new Date();
			$.get(url,function(data){
			if(data != null && data != ""){
				var temp = data ;
				var str1 = temp.split("&") ;
				var str =str1[1]; 
				
				alert("此订单已经锁定，锁定人为"+str);
				return;
			}else{
				url ="/acquire/queryAcquireInfo.do?order_id="+order_id+"&query_type=1";
				
		 		showlayer('人工支付',url,'1000px','800px')
			}
		});
	}
	
	var auto_scan=0;
	var sum_time=0;
	var base_time=8;
	var timer=setInterval("refreshPage();",1000);
	function refreshPage(){
		if(auto_scan==1){
			sum_time++;
			var page_left=parseInt($("#left_time").text());
			$("#left_time").html(page_left-1>0?page_left-1:0);
			if(sum_time==base_time){
				$("form:first").submit();
			}
		}
	} 
//	function switchRefresh(obj){
//		if(!$(obj).attr("checked")) {
//			auto_scan=0;
//			setCookie("auto_flag", "0");
//			$("#switcher").html("开启自动刷新");
//		}else{
//			auto_scan=1;
//			setCookie("auto_flag", "1");
//			$("#switcher").html("关闭自动刷新");
//		}
//	}
	
//	function setCookie(name,value){
//	    var Days = 30;
//	    var exp = new Date();
//	    exp.setTime(exp.getTime() + Days*24*60*60*1000);
//	    document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
//	}
	
	//读取cookies
//	function getCookie(name){
//	    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
//	    if(arr=document.cookie.match(reg))
//	        return (arr[2]);
//	    else
//	        return null;
//	}
	
//	$().ready(function(){
//		var auto_flag=getCookie("auto_flag");
//		if(auto_flag==null || auto_flag=="0"){
//			auto_scan=0;
//			$("#auto_check").attr("checked", false);
//		}else{
//			auto_scan=1;
//			$("#auto_check").attr("checked", true);
//		}
//	});
	function submitUpdateStatusInfo(order_id){
			var  url = "/acquire/updateInfoOrderStatus.do?order_id="+order_id+"&version="+new Date();
				$.post(url,function(data){
					if(data=="yes"){
						//window.location="/acquire/queryAcquireList.do?pageIndex="+<%=pageIndex%>;
						$("form:first").attr("action","/acquire/queryAcquireList.do?pageIndex="+<%=pageIndex%>);
						$("form:first").submit();
						//window.location.reload();
					}else{
						alert("预定重发请求失败");
					}
				});
	}

	function queryInfo(order_id){
		var url="/acquire/queryAcquireInfo.do?order_id="+order_id+"&query_type=mingxi";
		showlayer('明细',url,'1000px','800px')
		}

	function submitForm(){
		$("form:first").attr("action","/acquire/queryAcquireFailList.do?order_status=10&over_time=0");
		$("form:first").submit();
		}
	function overTime(){
		$("form:first").attr("action","/acquire/queryAcquireList.do?orderStatus=99&orderStatus=10&over_time=1");
		$("form:first").submit();
		}
	function checkboxed(this_value,key){
		var str=document.getElementsByName("channel");
		var objarray=str.length;
		var chestr="";
		for (i=0;i<objarray;i++){
	  		if( (str[i].checked == true) && (str[i].value==this_value) ){
		  		//alert(str[i].value);
		  		//alert((str[i].value).indexOf('19e'));
		  		var channel_id = "#channel"+(i+1);
				if((str[i].value).indexOf(key)>=0 || (str[i].value).indexOf('19e')>=0 || (str[i].value).indexOf('cmpay')>=0 || (str[i].value).indexOf('19pay')>=0 || (str[i].value).indexOf('web')>=0|| (str[i].value).indexOf('app')>=0 || (str[i].value).indexOf('ccb')>=0 || (str[i].value).indexOf('weixin')>=0){
		   			$("#channel_qita").attr("checked",true);
		   			$("#channel_qunar1").attr("checked",false);//去哪儿的radio
		   			$("#channel_qunar").attr("checked",false);//去哪儿的checkbox
		   		//	$("#channel_elong").attr("checked",false);//艺龙的checkbox
		   			$(channel_id).attr("checked",true);
		   			document.getElementById('divqunarcon').style.display = "none";
				    document.getElementById('divcon').style.display = "block";
				    //var ss = $("#divqunarcon input:checked");
		  			//for(var i=0;i<ss.length;i++){
		  			//	ss[i].checked = false;
		  			//	alert(ss[i].value);
		  			//}
				    
			   	}
				if((str[i].value).indexOf('qunar')>=0 ){   
	   				$("#channel_qunar1").attr("checked",true);
	   				for(j=0;j<objarray;j++){    
		   				$("#channel"+j).attr("checked",false);
		   			}
	   				if((str[i].value).indexOf('qunar')>=0){
		   				$("#channel_qunar").attr("checked",true);//去哪儿的checkbox
		   			}
	   			//	if((str[i].value).indexOf('elong')>=0){
	   			//		$("#channel_elong").attr("checked",true);//艺龙的checkbox
		   		//	}
	   				document.getElementById('divqunarcon').style.display = "block";
	  	            document.getElementById('divcon').style.display = "none";
		   		}
				break;
	  		}
		}
	}
	
	function div() {
		
	    var cheLength = document.getElementsByName("channel1");
	    for(var i=0; i<cheLength.length; i++) {
	       //账号停用展示停用原因
	       if(cheLength[i].checked && cheLength[i].value == '1'){
	          document.getElementById('divqunarcon').style.display = "block";
	          document.getElementById('divcon').style.display = "none";
	       }else if(cheLength[i].checked && cheLength[i].value == '0'){
	    	  document.getElementById('divqunarcon').style.display = "none";
		      document.getElementById('divcon').style.display = "block";
	       }
	    }
	}
	function selectAll(){
		var acc_id = document.getElementsByName("channel");
	    //alert($('input:radio[name=channel1]:checked').val());
	    var val;
	    $(".isExchangeDay").each(function(){      
		    if($(this).attr("checked")){               
			    val=$(this).attr("value")          
			} 
		});
		//alert(val);
	    if(val == 1 ){//qunar
	        for(var i = 0 ; i < acc_id.length ; i++){
		        if(acc_id[i].value == 'qunar' ){
		        	acc_id[i].checked = true ;
			    }else{
			    	acc_id[i].checked = false ;
				}
	        }
	    }else if(val == 0){
	        for(var i = 0 ; i < acc_id.length ; i++){
	        	if(acc_id[i].value == 'qunar' ){
	        		acc_id[i].checked = false ;
			    }else{
			    	acc_id[i].checked = true ;
				}
	            
	        }
	    }
	}
	
		//鼠标悬浮于“明细”，显示该订单的操作日志
	var heightDiv = 0; 
	function showdiv(order_id){  
	     var oSon = window.document.getElementById("hint");   
	     if (oSon == null) return;   
	     with (oSon){   
	 		 $.ajax({
				url:"/acquire/queryOrderOperHistory.do?order_id="+order_id,
				type: "POST",
				cache: false,
				dataType: "json",
				async: true,
				success: function(data){
					if(data=="" || data == null){
						return false;
					}
					var size = data.length;
					heightDiv = 0;
					for(var i=0; i<size; i++){
						var index = (parseInt(i)+1);
						if($("#index_"+index).innerText!=index){
							$("#historyTable").append("<tr line-height='15px'align='center' ><td id='index_'"+index+"''>"+index+"</td><td align='left'   style='word-break:break-all;'>"+data[i].order_optlog+"</td>"+
									"<td>"+data[i].create_time+"</td><td>"+data[i].opter+"</td></tr>");
							if(data[i].order_optlog.length>44){
								heightDiv = heightDiv + 30;
							}else{
								heightDiv = heightDiv + 15;
							}
						}
					}
				}
			});
	 		innerHTML = historyDiv.innerHTML; 
		    style.display = "block"; 
		    heightDiv = heightDiv + 106;//106为div中表格边距以及表头的高度86+20
		    style.pixelLeft = window.event.clientX + window.document.body.scrollLeft - 750;   
		    style.pixelTop = window.event.clientY + window.document.body.scrollTop - heightDiv;   
	    }   
	}   
	//鼠标离开“明细”，隐藏该订单的操作日志
	function hidediv(){   
	    var oSon = window.document.getElementById("hint");   
	    if(oSon == null) return;   
	    oSon.style.display="none";   
	} 
	//全选
	function selectAllError(){
		var checklist1 = document.getElementsByName("ERROR_INFO_1");
		var checklist2 = document.getElementsByName("ERROR_INFO_2");
		var checklist3 = document.getElementsByName("ERROR_INFO_3");
		var checklist4 = document.getElementsByName("ERROR_INFO_4");
		var checklist5 = document.getElementsByName("ERROR_INFO_5");
		var checklist6 = document.getElementsByName("ERROR_INFO_6");
		var checklist7 = document.getElementsByName("ERROR_INFO_7");
		var checklist8 = document.getElementsByName("ERROR_INFO_8");
		var checklist11 = document.getElementsByName("ERROR_INFO_11");
		var checklist9 = document.getElementsByName("ERROR_INFO_9");
		if(document.getElementById("controlAllError").checked){
				checklist1[0].checked = 1;
				checklist2[0].checked = 1;
				checklist3[0].checked = 1;
				checklist4[0].checked = 1;
				checklist5[0].checked = 1;
				checklist6[0].checked = 1;
				checklist7[0].checked = 1;
				checklist8[0].checked = 1;
				checklist11[0].checked = 1;
				checklist9[0].checked = 1;
		}else{
				checklist1[0].checked = 0;
				checklist2[0].checked = 0;
				checklist3[0].checked = 0;
				checklist4[0].checked = 0;
				checklist5[0].checked = 0;
				checklist6[0].checked = 0;
				checklist7[0].checked = 0;
				checklist8[0].checked = 0;
				checklist11[0].checked = 0;
				checklist9[0].checked = 0;

		}
	}
	//全选
	function selectAllErrorQunar(){
		var checklist = document.getElementsByName("error_qunar_info");
		if(document.getElementById("controlAllErrorQunar").checked){
			for(var i=0; i<checklist.length; i++){
				checklist[i].checked = 1;
			}
		}else{
			for(var j=0; j<checklist.length; j++){
				checklist[j].checked = 0;
			}

		}
	}
	
	
function exportExcel() {
			$("form:first").attr("action","/acquire/exportFailexcel.do?order_status=10");
			$("form:first").submit();
			$("form:first").attr("action","/acquire/queryAcquireFailList.do?order_status=10");
		}
</script>
<style>
	#refresh_span a:link,#refresh_span a:visited{color:#2ea6d8;}
	#refresh_span a:hover{text-decoration:underline;}
	#hint{width:700px;border:1px solid #C1C1C1;background:#F0FAC1;position:fixed;z-index:9;padding:6px;line-height:17px;text-align:left;overflow:auto;} 
</style>


	</head>

	<body onload="div();"><div></div>
		<div class="book_manage oz">
			<form action="/acquire/queryAcquireFailList.do?order_status=10" method="post">
				<div style="border: 0px solid #00CC00;">
					<ul class="order_num oz" style="margin-top: 10px;">
						<li>
							订单号：&nbsp;&nbsp;&nbsp;
							<input type="text" class="text" name="order_id"
								value="${order_id }" />
						</li>
						<li>
							订单级别：&nbsp;
							<select name="level" style="width:170px;">
								<option value="" <c:if test="${empty level}">selected</c:if>>
									全部
								</option>
							<!--  	<option value="VIP"
									<c:if test="${!empty level && level eq 'VIP'}">selected</c:if>>
									VIP
								</option>-->
								<option value="5"
									<c:if test="${!empty level && level eq '5'}">selected</c:if>>
									SVIP
								</option>
								<option value="1"
									<c:if test="${!empty level && level eq '1'}">selected</c:if>>
									VIP1
								</option>
								<option value="2"
									<c:if test="${!empty level && level eq '2'}">selected</c:if>>
									VIP2
								</option>
								<option value="0"
									<c:if test="${!empty level && level eq '0'}">selected</c:if>>
									普通
								</option>
								<option value="10"
									<c:if test="${!empty level && level eq '10'}">selected</c:if>>
									联程
								</option>
							</select>
						</li>
						<li></li>
						<%
						
						if ("2".equals(loginUserVo.getUser_level())
								|| "1".equals(loginUserVo.getUser_level())
								|| "1.2".equals(loginUserVo.getUser_level())) {
						%>
						<li>
							&nbsp;&nbsp;操作人：&nbsp;
							<input type="text" class="text" name="opt_ren" value="${opt_ren}" />
						</li>
						<%} %>
					</ul>
					
					<ul class="order_num oz" style="margin-top: 10px;">
						<li>
							开始时间：&nbsp;
							<input type="text" class="text" name="begin_info_time"
								readonly="readonly" value="${begin_info_time }"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" id="defaultStartDate1"/>
						</li>
						<li>
							结束时间：&nbsp;
							<input type="text" class="text" name="end_info_time"
								readonly="readonly" value="${end_info_time }"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
						</li>
					</ul>
					<!-- 
					<ul class="ser oz">
					<li>
						<label style="float:left;"> 渠道：&nbsp;&nbsp;&nbsp;&nbsp;</label>
						<table style="width:930px;display:inline;border:none;"><tr style="border:none;"><td style="text-align:left;border:none;">
							<input type="radio" name="channel1" id="channel_qita"  value="0" onclick="div();selectAll();" class="isExchangeDay" style="display:none;"
								<c:if test="${fn:contains(channel1, '0' ) }">checked="checked"</c:if>/> 
								<label for="channel_qita">
								<c:forEach items="${channel_types }" var="s" varStatus="index">
									<input type="checkbox" id="channel${index.count }" onclick="div();checkboxed(this.value,'${s.key}');" name="channel" value="${s.key }"
										<c:if test="${fn:contains(channelStr, s.key ) }">checked="checked"</c:if> />
										${s.value }
									
								</c:forEach>
								</label>
							<input type="radio" id="channel_qunar1" onclick="div();selectAll();" name="channel1" value="1" class="isExchangeDay" style="display:none;"
								<c:if test="${fn:contains(channel1, '1' ) }">checked="checked"</c:if>	/> 
									<label for="channel_qunar1">
										<input type="checkbox" id="channel_qunar" onclick="div();checkboxed(this.value)" name="channel" value="qunar"
										<c:if test="${fn:contains(channelStr, 'qunar' ) }">checked="checked"</c:if> />去哪
									</label>
						</td></tr></table>
					</li>	
					</ul> -->
					
					
					
					<dl class="oz" style="padding-top:20px;">
						<dt style="float:left;padding-left:40px;line-height:22px;">渠道：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</dt>
						<dd  style="float:left;width:900px;">
							<input type="radio" name="channel1" id="channel_qita"  value="0" onclick="div();selectAll();" class="isExchangeDay" style="display:none;"
								<c:if test="${fn:contains(channel1, '0' ) }">checked="checked"</c:if>/> 
								<label for="channel_qita">
								<c:forEach items="${channel_types }" var="s" varStatus="index">
								<c:if test="${s.key ne '301016' && s.key ne '30101601' && s.key ne '30101602'}">
								<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
									<input type="checkbox" id="channel${index.count }" onclick="div();checkboxed(this.value,'${s.key}');" name="channel" value="${s.key }"
										<c:if test="${fn:contains(channelStr, s.key ) }">checked="checked"</c:if> />
										${s.value }
								</div>	
								</c:if>
								</c:forEach>
								<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
									<input type="checkbox" id="channel01" onclick="div();checkboxed(this.value,'${s.key}');" name="channel" value="30101612"
										<c:if test="${fn:contains(channelStr, '30101612') }">checked="checked"</c:if> />
										利安
								</div>
								
								</label>
							<input type="radio" id="channel_qunar1" onclick="div();selectAll();" name="channel1" value="1" class="isExchangeDay" style="display:none;"
								<c:if test="${fn:contains(channel1, '1' ) }">checked="checked"</c:if>	/> 
									<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
									<label for="channel_qunar1">
										<input type="checkbox" id="channel_qunar" onclick="div();checkboxed(this.value)" name="channel" value="qunar"
										<c:if test="${fn:contains(channelStr, 'qunar' ) }">checked="checked"</c:if> />去哪
									</label>
									</div>
							<!-- <div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
									<label for="channel_qita">
										<input type="checkbox" id="channel_elong" onclick="div();checkboxed(this.value,elong)" name="channel" value="elong"
										<c:if test="${fn:contains(channelStr, 'elong' ) }">checked="checked"</c:if> />艺龙
									</label>
									</div> -->
						</dd>
					</dl>
					
					<div id="divcon" style="display:none">
					<ul class="ser oz" >
						<li>
						失败原因：
								<input type="checkbox" onclick="selectAllError()" name="controlAllError" style="controlAllError" id="controlAllError"/>
								<label for="controlAllError">&nbsp;全部&nbsp;&nbsp;&nbsp;&nbsp;</label>
							<input type="checkbox" id="ERROR_INFO_1" name="ERROR_INFO_1" value="ERROR_INFO_1"
									<c:if test="${ERROR_INFO_1 ne '' }">checked="checked"</c:if> />
									<label for="ERROR_INFO_1">所购买的车次坐席已无票</label>
							<input type="checkbox" id="ERROR_INFO_2" name="ERROR_INFO_2" value="ERROR_INFO_2"
									<c:if test="${ERROR_INFO_2 ne '' }">checked="checked"</c:if> />
									<label for="ERROR_INFO_2">身份证件已经实名制购票</label>
							<input type="checkbox" id="ERROR_INFO_3" name="ERROR_INFO_3" value="ERROR_INFO_3"
									<c:if test="${ERROR_INFO_3 ne '' }">checked="checked"</c:if> />
									<label for="ERROR_INFO_3">票价和12306不符</label>
							<input type="checkbox" id="ERROR_INFO_4" name="ERROR_INFO_4" value="ERROR_INFO_4"
									<c:if test="${ERROR_INFO_4 ne '' }">checked="checked"</c:if> />
									<label for="ERROR_INFO_4">乘车时间异常</label>
							<input type="checkbox" id="ERROR_INFO_5" name="ERROR_INFO_5" value="ERROR_INFO_5"
									<c:if test="${ERROR_INFO_5 ne '' }">checked="checked"</c:if> />
									<label for="ERROR_INFO_5">证件错误</label>
							<input type="checkbox" id="ERROR_INFO_6" name="ERROR_INFO_6" value="ERROR_INFO_6"
									<c:if test="${ERROR_INFO_6 ne '' }">checked="checked"</c:if> />
									<label for="ERROR_INFO_6">用户要求取消订单</label>
							<input type="checkbox" id="ERROR_INFO_7" name="ERROR_INFO_7" value="ERROR_INFO_7"
									<c:if test="${ERROR_INFO_7 ne '' }">checked="checked"</c:if> />
									<label for="ERROR_INFO_7">未通过12306实名认证</label>
							<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="checkbox" id="ERROR_INFO_8" name="ERROR_INFO_8" value="ERROR_INFO_8"
									<c:if test="${ERROR_INFO_8 ne '' }">checked="checked"</c:if> />
									<label for="ERROR_INFO_8">乘客身份信息待核验</label>
							<input type="checkbox" id="ERROR_INFO_11" name="ERROR_INFO_11" value="ERROR_INFO_11"
									<c:if test="${ERROR_INFO_11 ne '' }">checked="checked"</c:if> />
									<label for="ERROR_INFO_11">乘客超时未支付</label>
							<input type="checkbox" id="ERROR_INFO_9" name="ERROR_INFO_9" value="ERROR_INFO_9"
									<c:if test="${ERROR_INFO_9 ne '' }">checked="checked"</c:if> />
									<label for="ERROR_INFO_9">系统异常</label>
						</li>
					</ul>
					</div>
					<div id="divqunarcon" style="display:none">
					<ul class="ser oz">
						<li>
						失败原因：
								<input type="checkbox" onclick="selectAllErrorQunar()" name="controlAllErrorQunar" style="controlAllErrorQunar" id="controlAllErrorQunar"/>
								<label for="controlAllErrorQunar">&nbsp;全部&nbsp;&nbsp;&nbsp;&nbsp;</label>
							<c:forEach items="${error_info_qunars }" var="s" varStatus="index">
									<input type="checkbox" id="error_qunar_info${index.count }"
										name="error_qunar_info" value="${s.key }"
										<c:if test="${fn:contains(errorInfoQunarStr, s.key ) }">checked="checked"</c:if> />
									<label for="error_qunar_info${index.count }">
										${s.value }
									</label>
							</c:forEach>
						</li>
					</ul>
					</div> 
					<br/>
        <p><input type="button" value="查 询" class="btn" onclick="submitForm();"/>
        
		 <% if ("2".equals(loginUserVo.getUser_level()) ) {%>
		<input type="button" value="导出Excel" class="btn" onclick="exportExcel()" />
		<%} %>
		<div style="float: right;margin-right: 30px;">
		<span style="font-size:14px;">订单总数：<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${totalCount2 }</span>个</span>
       
		<span style="font-size:14px;">符合条件订单总数：
		<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${totalCount }</span>个</span>
		<span style="font-size:14px;">占比:
		<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${zhanbi}%</span>
		</span>
		</div>
        </p>
        
				</div>
				<div id="hint" class="pub_con" style="display:none"></div>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #EAEAEA;">
							<th>
								NO
							</th>
							<th width="120">
								订单号
							</th>
							<th>
								出发/到达
							</th>
							<th>
								车次
							</th>
							<th width="65">
								乘车时间
							</th>
							<th>
								坐席
							</th>
							<th>
								票价
							</th>
							<th>
								进价
							</th>
							<th width="65">
								创建时间
							</th>
							<th width="65">
								预订时间
							</th>
							<!--  <th>出票方式</th>-->
							<th>
								12306单号
							</th>
							<th>
								渠道
							</th>
							<th>
								级别
							</th>
							<th>
								失败原因
							</th>
							<th>
								操作员
							</th>
							<th>
								操作人
							</th>
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${acquireList}" varStatus="idx">
							<c:choose>
								<c:when
									test="${fn:contains('00,01,11,33,55,66,88,81', list.order_status )}">
									<tr style="background: #BEE0FC;">
								</c:when>
								<c:when test="${fn:contains('61,82,44', list.order_status )}">
									<tr style="background: #E0F3ED;">
								</c:when>
								
								<c:when test="${fn:contains('83,77,85', list.order_status )}">
									<tr style="background: #FFF8DC;">
								</c:when>
								
								<c:otherwise>
									<tr>
								</c:otherwise>
							</c:choose>
							<td>
								${idx.index+1}
							</td>
							<td>
								${list.order_id}
							</td>
							<td>
								${list.from_city}/${list.to_city}
							</td>
							<td>
								${list.train_no }
							</td>
							<td>
								${fn:substringBefore(list.from_time, ' ')}
								<br />
								${fn:substringAfter(list.from_time, ' ')}
							</td>
							<!--  -->
							<td>
								${seat_Types[list.seat_type] }
							</td>
							<c:choose>
								<c:when
									test="${list.buy_money != null && list.buy_money!= list.pay_money}">
									<c:choose>
										<c:when
											test="${list.buy_money != null && list.buy_money > list.pay_money}">
											<td style="color: #f00;">
												<strong>${list.pay_money}</strong>
											</td>
											<td style="color: #f00;">
												<strong>${list.buy_money}</strong>
											</td>
										</c:when>
										<c:otherwise>
											<td style="color: #00f;">
												${list.pay_money}
											</td>
											<td style="color: #00f;">
												${list.buy_money}
											</td>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<td>
										${list.pay_money}
									</td>
									<td>
										${list.buy_money}
									</td>
								</c:otherwise>
							</c:choose>
							<td>
								${fn:substringBefore(list.create_time, ' ')}
								<br />
								${fn:substringAfter(list.create_time, ' ')}
							</td>
							<td>
								${fn:substringBefore(list.out_ticket_time, ' ')}
								<br />
								${fn:substringAfter(list.out_ticket_time, ' ')}
							</td>
							<!-- <td>
			                	<c:if test="${list.out_ticket_type eq '11'}">电子票</c:if>
			                	<c:if test="${list.out_ticket_type eq '22'}">配送票</c:if>
			                </td>-->
							<td>
								${list.out_ticket_billno}
							</td>
							<td>
								${channelTypes[list.channel] }
							</td>
							<c:choose>
								<c:when test="${list.level eq '0'}">
									<td>
										普通
									</td>
								</c:when>
								<c:when test="${empty list.level}">
									<td>
									</td>
								</c:when>
								<c:otherwise>
									<td style="color: red;">
							<!-- 			<c:if test="${list.level !=10 && list.level != 5}">
											<strong>VIP${list.level }</strong>
										</c:if> -->
										<c:if test="${list.level ==10}">
											<strong>联程</strong>
										</c:if>
										<c:if test="${list.level ==5}">
											<strong>SVIP</strong>
										</c:if>
										<c:if test="${list.level ==1}">
											<strong>VIP1</strong>
										</c:if>
										<c:if test="${list.level ==2}">
											<strong>VIP2</strong>
										</c:if>
									</td>
								</c:otherwise>
							</c:choose>
							<td>
								<c:choose>
									<c:when test="${list.channel eq 'qunar'  }">
										${error_info_qunars[list.error_info] }
									</c:when>
									<c:otherwise>
										${error_infos[list.error_info] }
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								${list.worker_name}
							</td>
							<td>
								${list.opt_ren}
							</td>
							<td>
								<span>  
									<c:choose> 
											<c:when test="${fn:contains('82', list.order_status)}"> 
												<a href="javascript:gotoOrderPay('${list.order_id}');">人工处理</a> 
											</c:when> 
											<c:when test="${fn:contains('44', list.order_status)}"> 
												<a href="javascript:gotoOrderPay('${list.order_id}');">人工处理</a> 
												<a href="javascript:gotoRobot('${list.order_id}','${list.create_time }');">机器处理</a> 
											</c:when> 
											<c:when test="${list.order_status eq '66'}">机器人处理中，不能进行干预 
											</c:when> 
											<c:when test="${list.order_status eq '61'}"> 
												<a href="javascript:gotoOrderPayLock('${list.order_id}')">支付</a> 
											</c:when> 
											<c:when test="${list.order_status eq '11'}"> 
												<a href="javascript:submitUpdateStatusInfo('${list.order_id}')">预定重发</a> 
											</c:when>
											<c:when test="${fn:contains('77', list.order_status)}"> 
												<a href="javascript:gotoOrderPay('${list.order_id}');">人工处理</a> 
											</c:when> 
											<c:when test="${fn:contains('10,99', list.order_status)}"> 
											</c:when> 
									</c:choose>  
										</span><div><span><a href="javaScript:void(0)" onclick="queryInfo('${list.order_id}');"  onmouseover="showdiv('${list.order_id}')" onmouseout="hidediv()">明细</a> 
									</span></div>
								</td>
							</tr>
						</c:forEach>
					</table>
					<jsp:include page="/pages/common/paging.jsp" />
					<div id="historyDiv" style="display:none;">
						<table class="pub_table" style="width: 680px; margin: 10px 10px;" id="historyTable">
							<tr>
								<th style="width: 30px;">序号</th>
								<th style="width: 450px;">操作日志</th>
								<th style="width: 130px;">操作时间</th>
								<th style="width: 70px;">操作人</th>
							</tr>
						</table>
					</div>
				</c:if>
			</form>
		</div>
	</body>
</html>
