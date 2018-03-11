package com.l9e.transaction.job;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

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
		gtBookNotify(timestamp, listNotify);//预定通知

	}

	
	/**
	 * @param timestamp
	 * @param listNotify
	 * 高铁的预定通知
	 */
	public void gtBookNotify(String timestamp,List<Map<String, String>> listNotify) {
		
		List<Map<String, String>> listNotifyNew=new ArrayList<Map<String,String>>();
		try {
			for(Map<String, String> map:listNotify){
				
				String order_id = map.get("order_id");
				Map<String,String> paramMap=new HashMap<String, String>();
				paramMap.put("order_id", order_id);
				paramMap.put("notify_status", "12");//00、未通知 11、准备通知 12、开始通知 22、通知完成
				paramMap.put("current_notify_status", "11");
				Integer num=receiveService.updateOrderGtBookNotifyStatus(paramMap);//更新状态为11->12
				logger.info(order_id+",----"+num);
				if (num<=0) {
				   logger.info("更新失败："+order_id);
				  // listNotify.remove(map);
				}else{
					logger.info("更新成功："+order_id);
					listNotifyNew.add(map);
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info("获取订单出现异常", e);
		}
		
		logger.info("bookList,"+listNotifyNew.size()+","+listNotify.size());
		
		JSONArray orderUsers = new JSONArray();
		if (listNotifyNew != null && listNotifyNew.size() > 0) {
			
			for (Map<String, String> map : listNotifyNew) {
				String order_id = "";
				JSONObject json_param = new JSONObject();
				order_id = map.get("order_id");
				logger.info("发起订单预定结果通知！！订单号：" + order_id);
				// 更新订单预定结果通知表通知开始时间和通知次数
				receiveService.updateOrderBookNotifyStartNum(order_id);
				// 添加开始通知日志
				ExternalLogsVo logs = new ExternalLogsVo();
				logs.setOrder_id(order_id);
				logs.setOrder_optlog("发起订单预定结果通知！！订单号：" + order_id);
				logs.setOpter("gt_app");
				orderService.insertOrderLogs(logs);
				//ticketInfo,reqtoken,pay_limit_time
				OrderInfo order = orderService.queryOrderInfo(order_id);
				String status = "";
			    if(TrainConsts.OUT_FAIL.equals(order.getOrder_status())||TrainConsts.ORDER_OUT_TIME.equals(order.getOrder_status())) {//占座失败通知,(超时订单，有时候需要回调占座失败)
				    status = "FAILURE";
				    String fail_reason=order.getFail_reason();//占座失败原因    
				    String fail_code="";//失败原因code
				    String failReason="";//失败原因提示
					logger.info(order_id+",占座失败订单通知:"+order.getOrder_status());
					json_param.put("reqtoken",StringUtils.isEmpty(order.getReqtoken())?"":order.getReqtoken());
					json_param.put("reqtime", timestamp);
					json_param.put("status", status);
					json_param.put("pay_limit_time", "");//支付截止时间
					json_param.put("refund_online", 0);
					json_param.put("ticket_price_all", "");//订单总价格
					json_param.put("supplierOrderId", order_id);
					json_param.put("ticketInfo", orderUsers.toString());
					json_param.put("order12306", "");
					json_param.put("gtgjOrderId", order.getMerchant_order_id());
					json_param.put("supplier", "19e");
					
					List<Map<String,String>> cpInfoList = orderService.queryCpInfoList(order_id);	
					fail_code = TrainConsts.getOutFailReasonCode().get(fail_reason);
					if(StringUtils.isEmpty(fail_code))fail_code = TrainConsts.OUT_FAIL_CODE_1011;
						failReason = TrainConsts.getOutFailReason().get(fail_reason);
						if(fail_reason!=null&&fail_reason.equals("12")){
							if(cpInfoList.size()==1){
								failReason = cpInfoList.get(0).get("user_name")+"涉嫌冒用";
								json_param.put("fail_id",cpInfoList.get(0).get("user_ids"));
								json_param.put("fail_name",cpInfoList.get(0).get("user_name"));
						}else{
							//信息冒用返回哪个乘客信息冒用 ,从操作日志表中获得
							Map<String,Object> paramMap = new HashMap<String,Object>();
							paramMap.put("orderId", order_id);
							paramMap.put("content", "%冒用%");
							String errorInfoLog  = orderService.selectOrderLog(paramMap);
							logger.info("高铁订单处理结果回调，信息冒用，查询日志返回结果为"+errorInfoLog);
							if(errorInfoLog!=null){
								for(Map<String,String> cp1 : cpInfoList){
									String  fail_user_ids = cp1.get("user_ids");
									String  fail_user_name = cp1.get("user_name");
									if(errorInfoLog.contains(fail_user_name)){
										json_param.put("fail_id",fail_user_ids);
										json_param.put("fail_name",fail_user_name);
										failReason = fail_user_name+"涉嫌冒用";
										break;
									}
									}
								}
							}
						    logger.info("高铁订单处理结果回调，信息冒用，错误信息为"+failReason);
						}
						if(fail_reason!=null&&fail_reason.equals("8")){
							if(cpInfoList.size()==1){
								failReason = cpInfoList.get(0).get("user_name")+"尚未通过身份信息核验";
								json_param.put("fail_id",cpInfoList.get(0).get("user_ids"));
								json_param.put("fail_name",cpInfoList.get(0).get("user_name"));
							}else{
								//身份待核验返回哪个乘客信息待核验 ,从操作日志表中获得
								Map<String,Object> paramMap = new HashMap<String,Object>();
								paramMap.put("orderId", order_id);
								paramMap.put("content", "%尚未通过身份信息核验%");
								String errorInfoLog = orderService.selectOrderLog(paramMap);
								if(errorInfoLog==null || errorInfoLog.equals("")){
									paramMap.put("content", "%待核验%");
									errorInfoLog = orderService.selectOrderLog(paramMap);
									if(errorInfoLog==null || errorInfoLog.equals("")){
										paramMap.put("content", "%待审核%");
										errorInfoLog = orderService.selectOrderLog(paramMap);
									}
								}
								logger.info("高铁订单处理结果回调，未通过核验，查询日志返回结果为"+errorInfoLog);
								if(errorInfoLog!=null){
									for(Map<String,String> cp2 : cpInfoList){
										String  fail_user_ids = cp2.get("user_ids");
										String  fail_user_name = cp2.get("user_name");
										if(errorInfoLog.contains(fail_user_name)){
											json_param.put("fail_id",fail_user_ids);
											json_param.put("fail_name",fail_user_name);
											failReason = fail_user_name+"尚未通过身份核验";
											break;
										}
									}
								}
							}
							logger.info("高铁订单处理结果回调，身份待核验，错误信息为"+failReason);
						}
				if(StringUtils.isEmpty(failReason))failReason = "其他";
				
				//失败原因，转换，完成
				json_param.put("fail_code", fail_code);
				json_param.put("fail_msg", failReason);
						
				String method ="book";
				Map<String,String> merchantInfoMap = orderService.queryMerchantInfoByOrderId(order_id);
				Map<String,String> merchantSetting = commonService.queryMerchantInfo(merchantInfoMap.get("merchant_id"));
				//md5(partnerid+method+reqtime+md5(KEY).toLowerCase()).toLowerCase(),签名规则
				String md5_str = TrainConsts.PARTNER_ID+method+timestamp+Md5Encrypt.getKeyedDigestFor19Pay(merchantSetting.get("sign_key"),"", "utf-8");
				String sign = Md5Encrypt.getKeyedDigestFor19Pay(md5_str, "", "utf-8");
				json_param.put("sign", sign);
				
				StringBuffer params = new StringBuffer();
				logger.info(order_id+",占座失败通知参数："+json_param.toString());
				logger.info(order_id+"seatdata----");
				try {
					params.append("seatdata=").append(URLEncoder.encode(json_param.toString().replace("+","%20"), "utf-8"));
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}

				logger.info("订单占座失败结果异步通知参数："+params.toString());
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
					logger.info("将订单占座失败结果通知合作商户url异常",e);
				}
				logger.info("result="+result);
				//用户接收并处理完成后，更新通知状态为完成
				if(null!=result&&"SUCCESS".equals(result.trim())){
					receiveService.updateOrderBookNotifyFinish(order_id);
					logs.setOrder_optlog("通知高铁成功[占座失败结果通知]");
					orderService.insertOrderLogs(logs);
				}else{
					Map<String,String> paramMap=new HashMap<String, String>();
					paramMap.put("order_id", order_id);
					paramMap.put("notify_status", "11");//00、未通知 11、准备通知 12、开始通知 22、通知完成
					paramMap.put("current_notify_status", "12");
					Integer num=receiveService.updateOrderGtBookNotifyStatus(paramMap);//更新状态为12->11
					logger.info(order_id+",----"+num+",通知失败置为准备通知状态");
				}
		
		 }else if(TrainConsts.BOOK_SUCCESS.equals(order.getOrder_status())){
					orderUsers = orderService.updateBookSuccessInfoGt("query",order_id,map.get("merchant_id"), order.getOut_ticket_billno());
					status = "SUCCESS"; 
					String pay_limit_time=DateUtil.dateToString(DateUtil.dateAddMin(DateUtil.stringToDate(order.getOut_ticket_time(),DateUtil.DATE_FMT3),28), DateUtil.DATE_FMT3);//限制28分钟内支付
					logger.info(order_id+",出票占座时间:"+order.getOut_ticket_time()+",pay_limit_time:"+pay_limit_time);	
					json_param.put("reqtoken",StringUtils.isEmpty(order.getReqtoken())?"":order.getReqtoken());
					json_param.put("reqtime", timestamp);
					json_param.put("status", status);
					json_param.put("pay_limit_time", pay_limit_time);//支付截止时间
					json_param.put("refund_online", 0);
					json_param.put("ticket_price_all", order.getBuy_money());//订单总价格
					json_param.put("supplierOrderId", order_id);
					json_param.put("ticketInfo", orderUsers.toString());
					json_param.put("order12306", order.getOut_ticket_billno()==null ?"":order.getOut_ticket_billno());
					json_param.put("gtgjOrderId", order.getMerchant_order_id());
					json_param.put("supplier", "19e");
					
					String method ="book";
					Map<String,String> merchantInfoMap = orderService.queryMerchantInfoByOrderId(order_id);
					Map<String,String> merchantSetting = commonService.queryMerchantInfo(merchantInfoMap.get("merchant_id"));
					//md5(partnerid+method+reqtime+md5(KEY).toLowerCase()).toLowerCase(),签名规则
					String md5_str = TrainConsts.PARTNER_ID+method+timestamp+Md5Encrypt.getKeyedDigestFor19Pay(merchantSetting.get("sign_key"),"", "utf-8");
					logger.info(order_id+":"+md5_str+"MD5()"+merchantSetting.get("sign_key"));
					String sign = Md5Encrypt.getKeyedDigestFor19Pay(md5_str, "", "utf-8");
					
					json_param.put("sign", sign);
					//json_param.put("method", method);
				
				StringBuffer params = new StringBuffer();
				logger.info(order_id+",占座通知参数："+json_param.toString());
				logger.info(order_id+"seatdata----");
				try {
					params.append("seatdata=").append(URLEncoder.encode(json_param.toString().replace("+","%20"), "utf-8"));
	
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				logger.info("订单预定结果异步通知参数："+params.toString().replace("+","%20"));
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
					logger.info("将订单预定结果通知合作商户url异常",e);
				}
				logger.info(order_id+"result="+result);
				//用户接收并处理完成后，更新通知状态为完成
				if("SUCCESS".equals(result.trim())){
					receiveService.updateOrderBookNotifyFinish(order_id);
					logs.setOrder_optlog("通知高铁成功[占座成功结果通知]");
					orderService.insertOrderLogs(logs);
				}else {
					Map<String,String> paramMap=new HashMap<String, String>();
					paramMap.put("order_id", order_id);
					paramMap.put("notify_status", "11");//00、未通知 11、准备通知 12、开始通知 22、通知完成
					paramMap.put("current_notify_status", "12");
					Integer num=receiveService.updateOrderGtBookNotifyStatus(paramMap);//更新状态为12->11
					logger.info(order_id+",----"+num+",通知失败置为准备通知状态");
				}
			}else {
				logger.info(order_id+"，订单状态不对,"+order.getOrder_status());
				
			}

			}
		}else{
			
			logger.info("没有获取到可以处理的订单！");
		}
				
	}



	/**
	 * @param timestamp
	 * @param listNotify
	 * 原有的预定通知，不作修改
	 */
	public void bookNotify(String timestamp,List<Map<String, String>> listNotify) {	
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
				logs.setOpter("ext_app");
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
//					logs.setOpter("ext_app");
					orderService.insertOrderLogs(logs);
				}
			}
		}
	}
}
