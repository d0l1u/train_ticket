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
import com.l9e.util.HttpUtil;
import com.l9e.util.HttpsUtil;
import com.l9e.util.Md5Encrypt;


/**
 * 向合作商户发送订单处理完成通知job
 * @author zuoyuxing
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
				orderUsers = new JSONArray();
				JSONObject users = null;
				List<Map<String,String>> cpInfoList = orderService.queryCpInfoList(order_id);
				double bx_pay_money = 0.0;
				double ticket_pay_money = 0.0;
				for(Map<String,String> cp : cpInfoList){
					users = new JSONObject();
					String buy_money = cp.get("buy_money")==null?"0.00":cp.get("buy_money");
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
				//订单异步通知商户参数
				json_param.put("merchant_order_id", order.getMerchant_order_id());
				json_param.put("status", status);
				String failReason ="";
				failReason = TrainConsts.getOutFailReason().get(fail_reason);
				if("FAILURE".equals(status)){
					//未核验和冒用具体到个人
					if(fail_reason.equals("8")){
						if(cpInfoList.size()==1){
							failReason = cpInfoList.get(0).get("user_name")+"尚未通过身份信息核验";
						}else{
							//未核验
							Map<String,Object> paramMap = new HashMap<String,Object>();
							paramMap.put("orderId", order_id);
							paramMap.put("content", "%尚未通过身份信息核验%");
							String log = orderService.selectOrderLog(paramMap);
							if(log==null || log.equals("")){
								paramMap.put("content", "%待核验%");
								log = orderService.selectOrderLog(paramMap);
								if(log==null||log.equals("")){
									paramMap.put("content", "%待审核%");
									log = orderService.selectOrderLog(paramMap);
								}
							}
							logger.info("占座回调，身份待核验，查询日志为"+order.getOrder_id()+":"+log);
							if(log!=null){
								for(Map<String,String> cp : cpInfoList){
									String  fail_user_name = cp.get("user_name");
									if(log.contains(fail_user_name)){
										failReason = fail_user_name+"尚未通过身份核验";
										break;
									}
								}
							}
						}
						logger.info("占座回调，身份待核验，错误信息为"+failReason);
					}
					//身份信息被冒用具体到个人
					if(fail_reason.equals("12")){
						if(cpInfoList.size()==1){
							failReason = cpInfoList.get(0).get("user_name")+"涉嫌冒用";
						}else{
							//冒用
							Map<String,Object> paramMap = new HashMap<String,Object>();
							paramMap.put("orderId", order_id);
							paramMap.put("content", "%冒用%");
							String log =  orderService.selectOrderLog(paramMap);
							logger.info("占座回调，信息冒用，查询日志返回结果为"+order.getOrder_id()+":"+log);
							if(log!=null){
								for(Map<String,String> cp : cpInfoList){
									String  fail_user_name = cp.get("user_name");
									if(log.contains(fail_user_name)){
										failReason = fail_user_name+"涉嫌冒用";
										break;
									}
								}
							}
						}
						logger.info("占座回调，信息冒用，错误信息为"+failReason);
					}
					//身份证实名制购票具体到个人
					if(fail_reason.equals("2")){
						if(cpInfoList.size()==1){
							failReason = cpInfoList.get(0).get("user_name")+"身份证实名制，不能再次购买该车次的车票";
						}else {
							//实名制
							Map<String,Object> paramMap = new HashMap<String,Object>();
							paramMap.put("orderId", order_id);
							paramMap.put("content", "%已订%");
							String log =  orderService.selectOrderLog(paramMap);
							logger.info("占座回调，实名制，查询日志返回结果为"+order.getOrder_id()+":"+log);
							if(log!=null){
								for(Map<String,String> cp : cpInfoList){
									String  fail_user_name = cp.get("user_name");
									if(log.contains(fail_user_name)){
										failReason = fail_user_name+"身份证实名制，不能再次购买该车次的车票";
										break;
									}
								}
							}
						}
						logger.info("占座回调，实名制，错误信息为"+failReason);
					}
					if("".equals(failReason))failReason = "其他";
				}
				
				json_param.put("fail_reason", failReason);
				json_param.put("amount", String.valueOf(ticket_pay_money+bx_pay_money));
				json_param.put("refund_amount", refund_amount);
				json_param.put("refund_type", refund_type);
				json_param.put("order_id", order_id);
				json_param.put("out_ticket_billno", order.getOut_ticket_billno()==null ?"":order.getOut_ticket_billno());
				json_param.put("out_ticket_time", order.getOut_ticket_time());
				json_param.put("bx_pay_money", String.valueOf(bx_pay_money));
				json_param.put("spare_pro1", "");
				json_param.put("spare_pro2", "");
				json_param.put("order_userinfo", orderUsers.toString());
				json_param.put("trade_no", order.getEop_order_id());//平台eop支付流水号
				//拼接请求参数
				Map<String,String> merchantInfoMap = orderService.queryMerchantInfoByOrderId(order_id);
				Map<String,String> merchantSetting = commonService.queryMerchantInfo(merchantInfoMap.get("merchant_id"));
				String md5_str = merchantInfoMap.get("merchant_id")+timestamp+merchantInfoMap.get("merchant_version")+json_param.toString();
				String hmac = Md5Encrypt.getKeyedDigestFor19Pay(md5_str+merchantSetting.get("sign_key"), "", "utf-8");
				StringBuffer params = new StringBuffer();
				params.append("merchant_id=").append(merchantInfoMap.get("merchant_id"))
					.append("&timestamp=").append(timestamp)
					.append("&version=").append(merchantInfoMap.get("merchant_version"));
				try {
					params.append("&json_param=").append(URLEncoder.encode(json_param.toString(),"UTF-8"));
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				params.append("&hmac=").append(hmac);
				logger.info("订单处理结果异步通知参数："+params.toString());
				String url = order.getOrder_result_url();
				logger.info("url:"+url);
				String result = "";
				try {
					if(url.contains("https")){
						result = HttpsUtil.sendHttps(url+"?"+params.toString());
					}else{
						result = HttpUtil.sendByPost(url, params.toString(), "utf-8");
					}
				} catch (Exception e) {
					logger.error("将订单处理结果通知合作商户url异常",e);
				}
				logger.info("result="+result);
				//用户接收并处理完成后，更新通知状态为完成
				Map<String,String> mp = new HashMap<String,String>();
				mp.put("order_id", order_id);
				if(null!=result && "SUCCESS".equals(result.trim())){
					mp.put("notify_status", TrainConsts.ORDER_RESULT_22);
					receiveService.updateOrderResultNotify(mp);
					ExternalLogsVo logs=new ExternalLogsVo();
					logs.setOrder_id(order_id);
					logs.setOrder_optlog("通知商户成功[出票结果通知]");
					logs.setOpter("ext_app");
					orderService.insertOrderLogs(logs);
				}else{
					int num = receiveService.queryOrderResultNotifyStartNum(order_id);
					if(num>=10){
						mp.put("notify_status", TrainConsts.ORDER_RESULT_44);
						receiveService.updateOrderResultNotify(mp);
						
						ExternalLogsVo logs=new ExternalLogsVo();
						logs.setOrder_id(order_id);
						logs.setOrder_optlog("通知商户失败[出票结果通知]");
						logs.setOpter("ext_app");
						orderService.insertOrderLogs(logs);
					}
				}
			}
		}
	}
}
