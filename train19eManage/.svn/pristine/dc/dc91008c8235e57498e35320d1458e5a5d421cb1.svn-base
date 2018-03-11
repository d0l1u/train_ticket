<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<%@ page import="com.l9e.transaction.vo.PageVo"%>
<jsp:directive.page import="java.text.NumberFormat" />
<jsp:directive.page import="java.text.DecimalFormat" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>手动导入车站退票页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		 <script type="text/javascript" src="/js/jquery.cookie.js"></script>
		 	<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
		<script type="text/javascript">
		<%
			PageVo pageObject = (PageVo) request.getAttribute("pageBean");
			int pageIndex = pageObject.getPageIndex();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String user_level = loginUserVo.getUser_level();
		%>
		
		function mingxi(refund_seq,create_time){
			$("form:first").attr("action","/checkPrice/queryCheckPriceInfo.do?refund_seq="+refund_seq+"&order_status=11&begin_time="+create_time);
			$("form:first").submit();
		}
		
		function addCheckPrice(type){
		var cheLength = document.getElementsByName("query_type");
	    	for(var i=0; i<cheLength.length; i++) {
	    	  if(cheLength[i].checked){
		    	var ticket_type =cheLength[i].value;
		    	if(ticket_type == '11'|| ticket_type =='33'){
		    		if(confirm("请确认选对对账类型！确定继续否则取消！")){
					$("form:first").attr("action","/checkPrice/addCheckPricePage.do?add_type="+type+"&ticket_type="+ticket_type);
					$("form:first").submit();
					}
		      	 }else{
		          alert("退票不能上传异常单！");
		    	 }
	    	  }
	    	}
		}
		
		function queryBalance(){
			$("form:first").attr("action","/checkPrice/queryAlipayBalance.do");
			$("form:first").submit();
		}
		function queryUploadAlipay(){
			$("form:first").attr("action","/checkPrice/queryUploadAlipay.do");
			$("form:first").submit();
		}
		
		function exportExcel(){
			$("form:first").attr("action","/checkPrice/exportCheckPriceExcel.do");
			$("form:first").submit();
		}
		
		function exportAlipay(){
			$("form:first").attr("action","/checkPrice/exportAlipayExcel.do");
			$("form:first").submit();
		}
		
		function queryTicket(){
			$("form:first").attr("action", "/checkPrice/queryCheckPriceList.do");
			$("form:first").submit();
		}	
		
		function changeSeqById(order_id,ticket_type){
		  var seq = document.getElementById("seq_"+order_id).value;
		  var url ="/checkPrice/updateSeqById.do?order_id="+order_id+"&bank_pay_seq="+seq+"&ticket_type="+ticket_type+"&version="+new Date();
		 $.get(url,function(data){
			if(data == "success"){
				$("form:first").attr("action", "/checkPrice/queryCheckPriceList.do?pageIndex="+<%=pageIndex%>);
				$("form:first").submit();
			}
			});
		}
		
		function div(){
	   	 var cheLength = document.getElementsByName("query_type");
	    	for(var i=0; i<cheLength.length; i++) {
	       //账号停用展示停用原因
	       if(cheLength[i].checked && cheLength[i].value == '22'){
	          document.getElementById('check_cp_div').style.display = "none";
	          document.getElementById('alipay').style.display = "none";
	          document.getElementById('abnormal').style.display = "none";
	          document.getElementById('check_tk_div').style.display = "block";
	       }
	       else if(!cheLength[i].checked && cheLength[i].value == '22'){
	          document.getElementById('check_cp_div').style.display = "block";
	          document.getElementById('alipay').style.display = "block";
	          document.getElementById('abnormal').style.display = "block";
	          document.getElementById('check_tk_div').style.display = "none";
	    	   }
	    	}
		}	
		
		
		function updaterefund(){
		$("#refund").attr("disabled",true); 
		 var url ="/checkPrice/updaterefund.do?version="+new Date();
		 $.get(url,function(data){
			if(data == "success"){
				$("#refund").attr("disabled",false); 
			}
			});
		}
		
		//删除当前行
		function deleteTr(nowTr,check_id,ticket_type){ 
			var url ="/checkPrice/deleteTicket.do?check_id="+check_id+"&ticket_type="+ticket_type;
		 	$.get(url,function(data){
				if(data == "success"){
	        		$(nowTr).parent().parent().remove();
				}
			});
     	}
     	
     	//删除所有
		function deleteAll(){   
			$("input[name='check_price']:checkbox:checked").each(function(){ //遍历被选中CheckBox元素的集合 得到Value值
			var check_id = $(this).val();
			var tr = $(this).parent().parent();  
			var ticket_type = $("#check_"+check_id).val();
			var url ="/checkPrice/deleteTicket.do?check_id="+check_id+"&ticket_type="+ticket_type;
			 	$.get(url,function(data){
					if(data == "success"){ 
		        		tr.remove();
					}
				});
			}); 
     	}
     	
     	
		//全选selectAllChannel
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
	      var order_id = document.getElementsByName("check_price");
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
	
	
</script>
<style>
</style>
</head>
	<body onload="div();"><div></div>
		<div class="book_manage oz">
			<form action="/checkPrice/queryCheckPriceList.do" method="post" name="myform" id="myform">
				
				<ul class="order_num oz" style="margin-top: 10px;">
					<li>
						开始时间：&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="begin_info_time"
							readonly="readonly" value="${begin_info_time }"
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>
					<li>
						结束时间：&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="end_info_time"
							readonly="readonly" value="${end_info_time }"
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>
				</ul>
				<ul  class="order_num oz" style="margin-top: 10px;"> 
					<li>
						渠道：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="checkbox" onclick="selectAllChannel()" name="controlAllChannel" style="controlAllChannel" id="controlAllChannel"/>
						<label for="controlAllChannel">全部</label>
					</li>
						<li>
							<input type="checkbox" id="channel1" name="channel" value="19e" <c:if test="${fn:contains(channelList, '19e') }">checked="checked"</c:if> />
							<label>19e</label> 
						</li>
						<li>
							<input type="checkbox" id="channel2" name="channel" value="qunar" <c:if test="${fn:contains(channelList, 'qunar') }">checked="checked"</c:if> />
							<label>去哪</label> 
						</li>
						<li>
							<input type="checkbox" id="channel3" name="channel" value="elong" <c:if test="${fn:contains(channelList, 'elong') }">checked="checked"</c:if> />
							<label>艺龙</label> 
						</li>
						<li>
							<input type="checkbox" id="change_channel1" name="channel" value="tongcheng" <c:if test="${fn:contains(channelList, 'tongcheng') }">checked="checked"</c:if> />
							<label for="change_channel1">
								同程
							</label>
						</li> 
						<li>
							<input type="checkbox" id="change_channel2" name="channel" value="tuniu" <c:if test="${fn:contains(channelList, 'tuniu') }">checked="checked"</c:if> />
							<label for="change_channel2">
								途牛
							</label>
						</li> 
						<li>
							<input type="checkbox" id="change_channel3" name="channel" value="meituan" <c:if test="${fn:contains(channelList, 'meituan') }">checked="checked"</c:if> />
							<label for="change_channel3">
								美团
							</label>
						</li> 
						<li>
							<input type="checkbox" id="change_channel21" name="channel" value="301030" <c:if test="${fn:contains(channelList, '301030') }">checked="checked"</c:if> />
							<label for="change_channel21">
								高铁管家
							</label>
						</li>
					
						<li>
							<input type="checkbox" id="change_channel22" name="channel" value="301031" <c:if test="${fn:contains(channelList, '301031') }">checked="checked"</c:if> />
							<label for="change_channel22">
								携程
							</label>
						</li>  
					 
				</ul>
				<ul class="order_num oz" style="margin-top: 10px;margin-bottom: 10px;">
					<li>
						对账类型：&nbsp;&nbsp;&nbsp;
							<input type="radio" id="query_type_11"
								name="query_type" value="11" onclick="div()"
								<c:if test="${query_type eq '11' }">checked="checked"</c:if> />
							<label for="query_type_11">
								出票对账
							</label>
							<input type="radio" id="query_type_33"
								name="query_type" value="33" onclick="div()"
								<c:if test="${query_type eq '33' }">checked="checked"</c:if> />
							<label for="query_type_33">
								改签对账
							</label>
							<input type="radio" id="query_type_22"
								name="query_type" value="22" onclick="div()"
								<c:if test="${query_type eq '22' }">checked="checked"</c:if> />
							<label for="query_type_22">
								退票对账
							</label>
							<!-- 
							 -->
					</li>
				</ul>
				<ul class="order_num oz" style="margin-top: 10px;margin-bottom: 10px;display: none;" id="check_cp_div">
					<li>
						匹配状态：&nbsp;&nbsp;&nbsp;
							<input type="radio" id="check_status_C1"
								name="check_status" value="C1"
								<c:if test="${check_status eq 'C1' }">checked="checked"</c:if> />
							<label for="check_status_C1">
								完全匹配
							</label>
							<input type="radio" id="check_status_C2"
								name="check_status" value="C2"
								<c:if test="${check_status eq 'C2' }">checked="checked"</c:if> />
							<label for="check_status_C2">
								异常订单
							</label>
					</li>
				</ul>
				<ul class="order_num oz" style="margin-top: 10px;margin-bottom: 10px;display: none;" id="check_tk_div">
					<li>
						匹配状态：&nbsp;&nbsp;&nbsp;
							<input type="radio" id="check_status_T0"
								name="check_status" value=""
								<c:if test="${check_status eq '' }">checked="checked"</c:if> />
							<label for="check_status_T0">
								全部
							</label>
							<input type="radio" id="check_status_T1"
								name="check_status" value="T1"
								<c:if test="${check_status eq 'T1' }">checked="checked"</c:if> />
							<label for="check_status_T1">
								完全匹配
							</label>
							<input type="radio" id="check_status_T2"
								name="check_status" value="T2"
								<c:if test="${check_status eq 'T2' }">checked="checked"</c:if> />
							<label for="check_status_T2">
								亏损订单
							</label>
							<input type="radio" id="check_status_T3"
								name="check_status" value="T3"
								<c:if test="${check_status eq 'T3' }">checked="checked"</c:if> />
							<label for="check_status_T3">
								需补退单
							</label>
					</li>
				</ul>
				<ul class="order_num oz" style="margin-top: 10px;">
					<li>
						订单号：&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="order_id"
							value="${order_id}" />
					</li> 
				</ul>
				<p>
					<input type="button" value="查  询" class="btn" style="margin-top: 10px;" onclick="queryTicket();"/>
					<%
						if ("2".equals(loginUserVo.getUser_level()) ) {
           			%>
           			<input type="button" value="上传支付宝" class="btn" onclick="addCheckPrice('11');"/>  
           			<input type="button" value="导出Excel" class="btn" onclick="exportExcel();"/>  
					<input type="button" value="更新退票" class="btn" id="refund" onclick="updaterefund();"/>
           			<input type="button" value="导出支付宝" class="btn" id="alipay" onclick="exportAlipay();" style="margin-left: 433px;margin-top: -35px;"/>  
           			<input type="button" value="上传异常单" class="btn" id="abnormal" onclick="addCheckPrice('22');" style="margin-left: 542px;margin-top: -35px;margin-bottom: 20px;"/>  
           			<input type="button" value="查询每日余额" class="btn" onclick="queryBalance();" style="margin-left: 651px;margin-top: -55px;margin-bottom: 20px;display:block;"/>  
           			<input type="button" value="查询上传情况" class="btn" onclick="queryUploadAlipay();" style="margin-left: 760px;margin-top: -55px;margin-bottom: 20px;display:block;"/>  
					<%} %> 
				</p>
				<p>
           			<input type="button" value="删除" class="btn" onclick="deleteAll();"/></p>
				<div id="hint" class="pub_con" style="display:none">				
					<input type="hidden" id="tag1" name="tag1" value="0" />
				</div>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #EAEAEA;">
							<th style="width:30px;">全选 <br/><input type="checkbox" id="checkChnRetRulAll" name="checkChnRetRulAll" onclick="checkChnRetRuleAll()"/></th>
							
							<th style="10px;">
								NO
							</th>
							<th style="width: 200px;">
								流水号
							</th>
							<th>
								订单号
							</th>
							
							<c:choose>
							<c:when test="${query_type eq '11' || query_type eq '33'}">
								<th>
								出票总额
								</th>
								<th>
								支付宝金额
								</th>
								<th width="70px">
									出票时间
								</th>
								<th width="70px">
									支付时间
								</th>
								<th >
									匹配状态
								</th>
								<th>
									支付宝
								</th>
								<th>
									对账类型
								</th>
							</c:when>
							<c:when test="${query_type eq '22'}">
								<th>
									支付宝已退
								</th>
								<th>
									退款总额
								</th>
								<th width="70px">
									创建时间
								</th>
							</c:when>
							</c:choose>
							<th >
								渠道
							</th> 
							<th >
								操作
							</th>
						</tr>
						<c:forEach var="list" items="${checkPriceList}" varStatus="idx">
						<tr
							<c:if test="${list.ticket_type eq '33'}">style="background: #FFF8C2;"</c:if>
							>
							<td>
								<input type="checkbox" id="check_price" name="check_price" value="${list.check_id}"/> 
								<input type="hidden" id="check_${list.check_id }" value="${list.ticket_type }" />
							</td>
							<td>
								${idx.count }
							</td>
							<c:if test="${query_type eq '11' || query_type eq '33'}">
								<td style="width: 200px;">
								<c:if test="${check_status eq 'C1'}">
									${list.bank_pay_seq }
								</c:if>
								<c:if test="${check_status eq 'C2'}">
									<input type="text" class="text" name="seq_${list.order_id}" value="${list.bank_pay_seq }" style="size: 200px;"
									id="seq_${list.order_id}" onchange="changeSeqById('${list.order_id}','${list.ticket_type}')"/>
								</c:if>
								</td>
								<td>
									${list.order_id}
								</td>
								<td 
									<c:if test="${list.out_ticket_price gt list.pay_money}">style="color: blue;"</c:if>
									<c:if test="${list.out_ticket_price lt list.pay_money || list.pay_money eq null}">style="color: red;"</c:if>
								>
									${list.out_ticket_price }
								</td>
								<td 
									<c:if test="${list.out_ticket_price gt list.pay_money}">style="color: blue;"</c:if>
									<c:if test="${list.out_ticket_price lt list.pay_money || list.pay_money eq null}">style="color: red;"</c:if>
								>
									${list.pay_money }
								</td>
								<td>
									${list.out_ticket_time }
								</td>
								<td>
									${list.pay_time }
								</td>
								<td>
									<c:if test="${check_status eq 'C1'}">完全匹配</c:if>
									<c:if test="${check_status eq 'C2'}">异常订单</c:if>
								</td>
								<td>
									${list.alipay_id}
								</td>
								<td>
									<c:if test="${list.ticket_type eq '11'}">出票</c:if>
									<c:if test="${list.ticket_type eq '33'}">改签</c:if>
								</td>
							</c:if>
							<c:if test="${query_type eq '22'}">
								<td style="width: 200px;">
									${list.bank_pay_seq }
								</td>
								<td>
									${list.order_id}
								</td>
								<td 
									<c:if test="${list.refund_money gt list.refund_price}">style="color: blue;"</c:if>
									<c:if test="${list.refund_money lt list.refund_price}">style="color: red;"</c:if>
								>
									${list.refund_money }
								</td>
								<td 
									<c:if test="${list.refund_money gt list.refund_price}">style="color: blue;"</c:if>
									<c:if test="${list.refund_money lt list.refund_price}">style="color: red;"</c:if>
								>
									${list.refund_price }
								</td>
								<td>
								${list.create_time }
								</td>
							</c:if>
							<td>
								${list.channel }
							</td>
							<td>
								<!-- ${list.opt_person} -->
								<a href="#" onclick="deleteTr(this,'${list.check_id}','${list.ticket_type}')">删除</a> 
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
