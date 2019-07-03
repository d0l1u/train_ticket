package com.l9e.transaction.job;

import java.util.ArrayList;
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
 * 预定超时未支付
 * @author zyx
 *
 */
@Component("extLockOrderBookJob")
public class ExtLockOrderBookJob {
	
	private static final Logger logger = Logger.getLogger(ExtLockOrderBookJob.class);
	
	@Resource
	private CommonService commonService;
	
	public void bookNotify() throws Exception {
		Date date = new Date();
		String datePre = DateUtil.dateToString(date, "yyyyMMdd");
		Date begin = DateUtil
				.stringToDate(datePre + "053000", "yyyyMMddHHmmss");// 5：30
		Date end = DateUtil.stringToDate(datePre + "233000", "yyyyMMddHHmmss");// 23:30
		if (date.before(begin) || date.after(end)) {
			logger.info("5:30--23：30点方可唤醒超市未支付机器人！");
			return;
		}
		
		try{
			logger.info("start get the timeout order which is not pay!");
			//先获取，订票流程为先预定后支付的对外商户信息
			List<Map<String,String>> list_merchant = commonService.queryExtMerchantList();
			//获取超时未支付订单
			List<String> list_str = new ArrayList<String>();
			for(Map<String,String> map :list_merchant){
				logger.info("book_limit_time :"+map.get("book_limit_time"));
				map.put("order_status", "45");
				map.put("channel", map.get("merchant_id"));
//				String limit_time =  DateUtil.minuteBefore(Integer.valueOf(map.get("book_limit_time")));
//				logger.info("out_ticket_time limit :" +limit_time);
				map.put("book_limit_time",map.get("book_limit_time"));
				List<String> list = commonService.queryLockBookOrder(map);
				list_str.addAll(list);
			}
			//将该订单置为失败订单，失败原因为超时未支付
			for(String order_id:list_str){
				logger.info("the timeout order make it filure!"+order_id);
				Map<String,String> up_map = new HashMap<String,String>();
				up_map.put("order_id", order_id);
				up_map.put("error_info", "11");
				up_map.put("opt_ren", "job_verify");
				up_map.put("new_order_status", "10");
				up_map.put("order_status", "45");
				// 更新订单状态
				commonService.updateCpOrderStatus(up_map);
				
				up_map.put("notify_status", "1");
				
				// 开始发送通知给前台
				commonService.updateCpOrderinfoNotify(up_map);
				
				Map<String,String> his_map = new HashMap<String,String>();
				his_map.put("order_id", order_id);
				his_map.put("order_optlog", "超市订单未支付，直接出票失败！");
				his_map.put("opter", "job_verify");
				commonService.insertCpHistory(his_map);
			}
		}catch(Exception e){
			logger.error("超市订单未支付，直接出票失败操作异常！", e);
		}
		//内嵌cmpay
		try{
			logger.info("start 内嵌cmpay order which is not pay!");
			//获取超时未支付订单
			List<String> list_str = new ArrayList<String>();
			Map<String,String> map = new HashMap<String,String>();
			map.put("order_status", "45");
			map.put("channel", "cmpay");
//				String limit_time =  DateUtil.minuteBefore(Integer.valueOf(map.get("book_limit_time")));
//				logger.info("out_ticket_time limit :" +limit_time);
			map.put("book_limit_time","30");
			List<String> list = commonService.queryLockBookOrder(map);
			list_str.addAll(list);
			//将该订单置为失败订单，失败原因为超时未支付
			for(String order_id:list_str){
				logger.info("the timeout cmpay order make it filure!"+order_id);
				Map<String,String> up_map = new HashMap<String,String>();
				up_map.put("order_id", order_id);
				up_map.put("error_info", "11");
				up_map.put("opt_ren", "job_verify");
				up_map.put("new_order_status", "10");
				up_map.put("order_status", "45");
				// 更新订单状态
				commonService.updateCpOrderStatus(up_map);
				
				up_map.put("notify_status", "1");
				
				// 开始发送通知给前台
				commonService.updateCpOrderinfoNotify(up_map);
				
				Map<String,String> his_map = new HashMap<String,String>();
				his_map.put("order_id", order_id);
				his_map.put("order_optlog", "超时订单未支付，直接出票失败！");
				his_map.put("opter", "job_verify");
				commonService.insertCpHistory(his_map);
			}
		}catch(Exception e){
			logger.error("超市订单未支付，直接出票失败操作异常！", e);
		}
	}
	
