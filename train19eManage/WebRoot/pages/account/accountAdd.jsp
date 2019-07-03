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
		
		if($.trim($("#acc_username").val())==""){
		$("#acc_username").focus();
			alert("登录名不能为空！");
			return;
		}
		
		if($.trim($("#acc_password").val())==""){
		$("#acc_password").focus();
			alert("登录密码不能为空！");
			return;
		}

		if($.trim($("#real_name").val())==""){
			$("#real_name").focus();
				alert("真实姓名不能为空！");
				return;
			}

		if($.trim($("#id_card").val())==""){
			$("#id_card").focus();
				alert("身份证号码不能为空！");
				return;
			}
		
		var email = $.trim( document.getElementById("acc_mail").value ); 
		var reg = /^[a-zA-Z0-9_-]+(\.([a-zA-Z0-9_-])+)*@[a-zA-Z0-9_-]+[.][a-zA-Z0-9_-]+([.][a-zA-Z0-9_-]+)*$/;
		if($.trim($("#acc_mail").val())==""){
			$("#acc_mail").focus();
				alert("邮箱不能为空！");
				return;
		}else if((!email=="") && (reg.test(email))==false){
			alert("邮箱格式不正确!");
			return;
		}
		
		if($.trim($("#channel").val())==""){
		$("#channel").focus();
			alert("渠道不能为空！");
			return;
		}
		$("#updateForm").submit();
		
	}
	
	function selectCity(){
		
		var url = "/common/queryGetCity.do?provinceid="+$("#at_province_id").val();
		$.get(url,function(data,status){
		    $("#at_city_id").empty(); 
		    var obj = eval(data);
			$(obj).each(function(index){
				var val = obj[index];
				$("#at_city_id").append("<option value='"+val.area_no+"'>"+val.area_name+"</option>");
			});
		  });
	}
	
	function onblurUsername(){
		var str = document.getElementById("acc_username").value ;
		var name = $.trim(str);
		if(name==""){
		$("#username_info").text("用户名不能为空!").css("color","#f00");
		}else{
			var url = "/account/queryAcc_username.do?acc_username="+name;
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

	function onblurAccMail(){
		$("#accMail_info").text("");
		var email = $.trim( document.getElementById("acc_mail").value ); 
		var reg = /^[a-zA-Z0-9_-]+(\.([a-zA-Z0-9_-])+)*@[a-zA-Z0-9_-]+[.][a-zA-Z0-9_-]+([.][a-zA-Z0-9_-]+)*$/;
		if(email==""){
			$("#accMail_info").text("邮箱不能为空!").css("color","#f00");
		}else if((!email=="") && (reg.test(email))==false){
			$("#accMail_info").text("邮箱格式不正确!").css("color","#f00");
		}
	}
	function focusUsername(){
		$("#username_info").text("");
	}
	

</script>
</head>

<body>

	<div class="book_manage account_manage oz">
	<form action="/account/addAccount.do" method="post" name="updateForm" id="updateForm">
	<br/><br/><br/><br/>
    	<ul class="ser oz">
        			<li>省<select name="at_province_id" id="at_province_id" onchange="selectCity();">
							<c:forEach items="${province }" var="p">
								<option value="${p.area_no }">${p.area_name}</option>
							</c:forEach>
            			</select>
            		</li>
           			 <li>市<select name="at_city_id" id="at_city_id">
            			<option value="" selected="selected">请选择</option>
            		</select>
           		 </li>
        		</ul>
        <ul class="order_num oz">
        	<li>登陆名&nbsp;&nbsp;：<input type="text" name="acc_username" id="acc_username"  
        			onblur="onblurUsername()" onfocus="focusUsername()"/><span id="username_info"></span></li>
            <li>登陆密码：<input type="text" name="acc_password" id="acc_password" /></li>
            <li>真实姓名：<input type="text" name="real_name" id="real_name" /></li>
            <li>身份证号：<input type="text" name="id_card" id="id_card" /></li>
            <li>邮&nbsp;&nbsp;&nbsp;&nbsp;箱：<input type="text" name="acc_mail" id="acc_mail" onblur="onblurAccMail()"/><span id="accMail_info"></span></li>
        	<li>渠&nbsp;&nbsp;&nbsp;&nbsp;道：
        			<select name="channel" id="channel">
        				<c:forEach items="${channel_types }" var="s" varStatus="index">
	        				<option id="channel${index.count }" value="${s.key }">
	        					${s.value }
	        				</option>
						</c:forEach>	
        			</select>
        	</li>
        </ul>
        <p><input type="button" value="提 交" class="btn" id="btnModify" onclick="submitForm()"/></p>
        </form>
	</div>

 
</body>
</html>
