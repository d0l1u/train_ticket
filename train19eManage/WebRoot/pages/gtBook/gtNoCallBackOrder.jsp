<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>订单</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script type="text/javascript">
	<%
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user_level = loginUserVo.getUser_level();
	%>
	function exportExcel() {
		$("form:first").attr("action","/orderForExcel/excelExportGtBooking.do");
		$("form:first").submit();
		$("form:first").attr("action","/gtBooking/queryGtBookList.do");
	}
	$().ready(function(){
		if($("#defaultStartDate").val()==null || $("#defaultStartDate").val()==""){
			$("#defaultStartDate").val(GetDateStr(-15));
		}
	});
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
	
	function selectAllMerchant(){
		var checklist = document.getElementsByName("merchant_id");
		if(document.getElementById("controlAllMerchant").checked){
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
		
//鼠标悬浮于“明细”，显示该订单的操作日志
	var heightDiv = 0; 
	function showdiv(order_id){  
	     var oSon = window.document.getElementById("hint");   
	     if (oSon == null) return;   
	     with (oSon){   
	 		 $.ajax({
				url:"/gtBooking/queryOrderOperHistory.do?order_id="+order_id,
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
	
	
	//出票系统重新通知出票系统
	function cpNoticeAgain(order_id){  
		$.ajax({
			url:"/gtBooking/cpNoticeAgain.do?order_id="+order_id,
			type: "POST",
			cache: false,
			async: true,
			success: function(data){
				if(data!="success" || data == null){
					alert("操作失败，请重试！！");
				}else{
					alert("操作成功");
				}
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
		
		//时单击按钮，禁用按钮
		$("#NoCallBackOrder").attr({"disabled":"disabled"});
		$("#table1 tbody tr:odd").addClass("odd");
	    $("#table1 tbody tr:even").addClass("even");
		$("#table2 tbody tr:odd").addClass("odd");
	    $("#table2 tbody tr:even").addClass("even");
		$("#table3 tbody tr:odd").addClass("odd");
	    $("#table3 tbody tr:even").addClass("even");
		$("#table4 tbody tr:odd").addClass("odd");
	    $("#table4 tbody tr:even").addClass("even");
		$("#table5 tbody tr:odd").addClass("odd");
	    $("#table5 tbody tr:even").addClass("even");
	    
	    //清空table tbody
	    $("#table1 tbody").html("");
	    $("#table2 tbody").html("");
	    $("#table3 tbody").html("");
	    $("#table4 tbody").html("");
	    $("#table5 tbody").html(""); 
	    
	    
		$.ajax(
			    {
			        type:'get',
			        url : 'http://219.238.151.236/gt/externalInterface.do?type=getNoCallBackOrder',
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
			        		
			        		$.each(buy,function(index,value){
			        			var tr="<tr><td>"+index+"</td>"+
			        			"<td>"+value.comment+"</td>"+
			        			"<td>"+value.orderId+"</td>"+
			        			"<td></td>"+
			        			"<td>"+value.supplierOrderId+
			        			"</td></tr>";
			        			$("#table1 tbody").append(tr);
			        			
			        		});
			        		$.each(refund,function(index,value){
			        			var tr="<tr><td>"+index+"</td>"+
			        			"<td>"+value.comment+"</td>"+
			        			"<td>"+value.order_id+"</td>"+
			        			"<td>"+value.sub_order_id+"</td>"+
			        			"<td>"+value.sequence_no+
			        			"</td></tr>";
			        			$("#table2 tbody").append(tr);
			        			
			        		});
			        		$.each(confirmChange,function(index,value){
			        			var tr="<tr><td>"+index+"</td>"+
			        			"<td>"+value.comment+"</td>"+
			        			"<td>"+value.orderId+"</td>"+
			        			"<td>"+value.subOrderId+"</td>"+
			        			"<td>"+value.supplierOrderId+
			        			"</td></tr>";
			        			$("#table3 tbody").append(tr);
			        			
			        		});
			        		$.each(changeSeat,function(index,value){
			        			var tr="<tr><td>"+index+"</td>"+
			        			"<td>"+value.comment+"</td>"+
			        			"<td>"+value.orderId+"</td>"+
			        			"<td>"+value.subOrderId+"</td>"+
			        			"<td>"+value.supplierOrderId+
			        			"</td></tr>";
			        			$("#table4 tbody").append(tr);
			        			
			        		});
			        		$.each(seat,function(index,value){
			        			var tr="<tr><td>"+index+"</td>"+
			        			"<td>"+value.comment+"</td>"+
			        			"<td>"+value.orderId+"</td>"+
			        			"<td></td>"+
			        			"<td>"+value.supplierOrderId+
			        			"</td></tr>";
			        			$("#table5 tbody").append(tr);
			        			
			        		});
			        		
			        		
			        		
			        		
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

tr:hover{background:#ecffff;}
#hint{width:700px;border:1px solid #C1C1C1;background:#F0FAC1;position:fixed;z-index:9;padding:6px;line-height:17px;text-align:left;overflow:auto;} 
caption{background:#eccfff;font-size:15px;font-weight:bold;}
</style>
	</head>
	<body><div></div>
		<div class="book_manage oz">
		
				<br /><br /><br />
				
		<p>			  
			<%if(("2".equals(loginUserVo.getUser_level()))){%>
			  <input id="NoCallBackOrder"  type="button" value="未回调订单 " class="btn" onclick="getNoCallBackOrder()"/>
			<%} %>
		</p>
	
		<table id="table1">
		<caption align="top">确认出票或先支付后下单没有回调的订单</caption>  
		<thead>
     		<tr>
     		   <th style="width: 160px;background: #EAEAEA;">No</th>
	           <th style="width: 160px;background: #EAEAEA;">comment</th>
	           <th style="width: 160px;background: #EAEAEA;">orderId</th>
	           <th style="width: 160px;background: #EAEAEA;">subOrderId</th>
	           <th style="width: 160px;background: #EAEAEA;">supplierOrderId</th>
	           </tr>
        </thead>
		<tbody>
		</tbody>
								
		</table>
		<br/>
	
		<table id="table2">			
			<caption align="top">退票没有回调的订单</caption>	
			<thead>
		  <tr>
		       <th style="width: 160px;background: #EAEAEA;">No</th>
	           <th style="width: 160px;background: #EAEAEA;">comment</th>
	           <th style="width: 160px;background: #EAEAEA;">orderId</th>
	           <th style="width: 160px;background: #EAEAEA;">subOrderId</th>
	           <th style="width: 160px;background: #EAEAEA;">supplierOrderId</th>
	      </tr>
		</thead>
		<tbody>
		</tbody>
				
		</table>
		<br/>
				
		<table id="table3">					
			<caption align="top">确认改签没有回调的订单</caption>
		<thead>
		  <tr>
		       <th style="width: 160px;background: #EAEAEA;">No</th>
	           <th style="width: 160px;background: #EAEAEA;">comment</th>
	           <th style="width: 160px;background: #EAEAEA;">orderId</th>
	           <th style="width: 160px;background: #EAEAEA;">subOrderId</th>
	           <th style="width: 160px;background: #EAEAEA;">supplierOrderId</th>
	      </tr>
		</thead>
		<tbody>
		</tbody>
							
		</table>
		<br/>
		
		<table id="table4">
			<caption align="top">改签占座没有回调的订单</caption>	
			<thead>			
		  <tr>
		       <th style="width: 160px;background: #EAEAEA;">No</th>
	           <th style="width: 160px;background: #EAEAEA;">comment</th>
	           <th style="width: 160px;background: #EAEAEA;">orderId</th>
	           <th style="width: 160px;background: #EAEAEA;">subOrderId</th>
	           <th style="width: 160px;background: #EAEAEA;">supplierOrderId</th>
	      </tr>
			</thead>
		<tbody>
		</tbody>			
		</table>
		<br/>

		<table id="table5">			
			<caption align="top">买票占座没有回调的订单</caption>	
		<thead>
		  <tr>
		  	   <th style="width: 160px;background: #EAEAEA;">No</th>
	           <th style="width: 160px;background: #EAEAEA;">comment</th>
	           <th style="width: 160px;background: #EAEAEA;">orderId</th>
	           <th style="width: 160px;background: #EAEAEA;">subOrderId</th>
	           <th style="width: 160px;background: #EAEAEA;">supplierOrderId</th>
	      </tr>
			</thead>
		<tbody>
		</tbody>			
		</table>
		
		</div>
		
		<br/><br/><br/><br/>
	</body>
</html>