	public void tcNoPay() throws Exception {
		Date date = new Date();
		String datePre = DateUtil.dateToString(date, "yyyyMMdd");
		Date begin = DateUtil
				.stringToDate(datePre + "053000", "yyyyMMddHHmmss");// 5：30
		Date end = DateUtil.stringToDate(datePre + "233000", "yyyyMMddHHmmss");// 23:30
		if (date.before(begin) || date.after(end)) {
			logger.info("5:30--23：30点方可唤醒超市未支付机器人！");
			return;
		}
		
		try{
			logger.info("start get the tongcheng timeout order which is not pay!");
			//获取超时未支付订单
			List<String> list_str = new ArrayList<String>();
			Map<String,String> map = new HashMap<String,String>();
			map.put("order_status", "45");
			map.put("channel", "tongcheng");
//				String limit_time =  DateUtil.minuteBefore(Integer.valueOf(map.get("book_limit_time")));
//				logger.info("out_ticket_time limit :" +limit_time);
			map.put("book_limit_time","35");
			List<String> list = commonService.queryLockBookOrder(map);
			list_str.addAll(list);
			//将该订单置为失败订单，失败原因为超时未支付
			for(String order_id:list_str){
				logger.info("the timeout order make it cancel tongcheng channel!"+order_id);
				Map<String,String> up_map = new HashMap<String,String>();
				up_map.put("order_id", order_id);
				up_map.put("error_info", "11");
				up_map.put("opt_ren", "job_verify");
				up_map.put("new_order_status", "10");//自动失败
				up_map.put("order_status", "45");
				up_map.put("return_optlog", "C1");//返回日志添加自动取消
				// 更新订单状态
				commonService.updateCpOrderStatus(up_map);
				
				up_map.put("notify_status", "1");
				
				// 开始发送通知给前台
				commonService.updateCpOrderinfoNotify(up_map);
				
				Map<String,String> his_map = new HashMap<String,String>();
				his_map.put("order_id", order_id);
				his_map.put("order_optlog", "超市订单未支付，转取消订单动作");
				his_map.put("opter", "job_verify");
				commonService.insertCpHistory(his_map);
				
				//并且释放账号
				String acc_id=commonService.queryAccountIdByOrderId(order_id);
				commonService.updateAccountStatusFree(acc_id);
				logger.info("updateAccountStatusFree order_id:"+order_id+"_"+acc_id);
				
			}
		}catch(Exception e){
			logger.error("超市订单未支付，直接出票失败操作异常！", e);
		}
		
		
		//elong超时时间为30分钟
		try{
			logger.info("start get the elong timeout order which is not pay!");
			//获取超时未支付订单
			List<String> list_str = new ArrayList<String>();
			Map<String,String> map = new HashMap<String,String>();
			map.put("order_status", "45");
			map.put("channel", "elong");
//				String limit_time =  DateUtil.minuteBefore(Integer.valueOf(map.get("book_limit_time")));
//				logger.info("out_ticket_time limit :" +limit_time);
			map.put("book_limit_time","20");
			List<String> list = commonService.queryLockBookOrder(map);
			list_str.addAll(list);
			//将该订单置为失败订单，失败原因为超时未支付
			for(String order_id:list_str){
				logger.info("the timeout order make it filure!"+order_id);
				Map<String,String> up_map = new HashMap<String,String>();
				up_map.put("order_id", order_id);
				up_map.put("error_info", "11");
				up_map.put("opt_ren", "job_verify");
				up_map.put("new_order_status", "10");
				up_map.put("order_status", "45");
				// 更新订单状态
				commonService.updateCpOrderStatus(up_map);
				
				up_map.put("notify_status", "1");
				
				// 开始发送通知给前台
				commonService.updateCpOrderinfoNotify(up_map);
				
				Map<String,String> his_map = new HashMap<String,String>();
				his_map.put("order_id", order_id);
				his_map.put("order_optlog", "超时订单未支付，直接出票失败！");
				his_map.put("opter", "job_verify");
				commonService.insertCpHistory(his_map);
			}
		}catch(Exception e){
			logger.error("超市订单未支付，直接出票失败操作异常！", e);
		}
		
	}
	
