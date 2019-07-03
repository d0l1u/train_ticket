package com.l9e.transaction.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.CommonService;
import com.l9e.util.DateUtil;

/**
 * 处理退票卡单
 * @author zhangjun
 *
 */
@Component("lockAlterManagerJob")
public class LockAlterManagerJob {
	private static final Logger logger = Logger.getLogger(LockAlterManagerJob.class);
	
	@Resource
	private CommonService commonService;
	
	public void lockAlterManager() throws Exception {
		List<Map<String,String>> list =  commonService.queryLockAlterOrderList();
		for(Map<String,String> map :list){
			managerLock(map);
		}
	}
	
	/**
	 * 
	 * @param account
	 */
	private void managerLock(Map<String,String> map){
		
		Date date = new Date();
		String datePre = DateUtil.dateToString(date, "yyyyMMdd");
		Date begin = DateUtil
				.stringToDate(datePre + "053000", "yyyyMMddHHmmss");// 5：30
		Date end = DateUtil.stringToDate(datePre + "233000", "yyyyMMddHHmmss");// 23:30
		if (date.before(begin) || date.after(end)) {
			logger.info("5:30--23：30点方可唤醒卡单处理机器人！");
			return;
		}
		
		//根据卡单当前状态进行相应人工处理
		Map<String,String> logMap = new HashMap<String,String>();
		logMap.put("order_id", map.get("order_id"));
		logMap.put("cp_id", map.get("cp_id"));
		logMap.put("opter", "job_app");
		String status = map.get("refund_status");
		logger.info("lock order_id:"+ map.get("order_id"));
		if("02".equals(status)){
			//正在预定切换人工预定
			map.put("new_refund_status", "03");
			logMap.put("order_optlog", map.get("order_id")+"卡单，正在改签切换为人工改签");
		}else if("06".equals(status) || "04".equals(status)){
			//正在支付切换人工支付
			map.put("new_refund_status", "07");
			logMap.put("order_optlog", map.get("order_id")+"卡单，正在退票切换为人工退票");
		}else if("09".equals(status)){
			//正在支付切换人工支付
			map.put("new_refund_status", "08");
			logMap.put("order_optlog", map.get("order_id")+"卡单，正在审核退票切换为重新审核退票");
		}
		logger.info("退票卡单处理"+map.get("order_id")+"/"+map.get("cp_id"));
		commonService.update19eRefundOrderStatus(map);
		commonService.addCpOrderRefundHistory(logMap);
 	}
	
	/**
	 * 处理预定卡单
	 * @param account
	 */
	public void channelLockOrder(){
		Date date = new Date();
		String datePre = DateUtil.dateToString(date, "yyyyMMdd");
		Date begin = DateUtil
				.stringToDate(datePre + "080000", "yyyyMMddHHmmss");// 5：30
		Date end = DateUtil.stringToDate(datePre + "233000", "yyyyMMddHHmmss");// 23:30
		if (date.before(begin) || date.after(end)) {
			logger.info("8:00--23：10点方可唤醒卡单处理机器人！");
			return;
		}
		
		logger.info("channel lock order manager!");
		//内嵌卡单
		List<Map<String,String>> list =  commonService.queryInnerLockOrderList();
		for(Map<String,String> map_inner :list){
			logger.info("内嵌卡单:"+map_inner.get("order_id"));
			Map<String,String> map_cp = commonService.queryCpOrderList(map_inner);
//			long diff = DateUtil.minuteDiff(new Date(),DateUtil.stringToDate(map_inner.get("create_time"), DateUtil.DATE_FMT3));
			if(null != map_cp){
				String order_status = map_cp.get("order_status");
				if("33".equals(order_status) || "10".equals(order_status) || "99".equals(order_status)){
					commonService.updateLockCpOrderinfoNotify(map_inner);
				}
			}
		}
		
		//19e eop卡单
		List<Map<String,String>> list_eop =  commonService.query19eEopLockOrderList();
		for(Map<String,String> map_eop :list_eop){
			logger.info("map_eop卡单:"+map_eop.get("order_id"));
			long diff = DateUtil.minuteDiff(new Date(),DateUtil.stringToDate(map_eop.get("pay_time"), DateUtil.DATE_FMT3));
			if(diff>5){
				commonService.updateEopLockOrder(map_eop);
			}
		}
		
		//19e 预定卡单
		List<Map<String,String>> list_19e =  commonService.query19eLockOrderList();
		for(Map<String,String> map_19e :list_19e){
			logger.info("map_19e卡单:"+map_19e.get("order_id"));
			Map<String,String> map_cp = commonService.queryCpOrderList(map_19e);
			if(null != map_cp){
				String order_status = map_cp.get("order_status");
				if("33".equals(order_status) || "10".equals(order_status) || "99".equals(order_status)){
					commonService.updateLockCpOrderinfoNotify(map_19e);
				}
			}
		}
		
		//elong卡单
		String df2 = DateUtil.dateToString(date, "yyyy-MM-dd");
		List<Map<String,String>> list_elong =  commonService.queryElongLockOrderList();
		for(Map<String,String> map_elong :list_elong){
			logger.info("map_elong卡单:"+map_elong.get("order_id"));
			Map<String,String> map_cp = commonService.queryCpOrderList(map_elong);
			
			if(null != map_cp){
				String order_status = map_cp.get("order_status");
				if("33".equals(order_status) || "10".equals(order_status) || "99".equals(order_status)){
					commonService.updateLockCpOrderinfoNotify(map_elong);
				}
			}
		}
		
		//qunar卡单
		List<Map<String,String>> list_qunar =  commonService.queryQunarLockOrderList();
		for(Map<String,String> map_qunar :list_qunar){
			logger.info("qunar卡单:"+map_qunar.get("order_id"));
			Map<String,String> map_cp = commonService.queryCpOrderList(map_qunar);
			if(null != map_cp){
				String order_status = map_cp.get("order_status");
				if("33".equals(order_status) || "10".equals(order_status) || "99".equals(order_status)){
					commonService.updateLockCpOrderinfoNotify(map_qunar);
				}
			}
		}
		
		//ext卡单
		List<Map<String,String>> list_ext =  commonService.queryExtLockOrderList();
		for(Map<String,String> map_ext :list_ext){
			logger.info("ext卡单:"+map_ext.get("order_id"));
			Map<String,String> map_cp = commonService.queryCpOrderList(map_ext);
			
			if(null != map_cp){
			String order_status = map_cp.get("order_status");
			if("33".equals(order_status) || "10".equals(order_status) || "99".equals(order_status)){
				commonService.updateLockCpOrderinfoNotify(map_ext);
			}
		}
		}
	}
	
}
