<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>19trip旅行</title>
<link rel="stylesheet" href="/css/reset-min.css" type="text/css" />
<link rel="stylesheet" href="/css/style.css" type="text/css"/>
<link rel="stylesheet" href="/css/sreachbar.css" type="text/css"/>
<link rel="stylesheet" href="/css/travel.css" type="text/css"/>
<script type="text/javascript" src="/js/artDialog.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery.form.js"></script>
<script type="text/javascript" src="/js/dialog.js"></script>
<style type="text/css">
/****订单弹出框*****/
#drawBill{ position:fixed;_position: absolute;_top: expression(documentElement.scrollTop + 340 + "px");
	 background:#fff; width:400px; height:280px; border:1px solid #86CBFF; top:50%; left:50%; 
	 margin: -200px 0 0 -250px; overflow:hidden; z-index:999999; font-size:14px; color:#000; display:none;}
* html,* html body{background-image:url(about:blank);background-attachment:fixed} 
* html .ie6fixedTL{position:absolute;left:expression(eval(document.documentElement.scrollLeft));top:expression(eval(document.documentElement.scrollTop))} 
* html .ie6fixedBR{position:absolute;left:expression(eval(document.documentElement.scrollLeft+document.documentElement.clientWidth-this.offsetWidth)-(parseInt(this.currentStyle.marginLeft,10)||0)-(parseInt(this.currentStyle.marginRight,10)||0));top:expression(eval(document.documentElement.scrollTop+document.documentElement.clientHeight-this.offsetHeight-(parseInt(this.currentStyle.marginTop,10)||0)-(parseInt(this.currentStyle.marginBottom,10)||0)))} 
</style>

<script type="text/javascript">
$().ready(function(){
	//初始化年份  
	var year=new Date().getYear(); 
	//alert($("#s_year").val());

	for(var i=year;i>=1990;i--) {
		document.getElementById("year").options[parseInt(year)+1-i] = new Option(i,i);   
	}  
	//初始化月份  
	for(var i=1;i<=12;i++) {  
		document.getElementById("month").options[i] = new Option(i,i); 
	}
	$('#year').val($("#s_year").val()); 
	$('#month').val($("#s_month").val()); 
	initDay();
	$('#day').val($("#s_day").val()); 



	var optionsUser = {
	   target: '#output',          //把服务器返回的内容放入id为output的元素中    
	   beforeSubmit: showRequest,
	   success: showResponse,      //提交后的回调函数
	   //url: url,                 //默认是form的action， 如果申明，则会覆盖
	   dataType : 'json',
	   timeout: 5000,               //限制请求的时间，当请求大于3秒后，跳出请求
	   failure : function(xhr,msg){ 
			alert("很抱歉，修改用户信息失败！"); 
			return false;
	   } 
	}
	function showRequest(){
	}
	//提交后的回调函数
	function showResponse(responseText, statusText){
		var result = responseText.result;
		if (responseText.result == "SUCCESS") { 
			var dialog = new popup("land_on","drawBill","land_off");
			$("#land_on").click();
	    }else{ 
	    	alert("很抱歉，修改用户信息失败！"); 
			return false;
	    } 
	}
	
	//保存用户信息
	$("#trainForm").submit(function() {
		 //$("#trainForm").attr("action", "/login/saveUserinfo.jhtml");
		 $(this).ajaxSubmit(optionsUser); 
	     return false; 
	}); 
});

//初始化日  
function initDay() {
	//添加之前先初始化  
	document.getElementById("day").length=1;  
	var year=document.getElementById("year").value;   
	var month=document.getElementById("month").value;  
	if(year==""||month=="")  {   
		return;   
	}  else  {   
		var arr=new Array(31,28,31,30,31,30,31,31,30,31,30,31);   
		if((year%4==0&&year%100!=0)||year%400==0)   {    
			arr[1]++;    
		}   
		for(i=1;i<=arr[month-1];i++)   {    
			document.getElementById("day").options[i]=new Option(i,i);    
		}  
	} 
}


	
</script>
</head>

<body>
<!--以下是头部logo部分start -->
<jsp:include flush="true" page="/pages/common/headerNav.jsp">
	<jsp:param name="menuId" value="lx" />
</jsp:include>
<!--以下是头部logo部分end -->


<!--以下是我的旅行正文内容travel_con部分start -->
<div class="travel_con">
	<!--左边内容 start-->
			<jsp:include flush="true" page="/pages/common/menuLeft.jsp">
				<jsp:param name="menuId" value="myInfo" />
			</jsp:include>
	<!--左边内容 end-->
    
    
    <!--右边内容 start-->
	<div class="right_con">
	<form id="trainForm" action="/login/saveUserinfo.jhtml" method="post">
		<input type="hidden" id="user_id" name="user_id" value="${loginMap.user_id }" />
        <ul class="MyOrder">
    		<li>编辑信息</li>
    	</ul>
        <table class="MyInmatable">
        <tr>
	        <td>手机：</td>
	        <td>${loginMap.user_phone }</td>
        </tr>
        <tr>
        <td>邮箱：</td>
        <td><input type="text" class="text" id="user_email" name="user_email" value="${loginMap.user_email }" /></td>
        </tr>
        <tr>
        <td>昵称：</td>
        <td><input type="text" class="text" id="user_name" name="user_name" value="${loginMap.user_name }" /></td>
        </tr>
        <tr>
        <td>姓别：</td>
        <td>
        	<input type="radio" name="user_sex" value="1" <c:if test="${loginMap.user_sex eq '1' }">checked="checked"</c:if> />&nbsp;女&nbsp;&nbsp;&nbsp;
        	<input type="radio" name="user_sex" value="0" <c:if test="${loginMap.user_sex eq '0' }">checked="checked"</c:if> />&nbsp;男
        </td>
        </tr>
        <tr>
        <td>生日：</td>
        <td>
        	<select id="year" name="year" onchange="initDay();" class="sel">
				<option>年</option>
			</select>&nbsp;年
			<select id="month" name="month" onchange="initDay();" class="sel">  
				<option>月</option>
			</select>&nbsp;月
			<select id="day" name="day" class="sel">
				<option>日</option> 
			</select>&nbsp;日
			<input type="hidden" id="s_year" value="${year }" />
			<input type="hidden" id="s_month" value="${month }" /> 
			<input type="hidden" id="s_day" value="${day }" />  
        </td>
        </tr>
		</table>
        <input type="submit" class="btn13 MyInma_button" value="保&nbsp;&nbsp;存" />
        <br/><br/><br/>
    </form>
	</div>
  	<!--右边内容 end-->
</div>
<!--以下是我的旅行正文内容travel_con部分end -->
<!--密码修改成功弹框-->
  	<div class="password" id="drawBill">
    	<dl>
			<dt>
				<strong></strong>
			</dt>
			<dd>
                <p>恭喜您，信息修改成功！</p>
            </dd>
		</dl>
		<input type="hidden" id="land_off" />
		<input type="hidden" id="land_on" />
		<button class="btn13" type="button" onclick="window.location='/login/myInfo.jhtml'">确&nbsp;&nbsp;定</button>
    </div>
<!-- footer start -->
<%@ include file="/pages/common/footer.jsp"%>
<!-- footer end -->
</body>
</html>
