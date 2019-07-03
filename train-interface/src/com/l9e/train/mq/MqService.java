package com.l9e.train.mq;

public interface MqService {
	
	
	/**
	 * 消息发送接口
	 * @param queue
	 * @param data
	 */
	public void sendMqMsg(String queue,final String data);
}
