<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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
	
	$().ready(function(){
		failShuaXin();
		//currentCode();
		accountMargin();contactMargin(); robotAndOrder();
		queryZhifubaoMoney();
		});
	
	function currentCode(){
		var url = "/remind/queryCodeCount.do?&version ="+new Date();
		$.get(url,function(data){
			var strJSON = data;//得到的JSON
			var obj = new Function("return" + strJSON)();//转换后的JSON对象
			document.getElementById("totalPerson").value = obj.totalPerson;
			document.getElementById("codeCountToday").value = obj.codeCountToday;
			document.getElementById("codeSuccessToday").value = obj.codeSuccessToday;
		//	document.getElementById("uncodeCount").value = obj.uncodeCount;
			document.getElementById("uncodeQunarCount").value = obj.uncodeQunarCount;
			//document.getElementById("uncodeTcCount").value = obj.uncodeTcCount;
			
			document.getElementById("waitCodeCount01").value = obj.waitCodeCount01;
			document.getElementById("waitCodeCount02").value = obj.waitCodeCount02;
			document.getElementById("waitCodeCount03").value = obj.waitCodeCount03;
			document.getElementById("waitCodeCount04").value = obj.waitCodeCount04;
			document.getElementById("totalCount01").value = obj.totalCount01;
			document.getElementById("totalCount02").value = obj.totalCount02;
			document.getElementById("totalCount03").value = obj.totalCount03;
			document.getElementById("totalCount04").value = obj.totalCount04;
			
			if(obj.codeQunarType=="00"){
			document.getElementById("codeQunarType").value = "去哪打码" ;
			}else if(obj.codeQunarType=="11"){
			document.getElementById("codeQunarType").value = "我们打码";
			}else if(obj.codeQunarType=="22"){
			document.getElementById("codeQunarType").value = "去哪+我们打码";
			}
				if((obj.uncodeCount+obj.uncodeQunarCount)/obj.totalPerson >5){
				document.getElementById("uncodeCount2").style.display = "block";
				}
		});
	}	
	
	function failShuaXin(){
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
			document.getElementById("bxCount").value = obj.bxCount;
			
			document.getElementById("bookExtCount").value = obj.bookExtCount; 
			document.getElementById("bookQunarCount").value = obj.bookQunarCount;  
			document.getElementById("bookElongCount").value = obj.bookElongCount;  
			document.getElementById("bookTcCount").value = obj.bookTcCount;
			
			document.getElementById("bookGtCount").value = obj.bookGtCount;
			document.getElementById("bookMtCount").value = obj.bookMtCount;
			document.getElementById("bookTuniuCount").value = obj.bookTuniuCount;
			  
			document.getElementById("refundExtCount").value = obj.refundExtCount;  
			document.getElementById("refundQunarCount").value = obj.refundQunarCount;  
			document.getElementById("refundElongCount").value = obj.refundElongCount;  
			document.getElementById("refundTcCount").value = obj.refundTcCount; 
			 
			document.getElementById("pay").value = obj.payCount;
			document.getElementById("book").value = obj.bookCount;
			document.getElementById("refund").value = obj.refundCount;
			document.getElementById("endorse").value = obj.endorseCount;
			document.getElementById("cancel").value = obj.cancelCount;
			document.getElementById("check").value = obj.checkCount;
			document.getElementById("register").value = obj.registerCount;
			document.getElementById("query").value = obj.queryCount;
			document.getElementById("money").value = obj.moneyCount;
			document.getElementById("delete").value = obj.deleteCount;
			document.getElementById("enroll").value = obj.enrollCount;
			document.getElementById("activate").value = obj.activateCount;
			
			document.getElementById("alter").value = obj.alterCount;
		});
	}
	
	
	function accountMargin(){
		var url = "/remind/queryAccountMarginCount.do?&version ="+new Date();
		$.get(url,function(data){
			var strJSON = data;//得到的JSON
			var obj = new Function("return" + strJSON)();//转换后的JSON对象
			document.getElementById("l9eAccount").value = obj.l9eAccount;
			document.getElementById("qunarAccount").value = obj.qunarAccount;
			document.getElementById("elongAccount").value = obj.elongAccount;
			document.getElementById("tcAccount").value = obj.tcAccount;
			document.getElementById("qitaAccount").value = obj.qitaAccount;
		});
	}
	
	function contactMargin(){
		var url = "/remind/queryAccountContactCount.do?&version ="+new Date();
		$.get(url,function(data){
			var strJSON = data;//得到的JSON
			var obj = new Function("return" + strJSON)();//转换后的JSON对象
			document.getElementById("l9eContact").value = obj.l9eContact;
			document.getElementById("qunarContact").value = obj.qunarContact;
			document.getElementById("elongContact").value = obj.elongContact;
			document.getElementById("tcContact").value = obj.tcContact;
			document.getElementById("qitaContact").value = obj.qitaContact;
		});
	}
	
	function robotAndOrder(){
		var url = "/remind/queryRobotAndOrderCount.do?&version ="+new Date();
		$.get(url,function(data){
			var strJSON = data;//得到的JSON
			var obj = new Function("return" + strJSON)();//转换后的JSON对象
			document.getElementById("robotOrderRemind").value = obj.robotOrderRemind;
			document.getElementById("orderNumber").value = obj.orderNumber;
			document.getElementById("robotNumber_00").value = obj.robotNumber_00;
			document.getElementById("robotNumber_01").value = obj.robotNumber_01;
			document.getElementById("robotNumber_11").value = obj.robotNumber_11;
		});
	}
	$(".zhifubaoRadio").live("click",function(){
		queryZhifubaoMoney();
	});
	function queryZhifubaoMoney(){
		var zhifubaoRadio =  $('.zhifubaoRadio:checked ').val(); 
		$("#zhifubao").empty();
		var url = "/remind/queryZhifubaoMoney.do?zhifubaoRadio="+zhifubaoRadio+"&version ="+new Date();
		$.get(url,function(data){
			var strJSON = data;//得到的JSON
			var obj = new Function("return" + strJSON)();//转换后的JSON对象
			//document.getElementById("alipayList").value = obj.alipayList;
			var str = obj.alipayList;
			for(var i=0;i<str.length;i++){
				var map = str[i];
				//alert(map.zhifubao+":"+map.card_remain);
				
				if(map.zhifubao.length==28){
					$("#zhifubao").append("<span style='display:inline;float:left;width:200px;'>"+map.zhifubao.substr(10, 7) +"&nbsp;&nbsp;--&nbsp;&nbsp;余额：<font style='color:#f60;'>"+map.card_remain+"</font>；</span>&nbsp;&nbsp;");
				}else if(map.zhifubao.length==23){
					$("#zhifubao").append("<span style='display:inline;float:left; width:200px;'>"+map.zhifubao.substr(10, 5) +"&nbsp;&nbsp;--&nbsp;&nbsp;余额：<font style='color:#f60;'>"+map.card_remain+"</font>；</span>&nbsp;&nbsp;");
				}
				if((i+1)%4==0)$("#zhifubao").append("<br/>");
			}
		});
	}
	
	//jsonp访问，高铁接口
	//http://43.241.226.93:17102/order.json
	function  getNoCallBackOrder(){
		
		var buy=[];
		var refund=[];
		var confirmChange=[];
		var changeSeat=[];
		var seat=[];
		var result;
		var msg;
			    
		$.ajax(
			    {
			        type:'get',
			        url : 'http://www.19trip.com/gt/externalInterface.do?type=getNoCallBackOrder',
			        dataType : 'jsonp',
			        jsonp:"callbackparam",
			        jsonpCallback:"orderFunction",
			        success  : function(data) {
			        
			        	result=data.result;
			        	msg=data.msg;
			        	
			        	if($.trim(result) == $.trim("SUCCESS")){
			        		alert("获取成功");
			        		buy=data.buy;
			        		refund=data.refund;
			        		confirmChange=data.confirmChange;
			        		changeSeat=data.changeSeat;
			        		seat=data.seat;
			        		
			        		
			        		
			        		
			        	}else{
			        		alert("请求失败,稍后重试,错误原因:"+msg);
			        	}
			        	
			        	
			          
			        },
			        error : function() {
			            alert("网络异常,请求失败");  
			        }  
			    }  
			);
		
		$("#NoCallBackOrder").removeAttr("disabled");
		
	}
	
	
	
