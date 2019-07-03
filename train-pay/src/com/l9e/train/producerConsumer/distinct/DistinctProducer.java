package com.l9e.train.producerConsumer.distinct;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l9e.train.producerConsumer.util.LRULinkedHashMap;

/**
 * 
 * 不包含去重功能的生产者-消费者模式 生产者抽象类
 * 
 * @author liyong
 */

public abstract class DistinctProducer<T> implements Runnable {

	private Logger logger = LoggerFactory.getLogger(DistinctProducer.class);
	
	private String producerid;
	private BlockingQueue<T> queue;

	// 处理中的资源列表
	private ConcurrentHashMap<String, Long> dealingmap;
	// 处理成功的资源列表
	private LRULinkedHashMap<String, Long> succmap;

	private long noProductIdletimes;
	private long hasProductIdletimes;

	public final void run() {
		try {
			while (!Thread.interrupted()) {
				String millis = String.valueOf(System.currentTimeMillis());
				String logid = "[" + producerid + millis.substring(millis.length() - 4) + "] ";
				logger.info(logid + "=========== 生产者开始生产 ==========");

				// 获取队列
				List<T> list = getProducts(logid);

				if (list != null && list.size() > 0) {
					Long currentTime = System.currentTimeMillis();
					logger.info(logid + "currentTime：" + currentTime);
					// 将所查到的list全部放到队列中
					logger.info(logid + "生产者：" + producerid + " 已获得资源List size：" + list.size());
					for (T t : list) {
						// 获取资源的主键id及当前时间，调用ConcurrentHashMap的putIfAbsent方法，如果返回值与当前时间不一致，表示之前已存在该资源，则放弃该资源
						String orderId = getObjectKeyId(t);

						Long puttime = dealingmap.putIfAbsent(orderId, currentTime);
						logger.info(logid + "puttime：" + puttime);
						if (puttime != null) {
							// 返回值不相等，表示之前已存在该资源，则放弃该资源
							logger.info(logid + "资源ID：" + orderId + " 正在处理中,放弃该资源并继续循环...");
							continue;
						}
						Long succtime = succmap.get(orderId);
						logger.info(logid + "succtime：" + succtime);
						if (succtime != null) {
							// 如果存在于成功队列，则比较时间,如果获取资源的时间小于等于成功的时间，说明该信息已过去，则舍弃,如果获取资源的时间大于成功的时间，则表示资源需要发送
							if (currentTime <= succtime) {
								logger.info(logid + "资源ID：" + orderId + " 已成功,放弃该资源并继续循环...");
								// 删除dealingmap中的缓存
								dealingmap.remove(orderId);
								continue;
							} else {
								succmap.remove(orderId);
								logger.info(logid + "资源ID：" + orderId + " 已成功,但获取资源的时间大于成功的时间,需要再次发送 ...");
							}
						}
						queue.put(t);
						logger.info(logid + "将资源ID：" + orderId + " 放入队列");
					}
					logger.info(logid + "放入队列完毕,休眠 " + hasProductIdletimes + " 毫秒");
					Thread.sleep(hasProductIdletimes);
				} else {
					logger.info(logid + "没有获得资源List，休眠 " + noProductIdletimes + " 毫秒");
					Thread.sleep(noProductIdletimes);
				}
			}
		} catch (Exception e) {
			logger.info("【DistinctProducer Exception】", e);
		}
	}

	// 获取资源list抽象方法
	public abstract List<T> getProducts(String logid);

	public abstract String getObjectKeyId(T t);

	public void setProducerid(String producerid) {
		this.producerid = producerid;
	}

	public void setQueue(BlockingQueue<T> queue) {
		this.queue = queue;
	}

	public void setDealingmap(ConcurrentHashMap<String, Long> dealingmap) {
		this.dealingmap = dealingmap;
	}

	public void setNoProductIdletimes(long noProductIdletimes) {
		this.noProductIdletimes = noProductIdletimes;
	}

	public void setSuccmap(LRULinkedHashMap<String, Long> succmap) {
		this.succmap = succmap;
	}

	public void setHasProductIdletimes(long hasProductIdletimes) {
		this.hasProductIdletimes = hasProductIdletimes;
	}

}
