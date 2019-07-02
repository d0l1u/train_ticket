package com.train.system.center.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.train.system.center.dao.HistoryDao;
import com.train.system.center.dao.NotifyDao;
import com.train.system.center.dao.PassengerDao;
import com.train.system.center.dao.RefundDao;
import com.train.system.center.em.ChangeStatus;
import com.train.system.center.em.OrderStatus;
import com.train.system.center.entity.Change;
import com.train.system.center.entity.CpCount;
import com.train.system.center.entity.Order;
import com.train.system.center.entity.Passenger;
import com.train.system.center.entity.Refund;
import com.train.system.center.service.ChangeService;
import com.train.system.center.service.HthyService;
import com.train.system.center.service.OrderService;

/**
 * HthyServiceImpl
 *
 * @author taokai3
 * @date 2018/7/9
 */
@Service("hthyService")
public class HthyServiceImpl extends BaseService implements HthyService {

	private Logger logger = LoggerFactory.getLogger(HthyServiceImpl.class);

	@Resource
	private HistoryDao historyDao;

	@Resource
	private NotifyDao notifyDao;

	@Resource
	private OrderService orderService;

	@Resource
	private RefundDao refundDao;

	@Resource
	private ChangeService changeService;

	@Resource
	private PassengerDao passengerDao;

