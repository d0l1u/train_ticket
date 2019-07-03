package com.l9e.transaction.job;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.AccountService;
import com.l9e.transaction.service.BackupService;
import com.l9e.util.DateUtil;

/**
 * 备份orderInfo cpInfo job
 * @author liuyi02
 *
 */
@Component("backupOrderCpJob")
public class BackupOrderCpJob {
	
	@Resource
	private BackupService backupServiceNew;
	
	@Resource
	private AccountService accountService;
	
	private static final Logger logger = Logger.getLogger(BackupOrderCpJob.class);
	public void backup(){
		/**凌晨开始每天 job*/
		
		/***** 清空机器人操作订单数量*****/
		try {
			accountService.updateWorkerinfoNum();
		} catch (Exception e) {
			logger.info("清空机器人操作订单数量异常"+e);
			e.printStackTrace();
		}
		
		/*****唤醒预下单订单状态至正常下单状态 77->11*****/
		String beginTime=DateUtil.dateToString(DateUtil.dateAddDays( new Date(), 19), "yyyy-MM-dd HH:mm:ss");
		try {
			int num=backupServiceNew.updateOrderStatus(beginTime);
			logger.info("成功唤醒订单数量:"+num);
		} catch (Exception e) {
			logger.info("唤醒预下单订单异常"+e);
			e.printStackTrace();
		}
		
		
		
		
		

		/***** 数据备份开始*****/
		//试用日期
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -90);
		//Date date = new Date();
		Date date = calendar.getTime();

		/***** 数据备份开始*/

		String datePre = DateUtil.dateToString(date, "yyyyMMdd");	
		Date begin = DateUtil.stringToDate(datePre + "000000", "yyyyMMddHHmmss");//00:00
		Date end =DateUtil.dateAddDays(begin, 1);
		try {
			backupServiceNew.addBackup(begin,end);
		} catch (Exception e) {
			logger.info("数据备份异常"+e);
			e.printStackTrace();
		}
		
	}
	
}
