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
		var statusList = $("#statusList").val();
		var str = /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){1,3})?$/;
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
			window.location='/gtBooking/updateGtSwitch_ignore.do?order_id=${orderInfo.order_id}&refund_deadline_ignore='+refund_deadline_ignore;
		}
	}
	

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
                         <td>商户订单号：<span>${orderInfo.merchant_order_id }&nbsp;</span></td>
                        	<td width="234">商户名称：<span>${orderInfo.dealer_name}</span></td>
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
                        </tr>
                        <c:if test="${fn:contains('55,66,77', list.refund_status)}">
                        <tr>
                        	<td>出票时间：<span>${orderInfo.out_ticket_time}</span></td>
                        	<td>退款时间：<span>${orderInfo.refund_time}</span></td>
                        </tr>
                        </c:if>
                        
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
                            <td width="500" colspan="2">出 发/到 达：<span>${orderInfo.from_city}（${orderInfo.from_time}）</span>—<span>${orderInfo.to_city}（${orderInfo.to_time}）</span></td>
                        </tr>
                        <tr>

                        	<td width="234" colspan="2">日 期：<span>${orderInfo.travel_time}</span></td>
                        </tr>
                        <tr>
                        	<td width="234">坐 席：${seattype[orderInfo.seat_type] }</td>
                        	<td>数 量：<span>${fn:length(cpList)}</span> 张</td>
                        </tr>
                        <tr>
                        	<td>票价 ：<span style="font-weight: bold;color: red;">${orderInfo.ticket_pay_money}</span> 元</td>
                        	<td>保 险：<span style="font-weight: bold;color: red;">${orderInfo.bx_pay_money}</span> 元</td>
                        	
                        </tr>  
                        <tr>
                        	<td></td>
                       		<td>总 计：<span style="font-weight: bold;color: red;font-size: 20px;">${orderInfo.pay_money}</span> 元</td>
                        </tr>
                    </table>
                </div>
            </div>
            <div class="pub_passager_mes oz mb10_all">
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
							</td>
							<td colspan="2"> 
								车票ID：<span>${cp.cp_id}</span>
							</td>
						</tr>
                    	<tr>
                        	<td width="234">姓 名：<span>${cp.user_name}</span></td>
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
                       		<td>
                       		座位：
                       		<span>${cp.train_box }</span>&nbsp;车&nbsp;<span>${cp.seat_no }</span>&nbsp;
                       		</td>
                        	<td>票面价：<span>${cp.pay_money }</span></td>
                       	</tr>
                    </table>
                    <c:if test="${idx.index != 0}">
                      <%
                      	out.println("</div>");
                      %>
            		</c:if>
            		</c:forEach>
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
                        <!-- 
                        <tr>
                        	<td class="pub_yuliu"></td>
                        	<td>配送费：<span style="font-weight: bold;color: red;">${orderInfo.ps_pay_money}</span></td>
                        	<td>邮 箱：<span>${orderInfo.link_mail}</span></td> 	
                        </tr>
                         -->
                    </table>
                </div>
        	</div>
        	</c:if>
        	<!-- 
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
			 -->
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
                  	<table class="pub_table">
                       <tr>
                       	<td class="pub_yuliu"></td>
                   		<td colspan="3">12306账号：<span style="color: red; font-weight: bold;">${account }</span></td>	
                       </tr>
                       <tr>
                       <td class="pub_yuliu"></td>
							<td>12306单号：<span>${orderInfo.out_ticket_billno }</span></td>     
                        </tr>
                     </table>
                </div>
        	</div>

            <div class="pub_debook_mes  oz mb10_all">	
               <p>
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
   </div>
</body>
</html>
