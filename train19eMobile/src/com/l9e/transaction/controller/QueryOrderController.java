package com.l9e.transaction.controller;

import java.util.Date;
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
import com.l9e.common.LoginUserInfo;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoPs;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.AmountUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.PageUtil;

/**
 * 预订查询
 * @author zhangjun
 *
 */
@Controller
@RequestMapping("/query")
public class QueryOrderController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(QueryOrderController.class);
	
	private static final int PAGE_SIZE = 10;//每页显示的条数
	
	@Resource
	private OrderService orderService;
	
	/**
	 * 进入查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryIndex.jhtml")
	public String queryIndex(HttpServletRequest request, 
			HttpServletResponse response){
		return "query/orderQuery";
	}
	
	
	/**
	 * 查询订单列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryOrderList.jhtml")
	public void queryOrderList(HttpServletRequest request, 
			HttpServletResponse response){
		String order_id = this.getParam(request, "order_id");
		String out_ticket_billno = this.getParam(request, "out_ticket_billno");
		
		LoginUserInfo loginUser = this.getLoginUser(request);
		String agentId = loginUser.getAgentId();
		
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("order_id", order_id);//订单号
		paramMap.put("agent_id", agentId);//代理商id
		paramMap.put("out_ticket_billno", out_ticket_billno);//12306订单号
		int count = orderService.queryOrderListCount(paramMap);
		
		logger.info("查询出订单" + count + "条");
		//分页
		PageVo page = PageUtil.getInstance().paging(request, PAGE_SIZE, count);
		
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		List<OrderInfo> orderList = orderService.queryOrderList(paramMap);
		
		JSONArray orders = new JSONArray();
		JSONObject orderJson = null;
		String can_refund = null;
		String deadline_ignore = null;
		String is_deadline = null;
		String refund_opt = null;
		String order_status = null;
		for(OrderInfo orderinfo : orderList){
			orderJson = new JSONObject();
			order_status = orderinfo.getOrder_status();
			orderJson.put("order_id", orderinfo.getOrder_id());
			orderJson.put("order_status", order_status);
			orderJson.put("from_station", orderinfo.getFrom_city());
			orderJson.put("arrive_station", orderinfo.getTo_city());
			orderJson.put("from_time", orderinfo.getFrom_time());
			orderJson.put("arrive_time", orderinfo.getTo_time());
			orderJson.put("travel_time", orderinfo.getTravel_time());
			orderJson.put("create_time", orderinfo.getCreate_time());
			orderJson.put("pay_time", nullToEmpty(orderinfo.getPay_time()));
			orderJson.put("out_ticket_time", nullToEmpty(orderinfo.getOut_ticket_time()));
			
			is_deadline = orderinfo.getIs_deadline();
			can_refund = orderinfo.getCan_refund();//是否可以退款（默认可以退 0:不能退 1:能退）
			deadline_ignore = orderinfo.getDeadline_ignore();//1：无视截止时间（发车12小时后为退款截止时间）
			
			refund_opt = "0";//默认不能退票
			//可以退票的条件：1、出票成功 2、可以退票 3、没有达到截止日期 or 人为移除截止限制
			if(TrainConsts.OUT_SUCCESS.equals(order_status)){
				if(StringUtils.isEmpty(can_refund) || "1".equals(can_refund)){
					if("0".equals(is_deadline)){//没有到达截止日期
						refund_opt = "1";
					}else if("1".equals(is_deadline) && "1".equals(deadline_ignore)){//移除截止限制
						refund_opt = "1";
					}
				}
			}
			
			orderJson.put("can_refund", refund_opt);
			orderJson.put("refund_status", nullToEmpty(orderinfo.getRefund_status()));
			orders.add(orderJson);
		}
		
		JSONObject returnJson = new JSONObject();
		returnJson.put("return_code","SUCCESS");
		returnJson.put("message","查询订单列表成功");
		returnJson.put("orders", orders.toString());
		printJson(response,returnJson.toString());
		
	}
	
	/**
	 * 查看订单明细
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryOrderDetail.jhtml")
	public void queryOrderDetail(HttpServletRequest request, 
			HttpServletResponse response){
		String order_id= this.getParam(request, "order_id");
		
		if(StringUtils.isEmpty(order_id)){
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code","PARAM_EMPTY");
			returnJson.put("message","请求的必填参数不能为空！");
			printJson(response,returnJson.toString());
			return;
		}
		OrderInfo orderInfo = orderService.queryOrderInfo(order_id);
		OrderInfoPs orderInfoPs = orderService.queryOrderInfoPs(order_id);
		
		List<Map<String, String>> detailList = orderService.queryOrderDetailList(order_id);
		List<Map<String, String>> rsList = orderService.queryRefundStreamList(order_id);
		this.groupOrderList(order_id, detailList, rsList, false);//查询经过数据处理的车票列表
		
		String orderJson = this.queryOrderInfoJson(orderInfo, orderInfoPs, detailList, rsList, false, null);
		printJson(response, orderJson);
		
	}
	
	/**
	 * 查看订单退款信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryRefundDetail.jhtml")
	public void queryRefundDetail(HttpServletRequest request, 
			HttpServletResponse response){
		String order_id= this.getParam(request, "order_id");
		if(StringUtils.isEmpty(order_id)){
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code","PARAM_EMPTY");
			returnJson.put("message","请求的必填参数不能为空！");
			printJson(response,returnJson.toString());
			return;
		}
		OrderInfo orderInfo = orderService.queryOrderInfo(order_id);
		String order_status = orderInfo.getOrder_status();
		if(StringUtils.isEmpty(order_status) || !TrainConsts.OUT_SUCCESS.equals(order_status)){
			logger.info("订单状态不是出票成功，不能查看退票明细，order_id="+order_id);
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code", "FAIL");
			returnJson.put("message", "订单状态不是出票成功，不能查看退票明细！");
			return;
		}
		
		OrderInfoPs orderInfoPs = orderService.queryOrderInfoPs(order_id);
		
		List<Map<String, String>> detailList = orderService.queryOrderDetailList(order_id);
		List<Map<String, String>> rsList = orderService.queryRefundStreamList(order_id);
		this.groupOrderList(order_id, detailList, rsList, true);//查询经过数据处理的车票列表
		
		//防止表单重复提交
		String token = Math.random() + order_id + System.currentTimeMillis();
		request.getSession().setAttribute("sessionToken", token);
		String orderJson = this.queryOrderInfoJson(orderInfo, orderInfoPs, detailList, rsList, true, token);
		printJson(response, orderJson);
	}
	
	
	/**
	 * 根据订单号查询订单信息(json格式)
	 * @param order_id
	 */
	private String queryOrderInfoJson(OrderInfo orderInfo,OrderInfoPs orderInfoPs, 
				List<Map<String, String>> detailList, List<Map<String, String>> rsList, 
				boolean extRefund, String token){
		JSONObject returnJson = new JSONObject();
		returnJson.put("return_code", "SUCCESS");
		
		if(extRefund){//退票token，防止重复提交
			returnJson.put("message", "查询退票明细成功！");
			returnJson.put("refund_token", token);
		}else{
			returnJson.put("message", "查询订单明细成功！");
		}

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
		
/*		SELECT cp.order_id,cp.cp_id,cp.seat_type,cp.pay_money AS cp_pay_money,CONVERT(cp.buy_money,CHAR) AS cp_buy_money,cp.train_box,cp.seat_no,
		CONVERT(cp.ticket_type,CHAR) AS ticket_type,cp.user_name,CONVERT(cp.ids_type,CHAR) AS ids_type,cp.user_ids,
		bx.bx_id,bx.bx_billno,bx.bx_code,bx.pay_money AS bx_pay_money,p.product_id,p.name AS bx_name
		FROM hc_orderinfo_cp cp
		LEFT JOIN hc_orderinfo_bx bx
		ON cp.order_id=bx.order_id
		AND cp.cp_id=bx.cp_id
		LEFT JOIN hc_productinfo p
		ON bx.product_id=p.product_id
	WHERE cp.order_id=#order_id:VARCHAR#*/
		
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
			
			if(extRefund){
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
				if("1".equals(refund_opt)){
					passenger.put("refund_money", AmountUtil.formatMoney(nullToEmpty(detailMap.get("cp_refund_money"))));
					passenger.put("refund_percent", nullToEmpty(detailMap.get("refund_percent")));
				}else{
					passenger.put("refund_money", "");
					passenger.put("refund_percent", "");
				}
			}
			passengers.add(passenger);
		}
		
