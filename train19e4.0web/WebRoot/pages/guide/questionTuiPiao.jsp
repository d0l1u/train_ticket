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
        <li ><a style="color:#32B1F0;" href="questionTuiPiao.jsp">退票问题</a></li>
        <li ><a href="questionBX.jsp">保险问题</a></li>
        <li ><a href="questionYushou.jsp">火车票预售期</a></li>
     </ul>
  </div>   
  <!--内容 start-->
<div class="yew_r_con oz">
    <h2>火车票业务常见问题</h2>
    <div class="con_con">
      <h5>退票问题</h5>
      <ul>
        <li>
        <p >1.根据梯次退票方案，2013年9月1日起，票面乘车站开车前48小时以上的，退票时收取票价5%的退票费；开车前24小时以上、不足48小时的，退票时收取票价10%的退票费；开车前不足24小时的，退票时收取票价20%退票费。（注：此9月1日为火车票面发车时间）</p>
        </li>
        
        <li>
        <p >2.19e火车票业务受理退款时间为：早7:00-晚23:00。</p>
        </li>
        
        <li>
          <h3>退票处理方法 </h3>
        </li>
        
        <li>
        <p >1.如果由于铁路系统原因（如无票、身份信息待核验等）车票出票失败，自动进行退款请求。<br />
        	&nbsp;&nbsp;&nbsp;&nbsp;处理方法：请耐心等待，我们会在24小时内进行退款，不扣除任何费用，自动退回到代理商资金账户中。
        </p>
        </li>
        
        <li>
        <p >2.如果未换取纸质车票，在发车前<font color="red">大于4小时</font>，发起退款请求<br />
        	&nbsp;&nbsp;&nbsp;&nbsp;处理方法：乘客可以向代理商出示有效身份证件，由代理商帮助乘客提交退款请求，<font color="red">退款审核通过后</font>我们将于<font color="red">24小时之内</font>，扣除相应手续费退还至代理商帐户内。
        </p>
        </li>
        
        <li>
        <p >3.如果未换取纸质车票，在发车前<font color="red">小于4小时</font>，发起退款请求<br />
        	&nbsp;&nbsp;&nbsp;&nbsp;处理方法：乘客<font color="red">需去火车站窗口办理退票</font>后，将退票凭证交给代理商确定乘客已经去车站办理退票，由代理商帮助乘客提交退款请求，我们收到铁路部门退还的相应票款后，将于24小时之内将扣除20%手续费的退款金额退还到代理商账户内。
        </p>
        </li>
        
        <li>
        <p >4.如果未换取纸质车票，在发车前<font color="red">小于4小时</font>，发起退款请求<br />
        	&nbsp;&nbsp;&nbsp;&nbsp;处理方法：乘客处理方法：需要乘客凭车票和有效身份证件去车站办理退票后，将退票凭证交给代理商确定乘客已经去车站办理退票，由代理商帮助乘客提交退款请求，收到铁路部门退换的相应票款后，将于24小时之内将扣除相应手续费的退款金额退还到代理商账户内。
        </p>
        </li>
        
        <li>
          <h3>补充说明 </h3>
        </li>
        
        <li>
        <p >1.发车后12小时后，系统将无法提交退款申请，请联系火车票客服进行办理。<br/>
        	&nbsp;&nbsp;&nbsp;&nbsp;2.关于改签：乘客可到任意火车站窗口办理改签。
        </p>
        </li>
        
        <li>
          <h3>退款咨询热线：400-698-6666转5</h3>
        </li>
        <br/>
        
      </ul>
      
    </div>
   </div>
   <!--内容 end--> 
 </div>
 </div>
</div>
</body>
</html>
