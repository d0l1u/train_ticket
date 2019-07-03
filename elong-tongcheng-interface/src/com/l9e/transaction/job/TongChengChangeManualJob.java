package com.l9e.transaction.job;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.TongChengChangeService;

/**
 * 同程改签异常转人工job
 * @author licheng
 *
 */
@Component("tongChengChangeManualJob")
public class TongChengChangeManualJob {

	private static final Logger logger = Logger.getLogger(TongChengChangeManualJob.class); 
	
	@Resource
	private TongChengChangeService tongChengChangeService;
	
	/**
	 * 改签停滞转人工
	 */
	public void changeManual() {
		
		logger.info("检测正在预订和正在支付超时转人工job执行... ...");
		tongChengChangeService.changeManual4Timeout();
	}
}
