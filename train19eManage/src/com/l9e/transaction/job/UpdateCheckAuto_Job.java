package com.l9e.transaction.job;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.ElongSettingService;
import com.l9e.transaction.service.UpdateCheckAutoService;
import com.l9e.util.DateUtil;

/**
 * 自动切换核验开关
 * @author zhangjc
 *
 */
@Component("updateCheckAuto_Job")
public class UpdateCheckAuto_Job {
	private static final Logger logger = Logger.getLogger(UpdateCheckAuto_Job.class);
	@Resource
	private UpdateCheckAutoService updateCheckAutoService;
	@Resource 
	private ElongSettingService elongSettingService;
	
	//查询自动切换是否开启
	public void queryCheckAutoStatus(){
		Date date1 = new Date();
		String datePre = DateUtil.dateToString(date1, "yyyyMMdd");
		Date begin = DateUtil.stringToDate(datePre + "070000", "yyyyMMddHHmmss");// 7:00
		Date end = DateUtil.stringToDate(datePre + "230000", "yyyyMMddHHmmss");// 23:00
		if (date1.before(begin) || date1.after(end)) {
			logger.info("07:00-23:00可以自动切换核验开关！");
			return;
		}
		String Status = updateCheckAutoService.querySettingStatus("tongcheng_check_auto");
		if("1".equals(Status)){
			logger.info("自动切换核验开关-->start!");
			this.updateCheckAutoStatus();
		}else{
			logger.info("自动切换核验开关-->未开启!");
		}
	}
	
	public void updateCheckAutoStatus(){
		logger.info("自动切换核验开关-->ing!");
		String Status = updateCheckAutoService.querySettingStatus("tongcheng_check");
		Map<String, String> log = new HashMap<String, String>();
		if("1".equals(Status)){
			logger.info("同程 核验开关：开启-->关闭!");
			updateCheckAutoService.updateSettingStatus("0");
			log.put("opt_person", "auto");
			log.put("content", "同程 核验开关：自动关闭!");
			elongSettingService.addSystemLog(log);
		}else if("0".equals(Status)){
			logger.info("同程 核验开关：关闭-->开启!");
			updateCheckAutoService.updateSettingStatus("1");
			log.put("opt_person", "auto");
			log.put("content", "同程 核验开关：自动开启!");
			elongSettingService.addSystemLog(log);
		}else{
			logger.info("同程 核验开关：异常!");
			log.put("opt_person", "auto");
			log.put("content", "同程 核验开关状态自动修改异常!");
			elongSettingService.addSystemLog(log);
		}
		logger.info("自动切换核验开关-->end!");
	}

}
