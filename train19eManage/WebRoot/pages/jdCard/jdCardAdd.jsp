<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>京东预付卡添加页</title>
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
		if($.trim($("#card_no").val())==""){
		$("#card_no").focus();
			alert("预付卡号不能为空！");
			return;
		}
		
		if($.trim($("#card_pwd").val())==""){
		$("#card_pwd").focus();
			alert("密码不能为空！");
			return;
		}


		if($.trim($("#card_amount").val())==""){
			$("#card_amount").focus();
				alert("预付卡面值不能为空！");
				return;
			}
		if($.trim($("#card_money").val())==""){
			$("#card_money").focus();
				alert("预付卡余额不能为空！");
				return;
			}
			
			if($.trim($("#become_due_time").val())==""){
			$("#become_due_time").focus();
				alert("预付卡到期时间不能为空！");
				return;
			}
			
	    var card_no = document.getElementById("card_no").value;
		var card_pwd = document.getElementById("card_pwd").value;
		var card_amount = document.getElementById("card_amount").value;
		var card_money = document.getElementById("card_money").value;
		var become_due_time = document.getElementById("become_due_time").value;
		var url = "/jdCard/addJdCard.do?card_no="+card_no+"&card_pwd="+card_pwd+"&card_amount="+card_amount
		+"&card_money="+card_money+"&become_due_time="+become_due_time;
		
		
		$.post(url,function(data){
			if(data=="yes"){
				var index = parent.layer.getFrameIndex(window.name);
				$("form:first").submit();
				parent.reloadPage();
				parent.layer.close(index);
			}
			
		});
		
	}


</script>
</head>

<body>

	<div class="book_manage account_manage oz">
	<form action="/jdCard/addJdCard.do" method="post" name="updateForm" id="updateForm">
	<br/><br/><br/><br/>
        <ul class="order_num oz">
        	<li>卡号&nbsp;&nbsp;：<input type="text" name="card_no" id="card_no"/></li>
            <li>密码&nbsp;&nbsp;：<input type="text" name="card_pwd" id="card_pwd" /></li>
            <li>面额&nbsp;&nbsp;：<input type="text" name="card_amount" id="card_amount" /></li>
            <li>余额&nbsp;&nbsp;：<input type="text" name="card_money" id="card_money" /></li>
            <li>到期时间：<input type="text" name="become_due_time" id="become_due_time" /></li>
        </ul>
        <p><input type="button" value="提 交" class="btn" id="btnModify" onclick="submitForm()"/></p>
        </form>
	</div>

 
</body>
</html>
