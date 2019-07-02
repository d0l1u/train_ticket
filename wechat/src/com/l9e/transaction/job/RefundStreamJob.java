package com.l9e.transaction.job;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.l9e.client.ClientResponseHandler;
import com.l9e.client.RequestHandler;
import com.l9e.client.TenpayHttpClient;
import com.l9e.common.ErrorCode;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;
import com.l9e.util.HttpUtil;
import com.l9e.weixin.util.MD5Util;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import com.umpay.api.paygate.v40.Mer2Plat_v40;
import com.umpay.api.paygate.v40.Plat2Mer_v40;

/**
 * 扫描退款流水发起退款请求job
 * 
 * @author zhangjun
 * 
 */
@Component("refundStreamJob")
public class RefundStreamJob {

	private static final Logger logger = Logger
			.getLogger(RefundStreamJob.class);

	@Resource
	private OrderService orderService;

	@Value("#{propertiesReader[merchantId]}")
	private String merchantId;

	@Value("#{propertiesReader[notify_refund_rul]}")
	private String notify_refund_rul;// 退款结果通知地址

	@Value("#{propertiesReader[signKey]}")
	private String signKey;// 验签key

	@Value("#{propertiesReader[req_url]}")
	private String req_url;// 移动优势请求地址

	@Value("#{propertiesReader[characterSet]}")
	private String characterSet;

	@Value("#{propertiesReader[partnerID]}")
	private String partnerID;
	@Value("#{propertiesReader[partnerKey]}")
	private String partnerKey;

	@Value("#{propertiesReader[caPath]}")
	private String caPath;
	@Value("#{propertiesReader[certPath]}")
	private String certPath;
	@Value("#{propertiesReader[op_user_passwd]}")
	private String op_user_passwd;
	@Value("#{propertiesReader[cert_pwd]}")
	private String cert_pwd;

	public void refund() {
		List<Map<String, String>> refundList = orderService
				.queryTimedRefundStreamList();
		for (Map<String, String> refundMap : refundList) {
			String pay_type = refundMap.get("pay_type");
			if ("33".equals(pay_type)) {
				this.tenpayRefund(refundMap);
			}
			// this.sendRefundStreamRequest(refundMap);
		}
	}

	private void sendRefundStreamRequest(Map<String, String> refundMap) {
		if (!"11".equals(refundMap.get("pay_type"))
				|| "".equals(refundMap.get("pay_time"))) {
			return;
		}
		Map<String, String> map = ErrorCode.getPayErrorCode();
		Map<String, String> upayRefundStatusMap = ErrorCode
				.getUpayRefundStatusMap();
		Map<String, String> updateMap = new HashMap<String, String>();

		String requestId = refundMap.get("refund_seq");// ASP退款请求流水号
		if (StringUtils.isEmpty(requestId)) {
			logger.info("【退款流水接口】退款流水号为空，订单号：" + refundMap.get("order_id"));
			return;
		}

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("stream_id", refundMap.get("stream_id"));
		paramMap.put("order_id", refundMap.get("order_id"));
		paramMap.put("refund_seq", requestId);
		paramMap.put("refund_status", TrainConsts.REFUND_STREAM_BEGIN_REFUND);// 开始退款

		int count = orderService.updateRefundStreamBegin(paramMap);// 更新退款请求开始

		if (count == 1) {
			if (TrainConsts.REFUND_TYPE_1.equals(refundMap.get("refund_type"))) {
				logger.info("【退款流水接口】用户退款，A订单号：" + refundMap.get("order_id"));

			} else if (TrainConsts.REFUND_TYPE_2.equals(refundMap
					.get("refund_type"))) {
				logger.info("【退款流水接口】差额退款，订单号：" + refundMap.get("order_id"));

			} else if (TrainConsts.REFUND_TYPE_3.equals(refundMap
					.get("refund_type"))) {
				logger.info("【退款流水接口】出票失败退款，订单号：" + refundMap.get("order_id"));

			} else if (TrainConsts.REFUND_TYPE_4.equals(refundMap
					.get("refund_type"))) {
				logger.info("【退款流水接口】改签差额退款，订单号：" + refundMap.get("order_id"));

			} else if (TrainConsts.REFUND_TYPE_5.equals(refundMap
					.get("refund_type"))) {
				logger.info("【退款流水接口】改签单退款，订单号：" + refundMap.get("order_id"));
			}
		} else {
			logger.info("【退款流水接口】更新退款开始异常，退款请求取消！" + refundMap.get("order_id"));
			return;
		}

		String orderId = refundMap.get("order_id");
		String refundMoney = refundMap.get("refund_money");
		String orgAmount = refundMap.get("pay_money");
		String payTime = refundMap.get("pay_time");
		Map<String, String> upayRefundMap = new HashMap<String, String>();
		upayRefundMap.put("service", "mer_refund");
		upayRefundMap.put("charset", "UTF-8");
		upayRefundMap.put("sign_type", "RSA");
		upayRefundMap.put("notify_url", notify_refund_rul);
		upayRefundMap.put("res_format", "HTML");
		upayRefundMap.put("mer_id", merchantId);
		upayRefundMap.put("version", "4.0");
		upayRefundMap.put("refund_no", refundMap.get("refund_no"));
		upayRefundMap.put("order_id", orderId);
		upayRefundMap.put("refund_amount", String.valueOf((int) (Double
				.parseDouble(refundMoney) * 100)));
		upayRefundMap.put("org_amount", String.valueOf((int) (Double
				.parseDouble(orgAmount) * 100)));
		upayRefundMap.put("mer_date", payTime);
		String refundUrl = null;
		try {
			refundUrl = Mer2Plat_v40.ReqDataByGet(upayRefundMap).getUrl();
		} catch (ReqDataException e) {
			logger.info("【退款流水接口】验证字段规则或者数据签名发生异常");
			return;
		}
		logger.info("url:::" + refundUrl);
		String html = HttpUtil.sendByGet(refundUrl, "UTF-8", null, null);
		logger.info("订单号为" + orderId + "退款返回html为" + html);
		Map<String, Object> resultMap = null;
		try {
			resultMap = Plat2Mer_v40.getResData(html);
		} catch (RetDataException e) {
			logger.info("订单号为" + orderId + "的订单退款校验失败！");
			return;
		}
		String retCode = (String) resultMap.get("ret_code");
		logger.info("【退款流水接口】订单号为" + orderId + "的流水号为" + requestId
				+ ",联通优势返回的退款状态为" + map.get(retCode) + "， 状态码为" + retCode);
		String refund_state = (String) resultMap.get("refund_state");

		updateMap.put("order_id", orderId);
		if ("REFUND_SUCCESS".equals(refund_state)) {
			logger.info("【退款流水接口】订单号为" + orderId + "的流水号为" + requestId
					+ ",联通优势返回的退款状态为" + upayRefundStatusMap.get(refund_state));
			updateMap.put("refund_status",
					TrainConsts.REFUND_STREAM_REFUND_FINISH);
			updateMap
					.put("eop_refund_seq", (String) resultMap.get("refund_no"));
			updateMap.put("ld_refund_status", "SUCCESS");
		} else {
			logger.info("【退款流水接口】订单号为" + orderId + "的流水号为" + requestId
					+ ",联通优势返回的退款状态为" + upayRefundStatusMap.get(refund_state));
			updateMap.put("refund_status", TrainConsts.REFUND_STREAM_FAIL);
			updateMap.put("unrefund_reason", map.get(retCode));
			updateMap.put("ld_refund_status", "FAIL");
		}
		updateMap.put("refund_seq", requestId);
		orderService.updateOrderStreamStatus(updateMap);

	}

