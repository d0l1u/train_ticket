package com.l9e.transaction.service;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.l9e.transaction.vo.WorkerVo;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:context/applicationContext.xml")
public class WorkerServiceTest {

	
	@Resource
	WorkerService workerService;
	
	@Test
	public void testQueryWorkerList() {
		
		WorkerVo Worker = new WorkerVo();
		Worker.setWorker_name("test");
		Worker.setWorker_ext("test");
		
		workerService.insertWorker(Worker);
		
		Worker = new WorkerVo();
		Worker.setWorker_name("test2");
		Worker.setWorker_ext("test");
		workerService.insertWorker(Worker);
		
		
		int count = workerService.queryWorkerListCount(null);
		
		System.out.println("count:"+count);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("everyPagefrom", 0);
		params.put("pageSize", 6);
		
		List<Map<String, Object>> Workers = workerService.queryWorkerList(params);
		
		System.out.println("Workers:"+Workers.size());
		
		for (Map<String, Object> map : Workers) {
			Map<String, Object> Worker1 =  workerService.queryWorker(String.valueOf(map.get("worker_id")));
			
			System.out.println(Worker1.get("worker_name"));
			
			Worker = new WorkerVo();
			Worker.setWorker_id(String.valueOf(Worker1.get("worker_id")));
			Worker.setWorker_name("test3333");
			
			workerService.updateWorker(Worker);
			
			
		}
		
	}

}
