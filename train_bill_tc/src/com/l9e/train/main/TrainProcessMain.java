package com.l9e.train.main;



import org.apache.log4j.Logger;

import com.l9e.train.po.BillOrder;
import com.l9e.train.producerConsumer.nodistinct.NoDistinctProducerAndConsumer;
import com.l9e.train.thread.TimeoutConsumer;
import com.l9e.train.thread.TimeoutProducer;
import com.l9e.train.util.BillConst;
import com.unlun.commons.res.Config;

public class TrainProcessMain {
	
	private static Logger logger=Logger.getLogger(TrainProcessMain.class);
	
	public static void main(String[] args) {
		
		logger.info("init Resource start");
		
		String databasePath = TrainProcessMain.class.getClassLoader().getResource("database.properties").getPath();
		Config.setConfigResource(databasePath);
		
		String consumerSize = Config.getProperty("consumersize");
		String queuesize = Config.getProperty("queuesize");
		String sleepMilliSecondsWhenNolist = Config.getProperty("sleepMilliSecondsWhenNolist");
		String sleepMilliSecondsWhenHaslist=Config.getProperty("sleepMilliSecondsWhenHaslist");
		
		BillConst.BILL_SYNC_URL = Config.getProperty("sync_bill_url");
		BillConst.MD5_KEY = Config.getProperty("md5_key");
		logger.info("同程对账接口地址：" + BillConst.BILL_SYNC_URL);
		logger.info("同程对账md5key：" + BillConst.MD5_KEY);
		
		logger.info("init Producer and Consumer consumerSize="+consumerSize+" queuesize="+queuesize+" sleepMilliSecondsWhenNolist="+sleepMilliSecondsWhenNolist);
		logger.info("sleepMilliSecondsWhenHaslist="+sleepMilliSecondsWhenHaslist);
		
		NoDistinctProducerAndConsumer<BillOrder> container = new NoDistinctProducerAndConsumer<BillOrder>();
		container.createNoDistinct(new String[] { TimeoutProducer.class.getName() },
				TimeoutConsumer.class.getName(), Integer.parseInt(consumerSize), Integer
				.parseInt(queuesize),
		false, Integer.parseInt(sleepMilliSecondsWhenNolist),
		Integer.parseInt(sleepMilliSecondsWhenHaslist));
				
	}
}
