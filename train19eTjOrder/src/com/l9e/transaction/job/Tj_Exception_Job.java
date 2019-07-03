package com.l9e.transaction.job;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.Tj_OpterService;
import com.l9e.util.DateUtil;
import com.l9e.util.DbContextHolder;

/**
 * 根据cp_exception_config表的关键字去查询cp_orderinfo_history表的日志，来统计异常数目，加入tj_exception表
 */
@Component("tj_Exception_Job")
public class Tj_Exception_Job {

	private static final Logger logger = Logger.getLogger(Tj_Exception_Job.class);
	
	@Resource
	Tj_OpterService tj_OpterService;
	
	public void queryExceptionJob() {
		String logPre = "[异常统计]";
		Date date1 = new Date();
		String datePre = DateUtil.dateToString(date1, "yyyyMMdd");
		Date begin = DateUtil.stringToDate(datePre + "060000", "yyyyMMddHHmmss");// 6:00
		Date end = DateUtil.stringToDate(datePre + "235959", "yyyyMMddHHmmss");// 23:59
		if (date1.before(begin) || date1.after(end)) {
			logger.info(logPre+"06:00-23:59可以更新统计表！");
			return;
		}
		/************************************ 19e统计开始 *****************************************/
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 0);
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
		String end_time = df.format(date);//当前时间
		
		calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -10); //10分钟之前的时间
		Date date2 = calendar.getTime();
		String begin_time = df.format(date2);//当前时间
		logger.info(logPre+"tj_Exception_Job自动执行JOB，统计时间为："+begin_time+"到"+ end_time);
		
		//查询cp_exception_config表的关键字信息列表
		List<Map<String, String>> configList = tj_OpterService.queryExceptionConfig();
		
		if(configList.size()<=0){
			logger.info(logPre+"cp_exception_config表的启用关键字信息列表为空！");
			return;
		}else{
			Map<String, Object> paramMap = null;
			for(Map<String, String> configMap : configList){
				//去查询cp_orderinfo_history表的日志，来统计异常数目
				paramMap = new HashMap<String, Object>();
				paramMap.put("begin_time", begin_time);
				paramMap.put("end_time", end_time);
				paramMap.put("config_name", configMap.get("config_name"));
				int exception_num = tj_OpterService.queryExceptionNum(paramMap);
				
				//查询tj_exception表是否存在该数据
				paramMap = new HashMap<String, Object>();
				paramMap.put("tj_date", df2.format(date));
				paramMap.put("execption_id", configMap.get("config_id"));
				paramMap.put("execption_name", configMap.get("config_name"));
				paramMap.put("execption_type", configMap.get("config_type"));
				int tjExceptionCount = tj_OpterService.queryTjExceptionCount(paramMap);
				paramMap.put("execption_num", exception_num);
				paramMap.put("opter", "tj_job");
				paramMap.put("update_time", "update_time");
				DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库，将数据更新到运行数据库中
				if (tjExceptionCount == 0) {
					// 添加到表tj_exception表中
					paramMap.put("create_time", end_time);
					tj_OpterService.addToTjException(paramMap);
				} else {
					// 更新tj_exception表中数据
					tj_OpterService.updateToTjException(paramMap);
				}
				/*try {
					Thread.sleep(60*1000);//1分钟
				} catch (InterruptedException e) {
					e.printStackTrace();
					logger.info(logPre+"tj_Exception_Job线程中断1分钟异常");
				}*/
			}
		}
		logger.info(logPre+"tj_Exception_Job自动执行JOB结束");
	}
}