<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>页面打码</title>
<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
<link rel="stylesheet" href="/css/12306/layout.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" type="text/javascript" src="/js/jquery.validate.js"></script>
<script language="javascript" type="text/javascript" src="/js/jquery.metadata.js"></script>
<script type="text/javascript" language="Javascript">
$(document).ready(function(){
	//如果是开始取码状态，则计数运行
	if("start"==$("#status").val()){
		if($("#pic_id").val()==""){
			$("#codeimg").attr("style","background-image:url(/images/loading_code.gif)");
		}
		//倒计时开始
		secondCounter(15);
		$("#startAndStop").val("暂停");
	}else{
		$("#startAndStop").val("开始");
	}
	
	var pic_url = '${picture.pic_filename}';
	if(pic_url!=""){
		$("#codeimg").attr("style","background-image:url("+pic_url+")");
	}
	$(".img_on").click(function(){
		if(pic_url==""){
			return false;
		}
		
		var result = $("#result").val();
		var num = $(this).attr("id").split("_")[1];

		var id = $(this).attr("id");
		
		if(result.indexOf(num) != -1){
			result = result.replace(num+"","");
			$("#result").val(result);
			//删除对勾
			$("#"+id +" img").remove();
		}else{
			result = result+""+num;
			$("#result").val(result);
			//添加对勾
			$("#"+id).html("<img src=\"/images/correct.png\">");
		}
	})


	$("#startAndStop").click(function(){
		var value = $("#status").val();
		if(""==value || "stop"==value){
			$("#status").val("start");
			var url = "/web/startCodePage.do?status=start&pic_id="+$("#pic_id").val();
			window.location.href=url;
		}else{
			$("#status").val("stop");
			var url = "/web/startCodePage.do?status=stop";
			window.location.href=url;
		}
	})


	function playCodeSubmit(){
		$.ajax({
			url:"/web/submitCode.do?pic_id="+$("#pic_id").val()+"&result="+$("#result").val(),
			type: "POST",
			cache: false,
			async: true,
			dataType: "text",
			success: function(data){
				
			}
		});
	}

});
function secondCounter(defSec){
	$("#limit_time").html(defSec--);
	/*
	if($("#pic_id").val()=="" && defSec<=5){
		var url="/web/startCodePage.do?status=start&pic_id="+$("#pic_id").val();
		window.location.href=url;
	}
	*/
	if(defSec<=0){
		var noplaycode = $("#noplaycode").val();
		if(noplaycode==0){
			noplaycode = 1;
			var url="/web/startCodePage.do?status=start&pic_id="+$("#pic_id").val()+"&noplaycode="+noplaycode;
			window.location.href=url;
		}else{
			var url = "/web/startCodePage.do?status=stop";
			window.location.href=url;
		}
	}else{
		window.setTimeout("secondCounter("+defSec+")",1000);
	}
}

function submitForm(){
	if($("#result").val()==""){
		return;
	}else{
		document.updateForm.submit();
	}
}
//回车提交打码结果
document.onkeydown=function(event){
	e = event ? event:(window.event ? window.event : null);
	if(e.keyCode==13 || e.keyCode==32){
		submitForm();
	}
}

</script>
</head>

<body>
	<div style="width:expression(document.body.clientWidth + 'px')">
		<div class="play_manage account_manage oz" style="width:700px;">
		        <span style="color:red; font-size:30px;" id="limit_time"></span>
			    <div id="container">
				    <form action="/web/submitCode.do" method="post" name="updateForm">
						<input id="noplaycode" name="noplaycode" type="hidden" value="${noplaycode}"/>
				        <input id="status" name="status" type="hidden" value="${status}"/>
				        <input id="result" name="result" type="hidden"/>
				        <input id="pic_id" name="pic_id" type="hidden" value="${picture.pic_id}"/>
				    	<div id="playCard">
				        	<div id="codeimg" style="background-image:url('/images/start_code.jpg')">
					        	<div id="header"></div>
					        	<div id="topContent">
					        		<div id="num_1" class="odd-content img_on""></img></div>
					        		<div id="num_2" class="even-content img_on"></div>
					        		<div id="num_3" class="odd-content img_on"></div>
					        		<div id="num_4" class="even-content img_on"></div>
					        	</div>
					        	<div id="botContent">
					        		<div id="num_5" class="even-content img_on"></div>
					        		<div id="num_6" class="odd-content img_on"></div>
					        		<div id="num_7" class="even-content img_on"></div>
					        		<div id="num_8" class="odd-content img_on"></div>
					        	</div>
					        </div>
					        <div>
					        	<p style="margin:0 auto; padding-top:10px;">
					        		<input type="button" value="" class="btn" id="startAndStop"/>
					        		<input type="button" value="提 交" class="btn" onClick="submitForm();"/>
						        </p>
					        
					        </div>
				        </div>
				 </form>
			        <div id="dataShow">
			        	<ul>
			        		<li><span>今日打码总数：</span>${user_total_num}</li>
			        		<li><span>今日打码正确个数：</span>${user_right_num}</li>
			        		<li><span>今日打码错误个数：</span>${user_error_num}</li>
			        		<br/>
			        		<li><span>打码人员：</span></li>
			        		<li><span>未打码图片：</span></li>
			        	</ul>
			        </div>
		        </div>
		        
		</div>
	</div>
</body>

</html>
