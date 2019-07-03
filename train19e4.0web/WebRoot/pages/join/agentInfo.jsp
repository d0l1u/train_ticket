<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>我要加盟-个人信息页</title>
<link rel="stylesheet" href="/css/style.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
</head>

<body>
	<div class="content oz">
		<div class="index_all">
		<!--左边内容 start-->
			<jsp:include flush="true" page="/pages/common/menu.jsp">
					<jsp:param name="menuId" value="book" />
				</jsp:include>
		<!--左边内容 end-->
    	<!--右边内容 start-->
    	<div class="infoinput-right oz" >
            <div class="join_us oz" style="height:650px;">
	           	 <c:if test="${(realPass eq '1' && agentVo.estate eq '11') or realPass eq '0' }">
	           	   <div class="agent_pass oz">
		            	<ul class="agent_pass_ul" >
		                	<li class="current current1"><a href="/joinUs/agentInfoIndex.jhtml">个人信息</a></li>
		                	<li class="current1"><a href="/joinUs/joinIndex.jhtml">我要开通</a>
		                	<li class="current1"><a href="/joinUs/realNameAuth.jhtml">实名认证</a></li>
		                </ul>
	         		</div>
	             </c:if>
	             <c:if test="${realPass eq '1' && agentVo.estate eq '22'}">
	             <div class="agent_pass oz">
		            	<ul class="agent_pass_ul" >
		                	<li class="current current1"><a href="/joinUs/agentInfoIndex.jhtml">个人信息</a></li>
		                	<li class="current1"><a href="/joinUs/joinIndex.jhtml">我要开通</a>
		                	<li class="current1"><a href="/joinUs/realNameAuth.jhtml">实名认证</a></li>
		                </ul>
	         	 </div>
	             </c:if>
	             <c:if test="${agentVo.estate eq '33' or agentVo.estate eq '44'}">
             		<div class="agent_pass oz">
		            	<ul class="agent_pass_ul">
		            		<li class="current current1"><a href="/joinUs/agentInfoIndex.jhtml">个人信息</a></li>
		                	<li class="current1"><a href="/joinUs/realNameAuth.jhtml">实名认证</a></li>
		                </ul>
	                </div>
	             </c:if>
	             
         		<br />
               	<div class="backg oz" >
                     <table class="user_mes" >
                   		<tr>
                            <th width="90" style="text-align:left;">代理商名：</th>
                            <td width="200" colspan="2">${agentVo.user_name}</td>
                            <th style="text-align:left;" colspan="2">代理商ID：<span style="font-weight:normal;">${agentVo.user_id}</span></th>
                        </tr>
                        <tr>
                            <th width="90" style="text-align:left;">店铺名称：</th>
                            <td width="200" colspan="2">${agentVo.shop_name}</td>
                            <th style="text-align:left;" colspan="2">店铺简称：<span style="font-weight:normal;">${agentVo.shop_short_name}</span></th>
                        </tr>
                        <tr>
                            <th width="90" style="text-align:left;">联系电话：</th>
                            <td width="200" colspan="2"> ${agentVo.user_phone}</td>
                            <th style="text-align:left;"  colspan="2">联系QQ：<span style="font-weight:normal;">${agentVo.user_qq}</span></th>
                        </tr>
                        <tr>
                            <th width="90" style="text-align:left;">用户等级：</th>
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
                     	  		<th style="text-align:left;">所属区域：</th>
                         		<td colspan="3"><span  style="font-weight:normal;">${agentVo.city_name}&nbsp;${agentVo.district_name}</span></td>
                    	</tr>
                       	<tr class="dizhi">
                       	  <th style="text-align:left;">详细地址：</th>
                            <td colspan="3">${agentVo.user_address}</td>
                        </tr>
                    </table>
                    <h3 class="contact" style="margin-left:40px;margin-top:20px;font-size:15px;"><span>实名认证信息</span></h3>
                    <div class="id-agent-result">
                     <input type="hidden" id="registNum" value="${fn:length(registList)}"/>
                       <h3><span class="serial">序号</span><span class="name">姓名</span><span class="id">身份证号</span><span class="tel">联系人电话</span><span class="state2">状态</span></h3>
                        <c:forEach varStatus="status" items="${registList}" var="regist">
					       <c:choose>
					         <c:when test="${regist.regist_status eq '22'}">
		                        <ul class="oz person_data" id="person_data_${regist.regist_id}">
		                        	<li class="serial">${status.index + 1}</li>
		                            <li class="name">${regist.user_name}</li>
		                            <li class="id">${regist.ids_card}</li>
		                            <li class="tel">&nbsp;${regist.user_phone}</li>
			                        <li class="state2" style="color:blue;">已通过</li>
		                        </ul>
	                        </c:when>
	                       	<c:when test="${regist.regist_status eq '33'}">
	                       	 	 <ul class="oz person_data" id="person_data_${regist.regist_id}">
	                       	 		<li class="serial">${status.index + 1}</li>
		                            <li class="name">${regist.user_name}</li>
		                            <li class="id">${regist.ids_card}</li>
		                            <li class="tel">&nbsp;${regist.user_phone}</li>
	                        		<li class="state2" style="color:red;">未通过</li>
	                        		<br/>
	                        	 <c:if test="${regist.fail_reason eq '1'}">
	                       	 		<li id="deleteFail_${regist.regist_id}" style="display:inline;margin-left:110px;line-height:22px;color:#ff8e17;">失败原因：实名制身份信息有误！</li>
	                        	 </c:if>
	                        	  <c:if test="${regist.fail_reason eq '2'}">
	                       	 		<li id="deleteFail_${regist.regist_id}" style="margin-left:110px;line-height:22px;color:#ff8e17;">失败原因：您提交的身份信息无法实名，请更换其他身份信息！</li>
	                        	 </c:if>
	                        	</ul>
			                </c:when>
			                <c:when test="${regist.regist_status eq '55'}">
	                       	 	 <ul class="oz person_data" id="person_data_${regist.regist_id}">
	                       	 		<li class="serial">${status.index + 1}</li>
		                            <li class="name">${regist.user_name}</li>
		                            <li class="id">${regist.ids_card}</li>
		                            <li class="tel">&nbsp;${regist.user_phone}</li>
	                        		<li class="state2">待核验</li>
	                        		<br/>
	                        	   <input type="hidden" id="alreadyIdsCard_${regist.regist_id}" name="alreadyIdsCard" value="${regist.ids_card}"/>
	                        	   <input type="hidden" id="alreadyUserName_${regist.regist_id}" name="alreadyUserName" value="${regist.user_name}"/>
	                       	 		<li id="deleteFail_${regist.regist_id}" style="display:inline;margin-left:110px;"><p style="margin: 0;line-height:22px;color:#ff8e17;">该联系人身份信息待核验,需本人持有效证件去车站窗口实名认证后方可购票！</p></li>
	                       	 	</ul>
			                </c:when>
			                <c:otherwise>
			                 <ul class="oz person_data" id="person_data_${regist.regist_id}">
	                       	 		<li class="serial">${status.index + 1}</li>
		                            <li class="name">${regist.user_name}</li>
		                            <li class="id">${regist.ids_card}</li>
		                            <li class="tel">&nbsp;${regist.user_phone}</li>
	                        		<li class="state2">审核中</li>
	                        	 </ul>
	                        </c:otherwise>
	                       </c:choose> 
                        </c:forEach>
                       <div style="height:60px;">
                       		<c:if test="${(agentLevel eq 3) or (agentLevel eq 4) or (agentLevel eq 5)}">
                       			<p style="text-align:center;"><input type="button" class="btn realName_btn" value="立即认证" onclick="javascript:window.location='/joinUs/realNameAuth.jhtml'"/></p>
                       		 </c:if>
                        </div>
                    </div>
	            </div>
	          
            </div>
        </div>
        <!--右边内容 end-->
    </div>
    </div>
</body>
</html>
