package com.l9e.transaction.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.l9e.client.ClientResponseHandler;
import com.l9e.client.RequestHandler;
import com.l9e.client.ResponseHandler;
import com.l9e.client.TenpayHttpClient;
import com.l9e.common.BaseController;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.UserInfoService;
import com.l9e.weixin.exception.SDKRuntimeException;
import com.l9e.weixin.util.MD5SignUtil;
import com.l9e.weixin.util.Sha11Util;
import com.l9e.weixin.util.WeChatUtil;

@Controller
@RequestMapping("/wepay")
public class WeChatPayController extends BaseController {
	private static final Logger logger = Logger
			.getLogger(WeChatPayController.class);
	@Value("#{propertiesReader[paySignKey]}")
	private String paySignKey;// 公众号中用于支付请求中用于加密的密钥
	@Value("#{propertiesReader[delivernotify_url]}")
	private String delivernotify_url;
	@Value("#{propertiesReader[appid]}")
	private String appid;
	@Value("#{propertiesReader[partnerID]}")
	private String partnerID;
	@Value("#{propertiesReader[partnerKey]}")
	private String partnerKey;
	@Value("#{propertiesReader[secret]}")
	private String secret;
	@Value("#{propertiesReader[query_order_url]}")
	private String query_order_url;
	@Value("#{propertiesReader[caPath]}")
	private String caPath;
	@Value("#{propertiesReader[certPath]}")
	private String certPath;
	@Value("#{propertiesReader[op_user_passwd]}")
	private String op_user_passwd;
	@Value("#{propertiesReader[cert_pwd]}")
	private String cert_pwd;

	@Resource
	private OrderService orderService;
	@Resource
	private UserInfoService userInfoService;

