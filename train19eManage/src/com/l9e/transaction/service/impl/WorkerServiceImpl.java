package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.WorkerDao;
import com.l9e.transaction.service.WorkerService;
import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.WorkerVo;

@Service("workerService")
public class WorkerServiceImpl implements WorkerService{

	
	@Resource
	private WorkerDao workerDao;
	
	public List<Map<String, Object>> queryWorkerList(
			Map<String, Object> paramMap) {
		return workerDao.queryWorkerList(paramMap);
	}
	
	public List<Map<String, Object>> queryWorkerList2(
			Map<String, Object> paramMap) {
		return workerDao.queryWorkerList2(paramMap);
	}

	public int queryWorkerListCount(Map<String, Object> paramMap) {
		return workerDao.queryWorkerListCount(paramMap);
	}

	public Map<String, Object> queryWorker(String worker_id) {
		return workerDao.queryWorker(worker_id);
	}

	public void updateWorker(WorkerVo  Worker) {
		workerDao.updateWorker(Worker);
	}

	public void insertWorker(WorkerVo  Worker) {
		workerDao.insertWorker(Worker);
	}

	public void deleteWorker(WorkerVo Worker) {
		workerDao.deleteWorker(Worker);
	}
	public List<AreaVo> getProvince() {
		return workerDao.getProvince();
	}

	public List<AreaVo> getCity(String provinceid) {
		return workerDao.getCity(provinceid);
	}

	public List<AreaVo> getArea(String cityid) {
		return workerDao.getArea(cityid);
	}

	public List<Map<String, Object>> queryWorkerCardNoAndStatus(String workerId) {
		return workerDao.queryWorkerCardNoAndStatus(workerId);
	}

	public void updateZhifuWorkerCardStatus(String cardNo) {
		workerDao.updateZhifuWorkerCardStatus(cardNo);
		
	}

	public String queryWorkerCardNoWorking(String workerId) {
		return workerDao.queryWorkerCardNoWorking(workerId);
	}

	@Override
	public void updateZhifuWorkerCardStatusToParse(String cardNoWork) {
		workerDao.updateZhifuWorkerCardStatusToParse(cardNoWork);
		
	}

	@Override
	public String queryWorkerName(String workerId) {
		return workerDao.queryWorkerName(workerId);
	}

	@Override
	public void updateWorkerOpen() {
		workerDao.updateWorkerOpen();
		
	}

	@Override
	public void updateWorkerClose() {
		workerDao.updateWorkerClose();
	}

	@Override
	public void insertLog(Map<String, String> map) {
		workerDao.insertLog(map);
	}

	@Override
	public int queryOptionLogListCount() {
		return workerDao.queryOptionLogListCount();
	}

	@Override
	public List<Map<String, Object>> queryOptionLogListList(
			Map<String, Object> paramMap) {
		return workerDao.queryOptionLogListList(paramMap);
	}

	@Override
	public void updateWorkerByRobotId(WorkerVo worker) {
		workerDao.updateWorkerByRobotId(worker);
		
	}

	@Override
	public List<WorkerVo> queryWorkerByRobotId(String robot_id) {
		return workerDao.queryWorkerByRobotId(robot_id);
	}

//	@Override
//	public WorkerVo queryWorkerById(Map<String, Object> paramMap) {
//		return workerDao.queryWorkerById(paramMap);
//	}

	@Override
	public void updateRobotUrl(WorkerVo worker) {
		workerDao.updateRobotUrl(worker);
//		workerDao.insertLog(log);
	}
	
	@Override
	public void updateRobotStatus(WorkerVo worker, Map<String, String> log) {
		workerDao.updateRobotStatus(worker);
		workerDao.insertLog(log);
	}
	
	@Override
	public void updateRobotZfb(Map<String, Object> paramMap) {
		if("".equals(paramMap.get("zhifubaoNew"))){
			workerDao.updateRobotBfZfb(paramMap);
		}else{
			workerDao.updateRobotZfb(paramMap);
			workerDao.updateRobotBfZfb(paramMap);
		}
	}

	@Override
	public void updateComNo(Map<String, Object> paMap) {
		workerDao.updateComNo(paMap);
	}
	
	public String queryComNo(String worker_id) {
		return workerDao.queryComNo(worker_id);
	}
	
	@Override
	public int queryComNoCount(Map<String, Object> paramMap) {
		return workerDao.queryComNoCount(paramMap);
	}
	
@Override
public WorkerVo queryWorkerByWorkerId(String workerId) {
	return workerDao.queryWorkerByWorkerId(workerId);
}

@Override
public WorkerVo queryWorkerByRobotType(Map<String, Object> paramMap) {
	return workerDao.queryWorkerByRobotType(paramMap);
}

	
}
