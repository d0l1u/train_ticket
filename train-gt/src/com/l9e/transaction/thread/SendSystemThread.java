package com.l9e.transaction.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.ReceiveNotifyService;
import com.l9e.transaction.vo.ExternalLogsVo;
import com.l9e.util.HttpUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.UrlFormatUtil;

/**
 * @ClassName: SendThread
 * @Description: TODO
 * @author: taoka
 * @date: 2018年1月9日 下午3:16:59
 * @Copyright: 2018 www.19e.cn Inc. All rights reserved.
 */
@Component("sendSystemThread")
@Scope("prototype")
public class SendSystemThread implements Runnable {

	private static final Logger logger = Logger.getLogger(SendSystemThread.class);

	@Resource
	private OrderService orderService;

	@Resource
	protected CommonService commonService;

	@Resource
	private ReceiveNotifyService receiveService;

	private Map<String, String> sendMap;

	@Override
	public void run() {
		Random random = new Random();
		String uuid = "[";
		for (int i = 0; i < 6; i++) {
			uuid = uuid + random.nextInt(9);
		}
		uuid = uuid + "] ";

		ExternalLogsVo logs = new ExternalLogsVo();
		String orderId = sendMap.get("order_id");
		logs.setOrder_id(orderId);

		// 根据ORDER ID 查询商户ID
		logger.info(uuid + "根据ORDER ID:" + orderId + ",查询商户ID");
		String merchantId = receiveService.queryMerchantIdByOrderId(orderId);

		// 根据商户ID 查询商户配置
		logger.info(uuid + "根据商户ID:" + merchantId + ",查询商户配置");
		Map<String, String> merchantSetting = commonService.queryMerchantInfo(merchantId);

		String bookFlow = merchantSetting.get("book_flow");
		String payType = sendMap.get("pay_type");
		String payMoney = sendMap.get("pay_money");

		logger.info(uuid + "bookFlow:" + bookFlow + ", payType:" + payType + ", payMoney:" + payMoney);

		// 11:代扣（钱包）；22：自行扣费
		// 发货完成则通知出票系统开始支付出票
		if ("22".equals(payType)) {
			logger.info(uuid + "通知出票系统开始支付订单:" + orderId + ", 金额:" + payMoney);
			String returnStr = "";
			try {
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("order_id", orderId);
				maps.put("pay_money", payMoney);
				String reqParams = UrlFormatUtil.CreateUrl("", maps, "", "UTF-8");
				String url = getTrainSysSettingValue("notify_pay_interface_url", "ext_notify_pay_interface_url");
				logger.info(uuid + "通知路径:" + url);
				returnStr = HttpUtil.sendByPost(url, reqParams, "utf-8");
				logger.info(uuid + orderId + ",出票系统支付,返回结果:" + returnStr);
			} catch (Exception e) {
				logger.info(uuid + orderId + ",通知出票系统开始支付订单失败！-1" + e.getClass().getSimpleName(), e);
				logs.setOrder_optlog("通知出票系统开始支付订单失败！" + e.getClass().getSimpleName());
			}

			if ("success".equals(returnStr)) {
				logger.info(uuid + orderId + ",通知出票系统开始支付订单成功！");
				logs.setOrder_optlog("通知出票系统开始支付订单成功！");
				logs.setOpter("gt_app");
				receiveService.updateEopAndPayNotifyFinish(sendMap);
			} else {
				logger.info(uuid + orderId + ",通知出票系统开始支付订单失败！-2");
				logs.setOrder_optlog("通知平台发货完成，通知出票系统开始支付订单失败！");
				logs.setOpter("gt_app");
				String notifyNum = sendMap.get("notify_nums");
				if (StringUtils.isBlank(notifyNum)) {
					notifyNum = "0";
				}
				logger.info(uuid + "notifyNum:" + notifyNum);
				if (Integer.valueOf(notifyNum).intValue() >= 5) {
					sendMap.put("notify_status", "33");
				} else {
					sendMap.put("notify_status", "11");
				}
				receiveService.updateEopAndPayNotifyNums(sendMap);
			}
		} else {
			logs.setOrder_optlog("通知平台发货失败，通知出票系统开始支付订单失败！");
			logs.setOpter("gt_app");
			if ("5".equals(sendMap.get("notify_nums"))) {
				sendMap.put("notify_status", "33");
			} else {
				sendMap.put("notify_status", "11");
			}
			receiveService.updateEopAndPayNotifyNums(sendMap);
		}
		orderService.insertOrderLogs(logs);
	}

	public Map<String, String> getSendMap() {
		return sendMap;
	}

	public void setSendMap(Map<String, String> sendMap) {
		this.sendMap = sendMap;
	}

	/**
	 * 获取系统配置参数
	 * 
	 * @author: taoka
	 * @date: 2018年1月10日 上午11:40:59
	 * @param settingName
	 * @param key
	 * @return String
	 */
	private String getTrainSysSettingValue(String settingName, String key) {
		String value = null;
		if (null == MemcachedUtil.getInstance().getAttribute(key)) {
			value = commonService.getTrainSysSettingValue(settingName);
			MemcachedUtil.getInstance().setAttribute(key, value, 2 * 60 * 1000);
		} else {
			value = (String) MemcachedUtil.getInstance().getAttribute(key);
		}
		return value;
	}
}
