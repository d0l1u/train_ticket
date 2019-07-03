package com.l9e.transaction.job;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.l9e.common.BaseController;
import com.l9e.producerConsumer.distinct.DistinctProducerAndConsumer;
import com.l9e.thread.TimeoutConsumer;
import com.l9e.thread.TimeoutProducer;
import com.l9e.transaction.service.RobotCodeService;
import com.speed.util.Config;
/**
 * 页面打码
 */
@Deprecated
@Component("qunarCodeJob")
public class QunarCodeJob extends BaseController implements Runnable{
	private static final Logger logger = 
		Logger.getLogger(QunarCodeJob.class);


	@Value("#{propertiesReader[rand_url]}")
	private String rand_url;
	
	@Value("#{propertiesReader[rand_url_am]}")
	private String rand_url_am;
	
	@Value("#{propertiesReader[sys_root]}")
	private String sys_root;
	
	@Value("#{propertiesReader[agent_code1]}")
	private String agent_code1;
	
	@Value("#{propertiesReader[agent_code2]}")
	private String agent_code2;
	
	@Value("#{propertiesReader[secretKey1]}")
	private String secretKey1;
	
	@Value("#{propertiesReader[secretKey2]}")
	private String secretKey2;
	
	@Value("#{propertiesReader[pic_root]}")
	private String pic_root;
	
	@Resource
	RobotCodeService robotService;

	private static int index = 1;
	public void qunarCode(){
		if(index!=1){
			return;
		}
		index++;
		Map<String,String> param = new HashMap<String,String>();
		param.put("rand_url", rand_url);
		param.put("rand_url_am", rand_url_am);
		param.put("sys_root", sys_root);
		param.put("agent_code1", agent_code1);
		param.put("agent_code2", agent_code2);
		param.put("secretKey1", secretKey1);
		param.put("secretKey2", secretKey2);
		param.put("pic_root", pic_root);
		
		logger.info("init Resource start");
		
		String databasePath = QunarCodeJob.class.getClassLoader().getResource("config.properties").getPath();
		Config.setConfigResource(databasePath);
		
		String consumerSize = Config.getProperty("consumersize");
		String queuesize = Config.getProperty("queuesize");
		String sleepMilliSecondsWhenNolist = Config.getProperty("sleepMilliSecondsWhenNolist");
		String sleepMilliSecondsWhenHaslist=Config.getProperty("sleepMilliSecondsWhenHaslist");
		
		
		
		logger.info("init Producer and Consumer consumerSize="+consumerSize+" queuesize="+queuesize+" sleepMilliSecondsWhenNolist="+sleepMilliSecondsWhenNolist);
		logger.info("sleepMilliSecondsWhenHaslist="+sleepMilliSecondsWhenHaslist);
		
		DistinctProducerAndConsumer<Order> container = new DistinctProducerAndConsumer<Order>();
		container.createDistinct(
				new String[] { TimeoutProducer.class.getName() },
				TimeoutConsumer.class.getName(), Integer.parseInt(consumerSize), Integer
						.parseInt(queuesize),
				false, Integer.parseInt(sleepMilliSecondsWhenNolist),
				Integer.parseInt(sleepMilliSecondsWhenHaslist),robotService,param);
	}
	@Override
	public void run() {
		
	}
}
