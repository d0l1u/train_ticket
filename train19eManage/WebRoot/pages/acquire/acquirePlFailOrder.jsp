<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>批量失败页面</title>
		<link rel="stylesheet" href="/css/style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
		<script type="text/javascript">
		function submitFailOrder(order_idList){
			var error_info="";
			var errorNum = 0;
			$("input[name='error_info']:radio:checked").each(function(){ //遍历被选中CheckBox元素的集合 得到Value值
			error_info = $(this).val();
			errorNum++;
			});
			if(errorNum==0){
				alert("请选中一个失败原因!");
				return false;
			}
			//alert(order_idList);
			$("form:first").attr("action","/acquire/updateFailOrder.do?order_idList="+order_idList+"&error_info="+error_info);
			$("form:first").submit();
			parent.reloadPage();
		}
</script>
	</head>
	<body>
		<form action="/acquire/updateAcquireInfo.do" method="post">
			<div style="margin-top:20px;margin-left: 60px;margin-bottom: 20px; ">
				<input type="hidden" class="text" name="channel" value="${channel}" />
				<c:choose>
				<c:when test="${channel eq 'qunar'}">
		    		<ul>
				    	<li>
			    			<input type="radio" id="error_info" name="error_info" value="0"/>其他
			    		<br/></li>
				    	<li>
			    			<input type="radio" id="error_info" name="error_info" value="1"/>所购买的车次坐席已无票
			    		<br/></li>
				    	<li>
			    			<input type="radio" id="error_info" name="error_info" value="2"/>身份证件已经实名制购票，不能再次购买同日期同车次的车票
			    		<br/></li>
				    	<li>
				    		<input type="radio" id="error_info" name="error_info" value="3"/>去哪儿票价和12306不符
			    		<br/></li>
				    	<li>
			    			<input type="radio" id="error_info" name="error_info" value="4"/>车次数据与12306不一致
			    		<br/></li>
				    	<li>
				    		<input type="radio" id="error_info" name="error_info" value="5"/>乘客信息错误
			    		<br/></li>
				    	<li>
				    		<input type="radio" id="error_info" name="error_info" value="6"/>12306乘客身份信息核验失败
			    		<br/></li>
			    	</ul>
			    </c:when>
			    <c:otherwise>
		    		<ul>
				    	<li>
			    			<input type="radio" id="error_info" name="error_info" value="1"/>所购买的车次坐席已无票
			    		<br/></li>
				    	<li>
				    		<input type="radio" id="error_info" name="error_info" value="2"/>身份证件已经实名制购票
			    		<br/></li>
				    	<li>
				    		<input type="radio" id="error_info" name="error_info" value="3"/>票价和12306不符
			    		<br/></li>
				    	<li>
				    		<input type="radio" id="error_info" name="error_info" value="4"/>乘车时间异常
			    		<br/></li>
				    	<li>
				    		<input type="radio" id="error_info" name="error_info" value="5"/>证件错误
			    		<br/></li>
				    	<li>
				    		<input type="radio" id="error_info" name="error_info" value="6"/>用户要求取消订单
			    		<br/></li>
				    	<li>
				    		<input type="radio" id="error_info" name="error_info" value="7"/>未通过12306实名认证
			    		<br/></li>
				    	<li>
				    		<input type="radio" id="error_info" name="error_info" value="8"/>乘客身份信息待核验
			    		<br/></li>
			    		<c:if test="${channel eq 'tongcheng'}">
			    		<li>
				    		<input type="radio" id="error_info" name="error_info" value="9"/>系统异常
			    		<br/></li>
			    		<li>
				    		<input type="radio" id="error_info" name="error_info" value="11"/>超时未支付
			    		<br/></li>
			    		</c:if>
						<li>
				    		<input type="radio" id="error_info" name="error_info" value="12"/>信息冒用
			    		<br/></li>
			    		<c:if test="${channel eq '301030'}">
			    		<li>
				    		<input type="radio" id="error_info" name="error_info" value="13"/><font color="#F60;">排队人数过多，高铁管家要求失败</font>
			    		<br/></li>
			    		</c:if>			    		
			    	</ul>
			    </c:otherwise>
			    
				</c:choose>
			   
	</div>
	</form>
	<div>
		<p>
			<input type="button" value="出票失败" onclick="submitFailOrder('${order_idList}');" 
				style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#BA55D3;cursor:pointer;width:100px;height:30px;"/>
			<input type="button" value="返回" onclick="backPage();"
				style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#FFA500;cursor:pointer;width:100px;height:30px;"/>
		</p>
	</div>
					
</body>
</html>
