package com.l9e.transaction.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.common.Consts;
import com.l9e.common.TongChengConsts;
import com.l9e.transaction.service.ChangeService;
import com.l9e.transaction.service.ElongOrderService;
import com.l9e.transaction.service.NoticeService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.DBNoticeVo;
import com.l9e.transaction.vo.DBOrderInfo;
import com.l9e.transaction.vo.DBPassengerInfo;
import com.l9e.transaction.vo.DBStudentInfo;
import com.l9e.transaction.vo.ElongOrderLogsVo;
import com.l9e.util.AmountUtil;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpPostJsonUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.StrUtil;
import com.l9e.util.UrlFormatUtil;


/**
 * 美团 主控制器（美团向19e发起的请求）
 */
@Controller
@RequestMapping("/RestfulService")
public class MeituanController extends BaseController{
	
	private static Logger logger= Logger.getLogger(MeituanController.class);
	
	@Resource
	private OrderService orderService;
	
	@Resource
	private ElongOrderService elongOrderService;
	
	@Resource
	private NoticeService noticeService;
	
	@Resource
	private ChangeService changeService;
	
	//查询车次、余票、票价信息
	@ResponseBody
	@RequestMapping("/trainQuery")
	public void trainQuery(HttpServletRequest request, HttpServletResponse response, @RequestBody String json){
		String serviceid = "trainQuery";
		trainMeituanCommon(request, response, serviceid, json);
	}
	
	//火车票下订单接口
	@ResponseBody
	@RequestMapping("/trainOrder")
	public void trainOrder(HttpServletRequest request, HttpServletResponse response, @RequestBody String json){
		String serviceid = "trainOrder";
		trainMeituanCommon(request, response, serviceid, json);
	}
	
	//火车票退票接口
	@ResponseBody
	@RequestMapping("/returnTickets")
	public void refundTickets(HttpServletRequest request, HttpServletResponse response, @RequestBody String json){
		logger.info("美团请求退票");
		String serviceid = "returnTickets";
		trainMeituanCommon(request, response, serviceid, json);
	}
	
