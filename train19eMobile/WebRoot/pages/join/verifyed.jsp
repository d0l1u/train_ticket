<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>我要加盟-审核通过页</title>
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
            <div class="notice_weak_box oz">
            	<div class="weak_box" style="width:98%;padding:10px 10px 10px 0;">
            		<h3 style="text-align:center;color:#f00;">重要通知：关于身份证代理商实名</h3>
                    <ul>
                        <li style="color:#f00;padding-left:0;text-indent:2em;line-height:22px;background:none;">根据<a onclick="window.open('http://www.12306.cn/mormhweb/zxdt/201402/t20140223_1435.html')" href="#">《铁路互联网购票身份核验须知》</a>的规定，自3月1日起铁路系统已经开始对注册用户进行实名认证，为了保证系统正规和合法的运营，方便周边用户的出行，所以需要代理商提供未在12306绑定的实名身份证信息对进行绑定，我们将在24小时之内通知代理商是否绑定成功，系统将于 4月6日对未绑定的代理商暂停提供服务，请各位代理商抓紧绑定身份证信息以免暂停业务！</li>
                    </ul>
                </div>
            </div>
            <!--提醒通告模块 end-->
         	<!--我要加盟-tab start-->
            <div class="join_us oz">
            	<ul class="join_us_ul">
                	<li class="current"><a href="/joinUs/joinIndex.jhtml">申请开通</a></li>
                	<li><a href="/joinUs/realNameAuth.jhtml">实名认证</a></li>
                </ul>
              	  <div class="flow_step">
              	  	<c:if test="${agentVo.is_probation eq '1' && agentVo.estate eq '33'}">
	                	<p class="flow_pic4"></p>
	                	<ul class="oz">
	                      <li class="step1 current">填写注册资料</li>
	                      <li class="step2 current">购买产品</li>
	                      <li class="step3 current">正在审核</li>
	                      <li class="step4 current">业务试用</li>
	                      <li class="step5">${agentEstateMap[agentVo.estate]}</li>
	                    </ul>
                    </c:if>
                    <c:if test="${agentVo.is_probation eq '0' || agentVo.estate eq '22'}">
	                	<p class="flow_pic5"></p>
	                	<ul class="oz">
	                      <li class="step1 current">填写注册资料</li>
	                      <li class="step2 current">购买产品</li>
	                      <li class="step3 current">正在审核</li>
	                      <li class="step4 current">业务试用</li>
	                      <li class="step5 current">${agentEstateMap[agentVo.estate]}</li>
	                    </ul>
                    </c:if>
                </div>
                <!--  
                <p class="state"><strong></strong>您的账号<span>${agentEstateMap[agentVo.estate]}</span><br />如果有问题请咨询QQ：930812452，电话：010-57386868-6366谢谢！</p>
                -->
                <c:choose>
                	<c:when test="${agentVo.estate eq '33'}">

                		<div class="verified_tip">
		                	<c:if test="${agentVo.is_probation eq '1'}">
		                		<p>火车票业务有10天试用期，在此期间分公司将对代理商进行终审。</p>
		                	</c:if>
		                	<c:if test="${agentVo.is_probation eq '0'}">
	             				<p>您的火车票业务已经审核通过，请点击车票预定进行火车票的购买。</p>
	             			</c:if>
	                	</div>
                	</c:when>
                	<c:otherwise>
                		<div class="verified_tip">
	             			<p>很抱歉，您的火车票业务审核未通过，如有问题请咨询客服。</p>
	                	</div>
                	</c:otherwise>
                </c:choose>
               	<div class="backg oz">
               		<!-- 审核通过显示有效期 -->
               		<c:if test="${productVo != null && agentVo.estate eq '33'}">

	                    <h3 class="validity"><span>业务有效期</span></h3>

	                     <table class="user_mes">
	                        <tr>
	                            <th>开始时间：</th>
	                            <td>${agentVo.begin_time}</td>
	                            <th style="text-align:left;" colspan="2">结束时间：<span style="font-weight:normal;">${agentVo.end_time}</span></th>
	                        </tr>
	                    </table>
	                    <div class="verified_tip verified_tip2">
	                        <p>我们会在业务到期的前3天提醒续费，请及时续费，避免影响业务的正常使用。</p>
	                    </div>
	                </c:if>

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
                    
                    <!-- 审核未通过显示购买产品 -->
                     <c:if test="${productVo != null && agentVo.estate eq '22'}">
		                 <table class="user_kinds">
	                       <tr>
		                      <td colspan="2" <c:if test="${productVo.name eq 'VIP用户'}">class="vip_user"</c:if>
	                     		<c:if test="${productVo.name eq '普通用户'}">class="vip_user no_vip"</c:if>>
	                      		<p class="pay_state">
	                      			<strong>状态：</strong>
									<c:choose>
		                    			<c:when test="${pay_status eq 'SUCCESS'}">支付成功</c:when>
		                    			<c:when test="${pay_status eq 'FAIL'}">支付失败</c:when>
		                    			<c:when test="${pay_status eq 'REFUNDING'}">退款中</c:when>
		                    			<c:when test="${pay_status eq 'REFUNDED'}">退款完成</c:when>
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
	                	<p>
	                		<!-- 审核通过显示续费按钮 -->
	                		<c:if test="${productVo != null && agentVo.estate eq '33'}">
	                			<input type="button" value="续 费" class="btn btn1" onclick="javascript:window.location='/joinUs/gotoRepayPage.jhtml'" />
	                		</c:if>
	                		<input type="button" value="投诉建议" class="btn btn2" onclick="javascript:window.location='/complain/complainIndex.jhtml?ques_Id=1'" />
	                	</p>
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
