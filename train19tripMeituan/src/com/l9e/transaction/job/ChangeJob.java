package com.l9e.transaction.job;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import com.l9e.common.Consts;

import com.l9e.transaction.service.ChangeService;
import com.l9e.transaction.vo.ChangeInfo;
/**
 * 改签job
 * @author caona
 *
 */
@Component("changeJob")
public class ChangeJob {
	@Resource
	private ChangeService changeService;
	private static final Logger logger = Logger.getLogger(ElongRefundJob.class);
	/**
	 *	美团改签结果通知
	 */
	public void changeNotify(){
		List<ChangeInfo> list = new ArrayList<ChangeInfo>();
		//改签记录
		list = changeService.getNoticeChangeInfo(Consts.CHANNEL_MEITUAN);
		
		if(list.size()>0){
			logger.info("美团回调改签信息");
			changeService.callbackChangeNotice(list);
		}else{
			logger.info("美团回调改签记录为空");
		}
		
	}
}
