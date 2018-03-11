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
		<title>订单转人工管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
		<script language="javascript" src="/js/json2.js"></script>
		<script type="text/javascript">
	<%
		PageVo pageObject = (PageVo) request.getAttribute("pageBean");
		int pageIndex =1;
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user_level = loginUserVo.getUser_level();
	%>
	
	
	/*是否字母和数字组合*/
	function  isOrderInfo(strValue)  { 
	   var  objRegExp=/^[0-9a-zA-Z]*$/;
	   return  objRegExp.test(strValue);  
	} 

	/**
	        订单转人工处理状态,支付人工,预定人工
	*/
	function updateOrderStatusToManual(type){
		
		var orderList=$("#orderList").val().replace(/\ +/g,"");
		alert(orderList);
		if(orderList==null||orderList==undefined||orderList==""){
			alert("输入订单号为空！");
			return;
		}
		var orders=null;
		var newOrders=[];
		try {
			orders=orderList.split("\n");
		} catch (error) {
		    alert("订单格式不正确");
		    return;
		}
		
		//去除空格和空白换行
		for(var index in orders){
			var tempStr=$.trim(orders[index]).replace(/[\r\n]/g,"");      //前后两端的空白符,去掉回车换行               
			var flag=isOrderInfo(tempStr);                        //检测是否通过校验
			if(tempStr.length!=0&&flag){
				newOrders.push(tempStr);
			}else{
				alert(tempStr+",订单号格式错误!");
			};
        }
		var orderStr="";
		for(var index in newOrders){
			orderStr=orderStr+newOrders[index]+",";
		}
		
		alert("检验后的订单个数:"+newOrders.length+",订单详情:"+orderStr);
		
		if(newOrders.length==0){
			return false;
		}
		
		var newOrdersFinal=[];
		$.each(newOrders, function(i, orderId){
			/*  console.info( "Index：" + i + ": " + orderId );  */
			//ajax验证是否锁
			var url = "/acquire/queryPayIsLock.do?order_id="+orderId+"&version="+new Date();
				$.get(url,function(data){
					if(data != null && data != ""){
						var temp = data;
						var str1 = temp.split("&");
						var str =str1[1]; 
						alert("此订单已经锁定，锁定人为"+str+",不作处理");
					}else{
						var json = {"order_id":orderId};
						newOrdersFinal.push(JSON.stringify(json));
					};
					
				});
		});
	
		var str ="";
	 	if(type == 'book'){
	 		str = "预定人工";
	 	}else if(type =='pay'){
	 		str = "支付人工";
	 	}
	 
	    if(confirm("是否"+str+"？")){
	    	if(newOrdersFinal.length==0){
	    		alert("订单正在检测,没有符合条件的订单,请等待3s继续操作");
	    	}
	    	$('#btnModifyBook').attr('disabled',"true");
	    	$('#btnModifyPay').attr('disabled',"true");
	    	
			var  uri = "/acquire/updateOrderStatusToManual.do?jsonArr="+newOrdersFinal+"&type="+type+"&version="+new Date();
			$.post(uri,function(data){
				
				if(data==null){
					alert("操作失败,网络异常");
				}
				var dataObj=$.parseJSON(data);
				if(dataObj.status){
					alert("操作成功,提示："+dataObj.msg+",成功个数："+dataObj.succnum+",失败个数："+dataObj.failnum);
				}else{
					alert("操作失败,原因："+dataObj.msg);
				};
				
				$('#btnModifyBook').removeAttr("disabled");
		    	$('#btnModifyPay').removeAttr("disabled");
		    	
			});
			
	    }else{
	    	return;
	    }
	 		
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
			<br/><br/><br/>
			<form action="#" method="post" name="queryFrm">
				<div style="border: 0px solid #00CC00; margin: 10px;">
				<ul class="order_num oz" style="margin-top: 10px;">
					<li style="width: 80px">订单号列表:</li>
					<li>
					<textarea name="orderList" id="orderList"
					   style="border:solid 1px #9dc5dc; font-size:18px; padding:5px;overflow-x:hidden;overflow-y:scroll;"
					   rows="20" cols="50" placeholder="请填写订单号,每个订单号占一行,不含空格"></textarea>
					</li>
				</ul>
				
				<p style="margin-top: 20px;">
					<input id="btnModifyBook" type="button" value="预定人工" class="btn" onclick="updateOrderStatusToManual('book')"/>
					<input id="btnModifyPay" type="button" value="支付人工" class="btn" onclick="updateOrderStatusToManual('pay')"/>
				</p>
				
				</div>
		 </form>
		 </div>
	</body>
</html>
