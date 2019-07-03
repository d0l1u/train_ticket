<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>自提订单明细页</title>
		<link rel="stylesheet" href="/css/style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript">
	function submit(){
	
		if(confirm("是否提交？")){
			$("#updateForm").submit();
		}
	}
</script>
	</head>
	<body>
		<div class="book_manage account_manage oz">
			<form action="/system/updateSystem.do" method="post"
				name="updateForm">
				<input type="hidden" name="config_id" value="${system.config_id }" />

				<div class="pub_order_mes oz mb10_all">
					<h4>
						系统管理修改
					</h4>
					<div class="pub_con">
						<table class="pub_table">
							<tr>
								<td class="pub_yuliu" rowspan="5"></td>
								<td width="300" colspan="3">
									省:
									<span>${system.area_name }</span>
								</td>
							</tr>
							<tr>
								<td width="300">
									开通
									<select name="is_open">
										<c:forEach items="${isopen }" var="open">
											<option value="${open.key }"
												<c:if test="${open.key eq system.is_open }">selected="selected"</c:if>>
												${open.value }
											</option>
										</c:forEach>
									</select>
									（判断地区是否开通服务）
								</td>
							</tr>
							<tr>
								<td width="300">
									付费
									<select name="is_cost">
										<c:forEach items="${iscost }" var="open">
											<option value="${open.key }"
												<c:if test="${open.key eq system.is_cost }">selected="selected"</c:if>>
												${open.value }
											</option>
										</c:forEach>
									</select>
									（判断地区是否需要付费加盟）
								</td>
							</tr>
							<tr>
								<td width="300">
									配送
									<select name="is_ps">
										<c:forEach items="${isps }" var="open">
											<option value="${open.key }"
												<c:if test="${open.key eq system.is_ps }">selected="selected"</c:if>>
												${open.value }
											</option>

										</c:forEach>
									</select>
									（判断地区是否开通配送票的服务）
								</td>
							</tr>
							<tr>
								<td width="300">
									购买
									<select name="is_buyable">
										<c:forEach items="${isbuy }" var="open">
											<option value="${open.key }"
												<c:if test="${open.key eq system.is_buyable }">selected="selected"</c:if>>
												${open.value }
											</option>
										</c:forEach>
									</select>
									（判断地区是否可以购买）
								</td>
							</tr>
						</table>
					</div>
					<div class="pub_debook_mes delivery_debook oz mb10_all">
						<h4>
							规则
						</h4>
						<div class="pub_con">
							<table>
								<tr>
									<td colspan="4"></td>
								</tr>
								<tr>
									<td class="pub_yuliu" rowspan="2"></td>
									<td style="vertical-align: top;"></td>
									<td colspan="3">
										<textarea id="rule_content" cols="30" rows="10"
											name="rule_content">${system.rule_content }
										</textarea>
									</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
				<div class="pub_debook_mes  oz mb10_all">
					<p>
						<input type="button" value="更新" class="btn btn_normal"
							onclick="submit()" />
						<input type="button" value="返 回" class="btn btn_normal"
							onclick="javascript:history.back(-1);" />
					</p>
				</div>
			</form>
		</div>
	</body>
</html>
