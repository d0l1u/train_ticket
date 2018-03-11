<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加机器人页面</title>
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
		
		if($.trim($("#robot_name").val())==""){
		$("#robot_name").focus();
			alert("机器人名称不能为空！");
			return;
		}
		
		if($.trim($("#robot_text").val())==""){
		$("#robot_text").focus();
			alert("请求网址不能为空！");
			return;
		}

//		if($.trim($("#robot_con_timeout").val())==""){
//			$("#robot_con_timeout").focus();
//				alert("连接超时时间不能为空！");
//				return;
//			}

//		if($.trim($("#robot_read_timeout").val())==""){
//			$("#robot_read_timeout").focus();
	//			alert("读取超时时间不能为空！");
//				return;
//			}
		if($.trim($("#priority").val())==""){
			$("#priority").focus();
				alert("读取超时时间不能为空！");
				return;
			}
		
		$("#updateForm").submit();
		
	}
	function onblurUsername(){
		var robot_name = $.trim( $("#robot_name").val() );
			robot_name = encodeURI(robot_name);
		if(robot_name==""){
			$("#username_info").text("机器人名称不能为空!").css("color","#f00");
		}else{
			var url = "/robotSet/queryRobot_name.do?robot_name="+robot_name+"&version="+new Date();
			$.get(url,function(data){
				if(data == "yes"){
					$("#username_info").text("可添加新的机器人!").css("color","#0b0");
				}else{
					$("#username_info").text("机器人已存在，可添加新功能！").css("color","#f00");
				}
			});
		}
	}

	function focusUsername(){
		$("#username_info").text("");
	}
	
	function updateOrinsert(){
		var robot_name = $.trim( $("#robot_name").val() );
			robot_name = encodeURI(robot_name);
//		var robot_setList =$.trim( $("#robot_setList").val() );
//		alert(robot_setList);
		
//		$("form:first").attr("action", "/robotSet/queryRobot.do?robot_name="+robot_name+"robot_setList"+robot_setList);
//		$.get(url,function(data){
//				if(data == "no"){
//					alert("机器人该功能已添加，不可重复添加！");
//				}else{
			$("form:first").attr("action", "/robotSet/addRobotSetting.do?robotName="+robot_name);
			$("form:first").submit();
//			}
	}
	
</script>
</head>

<body>

	<div class="book_manage account_manage oz">
	<form action="/robotSet/queryRobotSetting.do" method="post" name="updateForm" id="updateForm">
	<br/><br/><br/><br/>
  
        <ul class="order_num oz">
        	<li>机器人名称：&nbsp;&nbsp;&nbsp; &nbsp;<input type="text" name="robot_name" id="robot_name" 
        	onblur="onblurUsername()" onfocus="focusUsername()" /><span id="username_info"></span></li>
        	<li>添加功能：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        	<select name="robot_setList" id="robot_setList" style="height: 20px; width: 66px; ">
				<c:forEach items="${robot_setList }" var="p">
					<option value="${p.key }" >
						${p.value}
					</option>
				</c:forEach>
			</select>
        	</li>
            <li>请求网址：&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;<input type="text" name="robot_text" id="robot_text" /></li>
        	<li>机器公网IP：&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;<input type="text" name="public_ip" id="public_ip" /></li>
			<!--	
			<li>连接超时时间：&nbsp;&nbsp;<input type="text" name="robot_con_timeout" id="robot_con_timeout" /></li>
            <li>读取超时时间：&nbsp;&nbsp;<input type="text" name="robot_read_timeout" id="robot_read_timeout" /></li>
             -->
            <li>机器提供商：&nbsp;&nbsp;&nbsp;&nbsp;  <select name="worker_vendor" id="worker_vendor" style="width: 99px; height: 19px">
				<option value="" >
						空
				</option>
				<c:forEach items="${worker_vendorList}" var="p">
					<option value="${p.key }" >
						${p.value}
					</option>
				</c:forEach>
			</select>
        	</li>
        	<li>机器区域：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <select name="worker_region" id="worker_region" style="width:120px;">
        		<option value="" >
						空
				</option>
				<c:forEach items="${worker_regionList}" var="p">
					<option value="${p.key }" >
						${p.value}
					</option>
				</c:forEach>
			</select>
        	</li>
        	<li>机器语言：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        	<select name="worker_language" id="worker_language" style="width:120px;">
				<c:forEach items="${worker_languageList}" var="p">
					<option value="${p.key}" >
						${p.value}
					</option>
				</c:forEach>
			</select>
        	</li>       
            <li>机器描述：&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;<input type="text" name="worker_describe" id="worker_describe" /></li>     
         <!--    <li>优先级：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="priority" id="priority" /></li> -->
        	<li>说明：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;<input type="text" name="robot_desc" id="robot_desc" /></li>
        </ul>
        <p><input type="button" value="提 交" class="btn" id="btnModify" onclick="updateOrinsert()"/></p>
        </form>
	</div>

 
</body>
</html>
