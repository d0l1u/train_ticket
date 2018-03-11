<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="com.l9e.transaction.vo.SystemSettingVo" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>系统管理设置new</title>
      <%LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");%>
    <link rel="stylesheet" href="/css/back_style.css" type="text/css" />
	<script type="text/javascript" src="/js/jquery.js"></script>
	<script type="text/javascript" src="/js/datepicker/WdatePicker.js"></script>
	<script language="javascript" src="/js/layer/layer.js"></script>
	<script language="javascript" src="/js/mylayer.js"></script>
  	<script type="text/javascript">
  		
  		//查看操作日志
		function opt_rizhi(){
			var url="/trainSetting/queryHistory.do";
			showlayer('操作日志',url,'950px','600px')
		}
		//查看返回日志
		function return_log(){
			$("form:first").attr("action", "/trainSetting/gototrain_return_optlog.do" );
			$("form:first").submit();
		}
		//切换状态
		function changeStatus(setting_id,setting_status) {
			if(confirm("确认"+setting_status+"吗？")) {
				$("form:first").attr("action", "/trainSetting/changeSettingStatus.do?setting_id=" +setting_id);
				$("form:first").submit();
			}
		}
		//修改Value值
		function changeValue(setting_id,show_type,count) {
		var name = "setting_value_"+show_type+"_"+count;
		//alert(name);
		var setting_value = "";
		var type = "";
		if(show_type == "checkbox"){
			type ="切换";
			$("input[name="+name+"]:checkbox:checked").each(function(){ 
			 setting_value += $(this).val()+"@@";
			 });
		}else if(show_type == "radio"){
			type ="切换";
			$("input[name="+name+"]:radio:checked").each(function(){ 
			 setting_value = $(this).val();
			 });
		}else{
			type="修改";
			setting_value = document.getElementById(name).value;
		}
			if(confirm("确认"+type+"吗？")) {
			var action = "";
				if(setting_id != "TR0043" && setting_id != "TR0044"){
					action="/trainSetting/changeSettingValue.do?setting_id=" +setting_id+"&setting_value="+setting_value;
				}else{
					var person = document.getElementById("totalPerson_"+setting_id).value;
					if(setting_value >2*person ){
					alert("【预订】阀值不能超过打码人数两倍！");
					action = "/trainSetting/trainSettingList.do";
					}else{
					action = "/trainSetting/changeSettingValue.do?setting_id=" +setting_id+"&setting_value="+setting_value;
					}
				}
				if(action != ""){
				$("form:first").attr("action", action);
				$("form:first").submit();
				}
			}
		}
		
		//增加
		function turnToAddSetPage(){
			$("form:first").attr("action", "/trainSetting/turnToAddsettingPage.do" );
			$("form:first").submit();
		}
		
		function option_power(){
			var user_name = document.getElementById("user_name").value;
		}
		function design(){
			alert("暂未开通此功能，尽请期待！");
		}
		
		$().ready(function(){
			var url = "/remind/queryCodeCount.do?&version ="+new Date();
			$.get(url,function(data){
				var strJSON = data;//得到的JSON
				var obj = new Function("return" + strJSON)();//转换后的JSON对象
				document.getElementById("totalPerson_TR0043").value = obj.totalPerson;
				document.getElementById("totalPerson_TR0044").value = obj.totalPerson;
			});
		});
		
		function changeCodeSet(){
			var code01_weight = $("#code01_weight").val();
			if($.trim(code01_weight)==""){
				alert("打码团队01的权重值不能为空！");
				return;
			}
			var code02_weight = $("#code02_weight").val();
			if($.trim(code02_weight)==""){
				alert("打码团队02的权重值不能为空！");
				return;
			}
			var code03_weight = $("#code03_weight").val();
			if($.trim(code03_weight)==""){
				alert("打码团队03的权重值不能为空！");
				return;
			}
			var code04_weight = $("#code04_weight").val();
			if($.trim(code04_weight)==""){
				alert("打码团队04的权重值不能为空！");
				return;
			}
		 if(confirm("确认修改吗?")) {
				$("form:first").attr("action", "/trainSetting/updateCodeWeight.do");
				$("form:first").submit();
			}
		}
		
		function ShuaXin(){
		var url = "/remind/queryAccountCount.do?&version ="+new Date();
		$.get(url,function(data){
			var strJSON = data;//得到的JSON
			var obj = new Function("return" + strJSON)();//转换后的JSON对象
			document.getElementById("status_00").value = obj.status_00;
			document.getElementById("status_01").value = obj.status_01;
			document.getElementById("status_02").value = obj.status_02;
			document.getElementById("status_22").value = obj.status_22;
			document.getElementById("status_33").value = obj.status_33;
			document.getElementById("status_34").value = obj.status_34;
			});
		}
		$().ready(function(){
				ShuaXin();
			});	
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
		.btn2{background: #F67F2A;}
		.code_weight{width:50px;}
	</style>
  </head>
  
 <body>

  	<div class="outer">
  	<%
						if ("2".equals(loginUserVo.getUser_level()) ) {
           			%>
				<div style="margin-top:20px;margin-left: 10px;">
				获取成功【<input value="" id="status_00" readonly style="color:#f60;width:50px;border:0;text-align: right"/>】
				发送成功【<input value="" id="status_01" readonly style="color:#f60;width:50px;border:0;text-align: right"/>】
				获取验证码成功【<input value="" id="status_02" readonly style="color:#f60;width:50px;border:0;text-align: right"/>】
				验证成功【<input value="" id="status_22" readonly style="color:#f60;width:50px;border:0;text-align: right"/>】
				手机号已被验证【<input value="" id="status_33" readonly style="color:#f60;width:50px;border:0;text-align: right"/>】
				其他错误【<input value="" id="status_34" readonly style="color:#f60;width:50px;border:0;text-align: right"/>】
				</div>
				<%} %>
  	<div class="book_manage oz">
  	<form action="/trainSetting/trainSettingList.do" method="post">
  		<input type="hidden" value="<%=loginUserVo.getReal_name() %>" id="user_name"/>
	  	<table>
  		<tr>
  			<th>NO</th>
	    	<th style="width: 170px;">简称</th>
	    	<th style="width: 300px;">内容</th>
	    	<th>操作</th>
	    </tr>
	    <c:forEach items="${trainSetList}" var="ss" varStatus="idx">
	   	 <tr>
	   	 	 <td>${idx.count }</td>
		    <td>${ ss.show_name}：</td>
		     <td>
		     <c:choose>
		     	<c:when test="${ss.show_type eq 'radio'}">
		     		<c:forEach items="${ss.showList }" var="tt" varStatus="index">
			     	<input type="radio" name="setting_value_radio_${idx.count }" id="setting_value_radio_${idx.count }_${index.count }"
				    	<c:if test="${ss.setting_value eq tt.key}">
				    			checked
				    		</c:if>
				    	value="${tt.key}"/>
				    	<label for="value${index.count }">
							${tt.value }
						</label>
				    </c:forEach>
		     	</c:when>
		     	<c:when test="${ss.show_type eq 'checkbox'}">
		     		<c:forEach items="${ss.showList }" var="tt" varStatus="index">
			     	<input type="checkbox" name="setting_value_checkbox_${idx.count }"  id="setting_value_radio_${idx.count }_${index.count }"
				    	<c:if test="${fn:contains(ss.setting_value , tt.key)}">
				    			checked="checked"
				    		</c:if>
				    	value="${tt.key}"/>
				    	<label for="value${index.count }">
							${tt.value }
						</label>
				    </c:forEach>
		     	</c:when>
		     	<c:otherwise>
		     		<input type="text" id="setting_value_text_${idx.count }" name="setting_value_text_${idx.count }" value="${ss.setting_value}"/>
		     		<c:if test="${ ss.setting_id eq 'TR0043' || ss.setting_id eq 'TR0044'}">
		     		打码人数：<input value="" id="totalPerson_${ss.setting_id }" name="totalPerson" style="color:#f60;width:50px;border:0;"/>
		     		</c:if>
		     	</c:otherwise>
		     </c:choose>
		    </td>
		    <td>
		    <c:choose>
		     	<c:when test="${ss.show_type eq 'radio'  || ss.show_type eq 'checkbox' }">
		    	<input type="button" class="btn" value="切换" onclick="changeValue('${ss.setting_id}','${ss.show_type }','${idx.count }')"/>
		     	<input type="button" class="btn" value="${ss.setting_status }" onclick="changeStatus('${ss.setting_id}','${ss.setting_status }')"/>
		     	</c:when>
		     	<c:otherwise>
		    	<input type="button" class="btn" value="修改" onclick="changeValue('${ss.setting_id}','text','${idx.count }')"/>
		     	</c:otherwise>
		    </c:choose>
		    	<input type="button" class="btn btn2" value="编辑" onclick="design('${ss.setting_id}')"/>
		    </td>
		 </tr>	
	    </c:forEach>
	    </table>
	    <table  style="display:none;">
	    <tr>
  			<td>max</td>
	    	<td style="width: 170px;">打码器权重设置:</td>
	    	<td style="width: 300px;">
	    	01<input type="text" id="code01_weight" name="code01_weight" class="code_weight" value="${codeInfo.code01_weight}"/>
	    	02<input type="text" id="code02_weight" name="code02_weight" class="code_weight" value="${codeInfo.code02_weight}"/>
	    	03<input type="text" id="code03_weight" name="code03_weight" class="code_weight" value="${codeInfo.code03_weight}"/>
	    	qt<input type="text" id="code04_weight" name="code04_weight" class="code_weight" value="${codeInfo.code04_weight}"/>
	    	</td>
	    	<td style="width: 293px;">
	    	<input type="button" class="btn" value="修改" onclick="changeCodeSet();"/>
	    	</td>
	    </tr>
	    </table>
	    <br/>
	    <p>
	    	<input type="button" value="添  加" class="btn btn2" id="addSysSet" onclick="turnToAddSetPage();" />
			<input type="button" value="操作日志" class="btn" id="rizhi" onclick="opt_rizhi();" />
			<input type="button" value="查看返回日志" class="btn" id="fanhui" onclick="return_log();" />
		</p>    
    </form>
    </div>
    </div>
</body>
</html>
