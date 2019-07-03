<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>自提订单明细页</title>
<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" type="text/javascript" src="/js/jquery.validate.js"></script>
<script language="javascript" type="text/javascript" src="/js/jquery.metadata.js"></script>
<script type="text/javascript">
	var checkSubmitFlg = false;//设置全局变量，只允许表单提交一次
	function checkSubmit() {
	   if (checkSubmitFlg == true) {
	        return false;
	   }
	   checkSubmitFlg = true;
	   return true;
	}
	document.ondblclick = function docondblclick() {
	    window.event.returnValue = false;
	}
	document.onclick = function doconclick() {
	   if (checkSubmitFlg) {
	       window.event.returnValue = false;
	   }
	}
	$().ready(function() { 
		$("#updateForm").validate(); 
	});
	function submitForm(){
		if($.trim($("#ctrip_name").val())==""){
		$("#ctrip_name").focus();
			alert("登录名不能为空！");
			return;
		}
		
		if($.trim($("#ctrip_password").val())==""){
		$("#ctrip_password").focus();
			alert("登录密码不能为空！");
			return;
		}

		if($.trim($("#pay_password").val())==""){
			$("#pay_password").focus();
				alert("支付密码不能为空！");
				return;
			}

		if($.trim($("#ctrip_username").val())==""){
			$("#ctrip_username").focus();
				alert("用户名不能为空！");
				return;
			}
		if($.trim($("#ctrip_phone").val())==""){
			$("#ctrip_phone").focus();
				alert("手机号不能为空！");
				return;
			}
		
		
		if($.trim($("#cookie").val()).length>200){
			alert("Cookie值长度超过200！");
			return;
		}
		
		$("#updateForm").submit();
		
	}
	
	function onblurUsername(){
		var str = document.getElementById("ctrip_name").value ;
		var name = $.trim(str);
		if(name==""){
		$("#username_info").text("用户名不能为空!").css("color","#f00");
		}else{
			var url = "/ctripAccount/queryCtrip_name.do?ctrip_name="+name;
			$.get(url,function(data){
				if(data == "yes"){
					$("#username_info").text("恭喜!用户名可用!").css("color","#0b0");
					$("#btnModify").attr("disabled", false);
				}else{
					$("#username_info").text("用户名已被占用！").css("color","#f00");
					$("#btnModify").attr("disabled", true);
				}
			});
		}
	}

</script>
</head>

<body>

	<div class="book_manage account_manage oz">
	<form action="/ctripAccount/addCtripAccount.do" method="post" name="updateForm" id="updateForm">
	<br/><br/><br/><br/>
        <ul class="order_num oz">
        	<li>登陆名&nbsp;&nbsp;：<input type="text" name="ctrip_name" id="ctrip_name"  
        			onblur="onblurUsername()"/><span id="username_info"></span></li>
            <li>登陆密码：<input type="text" name="ctrip_password" id="ctrip_password" /></li>
            <li>支付密码：<input type="text" name="pay_password" id="pay_password" /></li>
            <li>用户名&nbsp;&nbsp;：<input type="text" name="ctrip_username" id="ctrip_username" /></li>
            <li>手机号&nbsp;&nbsp;：<input type="text" name="ctrip_phone" id="ctrip_phone" /></li>
            <li><label>wap端cookie&nbsp;&nbsp;：</label><textarea name="cookie" id="cookie" style="border:2px solid #dadada; width: 200px; height: 80px; font-size:10px;"></textarea></li>
            <li>wap端cid&nbsp;&nbsp;：<input type="text" name="cid" id="cid"/></li>
            <li>wap端auth&nbsp;&nbsp;：<input type="text" name="auth" id="auth"/></li>
            <li>wap端sauth&nbsp;&nbsp;：<input type="text" name="sauth" id="sauth"/></li> 
        </ul>
        <p><input type="button" value="提 交" class="btn" id="btnModify" onclick="submitForm()"/></p>
        </form>
	</div>

 
</body>
</html>
