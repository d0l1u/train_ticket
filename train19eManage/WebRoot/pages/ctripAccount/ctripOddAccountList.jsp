<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.PageVo"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo"%>
<%@ page import="java.util.*" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>携程账号管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
			<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
	</head>
	<script type="text/javascript">
	<%
		PageVo pageObject = (PageVo) request.getAttribute("pageBean");
		int pageIndex = pageObject.getPageIndex();
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user_level = loginUserVo.getUser_level();
	%>

	function updateInfoResult(ctrip_id,ctrip_name){
		var url="/ctripAccount/removeOddAccount.do?version="+new Date();
		$.post(url,{ctrip_id:ctrip_id,ctrip_name:ctrip_name},function(data){
			if(data=="yes"){
				$("form:first").attr("action","/ctripAccount/queryOddAccountCtrip.do?pageIndex="+<%=pageIndex%>);
				$("form:first").submit();
			}else{
				alert("修改账号状态失败！");
			}
		});
	}
	
	function opt_history(){
		var url="/ctripAccount/queryCtripHistory.do";
		showlayer('修改 ',url,'950px','600px');
	}
	function amountAreaSet(){
		window.location.href="/ctripAccount/queryAmountArea.do";
	} 

</script>
	<body><div></div>
		<div class="book_manage oz">
			<form  name="queryFrm" action="/ctripAccount/queryOddAccountCtrip.do" method="post">
				<ul class="ser oz">
					<li>
						登陆名称：
						<input type="text" class="text" name="ctrip_name" value="${ctrip_name }"  style="width:146px;"/>
					</li>
					<li>
						用户名称：
						<input type="text" class="text" name="ctrip_username" value="${ctrip_username }" style="width:140px;" />
					</li>
					<li>
						手机号：
						<input type="text" class="text" name="ctrip_phone" value="${ctrip_phone }" />
					</li>
				</ul>
				<ul class="ser oz">
					<li>
							开始时间：
							<input type="text" class="text" name="begin_info_time"
								readonly="readonly" value="${begin_info_time }" style="width:146px;"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" id="defaultStartDate1"/>
						</li>
						<li>
							结束时间：
							<input type="text" class="text" name="end_info_time"
								readonly="readonly" value="${end_info_time }" style="width:140px;"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
						</li>
						<li>
							金&nbsp;额：
							<input type="text" class="text" name="beginBalance" value="${beginBalance }"/>
							-
							<input type="text" class="text" name="endBalance" value="${endBalance }"/>
						</li>
						
				</ul>
				<ul class="ser oz">
					<li>
							&nbsp;&nbsp;&nbsp;&nbsp;状态：
						<c:forEach items="${ctripStatus }" var="s" varStatus="index">
								<input type="checkbox" id="ctrip_status${index.count }" name="ctrip_status" value="${s.key }" 
									<c:if test="${fn:contains(statusStr, s.key ) }">checked="checked"</c:if> />
								<label for="ctrip_status${index.count }">
									${s.value }
								</label>&nbsp;&nbsp;&nbsp;&nbsp;
						</c:forEach>
					</li>
				</ul>
				<ul class="ser oz">
					<li>
						账号级别：
						<select name="acc_degree"  style="width:100px;">
							<option value="">全部</option>
							<c:forEach items="${ctripDegree}" var="s" varStatus="index">
								<option value="${s.key}" ${s.key eq acc_degree ? 'selected':''}>${s.value }</option>
							</c:forEach>
						</select>
					</li>
				</ul>
				<br />
				<p>
					<input type="submit" value="查 询" class="btn" /> 
					<input type="button" value="返回" onclick="location.href = '/ctripAccount/queryCtripAccountPage.do'" class="btn" /> 
				</p>
				
				<br/>
					<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #EAEAEA;">
							<th width="8">
								NO
							</th> 
							<th>
								登陆名
							</th>
							<th>
								异常状态
							</th>
							<th>
								账号状态
							</th> 
							<th>
								账号余额
							</th> 
							<th>
								手机号
							</th>
							<th>
								礼品卡
							</th> 
							<th>
								邮箱账号
							</th> 
							<th>
								邮箱密码
							</th> 
							<th width="65">
								创建时间
							</th>
							<th width="65">
								操作时间
							</th>
							<th>
								操作人
							</th>
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${ctripAccountList}" varStatus="idx">
							<tr>
							<td>
								${idx.index+1}
							</td> 
							<td>
								${list.ctrip_name}
							</td>
							<td>
								<c:if test="${list.result_type eq '1' }">账号异常</c:if> 
								<c:if test="${list.result_type eq '2' }">查询超时</c:if> 
								<c:if test="${list.result_type eq '3' }">余额不一致</c:if> 
							</td> 
							<td>
								${ctripStatus[list.ctrip_status]}
							</td> 
							<td>
								${list.balance}
							</td> 
							<td>
								${list.ctrip_phone}
							</td>
							<td>
								${list.ctrip_card_no}
							</td> 
							<td>
								${list.mail_account}
							</td> 
							<td>
								${list.mail_pwd}
							</td> 
							<td>
								${fn:substringBefore(list.create_time, ' ')}
								<br />
								${fn:substringAfter(list.create_time, ' ')}
							</td>
							<td>
								${fn:substringBefore(list.option_time, ' ')}
								<br />
								${fn:substringAfter(list.option_time, ' ')}
							</td>
							<td>
								${list.opt_person }
							</td>
							<td>
								<span> 
									<a href="javaScript:void(0)" onclick="updateInfoResult('${list.ctrip_id }','${list.ctrip_name }');" >移除异常</a>  
								</span>
							</td>
							</tr>
							
						</c:forEach>
					</table>
					<jsp:include page="/pages/common/paging.jsp" />
				</c:if>
				<br />
				<p></p>
			</form>
		</div>
	</body>
</html>
