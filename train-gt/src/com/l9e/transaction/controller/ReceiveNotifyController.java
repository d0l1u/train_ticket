package com.l9e.transaction.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.ChangeService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.ReceiveNotifyService;
import com.l9e.transaction.service.RefundService;
import com.l9e.transaction.vo.ExternalLogsVo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.util.MathUtil;
import com.l9e.util.StrUtil;

/**
 * @ClassName: ReceiveNotifyController
 * @Description: 接收出票系统通知，更新订单信息
 * @author: taoka
 * @date: 2018年1月9日 下午4:30:43
 * @Copyright: 2018 www.19e.cn Inc. All rights reserved.
 */
@Controller
@RequestMapping("/receiveNotify")
public class ReceiveNotifyController extends BaseController {

	private static final Logger logger = Logger.getLogger(ReceiveNotifyController.class);

	@Resource
	private OrderService orderService;

	@Resource
	private RefundService refundService;

	@Resource
	private ReceiveNotifyService receiveNotifyService;
	@Resource
	private ChangeService changeService;

	/**
	 * 接收出票结果通知
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/cpNotify_no.jhtml")
	public void cpNotify(HttpServletRequest request, HttpServletResponse response) {
		String result = this.getParam(request, "result");
		String orderId = this.getParam(request, "orderid");
		String billNo = this.getParam(request, "billno");
		String buyMoney = this.getParam(request, "buymoney");
		String seatTrains = this.getParam(request, "seattrains");
		String status = this.getParam(request, "status");// 00：出票成功 11预定成功
		String level = this.getParam(request, "level");// vip用户不为空
		String errorinfo1 = this.getParam(request, "errorinfo");// 错误信息

		logger.info("[接收出票结果通知接口]参数orderId=" + orderId + "，result=" + result + "，billNo=" + billNo + "，buyMoney=" + buyMoney + "，seatTrains=" + seatTrains
				+ "，status=" + status + ",level=" + level + ",errorinfo1=" + errorinfo1);

		Map<String, String> paramMap = new HashMap<String, String>(3);// 主订单参数
		List<Map<String, String>> cpMapList = new ArrayList<Map<String, String>>();// 车票订单参数

		if (StringUtils.isEmpty(result) || StringUtils.isEmpty(orderId)) {
			// 参数为空
			logger.info("exception：参数为空！");
			write2Response(response, "failed");

		} else if (TrainConsts.SUCCESS.equalsIgnoreCase(result)) {// 成功
			if (StringUtils.isEmpty(billNo) || StringUtils.isEmpty(buyMoney) || StringUtils.isEmpty(status) || StringUtils.isEmpty(seatTrains)) {
				// 参数为空
				logger.info("exception：明细参数为空！");
				write2Response(response, "failed");
				return;
			} else if (!"11".equals(status) && !"00".equals(status)) {
				logger.info("exception：状态有误！status=" + status);
				write2Response(response, "failed");
				return;
			}

			// 查询订单信息
			OrderInfo orderInfo = orderService.queryOrderInfo(orderId);
			String order_type = orderInfo.getOrder_type(); // order_type:11.先下单后付款,22.先支付后预定
			logger.info(orderId + ",订单类型:" + order_type);

			if (StringUtils.isEmpty(orderInfo.getOrder_status())) {
				logger.info("exception：订单查询异常，订单状态为空！");
				write2Response(response, "failed");
				return;
			}
			Map<String, String> merchantSetting = commonService.queryMerchantInfo(orderInfo.getMerchant_id());
			if ("11".equals(status)) {// 预订成功
				// 预订成功通知重复通知
				if (!TrainConsts.BOOKING_TICKET.equals(orderInfo.getOrder_status())) {
					logger.info("[接收出票结果通知接口本次预订成功通知为重复通知，orderId=" + orderId);
					write2Response(response, "success");
					return;
				}
				paramMap.put("order_id", orderId);
				paramMap.put("book_flow", merchantSetting.get("merchantSetting")); // 购票流程
																					// 00:先支付后出票；11：先预定后支付
				paramMap.put("buy_money", buyMoney);
				paramMap.put("out_ticket_billno", billNo);
				paramMap.put("order_status", TrainConsts.BOOK_SUCCESS);// 预订成功

				// 明细数据处理
				this.detailDataPacking(seatTrains, cpMapList, response);

				int count = receiveNotifyService.updateOrderWithCpNotify(paramMap, cpMapList, null);
				if (count == 1) {
					write2Response(response, "success");
					// if("1".equals(orderInfo.getSms_notify()) &&
					// "1".equals(orderInfo.getOrder_level())){
					// //vip发送预订成功短信
					// this.sendVipSuccMsn(orderId, billNo);
					// }
					ExternalLogsVo logs = new ExternalLogsVo();
					logs.setOrder_id(orderId);
					logs.setOrder_optlog("预定成功");
					logs.setOpter("gt_app");
					orderService.insertOrderLogs(logs);
				} else {
					logger.info("failed：修改订单" + orderId + "失败！");
					write2Response(response, "failed");
				}

			} else if ("00".equals(status)) {// 出票成功

				if (!TrainConsts.BOOKING_TICKET.equals(orderInfo.getOrder_status()) && !TrainConsts.BOOK_SUCCESS.equals(orderInfo.getOrder_status())) {
					logger.info("[接收出票结果通知接口]本次出票成功通知为重复通知，orderId=" + orderId);
					write2Response(response, "success");
					return;
				}
				// 出票成功errorinfo 为12时，为旅游票不可退
				String errorinfo = this.getParam(request, "errorinfo");// 错误信息
				if ("12".equals(errorinfo)) {
					paramMap.put("is_travel", "1");
				}
				logger.info("errorinfo=" + errorinfo);

				paramMap.put("order_id", orderId);
				paramMap.put("buy_money", buyMoney);
				paramMap.put("out_ticket_time", "now");// 保证出票时间值非空，数据库进行处理
				paramMap.put("out_ticket_billno", billNo);
				paramMap.put("order_status", TrainConsts.OUT_SUCCESS);// 出票成功

				// 明细数据处理
				this.detailDataPacking(seatTrains, cpMapList, response);

				int count = receiveNotifyService.updateOrderWithCpNotify(paramMap, cpMapList, null);
				if (count == 1) {
					write2Response(response, "success");
					ExternalLogsVo logs = new ExternalLogsVo();
					logs.setOrder_id(orderId);
					logs.setOrder_optlog("出票成功");
					logs.setOpter("gt_app");
					orderService.insertOrderLogs(logs);
				} else {
					logger.info("failed：修改订单" + orderId + "失败！");
					write2Response(response, "failed");
				}
			}

		} else if (TrainConsts.FAILURE.equalsIgnoreCase(result)) {// 失败
			// 阻止重复出票系统通知请求
			OrderInfo orderInfo = orderService.queryOrderInfo(orderId);
			if (!StringUtils.isEmpty(orderInfo.getOrder_status()) && TrainConsts.OUT_FAIL.equals(orderInfo.getOrder_status())) {
				logger.info("[接收出票结果通知接口]本次出票失败通知为重复请求，orderId=" + orderId);
				write2Response(response, "success");
				return;
			}

			if (!StringUtils.isEmpty(orderInfo.getOrder_status()) && TrainConsts.ORDER_OUT_TIME.equals(orderInfo.getOrder_status())) {
				logger.info("[接收出票结果通知接口]订单已经超时,不再更新订单内容以及结果,orderId=" + orderId);
				write2Response(response, "success");
				return;
			}

			if (!StringUtils.isEmpty(orderInfo.getOrder_status()) && TrainConsts.CANCEL_SUCCESS.equals(orderInfo.getOrder_status())) {
				logger.info("[接收出票结果通知接口]订单已经取消,不再更新订单内容以及结果,orderId=" + orderId);
				write2Response(response, "success");
				return;
			}
			// //如果是先预定后支付 则 判断是否为支付成功订单
			// Map<String, String> merchantSetting =
			// commonService.queryMerchantInfo(orderInfo.getMerchant_id());
			// if("11".equals(merchantSetting.get("book_flow"))){//针对先预定后支付的订单
			// if(StringUtils.isEmpty(orderInfo.getEop_pay_number())){//前台代扣支付流水号空
			// logger.info("[接收出票结果通知接口]本次出票失败不是支付成功订单不添加出票失败退款信息，orderId=" +
			// orderId);
			// ExternalLogsVo logs=new ExternalLogsVo();
			// logs.setOrder_id(orderId);
			// logs.setOrder_optlog("[接收出票结果通知接口]本次出票失败不是支付成功订单不添加出票失败退款信息，orderId="
			// + orderId);
			// logs.setOpter("gt_app");
			// orderService.insertOrderLogs(logs);
			// write2Response(response, "success");
			// return;
			// }else
			// if(!StringUtils.isEmpty(orderInfo.getEop_pay_number())){//支付流水号不为空
			// int count =
			// orderService.queryHistoryByOrderId(orderId);//判断是否有支付成功日志（余额不足等原因会造成有支付流水号但没有支付成功）
			// if(count == 0){
			// ExternalLogsVo logs=new ExternalLogsVo();
			// logs.setOrder_id(orderId);
			// logs.setOrder_optlog("[接收出票结果通知接口]本次出票失败不是支付成功订单不添加出票失败退款信息，orderId="
			// + orderId);
			// logs.setOpter("gt_app");
			// orderService.insertOrderLogs(logs);
			// logger.info("[接收出票结果通知接口]本次出票失败不是支付成功订单不添加出票失败退款信息，orderId=" +
			// orderId);
			// write2Response(response, "success");
			// return;
			// }
			// }
			// }
			paramMap.put("order_id", orderId);
			paramMap.put("buy_money", buyMoney);
			paramMap.put("out_ticket_billno", billNo);
			paramMap.put("order_status", TrainConsts.OUT_FAIL);// 出票失败

			String errorinfo = this.getParam(request, "errorinfo");// 错误信息
			Map<String, String> failRefundMap = new HashMap<String, String>();// 出票失败退款map
			failRefundMap.put("order_id", orderId);
			failRefundMap.put("refund_type", TrainConsts.ORDER_RETURN_TYPE_2);// 出票失败退款
			failRefundMap.put("eop_pay_number", orderInfo.getEop_pay_number());// 出票失败退款
			failRefundMap.put("refund_amount", orderInfo.getPay_money_show());// 退给用户的金额
			failRefundMap.put("refund_money", orderInfo.getPay_money()); // 退给商户的金额
			failRefundMap.put("fail_reason", errorinfo);
			int count = receiveNotifyService.updateOrderWithCpNotify(paramMap, null, failRefundMap);
			if (count == 1) {
				write2Response(response, "success");
				// 发送出票失败短信
				// this.sendCpFailMsn(orderId);

			} else {
				logger.info("failed：修改订单" + orderId + "失败！");
				write2Response(response, "failed");
			}

		} else {// 异常
			logger.info("exception：订单" + orderId + "，接口返回未知状态码！");
			write2Response(response, "failed");
		}
	}

	/**
	 * 明细数据组合
	 */
	private void detailDataPacking(String seatTrains, List<Map<String, String>> cpMapList, HttpServletResponse response) {
		// CP0120212|133|12|058号#CP0120213|133|12|059号
		String[] seatMsgs = seatTrains.split("#");
		Map<String, String> cpMap = null;
		for (String seatMsg : seatMsgs) {// CP0120212|133|12|058号
			String[] str = seatMsg.split("\\|");
			if (str == null) {
				// 参数为空
				logger.info("exception：参数拆分失败！");
				write2Response(response, "failed");
				return;
			}
			for (int i = 0; i < str.length; i++) {
				cpMap = new HashMap<String, String>();
				cpMap.put("cp_id", str[0]);
				cpMap.put("buy_money", str[1]);// 成本价格
				cpMap.put("train_box", str[2]);// 车厢
				cpMap.put("seat_no", str[3]);// 座位号
				if (str.length == 5) {
					cpMap.put("seat_type", str[4]);// 座位号
				}
				cpMapList.add(cpMap);
			}
		}
	}

