package com.l9e.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.jiexun.iface.sign.RefundResultSign;
import com.l9e.common.LoginUserInfo;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.RobTicketService;
import com.l9e.transaction.vo.BookDetailInfo;
import com.l9e.transaction.vo.RobTicketFormVo;
import com.l9e.transaction.vo.RobTicketVo;
import com.l9e.transaction.vo.RobTicket_CP;
import com.l9e.transaction.vo.RobTicket_OI;
import com.l9e.transaction.vo.RobTicket_Refund;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

/**
 * 抢票 各种 工具方法
 * 
 * @author yangwei01
 * 
 */
@SuppressWarnings("all")
public class RobTicketUtils {
	// 前台状态 : 待支付 (00) 出票中(88) 出票成功(99) 出票失败(10) 退款结果()
	private static final Logger logger = Logger.getLogger(RobTicketUtils.class);
	/**
	 * 内部抢票推送地址
	 */
	private static final String url = "http://10.3.12.94:18018/jlOrder";
	private static List<HashMap> szm;
	private static LoginUserInfo loginUser;
	private static HashMap<String, String> ctripSeatType;
	private static HashMap<String, String> orderStatusMap;
	private static HashSet<String> jsonkeys;
	private static final String STU_PROVICE = "stu_provice";
	private static final String STU_SCHOOL = "stu_school"; 
	private static final String STU_CITY = "stu_city"; 
	/**
	 * 初始化 一大堆
	 */
	static {
		// 三字码
		InputStream stream = RobTicketUtils.class.getClassLoader().getResourceAsStream("szm.json");
		String inputStream2String = inputStream2String(stream);
		szm = JSON.parseArray(inputStream2String, HashMap.class);

		// 测试用户
		String testuser = RobTicketUtils
				.inputStream2String(RobTicketUtils.class.getClassLoader().getResourceAsStream("testUser.json"));
		loginUser = JSON.parseObject(testuser, LoginUserInfo.class);

		// 携程坐席类型
		ctripSeatType = new HashMap<String, String>();

		// 9、商务座 P、特等座 M、一等座 O、二等座 6、高级软卧 4、软卧 3、硬卧 2、 软座 1、硬座 0、无座 F:动卧 A:高级动卧
		// H:包厢硬卧
		ctripSeatType.put("商务座", "9");
		ctripSeatType.put("特等座", "P");
		ctripSeatType.put("一等座", "M");
		ctripSeatType.put("二等座", "O");
		ctripSeatType.put("高级软卧", "6");
		ctripSeatType.put("软卧", "4");
		ctripSeatType.put("硬卧", "3");
		ctripSeatType.put("软座", "2");
		ctripSeatType.put("硬座", "1");
		ctripSeatType.put("无座", "0");
		ctripSeatType.put("动卧", "F");
		ctripSeatType.put("高级动卧", "A");
		ctripSeatType.put("包厢硬卧", "H");

		// 订单状态
		orderStatusMap = new HashMap<String, String>();
		// 00、开始出票 01、重发出票 10、出票失败 11、正在预定 33、请求抢票成功 44、预定人工
		// 55、扣位成功（开始支付） 56、重新支付 61、人工支付 85、开始取消 83、正在取消
		// 84、取消重发 86、取消人工 87、取消失败 88、支付成功 99、出票成功 '
		orderStatusMap.put("00", "开始出票");
		orderStatusMap.put("01", "重发出票");
		orderStatusMap.put("10", "出票失败");
		orderStatusMap.put("11", "正在预定");
		orderStatusMap.put("33", "预订成功");
		orderStatusMap.put("44", "预定人工");
		orderStatusMap.put("55", "扣位成功");
		orderStatusMap.put("56", "重新支付");
		orderStatusMap.put("61", "人工支付");
		orderStatusMap.put("85", "开始取消");
		orderStatusMap.put("83", "正在取消");
		orderStatusMap.put("84", "取消重发");
		orderStatusMap.put("86", "取消人工");
		orderStatusMap.put("87", "取消失败");
		orderStatusMap.put("88", "支付成功");
		orderStatusMap.put("99", "出票成功");
		orderStatusMap.put("70", "退票中");
		orderStatusMap.put("71", "退票成功");
		orderStatusMap.put("72", "退票失败");
		orderStatusMap.put("80", "订单锁定");
		// JSON字段
		jsonkeys = new HashSet<String>();
		jsonkeys.add("orderId");
		jsonkeys.add("fromToZh");
		jsonkeys.add("payMoney");
		jsonkeys.add("buyMoney");
		jsonkeys.add("orderStatus");
		jsonkeys.add("createTime");
		jsonkeys.add("outTicketTime");
		jsonkeys.add("trainNo");
		jsonkeys.add("trainNoAccept");
		jsonkeys.add("fromCity");
		jsonkeys.add("toCity");
		jsonkeys.add("fromTime");
		jsonkeys.add("toTime");
		jsonkeys.add("travelTime");
		jsonkeys.add("seatType");
		jsonkeys.add("seatTypeAccept");
		jsonkeys.add("outTicketType");
		jsonkeys.add("channel");
		jsonkeys.add("level");
		jsonkeys.add("isPay");
		jsonkeys.add("fromCity3c");
		jsonkeys.add("toCity3c");
		jsonkeys.add("manualOrder");
		jsonkeys.add("payMoneyExt");
		jsonkeys.add("buyMoneyExt");
		jsonkeys.add("finalTrainNo");
		jsonkeys.add("finalSeatType");
		jsonkeys.add("paySerialNumber");
		jsonkeys.add("leakCutStr");
		jsonkeys.add("ctripOrderId");
		jsonkeys.add("orderCps");

	}

