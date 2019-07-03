<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>我要加盟-购买产品页</title>
<link rel="stylesheet" href="/css/style.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
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
		  			<jsp:include flush="true" page="/notice/queryNoticeList.jhtml">
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
                	<p class="flow_pic2"></p>
                	<ul class="oz">
                      <li class="step1 current">填写注册资料</li>
                      <li class="step2 current">购买产品</li>
                      <li class="step3">正在审核</li>
                      <li class="step4">业务试用</li>
                      <li class="step5">审核通过</li>
                    </ul>
                </div>
                <c:choose>
                	<c:when test="${agentVo.estate eq '44'}">
	                	<div class="buy_tip">
	                		<p>您购买的产品已到期，为了保证您能正常使用火车票业务，请续费。</p>
	                	</div>
                	</c:when>
                	<c:otherwise>
	                	<div class="buy_tip">
	                		<p>请核对详细信息，我们会根据您提供的信息进行网点的审核。</p>
	                	</div>
                	</c:otherwise>
                </c:choose>
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
                    <div class="buy_tip buy_tip2">
                		<p>请点击“购买”对加盟产品进行购买,审核通过会以平台和电话进行通知；审核未通过我们会把您支付的加盟费退还到您的19e钱包中，并且在平台通知。</p>
                	</div>
                    <table class="user_kinds xuanze">
	                 	<c:forEach items="${productList}" var="productVo">
	                      <tr class="no_hover">
	                     	<td colspan="2" <c:if test="${productVo.name eq 'VIP用户'}">class="vip_user"</c:if>
	                     		<c:if test="${productVo.name eq '普通用户'}">class="vip_user no_vip"</c:if>>
	                     		<c:if test="${!empty pay_url}">
	                     	     	<p class="pay_state">
		                      			<strong>状态：</strong>待支付
			                    	</p>
		                    	</c:if>
		                    	<span>${productVo.name}<b>${fn:substringBefore(productVo.sale_price, '.')}</b><strong>${saleTypeMap[productVo.sale_type]}</strong></span>
	                    	</td>
	                      </tr>
	                      <tr>
	                        <td class="td_detail">
	                           	<ul class="oz">
									${productVo.describe}
	                            </ul>
	   						</td>
	   						<c:choose>
	   							<c:when test="${empty pay_url}">
		                    		<td>
		                    			<input type="button" class="btn buy_btn" value="购买" 
		                    				onclick="javascript:window.location='/joinUs/createJmOrder.jhtml?productId=${productVo.product_id}&buyType=${buyType}'" />
		                    		</td>
	                    		</c:when>
	                    		<c:otherwise>
	                    			<td></td>
	                    		</c:otherwise>
	                    	</c:choose>
	                       </tr>
	                   </c:forEach>
                   </table>
                </div>
	            
	            <!--   
                <table class="user_kinds xuanze">
	                <c:forEach items="${productList}" var="product">
	                	<tr>
	                    	<td>${product.name}</td>
	                    	<td><span>${product.sale_price}</span>&nbsp;${saleTypeMap[product.sale_type]}</td>
	                    	<td class="td_detail">
	                    		${product.describe}
							</td>
							<c:if test="${empty pay_url}">
	                    	<td><input type="button" class="btn buy_btn" value="购买" 
	                    			onclick="javascript:window.location='/joinUs/createJmOrder.jhtml?productId=${product.product_id}'" /></td>
	                    	</c:if>
	                    </tr>
	                </c:forEach>
                </table>
                -->  
                
                <c:if test="${!empty pay_url}">
                <div class="pay_content">
					<iframe src="${pay_url}" frameborder="0" scrolling="no" width="550px;" height="200px" style="background: #F0F7FF;"></iframe>
	  				<br />*&nbsp;&nbsp;请确认以上信息，支付后将无法修改
				</div>
				</c:if>
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
