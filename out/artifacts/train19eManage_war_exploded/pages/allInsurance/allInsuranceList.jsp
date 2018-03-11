<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>保险管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<style>
			.book_manage .ser{margin:10px 0px;}
		</style>
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
		<script type="text/javascript">
		<%
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user_level = loginUserVo.getUser_level();
	%>
		function exportExcelInsurance() {
			$("form:first").attr("action","/orderForExcel/excelexportAllInsurance.do");
			$("form:first").submit();
			$("form:first").attr("action","/allInsurance/queryInsuranceList.do");
		}
		function selectAll(){
			var checklist = document.getElementsByName("insueance_status");
			if(document.getElementById("controlAll").checked){
				for(var i=0; i<checklist.length; i++){
					checklist[i].checked = 1;
				}
			}else{
				for(var j=0; j<checklist.length; j++){
					checklist[j].checked = 0;
				}
			}
		}
		function selectAllBxchannel(){
			var checklist = document.getElementsByName("insueance_bx_channel");
			if(document.getElementById("controlAllBxchannel").checked){
				for(var i=0; i<checklist.length; i++){
					checklist[i].checked = 1;
				}
			}else{
				for(var j=0; j<checklist.length; j++){
					checklist[j].checked = 0;
				}
			}
		}
		function selectAllChannel(){
			var checklist = document.getElementsByName("channel");
			if(document.getElementById("controlAllChannel").checked){
				for(var i=0; i<checklist.length; i++){
					checklist[i].checked = 1;
				}
			}else{
				for(var j=0; j<checklist.length; j++){
					checklist[j].checked = 0;
				}

			}
		}
		/** 渠道分类 全选
		function selectAllChannel(){
			var checklist = document.getElementsByName("channel");
			if(document.getElementById("controlAllChannel").checked){
				for(var i=0; i<checklist.length; i++){
					checklist[i].checked = 1;
				}
			}else{
				for(var j=0; j<checklist.length; j++){
					checklist[j].checked = 0;
				}
			}
			var checklist = document.getElementsByName("sort");
			if(document.getElementById("controlAllChannel").checked){
				for(var i=0; i<checklist.length; i++){
					checklist[i].checked = 1;
				}
			}else{
				for(var j=0; j<checklist.length; j++){
					checklist[j].checked = 0;
				}
			}
		}
		*/
		function div() {/**类别：11公司 companyDiv 、22商户 merchantDiv 、33内嵌 innerDiv 、44B2C b2cDiv  */
		    var cheLength = document.getElementsByName("sort");
		    var controlAllChannel = document.getElementsByName("controlAllChannel");
		    for(var i=0; i<cheLength.length; i++) {
			    if($("#controlAllChannel").attr('checked')==undefined){//全选---未选中
			    		if(cheLength[i].checked && cheLength[i].value == '11'){
				           document.getElementById('companyDiv').style.display = "block";
				        } else if(!cheLength[i].checked && cheLength[i].value == '11'){
				           document.getElementById('companyDiv').style.display = "none";
				        } else if(cheLength[i].checked && cheLength[i].value == '22'){
					        document.getElementById('merchantDiv').style.display = "block";
					    } else if(!cheLength[i].checked && cheLength[i].value == '22'){
					        document.getElementById('merchantDiv').style.display = "none";
					    } else if(cheLength[i].checked && cheLength[i].value == '33'){
					        document.getElementById('innerDiv').style.display = "block";
					    } else if(!cheLength[i].checked && cheLength[i].value == '33'){
					        document.getElementById('innerDiv').style.display = "none";
					    } else if(cheLength[i].checked && cheLength[i].value == '44'){
					        document.getElementById('b2cDiv').style.display = "block";
					    } else if(!cheLength[i].checked && cheLength[i].value == '44'){
					        document.getElementById('b2cDiv').style.display = "none";
					    } 
				}else{
					document.getElementById('companyDiv').style.display = "none";
					document.getElementById('merchantDiv').style.display = "none";
					document.getElementById('innerDiv').style.display = "none";
					document.getElementById('b2cDiv').style.display = "none";
				}
		    }
		  }
		  
		  function statusSendAgain(order_id,bx_id){
		  if(confirm("确认重新投保么？")) {
		  var url ="/allInsurance/updateInsuranceStatusSendAgain.do?order_id=" +order_id+"&bx_id="+bx_id;
		 	  $.get(url,function(data){
					if(data == "true"){
						$("form:first").submit();
						}
				});
		  }
		  }
		  function statusNeedCancel(order_id,bx_id,type){
		  if(type=="1"){
			    if(confirm("确认退保么？")) {
			     var url = "/allInsurance/updateInsuranceStatusNeedCancel.do?bx_id=" +bx_id+"&order_id="+order_id;
				  $.get(url,function(data){
					if(data == "true"){
						$("form:first").submit();
						}
				});
			  }
		  }else{
			    if(confirm("确认重新投保么？")) {
			    var url = "/allInsurance/updateInsuranceStatusNeedCancel.do?bx_id=" +bx_id+"&order_id="+order_id;
				  $.get(url,function(data){
					if(data == "true"){
						$("form:first").submit();
						}
				});
			  }
		  }
		 }
		  
		</script>
	</head>
	<body onload="div();">
	<div></div>
		<div class="book_manage oz">
			<form action="/allInsurance/queryInsuranceList.do" method="post" name="myform" id="myform">
				<div style="border: 0px solid #00CC00; margin: 10px;">
					<ul class="order_num oz" >
						<li>
							订单号&nbsp;&nbsp;：
							<input type="text" class="text" name="order_id" value="${order_id }" />
						</li>
						<li>
							保险单号：
							<input type="text" class="text" name="bx_code" value="${bx_code }"/>
						</li>
						<li>
							联系电话：
							<input type="text" class="text" name="telephone" value="${telephone }" />
						</li>
					</ul>
					<ul class="order_num oz" style="margin-top: 10px;">
						<li>
							开始时间：
							<input type="text" class="text" name="begin_create_time" readonly="readonly" value="${begin_create_time }"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})"/> 
						</li>
						<li>
							结束时间：
								<input type="text" class="text" name="end_create_time" readonly="readonly" value="${end_create_time}"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" /> 
						</li>
					</ul>
					<ul class="ser oz" style="margin-top: 14px;">
						<li>
							保险状态：
							<input type="checkbox" onclick="selectAll()" name="controlAll" style="controlAll" id="controlAll"/><label for="">&nbsp;全部</label>
						</li>
						<c:forEach items="${insueance_statuses }" var="s" varStatus="index">
							<li>
								<input type="checkbox" id="insueance_status${index.count }"
									name="insueance_status" value="${s.key }"
									<c:if test="${fn:contains(insueance_Str, s.key ) }">checked="checked"</c:if> />
								<label for="insueance_status${index.count }">
									${s.value }
								</label>
							</li>
						</c:forEach>
					</ul>
					
					<ul class="ser oz" style="margin-top: 14px;">
						<li>
							供保公司：
							<input type="checkbox" onclick="selectAllBxchannel()" name="controlAllBxchannel" style="controlAllBxchannel" id="controlAllBxchannel"/><label for="">&nbsp;全部</label>
						</li>
						<c:forEach items="${insueance_bx_channel }" var="b" varStatus="index">
							<li>
								<input type="checkbox" id="insueance_bx_channel${index.count }"
									name="insueance_bx_channel" value="${b.key }"
									<c:if test="${fn:contains(bx_channel_Str, b.key ) }">checked="checked"</c:if> />
								<label for="insueance_bx_channel${index.count }">
									${b.value }
								</label>
							</li>
						</c:forEach>
					</ul>
					<!--  
					<ul class="ser oz">
							<li>
								类&nbsp;&nbsp;&nbsp;&nbsp;别：
								<input type="checkbox" onclick="selectAllChannel();div();" name="controlAllChannel" style="controlAllChannel" id="controlAllChannel" <c:if test="${controlAllChannel eq 'on' }">checked="checked"</c:if> /><label for="controlAllChannel">&nbsp;全部</label>
									<c:forEach items="${sort_channel}" var="s" varStatus="index">
										<input type="checkbox" id="sort${index.count }"
												name="sort" value="${s.key }" onclick="div();"
													<c:if test="${fn:contains(sort, s.key ) }">checked="checked"</c:if> />
											<label for="sort${index.count }">
												${s.value }
											</label>
									</c:forEach>
								</li>
							</ul>
							
							<div id="companyDiv" style="display:none">
							<ul class="ser oz">
								<li>
								<label style="float:left;"> 公司渠道：&nbsp;</label>
								<table style="width:870px;display:inline;border:none;"><tr style="border:none;"><td style="text-align:left;border:none;">
									<c:forEach items="${company_channel }" var="s" varStatus="index">
											<input type="checkbox" id="companychannel${index.count }"
												name="channel" value="${s.key }"
												<c:if test="${fn:contains(channelStr, s.key ) }">checked="checked"</c:if> />
											<label for="companychannel${index.count }">
												${s.value }
											</label>
									</c:forEach>
								</td></tr></table>
								</li>
					        </ul>
					        </div>
					        <div id="merchantDiv" style="display:none">
							<ul class="ser oz">
								<li>
								<label style="float:left;"> 商户渠道：&nbsp;</label>
								<table style="width:900px;display:inline;border:none;"><tr style="border:none;"><td style="text-align:left;border:none;">
									<c:forEach items="${merchant_channel }" var="s" varStatus="index">
											<input type="checkbox" id="merchantchannel${index.count }"
												name="channel" value="${s.key }"
												<c:if test="${fn:contains(channelStr, s.key ) }">checked="checked"</c:if> />
											<label for="merchantchannel${index.count }">
												${s.value }
											</label>
									</c:forEach>
								</td></tr></table>
								</li>
					        </ul>
					        </div>
					        <div id="innerDiv" style="display:none">
							<ul class="ser oz">
								<li>
								<label style="float:left;"> 内嵌渠道：&nbsp;</label>
								<table style="width:870px;display:inline;border:none;"><tr style="border:none;"><td style="text-align:left;border:none;">
									<c:forEach items="${inner_channel }" var="s" varStatus="index">
											<input type="checkbox" id="innerchannel${index.count }"
												name="channel" value="${s.key }"
												<c:if test="${fn:contains(channelStr, s.key ) }">checked="checked"</c:if> />
											<label for="innerchannel${index.count }">
												${s.value }
											</label>
									</c:forEach>
								</td></tr></table>
								</li>
					        </ul>
					        </div>
					        <div id="b2cDiv" style="display:none">
							<ul class="ser oz">
								<li>
								<label style="float:left;"> B2C&nbsp;渠道：&nbsp;</label>
								<table style="width:870px;display:inline;border:none;"><tr style="border:none;"><td style="text-align:left;border:none;">
									<c:forEach items="${b2c_channel }" var="s" varStatus="index">
											<input type="checkbox" id="b2cchannel${index.count }"
												name="channel" value="${s.key }"
												<c:if test="${fn:contains(channelStr, s.key ) }">checked="checked"</c:if> />
											<label for="b2cchannel${index.count }">
												${s.value }
											</label>
									</c:forEach>
								</td></tr></table>
								</li>
					        </ul>
					        </div>
					      -->  
					<dl class="oz">
						<dt style="float:left;padding-left:40px;line-height:22px;">渠道：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</dt>
						<dd  style="float:left;width:1000px;">
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="checkbox" onclick="selectAllChannel()" name="controlAllChannel" style="controlAllChannel" id="controlAllChannel"/><label for="controlAllChannel">&nbsp;全部&nbsp;&nbsp;</label>
							</div>
						<c:forEach items="${channel_types }" var="s" varStatus="index">
						<c:if test="${s.key ne '301016' && s.key ne '30101601' && s.key ne '30101602'}">
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="checkbox" id="channel${index.count }" name="channel" value="${s.key }" value="1"
									<c:if test="${fn:contains(channelStr, s.key ) }">checked="checked"</c:if> />
								<label for="channel${index.count }">
									${s.value }
								</label>
							</div>
							</c:if>
						</c:forEach>
						<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
							<input type="checkbox" id="channel01" name="channel" value="30101612"
								<c:if test="${fn:contains(channelStr, '30101612')}">checked="checked"</c:if> />
								<label for="channel01">利安</label>
							</div>
						</dd>
					</dl>
				</div>
				<p>
					<input type="submit" value="查 询" class="btn" />
					<%
						if ("2".equals(loginUserVo.getUser_level()) ) {
           			%>
					<input type="button" value="导出Excel" class="btn" onclick="exportExcelInsurance()"/>
					<!-- 
					 <a href="/allInsurance/plUpdateAgain.do">批量重新投保</a>
					 -->
					<%} %>
				</p>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #f0f0f0;">
							<th>
								序号
							</th>
							<th>
								订单号
							</th>
							<th>
								保险单号
							</th>
							<th>
								联系电话
							</th>
							<th>
								出发/到达
							</th>
							<th>
								用户姓名
							</th>
							<th>
								创建时间
							</th>
							<th>
								状态
							</th>
							<th>
								进货金额
							</th>
							<th>
								支付金额
							</th>
							<th>
								供保公司
							</th>
							<th>
								渠道
							</th>
							<th>
								失败原因
							</th>
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${insuranceList}" varStatus="idx">
							<c:choose>
								<c:when test="${list.bx_status !=null && list.bx_status ==0}">
		            		 		<tr style="background:#E0F3ED; ">
		            			</c:when>
		            			<c:when test="${list.bx_status ==null}">
		            		 		<tr style="background:#F5A9A9; ">
		            			</c:when>
		            			<c:when test="${list.bx_status !=null && list.bx_status ==6}">
		            		 		<tr style="background:#F7BE81;">
		            			</c:when>
		            			<c:when test="${list.bx_status !=null && list.bx_status ==5}">
		            		 		<tr style="background:#AEFF00;">
		            			</c:when>
		            			<c:otherwise>
		            				<tr>
		            			</c:otherwise>
							</c:choose>
								<td>
									${idx.index+1}
								</td>
								<td>
									${list.order_id}
								</td>
								<td>
									${list.bx_code }
								</td>
								<td>
									${list.telephone }
								</td>
								<td>
									${list.from_name}/${list.to_name }
								</td>
								<td>
									${list.user_name }
								</td>
								<td>
									${list.create_time }
								</td>
								<c:choose>
									<c:when test="${list.bx_status ==null }">
										<td>
											未支付
										</td>
									</c:when>
									<c:otherwise>
										<td>
											${insueance_statuses[list.bx_status] }
										</td>
									</c:otherwise>
								</c:choose>
								<td>
									${list.buy_money }
								</td>
								<td>
									${list.pay_money }
								</td>
								<td>
									${insueance_bx_channel[list.bx_channel] }
								</td>
								<td>
									<c:choose>
										<c:when test="${! empty channel_types[list.channel] }">
											${channel_types[list.channel] }
										</c:when>
										<c:otherwise>
											${merchantMap[list.channel] }
										</c:otherwise>
									</c:choose>
								</td>
								<td>
									${list.fail_reason }
								</td>
								<td>
									<a href="/allInsurance/queryInsuranceInfo.do?bx_id=${list.bx_id }&order_id=${list.order_id }">明细</a>
									<c:if test="${list.bx_status != null && list.bx_status == 5}">
									<a href="javascript:statusSendAgain('${list.order_id}', '${list.bx_id}');">重新投保</a>
										<!--  <a href="/allInsurance/updateInsuranceStatusSendAgain.do?bx_id=${list.bx_id }&order_id=${list.order_id }"
											onclick="return confirm('确认重新投保么？')">重新投保</a>  -->
											
									 </c:if>
									 <c:if test="${list.bx_status != null && list.bx_status eq '2'}">
									 <a href="javascript:statusNeedCancel('${list.order_id}', '${list.bx_id}','1');">退保</a>
										<!--  <a href="/allInsurance/updateInsuranceStatusNeedCancel.do?bx_id=${list.bx_id }&order_id=${list.order_id }"
											onclick="return confirm('确认退保么？')">退保</a>  -->
									 </c:if>
									 <c:if test="${list.bx_status != null && list.bx_status eq '1'}">
									 <a href="javascript:statusSendAgain('${list.order_id}', '${list.bx_id}');">重新投保</a>
										<!--  <a href="/allInsurance/updateInsuranceStatusSendAgain.do?bx_id=${list.bx_id }&order_id=${list.order_id }"
											onclick="return confirm('确认重新投保么？')">重新投保</a>  -->
									 </c:if>
									 <c:if test="${list.bx_status != null && list.bx_status eq '3'}">
									 <a href="javascript:statusNeedCancel('${list.order_id}', '${list.bx_id}','2');">重新投保</a>
										<!--  <a href="/allInsurance/updateInsuranceStatusNeedCancel.do?bx_id=${list.bx_id }&order_id=${list.order_id }"
											onclick="return confirm('确认重新退保么？')">重新退保</a>  -->
									 </c:if>
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