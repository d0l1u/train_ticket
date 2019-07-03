<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>加盟管理明细页面</title>
		<link rel="stylesheet" href="/css/style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<link rel="stylesheet" href="/css/style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
		<script type="text/javascript">
		</script>
	</head>
	<body>
		<div class="content1 oz">
			<!--左边内容 start-->
    		<div class="left_con oz">
         		<div class="pub_order_mes oz mb10_all">
            		<h4>代理商信息</h4>
                	<div class="pub_con">
                		<table class="pub_table">
							<tr>
								<td class="pub_yuliu" rowspan="9"></td>
								<td width="234">姓名：<span>${joinUsDetail.user_name }</span></td>
								<td>
									店铺类型：<span>${shop_type[joinUsDetail.shop_type] }</span>
								</td>
							</tr>
							<tr>
								<td>店铺名称：<span>${joinUsDetail.shop_name }</span></td>
								<td>店铺简称：<span>${joinUsDetail.shop_short_name }</span></td>
							</tr>
							<tr>
								<td width="234">联系电话：<span>${joinUsDetail.user_phone }</span></td>
								<td>Q  Q  ：<span>${joinUsDetail.user_qq }</span></td>
							</tr>
							<!-- 
							<tr>
								<td>开始时间：<span>${joinUsDetail.begin_time }</span></td>
								<td>结束时间：<span>${joinUsDetail.end_time}</span></td>
							</tr>
							<tr>
								<td>最后订购金额：<span>${lastCreate.pay_money }</span></td>
								<td>最后订购时间：<span>${lastCreate.create_time }</span></td>
							</tr>
							 -->
							<tr>
								<td>申请时间：<span>${joinUsDetail.apply_time }</span></td>
								<td>审核时间：<span>${joinUsDetail.auditing_time }</span></td>
							</tr>
							<tr>
								<td>操作人：<span>${joinUsDetail.opt_person }</span></td>
								<td>操作时间：<span>${joinUsDetail.opt_time }</span></td>
							</tr>
							<tr>
								<td colspan="3">
									所属区域：
									<span>${joinUsDetail.PROVINCE_NAME }-</span>
									<span>${joinUsDetail.CITY_NAME }</span>
									<span>${joinUsDetail.DISTRICT_NAME }</span>
								</td>
							</tr>
							<tr>
								<td colspan="3">
									详细地址：
									<span>${joinUsDetail.user_address }</span>
								</td>
							</tr>
						<!-- 
						<td>
							级别：
							<c:if test="${joinUsDetail.user_level eq '0'}">
								<span><font color="#eac100"><strong>${level[joinUsDetail.user_level]}
								</strong> </font> </span>
							</c:if>
							<c:if test="${joinUsDetail.user_level eq '1'}">
								<span><font color="#ff0000"><strong>${level[joinUsDetail.user_level]}
								</strong> </font> </span>
							</c:if>
							<c:if test="${joinUsDetail.user_level eq '2'}">
								<span><font color="#000000"><strong>${level[joinUsDetail.user_level]}
								</strong> </font> </span>
							</c:if>
						</td>
						 -->
						</table>
               		</div>
            	</div>
            	<!-- 
				<input type='hidden' name='user_id' value='${joinUsDetail.user_id}' />
				 -->
				<div class="pub_order_mes  oz mb10_all">
            		<h4>实名认证</h4>
	                <div class="pub_con">
	            		<form name="queryFrm" action="/joinUs/queryJoinUsDetail.do?user_id=${joinUsDetail.user_id}" method="post">
	                		<div class="book_manage" style="width:660px;height:100%;">
								<span><font color="#f00"><strong>用户等级:	${agent_grade[joinUsDetail.agent_grade] }</strong></font></span>
	                			<c:if test="${totalCount > 0}">
	                				<table style="width:100%;height:100%;text-align:center;font-size:12px;border:#dadada 1px solid;">
										<tr style="background: #EAEAEA;">
											<th>序号</th>
											<th>姓名</th>
											<th>身份证号码</th>
											<th>联系人电话</th>
											<th>核验状态</th>
											<th>创建时间</th>
										</tr>
										<c:forEach var="list" items="${userRegistInfo}" varStatus="idx">
											<tr style="background: #FFFFFF;">
												<td>
													${idx.index+1}
												</td>
												<td>
													${list.user_name}
												</td>
												<td>
													${list.ids_card}
												</td>
												<td>
													${list.user_phone}
												</td>
												<td>
													${regist_status[list.regist_status]}
												</td>
												<td>
													${list.create_time}
												</td>
											</tr>
										</c:forEach>
									</table>
								</c:if>
								<c:if test="${totalCount == 0}">
				                	<table style="width:100%;height:100%;text-align:center;font-size:12px;border:#dadada 1px solid;">
										<tr style="background: #EAEAEA;">
											<td>没有实名认证信息</td>
										</tr>
									</table>
								</c:if>
							</div>
						</form>
					</div>
				</div>
				<div class="pub_order_mes  oz mb10_all">
					<h4>购买记录</h4>
                		<div class="pub_con">
	                		<div align=center>
								<div style='width: 100%; height: 300px; overflow: auto; scrollbar-face-color: #EAEAEA; '>
                		<!-- 
                			<table class="pub_table">
								<tr>
									<td class="pub_yuliu" rowspan="4"></td>
									<td width="123"> ${level[joinUsDetail.user_level] }
									</td>
									<td>
										<span>${joinUsDetail.sale_price}</span>&nbsp;${types[joinUsDetail.sale_type]}
									</td>
									<td class="td_detail">
										${joinUsDetail.describe }
									</td>
									<td width="132">
										&nbsp;
										<span><a href="/joinUs/queryUserOrder.do?user_id=${joinUsDetail.user_id }">加盟产品明细</a>
										</span>
									</td>
								</tr>
							</table>
						 -->
									<table width="500px" style="margin: 0 auto;">
										<tr>
											<td width="50%"><span><font color="#f00"><strong>上月总计:${sumPre.sumPre}元</strong></font></span></td>
										 	<td width="50%"><span><font color="#f00"><strong>本月总计:${sumNow.sumNow}元</strong></font></span></td>
										</tr>
							    	</table>
									<table width="500px" style="margin: 0 auto;">
									<tr>
										<td width="50%">
											<table width="100%" >
												<c:forEach var="detailPre" items="${joinUsDetailPreMouth}">
													<tr>
														<td width='150px;' height="24px">${detailPre.create_time }</td>
														<td height="24px">${detailPre.pay_money }</td>
													</tr>
												</c:forEach>
											</table>
										</td>
										<td width="50%">
											<table width="100%">
												<c:forEach var="detailNow" items="${joinUsDetailNowMouth}">
													<tr>
														<td height="24px" width="50%" align="center">
															${detailNow.create_time }
														</td>
														<td height="24px" align="center">
															${detailNow.pay_money }
														</td>
													</tr>
												</c:forEach>
											</table>
										</td>
									</tr>
								</table>	
							<!-- 
								<table width="500px" style="margin: 0 auto;">
									<tr align="center">
										<td width="50%">
											<span><font color="#f00"><strong>总计:${sumPre.sumPre}</strong></font></span>
									 	</td>
									 	<td width="50%">
											<span><font color="#f00"><strong>总计:${sumNow.sumNow}</strong></font></span>
									 	</td>
									</tr>
							    </table>
							   -->
							</div>
						</div>
					</div>
				</div>
				<div class="pub_debook_mes  oz mb10_all">
					<p>
						<input type="button" value="返 回" class="btn btn_normal" onclick="javascript:history.back(-1);" />
					</p>
				</div>
			</div>
		</div>
	</body>
</html>
