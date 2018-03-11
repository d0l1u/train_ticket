<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>加盟订单页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<style>
td {
	padding: 5px 0;
}

.tit {
	background: #ddd;
}

</style>
	</head>

	<body>
		<c:if test="${!empty isShowList}">
			<div class="book_manage oz">
				<table class="pub_table" style="margin-top: 20px;">
					<tr class="tit">
						<td>
							序号
						</td>
						<td>
							代理商姓名
						</td>
						<td>
							产品名称
						</td>
						<td>
							产品ID
						</td>
						<td>
							支付金额
						</td>
						<td>
							加盟状态
						</td>
					</tr>
					<c:forEach var="list" items="${userOrder}" varStatus="idx">
						<tr>
							<td width='30px'>
								${idx.index+1}
							</td>
							<td width='70px;'>
								${list.user_name }
							</td>
							<td width='70px;'>
								${list.name }
							</td>
							<td width='70px;'>
								${list.product_id }
							</td>
							<td width='70px;'>
								${list.pay_money }
							</td>
							<td width='70px;'>
								${order_status[list.order_status] }
							</td>

						</tr>
					</c:forEach>
				</table>
			</div>
			<jsp:include page="/pages/common/paging.jsp" />
		</c:if>
		<div class="pub_debook_mes  oz mb10_all">
			<p>
				<input type="button" value="返 回" class="btn btn_normal"
					onclick="javascript:history.back(-1);" />
			</p>
		</div>
	</body>
</html>
