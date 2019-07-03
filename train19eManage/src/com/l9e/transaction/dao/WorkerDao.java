package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.WorkerVo;
import com.l9e.transaction.vo.AcquireVo;

public interface WorkerDao {
	List<Map<String, Object>> queryWorkerList(Map<String, Object> paramMap);
	
	List<Map<String, Object>> queryWorkerList2(Map<String, Object> paramMap);
	
//	public WorkerVo queryWorkerById(Map<String, Object> paramMap);

	int queryWorkerListCount(Map<String, Object> paramMap);

	Map<String, Object> queryWorker(String worker_id);
	
	public List<WorkerVo> queryWorkerByRobotId(String robot_id);
	
	public WorkerVo queryWorkerByWorkerId(String worker_id);

	public WorkerVo queryWorkerByRobotType(Map<String, Object> paramMap);
	
	void updateWorker(WorkerVo  worker);
	
	void updateWorkerByRobotId(WorkerVo  Worker);
	
	void insertWorker(WorkerVo  worker);
	
	void deleteWorker(WorkerVo  worker);
	
	/**
	 * 获取省份
	 * @return
	 */
	public List<AreaVo> getProvince();
	
	
	/**
	 * 获取城市
	 * @return
	 */
	public List<AreaVo>  getCity(String provinceid);
	
	
	/**
	 * 获取区县
	 * @return
	 */
	public List<AreaVo>  getArea(String cityid);

	List<Map<String, Object>> queryWorkerCardNoAndStatus(String workerId);

	void updateZhifuWorkerCardStatus(String cardNo);

	String queryWorkerCardNoWorking(String workerId);

	void updateZhifuWorkerCardStatusToParse(String cardNoWork);

	String queryWorkerName(String workerId);

	void updateWorkerOpen();

	void updateWorkerClose();

	void insertLog(Map<String, String> map);

	int queryOptionLogListCount();

	List<Map<String, Object>> queryOptionLogListList(
			Map<String, Object> paramMap);
	
	public void updateRobotUrl(WorkerVo worker);
	
	public void updateRobotStatus(WorkerVo worker);
	
	public void updateRobotZfb(Map<String, Object> paramMap);
	
	public void updateRobotBfZfb(Map<String, Object> paramMap);

	public void updateComNo(Map<String, Object> paMap);
	
	String queryComNo(String worker_id);
	
	public int queryComNoCount(Map<String, Object> paramMap);
}
