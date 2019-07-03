<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>当前打码</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	    <script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
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
	</head>
	<body>
		<div style="width:expression(document.body.clientWidth + 'px')">
		<div class="book_manage oz">
			<form action="/loginManager/queryCurrentPage.do" method="post" name="myform" id="myform">
				<div style="border: 0px solid #00CC00; margin: 15px;">
				<ul class="order_num oz">
					<li>
						<span style="font-size:14px;">当前打码总共：<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${totalCount }</span>人</span>
					</li>
					<li>
						<span style="font-size:14px;">今日打码总数：<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${codeCountToday }</span>个</span>
					</li>
					<li>
						<span style="font-size:14px;">今日打码成功总数：<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${codeSuccessToday }</span>个</span>
					</li>
				</ul>
				</div>
				<br />
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #f0f0f0;">
							<th>
								序号
							</th>
							<th>
								用户名
							</th>
							<th>
								姓名
							</th>
							<th>
								部门
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
								截止时间
							</th>
						</tr>
						
						<c:forEach var="list" items="${codeList}" varStatus="idx">
						<tr class="nlist_1s">
							<td>
								${idx.index+1}
							</td>
							<td>
								${list.username}
							</td>
							<td>
								${list.real_name}
							</td>
							<td>
            					${userDepartment[list.department] }
							</td>
							<td>
								${list.adminCodeTodayCount }
							</td>
							<td>
								${list.adminCodeTodaySuccess }
							</td>
							<td>
								${list.adminCodeTodayFail }
							</td>
							<td>
								${list.adminCodeTodayOvertime }
							</td>
							<td>
								${newDate }
							</td>
							</tr>
						</c:forEach>
					</table>
					<jsp:include page="/pages/common/paging.jsp" />
				</c:if>
			<br/>
			<div class="pub_debook_mes  oz mb10_all">
				<input type="button" value="返 回" class="btn btn_normal"onclick="javascript:history.back(-1);" />
			</div>
			</form>
		</div>
		</div>
	</body>
</html>