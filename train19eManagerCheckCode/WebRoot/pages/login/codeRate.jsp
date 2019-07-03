<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>打码效率页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	    <script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
	    <script type="text/javascript">
		    function submitForm(){
		    	$("#myform").submit();
			}

		    function exportExcel() {
				$("form:first").attr("action","/excel/excelCurrentExport.do");
				$("form:first").submit();
				$("form:first").attr("action","/loginManager/queryByAdmin.do");
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
			<form action="/loginManager/queryCodeRate.do" method="post" name="myform" id="myform">
				<div style="border: 0px solid #00CC00; margin: 15px;">
				<ul class="order_num oz">
					<li>打码日期：&nbsp;
						<input type="text" class="text" name="day_stat" readonly="readonly" value="${day_stat }" style="width:120px;"
										onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})"/>
					</li>
					<li>打码时间：&nbsp;
						<select name="hour_stat" style="width: 120px;">
							<option value="" <c:if test="${!empty hour_stat && hour_stat eq '' }">selected</c:if>>全部</option>
							<option value="07" <c:if test="${!empty hour_stat && hour_stat eq '07' }">selected</c:if>>07</option>
							<option value="08" <c:if test="${!empty hour_stat && hour_stat eq '08' }">selected</c:if>>08</option>
							<option value="09" <c:if test="${!empty hour_stat && hour_stat eq '09' }">selected</c:if>>09</option>
							<option value="10" <c:if test="${!empty hour_stat && hour_stat eq '10' }">selected</c:if>>10</option>
							<option value="11" <c:if test="${!empty hour_stat && hour_stat eq '11' }">selected</c:if>>11</option>
							<option value="12" <c:if test="${!empty hour_stat && hour_stat eq '12' }">selected</c:if>>12</option>
							<option value="13" <c:if test="${!empty hour_stat && hour_stat eq '13' }">selected</c:if>>13</option>
							<option value="14" <c:if test="${!empty hour_stat && hour_stat eq '14' }">selected</c:if>>14</option>
							<option value="15" <c:if test="${!empty hour_stat && hour_stat eq '15' }">selected</c:if>>15</option>
							<option value="16" <c:if test="${!empty hour_stat && hour_stat eq '16' }">selected</c:if>>16</option>
							<option value="17" <c:if test="${!empty hour_stat && hour_stat eq '17' }">selected</c:if>>17</option>
							<option value="18" <c:if test="${!empty hour_stat && hour_stat eq '18' }">selected</c:if>>18</option>
							<option value="19" <c:if test="${!empty hour_stat && hour_stat eq '19' }">selected</c:if>>19</option>
							<option value="20" <c:if test="${!empty hour_stat && hour_stat eq '20' }">selected</c:if>>20</option>
							<option value="21" <c:if test="${!empty hour_stat && hour_stat eq '21' }">selected</c:if>>21</option>
							<option value="22" <c:if test="${!empty hour_stat && hour_stat eq '22' }">selected</c:if>>22</option>
							<option value="23" <c:if test="${!empty hour_stat && hour_stat eq '23' }">selected</c:if>>23</option>
		       			</select>
					</li>
					<li>用户名：&nbsp;
						<input type="text" class="text" name="username" value="${username }" style="width:120px;" />
					</li>
					<li>所在部门：&nbsp;
						<select name="department" style="width: 120px;">
							<c:forEach items="${userDepartment }" var="t">
           						<option value="${t.key}" <c:if test="${!empty department && department eq t.key }">selected</c:if>>${t.value}</option>
           					</c:forEach>
		       			</select>
					</li>
					<li>
						<input type="button" value="查 询" id="btnSubmit" class="btn btn_normal" style="font-size:12px;" onclick="submitForm()" />&nbsp;
						<!-- 
							<input type="button" value="导出Excel" class="btn btn_normal" style="font-size:12px;" onclick="exportExcel()"/>
						 -->
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
								打码日期
							</th>
							<th>
								打码小时
							</th>
							<th>
								用户名
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
								拉码平均时长
							</th>
							<th>
								打码平均时长
							</th>
							<th>
								打码成功率
							</th>
						</tr>
						<c:forEach var="list" items="${codeList }" varStatus="idx">
						<tr class="nlist_1s">
							<td>
								${idx.index+1}
							</td>
							<td>
								${list.day_stat}
							</td>
							<td>
								${list.hour_stat}
							</td>
							<td>
								${list.username}
							</td>
							<td>
            					${userDepartment[list.department] }
							</td>
							<td>
								${list.code_count }
							</td>
							<td>
								${list.code_success }
							</td>
							<td>
								${list.code_fail }
							</td>
							<td>
								${list.code_over }
							</td>
							<td>
								${list.code_get_time }
							</td>
							<td>
								${list.code_time }
							</td>
							<td>
								${list.code_success_rate }
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