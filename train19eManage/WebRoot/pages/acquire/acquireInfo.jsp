<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>自提订单明细页</title>
		<link rel="stylesheet" href="/css/style.css" type="text/css" />
		
		<style type="text/css">
			.pub_table td{height:20px;}
		</style>
		
		
		<script type="text/javascript" src="/js/jquery.js"></script>
		<script language="javascript" src="/js/layer/layer.js"></script>
		<script language="javascript" src="/js/mylayer.js"></script>
		<script type="text/javascript">
		/**function gotoRobot(order_id,create_time){
			//ajax验证是否锁
			var url = "/acquire/queryPayIsLock.do?order_id="+order_id+"&version="+new Date();
				$.get(url,function(data){
				if(data != null && data != ""){
					var temp = data ;
					var str1 = temp.split("&") ;
					var str =str1[1]; 
					
					alert("此订单已经锁定，锁定人为"+str);
					return;
				 }else{
					var  uri = "/acquire/updateAcquireToRobot.do?order_id="+order_id+"&order_status=01&create_time="+create_time+"&version="+new Date();
					$.post(uri,function(data){
						if(data=="yes"){
							//window.location="/acquire/queryAcquireList.do";
						}else{
							alert("机器人处理请求失败");
						}
					});
				 }
			 	 
			});
		}*/
	function gotoRobotHandle(order_id,create_time){
		if(confirm('确定机器处理吗?')){
			var url = "/acquire/updateAcquireToRobot.do?order_id="+order_id+"&order_status=01&create_time="+create_time+"&version="+new Date();
			$.get(url,function(data){
				if(data=="yes"){
				var index = parent.layer.getFrameIndex(window.name);
				parent.reloadPage();
				parent.layer.close(index);

					}else{
				alert("请求失败");
						}
				});
			return true;
		}
		return false;
	}	
		
		 
	function submitInfo(status, obj){
		if($(obj).attr("id")=="btnModify_12306" && $("#out_ticket_billno").val()==""){
			alert("12306单号不能为空！");
			$("#out_ticket_billno").focus();
			return;
		}
		
		if($(obj).attr("id")=="btnModify_12306" && $("#buy_money_total").val()==""){
			alert("出票金额不能为空！");
			$("#buy_money_total").focus();
			return;
		}
		
		
		
		if($(obj).attr("id")=="btnModify_12306" && $("#train_box_id").val()==""){
			alert("车厢号不能为空！");
			$("#train_box_id").focus();
			return;
		}
		
		if($(obj).attr("id")=="btnModify_12306" && $("#seat_no_id").val()==""){
			alert("坐位号不能为空！");
			$("#seat_no_id").focus();
			return;
		}
		
		if($(obj).attr("id")=="btnModify_12306" && $("#buy_money_id").val()==""){
			alert("成本票价不能为空！");
			$("#buy_money_id").focus();
			return;
		}
		
		if(($(obj).attr("id")=="outTicket_Lose" || $(obj).attr("id")=="cancleRobotOrder1" || $(obj).attr("id")=="cancleRobotOrder3" ||$(obj).attr("id")=="cancleRobotOrder4") && $("#error_info").val()== 000){
			alert("请选择失败原因！");
			$("#error_info").focus();
			return;
		}
		
		if('${orderInfo.manual_order}'=='22'){
			if($.trim($("#ctrip_order_id").val())==""){
				$("ctrip_order_id").focus();
				alert("携程订单号不能为空！");
				return;
			}
		}
		if($(obj).attr("id")=="btnModify_12306" && $("#check61").attr("checked")!="checked"){
			alert("请先勾选切换坐席注意事项！");
			return;
		}
		
		$("#order_status").val(status);
		if(confirm("是否提交？"+obj.value)){
		
		if('${orderInfo.manual_order}'=='22'){
			var ctrip_order_id = document.getElementById("ctrip_order_id").value;
			var url="/acquire/updateCtripGoStatusTo44.do?order_id="+'${orderInfo.order_id}'+"&ctrip_order_id="+ctrip_order_id;
				$.get(url,function(data){
				});
			}
			
			var index = parent.layer.getFrameIndex(window.name);
			$("#updateForm").submit();
			parent.reloadPage();
			parent.layer.close(index);
			
		}
	}
	
	
	function pay(){
		
		if(confirm("请检测价格，确定价格是否正确？")){
			$("#epayForm").submit();
		}
	}
	
	function submitUpdateInfo(){
		if($.trim($("#buy_money_total").val())==""){
			$("#buy_money_total").focus();
			alert("出票金额不能为空！");
			return;
		}
	
		
		var isValid = true;
		$(".train_box_class").each(function(){
			if($.trim($(this).val())==""){
				isValid = false;
				$(this).focus();
				alert("车厢号不能为空！");
				return false;
			}
		});
		if(isValid==false){
			return;
		}
		
		$(".seat_no_class").each(function(){
			if($.trim($(this).val())==""){
				isValid = false;
				$(this).focus();
				alert("座位号不能为空！");
				return false;
			}
		});
		if(isValid==false){
			return;
		}
		
		$(".buy_money_class").each(function(){
			if($.trim($(this).val())==""){
				isValid = false;
				$(this).focus();
				alert("成本票价不能为空！");
				return false;
			}
			
		});
		
		if(isValid==false){
			return;
		}
		$(".out_ticket_billno_calss").each(function(){
			if($.trim($(this).val())==""){
				isValid = false;
				$(this).focus();
				alert("12306单号不能为空！");
				return false;
			}
		});
		
		if(isValid==false){
			return;
		}
		
		if('${orderInfo.manual_order}'=='22'){
			$(".ctrip_order_id_class").each(function(){
			if($.trim($(this).val())==""){
				isValid = false;
				$(this).focus();
				alert("携程订单号不能为空！");
				return false;
			}
			});
		}
		
		if(isValid==false){
			return;
		}
		
		
		if(confirm("确认修改的信息与12306上的信息一致 ？")){
		$("#updateForm").attr("action", "/acquire/updateCpDetail.do");
		
			if('${orderInfo.manual_order}'=='22'){
			var ctrip_order_id = document.getElementById("ctrip_order_id").value;
			var url="/acquire/updateCtripGoStatusTo44.do?order_id="+'${orderInfo.order_id}'+"&ctrip_order_id="+ctrip_order_id;
				$.get(url,function(data){
				});
			}
			
			var index = parent.layer.getFrameIndex(window.name);
			$("#updateForm").submit();
			parent.reloadPage();
			parent.layer.close(index);
		}
	}
	
	function submitUpdateStatusInfo(){
		if(confirm("确认重发出票么？")){
			$("form:first").attr("action","/acquire/updateInfoOrderStatus.do");
			var index = parent.layer.getFrameIndex(window.name);
			
			$("form:first").submit();
			parent.reloadPage();
			parent.layer.close(index);
		}
	}
	
	function changeSeatType(obj){
		this.obj = obj;
		var create_time = this.obj;
		if(confirm("若预订成功，请先去12306取消订单，然后退到列表页面点击机器处理！")){
			var create_time = obj;
			$("form:first").attr("action", "/acquire/updateChangeSeatType.do?create_time="+create_time);
			$("form:first").submit();
		}
	}
	
	function copyBookInfo(obj){
		var lockAccount = obj;
		var url="/acquire/lockAccount.do?lockAccount="+lockAccount;
		$.get(url,function(data){
			if(data=='yes'){
				alert("12306账号锁定成功！");
			}else{
				alert("12306账号锁定失败！");
			}
		});
	}
	
	function updateAccount(order_id,acc_id,channel,acc_username,acc_password ,create_time){
		if(confirm('确定切换吗?')){
			//var url="/acquire/updateAccount.do?order_id="+order_id+"&account_id="+acc_id+" &channel="+channel+"&acc_username="+acc_username+"&acc_password="+acc_password+" &create_time="+create_time;
			var stopAccount = 'no';
			var stop_reason = '';
			if($("#checkStopAccount").attr("checked")=="checked"){
				stopAccount='yes';
				if($("#stop_reason").val()==""){
					alert("停用原因不能为空！");
					$("#stop_reason").focus();
					return;
				}
				stop_reason = $("#stop_reason").val();
			}
			
			var url="/acquire/updateAccount.do";
			$.post(url,{stopAccount:stopAccount,order_id:order_id,account_id:acc_id,channel:channel,acc_username:acc_username,acc_password:acc_password,create_time:create_time,stop_reason:stop_reason},function(data){
				if(data!=null && data=="yes"){
					window.location.reload();
				}else{
					alert("当前没有空余账号！");
				}
			});
			return true;
		}
		return false;
	}
	//cancleOrder('${orderInfo.acc_username }','${orderInfo.acc_password }','${orderInfo.order_id}','${orderInfo.out_ticket_billno }','${orderInfo.channel }','${orderInfo.create_time }','${orderInfo.error_info}','83',this)
	function cancleOrder(acc_username,acc_password,order_id,out_ticket_billno,channel,create_time,error_info,status,obj){
		if(confirm('确定取消订单吗?')){
			if(($(obj).attr("id")=="cancleRobotOrder1" || $(obj).attr("id")=="cancleRobotOrder3") && $("#error_info").val()== 000){
				alert("请选择失败原因！");
				$("#error_info").focus();
				return;
			}
			var index = parent.layer.getFrameIndex(window.name);
			//alert(${orderInfo.order_status});
			$("#order_status").val(status);
			$("#updateForm").submit();
			//var url = "/acquire/queryRobotCancleOrder.do?acc_username=acc_username&acc_password=acc_password&order_id=order_id&out_ticket_billno=out_ticket_billno&channel=channel&status:status&version="+new Date();
			
			if(${orderInfo.order_status}=='61'){
				$.ajax({
			           url:"/acquire/queryRobotCancleOrder.do?acc_username=${orderInfo.acc_username }&acc_password=${orderInfo.acc_password }&order_id=${orderInfo.order_id}&out_ticket_billno=${orderInfo.out_ticket_billno }&channel=${orderInfo.channel }&status=83&create_time=${orderInfo.create_time }&error_info=1&version="+new Date(),
			           type: "POST",
			           data:{acc_username:acc_username,
							 acc_password:acc_password,
							 order_id:order_id,
							 out_ticket_billno:out_ticket_billno,
							 channel:channel,
							 status:status,
							 create_time:create_time,
							 error_info:error_info},
					   dataType: "json",
			           cache: false
			       });
			}else{
				$.ajax({
					   url:"/acquire/queryRobotCancleOrder.do?acc_username=${orderInfo.acc_username }&acc_password=${orderInfo.acc_password }&order_id=${orderInfo.order_id}&out_ticket_billno=${orderInfo.out_ticket_billno }&channel=${orderInfo.channel }&status=83&create_time=${orderInfo.create_time }&error_info="+document.all('error_info').value+"&version="+new Date(),
			           type: "POST",
			           data:{acc_username:acc_username,
							 acc_password:acc_password,
							 order_id:order_id,
							 out_ticket_billno:out_ticket_billno,
							 channel:channel,
							 status:status,
							 create_time:create_time,
							 error_info:error_info},
					   dataType: "json",
			           cache: false
			       });
			}
			parent.reloadPage();
			parent.layer.close(index);
			/***
			var url = "/acquire/queryRobotCancleOrder.do?version="+new Date();	
			$.post(url,
					{acc_username:acc_username,
					 acc_password:acc_password,
					 order_id:order_id,
					 out_ticket_billno:out_ticket_billno,
					 channel:channel,
					 status:status,
					 create_time:create_time,
					 error_info:error_info},
			function(data){
				//success表示取消订单成功， failure表示取消订单失败， noorder表示没有在账号里查到该订单
				if(data=="failure"){
					alert("取消订单失败！");
				}else{
					$("#order_status").val(status);
					var index = parent.layer.getFrameIndex(window.name);
					//$("#updateForm").submit();
					parent.reloadPage();
					parent.layer.close(index);
				}
			});
			***/
			return true;
		}
		return false;
	}
	//updatePrice('${orderInfo.train_no}','${orderInfo.from_city}','${orderInfo.to_city}','${orderInfo.from_time}',
	//'${orderInfo.to_time}','${orderInfo.seat_type}','${cp.seat_no}','${cp.buy_money}','${cp.pay_money}')
	function updatePrice(order_id,cc,fz,dz,start_time,arrive_time,seat_type,seat_no,buy_money,pay_money){
		var url = "/acquire/updateCpTicketPrice.do?version="+new Date();
		$.post(url,
				{order_id:order_id,
				 train_no:cc,
				 from_city:fz,
				 to_city:dz,
				 from_time:start_time,
				 to_time:arrive_time,
				 seat_type:seat_type,
				 seat_no:seat_no,
				 buy_money:buy_money,
				 pay_money:pay_money
				 },
		function(data){
			if(data == "success"){
				alert("更新票价成功！");
			}else if(data =="equils"){
				alert("票价已更新");
			}else{
				alert("更新票价失败，请您重试！");
			}
		});
	}
	
	
	function ctripSearchAgain(order_id,create_time){
		if(confirm("确认重新查询么？")){
			$("#updateForm").attr("action", "/acquire/ctripSearchAgain.do?order_id="+order_id+"&create_time="+create_time);
			var index = parent.layer.getFrameIndex(window.name);
			$("#updateForm").submit();
			parent.reloadPage();
			parent.layer.close(index);
		}
	}
	
	
	function ctripOrderToEnd(order_id){
		alert("ctripOrderToEnd");
		var url="/acquire/ctripOrderToEnd.do?order_id="+order_id;
		$.get(url,function(data){
			alert(1);
		});
	}
