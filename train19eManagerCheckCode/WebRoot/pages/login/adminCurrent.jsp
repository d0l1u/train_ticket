<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>当前用户查询页面</title>
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
				<p>
					<span style="font-size:14px;float:right;">当前未打码总数：<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${waitCodeCount }</span>个，
					未打码明细【打码团队01：<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${waitCodeCount01 }</span>，
					打码团队02：<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${waitCodeCount02 }</span>，
					打码团队03：<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${waitCodeCount03 }</span>，
					打码团队其他：<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${waitCodeCount04 }</span>】<br/>
					当前打码员总数：<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${totalCount }</span>人，
					打码员明细【打码团队01：<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${totalCount01 }</span>，
					打码团队02：<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${totalCount02 }</span>，
					打码团队03：<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${totalCount03 }</span>，
					打码团队其他：<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${totalCount04 }</span>】</span>
				</p>
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
								拉码时间间隔
							</th>
							<th>
								截止时间
							</th>
							<th>
								操作
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
								${list.get_code_time_limit }
							</td>
							<td>
								${newDate }
							</td>
							<td>
								<a href="/jfree/showPeruserCodePicture.do?opt_ren=${list.username }">15日内打码图表</a>
							</td>
							</tr>
						</c:forEach>
					</table>
					<jsp:include page="/pages/common/paging.jsp" />
				</c:if>
			</form>
		</div>
		</div>
	</body>
</html>