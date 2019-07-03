package com.l9e.transaction.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.ElongNoticeService;

/**
 *	elong退票通知job
 * 	@author liuyi02
 *
 */
@Component("elongRefundNoticeJob")
public class ElongRefundNoticeJob {
	@Resource
	private ElongNoticeService elongNoticeService;
	private static final Logger logger = Logger.getLogger(ElongRefundNoticeJob.class);
	/**
	 *	退票通知
	 */
	public void refundnotice(){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		list=elongNoticeService.getRefundNoticesList();
		if(list.size()>0){
			for(Map<String,Object> map:list){
				int count=elongNoticeService.updateBeginRefundNotice(map);
				if (count>0){
					logger.info("退票通知"+map.get("order_id")+",开始");
					elongNoticeService.sendRefundNotice(map);
				}else{
					logger.info("退票通知"+map.get("order_id")+",锁定");
				}
			}
		}
	}
}
