<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
  <head>
    <title>系统设置管理</title>
    <link rel="stylesheet" href="/css/back_style.css" type="text/css" />
	<script type="text/javascript" src="/js/jquery.js"></script>
	<script language="javascript" src="/js/layer/layer.js"></script>
	<script language="javascript" src="/js/mylayer.js"></script>
	<style>
		.outer{width:1000px; padding:0 20px;}
		.book_manage{width:1000px;margin:20px auto;}
		.book_manage table{border:1px solid #dadada;width:800px;text-align:center;}
		.book_manage th,.book_manage td,.book_manage tr{border:1px solid #dadada;}
		.book_manage th{height:30px;font:bold 13px/30px "宋体";}
		.book_manage td{line-height:20px;}
		td{padding: 5px 0;}
		.setting_text{width:220px;}
	</style>
	<script type="text/javascript">
		function changeCode01Weight() {
			var code01_weight = $("#code01_weight").val();
			if($.trim(code01_weight)==""){
				alert("打码团队01的权重值不能为空！");
				return;
			}
			if(confirm("确认修改吗?")) {
				$("form:first").attr("action", "/loginManager/updateCode01Weight.do?code01_weight=" + code01_weight);
				$("form:first").submit();
			}
		}

		function changeCode02Weight() {
			var code02_weight = $("#code02_weight").val();
			if($.trim(code02_weight)==""){
				alert("打码团队02的权重值不能为空！");
				return;
			}
			if(confirm("确认修改吗?")) {
				$("form:first").attr("action", "/loginManager/updateCode01Weight.do?code02_weight=" + code02_weight);
				$("form:first").submit();
			}
		}

		function changeCode03Weight() {
			var code03_weight = $("#code03_weight").val();
			if($.trim(code03_weight)==""){
				alert("打码团队03的权重值不能为空！");
				return;
			}
			if(confirm("确认修改吗?")) {
				$("form:first").attr("action", "/loginManager/updateCode01Weight.do?code03_weight=" + code03_weight);
				$("form:first").submit();
			}
		}

		function changeCode04Weight() {
			var code04_weight = $("#code04_weight").val();
			if($.trim(code04_weight)==""){
				alert("打码团队04的权重值不能为空！");
				return;
			}
			if(confirm("确认修改吗?")) {
				$("form:first").attr("action", "/loginManager/updateCode01Weight.do?code04_weight=" + code04_weight);
				$("form:first").submit();
			}
		}
		
	</script>
	
  </head>
  
  <body>
  	<div class="outer">
  	<div class="book_manage oz">
  	<form action="/loginManager/systemSetting.do" method="post">
  		
	  	<table>
  			<tr>
	    		<th style="width: 170px;">key</th>
	    		<th style="width: 300px;">内容</th>
	    		<th>操作</th>
	    	</tr>
	    	
    		<tr>
    			<td>打码团队01的权重值：</td>
    			<td><input type="text" name="code01_weight" id="code01_weight" value="${sysoInfo.code01_weight}"/></td>
    			<td><input type="button" class="btn" value="修改" onclick="changeCode01Weight()"/></td>
    		</tr>
	    	
	    	<tr>
    			<td>打码团队02的权重值：</td>
    			<td><input type="text" name="code02_weight" id="code02_weight" value="${sysoInfo.code02_weight}"/></td>
    			<td><input type="button" class="btn" value="修改" onclick="changeCode02Weight()"/></td>
    		</tr>
    		
    		<tr>
    			<td>打码团队03的权重值：</td>
    			<td><input type="text" name="code03_weight" id="code03_weight" value="${sysoInfo.code03_weight}"/></td>
    			<td><input type="button" class="btn" value="修改" onclick="changeCode03Weight()"/></td>
    		</tr>
    		
    		<tr>
    			<td>打码团队其他的权重值：</td>
    			<td><input type="text" name="code04_weight" id="code04_weight" value="${sysoInfo.code04_weight}"/></td>
    			<td><input type="button" class="btn" value="修改" onclick="changeCode04Weight()"/></td>
    		</tr>
		    	
	    </table>
	    <br/>
	    <p><!-- 
			<input type="button" value="操作日志" class="btn" id="rizhi" onclick="opt_rizhi()" /> -->
		</p>    
    </form>
    </div>
    </div>
  </body>
</html>
