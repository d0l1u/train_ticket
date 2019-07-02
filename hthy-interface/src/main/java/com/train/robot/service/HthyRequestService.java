package com.train.robot.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.train.commons.util.DateUtil;
import com.train.commons.util.HttpUtil;
import com.train.commons.util.MD5Util;
import com.train.robot.em.CardType;
import com.train.robot.em.SeatType;
import com.train.robot.em.TicketType;
import com.train.robot.util.PropertyUtil;

/**
 * HthyRequestService
 *
 * @author taokai3
 * @date 2018/7/4
 */
public class HthyRequestService {

	private Logger logger = LoggerFactory.getLogger(HthyRequestService.class);
	private String logid;

	private HttpUtil httpUtil = new HttpUtil();

	public HthyRequestService(String logid) {
		this.logid = logid;
	}

	// 航天华有分配帐号
	private static final String partnerId = PropertyUtil.getValue("interface.partnerId");

	// 航天华有分配key
	private static final String key = PropertyUtil.getValue("interface.key");

	// 下单接口
	private static final String hthyUrl = PropertyUtil.getValue("interface.order");

	// 下单占座回调地址
	private static final String orderCallback = PropertyUtil.getValue("callback.order");

	// 退票回调地址
	private static final String refundCallback = PropertyUtil.getValue("callback.refund");

	private static final String changeOrderCallback = PropertyUtil.getValue("callback.changeOrder");

	private static final String changeConfirmCallback = PropertyUtil.getValue("callback.changeConfirm");

	/**
	 * 占位下单
	 *
	 * @param paramJson
	 * @return
	 */
	public String order(JSONObject paramJson) {
		// json解析
		JSONObject accountJson = paramJson.getJSONObject("account");// 12306帐号相关数据
		JSONObject trainJson = paramJson.getJSONObject("data");// 预定车次相关数据
		JSONArray passengersJson = trainJson.getJSONArray("passengers");// 预定人相关数据

		// 创建接口基础参数
		JSONObject requestJson = createParameter("train_order");

		String trainCode = trainJson.getString("trainCode");
		requestJson.put("orderid", paramJson.getString("myOrderId")); // 使用方订单号
		requestJson.put("checi", trainCode);// 车次
		requestJson.put("from_station_code", trainJson.getString("fromStationCode")); // 出发站简码(非必选)
		requestJson.put("from_station_name", trainJson.getString("fromStationName")); // 出发站名称
		requestJson.put("to_station_code", trainJson.getString("toStationCode")); // 到达站简码(非必选)
		requestJson.put("to_station_name", trainJson.getString("toStationName")); // 到达站名称
		requestJson.put("train_date", trainJson.getString("departureDate")); // 乘车日期（yyyy-MM-dd）
		requestJson.put("callbackurl", orderCallback);// 占座成功回调地址[选填]
		if (accountJson != null) {
			requestJson.put("LoginUserName", accountJson.getString("username"));// 12306用户名
			requestJson.put("LoginUserPassword", accountJson.getString("password"));// 12306密码
		} else {
			requestJson.put("LoginUserName", "");// 12306用户名
			requestJson.put("LoginUserPassword", "");// 12306密码
		}

		String chooseSeat = trainJson.getString("chooseSeats");
		if (StringUtils.isNotBlank(chooseSeat)) {
			requestJson.put("is_choose_seats", true);// 是否需要选座
			requestJson.put("choose_seats", chooseSeat);// 选座STR（比如：1A1D2B2C2F，就是选5个坐席），选座个数要与乘客数量应该一致
		} else {
			requestJson.put("is_choose_seats", false);// 是否需要选座
			requestJson.put("choose_seats", "");// 选座STR（比如：1A1D2B2C2F，就是选5个坐席），选座个数要与乘客数量应该一致
		}
		requestJson.put("is_accept_standing", false);// true要;false或者不传不要

		// 存放乘客信息(多条)
		JSONArray passengersArray = new JSONArray();
		for (Object object : passengersJson) {
			JSONObject passenger = (JSONObject) object;
			JSONObject passengerJson = new JSONObject(true);
			passengerJson.put("passengersename", passenger.getString("name"));// 乘客姓名
			passengerJson.put("passportseno", passenger.getString("cardNo"));// 乘客证件号
			// ----获取证件类型进行转换
			CardType cardType = CardType.getByKy(passenger.getString("cardType"));
			passengerJson.put("passporttypeseidname", cardType.getTitle());// 乘客证件类型名
			passengerJson.put("passporttypeseid", cardType.getHthy());// 乘客证件类型CODE
			passengerJson.put("passengerid", passenger.getString("passengerNo"));// 乘客唯一标识（字符串内为随机长数字）
			// ----获取坐席类型进行格式转换
			String seatType = passenger.getString("seatType");
			if (seatType.equals("9")) {
				// 无座，判断 是 二等座 还是 硬座
				if (trainCode.startsWith("C") || trainCode.startsWith("D") || trainCode.startsWith("G")) {
					passengerJson.put("zwname", SeatType.GAOTIE_NO_SEAT.getTitle());// 坐席名
					passengerJson.put("zwcode", SeatType.GAOTIE_NO_SEAT.getHthy());// 坐席类型CODE
				} else {
					passengerJson.put("zwname", SeatType.COMMONLY_NO_SEAT.getTitle());
					passengerJson.put("zwcode", SeatType.COMMONLY_NO_SEAT.getHthy());
				}
				// 重置备选坐席，备选无座
				requestJson.put("is_accept_standing", true);// true要;false或者不传不要
			} else {
				passengerJson.put("zwname", SeatType.getByKy(seatType).getTitle());
				passengerJson.put("zwcode", SeatType.getByKy(seatType).getHthy());
			}
			passengerJson.put("price", 0);// 票价(这里必填，不了解则传0)
			// -----获取票类型进行转换-------
			passengerJson.put("piaotype", TicketType.getByKy(passenger.getString("ticketType")).getHthy());// 票类型(成人票、儿童票之类)
			passengerJson.put("cxin", "");// 传空字符串
			// 学生票 TODO
			passengersArray.add(passengerJson);
		}
		requestJson.put("passengers", passengersArray);// 乘客信息
		String requestStr = "jsonStr=" + requestJson.toJSONString();
		logger.info("{}占位接口参数:{}", logid, requestStr);

		String httpResult = httpUtil.doHttpPost(hthyUrl, requestStr, 1000 * 30, false, "UTF-8");
		// {"orderid":"T1609086DC260C00B4FD0434D0B3AD04aaaaaaaA70","code":"802","msg":"创建订单成功","success":true}
		logger.info("{}占位接口响应结果:{}", logid, httpResult);
		return httpResult;
	}

