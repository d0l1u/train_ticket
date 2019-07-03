package com.l9e.transaction.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoPs;
import com.l9e.util.AmountUtil;

@Controller
@RequestMapping("/search")
public class NewQueryOrderController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(NewQueryOrderController.class);
	
	@Resource
	private OrderService orderService;
	/**
	 * 无登陆查询订单详情
	 * @param request
	 * @param response
	 */
	@RequestMapping("/queryOrderInfo_no.jhtml")
	public void queryOrderInfoNoSession(HttpServletRequest request, 
			HttpServletResponse response){
		String order_id= this.getParam(request, "order_id");
		String agentId = this.getParam(request, "agentId");
		logger.info("查看订单详情order_id="+order_id+"&agentId="+agentId);
		
		if(StringUtils.isEmpty(order_id)
				|| StringUtils.isEmpty(agentId)){
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code","PARAM_EMPTY");
			returnJson.put("message","请求的必填参数不能为空！");
			printJson(response,returnJson.toString());
			return;
		}
		
		//校验订单号和代理商是否匹配
		Map<String, String> paramMap = new HashMap<String, String>(2);
		paramMap.put("order_id", order_id);
		paramMap.put("agentId", agentId);
		OrderInfo orderInfo = orderService.queryOrderInfoWithAgentId(paramMap);
		if(orderInfo == null){
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code","NO_MATCH_ORDER");
			returnJson.put("message","订单和代理商ID不匹配！");
			printJson(response,returnJson.toString());
			return;
		}
		try{
			OrderInfoPs orderInfoPs = orderService.queryOrderInfoPs(order_id);
			List<Map<String, String>> detailList = orderService.queryOrderDetailList(order_id);
			List<Map<String, String>> rsList = orderService.queryRefundStreamList(order_id);
			this.groupOrderList(order_id, detailList, rsList);//查询经过数据处理的车票列表
			
			//防止表单重复提交
			String token = Math.random() + order_id + System.currentTimeMillis();
			request.getSession().setAttribute("sessionToken", token);
			String orderJson = this.queryOrderInfoJson(orderInfo, orderInfoPs, detailList, rsList, token);
			printJson(response, orderJson);
		}catch(Exception e){
			logger.error("查询订单异常！", e);
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code","SYS_ERROR");
			returnJson.put("message","查询订单异常！");
			printJson(response,returnJson.toString());
			return;
		}
	}
	
	
	/**
	 * 根据订单号查询订单信息(json格式)
	 * @param order_id
	 */
	private String queryOrderInfoJson(OrderInfo orderInfo,OrderInfoPs orderInfoPs, 
				List<Map<String, String>> detailList, List<Map<String, String>> rsList, 
				String token){
		JSONObject returnJson = new JSONObject();
		returnJson.put("return_code", "SUCCESS");
		
		returnJson.put("message", "查询订单详情成功！");
		returnJson.put("refund_token", token);

		returnJson.put("order_id", orderInfo.getOrder_id());
		returnJson.put("order_status", orderInfo.getOrder_status());
		returnJson.put("train_no", orderInfo.getTrain_no());
		returnJson.put("trainTypeCn", this.getTrainTypeCn(orderInfo.getTrain_no()));
		returnJson.put("from_station", orderInfo.getFrom_city());
		returnJson.put("arrive_station", orderInfo.getTo_city());
		returnJson.put("from_time", orderInfo.getFrom_time());
		returnJson.put("arrive_time", orderInfo.getTo_time());
		returnJson.put("travel_time", orderInfo.getTravel_time());
		returnJson.put("link_name", orderInfoPs.getLink_name());
		returnJson.put("link_phone", orderInfoPs.getLink_phone());
		
		if(orderInfo!=null && !StringUtils.isEmpty(orderInfo.getExt_seat())
				&& orderInfo.getExt_seat().indexOf(TrainConsts.SEAT_9)!=-1){//备选无座
			returnJson.put("wz_ext", "1");
		}else{
			returnJson.put("wz_ext", "0");
		}
		
		Double totalPay4Show = AmountUtil.add(Double.parseDouble(orderInfo.getTicket_pay_money()), 
				Double.parseDouble(orderInfo.getBx_pay_money()));
		totalPay4Show = AmountUtil.add(totalPay4Show, 
				Double.parseDouble(orderInfo.getPs_pay_money()));
		
		returnJson.put("total_pay", AmountUtil.formatMoney(orderInfo.getPay_money()));
		returnJson.put("total_pay_show", AmountUtil.formatMoney(totalPay4Show));
		returnJson.put("ticket_pay_money", AmountUtil.formatMoney(orderInfo.getTicket_pay_money()));
		returnJson.put("ticket_buy_money", AmountUtil.formatMoney(nullToEmpty(orderInfo.getBuy_money())));
		returnJson.put("bx_pay_money", AmountUtil.formatMoney(orderInfo.getBx_pay_money()));
		returnJson.put("ps_pay_money", AmountUtil.formatMoney(orderInfo.getPs_pay_money()));
		returnJson.put("seat_type", orderInfo.getSeat_type());
		returnJson.put("out_ticket_type", orderInfo.getOut_ticket_type());
		returnJson.put("out_ticket_billno", nullToEmpty(orderInfo.getOut_ticket_billno()));
		
		
		JSONArray passengers = new JSONArray();
		JSONObject passenger = null;
		String bx_product_id = null;
		String order_status = orderInfo.getOrder_status();
		String deadline_ignore = orderInfo.getDeadline_ignore();
		String is_deadline = orderInfo.getIs_deadline();
		String can_refund = orderInfo.getCan_refund();
		String refund_status = null;
		String refund_opt = null;
		for(Map<String, String> detailMap : detailList){
			passenger = new JSONObject();
			refund_status = nullToEmpty(detailMap.get("refund_status"));
			
			passenger.put("cp_id", detailMap.get("cp_id"));
			passenger.put("user_name", detailMap.get("user_name"));
			passenger.put("ids_type", detailMap.get("ids_type"));
			passenger.put("user_ids", detailMap.get("user_ids"));
			passenger.put("seat_type", detailMap.get("seat_type"));
			passenger.put("pay_money", AmountUtil.formatMoney(detailMap.get("cp_pay_money")));
			passenger.put("buy_money", AmountUtil.formatMoney(nullToEmpty(detailMap.get("cp_buy_money"))));
			passenger.put("train_box", nullToEmpty(detailMap.get("train_box")));
			passenger.put("seat_no", nullToEmpty(detailMap.get("seat_no")));
			passenger.put("bx_billno", nullToEmpty(detailMap.get("bx_billno")));
			passenger.put("refund_status", refund_status);
			passenger.put("refuse_reason", nullToEmpty(detailMap.get("our_remark")));
			
			bx_product_id = detailMap.get("product_id");
			if(StringUtils.isEmpty(bx_product_id)){
				passenger.put("bx_product_id", "BX_NO");
				passenger.put("bx_product_name", "不购买保险");
			}else if("BX_10".equalsIgnoreCase(bx_product_id)){//10元保险
				passenger.put("bx_product_id", "BX_10");
				passenger.put("bx_product_name", detailMap.get("bx_name"));
			}else if("BX_20".equalsIgnoreCase(bx_product_id)){//20元保险
				passenger.put("bx_product_id", "BX_20");
				passenger.put("bx_product_name", detailMap.get("bx_name"));
			}else{//不购买保险
				passenger.put("bx_product_id", "BX_NO");
				passenger.put("bx_product_name", "不购买保险");
			}
			
			refund_opt = "0";//默认不能退票
			//可以退票的条件：1、出票成功 2、可以退票 3、没有达到截止日期或者人为移除截止限制 4、没有发生过退票或者拒绝退票
			if(TrainConsts.OUT_SUCCESS.equals(order_status)){
				if(StringUtils.isEmpty(can_refund) || "1".equals(can_refund)){
					if("0".equals(is_deadline) || ("1".equals(is_deadline) && "1".equals(deadline_ignore))){
						if(StringUtils.isEmpty(refund_status) || TrainConsts.REFUND_STREAM_REFUSE.equals(refund_status))
						refund_opt = "1";
					}
				}
			}
			passenger.put("can_refund", refund_opt);//是否可以退改票 
			passengers.add(passenger);
		}
		
		JSONArray refunds = new JSONArray();
		JSONObject refundJson = null;
		for(Map<String, String> rs : rsList){
			refundJson = new JSONObject();
			refundJson.put("refund_type", rs.get("refund_type"));
			refundJson.put("refund_money", AmountUtil.formatMoney(rs.get("refund_money")));
			refundJson.put("out_fail_reason", nullToEmpty(rs.get("user_remark")));
			refundJson.put("our_remark", nullToEmpty(rs.get("our_remark")));
			refundJson.put("refund_time", nullToEmpty(rs.get("refund_time")));
			refunds.add(refundJson);
		}
		returnJson.put("passengers", passengers.toString());
		returnJson.put("refunds", refunds.toString());
		
		return returnJson.toString();
	}
	
	/**
	 * 查询经过数据处理的车票列表
	 * @param request
	 * @param order_id
	 * @param extRefund 是否附加退款信息计算
	 */
	private void groupOrderList(String order_id, List<Map<String, String>> detailList, 
			List<Map<String, String>> rsList){

		if(rsList != null && rsList.size()>0){
			for(Map<String, String> rsMap : rsList){
				if(StringUtils.isEmpty(rsMap.get("refund_type"))){
					continue;
				}else if(TrainConsts.REFUND_TYPE_1.equals(rsMap.get("refund_type"))){//用户退款
					for(Map<String, String> detailMap : detailList){
						if(detailMap.get("order_id").equals(rsMap.get("order_id"))
							&& detailMap.get("cp_id").equals(rsMap.get("cp_id"))){
							detailMap.put("refund_status", rsMap.get("refund_status"));
							//拒绝退款则写入退款原因
							if(!StringUtils.isEmpty(rsMap.get("refund_status"))
									&& TrainConsts.REFUND_STREAM_REFUSE.equals(rsMap.get("refund_status"))){
								detailMap.put("our_remark", rsMap.get("our_remark"));
							}
							break;
						}
					}
				}else if(TrainConsts.REFUND_TYPE_3.equals(rsMap.get("refund_type"))){//出票失败退款
					for(Map<String, String> detailMap : detailList){
						detailMap.put("refund_status", rsMap.get("refund_status"));
					}
					break;
				}
			}
		}
		/*if(extRefund){
			//查询退款需要上传小票的时间点 列车发车前3小时
			Map<String, String> timeMap = orderService.querySpecTimeBeforeFrom(order_id);
			
			
			 * ①开车前48小时以上的，退票时收取票价5%的退票费；
			 * ②开车前24小时以上、不足48小时的，退票时收取票价10%的退票费；
			 * ③开车前不足24小时的，退票时收取票价20%退票费。
			 
			String refund_percent = null;
			double feePercent = 0;
			if(new Date().before(DateUtil.stringToDate(timeMap.get("from_time_48"), "yyyy-MM-dd HH:mm:ss"))){
				feePercent = 0.05;
				refund_percent = "5%";
			}else if(new Date().before(DateUtil.stringToDate(timeMap.get("from_time_24"), "yyyy-MM-dd HH:mm:ss"))){
				feePercent = 0.1;
				refund_percent = "10%";
			}else{
				feePercent = 0.2;
				refund_percent = "20%";
			}
			
			//计算退票金额
			double refund_money = 0;
			for(Map<String, String> detailMap : detailList){
				double buy_money = Double.parseDouble(detailMap.get("cp_buy_money"));
				double sxf = AmountUtil.quarterConvert(AmountUtil.mul(buy_money, feePercent));//手续费
				sxf = sxf > 2 ? sxf : 2;
				refund_money = AmountUtil.ceil(AmountUtil.sub(buy_money, sxf));
				detailMap.put("cp_refund_money", String.valueOf(refund_money));
				detailMap.put("refund_percent", refund_percent);
			}
		}*/

	}
	
}
