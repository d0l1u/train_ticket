<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>确认支付页</title>
<link rel="stylesheet" href="/css/style.css" type="text/css" />
<link rel="stylesheet" href="/css/default.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/artDialog.js"></script>
<script type="text/javascript" src="/js/city.js"></script>
</head>

<body>
	<div class="content oz">
		<div class="index_all">
				
		<!--左边内容 start-->
			<jsp:include flush="true" page="/pages/common/menu.jsp">
					<jsp:param name="menuId" value="book" />
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
                    <c:if test="${orderInfo.seat_type eq '6'}">
                        <tr>
                        	<td colspan="2"><p class="price_tip" style="color:#EA0000;">卧铺铺位上中下是随机的，我们暂收下铺价格，出票后根据实际票价退还差价。</p></td>   	
                        </tr>
                    </c:if> 
                    </table>
                </div>
                
            </div>
            <div class="pub_passager_mes oz mb10_all">
            	<div class="pub_ord_tit">
                    <h4 class="fl">乘客信息</h4>
                    <p class="tit_tip">（一个订单最多可代购5张票，请务必填写乘车人真实信息）</p>
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
            
            <c:if test="${orderInfo.out_ticket_type eq '11' and orderInfo.ticket_ps_type ne '11'}">
	        	<div class="pub_self_take_mes oz mb10_all">
		        	<div class="pub_ord_tit">
		        	    <h4 class="fl">车站自提</h4>
	                    <p class="tit_tip">（取票规则：开车前可在任何火车站或代售点自由取票，<span onclick="javascript:showQpDesc();">取票说明</span>）</p>
	                </div>
	                <div class="new_pub_con">
	                	<table class="pub_table">
	                    	<tr>
	                        	<td class="pub_yuliu" rowspan="2"></td>
	                        	<td width="234">联 系 人：<span>${orderInfoPs.link_name}</span></td>
	                        	<td>手 机：<span>${orderInfoPs.link_phone}</span></td>
	                        	<!--<td>邮 箱：<span>${orderInfoPs.link_mail}</span></td>-->
	                        </tr>
	                        <!-- 
	                        <tr>
	                        	<td colspan="2">
	                        		<p class="price_tip">手机号是接收订票成功短信通知的，请务必填写真实有效的手机号。</p>
	                        	</td>
                        	</tr>
                        	 -->
	                    </table>
	                </div>
	        	</div>
        	</c:if>
        	<c:if test="${orderInfo.out_ticket_type eq '22'}">
	        	<div class="pub_self_take_mes oz mb10_all">
	            	<h4>送票上门</h4>
	                <div class="new_pub_con">
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
        	
        	 <c:if test="${orderInfo.out_ticket_type eq '11' and orderInfo.ticket_ps_type eq '11'}">
	        	<div class="pub_self_take_mes oz mb10_all">
	            	<h4>送票上门</h4>
	                <div class="new_pub_con">
						<table class="pub_table">
	                    	<tr>
	                        	<td class="pub_yuliu" rowspan="3"></td>
	                        	<td>联 系 人：<span>${orderInfoPssm.link_name_ps}</span></td>
	                        	<td>手 机：<span>${orderInfoPssm.link_phone_ps}</span></td>
	                        </tr>
	                        <tr>
	                        	<td colspan="5">
	                        	所在省市：
		                    	<select name="province" id="province" disabled="disabled"> 
								<option value="0">请选择省份</option> 
								</select> 
								<select name="city" id="city" disabled="disabled">  
								<option value="0">请选择城市</option> 
								</select> 
								<select name="district" id="district" disabled="disabled"> 
								<option value="0">请选择区县</option> 
								</select> 
								
								<!--下列为初始值(可选,编辑表单时设置)--> 
								<input type="hidden" value="${orderInfoPssm.province}" id="pre_province"/> 
								<input type="hidden" value="${orderInfoPssm.city}" id="pre_city"/> 
								<input type="hidden" value="${orderInfoPssm.district}" id="pre_district"/> 
								
	                        	</td>
	                        </tr>
	                        <tr>
	                        	<td colspan="3">收件地址：<span>${orderInfoPssm.ps_address}</span></td>
	                        </tr>
	                    </table>
	                </div>
	        	</div>
        	</c:if>
        	
        	
        	<div class="pub_self_take_mes oz mb10_all">
       			<div class="pub_ord_tit">
	        	    <h4 class="fl">支付信息</h4>
                    <p class="tit_tip">（火车票票源紧张，支付后不保证100%出票）</p>
                </div>
                <div class="new_pub_con">
                	<table class="pub_table">
                    	<tr>
                        	<td style="width:70px;"></td>
                        	<td>票 价：<span style="font-weight: bold;color: red;"><fmt:formatNumber value="${orderInfo.ticket_pay_money}" type="currency" pattern="#0.00元"/></span></td>
                        	<td>保 险：<span style="font-weight: bold;color: red;"><fmt:formatNumber value="${orderInfo.bx_pay_money}" type="currency" pattern="#0.00元"/></span></td>
                        	<c:if test="${engineeringCost eq '5'}">
                        		<td>SVIP服务费：<span style="font-weight: bold;color: red;"><fmt:formatNumber value="${engineeringCost}" type="currency" pattern="#0.00元"/></span></td>
                        	</c:if>
                        	<c:if test="${!empty orderInfoPssm.pay_money}">
                        	<td>配送费：<span style="font-weight: bold;color: red;"><fmt:formatNumber value="${orderInfoPssm.pay_money}" type="currency" pattern="#0.00元"/></span></td>
                        	</c:if>
                        	<td>总 计：<span style="font-weight: bold;font-size: 20px;color: red;"><fmt:formatNumber value="${totalPay4Show+orderInfoPssm.pay_money}" type="currency" pattern="#0.00元"/></span></td>
                        </tr>
                    </table>
                <p style="padding-left:80px;padding-bottom:20px;">
					<iframe src="${pay_url}" frameborder="0" scrolling="no"  width="550px;" height="140px" style="background: #F0F7FF;"></iframe>
	  				<br /><span style="margin-left:80px;">*&nbsp;&nbsp;请确认以上信息，支付后将无法修改</span>
				</p>
				</div>
        	</div>
        	<!-- 禁止收取手续费
        	<div class="ban4"></div>
        	 -->
        </div>
        <!--右边内容 end-->
    </div> 
    </div>
<script type="text/javascript">
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
	
	//<!--
	try{
	  document.domain="19e.cn";
	}catch(e){
	}
//-->
</script>
</body>
</html>
