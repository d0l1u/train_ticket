package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.AgentVo;

public interface JoinUsDao {

	AgentVo queryAgentInfo(String agentId);

	AgentVo queryAgentInfoById(String userId);
	
	List<Map<String, String>> queryDistrictList(String cityId);

	void addAgentInfo(AgentVo agentVo);

	void addJmOrder(Map<String, Object> jmMap);

	void updateJmOrderEopInfo(Map<String, String> eopInfo);

	void updateAgentJmInfo(Map<String, String> eopInfo);

	List<Map<String, String>> queryTimedJmRefundList();

	Map<String, String> queryJmOrderInfo(Map<String, String> map);

	void updateAgentJmSysXf(Map<String, String> eopInfo);

	void updateAgentJmHumXf(Map<String, String> map);

}
