package com.l9e.train.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l9e.train.po.Order;
import com.l9e.train.producerConsumer.distinct.DistinctProducerAndConsumer;
import com.l9e.train.thread.TimeoutConsumer;
import com.l9e.train.thread.TimeoutProducer;
import com.l9e.train.util.ConfigUtil;
import com.l9e.train.util.Consts;
import com.l9e.train.util.WorkIdNum;
import com.l9ea.train.dao.RedisDao;
import com.l9ea.train.service.AccountService;
import com.l9ea.train.service.IpInfoService;
import com.l9ea.train.service.OrderService;
import com.l9ea.train.service.WorkerService;
import com.train.commons.jedis.SingleJedisClient;
import com.unlun.commons.res.Config;

public class TrainProcessMain {

	private static Logger logger = LoggerFactory.getLogger(TrainProcessMain.class);

	public static void main(String[] args) {
		logger.info("### ================ Start Service ===============");
		try {
			logger.info("### Loading SpringContext ...");
			Class.forName("com.l9e.train.main.SpringContext");

			App.workerService = SpringContext.getBean("workerService", WorkerService.class);
			App.accountService = SpringContext.getBean("accountService", AccountService.class);
			App.orderService = SpringContext.getBean("orderService", OrderService.class);
			App.redisDao = SpringContext.getBean("redisDao", RedisDao.class);
			App.ipInfoService = SpringContext.getBean("ipInfoService", IpInfoService.class);
			App.jedisClient = SpringContext.getBean("jedisClient", SingleJedisClient.class);
			
			System.err.println("App.jedisClient:"+App.jedisClient);

			String databasePath = TrainProcessMain.class.getClassLoader().getResource("database.properties").getPath();
			Config.setConfigResource(databasePath);

			logger.info("### Loading Service Url ...");
			loadServiceUrl();

			WorkIdNum.limit_day = Integer.valueOf(ConfigUtil.getValue("limit_day"));
			WorkIdNum.contact_num = Integer.valueOf(ConfigUtil.getValue("contact_num"));
			logger.info("### WorkIdNum limit_day:{}", WorkIdNum.limit_day);
			logger.info("### WorkIdNum contact_num:{}", WorkIdNum.contact_num);

			Integer consumerSize = Integer.valueOf(ConfigUtil.getValue("consumersize"));
			Integer queuesize = Integer.valueOf(ConfigUtil.getValue("queuesize"));
			Integer sleepWhenNolist = Integer.valueOf(ConfigUtil.getValue("sleepMilliSecondsWhenNolist"));
			Integer sleepWhenHaslist = Integer.valueOf(ConfigUtil.getValue("sleepMilliSecondsWhenHaslist"));
			logger.info("### Consumer Size:{}, Queue Size:{}", consumerSize, queuesize);
			logger.info("### Sleep When No List:{}, Sleep When Have List:{}", sleepWhenNolist, sleepWhenHaslist);

			DistinctProducerAndConsumer<Order> container = new DistinctProducerAndConsumer<Order>();
			container.createDistinct(new String[] { TimeoutProducer.class.getName() }, TimeoutConsumer.class.getName(), consumerSize, queuesize, false,
					sleepWhenNolist, sleepWhenHaslist);
		} catch (Exception e) {
			logger.info("### 【Service Start Exception】", e);
		}

	}

	public static void loadServiceUrl() {
		/* 机器人服务 */
		Consts.GET_WORKER_BY_ID = ConfigUtil.getValue("getWorkerById");
		Consts.GET_WORKER_BY_TYPE = ConfigUtil.getValue("getWorkerByType");
		Consts.RELEASE_WORKER = ConfigUtil.getValue("releaseWorker");
		Consts.STOP_WORKER = ConfigUtil.getValue("stopWorker");
		Consts.GET_JAVA_WORKER = ConfigUtil.getValue("getOneJavaWorker");

		/* 账号服务 */
		Consts.GET_CHANNEL_ACCOUNT = ConfigUtil.getValue("getChannelAccount");
		Consts.GET_ORDER_ACCOUNT = ConfigUtil.getValue("getOrderAccount");
		Consts.STOP_ACCOUNT = ConfigUtil.getValue("stopAccount");
		Consts.UPDATE_ACCOUNT = ConfigUtil.getValue("updateAccount");
		Consts.FILTER_ACCOUNT = ConfigUtil.getValue("filterAccount");
		Consts.RELEASE_ACCOUNT = ConfigUtil.getValue("releaseAccount");
		
		// 自带12306账号错误信息处理
		Consts.HANDLE_BIND_ACCOUNT = ConfigUtil.getValue("handleBindAccErrorCode");

		/* 订单服务 */
		Consts.GET_ORDER_PASSENGER = ConfigUtil.getValue("getOrderPassenger");
		Consts.UPDATE_ORDER = ConfigUtil.getValue("updateOrder");
	}
}
