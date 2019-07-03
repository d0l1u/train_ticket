package com.l9e.transaction.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.WorkerService;
import com.l9e.util.HttpUtil;
import com.l9e.util.UrlFormatUtil;


/**
 * 启用停用（ip被封，关闭代理，预定机器人）的机器人
 */
@Component("openRobotJob")
public class OpenRobotJob {
	private static final Logger logger = Logger.getLogger(OpenRobotJob.class);
	
	@Resource
	private WorkerService workerService;

	public void openRobot() {
		logger.info("openRobotJob start~~~");
		List<Map<String, String>> list = workerService.queryCloseRobots();
		for(Map<String, String> map : list){
			String worker_ext = map.get("worker_ext");//http://localhost:8091/RunScript
			//?ScriptPath=isLocked.lua&SessionID=1&Timeout=120000&ParamCount=1
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("ScriptPath", "isLocked.lua");
			maps.put("SessionID", String.valueOf(System.currentTimeMillis()));
			maps.put("Timeout", "20000");
			maps.put("ParamCount", "1");

			try {
				String param = UrlFormatUtil.CreateUrl("", maps, "", "UTF-8");
				String reqResult = HttpUtil.sendByPost(worker_ext, param, "UTF-8");
				logger.info("worker_ext="+ worker_ext + ", reqResult=" + reqResult);
				
				if(StringUtils.isEmpty(reqResult) || reqResult==null || reqResult==""){//该机器异常
					String reqResult1 = HttpUtil.sendByPost(worker_ext, param, "UTF-8");
					logger.info("[重试1]worker_ext="+ worker_ext + ", reqResult1=" + reqResult1);
					if(StringUtils.isEmpty(reqResult1) || reqResult1==null || reqResult1==""){//该机器异常
						String reqResult2 = HttpUtil.sendByPost(worker_ext, param, "UTF-8");
						logger.info("[重试2]worker_ext="+ worker_ext + ", reqResult2=" + reqResult2);
						if(StringUtils.isEmpty(reqResult2) || reqResult2==null || reqResult2==""){//该机器异常
							Map<String, String> paramMap = new HashMap<String, String>();
							paramMap.put("worker_status", "99");//工作状态：00、工作中 11、空闲 22、停用 33、备用 44、占座中 99人工
//							paramMap.put("stop_reason", "00");
							paramMap.put("opt_name", "robot_job");
							paramMap.put("robot_id", map.get("robot_id"));
							
							Map<String, String> log = new HashMap<String, String>();
							log.put("content", "robot_job【异常返人工time out】" + worker_ext + "[22-->99]");
							log.put("opt_person", "robot_job");
							log.put("robot_name", map.get("worker_name"));
							log.put("worker_id", map.get("worker_id"));
							log.put("robot_id", map.get("robot_id"));
							workerService.updateRobotOpen(paramMap, log);
							logger.info("[返人工time out]机器人worker_ext:" + worker_ext);
						}else{//usabled是可用,ipLocked是不可用的
							if(reqResult2.contains("usabled")){//启用该机器人
								Map<String, String> paramMap = new HashMap<String, String>();
								paramMap.put("worker_status", "00");//工作状态：00、工作中 11、空闲 22、停用 33、备用 44、占座中
								paramMap.put("stop_reason", "00");
								paramMap.put("opt_name", "robot_job");
								paramMap.put("robot_id", map.get("robot_id"));
								
								Map<String, String> log = new HashMap<String, String>();
								log.put("content", "robot_job【启用】" + worker_ext + "[22-->00]");
								log.put("opt_person", "robot_job");
								log.put("robot_name", map.get("worker_name"));
								log.put("robot_id", map.get("robot_id"));
								workerService.updateRobotOpen(paramMap, log);
								logger.info("成功启用机器人worker_ext:" + worker_ext);
							}
						}
					}else{//usabled是可用,ipLocked是不可用的
						if(reqResult1.contains("usabled")){//启用该机器人
							Map<String, String> paramMap = new HashMap<String, String>();
							paramMap.put("worker_status", "00");//工作状态：00、工作中 11、空闲 22、停用 33、备用 44、占座中
							paramMap.put("stop_reason", "00");
							paramMap.put("opt_name", "robot_job");
							paramMap.put("robot_id", map.get("robot_id"));
							
							Map<String, String> log = new HashMap<String, String>();
							log.put("content", "robot_job【启用】" + worker_ext + "[22-->00]");
							log.put("opt_person", "robot_job");
							log.put("robot_name", map.get("worker_name"));
							log.put("robot_id", map.get("robot_id"));
							workerService.updateRobotOpen(paramMap, log);
							logger.info("成功启用机器人worker_ext:" + worker_ext);
						}
					}
					
				}else{//usabled是可用,ipLocked是不可用的
					if(reqResult.contains("usabled")){//启用该机器人
						Map<String, String> paramMap = new HashMap<String, String>();
						paramMap.put("worker_status", "00");//工作状态：00、工作中 11、空闲 22、停用 33、备用 44、占座中
						paramMap.put("stop_reason", "00");
						paramMap.put("opt_name", "robot_job");
						paramMap.put("robot_id", map.get("robot_id"));
						
						Map<String, String> log = new HashMap<String, String>();
						log.put("content", "robot_job【启用】" + worker_ext + "[22-->00]");
						log.put("opt_person", "robot_job");
						log.put("robot_name", map.get("worker_name"));
						log.put("robot_id", map.get("robot_id"));
						workerService.updateRobotOpen(paramMap, log);
						logger.info("成功启用机器人worker_ext:" + worker_ext);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		logger.info("openRobotJob end~~~");
	}
	
}
