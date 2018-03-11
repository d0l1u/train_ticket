<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加用户信息明细页</title>
<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" type="text/javascript" src="/js/jquery.validate.js"></script>
<script language="javascript" type="text/javascript" src="/js/jquery.metadata.js"></script>
<script type="text/javascript">
	function submitForm(){
		if($.trim($("#user_name").val())==""){
		$("#user_name").focus();
			alert("登录账号不能为空！");
			return;
		}
		if($.trim($("#real_name").val())==""){
		$("#real_name").focus();
			alert("真实姓名不能为空！");
			return;
		}
		if($.trim($("#password").val())==""){
		$("#password").focus();
			alert("登录密码不能为空！");
			return;
		}
		if(($.trim($("#password").val()))!= ($.trim($("#password1").val()))){
			alert("登录密码与确认密码不一致！");
			return;
		}
		if(($.trim($("#user_level").val())) == '1.1' && $(".supervise_name:checked").length== '0'){
			alert("省级负责人，必须有负责省市");
			return;
		}
		
		/*
		if($.trim($("#email").val())==""){
		$("#email").focus();
			alert("邮箱不能为空！");
			return;
		}
		
		if($.trim($("#user_phone").val())==""){
		$("#user_phone").focus();
			alert("手机号不能为空！");
			return;
		}
		*/
		$("#updateForm").submit();
		
	}
	function onblurUsername(){
		var str = document.getElementById("user_name").value ;
		var name = $.trim(str);
		if(name==""){
		$("#username_info").text("用户名不能为空!").css("color","#f00");
		}else{
			var url = "/login/queryUsername.do?user_name="+name;
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
	function onblurRealname(){
		var str = document.getElementById("real_name").value ;
		var name=$.trim(str);
		if(name==""){
		$("#realname_info").text("真实姓名不能为空!").css("color","#f00");
		}else{
			var url = "/login/queryRealname.do?real_name="+name;
			$.get(url,function(data){
				if(data == "yes"){
					$("#btnModify").attr("disabled", false);
				}else{
					$("#realname_info").text("姓名重复，请添加_等标识符，以便区分！").css("color","#f00");
					$("#btnModify").attr("disabled", true);
				}
			});
		}
	}
	
	function focusUsername(){
	$("#username_info").text("");
	}
	function focusRealname(){
	$("#realname_info").text("");
	}
	
</script>
</head>

<body>
	<div class="book_manage account_manage oz">
	<form action="/login/addUser.do" method="post" name="updateForm" id="updateForm">
		<br/><br/><br/>
        <ul class="order_num oz">
        	<li><input type="hidden" name="user_id" value="${userInfo_Map.user_id }"/></li>
        	<li>登陆账号：<input type="text" name="user_name" id="user_name" onblur="onblurUsername()" onfocus="focusUsername()"/><span id="username_info"></span></li>
        	<li>真实姓名：<input type="text" name="real_name" id="real_name"  onblur="onblurRealname()" onfocus="focusRealname()"/><span id="realname_info"></span></li>
            <li>登录密码：<input type="password" name="password" id="password"/></li>
            <li>确认密码：<input type="password" name="password1" id="password1"/></li>
        	<li>邮箱：<input type="text" name="email" id="email" value="${userInfo_Map.email }"/></li>
        	<li>手机：<input type="text" name="user_phone" id="user_phone" value="${userInfo_Map.user_phone }"/></li>
        	<li>级别:
        		<select name="user_level" id="user_level">
        			<c:forEach items="${userType }" var="t">
            			<option value="${t.key }">${t.value}</option>
            		</c:forEach>
       			</select>
        	</li>
        	<li>备注:
        		<input type="text" name="remark" value="${userInfo_Map.remark }"/>
        	</li>
        	<li>负责区域:
        		<br />
	            <c:forEach items="${province }" var="s" varStatus="index">
					<input type="checkbox" class="supervise_name" id="area_no${index.count }" name="supervise_name" value="${s.area_name }"/><label for="area_no${index.count }">${s.area_name }</label>
				</c:forEach>	
            </li>
        </ul>
        <p><input type="button" value="提 交" class="btn" id="btnModify" onclick="submitForm()"/>
        <input type="button" value="返 回" class="btn btn_normal"
					onclick="javascript:history.back(-1);" /></p>
        </form>
	</div>
</body>
</html>
