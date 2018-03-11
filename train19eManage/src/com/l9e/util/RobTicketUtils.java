package com.l9e.util;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.l9e.transaction.service.RobTicketService;
import com.l9e.transaction.vo.RobTicketVo;

import redis.clients.jedis.Jedis;

/**
 * 抢票 各种 工具方法
 * 
 * @author yangwei01
 * 
 */
@SuppressWarnings("all")
public class RobTicketUtils {
	//前台状态 : 待支付 (00) 出票中(88) 出票成功(99) 出票失败(10) 退款结果()
	private static final Logger logger = Logger.getLogger(RobTicketUtils.class);
	/**
	 * 内部抢票推送地址
	 */
	private static final String url = "http://10.3.12.94:18018/jlOrder";
	private static final String REFUND_URL = "http://10.16.22.21:8380/ctrip_interface/ctripInterface/returnTicket";
	//private static final String REFUND_URL = "http://localhost:8080/ctrip_interface/ctripInterface/returnTicket";
	private static List<HashMap> szm;
	private static HashMap<String, String> ctripSeatType;
	private static HashMap<String, String> orderStatusMap;
	private static HashSet<String> jsonkeys;
	private static HashMap<String, String> channelMap  = new HashMap<String, String>();
	/**
	 * 初始化 一大堆
	 */
	static {
		ctripSeatType = new HashMap<String, String>();
		//9、商务座 P、特等座 M、一等座 O、二等座 6、高级软卧 4、软卧 3、硬卧 2、 软座 1、硬座 0、无座    F:动卧  A:高级动卧  H:包厢硬卧
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
		orderStatusMap = new HashMap<String, String>();
		//00、开始出票 01、重发出票 10、出票失败 11、正在预定  33、请求抢票成功 44、预定人工 
		//55、扣位成功（开始支付） 56、重新支付  61、人工支付  85、开始取消 83、正在取消 
		//84、取消重发 86、取消人工 87、取消失败  88、支付成功 99、出票成功 '
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
		// 渠道
		channelMap.put("HC","19e");
		channelMap.put("CS","测试");
		channelMap.put("HX","航信");
	}

	private RobTicketUtils() {
	}
	
