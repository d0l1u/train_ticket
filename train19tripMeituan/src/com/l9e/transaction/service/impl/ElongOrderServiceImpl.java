package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.common.Consts;
import com.l9e.common.ElongConsts;
import com.l9e.transaction.dao.ElongNoticeDao;
import com.l9e.transaction.dao.ElongOrderDao;
import com.l9e.transaction.dao.NoticeDao;
import com.l9e.transaction.service.ElongOrderService;
import com.l9e.transaction.vo.DBNoticeVo;
import com.l9e.transaction.vo.ElongOrderInfo;
import com.l9e.transaction.vo.ElongOrderInfoCp;
import com.l9e.transaction.vo.ElongOrderLogsVo;
import com.l9e.transaction.vo.ElongOrderNoticeVo;
import com.l9e.transaction.vo.ElongPassengerInfo;
import com.l9e.util.HttpUtil;
import com.l9e.util.UrlFormatUtil;

/**
 * elong订单管理Service
 * */
@Service("elongOrderService")
public class ElongOrderServiceImpl implements ElongOrderService{
	private static final Logger logger = Logger.getLogger(ElongOrderServiceImpl.class);
	@Resource
	private ElongOrderDao elongOrderDao;
	
	@Resource
	private ElongNoticeDao elongNoticeDao;
	
	@Resource
	private NoticeDao noticeDao;
	
