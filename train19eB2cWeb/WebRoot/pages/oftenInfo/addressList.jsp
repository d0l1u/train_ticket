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
//按照姓名查询地址信息
function queryAddressList(){
	$("#trainForm").submit();
}
//增加新的地址
function toAdd(){
	window.location.href='/login/toAddAddress.jhtml';
}
//删除常用地址
function deleteAddress(address_id){
	var dialog = new popup("land_on","drawBill","land_off");
	$("#land_on").click();//取消
	document.getElementById('ok_btn').onclick = function(){//确定
		window.location.href='/login/deleteAddress.jhtml?address_id='+address_id;
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
				<jsp:param name="menuId" value="postAddress" />
			</jsp:include>
	<!--左边内容 end-->
    
    
    <!--右边内容 start-->
    <c:if test="${sunCount!=0 }">
	<div class="right_con">
	<form id="trainForm" action="/login/queryAddress.jhtml" method="post">
		<ul class="MyOrder">
    		<li>邮寄地址</li>
    	</ul>
        <div class="CommonI_cx">
        	按收件人查询：
        	<input type="text" class="txt" id="addressee" name="addressee" value="${addressee }" /> 
	        <input type="button" class="btn13 btn" onclick="queryAddressList();" value="查&nbsp;&nbsp;询" />
	        <input type="button" class="btn13 btn" onclick="javascript:window.location.href='/login/toAddAddress.jhtml'" value="添加新地址" />
        </div>
		<table class="CommonI_tb">
	    	<tr class="order_tit">
				<th width="10%">收件人</th>    
				<th width="15%">地区</th>    
				<th width="20%">详细地址</th>    
				<th width="18%">邮编</th>    
				<th width="15%">手机</th>    
				<th width="22%">操作</th>    
		    </tr>
    		<tbody>
    		<c:if test="${count != 0}">
    		<c:forEach items="${addressList}" var="list" varStatus="idx">
	    	<tr>
	        	<td>${list.addressee }</td>
	        	<td>${list.province }${list.city }${list.district }</td>
	        	<td>${list.address_name }</td>
	        	<td>${list.zip_code }</td>
	        	<td>${list.addressee_phone }</td>
	        	<td>        
	        		<a href="#" style="margin-right:30px" onclick="window.location='/login/toUpdateAddress.jhtml?address_id=${list.address_id}'">编辑</a>
		        	<a href="javascript:void(0);" onclick="deleteAddress('${list.address_id}');">删除</a>
				</td>
       	 	</tr>
       	 	</c:forEach>
       	 	</c:if>
       	 	
       	 	<c:if test="${count == 0}">
	    	<tr>
	    		<td colspan="6">暂无符合条件邮寄地址</td>
	    	</tr>
	    </c:if>
    		</tbody>
		</table>        
    </form>
	</div>
	</c:if>
	
	<c:if test="${sunCount==0}">
	<div class="right_con">
    	<ul class="MyOrder">
    		<li>邮寄地址</li>
    	</ul>
		<table class="CommonI_tb" style="margin-top:20px;">
	    	<tr class="order_tit">
				<th width="10%">收件人</th>    
				<th width="15%">省份</th>    
				<th width="20%">城市</th>    
				<th width="18%">区/县</th>    
				<th width="15%">详细地址</th>    
				<th width="22%">邮编</th>    
		    </tr>
	    </table> 
	    <div class="zw_add">暂无收货地址
	    	<input type="button" class="btn13 btn" onclick="javascript:window.location.href='/login/toAddAddress.jhtml'" value="添加新地址" />
	    </div>       
	</div>
	</c:if>
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
        	<p>您确认要删除所选地址吗？</p>
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
