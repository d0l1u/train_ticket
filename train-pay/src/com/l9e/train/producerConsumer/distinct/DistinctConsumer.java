package com.l9e.train.producerConsumer.distinct;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l9e.train.producerConsumer.util.LRULinkedHashMap;

/**
 * 
 * 不包含去重功能的生产者-消费者模式 消费者抽象类
 * 
 * @author liyong
 */

public abstract class DistinctConsumer<T> implements Runnable {

	private Logger logger = LoggerFactory.getLogger(DistinctConsumer.class);
	
	/** 消费者ID */
	private String consumerid;

	private BlockingQueue<T> queue;
	// 处理中的资源列表
	private ConcurrentHashMap<String, Long> dealingmap;
	// 待重发的资源列表
	private BlockingQueue<T> reofferqueue;
	// 处理成功的资源列表
	private LRULinkedHashMap<String, Long> succmap;

	private boolean isNeedReOffer;// 发送失败是否需要从新插入队列标识

	// public final void run() {
	public void run() {
		try {
			while (!Thread.interrupted()) {
				String millis = String.valueOf(System.currentTimeMillis());
				String logid = "[" + consumerid + millis.substring(millis.length() - 4) + "] ";
				logger.info(logid + "=========== 消费者开始消费 ==========");
				// 从队列中获取资源，并从队列移除该资源
				T t = queue.take();

				// getObjectKeyId() 由其子类去实现。子类执行父类run方法
				String orderId = getObjectKeyId(t);
				logger.info(logid + "消费者：" + consumerid + "，获取到待消费资源ID：" + orderId);

				boolean consumeresult = false;
				// 进行消费
				try {
					consumeresult = consume(t, logid);
				} catch (Exception e) {
					logger.info(logid + "【consume() Exception】", e);
				}
				if (consumeresult) {
					// 如果消费成功，则完成,添加到已成功队列，并从已存在的资源列表中删除
					succmap.put(orderId, System.currentTimeMillis());
					dealingmap.remove(orderId);
					logger.info(logid + "消费者：" + consumerid + " 完成 " + orderId + " 消费。");
				} else {
					// 消费不成功时
					if (isNeedReOffer) {
						// 需要重新插入队列标识时，则重新加入队列等待处理
						// 此处不采取阻塞等待方式，因为可能产生死锁，所以新增加一个需要重新offer的Map,该Map由另一线程负责reoffer
						reofferqueue.put(t);
						logger.info(logid + "消费者：" + consumerid + " 未完成 " + orderId + " 消费，将资源放入reoffer队列");
					} else {
						// 不需要重新插入队列标识时，从存在的资源列表中删除
						dealingmap.remove(orderId);
						logger.info(logid + "消费者：" + consumerid + " 未完成 " + orderId + " 消费，从存在的资源列表中删除");
					}
				}
			}
		} catch (Exception e) {
			logger.info("【DistinctConsumer Exception】", e);
		}
	}

	// 消费抽象方法
	public abstract boolean consume(T t, String logid);

	public abstract String getObjectKeyId(T t);

	public void setConsumerid(String consumerid) {
		this.consumerid = consumerid;
	}

	public void setQueue(BlockingQueue<T> queue) {
		this.queue = queue;
	}

	public void setDealingmap(ConcurrentHashMap<String, Long> dealingmap) {
		this.dealingmap = dealingmap;
	}

	public void setReofferqueue(BlockingQueue<T> reofferqueue) {
		this.reofferqueue = reofferqueue;
	}

	public void setNeedReOffer(boolean isNeedReOffer) {
		this.isNeedReOffer = isNeedReOffer;
	}

	public void setSuccmap(LRULinkedHashMap<String, Long> succmap) {
		this.succmap = succmap;
	}
}
