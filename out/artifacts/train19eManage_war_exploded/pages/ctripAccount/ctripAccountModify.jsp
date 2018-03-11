<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>自提订单明细页</title>

		<style type="text/css">
			.pub_table td{height:20px;}
			
			
		</style>
		
<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/layer/layer.js"></script>
<script language="javascript" src="/js/mylayer.js"></script>
<script type="text/javascript">

	function submit(){
		if(confirm("是否提交？")){
			$("#updateForm").submit();
		}
	}
	function submitUpdateInfo(){
		if(confirm("确认修改么？")){
		var ctrip_id = document.getElementById("ctrip_id").value;
		var ctrip_name = document.getElementById("ctrip_name").value;
		var ctrip_password = document.getElementById("ctrip_password").value;
		var pay_password = document.getElementById("pay_password").value;
		var ctrip_username = document.getElementById("ctrip_username").value;
		var cookie = document.getElementById("cookie").value;
		var cid = document.getElementById("cid").value;
		var auth = document.getElementById("auth").value;
		var sauth = document.getElementById("sauth").value;
		var ctrip_phone = document.getElementById("ctrip_phone").value;
		var url = "/ctripAccount/updateCtripAccountInfo.do?ctrip_id="+ctrip_id+"&ctrip_name="+ctrip_name+"&ctrip_password="+ctrip_password
		+"&pay_password="+pay_password+"&ctrip_username="+ctrip_username+"&ctrip_phone="+ctrip_phone+"&cookie="+cookie+"&cid="+cid+"&auth="+auth+"&sauth="+sauth+"&version="+new Date();
		
		if(cookie.length>200){
			alert("Cookie值长度超过200！");
			return;
		}
		
		$.post(url,function(data){
			if(data=="yes"){
				var index = parent.layer.getFrameIndex(window.name);
				$("form:first").submit();
				parent.reloadPage();
				parent.layer.close(index);
			}
		});
	 }
	}
	
</script>
</head>

<body>

	<div class="book_manage account_manage oz">
	<form action="" method="post" name="updateForm">
	<br/><br/><br/><br/>
	<input type="hidden" name="ctrip_id" id="ctrip_id" value="${ctripAccount.ctrip_id }"/>
        <ul class="order_num oz">
        	<li>登陆名&nbsp;&nbsp;：<input type="text" name="ctrip_name" id="ctrip_name" value="${ctripAccount.ctrip_name }"/></li>
            <li>登陆密码：<input type="text" name="ctrip_password" id="ctrip_password" value="${ctripAccount.ctrip_password }"/></li>
            <li>支付密码：<input type="text" name="pay_password" id="pay_password" value="${ctripAccount.pay_password }"/></li>
            <li>用户名&nbsp;&nbsp;：<input type="text" name="ctrip_username" id="ctrip_username" value="${ctripAccount.ctrip_username }"/></li>
            <li>手机号&nbsp;&nbsp;：<input type="text" name="ctrip_phone" id="ctrip_phone" value="${ctripAccount.ctrip_phone }"/></li>
            <li><label>wap端cookie&nbsp;&nbsp;：</label><textarea name="cookie" id="cookie" style="border:2px solid #dadada; width: 200px; height: 80px; font-size:10px;">${ctripAccount.cookie}</textarea></li>
            <li>wap端cid&nbsp;&nbsp;：<input type="text" name="cid" id="cid" value="${ctripAccount.cid }"/></li>
            <li>wap端auth&nbsp;&nbsp;：<input type="text" name="auth" id="auth"  value="${ctripAccount.auth}" /></li>
            <li>wap端sauth&nbsp;&nbsp;：<input type="text" name="sauth" id="sauth"  value="${ctripAccount.sauth}" /></li>         
        </ul>
        <p><input type="button" value="更新" class="btn" onclick="submitUpdateInfo()"/></p>
    </form>
	</div>

 
</body>
</html>
