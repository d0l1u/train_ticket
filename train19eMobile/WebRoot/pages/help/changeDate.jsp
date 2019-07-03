<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>火车票改签页面</title>
<!-- InstanceEndEditable -->
<link rel="stylesheet" href="/css/style.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<!-- InstanceBeginEditable name="head" -->
<!-- InstanceEndEditable -->
</head>

<body>
	<div class="content oz">
    	<!--导航条 start-->
    	<div class="main_nav">
        	<ul class="oz">
        	    <jsp:include flush="true" page="/pages/common/menu.jsp">
					<jsp:param name="menuId" value="help" />
				</jsp:include>
            </ul>
            <div class="slogan"></div>
        </div>
        <!--导航条 end-->
    	<!--内容 start-->
        <div class="bread_nav mb10">
        	您所在的位置：<a href="#">业务介绍</a> > <span>购票常识</span>
        	
        </div>
        <div class="yew_l_con oz">
        	<div class="left_nav">
            	<h2>业务介绍</h2>
            	<ul>
                    <li><a href="protocol.jsp">火车票线下代购服务协议</a></li>
                    <li><a href="realNameEffective.jsp">委托注册多个代购账户协议</a></li>
                	<li><a href="trueFalse.jsp">新版火车票真伪识别</a></li>
                    <li><a href="realName.jsp">实名制购票流程</a></li>
                	<li class="current"><a href="changeDate.jsp">改签规定</a></li>
                    <li><a href="refund.jsp">退票规定</a></li>
                    <li><a href="sendArea.jsp">配送范围</a></li>
                    <li><a href="bookPeriod.jsp">车票预定期</a></li>
                    <!--  <li><a href="bxProtocol.jsp">购票保险协议</a></li>-->
                </ul>
            </div>
        </div>
        <div class="yew_r_con oz"><!-- InstanceBeginEditable name="EditRegion3" -->
          <h2>火车票改签</h2>
          <div class="con_con">
            <ul>
              <li><h3>火车票改签</h3></li>
             <li>旅客买好车票后,如不能按票面指定的日期、车次乘车时,可以在列车开车前到发站签证窗口（或指定窗口,代售点不办理）办理提前或改晚乘车签证手续。</li>
             <li>办理改晚乘车签证手续时，一般需要在开车前办理。如遇特殊情况，铁路部门同意后可在开车后2小时内办理。团体旅客必须在开车48小时前办理。</li>
             <li>改晚乘车签证手续，事实上与客票有效期无关。</li>
             <li><h3>改签的条件为：</h3></li>
             <li>1）您的票还没有过期，即该车次还没有开车；<br />2）您所需改签到的日期的车次的车票必须已经发售，且必须还有剩余票额。</li>
             <li><p>例如，你买了1月10日的火车票，因故需要推迟，则如果你1月5日办理推迟改签的话，则可以改签为1月11日~1月14日的车票（因火车票的预售期为10天，1月5日只发售到1月14日的车票）；类似的，如果你1月6日办理推迟改签的话，则可以改签为1月11日~1月15日的车票，以此类推，但又如15日的车票已经卖完，则你不可以改签这天。假设1月11日~1月19日的票已经全部卖完，则你这张票不可以办理改签。</p></li>
             <li>另外，如果你是硬卧的车票要改签，但硬卧已无票，硬座有票，则也可以改签硬座（由你选择），不收取手续费。</li>
             <li>旅客在发站办理改签时，改签后的车次票价高于原票价时，补收票价差额；改签后的车次票价低于原票价时，退还票价差额。</li>
             <li>旅客在发站办理车票改签时，车站会收回原票换发新票，票面打印“始发改签”字样。</li>
             <li>改签只可以办理一次。往返票、联程票不办理改签。车票需要提前改签。</li>
             <li><p>比如：1月10号的车票，您要1月5号走，在预售期范围内是可以办理改签的。（动车20天 其他10天）<br />当然1月5号要有有运载能力的火车，如果是动车的话，按照规定，动车是不允许买站票的。也就是说动车1月5号没有座的话，就不能改签了。</li>
             <li>改签可以是不同的车次，但目的地要相同。<br />改签不收手续费，票价多退少补。</p></li>
            </ul>
          </div>
        <!-- InstanceEndEditable --></div>
        <!--内容 end-->
    </div>
	
</body>
<!-- InstanceEnd --></html>
