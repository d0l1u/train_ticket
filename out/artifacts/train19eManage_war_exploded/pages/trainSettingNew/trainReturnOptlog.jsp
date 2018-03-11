<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.PageVo"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>系统返回日志页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	    <script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
	    <script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
	    <script type="text/javascript">
	    <%
			PageVo pageObject = (PageVo) request.getAttribute("pageBean");
			int pageIndex = pageObject.getPageIndex();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String user_level = loginUserVo.getUser_level();
		%>
	   		//显示添加栏
	   		function addpage(){
			          document.getElementById('divcon').style.display = "block";
			           document.getElementById('addRizhi').style.display = "none";
			            document.getElementById('submitRizhi').style.display = "block";
	   		}
	   		 //增加返回日志
			function addtrain_return_optlog() {
				var	return_id=$("#return_id_add").val();
				if(return_id==''){
				alert("编号不能为空！");
				return;
				}else if(!/^[0-9a-zA-Z]*$/g.test($.trim($("#return_id_add").val()))){
				alert("只能输入数字或字母的编号");
				return;
				}else if($("#return_id_add").val().length!=2){
				alert("编号长度只能是两个字节");
				return;
				}else if($("#return_type_add").val()=="11" && $("#return_fail_reason_add").val()==""){
				alert("处理方式为失败,需要选择失败原因!");
				return;
				}else{
				var url = "/trainSetting/queryreturn_optlog_id.do?return_id="+return_id+"&version="+new Date();
				$.get(url,function(data){
					if(data == "yes"){
						$("form:first").attr("action","/trainSetting/addtrain_return_optlog.do?pageIndex="+<%=pageIndex%>);
						$("form:first").submit();
					}else{
						alert("添加编号已存在，请重新输入！");
					}
				});
				
				
				}
				
			}
			 //修改返回日志
			function updatetrain_return_optlog(return_id) {
				$("form:first").attr("action","/trainSetting/updatetrain_return_optlog.do?return_id="+return_id+"&pageIndex="+<%=pageIndex%>);
				$("form:first").submit();
			}
			 //删除返回日志
			function deletetrain_return_optlog(return_id) {
			if(confirm("确认删除返回日志吗?")) {
				$("form:first").attr("action","/trainSetting/deletetrain_return_optlog.do?return_id="+return_id+"&pageIndex="+<%=pageIndex%>);
				$("form:first").submit();
				}
			}
			//查看操作日志
			function opt_rizhi(){
				var url="/trainSetting/queryHistory.do";
				showlayer('操作日志',url,'950px','600px')
			}
			//返回
			function goback(){
				$("form:first").attr("action","/trainSetting/trainSettingPage.do");
				$("form:first").submit();
			}
			
			function submitForm(){
			
		    $("form:first").attr("action","/trainSetting/gototrain_return_optlog.do");
		    $("form:first").submit();
		}
		
	    </script>
	    <style>
	    .train_returnlog{width:1000px;margin-top: 30px;margin-left: 10px;}
		.train_returnlog table{border:1px solid #dadada;width:1000px;text-align:center;}
		.train_returnlog th,.train_returnlog td,.train_returnlog tr{border:1px solid #dadada;}
		.train_returnlog th{height:50px;font:bold 13px/30px "宋体";}
		.train_returnlog td{line-height:40px;}
		</style>
	</head>
	<body>
	
		<div id="fullbg"  style="border: 0px solid #00CC00; margin: 10px;">
			<form action="/trainSetting/gototrain_return_optlog.do" method="post" name="myform" id="myform">
			<div class="book_manage oz">
			<ul class="order_num oz" style="margin-top:10px;">
					<li>
						出票日志：&nbsp;
						<input type="text" id="return_name_find" name="return_name_find" value="${list.return_name }" style="width:300px;"/>
					</li>
					<li>
						返回结果：&nbsp;
						<input type="text" id="return_value_find" name="return_value_find" value="${list.return_value }" style="width: 119px;"/>
					</li>
					 <li>处理方式：&nbsp;&nbsp;<select name="return_type_find" id="return_type_find" style="width:110px;">
								<option value="" style="color:gray;"
								<c:if test="${!empty list.return_type && list.return_type eq ''}">selected</c:if>>
									请选择处理方式
								</option>
								<option value="00"
								<c:if test="${!empty list.return_type && list.return_type eq '00'}">selected</c:if>>
									重发
								</option>
								<option value="11"
									<c:if test="${!empty list.return_type && list.return_type eq '11'}">selected</c:if>>
									失败
								</option>
								<option value="22"
									<c:if test="${!empty list.return_type && list.return_type eq '22'}">selected</c:if>>
									人工
								</option>
								<option value="33"
									<c:if test="${!empty list.return_type && list.return_type eq '33'}">selected</c:if>>
									按时重发
								</option>
							</select>
					</li>
					 <li>启用状态：&nbsp;&nbsp;<select name="return_status_find" id="return_status_find" style="width:110px;">
								
								<option value="" style="color:gray;"
								<c:if test="${!empty list.return_status && list.return_status eq ''}">selected</c:if>>
									请选择启用状态
								</option>
								<option value="0"
								<c:if test="${!empty list.return_status && list.return_status eq '0'}">selected</c:if>>
									否
								</option>
								<option value="1"
									<c:if test="${!empty list.return_status && list.return_status eq '1'}">selected</c:if>>
									是
								</option>
							</select>
					</li>
					
					
				</ul>
				
				
				<ul class="order_num oz" style="margin-top:10px;">
					<li>
						是否重发：&nbsp;&nbsp;<select name="return_join_find" id="return_join_find" style="width:110px;">
								<option  value=""
								<c:if test="${!empty list.return_join && list.return_join eq ''}">selected</c:if>>
									请选择是否重发
								</option>
								<option value="0"
								<c:if test="${!empty list.return_join && list.return_join eq '0'}">selected</c:if>>
									否
								</option>
								<option value="1"
									<c:if test="${!empty list.return_join && list.return_join eq '1'}">selected</c:if>>
									是
								</option>
							</select>
					</li>
					<li>
						出票方式：&nbsp;
						<select name="return_ticket_find" id="return_ticket_find" style="width:110px;">
						<option value=""
								<c:if test="${!empty list.return_ticket && list.return_ticket eq ''}">selected</c:if>>
									请选择出票方式
								</option>
								<option value="11"
								<c:if test="${!empty list.return_ticket && list.return_ticket eq '11'}">selected</c:if>>
									出票
								</option>
								<option value="22"
									<c:if test="${!empty list.return_ticket && list.return_ticket eq '22'}">selected</c:if>>
									退票
								</option>
							</select>
					</li>
					 <li>机器人：&nbsp;&nbsp;
						  <select name="return_active_find" id="return_active_find" style="width:100px;">
						  	
						       <option value=""
								<c:if test="${!empty list.return_ticket && list.return_ticket eq ''}">selected</c:if>>
									请选择机器人
								</option>
								<option value="00"
								<c:if test="${!empty list.return_active && list.return_active eq '00'}">selected</c:if>>
									预订
								</option>
								<option value="11"
									<c:if test="${!empty list.return_active && list.return_active eq '11'}">selected</c:if>>
									支付
								</option>
								<option value="22"
									<c:if test="${!empty list.return_active && list.return_active eq '22'}">selected</c:if>>
									改签
								</option>
								<option value="33"
									<c:if test="${!empty list.return_active && list.return_active eq '33'}">selected</c:if>>
									退票
								</option>
							</select>
					</li>
					 <li>失败原因：&nbsp;&nbsp;
						<select  name="return_fail_reason_find" id="return_fail_reason_find" style="width:150px;">
								<option value="" style="color:gray;"
								<c:if test="${!empty list.return_fail_reason && list.return_fail_reason eq ''}">selected</c:if>>
									请选择失败原因
								</option>
								<option value="1"
									<c:if test="${!empty list.return_fail_reason && list.return_fail_reason eq '1'}">selected</c:if>>
									所购买的车次坐席已无票
								</option>
								<option value="2"
									<c:if test="${!empty list.return_fail_reason && list.return_fail_reason eq '2'}">selected</c:if>>
									身份证件已经实名制购票
								</option>
								<option value="3"
									<c:if test="${!empty list.return_fail_reason && list.return_fail_reason eq '3'}">selected</c:if>>
									 票价和12306不符 
								</option>
								<option value="4"
									<c:if test="${!empty list.return_fail_reason && list.return_fail_reason eq '4'}">selected</c:if>>
									 乘车时间异常
								</option>
								<option value="5"
									<c:if test="${!empty list.return_fail_reason && list.return_fail_reason eq '5'}">selected</c:if>>
									 证件错误
								</option>
								<option value="6"
									<c:if test="${!empty list.return_fail_reason && list.return_fail_reason eq '6'}">selected</c:if>>
									 用户要求取消订单
								</option>
								<option value="7"
									<c:if test="${!empty list.return_fail_reason && list.return_fail_reason eq '7'}">selected</c:if>>
									 未通过12306实名认证
								</option>
								<option value="8"
									<c:if test="${!empty list.return_fail_reason && list.return_fail_reason eq '8'}">selected</c:if>>
									 乘客身份信息待核验
								</option>
								<option value="9"
									<c:if test="${!empty list.return_fail_reason && list.return_fail_reason eq '9'}">selected</c:if>>
									系统异常
								</option>
								<option value="11"
									<c:if test="${!empty list.return_fail_reason && list.return_fail_reason eq '11'}">selected</c:if>>
									超时未支付
								</option>
								<option value="10"
									<c:if test="${!empty list.return_fail_reason && list.return_fail_reason eq '10'}">selected</c:if>>
									高消费限制失败
								</option>
								<option value="12"
									<c:if test="${!empty list.return_fail_reason && list.return_fail_reason eq '12'}">selected</c:if>>
									 信息冒用
								</option>
								
								<option value="29"
									<c:if test="${!empty list.return_fail_reason && list.return_fail_reason eq '29'}">selected</c:if>>
								 	该车次未到起售时间
								</option>
							</select>
					</li>
				</ul>
				
			    <p style="height: 37px; margin-top:30px">
					
					 <input type="button" value="查 询" class="btn" onclick="submitForm();"/>
				</p>
			
			<div class="train_returnlog">
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #f0f0f0;">
							<th>
								序号
							</th>
							<th>
								编号
							</th>
							<th>
								出票日志
							</th>
							<th>
								返回结果
							</th>
							<th>
								处理方式
							</th>
							<th>
								启用状态
							</th>
							<th>
								是否重发
							</th>
							<th>
								出票方式
							</th>
							<th>
								机器人
							</th>
							<th>
								失败原因
							</th>
							<th>
								操作
							</th>
							
						</tr>
						
						<c:forEach var="list" items="${train_return_optlogList}" varStatus="idx">
						<tr>
							<td>
								${idx.index+1}
							</td>
							<td>
								${list.return_id }
							</td>
							<td>
								<input type="text" id="return_name_${list.return_id }" name="return_name_${list.return_id }" value="${list.return_name }" style="width:300px;"/>
							</td>
							<td>
								<input type="text" id="return_value_${list.return_id }" name="return_value_${list.return_id }" value="${list.return_value }" style="width:70px;"/>
							</td>
							<td>
								<select name="return_type_${list.return_id }" id="return_type_${list.return_id }" style="width:70px;">
								<option value="00"
								<c:if test="${!empty list.return_type && list.return_type eq '00'}">selected</c:if>>
									重发
								</option>
								<option value="11"
									<c:if test="${!empty list.return_type && list.return_type eq '11'}">selected</c:if>>
									失败
								</option>
								<option value="22"
									<c:if test="${!empty list.return_type && list.return_type eq '22'}">selected</c:if>>
									人工
								</option>
								<option value="33"
									<c:if test="${!empty list.return_type && list.return_type eq '33'}">selected</c:if>>
									按时重发
								</option>
							</select>
							</td>
							<td>
							<select name="return_status_${list.return_id }" id="return_status_${list.return_id }" style="width:50px;">
								<option value="0"
								<c:if test="${!empty list.return_status && list.return_status eq '0'}">selected</c:if>>
									否
								</option>
								<option value="1"
									<c:if test="${!empty list.return_status && list.return_status eq '1'}">selected</c:if>>
									是
								</option>
							</select>
							</td>
							<td>
							<select name="return_join_${list.return_id }" id="return_join_${list.return_id }" style="width:50px;">
								<option value="0"
								<c:if test="${!empty list.return_join && list.return_join eq '0'}">selected</c:if>>
									否
								</option>
								<option value="1"
									<c:if test="${!empty list.return_join && list.return_join eq '1'}">selected</c:if>>
									是
								</option>
							</select>
							</td>
							<td>
							<select name="return_ticket_${list.return_id }" id="return_ticket_${list.return_id }" style="width:50px;">
								<option value="11"
								<c:if test="${!empty list.return_ticket && list.return_ticket eq '11'}">selected</c:if>>
									出票
								</option>
								<option value="22"
									<c:if test="${!empty list.return_ticket && list.return_ticket eq '22'}">selected</c:if>>
									退票
								</option>
							</select>
							</td>
							<td>
							<select name="return_active_${list.return_id }" id="return_active_${list.return_id }" style="width:50px;">
								<option value="00"
								<c:if test="${!empty list.return_active && list.return_active eq '00'}">selected</c:if>>
									预订
								</option>
								<option value="11"
									<c:if test="${!empty list.return_active && list.return_active eq '11'}">selected</c:if>>
									支付
								</option>
								<option value="22"
									<c:if test="${!empty list.return_active && list.return_active eq '22'}">selected</c:if>>
									改签
								</option>
								<option value="33"
									<c:if test="${!empty list.return_active && list.return_active eq '33'}">selected</c:if>>
									退票
								</option>
							</select>
							</td>
							<td style="width: 150px;">
							<select  name="return_fail_reason_${list.return_id }" id="return_fail_reason_${list.return_id }" style="width:150px;">
								<option value="" style="color:gray;"
								<c:if test="${!empty list.return_fail_reason && list.return_fail_reason eq ''}">selected</c:if>>
									请选择失败原因
								</option>
								<option value="1"
									<c:if test="${!empty list.return_fail_reason && list.return_fail_reason eq '1'}">selected</c:if>>
									所购买的车次坐席已无票
								</option>
								<option value="2"
									<c:if test="${!empty list.return_fail_reason && list.return_fail_reason eq '2'}">selected</c:if>>
									身份证件已经实名制购票
								</option>
								<option value="3"
									<c:if test="${!empty list.return_fail_reason && list.return_fail_reason eq '3'}">selected</c:if>>
									 票价和12306不符 
								</option>
								<option value="4"
									<c:if test="${!empty list.return_fail_reason && list.return_fail_reason eq '4'}">selected</c:if>>
									 乘车时间异常
								</option>
								<option value="5"
									<c:if test="${!empty list.return_fail_reason && list.return_fail_reason eq '5'}">selected</c:if>>
									 证件错误
								</option>
								<option value="6"
									<c:if test="${!empty list.return_fail_reason && list.return_fail_reason eq '6'}">selected</c:if>>
									 用户要求取消订单
								</option>
								<option value="7"
									<c:if test="${!empty list.return_fail_reason && list.return_fail_reason eq '7'}">selected</c:if>>
									 未通过12306实名认证
								</option>
								<option value="8"
									<c:if test="${!empty list.return_fail_reason && list.return_fail_reason eq '8'}">selected</c:if>>
									 乘客身份信息待核验
								</option>
								<option value="9"
									<c:if test="${!empty list.return_fail_reason && list.return_fail_reason eq '9'}">selected</c:if>>
									系统异常
								</option>
								<option value="11"
									<c:if test="${!empty list.return_fail_reason && list.return_fail_reason eq '11'}">selected</c:if>>
									超时未支付
								</option>
								<option value="10"
									<c:if test="${!empty list.return_fail_reason && list.return_fail_reason eq '10'}">selected</c:if>>
									高消费限制失败
								</option>
								<option value="12"
									<c:if test="${!empty list.return_fail_reason && list.return_fail_reason eq '12'}">selected</c:if>>
									 信息冒用
								</option>
								<option value="29"
									<c:if test="${!empty list.return_fail_reason && list.return_fail_reason eq '29'}">selected</c:if>>
								 	该车次未到起售时间
								</option>
							</select>
							</td>
							<td>
								<a  href="javascript:updatetrain_return_optlog('${list.return_id}')">修改</a>
								<a  href="javascript:deletetrain_return_optlog('${list.return_id}')">删除</a>
							</td>
							</tr>
						</c:forEach>
					</table>
					<jsp:include page="/pages/common/paging.jsp" />
				</c:if>
					</div>
					
					</div>
					<div id="divcon" style="display:none;margin:30px 0px 20px 200px" >
					
					<table >
						<tr>
							<td>编号：</td>
							<td><input type="text" id="return_id_add" name="return_id_add"/></td>
						</tr>
						<tr>
							<td>出票日志：</td>
							<td><input type="text" id="return_name_add" name="return_name_add"/></td>
						</tr>
						<tr>
							<td>返回结果：</td>
							<td><input type="text" id="return_value_add" name="return_value_add"/></td>
						</tr>
						<tr>
							<td>处理方式：</td>
							<td>
							<select name="return_type_add" id="return_type_add" style="width:50px;">
								<option value="00">
									重发
								</option>
								<option value="11">
									失败
								</option>
								<option value="22">
									人工
								</option>
								<option value="33">
									按时重发
								</option>
							</select>
							</td>
						</tr>
						<tr>
							<td>启用状态：</td>
							<td>
							<select name="return_status_add" id="return_status_add" style="width:50px;">
								<option value="0">
									否
								</option>
								<option value="1">
									是
								</option>
							</select>
							</td>
						</tr>
						<tr>
							<td>是否重发：</td>
							<td>
							<select name="return_join_add" id="return_join_add" style="width:50px;">
								<option value="0">
									否
								</option>
								<option value="1">
									是
								</option>
							</select>
							</td>
						</tr>
						<tr>
							<td>出票方式：</td>
							<td>
							<select name="return_ticket_add" id="return_ticket_add" style="width:50px;">
								<option value="11" >
									出票
								</option>
								<option value="22">
									退票
								</option>
							</select>
							</td>
						</tr>
						<tr>
							<td>机器人：</td>
							<td>
							<select name="return_active_add" id="return_active_add" style="width:50px;">
								<option value="00" >
									预订
								</option>
								<option value="11">
									支付
								</option>
								<option value="22">
									改签
								</option>
								<option value="33">
									退票
								</option>
							</select>
							</td>
						</tr>
						
						<tr>
							<td>失败原因：</td>
							<td>
							<select name="return_fail_reason_add" id="return_fail_reason_add">
								<option value="" style="color:gray;">
										请选择失败原因
								</option>
								<option value="1" >
									所购买的车次坐席已无票
								</option>
								<option value="2">
									身份证件已经实名制购票
								</option>
								<option value="3">
									票价和12306不符
								</option>
								<option value="4">
									乘车时间异常 
								</option>
								<option value="5">
									证件错误
								</option>
								<option value="6">
									用户要求取消订单
								</option>
								<option value="7">
									未通过12306实名认证
								</option>
								<option value="8">
									乘客身份信息待核验
								</option>
								<option value="9">系统异常</option>
								<option value="11">超时未支付</option>
								<option value="10">高消费限制失败</option>
								<option value="12">信息冒用</option>
								<option value="29">该车次未到起售时间</option>
							</select>
							</td>
						</tr>
					</table>
					
					</div>
					<div class="book_manage oz"  style="margin: 30px;" align="center" >
					<p>
						<input type="button" value="增加返回日志" id="addRizhi" class="btn" onclick="javascript:addpage();" style="display:block;"/>
						<input type="button" value="完成" id="submitRizhi" class="btn" onclick="javascript:addtrain_return_optlog();" style="display:none;"/>
						<input type="button" value="返回" class="btn" onclick="javascript:goback();"style="margin-left: 120px;float: left;margin-top: -34px;"/>
						<input type="button" value="操作日志" class="btn" id="rizhi" onclick="opt_rizhi()" style="margin-left: 20px;float: left;margin-top: -34px;"/>
					</p>
					</div>
					
			</form>
		</div>
	</body>
</html>