package com.l9e.transaction.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.ExternalLogsVo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.util.DateUtil;
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
	
	@Resource
	private CommonService commonService;
	/**
	 * 发货
	 */
	public void send(){
		List<Map<String, String>> sendList = orderService.queryTimedSendList();
		for (Map<String, String> sendMap : sendList) {
			Map<String,String> merchantSetting = commonService.queryMerchantInfo(sendMap.get("merchant_id"));
			OrderInfo orderInfo=orderService.queryOrderInfo(sendMap.get("order_id"));
			String  order_type=orderInfo.getOrder_type();
			logger.info("订单号："+orderInfo.getOrder_id()+"订单类型:"+order_type);//order_type,11.先下单后付款,22.先支付后预定
			
			if ("11".equals(order_type)) {//增加一个新的分支
				this.notifyCpSys("11",sendMap.get("order_id"),"11"); //book_flow, 00:先支付后出票；11：先预定后支付
			}else if("22".equals(order_type)){
				//原有流程不做改动
				if("11".equals(merchantSetting.get("pay_type")) && "00".equals(merchantSetting.get("book_flow"))){
					if (orderService.sendNotifyEop(sendMap)) {//发货完成则通知出票系统出票
						this.notifyCpSys("12",sendMap.get("order_id"),merchantSetting.get("book_flow"));
					}
				}else if("22".equals(merchantSetting.get("pay_type")) ||("11".equals(merchantSetting.get("pay_type")) && "11".equals(merchantSetting.get("book_flow")))){
					    this.notifyCpSys("11",sendMap.get("order_id"),merchantSetting.get("book_flow"));
				}
			}
		}
	}
	
	/**
	 * 通知出票系统
	 * @param order_id
	 */
	private void notifyCpSys(String old_status,String order_id,String book_flow){
		try{
			Map<String, String> orderMap = orderService.queryNotifyCpOrderInfo(order_id);		
		
			//查询发货的订单
			List<Map<String, String>> cpInfoList = orderService.queryCpInfoList(order_id);
			
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("orderid", orderMap.get("order_id"));
			paramMap.put("ordername", orderMap.get("order_name"));
			paramMap.put("paymoney", orderMap.get("ticket_pay_money"));//车票总价
			paramMap.put("trainno", orderMap.get("train_no"));
			paramMap.put("fromcity", orderMap.get("from_station"));
			paramMap.put("tocity", orderMap.get("arrive_station"));
			paramMap.put("fromtime", orderMap.get("travel_time") + " "+orderMap.get("from_time"));
			paramMap.put("totime",  DateUtil.dateToString(new Date(),DateUtil.DATE_FMT1) + " "+orderMap.get("arrive_time"));
			paramMap.put("traveltime", orderMap.get("travel_time"));
			paramMap.put("seattype", orderMap.get("seat_type"));
			paramMap.put("outtickettype", orderMap.get("out_ticket_type"));
			String channel =orderMap.get("merchant_id");
			if (TrainConsts.GT_MERCHANT_ID_TEST.equals(channel)) {//测试的id
				paramMap.put("channel", TrainConsts.GT_MERCHANT_ID);//合作商编号
			}else if(TrainConsts.GT_MERCHANT_ID.equals(channel)){//正式的id
				paramMap.put("channel", TrainConsts.GT_MERCHANT_ID);//合作商编号
			}
			
			paramMap.put("ispay", book_flow);	//是否支付；00：是；11：否
			
			if("1".equals(orderMap.get("isChooseSeats"))) {//是否选座：1 :选  0： 不选
				paramMap.put("choose_seats", orderMap.get("chooseSeats"));// 选座信息(选座个数要和乘客数量一致) //出票系统接收
			}
			
			logger.info("order_id:"+orderMap.get("order_id")+",isChooseSeats:"+orderMap.get("isChooseSeats")+",choose_seats:"+orderMap.get("chooseSeats"));
			
			//购买了保险
			if(!StringUtils.isEmpty(orderMap.get("bx_pay_money")) 
					&& Double.parseDouble(orderMap.get("bx_pay_money"))>0){
				paramMap.put("ext", "level|1");
			}
			if(!StringUtils.isEmpty(orderMap.get("order_level"))
					&& "1".equals(orderMap.get("order_level"))){//VIP订单
				paramMap.put("ext", "level|2");
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
				    map.put("old_status", old_status);//支付完成或eop发货成功
			        orderService.updateOrderStatus(map);
					logger.info("通知出票系统发货成功，order_id=" + order_id);
					
					ExternalLogsVo logs=new ExternalLogsVo();
					logs.setOrder_id(order_id);
					logs.setOrder_optlog("通知出票系统成功");
					logs.setOpter("ext_app");
					orderService.insertOrderLogs(logs);
				}else{//更新超时重发
					logger.info("通知出票系统发货失败，即将启动超时重发，order_id=" + order_id);
					Map<String, String> map = new HashMap<String, String>();
			        map.put("asp_order_id", order_id);
				    map.put("order_status", TrainConsts.PAY_SUCCESS);//支付成功
				    map.put("old_status", TrainConsts.EOP_SEND);//EOP发货
			        orderService.updateOrderStatus(map);	
				}
			}else{
				logger.info("通知出票系统发货失败，即将启动超时重发，order_id=" + order_id);
				Map<String, String> map = new HashMap<String, String>();
		        map.put("asp_order_id", order_id);
			    map.put("order_status", TrainConsts.PAY_SUCCESS);//支付成功
			    map.put("old_status", TrainConsts.EOP_SEND);//EOP发货
		        orderService.updateOrderStatus(map);	
			}
		}catch (Exception e){//发生异常则更新超时重发
			logger.info("通知出票系统异常", e);
			logger.info("通知出票系统异常，即将启动超时重发，order_id=" + order_id);
		}

	}

}
