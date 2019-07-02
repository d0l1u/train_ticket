package com.l9e.transaction.service;

import com.l9e.transaction.vo.Worker;

/**
 * 机器人业务接口
 * 
 * @author licheng
 *
 */
public interface WorkerService {

	/**
	 * 准备机器人
	 * 
	 * @param type
	 *            机器人类型
	 * @param limit 
	 * @param logid
	 */
	void setWorker(Integer type, int limit, String logid);

	/**
	 * 根据指定类型获取一个队列中机器人
	 * 
	 * @param type
	 *            机器人类型
	 * @param logid 
	 * @return
	 */
	Worker getWorker(Integer type, String logid);

	/**
	 * 从库中查询机器人
	 * 
	 * @param workerId
	 *            机器人id
	 * @return
	 */
	Worker queryWorkerById(Integer workerId);

	/**
	 * 修改机器人信息
	 * 
	 * @param worker
	 *            机器人实体
	 */
	void updateWorker(Worker worker);

	/**
	 * 停用机器人
	 * 
	 * @param workerId
	 *            机器人id
	 * @param stopReason
	 *            停用原因
	 */
	Worker stopWorker(Integer workerId, String stopReason);

	/**
	 * 启用备用机器人
	 * 
	 * @param type
	 *            机器人类型
	 * @return
	 */
	Worker startPreparedWorker(Integer type);

	/**
	 * 随机查询一个java脚本机器人
	 * 
	 * @return Worker
	 */
	Worker queryOneJavaWorker();

	/**
	 * 切换美团云IP，把旧IP换成新购IP
	 * 
	 * @param ip_name
	 *            浮动IP的自定义名称 唯一(以字母开头，仅包含字母、数字或中划线的3-40个字符)
	 * @param oldIP
	 *            旧IP
	 * @return 新购IP
	 */
	String changeMtyunIp(String ip_name, String oldIP);
}
