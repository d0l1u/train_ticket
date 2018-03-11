<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<%@ page import="com.l9e.transaction.vo.PageVo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>退款管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script language="javascript" src="/js/json2.js"></script>
		<script type="text/javascript">
		<%
			PageVo pageObject = (PageVo) request.getAttribute("pageBean");
			int pageIndex = pageObject.getPageIndex();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String user_level = loginUserVo.getUser_level();
		%>
		function exportExcel() {
			$("form:first").attr("action","/allRefund/exportrefundexcel.do");
			$("form:first").submit();
			$("form:first").attr("action","/allRefund/queryAllRefundList.do");
		}
		
		function gotoOrderRefund(order_id,cp_id, refund_seq, channel, order_type){
		//ajax验证是否锁
		var url = "/allRefund/queryPayIsLock.do?order_id="+order_id+"&cp_id="+cp_id+ "&refund_seq="+ refund_seq+"&version="+new Date();
			$.get(url,function(data){
			if(data != null && data != ""){
				var temp = data ;
				var str1 = temp.split("&") ;
				var str =str1[1]; 
				
				alert("此订单已经锁定，锁定人为"+str);
				return;
			 }else{
				 var channelStr = "";
			 	$("input[name='refund_status']:checkbox:checked").each(function(){ 
							channelStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
						}); 
						channelStr = channelStr.substring(0, channelStr.length-1);
				 var notifyStr = "";
			 	$("input[name='notify_status']:checkbox:checked").each(function(){ 
							notifyStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
						}); 
						notifyStr = notifyStr.substring(0, notifyStr.length-1);
				 var channel2Str = "";
			 	$("input[name='channel']:checkbox:checked").each(function(){ 
							channel2Str += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
						}); 
						channel2Str = channel2Str.substring(0, channel2Str.length-1);		
				$("form:first").attr("action","/allRefund/queryAllRefundInfo.do?order_id="+order_id +"&cp_id="+cp_id+ "&refund_seq=" + refund_seq + "&isActive=1&statusList="+channelStr+ "&notifyList="+ notifyStr+ "&channelList="+ channel2Str+"&pageIndex="+<%=pageIndex%>);
				$("form:first").submit();
			 }
		 	 
		});
	}
	//重新通知
		function gotonotifyAgain(order_id,cp_id, refund_seq,channel, order_type){
		//ajax验证是否锁
		var url = "/allRefund/queryPayIsLock.do?order_id="+order_id+"&cp_id="+cp_id+ "&refund_seq=" + refund_seq+"&version="+new Date();
			$.get(url,function(data){
			if(data != null && data != ""){
				var temp = data ;
				var str1 = temp.split("&") ;
				var str =str1[1]; 
				
				alert("此订单已经锁定，锁定人为"+str);
				return;
			 }else{
				 var channelStr = "";
			 	$("input[name='refund_status']:checkbox:checked").each(function(){ 
							channelStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
						}); 
						channelStr = channelStr.substring(0, channelStr.length-1);
				 var notifyStr = "";
			 	$("input[name='notify_status']:checkbox:checked").each(function(){ 
							notifyStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
						}); 
						notifyStr = notifyStr.substring(0, notifyStr.length-1);
				 var channel2Str = "";
			 	$("input[name='channel']:checkbox:checked").each(function(){ 
							channel2Str += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
						}); 
						channel2Str = channel2Str.substring(0, channel2Str.length-1);		
						
				$("form:first").attr("action","/allRefund/queryAllRefundInfo.do?order_id="+order_id +"&cp_id="+cp_id+ "&refund_seq=" + refund_seq + "&isActive=1&statusList="+channelStr+ "&notifyList=" + notifyStr+ "&channelList="+ channel2Str+"&pageIndex="+<%=pageIndex%>);
				$("form:first").submit();
				//$("form:first").attr("action","/allRefund/queryAllRefundList.do");
			 }
		 	 
		});
	}
		
		//鼠标悬浮于“明细”，显示该订单的操作日志
		var heightDiv = 0; 
		function showdiv(order_id,cp_id){  
		     var oSon = window.document.getElementById("hint");   
		     if (oSon == null) return;   
		     with (oSon){   
		 		 $.ajax({
					url:"/allRefund/queryOrderOperHistory.do?order_id="+order_id+"&cp_id="+cp_id,
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
								$("#historyTable").append("<tr line-height='15px' align='center'><td id='index_'"+index+"''>"+index+
								"</td><td align='left' style='word-break:break-all;'>"+data[i].order_optlog+"</td>"+
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
		//全选refund
		function selectAll(){
			var checklist = document.getElementsByName("refund_status");
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
		
		//全选notify
		function selectAllNotify(){
			var checklist = document.getElementsByName("notify_status");
			if(document.getElementById("controlAllNotify").checked){
				for(var i=0; i<checklist.length; i++){
					checklist[i].checked = 1;
				}
			}else{
				for(var j=0; j<checklist.length; j++){
					checklist[j].checked = 0;
				}
			}
		}
		//全选selectAllChannel
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
		//机器改签
		function gotoRobotGai(order_id, cp_id,refund_seq,channel, order_type){
			//ajax验证是否锁
			var url = "/allRefund/queryPayIsLock.do?order_id="+order_id+ "&refund_seq=" + refund_seq+"&version="+new Date();
			$.get(url,function(data){
				if(data != null && data != ""){
					var temp = data ;
					var str1 = temp.split("&") ;
					var str =str1[1]; 
					alert("此订单已经锁定，锁定人为"+str);
					return;
				 }else{
					var uri = "/allRefund/updateOrderstatusToRobotGai.do?order_id="+order_id+"&cp_id="+cp_id+"&refund_seq="+refund_seq+"&refund_status=00&version="+new Date();
					$.post(uri,function(data){
						if(data=="yes"){
							$("form:first").attr("action","/allRefund/queryAllRefundList.do?pageIndex="+<%=pageIndex%>);
							$("form:first").submit();
						}else{
							alert("机器改签失败");
						}
					});
				 }
			});
		}

		//机器退票
		function gotoRobotRefund(order_id, cp_id, refund_seq,channel, order_type){
			//ajax验证是否锁
			var url = "/allRefund/queryPayIsLock.do?order_id="+order_id+ "&refund_seq=" + refund_seq+"&version="+new Date();
			$.get(url,function(data){
				if(data != null && data != ""){
					var temp = data ;
					var str1 = temp.split("&") ;
					var str =str1[1]; 
					alert("此订单已经锁定，锁定人为"+str);
					return;
				 }else{
					var uri = "/allRefund/updateOrderstatusToRobotGai.do?order_id="+order_id+"&cp_id="+cp_id+"&refund_seq="+refund_seq+"&refund_status=04&version="+new Date();
					$.post(uri,function(data){
						if(data=="yes"){
							$("form:first").attr("action","/allRefund/queryAllRefundList.do?pageIndex="+<%=pageIndex%>);
							$("form:first").submit();
						}else{
							alert("机器退票失败");
						}
					});
				 }
			});
		}
function shuaXinAllAuto(){
	setInterval("shuaXinAll()",1000);//一分钟刷新一次
}		
function shuaXinAll(){
		var url = "/remind/queryCount.do?&version ="+new Date();
		$.get(url,function(data){
			var strJSON = data;//得到的JSON
			var obj = new Function("return" + strJSON)();//转换后的JSON对象
			document.getElementById("19e").value = obj.l9eCount;
			document.getElementById("qunar").value = obj.qunarCount;
			document.getElementById("ext").value = obj.extCount;
			document.getElementById("B2C").value = obj.B2CCount;
			document.getElementById("inner").value = obj.innerCount;
			document.getElementById("elong").value = obj.elongCount;
			document.getElementById("tongcheng").value = obj.tcCount;
			document.getElementById("complain").value = obj.complain;
			
			document.getElementById("bookExtCount").value = obj.bookExtCount; 
			document.getElementById("bookQunarCount").value = obj.bookQunarCount;  
			document.getElementById("bookElongCount").value = obj.bookElongCount;  
			document.getElementById("bookTcCount").value = obj.bookTcCount;  
			document.getElementById("refundExtCount").value = obj.refundExtCount;  
			document.getElementById("refundQunarCount").value = obj.refundQunarCount;  
			document.getElementById("refundElongCount").value = obj.refundElongCount;  
			document.getElementById("refundTcCount").value = obj.refundTcCount; 
		});
	}
$().ready(function(){
		shuaXinAll();shuaXinAllAuto();changecolor();
});	
function changecolor(){
	var a=document.getElementById("urgent_order").value;
	if(a!=""){
	document.getElementById("urgent_order_color").style.color="red";
	}
}
function submitForm(){
	document.getElementById("urgent_order").value="";
	$("form:first").attr("action","/allRefund/queryAllRefundList.do");
	$("form:first").submit();
}

function urgertOrder(){
	$("form:first").attr("action","/allRefund/queryRefundPage.do?urgent_order=right_now");
	$("form:first").submit();
}

var jsonArr = [];　
	//批量审核完成  
	function submitToRefund(){
		var orderIdStr = "";
		var orderIdNum = 0;
		var str = "";
		$("input[name='refund_seq']:checkbox:checked").each(function(){ //遍历被选中CheckBox元素的集合 得到Value值
			var refund_seq = $(this).val();
			var order_id = $("#orderId_"+refund_seq).val();
			var cp_id = $("#cpId_"+refund_seq).val();
			if($("#orderStatus_"+refund_seq).val()==33){//订单状态为审核退款33
			//alert($("#orderStatus_"+refund_seq).val()==33);
				//验证订单是否加锁
				var url = "/allRefund/queryPayIsLock.do?order_id="+order_id+ "&refund_seq=" + refund_seq +"&version="+new Date();
				$.get(url,function(data){
				if(data != null && data != ""){
					var temp = data ;
					var str1 = temp.split("&") ;
					var str =str1[1]; 
					//alert("此订单已经锁定，锁定人为"+str);
					return;
				 }else{//订单被选中，状态为33，未加锁的订单
						var json = {"order_id": order_id, "refund_seq": refund_seq, "cp_id": cp_id};
						jsonArr.push(JSON.stringify(json));
						//alert(jsonArr);
					 }
				});
				orderIdNum++;
			}
		}); 
		if(orderIdNum==0){
			alert("没有审核退款状态的订单");
			return false;
		}
		var channelStr = "";
			 	$("input[name='refund_status']:checkbox:checked").each(function(){ 
							channelStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
						}); 
						channelStr = channelStr.substring(0, channelStr.length-1);
				 var notifyStr = "";
			 	$("input[name='notify_status']:checkbox:checked").each(function(){ 
							notifyStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
						}); 
						notifyStr = notifyStr.substring(0, notifyStr.length-1);
				 var channel2Str = "";
			 	$("input[name='channel']:checkbox:checked").each(function(){ 
							channel2Str += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
						}); 
						channel2Str = channel2Str.substring(0, channel2Str.length-1);		
						
		if(confirm("确认批量机器处理吗 ？")){
			$("form:first").attr("action","/allRefund/submitToRefund.do?pageIndex="+<%=pageIndex%>+"&statusList="+channelStr+ "&notifyList=" 
			+ notifyStr+ "&channelList="+ channel2Str+"&jsonArr="+jsonArr+"&version="+new Date());
			$("form:first").submit();
		}
	}
	
	//全选操作
	function checkChnRetRuleAll(){
	      var order_id = document.getElementsByName("refund_seq");
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
	
	//账号核验处理 
	function accountCheck(order_id,cp_id,refund_seq){
		//ajax验证是否锁
			var url = "/allRefund/queryPayIsLock.do?order_id="+order_id+ "&refund_seq=" + refund_seq+"&version="+new Date();
			$.get(url,function(data){
				if(data != null && data != ""){
					var temp = data ;
					var str1 = temp.split("&") ;
					var str =str1[1]; 
					alert("此订单已经锁定，锁定人为"+str);
					return;
				 }else{
					var uri = "/allRefund/updateToAccountCheck.do?order_id="+order_id+"&cp_id="+cp_id+"&refund_seq="+refund_seq+"&version="+new Date();
					$.post(uri,function(data){
						if(data=="yes"){
							$("form:first").attr("action","/allRefund/queryAllRefundList.do?pageIndex="+<%=pageIndex%>);
							$("form:first").submit();
						}else{
							alert("手机核验失败");
						}
					});
				 }
			});
	}
	
	//人工核验账号完成
	function manualCheck(order_id,cp_id,refund_seq){
		//ajax验证是否锁
			var url = "/allRefund/queryPayIsLock.do?order_id="+order_id+ "&refund_seq=" + refund_seq+"&version="+new Date();
			$.get(url,function(data){
				if(data != null && data != ""){
					var temp = data ;
					var str1 = temp.split("&") ;
					var str =str1[1]; 
					alert("此订单已经锁定，锁定人为"+str);
					return;
				 }else{
					var uri = "/allRefund/updateToManualCheck.do?order_id="+order_id+"&cp_id="+cp_id+"&refund_seq="+refund_seq+"&version="+new Date();
					$.post(uri,function(data){
						if(data=="yes"){
							$("form:first").attr("action","/allRefund/queryAllRefundList.do?pageIndex="+<%=pageIndex%>);
							$("form:first").submit();
						}else{
							alert("人工核验失败");
						}
					});
				 }
			});
	}
</script>
<style>
.liancheng {color: red;}
tr:hover {background: #ecffff;}
#hint{width:700px;border:1px solid #C1C1C1;background:#F0FAC1;position:fixed;z-index:9;padding:6px;line-height:17px;text-align:left;overflow:auto;} 
</style>
</head>
	<body>
		<div></div>
		<div class="book_manage oz">
			<form action="/allRefund/queryAllRefundList.do" method="post">

				<ul class="order_num oz" style="margin-top: 10px;">
					<li>
						订单号：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="order_id"
							value="${order_id}" />
					</li>
					<li>
						退款流水号：&nbsp;
						<input type="text" class="text" name="refund_seq"
							value="${refund_seq}" />
					</li>
					<li>
						12306退款流水号：
						<input type="text" class="text" name="refund_12306_seq"
							value="${refund_12306_seq}" />
					</li>
				</ul>
				<ul class="order_num oz" style="margin-top: 10px;">
					<li>
						开始时间：&nbsp;&nbsp;&nbsp;
						<!-- <input type="text" class="text" name="begin_info_time" value="${begin_info_time }"/> -->
						<input type="text" class="text" name="begin_info_time"
							readonly="readonly" value="${begin_info_time }"
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>
					<li>
						结束时间：&nbsp;&nbsp;&nbsp;
						<!-- <input type="text" class="text" name="end_info_time" value="${end_info_time }"/> -->
						<input type="text" class="text" name="end_info_time"
							readonly="readonly" value="${end_info_time }"
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>
					<li>
							操作人：
							<input type="text" class="text" name="opt_ren" value="${opt_ren }" />
					</li>
				</ul>
				<ul class="order_num oz" style="margin-top: 10px;">
						<li><b><font color="red">通知失败</font></b>：
						&nbsp;预订
								【	去哪：<input value="" id="bookQunarCount" style="color:#f60;width:20px;border:0;"/>
			       					艺龙：<input value="" id="bookElongCount" style="color:#f60;width:20px;border:0;"/>
			       					同程：<input value="" id="bookTcCount" style="color:#f60;width:20px;border:0;"/>
			       					商户：<input value="" id="bookExtCount" style="color:#f60;width:20px;border:0;"/>
			       				】
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;退款
								【	去哪：<input value="" id="refundQunarCount" style="color:#f60;width:20px;border:0;"/>
			       					艺龙：<input value="" id="refundElongCount" style="color:#f60;width:20px;border:0;"/>
			       					同程：<input value="" id="refundTcCount" style="color:#f60;width:20px;border:0;"/>
			       					商户：<input value="" id="refundExtCount" style="color:#f60;width:20px;border:0;"/>
								】
						</li>
					</ul>
					<dl class="oz" style="padding-top:20px;">
						<dt style="float:left;padding-left:40px;line-height:22px;">渠道：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</dt>
						<dd  style="float:left;width:910px;">
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="checkbox" onclick="selectAllChannel()" name="controlAllChannel" style="controlAllChannel" id="controlAllChannel"/><label for="controlAllChannel">&nbsp;全部&nbsp;&nbsp;</label>
							</div>
						<c:forEach items="${merchantMap }" var="s" varStatus="index">
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
							<input type="checkbox" id="channel01" name="channel" value="30101612"
								<c:if test="${fn:contains(channelStr, '30101612')}">checked="checked"</c:if> />
								<label for="channel01">利安</label>
							</div>
						</dd>
					</dl>
				<ul class="order_state oz">
					<li>
						&nbsp;&nbsp;&nbsp;退款状态：
					</li>
					<li><input type="checkbox" onclick="selectAll()" name="controlAll" style="controlAll" id="controlAll"/><label for="controlAll">全部</label></li>
					<li><input type="checkbox" id="refund_status1" name="refund_status" value="012"
								<c:if test="${fn:contains(refund_status, '012')}">checked="checked"</c:if> />
								<label for="refund_status1">正在改签</label></li>
					<li><input type="checkbox" id="refund_status2" name="refund_status" value="03"
								<c:if test="${fn:contains(refund_status, '03')}">checked="checked"</c:if> />
								<label for="refund_status2">人工改签</label></li>
					<li><input type="checkbox" id="refund_status3" name="refund_status" value="456"
								<c:if test="${fn:contains(refund_status, '456')}">checked="checked"</c:if> />
								<label for="refund_status3">正在退票</label></li>
					<li><input type="checkbox" id="refund_status4" name="refund_status" value="07"
								<c:if test="${fn:contains(refund_status, '07')}">checked="checked"</c:if> />
								<label for="refund_status4">人工退票</label></li>
					<li><input type="checkbox" id="refund_status5" name="refund_status" value="11"
								<c:if test="${fn:contains(refund_status, '11')}">checked="checked"</c:if> />
								<label for="refund_status5">退票完成</label></li>	
					<li><input type="checkbox" id="refund_status6" name="refund_status" value="22"
								<c:if test="${fn:contains(refund_status, '22')}">checked="checked"</c:if> />
								<label for="refund_status6">拒绝退票</label></li>	
					<li><input type="checkbox" id="refund_status7" name="refund_status" value="33"
								<c:if test="${fn:contains(refund_status, '33')}">checked="checked"</c:if> />
								<label for="refund_status7">审核退款</label></li>
					<li><input type="checkbox" id="refund_status9" name="refund_status" value="089"
								<c:if test="${fn:contains(refund_status, '089')}">checked="checked"</c:if> />
								<label for="refund_status9">正在审核</label></li>			
				 	<li><input type="checkbox" id="refund_status8" name="refund_status" value="99"
								<c:if test="${fn:contains(refund_status, '99')}">checked="checked"</c:if> />
								<label for="refund_status8">搁置订单</label></li>	
					<li><input type="checkbox" id="refund_status10" name="refund_status" value="44"
								<c:if test="${fn:contains(refund_status, '44')}">checked="checked"</c:if> />
								<label for="refund_status10">发起核验</label></li>
					<li><input type="checkbox" id="refund_status11" name="refund_status" value="45"
								<c:if test="${fn:contains(refund_status, '45')}">checked="checked"</c:if> />
								<label for="refund_status11">正在核验</label></li>  
				</ul>
				<ul class="order_state oz">
					<li>
						&nbsp;&nbsp;&nbsp;通知状态：
					</li>
					<li><input type="checkbox" onclick="selectAllNotify()" name="controlAllNotify" 
					style="controlAllNotify" id="controlAllNotify"/>
					<label for="controlAllNotify">全部</label></li>
					<c:forEach items="${notifyStatus}" var="d" varStatus="index">
						<li>
							<input type="checkbox" id="notify_status${index.count }"
								name="notify_status" value="${d.key }"
								<c:if test="${fn:contains(notify_status,d.key) }">checked="checked"</c:if> />
							<label for="notify_status${index.count }">
								${d.value }
							</label>
						</li>
					</c:forEach>
				</ul>
				<input type="hidden" value="${urgent_order }" id="urgent_order" name="urgent_order"/>
				<p>
					<input type="button" value="查 询" class="btn" onclick="submitForm();" />
					<%
						if ("2".equals(loginUserVo.getUser_level()) ) {
           			%>
           				<input type="button" value="批量审核完成" onclick="submitToRefund();" class="btn" />
						<input type="button" value="导出Excel" class="btn" onclick="exportExcel()" />
					<%} %>
					&nbsp;&nbsp; 	 
					<a  href="/allRefund/queryAllRefundList.do?refund_status=012&refund_status=456&refund_status=089&refund_status=44&refund_status=45">正在处理</a>&nbsp;&nbsp;	
					&nbsp;&nbsp; 	 
					<a id="urgent_order_color" href="javascript:urgertOrder();">紧急订单</a>
					&nbsp;&nbsp;	
					&nbsp;投诉：<input value="" type="text" id="complain" style="color:#f60;width:20px;border:0;"/>
		       	<tr>
						<th>
							未退款【
						</th>
						<th>
							19e：<input value="" id="19e" style="color:#f60;width:20px;border:0;"/>
						</th>
						<th>
							去哪：<input value="" id="qunar" style="color:#f60;width:20px;border:0;"/>
						</th>
						<th>
							艺龙：<input value="" id="elong" style="color:#f60;width:20px;border:0;"/>
						</th>
						<th>
							同程：<input value="" id="tongcheng" style="color:#f60;width:30px;border:0;"/>
						</th>
						<th>
							商户：<input value="" id="ext" style="color:#f60;width:20px;border:0;"/>
						</th>
						<th>
							B2C：<input value="" id="B2C" style="color:#f60;width:20px;border:0;"/>
						</th>
						<th>
							内嵌：<input value="" id="inner" style="color:#f60;width:20px;border:0;"/>】
						</th>
				</tr> 	
				</p>
				<div id="hint" class="pub_con" style="display:none"></div>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #EAEAEA;">
							<th style="width:30px;">全选 <br/><input type="checkbox" id="checkChnRetRulAll" name="checkChnRetRulAll" onclick="checkChnRetRuleAll()"/></th>
							<th>
								序号
							</th>
							<th>
								订单号
							</th>
							<th>
								车票号
							</th>
						<!--	<th>票数</th>
							<th>
								退款流水号
							</th>  -->
							<th>
								乘客姓名
							</th>
							<th>
								退款金额
							</th>
							<th>
								实际退款
							</th>
							<th>
								手续费比例
							</th>

							<th width="70px">
								创建时间
							</th>
							<th width="70px">
								发车时间
							</th>
							<th>
								退款状态
							</th>
							<th>
								退票类型
							</th>
							<th>
								通知状态
							</th>
							<th>
								渠道
							</th>
							<th>
								操作人
							</th>
							<th>
								返回日志
							</th>
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${allRefundList}" varStatus="idx">
							<tr 
								<c:if test="${fn:contains('00,01,02,04,05,06,08,09', list.order_status )}">
									style="background: #BEE0FC;"
								</c:if>
								<c:if test="${fn:contains('03, 07, 33,99', list.order_status )}">
									style="background: #E0F3ED;"
								</c:if>
								<c:if test="${fn:contains('22', list.order_status )}">
									style="background:#FFB5B5;"
								</c:if>
								<c:if test="${fn:contains('44,45', list.order_status )}">
									style="background: #FFF8C2;"
								</c:if>
								>
							<td>
								<input type="checkbox" id="refund_seq" name="refund_seq" value="${list.refund_seq }"/>
								<input type="hidden" id="orderStatus_${list.refund_seq }" value="${list.order_status }" />
								<input type="hidden" id="orderId_${list.refund_seq }" value="${list.order_id }" />
								<input type="hidden" id="cpId_${list.refund_seq }" value="${list.cp_id }" />
								<input type="hidden" id="refundSeq_${list.refund_seq }" value="${list.refund_seq }" />
							</td>
							<td>
								${idx.index+1}
							</td>
							
							<td >
							<!-- 
							<c:if test="${fn:contains(manualOrderList, list.order_id ) }">
							style="color:red;font-weight: bold;"
							</c:if>
							>-->
								${list.order_id}
							</td>
							<td>
								${list.cp_id}
							</td>
							<td>
								${list.user_name}
							</td>
							<td>
								${list.refund_money }
							</td>
							<td>
								${list.refund_12306_money }
							</td>
							<td>
								${list.refund_percent}
							</td>
							<td width="70px">
								${list.create_time}
							</td>
							<td width="70px">
							<c:choose>
							<c:when test="${list.from_time le now2}"><font color="#FF6600;">${list.from_time}</font></c:when>
							<c:when test="${list.from_time le now4 && list.from_time gt now2}"><font color="#FF0000;"><b>${list.from_time}</b></font></c:when>
							<c:otherwise>${list.from_time}</c:otherwise>
							</c:choose>
							</td>
							<td>
								${refundStatus[list.order_status]}
							</td>
							<td>
							<c:choose>
							<c:when test="${list.refund_type  eq '55'}"><font color="#FF0000;">改签退票</font></c:when>
							<c:otherwise>线上退票</c:otherwise>
							</c:choose>
							</td>
							<td>
								${notifyStatus[list.notify_status]}
							</td>
							<td>
								${merchantMap[list.channel]}
							</td>
							<td>
								${list.opt_ren}
							</td>
							<td>
								${returnlogMap[list.return_optlog] }
							</td>
							<td>
								<span>
								<c:choose> 
									<c:when test="${fn:contains('03', list.order_status)}"> 
										<a href="javascript:gotoOrderRefund('${list.order_id}','${list.cp_id}','${list.refund_seq}','${list.channel}','0');">人工改签</a> 
										<c:if test="${refund_and_alert eq '1'}">
												<a href="javascript:gotoRobotGai('${list.order_id}','${list.cp_id}','${list.refund_seq}','${list.channel}','0');">机器改签</a> 	
										</c:if>
									</c:when> 
									<c:when test="${fn:contains('07', list.order_status)}"> 
										<a href="javascript:gotoOrderRefund('${list.order_id}','${list.cp_id}','${list.refund_seq}','${list.channel}','0');">人工退票</a> 
										<c:if test="${refund_and_alert eq '1'}">
										<a href="javascript:gotoRobotRefund('${list.order_id}','${list.cp_id}','${list.refund_seq}','${list.channel}','0');">机器退票</a>  
										</c:if>
									</c:when> 
									<c:when test="${fn:contains('33', list.order_status)}"> 
										<a href="javascript:gotoOrderRefund('${list.order_id}','${list.cp_id}','${list.refund_seq}','${list.channel}','0');">审核退款</a> 
									</c:when> 
									<c:when test="${fn:contains('99', list.order_status)}"> 
										<a href="javascript:gotoOrderRefund('${list.order_id}','${list.cp_id}','${list.refund_seq}','${list.channel}','0');">搁置订单</a> 
									</c:when> 
									<c:when test="${list.notify_status ne '0'}"> 
									 <%
										if ("2".equals(loginUserVo.getUser_level()) ) {
				           			%>
										<a href="javascript:gotonotifyAgain('${list.order_id}','${list.cp_id}','${list.refund_seq}','${list.channel}','0');">人工操作</a> 
									<%
										}
				           			%>
				           			</c:when>
								</c:choose>  
								</span>
								<br/>
								<span>
								<c:if test="${fn:contains('07', list.order_status)}"> 
								<%if ("2".equals(loginUserVo.getUser_level()) ) {%>
								<a href="javaScript:void(0)" onclick="accountCheck('${list.order_id}','${list.cp_id }','${list.refund_seq}');" style="color:red;">核验</a> 
								<%} %>
								</c:if> 
								<c:if test="${fn:contains('44,45', list.order_status)}"> 
								<%if ("2".equals(loginUserVo.getUser_level()) ) {%>
								<a href="javaScript:void(0)" onclick="manualCheck('${list.order_id}','${list.cp_id }','${list.refund_seq}');" style="color:red;">核验完成</a> 
								<%} %>
								</c:if> 
								<a href="/allRefund/queryAllRefundInfo.do?order_id=${list.order_id}&cp_id=${list.cp_id }&refund_seq=${list.refund_seq}&isActive=0" onmouseover="showdiv('${list.order_id}','${list.cp_id}')" onmouseout="hidediv()">明细</a>
								</span>
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
