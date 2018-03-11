<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>停用机器人选择原因页面</title>
		<link rel="stylesheet" href="/css/style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
		<script type="text/javascript">
		function submitStopRobot(robotId,workerType,msn,robot_type){
			var stop_reason="";
			var errorNum = 0;
			$("input[name='stop_reason']:radio:checked").each(function(){ //遍历被选中CheckBox元素的集合 得到Value值
			stop_reason = $(this).val();
			errorNum++;
			});
			if(errorNum==0){
				alert("请选中一个失败原因!");
				return false;
			}
			$("form:first").attr("action", "/robotSet/changeRobotAction.do?robotId=" +robotId+"&workerType="+workerType+"&msn="+msn
								+"&robot_type="+robot_type+"&stop_reason="+stop_reason+"&version="+new Date());
			$("form:first").submit();
			parent.reloadPage();
		}
</script>
	</head>
	<body>
		<form action="/robotSet/queryRobotSetting.do" method="post">
			<div style="margin-top:20px;margin-left: 60px;margin-bottom: 20px; ">
		    		<ul style="line-height: 30px;">
				    	<li>
			    			<input type="radio" id="stop_reason" name="stop_reason" value="11"/>机器人需要重启
			    		<br/></li>
				    	<li>
			    			<input type="radio" id="stop_reason" name="stop_reason" value="22"/>机器人IP被封
			    		<br/></li>
				    	<li>
				    		<input type="radio" id="stop_reason" name="stop_reason" value="33"/>该支付机器人余额不足
			    		<br/></li>
				    	<li>
			    			<input type="radio" id="stop_reason" name="stop_reason" value="44"/>其他
			    		<br/></li>
			    	</ul>
			   
	</div>
	</form>
	<div>
		<p>
			<input type="button" value="停用" onclick="submitStopRobot('${robotId}','${workerType}','${msn}','${robot_type }');" 
				style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#BA55D3;cursor:pointer;width:100px;height:30px;"/>
			<input type="button" value="返回" onclick="backPage();"
				style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#FFA500;cursor:pointer;width:100px;height:30px;"/>
		</p>
	</div>
					
</body>
</html>
