<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
String color="";
if(request.getParameter("color")==null){
	color="0";
}else{
	color=request.getParameter("color");
}

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>火车票业务常见问题</title>
<link rel="stylesheet" href="/css/style.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
</head>

<body>
<div class="content oz">
<div class="index_all">
				
		<!--左边内容 start-->
			<jsp:include flush="true" page="/pages/common/menu.jsp">
				<jsp:param name="menuId" value="guide" />
			</jsp:include>
		<!--左边内容 end-->
    	<!--右边内容 start--> 
    	<div class="infoinput-right oz">
  			<div class="help_type oz">
            	<ul class="help_type_ul">
	                <li class="current1"><a href="/pages/guide/newKaiTong.jsp">新手指南</a></li>
	                <li class="current current1"><a href="/pages/guide/questionGouPiao.jsp">常见问题</a></li>
	                <li class="current1"><a href="/pages/guide/bxShuoMing.jsp">相关规章</a></li>
	                <li class="current1" style=" width:196px;"><a href="/pages/guide/tuiPiaoGuiDing.jsp">业务常识</a></li>
	           </ul>
  		</div>  
  		  <!--内容 start-->
  <div class="bread_nav mb10"> 
  	<ul class="bread_nav_ul">
        <li ><a href="questionGouPiao.jsp">购票问题</a></li>
        <li ><a href="questionTuiPiao.jsp">退票问题</a></li>
        <li ><a style="color:#32B1F0;" href="questionBX.jsp">保险问题</a></li>
        <li ><a href="questionYushou.jsp">火车票预售期</a></li>
     </ul>
  </div>   
  <!--内容 start-->
<div class="yew_r_con oz">
    <h2>火车票业务常见问题</h2>
    <div class="con_con">
      <h5>保险问题</h5>
      <ul>
        <li>
        <p >购票时是否购买保险由代理商跟用户协商决定，保险现有种类：10元保20万、20元保65万，乘客在订票过程中可自行选择，保险预定成功后会给用户下发短信。</p>
        </li>
        
        <li>
        <p >关于保险验证：投保成功后，用户将收到短信提醒，内含保险单号信息。请依据保险单号登录验证页面或拨打服务电话进行验证。</p>
        </li>
        
        <li>
        <p >保险验证页面：<a onclick="window.open('http://www.unionlife.com.cn/tab144/')" href="#" >http://www.unionlife.com.cn/tab144/</a></p>
        </li>
        
        <li>
        <p >服务电话：95515</p>
        </li>
        
        <li>
        <p >注意：一旦购票成功，申请退票时，保险是不退的。</p>
        </li>
      </ul>
      
    </div>
   </div>
   <!--内容 end--> 
 </div>
 </div>
</div>
</body>
</html>
