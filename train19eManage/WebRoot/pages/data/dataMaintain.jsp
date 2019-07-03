<%@ page language="java" import="com.l9e.transaction.vo.SystemSettingVo" contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>数据维护管理</title>
    <link rel="stylesheet" href="/css/back_style.css" type="text/css" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="/js/jquery.js"></script>
	<script type="text/javascript" src="/js/artDialog.js"></script>
	<script type="text/javascript" src="/js/datepicker/WdatePicker.js"></script>
	<script type="text/javascript">
	$().ready(function(){
		$("#checkMaintainPwd").click(function(){
			$.ajax({
				url:"/data/checkThePwd.do?pwd="+$("#pwd_value").val(),
				type: "POST",
				cache: false,
				success: function(data){
					if(data=="true"){
						$("#dataMaintainPwd").hide();
						$("#dataMaintain").show();
						return true;
					}else{
						alert("非研发人员，请勿执行此操作！");
						$("#pwd_value").val("")
						return false;
					}
				}
			});
			
		});
	});

		function updatePriceData(){
			if(confirm("确认整合新票价表？")) {
				$("form:first").attr("action", "/data/updateNewDataPrice.do");
				$("form:first").submit();
			}
		}

		function updateTrainData(){
			if(confirm("确认整合新车次表？")) {
				$("form:first").attr("action", "/data/updateNewDataTrain.do");
				$("form:first").submit();
			}
		}

		function appendTrainData(){
			if(confirm("确认往新票价表拼接发车时间、到达时间、始发站、终点站等信息？")) {
				$("form:first").attr("action", "/data/appendNewDataPrice.do");
				$("form:first").submit();
			}
		}

		function conformOfficialPriceTable(){
			if(confirm("确认整合线上票价表（价格）？")) {
				$("form:first").attr("action", "/data/conformOfficialPriceTable.do");
				$("form:first").submit();
			}
		}
		function conformOfficialNameTimeTable(){
			if(confirm("确认整合线上票价表（站名、时间）？")) {
				$("form:first").attr("action", "/data/conformOfficialNameTimeTable.do");
				$("form:first").submit();
			}
		}
	</script>
	<style>
		.outer{
			width:1000px;
			padding:0 20px;
		}
		.book_manage{width:1000px;margin:20px auto;}
		.book_manage table{border:1px solid #dadada;width:800px;text-align:center;}
		.book_manage th,.book_manage td,.book_manage tr{border:1px solid #dadada;}
		.book_manage th{height:30px;font:bold 13px/30px "宋体";}
		.book_manage td{line-height:20px;}
		td{
			padding: 5px 0;
		}
		.setting_text{
			width:220px;
		}
	</style>
  </head>
  
  <body>
  	<div class="outer">
  	<div id="dataMaintainPwd" class="book_manage oz">
  		<table>
	    	<tr>
    			<td>请输入数据维护操作动态密码：</td>
    			<td>
    				<input type="text" id="pwd_value" value=""/>
    				<input type="button" class="btn" id="checkMaintainPwd" value="确认"/>
    			</td>
	    	</tr>
	    </table>
  	</div>
  	<div id="dataMaintain" class="book_manage oz" style="display:none">
  	<form action="/data/dataMaintrain.do" method="post">
  		<p style="color:red;align:center">相关操作请询问开发人员---zuoyuxing</p>
	  	<table>
	    	<tr>
    			<td>整合线上票价表数据（价格）：</td>
    			<td>
    				<input type="button" class="btn" name="maintain" value="更新" onclick="conformOfficialPriceTable()"/>
    			</td>
	    	</tr>
	    	<tr>
    			<td>整合线上票价表数据（站名、时间）：</td>
    			<td>
    				<input type="button" class="btn" name="maintain" value="更新" onclick="conformOfficialNameTimeTable()"/>
    			</td>
	    	</tr>
	    	
	    </table>
    </form>
    </div>
    </div>
  </body>
</html>