	@RequestMapping("/payReturn.jhtml")
	public void payReturn(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, String> payResultMap = new HashMap<String, String>();

		// 创建支付应答对象
		ResponseHandler resHandler = new ResponseHandler(request, response);
		resHandler.setKey(partnerKey);

		if (resHandler.isValidSign() == true) {

			// 通知id
			String notify_id = resHandler.getParameter("notify_id");

			// 创建请求对象
			RequestHandler queryReq = new RequestHandler(null, null);
			// 通信对象
			TenpayHttpClient httpClient = new TenpayHttpClient();
			// 应答对象
			ClientResponseHandler queryRes = new ClientResponseHandler();

			// 通过通知ID查询，确保通知来至财付通
			queryReq.init();
			queryReq.setKey(partnerKey);
			queryReq
					.setGateUrl("https://gw.tenpay.com/gateway/verifynotifyid.xml");
			queryReq.setParameter("partner", partnerID);
			queryReq.setParameter("notify_id", notify_id);

			// 通信对象
			httpClient.setTimeOut(5);
			// 设置请求内容
			try {
				httpClient.setReqContent(queryReq.getRequestURL());
				logger.info("queryReq:" + queryReq.getRequestURL());
			} catch (UnsupportedEncodingException e) {
				logger.error("【支付结果返回接口】设置请求内容发生异常:" + e);
			}

			// 后台调用
			if (httpClient.call()) {
				// 设置结果参数
				try {
					queryRes.setContent(httpClient.getResContent());
				} catch (Exception e) {
					logger.error("【支付结果返回接口】设置请求内容发生异常:" + e);
				}
				logger.info("queryRes:" + httpClient.getResContent());
				queryRes.setKey(partnerKey);

				// 获取返回参数
				String retcode = queryRes.getParameter("retcode");
				String trade_state = queryRes.getParameter("trade_state");
				String trade_mode = queryRes.getParameter("trade_mode");

				// 判断签名及结果
				if (queryRes.isTenpaySign() && "0".equals(retcode)
						&& "0".equals(trade_state) && "1".equals(trade_mode)) {

					logger.info("订单查询成功");
					// 取结果参数做业务处理
					logger.info("out_trade_no:"
							+ queryRes.getParameter("out_trade_no")
							+ " transaction_id:"
							+ queryRes.getParameter("transaction_id"));
					logger.info("trade_state:"
							+ queryRes.getParameter("trade_state")
							+ " total_fee:"
							+ queryRes.getParameter("total_fee"));
					// 如果有使用折扣券，discount有值，total_fee+discount=原请求的total_fee
					logger.info("discount:" + queryRes.getParameter("discount")
							+ " time_end:" + queryRes.getParameter("time_end"));

					// 商户订单号
					String out_trade_no = queryRes.getParameter("out_trade_no");

					// 财付通订单号
					String transaction_id = queryRes
							.getParameter("transaction_id");

					// 金额,以分为单位
					String total_fee = queryRes.getParameter("total_fee");

					// 如果有使用折扣券，discount有值，total_fee+discount=原请求的total_fee
					String discount = queryRes.getParameter("discount");
					String timeEnd = queryRes.getParameter("time_end");
					DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
					Date payTime = null;
					try {
						payTime = df.parse(timeEnd);
					} catch (ParseException e) {
						logger.error("字符串转化为日期时发生错误！");
						e.printStackTrace();
					}
					DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					String payTimeStr = sdf.format(payTime);

					// ------------------------------
					// 处理业务开始
					// ------------------------------

					// 处理数据库逻辑
					payResultMap.put("order_id", out_trade_no);
					payResultMap.put("return_fee", total_fee);
					payResultMap.put("discount", discount);
					payResultMap.put("transaction_id", transaction_id);
					payResultMap.put("pay_type", "33");
					payResultMap.put("pay_time", payTimeStr);
					payResultMap.put("notify_id", notify_id);
					payResultMap.put("order_status", TrainConsts.PAY_SUCCESS);
					Map<String, String> orderInfo = orderService
							.queryOrderInfo(out_trade_no);
					if (!TrainConsts.PAY_SUCCESS.equals(orderInfo
							.get("order_status"))) {
						orderService.updateOrderInfo(payResultMap);
					}

					// 注意交易单不要重复处理

					// !!!注意判断返回金额(total_fee + discount) = 原请求的total_fee!!!

					// ------------------------------
					// 处理业务完毕
					// ------------------------------
					logger.info("支付成功");
				} else {
					// 错误时，返回结果未签名，记录retcode、retmsg看失败详情。
					logger.error("查询验证签名失败或业务错误");
					logger.error("retcode:" + queryRes.getParameter("retcode")
							+ " retmsg:" + queryRes.getParameter("retmsg"));
					logger.error("通知查询签名失败或业务参数错误，支付失败");
				}

			} else {
				logger.error("后台调用通信失败");
				logger.error(httpClient.getResponseCode());
				logger.error(httpClient.getErrInfo());
				logger.error("订单通知查询失败");
				// 有可能因为网络原因，请求已经处理，但未收到应答。
			}
		} else {
			logger.error("认证签名失败");
		}

		// 获取debug信息,建议把debug信息写入日志，方便定位问题
		String debuginfo = resHandler.getDebugInfo();
		logger.info("debuginfo:" + debuginfo);
	}