	@Override
	public String order(JSONObject paramJson, String logid) {

		String myOrderId = paramJson.getString("orderid");
		String supplierOrderId = paramJson.getString("transactionid");
		Order order = orderService.selectByMyId(myOrderId);
		String orderId = order.getOrderId();
		logger.info("{}分销商ID:{},我方ID:{},供货商ID:{}", logid, orderId, myOrderId, supplierOrderId);

		order.setSupplierOrderId(supplierOrderId);

		String orderStatus = order.getOrderStatus();
		logger.info("{}当前订单状态:{}", logid, orderStatus);
		Boolean success = paramJson.getBoolean("success");
		Boolean orderSuccess = paramJson.getBoolean("ordersuccess");
		Integer code = paramJson.getInteger("code");
		String msg = paramJson.getString("msg");
		historyDao.insertBookingHistory(orderId,  msg, "system");
		if (!success || !orderSuccess || !code.equals(100)) {
			logger.info("{}占位失败,Msg:{}", logid, msg);
			//占位失败，根据message查询数据库配置
			if(msg.contains("没有余票") || msg.contains("排队失败") || msg.contains("占座超时")){
				order.setOrderStatus(OrderStatus.ORDER_FAIL);
				order.setReturnLog("CC");
				order.setErrorCode("1");
				orderService.updateOrder(order, orderStatus);
				int update = notifyDao.updateBeginByOrderId(orderId);
				logger.info("{}占位通知更新结果:{}", logid, update);
				return "success";
			}else if(msg.contains("行程冲突") || msg.contains("实名制")){
				order.setOrderStatus(OrderStatus.ORDER_FAIL);
				order.setReturnLog("II");
				order.setErrorCode("2");
				orderService.updateOrder(order, orderStatus);
				int update = notifyDao.updateBeginByOrderId(orderId);
				logger.info("{}占位通知更新结果:{}", logid, update);
				return "success";
			}else if(msg.contains("乘客身份信息未通过验证")){
				order.setOrderStatus(OrderStatus.ORDER_FAIL);
				order.setReturnLog("01");
				order.setErrorCode("8");
				orderService.updateOrder(order, orderStatus);
				int update = notifyDao.updateBeginByOrderId(orderId);
				logger.info("{}占位通知更新结果:{}", logid, update);
				return "success";
			}else{
				order.setOrderStatus(OrderStatus.BOOKING_MANUAL);
				orderService.updateOrder(order, orderStatus);
				return "success";
			}
		}

		String sequence = paramJson.getString("ordernumber");
		Date startTime = paramJson.getDate("start_time");
		Date arriveTime = paramJson.getDate("arrive_time");
		BigDecimal totalPrice = paramJson.getBigDecimal("orderamount");
		Date payLimitTime = paramJson.getDate("clear_time");
		logger.info("{}支付截至时间:{}", logid, payLimitTime);
		if(payLimitTime == null){
			payLimitTime = new DateTime().plusMinutes(27).toDate();
		}
		order.setSequence(sequence);
		order.setDepartureTime(startTime);
		order.setArrivalTime(arriveTime);
		order.setTotalPrice(totalPrice);
		order.setPayLimitTime(payLimitTime);

		List<Passenger> passengerList = new ArrayList<>();
		JSONArray jsonArray = paramJson.getJSONArray("passengers");
		for (int i = 0; jsonArray != null && i < jsonArray.size(); i++) {
			JSONObject json = jsonArray.getJSONObject(i);
			Passenger passenger = new Passenger();
			passenger.setPassengerNo(json.getString("passengerid"));
			passenger.setSubSequence(json.getString("ticket_no"));
			String cxin = json.getString("cxin");
			if (StringUtils.isNotBlank(cxin)) {
				String[] cxinArr = cxin.split(",");
				if (cxinArr.length == 1) {
					passenger.setBoxName(cxinArr[0]);
					passenger.setSeatName(cxinArr[0]);
				} else {
					passenger.setBoxName(cxinArr[0]);
					passenger.setSeatName(cxinArr[1]);
				}
			}
			passenger.setPrice(json.getBigDecimal("price"));
			passengerList.add(passenger);
		}
		order.setPassengers(passengerList);

		// 判断是否取消
		if (OrderStatus.CANCEL_INIT.equals(orderStatus)) {
			logger.info("{}订单申请取消,不做任何操作，直接跳过", logid);
			historyDao.insertBookingHistory(orderId, "订单申请取消", "system");
			orderService.updateOrder(order, "");
			return "success";
		} else {
			if (!OrderStatus.BOOKING_INIT.equals(orderStatus) && !OrderStatus.BOOKING_RESEND.equals(orderStatus)
					&& !OrderStatus.BOOKING_QUEUE.equals(orderStatus) && !OrderStatus.BOOKING_ING.equals(orderStatus)
					&& !OrderStatus.BOOKING_MANUAL.equals(orderStatus)) {
				logger.info("{}订单状态不正确,勿要轻举妄动,跳过", logid);
				// 发送邮件通知相关人员 TODO
				historyDao.insertBookingHistory(orderId, "订单状态不正确,勿要轻举妄动:" + orderStatus, "system");
				order.setOrderStatus(OrderStatus.BOOKING_MANUAL);
				orderService.updateOrder(order, "");
				return "success";
			}
		}

		// 判断一人多单
		try {
			String setResult = lock(sequence, orderId, 60 * 30);
			if (!"OK".equals(setResult)) {
				// 判断value是否与订单号一致，如果一致，则默认成功
				String getValue = privateLockJedisClient.get(sequence);
				logger.info("{}{}一人多单查询结果:{}-{}", logid, sequence, orderId, getValue);
				if (!orderId.equals(getValue)) {
					logger.info("{}可能存在一人多单，或者往返票赔款的情况，转入人工处理", logid);
					historyDao.insertBookingHistory(orderId, "可能存在一人多单，或者往返票赔款的情况", "system");
					order.setReturnLog("29");
					order.setOrderStatus(OrderStatus.BOOKING_MANUAL);
					orderService.updateOrder(order, orderStatus);
					return "success";
				}
			}
		} catch (Exception e) {
			logger.info("{}查询一人多单异常", logid, e);
		}

		// 判断是否需要直接支付
		String isPay = order.getIsPay();
		BigDecimal payMoney = order.getPayMoney();
		order.setFinishTime(new Date());
		logger.info("{}isPay:{} [00:占位支付, 11:先占位后支付]", logid, isPay);
		logger.info("{}商户支付金额:{},真实支付金额:{}", logid, payMoney, totalPrice);
		if ("00".equals(isPay) && totalPrice.compareTo(payMoney) > 0) {
			// error_info = 3
			logger.info("{}占位失败，票价不符，转取消流程", logid);
			historyDao.insertBookingHistory(orderId, "占位失败，票价不符，转取消流程", "system");
			order.setErrorCode("3");
			order.setOrderStatus(OrderStatus.CANCEL_START);
			orderService.updateOrder(order, "");
		} else if ("00".equals(isPay)) {
			logger.info("{}占位成功,直接支付", logid);
			historyDao.insertBookingHistory(orderId, "占位成功,直接支付", "system");
			order.setOrderStatus(OrderStatus.PAY_BEGIN);
			orderService.updateOrder(order, "");
		} else {
			logger.info("{}占位成功,等待分销商支付", logid);
			historyDao.insertBookingHistory(orderId, "占位成功,等待分销商支付", "system");
			order.setOrderStatus(OrderStatus.PAY_WAIT);
			orderService.updateOrder(order, orderStatus);

			int update = notifyDao.updateBeginByOrderId(orderId);
			logger.info("{}占位通知更新结果:{}", logid, update);
		}
		return "success";
	}

