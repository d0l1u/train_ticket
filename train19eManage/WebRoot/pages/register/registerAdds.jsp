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
		<script type="text/javascript">	


		function importExcel(){
			var file = $.trim($("#excelFile").val());
			var fileType = (file.substring(file.lastIndexOf(".")+1,file.length)).toLowerCase();  
			//alert(fileType);
			if(file==""){
				alert("请选择'.xls'格式的文件！");
				$("#excelFile").focus();
				return;
			}else if(fileType != "xls"){
				alert("请选择'.xls'格式的文件！");
				$("#excelFile").focus();
				return;
			}
			//$("form:first").attr("method","post");
			$("form:first").attr("action","/register/addExcelRegister.do?file="+file);
			$("form:first").submit();
		}


	
	
</script>
</head>
<body>
	<form action="/register/queryRegisterList.do" method="post" enctype="multipart/form-data" id="addForm">
	<div class="book_manage account_manage oz" >
		<br/><br/><br/><br/>
		选择文件：<input type="file" name="excelFile" id="excelFile" /><br/><br/><br/>
		<p><input type="button" value="批量导入" class="btn btn_normal" id="import" onclick="importExcel()"/>
		<input type="button" value="返 回" class="btn btn_normal" onclick="javascript:history.back(-1);" /></p><br/>
	</div>
	</form>
</body>
</html>