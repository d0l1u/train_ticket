package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.AgentVo;

public interface JoinUsService {

	AgentVo queryAgentInfo(String agentId);

	AgentVo queryAgentInfoById(String userId);
	
	void addAgentInfo(AgentVo agentVo);

	List<Map<String, String>> queryDistrictList(String cityId);

	void addJmOrder(Map<String, Object> jmMap);

	void updateJmOrderEopInfo(Map<String, String> eopInfo);

	List<Map<String, String>> queryTimedJmRefundList();

	void updateJmOrderStatus(Map<String, String> map);

	Map<String, String> queryJmOrderInfo(Map<String, String> map);

	void updateAgentJmSysXf(Map<String, String> map);

	void updateAgentJmHumXf(Map<String, String> map);

}
