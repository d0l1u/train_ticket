<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page pageEncoding="utf-8" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>上传页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
		<script type="text/javascript"><!--
	
	
		function submitForm(){
	    	if($.trim($("#bill_time").val())==""){
	    		$("#bill_time").focus();
	    			alert("账单时间不能为空！");
	    			$("#bill_time").focus();
	    			return;
	    	}
	    	if($.trim($("#file1").val())==""){
	    		$("#file1").focus();
	    			alert("文件不能为空！");
	    			$("#file1").focus();
	    			return;
	    	}
			$("#addForm").submit();
		}


	
</script>
</head>
<body>
	<form action="/file/fileupload" method="post" enctype="multipart/form-data" id="addForm">
	<div class="book_manage account_manage oz" >
		<br/><br/><br/><br/>
		账单时间：
		<input type="text" class="text" name="bill_time" id="bill_time" readonly="readonly" value="${bill_time }"
			onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" id="defaultStartDate1"/><br/><br/><br/>
		文件：<input type="file" name="file1" id="file1"/><br/><br/><br/>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" value="上传" class="btn btn_normal" onclick="submitForm()"/><br/>
		
	</div>
	</form>
</body>
</html>