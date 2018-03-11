<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<%@ page import="com.l9e.transaction.vo.PageVo"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>机器管理页面</title>
     <link rel="stylesheet" href="/css/back_style.css" type="text/css" />
	<meta http-equiv="pragma" content="no-cache"></meta>
	<meta http-equiv="cache-control" content="no-cache"></meta>
	<meta http-equiv="expires" content="0">   </meta> 
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"></meta>
	<meta http-equiv="description" content="This is my page"></meta>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="/js/jquery.js"></script>
	<script type="text/javascript" src="/js/datepicker/WdatePicker.js"></script>
	<script language="javascript" src="/js/layer/layer.js"></script>
	<script language="javascript" src="/js/mylayer.js"></script>
		<script type="text/javascript">
		<%
		PageVo pageObject = (PageVo) request.getAttribute("pageBean");
		int pageIndex = pageObject.getPageIndex();%>
		
		//删除机器人
			function deleteRobotStatus(robotId){
			alert("无权限删除");
			}
		
		
		//增加机器人
			function addNewRobotURL() {
				$("form:first").attr("action","/robotSet/turnToAddNewRobotURLPage.do?pageIndex="+<%=pageIndex%>);
				$("form:first").submit();
			}
		
		//操作日志	
		function opt_rizhi(robot_id){
				var url="/robotSet/queryRobotSetList.do?robot_id=" +robot_id;
				showlayer('操作日志',url,'950px','600px')
			}
		//切换机器人运行状态
			function changeNewRobotStatus(robotId,msn,status) {
				 var robot_type = "";
			 	$("input[name='robot_type']:checkbox:checked").each(function(){ 
							robot_type += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
						}); 
						robot_type = robot_type.substring(0, robot_type.length-1);
				 $("form:first").attr("action", "/robotSet/changeNewRobotURLStatus.do?pageIndex="+<%=pageIndex%>+"&robotId=" +robotId
				 +"&robot_type="+robot_type+"&msn="+msn+"&status="+status+"&version="+new Date());
				 $("form:first").submit();
			}
			
		//切换机器人各功能状态
		function changeRobotAction(robotId,robotStatus,workerType) {
			if(robotStatus=="1" && (robotId !="" ||robotId !="" ||robotId !="" )){
				 var robot_type = "";
			 	$("input[name='robot_type']:checkbox:checked").each(function(){ 
							robot_type += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
						}); 
						robot_type = robot_type.substring(0, robot_type.length-1);
					var robot_name = document.getElementById("robot_name").value;
				 $("form:first").attr("action", "/robotSet/changeRobotAction.do?robotId=" +robotId+"&workerType="+workerType+"&pageIndex="+<%=pageIndex%>
								+"&robot_type="+robot_type+"&robot_name="+robot_name+"&version="+new Date());
				 $("form:first").submit();
				}else{
				alert("机器人没有启用状态下不可启用该功能");
				}
		}
		
		//切换机器人各功能状态
		function stopRobotAction(robotId,robotStatus,workerType,msn) {
			if(robotStatus=="1"){
				 var robot_type = "";
			 	$("input[name='robot_type']:checkbox:checked").each(function(){ 
							robot_type += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
						}); 
						robot_type = robot_type.substring(0, robot_type.length-1);
				 	var url="/robotSet/chooseStopReason.do?robotId="+robotId+"&workerType="+workerType+"&msn="+"0"+"&robot_type="+robot_type+"&version="+new Date();
					showlayer('请选择停用原因:',url,'500px','300px');
				}else{
				alert("机器人没有启用状态下不可启用该功能");
				}
		}
			
		//修改机器人查询渠道
		function changeNewRobotChannel(robotId) {
		var robot_type = "";
			 	$("input[name='robot_type']:checkbox:checked").each(function(){ 
							robot_type += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
						}); 
						robot_type = robot_type.substring(0, robot_type.length-1);
				var robot_name = document.getElementById("robot_name").value;
				$("form:first").attr("action", "/robotSet/changeNewRobotChannel.do?pageIndex="+<%=pageIndex%>+"&robotId=" + robotId
				+"&robot_type="+robot_type+"&robot_name="+robot_name);
				$("form:first").submit();
		}
		
		//修改机器人信息
		function changeNewRobot(robotId,robotName) {
			robotName = encodeURI(robotName);
			var robot_type = "";
			 	$("input[name='robot_type']:checkbox:checked").each(function(){ 
							robot_type += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
						}); 
						robot_type = robot_type.substring(0, robot_type.length-1);
				var robot_name = document.getElementById("robot_name").value;
				$("form:first").attr("action", "/robotSet/turnToUpdateRobotPage.do?pageIndex="+<%=pageIndex%>+"&robotId=" + robotId
				+"&robot_type="+robot_type+"&robot_name="+robot_name+"&robotName="+robotName);
				$("form:first").submit();
		}
		
		//全选
		function selectAll(){
			var checklist = document.getElementsByName("robot_type");
			if(document.getElementById("controlAll").checked){
				for(var i=0; i<checklist.length; i++){
				checklist[i].checked = 1;
				}
			}else{
				for(var i=0; i<checklist.length; i++){
				checklist[i].checked = 0;
				}
			}
		}
		
		//切换代理
		function channgeProxy(robot_id,worker_type,cope){
			var status = "开启";
			if(cope == "22"){
				status = "关闭";
			}else if(cope == "00"){
				status = "开启";
			}
			if(confirm("确认【"+status+"】自动切换代理吗?")) {
			 var robot_type = "";
			 	$("input[name='robot_type']:checkbox:checked").each(function(){ 
							robot_type += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
						}); 
						robot_type = robot_type.substring(0, robot_type.length-1);
				var robot_name = document.getElementById("robot_name").value;
				 $("form:first").attr("action", "/robotSet/channgeProxy.do?robotId=" +robot_id+"&workerType="+worker_type+"&pageIndex="+<%=pageIndex%>
								+"&cope="+cope+"&robot_type="+robot_type+"&robot_name="+robot_name+"&version="+new Date());
				 $("form:first").submit();
			}
		}
