<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>管理员首页</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	</head>
	<script type="text/javascript">
	 	function submitForm(){
	    	$("#myform").submit();
		}

	 	$(function () {  
	        odd_even();  
	        $(".nlist_1s").each(function () {  
	            var _color = $(this).css("backgroundColor");  
	            $(this).hover(function () {  
	                if (parseInt($(this).attr("class").indexOf("checked")) == -1) {  
	                    $(this).css({ "backgroundColor": "#cccccc", "color": "black" });  
	                }  
	            }, function () {  
	                if (parseInt($(this).attr("class").indexOf("checked")) == -1) {  
	                    $(this).css({ "backgroundColor": _color, "color": "#535353" });  
	                }  
	            });  
	            /**
	            $(this).click(function () {  
	                //所有行  
	                odd_even();  
	                //当前行  
	                $(this).css({ "backgroundColor": "#20B2AA", "color": "#ffffff" }).addClass("checked");  
	            });  
	            **/
	        });  
	    });  
	    function odd_even() {  
	        //偶数行 第一行为偶数0行  
	        $(".nlist_1s:odd").css({ "backgroundColor": "#FFFFF0", "color": "#535353" });  
	        //奇数行  
	        $(".nlist_1s:even").css({ "backgroundColor": "#E0F3ED", "color": "#535353" });  
	    }  	
	</script>
	<body>
		<div style="width:expression(document.body.clientWidth + 'px')">
		<div class="book_manage oz">
			<form action="/loginManager/queryAdminPage.do" method="post" name="myform" id="myform">
				<div style="border: 0px solid #00CC00; margin: 15px;">
				<ul class="order_num oz">
					<li>所在部门：&nbsp;&nbsp;&nbsp;&nbsp;
						<select name="department" style="width: 120px;">
							<c:forEach items="${userDepartment }" var="t">
           						<option value="${t.key}" <c:if test="${!empty department && department eq t.key }">selected</c:if>>${t.value}</option>
           					</c:forEach>
		       			</select>
					</li>
					<li>
						<input type="button" value="查 询" id="btnSubmit" class="btn btn_normal" style="font-size:13px;" onclick="submitForm()" />
					</li>
				</ul>
				</div>
					<table>
						<tr style="background: #f0f0f0;height:60px;">
							<th>
								<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">
									${userDepartment[department] }
								</span>
							</th>
							<th>
								打码总数
							</th>
							<th>
								打码正确
							</th>
							<th>
								打码错误
							</th>
							<th>
								打码超时
							</th>
							<th>
								打码金额
							</th>
							<th>
								截止时间
							</th>
						</tr>
						<tr style="height:60px; " class="nlist_1s">
							<td>
								当天打码
							</td>
							<td>
								${codeCountToday}
							</td>	
							<td>
								${codeSuccessToday }
							</td>
							<td>
								${codeFailToday }
							</td>
							<td>
								${codeOvertimeToday }
							</td>
							<td>
								￥<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${codeSuccessToday*6/100 }</span>元
							</td>
							<td>
								${newDate }
							</td>
						</tr>
						<tr style="height:60px; " class="nlist_1s">
							<td>
								昨日打码
							</td>
							<td>
								${yesterdayMap.pic_count}
							</td>	
							<td>
								${yesterdayMap.pic_success }
							</td>
							<td>
								${yesterdayMap.pic_fail }
							</td>
							<td>
								${yesterdayMap.pic_unkonwn }
							</td>
							<td>
								￥<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${adminCodeYesterdaySuccess*6/100 }</span>元
							</td>
							<td>
								${time }
							</td>
						</tr>
						<tr style="height:60px; " class="nlist_1s">
							<td>
								本周打码
							</td>
							<td>
								${weekMap.pic_count}
							</td>	
							<td>
								${weekMap.pic_success }
							</td>
							<td>
								${weekMap.pic_fail }
							</td>
							<td>
								${weekMap.pic_unkonwn }
							</td>
							<td>
								￥<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${adminCodeWeekSuccess*6/100 }</span>元
							</td>
							<td>
								${time }
							</td>
						</tr>
						<tr style="height:60px; " class="nlist_1s">
							<td>
								本月打码
							</td>
							<td>
								${monthMap.pic_count}
							</td>	
							<td>
								${monthMap.pic_success }
							</td>
							<td>
								${monthMap.pic_fail }
							</td>
							<td>
								${monthMap.pic_unkonwn }
							</td>
							<td>
								￥<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${adminCodeMonthSuccess*6/100 }</span>元
							</td>
							<td>
								${time }
							</td>
						</tr>
						<tr style="height:60px; " class="nlist_1s">
							<td>
								总打码
							</td>
							<td>
								${totalMap.pic_count}
							</td>	
							<td>
								${totalMap.pic_success }
							</td>
							<td>
								${totalMap.pic_fail }
							</td>
							<td>
								${totalMap.pic_unkonwn }
							</td>
							<td>
								￥<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${adminCodeTotalSuccess*6/100 }</span>元
							</td>
							<td>
								${time }
							</td>
						</tr>
					</table>
				
				<br />
				</form>
			</div>
			</div>
	</body>
</html>
