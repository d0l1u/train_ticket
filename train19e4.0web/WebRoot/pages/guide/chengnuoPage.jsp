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
        <li ><a href="zhuCe.jsp">委托注册多个代购账户协议</a></li>
         <li ><a  style="color:#32B1F0;" href="chengnuoPage.jsp"> 平台购票承诺书</a></li>
     </ul>
  </div>   
  <!--内容 start-->
<div class="yew_r_con oz">
    <h2>火车票业务相关章程</h2>
    <div class="con_con">
      <h5>平台购票承诺书</h5>
            <ul>
              <li>
                <h3>由于国家对火车票代购事宜的相关规定，我自愿承诺并保证如下：</h3>
              </li>
              <li><p>1.严格遵守国家的有关法律、法规和行政规章制度。</p></li>
              <li><p>2.严格遵守北京一九易站电子商务有限公司（以下简称：一九易）的《火车票线下代购服务协议》等相关协议规定。</p></li>
              <li><p>3.在为用户通过一九易平台代购火车票过程中，不收取任何手续费。如有通过任何形式收取手续费的现象，均属我个人行为，与一九易无关。</p></li>
              <li><p>4.若因我个人行为，造成一九易受到相关部门查处或造成一九易任何损失，我愿意承担全部责任。</p></li>
              <li><p>5.一九易若发现我有上述行为，有权立即停用我的火车票代售服务，有权扣除我全部酬金。若上述违约金不足以弥补一九易损失的，一九易有权要求我进一步赔偿所有损失。</p></li>
              <li><p>6.我将严格遵守本承诺，如有违反，一九易有权立即解除与我的合作，我愿意接受任何处罚，同时，一九易有权追究我任何法律责任。</p></li>
      		  <li><p>一旦我通过网络页面点击确认或以其他方式选择签署本承诺书，本承诺书则立即生效，不得以任何理由撤回或撤销。</p></li>
         </ul> 
    </div>
   </div>
   <!--内容 end--> 
 </div>
 </div>
</div>
</body>
</html>
