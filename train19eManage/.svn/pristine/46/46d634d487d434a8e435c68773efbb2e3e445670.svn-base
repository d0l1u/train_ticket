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
		<title>出票管理页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
		<script language="javascript" src="/js/json2.js"></script>
		<script type="text/javascript">
	<%
		PageVo pageObject = (PageVo) request.getAttribute("pageBean");
		int pageIndex = pageObject.getPageIndex();
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user_level = loginUserVo.getUser_level();
	%>
	
	function queryInfo(order_id){
		var url="/oldOrder/queryOldOrderInfo.do?order_id="+order_id+"&query_type=mingxi";
		showlayer('明细',url,'1000px','800px')
		}

	function submitForm(){
		$("form:first").attr("action","/oldOrder/queryOldOrderList.do");
		$("form:first").submit();
		}

	function selectAll(){
		var checklist = document.getElementsByName("order_status");
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
	//全选操作
	function checkChnRetRuleAll(){
	      var order_id = document.getElementsByName("order_id");
	      var tag1 = document.getElementById("tag1").value;
	      if(tag1 == 0 ){
	         for(var i = 0 ; i < order_id.length ; i++){
	        	 order_id[i].checked = true ;
	         }
	         document.getElementById("tag1").value = 1;
	      }else if(tag1 == 1){
	         for(var i = 0 ; i < order_id.length ; i++){
	        	 order_id[i].checked = false ;
	         }
	         document.getElementById("tag1").value = 0;
	      }
	}
	
	function exportExcel() {
			$("form:first").attr("action","/oldOrder/exportexcel.do");
			$("form:first").submit();
			$("form:first").attr("action","/oldOrder/queryOldOrderList.do");
		}
		
</script>

	</head>

	<body><div></div>
		<div class="book_manage oz">
			<form action="/oldOrder/queryOldOrderList.do" method="post" name="queryFrm">
				<div style="border: 0px solid #00CC00; margin: 10px;">
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
						<li>
							&nbsp;&nbsp;乘车人：&nbsp;
							<input type="text" class="text" name="user_name" value="${user_name}" />
						</li>
						
						<%} %>
					</ul>
					
					<ul class="order_num oz" style="margin-top: 10px;">
						<li>
							开始时间：&nbsp;
							<input type="text" class="text" name="begin_info_time"
								readonly="readonly" value="${begin_info_time }"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" id="defaultStartDate1"/>
						</li>
						<li>
							结束时间：&nbsp;
							<input type="text" class="text" name="end_info_time"
								readonly="readonly" value="${end_info_time }"
								onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
						</li>
						<li></li>
						<li>
							12306单号：
							<input type="text" class="text" name="out_ticket_billno"
								value="${out_ticket_billno }" />
						</li>
					</ul>
					<%
						
						if ("2".equals(loginUserVo.getUser_level())
								|| "1".equals(loginUserVo.getUser_level()) || "1.2".equals(loginUserVo.getUser_level())
								|| "1.1".equals(loginUserVo.getUser_level())) {
					%>
					<dl class="oz" style="padding-top:20px;">
						<dt style="float:left;padding-left:40px;line-height:22px;">状态：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</dt>
						<dd  style="float:left;width:900px;">
							<div class="ser-item" style="float:left;white-space:nowrap;padding-right:20px;">
								<input type="checkbox" onclick="selectAll()" name="controlAll" style="controlAll" id="controlAll"/><label for="controlAll">&nbsp;全部&nbsp;&nbsp;&nbsp;&nbsp;</label>
							</div>
						<c:forEach items="${acquireStatus }" var="s" varStatus="index">
							<div class="ser-item" style="float:left;white-space:nowrap;padding-right:20px;">
								<input type="checkbox" id="order_status${index.count }" name="order_status" value="${s.key }" value="1"
									<c:if test="${fn:contains(statusStr, s.key ) }">checked="checked"</c:if> />
								<label for="order_status${index.count }">
									${s.value }
								</label>
							</div>
						</c:forEach>
						<c:forEach items="${acquirePayType }" var="s" varStatus="index">
						<div class="ser-item" style="float:left;white-space:nowrap;padding-right:20px;">
						<input type="checkbox" id="pay_type${index.count }" name="pay_type" value="${s.key }" value="1"
									<c:if test="${fn:contains(pay_typeStr, s.key )}">checked="checked"</c:if> />
						<label for="pay_type${index.count }">
									${s.value }
								</label>
							</div>
						</c:forEach>
						</dd>
					</dl>
					
					<%
						}
					%>
					<dl class="oz" style="padding-top:20px;">
						<dt style="float:left;padding-left:40px;line-height:22px;">渠道：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</dt>
						<dd  style="float:left;width:910px;">
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
		<br/>
        <p>
         <input type="button" value="查 询" class="btn" onclick="submitForm();"/>
		 <%		if ("2".equals(loginUserVo.getUser_level()) ) {%>
		<input type="button" value="导出Excel" class="btn" onclick="exportExcel()" />
		<%} %>
        			
        </p>
				</div>
				<div id="hint" class="pub_con" style="display:none"></div>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #EAEAEA;">
							<th style="width:30px;">全选 <br/><input type="checkbox" id="checkChnRetRulAll" name="checkChnRetRulAll" onclick="checkChnRetRuleAll()"/></th>
							<th>
								NO
							</th>
							<th width="110px">
								订单号
							</th>
							<th>
								出发/到达
							</th>
							<th>
								车次
							</th>
							<th width="70px">
								乘车时间
							</th>
							<th width="40px">
								坐席
							</th>
							<th>
								票价
							</th>
							<th>
								进价
							</th>
							<th width="50px">
								状态
							</th>
							<th width="70px">
								创建时间
							</th>
							<th width="70px">
								预订时间
							</th>
							<th width="70px">
								出票时间
							</th>
							<th>
								12306单号
							</th>
							<th>
								渠道
							</th>
							<th>
								级别
							</th>
							<th width="50px">
								操作员
							</th>
							<th>
								操作人
							</th>
							<th>
								返回日志
							</th>
							<th width="120px">
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
								<input type="checkbox" id="order_id" name="order_id" value="${list.order_id }"/>
								<input type="hidden" id="orderStatus_${list.order_id }" value="${list.order_status }" />
								<input type="hidden" id="createTime_${list.order_id }" value="${list.create_time }" />
							</td>
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
								${acquireStatus[list.order_status] }
							</td>
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
							<td>
								${fn:substringBefore(list.pay_time, ' ')}
								<br />
								${fn:substringAfter(list.pay_time, ' ')}
							</td>
							<td>
								${list.out_ticket_billno}
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
								${list.worker_name}
							</td>
							<td>
								${list.opt_ren}
							</td>
							<td>
								${returnlogMap[list.return_optlog] }
							</td>
							<td>
									<div><span>
										<a href="javaScript:void(0)" onclick="queryInfo('${list.order_id}');" onmouseover="showdiv('${list.order_id}')" onmouseout="hidediv()" >明细</a> 
									</span></div>
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
