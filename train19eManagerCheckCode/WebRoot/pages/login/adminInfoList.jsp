<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>按天查询页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
	    <script language="javascript" src="/js/datepicker/WdatePicker.js"></script> 
	    <script type="text/javascript" src="/js/FusionCharts.js"></script>
	    <script type="text/javascript" src="/js/FusionChartsExportComponent.js"></script>
	    <script type="text/javascript">
		    function submitForm(){
		    	$("#myform").submit();
		    	//location.href = 'queryByAdmin.do';
			}

		    $(function () {  
		        odd_even();  
		        $(".nlist_1s").each(function () {  
		            var _color = $(this).css("backgroundColor");  
		            $(this).hover(function () {  
		                if (parseInt($(this).attr("class").indexOf("checked")) == -1) {  
		                    $(this).css({ "backgroundColor": "#cccccc", "color": "black" });  
		                }  
		            }, function () {  
		                if (parseInt($(this).attr("class").indexOf("checked")) == -1) {  
		                    $(this).css({ "backgroundColor": _color, "color": "#535353" });  
		                }  
		            });  
		            /**
		            $(this).click(function () {  
		                //所有行  
		                odd_even();  
		                //当前行  
		                $(this).css({ "backgroundColor": "#20B2AA", "color": "#ffffff" }).addClass("checked");  
		            });  
		            **/
		        });  
		    });  
		    function odd_even() {  
		        //偶数行 第一行为偶数0行  
		        $(".nlist_1s:odd").css({ "backgroundColor": "#FFFFF0", "color": "#535353" });  
		        //奇数行  
		        $(".nlist_1s:even").css({ "backgroundColor": "#E0F3ED", "color": "#535353" });  
		    }  	
	    </script>
	</head>
	<body>
		<div style="width:expression(document.body.clientWidth + 'px')">
		<div class="book_manage oz">
				<div style="border: 0px solid #00CC00; margin: 10px;">
				<ul class="order_num oz">
					<li>用户名：<span style="color:red">${opt_ren }</span>
					</li>
					<li>真实姓名：<span style="color:red">${opt_name }</span>
					</li>
					<li>
					<!-- 
						上月收入：￥<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${successPreCount/100 }</span>元
					 -->
					</li>
				</ul>
				</div>
				
					<table>
						<tr style="background: #f0f0f0;height:60px;">
							<th>
								统计类型
							</th>
							<th>
								打码总数
							</th>
							<th>
								打码正确
							</th>
							<th>
								打码错误
							</th>
							<th>
								打码超时
							</th>
							<!-- 
							<th>
								打码收入
							</th>
							 -->
							<th>
								截止时间
							</th>
						</tr>
						<tr style="background:#E0F3ED; height:60px;" class="nlist_1s">
							<td>
								当天打码
							</td>
							<td>
								${adminCodeTodayCount }
							</td>
							<td>
								${adminCodeTodaySuccess }
							</td>
							<td>
								${adminCodeTodayFail }
							</td>
							<td>
								${adminCodeTodayOvertime }
							</td>
							<!-- 
							<td>
								￥<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${adminCodeTodaySuccess/100 }</span>元
							</td>
							 -->
							<td>
								${time }
							</td>
						</tr>
						
						<tr style="background:#E0F3ED;height:60px; " class="nlist_1s">
							<td>
								昨日打码
							</td>
							<td>
								${yesterdayMap.pic_count }
							</td>
							<td>
								${yesterdayMap.pic_success }
							</td>
							<td>
								${yesterdayMap.pic_fail }
							</td>
							<td>
								${yesterdayMap.pic_unkonwn }
							</td>
							<!-- 
							<td>
								￥<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${adminCodeYesterdaySuccess/100 }</span>元
							</td>
							 -->
							<td>
								${yesterday }
							</td>
						</tr>
						
						<tr style="background:#E0F3ED;height:60px; " class="nlist_1s">
							<td>
								本周打码
							</td>
							<td>
								${weekMap.pic_count }
							</td>
							<td>
								${weekMap.pic_success }
							</td>
							<td>
								${weekMap.pic_fail }
							</td>
							<td>
								${weekMap.pic_unkonwn }
							</td>
							<!-- 
							<td>
								￥<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${adminCodeWeekSuccess/100 }</span>元
							</td>
							 -->
							<td>
								${yesterday }
							</td>
						</tr>
						
						<tr style="background:#E0F3ED; height:60px;" class="nlist_1s">
							<td>
								本月打码
							</td>
							<td>
								${monthMap.pic_count }
							</td>
							<td>
								${monthMap.pic_success }
							</td>
							<td>
								${monthMap.pic_fail }
							</td>
							<td>
								${monthMap.pic_unkonwn }
							</td>
							<!-- 
							<td>
								￥<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${adminCodeMonthSuccess div 100 }</span>元
							</td>
							 -->
							<td>
								${yesterday }
							</td>
						</tr>
						
						<tr style="background:#E0F3ED;height:60px;" class="nlist_1s">
							<td>
								总打码
							</td>
							<td>
								${countMap.pic_count }
							</td>
							<td>
								${countMap.pic_success }
							</td>
							<td>
								${countMap.pic_fail }
							</td>
							<td>
								${countMap.pic_unkonwn }
							</td>
							<!-- 
							<td>
								￥<span style="color:#f60;font-weight:bold;font-family:arial;font-size:16px;">${adminCodeTotalSuccess/100 }</span>元
							</td>
							 -->
							<td>
								${yesterday }
							</td>
						</tr>
					</table>
				<!--	<br/>
					
				 	<div class="pub_debook_mes  oz mb10_all">
						<input type="button" value="返 回" class="btn btn_normal"onclick="javascript:history.back(-1);" />
					</div>   -->
		</div>
		</div>
	</body>
</html>