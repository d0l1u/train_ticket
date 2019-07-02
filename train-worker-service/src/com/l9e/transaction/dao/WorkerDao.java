package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.Worker;

/**
 * 机器人持久层接口
 * @author licheng
 *
 */
public interface WorkerDao {

	/**
	 * 查询多个机器人
	 * @param params
	 * @return
	 */
	List<Worker> selectWorkers(Map<String, Object> params);
	
	/**
	 * 查询一个机器人
	 * @param params
	 * @return
	 */
	Worker selectOneWorker(Map<String, Object> params);
	
	/**
	 * 更新机器人
	 * @param worker
	 */
	int updateWorker(Worker worker);
	
	/**
	 * 查询补充队列的支付机器人(特殊方法，暂时启用)
	 * @param limit 
	 * @return
	 */
	List<Worker> selectQueuePayWorkers(int limit);
	
	/**
	 * 查询补充队列的核验机器人(特殊方法，暂时启用)
	 * @return
	 */
	List<Worker> selectQueueVerifyWorkers();
	
	/**
	 * 查询补充队列的注册机器人(特殊方法，暂时启用)
	 * @return
	 */
	List<Worker> selectQueueRegistWorkers();
	
	/**
	 * 随机查询一个java脚本机器人
	 * @return Worker
	 */
	Worker selectOneJavaWorker();
	
	/**
	 * 往cp_ipinfo_log表插入一条日志记录
	 * @param paramMap
	 */
	void insertIpInfoLog(Map<String, Object> paramMap);
	
	/**
	 * 往cp_ipinfo_release表插入一条待释放IP记录
	 * @param paramMap
	 */
	void insertIpInfoRelease(Map<String, Object> paramMap);
}
