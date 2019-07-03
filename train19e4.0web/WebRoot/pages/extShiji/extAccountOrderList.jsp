<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>商户对账管理页</title>
<link rel="stylesheet" href="/css/style.css" type="text/css" />
<link rel="stylesheet" href="/css/default.css" type="text/css" />
<script type="text/javascript" src="/js/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/js/artDialog.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/print.js"></script>
<script type="text/javascript">
	function exportExcelRefund() {
		if($.trim($("#begin_time").val())==""){
			$("#begin_time").focus();
			alert("请选择开始时间！");
			return;
		}
		if($.trim($("#end_time").val())==""){
			$("#end_time").focus();
			alert("请选择结束时间！");
			return;
		}
		var begin_time = $.trim($("#begin_time").val());//开始时间
		var end_time = $.trim($("#end_time").val());//结束时间
		var next_time = getNextMonth(begin_time);//一个月后的时间
		//alert("begin_time："+begin_time+"，end_time："+end_time+"，next_time："+next_time);
		if(next_time < end_time){
			alert("请选择一个月的时间段！");
			return;
		}
		$("form:first").attr("action","/extShiji/excelexportForExtAccount.jhtml");
		$("form:first").submit();
		$("form:first").attr("action","/extShiji/queryExtAccountOrderList.jhtml");
	}
	function submitForm() {
		if($.trim($("#begin_time").val())==""){
			$("#begin_time").focus();
			alert("请选择开始时间！");
			return;
		}
		if($.trim($("#end_time").val())==""){
			$("#end_time").focus();
			alert("请选择结束时间！");
			return;
		}
		var begin_time = $.trim($("#begin_time").val());//开始时间
		var end_time = $.trim($("#end_time").val());//结束时间
		var next_time = getNextMonth(begin_time);//一个月后的时间
		//alert("begin_time="+begin_time+",end_time="+end_time+",next_time="+next_time);
		if(next_time < end_time){
			alert("请选择一个月的时间段！");
			return;
		}
		$("#updateForm").submit();
	}
	/**
     * 获取下一个月
     * @date 格式为yyyy-mm-dd的日期，如：2014-01-25(下个月为：2014-02-25)
     */        
    function getNextMonth(date) {
        var arr = date.split('-');
        var year = arr[0]; //获取当前日期的年份
        var month = arr[1]; //获取当前日期的月份
        var day = arr[2]; //获取当前日期的日
        var days = new Date(year, month, 0);
        days = days.getDate(); //获取当前日期中的月的天数
        var year2 = year;
        var month2 = parseInt(month,10) + 1;//按照十进制计算
        if (month2 == 13) {
            year2 = parseInt(year2) + 1;
            month2 = 1;
        }
        var day2 = day;
        var days2 = new Date(year2, month2, 0);
        days2 = days2.getDate();
        if (day2 > days2) {
            day2 = days2;
        }
        if (month2 < 10) {
            month2 = '0' + month2;
        }
        var t2 = year2 + '-' + month2 + '-' + day2;
        return t2;
    }

</script>
</head>

