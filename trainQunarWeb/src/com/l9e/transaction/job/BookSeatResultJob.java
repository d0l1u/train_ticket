package com.l9e.transaction.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.InterAccount;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.QunarResult;
import com.l9e.transaction.vo.SysConfig;
import com.l9e.util.HttpUtil;
import com.l9e.util.TrainPropUtil;
import com.l9e.util.UrlFormatUtil;
//占座回调接口
@Component("bookSeatResultJob")
public class BookSeatResultJob {
	private static final Logger logger = Logger.getLogger(BookSeatResultJob.class);
	@Resource
	private OrderService orderService;
	
	public void sendBookRsult(){
		List<Map<String, String>> sendList = orderService.queryBookResultList();
		if(sendList.size()>0){
			for (Map<String, String> sendMap : sendList) {
				if(!StringUtils.isEmpty(sendMap.get("order_id"))){
					logger.info("【占座回调接口--出票结果】出票结果通知，order_id="+sendMap.get("order_id"));
					orderService.updateQunarBookNotifyBegin(sendMap.get("order_id"));
					this.notifyResultSys(sendMap.get("order_id"));
				}
			}
		}
	}

	private void notifyResultSys(String order_id) {
		OrderInfo orderInfo = orderService.queryOrderInfoById(order_id);
		String status = "";//是否预订成功
		String comment = "";//协议-备注
		String code = "";
		String msg = "";
		Map<String, String> map = null;
		
		//获取qunar订单来源账号
		String order_source = null;
		if(StringUtils.isEmpty(orderInfo.getOrder_source())){
			order_source = "qunar1";
		}else{
			order_source = orderInfo.getOrder_source();
		}
		InterAccount account = SysConfig.getAccountByName(order_source);
		if(account == null){
			logger.error("fatal error InterAccount is null!");
			return;
		}
		String md5Key = account.getMd5Key();
		String merchantCode = account.getMerchantCode();
		String logPre = "【占座回调接口--出票结果<"+order_source+">】";
		
		
		if(orderInfo == null || StringUtils.isEmpty(orderInfo.getOrder_status())){
			logger.info(logPre+"阻止本次出票结果通知，订单状态为空，order_id="+order_id);
			return;
		}
		//通知qunar：0、出票成功通知 1、预定成功通知  2、先预定后支付
//		String whenNotify = orderService.queryQunarSysSetting("out_notify_qunar");
		if(TrainConsts.OUT_SUCCESS.equals(orderInfo.getOrder_status()) 
				|| TrainConsts.BOOK_SUCCESS.equals(orderInfo.getOrder_status())){//出票成功 或者 预订成功
			status = "success";
			List<Map<String, String>> cpList = null;
			
			List<Map<String, Object>> tickets = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> ticket = null;
			cpList = orderService.queryOrderCpList(order_id);
			for(Map<String, String> cpInfo : cpList){
				ticket = new HashMap<String, Object>();
				ticket.put("ticketNo", cpInfo.get("out_ticket_billno"));
				ticket.put("seatType", cpInfo.get("qunar_seat_type"));
				ticket.put("seatNo", TrainPropUtil.beautifySeatNo(cpInfo.get("seat_no")));
				ticket.put("price", Double.parseDouble(cpInfo.get("buy_money")));
				ticket.put("passengerName", cpInfo.get("user_name"));
				ticket.put("seq", "0");
				ticket.put("ticketType", TrainPropUtil.getQunarTicketType(cpInfo.get("ticket_type")));
				tickets.add(ticket);
			}
			
			Map<String, Object> resultMap = new HashMap<String, Object>();//协议-票号
			resultMap.put("count", tickets.size());
			resultMap.put("tickets", JSONArray.fromObject(tickets));
			String result = JSONObject.fromObject(resultMap).toString();
			System.out.println(result);
			logger.info(logPre+"result="+result);
			comment = "占座成功";
			
			map = new HashMap<String,String>();
			map.put("merchantCode", merchantCode);
			map.put("orderNo", order_id);
			map.put("result", result);
			map.put("status", status);
			map.put("comment", comment);
			logger.info("【占座结果回调通知】要加密的字符串："+md5Key + merchantCode + order_id + result + status + code + msg + comment);
			String hMac = DigestUtils.md5Hex(md5Key + merchantCode + order_id + result + status + code + msg + comment).toUpperCase();
			logger.info("【占座结果回调通知】我们加密的结果="+hMac);
			map.put("HMAC", hMac);
		
		}else if(TrainConsts.OUT_FAIL.equals(orderInfo.getOrder_status())){
			status = "false";
			String reason = orderInfo.getOut_fail_reason();
			String passengerReason = orderInfo.getPassenger_reason();//乘车人信息错误2014-03-08 add
			
			if(TrainConsts.OUT_FAIL_REASON_0.equals(reason)){
				code = "0";
				msg = "其他";
				comment = "其他";
			}else if(TrainConsts.OUT_FAIL_REASON_1.equals(reason)){
				code = "1";
				msg = "所购买的车次坐席已无票";
				comment = "所购买的车次坐席已无票";
			}else if(TrainConsts.OUT_FAIL_REASON_2.equals(reason)){
				code = "2";
				msg = "身份证件已经实名制购票，不能再次购买同日期同车次的行程冲突";
				comment = "身份证件已经实名制购票，不能再次购买同日期同车次的车票";
			}else if(TrainConsts.OUT_FAIL_REASON_3.equals(reason)){
				code = "3";
				msg = "qunar票价和12306不符";
				comment = "qunar票价和12306不符";
			}else if(TrainConsts.OUT_FAIL_REASON_4.equals(reason)){
				code = "4";
				msg = "车次数据与12306不一致";
				comment = "车次数据与12306不一致";
			}else if(TrainConsts.OUT_FAIL_REASON_5.equals(reason)){
				code = "5";
				msg = "乘客信息错误";
				comment = "乘客信息错误";
			}else if(TrainConsts.OUT_FAIL_REASON_6.equals(reason)){
				code = "6";
				msg = "12306乘客身份信息核验失败";
				comment = "12306乘客身份信息核验失败";
			}else{
				/**为什么系统会返回其他错误状态码? 出票系统问题!*/
				reason="0";
				comment = "其他";
				passengerReason="";
				code = "314";
				msg = "其他";
			}
			//防止人为选错失败原因
			if(TrainConsts.OUT_FAIL_REASON_6.equals(reason)){//passengerReason必填
				if(StringUtils.isEmpty(passengerReason)){
					reason = TrainConsts.OUT_FAIL_REASON_0;
					comment = "其他";
					passengerReason = "";
					code = "314";
					msg = "其他";
				}
			}else{
				passengerReason = "";
			}
			
			map = new HashMap<String,String>();
			map.put("merchantCode", merchantCode);
			map.put("orderNo", order_id);
			map.put("result", "");
			map.put("status", status);
			map.put("code", code);
			map.put("msg", msg);
			map.put("comment", comment);
			logger.info("【回调接口】要加密的字符串："+md5Key + merchantCode + order_id + status + code + msg + comment);
			String hMac = DigestUtils.md5Hex(md5Key + merchantCode + order_id + status + code + msg + comment).toUpperCase();
			logger.info("【占座结果回调通知】我们加密的结果="+hMac);
			map.put("HMAC", hMac);
		}
		String retUrl = orderInfo.getRetUrl();
		if(map != null){
			String reqParams;
			try {
				//添加日志
				Map<String, String> logMap = new HashMap<String, String>();
				logMap.put("order_id",order_id);
				logMap.put("opt_person", "qunar_app");
				
				logger.info(logPre+"通知去哪回传参数为："+map);
				reqParams = UrlFormatUtil.CreateUrl("", map, "", "UTF-8");
				StringBuffer reqUrl = new StringBuffer();
				reqUrl.append(retUrl);
				String jsonRs = HttpUtil.sendByPost(reqUrl.toString(), reqParams, "UTF-8");
				if(!"".equals(jsonRs) && jsonRs!=null){
					logger.info(logPre+"通知返回："+jsonRs);
					ObjectMapper mapper = new ObjectMapper();
					QunarResult rs = mapper.readValue(jsonRs, QunarResult.class);
					if(rs.isRet()){
						logger.info(logPre+"通知qunar成功，order_id="+order_id);
						orderService.updateQunarBookNotifyEnd(order_id);
						
						logMap.put("content", "占座结果通知qunar成功");
					}else{
						logger.info(logPre+"通知qunar失败，order_id="+order_id+"，系统将在约1分钟后重新通知");
						
						int notifyNum = orderService.queryBookTicketNotifyCount(order_id);
						if(notifyNum >= 5){
							orderService.updateOrderBookNotifyFail(order_id);
						}
						
						//添加日志
						logMap.put("content", "占座结果通知qunar失败，errCode="+rs.getErrCode()+"&errMsg="+rs.getErrMsg());
					}
					orderService.addOrderInfoLog(logMap);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info(logPre+"通知qunar异常，order_id="+order_id+"，系统将在约1分钟后重新通知");
				
				int notifyNum = orderService.queryBookTicketNotifyCount(order_id);
				if(notifyNum >= 5){
					orderService.updateOrderBookNotifyFail(order_id);
				}
				
				//添加日志
				Map<String, String> logMap = new HashMap<String, String>();
				logMap.put("order_id",order_id);
				logMap.put("content", "占座结果通知qunar异常");
				logMap.put("opt_person", "qunar_app");
				orderService.addOrderInfoLog(logMap);
			}
		}
	}
	
	
}
