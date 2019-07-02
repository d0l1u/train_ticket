package com.l9e.common;

import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;

//import com.l9e.transaction.service.SystemService;
import com.l9e.transaction.service.WorkerService;
import com.l9e.util.StrUtil;
import com.train.commons.jedis.SingleJedisClient;

public abstract class WorkerBase {
	private static Logger logger = Logger.getLogger(WorkerBase.class);

	private Timer timer;

	/**
	 * 队列最大值
	 * 
	 * @author: taoka
	 * @date: 2018年3月1日 下午1:42:12
	 * @return Integer
	 */
	public abstract Integer maxSize();

	/**
	 * 机器人类型
	 * 
	 * @return
	 */
	public abstract Integer type();

	/**
	 * 任务执行时间
	 * 
	 * @author: taoka
	 * @date: 2018年3月1日 下午1:47:57
	 * @return Integer
	 */
	public abstract Integer executeMilliseconds();

	public abstract String createLogid();

	@Resource
	private WorkerService workerService;

	@Resource
	private SingleJedisClient jedisClient;

	@PostConstruct
	public void init() {
		this.supplementWorker(type(), maxSize(), executeMilliseconds(), createLogid());
	}

	/**
	 * 补充机器人队列
	 * 
	 * @param executeMilliseconds
	 * @param waitsec
	 * @param maxSize
	 */
	public void supplementWorker(final Integer type, final int maxSize, int executeMilliseconds, final String logid) {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}

		logger.info(logid + "========== Supplement Begin ==========");
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					int size = maxSize;
					String key = StrUtil.getWorkerQueue(type);
					Long lLen = jedisClient.llen(key);
					logger.info(logid + key + " length:" + lLen);
					int limit = size - Long.valueOf(lLen).intValue();
					if (limit > 0) {
						workerService.setWorker(type, limit, logid);
					}
				} catch (Exception e) {
					logger.info(logid + "【supplementWorker() Exception】" + e.getClass().getSimpleName(), e);
				}
			}
		}, 0, executeMilliseconds);
	}

}
