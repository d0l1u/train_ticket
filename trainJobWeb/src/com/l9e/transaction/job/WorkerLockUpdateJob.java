package com.l9e.transaction.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.l9e.transaction.service.WorkerService;
import com.l9e.transaction.vo.Worker;
import com.l9e.util.HttpUtil;

/**
 * 解锁机器操作时间距离当前时间大于10分钟，并且机器已锁定的所有lua机器人
 * @author wangsf01
 *
 */
@Component("workerLockUpdateJob")
public class WorkerLockUpdateJob {

    private static final Logger logger = Logger.getLogger(WorkerLockUpdateJob.class);
	
    @Resource
	private WorkerService workerService;
	
	@Value("#{propertiesReader[releaseWorker]}")
	private  String releaseWorker;//释放机器人url
	
	public void updateWorkerLockStatus(){
		logger.info("workerLockUpdateJob start~~~");
		List<Worker> workerLockList = null;
		
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("workerLock", 1);//1：锁定   0：不锁定
		queryMap.put("workerType", 1);//1：预定机器人
		queryMap.put("optionTime", "now()");
		
		//查询锁定时间大于10分钟的所有预定lua版机器人
		workerLockList = workerService.queryTimeOutWorkerList(queryMap);
		logger.info("workerLockUpdateJob 查询得到的workerLockList为: "+ workerLockList);
		if(workerLockList.size() > 0){
			Worker worker = null;
			Integer workerId = null;
			
			for(int i=0;i<workerLockList.size();i++){
				worker = workerLockList.get(i);
				logger.info("workerLockUpdateJob 循环遍历得到的worker为: "+ worker);
				workerId = Integer.valueOf(worker.getWorkerId());
				logger.info("workerLockUpdateJob~~workerID为: "+ workerId);
				
				//解锁机器人
				logger.info("workerLockUpdateJob~~解锁机器人的请求地址releaseWorker为: "+ releaseWorker);
				String result = HttpUtil.sendByPost(releaseWorker, "workerId=" + workerId, "utf-8");
				logger.info("workerLockUpdateJob 机器人释放结果result为: "+ result);
				
			}
			
		}
		
	}
}
