package com.l9e.transaction.mq;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

@Service("mqService")
public class MqServiceImpl implements MqService {

	@Autowired
	private MyJmsTemplate myJmsTemplate;
	
	@Override
	public void sendMqMsg(String queue, final String data) {
		/*
		 * 消息末班发送消息
		 */
		this.myJmsTemplate.send(queue,new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					TextMessage message = session.createTextMessage();
					message.setText(data);
					return message;
				}
			});
	}

	@Override
	public Message receive(String queue) {
		 Destination destination = new ActiveMQQueue(queue);
		 return this.myJmsTemplate.receive(destination);
	}

}
