package com.l9e.transaction.service.impl;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.common.Consts;
import com.l9e.transaction.service.BaseHttpService;
import com.l9e.transaction.service.WorkerService;
import com.l9e.transaction.vo.Worker;


@Service("workerService")
public class WorkerServiceImpl extends BaseHttpService implements WorkerService {
	
	private static final Logger logger = Logger.getLogger(WorkerServiceImpl.class);

	@Override
	public Worker getWorkerById(Integer id) {
		
		Worker worker = null;
		
		try {
			String result = requestPost(Consts.GET_WORKER_BY_ID, "workerId=" + id, "utf-8", 3, 500);
			logger.info("get worker by id , worker result : " + result);
			if(!StringUtils.isEmpty(result)) {
				JSONObject resultJsonObject = JSONObject.fromObject(result);
				if(resultJsonObject.has("success") && resultJsonObject.getBoolean("success")) {
					worker = (Worker) JSONObject.toBean(resultJsonObject.getJSONObject("data"), Worker.class);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return worker;
	}

	@Override
	public Worker getWorkerByType(Integer type) {
		Worker worker = null;
		
		int count = 0;
		do {
			try {
				String result = requestPost(Consts.GET_WORKER_BY_TYPE, "type=" + type, "UTF-8", 3, 500);
				logger.info("get worker by type, worker result : " + result);
				if(!StringUtils.isEmpty(result)) {
					JSONObject resultJsonObject = JSONObject.fromObject(result);
					if(resultJsonObject.has("success") && resultJsonObject.getBoolean("success")) {
						worker = (Worker) JSONObject.toBean(resultJsonObject.getJSONObject("data"), Worker.class);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			count++;
		} while(worker == null && count < 5);
		return worker;
	}

	@Override
	public void releaseWorker(Integer id) {
		try {
			String result = requestPost(Consts.RELEASE_WORKER, "workerId=" + id, "utf-8", 0, 0);
			logger.info("release worker result : " + result);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void stopWorker(Integer id) {
		try {
			String result = requestPost(Consts.STOP_WORKER, "reason=22&workerId=" + id, "utf-8", 0, 0);
			logger.info("stop worker result : " + result);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Worker getOneJavaWorker() {
		// TODO Auto-generated method stub

		Worker worker = null;
		
		try {
			String result = requestPost(Consts.GET_JAVA_WORKER,"","utf-8", 3, 500);
			logger.info("get one java worker , worker result : " + result);
			if(!StringUtils.isEmpty(result)) {
				JSONObject resultJsonObject = JSONObject.fromObject(result);
				if(resultJsonObject.has("success") && resultJsonObject.getBoolean("success")) {
					worker = (Worker) JSONObject.toBean(resultJsonObject.getJSONObject("data"), Worker.class);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return worker;
	}

}
