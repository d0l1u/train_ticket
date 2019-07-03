package com.l9e.transaction.job;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.BackupService;

/**
 * 备份orderInfo cpInfo job
 * @author liuyi02
 *
 */
@Component("backupAccFilterJob")
public class BackupAccountFilter {
	
	@Resource
	private BackupService backupService;
	
	private static final Logger logger = Logger.getLogger(BackupAccountFilter.class);
	public void backup(){
		
		/***** 数据备份account_filter开始*****/
		try {
			backupService.addBackupAccountFilter();
		} catch (Exception e) {
			logger.info("数据备份异常"+e);
			e.printStackTrace();
		}
		
		
	}
	
}
