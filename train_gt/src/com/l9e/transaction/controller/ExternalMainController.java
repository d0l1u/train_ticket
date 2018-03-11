package com.l9e.transaction.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jiexun.iface.util.StringUtil;
import com.l9e.common.ExternalBase;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.ChangeService;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.BookDetailInfo;
import com.l9e.transaction.vo.BookInfo;
import com.l9e.transaction.vo.ChangeInfo;
import com.l9e.transaction.vo.ChangeLogVO;
import com.l9e.transaction.vo.ChangePassengerInfo;
import com.l9e.transaction.vo.CreateNewRetrunOrderVo;
import com.l9e.transaction.vo.ExternalLogsVo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoBx;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.util.Base64Util;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.HttpsUtil;
import com.l9e.util.MathUtil;
import com.l9e.util.Md5Encrypt;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.ParamCheckUtil;
import com.l9e.util.UrlFormatUtil;

/**
 * 对外接口
 * 
 * @author zhangjc02
 * 
 */

@Controller
public class ExternalMainController extends ExternalBase {
	@Resource
	protected NewBuyTicketController newBuyTicketController;
	@Resource
	private CreateOrderController createOrder;
	@Resource
	private OrderService orderService;
	@Resource
	private QueryOrderController queryOrder;
	@Resource
	private RefundTicketController refundTicketController;
	@Resource
	private CommonService commonService;
	@Resource
	private ChangeService changeService;

	@Value("#{propertiesReader[GT_GETNOCALLORDER_KEY]}")
	private String GT_GETNOCALLORDER_KEY;

	@Value("#{propertiesReader[get_nocallorder_url]}")
	private String get_nocallorder_url;

