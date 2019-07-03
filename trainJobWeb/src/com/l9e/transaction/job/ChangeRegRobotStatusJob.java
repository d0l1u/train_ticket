package com.l9e.transaction.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.WorkerService;
import com.l9e.transaction.vo.Worker;


@Component("changeRegRobotStatusJob")
public class ChangeRegRobotStatusJob {
	private static final Logger logger = Logger
			.getLogger(ChangeRegRobotStatusJob.class);
	
	@Resource
	private WorkerService  workerService;
	
	private static final Integer type=14;//注册机器人
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void  change(){
		
		logger.info("触发执行开始");
		
		HashMap parm=new HashMap();
		List <String> keyList=new ArrayList<String>();
		keyList.add("00");
		keyList.add("11");
		parm.put("worker_type", type);
		parm.put("keyList", keyList);
		
		
		List<Worker> res=workerService.selectRegWorker(parm);
		
		HashMap parm1=new HashMap();
		parm1.put("workstatus1", "00");//工作中
		parm1.put("workstatus2", "33");//备用
		for(Worker i:res){	
			
			if("00".equals(i.getWorkerStatus())){
				logger.info("代理状态不为（配置代理中,配置代理完成）的["+i.getWorkerName()+"]注册机器人状态：工作中-->备用");
				parm1.put("workerid", Integer.parseInt(i.getWorkerId()));
				workerService.updateRegWorker1(parm1);
				
			}else if("33".equals(i.getWorkerStatus())){
				logger.info("代理状态不为（配置代理中,配置代理完成）的["+i.getWorkerName()+"]注册机器人状态：备用-->工作中");
				parm1.put("workerid", Integer.parseInt(i.getWorkerId()));
				workerService.updateRegWorker2(parm1);
			}
			
			
		}

		logger.info("触发执行完毕");
		
		System.out.println("------change------");
		System.out.println(res);
		
		
	}
	

}
