package com.l9e.transaction.service.impl;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.common.Consts;
import com.l9e.transaction.dao.RedisDao;
import com.l9e.transaction.service.AccountService;
import com.l9e.transaction.service.IpInfoService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.service.WorkerService;

@Service("baseService")
public class BaseService {
	@Resource
	private RedisDao redisDao;
	
	@Resource
	private AccountService accountService;
	
	@Resource
	private OrderService orderService;
	
	@Resource
	private WorkerService workerService;
	
	@Resource
	private IpInfoService ipInfoService;
	
	@PostConstruct
	public void init(){
		Consts.redisDao = this.redisDao;//注入redis
		Consts.accountService = this.accountService;
		Consts.orderService = this.orderService;
		Consts.workerService = this.workerService;
		Consts.ipInfoService = this.ipInfoService;
	}
}
