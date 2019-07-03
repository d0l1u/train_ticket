<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>投诉建议页</title>
<link rel="stylesheet" href="/css/style.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
</head>

<body>
	<div class="content oz">
		<!--导航条 start-->
	    <div class="main_nav">
        	<ul class="oz">
            	<jsp:include flush="true" page="/pages/common/menu.jsp">
					<jsp:param name="menuId" value="complain" />
				</jsp:include>
            </ul>
            <div class="slogan"></div>
        </div>
        <!--导航条 end-->
    	<!--左边内容 start-->
    	<div class="left_con oz">
         	<div class="advice oz">
            	<h2>
                	<span class="nav_left_bg"></span>
                	<span class="nav_mid_bg">用户级别</span>
                	<span class="nav_right_bg"></span>
                </h2>
                <form action="/complain/addComplainInfo.jhtml" method="post">
                <div class="con1 oz">
                	<dl class="oz">
                    	<dt>选择问题的类型：</dt>
                        <dd>
                        	<select name="question_type" id="question_type">
                            	<option value="">请选择问题的类型</option>
                            	<c:forEach items="${questionTypeMap}" var="map">
                            		<option value="${map.key}"<c:if test="${!empty ques_Id and map.key eq ques_Id}">selected="selected"</c:if>>${map.value}</option>
                            	</c:forEach>
                            </select>
                        </dd>
                    </dl>
                    <dl class="oz">
                    	<dt class="dt_nr">内容：</dt>
                        <dd class="dd_jian">
                        
                            <textarea name="question" id="question" cols="30" rows="10">${question }</textarea> 
                            <span></span>
                        </dd> 
                    </dl>
                    <p>
                    	<input type="button" id="btnOK" value="提交" class="btn" />
                        <input type="reset" value="清空" class="btn" />
                    </p>
                </div>
                </form>
                <div class="con2 oz">
                	<h3>投诉与建议</h3>
                    <div class="ques oz">
                    	<c:forEach items="${complainList}" var="complain" varStatus="idx">
                    	  <c:if test="${idx.index + 1 != fn:length(complainList)}">
	                    	<dl class="oz">
	                      </c:if>
	                      <c:if test="${idx.index + 1 == fn:length(complainList)}">
	                    	<dl class="oz no_border">
	                      </c:if>
	                            <dt>
		                            <span class="time" style="float: right;">
		                            	<fmt:formatDate value="${complain.create_time}" pattern="yyyy-MM-dd" />
		                            </span>
		                            <span class="xuhao">
		                            	${idx.index+1}
		                            </span>
		                            <span>
		                            	${complain.question}
		                            </span>
	                            </dt>
	                            <dd>
		                            <span class="time" style="float: right;">
		                            	<fmt:formatDate value="${complain.reply_time}" pattern="yyyy-MM-dd" />
		                            </span>
	                           		<span style="display:inline-block;width:500px;">答：${complain.answer}</span>
	                            </dd>
	                        </dl>
                        </c:forEach>
                    </div>
                	
                </div>
            </div>
        
        	
        </div>
        <!--左边内容 end-->
        <!--右边内容 start-->
        <%@ include file="/pages/common/right.jsp"%>
    	<!--右边内容 end-->
    
    </div>
    <div id="tip" class="tip_wrap" style="display: none;"><div class="tip"><b></b><span class="errMsg"></span></div></div>
<script type="text/javascript" language="JavaScript">
	$().ready(function(){
		$("#btnOK").click(function(){
			if(!checkQuestionType() || !checkQuestion() || !checkDailyOnlyOne()){
				return;
			}else{
				$("form:first").submit();
			}
		});
		
		$("#question_type").change(function(){
			checkQuestionType();
		});
		
		$("#question").blur(function(){
			checkQuestion();
		});
		
	    function checkDailyOnlyOne(){
	    	var isOK = false;
			$.ajax({
				url:"/complain/queryTodayCan.jhtml",
				cache:false,
				async:false,
				type: "POST",
				data: "{}", 
				dataType: "text",
				success: function(res){
					if(res=="yes"){
						hideErrMsg("btnOK");
						isOK = true;
					}else{
						showErrMsg("btnOK", "250px", "Sorry，您今天已经提交过投诉建议了！");
						isOK = false;
					}
				},
				error: function(res){
                	isOK = false;
           		}
			}); 
			return isOK;
		}
		
		function checkQuestionType(){
			if($("#question_type").val()==""){
				showErrMsg("question_type", "120px", "请选择问题类型！");
				return false;
			}else{
				hideErrMsg("question_type");
				return true;
			}
		}
		
		function checkQuestion(){
			if($.trim($("#question").val())==""){
				showErrMsg("question", "100px", "请输入内容！");
				return false;
			}else if(DataLength($("#question").val())>50){
				showErrMsg("question", "100px", "内容不能超过50字！");
				return false;
			}else{
				hideErrMsg("question");
				return true;
			}
		}
		
		function DataLength(fData){
		    var intLength=0;
		    for (var i=0;i<fData.length;i++){
		        if ((fData.charCodeAt(i) < 0) || (fData.charCodeAt(i) > 255)){
		            intLength=intLength+2;
		        }else{
		            intLength=intLength+1;
		        }
		    }
		    return intLength;
		} 
		
		function showErrMsg(id, _width, msg){
			$("#"+id+"_errMsg").remove();
			var offset = $("#"+id).offset();
			$obj=$("#tip").clone().attr("id", id+"_errMsg")
				.css({'position':'absolute', 'top':offset.top-30, 'left':offset.left, 'width':_width}).appendTo("body");
			$obj.find(".errMsg").text(msg).end().show();
		}
		
		function hideErrMsg(id){
			$("#"+id+"_errMsg").remove();
		}
	});
</script>
</body>
</html>
