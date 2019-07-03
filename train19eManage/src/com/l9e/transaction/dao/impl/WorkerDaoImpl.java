package com.l9e.transaction.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.WorkerDao;
import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.RobotSettingVo;
import com.l9e.transaction.vo.WorkerVo;

@Repository("workerDao")
public class WorkerDaoImpl extends BaseDao implements WorkerDao{

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryWorkerList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("worker.queryWorkerList", paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryWorkerList2(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("worker.queryWorkerList2", paramMap);
	}

	@SuppressWarnings("unchecked")
	public int queryWorkerListCount(Map<String, Object> paramMap) {
		return getTotalRows("worker.queryWorkerListCount", paramMap);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryWorker(String worker_id) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("worker.queryWorker", worker_id);
	}

	@SuppressWarnings("unchecked")
	public void updateWorker(WorkerVo  Worker) {
		this.getSqlMapClientTemplate().update("worker.updateWorker", Worker);
	}

	@SuppressWarnings("unchecked")
	public void insertWorker(WorkerVo  Worker) {
		this.getSqlMapClientTemplate().insert("worker.insertWorker", Worker);
	}

	@SuppressWarnings("unchecked")
	public void deleteWorker(WorkerVo Worker) {
		this.getSqlMapClientTemplate().delete("worker.deleteWorker", Worker);
	}
	@SuppressWarnings("unchecked")
	public List<AreaVo> getProvince() {
		return this.getSqlMapClientTemplate().queryForList("worker.getProvince");
	}

	@SuppressWarnings("unchecked")
	public List<AreaVo> getCity(String provinceid) {
		return this.getSqlMapClientTemplate().queryForList("worker.getCity", provinceid);
	}

	@SuppressWarnings("unchecked")
	public List<AreaVo> getArea(String cityid) {
		return this.getSqlMapClientTemplate().queryForList("worker.getArea", cityid);
	}

	public List<Map<String, Object>> queryWorkerCardNoAndStatus(String workerId) {
		return this.getSqlMapClientTemplate().queryForList("worker.queryWorkerCardNoAndStatus", workerId);
	}

	public void updateZhifuWorkerCardStatus(String cardNo) {
		this.getSqlMapClientTemplate().update("worker.updateZhifuWorkerCardStatus", cardNo);
	}

	public String queryWorkerCardNoWorking(String workerId) {
		return (String) this.getSqlMapClientTemplate().queryForObject("worker.queryWorkerCardNoWorking", workerId);
	}

	public void updateZhifuWorkerCardStatusToParse(String cardNoWork) {
		this.getSqlMapClientTemplate().update("worker.updateZhifuWorkerCardStatusToParse", cardNoWork);
		
	}

	public String queryWorkerName(String workerId) {
		return (String) this.getSqlMapClientTemplate().queryForObject("worker.queryWorkerName", workerId);
	}

	public void updateWorkerOpen() {
		this.getSqlMapClientTemplate().update("worker.updateWorkerOpen");
		
	}

	@Override
	public void updateWorkerClose() {
		this.getSqlMapClientTemplate().update("worker.updateWorkerClose");
	}

	@Override
	public void insertLog(Map<String, String> map) {
		this.getSqlMapClientTemplate().insert("worker.insertLog", map);
	}

	@Override
	public int queryOptionLogListCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("worker.queryOptionLogListCount");
	}

	@Override
	public List<Map<String, Object>> queryOptionLogListList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("worker.queryOptionLogListList", paramMap);
	}

	@Override
	public void updateWorkerByRobotId(WorkerVo Worker) {
		this.getSqlMapClientTemplate().update("worker.updateWorkerByRobotId", Worker);
	}

	@Override
	public List<WorkerVo> queryWorkerByRobotId(String robot_id) {
		return this.getSqlMapClientTemplate().queryForList("worker.queryWorkerByRobotId", robot_id);
	}
	
	@Override
	public WorkerVo queryWorkerByWorkerId(String worker_id) {
		return (WorkerVo)(this.getSqlMapClientTemplate().queryForObject("worker.queryWorkerByWorkerId", worker_id));
	}

//	@Override
//	public WorkerVo queryWorkerById(Map<String, Object> paramMap) {
//		try {
//			return (WorkerVo)(this.getSqlMapClient().queryForObject("worker.queryWorkerById", paramMap));
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

	@Override
	public void updateRobotUrl(WorkerVo worker) {
		this.getSqlMapClientTemplate().update("worker.updateRobotUrl",worker);
	}
	
	@Override
	public void updateRobotStatus(WorkerVo worker) {
		this.getSqlMapClientTemplate().update("worker.updateRobotStatus",worker);
	}

	@Override
	public void updateRobotZfb(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().update("worker.updateRobotZfb",paramMap);
	}
	
	@Override
	public void updateRobotBfZfb(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().update("worker.updateRobotBfZfb",paramMap);
	}
	
	@Override
	public void updateComNo(Map<String, Object> paMap) {
		this.getSqlMapClientTemplate().update("worker.updateComNo",paMap);
	}
	
	@Override
	public WorkerVo queryWorkerByRobotType(Map<String, Object> paramMap) {
		try {
			return (WorkerVo)(this.getSqlMapClient().queryForObject("worker.queryWorkerByRobotType", paramMap));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String queryComNo(String worker_id) {
		return (String) this.getSqlMapClientTemplate().queryForObject("worker.queryComNo", worker_id);
	}
	
	@Override
	public int queryComNoCount(Map<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("worker.queryComNoCount",paramMap);
	}
}
