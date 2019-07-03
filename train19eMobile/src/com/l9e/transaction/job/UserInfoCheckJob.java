package com.l9e.transaction.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import com.l9e.transaction.service.UserInfoService;

/**
 * 扫描待审核的用户修改为已通过
 * @author yangchao
 *
 */
@Component("userInfoCheckJob")
public class UserInfoCheckJob {
	
	private static final Logger logger = Logger.getLogger(UserInfoCheckJob.class);
	
	@Resource
	private UserInfoService userInfoService;
	
	public void updateUserInfoStatus(){
		List<Map<String, String>>  userInfoCheckList=userInfoService.queryUserInfoCheckList();
		if(userInfoCheckList!=null && userInfoCheckList.size()>0){
			for (Map<String, String> userInfoCheckMap : userInfoCheckList) {
				this.updateUserInfoCheckStatus(userInfoCheckMap);
			}
		}
	}
	
	private void updateUserInfoCheckStatus(Map<String, String> userInfoCheckMap){
		logger.info("开始定时修改身份证待审核状态为已通过！身份证号码为："+userInfoCheckMap.get("ids_card"));
		String ids_card=userInfoCheckMap.get("ids_card");
		Map<String, String> map=new HashMap<String,String>();
		map.put("ids_card", ids_card);
		map.put("user_name", userInfoCheckMap.get("user_name"));
		map.put("status", "0");
		userInfoService.updateUserInfoCheck(map);
	}

}
