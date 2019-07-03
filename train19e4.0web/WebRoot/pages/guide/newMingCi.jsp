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
        <li><a style="color:#32B1F0;" href="newMingCi.jsp">名词解释</a></li>
        <li><a href="newHuoQuLiRun.jsp">如何获取利润</a></li>
        <li><a href="newQueryOrder.jsp">如何查询订单状态</a></li>
     </ul>
  </div>   
  <!--内容 start-->

  <div class="yew_r_con oz">
    <h2>火车票业务新手指南</h2>
    <div class="con_con">
      <h5>名词解释</h5>
      <ul>
        <li>
        <p>【正在审核】：此状态表明您的业务开通申请已提交成功，请耐心等待。</p>
        </li>
        
        <li>
        <p >【业务试用】：此状态表明您的业务开通申请已受理，进入10天的业务使用期，可正常订票。</p>
        </li>
        
        <li>
        <p >【审核通过】：此状态表明业务开通申请已通过审核，可正式免费使用火车票业务。</p>
        </li>
        
        <li>
        <p >【支付成功】：此状态表明所订车票已完成付款，即票价已从您的19e账户内扣除。</p>
        </li>
        
        <li>
        <p >【出票中】： 此状态表明机器人正在处理您所订购的订单，请稍候。</p>
        </li>
        
        <li>
        <p >【预订成功】：此状态表明您所订购的车票已成功预订，等待机器人付款。此时乘客会收到短信提示车票预订成功。</p>
        </li>
        
        <li>
        <p >【出票成功】：此状态表明机器人已完成付款，乘客会收到短信提示出票成功。此时标志着车票已成功订购，用户可去窗口取票。</p>
        </li>
        
        <li>
        <p >【出票失败】：此状态表明车票订购失败，我们会在24小时内将退款返回至您的19e账户中，请注意查看！</p>
        </li>
        
        <li>
        <p >【退款申请中】：此状态表明您的退款申请已提交，等待退款专员处理。</p>
        </li>
        
         <li>
        <p >【正在退款】：此状态表明您的退款申请已处理，等待退款（退款会在24小时之内返还至您的19e账户）</p>
        </li>
        
         <li>
        <p >【退款完成】：此状态表明退款已经返还至您的19e账户中，请查看资金变动。</p>
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
