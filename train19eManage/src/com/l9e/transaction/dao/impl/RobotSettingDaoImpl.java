package com.l9e.transaction.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.RobotSettingDao;
import com.l9e.transaction.vo.RobotSettingVo;
@Repository("robotSettingDao")
public class RobotSettingDaoImpl extends BaseDao implements RobotSettingDao{
	/*
	 * 代理机器人
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RobotSettingVo> getRobotSystemSetting(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList(
		"robotsetting.queryRobotSystemSetting",paramMap);
	}
	
	@Override
	public int queryRobotSystemSettingCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"robotsetting.queryRobotSystemSettingCount", map);
	}
	@Override
	public RobotSettingVo getRobotSystemSettingById(String robot_id) {
		try {
			return (RobotSettingVo)(this.getSqlMapClient().queryForObject("robotsetting.getRobotSystemSettingById", robot_id));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void updateNewRobotURL(RobotSettingVo robotSettingVo) {
		this.getSqlMapClientTemplate().update("robotsetting.updateRobotURL",
				robotSettingVo);
	}

	@Override
	public void addRobotSystemLog(Map<String, String> log) {
		this.getSqlMapClientTemplate().insert("robotsetting.addRobotLog", log);
	}

	@Override
	public int changeRobotStatus(RobotSettingVo robotSettingVo) {
		return(Integer) this.getSqlMapClientTemplate().update(
				"robotsetting.changeRobotStatus", robotSettingVo);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryRobotSetList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("robotsetting.queryRobotSetList",paramMap);
	}

	public int queryRobotSetListCount() {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("robotsetting.queryRobotSetListCount");
	}

	@Override
	public void addNewRobot(RobotSettingVo robotSettingVo) {
		this.getSqlMapClientTemplate().insert("robotsetting.addRobot",
				robotSettingVo);
	}
	
	@Override
	public void updateNewRobot(RobotSettingVo robotSettingVo) {
		this.getSqlMapClientTemplate().update("robotsetting.updateRobot",
				robotSettingVo);
	}

	@Override
	public int queryRobotSetListCount2(String robotName) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("robotsetting.queryRobotSetListCount2",robotName);
	}

	@Override
	public int queryRobotListCount(Map<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("robotsetting.queryRobotListCount",paramMap);
	}

	@Override
	public String queryRobot_name(String robot_name) {
		return (String) this.getSqlMapClientTemplate().queryForObject("robotsetting.queryRobot_name",robot_name);
	}
	
	@Override
	public String queryRobot_id(String robot_name) {
		return (String) this.getSqlMapClientTemplate().queryForObject("robotsetting.queryRobot_id",robot_name);
	}
	
	@Override
	public String queryZhifubaoByWorkerId(String worker_id) {
		return (String) this.getSqlMapClientTemplate().queryForObject("robotsetting.queryZhifubaoByWorkerId",worker_id);
	}

	@Override
	public int queryZhifubaoCount(Map<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("robotsetting.queryZhifubaoCount",paramMap);
	}

	@Override
	public String getphoneList() {
		return (String) this.getSqlMapClientTemplate().queryForObject("robotsetting.queryphoneList");
	}

	@Override
	public String queryZhanghaoList() {
		return (String) this.getSqlMapClientTemplate().queryForObject("robotsetting.queryZhanghaoList");
	}

}
