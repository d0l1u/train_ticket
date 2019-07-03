package com.l9e.transaction.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.Robot;
import com.l9e.transaction.vo.Worker;

public interface WorkerService {

	public void updateWorker(Map<String, Object> map);
	
	public List<Worker> selectWorker(Map<String, Object> map);
	
	public void updateWorkerObject(Worker worker);
	
	public List<Robot> selectRobot(Map<String, Object> map);
	
	public void updateRobot(Robot robot);

	public List<Worker> selectRegWorker(HashMap parm);

	public void updateRegWorker2(HashMap parm1);

	public void updateRegWorker1(HashMap parm1);

	public List<Map<String, String>> queryCloseRobots();

	public void updateRobotOpen(Map<String, String> paramMap,
			Map<String, String> log);
	
	public List<Map<String,String>>queryPayCloseRobots();
	
	public void updatePayRobotOpen(Map<String, String> paramMap,
			Map<String, String> log);
	
	/**
	 * 查询机器操作时间距离当前时间大于10分钟，并且机器已锁定的所有lua机器人
	 * @param map
	 * @return
	 */
	public List<Worker> queryTimeOutWorkerList(Map<String, Object> map);
	
}