	private RobTicketUtils() {
	}

	/**
	 * 获得抢票 orderStatus 代号--中文
	 * 
	 * @return
	 */
	public static HashMap<String, String> getOrderStatusMap() {
		return orderStatusMap;
	}

	/**
	 * from --> db
	 * 
	 * @param formVo
	 * @param robvo
	 */
	public static void formVo2DBVO(RobTicketFormVo formVo, RobTicketVo dbvo) {
		RobTicket_OI orderInfo = dbvo.getOi();
		String orderId = orderInfo.getOrderId();
		orderInfo.setFromtoZh(formVo.getFrom_city() + "/" + formVo.getTo_city());
		/*
		 * 必须先给主订单表pay_money等金额赋值后再处理明细表(涉及匹配产品的问题)
		 */
		orderInfo.setFromCity(formVo.getFrom_city());
		orderInfo.setToCity(formVo.getTo_city());
		orderInfo.setFromTime(DateUtil.stringToDate(formVo.getFrom_time() + ":00", "yyyy-MM-dd HH:mm:ss"));
		orderInfo.setToTime(DateUtil.stringToDate(formVo.getTo_time() + ":00", "yyyy-MM-dd HH:mm:ss"));
		orderInfo.setTrainNo(formVo.getTrain_no());// 车次
		orderInfo.setTravelTime(formVo.getTravelTime());
		orderInfo.setBuyMoneyExtSum(new BigDecimal(formVo.getSvip_price()));// 我方收取客户服务费(SVIP服务费)
		orderInfo.setPayMoney(formVo.getTotalPay());// 用户支付总价格
		orderInfo.setBuyMoney(formVo.getTotal12306Price());// 12306最高票面价格 的 总和
		orderInfo.setOutTicketType(RobTicketVo.OI_OUT_TICKET_TYPE_ELEC);// 出票方式
																		// 电子
		orderInfo.setSeatType(RobTicketUtils.toCtripSeatType(formVo.getDefaultSeat().split("_")[2])); // 默认选择坐席(携程标准)
		orderInfo.setSeatTypeAccept(RobTicketUtils.toCtripBackSeats(formVo.getSelectSeats())); // 用户可接受的
																								// 备选坐席(携程标准)
		orderInfo.setCreateTime(new Date());// 入库时间
		orderInfo.setFrom3c(RobTicketUtils.getSZMbyStationName(formVo.getFrom_city()));// 出站三字码
		orderInfo.setTo3c(RobTicketUtils.getSZMbyStationName(formVo.getTo_city()));// 到站三字码
		orderInfo.setLeakCutOfftime(Integer.parseInt(formVo.getYun_qiangpiao()));// 抢票截止时间段
		orderInfo.setOrderStatus(RobTicketVo.OI_ORDER_STATUS_EXT_BEGIN);// 预下单
		orderInfo.setContactPerson(formVo.getLink_name());
		orderInfo.setContactPhone(formVo.getLink_phone());
		orderInfo.setLeakCutStr(formVo.leakCutStrFormat()); // leak_cut_str 格式化下

		for (BookDetailInfo info : formVo.getBookDetailInfoList()) {
			// 车票
			RobTicket_CP cp = dbvo.getOneFreshCP();
			String cp_id = CreateIDUtil.createID("HC_ROB_CP");// 车票id
			cp.setCpId(cp_id); // cp id
			cp.setOrderId(orderId); // 关联orderinfo ID
			cp.setUserName(info.getUser_name()); // 乘客名字
			cp.setTicketType(Integer.parseInt(info.getTicket_type()));// 车票类型
			cp.setCertType(info.getIds_type());// 证件类型
			cp.setCertNo(info.getUser_ids()); // 证件号码
			cp.setTelephone(orderInfo.getContactPhone());
			cp.setCreateTime(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3));
			cp.setBuyMoney(formVo.getMax12306Price().toString());
			cp.setBuyMoneyExt(formVo.getSingleSVIP_Price().toString());// 每一张票平摊的
																		// SVIP服务费
			cp.setPayMoney(formVo.getMax12306Price().add(formVo.getSingleSVIP_Price()).toString());
			// cp.setPayMoneyExt(payMoneyExt); 19e 支付给携程的服务费
			cp.setSeatType(orderInfo.getSeatType() + "|" + orderInfo.getSeatTypeAccept()); // 客户所选择的坐席(默认坐席,备选坐席
																							// |
																							// 分割)
			cp.setTrainNo(orderInfo.getTrainNo());
			cp.setCheckStatus(RobTicketVo.CP_CHECK_STATUS_PASS);
			dbvo.getCps().add(cp);
		}

	}

	/**
	 * 获取列车类型中文名称（eg 高铁 动车..）
	 * 
	 * @param train_no
	 * @return
	 */
	public static String getTrainTypeCn(String train_no) {
		/*
		 * G 高铁 D 动车 K 快车 T 特快 Z 直达 C 城际 数字 普快 L 临客 Y 旅游 剩下的就写其他
		 */
		String type = "";
		if (!StringUtils.isEmpty(train_no)) {
			String pre = train_no.substring(0, 1);
			if ("G".equalsIgnoreCase(pre)) {
				type = "高铁";
			} else if ("D".equalsIgnoreCase(pre)) {
				type = "动车";
			} else if ("K".equalsIgnoreCase(pre)) {
				type = "快车";
			} else if ("T".equalsIgnoreCase(pre)) {
				type = "特快";
			} else if ("Z".equalsIgnoreCase(pre)) {
				type = "直达";
			} else if ("C".equalsIgnoreCase(pre)) {
				type = "城际";
			} else if ("L".equalsIgnoreCase(pre)) {
				type = "临客";
			} else if ("Y".equalsIgnoreCase(pre)) {
				type = "旅游";
			} else if (pre.charAt(0) >= '0' && pre.charAt(0) <= '9') {
				type = "普快";
			} else {
				type = "其他";
			}
		}
		return type;
	}

	/**
	 * 解析座位信息
	 * 
	 * @param seatInfoList
	 * @param seatMsg
	 */
	public static void deSeatMsg(LinkedList<Map<String, String>> seatInfoList, String seatMsg,
			Map<String, String> seatPrizeMap, String defaultSelect, Map<String, String> seatMap,
			CommonService commonService) {
		Map<String, String> seatMoneyMap = commonService.querySeatMap(seatMap);// 根据车次cc、发站fz、到站dz查询各种坐席的票价
		String[] seats = seatMsg.split(",");
		for (String seat : seats) {
			String[] element = seat.split("_");
			String price = element[1].trim();
			String yp = element[2].trim();
			if (!StringUtils.isEmpty(price) && !"-".equals(price)) {// 有该类别坐席

				// 余票小于等于10张则过滤该坐席
				if (StringUtils.isEmpty(yp)) {
					continue;
				} else {
					if (yp.equals("_")) {
						yp = "0";
					}
				}
				Map<String, String> map = new HashMap<String, String>(3);

				String seatType = null;
				// 根据坐席名称取得坐席ID
				for (Map.Entry<String, String> entry : TrainConsts.getSeatType().entrySet()) {
					if (!StringUtils.isEmpty(element[0]) && entry.getValue().equals(element[0])) {
						seatType = entry.getKey();
					}
				}
				map.put("seatName", element[0]);
				map.put("seatType", seatType);
				if (seatType != null) {
					if (seatType.equals("10")) {
						logger.info("其他坐席，数据库无数据，使用页面传值：" + price);
					} else {
						String priceDb = seatMoneyMap.get("seat" + seatType);
						if (!price.equals(priceDb)) {
							if (Float.parseFloat(priceDb) >= Float.parseFloat(price)) {
								logger.info("【seatType" + seatType + "=" + priceDb + "】数据库票价>=页面票价，数据库票价priceDb="
										+ priceDb + ",页面传票价price=" + price);
								price = priceDb;
							} else {
								logger.info("【seatType" + seatType + "=" + price + "】数据库票价<页面票价，数据库票价priceDb=" + priceDb
										+ ",页面传票价price=" + price);
							}
						}
					}
				}
				map.put("price", price);
				map.put("yp", yp);
				if (defaultSelect.equals(element[0])) {
					map.put("seatSelect", "select");
					seatInfoList.addFirst(map);
				} else {
					map.put("seatSelect", "unSelect");
					seatInfoList.add(map);
				}
				seatPrizeMap.put("seatType" + seatType, price);// 坐席价格映射
			}
		}
		logger.info("解析座位信息:" + seatPrizeMap);
		/********* end ***********/
	}

	/**
	 * 中文站名--->三字码
	 * 
	 * @param fromORtoStationName
	 * @return
	 */
	public static String getSZMbyStationName(String fromORtoStationName) {
		for (HashMap map : szm) {
			String value = (String) map.get(fromORtoStationName);
			if (value != null) {
				return value;
			}
		}

		return "";
	}

	/**
	 * 输入流-->String (UTF-8)
	 * 
	 * @param in
	 * @return
	 */
	public static String inputStream2String(InputStream in) {
		StringBuffer buffer = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
			buffer = new StringBuffer();
			while (reader.ready()) {
				buffer.append(reader.readLine());
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	/**
	 * 本地测试 获得一个 有效登录用户
	 * 
	 * @param loginUserInfo
	 * @return
	 */
	public static LoginUserInfo getTestUser(LoginUserInfo loginUserInfo) {
		if (loginUserInfo != null) {
			return loginUserInfo;
		} else {
			return loginUser;
		}
	}

	/**
	 * 中文坐席 转 携程 坐席 代号
	 * 
	 * @param chineseSeatType
	 * @return
	 */
	public static String toCtripSeatType(String chineseSeatType) {
		return ctripSeatType.get(chineseSeatType);
	}

	/**
	 * 得到 备选坐席 : 分隔符 采用 "|" -- 例子: 0|M|O|9
	 * 
	 * @param chineseSeatType
	 * @return
	 */
	public static String toCtripBackSeats(String chineseBkSeats) {
		if (StringUtils.isEmpty(chineseBkSeats)) {
			return "";
		}
		String[] split = chineseBkSeats.split(",");
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < split.length; i++) {
			String one = split[i];
			String chineseName = one.split("_")[2];
			String ctripValue = ctripSeatType.get(chineseName);
			buffer.append(ctripValue);
			if (i >= 0 && i < split.length - 1) {
				buffer.append("|");
			}
		}
		return buffer.toString();
	}

	public static void main(String[] args) {
		String decimal = getDecimal("退票成功，退款金额:201.00元");
		System.out.println(decimal);
	}

	/**
	 * robVo -- 推送JSON
	 * 
	 * @param vo
	 * @return
	 */
	public static String robVo2JSON(RobTicketVo vo) {
		RobTicket_OI oi = vo.getOi();
		List<RobTicket_CP> cps = vo.getCps();
		oi.setOrderCps(cps);
		Map<String, Object> jsonMapFromOrderInfo = getJsonMapFromOrderInfo(oi);
		return JSON.toJSONString(jsonMapFromOrderInfo);
	}

	/**
	 * 
	 * @param robTicketService
	 * @param oi
	 * @return
	 * @throws Exception
	 */
	public static String pushRobTicket(RobTicketService robTicketService, RobTicket_OI oi) throws Exception {
		Map<String, Object> selectAndPushRob = robTicketService.selectAndPushRob(oi);
		RobTicket_OI order = (RobTicket_OI) selectAndPushRob.get("oi");
		List<RobTicket_CP> cps = (List<RobTicket_CP>) selectAndPushRob.get("cps");
		RobTicketVo vo = new RobTicketVo();
		vo.setOi(order);
		vo.setCps(cps);
		String robVo2JSON = robVo2JSON(vo);
		logger.info("支付成功后抢票订单推送开始--单号--" + oi.getOrderId() + " JSON--" + robVo2JSON);
		String sendAndRecive = "";
		sendAndRecive = HttpPostUtil.sendAndRecive(url, "type=push&json=" + robVo2JSON);
		return sendAndRecive;

	}

	/**
	 * request -- MAP
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> request2Map(HttpServletRequest request) {
		HashMap<String, String> map = new HashMap<String, String>();
		Map parameterMap = request.getParameterMap();
		Set<Entry<String, String>> entrySet = parameterMap.entrySet();
		for (Entry<String, String> entry : entrySet) {
			String key = entry.getKey();
			String value = getParam(request, key);
			if (!value.equals("")) {
				map.put(key, value);
			}
		}
		return map;
	}

	/**
	 * 解析 request 通过Key 得到 value
	 * 
	 * @param request
	 * @param param
	 * @return
	 */
	public static String getParam(HttpServletRequest request, String param) {
		return request.getParameter(param) == null ? "" : request.getParameter(param).toString().trim();
	}

	/**
	 * 获得携程接口的 坐席 MAP 中文-代号
	 * 
	 * @return
	 */
	public static Map<String, String> getCtripSeatMap() {
		return ctripSeatType;
	}

	/**
	 * 获得携程接口的 坐席 MAP 代号-中文
	 * 
	 * @return
	 */
	public static HashMap<String, String> getCtripSeatMapReverse() {
		Map<String, String> ctripSeatMap = RobTicketUtils.getCtripSeatMap();
		HashMap<String, String> seatmap = new HashMap<String, String>();
		Iterator<Entry<String, String>> iterator = ctripSeatMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
			seatmap.put(entry.getValue(), entry.getKey());
		}
		return seatmap;
	}

	/**
	 * 获取当前订单车次的 所有坐席信息(中文名,价格)
	 * 
	 * @param oi
	 * @param commonService
	 * @return
	 */
	public static Map<String, String> getChinese_Price_SeatMap(RobTicket_OI oi, CommonService commonService) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("cc", oi.getTrainNo());
		map.put("fz", oi.getFromCity());
		map.put("dz", oi.getToCity());
		Map<String, String> querySeatMap = commonService.querySeatMap(map);
		map.clear();
		Iterator<Entry<String, String>> iterator = querySeatMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<java.lang.String, java.lang.String> entry = (Map.Entry<java.lang.String, java.lang.String>) iterator
					.next();
			String key = entry.getKey();
			String value = entry.getValue();
			if (value.equals("0")) {
				continue;
			}
			key = key.replace("seat", "");
			String chinese = TrainConsts.getSeatType().get(key);
			map.put(chinese, value);
		}
		return map;
	}

	public static Set<String> getJsonKeys() {
		return jsonkeys;
	}

	/**
	 * 通过orderInfo 得到 抢单接口的 交互 JSON 如果需要扩展字段 ,需要修改 jsonKeys
	 * 
	 * @param oi
	 * @return
	 */
	public static Map<String, Object> getJsonMapFromOrderInfo(RobTicket_OI oi) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			String jsonString = JSON.toJSONString(oi, new ValueFilter() {

				@Override
				public Object process(Object arg0, String arg1, Object arg2) {
					if (arg2 == null) {
						return "";
					}
					return arg2;
				}
			});
			HashMap parseObject = JSON.parseObject(jsonString, HashMap.class);
			Iterator iterator = parseObject.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Object> entry = (Entry<String, Object>) iterator.next();
				String key = entry.getKey();
				Object value = entry.getValue();
				if (RobTicketUtils.getJsonKeys().contains(key)) {
					jsonMap.put(key, value);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		jsonMap.put("leakCutOffTime", oi.getLeakCutStr());
		return jsonMap;
	}

	public static RobTicketVo apiJSON2RobVO(String json) {

		return null;
	}

	public static String cancelRefund(String orderId, String type) {
		String sendAndRecive = "";
		try {
			sendAndRecive = HttpPostUtil.sendAndRecive(url, "type=" + type + "&orderId=" + orderId);
		} catch (Exception e) {
			logger.error("抢票取消失败,接口调用失败");
			e.printStackTrace();
		}
		return sendAndRecive;
	}

	/**
	 * 抢票订单接收 退款 回调 并处理
	 * 
	 * @param rs
	 * @param request
	 * @param response
	 * @param orderId
	 * @param robTicketService
	 */
	public static void refundNotifyBiz(RefundResultSign rs, HttpServletRequest request, HttpServletResponse response,
			String orderId, RobTicketService robTicketService) {
		Jedis jedis = null;
		try {
			String jsonString = JSON.toJSONString(rs);
			Map<String, String> map = new HashMap<String, String>();
			map.put(rs.getEop_refund_seq(), jsonString);
			// 缓存一下.
			jedis = JedisUtil.getJedis();
			jedis.hmset("ROB_REFUND_NOTIFY", map);
			RobTicket_Refund refund = new RobTicket_Refund();
			refund.setOpt_time(new Date());
			refund.setEop_refund_seq(rs.getEop_refund_seq());// 当前退款 唯一 流水号
			JSONObject json = new JSONObject();
			if (StringUtils.isEmpty(rs.getRefund_status())) {// 异常
				logger.info("【接收退款结果通知接口】通知异常退款结果为空，EOP订单号" + rs.getEop_order_id());
				json.put("result_code", "FAIL");
				json.put("result_desc", "退款结果为空");
				refund.setRefund_status(refund.FAIL);
				robTicketService.updateRefund(refund);
			} else if ("SUCCESS".equalsIgnoreCase(rs.getRefund_status())) {// 退款成功
				// 退款成功逻辑
				json.put("result_code", "SUCCESS");
				json.put("result_desc", "退款通知接收成功");
				logger.info("--eop退款结果成功--" + orderId);
				refund.setRefund_status(refund.SUCC);
				robTicketService.updateRefund(refund);
				write2Response(response, json.toString());
			} else {
				logger.info("【接收退款结果通知接口】EOP订单号" + rs.getEop_order_id() + "退款失败");
				json.put("result_code", "FAIL");
				json.put("result_desc", "退款结果错误");
				refund.setRefund_status(refund.FAIL);
				robTicketService.updateRefund(refund);
				write2Response(response, json.toString());
			}
		} catch (Exception e) {
			logger.error("EOP退款通知 抢票退款 处理 异常--当前通知的订单号--" + orderId + "--当前退款流水票号--" + rs.getEop_refund_seq());
			e.printStackTrace();
		} finally {
			jedis.close();
		}

	}

	public static void write2Response(HttpServletResponse response, String StatusStr) {
		try {
			response.getWriter().write(StatusStr);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static String getTicketTypeChinese(Integer ticketType) {
		String ticType = "";
		switch (ticketType) {
		case 0:
			ticType = "成人票";
			break;
		case 1:
			ticType = "儿童票";
			break;
		case 2:
			ticType = "学生票";
			break;
		default:
			break;
		}
		return ticType;
	}

	public static String getDecimal(String str) {
		String[] split = str.split(":");
		if (split.length > 2) {
		}
		System.out.println(split[1]);
		return split[1];
	}

	public static String getschoolCode(String school_name) {
		String code = "";
		Jedis jedis = null;
		try {
			jedis = JedisUtil.getJedis();
			school_name = StringUtils.trim(school_name);
			code = jedis.hget(STU_SCHOOL, school_name);
		} catch (Exception e) {
			logger.error(e.toString());
		} finally {
			jedis.close();
		}
		return code;
	}

	public static String getProvinceCode(String province_name) {
		String code = "";
		Jedis jedis = null;
		try {
			jedis = JedisUtil.getJedis();
			province_name = StringUtils.trim(province_name);
			code = jedis.hget(STU_PROVICE, province_name);
		} catch (Exception e) {
			logger.error(e.toString());
		} finally {
			jedis.close();
		}
		return code;
	}
	public static String getPreferenceCityCode(String city_name) {
		String code = "";
		Jedis jedis = null;
		try {
			jedis = JedisUtil.getJedis();
			city_name = StringUtils.trim(city_name);
			code = jedis.hget(STU_CITY, city_name);
		} catch (Exception e) {
			logger.error(e.toString());
		} finally {
			jedis.close();
		}
		return code;
	}

}
