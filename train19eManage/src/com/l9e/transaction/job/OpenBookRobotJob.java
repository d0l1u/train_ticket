package com.l9e.transaction.job;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.transaction.service.WorkerService;
import com.l9e.transaction.vo.WorkerVo;

@Component("OpenBookRobotJob")
public class OpenBookRobotJob {
	private static final Logger logger = Logger.getLogger(OpenBookRobotJob.class);
	@Resource
	private WorkerService workerService;
	/**
	 * 开启预定机器人
	 * @param Worker
	 * @param request
	 * @param response
	 * @return
	 */
	public void updateWorkerToOpen(){
		logger.info("OpenBookRobotJob自动执行JOB，自动开启预定机器人");
		workerService.updateWorkerOpen();
		logger.info("OpenBookRobotJob执行完毕，预定机器人已经开启，工作中");
	}
	
	
}
