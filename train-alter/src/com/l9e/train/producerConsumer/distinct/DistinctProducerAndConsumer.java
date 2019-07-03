package com.l9e.train.producerConsumer.distinct;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l9e.train.producerConsumer.ReOfferResourceThread;
import com.l9e.train.producerConsumer.util.LRULinkedHashMap;

public class DistinctProducerAndConsumer<T> {
	private Logger logger = LoggerFactory.getLogger(DistinctProducerAndConsumer.class);

	public void createDistinct(String[] distinctProducerClassNames, String distinctConsumerClassName, int distinctConsumerSize, int blockingQueueSize,
			boolean isNeedReOffer, int nolistidleseconds, int haslistidleseconds) {
		this.createDistinct(distinctProducerClassNames, distinctConsumerClassName, distinctConsumerSize, blockingQueueSize, isNeedReOffer, nolistidleseconds,
				haslistidleseconds, 10);
	}

	public void createDistinct(String[] distinctProducerClassNames, String distinctConsumerClassName, int distinctConsumerSize, int blockingQueueSize,
			boolean isNeedReOffer, int nolistidleseconds, int haslistidleseconds, int distinctFactor) {

		BlockingQueue<T> queue = new ArrayBlockingQueue<T>(blockingQueueSize);
		BlockingQueue<T> reofferqueue = new ArrayBlockingQueue<T>(1000);
		ConcurrentHashMap<String, Long> dealingmap = new ConcurrentHashMap<String, Long>();
		LRULinkedHashMap<String, Long> succmap = new LRULinkedHashMap<String, Long>(distinctFactor * blockingQueueSize);

		ExecutorService exec = Executors.newCachedThreadPool();

		ReOfferResourceThread<T> rrt = new ReOfferResourceThread<T>(queue, reofferqueue);
		exec.execute(rrt);

		for (int i = 0; i < distinctProducerClassNames.length; i++) {
			try {
				Class<T> producerClass = (Class<T>) Class.forName(distinctProducerClassNames[i]);
				Constructor<T> constructor = producerClass.getConstructor();
				DistinctProducer<T> producer = (DistinctProducer<T>) constructor.newInstance();
				Method[] methods = producerClass.getMethods();
				for (Method m : methods) {
					if ("setProducerid".equals(m.getName())) {
						m.invoke(producer, "p" + i);
					} else if ("setQueue".equals(m.getName())) {
						m.invoke(producer, queue);
					} else if ("setDealingmap".equals(m.getName())) {
						m.invoke(producer, dealingmap);
					} else if ("setNoProductIdletimes".equals(m.getName())) {
						m.invoke(producer, nolistidleseconds);
					} else if ("setSuccmap".equals(m.getName())) {
						m.invoke(producer, succmap);
					} else if ("setHasProductIdletimes".equals(m.getName())) {
						m.invoke(producer, haslistidleseconds);
					}
				}
				exec.execute(producer);
			} catch (Exception e) {
				logger.info("", e);
			}
		}

		for (int i = 0; i < distinctConsumerSize; i++) {
			try {
				Class<T> consumerClass = (Class<T>) Class.forName(distinctConsumerClassName);
				Constructor<T> constructor = consumerClass.getConstructor();
				DistinctConsumer<T> consumer = (DistinctConsumer<T>) constructor.newInstance();
				Method[] methods = consumerClass.getMethods();
				for (Method m : methods) {
					if ("setConsumerid".equals(m.getName())) {
						m.invoke(consumer, "c" + i);
					} else if ("setQueue".equals(m.getName())) {
						m.invoke(consumer, queue);
					} else if ("setDealingmap".equals(m.getName())) {
						m.invoke(consumer, dealingmap);
					} else if ("setReofferqueue".equals(m.getName())) {
						m.invoke(consumer, reofferqueue);
					} else if ("setNeedReOffer".equals(m.getName())) {
						m.invoke(consumer, isNeedReOffer);
					} else if ("setSuccmap".equals(m.getName())) {
						m.invoke(consumer, succmap);
					}
				}
				exec.execute(consumer);
			} catch (Exception e) {
				logger.info("", e);
			}
		}
	}

}