</script>
<style>
table{border:2px solid #dadada;width:950px;color:#333;margin-left: 50px;}
tr{ height: 50px;border:1px solid #dadada;margin: 10px;}
td{padding-left: 15px;}
</style>
 </head>
	<body>
		<div></div>
				<div style="border: 0px solid #00CC00; margin: 10px;" align="center">
				<table>
						<tr><td><font color="red">通知失败</font>：&nbsp;&nbsp;
						&nbsp;预订
								【	去哪：<input value="" id="bookQunarCount" style="color:#f60;width:20px;border:0;"/>
			       					艺龙：<input value="" id="bookElongCount" style="color:#f60;width:20px;border:0;"/>
			       					同程：<input value="" id="bookTcCount" style="color:#f60;width:20px;border:0;"/>
			       					高铁：<input value="" id="bookGtCount" style="color:#f60;width:24px;border:0;"/>
			       					美团：<input value="" id="bookMtCount" style="color:#f60;width:24px;border:0;"/>
			       					途牛：<input value="" id="bookTuniuCount" style="color:#f60;width:24px;border:0;"/>
			       					
			       					商户：<input value="" id="bookExtCount" style="color:#f60;width:20px;border:0;"/>
			       				】
						&nbsp;&nbsp;&nbsp;&nbsp;退款
								【	去哪：<input value="" id="refundQunarCount" style="color:#f60;width:20px;border:0;"/>
			       					艺龙：<input value="" id="refundElongCount" style="color:#f60;width:20px;border:0;"/>
			       					同程：<input value="" id="refundTcCount" style="color:#f60;width:20px;border:0;"/>
			       					商户：<input value="" id="refundExtCount" style="color:#f60;width:20px;border:0;"/>
			       				】
			       		&nbsp;&nbsp;&nbsp;&nbsp;保险【未发送：<input value="" id="bxCount" style="color:#f60;width:30px;border:0;"/>】
					</td></tr>
					<tr><td>
					<font color="red">投诉</font>：
			         	<input value="" type="text" id="complain" style="color:#f60;width:18px;border:0;"/>条
										未退款【
									
										19e：<input value="" id="19e" style="color:#f60;width:30px;border:0;"/>
									
										去哪：<input value="" id="qunar" style="color:#f60;width:30px;border:0;"/>
									
										艺龙：<input value="" id="elong" style="color:#f60;width:30px;border:0;"/>
										
										同程：<input value="" id="tongcheng" style="color:#f60;width:30px;border:0;"/>
									
										商户：<input value=""  id="ext" style="color:#f60;width:30px;border:0;"/>
									
										B2C：<input value=""  id="B2C" style="color:#f60;width:30px;border:0;"/>
									
										内嵌：<input value=""  id="inner" style="color:#f60;width:30px;border:0;"/>
									】
								未改签【
								<input value="" id="alter" style="color:#f60;width:30px;border:0;"/>
								】
					</td></tr>
			        <tr><td>
			         <font color="red">机器人</font>：&nbsp;
								【	支付：<input value=""  id="pay" style="color:#f60;width:20px;border:0;"/>
									预订：<input value=""  id="book" style="color:#f60;width:22px;border:0;"/>
									退票：<input value=""  id="refund" style="color:#f60;width:20px;border:0;"/>
									改签：<input value=""  id="endorse" style="color:#f60;width:20px;border:0;"/>
									取消：<input value=""  id="cancel" style="color:#f60;width:20px;border:0;"/>
			       					核验：<input value=""  id="check" style="color:#f60;width:20px;border:0;"/>
			       					实名：<input value=""  id="register" style="color:#f60;width:20px;border:0;"/>
			       					余票：<input value=""  id="query" style="color:#f60;width:20px;border:0;"/>
			       					票价：<input value=""  id="money" style="color:#f60;width:20px;border:0;"/>
			       					删除：<input value=""  id="delete" style="color:#f60;width:20px;border:0;"/>
			       					注册：<input value=""  id="enroll" style="color:#f60;width:20px;border:0;"/>
			       					激活：<input value=""  id="activate" style="color:#f60;width:20px;border:0;"/>
			       				】
					</td></tr>
					<!-- 
					 <tr><td>
							当前打码人数：<input value="" id="totalPerson" style="color:#f60;width:50px;border:0;"/>
							当前去哪未打码数：<input value="" id="uncodeQunarCount" style="color:#f60;width:50px;border:0;"/>
							今日打码总数：<input value="" type="text" id="codeCountToday" style="color:#f60;width:50px;border:0;"/>
							今日打码成功数：<input value="" type="text" id="codeSuccessToday" style="color:#f60;width:50px;border:0;"/>
						打码方式:<input value="" id="codeQunarType" style="color:#f60;width:100px;border:0;"/>
					</td></tr>
					 <tr><td>
						未打码【团队01：<input value="" id="waitCodeCount01" style="color:#f60;width:30px;border:0;"/>
						团队02：<input value="" id="waitCodeCount02" style="color:#f60;width:30px;border:0;"/>
						团队03：<input value="" id="waitCodeCount03" style="color:#f60;width:30px;border:0;"/>
						团队其他：<input value="" id="waitCodeCount04" style="color:#f60;width:30px;border:0;"/>】
						</td></tr>
					 <tr><td>
						打码员【团队01：<input value="" id="totalCount01" style="color:#f60;width:30px;border:0;"/>
						团队02：<input value="" id="totalCount02" style="color:#f60;width:30px;border:0;"/>
						团队03：<input value="" id="totalCount03" style="color:#f60;width:30px;border:0;"/>
						团队其他：<input value="" id="totalCount04" style="color:#f60;width:30px;border:0;"/>】
					</td></tr>
					-->
					<tr><td>
						账号可用余量【19e：<input value="" id="l9eAccount" style="color:#f60;width:50px;border:0;"/>
						艺龙：<input value="" id="elongAccount" style="color:#f60;width:50px;border:0;"/>
						同程：<input value="" id="tcAccount" style="color:#f60;width:50px;border:0;"/>
						去哪：<input value="" id="qunarAccount" style="color:#f60;width:50px;border:0;"/>
						其他：<input value="" id="qitaAccount" style="color:#f60;width:50px;border:0;"/>】
					</td></tr>
					<tr><td>
						账号联系人已用/剩余【19e：<input value="" id="l9eContact" style="color:#f60;width:100px;border:0;"/>
						艺龙：<input value="" id="elongContact" style="color:#f60;width:100px;border:0;"/>
						同程：<input value="" id="tcContact" style="color:#f60;width:100px;border:0;"/>
						去哪：<input value="" id="qunarContact" style="color:#f60;width:100px;border:0;"/>
						其他：<input value="" id="qitaContact" style="color:#f60;width:100px;border:0;"/>】
					</td></tr>
					<tr><td>
						分单量--可用机器预警【分单量：<input value="" id="orderNumber" style="color:#f60;width:100px;border:0;"/>
						空闲：<input value="" id="robotNumber_00" style="color:#f60;width:100px;border:0;"/>
						队列中：<input value="" id="robotNumber_01" style="color:#f60;width:100px;border:0;"/>
						运行中：<input value="" id="robotNumber_11" style="color:#f60;width:100px;border:0;"/>
						阀值：<input value="" id="robotOrderRemind" style="color:#f60;width:100px;border:0;"/>】
					</td></tr>
					<tr>
					<td >
						<span style='display:inline;float:left;width:400px;'>
							<input type="radio" class="zhifubaoRadio" name="state" value="19e" checked="checked"/>19e
							<input type="radio" class="zhifubaoRadio" name="state" value="kuyou" />kuyou
						</span>
						<br/>
					<span id="zhifubao">
					</span>
					</td>
					</tr>
					<!--高铁未回调订单提示 行  -->
					<tr>
						<td>
						<span style='display:inline;float:left;width:400px;'>
						<font color="blue">高铁未回调订单提示</font><font color="red">【每10分钟获取一次,访问间隔时间为20s】</font>
						</span><br/>
						<input id="click_call" value="手动点击"  type="button" onclick="javascript:timedMsg();"></input>
						<br/>
						<ul id="noCallOrder" >
						<li id="buy" >
						<span >确认出票或先支付后下单没有回调的订单:</span>
						 <input value="" id="buy_num" style="color:#f60;width:100px;border:0;"/>
						</li>
						
						<li id="refund">
						<span >退票没有回调的订单:</span>
						<input value="" id="refund_num" style="color:#f60;width:100px;border:0;"/>
						</li>
						
						<li id="confirmChange" >
						<span >确认改签没有回调的订单:</span>
						<input value="" id="confirmChange_num" style="color:#f60;width:100px;border:0;"/>
						</li>
						
						<li id="changeSeat" >
						<span>改签占座没有回调的订单:</span>
						<input value="" id="changeSeat_num" style="color:#f60;width:100px;border:0;"/>
						</li>
						
						<li id="seat" >
						<span >买票占座没有回调的订单:</span>
						<input value="" id="seat_num" style="color:#f60;width:100px;border:0;"/>
						</li>
						</ul>
						<span>
							  <font color="#008000" > 获取结果提示:</font>
						      <label id="tips"></label>
					    </span>
						</td>
						
						      
						
					</tr>
					
					
			</table>
			<!-- 
			<input type="button" value="返回" onclick="backPage();"
			style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#FFA500;cursor:pointer;width:100px;height:30px;margin-left: 500px;"/>
			 -->
		</div>
	</body>
<script type="text/javascript">

	function timedMsg() {
		
		var buy=[];
		var refund=[];
		var confirmChange=[];
		var changeSeat=[];
		var seat=[];
		var result;
		var msg;
		
		$("#tips").html("----");
		$("#buy_num").val("0");
		$("#refund_num").val("0");
		$("#confirmChange_num").val("0");
		$("#changeSeat_num").val("0");
		$("#seat_num").val("0");
		
		$.ajax(
			    {
			        type:'get',
			        url : 'http://www.19trip.com/gt/externalInterface.do?type=getNoCallBackOrder',
			        dataType : 'jsonp',
			        jsonp:"callbackparam",
			        jsonpCallback:"orderFunction",
			        success  : function(data) {
			        	result=data.result;
			        	msg=data.msg;
			        	if($.trim(result) == $.trim("SUCCESS")){
			        		
			        		buy=data.buy;
			        		refund=data.refund;
			        		confirmChange=data.confirmChange;
			        		changeSeat=data.changeSeat;
			        		seat=data.seat;
			        		
			        		$("#buy_num").val(buy.length);
			        		$("#refund_num").val(refund.length);
			        		$("#confirmChange_num").val(confirmChange.length);
			        		$("#changeSeat_num").val(changeSeat.length);
			        		$("#seat_num").val(seat.length);
			        	
			        	
			        	}else{
			        		
			        		$("#tips").css({"background-color":"#FFF","font-family":"Georgia, serif","font-size":"14px","color":"#F00"}).html("获取失败："+msg);
			        	}
			        		
			        },
			        error : function() {
					      $("#tips").html("获取失败：网络错误").css({"background-color":"#FFF","font-family":"Georgia, serif","font-size":"14px","color":"#F00"});
					}
			    }	
			        
		);
		
		setTimeout("timedMsg()", 1000*60*10);  //10min执行一次
	}

	 timedMsg();
</script>
</html>
