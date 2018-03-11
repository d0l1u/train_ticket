<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ctrip_callback Redis</title>
<script type="text/javascript" src="/js/jquery.js"></script>
<style type="text/css">
body, table {
	font-size: 12px;
}

table {
	table-layout: fixed;
	empty-cells: show;
	border-collapse: collapse;
	margin: 0 auto;
}

td {
	height: 30px;
}

h1, h2, h3 {
	font-size: 12px;
	margin: 0;
	padding: 0;
}

.table {
	border: 1px solid #cad9ea;
	color: #666;
}

.table th {
	background-repeat: repeat-x;
	height: 30px;
}

.table td, .table th {
	border: 1px solid #cad9ea;
	padding: 0 1em 0;
}

.table tr.alter {
	background-color: #f5fafe;
}
</style>
</head>
<body>
<div align="center" style="margin-bottom: 20px">
 <span style="font-size: 20px;">${key}--Redis列表如下:</span>
 <input type="button" value="已处理" onclick="goYES();">
 <input type="button" value="未处理" onclick="goNO();">
</div>
	<table id="historyTable" width="60%" class="table">
		<tr>
			<th>key</th>
			<th>value</th>
			<th>操作</th>
		</tr>
		<c:forEach items="${ctrip_callback }" var="r">
			<tr>
			
				<td>${r.key }</td>
				<td><textarea rows="1">${r.value }</textarea></td>
				<td>
					<input type="button" value="修改">&nbsp;
					<input type="button" value="删除">&nbsp;
				</td>
			</tr>
		</c:forEach>
	</table>
<script type="text/javascript">
  function goYES(){
	  window.location.href = "/rob/ctrip_callback_yes.do";
  }
  function goNO(){
	  window.location.href = "/rob/ctrip_callback.do";
  }
</script>
</body>
</html>