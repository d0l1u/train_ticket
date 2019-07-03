<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>19trip旅行</title>
<link rel="stylesheet" href="/css/reset-min.css" type="text/css" />
<link rel="stylesheet" href="/css/style.css" type="text/css"/>
<link rel="stylesheet" href="/css/sreachbar.css" type="text/css"/>
<link rel="stylesheet" href="/css/travel.css" type="text/css"/>
<script type="text/javascript" src="/js/artDialog.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">
//编辑用户信息
function toUpdateUserinfo(user_id){
	window.location='/login/toUpdateUserinfo.jhtml?user_id='+user_id;
}
</script>
</head>

<body>
<!--以下是头部logo部分start -->
<jsp:include flush="true" page="/pages/common/headerNav.jsp">
	<jsp:param name="menuId" value="lx" />
</jsp:include>
<!--以下是头部logo部分end -->


<!--以下是我的旅行正文内容travel_con部分start -->
<div class="travel_con">
	<!--左边内容 start-->
			<jsp:include flush="true" page="/pages/common/menuLeft.jsp">
				<jsp:param name="menuId" value="myInfo" />
			</jsp:include>
	<!--左边内容 end-->
    
    
    <!--右边内容 start-->
	<div class="right_con">
        <ul class="MyOrder">
    		<li>我的信息</li>
    	</ul>
        <table class="MyInmatable">
        <tr>
	        <td>手机：</td>
	        <td>${loginMap.user_phone }</td>
        </tr>
        <tr>
	        <td>邮箱：</td>
	        <td> 
		        <c:choose>
		        	<c:when test="${loginMap.user_email eq '' || loginMap.user_email==null}">
		        		未设置
		        	</c:when>
		        	<c:otherwise>
		        		${loginMap.user_email }
		        	</c:otherwise>
		        </c:choose>
	        </td>
        </tr>
        <tr>
	        <td>昵称：</td>
	        <td>
	        	<c:choose>
		        	<c:when test="${loginMap.user_name eq '' || loginMap.user_name==null }">
		        		未设置
		        	</c:when>
		        	<c:otherwise>
		        		${loginMap.user_name }
		        	</c:otherwise>
		        </c:choose>
	        </td>
        </tr>
        <tr>
	        <td>姓别：</td>
	        <td>
	        	<c:choose>
		        	<c:when test="${loginMap.user_sex eq '' || loginMap.user_sex==null }">
		        		未设置
		        	</c:when>
		        	<c:otherwise>
		        		<c:if test="${loginMap.user_sex eq '1' }">女</c:if>
		        		<c:if test="${loginMap.user_sex eq '0' }">男</c:if>
		        	</c:otherwise>
		        </c:choose>
	        </td>
        </tr>
        <tr>
	        <td>生日：</td>
	        <td>
	        	<c:choose>
		        	<c:when test="${loginMap.user_birth eq '' || loginMap.user_birth==null }">
		        		未设置
		        	</c:when>
		        	<c:otherwise>
		        		${loginMap.user_birth }
		        	</c:otherwise>
		        </c:choose>
	        </td>
        </tr>
		</table>
        <button class="btn13 MyInma_button" type="button" onclick="toUpdateUserinfo('${loginMap.user_id }');">编&nbsp;&nbsp;辑</button>
        <br/><br/><br/>
	</div>
  	<!--右边内容 end-->
</div>
<!--以下是我的旅行正文内容travel_con部分end -->

<!-- footer start -->
<%@ include file="/pages/common/footer.jsp"%>
<!-- footer end -->
</body>
</html>
