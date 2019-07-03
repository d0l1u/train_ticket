package com.l9e.transaction.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.BackupDao;
import com.l9e.transaction.service.BackupService;
import com.l9e.util.DateUtil;
@Service("backupService")
public class BackupServiceImpl implements BackupService{

	private static final Logger logger = Logger.getLogger(BackupServiceImpl.class);
	
	@Resource
	private BackupDao backupDao;
	@Override
	public void addBackup(Date begin, Date end) {
		try {
			Map<String,String> param=new HashMap<String, String>();
			param.put("begin", DateUtil.dateToString(begin, "yyyy-MM-dd HH:mm:ss"));
			param.put("end", DateUtil.dateToString(end, "yyyy-MM-dd HH:mm:ss"));
			backupDao.backupCpInfo(param);
			backupDao.backupOrderInfo(param);
			backupDao.backupCpOrderNotifyInfo(param);
			logger.info("日期下:"+DateUtil.dateToString(begin, "yyyy-MM-dd HH:mm:ss")+"订单、车票信息日备份【成功】");
		} catch (Exception e) {
			logger.info("日期下:"+DateUtil.dateToString(begin, "yyyy-MM-dd HH:mm:ss")+"订单、车票信息日备份发生异常"+e);
		}
	}
	@Override
	public int updateOrderStatus(String beginTime) {
		return backupDao.updateOrderStatus(beginTime);
	}
	@Override
	public void addBackupAccountFilter() {
		//查询数量
		
		//超过20万就备份
		
		
	}

	
}
