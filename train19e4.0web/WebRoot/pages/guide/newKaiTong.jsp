<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>火车票业务常见问题--如何开通</title>
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
	                <li class="current current1"><a href="/pages/guide/newKaiTong.jsp">新手指南</a></li>
	                <li class="current1"><a href="/pages/guide/questionGouPiao.jsp">常见问题</a></li>
	                <li class="current1"><a href="/pages/guide/bxShuoMing.jsp">相关规章</a></li>
	                <li class="current1" style=" width:196px;"><a href="/pages/guide/tuiPiaoGuiDing.jsp">业务常识</a></li>
	           </ul>
  		</div>     
  <!--内容 start-->
  <div class="bread_nav mb10"> 
  	<ul class="bread_nav_ul">
  		<li><a style="color:#32B1F0;" href="newKaiTong.jsp">如何开通</a></li>
        <li><a href="newGouPiao.jsp">如何购票</a></li>
        <li><a href="newTuiPiao.jsp">如何退票</a></li>
        <li><a href="newQuPiao.jsp">如何取票</a></li>
        <li><a href="newMingCi.jsp">名词解释</a></li>
        <li><a href="newHuoQuLiRun.jsp">如何获取利润</a></li>
        <li><a href="newQueryOrder.jsp">如何查询订单状态</a></li>
     </ul>
  </div>
  
  
  <div class="yew_r_con oz">
    <h2>火车票业务新手指南</h2>
    <div class="con_con">
      <h5>如何开通</h5>
      <ul>
        <li>
        <p>1.登陆平台，点击“旅游/火车票”选项下的火车票</p>
        </li>
        <li><span style="margin-left:50px;"></span><img src="/images/login.jpg" width="201" height="326"  alt="进入火车票" /> </li>
        
        <li>
        <p class="new_page">2.填写基本资料，点击下一步</p>
        </li>
        <li><img src="/images/newKaiTong1.jpg" width="732" height="589"  alt="填写基本资料" /> </li>
        
        <li>
       <p class="new_page">3.进入实名认证页面，按要求填写完整并点击【提交审核】</p>
        </li>
        <li><img src="/images/newKaiTong2.jpg" width="750" height="401"  alt="实名认证" /> </li>
        
        
        <li>
        <p style="margin:20px 0 40px 0">4.系统将自动初审，通过后您将得到10天业务试用期，试用期内通过驻地审核后，才可正式免费使用火车票业务！</p>
        </li>
      </ul>
      
    </div>
 </div>
 </div>
 </div>
  <!--内容 end--> 
</div>
</body>
</html>
