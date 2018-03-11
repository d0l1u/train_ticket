package com.l9e.transaction.job;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.ChangeService;
import com.l9e.transaction.vo.ChangeInfo;

/**
 * 改签异步回调job
 * @author licheng
 *
 */
@Component("changeNoticeJob")
public class ChangeNoticeJob {
	
	private static final Logger logger = Logger.getLogger(ChangeNoticeJob.class);
	
	@Resource
	private ChangeService changeService;
	
	

	/**
	 * 改签结果通知
	 */
	public void changeNotice() {
		/*获取回调通知列表*/
		List<ChangeInfo> notifyList = changeService.getNoticeChangeInfo();
		
		if(notifyList!=null && notifyList.size()>0){
			logger.info("改签回调准备开始");
			changeService.changeNotice(notifyList);
		}else{
			logger.info("改签回调信息为空");
		}
	}
		
}