	@Override
	public String confirm(Map<String, Object> paramMap, String logid) {
		// reqtime=20180709080347534&sign=adcfff68ea8d64ab9be46503a3b08add&orderid=test201807090755&transactionid=T18070992AE99740C1ED04C510AD6E00AC617B41FFF&isSuccess=Y
		String myOrderId = paramMap.get("orderid").toString();
		String supplierOrderId = paramMap.get("transactionid").toString();
		String ticketEntrance = paramMap.get("ticket_entrance").toString();
		logger.info("{}检票口信息:{}", logid, ticketEntrance);
		Order order = orderService.selectByMyId(myOrderId);
		String orderId = order.getOrderId();
		String isSuccess = paramMap.get("isSuccess").toString();
		logger.info("{}出票结果:{},分销商ID:{}, 我方ID:{},供货商ID:{}", logid, isSuccess, orderId, myOrderId, supplierOrderId);
		order.setSupplierOrderId(supplierOrderId);

		String orderStatus = order.getOrderStatus();
		logger.info("{}当前订单状态:{}", logid, orderStatus);

		if ("Y".equals(isSuccess)) {
			logger.info("{}出票回调‘Y’,支付成功,更新检票口信息", logid);
			if (StringUtils.isNotBlank(ticketEntrance)) {
				int insertResult = orderService.insertTicketEntrance(orderId, order.getTrainCode(),
						order.getFromStationName(), ticketEntrance);
				logger.info("{}检票口信息更新结果:{}", logid, insertResult);
			}
			historyDao.insertBookingHistory(orderId, "支付成功", "pay");
			order.setPayTime(new Date());
			order.setOrderStatus(OrderStatus.ORDER_SUCCESS);
		} else {
			logger.info("{}出票回调‘N’,支付失败,转人工支付", logid);
			historyDao.insertBookingHistory(orderId, "支付失败,转人工支付", "pay");
			order.setOrderStatus(OrderStatus.PAY_MANUAL);
		}

		if (order.getOrderStatus().equals(OrderStatus.ORDER_SUCCESS)) {
			int updateResult = notifyDao.updateBeginByOrderId(orderId);
			logger.info("{}通知更新结果:{}", logid, updateResult);
		}

		int updateOrder = orderService.updateOrder(order, orderStatus);
		logger.info("{}出票订单更新结果:{}", logid, updateOrder);
		return "success";
	}

