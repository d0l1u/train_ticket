package com.l9e.transaction.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import com.l9e.transaction.vo.RobTicket_CP;
import com.l9e.transaction.vo.RobTicket_History;
import com.l9e.transaction.vo.RobTicket_Notify;
import com.l9e.transaction.vo.RobTicket_OI;
import com.l9e.transaction.vo.RobTicket_Refund;

public interface RobTicketDao {
	/*
	 * 增
	 */
	void insertOrderInfo(RobTicket_OI oi);
	void insertCP(RobTicket_CP cp);
	void insertHistory(RobTicket_History history);
	void insertNotify(RobTicket_Notify notify);
	/*
	 * 删
	 */
	void deleteOrderInfo(RobTicket_OI oi);
	void deleteCP(RobTicket_CP cp);
	void deleteHistory(RobTicket_History history);
	void deleteNotify(RobTicket_Notify notify);
	/*
	 * 改
	 */
	void updateOrderInfo(RobTicket_OI oi);
	void updateCP(RobTicket_CP cp);
	void updateHistory(RobTicket_History history);
	void updateNotify(RobTicket_Notify notify);
	
	/*
	 * 条件查询
	 */
	List<Map<String, String>> queryOrderInfo(Map<String, Object> paramMap);
	List<Map<String, String>> queryCP(Map<String, Object> paramMap);
	List<Map<String, String>> queryHistory(Map<String, Object> paramMap);
	List<Map<String, String>> queryNotify(Map<String, Object> paramMap);
	
	
	/**
	 * 主键查一条
	 * @param oi
	 * @return
	 */
	RobTicket_OI selectOrderInfoByPrimaryKey(RobTicket_OI oi);
	RobTicket_CP selectCPByPrimaryKey(RobTicket_CP cp);
	RobTicket_History selectHistoryByPrimaryKey(RobTicket_History history);
	RobTicket_Notify selectNotifyByPrimaryKey(RobTicket_Notify notify);
	
	
	List<RobTicket_CP> selectCPsByorderId(RobTicket_OI oi);
	List<RobTicket_OI> selectOrderInfoByConditions(Map<String, Object> conditions);
	
	HashMap<String,String> selectOrderStatusNum(HashMap<String, String> paraMap);
	void deleteCPByOrderInfo(RobTicket_OI oi);
	Map<String, String> queryEOPByEopId(String eopOrderId);
	Map<String, String> queryOrderIdByCtripId(String eopOrderId);
	
	
	void insertRefund(RobTicket_Refund refund);
	void updateRefund(RobTicket_Refund refund);
	void updateJLOrderInfo(RobTicket_OI oi);
	void insertJLHistory(Map<String, Object> map);
	void updateCPRobSucc(List<HashMap<String, String>> adultCPS);
	
	int selectOrderInfoByConditionsCount(Map<String, String> conditions);
	void updateFrontBackCP_Refund(String[] cpids,String status);
	RobTicket_CP queryRefundCp(HashMap<String, Object> map);
	Map<String, String> querySMSRobSucc(String ctripOrderId);
	void insertRobRefundFromCtrip(Map<String, String> map);

}
