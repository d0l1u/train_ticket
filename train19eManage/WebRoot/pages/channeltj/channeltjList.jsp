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
		<title>渠道统计页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		 <script type="text/javascript" src="/js/jquery.cookie.js"></script>
		<script type="text/javascript">
		<%
			PageVo pageObject = (PageVo) request.getAttribute("pageBean");
			int pageIndex = pageObject.getPageIndex();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String user_level = loginUserVo.getUser_level();
		%>
		function exportExcel() {
			$("form:first").attr("action","/channeltj/exportChanneltjexcel.do");
			$("form:first").submit();
			$("form:first").attr("action","/channeltj/queryChanneltjList.do");
		}
		
		//全选selectAllChannel
		function selectAllChannel(){
			var checklist = document.getElementsByName("channel");
			if(document.getElementById("controlAllChannel").checked){
				for(var i=0; i<checklist.length; i++){
					checklist[i].checked = 1;
				}
			}else{
				for(var j=0; j<checklist.length; j++){
					checklist[j].checked = 0;
				}
			}
		}

		
	$(document).ready(function(){ 
		if( $.cookie("one") == "yes"){
			//根据Value值设置checkbox为选中值
			var channelStr = $.cookie("channel");
			if(channelStr==null){
				$("input[name='channel']").attr("checked",false);  
			}else{
				var channel = new Array(); //定义一数组
				channel = channelStr.split(",");
				//alert("第一次加载页面时从cookie中取到的值是："+channel+"，页面该渠道被选中状态");
				$("input[name='channel']").each(function(){
					for (i=0;i<channel.length ;i++ ){
						//alert(channel[i]);
						$("input:checkbox[value='"+ channel[i] +"']").attr('checked','true');
					}    
				}); 
			}
		} 
		$.cookie("one", "no", {expires : 7}); 
	});
	    
	function submitForm(){
		//遍历被选中CheckBox元素的集合 得到Value值
		var channelStr = "";
		$("input[name='channel']:checkbox:checked").each(function(){ 
			channelStr += $(this).val()+",";//遍历被选中CheckBox元素的集合 得到Value值
		}); 
		channelStr = channelStr.substring(0, channelStr.length-1);
		//alert("提交表单时存入cookie的值是："+channelStr);
		$.cookie("channel", channelStr, {expires : 7}); //cookie有效时间是7天
		$("#myform").submit();
	}
		
</script>
<style>
</style>
	</head>
	<body><div></div>
		<div class="book_manage oz">
			<form action="/channeltj/queryChanneltjList.do" method="post" name="myform" id="myform">

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
			
					<dl class="oz" style="padding-top:20px;">
						<dt style="float:left;padding-left:40px;line-height:22px;">渠道：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</dt>
						<dd  style="float:left;width:910px;">
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								<input type="checkbox" onclick="selectAllChannel()" name="controlAllChannel" style="controlAllChannel" id="controlAllChannel"/><label for="controlAllChannel">&nbsp;全部&nbsp;&nbsp;</label>
							</div>
						<c:forEach items="${merchantMap }" var="s" varStatus="index">
						<c:if test="${s.key ne '301016' && s.key ne '30101601' && s.key ne '30101602'}">
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
								
								<input type="checkbox" id="channel${index.count }" name="channel" value="${s.key }" value="1"
									<c:if test="${fn:contains(channelStr, s.key ) }">checked="checked"</c:if> />
								<label for="channel${index.count }">
									${s.value }
								</label>
							</div>
						</c:if>
						</c:forEach>
							<div class="ser-item" style="float:left;white-space:nowrap;width:70px;">
							<input type="checkbox" id="channel01" name="channel" value="30101612"
								<c:if test="${fn:contains(channelStr, '30101612')}">checked="checked"</c:if> />
								<label for="channel01">利安</label>
							</div>
						</dd>
					</dl>
				
				
				<p>
					<input type="button" value="查 询" class="btn" onclick="submitForm();" />
					<%
						if ("2".equals(loginUserVo.getUser_level()) ) {
           			%>
						<input type="button" value="导出Excel" class="btn" onclick="exportExcel()" />
					<%} %>
				</p>
				<div id="hint" class="pub_con" style="display:none"></div>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #EAEAEA;">
							<th>
								序号
							</th>
							<th>
								渠道
							</th>
							<th width="70px">
								日期
							</th>
								
							<th>
								查询总数
							</th>
							<th>
								查询成功
							</th>
							<th>
								查询失败
							</th>
							<th>
								下发短信
							</th>
							<th>
								改签总数
							</th>
							<th>
								改签成功
							</th>
							<th>
								改签失败
							</th>
							<th>
								退款总数
							</th>
							<th>
								退款成功
							</th>
							<th>
								退款失败
							</th>
							<th>
								改签成功率
							</th>
							<th>
								退款成功率
							</th>
						</tr>
						<c:forEach var="list" items="${channeltjList}" varStatus="idx">
						<tr>
							<td>
								${idx.count }
							</td>
							<td>
							<c:choose>
										<c:when test="${list.channel eq '30101612'}">
											利安
										</c:when>
										<c:otherwise>
											${merchantMap[list.channel] }
										</c:otherwise>
									</c:choose>
							</td>
							<td>
								${fn:substringBefore(list.create_time, ' ')}
							</td>
							<td>
								${list.search_count }
							</td>
							<td>
								${list.search_success }
							</td>
							<td>
								${list.search_fail }
							</td>
							<td>
								${list.msg_count }
							</td>
							<td>
								${list.alter_count }
							</td>
							<td>
								${list.alter_success }
							</td>
							<td>
								${list.alter_fail }
							</td>
							<td>
								${list.refund_count }
							</td>
							<td>
								${list.refund_success }
							</td>
							<td>
								${list.refund_fail }
							</td>
							<td>
								${list.alter_success_cgl }
							</td>
							<td>
								${list.refund_success_cgl }
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
