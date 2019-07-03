package com.l9e.transaction.job;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.Tj_Channel_Service;
import com.l9e.transaction.service.Tj_Hc_orderInfo_Today_Service;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.DbContextHolder;
import com.l9e.util.SwitchUtils;

/**
 * 对当天的数据每隔十分钟进行一次统计，然后更新tj表里面的数据
 * 
 * @author zhangjc
 * 
 */
@Component("tj_Channel_Job")
public class Tj_Channel_Job {

	private static final Logger logger = Logger.getLogger(Tj_Channel_Job.class);
	@Resource
	Tj_Hc_orderInfo_Today_Service tj_Hc_orderInfo_Today_Service;
	
	@Resource
	Tj_Channel_Service tj_Channel_Service;
	
	public void queryHcToInsertUpdateTj() {
		Date date1 = new Date();
		String datePre = DateUtil.dateToString(date1, "yyyyMMdd");
		Date begin = DateUtil.stringToDate(datePre + "060000", "yyyyMMddHHmmss");// 6:00
		Date end = DateUtil.stringToDate(datePre + "235959", "yyyyMMddHHmmss");// 23:00
		if (date1.before(begin) || date1.after(end)) {
			logger.info("06:00-23:59可以更新统计表！");
			return;
		}
		List<Map<String, String>> merchantIdList =  tj_Hc_orderInfo_Today_Service.queryMerchantId();
		List<String> merchantId_list = new ArrayList<String>();//存放对外商户的渠道
		merchantId_list.add("19e");
		merchantId_list.add("qunar");
		merchantId_list.add("cmpay");
		merchantId_list.add("cmwap");
		merchantId_list.add("19pay");
		merchantId_list.add("web");
		merchantId_list.add("app");
		merchantId_list.add("ccb");
		merchantId_list.add("weixin");
		merchantId_list.add("elong");
		merchantId_list.add("tongcheng");
		merchantId_list.add("chq");
		for(int i=0; i<merchantIdList.size(); i++){
			String merchant_id = merchantIdList.get(i).get("merchant_id").toString();
			if(!"301016".equals(merchant_id) ||!"30101601".equals(merchant_id)||!"30101602".equals(merchant_id)){
				System.out.println("merchantIdList.size()"+merchantIdList.size());
				merchantId_list.add(merchant_id);
			}
		}
		int search_count_lian=0;
		int search_success_lian=0;
		int search_fail_lian=0;
		int msg_count_lian=0;
		int alter_success_lian=0;
		int alter_fail_lian=0;
		int refund_success_lian=0;
		int refund_fail_lian=0;
		
		/************************************ 渠道统计开始 *****************************************/
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 0); // 得到
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String create_time = df.format(date);// 时间
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("create_time", create_time);
		logger.info("tj_Channel_Job自动执行JOB，统计时间为："+ df2.format(date));
		for(int j=0;j<merchantId_list.size();j++){
		paramMap.put("channel", merchantId_list.get(j));
		int search_count=tj_Channel_Service.search_count(paramMap);
		int search_success=tj_Channel_Service.search_success(paramMap);
		int search_fail=tj_Channel_Service.search_fail(paramMap);
		int msg_count=tj_Channel_Service.msg_count(paramMap);
		int alter_success=tj_Channel_Service.alter_success(paramMap);
		int alter_fail=tj_Channel_Service.alter_fail(paramMap);
		int refund_success=tj_Channel_Service.refund_success(paramMap);
		int refund_fail=tj_Channel_Service.refund_fail(paramMap);
		
		int alter_count=alter_success+alter_fail;
		int refund_count=refund_success+refund_fail;
		String alter_success_cgl="";
		Double success_cgl;
		if (alter_count!= 0) {
			success_cgl = ((double) alter_success / (double) alter_count);// 订单成功率
			alter_success_cgl = SwitchUtils.format_perCent(success_cgl);
		} else {
			alter_success_cgl = "0.00%";
		}
		String refund_success_cgl="";
		Double refund_cgl;
		if (refund_count!= 0) {
			refund_cgl = ((double) refund_success / (double) refund_count);// 订单成功率
			refund_success_cgl = SwitchUtils.format_perCent(refund_cgl);
		} else {
			refund_success_cgl = "0.00%";
		}
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("channel_id", CreateIDUtil.createID("TJ"));
		map.put("search_count", search_count);
		map.put("search_success", search_success);
		map.put("search_fail", search_fail);
		map.put("msg_count", msg_count);
		map.put("alter_count", alter_count);
		map.put("alter_success", alter_success);
		map.put("alter_fail", alter_fail);
		map.put("refund_count", refund_count);
		map.put("refund_success", refund_success);
		map.put("refund_fail", refund_fail);
		map.put("alter_success_cgl", alter_success_cgl);
		map.put("refund_success_cgl", refund_success_cgl);
		map.put("create_time", df.format(date));
		map.put("channel", merchantId_list.get(j));
		
		if("301016".equals(merchantId_list.get(j)) ||"30101601".equals(merchantId_list.get(j))||"30101602".equals(merchantId_list.get(j))){
			search_count_lian+=search_count;
			search_success_lian+=search_success;
			search_fail_lian+=search_fail;
			msg_count_lian+=msg_count;
			alter_success_lian+=alter_success;
			alter_fail_lian+=alter_fail;
			refund_success_lian+=refund_success;
			refund_fail_lian+=refund_fail;
		}
		// 查询tj_hc_orderInfo表中是否有today的记录，如果没有，进行添加，若当天的数据存在，则进行更新
		DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库，将数据更新到运行数据库中
		int today19eCount = tj_Channel_Service.query19eTodayCount(map);
		if (today19eCount == 0) {
			// 添加到表tj_hc_orderInfo表中
			tj_Channel_Service.addToTj_Channel(map);
		} else {
			// 更新tj_hc_orderInfo表中数据
			tj_Channel_Service.updateToTj_Channel(map);
		}
		/************************************ 渠道统计结束 *****************************************/
		
		/************************************ 利安统计开始 *****************************************/
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		int alter_count_lian=alter_success_lian+alter_fail_lian;
		int refund_count_lian=refund_success_lian+refund_fail_lian;
		String alter_success_cgl_lian="";
		Double success_cgl_lian;
		if (alter_count_lian!= 0) {
			success_cgl_lian = ((double) alter_success_lian / (double) alter_count_lian);// 订单成功率
			alter_success_cgl_lian = SwitchUtils.format_perCent(success_cgl_lian);
		} else {
			alter_success_cgl_lian = "0.00%";
		}
		String refund_success_cgl_lian="";
		Double refund_cgl_lian;
		if (refund_count_lian!= 0) {
			refund_cgl_lian = ((double) refund_success_lian / (double) refund_count_lian);// 订单成功率
			refund_success_cgl_lian = SwitchUtils.format_perCent(refund_cgl_lian);
		} else {
			refund_success_cgl_lian = "0.00%";
		}
		
		Map<String, Object> lianMap = new HashMap<String, Object>();
		lianMap.put("channel_id", CreateIDUtil.createID("TJ"));
		lianMap.put("search_count", search_count_lian);
		lianMap.put("search_success", search_success_lian);
		lianMap.put("search_fail", search_fail_lian);
		lianMap.put("msg_count", msg_count_lian);
		lianMap.put("alter_count", alter_count_lian);
		lianMap.put("alter_success", alter_success_lian);
		lianMap.put("alter_fail", alter_fail_lian);
		lianMap.put("refund_count", refund_count_lian);
		lianMap.put("refund_success", refund_success_lian);
		lianMap.put("refund_fail", refund_fail_lian);
		lianMap.put("alter_success_cgl", alter_success_cgl_lian);
		lianMap.put("refund_success_cgl", refund_success_cgl_lian);
		lianMap.put("create_time", df.format(date));
		lianMap.put("channel", "30101612");
		DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库，将数据更新到运行数据库中
		int todaylianCount = tj_Channel_Service.query19eTodayCount(lianMap);
		if (todaylianCount == 0) {
			tj_Channel_Service.addToTj_Channel(lianMap);
		} else {
			tj_Channel_Service.updateToTj_Channel(lianMap);
		}
		/************************************ 利安统计结束 *****************************************/
		}
		logger.info("tj_Channel_Job自动执行JOB结束");
	}
	

}
