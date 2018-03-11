<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>自提订单明细页</title>
<link rel="stylesheet" href="/css/style.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">

	function sumbitRefund(){
		if($.trim($("#refund_money").val())==""){
			$("#refund_money").focus();
			alert("退款金额不能为空！");
			return;
		}
		/*
		if($.trim($("#refund_12306_seq").val())==""){
			$("#refund_12306_seq").focus();
			alert("退款流水号不能为空！");
			return;
		}*/
		if(isNaN($.trim($("#refund_money").val()))){
			$("#refund_money").focus();
			alert("请输入数字！");
			return;
		}
		if(confirm("是否已经核对12306，并且退款？")){
			$("#refundForm").submit();
		}
	}
	function refuseRefund(){
		if($.trim($("#refund_memo").val())==""){
			$("#refund_memo").focus();
			alert("退款备注不能为空！");
			return;
		}
		if(confirm("确认拒绝退款？并且在备注中说明拒绝原因。")){
			$("#refundForm").attr("action","/booking/updateRefuseRefund.do");
		
			$("#refundForm").submit();
		}
	}
	function differRefund(){
		if($.trim($("#refund_money_id").val())==""){
			$("#refund_money_id").focus();
			alert("退款金额不能为空！");
			return;
		}
		if(confirm("确认差额退款？并且核对退款金额。")){
			$("#differForm").attr("action","/booking/updateDifferRefunding.do");
			
			$("#differForm").submit();
		}
	}
	function switch_ignore(refund_deadline_ignore){
		if(confirm("确认切换无视退款时间？")){
			window.location='/booking/updateSwitch_ignore.do?order_id=${orderInfo.order_id}&refund_deadline_ignore='+refund_deadline_ignore;
		}
	}
	function onblurOrderId(cp_id,order_id){
	//	alert("11111111");
		var url = "/booking/refund_register.do?version="+new Date()+"&cp_id="+cp_id+"&order_id="+order_id;
		$.get(url,function(data){
		//	alert("2222");
		//	alert(data);
			if( data == "no"){
				$("#username_info_"+cp_id)[0].style.display="";
				
				//	$("#username_info_"+cp_id)[0].text("已为该乘客提交过电话退款或用户退款，不可重复提交！").css("color","#f00");
				//	$("#refund_success").attr("disabled", true);
			}
		});
	}
	function refund_success(){
		var count=$(".checkcp:checked").length;
		if(count==0){
			alert("请勾选需要退款的乘客！");
			return;
		}
		if(confirm("确认退款完成？")){
			$("#refundcpIdForm").attr("action","/booking/refund_success.do");
		
			$("#refundcpIdForm").submit();
		}
	}
	
	function cancel_book(){
		if(confirm("确认取消预约？")){
		var orderId = $.trim( $("#order_id").val() );
		var url = "/booking/cancel_book.do?orderId="+orderId+"&version="+new Date();
		$.get(url,function(data){
				if(data == "success"){
					$("#refundcpIdForm").submit();
					alert("发送取消预约成功！");
				}else{
					alert("发送取消预约失败！");
				}
			});
		}
	}
	
//	function choose(){ 
//	 if(document.getElementById("changyong").checked) {  
//	 document.getElementById("cyrj").style.display = "";
//	  }else { 
//	   document.getElementById("cyrj").style.display = "none";
//	    }
//	}

</script>

</head>

