<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>商户管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
		<script type="text/javascript">
			function opt_rizhi(){
				var url="/extSetting/querySystemSetList.do";
				showlayer('操作日志',url,'950px','600px')
			}
			function stop(stop_id,merchant_id,merchant_name,obj){
				if( $.trim($("#merchant_stop_reason_"+stop_id).val())== "" ){
					alert("请输入停用原因！");
					$("#merchant_stop_reason_"+stop_id).focus();
					return false;
				}else{
					var merchant_stop_reason = $("#merchant_stop_reason_"+stop_id).val();
					$("form:first").attr("action","/extSetting/updateMerchantStatus.do?merchant_id="+merchant_id+"&merchant_status=00&merchant_name="+merchant_name+"&merchant_stop_reason="+merchant_stop_reason);
					$("form:first").submit();
				}
			}
		</script>
		<style>
			tr:hover{background:#ecffff;}
		</style>
	</head>
	<body><div></div>
		<div class="book_manage oz">
			<form action="/extSetting/queryExtSettingList.do" method="post">
				<ul class="order_num oz" style="margin-top:20px;margin-bottom:20px;">
					<li>
						合作商户编号：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="merchant_id" value="${merchant_id}"/>
					</li>
					<li>
					 	合作商户名称：&nbsp;&nbsp;
						<input type="text" class="text" name="merchant_name" value="${merchant_name}"/>
					</li>
				</ul>
				
				<p>
					<input type="submit" value="查 询" class="btn" />&nbsp;&nbsp;
					<input type="button" value="添加商户" class="btn" onclick="location.href = '/extSetting/toAddMarchantInfo.do'"/>&nbsp;&nbsp;
					<input type="button" value="查看日志" class="btn" id="rizhi" onclick="opt_rizhi()" />
				</p>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #EAEAEA;">
							<th>
								序号
							</th>
							<th>
								合作商户名称
							</th>
							<th>
								合作商户编号
							</th>
							<th>
								扣费方式
							</th>
							<th>
								支付方式
							</th>
							<th>
								保险
							</th>
							<th>
								短信
							</th>
							<th>
								余票阀值
							</th>
							<th>
								发车前订票
							</th>
							<th>
								商户状态
							</th>
							<th>
								停用原因
							</th>
							<th>
								创建时间
							</th>
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${extSettingList}" varStatus="idx">
							<tr 
								<c:if test="${list.merchant_status != null && list.merchant_status=='11'}">
									style="background: #BEE0FC;"
								</c:if>
							>
							<td>
								${idx.index+1}
							</td>
							<td>
								${list.merchant_name}
							</td>
							<td>
								${list.merchant_id }
							</td>
							<td>
								${merchantFees[list.merchant_fee]}
							</td>
							<td>
								${payTypes[list.pay_type]}
							</td>
							<td>
								${bxCompanys[list.bx_company] }
							</td>
							<td>
								${smsChannels[list.sms_channel]}
							</td>
							<td>
								${list.spare_ticket_amount }
							</td>
							<td>
								${list.stop_buyTicket_time }小时
							</td>
							<td>
								${merchantStatuss[list.merchant_status] }
							</td>
							<td>
								<c:if test="${list.merchant_status eq '11'}">
									<input type="text" id="merchant_stop_reason_${idx.index}" name="merchant_stop_reason" />
								</c:if>
								<c:if test="${list.merchant_status eq '00'}">
									${list.merchant_stop_reason }
								</c:if>
							</td>
							<td>
								${list.create_time }
								<!-- 
								${fn:substringBefore(list.create_time, ' ')}
								<br />
								${fn:substringAfter(list.create_time, ' ')}
								 -->
							</td>
							<td>
								<span>
									<c:if test="${list.merchant_status eq '11'}">
										<a href="javascript:void(0)" onclick="stop(${idx.index},'${list.merchant_id}','${list.merchant_name }',this)">停用</a>
									</c:if>
									<c:if test="${list.merchant_status eq '00'}">
										<a href="/extSetting/updateMerchantStatus.do?merchant_id=${list.merchant_id}&merchant_status=11&merchant_name=${list.merchant_name}">启用</a>
									</c:if>
									<a href="/extSetting/toUpdateMerchantInfo.do?merchant_id=${list.merchant_id}">修改</a>
								</span>
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
