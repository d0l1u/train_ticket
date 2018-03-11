package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.RobotSettingDao;
import com.l9e.transaction.service.RobotSettingService;
import com.l9e.transaction.vo.RobotSettingVo;
@Service("robotSettingService")
public class RobotSettingServiceImpl  implements RobotSettingService{
	
	@Resource
	private RobotSettingDao robotSettingDao;

	@Override
	public List<RobotSettingVo> getRobotSystemSetting(Map<String, Object> paramMap) {
		return robotSettingDao.getRobotSystemSetting(paramMap);
	}
	
	@Override
	public int queryRobotSystemSettingCount(Map<String, Object> map) {
		return robotSettingDao.queryRobotSystemSettingCount(map);
	}
	
	@Override
	public RobotSettingVo getRobotSystemSettingById(String robot_id) {
		return robotSettingDao.getRobotSystemSettingById(robot_id);
	}

	@Override
	public void updateNewRobotURL(RobotSettingVo robotSettingVo,
			Map<String, String> log) {
		robotSettingDao.updateNewRobotURL(robotSettingVo);
		robotSettingDao.addRobotSystemLog(log);
	}
	public int updateRobotStatus(RobotSettingVo robotSettingVo, Map<String, String> log){
		robotSettingDao.addRobotSystemLog(log);
		int count = robotSettingDao.changeRobotStatus(robotSettingVo);
		return count;
	}
	
	@Override
	public List<Map<String, Object>> queryRobotSetList(
			Map<String, Object> paramMap) {
		return robotSettingDao.queryRobotSetList(paramMap);
	}

	@Override
	public int queryRobotSetListCount() {
		return robotSettingDao.queryRobotSetListCount();
	}

	@Override
	public void addRobot(RobotSettingVo robotSettingVo, Map<String, String> log) {
		robotSettingDao.addNewRobot(robotSettingVo);
		robotSettingDao.addRobotSystemLog(log);
	}
	
	@Override
	public void updateRobot(RobotSettingVo robotSettingVo, Map<String, String> log) {
		robotSettingDao.updateNewRobot(robotSettingVo);
		robotSettingDao.addRobotSystemLog(log);
	}

	@Override
	public int queryRobotSetListCount2(String robotName) {
		return robotSettingDao.queryRobotSetListCount2(robotName);
	}

	@Override
	public int queryRobotListCount(Map<String, Object> paramMap) {
		return robotSettingDao.queryRobotListCount(paramMap);
	}
	
	public String queryRobot_name(String robot_name) {
		return robotSettingDao.queryRobot_name(robot_name);
	}
	
	public String queryRobot_id(String robot_name) {
		return robotSettingDao.queryRobot_id(robot_name);
	}
	
	public String queryZhifubaoByWorkerId(String worker_id) {
		return robotSettingDao.queryZhifubaoByWorkerId(worker_id);
	}
	
	@Override
	public int queryZhifubaoCount(Map<String, Object> paramMap) {
		return robotSettingDao.queryZhifubaoCount(paramMap);
	}
	
	public String getphoneList() {
		return robotSettingDao.getphoneList();
	}

	@Override
	public String queryZhanghaoList() {
		return robotSettingDao.queryZhanghaoList();
	}
	
}
