package com.l9e.transaction.job;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.WorkerService;

@Component("CloseBookRobotJob")
public class CloseBookRobotJob {
	private static final Logger logger = Logger.getLogger(CloseBookRobotJob.class);
	@Resource
	private WorkerService workerService;
	/**
	 * 自动关闭预定机器人
	 * @param Worker
	 * @param request
	 * @param response
	 * @return
	 */
	public void updateWorkerToClose(){
		logger.info("OpenBookRobotJob自动执行JOB，自动关闭预定机器人");
		workerService.updateWorkerClose();
		logger.info("OpenBookRobotJob执行完毕，预定机器人已经关闭，停用中");
	}
}
