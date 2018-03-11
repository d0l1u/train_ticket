<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo"%>
<%@ page import="com.l9e.transaction.vo.PageVo"%>
<%@ page import="java.util.*"%>
<%@ page import="com.l9e.util.JSONUtil"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>邮箱管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script type="text/javascript">
		<%
		PageVo pageObject = (PageVo) request.getAttribute("pageBean");
		int pageIndex = pageObject.getPageIndex();
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user_level = loginUserVo.getUser_level();
	%>
	
			function importExcel(){
				var file = $.trim($("#excelFile").val());
				var fileType = (file.substring(file.lastIndexOf(".")+1,file.length)).toLowerCase();  
				//alert(fileType);
				if(file==""){
					alert("请选择'.xls'格式的文件！");
					$("#excelFile").focus();
					return;
				}else if(fileType != "xls"){
					alert("请选择'.xls'格式的文件！");
					$("#excelFile").focus();
					return;
				}
				$("form:first").attr("method","post");
				$("form:first").attr("action","/mail/addExcelMail.do?file="+file);
				//$("form:first").attr("enctype","multipart/form-data");
				//$("form:first").attr("action","/file/fileupload");
				$("form:first").submit();
			}
			
			function exportExcel() {
			$("form:first").attr("action","/mail/exportexcel.do");
			$("form:first").submit();
			$("form:first").attr("action","/mail/queryMailList.do");
		}
		</script>
	</head>

	<body><div></div>
		<div class="book_manage oz">
			<form action="/mail/queryMailList.do" method="post" enctype="multipart/form-data">
				<div style="border: 0px solid #00CC00; margin: 10px;">
					<ul class="order_num oz" style="margin-top: 10px;">
						<li>
							邮箱地址：
							<input type="text" class="text" name="address"
								value="${address }" />
						</li>
						<li>
							开始时间：
							<input type="text" class="text" name="begin_info_time"
								readonly="readonly" value="${begin_info_time }"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" id="defaultStartDate1"/>
						</li>
						<li>
							结束时间：
							<input type="text" class="text" name="end_info_time"
								readonly="readonly" value="${end_info_time }"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
						</li>
					</ul>
					
					<ul class="ser oz">
						<li>
							邮箱状态：
						</li>
						<li>
							<c:forEach items="${statusStr }" var="s" varStatus="index">
								<li>
									<input type="checkbox" id="status${index.count }"
										name="status" value="${s.key }"
										<c:if test="${fn:contains(status, s.key ) }">checked="checked"</c:if> />
									<label for="status${index.count }">
										${s.value }
									</label>
								</li>
							</c:forEach>
						</li>
					</ul>
					<ul class="ser oz">
						<li>
						邮箱类型：
						</li>
							<li>
								<input type="checkbox" id="mail_qita" name="mail_qita" value="mail_qita"
									<c:if test="${mail_qita ne '' }">checked="checked"</c:if> />
								<label for="mail_qita">
									其他
								</label>
							</li>
							<li>
								<input type="checkbox" id="mail_163" name="mail_163" value="mail_163"
									<c:if test="${mail_163 ne '' }">checked="checked"</c:if> />
								<label for="mail_163">
									163
								</label>
							</li>
							<li>
								<input type="checkbox" id="mail_19trip" name="mail_19trip" value="mail_19trip"
									<c:if test="${mail_19trip ne '' }">checked="checked"</c:if> />
								<label for="mail_19trip">
									19trip
								</label>
							</li>
							
				</ul>
					<!-- 
					<ul class="ser oz">
						<li>
							是否启用：
						</li>
						<li>
							<c:forEach items="${isOpenStr }" var="s" varStatus="index">
								<li>
									<input type="checkbox" id="is_open${index.count }"
										name="is_open" value="${s.key }"
										<c:if test="${fn:contains(is_open, s.key ) }">checked="checked"</c:if> />
									<label for="is_open${index.count }">
										${s.value }
									</label>
								</li>
							</c:forEach>
						</li>
					</ul>
				 -->
        <p>
        	<input type="submit" value="查 询" class="btn" />
        	<input type="button" value="添 加" class="btn" id="addMail" onclick="location.href = '/mail/toAddMail.do'" />
        	<%		if ("2".equals(loginUserVo.getUser_level()) ) {%>
					<input type="button" value="导出Excel" class="btn" onclick="exportExcel()" />
					<%} %>
       	<span style="font-size:14px;float:right;">
        	选择文件：<input type="file" name="excelFile" id="excelFile" />
        	<input type="button" value="批量导入" id="import" onclick="importExcel()"/>  </span>
        </p>
				</div>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #EAEAEA;">
							<th>
								序号
							</th>
							<th>
								邮箱地址
							</th>
							<th>
								邮箱密码
							</th>
							<th>
								状态
							</th>
							<!-- 
							<th>
								是否启用
							</th>
							 -->
							<th>
								创建时间
							</th>
							<th>
								操作时间
							</th>
							<th>
								操作人
							</th>
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${mailList}" varStatus="idx">
						<tr 
							<c:if test="${list.status eq '0' }">
								style="background:#E0F3ED; "
							</c:if> 
							<c:if test="${list.status eq '1' }">
								style="background:#ffffff; "
							</c:if>
							>
							<td>
								${idx.index+1}
							</td>
							<td>
								${list.address}
							</td>
							<td>
								${list.pwd }
							</td>
							<td>
								${statusStr[list.status] }
							</td>
							<!-- 
							<td>
								${isOpenStr[list.is_open] }
							</td>
							 -->
							<td>
								${list.create_time }
							</td>
							<td>
								${list.opt_time }
							</td>
							<td>
								${list.opt_name}
							</td>
							<td>
								
								<span> 
									<c:if test="${list.status eq '0' }">
										<a href="javascript:location='/mail/updateMailOpen.do?mail_id=${list.mail_id }';">已使用</a>
									</c:if> 
									<c:if test="${list.status eq '1' }">
										<a href="javascript:location='/mail/updateMailStop.do?mail_id=${list.mail_id }';">未使用</a>
									</c:if>
								</span>
									<a href="/mail/toUpdateMail.do?mail_id=${list.mail_id }">修改</a>
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
