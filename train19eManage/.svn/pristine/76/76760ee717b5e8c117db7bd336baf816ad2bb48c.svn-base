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
                        	<td>支付金额：<span style="font-weight: bold;color: red;font-size: 20px;">${orderInfo.pay_money}</span> 元</td>
                        	<td>成本金额：<span style="font-weight: bold;color: red;font-size: 20px;">${orderInfo.buy_money}</span> 元</td>
                        </tr>
                        <tr>
                        	<td>出票时间：<span>${orderInfo.out_ticket_time}</span></td>
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
                            <td width="500" colspan="2">出 发/到 达：<span>${orderInfo.from_city}（${orderInfo.from_time}）</span>—<span>${orderInfo.to_city}（${orderInfo.to_time}）</span></td>
                        </tr>
                        <tr>
                        	<td>数 量：<span>${fn:length(cpList)}</span> 张</td>
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
                       		<td width="234">座位类型：<span>${seattype[cp.seat_type] }</span></td>
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
            

            <div class="pub_debook_mes  oz mb10_all">	
               <p>
	               <input type="button" value="返 回" class="btn" onclick="javascript:history.back(-1);"/>
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
