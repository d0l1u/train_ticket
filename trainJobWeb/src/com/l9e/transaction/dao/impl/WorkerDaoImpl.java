package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.WorkerDao;
import com.l9e.transaction.vo.Robot;
import com.l9e.transaction.vo.Worker;

@Repository("workerDao")
public class WorkerDaoImpl extends BaseDao implements WorkerDao {

	@Override
	public void updateWorker(Map<String, Object> map) {
		this.getSqlMapClientTemplate().update("worker.updateWorker", map);
	}

	@Override
	public List<Worker> selectWorker(Map<String, Object> map) {
		List<Worker> workerList = this.getSqlMapClientTemplate().queryForList("worker.selectWorker", map);
		return workerList;
	}

	@Override
	public void updateWorkerObject(Worker worker) {
		this.getSqlMapClientTemplate().update("worker.updateWorkerObject", worker);
		
	}

	@Override
	public List<Robot> selectRobot(Map<String, Object> map) {
		List<Robot> robotList = this.getSqlMapClientTemplate().queryForList("worker.selectRobot", map);
		return robotList;
	}

	@Override
	public void updateRobot(Robot robot) {
		this.getSqlMapClientTemplate().update("worker.updateRobot", robot);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Worker> selectRegWorker(HashMap parm) {
		// TODO Auto-generated method stub
		
		List<Worker> workerList = this.getSqlMapClientTemplate().queryForList("worker.selectRegRobot",parm);
		
		return workerList;
	}

	@Override
	public void updateRegWorker1(HashMap parm1) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().update("worker.updateRegRobot1", parm1);
	}

	@Override
	public void updateRegWorker2(HashMap parm2) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().update("worker.updateRegRobot2", parm2);
	}

	@Override
	public void insertLog(Map<String, String> log) {
		this.getSqlMapClientTemplate().insert("worker.insertLog", log);
	}

	@Override
	public List<Map<String, String>> queryCloseRobots() {
		return this.getSqlMapClientTemplate().queryForList("worker.queryCloseRobots");
	}

//	@Override
//	public void updateRobotOpen(Map<String, String> paramMap) {
//		
//	}

	@Override
	public void updateWorkerOpen(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().update("worker.updateWorkerOpen", paramMap);
	}

	@Override
	public List<Map<String, String>> queryPayCloseRobots() { 
		return this.getSqlMapClientTemplate().queryForList("worker.queryClosePayRobots");
	}

	@Override
	public void updatePayWorkerOpen(Map<String, String> paramMap) {
		this.getSqlMapClientTemplate().update("worker.updateWorkerPayOpen", paramMap);
	}

	/**
	 * 查询机器操作时间距离当前时间大于10分钟，并且机器已锁定的所有lua机器人
	 * @param map
	 * @return
	 */
	@Override
	public List<Worker> selectTimeOutWorkerList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().queryForList("worker.selectTimeOutWorkerList", map);
	}

}
