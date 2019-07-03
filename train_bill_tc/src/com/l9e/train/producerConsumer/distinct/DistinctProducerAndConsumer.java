package com.l9e.train.producerConsumer.distinct;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;

import com.l9e.train.producerConsumer.ReOfferResourceThread;
import com.l9e.train.producerConsumer.util.LRULinkedHashMap;
import com.l9e.train.util.MemcachedUtil;
import com.l9e.train.util.MobileMsgUtil;



public class DistinctProducerAndConsumer<T> {
	private static final Logger logger = Logger.getLogger(ReOfferResourceThread.class);
	
	public void createDistinct(String[] distinctProducerClassNames,String distinctConsumerClassName,int distinctConsumerSize,int blockingQueueSize,boolean isNeedReOffer,int nolistidleseconds,int haslistidleseconds){
		this.createDistinct(distinctProducerClassNames, distinctConsumerClassName, distinctConsumerSize, blockingQueueSize, isNeedReOffer, nolistidleseconds,haslistidleseconds, 10);
	}
	
	
	public void createDistinct(String[] distinctProducerClassNames,String distinctConsumerClassName,
			int distinctConsumerSize,int blockingQueueSize,boolean isNeedReOffer,int nolistidleseconds,
			int haslistidleseconds,int distinctFactor){
		BlockingQueue<T> queue = new ArrayBlockingQueue<T>(blockingQueueSize);
		//待重发
		BlockingQueue<T> reofferqueue = new ArrayBlockingQueue<T>(1000);
		//并发对象
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
					}
				}
				exec.execute(producer);
			} catch (Exception e) {
				logger.info("distinctProducerClassNames error",e);
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
					}
				}
				exec.execute(consumer);
			} catch (Exception e) {
				logger.info("distinctConsumerSize error",e);
			}
		}
		
		try {
			
			ThreadPoolExecutor pexec = (ThreadPoolExecutor)exec;
			while(!Thread.interrupted()){
				int num = 0;
				if(null == MemcachedUtil.getInstance().getAttribute("robot_app_stop_num")){
					MemcachedUtil.getInstance().setAttribute("robot_app_stop_num", 0, 6000);
				}else{
					num = Integer.valueOf(String.valueOf(MemcachedUtil.getInstance().getAttribute("robot_app_stop_num")));
					if(num>=30){
						MobileMsgUtil msg = new MobileMsgUtil();
						msg.send("13718235385", "预定机器人异常，请马上重启94服务器/app/train_app项目！！");
						msg.send("18610587743", "预定机器人异常，请马上重启94服务器/app/train_app项目！！");
						msg.send("15201169346", "预定机器人异常，请马上重启94服务器/app/train_app项目！！");
						MemcachedUtil.getInstance().setAttribute("robot_app_stop_num", 0, 6000);
					}else{
						MemcachedUtil.getInstance().setAttribute("robot_app_stop_num", ++num, 6000);
					}
				}
				logger.info("robot_app_stop_num: "+num);
				
				logger.info("pexec.getActiveCount###################"+pexec.getActiveCount());
				logger.info("pexec.getPoolSize###################"+pexec.getPoolSize());
				logger.info("pexec.getCompletedTaskCount###################"+pexec.getCompletedTaskCount());
				
				Thread.sleep(6000);
			}
		} catch (InterruptedException e) {
			logger.error("thread is error!", e);
		}
		
	}
	
}
