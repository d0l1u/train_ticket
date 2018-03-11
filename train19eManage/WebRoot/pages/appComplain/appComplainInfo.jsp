<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>投诉建议管理详细</title>
		<link rel="stylesheet" href="/css/style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<!--<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>-->
		<script type="text/javascript">
			function submit(complain_id){
				
				$("#updateForm").submit();
			}
		</script>
	</head>
	<body>
		<div class="content oz">
			<form action="/appComplain/updateComplainInfo.do" method="post"
				id="updateForm">
				<input type='hidden' name='complain_id'
					value='${complainInfo.complain_id}' />

				<div class="pub_order_mes oz mb10_all">
					<h4>
						投诉与建议详细
					</h4>
					<div class="pub_con">
						<table class="pub_table">
							<tr>
								<td class="pub_yuliu" rowspan="6"></td>
								<td width="234" colspan="2">
									问题类型：
									<span>${questionType[complainInfo.question_type]}</span>
								</td>
							</tr>
							<tr>
								<td width="234">
									问题内容：
									<span>${complainInfo.question}</span>
								</td>
							</tr>
						<!-- 	<tr>
								<td width="234">
									代理商姓名：
									<span>${complainInfo.user_name}</span>
								</td>
								<td>
									代理商账号：
									<span>${complainInfo.user_phone}</span>
								</td>
							</tr>
							<tr>
								<td width="234">
									所在省：
									<span>${complainInfo.province_name }</span>
								</td>
								<td>
									所在市：
									<span>${complainInfo.city_name }</span>
								</td>
							</tr>    -->
							
							<c:if test="${sessionScope.loginUserVo.user_level eq '1'||sessionScope.loginUserVo.user_level eq '2'}">
							<tr>
								<c:if test="${complainInfo.permission!=1}">
									<td>
										&nbsp;&nbsp;&nbsp;
										<input type="radio" value="0" name="permission"
											checked="checked" />
										全部可见 &nbsp;&nbsp;&nbsp;
										<input type="radio" value="1" name="permission" />
										自己可见
									</td>
								</c:if>
								<c:if test="${complainInfo.permission!=0}">
									<td>
										&nbsp;&nbsp;&nbsp;
										<input type="radio" name="permission" value="0" />
										全部可见 &nbsp;&nbsp;&nbsp;
										<input type="radio" name="permission" value="1"
											checked="checked" />
										自己可见
									</td>
								</c:if>
							</tr>
							</c:if>
							<c:if test="${sessionScope.loginUserVo.user_level eq '0'}">
								<input type="hidden" name="permission" value="1" />
							</c:if>
						</table>
					</div>
					<div class="pub_debook_mes delivery_debook oz mb10_all">
						<h4>
							解答内容
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
										<textarea name="answer" id="answer" cols="30" rows="10">${complainInfo.answer}</textarea>
									</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</form>

		</div>
		<div class="pub_debook_mes  oz mb10_all">
			<p>
				<input type="button" value="修改" class="btn btn_normal"
					onclick="submit()" />
				<input type="button" value="返 回" class="btn btn_normal"
					onclick="javascript:history.back(-1);" />
			</p>
		</div>
	</body>
</html>