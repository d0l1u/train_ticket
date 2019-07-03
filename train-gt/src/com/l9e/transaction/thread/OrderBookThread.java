package com.l9e.transaction.thread;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.ReceiveNotifyService;
import com.l9e.transaction.vo.ExternalLogsVo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.HttpsUtil;
import com.l9e.util.Md5Encrypt;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author meizs
 * @create 2018-03-21 11:52 占座结果回调
 **/
@Component("orderBookThread")
@Scope("prototype")
public class OrderBookThread implements Runnable {

	private static final Logger logger = Logger.getLogger(OrderBookThread.class);

	@Resource
	ReceiveNotifyService receiveService;

	@Resource
	private OrderService orderService;

	@Resource
	private CommonService commonService;

	private Map<String, String> map;

	private String timestamp;

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public Map<String, String> getParamMap() {
		return map;
	}

	public void setParamMap(Map<String, String> paramMap) {
		this.map = paramMap;
	}

	@Override
	public void run() {

		String order_id = "";
		long a = System.currentTimeMillis();
		try {
			JSONArray orderUsers = new JSONArray();
			JSONObject json_param = new JSONObject();
			order_id = map.get("order_id");
			logger.info("发起订单预定结果通知！！订单号：" + order_id);
			// 更新订单预定结果通知表通知开始时间和通知次数
			receiveService.updateOrderBookNotifyStartNum(order_id);
			// 添加开始通知日志
			ExternalLogsVo logs = new ExternalLogsVo();
			logs.setOrder_id(order_id);
			logs.setOrder_optlog("发起订单预定结果通知！！订单号：" + order_id);
			logs.setOpter("gt_app");
			orderService.insertOrderLogs(logs);
			// ticketInfo,reqtoken,pay_limit_time
			OrderInfo order = orderService.queryOrderInfo(order_id);
			String status = "";
			if (TrainConsts.OUT_FAIL.equals(order.getOrder_status())
					|| TrainConsts.ORDER_OUT_TIME.equals(order.getOrder_status())) {// 占座失败通知,(超时订单，有时候需要回调占座失败)

				status = "FAILURE";
				String fail_reason = order.getFail_reason();// 占座失败原因
				String fail_code = "";// 失败原因code
				String failReason = "";// 失败原因提示
				logger.info(order_id + ",占座失败订单通知:" + order.getOrder_status());
				json_param.put("reqtoken", StringUtils.isEmpty(order.getReqtoken()) ? "" : order.getReqtoken());
				json_param.put("reqtime", timestamp);
				json_param.put("status", status);
				json_param.put("pay_limit_time", "");// 支付截止时间
				json_param.put("refund_online", 0);
				json_param.put("ticket_price_all", "");// 订单总价格
				json_param.put("supplierOrderId", order_id);
				json_param.put("ticketInfo", orderUsers.toString());
				json_param.put("order12306", "");
				json_param.put("gtgjOrderId", order.getMerchant_order_id());
				json_param.put("supplier", "19e");

				List<Map<String, String>> cpInfoList = orderService.queryCpInfoList(order_id);
				fail_code = TrainConsts.getOutFailReasonCode().get(fail_reason);
				if (StringUtils.isEmpty(fail_code))
					fail_code = TrainConsts.OUT_FAIL_CODE_1011;
				failReason = TrainConsts.getOutFailReason().get(fail_reason);
				if (fail_reason != null && fail_reason.equals("12")) {
					if (cpInfoList.size() == 1) {
						failReason = cpInfoList.get(0).get("user_name") + "涉嫌冒用";
						json_param.put("fail_id", cpInfoList.get(0).get("user_ids"));
						json_param.put("fail_name", cpInfoList.get(0).get("user_name"));
					} else {
						// 信息冒用返回哪个乘客信息冒用 ,从操作日志表中获得
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("orderId", order_id);
						paramMap.put("content", "%冒用%");
						String errorInfoLog = orderService.selectOrderLog(paramMap);
						logger.info("高铁订单处理结果回调，信息冒用，查询日志返回结果为" + errorInfoLog);
						if (errorInfoLog != null) {
							for (Map<String, String> cp1 : cpInfoList) {
								String fail_user_ids = cp1.get("user_ids");
								String fail_user_name = cp1.get("user_name");
								if (errorInfoLog.contains(fail_user_name)) {
									json_param.put("fail_id", fail_user_ids);
									json_param.put("fail_name", fail_user_name);
									failReason = fail_user_name + "涉嫌冒用";
									break;
								}
							}
						}
					}
					logger.info("高铁订单处理结果回调，信息冒用，错误信息为" + failReason);
				}
				if (fail_reason != null && fail_reason.equals("8")) {
					if (cpInfoList.size() == 1) {
						failReason = cpInfoList.get(0).get("user_name") + "尚未通过身份信息核验";
						json_param.put("fail_id", cpInfoList.get(0).get("user_ids"));
						json_param.put("fail_name", cpInfoList.get(0).get("user_name"));
					} else {
						// 身份待核验返回哪个乘客信息待核验 ,从操作日志表中获得
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("orderId", order_id);
						paramMap.put("content", "%尚未通过身份信息核验%");
						String errorInfoLog = orderService.selectOrderLog(paramMap);
						if (errorInfoLog == null || errorInfoLog.equals("")) {
							paramMap.put("content", "%待核验%");
							errorInfoLog = orderService.selectOrderLog(paramMap);
							if (errorInfoLog == null || errorInfoLog.equals("")) {
								paramMap.put("content", "%待审核%");
								errorInfoLog = orderService.selectOrderLog(paramMap);
							}
						}
						logger.info("高铁订单处理结果回调，未通过核验，查询日志返回结果为" + errorInfoLog);
						if (errorInfoLog != null) {
							for (Map<String, String> cp2 : cpInfoList) {
								String fail_user_ids = cp2.get("user_ids");
								String fail_user_name = cp2.get("user_name");
								if (errorInfoLog.contains(fail_user_name)) {
									json_param.put("fail_id", fail_user_ids);
									json_param.put("fail_name", fail_user_name);
									failReason = fail_user_name + "尚未通过身份核验";
									break;
								}
							}
						}
					}
					logger.info("高铁订单处理结果回调，身份待核验，错误信息为" + failReason);
				}
				if (StringUtils.isEmpty(failReason))
					failReason = "其他";

				// 失败原因，转换，完成
				json_param.put("fail_code", fail_code);
				json_param.put("fail_msg", failReason);

				// 占座回调处理开始
				sendBookNotifyAndResultDeal(order_id, json_param, logs);

			} else if (TrainConsts.BOOK_SUCCESS.equals(order.getOrder_status())) {

				orderUsers = orderService.updateBookSuccessInfoGt("query", order_id, map.get("merchant_id"),
						order.getOut_ticket_billno());
				status = "SUCCESS";
				String pay_limit_time = "";

				// 从cp_orderinfo表中查询支付截至时间
				Date date = orderService.queryPayLimitTime(order_id);
				if (date == null) {
					pay_limit_time = DateUtil
							.dateToString(
									DateUtil.dateAddMin(
											DateUtil.stringToDate(order.getOut_ticket_time(), DateUtil.DATE_FMT3), 28),
									DateUtil.DATE_FMT3);// 限制28分钟内支付
				} else {
					pay_limit_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				}

				logger.info(order_id + ",出票占座时间:" + order.getOut_ticket_time() + ",pay_limit_time:" + pay_limit_time);
				json_param.put("reqtoken", StringUtils.isEmpty(order.getReqtoken()) ? "" : order.getReqtoken());
				json_param.put("reqtime", timestamp);
				json_param.put("status", status);
				json_param.put("pay_limit_time", pay_limit_time);// 支付截止时间
				json_param.put("refund_online", 0);
				json_param.put("ticket_price_all", order.getBuy_money());// 订单总价格
				json_param.put("supplierOrderId", order_id);
				json_param.put("ticketInfo", orderUsers.toString());
				json_param.put("order12306", order.getOut_ticket_billno() == null ? "" : order.getOut_ticket_billno());
				json_param.put("gtgjOrderId", order.getMerchant_order_id());
				json_param.put("supplier", "19e");

				// 占座回调处理开始
				sendBookNotifyAndResultDeal(order_id, json_param, logs);

			} else {

				logger.info(order_id + "，订单状态不对," + order.getOrder_status());

			}

		} catch (Exception e) {
			logger.info("OrderBookThread-当前订单处理异常：", e);
		}

		logger.info("发起订单预定结果通知结束，订单号：" + order_id + "耗时：" + (System.currentTimeMillis() - a) / 1000f + "秒");
	}

