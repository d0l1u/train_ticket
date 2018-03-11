<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>添加注册页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	    <script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
	    <script type="text/javascript" src="/js/idCard.js"></script>
	    <script type="text/javascript">


			function onblurUser_name(){
				$("#user_name_info").text("");
				var user_name = $.trim( $("#user_name").val() );
				if(user_name==""){
					$("#user_name_info").text("姓名不能为空 ").css("color","#f00");
				}
			}


			//验证身份证是否有效
		    function valiIdCard(idCard){
		       var checkFlag = new clsIDCard(idCard);
		       if(!checkFlag.IsValid()){
		    	   alert("身份证输入不合法"); 
		           return false;
		       }else{
		    	   alert("身份证合法"); 
		           return true;
		       }
		    }
		    
					

			function onblurIds_card(){
				$("#ids_card_info").text("");
				var ids_card = $.trim( $("#ids_card").val() );

				if(ids_card==""){
					$("#ids_card_info").text("身份证号不能为空").css("color","#f00");
				}else{
					var checkFlag = new clsIDCard(ids_card);
				       if(!checkFlag.IsValid()){
				    	    alert("身份证输入不合法"); 
							$("#ids_card_info").text("身份证输入不合法 ").css("color","#f00");
							$("#btnModify").attr("disabled", true); 
							return  false;  
				       }else{
				    	   var url = "/register/queryRegisterIdcard.do?ids_card="+ids_card+"&version="+new Date();
							$.get(url,function(data){
								if(data == "no"){
									$("#ids_card_info").text("身份证号可用").css("color","#0b0");
									$("#btnModify").attr("disabled", false);
								}
								if(data == "yes"){
									$("#ids_card_info").text("该号码已存在！").css("color","#f00");
									$("#btnModify").attr("disabled", true);
								}
							});
				       }
					//var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;  
					//if(reg.test(ids_card) == false)
					//{  
					//       alert("身份证输入不合法"); 
					//       $("#ids_card_info").text("身份证输入不合法 ").css("color","#f00");
					//       $("#btnModify").attr("disabled", true); 
					//       return  false;  
					//}
					
				}
			}
	
			function submitForm(){
		    	if($.trim($("#user_name").val())==""){
		    		$("#user_name").focus();
		    			alert("姓名不能为空！");
		    			return;
		    	}
		    	if($.trim($("#ids_card").val())==""){
		    		$("#id_card").focus();
		    			alert("邮箱不能为空！");
		    			return;
		    	}
				$("#addForm").submit();
			}

	    </script>
	</head>
	<body>
		<div class="book_manage account_manage oz">
	<form action="/register/addRegister.do" method="post" name="addForm" id="addForm">
        <br/><br/><br/><br/>
        <ul class="order_num oz">
        	<li>姓&nbsp;&nbsp;&nbsp;&nbsp;名：&nbsp;&nbsp;<input type="text" name="user_name" id="user_name"  onblur="onblurUser_name()"/><span id="user_name_info"></span></li>
        	<li>身份证号：&nbsp;&nbsp;<input type="text" name="ids_card" id="ids_card"  onblur="onblurIds_card();"/><span id="ids_card_info"></span></li>
        	<li>联系电话：&nbsp;&nbsp;<input type="text" name="user_phone" id="user_phone"/></li>
        	<li>代理商账号：<input type="text" name="user_id" id="user_id"/></li>
        	<li>12306账号：&nbsp;<input type="text" name="account_name" id="account_name"/></li>
        	<li>12306密码：&nbsp;<input type="text" name="account_pwd" id="account_pwd"/></li>
        </ul>	
         <p><input type="button" value="提 交" class="btn" id="btnModify" onclick="submitForm()"/>
         <input type="button" value="返 回" class="btn btn_normal" onclick="javascript:history.back(-1);" /></p>
        </form>
	</div>
	</body>
</html>