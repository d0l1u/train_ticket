package com.l9e.transaction.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface RemindDao {
	//查询各渠道未退款数量
	int query19eFailCount();
	int queryQunarFailCount();
	int queryExtFailCount();
	int queryB2CFailCount();
	int queryinnerFailCount();
	int querycomplainCount();
	int queryElongFailCount();
	int queryTcFailCount();
	int queryAllRefundCount(String channel);
	//查询通知失败
	int bookExtCount();
	int bookQunarCount();
	int bookElongCount();
	int bookTcCount();
	
	int bookGtCount();
	int bookMtCount();
	int bookTuniuCount();
	
	int refundExtCount();
	int refundQunarCount();
	int refundElongCount();
	int refundTcCount();
	
	int alterCount();
	//保险
	int querybxCount();
	//机器人
	List<Map<String, Object>> queryRobotCount();
	
	
	//打码
	int queryAdminCurrentNameCount();
	int queryCodeCountToday();
	int queryCodeToday();
	int queryUncode(String channel);
	String codeQunarType();
	
	int queryWaitCodeQueenCount(String department);

	int queryAdminCurrentNameCount2(Map<String, Object> paramMap1);
	
	List<Map<String, String>> queryAccountList();
	List<Map<String, String>> queryAccountMarginList(
			HashMap<String, Integer> map);
	List<Map<String, String>> queryAccountContactList(
			HashMap<String, Integer> map);
	int queryOrderNumber();
	int queryRobotNumber(String string);
	
	List<Map<String, String>> queryZhifubaoMoney(String name);
}
