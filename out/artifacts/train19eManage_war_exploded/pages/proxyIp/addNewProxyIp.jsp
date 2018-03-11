<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加代理IP页面</title>
<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery.validate.js"></script>
<script type="text/javascript" src="/js/jquery.metadata.js"></script>
<script type="text/javascript" src="/form-plugin/jquery.form.js" ></script>

<script type="text/javascript" src="/js/layer/layer.js"></script>
<script type="text/javascript" src="/js/mylayer.js"></script>
<script type="text/javascript" src="/js/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/js/json2.js"></script>


<script type="text/javascript">
	//重复提交标识
	var isRepeat = false;

	$().ready(function() {
		$("#addNewProxyIpForm").validate({
			rules: {
				orderId:{
					required:true,
					minlength: 5
				},
				username: {
					required: true,
			        minlength: 3
				},
				password: {
					required: true,
					minlength: 3
				},
				port: {
					required: true,
					maxlength:5
				},
				endTime: {
					required: true
				},
				proxyIpList: {
					required: true
				}
			},
			messages: {
				orderId: {
					required: "请输入订单号",
					minlength: "订单号长度不能小于5位"
				},
				username: {
					required: "请输入用户名",
					minlength: "用户名长度不能小于3位"
				},
				password: {
					required: "请输入密码",
			        minlength: "密码长度不能小于3位"
				},
				port: {
					required: "请输入端口",
					maxlength: "端口长度不能大于5位"
				},
				endTime: {
					required: "请输到期时间"
				},
				proxyIpList: {
					required: "请输入IP"
				}
			}
		});
	});

	var options = {
		success : function() {
			isRepeat = false;
			alert("添加成功") 
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
            alert(XMLHttpRequest.responseText);
			isRepeat = false;
		},
		beforeSubmit : function() {
			//判断是否是重复提交
			if (!isRepeat) {
				isRepeat = true;
				return true;
			} else {
				return false;
			}
		},
		resetForm : true,
		clearForm : true
	};
	$('#addNewProxyIpForm').ajaxForm(options);
	
</script>
<style>
.error{
	color:red;
}
</style>
</head>

<body>

	<div class="book_manage oz">
		<form action="/ip/addNewProxyIp.do" method="post" name="addNewProxyIpForm" id="addNewProxyIpForm">
			<br/><br/><br/><br/>
	  
	        <ul class="order_num oz" style="margin-top: 10px;">
				<li style="width: 60px">订单号:</li>
				<li >
					<input type="text" class="text" name="orderId" id="orderId" value="" />
				</li>
			</ul>
	        <ul class="order_num oz" style="margin-top: 10px;">
				<li style="width: 60px">账号名:</li>
				<li >
					<input type="text" class="text" name="username" id="username" value="" />
				</li>
			</ul>
			<ul class="order_num oz" style="margin-top: 10px;">
				<li style="width: 60px">密码:</li>
				<li >
					<input type="text" class="text" name="password" id="password" value="" />
				</li>
			</ul>
			<ul class="order_num oz" style="margin-top: 10px;">
				<li style="width: 60px">端口:</li>
				<li >
					<input type="text" class="text" name="port" id="port" value="" />
				</li>
			</ul>
			<ul class="order_num oz" style="margin-top: 10px;">
				<li style="width: 60px">到期时间:</li>
				<li >
					<input type="text" class="text" id="endTime"
					name="endTime"  value=""
					onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
				</li>
			</ul>
			<ul class="order_num oz" style="margin-top: 10px;">
				<li style="width: 60px">IP列表:</li>
				<li >
					<textarea name="proxyIpList" id="proxyIpList" style="border:solid 1px #9dc5dc; font-size:18px; padding:5px; overflow-x:hidden; overflow-y:auto" rows="20px" cols="80px" placeholder="请填写IP,格式 127.0.0.1 每个IP独占一行"></textarea>
				</li>
			</ul>
        
	        <p style="margin-top: 20px;">
	        	<input type="submit" value="提 交" class="btn" id="btnAdd" />
	        </p>
        </form>
	</div>
</body>
</html>
