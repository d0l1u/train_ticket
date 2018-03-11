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
		<title>出票管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
		<script language="javascript" src="/js/json2.js"></script>
		<script type="text/javascript">
	<%
		PageVo pageObject = (PageVo) request.getAttribute("pageBean");
		int pageIndex = pageObject.getPageIndex();
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user_level = loginUserVo.getUser_level();
	%>
	
	function accountMargin(){
		var url = "/remind/queryAccountMarginCount.do?&version ="+new Date();
		$.get(url,function(data){
			var strJSON = data;//得到的JSON
			var obj = new Function("return" + strJSON)();//转换后的JSON对象
			var accountRemind = "",number=0;
			if(obj.l9eAccount<1000){accountRemind+="l9e ";number++;}
			//if(obj.qunarAccount<1000){accountRemind+="去哪 ";number++;}
			//if(obj.elongAccount<1000){accountRemind+="艺龙 ";number++;}
			//if(obj.tcAccount<1000){accountRemind+="同程 ";number++;}
			if(accountRemind != ""){
			document.getElementById("remindDiv").style.display="block";
			document.getElementById("accountRemind").value = accountRemind;
			document.getElementById("accountRemind").style.width = number*37;
			}
		});
	}
	
	function robotAndOrder(){
		var url = "/remind/queryRobotAndOrderCount.do?&version ="+new Date();
		$.get(url,function(data){
			var strJSON = data;//得到的JSON
			var obj = new Function("return" + strJSON)();//转换后的JSON对象
			document.getElementById("orderNumber").value = obj.orderNumber;
			var robotNumber = obj.robotNumber_00 + obj.robotNumber_01 + obj.robotNumber_11;
			document.getElementById("robotNumber").value = robotNumber;
			if(obj.orderNumber + obj.robotOrderRemind <= robotNumber){
			//document.getElementById("remindDiv2").style.display="block";
			}
		});
	}
	
	$().ready(function(){
		//乘车时间排序箭头变化
		var travel=document.getElementById("travel_time_px").value;
		if(travel=="up"){
		document.getElementById("travel_time_img").src="/images/up.png";
		}else if(travel=="down"){
		document.getElementById("travel_time_img").src="/images/down.png";
		}else if(travel==""){
		document.getElementById("travel_time_img").src="/images/normal.png";
		}
		//创建时间排序箭头变化
		var create=document.getElementById("create_time_px").value;
		if(create=="up"){
		document.getElementById("create_time_img").src="/images/up.png";
		}else if(create=="down"){
		document.getElementById("create_time_img").src="/images/down.png";
		}else if(create==""){
		document.getElementById("create_time_img").src="/images/normal.png";
		}
		//发车时间排序箭头变化
		var out_ticket=document.getElementById("out_ticket_time_px").value;
		if(out_ticket=="up"){
		document.getElementById("out_ticket_time_img").src="/images/up.png";
		}else if(out_ticket=="down"){
		document.getElementById("out_ticket_time_img").src="/images/down.png";
		}else if(out_ticket==""){
		document.getElementById("out_ticket_time_img").src="/images/normal.png";
		}
		//statusColor(); channelColor();
		accountMargin();robotAndOrder();
	});
	
	
	
	 //选中变色
     function statusColor(){
		var a=document.getElementsByName("order_status_a");
		var check = document.getElementsByName("order_status");
			for(var i=0;i<a.length;i++){
				a[i].style.color="grey";
				if(check[i].checked){
				a[i].style.color="#FEFEFB";
				a[i].style.backgroundColor="#2C99FF";
				//a[i].style.background='url(/images/pro.png) -90px no-repeat';
				}
		}
	}
	
	//选中变色
     function channelColor(){
     var a=document.getElementsByName("channel_a");
		var check = document.getElementsByName("channel");
			for(var i=0;i<a.length;i++){
				a[i].style.color="grey";
				if(check[i].checked){
				a[i].style.color="#FEFEFB";
				a[i].style.backgroundColor="#2C99FF";
				//a[i].style.background='url(/images/pro.png) -90px no-repeat';
			}
		}
	}

	function queryByStatus(status){
		var check = document.getElementsByName("order_status");
		for(var i=0;i<check.length;i++){
			if(status == "all"){
				check[i].checked = 1;
			}else{
					check[i].checked = 0;
				if(check[i].value == status){
					check[i].checked = 1;
				}
			}
		}
		$("form:first").attr("action","/acquire/queryAcquireList.do?&over_time=0");
		$("form:first").submit();
	}
	
	function queryByChannel(channel){
		var check = document.getElementsByName("channel");
		for(var i=0;i<check.length;i++){
			if(channel == "all"){
				check[i].checked = 1;
			}else{
					check[i].checked = 0;
				if(check[i].value == channel){
					check[i].checked = 1;
				}
			}
		}
		$("form:first").attr("action","/acquire/queryAcquireList.do?&over_time=0");
		$("form:first").submit();
	}	
	
	var objWin; 
	function remind(){
	var url="/remind/queryRemindList.do";
	//showlayer('数据监控:',url,'1100px','400px');
			//判断是否打开 
            if (objWin == null || objWin.closed) {
                objWin = window.open(url); 
            } else { 
                objWin.location.replace(url); 
            } 
	}
	
	var objWin1; 
	function orderChangeManual(){//打开订单转人工处理页面
		var url="/acquire/orderChangeManual.do";
		//showlayer('数据监控:',url,'1100px','400px');
		//判断是否打开
        if (objWin1 == null || objWin1.closed) {
        	 objWin1 = window.open(url);
        } else { 
        	 objWin1.location.replace(url);
        }
	}
	
	/**
	function getYestoday(date){ 
	
		var yesterday_milliseconds=date.getTime()-1000*60*60*24*30;
		var yesterday = new Date();
	    yesterday.setTime(yesterday_milliseconds); 
	    var strYear = yesterday.getFullYear();  
	    var strDay = yesterday.getDate();  
	    var strMonth = yesterday.getMonth()+1;
	    if(strMonth<10){     
	   		 strMonth="0"+strMonth;  
	    }   
	    datastr = strYear+"-"+strMonth+"-"+strDay;  
	    return datastr; 
    }   
    **/
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
	//得到当前日期，时间格式为"yyyy-MM-dd"
	function TodayFormat(){
		var date = new Date();
		var year = date.getFullYear();
		var month = date.getMonth()+1; //js从0开始取 
		month = month<10 ? "0"+month : month;
		var date1 = date.getDate(); 
		date1 = date1<10 ? "0"+date1 : date1;
		var now = year+"-"+month+"-"+date1;
		//alert(date+" | "+now );
		return now;
	}
	//得到第二天日期，时间格式为"yyyy-MM-dd"
	function TomorrowFormat(){
		var dd = new Date(); 
		dd.setDate(dd.getDate()+1);//获取1天后的日期 
		var y = dd.getYear(); 
		var m = dd.getMonth()+1;//获取当前月份的日期 
		m = m<10 ? "0"+m : m;
		var d = dd.getDate(); 
		d = d<10 ? "0"+d : d;
		var tomorrow = y+"-"+m+"-"+d;
		//alert(tomorrow);
		return tomorrow; 
	}
	//比较两个时间的大小,时间格式为"yyyy-MM-dd hh:mm:ss"
	function compareTime(beginTime,endTime){
		var beginTimes = beginTime.substring(0, 10).split('-');
		var endTimes = endTime.substring(0, 10).split('-');

	 	beginTime = beginTimes[1] + '-' + beginTimes[2] + '-' + beginTimes[0] + ' ' + beginTime.substring(10, 19);
		endTime = endTimes[1] + '-' + endTimes[2] + '-' + endTimes[0] + ' ' + endTime.substring(10, 19);

		var a = (Date.parse(endTime ) - Date.parse(beginTime)) / 3600 / 1000;
		if (a < 0) {
		    return 'false';//endTime 
		} else if (a > 0) {
			return 'true';//endTime大 
		} else if (a == 0) {
			return 'equal';//时间相等 
		} else {
			return 'exception';
		}
	}
	//判断提交的订单的发车时间
	function judgmentFromTime(obj){
		var selectedTr = obj;
		var create_time = selectedTr.cells[9].innerHTML; //提交订单时间
		var from_time = selectedTr.cells[4].innerHTML;	 //发车时间
		var c1 = TodayFormat() + ' 23:00:00';//提交订单时间--当天晚上23:00:00
		var c2 = TomorrowFormat() + ' 07:00:00';//提交订单时间--第二天早上07:00:00
		var f = TomorrowFormat() + ' 09:00:00';//发车时间--第二天早上09:00:00
		var a = compareTime(create_time,c1);//若a为true则提交订单的时间在23点以后  create_time>c1
		var b = compareTime(create_time,c2);//若a为false则提交订单的时间在7点以前  create_time<c2
		var c = compareTime(from_time,f);  //from_time<f
		//alert(from_time+" "+f+" "+ c);
		//alert(create_time+" "+c1+" "+ b);
		if( a=='false' && b=='true' && c=='true' ){
			alert("亲爱的代理商：您好！\n    由于官网处理订单的时间是早7:00—晚23:00，故晚23:00以后在网上订不了早9点以前的票，"
					+"如果一定要订早9点以前发车的票，请携带二代身份证件去车站办理买票。\n"+
					"    您的发车时间是："+from_time+"，您的提交订单时间是："+create_time+"。");
		}
	}
	
	//人工补单
	function gotoBudan(order_id){
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
				 url="/acquire/queryAcquireInfo.do?order_id="+order_id+"&query_type=1&budan=1";
				 showlayer('人工处理',url,'1000px','800px')
			 }
		});
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
				var  uri = "/acquire/updateAcquireToRobot.do?order_id="+order_id+"&order_status=01&create_time="+create_time+"&version="+new Date();
				$.post(uri,function(data){
					if(data=="yes"){
						$("form:first").attr("action","/acquire/queryAcquireList.do?pageIndex="+<%=pageIndex%>);
						$("form:first").submit();
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
	function switchRefresh(obj){
		if(!$(obj).attr("checked")) {
			auto_scan=0;
			setCookie("auto_flag", "0");
			$("#switcher").html("开启自动刷新");
		}else{
			auto_scan=1;
			setCookie("auto_flag", "1");
			$("#switcher").html("关闭自动刷新");
		}
	}
	
	function setCookie(name,value){
	    var Days = 30;
	    var exp = new Date();
	    exp.setTime(exp.getTime() + Days*24*60*60*1000);
	    document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
	}
	
	//读取cookies
	function getCookie(name){
	    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
	    if(arr=document.cookie.match(reg))
	        return (arr[2]);
	    else
	        return null;
	}
	
	$().ready(function(){
		var auto_flag=getCookie("auto_flag");
		if(auto_flag==null || auto_flag=="0"){
			auto_scan=0;
			$("#auto_check").attr("checked", false);
		}else{
			auto_scan=1;
			$("#auto_check").attr("checked", true);
		}
	});
	function submitUpdateStatusInfo(order_id){
			var  url = "/acquire/updateInfoOrderStatus.do?order_id="+order_id+"&version="+new Date();
				$.post(url,function(data){
					if(data=="yes"){
						$("form:first").attr("action","/acquire/queryAcquireList.do?pageIndex="+<%=pageIndex%>);
						$("form:first").submit();
					}else{
						alert("预定重发请求失败");
					}
				});
	}
	//重新支付
	function updateStatusTo55(order_id, order_status){
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
				var url = "/acquire/updateInfoOrderStatusTo55.do?order_id="+order_id+"&order_status="+order_status+"&version="+new Date();
				$.post(url,function(data){
					if(data=="yes"){
						$("form:first").attr("action","/acquire/queryAcquireList.do?pageIndex="+<%=pageIndex%>);
						$("form:first").submit();
					}else{
						if(order_status==55){
							alert("重新支付请求失败");
						}else if(order_status==88){
							alert("支付完成请求失败");
						}
					}
				});
			}
		});
	}
	
	function queryInfo(order_id){
		var url="/acquire/queryAcquireInfo.do?order_id="+order_id+"&query_type=mingxi";
		showlayer('明细',url,'1000px','800px')
		}

	function submitForm(){
		$("form:first").attr("action","/acquire/queryAcquireList.do?over_time=0");
		$("form:first").submit();
		}

	function selectAll(){
		var checklist = document.getElementsByName("order_status");
		if(document.getElementById("controlAll").checked){
			for(var i=0; i<checklist.length; i++){
				checklist[i].checked = 1;
			}
		}else{
			for(var j=0; j<checklist.length; j++){
				checklist[j].checked = 0;
			}

		}
	}
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
	//全选操作
	function checkChnRetRuleAll(){
	      var order_id = document.getElementsByName("order_id");
	      var tag1 = document.getElementById("tag1").value;
	      if(tag1 == 0 ){
	         for(var i = 0 ; i < order_id.length ; i++){
	        	 order_id[i].checked = true ;
	         }
	         document.getElementById("tag1").value = 1;
	      }else if(tag1 == 1){
	         for(var i = 0 ; i < order_id.length ; i++){
	        	 order_id[i].checked = false ;
	         }
	         document.getElementById("tag1").value = 0;
	      }
	}
	
	
	var jsonArr = [];
	//批量机器处理  
	function submitBatchToRobot(){
		var orderIdStr = "";
		var orderIdNum = 0;
		var str = "";
		$("input[name='order_id']:checkbox:checked").each(function(){ //遍历被选中CheckBox元素的集合 得到Value值
			var order_id = $(this).val();
			if(($("#orderStatus_"+order_id).val()==44 ||$("#orderStatus_"+order_id).val()=="AA")&& $("#manualOrder_"+order_id).val()!="22" ){//订单状态为人工预定44
				//验证订单是否加锁
				var url = "/acquire/queryPayIsLock.do?order_id="+$(this).val()+"&version="+new Date();
				$.get(url,function(data){
					if(data != null && data != ""){
						var temp = data ;
						var str1 = temp.split("&") ;
						var str =str1[1]; 
						//alert("订单"+$(this).val()+"已经锁定，锁定人为"+str);
						//continue;
					 }else{//订单被选中，状态为44，未加锁的订单
						var create_time = $("#createTime_"+order_id).val();//得到该订单的创建时间
						var json = {"order_id": order_id, "create_time": create_time};
						jsonArr.push(JSON.stringify(json));
					 }
				});
				orderIdNum++;
			}
		}); 
		if(orderIdNum==0){
			alert("没有选中符合条件的订单!");
			return false;
		}
		 var orderStr = "";
		$("input[name='order_status']:checkbox:checked").each(function(){ 
				orderStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
			}); 
			orderStr = orderStr.substring(0, orderStr.length-1);
		 var channelStr = "";
		$("input[name='channel']:checkbox:checked").each(function(){ 
				channelStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
			}); 
			channelStr = channelStr.substring(0, channelStr.length-1);	
		if(confirm("确认批量机器处理吗 ？")){
		//时间排序
		var travel=document.getElementById("travel_time_px").value;
		var create=document.getElementById("create_time_px").value;
		var out_ticket=document.getElementById("out_ticket_time_px").value;
		var paixu = "&travel_time_px="+travel+"&out_ticket_time_px="+out_ticket+"&create_time_px="+create;
			document.queryFrm.action="/acquire/updateBatchToRobot.do?pageIndex="+<%=pageIndex%>+paixu+"&statusList="+orderStr+"&channelList="+channelStr+"&jsonArr="+jsonArr+"&version="+new Date();
			document.queryFrm.submit();
		}
	}
	
	//批量切换订单帐号
	var jsonArrSwitch = [];
	function submitSwitchOrderAccount(){
		
		var orderIdStr = "";
		var orderIdNum = 0;
		var str = "";
		$("input[name='order_id']:checkbox:checked").each(function(){ //遍历被选中CheckBox元素的集合 得到Value值
			var order_id = $(this).val();
			if(($("#orderStatus_"+order_id).val()==44 ||$("#orderStatus_"+order_id).val()=="AA")&& $("#manualOrder_"+order_id).val()!="22" && $("#accountSource_"+order_id).val()=="0"){//订单状态为人工预定44,公司自有帐号
				//验证订单是否加锁
				var url = "/acquire/queryPayIsLock.do?order_id="+$(this).val()+"&version="+new Date();
				$.get(url,function(data){
					if(data != null && data != ""){
						var temp = data ;
						var str1 = temp.split("&") ;
						var str =str1[1]; 
						//alert("订单"+$(this).val()+"已经锁定，锁定人为"+str);
						//continue;
					 }else{//订单被选中，状态为44，未加锁的订单
						var create_time = $("#createTime_"+order_id).val();//得到该订单的创建时间
						var json = {"order_id": order_id, "create_time": create_time};
						jsonArrSwitch.push(JSON.stringify(json));
					 }
				});
				orderIdNum++;
			}
		}); 
		if(orderIdNum==0){
			alert("没有选中符合条件的订单!");
			return false;
		}
		
		var orderStr = "";
		$("input[name='order_status']:checkbox:checked").each(function(){ 
				orderStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
			}); 
			orderStr = orderStr.substring(0, orderStr.length-1);
		 var channelStr = "";
		$("input[name='channel']:checkbox:checked").each(function(){ 
				channelStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
			}); 
			channelStr = channelStr.substring(0, channelStr.length-1);
			
		
		if(confirm("确认批量切换处理吗 ？")){
				//时间排序
				var travel=document.getElementById("travel_time_px").value;
				var create=document.getElementById("create_time_px").value;
				var out_ticket=document.getElementById("out_ticket_time_px").value;
				var paixu = "&travel_time_px="+travel+"&out_ticket_time_px="+out_ticket+"&create_time_px="+create;
				var url="/acquire/updateBatchToSwitchAccount.do?pageIndex="+<%=pageIndex%>+paixu+"&statusList="+orderStr+"&channelList="+channelStr+"&jsonArr="+jsonArrSwitch+"&version="+new Date();
				alert("请等待提示框出现，然后操作，时间大约："+orderIdNum*1+"秒");
				$.get(url, function(data) {
					alert(data);
					$("form:first").submit();
				});
		}
	
	}
	
	
	//批量重新支付
	var jsonArrPay = [];
	function submitBatchToPay(){
		var orderIdStr = "";
		var orderIdNum = 0;
		var str = "";
		$("input[name='order_id']:checkbox:checked").each(function(){ //遍历被选中CheckBox元素的集合 得到Value值
			var order_id = $(this).val();
			if($("#orderStatus_"+order_id).val()==61 && $("#manualOrder_"+order_id).val()!="22" ){//订单状态为人工支付61
				//验证订单是否加锁
				var url = "/acquire/queryPayIsPayLock.do?order_id="+order_id+"&version="+new Date();
				$.get(url,function(data){
					if(data != null && data != ""){
						var temp = data ;
						var str1 = temp.split("&") ;
						var str =str1[1]; 
						//alert("此订单已经锁定，锁定人为"+str);
						//continue;
					 }else{//订单被选中，状态为61，未加锁的订单
						var create_time = $("#createTime_"+order_id).val();//得到该订单的创建时间
						var json = {"order_id": order_id, "create_time": create_time};
						jsonArrPay.push(JSON.stringify(json));
					 }
				});
				orderIdNum++;
			}
		}); 
		if(orderIdNum==0){
			alert("请选中一个状态为【人工支付】的非携程出票订单!");
			return false;
		}
		 var orderStr = "";
		$("input[name='order_status']:checkbox:checked").each(function(){ 
				orderStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
			}); 
			orderStr = orderStr.substring(0, orderStr.length-1);
		 var channelStr = "";
		$("input[name='channel']:checkbox:checked").each(function(){ 
				channelStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
			}); 
			channelStr = channelStr.substring(0, channelStr.length-1);
		if(confirm("确认批量重新支付吗 ？")){
		//时间排序
		var travel=document.getElementById("travel_time_px").value;
		var create=document.getElementById("create_time_px").value;
		var out_ticket=document.getElementById("out_ticket_time_px").value;
		var paixu = "&travel_time_px="+travel+"&out_ticket_time_px="+out_ticket+"&create_time_px="+create;
			document.queryFrm.action="/acquire/updateBatchToPay.do?pageIndex="+<%=pageIndex%>+paixu+"&statusList="+orderStr+"&channelList="+channelStr+"&jsonArrPay="+jsonArrPay+"&version="+new Date();
			document.queryFrm.submit();
		}
	}
	
	//批量反支
	var jsonArrAnomalyPay = [];
	function submitBatchAnomalyPay(){
		var orderIdStr = "";
		var orderIdNum = 0;
		var str = "";
		$("input[name='order_id']:checkbox:checked").each(function(){ //遍历被选中CheckBox元素的集合 得到Value值
			var order_id = $(this).val();
			if($("#orderStatus_"+order_id).val()==61 && $("#manualOrder_"+order_id).val()!="22" ){//订单状态为人工支付61
				//验证订单是否加锁
				var url = "/acquire/queryPayIsPayLock.do?order_id="+order_id+"&version="+new Date();
				$.get(url,function(data){
					if(data != null && data != ""){
						var temp = data ;
						var str1 = temp.split("&") ;
						var str =str1[1]; 
						//alert("此订单已经锁定，锁定人为"+str);
						//continue;
					 }else{//订单被选中，状态为61，未加锁的订单
						var create_time = $("#createTime_"+order_id).val();//得到该订单的创建时间
						var json = {"order_id": order_id, "create_time": create_time};
						jsonArrAnomalyPay.push(JSON.stringify(json));
					 }
				});
				orderIdNum++;
			}
		}); 
		if(orderIdNum==0){
			alert("请选中一个状态为【人工支付】的非携程出票订单!");
			return false;
		}
		 var orderStr = "";
		$("input[name='order_status']:checkbox:checked").each(function(){ 
				orderStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
			}); 
			orderStr = orderStr.substring(0, orderStr.length-1);
		 var channelStr = "";
		$("input[name='channel']:checkbox:checked").each(function(){ 
				channelStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
			}); 
			channelStr = channelStr.substring(0, channelStr.length-1);
		if(confirm("确认批量反支吗 ？")){
		//时间排序
		var travel=document.getElementById("travel_time_px").value;
		var create=document.getElementById("create_time_px").value;
		var out_ticket=document.getElementById("out_ticket_time_px").value;
		var paixu = "&travel_time_px="+travel+"&out_ticket_time_px="+out_ticket+"&create_time_px="+create;
			document.queryFrm.action="/acquire/updateBatchAnomalyPay.do?pageIndex="+<%=pageIndex%>+paixu+"&statusList="+orderStr+"&channelList="+channelStr+"&jsonArrPay="+jsonArrAnomalyPay+"&version="+new Date();
			document.queryFrm.submit();
		}
	}
	
	
	function exportExcel() {
			$("form:first").attr("action","/acquire/exportexcel.do");
			$("form:first").submit();
			$("form:first").attr("action","/acquire/queryAcquireList.do");
	}
		
		
	//批量出票失败开始
	function submitFailOrder(){
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
			submitFailOrderPage(channel);
	}
	
 	function submitFailOrderPage(channel){
		var str = "";
		var orderIdStr="";
		var orderIdNum = 0;
		$("input[name='order_id']:checkbox:checked").each(function(){ //遍历被选中CheckBox元素的集合 得到Value值
			var order_id = $(this).val();
			if(($("#orderStatus_"+order_id).val()==44 ||$("#orderStatus_"+order_id).val()==77 ||$("#orderStatus_"+order_id).val()==83)&& $("#manualOrder_"+order_id).val()!="22" ){//订单状态为人工预订44
				//验证订单是否加锁
				var url1 = "/acquire/queryPayIsPayLock.do?order_id="+order_id+"&version="+new Date();
				$.get(url1,function(data){
					if(data != null && data != ""){
						var temp = data ;
						var str1 = temp.split("&") ;
						var str =str1[1]; 
					 }else{//订单被选中，状态为61，未加锁的订单
						orderIdStr+=order_id+",";
					 }
				});
				orderIdNum++;
			}
		}); 
		if(orderIdNum==0){
			alert("只有人工预订、正在取消、和取消失败的订单才可以批量失败!");
			return false;
		}
		orderIdStr = orderIdStr.substring(0, orderIdStr.length-1);
		if(confirm("确认批量失败吗 ？")){
			var url2="/acquire/queryAcquirePlFail.do?orderIdStr="+orderIdStr+"&channel="+channel+"&version="+new Date();
			showlayer('请选择失败原因:',url2,'500px','300px');
			}
}
	//批量机器处理结束

	//排序
	function changeInTurn(type){
		if(type=="travel"){
		var value=null;
		var px=document.getElementById("travel_time_px").value;
		if(px=="" || px=="down"){
			value="up";
		}else if(px=="up"){
			value="down";
		}
		$("form:first").attr("action","/acquire/queryAcquireList.do?over_time=0&travel_time_px="+value);
		$("form:first").submit();
		}else if(type=="create"){
		var value=null;
		var px=document.getElementById("create_time_px").value;
		if(px=="" || px=="down"){
			value="up";
		}else if(px=="up"){
			value="down";
		}
		$("form:first").attr("action","/acquire/queryAcquireList.do?over_time=0&create_time_px="+value);
		$("form:first").submit();
		}else{
		var value=null;
		var px=document.getElementById("out_ticket_time_px").value;
		if(px=="" || px=="down"){
			value="up";
		}else if(px=="up"){
			value="down";
		}
		$("form:first").attr("action","/acquire/queryAcquireList.do?over_time=0&out_ticket_time_px="+value);
		$("form:first").submit();
		}
	}
	
	//携程机器处理 
	function gotoCtripRobot(order_id,create_time,manual_order){
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
			 var str ="";
			 if(manual_order == '22'){
			 str = "携程处理";
			 }else{
			 str = "正常处理";
			 }
			 if(confirm("是否"+str+"？")){
				var  uri = "/acquire/updateAcquireToRobot.do?order_id="+order_id+"&order_status=01&create_time="+create_time+"&manual_order="+manual_order+"&version="+new Date();
				$.post(uri,function(data){
					if(data=="yes"){
						$("form:first").attr("action","/acquire/queryAcquireList.do?pageIndex="+<%=pageIndex%>);
						$("form:first").submit();
					}else{
						alert("机器人处理请求失败");
					}
				});
			 }
		 	 }
		});
	}
	
	
	//隐藏
	function showmore(){
		if(document.getElementById("showmore").style.display == "none"){
			document.getElementById("showmore").style.display = "block";
			document.getElementById("showmore1").style.display = "block";
		}else{
			document.getElementById("showmore").style.display = "none";
			document.getElementById("showmore1").style.display = "none";
		}
	}
	
	
	//手机核验处理 
	function phoneCheck(order_id,create_time){
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
			/*  if(confirm("是否【手机核验】处理？")){ */
				var url="/acquire/phoneCheck.do";
				$.post(url,{order_id:order_id},function(data){
					if(data!=null && data=="yes"){
					gotoRobot(order_id,create_time);
					}else{
						alert("当前没有空余账号！");
					}
				});
			/*  } */
		 	 }
		});
	}
	
	
