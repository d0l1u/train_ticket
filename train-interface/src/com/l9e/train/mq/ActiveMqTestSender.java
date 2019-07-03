package com.l9e.train.mq;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.l9e.train.util.Contes;



public class ActiveMqTestSender implements Runnable {

	private MqService mqService;
	public ActiveMqTestSender (MqService mqService){
		this.mqService = mqService;
		
	}

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:context/applicationContext.xml");

		MqService  helloService = Contes.mqUtil.get("mq");

		//helloService.sendMqMsg( "woqu");
		
	/*	 for(int i=0 ;i<100000;i++){
			 Thread thread = new Thread(new ActiveMqTestSender(helloService));
	        	System.out.println("开始发送消息");
	        	thread.start();
	        } */
	}

	@Override
	public void run() {
		this.mqService.sendMqMsg("test","woqu");
	}
	
	
}
