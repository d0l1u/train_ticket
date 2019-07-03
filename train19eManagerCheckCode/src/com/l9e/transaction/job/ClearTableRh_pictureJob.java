package com.l9e.transaction.job;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.TjPicService;

@Component("clearTableRh_pictureJob")
public class ClearTableRh_pictureJob {
	private static final Logger logger = Logger.getLogger(TjPicJob.class);
	@Resource
	private TjPicService tjPicService;
	
	public void clearTable(){
		logger.info("ClearTableRh_pictureJob自动执行JOB:自动删除rh_picture表中当天日期之前的数据");
		tjPicService.clearRh_picture();
		logger.info("ClearTableRh_pictureJob自动执行JOB:rh_picture表中当天日期之前的数据已删除");
	}
	
}
