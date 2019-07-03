package com.l9e.train.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l9e.train.po.Order;
import com.l9e.train.po.OrderCP;
import com.l9e.train.service.impl.TrainServiceImpl;
import com.l9e.train.util.StrUtil;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;


public class TrainAlertBase {

	private static Logger logger = LoggerFactory.getLogger(TrainAlertBase.class);
	
	/**
	 * alterURL
	 * @param map
	 * @return
	 */
	public String getAlterUrl(Order alter, OrderCP pas, String interfaceUrl){
		StringBuffer result = new StringBuffer();
		String url = new String(interfaceUrl);
		StringBuffer sb1 = new StringBuffer();
		sb1.append(alter.getAccountName())
		  .append("|")
		  .append(alter.getAccountPwd())
		  .append("|")
		  .append(alter.getOrderId())
		  .append("|")
		  .append(alter.getOutTicketBillno())
		  .append("|")
		  .append(alter.getFromStation())
		  .append("|")
		  .append(alter.getArriveStation())
		  .append("|")
		  .append(alter.getTravelTime())
		  .append("|")
		  .append(alter.getAlterTravelTime())
		  .append("|")
		  .append(alter.getTrainNo())
		  .append("|")
		  .append(alter.getAlterTrainNo())
		  .append("|")
		  .append(alter.getInputCode());
		logger.info("发起改签的车次信息:"+sb1.toString());
		String param1 = "";
		try {
			param1 = URLEncoder.encode(sb1.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		StringBuffer sb2 = new StringBuffer();
		sb2.append(pas.getCpId())
		  .append("|")
		  .append(pas.getUserName())
		  .append("|")
		  .append(pas.getTicketType())
		  .append("|")
		  .append(pas.getIdsType())
		  .append("|")
		  .append(pas.getUserIds())
		  .append("|")
		  .append(alter.getAlterSeatType())
		  .append("|")
		  .append(pas.getTrainBox())
		  .append("|")
		  .append(pas.getSeatNo());
		logger.info(alter.getOrderId()+";发起改签乘客信息:"+sb2.toString());
		String param2 = "";
		try {
			param2 = URLEncoder.encode(sb2.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}  
		String session_id = String.valueOf(System.currentTimeMillis());
		logger.info("[机器人session_id]=" + session_id);
		
		//新增参数:设备号,三字码,端口号
		 TrainServiceImpl service = new TrainServiceImpl();
			
		 //生成一个随机的16位的字符串(设备号）
		 String deviceNo=StrUtil.getRandomString(16);
		 //commond
		 String commondStr = "refundAlterTicket";
		 
		 //改签是否接受无座票  1、不改到无座票     0、允许改到无座票
		 Integer hasSeat = 0;
		 
		 //在此获取订单中出发城市和到达城市的三字码  add by wangsf
		String fromCity = alter.getFromStation();
		logger.info("订单中出发城市为：" + fromCity);
		String toCity = alter.getArriveStation();
		logger.info("订单中到达城市为：" + toCity);
		String fromCity3c = "";//出发城市三字码
		String toCity3c = "";//到达城市三字码
		if ((null != fromCity && "" != fromCity)
				&& (null != toCity && "" != toCity)) {
				logger.info("从数据库中取三字码");
				Order orderCity3c = new Order();
				// 查询出包含站点城市和三字码的order实体
				try {
					service.queryOrderCity3c(fromCity, toCity);
				} catch (RepeatException e) {
					logger.info("queryOrderCity3c：" + e.toString());
					e.printStackTrace();
				} catch (DatabaseException e) {
					logger.info("queryOrderCity3c2：" + e.toString());
					e.printStackTrace();
				} 
				logger.info("从数据库中取出三字码");
				orderCity3c = service.getOrder3c();
				if (null != orderCity3c) {
					fromCity3c = orderCity3c.getFromCity_3c();
					toCity3c = orderCity3c.getToCity_3c();
					logger.info("从数据库中取出的出发三字码---"+fromCity3c);
					logger.info("从数据库中取出的到达三字码---"+toCity3c);
				}

		}
		
		url += "?ScriptPath=resign.lua&SessionID=$session_id&Timeout=180000&ParamCount=$paramCount"
				+ "&commond=$commondStr&DeviceNo=$deviceNo&FromCity3c=$fromCity3c&ToCity3c=$toCity3c&HasSeat=$hasSeat"
				+ "&Param1=$param1&Param2=$param2";
		result.append(url.replace("$session_id", session_id).replace("$paramCount", "2")
				.replace("$commondStr", commondStr).replace("$deviceNo", deviceNo).replace("$fromCity3c", fromCity3c)
				.replace("$toCity3c", toCity3c).replace("$hasSeat",String.valueOf(hasSeat)).replace("$param1", param1)
				.replace("$param2", param2));
		return result.toString();
	}
	
	/**
	 * alter继续改签
	 * @param map
	 * @return
	 */
	public String getContinuePayUrl(Order alter, List<OrderCP> pas_list, String interfaceUrl){
		StringBuffer result = new StringBuffer();
		String url = new String(interfaceUrl);
		StringBuffer sb1 = new StringBuffer();
		sb1.append(alter.getAccountName())
		  .append("|")
		  .append(alter.getAccountPwd())
		  .append("|")
		  .append(alter.getOrderId())
		  .append("|")
		  .append(alter.getOutTicketBillno())
		  .append("|")
		  .append(alter.getFromStation())
		  .append("|")
		  .append(alter.getArriveStation())
		  .append("|")
		  .append(alter.getTravelTime())
		  .append("|")
		  .append(alter.getAlterTravelTime())
		  .append("|")
		  .append(alter.getTrainNo())
		  .append("|")
		  .append(alter.getAlterTrainNo())
		  .append("|")
		  .append(alter.getInputCode());
		logger.info("发起继续支付的车次信息:"+sb1.toString());
		String param1 = "";
		try {
			param1 = URLEncoder.encode(sb1.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		StringBuffer sb = new StringBuffer();
		int index = 2;
		for(OrderCP pas:alter.getCps()){
			StringBuffer sb2 = new StringBuffer();
			sb2.append(pas.getCpId())
			  .append("|")
			  .append(pas.getUserName())
			  .append("|")
			  .append(pas.getTicketType())
			  .append("|")
			  .append(pas.getIdsType())
			  .append("|")
			  .append(pas.getUserIds())
			  .append("|")
			  .append(alter.getAlterSeatType())
			  .append("|")
			  .append(pas.getTrainBox())
			  .append("|")
			  .append(pas.getSeatNo());
			
			String param2 = "";
			try {
				param2 = URLEncoder.encode(sb2.toString(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			sb.append("&Param"+index).append("=").append(param2);
			index++;
		}
		logger.info(alter.getOrderId()+";发起继续支付乘客信息:"+sb.toString());
		String session_id = String.valueOf(System.currentTimeMillis());
		logger.info("[机器人url]="+url+ " [session_id]" + session_id);
		String paramCount = String.valueOf(pas_list.size()+1);
		
		//commond
		 String commondStr = "refundAlterPay";
		//生成一个随机的16位的字符串(设备号）
		 String deviceNo=StrUtil.getRandomString(16);
		 
		 //改签支付类型    1：平改   2：高改低   3：低改高
		 Integer alterPayType = 1;
		
		url += "?ScriptPath=payResign.lua&SessionID=$session_id&Timeout=180000&ParamCount=$paramCount&commond=$commondStr"
				+ "&AlterPayType=$alterPayType&DeviceNo=$deviceNo&Param1=$param1&Param2=$param2";
		result.append(url.replace("$session_id", session_id).replace("$paramCount", paramCount)
				.replace("$commondStr", commondStr)
				.replace("$alterPayType", String.valueOf(alterPayType))
				.replace("$deviceNo", deviceNo)
				.replace("$param1", param1).replace("&Param2=$param2", sb.toString()));
		return result.toString();
	}
	
	/**
	 * alterAllURL批量改签
	 * @param map
	 * @return
	 */
	public String getAlterAllUrl(Order alter, List<OrderCP> pas_list, String interfaceUrl){
		StringBuffer result = new StringBuffer();
		String url = new String(interfaceUrl);
		StringBuffer sb1 = new StringBuffer();
		sb1.append(alter.getAccountName())
		  .append("|")
		  .append(alter.getAccountPwd())
		  .append("|")
		  .append(alter.getOrderId())
		  .append("|")
		  .append(alter.getOutTicketBillno())
		  .append("|")
		  .append(alter.getFromStation())
		  .append("|")
		  .append(alter.getArriveStation())
		  .append("|")
		  .append(alter.getTravelTime())
		  .append("|")
		  .append(alter.getAlterTravelTime())
		  .append("|")
		  .append(alter.getTrainNo())
		  .append("|")
		  .append(alter.getAlterTrainNo())
		  .append("|")
		  .append(alter.getInputCode());
		logger.info("发起改签的车次信息:"+sb1.toString());
		String param1 = "";
		try {
			param1 = URLEncoder.encode(sb1.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		StringBuffer sb = new StringBuffer();
		int index = 2;
		for(OrderCP pas:pas_list){
			StringBuffer sb2 = new StringBuffer();
			sb2.append(pas.getCpId())
			  .append("|")
			  .append(pas.getUserName())
			  .append("|")
			  .append(pas.getTicketType())
			  .append("|")
			  .append(pas.getIdsType())
			  .append("|")
			  .append(pas.getUserIds())
			  .append("|")
			  .append(alter.getAlterSeatType())
			  .append("|")
			  .append(pas.getTrainBox())
			  .append("|")
			  .append(pas.getSeatNo());
			
			String param2 = "";
			try {
				param2 = URLEncoder.encode(sb2.toString(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			sb.append("&Param"+index).append("=").append(param2);
			index++;
		}
		logger.info(alter.getOrderId()+";发起改签乘客信息:"+sb.toString());
		String session_id = String.valueOf(System.currentTimeMillis());
		logger.info("[机器人url]="+url+" [session_id]:" + session_id);
		String paramCount = String.valueOf(pas_list.size()+1);
		
		//新增参数:设备号,三字码,端口号
		 TrainServiceImpl service = new TrainServiceImpl();
			
		 //生成一个随机的16位的字符串(设备号）
		 String deviceNo=StrUtil.getRandomString(16);
		 //commond
		 String commondStr = "refundAlterTicket";
		 
		//改签是否接受无座票  1、不改到无座票     0、允许改到无座票
		 Integer hasSeat = 0;
		 
		 //在此获取订单中出发城市和到达城市的三字码  add by wangsf
		String fromCity = alter.getFromStation();
		logger.info("订单中出发城市为：" + fromCity);
		String toCity = alter.getArriveStation();
		logger.info("订单中到达城市为：" + toCity);
		String fromCity3c = "";//出发城市三字码
		String toCity3c = "";//到达城市三字码
		if ((null != fromCity && "" != fromCity)
				&& (null != toCity && "" != toCity)) {
				logger.info("从数据库中取三字码");
				Order orderCity3c = new Order();
				// 查询出包含站点城市和三字码的order实体
				try {
					service.queryOrderCity3c(fromCity, toCity);
				} catch (RepeatException e) {
					logger.info("queryOrderCity3c：" + e.toString());
					e.printStackTrace();
				} catch (DatabaseException e) {
					logger.info("queryOrderCity3c2：" + e.toString());
					e.printStackTrace();
				} 
				logger.info("从数据库中取出三字码");
				orderCity3c = service.getOrder3c();
				if (null != orderCity3c) {
					fromCity3c = orderCity3c.getFromCity_3c();
					toCity3c = orderCity3c.getToCity_3c();
					logger.info("从数据库中取出的出发三字码---"+fromCity3c);
					logger.info("从数据库中取出的到达三字码---"+toCity3c);
				}

		}
		
		url += "?ScriptPath=resign.lua&SessionID=$session_id&Timeout=180000&ParamCount=$paramCount" +
				"&commond=$commondStr&DeviceNo=$deviceNo&FromCity3c=$fromCity3c&ToCity3c=$toCity3c&HasSeat=$hasSeat&Param1=$param1&Param2=$param2";
		result.append(url.replace("resign", "resignall").replace("$session_id", session_id).replace("$paramCount", paramCount)
				.replace("$commondStr", commondStr).replace("$deviceNo", deviceNo)
				.replace("$fromCity3c", fromCity3c).replace("$toCity3c", toCity3c).replace("$hasSeat", String.valueOf(hasSeat))
				.replace("$param1", param1).replace("&Param2=$param2", sb.toString()));
		return result.toString();
	}
}
