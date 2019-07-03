package com.l9e.transaction.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoTrip;
import com.l9e.util.HttpUtil;
import com.l9e.util.UrlFormatUtil;

@Component("cpSysNotifyJob")
public class CpSysNotifyJob {
	
	private static final Logger logger = Logger.getLogger(CpSysNotifyJob.class);
	@Resource
	private OrderService orderService;
	
	private String notify_cp_interface_url;
	
	private String notify_cp_back_url;
	
	@Value("#{propertiesReader[notify_cp_interface_url]}")
	public void setNotify_cp_interface_url(String notify_cp_interface_url) {
		this.notify_cp_interface_url = notify_cp_interface_url;
	}
	
	@Value("#{propertiesReader[notify_cp_back_url]}")
	public void setNotify_cp_back_url(String notify_cp_back_url) {
		this.notify_cp_back_url = notify_cp_back_url;
	}

	public void cpNotify() throws Exception {
		List<Map<String, String>> orders = orderService.queryTimedCpSysList();
		OrderInfo orderInfo = null;
		String order_type = null;//订单类别
		
		Map<String, String> paramMap = null;
		//通知qunar：0、出票成功通知 1、预定成功通知  2、先预定后支付
		for (Map<String, String> orderMap : orders) {
			order_type = orderMap.get("order_type");
			
			if(StringUtils.isEmpty(order_type) || TrainConsts.ORDER_TYPE_COMMON.equals(order_type)){//普通订单
				orderInfo = orderService.queryOrderInfoById(orderMap.get("order_id"));
				logger.info("通知去哪order_id:"+orderMap.get("order_id"));
				if(!TrainConsts.PAY_SUCCESS.equals(orderInfo.getOrder_status())){
					logger.info("【出票系统接口】订单状态不是支付成功,过滤该条数据，order_id="+orderInfo.getOrder_id()
							+"&order_status="+orderInfo.getOrder_status());
					continue;
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
				
//				if("2".equals(whenNotify)){
//					paramMap.put("is_pay", "11");
//				}else{
//					paramMap.put("is_pay", "00");
//				}
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
				trip = orderService.queryTripOrderInfoById(orderMap.get("order_id"));
				
				if(!TrainConsts.PAY_SUCCESS.equals(trip.getOrder_status())){
					logger.info("【出票系统接口】联程订单状态不是支付成功,过滤该条数据，trip_id="+trip.getTrip_id()
							+"&order_status="+trip.getOrder_status());
					continue;
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
				
//				if("2".equals(whenNotify)){
//					paramMap.put("is_pay", "11");
//				}else{
//					paramMap.put("is_pay", "00");
//				}
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
		cpInfoList = orderService.queryCpInfoList(corder_id);
		orderService.updateCpSysNotifyBegin(corder_id);
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
				orderService.updateCpSysOutNotifyEnd(corder_id, order_id);
			}else{
				logger.info("【出票系统接口】通知出票系统失败，order_id="+ corder_id + "，系统将在约1分钟后重新通知");
				//添加日志
				Map<String, String> logMap = new HashMap<String, String>();
				logMap.put("order_id", order_id);
				logMap.put("content", "通知出票系统失败，"+corder_id);
				logMap.put("opt_person", "qunar_app");
				orderService.addOrderInfoLog(logMap);
			}
		}
	}
	
	
	public static void main(String[] args) {
		String str = "K7388/K7389";
		String str2 = "K7388";
		System.out.println(str.split("/")[1]);
		System.out.println(str2.split("/")[0]);
	}

}
