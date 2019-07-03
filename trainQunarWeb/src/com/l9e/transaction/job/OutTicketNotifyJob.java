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

/**
 * 出票结果通知job
 * @author zhangjun
 *
 */
@Component("outTicketNotifyJob")
public class OutTicketNotifyJob {
	
	private static final Logger logger = Logger.getLogger(OutTicketNotifyJob.class);
	
	@Resource
	private OrderService orderService;
	
	@Value("#{propertiesReader[qunarReqUrl]}")
	private String qunarReqUrl;//Qunar请求地址
	
	public void outNotify() throws Exception {
	
		List<Map<String, String>> orderList = orderService.queryTimedOutTicketList();//定时扫描待出票结果通知的订单
		for(Map<String, String> map : orderList){
			orderService.updateQunarOutNotifyBegin(map.get("order_id"));
			String order_type= map.get("order_type");
			this.sendNotifyRequest(map.get("order_id"), order_type);
		}
	}
	
	private void sendNotifyRequest(String order_id, String order_type) throws Exception {
		OrderInfo orderInfo = orderService.queryOrderInfoById(order_id);
		String opt = null;//协议-操作类型
		String status = null;//是否预订成功
		String comment = null;//协议-备注
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
		String logPre = "【出票结果<"+order_source+">】";
		
		
		if(orderInfo == null || StringUtils.isEmpty(orderInfo.getOrder_status())){
			logger.info(logPre+"阻止本次出票结果通知，订单状态为空，order_id="+order_id);
			return;
		}
		//通知qunar：0、出票成功通知 1、预定成功通知  2、先预定后支付
//		String whenNotify = orderService.queryQunarSysSetting("out_notify_qunar");
		if(TrainConsts.OUT_SUCCESS.equals(orderInfo.getOrder_status()) 
				|| TrainConsts.BOOK_SUCCESS.equals(orderInfo.getOrder_status())){//出票成功 或者 预订成功
			opt = "CONFIRM";//协议-操作类型
			List<Map<String, String>> cpList = null;
			
			List<Map<String, Object>> tickets = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> ticket = null;
			if(StringUtils.isEmpty(order_type) || TrainConsts.ORDER_TYPE_COMMON.equals(order_type)){//普通订单
				cpList = orderService.queryOrderCpList(order_id);
				
				for(Map<String, String> cpInfo : cpList){
					ticket = new HashMap<String, Object>();
					ticket.put("seq", "0");
					ticket.put("ticketNo", cpInfo.get("out_ticket_billno"));
					ticket.put("seatType", cpInfo.get("qunar_seat_type"));
					ticket.put("seatNo", this.beautifySeatNo(cpInfo.get("seat_no")));
					ticket.put("price", Double.parseDouble(cpInfo.get("buy_money")));
					ticket.put("passengerName", cpInfo.get("user_name"));
					ticket.put("ticketType", TrainPropUtil.getQunarTicketType(cpInfo.get("ticket_type")));
					tickets.add(ticket);
				}
			}else{
				List<Map<String, String>> tripList = orderService.queryTripListByOrderId(order_id);
				
				for(Map<String, String> trip : tripList){
					if(!TrainConsts.OUT_SUCCESS.equals(trip.get("order_status")) 
							&& !TrainConsts.BOOK_SUCCESS.equals(trip.get("order_status"))){//出票成功 或者 预订成功
						logger.info(logPre+"阻止本次出票结果通知，联程订单状态为"+trip.get("order_status")+"，trip_id="+trip.get("trip_id"));
						return;
					}
					
					String trip_id = trip.get("trip_id");
					cpList = orderService.queryOrderCpList(trip_id);

					for(Map<String, String> cpInfo : cpList){
						ticket = new HashMap<String, Object>();
						ticket.put("seq", trip.get("trip_seq"));
						ticket.put("ticketNo", cpInfo.get("out_ticket_billno"));
						ticket.put("seatType", cpInfo.get("qunar_seat_type"));
						ticket.put("seatNo", this.beautifySeatNo(cpInfo.get("seat_no")));
						ticket.put("price", Double.parseDouble(cpInfo.get("buy_money")));
						ticket.put("passengerName", cpInfo.get("user_name"));
						ticket.put("ticketType", TrainPropUtil.getQunarTicketType(cpInfo.get("ticket_type")));
						tickets.add(ticket);
					}
				}
			}
			
			Map<String, Object> resultMap = new HashMap<String, Object>();//协议-票号
			resultMap.put("count", tickets.size());
			resultMap.put("tickets", JSONArray.fromObject(tickets));
			String result = JSONObject.fromObject(resultMap).toString();
			//System.out.println(result);
			logger.info(logPre+"result="+result);
			
			comment = "已经出票成功";
			
			map = new HashMap<String,String>();
			map.put("merchantCode", merchantCode);
			map.put("orderNo", order_id);
			map.put("opt", opt);
			map.put("result", result);
			map.put("comment", comment);
			String hMac = DigestUtils.md5Hex(md5Key + merchantCode + order_id + opt + result + comment).toUpperCase();
			map.put("HMAC", hMac);
		
		}else if(TrainConsts.OUT_FAIL.equals(orderInfo.getOrder_status())){
			opt = "NO_TICKET";
			String reason = orderInfo.getOut_fail_reason();
			String passengerReason = orderInfo.getPassenger_reason();//乘车人信息错误2014-03-08 add
			
			if(TrainConsts.OUT_FAIL_REASON_0.equals(reason)){
				comment = "其他";
			}else if(TrainConsts.OUT_FAIL_REASON_1.equals(reason)){
				comment = "所购买的车次坐席已无票";
			}else if(TrainConsts.OUT_FAIL_REASON_2.equals(reason)){
				comment = "身份证件已经实名制购票，不能再次购买同日期同车次的车票";
			}else if(TrainConsts.OUT_FAIL_REASON_3.equals(reason)){
				comment = "qunar票价和12306不符";
			}else if(TrainConsts.OUT_FAIL_REASON_4.equals(reason)){
				comment = "车次数据与12306不一致";
			}else if(TrainConsts.OUT_FAIL_REASON_5.equals(reason)){
				comment = "乘客信息错误";
			}else if(TrainConsts.OUT_FAIL_REASON_6.equals(reason)){
				comment = "12306乘客身份信息核验失败";
			}else if(TrainConsts.OUT_FAIL_REASON_7.equals(reason)){
				comment = "12306乘客身份信息被冒用";
			}else{
				reason="0";
				comment = "其他";
				passengerReason="";
			}
			//防止人为选错失败原因
			if(TrainConsts.OUT_FAIL_REASON_2.equals(reason)){
				if(StringUtils.isEmpty(order_type) || TrainConsts.ORDER_TYPE_COMMON.equals(order_type)){//普通订单
					JSONArray arr=new JSONArray();
					List<Map<String, String>> cpList = null;
					cpList = orderService.queryOrderCpList(order_id);
					
					for(Map<String, String> cpInfo : cpList){
						JSONObject cp_json=new JSONObject();
						/*[{"certNo":"22222222",
							"certType":1,
							"name":"李四",
							"ticketType":1,
							"reason":"3",
							preDate:"2015-07-01",
							preTrainNo："G12"}]*/
						cp_json.put("certNo",cpInfo.get("user_ids"));//user_ids
						cp_json.put("certType", cpInfo.get("qunar_certtype"));//qunar_certtype
						cp_json.put("name",cpInfo.get("user_name"));//user_name
						cp_json.put("ticketType",TrainPropUtil.getQunarTicketType(cpInfo.get("ticket_type")));//ticket_type
						cp_json.put("reason", "3");
						String preDate=orderInfo.getFrom_time();
						//2015-06-27 10:08:00
						preDate=preDate.substring(0,preDate.indexOf(" "));
						cp_json.put("preDate",preDate);
						cp_json.put("preTrainNo",orderInfo.getTrain_no());
						arr.add(cp_json);
					}
					passengerReason=arr.toString();
					logger.info("passengerReason in OUT_FAIL_REASON_2:"+passengerReason);
				}else{
					reason = TrainConsts.OUT_FAIL_REASON_0;
					comment = "其他";
					passengerReason = "";
				}
			}else{
				passengerReason = "";
			}
			
			map = new HashMap<String,String>();
			map.put("merchantCode", merchantCode);
			map.put("orderNo", order_id);
			map.put("opt", opt);
			map.put("reason", reason);
			if(StringUtils.isEmpty(passengerReason)){
				map.put("passengerReason", passengerReason);
			}
			map.put("passengerReason", passengerReason);
			
			map.put("comment", comment);
			String hMac = DigestUtils.md5Hex(md5Key + merchantCode + order_id + opt + reason + passengerReason + comment).toUpperCase();
			map.put("HMAC", hMac);
		}
	
		try {
			if(map != null){
				logger.info("出票结果通知param"+map.toString());
				String reqParams = UrlFormatUtil.CreateUrl("", map, "", "UTF-8");
				
				StringBuffer reqUrl = new StringBuffer();
				reqUrl.append(qunarReqUrl).append("ProcessPurchase.do");
				
				String jsonRs = HttpUtil.sendByPost(reqUrl.toString(), reqParams, "UTF-8");
				logger.info("出票结果通知返回："+jsonRs);
				ObjectMapper mapper = new ObjectMapper();
				QunarResult rs = mapper.readValue(jsonRs, QunarResult.class);
				
				if(rs.isRet()){
					logger.info(logPre+"通知qunar成功，order_id="+order_id);
					
					orderService.updateQunarOutNotifyEnd(order_id);
				}else{
					logger.info(logPre+"通知qunar失败，order_id="+order_id+"，系统将在约1分钟后重新通知");
					
					int notifyNum = orderService.queryOutTicketNotifyCount(order_id);
					if(notifyNum >= 5){
						orderService.updateOrderNotifyFail(order_id);
					}
					
					//添加日志
					Map<String, String> logMap = new HashMap<String, String>();
					logMap.put("order_id",order_id);
					logMap.put("content", "出票结果通知qunar失败，errCode="+rs.getErrCode()+"&errMsg="+rs.getErrMsg());
					logMap.put("opt_person", "qunar_app");
					orderService.addOrderInfoLog(logMap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(logPre+"通知qunar异常，order_id="+order_id+"，系统将在约1分钟后重新通知");
			
			int notifyNum = orderService.queryOutTicketNotifyCount(order_id);
			if(notifyNum >= 5){
				orderService.updateOrderNotifyFail(order_id);
			}
			
			//添加日志
			Map<String, String> logMap = new HashMap<String, String>();
			logMap.put("order_id",order_id);
			logMap.put("content", "出票结果通知qunar异常");
			logMap.put("opt_person", "qunar_app");
			orderService.addOrderInfoLog(logMap);
			
		}
		
	}
	
	private String beautifySeatNo(String seatNo){
		//String seatNo = "02车厢车13D号 上铺";
		seatNo = seatNo.replace(" ", "").replaceAll(" ", "").replace("车厢车", "车");
		seatNo = seatNo.replace("车上", "车").replace("车下", "车");
//		if(seatNo.startsWith("0")){
//			seatNo = seatNo.substring(1);
//		}
		//System.out.println(seatNo);
		return seatNo;
	}
	
}
