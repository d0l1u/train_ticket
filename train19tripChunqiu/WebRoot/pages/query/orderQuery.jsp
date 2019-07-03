<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>预订查询页</title>
<link rel="stylesheet" href="/css/style.css" type="text/css" />
<link rel="stylesheet" href="/css/default.css" type="text/css" />
<!-- 自适应高度 -->
<script type="text/javascript" src="/js/trendsHeight.js"></script>
<!-- 自适应高度 -->
<script type="text/javascript" src="/js/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/print.js"></script>
<script type="text/javascript" src="/js/artDialog.js"></script>
</head>

<body>
	<div class="content oz">
		<!--导航条 start-->
	    <div class="main_nav">
        	<ul class="oz">
            	<jsp:include flush="true" page="/pages/common/menu.jsp">
					<jsp:param name="menuId" value="query" />
				</jsp:include>
            </ul>
            <div class="slogan"></div>
        </div>
        <!--导航条 end-->
    	<!--左边内容 start-->
    	<div class="left_con oz">
            <form action="/chunqiu/query/queryOrderList.jhtml" method="post">
            <input type="hidden" id="isLogin" value="${isLogin }" />
            <!--订单号查询模块 start-->
            <dl class="order_num oz">
	                <dt style="padding-left:30px;">订单号</dt>
	                <dd class="dd_text"><input type="text" name="order_id" id="order_id" value="${order_id}" 
	                	onkeyup="value=value.replace(/(^\s*)|(\s*$)/g,'');" /></dd>
	                
			</dl>
			<dl class="order_num oz" style="padding-bottom:10px;">
	               	<dt>取票订单号</dt>
	                <dd class="dd_text"><input type="text" name="out_ticket_billno" id="out_ticket_billno" value="${out_ticket_billno}" 
	                	onkeyup="value=value.replace(/(^\s*)|(\s*$)/g,'');" /></dd>
	                <dd style="cursor:pointer;" class="dd_btn"><img src="../../images/chunqiu_search.jpg" id="query"></img></dd>
            </dl>	
            	
            <!--订单号查询模块 end-->
         	<div class="debook oz">
            	<h2>
                	<span class="nav_mid_bg">订单详情</span>
                </h2>
                <dl class="oz">
                	<dt></dt>
                	<dd></dd>
                </dl>
                <table>
                    <tr class="tit">
                		<td class="pad1">序 号</td>
                		<td width="90">出发-到达/方式</td>
                		
                		<td>订单号/支付金额</td>
                	
                		<td>创建时间/支付时间</td>
                		<td>出票时间/订单状态</td>
                		
                		<td>操 作</td>
                	</tr>
                	<c:forEach items="${orderList}" var="order" varStatus="idx">
              			
	                    <tr class="con">
	                    	<td>${idx.index+1}</td>
	                    	<td>${order.from_city}-${order.to_city}</td>
	                    	<td>${order.order_id}</td>
	                    	
	                    	<td>${order.create_time }</td>
	                    	
	                        <td>${order.out_ticket_time}</td>
	                        <td> 
	                        <input style="margin-top: 5px;"  type="button" class="btn" value="明细" onclick="javascript:window.location='/chunqiu/query/queryOrderDetail.jhtml?order_id=${order.order_id}&type=detail'" />
	                        <input style="margin-top: 5px;"  type="button" class="btn" value="投诉" onclick="javascript:window.location='/chunqiu/complain/complainIndex.jhtml?question=${order.order_id}&ques_Id=0'"/>
	                        <c:if test="${fn:contains('44', order.order_status )}"><!-- 12,22,33,44 -->
	                        		<c:choose>
	                        			<c:when test="${order.order_status eq '44' and order.out_ticket_type eq '22' }">
	                        			</c:when>
	                        			<c:when test="${order.can_refund != null and order.can_refund ne '1'}">
	                        			</c:when>
	                        			<c:when test="${order.is_before eq '1' and order.deadline_ignore ne '1'}">
	                        			</c:when>
	                        			<c:when test="${order.is_deadline eq '1' and order.deadline_ignore ne '1'}">
	                        			</c:when>
	                        			<c:otherwise>
	                        				<input style="margin-top: 5px;"  type="button" class="btn" value="退款"  onclick="javascript:window.location='/chunqiu/query/queryOrderRefund.jhtml?order_id=${order.order_id}&type=refund'" />
	                        			</c:otherwise>
	                        		</c:choose>
	                        </c:if>
	                        </td>
	                    </tr>
	                    <tr class="order_num_state">
	                    	<td></td>
	                    	<td><c:if test="${order.out_ticket_type eq '11'}">自 提</c:if>
	                			<c:if test="${order.out_ticket_type eq '22'}">配 送</c:if></td>
	                		<td nowrap="nowrap">
	                			<font style="font-weight:bold;color:red;"><fmt:formatNumber value="${order.ticket_pay_money + order.bx_pay_money}" type="currency" pattern="#0.00"/></font>
	                		</td>
	                		<td>${order.pay_time}</td>
	                		
	                		<td>${orderStatusMap[order.order_status]}<c:if test="${!empty order.refund_status}">/${rsStatusMap[order.refund_status]}</c:if></td>
	                		<td>
	                		 <c:if test="${order.order_status eq '00' or order.order_status eq '99'}">
			                        	<c:if test="${order.is_repay eq '1'}">
			                        	 <input type="button" class="btn" value="重新支付" style="margin-top: 5px;width: 60px;" onclick="javascript:rePayOrder('${order.order_id}');" />
			                        	</c:if>
			                </c:if>
	                		</td>
	                	</tr>
	                   
                    </c:forEach>
                </table>
                <c:if test="${isShowList == 1}">
                	<jsp:include page="/pages/common/paging.jsp" />
                </c:if>
            </div>
            </form>
        
        	<!--业务所有标注 start-->
	        <div class="business-provider">
	        	
	        </div>
	        <!--业务所有标注 end-->
        </div>
        <!--左边内容 end-->
    </div>
    <div id="tip" class="tip_wrap" style="display: none;"><div class="tip"><b></b><span class="errMsg"></span></div></div>
