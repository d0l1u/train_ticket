package com.l9e.producerConsumer.nodistinct;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.l9e.producerConsumer.ReOfferResourceThread;




public class NoDistinctProducerAndConsumer<T> {
	private static final Logger logger = Logger.getLogger(ReOfferResourceThread.class);
	
	public void createNoDistinct(String[] noDistinctProducerClassNames,String noDistinctConsumerClassName,int noDistinctConsumerSize,int blockingQueueSize,boolean isNeedReOffer,int nolistidleseconds,int haslistidleseconds){

		
		BlockingQueue<T> queue = new ArrayBlockingQueue<T>(blockingQueueSize);
		BlockingQueue<T> reofferqueue = new ArrayBlockingQueue<T>(1000);
		ExecutorService exec = Executors.newCachedThreadPool();
		
		ReOfferResourceThread<T> rrt = new ReOfferResourceThread<T>(queue,reofferqueue);
		exec.execute(rrt);
		
		for(int i = 0;i<noDistinctProducerClassNames.length;i++){
			try {
				Class<T> producerClass = (Class<T>) Class.forName(noDistinctProducerClassNames[i]);
				Constructor<T> constructor = producerClass.getConstructor();
				NoDistinctProducer<T> producer = (NoDistinctProducer<T>) constructor.newInstance();
				Method[] methods = producerClass.getMethods();
				for(Method m:methods){
					if("setProducerid".equals(m.getName())){
						m.invoke(producer, "p"+i);
					}else if("setQueue".equals(m.getName())){
						m.invoke(producer, queue);
					}else if("setNoProductIdletimes".equals(m.getName())){
						m.invoke(producer, nolistidleseconds);
					}else if("setHasProductIdletimes".equals(m.getName())){
						m.invoke(producer, haslistidleseconds);
					}
				}
				exec.execute(producer);
			} catch (Exception e) {
				logger.info("",e);
			}
		}
		 
		
		for(int i = 0;i<noDistinctConsumerSize;i++){
			try {
				Class<T> consumerClass = (Class<T>) Class.forName(noDistinctConsumerClassName);
				Constructor<T> constructor = consumerClass.getConstructor();
				NoDistinctConsumer<T> consumer = (NoDistinctConsumer<T>) constructor.newInstance();
				Method[] methods = consumerClass.getMethods();
				for(Method m:methods){
					if("setConsumerid".equals(m.getName())){
						m.invoke(consumer, "c"+i);
					}else if("setQueue".equals(m.getName())){
						m.invoke(consumer, queue);
					}else if("setReofferqueue".equals(m.getName())){
						m.invoke(consumer, reofferqueue);
					}else if("setNeedReOffer".equals(m.getName())){
						m.invoke(consumer, isNeedReOffer);
					}
				}
				exec.execute(consumer);
			} catch (Exception e) {
				logger.info("",e);
			}
		}
	}
	
}
