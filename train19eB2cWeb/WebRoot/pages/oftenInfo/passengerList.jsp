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
//按照姓名查询旅客信息
function queryPassengerList(){
	$("#trainForm").submit();
}
//增加新的旅客
function toAdd(){
	window.location.href='/login/toAddPassenger.jhtml';
}
//删除常用旅客
function deletePasser(link_id){
	var dialog = new popup("land_on","drawBill","land_off");
	$("#land_on").click();//取消
	document.getElementById('ok_btn').onclick = function(){//确定
		window.location.href='/login/deletePassenger.jhtml?link_id='+link_id;
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
				<jsp:param name="menuId" value="passInfo" />
			</jsp:include>
	<!--左边内容 end-->
    
    
    <!--右边内容 start-->
    
	<div class="right_con">
	<form id="trainForm" action="/login/queryPassenger.jhtml" method="post">
		<ul class="MyOrder">
    		<li>常用旅客</li>
    	</ul>
        <div class="CommonI_cx">
        	按乘客姓名查询：
        	<input type="text" class="txt" id="link_name" name="link_name" value="${link_name }" /> 
	        <input type="button" class="btn13 btn" onclick="queryPassengerList();" value="查&nbsp;&nbsp;询" />
	        <input type="button" class="btn13 btn" onclick="javascript:window.location.href='/login/toAddPassenger.jhtml'" value="添加新旅客" />
        </div>
		<table class="CommonI_tb">
    	<tr class="order_tit">
			<th width="10%">姓名</th>    
			<th width="15%">证件类型</th>    
			<th width="20%">证件号码</th>    
			<th width="18%">手机号码</th>    
			<th width="15%">乘客类型</th>    
			<th width="22%">操作</th>    
	    </tr>
    	<tbody>
    	<c:if test="${count != 0}">
    		<c:forEach items="${linkerList}" var="list" varStatus="idx">
	    	<tr>
	        	<td>${list.link_name }</td>
	        	<td>${idsTypeMap[list.ids_type]}</td>
	        	<td>${list.ids_card }</td>
	        	<td>${list.link_phone }</td>
	        	<td>${ticketTypeMap[list.passenger_type]}</td>
	        	<td>        
		            <a href="#" style="margin-right:30px" onclick="window.location='/login/toUpdatePassenger.jhtml?link_id=${list.link_id}'">编辑</a>
		        	<a href="javascript:void(0);" onclick="deletePasser('${list.link_id}');">删除</a>
				</td>
	        </tr>
	       	</c:forEach>
	    </c:if>
	    <c:if test="${count == 0}">
	    	<tr>
	    		<td colspan="6">暂无符合条件常用旅客</td>
	    	</tr>
	    </c:if>
    	</tbody>
    </table>        
    </form>
	</div>
  	<!--右边内容 end-->
</div>
<!--以下是我的旅行正文内容travel_con部分end -->

<!-- 密码修改成功弹框start -->
    <div class="password" id="drawBill">
    	<dl>
        <dt>
        	<i></i>
        </dt>
        <dd>
        	<p>您确认要删除所选旅客吗？</p>
        </dd>
        </dl> 
		<input type="hidden" id="land_on" />
		<button class="btn13" type="button" style="margin-left:50px;" id="ok_btn">确&nbsp;&nbsp;定</button>
		<button class="btn6" type="button" style="margin-left:40px;" id="land_off">取&nbsp;&nbsp;消</button>
    </div>
<!-- 密码修改成功弹框end -->
   
<!-- footer start -->
<%@ include file="/pages/common/footer.jsp"%>
<!-- footer end -->
</body>
</html>
