package com.l9e.transaction.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.l9e.common.TrainConsts;
import com.l9e.transaction.dao.CommonDao;
import com.l9e.transaction.dao.OrderDao;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.DBStudentInfo;
import com.l9e.transaction.vo.InterAccount;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.transaction.vo.OrderInfoTrip;
import com.l9e.transaction.vo.QunarResult;
import com.l9e.transaction.vo.SysConfig;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.TrainPropUtil;
import com.l9e.util.UrlFormatUtil;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
	
	private static final Logger logger = Logger.getLogger(OrderServiceImpl.class);
	
	@Resource
	private OrderDao orderDao;
	
	@Resource
	private CommonDao commonDao;
	
	
	private String notify_cp_interface_url;
	
	private String notify_cp_back_url;
	

	@Value("#{propertiesReader[qunarReqUrl]}")
	private String qunarReqUrl;//Qunar请求地址
	
	@Value("#{propertiesReader[notify_cp_interface_url]}")
	public void setNotify_cp_interface_url(String notify_cp_interface_url) { 
		this.notify_cp_interface_url = notify_cp_interface_url;
	}
	
	@Value("#{propertiesReader[notify_cp_back_url]}")
	public void setNotify_cp_back_url(String notify_cp_back_url) {
		this.notify_cp_back_url = notify_cp_back_url;
	}
	@Override
	public int queryOrderCountByNo(String order_id) {
		return orderDao.queryOrderCountByNo(order_id);
	}

	@Override
	public int queryOrderCPCountByNo(String order_id) {
		return orderDao.queryOrderCPCountByNo(order_id);
	}
	
	@Override
	public void addQunarOrder(OrderInfo orderInfo, List<OrderInfoCp> cpList, List<OrderInfoTrip> tripList, List<DBStudentInfo> students) {
		orderDao.addQunarOrder(orderInfo);//订单表
		for(OrderInfoCp cpInfo : cpList){
			orderDao.addQunarOrderCp(cpInfo);//车票表
		}
		
		if(students!=null&&students.size()>0){
			for(DBStudentInfo s:students){
				orderDao.addStudentInfo(s);//学生票入库
			}
		}
		
		//添加日志
		Map<String, String> logMap = new HashMap<String, String>();
		logMap.put("order_id", orderInfo.getOrder_id());
		
		
		String order_type = orderInfo.getOrder_type();
		String order_type_name = null;
		String weather_work = commonDao.queryQunarSysValue("weather_work");
		if(StringUtils.isEmpty(order_type) || TrainConsts.ORDER_TYPE_COMMON.equals(order_type)){//普通订单

			order_type_name = "普通订单";
			
			if("1".equals(weather_work)){
				logger.info("12306异常，直接失败，"+orderInfo.getOrder_id());
				Map<String, String> failMap = new HashMap<String, String>();
				failMap.put("order_id", orderInfo.getOrder_id());
				failMap.put("order_status", "45");
				failMap.put("out_fail_reason", "1");
				orderDao.addQunarOrderNotify(orderInfo.getOrder_id(), TrainConsts.ORDER_TYPE_COMMON, null);//通知表
				orderDao.updateOrderWithCpNotify(failMap);
				orderDao.updateOutNotifyPrepare(orderInfo.getOrder_id());
				logMap.put("content", "成功接收qunar订单并且下单完成，"+order_type_name);
			}else{
				if(StringUtils.isEmpty(orderInfo.getSeat_type())){//qunar坐席为空，异常数据，直接失败
					logger.info("qunar坐席为空，异常数据，直接失败，"+orderInfo.getOrder_id());
					Map<String, String> failMap = new HashMap<String, String>();
					failMap.put("order_id", orderInfo.getOrder_id());
					failMap.put("order_status", "45");
					failMap.put("out_fail_reason", "1");
					orderDao.addQunarOrderNotify(orderInfo.getOrder_id(), TrainConsts.ORDER_TYPE_COMMON, null);//通知表
					orderDao.updateOrderWithCpNotify(failMap);
					orderDao.updateOutNotifyPrepare(orderInfo.getOrder_id());
				}else{
					orderDao.addQunarOrderNotify(orderInfo.getOrder_id(), TrainConsts.ORDER_TYPE_COMMON, "00");//通知表
				}
				
				if("11".equals(orderInfo.getIs_pay())){//是否支付：00：已支付；11：未支付
					logMap.put("content", "成功接收qunar占座订单并且下单完成，"+order_type_name);
				}else{
					logMap.put("content", "成功接收qunar订单并且下单完成，"+order_type_name);
				}
				
			}
			
		}else{
			if(tripList != null && tripList.size()>0){
				for(OrderInfoTrip trip : tripList){
					orderDao.addQunarOrderTrip(trip);//联程订单
					orderDao.addQunarOrderNotify(trip.getTrip_id(), TrainConsts.ORDER_TYPE_TRIP, "00");
				}
			}
			orderDao.addQunarOrderNotify(orderInfo.getOrder_id(), TrainConsts.ORDER_TYPE_TRIP, null);
			
			order_type_name = "联程订单";
			
			if("1".equals(weather_work)){
				Map<String, String> failMap = new HashMap<String, String>();
				failMap.put("order_id", orderInfo.getOrder_id());
				failMap.put("order_status", "45");
				failMap.put("out_fail_reason", "1");
				orderDao.updateOrderWithCpNotify(failMap);
				orderDao.updateOutNotifyPrepare(orderInfo.getOrder_id());
			}
			logMap.put("content", "成功接收qunar订单并且下单完成，"+order_type_name);
		}
		
		logMap.put("opt_person", "qunar_app");
		orderDao.addOrderInfoLog(logMap);
	}

	@Override
	public List<Map<String, String>> queryTimedOutTicketList() {
		return orderDao.queryTimedOutTicketList();
	}

	@Override
	public List<Map<String, String>> queryOrderCpList(String order_id) {
		return orderDao.queryOrderCpList(order_id);
	}

	@Override
	public void updateQunarOutNotifyEnd(String order_id) {
		orderDao.updateQunarOutNotifyEnd(order_id);
		//添加日志
		Map<String, String> logMap = new HashMap<String, String>();
		logMap.put("order_id", order_id);
		logMap.put("content", "出票结果通知qunar成功");
		logMap.put("opt_person", "qunar_app");
		orderDao.addOrderInfoLog(logMap);
	}

	@Override
	public void updateQunarOutNotifyBegin(String order_id) {
		orderDao.updateQunarOutNotifyBegin(order_id);
	}

	@Override
	public List<Map<String, String>> queryTimedWaitingRefundList() {
		return orderDao.queryTimedWaitingRefundList();
	}

	@Override
	public void updateQunarRefundNotifyBegin(Map<String, String> map) {
		orderDao.updateQunarRefundNotifyBegin(map);
	}

	@Override
	public void updateQunarRefundNotifyEnd(Map<String, String> map) {
		orderDao.updateQunarRefundNotifyEnd(map);
		//添加日志
		Map<String, String> logMap = new HashMap<String, String>();
		logMap.put("order_id", map.get("order_id"));
		logMap.put("content", "退票结果通知qunar成功");
		logMap.put("opt_person", "qunar_app");
		orderDao.addOrderInfoLog(logMap);
	}

	@Override
	public int queryRefundCountByNo(String order_id) {
		return orderDao.queryRefundCountByNo(order_id);
	}

	@Override
	public void addQunarRefund(Map<String, String> map) {
		orderDao.addQunarRefund(map);
		//添加日志
		Map<String, String> logMap = new HashMap<String, String>();
		logMap.put("order_id", map.get("order_id"));
		logMap.put("content", "成功接收qunar退票订单");
		logMap.put("opt_person", "qunar_app");
		orderDao.addOrderInfoLog(logMap);
	}

	@Override
	public OrderInfo queryOrderInfoById(String order_id) {
		return orderDao.queryOrderInfoById(order_id);
	}

	@Override
	public List<Map<String, String>> queryTimedCpSysList() {
		return orderDao.queryTimedCpSysList();
	}

	@Override
	public List<Map<String, String>> queryCpInfoList(String order_id) {
		return orderDao.queryCpInfoList(order_id);
	}

	@Override
	public void updateCpSysNotifyBegin(String order_id) {
		orderDao.updateCpSysNotifyBegin(order_id);
	}

	@Override
	public void updateCpSysOutNotifyEnd(String corder_id, String order_id) {
		orderDao.updateCpSysOutNotifyEnd(corder_id);
		//添加日志
		Map<String, String> logMap = new HashMap<String, String>();
		logMap.put("order_id", order_id);
		logMap.put("content", "成功通知出票系统出票，"+corder_id);
		logMap.put("opt_person", "qunar_app");
		orderDao.addOrderInfoLog(logMap);
	}

	@Override
	public int updateOrderWithCpNotify(Map<String, String> paramMap,
			List<Map<String, String>> cpMapList, Map<String, String> logMap, 
			String notifyQuanrAtStatus, OrderInfoTrip trip) {
		try {
//			OrderInfoTrip trip = null;
//			trip = orderDao.queryTripOrderInfoById(paramMap.get("order_id"));
			if(trip == null){//普通订单
				orderDao.updateOrderWithCpNotify(paramMap);
			}else{//联程订单
				orderDao.updateTripOrderWithCpNotify(paramMap);
				logger.info("【联程订单】:order_id="+paramMap.get("order_id")+"更新qunar_orderinfo_trip表的数据,notifyQuanrAtStatus:"+notifyQuanrAtStatus);
				logger.info("【联程订单】:paramMap="+paramMap);
//				logMap.put("order_id", trip.getOrder_id());
//				logMap.put("content", logMap.get("logMap")+"，"+paramMap.get("order_id"));
			}
			
			if (cpMapList != null) {
				for (Map<String, String> cpMap : cpMapList) {
					orderDao.updateCpOrderWithCpNotify(cpMap);
					logger.info("order_id="+paramMap.get("order_id")+"更新qunar_orderinfo_cp表的数据");
				}
				
			}
			//更新通知qunar通知结果状态
			if((notifyQuanrAtStatus.equals(paramMap.get("order_status")) 
					|| TrainConsts.OUT_SUCCESS.equals(paramMap.get("order_status")))
					&& "00".equals(paramMap.get("is_pay"))){
				logger.info("【更新通知qunar通知结果状态】order_id="+paramMap.get("order_id"));
				if(trip == null){//普通订单
					orderDao.updateOutNotifyPrepare(paramMap.get("order_id"));
				}else{//联程订单需要判断兄弟订单是否出票完成
					List<Map<String, String>> tripList = orderDao.queryTripListByOrderId(trip.getOrder_id());
					logger.info("【联程订单】:tripList="+tripList);
					double total_pay = 0;
					boolean isWanted = true;//是否需要修改通知表
					if(notifyQuanrAtStatus.equals(TrainConsts.OUT_SUCCESS)){//出票成功后通知
						for(Map<String, String> tripMap : tripList){
							if(!tripMap.get("order_status").equals(TrainConsts.OUT_SUCCESS)){
								isWanted = false;
								total_pay = 0;
								break;
							}else{
								total_pay += Double.parseDouble(tripMap.get("buy_money"));
							}
						}
						logger.info("【联程订单】出票成功后通知:order_id="+paramMap.get("order_id")+",isWanted="+isWanted+",total_pay="+total_pay);
					}else if(notifyQuanrAtStatus.equals(TrainConsts.BOOK_SUCCESS)){//预订成功后通知
						for(Map<String, String> tripMap : tripList){
							if(!tripMap.get("order_status").equals(TrainConsts.OUT_SUCCESS)
									&& !tripMap.get("order_status").equals(TrainConsts.BOOK_SUCCESS)){
								isWanted = false;
								total_pay = 0;
								break;
							}else{
								total_pay += Double.parseDouble(tripMap.get("buy_money"));
							}
						}
						logger.info("【联程订单】预订成功后通知:order_id="+paramMap.get("order_id")+",isWanted="+isWanted+",total_pay="+total_pay);
					}else{//出票失败
						isWanted = true;
						logger.info("【联程订单】出票失败通知:order_id="+paramMap.get("order_id")+",isWanted="+isWanted);
					}
					if(isWanted == true){
						//更新大订单信息
						paramMap.put("order_id", trip.getOrder_id());
						if(total_pay > 0){
							paramMap.put("buy_money", String.valueOf(total_pay));
						}else{
							paramMap.put("buy_money", null);
						}
						orderDao.updateOrderWithCpNotify(paramMap);
						
						orderDao.updateOutNotifyPrepare(trip.getOrder_id());
						logger.info("【联程订单】:order_id="+paramMap.get("order_id")+"更新qunar_orderinfo、qunar_orderinfo_notify表的数据,out_notify_status='00'");
					}

				}
			}
			//日志
			orderDao.addOrderInfoLog(logMap);
		} catch (Exception e) {
			logger.error("Exception:order_id="+paramMap.get("order_id"), e);
			e.printStackTrace();
			return 0;
		}
		return 1;
	}

	@Override
	public void addOrderInfoLog(Map<String, String> logMap) {
		orderDao.addOrderInfoLog(logMap);
	}

	@Override
	public String queryQunarSysSetting(String key) {
		return orderDao.queryQunarSysSetting(key);
	}

	@Override
	public OrderInfoTrip queryTripOrderInfoById(String trip_id) {
		return orderDao.queryTripOrderInfoById(trip_id);
	}

	@Override
	public List<Map<String, String>> queryTripListByOrderId(String order_id) {
		return orderDao.queryTripListByOrderId(order_id);
	}

	@Override
	public int queryOutTicketNotifyCount(String order_id) {
		return orderDao.queryOutTicketNotifyCount(order_id);
	}

	@Override
	public void updateOrderNotifyFail(String order_id) {
		orderDao.updateOrderNotifyFail(order_id);
	}

	@Override
	public List<Map<String, String>> queryPayMoneyByOrderId(String order_id) {
		return orderDao.queryPayMoneyByOrderId(order_id);
	}

	@Override
	public int queryRefundCountByRefuse(String orderId) {
		return orderDao.queryRefundCountByRefuse(orderId);
	}

	@Override
	public Map<String, String> queryRefundStatusByNo(String order_id) {
		return orderDao.queryRefundStatusByNo(order_id);
	}

	@Override
	public void updateQunarRefund(String order_id) {
		orderDao.updateQunarRefund(order_id);
		//添加日志
		Map<String, String> logMap = new HashMap<String, String>();
		logMap.put("order_id", order_id);
		logMap.put("content", DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3)+"qunar重复退票[针对系统已拒绝退款订单]");
		logMap.put("opt_person", "qunar_app");
		orderDao.addOrderInfoLog(logMap);
	}

	@Override
	public void updateQunarRefundStatus(String order_id) {
		orderDao.updateQunarRefundStatus(order_id);
		
	}

	@Override
	public void updateCPAlterInfo(Map<String, String> map) {
		orderDao.updateCPAlterInfo(map);
	}

	@Override
	public void updateCPRefundInfo(Map<String, String> map) {
		orderDao.updateCPRefundInfo(map);
	}

	@Override
	public void updateRefundInfoSingle(Map<String, String> map) {
		orderDao.updateRefundInfoSingle(map);
	}

	@Override
	public void updateQunarCPRefundMoney(Map<String, Object> map) {
		orderDao.updateQunarCPRefundMoney(map);
	}

	@Override
	public Map<String, String> queryAccountOrderInfo(Map<String, String> param) {
		return orderDao.queryAccountOrderInfo(param);
	}

	@Override
	public List<Map<String, String>> queryCanRefundStreamList() {
		return orderDao.queryCanRefundStreamList();
	}

	@Override
	public Map<String, String> queryRefundCpOrderInfo(Map<String, String> param) {
		return orderDao.queryRefundCpOrderInfo(param);
	}

	@Override
	public void updateOrderRefundStatus(Map<String, String> map) {
		orderDao.updateOrderRefundStatus(map);
	}

	@Override
	public int queryOrderCpNoRefundNum(String order_id) {
		return orderDao.queryOrderCpNoRefundNum(order_id);
	}

	@Override
	public List<Map<String, String>> queryOrderCpRefundInfoList(String order_id) {
		return orderDao.queryOrderCpRefundInfoList(order_id);
	}

	@Override
	public List<Map<String, String>> queryBookResultList() {
		return orderDao.queryBookResultList();
	}

	@Override
	public int queryBookTicketNotifyCount(String orderId) {
		return orderDao.queryBookTicketNotifyCount(orderId);
	}

	@Override
	public void updateOrderBookNotifyFail(String orderId) {
		orderDao.updateOrderBookNotifyFail(orderId);
	}

	@Override
	public void updateQunarBookNotifyEnd(String orderId) {
		orderDao.updateQunarBookNotifyEnd(orderId);
		//添加日志
		/*Map<String, String> logMap = new HashMap<String, String>();
		logMap.put("order_id", orderId);
		logMap.put("content", "占座结果通知qunar成功");
		logMap.put("opt_person", "qunar_app");
		orderDao.addOrderInfoLog(logMap);*/
	}

	@Override
	public void updateQunarBookNotifyBegin(String orderId) {
		orderDao.updateQunarBookNotifyBegin(orderId);
	}

	@Override
	public int queryPayNotifyCount(String orderNo) {
		return orderDao.queryPayNotifyCount(orderNo);
	}

	@Override
	public List<Map<String, String>> queryWaitPayOrderList() {
		return orderDao.queryWaitPayOrderList();
	}

	@Override
	public void updateOrderPayNotifyFail(String orderNo) {
		orderDao.updateOrderPayNotifyFail(orderNo);
	}

	@Override
	public void updateQunarPayNotifyBegin(String orderNo) {
		orderDao.updateQunarPayNotifyBegin(orderNo);
	}

	@Override
	public void updateQunarPayNotifyEnd(String orderNo) {
		orderDao.updateQunarPayNotifyEnd(orderNo);
		//添加日志
		Map<String, String> logMap = new HashMap<String, String>();
		logMap.put("order_id", orderNo);
		logMap.put("content", "代付结果通知qunar成功");
		logMap.put("opt_person", "qunar_app");
		orderDao.addOrderInfoLog(logMap);
	}
	

	@Override
	public void addOrderInfoByBackup(String order_id) {
		orderDao.addOrderInfoByBackup(order_id);
	}

	@Override
	public void addOrderCpInfoByBackup(String order_id) {
		orderDao.addOrderCpInfoByBackup(order_id);
	}

	@Override
	public Map<String, String> queryAccountOrderBackupInfo(
			Map<String, String> param) {
		return orderDao.queryAccountOrderBackupInfo(param);
	}


	@Override
	public Map<String, String> queryAccountInfo(String orderNo) {
		return orderDao.queryAccountInfo(orderNo);
	}

	@Override
	public void updateOrderStatus(Map<String, String> map) {
		orderDao.updateOrderWithCpNotify(map);
	}

	/**通知出票系统出票*/
	@Override
	public void sendCpSysOutTicket(String order_type,String order_id) throws Exception {
		OrderInfo orderInfo = null;
		
		Map<String, String> paramMap = null;
		//通知qunar：0、出票成功通知 1、预定成功通知  2、先预定后支付
		if(StringUtils.isEmpty(order_type) || TrainConsts.ORDER_TYPE_COMMON.equals(order_type)){//普通订单
			orderInfo = this.queryOrderInfoById(order_id);
			logger.info("通知去哪order_id:"+order_id);
			if(!TrainConsts.PAY_SUCCESS.equals(orderInfo.getOrder_status())){
				logger.info("【出票系统接口】订单状态不是支付成功,过滤该条数据，order_id="+orderInfo.getOrder_id()
						+"&order_status="+orderInfo.getOrder_status());
				return;
			}
			paramMap = new HashMap<String, String>();
			paramMap.put("orderid", orderInfo.getOrder_id());
			paramMap.put("ordername", orderInfo.getOrder_name());
			paramMap.put("paymoney", orderInfo.getPay_money());//车票总价
			paramMap.put("trainno", orderInfo.getTrain_no());
			paramMap.put("fromcity", orderInfo.getFrom_city());
			paramMap.put("tocity", orderInfo.getTo_city());
			paramMap.put("fromtime", orderInfo.getFrom_time());
			paramMap.put("totime", orderInfo.getTo_time());
			paramMap.put("traveltime", orderInfo.getTravel_time());
			paramMap.put("seattype", orderInfo.getSeat_type().substring(0, 1));
			paramMap.put("outtickettype", orderInfo.getOut_ticket_type());
			paramMap.put("channel", orderInfo.getChannel());
			if(orderInfo.getRetUrl()!=null || !StringUtils.isEmpty(orderInfo.getRetUrl())){//占座订单
				paramMap.put("ispay", "11");
			}else{
				paramMap.put("ispay", "00");
			}
//			if("2".equals(whenNotify)){
//				paramMap.put("is_pay", "11");
//			}else{
//				paramMap.put("is_pay", "00");
//			}
			StringBuffer extSb = new StringBuffer();
			extSb.append(orderInfo.getSeat_type());
			if(!StringUtils.isEmpty(orderInfo.getExt_seat())){
				extSb.append("#").append(orderInfo.getExt_seat());
			}else{
				extSb.append("#").append("无");//无备选坐席
			}
			paramMap.put("extseattype", extSb.toString());
			
			this.sendRequest(orderInfo.getOrder_id(), paramMap, orderInfo.getOrder_id());

		}else{//联程订单
			
			OrderInfoTrip trip = null;
			trip = this.queryTripOrderInfoById(order_id);
			
			if(!TrainConsts.PAY_SUCCESS.equals(trip.getOrder_status())){
				logger.info("【出票系统接口】联程订单状态不是支付成功,过滤该条数据，trip_id="+trip.getTrip_id()
						+"&order_status="+trip.getOrder_status());
				return;
			}
			paramMap = new HashMap<String, String>();
			paramMap.put("orderid", trip.getTrip_id());
			paramMap.put("ordername", trip.getOrder_name());
			paramMap.put("paymoney", trip.getPay_money());//车票总价？？？？？？
			paramMap.put("trainno", trip.getTrain_no());
			paramMap.put("fromcity", trip.getFrom_city());
			paramMap.put("tocity", trip.getTo_city());
			paramMap.put("fromtime", trip.getFrom_time());
			paramMap.put("totime", trip.getTo_time());
			paramMap.put("traveltime", trip.getTravel_time());
			paramMap.put("seattype", trip.getSeat_type().substring(0, 1));
			paramMap.put("outtickettype", trip.getOut_ticket_type());
			paramMap.put("channel", trip.getChannel());
			
//			if("2".equals(whenNotify)){
//				paramMap.put("is_pay", "11");
//			}else{
//				paramMap.put("is_pay", "00");
//			}
			paramMap.put("ext", "level|10");//10为联程票
			
			StringBuffer extSb = new StringBuffer();
			extSb.append(trip.getSeat_type());
			if(!StringUtils.isEmpty(trip.getExt_seat())){
				extSb.append("#").append(trip.getExt_seat());
			}else{
				extSb.append("#").append("无");//无备选坐席
			}
			paramMap.put("extseattype", extSb.toString());

			this.sendRequest(trip.getTrip_id(), paramMap, trip.getOrder_id());
		}
		
	}


