<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>加盟管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	    <script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
		<script type="text/javascript">
		
		function selectAllagent_grade(){
			var checklist = document.getElementsByName("agent_grade");
			if(document.getElementById("controlAllagent_grade").checked){
				for(var i=0; i<checklist.length; i++){
					checklist[i].checked = 1;
				}
			}else{
				for(var j=0; j<checklist.length; j++){
					checklist[j].checked = 0;
				}
			}
		}
		function selectCity(){
		var url = "/joinUs/queryGetCity.do?provinceid="+$("#province_id").val();
		$.get(url,function(data,status){
	    	$("#city_id").empty(); 
	    	$("#city_id").append("<option value='' selected='selected'>全部</option>");
	    	$("#district_id").empty(); 
	    	$("#district_id").append("<option value='' selected='selected'>全部</option>");
	    	var obj = eval(data);
			$(obj).each(function(index){
			var val = obj[index];
			$("#city_id").append("<option value='"+val.area_no+"'>"+val.area_name+"</option>");
		});
	  });
	}
	function selectDistrict(){
		var url = "/joinUs/queryGetArea.do?cityid="+$("#city_id").val();
		$.get(url,function(data,status){
	    	$("#district_id").empty(); 
	    	$("#district_id").append("<option value='' selected='selected'>全部</option>");
	    	var obj = eval(data);
			$(obj).each(function(index){
			var val = obj[index];
			$("#district_id").append("<option value='"+val.area_no+"'>"+val.area_name+"</option>");
		});
	  });
	}
	
	function submitBatchPass(){
		$("form:first").attr("action", "/joinUs/updateEstatePass.do");
			if(confirm("确认批量审核通过 ？")){
				$("form:first").submit();
			}
	}
	function submitBatchNot(){
		$("form:first").attr("action", "/joinUs/updateEstateNot.do");
			if(confirm("确认批量审核未通过 ？")){
				$("form:first").submit();
			}
	}
	function unselectall(){
		if(document.myform.chkAll.checked){
			document.myform.chkAll.checked = document.myform.chkAll.checked&0;
		}
	}
	
	function CheckAll(obj){
		if($(obj).attr("checked")=="checked"){
			$(":checkbox.checkbox_list").attr("checked",true);
		}else{
			$(":checkbox.checkbox_list").attr("checked",false);
		}
	}

	//单选 
	function CheckOne(cb){
		var obj = document.getElementsByName("order_num");
		for(i=0;i<obj.length;i++){
			if(obj[i]!=cb) {
				obj[i].checked = false;
			}
			else{
				obj[i].checked = true;
			}
		}
	}
	
	//全选
	function selectAll(){
		var checklist = document.getElementsByName("shop_type");
		if(document.getElementById("controlAll").checked){
			for(var i=0; i<checklist.length; i++){
				checklist[i].checked = 1;
			}
		}else{
			for(var j=0; j<checklist.length; j++){
				checklist[j].checked = 0;
			}

		}
	}
	
</script>
		<style>
td {
	padding: 5px 0;
}

.tit {
	background: #ddd;
}

.book_manage .ser_mingxi {
	width: 1000px;
	margin: 0 auto;
	border: none;
}

.book_manage .ser_mingxi tr,.book_manage .ser_mingxi td {
	border: none;
	line-height:15px;
}

