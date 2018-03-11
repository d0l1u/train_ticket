<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>退款详情页面</title>
		<link rel="stylesheet" href="/css/style.css" type="text/css" />
		<style type="text/css">
			.pub_table td{height:25px;}
			.pub_yuliu {width:130px;}
		</style>
		<script type="text/javascript" src="/js/jquery.js"></script> 
	</head>

	<body>
		<form id="myform" name="myform" method="post">
			<div class="content1 oz">
				<!--左边内容 start-->
				<div class="left_con oz">
					<div class="pub_order_mes oz mb10_all">
						<div class="pub_con">
							<table class="pub_table">
								<tr>
									<td width="50"></td>
									<td width="234">
										统计人员：
										<span>${opterInfo.opt_person}</span>
									</td>
									<td>
										统计日期：
										<span>${opterInfo.tj_time}</span>
									</td>
								</tr> 
							</table>
						</div>
					</div> 
					<div class="pub_order_mes oz mb10_all">
						<div class="pub_con">
							<table class="pub_table">
								<tr>
									<td width="50"></td>
									<td width="234">
										订单出票数：
										<span>${opterInfo.out_ticket_total}</span>
									</td> 
								</tr> 
							</table>
						</div>
					</div> 
					<div class="pub_order_mes oz mb10_all">
						<div class="pub_con">
							<table class="pub_table">
								<tr>
									<td width="50"></td>
									<td width="234">
										退款数&nbsp;： ${opterInfo.refund_total}
									</td> 
								</tr> 
								<tr>
									<td width="50"></td>
									<td width="634">
										【19e退款：${opterInfo.refund_total_19e} &nbsp;&nbsp;
										去哪退款： ${opterInfo.refund_total_qunar}&nbsp;&nbsp;
										艺龙退款：${opterInfo.refund_total_elong} &nbsp;&nbsp;
										同程退款：${opterInfo.refund_total_tongcheng} &nbsp;&nbsp;
										美团退款： ${opterInfo.refund_total_meituan}&nbsp;&nbsp;
										内嵌退款： ${opterInfo.refund_total_inner}&nbsp;&nbsp;
										B2C退款： ${opterInfo.refund_total_app}&nbsp;&nbsp;
										商户退款： ${opterInfo.refund_total_ext}&nbsp;&nbsp;
										审核退款： ${opterInfo.refund_total_verify}&nbsp;&nbsp;
										高铁退款： ${opterInfo.refund_total_gtgj}&nbsp;&nbsp;
										途牛退款： ${opterInfo.refund_total_tuniu}&nbsp;&nbsp;
										携程退款：${opterInfo.refund_total_ctrip}】
									</td> 
								</tr> 
							</table>
						</div>
					</div>  
						<div class="pub_debook_mes  oz mb10_all">
							<p>
								<input type="button" value="返 回" class="btn"
									onclick="javascript:history.back(-1);" /> 
							</p>
						</div>
 
						<!--左边内容 end-->
					</div>
				</div>
			</div>
		</form>
	</body>
</html>
