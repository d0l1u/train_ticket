package com.l9e.train.channel.request.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.l9e.train.po.Account;
import com.l9e.train.po.Order;
import com.l9e.train.po.Worker;

public class RobotOrderRequestTest {

	private Order order;
	
	@Test
	public void testRequest() {
		order = new Order();
		order.setOrderstr("HC0102021021|北京南|上海|2013-05-29|D321");
		
		String str = "郭斌|0|2|110101198101010157|3";
		order.addOrderCp(str);
		
		
		Account account = new Account();
		account.setAccPassword("123456a");
		account.setAccUsername("pek_19");
		
		Worker worker = new Worker();
		worker.setWorkerExt("http://192.168.12.5:8090/RunScript");
		
		
		RobotCancelRequest request = new RobotCancelRequest(account, worker);
		
		
		request.request(order);
		
	}

}
