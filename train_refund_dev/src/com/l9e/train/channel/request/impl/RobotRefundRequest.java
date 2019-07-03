package com.l9e.train.channel.request.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.l9e.train.po.DeviceWeight;
import com.l9e.train.po.OrderCP;
import com.l9e.train.po.ResultCP;
import com.l9e.train.po.ReturnRefundInfo;
import com.l9e.train.po.ReturnRefundPasEntity;
import com.l9e.train.po.TrainConsts;
import com.l9e.train.po.Worker;
import com.l9e.train.service.impl.TrainServiceImpl;
import com.l9e.train.util.HttpUtil;
import com.l9e.train.util.MD5;

public class RobotRefundRequest extends RequestImpl {

	private Logger logger = Logger.getLogger(this.getClass());

	public RobotRefundRequest(Worker worker) {
		super(worker);
	}

	@Override
	public ResultCP request(OrderCP order, String weight) {
		String prefix = "[";
		Random random = new Random();
		for (int i = 0; i < 6; i++) {
			prefix = prefix + random.nextInt(9);
		}
		prefix = prefix + "] ";
		String orderId = order.getOrderId();
		logger.info(prefix + "--------- 退票流程开始-" + orderId + " ---------");
		TrainServiceImpl trainImpl = new TrainServiceImpl();

		/*
		 * 查询返回日志的list--》 然而并无卵用 SysSettingServiceImpl sysImpl = new
		 * SysSettingServiceImpl(); try { sysImpl.queryLogList();
		 * }catch(Exception e){ logger.error("获取出票系统日志异常！",e); }
		 * List<ReturnOptlog> list_return = sysImpl.getList_return();
		 */
		/*
		 * 查询系统设置的预订机器人版本 --》 然而并无卵用 int resend_num = 0; if(null !=
		 * MemcachedUtil.getInstance().getAttribute("refund_resend_times_"+order
		 * .getOrderId())){ resend_num =
		 * Integer.valueOf(String.valueOf(MemcachedUtil.getInstance().
		 * getAttribute("refund_resend_times_"+order.getOrderId()))); } try {
		 * sysImpl.querySysVal("rand_code_type");
		 * 
		 * if(StringUtils.isNotEmpty(sysImpl.getSysVal())){
		 * order.setInputCode(sysImpl.getSysVal()); } } catch (Exception e) {
		 * e.printStackTrace(); }
		 */

		String optlog = "";
		String refundMoney = order.getRefundMoney();
		String channel = order.getChannel();
		// 是否是咱们自己改签订单的标识0：否 1：是
		String alterMyself = order.getAlterMyself();
		logger.info(prefix + "refundMoney:" + refundMoney + ", channel:" + channel + ", alterMyself:" + alterMyself);

		ResultCP resultCp = new ResultCP();
		resultCp.setOrderId(orderId);
		resultCp.setWorker(worker);
		String cpId = order.getCpId();
		try {
			// 判断JAVA-PC 还是LUA 退票
			String script = worker.getScript();
			logger.info(prefix + "机器人脚本类型:" + script + " [1:LUA, 2:JAVA]");
			String jsonStr = "";
			String url = "";
			if (StringUtils.isNotBlank(script) && "2".equals(script)) {
				String publicIp = worker.getPublicIp();
				url = "http://" + publicIp + ":18050/robot/refund/pc";

				JSONObject requestJson = new JSONObject();
				requestJson.put("orderId", orderId);
				requestJson.put("publicIp", "");
				requestJson.put("privateIp", "");

				JSONObject accountJson = new JSONObject();
				accountJson.put("username", order.getAccountName());
				accountJson.put("password", order.getAccountPwd());
				requestJson.put("account", accountJson);

				JSONObject ticketJson = new JSONObject();
				ticketJson.put("sequence", order.getOutTicketBillno());
				ticketJson.put("departureDate", order.getTrainDate());
				ticketJson.put("trainCode", order.getTrainNo());
				// requestJson.put("fromStationName", order.getOutTicketTime());
				// requestJson.put("toStationName", order.getOutTicketTime());

				JSONArray passengers = new JSONArray();
				JSONObject passenger = new JSONObject();
				passenger.put("name", order.getUserName());
				passenger.put("cardType", order.getIdsType());
				passenger.put("cardNo", order.getIdsCard().toUpperCase().trim());
				passenger.put("ticketType", order.getTicketType());
				passenger.put("seatType", seatTypeTurn(order.getSeatType()));
				passenger.put("seatName", order.getSeatNo());
				passenger.put("boxName", order.getTrainBox());
				passenger.put("passengerNo", order.getCpId());

				passengers.add(passenger);
				ticketJson.put("passengers", passengers);
				requestJson.put("data", ticketJson);

				String parameter = requestJson.toJSONString();
				logger.info(prefix + "JAVA-PC机器人退票请求路径和参数:" + url + ":" + parameter);
				trainImpl.insertHistory(orderId, cpId, "JAVA-PC退票:" + publicIp);

				jsonStr = HttpUtil.postJson(url, "UTF-8", parameter);
				logger.info(prefix + "JAVA-PC返回结果:" + jsonStr);

				JSONObject resultJson = new JSONObject();
				resultJson.put("ErrorCode", 0);
				JSONArray errorInfo = new JSONArray();
				JSONObject info = new JSONObject();
				if (StringUtils.isBlank(jsonStr) || !jsonStr.startsWith("{")) {
					info.put("retValue", "failure");
					info.put("retInfo", "返回结果不符合JSON格式");
				} else {
					JSONObject robotJson = JSONObject.parseObject(jsonStr);
					String status = robotJson.getString("status");
					if (StringUtils.isBlank(status)) {
						info.put("retValue", "failure");
						info.put("retInfo", "机器人返回状态为空");
					} else if ("0000".equals(status)) {
						robotJson = robotJson.getJSONObject("body");
						info.put("orderId", orderId);
						info.put("summoney", 0);
						info.put("from", "");
						info.put("seattime", "");
						info.put("trainno", "");
						info.put("to", "");
						info.put("contactsnum", 0);
						info.put("outTicketBillno", "");

						JSONArray cps = new JSONArray();
						JSONObject cp = new JSONObject();

						robotJson = robotJson.getJSONArray("passengers").getJSONObject(0);
						cp.put("cpId", robotJson.getString("passengerNo"));
						cp.put("name", robotJson.getString("name"));
						cp.put("idtype", robotJson.getString("cardType"));
						cp.put("status", "00");
						cp.put("resign_flag", "2");
						cp.put("return_flag", "Y");
						cp.put("tradeNo", robotJson.getString("refundNo"));
						cp.put("refund_money", robotJson.getDouble("refundMoney"));
						cp.put("trainbox", robotJson.getString("boxName"));
						cp.put("seatNo", robotJson.getString("seatName"));
						cp.put("seatNo", robotJson.getString("seatName"));
						cp.put("seattype", robotJson.getString("seatType"));
						cp.put("tickettype", robotJson.getString("ticketType"));
						cps.add(cp);
						info.put("cps", cps);
					} else {
						String message = robotJson.getString("message");
						info.put("retValue", "failure");
						info.put("retInfo", message);
					}
				}
				errorInfo.add(info);
				resultJson.put("ErrorInfo", errorInfo);
				jsonStr = resultJson.toJSONString();
			} else {
				String workerExt = worker.getWorkerExt();
				url = getRefundUrl(order, workerExt, weight);
				logger.info(prefix + "LUA退票请求路径和参数:" + url);
				trainImpl.insertHistory(orderId, cpId, "LUA退票:" + workerExt);
				jsonStr = HttpUtil.sendByGet(url, "UTF-8", "200000", "200000");// 调用接口
				logger.info(prefix + "LUA返回结果:" + jsonStr);
				jsonStr = jsonStr.replace("\\\"", "\"").replace("[\"{", "[{").replace("}\"]", "}]");
			}
			logger.info(prefix + "最终退票返回结果:" + jsonStr);

			if ("TIMEOUT".equals(jsonStr)) {
				logger.info(prefix + "连接机器人超时:" + jsonStr + cpId);
				trainImpl.insertHistory(orderId, cpId, "该机器人连接超时，若该机器此异常出现次数过多，请联系技术人员!");
				resultCp.setCpId(cpId);
				resultCp.setRetValue(ResultCP.FAILURE);
				resultCp.setErrorInfo("该机器人连接超时，若该机器此异常出现次数过多，请联系技术人员！");
			}

			if (jsonStr.contains("<html>") || jsonStr.contains("<HTML>")) {
				if (jsonStr.contains("springframework")) {
					logger.info(prefix + "发起机器退票失败【服务异常】,转为人工处理---" + jsonStr + cpId);
					optlog = "发起机器退票失败【服务异常】,转为人工处理!";
				} else {
					optlog = "发起机器退票失败【12306异常】,转为人工处理!";
					logger.info(prefix + "发起机器退票失败【12306异常】,转为人工处理---" + jsonStr + cpId);
				}
				trainImpl.insertHistory(orderId, cpId, optlog);
				resultCp.setCpId(cpId);
				resultCp.setRetValue(ResultCP.FAILURE);
				resultCp.setErrorInfo(optlog);
			}

			if (!jsonStr.contains("\"ErrorCode\":0")) {
				logger.info(prefix + "机器人异常:" + jsonStr + cpId);
				trainImpl.insertHistory(orderId, cpId, "机器人异常:" + jsonStr);
				resultCp.setCpId(cpId);
				resultCp.setRetValue(ResultCP.RESEND);
				resultCp.setErrorInfo("机器人异常:" + jsonStr);
				// resultCp_list.add(ResultCp);
			}

			ObjectMapper mapper = new ObjectMapper();
			ReturnRefundInfo refundResult = mapper.readValue(jsonStr, ReturnRefundInfo.class);
			if (jsonStr.contains("\"retValue\":\"failure\"")) {
				optlog = "机器退票失败,转为人工处理:" + refundResult.getErrorInfo().get(0).getRetInfo();
				trainImpl.insertHistory(orderId, cpId, optlog);
				logger.info(prefix + "机器退票失败,转为人工处理---" + jsonStr + cpId);
				resultCp.setCpId(cpId);
				resultCp.setRetValue(ResultCP.FAILURE);
				resultCp.setErrorInfo(optlog);
			} else if (jsonStr.contains("已出票")) {
				optlog = "已出票,转为人工处理！ " + refundResult.getErrorInfo().get(0).getRetInfo();
				trainImpl.insertHistory(orderId, cpId, optlog);
				logger.info(prefix + "已出票,转为人工处理---" + jsonStr + cpId);
				resultCp.setCpId(cpId);
				resultCp.setRetValue(ResultCP.MANUAL);
				resultCp.setErrorInfo(optlog);
				resultCp.setReturnOptlog(TrainConsts.RETURN_G3);
			} else if (jsonStr.contains("已退票")) {
				optlog = "已退票,转为人工处理！ " + refundResult.getErrorInfo().get(0).getRetInfo();
				trainImpl.insertHistory(orderId, cpId, optlog);
				logger.info(prefix + "已退票,转为人工处理---" + jsonStr + cpId);
				resultCp.setCpId(cpId);
				resultCp.setRetValue(ResultCP.MANUAL);
				resultCp.setErrorInfo(optlog);
				resultCp.setReturnOptlog(TrainConsts.RETURN_G4);
			} else if (jsonStr.contains("旅游旺季")) {
				optlog = "旅游旺季,转为人工处理！ " + refundResult.getErrorInfo().get(0).getRetInfo();
				trainImpl.insertHistory(orderId, cpId, optlog);
				logger.info(prefix + "旅游旺季,转为人工处理---" + jsonStr + cpId);
				resultCp.setCpId(cpId);
				resultCp.setRetValue(ResultCP.MANUAL);
				resultCp.setErrorInfo(optlog);
				resultCp.setReturnOptlog(TrainConsts.REFUNN_G8);
			} else {
				List<ReturnRefundPasEntity> list_pas = refundResult.getErrorInfo().get(0).getCps();
				if (list_pas.size() == 0) {
					logger.info(prefix + "机器退票返回结果为空！请人工确认并处理" + cpId);
					optlog = "机器退票返回结果为空！请人工确认并处理！ " + refundResult.getErrorInfo().get(0).getRetInfo();
					trainImpl.insertHistory(orderId, cpId, optlog);
					resultCp.setCpId(cpId);
					resultCp.setRetValue(ResultCP.FAILURE);
					resultCp.setErrorInfo(optlog);
				}
				for (ReturnRefundPasEntity rrps : list_pas) {
					logger.info(prefix + "..........");
					String status = rrps.getStatus();
					String cp_id = rrps.getCpId();
					String refund12306Money = rrps.getRefund_money();
					String tradeNo = rrps.getTradeNo();

					logger.info(prefix + "status:" + status);
					logger.info(prefix + "cp_id:" + cp_id);
					logger.info(prefix + "refundMoney:" + refundMoney);
					logger.info(prefix + "refund12306Money:" + refund12306Money);
					logger.info(prefix + "tradeNo:" + tradeNo);
					logger.info(prefix + "channel:" + channel);
					logger.info(prefix + "alterMyself:" + alterMyself);
					if ("00".equals(status)) {
						// 更新车票表退票后信息
						Map<String, String> refund_map = new HashMap<String, String>();
						refund_map.put("order_id", orderId);
						refund_map.put("cp_id", cp_id);
						refund_map.put("refund_12306_money", refund12306Money);
						refund_map.put("refund_12306_seq", tradeNo);
						// 新增退票款,是否是咱们自己改签的标识参数
						refund_map.put("refund_money", refundMoney);
						refund_map.put("channel", channel);
						refund_map.put("alter_myself", alterMyself);

						trainImpl.updateCPOrderRefund(prefix, refund_map);
						logger.info(prefix + "退票完成，机器自动审核" + cpId);
						optlog = "退票完成，准备机器审核！ ";
						trainImpl.insertHistory(orderId, cpId, optlog);
						resultCp.setCpId(cpId);
						resultCp.setRetValue(ResultCP.SUCCESS);
						resultCp.setErrorInfo(optlog);
						continue;
					}
					if ("09".equals(status)) {
						// 更新车票表退票后信息
						Map<String, String> refund_map = new HashMap<String, String>();
						refund_map.put("order_id", orderId);
						refund_map.put("cp_id", cp_id);
						refund_map.put("refund_12306_money", refund12306Money);
						refund_map.put("refund_12306_seq", tradeNo);
						// 新增退票款参数,是否是咱们自己改签的标识参数
						refund_map.put("refund_money", refundMoney);
						refund_map.put("channel", channel);
						refund_map.put("alter_myself", alterMyself);

						trainImpl.updateCPOrderRefund(prefix, refund_map);
						logger.info(prefix + "机器自动审核退票完成，请人工审核" + cpId);
						optlog = "机器自动审核退票完成，请人工审核！ ";
						trainImpl.insertHistory(orderId, cpId, optlog);
						resultCp.setCpId(cpId);
						resultCp.setRetValue(ResultCP.SUCCESS);
						resultCp.setErrorInfo(optlog);
						continue;
					} else if ("05".equals(status)) {
						logger.info(prefix + "退票失败，请人工处理" + cpId);
						optlog = "退票失败，请人工处理！ " + rrps.getMsg();
						trainImpl.insertHistory(orderId, cpId, optlog);

						// 更新车票表退票后信息
						Map<String, String> refund_map = new HashMap<String, String>();
						refund_map.put("order_id", orderId);
						refund_map.put("cp_id", cp_id);
						refund_map.put("refund_12306_money", refund12306Money);
						refund_map.put("refund_12306_seq", tradeNo);
						// 新增退票款,是否是咱们自己改签的标识参数
						refund_map.put("refund_money", refundMoney);
						refund_map.put("channel", channel);
						refund_map.put("alter_myself", alterMyself);

						trainImpl.updateCPOrderRefund(prefix, refund_map);

						resultCp.setCpId(cpId);
						resultCp.setRetValue(ResultCP.FAILURE);
						resultCp.setErrorInfo(optlog);
						continue;
					} else if ("04".equals(status)) {
						if ("N".equals(rrps.getReturn_flag())) {
							logger.info(prefix + "12306上不能退票，请人工处理" + cpId);
							optlog = "12306上不能退票，请人工处理！ " + rrps.getMsg();
						} else {
							logger.info(orderId + "退票失败，请人工处理" + cpId);
							optlog = "退票失败，请人工处理！ " + rrps.getMsg();
						}
						trainImpl.insertHistory(orderId, cpId, optlog);

						// 更新车票表退票后信息
						Map<String, String> refund_map = new HashMap<String, String>();
						refund_map.put("order_id", orderId);
						refund_map.put("cp_id", cp_id);
						refund_map.put("refund_12306_money", "0.00");
						refund_map.put("refund_12306_seq", tradeNo);
						// 新增退票款,是否是咱们自己改签的标识参数
						refund_map.put("refund_money", refundMoney);
						refund_map.put("channel", channel);
						refund_map.put("alter_myself", alterMyself);
						trainImpl.updateCPOrderRefund(prefix, refund_map);
						resultCp.setCpId(cpId);
						resultCp.setRetValue(ResultCP.MANUAL);
						resultCp.setErrorInfo(optlog);
						continue;
					} else {
						logger.info(prefix + "退票失败，请人工处理" + cpId);
						optlog = "退票失败，请人工处理！ " + rrps.getMsg();
						trainImpl.insertHistory(orderId, cpId, optlog);
						resultCp.setCpId(cpId);
						resultCp.setRetValue(ResultCP.FAILURE);
						resultCp.setErrorInfo(optlog);
						continue;
					}
				}
			}
		} catch (Exception e) {
			logger.error(prefix + "退票失败，请人工处理", e);
			optlog = "退票失败，请人工处理！ ";
			try {
				trainImpl.insertHistory(orderId, cpId, optlog);
			} catch (Exception e1) {
				logger.info(prefix + "插入日志异常", e);
			}
			resultCp.setCpId(cpId);
			resultCp.setRetValue(ResultCP.MANUAL);
			resultCp.setErrorInfo(optlog);
		}

		return resultCp;
	}

