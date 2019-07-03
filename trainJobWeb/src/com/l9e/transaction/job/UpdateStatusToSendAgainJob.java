package com.l9e.transaction.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.dao.SendAgainDao;
import com.l9e.transaction.service.CommonService;
/**
 * 排队订单自动重发Job
 * @author zhangjc02
 *
 */
@Component("updateStatusToSendAgainJob")
public class UpdateStatusToSendAgainJob {
	
	private static final Logger logger = Logger.getLogger(UpdateStatusToSendAgainJob.class);
	@Resource
	private SendAgainDao sendAgainDao;
	@Resource
	private CommonService commonService;
	
	public void sendJob(){
		String queue_time_limit = commonService.querySysSettingByKey("queue_time_limit");
		int num = 10;
		if(!StringUtils.isEmpty(queue_time_limit)){
			num = Integer.parseInt(queue_time_limit);
		}
		this.sendAgain(num);
		this.StopSendAgain(num);
	}
	
	//排队改重发
	private void sendAgain(int num){
		List<String> list = sendAgainDao.queryNeedSendAgainOrder(num);
		if(list.size()>0){
			logger.info("排队待重发的订单数："+list.size());
			for(String orderId: list){
					sendAgainDao.updateToSendAgainByOrderId(orderId);//cp_orderinfo
					logger.info("排队待重发修改至重发成功："+orderId);
					sendAgainDao.updateCpQueueByOrderId(orderId);//cp_orderinfo_queue
					Map<String,String> map = new HashMap<String, String>();
					map.put("order_id", orderId);
					map.put("order_optlog", "排队待重发修改至重发成功："+orderId);
					map.put("opter", "again_job");
					commonService.addCpOrderHistory(map);
			}
		}
	}
	
	//排队改人工
	private void StopSendAgain(int num){
		List<String> list = sendAgainDao.queryNeedToManualOrder(num);
		if(list.size()>0){
			logger.info("排队待重发超时订单数："+list.size());
			for(String orderId: list){
					sendAgainDao.updateToManualByOrderId(orderId);//cp_orderinfo
					logger.info("排队重发超时改人工："+orderId);
					sendAgainDao.updateCpQueueByOrderId(orderId);//cp_orderinfo_queue
					Map<String,String> map = new HashMap<String, String>();
					map.put("order_id", orderId);
					map.put("order_optlog", "排队重发超时改人工："+orderId);
					map.put("opter", "again_job");
					commonService.addCpOrderHistory(map);
			}
		}
	}
}
