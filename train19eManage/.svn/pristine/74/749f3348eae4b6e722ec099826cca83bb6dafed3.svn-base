<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>投诉建议管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script type="text/javascript">
		

	//鼠标悬浮于“明细”，显示该订单的操作日志
	var heightDiv = 0; 
	function showdiv(complain_id){  
	     var oSon = window.document.getElementById("hint");   
	     if (oSon == null) return;   
	     with (oSon){   
	 		 $.ajax({
				url:"/allComplain/queryOrderOperHistory.do?complain_id="+complain_id,
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
							$("#historyTable").append("<tr align='center'><td align='left' style='padding-left:10px;padding-right:10px;word-break:break-all;'>"+data[i].question+"</td><td align='left' style='padding-left:10px;padding-right:10px;'>"+data[i].our_reply+"</td></tr>");
							if(data[i].our_reply.length>44){
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
	
		function selectAll(){
		var checklist = document.getElementsByName("question_type");
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
	function selectAllChannel(){
		var checklist = document.getElementsByName("channel");
		if(document.getElementById("controlAllChannel").checked){
			for(var i=0; i<checklist.length; i++){
				checklist[i].checked = 1;
			}
		}else{
			for(var j=0; j<checklist.length; j++){
				checklist[j].checked = 0;
			}

		}
	}
	function selectAllReply(){
		var checklist = document.getElementsByName("reply_season");
		if(document.getElementById("controlAllReply").checked){
			for(var i=0; i<checklist.length; i++){
				checklist[i].checked = 1;
			}
		}else{
			for(var j=0; j<checklist.length; j++){
				checklist[j].checked = 0;
			}

		}
	}
	
	function selectAllPurview(){
		var checklist = document.getElementsByName("permission");
		if(document.getElementById("controlAllPurview").checked){
			for(var i=0; i<checklist.length; i++){
				checklist[i].checked = 1;
			}
		}else{
			for(var j=0; j<checklist.length; j++){
				checklist[j].checked = 0;
			}

		}
	}
	function gotoOrderLock1(complain_id){
		//ajax验证是否锁
		var url = "/allComplain/queryComplainIsLock.do?complain_id="+complain_id+"&version="+new Date();
			$.get(url,function(data){
			if(data != null && data != ""){
				var temp = data ;
				var str1 = temp.split("&") ;
				var str =str1[1]; 
				alert("此投诉建议已经锁定，锁定人为"+str);
				return;
			 }else{
				$("form:first").attr("action","/allComplain/queryComplainUpdate.do?complain_id="+complain_id);
				$("form:first").submit();
			 }
		});
	}
	function gotoOrderLock2(complain_id){
		//ajax验证是否锁
		var url = "/allComplain/queryComplainIsLock.do?complain_id="+complain_id+"&version="+new Date();
			$.get(url,function(data){
			if(data != null && data != ""){
				var temp = data ;
				var str1 = temp.split("&") ;
				var str =str1[1]; 
				alert("此投诉建议已经锁定，锁定人为"+str);
				return;
			 }else{
				$("form:first").attr("action","/allComplain/queryComplainInfo.do?complain_id="+complain_id);
				$("form:first").submit();
			 }
		});
	}
</script>
		<style>
td {
	padding: 5px 0;
}

.tit {
	background: #ddd;
}

.book_manage .ser_mingxi {
	width: 800px;
	margin: 0 auto;
	border: none;
}

.book_manage .ser_mingxi tr,.book_manage .ser_mingxi td {
	border: none;
	line-height: 30px;
}

.book_manage .ser_mingxi span {
	color: #f00;
}
#hint{width:700px;border:1px solid #C1C1C1;background:#F0FAC1;position:fixed;z-index:9;padding:6px;line-height:17px;text-align:left;overflow:auto;} 
</style>
	</head>
	<body><div></div>
		<div class="book_manage oz">
			<form action="/allComplain/queryComplainList.do" method="post">
			<div style="border: 0px solid #00CC00; margin: 10px;">
			
			<!--<dl class="oz" style="padding-top:0px;">
						<dt style="float:left;padding-left:40px;line-height:22px;">
							提问时间：&nbsp;
							<input type="text" class="text" name="create_time"
								readonly="readonly" value="${create_time }"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" id="defaultStartDate1"/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							解决时间：&nbsp;
							<input type="text" class="text" name="reply_time"
								readonly="readonly" value="${reply_time }"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
						</dt>
						</dl>  -->
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
				<dl class="oz" style="padding-top:10px;">
						<dt style="float:left;padding-left:40px;line-height:22px;">状&nbsp;&nbsp;&nbsp;&nbsp;态：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</dt>
						<dd  style="float:left;width:900px;">
							<div class="ser-item" style="float:left;white-space:nowrap;padding-right:20px;">
								<input type="checkbox" onclick="selectAll()" name="controlAll" style="controlAll" id="controlAll"/>
								<label for="controlAll">&nbsp;全部</label>
							</div>
						<c:forEach items="${questionType }" var="str" varStatus="index">
							<div class="ser-item" style="float:left;white-space:nowrap;padding-right:20px;">
								<input type="checkbox" id="question_type${index.count }" name="question_type" value="${str.key }" value="1"
									<c:if test="${fn:contains(question_typeStr, str.key ) }">checked="checked"</c:if> />
								<label for="question_type${index.count }">
									${str.value }
								</label>
							</div>
						</c:forEach>
						</dd>
					</dl>
					
					<dl class="oz" style="padding-top:10px;">
						<dt style="float:left;padding-left:40px;line-height:22px;">渠&nbsp;&nbsp;&nbsp;&nbsp;道：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</dt>
						<dd  style="float:left;width:900px;">
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="checkbox" onclick="selectAllChannel()" name="controlAllChannel" style="controlAllChannel" id="controlAllChannel"/>
								<label for="controlAllChannel">&nbsp;全部&nbsp;&nbsp;</label>
							</div>
						<c:forEach items="${questionChannel }" var="str" varStatus="index">
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="checkbox" id="channel${index.count }" name="channel" value="${str.key }" value="1"
									<c:if test="${fn:contains(question_channelStr, str.key ) }">checked="checked"</c:if> />
								<label for="channel${index.count }">
									${str.value }
								</label>
							</div>
						</c:forEach>
						</dd>
					</dl>
					<dl class="oz" style="padding-top:10px;">
						<dt style="float:left;padding-left:40px;line-height:22px;">处理状态：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</dt>
						<dd  style="float:left;width:900px;">
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="checkbox" onclick="selectAllReply()" name="controlAllReply" style="controlAllReply" id="controlAllReply"/>
								<label for="controlAllReply">&nbsp;全部&nbsp;&nbsp;</label>
							</div>
						<c:forEach items="${replySeason }" var="str" varStatus="index">
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="checkbox" id="reply_season${index.count }" name="reply_season" value="${str.key }" value="1"
									<c:if test="${fn:contains(reply_seasonStr, str.key ) }">checked="checked"</c:if> />
								<label for="reply_season${index.count }">
									${str.value }
								</label>
							</div>
						</c:forEach>
						</dd>
						</dl>
					<dl class="oz" style="padding-top:10px;">
						<dt style="float:left;padding-left:40px;line-height:22px;">可见范围：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</dt>
						<dd  style="float:left;width:900px;">
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="checkbox" onclick="selectAllPurview()" name="controlAllReply" style="controlAllPurview" id="controlAllPurview"/>
								<label for="controlAllPurview">&nbsp;全部&nbsp;&nbsp;</label>
							</div>
						<c:forEach items="${purview }" var="str" varStatus="index">
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="checkbox" id="permission${index.count }" name="permission" value="${str.key }" value="1"
									<c:if test="${fn:contains(permissionStr, str.key ) }">checked="checked"</c:if> />
								<label for="permission${index.count }">
									${str.value }
								</label>
							</div>
						</c:forEach>
						</dd>
						</dl>
					<br/>
				<p>
					<input type="submit" value="查 询" class="btn" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				</p>
				</div>
				 <div id="hint" class="pub_con" style="display:none"></div>
				<c:if test="${!empty isShowList}">
					<!--  <table class="ser_mingxi" style="width: 1000px; margin: 0 auto;">
						<tr>

							<td>
								全部:
								<span>${count }</span>条
							</td>
							<td>
								已经解答:
								<span>${answerCount }</span>条
							</td>
							<td>
								自己可见:
								<span>${selfLook }</span>条
							</td>
							<td>
								全部可见:
								<span>${allLook }</span>条
							</td>
							<td>
								订单问题:
								<span>${order_question }</span>条
							</td>
							<td>
								出票问题:
								<span>${acquire_question }</span>条
							</td>
							<td>
								业务建议:
								<span>${operation_advice }</span>条
							</td>
							<td>
								其他：
								<span>${other_advice }</span>条
							</td>
						</tr>
					</table>-->
					<table>
						<tr style="background: #f0f0f0;">
						
							<th>
								序号
							</th>
							<th>
								用户账号
							</th> 
							<th>
								渠道
							</th>  
							<th>
								问题类型
							</th>
							<th>
								可见范围
							</th>
							<th>
								提问时间
							</th>
							<th>
								解决时间
							</th>
							<th>
								处理结果
							</th>
							<th>
								操作人
							</th>
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${allComplainList}" varStatus="idx">
						<c:choose>
								<c:when test="${fn:contains('1', list.permission )}">
									<tr style="background: #ffecf5;">
								</c:when>
								<c:otherwise>
									<tr>
								</c:otherwise>
							</c:choose>
							
								<td>
									${idx.index+1}
								</td>
								<td>
								<c:if test="${list.channel eq '19e' }">${list.agent_id }</c:if>
								<c:if test="${list.channel ne '19e' }">${list.user_id }</c:if>	
								</td>
								<td>
									${questionChannel[list.channel] }
								</td>
								<td>
									${questionType[list.question_type] }
								</td>
								<td>
									${purview[list.permission] }
								</td>
								<td>
									${list.create_time }
								</td>
								<td>
									${list.reply_time  }
								</td>
								
								<td>
									${replySeason[list.reply_season] }
								</td>
								
								<td>
									${list.opt_person }
								</td>
								<td>
									<span>
									<a  href="javascript:void(0)" onclick="gotoOrderLock2('${list.complain_id}');" onmouseover="showdiv('${list.complain_id}')" onmouseout="hidediv()" >明细</a> 
								<!--  	<a href="javascript:gotoOrderLock('${list.complain_id}');">明细</a>-->
									<a  href="javascript:gotoOrderLock1('${list.complain_id}');">修改</a>
									</span>
									  <% LoginUserVo loginUserVo = (LoginUserVo) request.getSession()
                     .getAttribute("loginUserVo");
                               if ("2".equals(loginUserVo.getUser_level())){
                               %>
									<span><a href="/allComplain/deleteComplain.do?complain_id=${list.complain_id}" 
										onclick="return confirm('确认删除么？')">删除</a></span>
										 <%
                               }
                               %>
								</td>
							</tr>
						</c:forEach>
					</table>
					<jsp:include page="/pages/common/paging.jsp" />
					<div id="historyDiv" style="display:none;">
						<table class="pub_table" style="width: 680px; margin: 10px 10px;" id="historyTable">
							<tr>
								<th style="width: 340px;">问题内容</th>
								<th style="width: 340px;">解答内容</th>
							</tr>
						</table>
					</div>
				</c:if>

			</form>
		</div>
	</body>
</html>
