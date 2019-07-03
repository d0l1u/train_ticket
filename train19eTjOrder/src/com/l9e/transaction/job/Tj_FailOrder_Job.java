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

import com.l9e.transaction.service.Tj_FailOrder_Service;
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
@Component("tj_FailOrder_Job")
public class Tj_FailOrder_Job {

	private static final Logger logger = Logger.getLogger(Tj_FailOrder_Job.class);
	@Resource
	Tj_Hc_orderInfo_Today_Service tj_Hc_orderInfo_Today_Service;
	
	@Resource
	Tj_FailOrder_Service tj_FailOrder_Service;
	
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
		merchantId_list.add("meituan");
		merchantId_list.add("tuniu");
		merchantId_list.add("chq");
		for(int i=0; i<merchantIdList.size(); i++){
			String merchant_id = merchantIdList.get(i).get("merchant_id").toString();
				merchantId_list.add(merchant_id);
		}
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		/************************************ 渠道统计开始 *****************************************/
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		
//		for(int z=1;z<=23;z++){
//			Calendar calendar = Calendar.getInstance();
//			calendar.add(Calendar.DATE, -z); // 得到
//			Date date = calendar.getTime();
//			String create_time = df.format(date);// 时间
//			logger.info(create_time+"Tj_FailOrder_Job自动执行JOB，统计时间为："+ df2.format(date));
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 0); // 得到
		Date date = calendar.getTime();
		String create_time = df.format(date);// 时间
		logger.info("Tj_FailOrder_Job自动执行JOB，统计时间为："+ df2.format(date));
		
		
		int fail_1_lian=0,fail_2_lian=0,fail_3_lian=0,fail_4_lian=0,fail_5_lian=0,
		fail_6_lian=0,fail_7_lian=0,fail_8_lian=0,fail_9_lian=0,fail_11_lian=0,all_order_lian=0;
		
		for(int j=0;j<merchantId_list.size();j++){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("create_time", create_time);
		paramMap.put("channel", merchantId_list.get(j));
		
		Map<String, Object> paramMap2 = new HashMap<String, Object>();
		paramMap2.put("create_time", create_time);
		paramMap2.put("channel", merchantId_list.get(j));
		int all_order=tj_FailOrder_Service.queryFailOrderCount(paramMap2);
		
		paramMap.put("order_status", "10");
		int fail_order=tj_FailOrder_Service.queryFailOrderCount(paramMap);
		
		paramMap.put("error_info","1");
		int fail_1=tj_FailOrder_Service.queryFailOrderCount(paramMap);
		paramMap.put("error_info","2");
		int fail_2=tj_FailOrder_Service.queryFailOrderCount(paramMap);
		paramMap.put("error_info","3");
		int fail_3=tj_FailOrder_Service.queryFailOrderCount(paramMap);
		paramMap.put("error_info","4");
		int fail_4=tj_FailOrder_Service.queryFailOrderCount(paramMap);
		paramMap.put("error_info","5");
		int fail_5=tj_FailOrder_Service.queryFailOrderCount(paramMap);
		
		int fail_6=0,fail_7=0,fail_8=0,fail_9=0,fail_11=0,fail_12=0,fail_0=0,fail_6_2=0,fail_7_2=0;
		if("qunar".equals(merchantId_list.get(j))){
			paramMap.put("error_info","0");
			fail_0=tj_FailOrder_Service.queryFailOrderCount(paramMap);
			paramMap.put("error_info","6");
			fail_6_2=tj_FailOrder_Service.queryFailOrderCount(paramMap);
			paramMap.put("error_info","7");
			fail_7_2=tj_FailOrder_Service.queryFailOrderCount(paramMap);//信息冒用
		}
		else{
			paramMap.put("error_info","6");
			fail_6=tj_FailOrder_Service.queryFailOrderCount(paramMap);
			paramMap.put("error_info","7");
			fail_7=tj_FailOrder_Service.queryFailOrderCount(paramMap);
			paramMap.put("error_info","8");
			fail_8=tj_FailOrder_Service.queryFailOrderCount(paramMap);
			paramMap.put("error_info","9");
			fail_9=tj_FailOrder_Service.queryFailOrderCount(paramMap);
			paramMap.put("error_info","11");
			fail_11=tj_FailOrder_Service.queryFailOrderCount(paramMap);
			paramMap.put("error_info","12");
			fail_12=tj_FailOrder_Service.queryFailOrderCount(paramMap);
		}
		String cgl="";
		if (all_order!= 0) {
			cgl = SwitchUtils.format_perCent((double)(all_order-fail_order) / (double) all_order);
		} else {
			cgl = "0.00%";
		}
		String zhanbi="";
		if (all_order!= 0) {
			zhanbi = SwitchUtils.format_perCent((double) fail_order / (double) all_order);
		} else {
			zhanbi = "0.00%";
		}
		String zhanbi_1="";
		if (all_order!= 0) {
			zhanbi_1 = SwitchUtils.format_perCent((double) fail_1 / (double) all_order);
		} else {
			zhanbi_1 = "0.00%";
		}
		String zhanbi_2="";
		if (all_order!= 0) {
			zhanbi_2 = SwitchUtils.format_perCent((double) fail_2 / (double) all_order);
		} else {
			zhanbi_2 = "0.00%";
		}
		String zhanbi_3="";
		if (all_order!= 0) {
			zhanbi_3 = SwitchUtils.format_perCent((double) fail_3 / (double) all_order);
		} else {
			zhanbi_3 = "0.00%";
		}
		String zhanbi_4="";
		if (all_order!= 0) {
			zhanbi_4 = SwitchUtils.format_perCent((double) fail_4 / (double) all_order);
		} else {
			zhanbi_4 = "0.00%";
		}
		String zhanbi_5="";
		if (all_order!= 0) {
			zhanbi_5 = SwitchUtils.format_perCent((double) fail_5 / (double) all_order);
		} else {
			zhanbi_5 = "0.00%";
		}
		

		String zhanbi_6="",zhanbi_7="",zhanbi_8="",zhanbi_9="",zhanbi_11="",zhanbi_12="",zhanbi_0="",zhanbi_6_2="",zhanbi_7_2="";
		if("qunar".equals(merchantId_list.get(j))){
			if (all_order!= 0) {
				zhanbi_0 = SwitchUtils.format_perCent((double) fail_0 / (double) all_order);
			} else {
				zhanbi_0 = "0.00%";
			}
			if (all_order!= 0) {
				zhanbi_6_2 = SwitchUtils.format_perCent((double) fail_6_2 / (double) all_order);
			} else {
				zhanbi_6_2 = "0.00%";
			}
			if (all_order!= 0) {
				zhanbi_7_2 = SwitchUtils.format_perCent((double) fail_7_2 / (double) all_order);
			} else {
				zhanbi_7_2 = "0.00%";
			}
		}else{
			if (all_order!= 0) {
				zhanbi_6 = SwitchUtils.format_perCent((double) fail_6 / (double) all_order);
			} else {
				zhanbi_6 = "0.00%";
			}
			if (all_order!= 0) {
				zhanbi_7 = SwitchUtils.format_perCent((double) fail_7 / (double) all_order);
			} else {
				zhanbi_7 = "0.00%";
			}
			if (all_order!= 0) {
				zhanbi_8 = SwitchUtils.format_perCent((double) fail_8 / (double) all_order);
			} else {
				zhanbi_8 = "0.00%";
			}
			if (all_order!= 0) {
				zhanbi_9 = SwitchUtils.format_perCent((double) fail_9 / (double) all_order);
			} else {
				zhanbi_9 = "0.00%";
			}
			if (all_order!= 0) {
				zhanbi_11 = SwitchUtils.format_perCent((double) fail_11 / (double) all_order);
			} else {
				zhanbi_11 = "0.00%";
			}
			if (all_order!= 0) {
				zhanbi_12 = SwitchUtils.format_perCent((double) fail_12 / (double) all_order);
			} else {
				zhanbi_12 = "0.00%";
			}
		}
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tj_id", CreateIDUtil.createID("TJ"));
		map.put("count", fail_order);
		map.put("zhanbi", zhanbi);
		map.put("cgl", cgl);
		map.put("fail_1", fail_1);
		map.put("fail_2", fail_2);
		map.put("fail_3", fail_3);
		map.put("fail_4", fail_4);
		map.put("fail_5", fail_5);
		map.put("zhanbi_1", zhanbi_1);
		map.put("zhanbi_2", zhanbi_2);
		map.put("zhanbi_3", zhanbi_3);
		map.put("zhanbi_4", zhanbi_4);
		map.put("zhanbi_5", zhanbi_5);
		
		if("qunar".equals(merchantId_list.get(j))){
			map.put("fail_0", fail_0);
			map.put("fail_8", fail_6_2);
			map.put("fail_12", fail_7_2);
			map.put("zhanbi_0", zhanbi_0);
			map.put("zhanbi_8", zhanbi_6_2);
			map.put("zhanbi_12", zhanbi_7_2);
			//qunar不存在的原因
			map.put("fail_6", 0);
			map.put("fail_7", 0);
			map.put("fail_9", 0);
			map.put("fail_11", 0);
			map.put("zhanbi_6", "0.00%");
			map.put("zhanbi_7", "0.00%");
			map.put("zhanbi_9", "0.00%");
			map.put("zhanbi_11", "0.00%");
		}else{
			map.put("fail_6", fail_6);
			map.put("fail_7", fail_7);
			map.put("fail_8", fail_8);
			map.put("fail_9", fail_9);
			map.put("fail_11", fail_11);
			map.put("fail_12", fail_12);
			map.put("zhanbi_6", zhanbi_6);
			map.put("zhanbi_7", zhanbi_7);
			map.put("zhanbi_8", zhanbi_8);
			map.put("zhanbi_9", zhanbi_9);
			map.put("zhanbi_11", zhanbi_11);
			map.put("zhanbi_12", zhanbi_12);
		}
		map.put("create_time", create_time);
		map.put("all_order", all_order);
		map.put("channel", merchantId_list.get(j));
		
		if("301016".equals(merchantId_list.get(j)) ||"30101601".equals(merchantId_list.get(j))||"30101602".equals(merchantId_list.get(j))){
			 fail_1_lian+=fail_1; fail_2_lian+=fail_2;
			 fail_3_lian+=fail_3; fail_4_lian+=fail_4;
			 fail_5_lian+=fail_5; fail_6_lian+=fail_6;
			 fail_7_lian+=fail_7; fail_8_lian+=fail_8;
			 fail_9_lian+=fail_9; fail_11_lian+=fail_11;
			 all_order_lian+=all_order;
		}
		// 查询tj_hc_orderInfo表中是否有today的记录，如果没有，进行添加，若当天的数据存在，则进行更新
		DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库，将数据更新到运行数据库中
		int todayCount = tj_FailOrder_Service.queryTodayCount(map);
		if(!"301016".equals(merchantId_list.get(j)) &&!"30101601".equals(merchantId_list.get(j))&&!"30101602".equals(merchantId_list.get(j))){
		if (todayCount == 0) {
			// 添加到表tj_hc_orderInfo表中
			tj_FailOrder_Service.addToTj_FailOrder(map);
		} else {
			// 更新tj_hc_orderInfo表中数据
			tj_FailOrder_Service.updateToTj_FailOrder(map);
		}
		}
		/************************************ 渠道统计结束 *****************************************/
		
		/************************************ 利安统计开始 *****************************************/
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		int count_lian=fail_1_lian+fail_2_lian+fail_3_lian+fail_4_lian+fail_5_lian
		 +fail_6_lian+fail_7_lian+fail_8_lian+fail_9_lian+fail_11_lian;
		String cgl_lian="";
		if (all_order_lian!= 0) {
			cgl_lian = SwitchUtils.format_perCent((double)(all_order_lian - count_lian) / (double) all_order_lian);
		} else {
			cgl_lian = "0.00%";
		}
		String zhanbi_lian="";
		if (all_order_lian!= 0) {
			zhanbi_lian = SwitchUtils.format_perCent((double) count_lian / (double) all_order_lian);
		} else {
			zhanbi_lian = "0.00%";
		}
		String zhanbi_1_lian="";
		if (all_order_lian!= 0) {
			zhanbi_1_lian = SwitchUtils.format_perCent((double) fail_1_lian / (double) all_order_lian);
		} else {
			zhanbi_1_lian = "0.00%";
		}
		String zhanbi_2_lian="";
		if (all_order_lian!= 0) {
			zhanbi_2_lian = SwitchUtils.format_perCent((double) fail_2_lian / (double) all_order_lian);
		} else {
			zhanbi_2_lian = "0.00%";
		}
		String zhanbi_3_lian="";
		if (all_order_lian!= 0) {
			zhanbi_3_lian = SwitchUtils.format_perCent((double) fail_3_lian / (double) all_order_lian);
		} else {
			zhanbi_3_lian = "0.00%";
		}
		String zhanbi_4_lian="";
		if (all_order_lian!= 0) {
			zhanbi_4_lian = SwitchUtils.format_perCent((double) fail_4_lian / (double) all_order_lian);
		} else {
			zhanbi_4_lian = "0.00%";
		}
		String zhanbi_5_lian="";
		if (all_order_lian!= 0) {
			zhanbi_5_lian = SwitchUtils.format_perCent((double) fail_5_lian / (double) all_order_lian);
		} else {
			zhanbi_5_lian = "0.00%";
		}
		String zhanbi_6_lian="";
		if (all_order_lian!= 0) {
			zhanbi_6_lian = SwitchUtils.format_perCent((double) fail_6_lian / (double) all_order_lian);
		} else {
			zhanbi_6_lian = "0.00%";
		}
		String zhanbi_7_lian="";
		if (all_order_lian!= 0) {
			zhanbi_7_lian = SwitchUtils.format_perCent((double) fail_7_lian / (double) all_order_lian);
		} else {
			zhanbi_7_lian = "0.00%";
		}
		String zhanbi_8_lian="";
		if (all_order_lian!= 0) {
			zhanbi_8_lian = SwitchUtils.format_perCent((double) fail_8_lian / (double) all_order_lian);
		} else {
			zhanbi_8_lian = "0.00%";
		}
		String zhanbi_9_lian="";
		if (all_order_lian!= 0) {
			zhanbi_9_lian = SwitchUtils.format_perCent((double) fail_9_lian / (double) all_order_lian);
		} else {
			zhanbi_9_lian = "0.00%";
		}
		String zhanbi_11_lian="";
		if (all_order_lian!= 0) {
			zhanbi_11_lian = SwitchUtils.format_perCent((double) fail_11_lian / (double) all_order_lian);
		} else {
			zhanbi_11_lian = "0.00%";
		}
		
		Map<String, Object> lianMap = new HashMap<String, Object>();
		lianMap.put("tj_id", CreateIDUtil.createID("TJ"));
		lianMap.put("count", count_lian);
		lianMap.put("cgl", cgl_lian);
		lianMap.put("zhanbi", zhanbi_lian);
		lianMap.put("all_order", all_order_lian);
		lianMap.put("fail_1", fail_1_lian);
		lianMap.put("fail_2", fail_2_lian);
		lianMap.put("fail_3", fail_3_lian);
		lianMap.put("fail_4", fail_4_lian);
		lianMap.put("fail_5", fail_5_lian);
		lianMap.put("fail_6", fail_6_lian);
		lianMap.put("fail_7", fail_7_lian);
		lianMap.put("fail_8", fail_8_lian);
		lianMap.put("fail_9", fail_9_lian);
		lianMap.put("fail_11", fail_11_lian);
		lianMap.put("zhanbi_1", zhanbi_1_lian);
		lianMap.put("zhanbi_2", zhanbi_2_lian);
		lianMap.put("zhanbi_3", zhanbi_3_lian);
		lianMap.put("zhanbi_4", zhanbi_4_lian);
		lianMap.put("zhanbi_5", zhanbi_5_lian);
		lianMap.put("zhanbi_6", zhanbi_6_lian);
		lianMap.put("zhanbi_7", zhanbi_7_lian);
		lianMap.put("zhanbi_8", zhanbi_8_lian);
		lianMap.put("zhanbi_9", zhanbi_9_lian);
		lianMap.put("zhanbi_11", zhanbi_11_lian);
		lianMap.put("create_time", create_time);
		lianMap.put("channel", "30101612");
		DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库，将数据更新到运行数据库中
		int todaylianCount = tj_FailOrder_Service.queryTodayCount(lianMap);
		if (todaylianCount == 0) {
			tj_FailOrder_Service.addToTj_FailOrder(lianMap);
		} else {
			tj_FailOrder_Service.updateToTj_FailOrder(lianMap);
		}
		/************************************ 利安统计结束 *****************************************/
		}
//		}
		logger.info("Tj_FailOrder_Job自动执行JOB结束");
	}
	

}
