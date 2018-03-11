package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.AccountVo;
import com.l9e.transaction.vo.AcquireVo;

public interface AcquireService {
	List<Map<String, String>> queryAcquireList(Map<String, Object> paramMap);
	
	List<Map<String, String>> queryAcquireListCp(Map<String, Object> paramMap);
	
	List<Map<String, String>> queryAcquireExcel(Map<String, Object> paramMap);
	
	List<Map<String, String>> queryAcquireFailExcel(Map<String, Object> paramMap);

	int queryAcquireListCount(Map<String, Object> paramMap);
	
	int queryAcquireListCountCp(Map<String, Object> paramMap);

	Map<String, String> queryAcquireOrderInfo(String order_id);

	List<Map<String, Object>> queryAcquireOrderInfoCp(String order_id);

	void updateAcquire(AcquireVo acquire,AccountVo account);
	
	void updateCpDetail(Map<String,String> map, List<Map<String, String>> cpList);
	
//	Map<String, String> queryPayOrderInfo(String order_id);
	
	List<Map<String, Object>>  queryHistroyByOrderId(String order_id);

	void updateAccount(Map<String,String>map,AccountVo account);

	String queryAccount(String channel);

	String queryDbOrder_status(String order_id);

	// tomcat
	void addUserAccount(Map<String, String> paramMap);
	
	void updateInfoOrderStatus(String order_id,String user);

	void updateChangeSeatTypeAndOrderInfo(Map<String, String> modify_Map,Map<String, String> modify_CpMap);

	void updateOrderInfo(Map<String, String> modify_CpMap);

	void updateCpListFor61(List<Map<String, String>> cpList);

	void updateOrderInfoFor45(Map<String, String> update_Map);
	void updateOrderInfoFor61(Map<String, String> update_Map);

	void updateNotify(String order_id);

	String updateReceiveOrderInfo(String data);

	boolean updateLockAccount(String lockAccount);

	String updateAcquireToRobot(Map<String, String> updateMap);

	String queryCMpayOrderStatus(Map<String, String> paramMap);

	String queryCodeType();

	String queryRobotRandom();

	Map<String,String> queryMoney(Map<String, String> updateMap);

	void updateCpPrice(Map<String, String> updateMap);

	String queryOrderMoney(Map<String, String> updateMap);

	//大于十分钟订单列表
	List<Map<String, String>> queryAcquireOvertimeList(
			Map<String, Object> paramMap);
	
	//大于十分钟订单条数
	int queryOvertimeListCount(Map<String, Object> paramMap);

	int queryAcquireFailListCount(Map<String, Object> paramMap);

	List<Map<String, String>> queryAcquireFailList(Map<String, Object> paramMap);
	
	String queryCurrentCodeType();

	void updateInfoOrderStatusTo55(Map<String, String> param_map);

	//效率
	List<Map<String, String>> queryAcquireListXl(Map<String, Object> paramMap);
	List<Map<String, String>> queryAcquireExcelXl(Map<String, Object> paramMap);
	int queryAcquireListCountXl(Map<String, Object> paramMap);
	
	void updateStatus00To11(Map<String, String> paramMap);
	
	String queryOrderIsPay(String order_id);
	
	//人工出票改到人工预订
	void updateManualToRobot(Map<String,String>map,Map<String,String> updateMap);

	void updateCtripToRobot(Map<String, String> updateMap);
	
	
	void updateEndOpt_Ren(Map<String, String> userMap);

	void ctripSearchAgain(Map<String, String> map);

	void updateCtripGoStatusTo44(Map<String, String> updateMap);

	int updateInfoOrderStatusToManual(Map<String, String> param_map);
}
