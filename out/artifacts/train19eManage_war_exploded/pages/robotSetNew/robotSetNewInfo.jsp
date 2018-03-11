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
	
		
		$("#updateForm").attr("action", "/robotSetNew/updateRobotSetInfo.do");
		$("#updateForm").submit();
		//var index = parent.layer.getFrameIndex(window.name);
		 parent.reloadPage();
		//parent.layer.close();
	}
	
	function jiaoyan(){
		var zhifubao = $.trim( $("#zhifubao").val() );
		//var com_no = $.trim( $("#com_no").val() );
			zhifubao = encodeURI(zhifubao);
			//alert(zhifubao);
			if(zhifubao!=""){
			var url = "/robotSet/queryZhifubao.do?zhifubao="+zhifubao+"&version="+new Date();
			$.get(url,function(data){
				if(data == "yes"){
					alert("当前选择账号已绑定2个机器人，请重选账号！");
					return;
				}
			});
			}
		}
	function check(){
		var com_no = $.trim( $("#com_no").val());
			if(com_no!=""){
				var url = "/robotSetNew/queryComNo.do?com_no="+com_no+"&version="+new Date();
				$.get(url,function(data){
					if(data == "no"){
						alert("当前想要更改的短信猫口已被使用，请重选！");
						return;
					}
				});
			}
		}
</script>
		
<style>
	.pub_table td{height:20px;}
</style>
</head>

