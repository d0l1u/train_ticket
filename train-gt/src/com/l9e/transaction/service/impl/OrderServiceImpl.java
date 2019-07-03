package com.l9e.transaction.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jiexun.iface.util.StringUtil;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.dao.OrderDao;
import com.l9e.transaction.dao.ReceiveNotifyDao;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.BookDetailInfo;
import com.l9e.transaction.vo.ExternalLogsVo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoBx;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.transaction.vo.OrderInfoPs;
import com.l9e.transaction.vo.TicketEntrance;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
	private static final Logger logger = Logger.getLogger(OrderServiceImpl.class);

	@Resource
	private OrderDao orderDao;

	@Resource
	ReceiveNotifyDao receiveDao;

	@Resource
	private CommonService commonService;

	@Override
	public String addOrder(OrderInfo orderInfo, List<OrderInfoCp> orderInfoCpList, List<OrderInfoBx> orderInfoBxList,
			Map<String, String> bxfpMap, String book_flow) {

		if ("11".equals(orderInfo.getPay_type()) && "00".equals(book_flow)) {
			orderInfo.setOrder_status(TrainConsts.NO_PAY);
		} else {
			orderInfo.setOrder_status(TrainConsts.PAY_SUCCESS);
		}
		orderInfo.setTicket_num(String.valueOf(orderInfoCpList.size()));
		orderDao.addOrderInfo(orderInfo);// 订单

		for (OrderInfoCp orderInfoCp : orderInfoCpList) {// 车票
			orderDao.addOrderInfoCp(orderInfoCp);
		}

		for (OrderInfoBx orderInfoBx : orderInfoBxList) {// 保险
			orderDao.addOrderInfoBx(orderInfoBx);
		}
		// orderDao.addOrderInfoPs(orderInfoPs);//配送

		if (bxfpMap != null) {
			orderDao.addOrderInfoBxfp(bxfpMap);
		}

		return orderInfo.getOrder_id();
	}

	@Override
	public List<Map<String, String>> queryOrderDetailList(String order_id) {
		return orderDao.queryOrderDetailList(order_id);
	}

	@Override
	public OrderInfo queryOrderInfo(String order_id) {
		return orderDao.queryOrderInfo(order_id);
	}

	@Override
	public List<OrderInfo> queryOrderList(Map<String, Object> paramMap) {
		return orderDao.queryOrderList(paramMap);
	}

	@Override
	public List<BookDetailInfo> queryOrderCpList(String order_id) {
		return orderDao.queryOrderCpList(order_id);
	}

	public List<Map<String, String>> queryCpContactInfo(String orderId) {
		return orderDao.queryCpContactInfo(orderId);
	}

	public Map<String, String> queryOrderContactInfo(String orderId) {
		return orderDao.queryOrderContactInfo(orderId);
	}

	@Override
	public int queryOrderListCount(Map<String, Object> paramMap) {
		return orderDao.queryOrderListCount(paramMap);
	}

	@Override
	public void updateOrderRefund(Map<String, String> map) {
		orderDao.updateOrderRefund(map);
	}

	@Override
	public List<Map<String, String>> queryTimedRefundList() {
		return orderDao.queryTimedRefundList();
	}

	@Override
	public List<Map<String, String>> queryCpInfoList(String order_id) {
		return orderDao.queryCpInfoList(order_id);
	}

	@Override
	public Map<String, String> queryNotifyCpOrderInfo(String order_id) {
		return orderDao.queryNotifyCpOrderInfo(order_id);
	}

	@Override
	public List<Map<String, String>> queryTimedSendList() {
		return orderDao.queryTimedSendList();
	}

	@Override
	public void updateOrderStatus(Map<String, String> map) {
		orderDao.updateOrderStatus(map);
	}

	@Override
	public List<Map<String, String>> queryRefundStreamList(String order_id) {
		return orderDao.queryRefundStreamList(order_id);
	}

	@Override
	public Map<String, String> querySpecTimeBeforeFrom(String order_id) {
		return orderDao.querySpecTimeBeforeFrom(order_id);
	}

	@Override
	public List<String> queryCp_idList(String orderId) {
		return orderDao.queryCp_idList(orderId);
	}

	@Override
	public List<String> queryCp_idByIds(Map<String, String> paramMap) {
		return orderDao.queryCp_idByIds(paramMap);
	}

	@Override
	public String queryOrderStatusById(Map<String, String> paramMap) {
		return orderDao.queryOrderStatusById(paramMap);
	}

	@Override
	public String queryCpOrderinfoStatusById(Map<String, String> paramMap) {
		return orderDao.queryCpOrderinfoStatusById(paramMap);
	}

	@Override
	public void updateOrderStatusById(Map<String, String> paramMap) {
		orderDao.updateOrderStatusById(paramMap);
	}

	@Override
	public int queryOrderBxCount(Map<String, String> paramMap) {
		return orderDao.queryOrderBxCount(paramMap);
	}

	@Override
	public Map<String, String> queryOrderBxInfo(Map<String, String> paramMap) {
		return orderDao.queryOrderBxInfo(paramMap);
	}

	@Override
	public OrderInfoPs queryOrderInfoPs(String orderId) {
		return orderDao.queryOrderInfoPs(orderId);
	}

	@Override
	public Map<String, String> queryOrderInfoByMap(Map<String, String> paramMap) {
		return orderDao.queryOrderInfoByMap(paramMap);
	}

	@Override
	public Map<String, String> queryMerchantInfoByOrderId(String order_id) {
		return orderDao.queryMerchantInfoByOrderId(order_id);
	}

	@Override
	public void updateOrderEopInfo(Map<String, String> map) {
		orderDao.updateOrderEopInfo(map);
	}

	@Override
	public String queryMsgChannel(String orderId) {
		return orderDao.queryMsgChannel(orderId);
	}

	@Override
	public List<String> queryRefund_cp_list(String orderId) {
		return orderDao.queryRefund_cp_list(orderId);
	}

	@Override
	public List<OrderInfoBx> queryBxInfosById(Map<String, String> paramMap) {
		return orderDao.queryBxInfosById(paramMap);
	}

	@Override
	public int updateRefuseOrderStatus(Map<String, String> param) {
		int count = orderDao.updateRefuseOrderStatus(param);
		orderDao.updateRefundOrderRepeatNotify(param);
		if (count != 0) {
			logger.info(param.get("order_id") + "发起重复退款请求，并且上次为拒绝状态,重置于初始状态");
			ExternalLogsVo logs = new ExternalLogsVo();
			logs.setCp_id(param.get("cp_id"));
			logs.setOrder_id(param.get("order_id"));
			logs.setOrder_optlog("对外商户再次发起退款请求[针对已拒绝退款订单置于初始状态]");
			logs.setOpter("ext_app");
			orderDao.insertOrderLogs(logs);
		}
		return count;
	}

	@Override
	public void insertOrderLogs(ExternalLogsVo logs) {
		orderDao.insertOrderLogs(logs);
	}

	@Override
	public Map<String, String> queryFeeModel(String merchant_id) {
		return orderDao.queryFeeModel(merchant_id);
	}

	@Override
	public String queryOrderIdByEop(String eop_order_id) {
		return orderDao.queryOrderIdByEop(eop_order_id);
	}

	@Override
	public void startNotifyPayOrder(String order_id, String pay_money, String pay_type) {
		Map<String, String> map_eop = new HashMap<String, String>();
		OrderInfo order = queryOrderInfo(order_id);
		map_eop.put("eop_order_id", order.getEop_order_id());
		map_eop.put("order_id", order_id);
		map_eop.put("pay_money", pay_money);
		map_eop.put("pay_type", pay_type);
		receiveDao.addEopAndPayNotify(map_eop);
	}

	@Override
	public void addOrderBookNotifyInfo(Map<String, Object> map) {
		orderDao.addOrderBookNotifyInfo(map);
	}

	@Override
	public void updateOrderWaitPayMoney(Map<String, Object> map) {
		orderDao.updateOrderWaitPayMoney(map);
	}

	public void deleteAllOrderInfo(String order_id) {
		orderDao.deleteOrderBxInfo(order_id);
		orderDao.deleteOrderCpInfo(order_id);
		orderDao.deleteOrderInfo(order_id);
	}

	@Override
	public JSONArray updateBookSuccessInfo(String type, String order_id, String merchant_id) {
		JSONArray orderUsers = new JSONArray();
		JSONObject users = null;
		List<Map<String, String>> cpInfoList = queryCpInfoList(order_id);
		double bx_pay_money = 0.0;
		double ticket_pay_money = 0.0;
		for (Map<String, String> cp : cpInfoList) {
			users = new JSONObject();
			String buy_money = cp.get("buy_money") == null ? "0.00" : cp.get("buy_money");
			users.put("amount", buy_money);
			ticket_pay_money += Double.valueOf(buy_money);
			users.put("ids_type", cp.get("ids_type"));
			users.put("ticket_type", cp.get("ticket_type"));
			users.put("user_ids", cp.get("user_ids"));
			users.put("user_name", cp.get("user_name"));
			users.put("train_box", cp.get("train_box"));
			users.put("seat_no", cp.get("seat_no"));
			users.put("seat_type", cp.get("seat_type"));
			// 查询保险信息参数，并拼接保险信息
			Map<String, String> param = new HashMap<String, String>();
			param.put("order_id", order_id);
			param.put("user_ids", cp.get("user_ids"));
			param.put("ids_type", cp.get("ids_type"));
			Map<String, String> returnBx = queryOrderBxInfo(param);
			if (returnBx == null) {
				users.put("bx_price", "");
				users.put("bx_code", "");
				users.put("bx_channel", "");
			} else {
				String bx_money = returnBx.get("pay_money");
				if (StringUtil.isEmpty(bx_money)) {
					users.put("bx_price", "");
				} else {
					users.put("bx_price", bx_money);
					bx_pay_money += Double.valueOf(bx_money);
				}
				if (StringUtil.isEmpty(returnBx.get("bx_code"))) {
					users.put("bx_code", "");
				} else {
					users.put("bx_code", returnBx.get("bx_code"));
				}

				if ("1".equals(returnBx.get("bx_channel"))) {
					users.put("bx_channel", "快保");
				} else if ("2".equals(returnBx.get("bx_channel"))) {
					users.put("bx_channel", "合众");
				} else {
					users.put("bx_channel", "");
				}
			}
			orderUsers.add(users);
		}
		if ("query".equals(type)) {
			return orderUsers;
		} else if ("update".equals(type)) {
			// 订单异步通知商户参数
			Map<String, String> merchantInfo = commonService.queryMerchantInfo(merchant_id);
			Double merchant_money = getTotalPay(bx_pay_money, cpInfoList.size(), merchantInfo, ticket_pay_money);
			// 更新对外商户订单表需要支付金额
			Map<String, Object> map_money = new HashMap<String, Object>();
			map_money.put("order_id", order_id);
			map_money.put("pay_money", merchant_money);
			map_money.put("ticket_pay_money", String.valueOf(ticket_pay_money));
			map_money.put("pay_money_show",
					String.valueOf(ticket_pay_money + getActurlBxPay(bx_pay_money, cpInfoList.size(), merchantInfo)));
			updateOrderWaitPayMoney(map_money);
			return null;
		}
		return orderUsers;
	}

	// 去除无关字段，高铁
	@Override
	public JSONArray updateBookSuccessInfoGt(String type, String order_id, String merchant_id, String orderId12306) {
		JSONArray orderUsers = new JSONArray();
		JSONObject users = null;
		List<Map<String, String>> cpInfoList = queryCpInfoList(order_id);
		for (Map<String, String> cp : cpInfoList) {
			users = new JSONObject();
			String buy_money = cp.get("buy_money") == null ? "0.00" : cp.get("buy_money");
			users.put("ticket_id", cp.get("cp_id"));// 票号
			users.put("ticket_price", buy_money); // 票价

			HashMap<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("cp_id", cp.get("cp_id"));
			paramMap.put("order_id", order_id);
			String sub_order_12306 = orderDao.selectSubOrder12306(paramMap);// 从出票系统查询12306子订单号
			logger.info(order_id + ",12306子订单号：" + sub_order_12306);

			users.put("sub_order_12306", StringUtil.isEmpty(sub_order_12306) ? "" : sub_order_12306);// 子12306订单号
			users.put("seat_type", cp.get("seat_type"));// 座位类型code
			users.put("seat_type_name", TrainConsts.getSeatType().get(cp.get("seat_no")));// 座位类型名称
			users.put("seat_name", cp.get("seat_no"));// 座位号
			users.put("coach_name", cp.get("train_box"));// 车厢号
			orderUsers.add(users);
		}
		return orderUsers;
	}

	// 获取订单需要支付的总金额
	private Double getTotalPay(double bx_total_pay, int size, Map<String, String> map, double ticketPayMoney) {
		double ticket_total_pay = 0;// 票价总计(包括手续费)
		Map<String, String> fee_map = queryFeeModel(map.get("merchant_id"));
		String fee_type = fee_map.get("fee_type");
		if ("order".equals(fee_type)) {
			ticket_total_pay = Double.valueOf(fee_map.get("order_fee")) + ticketPayMoney;
		} else if ("ticket".equals(fee_type)) {
			ticket_total_pay = Double.valueOf(fee_map.get("ticket_fee")) * size + ticketPayMoney;
		} else if ("percent".equals(fee_type)) {
			ticket_total_pay = ticketPayMoney * Double.valueOf(fee_map.get("percent_fee"));
		} else if ("echelon1".equals(fee_type)) {
			String[] echelon_fir = map.get("echelon_fir").split(":");
			if (map.get("ticket_num").compareTo(echelon_fir[0]) > 0) {
				ticket_total_pay = Double.valueOf(echelon_fir[1]) * size + ticketPayMoney;
			} else {
				ticket_total_pay = Double.valueOf(fee_map.get("echelon_zeo")) * size + ticketPayMoney;
			}
		} else if ("echelon2".equals(fee_type)) {
			String[] echelon_fir = fee_map.get("echelon_fir").split(":");
			String[] echelon_sec = fee_map.get("echelon_sec").split(":");
			if (map.get("ticket_num").compareTo(echelon_fir[0]) <= 0) {
				ticket_total_pay = Double.valueOf(fee_map.get("echelon_zeo")) * size + ticketPayMoney;
			} else if (map.get("ticket_num").compareTo(echelon_sec[0]) > 0) {
				ticket_total_pay = Double.valueOf(echelon_sec[1]) * size + ticketPayMoney;
			} else {
				ticket_total_pay = Double.valueOf(echelon_fir[1]) * size + ticketPayMoney;
			}
		} else {
			ticket_total_pay = ticketPayMoney;
		}
		return ticket_total_pay + getActurlBxPay(bx_total_pay, size, map);
	}

	private Double getActurlBxPay(double bx_total_pay, int size, Map<String, String> map) {
		if (bx_total_pay != 0) {
			if (bx_total_pay / size == 10) {
				bx_total_pay = Double.valueOf(map.get("bx_10_fee")) * size;
			} else if (bx_total_pay / size == 20) {
				bx_total_pay = Double.valueOf(map.get("bx_20_fee")) * size;
			}
		}
		return bx_total_pay;
	}

	@Override
	public String queryRefundStreamSeq(Map<String, String> param) {
		return orderDao.queryRefundStreamSeq(param);
	}

	@Override
	public Map<String, String> queryCpSizeAndPrice(String order_id) {
		return orderDao.queryCpSizeAndPrice(order_id);
	}

	@Override
	public Map<String, String> queryRefundCpOrderInfo(Map<String, String> param) {
		return orderDao.queryRefundCpOrderInfo(param);
	}

	@Override
	public void updateCPAlterInfo(Map<String, Object> map) {
		orderDao.updateCPAlterInfo(map);
	}

	@Override
	public void updateOrderRefundStatus(Map<String, String> map) {
		orderDao.updateOrderRefundStatus(map);
	}

	@Override
	public void updateRefundInfo(Map<String, Object> map) {
		orderDao.updateRefundInfo(map);
	}

	@Override
	public Map<String, Object> queryAccountOrderInfo(Map<String, String> param) {
		return orderDao.queryAccountOrderInfo(param);
	}

	@Override
	public List<Map<String, String>> queryCanRefundStreamList() {
		return orderDao.queryCanRefundStreamList();
	}

	// 同意退款
	@Override
	public void updateRefundAgree(Map<String, Object> map, Map<String, String> paramMap) {
		orderDao.updateRefundInfo(map);// 更新ext_orderinfo_refundstream表的信息（同意退款）
		orderDao.updateCPOrderInfo(map);// 更新ext_orderinfo_cp表的信息
		String refund_seq = orderDao.queryRefundStreamSeq(paramMap);
		int count = orderDao.queryRefundCount(refund_seq);// 根据refund_seq查询ext_orderinfo_refundstream有几条退款记录
		if (count <= 0) {
			map.put("refund_seq", refund_seq);
			try {
				orderDao.updateExtRefundNotifyStatus(map);// 更新ext_orderinfo_refundnotify表的信息（同意退款）
				logger.info("更新退款表成功refund_seq=" + refund_seq);
			} catch (Exception e) {
				logger.error("更新退款失败refund_seq=" + refund_seq);
			}

		}
	}

	// 拒绝退款
	@Override
	public void updateRefundRefuse(Map<String, Object> map, Map<String, String> paramMap) {
		orderDao.updateRefundInfo(map);
		orderDao.updateCPOrderInfo(map);// 更新ext_orderinfo_cp表的信息
		String refund_seq = orderDao.queryRefundStreamSeq(paramMap);
		int count = orderDao.queryRefundCount(refund_seq);// 根据refund_seq查询ext_orderinfo_refundstream有几条退款记录
		if (count <= 0) {
			map.put("refund_seq", refund_seq);
			orderDao.updateExtRefundNotifyStatus(map);// 更新ext_orderinfo_refundnotify表的信息（同意退款）
		}
	}

	@Override
	public int queryHistoryByOrderId(String orderId) {
		return orderDao.queryHistoryByOrderId(orderId);
	}

	@Override
	public OrderInfoCp queryCpInfoByCpId(String cpId) {
		return orderDao.queryCpInfoByCpId(cpId);
	}

	@Override
	public String selectOrderLog(Map<String, Object> map) {
		return orderDao.selectOrderLog(map);
	}

	// 查询自己的订单号，根据高铁的订单号
	@Override
	public String queryOrderId(Map<String, Object> paramMap) {
		return orderDao.queryOrderId(paramMap);
	}

	@Override
	public int updateOrderReqtoken(Map<String, Object> paramMap) {
		return orderDao.updateOrderReqtoken(paramMap);
	}

	@Override
	public int queryEopandpaynotifyCount(String supplierOrderId) {
		return orderDao.queryEopandpaynotifyCount(supplierOrderId);
	}

	@Override
	public int updateOrderConfirmNotifyUrl(Map<String, Object> paraMap1) {
		return orderDao.updateOrderConfirmNotifyUrl(paraMap1);
	}

	@Override
	public String selectSubOrder12306(HashMap<String, String> paramMap) {
		return orderDao.selectSubOrder12306(paramMap);
	}

	@Override
	public List<TicketEntrance> selectOrderTicketEntrance(String order_id) {
		return orderDao.selectOrderTicketEntrance(order_id);
	}

	@Override
	public Date queryPayLimitTime(String order_id) {
		return orderDao.queryPayLimitTime(order_id);
	}

}
