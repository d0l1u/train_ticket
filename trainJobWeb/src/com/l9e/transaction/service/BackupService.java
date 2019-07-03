package com.l9e.transaction.service;

import java.util.Date;
import java.util.List;

public interface BackupService {
	
	/**
	 * 拉取原表数据最大数
	 */
	int LIMIT = 1000;
	
	/**
	 * 批处理数
	 */
	int BATCHCOUNT = 500;
	
	void addBackup(Date begin,Date end);

	int updateOrderStatus(String beginTime);
	
	void addBackupAccountFilter();
}
