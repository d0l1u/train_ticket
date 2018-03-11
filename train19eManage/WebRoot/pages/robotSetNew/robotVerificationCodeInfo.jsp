<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>修改机器人功能页面</title>
		<link rel="stylesheet" href="/css/style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
<script type="text/javascript">

	function submitUpdateInfo(){
		var verification_code=document.getElementById("verification_code").value;
		var worker_id=document.getElementById("worker_id").value;
		
		var url="/robotSetNew/addRobotSetCodeInfo.do?worker_id="+worker_id+"&verification_code="+verification_code+"&version="+new Date();
		if(confirm("确认添加验证码吗 ？")){
		$.get(url,function(data){
				if(data == "success"){
					$("form:first").submit();
				}
				if(data == "wrong"){
					alert("10分钟内不能添加相同验证码!");
				}
			});
		}
	}
	
</script>
		
<style>
	.pub_table td{height:40px;}
</style>
</head>

<body>
<div class="outer">
  	<div class="content1 oz">
  	<form action="/robotSetNew/queryRobotCodeInfo.do" method="post" id="updateForm">
		<div class="left_con oz">
			<div class="pub_order_mes oz mb10_all">
					<h4>
						订单信息
					</h4>
					<div class="pub_con" >
						<table class="pub_table" >
							<tr>
								<td class="pub_yuliu" rowspan="11"></td>
								<td width="250">
									名 称 ：
									<span>${worker.worker_name}</span>
									<input type="hidden" name="worker_id" id="worker_id" value="${worker.worker_id }" />
								</td>
							</tr>
							<tr>
								<td>
									状 态 ：
										<font 
											<c:if test="${worker.worker_status eq '11' }">color="green"</c:if>
											<c:if test="${worker.worker_status eq '22' }">color="#FF82AB"</c:if>
											<c:if test="${worker.worker_status eq '33' }">color="#1C86EE"</c:if>
										>
										${workerStatus[worker.worker_status]}</font>
								</td>
							</tr>
							<tr>
								<td>
									请求网址：
									<input type="text" readonly="readonly" class="worker_ext"  name="worker_ext" id="worker_ext" value="${worker.worker_ext}" size="60" />
								</td>
							</tr>
							<tr>
								<td>
									支付宝：
										<select id="zhifubao" disabled="disabled" name="zhifubao" style="width:250px;" >
											<option value="" 
												<c:if test="${empty zhifubao}">selected</c:if>>
													空
												</option>
											<c:forEach items="${zhanghaoList}" var="tt" varStatus="index">
												<option value="${tt.card_no }"
													<c:if test="${!empty zhifubao && tt.card_no eq zhifubao}">selected</c:if>>
														${tt.card_no}
												</option>
											</c:forEach>
										</select>
								</td>
							</tr>
							<tr>
								<td>
									空闲数：
	    							<input type="text" readonly="readonly" class="spare_thread"  name="spare_thread" id="spare_thread" value="${worker.spare_thread}"size="5"/>
								</td>
							</tr>
							 <tr>
								<td>
									名单类型：
									<select id="app_valid" disabled="disabled" name="app_valid" style="width:80px;">
											<option value="0" 
												<c:if test="${worker.app_valid eq 0}">selected</c:if>>
													白名单
												</option>
												<option value="1" 
												<c:if test="${worker.app_valid eq 1}">selected</c:if>>
													黑名单
												</option>
										</select>
								</td>
							</tr>
							<tr>
								<td>
				
									机器区域：
									<input type="text" disabled="disabled" class="worker_region"  name="worker_region" id="worker_region" value="${worker.worker_region}" size="8"/>
								</td>
							</tr>
							<tr>
								<td>
				
									支付类型：
									<select id="pay_device_type" disabled="disabled" name="pay_device_type" style="width:90px;">
											<option value="0" 
												<c:if test="${worker.pay_device_type eq 0}">selected</c:if>>
													PC端支付
												</option>
												<option value="1" 
												<c:if test="${worker.pay_device_type eq 1}">selected</c:if>>
													APP端支付
												</option>
										</select>
								</td>
							</tr>
							<tr>
								<td>
									<font size="5">验证码：</font>
									<input type="text" class="verification_code"  name="verification_code" id="verification_code" value="" size="35"/>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</form>
			<p>
				<input type="submit" value="保存验证码" id="btnModify" onclick="submitUpdateInfo()"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#00FFFF;cursor:pointer;width:100px;height:30px;"/>
				<input type="button" value="返回" onclick="backPage();"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#FFA500;cursor:pointer;width:100px;height:30px;"/>
			</p>
	  </div>
   </div>
</body>
</html>
