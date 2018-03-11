<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<%@ page import="com.l9e.transaction.vo.PageVo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>账号统计明细</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
		<script language="javascript" src="/js/json2.js"></script>
		
<style>
table{border:2px solid #dadada;width:900px;text-align:center;color:#333;margin-left: 50px;}
th{ height: 50px;border:1px solid #dadada;}
tr{line-height: 30px; }
</style>
	</head>
	<body>
	<div style="margin: 20px auto;">
		<div style="margin-left: 90%;margin-bottom: 5px;"><a href="javascript:backPage();" >返回</a></div>
					<table>
						<tr style="background: #EAEAEA;">
							<th>
								序号
							</th>
							<th>
								日期
							</th>
							<th>
								渠道
							</th>
							<th>
								总账号
							</th>
							<th>
								总停用
							</th>
							<th>
								当前可用
							</th>
							<th>
								当日停用
							</th>
							<th>
								账号被封
							</th>
							<th>
								取消过多
							</th>
							<th>
								联系人达上线
							</th>
							<th>
								未实名制
							</th>
							<th>
								已达订购上限
							</th>
							<th>
								用户取回
							</th>
						</tr>
						<c:forEach var="list" items="${accounttjList}" varStatus="idx">
						<tr <c:if test="${fn:contains('1,5,9,13,17', idx.count )}">
									style="background: #BEE0FC;"
								</c:if> >
							<td>
								${idx.count+2*idx.index }
							</td>
							<td>
								${fn:substringBefore(list.create_time, ' ')}
							</td>
							<td>
								去哪
							</td>
							<td>
								${list.qunar_num }
							</td>
							<td>
								${list.qunar_stop }
							</td>
							<td>
								${list.qunar_can_use }
							</td>
							<td>
								${list.qunar_stop_today }
							</td>
							<td>
								${list.qunar_seal }
							</td>
							<td>
								${list.qunar_toomuch }
							</td>
							<td>
								${list.qunar_out }
							</td>
							<td>
								${list.qunar_not_real }
							</td>
							<td>
								${list.qunar_shici }
							</td>
							<td>
								${list.qunar_goback }
							</td>
						</tr>
						<tr <c:if test="${fn:contains('1,5,9,13,17', idx.count )}">
									style="background: #BEE0FC;"
								</c:if> >
							<td>
								${idx.count+2*idx.index+1 }
							</td>
							<td>
								${fn:substringBefore(list.create_time, ' ')}
							</td>
							<td>
								同程
							</td>
							<td>
								${list.tongcheng_num }
							</td>
							<td>
								${list.tongcheng_stop }
							</td>
							<td>
								${list.tongcheng_can_use }
							</td>
							<td>
								${list.tongcheng_stop_today }
							</td>
							<td>
								${list.tongcheng_seal }
							</td>
							<td>
								${list.tongcheng_toomuch }
							</td>
							<td>
								${list.tongcheng_out }
							</td>
							<td>
								${list.tongcheng_not_real }
							</td>
							<td>
								${list.tongcheng_shici }
							</td>
							<td>
								${list.tongcheng_goback }
							</td>
						</tr>
						<tr <c:if test="${fn:contains('1,5,9,13,17', idx.count )}">
									style="background: #BEE0FC;"
								</c:if> >
							<td>
								${idx.count+2*idx.index+2 }
							</td>
							<td>
								${fn:substringBefore(list.create_time, ' ')}
							</td>
							<td>
								艺龙
							</td>
							<td>
								${list.elong_num }
							</td>
							<td>
								${list.elong_stop }
							</td>
							<td>
								${list.elong_can_use }
							</td>
							<td>
								${list.elong_stop_today }
							</td>
							<td>
								${list.elong_seal }
							</td>
							<td>
								${list.elong_toomuch }
							</td>
							<td>
								${list.elong_out }
							</td>
							<td>
								${list.elong_not_real }
							</td>
							<td>
								${list.elong_shici }
							</td>
							<td>
								${list.elong_goback }
							</td>
						</tr>
						</c:forEach>
					</table>
			</div>		
	</body>
</html>
