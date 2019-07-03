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

import com.l9e.transaction.service.Tj_hc_halfHourService;
import com.l9e.util.DateUtil;
import com.l9e.util.DbContextHolder;
/**
 * 小时销售统计
 * @author guona
 *
 */
@Component("Tj_hc_halfHour_job")
public class Tj_hc_halfHour_job {
	private static final Logger logger = Logger.getLogger(Tj_hc_halfHour_job.class);
	@Resource
	private Tj_hc_halfHourService tj_hc_halfHourService ;
	
	public void queryHalfHour(){
		Date date1 = new Date();
		String datePre = DateUtil.dateToString(date1, "yyyyMMdd");
		Date begin = DateUtil
				.stringToDate(datePre + "060000", "yyyyMMddHHmmss");// 6:00
		Date end = DateUtil.stringToDate(datePre + "235959", "yyyyMMddHHmmss");// 23:00
		if (date1.before(begin) || date1.after(end)) {
			logger.info("6:00-23:59之间可以更新统计表tj_hc_halfHour！");
			return;
		}
		
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		//int table_count = tj_hc_halfHourService.queryTable_Count();
		logger.info("Tj_hc_halfHour_job自动执行JOB，更新tj_hc_halfHour统计表，更新时间为："+DateUtil.dateToString(date1, "yyyy-MM-dd HH:mm:ss"));
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 0);    //今天的时间
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String create_time = df.format(date);//今天
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("create_time", create_time);
		List<Map<String, String>> before_map1 = tj_hc_halfHourService.queryDayTimeBefore(create_time); // 每个前半小时
		List<Map<String, String>> after_map1 = tj_hc_halfHourService.queryDayTimeAfter(create_time); // 每个后半小时
		List<Map> list1 = new ArrayList<Map>();
		long num1 = 0;
		String hour1 = null, day1 = null;
		for (Map map_before1 : before_map1) { // 遍历每个前半小时的数据
			num1 = (Long) map_before1.get("order_count");
			hour1 = map_before1.get("hour_stat").toString();
			day1 = map_before1.get("day_stat").toString();
			list1.add(map_before1);// 将每个前半小时的数据加入到list里面
		}
		for (Map map_after1 : after_map1) { // 遍历每个后半小时的数据
			num1 = (Long) map_after1.get("order_count");
			hour1 = map_after1.get("hour_stat").toString();
			day1 = map_after1.get("day_stat").toString();
			list1.add(map_after1);// 将每个后半小时的数据加入到list里面
		}
		Map currentMap1; // 对list按照hour_stat进行排序
		for (int k = 0; k < list1.size() - 1; k++) {
			for (int j = 0; j < list1.size() - k - 1; j++) {
				String[] hh = list1.get(j).get("hour_stat").toString().split(":");
				String[] jj = list1.get(j + 1).get("hour_stat").toString().split(":");
				int m = Integer.parseInt(hh[0]);
				int n = Integer.parseInt(jj[0]);
				if (m > n) {
					currentMap1 = list1.get(j + 1);
					list1.set(j + 1, list1.get(j));
					list1.set(j, currentMap1);
				}
			}
		}
		Map map = new HashMap();
		for (int j = 0; j < list1.size(); j++) {// 循环添加数据集
			num1 = (Long) list1.get(j).get("order_count");
			hour1 = list1.get(j).get("hour_stat").toString();
			day1 = list1.get(j).get("day_stat").toString();
			map.put("order_count", num1);
			map.put("hour_stat", hour1);
			map.put("day_stat", day1);
			DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库
			int count = tj_hc_halfHourService.queryCount(map);//按照day_stat和hour_stat查询数据是否存在
			if(count == 0){
				//添加到tj_hc_halfHour表中
				tj_hc_halfHourService.addToTj_hc_halfHour(map);	
			}else{
				//更新tj_hc_halfHour表中
				tj_hc_halfHourService.updateTj_hc_halfHour(map);	
			}
		}
		logger.info("Tj_hc_halfHour_job自动执行JOB结束");
	}
	
}
