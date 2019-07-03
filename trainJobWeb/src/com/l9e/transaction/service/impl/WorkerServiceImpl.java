package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.WorkerDao;
import com.l9e.transaction.service.WorkerService;
import com.l9e.transaction.vo.Robot;
import com.l9e.transaction.vo.Worker;

@Service("workerService")
public class WorkerServiceImpl implements WorkerService {

	private static final Logger logger = Logger
			.getLogger(WorkerServiceImpl.class);

	@Resource
	private WorkerDao workerDao;

	public void updateWorker(Map<String, Object> map) {
		workerDao.updateWorker(map);

	}

	@Override
	public List<Worker> selectWorker(Map<String, Object> map) {
		return workerDao.selectWorker(map);
	}

	@Override
	public void updateWorkerObject(Worker worker) {
		workerDao.updateWorkerObject(worker);
	}

	@Override
	public List<Robot> selectRobot(Map<String, Object> map) {
		return workerDao.selectRobot(map);
	}

	@Override
	public void updateRobot(Robot robot) {
		workerDao.updateRobot(robot);
	}

	@Override
	public List<Worker> selectRegWorker(HashMap parm) {
		
		List<Worker> res=workerDao.selectRegWorker(parm);
		
		
		System.out.println("---selectRegWorker---");
		
		return res;
		
	}

	@Override
	public void updateRegWorker1(HashMap parm1) {
		// TODO Auto-generated method stub
		
		workerDao.updateRegWorker1(parm1);
		
		
		
	}

	@Override
	public void updateRegWorker2(HashMap parm2) {
		// TODO Auto-generated method stub
		
		workerDao.updateRegWorker2(parm2);
	}

	@Override
	public List<Map<String, String>> queryCloseRobots() {
		return workerDao.queryCloseRobots();
	}

	@Override
	public void updateRobotOpen(Map<String, String> paramMap,
			Map<String, String> log) {
		workerDao.updateWorkerOpen(paramMap);
//		workerDao.updateRobotOpen(paramMap);
		workerDao.insertLog(log);
	}

	@Override
	public List<Map<String, String>> queryPayCloseRobots() {
		return workerDao.queryPayCloseRobots();
	}

	@Override
	public void updatePayRobotOpen(Map<String, String> paramMap,
			Map<String, String> log) {
		workerDao.updatePayWorkerOpen(paramMap);
		workerDao.insertLog(log);		
	}

	
	/**
	 * 查询机器操作时间距离当前时间大于10分钟，并且机器已锁定的所有lua机器人
	 * @param map
	 * @return
	 */
	@Override
	public List<Worker> queryTimeOutWorkerList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return workerDao.selectTimeOutWorkerList(map);
	}

}
