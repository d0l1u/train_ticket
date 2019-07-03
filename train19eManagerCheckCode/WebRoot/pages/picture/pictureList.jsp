<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>查询页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	    <script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
	    <script type="text/javascript">
		    function submitForm(){
				$("#myform").submit();
			}
		    function exportExcel() {
				$("form:first").attr("action","/excel/excelAdminExport.do");
				$("form:first").submit();
				$("form:first").attr("action","/picture/queryPicturePageList.do");
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
			//人数分时统计
		    function queryUserHour(){
		    	var begin_time = $('#begin_time').val(); 
				var end_time = $('#end_time').val();
				var department = $('#department').val();
				//var url="/picture/queryUserHour.do?begin_time="+begin_time+"&end_time="+end_time+"&department="+department+"&version="+new Date();
				var url="/picture/queryUserHour.do?begin_time="+begin_time+"&end_time="+end_time+"&version="+new Date();
				showlayer('人数分时统计',url,'1000px','420px');
			}
		  	//打码分时统计
		    function queryCodeHour(){
		    	var begin_time = $('#begin_time').val(); 
				var end_time = $('#end_time').val();
				var department = $('#department').val();
				//var url="/picture/queryCodeHour.do?begin_time="+begin_time+"&end_time="+end_time+"&department="+department+"&version="+new Date();
				var url="/picture/queryCodeHour.do?begin_time="+begin_time+"&end_time="+end_time+"&version="+new Date();
				showlayer('打码分时统计',url,'1000px','420px');
			}
		  	//打码成功分时统计
		    function querySuccessCodeHour(){
		    	var begin_time = $('#begin_time').val(); 
				var end_time = $('#end_time').val();
				var department = $('#department').val();
				//var url="/picture/queryCodeHour.do?begin_time="+begin_time+"&end_time="+end_time+"&department="+department+"&version="+new Date();
				var url="/picture/querySuccessCodeHour.do?begin_time="+begin_time+"&end_time="+end_time+"&version="+new Date();
				showlayer('打码成功分时统计',url,'1000px','420px');
			}
		  	//打码失败分时统计
		    function queryFailCodeHour(){
		    	var begin_time = $('#begin_time').val(); 
				var end_time = $('#end_time').val();
				var department = $('#department').val();
				//var url="/picture/queryCodeHour.do?begin_time="+begin_time+"&end_time="+end_time+"&department="+department+"&version="+new Date();
				var url="/picture/queryFailCodeHour.do?begin_time="+begin_time+"&end_time="+end_time+"&version="+new Date();
				showlayer('打码失败分时统计',url,'1000px','420px');
			}
	    </script>
	</head>
	<body>
	<div style="width:expression(document.body.clientWidth + 'px')">
		<div class="book_manage oz">
			<form action="/picture/queryPicturePageList.do" method="post" name="myform" id="myform">
				<div style="border: 0px solid #00CC00; margin: 15px;">
				<ul class="order_num oz">
					<li>开始时间：&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="begin_time" id="begin_time" readonly="readonly" value="${begin_time }"
										onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})"/>
					</li>
					<li>结束时间：&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="end_time" id="end_time" readonly="readonly" value="${end_time }"
										onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>
					<li>所在部门：&nbsp;&nbsp;&nbsp;&nbsp;
						<select name="department" style="width: 120px;" id="department">
							<c:forEach items="${userDepartment }" var="t">
           						<option value="${t.key}" <c:if test="${!empty department && department eq t.key }">selected</c:if>>${t.value}</option>
           					</c:forEach>
		       			</select>
					</li>
					<!-- 
					<li>
						<input type="button" value="查 询" id="btnSubmit" class="btn btn_normal" style="font-size:12px;" onclick="submitForm()" />&nbsp;&nbsp;
						<input type="button" value="导出Excel" class="btn btn_normal" style="font-size:12px;" onclick="exportExcel()"/>
					</li>
					 -->
				</ul>
				<br />
	        	<p>
	        		<input type="button" value="查 询" id="btnSubmit" class="btn" onclick="submitForm()" />&nbsp;
					<input type="button" value="导出Excel" class="btn" onclick="exportExcel()"/>&nbsp;
					
		        	<input type="button" value="人数分时统计" class="btn" id="userHour" onclick="queryUserHour()" />&nbsp;
		        	<input type="button" value="打码分时统计" class="btn" id="codeHour" onclick="queryCodeHour()" />&nbsp;
		        	<input type="button" value="打码成功分时" class="btn" id="codeHour" onclick="querySuccessCodeHour()" />&nbsp;
		        	<input type="button" value="打码失败分时" class="btn" id="codeHour" onclick="queryFailCodeHour()" />&nbsp;
		        </p>
				</div>
				
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #f0f0f0;">
							<th>
								序号
							</th>
						<!-- 	<th>
								创建日期
							</th>  -->
							<th>
								操作日期
							</th>
							<th>
								用户名
							</th>
							<th>
								真实姓名
							</th>
							<th>
								所在部门
							</th>
							<th>
								打码总数
							</th>
							<th>
								打码成功
							</th>
							<th>
								打码失败
							</th>
							<th>
								打码超时
							</th>
							<th>
								渠道
							</th>
						</tr>
						
						<c:forEach var="list" items="${pictureList}" varStatus="idx">
						<tr class="nlist_1s">
							<td>
								${idx.index+1}
							</td>
						<!-- 	<td>
								${list.create_time}
							</td>   -->
							<td>
								${list.opt_time}
							</td>	
							<td>
								${list.opt_ren }
							</td>
							<td>
								${list.opt_name }
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
								${list.channel }
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