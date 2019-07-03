package com.l9e.train.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l9e.train.po.Order;
import com.l9e.train.producerConsumer.distinct.DistinctProducerAndConsumer;
import com.l9e.train.thread.TimeoutConsumer;
import com.l9e.train.thread.TimeoutProducer;
import com.unlun.commons.res.Config;

public class TrainProcessMain {

	private static Logger logger = LoggerFactory.getLogger(TrainProcessMain.class);

	public static void main(String[] args) {

		logger.info("init Resource start");

		String databasePath = TrainProcessMain.class.getClassLoader().getResource("database.properties").getPath();
		Config.setConfigResource(databasePath);

		String consumerSize = Config.getProperty("consumersize");
		String queuesize = Config.getProperty("queuesize");
		String sleepMilliSecondsWhenNolist = Config.getProperty("sleepMilliSecondsWhenNolist");
		String sleepMilliSecondsWhenHaslist = Config.getProperty("sleepMilliSecondsWhenHaslist");

		logger.info("init Producer and Consumer consumerSize=" + consumerSize + " queuesize=" + queuesize + " sleepMilliSecondsWhenNolist="
				+ sleepMilliSecondsWhenNolist);
		logger.info("sleepMilliSecondsWhenHaslist=" + sleepMilliSecondsWhenHaslist);

		DistinctProducerAndConsumer<Order> container = new DistinctProducerAndConsumer<Order>();
		container.createDistinct(new String[] { TimeoutProducer.class.getName() }, TimeoutConsumer.class.getName(), Integer.parseInt(consumerSize),
				Integer.parseInt(queuesize), false, Integer.parseInt(sleepMilliSecondsWhenNolist), Integer.parseInt(sleepMilliSecondsWhenHaslist));

	}
}
