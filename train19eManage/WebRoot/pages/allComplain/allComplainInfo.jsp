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
		<script type="text/javascript">
			function submit(complain_id){
				
				$("#updateForm").submit();
			}
		</script>
	</head>
	<body>
		<div class="content oz">
			<form action="/allComplain/updateComplainInfo.do" method="post"
				id="updateForm">
				<input type='hidden' name='complain_id'
					value='${allComplainInfo.complain_id}' />

				<div class="pub_order_mes oz mb10_all">
					<h4>
						投诉与建议详细
					</h4>
					<div class="pub_con">
						<table class="pub_table">
							<tr>
								<td class="pub_yuliu" rowspan="6"></td>
								<td   colspan="2">
									问题类型：
									<span>${questionType[allComplainInfo.question_type]}</span>
								</td>
							</tr>
							<tr>
								<td colspan="2" >
									问题内容：
									<span><c:out value="${allComplainInfo.question}"></c:out></span>
								</td>
							</tr>
							
							<tr>
								<td colspan="2">
									渠&nbsp;&nbsp;&nbsp;&nbsp;道：
									<span>${questionChannel[allComplainInfo.channel] }</span>
								</td>
							</tr>
							<c:if test="${allComplainInfo.channel eq '19e' }">
							<tr>
								<td width="234">
									代理商姓名：
									<span>${allComplainInfo.user_name}</span>
								</td>
								<td>
									代理商账号：
									<span>${allComplainInfo.user_phone}</span>
								</td>
							</tr>
							<tr>
								<td width="234">
									所在省：
									<span>${allComplainInfo.province_name }</span>
								</td>
								<td>
									所在市：
									<span>${allComplainInfo.city_name }</span>
								</td>
							</tr>
								
							</c:if>
					
							<!--<c:if test="${sessionScope.loginUserVo.user_level eq '1'||sessionScope.loginUserVo.user_level eq '2'}">
							 <tr>
								<c:if test="${allComplainInfo.permission!=1}">
									<td>
										&nbsp;&nbsp;&nbsp;
										<input type="radio" value="0" name="permission"
											checked="checked" />
										全部可见 &nbsp;&nbsp;&nbsp;
										<input type="radio" value="1" name="permission" />
										自己可见
									</td>
								</c:if>
								<c:if test="${allComplainInfo.permission!=0}">
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
							</c:if>-->
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
									<!--	<textarea name="answer" id="answer" cols="30" rows="10">${allComplainInfo.answer}</textarea>-->
										${allComplainInfo.answer}
									</td>
								</tr>
							</table>
						</div>
					</div>
			<!-- 		<div class="pub_debook_mes  oz mb10_all">
						<h4>
							历史操作
						</h4>
						<div class="pub_con">
							<table class="pub_table" style="width: 500px; margin: 20px auto; border:1px solid #C0C0C0; " rules="rows" cellspacing=0>
								<c:forEach var="hs" items="${history}" varStatus="idx">
									<tr align="center" style="padding-botton:0px;">
										<td>
											${idx.index+1 }
										</td>
										<td>
											${hs.reply_time}
										</td>
										<td>
											${hs.reply_person}
										</td>
										<td width=100px></td>
									</tr>
									<tr>
										<td colspan="4" style="border-bottom:1px solid #ddd;line-height:20px;padding:0 10px 5px;">答复内容：
											${hs.our_reply}
										</td>
									</tr>
									<!-- 
									<tr><td colspan="3"><hr style="width: 500px;"/></td></tr>
									 -->
						<!--		</c:forEach>
							</table>
						</div>
					</div> -->
				</div>
			</form>

		</div>
		<div class="pub_debook_mes  oz mb10_all">
			<p>
			<!-- 	<input type="button" value="修改" class="btn btn_normal"
					onclick="submit()" /> -->
				<input type="button" value="返 回" class="btn btn_normal"
					onclick="javascript:history.back(-1);" />
			</p>
		</div>
	</body>
</html>