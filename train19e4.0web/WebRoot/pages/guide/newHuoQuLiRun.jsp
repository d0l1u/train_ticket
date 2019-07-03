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
        <li><a href="newTuiPiao.jsp">如何退票</a></li>
        <li><a href="newQuPiao.jsp">如何取票</a></li>
        <li><a href="newMingCi.jsp">名词解释</a></li>
        <li><a style="color:#32B1F0;" href="newHuoQuLiRun.jsp">如何获取利润</a></li>
        <li><a href="newQueryOrder.jsp">如何查询订单状态</a></li>
     </ul>
  </div>   
  <!--内容 start-->

<div class="yew_r_con oz">
    <h2>火车票业务新手指南</h2>
    <div class="con_con">
      <h5>如何获取利润</h5>
      <ul>
        <li>
        <p><strong>1.通过售卖保险获取</strong><br/>
				     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;本平台可售卖两种保险：10元保20万，20元保65万。<br/>
			         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;如乘客选择10元保险，每张车票您将获得2元利润<br/>
			         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;如乘客选择20元保险，每张车票您将获得5元利润<br/>
			         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 以上均按坐扣形式返还。
		</p>
        </li>
        
        <li>
        <p ><strong>2.通过活动奖励获取</strong><br/>
				   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;平台不定期会以活动形式对用户进行奖励，请随时关注平台火车票业务的活动信息。<br/>
				   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;活动奖励均后返至代理商账户中。
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