	@Override
	public int getOrderCount(String order_id) {
		return	elongOrderDao.queryOrderCount(order_id);
	}
	@Override
	public Map<String, Object> queryOrderInfo(String order_id) {
		return elongOrderDao.queryOrderInfo(order_id);
	}
	@Override
	public void insertRefundOrder(Map<String, String> paramMap) {
		elongOrderDao.insertRefundOrder(paramMap);
		
		ElongOrderLogsVo log=new ElongOrderLogsVo();
		log.setOpt_person("meituan_app");
		log.setContent("艺龙申请退款_成功["+paramMap.get("cp_id")+"]"+("11".equals(paramMap.get("refund_status"))?"2元及2元以下,直接置于退票成功":"")
		+("55".equals(paramMap.get("refund_status"))?"出票系统未出票成功,暂为预退票状态":"")		
		);
		log.setOrder_id(paramMap.get("order_id"));
		elongOrderDao.insertElongOrderLogs(log);
	}
	/**
	 * elong_通知出票系统接口
	 * */
	@Override
	public String send(ElongOrderInfo orderInfo, String type) {
		String order_id=orderInfo.getOrderId();
		try {
			String notify_cp_back_url="";
			if("book".equals(type)){
				notify_cp_back_url=Consts.NOTIFY_CP_ALLCHANNEL_BACK_URL;
			}else{
				notify_cp_back_url=Consts.NOTIFY_CP_BACK_URL;
			}
			String notify_cp_interface_url=Consts.NOTIFY_CP_INTERFACE_URL;
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("orderid", orderInfo.getOrderId());
			paramMap.put("paymoney", orderInfo.getSumTicketPrice());//车票总价
			paramMap.put("trainno", orderInfo.getTrainNo());
			//String fromcity=orderInfo.get("from_city").toString();
			//String tocity=orderInfo.get("to_city").toString();
			paramMap.put("fromcity",orderInfo.getDptStation());
			paramMap.put("tocity", orderInfo.getArrStation());
			paramMap.put("ordername",orderInfo.getDptStation()+"/"+orderInfo.getArrStation());
			String fromTime=orderInfo.getTrainStartTime();
			paramMap.put("fromtime",fromTime);
			paramMap.put("totime",  orderInfo.getTrainEndTime());
			paramMap.put("traveltime", fromTime.substring(0,fromTime.indexOf(" ")));
			paramMap.put("seattype", orderInfo.getSysSeatType());
			paramMap.put("outtickettype", "11");//
			paramMap.put("channel", "meituan");//合作商编号
			//购买了保险
			//paramMap.put("ext", "level|1");
			if("book".equals(type)){
				//先预订后支付
				paramMap.put("ispay","11");
			}else if("outTicket".equals(type)){
				//已经支付
				paramMap.put("ispay","00");
			}
			//备选坐席问题
			paramMap.put("extseattype", orderInfo.getExt_field1());
			List<ElongPassengerInfo> passengers=orderInfo.getPassengers();
			StringBuffer sb = new StringBuffer();
			for (ElongPassengerInfo cpInfo : passengers) {
				if(sb.length()>0) {
					sb.append("#");
				}
				sb.append(cpInfo.getOrderItemId()).append("|").append(cpInfo.getName()).append("|")
				  .append(cpInfo.getSysTicketType()).append("|").append(cpInfo.getSysCertType()).append("|")
				  .append(cpInfo.getCertNo()).append("|").append(cpInfo.getSeat_type()).append("|")
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
			logger.info("通知出票系统：" + orderInfo.getOrderId());
			String result = HttpUtil.sendByPost(notify_cp_interface_url, params, "UTF-8",5000,5000);
			logger.info(orderInfo.getOrderId()+"请求通知出票接口返回：" + result);
			
			logger.info(paramMap);
			if(!StringUtils.isEmpty(result)){
				String[] results = result.trim().split("\\|");
				if("success".equalsIgnoreCase(results[0]) && results.length == 2
						&& orderInfo.getOrderId().equals(results[1])){//通知成功
					Map<String,Object> map = new HashMap<String, Object>();
			        //通知出票系统成功则订单状态修改为正在出票  
			        map.put("order_id", orderInfo.getOrderId());
				    map.put("order_status", Consts.ELONG_ORDER_ING);//正在出票
				    elongOrderDao.updateOrderStatus(map);	
				    logger.info("通知出票系统成功，order_id=" + order_id);
				    return "Success";
				}else{//超时重发
					 logger.info("通知出票系统失败，order_id=" + order_id+"results:"+results);
					 return "Fail"+result;
				}
			}else{
				logger.info("通知出票系统超时，order_id=" + order_id);
				return "OutTime";
			}
		} catch (Exception e) {
			logger.info("通知出票系统异常", e);
			return "Exception";
		}
	}
	
	
	@Override
	public void addNotifyCpSys(Map<String, Object> orderInfo,
			List<Map<String, Object>> cpInfoList, String order_id,int num) {
		try {
			String notify_cp_back_url=Consts.NOTIFY_CP_BACK_URL;
			String notify_cp_interface_url=Consts.NOTIFY_CP_INTERFACE_URL;
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("orderid", order_id);
			paramMap.put("paymoney", orderInfo.get("pay_money").toString());//车票总价
			paramMap.put("trainno", orderInfo.get("train_no").toString());
			String fromcity=orderInfo.get("from_city").toString();
			String tocity=orderInfo.get("to_city").toString();
			paramMap.put("fromcity", fromcity);
			paramMap.put("tocity", tocity);
			paramMap.put("ordername",fromcity+"/"+tocity );
			String fromTime=orderInfo.get("from_time").toString();
			paramMap.put("fromtime",fromTime);
			paramMap.put("totime",  orderInfo.get("to_time").toString());
			paramMap.put("traveltime", fromTime.substring(0,fromTime.indexOf(" ")));
			paramMap.put("seattype", orderInfo.get("seat_type").toString());
			paramMap.put("outtickettype", "11");//
			paramMap.put("channel", "meituan");//合作商编号
			//购买了保险
			//paramMap.put("ext", "level|1");
			
			//备选坐席问题
			paramMap.put("extseattype", orderInfo.get("ext_field1").toString());
			
			StringBuffer sb = new StringBuffer();
			for (Map<String, Object> cpInfo : cpInfoList) {
				if(sb.length()>0) {
					sb.append("#");
				}
				sb.append(cpInfo.get("cp_id")).append("|").append(cpInfo.get("user_name")).append("|")
				  .append(cpInfo.get("ticket_type")).append("|").append(cpInfo.get("ids_type")).append("|")
				  .append(cpInfo.get("user_ids")).append("|").append(cpInfo.get("seat_type")).append("|")
				  .append(cpInfo.get("pay_money"));
			}
			paramMap.put("seattrains", sb.toString());
			paramMap.put("backurl", notify_cp_back_url);
			
			//站名的三字码
			String[] exts = orderInfo.get("ext_field2").toString().split("\\|");
			String from_station_code = exts[0];
			String to_station_code = exts[1];
			paramMap.put("fromCity3c", from_station_code);//订单中出发城市三字码
			paramMap.put("toCity3c", to_station_code);//订单中到达城市三字码
			
			String params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
			logger.info("通知出票系统：" + order_id);
			String result = HttpUtil.sendByPost(notify_cp_interface_url, params, "UTF-8");
			logger.info(order_id+"请求通知出票接口返回：" + result);
			
			logger.info(paramMap);
			if(!StringUtils.isEmpty(result)){
				String[] results = result.trim().split("\\|");
				if("success".equalsIgnoreCase(results[0]) && results.length == 2
						&& orderInfo.get("order_id").equals(results[1])){//通知成功
					Map<String,Object> map = new HashMap<String, Object>();
			        //通知出票系统成功则订单状态修改为正在出票  
			        map.put("order_id", order_id);
				    map.put("order_status", Consts.ELONG_ORDER_ING);//正在出票
				    elongOrderDao.updateOrderStatus(map);	
				    
				    noticeDbDo("SUCCESS",order_id,num);
					
				    logger.info("通知出票系统成功，order_id=" + order_id);
				}else{//超时重发
					 logger.info("通知出票系统失败，order_id=" + order_id+"results:"+results);
					noticeDbDo("FAIL",order_id,num);
				}
			}else{
				logger.info("通知出票系统超时，order_id=" + order_id);
				noticeDbDo("FAIL",order_id,num);
			}
		} catch (Exception e) {
			logger.info("通知出票系统异常", e);
			noticeDbDo("Exception",order_id,num);
		}
	}
	
	private void noticeDbDo(String status,String order_id,int num){
		/**通知表更新参数*/
		Map<String,Object> noticeInfo = new HashMap<String, Object>();
		noticeInfo.put("order_id",order_id);
		noticeInfo.put("cp_notify_time", "cp_notify_time");
	    noticeInfo.put("cp_notify_num", num+1);
	    
	    ElongOrderLogsVo log=new ElongOrderLogsVo();
		log.setOpt_person("meituan_app");
		log.setOrder_id(order_id);
		log.setContent("通知出票系统_"+("SUCCESS".equals(status)?"成功":
			"FAIL".equals(status)?"失败,将启用重发":"异常,将启用重发"
		));
		if("SUCCESS".equals(status)){
			 noticeInfo.put("cp_notify_status",Consts.NOTICE_OVER);
			 noticeInfo.put("cp_notify_finish_time", "cp_notify_finish_time");
		}else{
			noticeInfo.put("cp_notify_status",(num==Consts.CP_NOTICE_NUM-1)?Consts.NOTICE_FAIL:Consts.NOTICE_ING);
		}
		 elongNoticeDao.updateSysNotice(noticeInfo);
		 elongOrderDao.insertElongOrderLogs(log);
	}
	@Override
	public List<Map<String, Object>> querySendOrderCpsInfo(String order_id) {
		return elongOrderDao.querySendOrderCpsInfo(order_id);
	}
	@Override
	public void updateOrderInfo(Map<String, Object> map,List<Map<String, String>> cpMapList,boolean isNotNotice) {
		elongOrderDao.updateOrderInfo(map);
		String status=map.get("order_status").toString();
		/**插入订单操作 日记*/
		ElongOrderLogsVo log=new ElongOrderLogsVo();
		log.setOpt_person("meituan_app");
		log.setContent("出票系统返回结果_"+(Consts.ELONG_ORDER_MAKED.equals(status)?"预订成功":
			Consts.ELONG_ORDER_SUCCESS.equals(status)?"出票成功":
				Consts.ELONG_ORDER_FAIL.equals(status)?("出票失败[原因:"+ElongConsts.getFailReasonMessage(map.get("out_fail_reason").toString())+",乘客错误信息:"+map.get("passenger_reason")):
					"成功")+"");
		log.setOrder_id(map.get("order_id").toString());
		elongOrderDao.insertElongOrderLogs(log);
		if(cpMapList!=null){
			for(Map<String,String> cpInfo:cpMapList){
				cpInfo.put("order_id", map.get("order_id").toString());
				if(map.get("out_ticket_billno")!=null){
					cpInfo.put("out_ticket_billno",  map.get("out_ticket_billno").toString());
				}
				elongOrderDao.updateCpOrderInfo(cpInfo);
			}
		}
		/**针对出票成功 出票失败的 激活通知*/
		if(isNotNotice){
			Map<String,Object> noticeInfo = new HashMap<String, Object>();
			noticeInfo.put("order_id",map.get("order_id").toString());
			noticeInfo.put("out_notify_status", Consts.NOTICE_START);
			elongNoticeDao.updateSysNotice(noticeInfo);
		}
	}
	@Override
	public String queryRefundStatus(Map<String, String> paramMap) {
		return elongOrderDao.queryRefundStatus(paramMap);
	}
	@Override
	public Map<String, Object> queryrefundInfo(Map<String,String > params) {
		return elongOrderDao.queryrefundInfo(params);
	}
	@Override
	public void addOrder(ElongOrderInfo orderInfo) {
		List<ElongPassengerInfo> passengers=orderInfo.getPassengers();
		elongOrderDao.addOrderInfo(orderInfo);
		for(ElongPassengerInfo p:passengers){
			elongOrderDao.addPassengerInfo(p);
		}
		
	}
	@Override
	public void updateRefundStatus(Map<String, String> paramMap) {
		elongOrderDao.updateRefundStatus(paramMap);
	}
	@Override
	public String queryCpPayMoney(Map<String, String> paramMap) {
		return elongOrderDao.queryCpPayMoney(paramMap);
	}
	@Override
	public String queryNoticeStatusByOrderId(String orderId) {
		return elongNoticeDao.queryNoticeStatusByOrderId(orderId);
	}
	@Override
	public void insertElongOrderLogs(ElongOrderLogsVo log) {
		elongOrderDao.insertElongOrderLogs(log);
	}
	@Override
	public void addOfflineRefund(Map<String, String> params) {
		elongOrderDao.addOfflineRefund(params);
	}
	@Override
	public List<ElongOrderInfoCp> queryOrderCpInfo(String orderId) {
		return elongOrderDao.queryOrderCpInfo(orderId);
	}
	@Override
	public String queryCpid(Map<String, String> pMap) {
		return elongOrderDao.queryCpid(pMap);
	}
	
	
}
