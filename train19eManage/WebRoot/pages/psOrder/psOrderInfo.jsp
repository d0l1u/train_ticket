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
		<script language="javascript" src="/js/city.js"></script>
		<script type="text/javascript">

		
		 
	function submitInfo(status, obj){
		if($(obj).attr("id")=="btnModify_12306" && $("#out_ticket_billno").val()==""){
			alert("火车票单号不能为空！");
			$("#out_ticket_billno").focus();
			return;
		}
		if($(obj).attr("id")=="btnModify_12306" && $("#buy_money_total").val()==""){
			alert("出票金额不能为空！");
			$("#buy_money_total").focus();
			return;
		}else if(Number($("#buy_money_total").val())>Number('${orderInfo.pay_money}')){
			alert("出票金额不能大于支付票价，票价不符请败单！");
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
		
		$("#order_status").val(status);
		if(confirm("是否提交？"+obj.value)){
			var index = parent.layer.getFrameIndex(window.name);
			$("#updateForm").submit();
			parent.reloadPage();
			parent.layer.close(index);
			
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
				alert("火车票单号不能为空！");
				return false;
			}
		});
		
		if(isValid==false){
			return;
		}
		
		if(confirm("确认修改的信息与实际订票的信息一致 ？")){
			var index = parent.layer.getFrameIndex(window.name);
			$("#updateForm").submit();
			parent.reloadPage();
			parent.layer.close(index);
		}
	}
	
	function changeSeatType(obj){alert(1);
		this.obj = obj;
		var create_time = this.obj;
		var num = 0;
		$("input[name='ext_seattype']:radio:checked").each(function(){ //遍历被选中CheckBox元素的集合 得到Value值
			num++;
		});
		if(num==0){
			alert("请选择备选坐席!");
		}else{
			var create_time = obj;
			$("form:first").attr("action", "/manual/updateChangeSeatType.do?create_time="+create_time);
			$("form:first").submit();
		}
	}
	