	/**
	 * 取消订单
	 *
	 * @param orderId
	 * @param supplierId
	 * @return
	 */
	public String cancel(String orderId, String supplierId) {
		JSONObject requestJson = createParameter("train_cancel");
		requestJson.put("orderid", orderId); // 使用方订单号
		requestJson.put("transactionid", supplierId);// 交易单号
		requestJson.put("callbackurl", "");// 回调地址(可为空)
		String requestStr = "jsonStr=" + requestJson.toJSONString();
		logger.info("{}取消接口参数:{}", logid, requestStr);
		String httpResult = httpUtil.doHttpPost(hthyUrl, requestStr, 1000 * 120, false, "UTF-8");
		logger.info("{}取消接口响应结果:{}", logid, httpResult);
		return httpResult;
	}

	/**
	 * 确认出票
	 * 
	 * @param orderId
	 * @param supplierId
	 * @return
	 */
	public String confirm(String orderId, String supplierId) {
		JSONObject requestJson = createParameter("train_confirm");
		requestJson.put("transactionid", supplierId);
		requestJson.put("orderid", orderId);
		requestJson.put("callbackurl", "");
		String requestStr = "jsonStr=" + requestJson.toJSONString();
		logger.info("{}出票接口参数:{}", logid, requestStr);
		String httpResult = httpUtil.doHttpPost(hthyUrl, requestStr, 1000 * 30, false, "UTF-8");
		logger.info("{}出票接口响应结果:{}", logid, httpResult);
		return httpResult;
	}

