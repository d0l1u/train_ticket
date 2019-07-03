package com.l9e.transaction.job;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.AccountService;

@Component("ChangeStatus")
public class ChangeStatus {
	private static final Logger logger = Logger.getLogger(ChangeStatus.class);
	
	@Resource
	private AccountService accountService;
	
	
	//自动开启停用原因为为取消订单过多的账户
	public void updateStopStatus(){
		logger.info("自动开启停用原因为为取消订单过多的账户");
		accountService.updateStopStatus();
		logger.info("账户开启完毕");
	}
}
