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
		<title>失败订单统计页面</title>
		<link rel="stylesheet" href="/css/back_style.css" type="text/css" />
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/datepicker/WdatePicker.js"></script>
		 <script type="text/javascript" src="/js/jquery.cookie.js"></script>
		 	<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
		<script type="text/javascript">
		<%
			PageVo pageObject = (PageVo) request.getAttribute("pageBean");
			int pageIndex = pageObject.getPageIndex();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String user_level = loginUserVo.getUser_level();
		%>

		function mingxi(begin_info_time,channel){
			$("form:first").attr("action","/failtj/queryFailtjInfo.do?begin_info_time="+begin_info_time+"&channel="+channel);
			$("form:first").submit();
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
#width{width: 90px;}
</style>
	</head>
	<body><div></div>
		<div class="book_manage oz">
			<form action="/failtj/queryFailtjList.do" method="post" name="myform" id="myform">

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
					<input type="button" value="查 询" class="btn" onclick="submitForm();" style="margin-top: 10px;"/>
				</p>
				<div id="hint" class="pub_con" style="display:none"></div>
				<c:if test="${!empty isShowList}">
					<table>
						<tr style="background: #EAEAEA;">
							<th style="10px;">
								NO
							</th>
							<th width="90px">
								日期
							</th>
							<th width="40px">
								渠道
							</th>
							<th width="60px">
								总订单
							</th>
							<th width="40px">
								失败总数
							</th>
							<th width="40px">
								总成功率
							</th>
							<th width="40px">
								总失败率
							</th>
							<th id="width" >
								无票
							</th>
							<th id="width">
								实名制
							</th>
							<th id="width">
								票价不符
							</th>
							<th id="width">
								时间异常
							</th>
							<th id="width">
								证件错误
							</th>
							<th id="width">
								未实名
							</th>
							<th id="width">
								待核验
							</th>
							<th id="width">
								信息冒用
							</th>
							<th id="width">
								系统异常
							</th>
							<th id="width" >
								超时
							</th>
							<th id="width">
								用户取消
							</th>
							<th id="width" >
								其他
							</th>
							<th width="40px">
								操作
							</th>
						</tr>
						<%NumberFormat nf=new DecimalFormat("###0.00");
							
						%>

						<c:forEach var="list" items="${failtjList}" varStatus="idx">
							<tr <c:if test="${fn:contains('01,03,05,07,09,11,13,15,17,19,21,23,25,27,29,31', list.create_time3 )}"> 
							style="background: #BEE0FC;"</c:if>>
							<td>
							<input type="hidden" id="count" value="${idx.count }"/>
								${idx.count }
							</td>
							<td>
								${list.create_time2}
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
								${list.all_order }
							</td>
							<td>
								${list.count }
							</td>
							<td>
								${list.cgl }
							</td>
							<td>
								${list.zhanbi }
							</td>
							<td>
								${list.fail_1 }|${list.zhanbi_1 }
							</td>
							<td>
								${list.fail_2 }|${list.zhanbi_2 }
							</td>
							<td>
								${list.fail_3 }|${list.zhanbi_3 }
							</td>
							<td>
								${list.fail_4 }|${list.zhanbi_4 }
							</td>
							<td>
								${list.fail_5 }|${list.zhanbi_5 }
							</td>
							<td>
								${list.fail_7 }|${list.zhanbi_7 }
							</td>
							<td>
								${list.fail_8 }|${list.zhanbi_8 }
							</td>
							<td>
								${list.fail_12 }|${list.zhanbi_12 }
							</td>
							<td>
								${list.fail_9 }|${list.zhanbi_9 }
							</td>
							<td>
								${list.fail_11 }|${list.zhanbi_11 }
							</td>
							<td>
								${list.fail_6 }|${list.zhanbi_6 }
							</td>
							<td>
								${list.fail_0 }|${list.zhanbi_0}
							</td>
							<td>
								<a href="javascript:mingxi('${list.create_time2 }','${list.channel }')">明细</a>
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
