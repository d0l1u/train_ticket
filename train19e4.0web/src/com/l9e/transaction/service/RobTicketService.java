package com.l9e.transaction.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.l9e.transaction.vo.RobTicketVo;
import com.l9e.transaction.vo.RobTicket_CP;
import com.l9e.transaction.vo.RobTicket_History;
import com.l9e.transaction.vo.RobTicket_Notify;
import com.l9e.transaction.vo.RobTicket_OI;
import com.l9e.transaction.vo.RobTicket_Refund;

public interface RobTicketService {

	/*
	 * int queryRobListCount(Map<String, Object> paramMap);
	 * 
	 * List<Map<String, String>> queryRobList(Map<String, Object> paramMap);
	 * 
	 * int queryRobListCountCp(Map<String, Object> paramMap);
	 * 
	 * List<Map<String, String>> queryRobListCp(Map<String, Object> paramMap);
	 */

	/*
	 * 增
	 */
	void insertOrderInfo(RobTicket_OI oi) throws Exception;

	void insertCP(RobTicket_CP cp) throws Exception;

	void insertHistory(RobTicket_History history) throws Exception;

	void insertNotify(RobTicket_Notify notify) throws Exception;

	/*
	 * 删
	 */
	void deleteOrderInfo(RobTicket_OI oi) throws Exception;

	void deleteCP(RobTicket_CP cp)throws Exception;

	void deleteHistory(RobTicket_History history)throws Exception;

	void deleteNotify(RobTicket_Notify notify)throws Exception;

	/*
	 * 改
	 */
	void updateOrderInfo(RobTicket_OI oi)throws Exception;

	void updateCP(RobTicket_CP cp)throws Exception;

	void updateHistory(RobTicket_History history)throws Exception;

	void updateNotify(RobTicket_Notify notify)throws Exception;

	/*
	 * 查
	 */
	List<Map<String, String>> queryOrderInfo(Map<String, Object> paramMap)throws Exception;

	List<Map<String, String>> queryCP(Map<String, Object> paramMap)throws Exception;

	List<Map<String, String>> queryHistory(Map<String, Object> paramMap)throws Exception;

	List<Map<String, String>> queryNotify(Map<String, Object> paramMap)throws Exception;

	void insertOrderAndTickets(RobTicket_OI oi, List<RobTicket_CP> cps)throws Exception;

	/**
	 * 支付之后,根据支付返回结果,对应改变支付状态
	 * 
	 * @param oi
	 */
	void updateAfterPay(RobTicket_OI oi, String status,Map<String, String> eopInfo)throws Exception;

	Map<String, Object> selectAndPushRob(RobTicket_OI oi)throws Exception;

	List<RobTicket_OI> selectOrderInfoByConditions(Map<String, Object> request2Map)throws Exception;
	
	RobTicket_OI selectOrderInfo(RobTicket_OI oi)throws Exception;
	RobTicket_CP selecTicketCP(RobTicket_CP cp)throws Exception;
	List<RobTicket_CP> selectCps(RobTicket_OI oi)throws Exception;

	void updateRobOrderbyApiResult(RobTicketVo vo)throws Exception;

	HashMap<String, String> selectOrderStatusNum(HashMap<String, String> request2Map)throws Exception;

	void deleteOiAndCps(RobTicket_OI oi) throws Exception;

	Map<String, String> queryEOPByEopId(String eopOrderId)throws Exception;
	Map<String, String> queryOrderIdByCtripId(String criptId)throws Exception;
	
	void insertRefund(RobTicket_Refund refund)throws Exception;
	void updateRefund(RobTicket_Refund refund)throws Exception;

	void updateFrontAndBack(RobTicket_OI oi)throws Exception;

	void insertJLHistory(String orderId, String log, String person)throws Exception;

	void updateFrontAndBackWithSucc(RobTicket_OI oi, String json)throws Exception;
	public void updateCPRobSucc(List<HashMap<String, String>> adultCPS)throws Exception;
	
	public void updateWithCtripCallback(String pattern, String channel, String message)throws Exception;
	int selectOrderInfoByConditionsCount(Map<String, String> conditions) throws Exception;

	void updateFrontBackCP_Refund(String[] cpids,String status);
	RobTicket_CP queryRefundCp(HashMap<String, Object> map)throws Exception;

	void sendSMSAfterRobSucc(RobTicket_OI oi);

	void insertRobRefundFromCtrip(String orderId, String refundMoney, JSONObject object);

}
