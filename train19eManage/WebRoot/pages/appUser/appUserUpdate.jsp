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
<link rel="stylesheet" href="/css/style.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/layer/layer.js"></script>
<script language="javascript" src="/js/mylayer.js"></script>
<script type="text/javascript">
function submitForm(){
	if($.trim($("#user_password").val())==""){
		$("#user_password").focus();
		alert("请输入密码！");
		return;
	}
	if($.trim($("#user_password1").val())==""){
		$("#user_password1").focus();
		alert("请确认密码！");
		return;
	}
	if($.trim($("#user_password1").val())!=$.trim($("#user_password").val())){
		$("#user_password1").focus();
		alert("两次密码不一致！");
		return;
	}
	var index = parent.layer.getFrameIndex(window.name);
	
	$("#updateForm").submit();
	parent.reloadPage();
	parent.layer.close(index);
	
}

function onblurPassword(){
	$("#user_password_info").text("");
	var user_password=$.trim( document.getElementById("user_password").value );
	if(user_password==""){
		$("#user_password_info").text("修改密码不能为空!").css("color","#f00");
	}
}

function onblurPassword1(){
	$("#user_password1_info").text("");
	var user_password1=$.trim( document.getElementById("user_password1").value );
	var user_password=$.trim( document.getElementById("user_password").value );
	if(user_password1==""){
		$("#user_password1_info").text("确认密码不能为空!").css("color","#f00");
	}else if(user_password1 != user_password){
		$("#user_password1_info").text("两次密码不一致!").css("color","#f00");
	}
}
</script>

</head>

<body>
	<div class="content1 oz">
    	<!--左边内容 start-->
    	<div class="left_con oz">
         	<div class="pub_order_mes oz mb10_all" style="margin-top:100px;">
            	<h4>修改密码</h4>
                <div class="pub_con">
                <form action="/appUser/updateAppUser.do?user_id=${userInfo.user_id }" method="post" name="updateForm" id="updateForm" >
                	<table class="pub_table" style="margin:50px 150px;">
                    	<tr>
                        	<td>姓&nbsp;&nbsp;&nbsp;&nbsp;名：<span>${userInfo.user_name}</span></td>
                        </tr>
                        <tr>
                         	<td>账&nbsp;&nbsp;&nbsp;&nbsp;号：<span>${userInfo.user_phone }</span></td>
                        </tr>
                        <tr>
                        	<td>修改密码：<input type="text" name="user_password" id="user_password" onblur="onblurPassword()"/><span id="user_password_info"></span></td>
                        </tr>
                        <tr>
                        	<td>确认密码：<input type="text" name="user_password1" id="user_password1" onblur="onblurPassword1()"/><span id="user_password1_info"></span></td>
                        </tr>
                        <tr>
                        	<td>&nbsp;&nbsp;&nbsp;&nbsp;
                        		<input type="button" value="确定" class="btn btn_normal" onclick="submitForm()"/>
                        		<input type="button" value="返 回" class="btn btn_normal" onclick="backPage();"/>
                        	</td>
                        	
                        </tr>
                    </table>
			        </form>
                </div>
            </div>

        <!--左边内容 end-->
    </div>
</body>
</html>
