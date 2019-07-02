package com.l9e.transaction.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.ElongNoticeService;

/**
 *	美团线下退款结果通知接口 
 */
@Component("elongRefundJob")
public class ElongRefundJob {
	@Resource
	private ElongNoticeService elongNoticeService;
	private static final Logger logger = Logger.getLogger(ElongRefundJob.class);
	/**
	 *	美团退款结果通知
	 */
	public void offlineRefund(){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		//线下退票和车站退票的订单
		list=elongNoticeService.getOfflineRefundRefundList();
		
		if(list.size()>0){
			for(Map<String,Object> map:list){
				int count=elongNoticeService.updateStartOfflineRefundNotice(map);
				if(count>0){
					logger.info("美团线下退款,"+map.get("order_id")+",开始");
					elongNoticeService.sendOfflineRefund(map);
				}else{
					logger.info("美团线下退款,"+map.get("order_id")+",锁定");
				}
			}
		}
		
	}
}
