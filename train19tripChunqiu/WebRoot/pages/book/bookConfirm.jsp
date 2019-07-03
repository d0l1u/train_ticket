<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>确认订单页</title>
<link rel="stylesheet" href="/css/style.css" type="text/css" />
<link rel="stylesheet" href="/css/default.css" type="text/css" />
<link rel="stylesheet" href="/css/attach_style.css" type="text/css" />
<!-- 自适应高度 -->
<script type="text/javascript" src="/js/trendsHeight.js"></script>
<!-- 自适应高度 -->
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/artDialog.js"></script>
<script type="text/javascript" src="/js/idCard.js"></script>
</head>

<body>
	<div class="content oz">
		<!--导航条 start-->
	    <div class="main_nav">
        	<ul class="oz">
            	<jsp:include flush="true" page="/pages/common/menu.jsp">
					<jsp:param name="menuId" value="book" />
				</jsp:include>
            </ul>
            <div class="slogan"></div>
        </div>
        <!--导航条 end-->
    	<!--左边内容 start-->
    	<form name="content" method="post" action="/chunqiu/order/orderYjpay.jhtml">
    	<input type="hidden" name="order_id" id="order_id" value="${orderInfo.order_id}"/>
    	<input type="hidden" name="total_fee" id="total_fee" value="${orderInfo.ticket_pay_money+orderInfo.bx_pay_money}"/>
    	<input type="hidden" name="subject" id="subject" value="火车票"/>
    	<input type="hidden" id="alibody" name="alibody" value="从${orderInfo.from_city}到${orderInfo.to_city}"/>
    	<input type="hidden" name="from_station" value="${orderInfo.from_city}"/>
    	<input type="hidden" name="to_station" value="${orderInfo.to_city}"/>
    	<input type="hidden" name="start_time" value="${orderInfo.travel_time}"/>
    	<div class="left_con oz">
    		<div class="tip_term oz" style="margin:10px auto 0;">
            	<p class="price_tip">
            		<span>客服电话：400-688-2666&nbsp;&nbsp;转2号键</span>
            		<span style="padding-left:400px;">业务提供方：19旅行</span>
            	</p>
            </div>
            <div class="pub_order_mes ticket_mes oz mb10_all">
	            <div class="oz">
	            	<h4 class="fl">车票信息</h4>
	            	<p class="tit_tip">（数据仅作参考，请以实际出票情况为准）</p>
	            </div>
                <div class="pub_con">
                	<table class="pub_table">
                    	<tr>
                        	<td class="pub_yuliu" rowspan="5">
                            	<strong>${orderInfo.train_no}</strong><br />
                            	<span class="checi">${trainTypeCn}</span>
                            </td>
                            <td class="addr" width="500"><strong>${orderInfo.from_city}<span>（${orderInfo.from_time}）</span></trong>—<strong>${orderInfo.to_city}<span>（${orderInfo.to_time}）</span></strong></td>
                        
                        </tr>
                        <tr>

                        	<td width="234">日 期：<span>${orderInfo.travel_time}</span></td>
                        </tr>
                        <tr>
                        	<td width="234">
                        	坐 席：<span>${seatTypeMap[orderInfo.seat_type]}</span><c:if test="${!empty wz_ext && wz_ext eq '1'}"><span style="padding-left:10px;">[备选无座]</span></c:if></td>
                        	
                        </tr>
                          <tr>
                        	<td>价 格：<span>${detailList[0].cp_pay_money}</span> 元</td>   	
                        </tr>  
                        <tr>
                        	<td colspan="2"><p class="price_tip" style="color:#EA0000;">卧铺铺位上中下是随机的，我们暂收下铺价格，出票后根据实际票价退还差价。
                        	</p></td>   	
                        </tr>
                    </table>
                </div>
                
            </div>
            <div class="pub_passager_mes oz mb10_all">
            	<div class="oz">
                    <h4 class="fl">乘客信息</h4>
                    <p class="tit_tip">（一个订单最多可代购5张票，请务必填写乘车人真实信息）</p>
                </div>
                <div class="pub_con">
            	<c:forEach var="detailInfo" items="${detailList}" varStatus="idx">
            		<c:if test="${idx.index != 0}">
                      <%
                      	out.println("<div class=\"oz pas1\"><p class=\"border_top\"></p>");
                      %>
            		</c:if>
                	<table class="ticketInfo pub_table add_bg1">
                    	<tr>
                        	<td class="pub_yuliu" rowspan="3">
                            	
                            </td>
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
                        	<td colspan="2">保 险：<span>
                        	<c:choose>
	                        	<c:when test="${!empty detailInfo.bx_name}">
	                        		${detailInfo.bx_name}
	                        	</c:when>
	                        	<c:otherwise>
	                        		不购买保险
	                        	</c:otherwise>
                        	</c:choose>
                        	</span></td>
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
            
            <c:if test="${orderInfo.out_ticket_type eq '11'}">
	        	<div class="pub_self_take_mes oz mb10_all">
		        	<div class="oz">
		        	    <h4 class="fl">车站自提</h4>
	                    <p class="tit_tip">（取票规则：开车前可在任何火车站或代售点自由取票，<span onclick="javascript:showQpDesc();">取票说明</span>）</p>
	                </div>
	                <div class="pub_con">
	                	<table class="pub_table">
	                    	<tr>
	                        	<td class="pub_yuliu" rowspan="2"></td>
	                        	<td width="234">联 系 人：<span>${orderInfoPs.link_name}</span></td>
	                        	<td>手 机：<span>${orderInfoPs.link_phone}</span></td>
	                        	<!--<td>邮 箱：<span>${orderInfoPs.link_mail}</span></td>-->
	                        </tr>
	                        <tr>
	                        	<td colspan="2">
	                        		<p class="price_tip">手机号是接收订票成功短信通知的，请务必填写真实有效的手机号。</p>
	                        	</td>
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
	                        	  
	                        	<!--<td>邮 箱：<span>${orderInfoPs.link_mail}</span></td>-->
	                        </tr>
	                        <tr>
	                        	<td colspan="3">收件地址：<span>${orderInfoPs.link_address}</span></td>
	                        </tr>
	                    </table>
	                </div>
	        	</div>
        	</c:if>
        	
        	<div class="pub_self_take_mes oz mb10_all">
       			<div class="oz">
	        	    <h4 class="fl">支付信息</h4>
                    <p class="tit_tip">（火车票票源紧张，支付后不保证100%出票）</p>
                </div>
                <div class="pub_con" >
                	<table class="pub_table">
                    	<tr>
                        	<td class="pub_yuliu"></td>
                        	<td>票 价：<span style="font-weight: bold;color: #FF6600;"><fmt:formatNumber value="${orderInfo.ticket_pay_money}" type="currency" pattern="#0.00元"/></span></td>
                        	<td>保 险：<span style="font-weight: bold;color: #FF6600;"><fmt:formatNumber value="${orderInfo.bx_pay_money}" type="currency" pattern="#0.00元"/></span></td>
                        	<!-- <td>总 计：<span id="totalPrice" style="font-weight: bold;font-size: 20px;color: #FF6600;"><fmt:formatNumber value="${totalPay4Show}" type="currency" pattern="#0.00元"/></span></td> -->
                        	<td>总 计：<span id="totalPrice" style="font-weight: bold;font-size: 20px;color: #FF6600;"><fmt:formatNumber value="${orderInfo.ticket_pay_money+orderInfo.bx_pay_money}" type="currency" pattern="#0.00元"/></span></td>
                        </tr>
                        <tr >
                           <td class="pub_yuliu" colspan="2"></td>
                           <td >
	                        	<input onclick="gotoPay()" type="button" class="btn1 bookTicket btn_link" 
	                        		value="前往支付" onmouseover="this.className='btn1 bookTicket btn_hover'" 
	                        		onmouseout="this.className='btn1 bookTicket btn_link'"/>
	                        	
	                        </td>
	                        <td>
	                        	<input onclick="gotoAlipay(${orderInfo.ticket_pay_money+orderInfo.bx_pay_money})" type="button" style="background:url(../../images/alipay182.png) center; width:100px; height:35px;"/>
	                        </td>
	                        <td class="pub_yuliu" colspan="2"></td>
                        </tr>
                    </table>
				</div>
        	</div>
        	<!--业务所有标注 start-->
	        <div class="business-provider">
	        	
	        </div>
	        <!--业务所有标注 end-->
        </div>
        </form>
        <!--左边内容 end-->
        
    </div> 
<script type="text/javascript">

	//跳转到支付宝
	function gotoAlipay(pay_money) {
		$("form:first").attr("action", "/alipay/turnToAlipay.jhtml");
		$("form:first").submit();
	}
	//确认支付
	function gotoPay(){
		var count = $(".ticketInfo:visible").length;
		var bankAbbr = $("#bankAbbr").val();
		var order_id = $("#order_id").val();
		$("form:first").attr("action", "/chunqiu/order/orderCmpay.jhtml?order_id="+order_id+"&bankAbbr="+bankAbbr+"&productNum="+count);
		//消息框	
		var dialog = art.dialog({
			lock: true,
			fixed: true,
			left: '50%',
			top: '250px',
		    title: 'Loading...',
		    icon: "/images/loading.gif",
		    content: '正在前往支付页面，请稍候！'
		});
		$(".aui_titleBar").hide();
		$("form:first").submit();
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
	
	<!--
	try{
	  document.domain="19e.cn";
	}catch(e){
	}
//-->
</script>
</body>
</html>