	/**
	 * 途牛超时未支付订单
	 * @throws Exception
	 */
	public void tuniuNoPay() throws Exception {
		Date date = new Date();
		String datePre = DateUtil.dateToString(date, "yyyyMMdd");
		Date begin = DateUtil
				.stringToDate(datePre + "053000", "yyyyMMddHHmmss");// 5：30
		Date end = DateUtil.stringToDate(datePre + "233000", "yyyyMMddHHmmss");// 23:30
		if (date.before(begin) || date.after(end)) {
			logger.info("5:30--23：30点方可唤醒超市未支付！");
			return;
		}
		
		try{
			logger.info("start get the tuniu timeout order which is not pay!");
			//获取超时未支付订单
			List<String> list_str = new ArrayList<String>();
			Map<String,String> map = new HashMap<String,String>();
			map.put("order_status", "45");
			map.put("channel", "tuniu");
//				String limit_time =  DateUtil.minuteBefore(Integer.valueOf(map.get("book_limit_time")));
//				logger.info("out_ticket_time limit :" +limit_time);
			map.put("book_limit_time","27");
			List<String> list = commonService.queryLockBookOrder(map);
			list_str.addAll(list);
			//将该订单置为失败订单，失败原因为超时未支付
			for(String order_id:list_str){
				logger.info("the timeout order make it cancel tuniu channel!"+order_id);
				Map<String,String> up_map = new HashMap<String,String>();
				up_map.put("order_id", order_id);
				up_map.put("error_info", "11");
				up_map.put("opt_ren", "job_verify");
				up_map.put("new_order_status", "10");//自动失败
				up_map.put("order_status", "45");
				up_map.put("return_optlog", "C1");//返回日志添加自动取消
				// 更新订单状态
				commonService.updateCpOrderStatus(up_map);
				
				up_map.put("notify_status", "1");
				
				// 开始发送通知给前台
				commonService.updateCpOrderinfoNotify(up_map);
				
				Map<String,String> his_map = new HashMap<String,String>();
				his_map.put("order_id", order_id);
				his_map.put("order_optlog", "超市订单未支付，转取消订单动作");
				his_map.put("opter", "job_verify");
				commonService.insertCpHistory(his_map);
				
				//并且释放账号
				String acc_id=commonService.queryAccountIdByOrderId(order_id);
				commonService.updateAccountStatusFree(acc_id);
				logger.info("updateAccountStatusFree order_id:"+order_id+"_"+acc_id);
				
			}
		}catch(Exception e){
			logger.error("超市订单未支付，直接出票失败操作异常！", e);
		}
		
	}
	