/**
 * 发送请求
 * @param corder_id 子订单号
 * @param paramMap
 * @param order_id qunar订单号
 * @throws Exception
 */
public void sendRequest(String corder_id, Map<String, String> paramMap, String order_id) throws Exception{
	List<Map<String, String>> cpInfoList = null;
	cpInfoList = this.queryCpInfoList(corder_id);
	this.updateCpSysNotifyBegin(corder_id);
	StringBuffer sb = new StringBuffer();
	for (Map<String, String> cpInfo : cpInfoList) {
		if(sb.length()>0) {
			sb.append("#");
		}
		sb.append(cpInfo.get("cp_id")).append("|").append(cpInfo.get("user_name")).append("|")
		  .append(cpInfo.get("ticket_type")).append("|").append(cpInfo.get("ids_type")).append("|")
		  .append(cpInfo.get("user_ids")).append("|").append(paramMap.get("seattype")).append("|")
		  .append(cpInfo.get("pay_money"));
	}
	paramMap.put("seattrains", sb.toString());
	paramMap.put("backurl", notify_cp_back_url);

	String params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
	
	String result = HttpUtil.sendByPost(notify_cp_interface_url, params, "UTF-8");
	logger.info("【出票系统接口】通知返回：" + result);
	
	if(!StringUtils.isEmpty(result)){
		String[] results = result.trim().split("\\|");
		
		if("success".equalsIgnoreCase(results[0]) && results.length == 2
				&& corder_id.equals(results[1])){//通知成功
			logger.info("【出票系统接口】通知出票系统成功，order_id=" + corder_id);
			this.updateCpSysOutNotifyEnd(corder_id, order_id);
		}else{
			logger.info("【出票系统接口】通知出票系统失败，order_id="+ corder_id + "，系统将在约1分钟后重新通知");
			//添加日志
			Map<String, String> logMap = new HashMap<String, String>();
			logMap.put("order_id", order_id);
			logMap.put("content", "通知出票系统失败，"+corder_id);
			logMap.put("opt_person", "qunar_app");
			this.addOrderInfoLog(logMap);
		}
	}
}

	@Override
	public void sendNotifyRequest(String order_id, String order_type)
			throws Exception {
		this.updateQunarOutNotifyBegin(order_id);
		
		OrderInfo orderInfo = this.queryOrderInfoById(order_id);
		String opt = null;//协议-操作类型
		String status = null;//是否预订成功
		String comment = null;//协议-备注
		Map<String, String> map = null;
		
		//获取qunar订单来源账号
		String order_source = null;
		if(StringUtils.isEmpty(orderInfo.getOrder_source())){
			order_source = "qunar1";
		}else{
			order_source = orderInfo.getOrder_source();
		}
		InterAccount account = SysConfig.getAccountByName(order_source);
		if(account == null){
			logger.error("fatal error InterAccount is null!");
			return;
		}
		String md5Key = account.getMd5Key();
		String merchantCode = account.getMerchantCode();
		String logPre = "【出票结果<"+order_source+">】";
		
		
		if(orderInfo == null || StringUtils.isEmpty(orderInfo.getOrder_status())){
			logger.info(logPre+"阻止本次出票结果通知，订单状态为空，order_id="+order_id);
			return;
		}
		//通知qunar：0、出票成功通知 1、预定成功通知  2、先预定后支付
	//	String whenNotify = orderService.queryQunarSysSetting("out_notify_qunar");
		if(TrainConsts.OUT_SUCCESS.equals(orderInfo.getOrder_status()) 
				|| TrainConsts.BOOK_SUCCESS.equals(orderInfo.getOrder_status())){//出票成功 或者 预订成功
			opt = "CONFIRM";//协议-操作类型
			List<Map<String, String>> cpList = null;
			
			List<Map<String, Object>> tickets = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> ticket = null;
			if(StringUtils.isEmpty(order_type) || TrainConsts.ORDER_TYPE_COMMON.equals(order_type)){//普通订单
				cpList = this.queryOrderCpList(order_id);
				
				for(Map<String, String> cpInfo : cpList){
					ticket = new HashMap<String, Object>();
					ticket.put("seq", "0");
					ticket.put("ticketNo", cpInfo.get("out_ticket_billno"));
					ticket.put("seatType", cpInfo.get("qunar_seat_type"));
					ticket.put("seatNo", this.beautifySeatNo(cpInfo.get("seat_no")));
					ticket.put("price", Double.parseDouble(cpInfo.get("buy_money")));
					ticket.put("passengerName", cpInfo.get("user_name"));
					ticket.put("ticketType", TrainPropUtil.getQunarTicketType(cpInfo.get("ticket_type")));
					tickets.add(ticket);
				}
			}else{
				List<Map<String, String>> tripList = this.queryTripListByOrderId(order_id);
				
				for(Map<String, String> trip : tripList){
					if(!TrainConsts.OUT_SUCCESS.equals(trip.get("order_status")) 
							&& !TrainConsts.BOOK_SUCCESS.equals(trip.get("order_status"))){//出票成功 或者 预订成功
						logger.info(logPre+"阻止本次出票结果通知，联程订单状态为"+trip.get("order_status")+"，trip_id="+trip.get("trip_id"));
						return;
					}
					
					String trip_id = trip.get("trip_id");
					cpList = this.queryOrderCpList(trip_id);
	
					for(Map<String, String> cpInfo : cpList){
						ticket = new HashMap<String, Object>();
						ticket.put("seq", trip.get("trip_seq"));
						ticket.put("ticketNo", cpInfo.get("out_ticket_billno"));
						ticket.put("seatType", cpInfo.get("qunar_seat_type"));
						ticket.put("seatNo", this.beautifySeatNo(cpInfo.get("seat_no")));
						ticket.put("price", Double.parseDouble(cpInfo.get("buy_money")));
						ticket.put("passengerName", cpInfo.get("user_name"));
						ticket.put("ticketType", TrainPropUtil.getQunarTicketType(cpInfo.get("ticket_type")));
						tickets.add(ticket);
					}
				}
			}
			
			Map<String, Object> resultMap = new HashMap<String, Object>();//协议-票号
			resultMap.put("count", tickets.size());
			resultMap.put("tickets", JSONArray.fromObject(tickets));
			String result = JSONObject.fromObject(resultMap).toString();
			System.out.println(result);
			logger.info(logPre+"result="+result);
			
			comment = "已经出票成功";
			
			map = new HashMap<String,String>();
			map.put("merchantCode", merchantCode);
			map.put("orderNo", order_id);
			map.put("opt", opt);
			map.put("result", result);
			map.put("comment", comment);
			String hMac = DigestUtils.md5Hex(md5Key + merchantCode + order_id + opt + result + comment).toUpperCase();
			map.put("HMAC", hMac);
		
		}else if(TrainConsts.OUT_FAIL.equals(orderInfo.getOrder_status())){
			opt = "NO_TICKET";
			String reason = orderInfo.getOut_fail_reason();
			String passengerReason = orderInfo.getPassenger_reason();//乘车人信息错误2014-03-08 add
			
			if(TrainConsts.OUT_FAIL_REASON_0.equals(reason)){
				comment = "其他";
			}else if(TrainConsts.OUT_FAIL_REASON_1.equals(reason)){
				comment = "所购买的车次坐席已无票";
			}else if(TrainConsts.OUT_FAIL_REASON_2.equals(reason)){
				comment = "身份证件已经实名制购票，不能再次购买同日期同车次的车票";
			}else if(TrainConsts.OUT_FAIL_REASON_3.equals(reason)){
				comment = "qunar票价和12306不符";
			}else if(TrainConsts.OUT_FAIL_REASON_4.equals(reason)){
				comment = "车次数据与12306不一致";
			}else if(TrainConsts.OUT_FAIL_REASON_5.equals(reason)){
				comment = "乘客信息错误";
			}else if(TrainConsts.OUT_FAIL_REASON_6.equals(reason)){
				comment = "12306乘客身份信息核验失败";
			}else{
				/**为什么系统会返回其他错误状态码? 出票系统问题!*/
				reason="0";
				comment = "其他";
				passengerReason="";
			}
			//防止人为选错失败原因
			if(TrainConsts.OUT_FAIL_REASON_6.equals(reason)){//passengerReason必填
				if(StringUtils.isEmpty(passengerReason)){
					reason = TrainConsts.OUT_FAIL_REASON_0;
					comment = "其他";
					passengerReason = "";
				}
			}else if(TrainConsts.OUT_FAIL_REASON_2.equals(reason)){
				if(StringUtils.isEmpty(order_type) || TrainConsts.ORDER_TYPE_COMMON.equals(order_type)){//普通订单
					JSONArray arr=new JSONArray();
					List<Map<String, String>> cpList = null;
					cpList = this.queryOrderCpList(order_id);
					
					for(Map<String, String> cpInfo : cpList){
						JSONObject cp_json=new JSONObject();
						/*[{"certNo":"22222222",
							"certType":1,
							"name":"李四",
							"ticketType":1,
							"reason":"3",
							preDate:"2015-07-01",
							preTrainNo："G12"}]*/
						cp_json.put("certNo",cpInfo.get("user_ids"));//user_ids
						cp_json.put("certType", cpInfo.get("qunar_certtype"));//qunar_certtype
						cp_json.put("name",cpInfo.get("user_name"));//user_name
						cp_json.put("ticketType",TrainPropUtil.getQunarTicketType(cpInfo.get("ticket_type")));//ticket_type
						cp_json.put("reason", "3");
						String preDate=orderInfo.getFrom_time();
						//2015-06-27 10:08:00
						preDate=preDate.substring(0,preDate.indexOf(" "));
						cp_json.put("preDate",preDate);
						cp_json.put("preTrainNo",orderInfo.getTrain_no());
						arr.add(cp_json);
					}
					passengerReason=arr.toString();
					logger.info("passengerReason in OUT_FAIL_REASON_2:"+passengerReason);
				}else{
					reason = TrainConsts.OUT_FAIL_REASON_0;
					comment = "其他";
					passengerReason = "";
				}
			}else{
				passengerReason = "";
			}
			
			map = new HashMap<String,String>();
			map.put("merchantCode", merchantCode);
			map.put("orderNo", order_id);
			map.put("opt", opt);
			map.put("reason", reason);
			if(StringUtils.isEmpty(passengerReason)){
				map.put("passengerReason", passengerReason);
			}
			map.put("passengerReason", passengerReason);
			
			map.put("comment", comment);
			String hMac = DigestUtils.md5Hex(md5Key + merchantCode + order_id + opt + reason + passengerReason + comment).toUpperCase();
			map.put("HMAC", hMac);
		}
	
		if(map != null){
			String reqParams = UrlFormatUtil.CreateUrl("", map, "", "UTF-8");
			
			StringBuffer reqUrl = new StringBuffer();
			reqUrl.append(qunarReqUrl).append("ProcessPurchase.do");
			
			String jsonRs = HttpUtil.sendByPost(reqUrl.toString(), reqParams, "UTF-8");
			logger.info("出票结果通知返回："+jsonRs);
			ObjectMapper mapper = new ObjectMapper();
			QunarResult rs = mapper.readValue(jsonRs, QunarResult.class);
			
			if(rs.isRet()){
				logger.info(logPre+"通知qunar成功，order_id="+order_id);
				
				this.updateQunarOutNotifyEnd(order_id);
			}else{
				logger.info(logPre+"通知qunar失败，order_id="+order_id+"，系统将在约1分钟后重新通知");
				
				int notifyNum = this.queryOutTicketNotifyCount(order_id);
				if(notifyNum >= 5){
					this.updateOrderNotifyFail(order_id);
				}
				
				//添加日志
				Map<String, String> logMap = new HashMap<String, String>();
				logMap.put("order_id",order_id);
				logMap.put("content", "出票结果通知qunar失败，errCode="+rs.getErrCode()+"&errMsg="+rs.getErrMsg());
				logMap.put("opt_person", "qunar_app");
				this.addOrderInfoLog(logMap);
			}
		}
		
		}
	private String beautifySeatNo(String seatNo){
		//String seatNo = "02车厢车13D号 上铺";
		seatNo = seatNo.replace(" ", "").replaceAll(" ", "").replace("车厢车", "车");
		seatNo = seatNo.replace("车上", "车").replace("车下", "车");
	//	if(seatNo.startsWith("0")){
	//		seatNo = seatNo.substring(1);
	//	}
		System.out.println(seatNo);
		return seatNo;
	}

	@Override
	public void updateOrderWithCpNotify(Map<String, String> map) {
		orderDao.updateOrderWithCpNotify(map);
	}

	@Override
	public void updateBookNotifyPrepare(String order_id) {
		orderDao.updateBookNotifyPrepare(order_id);
	}

	@Override
	public List<Map<String, String>> queryOrderList(Map<String, Object> param) {
		return orderDao.queryOrderList(param);
	}

	@Override
	public List<Map<String, String>> queryPassengerList(Map<String, String> map) {
		return orderDao.queryPassengerList(map);
	}

	@Override
	public Integer queryOrderCount(Map<String, Object> param) {
		return orderDao.queryOrderCount(param);
	}
}
