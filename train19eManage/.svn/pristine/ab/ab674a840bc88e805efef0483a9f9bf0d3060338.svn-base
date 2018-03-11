<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>创建问题订单页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	    <script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
	    <script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
			<script language="javascript" src="/js/imgPreview/CJL.0.1.min.js"></script>
		<script language="javascript" src="/js/imgPreview/QuickUpload.js"></script>
		<script language="javascript" src="/js/imgPreview/ImagePreviewd.js"></script>
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
	
	    </script>
	 <style>
	.perview {width:350px;background:#fff;font-size:12px; border-collapse:collapse;}
	.perview td, .perview th {padding:5px;border:1px solid #ccc;}
	.perview th {background-color:#f0f0f0; height:20px;}
	.perview a:link, .perview a:visited, .perview a:hover, .perview a:active {color:#00F;}
	.perview table{ width:100%;border-collapse:collapse;}
	/*file样式*/
	#idPicFile {
		width:80px;height:20px;overflow:hidden;position:relative;
	}
	#idPicFile input {
		font-size:20px;cursor:pointer;
		position:absolute;right:0;bottom:0;
		filter:alpha(opacity=0);opacity:0;
		outline:none;hide-focus:expression(this.hideFocus=true);
	}
	.order_num li{font-size:15px;}
	.order_num li span{font-size:15px;}
	</style>
  </head>
  
  <body>
  	<div style="background:#f0f0f0; margin: 100px;border:1px solid #A3B083;">
  		<br/><br/>
  		<div id="idPicFile" style="margin-left: 120px;">
  		<font color="#F4965D" size="3px;">添加图片</font>
  		
  		</div>
  		<div style="margin-left: 200px; margin-top:-15px;">
  		<c:if test="${question_pic ne ''}"><font color="red">已添加</font></c:if> 
  		</div>
		 <form action="/PicUpload" method="post" enctype="multipart/form-data" id="addForm" onsubmit="return checkSubmit();">
       	<div style="width:200px;margin-left: 200px;">
       	<table border="0" class="perview" id="question_pic">
	       	<thead style="display:none;" id="thead">
	       		
				<tr>
					<th>选择文件</th>
					<th width="50%">预览图</th>
					<th width="20%"> 操作 </th>
				</tr>
			</thead>
			<tbody id="idPicList">
				<tr>
					<td height="100"><input id="file1" name="file1" type="file"  /></td>
					<td align="center"></td>
					<td align="center"><a href="#">移除</a></td>
				</tr>
			</tbody>
		</table><input type="button" value="上传" id="upload" onclick="uploadForm()" style="display:none"/>
				<script>
		var table = $$("idPicList"), model = table.removeChild(table.rows[0]);
		var tChild=0;
		var unOnload="1";
		function addPic(){
		var file = document.createElement("input"),
		img = document.createElement("img"),
		ip = new ImagePreview( file, img, {
				maxWidth:	125,
				maxHeight:	100,
				action:		"",
				onErr:		function(){ alert("载入预览出错！"); ResetFile(file); },
				onCheck:	CheckPreview,
				onShow:		ShowPreview
			});
		file.type = "file"; file.name = "file1";
		file.onchange = function(){ ip.preview(); $$("thead").style.display="";$$("upload").style.display=""; };
		$$("idPicFile").appendChild(file);
		if(tChild!=0){ unOnload="0"; }
		tChild+=1;
		if(tChild==4){$$("idPicFile").style.display="none";}
		}
		//检测程序
		var exts = "jpg|gif|bmp|png", paths = "|";
		function CheckPreview(){
			var value = this.file.value, check = true;
			if ( !value ) {
				check = false; alert("请先选择文件！");
			} else if ( !RegExp( "\.(?:" + exts + ")$$", "i" ).test(value) ) {
				check = false; alert("只能上传以下类型：" + exts);
			} else if ( paths.indexOf( "|" + value + "|" ) >= 0 ) {
				check = false; alert("已经有相同文件！");
			}
			check || ResetFile(this.file);
			return check;
		}
		//显示预览
		function ShowPreview(){
			var row = table.appendChild(model.cloneNode(true)),
				file = this.file, value = file.value, oThis = this;
			
			row.appendChild(file).style.display = "none";
			row.cells[0].innerHTML = value;
			row.cells[1].appendChild(this.img);
			
			row.getElementsByTagName("a")[0].onclick = function(){
				oThis.dispose(); table.removeChild(row);
				paths = paths.replace(value, ""); return false;
			};
			
			paths += value + "|";
			addPic();
			
		}
		
		addPic();
		
		function ResetFile(file){
			file.value = "";//ff chrome safari
			if ( file.value ) {
				if ( $$B.ie ) {//ie
					with(file.parentNode.insertBefore(document.createElement('form'), file)){
						appendChild(file); reset(); removeNode(false);
					}
				} else {//opera
					file.type = "text"; file.type = "file";
				}
			}
		}
		
		//上传图片
		
			function uploadForm(){
			$("#addForm").submit();
			}
		
		//提交
		    function submitForm(){
				if($.trim($("#question_theme").val())==""){
					$("#question_theme").focus();
					alert("主题不能为空！");
					return;
				}
				if(unOnload=="0"){
				alert("图片还没上传！");
				return;
				}
				$("#updateForm").submit();
			}
		</script>
		<br />
		</div>
		</form>       
        		
  		<div class="book_manage account_manage oz" style="width:700px; margin-top: 5px;">
		<form action="/questionOrder/addtrain_question_order.do" method="post" name="updateForm" id="updateForm" onsubmit="return checkSubmit();">
        <ul class="order_num oz">
        	<li>订单号：&nbsp;<input type="text" name="question_order_id" id="question_order_id" style="width: 200px;" />
        	</li>
        	<li><font color="red">*&nbsp;</font>标题：&nbsp;<input type="text" name="question_theme" id="question_theme" style="width: 200px;" />
        	</li>
        	<li>指定人：&nbsp;<input type="text" name="question_assigner" id="question_assigner" style="width: 200px;" />
        	</li>
        	<li><span style="display:inline-block; height:100px; text-align:top; float:left;">&nbsp;&nbsp;详细：</span>
        	<textarea rows="9" cols="60" name="question_desc" id="question_desc"  style="border:1px solid #A3B083;"></textarea>
        	</li>
        	<li>
        	<input type="hidden" name="question_pic" id="question_pic" value="${question_pic }"/>
        	</li>
        	</ul>
         </form>
         </div>
	<div class="book_manage account_manage oz">
        <p><input type="button" value="提 交" class="btn" id="btnSubmit" onclick="submitForm()"/>
           <input type="button" value="返 回" class="btn" onclick="javascript:location='/questionOrder/gototrain_question_order.do?question_status=11&question_status=33'"/></p>
	</div>
	<br/><br/>
	</div>
  </body>
</html>
