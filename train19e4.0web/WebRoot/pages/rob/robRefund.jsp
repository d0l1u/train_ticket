<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>抢票订单退款操作页面</title>
<link rel="stylesheet" href="/css/style.css" type="text/css" />
<link rel="stylesheet" href="/css/default.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/artDialog.js"></script>
</head>
 <script type="text/javascript">
	$().ready(function(){
		$("#all_chk").click(function(){
			if($(this).attr("checked")){
				$(".chk_refund").attr("checked", true);
			}else{
				$(".chk_refund").attr("checked", false);
			}
		});
		
		$(".chk_refund").click(function(){
			if($(".chk_refund:checked").length==$(".chk_refund").length){
				$("#all_chk").attr("checked", true);
			}else{
				$("#all_chk").attr("checked", false);
			}
		});
	});

	function sumbitRefund(){
		var count=$(".chk_refund:checked").length;
		if(count==0){
			$(".chk_refund").css({border: "2px solid red" });
			alert("请勾选需要退款的车票！");
			return;
		}else{
			$(".chk_refund").css({border: "0px solid" });
		}
		var cp_id="",addr="",index=0,suffix="",user_remark="",isValid=true;
		var cp_id_str="";
		$(".chk_refund:checked").each(function(index){
			cp_id=$(this).val();
			cp_id_str+=cp_id;
			if(index<count-1){
				cp_id_str+=",";
			}
			user_remark = $("#user_remark_"+cp_id).val();
			if(user_remark == "000"){
				alert("请选择退款原因！");
				isValid=false;
				return false;
			}
		});
		if(isValid==false){
			return;
		}
		if(confirm("您是否确认对勾选的"+count+"张车票进行退票？")){
			//消息框	
			var dialog = art.dialog({
				lock: true,
				fixed: true,
				left: '50%',
				top: '80%',
			    title: 'Loading...',
			    icon: "/images/loading.gif",
			    content: '正在提交您的退款申请，请稍候！'
			});
			$(".aui_titleBar").hide();
			$("#refundForm").attr("action", "/rob/refund.jhtml?cp_id_str="+cp_id_str);
			$("#refundForm").submit();
		}
	}
	
	
	//显示取票说明
	function showQpDesc(){
		//消息框
		var dialog = art.dialog({
			lock: true,
			fixed: true,
			left: '50%',
			top: '50%',
		    title: '取票说明',
		    okVal: '确认',
		    content: '<p style="width:250px;height:40px;line-height:20px;">1、凭购票时的有效证件和电子订单号，发车前可在全国任意火车站或代售点取票。</p>'
		    		+'<p style="width:250px;height:40px;line-height:20px;margin-top:15px;">2、代售点收取代售费5元/张，另外车站售票窗口取异地票，火车站将收取代售费5元/张。</p>',
		    ok: function(){}
		});
	}
	
	//选择失败原因
	function changeFailReason(cp_id){
		var reason = $("#user_remark_"+cp_id).val();//选中的option值
		//alert(reason);
		if(reason == "距发车4小时以上且未换取纸质车票"){
			$("#reason_"+cp_id)[0].style.display = "block";
			//document.getElementById('reason_'+cp_id).style.display = "block";
		}else{
			$("#reason_"+cp_id)[0].style.display = "none";
		}
	}
</script> 

