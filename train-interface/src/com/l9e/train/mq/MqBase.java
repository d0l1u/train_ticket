package com.l9e.train.mq;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.l9e.train.util.Contes;

@Service("mqBase")
public class MqBase {
	
	@Autowired
	private MqService mqService;
	
	@PostConstruct
	public void init(){
		Contes.mqUtil.put("mq", mqService);
	}
	
}
