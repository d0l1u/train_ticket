package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.common.Consts;
import com.l9e.common.ElongConsts;
import com.l9e.common.TongChengConsts;
import com.l9e.transaction.dao.CommonDao;
import com.l9e.transaction.dao.ElongOrderDao;
import com.l9e.transaction.dao.NoticeDao;
import com.l9e.transaction.dao.OrderDao;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.DBNoticeVo;
import com.l9e.transaction.vo.DBOrderInfo;
import com.l9e.transaction.vo.DBPassengerInfo;
import com.l9e.transaction.vo.DBStudentInfo;
import com.l9e.transaction.vo.ElongOrderInfoCp;
import com.l9e.transaction.vo.ElongOrderLogsVo;
import com.l9e.transaction.vo.Station;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpPostJsonUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.StrUtil;
import com.l9e.util.UrlFormatUtil;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

	private static final Logger logger = Logger.getLogger(OrderServiceImpl.class);
	@Resource
	private OrderDao orderDao;
	/*
	 * @Resource private ElongNoticeDao elongNoticeDao;
	 */
	@Resource
	private ElongOrderDao elongOrderDao;
	@Resource
	private NoticeDao noticeDao;
	@Resource
	private CommonDao commonDao;

	/** 订单插入 */
	@Override
	public void addOrder(DBOrderInfo orderInfo) {
		List<DBPassengerInfo> passengers = orderInfo.getPassengers();
		List<DBStudentInfo> students = orderInfo.getStudents();
		orderDao.addOrderInfo(orderInfo);
		for (DBPassengerInfo p : passengers) {
			orderDao.addPassengerInfo(p);
		}
		if(students!=null&&students.size()>0){
			for(DBStudentInfo s:students){
				orderDao.addStudentInfo(s);
			}
		}
		/** 加入通知表 */
		String channel = orderInfo.getChannel();
		if (Consts.CHANNEL_MEITUAN.equals(channel)) {
			String callBackUrl = orderInfo.getCallbackurl();
			/*
			 * if(callBackUrl!=null){ //同城异步(插入到通知表 启动通知出票系统项) DBNoticeVo
			 * notice=new DBNoticeVo();
			 * notice.setOrder_id(orderInfo.getOrder_id());
			 * notice.setCp_notify_status(Consts.NOTICE_START);
			 * notice.setChannel(orderInfo.getChannel());
			 * notice.setBook_notify_url(callBackUrl);//预订成功异步回调地址
			 * noticeDao.insertNotice(notice); }
			 */
			ElongOrderLogsVo log = new ElongOrderLogsVo();
			log.setOpt_person("meituan_app");
//			log.setContent("成功接受美团订单[" + orderInfo.getOrder_id() + "]"
//					+ (callBackUrl == null ? "同步方式" : "异步方式"));
//			log.setContent("成功接受美团订单[" + orderInfo.getOrder_id() + "]，锁票地址callBackUrl=" + callBackUrl);
			log.setContent("成功接受美团订单[" + orderInfo.getOrder_id() + "]");
			log.setOrder_id(orderInfo.getOrder_id());
			elongOrderDao.insertElongOrderLogs(log);
		} else {
			logger.info("order_id:" + orderInfo.getOrder_id()
					+ ",channel type is error:" + orderInfo.getChannel());
		}
	}

	/**
	 * 多渠道通知出票系统出票
	 * */
	@Override
	public void addNotifyCpSys(DBOrderInfo orderInfo, DBNoticeVo vo) {
		String channel = orderInfo.getChannel();
		String order_id = orderInfo.getOrder_id();
		Map<String, String> updateParam = new HashMap<String, String>();
		updateParam.put("order_id", order_id);
		updateParam.put("channel", orderInfo.getChannel());
		updateParam.put("num", vo.getCp_notify_num() + "");
		if (Consts.CHANNEL_MEITUAN.equals(channel)) {
			/** 先预订后支付 */
			String result = sendOutTicket(orderInfo, "book");
			if ("SUCCESS".equalsIgnoreCase(result)) {
				Map<String, String> map = new HashMap<String, String>();
				// 通知出票系统成功则订单状态修改为正在出票
				map.put("order_id", order_id);
				map.put("old_order_status1", Consts.ELONG_ORDER_DOWN);// 正在出票
				map.put("order_status", Consts.ELONG_ORDER_ING);// 正在出票
				orderDao.updateOrderStatus(map);
				updateParam.put("notice_status", "SUCCESS");
				asResultUpdateNotice(updateParam);
				logger.info("通知出票系统成功，channel:" + orderInfo.getChannel()
						+ ",order_id=" + order_id);
			} else if ("FAIL".equalsIgnoreCase(result)) {
				logger.info("通知出票系统超时，channel:" + orderInfo.getChannel()
						+ ",order_id=" + order_id);
				updateParam.put("notice_status", "FAIL");
				asResultUpdateNotice(updateParam);
			} else {
				logger.info("通知出票系统异常，channel:" + orderInfo.getChannel()
						+ ",order_id=" + order_id);
				updateParam.put("notice_status", "Exception");
				asResultUpdateNotice(updateParam);
			}
		}
	}

	private void asResultUpdateNotice(Map<String, String> param) {
		String status = param.get("notice_status");
		String order_id = param.get("order_id");
		int num = Integer.parseInt(param.get("num"));
		String channel = param.get("channel");
		/** 通知表更新参数 */

		DBNoticeVo noticeInfo = new DBNoticeVo();
		noticeInfo.setOrder_id(order_id);
		noticeInfo.setCp_notify_time("cp_notify_time");
		noticeInfo.setCp_notify_num(num + 1);
		noticeInfo.setChannel(channel);

		ElongOrderLogsVo log = new ElongOrderLogsVo();
		log.setOpt_person("meituan_app");
		log.setOrder_id(order_id);
		log.setContent("通知出票系统_"
				+ ("SUCCESS".equals(status) ? "成功"
						: "FAIL".equals(status) ? "失败,将启用重发" : "异常,将启用重发"));
		if ("SUCCESS".equals(status)) {
			noticeInfo.setCp_notify_status(Consts.NOTICE_OVER);
			noticeInfo.setCp_notify_finish_time("finished");
		} else {
			noticeInfo
					.setCp_notify_status((num == Consts.CP_NOTICE_NUM - 1) ? Consts.NOTICE_FAIL
							: Consts.NOTICE_ING);
		}
		noticeDao.updateNotice(noticeInfo);

		elongOrderDao.insertElongOrderLogs(log);
	}

	/**
	 * 多渠道出票结果异步请求
	 * */
	@Override
	public void addOrderResultNotice(DBNoticeVo vo) {
		String channel = vo.getChannel();
		if (Consts.CHANNEL_MEITUAN.equals(channel)) {
			// 美团渠道出票结果的通知
			sendMtOrderResult(vo);
		} else {
			logger.info("非法渠道的通知channel:" + channel + ",order_id:"
					+ vo.getOrder_id());
		}
	}

	/** 出票结果组装 */
	private String getTicketsJson(Map<String, Object> orderInfo,
			List<ElongOrderInfoCp> list) {
		JSONObject cpInfo = new JSONObject();
		// cpInfo.put("ticketNo", elongNoticeVo.getOut_ticket_billno());
		cpInfo.put("orderId", orderInfo.get("order_id"));
		cpInfo.put("ticketNo", orderInfo.get("out_ticket_billno"));
		String ext_field2 = orderInfo.get("ext_field2").toString();
		JSONArray arr = new JSONArray();
		for (ElongOrderInfoCp cp : list) {
			JSONObject json = new JSONObject();
			json.put("orderItemId", cp.getCp_id());
			json.put("seatType", "".equals(ext_field2) ? cp
					.getElong_seat_type() : (seatTypeCheck(cp.getSeat_type(),
					cp.getElong_seat_type(), ext_field2)));
			String seat_no = cp.getSeat_no();
			String train_box = cp.getTrain_box();
			json.put("seatNo", (StrUtil.isEmpty(train_box) ? ""
					: (train_box + "车厢 "))
					+ (StrUtil.isEmpty(seat_no) ? "无座" : seat_no));
			json.put("price", cp.getBuy_money());
			json.put("passengerName", cp.getUser_name());
			json.put("certNo", cp.getUser_ids());
			json.put("ticketType", cp.getElong_ticket_type());
			arr.add(json);
		}
		cpInfo.put("tickets", arr);
		return cpInfo.toString();
	}

	/**
	 * 多渠道出票结果异步请求-美团
	 * */
	private void sendMtOrderResult(DBNoticeVo vo) {
		String order_id = vo.getOrder_id();
		String tc_out_notify_url = vo.getOut_notify_url();
		DBOrderInfo orderInfo = orderDao.queryOrderInfo(order_id);
		List<DBPassengerInfo> cpInfoList = orderDao.queryOrderCpsInfo(order_id);
		boolean ordersuccess;
		if (Consts.ELONG_ORDER_SUCCESS.equals(orderInfo.getOrder_status())) {//出票成功
			ordersuccess = true;
		} else if (Consts.ELONG_ORDER_FAIL.equals(orderInfo.getOrder_status())) {//出票失败
			ordersuccess = false;
		} else if (Consts.ELONG_ORDER_CANCELED.equals(orderInfo.getOrder_status())) {//取消成功
			ordersuccess = false;
		} else if (Consts.ELONG_OUT_TIME.equals(orderInfo.getOrder_status())) {//超时订单(针对同城  同步接口 未返回结果订单变更为超时订单)
			ordersuccess = false;
		} else {
			//ordersuccess = false;
			logger.info("出票结果通知美团失败,订单状态不是33出票成功/44出票失败,order_id="+order_id);
			return;
		}
		try {
			String[] exts = orderInfo.getExt_field2().split("\\|");
			String from_station_code = exts[0];
			String to_station_code = exts[1];
			String reqtoken = "";
			if (exts.length == 3) {
				reqtoken = exts[2];
			}
			String error_info = orderInfo.getOut_fail_reason();
			JSONObject j = new JSONObject();

			/*
			 * success bool true:成功，false:失败 code int 4 状态编码 msg string 1~25 6
			 * 提示信息
			 */
			// TODO 查询线上数据库的错误原因进行匹配
			if (ordersuccess) {
				j.put("success", true);
				j.put("code", 100);
				j.put("msg", "处理或操作成功");
			} else {
				j.put("success", false);
				int code = 301;
				String msg = "无票";
				//19e失败原因：1所购买的车次坐席已无票 2身份证件已经实名制购票 3票价和12306不符 4乘车时间异常 
				//5证件错误 6用户要求取消订单 7未通过12306实名认证 8乘客身份信息待核验9、系统异常 10、超时未支付 12身份冒用
				//美团失败原因：301、无票 302、与已购车票行程冲突 303、下单时间距离发车时间太近 304、下单时车票价格与实际价格不一致 
				//305、下单时发车时间与实际时间不一致 306、身份信息校验未通过 307、身份信息被冒用 901、系统繁忙
				if ("2".equals(error_info)) {
					code = 302;
					msg = "与已购车票行程冲突";
				} else if("3".equals(error_info)){
					code = 304;
					msg = "下单时车票价格与实际价格不一致 ";
				} else if("4".equals(error_info)){
					code = 305;
					msg = "下单时发车时间与实际时间不一致";
				} else if ("5".equals(error_info) || "7".equals(error_info)
						|| "8".equals(error_info)) {
					code = 306;
					msg = "身份信息校验未通过 ";
					for (DBPassengerInfo p : cpInfoList) {
						msg += p.getUser_name() + p.getUser_ids();
						break;
					}
				} else if ("9".equals(error_info)) {
					code = 901;
					msg = "系统繁忙";
				} else if ("12".equals(error_info)) {
					code = 307;
					msg = "身份信息被冒用";
//				} else if ("10".equals(error_info)) {
//					code = 313;
//					msg = "订单内有乘客已被法院依法限制高消费，禁止乘坐列车当前坐席类型";
				}
				j.put("code", code);
				j.put("msg", msg);
			}

			j.put("orderid", orderInfo.getOrder_id());
			j.put("agent_orderid", orderInfo.getOrder_id());// 代理商交易订单号
			j.put("order_success", ordersuccess);
			j.put("order_amount", orderInfo.getBuy_money() == null ? ""
					: orderInfo.getBuy_money());
			j.put("train_code", orderInfo.getTrain_no());
			j.put("from_station_code", from_station_code);
			j.put("from_station_name", orderInfo.getFrom_city());
			j.put("to_station_code", to_station_code);
			j.put("to_station_name", orderInfo.getTo_city());
			j.put("req_token", reqtoken);
			j.put("order_12306_serial",
					orderInfo.getOut_ticket_billno() == null ? "" : orderInfo
							.getOut_ticket_billno());// 12306取票号

			/** 乘车日期 格式 */
			if (ordersuccess) {
				j.put("train_date", orderInfo.getTravel_date());
				String start_time = orderInfo.getFrom_time();
				String arrive_time = orderInfo.getTo_time();
				if (arrive_time == null) {
					// 针对没有to_time采用查询补全
					Map<String, String> paramMap = new HashMap<String, String>();
					String url = Consts.QUERY_TICKET;
					paramMap.put("channel", "tongcheng");
					paramMap.put("from_station", orderInfo.getFrom_city());
					paramMap.put("arrive_station", orderInfo.getTo_city());
					paramMap.put("travel_time", orderInfo.getTravel_date());
					paramMap.put("purpose_codes", "ADULT");
					paramMap.put("isNotZW", "yes");// 非中文查询
					String params;
					params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
					logger.info("美团发起余票查询" + paramMap.toString() + "url" + url);
					String result = HttpUtil.sendByPost(url, params, "UTF-8");

					String start_query_time = null;
					String arrive_query_time = null;
					String runtime = null;
					if (result == null
							|| result.equalsIgnoreCase("STATION_ERROR")
							|| result.equalsIgnoreCase("ERROR")) {
						logger.info("美团book通知,针对没有to_time采用查询补全_查询失败");
//						throw new RuntimeException("to_time采用查询补全_查询失败");
					} else {
						JSONArray arr = JSONArray.fromObject(result);
						int index = arr.size();
						for (int i = 0; i < index; i++) {
							if (arr.getJSONObject(i).get("train_code").equals(
									orderInfo.getTrain_no())) {
								runtime = arr.getJSONObject(i).getString(
										"run_time_minute");
								start_query_time = arr.getJSONObject(i)
										.getString("start_time");
								arrive_query_time = arr.getJSONObject(i)
										.getString("arrive_time");
								break;
							}

						}
						if (start_query_time == null
								|| arrive_query_time == null || runtime == null) {
							logger.info("美团book通知,针对没有to_time采用查询补全_查询失败,未匹配到结果");
//							throw new RuntimeException("to_time采用查询补全_查询失败");
						} else {
							logger.info("美团book通知,针对没有to_time采用查询补全_查询成功"
									+ start_query_time + "_"
									+ arrive_query_time + "_" + runtime);
							j.put("start_time", start_query_time);
							j.put("arrive_time", arrive_query_time);
							j.put("duration", runtime);// 运行时间
						}
					}
				} else if(start_time==null){
					j.put("start_time", start_time == null ? "" : start_time);
					j.put("arrive_time", arrive_time == null ? "" : arrive_time);
					j.put("duration", "");// 运行时间
				} else {
					String runtime = DateUtil.minuteDiff(DateUtil.stringToDate(
							arrive_time, "yyyy-MM-dd HH:mm:ss"), DateUtil
							.stringToDate(start_time, "yyyy-MM-dd HH:mm:ss"))
							+ "";
					start_time = start_time.substring(
							start_time.indexOf(" ") + 1, start_time.indexOf(" ") + 6);
					arrive_time = arrive_time.substring(arrive_time
							.indexOf(" ") + 1, arrive_time.indexOf(" ") + 6);
					j.put("start_time", start_time == null ? "" : start_time);
					j.put("arrive_time", arrive_time == null ? "" : arrive_time);
					j.put("duration", runtime);// 运行时间
				}
//				j.put("ordernumber", orderInfo.getOut_ticket_billno() == null ? ""
//								: orderInfo.getOut_ticket_billno());
				/** 时间 计算方式 */
			} else {
				j.put("train_date", orderInfo.getTravel_date());
				j.put("start_time", "");
				j.put("arrive_time", "");
				j.put("ordernumber", "");
				j.put("duration", "");// 运行时间
			}

			JSONArray parr = new JSONArray();
			for (DBPassengerInfo p : cpInfoList) {

				JSONObject p_json = new JSONObject();
				String seat_no = p.getSeat_no();
				String train_box = p.getTrain_box();
				String cp_id = p.getCp_id();
				// String passengerid=cp_id.substring(cp_id.lastIndexOf("_")+1);
				p_json.put("passengerid", p.getOut_passengerid());
				p_json.put("ticket_no", ordersuccess ? p.getCp_id() : "");
				p_json.put("passenger_name", p.getUser_name());
				p_json.put("certificate_no", p.getUser_ids());
				p_json.put("certificate_type", p.getElong_ids_type());
				p_json.put("certificate_name", TongChengConsts
						.getPassporttypeseidname(p.getElong_ids_type()));
				p_json.put("ticket_type", p.getElong_ticket_type());

				//19e坐席编码：座位类型：0、商务座 1、特等座 2、一等座 3、二等座 4、高级软卧（41、高级软卧上 42、高级软卧下）
				// 5、软卧 （51、软卧上 52、软卧下）6、硬卧 （61、硬卧上 62、硬卧中 63、硬卧下） 7、软座 8、硬座 9、无座 10、其他
				
				//美团坐席编码：1硬座 2硬卧上 3硬卧中 4硬卧下 5软座 6软卧上 7软卧中 8软卧下 9商务座 10观光座 11一等包座 12特等座 
				//13一等座 14二等座 15高级软卧上 16高级软卧下 17无座 18一人软包20动卧 21高级动卧 22包厢硬卧
				//出票结果回传坐席以实际出票坐席为准----美团坐席
				String mseatType =p.getSeat_type();//19e坐席
				String meituanseat = p.getElong_seat_type();
				if(StrUtil.isNotEmpty(seat_no)){//为空则为无座
					if("4".equals(mseatType) && seat_no.contains("上")){//19e坐席 4、高级软卧（41、高级软卧上 42、高级软卧下）
						mseatType = "15";//美团坐席15高级软卧上 16高级软卧下
						p_json.put("seat_type",mseatType);// 座位编码
					}else if("4".equals(mseatType) && seat_no.contains("下")){
						mseatType = "16";
						p_json.put("seat_type",mseatType);// 座位编码
					}else if("5".equals(mseatType) && seat_no.contains("上")){//5、软卧 （51、软卧上 52、软卧下）
						mseatType = "6";
						p_json.put("seat_type",mseatType);// 座位编码
					}else if("5".equals(mseatType) && seat_no.contains("中")){
						mseatType = "7";
						p_json.put("seat_type",mseatType);// 座位编码
					}else if("5".equals(mseatType) && seat_no.contains("下")){
						mseatType = "8";
						p_json.put("seat_type",mseatType);// 座位编码
					}else if("6".equals(mseatType) && seat_no.contains("上")){//5、6、硬卧 （61、硬卧上 62、硬卧中 63、硬卧下）
						mseatType = "2";//2硬卧上 3硬卧中 4硬卧下
						p_json.put("seat_type",mseatType);// 座位编码
					}else if("6".equals(mseatType) && seat_no.contains("中")){
						mseatType = "3";
						p_json.put("seat_type",mseatType);// 座位编码
					}else if("6".equals(mseatType) && seat_no.contains("下")){
						mseatType = "4";
						p_json.put("seat_type",mseatType);// 座位编码
					}else{
						p_json.put("seat_type",meituanseat);// 座位编码
					}
				}else{
					p_json.put("seat_type",meituanseat);// 座位编码
				}

				p_json.put("seat_detail_no",
						ordersuccess ? ((StrUtil.isEmpty(train_box) ? ""
								: (train_box + "车厢,")) + (StrUtil
								.isEmpty(seat_no) ? "无座" : seat_no)) : "");
				p_json.put("price", ordersuccess ? p.getBuy_money() : "");

				// 审核未通过
				if (ordersuccess) {
					// 身份核验状态 0：正常 1：待审核 2：未通过
					p_json.put("certificate_verify_status", "0");
				} else {
					if ("6".equals(orderInfo.getOut_fail_reason())) {
						p_json.put("certificate_verify_status", "1");
					} else {
						p_json.put("certificate_verify_status", "0");
					}
				}
				parr.add(p_json);
			}
			j.put("passengers", parr.toString());
			Map<String, String> params = new HashMap<String, String>();
			params.put("passengers", j.toString());
			logger.info("美团出票结果通知请求参数" + j.toString());
			long start = System.currentTimeMillis();
//			String sendParams = ElongUrlFormatUtil.createGetUrl("", params, "utf-8");
//			String result = HttpUtil.sendByPost(vo.getOut_notify_url(), sendParams, "utf-8");
			//post请求，传参用json格式
			String result = HttpPostJsonUtil.sendJsonPost(Consts.MT_URL+"/106/ticketNotify", j.toString(), "utf-8");
			
			logger.info("美团出票结果通知接口返回："+result);
			if (result != null && StringUtils.isNotEmpty(result)) {
				JSONObject json = JSONObject.fromObject(result);
				logger.info("美团出票结果通知返回结果,耗时"
						+ (System.currentTimeMillis() - start) + "ms,result:"
						+ result);
				// JSONObject json=JSONObject.fromObject(result);
				boolean success = json.getBoolean("success");
				if (success) {// TODO 可以出票
					logger.info("美团出票结果通知成功,耗时"
							+ (System.currentTimeMillis() - start)
							+ "ms,order_id:" + order_id + ",result:" + result);
					// bookResultSendUpdateNotice("SUCCESS",vo,result,ordersuccess,
					// orderInfo);
					resultSendUpdateNotice("SUCCESS", vo, result);
				} else {
					logger.info("美团出票结果通知失败,order_id:" + order_id + ",result:"
							+ result);
					resultSendUpdateNotice("FAIL", vo, result);
				}
			} else {
				logger.info("美团出票结果通知失败,order_id:" + order_id
								+ ",result返回null");
				resultSendUpdateNotice("FAIL", vo, result);
			}
		} catch (Exception e) {
			logger.info("美团出票结果通知异常,order_id:" + order_id + ",msg:" + e);
			resultSendUpdateNotice("EXCEPTION", vo, "");
			e.printStackTrace();
		}

		/*
		 * String partnerid=Consts.TC_PARTNERID; String key=Consts.TC_SIGNKEY;
		 * String orderStatus=orderDao.queryOrderStatusByOrderId(order_id);
		 * String tc_cancel_back_url=Consts.TC_CANCELBACKURL;
		 * 
		 * 
		 * try { String reqtime=DateUtil.dateToString(new
		 * Date(),"yyyyMMddHHmmssSSS"); Map<String,Object> params=new
		 * HashMap<String,Object>(); params.put("orderid",order_id);
		 * params.put("agent_orderid",order_id); //isSuccess
		 * 是否出票成功的标识（Y：出票成功,N：出票失败）
		 * params.put("ordersuccess",Consts.ELONG_ORDER_SUCCESS
		 * .equals(orderStatus)?true:false); params.put("orderamount",order_id);
		 * params.put("train_code",order_id);
		 * params.put("from_station_code",order_id);
		 * params.put("from_station_name",order_id);
		 * params.put("to_station_code",order_id);
		 * params.put("to_station_name",order_id);
		 * params.put("train_date",order_id); params.put("start_time",order_id);
		 * params.put("arrive_time",order_id); params.put("duration",order_id);
		 * params.put("passengers",order_id); params.put("req_token",order_id);
		 * params.put("order_12306_serial",order_id);
		 * 
		 * 
		 * String sendParams=ElongUrlFormatUtil.createGetUrl("", params,
		 * "utf-8"); String result=HttpUtil.sendByPost(tc_out_notify_url,
		 * sendParams, "utf-8"); if(result!=null&&"SUCCESS".equals(result)){
		 * logger
		 * .info("同程-出票"+(Consts.ELONG_ORDER_SUCCESS.equals(orderStatus)?"成功"
		 * :"失败")+"通知[成功],order_id:"+order_id+",result:"+result);
		 * resultSendUpdateNotice("SUCCESS",vo,result); }else{
		 * logger.info("同程-出票"
		 * +(Consts.ELONG_ORDER_SUCCESS.equals(orderStatus)?"成功"
		 * :"失败")+"通知[失败],order_id:"+order_id+",result:"+result);
		 * resultSendUpdateNotice("FAIL",vo,result); } } catch (Exception e) {
		 * logger
		 * .info("同程-出票"+(Consts.ELONG_ORDER_SUCCESS.equals(orderStatus)?"成功"
		 * :"失败")+"通知[异常],order_id:"+order_id+",msg:"+e);
		 * resultSendUpdateNotice("EXCEPTION",vo,""); e.printStackTrace(); }
		 */
	}

	/**
	 * 通知各渠道出票结果---更新通知表
	 * */
	private void resultSendUpdateNotice(String noticeStatus, DBNoticeVo vo,
			String result) {
		String order_id = vo.getOrder_id();
		String channel = vo.getChannel();
		int num = vo.getOut_notify_num();
		DBNoticeVo noticeInfo = new DBNoticeVo();
		noticeInfo.setOrder_id(order_id);
		noticeInfo.setChannel(channel);
		noticeInfo.setOut_notify_time("out_notify_time");
		noticeInfo.setOut_notify_num(num + 1);
		/** 更新订单 通知状态 */
		Map<String, String> orderNotice = new HashMap<String, String>();
		orderNotice.put("order_id", order_id);
		orderNotice.put("channel", channel);
		/** logsvo */
		ElongOrderLogsVo log = null;
		if ("SUCCESS".equals(noticeStatus)) {
			noticeInfo.setOut_notify_finish_time("finished");
			noticeInfo.setOut_notify_status(Consts.NOTICE_OVER);
			log = new ElongOrderLogsVo(order_id, "通知" + channel
					+ "出票结果/取消结果_成功", "meituan_app");
			orderNotice.put("notice_status", Consts.NOTICE_OVER);
		} else {
			String all_notify_status = num == (Consts.TCOUT_NOTICE_NUM - 1) ? Consts.NOTICE_FAIL
					: Consts.NOTICE_ING;
			noticeInfo.setOut_notify_status(all_notify_status);
			if (all_notify_status.equals(Consts.NOTICE_FAIL)) {
				orderNotice.put("notice_status", Consts.NOTICE_FAIL);
			} else {
				orderNotice.put("notice_status", Consts.NOTICE_ING);
			}
			// TODO 此接口要求一旦不成功就要求重试，要求一天中至少重试9次 （第一次重试 5秒后，第二次重试 30秒后，第三次重试
			// 1分钟后，第四次重试
			// 5分钟后 ，第五次重试 10分钟后，第六次重试 15分钟 第七次重试30分钟后，第八次重试 1小时后，第九次重试 2小时后。）
			if ("FAIL".equals(noticeStatus)) {
				log = new ElongOrderLogsVo(order_id, "第" + (num + 1) + "次通知"
						+ channel + "出票结果/取消结果_失败,返回信息[" + result + "]"
						+ ",等待重新通知", "meituan_app");
			} else {// 异常处理
				log = new ElongOrderLogsVo(order_id, "第" + (num + 1) + "次通知"
						+ channel + "出票结果/取消结果_异常,等待重新通知", "meituan_app");
			}
		}
		noticeDao.updateNotice(noticeInfo);
		elongOrderDao.insertElongOrderLogs(log);
		// 更新订单表的通知状态
		/** 设置更新状态的单向性 00、准备通知 --> 11、开始通知 --> 22、通知完成 --> 33、通知失败 */
		orderDao.updateOrderNoticeStatus(orderNotice);
	}

	@Override
	public String queryOrderStatusByOrderId(String orderId) {
		return orderDao.queryOrderStatusByOrderId(orderId);
	}

	@Override
	public DBOrderInfo queryOrderInfo(String orderId) {
		DBOrderInfo orderInfo = orderDao.queryOrderInfo(orderId);
		if (orderInfo != null) {
			orderInfo.setPassengers(orderDao.queryOrderCpsInfo(orderId));
		}
		return orderInfo;
	}

	@Override
	public int updateOrderStatus(Map<String, String> map) {
		return orderDao.updateOrderStatus(map);
	}

	@Override
	public String queryOrderStatus(String orderid) {
		// return orderDao.queryOrderStatus(orderid);
		return null;
	}

	@Override
	public String sendOutTicket(DBOrderInfo orderInfo, String type) {String order_id=orderInfo.getOrder_id();
		try {
			String notify_cp_back_url=Consts.NOTIFY_CP_ALLCHANNEL_BACK_URL;
			String notify_cp_interface_url=Consts.NOTIFY_CP_INTERFACE_URL;
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("orderid", order_id);
			paramMap.put("paymoney", orderInfo.getPay_money());//车票总价
			paramMap.put("trainno", orderInfo.getTrain_no());
			paramMap.put("fromcity", orderInfo.getFrom_city());
			paramMap.put("tocity", orderInfo.getTo_city());
			paramMap.put("ordername",orderInfo.getOrder_name() );
			paramMap.put("traveltime", orderInfo.getTravel_date());
			paramMap.put("seattype", orderInfo.getSeat_type());
			paramMap.put("outtickettype", "11");//
			paramMap.put("channel", "meituan");//渠道编号
			//备选坐席问题
			//paramMap.put("ext", "level|1");
			if("book".equals(type)){
				//先预订后支付
				paramMap.put("ispay","11");
			}else if("outTicket".equals(type)){
				//已经支付
				paramMap.put("ispay","00");
			}
			if("11".equals(orderInfo.getWait_for_order())){
				paramMap.put("waitfororder","11");//支持继续出票
			}else{
				paramMap.put("waitfororder","00");//不支持继续出票
			}
			paramMap.put("extseattype", orderInfo.getExt_field1());
			StringBuffer sb = new StringBuffer();
			List<DBPassengerInfo> cpInfoList=orderInfo.getPassengers();
			for (DBPassengerInfo cpInfo : cpInfoList) {
				if(sb.length()>0) {
					sb.append("#");
				}
				sb.append(cpInfo.getCp_id()).append("|").append(cpInfo.getUser_name()).append("|")
				  .append(cpInfo.getTicket_type()).append("|").append(cpInfo.getIds_type()).append("|")
				  .append(cpInfo.getUser_ids()).append("|").append(cpInfo.getSeat_type()).append("|")
				  .append(cpInfo.getPay_money());
			}
			paramMap.put("seattrains", sb.toString());
			paramMap.put("backurl", notify_cp_back_url);
			
			//站名的三字码
			String[] exts = orderInfo.getExt_field2().split("\\|");
			String from_station_code = exts[0];
			String to_station_code = exts[1];
			paramMap.put("fromCity3c", from_station_code);//订单中出发城市三字码
			paramMap.put("toCity3c", to_station_code);//订单中到达城市三字码
			
			String params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
			logger.info("通知出票系统：" + order_id+paramMap.toString());
			String result = HttpUtil.sendByPost(notify_cp_interface_url, params, "UTF-8");
			logger.info(order_id+"请求通知出票接口返回：" + result);
			
			if(!StringUtils.isEmpty(result)){
				String[] results = result.trim().split("\\|");
				if("success".equalsIgnoreCase(results[0]) && results.length == 2
						&& orderInfo.getOrder_id().equals(results[1])){//通知成功
					Map<String,String> map = new HashMap<String, String>();
			        //通知出票系统成功则订单状态修改为正在出票  
			        map.put("order_id", order_id);
			        map.put("old_order_status1", Consts.ELONG_ORDER_DOWN);//正在出票
			        map.put("order_status", Consts.ELONG_ORDER_ING);//正在出票
			        orderDao.updateOrderStatus(map);	
				    logger.info("通知出票系统成功，channel:"+orderInfo.getChannel()+",order_id=" + order_id);
				    return "SUCCESS";
				}else{//超时重发
					 logger.info("通知出票系统失败，channel:"+orderInfo.getChannel()+",order_id=" + order_id+"results:"+results);
					return "FAIL";
				}
			}else{
				logger.info("通知出票系统超时，channel:"+orderInfo.getChannel()+",order_id=" + order_id);
				return "FAIL";
			}
		} catch (Exception e) {
			logger.info("通知出票系统异常"+e);
			e.printStackTrace();
			return "EXCEPTION";
		}
	}

	@Override
	public String queryOutTicketOrderStatusByOrderId(String orderId) {
		return orderDao.queryOutTicketOrderStatusByOrderId(orderId);
	}

	@Override
	public Map<String, Object> queryOutTicketOrderInfo(String orderId) {
		return orderDao.queryOutTicketOrderInfo(orderId);
	}

	@Override
	public Map<String, Object> queryOutTicketCpInfo(String cpId) {
		return orderDao.queryOutTicketCpInfo(cpId);
	}

	/**
	 * 多渠道 预订结果通知
	 * */
	@Override
	public void addBookResultNotice(DBNoticeVo vo) {
		String channel = vo.getChannel();
		if (Consts.CHANNEL_MEITUAN.equals(channel)) {
			// 美团的通知----火车票出票前代理商回调美团校验
			sendMtBookResult(vo);
		} else {
			logger.info("非法渠道的通知channel:" + channel + ",order_id:"
					+ vo.getOrder_id());
		}
	}

	// 组装艺龙车票信息方法 预订回调
	private String getBookTicketsJson(Map<String, Object> orderInfo,
			List<ElongOrderInfoCp> list) {
		JSONObject cpInfo = new JSONObject();
		// cpInfo.put("ticketNo", elongNoticeVo.getOut_ticket_billno());
		cpInfo.put("orderId", orderInfo.get("order_id"));
		cpInfo.put("ticketNo", orderInfo.get("out_ticket_billno"));

		cpInfo.put("holdingSeatSuccessTime", orderInfo.get("out_ticket_time"));
		cpInfo.put("payTimeDeadLine", orderInfo.get("pay_limit_time"));
		String ext_field2 = orderInfo.get("ext_field2").toString();
		JSONArray arr = new JSONArray();
		for (ElongOrderInfoCp cp : list) {
			JSONObject json = new JSONObject();
			json.put("orderItemId", cp.getCp_id());
			json.put("seatType", "".equals(ext_field2) ? cp
					.getElong_seat_type() : (seatTypeCheck(cp.getSeat_type(),
					cp.getElong_seat_type(), ext_field2)));
			String seat_no = cp.getSeat_no();
			String train_box = cp.getTrain_box();
			json.put("seatNo", (StrUtil.isEmpty(train_box) ? ""
					: (train_box + "车厢 "))
					+ (StrUtil.isEmpty(seat_no) ? "无座" : seat_no));
			json.put("price", cp.getBuy_money());
			json.put("passengerName", cp.getUser_name());
			json.put("certNo", cp.getUser_ids());
			json.put("ticketType", cp.getElong_ticket_type());
			arr.add(json);
		}
		cpInfo.put("tickets", arr);
		return cpInfo.toString();
	}

	private String seatTypeCheck(String seatType, String elong_seat_type,
			String ext_field1) {
		if (ElongConsts.SEAT_TYPE_2.equals(seatType)) {
			/** 一等座|一等软座 区分 */
			if (ElongConsts.ELONG_SEAT_TYPE_6.equals(elong_seat_type)
					|| ext_field1.indexOf(ElongConsts.ELONG_SEAT_TYPE_6 + ",") > 0) {
				// 一等软座
				return ElongConsts.ELONG_SEAT_TYPE_6;
			} else {
				return ElongConsts.ELONG_SEAT_TYPE_9;
			}
		} else if (ElongConsts.SEAT_TYPE_3.equals(seatType)) {
			/** 二等座|二等软座 区分 */
			if (ElongConsts.ELONG_SEAT_TYPE_7.equals(elong_seat_type)
					|| ext_field1.indexOf(ElongConsts.ELONG_SEAT_TYPE_7 + ",") > 0) {
				// 一等软座
				return ElongConsts.ELONG_SEAT_TYPE_7;
			} else {
				return ElongConsts.ELONG_SEAT_TYPE_10;
			}

		} else if (ElongConsts.SEAT_TYPE_1.equals(seatType)) {
			/** 特等座|特等软座 区分 */
			if (ElongConsts.ELONG_SEAT_TYPE_13.equals(elong_seat_type)
					|| ext_field1.indexOf(ElongConsts.ELONG_SEAT_TYPE_13 + ",") > 0) {
				// 一等软座
				return ElongConsts.ELONG_SEAT_TYPE_13;
			} else {
				return ElongConsts.ELONG_SEAT_TYPE_11;
			}
		} else if (ElongConsts.SEAT_TYPE_9.equals(seatType)) {
			return elong_seat_type;
		} else if (ElongConsts.SEAT_TYPE_5.equals(seatType)) {
			/** 软卧|动卧 区分 */
			if (ElongConsts.ELONG_SEAT_TYPE_15.equals(elong_seat_type)
					|| ext_field1.indexOf(ElongConsts.ELONG_SEAT_TYPE_15 + ",") > 0) {
				// 一等软座
				return ElongConsts.ELONG_SEAT_TYPE_15;
			} else {
				return ElongConsts.ELONG_SEAT_TYPE_4;
			}
		} else {
			return ElongConsts.getElongSeatType(seatType);
		}
	}

	/** 针对美团的 预订结果的异步回调 具体参数协议---火车票出票前代理商回调美团校验 */
	private void sendMtBookResult(DBNoticeVo vo) {
		String order_id = vo.getOrder_id();
		String book_notify_url = vo.getBook_notify_url();
		DBOrderInfo orderInfo = orderDao.queryOrderInfo(order_id);
		List<DBPassengerInfo> cpInfoList = orderDao.queryOrderCpsInfo(order_id);
		boolean ordersuccess;
		if (Consts.ELONG_ORDER_MAKED.equals(orderInfo.getOrder_status())) {// 预订成功
			ordersuccess = true;
		} else if (Consts.ELONG_ORDER_FAIL.equals(orderInfo.getOrder_status())) {// 预定失败
			ordersuccess = false;
		} else {
			ordersuccess = false;
		}
		try {
			String[] exts = orderInfo.getExt_field2().split("\\|");
			String from_station_code = exts[0];
			String to_station_code = exts[1];
			String reqtoken = "";
			if (exts.length == 3) {
				reqtoken = exts[2];
			}
			String error_info = orderInfo.getOut_fail_reason();
			JSONObject j = new JSONObject();

			/*
			 * success bool true:成功，false:失败 code int 4 状态编码 msg string 1~25 6
			 * 提示信息
			 */
			/*
			 * if(ordersuccess){ j.put("ordersuccess", "true");
			 * j.put("code",100); j.put("msg", "处理或操作成功"); }else{
			 * j.put("ordersuccess", "false"); int code=309; String msg="没有余票";
			 * if("2".equals(error_info)){ code=305; msg="乘客已经预订过该车次"; }else
			 * if("5"
			 * .equals(error_info)||"7".equals(error_info)||"8".equals(error_info
			 * )){ code=308; for(DBPassengerInfo p:cpInfoList){
			 * msg=p.getUser_name()+p.getUser_ids(); break; } }else
			 * if("9".equals(error_info)){ code=506; msg="出票系统故障"; }else
			 * if("10".equals(error_info)){ code=313;
			 * msg="订单内有乘客已被法院依法限制高消费，禁止乘坐列车当前坐席类型";
			 * 
			 * } j.put("code",code); j.put("msg", msg); }
			 */
			if("11".equals(orderInfo.getOrder_type())){
				//先占座订单
				j.put("orderId",  Long.parseLong(orderInfo.getOrder_id()));
				j.put("bookingSuccess", ordersuccess);
				j.put("orderAmount", orderInfo.getBuy_money() == null ? "0": orderInfo.getBuy_money());
				j.put("trainCode", orderInfo.getTrain_no());
				j.put("fromStationCode", from_station_code);
				j.put("toStationCode", to_station_code);
				j.put("reqToken", reqtoken);
				j.put("order12306Serial",orderInfo.getOut_ticket_billno() == null ? "" : orderInfo
								.getOut_ticket_billno());// 12306取票号

				/** 乘车日期 格式 */
				if (ordersuccess) {
					String start_time = orderInfo.getFrom_time();
					String arrive_time = orderInfo.getTo_time();
					j.put("startTime", start_time == null ? "" : DateUtil.dateToString(DateUtil.stringToDate(start_time,DateUtil.DATE_FMT3), DateUtil.DATE_FMT3)  );
					j.put("arriveTime", arrive_time == null ? "" : DateUtil.dateToString(DateUtil.stringToDate(arrive_time,DateUtil.DATE_FMT3), DateUtil.DATE_FMT3) );
					j.put("payEndTime", orderInfo.getPay_limit_time()==null ?"":DateUtil.dateToString(DateUtil.stringToDate(orderInfo.getPay_limit_time(),DateUtil.DATE_FMT3), DateUtil.DATE_FMT3));
					j.put("seatHoldFailReason","");
				} else {
					j.put("startTime", DateUtil.dateToString(DateUtil.stringToDate(orderInfo.getFrom_time(),DateUtil.DATE_FMT3), DateUtil.DATE_FMT3));
					j.put("arriveTime", "");
					j.put("payEndTime", "");
					String out_fail_reason = orderInfo.getOut_fail_reason();
					String fail_reason_code=getHoldErrorCode(out_fail_reason);
					j.put("seatHoldFailReason",fail_reason_code);
				}
				JSONArray parr = new JSONArray();
				for (DBPassengerInfo p : cpInfoList) {                            
					JSONObject p_json = new JSONObject();
					String seat_no = p.getSeat_no();
					String train_box = p.getTrain_box();
					p_json.put("ticket_id",p.getOut_passengerid());
					p_json.put("passenger_name", p.getUser_name());
					p_json.put("certificate_no", p.getUser_ids());
					p_json.put("certificate_type", p.getElong_ids_type());
					p_json.put("ticket_type", p.getElong_ticket_type());
					// p_json.put("piaotypename",
					// TongChengConsts.getPiaotypename(p.getElong_ticket_type()));

					// 系统扩展坐席类型：21动卧 22高级动卧 23一等软座 24二等软座
					// 订单请求坐席类型：M一等座 O二等座 4软卧 F动卧 A高级软座 ,7一等软座 8二等软座

					// 9:商务座，P:特等座，M:一等座，O:二等座，6:高级软卧，
					// 4:软卧，3:硬卧，2:软座，1:硬座 7:一等软座 8:二等软座 A:高级动卧 F:动卧

					// 美团9:商务座，P:特等座，M:一等座，O:二等座，6:高级软卧，4:软卧，3:硬卧，2:软座，1:硬座
					// meituan座位类型 :1硬座 2硬卧上 3硬卧中 4硬卧下 5软座 6软卧上 7软卧中 8软卧下 9商务座 10观光座
					// 11一等包座 12特等座
					// 13一等座 14二等座 15高级软卧上 16高级软卧下 17无座 18一人软包 10动卧 21高级动卧 22包厢硬卧

					//出票结果回传坐席以实际出票坐席为准----美团坐席
					String mseatType =p.getSeat_type();//19e坐席
					String meituanseat = p.getElong_seat_type();
					if(StrUtil.isNotEmpty(seat_no)){//为空则为无座
						if("4".equals(mseatType) && seat_no.contains("上")){//19e坐席 4、高级软卧（41、高级软卧上 42、高级软卧下）
							mseatType = "15";//美团坐席15高级软卧上 16高级软卧下
							p_json.put("seat_type",mseatType);// 座位编码
						}else if("4".equals(mseatType) && seat_no.contains("下")){
							mseatType = "16";
							p_json.put("seat_type",mseatType);// 座位编码
						}else if("5".equals(mseatType) && seat_no.contains("上")){//5、软卧 （51、软卧上 52、软卧下）
							mseatType = "6";
							p_json.put("seat_type",mseatType);// 座位编码
						}else if("5".equals(mseatType) && seat_no.contains("中")){
							mseatType = "7";
							p_json.put("seat_type",mseatType);// 座位编码
						}else if("5".equals(mseatType) && seat_no.contains("下")){
							mseatType = "8";
							p_json.put("seat_type",mseatType);// 座位编码
						}else if("6".equals(mseatType) && seat_no.contains("上")){//5、6、硬卧 （61、硬卧上 62、硬卧中 63、硬卧下）
							mseatType = "2";//2硬卧上 3硬卧中 4硬卧下
							p_json.put("seat_type",mseatType);// 座位编码
						}else if("6".equals(mseatType) && seat_no.contains("中")){
							mseatType = "3";
							p_json.put("seat_type",mseatType);// 座位编码
						}else if("6".equals(mseatType) && seat_no.contains("下")){
							mseatType = "4";
							p_json.put("seat_type",mseatType);// 座位编码
						}else{
							p_json.put("seat_type",meituanseat);// 座位编码
						}
					}else{
						p_json.put("seat_type",meituanseat);// 座位编码
					}
					p_json.put("coach_no", ordersuccess ? (StrUtil.isEmpty(train_box) ? "": train_box + "车厢"):"");
					p_json.put("seat_no", ordersuccess?(StrUtil.isEmpty(seat_no) ? "无座" : seat_no):"");
					p_json.put("ticket_no", "");
					p_json.put("price", ordersuccess ? p.getBuy_money() : "0");
					parr.add(p_json);
				}
				j.put("tickets", parr.toString());
				logger.info("美团占座回调请求参数" + j.toString());
				long start = System.currentTimeMillis();
				String result = HttpPostJsonUtil.sendJsonPost(vo.getBook_notify_url(), j.toString(), "utf-8");
				logger.info(order_id+"【美团占座回调返回result】"+result);
				if (result != null && StringUtils.isNotEmpty(result)) {
					JSONObject json = JSONObject.fromObject(result);
					logger.info(order_id+"美团占座回调返回结果,耗时"+ (System.currentTimeMillis() - start) + "ms,result:"+ result);
					if (("100".equals(json.get("code")) || json.getInt("code")==100 || (json.getBoolean("success"))) ) {
						logger.info("美团占座回调通知成功,耗时"
								+ (System.currentTimeMillis() - start)
								+ "ms,order_id:" + order_id + ",result:" + result);
						bookResultSendUpdateNotice("SUCCESS", vo, result,ordersuccess, orderInfo);
					} 
					else {
						logger.info("美团占座回调通知失败,order_id:" + order_id+ ",result返回:"+result);
						bookResultSendUpdateNotice("FAIL", vo, result, ordersuccess,
								orderInfo);
					}
				} else {
					logger.info("美团占座回调通知失败,order_id:" + order_id
							+ ",result返回:"+result);
					bookResultSendUpdateNotice("FAIL", vo, result, ordersuccess,
							orderInfo);
				}
			}else{
				j.put("orderid", orderInfo.getOrder_id());
				j.put("agent_orderid", orderInfo.getOrder_id());// 代理商交易订单号
				j.put("order_success", ordersuccess);
				j.put("order_amount", orderInfo.getBuy_money() == null ? ""
						: orderInfo.getBuy_money());
				j.put("train_code", orderInfo.getTrain_no());
				j.put("from_station_code", from_station_code);
				j.put("from_station_name", orderInfo.getFrom_city());
				j.put("to_station_code", to_station_code);
				j.put("to_station_name", orderInfo.getTo_city());
				j.put("req_token", reqtoken);
				j.put("order_12306_serial",
						orderInfo.getOut_ticket_billno() == null ? "" : orderInfo
								.getOut_ticket_billno());// 12306取票号

				/** 乘车日期 格式 */
				if (ordersuccess) {
					j.put("train_date", orderInfo.getTravel_date());
					String start_time = orderInfo.getFrom_time();
					String arrive_time = orderInfo.getTo_time();
					if (arrive_time == null || start_time == null) {
						//查询数据库
						Map<String, Object> queryParam = new HashMap<String, Object>();
						queryParam.put("checi", orderInfo.getTrain_no());
						queryParam.put("name", orderInfo.getFrom_city());
						Station fromStation = commonDao.selectOneStation(queryParam);
						queryParam.put("name", orderInfo.getTo_city());
						Station toStation = commonDao.selectOneStation(queryParam);
						String start_query_time = null;
						String arrive_query_time = null;
						if(fromStation == null || toStation == null) {
							logger.info(orderInfo.getOrder_id()  + " 美团-数据库时间补全失败!");

							// 针对没有to_time采用查询补全
							Map<String, String> paramMap = new HashMap<String, String>();
							String url = Consts.QUERY_TICKET;
							paramMap.put("channel", "tongcheng");//利用同程渠道的查询方法
							paramMap.put("from_station", orderInfo.getFrom_city());
							paramMap.put("arrive_station", orderInfo.getTo_city());
							paramMap.put("travel_time", orderInfo.getTravel_date());
							paramMap.put("purpose_codes", "ADULT");
							paramMap.put("isNotZW", "yes");// 非中文查询
							String params;
							params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
							logger.info(orderInfo.getOrder_id()+" 美团发起余票查询" + paramMap.toString() + "url" + url);
							String result = HttpUtil.sendByPost(url, params, "UTF-8");
							logger.info(orderInfo.getOrder_id()+ " 美团发起余票查询，查询结果"+result);

							String runtime = null;
							if(result == null || result == ""
								|| result.equalsIgnoreCase("STATION_ERROR")
								|| result.equalsIgnoreCase("ERROR")){
								result = HttpUtil.sendByPost(url, params, "UTF-8");
								if(result == null || result == ""
									|| result.equalsIgnoreCase("STATION_ERROR")
									|| result.equalsIgnoreCase("ERROR")){
									result = HttpUtil.sendByPost(url, params, "UTF-8");
								}
							}
							if (result == null || result == ""
									|| result.equalsIgnoreCase("STATION_ERROR")
									|| result.equalsIgnoreCase("ERROR")) {
								logger.info("美团book通知,针对没有to_time采用查询补全_查询失败");
							} else {
								try{
									JSONArray arr = JSONArray.fromObject(result);
									int index = arr.size();
									for (int i = 0; i < index; i++) {
										if (arr.getJSONObject(i).get("train_code").equals(
												orderInfo.getTrain_no())) {
											runtime = arr.getJSONObject(i).getString(
													"run_time_minute");
											start_query_time = arr.getJSONObject(i)
													.getString("start_time");
											arrive_query_time = arr.getJSONObject(i)
													.getString("arrive_time");
											break;
										}

									}
									if (start_query_time == null
											|| arrive_query_time == null || runtime == null) {
										logger.info("美团book通知,针对没有to_time采用查询补全_查询失败,未匹配到结果");
//										throw new RuntimeException("to_time采用查询补全_查询失败");
									} else {
										logger.info("美团book通知,针对没有to_time采用查询补全_查询成功"
												+ start_query_time + "_"
												+ arrive_query_time + "_" + runtime);
										j.put("start_time", start_query_time);
										j.put("arrive_time", arrive_query_time);
										j.put("duration", runtime);// 运行时间
									}
								}catch(JSONException e1){
									logger.info("美团book通知,针对没有to_time采用查询补全_查询异常, result="+result);
									e1.printStackTrace();
								}
							}
						
						} else {
							start_time = fromStation.getStartTime();
							start_query_time = fromStation.getStartTime();
							arrive_time = toStation.getArriveTime();
							logger.info(orderInfo.getOrder_id() + " 美团数据库时间补全成功,startTime : " + start_time + " ,arriveTime : " + arrive_time);
							j.put("start_time", start_time);
							j.put("arrive_time", arrive_time);
							String travleDate = orderInfo.getTravel_date();
							String runtime = DateUtil.minuteDiff(DateUtil.dateAddDays ( DateUtil.stringToDate(
									travleDate +" "+arrive_time+":00", "yyyy-MM-dd HH:mm:ss"),toStation.getCost()), DateUtil
									.stringToDate(travleDate +" "+start_time+":00", "yyyy-MM-dd HH:mm:ss"))
									+ "";
							j.put("duration", runtime);// 运行时间
						}
						//更新出发时间
						if(start_query_time!=null && start_time==null){
							orderInfo.setFrom_time(orderInfo.getTravel_date()+" "+start_query_time+":00");
							orderDao.updateOrderInfo(orderInfo);
						}
					}else {
						String runtime = DateUtil.minuteDiff(DateUtil.stringToDate(
								arrive_time, "yyyy-MM-dd HH:mm:ss"), DateUtil
								.stringToDate(start_time, "yyyy-MM-dd HH:mm:ss"))
								+ "";
						start_time = start_time.substring(
								start_time.indexOf(" ") + 1, start_time
										.indexOf(" ") + 6);
						arrive_time = arrive_time.substring(arrive_time
								.indexOf(" ") + 1, arrive_time.indexOf(" ") + 6);
						j.put("start_time", start_time == null ? "" : start_time);
						j.put("arrive_time", arrive_time == null ? "" : arrive_time);
						j.put("duration", runtime);// 运行时间
					}
					
					j.put("ordernumber",
							orderInfo.getOut_ticket_billno() == null ? ""
									: orderInfo.getOut_ticket_billno());
					/** 时间 计算方式 */
				} else {
					j.put("train_date", orderInfo.getTravel_date());
					j.put("start_time", "");
					j.put("arrive_time", "");
					j.put("ordernumber", "");
					j.put("duration", "");// 运行时间
				}

				JSONArray parr = new JSONArray();
				for (DBPassengerInfo p : cpInfoList) {
					JSONObject p_json = new JSONObject();
					String seat_no = p.getSeat_no();
					String train_box = p.getTrain_box();
					String cp_id = p.getCp_id();
					// String passengerid=cp_id.substring(cp_id.lastIndexOf("_")+1);
					p_json.put("passengerid", p.getOut_passengerid());
					p_json.put("ticket_no", ordersuccess ? p.getCp_id() : "");
					p_json.put("passenger_name", p.getUser_name());
					p_json.put("certificate_no", p.getUser_ids());

					p_json.put("certificate_type", p.getElong_ids_type());
					p_json.put("certificate_name", TongChengConsts
							.getPassporttypeseidname(p.getElong_ids_type()));
					p_json.put("ticket_type", p.getElong_ticket_type());
					// p_json.put("piaotypename",
					// TongChengConsts.getPiaotypename(p.getElong_ticket_type()));

					// 系统扩展坐席类型：21动卧 22高级动卧 23一等软座 24二等软座
					// 订单请求坐席类型：M一等座 O二等座 4软卧 F动卧 A高级软座 ,7一等软座 8二等软座

					// 9:商务座，P:特等座，M:一等座，O:二等座，6:高级软卧，
					// 4:软卧，3:硬卧，2:软座，1:硬座 7:一等软座 8:二等软座 A:高级动卧 F:动卧

					// 美团9:商务座，P:特等座，M:一等座，O:二等座，6:高级软卧，4:软卧，3:硬卧，2:软座，1:硬座
					// meituan座位类型 :1硬座 2硬卧上 3硬卧中 4硬卧下 5软座 6软卧上 7软卧中 8软卧下 9商务座 10观光座
					// 11一等包座 12特等座
					// 13一等座 14二等座 15高级软卧上 16高级软卧下 17无座 18一人软包 10动卧 21高级动卧 22包厢硬卧

					//出票结果回传坐席以实际出票坐席为准----美团坐席
					String mseatType =p.getSeat_type();//19e坐席
					String meituanseat = p.getElong_seat_type();
					if(StrUtil.isNotEmpty(seat_no)){//为空则为无座
						if("4".equals(mseatType) && seat_no.contains("上")){//19e坐席 4、高级软卧（41、高级软卧上 42、高级软卧下）
							mseatType = "15";//美团坐席15高级软卧上 16高级软卧下
							p_json.put("seat_type",mseatType);// 座位编码
						}else if("4".equals(mseatType) && seat_no.contains("下")){
							mseatType = "16";
							p_json.put("seat_type",mseatType);// 座位编码
						}else if("5".equals(mseatType) && seat_no.contains("上")){//5、软卧 （51、软卧上 52、软卧下）
							mseatType = "6";
							p_json.put("seat_type",mseatType);// 座位编码
						}else if("5".equals(mseatType) && seat_no.contains("中")){
							mseatType = "7";
							p_json.put("seat_type",mseatType);// 座位编码
						}else if("5".equals(mseatType) && seat_no.contains("下")){
							mseatType = "8";
							p_json.put("seat_type",mseatType);// 座位编码
						}else if("6".equals(mseatType) && seat_no.contains("上")){//5、6、硬卧 （61、硬卧上 62、硬卧中 63、硬卧下）
							mseatType = "2";//2硬卧上 3硬卧中 4硬卧下
							p_json.put("seat_type",mseatType);// 座位编码
						}else if("6".equals(mseatType) && seat_no.contains("中")){
							mseatType = "3";
							p_json.put("seat_type",mseatType);// 座位编码
						}else if("6".equals(mseatType) && seat_no.contains("下")){
							mseatType = "4";
							p_json.put("seat_type",mseatType);// 座位编码
						}else{
							p_json.put("seat_type",meituanseat);// 座位编码
						}
					}else{
						p_json.put("seat_type",meituanseat);// 座位编码
					}
					
					// p_json.put("seat_type",zwcode);
//					p_json.put("seat_type", p.getElong_seat_type());// 座位编码
					// p_json.put("zwname",TongChengConsts.getZwname(zwcode));

					p_json.put("seat_detail_no",
							ordersuccess ? ((StrUtil.isEmpty(train_box) ? ""
									: (train_box + "车厢,")) + (StrUtil
									.isEmpty(seat_no) ? "无座" : seat_no)) : "");
					p_json.put("price", ordersuccess ? p.getBuy_money() : "");

					// 审核未通过
					if (ordersuccess) {
						// 身份核验状态 0：正常 1：待审核 2：未通过
						p_json.put("certificate_verify_status", "0");
					} else {
						if ("6".equals(orderInfo.getOut_fail_reason())) {
							p_json.put("certificate_verify_status", "1");
						} else {
							p_json.put("certificate_verify_status", "0");
						}
					}
					parr.add(p_json);
				}
				j.put("passengers", parr.toString());
				Map<String, String> params = new HashMap<String, String>();
				params.put("passengers", j.toString());
				logger.info("美团book核验请求参数" + j.toString());
				long start = System.currentTimeMillis();
//				String sendParams = ElongUrlFormatUtil.createGetUrl("", params, "utf-8");
	//
//				String result = HttpUtil.sendByPost(vo.getBook_notify_url(), j.toString(), "utf-8");
				String result = HttpPostJsonUtil.sendJsonPost(vo.getBook_notify_url(), j.toString(), "utf-8");
				logger.info("【美团出票前核验通知接口返回result】"+result);
				if (result != null && StringUtils.isNotEmpty(result)) {
					JSONObject json = JSONObject.fromObject(result);
					logger.info("美团book核验返回结果,耗时"
							+ (System.currentTimeMillis() - start) + "ms,result:"
							+ result);
					// JSONObject json=JSONObject.fromObject(result);
					//{"code":100,"msg":"处理或操作成功","success":true}
					if (("100".equals(json.get("code")) || json.getInt("code")==100 || (json.getBoolean("success"))) && ordersuccess) {// TODO 可以出票
						logger.info("美团book核验通知成功,耗时"
								+ (System.currentTimeMillis() - start)
								+ "ms,order_id:" + order_id + ",result:" + result);
						bookResultSendUpdateNotice("SUCCESS", vo, result,
								ordersuccess, orderInfo);
					} else {
						try{
							Integer code = json.getInt("code");
							String failCode = "";
							if(code.equals(305)){
								failCode = "4";
							}else if(code.equals(304)){
								failCode = "3";
							}
							//保存取消出票失败原因
							DBOrderInfo orderCancel = new DBOrderInfo();
							orderCancel.setOrder_id(orderInfo.getOrder_id());
							orderCancel.setOrder_status(orderInfo.getOrder_status());
							orderCancel.setOut_fail_reason(failCode);
							orderDao.updateOrderInfo(orderCancel);
						}catch(Exception e){
							logger.info("美团取消订单获取错误原因异常");
						}
						
						logger.info("美团book核验通知成功【取消订单】,order_id:" + order_id + ",result:" + result);
						bookResultSendUpdateNotice("CANCEL", vo, result, ordersuccess, orderInfo);
					}
				} else {
					logger.info("美团book核验通知失败,order_id:" + order_id
							+ ",result返回:"+result);
					bookResultSendUpdateNotice("FAIL", vo, result, ordersuccess,
							orderInfo);
				}
			}

		} catch (Exception e) {
			logger.info("美团book核验通知异常,order_id:" + order_id + ",msg:" + e);
			bookResultSendUpdateNotice("EXCEPTION", vo, "", ordersuccess,
					orderInfo);
			e.printStackTrace();
		}
	}

	/**
	 * 通知各渠道出票结果 更新通知表
	 * */
	private void bookResultSendUpdateNotice(String noticeStatus, DBNoticeVo vo,
			String result, boolean ordersuccess, DBOrderInfo orderInfo) {
		//if ("meituan".equals(vo.getChannel())) {
			String order_id = vo.getOrder_id();
			String channel = vo.getChannel();
			int num = vo.getBook_notify_num();

			DBNoticeVo noticeInfo = new DBNoticeVo();
			noticeInfo.setOrder_id(order_id);
			noticeInfo.setChannel(channel);
			noticeInfo.setBook_notify_time("out_notify_time");
			noticeInfo.setBook_notify_num(num + 1);

			/** 更新订单 通知状态 */
			Map<String, String> orderNotice = new HashMap<String, String>();
			orderNotice.put("order_id", order_id);
			orderNotice.put("channel", Consts.CHANNEL_MEITUAN);

			/** logsvo */
			ElongOrderLogsVo log = null;
			if ("SUCCESS".equals(noticeStatus)) {
				orderNotice.put("notice_status", Consts.NOTICE_OVER);
				noticeInfo.setBook_notify_finish_time("finished");
				noticeInfo.setBook_notify_status(Consts.NOTICE_OVER);
				log = new ElongOrderLogsVo(order_id, "通知" + channel + "预订结果_成功", "meituan_app");
				if(!"11".equals(orderInfo.getOrder_type())){
					// 确认出票
					String confirmTrain = pay(order_id, orderInfo.getBuy_money());
					if ("SUCCESS".equals(confirmTrain)) {
						
						logger.info("美团-请求[App]确认出票SUCCESS,order_id:" + order_id);
						ElongOrderLogsVo logmt = new ElongOrderLogsVo();
						logmt.setOpt_person("meituan_app");
						logmt.setContent("美团确认出票[成功]");
						logmt.setOrder_id(order_id);
						elongOrderDao.insertElongOrderLogs(logmt);
					} else {
						logger.info("1美团-请求[App]确认出票Fail,order_id:" + order_id);
						// 确认出票失败-----重新确认出票
						String confirmTrain2 = pay(order_id, orderInfo.getBuy_money());
						if ("SUCCESS".equals(confirmTrain2)) {
							logger.info("美团-请求[App]确认出票SUCCESS,order_id:" + order_id);
							ElongOrderLogsVo logmt = new ElongOrderLogsVo();
							logmt.setOpt_person("meituan_app");
							logmt.setContent("美团确认出票[成功]");
							logmt.setOrder_id(order_id);
							elongOrderDao.insertElongOrderLogs(logmt);
						} else {
							logger.info("2美团-请求[App]确认出票Fail,order_id:" + order_id);
						}
					}
				}
			} else if("CANCEL".equals(noticeStatus)){//取消订单
				noticeInfo.setBook_notify_finish_time("finished");
				noticeInfo.setBook_notify_status(Consts.NOTICE_OVER);
				noticeInfo.setOut_notify_status(Consts.NOTICE_START);//取消订单默认设置出票通知开始
				log = new ElongOrderLogsVo(order_id, "通知" + channel + "预订结果_成功[美团申请取消订单]", "meituan_app");
				orderNotice.put("notice_status", Consts.NOTICE_OVER);
				if(ordersuccess){
					//取消订单
					String cancelResult = cancel(order_id);
					if ("SUCCESS".equals(cancelResult)) {
						logger.info("美团-请求[App]取消订单SUCCESS,order_id:" + order_id);
						ElongOrderLogsVo logmt = new ElongOrderLogsVo();
						logmt.setOpt_person("meituan_app");
						logmt.setContent("美团取消订单[成功]");
						logmt.setOrder_id(order_id);
						elongOrderDao.insertElongOrderLogs(logmt);
					} else {
						logger.info("1美团-请求[App]取消订单Fail,order_id:" + order_id);
						// 确认出票失败-----重新确认出票
						String cancelResult2 = cancel(order_id);
						if ("SUCCESS".equals(cancelResult2)) {
							logger.info("美团-请求[App]取消订单SUCCESS,order_id:" + order_id);
							ElongOrderLogsVo logmt = new ElongOrderLogsVo();
							logmt.setOpt_person("meituan_app");
							logmt.setContent("美团取消订单[成功]");
							logmt.setOrder_id(order_id);
							elongOrderDao.insertElongOrderLogs(logmt);
						} else {
							logger.info("2美团-请求[App]取消订单Fail,order_id:" + order_id);
						}
					}
				}
			} else {
				String all_notify_status = num == (Consts.TCBOOK_NOTICE_NUM - 1) ? Consts.NOTICE_FAIL
						: Consts.NOTICE_ING;
				noticeInfo.setBook_notify_status(all_notify_status);
				if ("FAIL".equals(noticeStatus)) {
					log = new ElongOrderLogsVo(order_id, "第" + (num + 1)
							+ "次通知" + channel + "预订结果_失败,返回信息[" + result + "]"
							+ ",等待重新通知", "meituan_app");
				} else {// 异常处理
					log = new ElongOrderLogsVo(order_id, "第" + (num + 1)
							+ "次通知" + channel + "预订结果_异常,等待重新通知", "meituan_app");
				}
				if (all_notify_status.equals(Consts.NOTICE_FAIL)) {
					orderNotice.put("notice_status", Consts.NOTICE_FAIL);

					/** 针对预订结果通知 失败的更新通知失败 */
					orderDao.updateOrderNoticeStatus(orderNotice);
				} else {
					orderNotice.put("notice_status", Consts.NOTICE_ING);
				}

			}
			noticeDao.updateNotice(noticeInfo);
			elongOrderDao.insertElongOrderLogs(log);
			// 更新订单表的通知状态
			/** 设置更新状态的单向性 00、准备通知 --> 11、开始通知 --> 22、通知完成 --> 33、通知失败 */
			// 是否设置一个 预订通知 成功与否的状态值

			// 针对预订失败的通知 有通知成功项
			// orderDao.updateOrderBookNoticeStatus(orderNotice);
			if (!ordersuccess) {
				orderDao.updateOrderNoticeStatus(orderNotice);
			}
		//}

	}

	@Override
	public void updateOrderInfo(List<Map<String, String>> cpMapList,
			DBOrderInfo orderInfo) {
		String channel = orderInfo.getChannel();
		// System.out.println(channel+"channel--------------------------");
		ElongOrderLogsVo log = new ElongOrderLogsVo();
		log.setOpt_person("meituan_app");
		log.setOrder_id(orderInfo.getOrder_id());
		String status = orderInfo.getOrder_status();
		//if (Consts.CHANNEL_MEITUAN.equals(channel)) {
			String order_status = orderInfo.getOrder_status();
			if (Consts.ELONG_ORDER_FAIL.equals(order_status)
					&& "11".equals(orderInfo.getOut_fail_reason())) {
				// 超时未支付 更新为超时订单
				orderInfo.setOrder_status(Consts.ELONG_OUT_TIME);
			}
			if (Consts.ELONG_ORDER_FAIL.equals(order_status)
					&& "6".equals(orderInfo.getOut_fail_reason())) {
				// 用户取消 更新为取消成功订单
				orderInfo.setOrder_status(Consts.ELONG_ORDER_CANCELED);
			}

			orderDao.updateOrderInfo(orderInfo);
			if (cpMapList != null) {
				for (Map<String, String> cpInfo : cpMapList) {
					cpInfo.put("order_id", orderInfo.getOrder_id());
					cpInfo.put("out_ticket_billno", orderInfo
							.getOut_ticket_billno());
					orderDao.updateCpOrderInfo(cpInfo);
				}
			}
			DBNoticeVo notice = new DBNoticeVo();
			notice.setOrder_id(orderInfo.getOrder_id());
			notice.setChannel(Consts.CHANNEL_MEITUAN);
			if (Consts.ELONG_ORDER_FAIL.equals(order_status)) {
				/*if (orderInfo.getOrder_status().equals(Consts.ELONG_OUT_TIME)) {
					log.setContent("超时订单不给予出票通知" + orderInfo.getOrder_id());
					elongOrderDao.insertElongOrderLogs(log);
//				} else if (orderInfo.getOrder_status().equals(
//						Consts.ELONG_ORDER_CANCELED)) {
//					log.setContent("失败原因为[用户取消],启用取消回调"
//							+ orderInfo.getOrder_id());
//					elongOrderDao.insertElongOrderLogs(log);
				} else {
					// 区分预订失败通知 出票失败通知
					String book_notify = noticeDao
							.selectBookNoticeStatus(orderInfo.getOrder_id());
					if (Consts.NOTICE_OVER.equals(book_notify)) {
						// 出票失败通知 区分超时补单通知 正常通知
						notice.setOut_notify_status(Consts.NOTICE_START);
						noticeDao.updateNotice(notice);
					} else {
						notice.setBook_notify_status(Consts.NOTICE_START);
						noticeDao.updateNotice(notice);
					}
				}*/
				// 区分预订失败通知 出票失败通知
				String book_notify = noticeDao
						.selectBookNoticeStatus(orderInfo.getOrder_id());
				//orderInfo.getOrder_type().equals("11")
				if(("6".equals(orderInfo.getOut_fail_reason())||"11".equals(orderInfo.getOut_fail_reason()))){
					//先占座的订单取消不回调
					notice.setBook_notify_status(Consts.NOTICE_OVER);
					notice.setOut_notify_status(Consts.NOTICE_OVER);
					noticeDao.updateNotice(notice);
					Map<String, String> orderNotice = new HashMap<String,String>();
					orderNotice.put("notice_status", "22");
					orderNotice.put("order_id", orderInfo.getOrder_id());
					orderDao.updateOrderNoticeStatus(orderNotice);
					log.setContent("美团取消订单或超时订单不回调，默认设置通知成功");
					elongOrderDao.insertElongOrderLogs(log);
				}/*else if(orderInfo.getOrder_type().equals("11")&& book_notify!=null ){
					notice.setBook_notify_status(Consts.NOTICE_OVER);
					notice.setOut_notify_status(Consts.NOTICE_START);
					noticeDao.updateNotice(notice);
				}*/else{
					if (Consts.NOTICE_OVER.equals(book_notify)&& !"11".equals(orderInfo.getOut_fail_reason())) {
						// 出票失败通知 区分超时补单通知 正常通知
						notice.setOut_notify_status(Consts.NOTICE_START);
						noticeDao.updateNotice(notice);
					} else {
						notice.setBook_notify_status(Consts.NOTICE_START);
						noticeDao.updateNotice(notice);
					}
				}
			} else {
				logger.info("获取美团的出票结果通知,订单状态order_status:" + order_status);
			}
			if(status==null||status==""){
				log.setContent("修改订单信息");
			}else{
				log.setContent("出票系统返回结果_" + (Consts.ELONG_ORDER_MAKED.equals(status) ? "预订成功"
						: Consts.ELONG_ORDER_SUCCESS.equals(status) ? "出票成功"
						: Consts.ELONG_ORDER_FAIL.equals(status) ? ("出票失败[原因:"
						+ TongChengConsts.getErrorInfo(orderInfo.getOut_fail_reason())
						+ ",乘客错误信息:" + orderInfo.getPassenger_reason()): "成功") + "");
			}
			
		//}

		/** 插入订单操作 日记 */
		elongOrderDao.insertElongOrderLogs(log);
	}

	@Override
	public String cancel(String orderId) {
		String notify_cp_cancel_url = Consts.NOTIFY_CP_CANCEL_URL;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_id", orderId);
		String params;
		try {
			params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
			String result = HttpUtil.sendByPost(notify_cp_cancel_url, params,
					"UTF-8");
			logger.info("请求出票系统取消订单:" + orderId + "请求返回结果:" + result);

			if (!StringUtils.isEmpty(result)) {
				if ("success".equalsIgnoreCase(result)) {// 通知成功
					Map<String, String> map = new HashMap<String, String>();
					// 通知出票系统成功则订单状态修改为正在出票
					map.put("order_id", orderId);
					map.put("old_order_status1", Consts.ELONG_ORDER_MAKED);// 预订成功
					map.put("order_status", Consts.ELONG_ORDER_CANCELED);// 取消成功
					orderDao.updateOrderStatus(map);
					logger.info("请求出票系统取消订单,order_id=" + orderId);
					return "SUCCESS";
				} else {// 超时重发
					logger.info("请求出票系统取消订单,order_id=" + orderId + "result:"
							+ result);
					return "FAIL";
				}
			} else {
				logger.info("请求出票系统取消订单,order_id=" + orderId);
				return "FAIL";
			}
		} catch (Exception e) {
			logger.info("请求出票系统取消订单系统异常" + e);
			e.printStackTrace();
			return "EXCEPTION";
		}
	}

	@Override
	public String pay(String orderId, String payMoney) {
		String notify_cp_PAY_url = Consts.NOTIFY_CP_PAY_URL;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_id", orderId);
		paramMap.put("pay_money", payMoney);// 车票总价
		String params;
		try {
			params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
			String result = HttpUtil.sendByPost(notify_cp_PAY_url, params,
					"UTF-8");
			logger.info("请求出票系统开始支付:" + orderId + "请求返回结果:" + result);

			if (!StringUtils.isEmpty(result)) {
				if ("success".equalsIgnoreCase(result)) {// 通知成功
					Map<String, String> map = new HashMap<String, String>();
					// 通知出票系统成功则订单状态修改为正在出票
					map.put("order_id", orderId);
					map.put("old_order_status1", Consts.ELONG_ORDER_MAKED);// 预订成功
					map.put("order_status", Consts.ELONG_ORDER_WAITPAY);// 支付中
					orderDao.updateOrderStatus(map);
					orderDao.updateOrderPayMoney(paramMap);
					logger.info("请求出票系统开始支付,order_id=" + orderId);
					return "SUCCESS";
				} else {// 超时重发
					logger.info("请求出票系统开始支付,order_id=" + orderId + "result:"
							+ result);
					return "FAIL";
				}
			} else {
				logger.info("请求出票系统开始支付,order_id=" + orderId);
				return "FAIL";
			}
		} catch (Exception e) {
			logger.info("请求出票系统开始支付系统异常" + e);
			e.printStackTrace();
			return "EXCEPTION";
		}
	}

	@Override
	public String queryRefundStatus(Map<String, String> paramMap) {
		return orderDao.queryRefundStatus(paramMap);
	}

	@Override
	public void insertRefundOrder(Map<String, String> paramMap) {
		orderDao.insertRefundOrder(paramMap);
	}

	
	
	public static void main(String[] args) {
		String str = "TC_54C2286921003DC127_0";
		System.out.println(str.substring(str.lastIndexOf("_") + 1));

		String runtime = DateUtil.minuteDiff(DateUtil.stringToDate(
				"2014-06-11 16:05:02", "yyyy-MM-dd HH:mm:ss"), DateUtil
				.stringToDate("2014-06-11 16:01:02", "yyyy-MM-dd HH:mm:ss"))
				+ "";
		System.out.println(runtime);
		String result = "[{\"access_byidcard\":\"1\",\"arrive_days\":\"0\",\"arrive_time\":\"13:41\",\"can_buy_now\":\"Y\",\"edz_num\":\"285\",\"end_station_name\":\"宁波\",\"from_station_code\":\"VNP\",\"from_station_name\":\"北京南\",\"gjrw_num\":\"-\",\"note\":\"\",\"qtxb_num\":\"-\",\"run_time\":\"06:16\",\"run_time_minute\":\"376\",\"rw_num\":\"-\",\"rz_num\":\"-\",\"sale_date_time\":\"\",\"start_station_name\":\"北京南\",\"start_time\":\"07:25\",\"swz_num\":\"11\",\"tdz_num\":\"\",\"to_station_code\":\"SSH\",\"to_station_name\":\"上虞北\",\"train_code\":\"G57\",\"train_no\":\"2400000G5703\",\"train_start_date\":\"20150301\",\"train_type\":\"\",\"wz_num\":\"-\",\"ydz_num\":\"-\",\"yw_num\":\"-\",\"yz_num\":\"-\"}]";

		JSONArray arr = JSONArray.fromObject(result);
		String mi = null;
		String from_time = null;
		String to_time = null;
		int index = arr.size();
		for (int i = 0; i < index; i++) {
			if (arr.getJSONObject(i).get("train_code").equals("G57")) {
				mi = arr.getJSONObject(i).getString("run_time_minute");
				from_time = arr.getJSONObject(i).getString("start_time");
				to_time = arr.getJSONObject(i).getString("arrive_time");
				break;
			}
		}

		System.out.println(mi + "   " + from_time + "   " + to_time);

	}
	public  String getHoldErrorCode(String errorInfo){
		if(errorInfo.equals("1")){
			return "502";
		}else if(errorInfo.equals("2")){
			return "507";
		}else if(errorInfo.equals("8")){
			return "509";
	    }else{
	    	return "0";
	    }
	}
	@Override
	public Map<String, Object> queryAllChannelNotify(String order_id) {
		return orderDao.queryAllChannelNotify(order_id);
	}

	@Override
	public String querySeatNo(Map<String, String> cpInfo) {
		return orderDao.querySeatNo(cpInfo);
	}

	@Override
	public List<Map<String, String>> queryPassengerList(
			Map<String, String> param) {
		return orderDao.queryPassengerList(param);
	}

	@Override
	public List<Map<String, String>> queryPassersList(
			HashMap<String, Object> map) {
		return orderDao.queryPassersList(map);
	}

	@Override
	public int queryPassersCount() {
		return orderDao.queryPassersCount();
	}

	@Override
	public void addVerifyTime(Map<String, String> timeMap) {
		orderDao.addVerifyTime(timeMap);
	}

	@Override
	public String queryVerfiyTimeRatio() {
		return orderDao.queryVerfiyTimeRatio();
	}

	@Override
	public void addPhone(Map<String, String> paramMap) {
		orderDao.addPhone(paramMap);
	}
	
}