<body>
	<div class="content oz">
	<div class="index_all">
				
		<!--左边内容 start-->
			<jsp:include flush="true" page="/pages/common/menu.jsp">
					<jsp:param name="menuId" value="extaccount" />
				</jsp:include>
		<!--左边内容 end-->
    	<!--右边内容 start-->
    	<div class="infoinput-right oz">
            <form action="/extShiji/queryExtAccountOrderList.jhtml" method="post" id="updateForm">
            <p style="margin-top:5px;">
				<span style="margin-left:0px;">
					订&nbsp;单&nbsp;号：<input style="width:145px;" type="text" name="order_id" value="${order_id}" 
		                	onkeyup="value=value.replace(/(^\s*)|(\s*$)/g,'');" />
				</span>
				<span style="margin-left:10px;">
					EOP订单号：<input style="width:145px;" type="text" name="eop_order_id" value="${eop_order_id}" 
		                	onkeyup="value=value.replace(/(^\s*)|(\s*$)/g,'');" />
				</span>
				<span style="margin-left:10px;">
					订单状态：
		                 <select name="order_status" id="order_status" style="width:85px;" value="${order_status }">
			        		<option value="00" <c:if test="${order_status eq '00'}">selected</c:if>>所有</option>
			        		<option value="44" <c:if test="${order_status eq '44'}">selected</c:if> >出票成功</option>
			        		<option value="45" <c:if test="${order_status eq '45'}">selected</c:if> >出票失败</option>
			       		</select>
				</span>
				<span style="margin-left:10px;">
					退款状态：
		                <select name="refund_status" id="refund_status" style="width:85px;" value="${refund_status }">
			        		<option value="" <c:if test="${refund_status eq ''}">selected</c:if>>所有</option>
			        		<option value="00" <c:if test="${refund_status eq '00'}">selected</c:if> >已差额退款</option>
			        		<option value="11" <c:if test="${refund_status eq '11'}">selected</c:if> >退款完成</option>
			       		</select>
				</span>
			</p>
			<p style="margin-top:5px;">
				<span style="margin-left:0px;">
					开始时间：<input style="width:145px;" type="text" id="begin_time" name="begin_time" 
		                    value="${begin_time}" readonly="readonly" 
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
				</span>
				<span style="margin-left:10px;">
					结束时间&nbsp;：<input style="width:145px;" type="text" id="end_time" name="end_time" 
		                    value="${end_time}" readonly="readonly" 
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
				</span>
				<span style="margin-left:10px;">
					支付类型：
		                <select name="pay_type" id="pay_type" style="width:85px;" value="${pay_type }">
			        		<option value="" <c:if test="${pay_type eq ''}">selected</c:if>>所有</option>
			        		<option value="0" <c:if test="${pay_type eq '0'}">selected</c:if> >19e钱包</option>
			        		<option value="1" <c:if test="${pay_type eq '1'}">selected</c:if> >支付宝</option>
			       		</select>
				</span>
				
				<span style="margin-left:10px;">
				 	<input type="button" class="btn search_order_btn" value="查&nbsp;询" style="padding:0;" onclick="submitForm();" />&nbsp;&nbsp;
				 	<input type="button" class="btn search_order_btn" value="下载报表" style="padding:0;" onclick="exportExcelRefund();" />
				</span>  
           </p>
            <!--订单号查询模块 end-->
            <!-- 页面显示模块，0全部订单显示 -->
	         	<div class="myorder oz">
	                <table >
	                	<tr style="background: #EAEAEA;">
		                	<th>NO</th>
		                	<th>订单号</th>
		                	<th>EOP订单号</th>
		                	<th>出发-到达</th>
		                	<th>支付时间</th>
		                	<th>退款时间</th>
		                	<th>支付类型</th>
							<th>订单状态</th>
							<th>退款状态</th>
							<th>支付金额</th>   
							<th>退款金额</th>                	
	                	</tr>
	                	<c:forEach items="${orderList}" var="order" varStatus="idx">
		                    <tr >
		                    	<td>${idx.index+1}</td>
		                    	<td>${order.order_id}</td>
		                    	<td>${order.eop_order_id }</td>
		                    	<!-- <td>${order.from_city}<br/>${order.to_city}</td> -->
		                    	<td><p style="line-height:22px;margin: 0;">${order.from_city}</p><p style="line-height:22px;margin: 0;"> ${order.to_city}</p></td>
		                    	<td><p style="line-height:22px;margin: 0;">${order.pay_time_start}</p><p style="line-height:22px;margin: 0;"> ${order.pay_time_end}</p></td>
		                        <td><p style="line-height:22px;margin: 0;">${order.refund_time_start}</p><p style="line-height:22px;margin: 0;">${order.refund_time_end}</p></td>
		                        <td>${payTypeMap[order.pay_type]}</td>
		                        <td>${orderStatusMap[order.order_status]}</td>
		                        <td>
			                        <c:if test="${order.refund_status eq '33'}">
				                        <c:choose>
				                        	<c:when test="${order.refund_type eq '4'}">
				                        		已差额退款
				                        	</c:when>
				                        	<c:otherwise>
				                        		退款完成
				                        	</c:otherwise>
				                        </c:choose>
			                        </c:if>
		                        </td>
		                        <td>${order.pay_money+0 }</td>
		                        <td>${order.refund_money }</td>
		                   </tr>
	                    </c:forEach>
	                </table>
	                <c:if test="${isShowList == 1}">
	                	<jsp:include page="/pages/common/paging.jsp" />
	                </c:if>
	            </div>
            
            </form>
        </div>
        <!--右边内容 end-->
        </div>
    </div>
</body>
</html>
