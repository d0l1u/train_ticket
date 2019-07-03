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
    	<form name="content" method="post" action="/order/orderCmpay.jhtml">
    	<input type="hidden" id="order_id" value="${orderInfo.order_id}"/>
    	<div class="left_con oz">
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
                        	<br />硬座余票不足时系统可能会预订上无座票，如果您不要该无座票可与我们联系。</p></td>   	
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
                        	<td>票 价：<span style="font-weight: bold;color: red;"><fmt:formatNumber value="${orderInfo.ticket_pay_money}" type="currency" pattern="#0.00元"/></span></td>
                        	<td>保 险：<span style="font-weight: bold;color: red;"><fmt:formatNumber value="${orderInfo.bx_pay_money}" type="currency" pattern="#0.00元"/></span></td>
                        	<td>总 计：<span id="totalPrice" style="font-weight: bold;font-size: 20px;color: red;"><fmt:formatNumber value="${totalPay4Show}" type="currency" pattern="#0.00元"/></span></td>
                        </tr>
                        <tr >
                           <td class="pub_yuliu" colspan="2"></td>
                           <td >
	                        	<input onclick="gotoPay()" type="button" class="btn1 bookTicket btn_link" 
	                        		value="前往支付" onmouseover="this.className='btn1 bookTicket btn_hover'" 
	                        		onmouseout="this.className='btn1 bookTicket btn_link'"/>
	                        </td>
                           <!-- 
                           <td colspan="1" style="height:35px;"><strong>请选择支付方式：</strong>
                        	<select id="bankAbbr" name="bankAbbr">
			                   <option value="ICBC">工商银行</option><option value="CMB">招商银行</option>
			                   <option value="CCB">建设银行</option><option value="ABC">农业银行</option>
			                   <option value="BOC">中国银行</option><option value="SPDB">上海浦东发展银行</option>
			                   <option value="BCOM">交通银行</option><option value="CMBC">民生银行</option>
			                   <option value="CEBB">光大银行</option><option value="GDB">广东发展银行</option>
			                   <option value="CITIC">中信银行</option><option value="HXB">华夏银行</option>
			                   <option value="FIB">兴业银行</option><option value="CPSRB">中国邮政储蓄</option>
			                   <option value="BOB">北京银行</option>  
			                </select>      
			                       
			                        <option value="SDB">深圳发展银行</option><option value="BBGB">广西北部湾银行</option>
			                  		<option value="BEA">东亚银行</option><option value="CBHB">渤海银行</option>
			                        <option value="CDRCB">成都农村商业银行</option><option value="CQRCB">重庆农村商业银行</option>
			                        <option value="DGB">东莞银行</option><option value="DLB">大连银行</option>
			                        <option value="DYCCB">东营市商业银行</option><option value="FDB">富银行</option>
			                        <option value="">广州银行</option><option value="">河北银行</option>
			                        <option value="">汉口银行</option><option value="">杭州银行</option>
			                        <option value="">浙江泰隆商业银行</option><option value="">徽商银行</option>
			                        <option value="">北京农商银行</option><option value="">北京银行</option>
			                        <option value="">南京银行</option><option value="">上海农村银行</option>
			                        <option value="">南洋商业银行</option><option value="">张家港农商银行</option>
			                        <option value="">浙商银行</option><option value="">珠海市农村信用合作社</option>
			                        <option value="">宜昌市商业银行</option>
			                        <option value="">厦门银行</option>
			                        <option value="">温州银行</option>
			                        <option value="">乌鲁木齐市商业银行</option>
			                        <option value="">威海市商业银行</option>
			                        <option value="">泰安市商业银行</option>
			                        <option value="">深圳农村商业银行</option>
			                        <option value="">苏州银行</option>
			                        <option value="">上饶银行</option>
			                        <option value="">平安银行</option>
			                        <option value="">盛京银行</option>
			                        <option value="">上海农村商业银行</option>
			                        <option value="">顺德农村商业银行</option>
			                        <option value="">渣打银行</option>
			                        <option value="">日照银行</option>
			                        <option value="">奇商银行</option>
			                        <option value="">齐鲁银行</option>
			                        <option value="">宁夏银行</option>
			                        <option value="">宁波银行</option>
			                        <option value="">江苏银行</option>
			                        <option value="">九江银行</option>
			                        <option value="">湖南农村信用社</option>
                        	</td>
                        	 -->
                        	
                        </tr>
                    </table>
				</div>
        	</div>
        	<!--业务所有标注 start-->
	        <div class="business-provider">
	        	<p>业务提供方：19旅行</p>
	        </div>
	        <!--业务所有标注 end-->
        </div>
        </form>
        <!--左边内容 end-->
        
    </div> 
<script type="text/javascript">
	//确认支付
	function gotoPay(){
		var count = $(".ticketInfo:visible").length;
		var bankAbbr = $("#bankAbbr").val();
		var order_id = $("#order_id").val();
		if(confirm("确认前往支付该订单吗？")){
			$("form:first").attr("action", "/order/orderCmpay.jhtml?order_id="+order_id+"&bankAbbr="+bankAbbr+"&productNum"+count);
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
