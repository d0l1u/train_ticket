package com.l9e.transaction.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jiexun.iface.bean.DeliverBean;
import com.jiexun.iface.util.ASPUtil;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.JoinUsService;
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
	
	@Resource
	private JoinUsService joinUsService;
	
	@Value("#{propertiesReader[ASP_ID]}")
	private String asp_id;
	
	@Value("#{propertiesReader[ASP_VERIFY_KEY]}")
	private String asp_verify_key;//验签key
	
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
			if (this.sendNotifyEop(sendMap)) {//发货完成则通知出票系统出票
				if(!StringUtils.isEmpty(sendMap.get("asp_order_type"))
						&& "hc".equals(sendMap.get("asp_order_type"))){
					this.notifyCpSys(sendMap.get("order_id"));
				}
			}
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
				    map.put("old_status", TrainConsts.EOP_SEND);//EOP发货
			        orderService.updateOrderStatus(map);			        
					logger.info("通知出票系统发货成功，order_id=" + order_id);
				}else{//更新超时重发
					logger.info("通知出票系统发货失败，即将启动超时重发，order_id=" + order_id);
					Map<String, String> map = new HashMap<String, String>(2);
					map.put("timeout", "1");
					map.put("order_id", order_id);
					orderService.updateOrderTimeOut(map);
				}
			}
		}catch (Exception e){//发生异常则更新超时重发
			e.printStackTrace();
			logger.info("通知出票系统异常，即将启动超时重发，order_id=" + order_id);
			Map<String, String> map = new HashMap<String, String>(2);
			map.put("timeout", "1");
			map.put("order_id", order_id);
			orderService.updateOrderTimeOut(map);
		}

	}
	
	/**
	 * 通知EOP发货成功
	 * @param map
	 * @return
	 */
	private boolean sendNotifyEop(Map<String, String> map){
		DeliverBean bean = new DeliverBean();			
		
		bean.setAsp_verify_key(asp_verify_key);	
		bean.setPartner_id(asp_id);	
		bean.setAsp_order_id(map.get("order_id"));	
		bean.setEop_order_id(map.get("eop_order_id"));
		bean.setSend_result("SUCCESS");//全部成功	
		if(!StringUtils.isEmpty(map.get("asp_order_type"))
				&& "hc".equals(map.get("asp_order_type"))){
			bean.setRemark("火车票发货通知");
		}else{
			bean.setRemark("火车票加盟发货通知");
		}
			
		StringBuffer sendNotifyUrl = new StringBuffer();
		sendNotifyUrl.append(map.get("send_notify_url"))
				   .append((map.get("send_notify_url").indexOf("?")!=-1) ? "&data_type=JSON" : "?data_type=JSON");
		
		ASPUtil.sendResultInform(bean, sendNotifyUrl.toString());
		/** hc:火车票订单 jm:加盟订单 jmxf_sys:主动续费 jmxf_hum:被动续费**/
		if("SUCCESS".equalsIgnoreCase(bean.getResult_code())){		    
	        logger.info("【发货结果通知EOP】EOP发货成功，eopOrderId=" + map.get("eop_order_id"));
			if(!StringUtils.isEmpty(map.get("asp_order_type"))
					&& "hc".equals(map.get("asp_order_type"))){
		        //订单状态修改为通知EOP发货中
		        map.put("asp_order_id", map.get("order_id"));
			    map.put("order_status", TrainConsts.EOP_SEND);//EOP发货
			    map.put("old_status", TrainConsts.PAY_SUCCESS);//支付成功
		        orderService.updateOrderStatus(map);
			}else if("jm".equals(map.get("asp_order_type"))){//加盟首次付款
		        map.put("asp_order_id", map.get("order_id"));
			    map.put("order_status", "12");//EOP发货完成
			    map.put("old_status", TrainConsts.PAY_SUCCESS);//支付成功
			    joinUsService.updateJmOrderStatus(map);
			}else if("jmxf_sys".equals(map.get("asp_order_type"))){//加盟续费 用户状态直接改为审核通过
		        map.put("asp_order_id", map.get("order_id"));
			    map.put("order_status", "12");//EOP发货完成
			    map.put("old_status", TrainConsts.PAY_SUCCESS);//支付成功
			    map.put("estate", TrainConsts.AGENT_ESTATE_PASSED);//审核通过
			    map.put("old_estate", TrainConsts.AGENT_ESTATE_NEED_REPAY);//需要续费
			    joinUsService.updateAgentJmSysXf(map);
			}else if("jmxf_hum".equals(map.get("asp_order_type"))){
		        map.put("asp_order_id", map.get("order_id"));
			    map.put("order_status", "12");//EOP发货完成
			    map.put("old_status", TrainConsts.PAY_SUCCESS);//支付成功
			    map.put("estate", TrainConsts.AGENT_ESTATE_PASSED);//审核通过
			    map.put("old_estate", TrainConsts.AGENT_ESTATE_PASSED);//审核通过
			    joinUsService.updateAgentJmHumXf(map);
			}
	        return true;
		}else{
	        logger.info("【发货结果通知EOP】EOP发货失败：eopOrderId=" + map.get("eop_order_id") + "，失败原因：" + bean.getResult_desc());
	        return false;
		}
	}

}
