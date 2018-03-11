<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>辛增竞价页</title>
<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script language="javascript" type="text/javascript" src="/js/jquery.validate.js"></script>
<script language="javascript" type="text/javascript" src="/js/jquery.metadata.js"></script>
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

		
	$().ready(function() { 
		$("#updateForm").validate(); 
	});
	function submitForm(){
		if($.trim($("#compete_time").val())==""){
			alert("竞价时段不能为空！");
			return;
		}
		var channelNum=0;
		$("input[name='compete_channel']:radio:checked").each(function(){ 
			channelNum++;
		});
		if(channelNum==0){
			alert("渠道不能为空！");
			return;
		}
		if($.trim($("#compete_money_1").val())=="" ||$.trim($("#compete_money_2").val())==""){
			alert("CDG竞价不能为空！");
			return;
		}
		if($.trim($("#compete_money_un_1").val())==""||$.trim($("#compete_money_un_2").val())==""){
			alert("非CDG竞价不能为空！");
			return;
		}
		if($.trim($("#compete_ranking_1").val())==""||$.trim($("#compete_ranking_2").val())==""){
			alert("CDG排名不能为空！");
			return;
		}
		if($.trim($("#compete_ranking_un_1").val())==""||$.trim($("#compete_ranking_un_2").val())==""){
			alert("非CDG排名不能为空！");
			return;
		}
		if($.trim($("#compete_top").val())==""){
			alert("CDG前五名不能为空！");
			return;
		}
		if($.trim($("#compete_top_un").val())==""){
			alert("非CDG前五名不能为空！");
			return;
		}
		
		$("#updateForm").submit();
	}

</script>
<style type="text/css">
table{width: 800px;line-height: 40px;border-collapse:collapse;}
table th,td{font-size:17px;font-family:"宋体";margin-left: 20px;}
</style>
</head>

<body>
	<div style="margin-top:100px;width:1000px;background-color: #dadada;">
	<div style="margin-left: 50px;">
		<form action="/compete/addCompete.do" method="post" name="updateForm" id="updateForm" onsubmit="return checkSubmit();">
	        <table>
	        	<tr>
	        		<td><b>添加竞价规则</b></td>
	        	</tr>
	        	<tr>
	        		<td colspan="3">竞价时段:&nbsp;&nbsp;
	        		<select id="compete_time" name="compete_time" style="width: 150px;">
	        			<option value="">请选择竞价时段</option>
	        			<option value="07:00-07:59">07:00-07:59</option>
	        			<option value="08:00-08:59">08:00-08:59</option>
	        			<option value="09:00-09:59">09:00-09:59</option>
	        			<option value="10:00-10:59">10:00-10:59</option>
	        			<option value="11:00-11:59">11:00-11:59</option>
	        			<option value="12:00-12:59">12:00-12:59</option>
	        			<option value="13:00-13:59">13:00-13:59</option>
	        			<option value="14:00-14:59">14:00-14:59</option>
	        			<option value="15:00-15:59">15:00-15:59</option>
	        			<option value="16:00-16:59">16:00-16:59</option>
	        			<option value="17:00-17:59">17:00-17:59</option>
	        			<option value="18:00-18:59">18:00-18:59</option>
	        			<option value="19:00-19:59">19:00-19:59</option>
	        			<option value="20:00-20:59">20:00-20:59</option>
	        			<option value="21:00-21:59">21:00-21:59</option>
	        			<option value="22:00-22:59">22:00-22:59</option>
	        		</select>
	        		</td>
	        	</tr>
	        	<tr>
	        		<td colspan="3">竞价渠道:&nbsp;&nbsp;&nbsp;&nbsp;
	        			<c:forEach items="${competeChannel}" var="d" varStatus="index">
							<input type="radio" id="compete_channel${index.count }"
								name="compete_channel" value="${d.key }"
								<c:if test="${fn:contains(compete_channel,d.key) }">checked="checked"</c:if> />
							<label for="compete_channel${index.count }">
								${d.value }
							</label>
						</c:forEach>
	        		</td>
	        	</tr>
	        	<tr>
	        	<td><font color="#2C99FF">19旅行</font></td>
	        	</tr>
	        	<tr>
	        		<td>CDG竞价:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	        		<input type="text" name="compete_money_1" id="compete_money_1" />
	        		</td>
	        		<td>非CDG竞价:&nbsp;&nbsp;&nbsp;&nbsp;
	        		<input type="text" name="compete_money_un_1" id="compete_money_un_1" />
	        		</td>
	        	</tr>
	        	<tr>
	        		<td>CDG排名:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	        		<input type="text" name="compete_ranking_1" id="compete_ranking_1" />
	        		</td>
	        		<td>非CDG排名:&nbsp;&nbsp;&nbsp;&nbsp;
	        		<input type="text" name="compete_ranking_un_1" id="compete_ranking_un_1" />
	        		</td>
	        	</tr>
	        	<tr>
	        		<td><font color="#2C99FF">九九商旅</font></td>
	        	</tr>
	        	<tr>
	        		<td>CDG竞价:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	        		<input type="text" name="compete_money_2" id="compete_money_2" />
	        		</td>
	        		<td>非CDG竞价:&nbsp;&nbsp;&nbsp;&nbsp;
	        		<input type="text" name="compete_money_un_2" id="compete_money_un_2" />
	        		</td>
	        	</tr>
	        	<tr>
	        		<td>CDG排名:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	        		<input type="text" name="compete_ranking_2" id="compete_ranking_2" />
	        		</td>
	        		<td>非CDG排名:&nbsp;&nbsp;&nbsp;&nbsp;
	        		<input type="text" name="compete_ranking_un_2" id="compete_ranking_un_2" />
	        		</td>
	        	</tr>
	        	<tr>
	        		<td colspan="3">CDG前五名:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	        		<input type="text" name="compete_top" id="compete_top" style="width: 80px;"/>
	        		<input type="text" name="compete_top" id="compete_top" style="width: 80px;"/>
	        		<input type="text" name="compete_top" id="compete_top" style="width: 80px;"/>
	        		<input type="text" name="compete_top" id="compete_top" style="width: 80px;"/>
	        		<input type="text" name="compete_top" id="compete_top" style="width: 80px;"/>
	        	</td>
	        	</tr>
	        	<tr>
	        		<td colspan="3">非CDG前五名:&nbsp;&nbsp;&nbsp;
	        		<input type="text" name="compete_top_un" id="compete_top_un" style="width: 80px;"/>
	        		<input type="text" name="compete_top_un" id="compete_top_un" style="width: 80px;"/>
	        		<input type="text" name="compete_top_un" id="compete_top_un" style="width: 80px;"/>
	        		<input type="text" name="compete_top_un" id="compete_top_un" style="width: 80px;"/>
	        		<input type="text" name="compete_top_un" id="compete_top_un" style="width: 80px;"/>
	        	</td>
	        	</tr>
       		</table>
         </form>
     </div>
        <div class="book_manage" style="margin-left: 30%;">
        <p><input type="button" value="提 交" class="btn" id="btnSubmit" onclick="submitForm()"/>
           <input type="button" value="返 回" class="btn" onclick="javascript:history.back(-1);"/></p>
        </div>
   
	</div>
</body>
</html>
