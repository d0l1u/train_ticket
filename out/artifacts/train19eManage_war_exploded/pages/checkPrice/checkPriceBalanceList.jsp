<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="com.l9e.transaction.vo.LoginUserVo" %>
<%@ page import="com.l9e.transaction.vo.PageVo"%>
<jsp:directive.page import="java.text.NumberFormat" />
<jsp:directive.page import="java.text.DecimalFormat" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>支付宝每日余额</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		 <script type="text/javascript" src="/js/jquery.cookie.js"></script>
		 	<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
		<script type="text/javascript">
		
		function queryTicket(){
			$("form:first").attr("action","/checkPrice/queryAlipayBalancePage.do");
			$("form:first").submit();
		}	
		
		function goback(){
			$("form:first").attr("action","/checkPrice/queryCheckPricePage.do");
			$("form:first").submit();
		}
		
		function exportExcel(){
			$("form:first").attr("action","/checkPrice/exportAlipayBalanceExcel.do");
			$("form:first").submit();
		}
		
		function div(){
	    /*var cheLength = document.getElementsByName("query_type");
	    	for(var i=0; i<cheLength.length; i++) {
	       //账号停用展示停用原因
	       if(cheLength[i].checked && cheLength[i].value == '22'){
	          document.getElementById('check_cp_div').style.display = "none";
	          document.getElementById('alipay').style.display = "none";
	          document.getElementById('abnormal').style.display = "none";
	          document.getElementById('check_tk_div').style.display = "block";
	       }
	       else if(!cheLength[i].checked && cheLength[i].value == '22'){
	          document.getElementById('check_cp_div').style.display = "block";
	          document.getElementById('alipay').style.display = "block";
	          document.getElementById('abnormal').style.display = "block";
	          document.getElementById('check_tk_div').style.display = "none";
	    	   }
	    	}*/
		}	
</script>
<style>
</style>
</head>
	<body onload="div();"><div></div>
		<div class="book_manage oz">
			<form action="/checkPrice/queryAlipayBalancePage.do" method="post" name="myform" id="myform">

				<ul class="order_num oz" style="margin-top: 10px;">
					<li>
						开始时间：&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="begin_info_time"
							readonly="readonly" value="${begin_info_time }"
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>
					<li>
						结束时间：&nbsp;&nbsp;&nbsp;
						<input type="text" class="text" name="end_info_time"
							readonly="readonly" value="${end_info_time }"
							onfocus="WdatePicker({doubleCalendar: true ,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
					</li>
				</ul>
				<ul class="order_num oz" style="margin-top: 10px;margin-bottom: 10px;">
					<li>
						支付宝账号：&nbsp;&nbsp;
							<select id="alipay_id" name="alipay_id">
								<option value="0" ${alipay_id=="0"?'selected':''}>-- 请选择 --</option>
							    <option value="02" ${alipay_id=="huochepiaokuyou02@19e.com.cn"?'selected':''}>huochepiaokuyou02@19e.com.cn</option>
							    <option value="03" ${alipay_id=="huochepiaokuyou03@19e.com.cn"?'selected':''}>huochepiaokuyou03@19e.com.cn</option>
							    <option value="04" ${alipay_id=="huochepiaokuyou04@19e.com.cn"?'selected':''}>huochepiaokuyou04@19e.com.cn</option>
							    <option value="05" ${alipay_id=="huochepiaokuyou05@19e.com.cn"?'selected':''}>huochepiaokuyou05@19e.com.cn</option>
							    <option value="06" ${alipay_id=="huochepiaokuyou06@19e.com.cn"?'selected':''}>huochepiaokuyou06@19e.com.cn</option>
							    <option value="07" ${alipay_id=="huochepiaokuyou07@19e.com.cn"?'selected':''}>huochepiaokuyou07@19e.com.cn</option>
							    <option value="08" ${alipay_id=="huochepiaokuyou08@19e.com.cn"?'selected':''}>huochepiaokuyou08@19e.com.cn</option>
							    <option value="09" ${alipay_id=="huochepiaokuyou09@19e.com.cn"?'selected':''}>huochepiaokuyou09@19e.com.cn</option>
							    <option value="10" ${alipay_id=="huochepiaokuyou10@19e.com.cn"?'selected':''}>huochepiaokuyou10@19e.com.cn</option>
							    <option value="11" ${alipay_id=="huochepiaokuyou11@19e.com.cn"?'selected':''}>huochepiaokuyou11@19e.com.cn</option>
							    <option value="12" ${alipay_id=="huochepiaokuyou12@19e.com.cn"?'selected':''}>huochepiaokuyou12@19e.com.cn</option>
							    <option value="13" ${alipay_id=="huochepiaokuyou13@19e.com.cn"?'selected':''}>huochepiaokuyou13@19e.com.cn</option>
							    <option value="14" ${alipay_id=="huochepiaokuyou14@19e.com.cn"?'selected':''}>huochepiaokuyou14@19e.com.cn</option>
							    <option value="15" ${alipay_id=="huochepiaokuyou15@19e.com.cn"?'selected':''}>huochepiaokuyou15@19e.com.cn</option>
							    <option value="16" ${alipay_id=="huochepiaokuyou16@19e.com.cn"?'selected':''}>huochepiaokuyou16@19e.com.cn</option>
							    <option value="17" ${alipay_id=="huochepiaokuyou17@19e.com.cn"?'selected':''}>huochepiaokuyou17@19e.com.cn</option>
							    <option value="18" ${alipay_id=="huochepiaokuyou18@19e.com.cn"?'selected':''}>huochepiaokuyou18@19e.com.cn</option>
							    <option value="19" ${alipay_id=="huochepiaokuyou19@19e.com.cn"?'selected':''}>huochepiaokuyou19@19e.com.cn</option>
							    <option value="20" ${alipay_id=="huochepiaokuyou20@19e.com.cn"?'selected':''}>huochepiaokuyou20@19e.com.cn</option>
							    <option value="21" ${alipay_id=="huochepiaokuyou21@19e.com.cn"?'selected':''}>huochepiaokuyou21@19e.com.cn</option>
							</select>
					</li>
				</ul>
				<p>
					<input type="button" value="查  询" class="btn" style="margin-top: 10px;margin-left: -12px;" onclick="queryTicket();"/>
					<input type="button" value="返  回" class="btn btn_normal" onclick="javascript:goback();" />
					<input type="button" value="导出Excel" class="btn btn_normal" style="margin-left: 3px;" onclick="exportExcel();"/><br/>
				</p>
				<div id="hint" class="pub_con" style="display:none"></div>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #EAEAEA;">
							<th style="10px;">
								NO
							</th>
							<th style="width: 200px;">
								支付时间
							</th>
							<th>
								支付宝账号
							</th>
							<th>
								账户余额
							</th>
						</tr>
						<c:forEach var="list" items="${checkPriceBalanceList}" varStatus="idx">
						<tr>
							<td>
								${idx.index+1 }
							</td>
								<td>
									${list.pay_time}
								</td>
								<td>
									${list.alipay_id}
								</td>
								<td>
									${list.account_balance }
								</td>
							</tr>
						</c:forEach>
					</table>
					<jsp:include page="/pages/common/paging.jsp" />
				</c:if>
			</form>
		</div>
	</body>
</html>
