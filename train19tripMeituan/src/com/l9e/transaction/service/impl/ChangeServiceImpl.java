package com.l9e.transaction.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.l9e.common.TongChengConsts;
import com.l9e.transaction.dao.ChangeDao;
import com.l9e.transaction.dao.CommonDao;
import com.l9e.transaction.dao.OrderDao;
import com.l9e.transaction.dao.RefundDao;
import com.l9e.transaction.service.ChangeService;
import com.l9e.transaction.vo.ChangeInfo;
import com.l9e.transaction.vo.ChangeLog;
import com.l9e.transaction.vo.ChangePassengerInfo;
import com.l9e.transaction.vo.DBOrderInfo;
import com.l9e.transaction.vo.DBPassengerInfo;
import com.l9e.transaction.vo.Station;
import com.l9e.util.AmountUtil;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpPostJsonUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.StrUtil;
import com.l9e.util.UrlFormatUtil;
@Service("changeService")
public class ChangeServiceImpl implements  ChangeService{
	private static final Logger logger = Logger.getLogger(ChangeServiceImpl.class);
	@Resource
	private ChangeDao changeDao;
	@Resource
	private OrderDao orderDao;
	@Resource
	private RefundDao refundDao;
	@Resource
	private CommonDao commonDao;

	@Override
	public JSONObject addChange(JSONObject webJson){
		JSONObject result = new JSONObject();
		
		//改签日志
		ChangeLog log=new ChangeLog();
		logger.info("美团请求改签");
		Long resignIdL = webJson.getLong("resignId");//改签id
		Long orderIdL = webJson.getLong("orderId");//美团订单号
		//String orderType = webJson.getString("orderType");//下单类型
		String trainCode = webJson.getString("trainCode");//车次号
		String trainDate = webJson.getString("trainDate");//发车时间
		String fromStationCode = webJson.getString("fromStationCode");//出发站code
		String fromStationName = webJson.getString("fromStationName");//出发站名称
		String toStationCode = webJson.getString("toStationCode");//到达站code
		String toStationName = webJson.getString("toStationName");//到达站名称
		String callBackURL = webJson.getString("callBackURL");//回调地址
		String reqToken = webJson.getString("reqToken");//鉴权字段
		Boolean chooseNoSeat = webJson.getBoolean("chooseNoSeat");//是否接受无座
		String method = webJson.getString("method");//操作功能名
		String reqtime = webJson.getString("reqtime");//请求时间
		String partnerid = webJson.getString("partnerid");//调用方识别
		String sign = webJson.getString("sign");//数字签名
		Integer toStationChange = webJson.getInt("toStationChange");
		JSONArray tickets = webJson.getJSONArray("tickets");//车票信息
		logger.info("tickets ：          "+tickets);
		
		try{
			//通用参数检查
			if (StrUtil.isEmpty(partnerid) || StrUtil.isEmpty(reqtime)
					|| StrUtil.isEmpty(sign)) {
				logger.info("美团-通用参数缺失:partnerid," + partnerid + "|reqtime,"
						+ reqtime + "|sign," + sign);
				result.put("success", false);
				result.put("code", 103);
				result.put("msg", "通用参数缺失");
				return result;
			}
			/*业务参数检查*/
			if("".equals(resignIdL) || "".equals(orderIdL)|| "".equals(trainCode) || "".equals(trainDate) || 
					"".equals(fromStationCode) || "".equals(toStationCode) || "".equals(callBackURL) 
					|| "".equals(reqToken)|| "".equals(chooseNoSeat)|| "".equals(method) ||"".equals(reqtime)||"".equals(tickets)||tickets.size() == 0) {
				logger.info("美团请求改签 ERROR,参数有空order_id:"+orderIdL);
				result.put("success", false);
				result.put("code",108);
				result.put("msg", "错误的业务参数");
				return result;
			}
			//检查订单
			String resignId = resignIdL.toString();
			String orderId = orderIdL.toString();
			DBOrderInfo orderInfo = orderDao.queryOrderInfo(orderId);
			if(orderInfo==null){
				logger.info("美团改签，订单不存在，orderId"+orderId);
				result.put("success", false);
				result.put("code",112);
				result.put("msg", "订单不存在");
				return result;
			}else if(!orderInfo.getOrder_status().equals(Consts.ELONG_ORDER_SUCCESS)){
				logger.info("美团改签，订单状态不正确，orderId"+orderId);
				result.put("success", false);
				result.put("code",112);
				result.put("msg", "订单状态不正确");
				return result;
			}
			
			/*查询该订单号下的改签特征值，排除重复请求*/
			Map<String,Object> changeParam = new HashMap<String,Object>();
			changeParam.put("mt_change_id", resignId);
			ChangeInfo changeInfo = changeDao.selectChangeInfo(changeParam);
			if(changeInfo!=null){
				logger.info("美团请求改签，该请求已存在，mt_change_id为"+resignId);
				result.put("success", false);
				result.put("code",111);
				result.put("msg", "该请求已存在");
				return result;
			}
			
			/*改签时间验证*/
			if(DateUtil.minuteDiff(DateUtil.stringToDate(orderInfo.getFrom_time(), DateUtil.DATE_FMT3), new Date()) < 30) {
				/*距离发车时间小于30分*/
				logger.info("美团请求改签ERROR,距离开车时间太近无法改签，orderId"+orderId);
				result.put("success", false);
				result.put("code",111);
				result.put("msg", "美团请求改签,距离开车时间太近无法改签");
				return result;
			}
			/*批量改签*/
			if(tickets.size() > 1) {
				for(int i = 0; i < tickets.size(); i++){     
					String orginSeatType = orderInfo.getSeat_type();
					JSONObject ticket = tickets.getJSONObject(i);
				    String seatType = ticket.getString("seatType");
				    if(seatType.equals("2")||seatType.equals("3")||seatType.equals("4")||seatType.equals("6")||seatType.equals("7")||
				    		seatType.equals("8")||seatType.equals("15")||seatType.equals("16")||seatType.equals("20")||seatType.equals("21")||seatType.equals("22")
				    	||orginSeatType.contains("4")||orginSeatType.contains("5")||orginSeatType.contains("6")){
				    	logger.info("美团-请求改签ERROR,批量改签原票或新票坐席不能为卧铺");
						result.put("success", false);
						result.put("code",111);
						result.put("msg", "批量改签原票或新票坐席不能为卧铺");
						return result;
				    }
				}
			} 
			/*组装改签车票信息*/
			changeInfo = new ChangeInfo();
			List<ChangePassengerInfo> changePassengers = new ArrayList<ChangePassengerInfo>();
			for(int i = 0; i < tickets.size(); i++) {
				/*传入的参数数据*/
				JSONObject ticket = tickets.getJSONObject(i);
				//改签车票信息
				ChangePassengerInfo cp = new ChangePassengerInfo();
				Map<String, Object> changeCpParam = new HashMap<String, Object>();
				changeCpParam.put("cp_id", ticket.getString("ticketNo"));
				List<ChangePassengerInfo> cps = changeDao.selectChangeCp(changeCpParam);
				if(cps !=  null) {
					for(ChangePassengerInfo changeCp:cps){
						/*每张车票只能改签一次*/
						if(changeCp.getIs_changed().equals("Y")){
							logger.info("美团-请求改签ERROR,车票已改签过,车票id:" + changeCp.getCp_id());
							result.put("success", false);
							result.put("code",111);
							result.put("msg", "美团-请求改签ERROR,车票已改签过,车票id:" + changeCp.getCp_id());
							return result;						}
					}
				} 
				String seatType = ticket.getString("seatType");
				cp.setOrder_id(orderId);//订单id
				cp.setCp_id(ticket.getString("ticketNo"));//车票id(原票)
				cp.setNew_cp_id(CreateIDUtil.createID("mt"));//改签后车票id
				cp.setChange_seat_type(TongChengConsts.getMtTo19eSeatType(seatType));//19e改签后新座位席别
				cp.setSeat_type(orderInfo.getSeat_type());//19e原座位类型
				cp.setMt_change_seat_type(seatType.toString());//美团改签后座位类型
				cp.setIs_changed("N");
				
				/*原票信息*/
				Map<String, Object> p  = orderDao.queryOutTicketCpInfo(ticket.getString("ticketNo"));
				if(p != null) {
					String buy_money = (String) p.get("buy_money");
					cp.setBuy_money(Double.parseDouble(buy_money));//成本价格
					cp.setSeat_no((String) p.get("seat_no"));//座位号
					cp.setSeat_type(((Integer) p.get("seat_type")).toString());//座位席别
					cp.setTrain_box((String) p.get("train_box"));//车厢
					cp.setTicket_type(((Integer) p.get("ticket_type")).toString());//车票类型
					cp.setIds_type(((Integer) p.get("cert_type")).toString());//证件类型
					cp.setUser_ids((String) p.get("cert_no"));//证件号码
					cp.setUser_name((String) p.get("user_name"));//乘客姓名
				}
				changePassengers.add(cp);
			}
			/*组装改签记录信息*/
			changeInfo.setChange_travel_time(trainDate.split(" ")[0]);//改签后乘车日期
			changeInfo.setTrain_no(orderInfo.getTrain_no());//车次
			changeInfo.setChange_train_no(trainCode);//改签后车次
			changeInfo.setFrom_time(orderInfo.getFrom_time());//发车时间
			changeInfo.setChange_from_time(trainDate);//改签后发车时间
			changeInfo.setTravel_time(orderInfo.getTravel_date());
			changeInfo.setFrom_city(fromStationName);//出发车站
			changeInfo.setTo_city(toStationName);//到达车站
			changeInfo.setFrom_station_code(fromStationCode);
			changeInfo.setTo_station_code(toStationCode);
			changeInfo.setIschangeto(toStationChange);
			changeInfo.setOut_ticket_billno(orderInfo.getOut_ticket_billno());//12306单号
			changeInfo.setOrder_id(orderId);
			changeInfo.setIsasync("Y");//异步
			changeInfo.setCallbackurl(callBackURL);
			changeInfo.setReqtoken(reqToken);
			changeInfo.setHasSeat(chooseNoSeat?0:1);
			changeInfo.setChange_status(Consts.TRAIN_REQUEST_CHANGE);//11改签预定
			changeInfo.setcPassengers(changePassengers);//改签、车票关系
			changeInfo.setBook_ticket_time(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3));
			Map<String,String> accountParam = new HashMap<String,String>();
			accountParam.put("order_id", orderId);
			Map<String,Object> accInfo= refundDao.queryAccountOrderInfo(accountParam);
			changeInfo.setAccount_id(accInfo.get("acc_id").toString());//出票账号id
			changeInfo.setChange_notify_count(0);
			changeInfo.setChange_notify_status(Consts.CHANGE_NOTIFY_PRE);
			changeInfo.setMerchant_id(Consts.CHANNEL_MEITUAN);
			changeInfo.setMt_change_id(resignId);
			/*改签信息入库*/
			changeDao.insertChangeInfo(changeInfo);
			int change_id = changeInfo.getChange_id();
			List<ChangePassengerInfo> cPassengers = changeInfo.getcPassengers();
			if (cPassengers != null && cPassengers.size() != 0) {
				for (ChangePassengerInfo cPassenger : cPassengers) {
					cPassenger.setChange_id(change_id);
					changeDao.insertChangeCp(cPassenger);
				}
			}
			log.setChange_id(change_id);
			log.setOrder_id(orderId);
			log.setContent("美团请求改签成功");
		    logger.info("美团请求改签成功,orderId : " + orderId );
		} catch (Exception e) {
			logger.info("美团请求改签异常"+e);
			e.printStackTrace();
			log.setContent("美团请求改签异常!");
			result.put("success", false);
			result.put("code",111);
			result.put("msg", "请求改签发生异常" + orderIdL);
			return result;		
		} finally {
			String orderId = orderIdL.toString();
			log.setOrder_id(orderId);
			log.setOpt_person("meituan");
			changeDao.addChangeLog(log);
		}
		result.put("success", true);
		result.put("code", 802);
		result.put("msg", "请求改签接受成功");
		return result;		
	}

	@Override
	public JSONObject cancelChange(JSONObject webJson) {
		JSONObject result = new JSONObject();
		logger.info("美团取消改签开始");
		Long resignIdL = webJson.getLong("resignId");//改签id
		Long orderIdL = webJson.getLong("orderId");//美团订单号
		String reqtime = webJson.getString("reqtime");//请求时间
		String partnerid = webJson.getString("partnerid");//调用方识别
		String reqToken = webJson.getString("reqToken");//鉴权字段
		String sign = webJson.getString("sign");//数字签名
		//改签日志
		ChangeLog log=new ChangeLog();
		try {
			//通用参数检查
			if (StrUtil.isEmpty(partnerid) || StrUtil.isEmpty(reqtime)
					|| StrUtil.isEmpty(sign)) {
				logger.info("美团-通用参数缺失:partnerid," + partnerid + "|reqtime,"
						+ reqtime + "|sign," + sign);
				result.put("success", false);
				result.put("code", 103);
				result.put("msg", "通用参数缺失");
				return result;
			}
			/*业务参数检查*/
			if("".equals(resignIdL) || "".equals(orderIdL)) {
				logger.info("美团取消改签 ERROR,参数有空order_id:"+orderIdL);
				result.put("success", false);
				result.put("code",108);
				result.put("msg", "错误的业务参数");
				return result;
			}
			/*查询订单*/
			String resignId = resignIdL.toString();
			String orderId = orderIdL.toString();
			DBOrderInfo orderInfo = orderDao.queryOrderInfo(orderId);
			if(orderInfo==null){
				logger.info("美团取消改签，订单不存在，orderId"+orderId);
				result.put("success", false);
				result.put("code",112);
				result.put("msg", "订单不存在");
				return result;
			}else if(!orderInfo.getOrder_status().equals(Consts.ELONG_ORDER_SUCCESS)){
				logger.info("美团取消改签，订单状态不正确，orderId"+orderId);
				result.put("success", false);
				result.put("code",112);
				result.put("msg", "订单状态不正确");
				return result;
			}
		
			Map<String,Object> changeParam = new HashMap<String,Object>();
			changeParam.put("mt_change_id", resignId);
			ChangeInfo changeInfo = changeDao.selectChangeInfo(changeParam);
			if(changeInfo.getChange_status().startsWith("3")){
				/*订单已确认改签，不能取消改签*/
				logger.info("订单已确认改签，不能取消改签，orderId:"+orderId);
				result.put("success", false);
				result.put("code",112);
				result.put("msg", "订单状态不正确");
				return result;
			}
			if(!changeInfo.getChange_status().equals(Consts.TRAIN_REQUEST_CHANGE_SUCCESS)){
				/*订单状态不正确*/
				logger.info("美团取消改签，订单状态不是改签成功，orderId:"+orderId);
				result.put("success", false);
				result.put("code",112);
				result.put("msg", "订单状态不正确");
				return result;
			}
		
			log.setChange_id(changeInfo.getChange_id());
			Date currentTime = new Date();
			/*预订成功后的30分钟内才能取消改签*/
			Date bookTime = DateUtil.stringToDate(changeInfo.getBook_ticket_time(), DateUtil.DATE_FMT3);
			long minuteDiff = DateUtil.minuteDiff(currentTime, bookTime);
			if(minuteDiff > 30) {
				logger.info("美团取消改签ERROR,距离改签车票预订时间超过30分钟");
				result.put("success", false);
				result.put("code",112);
				result.put("msg", "订单状态不正确");
				return result;
			}
			/*将状态为14、预订成功的改签状态都改为21、改签取消*/
			changeInfo.setChange_status(Consts.TRAIN_CANCEL_CHANGE);
			changeInfo.setChange_notify_count(0);
			changeInfo.setChange_notify_status(Consts.CHANGE_NOTIFY_PRE);
			changeInfo.setReqtoken(reqToken);
			changeInfo.setCallbackurl(Consts.CANCEL_TICKET_NOTIFY_URL);
			/*更新改签状态*/
			changeDao.updateChangeInfo(changeInfo);
			log.setContent("美团取消改签请求成功");
		}catch (Exception e) {
			logger.info("美团取消改签异常"+e);
			log.setContent("美团取消改签异常!");
			result.put("success", false);
			result.put("code",111);
			result.put("msg", "美团取消改签发生异常" + orderIdL);
			return result;		
		} finally {
			String orderId = orderIdL.toString();
			log.setOrder_id(orderId);
			log.setOpt_person("meituan");
			changeDao.addChangeLog(log);
		}
		result.put("success", true);
		result.put("code", 802);
		result.put("msg", "取消改签接受成功");
		return result;	

	}

	@Override
	public JSONObject confirmChange(JSONObject webJson) {
		JSONObject result = new JSONObject();
		logger.info("美团确认改签开始");
		Long resignIdL = webJson.getLong("resignId");//改签id
		Long orderIdL = webJson.getLong("orderId");//美团订单号
		String reqtime = webJson.getString("reqtime");//请求时间
		String partnerid = webJson.getString("partnerid");//调用方识别
		String sign = webJson.getString("sign");//数字签名
		String reqToken = webJson.getString("reqToken");//鉴权字段
		//改签日志
		ChangeLog log=new ChangeLog();
		try {
			//通用参数检查
			if (StrUtil.isEmpty(partnerid) || StrUtil.isEmpty(reqtime)
					|| StrUtil.isEmpty(sign)) {
				logger.info("美团确认改签-通用参数缺失:partnerid," + partnerid + "|reqtime,"
						+ reqtime + "|sign," + sign);
				result.put("success", false);
				result.put("code", 103);
				result.put("msg", "通用参数缺失");
				return result;
			}
			/*业务参数检查*/
			if("".equals(resignIdL) || "".equals(orderIdL)) {
				logger.info("美团确认改签 ERROR,参数有空order_id:"+orderIdL);
				result.put("success", false);
				result.put("code",108);
				result.put("msg", "错误的业务参数");
				return result;
			}
			/*查询订单*/
			String resignId = resignIdL.toString();
			String orderId = orderIdL.toString();
			DBOrderInfo orderInfo = orderDao.queryOrderInfo(orderId);
			if(orderInfo==null){
				logger.info("美团确认改签，订单不存在，orderId"+orderId);
				result.put("success", false);
				result.put("code",112);
				result.put("msg", "订单不存在");
				return result;
			}else if(!orderInfo.getOrder_status().equals(Consts.ELONG_ORDER_SUCCESS)){
				logger.info("美团确认改签，订单状态不正确，orderId"+orderId);
				result.put("success", false);
				result.put("code",112);
				result.put("msg", "订单状态不正确");
				return result;
			}
			Map<String,Object> changeParam = new HashMap<String,Object>();
			changeParam.put("mt_change_id", resignId);
			ChangeInfo changeInfo = changeDao.selectChangeInfo(changeParam);
			log.setChange_id(changeInfo.getChange_id());
			/*22:34:59*/
			Calendar time_22_44_59 = Calendar.getInstance();
			time_22_44_59.set(Calendar.HOUR_OF_DAY, 22);
			time_22_44_59.set(Calendar.MINUTE, 44);
			time_22_44_59.set(Calendar.SECOND, 59);
			/*23:30:00*/
			Calendar time_23_30_00 = Calendar.getInstance();
			time_23_30_00.set(Calendar.HOUR_OF_DAY, 23);
			time_23_30_00.set(Calendar.MINUTE, 30);
			time_23_30_00.set(Calendar.SECOND, 00);
			Calendar currentTime = Calendar.getInstance();
			Calendar bookTime = Calendar.getInstance();
			Date book = DateUtil.stringToDate(changeInfo.getBook_ticket_time(), DateUtil.DATE_FMT3);
			bookTime.setTime(book);
			
			boolean timeOut = false;
			if(bookTime.before(time_22_44_59)) {//当天22:44:59之前预订
				/*30分钟的付款时间*/
				System.out.println("current : " + DateUtil.dateToString(currentTime.getTime(), "yyyy-MM-dd HH:mm:ss"));
				System.out.println("book : " + DateUtil.dateToString(book, "yyyy-MM-dd HH:mm:ss"));
				if(DateUtil.minuteDiff(currentTime.getTime(), book) > 30) {
					timeOut = true;
				}
				logger.info("当天22:44:59之前预订,30分钟的付款时间,timeout" + timeOut);
			} else if(bookTime.after(time_22_44_59)) {//当天22:44:59之后预订
				/*23:30:00之前付款*/
				if(currentTime.after(time_23_30_00)) {
					timeOut = true;
				}
				logger.info("当天22:44:59之后预订,23:30:00之前付款,timeout" + timeOut);
			}
			if(timeOut) {
				logger.info("美团确认改签ERROR,确认改签的请求时间已超过规定时间");
				result.put("success", false);
				result.put("code",111);
				result.put("msg", "确认改签的请求时间已超过规定时间");
				return result;
			}
			/*确认改签*/
			/*if(changeInfo.getChange_refund_money()!=null && changeInfo.getChange_refund_money()!="" && changeInfo.getChange_receive_money()!=null &&changeInfo.getChange_receive_money()!="") {
				
				Double change_refund_money = Double.parseDouble(changeInfo.getChange_refund_money());
				Double change_receive_money = Double.parseDouble(changeInfo.getChange_receive_money());
				if(change_receive_money > change_refund_money) {
					changeInfo.setChange_status(Consts.TRAIN_CONFIRM_CHANGE_START_PAY);
				} else {
					changeInfo.setChange_status(Consts.TRAIN_CONFIRM_CHANGE);
				}
			} else {
				changeInfo.setChange_status(Consts.TRAIN_CONFIRM_CHANGE);
			}*/
			changeInfo.setChange_status(Consts.TRAIN_CONFIRM_CHANGE);
			changeInfo.setReqtoken(reqToken);
			changeInfo.setChange_notify_count(0);
			changeInfo.setChange_notify_status(Consts.CHANGE_NOTIFY_PRE);
			changeInfo.setCallbackurl(Consts.CONFIRM_TICKET_NOTIFY_URL);
			/*更新车票改签状态*/
			changeDao.updateChangeInfo(changeInfo);
			log.setContent("美团确认改签success ,orderId" + orderId);
		}catch (Exception e) {
			logger.info("美团确认改签异常"+e);
			log.setContent("美团确认改签异常!");
			result.put("success", false);
			result.put("code",111);
			result.put("msg", "美团确认改签发生异常" + orderIdL);
			return result;		
		} finally {
			String orderId = orderIdL.toString();
			log.setOrder_id(orderId);
			log.setOpt_person("meituan");
			changeDao.addChangeLog(log);
		}
		result.put("success", true);
		result.put("code", 802);
		result.put("msg", "确认改签接受成功");
		return result;
	}

	@Override
	public List<ChangeInfo> getNoticeChangeInfo(String merchantId) {
		return changeDao.selectNoticeChangeInfo(merchantId);
	}
	@Override
	public void callbackChangeNotice(List<ChangeInfo> notifyList) {
		logger.info("美团改签回调");
		try {
			for(ChangeInfo changeInfo:notifyList){
				//先将通知信息更新状态
				ChangeInfo updateInfo = new ChangeInfo();
				updateInfo.setChange_id(changeInfo.getChange_id());
				updateInfo.setChange_notify_status(Consts.CHANGE_NOTIFY_START);
				updateInfo.setChange_notify_time(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3));
				changeDao.updateChangeInfo(updateInfo);
				//改签车票信息
				Map<String, Object> cpParam = new HashMap<String, Object>();
				cpParam.put("change_id", changeInfo.getChange_id());
				/*获取改签车票信息*/
				List<ChangePassengerInfo> cPassengers = changeDao.selectChangeCp(cpParam);
				
				/*返回data*/
				JSONObject dataParameter = new JSONObject();
				String errorMsg="";
				String changeStatus = changeInfo.getChange_status();
				//改签占座
				if(changeStatus.startsWith("1")) {
					/*请求改签*/
					dataParameter.put("resignId",Long.parseLong(changeInfo.getMt_change_id()) );
					dataParameter.put("orderId", Long.parseLong(changeInfo.getOrder_id()));
					dataParameter.put("trainCode", changeInfo.getChange_train_no());
					dataParameter.put("trainDate", changeInfo.getChange_travel_time().split(" ")[0]);
					dataParameter.put("fromStationCode", changeInfo.getFrom_station_code());
					dataParameter.put("fromStationName", changeInfo.getFrom_city());
					dataParameter.put("toStationCode", changeInfo.getTo_station_code());
					dataParameter.put("toStationName", changeInfo.getTo_city());
					dataParameter.put("reqToken",changeInfo.getReqtoken());
					dataParameter.put("success",changeStatus.equals("14")?true:false);
					if(changeStatus.equals("14")) {
						logger.info("改签成功，orderId" +changeInfo.getOrder_id());
						/*查询到达时间*/
						Map<String, Object> queryParam = new HashMap<String, Object>();
						queryParam.put("train_date", changeInfo.getChange_travel_time().split(" ")[0]);
						queryParam.put("from_station", changeInfo.getFrom_station_code());
						queryParam.put("to_station",changeInfo.getTo_station_code());
						queryParam.put("train_code", changeInfo.getChange_train_no());
						String startTime ="";
						if(changeInfo.getChange_from_time()!=null && changeInfo.getChange_from_time().length()>16){
							startTime = changeInfo.getChange_from_time().split(" ")[1].substring(0, 5);
						}
						String endTime = "";
						if(changeInfo.getChange_to_time()!=null&&changeInfo.getChange_to_time().length()>16){
							endTime = changeInfo.getChange_to_time().split(" ")[1].substring(0, 5);
						}
						if(startTime==null ||startTime.equals("")||endTime==null ||endTime.equals("")){
							Map<String,Object> timeResult = this.queryTime(changeInfo);
							logger.info("美团改签占座回调查询时间结果为"+timeResult.toString());
							if(timeResult!=null && !"".equals(timeResult)){
								dataParameter.put("arriveTime",timeResult.get("arrive_time"));
								dataParameter.put("startTime",timeResult.get("start_time"));
							}else{
								logger.info("美团改签回调查询时间，未查询到   "+changeInfo.getOrder_id());
								dataParameter.put("arriveTime","");
								dataParameter.put("startTime","");
							}
						}else{
							dataParameter.put("arriveTime",startTime);
							dataParameter.put("startTime",endTime);
						}
						
					} else if(changeStatus.equals("15")){
						logger.info("改签失败，orderId" +changeInfo.getOrder_id());
						String code = changeInfo.getFail_reason();
						errorMsg = getChangeErrorInfo(code);
						dataParameter.put("seatHoldFailReason",errorMsg);
					}
					
					//车票信息
					JSONArray newTickets = new JSONArray();
					Date book_ticket_time;
					if(changeInfo.getBook_ticket_time()!=null){
						book_ticket_time=DateUtil.stringToDate(changeInfo.getBook_ticket_time(), DateUtil.DATE_FMT3);
					}else{
						book_ticket_time=new Date();
					}
					
					//费率
					double diffRate=getDiffRate(changeInfo.getFrom_time(), book_ticket_time);
					double totalBuyMoney = 0.0;
					double totalChangeBuyMoney = 0.0;
					double totalDiff = 0.0;
					double totalPriceDiff = 0.0;
					double fee = 0.0;
					Integer priceInfoType = null;
					int cp_num=cPassengers.size();
					for(int i=0;i< cPassengers.size();i++) {
						ChangePassengerInfo cPassenger = cPassengers.get(i);
						JSONObject newTicket = new JSONObject();
						DBPassengerInfo originPassenger = orderDao.queryCpById( cPassenger.getCp_id());
						newTicket.put("ticketId",originPassenger.getOut_passengerid());
						newTicket.put("passengerName", cPassenger.getUser_name());
						newTicket.put("certificateNo", cPassenger.getUser_ids());
						if(changeStatus.equals("14")) {
							newTicket.put("ticketPrice", Double.parseDouble(cPassenger.getChange_buy_money()));
							newTicket.put("seatType", cPassenger.getMt_change_seat_type());
							newTicket.put("ticketType", TongChengConsts.getTcTicketType(cPassenger.getTicket_type()));
							newTicket.put("coachNo", cPassenger.getChange_train_box());
							newTicket.put("seatNo", cPassenger.getChange_seat_no());
							newTicket.put("ticketNo", cPassenger.getNew_cp_id());
							/*成功*/
							totalBuyMoney += cPassenger.getBuy_money();//改签之前总成本价
							totalChangeBuyMoney += Double.parseDouble(cPassenger.getChange_buy_money());//改签之后总成本价
						} else if(changeStatus.equals("15")) {
							/*失败*/
							priceInfoType = 0;		
						}
						newTickets.add(newTicket);
					}
					logger.info("totalBuyMoney : " + totalBuyMoney);
					logger.info("totalChangeBuyMoney : " + totalChangeBuyMoney);
					totalDiff = totalChangeBuyMoney - totalBuyMoney;//总差价
					if(totalDiff < 0.0) {//新票款低于原票款
						priceInfoType = 3;
					} else if(totalDiff == 0.0) {//新票款等于原票款
						priceInfoType = 2;
					} else if(totalDiff > 0.0) {//新票款大于原票款
						priceInfoType = 1;
					}
				
					if(priceInfoType != null) {
						/*改签成功后生成流水号、计算手续费*/
						if(priceInfoType == 3) {//新票款低于原票款
							if(diffRate==0){
								fee=0;
							}else{
								fee = AmountUtil.quarterConvert(Math.abs(AmountUtil.mul(totalDiff, diffRate)));//手续费=退款差额 * 费率
								int less_fee=2*cp_num;
								fee=fee<less_fee?less_fee:fee;
								
								if(fee > Math.abs(totalDiff)) {
									fee = Math.abs(totalDiff);
								}
							}
							totalPriceDiff = AmountUtil.sub(Math.abs(totalDiff), fee);//实际退款=退款差额-手续费
							changeInfo.setFee(fee+"");
							changeInfo.setDiffrate(diffRate+"");
							changeInfo.setTotalpricediff(totalPriceDiff+"");
						} else if(priceInfoType == 1) {//新票款大于原票款
							changeInfo.setChange_refund_money(totalBuyMoney + "");
							changeInfo.setChange_receive_money(totalChangeBuyMoney + "");
						}
					}
					changeDao.updateChangeInfo(changeInfo);
					dataParameter.put("tickets", newTickets);
					dataParameter.put("orderAmount",totalChangeBuyMoney);
					dataParameter.put("returnFact", totalPriceDiff);
				} else if(changeStatus.startsWith("2")) {
					//改签取消
					dataParameter.put("resignId", Long.parseLong(changeInfo.getMt_change_id()));
					dataParameter.put("reqToken",changeInfo.getReqtoken());
					dataParameter.put("success",changeStatus.equals("23")?true:false);
				} else if(changeStatus.startsWith("3")) {
					dataParameter.put("resignId", Long.parseLong(changeInfo.getMt_change_id()));
					dataParameter.put("reqToken",changeInfo.getReqtoken());
					dataParameter.put("success",changeStatus.equals("34")?true:false);
					if(changeStatus.equals("34")){
						for(ChangePassengerInfo cPassenger : cPassengers) {
							cPassenger.setIs_changed("Y");
						    changeDao.updateChangeCp(cPassenger);
						}
					}
				}
				
				/*回调*/
				logger.info("美团改签回调地址为："+changeInfo.getCallbackurl());
				logger.info("美团改签回调参数为："+dataParameter.toString());
				String result = HttpPostJsonUtil.sendJsonPost(changeInfo.getCallbackurl(), dataParameter.toString(), "utf-8");
				changeInfo.setChange_notify_count(changeInfo.getChange_notify_count()+1);
				if (result != null && StringUtils.isNotEmpty(result)) {
					JSONObject json = JSONObject.fromObject(result);
					logger.info("美团改签结果通知返回结果："+ result);
					boolean success = json.getBoolean("success");
					if (success) {
						logger.info("美团改签结果通知成功");
						changeInfo.setChange_notify_status(Consts.CHANGE_NOTIFY_OVER);
						changeInfo.setChange_notify_finish_time(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3));
					} else {
						logger.info("美团改签结果通知失败");
						if (changeInfo.getChange_notify_count() == 5&& (!changeInfo.getChange_notify_status().equals(Consts.CHANGE_NOTIFY_OVER))) {
							changeInfo.setChange_notify_status(Consts.CHANGE_NOTIFY_FAIL);
						}
					}
				} else {
					logger.info("美团出票结果通知失败,result返回null");
				}
				updateNotice(changeInfo);
			} 
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("美团改签回调异常");
		}
	}
	private void updateNotice(ChangeInfo change) {
		/*回调*/
		ChangeLog log = new ChangeLog();
		log.setOrder_id(change.getOrder_id());
		log.setChange_id(change.getChange_id());
		log.setOpt_person("meituan");
		//改签成功
		if(change.getChange_notify_status().equals(Consts.CHANGE_NOTIFY_OVER)){
			log.setContent("美团改签回调成功");
		}else{
			//改签失败
			log.setContent("美团改签回调失败");
		}
		changeDao.updateChangeInfo(change);
		changeDao.addChangeLog(log);
	}
	
	
	//改签结算费率
	private  Double getDiffRate(String from_time, Date current) {
		Double rate = 0.0;//默认值
		
		Date from = DateUtil.stringToDate(from_time, DateUtil.DATE_FMT3);
		Date from_24hour = DateUtil.dateAddDays(from, -1);//24小时以内
		Date from_48hour = DateUtil.dateAddDays(from, -2);//48小时以内
		Date from_15Day = DateUtil.dateAddDays(from, -15);
		
		if(current.after(from_24hour)) {
			rate = 0.2;
		} else if(current.before(from_24hour) && current.after(from_48hour)) {
			rate = 0.1;
		} else if(current.before(from_48hour) && current.after(from_15Day)) {
			rate = 0.05;
		} else {
			
		}
		return rate;
	}
	
	private Map<String,Object> queryTime(ChangeInfo changeInfo){
		/*// 针对没有to_time采用查询补全
		String url = Consts.QUERY_TRAIN_TIME;
		String result ="";
		paramMap.put("commond", "queryStation");
		String params;
		try {
			params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
			result = HttpUtil.sendByPost(url, params, "UTF-8");
			System.out.println(result);
		} catch (Exception e) {
			logger.info("美团改签回调补全时间发生异常");
			e.printStackTrace();
		}
		return result;*/
		Map<String,Object> timeResult = new HashMap<String,Object>();
		
		try{
			//查询数据库
			Map<String, Object> queryParam = new HashMap<String, Object>();
			queryParam.put("checi", changeInfo.getChange_train_no());
			queryParam.put("name", changeInfo.getFrom_city());
			Station fromStation = commonDao.selectOneStation(queryParam);
			queryParam.put("name",changeInfo.getTo_city());
			Station toStation = commonDao.selectOneStation(queryParam);
			String start_query_time = null;
			String arrive_query_time = null;
			if(fromStation == null || toStation == null) {
				logger.info(changeInfo.getOrder_id()  + " 美团改签回调-数据库时间补全失败!");

				// 针对没有to_time采用查询补全
				Map<String, String> paramMap = new HashMap<String, String>();
				String url = Consts.QUERY_TICKET;
				paramMap.put("channel", "tongcheng");//利用同程渠道的查询方法
				paramMap.put("from_station", changeInfo.getFrom_city());
				paramMap.put("arrive_station", changeInfo.getTo_city());
				paramMap.put("travel_time", changeInfo.getChange_from_time().split(" ")[0]);
				paramMap.put("purpose_codes", "ADULT");
				paramMap.put("isNotZW", "yes");// 非中文查询
				String params;
				params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
				logger.info(changeInfo.getOrder_id()+" 美团发起余票查询" + paramMap.toString() + "url" + url);
				String result = HttpUtil.sendByPost(url, params, "UTF-8");
				logger.info(changeInfo.getOrder_id()+ " 美团发起余票查询，查询结果"+result);

				String runtime = null;
				if(result == null || result == ""|| result.equalsIgnoreCase("STATION_ERROR")|| result.equalsIgnoreCase("ERROR")){
					result = HttpUtil.sendByPost(url, params, "UTF-8");
					if(result == null || result == ""|| result.equalsIgnoreCase("STATION_ERROR")|| result.equalsIgnoreCase("ERROR")){
						result = HttpUtil.sendByPost(url, params, "UTF-8");
					}
				}
				if (result == null || result == ""|| result.equalsIgnoreCase("STATION_ERROR")|| result.equalsIgnoreCase("ERROR")) {
					logger.info(changeInfo.getOrder_id()+"美团改签回调通知,针对没有to_time采用查询补全_查询失败");
				} else {
					try{
						JSONArray arr = JSONArray.fromObject(result);
						int index = arr.size();
						for (int i = 0; i < index; i++) {
							if (arr.getJSONObject(i).get("train_code").equals(changeInfo.getChange_train_no())) {
								runtime = arr.getJSONObject(i).getString(
										"run_time_minute");
								start_query_time = arr.getJSONObject(i)
										.getString("start_time");
								arrive_query_time = arr.getJSONObject(i)
										.getString("arrive_time");
								break;
							}

						}
						if (start_query_time == null|| arrive_query_time == null || runtime == null) {
							logger.info("美团改签回调通知,针对没有to_time采用查询补全_查询失败,未匹配到结果");
						} else {
							logger.info("美团改签回调通知,针对没有to_time采用查询补全_查询成功"+ start_query_time + "_"+ arrive_query_time + "_" + runtime);
							timeResult.put("start_time", start_query_time);
							timeResult.put("arrive_time", arrive_query_time);
						}
					}catch(JSONException e1){
						logger.info("美团改签回调通知,针对没有to_time采用查询补全_查询异常, result="+result);
						e1.printStackTrace();
					}
				}
			
			} else {
				logger.info(changeInfo.getOrder_id() + " 美团数据库时间补全成功");
				timeResult.put("start_time",  fromStation.getStartTime());
				timeResult.put("arrive_time", toStation.getArriveTime());
			}
		} catch (Exception e) {
			logger.info("美团改签回调补全时间发生异常");
			e.printStackTrace();
		}
		return timeResult;
	}
	private static Map<String, String> changeErrorInfo = new HashMap<String, String>();
	static {
		changeErrorInfo.put("1002","距离开车时间太近无法改签");
		changeErrorInfo.put("1004","取消改签次数超过上限,无法继续操作");
		changeErrorInfo.put("301","没有余票");
		changeErrorInfo.put("310","本次购票与其他订单冲突");
		changeErrorInfo.put("506","系统异常,无法正常占座操作");
		changeErrorInfo.put("313","订单内乘客已被法院依法限制高消费，禁止乘坐当前预订席别");
		changeErrorInfo.put("314","账号登陆失败");
		changeErrorInfo.put("999","未定义的12306错误");
		changeErrorInfo.put("9991","旅游票,请到车站办理");
		changeErrorInfo.put("308","存在未完成订单，请支付后再试");
		changeErrorInfo.put("316","开车前48小时（不含）以上，可改签预售期内其他列车；开车前48小时以内且非开车当日，可改签票面日期当日及以前的其他列车；开车当日，可改签票面日期当日的其他列车");
		changeErrorInfo.put("318","已退票，不可改签");
		changeErrorInfo.put("320","未找到改签待支付订单");
		changeErrorInfo.put("315","未找到要改签的车次");
		changeErrorInfo.put("317","已出票，请到车站办理");
		changeErrorInfo.put("319","已改签，不可再次改签");
		changeErrorInfo.put("322","当前的排队购票人数过多，请稍后重试");
		changeErrorInfo.put("324","已确认改签，不可取消");
	}
	
	private  String getChangeErrorInfo(String errorCode) {
		String errorInfo = changeErrorInfo.get(errorCode);
		return errorInfo;
	}

	@Override
	public Map<String, Object> queryChangeCpInfo(Map<String, Object> param) {
		
		return changeDao.queryChangeCpInfo(param);
	}
}