<script type="text/javascript" language="JavaScript">
//登录状态，记在session或者cookie
var isLogin = $("#isLogin").val();

window.CHSyncLogin = function(d){
	var f = d.createElement('iframe');
	f.style.display = 'none';
	d.body.appendChild(f);
	return function(callback){
		f.src = 'http://help.ch.com/TPS/TrainApplyLogin' + '?vs=' + new Date().getTime();
		CHLoginCallback = callback || function(){};
	};
}(document);

	$("#query").click(function(){
		if(isLogin == "true"){
			$("form:first").attr("action", "/chunqiu/query/queryOrderList.jhtml");
			$("form:first").submit();
		}else{
			//登录成功，春秋返回user_id
			CHSyncLogin(function(){
				$("form:first").attr("action", "/chunqiu/query/queryOrderList.jhtml");
				$("form:first").submit();
		       });
		}
		if($.trim($("#order_id").val())=="" && $.trim($("#out_ticket_billno").val())==""){
			showErrMsg("order_id", "180px", "请输入订单号或者取票订单号！");
			return;
		}else{
			hideErrMsg("fromZh");
		}
		
		//消息框	
		var dialog = art.dialog({
			lock: true,
			fixed: true,
			left: '50%',
			top: '250px',
		    title: 'Loading...',
		    icon: "/images/loading.gif",
		    content: '正在查询，请稍候！'
			});
			$(".aui_titleBar").hide();
			$("form:first").submit();
		});
		
		function showErrMsg(id, _width, msg){
			$("#"+id+"_errMsg").remove();
			var offset = $("#"+id).offset();
			$obj=$("#tip").clone().attr("id", id+"_errMsg")
				.css({'position':'absolute', 'top':offset.top-30, 'left':offset.left, 'width':_width}).appendTo("body");
			$obj.find(".errMsg").text(msg).end().show();
		}
		
		function hideErrMsg(id){
			$("#"+id+"_errMsg").remove();
		}
		
		function rePayOrder(order_id){
  		$.ajax({
				url:"/chunqiu/order/orderRepay.jhtml?order_id="+order_id,
				type: "POST",
				cache: false,
				success: function(res){
					if(res=='success'){
						window.location='/chunqiu/order/orderComfirm.jhtml?order_id='+order_id;
					}else{
						var dialog = art.dialog({
							lock: true,
							fixed: true,
							left: '50%',
							top: '40%',
						    title: '提示',
						    okVal: '确认',
						    icon: "/images/warning.png",
						    content: '对不起，您所选的车次已无票，请您重新购票！',
						    ok: function(){}
						});
						return false;
					}
				}
  		});
      }
</script>
</body>

</html>
