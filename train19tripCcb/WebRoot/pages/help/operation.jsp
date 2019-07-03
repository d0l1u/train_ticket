<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html><!-- InstanceBegin template="/Templates/业务介绍.dwt" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- InstanceBeginEditable name="doctitle" -->
<title>操作演示</title>
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
        <div class="tip_term oz" style="margin:10px auto 0;">
        	<p class="price_tip">
        	<span>客服电话：400-688-2666&nbsp;&nbsp;转2号键</span>
        		<span style="padding-left:400px;">业务提供方：19旅行</span>
        	</p>
        </div>
    	<!--内容 start-->
        <div class="bread_nav mb10">
        	您所在的位置：<a href="#">业务介绍</a> > <span>操作演示</span>
        	
        </div>
        <div class="yew_l_con oz">
        	<div class="left_nav">
            	<h2>业务介绍</h2>
            	<ul>
                    <li><a href="protocol.jsp">代购服务协议</a></li>
                	<li><a href="trueFalse.jsp">车票真伪识别</a></li>
                    <li><a href="refund.jsp">退票规定</a></li>
                    <li class="current"><a href="operation.jsp">操作演示</a></li>
                    <li><a href="oftenQuestion.jsp">常见问题</a></li>
                    <!--   <li class="current"><a href="realName.jsp">实名制购票流程</a></li>
                    <li><a href="bxProtocol.jsp">购票保险协议</a></li>-->
                </ul>
            </div>
        </div>
        <div class="yew_r_con oz"><!-- InstanceBeginEditable name="EditRegion3" -->
          <h2>操作演示</h2>
          <div class="con_con">
          	<h5>操作演示</h5>
            <ul>
        <li>
          <h3>1.火车票---订票步骤</h3>
        </li>
        <li>
          <p>①进入订票页面后，请输入<strong>出发城市</strong>、<strong>到达城市</strong>以及<strong>出发日期</strong>,根据查询到的余票信息，选择相应车次点击“订购”按钮:</p>
          <p><img src="/images/book1.jpg" width="510"  alt="订票1" /></p>
          <p><img src="/images/book2.jpg" width="510" alt="订票二" /></p>
          <p>②进入订购页面后,录入详细的订购信息，点击“提交”按钮:</p>
          <p><img src="/images/book3.jpg" width="510" alt="订票--录入信息" /></p>
          <p><img src="/images/book4.jpg" width="510" alt="订票--录入信息" /></p>
          <p>③到支付页面进行支付，完成订票。</p>
          <p><img src="/images/pay1.jpg" width="510" alt="订票-支付" /></p>
          <p><img src="/images/pay2.jpg" width="510" alt="订票-支付" /></p>
          <p><img src="/images/pay3.jpg" width="510" alt="订票-支付" /></p>
          
        </li>
        <li>
          <h3>2.火车票---购买保险</h3>
          <p>购票时是否购买保险由用户自行决定，绝无强制行为.保险现有种类：10元保20万、20元保65万，乘客在订票过程中可自行选择是否购买，保险预定成功后会给用户下发短信。</p>
          <p><img src="/images/insure1.jpg" width="510" alt="购买保险选择" /></p>
          <p>若需索取保险发票,请在订票过程中勾选”保险发票”选项。</p>
          <p><img src="/images/insure2.jpg" width="510" alt="勾选保险发票" /></p>
          <p>关于保险验证：<a href="http://www.unionlife.com.cn/tab147/">http://www.unionlife.com.cn/tab147/</a></p>
          <p>服务电话：95515</p>
        </li>
        <li>
          <h3>3.火车票---查询订单</h3>
          <p>登陆平台进入火车票业务，点击“订单/退款”下“查询”按钮，即可看到您所订购的火车票的具体信息。也可通过订单号直接进行查询。</p>
          <p><img src="/images/ser-pic1.jpg" width="510" alt="查询订单" /></p>
        </li>
        <li>
          <h3>4.火车票---退票申请</h3>
        </li>
        <li>
          <p>登陆平台进入火车票业务，点击“订单/退款”下“查询”按钮，即可看到您已订购的所有火车票的具体信息。或者通过您已知的订单号进行精确查询。请选定需要操作退票的订单，在“操作”中点击“退款”。</p>
          <p>①查询订单</p>
          <p><img src="/images/ser-pic1.jpg" width="510" alt="退票--查询" /></p>
          <p>②退票操作</p>
          <p><img src="/images/refund2.jpg" width="510" alt="退票--退票" /></p>
          <p>打开退票页面后，找到退款小票上传处，点击“浏览”将退款小票上传，最后点击“提交”即可。</p>
          <p><img src="/images/refund3.jpg" width="510" alt="退票--退票>提交" /></p>
        </li>
        <li>
          <h3>5.火车票---投诉建议</h3>
          <p>若您在业务使用过程中,有不满意的地方或者宝贵的建议,请点击导航栏中的“投诉/建议”,留下您的想法</p>
          <p><img src="/images/complaint1.jpg" width="510" alt="投诉" /></p>
        </li>
        <li>
          <h3>6.火车票---帮助</h3>
          <p>若您在业务使用过程中,有不熟悉的地方,请点击导航栏中的“帮助”</p>
          <p><img src="/images/help1.jpg" width="510" alt="帮助" /></p>
        </li>
      </ul>
          </div>
        <!-- InstanceEndEditable --></div>
        <!--内容 end-->
        
    </div>
    <!--业务所有标注 start-->
    <div class="business-provider">
    	
    </div>
    <!--业务所有标注 end-->
	
</body>
<!-- InstanceEnd --></html>
