package com.l9e.train.main;



import org.apache.log4j.Logger;


import com.l9e.train.po.Notify;
import com.l9e.train.producerConsumer.distinct.DistinctProducerAndConsumer;
import com.l9e.train.thread.NotifyConsumer;
import com.l9e.train.thread.NotifyProducer;
import com.unlun.commons.res.Config;

public class NotifyProcessMain {
	
	private static Logger logger=Logger.getLogger(NotifyProcessMain.class);
	
	public static void main(String[] args) {
		
		logger.info("init Resource start");
		
		String databasePath = NotifyProcessMain.class.getClassLoader().getResource("database.properties").getPath();
		Config.setConfigResource(databasePath);
		
		String consumerSize = Config.getProperty("consumersize");
		String queuesize = Config.getProperty("queuesize");
		String sleepMilliSecondsWhenNolist = Config.getProperty("sleepMilliSecondsWhenNolist");
		String sleepMilliSecondsWhenHaslist=Config.getProperty("sleepMilliSecondsWhenHaslist");
		
		
		
		logger.info("init Producer and Consumer consumerSize="+consumerSize+" queuesize="+queuesize+" sleepMilliSecondsWhenNolist="+sleepMilliSecondsWhenNolist);
		logger.info("sleepMilliSecondsWhenHaslist="+sleepMilliSecondsWhenHaslist);
		
		DistinctProducerAndConsumer<Notify> container = new DistinctProducerAndConsumer<Notify>();
		container.createDistinct(
				new String[] { NotifyProducer.class.getName() },
				NotifyConsumer.class.getName(), Integer.parseInt(consumerSize), Integer
						.parseInt(queuesize),
				false, Integer.parseInt(sleepMilliSecondsWhenNolist),
				Integer.parseInt(sleepMilliSecondsWhenHaslist));
		
	}
}
