<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>修改机器人功能页面</title>
<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<!-- <script language="javascript" src="/js/datepicker/WdatePicker.js"></script>-->
<script language="javascript" src="/js/layer/layer.js"></script>
<script language="javascript" src="/js/mylayer.js"></script>
<script type="text/javascript">

	function updateRobotUrl(workerId,pageIndex){
	//if(confirm("确认修改机器人信息吗?")) {
				//alert(workerId);
				$("form:first").attr("action", "/robotSet/updateRobotUrl.do?workerId=" + workerId+"&pageIndex="+pageIndex+"&robot_type="+'${robot_type}'+"&robot_name_search="+'${robot_name_search}');
				$("form:first").submit();
		//	}
	}
	function opt_rizhi(){
		var url="/worker/queryOptionLogList.do"
		showlayer('操作日志',url,'950px','600px')
	}
	
	function fanhui(pageIndex){
		var robot_type ='${robot_type}';
		var str = "";
			if(robot_type!=""){
				var arr= new Array();   
				arr = robot_type.split(",");
				for(var i=0;i<arr.length;i++){
					str += "&robot_type="+arr[i];
				}
			}
		$("form:first").attr("action", "/robotSet/queryRobotSetting.do?pageIndex="+pageIndex+"&robot_type="+str+"&robot_name="+'${robot_name_search}');
		$("form:first").submit();
	}
	function jiaoyan(){
		var zhifubao = $.trim( $("#zhifubao").val() );
			zhifubao = encodeURI(zhifubao);
			//alert(zhifubao);
			if(zhifubao!=""){
			var url = "/robotSet/queryZhifubao.do?zhifubao="+zhifubao+"&version="+new Date();
			$.get(url,function(data){
				if(data == "yes"){
					alert("当前选择账号已绑定2个机器人，请重选账号！");
					return;
				}
			});
			}
		}
</script>
<style>
	.book_manage table{border:1px solid #dadada;width:1100px;text-align:center;}
</style>
</head>

<body>
<div class="outer">
  	<div class="book_manage oz">
  	<form action="/robotSet/queryRobotSetting.do" method="post">

	<table>
  			<tr>
	    		<th width="50px">序号
	    		<input type="hidden"  name="robot_type" id="robot_type" value="${robot_type}" />
	    		<input type="hidden"  name="robot_name_search" id="robot_name_search" value="${robot_name_search}"/>
	    		<input type="hidden"  name="pageIndex" id="pageIndex" value="${pageIndex}" />
	    		</th>
	    		<th width="100px">机器人功能</th>
	    		<th width="50px">状态</th>
	    		<th>请求网址</th>
	    		<th  width="80px">支付宝</th>
	    		<th  width="80px">空闲数</th>
	    		<th  width="80px">优先级</th>
	    		<th  width="100px">连接超时时间</th>
	    		<th  width="100px">读取超时时间</th>
	    		<th width="80px">操作人</th>
	    		<th width="100px">操作</th>
	    	</tr>
	    	<c:forEach var="list" items="${workerList}" varStatus="idx">
	    	<tr>
	    		<td>
	    			${idx.index+1}
	    		</td>
	    		<td>
	    			${workerType[list.worker_type]}
	    		</td>
	    		<td>
	    			<c:if test="${list.worker_status eq '00' }"><font color="green">工作中</font></c:if>
	    			<c:if test="${list.worker_status eq '22' }"><font color="#FF82AB">停用</font></c:if>
	    			<c:if test="${list.worker_status eq '33' }"><font color="#1C86EE">备用</font></c:if>
	    			
	    		</td>
	    		<td>
	    			<input type="text" class="worker_ext"  name="worker_ext_${list.worker_id}" id="worker_ext" value="${list.worker_ext}" size="60"/>
	    		</td>
	    		<td>
	    		<c:if test="${list.worker_type eq '3' }">
							<select id="zhifubao" name="zhifubao" style="width:60px;" onchange="jiaoyan()" >
								<option value="" <c:if test="${empty zhifubao}">selected</c:if>>
									空
								</option>
								<!-- <option value="huochepiao19e@163.com"
									<c:if test="${!empty zhifubao && zhifubao eq 'huochepiao19e@163.com'}">selected</c:if>>
									01
								</option>-->
								<c:forEach items="${zhanghaoList}" var="tt" varStatus="index">
									<option value="${tt.key }"
									<c:if test="${!empty zhifubao && fn:contains(tt.key,zhifubao)}">selected</c:if>>
									${tt.value}
									</option>
								</c:forEach>
							</select>
				</c:if>
	    		</td>
	    		<td >
	    		<input type="text" class="spare_thread"  name="spare_thread_${list.worker_id}" id="spare_thread" value="${list.spare_thread}"size="5"/>
	    		</td>
	    		<td>
				<input type="text" class="worker_priority"  name="worker_priority_${list.worker_id}" id="worker_priority" value="${list.worker_priority}" size="5"/>
	    		</td >
	    		
	    		<td >
	    		<input type="text" class="robot_con_timeout"  name="robot_con_timeout_${list.worker_id}" id="robot_con_timeout" value="${list.robot_con_timeout}" size="5"/>
	    		</td>
				<td >
				<input type="text" class="robot_read_timeout"  name="robot_read_timeout_${list.worker_id}" id="robot_read_timeout" value="${list.robot_read_timeout}" size="5"/>
	    		</td>
				
	    		<td>
	    		${list.opt_name }
	    		</td>
				<td>
				<a href="javascript:updateRobotUrl('${list.worker_id}','${pageIndex}')">保存修改</a>
				</td>
	    		</tr>
	    	  </c:forEach>
		   </table>
			<p>
				<input type="button" value="查看日志" class="btn" id="rizhi" onclick="opt_rizhi()" />
				<input type="button" value="返 回" class="btn"
									onclick="fanhui('${pageIndex}')" />
			</p>
	    </form>
	  </div>
   </div>
</body>
</html>
