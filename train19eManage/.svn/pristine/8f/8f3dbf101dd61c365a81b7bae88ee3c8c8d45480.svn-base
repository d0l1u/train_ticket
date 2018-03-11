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
<script type="text/javascript">

	function submit(){
	
		if(confirm("是否提交？")){
			$("#updateForm").submit();
		}
	}
	
	
	
</script>
</head>

<body>

	<div class="book_manage account_manage oz">
	<form action="/system/addSystem.do" method="post" name="updateForm">
    	<ul class="ser oz">
        			<li>省<select name="province_id" id="province_id">
							<c:forEach items="${province }" var="p">
								<option value="${p.area_no }">${p.area_name}</option>
							</c:forEach>
            			</select>
            		</li>
           			 
        		</ul>
           <ul class="order_num oz">
        	<li>开通：
        		<select name="is_open">
        		   <c:forEach items="${isopen }" var="open">
        		   		<option value="${open.key }" >${open.value }</option>
        		   		
        		   </c:forEach>
        		</select>
        	（判断地区是否开通服务）</li>
            <li>付费：<select name="is_cost">
        			<c:forEach items="${iscost }" var="open">
        		   		<option value="${open.key }">${open.value }</option>
        		   		
        		   </c:forEach>
        		</select>（判断地区是否需要付费加盟）</li>
            <li>配送：<select name="is_ps">
        			<c:forEach items="${isps }" var="open">
        		   		<option value="${open.key }">${open.value }</option>
        		   		
        		   </c:forEach>
        		</select>（判断地区是否开通配送票的服务）</li>
            <li>
            <li>购买：<select name="is_buyable">
        			<c:forEach items="${isbuy }" var="open">
        		   		<option value="${open.key }">${open.value }</option>
        		   		
        		   </c:forEach>
        		</select>（判断地区是否可以购买）</li>
            <li>
            	<textarea style="border:1px solid #dadada; width: 300px; height: 200px;" name="rule_content"></textarea>
            </li>
        </ul>
        <p><input type="button" value="提 交" class="btn" onclick="submit()"/></p>
        </form>
	</div>

 
</body>
</html>
