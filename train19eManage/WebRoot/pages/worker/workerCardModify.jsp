<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>切换支付机器人卡号状态页面</title>
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

	<div class="book_manage account_manage oz" style="margin-top:100px">
	<form action="/worker/queryWorkerList.do" method="post" name="updateForm" id="updateForm">
		<input type="hidden" name="worker_id" value="${worker_id }"/>
		<p>人员名称：<span style="color:#f60;">${worker_name }</span></p>
        <c:if test="${!empty isShowList}">
        <table style="width:500px;">
           <tr style="background:#EAEAEA; ">
           		<th>序号</th>
                <th>卡号</th>
                <th>状态</th>
                <th>操作</th>
            </tr>
            <c:forEach var="list" items="${workerList}" varStatus="idx">
	            <tr><td>${idx.index+1}</td>
	                <td>${list.card_no}</td>
	                <td><c:if test="${list.card_status eq '22' }">
	                		暂停
	                	</c:if>
	                	<c:if test="${list.card_status eq '11' }">
	                		工作
	                	</c:if>
	                </td>
	                <td>
	                	<c:if test="${list.card_status eq '22' }">
	                		<a href="/worker/updateWorkerCardStatus.do?worker_id=${worker_id }&card_no=${list.card_no }">启用</a>
	                	</c:if>
	                	<c:if test="${list.card_status eq '11' }">
	                		启用中
	                	</c:if>
	                </td>
	            </tr>
            </c:forEach>
        </table>
        </c:if>
        <br/><br/>
        <p><input type="button" value="提 交" class="btn" onclick="submitForm()"/>
        <input type="button" value="返 回" class="btn btn_normal" onclick="javascript:history.back(-1);" /></p>
        </form>
	</div>

 
</body>
</html>