	// 【步步高超市】尊贵的VIP用户，欢迎您使用19e提供的火车票订购业务，我们会竭诚为您服务！如果您在订购中出现问题，请拨打专线400-698-6666转5进行咨询。

	
	/**
	 * 接收退票结果通知
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/refundSendNotify_no.jhtml")
	public void refundSendNotify(HttpServletRequest request, HttpServletResponse response) {
		String result = this.getParam(request, "result");// 成功
		String orderId = this.getParam(request, "orderid");
		String cpid = this.getParam(request, "cpid");
		String alterdiffmoney = this.getParam(request, "alterdiffmoney");
		String refundmoney = this.getParam(request, "refundmoney");
		String refund12306money = this.getParam(request, "refund12306money");
		String refund12306seq = this.getParam(request, "refund12306seq");
		String status = this.getParam(request, "status");// 0、改签通知 1、退票通知
		String our_remark = this.getParam(request, "our_remark");// 备注
		String refuse_reason = this.getParam(request, "refuse_reason");// 拒绝退票原因

		logger.info("【接收退票系统通知】参数orderId=" + orderId + "，cpid=" + cpid + "，alterdiffmoney=" + alterdiffmoney + "，refund12306money=" + refund12306money
				+ "，refund12306seq=" + refund12306seq + "，status=" + status + "，result=" + result + "，our_remark=" + our_remark + "，refuse_reason="
				+ refuse_reason);
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> paramMap = new HashMap<String, String>();
		map.put("order_id", orderId);
		map.put("cp_id", cpid);
		map.put("our_remark", refuse_reason);// 出票方备注
		map.put("refuse_reason", refuse_reason);
		paramMap.put("order_id", orderId);
		paramMap.put("cp_id", cpid);
		String refund_seq = orderService.queryRefundStreamSeq(paramMap);
		paramMap.put("refund_seq", refund_seq);

		try {
			// 00、等待退票
			// 01、02正在改签（01：重新机器改签;02：开始机器改签;）03：人工改签;04、05、06正在退票（04：等待机器退票；05：重新机器退票；06：开始机器退票；）07：人工退票；
			// 11、同意退票 22、拒绝退票 33、退票完成 44、退票失败 55、审核退款；99、搁置订单
			if ("0".equals(status) && TrainConsts.SUCCESS.equalsIgnoreCase(result)) {
				map.put("alter_money", alterdiffmoney);
				orderService.updateCPAlterInfo(map);
				logger.info("更改gt_orderinfo_cp表的alter_money=" + alterdiffmoney);
				write2Response(response, "success");
			} else if ("1".equals(status) && TrainConsts.SUCCESS.equalsIgnoreCase(result) && !StrUtil.isNotEmpty(refuse_reason)) {
				String buyMoney = "";
				// 查询订单信息
				OrderInfoCp orderCp = orderService.queryCpInfoByCpId(cpid);
				if (orderCp == null) {
					// 改签退款
					Map<String, Object> changeParam = new HashMap<String, Object>();
					changeParam.put("new_cp_id", cpid);
					changeParam.put("order_id", orderId);
					Map<String, Object> changeCpInfo = changeService.queryChangeCpInfo(changeParam);
					buyMoney = MathUtil.bigDecimalTOString((java.math.BigDecimal) changeCpInfo.get("change_buy_money"));
				} else {
					buyMoney = orderCp.getBuy_money();
				}
				map.put("alter_tickets_money", Double.valueOf(alterdiffmoney));// 改签差价
				map.put("actual_refund_money", Double.valueOf(refund12306money));// 12306实际退款金额
				map.put("refund_12306_seq", refund12306seq); // 12306退款流水单号
				map.put("refund_status", "11");
				if (Double.valueOf(refundmoney) < 0) {
					map.put("refund_money", Double.valueOf("0"));
				} else {
					if (buyMoney.equals(refund12306money)) {
						map.put("refund_money", Double.valueOf(refund12306money));
					} else {
						map.put("refund_money", Double.valueOf(refundmoney));
					}
				}
				map.put("refund_time", "NOW()");
				// map.put("refund_type", "1");
				// orderService.updateRefundInfo(map);
				orderService.updateRefundAgree(map, paramMap);// 同意退款
				paramMap.put("notify_status", "11");
				refundService.updateRefundNotifyRestart(paramMap);
				logger.info("更改gt_orderinfo_refundstream表的refund_status=11同意退款");
				write2Response(response, "success");
			} else if ("1".equals(status) && (TrainConsts.FAILURE.equalsIgnoreCase(result) || StrUtil.isNotEmpty(refuse_reason))) {
				map.put("refund_status", "22");// 22、拒绝退款
				// map.put("refund_type", "1");
				orderService.updateRefundRefuse(map, paramMap);// 拒绝退款
				// orderService.updateRefundInfo(map);
				logger.info("更改gt_orderinfo_refundstream表的refund_status=22拒绝退款");
				paramMap.put("notify_status", "11");
				refundService.updateRefundNotifyRestart(paramMap);
				write2Response(response, "success");
			} else if ("2".equals(status)) {
				map.put("refund_status", "99");// 99、搁置订单
				// map.put("refund_type", "1");
				orderService.updateRefundInfo(map);
				logger.info("更改gt_orderinfo_refundstream表的refund_status=99搁置订单");
				write2Response(response, "success");
			}
		} catch (Exception e) {
			logger.error("exception", e);
			write2Response(response, "failed");
		}
	}

}