	//车次查询(途经站)
	@ResponseBody
	@RequestMapping("/queryTrainInfo")
	public void queryTrainInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody String json){
		String serviceid = "queryTrainInfo";
		trainMeituanCommon(request, response, serviceid, json);
	}
	
	//美团取消代理商订单
	@ResponseBody
	@RequestMapping("/cancelOrder")
	public void cancelOrder(HttpServletRequest request, HttpServletResponse response, @RequestBody String json){
		String serviceid = "cancelOrder";
		trainMeituanCommon(request, response, serviceid, json);
	}
	
	//请求改签
	@ResponseBody
	@RequestMapping("/resignTicket")
	public void resignTicket(HttpServletRequest request, HttpServletResponse response, @RequestBody String json){
		String serviceid = "resignTicket";
		trainMeituanCommon(request, response, serviceid, json);
	}
	//取消改签
	@ResponseBody
	@RequestMapping("/cancelResign")
	public void cancelResign(HttpServletRequest request, HttpServletResponse response, @RequestBody String json){
		String serviceid = "cancelResign";
		trainMeituanCommon(request, response, serviceid, json);
	}
	//确认改签
	@ResponseBody
	@RequestMapping("/confirmResign")
	public void confirmResign(HttpServletRequest request, HttpServletResponse response, @RequestBody String json){
		String serviceid = "confirmResign";
		trainMeituanCommon(request, response, serviceid, json);
	}
	//占座接口
	@ResponseBody
	@RequestMapping("/holdseat")
	public void holdseat(HttpServletRequest request, HttpServletResponse response, @RequestBody String json){
		String serviceid = "holdseat";
		trainMeituanCommon(request, response, serviceid, json);
	}
	//取消订单接口
	@ResponseBody
	@RequestMapping("/cancelHoldSeat")
	public void cancelHoldSeat(HttpServletRequest request, HttpServletResponse response, @RequestBody String json){
		String serviceid = "cancelHoldSeat";
		trainMeituanCommon(request, response, serviceid, json);
	}
	
	/**
	 * 美团通用参数处理
	 */
	private void trainMeituanCommon(HttpServletRequest request,
			HttpServletResponse response, String serviceid, String jsonStr) {
		/** 返回json */
		JSONObject json = new JSONObject();
		try {
//			String jsonStr = getParam(request, "callbackurl");
			logger.info("美团传过来的serviceid："+serviceid+"，jsonStr："+jsonStr);
//			logger.info("1111美团传过来的jsonStr："+getParam(request, "body"));
			if (StrUtil.isEmpty(jsonStr) || jsonStr==null) {
				logger.info("美团main-传入的 json为空对象");
				json.put("success", false);
				json.put("code", 101);
				json.put("msg", "传入的json为空对象");
				printJson(response, json.toString());
				return;
			}
			JSONObject webJson = JSONObject.fromObject(jsonStr);
			// 通用输入参数
			String partnerid = getJsonValue(webJson, "partnerid");
			String reqtime = getJsonValue(webJson, "reqtime");// 请求时间，格式：yyyyMMddhhmmss（非空）例：20140101093518
			String sign = getJsonValue(webJson, "sign");// 数字签名=md5(partnerid+serviceid+reqtime+md5(key))，
														// md5算法得到的字符串全部为小写,key为美团分配给代理商的密码
			logger.info("美团-通用参数:partnerid," + partnerid + "|reqtime,"
					+ reqtime + "|sign," + sign);
			
			 if (StrUtil.isEmpty(partnerid) || StrUtil.isEmpty(reqtime)
					|| StrUtil.isEmpty(sign)) {
				logger.info("美团-通用参数缺失:partnerid," + partnerid + "|reqtime,"
						+ reqtime + "|sign," + sign);
				json.put("success", false);
				json.put("code", 103);
				json.put("msg", "通用参数缺失");
				printJson(response, json.toString());
				return;
			}
			/*
			 * if(!sysPartnerid.equals(partnerid)){ json.put("success", false);
			 * json.put("code", 104); json.put("msg", "账户无效");
			 * printJson(response,json.toString()); return; }
			 */
			String sysSign = DigestUtils.md5Hex(
					partnerid + serviceid + reqtime
							+ DigestUtils.md5Hex(Consts.MT_KEY).toLowerCase())
					.toLowerCase();
			if (sysSign.equals(sign)) {
				if ("trainQuery".equals(serviceid)) {// 查询车次、余票、票价信息
					trainQueryMeituan(webJson, request, response);
				} else if ("trainOrder".equals(serviceid)) {// 火车票下订单接口
					trainOrderMeituan(webJson, request, response);
				} else if ("returnTickets".equals(serviceid)) {// 火车票退票接口
					returnTicketsMeituan(webJson, request, response);
				} else if ("queryTrainInfo".equals(serviceid)) {// 车次查询(途经站)
					queryTrainInfoMeituan(webJson, request, response);
				} else if ("cancelOrder".equals(serviceid)) {// 美团取消代理商订单
					cancelOrderMeituan(webJson, request, response);
				} else if("resignTicket".equals(serviceid)){//请求改签
					resignTicketMeituan(webJson, request, response);
				}else if("cancelResign".equals(serviceid)){//取消改签
					cancelResignMeituan(webJson, request, response);
				}else if("confirmResign".equals(serviceid)){//确认改签
					confirmResignMeituan(webJson, request, response);
				}else if("holdseat".equals(serviceid)){
					holdseat(webJson, request, response);//占座
				}else if("cancelHoldSeat".equals(serviceid)){
					cancelHoldSeat(webJson, request, response);//取消订单
				}
				else {
					logger.info("美团-error非法的请求serviceid:" + serviceid);
					json.put("success", false);
					json.put("code", 106);
					json.put("msg", "系统没有提供这个接口或者没有调用权限");
					printJson(response, json.toString());
				}
			} else {
				logger.info("美团-身份校验错误:sysSign," + sysSign + "|sign," + sign
						+ "|param," + partnerid + reqtime
						+ (DigestUtils.md5Hex(Consts.MT_KEY).toLowerCase()));
				json.put("success", false);
				json.put("code", 105);
				json.put("msg", "错误的通用参数");
				printJson(response, json.toString());
				return;
			}
		} catch (Exception e) {
			logger.info("美团-请求异常:" + e);
			json.put("success", false);
			json.put("code", 102);
			json.put("msg", "传入的 json 格式错误");
			printJson(response, json.toString());
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 美团查询车次、余票及票价信息===============同程车票预订查询
	 */
	private void trainQueryMeituan(JSONObject webJson,HttpServletRequest request, 
			HttpServletResponse response) { 
		String train_date = getJsonValue(webJson, "train_date");
		String from_station = getJsonValue(webJson, "from_station");
		String to_station = getJsonValue(webJson, "to_station");
		String purpose_codes = getJsonValue(webJson, "purpose_codes");
		if(train_date.equals("")||from_station.equals("")||to_station.equals("")||purpose_codes.equals("")){
			logger.info("参数为null");
		}else{
			try {
				Map<String,String> paramMap=new HashMap<String, String>();
				String url=Consts.QUERY_PRICE;
				paramMap.put("channel", Consts.CHANNEL_MEITUAN);
				paramMap.put("from_station", from_station);
				paramMap.put("arrive_station", to_station);
				paramMap.put("travel_time", train_date);
				paramMap.put("purpose_codes", purpose_codes);
				paramMap.put("isNotZW", "no");//非中文查询
				String params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
				logger.info("美团发起余票查询"+paramMap.toString()+"url"+url);
				String result = HttpUtil.sendByPost(url, params, "UTF-8");
				
				logger.info("美团发起余票查询result："+result);
				if(result.equalsIgnoreCase("STATION_ERROR")){
					JSONObject jsonReturn=new JSONObject();	
					jsonReturn.put("success", false);
					jsonReturn.put("code",108);
					jsonReturn.put("msg", "错误的业务参数");
					printJson(response,jsonReturn.toString());
				}else if(result==null||result.equalsIgnoreCase("ERROR")){
					JSONObject jsonReturn=new JSONObject();	
					jsonReturn.put("success", false);
					jsonReturn.put("code",202);
					jsonReturn.put("msg", "查询失败");
					printJson(response,jsonReturn.toString());
				}else if(result==null||result.equalsIgnoreCase("NO_DATAS")){
					JSONObject jsonReturn=new JSONObject();	
					jsonReturn.put("success", false);
					jsonReturn.put("code", 201);
					jsonReturn.put("msg", "没有符合条件的车次信息");
					printJson(response,jsonReturn.toString());
				}else{
					JSONObject jsonReturn=new JSONObject();	
					jsonReturn.put("success", true);
					jsonReturn.put("code",100);
					jsonReturn.put("msg", "正常获得结果");
					jsonReturn.put("data", result);
					printJson(response,jsonReturn.toString());
				}
			}catch (Exception e) {
				e.printStackTrace();
				logger.info("美团发起余票查询,系统异常"+e);
				JSONObject json=new JSONObject();	
				json.put("success", false);
				json.put("code",202);
				json.put("msg", "查询失败");
				printJson(response,json.toString());
				e.printStackTrace();
			}
		}
	} 
	
	
	
	
	
	/**
	 * 美团火车票下订单接口
	 */
	private void trainOrderMeituan(JSONObject webJson,HttpServletRequest request, 
			HttpServletResponse response) {
		String orderid = getJsonValue(webJson, "orderid");//美团的订单号
		String reqtoken = getJsonValue(webJson, "req_token");//请求物证值[回调的时候原封不动的传回来]

		String meituan_get_order=this.queryMeituanSysValueByName("meituan_get_order", 60*1000);

		logger.info("美团下单（预定）请求开始"+orderid);
		long start=System.currentTimeMillis();
		JSONObject returnJson=new JSONObject();
//		returnJson.put("reqtoken", reqtoken);
		try{
			String orderStatus=orderService.queryOrderStatusByOrderId(orderid);
			if(orderStatus==null){
				DBOrderInfo orderInfo=getOrderInfoFromTc(webJson,"asynchronous");
				if("0".equals(meituan_get_order)&&"00".equals(orderInfo.getWait_for_order())){
					returnJson.put("success", false);
					returnJson.put("msg","处理失败，12306系统异常");
					returnJson.put("req_token", reqtoken);
					printJson(response,returnJson.toString());
					logger.info("美团下单（预定）接口关闭,order_id:"+orderid);
					return;
				}
				orderService.addOrder(orderInfo);
				returnJson.put("success", true);
				returnJson.put("msg", "操作请求已接受");
				returnJson.put("req_token", reqtoken);
				printJson(response,returnJson.toString());
				logger.info("美团下单（预定）成功,order_id:"+orderid+",耗时:"+(System.currentTimeMillis()-start)+"ms");
				
				
				/**通知出票系统出票start*/
				String out=null;
				out=orderService.sendOutTicket(orderInfo,"book");
				/*	if(out!=null){
						break;
					}
				}*/
				DBNoticeVo notice=new DBNoticeVo();
				notice.setOrder_id(orderInfo.getOrder_id());
				notice.setCp_notify_status("SUCCESS".equals(out)?Consts.NOTICE_OVER:Consts.NOTICE_START);
				notice.setChannel(orderInfo.getChannel());
				//预订成功异步回调地址---火车票出票前代理商回调美团校验地址
//				notice.setBook_notify_url(Consts.MT_URL+"/"+getJsonValue(webJson, "partnerid")+"/ticketValidate");
//				notice.setOut_notify_url(Consts.MT_URL+"/"+getJsonValue(webJson, "partnerid")+"/ticketNotify");//出票结果异步回调地址
				notice.setBook_notify_url(Consts.MT_URL+"/106/ticketValidate");
				notice.setOut_notify_url(Consts.MT_URL+"/106/ticketNotify");//出票结果异步回调地址
				noticeService.insertNotice(notice);
				
				ElongOrderLogsVo log=new ElongOrderLogsVo();
				log.setOpt_person("meituan_app");
				log.setContent("通知出票系统_"+out);
				log.setOrder_id(orderInfo.getOrder_id());
				elongOrderService.insertElongOrderLogs(log);
			}else{
				DBOrderInfo orderInfo =orderService.queryOrderInfo(orderid);
				if(orderInfo!=null && orderInfo.getOrder_type().equals("11")&& orderInfo.getOrder_status().equals(Consts.ELONG_ORDER_MAKED)){
					logger.info("美团确定出票，订单号:"+orderInfo.getOrder_id());
					//更新reqtoken
					orderInfo.setExt_field2(orderInfo.getExt_field2().split("\\|")[0]+"|"+orderInfo.getExt_field2().split("\\|")[1]+"|"+reqtoken);//出发站编码|目的站编码|请求物证值
					orderService.updateOrderInfo(null, orderInfo);
					
					returnJson.put("success", true);
					returnJson.put("msg", "操作请求已接收");
					returnJson.put("req_token", reqtoken);
					printJson(response,returnJson.toString());
					//出票
					String confirmTrain = orderService.pay(orderid, orderInfo.getBuy_money());
					if ("SUCCESS".equals(confirmTrain)) {
						logger.info("美团-请求[App]确认出票SUCCESS,order_id:" + orderid);
						ElongOrderLogsVo logmt = new ElongOrderLogsVo();
						logmt.setOpt_person("meituan_app");
						logmt.setContent("美团确认出票[成功]");
						logmt.setOrder_id(orderid);
						elongOrderService.insertElongOrderLogs(logmt);
					} else {
						logger.info("美团-请求[App]确认出票Fail,order_id:" + orderid);
						// 确认出票失败-----重新确认出票
						String confirmTrain2 = orderService.pay(orderid, orderInfo.getBuy_money());
						if ("SUCCESS".equals(confirmTrain2)) {
							logger.info("美团-请求[App]确认出票SUCCESS,order_id:" + orderid);
							ElongOrderLogsVo logmt = new ElongOrderLogsVo();
							logmt.setOpt_person("meituan_app");
							logmt.setContent("美团确认出票[成功]");
							logmt.setOrder_id(orderid);
							elongOrderService.insertElongOrderLogs(logmt);
						} else {
							logger.info("美团-请求[App]确认出票Fail,order_id:" + orderid);
						}

					}
				}else if(orderInfo!=null && orderInfo.getOrder_type().equals("11")&&(orderInfo.getOrder_status().equals(Consts.ELONG_ORDER_DOWN)||orderInfo.getOrder_status().equals(Consts.ELONG_ORDER_ING))){
					//超时订单转先支付后占座订单
					logger.info("美团先占座订单转先支付订单，订单号:"+orderInfo.getOrder_id());
					//更新reqtoken.订单类型
					DBOrderInfo newOrderInfo = new DBOrderInfo();
					newOrderInfo.setOrder_id(orderInfo.getOrder_id());
					newOrderInfo.setExt_field2(orderInfo.getExt_field2().split("\\|")[0]+"|"+orderInfo.getExt_field2().split("\\|")[1]+"|"+reqtoken);//出发站编码|目的站编码|请求物证值
					newOrderInfo.setOrder_type("22");
					orderService.updateOrderInfo(null, newOrderInfo);
					//更新回调地址
					DBNoticeVo notice=new DBNoticeVo();
					notice.setOrder_id(orderInfo.getOrder_id());
					//更新预订成功异步回调地址---火车票出票前代理商回调美团校验地址
					notice.setBook_notify_url(Consts.MT_URL+"/106/ticketValidate");
					notice.setOut_notify_url(Consts.MT_URL+"/106/ticketNotify");//出票结果异步回调地址
					noticeService.updateNotice(notice);
					returnJson.put("success", true);
					returnJson.put("msg", "操作请求已接收");
					returnJson.put("req_token", reqtoken);
					printJson(response,returnJson.toString());
				}else if(orderInfo!=null && orderInfo.getOrder_type().equals("11")&& orderInfo.getOrder_status().equals(Consts.ELONG_ORDER_FAIL)){
					//更新reqtoken
					DBOrderInfo newOrderInfo = new DBOrderInfo();
					newOrderInfo.setOrder_id(orderInfo.getOrder_id());
					newOrderInfo.setExt_field2(orderInfo.getExt_field2().split("\\|")[0]+"|"+orderInfo.getExt_field2().split("\\|")[1]+"|"+reqtoken);//出发站编码|目的站编码|请求物证值
					newOrderInfo.setOrder_type("22");
					orderService.updateOrderInfo(null, newOrderInfo);
					//更新回调地址
					DBNoticeVo notice=new DBNoticeVo();
					notice.setOrder_id(orderInfo.getOrder_id());
					//更新预订成功异步回调地址---火车票出票前代理商回调美团校验地址
					notice.setBook_notify_url(Consts.MT_URL+"/106/ticketValidate");
					notice.setOut_notify_url(Consts.MT_URL+"/106/ticketNotify");//出票结果异步回调地址
					notice.setBook_notify_status("00");
					noticeService.updateNotice(notice);
					returnJson.put("success", true); 
					returnJson.put("msg", "操作请求已接收");
					returnJson.put("req_token", reqtoken);
					printJson(response,returnJson.toString());
				}else{
					DBOrderInfo newOrderInfo = new DBOrderInfo();
					newOrderInfo.setOrder_id(orderInfo.getOrder_id());
					newOrderInfo.setExt_field2(orderInfo.getExt_field2().split("\\|")[0]+"|"+orderInfo.getExt_field2().split("\\|")[1]+"|"+reqtoken);//出发站编码|目的站编码|请求物证值
					orderService.updateOrderInfo(null, newOrderInfo);
					returnJson.put("success", true);
					returnJson.put("msg", "重复订单");
					returnJson.put("req_token", reqtoken);
					printJson(response,returnJson.toString());
					logger.info("美团下单（预定）-重复订单,order_id:"+orderid);
				}
			}
		}catch (Exception e){
			returnJson.put("success", false);
			returnJson.put("msg","系统异常,下单失败");
			returnJson.put("req_token", reqtoken);
			printJson(response,returnJson.toString());
			logger.info("美团下单（预定）-异常,order_id:"+orderid+",msg:"+e);
			e.printStackTrace();
		}
	
	}
	
	
	/**
	 * 美团JSONObject
	 * 转化成订单信息的 ：JAVAbean
	 * */
	private DBOrderInfo getOrderInfoFromTc(JSONObject webJson,String type) {
		DBOrderInfo orderInfo=new DBOrderInfo();
		String orderid = getJsonValue(webJson, "orderid");//美团的订单号
		String reqtoken = getJsonValue(webJson, "req_token");//请求物证值[回调的时候原封不动的传回来]
		String checi = getJsonValue(webJson, "train_code");//车次
		String from_station_code = getJsonValue(webJson, "from_station_code");//出发站简码
		String from_station_name = getJsonValue(webJson, "from_station_name");//出发站名称
		String to_station_code = getJsonValue(webJson, "to_station_code");//到达站简码
		String to_station_name = getJsonValue(webJson, "to_station_name");//到达站名称
		String train_date = getJsonValue(webJson, "train_date");//乘车日期
		String callbackurl = getJsonValue(webJson, "callback_url");//锁票异步回调地址
		Boolean hasseat = webJson.getBoolean("choose_no_seat");//是否出无座票 true:不出无座票 false:允许出无座票
		
		/*if("true".equals(wait_for_order)){//12306异常是否继续等待出票 11：继续等待 00：不继续等待
			orderInfo.setWait_for_order("11");
		}else{
			orderInfo.setWait_for_order("00");
		}*/
		//12306异常是否继续等待出票 11：继续等待 00：不继续等待
		orderInfo.setWait_for_order("11");
		orderInfo.setOrder_id(orderid);
		orderInfo.setTrain_no(checi);
		orderInfo.setFrom_city(from_station_name);
		orderInfo.setFrom_station_code(from_station_code);
		orderInfo.setTo_station_code(to_station_code);
		orderInfo.setTo_city(to_station_name);
		orderInfo.setTravel_date(train_date);
		orderInfo.setOrder_name(from_station_name+"/"+to_station_name);
		orderInfo.setChannel(Consts.CHANNEL_MEITUAN);//渠道编号
		orderInfo.setOrder_status(Consts.ELONG_ORDER_DOWN);//下单成功
		orderInfo.setOrder_type("22");
		JSONArray passengersArr=JSONArray.fromObject(webJson.getString("passengers"));
		int num=passengersArr.size();
		orderInfo.setTicket_num(num+"");
		//先预定后支付不涉及价格
		double pay_money=0;
		String sysSeatType="";
		String channelSeatType="";
		List<DBPassengerInfo> passengers=new ArrayList<DBPassengerInfo>();
		
		List<DBStudentInfo> students=new ArrayList<DBStudentInfo>();
		for(int i=0;i<num;i++){
			DBPassengerInfo p=new DBPassengerInfo();
			JSONObject pJson=passengersArr.getJSONObject(i);
			p.setOrder_id(orderid);
			p.setPassengerid(pJson.getString("passengerid"));
			p.setOut_passengerid(pJson.getString("passengerid"));
			p.setCp_id(CreateIDUtil.createID("mt"));
		//	p.setCp_id(orderId+"_"+pJson.getString("passengerid"));
			p.setUser_name(pJson.getString("passenger_name"));
			p.setUser_ids(pJson.getString("certificate_no"));
			p.setElong_ids_type(pJson.getString("certificate_type"));//美团证件类型ID  1、二代身份证
			p.setIds_type(TongChengConsts.get19eIdsType(pJson.getString("certificate_type")));
			String piaotype=pJson.getString("ticket_type");//票种ID 1:成人票，2:儿童票，3:学生票，4:残军票
			p.setElong_ticket_type(piaotype);
			p.setTicket_type(TongChengConsts.get19eTicketType(piaotype));
			p.setElong_seat_type(pJson.getString("seat_type"));
			p.setSeat_type(TongChengConsts.getMtTo19eSeatType(pJson.getString("seat_type")));//美团坐席-->19e坐席
			//pJson.getString("zwname") 座位名称
			p.setPay_money(pJson.getString("price"));
			pay_money=pay_money+Double.parseDouble(pJson.getString("price"));
			sysSeatType=p.getSeat_type();//19e坐席
			passengers.add(p);
			channelSeatType=p.getElong_seat_type();
			if("3".equals(piaotype)){
				DBStudentInfo s=new DBStudentInfo();
				/*province_code string 省份编号
				school_code string 学校代号
				school_name string 学校名称
				student_no string 学号
				school_system string 学制
				enrolment_year string 入学年份
				preference_from_station_name string 优惠区间起始地名称【选填】
				preference_from_station_code string 优惠区间起始地代号
				preference_to_station_name string 优惠区间到达地名称【选填】
				preference_to_station_code string 优惠区间到达地代号*/
				s.setOrder_id(orderid);
				s.setCp_id(p.getCp_id());
				s.setProvince_name(getJsonValue(pJson,"province_name"));//省份名称
				s.setProvince_code(getJsonValue(pJson,"province_code"));//省份编号
				s.setSchool_code(getJsonValue(pJson,"school_code"));//学校代号
				s.setSchool_name(getJsonValue(pJson,"school_name"));//学校名称
				s.setStudent_no(getJsonValue(pJson,"student_no"));//学号
				s.setSchool_system(getJsonValue(pJson,"school_system"));//学制
				s.setEnter_year(getJsonValue(pJson,"enrolment_year"));//入学年份：yyyy
				s.setPreference_from_station_name(getJsonValue(pJson,"preference_from_station_name"));//优惠区间起始地名称【选填】
				s.setPreference_from_station_code(getJsonValue(pJson,"preference_from_station_code"));//优惠区间起始地代号
				s.setPreference_to_station_name(getJsonValue(pJson,"preference_to_station_name"));//优惠区间到达地名称【选填】
				s.setPreference_to_station_code(getJsonValue(pJson,"preference_to_station_code"));//优惠区间到达地代号
				s.setChannel(Consts.CHANNEL_MEITUAN);
				students.add(s);
			}
		}
		orderInfo.setPay_money(pay_money+"");//订单的支付价格
		//通知出票系统  备选坐席处理
		StringBuffer ext_field1=new StringBuffer();
		
		orderInfo.setSeat_type(sysSeatType);
		orderInfo.setElong_seat_type(channelSeatType);
		//当最低的一种座位，无票时，购买选择该座位种类， 买下的就是无座(也就说买无座的席别编码就是该车次的 最低席别的编码)，另外，当最低席别的票卖完了的时候 才可
//		ext_field1.append(sysSeatType+"#9,0");
		if(!hasseat){//允许出无座//是否出无座票 true:不出无座票 false:允许出无座票
			ext_field1.append(sysSeatType+"#9,0");
		}else{
			ext_field1.append(sysSeatType+"#无");
		}
		orderInfo.setExt_field1(ext_field1.toString());
		if("asynchronous".equals(type)){//异步 类型 
			//备选字段二 用于记录 ：请求物证值|出发站编码|目的站编码[异步时填写] 只针对美团渠道 
			orderInfo.setExt_field2(from_station_code+"|"+to_station_code+"|"+reqtoken);//出发站编码|目的站编码|请求物证值
			orderInfo.setCallbackurl(callbackurl);
		}else{//同步
			orderInfo.setExt_field2(from_station_code+"|"+to_station_code);
		}
		orderInfo.setPassengers(passengers);
		orderInfo.setStudents(students);
		orderInfo.setLock_callback_url(callbackurl);//锁票异步回调地址
		return orderInfo;
	}
	

	/**
	 * 美团取消火车票订单
	 */
	private void cancelOrderMeituan(JSONObject webJson,HttpServletRequest request, 
			HttpServletResponse response) {
		String orderid=webJson.getString("orderid");
		String biz_sign=webJson.getString("biz_sign");
		
		String sign_19e = DigestUtils.md5Hex(
				webJson.getString("partnerid") + Consts.MT_KEY + orderid
				+ DigestUtils.md5Hex(orderid + webJson.getString("partnerid")).toLowerCase())
		.toLowerCase();
		
		if(sign_19e.equals(biz_sign)){
			//更新订单状态param
			Map<String,String> param=new HashMap<String,String>();
			param.put("order_id", orderid);
			
			String orderStatus=orderService.queryOrderStatusByOrderId(orderid);
			try {
				if(orderStatus!=null){
					if(Consts.ELONG_ORDER_MAKED.equals(orderStatus)){
						logger.info("美团-请求[App]取消订单开始,order_id:"+orderid);
						//同步->出票系统 发起取消预订
						//send
						String result=orderService.cancel(orderid);
						if(result.equals("SUCCESS")){
							logger.info("App-请求[出票Sys]取消订单SUCCESS,order_id:"+orderid);
							ElongOrderLogsVo log=new ElongOrderLogsVo();
							log.setOpt_person("meituan_app");
							log.setContent("美团请求取消订单[成功]");
							log.setOrder_id(orderid);
							elongOrderService.insertElongOrderLogs(log);
							//取消火车票成功
							JSONObject json=new JSONObject();
							json.put("success", true);
//							json.put("code",100);
							json.put("errorMsg", null);
//							json.put("orderid",orderid);
							printJson(response,json.toString());
							
							/**开启取消异步通知*/
							DBNoticeVo notice=new DBNoticeVo();
							notice.setOrder_id(orderid);
							notice.setChannel(Consts.CHANNEL_MEITUAN);
							notice.setOut_notify_status(Consts.NOTICE_START);
							noticeService.updateNotice(notice);
						}else{
							JSONObject json=new JSONObject();
							json.put("success", false);
//							json.put("code",500);
							json.put("errorMsg", "处理或操作失败,请重新操作");
//							json.put("orderid",orderid);
							printJson(response,json.toString());
						}
					}else if(Consts.ELONG_ORDER_CANCELED.equals(orderStatus)){
						logger.info("美团-请求[App]取消订单SUCCESS,已经取消成功"+orderid);
						//取消火车票成功
						JSONObject json=new JSONObject();
						json.put("success", true);
//						json.put("code",100);
						json.put("errorMsg", "处理或操作成功");
//						json.put("orderid",orderid);
						printJson(response,json.toString());
					}else if(Consts.ELONG_OUT_TIME.equals(orderStatus)){
						logger.info("美团-请求[App]取消订单SUCCESS,已经超时"+orderid);
						//取消火车票成功
						ElongOrderLogsVo log=new ElongOrderLogsVo();
						log.setOpt_person("meituan_app");
						log.setContent("美团请求取消订单[成功],超时未支付默认取消成功");
						log.setOrder_id(orderid);
						elongOrderService.insertElongOrderLogs(log);
						
						JSONObject json=new JSONObject();
						json.put("success", true);
//						json.put("code",100);
						json.put("errorMsg", "处理或操作成功");
//						json.put("orderid",orderid);
						printJson(response,json.toString());
					}
					else{
						logger.info("美团-请求[App]取消订单ERROR,状态错误order_id:"+orderid+",orderStatus:"+orderStatus);
						//订单状态错误
						JSONObject json=new JSONObject();	
						json.put("success", false);
//						json.put("code",112);
						json.put("errorMsg", "订单状态不正确");
						printJson(response,json.toString());
					}
				}else{
					//false	402	订单不存在*/
					logger.info("美团-请求[App]取消订单ERROR,订单不存在order_id:"+orderid);
					JSONObject json=new JSONObject();	
					json.put("success", false);
					//json.put("code",402);
					json.put("errorMsg", "订单不存在");
					printJson(response,json.toString());
				}
			} catch (Exception e) {
				//false 系统异常
				logger.info("美团-请求[App]取消订单异常,order_id:"+orderid+",msg"+e);
				JSONObject json=new JSONObject();	
				json.put("success", false);
//				json.put("code",500);
				json.put("errorMsg", "系统异常");
				printJson(response,json.toString());
				e.printStackTrace();
			}
		}else{
			logger.info("美团-请求[App]取消订单失败,签名不正确,order_id:"+orderid+",biz_sign="+biz_sign+",sign_19e="+sign_19e);
			JSONObject json=new JSONObject();	
			json.put("success", false);
//			json.put("code",500);
			json.put("errorMsg", "签名不正确");
			printJson(response,json.toString());
		}
	} 

	/**
	 * 美团线上退票接口
	 * 可对同一订单的多张车票进行退票处理。即只要所退车票是同一订单内，就可以一次性进行退票处理
	 */
	private void returnTicketsMeituan(JSONObject webJson,HttpServletRequest request, 
			HttpServletResponse response) { 
		String orderid=getJsonValue(webJson,"orderid");
		String agent_orderid=getJsonValue(webJson,"agent_orderid");
		String order_12306_serial=getJsonValue(webJson, "order_12306_serial");//12306取票单号
//		String ordernumber=getJsonValue(webJson,"ordernumber");
		String req_token=getJsonValue(webJson,"req_token");
		String callback_url=getJsonValue(webJson,"callback_url");
		String tickets=getJsonValue(webJson,"tickets");
		String remarkAll=getJsonValue(webJson,"remark");//用于区分是否已线下退票【选填】当值为0时，表示已线下退票
		JSONArray arr=JSONArray.fromObject(tickets);
		int size=arr.size();
		/**接口同步返回json字符串*/
		JSONObject json=new JSONObject();	
//		json.put("orderid",orderid);
//		json.put("ordernumber",ordernumber);
//		json.put("req_token", req_token);
		DBOrderInfo orderInfo=orderService.queryOrderInfo(orderid);
		if(size<=0||orderInfo==null||(!Consts.ELONG_ORDER_SUCCESS.equals(orderInfo.getOrder_status()))){
			logger.info("美团-线上退票ERROR,order_id:"+orderid+"订单不存在或者状态错误");
			json.put("success", false);
			json.put("code",112);
			json.put("msg", "订单状态不正确");
			printJson(response,json.toString());
			return;
		}
		String tooltip="";
		try {
			boolean returnTicket=true;
			for(int i=0;i<size;i++){
				JSONObject jp=JSONObject.fromObject(arr.get(i));
//				String cp_id=jp.getString("ticket_no");
				String passenger_name = jp.getString("passenger_name");//乘客姓名
				String passporttypeseid = jp.getString("certificate_type");
				String passportseno = jp.getString("certificate_no");//证件号码
				Object remarkOb=jp.get("remark");//remark值为0时，表示已线下退票
				String cp_id = jp.getString("ticket_no");//票号
				String remark=remarkOb==null?"":remarkOb.toString();
				
				/*Map<String, String> pMap = new HashMap<String, String>();
				pMap.put("order_id", orderid);
				pMap.put("user_name", passenger_name);
				pMap.put("user_ids", passportseno);
				//根据订单号order_id、乘客姓名和证件号码来获取其对应的cp_id
				String cp_id = elongOrderService.queryCpid(pMap);*/
				
				logger.info("开始退票"+cp_id);
				Map<String,String> paramMap=new HashMap<String, String>();
				paramMap.put("cp_id", cp_id);
				paramMap.put("order_id", orderid);
				paramMap.put("refund_seq", CreateIDUtil.createID("MTTK"));
				paramMap.put("refund_type", "11");//线上退票 
				String status=orderService.queryRefundStatus(paramMap);//查询退款状态
				paramMap.put("refund_type", "22");//线下退款
				String offLineStatus=orderService.queryRefundStatus(paramMap);//查询退款状态
				paramMap.put("refund_type", "55");//改签退票
				String changeStatus=orderService.queryRefundStatus(paramMap);//查询退款状态
				
				
				if(offLineStatus==null&&status==null&&changeStatus==null&&!"0".equals(remark)){//线上退款
					String buy_money_str=elongOrderService.queryCpPayMoney(paramMap);
					String from_time ="";
					if(buy_money_str==null){
						//改签退票
						Map<String,Object> changeParam=new HashMap<String, Object>();
						changeParam.put("new_cp_id", cp_id);
						changeParam.put("order_id", orderid);
						Map<String,Object> changeCpInfo= changeService.queryChangeCpInfo(changeParam);
						if(changeCpInfo ==null ||changeCpInfo.get("is_changed").equals("N")){
							logger.info("美团-线上退票ERROR,cp_id:"+cp_id+"不存在");
							json.put("success", false);
							json.put("code",112);
							json.put("msg", "车票不存在");
							printJson(response,json.toString());
							return;
						}
						buy_money_str=changeCpInfo.get("change_buy_money").toString();
						paramMap.put("refund_type", "55");//改签退票
						from_time=changeCpInfo.get("change_from_time").toString();//改签后出发时间
						String from_15d = DateUtil.dateAddDaysFmt3(from_time,"-15");
						if(new Date().before(DateUtil.stringToDate(from_15d, "yyyy-MM-dd HH:mm:ss"))){
							//如果改签票在15天之外，查询原票时间
							from_time= orderInfo.getFrom_time()+":00";
						}
						logger.info("改签退票开始,buy_money_str:"+buy_money_str+",from_time:"+from_time);
					}else{
						paramMap.put("refund_type", "11");//线上退票
						from_time= orderInfo.getFrom_time()+":00";
					}
					/**插入新退款记录*/
					String refund_money = "0";
					//String start_time = orderInfo.getFrom_time()+":00";
					//start_time = DateUtil.dateToString(DateUtil.stringToDate(start_time,DateUtil.DATE_FMT1),DateUtil.DATE_FMT1);
					String from_15d = DateUtil.dateAddDaysFmt3(from_time,"-15");
					String from_24 = DateUtil.dateAddDaysFmt3(from_time,"-1");
			        String from_48 = DateUtil.dateAddDaysFmt3(from_time,"-2");
			        double feePercent = 0;
			        if(new Date().before(DateUtil.stringToDate(from_15d, "yyyy-MM-dd HH:mm:ss"))){
			             feePercent = 0.0;
			        }else if(new Date().before(DateUtil.stringToDate(from_48, "yyyy-MM-dd HH:mm:ss"))){
			             feePercent = 0.05;
			        }else if(new Date().before(DateUtil.stringToDate(from_24, "yyyy-MM-dd HH:mm:ss"))){
			             feePercent = 0.1;
			        }else{
			             feePercent = 0.2;
			        }
					double total_refund_money = 0;
					
					
					double buy_money = Double.parseDouble(buy_money_str);
					double sxf = 0.0;
			        if(feePercent!=0){
			              sxf = AmountUtil.quarterConvert(AmountUtil.mul(buy_money, feePercent));//手续费
			              sxf = sxf > 2 ? sxf : 2;
			        }
					total_refund_money += AmountUtil.ceil(AmountUtil.sub(buy_money, sxf));
					refund_money = String.valueOf(total_refund_money);
					if(buy_money<=2&&feePercent!=0){
						refund_money=String.valueOf(0);
						//退票状态直接置于退款成功状态
						paramMap.put("refund_status", "11");
					}
					paramMap.put("refund_status", "00");//00：等待机器改签
					paramMap.put("refund_money", refund_money);
					paramMap.put("channel", "meituan");
					paramMap.put("reqtoken", req_token);
					paramMap.put("callbackurl", callback_url);
					orderService.insertRefundOrder(paramMap);
					
					ElongOrderLogsVo log=new ElongOrderLogsVo();
					log.setOpt_person("meituan_app");
//					log.setContent("美团申请"+(isChangeRefund?"改签退票":"线上退票")+"_成功["+paramMap.get("cp_id")+"]"+("11".equals(paramMap.get("refund_status"))?"2元及2元以下,直接置于退票成功":""));
					log.setContent("美团申请线上退票_成功["+paramMap.get("cp_id")+"]"+("11".equals(paramMap.get("refund_status"))?"2元及2元以下,直接置于退票成功":""));
					log.setOrder_id(paramMap.get("order_id"));
					elongOrderService.insertElongOrderLogs(log);
				}else if((offLineStatus==null||"22".equals(offLineStatus))&&(status==null||"22".equals(status))&&"0".equals(remark)){//线下退票
					paramMap.put("refund_type", "44");
					
					paramMap.put("refund_status", "73");//用户生成线下退款(接口推送)
					paramMap.put("channel", Consts.CHANNEL_MEITUAN);
					paramMap.put("reqtoken", req_token);
					paramMap.put("callbackurl", callback_url);
					orderService.insertRefundOrder(paramMap);
					
					ElongOrderLogsVo log=new ElongOrderLogsVo();
					log.setOpt_person("meituan_app");
					log.setContent("美团申请线下退款_成功["+paramMap.get("cp_id")+"]");
					log.setOrder_id(paramMap.get("order_id"));
					elongOrderService.insertElongOrderLogs(log);
				}else{
					tooltip=tooltip+cp_id+",";
					returnTicket=false;
				}
			}
			if(returnTicket){
				json.put("success", true);
				json.put("code", 100);
				json.put("msg", "处理或操作成功");
				printJson(response,json.toString());
			}else{
				json.put("success", false);
				json.put("code", 802);
				json.put("msg", "操作请求已接受,"+tooltip+"已经申请线下退票");
				printJson(response,json.toString());
			}
		} catch (Exception e) {
			logger.info("美团线上退票异常"+e);
			e.printStackTrace();
			json.put("success", false);
			json.put("code", 111);
			json.put("msg", "处理失败");
			printJson(response,json.toString());
		}
	} 
	
	
	/**
	 * 美团车次信息查询---停经站
	 */
	private void queryTrainInfoMeituan(JSONObject webJson, HttpServletRequest request, HttpServletResponse response) {
		String train_date = getJsonValue(webJson, "train_date");//乘车日期yyyy-MM-dd
		String from_station = getJsonValue(webJson, "from_station");//出发站简码
		String to_station = getJsonValue(webJson, "to_station");//到达站简码
//		String train_no = getJsonValue(webJson, "train_no");//官方系统车次内部编码
		String train_code = getJsonValue(webJson, "train_code");//车次号
		
		JSONObject json=new JSONObject();
		json.put("success", true);
		json.put("code", 100);
		json.put("msg", "车次查询成功");
		/*业务参数检查*/
		if("".equals(train_date) || "".equals(from_station) || 
				"".equals(to_station) || "".equals(train_code)) {
			logger.info("业务参数缺失" + webJson);
			json.put("success", false);
			json.put("code", 107);
			json.put("msg", "业务参数缺失");
			printJson(response, json.toString());
			return;
		}
		
		/*调用内部系统接口*/
		String url = Consts.QUERY_TRAIN_INFO;//trainRobotInterface -->/tcQueryTrainInfo
		
		try {
			/*参数*/
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("train_no", "");
			paramMap.put("from_station", from_station);
			paramMap.put("to_station", to_station);
			paramMap.put("train_date", train_date);
			paramMap.put("train_code", train_code);
			paramMap.put("channel", Consts.CHANNEL_TONGCHENG);//用tongcheng渠道的查询信息
			
			/*请求robot分发接口*/
			String params;
			params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
			logger.info("美团发起车次查询url:" + url + ",param:"+paramMap.toString());
			String result = HttpUtil.sendByPost(url, params, "UTF-8");
			logger.info("美团发起车次查询result:"+result);
			
			if(StringUtils.isNotEmpty(result) && result != null){
				if("NO_DATAS".equals(result)) {
					/*查询无数据*/
					logger.info("美团车次查询，没有符合条件的车次信息");
					json.put("success", false);
					json.put("code", 201);
					json.put("msg", "没有符合条件的车次信息");
					printJson(response, json.toString());
					return;
				} else if("ERROR".equals(result)) {
					/*查询失败*/
					logger.info("美团车次查询，查询失败");
					json.put("success", false);
					json.put("code", 202);
					json.put("msg", "查询失败");
					printJson(response, json.toString());
					return;
				}
				/*业务输出参数*/
				ObjectMapper mapper = new ObjectMapper();
				JsonNode trainInfoData = mapper.readTree(result);
				String train_no = trainInfoData.path("train_no").getTextValue();
				
				JsonNode train_info_list = trainInfoData.path("train_info_list");
				JSONArray data = new JSONArray();
				
				int diffDay = 0;//列车从出发站到达目的站的运行天数
				String lastTime = "00:00";
				boolean isEnabled = false;
				if(train_info_list.size() > 0) {
					for(int i = 0; i < train_info_list.size(); i++) {
						JsonNode train_info = train_info_list.get(i);
						JSONObject j = new JSONObject();
						String arrive_time = train_info.path("arrive_time").getTextValue();
						//isEnabled = train_info.path("isEnabled").getBooleanValue();
						if(i == 0) {
							/*第一个节点中包含额外信息*/
							json.put("from_station_name", train_info.path("start_station_name").getTextValue());//始发站站名
							json.put("to_station_name", train_info.path("end_station_name").getTextValue());//终到站站名
						} else {
							//if(isEnabled && arrive_time.compareTo(lastTime) < 0) {
							if(arrive_time.compareTo(lastTime) < 0) {
								diffDay++;
							}
//							lastTime = arrive_time;
						}
						lastTime = train_info.path("start_time").getTextValue();
						j.put("arrive_days", diffDay);
						j.put("station_no", train_info.path("station_no").getTextValue());
						j.put("station_name", train_info.path("station_name").getTextValue());
						j.put("arrive_time", arrive_time);
						j.put("start_time", train_info.path("start_time").getTextValue());
						j.put("wait_time", train_info.path("stopover_time").getTextValue());//经停时间(分钟),如“2 分钟”
						data.add(j);
						
					}
					String trainType = TongChengConsts.getTongChengTrainType(train_code);
					json.put("train_no", train_no);//12306车次内部编号
					json.put("train_code", train_code);//车次号
					json.put("train_type", trainType);//列车类型
					json.put("way_station_info", data);//途径站信息
					printJson(response, json.toString());
				} else {
					logger.info("美团车次查询，没有符合条件的车次信息");
					json.put("success", false);
					json.put("code", 201);
					json.put("msg", "没有符合条件的车次信息");
					printJson(response, json.toString());
				}
			}else{
				logger.info("美团车次查询，没有符合条件的车次信息");
				json.put("success", false);
				json.put("code", 201);
				json.put("msg", "没有符合条件的车次信息");
				printJson(response, json.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("美团发起车次查询,系统异常"+e);
			json.put("success", false);
			json.put("code", 202);
			json.put("msg", "查询失败");
			printJson(response,json.toString());
			e.printStackTrace();
		}
		
	}
	
	
	
	/**
	 * 代理商锁定美团订单$PATH/{ partnerid }/lockOrder
	 */
	private void lockOrderMeituan(JSONObject webJson,HttpServletRequest request, 
			HttpServletResponse response) {
		String orderid=webJson.getString("orderid");
		String biz_sign = DigestUtils.md5Hex(webJson.getString("partnerid") + Consts.MT_KEY + orderid
				+ DigestUtils.md5Hex(orderid + webJson.getString("partnerid")).toLowerCase()).toLowerCase();
		
//		Map<String, String> paramMap = new HashMap<String, String>();
//		paramMap.put("orderid", orderid);
//		paramMap.put("biz_sign", biz_sign);
		String params;
		try {
//			params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
////			String result = HttpUtil.sendByPost(Consts.MT_URL+"/"+webJson.getString("partnerid")+"/lockOrder", params, "UTF-8");
//			String result = HttpUtil.sendByPost(Consts.MT_URL+"/106/lockOrder", params, "UTF-8");
			
			JSONObject jsonStr = new JSONObject();
			jsonStr.put("orderid", orderid);
			jsonStr.put("biz_sign", biz_sign);
			//post请求，传参用json格式
			String result = HttpPostJsonUtil.sendJsonPost(Consts.MT_URL+"/106/lockOrder", jsonStr.toString(), "utf-8");
			logger.info("代理商锁定美团订单orderid:" + orderid + "请求返回结果:" + result);
			JSONObject json = JSONObject.fromObject(result);
			if(json.getBoolean("success")){//是否接收成功
				logger.info("代理商锁定美团订单orderid:" + orderid + "成功");
			}else{
				logger.info("代理商锁定美团订单orderid:" + orderid + "失败" + ",errorMsg" + json.get("errorMsg"));
			}
		} catch (Exception e) {
			logger.info("代理商锁定美团订单系统orderid:" + orderid + "异常" + e);
			e.printStackTrace();
		}
	} 
	
	private String getJsonValue(JSONObject json,String name){
		return json.get(name)==null?"":json.getString(name);
		
	}
	
	private DBPassengerInfo getCp(String cp_id, DBOrderInfo orderInfo) {
		for(DBPassengerInfo p : orderInfo.getPassengers()) {
			String cpId = p.getCp_id();
			if(cpId.equals(cp_id)) {
				return p;
			}
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		/*Date now=new Date();
		int hours=now.getHours();
		System.out.println(hours);*/
		String ss="V111";
		String seat_type="O";
		if("M".equals(seat_type)||"O".equals(seat_type)){
			String train_no=ss.toUpperCase();
			System.out.println(train_no);
			if(!train_no.contains("C")&&!train_no.contains("D")&&!train_no.contains("G")){
				
				System.out.println("M".equals(seat_type)?"7":"8");;
			}else{
				System.out.println(seat_type);
			}
			
		}else{
			System.out.println(seat_type);
		}
	}
	/** 
	 * 美团请求改签
	 */
	private void resignTicketMeituan(JSONObject webJson,HttpServletRequest request, HttpServletResponse response) {
		JSONObject result = changeService.addChange(webJson);
		printJson(response, result.toString());
	}
	/**
	 * 美团取消改签
	 */
	private void cancelResignMeituan(JSONObject webJson,HttpServletRequest request, 
			HttpServletResponse response) {
		JSONObject result = changeService.cancelChange(webJson);
		printJson(response, result.toString());
	}
	/**
	 * 美团确认改签
	 */
	private void confirmResignMeituan(JSONObject webJson,HttpServletRequest request, 
			HttpServletResponse response) {
		JSONObject result = changeService.confirmChange(webJson);
		printJson(response, result.toString());
	}
	/**
	 * 占座
	 * @param webJson
	 * @param request
	 * @param response
	 */
	private void holdseat(JSONObject webJson,HttpServletRequest request, HttpServletResponse response){
		String orderid = getJsonValue(webJson, "orderid");//美团的订单号
		String reqtoken = getJsonValue(webJson, "req_token");//请求物证值[回调的时候原封不动的传回来]

		String meituan_get_order=this.queryMeituanSysValueByName("meituan_get_order", 60*1000);

		logger.info("美团占座请求开始"+orderid);
		long start=System.currentTimeMillis();
		JSONObject returnJson=new JSONObject();
		try{
			//订单去重
			String orderStatus=orderService.queryOrderStatusByOrderId(orderid);
			if(orderStatus==null){
				DBOrderInfo orderInfo=getHoldOrderInfo(webJson,"asynchronous");
				if("0".equals(meituan_get_order)&&"00".equals(orderInfo.getWait_for_order())){
					returnJson.put("success", false);
					returnJson.put("msg","处理失败，12306系统异常");
					returnJson.put("code", 000);
					printJson(response,returnJson.toString());
					logger.info("美团占座接口关闭,order_id:"+orderid);
					return;
				}
				orderService.addOrder(orderInfo);
				returnJson.put("success", true);
				returnJson.put("msg", "操作请求已接受");
				returnJson.put("code", 100);
				printJson(response,returnJson.toString());
				logger.info("美团占座成功,order_id:"+orderid+",耗时:"+(System.currentTimeMillis()-start)+"ms");
				
				
				/**通知出票系统出票start*/
				String out=null;
				out=orderService.sendOutTicket(orderInfo,"book");
				DBNoticeVo notice=new DBNoticeVo();
				notice.setOrder_id(orderInfo.getOrder_id());
				notice.setCp_notify_status("SUCCESS".equals(out)?Consts.NOTICE_OVER:Consts.NOTICE_START);
				notice.setChannel(orderInfo.getChannel());
				//预订成功异步回调地址---火车票出票前代理商回调美团校验地址
				notice.setBook_notify_url(Consts.MT_URL+"/106/holdSeatNotify");
				notice.setOut_notify_url(Consts.MT_URL+"/106/ticketNotify");//出票结果异步回调地址
				noticeService.insertNotice(notice);
				
				ElongOrderLogsVo log=new ElongOrderLogsVo();
				log.setOpt_person("meituan_app");
				log.setContent("通知出票系统_"+out);
				log.setOrder_id(orderInfo.getOrder_id());
				elongOrderService.insertElongOrderLogs(log);
			}else{
				returnJson.put("success", true);
				returnJson.put("msg", "操作请求已接受");
				returnJson.put("code", 100);
				printJson(response,returnJson.toString());
				logger.info("美团占座-重复订单成功,order_id:"+orderid);
			}
		}catch (Exception e){
			returnJson.put("success", false);
			returnJson.put("msg","系统异常,下单失败");
			returnJson.put("code", 000);
			printJson(response,returnJson.toString());
			logger.info("美团占座-异常,order_id:"+orderid+",msg:"+e);
			e.printStackTrace();
		}
	
	}
	/**
	 * 取消占座
	 * @param webJson
	 * @param request
	 * @param response
	 */
	private void cancelHoldSeat(JSONObject webJson,HttpServletRequest request, HttpServletResponse response){
		Long orderId = webJson.getLong("orderId");//美团的订单号
		String orderId12306 = getJsonValue(webJson, "orderId12306");//12306订单号
		logger.info("美团取消占座请求开始"+orderId);
		JSONObject returnJson=new JSONObject();
		try{
			if(orderId==null ){
				returnJson.put("success", false);
				returnJson.put("msg","参数异常");
				returnJson.put("code", 108);
				printJson(response,returnJson.toString());
				logger.info("美团取消占座-参数异常,order_id:"+orderId);
			}
			//查询订单
			DBOrderInfo orderInfo=orderService.queryOrderInfo(orderId.toString());
			if(orderInfo==null){
				returnJson.put("success", false);
				returnJson.put("msg","参数异常");
				returnJson.put("code", 108);
				printJson(response,returnJson.toString());
				logger.info("美团取消占座-参数异常,order_id:"+orderId);
			}else if(!orderInfo.getOrder_status().equals(Consts.ELONG_ORDER_MAKED)){
				returnJson.put("success", false);
				returnJson.put("msg","订单状态不是预定成功，不可申请取消");
				returnJson.put("code", 000);
				printJson(response,returnJson.toString());
				logger.info("美团取消占座-订单状态不是预定成功，不可申请取消,order_id:"+orderId);
			}else{
				returnJson.put("success", true);
				returnJson.put("msg","取消订单接收成功");
				returnJson.put("code", 100);
				printJson(response,returnJson.toString());
				//取消订单
				DBNoticeVo noticeInfo = new DBNoticeVo();
				noticeInfo.setOrder_id(orderId.toString());
				noticeInfo.setOut_notify_status(Consts.NOTICE_START);//取消订单默认设置出票通知开始
				ElongOrderLogsVo log = new ElongOrderLogsVo(orderId.toString(), "美团申请取消订单", "meituan_app");
				elongOrderService.insertElongOrderLogs(log);
				//取消订单
				String cancelResult = orderService.cancel(orderId.toString());
				
				if ("SUCCESS".equals(cancelResult)) {
					logger.info("美团-取消订单SUCCESS,order_id:" + orderId);
					ElongOrderLogsVo logmt = new ElongOrderLogsVo();
					logmt.setOpt_person("meituan_app");
					logmt.setContent("美团取消订单[成功]");
					logmt.setOrder_id(orderId.toString());
					elongOrderService.insertElongOrderLogs(logmt);
				} else {
					logger.info("1美团-请求[App]取消订单Fail,order_id:" + orderId);
					//取消订单失败--重新取消订单
					String cancelResult2 = orderService.cancel(orderId.toString());
					if ("SUCCESS".equals(cancelResult2)) {
						logger.info("美团-请求[App]取消订单SUCCESS,order_id:" + orderId);
						ElongOrderLogsVo logmt = new ElongOrderLogsVo();
						logmt.setOpt_person("meituan_app");
						logmt.setContent("美团取消订单[成功]");
						logmt.setOrder_id(orderId.toString());
						elongOrderService.insertElongOrderLogs(logmt);
					} else {
						logger.info("美团-请求取消订单Fail,order_id:" + orderId);
					}
				}
				
				
			}
		}catch (Exception e){
			returnJson.put("success", false);
			returnJson.put("msg","系统异常,下单失败");
			returnJson.put("code", 000);
			printJson(response,returnJson.toString());
			logger.info("美团取消占座-异常,order_id:"+orderId+",msg:"+e);
			e.printStackTrace();
		}
	
	}
	/**
	 * 美团JSONObject
	 * 转化成订单信息的 ：JAVAbean
	 * */
	private DBOrderInfo getHoldOrderInfo(JSONObject webJson,String type) {
		DBOrderInfo orderInfo=new DBOrderInfo();
		String orderid = getJsonValue(webJson,"orderid");//美团的订单号
		String reqtoken = getJsonValue(webJson, "req_token");//请求物证值[回调的时候原封不动的传回来]
		String checi = getJsonValue(webJson, "train_code");//车次
		String from_station_code = getJsonValue(webJson, "from_station_code");//出发站简码
		String from_station_name = getJsonValue(webJson, "from_station_name");//出发站名称
		String to_station_code = getJsonValue(webJson, "to_station_code");//到达站简码
		String to_station_name = getJsonValue(webJson, "to_station_name");//到达站名称
		String train_date = getJsonValue(webJson, "start_time");//发车时间
		String callbackurl = getJsonValue(webJson, "callback_url");//锁票异步回调地址
		
		//12306异常是否继续等待出票 11：继续等待 00：不继续等待
		orderInfo.setWait_for_order("11");
		orderInfo.setOrder_id(orderid);
		orderInfo.setTrain_no(checi);
		orderInfo.setFrom_city(from_station_name);
		orderInfo.setFrom_station_code(from_station_code);
		orderInfo.setTo_station_code(to_station_code);
		orderInfo.setTo_city(to_station_name);
		orderInfo.setTravel_date(train_date.split(" ")[0]);
		orderInfo.setFrom_time(train_date);
		orderInfo.setOrder_name(from_station_name+"/"+to_station_name);
		orderInfo.setChannel(Consts.CHANNEL_MEITUAN);//渠道编号
		orderInfo.setOrder_status(Consts.ELONG_ORDER_DOWN);//下单成功
		orderInfo.setOrder_type("11");
		JSONArray passengersArr=JSONArray.fromObject(webJson.getString("passengers"));
		int num=passengersArr.size();
		orderInfo.setTicket_num(num+"");
		//先预定后支付不涉及价格
		double pay_money=0;
		String sysSeatType="";
		String channelSeatType="";
		List<DBPassengerInfo> passengers=new ArrayList<DBPassengerInfo>();
		
		List<DBStudentInfo> students=new ArrayList<DBStudentInfo>();
		for(int i=0;i<num;i++){
			DBPassengerInfo p=new DBPassengerInfo();
			JSONObject pJson=passengersArr.getJSONObject(i);
			p.setOrder_id(orderid);
			p.setCp_id(pJson.getString("ticket_id"));
			p.setOut_passengerid(pJson.getString("ticket_id"));
			p.setCp_id(CreateIDUtil.createID("mt"));
			p.setUser_name(pJson.getString("passenger_name"));
			p.setUser_ids(pJson.getString("certificate_no"));
			p.setElong_ids_type(pJson.getString("certificate_type"));//美团证件类型ID  1、二代身份证
			p.setIds_type(TongChengConsts.get19eIdsType(pJson.getString("certificate_type")));
			String piaotype=pJson.getString("ticket_type");//票种ID 1:成人票，2:儿童票，3:学生票，4:残军票
			p.setElong_ticket_type(piaotype);
			p.setTicket_type(TongChengConsts.get19eTicketType(piaotype));
			p.setElong_seat_type(pJson.getString("seat_type"));
			p.setSeat_type(TongChengConsts.getMtTo19eSeatType(pJson.getString("seat_type")));//美团坐席-->19e坐席
			p.setPay_money(pJson.getString("price"));
			pay_money=pay_money+Double.parseDouble(pJson.getString("price"));
			sysSeatType=p.getSeat_type();//19e坐席
			passengers.add(p);
			channelSeatType=p.getElong_seat_type();
			if("3".equals(piaotype)){
				DBStudentInfo s=new DBStudentInfo();
				/*province_code string 省份编号
				school_code string 学校代号
				school_name string 学校名称
				student_no string 学号
				school_system string 学制
				enrolment_year string 入学年份
				preference_from_station_name string 优惠区间起始地名称【选填】
				preference_from_station_code string 优惠区间起始地代号
				preference_to_station_name string 优惠区间到达地名称【选填】
				preference_to_station_code string 优惠区间到达地代号*/
				s.setOrder_id(orderid);
				s.setCp_id(p.getCp_id());
				s.setProvince_name(getJsonValue(pJson,"province_name"));//省份名称
				s.setProvince_code(getJsonValue(pJson,"province_code"));//省份编号
				s.setSchool_code(getJsonValue(pJson,"school_code"));//学校代号
				s.setSchool_name(getJsonValue(pJson,"school_name"));//学校名称
				s.setStudent_no(getJsonValue(pJson,"student_no"));//学号
				s.setSchool_system(getJsonValue(pJson,"school_system"));//学制
				s.setEnter_year(getJsonValue(pJson,"enrolment_year"));//入学年份：yyyy
				s.setPreference_from_station_name(getJsonValue(pJson,"preference_from_station_name"));//优惠区间起始地名称【选填】
				s.setPreference_from_station_code(getJsonValue(pJson,"preference_from_station_code"));//优惠区间起始地代号
				s.setPreference_to_station_name(getJsonValue(pJson,"preference_to_station_name"));//优惠区间到达地名称【选填】
				s.setPreference_to_station_code(getJsonValue(pJson,"preference_to_station_code"));//优惠区间到达地代号
				s.setChannel(Consts.CHANNEL_MEITUAN);
				students.add(s);
			}
		}
		orderInfo.setPay_money(pay_money+"");//订单的支付价格
		//通知出票系统  备选坐席处理
		StringBuffer ext_field1=new StringBuffer();
		
		orderInfo.setSeat_type(sysSeatType);
		orderInfo.setElong_seat_type(channelSeatType);
		ext_field1.append(sysSeatType+"#无");
		/*if(!hasseat){//允许出无座//是否出无座票 true:不出无座票 false:允许出无座票
			ext_field1.append(sysSeatType+"#9,0");
		}else{
			ext_field1.append(sysSeatType+"#无");
		}*/
		orderInfo.setExt_field1(ext_field1.toString());
		if("asynchronous".equals(type)){//异步 类型 
			//备选字段二 用于记录 ：请求物证值|出发站编码|目的站编码[异步时填写] 只针对美团渠道 
			orderInfo.setExt_field2(from_station_code+"|"+to_station_code+"|"+reqtoken);//出发站编码|目的站编码|请求物证值
			orderInfo.setCallbackurl(callbackurl);
		}else{//同步
			orderInfo.setExt_field2(from_station_code+"|"+to_station_code);
		}
		orderInfo.setPassengers(passengers);
		orderInfo.setStudents(students);
		orderInfo.setLock_callback_url(callbackurl);//锁票异步回调地址
		return orderInfo;
	}
	
}
