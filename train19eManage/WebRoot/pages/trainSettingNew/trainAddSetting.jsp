<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>增加系统管理</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	    <script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
	    <script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
	    <script type="text/javascript">
	    	//返回
			function goback(){
				$("form:first").attr("action","/trainSetting/trainSettingPage.do");
				$("form:first").submit();
			}
			//默认选择 文本格式 选项隐藏
			$().ready(function(){
				 document.getElementById('chooselist').style.display = "none";
				 document.getElementById('type3').checked = 1;
			});
			
			//选择单选或多选显示选项
			function changeType(){
				var value="";
				$("input[name='show_type']:radio:checked").each(function(){ 
					 value = $(this).val();
					 if(value=="radio" || value == "checkbox"){
					 	document.getElementById('chooselist').style.display = "block";
					 }else{
					 	document.getElementById('chooselist').style.display = "none";
					 }
				});
			}
			
			//提交
			function submitForm(){
				if(document.getElementById("show_name").value ==""){
					alert("简称不能为空!");
					return;
				}
				if(document.getElementById("setting_name").value ==""){
					alert("setting_name不能为空!");
					return;
				}
				if(document.getElementById("setting_value").value ==""){
					alert("setting_value不能为空!");
					return;
				}
				if(document.getElementById("setting_desc").value ==""){
					alert("setting_desc不能为空!");
					return;
				}
				var ListValue= true;
				$("input[name='show_type']:radio:checked").each(function(){ 
					 var value = $(this).val();
					 if(value=="radio" || value == "checkbox"){
						var list_name = document.getElementsByName("show_list_name");
						var list_value = document.getElementsByName("show_list_value");
						//alert(isRepeat(list_name)+"#######"+isRepeat(list_value));
						if(isRepeat(list_name) || isRepeat(list_value)){
							alert("选项值名字和值不能重复！");
							ListValue= false;
						}
						for(var m=0;m<list_name.length;m++){
							if(list_name[m].value == ""||list_value[m].value == ""){
							alert("单选或多选时，选项值不能为空!");
							ListValue= false;
							break;
							}
						}
					 }
				});
				if(ListValue){//alert(ListValue);
				$("form:first").submit();
				}
			}
			
			//检查是否有重复数据
			function isRepeat(list){
			 	var arr = [];
				 for(var j=0;j<list.length;j++){
					arr.push(list[j].value);
				 }
				// alert(arr);
				var hash = {};
				for(var i in arr) {
				if(hash[arr[i]])
				return true;
				hash[arr[i]] = true;
				}
			return false;
			}
			
			//增加一行选项
			function addShowList(){
			var height = document.getElementsByName("show_list_name").length+1;
			var num = height+generateMixed(5);   //id获取随机数
			if(height<9){
				$("<input name=\"show_list_name\" class=\"inptxt pad30 width80\"  id=\"show_list_name_"+num+"\"  />").appendTo("#showList").show();
				$("<input class=\"inptxt pad30 width80\" name=\"show_list_value\" id=\"show_list_value_"+num+"\" style=\"margin-left:6px;\" />").appendTo("#showList").show();
				$("<span id=\"delect_"+num+"\" onclick=\"deleteShowList('"+num+"')\"><img src=\"/images/redCha.png\" style=\"margin-left:6px;\"  /><br/></span>").appendTo("#showList").show();
				
				//每添加一行增加40px整体高度
				document.getElementById("chooselist").style.height=40*height+"px"; 
				}else{
					alert("选项最多可添加8个！已达上限。");
				}
			 var list = document.getElementsByName("show_list_name");
				 if(list.length>1){
				  	 list[1].style.marginLeft="4px";//行对齐
				 }		
			}
			
			//删除一行选项
			function deleteShowList(id){
			$("#show_list_name_"+id).remove();
			$("#show_list_value_"+id).remove();
			$("#delect_"+id).remove();
			var height = document.getElementsByName("show_list_name").length;
			document.getElementById("chooselist").style.height=40*height+"px"; 
			 var list = document.getElementsByName("show_list_name");
				 if(list.length>1){
				  	 list[1].style.marginLeft="4px";//行对齐
				 }
			}
			
			//随机数
			var chars = ['0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'];
			function generateMixed(n) {
			     var res = "";
			     for(var i = 0; i < n ; i ++) {
			         var id = Math.ceil(Math.random()*35);
			         res += chars[id];
			     }
			     return res;
			}
			
						
	    </script>
	    <style>
	    	body{background: #fff;}
	    	.book_manage { width:97%;margin:10px auto;margin-top: 30px;}
	    	.info{position: relative;line-height: 30px;margin-bottom: 5px;height: 30px;margin-top: 30px;}
	    	.info .lable {text-align: right;line-height: 30px;width: 225px;padding-right: 5px;float: left;height: 30px;font:normal 15px "宋体";padding-top: 10px;}
	    	.inptxt{border-bottom: #cfcdc7 1px solid;border-left: #cfcdc7 1px solid;padding-bottom: 5px;line-height: 30px;padding-left: 5px;
	    	padding-right: 0px;height: 30px; border-top: #cfcdc7 1px solid;border-right: #cfcdc7 1px solid;width: 400px;}
	    	.con{float: left;position: relative;font:normal 15px "宋体";}
	    	.con .lable2{font:normal 15px bold;color: grey;}
	    	.inpradio{padding-top: 10px;}
	    	.height100{height: 100px;}
	    	.fanhui{position: relative;margin-top: 100px;margin-left: 30%;}
	    	.pad30{margin-top: 5px;}
	    	.width80{width: 80px;}
	    	
		</style>
	</head>
	<body>
	<form action="/trainSetting/addsetting.do" method="post" name="myform" id="myform">
		<div class="book_manage oz">
					<div class="info">
						<span class="lable">简称：</span>
						<div class="con">
							<input type="text" class="inptxt" name="show_name" id="show_name"/><span class="lable2">（页面显示简称）</span>
						</div>
					</div>
					<div class="info">
						<span class="lable">setting_name：</span>
						<div class="con">
							<input type="text" class="inptxt" name="setting_name" id="setting_name"/><span class="lable2">（唯一标识）</span>
							
						</div>
					</div>
					<div class="info">
						<span class="lable">类型：</span>
						<div class="con">
							<input type="radio"  id="type3" name="show_type"  class="inpradio" onclick="changeType()" value="text"/>文本
							<input type="radio"  id="type1" name="show_type"  class="inpradio" onclick="changeType()"  value="radio""/>单选
							<input type="radio"  id="type2" name="show_type"  class="inpradio" onclick="changeType()"  value="checkbox"/>多选
						</div>
					</div>
					<div class="info" id="chooselist"  style="display:block;">
						<span class="lable">选项：</span>
						<div class="con">
							<span id="showList">
							<input type="text" class="inptxt pad30 width80" name="show_list_name" id="show_list_name_1"/>
							<input type="text" class="inptxt pad30 width80" name="show_list_value" id="show_list_value_1"/>
							<input type="button" value="增加选项" onclick="addShowList();"/>
							<span class="lable2">
							（例：【选项 -- aa】，填入[选项] [aa]。）</span>
							<br/>
							</span>
						</div>
					</div>
					<div class="info">
						<span class="lable">setting_value：</span>
						<div class="con">
							<input type="text" class="inptxt" name="setting_value" id="setting_value"/>
						</div>
					</div>
					<div class="info">
						<span class="lable">描述：</span>
						<div class="con">
							<input type="text" class="inptxt height100" name="setting_desc" id="setting_desc"/>
						</div>
					</div>
					<div class="fanhui">
					<p><br />
						<input type="button" value="提交" class="btn" onclick="submitForm();"/>
						<input type="button" value="返回" class="btn" onclick="javascript:goback();"/>
					</p>
					</div>
					
		</div>
	</form>
	</body>
</html>