<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo"%>
<%@ page import="com.l9e.transaction.vo.PageVo"%>
<%@ page import="java.util.*"%>
<%@ page import="com.l9e.util.JSONUtil"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>出票失败统计明细</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
		<script type="text/javascript">
	<%
		PageVo pageObject = (PageVo) request.getAttribute("pageBean");
		int pageIndex = pageObject.getPageIndex();
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user_level = loginUserVo.getUser_level();
	%>
	

$().ready(function(){
		if($("#channel").val()!="qunar" ){
			document.getElementById('divqunarcon').style.display = "none";
		    document.getElementById('divcon').style.display = "block";
		}else{
		    document.getElementById('divqunarcon').style.display = "block";
		    document.getElementById('divcon').style.display = "none";
		}
	});
	function queryInfo(order_id){
		var url="/acquire/queryAcquireInfo.do?order_id="+order_id+"&query_type=mingxi";
		showlayer('明细',url,'1000px','800px')
		}

	function submitForm(){
		var channel = $("#channel").val();
		var begin_info_time = $("#begin_info_time").val();
		$("form:first").attr("action","/failtj/queryFailtjInfo.do?channel="+channel+"&begin_info_time="+begin_info_time);
		$("form:first").submit();
		}

		//鼠标悬浮于“明细”，显示该订单的操作日志
	var heightDiv = 0; 
	function showdiv(order_id){  
	     var oSon = window.document.getElementById("hint");   
	     if (oSon == null) return;   
	     with (oSon){   
	 		 $.ajax({
				url:"/acquire/queryOrderOperHistory.do?order_id="+order_id,
				type: "POST",
				cache: false,
				dataType: "json",
				async: true,
				success: function(data){
					if(data=="" || data == null){
						return false;
					}
					var size = data.length;
					heightDiv = 0;
					for(var i=0; i<size; i++){
						var index = (parseInt(i)+1);
						if($("#index_"+index).innerText!=index){
							$("#historyTable").append("<tr line-height='15px'align='center' ><td id='index_'"+index+"''>"+index+"</td><td align='left'   style='word-break:break-all;'>"+data[i].order_optlog+"</td>"+
									"<td>"+data[i].create_time+"</td><td>"+data[i].opter+"</td></tr>");
							if(data[i].order_optlog.length>44){
								heightDiv = heightDiv + 30;
							}else{
								heightDiv = heightDiv + 15;
							}
						}
					}
				}
			});
	 		innerHTML = historyDiv.innerHTML; 
		    style.display = "block"; 
		    heightDiv = heightDiv + 106;//106为div中表格边距以及表头的高度86+20
		    style.pixelLeft = window.event.clientX + window.document.body.scrollLeft - 750;   
		    style.pixelTop = window.event.clientY + window.document.body.scrollTop - heightDiv;   
	    }   
	}   
	//鼠标离开“明细”，隐藏该订单的操作日志
	function hidediv(){   
	    var oSon = window.document.getElementById("hint");   
	    if(oSon == null) return;   
	    oSon.style.display="none";   
	} 
	//全选
	function selectAllError(){
		var checklist1 = document.getElementsByName("ERROR_INFO_1");
		var checklist2 = document.getElementsByName("ERROR_INFO_2");
		var checklist3 = document.getElementsByName("ERROR_INFO_3");
		var checklist4 = document.getElementsByName("ERROR_INFO_4");
		var checklist5 = document.getElementsByName("ERROR_INFO_5");
		var checklist6 = document.getElementsByName("ERROR_INFO_6");
		var checklist7 = document.getElementsByName("ERROR_INFO_7");
		var checklist8 = document.getElementsByName("ERROR_INFO_8");
		var checklist11 = document.getElementsByName("ERROR_INFO_11");
		var checklist12 = document.getElementsByName("ERROR_INFO_12");
		var checklist9 = document.getElementsByName("ERROR_INFO_9");
		if(document.getElementById("controlAllError").checked){
				checklist1[0].checked = 1;
				checklist2[0].checked = 1;
				checklist3[0].checked = 1;
				checklist4[0].checked = 1;
				checklist5[0].checked = 1;
				checklist6[0].checked = 1;
				checklist7[0].checked = 1;
				checklist8[0].checked = 1;
				checklist11[0].checked = 1;
				checklist12[0].checked = 1;
				checklist9[0].checked = 1;
		}else{
				checklist1[0].checked = 0;
				checklist2[0].checked = 0;
				checklist3[0].checked = 0;
				checklist4[0].checked = 0;
				checklist5[0].checked = 0;
				checklist6[0].checked = 0;
				checklist7[0].checked = 0;
				checklist8[0].checked = 0;
				checklist11[0].checked = 0;
				checklist12[0].checked = 0;
				checklist9[0].checked = 0;

		}
	}
	//全选
	function selectAllErrorQunar(){
		var checklist = document.getElementsByName("error_qunar_info");
		if(document.getElementById("controlAllErrorQunar").checked){
			for(var i=0; i<checklist.length; i++){
				checklist[i].checked = 1;
			}
		}else{
			for(var j=0; j<checklist.length; j++){
				checklist[j].checked = 0;
			}

		}
	}
	
	
