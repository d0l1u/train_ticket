package com.train.system.center.thread;

import com.alibaba.fastjson.JSONObject;
import com.train.system.center.em.OrderStatus;
import com.train.system.center.entity.Order;
import com.train.system.center.jedis.JedisClient;
import com.train.system.center.service.HistoryService;
import com.train.system.center.service.NotifyService;
import com.train.system.center.service.OrderService;
import com.train.system.center.util.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * PayThread
 *
 * @author taokai3
 * @date 2018/7/9
 */
@Component("payThread")
@Scope("prototype")
public class PayThread extends BaseThread implements Runnable {

	private Logger logger = LoggerFactory.getLogger(PayThread.class);
	private List<Order> orderList;
	private static final String LOCK_KEY_PREFFIX = "PAY_LOCK_";

	@Resource
	private JedisClient privateLockJedisClient;

	@Resource
	private OrderService orderService;

	@Resource
	private HistoryService historyService;

	@Value("${hthy.confirm}")
	private String confirmUrl;

	@Override
	public void run() {

		for (int i = 0; i < orderList.size(); i++) {
			// 创建日志编号，便于定位日志问题
			logid = String.valueOf(System.nanoTime());
			logid = "[" + logid.substring(logid.length() - 5) + "] ";

			Order order = orderList.get(i);
			String myOrderId = order.getMyOrderId();
			String supplierOrderId = order.getSupplierOrderId();
			String orderId = order.getOrderId();

			// 订单加锁
			String lockKey = LOCK_KEY_PREFFIX + orderId;
			String lockValue = String.valueOf(System.nanoTime());
			String lockResult = lock(privateLockJedisClient, lockKey, lockValue, 60 * 3);
			if (!"OK".equals(lockResult)) {
				logger.info("{}加锁失败,处理下一个订单", logid);
				continue;
			}
			logger.info("{}分销商ID:{},我方ID:{},供货商ID:{}", logid, orderId, myOrderId, supplierOrderId);
			String orderStatus = order.getOrderStatus();
			logger.info("{}system确认出票,当前状态:{}",logid,orderStatus);
			historyService.insertBookingHistory(orderId, "system确认出票,当前状态:" + orderStatus, "pay");

			// 更新为支付中
			order.setOrderStatus(OrderStatus.PAY_INT);
			int updateResult = orderService.updateOrder(order, orderStatus);
			logger.info("{}更新状态结果:{}", logid, updateResult);
			//重置前置条件
			orderStatus = OrderStatus.PAY_INT;

			String supplierType = order.getSupplierType();
			logger.info("{}供货商:{} [00:12306, 01:航天华有]", logid, supplierType);

			HttpUtil httpUtil = new HttpUtil();
			if ("01".equals(supplierType)) {
				historyService.insertBookingHistory(orderId, "航天华有确认出票", "pay");
				// 调用hthy-interface进行取消
				String url = confirmUrl + "/" + myOrderId + "/" + supplierOrderId;
				logger.info("{}hthy-interface确认出票地址:{}", logid, url);
				String httpResult = httpUtil.doHttpGet(url, 1000 * 60);
				// {"orderid":"test201807090754","code":"100","msg":"取消订单成功","success":true}
				logger.info("{}请求结果:{}", logid, httpResult);

				if (StringUtils.isBlank(httpResult) || httpResult.equals("EXCEPTION")) {
					logger.info("{}请求确认出票失败,重置订单状态为人工支付:{}", logid, OrderStatus.PAY_MANUAL);
					historyService.insertBookingHistory(orderId, "请求确认出票失败,接口异常", "pay");
					order.setOrderStatus(OrderStatus.PAY_MANUAL);
				} else {
					JSONObject httpJson = JSONObject.parseObject(httpResult);
					// String myOrderId = httpJson.getString("orderId");
					String code = httpJson.getString("code");
					String msg = httpJson.getString("msg");
					Boolean success = httpJson.getBoolean("success");
					if ("100".equals(code) && success) {
						logger.info("{}请求确认出票成功，等待回调结果", logid);
						historyService.insertBookingHistory(orderId, "请求确认出票成功,等待出票结果", "pay");
					} else {
						logger.info("{}请求确认出票失败，重置订单状态为:{}", logid, OrderStatus.PAY_MANUAL);
						historyService.insertBookingHistory(orderId, "请求确认出票失败,Msg:" + msg, "pay");
						order.setOrderStatus(OrderStatus.PAY_MANUAL);
					}
				}
			}
			updateResult = orderService.updateOrder(order, orderStatus);
			logger.info("{}请求出票订单更新结果:{}", logid, updateResult);

			// 释放锁
			release(privateLockJedisClient, lockKey, lockValue);
		}
	}

	public List<Order> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Order> orderList) {
		this.orderList = orderList;
	}
}
