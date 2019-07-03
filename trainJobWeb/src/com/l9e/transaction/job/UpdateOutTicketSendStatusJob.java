package com.l9e.transaction.job;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.CommonService;


/**
 * 去哪儿【系统设定】增加定时器功能，设置22:50自动改为“出票成功通知”；7:00改为“预订成功通知”
 */
@Component("updateOutTicketSendStatusJob")
public class UpdateOutTicketSendStatusJob {
private static final Logger logger = Logger.getLogger(UpdateOutTicketSendStatusJob.class);
	@Resource
	private CommonService commonService;
	/**
	 * 7:00改为“预订成功通知”
	 * */
	public void reserve () throws Exception {
		try {
			Map<String,String>paramQunar=new HashMap<String,String>();
			paramQunar.put("setting_name","out_notify_qunar");
			paramQunar.put("setting_value","1");
			int countQunar=commonService.updateQunarSysNoticeStaus(paramQunar);
			logger.info("7:00改为[预订成功通知]更新countQunar:"+countQunar);
			
			Map<String,String>paramElong=new HashMap<String,String>();
			paramElong.put("setting_name","out_notify_elong");
			paramElong.put("setting_value","1");
			int countElong=commonService.updateElongSysNoticeStaus(paramElong);
			logger.info("7:00改为[预订成功通知]更新countElong:"+countElong);
		} catch (Exception e) {
			logger.info("去哪儿【系统设定】增加定时器功能异常"+e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 22:50自动改为“出票成功通知”
	 * */
	public void outTicket () throws Exception {
		try {
			Map<String,String>param=new HashMap<String,String>();
			param.put("setting_name","out_notify_qunar");
			param.put("setting_value","0");
			int count= commonService.updateQunarSysNoticeStaus(param);
			logger.info("22:30自动改为[出票成功通知]更新countQunar:"+count);
			
			Map<String,String>paramElong=new HashMap<String,String>();
			paramElong.put("setting_name","out_notify_elong");
			paramElong.put("setting_value","0");
			int countElong=commonService.updateElongSysNoticeStaus(paramElong);
			logger.info("22:30自动改为[出票成功通知]更新countElong:"+countElong);
		} catch (Exception e) {
			logger.info("去哪儿【系统设定】增加定时器功能异常"+e);
			e.printStackTrace();
		}
	}
}