	/**
	 * 回调占座结果并处理
	 *
	 * @param order_id
	 * @param json_param
	 * @param logs
	 */
	public void sendBookNotifyAndResultDeal(String order_id, JSONObject json_param, ExternalLogsVo logs) {

		String method = "book";
		Map<String, String> merchantInfoMap = orderService.queryMerchantInfoByOrderId(order_id);
		Map<String, String> merchantSetting = commonService.queryMerchantInfo(merchantInfoMap.get("merchant_id"));
		// md5(partnerid+method+reqtime+md5(KEY).toLowerCase()).toLowerCase(),签名规则
		String md5_str = TrainConsts.PARTNER_ID + method + timestamp
				+ Md5Encrypt.getKeyedDigestFor19Pay(merchantSetting.get("sign_key"), "", "utf-8");
		logger.info(order_id + ":" + md5_str + "MD5()" + merchantSetting.get("sign_key"));
		String sign = Md5Encrypt.getKeyedDigestFor19Pay(md5_str, "", "utf-8");
		json_param.put("sign", sign);

		StringBuffer params = new StringBuffer();
		logger.info(order_id + ",占座通知参数：" + json_param.toString());
		logger.info(order_id + "seatdata----");
		try {
			params.append("seatdata=").append(URLEncoder.encode(json_param.toString().replace("+", "%20"), "utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		logger.info(order_id + "订单占座结果异步通知参数：" + params.toString());
		String url = map.get("notify_url");
		logger.info("url:" + url);
		String result = "";
		try {
			if (url.contains("https")) {
				result = HttpsUtil.sendHttps(url + "?" + params.toString());
			} else {
				result = HttpUtil.sendByPost(url, params.toString(), "utf-8");
			}
		} catch (Exception e) {
			logger.info(order_id + "订单占座结果通知合作商户url异常", e);
		}
		logger.info(order_id + "result=" + result);

		// 用户接收并处理完成后，更新通知状态为完成
		if (StringUtils.isNotEmpty(result) && "SUCCESS".equals(result.trim())) {
			receiveService.updateOrderBookNotifyFinish(order_id);
			logs.setOrder_optlog("通知高铁成功[占座结果通知]");
			orderService.insertOrderLogs(logs);
		} else {
			int notify_num = receiveService.queryOrderBookNotifyStartNum(order_id);
			if (notify_num == 5) {// 通知5次失败，修改为通知失败
				Map<String, String> map = new HashMap<String, String>();
				map.put("order_id", order_id);
				map.put("notify_status", "33");// 00、未通知 11、准备通知 12、开始通知 22、通知完成
												// 33、通知失败
				map.put("current_notify_status", "12");
				receiveService.updateOrderGtBookNotifyStatus(map);
			} else {
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("order_id", order_id);
				paramMap.put("notify_status", "11");// 00、未通知 11、准备通知 12、开始通知
													// 22、通知完成
				paramMap.put("current_notify_status", "12");
				Integer num = receiveService.updateOrderGtBookNotifyStatus(paramMap);// 更新状态为12->11
				logger.info(order_id + ",----" + num + ",通知失败置为准备通知状态");
			}
		}

	}

}
