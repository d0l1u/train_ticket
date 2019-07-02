package com.train.system.center.thread;

import com.alibaba.fastjson.JSONObject;
import com.train.system.center.em.ChangeStatus;
import com.train.system.center.entity.Change;
import com.train.system.center.entity.Passenger;
import com.train.system.center.jedis.JedisClient;
import com.train.system.center.service.ChangeService;
import com.train.system.center.service.HistoryService;
import com.train.system.center.service.PassengerService;
import com.train.system.center.util.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component("changePayThread")
@Scope("prototype")
public class ChangePayThread extends BaseThread implements Runnable{

	private Logger logger = LoggerFactory.getLogger(ChangePayThread.class);
	private List<Change> orderList;
	private static final String LOCK_KEY_PREFFIX = "CHANGE_PAY_LOCK_";

	@Resource
	private JedisClient privateLockJedisClient;

	@Resource
	private ChangeService changeService;

	@Resource
	private PassengerService passengerService;

	@Resource
	private HistoryService historyService;

	@Value("${hthy.changeConfirm}")
	private String changePayUrl;

	@Value("${getAccountById}")
	private String getAccountById;

	@Override
	public void run() {
		for (int i = 0; i < orderList.size(); i++) {
			// 创建日志编号，便于定位日志问题
			logid = String.valueOf(System.nanoTime());
			logid = "[" + logid.substring(logid.length() - 5) + "] ";
			Change change = orderList.get(i);
			String orderId = change.getOrderId();
			Integer changeId = change.getChangeId();
			String lockKey = LOCK_KEY_PREFFIX + orderId;
			String lockValue = String.valueOf(System.nanoTime());
			String lockResult = lock(privateLockJedisClient, lockKey, lockValue, 60 * 1);
			if (!"OK".equals(lockResult)) {
				logger.info("{}加锁失败,处理下一个订单", logid);
				continue;
			}
			String myOrderId = change.getMyOrderId();
			String supplierOrderId = change.getSupplierOrderId();
			logger.info("{}Change ID:{},分销商ID:{},我方ID:{},供货商ID:{}", logid, changeId, orderId, myOrderId,
					supplierOrderId);

			try {
				// 根据changeId查询乘客信息
				List<Passenger> passengerList = passengerService.queryChangePassengers(changeId);
				if (passengerList == null || passengerList.isEmpty()) {
					logger.info("{}该订单可能不是最新的改签订单，忽略此订单", logid);
					// TODO
					continue;
				}
				change.setPassengerList(passengerList);

				historyService.insertChangeOrderHistory(orderId, changeId.toString(), "system改签支付", "changePay");

				// 修改订单状态为改签支付中
				String changeStatus = change.getChangeStatus();
				change.setChangeStatus(ChangeStatus.PAY_ING);
				int updateResult = changeService.updateOrder(change, false, changeStatus);
				logger.info("{}改签支付中更新结果:{}", logid, updateResult);
				if (updateResult == 0) {
					// TODO
					continue;
				}
				changeStatus = ChangeStatus.PAY_ING;
				historyService.insertChangeOrderHistory(orderId, changeId.toString(), "航天华有改签支付", "changePay");
				String reqToken = change.getSerialnumber();
				if(StringUtils.isBlank(reqToken)){
					reqToken = change.toString();
				}
				logger.info("本次请求token:{}", reqToken);
				String url = changePayUrl + "/" + myOrderId + "/" + supplierOrderId + "/" + changeId;
				logger.info("{}hthy-interface改签支付地址:{}", logid, url);
				String httpResult = new HttpUtil().doHttpGet(url, 1000 * 60 * 3);
				logger.info("{}请求改签支付结果:{}", logid, httpResult);

				if (StringUtils.isBlank(httpResult)||httpResult.equals("EXCEPTION")) {
					logger.info("{}请求改签支付失败,重置订单状态为人工支付:{}", logid, ChangeStatus.PAY_MANAUL);
					historyService.insertRefundHistory(orderId, null, "请求改签支付失败,接口异常", "changePay");
					change.setChangeStatus(ChangeStatus.PAY_MANAUL);
				} else {
					JSONObject httpJson = JSONObject.parseObject(httpResult);
					// String transactionid =
					// httpJson.getString("transactionid");
					String msg = httpJson.getString("msg");
					Boolean success = httpJson.getBoolean("success");
					if (success) {
						logger.info("{}请求改签支付成功，等待支付结果", logid);
						historyService.insertChangeOrderHistory(orderId, changeId.toString(), "请求改签支付成功，等待支付结果",
								"changePay");
					} else {
						logger.info("{}请求改签支付失败，重置订单状态为人工支付:{}", logid, ChangeStatus.PAY_MANAUL);
						historyService.insertChangeOrderHistory(orderId, changeId.toString(), "改签支付失败,Msg:" + msg,
								"changePay");
						change.setChangeStatus(ChangeStatus.PAY_MANAUL);
					}
				}

				// 更新订单信息
				updateResult = changeService.updateOrder(change, false, changeStatus);
				logger.info("{}申请改签支付后更新结果:{}", logid, updateResult);
			} catch (Exception e) {
				logger.info("{}改签支付异常:{}", logid, e.getClass().getSimpleName(), e);
			}
		}
	}

	public List<Change> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Change> orderList) {
		this.orderList = orderList;
	}
}