	@Override
	public String refund(JSONObject paramJson, String logid) {
		String myOrderId = paramJson.getString("apiorderid");
		String sequence = paramJson.getString("trainorderid");
		Order order = orderService.selectByMyId(myOrderId);
		String orderId = order.getOrderId();
		logger.info("{}分销商ID:{},我方ID:{},12306订单号:{}", logid, orderId, myOrderId, sequence);

		Boolean returnState = paramJson.getBoolean("returnstate");
		// BigDecimal returnMoney = paramJson.getBigDecimal("returnmoney");
		String returnMsg = paramJson.getString("returnmsg");
		String returnType = paramJson.getString("returntype");

		logger.info("{}returnType:{} [0:线下退款, 1:线上退款, 2:线下改签退款]", logid, returnType);

		JSONArray ticketArray = paramJson.getJSONArray("returntickets");
		if ("1".equals(returnType)) {
			for (int i = 0; i < ticketArray.size(); i++) {

				JSONObject json = ticketArray.getJSONObject(i);
				String subSequence = json.getString("ticket_no");
				BigDecimal price = json.getBigDecimal("returnmoney");
				// Date returnTime = json.getDate("returntime");

				Refund refund = refundDao.queryByOrderIdAndSequence(orderId, subSequence);
				String cpId = refund.getCpId();

				String orderStatus = refund.getOrderStatus();
				logger.info("{}{}当前订单状态:{}", logid, cpId, orderStatus);

				if (returnState != null && returnState) {
					logger.info("{}退票成功", logid);
					refund.setRefundKyfwMoney(price);
					refund.setRefundMoney(price);
					refund.setOrderStatus(OrderStatus.REFUND_AUTO_OVER);
					historyDao.insertRefundHistory(orderId, cpId, "退票成功", "refund");
				} else {
					logger.info("{}退票失败,转人工退票", logid);
					refund.setOrderStatus(OrderStatus.REFUND_MANUAL);
					historyDao.insertRefundHistory(orderId, cpId, "退票失败,转人工退票Msg:" + returnMsg, "refund");
				}

				if (refund.getOrderStatus().equals(OrderStatus.REFUND_AUTO_OVER)) {
					int updateResult = notifyDao.updateRefund(orderId, cpId);
					logger.info("{}退票通知更新结果:{}", logid, updateResult);

					// 更新统计
					CpCount cpCount = new CpCount();
					cpCount.setSource("train_refund_dev");
					cpCount.setChannel(refund.getChannel());
					cpCount.setCode("01");
					cpCount.setMessage("退票成功！");
					cpCount.setType("04");

					// updateResult = cpCountDao.insertCount(cpCount);
					// logger.info("{}退票统计更新结果:{}", logid, updateResult);
				}

				int updateOrder = refundDao.updateById(refund, orderStatus);
				logger.info("{}退票订单更新结果:{}", logid, updateOrder);
			}
		} else {
			logger.info("{}线下退款，记录即可", logid);
			List<Refund> list = new ArrayList<>();
			for (int i = 0; i < ticketArray.size(); i++) {
				JSONObject json = ticketArray.getJSONObject(i);
				String name = json.getString("passengername");
				String cardNo = json.getString("passportseno");
				String subSequence = json.getString("ticket_no");
				BigDecimal price = json.getBigDecimal("returnmoney");
				Date returnTime = json.getDate("returntime");

				/*
				 * 不具体分析 是哪个乘客退票，由客服人员处理 //根据order_id 和 sub_sequence
				 * 查询elong_change_cp表 cpId logger.info("{}查询Change Cp",logid);
				 * Passenger passenger = changeDao.queryCp(orderId,
				 * subSequence); if(passenger == null){ logger.info(
				 * "{}非改签票线下退票，查询Order Cp",logid); passenger =
				 * changeDao.queryCp(orderId, subSequence);
				 * 
				 * if(passenger == null){ logger.info("{}非正常票线下退票，查询Order Cp"
				 * ,logid); passenger = orderService.queryCp(orderId,
				 * subSequence); } }
				 */

				Refund refund = new Refund();
				refund.setName(name);
				refund.setCardNo(cardNo);
				refund.setMyOrderId(myOrderId);
				refund.setOrderId(orderId);
				refund.setRefundKyfwMoney(price);
				refund.setSubSequence(subSequence);
				refund.setSupplierType("01");
				refund.setOrderStatus("00");
				refund.setVerifyTime(returnTime);
				// if(passenger != null){
				// refund.setCpId(passenger.getPassengerNo());
				// }
				list.add(refund);
			}
			int insertResult = refundDao.insertBatchUnder(list);
			logger.info("{}线下退款插入结果:{}", logid, insertResult);
		}
		return "success";
	}

