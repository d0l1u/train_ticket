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
		<!-- <script language="javascript" src="/js/datepicker/WdatePicker.js"></script>-->


		<script type="text/javascript">
	function selectCity(){
		var url = "/appComplain/queryGetCity.do?provinceid="+$("#province_id").val();
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
</script>
		<style>
td {
	padding: 5px 0;
}

.tit {
	background: #ddd;
}

.book_manage .ser_mingxi {
	width: 800px;
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
		<div class="book_manage oz">
			<form action="/appComplain/queryComplainList.do" method="post">
					<ul class="ser oz">
						<li>
							状态：
						</li>
						<li>
							<c:forEach items="${questionType }" var="str" varStatus="index">
								<li>
									<input type="checkbox" name="question_type_List"
										id="question_type_List${index.count }" value="${str.key }"
										<c:if test="${fn:contains(question_typeStr, str.key ) }">checked="checked"</c:if> />
									<label for="question_type_List${index.count }">
										${str.value }
									</label>
								</li>
							</c:forEach>
						</li>
					</ul>

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
								<!-- 
								<td>
									加盟问题:
									<span>${joinUs_question }</span>条
								</td>
								<td>
									配送问题:
									<span>${remand_question }</span>条
								</td>
								 -->
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
									用户账号
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
								<c:choose>
									<c:when test="${fn:contains('1', list.permission )}">
										<tr style="background: #ffecf5;">
									</c:when>
									<c:otherwise>
										<tr>
									</c:otherwise>
								</c:choose>
								<td>
									${idx.index+1}
								</td>
								<td>
									${list.user_id }
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
									<span><a
										href="/appComplain/queryComplainInfo.do?complain_id=${list.complain_id}">明细/修改</a>
									</span>
									<span><a
										href="/appComplain/deleteComplain.do?complain_id=${list.complain_id}"
										onclick="return confirm('确认删除么？')">删除</a>
									</span>
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
