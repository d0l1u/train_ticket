package com.l9e.transaction.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.RefundService;
import com.l9e.util.HttpUtil;
import com.l9e.util.UrlFormatUtil;

@Component("sendRefundJob")
public class SendRefundJob {
	private static final Logger logger = Logger.getLogger(SendRefundJob.class);

	@Resource
	private OrderService orderService;
	@Resource
	private RefundService refundService;

	@Value("${config.notify_refund_interface_url}")
	private String notify_refund_interface_url;

	@Value("${config.notify_refund_back_url}")
	private String notify_refund_back_url;

	/**
	 * 给cp_orderinfo_refund表添加退款数据
	 */
	public void sendRefund() {
		List<Map<String, String>> sendList = orderService.queryCanRefundStreamList();
		if (null == sendList || sendList.size() == 0) {
			return;
		}
		for (Map<String, String> sendMap : sendList) {
			if (!StringUtils.isEmpty(sendMap.get("order_id")) && !StringUtils.isEmpty(sendMap.get("cp_id"))) {
				this.notifyRefundSys(sendMap);
			}
		}
	}

	// 给退款接口传参
	private void notifyRefundSys(Map<String, String> sendMap) {
		String order_id = sendMap.get("order_id");
		String cp_id = sendMap.get("cp_id");
		logger.info("对外商户退款开始~~~订单号：" + order_id + "，车票号" + cp_id);
		try {
			Map<String, String> param = new HashMap<String, String>();
			param.put("order_id", order_id);
			param.put("cp_id", cp_id);

			Map<String, String> orderMap = orderService.queryRefundCpOrderInfo(param);
			Map<String, Object> accountMap = orderService.queryAccountOrderInfo(param);
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("orderid", order_id);
			paramMap.put("cpid", cp_id);
			if (sendMap.get("refund_type").equals("3")) {
				orderMap = refundService.queryChangeRefundCpOrderInfo(param);
				// 改签退票
				paramMap.put("trainno", orderMap.get("train_no"));
				paramMap.put("fromstation", orderMap.get("from_city"));
				paramMap.put("arrivestation", orderMap.get("to_city"));
				paramMap.put("traveltime", orderMap.get("travel_time"));
				paramMap.put("fromtime", orderMap.get("from_time"));
				paramMap.put("buymoney", orderMap.get("buy_money"));
				paramMap.put("refundmoney", sendMap.get("refund_money"));
				paramMap.put("username", orderMap.get("user_name"));
				paramMap.put("tickettype", orderMap.get("ticket_type"));
				paramMap.put("idstype", orderMap.get("ids_type"));
				paramMap.put("userids", orderMap.get("user_ids"));
				paramMap.put("seattype", orderMap.get("seat_type"));
				paramMap.put("trainbox", orderMap.get("train_box"));
				paramMap.put("seatno", orderMap.get("seat_no"));
				paramMap.put("outticketbillno", orderMap.get("out_ticket_billno"));
				paramMap.put("outtickettime", orderMap.get("out_ticket_time"));
				String channel = orderMap.get("channel");
				logger.info(order_id + ",channel:" + channel);
				if (TrainConsts.GT_MERCHANT_ID_TEST.equals(channel)) {// 测试的id
					paramMap.put("channel", TrainConsts.GT_MERCHANT_ID);// 合作商编号
				} else if (TrainConsts.GT_MERCHANT_ID.equals(channel)) {// 正式的id
					paramMap.put("channel", TrainConsts.GT_MERCHANT_ID);// 合作商编号
				}
				paramMap.put("backurl", notify_refund_back_url);// 回调地址
				paramMap.put("accountname", (String) accountMap.get("acc_username"));
				paramMap.put("accountpwd", (String) accountMap.get("acc_password"));
				paramMap.put("refundseq", sendMap.get("refund_seq"));
				paramMap.put("userremark", sendMap.get("user_remark"));
				paramMap.put("refundpercent", sendMap.get("refund_percent"));
				paramMap.put("refundtype", "55");
			} else {
				paramMap.put("trainno", orderMap.get("train_no"));// 车票总价
				paramMap.put("fromstation", orderMap.get("from_city"));
				paramMap.put("arrivestation", orderMap.get("to_city"));
				paramMap.put("traveltime", orderMap.get("travel_time"));
				paramMap.put("fromtime", orderMap.get("from_time"));
				paramMap.put("buymoney", orderMap.get("buy_money"));
				paramMap.put("refundmoney", sendMap.get("refund_money"));
				paramMap.put("username", orderMap.get("user_name"));
				paramMap.put("tickettype", orderMap.get("ticket_type"));
				paramMap.put("idstype", orderMap.get("ids_type"));
				paramMap.put("userids", orderMap.get("user_ids"));
				paramMap.put("seattype", orderMap.get("seat_type"));
				paramMap.put("trainbox", orderMap.get("train_box"));
				paramMap.put("seatno", orderMap.get("seat_no"));
				paramMap.put("outticketbillno", orderMap.get("out_ticket_billno"));
				paramMap.put("outtickettime", orderMap.get("out_ticket_time"));
				String channel = orderMap.get("channel");
				logger.info(order_id + ",channel:" + channel);
				if (TrainConsts.GT_MERCHANT_ID_TEST.equals(channel)) {// 测试的id
					paramMap.put("channel", TrainConsts.GT_MERCHANT_ID);// 合作商编号
				} else if (TrainConsts.GT_MERCHANT_ID.equals(channel)) {// 正式的id
					paramMap.put("channel", TrainConsts.GT_MERCHANT_ID);// 合作商编号
				}
				paramMap.put("backurl", notify_refund_back_url);// 回调地址
				paramMap.put("accountname", (String) accountMap.get("acc_username"));
				paramMap.put("accountpwd", (String) accountMap.get("acc_password"));
				paramMap.put("refundseq", sendMap.get("refund_seq"));
				paramMap.put("userremark", sendMap.get("user_remark"));
				paramMap.put("refundpercent", sendMap.get("refund_percent"));
			}

			logger.info("退款通知出票系统参数：paramMap:" + paramMap);

			String params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");

			String result = HttpUtil.sendByPost(notify_refund_interface_url, params, "UTF-8");
			logger.info("请求通知退票接口返回：" + result);

			if (!StringUtils.isEmpty(result)) {
				String[] results = result.trim().split("\\|");

				if ("success".equalsIgnoreCase(results[0]) && results.length == 2 && order_id.equals(results[1])) {// 通知成功
					Map<String, String> map = new HashMap<String, String>();
					// 通知退票系统成功则订单状态修改为02开始机器改签
					map.put("order_id", order_id);
					map.put("cp_id", cp_id);
					map.put("order_status", "02");// 开始机器改签
					map.put("refund_type", "3");
					orderService.updateOrderRefundStatus(map);
					logger.info("通知退票系统成功，order_id=" + order_id + "，cp_id=" + cp_id);
				} else {// 通知退票系统失败则订单状态修改为03 人工改签
					logger.info("通知退票系统失败，order_id=" + order_id + "，cp_id=" + cp_id);
					Map<String, String> map = new HashMap<String, String>(2);
					map.put("order_id", order_id);
					map.put("cp_id", cp_id);
					map.put("order_status", "03");// 人工改签
					map.put("refund_type", "1");
					orderService.updateOrderRefundStatus(map);
				}
			}
		} catch (Exception e) {// 发生异常则更新超时重发
			e.printStackTrace();
			logger.info("通知退票系统异常，order_id=" + order_id + "，cp_id=" + cp_id);
			// Map<String, String> map = new HashMap<String, String>(2);
			// map.put("timeout", "1");
			// map.put("order_id", order_id);
			// orderService.updateOrderTimeOut(map);
		}
		logger.info("对外商户退款结束~~~订单号：" + order_id + "，车票号" + cp_id);
	}
}
