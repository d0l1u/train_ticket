<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>注册管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script type="text/javascript">
	<%
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user_level = loginUserVo.getUser_level();
	%>
			function queryList(){
				$("form:first").attr("action","/register/uploadFile.do");}
		
		//鼠标悬浮于“明细”，显示该订单的操作日志
	var heightDiv = 0; 
	function showdiv(regist_id){  
	     var oSon = window.document.getElementById("hint");   
	     if (oSon == null) return;   
	     with (oSon){   
	 		 $.ajax({
				url:"/register/queryRegisterFailInfo.do?regist_id="+regist_id,
				type: "POST",
				cache: false,
				dataType: "json",
				async: true,
				success: function(data){
					if(data=="" || data == null){
						return false;
					}
					var size = data.length;
					heightDiv = 0;
					for(var i=0; i<size; i++){
						var index = (parseInt(i)+1);
						if($("#index_"+index).innerText!=index){
							if(data[i].fail_reason== "1"){
								$("#historyTable").append("<tr line-height='15px'align='center' ><td align='left'>实名制身份信息有误</td><td align='left'>"
								+data[i].description+"</td></tr>");
							}else if(data[i].fail_reason=="2"){
								$("#historyTable").append("<tr line-height='15px'align='center' ><td align='left'>身份信息已使用</td><td align='left'>"
								+data[i].description+"</td></tr>");
							}else if(data[i].fail_reason=="6"){
								$("#historyTable").append("<tr line-height='15px'align='center' ><td align='left'>用户取回</td><td align='left'>"
								+data[i].description+"</td></tr>");
							}else{
								$("#historyTable").append("<tr line-height='15px'align='center' ><td align='left'>null</td><td align='left'>"
								+data[i].description+"</td></tr>");
							}
							
						}
					}
				}
			});
	 		innerHTML = historyDiv.innerHTML; 
		    style.display = "block"; 
		    heightDiv = heightDiv + 106;//106为div中表格边距以及表头的高度86+20
		    style.pixelLeft = window.event.clientX + window.document.body.scrollLeft - 750;   
		    style.pixelTop = window.event.clientY + window.document.body.scrollTop - heightDiv;   
	    }   
	}   
	//鼠标离开“明细”，隐藏该订单的操作日志
	function hidediv(){   
	    var oSon = window.document.getElementById("hint");   
	    if(oSon == null) return;   
	    oSon.style.display="none";   
	}
			
		function exportExcel() {
			$("form:first").attr("action","/register/exportexcel.do");
			$("form:first").submit();
			$("form:first").attr("action","/register/queryRegisterList.do");
		}


	function selectAll(){
		var checklist = document.getElementsByName("regist_status");
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
		<style>
		#hint{width:700px;border:1px solid #C1C1C1;background:#F0FAC1;position:fixed;z-index:9;padding:6px;line-height:17px;text-align:left;overflow:auto;} 
		
		</style>
		
</head>
	<body><div></div>
		<div class="book_manage oz">
			<form action="/register/queryRegisterList.do" method="post" enctype="multipart/form-data">
			<div style="border: 0px solid #00CC00; margin: 10px;">
				<ul class="order_num oz" style="margin-top:10px;">
					<li>
						姓&nbsp;&nbsp;&nbsp;&nbsp;名&nbsp;&nbsp;
						<input type="text" class="text" name="user_name" value="${user_name}"/>
					</li>
					<li>
						身份证号&nbsp;&nbsp;
						<input type="text" class="text" name="ids_card" value="${ids_card}"/>
					</li>
					<li>
						联系电话&nbsp;&nbsp;
						<input type="text" class="text" name="user_phone" value="${user_phone}"/>
					</li>
				</ul>
				<ul class="order_num oz" style="margin-top:10px;">
					<li>12306账号&nbsp;
						<input type="text" class="text" name="account_name" value="${account_name}"/>
					</li>
					<li>
						代理商账号
						<input type="text" class="text" name="user_id" value="${user_id}"/>
					</li>
					<li>
						邮箱查询&nbsp;&nbsp;
						<input type="text" class="text" name="mail" value="${mail}"/>
					</li>
				</ul>
				<ul class="order_num oz" style="margin-top:10px;">
					<li>开始时间&nbsp;&nbsp;
						<input type="text" class="text" name="start_create_time" readonly="readonly" value="${start_create_time}"
										onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>
					<li>结束时间&nbsp;&nbsp;
						<input type="text" class="text" name="end_create_time" readonly="readonly" value="${end_create_time}"
										onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>
					<li>
						12306电话&nbsp;
						<input type="text" class="text" name="regist_phone" value="${regist_phone}"/>
					</li>
					
				</ul>
				<ul class="order_num oz" style="margin-top:10px;">
					<li>注册时间&nbsp;&nbsp;
						<input type="text" class="text" name="start_regist_time" readonly="readonly" value="${start_regist_time}"
										onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>
					<li>注册截止&nbsp;&nbsp;
						<input type="text" class="text" name="end_regist_time" readonly="readonly" value="${end_regist_time}"
										onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>
				</ul>
				
				<ul class="ser oz">
					<li>
						状态&nbsp;&nbsp;&nbsp;&nbsp;
					</li>
					<li>
					<input type="checkbox" onclick="selectAll()" name="controlAll" style="controlAll" id="controlAll"/><label for="controlAll">&nbsp;全部</label>
					</li>		
					<c:forEach items="${regist_status}" var="s" varStatus="index">
						<li>
							<input type="checkbox" id="regist_status${index.count }"
								name="regist_status" value="${s.key }"
								<c:if test="${fn:contains(StatusStr, s.key ) }">checked="checked"</c:if> />
							<label for="regist_status${index.count }">
								${s.value }
							</label>
						</li>
					</c:forEach>
				</ul>
				
				<ul class="ser oz">
					<li>
						来源&nbsp;&nbsp;&nbsp;&nbsp;
						<c:forEach items="${account_source}" var="s" varStatus="index">
							<li>
								<input type="checkbox" id="account_source${index.count }" name="account_source" value="${s.key }"
									<c:if test="${fn:contains(sourceStr, s.key ) }">checked="checked"</c:if> />
								<label for="account_source${index.count }">
									${s.value }
								</label>
							</li>
						</c:forEach>
					</li>
				</ul>
				<ul class="ser oz">
					<li>
						使用状态
						<c:forEach items="${is_output}" var="s" varStatus="index">
							<li>
								<input type="checkbox" id="is_output${index.count }" name="is_output" value="${s.key }"
									<c:if test="${fn:contains(is_outputStr, s.key ) }">checked="checked"</c:if> />
								<label for="is_output${index.count }">
									${s.value }
								</label>
							</li>
						</c:forEach>
					</li>
				</ul>
				<ul class="order_num oz">
						<li>
						邮箱类型：
						</li>
							<li>
								<input type="checkbox" id="mail_qita" name="mail_qita" value="mail_qita"
									<c:if test="${mail_qita ne '' }">checked="checked"</c:if> />
								<label for="mail_qita">
									其他
								</label>
							</li>
							<li>
								<input type="checkbox" id="mail_163" name="mail_163" value="mail_163"
									<c:if test="${mail_163 ne '' }">checked="checked"</c:if> />
								<label for="mail_163">
									163
								</label>
							</li>
							<li>
								<input type="checkbox" id="mail_19trip" name="mail_19trip" value="mail_19trip"
									<c:if test="${mail_19trip ne '' }">checked="checked"</c:if> />
								<label for="mail_19trip">
									19trip
								</label>
							</li>
							
				</ul>
				
				<br/>
				<p>
					<input type="submit" value="查 询" class="btn" />
					<%
						if ("2".equals(loginUserVo.getUser_level()) ) {
           			%>
           			<input type="button" value="导出Excel" class="btn" onclick="exportExcel()" />
					<input type="button" value="添 加" class="btn" id="addRegister" onclick="location.href = '/register/toAddRegister.do'" />
					<input type="submit" value="批量导入" class="btn btn_normal" onclick="queryList();"/>
					<%} %>
				</p>
			</div>
			<div id="hint" class="pub_con" style="display:none"></div>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #EAEAEA;">
							<th>
								NO
							</th>
							<th>
								姓名
							</th>
							<th>
								身份证号
							</th>
							<th>
								联系电话
							</th>
							<th>
								代理商账号
							</th>
							<th>
								12306账号
							</th>
							<th>
								12306密码
							</th>
							<th>
								12306电话
							</th>
							<th>
								邮箱
							</th>
							<th>
								邮箱密码
							</th>
							<th>
								创建时间
							</th>
							<th>
								来源
							</th>
							<th>
								操作人
							</th>
							<th>
								订单状态
							</th>
							<th>
								使用情况
							</th>
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${registerList}" varStatus="idx">
						<tr>
							<td>
								${idx.index+1}
							</td>
							<td>
								${list.user_name}
							</td>
							<td>
								${list.ids_card}
							</td>
							<td>
								${list.user_phone }
							</td>
							<td>
								${list.user_id}
							</td>
							<td>
								${list.account_name}
							</td>
							<td>
								${list.account_pwd}
							</td>
							<td>
								${list.regist_phone}
							</td>
							<td>
								${list.mail}
							</td>
							<td>
								${list.pwd}
							</td>
							<td>
								${fn:substringBefore(list.create_time, ' ')}
								<br />
								${fn:substringAfter(list.create_time, ' ')}
							</td>
							<td>
								${account_source[list.account_source] }
							</td>
							<td>
								${list.opt_person}
							</td>
							<td>
								${regist_status[list.regist_status] }
							</td>
							<td>${is_output[list.is_output] }</td>
							<td>
								<c:if test="${list.regist_status == 44 || list.regist_status == 55}">
									<a href="/register/queryRegisterInfo.do?regist_id=${list.regist_id }&isShow=0">人工处理</a>
								</c:if>	
								<c:if test="${list.regist_status == 33 || list.regist_status == 44}">	
								<a href="/register/queryRegisterInfo.do?regist_id=${list.regist_id}" onmouseover="showdiv('${list.regist_id}')" onmouseout="hidediv()">明细</a>
								</c:if>	
								<c:if test="${list.regist_status ne '33' && list.regist_status ne '44'}">	
								<a href="/register/queryRegisterInfo.do?regist_id=${list.regist_id}">明细</a>
								</c:if>	
							</td>
							</tr>
						</c:forEach>
					</table>
					<jsp:include page="/pages/common/paging.jsp" />
						<div id="historyDiv" style="display:none;">
						  	 <table class="pub_table" style="width: 680px; margin: 10px 10px;" id="historyTable">
								<tr>
								<th style="width: 150px;">失败原因</th>
								<th style="width: 150px;">人工注册说明</th>
								</tr>
							</table>
						</div> 
				</c:if>
			</form>
		</div>
	</body>
</html>
