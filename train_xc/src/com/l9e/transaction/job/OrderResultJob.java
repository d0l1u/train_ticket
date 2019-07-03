package com.l9e.transaction.job;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.jiexun.iface.util.StringUtil;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.ReceiveNotifyService;
import com.l9e.transaction.vo.ExternalLogsVo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpPostJsonUtil;
import com.l9e.util.Md5Encrypt;


/**
 * 向合作商户发送订单处理完成通知job
 * @author zhangjc02
 *
 */
@Component("orderResultJob")
public class OrderResultJob {
	private static final Logger logger = Logger.getLogger(OrderResultJob.class);
	@Resource
	ReceiveNotifyService receiveService;
	
	@Resource
	private OrderService orderService;
	
	@Resource
	private CommonService commonService;
	
	public void sendMerchantResultData(){
		String timestamp = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT2);
		List<Map<String,String>> listNotify = receiveService.findOrderResultNotify();
		JSONArray orderUsers = null;
		if(listNotify !=null && listNotify.size()>0 ){
			for(Map<String,String> map : listNotify){
				String status = "";
				String fail_reason = "";
				String refund_amount = "";
				String refund_type = "";
				String order_id = "";
				JSONObject json_param = new JSONObject();
				refund_type = (map.get("refund_type")==null || "".equals(map.get("refund_type"))) ? "001" : map.get("refund_type");
				//退款类型：001、差额退款；002、出票失败退款
				if("001".equals(refund_type)){
					status = "SUCCESS";
				}else{
					status = "FAILURE";
					fail_reason = map.get("fail_reason");
				}
				refund_amount = map.get("refund_amount");
				order_id = map.get("order_id");
				logger.info("发起订单处理结果通知！！订单号："+order_id);
				ExternalLogsVo log=new ExternalLogsVo();
				log.setOrder_id(order_id);
				log.setOrder_optlog("发起订单处理结果通知！！订单号："+order_id);
				log.setOpter("xc_app");
				orderService.insertOrderLogs(log);
				
				orderUsers = new JSONArray();
				JSONObject users = null;
				List<Map<String,String>> cpInfoList = orderService.queryCpInfoList(order_id);
				double bx_pay_money = 0.0;
				double ticket_pay_money = 0.0;
				for(Map<String,String> cp : cpInfoList){
					users = new JSONObject();
					String buy_money = cp.get("buy_money")==null?"0.00":cp.get("buy_money");
					users.put("cp_id", cp.get("cp_id"));
					users.put("amount", buy_money);
					ticket_pay_money += Double.valueOf(buy_money);
					users.put("ids_type", cp.get("ids_type"));
					users.put("ticket_type", cp.get("ticket_type"));
					users.put("user_ids", cp.get("user_ids"));
					users.put("user_name", cp.get("user_name"));
					users.put("train_box", cp.get("train_box"));
					users.put("seat_no", cp.get("seat_no"));
					users.put("seat_type", cp.get("seat_type"));
//					Map<String,String> merchantInfoMap2 = orderService.queryMerchantInfoByOrderId(order_id);
//					if("301008".equals(merchantInfoMap2.get("merchant_id"))&& "9".equals(String.valueOf(cp.get("seat_type"))) && cp.get("seat_no")!= null && "无座".equals( cp.get("seat_no"))){
//						OrderInfo order2 = orderService.queryOrderInfo(order_id);
//						  if(order2.getTrain_no().indexOf("C")!=-1 || order2.getTrain_no().indexOf("D")!=-1 || order2.getTrain_no().indexOf("G")!=-1){
//							  users.put("seat_type", "3");
//						  }else{
//							  users.put("seat_type", "8");
//						  }
//					}else{
//						users.put("seat_type", cp.get("seat_type"));
//					}
					//查询保险信息参数，并拼接保险信息
					Map<String,String> param = new HashMap<String,String>();
					param.put("order_id", order_id);
					param.put("user_ids", cp.get("user_ids"));
					param.put("ids_type", cp.get("ids_type"));
					param.put("cp_id", cp.get("cp_id"));
					Map<String,String> returnBx = orderService.queryOrderBxInfo(param);
					if(returnBx==null){
						users.put("bx_price", "");
						users.put("bx_code", "");
						users.put("bx_channel", "");
					}else{
						String bx_money = returnBx.get("pay_money");
						if(StringUtil.isEmpty(bx_money)){
							users.put("bx_price", "");
						}else{
							users.put("bx_price", bx_money);
							bx_pay_money += Double.valueOf(bx_money);
						}
						if(StringUtil.isEmpty(returnBx.get("bx_code"))){
							users.put("bx_code", "");
						}else{
							users.put("bx_code", returnBx.get("bx_code"));
						}
						
						if("1".equals(returnBx.get("bx_channel"))){
							users.put("bx_channel", "快保");
						}else if("2".equals(returnBx.get("bx_channel"))){
							users.put("bx_channel", "合众");
						}else{
							users.put("bx_channel", "");
						}
					}
					orderUsers.add(users);
				}
				//更新到开始通知
				Map<String, String> updatenew = new HashMap<String, String>();
				updatenew.put("new_status", "11");
				updatenew.put("old_status", "00");
				updatenew.put("order_id", order_id);
				receiveService.updateOrderReturnStatus(updatenew);
				//更新订单结果通知表通知开始时间和通知次数
				receiveService.updateOrderResultNotifyStartNum(order_id);
				//如果是平台支付并需要对用户进行差额退款，即向平台退款通知表中添加数据
				OrderInfo order = orderService.queryOrderInfo(order_id);
				//拼接请求参数
				Map<String,String> merchantInfoMap = orderService.queryMerchantInfoByOrderId(order_id);
				Map<String,String> merchantSetting = commonService.queryMerchantInfo(merchantInfoMap.get("merchant_id"));				
				//xcjson
				JSONObject xc_json = new JSONObject();
				xc_json.put("merchant_id", merchantInfoMap.get("merchant_id"));
				xc_json.put("timestamp", timestamp);
				xc_json.put("version", merchantInfoMap.get("merchant_version"));
				xc_json.put("merchant_order_id", order.getMerchant_order_id());
				xc_json.put("status", status);
				String failReason = "";
				if("FAILURE".equals(status)){
					failReason = TrainConsts.getOutFailReason().get(fail_reason);
					if("".equals(failReason))failReason = "其他";
				}
				xc_json.put("fail_reason", failReason);
				xc_json.put("amount", String.valueOf(ticket_pay_money+bx_pay_money));
				xc_json.put("refund_amount", refund_amount);
				xc_json.put("refund_type", refund_type);
				xc_json.put("order_id", order_id);
				xc_json.put("out_ticket_billno", order.getOut_ticket_billno()==null ?"":order.getOut_ticket_billno());
				xc_json.put("out_ticket_time", order.getOut_ticket_time());
				xc_json.put("bx_pay_money", String.valueOf(bx_pay_money));
				xc_json.put("spare_pro1", "");
				xc_json.put("spare_pro2", "");
				xc_json.put("order_userinfo", orderUsers.toString());
				xc_json.put("trade_no", order.getEop_order_id());//平台eop支付流水号
				String xc_md5_str = merchantInfoMap.get("merchant_id")+timestamp+merchantInfoMap.get("merchant_version")+order.getMerchant_order_id().toString();
				String xc_hmac = Md5Encrypt.getKeyedDigestFor19Pay(xc_md5_str+merchantSetting.get("sign_key"), "", "utf-8");
				xc_json.put("hmac", xc_hmac);
//				String xc_json_string = "";
//				try {
//					xc_json_string = URLEncoder.encode(xc_json.toString(),"UTF-8");
//				} catch (UnsupportedEncodingException e) {
//					e.printStackTrace();
//				}
				logger.info("订单处理结果异步通知json参数："+xc_json);
//				logger.info("订单处理结果异步通知json参数："+xc_json_string);
				String xc_url = order.getOrder_result_url();
				logger.info("url:"+xc_url);
				String result = "";
				try {
//					if(xc_url.contains("https")){
//						result = HttpsUtil.sendHttps(xc_url+"?"+xc_json.toString());
//					}else{
						result = HttpPostJsonUtil.sendJsonPost(xc_url, xc_json.toString(), "utf-8");
//					}
				} catch (Exception e) {
					logger.error("将订单处理结果通知合作商户url异常",e);
				}
				logger.info("通知携程【出票处理通知】返回结果："+result);
				String reason = "";
				if(!StringUtils.isEmpty(result)){
					JSONObject result_json = JSONObject.fromObject(result);
					reason = result_json.getString("status");
				}
				//用户接收并处理完成后，更新通知状态为完成
				Map<String,String> mp = new HashMap<String,String>();
				mp.put("order_id", order_id);
				if(null!=reason && ("SUCCESS".equals(reason.trim())||"success".equals(reason.trim()))){
					mp.put("notify_status", TrainConsts.ORDER_RESULT_22);
					receiveService.updateOrderResultNotify(mp);
					ExternalLogsVo logs=new ExternalLogsVo();
					logs.setOrder_id(order_id);
					logs.setOrder_optlog("通知商户成功[出票结果通知]");
					logs.setOpter("xc_app");
					orderService.insertOrderLogs(logs);
				}else{
					int num = receiveService.queryOrderResultNotifyStartNum(order_id);
					if(num>=10){
						mp.put("notify_status", TrainConsts.ORDER_RESULT_44);
						receiveService.updateOrderResultNotify(mp);
						
						ExternalLogsVo logs=new ExternalLogsVo();
						logs.setOrder_id(order_id);
						logs.setOrder_optlog("通知商户失败[出票结果通知]");
						logs.setOpter("xc_app");
						orderService.insertOrderLogs(logs);
					}
				}
			}
		}
	}
	
}
