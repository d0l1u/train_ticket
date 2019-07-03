package com.l9ea.train.service;

import com.l9e.train.po.Worker;


/**
 * 机器人服务接入
 * @author licheng
 *
 */
public interface WorkerService {

	/**
	 * 按渠道获取机器人
	 * @param type
	 * @return
	 */
	Worker getWorkerByType(Integer type);
	/**
	 * 按机器人主键id获取机器人
	 * @param id
	 * @return
	 */
	Worker getWorkerById(Integer id);
	/**
	 * 释放机器人至空闲
	 * @param id
	 */
	void releaseWorker(Integer id);
	/**
	 * 停用机器人
	 * @param id
	 */
	void stopWorker(Integer id);
	
	/**
	 * 从数据库中随机获取一个java脚本的机器人
	 * @return Worker
	 */
	Worker getOneJavaWorker();
}
