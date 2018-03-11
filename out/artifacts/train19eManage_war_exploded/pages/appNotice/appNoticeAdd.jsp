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
		if($.trim($("#notice_name").val())==""){
			$("#notice_name").focus();
			alert("标题不能为空！");
			return;
		}
		if($.trim($("#notice_content").val())==""){
			$("#notice_content").focus();
			alert("内容不能为空！");
			return;
		}
		if(($.trim($("#notice_system").val())=="33") && ($.trim($("#user_phone").val())=="")){
			//$("#bank_username").focus();
			alert("用户电话不能为空！");
			return;
		}
		$("#updateForm").submit();
	}
	$(function() {
        $('#notice_system').change(function() {
            if (this.value == '33') {
                $('#user_phone_div').show();
                //document.getElementById('user_phone_div').style.display = "block";
                //setInterval('onblurBankType1()',500);
            } else {
                $('#user_phone_div').hide();
               // $("#bank_type_info").text("乘客银行类型填写正确").css("color","#0b0");
    			//bankType1 = true;
            }
        });
    });
</script>
</head>

<body>

	<div style="width:600px;" class="book_manage account_manage oz">
	<form action="/appNotice/addNotice.do" method="post" name="updateForm" id="updateForm">
        <ul class="order_num oz" style="margin-top:100px;">
        	<li>标&nbsp;&nbsp;&nbsp;&nbsp;题：&nbsp;
        		<input type="text" name="notice_name" id="notice_name"
        			class="{required:true,messages:{required:'请输入标题'}}" style="width:295px;" />
        	</li>
            <li>
            	<label style="float:left;">内&nbsp;&nbsp;&nbsp;&nbsp;容：</label>&nbsp;
            	<textarea name="notice_content" id="notice_content"
            		style="border:1px solid #dadada; width: 300px; height: 200px;" 
            		class="{required:true,messages:{required:'请输入内容'}}">
            	</textarea>
            </li>
            <!-- 
            <li>发布状态：<select name="notice_status" style="width:100px;">
            		<c:forEach items="${noticeStatusMap }" var="p">
						<option value="${p.key }">${p.value}</option>
					</c:forEach>
            	</select>
            </li>
             -->
            <li>发布对象：&nbsp;
            	<select name="notice_system" id="notice_system" style="width:100px;">
            		<c:forEach items="${noticeSystemMap }" var="p">
						<option value="${p.key }">${p.value}</option>
					</c:forEach>
            	</select>
            	<div id="user_phone_div" style="display:none;"><br/>
            		手机号码：&nbsp;
            		<input type="text" name="user_phone" id="user_phone" 
            			class="{required:true,messages:{required:'请输入手机号码'}}" style="width:295px;" /> 
            		<span>*多个号码请用“,”隔开</span>
            	</div>
            </li>
            <!--  
            <li>生效时间:<input type="text" class="text" id="pub_time" name="pub_time" readonly="readonly" 
					  				onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d'})" class="{required:true,messages:{required:'请输入生效日期'}}"/></li>
            <li>到期时间:<input type="text" class="text" id="stop_time" name="stop_time"  readonly="readonly" 
					  				onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d'})" class="{required:true,messages:{required:'请输入失效日期'}}"/></li>
            <li>发布区域:
            <c:forEach items="${province }" var="s" varStatus="index">
				<input type="checkbox" id="area_no${index.count }" name="area_no" value="${s.area_name }"/><label for="area_no${index.count }">${s.area_name }</label>
			</c:forEach>
            </li>  -->
        </ul>
        <br/>
        <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        	<input type="button" value="发布" class="btn" onclick="submitForm()"/>
        	<input type="button" value="返回" class="btn" onclick="history.back(-1)"/></p>
        </form>
	</div>

 
</body>
</html>
