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
		alert(${account.channel_types });
		if(confirm("是否提交？")){
			$("#updateForm").submit();
		}
	}
	function submitUpdateInfo(){
		
		if(confirm("确认修改么？")){
			$("form:first").attr("action","/account/updateAccount.do");
			var index = parent.layer.getFrameIndex(window.name);
			
			$("form:first").submit();
			parent.reloadPage();
			parent.layer.close(index);
		}
	}
	
</script>
</head>

<body>

	<div class="book_manage account_manage oz">
	<form action="/account/updateAccount.do" method="post" name="updateForm">
	<br/><br/><br/><br/>
	<input type="hidden" name="acc_id" value="${account.acc_id }"/>
    	<ul class="ser oz">
        			<li>省:${account. province_name}
            		</li>
           			 <li>市:${account. city_name}
           		 </li>
        </ul>
        <ul class="order_num oz">
        	<li>登陆名&nbsp;&nbsp;：<input type="text" name="acc_username" id="acc_username" value="${account.acc_username }"/></li>
            <li>登陆密码：<input type="text" name="acc_password" id="acc_password" value="${account.acc_password }"/></li>  
            <li>真实姓名：<input type="text" name="real_name" id="real_name" value="${account.real_name }"/></li>  
            <li>身份证号：<input type="text" name="id_card" id="id_card" value="${account.id_card }"/></li>  
            <li>邮&nbsp;&nbsp;&nbsp;&nbsp;箱：<input type="text" name="acc_mail" id="acc_mail" value="${account.acc_mail }"/></li> 
            <li>渠&nbsp;&nbsp;&nbsp;&nbsp;道：
            	<select name="channel" id="channel">
            		<c:forEach items="${channel_types }" var="s" varStatus="index">
	        			<option id="channel${index.count }" value="${s.key }" 
	        				<c:if test="${fn:contains(account.channel, s.key ) }">selected</c:if>>
	        				${s.value }
	        			</option>
					</c:forEach>
            	</select>
            </li> 
        </ul>
        <p><input type="button" value="更新" class="btn" onclick="submitUpdateInfo()"/></p>
        </form>
	</div>

 
</body>
</html>