</script>
	</head>
	<body>
		<div class="content1 oz">
			<!--左边内容 start-->
			<form action="/acquire/updateAcquireInfo.do" method="post" id="updateForm">
			<div class="left_con oz">
				<div class="pub_order_mes oz mb10_all">
					<h4>
						订单信息
					</h4>
					<div class="pub_con">
						<table class="pub_table" >
							<tr>
								<td class="pub_yuliu" rowspan="5"></td>
								<td width="234">
									订 单 号：
									<span>${orderInfo.order_id}</span>
									<input type="hidden" name="budan" value="${budan }"/>
								</td>
								<td>
									订单状态：
									<span>${acquireStatus[orderInfo.order_status] } </span>
								</td>
							</tr>
							<tr>
								<td>
									支付金额：
									<span>${orderInfo.pay_money}</span> 元
								</td>
								<td>
									支付时间：
									<span>${orderInfo.pay_time}</span>
								</td>
							</tr>
							<tr>
								<td>
									出票方式：
									<span><c:if test="${orderInfo.out_ticket_type eq '11'}">电子票</c:if>
										<c:if test="${orderInfo.out_ticket_type eq '22'}">配送票</c:if> </span>
								</td>
								<td>
									来源渠道：
									<span>
										${channel_types[orderInfo.channel] }
									</span>
								</td>
							</tr>
							<tr>
								<td>
									出票金额：
									<span> 
									<c:choose>
										<c:when test="${orderInfo.order_status ne '82' && orderInfo.manual_order ne '22' }">
										<c:choose>
											<c:when test="${orderInfo.pay_money<orderInfo.buy_money }">
												<a style="font-weight: bold; color: red;">${orderInfo.buy_money}</a>
												<input type="hidden" name="buy_money_total" id="buy_money_total" value="${orderInfo.buy_money}" />
											</c:when>
											<c:when test="${empty orderInfo.buy_money }">
												<input type="text" name="buy_money_total" id="buy_money_total" value="${orderInfo.buy_money}" />
											</c:when>
											<c:otherwise>
												<a style="font-weight: bold; ">${orderInfo.buy_money}</a>
												<input type="hidden" name="buy_money_total" id="buy_money_total" value="${orderInfo.buy_money}" />
											</c:otherwise>
										</c:choose> 
										</c:when>
										<c:otherwise>
											<input type="text" name="buy_money_total" id="buy_money_total" value="${orderInfo.buy_money}" />
										</c:otherwise>
									</c:choose>
									</span>
								</td>
								<td>
									出票时间：
									<span>${orderInfo.out_ticket_time}</span>
								</td>
							</tr>
							<tr>
							<c:if test="${orderInfo.order_status eq '10'}">
								<c:choose>
									<c:when test="${orderInfo.channel eq 'qunar'}">
										<td colspan="2">
												<span>失败原因：
													<c:choose>
														<c:when test="${orderInfo.error_info == 0}">
															<span style="color:red;">
																其他
															</span>
														</c:when>
														<c:when test="${orderInfo.error_info == 1}">
															<span style="color:red;">
																所购买的车次坐席已无票
															</span>
														</c:when>
														<c:when test="${orderInfo.error_info == 2}">
															<span style="color:red;">
																身份证件已经实名制购票，不能再次购买同日期同车次的车票
															</span>
														</c:when>
														<c:when test="${orderInfo.error_info == 3}">
															<span style="color:red;">
																${channel_types[orderInfo.channel]}票价和12306不符
															</span>
														</c:when>
														<c:when test="${orderInfo.error_info == 4}">
															<span style="color:red;">
																车次数据与12306不一致
															</span>
														</c:when>
														<c:when test="${orderInfo.error_info == 5}">
															<span style="color:red;">
																乘客信息错误
															</span>
														</c:when>
														<c:when test="${orderInfo.error_info == 6}">
															<span style="color:red;">
																12306乘客身份信息核验失败
															</span>
														</c:when>
														<c:otherwise></c:otherwise>
													</c:choose>
												</span>	
											</td>
									</c:when>
									<c:otherwise>
										<c:forEach items="${channel_type }" var="s" varStatus="index">
											<c:choose>
												<c:when test="${fn:contains(orderInfo.channel, s.key ) }">
													<td colspan="2">
														<span>失败原因：
															<c:choose>
																<c:when test="${orderInfo.error_info == 1}">
																	<span style="color:red;">
																		所购买的车次坐席已无票
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 2}">
																	<span style="color:red;">
																		身份证件已经实名制购票
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 3}">
																	<span style="color:red;">
																		票价和12306不符
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 4}">
																	<span style="color:red;">
																		乘车时间异常
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 5}">
																	<span style="color:red;">
																		证件错误
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 6}">
																	<span style="color:red;">
																		用户要求取消订单
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 7}">
																	<span style="color:red;">
																		未通过12306实名认证
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 8}">
																	<span style="color:red;">
																		乘客身份信息待核验
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 9}">
																	<span style="color:red;">
																		系统异常
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 10}">
																	<span style="color:red;">
																		高消费限制失败
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 11}">
																	<span style="color:red;">
																		超时未支付
																	</span>
																</c:when>
																
																<c:when test="${orderInfo.error_info == 12}">
																	<span style="color:red;">
																		信息冒用
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 13}">
																	<span style="color:red;">
																		排队人数过多，高铁管家要求失败
																	</span>
																</c:when>
																
																<c:when test="${orderInfo.error_info == 21}">																
																	<span style="color:red;">
																		传入12306账号未进行手机核验
																	</span>
																</c:when>
																
																<c:when test="${orderInfo.error_info == 22}">															
																	<span style="color:red;">
																		传入12306账号 用户达上限
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 23}">																
																	<span style="color:red;">
																		传入12306账号中存在未完成订单
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 24}">																
																	<span style="color:red;">
																		传入12306账号取消次数过多
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 25}">																
																	<span style="color:red;">
																		传入12306账号账号待核验
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 26}">																
																	<span style="color:red;">
																		传入12306账号用户不存在
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 27}">																
																	<span style="color:red;">
																		密码错误
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 28}">
																	<span style="color:red;">
																		不存在未支付订单或该订单状态不可支付
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 29}">
																	<span style="color:red;">
																		该车次未到起售时
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 30}">
																	<span style="color:red;">
																		暂未查询到该车次信息或车次已经停运
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 31}">
																	<span style="color:red;">
																		账号已被锁
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 32}">
																	<span style="color:red;">
																		已出票，不可取消
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 33}">
																	<span style="color:red;">
																		当前订单状态不可取消
																	</span>
																</c:when>
																<c:when test="${orderInfo.error_info == 37}">
																	<span style="color:red;">
																		距离开车时间太近
																	</span>
																</c:when>
																<c:otherwise></c:otherwise>
															</c:choose>
														</span>	
													</td>
												</c:when>
												<c:otherwise>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</c:otherwise>
								</c:choose>
								
							</c:if>
							</tr>
						</table>
					</div>
				</div>

				<div class="pub_order_mes  oz mb10_all">
					<h4>
						车票信息
					</h4>
					<div class="pub_con">
						<table class="pub_table"  style="width:90%;">
							<tr>
								<td class="pub_yuliu" rowspan="7">
									<strong>${orderInfo.train_no}</strong>
									<br />
									车次
								</td>
								<td  colspan="2"  style="color: red; font-weight: bold; font-size: 15px;">
									出 发/到 达：
									<span>${orderInfo.from_city}（${orderInfo.from_time}）</span>—
									<span>${orderInfo.to_city}（${orderInfo.to_time}）</span>
								</td>
							</tr>
							<tr>
								<td  width="234" style="color: red; font-weight: bold; font-size: 15px;">
									日 期：
									<span>
									<c:if test="${orderInfo.from_time_all ne null}">
										${orderInfo.from_time_all}
									</c:if>
									<c:if test="${orderInfo.from_time_all eq null}">
										${orderInfo.travel_time}
									</c:if>
									</span>
								</td>
								<td style="color: red; font-weight: bold; font-size: 15px;">
									价 格：
									<span>${orderInfo.pay_money}</span> 元
								</td>
							</tr>
							<tr>
								<td  style="color: red; font-weight: bold;">
									坐 席：${seattype[orderInfo.seat_type] }
								</td>
								<td style="color: red; font-weight: bold;">
									数 量：
									<span>${fn:length(cpList)}</span> 张
								</td>
							</tr>
							<c:choose>
								<c:when test="${fn:contains('61,82,44,AA', orderInfo.order_status)}">
									<tr>
										<td colspan="2">
											<span style="color:red;font-weight: bold;">
											备选坐席：
											</span>
											<input type="hidden" name="ext_seattypeStr" value="${orderInfo.ext_seattype }"/>
											<c:forEach var="seat" items="${seatList}"  varStatus="index">
												<input type="radio" id="ext_seattype${index.count }" name="ext_seattype" value="${seat.s_type}_${seat.money}"
												<c:if test="${ext_type eq seat.s_type  }">checked="checked"</c:if>/>
												<label for="ext_seattype${index.count }">
													${seat_type_qunar[seat.s_type]}${seat.money}元
												</label>
											</c:forEach>
											<c:if test="${!empty seatList && query_type eq '1'}">
												<input type="button" value="切换" onclick="changeSeatType('${orderInfo.create_time }');" />
											</c:if>
										</td>
									 </tr>
								</c:when>
								<c:otherwise>
									<tr>
										<td colspan="2">
											<span style="color:red;font-weight: bold;">
											备选坐席：
											</span>
											<input type="hidden" name="ext_seattypeStr" value="${orderInfo.ext_seattype }"/>
											<c:forEach var="seat" items="${seatList}"  varStatus="index">
													${seat_type_qunar[seat.s_type]}&nbsp;&nbsp;${seat.money}元
											</c:forEach>
										</td>
									 </tr>
								</c:otherwise>
							</c:choose>
							
							 <tr>
								<!-- 明细中显示12306单号 -->
								<td>
									<c:if test="${fn:contains('11,10,99', orderInfo.order_status)}">
									12306单号：
									<span>${orderInfo.out_ticket_billno }|</span>
									</c:if>
								</td>
							</tr>
							<tr>
								<td colspan="2"><span style="color:red;font-weight: bold;">预订信息：</span>${cpIndoSb }</td>
							</tr>
							<tr>
								<td colspan="2">
								  <span style="color:red;font-weight: bold;font-size: 15px;">客户预选的座位号：
									<c:choose>
										<c:when test="${empty  chooseSeats}">
											 无选座信息
										</c:when>
										<c:otherwise>
										 	${chooseSeats}
										</c:otherwise>
									</c:choose>
								 </span>
								</td>
							</tr>
							<!--  
							<tr>
								<td colspan="2"><input type="button" value="锁定12306账号" onclick="copyBookInfo('${orderInfo.acc_username }')"/></td>
							</tr>-->
						</table>
					</div>
				</div>
				<div class="pub_passager_mes oz mb10_all">
					<h4>
						乘客信息
					</h4>
					<div class="pub_con">
						<c:forEach var="cp" items="${cpList}" varStatus="idx">
							<c:if test="${idx.index != 0}">
								<%
                      				out.println("<div class=\"oz pas1\"><p class=\"border_top\"></p>");
                      			%>
							</c:if>
							<table class="pub_table">
								<tr>
									<td class="pub_yuliu" rowspan="10">

									</td>
									<td width="234" style="color: red; font-weight: bold;">
										姓 名：
										<span>${cp.user_name}</span>
									</td>
									<td>
										类 型：
										<span>${tickettype[cp.ticket_type] }</span>
									</td>
								</tr>
								<tr>
									<td width="234"
										style="color: red; font-weight: bold; font-size: 15px;">
										证件类型：
										<span>${idstype[cp.cert_type] }</span>
									</td>
									<td style="color: red; font-weight: bold; font-size: 15px;">
										证件号码：
										<span>${cp.cert_no}</span>
									</td>
								</tr>
								<c:if test="${fn:contains('11,10,99,66,61', orderInfo.order_status)}">
									<tr>
										<td width="234">
											车厢：
											<span>${cp.train_box }号</span>
										</td>
										<td>
											座位号：
											<span>${cp.seat_no }</span>
										</td>
									</tr>
									<tr>
										<td>
											12306票价：
											<span>${cp.buy_money }</span>
										</td>
										<td>
											车票单价:
											<span>${cp.pay_money }</span>&nbsp;&nbsp;&nbsp;
							
							<c:if test="${cp.buyMoney > cp.payMoney}">
								<input type="button" value="更新票价" 
								onclick="updatePrice('${orderInfo.order_id}','${orderInfo.train_no}','${orderInfo.from_city}','${orderInfo.to_city}','${orderInfo.from_time}','${orderInfo.to_time}','${orderInfo.seat_type}','${cp.seat_no}','${cp.buy_money}','${cp.pay_money}');"
								style="text-align:center;border:none;color:#fff;background-color:#2E8B57;cursor:pointer;width:50px;height:20px;"/>
							</c:if>
										</td>
									</tr>
								</c:if>
								<c:if test="${fn:contains('82,44,AA', orderInfo.order_status)}">
									<tr>
										<td width="234">
											<span>
												<input type="hidden" name="cp_id" value="${cp.cp_id}" />
											</span>
											车厢：&nbsp;&nbsp;&nbsp;&nbsp;
											<span><input type="text" size="4" id="train_box_id" class="train_box_class" name="train_box" value="${cp.train_box }"/>号</span>
										</td>
										<td>
											座位号：
											<span><input type="text" id="seat_no_id" class="seat_no_class" name="seat_no" value="${cp.seat_no }"/></span>
										</td>
									</tr>
									<tr>
										<td>
											12306票价：
											<span><input type="text" size="4" id="buy_money_id" class="buy_money_class" name="per_buy_money" value="${cp.buy_money}"/>元</span>
										</td>
										
										<td style="color: red; font-weight: bold; font-size: 15px;">
											车票单价:
											<span>${cp.pay_money }</span>&nbsp;&nbsp;&nbsp;
											<c:if test="${cp.buyMoney > cp.payMoney}">
															<input type="button" value="更新票价" 
												onclick="updatePrice('${orderInfo.order_id}','${orderInfo.train_no}','${orderInfo.from_city}','${orderInfo.to_city}','${orderInfo.from_time}','${orderInfo.to_time}','${orderInfo.seat_type}','${cp.seat_no}','${cp.buy_money}','${cp.pay_money}');"
												style="text-align:center;border:none;color:#fff;background-color:#2E8B57;cursor:pointer;width:50px;height:20px;"/>
											</c:if>
										</td>
									</tr>
								</c:if>
								<!-- <tr>
                        	<td>保 险：<span>20元保险保50万?</span></td>
                        	<td>状  态：<span>订购成功?</span></td>
                        </tr> -->
	                        	<c:if test="${cp.ticket_type ==3}">
	                        	<!-- 学生票 -->
	                        		<tr>
	                        			<td>省份名称:<span>${cp.province_name }</span></td>
	                        			<td>省份编号:<span>${cp.province_code}</span></td>
	                        		</tr>
	                        		<tr>
	                        			<td>学校代号:<span>${cp.school_code}</span></td>
	                        			<td>学校名称:<span>${cp.school_name}</span></td>
	                        		</tr>
	                        		<tr>
	                        			<td>学号:<span>${cp.student_no}</span></td>
	                        			<td>学制:<span>${cp.school_system}</span></td>
	                        		</tr>
	                        		<tr>
	                        			<td>入学年份:<span>${cp.enter_year }</span></td>
	                        		</tr>
	                        		<tr>
	                        			<td>优惠区间起始地名称:<span>${cp.preference_from_station_name }</span></td>
	                        			<td>优惠区间起始地代号:<span>${cp.preference_from_station_code }</span> </td>
	                        		</tr>
	                        		<tr>
	                        			<td>优惠区间到达地名称:<span>${cp.preference_to_station_name}</span></td>
	                        			<td>优惠区间到达地代号:<span>${cp.preference_to_station_code}</span></td>
	                        		</tr>
	                        	</c:if>
							</table>
							<c:if test="${idx.index != 0}">
								<%
                      				out.println("</div>");
                     			 %>
							</c:if>
						</c:forEach>
					</div>
						<input type="hidden" name="create_time"
							value="${orderInfo.create_time }" />
						<input type="hidden" name="order_id"
							value="${orderInfo.order_id }" />
						<input type="hidden" name="account_id"
							value="${orderInfo.acc_id }" />
						<input type="hidden" name="acc_username"
							value="${orderInfo.acc_username }" />
						<input type="hidden" name="order_status" id="order_status" />
						<input type="hidden" name="channel" value="${orderInfo.channel }"/>
						<input type="hidden" name="account_from_way" value="${orderInfo.account_from_way }"/>
 						<div class="pub_passager_mes oz mb10_all">
							<h4>
								出票账号
							</h4>
							<div class="pub_con">

								<table class="pub_table">
									<tr>
										<td class="pub_yuliu" rowspan="3"><c:if test="${orderInfo.account_from_way eq '1'}"><span style="color:red;">自有账号</span></c:if></td>
										<td width="234">
											登陆账号：
											<span>${orderInfo.acc_username }|</span>
										</td>
										<td>
											登陆密码：
											<span>${orderInfo.acc_password }|</span>
										</td>
									</tr>
									<tr>
										<c:if test="${(orderInfo.order_status eq '44'||orderInfo.order_status eq 'AA') && query_type eq '1'}">
											<td>
								<select name="stop_reason" id="stop_reason" style="width: 100px;" id="stop_reason">
				        			<option value="2" >取消订单过多</option>
									<option value="1" >账号被封</option>
									<option value="3" >联系人达上限</option>
									<option value="4" >未实名制</option>
									<option value="6" >用户取回</option>
									<option value="7" >手机核验</option>
				       			</select><input type="checkbox" name="checkStopAccount" id="checkStopAccount"/>是否<strong style="color:red;">停用</strong>账号
											</td>
											<td>
											<!-- 
												<a
													href="/acquire/updateAccount.do?
													order_id=${orderInfo.order_id  }&account_id=${orderInfo.acc_id }&channel=${orderInfo.channel }
													&acc_username=${orderInfo.acc_username}&acc_password=${orderInfo.acc_password }&create_time=${orderInfo.create_time }">
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;切换账号</a> -->
												<input type="button" value="切换" class="btn" style="width:47px;height:22px;background:url(../images/sprites.png) no-repeat -109px -173px;color:#fff;font:normal 12px/22px 'Simsun';" 
												onclick="updateAccount('${orderInfo.order_id  }','${orderInfo.acc_id }','${orderInfo.channel }','${orderInfo.acc_username }','${orderInfo.acc_password }','${orderInfo.create_time }')"/>
											</td>
										</c:if>
									</tr>
								</table>
							</div>
						</div>
						<c:if test="${fn:contains('61', orderInfo.order_status)}">
							<div class="pub_passager_mes oz mb10_all">
								<h4>
									人工付款
								</h4>
								<div class="pub_con">

									<table class="pub_table">

										<tr>
											<td class="pub_yuliu" rowspan="3">
											</td>
											<td width="234">
												12306单号：
												<span><input type="text" name="out_ticket_billno" id="out_ticket_billno"
														value="${orderInfo.out_ticket_billno }" /></span>
											</td>
											<td>
												付款金额：
												<span>${orderInfo.buy_money }</span>
											</td>
										</tr>
										<tr>
											<td width="234">
												付款账号：
												<span> 
													<select name="out_ticket_account" id="out_ticket_account">
														<c:forEach items="${bank_type}" var="type">
															<option value="${type.key }">
																${type.value }
															</option>
														</c:forEach>
													</select> 
												</span>
											</td>
											
											<td width="234">
												支付流水号：
												<span>
													<input type="text" name="bank_pay_seq" 
													id="bank_pay_seq_61"
													value="${orderInfo.bank_pay_seq }"/>
												</span>
											</td>
										</tr>
										<tr>
											<td>
												登陆账号：
												<span>${orderInfo.acc_username }|</span>
											</td>
											<td>
												登陆密码：
												<span>${orderInfo.acc_password }|</span>
											</td>
										</tr>
									</table>
								</div>
							</div>
						</c:if>
						<c:if test="${fn:contains('66', orderInfo.order_status)}">
							<div class="pub_passager_mes oz mb10_all">
								<h4>
									人工付款
								</h4>
								<div class="pub_con">

									<table class="pub_table">

										<tr>
											<td class="pub_yuliu" rowspan="3">
											</td>
											<td width="234">
												12306单号：
												<span>${orderInfo.out_ticket_billno }</span>
											</td>
											
										</tr>
										<tr>
											<td width="234">
												付款账号：
												<span> 
													<select name="out_ticket_account" id="out_ticket_account">
														<c:forEach items="${bank_type}" var="type">
															<option value="${type.key }">
																${type.value }
															</option>
														</c:forEach>
													</select> 
												</span>
											</td>
											<td>
												付款金额：
												<span>${orderInfo.buy_money }</span>
											</td>
										</tr>
										<tr>
											<td>
												登陆账号：
												<span>${orderInfo.acc_username }</span>
											</td>
											<td>
												登陆密码：
												<span>${orderInfo.acc_password }</span>
											</td>
										</tr>
									</table>
								</div>
							</div>
						</c:if>
						
						<c:if test="${fn:contains('82,77,83', orderInfo.order_status)}">
							<div class="pub_passager_mes oz mb10_all">
								<h4>
									人工付款
								</h4>
								<div class="pub_con">

									<table class="pub_table">

										<tr>
											<td class="pub_yuliu" rowspan="3">
											</td>
											<td width="234">
												12306单号：
												<span>
													<input type="text" name="out_ticket_billno" class="out_ticket_billno_calss" value="${orderInfo.out_ticket_billno }"/>
												</span>
											</td>
											<td width="234">
												支付流水号：
												<span>
													<input type="text" name="bank_pay_seq" class="bank_pay_seq_calss" value="${orderInfo.bank_pay_seq }"/>
												</span>
											</td>
										</tr>
										<tr>
											<td width="234">
												付款账号：&nbsp;&nbsp;
												<span> 
													<select name="out_ticket_account" id="out_ticket_account">
														<c:forEach items="${bank_type}" var="type">
															<option value="${type.key }">
																${type.value }
															</option>
														</c:forEach>
													</select> 
												</span>
											</td>
											<td>
												付款金额：
												<span>${orderInfo.buy_money }</span>
											</td>
										</tr>
										<tr>
											<td>
												登陆账号：
												<span>${orderInfo.acc_username }</span>
											</td>
											<td>
												登陆密码：
												<span>${orderInfo.acc_password }</span>
											</td>
										</tr>
										<tr>
										<td class="pub_yuliu"><br /></td>
											<td>
												<span style="float:left;line-height:20px;padding-right:12px;">
												失败原因:
												</span>
												<select name="error_info" id="error_info" style="float:left;">
													<option value="000">请选择</option>
													<c:choose>
														<c:when test="${orderInfo.channel eq 'qunar' }">
															<option value="0">其他</option>
															<option value="1">所购买的车次坐席已无票</option>
															<option value="2">身份证件已经实名制购票，不能再次购买同日期同车次的车票</option>
															<option value="3">${channel_types[orderInfo.channel]}票价和12306不符</option>
															<option value="4">车次数据与12306不一致</option>
															<option value="5">乘客信息错误</option>
															<option value="6">12306乘客身份信息核验失败</option>
														</c:when>
														<c:otherwise>
															<c:forEach items="${channel_type }" var="s" varStatus="index">
																<c:choose>
																	<c:when test="${fn:contains(orderInfo.channel, s.key ) }">
																		<option value="1">所购买的车次坐席已无票</option>
																		<option value="2">身份证件已经实名制购票</option>
																		<option value="3">${channel_types[orderInfo.channel]}票价和12306不符 </option>
																		<option value="4">乘车时间异常</option>
																		<option value="5">证件错误 </option>
																		<option value="6">用户要求取消订单 </option>
																		<option value="7">未通过12306实名认证 </option>
																		<option value="8">乘客身份信息待核验</option>
																		<c:if test="${s.key eq 'tongcheng'}">
																		<option value="9">系统异常</option>
																		<option value="11">超时未支付</option>
																		<option value="10">高消费限制失败</option>
																		<option value="12">信息冒用</option>
																		<option value="34">该车次不支持选座</option>
																		<option value="36">该坐席不支持选座</option>
																		</c:if>
																		<c:if test="${s.key eq 'tuniu'}">
																		<option value="10">高消费限制失败</option>
																		<c:if test="${orderInfo.account_from_way eq '1'}">
																			<option value="21">12036账号未通过手机核验</option>
																			<option value="22">12306账号所添加的常旅数量已达到上限</option>
																			<option value="23">存在未完成订单</option>
																			<option value="24">取消次数过多</option>
																			<option value="25">账号尚未通过身份信息核验</option>
																			<option value="26">账号用户名不存在</option>
																			<option value="27">12306账号密码有误</option>
																			<option value="28">不存在未支付订单或该订单状态不可支付</option>
																		</c:if>
																		<option value="29">该车次未到起售时间</option>
																		<option value="30">暂未查询到该车次信息或车次已经停运</option>
																		<option value="31">账号已被锁定</option>
																		<option value="32">已出票，不可取消</option>
																		<option value="33">当前订单状态不可取消</option>
																		<option value="37">距离开车时间太近</option>
																		</c:if>
																		<c:if test="${s.key eq '301030'}">
																		<option value="13">排队人数过多，高铁管家要求失败</option>
																		</c:if>
																		
																	</c:when>
																	<c:otherwise>
																		
																	</c:otherwise>
																</c:choose>
															</c:forEach>
														</c:otherwise>
													</c:choose>
												</select>
											</td>
										</tr>
									</table>
								</div>
							</div>
						</c:if>
						<c:if test="${fn:contains('99', orderInfo.order_status)}">
							<div class="pub_passager_mes oz mb10_all">
								<h4>
									付款明细
								</h4>
								<div class="pub_con">
									<table class="pub_table">
										<tr>
											<td class="pub_yuliu" rowspan="3">
												<br />
											</td>
											<td width="234">
												登陆账号：
												<span>${orderInfo.acc_username }</span>
											</td>
											<td>
												登陆密码：
												<span>${orderInfo.acc_password }</span>
											</td>
										</tr>
										<tr>
											<td width="234">
												付款账号：
												<span>${orderInfo.out_ticket_account }</span>
											</td>
											<td>
												付款金额：
												<span>${orderInfo.buy_money }</span>
											</td>
										</tr>
										<tr>
											<td>
												12306单号:
												<span>${orderInfo.out_ticket_billno }</span>
											</td>
											<td>
												支付流水号:
												<span>${orderInfo.bank_pay_seq }</span>
											</td>
										</tr>
									</table>
								</div>
							</div>
						</c:if>
						<c:if test="${fn:contains('44,AA', orderInfo.order_status)}">
							<div class="pub_passager_mes oz mb10_all">
								<h4>
									人工付款
								</h4>
								<div class="pub_con">
									<table class="pub_table" style="width:90%;">
										<tr>
											<td class="pub_yuliu" rowspan="3">
											<br /></td>
											<td width="234">
												12306单号:
												<span> 
													<input type="text" name="out_ticket_billno" id="out_ticket_billno"
														value="${orderInfo.out_ticket_billno }" /> 
												</span>
											</td>
										</tr>
										<tr>
											<td>
												<span style="float:left;line-height:20px;padding-right:12px;">
												失败原因:
												</span>
												<select name="error_info" id="error_info" style="float:left;">
													<option value="000">请选择</option>
													<c:choose>
														<c:when test="${orderInfo.channel eq 'qunar' }">
															<option value="0">其他</option>
															<option value="1">所购买的车次坐席已无票</option>
															<option value="2">身份证件已经实名制购票，不能再次购买同日期同车次的车票</option>
															<option value="3">${channel_types[orderInfo.channel]}票价和12306不符</option>
															<option value="4">车次数据与12306不一致</option>
															<option value="5">乘客信息错误</option>
															<option value="6">12306乘客身份信息核验失败</option>
														</c:when>
														<c:otherwise>
															<c:forEach items="${channel_type }" var="s" varStatus="index">
																<c:choose>
																	<c:when test="${fn:contains('qunar', s.key ) }">
																	</c:when>
																	<c:when test="${fn:contains(orderInfo.channel, s.key ) }">
																		<option value="1">所购买的车次坐席已无票</option>
																		<option value="2">身份证件已经实名制购票</option>
																		<option value="3">${channel_types[orderInfo.channel]}票价和12306不符 </option>
																		<option value="4">乘车时间异常</option>
																		<option value="5">证件错误 </option>
																		<option value="6">用户要求取消订单 </option>
																		<option value="7">未通过12306实名认证 </option>
																		<option value="8">乘客身份信息待核验</option>
																		<c:if test="${!(s.key eq 'tuniu')}">
																			<c:if test="${orderInfo.account_from_way eq '1'}">
																				<option value="21">12036账号未通过手机核验</option>
																				<option value="22">12306账号所添加的常旅数量已达到上限</option>
																				<option value="23">存在未完成订单</option>
																				<option value="24">取消次数过多</option>
																				<option value="25">账号尚未通过身份信息核验</option>
																				<option value="26">账号用户名不存在</option>
																				<option value="27">12306账号密码有误</option>
																				<option value="28">不存在未支付订单或该订单状态不可支付</option>
																			</c:if>
																		</c:if>
																		<c:if test="${s.key eq 'tongcheng'}">
																		<option value="9">系统异常</option>
																		<option value="11">超时未支付</option>
																		<option value="10">高消费限制失败</option>
																		<option value="12">信息冒用</option>
																		</c:if>
																		<c:if test="${s.key eq 'tuniu'}">
																		<option value="10">高消费限制失败</option>
																		<c:if test="${orderInfo.account_from_way eq '1'}">
																			<option value="21">12036账号未通过手机核验</option>
																			<option value="22">12306账号所添加的常旅数量已达到上限</option>
																			<option value="23">存在未完成订单</option>
																			<option value="24">取消次数过多</option>
																			<option value="25">账号尚未通过身份信息核验</option>
																			<option value="26">账号用户名不存在</option>
																			<option value="28">不存在未支付订单或该订单状态不可支付</option>
																			<option value="27">12306账号密码有误</option>
																		</c:if>
																		<option value="29">该车次未到起售时间</option>
																		<option value="30">暂未查询到该车次信息或车次已经停运</option>
																		<option value="31">账号已被锁定</option>
																		<option value="32">已出票，不可取消</option>
																		<option value="33">当前订单状态不可取消</option>
																		<option value="37">距离开车时间太近</option>
																		</c:if>
																		<c:if test="${s.key eq '301030'}">
																		<option value="10">高消费限制失败</option>
																		<option value="12">信息冒用</option>
																		<option value="13">排队人数过多，高铁管家要求失败</option>
																		</c:if>
																	</c:when>
																	<c:otherwise>
																		
																	</c:otherwise>
																</c:choose>
															</c:forEach>
														</c:otherwise>
													</c:choose>
												</select>
											</td>
										</tr>	
									</table>
								</div>
							</div>
						</c:if>
						<c:if test="${ orderInfo.manual_order eq '22'}">
							<div class="pub_passager_mes oz mb10_all" style="color: red;">
								<h4>
									<font color="#FFA500;">携程订单明细</font>
								</h4>
								<div class="pub_con">
									<table class="pub_table" style="width:90%;">
										<tr>
											<td class="pub_yuliu" rowspan="3">
											<br /></td>
											<th>
											<span style="float:left;line-height:20px;padding-right:12px;">
											<c:choose>
											<c:when test="${empty ctripInfo.ctrip_order_id }">
												携程单号：<input type="text" name="ctrip_order_id" id="ctrip_order_id" class="ctrip_order_id_class" value="${ctripInfo.ctrip_order_id}" />
											</c:when>
											<c:otherwise>
													携程单号：${ctripInfo.ctrip_order_id }|
													<input type="hidden" name="ctrip_order_id" id="ctrip_order_id" class="ctrip_order_id_class" value="${ctripInfo.ctrip_order_id}" />
											</c:otherwise>
											</c:choose> 
											</span>
											<!-- 
												<span style="float:left;line-height:20px;padding-right:12px;">
													携程单号：${ctripInfo.ctrip_order_id }
												</span>
											 -->
											</th>
										</tr>
										<tr>
											<th>
												<span style="float:left;line-height:20px;padding-right:12px;">
												携程账号：${ctripInfo.ctrip_name }/${ctripInfo.ctrip_password }
												</span>
											</th>
									    </tr>
									</table>
								</div>
							</div>
						</c:if>
						
					</div>
					</div>
					</form>
					<c:if test="${query_type eq '1' && orderInfo.manual_order ne '22'}">
					<div class="pub_debook_mes  oz mb10_all">
						<p>
							<c:if test="${orderInfo.order_status eq '44' ||orderInfo.order_status eq 'AA'}">
							<input type="checkbox" name="check61" id="check61"/>
							当前坐席是否与12306坐席一致?如果不一致<strong style="color:red;">必须</strong>先切换坐席后再点击机器处理!
							</c:if>
						</p>
						<p>
							<c:if test="${(orderInfo.order_status eq '44'||orderInfo.order_status eq 'AA') && canOperation!=1}">
							<input type="button" value="预定成功" id="btnModify_12306" onclick="submitInfo('61', this);"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#228B22;cursor:pointer;width:100px;height:30px;"/>
							<input type="button" value="出票失败" id="outTicket_Lose" onclick="submitInfo('10', this)" 
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#BA55D3;cursor:pointer;width:100px;height:30px;"/>
							<input type="button" value="机器处理" id="Robot" onclick="gotoRobotHandle('${orderInfo.order_id}','${orderInfo.create_time }');"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#4B0082;cursor:pointer;width:100px;height:30px;"/>
								<!-- 释放账号，向前台发出票失败通知 -->
							<input type="button" value="取消订单" id="cancleRobotOrder1" onclick="submitInfo('85',this);" 
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#F08080;cursor:pointer;width:100px;height:30px;"/> 
								<!--先取消12306的订单，释放账号，然后再向前台发通知  -->	
							</c:if>
							
							<c:if test="${orderInfo.order_status eq '61'}">
							<input type="button" value="支付完成" id="btnModify_bank_pay_seq" onclick="submitInfo('88',this)"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:green;cursor:pointer;width:100px;height:30px;"/>
							<input type="button" value="重新支付" onclick="submitInfo('55',this)"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#0000CD;cursor:pointer;width:100px;height:30px;"/>
							<input type="button" value="重新预订" onclick="submitInfo('01',this)"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#87CEFA;cursor:pointer;width:100px;height:30px;"/>
							<input type="button" value="取消订单" id="cancleRobotOrder2" onclick="submitInfo('85',this);" 
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#F08080;cursor:pointer;width:100px;height:30px;"/> 
							</c:if>
							<c:if test="${orderInfo.order_status eq '66'}">
							<input type="button" value="重新支付" onclick="submitInfo('55',this)"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#0000CD;cursor:pointer;width:100px;height:30px;"/>
							<input type="button" value="人工支付" onclick="submitInfo('61',this)"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#FFC0CB;cursor:pointer;width:100px;height:30px;"/>
							</c:if>

							<c:if test="${orderInfo.order_status eq '61'}">
							      <input type="button" value="出票成功" id="btnModify" onclick="submitUpdateInfo()"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#00FFFF;cursor:pointer;width:100px;height:30px;"/>
							</c:if>
							<c:if test="${orderInfo.order_status eq '82'}">
							<input type="button" value="重新出票" onclick="submitInfo('01',this)"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#87CEFA;cursor:pointer;width:100px;height:30px;"/>
							<input type="button" value="出票成功" id="btnModify" onclick="submitUpdateInfo()"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#00FFFF;cursor:pointer;width:100px;height:30px;"/>
							<input type="button" value="出票失败" id="outTicket_Lose" onclick="submitInfo('10', this)" 
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#BA55D3;cursor:pointer;width:100px;height:30px;"/>
							<input type="button" value="取消订单" id="cancleRobotOrder3" onclick="submitInfo('85',this);" 
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#F08080;cursor:pointer;width:100px;height:30px;"/> 
							</c:if>
							<c:if test="${orderInfo.order_status eq '11'}">
							<input type="button" value="重发出票" onclick="submitUpdateStatusInfo();"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:green;cursor:pointer;width:100px;height:30px;"/>
							</c:if>
							<c:if test="${orderInfo.order_status eq '77' || orderInfo.order_status eq '83'}">
								<input type="button" value="出票失败" id="outTicket_Lose" onclick="submitInfo('10', this)" 
									style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#BA55D3;cursor:pointer;width:100px;height:30px;"/>
								<input type="button" value="取消订单" id="cancleRobotOrder4" onclick="submitInfo('85',this);" 
									style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#F08080;cursor:pointer;width:100px;height:30px;"/> 
							</c:if>
							<input type="button" value="返回" onclick="backPage();"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#FFA500;cursor:pointer;width:100px;height:30px;"/>
						</p>
					</div>
					</c:if>
					
					<c:if test="${query_type eq '1' && orderInfo.manual_order eq '22'}">
					<div class="pub_debook_mes  oz mb10_all">
						<p>
							<c:if test="${orderInfo.order_status eq '44' }">
							<input type="checkbox" name="check61" id="check61"/>
							当前坐席是否与12306坐席一致?如果不一致<strong style="color:red;">必须</strong>先切换坐席后再点击机器处理!
							</c:if>
						</p>
						<p>
							<c:if test="${orderInfo.order_status eq '44' && canOperation!=1}">
							<input type="button" value="出票成功" id="btnModify" onclick="submitUpdateInfo()"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#00FFFF;cursor:pointer;width:100px;height:30px;"/>
							<input type="button" value="出票失败" id="outTicket_Lose" onclick="submitInfo('10', this)" 
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#BA55D3;cursor:pointer;width:100px;height:30px;"/>
							<input type="button" value="重新查询" onclick="ctripSearchAgain('${orderInfo.order_id }','${orderInfo.create_time }');"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:green;cursor:pointer;width:100px;height:30px;"/>
							</c:if>
							<c:if test="${orderInfo.order_status eq '61' && canOperation!=1}">
							<input type="button" value="支付完成" id="btnModify_bank_pay_seq" onclick="submitInfo('88',this)"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:green;cursor:pointer;width:100px;height:30px;"/>
							</c:if>
							<input type="button" value="返回" onclick="backPage();"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#FFA500;cursor:pointer;width:100px;height:30px;"/>
						</p>
					</div>
					</c:if>
					
					<c:if test="${query_type eq 'mingxi'}">
						<div class="pub_debook_mes  oz mb10_all">
							<p>
								<input type="button" value="返回" onclick="backPage();"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#FFA500;cursor:pointer;width:100px;height:30px;"/>
							</p>
						</div>
					</c:if>
					<div class="pub_passager_mes oz mb10_all">
						<h4>
							历史操作
						</h4>
						<div class="pub_con">
							<table class="pub_table" style="width: 650px; margin: 20px 20px;">
									<tr>
										<th style="width: 50px;">
											序号
										</th>
										<th style="width: 400px;">
											操作日志
										</th>
										<th style="width: 100px;">
											操作时间
										</th>
										<th style="width: 100px;">
											操作人
										</th>
									</tr>
								<c:forEach var="hs" items="${history}" varStatus="idx">
									<tr align="center">
										<td>
											${idx.index+1 }
										</td>
										<td align="left" style="word-break:break-all;width: 300px; ">
											${hs.order_optlog}
										</td>
										<td>
											${hs.create_time }
										</td>
										<td>
											${hs.opter }
										</td>
									</tr>
								</c:forEach>
							</table>
						</div>
					</div>
					<!--左边内容 end-->
				</div>

				<form id="epayForm" action="https://epay.12306.cn/pay/payGateway"
					method="post" target="_blank">
					<input type="hidden" name="interfaceName" value="PAY_SERVLET"></input>
					<input type="hidden" name="interfaceVersion" value="1.0"></input>
					<input type="hidden" name="tranData" value="${payorder.tran_data}"></input>
					<input type="hidden" name="merSignMsg"
						value="${payorder.mer_sign_msg}"></input>
					<input type="hidden" name="appId" value="${payorder.app_id}"></input>
					<input type="hidden" name="transType"
						value="${payorder.trans_type}"></input>
				</form>
			</body>
</html>
