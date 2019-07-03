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
import com.l9e.util.HttpUtil;
import com.l9e.util.UrlFormatUtil;

/**
 * 向EOP请求发货完成通知job
 * @author zhangjun
 *
 */
@Component("sendJob")
public class SendJob {
	
	private static final Logger logger = Logger.getLogger(SendJob.class);
	
	@Resource
	private OrderService orderService;
	
	@Value("#{propertiesReader[notify_cp_interface_url]}")
	private String notify_cp_interface_url;
	
	@Value("#{propertiesReader[notify_cp_back_url]}")
	private String notify_cp_back_url;
	
	/**
	 * 通知EOP发货，发货完成则通知出票系统出票
	 */
	public void send(){
		List<Map<String, String>> sendList = orderService.queryTimedSendList();
		for (Map<String, String> sendMap : sendList) {
			this.notifyCpSys(sendMap.get("order_id"));
		}
	}
	
	/**
	 * 通知出票系统
	 * @param order_id
	 */
	private void notifyCpSys(String order_id){
		try{
			Map<String, String> orderMap = orderService.queryNotifyCpOrderInfo(order_id);		
			
			//查询EOP发货的订单
			List<Map<String, String>> cpInfoList = orderService.queryCpInfoList(order_id);
			
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("orderid", orderMap.get("order_id"));
			paramMap.put("ordername", orderMap.get("order_name"));
			paramMap.put("paymoney", orderMap.get("ticket_pay_money"));//车票总价
			paramMap.put("trainno", orderMap.get("train_no"));
			paramMap.put("fromcity", orderMap.get("from_city"));
			paramMap.put("tocity", orderMap.get("to_city"));
			paramMap.put("fromtime", orderMap.get("from_time"));
			paramMap.put("totime", orderMap.get("to_time"));
			paramMap.put("traveltime", orderMap.get("travel_time"));
			paramMap.put("seattype", orderMap.get("seat_type"));
			paramMap.put("outtickettype", orderMap.get("out_ticket_type"));
			paramMap.put("channel", "ccb");
			//购买了保险
			if(!StringUtils.isEmpty(orderMap.get("bx_pay_money")) 
					&& Double.parseDouble(orderMap.get("bx_pay_money"))>0){
				paramMap.put("ext", "level|1");//#
			}
			
			StringBuffer sb = new StringBuffer();
			for (Map<String, String> cpInfo : cpInfoList) {
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
			
			//硬座备选无座
			if(!StringUtils.isEmpty(orderMap.get("ext_seat"))){
				StringBuffer extSb = new StringBuffer();
				extSb.append(orderMap.get("seat_type"));
				extSb.append("#").append(orderMap.get("ext_seat"));
				paramMap.put("extseattype", extSb.toString());
			}
	
			String params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
			
			String result = HttpUtil.sendByPost(notify_cp_interface_url, params, "UTF-8");
			logger.info("请求通知出票接口返回：" + result);
			
			logger.info(paramMap);
			
			if(!StringUtils.isEmpty(result)){
				String[] results = result.trim().split("\\|");
				
				if("success".equalsIgnoreCase(results[0]) && results.length == 2
						&& order_id.equals(results[1])){//通知成功
					Map<String, String> map = new HashMap<String, String>();
			        //通知出票系统成功则订单状态修改为正在出票
			        map.put("asp_order_id", order_id);
				    map.put("order_status", TrainConsts.BOOKING_TICKET);//正在预订
				    map.put("old_status", TrainConsts.PAY_SUCCESS);//支付完成
			        orderService.updateOrderStatus(map);			        
					logger.info("通知出票系统发货成功，order_id=" + order_id);
				}else{//更新超时重发
					logger.info("通知出票系统发货失败，即将启动超时重发，order_id=" + order_id);
//					Map<String, String> map = new HashMap<String, String>(2);
//					map.put("timeout", "1");
//					map.put("order_id", order_id);
//					orderService.updateOrderTimeOut(map);
				}
			}
		}catch (Exception e){//发生异常则更新超时重发
			logger.info("通知出票系统异常", e);
			logger.info("通知出票系统异常，即将启动超时重发，order_id=" + order_id);
//			Map<String, String> map = new HashMap<String, String>(2);
//			map.put("timeout", "1");
//			map.put("order_id", order_id);
//			orderService.updateOrderTimeOut(map);
		}

	}

}
