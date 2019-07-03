package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface RobotSetNewDao {

	int queryRobotSetCount(Map<String, Object> paramMap);

	List<Map<String, String>> queryRobotSetList(Map<String, Object> paramMap);

	void insertLog(Map<String, String> log);

	int changeStatus(Map<String, String> worker);
	
	int changeAlipayAccount(Map<String, String> worker);
	
	int changeAlipayAccountType(Map<String, String> worker);

	List<Map<String, Object>> queryWorkerLog(String workerId);

	Map<String, String> queryRobotSetInfo(String workerId);

	List<Map<String, Object>> queryReportLog(String workerId);

	void deleteByWorkId(Map<String, String> worker);

	List<Map<String, Object>> queryZhanghaoList();

	public void addVerificationCode(Map<String, Object> paramMap);

	public List<Map<String, Object>> queryVerificationCode(Map<String, Object> paramMap);
}
