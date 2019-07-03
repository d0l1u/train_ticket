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
</head>

<body>
	<div class="content oz">
		<div class="index_all">
		<!--左边内容 start-->
			<jsp:include flush="true" page="/pages/common/menu.jsp">
					<jsp:param name="menuId" value="join" />
				</jsp:include>
		<!--左边内容 end-->
    	<!--右边内容 start-->
    	<div class="infoinput-right oz">
         	<!--我要加盟-tab start-->
            <div class="join_us oz" style="height:650px;">
            	 <div class="agent_pass oz" >
	            	<ul class="agent_pass_ul">
	            		<li class="current1"><a href="/joinUs/agentInfoIndex.jhtml">个人信息</a></li>
	                	<li class="current current1"><a href="/joinUs/joinIndex.jhtml">我要开通</a></li>
	                	<li class="current1"><a href="/joinUs/realNameAuth.jhtml">实名认证</a></li>
	                </ul>
	         	</div>
            	<div class="flow_step">
                	<p class="flow_pic3"></p>
                	<ul class="oz">
                      <li class="step1 current">填写基本资料</li>
                      <li class="step2 current">实名认证</li>
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
                	<div class="backg oz" >
                     <table class="user_mes">
                   		<tr>
                            <th width="90" >代理商名：</th>
                            <td width="200" colspan="2">${agentVo.user_name}</td>
                            <th style="text-align:left;" colspan="2">代理商ID：<span style="font-weight:normal;">${agentVo.user_id}</span></th>
                        </tr>
                        <tr>
                            <th width="90">店铺名称：</th>
                            <td width="200" colspan="2">${agentVo.shop_name}</td>
                            <th style="text-align:left;" colspan="2">店铺简称：<span style="font-weight:normal;">${agentVo.shop_short_name}</span></th>
                        </tr>
                        <tr>
                            <th width="90">联系电话：</th>
                            <td width="200" colspan="2"> ${agentVo.user_phone}</td>
                            <th style="text-align:left;"  colspan="2">联系QQ：<span style="font-weight:normal;">${agentVo.user_qq}</span></th>
                        </tr>
                        <tr>
                            <th width="90">用户等级：</th>
                            <td width="200" colspan="2">
                            <c:if test="${agentLevel eq 0}">铜牌用户</c:if>
                            <c:if test="${agentLevel eq 1}">银牌用户</c:if>
                            <c:if test="${agentLevel eq 2}">金牌用户</c:if>
                            <c:if test="${agentLevel eq 3}">无</c:if>
                            <c:if test="${agentLevel eq 4}">无</c:if>
                            <c:if test="${agentLevel eq 5}">无</c:if>
                            </td>
                            <c:choose>
                             <c:when test="${agentVo.estate eq '11'}" >
                             	 <th style="text-align:left;"  colspan="2">开通状态：<span  style="font-weight:normal;">开通审核中</span></th>
                             </c:when>
                             <c:when test="${agentVo.estate eq '22'}">
                                 <th style="text-align:left;"  colspan="2">开通状态：<span  style="font-weight:normal;">开通审核未通过</span></th>
                             </c:when>
                             <c:when test="${agentVo.is_probation eq '0' && agentVo.estate eq '33'}">
                             	 <th style="text-align:left;"  colspan="2">开通状态：<span  style="font-weight:normal;">已开通</span></th>
                             </c:when>
                              <c:when test="${agentVo.is_probation eq '1' && agentVo.estate eq '33'}">
                                <th style="text-align:left;"  colspan="2">开通状态：<span  style="font-weight:normal;">业务试用</span></th>
	                        </c:when>
                             </c:choose>
                            
                        </tr>
                       	<tr class="dizhi">
                     	  		<th>所属区域：</th>
                         		<td colspan="3"><span  style="font-weight:normal;">${agentVo.city_name}&nbsp;${agentVo.district_name}</span></td>
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
	             </div>
            </div>
            <!--我要加盟-tab end-->
        </div>
        <!--右边内容 end-->
         </div>
    </div>
</body>
</html>
