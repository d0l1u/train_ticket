package com.l9e.transaction.job;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.AccountService;

/**
 * @ClassName: ResetAccountStatusJob
 * @Description: TODO
 * @author: taokai
 * @date: 2017年8月9日 下午4:18:01
 * @Copyright: 2017 www.19e.cn Inc. All rights reserved.
 */
@Component
public class ResetAccountStatusJob {

	private Logger logger = Logger.getLogger(ResetAccountStatusJob.class);

	@Resource
	private AccountService accountService;

	@Scheduled(cron = "${quartz.reset}")
	public void reset() {
		logger.info("开始重置装好状态"); 
		try {
			int count = accountService.reset2Free();
			logger.info(count + " 个账号被重新置为空闲状态");
		} catch (Exception e) {
			logger.info("重置账号出现异常", e);
		}
	}
}
