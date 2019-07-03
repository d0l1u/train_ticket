package com.l9e.transaction.mq;

public interface MqService {
	
	
	/**
	 * 消息发送接口
	 * @param queue
	 * @param data
	 */
	public void sendMqMsg(String queue, final String data);
	
	/**
	 * 接收一条数据
	 * @param queue
	 * @return
	 */
	public Object receive(String queue);
}
