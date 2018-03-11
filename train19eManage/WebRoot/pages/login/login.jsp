<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>后台登陆管理页面</title>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery.cookie.js"></script>
<script type="text/javascript">

	function submitForm(){
	 //保存用户信息  
	if ($("#rmbUser").attr("checked") == "checked") {  
	   var userName = $("#user_name").val();  
	   var passWord = $("#password").val();  
	   $.cookie("rmbUser", "true", {expires : 7});
	   $.cookie("userName", userName, {expires : 7});
	   $.cookie("passWord", passWord, {expires : 7}); 
	  } else {  
	   $.cookie("rmbUser", "false");  
	   $.cookie("userName", null);  
	   $.cookie("passWord", null);  
	  }  
	
	
		if($.trim($("#user_name").val())==""){
		$("#user_name").focus();
			alert("登录名不能为空！");
			return;
		}
		if($.trim($("#password").val())==""){
		$("#password").focus();
			alert("登录密码不能为空！");
			return;
		}
		if($.trim($("#user_name").val())!="" && $.trim($("#password").val())!="" && $.trim($("#checkCode").val())){
			var str = document.getElementById("user_name").value ;
			var user_name = $.trim(str);
			var str1 = document.getElementById("password").value ;
			var password = $.trim(str1);
			var str2 = document.getElementById("checkCode").value ;
			var checkCode = $.trim(str2);
			
			var url = "/login/login.do?user_name="+user_name+"&password="+password+"&checkCode="+checkCode+"&version="+new Date();
			$.get(url,function(data){
				if(data == "yes")
					window.location="/index.jsp?version="+new Date();
				else if(data == "wait")
					alert("您的账号还没有审核通过");
				else if(data == "notthrough")
					alert("您的账号审核未通过");
				else if(data == "isClose")
					alert("此账号已停用");
				else if(data == "checkCodeNo")
					alert("验证码输入错误");
				else
					alert("用户名或密码错误");
			});
		}
	}
	
	function refresh(obj){
	   obj.src = "/imageServlet?"+Math.random();   
	 }
	 
	 
	 
	 

 $(document).ready(function() {  
  if ($.cookie("rmbUser") == "true") {
   $("#rmbUser").attr("checked", true);  
   $("#user_name").val($.cookie("userName"));  
   $("#password").val($.cookie("passWord"));  
  }  
 });  


</script>
<style type="text/css">
	body,p,h1,dl,dt,dd,img,input{margin:0;padding:0;font-family:"Simsun";font-size:12px;}
	body{color:#eef1f6;}
	li{list-style:none;}
	a{text-decoration:none;}
	input,select{font-size:12px;}
	.back-login-wrap{width:840px;margin:0 auto;}
	.oz{overflow:hidden;zoom:1;}
	.spacer{content:".";height:0;clear:both;visibility:hidden;font-size:0;line-height:0;}
	h1{width:393px;height:65px;padding:14px 0 8px 0;}
	.login-wrap{width:840px;height:356px;background:url(/images/login-bg.png) no-repeat;}
	.login{padding:80px 0 0 18px;width:256px;}
	.login dl{padding-bottom:12px;}
	.login dt{float:left;color:#555;line-height:26px;padding-right:4px;width:66px;text-align:right;}
	.login dd{float:left;}
	.login dl .text{border:1px solid #c8c8c8;width:156px;height:20px;line-height:20px;padding:2px;}
	.login dl .yz-text{width:76px;}
	.login .pwd{overflow:hidden;padding-left:70px;*padding-left:66px;line-height:22px;}
	.login .pwd input{vertical-align:middle;}
	.login .pwd label{padding-left:6px;color:#555;vertical-align:middle;}
	.login .pwd a{float:right;color:#0a85e5;padding-right:26px;white-space:nowrap; whi}
	.login .pwd a:hover{color:#f60;text-decoration:underline;}
	.login .btn{padding:14px 0 0 148px;}
	.login .btn input{border:none;text-align:center;width:82px;height:33px;background:url(/images/btn.png) no-repeat;cursor:pointer;}
	.footer{margin-top:40px;background:url(/images/foot-bg.png) no-repeat center top;padding-top:14px;}
	.footer p{line-height:26px;color:#666;text-align:center;}
</style>
</head>

<body>
	<div class="back-login-wrap oz">
    	<h1><img src="/images/logo-train.png" width="393" height="65" /></h1>
        <div class="login-wrap ">
        	<div class="login oz">
            	<dl class="oz">
                	<dt>用户名：</dt>
                    <dd><input type="text" class="text" name="user_name" id="user_name"/></dd>
                </dl>
                <dl class="oz">
                	<dt>密&nbsp;&nbsp;码：</dt>
                    <dd><input type="password" class="text" name="password" id="password"/></dd>
                </dl>
                  <dl class="oz" >
                	<dt>验证码：</dt>
                    <dd><input type="text" class="text yz-text" name="checkCode" id="checkCode"  onkeydown="if (event.keyCode==13) submitForm();"/></dd>
                    <dd><img src="/imageServlet" width="79" height="26" alt="" title="点击更换" onclick="javascript:refresh(this);" /></dd>
                  </dl>
                   
                <p class="pwd">
                	<!-- <a href="#">忘记密码</a> --> <input type="checkbox" value="" id="rmbUser" /><label for="rmbUser">记住密码</label>
                </p>
                <p class="btn" style="margin-top:40px;">
                	<input type="button" onclick="submitForm()"/>
                </p>
            </div>
        </div>
        <div class="footer oz">
        	<p>北京一九易站电子商务有限公司</p>
        </div>
    </div>
</body>
</html>
