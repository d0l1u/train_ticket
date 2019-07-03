package com.l9e.transaction.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.l9e.transaction.service.CommonService;
import com.l9e.util.DateUtil;

/**
 * 
 * @author zhangjun
 *
 */
@Component("managerLockOrderJob")
public class ManagerLockOrderJob {
	
	@Resource
	private CommonService commonService;
	
	public void managerLockOrder() throws Exception {
		List<Map<String,String>> list =  commonService.queryLockOrderList();
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
			return;
		}
		
		
		//根据卡单当前状态进行相应人工处理
		Map<String,String> logMap = new HashMap<String,String>();
		logMap.put("order_id", map.get("order_id"));
		logMap.put("opter", "job_app");
		String status = map.get("order_status");
		if("11".equals(status)){
			//正在预定切换人工预定
			map.put("new_order_status", "44");
			logMap.put("order_optlog", map.get("order_id")+"卡单，正在预定切换为人工预定");
		}else if("66".equals(status)){
			//正在支付切换人工支付
			map.put("new_order_status", "61");
			logMap.put("order_optlog", map.get("order_id")+"卡单，正在支付切换为人工支付");
		}else if("83".equals(status)){
			//正在取消切换开始取消
			map.put("new_order_status", "85");
			logMap.put("order_optlog", map.get("order_id")+"卡单，正在取消切换为开始取消");
		}else if("88".equals(status)){
			//支付成功切换人工支付
			map.put("new_order_status", "99");
			logMap.put("order_optlog", map.get("order_id")+"卡单，支付成功切换为出票成功");
		}
		commonService.updateCpOrderStatus(map);
		if("88".equals(status)){
			commonService.updateCpOrderNotify(map);
		}
 	}
	
	
}
