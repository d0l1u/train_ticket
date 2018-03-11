<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>当天竞价页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	    <script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
	    <script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
	    <script type="text/javascript">
	    
	    </script>
	    <style>
	    	.book_manage{width:1140px;}
			.book_manage TABLE{width:1056px;float: left;}
			.book_manage td{line-height: 33px;}
			.book_manage th{line-height: 33px;background: #f0f0f0;}
		</style>
	</head>
	<body>
		<div class="book_manage oz"><div style="margin-left: 95%;margin-bottom: 5px;"><a href="javascript:backPage();" >返回</a></div>
			<form action="/compete/todayCompete.do" method="post" name="myform" id="myform">
				<div style="border: 0px solid #00CC00; margin: 10px;">
				<div>
					<table style="width:60px;">
						<tr>
							<th>
								19旅行
							</th>
						</tr>
						<tr>
							<td>
								创建时间
							</td>
						</tr>
						<tr>
							<td>
								CDG竞价
							</td>
						</tr>
						<tr>
							<td>
								CDG排名
							</td>
						</tr>
							<tr>
							<td>
								非CDG竞价
							</td>
						</tr>
						<tr>
							<td>
								非CDG排名
							</td>
						</tr>
						<tr>
							<td>
								票数
							</td>
						</tr>
						<tr style="background: #BEE0FC;">
							<td>
								创建时间
							</td>
						</tr>
						<tr style="background: #BEE0FC;">
							<td>
								CDG竞价
							</td>
						</tr>
						<tr style="background: #BEE0FC;">
							<td>
								CDG排名
							</td>
						</tr>
						<tr style="background: #BEE0FC;">
							<td>
								非CDG竞价
							</td>
						</tr>
						<tr style="background: #BEE0FC;">
							<td>
								非CDG排名
							</td>
						</tr>
						<tr style="background: #BEE0FC;">
							<td>
								票数
							</td>
						</tr>
						<tr>
							<td>
								创建时间
							</td>
						</tr>
						<tr>
							<td>
								CDG竞价
							</td>
						</tr>
						<tr>
							<td>
								CDG排名
							</td>
						</tr>
							<tr>
							<td>
								非CDG竞价
							</td>
						</tr>
						<tr>
							<td>
								非CDG排名
							</td>
						</tr>
						<tr>
							<td>
								票数
							</td>
						</tr>
					</table>
				</div>
				<div>
					<table style="width:30px;">
						<c:set var="len8" value="0"/>
						<tr>
							<th>
								8
							</th>
						</tr>
						<c:forEach var="list" items="${competeList8}" varStatus="idx" begin="0" end="2">
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
							<td>
								${list.compete_money_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
							<td>
								${list.compete_ranking_1}
							</td>
						</tr>	
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
							<td>
								${list.compete_money_un_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
							<td>
								${list.compete_ranking_un_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_1}
							</td>
						</tr>
						<c:set var="len8" value="${len8+1}"/>
						</c:forEach>
						<c:if test="${len8 eq 0 }">
							<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len8 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len8 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
							
					</table>
				</div>
				<div>
					<table style="width:30px;">
					<c:set var="len9" value="0"/>
						<tr>
							<th>
								9
							</th>
						</tr>
						<c:forEach var="list" items="${competeList9}" varStatus="idx" begin="0" end="2">
						<c:set var="len9" value="${len9+1}"/>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
							<td>
								${list.compete_money_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
							<td>
								${list.compete_ranking_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
							<td>
								${list.compete_money_un_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_1}
							</td>
						</tr>
						</c:forEach>		
						<c:if test="${len9 eq 0 }">
							<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len9 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len9 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
							
					</table>
				</div>
				<div>
					<table style="width:30px;">
					<c:set var="len10" value="0"/>
						<tr>
							<th>
								10
							</th>
						</tr>
						<c:forEach var="list" items="${competeList10}" varStatus="idx" begin="0" end="2">
					<c:set var="len10" value="${len10+1}"/>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_un_1}
							</td>
						</tr>
							<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_1}
							</td>
						</tr>
							<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_1}
							</td>
						</tr>
						</c:forEach>		
					<c:if test="${len10 eq 0 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len10 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len10 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
					</table>
				</div>
				<div>
					<table style="width:30px;"><c:set var="len11" value="0"/>
						<tr>
							<th>
								11
							</th>
						</tr>
						<c:forEach var="list" items="${competeList11}" varStatus="idx" begin="0" end="2">
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						<c:set var="len11" value="${len11+1}"/>
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_un_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_1}
							</td>
						</tr>
						</c:forEach>		
					
						<c:if test="${len11 eq 0 }">
							<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len11 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len11 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
					</table>
				</div>
				<div>
					<table style="width:30px;">
					<c:set var="len12" value="0"/>
						<tr>
							<th>
								12
							</th>
						</tr>
						<c:forEach var="list" items="${competeList12}" varStatus="idx" begin="0" end="2">
						<c:set var="len12" value="${len12+1}"/>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_un_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_1}
							</td>
						</tr>
						</c:forEach>		
					<c:if test="${len12 eq 0 }">
							<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len12 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len12 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
					
					</table>
				</div>
				<div>
					<table style="width:30px;"><c:set var="len13" value="0"/>
						<tr>
							<th>
								13
							</th>
						</tr>
						<c:forEach var="list" items="${competeList13}" varStatus="idx" begin="0" end="2">
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
						<c:set var="len13" value="${len13+1}"/>
						
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_un_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_1}
							</td>
						</tr>
						</c:forEach>		
					<c:if test="${len13 eq 0 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len13 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len13 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
					</table>
				</div>
				<div>
					<table style="width:30px;"><c:set var="len14" value="0"/>
					<tr>
							<th>
								14
							</th>
						</tr>
						<c:forEach var="list" items="${competeList14}" varStatus="idx" begin="0" end="2">
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
						<c:set var="len14" value="${len14+1}"/>
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_un_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_1}
							</td>
						</tr>
						</c:forEach>		
					<c:if test="${len14 eq 0 }">
								<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len14 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len14 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
					</table>
				</div>
				<div>
					<table style="width:30px;"><c:set var="len15" value="0"/>
					<tr>
							<th>
								15
							</th>
						</tr>
						<c:forEach var="list" items="${competeList15}" varStatus="idx" begin="0" end="2">
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						<c:set var="len15" value="${len15+1}"/>
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_un_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_1}
							</td>
						</tr>
						</c:forEach>		
						<c:if test="${len15 eq 0 }">
								<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len15 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len15 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
					
					</table>
				</div>
				<div>
					<table style="width:30px;"><c:set var="len16" value="0"/>
					<tr>
							<th>
								16
							</th>
						</tr>
						<c:forEach var="list" items="${competeList16}" varStatus="idx" begin="0" end="2">
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						<c:set var="len16" value="${len16+1}"/>
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_un_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_1}
							</td>
						</tr>
						</c:forEach>		
					<c:if test="${len16 eq 0 }">
							<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len16 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len16 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
					</table>
				</div>
				<div>
					<table style="width:30px;"><c:set var="len17" value="0"/>
					<tr>
							<th>
								17
							</th>
						</tr>
						<c:forEach var="list" items="${competeList17}" varStatus="idx" begin="0" end="2">
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						<c:set var="len17" value="${len17+1}"/>
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_un_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_1}
							</td>
						</tr>
						</c:forEach>		
					<c:if test="${len17 eq 0 }">
							<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len17 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len17 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
					</table>
				</div>
				<div>
					<table style="width:30px;"><c:set var="len18" value="0"/>
					<tr>
							<th>
								18
							</th>
						</tr>
						<c:forEach var="list" items="${competeList18}" varStatus="idx" begin="0" end="2">
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						<c:set var="len18" value="${len18+1}"/>
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_un_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_1}
							</td>
						</tr>
						</c:forEach>		
					<c:if test="${len18 eq 0 }">
							<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len18 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len18 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						</table>
				</div>
				<div>
					<table style="width:30px;"><c:set var="len19" value="0"/>
					<tr>
							<th>
								19
							</th>
						</tr>
						<c:forEach var="list" items="${competeList19}" varStatus="idx" begin="0" end="2">
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						<c:set var="len19" value="${len19+1}"/>
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_un_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_1}
							</td>
						</tr>
						</c:forEach>		
						<c:if test="${len19 eq 0 }">
							<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len19 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len19 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						</table>
				</div>
				<div>
					<table style="width:30px;"><c:set var="len20" value="0"/>
					<tr>
							<th>
								20
							</th>
						</tr>
						<c:forEach var="list" items="${competeList20}" varStatus="idx" begin="0" end="2">
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						<c:set var="len20" value="${len20+1}"/>
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_un_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_1}
							</td>
						</tr>
						</c:forEach>		
					<c:if test="${len20 eq 0 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len20 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len20 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
							</table>
				</div>
				<div>
					<table style="width:30px;"><c:set var="len21" value="0"/>
					<tr>
							<th>
								21
							</th>
						</tr>
						<c:forEach var="list" items="${competeList21}" varStatus="idx" begin="0" end="2">
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						<c:set var="len21" value="${len21+1}"/>
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_un_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_1}
							</td>
						</tr>
						</c:forEach>		
					<c:if test="${len21 eq 0 }">
							<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len21 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len21 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						
					</table>
				</div>
				<div>
					<table style="width:30px;"><c:set var="len22" value="0"/>
						<tr>
							<th>
								22
							</th>
						</tr>
						<c:forEach var="list" items="${competeList22}" varStatus="idx" begin="0" end="2">
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						<c:set var="len22" value="${len22+1}"/>
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_un_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_1}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_1}
							</td>
						</tr>
						</c:forEach>		
					<c:if test="${len22 eq 0 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len22 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len22 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
							</table>
				</div>
				</div>
								<div style="border: 0px solid #00CC00; margin: 10px;">
				<div>
					<table style="width:60px;">
						<tr>
							<th>
								九九旅行
							</th>
						</tr>
						<tr>
							<td>
								创建时间
							</td>
						</tr>
						<tr>
							<td>
								CDG竞价
							</td>
						</tr>
						<tr>
							<td>
								CDG排名
							</td>
						</tr>
							<tr>
							<td>
								非CDG竞价
							</td>
						</tr>
						<tr>
							<td>
								非CDG排名
							</td>
						</tr>
						<tr>
							<td>
								票数
							</td>
						</tr>
						<tr style="background: #BEE0FC;">
							<td>
								创建时间
							</td>
						</tr>
						<tr style="background: #BEE0FC;">
							<td>
								CDG竞价
							</td>
						</tr>
						<tr style="background: #BEE0FC;">
							<td>
								CDG排名
							</td>
						</tr>
						<tr style="background: #BEE0FC;">
							<td>
								非CDG竞价
							</td>
						</tr>
						<tr style="background: #BEE0FC;">
							<td>
								非CDG排名
							</td>
						</tr>
						<tr style="background: #BEE0FC;">
							<td>
								票数
							</td>
						</tr>
						<tr>
							<td>
								创建时间
							</td>
						</tr>
						<tr>
							<td>
								CDG竞价
							</td>
						</tr>
						<tr>
							<td>
								CDG排名
							</td>
						</tr>
							<tr>
							<td>
								非CDG竞价
							</td>
						</tr>
						<tr>
							<td>
								非CDG排名
							</td>
						</tr>
						<tr>
							<td>
								票数
							</td>
						</tr>
					</table>
				</div>
				<div>
					<table style="width:30px;">
						<tr>
							<th>
								8
							</th>
						</tr>
						<c:forEach var="list" items="${competeList8}" varStatus="idx" begin="0" end="2">
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_2}
							</td>
						</tr>
						</c:forEach>
						<c:if test="${len8 eq 0 }">
							<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len8 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len8 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
								
					</table>
				</div>
				<div>
					<table style="width:30px;">
						<tr>
							<th>
								9
							</th>
						</tr>
						<c:forEach var="list" items="${competeList9}" varStatus="idx" begin="0" end="2">
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_2}
							</td>
						</tr>
						</c:forEach>		
						<c:if test="${len9 eq 0 }">
							<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len9 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len9 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						
					</table>
				</div>
				<div>
					<table style="width:30px;">
						<tr>
							<th>
								10
							</th>
						</tr>
						<c:forEach var="list" items="${competeList10}" varStatus="idx" begin="0" end="2">
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_2}
							</td>
						</tr>
						</c:forEach>		
					<c:if test="${len10 eq 0 }">
							<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len10 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len10 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
					
					</table>
				</div>
				<div>
					<table style="width:30px;">
						<tr>
							<th>
								11
							</th>
						</tr>
						<c:forEach var="list" items="${competeList11}" varStatus="idx" begin="0" end="2">
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_2}
							</td>
						</tr>
						</c:forEach>		
					
						<c:if test="${len11 eq 0 }">
							<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len11 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len11 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
					</table>
				</div>
				<div>
					<table style="width:30px;">
						<tr>
							<th>
								12
							</th>
						</tr>
						<c:forEach var="list" items="${competeList12}" varStatus="idx" begin="0" end="2">
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_2}
							</td>
						</tr>
						</c:forEach>		
					<c:if test="${len12 eq 0 }">
							<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len12 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len12 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
							
					</table>
				</div>
				<div>
					<table style="width:30px;">
						<tr>
							<th>
								13
							</th>
						</tr>
						<c:forEach var="list" items="${competeList13}" varStatus="idx" begin="0" end="2">
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_2}
							</td>
						</tr>
						</c:forEach>		
					<c:if test="${len13 eq 0 }">
							<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len13 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len13 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
					</table>
				</div>
				<div>
					<table style="width:30px;">
					<tr>
							<th>
								14
							</th>
						</tr>
						<c:forEach var="list" items="${competeList14}" varStatus="idx" begin="0" end="2">
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_2}
							</td>
						</tr>
						</c:forEach>		
					<c:if test="${len14 eq 0 }">
							<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len14 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len14 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
					</table>
				</div>
				<div>
					<table style="width:30px;">
					<tr>
							<th>
								15
							</th>
						</tr>
						<c:forEach var="list" items="${competeList15}" varStatus="idx" begin="0" end="2">
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_2}
							</td>
						</tr>
					<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_2}
							</td>
						</tr>
						</c:forEach>		
						<c:if test="${len15 eq 0 }">
							<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len15 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len15 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
					
					</table>
				</div>
				<div>
					<table style="width:30px;">
					<tr>
							<th>
								16
							</th>
						</tr>
						<c:forEach var="list" items="${competeList16}" varStatus="idx" begin="0" end="2">
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_2}
							</td>
						</tr>
						</c:forEach>		
					<c:if test="${len16 eq 0 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len16 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len16 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
					</table>
				</div>
				<div>
					<table style="width:30px;">
					<tr>
							<th>
								17
							</th>
						</tr>
						<c:forEach var="list" items="${competeList17}" varStatus="idx" begin="0" end="2">
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_2}
							</td>
						</tr>
						</c:forEach>		
					<c:if test="${len17 eq 0 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len17 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len17 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
					</table>
				</div>
				<div>
					<table style="width:30px;">
					<tr>
							<th>
								18
							</th>
						</tr>
						<c:forEach var="list" items="${competeList18}" varStatus="idx" begin="0" end="2">
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_2}
							</td>
						</tr>
						</c:forEach>		
					<c:if test="${len18 eq 0 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len18 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len18 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
					</table>
				</div>
				<div>
					<table style="width:30px;">
					<tr>
							<th>
								19
							</th>
						</tr>
						<c:forEach var="list" items="${competeList19}" varStatus="idx" begin="0" end="2">
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_2}
							</td>
						</tr>
						</c:forEach>		
						<c:if test="${len19 eq 0 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len19 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len19 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
					</table>
				</div>
				<div>
					<table style="width:30px;">
					<tr>
							<th>
								20
							</th>
						</tr>
						<c:forEach var="list" items="${competeList20}" varStatus="idx" begin="0" end="2">
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_2}
							</td>
						</tr>
						</c:forEach>		
					<c:if test="${len20 eq 0 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len20 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len20 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
					</table>
				</div>
				<div>
					<table style="width:30px;">
					<tr>
							<th>
								21
							</th>
						</tr>
						<c:forEach var="list" items="${competeList21}" varStatus="idx" begin="0" end="2">
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_2}
							</td>
						</tr>
						</c:forEach>		
					<c:if test="${len21 eq 0 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len21 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len21 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
					
					</table>
				</div>
				<div>
					<table style="width:30px;">
						<tr>
							<th>
								22
							</th>
						</tr>
						<c:forEach var="list" items="${competeList22}" varStatus="idx" begin="0" end="2">
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${fn:substringAfter(list.create_time, ' ')}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_money_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td>
								${list.compete_ranking_un_2}
							</td>
						</tr>
						<tr <c:if test="${idx.index eq 1}"> style="background: #BEE0FC;"</c:if> >
						
							<td  style="color:#F37022;font-weight:bolder;">
								${list.count_2}
							</td>
						</tr>
						</c:forEach>		
					<c:if test="${len22 eq 0 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len22 eq 1 }">
						<tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td>0</td></tr><tr style="background: #BEE0FC;"><td  style="color:#F37022;font-weight:bolder;">0</td></tr>
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
						<c:if test="${len22 eq 2 }">
						<tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td>0</td></tr><tr><td  style="color:#F37022;font-weight:bolder;">0</td></tr></c:if>
					</table>
				</div>
				</div>
			</form>
		</div>
	</body>
</html>