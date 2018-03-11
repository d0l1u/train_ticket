<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>投诉建议管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>


		<script type="text/javascript">
	function selectCity(){
		var url = "/complain/queryGetCity.do?provinceid="+$("#province_id").val();
		$.get(url,function(data,status){
	    	$("#city_id").empty(); 
	    	$("#district_id").empty(); 
	    
	    	$("#city_id").append("<option value='' selected='selected'>请选择</option>");
	    	$("#district_id").append("<option value='' selected='selected'>请选择</option>");
	    	var obj = eval(data);
			$(obj).each(function(index){
			var val = obj[index];
			$("#city_id").append("<option value='"+val.area_no+"'>"+val.area_name+"</option>");
			
			
		});
	  });
	}
	
	// function selectDistrict(){
		// var url = "/complain/queryGetArea.do?cityid="+$("#city_id").val();
		// $.get(url,function(data,status){
	    	// $("#district_id").empty(); 
	    	
	    	// $("#district_id").append("<option value='' selected='selected'>请选择</option>");
	    	// var obj = eval(data);
			// $(obj).each(function(index){
			// var val = obj[index];
			// $("#district_id").append("<option value='"+val.area_no+"'>"+val.area_name+"</option>");
		// });
	  // });
	// }
	
	//投诉建议锁 
	function gotoOrderLock(complain_id){
		//ajax验证是否锁
		var url = "/complain/queryComplainIsLock.do?complain_id="+complain_id+"&version="+new Date();
			$.get(url,function(data){
			if(data != null && data != ""){
				var temp = data ;
				var str1 = temp.split("&") ;
				var str =str1[1]; 
				alert("此投诉建议已经锁定，锁定人为"+str);
				return;
			 }else{
				$("form:first").attr("action","/complain/queryComplainInfo.do?complain_id="+complain_id);
				$("form:first").submit();
				//url="/complain/queryComplainInfo.do?complain_id="+complain_id;
			 }
		});
	}
	</script>
	<style>
		td {padding: 5px 0;}
		.tit {background: #ddd;}
		.book_manage .ser_mingxi {width: 800px;margin: 0 auto;border: none;}
		.book_manage .ser_mingxi tr,.book_manage .ser_mingxi td {border: none;line-height: 30px;}
		.book_manage .ser_mingxi span {color: #f00;}
	</style>
	</head>
	<body>
		<div class="book_manage oz">
			<form action="/complain/queryComplainList.do" method="post">
				<div style="border: 0px solid #00CC00; margin: 0px;">
					<ul class="order_num oz" style="margin-top: 10px;">
						<li>
							省：
							<select name="province_id" id="province_id"
								onchange="selectCity();" style="width:170px;">
								<c:forEach items="${province }" var="p">
									<option value="${p.area_no }"
										<c:if test="${province_id eq p.area_no }">selected="selected"</c:if>>
										${p.area_name}
									</option>

								</c:forEach>

							</select>
						</li>
						<li>
							市：
							<select name="city_id" id="city_id" style="width:170px;">
								<c:choose>
									<c:when test="${empty city }">
										<option value="" selected="selected">
											请选择
										</option>
									</c:when>
									<c:otherwise>
										<c:if test="${empty city_id}">
											<option value="" selected="selected">
											请选择
											</option>
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
<!-- 
						<li>
							区
							<select name="district_id" id="district_id" style="width:160px;">
								<c:choose>
									<c:when test="${empty district}">
										<option value="" selected="selected">
											请选择
										</option>
									</c:when>
									<c:otherwise>
										<c:if test="${empty district_id}">
											<option value="" selected="selected">
											请选择
											</option>
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
						</li>
						 -->
					<li>
						代理商账号：
						<input type="text" class="text" name="user_phone" value="${user_phone }"/>
					</li>
				</ul>
			<ul class="ser oz">
	        	<li>状态：</li>
	            <li>
		            <c:forEach items="${questionType }" var="str" varStatus="index">
		        		<li><input type="checkbox" name="question_type_List" id="question_type_List${index.count }" 
		        		value="${str.key }"
		        		<c:if test="${fn:contains(question_typeStr, str.key ) }">checked="checked"</c:if>/>
		        		<label for="question_type_List${index.count }">${str.value }</label></li>
		        	</c:forEach>
	            </li>
     	   </ul>
				</div>
			
				<p>
					<input type="submit" value="查 询" class="btn" />
				</p>
				<c:if test="${!empty isShowList}">
					<table class="ser_mingxi" style="width: 1000px; margin: 0 auto;">
						<tr>

							<td>
								全部:
								<span>${count }</span>条
							</td>
							<td>
								已经解答:
								<span>${answerCount }</span>条
							</td>
							<td>
								自己可见:
								<span>${selfLook }</span>条
							</td>
							<td>
								全部可见:
								<span>${allLook }</span>条
							</td>
							<td>
								订单问题:
								<span>${order_question }</span>条
							</td>
							<td>
								加盟问题:
								<span>${joinUs_question }</span>条
							</td>
							<td>
								配送问题:
								<span>${remand_question }</span>条
							</td>
							<td>
								出票问题:
								<span>${acquire_question }</span>条
							</td>
							<td>
								业务建议:
								<span>${operation_advice }</span>条
							</td>
							<td>
								其他：
								<span>${other_advice }</span>条
							</td>
						</tr>
					</table>
					<table>
						<tr style="background: #f0f0f0;">
							<th>
								序号
							</th>
							<th>
								省
							</th>
							<th>
								市
							</th>
							<!-- 
							<th>
								区
							</th>
							 -->
							<th>
								代理商
							</th>
							<th>
								代理商账号
							</th>
							<th>
								EOP
							</th>
							<th>
								问题类型
							</th>
							<th>
								可见范围
							</th>
							<th>
								提问时间
							</th>
							<th>
								解决时间
							</th>
							<th>
								操作人
							</th>
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${complainList}" varStatus="idx">
						<<tr 
								<c:if test="${fn:contains('1', list.permission )}">
									style="background: #ffecf5;"
								</c:if>
								>
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
								<!-- 
								<td>
									${list.DISTRICT_NAME }
								</td>
								 -->
								<td>
									${list.user_name }
								</td>
								<td>
									${list.user_phone }
								</td>
								<td>
									${list.eop_user }
								</td>
								<td>
									${questionType[list.question_type] }
								</td>
								<td>
									${purview[list.permission] }
								</td>
								<td>
									${list.create_time }
								</td>
								<td>
									${list.reply_time}
								</td>
								<td>
									${list.opt_person }
								</td>
								<td>
									<span>
									<!-- 
									<a href="/complain/queryComplainInfo.do?complain_id=${list.complain_id}">明细/修改</a>
									 -->
									<a href="javascript:gotoOrderLock('${list.complain_id}');">明细/修改</a>
									<a href="/complain/deleteComplain.do?complain_id=${list.complain_id}" 
										onclick="return confirm('确认删除么？')">删除</a></span>
									<!-- </span><span><a href="#">操作记录</a> </span> -->
								</td>
							</tr>
						</c:forEach>
					</table>
					<jsp:include page="/pages/common/paging.jsp" />
				</c:if>

			</form>
		</div>
	</body>
</html>