<body>
	<div class="content1 oz">
    	<!--左边内容 start-->
    	<div class="left_con oz">
         	<div class="pub_order_mes oz mb10_all">
            	<h4>订单信息</h4>
                <div class="pub_con">
                	<table class="pub_table">
                    	<tr>
                        	<td class="pub_yuliu" rowspan="6"></td>
                        	<td width="234">订 单 号：<span>${orderInfo.order_id}</span></td>
                            <td>订单状态：<span>${bookStatus[orderInfo.order_status] }</span>
                            </td>
                        </tr>
                        <tr>
                        	<td width="234">代理商姓名：<span>${orderInfo.dealer_name}</span></td>
                        	<td>代理商账号：<span>${orderInfo.user_phone}</span></td>
                        </tr>
                          <tr>
                        	<td>支付金额：<span style="font-weight: bold;color: red;font-size: 20px;">${orderInfo.pay_money}</span> 元</td>
                        	<td>支付时间：<span>${orderInfo.pay_time}</span></td>
                        </tr>
                          <tr>
                        	<td>出票方式：<span><c:if test="${orderInfo.out_ticket_type eq '11'}">电子票</c:if>
	                			<c:if test="${orderInfo.out_ticket_type eq '22'}">配送票</c:if>
	                			</span>
	                		</td>
                        	<td>
                        		EOP单号：<span>${orderInfo.eop_order_id }</span>
                        	</td>
                        </tr>
                        <c:if test="${fn:contains('55,66,77', list.refund_status)}">
                        <tr>
                        	<td>出票时间：<span>${orderInfo.out_ticket_time}</span></td>
                        	<td>退款时间：<span>${orderInfo.refund_time}</span></td>
                        </tr>
                        </c:if>
                        <tr>
							<td>12306单号：<span>${orderInfo.out_ticket_billno }</span></td>     
                        </tr>
                    </table>
                </div>
            </div>
            <div class="pub_order_mes  oz mb10_all">
            	<h4>车票信息</h4>
                <div class="pub_con">
                	<table class="pub_table">
                    	<tr>
                        	<td class="pub_yuliu" rowspan="4">
                            	<strong>${orderInfo.train_no}</strong><br />
                            	车次
                            </td>
                            <td width="234" colspan="2">出 发/到 达：<span>${orderInfo.from_city}（${orderInfo.from_time}）</span>—<span>${orderInfo.to_city}（${orderInfo.to_time}）</span></td>
                        </tr>
                        <tr>

                        	<td width="234" colspan="2">日 期：<span>${orderInfo.travel_time}</span></td>
                        </tr>
                        <tr>
                        	<td width="234">坐 席：${seattype[orderInfo.seat_type] }</td>
                        	<td>数 量：<span>${fn:length(cpList)}</span> 张</td>
                        </tr>
                          <tr>
                        	<td colspan="2">票 价：<span style="font-weight: bold;color: red;">${orderInfo.ticket_pay_money}</span> 元&nbsp;&nbsp;
                        	保 险：<span style="font-weight: bold;color: red;">${orderInfo.bx_pay_money}</span> 元&nbsp;&nbsp;
                        	服 务：<span style="font-weight: bold;color: red;">${orderInfo.server_pay_money }</span> 元
                        	</td>
                        </tr>  
                        <tr>
                        	<td></td>
                       		<td>总 计：<span style="font-weight: bold;color: red;font-size: 20px;">${orderInfo.pay_money}</span> 元</td>
                        </tr>
                    </table>
                </div>
            </div>
            <div class="pub_passager_mes oz mb10_all">
            <form action="/booking/queryBookList.do" id="refundcpIdForm">
            	<h4>乘客信息</h4>
                <div class="pub_con">
                <c:forEach var="cp" items="${cpList}" varStatus="idx">
            		<c:if test="${idx.index != 0}">
                      <%
                      	out.println("<div class=\"oz pas1\"><p class=\"border_top\"></p>");
                      %>
            		</c:if>
                	<table class="pub_table">
                    	<tr>
                        	<td class="pub_yuliu" rowspan="6">
                        	  <c:if test="${orderInfo.order_status eq '44' }">
                            	<input type="checkbox" class="checkcp" id="checkcp_${cp.cp_id }" name="checkcp" value="${cp.cp_id }"  onclick="onblurOrderId('${cp.cp_id }','${orderInfo.order_id}')"/>
                            	</c:if>
                            </td>
                        	<td width="234">姓 名：<span>${cp.user_name}</span></td>
                        
                        	<input type="hidden" name="order_id" id="order_id" value="${orderInfo.order_id}" />
                        	<input type="hidden" name="eop_order_id" id="eop_order_id" value="${orderInfo.eop_order_id }" />
                            <td>类 型：<span>${tickettype[cp.ticket_type] }</span></td>
                        </tr>
                        <tr>
                        	<td width="234">证件类型：<span>${idstype[cp.ids_type] }</span>
                        	</td>
                        	<td>证件号码：<span>${cp.user_ids}</span></td>
                        </tr>
                        <tr>
                        	<td>保    险：<span>${cp.name_type }</span></td>
                        	<td>保险单号：<span>${cp.bx_code }</span></td>
                        </tr>
                        <tr>
                        	<td>车票号：<span>${cp.cp_id}</span></td>
                        	<td></td>
                        </tr>
                       	<tr>
                       		<td>
                       		座位：
                       		<span>${cp.train_box }</span>&nbsp;车&nbsp;<span>${cp.seat_no }</span>&nbsp;
                       		</td>
                        	<td>票面价：<span>${cp.pay_money }</span></td>
                        	
                       	</tr>  
                       	<c:if test="${orderInfo.order_status eq '44' }">
                       	<tr>
                       		<td>退订金额：<input type="text" class="refund_money" name="refund_money_${cp.cp_id }" id="refund_money_${cp.cp_id }"  />	</td>
                       		<td>退订流水号：<input type="text" class="refund_12306_seq" name="refund_12306_seq_${cp.cp_id }" id="refund_12306_seq_${cp.cp_id }"  />	</td>
                       		
                       	</tr>
                       	<tr>
                       		<td>
	                       	<span id="username_info_${cp.cp_id }" style="color:red;font-weight:bold;display:none;">已为该乘客提交过电话退款或用户退款，不可重复提交！</span>
	                       	</td>
                       	</tr>
                       	</c:if>
                    </table>
                    <c:if test="${idx.index != 0}">
                      <%
                      	out.println("</div>");
                      %>
            		</c:if>
            		</c:forEach>
            		</form>
            </div>
            
            <c:if test="${orderInfo.out_ticket_type eq '11' }">
        	<div class="pub_self_take_mes oz mb10_all">
            	<h4>自提资料</h4>
                <div class="pub_con">
                	<table class="pub_table">
                    	<tr>
                        	<td class="pub_yuliu"></td>
                        	<td>联 系 人：<span>${orderInfo.link_name}</span></td>
                        	<td>手 机：<span>${orderInfo.link_phone}</span></td>     	
                        </tr>
                        <tr>
                        	<td class="pub_yuliu"></td>
                        	<td>配送费：<span style="font-weight: bold;color: red;">${orderInfo.ps_pay_money}</span></td>
                        	<td>邮 箱：<span>${orderInfo.link_mail}</span></td> 	
                        </tr>
                    </table>
                </div>
        	</div>
        	</c:if>
        	<c:if test="${orderInfo.out_ticket_type eq '22' }">
        	<div class="pub_self_take_mes oz mb10_all">
            	<h4>配送资料</h4>
                <div class="pub_con">
                	<table class="pub_table">
                    	<tr>
                        	<td class="pub_yuliu"></td>
                        	<td>联 系 人：<span>${orderInfo.link_name}</span></td>
                        	<td>手 机：<span>${orderInfo.link_phone}</span></td>
                        	<td>邮 箱：<span>${orderInfo.link_mail}</span></td>
                        </tr>
                        <tr>
                        	<td class="pub_yuliu"></td>
                        	<td>配送费  ：<span style="font-weight: bold;color: red;">${orderInfo.pay_money}</span></td>
                        	<td>收件地址：<span>${orderInfo.link_address}</span></td>
                        	<td></td>
                        </tr>
                         <tr>
                        	<td class="pub_yuliu"></td>
                        	<td>配公司：<span>${orderInfo.ps_company }</span></td>
                        	<td></td>
                        	<td>配送单号：<span>${orderInfo.ps_billno }</span></td>
                        </tr>
                    </table>
                </div>
        	</div>
        	</c:if>

        		<div class="pub_self_take_mes oz mb10_all">
            	<h4>退款资料</h4>
                <div class="pub_con">
                
                 
        		  <c:forEach var="outTicket" items="${outTicket_info}" varStatus="idx">
            		<c:if test="${idx.index != 0}">
            		  <%
                      	out.println("<div class=\"oz pas1\"><p class=\"border_top\"></p>");
                      %>
            		</c:if>
                	<table class="pub_table">
                    	<tr>
                        	<td class="pub_yuliu"></td>
                        	<td>退款类型：<span style="font-weight: bold;color:red;">${refund_types[outTicket.refund_type]}</span></td>
                        	<td>退款状态：<span style="font-weight: bold;color:red;">${refund_statuses[outTicket.refund_status]}</span></td>
                        </tr>
                        <tr>
                        	<td class="pub_yuliu"></td>
                        	<td>退款金额：<span  style="font-weight: bold;color:red;">${outTicket.refund_money}</span></td>
                        	<td>创建时间：<span>${outTicket.create_time}</span></td>
                        	<td></td>
                        </tr>
                    </table>
                        <c:if test="${idx.index != 0}">
                      <%
                      	out.println("</div>");
                      %>
            		</c:if>
                  </c:forEach>
                  <c:if test="${orderInfo.order_status ne '77' && orderInfo.order_status ne '78'  }">
                  	<table class="pub_table">
                       <tr>
                       	<td class="pub_yuliu"></td>
                       	
                   		<td colspan="3">12306账号：<span style="color: red; font-weight: bold;">${account }</span></td>	
                   		
                       </tr>
                     </table>
                     </c:if>
                </div>
        	</div>

            <div class="pub_debook_mes  oz mb10_all">	
               <p>
             	  <c:if test="${orderInfo.order_status eq '44' }">
						<input type="button"  name="refund_success" class="btn" 
						id="refund_success" value="退款完成" onclick="refund_success();"/>					
			   		</c:if>
			   		<c:if test="${orderInfo.order_status eq '77' }">
						<input type="button"  name="cancel_book" class="btn" 
						id="cancel_book" value="取消预约" onclick="cancel_book();"/>					
			   		</c:if>
	               <input type="button" value="返 回" class="btn" onclick="javascript:history.back(-1);"/>
				   <c:if test="${orderInfo.order_status eq '44' }">
						<input type="button"  name="refund_deadline_ignore" class="btn" 
						id="refund_deadline_ignore" value="${ignoreStatus[orderInfo.refund_deadline_ignore] }" onclick="switch_ignore('${strSwitch}');"/>					
			   		</c:if>
			   </p>
        	</div>
        	<div class="pub_passager_mes oz mb10_all">
						<h4>
							历史操作
						</h4>
						<div class="pub_con">
							<table class="pub_table" style="width: 500px; margin: 20px auto;">
								<tr>
									<th>
										序号
									</th>
									<th>
										操作日志
									</th>
									<th>
										操作时间
									</th>
									<th>
										操作人
									</th>
								</tr>
								<c:forEach var="hs" items="${history}" varStatus="idx">
									<tr align="center">
										<td>
											${idx.index+1 }
										</td>
										<td align="left" style="word-break:break-all;width: 300px; ">
											${hs.order_optlog}
										</td>
										<td>
											${hs.create_time }
										</td>
										<td>
											${hs.opter }
										</td>
									</tr>
								</c:forEach>
							</table>
						</div>
					</div>
        		</div>
        <!--左边内容 end-->
    </div>
</body>
</html>
