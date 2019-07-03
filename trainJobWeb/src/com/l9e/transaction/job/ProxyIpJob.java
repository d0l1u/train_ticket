package com.l9e.transaction.job;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import com.l9e.transaction.service.ProxyIpService;

/**
 * <p>
 * Title: ProxyIpJob.java
 * </p>
 * <p>
 * Description: 代理IP定时任务
 * </p>
 * 
 * @author taokai
 * @date 2017年3月2日
 */
//@Component
@Deprecated
public class ProxyIpJob {
	private Logger logger = Logger.getLogger(ProxyIpJob.class);
	
	@Resource
	private ProxyIpService proxyIpService;
	
	/**
	 * @Title: checkIp   
	 * @Description: 每天晚上0点执行一次  
	 * @author: taokai
	 * @date: 2017年7月11日 上午9:24:56 void
	 */
	@Scheduled(cron = "0 0 0 * * ?")
	public void checkIp(){
		logger.info("禁用即将到期的代理IP(还有2天到期)"); 
		Integer result = proxyIpService.updateDisable();
		logger.info("代理IP-JOB执行结果:"+result);
	}
	
}
