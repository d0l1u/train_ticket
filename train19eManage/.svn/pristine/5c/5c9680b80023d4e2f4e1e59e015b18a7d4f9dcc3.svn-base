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
		<title>上传下载页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
	<script type="text/javascript">
	function queryList(){
		$("form:first").attr("action","/file/uploadFile.do");
		//$("form:first").attr("enctype","multipart/form-data");
		//$("form:first").submit();
		//$("form:first").attr("action","/file/queryFileList.do");
	}

	function toScript(val)
	{
		var value = val
		value  = value.replace(/\\/gi,"\\\\").replace(/"/gi,"\\\"").replace(/'/gi,"\\\'")
		valArr = value.split("\r\n")
		value=""
		for (i=0; i<valArr.length; i++)
		{
			value += "\"" + valArr[i]
			value += (i!=valArr.length-1) ? "\"	+\"\\n\"+\n" : "\"\n" 
		}
		value = value.substring(1,value.length-2)
		val = value
		return val
	}
	<%
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String username = loginUserVo.getUser_name();
	%>
	
	</script>

	</head>

	<body><div></div>
		<div class="book_manage oz">
			<form action="/file/queryFileList.do" method="post">
				<div style="border: 0px solid #00CC00; margin: 10px;">
					<ul class="order_num oz" style="margin-top: 10px;">
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
				<br/>
        <p>
			<input type="submit" value="查 询" class="btn btn_normal"/>
			<%if(username.equals("libx") || username.equals("xuli") || username.equals("guona")|| username.equals("zhangjc") ){ %> 
			<input type="submit" value="上传" class="btn btn_normal" onclick="queryList();"/>
			<%} %>
        </p>
				</div>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #EAEAEA;">
							<th>
								NO
							</th>
							<th width="120">
								文件名
							</th>
							<th>
								账单时间
							</th>
							<th>
								上传者
							</th>
							<!-- 
							<th>
								文件路径
							</th>
							 -->
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${fileList}" varStatus="idx">
							<tr style="background:#E0F3ED; ">
							<td>
								${idx.index+1}
							</td>
							<td>
								${list.filename}
							</td>
							<td>
								${list.bill_time}
							</td>
							<td>
								${list.opt_name }
							</td>
							<!-- 
							<td>
								${list.filepath }
							</td>
							 -->
							<td>
								<!-- <a href="javascript:void()" onclick="down('${list.id }');">下载。。。</a> -->
								
								<a href="javascript:location='/file/fileDown.do?id=${list.id }'">下载</a>
					
								<%if(username.equals("libx") || username.equals("zhangjc") || username.equals("xuli") || username.equals("guona") ){ %> 
									<a href="javascript:location='/file/fileDelete.do?id=${list.id }'">删除</a>
								<%} %>
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