function exportExcel() {
		var channel = $("#channel").val();
		var begin_info_time = $("#begin_info_time").val();
			$("form:first").attr("action","/acquire/exportFailexcel.do?order_status=10&channel="+channel+"&begin_info_time="+begin_info_time+"&end_info_time="+begin_info_time);
			$("form:first").submit();
			$("form:first").attr("action","/failtj/queryFailtjInfo.do");
		}
</script>
<style>
	#refresh_span a:link,#refresh_span a:visited{color:#2ea6d8;}
	#refresh_span a:hover{text-decoration:underline;}
	#hint{width:700px;border:1px solid #C1C1C1;background:#F0FAC1;position:fixed;z-index:9;padding:6px;line-height:17px;text-align:left;overflow:auto;} 
</style>
</head>
	<body onload="div();"><div></div>
		<div class="book_manage oz">
			<form action="/failtj/queryFailtjInfo.do" method="post">
				<div style="border: 0px solid #00CC00;">
					<ul class="order_num oz" style="margin-top: 10px;">
						<li>
							订单号：&nbsp;&nbsp;&nbsp;
							<input type="text" class="text" name="order_id"
								value="${order_id }" />
						</li>
						<li>
							订单级别：&nbsp;
							<select name="level" style="width:170px;">
								<option value="" <c:if test="${empty level}">selected</c:if>>
									全部
								</option>
							<!--  	<option value="VIP"
									<c:if test="${!empty level && level eq 'VIP'}">selected</c:if>>
									VIP
								</option>-->
								<option value="5"
									<c:if test="${!empty level && level eq '5'}">selected</c:if>>
									SVIP
								</option>
								<option value="1"
									<c:if test="${!empty level && level eq '1'}">selected</c:if>>
									VIP1
								</option>
								<option value="2"
									<c:if test="${!empty level && level eq '2'}">selected</c:if>>
									VIP2
								</option>
								<option value="0"
									<c:if test="${!empty level && level eq '0'}">selected</c:if>>
									普通
								</option>
								<option value="10"
									<c:if test="${!empty level && level eq '10'}">selected</c:if>>
									联程
								</option>
							</select>
						</li>
						<li></li>
						<%
						
						if ("2".equals(loginUserVo.getUser_level())
								|| "1".equals(loginUserVo.getUser_level())
								|| "1.2".equals(loginUserVo.getUser_level())) {
						%>
						<li>
							&nbsp;&nbsp;操作人：&nbsp;
							<input type="text" class="text" name="opt_ren" value="${opt_ren}" />
						</li>
						<%} %>
					</ul>
	
					

					
					<div id="divcon" style="display:none">
					<ul class="ser oz" >
						<li>
						失败原因：
								<input type="checkbox" onclick="selectAllError()" name="controlAllError" style="controlAllError" id="controlAllError"/>
								<label for="controlAllError">&nbsp;全部&nbsp;&nbsp;&nbsp;&nbsp;</label>
							<input type="checkbox" id="ERROR_INFO_1" name="ERROR_INFO_1" value="ERROR_INFO_1"
									<c:if test="${ERROR_INFO_1 ne '' }">checked="checked"</c:if> />
									<label for="ERROR_INFO_1">所购买的车次坐席已无票</label>
							<input type="checkbox" id="ERROR_INFO_2" name="ERROR_INFO_2" value="ERROR_INFO_2"
									<c:if test="${ERROR_INFO_2 ne '' }">checked="checked"</c:if> />
									<label for="ERROR_INFO_2">身份证件已经实名制购票</label>
							<input type="checkbox" id="ERROR_INFO_3" name="ERROR_INFO_3" value="ERROR_INFO_3"
									<c:if test="${ERROR_INFO_3 ne '' }">checked="checked"</c:if> />
									<label for="ERROR_INFO_3">票价和12306不符</label>
							<input type="checkbox" id="ERROR_INFO_4" name="ERROR_INFO_4" value="ERROR_INFO_4"
									<c:if test="${ERROR_INFO_4 ne '' }">checked="checked"</c:if> />
									<label for="ERROR_INFO_4">乘车时间异常</label>
							<input type="checkbox" id="ERROR_INFO_5" name="ERROR_INFO_5" value="ERROR_INFO_5"
									<c:if test="${ERROR_INFO_5 ne '' }">checked="checked"</c:if> />
									<label for="ERROR_INFO_5">证件错误</label>
							<c:if test="${channel eq 'elong'}">
							<input type="checkbox" id="ERROR_INFO_8" name="ERROR_INFO_8" value="ERROR_INFO_8"
									<c:if test="${ERROR_INFO_8 ne '' }">checked="checked"</c:if> />
									<label for="ERROR_INFO_8">用户要求取消订单</label>
							</c:if>
							<c:if test="${channel ne 'elong'}">
							<input type="checkbox" id="ERROR_INFO_6" name="ERROR_INFO_6" value="ERROR_INFO_6"
									<c:if test="${ERROR_INFO_6 ne '' }">checked="checked"</c:if> />
									<label for="ERROR_INFO_6">用户要求取消订单</label>
							</c:if>
							<input type="checkbox" id="ERROR_INFO_7" name="ERROR_INFO_7" value="ERROR_INFO_7"
									<c:if test="${ERROR_INFO_7 ne '' }">checked="checked"</c:if> />
									<label for="ERROR_INFO_7">未通过12306实名认证</label>
							<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<c:if test="${channel eq 'elong'}">
							<input type="checkbox" id="ERROR_INFO_6" name="ERROR_INFO_6" value="ERROR_INFO_6"
									<c:if test="${ERROR_INFO_6 ne '' }">checked="checked"</c:if> />
									<label for="ERROR_INFO_6">乘客身份信息待核验</label>
							</c:if>
							<c:if test="${channel ne 'elong'}">
							<input type="checkbox" id="ERROR_INFO_8" name="ERROR_INFO_8" value="ERROR_INFO_8"
									<c:if test="${ERROR_INFO_8 ne '' }">checked="checked"</c:if> />
									<label for="ERROR_INFO_8">乘客身份信息待核验</label>
							</c:if>
							<input type="checkbox" id="ERROR_INFO_11" name="ERROR_INFO_11" value="ERROR_INFO_11"
									<c:if test="${ERROR_INFO_11 ne '' }">checked="checked"</c:if> />
									<label for="ERROR_INFO_11">乘客超时未支付</label>
							<input type="checkbox" id="ERROR_INFO_12" name="ERROR_INFO_12" value="ERROR_INFO_12"
									<c:if test="${ERROR_INFO_12 ne '' }">checked="checked"</c:if> />
									<label for="ERROR_INFO_12">信息冒用</label>
							<input type="checkbox" id="ERROR_INFO_9" name="ERROR_INFO_9" value="ERROR_INFO_9"
									<c:if test="${ERROR_INFO_9 ne '' }">checked="checked"</c:if> />
									<label for="ERROR_INFO_9">系统异常</label>
						</li>
					</ul>
					</div>
					<div id="divqunarcon" style="display:none">
					<ul class="ser oz">
						<li>
						失败原因：
								<input type="checkbox" onclick="selectAllErrorQunar()" name="controlAllErrorQunar" style="controlAllErrorQunar" id="controlAllErrorQunar"/>
								<label for="controlAllErrorQunar">&nbsp;全部&nbsp;&nbsp;&nbsp;&nbsp;</label>
							<c:forEach items="${error_info_qunars }" var="s" varStatus="index">
									<input type="checkbox" id="error_qunar_info${index.count }"
										name="error_qunar_info" value="${s.key }"
										<c:if test="${fn:contains(errorInfoQunarStr, s.key ) }">checked="checked"</c:if> />
									<label for="error_qunar_info${index.count }">
										${s.value }
									</label>
							</c:forEach>
						</li>
					</ul>
					</div> 
					<br/>
        <p><input type="button" value="查 询" class="btn" onclick="submitForm();"/>
        
		 <% if ("2".equals(loginUserVo.getUser_level()) ) {%>
		<input type="button" value="导出Excel" class="btn" onclick="exportExcel()" />
		<%} %>
		<input type="button" value="返 回" class="btn" onclick="javascript:window.location.href='/failtj/queryFailtjPage.do';"/>
		</p>
		<div style="float: right;margin-right: 30px;margin-top: -30px;">
		<span style="font-size:14px;">订单总数：<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${totalCount2 }</span>个</span>
       
		<span style="font-size:14px;">符合条件订单总数：
		<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${totalCount }</span>个</span>
		<span style="font-size:14px;">占比:
		<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${zhanbi}%</span></span>
		<span style="font-size:14px;">总占比:
		<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${zhanbi_all}%</span></span>
		
		</div>
        
        
				</div>
				<div id="hint" class="pub_con" style="display:none"></div>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #EAEAEA;">
							<th>
								NO
								<input type="hidden" id="channel" name="channel" value="${channel }" />
								<input type="hidden" id="begin_info_time" name="begin_info_time" value="${begin_info_time }" />
							</th>
							<th width="120">
								订单号
							</th>
							<th>
								出发/到达
							</th>
							<th>
								车次
							</th>
							<th width="65">
								乘车时间
							</th>
							<th>
								坐席
							</th>
							<th>
								票价
							</th>
							<th>
								进价
							</th>
							<th width="65">
								创建时间
							</th>
							<th width="65">
								预订时间
							</th>
							<!--  <th>出票方式</th>-->
							<th>
								12306单号
							</th>
							<th>
								渠道
							</th>
							<th>
								级别
							</th>
							<th>
								失败原因
							</th>
							<th>
								操作员
							</th>
							<th>
								操作人
							</th>
							<th>
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${acquireList}" varStatus="idx">
							<tr 
								<c:if test="${fn:contains('00,01,11,33,55,66,88,81', list.order_status )}">
									style="background: #BEE0FC;"
								</c:if>
								<c:if test="${fn:contains('61,82,44', list.order_status )}">
									style="background: #E0F3ED;"
								</c:if>
								<c:if test="${fn:contains('83,77,85', list.order_status )}">
									style="background: #FFF8DC;"
								</c:if>
							>
							<td>
								${idx.index+1}
							</td>
							<td>
								${list.order_id}
							</td>
							<td>
								${list.from_city}/${list.to_city}
							</td>
							<td>
								${list.train_no }
							</td>
							<td>
								${fn:substringBefore(list.from_time, ' ')}
								<br />
								${fn:substringAfter(list.from_time, ' ')}
							</td>
							<!--  -->
							<td>
								${seat_Types[list.seat_type] }
							</td>
							<c:choose>
								<c:when
									test="${list.buy_money != null && list.buy_money!= list.pay_money}">
									<c:choose>
										<c:when
											test="${list.buy_money != null && list.buy_money > list.pay_money}">
											<td style="color: #f00;">
												<strong>${list.pay_money}</strong>
											</td>
											<td style="color: #f00;">
												<strong>${list.buy_money}</strong>
											</td>
										</c:when>
										<c:otherwise>
											<td style="color: #00f;">
												${list.pay_money}
											</td>
											<td style="color: #00f;">
												${list.buy_money}
											</td>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<td>
										${list.pay_money}
									</td>
									<td>
										${list.buy_money}
									</td>
								</c:otherwise>
							</c:choose>
							<td>
								${fn:substringBefore(list.create_time, ' ')}
								<br />
								${fn:substringAfter(list.create_time, ' ')}
							</td>
							<td>
								${fn:substringBefore(list.out_ticket_time, ' ')}
								<br />
								${fn:substringAfter(list.out_ticket_time, ' ')}
							</td>
							<!-- <td>
			                	<c:if test="${list.out_ticket_type eq '11'}">电子票</c:if>
			                	<c:if test="${list.out_ticket_type eq '22'}">配送票</c:if>
			                </td>-->
							<td>
								${list.out_ticket_billno}
							</td>
							<td>
								${channelTypes[list.channel] }
							</td>
							<c:choose>
								<c:when test="${list.level eq '0'}">
									<td>
										普通
									</td>
								</c:when>
								<c:when test="${empty list.level}">
									<td>
									</td>
								</c:when>
								<c:otherwise>
									<td style="color: red;">
							<!-- 			<c:if test="${list.level !=10 && list.level != 5}">
											<strong>VIP${list.level }</strong>
										</c:if> -->
										<c:if test="${list.level ==10}">
											<strong>联程</strong>
										</c:if>
										<c:if test="${list.level ==5}">
											<strong>SVIP</strong>
										</c:if>
										<c:if test="${list.level ==1}">
											<strong>VIP1</strong>
										</c:if>
										<c:if test="${list.level ==2}">
											<strong>VIP2</strong>
										</c:if>
									</td>
								</c:otherwise>
							</c:choose>
							<td>
								<c:choose>
									<c:when test="${list.channel eq 'qunar'  }">
										${error_info_qunars[list.error_info] }
									</c:when>
									<c:when test="${list.channel eq 'elong'  }">
										${error_info_elongs[list.error_info] }
									</c:when>
									<c:otherwise>
										${error_infos[list.error_info] }
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								${list.worker_name}
							</td>
							<td>
								${list.opt_ren}
							</td>
							<td>
								<span>  
									<c:choose> 
											<c:when test="${fn:contains('82', list.order_status)}"> 
												<a href="javascript:gotoOrderPay('${list.order_id}');">人工处理</a> 
											</c:when> 
											<c:when test="${fn:contains('44', list.order_status)}"> 
												<a href="javascript:gotoOrderPay('${list.order_id}');">人工处理</a> 
												<a href="javascript:gotoRobot('${list.order_id}','${list.create_time }');">机器处理</a> 
											</c:when> 
											<c:when test="${list.order_status eq '66'}">机器人处理中，不能进行干预 
											</c:when> 
											<c:when test="${list.order_status eq '61'}"> 
												<a href="javascript:gotoOrderPayLock('${list.order_id}')">支付</a> 
											</c:when> 
											<c:when test="${list.order_status eq '11'}"> 
												<a href="javascript:submitUpdateStatusInfo('${list.order_id}')">预定重发</a> 
											</c:when>
											<c:when test="${fn:contains('77', list.order_status)}"> 
												<a href="javascript:gotoOrderPay('${list.order_id}');">人工处理</a> 
											</c:when> 
											<c:when test="${fn:contains('10,99', list.order_status)}"> 
											</c:when> 
									</c:choose>  
										</span><div><span><a href="javaScript:void(0)" onclick="queryInfo('${list.order_id}');"  onmouseover="showdiv('${list.order_id}')" onmouseout="hidediv()">明细</a> 
									</span></div>
								</td>
							</tr>
						</c:forEach>
					</table>
					<jsp:include page="/pages/common/paging.jsp" />
					<div id="historyDiv" style="display:none;">
						<table class="pub_table" style="width: 680px; margin: 10px 10px;" id="historyTable">
							<tr>
								<th style="width: 30px;">序号</th>
								<th style="width: 450px;">操作日志</th>
								<th style="width: 130px;">操作时间</th>
								<th style="width: 70px;">操作人</th>
							</tr>
						</table>
					</div>
				</c:if>
			</form>
		</div>
	</body>
</html>
