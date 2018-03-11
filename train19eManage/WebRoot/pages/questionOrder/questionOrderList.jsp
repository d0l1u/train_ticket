<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<%@ page import="com.l9e.transaction.vo.PageVo"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>问题订单页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	    <script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
	    <script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
	    <script type="text/javascript">
	    <%
		PageVo pageObject = (PageVo) request.getAttribute("pageBean");
		int pageIndex = pageObject.getPageIndex();%>
		
			 //修改
			function updatetrain_question_order(question_id,question_seq) {
				var statusStr = "";
						$("input[name='question_status']:checkbox:checked").each(function(){ 
							statusStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
						}); 
						statusStr = statusStr.substring(0, statusStr.length-1);
				$("form:first").attr("action","/questionOrder/turntoUpdateQuestionPage.do?pageIndex="+<%=pageIndex%>+"&question_id="+question_id+"&question_seq="+question_seq);
				$("form:first").submit();
			}
			//解决问题
			function updateQuestionStatus(question_id,question_seq,question_status) {
				var url ="/questionOrder/updateQuestionStatus.do?pageIndex="+<%=pageIndex%>+
					"&question_id="+question_id+"&question_seq="+question_seq+"&question_status="+question_status+"&version="+new Date();
				 	  $.get(url,function(data){
							if(data == "true"){
								$("form:first").submit();
								}
						});
			}
			 //创建问题
			function addQuestion() {
				$("form:first").attr("action","/questionOrder/turntoAddQuestionPage.do");
				$("form:first").submit();
			}
			 //删除
			function deletetrain_question_order(question_id,question_seq) {
			if(confirm("确认删除提交问题吗?")) {
				$("form:first").attr("action","/questionOrder/deletetrain_question_order.do?question_id="+question_id+"&question_seq="+question_seq);
				$("form:first").submit();
				}
			}
			//查询
			function submitForm(){
			$("form:first").attr("action","/questionOrder/gototrain_question_order.do");
			$("form:first").submit();
			}
			
			//鼠标悬浮于“明细”，显示该订单的操作日志
			var heightDiv = 0; 
			function showdiv(question_seq){  
			     var oSon = window.document.getElementById("hint");   
			     if (oSon == null) return;   
			     with (oSon){   
			 		 $.ajax({
						url:"/questionOrder/queryOrderOperHistory.do?question_seq="+question_seq,
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
									$("#historyTable").append("<tr line-height='15px'align='center' ><td id='index_'"+index+"''>"+index+
									"</td><td align='left' style='word-break:break-all;'>"+data[i].content+
									"</td><td>"+data[i].create_time+"</td><td>"+data[i].opt_person+"</td></tr>");
									if(data[i].content.length>44){
										heightDiv = heightDiv + 30;
									}else{
										heightDiv = heightDiv + 15;
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
			
			//全选
			function selectAll(){
				var checklist = document.getElementsByName("question_status");
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
	    .train_question_order{width:1000px;margin-top: 30px;margin-left: 10px;}
		.train_question_order table{border:1px solid #dadada;width:1000px;text-align:center;}
		.train_question_order th,.train_question_order td,.train_question_order tr{border:1px solid #dadada;}
		.train_question_order th{height:50px;font:bold 13px/30px "宋体";}
		.train_question_order td{line-height:40px;}
		#hint{width:700px;border:1px solid #C1C1C1;background:#F0FAC1;position:fixed;z-index:9;padding:6px;line-height:17px;text-align:left;overflow:auto;} 
		
		</style>
	</head>
	<body><div></div>
		<div class="book_manage oz">
			<form action="/questionOrder/gototrain_question_order.do" method="post" name="myform" id="myform">
			<div style="border: 0px solid #00CC00; margin: 10px;">
				<ul class="order_num oz" style="margin-top: 20px;">
						<li>
							提出时间：&nbsp;&nbsp;
							<input type="text" class="text" name="begin_answer_time"
								readonly="readonly" value="${begin_answer_time }"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" id="defaultStartDate1"/>
						
							至：
							<input type="text" class="text" name="end_answer_time"
								readonly="readonly" value="${end_answer_time }"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
						</li>
						<li>
							提出人：&nbsp;&nbsp;&nbsp;
							<input type="text" class="text" name="question_answer"
								value="${question_answer }" />
						</li>
						<li>
								订单号：&nbsp;&nbsp;&nbsp;
								<input type="text" class="text" name=question_order_id
									value="${question_order_id }" />
							</li>
						
				</ul>
					<ul class="order_num oz" style="margin-top: 10px;">
						<li>
							解决时间：&nbsp;&nbsp;
							<input type="text" class="text" name="begin_solve_time"
								readonly="readonly" value="${begin_solve_time }"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" id="defaultStartDate1"/>
						
							至：
							<input type="text" class="text" name="end_solve_time"
								readonly="readonly" value="${end_solve_time }"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
						</li>
						<li>
							解决人：&nbsp;&nbsp;&nbsp;
							<input type="text" class="text" name="question_solve"
								value="${question_solve }" />
						</li>
						<li>
								指定人：&nbsp;&nbsp;&nbsp;
								<input type="text" class="text" name="question_assigner"
									value="${question_assigner }" />
							</li>
					</ul>
						<dl class="oz" style="padding-top:20px;">
						<dt style="float:left;padding-left:40px;line-height:22px;">问题进度：&nbsp;&nbsp;</dt>
						<dd  style="float:left;width:910px;">
						<input type="checkbox" onclick="selectAll()" name="controlAll" style="controlAll" id="controlAll"/><label for="controlAll">全部</label>
						<c:forEach items="${statusList }" var="s" varStatus="index">
								<input type="checkbox" id="question_status${index.count }" name="question_status" value="${s.key }" value="1"
									<c:if test="${fn:contains(question_statusStr, s.key ) }">checked="checked"</c:if> />
								<label for="question_status${index.count }">
									${s.value }
								</label>
						</c:forEach>
						</dd>
					</dl>
			</div>
			<p>
			<input type="button" value="查 询" class="btn" onclick="submitForm();"/>
			<input type="button" value="创建问题" class="btn" onclick="addQuestion();"/>
			<span style="font-size:14px;padding-left:700px;">符合条件订单总数：<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${totalCount }</span>个</span>
			</p>
			<div id="hint" class="pub_con" style="display:none"></div>
			<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #f0f0f0;">
							<th>
								序号
							</th>
							<th>
								订单号
							</th>
							<th>
								问题描述
							</th>
							<th>
								问题状态
							</th>
							<th>
								提出人
							</th>
							<th>
								提出时间
							</th>
							<th>
								解决时间
							</th>
							<th>
								指定人
							</th>
							<th>
								解决人
							</th>
							<th>
								操作
							</th>
							
						</tr>
						
						<c:forEach var="list" items="${train_question_orderList}" varStatus="idx">
						<tr 
		            		<c:if test="${fn:contains('11,33', list.question_status )}">
		            		 	style="background:#E0F3ED; "
		            		</c:if>
		            		>
							<td>
								${idx.index+1}
							</td>
							<td>
								${list.question_order_id }
							</td>
							<td>
								${list.question_theme }
							</td>
							<td>
								${statusList[list.question_status] }	
							</td>
							<td>
								${list.question_answer }
							</td>
							<td>
								${list.question_answer_time }
							</td>
							<td>
								${list.question_solve_time }
							</td>
							<td>
								${list.question_assigner }
							</td>
							<td>
								${list.question_solve }
							</td>
							<td>
								<a  href="javascript:updatetrain_question_order('${list.question_id}','${list.question_seq }')" onmouseover="showdiv('${list.question_seq}')" onmouseout="hidediv()" >明细</a>
								<a  href="javascript:deletetrain_question_order('${list.question_id}','${list.question_seq }')">删除</a>
								<br/>
							<!--	<c:if test="${list.question_status eq '11' || list.question_status eq '33'}">
								<a  href="javascript:updateQuestionStatus('${list.question_id}','${list.question_seq }','22')">已解决</a>
								</c:if>
								<c:if test="${list.question_status eq '22'|| list.question_status eq '44'}">
								<a  href="javascript:updateQuestionStatus('${list.question_id}','${list.question_seq }','33')">问题重现</a>
								</c:if>  -->
							</td>
							</tr>
						</c:forEach>
					</table>
					<jsp:include page="/pages/common/paging.jsp" />
					<div id="historyDiv" style="display:none;">
					  	 <table class="pub_table" style="width: 680px; margin: 10px 10px;" id="historyTable">
						<tr>
						<th style="width: 30px;">序号</th>
						<th style="width: 450px;">操作日志</th>
						<th style="width: 130px;">操作时间</th>
						<th style="width: 70px;">操作人</th>
						</tr>
						</table>
					</div> 
					</c:if>
			</form>
		</div>
	</body>
</html>