//		SELECT rs.order_id,rs.cp_id,rs.refund_type,rs.refund_status,
//		CONVERT(refund_money, CHAR) as refund_money,rs.our_remark,rs.user_remark,
//		DATE_FORMAT(rs.refund_time,'%Y-%m-%d %H:%i:%s') AS refund_time
//		FROM hc_orderinfo_refundstream rs 
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
			List<Map<String, String>> rsList, boolean extRefund){

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
		if(extRefund){
			String isNeedTip = "0";//是否需要上传小票
			//查询退款需要上传小票的时间点 列车发车前3小时
			Map<String, String> timeMap = orderService.querySpecTimeBeforeFrom(order_id);
			String upload_time = timeMap.get("from_time_3");
			//String upload_time = orderService.queryUploadTipTime(order_id);
			if(!StringUtils.isEmpty(upload_time)){
				if(DateUtil.stringToDate(upload_time, "yyyy-MM-dd HH:mm:ss").before(new Date())){
					isNeedTip = "1";
				}
			}
			
			
			/*
			 * ①开车前48小时以上的，退票时收取票价5%的退票费；
			 * ②开车前24小时以上、不足48小时的，退票时收取票价10%的退票费；
			 * ③开车前不足24小时的，退票时收取票价20%退票费。
			 */
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
/*			feePercent = 0.2;
			refund_percent = "20%";*/
			
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
		}

	}
	
	
	/**
	 * 查询代理商最近订单
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryLastestOrderList.jhtml")
	public String queryLastestOrderList(HttpServletRequest request, 
			HttpServletResponse response){
		LoginUserInfo loginUser = this.getLoginUser(request);
		List<OrderInfo> lastestOrderList = null;
		if(loginUser != null && !StringUtils.isEmpty(loginUser.getAgentId())){
			String agentId = loginUser.getAgentId();
		
			Map<String, Object> paramMap = new HashMap<String, Object>(3);
			paramMap.put("agent_id", agentId);//代理商id
			
			lastestOrderList = orderService.queryLastestOrderList(paramMap);
		}
		request.setAttribute("lastestOrderList", lastestOrderList);
		return "common/lastestOrder";
	}
	

}
