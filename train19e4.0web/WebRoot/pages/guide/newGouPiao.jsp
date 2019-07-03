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
        <li><a style="color:#32B1F0;" href="newGouPiao.jsp">如何购票</a></li>
        <li><a href="newTuiPiao.jsp">如何退票</a></li>
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
      <h5>如何购票</h5>
      <ul>
        <li>
        <p>1.业务开通后，进入首页，选择城市和乘车日期，点击搜索。</p>
        </li>
        <li><img src="/images/newGouPiao1.jpg" width="643" height="381"  alt="购票1" /> </li>
        
        <li>
        <p class="new_page">2.进入选择车次页面，根据所想乘坐的坐席，点击【预定】。</p>
        </li>
        <li><img src="/images/newGouPiao2.jpg" width="730" height="262"  alt="购票2" /> </li>
        
        <li>
        <p class="new_page">3.进入填写订单页面，录入详细的订购信息，点击【确认按钮】。</p>
        </li>
        <li><img src="/images/newGouPiao3.jpg" width="730" height="587"  alt="购票3" /> </li>
        
        
        <li>
        <p class="new_page">4.进入支付页面，输入支付密码，点击【支付】。</p>
        </li>
        <li><img src="/images/newGouPiao4.jpg" width="730" height="287"  alt="购票4" /> </li>
        
        <li>
        <p class="new_page">5.支付完成后，跳转到该订单详细页面，状态为【支付成功】。
        </p>
        </li>
        
        <li>
        <p style="margin:20px 0 40px 0">6.进入我的订单页面，如状态变更为【出票成功】时，则表示该订单已经购票成功。
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
