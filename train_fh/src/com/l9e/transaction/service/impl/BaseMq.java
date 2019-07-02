package com.l9e.transaction.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.stereotype.Service;

import com.l9e.common.Consts;


@Service("baseMq")
public class BaseMq {
	
	/*@Autowired
	private DefaultMessageListenerContainer jmsContainer;
	
	@PostConstruct
	public void init(){
		Consts.jmsContainer = this.jmsContainer;//注入消息监听对象
		
	}*/
	
}