	/**
	 * 获得抢票  orderStatus 代号--中文
	 * @return
	 */
	public static HashMap<String, String> getOrderStatusMap(){
		return orderStatusMap;
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
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		try {
			for (int n; (n = in.read(b)) != -1;) {
				out.append(new String(b, 0, n, "UTF-8"));
			}
		} catch (Exception e) {
			logger.error("三字码 JSON 解析异常");
			e.printStackTrace();
		}
		return out.toString();
	}

	

	
	/**
	 * 中文坐席 转 携程 坐席 代号
	 * @param chineseSeatType
	 * @return
	 */
	public static String toCtripSeatType(String chineseSeatType){
		return ctripSeatType.get(chineseSeatType);
	}
	
	
	/**
	 * 得到 备选坐席  : 分隔符 采用 "|" -- 例子: 0|M|O|9
	 * @param chineseSeatType
	 * @return
	 */
	public static String toCtripBackSeats(String chineseBkSeats){
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
			if(i>=0&&i<split.length-1){
				buffer.append("|");
			}
		}
		return buffer.toString();
	}
	
	

	
	/**
	 * request -- MAP
	 * @param request
	 * @return
	 */
	public static Map<String, String> request2Map(HttpServletRequest request){
		HashMap<String, String> map = new HashMap<String, String>();
		Map parameterMap = request.getParameterMap();
		Set<Entry<String, String>> entrySet = parameterMap.entrySet();
		for (Entry<String, String> entry : entrySet) {
			String key = entry.getKey();
			String value = getParam(request, key);
			if(!value.equals("")){
				map.put(key, value);
			}
		}
		return map;
	}
	
	/**
	 * 解析 request 通过Key 得到 value
	 * @param request
	 * @param param
	 * @return
	 */
	public static String getParam(HttpServletRequest request, String param){
		return request.getParameter(param) == null ? "" : request.getParameter(param).toString().trim();
	}
	/**
	 * 获得携程接口的 坐席 MAP 中文-代号
	 * @return
	 */
	public static Map<String, String> getCtripSeatMap(){
		return ctripSeatType;
	}

	/**
	 * 获得携程接口的 坐席 MAP 代号-中文
	 * @return
	 */
	public static HashMap<String, String> getCtripSeatMapReverse() {
		Map<String, String> ctripSeatMap = RobTicketUtils.getCtripSeatMap();
		HashMap<String, String> seatmap = new HashMap<String, String>();
		Iterator<Entry<String, String>> iterator = ctripSeatMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator
					.next();
			seatmap.put(entry.getValue(), entry.getKey());
		}
		return seatmap;
	}

	
	public static Set<String> getJsonKeys(){
		return jsonkeys;
	}

	public static RobTicketVo apiJSON2RobVO(String json) {
		
		return null;
	}

	public static String cancelRefund(String orderId, String type) {
		String sendAndRecive="";
		try {
			 sendAndRecive = HttpPostUtil.sendAndRecive(url, "type="+type+"&orderId="+orderId);
		} catch (Exception e) {
			logger.error("抢票取消失败,接口调用失败");
			e.printStackTrace();
		}
		return sendAndRecive;
	}

	public static String refundManul(RobTicketService robTicketService, String cp_id) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("cp_id", cp_id);
		List<Map<String,String>> queryCP = robTicketService.queryCP(map);
		Map<String, String> cp = queryCP.get(0);
		logger.info("后台人工退款获取到的车票信息("+cp_id+")-->"+cp);
		//cp.get(key);
		String contactName=cp.get("contact_person");//订单联系人
	 	String contactMobile=cp.get("contact_phone");//订单联系人电话
	 	String eOrderNumber=cp.get("out_ticket_billno");//12306电子订单号
	 	String eOrderType=cp.get("ticket_type");//乘客类型1成人2儿童
	 	String seatNumber = cp.get("seat_no");//车厢座位号
	 	String passportName=cp.get("user_name");//乘客姓名
	 	String passport=cp.get("cert_no");//乘客证件号
		String orderNumber = cp.get("ctrip_order_id");//合作方订单号
		StringBuilder sb = new StringBuilder();
		sb.append("contactName="+contactName+"&");
		sb.append("contactMobile="+contactMobile+"&");
		sb.append("eOrderNumber="+eOrderNumber+"&");
		sb.append("eOrderType="+eOrderType+"&");
		sb.append("seatNumber="+seatNumber+"&");
		sb.append("passportName="+passportName+"&");
		sb.append("passport="+passport+"&");
		sb.append("orderNumber="+orderNumber);
		String result = HttpPostUtil.sendAndRecive(REFUND_URL, sb.toString());
		logger.info("后台人工退款携程返回消息("+cp_id+")-->"+result);
		return result;
	}
	

	public static HashMap<String, String> getAllChannelMap() {
		return channelMap;
	}
	public static HashMap<String, String> getAllChannelMapReverse() {
		HashMap<String, String> channelMapR = new HashMap<String, String>();
		Iterator<Entry<String, String>> iterator = channelMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator
					.next();
			channelMapR.put(entry.getValue(), entry.getKey());
		}
		return channelMapR;
	}
	
	public static void parseRubSuccJson(Map<String, Object> request, String json){
		JSONObject obj = JSON.parseObject(json);
		JSONObject TicketInfoFinal = obj.getJSONObject("TrainOrderService").getJSONObject("OrderInfo")
				.getJSONObject("TicketInfoFinal");
		String eordernumber12306 = TicketInfoFinal.getString("ElectronicOrderNumber");
		String OrderTicketCheci = TicketInfoFinal.getString("OrderTicketCheci");
		Object tickets = TicketInfoFinal.getJSONObject("Tickets").get("Ticket");
		List<HashMap<String, String>> adultCPS = new ArrayList<HashMap<String, String>>();
		List<HashMap<String, String>> childCPS = new ArrayList<HashMap<String, String>>();
		Iterator<Object> iterator = null;
		if (tickets instanceof JSONArray) {
			JSONArray arr = (JSONArray) tickets;
			iterator = arr.iterator();
		}else{
			JSONObject tic = (JSONObject) tickets;
			String TicketType = tic.getString("TicketType");
			String OrderTicketPrice = tic.getString("OrderTicketPrice");
			String OrderTicketSeat = tic.getString("OrderTicketSeat");
			if (TicketType.equals("成人票")) {
				JSONObject DetailInfos = tic.getJSONObject("DetailInfos");// .getJSONArray("DetailInfo");
				Object jsonObject = DetailInfos.get("DetailInfo");
				JSONArray infos = null;
				if (jsonObject instanceof JSONArray) {
					infos = DetailInfos.getJSONArray("DetailInfo");
					Iterator<Object> infosIt = infos.iterator();
					while (infosIt.hasNext()) {
						Object object2 = (Object) infosIt.next();
						JSONObject info = (JSONObject) object2;
						String NumberID = info.getString("NumberID");
						String SeatNo = info.getString("SeatNo");
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("NumberID", NumberID);
						map.put("seat_no", SeatNo);
						map.put("OrderTicketPrice", OrderTicketPrice);
						map.put("OrderTicketSeat", OrderTicketSeat);
						map.put("train_no", OrderTicketCheci);
						adultCPS.add(map);
					}
				} else {
					JSONObject js = (JSONObject)jsonObject;
					String NumberID = js.getString("NumberID");
					String SeatNo = js.getString("SeatNo");
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("NumberID", NumberID);
					map.put("seat_no", SeatNo);
					map.put("OrderTicketPrice", OrderTicketPrice);
					map.put("OrderTicketSeat", OrderTicketSeat);
					map.put("train_no", OrderTicketCheci);
					adultCPS.add(map);

				}

			} else if (TicketType.equals("儿童票")) {
				JSONObject DetailInfos = tic.getJSONObject("DetailInfos");// .getJSONArray("DetailInfo");
				Object jsonObject = DetailInfos.get("DetailInfo");
				JSONArray infos = null;
				if (jsonObject instanceof JSONArray) {
					infos = DetailInfos.getJSONArray("DetailInfo");
					Iterator<Object> infosIt = infos.iterator();
					while (infosIt.hasNext()) {
						Object object2 = (Object) infosIt.next();
						JSONObject info = (JSONObject) object2;
						String NumberID = info.getString("NumberID");
						String SeatNo = info.getString("SeatNo");
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("seat_no", SeatNo);
						map.put("OrderTicketPrice", OrderTicketPrice);
						map.put("OrderTicketSeat", OrderTicketSeat);
						map.put("train_no", OrderTicketCheci);
						childCPS.add(map);
					}
				}else{
					JSONObject js = (JSONObject) jsonObject;
					String SeatNo = js.getString("SeatNo");
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("seat_no", SeatNo);
					map.put("OrderTicketPrice", OrderTicketPrice);
					map.put("OrderTicketSeat", OrderTicketSeat);
					map.put("train_no", OrderTicketCheci);
					childCPS.add(map);
					
				}
			}
			
			
		}
		while (iterator!=null && iterator.hasNext()) {
			Object object = (Object) iterator.next();
			JSONObject tic = (JSONObject) object;
			String TicketType = tic.getString("TicketType");
			String OrderTicketPrice = tic.getString("OrderTicketPrice");
			String OrderTicketSeat = tic.getString("OrderTicketSeat");
			if (TicketType.equals("成人票")) {
				JSONObject DetailInfos = tic.getJSONObject("DetailInfos");// .getJSONArray("DetailInfo");
				Object jsonObject = DetailInfos.get("DetailInfo");
				JSONArray infos = null;
				if (jsonObject instanceof JSONArray) {
					infos = DetailInfos.getJSONArray("DetailInfo");
					Iterator<Object> infosIt = infos.iterator();
					while (infosIt.hasNext()) {
						Object object2 = (Object) infosIt.next();
						JSONObject info = (JSONObject) object2;
						String NumberID = info.getString("NumberID");
						String SeatNo = info.getString("SeatNo");
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("NumberID", NumberID);
						map.put("seat_no", SeatNo);
						map.put("OrderTicketPrice", OrderTicketPrice);
						map.put("OrderTicketSeat", OrderTicketSeat);
						map.put("train_no", OrderTicketCheci);
						adultCPS.add(map);
					}
				} else {
					JSONObject js = (JSONObject)jsonObject;
					String NumberID = js.getString("NumberID");
					String SeatNo = js.getString("SeatNo");
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("NumberID", NumberID);
					map.put("seat_no", SeatNo);
					map.put("OrderTicketPrice", OrderTicketPrice);
					map.put("OrderTicketSeat", OrderTicketSeat);
					map.put("train_no", OrderTicketCheci);
					adultCPS.add(map);

				}

			} else if (TicketType.equals("儿童票")) {
				JSONObject DetailInfos = tic.getJSONObject("DetailInfos");// .getJSONArray("DetailInfo");
				Object jsonObject = DetailInfos.get("DetailInfo");
				JSONArray infos = null;
				if (jsonObject instanceof JSONArray) {
					infos = DetailInfos.getJSONArray("DetailInfo");
					Iterator<Object> infosIt = infos.iterator();
					while (infosIt.hasNext()) {
						Object object2 = (Object) infosIt.next();
						JSONObject info = (JSONObject) object2;
						String NumberID = info.getString("NumberID");
						String SeatNo = info.getString("SeatNo");
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("seat_no", SeatNo);
						map.put("OrderTicketPrice", OrderTicketPrice);
						map.put("OrderTicketSeat", OrderTicketSeat);
						map.put("train_no", OrderTicketCheci);
						childCPS.add(map);
					}
				}else{
					JSONObject js = (JSONObject) jsonObject;
					String SeatNo = js.getString("SeatNo");
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("seat_no", SeatNo);
					map.put("OrderTicketPrice", OrderTicketPrice);
					map.put("OrderTicketSeat", OrderTicketSeat);
					map.put("train_no", OrderTicketCheci);
					childCPS.add(map);
					
				}
			}
		}
		
		request.put("adult", adultCPS);
		request.put("children", childCPS);
	}
	
	
	public static String excel(HttpServletRequest request, HttpServletResponse response, String[] order_status,
			String[] channel, RobTicketService robTicketService) {
		String order_id = getParam(request, "order_id");
		String begin_info_time = getParam(request, "begin_info_time"); // 导出 时  按照 出票时间
		String end_info_time = getParam(request, "end_info_time"); // 导出 时  按照 出票时间
		String out_ticket_billno = getParam(request, "out_ticket_billno");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("order_id", order_id);
		map.put("begin_info_time", begin_info_time);
		map.put("end_info_time", end_info_time);
		map.put("out_ticket_billno", out_ticket_billno);
		map.put("orderStatusArr", order_status);
		map.put("channelArr", channel);
		List<Map<String, String>> queryRobList = robTicketService.queryRobListForExcel(map);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		HashMap<String, String> channelMap = RobTicketUtils.getAllChannelMap();
		for (Map<String, String> m : queryRobList) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("ctrip_order_id"));
			linkedList.add(m.get("out_ticket_billno"));
			String order_name=m.get("fromTo_zh");
			linkedList.add(order_name);
			linkedList.add(m.get("train_no"));
			Object ob = m.get("highest_ticket_price_all");// 12306票价
			Object ob2 = m.get("real_total_price_all");// 用户支付金额
			String refund_history = m.get("refund_history");
			linkedList.add(refund_history==null?"":refund_history.toString());
			Object refund_total = m.get("refund_total");
			Double ref_d = (Double) refund_total;
			linkedList.add(ref_d==null?"":ref_d.toString());
			Object ob3 = m.get("t_count");// 票数量
			Object service_price = m.get("service_price");// 服务费总计
			if(null!=ob){
			linkedList.add(ob.toString());
			}else{
				linkedList.add("");
			}
			if(null!=ob2){
				linkedList.add(ob2.toString());
				}else{
					linkedList.add("");
				}
			if(null!=ob3){
				linkedList.add(ob3.toString());
				}else{
					linkedList.add("");
				}
			if(null!=service_price){
				linkedList.add(service_price.toString());
			}else{
				linkedList.add("");
			}
			
			Object out_ticket_time = m.get("out_ticket_time");
			linkedList.add(out_ticket_time==null?"":out_ticket_time.toString());
			linkedList.add(channelMap.get(m.get("channel"))); 
			String order_st = m.get("order_status"); // 定单状态
			order_st = getOrderStatusMap().get(order_st);
			linkedList.add(order_st==null?"":order_st.toString());
			list.add(linkedList);
		}
		String title = "火车票抢票管理明细";
		String date = createDate(begin_info_time, end_info_time);
		String filename = "火车票抢票管理.xls";
		String[] secondTitles = { "序号", "订单号","携程单号","12306单号","出/到站","车次","退款历史","退款总计","预付总票价","出票总票价","出票数量","服务费总计","出票时间","渠道","订单状态"};
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);
		
		return null;
	}
	
	private static String createDate(String begin_info_time, String end_info_time) {
		String date = "日期：";
		SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
		if (begin_info_time.equals(end_info_time)
				|| begin_info_time == end_info_time) {
			if (begin_info_time == null || "".equals(begin_info_time)) {
				date += ss.format(new Date());
			} else {
				date += begin_info_time;
			}
		} else {
			if (begin_info_time == null || "".equals(begin_info_time)) {
				if (end_info_time == null || "".equals(end_info_time)) {
					date += ss.format(new Date()) + "之前";
				} else {
					date += end_info_time + "之前";
				}
			} else {
				if (end_info_time == null || "".equals(end_info_time)) {
					date += begin_info_time + "-------" + ss.format(new Date());
				} else {
					date += begin_info_time + "-------" + end_info_time;
				}
			}
		}
		return date;
	}
	
	public static void main(String[] args) {
		Jedis jedis = JedisUtil.getJedis();
		
	}

}