<body>
<div class="outer">
  	<div class="content1 oz">
  	<form action="/robotSetNew/updateRobotSetInfo.do" method="post" id="updateForm">
		<div class="left_con oz">
			<div class="pub_order_mes oz mb10_all">
					<h4>
						订单信息
					</h4>
					<div class="pub_con" >
						<table class="pub_table" >
							<tr>
								<td class="pub_yuliu" rowspan="18"></td>
								<td width="250">
									机器名 称 ：									
									<span><input type="text" class="worker_name"  name="worker_name" id="worker_name" value="${worker.worker_name}" size="60"/></span>
									<input type="hidden" name="worker_id" value="${worker.worker_id }" />
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
									请求地址：
									<input type="text" class="worker_ext"  name="worker_ext" id="worker_ext" value="${worker.worker_ext}" size="60"/>
								</td>
							</tr>
							<tr>
								<td>
									公网IP：
									<input type="text" class="public_ip"  name="public_ip" id="public_ip" value="${worker.public_ip}" size="40"/>
								</td>
							</tr>
							<c:if test="${worker.worker_type eq '3' }">
							<tr>
								<td>
									支付宝：
										<select id="zhifubao" name="zhifubao" style="width:250px;" onchange="jiaoyan()" >
											<option value="" 
												<c:if test="${empty zhifubao}">selected</c:if>>
													空
												</option>
							  		   		<!--
							  		   		<option value="huochepiao19e@163.com"
												<c:if test="${!empty zhifubao && zhifubao eq 'huochepiao19e@163.com'}">selected</c:if>>
													01
											</option>
											-->
											<c:forEach items="${zhanghaoList}" var="tt" varStatus="index">
												<option value="${tt.card_no }"
													<c:if test="${!empty zhifubao && tt.card_no eq zhifubao}">selected</c:if>>
														${tt.card_no}
												</option>
											</c:forEach>
										</select>
								</td>
							</tr>
							</c:if>
							<tr>
								<td>
									空闲数：
	    							<input type="text" class="spare_thread"  name="spare_thread" id="spare_thread" value="${worker.spare_thread}"size="5"/>
								</td>
							</tr>
							<tr>
								<td>
									优先级：
									<input type="text" class="worker_priority"  name="worker_priority" id="worker_priority" value="${worker.worker_priority}" size="5"/>
								</td>
							</tr>
							<tr>
								<td>
									连接超时：
	    							<input type="text" class="robot_con_timeout"  name="robot_con_timeout" id="robot_con_timeout" value="${worker.robot_con_timeout}" size="5"/>
								</td>
							</tr>
							<tr>
								<td>
									读取超时：
									<input type="text" class="robot_read_timeout"  name="robot_read_timeout" id="robot_read_timeout" value="${worker.robot_read_timeout}" size="5"/>
								</td>
							</tr>
							 <tr>
								<td>
									名单类型：
									<select id="app_valid" name="app_valid" style="width:80px;">
											<option value="0" 
												<c:if test="${worker.app_valid eq 0}">selected</c:if>>
													白名单
												</option>
												<option value="1" 
												<c:if test="${worker.app_valid eq 1}">selected</c:if>>
													黑名单
												</option>
										</select>
									<!-- 
									<input type="text" class="app_valid"  name="app_valid" id="app_valid" value="${worker.app_valid}" size="6"/>	
									 -->
								</td>
							</tr>
							<tr>
								<td>
									机器语言：
										<select id="worker_language" name="worker_language" style="width:100px;" onchange="" >
											<option value="" 
												<c:if test="${empty worker.worker_language_type}">selected</c:if>>
													空
												</option>
											<c:forEach items="${worker_languageList}" var="p" varStatus="index">
												<option value="${p.key}"
													<c:if test="${!empty worker_languageList && p.key eq worker.worker_language_type}">selected</c:if>>
														${p.value}
												</option>
											</c:forEach>
										</select>
								</td>
							</tr>
							<tr>
								<td>
									机器提供商：
										<select id="worker_vendor" name="worker_vendor" style="width:100px;" onchange="" >
											<option value="" 
												<c:if test="${empty worker.worker_vendor}">selected</c:if>>
													空
												</option>
											<c:forEach items="${worker_vendorList}" var="p" varStatus="index">
												<option value="${p.key}"
													<c:if test="${!empty worker_vendorList && p.key eq worker.worker_vendor}">selected</c:if>>
														${p.value}
												</option>
											</c:forEach>
										</select>
								</td>
							</tr>
							<tr>
								<td>
									机器区域：
										<select id="worker_region" name="worker_region" style="width:100px;" onchange="" >
											<option value="" 
												<c:if test="${empty worker.worker_region}">selected</c:if>>
													空
												</option>
											<c:forEach items="${worker_regionList}" var="p" varStatus="index">
												<option value="${p.key}"
													<c:if test="${!empty worker_regionList && p.key eq worker.worker_region}">selected</c:if>>
														${p.value}
												</option>
											</c:forEach>
										</select>
								</td>
							</tr>
							<tr>
								<td>
									机器描述：
									<input type="text"  class="worker_describe"  name="worker_describe" id="worker_describe" value="${worker.worker_describe}" size="20"/>
								</td>
							</tr>
							<c:if test="${worker.worker_type eq '3' }">
							<tr>
								<td>
				
									支付类型：
									<select id="pay_device_type" name="pay_device_type" style="width:80px;">
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
									短信猫口：
									<select id="com_no" name="com_no" style="width:80px;"  onchange="check()">
										<option value=" ">-- 请选择 --</option>
									    <option value="COM2" ${com_no=="COM2"?'selected':''}>COM2</option>
									    <option value="COM3" ${com_no=="COM3"?'selected':''}>COM3</option>
									    <option value="COM4" ${com_no=="COM4"?'selected':''}>COM4</option>
									    <option value="COM5" ${com_no=="COM5"?'selected':''}>COM5</option>
									    <option value="COM6" ${com_no=="COM6"?'selected':''}>COM6</option>
									    <option value="COM7" ${com_no=="COM7"?'selected':''}>COM7</option>
									    <option value="COM8" ${com_no=="COM8"?'selected':''}>COM8</option>
									    <option value="COM9" ${com_no=="COM9"?'selected':''}>COM9</option>
									    <option value="COM10" ${com_no=="COM10"?'selected':''}>COM10</option>
									    <option value="COM11" ${com_no=="COM11"?'selected':''}>COM11</option>
									    <option value="COM12" ${com_no=="COM12"?'selected':''}>COM12</option>
									    <option value="COM13" ${com_no=="COM13"?'selected':''}>COM13</option>
									    <option value="COM14" ${com_no=="COM14"?'selected':''}>COM14</option>
									    <option value="COM15" ${com_no=="COM15"?'selected':''}>COM15</option>
									    <option value="COM16" ${com_no=="COM16"?'selected':''}>COM16</option>
									    <option value="COM17" ${com_no=="COM17"?'selected':''}>COM17</option>
									    <option value="COM18" ${com_no=="COM18"?'selected':''}>COM18</option>
									    <option value="COM19" ${com_no=="COM19"?'selected':''}>COM19</option>
									    <option value="COM20" ${com_no=="COM20"?'selected':''}>COM20</option>
									    <option value="COM21" ${com_no=="COM21"?'selected':''}>COM21</option>
									    <option value="COM22" ${com_no=="COM22"?'selected':''}>COM22</option>
									    <option value="COM23" ${com_no=="COM23"?'selected':''}>COM23</option>
									    <option value="COM24" ${com_no=="COM24"?'selected':''}>COM24</option>
									    <option value="COM25" ${com_no=="COM25"?'selected':''}>COM25</option>
									    <option value="COM26" ${com_no=="COM26"?'selected':''}>COM26</option>
									    <option value="COM27" ${com_no=="COM27"?'selected':''}>COM27</option>
									    <option value="COM28" ${com_no=="COM28"?'selected':''}>COM28</option>
									    <option value="COM29" ${com_no=="COM29"?'selected':''}>COM29</option>
									    <option value="COM30" ${com_no=="COM30"?'selected':''}>COM30</option>
									    <option value="COM31" ${com_no=="COM31"?'selected':''}>COM31</option>
									    <option value="COM32" ${com_no=="COM32"?'selected':''}>COM32</option>
									    <option value="COM33" ${com_no=="COM33"?'selected':''}>COM33</option>
									</select>
								</td>
							</tr>
							</c:if>				
						</table>
					</div>
				</div>
			</div>
		</form>
			<p>
				<input type="button" value="保存修改" id="btnModify" onclick="submitUpdateInfo()"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#00FFFF;cursor:pointer;width:100px;height:30px;"/>
				<input type="button" value="返回" onclick="backPage();"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#FFA500;cursor:pointer;width:100px;height:30px;"/>
			</p>
			<div class="pub_passager_mes oz mb10_all">
						<h4>
							最近处理订单
						</h4>
						<div class="pub_con">
							<table class="pub_table" style="width: 650px; margin: 20px 20px;">
									<tr>
										<th style="width: 50px;">
											序号
										</th>
										<th style="width: 400px;">
											订单号
										</th>
										<th style="width: 100px;">
											发送请求时间
										</th>
										<th style="width: 100px;">
											操作类型
										</th>
									</tr>
								<c:forEach var="re" items="${report}" varStatus="idx">
									<tr align="center">
										<td>
											${idx.index+1 }
										</td>
										<td  style="word-break:break-all;width: 300px; ">
											${re.order_id}
										</td>
										<td>
											${fn:substringBefore(re.request_time, ' ')}
											<br />
											${fn:substringAfter(re.request_time, ' ')}
										</td>
										<td>
											<c:if test="${re.opt_type eq '1'}">预订</c:if>
											<c:if test="${re.opt_type eq '2'}">支付</c:if>
											<c:if test="${re.opt_type ne '1'&& re.opt_type ne '2'}">其他</c:if>
										</td>
									</tr>
								</c:forEach>
							</table>
						</div>
			</div>
			<div class="pub_passager_mes oz mb10_all">
						<h4>
							历史操作
						</h4>
						<div class="pub_con">
							<table class="pub_table" style="width: 650px; margin: 20px 20px;">
									<tr>
										<th style="width: 50px;">
											序号
										</th>
										<th style="width: 400px;">
											操作日志
										</th>
										<th style="width: 100px;">
											操作时间
										</th>
										<th style="width: 100px;">
											操作人
										</th>
									</tr>
								<c:forEach var="hs" items="${history}" varStatus="idx">
									<tr align="center">
										<td>
											${idx.index+1 }
										</td>
										<td align="left" style="word-break:break-all;width: 300px; ">
											${hs.content}
										</td>
										<td>
											${fn:substringBefore(hs.create_time, ' ')}
											<br />
											${fn:substringAfter(hs.create_time, ' ')}
										</td>
										<td>
											${hs.opt_person }
										</td>
									</tr>
								</c:forEach>
							</table>
						</div>
			</div>
	  </div>
   </div>
</body>
</html>
