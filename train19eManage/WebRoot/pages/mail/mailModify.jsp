<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>票价修改页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	    <script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
	    <script type="text/javascript">
		    function submitForm(){
		    	
		    	if($.trim($("#pwd").val())==""){
		    		$("#pwd").focus();
		    			alert("邮箱密码不能为空！");
		    			return;
		    	}
				$("#addForm").submit();
			}
			function onblurAddress(){
				$("#address_info").text("");
				var address = $.trim( $("#address").val() );
				if(address==""){
					$("#address_info").text("邮箱地址不能为空").css("color","#f00");
				}
			}
			function onblurPwd(){
				$("#pwd_info").text("");
				var pwd = $.trim( $("#pwd").val() );
				if(pwd==""){
					$("#pwd_info").text("邮箱密码不能为空").css("color","#f00");
				}
			}
	    </script>
	</head>
	<body>
		<div class="book_manage account_manage oz">
	<form action="/mail/updateMail.do" method="post" name="addForm" id="addForm">
        <br/><br/><br/><br/>
        <ul class="order_num oz">
        	
        	<li>邮箱地址：&nbsp;&nbsp;${mail.address }<input type="hidden" name="mail_id" value=${mail.mail_id } /></li>
        	<li>邮箱密码：&nbsp;&nbsp;<input type="text" name="pwd" id="pwd" value="${mail.pwd }" onblur="onblurPwd()"/><span id="pwd_info"></span></li>
        </ul>	
         <p><input type="button" value="提 交" class="btn" id="btnModify" onclick="submitForm()"/></p>
        </form>
	</div>
	</body>
</html>