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
  		<li><a href="newKaiTong.jsp">如何开通</a></li>
        <li><a href="newGouPiao.jsp">如何购票</a></li>
        <li><a style="color:#32B1F0;" href="newTuiPiao.jsp">如何退票</a></li>
        <li><a href="newQuPiao.jsp">如何取票</a></li>
        <li><a href="newMingCi.jsp">名词解释</a></li>
        <li><a href="newHuoQuLiRun.jsp">如何获取利润</a></li>
        <li><a href="newQueryOrder.jsp">如何查询订单状态</a></li>
     </ul>
  </div>
  <!--内容start--> 
  <div class="yew_r_con oz">
    <h2>火车票业务新手指南</h2>
    <div class="con_con">
      <h5>如何退票</h5>
      <ul>
        <li>
        <p>1.点击【我的订单】，可选择订票日期、订单号或取票单号任一种方式查询，输入相应信息后，点击【查询】，即可看到该代理商需要查找的车票信息。然后在【操作】一栏中点击【申请退款】按钮。</p>
        </li>
        <li><img src="/images/newTuiPiao1.jpg" width="730" height="243"  alt="退票1" /> </li>
        
        <li>
        <p class="new_page">2.进入退款页面，请选择需要退票的车票，并点击【退款】按钮，即申请成功。</p>
        </li>
        <li><img src="/images/newTuiPiao2.jpg" width="730" height="445"  alt="退票2" /> </li>
        
      </ul>
      
    </div>
   <!--内容 end--> 
 </div>
 </div>
</div>
</div>
</body>
</html>
