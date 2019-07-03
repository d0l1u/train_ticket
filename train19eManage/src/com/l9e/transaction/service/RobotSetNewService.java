package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface RobotSetNewService {

	int queryRobotSetCount(Map<String, Object> paramMap);

	List<Map<String, String>> queryRobotSetList(Map<String, Object> paramMap);

	int changeStatus(Map<String, String> worker, Map<String, String> log);

	int changeAlipayAccount(Map<String, String> worker);

	int changeAlipayAccountType(Map<String, String> worker);

	List<Map<String, Object>> queryWorkerLog(String workerId);

	Map<String, String> queryRobotSetInfo(String workerId);

	List<Map<String, Object>> queryReportLog(String workerId);

	void deleteByWorkId(Map<String, String> worker, Map<String, String> log);

	List<Map<String, Object>> queryZhanghaoList();

	public void addVerificationCode(Map<String, Object> paramMap);
	
	public List<Map<String, Object>> queryVerificationCode(Map<String, Object> paramMap);

	public String modify12306Host(Map<String, String> worker, Map<String, String> log);

}
