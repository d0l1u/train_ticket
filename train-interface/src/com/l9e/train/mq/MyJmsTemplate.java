package com.l9e.train.mq;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.Message;
import org.springframework.jms.core.JmsTemplate;

/**
 * @ClassName: MyJmsTemplate 
 * @Description: 重写jms模板 
 * @author maorw 
 * @date 2015年2月6日 上午10:23:31 
 *
 */
public class MyJmsTemplate extends JmsTemplate {

	private Session session;

	public MyJmsTemplate() {
		super();
	}

	public MyJmsTemplate(ConnectionFactory connectionFactory) {
		super(connectionFactory);
	}

	public void doSend(MessageProducer producer, Message message) throws JMSException {
		if (isExplicitQosEnabled()) {
			producer.send(message, getDeliveryMode(), getPriority(), getTimeToLive());
		} else {
			producer.send(message);
		}
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
}