	@RequestMapping("/receiveWepayNotify.jhtml")
	public void receiveWepayNotify(HttpServletRequest request,
			HttpServletResponse response) {

		Map<String, String> payResultMap = new HashMap<String, String>();

		// 创建支付应答对象
		ResponseHandler resHandler = new ResponseHandler(request, response);
		resHandler.setKey(partnerKey);
		resHandler.setAppkey(paySignKey);
		
		// 创建请求对象
		RequestHandler queryReq = new RequestHandler(null, null);
		queryReq.init();
		// 判断签名
		logger
				.info("支付应答对象得到的参数为: "
						+ resHandler.getAllParameters().toString());
		if (resHandler.isValidSign() == true) {
			logger.info("resHandler.isValidSign()验证通过！");
			if (resHandler.isWXsign() == true) {
				logger.info("resHandler.isWXsign()验证通过！");
				// 商户订单号
				String order_id = resHandler.getParameter("out_trade_no");
				// 财付通订单号
				Map<String, String> orderInfo = orderService
						.queryOrderInfo(order_id);
				String transaction_id = resHandler
						.getParameter("transaction_id");
				// 金额,以分为单位
				String total_fee = resHandler.getParameter("total_fee");
				// 如果有使用折扣券，discount有值，total_fee+discount=原请求的total_fee
				String discount = resHandler.getParameter("discount");
				// 支付结果
				String trade_state = resHandler.getParameter("trade_state");
				String notify_id = resHandler.getParameter("notify_id");
				String timeEnd = resHandler.getParameter("time_end");
				if ("0".equals(trade_state)) {
					// ------------------------------
					// 即时到账处理业务开始
					// ------------------------------

					DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
					Date payTime = null;
					try {
						payTime = df.parse(timeEnd);
					} catch (ParseException e) {
						logger.error("字符串转化为日期时发生错误！");
						e.printStackTrace();
					}
					DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					String payTimeStr = sdf.format(payTime);
					payResultMap.put("order_id", order_id);
					payResultMap.put("pay_type", "33");
					payResultMap.put("channel", "weixin");
					payResultMap.put("transaction_id", transaction_id);
					payResultMap.put("pay_time", payTimeStr);
					// payResultMap.put("pay_money", totalFee);
					payResultMap.put("notify_id", notify_id);
					payResultMap.put("return_fee", String.valueOf(Double
							.parseDouble(total_fee) / 100));
					payResultMap.put("discount", String.valueOf(Double
							.parseDouble(discount) / 100));
					payResultMap.put("order_status", TrainConsts.PAY_SUCCESS);

					// 注意交易单不要重复处理
					logger.info("【微信支付结果通知接口】更新数据库表参数为：" + payResultMap);
					if (!TrainConsts.PAY_SUCCESS.equals(orderInfo
							.get("order_status"))) {
						orderService.updateOrderInfo(payResultMap);
					}

					// 处理数据库逻辑
					// 注意交易单不要重复处理
					// 注意判断返回金额

					// ------------------------------
					// 即时到账处理业务完毕
					// ------------------------------

					logger.info("success 后台通知成功");
					// 给财付通系统发送成功信息，财付通系统收到此结果后不再进行后续通知
				} else {
					payResultMap.put("order_id", order_id);
					payResultMap.put("pay_type", "33");
					payResultMap.put("transaction_id", transaction_id);
					payResultMap.put("notify_id", notify_id);
					payResultMap.put("channel", "weixin");
					payResultMap.put("order_status", TrainConsts.PAY_FAIL);
					if (!TrainConsts.PAY_SUCCESS.equals(orderInfo
							.get("order_status"))) {
						orderService.updateOrderInfo(payResultMap);
					}
					logger.info("fail 支付失败");
				}
				try {
					resHandler.sendToCFT("success");
				} catch (IOException e) {
					logger.error("【支付结果通知接口】向微信支付反馈success发生异常：" + e);
				}
			} else {// sha1签名失败
				logger.info("fail -SHA1 failed");
				try {
					resHandler.sendToCFT("fail");
				} catch (IOException e) {
					logger.error("【支付结果通知接口】向微信支付反馈fail发生异常：" + e);
					logger.error("【支付结果通知接口】向微信支付反馈发生异常：" + e);
				}
			}
		} else {// MD5签名失败
			logger.info("fail -Md5 failed");
		}
	}

