<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>保险管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
		<script type="text/javascript">
		
	function exportExcelInsurance() {
		$("form:first").attr("action","/orderForExcel/excelexportInsurance.do");
		$("form:first").submit();
		$("form:first").attr("action","/insurance/queryInsuranceList.do");
	}
	function selectAll(){
		var checklist = document.getElementsByName("insueance_status");
		if(document.getElementById("controlAll").checked){
			for(var i=0; i<checklist.length; i++){
				checklist[i].checked = 1;
			}
		}else{
			for(var j=0; j<checklist.length; j++){
				checklist[j].checked = 0;
			}

		}
	}
		</script>
	</head>
	<body>
		<div class="book_manage oz">
			<form action="/insurance/queryInsuranceList.do" method="post" name="myform" id="myform">
				<div style="border: 0px solid #00CC00; margin: 10px;">
					<ul class="order_num oz" style="margin-top: 10px;">
						<li>
							订单号&nbsp;&nbsp;：
							<input type="text" class="text" name="order_id" value="${order_id }" />
						</li>
						<li>
							保险单号：
							<input type="text" class="text" name="bx_code" value="${bx_code }"/>
						</li>
						<li>
							联系电话：
							<input type="text" class="text" name="telephone" value="${telephone }" />
						</li>
					</ul>
					<ul class="order_num oz" style="margin-top: 10px;">
						<li>
							开始时间：
							<input type="text" class="text" name="begin_create_time" readonly="readonly" value="${begin_create_time }"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})"/> 
						</li>
						<li>
							结束时间：
								<input type="text" class="text" name="end_create_time" readonly="readonly" value="${end_create_time}"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" /> 
						</li>
					</ul>
					<ul class="oz" style="margin-top: 10px;">
						<li>
							保险状态：
						</li>
						<li>
							<input type="checkbox" onclick="selectAll()" name="controlAll" style="controlAll" id="controlAll"/><label for="">全部</label>
						</li>
						<c:forEach items="${insueance_statuses }" var="s" varStatus="index">
							<li>
								<input type="checkbox" id="insueance_status${index.count }"
									name="insueance_status" value="${s.key }"
									<c:if test="${fn:contains(insueance_Str, s.key ) }">checked="checked"</c:if> />
								<label for="insueance_status${index.count }">
									${s.value }
								</label>
							</li>
						</c:forEach>
					</ul>
					<ul class="oz" style="margin-top: 10px;">
						<li>
							订单来源：
						</li>
						<c:forEach items="${orderSource }" var="s" varStatus="index">
							<li>
								<input type="checkbox" id="order_source${index.count }"
									name="order_source" value="${s.key }" value="1"
									<c:if test="${fn:contains(orderSourceStr, s.key ) }">checked="checked"</c:if> />
								<label for="order_source${index.count }">
									${s.value }
								</label>
							</li>
						</c:forEach>
					</ul>
				</div>
				<p>
					<input type="submit" value="查 询" class="btn" />
					<input type="button" value="导出Excel" class="btn" onclick="exportExcelInsurance()"/>
				</p>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #f0f0f0;">
							<td>
								序号
							</td>
							<td>
								订单号
							</td>
							<td>
								保险单号
							</td>
							<td>
								联系电话
							</td>
							<td>
								出发/到达
							</td>
							<td>
								用户姓名
							</td>
							<td>
								创建时间
							</td>
							<td>
								状态
							</td>
							<td>
								失败原因
							</td>
							<td>
								进货金额
							</td>
							<td>
								支付金额
							</td>
							<td>
								操作
							</td>
						</tr>
						<c:forEach var="list" items="${insuranceList}" varStatus="idx">
							<tr
								<c:if test="${list.bx_status !=null && list.bx_status ==0}">
		            		 		style="background:#E0F3ED; "
		            			</c:if>
		            			<c:if test="${list.bx_status ==null}">
		            		 		style="background:#F5A9A9; "
		            			</c:if>
		            			<c:if test="${list.bx_status !=null && list.bx_status ==6}">
		            		 		style="background:#F7BE81;"
		            			</c:if>
		            			<c:if test="${list.bx_status !=null && list.bx_status ==5}">
		            		 		style="background:#AEFF00;"
		            			</c:if>
		            			>
								<td>
									${idx.index+1}
								</td>
								<td>
									${list.order_id}
								</td>
								<td>
									${list.bx_code }
								</td>
								<td>
									${list.telephone }
								</td>
								<td>
									${list.from_name}/${list.to_name }
								</td>
								<td>
									${list.user_name }
								</td>
								<td>
									${list.create_time }
								</td>
								<c:choose>
									<c:when test="${list.bx_status ==null }">
										<td>
											未支付
										</td>
									</c:when>
									<c:otherwise>
										<td>
											${insueance_statuses[list.bx_status] }
										</td>
									</c:otherwise>
								</c:choose>
								<td>
									${list.fail_reason }
								</td>
								<td>
									${list.buy_money }
								</td>
								<td>
									${list.pay_money }
								</td>
								<td>
									<a href="/insurance/queryInsuranceInfo.do?bx_id=${list.bx_id }&order_id=${list.order_id }">明细</a>
									<c:if test="${list.bx_status != null && list.bx_status == 5}">
										<a href="/insurance/updateInsuranceStatusSendAgain.do?bx_id=${list.bx_id }&order_id=${list.order_id }"
											onclick="return confirm('确认重新投保么？')">重新投保</a>
									 </c:if>
									 <c:if test="${list.bx_status != null && list.bx_status eq '2'}">
										<a href="/insurance/updateInsuranceStatusNeedCancel.do?bx_id=${list.bx_id }&order_id=${list.order_id }"
											onclick="return confirm('确认退保么？')">退保</a>
									 </c:if>
									 <c:if test="${list.bx_status != null && list.bx_status eq '1'}">
										<a href="/insurance/updateInsuranceStatusSendAgain.do?bx_id=${list.bx_id }&order_id=${list.order_id }"
											onclick="return confirm('确认重新投保么？')">重新投保</a>
									 </c:if>
									 <c:if test="${list.bx_status != null && list.bx_status eq '3'}">
										<a href="/insurance/updateInsuranceStatusNeedCancel.do?bx_id=${list.bx_id }&order_id=${list.order_id }"
											onclick="return confirm('确认重新退保么？')">重新退保</a>
									 </c:if>
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