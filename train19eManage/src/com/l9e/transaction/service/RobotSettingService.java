package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;


import com.l9e.transaction.vo.RobotSettingVo;

public interface RobotSettingService {
	/*
	 * 获取机器人系统设置信息
	 */
	public List<RobotSettingVo> getRobotSystemSetting(Map<String, Object> paramMap);
	
	public int queryRobotSystemSettingCount(Map<String, Object> map);

	public RobotSettingVo getRobotSystemSettingById(String robot_id);

	public void updateNewRobotURL(RobotSettingVo robotSettingVo,
			Map<String, String> log);
	
	public int updateRobotStatus(RobotSettingVo robotSettingVo, Map<String, String> log);
	
	public int queryRobotSetListCount();

	public List<Map<String, Object>> queryRobotSetList(
			Map<String, Object> paramMap);
	
	public void addRobot(RobotSettingVo robotSettingVo, Map<String, String> log);
	
	public void updateRobot(RobotSettingVo robotSettingVo, Map<String, String> log);
	
	public int queryRobotSetListCount2(String robot_name);

	public int queryRobotListCount(Map<String, Object> paramMap);

	String queryRobot_name(String robot_name);
	
	String queryRobot_id(String robot_name);
	
	String queryZhifubaoByWorkerId(String worker_id);
	
	int queryZhifubaoCount(Map<String, Object> paramMap);
	
	String getphoneList();
	
	String queryZhanghaoList();
	
}
