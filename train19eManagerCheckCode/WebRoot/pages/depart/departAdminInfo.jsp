<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>按用户查询页面</title>
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
			<form action="/depart/queryDepartByAdmin.do?department=${department }" method="post" name="myform" id="myform">
				<div style="border: 0px solid #00CC00; margin: 15px;">
				<ul class="order_num oz">
					<li>所在部门：&nbsp;&nbsp;&nbsp;&nbsp;
						<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">	
							${userDepartment[department] }
						</span>
					</li>
					<li>用户名：&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="username" value="${username }" />
					</li>
					<li>真实姓名：&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="real_name" value="${real_name }" />
					</li>
					<li>
						<input type="button" value="查 询" id="btnSubmit" class="btn btn_normal" style="font-size:13px;" onclick="submitForm()" />
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
							<th>
								操作
							</th>
						</tr>
						
						<c:forEach var="list" items="${adminCodeList}" varStatus="idx">
						<!-- <tr style="background: #E0F3ED;"> -->
								<tr class="nlist_1s">
							<td>
								${idx.index+1}
							</td>
							<td>
								${empty list.opt_ren ? "无" : list.opt_ren}
							</td>
							<td>
								${empty list.opt_name ? "无" : list.opt_name}
							</td>
							<td>
            					${userDepartment[list.department] }
							</td>
							
							<td>
								${list.pic_count }
							</td>
							<td>
								${list.pic_success }
							</td>
							<td>
								${list.pic_fail }
							</td>
							<td>
								${list.pic_unkonwn }
							</td>
							<td>
								${time }
							</td>
							<td>
								<a href="/loginManager/queryByAdminInfo.do?opt_ren=${list.opt_ren }">打码明细</a>&nbsp;
								<a href="/jfree/showPeruserCodePicture.do?opt_ren=${list.opt_ren }">15日内打码图表</a>
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