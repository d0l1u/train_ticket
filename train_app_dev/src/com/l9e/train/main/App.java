package com.l9e.train.main;

import com.l9ea.train.dao.RedisDao;
import com.l9ea.train.service.AccountService;
import com.l9ea.train.service.IpInfoService;
import com.l9ea.train.service.OrderService;
import com.l9ea.train.service.WorkerService;
import com.train.commons.jedis.SingleJedisClient;

/**
 * 单例对象(重构中，暂时使用)
 * @author licheng
 *
 */
public class App {

	public static WorkerService workerService;
	public static AccountService accountService;
	public static OrderService orderService;
	public static RedisDao redisDao;
	public static IpInfoService ipInfoService;
	public static SingleJedisClient jedisClient;
}
