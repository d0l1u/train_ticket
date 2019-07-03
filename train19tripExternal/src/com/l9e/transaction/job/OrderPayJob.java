package com.l9e.transaction.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.ReceiveNotifyService;
import com.l9e.transaction.vo.ExternalLogsVo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.HttpsUtil;
import com.l9e.util.Md5Encrypt;


/**
 * 向合作商户发送订单平台支付结果通知job
 * @author zuoyuxing
 *
 */
@Component("orderPayJob")
@Deprecated
public class OrderPayJob {
	private static final Logger logger = Logger.getLogger(OrderPayJob.class);
	@Resource
	ReceiveNotifyService receiveService;
	
	@Resource
	private OrderService orderService;
	
	@Resource
	private CommonService commonService;
	
	public void sendPayResult(){
		String timestamp = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT2);
		List<Map<String,String>> listNotify = receiveService.findOrderPayNotify();
		if(listNotify !=null && listNotify.size()>0 ){
			for(Map<String,String> map : listNotify){
				String order_id = "";
				JSONObject json_param = new JSONObject();
				order_id = map.get("order_id");
				logger.info("发起订单支付结果通知！！订单号："+order_id);
				//更新订单预定结果通知表通知开始时间和通知次数
				receiveService.updatePayReturnNotifyNums(order_id);
				
				OrderInfo order = orderService.queryOrderInfo(order_id);
				//拼接请求参数
				Map<String,String> merchantInfoMap = orderService.queryMerchantInfoByOrderId(order_id);
				Map<String,String> merchantSetting = commonService.queryMerchantInfo(merchantInfoMap.get("merchant_id"));
				String md5_str = merchantInfoMap.get("merchant_id")+timestamp+merchantInfoMap.get("merchant_version")+json_param.toString();
				String hmac = Md5Encrypt.getKeyedDigestFor19Pay(md5_str+merchantSetting.get("sign_key"), "", "utf-8");
				StringBuffer params = new StringBuffer();
				params.append("merchant_id=").append(merchantInfoMap.get("merchant_id"));
				params.append("&timestamp=").append(timestamp);
				params.append("&version=").append(merchantInfoMap.get("merchant_version"));
				params.append("&merchant_order_id=").append(order.getMerchant_order_id());
				params.append("&pay_number=").append(map.get("pay_number"));
				if("11".equals(map.get("pay_status"))){
					params.append("&pay_status=SUCCESS");
					params.append("&fail_reason=");
				}else{
					params.append("&pay_status=FAILURE");
					params.append("&fail_reason=").append(map.get("fail_reason"));
				}
				
				params.append("&order_id=").append(order_id);
				params.append("&hmac=").append(hmac);
				logger.info("订单支付结果异步通知参数："+params);
				String url = order.getPay_result_url();
				logger.info("url:"+url);
				String result = "";
				try {
					if(url.contains("https")){
						result = HttpsUtil.sendHttps(url+"?"+params.toString());
					}else{
						result = HttpUtil.sendByPost(url, params.toString(), "utf-8");
					}
				} catch (Exception e) {
					logger.error("将订单支付结果通知合作商户url异常",e);
				}
				logger.info("result="+result);
				//用户接收并处理完成后，更新通知状态为完成
				Map<String,String> map_finish = new HashMap<String,String>();
				map_finish.put("order_id", order_id);
				if("SUCCESS".equals(result.trim())){
					map_finish.put("notify_status", "22");
					receiveService.updateOrderPayNotifyFinish(map_finish);
					
					ExternalLogsVo logs=new ExternalLogsVo();
					logs.setOrder_id(order_id);
					logs.setOrder_optlog("通知商户成功[支付结果异步通知]");
					logs.setOpter("ext_app");
					orderService.insertOrderLogs(logs);
				}else{
					receiveService.updateOrderPayNotifyFinish(map_finish);
					map_finish.put("notify_status", "44");
					receiveService.updateOrderPayNotifyFinish(map_finish);
					
					ExternalLogsVo logs=new ExternalLogsVo();
					logs.setOrder_id(order_id);
					logs.setOrder_optlog("通知商户失败[支付结果异步通知]");
					logs.setOpter("ext_app");
					orderService.insertOrderLogs(logs);
				}
			}
		}
	}
}
