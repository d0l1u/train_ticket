package com.l9e.transaction.job;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.common.ElongChangeConsts;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.ElongChangeService;
import com.l9e.transaction.vo.ElongChangeInfo;

@Component("elongChangeNoticeJob")
public class ElongChangeNoticeJob {
	private static final Logger logger = Logger.getLogger(ElongChangeNoticeJob.class);	
	@Resource
	private ElongChangeService changeService;
	/**
	 * 改签结果通知
	 */
	public void changeNotice() {
	
		List<ElongChangeInfo> notifyList = changeService.getNoticeChangeInfo(ElongChangeConsts.MERCHANT_ID);
		
		if(notifyList!=null && notifyList.size()>0){
			logger.info("elong改签回调准备开始");
			changeService.changeNotice(notifyList);
		}else{
			logger.info("elong改签回调信息为空");
		}
	}
		


}
