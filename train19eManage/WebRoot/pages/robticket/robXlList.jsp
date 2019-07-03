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
		<title>出票效率页面</title>
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
	
    function GetDateStr(AddDayCount){     
            var dd = new Date();     
            dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期     
            var y = dd.getFullYear();     
            var m = dd.getMonth()+1;//获取当前月份的日期  
            if(m<10){     
				m="0"+m;  
			}      
            var d = dd.getDate();
              if(d<10){     
				 d="0"+d;  
				}       
              return y+"-"+m+"-"+d;     
     }
    
	//得到当前日期，时间格式为"yyyy-MM-dd"
	function TodayFormat(){
		var date = new Date();
		var year = date.getFullYear();
		var month = date.getMonth()+1; //js从0开始取 
		month = month<10 ? "0"+month : month;
		var date1 = date.getDate(); 
		date1 = date1<10 ? "0"+date1 : date1;
		var now = year+"-"+month+"-"+date1;
		//alert(date+" | "+now );
		return now;
	}
	//得到第二天日期，时间格式为"yyyy-MM-dd"
	function TomorrowFormat(){
		var dd = new Date(); 
		dd.setDate(dd.getDate()+1);//获取1天后的日期 
		var y = dd.getYear(); 
		var m = dd.getMonth()+1;//获取当前月份的日期 
		m = m<10 ? "0"+m : m;
		var d = dd.getDate(); 
		d = d<10 ? "0"+d : d;
		var tomorrow = y+"-"+m+"-"+d;
		//alert(tomorrow);
		return tomorrow; 
	}
	//比较两个时间的大小,时间格式为"yyyy-MM-dd hh:mm:ss"
	function compareTime(beginTime,endTime){
		var beginTimes = beginTime.substring(0, 10).split('-');
		var endTimes = endTime.substring(0, 10).split('-');

	 	beginTime = beginTimes[1] + '-' + beginTimes[2] + '-' + beginTimes[0] + ' ' + beginTime.substring(10, 19);
		endTime = endTimes[1] + '-' + endTimes[2] + '-' + endTimes[0] + ' ' + endTime.substring(10, 19);

		var a = (Date.parse(endTime ) - Date.parse(beginTime)) / 3600 / 1000;
		if (a < 0) {
		    return 'false';//endTime 
		} else if (a > 0) {
			return 'true';//endTime大 
		} else if (a == 0) {
			return 'equal';//时间相等 
		} else {
			return 'exception';
		}
}	
	function queryInfo(order_id){
		var url="/acquire/queryAcquireInfo.do?order_id="+order_id+"&query_type=mingxi";
		showlayer('明细',url,'1000px','800px')
		}

	function submitForm(){
		$("form:first").attr("action","/acquire/queryAcquireXlList.do");
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
			$("form:first").attr("action","/acquire/exportexcelXl.do");
			$("form:first").submit();
			$("form:first").attr("action","/acquire/queryAcquireXlList.do");
		}
		
</script>
<style>
	#refresh_span a:link,#refresh_span a:visited{color:#2ea6d8;}
	#refresh_span a:hover{text-decoration:underline;}
	#hint{width:700px;border:1px solid #C1C1C1;background:#F0FAC1;position:fixed;z-index:9;padding:6px;line-height:17px;text-align:left;overflow:auto;} 