	/*
	 * // 通知id String notify_id = resHandler.getParameter("notify_id");
	 * 
	 * TenpayHttpClient httpClient = new TenpayHttpClient(); // 应答对象
	 * ClientResponseHandler queryRes = new ClientResponseHandler(); //
	 * 通过通知ID查询，确保通知来至财付通 queryReq.init(); queryReq.setKey(partnerKey); queryReq
	 * .setGateUrl("https://gw.tenpay.com/gateway/verifynotifyid.xml");
	 * queryReq.setParameter("partner", partnerID);
	 * queryReq.setParameter("notify_id", notify_id); // 通信对象
	 * httpClient.setTimeOut(5); // 设置请求内容 try {
	 * httpClient.setReqContent(queryReq.getRequestURL());
	 * logger.info("queryReq:" + queryReq.getRequestURL()); } catch
	 * (UnsupportedEncodingException e1) { logger.error("【微信支付结果通知接口】设置请求内容异常：" +
	 * e1); } // 后台调用 if (httpClient.call()) { // 设置结果参数 try {
	 * queryRes.setContent(httpClient.getResContent()); } catch (Exception e1) {
	 * logger.error("【微信支付结果通知接口】设置设置结果参数异常：" + e1); } logger.info("queryRes:" +
	 * httpClient.getResContent()); queryRes.setKey(partnerKey); // 获取返回参数
	 * String retcode = queryRes.getParameter("retcode"); String trade_state =
	 * queryRes.getParameter("trade_state");
	 * 
	 * String trade_mode = queryRes.getParameter("trade_mode"); // 判断签名及结果 if
	 * (queryRes.isTenpaySign() && "0".equals(retcode) &&
	 * "0".equals(trade_state) && "1".equals(trade_mode)) {
	 * logger.info("订单查询成功"); // 取结果参数做业务处理 logger.info("out_trade_no:" +
	 * queryRes.getParameter("out_trade_no") + " transaction_id:" +
	 * queryRes.getParameter("transaction_id")); logger.info("trade_state:" +
	 * queryRes.getParameter("trade_state") + " total_fee:" +
	 * queryRes.getParameter("total_fee")); //
	 * 如果有使用折扣券，discount有值，total_fee+discount=原请求的total_fee
	 * logger.info("discount:" + queryRes.getParameter("discount") + "
	 * time_end:" + queryRes.getParameter("time_end")); //
	 * ------------------------------ // 处理业务开始 //
	 * ------------------------------ // 处理数据库逻辑 String outTradeNo =
	 * queryRes.getParameter("out_trade_no"); String transactionId = queryRes
	 * .getParameter("transaction_id"); String timeEnd =
	 * queryRes.getParameter("time_end"); String totalFee =
	 * queryRes.getParameter("total_fee"); DateFormat df = new
	 * SimpleDateFormat("yyyyMMddhhmmss"); Date payTime = null; try { payTime =
	 * df.parse(timeEnd); } catch (ParseException e) {
	 * logger.error("字符串转化为日期时发生错误！"); e.printStackTrace(); } DateFormat sdf =
	 * new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); String payTimeStr =
	 * sdf.format(payTime); payResultMap.put("order_id", outTradeNo);
	 * payResultMap.put("pay_type", "33"); payResultMap.put("transaction_id",
	 * transactionId); payResultMap.put("pay_time", payTimeStr); //
	 * payResultMap.put("pay_money", totalFee); payResultMap.put("notify_id",
	 * notify_id); payResultMap.put("order_status", TrainConsts.PAY_SUCCESS); //
	 * 注意交易单不要重复处理 Map<String, String> orderInfo = orderService
	 * .queryOrderInfo(outTradeNo); if
	 * (!TrainConsts.PAY_SUCCESS.equals(orderInfo .get("order_status"))) {
	 * orderService.updateOrderInfo(payResultMap); } // 注意判断返回金额 //
	 * ------------------------------ // 处理业务完毕 //
	 * ------------------------------ try { resHandler.sendToCFT("Success"); }
	 * catch (IOException e) { logger.error("【微信支付结果通知接口】向微信支付返回信息异常：" + e); } }
	 * else { // 错误时，返回结果未签名，记录retcode、retmsg看失败详情。
	 * logger.info("查询验证签名失败或业务错误"); logger.info("retcode:" +
	 * queryRes.getParameter("retcode") + " retmsg:" +
	 * queryRes.getParameter("retmsg")); } } else {
	 * 
	 * logger.info("后台调用通信失败");
	 * 
	 * logger.info(httpClient.getResponseCode());
	 * logger.info(httpClient.getErrInfo()); // 有可能因为网络原因，请求已经处理，但未收到应答。 } }
	 * else { logger.info("通知签名验证失败"); }
	 */
	/*
	 * Map<String, String> orderInfo = orderService.queryOrderInfo(outTradeNo);
	 * if (Double.parseDouble(orderInfo.get("pay_money")) != Double
	 * .parseDouble(totalFee) / 100) { logger.error("【微信支付接收通知接口】支付金额" +
	 * Double.parseDouble(totalFee) / 100 + "与需要支付的金额" +
	 * orderInfo.get("pay_money") + "不一致， 未知错误！"); try {
	 * write2Response(response, "fail"); } catch (IOException e) {
	 * e.printStackTrace(); } return; }
	 * 
	 * orderService.updateOrderInfo(payResultMap); try {
	 * write2Response(response, "success"); } catch (IOException e) { try {
	 * write2Response(response, "fail"); } catch (IOException e1) {
	 * e1.printStackTrace(); } }
	 */