</script>
<style  type="text/css">
	input,select{font-size:12px;}
	.btn0{text-align:center;border:none;color:green;background:#ffffff;cursor:pointer;}
	.btn1{text-align:center;border:none;color:#1C86EE;background:#ffffff;cursor:pointer;}
	.btn2{text-align:center;border:none;color:#FF82AB;background:#ffffff;cursor:pointer;}
	.btn4{text-align:center;border:none;color:#3BA0FF;background:#ffffff;cursor:pointer;}
	.book_manage td a:link,.book_manage td a:visited{color:blue;}
	#function{width:5%;}
	#function span{padding:0;}
	#width1{width:1%;}
	#width4{width:4%;}
	#width10{text-align:left;width:10%;}
	#width12{width:12%;}
</style>
  </head>
  <body>
  <div></div>
		<div class="book_manage oz">
			<form action="/robotSet/queryRobotSetting.do" method="post" name="queryFrm">
				<div style="border: 0px solid #00CC00; margin: 10px;">
			<!-- 	<ul class="order_num oz" style="margin-top: 10px;">
						<li>
							机器人名称：&nbsp;
							<select name="robot_name" id="robot_name">
							<c:forEach items="${robotSystemSetting }" var="p">
								<option value="${p.robot_name }"
									<c:if test="${fn:contains(robot_nameStr, p.robot_name ) }">selected="selected"</c:if> >
									${p.robot_name}
								</option>
							</c:forEach>
						</select>
						</li>
					</ul> -->	
					<ul class="order_num oz" style="margin-top: 10px;">
						<li>
							机器人名称：&nbsp;
							<input name="robot_name" id="robot_name" value="${robot_name }" />
						</li>
					</ul>
					<ul class="order_state oz">
					<li>
						&nbsp;&nbsp;&nbsp;机器人状态：&nbsp;&nbsp;
							<c:forEach items="${robot_statusList }" var="i" varStatus="index">
						<li>
							<input type="checkbox" id="robot_status${index.count }"
								name="robot_status" value="${i.key }" value="1" style="float:left;white-space:nowrap;width:25px;"
								<c:if test="${fn:contains(robot_statusStr, i.key ) }">checked="checked"</c:if> />
							<label for="robot_status${index.count }">
								${i.value }
							</label>
						</li>
					</c:forEach>
					</li>
					
			
				</ul>
					<ul class="order_state oz">
					<li>
						&nbsp;&nbsp;&nbsp;机器人功能：&nbsp;&nbsp;
					</li><li><input type="checkbox" onclick="selectAll()" name="controlAll" style="controlAll" id="controlAll"/><label for="controlAll">全部</label></li>
					<li>
						<input type="checkbox" id="robot_money" name="robot_type" value="robot_money" value="1"
							<c:if test="${fn:contains(robot_type , 'robot_money' ) }">checked="checked"</c:if> />
							<label for="robot_money">查询票价</label>
					</li>
					<li>
						<input type="checkbox" id="robot_register" name="robot_type" value="robot_register" value="1"
							<c:if test="${fn:contains(robot_type , 'robot_register' )}">checked="checked"</c:if> />
							<label for="robot_register">实名认证</label>
					</li>
					<li>
						<input type="checkbox" id="robot_book" name="robot_type" value="robot_book" value="1"
							<c:if test="${fn:contains(robot_type ,  'robot_book'  )}">checked="checked"</c:if> />
							<label for="robot_book">预订</label>
					</li>
					<li>
						<input type="checkbox" id="robot_pay" name="robot_type" value="robot_pay" value="1"
							<c:if test="${fn:contains(robot_type , 'robot_pay' ) }">checked="checked"</c:if> />
							<label for="robot_pay">支付</label>
					</li>
					<li>
						<input type="checkbox" id="robot_check" name="robot_type" value="robot_check" value="1"
							<c:if test="${fn:contains(robot_type ,  'robot_check' ) }">checked="checked"</c:if> />
							<label for="robot_check">核验订单</label>
					</li>
					<li>
						<input type="checkbox" id="robot_cancel" name="robot_type" value="robot_cancel" value="1"
							<c:if test="${fn:contains(robot_type ,  'robot_cancel' ) }">checked="checked"</c:if> />
							<label for="robot_cancel">取消订单</label>
					</li>
					<li>
						<input type="checkbox" id="robot_endorse" name="robot_type" value="robot_endorse" value="1"
							<c:if test="${fn:contains(robot_type ,  'robot_endorse' ) }">checked="checked"</c:if> />
							<label for="robot_endorse">改签</label>
					</li>
					<li>
						<input type="checkbox" id="robot_refund" name="robot_type" value="robot_refund" value="1"
							<c:if test="${fn:contains(robot_type ,  'robot_refund' ) }">checked="checked"</c:if> />
							<label for="robot_refund">退票</label>
					</li>
					<li>
						<input type="checkbox" id="robot_query" name="robot_type" value="robot_query" value="1"
							<c:if test="${fn:contains(robot_type ,  'robot_query'  )}">checked="checked"</c:if> />
							<label for="robot_query">余票查询</label>
					</li>
					<li>
						<input type="checkbox" id="robot_delete" name="robot_type" value="robot_delete" value="1"
							<c:if test="${fn:contains(robot_type ,  'robot_delete' ) }">checked="checked"</c:if> />
							<label for="robot_delete">删除</label>
					</li>
					<li>
						<input type="checkbox" id="robot_enroll" name="robot_type" value="robot_enroll" value="1"
							<c:if test="${fn:contains(robot_type , 'robot_enroll'  )}">checked="checked"</c:if> />
							<label for="robot_enroll">注册账号</label>
					</li>
					<li>
						<input type="checkbox" id="robot_activate" name="robot_type" value="robot_activate" value="1"
							<c:if test="${fn:contains(robot_type ,  'robot_activate' )}">checked="checked"</c:if> />
							<label for="robot_activate">激活账号</label>
					</li>
					
				</ul> 
				</div>
			<p>
			<input type="submit" value="查 询" class="btn" />
			 <%  LoginUserVo loginUserVo = (LoginUserVo) request.getSession()
                     .getAttribute("loginUserVo"); 
			 if ("2".equals(loginUserVo.getUser_level())){ %>
			<input type="button" value="添加机器人" class="btn"  onclick="addNewRobotURL()" />
			 <%} %>
			</p>
			<c:if test="${!empty isShowList}">
			<table>
  			<tr>
	    		<th >NO</th>
	    		<th >名称</th>
	    		<th >状态</th>
	    		<th >功能</th>
	    		<th >查询渠道</th>
	    		<th >查询票价</th>
	    		<th >实名认证</th>
	    		<th >预订</th>
	    		<th >支付</th>
	    		<th >核验订单</th>
	    		<th >取消订单</th>
	    		<th >改签</th>
	    		<th >退票</th>
	    		<th >余票查询</th>
	    		<th >删除</th>
	    		<th >注册账号</th>
	    		<th >激活账号</th>
	    		<th >操作人</th>
	    		<th >操作</th>
	    	</tr>
	    	<c:forEach var="list" items="${robotSystemSetting}" varStatus="idx">
	    	<tr>
	    		<td id="width1">
	    			${idx.index+1}
	    		</td>
	    		<td id="width4">
	    			${list.robot_name}
	    		</td>
	    		<td id="width4">
	    			<c:choose>
			    		<c:when test="${list.robot_status eq '1'}">
			    			启用
			    		</c:when>
			    		<c:when test="${list.robot_status eq '2'}">
			    			停用
			    		</c:when>
			    		<c:when test="${list.robot_status eq '3'}">
			    			备用
			    		</c:when>
			    	</c:choose>
	    		</td>
	    		<td  id="width4">
	    		<c:choose>
		    		<c:when test="${list.robot_money_status eq '00'}">
		    		票价<br/>
		    		</c:when>
	    		</c:choose>
	    		<c:choose>
		    		<c:when test="${list.robot_register_status eq '00'}">
		    		认证<br/>
		    		</c:when>
	    		</c:choose>
	    		<c:choose>
		    		<c:when test="${list.robot_book_status eq '00'}">
		    		预订<br/>
		    		</c:when>
		    	</c:choose>
	    			<c:choose>
		    		<c:when test="${list.robot_pay_status eq '00'}">
		    		支付<br/>
		    		</c:when>
	    		</c:choose>
	    			<c:choose>
		    		<c:when test="${list.robot_check_status eq '00'}">
		    		核验<br/>
		    		</c:when>
	    		</c:choose>
	    			<c:choose>
		    		<c:when test="${list.robot_cancel_status eq '00'}">
		    		取消<br/>
		    		</c:when>
	    		</c:choose>
	    			<c:choose>
		    		<c:when test="${list.robot_endorse_status eq '00'}">
		    		改签<br/>
		    		</c:when>
	    		</c:choose>
	    			<c:choose>
		    		<c:when test="${list.robot_refund_status eq '00'}">
		    		退票<br/>
		    		</c:when>
	    		</c:choose>
	    		<c:choose>
		    		<c:when test="${list.robot_query_status eq '00'}">
		    		余票<br/>
		    		</c:when>
	    		</c:choose>
	    		<c:choose>
		    		<c:when test="${list.robot_delete_status eq '00'}">
		    		删除<br/>
		    		</c:when>
	    		</c:choose>
	    		<c:choose>
		    		<c:when test="${list.robot_enroll_status eq '00'}">
		    		注册<br/>
		    		</c:when>
	    		</c:choose>
	    		<c:choose>
		    		<c:when test="${list.robot_activate_status eq '00'}">
		    		激活<br/>
		    		</c:when>
	    		</c:choose>
	    		</td>	
	    		<td id="width10">
							<c:forEach items="${robot_channelList}" var="rc" varStatus="index">
							<div class="ser-item" style="float:left;white-space:nowrap;width: 45px;padding-left:3px;">
							<input type="checkbox" id="robot_channel${index.count }_${list.robot_id }" 
								name="robot_channel_${list.robot_id }" value="${rc.key }"
								<c:if test="${fn:contains(list.robot_channel, rc.key ) }">checked="checked"</c:if> />
							<label for="robot_channel${index.count }_${list.robot_id }">
								${rc.value }
							</label>
							</div>
						</c:forEach>
						<div class="ser-item" style="float:left;white-space:nowrap;width: 45px;padding-left:8px;">
						<a style="color:#1C86EE;" href="javascript:changeNewRobotChannel('${list.robot_id}');">提交</a>
						</div>
	    		</td>
	    		<td id="function">
	    		<span style="display:block;margin-top:5px;">
		    		<c:choose>
				    	<c:when test="${list.robot_money_status eq '00'}">
				   			<input type="button" class="btn0" value="停用" onclick="stopRobotAction('${list.robot_id}','${list.robot_status}','11','0')"/>
				   			<input type="button" class="btn0" value="短信停用" onclick="stopRobotAction('${list.robot_id}','${list.robot_status}','11','1')"/>
				   			空闲<span style="color:#f60;border:0;">  ${list.robot_money_num }</span>
				    	</c:when>
				    	<c:when test="${list.robot_money_status eq '22'}">
				    		<input type="button" class="btn2" value="置为备用" onclick="changeRobotAction('${list.robot_id}','${list.robot_status}','11')"/>
				    		<span style="color:red;border:0;"> <br /> ${stopReason[list.robot_money_stopReason] }</span>
				    		<input type="button" class="btn4" value="关闭代理" onclick="channgeProxy('${list.robot_id}','11','22')"/>
				    	</c:when>
				    	<c:when test="${list.robot_money_status eq '33'}">
				    		<input type="button" class="btn1" value="启用" onclick="changeRobotAction('${list.robot_id}','${list.robot_status}','11')"/>
				    		<span style="color:red;border:0;"> <br /> ${stopReason[list.robot_money_stopReason] }</span>
				    		<input type="button" class="btn4" value="启用代理" onclick="channgeProxy('${list.robot_id}','11','00')"/>
				    	</c:when>
				    </c:choose>
				     <c:if test="${list.robot_money_proxy ne null}"><span style="color:#F55E0C;border:0;"> <br /> ${proxyStatus[list.robot_money_proxy] }</span></c:if>
				    </span>
	    		</td>
	    		<td id="function">
	    		<span style="display:block;margin-top:5px;">
		    		<c:choose>
				    	<c:when test="${list.robot_register_status eq '00'}">
				   			<input type="button" class="btn0" value="停用" onclick="stopRobotAction('${list.robot_id}','${list.robot_status}','10','0')"/>
				   			<input type="button" class="btn0" value="短信停用" onclick="stopRobotAction('${list.robot_id}','${list.robot_status}','10','1')"/>
				   			空闲<span style="color:#f60;border:0;">  ${list.robot_register_num }</span>
				    	</c:when>
				    	<c:when test="${list.robot_register_status eq '22'}">
				    		<input type="button" class="btn2" value="置为备用" onclick="changeRobotAction('${list.robot_id}','${list.robot_status}','10')"/>
				    		<span style="color:red;border:0;"><br/>  ${stopReason[list.robot_register_stopReason] }</span>
				    		<input type="button" class="btn4" value="关闭代理" onclick="channgeProxy('${list.robot_id}','10','22')"/>
				    	</c:when>
				    	<c:when test="${list.robot_register_status eq '33'}">
				    		<input type="button" class="btn1" value="启用" onclick="changeRobotAction('${list.robot_id}','${list.robot_status}','10')"/>
				    		<span style="color:red;border:0;"><br/>  ${stopReason[list.robot_register_stopReason] }</span>
				    		<input type="button" class="btn4" value="启用代理" onclick="channgeProxy('${list.robot_id}','10','00')"/>
				    	</c:when>
				    </c:choose>
				      <c:if test="${list.robot_register_proxy ne null}"><span style="color:#F55E0C;border:0;"> <br /> ${proxyStatus[list.robot_register_proxy] }</span></c:if>
				    </span>
	    		</td>
	    		<td id="function">
	    		<span style="display:block;margin-top:5px;">
		    		<c:choose>
				    	<c:when test="${list.robot_book_status eq '00'}">
				   			<input type="button" class="btn0" value="停用" onclick="stopRobotAction('${list.robot_id}','${list.robot_status}','1','0')"/>
				   			<input type="button" class="btn0" value="短信停用" onclick="stopRobotAction('${list.robot_id}','${list.robot_status}','1','1')"/>
				    		空闲<span style="color:#f60;border:0;">  ${list.robot_book_num }</span>
				    	</c:when>
				    	<c:when test="${list.robot_book_status eq '22'}">
				    		<input type="button" class="btn2" value="置为备用" onclick="changeRobotAction('${list.robot_id}','${list.robot_status}','1')"/>
				    		<span style="color:red;border:0;"><br/>  ${stopReason[list.robot_book_stopReason] }</span>
				    		<input type="button" class="btn4" value="关闭代理" onclick="channgeProxy('${list.robot_id}','1','22')"/>
				    	</c:when>
				    	<c:when test="${list.robot_book_status eq '33'}">
				    		<input type="button" class="btn1" value="启用" onclick="changeRobotAction('${list.robot_id}','${list.robot_status}','1')"/>
				    		<span style="color:red;border:0;"><br/>  ${stopReason[list.robot_book_stopReason] }</span>
				    		<input type="button" class="btn4" value="启用代理" onclick="channgeProxy('${list.robot_id}','1','00')"/>
				    	</c:when>
				    </c:choose>
				      <c:if test="${list.robot_book_proxy ne null}"><span style="color:#F55E0C;border:0;"> <br /> ${proxyStatus[list.robot_book_proxy] }</span></c:if>
				    </span>
	    		</td>
	    		<td id="function">
	    		<span style="display:block;margin-top:5px;">
		    		<c:choose>
				    	<c:when test="${list.robot_pay_status eq '00'}">
				   			<input type="button" class="btn0" value="停用" onclick="stopRobotAction('${list.robot_id}','${list.robot_status}','3','0')"/>
				   			<input type="button" class="btn0" value="短信停用" onclick="stopRobotAction('${list.robot_id}','${list.robot_status}','3','1')"/>
				   			空闲
				   			<span style="color:#f60;border:0;margin:0 auto;"> ${list.robot_pay_num }</span>
				   			<br/>
				   			支付宝<br/>
				   			<input value="${list.zhifubao}" id="${robot_id }" style="color:red;width:15px;border:0;"/>
				    	</c:when>
				    	<c:when test="${list.robot_pay_status eq '22'}">
				    		<input type="button" class="btn2" value="置为备用" onclick="changeRobotAction('${list.robot_id}','${list.robot_status}','3')"/>
				    		<span style="color:red;border:0;"><br/>  ${stopReason[list.robot_pay_stopReason] }</span>
				    		<input type="button" class="btn4" value="关闭代理" onclick="channgeProxy('${list.robot_id}','3','22')"/>
				    	</c:when>
				    	<c:when test="${list.robot_pay_status eq '33'}">
				    		<input type="button" class="btn1" value="启用" onclick="changeRobotAction('${list.robot_id}','${list.robot_status}','3')"/>
				    		<span style="color:red;border:0;"><br/>  ${stopReason[list.robot_pay_stopReason] }</span>
				    		<input type="button" class="btn4" value="启用代理" onclick="channgeProxy('${list.robot_id}','3','00')"/>
				    	</c:when>
				    </c:choose>
				      <c:if test="${list.robot_pay_proxy ne null}"><span style="color:#F55E0C;border:0;"> <br /> ${proxyStatus[list.robot_pay_proxy] }</span></c:if>
				    </span>
	    		</td>
	    		<td id="function">
	    		<span style="display:block;margin-top:5px;">
		    		<c:choose>
				    	<c:when test="${list.robot_check_status eq '00'}">
				   			<input type="button" class="btn0" value="停用" onclick="stopRobotAction('${list.robot_id}','${list.robot_status}','5','0')"/>
				   			<input type="button" class="btn0" value="短信停用" onclick="stopRobotAction('${list.robot_id}','${list.robot_status}','5','1')"/>
				   			空闲<span style="color:#f60;border:0;">  ${list.robot_check_num }</span>
				    	</c:when>
				    	<c:when test="${list.robot_check_status eq '22'}">
				    		<input type="button" class="btn2" value="置为备用" onclick="changeRobotAction('${list.robot_id}','${list.robot_status}','5')"/>
				    		<span style="color:red;border:0;"><br/>  ${stopReason[list.robot_check_stopReason] }</span>
				    		<input type="button" class="btn4" value="关闭代理" onclick="channgeProxy('${list.robot_id}','5','22')"/>
				    	</c:when>
				    	<c:when test="${list.robot_check_status eq '33'}">
				    		<input type="button" class="btn1" value="启用" onclick="changeRobotAction('${list.robot_id}','${list.robot_status}','5')"/>
				    		<span style="color:red;border:0;"><br/>  ${stopReason[list.robot_check_stopReason] }</span>
				    		<input type="button" class="btn4" value="启用代理" onclick="channgeProxy('${list.robot_id}','5','00')"/>
				    	</c:when>
				    </c:choose>
				      <c:if test="${list.robot_check_proxy ne null}"><span style="color:#F55E0C;border:0;"> <br /> ${proxyStatus[list.robot_check_proxy] }</span></c:if>
				    </span>
	    		</td>
	    		<td id="function">
	    		<span style="display:block;margin-top:5px;">
		    		<c:choose>
				    	<c:when test="${list.robot_cancel_status eq '00'}">
				   			<input type="button" class="btn0" value="停用" onclick="stopRobotAction('${list.robot_id}','${list.robot_status}','6','0')"/>
				   			<input type="button" class="btn0" value="短信停用" onclick="stopRobotAction('${list.robot_id}','${list.robot_status}','6','1')"/>
				   			空闲<span style="color:#f60;border:0;">  ${list.robot_cancel_num }</span>
				    	</c:when>
				    	<c:when test="${list.robot_cancel_status eq '22'}">
				    		<input type="button" class="btn2" value="置为备用" onclick="changeRobotAction('${list.robot_id}','${list.robot_status}','6')"/>
				    		<span style="color:red;border:0;"><br/>  ${stopReason[list.robot_cancel_stopReason] }</span>
				    		<input type="button" class="btn4" value="关闭代理" onclick="channgeProxy('${list.robot_id}','6','22')"/>
				    	</c:when>
				    	<c:when test="${list.robot_cancel_status eq '33'}">
				    		<input type="button" class="btn1" value="启用" onclick="changeRobotAction('${list.robot_id}','${list.robot_status}','6')"/>
				    		<span style="color:red;border:0;"><br/>  ${stopReason[list.robot_cancel_stopReason] }</span>
				    		<input type="button" class="btn4" value="启用代理" onclick="channgeProxy('${list.robot_id}','6','00')"/>
				    	</c:when>
				    </c:choose>
				      <c:if test="${list.robot_cancel_proxy ne null}"><span style="color:#F55E0C;border:0;"> <br /> ${proxyStatus[list.robot_cancel_proxy] }</span></c:if>
				    </span>
	    		</td>
	    		<td id="function">
	    		<span style="display:block;margin-top:5px;">
		    		<c:choose>
				    	<c:when test="${list.robot_endorse_status eq '00'}">
				   			<input type="button" class="btn0" value="停用" onclick="stopRobotAction('${list.robot_id}','${list.robot_status}','7','0')"/>
				   			<input type="button" class="btn0" value="短信停用" onclick="stopRobotAction('${list.robot_id}','${list.robot_status}','7','1')"/>
				   			空闲<span style="color:#f60;border:0;">  ${list.robot_endorse_num }</span>
				    	</c:when>
				    	<c:when test="${list.robot_endorse_status eq '22'}">
				    		<input type="button" class="btn2" value="置为备用" onclick="changeRobotAction('${list.robot_id}','${list.robot_status}','7')"/>
				    		<span style="color:red;border:0;"><br/>  ${stopReason[list.robot_endorse_stopReason] }</span>
				    		<input type="button" class="btn4" value="关闭代理" onclick="channgeProxy('${list.robot_id}','7','22')"/>
				    	</c:when>
				    	<c:when test="${list.robot_endorse_status eq '33'}">
				    		<input type="button" class="btn1" value="启用" onclick="changeRobotAction('${list.robot_id}','${list.robot_status}','7')"/>
				    		<span style="color:red;border:0;"><br/>  ${stopReason[list.robot_endorse_stopReason] }</span>
				    		<input type="button" class="btn4" value="启用代理" onclick="channgeProxy('${list.robot_id}','7','00')"/>
				    	</c:when>
				    </c:choose>
				      <c:if test="${list.robot_endorse_proxy ne null}"><span style="color:#F55E0C;border:0;"> <br /> ${proxyStatus[list.robot_endorse_proxy] }</span></c:if>
				    </span>
	    		</td>
	    		<td id="function">
	    		<span style="display:block;margin-top:5px;">
		    		<c:choose>
				    	<c:when test="${list.robot_refund_status eq '00'}">
				   			<input type="button" class="btn0" value="停用" onclick="stopRobotAction('${list.robot_id}','${list.robot_status}','8','0')"/>
				   			<input type="button" class="btn0" value="短信停用" onclick="stopRobotAction('${list.robot_id}','${list.robot_status}','8','1')"/>
				   			空闲<span style="color:#f60;border:0;">  ${list.robot_refund_num }</span>
				    	</c:when>
				    	<c:when test="${list.robot_refund_status eq '22'}">
				    		<input type="button" class="btn2" value="置为备用" onclick="changeRobotAction('${list.robot_id}','${list.robot_status}','8')"/>
				    		<span style="color:red;border:0;"><br/>  ${stopReason[list.robot_refund_stopReason] }</span>
				    		<input type="button" class="btn4" value="关闭代理" onclick="channgeProxy('${list.robot_id}','8','22')"/>
				    	</c:when>
				    	<c:when test="${list.robot_refund_status eq '33'}">
				    		<input type="button" class="btn1" value="启用" onclick="changeRobotAction('${list.robot_id}','${list.robot_status}','8')"/>
				    		<span style="color:red;border:0;"><br/>  ${stopReason[list.robot_refund_stopReason] }</span>
				    		<input type="button" class="btn4" value="启用代理" onclick="channgeProxy('${list.robot_id}','8','00')"/>
				    	</c:when>
				    </c:choose>
				      <c:if test="${list.robot_refund_proxy ne null}"><span style="color:#F55E0C;border:0;"> <br /> ${proxyStatus[list.robot_refund_proxy] }</span></c:if>
				    </span>
	    		</td>
	    		<td id="function">
	    		<span style="display:block;margin-top:5px;">
		    		<c:choose>
				    	<c:when test="${list.robot_query_status eq '00'}">
				   			<input type="button" class="btn0" value="停用" onclick="stopRobotAction('${list.robot_id}','${list.robot_status}','9','0')"/>
				   			<input type="button" class="btn0" value="短信停用" onclick="stopRobotAction('${list.robot_id}','${list.robot_status}','9','1')"/>
				   			空闲<span style="color:#f60;border:0;">  ${list.robot_query_num }</span>
				    	</c:when>
				    	<c:when test="${list.robot_query_status eq '22'}">
				    		<input type="button" class="btn2" value="置为备用" onclick="changeRobotAction('${list.robot_id}','${list.robot_status}','9')"/>
				    		<span style="color:red;border:0;"><br/>  ${stopReason[list.robot_query_stopReason] }</span>
				    		<input type="button" class="btn4" value="关闭代理" onclick="channgeProxy('${list.robot_id}','9','22')"/>
				    	</c:when>
				    	<c:when test="${list.robot_query_status eq '33'}">
				    		<input type="button" class="btn1" value="启用" onclick="changeRobotAction('${list.robot_id}','${list.robot_status}','9')"/>
				    		<span style="color:red;border:0;"><br/>  ${stopReason[list.robot_query_stopReason] }</span>
				    		<input type="button" class="btn4" value="启用代理" onclick="channgeProxy('${list.robot_id}','9','00')"/>
				    	</c:when>
				    </c:choose>
				      <c:if test="${list.robot_query_proxy ne null}"><span style="color:#F55E0C;border:0;"> <br /> ${proxyStatus[list.robot_query_proxy] }</span></c:if>
				    </span>
	    		</td>
	    		<td id="function">
	    		<span style="display:block;margin-top:5px;">
		    		<c:choose>
				    	<c:when test="${list.robot_delete_status eq '00'}">
				   			<input type="button" class="btn0" value="停用" onclick="stopRobotAction('${list.robot_id}','${list.robot_status}','13','0')"/>
				   			<input type="button" class="btn0" value="短信停用" onclick="stopRobotAction('${list.robot_id}','${list.robot_status}','13','1')"/>
				   			空闲<span style="color:#f60;border:0;">  ${list.robot_delete_num }</span>
				    	</c:when>
				    	<c:when test="${list.robot_delete_status eq '22'}">
				    		<input type="button" class="btn2" value="置为备用" onclick="changeRobotAction('${list.robot_id}','${list.robot_status}','13')"/>
				    		<span style="color:red;border:0;"><br/>  ${stopReason[list.robot_delete_stopReason] }</span>
				    		<input type="button" class="btn4" value="关闭代理" onclick="channgeProxy('${list.robot_id}','13','22')"/>
				    	</c:when>
				    	<c:when test="${list.robot_delete_status eq '33'}">
				    		<input type="button" class="btn1" value="启用" onclick="changeRobotAction('${list.robot_id}','${list.robot_status}','13')"/>
				    		<span style="color:red;border:0;"><br/>  ${stopReason[list.robot_delete_stopReason] }</span>
				    		<input type="button" class="btn4" value="启用代理" onclick="channgeProxy('${list.robot_id}','13','00')"/>
				    	</c:when>
				    </c:choose>
				      <c:if test="${list.robot_delete_proxy ne null}"><span style="color:#F55E0C;border:0;"> <br /> ${proxyStatus[list.robot_delete_proxy] }</span></c:if>
				    </span>
	    		</td>
	    		<td id="function">
	    		<span style="display:block;margin-top:5px;">
		    		<c:choose>
				    	<c:when test="${list.robot_enroll_status eq '00'}">
				   			<input type="button" class="btn0" value="停用" onclick="stopRobotAction('${list.robot_id}','${list.robot_status}','14','0')"/>
				   			<input type="button" class="btn0" value="短信停用" onclick="stopRobotAction('${list.robot_id}','${list.robot_status}','14','1')"/>
				   			空闲<span style="color:#f60;border:0;">  ${list.robot_enroll_num }</span>
				    	</c:when>
				    	<c:when test="${list.robot_enroll_status eq '22'}">
				    		<input type="button" class="btn2" value="置为备用" onclick="changeRobotAction('${list.robot_id}','${list.robot_status}','14')"/>
				    		<span style="color:red;border:0;"><br/>  ${stopReason[list.robot_enroll_stopReason] }</span>
				    		<input type="button" class="btn4" value="关闭代理" onclick="channgeProxy('${list.robot_id}','14','22')"/>
				    	</c:when>
				    	<c:when test="${list.robot_enroll_status eq '33'}">
				    		<input type="button" class="btn1" value="启用" onclick="changeRobotAction('${list.robot_id}','${list.robot_status}','14')"/>
				    		<span style="color:red;border:0;"><br/>  ${stopReason[list.robot_enroll_stopReason] }</span>
				    		<input type="button" class="btn4" value="启用代理" onclick="channgeProxy('${list.robot_id}','14','00')"/>
				    	</c:when>
				    </c:choose>
				      <c:if test="${list.robot_enroll_proxy ne null}"><span style="color:#F55E0C;border:0;"> <br /> ${proxyStatus[list.robot_enroll_proxy] }</span></c:if>
				    </span>
	    		</td>
	    		<td id="function">
	    		<span style="display:block;margin-top:5px;">
		    		<c:choose>
				    	<c:when test="${list.robot_activate_status eq '00'}">
				   			<input type="button" class="btn0" value="停用" onclick="stopRobotAction('${list.robot_id}','${list.robot_status}','15','0')"/>
				   			<input type="button" class="btn0" value="短信停用" onclick="stopRobotAction('${list.robot_id}','${list.robot_status}','15','1')"/>
				   			空闲<span style="color:#f60;border:0;">  ${list.robot_activate_num }</span>
				    	</c:when>
				    	<c:when test="${list.robot_activate_status eq '22'}">
				    		<input type="button" class="btn2" value="置为备用" onclick="changeRobotAction('${list.robot_id}','${list.robot_status}','15')"/>
				    		<span style="color:red;border:0;"><br/>  ${stopReason[list.robot_activate_stopReason] }</span>
				    		<input type="button" class="btn4" value="关闭代理" onclick="channgeProxy('${list.robot_id}','15','22')"/>
				    		
				    	</c:when>
				    	<c:when test="${list.robot_activate_status eq '33'}">
				    		<input type="button" class="btn1" value="启用" onclick="changeRobotAction('${list.robot_id}','${list.robot_status}','15')"/>
				    		<span style="color:red;border:0;"><br/>  ${stopReason[list.robot_activate_stopReason] }</span>
				    		<input type="button" class="btn4" value="启用代理" onclick="channgeProxy('${list.robot_id}','15','00')"/>
				    	</c:when>
				    </c:choose>
				      <c:if test="${list.robot_activate_proxy ne null}"><span style="color:#F55E0C;border:0;"> <br /> ${proxyStatus[list.robot_activate_proxy] }</span></c:if>
				    </span>
	    		</td>
	    		
	    		
	    		<td  id="function">
	    			${list.opt_name }
	    		</td>
	    		<td align="left"  id="width12">
	    		 <% if ("2".equals(loginUserVo.getUser_level())){ %>
	    			&nbsp;<a  href="javascript:deleteRobotStatus('${list.robot_id}')">删除</a>
	    				 <%} %>
		    		<c:choose>
				    	<c:when test="${list.robot_status eq '1'}">
				    	 <% if (!"2".equals(loginUserVo.getUser_level())){ %>
				    		&nbsp;&nbsp;&nbsp; <%} %>
				    		<a href="javascript:changeNewRobotStatus('${list.robot_id}','0','1')">停用</a>
				    		<a href="javascript:changeNewRobotStatus('${list.robot_id}','1','1')">短信停用</a>
				    	</c:when>
				    	<c:when test="${list.robot_status eq '2'}">
				    	&nbsp;&nbsp;&nbsp;
				    		<a href="javascript:changeNewRobotStatus('${list.robot_id}','0','2')">置为备用</a>
				    	</c:when>
				    	<c:when test="${list.robot_status eq '3'}">
				    		 <% if (!"2".equals(loginUserVo.getUser_level())){ %>
				    		&nbsp; <%} %>&nbsp;&nbsp;<a href="javascript:changeNewRobotStatus('${list.robot_id}','0','3')">启用</a>
				    	</c:when>
				    </c:choose>
				   	 <br />
				   	  <% if (!"2".equals(loginUserVo.getUser_level())){ %>
				    		&nbsp;&nbsp;<%} %>
				  		&nbsp;<a href="javascript:opt_rizhi('${list.robot_id}')">操作日志</a>
				  		 <% if ("2".equals(loginUserVo.getUser_level())){ %>
				   		&nbsp;<a href="javascript:changeNewRobot('${list.robot_id}','${list.robot_name}')">修改</a>
				   		 <%} %>
	    		</td>
	    	</tr>
	    	</c:forEach>
	    	</table>
	    		<jsp:include page="/pages/common/paging.jsp" />
	    </c:if>
    </form>
    </div>
  </body>
</html>
