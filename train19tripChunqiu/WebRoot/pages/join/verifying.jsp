<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>我要加盟-正在审核页</title>
<link rel="stylesheet" href="/css/style.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<!-- 自适应高度 -->
<script type="text/javascript" src="/js/trendsHeight.js"></script>
<!-- 自适应高度 -->
</head>

<body>
	<div class="content oz">
		<!--导航条 start-->
	    <div class="main_nav">
        	<ul class="oz">
            	<jsp:include flush="true" page="/pages/common/menu.jsp">
					<jsp:param name="menuId" value="join" />
				</jsp:include>
            </ul>
            <div class="slogan"></div>
        </div>
        <!--导航条 end-->
    	<!--左边内容 start-->
    	<div class="left_con oz">
            <!--提醒通告模块 start-->
            <div class="notice_weak_box oz">
            	<div class="weak_box">
                	<h3>开通要求</h3>
                    <ul>
                    	<li>同意<a href="javascript:void(0);" onclick="window.open('/pages/common/protocolPop.jsp')">《火车票线下代购服务协议》</a>中的条款</li>
                    	<li>100米之内不允许有车票售卖点</li>
                        <li>愿意接受19e分公司的业务监督和指导</li>
                        <li>承诺不收取除购买款外任何多余的手续费</li>
                    </ul>
                </div>
                <div class="notice oz">
		  			<jsp:include flush="true" page="/chunqiu/notice/queryNoticeList.jhtml">
		  				<jsp:param name="" value=""/>
					</jsp:include>
                </div>
            </div>
            <!--提醒通告模块 end-->
         	<!--我要加盟-tab start-->
            <div class="join_us oz">
            	<ul class="join_us_ul">
                	<li class="current"><a href="#">申请开通</a></li>
                    <li><a href="/pages/help/protocol.jsp">业务介绍</a></li>
                </ul>
            	<div class="flow_step">
                	<p class="flow_pic3"></p>
                	<ul class="oz">
                      <li class="step1 current">填写注册资料</li>
                      <li class="step2 current">购买产品</li>
                      <li class="step3 current">正在审核</li>
                      <li class="step4">业务试用</li>
                      <li class="step5">审核通过</li>
                    </ul>
                </div>
                <!-- 
                <p class="state"><strong></strong>您的账号<span>正在审核中</span><br />如果有问题请咨询QQ：930812452，电话：010-57386868-6366谢谢！</p>
                -->
                <div class="verifying_tip">
                	<p>我们正在努力进行您的网点审核，确认提供的地址是否可以开通，如果过程中有任何疑问请咨询当地分公司。</p>
                </div>
				<div class="backg oz">
					<h3 class="contact"><span>联系方式</span></h3>
                    <table class="user_mes">
                        <tr>
                            <th width="90">店铺名称：</th>
                            <td width="200" colspan="2">${agentVo.shop_name}</td>
                            <th style="text-align:left;" colspan="2">店铺简称：<span style="font-weight:normal;">${agentVo.shop_short_name}</span></th>
                        </tr>
                        <tr>
                            <th width="90">店铺类别：</th>
                            <td width="200" colspan="3">${shopTypeMap[agentVo.shop_type]}</td>
                        </tr>
                        <tr>
                            <th width="90">您的姓名：</th>
                            <td width="200" colspan="3">${agentVo.user_name}</td>
                        </tr>
                        <tr>
                            <th>联系电话：</th>
                            <td>${agentVo.user_phone}</td>
                            <th style="text-align:left;"  colspan="2">QQ：<span style="font-weight:normal;">${agentVo.user_qq}</span></th>
                        </tr>
                        <tr>
                            <th>所属区域：</th>
                            <td colspan="3">
                                <span>${agentVo.city_name}</span>&nbsp;<span>${agentVo.district_name}</span>
                            </td>
                        </tr>
                        <tr class="dizhi">
                            <th>详细地址：</th>
                            <td colspan="3">${agentVo.user_address}</td>
                        </tr>
                    </table>
	               
	                <c:if test="${productVo != null}">
		                 <table class="user_kinds">
	                       <tr>
		                      <td colspan="2" <c:if test="${productVo.name eq 'VIP用户'}">class="vip_user"</c:if>
	                     		<c:if test="${productVo.name eq '普通用户'}">class="vip_user no_vip"</c:if>>
	                      		<p class="pay_state">
	                      			<strong>状态：</strong>
									<c:choose>
		                    			<c:when test="${pay_status eq 'SUCCESS'}">支付成功</c:when>
		                    			<c:when test="${pay_status eq 'FAIL'}">支付失败</c:when>
		                    			<c:otherwise>支付超时</c:otherwise>
		                    		</c:choose>
		                    	</p><span>${productVo.name}<b>${fn:substringBefore(productVo.sale_price, '.')}</b><strong>${saleTypeMap[productVo.sale_type]}</strong></span>
		                      </td>
	                       </tr>
	                       <tr>
	                            <td class="td_detail">
	                            	<ul class="oz">
										${productVo.describe}
	                                </ul>
	    						</td>
	                        </tr>
	                    </table>
	                </c:if>
	                <div class="vertified_btn oz">
	                	<p><input type="button" value="投诉建议" class="btn btn2" onclick="javascript:window.location='/chunqiu/complain/complainIndex.jhtml?ques_Id=1'" /></p>
	                </div>
	             </div>
            </div>
            <!--我要加盟-tab end-->
        	
        </div>
        <!--左边内容 end-->
        <!--右边内容 start-->
        <%@ include file="/pages/common/right.jsp"%>
    	<!--右边内容 end-->
    
    </div>
</body>
</html>
