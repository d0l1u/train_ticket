package com.l9e.transaction.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.ElongNoticeService;

/**
 *	线下退款通知接口 
 * 	@author liuyi02
 *
 */
@Component("elongRefundJob")
public class ElongRefundJob {
	@Resource
	private ElongNoticeService elongNoticeService;
	private static final Logger logger = Logger.getLogger(ElongRefundJob.class);
	/**
	 *	退款结果通知
	 */
	public void offlineRefund(){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		list=elongNoticeService.getOfflineRefundRefundList();
		
		/*for(Map<String,Object> map:list){
			elongNoticeService.updateStartOfflineRefundNotice(map.get("stream_id").toString());
		}
		*/
		
		if(list.size()>0){
			for(Map<String,Object> map:list){
				int count=elongNoticeService.updateStartOfflineRefundNotice(map);
				if(count>0){
					logger.info("艺龙线下退款,"+map.get("order_id")+",开始");
					elongNoticeService.sendOfflineRefund(map);
				}else{
					logger.info("艺龙线下退款,"+map.get("order_id")+",锁定");
				}
			}
		}
		
		
		
		List<Map<String,Object>> listTc=new ArrayList<Map<String,Object>>();
		listTc=elongNoticeService.getTcOfflineRefundRefundList();
		
		/*for(Map<String,Object> map:listTc){
			elongNoticeService.updateStartOfflineRefundNotice(map.get("stream_id").toString());
		}*/
		
		if(listTc.size()>0){
			for(Map<String,Object> map:listTc){
				int count=elongNoticeService.updateStartOfflineRefundNotice(map);
				if(count>0){
					logger.info("tc线下退款,"+map.get("order_id")+",开始");
					elongNoticeService.sendOfflineRefund(map);
				}else{
					logger.info("tc线下退款,"+map.get("order_id")+",锁定");
				}
			}
		}
	}
}
