package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.AcquireVo;

public interface AcquireDao {
	List<Map<String, String>> queryAcquireList(Map<String, Object> paramMap);
	
	List<Map<String, String>> queryAcquireListCp(Map<String, Object> paramMap);
	
	List<Map<String, String>> queryAcquireExcel(Map<String, Object> paramMap);
	
	List<Map<String, String>> queryAcquireFailExcel(Map<String, Object> paramMap);
	
	int queryAcquireListCount(Map<String, Object> paramMap);
	int queryAcquireListCountCp(Map<String, Object> paramMap);

	Map<String, String> queryAcquireOrderInfo(String order_id);

	List<Map<String, Object>> queryAcquireOrderInfoCp(String order_id);

	void updateAcquire(AcquireVo acquire);
	

	void updateEndOpt_Ren(Map<String, String> userMap);
	
	void updateNotify(AcquireVo acquire);
	
	
	void freeAccount(String accountId);
	
//	Map<String, String> queryPayOrderInfo(String order_id);
	
	 List<Map<String, Object>>  queryHistroyByOrderId(String order_id);

	void updateAccount(Map<String,String>map);

	void updateAccountToNull(Map<String,String>map);
	
	String queryAccount(String channel);

	void updateAccountAssoil(Map<String,String>map);
	
	void updateRegistTo33(Map<String,String>map);

	void updateAccountStart(Map<String, String> map);

	void updateCp_Orderinfo_Buy_money(Map<String,String>map);

	void updateCp_Orderinfo_Cp(Map<String, String> map);

	void updateCp_Orderinfo_Notify(String order_id);

	String queryDbOrder_status(String order_id);
	
	//tomcat
	void addUserAccount(Map<String,String>paramMap);

	void updateInfoOrderStatus(Map<String,String>paramMap);

	void updateChangeSeatType(Map<String, String> modify_Map);

	void updateOrderInfo(Map<String, String> modify_CpMap);

	void updateOrderInfo_cp(Map<String, String> modify_CpMap);

	void updateOrderInfoFor61(Map<String, String> update_Map);
	
	void updateOrderInfoFor45(Map<String, String> update_Map);

	void updateReceiveOrderInfo(Map<String, String> order_Map);

	void updateReceiveCpInfo(Map<String, String> cp_Map);

	String queryOrder_time(String order_time_);

	void updateNotifyToBookSuccess(String order_id);

	Map<String, Object> queryCpInfo(String cpId);

	void updateOrderStatus(String order_status, String order_id);

	void updateLockAccount(String lockAccount);

	void updateAcquireToRobot(Map<String, String> updateMap);

	String queryCMpayOrderStatus(Map<String, String> paramMap);

	String queryCodeType();

	String queryRobotRandom();

	Map<String,String> queryMoney(Map<String, String> updateMap);

	void updateCpPrice(Map<String, String> updateMap);

	String queryOrderMoney(Map<String, String> updateMap);

	List<Map<String, String>> queryAcquireOvertimeList(
			Map<String, Object> paramMap);

	int queryOvertimeListCount(Map<String, Object> paramMap);

	List<Map<String, String>> queryAcquireFailList(Map<String, Object> paramMap);

	int queryAcquireFailListCount(Map<String, Object> paramMap);

	String queryCurrentCodeType();

	void updateInfoOrderStatusTo55(Map<String,String> map);


	String queryOrderIsPay(String order_id);

	int queryAccountContactNum(String accountId);

	void updateAccountStop(String accountId);
	//效率
	List<Map<String, String>> queryAcquireListXl(Map<String, Object> paramMap);
	List<Map<String, String>> queryAcquireExcelXl(Map<String, Object> paramMap);
	int queryAcquireListCountXl(Map<String, Object> paramMap);
	
	void updateStatus00To11(Map<String, String> paramMap);
	
	//人工出票改到人工预订
	void updateManualToRobot(Map<String,String> updateMap);

	void updateCtripToRobot(Map<String, String> updateMap);

	void ctripSearchAgain(Map<String, String> map);

	void updateCtripGoStatusTo44(Map<String, String> updateMap);

	int updateInfoOrderStatusToManual(Map<String, String> param_map);
}
