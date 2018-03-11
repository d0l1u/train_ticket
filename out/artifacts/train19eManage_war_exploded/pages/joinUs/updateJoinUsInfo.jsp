<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>加盟管理页面</title>
		<link rel="stylesheet" href="/css/style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script type="text/javascript">
		function submit(){
		
		$("#updateForm").submit();
	}
		function selectCity(){
			var url = "/joinUs/queryGetCity.do?provinceid="+$("#province_id").val();
			$.get(url,function(data,status){
		    	$("#city_id").empty(); 
		    
		    	$("#city_id").append("<option value='' selected='selected'>请选择</option>");
		    	$("#district_id").empty(); 
		    	$("#district_id").append("<option value='' selected='selected'>请选择</option>");
		    	
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
		    	$("#district_id").append("<option value='' selected='selected'>请选择</option>");
		    	var obj = eval(data);
				$(obj).each(function(index){
				var val = obj[index];
				$("#district_id").append("<option value='"+val.area_no+"'>"+val.area_name+"</option>");
			});
		  });
		}
		function optionStatus(){
			var url = "/joinUs/queryGetStatus.do?user_id="+$("#user_id").val();
			$.get(url,function(data){	
				if(data=="22" && $("#estate").val()=="33" && $("#user_level").val()!= "2"){
					$("#estate_info").text("非免费用户，审核失败不能直接更改为审核通过,‘需付费!’").css("color","red");
					$("#btnModify").attr("disabled", true);
				}else if(data=="33" && $("#estate").val()=="22" && $("#user_level").val()!="2"){
					$("#estate_info").text("违法操作！审核通过不能直接更改为审核失败").css("color","red");
					$("#btnModify").attr("disabled", true);
				}else{
					$("#estate_info").text("");
					$("#btnModify").attr("disabled", false);
				}
			});
		}
		
		
	</script>
	</head>

	<body>
		<div class="content oz">
			<form action="/joinUs/updateJoinUs.do" method="post" id="updateForm" >
				<input type='hidden' name='user_id' id="user_id"
					value='${joinUpdateInfo.user_id}' />
				<input type='hidden' name='product_id'
					value='${joinUpdateInfo.product_id}' />
				<input type='hidden' name='jm_order_id'
					value='${joinUpdateInfo.jm_order_id}' />
				<input type='hidden' name='old_estate'
					value='${joinUpdateInfo.estate}' />
					
				<div class="pub_order_mes oz mb10_all">
					<h4>
						修改信息
					</h4>
					<div class="pub_con" >
						<table class="pub_table" style="margin-left:60px; ">
							<tr>
								<td class="pub_yuliu" rowspan="11"></td>
								<td width="234" colspan="2">
									代理商姓名:
									<span><input type="text" class="text" name="user_name"
											value="${joinUpdateInfo.user_name }" /> </span>
								</td>
							</tr>
							<tr>
								<td width="234">
									联系电话&nbsp; :
									<span><input type="text" class="text" name="user_phone"
											value="${joinUpdateInfo.user_phone }" /> </span>
								</td>
							</tr>
							<tr>
								<td width="234">
									店铺名称&nbsp; :
									<span><input type="text" class="text" name="shop_name"
											value="${joinUpdateInfo.shop_name }" /> </span>
								</td>
							</tr>
							<tr>
								<td width="234">
									店铺简称&nbsp; :
									<span><input type="text" class="text" name="shop_short_name"
											value="${joinUpdateInfo.shop_short_name }" /> </span>
								</td>
							</tr>
							<tr>
								<td width="234">
									店铺类型&nbsp;&nbsp;&nbsp;
									<select name="shop_type" id="shop_type" >
										<c:forEach items="${shop_types}" var="shop_type">
											<option value="${shop_type.key}"
												<c:if test='${shop_type.key eq joinUpdateInfo.shop_type}'>selected="selected"</c:if>>
												${shop_type.value}
											</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td width="234">
									配送地址&nbsp; :
									<span><input type="text" class="text"
											name="user_address" value="${joinUpdateInfo.user_address }" />
									</span>
								</td>
							</tr>
							<tr>
								<td width="234">
									代理商QQ&nbsp;&nbsp;:
									<span><input type="text" class="text" name="user_qq"
											value="${joinUpdateInfo.user_qq }" /> </span>
								</td>
							</tr>
							<tr>
								<td width="234">
									状  态&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<select name="estate" id="estate" onchange="optionStatus()">
										<c:forEach items="${estates}" var="estate">
											<option value="${estate.key}"
												<c:if test='${estate.key eq joinUpdateInfo.estate}'>selected="selected"</c:if>>
												${estate.value}
											</option>
										</c:forEach>
									</select>
								</td>
								<td><span id="estate_info"></span></td>
							</tr>
							<tr>
								<td>
									省&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<select name="province_id" id="province_id"
										onchange="selectCity();">
										<c:forEach items="${province }" var="p">
											<option value="${p.area_no }"
												<c:if test="${joinUpdateInfo.province_id eq p.area_no }">selected="selected"</c:if>>
												${p.area_name}
											</option>
										</c:forEach>
									</select>
								</td>
								</tr>
								<tr>
								<td>
									市&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<select name="city_id" id="city_id"
										onchange="selectDistrict();">
										<c:choose>
											<c:when test="${empty city }">
												<option value="" selected="selected">
													请选择
												</option>
											</c:when>
											<c:otherwise>
												<c:forEach items="${city }" var="p">
													<option value="${p.area_no }"
														<c:if test="${joinUpdateInfo.city_id eq p.area_no }">selected="selected"</c:if>>
														${p.area_name}
													</option>

												</c:forEach>
											</c:otherwise>
										</c:choose>
									</select>
								</td>
								</tr>
								<tr>
								<td>
									区&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<select name="district_id" id="district_id">
										<c:choose>
											<c:when test="${empty district}">
												<option value="" selected="selected">
													请选择
												</option>
											</c:when>
											<c:otherwise>
												<c:forEach items="${district}" var="p">
													<option value="${p.area_no }"
														<c:if test="${joinUpdateInfo.district_id eq p.area_no}">selected="selected"</c:if>>
														${p.area_name }
													</option>
												</c:forEach>
											</c:otherwise>
										</c:choose>
									</select>
								</td>
								<td><input type="hidden" id="user_level" name="user_level" value="${joinUpdateInfo.user_level}"/></td>
							</tr>
						</table>
					</div>
				</div>
			</form>

		</div>
		<div class="pub_debook_mes  oz mb10_all">
			<p>
				<input type="button" value="修改" id="btnModify" class="btn btn_normal"
					onclick="submit()" />
				<input type="button" value="返 回" class="btn btn_normal"
					onclick="javascript:history.back(-1);" />
			</p>
		</div>
	</body>
</html>