	private void tenpayRefund(Map<String, String> refundMap) {
		// 创建查询请求对象
		RequestHandler reqHandler = new RequestHandler(null, null);
		// 通信对象
		TenpayHttpClient httpClient = new TenpayHttpClient();
		// 应答对象
		ClientResponseHandler resHandler = new ClientResponseHandler();

		// -----------------------------
		// 设置请求参数
		// -----------------------------
		reqHandler.init();
		reqHandler.setKey(partnerKey);
		reqHandler
				.setGateUrl("https://mch.tenpay.com/refundapi/gateway/refund.xml");

		// -----------------------------
		// 设置接口参数
		// -----------------------------
		reqHandler.setParameter("service_version", "1.1");
		reqHandler.setParameter("partner", partnerID);
		if (refundMap.get("order_id") != null
				&& !"".equals(refundMap.get("order_id"))) {
			reqHandler.setParameter("out_trade_no", refundMap.get("order_id"));
		}
		if (refundMap.get("transaction_id") != null
				&& !"".equals(refundMap.get("transaction_id"))) {
			reqHandler.setParameter("transaction_id", refundMap
					.get("transaction_id"));
		}
		reqHandler.setParameter("out_refund_no", refundMap.get("refund_no"));
		reqHandler.setParameter("total_fee", String.valueOf((int) (Double
				.parseDouble(refundMap.get("pay_money")) * 100)));
		reqHandler.setParameter("refund_fee", String.valueOf((int) (Double
				.parseDouble(refundMap.get("refund_money")) * 100)));
		reqHandler.setParameter("op_user_id", partnerID);
		// 操作员密码,MD5处理
		reqHandler.setParameter("op_user_passwd", MD5Util.MD5Encode(
				op_user_passwd, "GBK"));

		// reqHandler.setParameter("recv_user_id", "");
		// reqHandler.setParameter("reccv_user_name", "");

		// -----------------------------
		// 设置通信参数
		// -----------------------------
		// 设置请求返回的等待时间
		httpClient.setTimeOut(5);
		// 设置ca证书
		httpClient.setCaInfo(new File(caPath));

		// 设置个人(商户)证书
		httpClient.setCertInfo(new File(certPath), cert_pwd);

		// 设置发送类型POST
		httpClient.setMethod("POST");

		// 设置请求内容
		String requestUrl = null;
		try {
			requestUrl = reqHandler.getRequestURL();
		} catch (UnsupportedEncodingException e) {
			logger.error("【微信退款接口】设置请求内容时获得requestUrl发生异常：" + e);
		}
		httpClient.setReqContent(requestUrl);
		String rescontent = "null";

		// 后台调用
		if (httpClient.call()) {
			// 设置结果参数
			rescontent = httpClient.getResContent();
			try {
				resHandler.setContent(rescontent);
			} catch (Exception e) {
				logger.error("【微信退款订单接口】resHandler设置内容发生错误：" + e);
			}
			resHandler.setKey(partnerKey);

			// 获取返回参数
			String retcode = resHandler.getParameter("retcode");

			Map<String, String> resultMap = new HashMap<String, String>();
			resultMap.put("order_id", resHandler.getParameter("out_trade_no"));
			resultMap
					.put("refund_no", resHandler.getParameter("out_refund_no"));
			resultMap.put("refund_id", resHandler.getParameter("refund_id"));
			resultMap.put("refund_channel", resHandler
					.getParameter("refund_channel"));
			resultMap.put("refund_fee", resHandler.getParameter("refund_fee"));
			// 判断签名及结果
			if (resHandler.isTenpaySign() && "0".equals(retcode)) {
				/*
				 * 退款状态 refund_status 4，10：退款成功。 3，5，6：退款失败。 8，9，11:退款处理中。 1，2:
				 * 未确定，需要商户原退款单号重新发起。
				 * 7：转入代发，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，资金回流到商户的现金帐号，需要商户人工干预，通过线下或者财付通转账的方式进行退款。
				 */
				String refund_status = resHandler.getParameter("refund_status");
				if ("4".equals(refund_status) || "10".equals(refund_status)) {
					resultMap.put("refund_status",
							TrainConsts.REFUND_STREAM_REFUND_FINISH);
					Date date = new Date();
					DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					String refund_time = sdf.format(date);
					refundMap.put("refund_time", refund_time);
				} else if ("3".equals(refund_status)
						|| "5".equals(refund_status)
						|| "6".equals(refund_status)) {
					resultMap.put("refund_status",
							TrainConsts.REFUND_STREAM_FAIL);
				} else if ("1".equals(refund_status)
						|| "2".equals(refund_status)) {
					resultMap.put("refund_status",
							TrainConsts.REFUND_STREAM_FAIL);
					resultMap.put("refund_fail_reason", "未确定，需要商户原退款单号重新发起。");
				} else if ("7".equals(refund_status)) {
					resultMap.put("refund_status",
							TrainConsts.REFUND_STREAM_FAIL);
					resultMap
							.put("refund_fail_reason",
									"转入代发，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，资金回流到商户的现金帐号，需要商户人工干预，通过线下或者财付通转账的方式进行退款。");
				} else if ("8".equals(refund_status)
						|| "9".equals(refund_status)
						|| "10".equals(refund_status)) {
					resultMap.put("refund_status", "55");
				}
				String out_refund_no = resHandler.getParameter("out_refund_no");

				logger.info("商户退款单号" + out_refund_no + "的退款状态是："
						+ refund_status);

			} else {
				// 错误时，返回结果未签名，记录retcode、retmsg看失败详情。
				logger.info("验证签名失败或业务错误");
				logger.info("retcode:" + resHandler.getParameter("retcode")
						+ " retmsg:" + resHandler.getParameter("retmsg"));
				logger.info("retcode:" + resHandler.getParameter("retcode")
						+ " retmsg:" + resHandler.getParameter("retmsg"));
				resultMap.put("refund_status", TrainConsts.REFUND_STREAM_FAIL);
				resultMap.put("refund_fail_reason", resHandler
						.getParameter("retmsg"));
			}

			orderService.updateOrderStreamStatus(resultMap);
		} else {
			logger.info("后台调用通信失败");
			logger.info(httpClient.getResponseCode());
			logger.info(httpClient.getErrInfo());
			// 有可能因为网络原因，请求已经处理，但未收到应答。
		}

		// 获取debug信息,建议把请求、应答内容、debug信息，通信返回码写入日志，方便定位问题
		logger.info("http res:" + httpClient.getResponseCode() + ","
				+ httpClient.getErrInfo());

		logger.info("req url:" + requestUrl);
		logger.info("req debug:" + reqHandler.getDebugInfo());
		logger.info("res content:" + rescontent);
		logger.info("res debug:" + resHandler.getDebugInfo());

	}

	private void tenpayRefundMD5(Map<String, String> refundMap) {
		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("version", "1.1");
		requestMap.put("partner", partnerID);
		requestMap.put("out_trade_no", refundMap.get("order_id"));
		requestMap.put("total_fee", String.valueOf(Double.parseDouble(refundMap
				.get("pay_money")) * 100));
		requestMap.put("refund_fee", String.valueOf(Double.parseDouble(refundMap
				.get("refund_money")) * 100));
		requestMap.put("op_user_id", partnerID);
		requestMap.put("pt_user_passwd", op_user_passwd);
		ArrayList<String> keyList = new ArrayList<String>(requestMap.keySet());
		
	     //对key键值按字典升序排序  
		Collections.sort(keyList);
		logger.info("" + keyList.toString());
	}
}