	/**
	 * web对外接口主入口
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/externalInterface.do")
	public void ExternalInterface(HttpServletRequest request, HttpServletResponse response) {

		logger.info("请求参数列表：" + getFullURL(request));

		String type = this.getParam(request, "type");
		String method = this.getParam(request, "method");

		logger.info("【xxx】type:" + type + "-method:" + method);
		/**
		 * 有的只有type，有的只有method，有的两者都有。 因为开发接口前期以19e为主，后期gt为主
		 */
		if ("createOrder".equals(type) || "createOrder".equals(type) && "book".equals(method)) {
			// if ("createOrder".equals(type)) {
			createOrder(request, response); // 下单,11.先预定后支付,22.先支付后预定
		} else if ("buy".equals(method)) {
			confirmOrder(request, response); // 确认出票
		} else if ("cancelOrder".equals(method)) {
			cancelOrder(request, response); // 取消订单
		} else if ("queryOrderInfo".equals(type)) {
			queryOrderInfo(request, response); // 查询订单
		} else if ("refundTicket".equals(type)) {
			refundTicketController.refundTicket(request, response); // 退款
		} else if ("queryBxInfo".equals(type)) {
			queryBxInfo(request, response); // 保险信息查询
		} else if ("verifyUsers".equals(type)) {
			verifyUsers(request, response); // 验证乘客信息接口
		} else if ("requestChange".equals(type)) {
			requestChange(request, response);// 请求改签
		} else if ("cancelChange".equals(type)) {
			cancelChange(request, response);// 取消改签
		} else if ("confirmChange".equals(type)) {
			confirmChange(request, response);// 确认改签
		} else if ("getNoCallBackOrder".equals(type)) {
			getNoCallBackOrder(request, response);// 获取未回调订单
		} else {
			logger.info("hcp对外接口-非法的type接口参数 ：type=" + type);
			logger.info("请求参数异常type:" + type);
			printJson(response, getJson("003").toString());
		}
	}

	/**
	 * @param request
	 * @param response
	 *            占座完成，申请取消订单
	 */
	public void cancelOrder(HttpServletRequest request, HttpServletResponse response) {
		logger.info("取消订单接口-调用接口开始");
		String sign = this.getParam(request, "sign"); // 签名
		String partnerid = this.getParam(request, "partnerid");// 供应商id
		String json_param = this.getParam(request, "json_param");
		String reqtime = this.getParam(request, "reqtime");// 请求时间
		String method = this.getParam(request, "method");// 动作
		String reqtoken = this.getParam(request, "reqtoken");// 请求token

		String order12306 = "";// 12306订单号
		String gtgjOrderId = ""; // 高铁管家订单号
		String supplierOrderId = "";// 供应商订单号
		String type = "";// 请求类型

		// 构造返回对象
		CreateNewRetrunOrderVo cnro = new CreateNewRetrunOrderVo();
		// Gson gson = new
		// GsonBuilder().serializeNulls().create();//gson实体转json时当字段值为空时，json串中就不存在，解决方法
		Gson gson = new Gson();
		if (StringUtil.isEmpty(sign) || StringUtil.isEmpty(partnerid) || StringUtil.isEmpty(reqtime)
				|| StringUtil.isEmpty(method) || StringUtil.isEmpty(reqtoken)
				|| ParamCheckUtil.isNotCheck(reqtime, ParamCheckUtil.TIMESTAMP_REGEX)) {
			logger.info("输入参数错误或为空或格式错误。");
			cnro.setGtgjOrderId("");
			cnro.setSupplierOrderId("");
			cnro.setOrder12306("");
			cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
			cnro.setSuccess("false");
			cnro.setMsg(TrainConsts.getReturnCode().get("003"));
			String result_json = gson.toJson(cnro);
			logger.info("先预定后支付，取消接口，返回结果：" + result_json);
			printJson(response, result_json);
			return;
		}

		logger.info("高铁传入的商户id：" + partnerid);
		String merchant_id = "";
		if (TrainConsts.PARTNER_ID.equals(partnerid)) { // 线上要替换一下
			merchant_id = TrainConsts.GT_MERCHANT_ID;
		}
		logger.info("高铁转换后的商户id：" + merchant_id);

		Map<String, String> merchantSetting = commonService.queryMerchantInfo(merchant_id);
		if (merchantSetting == null) {
			cnro.setGtgjOrderId("");
			cnro.setSupplierOrderId("");
			cnro.setOrder12306("");
			cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
			cnro.setSuccess("false");
			cnro.setMsg(TrainConsts.getReturnCode().get("004"));
			String result_json = gson.toJson(cnro);
			logger.info("先预定后支付，取消接口，返回结果：" + result_json);
			printJson(response, result_json);
			return;
		}

		logger.info("取消订单接口-用户:【" + merchantSetting.get("merchant_name") + "】登入成功!");
		logger.info("取消订单接口-用户传递sign：【" + sign + "】");
		// 加密明文
		String md_str = TrainConsts.PARTNER_ID + method + reqtime
				+ Md5Encrypt.getKeyedDigestFor19Pay(merchantSetting.get("sign_key"), "", "utf-8");
		String hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(md_str, "", "utf-8");

		logger.info("取消订单接口-用户下单参数：" + md_str + ",json_param:" + json_param);
		logger.info("-系统加密sign：【" + hmac_19 + "】");
		if (!hmac_19.equals(sign)) {
			logger.info("取消订单接口-安全校验error:不符合安全校验规则。");
			cnro.setGtgjOrderId("");
			cnro.setSupplierOrderId("");
			cnro.setReqtoken(reqtoken);
			cnro.setOrder12306("");
			cnro.setSuccess("false");
			cnro.setMsg(TrainConsts.getReturnCode().get("002"));
			String result_json = gson.toJson(cnro);
			logger.info("取消订单同步返回结果：" + result_json);
			printJson(response, result_json);
			return;
		}

		try {
			// 解析json_param
			JSONObject jsonObject = JSONObject.fromObject(json_param);
			order12306 = (String) jsonObject.get("order12306");// 12306订单号
			gtgjOrderId = (String) jsonObject.get("gtgjOrderId");// 高铁管家订单号
			supplierOrderId = (String) jsonObject.get("supplierOrderId");// 供应商订单号
			type = (String) jsonObject.get("type");// 供应商订单号

			if (StringUtil.isEmpty(order12306) || StringUtil.isEmpty(gtgjOrderId) || StringUtil.isEmpty(type)
					|| StringUtil.isEmpty(supplierOrderId)) {
				logger.info("取消订单接口-json_param参数校验error:必要参数为空或格式错误!");
				cnro.setGtgjOrderId(StringUtil.isEmpty(gtgjOrderId) ? "" : gtgjOrderId);
				cnro.setSupplierOrderId(StringUtil.isEmpty(supplierOrderId) ? "" : supplierOrderId);
				cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
				cnro.setOrder12306(StringUtil.isEmpty(order12306) ? "" : order12306);
				cnro.setSuccess("false");
				cnro.setMsg(TrainConsts.getReturnCode().get("003"));
				String result_json = gson.toJson(cnro);
				logger.info("取消订单同步返回结果：" + result_json);
				printJson(response, result_json);
				return;
			}

			String key = "cancel_" + merchant_id + gtgjOrderId;
			if (null == MemcachedUtil.getInstance().getAttribute(key)) {
				logger.info("缓存当前取消商户订单号：" + key);
				MemcachedUtil.getInstance().setAttribute(key, key, 5 * 1000);
			} else {
				String str = (String) MemcachedUtil.getInstance().getAttribute(key);
				if (str.equals(key)) {
					logger.info("取消接口，重复取消,异常：" + gtgjOrderId);
					cnro.setGtgjOrderId(gtgjOrderId);
					cnro.setSupplierOrderId(supplierOrderId);
					cnro.setReqtoken(reqtoken);
					cnro.setOrder12306(order12306);
					cnro.setSuccess("false");
					cnro.setMsg("重复取消,间隔时间太短");
					String result_json = gson.toJson(cnro);
					logger.info(supplierOrderId + ",确认出票同步返回结果：" + result_json);
					printJson(response, result_json);
					return;
				}
			}

			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("merchant_id", merchant_id);
			paramMap.put("order_id", supplierOrderId);
			paramMap.put("merchant_order_id", gtgjOrderId);
			String order_status = orderService.queryOrderStatusById(paramMap);
			logger.info(supplierOrderId + ",订单状态为：" + order_status);
			if (StringUtils.isEmpty(order_status)) {
				logger.info(supplierOrderId + ",订单不存在");
				cnro.setGtgjOrderId(StringUtil.isEmpty(gtgjOrderId) ? "" : gtgjOrderId);
				cnro.setSupplierOrderId(StringUtil.isEmpty(supplierOrderId) ? "" : supplierOrderId);
				cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
				cnro.setOrder12306(StringUtil.isEmpty(order12306) ? "" : order12306);
				cnro.setSuccess("false");
				cnro.setMsg("订单不存在");
				String result_json = gson.toJson(cnro);
				logger.info("取消订单同步返回结果：" + result_json);
				printJson(response, result_json);
				return;
			}

			if (TrainConsts.BOOK_SUCCESS.equals(order_status)) {
				int count = orderService.queryEopandpaynotifyCount(supplierOrderId);
				logger.info(supplierOrderId + ",gt_orderinfo_eopandpaynotify的记录数：" + count);
				if (count >= 1) {
					logger.info(supplierOrderId + ",订单已确认出票，不可取消，请等待出票结果。");
					cnro.setGtgjOrderId(StringUtil.isEmpty(gtgjOrderId) ? "" : gtgjOrderId);
					cnro.setSupplierOrderId(StringUtil.isEmpty(supplierOrderId) ? "" : supplierOrderId);
					cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
					cnro.setOrder12306(StringUtil.isEmpty(order12306) ? "" : order12306);
					cnro.setSuccess("false");
					cnro.setMsg("订单已确认出票，不可取消，请等待出票结果");
					String result_json = gson.toJson(cnro);
					logger.info("取消订单同步返回结果：" + result_json);
					printJson(response, result_json);
				}
			}

			if (TrainConsts.OUT_SUCCESS.equals(order_status) || TrainConsts.ORDER_FINISH.equals(order_status)) {
				logger.info(supplierOrderId + ",订单已经出票成功，不可取消，只能退票。");
				cnro.setGtgjOrderId(StringUtil.isEmpty(gtgjOrderId) ? "" : gtgjOrderId);
				cnro.setSupplierOrderId(StringUtil.isEmpty(supplierOrderId) ? "" : supplierOrderId);
				cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
				cnro.setOrder12306(StringUtil.isEmpty(order12306) ? "" : order12306);
				cnro.setSuccess("false");
				cnro.setMsg("该订单内车票已经支付完成，不能取消，请选择退款流程！");
				String result_json = gson.toJson(cnro);
				logger.info("取消订单同步返回结果：" + result_json);
				printJson(response, result_json);
				return;
			} else if (TrainConsts.NO_PAY.equals(order_status) || TrainConsts.NO_PAY.equals(order_status)
					|| TrainConsts.EOP_SEND.equals(order_status) || TrainConsts.BOOKING_TICKET.equals(order_status)) {
				logger.info(supplierOrderId + ",订单正在占座，不可取消，请稍后重试");
				cnro.setGtgjOrderId(StringUtil.isEmpty(gtgjOrderId) ? "" : gtgjOrderId);
				cnro.setSupplierOrderId(StringUtil.isEmpty(supplierOrderId) ? "" : supplierOrderId);
				cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
				cnro.setOrder12306(StringUtil.isEmpty(order12306) ? "" : order12306);
				cnro.setSuccess("false");
				cnro.setMsg("订单正在占座，不可取消，请稍后重试");
				String result_json = gson.toJson(cnro);
				logger.info("取消订单同步返回结果：" + result_json);
				printJson(response, result_json);
				return;
			} else if (TrainConsts.CANCEL_SUCCESS.equals(order_status)) {
				logger.info(supplierOrderId + ",订单已经取消成功,默认返回成功");
				cnro.setGtgjOrderId(StringUtil.isEmpty(gtgjOrderId) ? "" : gtgjOrderId);
				cnro.setSupplierOrderId(StringUtil.isEmpty(supplierOrderId) ? "" : supplierOrderId);
				cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
				cnro.setOrder12306(StringUtil.isEmpty(order12306) ? "" : order12306);
				cnro.setSuccess("true");
				cnro.setMsg("取消成功");
				String result_json = gson.toJson(cnro);
				logger.info("取消订单同步返回结果：" + result_json);
				printJson(response, result_json);
				return;
			} else if (TrainConsts.ORDER_OUT_TIME.equals(order_status)) {
				logger.info(supplierOrderId + ",订单已超时，默认取消成功,返回成功");
				cnro.setGtgjOrderId(StringUtil.isEmpty(gtgjOrderId) ? "" : gtgjOrderId);
				cnro.setSupplierOrderId(StringUtil.isEmpty(supplierOrderId) ? "" : supplierOrderId);
				cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
				cnro.setOrder12306(StringUtil.isEmpty(order12306) ? "" : order12306);
				cnro.setSuccess("true");
				cnro.setMsg("取消成功");
				String result_json = gson.toJson(cnro);
				logger.info("取消订单同步返回结果：" + result_json);
				printJson(response, result_json);
				return;
			} else if (TrainConsts.OUT_FAIL.equals(order_status)) {
				logger.info(supplierOrderId + ",占座失败,不需取消");
				cnro.setGtgjOrderId(StringUtil.isEmpty(gtgjOrderId) ? "" : gtgjOrderId);
				cnro.setSupplierOrderId(StringUtil.isEmpty(supplierOrderId) ? "" : supplierOrderId);
				cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
				cnro.setOrder12306(StringUtil.isEmpty(order12306) ? "" : order12306);
				cnro.setSuccess("false");
				cnro.setMsg("出票失败,不可取消");
				String result_json = gson.toJson(cnro);
				logger.info("取消订单同步返回结果：" + result_json);
				printJson(response, result_json);
				return;
			} else if (!TrainConsts.BOOK_SUCCESS.equals(order_status)) {
				logger.info(supplierOrderId + ",订单状态不对");
				cnro.setGtgjOrderId(StringUtil.isEmpty(gtgjOrderId) ? "" : gtgjOrderId);
				cnro.setSupplierOrderId(StringUtil.isEmpty(supplierOrderId) ? "" : supplierOrderId);
				cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
				cnro.setOrder12306(StringUtil.isEmpty(order12306) ? "" : order12306);
				cnro.setSuccess("false");
				cnro.setMsg("订单状态不为占座成功,请稍后重试");
				String result_json = gson.toJson(cnro);
				logger.info("取消订单同步返回结果：" + result_json);
				printJson(response, result_json);
				return;
			}

			OrderInfo orderInfo = orderService.queryOrderInfo(supplierOrderId);
			// 更新订单状态为取消订单
			String result = HttpUtil.sendByPost(commonService.getTrainSysSettingValue("notify_cancle_interface_url"),
					"order_id=" + supplierOrderId, "UTF-8");
			logger.info(supplierOrderId + " 请求取消订单接口返回：" + result);
			if ("success".equals(result)) {
				// 请求出票系统取消成功,更新订单状态为取消成功
				Map<String, String> map = new HashMap<String, String>();
				// 通知出票系统取消成功，则订单状态修改为正在出票
				map.put("asp_order_id", supplierOrderId);
				map.put("old_status", TrainConsts.BOOK_SUCCESS);// 预订成功,33
				map.put("order_status", TrainConsts.CANCEL_SUCCESS);// 取消成功,24
				orderService.updateOrderStatus(map);
				// 同步返回，取消成功
				cnro.setGtgjOrderId(StringUtil.isEmpty(gtgjOrderId) ? "" : gtgjOrderId);
				cnro.setSupplierOrderId(StringUtil.isEmpty(supplierOrderId) ? "" : supplierOrderId);
				cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
				cnro.setOrder12306(StringUtil.isEmpty(order12306) ? "" : order12306);
				cnro.setSuccess("true");
				cnro.setMsg("取消成功");
				String result_json = gson.toJson(cnro);
				logger.info("取消订单同步返回结果：" + result_json);
				printJson(response, result_json);
				String orderOptlog = "请求取消接口成功,method:" + type + "订单类型：" + orderInfo.getOrder_type();
				logInsert(supplierOrderId, orderOptlog);
				return;
			} else {
				cnro.setGtgjOrderId(StringUtil.isEmpty(gtgjOrderId) ? "" : gtgjOrderId);
				cnro.setSupplierOrderId(StringUtil.isEmpty(supplierOrderId) ? "" : supplierOrderId);
				cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
				cnro.setOrder12306(StringUtil.isEmpty(order12306) ? "" : order12306);
				cnro.setSuccess("false");
				cnro.setMsg("该订单状态不支持取消操作,请稍后重试");
				String result_json = gson.toJson(cnro);
				logger.info("取消订单同步返回结果：" + result_json);
				printJson(response, result_json);
				return;
			}

		} catch (Exception e) {
			logger.info("取消订单操作异常！", e);
			cnro.setGtgjOrderId(StringUtil.isEmpty(gtgjOrderId) ? "" : gtgjOrderId);
			cnro.setSupplierOrderId(StringUtil.isEmpty(supplierOrderId) ? "" : supplierOrderId);
			cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
			cnro.setOrder12306(StringUtil.isEmpty(order12306) ? "" : order12306);
			cnro.setSuccess("false");
			cnro.setMsg("取消订单操作异常");
			String result_json = gson.toJson(cnro);
			logger.info("取消订单同步返回结果：" + result_json);
			printJson(response, result_json);
			return;
		}
	}

	/**
	 * @param request
	 * @param response
	 *            确认出票
	 */
	public void confirmOrder(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		logger.info("确认出票开始");
		String reqtime = this.getParam(request, "reqtime");
		String json_param = this.getParam(request, "json_param");
		String type = this.getParam(request, "method");
		String partnerid = this.getParam(request, "partnerid");// 供应商id
		String sign = this.getParam(request, "sign");
		String reqtoken = this.getParam(request, "reqtoken"); // 请求token

		String order12306 = "";// 12306订单号
		String gtgjOrderId = ""; // 高铁管家订单号
		String supplierOrderId = "";// 供应商订单号
		String payPrice = "";// 票价
		String order_result_url = "";// 确认出票通知地址

		// 构造返回对象
		CreateNewRetrunOrderVo cnro = new CreateNewRetrunOrderVo();
		// Gson gson = new
		// GsonBuilder().serializeNulls().create();//gson实体转json时当字段值为空时，json串中就不存在，解决方法
		Gson gson = new Gson();
		try {
			if (StringUtil.isEmpty(reqtoken) || StringUtil.isEmpty(json_param) || StringUtil.isEmpty(type)
					|| StringUtil.isEmpty(sign) || ParamCheckUtil.isNotCheck(reqtime, ParamCheckUtil.TIMESTAMP_REGEX)) {
				logger.info("确认出票接口-参数校验error:必要参数为空或格式错误!");
				cnro.setGtgjOrderId("");
				cnro.setSupplierOrderId("");
				cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
				cnro.setOrder12306("");
				cnro.setSuccess("false");
				cnro.setMsg(TrainConsts.getReturnCode().get("003"));
				String result_json = gson.toJson(cnro);
				logger.info("确认出票同步返回结果：" + result_json);
				printJson(response, result_json);
				return;
			}

			logger.info("高铁传入的商户id：" + partnerid);
			String merchant_id = "";
			if (TrainConsts.PARTNER_ID.equals(partnerid)) { // 线上要替换一下
				merchant_id = TrainConsts.GT_MERCHANT_ID;
			}
			logger.info("高铁转换后的商户id：" + merchant_id);

			Map<String, String> merchantSetting = commonService.queryMerchantInfo(merchant_id);
			if (merchantSetting == null) {
				logger.info("确认出票接口-用户身份校验error:不存在的用户!");
				cnro.setGtgjOrderId("");
				cnro.setSupplierOrderId("");
				cnro.setReqtoken(reqtoken);
				cnro.setOrder12306("");
				cnro.setSuccess("false");
				cnro.setMsg(TrainConsts.getReturnCode().get("004"));
				String result_json = gson.toJson(cnro);
				logger.info("确认出票同步返回结果：" + result_json);
				printJson(response, result_json);
				return;
			}

			logger.info("确认出票接口-用户:【" + merchantSetting.get("merchant_name") + "】登入成功!");
			logger.info("确认出票接口-用户传递sign：【" + sign + "】");
			// 加密明文
			String md_str = TrainConsts.PARTNER_ID + type + reqtime
					+ Md5Encrypt.getKeyedDigestFor19Pay(merchantSetting.get("sign_key"), "", "utf-8");
			String hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(md_str, "", "utf-8");

			logger.info("确认出票接口-用户下单参数：" + md_str + ",json_param:" + json_param);
			logger.info("-系统加密sign：【" + hmac_19 + "】");
			if (!hmac_19.equals(sign)) {
				logger.info("确认出票接口-安全校验error:不符合安全校验规则。");
				cnro.setGtgjOrderId("");
				cnro.setSupplierOrderId("");
				cnro.setReqtoken(reqtoken);
				cnro.setOrder12306("");
				cnro.setSuccess("false");
				cnro.setMsg(TrainConsts.getReturnCode().get("002"));
				String result_json = gson.toJson(cnro);
				logger.info("确认出票同步返回结果：" + result_json);
				printJson(response, result_json);
				return;
			}

			// 解析json_param
			JSONObject json_param_object = JSONObject.fromObject(json_param);
			order12306 = (String) json_param_object.get("order12306");// 12306订单号
			gtgjOrderId = (String) json_param_object.get("gtgjOrderId");// 高铁管家订单号
			supplierOrderId = (String) json_param_object.get("supplierOrderId");// 供应商订单号
			payPrice = (String) json_param_object.get("payPrice"); // 票价
			order_result_url = (String) json_param_object.get("order_result_url"); // 确认出票通知地址

			if (StringUtil.isEmpty(order12306) || StringUtil.isEmpty(gtgjOrderId) || StringUtil.isEmpty(supplierOrderId)
					|| StringUtil.isEmpty(order_result_url)
					|| ParamCheckUtil.isNotCheck(payPrice, ParamCheckUtil.PRICE_TYPE)) {
				logger.info("确认出票接口-json_param参数校验error:必要参数为空或格式错误!");
				cnro.setGtgjOrderId(StringUtil.isEmpty(gtgjOrderId) ? "" : gtgjOrderId);
				cnro.setSupplierOrderId(StringUtil.isEmpty(supplierOrderId) ? "" : supplierOrderId);
				cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
				cnro.setOrder12306(StringUtil.isEmpty(order12306) ? "" : order12306);
				cnro.setSuccess("false");
				cnro.setMsg(TrainConsts.getReturnCode().get("003"));
				String result_json = gson.toJson(cnro);
				logger.info("确认出票同步返回结果：" + result_json);
				printJson(response, result_json);
				return;
			}
			OrderInfo orderInfo = orderService.queryOrderInfo(supplierOrderId);
			if (orderInfo == null || !orderInfo.getOrder_status().equals(TrainConsts.BOOK_SUCCESS)) {// 非占座成功
				// 订单状态不正确
				logger.info("高铁管家-确认出票接口ERROR,订单状态不正确order_id:" + supplierOrderId);
				cnro.setGtgjOrderId(gtgjOrderId);
				cnro.setSupplierOrderId(supplierOrderId);
				cnro.setReqtoken(reqtoken);
				cnro.setOrder12306(order12306);
				cnro.setSuccess("false");
				cnro.setMsg(TrainConsts.getReturnCode().get("802"));
				String result_json = gson.toJson(cnro);
				logger.info(supplierOrderId + ",确认出票同步返回结果：" + result_json);
				printJson(response, result_json);
				return;
			}
			// 超过约定支付期，不允许支付
			String book_limit_time = merchantSetting.get("book_limit_time");// 28min
			String book_ticket_time = orderInfo.getOut_ticket_time();
			logger.info(
					supplierOrderId + ",book_limit_time:" + book_limit_time + "book_ticket_time:" + book_ticket_time);
			if (null != book_ticket_time) {
				long diff = DateUtil.minuteDiff(new Date(),
						DateUtil.stringToDate(book_ticket_time, DateUtil.DATE_FMT3));
				if (diff > Integer.valueOf(book_limit_time)) {
					cnro.setGtgjOrderId(gtgjOrderId);
					cnro.setSupplierOrderId(supplierOrderId);
					cnro.setReqtoken(reqtoken);
					cnro.setOrder12306(order12306);
					cnro.setSuccess("false");
					cnro.setMsg(TrainConsts.getReturnCode().get("604"));
					String result_json = gson.toJson(cnro);
					logger.info(supplierOrderId + ",确认出票同步返回结果：" + result_json);
					printJson(response, result_json);
					return;
				}
			}
			String key = "pay_" + partnerid + orderInfo.getMerchant_order_id() + supplierOrderId;
			if (null == MemcachedUtil.getInstance().getAttribute(key)) {
				logger.info(supplierOrderId + "," + "缓存当前支付商户订单号：" + key);
				MemcachedUtil.getInstance().setAttribute(key, key, 5 * 1000);
			} else {
				String str = (String) MemcachedUtil.getInstance().getAttribute(key);
				if (str.equals(key)) {
					logger.info(supplierOrderId + ",购票支付接口-支付规则error:重复支付异常：" + orderInfo.getMerchant_order_id() + str
							+ "," + key);
					cnro.setGtgjOrderId(gtgjOrderId);
					cnro.setSupplierOrderId(supplierOrderId);
					cnro.setReqtoken(reqtoken);
					cnro.setOrder12306(order12306);
					cnro.setSuccess("false");
					cnro.setMsg(TrainConsts.getReturnCode().get("602"));
					String result_json = gson.toJson(cnro);
					logger.info(supplierOrderId + ",确认出票同步返回结果：" + result_json);
					printJson(response, getJson("602").toString());
					return;
				}
			}
			Map<String, String> map = orderService.queryCpSizeAndPrice(orderInfo.getOrder_id());// 订单成本总价,buy_money
			BigDecimal total_buy_money = MathUtil.stringTOBigDecimal(map.get("buy_money"), 2);
			BigDecimal payPriceDecimal = MathUtil.stringTOBigDecimal(payPrice, 2);

			if (!MathUtil.compareToBigDecimal(total_buy_money, payPriceDecimal)) {// 价格不匹配
				cnro.setGtgjOrderId(StringUtil.isEmpty(gtgjOrderId) ? "" : gtgjOrderId);
				cnro.setSupplierOrderId(StringUtil.isEmpty(supplierOrderId) ? "" : supplierOrderId);
				cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
				cnro.setOrder12306(StringUtil.isEmpty(order12306) ? "" : order12306);
				cnro.setSuccess("false");
				cnro.setMsg("与实际预定总金额不符");
				String result_json = gson.toJson(cnro);
				logger.info("确认出票同步返回结果：" + result_json);
				printJson(response, result_json);
				return;
			}

			if ("11".equals(merchantSetting.get("pay_type"))) {
				logger.info("*************");
			} else if ("22".equals(merchantSetting.get("pay_type"))) {// 先预定后支付
				try {
					// 校验是否已经提交过,确认出票请求,通知出票系统，出票的通知记录数
					int count = orderService.queryEopandpaynotifyCount(supplierOrderId);
					logger.info(supplierOrderId + ",gt_orderinfo_eopandpaynotify的记录数：" + count);
					if (count >= 1) {
						cnro.setGtgjOrderId(StringUtil.isEmpty(gtgjOrderId) ? "" : gtgjOrderId);
						cnro.setSupplierOrderId(StringUtil.isEmpty(supplierOrderId) ? "" : supplierOrderId);
						cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
						cnro.setOrder12306(StringUtil.isEmpty(order12306) ? "" : order12306);
						cnro.setSuccess("false");
						cnro.setMsg("已经确认出票，拒绝重复确认");
						String result_json = gson.toJson(cnro);
						logger.info("确认出票同步返回结果：" + result_json);
						printJson(response, result_json);
						return;
					}

					// 插入确认出票通知记录，请求出票系统支付
					Map<String, Object> paraMap = new HashMap<String, Object>();
					paraMap.put("order_id", supplierOrderId);
					paraMap.put("reqtoken", reqtoken);
					logger.info("原有的reqtoken:" + orderInfo.getReqtoken() + ",传入的reqtoken:" + reqtoken);
					// 更新确认确认出票的reqtoken
					int num = orderService.updateOrderReqtoken(paraMap);
					Map<String, Object> paraMap1 = new HashMap<String, Object>();
					paraMap1.put("order_id", supplierOrderId);
					paraMap1.put("order_result_url", order_result_url);
					int num1 = orderService.updateOrderConfirmNotifyUrl(paraMap1);
					logger.info("更新" + supplierOrderId + ",的reqtoken:" + reqtoken + ",更新成功:" + num + ","
							+ order_result_url + ",更新成功：" + num1);

					orderService.startNotifyPayOrder(orderInfo.getOrder_id(), String.valueOf(map.get("buy_money")),
							merchantSetting.get("pay_type"));

				} catch (Exception e) {
					logger.info("插入支付信息通知失败！", e);
					cnro.setGtgjOrderId(StringUtil.isEmpty(gtgjOrderId) ? "" : gtgjOrderId);
					cnro.setSupplierOrderId(StringUtil.isEmpty(supplierOrderId) ? "" : supplierOrderId);
					cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
					cnro.setOrder12306(StringUtil.isEmpty(order12306) ? "" : order12306);
					cnro.setSuccess("false");
					cnro.setMsg(TrainConsts.getReturnCode().get("001"));
					String result_json = gson.toJson(cnro);
					logger.info("确认出票同步返回结果：" + result_json);
					printJson(response, result_json);
					return;
				}
			}
			// 处理完成，返回确认成功
			logger.info(gtgjOrderId + ",处理完成，返回确认成功");
			cnro.setGtgjOrderId(StringUtil.isEmpty(gtgjOrderId) ? "" : gtgjOrderId);
			cnro.setSupplierOrderId(StringUtil.isEmpty(supplierOrderId) ? "" : supplierOrderId);
			cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
			cnro.setOrder12306(StringUtil.isEmpty(order12306) ? "" : order12306);
			cnro.setSuccess("true");
			cnro.setMsg("成功");
			String result_json = gson.toJson(cnro);
			logger.info("确认出票同步返回结果：" + result_json);
			printJson(response, result_json);

			String orderOptlog = "请求确认出票成功,method:" + type + "订单类型：" + orderInfo.getOrder_type();
			logInsert(supplierOrderId, orderOptlog);

		} catch (Exception e) {
			logger.info("支付订单接口-Exception:支付订单失败!", e);
			cnro.setGtgjOrderId(StringUtil.isEmpty(gtgjOrderId) ? "" : gtgjOrderId);
			cnro.setSupplierOrderId(StringUtil.isEmpty(supplierOrderId) ? "" : supplierOrderId);
			cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
			cnro.setOrder12306(StringUtil.isEmpty(order12306) ? "" : order12306);
			cnro.setSuccess("false");
			cnro.setMsg(TrainConsts.getReturnCode().get("001"));
			String result_json = gson.toJson(cnro);
			logger.info("确认出票同步返回结果：" + result_json);
			printJson(response, result_json);
		}

	}

	/**
	 * @param supplierOrderId
	 * @param orderOptlog
	 */
	public void logInsert(String supplierOrderId, String orderOptlog) {
		ExternalLogsVo logs = new ExternalLogsVo();
		logs.setOrder_id(supplierOrderId);
		logs.setOpter("gt_app");
		logs.setOrder_optlog(orderOptlog);
		orderService.insertOrderLogs(logs);
	}

	/**
	 * @param orderInfo
	 * @return
	 */
	public boolean isPayLimitTime(String time) {
		/* 22:34:59 */
		Calendar time_22_44_59 = Calendar.getInstance();
		time_22_44_59.set(Calendar.HOUR_OF_DAY, 22);
		time_22_44_59.set(Calendar.MINUTE, 44);
		time_22_44_59.set(Calendar.SECOND, 59);
		/* 23:30:00 */
		Calendar time_23_30_00 = Calendar.getInstance();
		time_23_30_00.set(Calendar.HOUR_OF_DAY, 23);
		time_23_30_00.set(Calendar.MINUTE, 30);
		time_23_30_00.set(Calendar.SECOND, 00);
		Calendar currentTime = Calendar.getInstance();
		Calendar bookTime = Calendar.getInstance();
		Date book = DateUtil.stringToDate(time, DateUtil.DATE_FMT3);
		bookTime.setTime(book);

		boolean timeOut = false;
		if (bookTime.before(time_22_44_59)) {// 当天22:44:59之前预订
			/* 30分钟的付款时间 */
			System.out.println("current : " + DateUtil.dateToString(currentTime.getTime(), "yyyy-MM-dd HH:mm:ss"));
			System.out.println("book : " + DateUtil.dateToString(book, "yyyy-MM-dd HH:mm:ss"));
			if (DateUtil.minuteDiff(currentTime.getTime(), book) > 30) {
				timeOut = true;
			}
			logger.info("当天22:44:59之前预订,30分钟的付款时间,timeout" + timeOut);
		} else if (bookTime.after(time_22_44_59)) {// 当天22:44:59之后预订
			/* 23:30:00之前付款 */
			if (currentTime.after(time_23_30_00)) {
				timeOut = true;
			}
			logger.info("当天22:44:59之后预订,23:30:00之前付款,timeout" + timeOut);
		}
		return timeOut;
	}

	/**
	 * 保险信息查询
	 * 
	 * @param request
	 * @param response
	 */
	public void queryBxInfo(HttpServletRequest request, HttpServletResponse response) {
		String terminal = this.getParam(request, "terminal");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String type = this.getParam(request, "type");
		String version = this.getParam(request, "version");
		String hmac = this.getParam(request, "hmac");
		String order_id = this.getParam(request, "order_id");
		if (StringUtil.isEmpty(order_id) || StringUtil.isEmpty(terminal) || StringUtil.isEmpty(merchant_id)
				|| StringUtil.isEmpty(timestamp) || StringUtil.isEmpty(type) || StringUtil.isEmpty(version)
				|| StringUtil.isEmpty(hmac)) {
			logger.info("保险信息查询接口-参数校验error:输入参数为空!");
			printJson(response, getJson("003").toString());
			return;
		}
		Map<String, String> merchantSetting = commonService.queryMerchantInfo(merchant_id);
		if (merchantSetting == null) {
			logger.info("保险信息查询接口-用户身份校验error:不存在的用户!");
			printJson(response, getJson("004").toString());
			return;
		}
		String md_str = terminal + merchant_id + timestamp + type + version + order_id;
		String hmac_19 = "";
		if ("net".equals(merchantSetting.get("md5_type"))) {
			hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(md_str + merchantSetting.get("sign_key"), "", "utf-8");
		} else {
			hmac_19 = Md5Encrypt.encodeMD5Hex(md_str + merchantSetting.get("sign_key"));
		}
		logger.info("保险信息查询接口-用户传递参数：" + md_str + merchantSetting.get("sign_key"));
		logger.info("保险信息查询接口-系统加密hmac：【" + hmac_19 + "】");
		if (!hmac_19.equals(hmac)) {
			logger.info("保险信息查询接口-安全校验error:不符合安全校验规则。");
			printJson(response, getJson("002").toString());
			return;
		}
		/** 查询保险信息 */
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_id", order_id);
		paramMap.put("merchant_id", merchant_id);
		paramMap.put("order_channel", "ext");// 对外商户标识
		List<OrderInfoBx> bxInfos = new ArrayList<OrderInfoBx>();
		bxInfos = orderService.queryBxInfosById(paramMap);
		if (bxInfos.size() == 0) {
			logger.info("没有找到该订单下的保险信息" + order_id);
			printJson(response, getJson("701").toString());
			return;
		}
		JSONObject json = new JSONObject();
		json.put("order_id", order_id);
		JSONArray arr = new JSONArray();
		for (OrderInfoBx bxInfo : bxInfos) {
			Map<String, String> returnInfo = new HashMap<String, String>();
			returnInfo.put("train_no", bxInfo.getTrain_no());
			returnInfo.put("buy_money", bxInfo.getBuy_money());
			returnInfo.put("user_name", bxInfo.getUser_name());
			returnInfo.put("user_ids", bxInfo.getUser_ids());
			returnInfo.put("telephone", bxInfo.getTelephone());
			returnInfo.put("bx_code", bxInfo.getBx_code());
			returnInfo.put("bx_status", "2".equals(bxInfo.getBx_status()) ? "SUCCESS" : "FAILURE");
			arr.add(returnInfo);
		}
		json.put("bxInfos", arr);
		System.out.println("订单保险信息返回结果：" + json.toString());
		printJson(response, json.toString());
	}

	// 购票下单接口
	public void createOrder(HttpServletRequest request, HttpServletResponse response) {
		String terminal = this.getParam(request, "terminal");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String type = this.getParam(request, "type");
		String version = this.getParam(request, "version");
		String json_param = this.getParam(request, "json_param");
		String hmac = this.getParam(request, "hmac");
		// 和先付款后买票的区别就是新增了do_type参数和method参数，method的值是book,do_type是1时，就是先下单后付款，否则就是先付款后买票
		String method = this.getParam(request, "method");
		String do_type = this.getParam(request, "do_type");
		String reqtoken = this.getParam(request, "reqtoken");

		if (StringUtils.isEmpty(method) || StringUtils.isEmpty(do_type)
				|| !("book".equals(method) && "1".equals(do_type))) {// 先付款后买票
			try {
				if (StringUtil.isEmpty(terminal) || StringUtil.isEmpty(merchant_id) || StringUtil.isEmpty(timestamp)
						|| StringUtil.isEmpty(type) || StringUtil.isEmpty(version) || StringUtil.isEmpty(hmac)
						|| StringUtil.isEmpty(json_param)
						|| ParamCheckUtil.isNotCheck(timestamp, ParamCheckUtil.TIMESTAMP_REGEX)) {
					logger.info("购票下单接口-参数校验error:必要参数为空或格式错误!");
					printJson(response, getJson("003").toString());
					return;
				}

				Map<String, String> merchantSetting = commonService.queryMerchantInfo(merchant_id);
				if (merchantSetting == null) {
					logger.info("购票下单接口-用户身份校验error:不存在的用户!");
					printJson(response, getJson("004").toString());
					return;
				}

				// 区分先预定后支付
				merchantSetting.put("method", method);
				merchantSetting.put("do_type", do_type);

				logger.info("购票下单接口-用户:【" + merchantSetting.get("merchant_name") + "】登入成功!");
				logger.info("购票下单接口-用户传递hmac：【" + hmac + "】");
				// 加密明文
				String md_str = terminal + merchant_id + timestamp + type + version + json_param + method + do_type;

				String hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(md_str + merchantSetting.get("sign_key"), "",
						"utf-8");
				logger.info("购票下单接口-用户下单参数：" + md_str);
				logger.info("-系统加密hmac：【" + hmac_19 + "】");
				if (!hmac_19.equals(hmac)) {
					logger.info("购票下单接口-安全校验error:不符合安全校验规则。");
					printJson(response, getJson("002").toString());
					return;
				}
				ObjectMapper mapper = new ObjectMapper();
				BookInfo bookInfo = null;
				JSONArray users = new JSONArray();
				if ("00".equals(merchantSetting.get("merchant_status"))) {
					logger.info("购票下单接口-下单规则error:合作商户已禁用，请客服人员通知商户！");
					printJson(response, getJson("007").toString());
					return;
				}
				bookInfo = mapper.readValue(json_param, BookInfo.class);
				String key = merchant_id + bookInfo.getMerchant_order_id();
				if (null == MemcachedUtil.getInstance().getAttribute(key)) {
					logger.info("缓存当前商户订单号：" + key);
					MemcachedUtil.getInstance().setAttribute(key, key, 5 * 1000);
				} else {
					String str = (String) MemcachedUtil.getInstance().getAttribute(key);
					if (str.equals(key)) {
						logger.info("购票下单接口-下单规则error:重复下单异常：" + bookInfo.getMerchant_order_id());
						printJson(response, getJson("201").toString());
						return;
					}
				}
				logger.info("购票下单接口-用户下单参数校验开始");
				if (ParamCheckUtil.createOrderParamIsEmpty(bookInfo)) {
					logger.info("购票下单接口-下单参数error:下订单参数为空!");
					printJson(response, getJson("204").toString());
					return;
				}
				if (ParamCheckUtil.createOrderParamCheck(bookInfo)) {
					logger.info("购票下单接口-下单参数error:下订单参数格式错误!");
					printJson(response, getJson("204").toString());
					return;
				}
				if (ParamCheckUtil.bookDetailInfoCheck(bookInfo.getBook_detail_list(), bookInfo.getOrder_level())) {
					logger.info("购票下单接口-下单参数error:订单内乘客信息参数为空或者格式错误!");
					printJson(response, getJson("205").toString());
					return;
				}

				if (bookInfo.getBook_detail_list().size() > 5) {
					logger.info("购票下单接口-下单规则error:下单票数异常");
					printJson(response, getJson("203").toString());
					return;
				}
				// 进行身份校验
				for (BookDetailInfo user : bookInfo.getBook_detail_list()) {
					JSONObject json = new JSONObject();
					json.put("cert_no", user.getUser_ids());
					json.put("cert_type", user.getIds_type());
					json.put("user_name", user.getUser_name());
					users.add(json);
				}
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("command", "verify");// 请求核验用户信息接口
				maps.put("passengers", users.toString());
				maps.put("channel", "qunar");
				String result = "";
				String fail_pas = "";
				// try{
				// String reqParams = UrlFormatUtil.CreateUrl("", maps, "",
				// "UTF-8");
				// result = HttpUtil.sendByPost(real_name_verify_url, reqParams,
				// "UTF-8");
				// logger.info("核验结果:"+result);
				// JSONArray jsonArr = JSONArray.fromObject(result);
				// int index = jsonArr.size();
				// for(int i=0;i<index;i++){
				// if(!"0".equals((jsonArr.getJSONObject(i).get("check_status")))){
				// fail_pas += jsonArr.getJSONObject(i).get("user_name")+",";
				// }
				// }
				// }catch(Exception e){
				// logger.info("核验用户信息失败！默认成功",e);
				// }
				logger.info("高铁管家不核验用户信息，默认成功！");
				JSONObject returnJson = new JSONObject();
				if ("".equals(fail_pas)) {
					createOrder.createTrainOrder(merchantSetting, bookInfo, request, response);
				} else {
					returnJson.put("return_code", "203");
					returnJson.put("message", "以下用户：" + fail_pas + "未通过铁路系统审核");
					printJson(response, returnJson.toString());
				}
			} catch (Exception e) {
				logger.error("购票下单接口-下单异常exception:第三方下单失败", e);
				printJson(response, getJson("001").toString());
			}

		} else { // 先下单后付款
			// 构造返回对象
			CreateNewRetrunOrderVo cnro = new CreateNewRetrunOrderVo();
			// Gson gson = new
			// GsonBuilder().serializeNulls().create();//gson实体转json时当字段值为空时，json串中就不存在，解决方法
			Gson gson = new Gson();
			try {
				if (StringUtil.isEmpty(terminal) || StringUtil.isEmpty(merchant_id) || StringUtil.isEmpty(timestamp)
						|| StringUtil.isEmpty(type) || StringUtil.isEmpty(version) || StringUtil.isEmpty(hmac)
						|| StringUtil.isEmpty(json_param)
						|| ParamCheckUtil.isNotCheck(timestamp, ParamCheckUtil.TIMESTAMP_REGEX)
						|| StringUtil.isEmpty(method) || StringUtil.isEmpty(do_type)) {
					logger.info("购票下单接口-参数校验error:必要参数为空或格式错误!");
					cnro.setGtgjOrderId("");
					cnro.setSupplierOrderId("");
					cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
					cnro.setSuccess("false");
					cnro.setMsg(TrainConsts.getReturnCode().get("003"));
					String result_json = gson.toJson(cnro);
					logger.info("先预定后支付，下单接口，返回结果：" + result_json);
					printJson(response, result_json);
					return;
				}

				Map<String, String> merchantSetting = commonService.queryMerchantInfo(merchant_id);
				if (merchantSetting == null) {
					logger.info("购票下单接口-用户身份校验error:不存在的用户!");
					cnro.setGtgjOrderId("");
					cnro.setSupplierOrderId("");
					cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
					cnro.setSuccess("false");
					cnro.setMsg(TrainConsts.getReturnCode().get("004"));
					String result_json = gson.toJson(cnro);
					logger.info("先预定后支付，下单接口，返回结果：" + result_json);
					printJson(response, result_json);
					return;
				}

				// 区分先预定后支付
				merchantSetting.put("method", method);
				merchantSetting.put("do_type", do_type);
				merchantSetting.put("reqtoken", reqtoken);

				logger.info("购票下单接口-用户:【" + merchantSetting.get("merchant_name") + "】登入成功!");
				logger.info("购票下单接口-用户传递hmac：【" + hmac + "】");
				// 加密明文
				String md_str = terminal + merchant_id + timestamp + type + version + json_param + method + do_type;

				String hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(md_str + merchantSetting.get("sign_key"), "",
						"utf-8");
				logger.info("购票下单接口-用户下单参数：" + md_str);
				logger.info("-系统加密hmac：【" + hmac_19 + "】");
				if (!hmac_19.equals(hmac)) {
					logger.info("购票下单接口-安全校验error:不符合安全校验规则。");
					cnro.setGtgjOrderId("");
					cnro.setSupplierOrderId("");
					cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
					cnro.setSuccess("false");
					cnro.setMsg(TrainConsts.getReturnCode().get("002"));
					String result_json = gson.toJson(cnro);
					printJson(response, result_json);
					return;
				}
				ObjectMapper mapper = new ObjectMapper();
				BookInfo bookInfo = null;
				JSONArray users = new JSONArray();
				if ("00".equals(merchantSetting.get("merchant_status"))) {
					logger.info("购票下单接口-下单规则error:合作商户已禁用，请客服人员通知商户！");
					cnro.setGtgjOrderId("");
					cnro.setSupplierOrderId("");
					cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
					cnro.setSuccess("false");
					cnro.setMsg(TrainConsts.getReturnCode().get("007"));
					String result_json = gson.toJson(cnro);
					printJson(response, result_json);
					return;
				}
				bookInfo = mapper.readValue(json_param, BookInfo.class);

				logger.info("购票下单接口-用户下单参数校验开始");
				if (ParamCheckUtil.createOrderParamIsEmpty(bookInfo)) {
					logger.info("购票下单接口-下单参数error:下订单参数为空!");
					cnro.setGtgjOrderId("");
					cnro.setSupplierOrderId("");
					cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
					cnro.setSuccess("false");
					cnro.setMsg(TrainConsts.getReturnCode().get("204"));
					String result_json = gson.toJson(cnro);
					printJson(response, result_json);
					return;
				}
				if (ParamCheckUtil.createOrderParamCheck(bookInfo)) {
					logger.info("购票下单接口-下单参数error:下订单参数格式错误!");
					cnro.setGtgjOrderId("");
					cnro.setSupplierOrderId("");
					cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
					cnro.setSuccess("false");
					cnro.setMsg(TrainConsts.getReturnCode().get("204"));
					String result_json = gson.toJson(cnro);
					printJson(response, result_json);
					return;
				}
				if (ParamCheckUtil.bookDetailInfoCheck(bookInfo.getBook_detail_list(), bookInfo.getOrder_level())) {
					logger.info("购票下单接口-下单参数error:订单内乘客信息参数为空或者格式错误!");
					cnro.setGtgjOrderId("");
					cnro.setSupplierOrderId("");
					cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
					cnro.setSuccess("false");
					cnro.setMsg(TrainConsts.getReturnCode().get("205"));
					String result_json = gson.toJson(cnro);
					printJson(response, result_json);
					return;
				}

				if (bookInfo.getBook_detail_list().size() > 5) {
					logger.info("购票下单接口-下单规则error:下单票数异常");
					cnro.setGtgjOrderId("");
					cnro.setSupplierOrderId("");
					cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
					cnro.setSuccess("false");
					cnro.setMsg(TrainConsts.getReturnCode().get("203"));
					String result_json = gson.toJson(cnro);
					printJson(response, result_json);
					return;
				}
				logger.info("参数校验完成，开始入库");

				String key = merchant_id + bookInfo.getMerchant_order_id();
				if (null == MemcachedUtil.getInstance().getAttribute(key)) {
					logger.info("缓存当前商户订单号：" + key);
					MemcachedUtil.getInstance().setAttribute(key, key, 5 * 1000);
				} else {
					String str = (String) MemcachedUtil.getInstance().getAttribute(key);
					if (str.equals(key)) {
						logger.info("购票下单接口-下单规则error:重复下单异常：" + bookInfo.getMerchant_order_id());
						cnro.setGtgjOrderId(bookInfo.getMerchant_order_id());
						cnro.setSupplierOrderId("");
						cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
						cnro.setSuccess("false");
						cnro.setMsg(TrainConsts.getReturnCode().get("201"));
						String result_json = gson.toJson(cnro);
						printJson(response, result_json);
						return;
					}
				}

				createOrder.createTrainOrder(merchantSetting, bookInfo, request, response);

			} catch (Exception e) {
				logger.info("购票下单接口-下单异常exception:第三方下单失败", e);
				cnro.setGtgjOrderId("");
				cnro.setSupplierOrderId("");
				cnro.setReqtoken(StringUtil.isEmpty(reqtoken) ? "" : reqtoken);
				cnro.setSuccess("false");
				cnro.setMsg(TrainConsts.getReturnCode().get("001"));
				String result_json = gson.toJson(cnro);
				printJson(response, result_json);
			}
			// --------------------------------//
		}

	}

	// 查询订单信息
	public void queryOrderInfo(HttpServletRequest request, HttpServletResponse response) {
		logger.info("查询订单信息接口-调用接口开始");
		String terminal = this.getParam(request, "terminal");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String type = this.getParam(request, "type");
		String version = this.getParam(request, "version");
		String order_id = this.getParam(request, "order_id");
		String merchant_order_id = this.getParam(request, "merchant_order_id");
		String hmac = this.getParam(request, "hmac");
		logger.info("查询订单信息接口-参数校验开始");
		if (StringUtil.isEmpty(terminal) || StringUtil.isEmpty(merchant_id) || StringUtil.isEmpty(timestamp)
				|| StringUtil.isEmpty(type) || StringUtil.isEmpty(version) || StringUtil.isEmpty(hmac)
				|| StringUtil.isEmpty(order_id)
				|| ParamCheckUtil.isNotCheck(timestamp, ParamCheckUtil.TIMESTAMP_REGEX)) {
			logger.info("查询订单信息接口-参数校验error:必要参数或为空或格式错误!");
			printJson(response, getJson("003").toString());
			return;
		}

		logger.info("查询订单信息接口-用户身份校验开始");
		Map<String, String> merchantSetting = commonService.queryMerchantInfo(merchant_id);
		if (merchantSetting == null) {
			logger.info("查询订单信息接口-用户身份校验error:不存在该用户!");
			printJson(response, getJson("004").toString());
			return;
		}

		logger.info("查询订单信息接口-用户:【" + merchantSetting.get("merchant_name") + "】接入成功!");

		logger.info("查询订单信息接口-安全校验开始");
		logger.info("查询订单信息接口-用户传递hmac：" + hmac);
		// 加密明文
		String md_str = terminal + merchant_id + timestamp + type + version + order_id + merchant_order_id;
		String hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(md_str + merchantSetting.get("sign_key"), "", "utf-8");
		logger.info("查询订单信息接口-用户传递查询参数：" + md_str);
		logger.info("查询订单信息接口-系统加密后hmac：" + hmac_19);
		if (!hmac_19.equals(hmac)) {
			printJson(response, getJson("002").toString());
		} else {
			queryOrder.queryOrderList(request, response, order_id, merchant_order_id);
		}
	}

	// 验证乘客信息接口
	public void verifyUsers(HttpServletRequest request, HttpServletResponse response) {
		logger.info("验证乘客信息接口-调用接口开始");
		String terminal = this.getParam(request, "terminal");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String type = this.getParam(request, "type");
		String version = this.getParam(request, "version");
		// String ids_type = this.getParam(request, "ids_type");
		// String user_ids = this.getParam(request, "user_ids");
		// String user_name = this.getParam(request, "user_name");
		String json_param = this.getParam(request, "json_param");
		String hmac = this.getParam(request, "hmac");
		String md_str = terminal + merchant_id + timestamp + type + version + json_param;
		logger.info("验证乘客信息接口-用户参数：" + md_str);
		try {
			if (StringUtil.isEmpty(terminal) || StringUtil.isEmpty(merchant_id) || StringUtil.isEmpty(timestamp)
					|| StringUtil.isEmpty(type) || StringUtil.isEmpty(version) || StringUtil.isEmpty(hmac)
					|| StringUtil.isEmpty(json_param)
					|| ParamCheckUtil.isNotCheck(timestamp, ParamCheckUtil.TIMESTAMP_REGEX)) {
				logger.info("验证乘客信息接口-参数校验error:必要参数为空或格式错误!");
				printJson(response, getJson("003").toString());
				return;
			}

			Map<String, String> merchantSetting = commonService.queryMerchantInfo(merchant_id);
			if (merchantSetting == null) {
				logger.info("验证乘客信息接口-用户身份校验error:不存在的用户!");
				printJson(response, getJson("004").toString());
				return;
			}

			logger.info("验证乘客信息接口-用户:【" + merchantSetting.get("merchant_name") + "】登入成功!");
			logger.info("验证乘客信息接口-用户传递hmac：【" + hmac + "】");
			// 加密明文

			String hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(md_str + merchantSetting.get("sign_key"), "", "utf-8");

			logger.info("验证乘客信息接口-系统加密hmac：【" + hmac_19 + "】");
			if (!hmac_19.equals(hmac)) {
				logger.info("验证乘客信息接口-安全校验error:不符合安全校验规则。");
				printJson(response, getJson("002").toString());
				return;
			}
			if ("00".equals(merchantSetting.get("merchant_status"))
					|| "00".equals(merchantSetting.get("verify_status"))) {
				logger.info("验证乘客信息接口-规则error:合作商户已禁用，请客服人员通知商户！");
				printJson(response, getJson("007").toString());
				return;
			}
			JSONObject json_params = JSONObject.fromObject(json_param);
			List<Map<String, String>> detail_list = json_params.getJSONArray("detail_list");
			// logger.info("detail_list:"+detail_list.toString());
			if (detail_list.size() <= 0) {
				logger.info("验证乘客信息接口-参数error:订单内乘客信息参数为空或者格式错误!");
				printJson(response, getJson("205").toString());
				return;
			}

			if (ParamCheckUtil.userInfoCheck(detail_list)) {
				logger.info("验证乘客信息接口-参数error:订单内乘客信息参数为空或者格式错误!");
				printJson(response, getJson("205").toString());
				return;
			}

			/*
			 * String key = merchant_id+detail_list.get(0).get("user_ids");
			 * if(null == MemcachedUtil.getInstance().getAttribute(key)){
			 * logger.info("缓存当前用户证件号："+key);
			 * MemcachedUtil.getInstance().setAttribute(key, key, 5*1000);
			 * }else{ String str =
			 * (String)MemcachedUtil.getInstance().getAttribute(key);
			 * if(str.equals(key)){
			 * logger.info("验证乘客信息接口-规则error:重复验证异常："+detail_list.get(0).get(
			 * "user_ids")); printJson(response, getJson("201").toString());
			 * return; } }
			 */

			JSONArray users = new JSONArray();
			// 进行身份校验
			for (Map<String, String> map : detail_list) {
				JSONObject json = new JSONObject();
				json.put("cert_no", map.get("user_ids"));
				json.put("cert_type", map.get("ids_type"));
				json.put("user_name", map.get("user_name"));
				users.add(json);
			}
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("command", "verify");// 请求核验用户信息接口
			maps.put("passengers", users.toString());
			maps.put("channel", "qunar");
			String result = "";
			String fail_pas = "", fail_idcard = "", fail_info = "";
			int fail_num = 0;
			String return_code = "203";

			String reqParams = UrlFormatUtil.CreateUrl("", maps, "", "UTF-8");
			result = HttpUtil.sendByPost(real_name_verify_url, reqParams, "UTF-8");
			logger.info("验证乘客信息接口-核验结果:" + result);
			JSONArray jsonArr = JSONArray.fromObject(result);
			int index = jsonArr.size();
			for (int i = 0; i < index; i++) {
				if (!"0".equals((jsonArr.getJSONObject(i).get("check_status")))) {			
					String msg = (String) jsonArr.getJSONObject(i).get("message");
					if(msg!=null&&msg.contains("您的常用联系人数量已超过上限")) {
						logger.info(msg+","+jsonArr.getJSONObject(i).get("cert_no"));
						continue;
					}
					fail_pas += jsonArr.getJSONObject(i).get("user_name") + ",";
					fail_idcard += jsonArr.getJSONObject(i).get("cert_no") + ",";
					if (msg != null && msg.contains("冒用")) {
						fail_info += (jsonArr.getJSONObject(i).get("user_name") + "/"
								+ jsonArr.getJSONObject(i).get("cert_no") + ":冒用" + ",");
						return_code = "202";
					}else {
						fail_info += (jsonArr.getJSONObject(i).get("user_name") + "/"
								+ jsonArr.getJSONObject(i).get("cert_no") + ":未通过核验" + ",");}
					
					fail_num++;
					break;
				}/* else if ("0".equals((jsonArr.getJSONObject(i).get("check_status")))
						&& "登录异常!".equals((jsonArr.getJSONObject(i).get("message")))) {
					fail_pas += jsonArr.getJSONObject(i).get("user_name") + ",";
					fail_idcard += jsonArr.getJSONObject(i).get("cert_no") + ",";
					fail_info += (jsonArr.getJSONObject(i).get("user_name") + "/"
							+ jsonArr.getJSONObject(i).get("cert_no") + ":未通过铁路系统审核" + ",");
					fail_num++;
					break;
				}*/
			}

			JSONObject returnJson = new JSONObject();
			if ("".equals(fail_pas) && !"".equals(result)) {
				logger.info("验证乘客信息接口-用户已通过铁路系统审核，可正常购票！");
				for (int i = 0; i < detail_list.size(); i++) {
					commonService.updateMerchantVerifyNum(merchant_id);
				}
				returnJson.put("return_code", "000");
				returnJson.put("message", "用户已通过铁路系统审核，可正常购票！");
				printJson(response, returnJson.toString());
			} else if ("".equals(fail_pas) && "".equals(result)) {
				logger.info("验证乘客信息接口-验证失败，请稍候重试！");
				returnJson.put("return_code", "001");
				returnJson.put("message", "验证失败，请稍候重试！");
				printJson(response, returnJson.toString());

			} else {
				for (int i = 0; i < detail_list.size() - fail_num; i++) {
					commonService.updateMerchantVerifyNum(merchant_id);
				}
				returnJson.put("return_code", return_code);
				returnJson.put("fail_idcard", fail_idcard.substring(0, fail_idcard.length() - 1));// 核验失败的证件号码
				returnJson.put("fail_name", fail_pas.substring(0, fail_pas.length() - 1));// 核验失败的
																							// 姓名
				returnJson.put("message", fail_info.substring(0, fail_info.length() - 1));
				returnJson.put("detail_list", json_params.getJSONArray("detail_list"));
				logger.info("验证乘客信息接口-返回结果：" + returnJson.toString());
				printJson(response, returnJson.toString());
			}
		} catch (Exception e) {
			logger.error("验证乘客信息接口-异常exception:", e);
			printJson(response, getJson("001").toString());
		}
	}

	/**
	 * 请求改签
	 */
	public void requestChange(HttpServletRequest request, HttpServletResponse response) {
		logger.info("高铁管家请求改签开始");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String version = this.getParam(request, "version");
		String type = this.getParam(request, "type");
		String json_param = this.getParam(request, "json_param");
		String md_str = merchant_id + timestamp + version + type + json_param;
		String order_id = "";
		String msg = "";
		// 改签日志
		ChangeLogVO log = new ChangeLogVO();
		logger.info("高铁管家请求改签接口-用户参数：" + md_str);
		// 检查通用参数
		if (checkUniversalParam(request, response)) {
			try {
				logger.info("高铁管家请求改签接口-用户:登入成功!");

				JSONObject json_params = JSONObject.fromObject(json_param);
				// 验证业务参数
				order_id = json_params.getString("order_id");// 订单号
				String from_station_code = json_params.getString("from_station_code");// 出发站简称
				String from_station_name = json_params.getString("from_station_name");// 出发站名称
				String to_station_code = json_params.getString("to_station_code");// 到达站简称
				String to_station_name = json_params.getString("to_station_name");// 到达站名称
				String isChangeTo = json_params.getString("isChangeTo");// 0：改签
																		// 1：变更到站
				String out_ticket_billno = json_params.getString("out_ticket_billno");// 出票单号
																						// 12306单号
				String change_train_no = json_params.getString("change_train_no");// 改签后车次
				String change_from_time = json_params.getString("change_from_time");// 改签后发车时间
				String seat_type = json_params.getString("seat_type");// 座位类型
				String change_seat_type = json_params.getString("change_seat_type");// 改签后座位类型
				String callbackurl = json_params.getString("callbackurl");// 回调地址
				String reqtoken = json_params.getString("reqtoken");// 唯一标识
				JSONArray tickets = json_params.getJSONArray("ticketinfo");
				
				Boolean isChooseSeats = false;   //true:选座  ,false：不选座			
				if(json_params.containsKey("isChooseSeats")) {
					isChooseSeats=json_params.getBoolean("isChooseSeats");
				}
				String chooseSeats="";
				if(json_params.containsKey("chooseSeats")) { //选座信息
					chooseSeats=json_params.getString("chooseSeats");
				}
				logger.info("order_id:"+order_id+",isChooseSeats:"+isChooseSeats+",chooseSeats:"+chooseSeats);
				
				if(isChooseSeats) { //如果选座，做检验
					if(StringUtil.isEmpty(chooseSeats)||ParamCheckUtil.isNotCheck(chooseSeats, ParamCheckUtil.chooseSeat)||chooseSeats.length()!=tickets.size()*2) {//校验不通过，例如：1A2B,
						logger.info("高铁请求"+msg+"ERROR,参数有空order_id:"+order_id);
						printJson(response, getJson(TrainConsts.RETURN_CODE_003).toString());
						return;
					}
				}

				msg = isChangeTo.equals("1") ? "变更到站" : "改签";
				logger.info("高铁管家" + msg + ",orderId : " + order_id + "车票信息 : " + tickets);

				/* 业务参数检查 */
				if ("".equals(order_id) || "".equals(out_ticket_billno) || "".equals(change_train_no)
						|| "".equals(change_from_time) || "".equals(seat_type) || "".equals(change_seat_type)
						|| "".equals(tickets) || "".equals(from_station_name) || "".equals(to_station_name)) {
					logger.info("高铁管家请求" + msg + "ERROR,参数有空order_id:" + order_id);
					printJson(response, getJson(TrainConsts.RETURN_CODE_003).toString());
					return;
				}

				/* 查询订单 */
				OrderInfo orderInfo = orderService.queryOrderInfo(order_id);
				if (orderInfo == null || orderInfo.getOrder_status() == null) {
					/* 订单不存在 */
					logger.info("高铁管家请求" + msg + "ERROR,订单不存在order_id:" + order_id);
					printJson(response, getJson(TrainConsts.RETURN_CODE_801).toString());
					return;
				}

				/* 检查订单状态 */
				String orderStatus = orderInfo.getOrder_status();
				if (!TrainConsts.OUT_SUCCESS.equals(orderStatus)) {
					/* 出票成功才可以改签 */
					logger.info("高铁管家请求" + msg + "ERROR,订单状态不正确，订单号为" + order_id);
					printJson(response, getJson(TrainConsts.RETURN_CODE_802).toString());
					return;
				}
				/* 查询该订单号下的改签特征值，排除重复请求 */
				ChangeInfo reqtokenChangeInfo = changeService.getChangeInfoByReqtoken(reqtoken);
				if (reqtokenChangeInfo != null) {
					logger.info("高铁管家请求" + msg + "该请求已存在，reqtoken为" + reqtoken);
					printJson(response, getJson(TrainConsts.RETURN_CODE_803).toString());
					return;
				}
				// 出发站验证
				if (!from_station_name.equals(orderInfo.getFrom_station())) {
					logger.info("高铁管家-请求" + msg + "ERROR,出发站不相同");
					printJson(response, getJson(TrainConsts.RETURN_CODE_103).toString());
					return;
				}
				/* 改签时间验证 */
				Date old_from_time = DateUtil.stringToDate(
						orderInfo.getTravel_time().split(" ")[0] + " " + orderInfo.getFrom_time() + ":00",
						DateUtil.DATE_FMT3);
				// 变更到站
				if (isChangeTo.equals("1")) {
					if (DateUtil.minuteDiff(old_from_time, new Date()) < 48 * 60) {
						/* 距离发车时间小于48小时 */
						logger.info("高铁管家-请求变更到站ERROR,距离开车时间太近无法变更到站");
						printJson(response, getJson(TrainConsts.RETURN_CODE_805).toString());
						return;
					}
				} else {
					if (DateUtil.minuteDiff(old_from_time, new Date()) < 30) {
						/* 距离发车时间小于30分 */
						logger.info("高铁管家-请求改签ERROR,距离开车时间太近无法改签");
						printJson(response, getJson(TrainConsts.RETURN_CODE_804).toString());
						return;
					}

				}

				/* 车票信息 */
				if (tickets.size() == 0) {
					logger.info("高铁管家-请求" + msg + "ERROR,没有车票信息");
					printJson(response, getJson(TrainConsts.RETURN_CODE_205).toString());
					return;
				}
				if (tickets.size() > 1) {
					/* 批量改签 */
					if (seat_type.equals(TrainConsts.SEAT_4) || seat_type.equals(TrainConsts.SEAT_5)
							|| seat_type.equals(TrainConsts.SEAT_6) || change_seat_type.equals(TrainConsts.SEAT_4)
							|| change_seat_type.equals(TrainConsts.SEAT_5)
							|| change_seat_type.equals(TrainConsts.SEAT_6)) {
						/* 批量改签原票坐席不能为卧铺 */
						logger.info("高铁管家-请求改签ERROR,批量改签原票或新票坐席不能为卧铺");
						printJson(response, getJson(TrainConsts.RETURN_CODE_806).toString());
						return;
					}
				}
				
				
				/* 组装改签车票信息 */
				ChangeInfo changeInfo = new ChangeInfo();
				List<ChangePassengerInfo> changePassengers = new ArrayList<ChangePassengerInfo>();
				for (int i = 0; i < tickets.size(); i++) {
					/* 传入的参数数据 */
					JSONObject ticket = tickets.getJSONObject(i);
					List<ChangePassengerInfo> cps = changeService.getChangeCpById(ticket.getString("old_ticket_no"));
					if (cps != null) {
						for (ChangePassengerInfo changeCp : cps) {
							/* 每张车票只能改签一次 */
							if (changeCp.getIs_changed().equals("Y")) {
								logger.info("高铁管家-请求改签ERROR,车票已改签过,车票id:" + changeCp.getCp_id());
								printJson(response, getJson(TrainConsts.RETURN_CODE_807).toString());
								return;
							}
						}
					}
					ChangePassengerInfo cp = new ChangePassengerInfo();
					String new_cp_id = ticket.getString("new_ticket_no");
					cp.setOrder_id(order_id);// 订单id
					cp.setCp_id(ticket.getString("old_ticket_no"));// 车票id(原票)
					cp.setNew_cp_id(new_cp_id);// 改签后车票id
					cp.setChange_seat_type(change_seat_type);// 19e改签后新座位席别
					cp.setSeat_type(seat_type);
					cp.setIs_changed("N");

					/* 原票信息 */
					OrderInfoCp p = orderService.queryCpInfoByCpId(cp.getCp_id());
					if (p != null) {
						cp.setBuy_money(p.getBuy_money());// 成本价格
						cp.setSeat_no(p.getSeat_no());// 座位号
						cp.setSeat_type(p.getSeat_type());// 座位席别
						cp.setTrain_box(p.getTrain_box());// 车厢
						cp.setTicket_type(p.getTicket_type());// 车票类型
						cp.setIds_type(p.getIds_type());// 证件类型
						cp.setUser_ids(p.getUser_ids());// 证件号码
						cp.setUser_name(p.getUser_name());// 乘客姓名
					}
					changePassengers.add(cp);
				}
				/* 组装改签记录信息 */
				changeInfo.setChange_travel_time(change_from_time);// 改签后乘车日期
				changeInfo.setTrain_no(orderInfo.getTrain_no());// 车次
				changeInfo.setChange_train_no(change_train_no);// 改签后车次
				changeInfo.setFrom_time(
						orderInfo.getTravel_time().split(" ")[0] + " " + orderInfo.getFrom_time() + ":00");// 发车时间
				changeInfo.setChange_from_time(change_from_time);// 改签后发车时间
				changeInfo.setFrom_city(from_station_name);// 出发车站
				changeInfo.setTo_city(to_station_name);// 到达车站
				changeInfo.setFrom_station_code(from_station_code);
				changeInfo.setTo_station_code(to_station_code);
				changeInfo.setIschangeto(new Integer(isChangeTo));
				changeInfo.setOut_ticket_billno(out_ticket_billno);// 12306单号
				changeInfo.setOrder_id(order_id);
				changeInfo.setIsasync("Y");// 异步
				changeInfo.setCallbackurl(callbackurl);
				changeInfo.setReqtoken(reqtoken);
				changeInfo.setChange_status(TrainConsts.TRAIN_REQUEST_CHANGE);// 11改签预定
				
				changeInfo.setIsChooseSeats(isChooseSeats?1:0);
				changeInfo.setChooseSeats(chooseSeats);
				
				changeInfo.setcPassengers(changePassengers);// 改签、车票关系
				Map<String, String> cpParam = new HashMap<String, String>();
				cpParam.put("order_id", order_id);
				Integer acc_id = (Integer) orderService.queryAccountOrderInfo(cpParam).get("acc_id");
				changeInfo.setAccount_id(acc_id.toString());// 出票账号id
				changeInfo.setChange_notify_count(0);
				changeInfo.setChange_notify_status(TrainConsts.CHANGE_NOTIFY_PRE);
				changeInfo.setMerchant_id(merchant_id);
				/* 改签信息入库 */
				changeService.addChangeInfo(changeInfo);
				int change_id = changeInfo.getChange_id();
				log.setChange_id(change_id);
				logger.info("高铁管家请求" + msg + "异步成功,orderId : " + order_id + "车票信息 : " + tickets);
				log.setContent("高铁管家请求" + msg + "异步success");
				log.setOrder_id(order_id);
				log.setOpt_person(merchant_id);
				changeService.addChangeLog(log);
				// 请求成功同步返回结果
				JSONObject json = new JSONObject();
				json.put("return_code", "000");
				json.put("message", "请求改签请求成功");
				json.put("ticketinfo", tickets);
				printJson(response, json.toString());
			} catch (Exception e) {
				logger.info("高铁管家请求" + msg + "异常" + e);
				e.printStackTrace();
				log.setContent("高铁管家请求" + msg + "异常!");
				log.setOrder_id(order_id);
				log.setOpt_person(merchant_id);
				changeService.addChangeLog(log);
				printJson(response, getJson(TrainConsts.RETURN_CODE_001).toString());
			}
		}
	}

	/**
	 * 取消改签
	 * 
	 * @param request
	 * @param response
	 */
	public void cancelChange(HttpServletRequest request, HttpServletResponse response) {
		logger.info("高铁管家取消改签开始");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String version = this.getParam(request, "version");
		String type = this.getParam(request, "type");
		String json_param = this.getParam(request, "json_param");
		String md_str = merchant_id + timestamp + version + type + json_param;
		String order_id = "";
		// 改签日志
		ChangeLogVO log = new ChangeLogVO();
		logger.info("高铁管家取消改签接口-用户参数：" + md_str);
		if (this.checkUniversalParam(request, response)) {
			try {
				Map<String, String> merchantSetting = commonService.queryMerchantInfo(merchant_id);

				logger.info("高铁管家取消改签接口登入成功!");

				// 业务参数
				JSONObject json_params = JSONObject.fromObject(json_param);
				order_id = json_params.getString("order_id");// 订单号
				String callbackurl = json_params.getString("callbackurl");// 回调地址
				String reqtoken = json_params.getString("reqtoken");// 唯一标识
				logger.info(
						merchantSetting.get("merchant_name") + "取消改签,orderId : " + order_id + "reqtoken: " + reqtoken);

				/* 检查业务参数 */
				if ("".equals(order_id) || "".equals(callbackurl)) {
					printJson(response, getJson(TrainConsts.RETURN_CODE_003).toString());
					return;
				}
				/* 查询订单 */
				OrderInfo orderInfo = orderService.queryOrderInfo(order_id);
				if (orderInfo == null || orderInfo.getOrder_status() == null) {
					/* 订单不存在 */
					logger.info("高铁管家-取消改签ERROR,订单不存在order_id:" + order_id);
					printJson(response, getJson(TrainConsts.RETURN_CODE_301).toString());
					return;
				}

				ChangeInfo changeInfo = changeService.getChangeInfoByReqtoken(reqtoken);
				if (!changeInfo.getChange_status().equals(TrainConsts.TRAIN_REQUEST_CHANGE_SUCCESS)) {
					/* 订单状态不正确 */
					logger.info("高铁管家-取消改签ERROR,订单状态不正确order_id:" + order_id);
					printJson(response, getJson(TrainConsts.RETURN_CODE_802).toString());
					return;
				}

				log.setChange_id(changeInfo.getChange_id());
				log.setOrder_id(order_id);
				log.setOpt_person("高铁管家接口");
				Date currentTime = new Date();
				/* 预订成功后的30分钟内才能取消改签 */
				Date bookTime = DateUtil.stringToDate(changeInfo.getBook_ticket_time(), DateUtil.DATE_FMT3);
				long minuteDiff = DateUtil.minuteDiff(currentTime, bookTime);
				if (minuteDiff > 30) {
					logger.info("高铁管家-取消改签ERROR,距离改签车票预订时间超过30分钟");
					printJson(response, getJson(TrainConsts.RETURN_CODE_811).toString());
					return;
				}
				/* 将状态为14、预订成功的改签状态都改为21、改签取消 */
				changeInfo.setChange_status(TrainConsts.TRAIN_CANCEL_CHANGE);
				changeInfo.setChange_notify_count(0);
				changeInfo.setChange_notify_status(TrainConsts.CHANGE_NOTIFY_PRE);
				changeInfo.setCallbackurl(callbackurl);
				changeInfo.setReqtoken(reqtoken);
				/* 更新改签状态 */
				changeService.updateChangeInfo(changeInfo);

				// 请求成功同步返回结果
				Map<String, Object> changePassParam = new HashMap<String, Object>();
				changePassParam.put("change_id", changeInfo.getChange_id());
				List<ChangePassengerInfo> cPassengers = changeService.getChangePassenger(changePassParam);
				JSONArray tickets = new JSONArray();
				for (ChangePassengerInfo cPassenger : cPassengers) {
					JSONObject jsonPass = new JSONObject();
					jsonPass.put("user_name", cPassenger.getUser_name());
					jsonPass.put("user_ids", cPassenger.getUser_name());
					jsonPass.put("old_ticket_no", cPassenger.getCp_id());
					jsonPass.put("new_ticket_no", cPassenger.getNew_cp_id());
					tickets.add(jsonPass);
				}
				JSONObject json = new JSONObject();
				json.put("return_code", "000");
				json.put("message", "取消改签请求成功");
				json.put("ticketinfo", tickets);
				printJson(response, json.toString());
				logger.info("高铁管家取消改签异步成功,orderId : " + order_id);
				log.setContent("高铁管家请求取消改签异步success");
			} catch (Exception e) {
				logger.info("高铁管家取消改签异常" + e);
				e.printStackTrace();
				printJson(response, getJson(TrainConsts.RETURN_CODE_001).toString());
				log.setContent("高铁管家取消改签异常!");
			} finally {

				log.setOpt_person(merchant_id);
				changeService.addChangeLog(log);
			}
		}

	}

	/**
	 * 确认改签
	 */
	public void confirmChange(HttpServletRequest request, HttpServletResponse response) {
		logger.info("确认改签开始");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String version = this.getParam(request, "version");
		String type = this.getParam(request, "type");
		String json_param = this.getParam(request, "json_param");
		String md_str = merchant_id + timestamp + version + type + json_param;
		String order_id = "";
		// 改签日志
		ChangeLogVO log = new ChangeLogVO();
		logger.info("确认改签接口-用户参数：" + md_str);
		if (this.checkUniversalParam(request, response)) {
			try {
				Map<String, String> merchantSetting = commonService.queryMerchantInfo(merchant_id);
				// 业务参数
				JSONObject json_params = JSONObject.fromObject(json_param);
				order_id = json_params.getString("order_id");// 订单号
				String callbackurl = json_params.getString("callbackurl");// 回调地址
				String reqtoken = json_params.getString("reqtoken");// 唯一标识
				logger.info(
						merchantSetting.get("merchant_name") + "确认改签,orderId : " + order_id + "reqtoken: " + reqtoken);

				/* 检查业务参数 */
				if ("".equals(order_id) || "".equals(callbackurl)) {
					printJson(response, getJson(TrainConsts.RETURN_CODE_003).toString());
					return;
				}
				/* 查询订单 */
				OrderInfo orderInfo = orderService.queryOrderInfo(order_id);
				if (orderInfo == null || orderInfo.getOrder_status() == null) {
					/* 订单不存在 */
					logger.info("高铁管家-确认改签ERROR,订单不存在order_id:" + order_id);
					printJson(response, getJson(TrainConsts.RETURN_CODE_301).toString());
					return;
				}

				/* 查询改签预订票并更新状态 */
				ChangeInfo changeInfo = changeService.getChangeInfoByReqtoken(reqtoken);
				if (changeInfo == null
						|| !changeInfo.getChange_status().equals(TrainConsts.TRAIN_REQUEST_CHANGE_SUCCESS)) {
					/* 订单状态不正确 */
					logger.info("高铁管家-确认改签ERROR,订单状态不正确order_id:" + order_id);
					printJson(response, getJson(TrainConsts.RETURN_CODE_802).toString());
					return;
				}

				log.setChange_id(changeInfo.getChange_id());
				log.setOrder_id(changeInfo.getOrder_id());
				log.setOpt_person("高铁管家接口");
				/* 22:34:59 */
				Calendar time_22_44_59 = Calendar.getInstance();
				time_22_44_59.set(Calendar.HOUR_OF_DAY, 22);
				time_22_44_59.set(Calendar.MINUTE, 44);
				time_22_44_59.set(Calendar.SECOND, 59);
				/* 23:30:00 */
				Calendar time_23_30_00 = Calendar.getInstance();
				time_23_30_00.set(Calendar.HOUR_OF_DAY, 23);
				time_23_30_00.set(Calendar.MINUTE, 30);
				time_23_30_00.set(Calendar.SECOND, 00);
				Calendar currentTime = Calendar.getInstance();
				Calendar bookTime = Calendar.getInstance();
				Date book = DateUtil.stringToDate(changeInfo.getBook_ticket_time(), DateUtil.DATE_FMT3);
				bookTime.setTime(book);

				boolean timeOut = false;
				if (bookTime.before(time_22_44_59)) {// 当天22:44:59之前预订
					/* 30分钟的付款时间 */
					System.out.println(
							"current : " + DateUtil.dateToString(currentTime.getTime(), "yyyy-MM-dd HH:mm:ss"));
					System.out.println("book : " + DateUtil.dateToString(book, "yyyy-MM-dd HH:mm:ss"));
					if (DateUtil.minuteDiff(currentTime.getTime(), book) > 30) {
						timeOut = true;
					}
					logger.info("当天22:44:59之前预订,30分钟的付款时间,timeout" + timeOut);
				} else if (bookTime.after(time_22_44_59)) {// 当天22:44:59之后预订
					/* 23:30:00之前付款 */
					if (currentTime.after(time_23_30_00)) {
						timeOut = true;
					}
					logger.info("当天22:44:59之后预订,23:30:00之前付款,timeout" + timeOut);
				}
				if (timeOut) {
					logger.info("高铁管家-确认改签ERROR,确认改签的请求时间已超过规定时间");
					printJson(response, getJson(TrainConsts.RETURN_CODE_810).toString());
					return;
				}
				/* 确认改签 */
				/*
				 * if(StrUtil.isNotEmpty(changeInfo.getChange_refund_money()) &&
				 * StrUtil.isNotEmpty(changeInfo.getChange_receive_money())) {
				 * 
				 * Double change_refund_money =
				 * Double.parseDouble(changeInfo.getChange_refund_money());
				 * Double change_receive_money =
				 * Double.parseDouble(changeInfo.getChange_receive_money());
				 * if(change_receive_money > change_refund_money) {
				 * changeInfo.setChange_status(TrainConsts.
				 * TRAIN_CONFIRM_CHANGE_START_PAY); } else {
				 * changeInfo.setChange_status(TrainConsts.TRAIN_CONFIRM_CHANGE)
				 * ; } } else {
				 * changeInfo.setChange_status(TrainConsts.TRAIN_CONFIRM_CHANGE)
				 * ; }
				 */
				changeInfo.setChange_status(TrainConsts.TRAIN_CONFIRM_CHANGE);
				changeInfo.setCallbackurl(callbackurl);
				changeInfo.setReqtoken(reqtoken);
				changeInfo.setChange_notify_count(0);
				changeInfo.setChange_notify_status(TrainConsts.CHANGE_NOTIFY_PRE);
				logger.info(order_id + ",callbackurl:" + changeInfo.getCallbackurl() + ",reqtoken:"
						+ changeInfo.getReqtoken());
				/* 更新车票改签状态 */
				int count = changeService.updateChangeInfo(changeInfo);
				logger.info(order_id + ",callbackurl:" + changeInfo.getCallbackurl() + ",reqtoken:"
						+ changeInfo.getReqtoken() + ",count:" + count);
				log.setContent("高铁管家请求改签支付异步success ,orderId" + order_id);
				// 请求成功同步返回结果
				Map<String, Object> changePassParam = new HashMap<String, Object>();
				changePassParam.put("change_id", changeInfo.getChange_id());
				List<ChangePassengerInfo> cPassengers = changeService.getChangePassenger(changePassParam);
				JSONArray tickets = new JSONArray();
				for (ChangePassengerInfo cPassenger : cPassengers) {
					JSONObject jsonPass = new JSONObject();
					jsonPass.put("user_name", cPassenger.getUser_name());
					jsonPass.put("user_ids", cPassenger.getUser_name());
					jsonPass.put("old_ticket_no", cPassenger.getCp_id());
					jsonPass.put("new_ticket_no", cPassenger.getNew_cp_id());
					tickets.add(jsonPass);
				}
				JSONObject json = new JSONObject();
				json.put("return_code", "000");
				json.put("message", "确认改签请求成功");
				json.put("ticketinfo", tickets);
				printJson(response, json.toString());
				logger.info("高铁管家确认改签异步成功,orderId : " + order_id);
			} catch (Exception e) {
				logger.info("高铁管家-确认改签异常" + e);
				e.printStackTrace();
				printJson(response, getJson(TrainConsts.RETURN_CODE_001).toString());
				log.setContent("高铁管家请求改签支付异常!");
			} finally {
				changeService.addChangeLog(log);
			}
		}
	}

	// 检查通用参数
	private Boolean checkUniversalParam(HttpServletRequest request, HttpServletResponse response) {
		logger.info("检查高铁管家请求的通用参数");
		String terminal = this.getParam(request, "terminal");
		String merchant_id = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String version = this.getParam(request, "version");
		String type = this.getParam(request, "type");
		String json_param = this.getParam(request, "json_param");
		String hmac = this.getParam(request, "hmac");
		String md_str = terminal + merchant_id + timestamp + version + type + json_param;
		if (StringUtil.isEmpty(merchant_id) || StringUtil.isEmpty(timestamp) || StringUtil.isEmpty(type)
				|| StringUtil.isEmpty(version) || StringUtil.isEmpty(hmac) || StringUtil.isEmpty(json_param)
				|| ParamCheckUtil.isNotCheck(timestamp, ParamCheckUtil.TIMESTAMP_REGEX)) {
			logger.info("参数校验error:必要参数为空或格式错误!");
			printJson(response, getJson(TrainConsts.RETURN_CODE_003).toString());
			return false;
		}

		Map<String, String> merchantSetting = commonService.queryMerchantInfo(merchant_id);
		if (merchantSetting == null) {
			logger.info("用户身份校验error:不存在的用户!");
			printJson(response, getJson(TrainConsts.RETURN_CODE_004).toString());
			return false;
		}
		if ("00".equals(merchantSetting.get("merchant_status")) || "00".equals(merchantSetting.get("verify_status"))) {
			logger.info("error:合作商户已禁用，请客服人员通知商户！");
			printJson(response, getJson(TrainConsts.RETURN_CODE_004).toString());
			return false;
		}
		logger.info("用户传递hmac：【" + hmac + "】");
		// 加密明文
		String hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(md_str + merchantSetting.get("sign_key"), "", "utf-8");

		logger.info("系统加密hmac：【" + hmac_19 + "】");
		if (!hmac_19.equals(hmac)) {
			logger.info("安全校验error:不符合安全校验规则。");
			printJson(response, getJson(TrainConsts.RETURN_CODE_002).toString());
			return false;
		}
		return true;

	}

	/**
	 * @param request
	 * @param response
	 *            获取没有回调的订单
	 */
	public void getNoCallBackOrder(HttpServletRequest request, HttpServletResponse response) {

		String type = this.getParam(request, "type");
		String supplierId = "19e";
		String key = GT_GETNOCALLORDER_KEY; // 访问需要的key
		logger.info("type:" + type + ",getNoCallBackOrder,开始查询未回调订单，由后台【train_manager】发起查询," + supplierId);
		// md5(supplierId+md5(key).toLowerCase()).toLowerCase()
		String base_64_supplierId = Base64Util.getBASE64(supplierId);
		String sid_md5 = Md5Encrypt.getKeyedDigestFor19Pay(
				base_64_supplierId + Md5Encrypt.getKeyedDigestFor19Pay(key, "", "utf-8"), "", "utf-8");

		logger.info("getNoCallBackOrder,base_64_supplierId:" + base_64_supplierId);

		StringBuffer param = new StringBuffer();
		param.append("supplierId=").append(base_64_supplierId).append("&sid=").append(sid_md5);

		String result = HttpsUtil.sendHttps(get_nocallorder_url + "?" + param.toString());
		logger.info("getNoCallBackOrder,result:" + result);

		String callbackFunName = request.getParameter("callbackparam");// 得到js函数名称
		logger.info("getNoCallBackOrder,callbackFunName:" + callbackFunName);
		if (StringUtils.isEmpty(result)) {
			logger.info("getNoCallBackOrder,查询异常");
			printJson(response, callbackFunName + "({\"result\":\"ERROR\",\"msg\":\"没有获取到结果\"})");
			return;
		}
		try {
			JSONObject obj = JSONObject.fromObject(result);
			String result_status = (String) obj.get("result");
			if ("SUCCESS".equals(result_status)) {
				logger.info("getNoCallBackOrder,查询成功,----");
				printJson(response, callbackFunName + "(" + result + ")");
				return;
			} else {
				String msg = (String) obj.get("msg");
				logger.info("getNoCallBackOrder,查询失败,----");
				printJson(response, callbackFunName + "({\"result\":\"ERROR\",\"msg\":\"获取结果错误," + msg + "\"" + "})");
				return;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info("getNoCallBackOrder,解析异常");
			printJson(response, callbackFunName + "({\"result\":\"ERROR\",\"msg\":\"获取结果解析异常\"})");
			return;
		}

	}

}
