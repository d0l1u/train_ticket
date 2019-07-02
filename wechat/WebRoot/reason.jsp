<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
</head>
<table>
	<TR>  
          <TH> 1.出票日期非大写或文字不正确 </TH>
          <TD> <input type="checkbox" name="checkbox" value="1">     
    </TD>
          <TH>2.大、小写金额不符或文字不正确</TH>
          <TD> <input type="checkbox" name="checkbox" value="2"> 
    </TD> 
    <TH>3.出票日期、金额、收款人涂改</TH>
          <TD> <input type="checkbox" name="checkbox" value="3"> 
    </TD>   
    </TR>
    </table>
    选完后的按钮
    <input type="button" name="ok" value="确 定" onclick="chooseReason()">
    //js
    <script>
    function chooseReason() 
    {
   		//获得页面所有为input的element
		var objs = document.getElementsByTagName("input");
		//拼凑理由的字符串
		var str='';
		//遍历
		for(var i=0; i<objs.length; i++) {//遍历所有的，被选中的复选框
    		if(objs[i].type.toLowerCase() == "checkbox" &&objs[i].checked )
	    	 	//连接客串str
	    	 	str=str+objs[i].value+',';
		}
   
		//取得父窗口传过来input的对象  
		// window.dialogArguments 父窗口传来的参数     
   		 var myObj = window.dialogArguments;     
		/*
		取出父窗体传过来的值.
		alert(myOjb.value);
		*/   
		//把字符串赋给输入页面的输入框 
   
    	myObj.value = str; // 把子窗口的值赋给父窗口传来的参数
   
    	//关闭窗口        
    	window.close();    
	}
	</script>
</html>