	@RequestMapping("/orderquery.jhtml")
	public String orderquery(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, String> queryMap = new HashMap<String, String>();
		Map<String, String> payResultMap = new HashMap<String, String>();
		String order_id = request.getParameter("order_id");
		Map<String, String> orderInfoMap = orderService
				.queryOrderInfo(order_id);
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
		reqHandler.setKey(this.partnerKey);
		reqHandler
				.setGateUrl("https://gw.tenpay.com/gateway/normalorderquery.xml");

		// -----------------------------
		// 设置接口参数
		// -----------------------------
		reqHandler.setParameter("partner", this.partnerID); // 商户号

		// out_trade_no和transaction_id至少一个必填，同时存在时transaction_id优先
		reqHandler.setParameter("out_trade_no", order_id); // 商家订单号
		reqHandler.setParameter("transaction_id", orderInfoMap
				.get("transaction_id")); // 财付通交易单号

		// -----------------------------
		// 设置通信参数
		// -----------------------------
		// 设置请求返回的等待时间
		httpClient.setTimeOut(5);
		httpClient.setCaInfo(new File(caPath));

		// 设置个人(商户)证书
		httpClient.setCertInfo(new File(certPath), cert_pwd);

		// 设置发送类型POST
		httpClient.setMethod("POST");

		// 设置请求内容
		String requestUrl = null;
		try {
			requestUrl = reqHandler.getRequestURL();
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		httpClient.setReqContent(requestUrl);
		String rescontent = "null";
		// 后台调用
		if (httpClient.call()) {
			// 设置结果参数
			rescontent = httpClient.getResContent();
			try {
				resHandler.setContent(rescontent);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			resHandler.setKey(this.partnerKey);

			// 获取返回参数
			String retcode = resHandler.getParameter("retcode");

			// 判断签名及结果
			if (resHandler.isTenpaySign() && "0".equals(retcode)) {
				logger.info("订单查询成功");

				// 商户订单号
				String out_trade_no = resHandler.getParameter("out_trade_no");
				// 财付通订单号
				String transaction_id = resHandler
						.getParameter("transaction_id");
				// 金额,以分为单位
				String total_fee = resHandler.getParameter("total_fee");
				// 如果有使用折扣券，discount有值，total_fee+discount=原请求的total_fee
				String discount = resHandler.getParameter("discount");
				// 支付结果
				if (!out_trade_no.equals(order_id)) {
					logger.error("【订单查询接口】返回的订单号为" + out_trade_no + "与查询的订单号"
							+ order_id + "不同");
					return null;
				}
				if (orderInfoMap.get("transaction_id") != null
						&& orderInfoMap.get("transaction_id").length() > 0
						&& !orderInfoMap.get("transaction_id").equals(
								transaction_id)) {
					logger.error("【订单查询接口】返回的微信交易号为" + transaction_id
							+ "与查询的微信交易号" + orderInfoMap.get("transaction_id")
							+ "不同");
				}
				String trade_state = resHandler.getParameter("trade_state");
				String endTime = orderInfoMap.get("end_time");
				payResultMap.put("transaction_id", transaction_id);
				payResultMap.put("order_id", order_id);
				payResultMap.put("total_fee", total_fee);
				payResultMap.put("discount", discount);
				// 支付成功
				if ("0".equals(trade_state)) {
					// 业务处理
					logger.info("transaction_id="
							+ resHandler.getParameter("transaction_id")
							+ " out_trade_no="
							+ resHandler.getParameter("out_trade_no"));
					logger.info("total_fee="
							+ resHandler.getParameter("total_fee")
							+ " discount="
							+ resHandler.getParameter("discount"));
					payResultMap.put("pay_type", "33");
					DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
					Date payTime = null;
					try {
						payTime = df.parse(endTime);
					} catch (ParseException e) {
						logger.error("字符串转化为日期时发生错误！");
						e.printStackTrace();
					}
					DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					String pay_time = sdf.format(payTime);
					payResultMap.put("pay_time", pay_time);
					payResultMap.put("order_status", TrainConsts.PAY_SUCCESS);

				} else {
					payResultMap.put("pay_type", "33");
					payResultMap.put("order_status", TrainConsts.PAY_FAIL);
				}
				orderService.updateOrderInfo(payResultMap);
			} else {
				// 错误时，返回结果未签名，记录retcode、retmsg看失败详情。
				logger.error("【订单查询接口】验证签名失败或业务错误");
				logger.error("【订单查询接口】retcode:"
						+ resHandler.getParameter("retcode") + " retmsg:"
						+ resHandler.getParameter("retmsg"));
			}
		} else {
			logger.error("【订单查询接口】后台调用通信失败");
			logger.error("【订单查询接口】" + httpClient.getResponseCode());
			logger.error("【订单查询接口】" + httpClient.getErrInfo());
			// 有可能因为网络原因，请求已经处理，但未收到应答。
		}

		// 获取debug信息,建议把请求、应答内容、debug信息，通信返回码写入日志，方便定位问题
		logger.info("【订单查询接口】http res:" + httpClient.getResponseCode() + ","
				+ httpClient.getErrInfo());
		logger.info("【订单查询接口】req url:" + requestUrl);
		logger.info("【订单查询接口】req debug:" + reqHandler.getDebugInfo());
		logger.info("【订单查询接口】res content:" + rescontent);
		logger.info("【订单查询接口】res debug:" + resHandler.getDebugInfo());
		/*
		 * String noncestr = Sha11Util.getNonceStr(); String timestamp =
		 * Sha11Util.getTimeStamp();
		 * 
		 * String encodeSign = "out_trade_no=" + order_id + "&partner=" +
		 * this.partnerID; String sign = null; try { sign =
		 * MD5SignUtil.Sign(encodeSign, this.partnerKey); } catch
		 * (SDKRuntimeException e) {
		 * logger.error("【订单支付结果查询接口】生成package中的sign时发生异常：" + e); } String
		 * packageStr = encodeSign + "&sign=" + sign; SortedMap<String, String>
		 * signParams = new TreeMap<String, String>(); signParams.put("appid",
		 * appid); signParams.put("noncestr", noncestr);
		 * signParams.put("package", packageStr); signParams.put("timestamp",
		 * timestamp); signParams.put("appkey", paySignKey); String querySign =
		 * null; try { querySign = Sha11Util.createSHA1Sign(signParams); } catch
		 * (Exception e) { logger.error("【订单支付结果查询接口】生成sign时发生异常：" + e); }
		 * 
		 * queryMap.put("package", packageStr); queryMap.put("appid", appid);
		 * queryMap.put("app_signature", querySign); queryMap.put("sign_method",
		 * "sha1"); queryMap.put("timestamp", timestamp);
		 * 
		 * String token = WeChatUtil.getAccessToken(appid, secret).getToken();
		 * String queryUrl = query_order_url.replace("xxxxxx", token); Gson gson =
		 * new Gson(); String jsonStr = gson.toJson(queryMap); JSONObject
		 * jsonObject = WeChatUtil .httpRequest(queryUrl, "get", jsonStr);
		 * logger.info("【订单支付结果查询接口】查询到的订单支付信息为：" + jsonObject.toString()); Map<String,
		 * Object> resultMap = JSONObject.fromObject(jsonObject);
		 * 
		 * logger.info("【订单支付结果查询接口】查询状态为" + (String) resultMap.get("errcode") + ",
		 * 状态信息为" + (String) resultMap.get("errmsg")); if (!"0".equals((String)
		 * resultMap.get("errcode"))) { logger.info("【订单支付结果查询接口】发生错误， 查询状态为" +
		 * (String) resultMap.get("errcode") + ", 状态信息为" + (String)
		 * resultMap.get("errmsg")); return null; } if
		 * (!"0".equals(orderInfoMap.get("ret_code"))) {
		 * logger.info("【订单支付结果查询接口】查询结果状态码为：" + orderInfoMap.get("ret_code") + ",
		 * 失败原因为" + orderInfoMap.get("ret_msg")); return null; } if
		 * (!order_id.equals(orderInfoMap.get("out_trade_no"))) {
		 * logger.error("【订单支付结果查询接口】查询到的订单号" +
		 * jsonObject.getString("out_trade_no") + ",与需要的订单号" + order_id + "不同");
		 * return null; } else { String total_fee =
		 * String.valueOf(Double.parseDouble(orderInfoMap .get("total_fee")) /
		 * 100); String discount =
		 * String.valueOf(Double.parseDouble(orderInfoMap .get("discount")) /
		 * 100); String transaction_id = orderInfoMap.get("transaction_id");
		 * String endTime = orderInfoMap.get("end_time");
		 * payResultMap.put("transaction_id", transaction_id);
		 * payResultMap.put("order_id", order_id); payResultMap.put("total_fee",
		 * total_fee); payResultMap.put("discount", discount); if
		 * ("0".equals(orderInfoMap.get("order_state"))) {
		 * payResultMap.put("pay_type", "33"); DateFormat df = new
		 * SimpleDateFormat("yyyyMMddhhmmss"); Date payTime = null; try {
		 * payTime = df.parse(endTime); } catch (ParseException e) {
		 * logger.error("字符串转化为日期时发生错误！"); e.printStackTrace(); } DateFormat sdf =
		 * new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); String pay_time =
		 * sdf.format(payTime); payResultMap.put("pay_time", pay_time);
		 * payResultMap.put("order_status", TrainConsts.PAY_SUCCESS);
		 * orderService.updateOrderInfo(payResultMap); } else {
		 * payResultMap.put("pay_type", "33"); payResultMap.put("order_status",
		 * TrainConsts.PAY_FAIL); orderService.updateOrderInfo(payResultMap); } }
		 */
		return "/query";
	}

	@RequestMapping("/delivernotify.jhtml")
	public String delivernotify(HttpServletRequest request,
			HttpServletResponse response) {
		String order_id = this.getParam(request, "order_id");
		Map<String, String> orderInfo = orderService.queryOrderInfo(order_id);
		Map<String, String> userMap = new HashMap<String, String>();
		String user_id = orderInfo.get("user_id");
		userMap.put("user_id", user_id);
		userMap = userInfoService.queryWeChartUser(userMap).get(0);
		String openID = userMap.get("openID");
		String transId = orderInfo.get("transaction_id");
		String out_trade_no = order_id;
		String deliver_timestamp = String.valueOf(new Date().getTime());
		return null;
	}
}
