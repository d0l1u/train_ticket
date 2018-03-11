<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>通知——增加通知</title>
<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
<script language="javascript" type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" type="text/javascript" src="/js/datepicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript" src="/js/jquery.validate.js"></script>
<script language="javascript" type="text/javascript" src="/js/jquery.metadata.js"></script>
<script type="text/javascript">
	$().ready(function() { 
		$("#updateForm").validate(); 
	});
	function submitForm(){
		$("#updateForm").attr("action", "/extNotice/addNotice.do");
		$("#updateForm").submit();
		
	}
	//全选
		function selectAll(){
			var checklist = document.getElementsByName("ext_channel");
			if(document.getElementById("controlAll").checked){
				for(var i=0; i<checklist.length; i++){
					checklist[i].checked = 1;
				}
			}else{
				for(var j=0; j<checklist.length; j++){
					checklist[j].checked = 0;
				}
			}
		}
	
	
</script>
</head>

<body>

	<div class="book_manage account_manage oz">
	<form action="/extNotice/addNotice.do" method="post" name="updateForm" id="updateForm">
        <ul class="order_num oz">
        	<li>标题：<input type="text" name="notice_name" class="{required:true,messages:{required:'请输入标题'}}" />
        	</li>
            <li>内容：<textarea name="notice_content" style="border:1px solid #dadada; width: 300px; height: 200px;" class="{required:true,messages:{required:'请输入内容'}}"></textarea></li>
             <li>渠&nbsp;&nbsp;&nbsp;&nbsp;道:
             <input type="checkbox" onclick="selectAll()" name="controlAll" style="controlAll" id="controlAll"/><label for="controlAll">全部</label>
            		<c:forEach items="${ext_channel}" var="rc" varStatus="index">
							<input type="checkbox" id="ext_channel" 
								name="ext_channel" value="${rc.key }" />
							<label for="ext_channel">
								${rc.value }
							</label>
						</c:forEach>
            </li>
            <li>发布状态:
            	<select name="notice_status">
            		<c:forEach items="${noticeStatusMap }" var="p">
						<option value="${p.key }">${p.value}</option>
					</c:forEach>
            	</select>
            </li>
            <li>生效时间:<input type="text" class="text" id="pub_time" name="pub_time" readonly="readonly" 
					  				onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d'})" class="{required:true,messages:{required:'请输入生效日期'}}"/></li>
            <li>到期时间:<input type="text" class="text" id="stop_time" name="stop_time"  readonly="readonly" 
					  				onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d'})" class="{required:true,messages:{required:'请输入失效日期'}}"/></li>
           <!--   <li>发布区域:
            <c:forEach items="${province }" var="s" varStatus="index">
				<input type="checkbox" id="area_no${index.count }" name="area_no" value="${s.area_name }"/><label for="area_no${index.count }">${s.area_name }</label>
			 </c:forEach>
            </li>  -->
        </ul>
        <p><input type="button" value="发布" class="btn" onclick="submitForm()"/>
        <input type="button" value="返回" class="btn" onclick="history.back(-1)"/></p>
        </form>
	</div>

 
</body>
</html>
