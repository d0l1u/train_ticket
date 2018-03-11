<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.PageVo"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>app用户管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
	</head>
	<script type="text/javascript">
	<%
		PageVo pageObject = (PageVo) request.getAttribute("pageBean");
		int pageIndex = pageObject.getPageIndex();
	%>
	function updatePassword(user_id){
		var url="/appUser/toUpdateAppUser.do?user_id="+user_id+"&version="+new Date();
		showlayer('修改密码',url,'800px','600px')
	}
	
	function queryUserDetail(user_id,referee_account_num){
		var url="/appUser/queryAppUserInfo.do?user_id="+user_id+"&referee_account_num="+referee_account_num+"&version="+new Date();
		showlayer('明细',url,'800px','800px')
	}
	
	function deleteUser(user_id){
		if (confirm("确定要删除吗？")){				
			var url = "/appUser/deleteAppUser.do?user_id="+user_id+"&version="+new Date();
			$.post(url,{user_id:user_id},function(data){
				if(data=="yes"){
					$("form:first").attr("action","/appUser/queryAppUserList.do?pageIndex="+<%=pageIndex%>);
					$("form:first").submit();
				}else{
					alert("删除用户失败");
				}
			});		
		}else{		
			return false;
		}
	}

	//全选操作
	  function checkChnRetRuleAll(){
	      var user_id = document.getElementsByName("user_id");
	      var tag1 = document.getElementById("tag1").value;
	      if(tag1 == 0 ){
	         for(var i = 0 ; i < user_id.length ; i++){
	        	 user_id[i].checked = true ;
	         }
	         document.getElementById("tag1").value = 1;
	      }else if(tag1 == 1){
	         for(var i = 0 ; i < user_id.length ; i++){
	        	 user_id[i].checked = false ;
	         }
	         document.getElementById("tag1").value = 0;
	      }
	  }
	  
	//批量操作启用,停用和删除
	function batchStop(type){
	  	document.getElementById("batchStoptype").value = type; 
	  	var aa = document.getElementsByName("user_id");
	  	var ii = 0;
	  	for (var i=0; i<aa.length; i++){
		  	if(aa[i].checked==true){
			  	ii++;
			}
		}
		if(ii==0){
			alert("请选中一条账号!");
			return false;
		}
		var batchStopnum = aa.length;
		var info = "";
		if(type == '4'){
			info = "确认将所选的"+ii+"条账号停用吗?";
			if(confirm(info)){
				document.queryFrm.action="/appUser/updateAppUserbatch.do?pageIndex="+<%=pageIndex%>+"&type="+type+"&version="+new Date();
				document.queryFrm.submit();
			}
		}else if(type == '1'){
			info = "确认将所选的"+ii+"条账号启用吗?";
			if(confirm(info)){
				document.queryFrm.action="/appUser/updateAppUserbatch.do?pageIndex="+<%=pageIndex%>+"&type="+type+"&version="+new Date();
				document.queryFrm.submit();
			}
		}
	}

