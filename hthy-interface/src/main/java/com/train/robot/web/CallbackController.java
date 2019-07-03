package com.train.robot.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.train.commons.util.HttpUtil;
import com.train.robot.method.HangtianMethod;

/**
 * CallbackController-航天华有接口对接
 *
 * @author daiqinghua
 * @date 2018/6/17
 */

@Controller
@RequestMapping("/callback")
public class CallbackController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(CallbackController.class);

	@Value("${k618.callback.order}")
	private String orderCallback;

	@Value("${k618.callback.confirm}")
	private String confirmCallback;

	@Value("${k618.callback.refund}")
	private String refund;

	/**
	 * 占位回调
	 */
	@RequestMapping(value = "/orderCallback")
	public void orderCallback(HttpServletRequest request, HttpServletResponse response) {
		long begin = System.currentTimeMillis();
		String logid = createLogid();
		logger.info("{}占位结果回调start.......", logid);
		String result = request.getParameter("data");
		logger.info("{}回调结果:{}", logid, result);

		// 判断结果必要参数是否存在
		logger.info("{}校验结果必要参数", logid);
		JSONObject resultJson = null;
		try {
			resultJson = JSONObject.parseObject(result);
			// String reqtoken = resultJson.getString("reqtoken");
			String code = resultJson.getString("code");
			// String msg = resultJson.getString("msg");
			String ordersuccess = resultJson.getString("ordersuccess");
			String transactionid = resultJson.getString("transactionid");
			String orderid = resultJson.getString("orderid");
			String success = resultJson.getString("success");

			if (
			// StringUtils.isBlank(reqtoken) ||
			StringUtils.isBlank(code) ||
			// StringUtils.isBlank(msg) ||
					StringUtils.isBlank(ordersuccess) || StringUtils.isBlank(transactionid)
					|| StringUtils.isBlank(orderid) || StringUtils.isBlank(success)) {
				throw new RuntimeException("占位回调结果缺失必要参数");
			}
		} catch (Exception e) {
			logger.info("{}占位回调结果解析异常", logid, e);
			printJson(response, "false");
			return;
		}

		// 处理结果，回调出票系统
		logger.info("{}回调出票系统占位{}:{}", logid, orderCallback, result);
		String httpResult = new HttpUtil().doHttpPost(orderCallback, result, 1000 * 30, true, "UTF-8");
		logger.info("{}出票系统同步结果:{}", logid, httpResult);
		if ("success".equals(httpResult)) {
			printJson(response, "success");
		} else {
			printJson(response, "false");
		}

		logger.info("{}占位回调耗时:{}ms", logid, (System.currentTimeMillis() - begin));
	}

	/**
	 * 取消占位回调
	 */
	@RequestMapping(value = "/cancelCallback")
	public void cancelCallback() {

	}

	/**
	 * 确认占位回调， 支付回调
	 */
	@RequestMapping(value = "/confirmCallback")
	public void confirmCallback(HttpServletRequest request, HttpServletResponse response) {
		long begin = System.currentTimeMillis();
		String logid = createLogid();
		logger.info("{}确认占位结果回调start.......", logid);
		String result = getPostParameter(request);
		logger.info("{}回调出票系统确认占位:{}", logid, result);

		String httpResult = new HttpUtil().doHttpPost(confirmCallback, result, 1000 * 30, false, "UTF-8");
		logger.info("{}出票系统同步结果:{}", logid, httpResult);
		if ("success".equals(httpResult)) {
			printJson(response, "success");
		} else {
			printJson(response, "false");
		}
		logger.info("{}确认占位回调耗时:{}ms", logid, (System.currentTimeMillis() - begin));
	}

	/**
	 * 线上退票结果回调
	 */
	@RequestMapping(value = "/refundCallback")
	public void refundCallback(HttpServletRequest request, HttpServletResponse response) {
		long begin = System.currentTimeMillis();
		String logid = createLogid();
		logger.info("{}线上退票结果回调start.......", logid);
		String result = request.getParameter("data");
		logger.info("{}回调出票系统线上退票:{}", logid, result);

		String httpResult = new HttpUtil().doHttpPost(confirmCallback, result, 1000 * 30, true, "UTF-8");
		logger.info("{}出票系统同步结果:{}", logid, httpResult);
		if ("success".equals(httpResult)) {
			printJson(response, "success");
		} else {
			printJson(response, "false");
		}
		logger.info("{}线上退票回调耗时:{}ms", logid, (System.currentTimeMillis() - begin));
	}

	/**
	 * 改签占座结果回调
	 */
	@RequestMapping(value = "/changeOrderCallback")
	public void changeTrainCallback(HttpServletRequest request, HttpServletResponse response) {
		String logid = createLogid();
		logger.info("{}改签占座结果回调start.......", logid);
		String result = getPostParameter(request);
		logger.info("{}获取到返回值:{}", logid, result);
		printJson(response, "success");
		logger.info("{}改签占座结果回调end.......", logid);
	}

	/**
	 * 确认改签占座结果回调，改签支付回调
	 */
	@RequestMapping(value = "/confirmChangeCallback")
	public void confirmChangeTrainCallback(HttpServletRequest request, HttpServletResponse response) {
		String logid = createLogid();
		logger.info("{}确认改签占座结果回调start.......", logid);
		String result = getPostParameter(request);
		logger.info("{}获取到返回值:{}", logid, result);
		printJson(response, "success");
		logger.info("{}确认改签占座结果回调end.......", logid);
	}
	// -------------------------------------回调方法end----------------------------------

	public static void main(String[] args) {
		HangtianMethod htc = new HangtianMethod();
		String orderId = "19eTest180621075530691";
		String transactionId = "T1806215C193F420EB7C04E8C0BC420BA20C1FA2C83";
		String orderNumber = "EE56977471";
		// 下单
		System.out.println(htc.trainOrder(null));
		// 确定订单
		// System.out.println(htc.confirmOrder(orderId,transactionId));
		// 查询订单
		// System.out.println(htc.queryOrder(orderId,transactionId));
		// 取消订单
		// System.out.println(htc.cancelOrder(orderId,transactionId));
		// 退票
		// System.out.println(htc.returnTicket(orderId,transactionId,orderNumber));
		// 改签
		// System.out.println(htc.changeTrain(orderId,transactionId,orderNumber));
		// 取消改签
		// System.out.println(htc.cancelChangeTrain(orderId,transactionId));
		// 确认改签
		// System.out.println(htc.confirmChangeTrain(orderId,transactionId));
	}

}
