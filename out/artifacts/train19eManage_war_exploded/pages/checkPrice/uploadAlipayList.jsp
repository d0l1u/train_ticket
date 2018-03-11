<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<%@ page import="com.l9e.transaction.vo.PageVo"%>
<jsp:directive.page import="java.text.NumberFormat" />
<jsp:directive.page import="java.text.DecimalFormat" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>支付宝上传情况</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		 <script type="text/javascript" src="/js/jquery.cookie.js"></script>
		 	<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
		<script type="text/javascript">
		
		function queryTicket(){
			$("form:first").submit();
		}	
		
		function goback(){
			$("form:first").attr("action","/checkPrice/queryCheckPricePage.do");
			$("form:first").submit();
		}
		
	
</script>
<style>
</style>
</head>
	<body><div></div>
		<div class="book_manage oz">
			<form action="/checkPrice/queryUploadAlipayList.do" method="post" name="myform" id="myform">

				<ul class="order_num oz" style="margin-top: 10px;">
					<li>
						时间：&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="date"
							readonly="readonly" value="${date}"
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>
					<li>
						<input name="alipaySort" value="19e" type="radio" ${alipaySort=="19e"?"checked='checked'":""}/>19e
						<input name="alipaySort" value="kuyou" type="radio" ${alipaySort=="酷游"?"checked='checked'":""}/>酷游
					</li>
					
				</ul>
				
				<p>
					<input type="button" value="查  询" class="btn" style="margin-top: 10px;margin-left: -12px;" onclick="queryTicket();"/>
					<input type="button" value="返  回" class="btn btn_normal" onclick="javascript:goback();" />
				</p>
				<div id="hint" class="pub_con" style="display:none"></div>
				<c:if test="${!empty alipayList}">
					<table>
						<tr style="background: #EAEAEA;">
							
							<th>
								支付宝账号
							</th>
							<th>
								上传状态
							</th>
						</tr>
						<c:forEach var="list" items="${alipayList}" varStatus="idx">
							<tr ${list['status']=="已上传"?"":"style='background:#E0F3ED;'"}>
								<td>
									${list['alipayName']}
								</td>
								<td>
									${list['status']}
								</td>
							</tr>
						</c:forEach>
					</table>
				</c:if>
			</form>
		</div>
	</body>
</html>
