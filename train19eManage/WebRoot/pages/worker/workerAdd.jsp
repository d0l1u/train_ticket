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
$().ready(function() { 
	$("#updateForm").validate(); 
});
	function submitForm(){
	
		$("#updateForm").submit();
		
	}

	
</script>
</head>

<body>

	<div class="book_manage account_manage oz">
	<form action="/worker/addWorker.do" method="post" name="updateForm" id="updateForm">
    	
        <ul class="order_num oz">
        	<li>员工名：<input type="text" name="worker_name" class="{required:true,messages:{required:'请输入名称！'}}" /></li>
            <li>员工类型：
            	<select name="worker_type">
            		<c:forEach items="${workerType }" var="t">
            		<option value="${t.key }">${t.value}</option>
            		</c:forEach>
            	</select>
            </li>
            <li>最大订单数：<input type="text" name="max_order_num" class="{required:true,messages:{required:'最大订单数！'}}" /></li>
            <li>扩展参数：<input type="text" name="worker_ext" class="{required:true,messages:{required:'扩展参数！'}}" /></li>
            <li>优先级：<input type="text" name="worker_priority" class="{required:true,messages:{required:'请输入优先级！'}}" /></li>
            
        </ul>
        <p><input type="button" value="提 交" class="btn" onclick="submitForm()"/></p>
        </form>
	</div>

 
</body>
</html>
