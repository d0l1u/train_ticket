package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.l9e.transaction.vo.AgentRegistVo;
import com.l9e.transaction.vo.AgentVo;

public interface JoinUsService {

	AgentVo queryAgentInfo(String agentId);

	AgentVo queryAgentInfoById(String userId);
	

	List<Map<String, String>> queryDistrictList(String cityId);

	void addJmOrder(Map<String, Object> jmMap);

	void updateJmOrderEopInfo(Map<String, String> eopInfo);

	List<Map<String, String>> queryTimedJmRefundList();

	void updateJmOrderStatus(Map<String, String> map);

	Map<String, String> queryJmOrderInfo(Map<String, String> map);

	void updateAgentJmSysXf(Map<String, String> map);

	void updateAgentJmHumXf(Map<String, String> map);
	
	void addAgentRegistInfo(AgentRegistVo agentRegistVo);
	
	AgentRegistVo queryAgentRegistInfo(Map<String, String> map);
	
	void updateAgentRegistInfo(AgentRegistVo agentRegistVo);
	
	void deleteAgentRegistInfo(Map<String, String> map);

	public List<AgentRegistVo> queryAgentRegistListInfo(String user_id);
	
	/**
	 * 根据代理商id查询其实名的姓名 身份证 和状态
	 * @param userId
	 * @return
	 */
	List<Map<String,String>> queryAgentRegisterInfo(String userId);
	
	
	
}
