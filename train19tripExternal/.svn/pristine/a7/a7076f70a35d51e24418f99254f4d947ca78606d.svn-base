package com.l9e.transaction.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.ReceiveNotifyService;
import com.l9e.transaction.vo.ExternalLogsVo;
import com.l9e.util.HttpUtil;
import com.l9e.util.UrlFormatUtil;

/**
 * 向EOP请求发货完成通知job
 * @author zhangjun
 *
 */
@Component("sendOutSysPayJob")
public class SendOutSysPayJob extends BaseController{
	
	private static final Logger logger = Logger.getLogger(SendOutSysPayJob.class);
	
	@Resource
	private OrderService orderService;
	
	@Resource
	private ReceiveNotifyService receiveService;
	/**
	 * 发货
	 */
	public void send(){
		List<Map<String, String>> sendList = receiveService.queryEopAndPayNotify();
		for (Map<String, String> sendMap : sendList) {
			ExternalLogsVo logs=new ExternalLogsVo();
			logs.setOrder_id(sendMap.get("order_id"));
			String merchant_id = receiveService.queryMerchantIdByOrderId(sendMap.get("order_id"));
			Map<String, String> merchantSetting = commonService.queryMerchantInfo(merchant_id);
			logger.info("orderid="+sendMap.get("order_id")+"merchant_id="+merchant_id +"book_flow="+ merchantSetting.get("book_flow"));
			boolean eopOrder = false;
			if("11".equals(sendMap.get("pay_type"))){
				if("11".equals(merchantSetting.get("book_flow"))){ 
					eopOrder = true;
					logger.info("eopOrder = true");
				}else{
					eopOrder = orderService.sendNotifyEop(sendMap);
					logger.info("eopOrder = orderService.sendNotifyEop(sendMap)");
				}
			}else if("22".equals(sendMap.get("pay_type"))){
				eopOrder = true;
			}
			if (eopOrder) {//发货完成则通知出票系统开始支付出票
				logger.info(sendMap.get("order_id")+"通知平台发货，并通知出票系统开始支付订单出票,金额："+sendMap.get("pay_money"));
				String return_str = "";
				try{
					Map<String, String> maps = new HashMap<String,String>();
			        maps.put("order_id", sendMap.get("order_id"));
			        maps.put("pay_money", sendMap.get("pay_money"));
			        String reqParams = UrlFormatUtil.CreateUrl("", maps, "", "UTF-8");
			        return_str = HttpUtil.sendByPost(this.getTrainSysSettingValue("notify_pay_interface_url", "ext_notify_pay_interface_url"),reqParams, "utf-8");
				}catch(Exception e){
					logger.error("通知平台发货失败，通知出票系统开始支付订单失败！", e);
					logs.setOrder_optlog("通知平台发货失败，通知出票系统开始支付订单失败！");
					logs.setOpter("ext_app");
					if("5".equals(sendMap.get("notify_nums"))){
						sendMap.put("notify_status", "33");
					}else{
						sendMap.put("notify_status", "11");
					}
				}
				if("success".equals(return_str)){
					logger.info("通知平台发货完成，并通知出票系统开始支付订单成功！");
					logs.setOrder_optlog("通知平台发货完成，并通知出票系统开始支付订单成功！");
					logs.setOpter("ext_app");
					receiveService.updateEopAndPayNotifyFinish(sendMap);
				}else{
					logs.setOrder_optlog("通知平台发货完成，通知出票系统开始支付订单失败！");
					logs.setOpter("ext_app");
					if("5".equals(sendMap.get("notify_nums"))){
						sendMap.put("notify_status", "33");
					}else{
						sendMap.put("notify_status", "11");
					}
					receiveService.updateEopAndPayNotifyNums(sendMap);
				}
			}else{
				logs.setOrder_optlog("通知平台发货失败，通知出票系统开始支付订单失败！");
				logs.setOpter("ext_app");
				if("5".equals(sendMap.get("notify_nums"))){
					sendMap.put("notify_status", "33");
				}else{
					sendMap.put("notify_status", "11");
				}
				receiveService.updateEopAndPayNotifyNums(sendMap);
			}
			orderService.insertOrderLogs(logs);
		}
	}
}
