<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>抢票订单明细页</title>
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
                            	<strong>${order.trainNo}</strong><br />
                            	<span class="checi">${trainTypeCn}</span>
                            </td>
                            <td class="addr" width="500"><strong>${order.fromCity}<span>（${fromTime}）</span></trong>—<strong>${order.toCity}<span>（${toTime}）</span></strong></td>
                        
                        </tr>
                        <tr>

                        	<td width="234">日 期：<span>${order.travelTime}</span></td>
                        </tr>
                        <tr>
                        	<td width="234">
                        	坐 席：<span>${seatType}</span></td>
                        </tr>
                        <TR>
									<TD id="bkSelected" width=234>
										备选坐席：
										<c:forEach var="seat" items="${Seats}">
											<SPAN>${seat.key}:${seat.value}</SPAN>&nbsp;&nbsp;
										</c:forEach>
									</TD>
								</TR>
                          <tr>
                        	<td>价 格：<span>${max12306Price}</span> 元</td>   	
                        </tr> 
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
                        		赠送保险，5元/单
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
            
            
	        	<div class="pub_self_take_mes oz mb10_all">
		        	<div class="pub_ord_tit">
		        	    <h4 class="fl">车站自提</h4>
	                    <p class="tit_tip">（取票规则：开车前可在任何火车站或代售点自由取票，<span onclick="javascript:showQpDesc();">取票说明</span>）</p>
	                </div>
	                <div class="new_pub_con">
	                	<table class="pub_table">
	                    	<tr>
	                        	<td class="pub_yuliu" rowspan="2"></td>
	                        	<td width="234">联 系 人：<span>${order.contactPerson}</span></td>
	                        	<td>手 机：<span>${order.contactPhone}</span></td>
	                        </tr>
	                    </table>
	                </div>
	        	</div>
        	<div class="pub_self_take_mes oz mb10_all">
       			<div class="pub_ord_tit">
	        	    <h4 class="fl">支付信息</h4>
                    <p class="tit_tip">（火车票票源紧张，支付后不保证100%出票）</p>
                </div>
                <div class="new_pub_con">
                	<table class="pub_table">
                    	<tr>
                        	<td style="width:150px;"></td>
                        	<td>票 价：<span style="font-weight: bold;color: red;"><fmt:formatNumber value="${order.buyMoney}" type="currency" pattern="#0.00元"/></span></td>
                        	<td>SVIP服务费：<span style="font-weight: bold;color: red;"><fmt:formatNumber value="${svip_price}" type="currency" pattern="#0.00元"/></span></td>
                        	<td>总 计：<span style="font-weight: bold;font-size: 20px;color: red;"><fmt:formatNumber value="${totalPay}" type="currency" pattern="#0.00元"/></span></td>
                        </tr>
                    </table>
				</div>
				
				<div class="vertified_btn oz">
		            	 <p class="tijiao">
		            	 	<input type="button" value="返 回" class="detail_btn" onclick="javascript:history.back(-1);" />
		            	 </p>
		            </div>
        	</div>
        </div>
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
