package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.WorkerDao;
import com.l9e.transaction.vo.Worker;

@Repository("workerDao")
public class WorkerDaoImpl extends BaseDao implements WorkerDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Worker> selectWorkers(Map<String, Object> params) {
		return getSqlMapClientTemplate().queryForList("worker.selectWorker", params);
	}

	@Override
	public int updateWorker(Worker worker) {
		return getSqlMapClientTemplate().update("worker.updateWorker", worker);
	}

	@Override
	public Worker selectOneWorker(Map<String, Object> params) {
		return (Worker) getSqlMapClientTemplate().queryForObject("worker.selectWorker", params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Worker> selectQueuePayWorkers(int limit) {
		return getSqlMapClientTemplate().queryForList("worker.selectQueuePayWorker", limit);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Worker> selectQueueVerifyWorkers() {
		return getSqlMapClientTemplate().queryForList("worker.selectQueueVerifyWorker");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Worker> selectQueueRegistWorkers() {
		return getSqlMapClientTemplate().queryForList("worker.selectQueueRegistWorker");
	}

	/**
	 * 随机查询一个java脚本机器人
	 * 
	 * @return Worker
	 */
	@Override
	public Worker selectOneJavaWorker() {
		// TODO Auto-generated method stub
		return (Worker) this.getSqlMapClientTemplate().queryForObject("worker.selectOneJavaWorker");
	}

	/**
	 * 往cp_ipinfo_log表插入一条日志记录
	 * 
	 * @param paramMap
	 */
	@Override
	public void insertIpInfoLog(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().insert("worker.insertIpInfoLog", paramMap);

	}

	/**
	 * 往cp_ipinfo_release表插入一条待释放IP记录
	 * 
	 * @param paramMap
	 */
	@Override
	public void insertIpInfoRelease(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().insert("worker.insertIpInfoRelease", paramMap);
	}
}