	/**
	 * alterURL
	 * 
	 * @param map
	 * @return
	 */
	protected String getRefundUrl(OrderCP refund, String interfaceUrl, String weight) {
		String scriptPath = "refund.lua";
		String accountPwd = refund.getAccountPwd();
		if (DeviceWeight.WEIGHT_PC.equals(weight)) {
			/* PC出票 */
		} else if (DeviceWeight.WEIGHT_APP.equals(weight)) {
			/* APP出票 */
			scriptPath = "appRefund.lua";
			accountPwd = MD5.getMd5_UTF8(accountPwd);
		}

		String url = new String(interfaceUrl);
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		sb1.append(refund.getAccountName()).append("|").append(accountPwd).append("|").append(refund.getOrderId()).append("|")
				.append(refund.getOutTicketBillno()).append("|").append(refund.getInputCode()).append("|")
				// 这个日期，傻逼写的。是一个毫无用处的日期
				.append(refund.getOutTicketTime());
		// .append("yyyy-MM-dd");

		sb2.append(refund.getCpId()).append("|").append(refund.getUserName()).append("|").append(refund.getTicketType()).append("|").append(refund.getIdsType())
				.append("|").append(refund.getIdsCard()).append("|").append(seatTypeTurn(refund.getSeatType())).append("|").append(refund.getTrainBox())
				.append("|").append(refund.getSeatNo());
		String param1 = "";
		try {
			param1 = URLEncoder.encode(sb1.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		String param2 = "";
		try {
			param2 = URLEncoder.encode(sb2.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		logger.info("发起退票的车次信息:Param1=" + sb1.toString() + "Param2=" + sb2.toString());
		String session_id = String.valueOf(System.currentTimeMillis());
		logger.info("[机器人url]=" + url + " [sessionid]:" + session_id);

		url += "?ScriptPath=" + scriptPath + "&SessionID=$session_id&Timeout=240000&ParamCount=2&Param1=$param1&Param2=$param2";

		String refund_url = url.replace("$session_id", session_id).replace("$param1", param1).replace("$param2", param2);
		return refund_url;
	}

	// 列车席位转换--针对卧铺
	public static String seatTypeTurn(String seat_type) {
		return seat_type.contains("6") ? "6" : seat_type.contains("5") ? "5" : seat_type.contains("4") ? "4" : seat_type;
	}

}