</script>
<style>
	#refresh_span a:link,#refresh_span a:visited{color:#2ea6d8;}
	#refresh_span a:hover{text-decoration:underline;}
	#hint{width:700px;border:1px solid #C1C1C1;background:#F0FAC1;position:fixed;z-index:9;padding:6px;line-height:17px;text-align:left;overflow:auto;} 
</style>

	</head>
	<body>
		<div></div>
		<div class="book_manage oz">
			<form action="/acquire/queryAcquireList.do?travel_time_px=${travel_time_px }&out_ticket_time_px=${out_ticket_time_px }&create_time_px=${create_time_px }" method="post" name="queryFrm">
				<div style="border: 0px solid #00CC00; margin: 10px;">
					<ul class="order_num oz" style="margin-top: 10px;">
						<li>
							订单号：&nbsp;&nbsp;&nbsp;
							<input type="text" class="text" name="order_id"
								value="${order_id }" />
								<input type="hidden" id="travel_time_px" value="${travel_time_px }" />
								<input type="hidden" id="out_ticket_time_px" value="${out_ticket_time_px }" />
								<input type="hidden" id="create_time_px" value="${create_time_px }" />
						</li>
						<li>
							开始时间：&nbsp;
							<input type="text" class="text" name="begin_info_time" id="begin_info_time"
								readonly="readonly" value="${begin_info_time }"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})" id="defaultStartDate1"/>
						</li>
						<li>
							结束时间：&nbsp;
							<input type="text" class="text" name="end_info_time"
								readonly="readonly" value="${end_info_time }"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})" />
						</li>
						<li></li>
						<li>
							12306单号：
							<input type="text" class="text" name="out_ticket_billno"
								value="${out_ticket_billno }" />
						</li>
					</ul>
					
					<ul class="order_num oz" style="margin-top: 10px;display: none;" id="showmore" >
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
						
						if ("2".equals(loginUserVo.getUser_level())) {
					%>
						<li>
							&nbsp;&nbsp;操作人：&nbsp;
							<input type="text" class="text" name="opt_ren" value="${opt_ren}" />
						</li>
						<li>
							&nbsp;&nbsp;乘车人：&nbsp;
							<input type="text" class="text" name="user_name" value="${user_name}" />
						</li>
						
						<%} %>
						<li>
							订单状态：&nbsp;
							<select name="pro_bak2" style="width:170px;">
								<option value="" <c:if test="${empty pro_bak2}">selected</c:if>>
									全部
								</option>
								<option value="00"
									<c:if test="${!empty pro_bak2 && pro_bak2 eq '00'}">selected</c:if>>
									正常订单
								</option>
								<option value="11"
									<c:if test="${!empty pro_bak2 && pro_bak2 eq '11'}">selected</c:if>>
									补充订单
								</option>
							</select>
						</li>
					</ul>
					<ul class="order_num oz" style="margin-top: 10px;display: none;" id="showmore1" >
						<li>
							出票渠道：&nbsp;
							<select name="manual_order2" style="width:170px;">
								<option value="" <c:if test="${empty manual_order2}">selected</c:if>>
									全部
								</option>
								<option value="00"
									<c:if test="${!empty manual_order2 && manual_order2 eq '00'}">selected</c:if>>
									12306
								</option>
								<option value="22"
									<c:if test="${!empty manual_order2 && manual_order2 eq '22'}">selected</c:if>>
									携程
								</option>
							</select>
						</li>
					</ul>
					<div  style="margin-top: 10px;padding-left:35px">
					<span style="color:blue;cursor: pointer;" onclick="showmore();">
					更多操作</span>
					
					
					</div>
					<%
						
						if ("2".equals(loginUserVo.getUser_level())
								|| "1".equals(loginUserVo.getUser_level()) ) {
					%>
					<dl class="oz" style="padding-top:10px;" id="statuslist">
						<dt style="float:left;padding-left:40px;line-height:50px;">
						状态：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</dt>
						
						<dd  style="float:left;width:80px;line-height:20px;">
							<div class="ser-item" style="float:left;white-space:nowrap;padding-right:20px;">
								<input type="checkbox" onclick="selectAll()" name="controlAll" style="controlAll" id="controlAll"/><label for="controlAll">&nbsp;全部&nbsp;&nbsp;&nbsp;&nbsp;</label>
							</div>
							</dd>
						
						 <!-- 
						 <dd  style="float:left;width:60px;line-height:20px;">
							 <div class="ser-item" style="float:left;white-space:nowrap;padding-right:20px;">
							 	<a href="javascript:queryByStatus('all');" id="order_status_all" name="order_status_a_all"
							 	style="color:grey;">&nbsp;全部&nbsp;&nbsp;&nbsp;&nbsp;</a>
							 </div>
						 </dd>
						  -->
						<c:forEach items="${acquireStatus }" var="s" varStatus="index">
						<dd  style="float:left;width:80px;line-height:20px;">
							<div class="ser-item" style="float:left;white-space:nowrap;padding-right:20px;">
								<input type="checkbox" id="order_status${index.count }" name="order_status" value="${s.key }" value="1"
									<c:if test="${fn:contains(statusStr, s.key ) }">checked="checked"</c:if> />
									<label>${s.value }</label>
								 <!--
								<a href="javascript:queryByStatus('${s.key }');" id="order_status${index.count }" name="order_status_a">${s.value }</a>
								-->
							</div>
						</dd>
						</c:forEach>
						<!-- 
						<c:forEach items="${acquirePayType }" var="s" varStatus="index">
						<div class="ser-item" style="float:left;white-space:nowrap;padding-right:20px;">
						<input type="checkbox" id="pay_type${index.count }" name="pay_type" value="${s.key }" value="1"
									<c:if test="${fn:contains(pay_typeStr, s.key )}">checked="checked"</c:if> />
						<label for="pay_type${index.count }">
									${s.value }
								</label>
							</div>
						</c:forEach>
						 -->
					</dl>
					
					<%
						}
					%>
					<dl class="oz" style="padding-top:10px;" >
						<dt style="float:left;padding-left:38px;line-height:100px;padding-bottom: 20px;">
						渠道：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</dt>
						  <dd  style="float:left;width:80px;line-height:20px;">
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="checkbox" onclick="selectAllChannel()" name="controlAllChannel" style="controlAllChannel" id="controlAllChannel"/><label for="controlAllChannel">&nbsp;全部&nbsp;&nbsp;</label>
							</div>
						 </dd>
						 <!--
						 <dd  style="float:left;width:60px;line-height:20px;">
							 <div class="ser-item" style="float:left;white-space:nowrap;padding-right:20px;">
							 	<a href="javascript:queryByChannel('all');" id="channel_all" name="channel_a_all"
							 	style="color:grey;">&nbsp;全部&nbsp;&nbsp;&nbsp;&nbsp;</a>
							 </div>
						 </dd>
						  -->
						<c:forEach items="${channel_types }" var="s" varStatus="index">
						<dd  style="float:left;width:80px;line-height:20px;">
						<c:if test="${s.key ne '301016' && s.key ne '30101601' && s.key ne '30101602'}">
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="checkbox" id="channel${index.count }" name="channel" value="${s.key }" value="1"
									<c:if test="${fn:contains(channelStr, s.key ) }">checked="checked"</c:if>  />
									<label>${s.value }</label>
								 <!--
								<a href="javascript:queryByChannel('${s.key }');" id="channel${index.count }" name="channel_a">${s.value }</a>
								  -->
							</div>
						</c:if>
						</dd>
						</c:forEach>
						<dd  style="float:left;width:60px;line-height:20px;">
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
							<input type="checkbox" id="channel01" name="channel" value="30101612"
								<c:if test="${fn:contains(channelStr, '30101612')}">checked="checked"</c:if> />
								<label>利安</label>
								 <!--
								<a href="javascript:queryByChannel('30101612');" id="channel01" name="channel_a">利安</a>
								-->
							</div>
						</dd>
					</dl>
		<div id="remindDiv" style="color:red;font-size: 15px;font-weight: bold;display: none;padding-left:50px; margin: 10px;">
			以下渠道：<input value="" id="accountRemind" style="color:#f60;font-size: 15px;font-weight: bold;border:0;" readonly="readonly"/>，账号可用余量不足1000,请尽快添加账号！！！
		</div>
		<div id="remindDiv2" style="color:red;font-size: 15px;font-weight: bold;display: none;padding-left:50px; margin: 10px;">
			每分钟分单量<input value="" id="orderNumber" style="color:#f60;width:50px;border:0;font-size: 15px;font-weight: bold;"/>，启用预订机器人个数<input value="" id="robotNumber" style="font-size: 15px;font-weight: bold;color:#f60;width:50px;border:0;"/> ！！！
		</div>
        <p>
         <input type="button" value="查 询" class="btn" onclick="submitForm();"/>
         <input type="button" value="批量机器处理" onclick="submitBatchToRobot();" class="btn" />
		 <input type="button" value="批量支付" onclick="submitBatchToPay();" class="btn" />
		 <input type="button" value="批量反支" onclick="submitBatchAnomalyPay();" class="btn" />
		 <%		if ("2".equals(loginUserVo.getUser_level()) ) {%>
		<input type="button" value="导出Excel" class="btn" onclick="exportExcel()" />
		<%} %>
		 <input type="button" value="批量失败" onclick="submitFailOrder();" class="btn" />  
		 <input type="button" value="批量切换" onclick="submitSwitchOrderAccount();" class="btn" />  
         <span style="padding-left:0px;" id="refresh_span">
          <input type="checkbox" id="auto_check" onclick="switchRefresh(this);" /></span>
          <span id="left_time" style="display:none;">8</span>自动刷新
          <a href="/acquire/queryAcquirePage.do">人工处理</a>&nbsp;
          <a href="/acquire/queryAcquireList.do?order_status=00&order_status=01&order_status=05&order_status=11&order_status=15&order_status=33&order_status=46&order_status=55&order_status=66&order_status=88&order_status=81&over_time=0">正在处理</a>&nbsp;
          <%  if ("2".equals(loginUserVo.getUser_level())){ %>
           	<a href="javascript:remind();" style="color:red;">数据监控</a>&nbsp;
          <%  } %>
          <a href="javascript:orderChangeManual();">订单转人工</a>&nbsp;
        </p>
       
				</div>
				<div id="hint" class="pub_con" style="display:none"></div>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #EAEAEA;">
							<th style="width:30px;">全选 <br/><input type="checkbox" id="checkChnRetRulAll" name="checkChnRetRulAll" onclick="checkChnRetRuleAll()"/></th>
							<th>
								NO
							</th>
							<th width="110px">
								订单号
							</th>
							<th width="65px">
								出发/到达
							</th>
							<th width="36px">
								车次
							</th>
							<th width="68px">
							<span id="px"  onclick="changeInTurn('travel')"style="cursor:pointer">
								乘车时间
								<img src="" alt="" name="travel_time_img" id="travel_time_img"  style=" display:inline-block; margin: auto -8px;"/>
							</span>
							</th>
							<th width="40px">
								坐席
							</th>
							<th>
								票价
							</th>
							<th>
								进价
							</th>
							<th width="50px">
								状态
							</th>
							<th width="68px">
							<span id="px"  onclick="changeInTurn('create')" style="cursor:pointer">
								创建时间
								<img src="" alt="" name="create_time_img" id="create_time_img"  style=" display:inline-block;margin: auto -8px;"/>
							</span>
							</th>
							<th width="30px">
								时长
							</th>
							<th width="68px">
							<span id="px"  onclick="changeInTurn('out_ticket')"style="cursor:pointer">
								预订时间
								<img src="" alt="" name="out_ticket_time_img" id="out_ticket_time_img" style=" display:inline-block;margin: auto -8px;"/>
							</span>
							</th>
							<th width="60px">
								出票时间
							</th>
							<th>
								12306单号
							</th>
							<th width="30px">
								渠道
							</th>
							<th>
								级别
							</th>
							<th width="30px">
								出票
							</th>
							<th width="45px">
								操作人
							</th>
							
							<th>
								返回日志
							</th>
							<th width="120px">
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${acquireList}" varStatus="idx">
							<tr
									<c:if test="${fn:contains('00,01,15,11,33,55,66,88,81', list.order_status )}">
										 style="background: #BEE0FC;"
									</c:if>
									<c:if test="${fn:contains('61,82,44,AA', list.order_status )}">
										 style="background: #E0F3ED;"
									</c:if>
									<c:if test="${fn:contains('83,77,85', list.order_status )}">
										 style="background: #FFF8DC;"
									</c:if>
							>
							<td>
								<input type="checkbox" id="order_id" name="order_id" value="${list.order_id }"/>
								<input type="hidden" id="orderStatus_${list.order_id }" value="${list.order_status }" />
								<input type="hidden" id="createTime_${list.order_id }" value="${list.create_time }" />
								<input type="hidden" id="manualOrder_${list.order_id }" value="${list.manual_order }" />
								<input type="hidden" id="accountSource_${list.order_id }" value="${list.account_from_way}" />
							</td>
							<td>
								${idx.index+1}
							</td>
							<td
								<c:if test="${list.manual_order eq '22' }">
									 style="color: #F88E2D;"
								</c:if>
							>
								${list.order_id}
							</td>
							<td>
								${list.from_city}/${list.to_city}
							</td>
							<td style="word-break:break-all;">
								${list.train_no }
							</td>
							<td>
							<c:if test="${list.from_time ne null}">
								${fn:substringBefore(list.from_time, ' ')}
								<br />
								${fn:substringAfter(list.from_time, ' ')}
							</c:if>
							<c:if test="${list.from_time eq null}">
								${list.travel_time}
							</c:if>
							
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
								${acquireStatus[list.order_status] }
							</td>
							<td>
								${fn:substringBefore(list.create_time, ' ')}
								<br />
								${fn:substringAfter(list.create_time, ' ')}
							</td>
							<c:choose>
								<c:when test="${list.time_out ge '5.0' && list.time_out le '1440.0'}">
									<td style="color:red;">
									${list.time_out}
									</td>
								</c:when>
								<c:when test="${list.time_out ge '1440.0'}">
									<td>
									超时
									</td>
								</c:when>
								<c:otherwise>
									<td>
									${list.time_out}
									</td>
								</c:otherwise>
							</c:choose>
							
							<td>
								${fn:substringBefore(list.out_ticket_time, ' ')}
								<br />
								${fn:substringAfter(list.out_ticket_time, ' ')}
							</td>
							<td>
								${fn:substringBefore(list.pay_time, ' ')}
								<br />
								${fn:substringAfter(list.pay_time, ' ')}
							</td>
							<!-- <td>
			                	<c:if test="${list.out_ticket_type eq '11'}">电子票</c:if>
			                	<c:if test="${list.out_ticket_type eq '22'}">配送票</c:if>
			                </td>-->
							<td>
								${list.out_ticket_billno}
							</td>
							<td>
								<c:choose>
									<c:when test="${! empty channel_types[list.channel] }">
										${channel_types[list.channel] }
									</c:when>
									<c:otherwise>
										${merchantMap[list.channel] }
									</c:otherwise>
								</c:choose>
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
							<td  style ="color:#F88E2D;">
								${manualOrder[list.manual_order]}
								<c:if test="${list.device_type ==1}">
									<strong>app</strong>
								</c:if>
								<c:if test="${list.device_type ==0}">
									<strong>PC</strong>
								</c:if>
							</td>
							<td>
								${list.opt_ren}
							</td>
							<td>
								${returnlogMap[list.return_optlog] }
							</td>
							<td>
								<span>  
									<c:if test="${list.manual_order eq '22' && list.order_status eq '44'}">
									<a href="javascript:gotoOrderPay('${list.order_id}');" style="color: #F88E2D;">人工</a> 
									<a href="javascript:gotoCtripRobot('${list.order_id}','${list.create_time }','22');" style="color: #F88E2D;">携程</a> 
									<a href="javascript:gotoCtripRobot('${list.order_id}','${list.create_time }','00');" style="color: #F88E2D;">正常</a>
									</c:if>
									<c:if test="${list.manual_order eq '22' && list.order_status eq '61'}">
									<a href="javascript:gotoOrderPayLock('${list.order_id}')" style="color: #F88E2D;">人工支付</a>
									</c:if>
									<c:if test="${list.manual_order ne '22'}">
									<c:choose> 
											<c:when test="${fn:contains('82', list.order_status)}"> 
												<a href="javascript:gotoOrderPay('${list.order_id}');">人工处理</a> 
											</c:when> 
											<c:when test="${fn:contains('44', list.order_status)}"> 
												<a href="javascript:gotoOrderPay('${list.order_id}');">人工处理</a> 
												<a href="javascript:gotoRobot('${list.order_id}','${list.create_time }');">机器处理</a> 
											</c:when> 
											<c:when test="${list.order_status eq '66'}">机器人处理,不能干预 
											</c:when> 
											<c:when test="${list.order_status eq '61'}"> 
												<a href="javascript:gotoOrderPayLock('${list.order_id}')">支付</a>
												<!-- 
												<a href="javascript:updateStatusTo55('${list.order_id}','88')">支付完成</a>
												 -->
												<a href="javascript:updateStatusTo55('${list.order_id}','55')">重新支付</a>  
											</c:when> 
											<c:when test="${list.order_status eq '11'}"> 
												<a href="javascript:submitUpdateStatusInfo('${list.order_id}')">预定重发</a> 
											</c:when>
											<c:when test="${fn:contains('77,83', list.order_status)}"> 
												<a href="javascript:gotoOrderPay('${list.order_id}');">人工处理</a> 
											</c:when> 
											<c:when test="${fn:contains('10,99', list.order_status)}"> 
											</c:when> 
											<c:when test="${fn:contains('AA', list.order_status)}"> 
												<a href="javascript:gotoBudan('${list.order_id}');">人工补单</a> 
												<a href="javascript:gotoRobot('${list.order_id}','${list.create_time }');">机器补单</a> 
											</c:when> 
											<c:when test="${fn:contains('05', list.order_status)}"> 
												 <%		if ("2".equals(loginUserVo.getUser_level()) ) {%>
												<a href="javascript:gotoRobot('${list.order_id}','${list.create_time }');">机器处理</a> 
												<%} %>
											</c:when>
											<c:when test="${list.order_status eq '46'}"> 
												 <%		if ("2".equals(loginUserVo.getUser_level()) ) {%>
												<a href="javascript:updateStatusTo55('${list.order_id}','55')">重新支付</a>  
												<%} %>
											</c:when>	
									</c:choose>
									</c:if>  
									</span>
									<div><span>
										<a href="javaScript:void(0)" onclick="queryInfo('${list.order_id}');" onmouseover="showdiv('${list.order_id}')" onmouseout="hidediv()" >明细</a> 
										
										<c:if test="${list.manual_order ne '22' && list.order_status eq '44' && list.account_from_way eq 0}">
										 <%		if ("2".equals(loginUserVo.getUser_level()) ) {%>
										<a href="javaScript:void(0)" onclick="phoneCheck('${list.order_id}','${list.create_time }');" style="color:red;">切换</a> 
										<%} %>
										</c:if>
										<!-- <a href="javaScript:void(0)" onclick="queryInfo('${list.order_id}');" onmouseover="show('${list.order_id}')" onmouseout="backPage();" >明细</a>  -->
									</span></div>
								</td>
							</tr>
						</c:forEach>
					</table>
					<jsp:include page="/pages/common/paging.jsp" />
					<input type="hidden" id="tag1" name="tag1" value="0" />
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
