package com.l9e.transaction.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.RobTicketVo.RobTicket_CP;
import com.l9e.transaction.vo.RobTicketVo.RobTicket_History;
import com.l9e.transaction.vo.RobTicketVo.RobTicket_Notify;
import com.l9e.transaction.vo.RobTicketVo.RobTicket_OI;

public interface RobTicketDao {

	List<Map<String, String>> queryRobListCp(Map<String, Object> paramMap);

	int queryRobListCountCp(Map<String, Object> paramMap);

	int queryRobListCount(Map<String, Object> paramMap);

	List<Map<String, String>> queryRobList(Map<String, Object> paramMap);
	List<Map<String, String>> queryRobListForExcel(Map<String, Object> paramMap);
	
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
	 * 查
	 */
	List<Map<String, String>> queryOrderInfo(Map<String, Object> paramMap);
	List<Map<String, String>> queryCP(Map<String, Object> paramMap);
	List<Map<String, String>> queryHistory(Map<String, Object> paramMap);
	List<Map<String, String>> queryNotify(Map<String, Object> paramMap);

	void updateFrontBackCP_Refund(HashMap<String, String> map);

	void insertJLHistory(Map<String, Object> map);


}
