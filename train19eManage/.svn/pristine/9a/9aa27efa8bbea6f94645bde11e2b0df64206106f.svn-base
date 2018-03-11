<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>系统管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript">

	function submitForm(url){
		if(confirm("确认删除吗？")){
			location.href = url; 
		}
	}
	
	function changeChannel(){
		if(confirm("确认切换吗？")){
			$("form:first").attr("action", "/system/updateSetting.do");
			$("form:first").submit();
		}
	}
	
	function CheckAll(obj){
		if($(obj).attr("checked")=="checked"){
			$(":checkbox.changeIsBuy_class").attr("checked",true);
		}else{
			$(":checkbox.changeIsBuy_class").attr("checked",false);
		}
	}
	
	function submitBatchIsBuy_True(){
		$("form:first").attr("action", "/system/updateIsBuy_True.do");
			if(confirm("确认批量可以购买 ？")){
				$("form:first").submit();
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
			line-height: 30px;
		}
		
		.book_manage .ser_mingxi span {
			color: #f00;
		}
	</style>
	</head>

	<body>
		<div  class="book_manage oz">
			<form action="/system/querySystemList.do" method="post">
				<ul class="ser oz">
					<li> 
						省 
						<select name="province_id" id="at_province_id">
							<c:forEach items="${province }" var="p">
								<option value="${p.area_no }"
									<c:if test="${province_id eq p.area_no }">selected="selected"</c:if>>
									${p.area_name}
								</option>
							</c:forEach>
						</select>
					</li>
					<!-- 
					<li>
						<c:forEach items="${setting }" var="s" varStatus="index">
							<li>
								<input type="radio" id="setting_value${index.count }"
									name="setting_value" value="${s.key }"
									<c:if test="${fn:contains(setting_value, s.key ) }">checked="checked"</c:if> />
								<label for="setting_value${index.count }">
									${s.value }
								</label>
							</li>
						</c:forEach>
					</li>
					<span>
						<input type="button" value="切换"onclick="changeChannel();" />
					</span>
					 -->
				</ul>
				<br />
				<p>
					<input type="submit" value="查 询" class="btn" />
					&nbsp;&nbsp;&nbsp;
					<input type="button" value="添加"
						onclick="location.href = '/system/addPreSystem.do'" class="btn" />
				</p>
				<c:if test="${!empty isShowList}">
					<table id="table_list">
						<tr style="background: #EAEAEA;">
							<th>
								序号
							</th>

							<th>
								省份
							</th>
							<th>
								开通
							</th>
							<th>
								付费
							</th>
							<th>
								配送
							</th>
							<th>
								购买
							</th>
							<th>
								创建时间
							</th>
							<th>
								修改时间
							</th>
							<th>
								操作人
							</th>
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${systemList}" varStatus="idx">
							<tr>
								<td>
									<c:choose>
										<c:when test="${list.is_buyable eq '00'}">
											${idx.index+1}
											<input type="checkbox" name="changeIsBuyTrue" class="changeIsBuy_class" id="changeIsBuy_id" value="${list.config_id}"/>
										</c:when>
										<c:otherwise>
											${idx.index+1}
											<input type="checkbox" disabled="disabled" />
										</c:otherwise>
									</c:choose>
								</td>
								<td>
									${list.area_name}
								</td>
								<td>
									${isopen[list.is_open]}
								</td>
								<td>
									${iscost[list.is_cost]}
								</td>
								<td>
									${isps[list.is_ps]}
								</td>
								<td>
									${isbuy[list.is_buyable]}
								</td>
								<td>
									${list.create_time}
								</td>
								<td>
									${list.modify_time}
								</td>
								<td>
									${list.opt_ren }
								</td>
								<td>
									<span> <a
										href="javascript:submitForm('/system/deleteSystem.do?config_id=${list.config_id }')">删除</a>
										<a
										href="/system/updatePreSystem.do?config_id=${list.config_id }">修改</a>
									</span>
								</td>
							</tr>
						</c:forEach>
					</table>
					<script type="text/javascript">
						var cobj=document.getElementById("table_list").rows;
   						for (i=1;i< cobj.length ;i++) {
     					(i%2==0)?(cobj[i].style.background = "#ECFFFF"):(cobj[i].style.background = "#FFFFFF");
   						 }
					</script>
					<jsp:include page="/pages/common/paging.jsp" />
					<table class="ser_mingxi"style="width: 320px; margin-left: 50px; margin-top: 10px;" align="left">
						<tr>
							<td>
								<span>全选</span><input name='chkAll' type='checkbox' id='chkAll' onclick='CheckAll(this)' value='checkbox'/>
							</td>
							<td>
								<input type="button" value="可以购买" onclick="submitBatchIsBuy_True();" class="btn"
									style="margin-right: 10px; font: bold 14px/ 35px '宋体'; width: 103px; height: 35px; background: url(/images/sprites.png) no-repeat 0 -458px;" />
							</td>
						</tr>
					</table>
				</c:if>
				<br />
			</form>
		</div>
	</body>
</html>
