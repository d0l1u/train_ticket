package com.l9e.train.service.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.l9e.train.po.Order;
import com.l9e.train.po.OrderCP;
import com.l9e.train.po.Result;
import com.l9e.train.po.Worker;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;
import com.unlun.commons.res.Config;

public class TrainServiceImplTest  extends TestCase{

	
	TrainServiceImpl service = new TrainServiceImpl();
	
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		Config.setConfigResource("D:\\workspace\\train_interface_dev\\src\\config.properties");
		Config.setConfigResource("D:\\workspace\\train_interface_dev\\src\\database.properties");
	}
	
	@Test
	public void testOrderIsSuccess() {
	
		Worker worker = new Worker();
		
		worker.setWorkerType(Worker.Robot_12306);
		
		
		Order order = new Order();
		order.setOrderId("HC1305301541171026");
		
		OrderCP cp = new OrderCP();
		
		cp.setSeatNo("12");
		cp.setTrainbox("2");
		cp.setCpId("HC1305301541171026");
		
		List<OrderCP> list =new ArrayList<OrderCP>();
		
		list.add(cp);
		
		
		Result result = new Result();
		
		result.setWorker(worker);
		//result.setOrderCps(list);
		try {
			service.payIsSuccess(order, result);
		} catch (RepeatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}

}
