package com.l9e.transaction.job;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
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
 * 向合作商户发送订单预定成功通知job
 * @author zuoyuxing
 *
 */
@Component("orderBookJob")
public class OrderBookJob {
	private static final Logger logger = Logger.getLogger(OrderBookJob.class);
	@Resource
	ReceiveNotifyService receiveService;
	
	@Resource
	private OrderService orderService;
	
	@Resource
	private CommonService commonService;
	
	public void sendMerchantBookData(){
		String timestamp = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT2);
		List<Map<String,String>> listNotify = receiveService.findOrderBookNotify();
		JSONArray orderUsers = null;
		if(listNotify !=null && listNotify.size()>0 ){
			for(Map<String,String> map : listNotify){
				String order_id = "";
				JSONObject json_param = new JSONObject();
				order_id = map.get("order_id");
				logger.info("发起订单预定结果通知！！订单号："+order_id);
				//更新订单预定结果通知表通知开始时间和通知次数
				receiveService.updateOrderBookNotifyStartNum(order_id);
				//添加开始通知日志
				ExternalLogsVo logs=new ExternalLogsVo();
				logs.setOrder_id(order_id);
				logs.setOrder_optlog("发起订单预定结果通知！！订单号："+order_id);
				logs.setOpter("xc_app");
				orderService.insertOrderLogs(logs);
				
				orderUsers = orderService.updateBookSuccessInfo("query",order_id,map.get("merchant_id"));
				
				OrderInfo order = orderService.queryOrderInfo(order_id);
				
				json_param.put("merchant_order_id", order.getMerchant_order_id());
				json_param.put("merchant_money", order.getPay_money());
				json_param.put("bx_pay_money", order.getBx_pay_money());
				json_param.put("ticket_pay_money", order.getTicket_pay_money());
				json_param.put("order_id", order_id);
				json_param.put("out_ticket_billno", order.getOut_ticket_billno()==null ?"":order.getOut_ticket_billno());
				json_param.put("out_ticket_time", order.getOut_ticket_time());
				json_param.put("spare_pro1", "");
				json_param.put("spare_pro2", "");
				json_param.put("order_userinfo", orderUsers.toString());
				//拼接请求参数
				Map<String,String> merchantInfoMap = orderService.queryMerchantInfoByOrderId(order_id);
				Map<String,String> merchantSetting = commonService.queryMerchantInfo(merchantInfoMap.get("merchant_id"));
				String md5_str = merchantInfoMap.get("merchant_id")+timestamp+merchantInfoMap.get("merchant_version")+json_param.toString();
				String hmac = Md5Encrypt.getKeyedDigestFor19Pay(md5_str+merchantSetting.get("sign_key"), "", "utf-8");
				StringBuffer params = new StringBuffer();
				try {
					params.append("merchant_id=").append(merchantInfoMap.get("merchant_id"))
						.append("&timestamp=").append(timestamp)
						.append("&version=").append(merchantInfoMap.get("merchant_version"))
						.append("&json_param=").append(URLEncoder.encode(json_param.toString(), "utf-8"))
						.append("&hmac=").append(hmac);
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				logger.info("订单预定结果异步通知参数："+params.toString());
				String url = map.get("notify_url");
				logger.info("url:"+url);
				String result = "";
				try {
					if(url.contains("https")){
						result = HttpsUtil.sendHttps(url+"?"+params.toString());
					}else{
						result = HttpUtil.sendByPost(url, params.toString(), "utf-8");
					}
				} catch (Exception e) {
					logger.error("将订单预定结果通知合作商户url异常",e);
				}
				logger.info("result="+result);
				//用户接收并处理完成后，更新通知状态为完成
				if("SUCCESS".equals(result.trim())){
					receiveService.updateOrderBookNotifyFinish(order_id);
					
//					ExternalLogsVo logs=new ExternalLogsVo();
//					logs.setOrder_id(order_id);
					logs.setOrder_optlog("通知商户成功[预定结果通知]");
//					logs.setOpter("xc_app");
					orderService.insertOrderLogs(logs);
				}
			}
		}
	}
}
