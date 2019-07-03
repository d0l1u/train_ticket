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
		/**
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
		*/
		$("#updateForm").submit();
	}
	/**
     * 获取下两个月
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
        var month2 = parseInt(month,10) + 2;//按照十进制计算
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
					<jsp:param name="menuId" value="saleReport" />
				</jsp:include>
		<!--左边内容 end-->
    	<!--右边内容 start-->
    	<div class="infoinput-right oz">
            <form action="/query/querySaleReportList.jhtml" method="post" id="updateForm">
            <p style="margin-top:5px;">
				<span style="margin-left:10px;">
					开始时间：&nbsp;<input style="width:145px;" type="text" id="begin_time" name="begin_time" 
		                    value="${begin_time}" readonly="readonly" 
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',minDate:'%y-{%M-2}-%d',maxDate:'%y-%M-%d'})" />
				</span>
				<span style="margin-left:20px;">
					结束时间：&nbsp;<input style="width:145px;" type="text" id="end_time" name="end_time" 
		                    value="${end_time}" readonly="readonly" 
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',minDate:'%y-{%M-2}-%d',maxDate:'%y-%M-%d'})" />
				</span>
				<span style="margin-left:20px;">
				 	<input type="button" class="btn search_order_btn" value="查&nbsp;询" onclick="submitForm();" />&nbsp;
				</span>  
           </p>
            <!--订单号查询模块 end-->
            <!-- 页面显示模块，0全部订单显示 -->
	         	<div class="myorder oz">
	                <table >
	                	<tr style="background: #EAEAEA;">
		                	<th>NO</th>
		                	<th>交易日期</th>
		                	<th>当天销售金额</th>
		                	<th>当天退款金额</th>
		                	<th>当天保险利润</th>
		                	<th>当天订单数</th>
		                	<th>当天票数</th>
							<th>本月总订单数</th>
							<th>本月总票数</th>
							<th>本月保险利润</th>   
	                	</tr>
	                	<c:forEach items="${orderList}" var="order" varStatus="idx">
		                    <tr >
		                    	<td>${idx.index+1}</td>
		                    	<td>${order.order_time}</td>
		                    	<td>${order.pay_money }</td>
		                    	<td>${order.refund_money }</td>
		                    	<td>${order.bx_money }</td>
		                    	<td>${order.order_count }</td>
		                    	<td>${order.ticket_count }</td>
		                    	<td>${order.month_order_count }</td>
		                        <td>${order.month_ticket_count }</td>
		                        <td>${order.month_bx_money }</td>
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