.book_manage .ser_mingxi span {
	color: #f00;
}
.book_manage td {
	line-height:15px;
}
</style>
	</head>

	<body><div></div>
		<div class="book_manage oz">
			<form action="/joinUs/queryJoinUsList.do" method="post" name="myform" id="myform">
				<div style="border: 0px solid #00CC00; margin: 0px;">
					<ul class="order_num oz" style="margin-top: 10px;">
						<li>省
							<select name="province_id" id="province_id" onchange="selectCity();" style="width: 120px;">
								<c:forEach items="${province }" var="p">
									<option value="${p.area_no }"
										<c:if test="${province_id eq p.area_no }">selected="selected"</c:if>>
											${p.area_name}
									</option>
								</c:forEach>
							</select>
						</li>
						<li>市
							<select name="city_id" id="city_id" onchange="selectDistrict();" style="width: 158px;">
								<c:choose>
									<c:when test="${empty city }">
										<option value="" selected="selected">全部</option>
									</c:when>
									<c:otherwise>
										<c:if test="${empty city_id}">
											<option value="" selected="selected">全部</option>
										</c:if>
										<c:forEach items="${city }" var="p">
											<option value="${p.area_no }"
												<c:if test="${city_id eq p.area_no }">selected="selected"</c:if>>
													${p.area_name}
											</option>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</select>
						</li>
						<!--	<li>区
							<select name="district_id" id="district_id" style="width: 158px;">
								<c:choose>
									<c:when test="${empty district}">
										<option value="" selected="selected">全部</option>
									</c:when>
									<c:otherwise>
										<c:if test="${empty district_id}">
											<option value="" selected="selected">全部</option>
										</c:if>
										<c:forEach items="${district}" var="p">
											<option value="${p.area_no }"
												<c:if test="${district_id eq p.area_no}">selected="selected"</c:if>>
													${p.area_name }
											</option>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</select>
						</li>-->
						<!-- <li>订购时间-->
							<!-- <input type="text" class="text" name="beginInfo_time" value="${beginInfo_time }"/> -->
						<!-- 	<input type="text" class="text" name="begin_pay_time" readonly="readonly" value="${begin_pay_time}"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" style="width: 80px;"/> 
								至
							<input type="text" class="text" name="end_pay_time" readonly="readonly" value="${end_pay_time}"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" style="width: 80px;"/> 
						</li>
						<li>审核时间
							<input type="text" class="text" name="begin_auditing_time" readonly="readonly" value="${begin_auditing_time}"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" style="width: 80px;"/> 
								至
							<input type="text" class="text" name="end_auditing_time" readonly="readonly" value="${end_auditing_time}"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" style="width: 80px;"/> 
						</li>-->
					</ul>
							<!-- 
							<ul class="order_num oz" style="margin-top: 10px;">
								<li>
									开始时间
									//<input type="text" class="text" name="beginInfo_time" value="${beginInfo_time }"/>
									 
									<input type="text" class="text" name="beginInfo_time" readonly="readonly" value="${beginInfo_time }"
										onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})"/> 
								</li>
								<li>
									结束时间
										<input type="text" class="text" name="endInfo_time" readonly="readonly" value="${endInfo_time}"
										onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" /> 
								</li>
								 -->
								<!-- 
								<li>
									<span><input type="radio" name="queryType" value="00"/>按申请时间查询</span>
									<span><input type="radio" name="queryType" value="11"/>按最后订购时间查询</span>
								</li>
								 -->
					<ul  class="order_num oz" style="margin-top: 10px;">
						<li>姓&nbsp;&nbsp;&nbsp;&nbsp;名
							<input type="text" class="text" name="user_name" value="${user_name }" />
						</li>
						<li>联系电话
							<input type="text" class="text" name="user_phone" value="${user_phone }" />
						</li>
					</ul>
					<ul  class="order_num oz" style="margin-top: 10px;">
						<li>上月订单 &nbsp;&nbsp;</li>
						<li>
						<input type="checkbox" id="order_num" name="order_num" value="0-100000000" onclick="CheckOne(this);"
							<c:if test="${!empty order_num && order_num eq '0-100000000'}">checked="checked"</c:if> />全部
							</li><li>
						<input type="checkbox" id="order_num" name="order_num" value="0-0" onclick="CheckOne(this);"
							<c:if test="${!empty order_num && order_num eq '0-0'}">checked="checked"</c:if> />0个
							</li><li>
						<input type="checkbox" id="order_num" name="order_num" value="1-19" onclick="CheckOne(this);"
							<c:if test="${!empty order_num && order_num eq '1-19'}">checked="checked"</c:if> />1-19个
							</li><li>
						<input type="checkbox" id="order_num" name="order_num" value="20-49" onclick="CheckOne(this);"
							<c:if test="${!empty order_num && order_num eq '20-49'}">checked="checked"</c:if> />20-49个
							</li><li>
						<input type="checkbox" id="order_num" name="order_num" value="50-99" onclick="CheckOne(this);"
							<c:if test="${!empty order_num && order_num eq '50-99'}">checked="checked"</c:if> />50-99个
							</li><li>
						<input type="checkbox" id="order_num" name="order_num" value="100-100000000" onclick="CheckOne(this);"
							<c:if test="${!empty order_num && order_num eq '100-100000000'}">checked="checked"</c:if> />100个以上
							</li>
					</ul>
					<ul  class="order_num oz" style="margin-top: 10px;">
						<li>审核状态 &nbsp;&nbsp; </li><li></li>
						<c:forEach items="${estates }" var="s" varStatus="index">
							<input type="checkbox" id="estate${index.count }" name="estate" value="${s.key }" 
								<c:if test="${fn:contains(estateStr, s.key ) }">checked="checked"</c:if> />
							<label for="estate${index.count }">${s.value }</label>
						</c:forEach>
					</ul>
					<ul  class="order_num oz" style="margin-top: 10px;">
						<li>店铺类型 &nbsp;&nbsp;</li>
						<li>
						<input type="checkbox" onclick="selectAll()" name="controlAll" style="controlAll" id="controlAll"/><label for="">&nbsp;全部&nbsp;&nbsp;</label>
						</li>
						<c:forEach items="${shop_type}" var="s" varStatus="index">
							<input type="checkbox" id="shop_type${index.count }" name="shop_type" value="${s.key }" value="1"
								<c:forEach items="${shopTypeList}" var="shopTypeItem">
									<c:if test="${shopTypeItem eq s.key}">
										checked="checked"
									</c:if>
								</c:forEach>
							/>
							<label for="shop_type${index.count }">${s.value }</label>
						</c:forEach>
					</ul>
					
					<ul  class="order_num oz" style="margin-top: 10px;">
						<li>代理商等级</li>
						<li>
						<input type="checkbox" onclick="selectAllagent_grade()" name="controlAllagent_grade" style="controlAllagent_grade" id="controlAllagent_grade"/><label for="">&nbsp;全部&nbsp;&nbsp;</label>
						</li>
						<c:forEach items="${agent_grade}" var="s" varStatus="index">
							<input type="checkbox" id="agent_grade${index.count }" name="agent_grade" value="${s.key }" value="1"
							<c:if test="${fn:contains(agent_gradeStr, s.key ) }">checked="checked"</c:if> />
							<label for="agent_grade${index.count }">${s.value }</label>
						</c:forEach>
					</ul>
					

					<!-- 
					<ul  class="order_num oz" style="margin-top: 10px;">
						<li>用户等级 &nbsp;&nbsp;</li>
						<c:forEach items="${user_level }" var="s" varStatus="index">
							<li>
								<input type="checkbox" id="user_level${index.count }" name="user_level" value="${s.key }"
									<c:if test="${fn:contains(user_levelStr, s.key ) }">checked="checked"</c:if> />
								<label for="user_level${index.count }">${s.value }</label>
							</li>
						</c:forEach>
					</ul>
					 -->
					<br />
					<p><input type="submit" value="查 询" class="btn" /></p>
					<c:if test="${!empty isShowList}">
						<table class="ser_mingxi" style="width: 800px; margin: 0 auto;">
							<tr>
								<td>
									全部:
									<span>${total }</span>条
								</td>
								<!-- 
								<td>
									需要付费:
									<span>${needpay }</span>条
								</td>
								 -->
								<td>
									等待审核:
									<span>${wait }</span>条
								</td>
								<td>
									未通过:
									<span>${doesnot }</span>条
								</td>
								<td>
									已通过:
									<span>${pass }</span>条
								</td>
								<!-- 
								<td>
									需要续费:
									<span>${repay }</span>条
								</td>
								 -->
								 <td>共
								 	<span>${totalCount}</span>条查询结果
								 </td>
							</tr>
						</table>
						<table>
							<tr style="background: #f0f0f0;">
								<th style="width:40px;">全选 <br/>
									<input name='chkAll' type='checkbox' id='chkAll' onclick='CheckAll(this)' value='checkbox'/>
								</th>
								<th>
									NO
								</th>
								<th width="60">
									省
								</th>
								<th width="70">
									市
								</th>
								<!--	<th width="70">
									区
								</th>
								<th width="70">
									店铺类型
								</th>
								<th width="60" style="text-align:center;">
									店铺名称
								</th>-->
								<th>
									等级
								</th>
								<th width="50">
									姓名
								</th>
								<th width="70">电话</th>
								<!--	<th width="70">申请时间</th> -->
								<!-- 
								<th width="60">
									订购时间
								</th>
								 -->
							<!--		<th width="70">审核时间</th>-->
								<th width="55">状态</th>
								<th width="70">最后订购</th>
								<!-- 
								<th width="70">用户等级</th>
								 -->
								<th width="60">上月订单</th>
								<th width="60">本月订单</th>
								<!-- 
								<th>
									上月总计
								</th>
								<th>
									本月总计
								</th>
								<th width="55">
									级别
								</th>
								 -->
								<th width="60">当月SVIP</th>
								<th width="60">当月VIP</th>
								<th width="60">上月SVIP</th>
								<th width="60">上月VIP</th>
								<th width="60">
									操作人
								</th>
								<th>
									操作
								</th>
							</tr>
							<c:forEach var="list" items="${joinUsList}" varStatus="idx">
								<tr 
									<c:if test="${fn:contains('11', list.estate )}">
										style="background: #ffecf5;"
									</c:if>
									<c:if test="${fn:contains('22',list.estate  )}">
										style="background: #e0e0e0;"
									</c:if>
								>
								<td>
									<input type="checkbox" class="checkbox_list" name="change" onclick='unselectall()' value="${list.user_id}" />
								</td>
								<td>
									${idx.index+1}
								</td>
								<td>
									<c:forEach items="${idAndName }" var="t">
	            						<c:if test="${t.key eq list.province_id }">${t.value}</c:if>
	            					</c:forEach>
								</td>
								<td>
									<c:forEach items="${idAndName }" var="t">
	            						<c:if test="${t.key eq list.city_id }">${t.value}</c:if>
	            					</c:forEach>
								</td>
					<!-- 			<td>
									<c:forEach items="${idAndName }" var="t">
	            						<c:if test="${t.key eq list.district_id }">${t.value}</c:if>
	            					</c:forEach>
								</td>
								<td>
									${shop_type[list.shop_type] }
								</td>
								<td>
									${list.shop_name }
								</td>-->	
								<td>
									<!-- <c:if test="${list.user_grade eq  null}">
										
											未认证
										</c:if>
									${list.user_grade } -->	
									${agent_grade[list.agent_grade] }
								</td>
								<td>
									${list.user_name }
								</td>
								<td>
									${list.user_phone }
								</td>
							<!--	<td>
									${fn:substringBefore(list.apply_time, ' ')}
									<br />
									${fn:substringAfter(list.apply_time, ' ')}
								</td>
								<td>
									${fn:substringBefore(list.auditing_time, ' ')}
									<br />
									${fn:substringAfter(list.auditing_time, ' ')}
								</td>-->	
								<td>
									${estates[list.estate] }
								</td>
								<td>
									${fn:substringBefore(list.create_timeInfo, ' ')}
									<br />
									${fn:substringAfter(list.create_timeInfo, ' ')}
								</td>
								<!--  
								<td>
									${list.pre_month_total }
								</td>
								<td>
									${list.this_month_total}
								</td>
								<td>
									${list.pay_money }
								</td>
								
								<td>
									<c:if test="${list.user_level eq '0'}">
										<span>${level[list.user_level] }</span>
									</c:if>
									<c:if test="${list.user_level eq '1'}">
										<span><font color="#ff0000"><strong>${level[list.user_level]
													}</strong> </font> </span>
									</c:if>
									<c:if test="${list.user_level eq '2'}">
										<span>${level[list.user_level] }</span>
									</c:if>
								</td>
								
								<td>
									<c:if test="${list.user_level eq '0'}">
										<span>${level[list.user_level] }</span>
									</c:if>
									<c:if test="${list.user_level eq '1'}">
										<span><font color="#ff0000"><strong>${level[list.user_level]
													}</strong> </font> </span>
									</c:if>
									<c:if test="${list.user_level eq '2'}">
										<span>${level[list.user_level] }</span>
									</c:if>
								</td>
								-->
								<!-- 
								<td>
									${list.pre_month_total }
								</td>
								<td>
									${list.this_month_total}
								</td>
								 -->
								<td>
									${list.pay_pre_count }
								</td>
								<td>
									${list.pay_now_count }
								</td>
								
								<td>
									0
								</td>
								<td>
									0
								</td>
								<td>
									0
								</td>
								<td>
									0
								</td>
								
								<td>
									${list.opt_person }
								</td>
								<td>
									
									<span>
									<a href="/joinUs/queryUpdateJoinUsInfo.do?user_id=${list.user_id }">修改</a>
									</span>
									<span>
									<a href="/joinUs/deleteJoinUs.do?user_id=${list.user_id }"
										onclick="return confirm('确认删除么？')">删除</a> 
									</span>
									<br />
									<span >
									<a href="/joinUs/queryJoinUsDetail.do?user_id=${list.user_id }">明细</a>
									</span>
									<!-- <span><input type="hidden" name="user_id" value="${list.user_id }" id="user_id"/></span>  -->
								</td>
							</tr>
							</c:forEach>
						</table>
						
						<jsp:include page="/pages/common/paging.jsp" />
						<table class="ser_mingxi" style="width: 250px; margin-left: 0px; margin-top: 10px;" align="left">
							<tr>
								<!-- 
									<td>
										<span>全选</span><input name='chkAll' type='checkbox' id='chkAll' onclick='CheckAll(this)' value='checkbox'/>
									</td>
								 -->
								<td>
									<input type="button" value="审核通过" onclick="submitBatchPass();" class="btn"
											style="margin-right: 10px; font: bold 14px/ 35px '宋体'; width: 103px; height: 35px; background: url(/images/sprites.png) no-repeat 0 -458px;" />
								</td>
								<td>
									<input type="button" value="审核未通过" onclick="submitBatchNot();"
										class="btn"
										style="margin-right: 10px; font: bold 14px/ 35px '宋体';color:#ff0; width: 103px; height: 35px; background: url(/images/sprites.png) no-repeat 0 -458px;" />
								</td>
							</tr>
						</table>
					</c:if>
				</div>
			</form>
		</div>
	</body>
</html>
