<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>修改用户信息明细页</title>
<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" type="text/javascript" src="/js/jquery.validate.js"></script>
<script language="javascript" type="text/javascript" src="/js/jquery.metadata.js"></script>
<script type="text/javascript">
	function submitForm(){
		
		if($.trim($("#password").val())==""){
		$("#password").focus();
			alert("登录密码不能为空！");
			return;
		}
		if(($.trim($("#password").val()))!= ($.trim($("#password1").val()))){
			alert("登录密码与确认密码不一致！");
			return;
		}
		if($.trim($("#email").val())==""){
		$("#email").focus();
			alert("邮箱不能为空！");
			return;
		}
		
		if($.trim($("#user_phone").val())==""){
		$("#user_phone").focus();
			alert("手机号不能为空！");
			return;
		}
		$("#updateForm").submit();
		
	}
	
</script>
</head>

<body>
	<div class="book_manage account_manage oz">
	<form action="/login/updateUserInfo.do" method="post" name="updateForm" id="updateForm">
		<br/><br/><br/>
        <ul class="order_num oz">
        	<li><input type="hidden" name="user_id" value="${userInfo_Map.user_id }"/></li>
        	<li>用户：&nbsp;&nbsp;&nbsp;&nbsp;${userInfo_Map.real_name}</li>
            <li>新密码：<input type="password" name="password" id="password"/></li>
            <li>确认密码：<input type="password" name="password1" id="password1"/></li>
        	<li>邮箱：<input type="text" name="email" id="email" value="${userInfo_Map.email }"/></li>
        	<li>手机：<input type="text" name="user_phone" id="user_phone" value="${userInfo_Map.user_phone }"/></li>
        	<c:if test="${user_Session_level eq '2'}">
	        	<li>级别:
	        		<select name="user_level" id="user_level">
	        			<c:forEach items="${userType }" var="t">
	            			<option value="${t.key }" <c:if test="${t.key eq  userInfo_Map.user_level}">selected="selected"</c:if> >${t.value}</option>
	            		</c:forEach>
	       			</select>
	        	</li>
        	</c:if>
        	<c:if test="${user_Session_level eq '2'}">
	        	<li>状态:
	        		<select name="user_status" id="user_status">
	        			<c:forEach items="${userStatus }" var="t">
	            			<option value="${t.key }" <c:if test="${t.key eq  userInfo_Map.user_status}">selected="selected"</c:if> >${t.value}</option>
	            		</c:forEach>
	       			</select>
	        	</li>
        	</c:if>
        	<c:if test="${user_Session_level eq '2'}">
	        	<li>备注:
	        		<input type="text" name="remark" value="${userInfo_Map.remark }"/>
	        	</li>
        	<li>负责区域:
        		<br />
	            <c:forEach items="${province }" var="s" varStatus="index">
					<input type="checkbox" id="area_no${index.count }" name="supervise_name" value="${s.area_name }"/><label for="area_no${index.count }">${s.area_name }</label>
				</c:forEach>	
            </li>
        	</c:if>
        </ul>
        <p><input type="button" value="提 交" class="btn" id="btnModify" onclick="submitForm()"/>
         <input type="button" value="返 回" class="btn btn_normal"
					onclick="javascript:history.back(-1);" /></p>
        </form>
	</div>
</body>
</html>