<body>
	<div class="content oz">
		<div class="index_all">
				
		<!--左边内容 start-->
			<jsp:include flush="true" page="/pages/common/menu.jsp">
					<jsp:param name="menuId" value="query" />
				</jsp:include>
		<!--左边内容 end-->
    	<!--右边内容 start-->
    	<div class="infoinput-right oz">
            <div class="pub_order_mes ticket_mes oz mb10_all" style="margin:0 0 10px 0;">
	            <div class="pub_ord_tit">
	            	<h4 class="fl">车次信息</h4>
	            	<p class="tit_tip">（数据仅作参考，请以实际出票情况为准）</p>
	            </div>
                <div class="new_pub_con">
                	<table class="pub_table">
                    	<tr>
                        	<td class="pub_yuliu" rowspan="4">
                            	<strong>${order.trainNo}</strong><br />
                            	<span class="checi">${trainTypeCn}</span>
                            </td>
                            <td class="addr" colspan="2"><strong>${order.fromCity}<span>（<fmt:formatDate value="${order.fromTime}" type="both"/>）</span></strong>—<strong>${order.toCity}<span>（<fmt:formatDate value="${order.toTime}" type="both"/>）</span></strong></td>
                        </tr>
                        <tr>
                        	<td width="234" colspan="2">日 期：<span>${order.travelTime}</span></td>
                        </tr>
                        <tr>
                        	<td width="234">抢票坐席：<span>${seatTypeMap[order.seatType]}</span></td>
                        	<td>数 量：<span>${fn:length(detailList)}</span> 张</td>
                        </tr>
                        <tr>
                        	<td width="250">备选无座：
		                        	<c:forEach var="seat" items="${Seats}">
											<SPAN>${seat.key}:${seat.value}</SPAN>&nbsp;
									</c:forEach>
							</td>
                        </tr>
                    </table>
                </div>
            </div>
            
            <div class="pub_order_mes oz mb10_all">
            	 <div class="pub_ord_tit">
	            	<h4>订单信息</h4>
	            </div>
                <div class="new_pub_con">
                	<table class="pub_table">
                    	<tr>
                        	<td class="pub_yuliu" rowspan="3"></td>
                        	<td width="234">订 单 号：<span>${order.orderId}</span></td>
                        	<td>支付时间：<span><fmt:formatDate value="${order.payTime}"
												type="both" pattern="yyyy-MM-dd HH:mm"/></span></td>
                        </tr>
                        <tr>
                        	<td width="234">取票单号：<span style="font-weight: bold;color: red;font-size: 20px;">${order.outTicketBillno}</span>
                        	</td>
                        	<td>出票时间：<span><fmt:formatDate value="${order.outTicketTime}"
												type="both" pattern="yyyy-MM-dd HH:mm"/></span>
                        	</td>
                        </tr>
                        <tr>
                        	<td width="234">最高票价：<fmt:formatNumber value="${max12306Price}" type="currency" pattern="#0.00"/></span> 元</td>
                        	<td>支付金额：<span style="font-weight: bold;font-size: 20px;"><fmt:formatNumber value="${order.payMoney}" type="currency" pattern="#0.00"/></span> 元</td>
                        </tr>
                    </table>
                    <p style="padding-left:600px;padding-bottom:10px;">订单状态：<span style="font-weight: bold;color: red;font-size: 20px;">${orderStatusMap[order.orderStatus]}</span></p>
                 </div>
            </div>
            
            <form action="/refund/refund.jhtml" method="post" name="refundForm" id="refundForm"
		    	enctype="multipart/form-data">
		    	<input type="hidden" name="order_id" value="${order.orderId }"/>
		    	<input type="hidden" id="isNeedTip" value="${isNeedTip}" />
		    	<input type="hidden" name="token" value="${token}" />
            <div class="pub_passager_mes oz mb10_all">
            	<div class="pub_ord_tit">
                    <h4 class="fl">车票信息</h4>
                    <p class="tit_tip">（每张票收取5%-20%的退票费、最低2元、保险不退，具体请看<span style="color:#0081cc;cursor:pointer;" onclick="window.location='http://ehcp.19e.cn/notice/queryNoticeInfo_no.jhtml?noticeId=46'">退票说明</span>）<input type="checkbox" id="all_chk"/><label for="all_chk" style="color:black;">全选</label></p>
                </div>
                <div class="new_pub_con">
            	<c:forEach var="detailInfo" items="${detailList}" varStatus="idx">
            		<c:if test="${idx.index != 0}">
                      <%
                      	out.println("<div class=\"oz pas1\"><p class=\"border_top\"></p>");
                      %>
            		</c:if>
                	<table class="pub_table add_bg1">
                    	<tr>
                        	<td class="pub_yuliu" rowspan="2">
                        	</td>
                        	<c:if test="${empty detailInfo.refund_status }">
                        	
                        	<td width="300">
                        		<span class="chk_area"><input type="checkbox" class="chk_refund" id="chk_${detailInfo.cp_id}" value="${detailInfo.cp_id}" /></span>
                        		姓 名：<span>${detailInfo.user_name}</span></td>
                        	</c:if>
                        	
                        	<c:if test="${!empty detailInfo.refund_status}">
                        	
                        	<td width="300">
                        		<span class="chk_area"></span>
                        		姓 名：<span>${detailInfo.user_name}</span></td>
                        	</c:if>
                            <td>类 型：<span>${ticketTypeMap[detailInfo.ticket_type]}</span>
                            </td>
                        </tr>
                        <tr>
                        	<td>${idsTypeMap[detailInfo.ids_type]}：<span>${detailInfo.user_ids}</span></td>
                        	<td>最终票价：<span>${detailInfo.orderTicketSeat}/${detailInfo.seat_no}/${detailInfo.orderTicketPrice}元</span></td>
                        </tr>
                       <c:if test="${fn:contains('11,22,44', order.orderStatus ) and empty detailInfo.refund_status}">
			        		<input type="hidden" name="cp_id_${detailInfo.cp_id}" value="${detailInfo.cp_id}"/>
                        	<input type="hidden" name="refund_money_${detailInfo.cp_id}" value="${detailInfo.cp_refund_money}" />
                        	<input type="hidden" name="refund_percent_${detailInfo.cp_id}" value="${detailInfo.refund_percent}" />
	                        <tr>
	                    		<td class="pub_yuliu" rowspan="3"></td>
	                    		<td width="300"><font color="red">*&nbsp;</font>退款原因：
	                    			<select name="user_remark_${detailInfo.cp_id}" id="user_remark_${detailInfo.cp_id}" onchange="changeFailReason('${detailInfo.cp_id}');">
	                    				<option value="000">请选择</option>
	                    				<option value="距发车4小时以上且未换取纸质车票">距发车4小时以上且未换取纸质车票</option>
	                    				<option value="用户已在车站窗口办理退票手续">用户已在车站窗口办理退票手续</option>
	                    				<option value="已在车站改签退票">已在车站改签退票</option>
	                    				<option value="车次停运用户已在车站窗口办理退票">车次停运用户已在车站窗口办理退票</option>
	                    			</select>
	                    		</td>
	                    		<td><div id="reason_${detailInfo.cp_id}" style="display:none;">退票金额：<span style="font-weight: bold;color: red;font-size: 20px;">${detailInfo.cp_refund_money}</span>&nbsp;元</div></td>
	                    	</tr>
                        </c:if>
                        <c:if test="${!empty detailInfo.refund_status}">
	                        <c:choose>
	                        	<c:when test="${detailInfo.refund_status eq '55'}">
	                        		<tr>
			                        	<td class="pub_yuliu" rowspan="2"></td>
			                        	<td colspan="2">
			                        		退款状态：<span style="color:#46A3FF;">##</span>
			                        	</td>
			                        </tr>
			                        <tr>
			                        	<td colspan="2">
			                        		拒绝原因：<span>##</span>
			                        	</td>
			                        </tr>
			                        <input type="hidden" name="cp_id_${detailInfo.cp_id}" value="${detailInfo.cp_id}" />
		                        	<input type="hidden" name="refund_money_${detailInfo.cp_id}" value="${detailInfo.cp_refund_money}" />
		                        	<input type="hidden" name="refund_status_${detailInfo.cp_id}" value="${detailInfo.refund_status}" />
		                        	<input type="hidden" name="refund_percent_${detailInfo.cp_id}" value="${detailInfo.refund_percent}" />
		                        	<tr>
			                    		<td class="pub_yuliu" rowspan="3"></td>
			                    		<td width="300"><font color="red">*&nbsp;</font>退款原因：
				                    		<select name="user_remark_${detailInfo.cp_id}" id="user_remark_${detailInfo.cp_id}" onchange="changeFailReason('${detailInfo.cp_id}');">
			                    				<option value="000">请选择</option>
			                    				<option value="距发车4小时以上且未换取纸质车票">距发车4小时以上且未换取纸质车票</option>
			                    				<option value="用户已在车站窗口办理退票手续">用户已在车站窗口办理退票手续</option>
			                    				<option value="已在车站改签退票">已在车站改签退票</option>
			                    				<option value="车次停运用户已在车站窗口办理退票">车次停运用户已在车站窗口办理退票</option>
			                    			</select>
			                    		</td>
			                    		<td><div id="reason_${detailInfo.cp_id}" style="display:none;">退票金额：<span style="font-weight: bold;color: red;font-size: 20px;">${detailInfo.cp_refund_money}</span>&nbsp;元</div></td>
			                    	</tr>
	                        	</c:when>
	                        	<c:otherwise>
				                	<tr>
			                        	<td class="pub_yuliu"></td>
			                        	<td colspan="2">
			                        		退款状态：<span style="color:#46A3FF;">${detailInfo.refund_status}</span>
			                        	</td>
			                        </tr>
	                        	</c:otherwise>
	                        </c:choose>
                         </c:if>
                    </table>
                    <c:if test="${idx.index != 0}">
                      <%
                      	out.println("</div>");
                      %>
            		</c:if>
            		</c:forEach>
           		 	<div class="tip_term">
                   		<p  style="margin-top: 10px;width: 700px;margin:10px auto;line-height:20px; color:#ea0000;">
                   			温馨提示：<br />1.退款原因请您正确选择，如退款原因与订单实际情况不一致，导致损失自行承担。<br />
                   			2.如果已过发车时间且未在车站窗口办理退票手续，系统将无法为您办理退票手续。<br/>
                   			3.本页面显示的退票金额仅作参考，请以【我的订单】页面中“正在退款”和“退款完成”状态下的实际退款金额为准。
                   		</p>
                   	</div>
                </div>
            </div>
            </form>
            
            
            <c:if test="${(rsList != null and fn:length(rsList)>0) or (orderInfo.order_status eq '33' and orderInfo.ticket_pay_money - orderInfo.buy_money>0)}">
            <div class="pub_order_mes oz mb10_all">
            	<div class="pub_ord_tit">
	            	<h4>退款状态</h4>
	            </div>
                <div class="new_pub_con">
                	<table class="pub_table"  width="665px;">
                		<c:forEach items="${rsList}" var="rs">
	                		<tr>
	                			<td width="152"></td>
	                        	<td width="234">退款金额：<span style="font-weight: bold;color: red;font-size: 20px;"><fmt:formatNumber value="${rs.refund_money}" type="currency" pattern="#0.00"/></span> 元</td>
	                        	<td>状 态：<span style="color:#46A3FF;">${rsStatusMap[rs.refund_status]}</span></td>
	                		</tr>
	                        <c:if test="${!empty rs.refund_status && fn:contains('44,55', rs.refund_status) && !empty rs.our_remark}">
		                        <tr>
	                        		<td width="152"></td>
		                        	<td colspan="2">备 注：<span>${rs.our_remark}</span></td>
		                        </tr>
	                        </c:if>
	                		<tr>
	                        	<td width="152"></td>
	                        	<td style="border-bottom:1px dashed #dadada;">退款时间：${rs.refund_time}</td>
	                        	<td style="border-bottom:1px dashed #dadada;">退款类型：${refundTypeMap[rs.refund_type]}</td>
	                        </tr>
	                    </c:forEach>   
                        <c:if test="${orderInfo.order_status eq '33' and orderInfo.ticket_pay_money - orderInfo.buy_money>0}">
	                        <tr>
                       			<td width="152"></td>
                        		<td width="234">退款金额：<span
                        			 style="font-weight: bold;color: red;font-size: 20px;"><fmt:formatNumber
                        			 value="${orderInfo.ticket_pay_money - orderInfo.buy_money}" type="currency" pattern="#0.00"/></span> 元
                        		</td>
                        		<td>
                        			状 态：<span style="color:#46A3FF;">退款申请中</span>
                        		</td>
	                        </tr>
	                        <tr>
	                        	<td width="152"></td>
	                        	<td style="border-bottom:1px dashed #dadada;">退款时间：<span></span></td>
	                        	<td style="border-bottom:1px dashed #dadada;">退款类型：差额退款</td>
	                        </tr>
                        </c:if>
                    </table>
                    <c:if test="${fn:contains('33,44', orderInfo.order_status) && orderInfo.ticket_pay_money - orderInfo.buy_money>0}">
	                    <div class="tip_term">
	                    	<p class="price_tip" style="margin-top: 10px;width: 500px;margin:10px 0 10px 142px;line-height:20px;">差额退款是由于卧铺上中下差价、成人儿童票差价等原因造成的。<br />差额退款会主动退还到代理商账户中，与代理商发起的退款是分开退还的。</p>
	                    </div>
                    </c:if>
                </div>
            </div>
            </c:if>
        	
          	<div class="vertified_btn oz">
            	 <p class="tijiao">
	            	<input type="button" value="退 票" class="detail_btn" onclick="sumbitRefund();"/>
            	 	<input type="button" value="返 回" class="detail_btn" onclick="javascript:history.back(-1);" />
            	 </p>
            </div>
            
        </div>
        <!--右边内容 end-->
    </div> 
     </div>
</body>
</html>