	@Override
	public String changeOrder(JSONObject paramJson, String logid) {
		String myOrderId = paramJson.getString("orderid");
		String reqtoken = paramJson.getString("reqtoken");
		Integer changeId = Integer.valueOf(reqtoken.split("_")[0]);
		logger.info("{}我方ID:{}, reqToken(changeId):{}", logid, myOrderId, changeId);
		Boolean success = paramJson.getBoolean("success");
		String msg = paramJson.getString("msg");
		Date payLimitTime = paramJson.getDate("clear_time");
		logger.info("{}支付截至时间:{}", logid, payLimitTime);

		if(payLimitTime == null){
			payLimitTime = new DateTime().plusMinutes(27).toDate();
		}

		Change change = changeService.queryByChangeId(changeId);
		String orderId = change.getOrderId();
		change.setPayLimitTime(payLimitTime);
		logger.info("{}分销商ID:{}", logid, orderId);

		String changeStatus = change.getChangeStatus();
		logger.info("{}当前订单改签状态:{}", logid, changeStatus);
		if (!ChangeStatus.CHANGE_ING.equals(changeStatus) && !ChangeStatus.CHANGE_MANAUL.equals(changeStatus)) {
			change.setChangeStatus(ChangeStatus.CHANGE_MANAUL);
			logger.info("{}改签占位回调时，前置状态不为改签占位中", logid);
			historyDao.insertChangeOrderHistory(orderId, changeId.toString(), "改签占位回调时，前置状态不为改签占位中", "changeOrder");
			changeService.updateOrder(change, false, changeStatus);
			return "success";
		}

		List<Passenger> passengerList = passengerDao.queryChangePassengers(changeId);
		if (passengerList == null || passengerList.isEmpty()) {
			change.setChangeStatus(ChangeStatus.CHANGE_MANAUL);
			logger.info("{}根据changeId查询passenger为空，可能该请求重复提交", logid);
			historyDao.insertChangeOrderHistory(orderId, changeId.toString(), "passenger为空，可能该请求重复提交", "changeOrder");
			changeService.updateOrder(change, false, changeStatus);
			return "success";
		}

		if (!success) {
			logger.info("{}改签占位失败,Msg:{}", logid, msg);
			change.setChangeStatus(ChangeStatus.CHANGE_MANAUL);
			historyDao.insertChangeOrderHistory(orderId, changeId.toString(), "改签占位失败,Msg:" + msg, "changeOrder");
			changeService.updateOrder(change, false, changeStatus);
			return "success";
		}

		// 改签占位成功
		// BigDecimal priceDifference =
		// paramJson.getBigDecimal("pricedifference");// 差价
		Integer payType = paramJson.getInteger("priceinfotype");// 1低-高，2平价，3低-高
		String fromDateStr = paramJson.getString("train_date");
		JSONArray newtickets = paramJson.getJSONArray("newtickets");
		change.setBookingTime(new Date());
		change.setChangeStatus(ChangeStatus.CHANGE_PAY);
		String fromTimeStr = fromDateStr + " " + paramJson.getString("start_time");
		String[] runtimeArr = paramJson.getString("runtime").split(":");
		int runtime = Integer.valueOf(runtimeArr[0]) * 60 + Integer.valueOf(runtimeArr[1]);
		change.setNewDepartureTime(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(fromTimeStr).toDate());
		change.setNewArrivalTime(
				DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(fromTimeStr).plusMinutes(runtime).toDate());

		// 1：平改 2：高改低 3：低改高
		if (payType == 1) {
			change.setPayType(3);
		} else if (payType == 2) {
			change.setPayType(1);
		} else if (payType == 3) {
			change.setPayType(2);
		}

		for (int i = 0; i < passengerList.size(); i++) {
			Passenger passenger = passengerList.get(i);
			String subSequenceOut = passenger.getSubSequenceOut();
			String cardNo = passenger.getCardNo().replaceAll("\\s", "");

			for (int j = 0; j < newtickets.size(); j++) {
				JSONObject json = newtickets.getJSONObject(j);
				String passportseno = json.getString("passportseno");
				String new_ticket_no = json.getString("new_ticket_no");
				String old_ticket_no = json.getString("old_ticket_no");
				String cxin = json.getString("cxin");
				BigDecimal price = json.getBigDecimal("price");

				if (cardNo.toUpperCase().equals(passportseno.toUpperCase()) && subSequenceOut.equals(old_ticket_no)) {
					passenger.setSubSequence(new_ticket_no);
					passenger.setPrice(price);
					if (StringUtils.isNotBlank(cxin)) {
						String[] cxinArr = cxin.split(",");
						if (cxinArr.length == 1) {
							passenger.setBoxName(cxinArr[0]);
							passenger.setSeatName(cxinArr[0]);
						} else {
							passenger.setBoxName(cxinArr[0]);
							passenger.setSeatName(cxinArr[1]);
						}
					}

					newtickets.remove(j);
					j--;
				}
			}
		}
		historyDao.insertChangeOrderHistory(orderId, changeId.toString(), "改签占位成功,等待分销商支付", "changeOrder");
		change.setPassengerList(passengerList);
		change.setSerialnumber(reqtoken);
		int updateResult = changeService.updateOrder(change, true, changeStatus);
		logger.info("{}改签占位成功更新结果:{}", logid, updateResult);
		// 不用通知，默认通知状态000，当改签状态为14的时候，分销接口会自动通知
		// if(updateResult > 0){
		// //插入通知
		// }
		return "success";
	}

