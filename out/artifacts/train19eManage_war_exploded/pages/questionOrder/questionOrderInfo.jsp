<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>问题订单详细页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	    <script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
	    <script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
	    <script type="text/javascript">
	    var checkSubmitFlg = false;//设置全局变量，只允许表单提交一次
			function checkSubmit() {
			   if (checkSubmitFlg == true) {
			        return false;
			   }
			   checkSubmitFlg = true;
			   return true;
			}
			document.ondblclick = function docondblclick() {
			    window.event.returnValue = false;
			}
			document.onclick = function doconclick() {
			   if (checkSubmitFlg) {
			       window.event.returnValue = false;
			   }
			}
	
	    	function submitForm(question_id,question_seq){
				if($.trim($("#question_theme").val())==""){
					$("#question_theme").focus();
					alert("主题不能为空！");
					return;
				}
				$("form:first").attr("action","/questionOrder/updatetrain_question_order.do?question_id=" + question_id+"&question_seq="+question_seq  );
				$("#updateForm").submit();
			}
			
			//解决问题
			function updateQuestionStatus(question_id,question_seq,question_status) {
				var pageIndex=$("#pageIndex").val();
				var url ="/questionOrder/updateQuestionStatus.do?pageIndex="+pageIndex+
					"&question_id="+question_id+"&question_seq="+question_seq+"&question_status="+question_status+"&version="+new Date();
				 	  $.get(url,function(data){
							if(data == "true"){
							
								$("form:first").attr("action","/questionOrder/gototrain_question_order.do" );
								$("form:first").submit();
								}
						});
			}
 		 </script>
	<script>
	</script>
  </head>
  	
  <body>
  <div style="margin: 50px;">
  <div class="question" style="width:700px;margin-top:10px;background:#f0f0f0;float:none;margin-bottom:14px; font: 14px/35px '宋体';">
	<form action="/questionOrder/updatetrain_question_order.do" method="post" name="updateForm" id="updateForm" onsubmit="return checkSubmit();">
        <ul class="order_num oz">
        	<li>订单号：&nbsp;<input type="text" name="question_order_id" id="question_order_id" value="${questionOrderInfo.question_order_id }" style="width: 200px;" />
        	<input type="hidden" name="pageIndex" id="pageIndex" value="${pageIndex }"/>
        	</li>
        	<li>标&nbsp;&nbsp;题：&nbsp;<input type="text" name="question_theme" id="question_theme" value="${questionOrderInfo.question_theme }" style="width: 200px;" />
        	</li>
        	<li>指定人：&nbsp;<input type="text" name="question_assigner" id="question_assigner" value="${questionOrderInfo.question_assigner }" style="width: 200px;" />
        	</li>
        	<li>
        	<span style="display:inline-block; height:100px; text-align:top; float:left">详&nbsp;&nbsp;细：
        	</span>
        	<textarea rows="9" cols="50" name="question_desc" id="question_desc"  style="border:1px solid #A3B083; resize:none;">${questionOrderInfo.question_desc }</textarea>
        		<c:if test="${amount eq 1}">
        		<a href="javascript:location='/questionOrder/pictureDown.do?question_id=${questionOrderInfo.question_id }&pic=0'">
        		<font color="red"> 下载图片1</font></a>
        		</c:if>
        		<c:if test="${amount eq 2}">
        		<a href="javascript:location='/questionOrder/pictureDown.do?question_id=${questionOrderInfo.question_id }&pic=0'">
        		<font color="red"> 下载图片1</font></a>
        		<a href="javascript:location='/questionOrder/pictureDown.do?question_id=${questionOrderInfo.question_id }&pic=1'">
        		<font color="red"> 下载图片2</font></a>
        		</c:if>
        		<c:if test="${amount eq 3}">
        		<a href="javascript:location='/questionOrder/pictureDown.do?question_id=${questionOrderInfo.question_id }&pic=0'">
        		<font color="red"> 下载图片1</font></a>
        		<a href="javascript:location='/questionOrder/pictureDown.do?question_id=${questionOrderInfo.question_id }&pic=1'">
        		<font color="red"> 下载图片2</font></a>
        		<a href="javascript:location='/questionOrder/pictureDown.do?question_id=${questionOrderInfo.question_id }&pic=2'">
        		<font color="red"> 下载图片3</font></a>
        		</c:if>
        	</li>
        	<li>
        	<span style="display:inline-block; height:100px; text-align:top; float:left">答&nbsp;&nbsp;复：</span>
        	<textarea rows="9" cols="50" name="question_reply" id="question_reply"  style="border:1px solid #A3B083; resize:none;">${questionOrderInfo.question_reply }</textarea>
        	</li>
        </ul>
        </form>
     
        <div class="book_manage oz">
        <p><input type="button" value="回 复" class="btn" id="btnSubmit" onclick="submitForm('${questionOrderInfo.question_id}','${questionOrderInfo.question_seq}')"/>
           <input type="button" value="返 回" class="btn" onclick="javascript:history.back(-1);"/>
           <c:if test="${questionOrderInfo.question_status eq '11' || questionOrderInfo.question_status eq '33'}">
			<input type="button" value="已解决" class="btn" id="updateQuestionStatus" onclick="updateQuestionStatus('${questionOrderInfo.question_id}','${questionOrderInfo.question_seq}','22')"/>
			</c:if>
			<c:if test="${questionOrderInfo.question_status eq '22'|| questionOrderInfo.question_status eq '44'}">
			<input type="button" value="问题重现" class="btn" id="updateQuestionStatus" onclick="updateQuestionStatus('${questionOrderInfo.question_id}','${questionOrderInfo.question_seq}','33')"/>
			</c:if> 
		</p>
          </div>
       </div>
    <div class="question" style="width:700px;margin-top:10px;border:1px solid #dadada;">
						<h4>
							历史操作
						</h4>
						<div class="pub_con">
							<table class="pub_table" style="width: 650px; margin: 20px 20px;">
									<tr>
										<th style="width: 50px;">
											序号
										</th>
										<th style="width: 400px;">
											操作日志
										</th>
										<th style="width: 100px;">
											操作时间
										</th>
										<th style="width: 100px;">
											操作人
										</th>
									</tr>
								<c:forEach var="hs" items="${history}" varStatus="idx">
									<tr align="center">
										<td>
											${idx.index+1 }
										</td>
										<td align="left" style="word-break:break-all;width: 300px; ">
											${hs.content}
										</td>
										<td>
											${hs.create_time }
										</td>
										<td>
											${hs.opt_person }
										</td>
									</tr>
								</c:forEach>
							</table>
						</div>
				</div>
	</div>
  </body>
</html>
