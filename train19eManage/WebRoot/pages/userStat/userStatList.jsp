<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>用户统计页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	    <script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
  	</head>
  	<body><div></div>
    	<div class="book_manage oz">
			<form action="/userStat/queryUserStatList.do" method="post" name="myform" id="myform">
				<table style="border:none;">
					<tr style="border:none;">
						<td style="border:none;">
							<ul class="ser oz">
								<li>&nbsp;&nbsp;
									地区 
									<select name="province_id" id="province_id"
										 style="width: 160px;">
										 <option value="-1">请选择</option>
										<c:forEach items="${province }" var="p">
											<option value="${p.area_no }"
												<c:if test="${provinceStr eq p.area_no }">selected="selected"</c:if>>
												${p.area_name}
											</option>
										</c:forEach>
									</select>
								</li>
							</ul>
						<ul class="oz" style="margin-top: 14px;">
								<li>
									开始时间
									 
									<input type="text" class="text" name="begin_time" readonly="readonly" value="${begin_time }"
										onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})"/> 
								</li>
								<li>
									结束时间
									<input type="text" class="text" name="end_time" readonly="readonly" value="${end_time}"
									onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" /> 
								</li>
								<li>
									<a href="/userStat/showUserStatPictureLine.do">用户增长趋势图</a>
									<a href="/userStat/showAllPrivateUserStatBar.do">各省用户对比图</a>
								</li>
							</ul>
						</td>
					</tr>
				</table>
				<br />
				<br />
				<p>
					<input type="submit" value="查 询" class="btn" />
				</p>
						

				<c:if test="${!empty isShowList}">
					<table id="table_list">
						<tr style="background: #f0f0f0;">
							<th>
								序号
							</th>
							<th>
								统计时间
							</th>
							<th>
								地区名称
							</th>
							<th>
								用户总数
							</th>
							<th>
								用户增长数
							</th>
							<th>
								用户增长率
							</th>
							<!-- 
							<th>
								活跃用户数
							</th>
							<th>
								总活跃用户数
							</th>
							<th>
								活跃用户增长数
							</th>
							<th>
								活跃用户增长率
							</th>
							 -->
							<th width="160">
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${userStat_List}" varStatus="idx">
							<tr>
								<td>
									${idx.index+1}
								</td>
								<td>
									${list.tj_time }
								</td>
								<td>
									${list.area_name }
								</td>
								<td>
									${list.user_total }
								</td>
								<td>
									${list.user_increase }
								</td>
								<td>
									${list.user_rate_increase }
								</td>
								<!-- 
								<td>
									${list.active_user }
								</td>
								<td>
									${list.active_user_total }
								</td>
								<td>
									${list.active_increase }
								</td>
								<td>
									${list.active_rate_increase }
								</td>
								-->
								<td>
									<a href="/userStat/showUserStatPictureInfo.do?tj_time=${list.tj_time }&area_no=${list.area_no }">本省趋势图</a>
									<!-- 
									<a href="/userStat/showUserStatPictureActive.do?tj_time=${list.tj_time }&area_no=${list.area_no }">活跃用户趋势</a>
									 -->
								</td>
							</tr>
						</c:forEach>
					</table>
					<script type="text/javascript">
						var cobj=document.getElementById("table_list").rows;
   						for (i=1;i< cobj.length ;i++) {
     					(i%2==0)?(cobj[i].style.background = "#FFEEDD"):(cobj[i].style.background = "#FFFFFF");
   						 }
					</script>
					<jsp:include page="/pages/common/paging.jsp" />
				</c:if>
			</form>
		</div>
  	</body>
</html>