	@Override
	public String changeConfirm(JSONObject paramJson, String logid) {
		String myOrderId = paramJson.getString("orderid");
		String reqtoken = paramJson.getString("reqtoken");
		logger.info("本次回调token:{}", reqtoken);
		Integer changeId = Integer.valueOf(reqtoken.split("_")[0]);
		logger.info("{}我方ID:{}, reqToken(changeId):{}", logid, myOrderId, changeId);
		Boolean success = paramJson.getBoolean("success");
		String msg = paramJson.getString("msg");
		// JSONArray ticketEntranceArray =
		// paramJson.getJSONArray("ticket_entrance");
		String ticketEntranceArray = paramJson.get("ticket_entrance").toString();
		logger.info("{}检票口信息:{}", logid, ticketEntranceArray);

		Change change = changeService.queryByChangeId(changeId);
		String orderId = change.getOrderId();
		logger.info("{}分销商ID:{}", logid, orderId);

		String changeStatus = change.getChangeStatus();
		logger.info("{}当前订单改签状态:{}", logid, changeStatus);
		if (!ChangeStatus.PAY_ING.equals(changeStatus)) {
			change.setChangeStatus(ChangeStatus.PAY_MANAUL);
			logger.info("{}改签支付回调时，前置状态不为改签支付中", logid);
			historyDao.insertChangeOrderHistory(orderId, changeId.toString(), "改签支付回调时，前置状态不为改签支付中", "changePay");
			changeService.updateOrder(change, false, changeStatus);
			return "success";
		}

		if (!success) {
			logger.info("{}改签支付失败,Msg:{}", logid, msg);
			change.setChangeStatus(ChangeStatus.PAY_MANAUL);
			historyDao.insertChangeOrderHistory(orderId, changeId.toString(), "改签支付失败,Msg:" + msg, "changePay");
			changeService.updateOrder(change, false, changeStatus);
			return "success";
		}

		historyDao.insertChangeOrderHistory(orderId, changeId.toString(), "改签支付成功", "changePay");
		change.setChangeStatus(ChangeStatus.PAY_SUCCESS);
		change.setPayTime(new Date());
		int updateResult = changeService.updateOrder(change, true, changeStatus);
		logger.info("{}改签支付成功更新结果:{}", logid, updateResult);
		return "success";
	}

	public static void main(String[] args) {
		DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime("2018-12-27 14:34");

		System.err.println(dateTime);
	}
}
