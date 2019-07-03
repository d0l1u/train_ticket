package com.l9e.producerConsumer.distinct;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.l9e.producerConsumer.ReOfferResourceThread;
import com.l9e.transaction.service.RobotCodeService;
import com.l9e.util.LRULinkedHashMap;


@Controller
public class DistinctProducerAndConsumer<T> {
	private static final Logger logger = Logger.getLogger(ReOfferResourceThread.class);
	
	public void createDistinct(String[] distinctProducerClassNames,String distinctConsumerClassName,
			int distinctConsumerSize,int blockingQueueSize,boolean isNeedReOffer,int nolistidleseconds,
			int haslistidleseconds,RobotCodeService robotService,Map<String,String> param){
		
		this.createDistinct(distinctProducerClassNames, distinctConsumerClassName, distinctConsumerSize,
			blockingQueueSize, isNeedReOffer, nolistidleseconds,haslistidleseconds, 10,robotService,param);
		
	}
	
	
	public void createDistinct(String[] distinctProducerClassNames,String distinctConsumerClassName,
			int distinctConsumerSize,int blockingQueueSize,boolean isNeedReOffer,int nolistidleseconds,
			int haslistidleseconds,int distinctFactor,RobotCodeService robotService,Map<String,String> param){

		
		BlockingQueue<T> queue = new ArrayBlockingQueue<T>(blockingQueueSize);
		BlockingQueue<T> reofferqueue = new ArrayBlockingQueue<T>(1000);
		ConcurrentHashMap<String, Long> dealingmap = new ConcurrentHashMap<String, Long>();
		LRULinkedHashMap<String, Long> succmap = new LRULinkedHashMap<String, Long>(distinctFactor*blockingQueueSize);
		
		ExecutorService exec = Executors.newCachedThreadPool();
		
		ReOfferResourceThread<T> rrt = new ReOfferResourceThread<T>(queue,reofferqueue);
		exec.execute(rrt);

		for(int i = 0;i<distinctProducerClassNames.length;i++){
			try {
				Class<T> producerClass = (Class<T>) Class.forName(distinctProducerClassNames[i]);
				Constructor<T> constructor = producerClass.getConstructor();
				DistinctProducer<T> producer = (DistinctProducer<T>) constructor.newInstance();
				Method[] methods = producerClass.getMethods();
				for(Method m:methods){
					if("setProducerid".equals(m.getName())){
						m.invoke(producer, "p"+i);
					}else if("setQueue".equals(m.getName())){
						m.invoke(producer, queue);
					}else if("setDealingmap".equals(m.getName())){
						m.invoke(producer, dealingmap);
					}else if("setNoProductIdletimes".equals(m.getName())){
						m.invoke(producer, nolistidleseconds);
					}else if("setSuccmap".equals(m.getName())){
						m.invoke(producer, succmap);
					}else if("setHasProductIdletimes".equals(m.getName())){
						m.invoke(producer, haslistidleseconds);
					}else if("setRobotService".equals(m.getName())){
						m.invoke(producer, robotService);
					}
				}
				exec.execute(producer);
			} catch (Exception e) {
				logger.error("producer error:",e);
			}
		}
		 
		
		for(int i = 0;i<distinctConsumerSize;i++){
			try {
				Class<T> consumerClass = (Class<T>) Class.forName(distinctConsumerClassName);
				Constructor<T> constructor = consumerClass.getConstructor();
				DistinctConsumer<T> consumer = (DistinctConsumer<T>) constructor.newInstance();
				Method[] methods = consumerClass.getMethods();
				for(Method m:methods){
					if("setConsumerid".equals(m.getName())){
						m.invoke(consumer, "c"+i);
					}else if("setQueue".equals(m.getName())){
						m.invoke(consumer, queue);
					}else if("setDealingmap".equals(m.getName())){
						m.invoke(consumer, dealingmap);
					}else if("setReofferqueue".equals(m.getName())){
						m.invoke(consumer, reofferqueue);
					}else if("setNeedReOffer".equals(m.getName())){
						m.invoke(consumer, isNeedReOffer);
					}else if("setSuccmap".equals(m.getName())){
						m.invoke(consumer, succmap);
					}else if("setRobotService".equals(m.getName())){
						m.invoke(consumer, robotService);
					}else if("setParam".equals(m.getName())){
						m.invoke(consumer, param);
					}
				}
				exec.execute(consumer);
			} catch (Exception e) {
				logger.error("consumer error",e);
			}
		}
	}
	
}
