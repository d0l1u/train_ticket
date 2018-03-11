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

		function importExcel(){
			var file = $.trim($("#excelFile").val());
			
			var fileType = (file.substring(file.lastIndexOf(".")+1,file.length)).toLowerCase();  
			if(file==""){
				alert("请选择'.xls'或'.xlsx'格式的文件！");
				$("#excelFile").focus();
				return;
			}else if(fileType != "xls" && fileType != "xlsx" ){
				alert("请选择'.xls'或'.xlsx'格式的文件！");
				$("#excelFile").focus();
				return;
			}
			if(confirm("确定上传？")){
			$("form:first").attr("action","/meituanStation/meituanAddRefunds.do?file="+file);
			$("form:first").submit();
			}
		}

		function goback(){
			$("form:first").attr("action","/meituanStation/queryMeituanStationPage.do");
			$("form:first").submit();
		}
	
	
</script>

 
</head>
<body>
	<form action="/meituanStation/meituanAddRefunds.do" method="post" enctype="multipart/form-data" id="addForm" onsubmit="return checkSubmit();">
	<div class="book_manage account_manage oz" >
		<br/><br/><br/><br/>

		选择文件：<input type="file" name="excelFile" id="excelFile" /><br/><br/><br/>
		<p><input type="button" value="批量导入" class="btn btn_normal" id="import" onclick="importExcel()"/>
		<input type="button" value="返 回" class="btn btn_normal" onclick="javascript:goback();" /></p><br/>
	</div>
	</form>
	
</body>
</html>

