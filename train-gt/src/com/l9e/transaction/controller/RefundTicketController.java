package com.l9e.transaction.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.common.BaseController;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.ChangeService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.RefundService;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.util.AmountUtil;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.MathUtil;
import com.l9e.util.Md5Encrypt;
import com.l9e.util.MemcachedUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class RefundTicketController extends BaseController {

	private static final Logger logger = Logger.getLogger(RefundTicketController.class);

	@Resource
	private OrderService orderService;

	@Resource
	private RefundService refundService;

	@Resource
	private ChangeService changeService;

	/**
	 * 申请退票接口
	 * 
	 * @param request
	 * @param response
	 */
	public void refundTicket(HttpServletRequest request, HttpServletResponse response) {
		String uuid = (String) request.getAttribute("uuid");
		logger.info(uuid + "### 调用对外申请退款接口操作...");

		JSONObject jsonRes = new JSONObject();
		// 获取协议参数
		String terminal = this.getParam(request, "terminal");
		String merchantId = this.getParam(request, "merchant_id");
		String timestamp = this.getParam(request, "timestamp");
		String type = this.getParam(request, "type");
		String version = this.getParam(request, "version");
		String sparePro1 = this.getParam(request, "spare_pro1");
		String sparePro2 = this.getParam(request, "spare_pro2");
		String jsonParamStr = this.getParam(request, "json_param");
		String hmac = this.getParam(request, "hmac");
		logger.info(uuid + "申请退款，Json Param:" + jsonParamStr);

		String merchantOrderId = "";// 初始化
		String orderId = "";

		// 根据商户ID 查询商户配置
		Map<String, String> merchantSetting = commonService.queryMerchantInfo(merchantId);

		try {
			logger.info(uuid + "接口验签...");
			String sourceStr = terminal + merchantId + timestamp + type + version + sparePro1 + sparePro2 + jsonParamStr;
			logger.info(uuid + "验签参数-sourceStr:" + sourceStr);

			String signKey = merchantSetting.get("sign_key");
			// 加密
			String hmac19 = Md5Encrypt.getKeyedDigestFor19Pay(sourceStr + signKey, "", "utf-8");
			if (!hmac.equalsIgnoreCase(hmac19)) {
				logger.info(uuid + "验签失败！");
				jsonRes.put("merchant_order_id", merchantOrderId);
				jsonRes.put("message", "安全验证错误，不符合安全校验规则。");
				jsonRes.put("order_id", orderId);
				jsonRes.put("return_code", "002");
				jsonRes.put("trip_no", "");
				jsonRes.put("status", "FAILURE");// 申请失败
				jsonRes.put("fail_reason", "验签失败");
				write2Response(response, jsonRes.toString());
				return;
			}

			logger.info(uuid + "解析退票参数...");
			String comment = this.getParam(request, "comment");
			JSONObject jsonParam = JSONObject.fromObject(jsonParamStr);
			merchantOrderId = jsonParam.getString("merchant_order_id");
			orderId = jsonParam.getString("order_id");
			String refundPictureUrl = jsonParam.getString("refund_picture_url");
			String refundResultUrl = jsonParam.getString("refund_result_url");
			String refundType = jsonParam.getString("refund_type");
			JSONArray refundinfoArray = JSONArray.fromObject(jsonParam.get("refundinfo"));
			String requestId = jsonParam.getString("request_id");
			String refundOrderType = "";
			if (jsonParam.containsKey("refund_order_type")) {
				refundOrderType = jsonParam.getString("refund_order_type");
			}
			// 验证参数是否为空
			if (checkParamEmpty(merchantOrderId, "merchant_order_id", jsonRes, uuid)) {
				write2Response(response, jsonRes.toString());
				return;
			}
			if (checkParamEmpty(orderId, "order_id", jsonRes, uuid)) {
				write2Response(response, jsonRes.toString());
				return;
			}
			if (checkParamEmpty(refundResultUrl, "refund_result_url", jsonRes, uuid)) {
				write2Response(response, jsonRes.toString());
				return;
			}
			if (checkParamEmpty(refundType, "refund_type", jsonRes, uuid)) {
				write2Response(response, jsonRes.toString());
				return;
			}
			if (checkParamEmpty(requestId, "request_id", jsonRes, uuid)) {
				write2Response(response, jsonRes.toString());
				return;
			}

			// 参数refund_type="part" refundinfo必填
			if ("part".equalsIgnoreCase(refundType)) {
				if (refundinfoArray == null || refundinfoArray.size() == 0 || "[]".equals(refundinfoArray.toString())) {
					logger.info(uuid + "refundinfo为空！");
					jsonRes.put("merchant_order_id", merchantOrderId);
					jsonRes.put("message", "参数refund_type=part，退票乘客信息refundinfo必填。");
					jsonRes.put("order_id", orderId);
					jsonRes.put("return_code", "003");
					jsonRes.put("trip_no", "");
					jsonRes.put("status", "FAILURE");// 申请失败
					jsonRes.put("fail_reason", "参数refund_type=part，退票乘客信息refundinfo必填。");
					write2Response(response, jsonRes.toString());
					return;
				}
			}

			logger.info(uuid + "解析退票乘客信息，查询退的车票id...");
			List<String> cpList = new ArrayList<String>();
			List<String> refundCpList = new ArrayList<String>();
			// 查询订单下是否已有退票信息
			refundCpList = orderService.queryRefund_cp_list(orderId);
			logger.info(uuid + "refundOrderType:" + refundOrderType + " [1:原票退款, 3:改签退款]");
			logger.info(uuid + "refundType:" + refundType);
			if ("3".equals(refundOrderType)) { // 改签退款
				if ("all".equals(refundType)) { // 全部
					cpList = changeService.getCpListByOrderId(orderId);
				} else {
					for (int i = 0; i < refundinfoArray.size(); i++) {
						JSONObject refundInfoJson = refundinfoArray.getJSONObject(i);
						String cpId = refundInfoJson.getString("cp_id");
						if (!cpList.contains(cpId)) {
							cpList.add(cpId);
						}
					}
				}
			} else { // 原票退款
				if ("all".equals(refundType)) { // 全部
					cpList = orderService.queryCp_idList(orderId);
				} else {
					Map<String, String> paramMap = new HashMap<String, String>();
					paramMap.put("order_id", orderId);
					for (int i = 0; i < refundinfoArray.size(); i++) {
						JSONObject obj = (JSONObject) refundinfoArray.get(i);
						paramMap.put("id_type", obj.getString("id_type"));
						paramMap.put("user_ids", obj.getString("user_ids"));
						paramMap.put("user_name", obj.getString("user_name"));
						paramMap.put("ticket_type", obj.getString("ticket_type"));
						paramMap.put("cp_id", obj.getString("cp_id"));
						List<String> cpidList = orderService.queryCp_idByIds(paramMap);

						if (cpidList.size() == 1) {
							cpList.add(cpidList.get(0));
						} else {
							for (String cp_id : cpidList) {
								if (!cpList.contains(cp_id)) {
									cpList.add(cp_id);
									break;
								}
							}
						}
					}
				}
			}
			logger.info(uuid + "本次退票乘客列表-cpIdList:" + cpList.toString());

			if (cpList.size() == 0) {
				logger.info(uuid + "本次退票请求乘客信息参数有误，merchant_refund_seq=" + requestId + ", channel=" + merchantId + ", merchant_order_id=" + merchantOrderId);
				jsonRes.put("merchant_order_id", merchantOrderId);
				jsonRes.put("message", "本次退票请求乘客信息参数有误。");
				jsonRes.put("order_id", orderId);
				jsonRes.put("return_code", "003");
				jsonRes.put("trip_no", "");
				jsonRes.put("status", "FAILURE");
				jsonRes.put("fail_reason", "本次退票请求乘客信息参数有误，不能申请退票操作。");
				write2Response(response, jsonRes.toString());
				return;
			}

			logger.info(uuid + "拦截频繁提交...");
			MemcachedUtil memcached = MemcachedUtil.getInstance();
			if ("part".equalsIgnoreCase(refundType)) {
				String key = merchantOrderId + cpList.get(0);
				Object mAttribute = memcached.getAttribute(key);
				if (null == mAttribute) {
					logger.info(uuid + "缓存当前商户订单号:" + key);
					memcached.setAttribute(key, key, 10 * 1000);
				} else {
					String mValue = (String) mAttribute;
					logger.info(uuid + "mKey:" + key + ", mValue:" + mValue);
					if (mValue.equals(key)) {
						logger.info(uuid + "本次退票请求为重复请求，请求过于频繁，merchant_refund_seq=" + requestId + ", channel=" + merchantId + ", merchant_order_id"
								+ merchantOrderId);

						jsonRes.put("merchant_order_id", merchantOrderId);
						jsonRes.put("message", "本次退票请求为重复请求，请求过于频繁。");
						jsonRes.put("order_id", orderId);
						jsonRes.put("return_code", "000");
						jsonRes.put("trip_no", "");
						jsonRes.put("status", "REPEAT");// 重复申请
						jsonRes.put("fail_reason", "本次退票请求为重复请求，请求过于频繁。");
						write2Response(response, jsonRes.toString());
					} else {
						// 不一致
					}
				}
			}

			logger.info(uuid + "重复提交去重...");
			cpList = getNowRefundCpList(orderId, cpList, refundCpList, uuid);

			if (cpList.size() == 0) {
				logger.info(
						uuid + "商户退票流水已经发生过退票，拒绝本次操作，merchant_refund_seq=" + requestId + ", channel=" + merchantId + ", merchant_order_id=" + merchantOrderId);

				jsonRes.put("merchant_order_id", merchantOrderId);
				jsonRes.put("message", "本次退票请求为重复请求。");
				jsonRes.put("order_id", orderId);
				jsonRes.put("return_code", "000");
				jsonRes.put("trip_no", "");
				jsonRes.put("status", "REPEAT");// 重复申请 返回退款申请成功
				jsonRes.put("fail_reason", "本次退票请求为重复请求，退款流水号已存在或已经退款完成。");
				write2Response(response, jsonRes.toString());
				return;
			}

			if (cpList.size() == 1 && cpList.get(0).contains("success")) {
				String refundSeq = cpList.get(0).split("_")[1];
				jsonRes.put("merchant_order_id", merchantOrderId);
				jsonRes.put("message", "");
				jsonRes.put("order_id", orderId);
				jsonRes.put("return_code", "000");
				jsonRes.put("trip_no", refundSeq);
				jsonRes.put("status", "SUCCESS");// 申请成功
				jsonRes.put("fail_reason", "");
				logger.info(uuid + "申请成功:" + jsonRes.toString());
				write2Response(response, jsonRes.toString());
				return;
			}

			/*
			 * //退票受理时间 Date currentDate = new Date(); String datePre =
			 * DateUtil.dateToString(currentDate, "yyyyMMdd"); Date begin =
			 * DateUtil.stringToDate(datePre + "060000",
			 * "yyyyMMddHHmmss");//6:00 Date end = DateUtil.stringToDate(datePre
			 * + "230000", "yyyyMMddHHmmss");//23:00
			 * if(currentDate.before(begin) || currentDate.after(end)){
			 * logger.info("不受理该退款，发起时间为："+DateUtil.dateToString(currentDate,
			 * "yyyy-MM-dd HH:mm:ss"));
			 * 
			 * jsonRes.put("merchant_order_id", merchant_order_id);
			 * jsonRes.put("message", "不受理本次退票申请，每日受理退票时间为早6点至晚23点。");
			 * jsonRes.put("order_id", order_id); jsonRes.put("return_code",
			 * "000"); jsonRes.put("trip_no",""); jsonRes.put("status",
			 * "ILLEGAL_TIME");//受理退票时间 jsonRes.put("fail_reason",
			 * "不受理本次退票申请，每日受理退票时间为早6点至晚23点。"); write2Response(response,
			 * jsonRes.toString()); return;
			 * 
			 * }
			 */

			logger.info(uuid + "[对外接口使用商主动发起退款]order_id=" + orderId);

			logger.info(uuid + "验证退票申请是否为重复申请...");
			Map<String, String> merchantMap = new HashMap<String, String>();
			merchantMap.put("merchant_refund_seq", requestId);// 商户退款流水号
			merchantMap.put("channel", merchantId);// 渠道
			merchantMap.put("merchant_order_id", merchantOrderId);
			int rcount = refundService.queryRefundCountByMerchantSeq(merchantMap);
			logger.info(uuid + "rcount:" + rcount);
			if (rcount > 0) {
				logger.info(
						uuid + "商户退票流水已经发生过退票，拒绝本次操作，merchant_refund_seq=" + requestId + ", channel=" + merchantId + ", merchant_order_id" + merchantOrderId);
				jsonRes.put("merchant_order_id", merchantOrderId);
				jsonRes.put("message", "本次退票请求为重复请求。");
				jsonRes.put("order_id", orderId);
				jsonRes.put("return_code", "000");
				jsonRes.put("trip_no", "");
				jsonRes.put("status", "REPEAT");// 重复申请
				jsonRes.put("fail_reason", "本次退票请求为重复请求，退款流水号已存在。");
				write2Response(response, jsonRes.toString());
				return;
			}

			logger.info(uuid + "验证是否出票完成的，否则不能进行退票申请...");
			Map<String, String> orderMap = new HashMap<String, String>();
			orderMap.put("order_id", orderId);
			orderMap.put("merchant_order_id", merchantOrderId);
			orderMap.put("merchant_id", merchantId);
			String status = refundService.queryOrderStatusById(orderMap);
			logger.info(uuid + "status:" + status);
			if (StringUtils.isEmpty(status) || !TrainConsts.OUT_SUCCESS.equals(status)) {

				logger.info(uuid + "商户退票流水不是出票成功状态，不能申请退票操作，merchant_refund_seq=" + requestId + ", channel=" + merchantId + ", merchant_order_id"
						+ merchantOrderId + ", status=" + status);

				jsonRes.put("merchant_order_id", merchantOrderId);
				jsonRes.put("message", "该订单不是出票成功状态，不能申请退票操作。");
				jsonRes.put("order_id", orderId);
				jsonRes.put("return_code", "000");
				jsonRes.put("trip_no", "");
				jsonRes.put("status", "FAILURE");// 申请失败
				jsonRes.put("fail_reason", "该订单不是出票成功状态，不能申请退票操作。");
				write2Response(response, jsonRes.toString());
				return;

			}

			logger.info(uuid + "计算退票金额...");
			double buyMoney = 0;
			Double refundMoney = 0.0;
			String refundSeq = CreateIDUtil.createID("TK");// 生成退票流水号
			Double feePercent = 0.0;
			List<Map<String, String>> refundList = new ArrayList<Map<String, String>>();
			Map<String, String> refundMap = new HashMap<String, String>();
			logger.info(uuid + "cpList:" + cpList);
			for (String cpId : cpList) {
				logger.info(uuid + "计算cpid:" + cpId);
				// 计算费率
				if ("3".equals(refundOrderType)) {
					logger.info(uuid + "改签退票");
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("order_id", orderId);
					param.put("new_cp_id", cpId);
					Map<String, Object> changeCpInfo = changeService.queryChangeCpInfo(param);
					String start_time = DateUtil.timestampToString((java.sql.Timestamp) changeCpInfo.get("change_from_time"), "yyyy-MM-dd HH:mm:ss");
					feePercent = this.calculateRefundPercent(start_time);
					buyMoney = MathUtil.bigDecimalTODouble((java.math.BigDecimal) changeCpInfo.get("change_buy_money"));
					refundMap.put("refund_type", TrainConsts.REFUND_TYPE_ALTER);// 用户线上退款
				} else {
					logger.info(uuid + "直接退票");
					Map<String, String> timeMap = refundService.querySpecTimeBeforeFrom(orderId);
					String start_time = timeMap.get("travel_time") + " " + timeMap.get("from_time") + ":00";
					feePercent = this.calculateRefundPercent(start_time);
					OrderInfoCp cpInfo = orderService.queryCpInfoByCpId(cpId);
					buyMoney = Double.parseDouble(cpInfo.getBuy_money());
					refundMap.put("refund_type", TrainConsts.REFUND_TYPE_ONLINE);// 用户线上退款
				}
				double sxf = 0.0;
				if (feePercent != 0) {
					sxf = AmountUtil.quarterConvert(AmountUtil.mul(buyMoney, feePercent));// 手续费
					sxf = sxf > 2 ? sxf : 2;
				}
				logger.info(uuid + "手续费:" + sxf);

				refundMoney = AmountUtil.ceil(AmountUtil.sub(buyMoney, sxf));
				logger.info(uuid + "退款金额:" + refundMoney);

				refundMap.put("order_id", orderId);
				refundMap.put("merchant_order_id", merchantOrderId);
				refundMap.put("cp_id", cpId);
				refundMap.put("merchant_refund_seq", requestId);// 商户退款流水号
				refundMap.put("refund_seq", refundSeq + "AA" + cpId.substring(cpId.length() - 2, cpId.length()));// 生成退票流水号;
				refundMap.put("refund_money", String.valueOf(refundMoney));// 退款金额
				refundMap.put("user_remark", comment);
				refundMap.put("refund_purl", refundPictureUrl);
				refundMap.put("refund_status", TrainConsts.REFUND_STATUS_WAIT);// 等待退票

				Map<String, String> cpMap = new HashMap<String, String>();
				cpMap.put("order_id", orderId);
				cpMap.put("cp_id", cpId);

				String oldStatus = refundService.queryRefundStatusByCpId(cpMap);
				logger.info(uuid + "旧的退款状态-oldStatus:" + oldStatus);

				refundMap.put("old_refund_status", oldStatus);//
				refundMap.put("refund_percent", feePercent.toString());
				refundMap.put("notify_url", refundResultUrl);
				refundMap.put("channel", merchantId);// 渠道
				refundList.add(refundMap);

			}

			logger.info(uuid + "生成用户申请退票...");
			boolean ret = refundService.addUserRefundStream(refundList, jsonRes);
			Map<String, String> map = new HashMap<String, String>();
			map.put("order_id", orderId);
			map.put("status", "REFUNDING");
			refundService.updateOrderRefundStatus(map); // 更新订单退款状态
			logger.info(uuid + "ret:" + ret);
			if (ret) {// 申请成功
				jsonRes.put("merchant_order_id", merchantOrderId);
				jsonRes.put("message", "");
				jsonRes.put("order_id", orderId);
				jsonRes.put("return_code", "000");
				jsonRes.put("trip_no", refundSeq);
				jsonRes.put("status", "SUCCESS");// 申请成功
				jsonRes.put("fail_reason", "");
				logger.info(uuid + "申请成功-2:" + jsonRes.toString());
				write2Response(response, jsonRes.toString());
			} else {// 失败
				logger.info(uuid + "申请失败:" + jsonRes.toString());
				write2Response(response, jsonRes.toString());
			}

		} catch (Exception e) {
			logger.info(uuid + "对外退票接口操作异常:" + e.getClass().getSimpleName(), e);

			jsonRes.put("merchant_order_id", merchantOrderId);
			jsonRes.put("message", "系统错误，未知服务异常。");
			jsonRes.put("order_id", orderId);
			jsonRes.put("return_code", "001");
			jsonRes.put("trip_no", "");
			jsonRes.put("status", "FAILURE");// 申请失败
			jsonRes.put("fail_reason", "程序未知异常，请重新申请。");
			write2Response(response, jsonRes.toString());
		}
	}

	/**
	 * 参数是否为空
	 * 
	 * @param param_val
	 * @param param_name
	 * @param jsonRes
	 * @param uuid
	 * @return
	 */
	private boolean checkParamEmpty(String param_val, String param_name, JSONObject jsonRes, String uuid) {
		if (StringUtils.isEmpty(param_val)) {
			logger.info(uuid + "输入参数错误，" + param_name + "不能为空");

			jsonRes.put("merchant_order_id", "");
			jsonRes.put("message", "输入参数错误，" + param_name + "不能为空");
			jsonRes.put("order_id", "");
			jsonRes.put("return_code", "003");// 输入参数错误。
			jsonRes.put("trip_no", "");
			jsonRes.put("status", "FAILURE");// 申请失败
			jsonRes.put("fail_reason", param_name + "不能为空");
			return true;
		} else {
			return false;
		}
	}

	/**
	 * private void eopRefund(Map<String,String> map){ RefundBean refundBean =
	 * new RefundBean(); refundBean.setService("refund");
	 * refundBean.setSign_type("MD5");
	 * refundBean.setTimestamp(DateUtil.dateToString(new Date(),
	 * DateUtil.DATE_FMT2)); refundBean.setPartner_id(partner_id);
	 * refundBean.setInput_charset("utf-8");
	 * refundBean.setAsp_refund_number(refund_seq);
	 * refundBean.setAsp_refund_money(notifyMap.get("refund_money"));
	 * refundBean.setRefund_reason(notifyMap.get("refund_reason"));
	 * refundBean.setEop_order_id(notifyMap.get("eop_order_id"));
	 * 
	 * RefundBean reBean = ASPUtil.refund(refundBean,
	 * notifyMap.get("notify_url")+"?data_type=JSON");
	 * if("SUCCESS".equals(reBean.getResult_code())){ //更新通知完成 Map<String,
	 * String> map = new HashMap<String, String>(); map.put("notify_id",
	 * notify_id); map.put("order_id", order_id); map.put("eop_refund_seq",
	 * reBean.getEop_refund_seq()); map.put("notify_status",
	 * TrainConsts.REFUND_NOTIFY_FINISH);
	 * refundService.updateEopRefundNotfiyFinish(map); logger.info("退票结果通知平台成功"
	 * + "订单号：" + order_id + "，退款流水号：" + refund_seq); }else
	 * if("REFUNDING".equals(reBean.getResult_code())){ logger.info("退票结果通知平台重复"
	 * + "订单号：" + order_id + "，退款流水号：" + refund_seq); }else{
	 * logger.info("退票结果通知商户失败" + "订单号：" + order_id + "，退款流水号：" +
	 * refund_seq+";失败原因："+reBean.getResult_desc()); } }
	 */

	/**
	 * 通过匹配已退款cp list 和请求 cp list 获取入库 cp list
	 * 
	 * @param uuid
	 */
	private List<String> getNowRefundCpList(String orderId, List<String> actionRefundCpList, List<String> hadRefundCpList, String uuid) {
		List<String> cpList = new ArrayList<String>();
		int hadSize = hadRefundCpList.size();
		int actionSize = actionRefundCpList.size();
		List<String> refundSeqList = new ArrayList<String>();
		if (hadSize == 0) {
			return actionRefundCpList;
		} else {
			boolean weaUp = false;
			String refundSeq = "";
			for (int i = 0; i < actionSize; i++) {
				String cp = actionRefundCpList.get(i);
				if (!hadRefundCpList.contains(cp)) {
					// 请求退款 原数据没有
					cpList.add(cp);
				} else {
					// 原有 对拒绝退款状态进行更新操作
					Map<String, String> param = new HashMap<String, String>();
					param.put("order_id", orderId);
					param.put("cp_id", cp);
					int count = orderService.updateRefuseOrderStatus(param);
					refundSeq = orderService.queryRefundStreamSeq(param);
					refundSeqList.add(refundSeq);
					if (count != 0) {
						logger.info(uuid + "更新退款:" + orderId + "/" + cp);
						weaUp = true;
					}
				}
			}
			if (cpList.size() == 0 && weaUp) {
				cpList.add("success_" + refundSeq);
			}
			return cpList;
		}
	}
	/*
	 * 计算费率 春运期间： 1、改签到春运期间，退票时收取20%手续费 2、其他情况参照非春运期间
	 * 
	 * 非春运期间 ①开车前15天以上的，退票时不收退票费； ②开车前48小时以上、不足15天的，退票时收取票价5%的退票费；
	 * ③开车前24以上，不足48小时的，退票时收取票价10%退票费。 4开车前不足24小时，退票时收取20%退票费
	 */

	private Double calculateRefundPercent(String startTime) {
		String from_15d = DateUtil.dateAddDaysFmt3(startTime, "-15");
		String from_24 = DateUtil.dateAddDaysFmt3(startTime, "-1");
		String from_48 = DateUtil.dateAddDaysFmt3(startTime, "-2");

		double feePercent = 0;
		if (new Date().before(DateUtil.stringToDate(from_15d, "yyyy-MM-dd HH:mm:ss"))) {
			feePercent = 0.0;
		} else if (new Date().before(DateUtil.stringToDate(from_48, "yyyy-MM-dd HH:mm:ss"))) {
			feePercent = 0.05;
		} else if (new Date().before(DateUtil.stringToDate(from_24, "yyyy-MM-dd HH:mm:ss"))) {
			feePercent = 0.1;
		} else {
			feePercent = 0.2;
		}
		return feePercent;
	}
}
