package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.RobotSettingVo;
import com.l9e.transaction.vo.SystemSettingVo;

public interface RobotSettingDao {
	/*
	 * 获取代理机器人信息
	 */
	public List<RobotSettingVo> getRobotSystemSetting(Map<String, Object> paramMap);
	
	public int queryRobotSystemSettingCount(Map<String, Object> map);

	public RobotSettingVo getRobotSystemSettingById(String robot_id);

	public void updateNewRobotURL(RobotSettingVo robotSettingVo);

	public void addRobotSystemLog(Map<String, String> log);


	public int changeRobotStatus(RobotSettingVo robotSettingVo);
	
	public List<Map<String, Object>> queryRobotSetList(
			Map<String, Object> paramMap);

	public int queryRobotSetListCount();
	
	
	public void addNewRobot(RobotSettingVo robotSettingVo);
	
	public void updateNewRobot(RobotSettingVo robotSettingVo);
	
	public int queryRobotSetListCount2(String robot_name);

	public int queryRobotListCount(Map<String, Object> paramMap);
	
	String queryRobot_name(String robot_name);
	
	String queryRobot_id(String robot_name);
	
	String queryZhifubaoByWorkerId(String worker_id);
	
	public int queryZhifubaoCount(Map<String, Object> paramMap);
	
	String getphoneList();
	
	String queryZhanghaoList();
}
