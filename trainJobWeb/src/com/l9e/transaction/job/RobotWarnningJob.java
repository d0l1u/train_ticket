package com.l9e.transaction.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.vo.Phone;
import com.l9e.util.DateUtil;
import com.l9e.util.MobileMsgUtil;

/**
 * 发起机器人警告短信通知
 * @author zhangjun
 *
 */
@Component("robotWarnningJob")
public class RobotWarnningJob {
	
	private static final Logger logger = Logger.getLogger(RobotWarnningJob.class);
	
	@Resource
	private CommonService commonService;
	
	public void robotNotify() throws Exception {
		List<Map<String,String>> list =  commonService.queryWaitNotify();
		for(Map<String,String> map :list){
			requestRobot(map);
		}
		
		Map<String,String> acc_map = new HashMap<String,String>();
		acc_map.put("acc_status", "66");
		acc_map.put("option_time", DateUtil.minuteBefore(45));
		
		/** 账号占座状态的账号 释放  目前有bug  后续放在凌晨统一更新   */
		/*List<Map<String,String>> list_acc = commonService.queryAccountSeatList(acc_map);
		
		for(Map<String,String> map :list_acc){
			logger.info("update the account_id to be free:"+map.get("acc_id"));
			commonService.updateAccountStatusFree(map.get("acc_id"));
		}*/
	}
	
	/**
	 * 
	 * @param account
	 */
	private void requestRobot(Map<String,String> map){
		//更新通知时间并将状态改为开始通知
		map.put("old_status", "0");
		map.put("new_status", "1");
		commonService.updateRobotStartNotify(map);
		String[] phoneArr = map.get("telephone").split(",");
		logger.info("机器人："+map.get("robot_name")+"出现异常,等待重启");
		int result = 0;
		MobileMsgUtil msu = new MobileMsgUtil();
		Phone phone = null;
		for(String phone_num :phoneArr){
			phone = new Phone();
			phone.setContent(map.get("content"));
			phone.setTelephone(phone_num);
			if("SUCCESS".equals(msu.send(phone))){
				result++;
			}
		}
		if(result>0){
			map.put("new_status", "2");
			map.put("old_status", "1");
		}else{
			map.put("new_status", "3");
			map.put("old_status", "1");
		}
		commonService.updateRobotStartNotify(map);
 	}
}