</script>
	<body><div></div>
		<div class="book_manage oz">
			<form  name="queryFrm" action="/appUser/queryAppUserList.do" method="post">
				<ul class=" oz">
					<li>
						姓名：&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="user_name" value="${user_name }"/>
					</li>
					<li>
						账号：&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="user_phone" value="${user_phone }"/>
					</li>
					<li>
						登录时间：&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="begin_info_time" readonly="readonly" value="${begin_info_time }"
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" id="defaultStartDate1"/>
						至
						<input type="text" class="text" name="end_info_time" readonly="readonly" value="${end_info_time }"
										onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>
				</ul>
				<ul class="ser oz">
					<li>
						来源：
					</li>
					<c:forEach items="${channelSource }" var="s" varStatus="index">
						<li>
							<input type="checkbox" id="order_status${index.count }" name="channel" value="${s.key }"
							<c:if test="${fn:contains(channel, s.key ) }">checked="checked"</c:if> />
							<label for="order_status${index.count }">${s.value }</label>
						</li>
					</c:forEach>
					<li>&nbsp;</li><li>&nbsp;</li>
				</ul>
				<ul class="ser oz">
					<li>
						状态：
					</li>
					<c:forEach items="${weatherAble }" var="s" varStatus="index">
						<li>
							<input type="checkbox" id="weather_able${index.count }" name="weather_able" value="${s.key }"
							<c:if test="${fn:contains(weather_able, s.key ) }">checked="checked"</c:if> />
							<label for="weather_able${index.count }">${s.value }</label>
						</li>
					</c:forEach>
				</ul>
				<!-- 
				<ul class="ser oz">
					<li>
						状态：
					</li>
					<c:forEach items="${weatherAble }" var="s" varStatus="index">
						<li>
							<input type="checkbox" id="weather_able${index.count }" name="weather_able" value="${s.key }"
							<c:if test="${fn:contains(weather_able, s.key ) }">checked="checked"</c:if> />
							<label for="weather_able${index.count }">${s.value }</label>
						</li>
					</c:forEach>
				</ul>
				 -->
				<ul class="ser oz">
					<li>
						积分：&nbsp;
						<select name="score_num" id="score_num" style="width: 130px;" value="${score_num }">
			        		<option value="" <c:if test="${empty score_num}">selected</c:if>>所有</option>
			        		<option value="0-99" <c:if test="${!empty score_num && score_num eq '0-99'}">selected="selected"</c:if>>0-99</option>
			        		<option value="100-499" <c:if test="${!empty score_num && score_num eq '100-499'}">selected="selected"</c:if>>100-499</option>
			        		<option value="500-999" <c:if test="${!empty score_num && score_num eq '500-999'}">selected="selected"</c:if>>500-999</option>
			        		<option value="1000-100000000" <c:if test="${!empty score_num && score_num eq '1000-100000000'}">selected="selected"</c:if>>1000以上</option>
			       		</select>
					</li>
					<li>
						登陆次数：&nbsp;
						<select name="login_num" id="login_num" style="width: 130px;" value="${login_num }">
	            			<option value="" <c:if test="${empty login_num}">selected</c:if>>所有</option>
			        		<option value="0-9" <c:if test="${!empty login_num && login_num eq '0-9'}">selected="selected"</c:if>>0-9</option>
			        		<option value="10-49" <c:if test="${!empty login_num && login_num eq '10-49'}">selected="selected"</c:if>>10-49</option>
			        		<option value="50-99" <c:if test="${!empty login_num && login_num eq '50-99'}">selected="selected"</c:if>>50-99</option>
			        		<option value="100-100000000" <c:if test="${!empty login_num && login_num eq '100-100000000'}">selected="selected"</c:if>>100以上</option>
			       		</select>
					</li>
				</ul>
				<p>
					<input type="submit" value="查 询" class="btn" />
				</p>
				<c:if test="${!empty isShowList}">
				<div class="pub_debook_mes  oz mb10_all">
					<span style="font-size:14px;float:right;">符合条件用户总数：<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${totalCount }</span>个</span>
				</div>
				
					<table>
						<tr style="background: #EAEAEA;">
							<th style="width:40px;">全选 <br/><input type="checkbox" id="checkChnRetRulAll" name="checkChnRetRulAll" onclick="checkChnRetRuleAll()"></th>
							<th>
								序号
							</th>
							<th>
								账号
							</th>
							<th>
								姓名
							</th>
							<th>
								开通时间
							</th>
							<!-- 
							<th>
								登录时间
							</th>
							 -->
							<th>
								最后登录
							</th>
							<th>
								登陆次数
							</th>
							<th>
								来源
							</th>
							<th>
								积分
							</th>
							<th>
								推荐人个数
							</th>
							<th>
								常用乘客
							</th>
							<th>
								手机型号
							</th>
							<th>
								状态
							</th>
							<th>
								操作人
							</th>
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${appUserList}" varStatus="idx">
						<c:choose>
						<c:when test="${list.weather_able eq '1'}">
							<tr style="background: #BEE0FC;">
						</c:when>
						<c:otherwise>
							<tr style="background: #E0F3ED;">
						</c:otherwise>
						</c:choose>
							<td><input type="checkbox" id="user_id" name="user_id" value="${list.user_id }"/></td>
							<td>
								${idx.index+1}
							</td>
							<td>
								${list.user_phone}
							</td>
							<td>
								${list.user_name}
							</td>
							<td>
								${list.create_time}
							</td>
							<!-- 
							<td>
								${list.login_time }
							</td>
							 -->
							<td>
								${list.last_login_time}
							</td>
							<td>
								${list.login_num }
							</td>
							<td>
								${channelSource[list.channel] }
							</td>
							<td>
								${list.score_num}
							</td>
							<td>
								${list.referee_account_num}
							</td>
							<td>
								${list.linker_num}
							</td>
							<td>
								${list.phone_pattern }
							</td>
							<td>
								${weatherAble[list.weather_able ] }
							</td>
							<td>
								${list.opt_ren }
							</td>
							<td>
								<!-- 
								<a href="/appUser/toUpdateAppUser.do?user_id=${list.user_id }" onclick="return confirm('确认修改么？')">修改密码</a>
								<a href="/appUser/deleteAppUser.do?user_id=${list.user_id }&pageIndex=<%=pageIndex%>" onclick="return confirm('确认删除么？')">删除</a>
								<a href="/appUser/queryAppUserInfo.do?user_id=${list.user_id }&link_num=${list.linker_num}">明细</a>
								 -->
								<a href="javaScript:void(0)" onclick="updatePassword('${list.user_id }')">修改密码</a>
								<a href="javaScript:void(0)" onclick="deleteUser('${list.user_id }')">删除</a>
								<a href="javaScript:void(0)" onclick="queryUserDetail('${list.user_id }','${list.referee_account_num}')">明细</a>
							</td>
							</tr>
							
						</c:forEach>
					</table>
					
			    	<input type="hidden" id="batchStoptype" name="batchStoptype">
			    	<input type="hidden" id="tag1" name="tag1" value="0">
					<jsp:include page="/pages/common/paging.jsp" />
					<table class="ser_mingxi" style="width: 150px; margin-top: 5px;border:0;" align="left">
						<tr style="border:0;">
						<!-- 
							<td style="border:0;">
								<input type="button" value=" 停 用 " onclick="batchStop('4');" class="btn"
										style="margin-right: 10px; font: bold 14px/ 35px '宋体'; width: 103px; height: 35px; background: url(/images/sprites.png) no-repeat 0 -458px;" />
							</td>
							<td style="border:0;">
								<input type="button" value=" 启 用 " onclick="batchStop('1');" class="btn"
										style="margin-right: 10px; font: bold 14px/ 35px '宋体'; width: 103px; height: 35px; background: url(/images/sprites.png) no-repeat 0 -458px;" />
							</td>
						 -->
							<td style="border:0;">
								<input type="button" value="停 用" onclick="batchStop('4');" class="btn btn_normal" style="font-size:13px;"	/>
							</td>
							<td style="border:0;">
								<input type="button" value="启 用" onclick="batchStop('1');" class="btn btn_normal" style="font-size:13px;"	/>
							</td>
							<!-- 
								<input type="button" value="停 用" class="btn" onclick="javascript:batchStop('4');" />
								<input type="button" value="启 用" class="btn" onclick="javascript:batchStop('1');" />
							 -->
						</tr>
					</table>
				</c:if>
				<br />
				<p></p>
			</form>
		</div>
	</body>
</html>
