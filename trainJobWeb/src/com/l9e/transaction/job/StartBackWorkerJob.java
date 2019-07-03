package com.l9e.transaction.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.WorkerService;
import com.l9e.transaction.vo.Robot;
import com.l9e.transaction.vo.Worker;

@Component("startBackWorkerJob")
public class StartBackWorkerJob {

	private static final Logger logger = Logger
			.getLogger(StartBackWorkerJob.class);

	@Resource
	private WorkerService workerService;

	public void startBackWorker() {
		Map<String, Object> workingRobotMap = new HashMap<String, Object>();
		workingRobotMap.put("worker_status", Worker.STATUS_WORKING);
		workingRobotMap.put("worker_type", Worker.BOOK_ROBOT);
		logger.info("【处于预定状态的预定机器人】查询参数为：" + workingRobotMap);
		List<Worker> workingBookWorkerList = workerService
				.selectWorker(workingRobotMap);
		logger.info("【处于预定状态的预定机器人】机器人个数为：" + workingBookWorkerList.size());

		Map<String, Object> spareBookWorkerMap = new HashMap<String, Object>();
		spareBookWorkerMap.put("worker_status", Worker.STATUS_SPARE);
		spareBookWorkerMap.put("worker_type", Worker.BOOK_ROBOT);
		logger.info("【处于备用状态的预定机器人】查询参数为：" + spareBookWorkerMap);
		List<Worker> spareBookWorkerList = workerService
				.selectWorker(spareBookWorkerMap);
		logger.info("【处于备用状态的预定机器人】机器人个数为：" + spareBookWorkerList.size());

		// 将备用状态的预定机器人置为预定状态
		Map<String, Object> spareRobotMap = new HashMap<String, Object>();
		List<Robot> robotList = null;
		for (Worker worker : spareBookWorkerList) {
			worker.setWorkerStatus(Worker.STATUS_WORKING);
			logger.info("【处于备用状态的预定机器人】机器人worker_name为："
					+ worker.getWorkerName());
			if (worker.getWorkerId() == null || "".equals(worker.getRobotId())) {
				logger.info("【处于备用状态的预定机器人】机器人worker_name为："
						+ worker.getWorkerName() + "的预订机器人id为空， 不予更新！");
			} else {
				workerService.updateWorkerObject(worker);
			}
			String robot_id = worker.getRobotId();
			if (robot_id != null && !"".equalsIgnoreCase(robot_id)) {
				spareRobotMap.put("robot_id", worker.getRobotId());
				logger.info("查询备用状态预订机器人所属机器参数为：" + spareRobotMap);
				robotList = workerService.selectRobot(spareRobotMap);
				logger.info("查询备用状态预订机器人所属机器条数：" + robotList.size());
				if (robotList.size()>0) {
					Robot robot = robotList.get(0);
					logger.info("需要更新为启用状态的机器人所属机器名称为：" + robot.getRobot_name());
					robot.setRobot_book("1");		//将备用机器人改为启用状态
					workerService.updateRobot(robot);
				}
			}
		}

		// 将预定状态的预定机器人置为备用状态
		for (Worker worker : workingBookWorkerList) {
			worker.setWorkerStatus(Worker.STATUS_SPARE);
			logger.info("【处于预定状态的预定机器人】机器人worker_name为："
					+ worker.getWorkerName());
			if (worker.getWorkerId() == null || "".equals(worker.getRobotId())) {
				logger.info("【处于预定状态的预定机器人】机器人worker_name为："
						+ worker.getWorkerName() + "的预订机器人id为空， 不予更新！");
			} else {
				workerService.updateWorkerObject(worker);
			}
			String robot_id = worker.getRobotId();
			if (robot_id != null && !"".equalsIgnoreCase(robot_id)) {
				spareRobotMap.put("robot_id", worker.getRobotId());
				logger.info("查询预订机器人所属机器参数为：" + spareRobotMap);
				robotList = workerService.selectRobot(spareRobotMap);
				logger.info("查询开启状态预订机器人所属机器条数：" + robotList.size());
				if (robotList.size()>0) {
					Robot robot = robotList.get(0);
					logger.info("需要更新为启用状态的机器人所属机器名称为：" + robot.getRobot_name());
					robot.setRobot_book("3");		//将预订机器人改为备用状态
					workerService.updateRobot(robot);
				}
			}
		}
	}
}
