<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单明细页</title>
<link rel="stylesheet" href="/css/style.css" type="text/css" />
<link rel="stylesheet" href="/css/default.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/artDialog.js"></script>
<script type="text/javascript">

	function sumbitRefund(cp_id){
		var addr = $("#stationTip_"+cp_id).val();
		if($("#isNeedTip").val()=="1" && addr==""){
			alert("请上传车站小票！");
			return;
		}
		if(addr!=""){
			var index = addr.lastIndexOf(".");
			if(index>0){
				var suffix = addr.substring(index + 1).toLowerCase();
				if(suffix!="jpg" && suffix!="bmp" && suffix!="png"){
					alert("车站小票只能为jpg、png或bmp格式的图片！");
					return;
				}
			}
		}
		if(confirm("您是否确认退票？")){
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
			$("#refundForm").attr("action", "/refund/singleRefund.jhtml");
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
	

</script>
</head>

<body>
	<div class="content oz">
		<!--导航条 start-->
	    <div class="main_nav">
        	<ul class="oz">
            	<jsp:include flush="true" page="/pages/common/menu.jsp">
					<jsp:param name="menuId" value="query" />
				</jsp:include>
            </ul>
            <div class="slogan"></div>
        </div>
        <!--导航条 end-->
    	<!--左边内容 start-->
    	<div class="left_con oz">
            <div class="pub_order_mes ticket_mes oz mb10_all">
	            <div class="oz">
	            	<h4 class="fl">车票信息</h4>
	            	<p class="tit_tip">（数据仅作参考，请以实际出票情况为准）</p>
	            </div>
                <div class="pub_con">
                	<table class="pub_table">
                    	<tr>
                        	<td class="pub_yuliu" rowspan="4">
                            	<strong>${orderInfo.train_no}</strong><br />
                            	<span class="checi">${trainTypeCn}</span>
                            </td>
                            <td class="addr" width="234" colspan="2"><strong>${orderInfo.from_city}<span>（${orderInfo.from_time}）</span></strong>—<strong>${orderInfo.to_city}<span>（${orderInfo.to_time}）</span></strong></td>
                        </tr>
                        <tr>

                        	<td width="234" colspan="2">日 期：<span>${orderInfo.travel_time}</span></td>
                        </tr>
                        <tr>
                        	<td width="234">坐 席：<span>${seatTypeMap[orderInfo.seat_type]}</span><c:if test="${!empty wz_ext && wz_ext eq '1'}"><span style="padding-left:10px;">[备选无座]</span></c:if></td>
                        	<td>数 量：<span>${fn:length(detailList)}</span> 张</td>
                        </tr>
                          <tr>
                        	<td>票 价：<span style="font-weight: bold;color: red;"><fmt:formatNumber value="${orderInfo.ticket_pay_money}" type="currency" pattern="#0.00" /></span> 元
                        	<span style="padding-left: 18px;">保 险：</span><span style="font-weight: bold;color: red;"><fmt:formatNumber value="${orderInfo.bx_pay_money}" type="currency" pattern="#0.00" /></span> 元</td>
                        	<td>
                        	
                        	配 送：<span style="font-weight: bold;color: red;"><fmt:formatNumber value="${orderInfo.ps_pay_money}" type="currency" pattern="#0.00" /></span> 元
                        	
                        	</td>
                        </tr>  
                        <tr>
                        	<td></td>
                       		<td>总 计：<span style="font-weight: bold;color: red;font-size: 20px;"><fmt:formatNumber value="${orderInfo.pay_money}" type="currency" pattern="#0.00" /></span> 元</td>
                        </tr>
                    </table>
                </div>
            </div>
            
            <form action="/refund/refunding.jhtml" method="post" name="refundForm" id="refundForm"
		    	enctype="multipart/form-data">
		    	<input type="hidden" name="order_id" value="${orderInfo.order_id }"/>
		    	<input type="hidden" id="isNeedTip" value="${isNeedTip}" />
            <div class="pub_passager_mes oz mb10_all">
            	<div class="oz">
                    <h4 class="fl">乘客信息</h4>
                    <p class="tit_tip">（一个订单最多可代购5张票）</p>
                </div>
                <div class="pub_con">
            	<c:forEach var="detailInfo" items="${detailList}" varStatus="idx">
            		<c:if test="${idx.index != 0}">
                      <%
                      	out.println("<div class=\"oz pas1\"><p class=\"border_top\"></p>");
                      %>
            		</c:if>
                	<table class="pub_table add_bg1">
                    	<tr>
                        	<td class="pub_yuliu" rowspan="4"></td>
                        	<td width="234">姓 名：<span>${detailInfo.user_name}</span></td>
                            <td>类 型：<span>${ticketTypeMap[detailInfo.ticket_type]}</span>
                            </td>
                        </tr>
                        <tr>
                        	<td width="234">证件类型：
                        		<span>${idsTypeMap[detailInfo.ids_type]}</span>
                        	</td>
                        	<td>证件号码：<span>${detailInfo.user_ids}</span></td>
                        </tr>
                        <tr>
                        	<td width="234">保 险：<span>
                        	<c:choose>
	                        	<c:when test="${!empty detailInfo.bx_name}">
	                        		${detailInfo.bx_name}
	                        	</c:when>
	                        	<c:otherwise>
	                        		不购买保险
	                        	</c:otherwise>
                        	</c:choose>
                        	</span></td>
                        	<td><c:if test="${!empty detailInfo.bx_name}">保险单号：<span>${detailInfo.bx_code}</span></c:if></td>
                        </tr>
                        <tr>
                        	<td width="234">
                        		车厢-${detailInfo.train_box}/座位-${detailInfo.seat_no}
                        	</td>
                        	<td><c:if test="${!empty detailInfo.refund_status}">退款状态：<span style="color:#46A3FF;">${rsStatusMap[detailInfo.refund_status]}</span></c:if></td>
                        </tr>
                    </table>
                    <c:if test="${idx.index != 0}">
                      <%
                      	out.println("</div>");
                      %>
            		</c:if>
            		</c:forEach>
                </div>
            </div>
            </form>
            
            <div class="pub_order_mes oz mb10_all">
            	<h4>订单信息</h4>
                <div class="pub_con">
                	<table class="pub_table">
                    	<tr>
                        	<td class="pub_yuliu" rowspan="3"></td>
                        	<td width="234">订 单 号：<span>${orderInfo.order_id}</span></td>
                        	<td>取票订单：<span style="font-weight: bold;color: red;font-size: 20px;">${orderInfo.out_ticket_billno}</span></td>
                        </tr>
                        <tr>
                        	<td width="234">支付金额：<span style="font-weight: bold;color: red;font-size: 20px;"><fmt:formatNumber value="${orderInfo.pay_money}" type="currency" pattern="#0.00"/></span> 元</td>
                        	<td>订单状态：<span style="font-weight: bold;color: red;font-size: 20px;">${orderStatusMap[orderInfo.order_status]}</span></td>
                        </tr>
                        <tr>
                        	<td width="234">支付时间：<span>${orderInfo.pay_time}</span></td>
                        	<td>出票时间：<span>${orderInfo.out_ticket_time}</span></td>
                        </tr>
                    </table>
                 </div>
            </div>
            <c:if test="${(rsList != null and fn:length(rsList)>0) or (orderInfo.order_status eq '33' and orderInfo.ticket_pay_money - orderInfo.buy_money>0)}">
            <div class="pub_order_mes oz mb10_all">
            	<h4>退款流水</h4>
                <div class="pub_con">
                	<table class="pub_table"  width="665px;">
                		<c:forEach items="${rsList}" var="rs">
	                		<tr>
	                			<td width="152"></td>
	                        	<td width="234">退款金额：<span style="font-weight: bold;color: red;font-size: 20px;"><fmt:formatNumber value="${rs.refund_money}" type="currency" pattern="#0.00"/></span> 元</td>
	                        	<td>状 态：<span style="color:#46A3FF;">${rsStatusMap[rs.refund_status]}</span></td>
	                		</tr>
	                		<!-- 退款状态为退款完成、拒绝退款 -->
	                        <c:if test="${!empty rs.refund_status && fn:contains('44,55', rs.refund_status) && !empty rs.our_remark}">
		                        <tr>
	                        		<td width="152"></td>
		                        	<td colspan="2">备 注：<span>${rs.our_remark}</span></td>
		                        </tr>
	                        </c:if>
	                        <!-- 退款状态为退款完成、拒绝退款 -->
	                        <c:if test="${!empty rs.refund_type && rs.refund_type eq '3' && !empty rs.user_remark}">
		                        <tr>
	                        		<td width="152"></td>
		                        	<td colspan="2">失败原因：<span>${outFailReasonMap[rs.user_remark]}</span></td>
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
            
            <c:if test="${orderInfo.out_ticket_type eq '11'}">
	        	<div class="pub_order_mes oz mb10_all">
		        	<div class="oz">
		        	    <h4 class="fl">车站自提</h4>
	                    <p class="tit_tip">（取票规则：开车前可在任何火车站或代售点自由取票，<span onclick="javascript:showQpDesc();">取票说明</span>）</p>
	                </div>
	                <div class="pub_con">
	                	<table class="pub_table">
	                    	<tr>
	                        	<td class="pub_yuliu"></td>
	                        	<td width="234">联 系 人：<span>${orderInfoPs.link_name}</span></td>
	                        	<td>手 机：<span>${orderInfoPs.link_phone}</span></td>
	                        	<!--  <td>邮 箱：<span>${orderInfoPs.link_mail}</span></td>-->
	                        </tr>
	                    </table>
	                </div>
	        	</div>
	        	
        	</c:if>
        	
        	<c:if test="${orderInfo.out_ticket_type eq '22'}">
	        	<div class="pub_self_take_mes oz mb10_all">
	            	<h4>送票上门</h4>
	                <div class="pub_con">
						<table class="pub_table">
	                    	<tr>
	                        	<td class="pub_yuliu" rowspan="3"></td>
	                        	<td>联 系 人：<span>${orderInfoPs.link_name}</span></td>
	                        	<td>手 机：<span>${orderInfoPs.link_phone}</span></td>
	                        	<td>邮 箱：<span>${orderInfoPs.link_mail}</span></td>
	                        </tr>
	                        <tr>
	                        	<td colspan="3">收件地址：<span>${orderInfoPs.link_address}</span></td>
	                        </tr>
	                        <tr>
	                            <td>配送物流：<span>${orderInfoPs.ps_company}</span></td>
	                        	<td>配送单号：<span>${orderInfoPs.ps_billno}</span></td>
	                            <td>状 态：<span>${psStatusMap[orderInfoPs.ps_status]}</span></td>
	                        </tr>
	                    </table>
	                </div>
	        	</div>
        	</c:if>
        	
        	<c:choose>
        		<c:when test="${fromFunc eq 'payNotify'}">
        			<div class="oz">
		            	 <p class="tijiao"><input type="button" value="订单查询" class="btn" onclick="javascript:window.location='/query/queryIndex.jhtml'" /></p>
		            </div>
        		</c:when>
        		<c:otherwise>
		          	<div class="vertified_btn oz">
		            	 <p class="tijiao"><input type="button" value="返 回" class="btn btn2" onclick="window.location='/query/queryOrderList.jhtml'" />
		            	 </p>
		            </div>
	            </c:otherwise>
            </c:choose>
            <!--业务所有标注 start-->
	        <div class="business-provider">
	        	
	        </div>
	        <!--业务所有标注 end-->
        </div>
        <!--左边内容 end-->
    </div> 
</body>
</html>