</script>
	</head>
	<body>
		<div class="content1 oz">
			<!--左边内容 start-->
			<form action="/psOrder/updatePsOrderInfo.do" method="post" id="updateForm">
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
								</td>
								<td>
									订单状态：
									<span>${psOrderStatus[orderInfo.order_status] } </span>
								</td>
							</tr>
							<tr>
								<td>
									支付票价：
									<span>${orderInfo.pay_money}</span> 元
								</td>
								<td>
									配送费：
									<span>25</span> 元
								</td>
							</tr>
							<tr>
								<td>
									出票金额：
									<span> 
									<c:choose>
											<c:when test="${empty orderInfo.buy_money }">
												<input type="text" name="buy_money_total" id="buy_money_total" value="${orderInfo.buy_money}" />
											</c:when>
											<c:otherwise>
												<a style="font-weight: bold; ">${orderInfo.buy_money}</a>
											</c:otherwise>
									</c:choose>
									</span>
								</td>
								<td>
									出票时间：
									<span>${orderInfo.out_ticket_time}</span>
								</td>
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
									支付金额：
									<span>${orderInfo.pay_money}</span> 元
								</td>
							</tr>
							<tr>
								<td  style="color: red; font-weight: bold;">
									坐 席：${seat_type[orderInfo.seat_type] }
								</td>
								<td style="color: red; font-weight: bold;">
									数 量：
									<span>${fn:length(cpList)}</span> 张
								</td>
							</tr>
							<c:choose>
								<c:when test="${fn:contains('00', orderInfo.order_status)}">
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
													${seat_type[seat.s_type]}${seat.money}元
												</label>
											</c:forEach>
											<c:if test="${!empty seatList && query_type eq '2'}">
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
													${seat_type[seat.s_type]}&nbsp;&nbsp;${seat.money}元
											</c:forEach>
										</td>
									 </tr>
								</c:otherwise>
							</c:choose>
							
							 <tr>
								<!-- 明细中显示12306单号 -->
								<td>
									<c:if test="${!empty orderInfo.out_ticket_billno}">
									12306单号：
									<span>${orderInfo.out_ticket_billno }</span>
									</c:if>
								</td>
							</tr>
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
									<td class="pub_yuliu" rowspan="4">

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
								<c:if test="${fn:contains('11', orderInfo.order_status)}">
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
							
										</td>
									</tr>
								</c:if>
								<c:if test="${fn:contains('00', orderInfo.order_status)}">
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
						
						<div class="pub_passager_mes oz mb10_all">
								<h4>
									人工出票
								</h4>
								<div class="pub_con">
									<table class="pub_table" style="width:90%;">
										<tr><td class="pub_yuliu" rowspan="4">
											<br /></td>
											<td width="234">
												用户要求购买下铺数:
												<span style="color:red;font-weight: bold;"> 
													${orderInfoPssm.choose_seat_num}
												</span>
											</td>
										</tr>
										<tr>
											<td width="234">
												下铺票数不足时，是否同意购买上中铺:
												<span style="color:red;font-weight: bold;"> 
													<c:if test="${orderInfoPssm.choose_ext eq '00'}">不同意</c:if>
													<c:if test="${orderInfoPssm.choose_ext eq '11'}">同意</c:if>
												</span>
											</td>
										</tr>
										<tr>
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
															<option value="1">所购买的车次坐席已无票</option>
															<option value="2">身份证件已经实名制购票</option>
															<option value="3">票价不符 </option>
															<option value="4">乘车时间异常</option>
															<option value="5">证件错误 </option>
															<option value="6">用户要求取消订单 </option>
															<option value="7">未通过12306实名认证 </option>
															<option value="8">乘客身份信息待核验</option>
												</select>
											</td>
										</tr>	
									</table>
								</div>
							</div>
						
						<div class="pub_passager_mes oz mb10_all">
								<h4>
									配送信息
								</h4>
								<div class="pub_con">
									<table class="pub_table" style="width:90%;">
										<tr>
											<td class="pub_yuliu" rowspan="5">
											<br /></td>
											<c:if test="${ orderInfo.order_status ne '00'}">
											<td width="234">
												配送单号:
												<span> 
													<input type="text" name="ps_billno" id="ps_billno"
														value="${orderInfoPssm.ps_billno }" /> 
												</span>
											</td>
										</tr>
										<tr>
											<td width="234">
												配送公司:
												<span> 
													<input type="text" name="ps_billno" id="ps_billno"
														value="${orderInfoPssm.ps_company }" /> 
												</span>
											</td>
											<td width="234">
												配送费 :
												<span> 
													<input type="text" name="ps_billno" id="ps_billno"
														value="${orderInfoPssm.pay_money }" /> 
												</span>
											</td>
											</c:if>
										</tr>	
										<tr>
											<td width="234">
												联系姓名:
												<span> 
													${orderInfoPssm.link_name_ps }
												</span>
											</td>
											<td width="234">
												联系电话:
												<span> 
													${orderInfoPssm.link_phone_ps }
												</span>
											</td>
										</tr>
										<tr>
											<td colspan="3">
				                        	所在省市：
					                    	<select name="province" id="province" disabled="disabled" style="background-color: #E4FACF"> 
											<option value="0">请选择省份</option> 
											</select> 
											<select name="city" id="city" disabled="disabled"style="background-color: #E4FACF">   
											<option value="0">请选择城市</option> 
											</select> 
											<select name="district" id="district" disabled="disabled"style="background-color: #E4FACF">  
											<option value="0">请选择区县</option> 
											</select> 
											<!--下列为初始值(可选,编辑表单时设置)--> 
											<input type="hidden" value="${orderInfoPssm.province}" id="pre_province"/> 
											<input type="hidden" value="${orderInfoPssm.city}" id="pre_city"/> 
											<input type="hidden" value="${orderInfoPssm.district}" id="pre_district"/> 
											</td>
										</tr>
										<tr>
											<td colspan="3">
												详细地址:
												<span> 
													${orderInfoPssm.ps_address }
												</span>
											</td>
										</tr>
									</table>
								</div>
							</div>
					</div>
					</div>
					</form>
					<c:if test="${query_type eq '2'}">
					<div class="pub_debook_mes  oz mb10_all">
						<p>
							<c:if test="${orderInfo.order_status eq '00' && canOperation!=1}">
							<input type="button" value="出票成功" id="btnModify_12306" onclick="submitInfo('11', this);"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#228B22;cursor:pointer;width:100px;height:30px;"/>
							<input type="button" value="出票失败" id="outTicket_Lose" onclick="submitInfo('12', this)" 
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#BA55D3;cursor:pointer;width:100px;height:30px;"/>
							</c:if>
							
							<c:if test="${orderInfo.order_status eq '11' && canOperation!=1}">
							<input type="button" value="开始配送" id="beginPsOrder" onclick="submitInfo('21', this)" 
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#7FC12B;cursor:pointer;width:100px;height:30px;"/>
							</c:if>
							
							<c:if test="${orderInfo.order_status eq '21' && canOperation!=1}">
							<input type="button" value="配送成功" id="btnModify_12306" onclick="submitInfo('22', this);"
								style="text-align:center;font-weight:bolder;border:none;color:#fff;background-color:#228B22;cursor:pointer;width:100px;height:30px;"/>
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
			</body>
</html>
