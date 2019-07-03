package com.l9e.train.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l9e.train.po.Order;
import com.l9e.train.producerConsumer.distinct.DistinctProducerAndConsumer;
import com.l9e.train.thread.TimeoutConsumer;
import com.l9e.train.thread.TimeoutProducer;
import com.l9e.train.util.ConfigUtil;
import com.unlun.commons.res.Config;

/**
 * @ClassName: TrainProcessMain
 * @Description: 程序入口
 * @author: taoka
 * @date: 2018年2月26日 下午2:26:49
 * @Copyright: 2018 www.19e.cn Inc. All rights reserved.
 */
public class TrainProcessMain {

	private static Logger logger = LoggerFactory.getLogger(TrainProcessMain.class);

	public static void main(String[] args) {

		logger.info("*** =======================================");
		logger.info("*******************************************");
		logger.info("***   Loader Config Resource start");
		String databasePath = TrainProcessMain.class.getClassLoader().getResource("database.properties").getPath();
		Config.setConfigResource(databasePath);

		logger.info("*******************************************");
		logger.info("***   Loader Config Resource start");
		String consumerSize = ConfigUtil.getValue("consumersize");
		String queuesize = ConfigUtil.getValue("queuesize");
		String sleepNolist = ConfigUtil.getValue("sleepMilliSecondsWhenNolist");
		String sleepHaslist = ConfigUtil.getValue("sleepMilliSecondsWhenHaslist");
		logger.info("***   consumerSize:{}, queuesize:{}", consumerSize, queuesize);
		logger.info("***   When No List Sleep MilliSeconds:{}", sleepNolist);
		logger.info("***   When Has List Sleep MilliSeconds:{}", sleepHaslist);

		logger.info("*******************************************");
		logger.info("***   DistinctProducerAndConsumer CreateDistinct start");
		DistinctProducerAndConsumer<Order> container = new DistinctProducerAndConsumer<Order>();
		container.createDistinct(new String[] { TimeoutProducer.class.getName() }, TimeoutConsumer.class.getName(), Integer.parseInt(consumerSize),
				Integer.parseInt(queuesize), false, Integer.parseInt(sleepNolist), Integer.parseInt(sleepHaslist));

		logger.info("*******************************************");
		logger.info("*** =======================================");
	}
}