	public String refund(JSONObject paramJson) {
		// json解析
		JSONObject accountJson = paramJson.getJSONObject("account");// 12306帐号相关数据
		JSONArray passengerArray = paramJson.getJSONArray("passengers");// 预定人相关数据

		JSONObject requestJson = createParameter("return_ticket");
		requestJson.put("orderid", paramJson.getString("myOrderId"));
		requestJson.put("transactionid", paramJson.getString("supplierOrderId"));
		requestJson.put("ordernumber", paramJson.getString("sequence"));
		requestJson.put("reqtoken", String.valueOf(System.nanoTime()));
		requestJson.put("callbackurl", refundCallback);
		if (accountJson != null && !accountJson.isEmpty()) {
			requestJson.put("LoginUserName", accountJson.getString("username"));
			requestJson.put("LoginUserPassword", accountJson.getString("password"));
		} else {
			requestJson.put("LoginUserName", "");
			requestJson.put("LoginUserPassword", "");
		}

		JSONArray passengerList = new JSONArray();
		for (int i = 0; i < passengerArray.size(); i++) {
			JSONObject json = passengerArray.getJSONObject(i);
			JSONObject passengerJson = new JSONObject(true);
			passengerJson.put("passengername", json.getString("name"));
			passengerJson.put("ticket_no", json.getString("subSequence"));
			passengerJson.put("passportseno", json.getString("cardNo"));
			passengerJson.put("passporttypeseid", CardType.getByKy(json.getString("cardType")).getHthy());
			passengerList.add(passengerJson);
		}

		requestJson.put("tickets", passengerList);
		String requestStr = "jsonStr=" + requestJson.toJSONString();
		logger.info("{}退票接口参数:{}", logid, requestStr);
		String httpResult = httpUtil.doHttpPost(hthyUrl, requestStr, 1000 * 30, false, "UTF-8");
		logger.info("{}退票接口响应结果:{}", logid, httpResult);
		return httpResult;
	}

	public String changeOrder(JSONObject paramJson) {
		JSONObject accountJson = paramJson.getJSONObject("account");// 12306帐号相关数据
		JSONArray passengerArray = paramJson.getJSONArray("passengers");// 预定人相关数据

		JSONObject requestJson = createParameter("train_request_change");

		requestJson.put("orderid", paramJson.getString("myOrderId"));
		requestJson.put("transactionid", paramJson.getString("supplierId"));
		requestJson.put("ordernumber", paramJson.getString("sequence"));
		requestJson.put("reqtoken", paramJson.getString("reqtoken"));
		requestJson.put("callbackurl", changeOrderCallback);
		if (accountJson != null && !accountJson.isEmpty()) {
			requestJson.put("LoginUserName", accountJson.getString("username"));
			requestJson.put("LoginUserPassword", accountJson.getString("password"));
		} else {
			requestJson.put("LoginUserName", "");
			requestJson.put("LoginUserPassword", "");
		}

		requestJson.put("isTs", false);// 是否变更到站，true/false
		String trainCode = paramJson.getString("newTrainCode");
		requestJson.put("change_checi", trainCode);// 改签车次
		// 改签发车时间，例：yyyy-MM-dd HH:mm:ss
		requestJson.put("change_datetime", paramJson.getString("newDepartureDate") + " 00:00:00");
		requestJson.put("from_station_code", paramJson.getString("newFromStationCode")); // 出发站简码(非必选)
		requestJson.put("from_station_name", paramJson.getString("newFromStationName")); // 出发站名称
		requestJson.put("to_station_code", paramJson.getString("newToStationCode")); // 到达站简码(非必选)
		requestJson.put("to_station_name", paramJson.getString("newToStationName")); // 到达站名称
		requestJson.put("isasync", "Y");// 异步改签，固定值（Y）

		// 乘客信息
		String oldSeatType = "";
		String newSeatType = "";
		JSONArray passengerList = new JSONArray();
		for (int i = 0; i < passengerArray.size(); i++) {
			JSONObject passenger = passengerArray.getJSONObject(i);
			JSONObject passengerJson = new JSONObject(true);
			passengerJson.put("passengersename", passenger.getString("name"));// 乘客姓名
			passengerJson.put("passportseno", passenger.getString("cardNo"));// 乘客证件号
			// ----获取证件类型进行转换
			CardType cardType = CardType.getByKy(passenger.getString("cardType"));
			passengerJson.put("passporttypeseid", cardType.getHthy());// 乘客证件类型CODE
			passengerJson.put("piaotype", TicketType.getByKy(passenger.getString("ticketType")).getHthy());// 票类型(成人票、儿童票之类)
			passengerJson.put("old_ticket_no", passenger.getString("subSequence"));
			// ----获取坐席类型进行格式转换
			String newSeat = passenger.getString("newSeatType");
			String seat = passenger.getString("seatType");
			if (newSeat.equals("9")) {
				// 无座，判断 是 二等座 还是 硬座
				if (trainCode.startsWith("C") || trainCode.startsWith("D") || trainCode.startsWith("G")) {
					newSeatType = SeatType.GAOTIE_NO_SEAT.getHthy();
				} else {
					newSeatType = SeatType.COMMONLY_NO_SEAT.getHthy();
				}
			} else {
				newSeatType = SeatType.getByKy(newSeat).getHthy();
			}

			if (seat.equals("9")) {
				// 无座，判断 是 二等座 还是 硬座
				if (trainCode.startsWith("C") || trainCode.startsWith("D") || trainCode.startsWith("G")) {
					oldSeatType = SeatType.GAOTIE_NO_SEAT.getHthy();
				} else {
					oldSeatType = SeatType.COMMONLY_NO_SEAT.getHthy();
				}
			} else {
				oldSeatType = SeatType.getByKy(seat).getHthy();
			}
			passengerList.add(passengerJson);
		}

		requestJson.put("change_zwcode", newSeatType);// 坐席类型CODE
		requestJson.put("old_zwcode", oldSeatType);// 坐席类型CODE
		requestJson.put("ticketinfo", passengerList);// 车票信息

		String requestStr = "jsonStr=" + requestJson.toJSONString();
		logger.info("{}改签占位接口参数:{}", logid, requestStr);
		String httpResult = httpUtil.doHttpPost(hthyUrl, requestStr, 1000 * 30, false, "UTF-8");
		logger.info("{}改签占位接口响应结果:{}", logid, httpResult);
		return httpResult;
	}

