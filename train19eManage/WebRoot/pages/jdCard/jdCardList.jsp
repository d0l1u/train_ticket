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
		<title>京东预付卡管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script language="javascript" src="/js/json2.js"></script>
	</head>
	<script type="text/javascript">
	<%
		PageVo pageObject = (PageVo) request.getAttribute("pageBean");
		int pageIndex = pageObject.getPageIndex();
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user_level = loginUserVo.getUser_level();
	%>
	

       //全选预付卡状态
	function selectAll(){
		var checklist = document.getElementsByName("card_status");
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
		
	function submitForm(){

	$("form:first").attr("action","/jdCard/queryJDCardList.do");
	$("form:first").submit();
    }
	
	function addCardInfo(){
	var url="/jdCard/addJdCardPage.do";
    showlayer('新增 ',url,'600px','400px');
    } 
    
    function updateCardInfo(card_id){
	var url="/jdCard/updateJdCardPage.do?card_id="+card_id;
	showlayer('修改 ',url,'800px','600px');
	}
	

</script>
	<body><div></div>
		<div class="book_manage oz">
			<form  name="queryFrm" action="/jdCard/queryJDCardList.do" method="post">
				<ul class="ser oz">
					<li>
						预付卡卡号：
						<input type="text" class="text" name="card_no" value="${card_no}"  style="width:146px;"/>
					</li>
					
					<li>
						金&nbsp;额：
						<input type="text" class="text" name="beginBalance" value="${beginBalance}"/>
						-
						<input type="text" class="text" name="endBalance" value="${endBalance}"/>
					</li>
				
				</ul>
				<ul class="ser oz">
					<li>
							开始时间：
							<input type="text" class="text" name="begin_info_time"
								readonly="readonly" value="${begin_info_time}" style="width:146px;"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" id="defaultStartDate1"/>
						</li>
						<li>
							结束时间：
							<input type="text" class="text" name="end_info_time"
								readonly="readonly" value="${end_info_time}" style="width:140px;"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
						</li>
					
						
				</ul>
			     
			     <ul class="ser oz">
					<li>
						预付卡状态：
					</li>
					<li><input type="checkbox" onclick="selectAll()" name="controlAll" style="controlAll" id="controlAll"/><label for="controlAll">全部</label></li>
					<li><input type="checkbox" id="card_status1" name="card_status" value="00"
								<c:if test="${fn:contains(card_status, '00')}">checked="checked"</c:if> />
								<label for="card_status1">空闲</label></li>
					<li><input type="checkbox" id="card_status2" name="card_status" value="11"
								<c:if test="${fn:contains(card_status, '11')}">checked="checked"</c:if> />
								<label for="card_status2">使用中</label></li>
				
					<li><input type="checkbox" id="card_status3" name="card_status" value="22"
								<c:if test="${fn:contains(card_status, '22')}">checked="checked"</c:if> />
								<label for="card_status3">停用</label></li>		
					 
				</ul>
				
				<br />
				<p>
					<input type="submit" value="查 询" class="btn" onclick="submitForm();"/>&nbsp;&nbsp;
					<input type="button" value="新增"
						onclick="addCardInfo();" class="btn" />	&nbsp;&nbsp;
					总金额：
						<lable type="text" class="text" name="total_money" readonly="readonly" style="color:red">${total_money}</lable>&nbsp;&nbsp;&nbsp;&nbsp;
					总余额：
						<lable  class="text" name="total_balance" readonly="readonly" style="color:red">${total_balance}</lable>	
				</p>
				
				<br/>
					<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #EAEAEA;">
							<th width="8">
								NO
							</th>
							<th width="60">
							         卡号ID
							</th>
							<th>
								卡号
							</th>
							<th>
								密码
							</th>
							<th>
								状态
							</th>
							<th>
								面额
							</th>
							<th>
								余额
							</th>
							<th width="65">
								创建时间
							</th>
							<th width="65">
								到期时间
							</th>
							<th width="65">
								更新时间
							</th>
							<th>
								截图
							</th>
							<th>
								操作
							</th>
						
						</tr>
						<c:forEach var="list" items="${jdCardList}" varStatus="idx">
							<tr
							
								<c:if test="${fn:contains('11', list.card_status )}">
									style="background: #E0F3ED;"
								</c:if>
								<c:if test="${fn:contains('22', list.card_status )}">
									style="background:#FFB5B5;"
								</c:if>
							>
							
							<td>
								${idx.index+1}
							</td>
							<td>
								${list.card_id}
							</td>
							<td width="200px">
								${list.card_no}
							</td>
							<td>
								${list.card_pwd}
							</td>
							<td>
								${jdCardStatus[list.card_status]}
							</td>
							<td>
							   ${list.card_amount}
							</td>
							<td>
								${list.card_money}
							</td>
							<td width="70px">
								${list.create_time}
							</td>
							<td width="70px">
							<c:choose>
							<c:when test="${list.become_due_time le now2}"><font color="#FF6600;">${list.become_due_time}</font></c:when>
							<c:when test="${list.become_due_time le now4 && list.become_due_time gt now2}"><font color="#FF0000;"><b>${list.become_due_time}</b></font></c:when>
							<c:otherwise>${list.become_due_time}</c:otherwise>
							</c:choose>
							</td>
					
							<td width="70px">
								${list.option_time}
							</td>
							<td>
								${list.img_url}
							</td>
								<td>
								<span> 
									<a href="javaScript:void(0)" onclick="updateCardInfo('${list.card_id}');" >修改</a> 
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
