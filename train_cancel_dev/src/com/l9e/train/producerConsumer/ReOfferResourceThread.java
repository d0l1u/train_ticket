package com.l9e.train.producerConsumer;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * 
 * 不包含去重功能的生产者-消费者模式 消费者抽象类
 * @author liyong
 */

public class ReOfferResourceThread<T> implements Runnable {
	
	private static final Logger logger = Logger.getLogger(ReOfferResourceThread.class);
	

	private BlockingQueue<T> queue;

	//待重发的资源列表
	private BlockingQueue<T> reofferqueue;

	
	public ReOfferResourceThread(BlockingQueue<T> queue,BlockingQueue<T> reofferqueue){
		this.queue = queue;
		this.reofferqueue = reofferqueue;
	}
	
	
	public final void run() {
		try{
			while(!Thread.interrupted()){
				
				T t = reofferqueue.take();
				logger.info("ReOfferResourceThread获取到将资源");
				queue.put(t);
				logger.info("ReOfferResourceThread将资源放入队列");
			}
		}catch (Exception e) {
			logger.info("",e);
		}

	}


}
