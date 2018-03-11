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
				alert("请选择'.xls'或'.xlsx'或'.zip'或'.rar'或'.csv'格式的文件！");
				$("#excelFile").focus();
				return;
			}else if(fileType != "xls" && fileType != "xlsx" && fileType != "zip"&& fileType != "rar" && fileType != "csv"){
				alert("请选择'.xls'或'.xlsx'或'.zip'或'.rar'或'.csv'格式的文件！");
				$("#excelFile").focus();
				return;
			}
			if(confirm("确定上传？")){
			var add_type = document.getElementById("add_type").value;
				if(add_type=="11"){
					$("form:first").attr("action","/checkPrice/addAlipayInfo.do?file="+file);
					$("form:first").submit();
				}else{
					var ticket_type = document.getElementById("ticket_type").value;
					$("form:first").attr("action","/checkPrice/updateOrderInfo.do?file="+file+"&ticket_type="+ticket_type);
					$("form:first").submit();
				}
			}
		}

		function goback(){
			$("form:first").attr("action","/checkPrice/queryCheckPricePage.do");
			$("form:first").submit();
		}
	
	
--></script>

 
</head>
<body>
	<form action="/checkPrice/queryCheckPricePage.do" method="post" enctype="multipart/form-data" id="addForm" onsubmit="return checkSubmit();">
	<div class="book_manage account_manage oz" >
		<br/><br/><br/><br/>

		选择文件：<input type="file" name="excelFile" id="excelFile"  multiple="multiple"/><br/><br/><br/>
		<input type="hidden" name="add_type" id="add_type" value="${add_type }" />
		<input type="hidden" name="ticket_type" id="ticket_type" value="${ticket_type }" />
		<br/>
		<font color="#F60;">
		<c:if test="${add_type eq '11'}">
		格式参考：[支付宝流水][时间][收入金额(退款)][支出金额(出票)][类型][支付宝账号]<br/><br/><br/><br/>
		</c:if>
		<c:if test="${add_type eq '22'}">
		格式参考：[订单号][支付宝流水号]  <br/><br/>
		<font color ="#8281D1;">已选择【<c:if test="${ticket_type eq '11'}">出票</c:if><c:if test="${ticket_type eq '33'}">改签</c:if>】的上传异常单</font><br/><br/>
		</c:if>
		</font>
		<p><input type="button" value="批量导入" class="btn btn_normal" id="import" onclick="importExcel()"/>
		<input type="button" value="返 回" class="btn btn_normal" onclick="javascript:goback();" /></p><br/>
	</div>
	</form>
	
</body>
</html>