	/**
	 * 美团超时未支付订单
	 * @throws Exception
	 */
	public void meituanNoPay() throws Exception {
		Date date = new Date();
		String datePre = DateUtil.dateToString(date, "yyyyMMdd");
		Date begin = DateUtil
				.stringToDate(datePre + "053000", "yyyyMMddHHmmss");// 5：30
		Date end = DateUtil.stringToDate(datePre + "233000", "yyyyMMddHHmmss");// 23:30
		if (date.before(begin) || date.after(end)) {
			logger.info("5:30--23：30点方可唤醒超时未支付！");
			return;
		}
		try{
			logger.info("start get the meituan timeout order which is not pay!");
			//获取超时未支付订单
			List<String> list_str = new ArrayList<String>();
			Map<String,String> map = new HashMap<String,String>();
			map.put("order_status", "45");
			map.put("channel", "meituan");
//				String limit_time =  DateUtil.minuteBefore(Integer.valueOf(map.get("book_limit_time")));
//				logger.info("out_ticket_time limit :" +limit_time);
			map.put("book_limit_time","27");
			List<String> list = commonService.queryLockBookOrder(map);
			list_str.addAll(list);
			//将该订单置为失败订单，失败原因为超时未支付
			for(String order_id:list_str){
				logger.info("the timeout order make it cancel meituan channel!"+order_id);
				Map<String,String> up_map = new HashMap<String,String>();
				up_map.put("order_id", order_id);
				up_map.put("error_info", "11");
				up_map.put("opt_ren", "job_verify");
				up_map.put("new_order_status", "10");//自动失败
				up_map.put("order_status", "45");
				up_map.put("return_optlog", "C1");//返回日志添加自动取消
				// 更新订单状态
				commonService.updateCpOrderStatus(up_map);
				
				up_map.put("notify_status", "1");
				
				// 开始发送通知给前台
				commonService.updateCpOrderinfoNotify(up_map);
				
				Map<String,String> his_map = new HashMap<String,String>();
				his_map.put("order_id", order_id);
				his_map.put("order_optlog", "超时订单未支付，转取消订单动作");
				his_map.put("opter", "job_verify");
				commonService.insertCpHistory(his_map);
				
				//并且释放账号
				String acc_id=commonService.queryAccountIdByOrderId(order_id);
				commonService.updateAccountStatusFree(acc_id);
				logger.info("updateAccountStatusFree order_id:"+order_id+"_"+acc_id);
			}
		}catch(Exception e){
			logger.error("超时订单未支付，直接出票失败操作异常！", e);
		}
		
	}
	
	
	/**
	 * 高铁超时未支付订单
	 * @throws Exception
	 */
	public void gaotieNoPay() throws Exception {
		Date date = new Date();
		String datePre = DateUtil.dateToString(date, "yyyyMMdd");
		Date begin = DateUtil
				.stringToDate(datePre + "053000", "yyyyMMddHHmmss");// 5：30
		Date end = DateUtil.stringToDate(datePre + "233000", "yyyyMMddHHmmss");// 23:30
		if (date.before(begin) || date.after(end)) {
			logger.info("5:30--23：30点方可唤醒超时未支付！");
			return;
		}
		try{
			logger.info("start get the 高铁管家 timeout order which is not pay!");
			//获取超时未支付订单
			List<String> list_str = new ArrayList<String>();
			Map<String,String> map = new HashMap<String,String>();
			map.put("order_status", "45");
			map.put("channel", "301030");
//				String limit_time =  DateUtil.minuteBefore(Integer.valueOf(map.get("book_limit_time")));
//				logger.info("out_ticket_time limit :" +limit_time);
			map.put("book_limit_time","29");
			List<String> list = commonService.queryLockBookOrder(map);
			list_str.addAll(list);
			//将该订单置为失败订单，失败原因为超时未支付
			for(String order_id:list_str){
				logger.info("the timeout order make it cancel 高铁 channel!"+order_id);
				Map<String,String> up_map = new HashMap<String,String>();
				up_map.put("order_id", order_id);
				up_map.put("error_info", "11");
				up_map.put("opt_ren", "job_verify");
				up_map.put("new_order_status", "10");//自动失败
				up_map.put("order_status", "45");
				up_map.put("return_optlog", "C1");//返回日志添加自动取消
				// 更新订单状态
				commonService.updateCpOrderStatus(up_map);
				
				up_map.put("notify_status", "1");
				
				// 开始发送通知给前台
				commonService.updateCpOrderinfoNotify(up_map);
				
				Map<String,String> his_map = new HashMap<String,String>();
				his_map.put("order_id", order_id);
				his_map.put("order_optlog", "超时订单未支付，转取消订单动作,train_job");
				his_map.put("opter", "job_verify");
				commonService.insertCpHistory(his_map);
				
				//并且释放账号
				String acc_id=commonService.queryAccountIdByOrderId(order_id);
				commonService.updateAccountStatusFree(acc_id);
				logger.info("updateAccountStatusFree order_id:"+order_id+"_"+acc_id);
			}
		}catch(Exception e){
			logger.error("超时订单未支付，直接出票失败操作异常！", e);
		}
		
	}
	
	
	
	
	
	
	
}