</style>

	</head>

	<body><div></div>
		<div class="book_manage oz">
			<form action="/acquire/queryAcquireXlList.do" method="post" name="queryFrm">
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
						<li>
						</li>
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
						<dl class="oz" style="padding-top:20px;">
						<dt style="float:left;padding-left:40px;line-height:22px;">预订效率：&nbsp;</dt>
						<dd  style="float:left;width:910px;">
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="radio" id="book_time" name="book_time" value=""
									<c:if test="${book_time eq '' }">checked="checked"</c:if> />全部
							</div>
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="radio" id="book_time" name="book_time" value="20"
									<c:if test="${book_time eq '20' }">checked="checked"</c:if> />20秒
							</div>
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="radio" id="book_time" name="book_time" value="30"
									<c:if test="${book_time eq '30' }">checked="checked"</c:if> />30秒
							</div>
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="radio" id="book_time" name="book_time" value="45"
									<c:if test="${book_time eq '45' }">checked="checked"</c:if> />45秒
							</div>
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="radio" id="book_time" name="book_time" value="60"
									<c:if test="${book_time eq '60' }">checked="checked"</c:if> />60秒
							</div>
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="radio" id="book_time" name="book_time" value="90"
									<c:if test="${book_time eq '90' }">checked="checked"</c:if> />90秒
							</div>
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="radio" id="book_time" name="book_time" value="120"
									<c:if test="${book_time eq '120' }">checked="checked"</c:if> />2分
							</div>
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="radio" id="book_time" name="book_time" value="180"
									<c:if test="${book_time eq '180' }">checked="checked"</c:if> />3分
							</div>
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="radio" id="book_time" name="book_time" value="300"
									<c:if test="${book_time eq '300' }">checked="checked"</c:if> />5分
							</div>
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="radio" id="book_time" name="book_time" value="600"
									<c:if test="${book_time eq '600' }">checked="checked"</c:if> />10分
							</div>
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="radio" id="book_time" name="book_time" value="none"
									<c:if test="${book_time eq 'none' }">checked="checked"</c:if> />10分以上
							</div>
						<!--<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="radio" id="book_time" name="book_time" value="2700"
									<c:if test="${book_time eq '2700' }">checked="checked"</c:if> />45分以上
							</div>  -->	 
						</dd>
					</dl>
					<dl class="oz" style="padding-top:20px;">
						<dt style="float:left;padding-left:40px;line-height:22px;">支付效率：&nbsp;</dt>
						<dd  style="float:left;width:910px;">
						<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="radio" id="pay_time" name="pay_time" value=""
									<c:if test="${pay_time eq '' }">checked="checked"</c:if> />全部
							</div>
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="radio" id="pay_time" name="pay_time" value="20"
									<c:if test="${pay_time eq '20' }">checked="checked"</c:if> />20秒
							</div>
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="radio" id="pay_time" name="pay_time" value="30"
									<c:if test="${pay_time eq '30' }">checked="checked"</c:if> />30秒
							</div>
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="radio" id="pay_time" name="pay_time" value="45"
									<c:if test="${pay_time eq '45' }">checked="checked"</c:if> />45秒
							</div>
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="radio" id="pay_time" name="pay_time" value="60"
									<c:if test="${pay_time eq '60' }">checked="checked"</c:if> />60秒
							</div>
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="radio" id="pay_time" name="pay_time" value="90"
									<c:if test="${pay_time eq '90' }">checked="checked"</c:if> />90秒
							</div>
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="radio" id="pay_time" name="pay_time" value="120"
									<c:if test="${pay_time eq '120' }">checked="checked"</c:if> />2分
							</div>
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="radio" id="pay_time" name="pay_time" value="180"
									<c:if test="${pay_time eq '180' }">checked="checked"</c:if> />3分
							</div>
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="radio" id="pay_time" name="pay_time" value="300"
									<c:if test="${pay_time eq '300' }">checked="checked"</c:if> />5分
							</div>
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="radio" id="pay_time" name="pay_time" value="600"
									<c:if test="${pay_time eq '600' }">checked="checked"</c:if> />10分
							</div>
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="radio" id="pay_time" name="pay_time" value="none"
									<c:if test="${pay_time eq 'none' }">checked="checked"</c:if> />10分以上
							</div>
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="radio" id="pay_time" name="pay_time" value="2700"
									<c:if test="${pay_time eq '2700' }">checked="checked"</c:if> />45分及以上
							</div>
						</dd>
					</dl>
		<br/>
        <p>
         <input type="button" value="查 询" class="btn" onclick="submitForm();"/>
		 <%		if ("2".equals(loginUserVo.getUser_level()) ) {%>
		<input type="button" value="导出Excel" class="btn" onclick="exportExcel()" />
		<%} %>
		<div style="float: right;margin-right: 30px;">
		<span style="font-size:14px;">订单总数：<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${totalCount2 }</span>个</span>
       
		<span style="font-size:14px;">符合条件订单总数：
		<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${totalCount }</span>个</span>
		<span style="font-size:14px;">占比:
		<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${zhanbi}%</span>
		</span>
		</div>
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
							<th width="120px">
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
								预订时长
							</th>
							<th>
								支付时长
							</th>
							<th width="70px">
								操作员
							</th>
							<th>
								操作人
							</th>
							<th >
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${acquireList}" varStatus="idx">
						<tr>
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
							<!-- <td>
			                	<c:if test="${list.out_ticket_type eq '11'}">电子票</c:if>
			                	<c:if test="${list.out_ticket_type eq '22'}">配送票</c:if>
			                </td>-->
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
								${list.book_time_xl}
							</td>
							<td>
								${list.pay_time_xl}
							</td>
							<td>
								${list.worker_name}
							</td>
							<td>
								${list.opt_ren}
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
					<input type="hidden" id="tag1" name="tag1" value="0" />
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