	public String changeCancel(String orderId, String supplierId) {
		JSONObject requestJson = createParameter("train_cancel_change");
		requestJson.put("orderid", orderId); // 使用方订单号
		requestJson.put("transactionid", supplierId);// 交易单号
		requestJson.put("callbackurl", "");// 回调地址(可为空)
		requestJson.put("reqtoken", orderId);
		String requestStr = "jsonStr=" + requestJson.toJSONString();
		logger.info("{}改签取消接口参数:{}", logid, requestStr);
		String httpResult = httpUtil.doHttpPost(hthyUrl, requestStr, 1000 * 120, false, "UTF-8");
		logger.info("{}改签取消接口响应结果:{}", logid, httpResult);
		return httpResult;
	}

	public String changeConfirm(String orderId, String supplierId, Integer changeId) {
		JSONObject requestJson = createParameter("train_confirm_change");
		requestJson.put("transactionid", supplierId);
		requestJson.put("orderid", orderId);
		requestJson.put("callbackurl", changeConfirmCallback);
		requestJson.put("isasync", "Y");
		requestJson.put("reqtoken", changeId);
		String requestStr = "jsonStr=" + requestJson.toJSONString();
		logger.info("{}改签确认接口参数:{}", logid, requestStr);
		String httpResult = httpUtil.doHttpPost(hthyUrl, requestStr, 1000 * 30, false, "UTF-8");
		logger.info("{}改签确认接口响应结果:{}", logid, httpResult);
		return httpResult;
	}

	/**
	 * 构造基础参数:partnerid,method,reqtime,sign
	 * 
	 * @param method
	 * @return
	 */
	private JSONObject createParameter(String method) {
		JSONObject requestJson = new JSONObject(true);
		requestJson.put("partnerid", partnerId);
		requestJson.put("method", method);
		// 获取当前时间戳
		String reqTimes = DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
		requestJson.put("reqtime", reqTimes);
		// 签名加密
		String sign = MD5Util.md5(partnerId + method + reqTimes + MD5Util.md5(key).toLowerCase()).toLowerCase();
		requestJson.put("sign", sign);
		return requestJson;
	}

}
