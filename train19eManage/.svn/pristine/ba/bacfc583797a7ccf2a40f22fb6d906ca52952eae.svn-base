<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>人员管理页面</title>
<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<!-- <script language="javascript" src="/js/datepicker/WdatePicker.js"></script>-->
<script language="javascript" src="/js/layer/layer.js"></script>
<script language="javascript" src="/js/mylayer.js"></script>
<script type="text/javascript">

	function submit(url){
		if(confirm("是否提交？")){
			location.href = url; 
		}
	}

	function opt_rizhi(){
		var url="/worker/queryOptionLogList.do";
		showlayer('操作日志',url,'950px','600px')
	}
</script>
</head>

<body>
	<div class="book_manage oz">
		<form action="/worker/queryWorkerList.do" method="post">
        
        <ul class="order_num oz">
        	<li>人员名称：&nbsp;&nbsp;&nbsp;<input type="text" class="text" name="worker_name" /></li>
        	
        </ul>
        <ul class="ser oz">
        	
        	<li>状态：
            <c:forEach items="${workerStatus }" var="s" varStatus="index">
        		<li><input type="checkbox" id="worker_status${index.count }" name="worker_status" value="${s.key }" <c:if test="${fn:contains(statusStr, s.key ) }">checked="checked"</c:if>/><label for="worker_status${index.count }">${s.value }</label></li>
        		
        	</c:forEach>
            </li>
        </ul>
         <ul class="ser oz">
        	
        	<li>类型：
            <c:forEach items="${workerType }" var="s" varStatus="index">
        		<li><input type="checkbox" id="worker_type${index.count }" name="worker_type" value="${s.key }" <c:if test="${fn:contains(typeStr, s.key ) }">checked="checked"</c:if>/><label for="worker_type${index.count }">${s.value }</label></li>
        		
        	</c:forEach>
            </li>
        </ul>
        <p>
        	<input type="submit" value="查 询" class="btn" />&nbsp;&nbsp;&nbsp;
        	 <%LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
										if ("2".equals(loginUserVo.getUser_level())){ %>
        	<input type="button" value="添加" onclick="location.href = '/worker/addPreWorker.do'" class="btn" />&nbsp;&nbsp;&nbsp;
        	<%} %>
        	<input type="button" value="查看日志" class="btn" id="rizhi" onclick="opt_rizhi()" />
        </p>
        <c:if test="${!empty isShowList}">
        <table>
           <tr style="background:#EAEAEA; ">
                <th>序号</th>
             
                <th>人员名称</th>
                
               
                <th>订单数</th>
                <th>最大订单数</th>
                <th>人员状态</th>             
                <th>优先级</th>
                <th>创建时间</th>
                <th>操作时间</th>
                <th>空闲状态</th>
                <th>操作人</th>
        
                <th>操作</th>
            </tr>
            <c:forEach var="list" items="${workerList}" varStatus="idx">
	            <tr 
            		<c:if test="${fn:contains('00', list.worker_status )}">
            		 	style="background:#BEE0FC; "
            		</c:if>
            		<c:if test="${fn:contains('22', list.worker_status )}">
            		 	style="background:#E0F3ED; "
            		</c:if>
            	>
	            	<td>${idx.index+1}</td>
	            	
	                <!--<td>${list.acc_name}</td> -->
	                <td align="left">[${workerType[list.worker_type]}]${list.worker_name}</td>
	                
	        
	                <td>${list.order_num}</td>
	                <td>${list.max_order_num}</td>
	                <td>${workerStatus[list.worker_status]}</td>
	                <td>${list.worker_priority}</td>
	                <td>${list.create_time}</td>
	                <td>${list.option_time}</td>
	                <td>${list.spare_thread }</td>
	                <td>${list.opt_name}</td>
	                <td><span>
	                <c:if test="${list.worker_status eq '00'}">
	                <%if ("2".equals(loginUserVo.getUser_level())){ %>
	                 	<a href="javascript:submit('/worker/deleteWorker.do?worker_id=${list.worker_id }')">删除</a>
	                 	<a href="javascript:submit('/worker/updatePreWorker.do?worker_id=${list.worker_id }')">修改</a>
	                 	<%} %>
	                 	<a href="javascript:submit('/worker/updateWorker.do?worker_status=22&worker_id=${list.worker_id }')">停用</a>
	                 	<a href="javascript:submit('/worker/updateWorker.do?worker_status=22&worker_id=${list.worker_id }&msn=1')">停用并发送短信</a>
	                </c:if>   
	                <c:if test="${list.worker_status eq '00' && list.worker_type eq '3'}">
	             		<a href="javascript:submit('/worker/updateZhifuWorkerCard.do?worker_id=${list.worker_id }')">切换</a>
	                </c:if> 
	                <c:if test="${list.worker_status eq '22' }">
	                 <%if ("2".equals(loginUserVo.getUser_level())){ %>
	                 	<a href="javascript:submit('/worker/deleteWorker.do?worker_id=${list.worker_id }')">删除</a>
	                 	<a href="javascript:submit('/worker/updatePreWorker.do?worker_id=${list.worker_id }')">修改</a>
	                 	<%} %>
	                 	<a href="javascript:submit('/worker/updateWorker.do?worker_status=33&worker_id=${list.worker_id }')">备用</a>
	                </c:if>               
	        		<c:if test="${list.worker_status eq '33' }">
	        		 <%if ("2".equals(loginUserVo.getUser_level())){ %>
	                 	<a href="javascript:submit('/worker/deleteWorker.do?worker_id=${list.worker_id }')">删除</a>
	                 	<a href="javascript:submit('/worker/updatePreWorker.do?worker_id=${list.worker_id }')">修改</a>
	                 	<%} %>
	                 	<a href="javascript:submit('/worker/updateWorker.do?worker_status=00&worker_id=${list.worker_id }')">启用</a>
	                </c:if>    
	                </span></td>
	            </tr>
            </c:forEach>
        </table>
        <jsp:include page="/pages/common/paging.jsp" />
        </c:if>
         <br/>
         <p></p>
         
        </form>
	</div>
</body>
</html>
