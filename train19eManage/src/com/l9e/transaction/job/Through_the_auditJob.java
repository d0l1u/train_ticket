package com.l9e.transaction.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import com.l9e.transaction.service.Through_the_auditService;
import com.l9e.transaction.vo.JoinUsVo;
import com.l9e.util.MobileMsgUtil;
/**
 * 审核通过自动流程
 * @author liht
 *
 */

@Component("through_the_auditJob")
public class Through_the_auditJob {
	private static final Logger logger = Logger.getLogger(Through_the_auditJob.class);
	@Resource
	Through_the_auditService through_the_auditService; 
	
	@Resource
	private MobileMsgUtil mobileMsgUtil;
	
	public void queryThrough_the_audit(){
		logger.info("定时审核通过任务开始");
		Map<String,String>queryList = new HashMap<String,String>();
		queryList.put("estate", JoinUsVo.WAIT);
		queryList.put("user_level", JoinUsVo.FREE);
		List<Map<String,String>>notPassList = through_the_auditService.queryDoes_not_pass_the_examination(queryList);
		for(int i=0;i<notPassList.size();i++){
				Map<String,String>update_Map = new HashMap<String,String>();
				update_Map.put("user_id", notPassList.get(i).get("user_id").toString());
				update_Map.put("estate", JoinUsVo.PASS);
				update_Map.put("opt_person", "executeJob");
				through_the_auditService.updateWaitPassToPass(update_Map);
				
				 String content = "【19e火车票】恭喜您的申请已经审核通过！感谢您对火车票业务的支持，使用过程中如有问题请查看帮助指南！";
					try {
						 mobileMsgUtil.send(notPassList.get(i).get("user_phone").toString().trim(), content);
					} catch (Exception e) {
						logger.error("短信发送异常", e);
					}
				logger.info("定时审核通过任务，更改为审核通过成功！user_id："+notPassList.get(i).toString());
			
		}
		logger.info("定时审核通过任务结束");
	}
}
