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
<script type="text/javascript" src="/js/datepicker/WdatePicker.js"></script>
<script type="text/javascript">

	function submitForm(){
		if(confirm("是否提交？")){
			$("form:first").submit();
		}
	}
	
	
</script>
</head>

<body>


	<div class="book_manage account_manage oz">
	<form action="/notice/updateNotice.do" method="post" name="updateForm">
        <ul class="order_num oz">
        	<li><input type="hidden" name="notice_id" value="${notice.notice_id }"/></li>
        	<li>标题：<input type="text" name="notice_name" value="${notice.notice_name}"/></li>
            <li>内容：<textarea name="notice_content" style="border:1px solid #dadada; width: 300px; height: 200px;">${notice.notice_content}</textarea></li>
            <li>发布状态:
            	<select name="notice_status">
            		<c:forEach items="${noticeStatusMap }" var="p">
						<option value="${p.key }" <c:if test="${p.key eq notice.notice_status }">selected="selected"</c:if>>${p.value}</option>
					</c:forEach>
            	</select>
            </li>
            <li>生效时间:<input type="text" class="text" id="pub_time" name="pub_time" readonly="readonly" 
					  				onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d'})" value="${notice.pub_time }"/></li>
            <li>到期时间:<input type="text" class="text" id="stop_time" name="stop_time"  readonly="readonly" 
					  				onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d'})"  value="${notice.stop_time }"/></li>
            <li>发布区域:
            <c:forEach items="${province }" var="s" varStatus="index">
				<input type="checkbox" id="area_no${index.count }" name="area_no" value="${s.area_name }" <c:if test="${fn:contains(notice.provinces, s.area_name ) }">checked="checked"</c:if>/><label for="area_no${index.count }">${s.area_name }</label>
			 </c:forEach>
            </li>
           
        </ul>
        <p><input type="button" value="修改" class="btn" onclick="submitForm()"/><input type="button" value="返回" class="btn" onclick="history.back(-1)"/></p>
        </form>
	</div>

 
</body>
</html>
