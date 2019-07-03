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
        <li><a style="color:#32B1F0;" href="newQuPiao.jsp">如何取票</a></li>
        <li><a href="newMingCi.jsp">名词解释</a></li>
        <li><a href="newHuoQuLiRun.jsp">如何获取利润</a></li>
        <li><a href="newQueryOrder.jsp">如何查询订单状态</a></li>
     </ul>
  </div>
  <!--内容start--> 
 <div class="yew_r_con oz">
    <h2>火车票业务新手指南</h2>
    <div class="con_con">
      <h5>如何取票</h5>
      <ul>
        <li>
        <p>1.去车站自动售票机取票：在收到出票成功的短信通知或订单状态为“购票成功”后，请拿购票时所使用的有效证件，刷身份证换票。</p>
        </li>
        
        <li>
        <p >2.去车站售票窗口取票：收到出票成功短信通知或查看订单状态为“购票成功”后，请拿购票时所使用的证件，以及E开头的电子订单号，到车站售票窗口取票。</p>
        </li>
        
        <li>
        <p >3.去代售点或异地取票：同2所述，但需支付5元/张的手续费。</p>
        </li>
        
        <li>
        <p >4.乘车站或下车站都具备二代居民身份证检票条件的，可以使用二代居民身份证原件直接在火车站自动检票机办理进、出站检票手续，无需换纸质车票。</p>
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
