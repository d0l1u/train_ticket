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

import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.UrlFormatUtil;

@Component("sendRefundJob")
public class SendRefundJob {
	private static final Logger logger = Logger.getLogger(SendRefundJob.class);
	
	@Resource
	private OrderService orderService;
	
	@Value("#{propertiesReader[notify_refund_interface_url]}")
	private String notify_refund_interface_url;
	
	@Value("#{propertiesReader[notify_refund_back_url]}")
	private String notify_refund_back_url;
	
	/**
	 * 给cp_orderinfo_refund表添加退款数据
	 */
	public void sendRefund(){
		List<Map<String, String>> sendList = orderService.queryCanRefundStreamList();
		for (Map<String, String> sendMap : sendList) {
			if(!StringUtils.isEmpty(sendMap.get("order_id"))){
				this.notifyRefundSys(sendMap);
			}
		}
	}
	
	//给退款接口传参
	private void notifyRefundSys(Map<String, String> sendMap){
		String order_id = sendMap.get("order_id");
		String cp_id = sendMap.get("cp_id");
		logger.info("退款开始~~~订单号："+order_id+"，车票号"+cp_id);
		try{
			Map<String, String> param = new HashMap<String, String>();
			param.put("order_id", order_id);
			param.put("cp_id", cp_id);
			
			Map<String, String> orderMap = orderService.queryRefundCpOrderInfo(param);
			Map<String, String> accountMap = orderService.queryAccountOrderInfo(param);
			if(null == accountMap ){
				accountMap = orderService.queryAccountOrderBackupInfo(param);
			}
			
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("orderid", order_id);
			paramMap.put("cpid", cp_id);
			paramMap.put("trainno", orderMap.get("train_no"));//车票总价
			paramMap.put("fromstation", orderMap.get("from_city"));
			paramMap.put("arrivestation", orderMap.get("to_city"));
			paramMap.put("traveltime", orderMap.get("travel_time"));
			paramMap.put("fromtime", orderMap.get("from_time"));
			paramMap.put("buymoney", orderMap.get("buy_money"));
			paramMap.put("refundmoney", orderMap.get("refund_money"));
			paramMap.put("username", orderMap.get("user_name"));
			paramMap.put("tickettype", orderMap.get("ticket_type"));
			paramMap.put("idstype", orderMap.get("ids_type"));
			paramMap.put("userids", orderMap.get("user_ids"));
			paramMap.put("seattype", orderMap.get("seat_type"));
			paramMap.put("trainbox", orderMap.get("train_box"));
			paramMap.put("seatno", orderMap.get("seat_no"));
			paramMap.put("outticketbillno", orderMap.get("out_ticket_billno"));
			paramMap.put("outtickettime", orderMap.get("out_ticket_time"));
			paramMap.put("channel", "qunar");
			paramMap.put("backurl", notify_refund_back_url);// 回调地址
			paramMap.put("accountname", accountMap.get("acc_username"));
			paramMap.put("accountpwd", accountMap.get("acc_password"));
			paramMap.put("refundseq", sendMap.get("refund_seq"));
			paramMap.put("userremark", orderMap.get("user_remark"));
			logger.info("paramMap:"+paramMap);
			
			String params = UrlFormatUtil.CreateUrl("", paramMap, "", "UTF-8");
			
			String result = HttpUtil.sendByPost(notify_refund_interface_url, params, "UTF-8");
			logger.info("请求通知退票接口返回：" + result);
			
			if(!StringUtils.isEmpty(result)){
				String[] results = result.trim().split("\\|");
				
				if("success".equalsIgnoreCase(results[0]) && results.length == 2
						&& order_id.equals(results[1])){//通知成功
					Map<String, String> map = new HashMap<String, String>();
			        //通知退票系统成功则订单状态修改为02开始机器改签
			        map.put("order_id", order_id);
				    map.put("order_status", "02");//开始机器改签
			        orderService.updateOrderRefundStatus(map);			        
					logger.info("通知退票系统成功，order_id=" + order_id + "，cp_id=" + cp_id);
				}else{//通知退票系统失败则订单状态修改为03 人工改签
					logger.info("通知退票系统失败，order_id=" + order_id + "，cp_id=" + cp_id);
				}
			}
		}catch (Exception e){//发生异常则更新超时重发
			e.printStackTrace();
			OrderInfo orderInfo = orderService.queryOrderInfoById(order_id);
			Map<String, String> param = new HashMap<String, String>();
			param.put("order_id", order_id);
			param.put("cp_id", cp_id);
			Map<String, String> accountMap = orderService.queryAccountOrderInfo(param);
			if(null==orderInfo || null==accountMap){
				Map<String, String> map = new HashMap<String, String>();
		        //通知退票系统成功则订单状态修改为03人工改签
		        map.put("order_id", order_id);
			    map.put("order_status", "03");//人工改签
		        orderService.updateOrderRefundStatus(map);	
		        
		        orderService.addOrderInfoByBackup(order_id);
		        orderService.addOrderCpInfoByBackup(order_id);
		      //添加日志
				Map<String, String> logMap = new HashMap<String, String>();
				logMap.put("order_id", order_id);
				logMap.put("content", DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3)+"该订单已过期，只能人工处理！");
				logMap.put("opt_person", "qunar_app");
				orderService.addOrderInfoLog(logMap);
			}
			logger.info("通知退票系统异常，order_id=" + order_id + "，cp_id=" + cp_id);
		}
		logger.info("退款结束~~~订单号："+order_id+"，车票号"+cp_id);
	}
}
