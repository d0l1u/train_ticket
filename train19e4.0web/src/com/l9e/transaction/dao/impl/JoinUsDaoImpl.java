package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.JoinUsDao;
import com.l9e.transaction.vo.AgentRegistVo;
import com.l9e.transaction.vo.AgentVo;

@Repository("joinUsDao")
public class JoinUsDaoImpl extends BaseDao implements JoinUsDao {

	public AgentVo queryAgentInfo(String agentId) {
		return (AgentVo) this.getSqlMapClientTemplate().queryForObject("agent.queryAgentInfo", agentId);
	}
	
	public AgentVo queryAgentInfoById(String userId) {
		return (AgentVo) this.getSqlMapClientTemplate().queryForObject("agent.queryAgentInfoById", userId);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryDistrictList(String cityId) {
		return this.getSqlMapClientTemplate().queryForList("agent.queryDistrictList", cityId);
	}

	public void addAgentInfo(AgentVo agentVo) {
		this.getSqlMapClientTemplate().insert("agent.addAgentInfo", agentVo);
	}

	public void addJmOrder(Map<String, Object> jmMap) {
		this.getSqlMapClientTemplate().insert("agent.addJmOrder", jmMap);
	}

	public void updateJmOrderEopInfo(Map<String, String> eopInfo) {
		this.getSqlMapClientTemplate().insert("agent.updateJmOrderEopInfo", eopInfo);
	}

	public void updateAgentJmInfo(Map<String, String> eopInfo) {
		this.getSqlMapClientTemplate().update("agent.updateAgentJmInfo", eopInfo);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryTimedJmRefundList() {
		return this.getSqlMapClientTemplate().queryForList("agent.queryTimedJmRefundList");
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> queryJmOrderInfo(Map<String, String> map) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("agent.queryJmOrderInfo", map);
	}

	public void updateAgentJmSysXf(Map<String, String> eopInfo) {
		this.getSqlMapClientTemplate().update("agent.updateAgentJmSysXf", eopInfo);
	}

	public void updateAgentJmHumXf(Map<String, String> eopInfo) {
		this.getSqlMapClientTemplate().update("agent.updateAgentJmHumXf", eopInfo);
	}

	@Override
	public void addAgentRegistInfo(AgentRegistVo agentRegistVo) {
		this.getSqlMapClientTemplate().insert("regist.addAgentRegistInfo", agentRegistVo);
	}

	@Override
	public void deleteAgentRegistInfo(Map<String, String> map) {
		this.getSqlMapClientTemplate().delete("regist.deleteAgentRegistInfo", map);
	}

	@Override
	public AgentRegistVo queryAgentRegistInfo(Map<String, String> map) {
		return (AgentRegistVo)this.getSqlMapClientTemplate().queryForObject("regist.queryAgentRegistInfo", map);
	}

	@Override
	public void updateAgentRegistInfo(AgentRegistVo agentRegistVo) {
		this.getSqlMapClientTemplate().update("regist.updateAgentRegistInfo", agentRegistVo);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AgentRegistVo> queryAgentRegistListInfo(String user_id) {
		return this.getSqlMapClientTemplate().queryForList("regist.queryAgentRegistListInfo", user_id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryAgentRegisterInfo(String userId) {
		return this.getSqlMapClientTemplate().queryForList("regist.queryAgentRegisterInfo", userId);
	}
}
