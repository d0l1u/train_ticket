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
	                <li class="current1"><a href="/pages/guide/questionGouPiao.jsp">常见问题</a></li>
	                <li class="current current1"><a href="/pages/guide/bxShuoMing.jsp">相关规章</a></li>
	                <li class="current1" style=" width:196px;"><a href="/pages/guide/tuiPiaoGuiDing.jsp">业务常识</a></li>
	           </ul>
  		</div>  
  		  <!--内容 start-->
  <div class="bread_nav mb10"> 
  	<ul class="bread_nav_ul">
        <li ><a href="bxShuoMing.jsp">保险说明</a></li>
        <li ><a href="daiGou.jsp">火车票线下代购协议</a></li>
        <li ><a style="color:#32B1F0;" href="zhuCe.jsp">委托注册多个代购账户协议</a></li>
         <li ><a href="chengnuoPage.jsp"> 平台购票承诺书</a></li>
     </ul>
  </div>   
  <!--内容 start-->
<div class="yew_r_con oz">
    <h2>火车票业务相关章程</h2>
    <div class="con_con">
       <h5>委托注册多个代购账户协议</h5>
          <ul>
     		<li>
     		<p>
     			为了更好的利用北京一九易站电子商务有限公司技术平台开展火车票代购业务，我方特向贵方申请开立多个代购账户，相关账户的开立资料（包括身份证信息等）由我方在正式申请注册时一并提供。
     		</p>
     		<p>
     			作为北京一九易站电子商务有限公司的代理商，我方完全知晓北京一九易站电子商务有限公司在帮助我方开展火车票代购业务过程中，仅提供技术服务。我方保证申请开立代购账户所提供的注册资料（包括身份证信息等）的完整性和真实性，并且保证不会因此产生任何法律纠纷。我方承诺，若因此出现任何纠纷或责任的，与北京一九易站电子商务有限公司无关，均由我方完全承担。
     		</p>
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
