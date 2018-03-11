<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>保险管理明细</title>
		<link rel="stylesheet" href="/css/style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script type="text/javascript">
		</script>
	</head>
	<body>
		<div class="content1 oz">
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
	                        	<td>用户账号：<span>${orderInfo.user_id }</span></td>
	                        	<td>
	                        		12306单号：<span>${orderInfo.out_ticket_billno }</span>
	                        	</td>
	                        </tr>
	                        <tr>
	                        	<td>支付金额：<span style="font-weight: bold;color: red;font-size: 20px;">${orderInfo.pay_money}</span> 元</td>
	                        	<td>支付时间：<span>${orderInfo.pay_time}</span></td>
	                        </tr>
	                         <tr>
	                        	<td>渠&nbsp;&nbsp;&nbsp;&nbsp;道：<span>${channelTypes[orderInfo.channel] }</span> </td>
	                        	<td></td>
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
	                            <td width="434" colspan="2">出 发/到 达：<span>${orderInfo.from_city}（${orderInfo.from_time}）</span>—<span>${orderInfo.to_city}（${orderInfo.to_time}）</span></td>
	                        </tr>
	                        <tr>
	                        	<td width="234" colspan="2">日 期：<span>${orderInfo.travel_time}</span></td>
	                        </tr>
	                        <tr>
	                        	<td width="234">坐 席：${seattype[orderInfo.seat_type] }</td>
	                        </tr>
                  	 	</table>
               		 </div>
         		 </div>
         		 <div class="pub_order_mes  oz mb10_all">
         		 	<h4>保险明细</h4>
               		 <div class="pub_con">
               		 	<table class="pub_table">
	                        <tr>
	                        	<td class="pub_yuliu" rowspan="6"></td>
	                        	<td width="234" colspan="2">用户姓名：${insuranceInfo.user_name }</td>
	                        	<td width="234" colspan="2">联系电话：${insuranceInfo.telephone }</td>
	                        </tr>
	                        <tr>
	                        	<td width="234" colspan="2">保险单号：${insuranceInfo.bx_code }</td>
	                        	<td width="234" colspan="2">保险有效期：${insuranceInfo.effect_date }</td>
	                        </tr>
	                        <tr>
	                        	<td width="234" colspan="2">保险状态：${bx_status[insuranceInfo.bx_status] }</td>
	                        	<!-- 
	                        	<td width="234" colspan="2"><a href="/insurance/updateInsuranceStstus.do?bx_id=${insuranceInfo.bx_id }&order_id=${insuranceInfo.order_id }">点击修改保险状态</a></td>
	                       		 -->
	                        </tr>
                  	 	</table>
               		 </div>
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
							<c:forEach var="hs" items="${log_List}" varStatus="idx">
								<tr align="center">
									<td>
										${idx.index+1 }
									</td>
									<td align="left">
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
		</div>		
	</body>
</html>