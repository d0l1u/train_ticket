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

import com.l9e.transaction.service.Tj_hc_outTicketSblService;
import com.l9e.util.DateUtil;
import com.l9e.util.DbContextHolder;
/**
 * 统计每半个小时的出票失败率
 * 出票失败率 = 出票失败数/(出票成功数+出票失败数)
 * @author guona
 *
 */
@Component("Tj_hc_outTicketSbl_job")
public class Tj_hc_outTicketSbl_job {
	private static final Logger logger = Logger.getLogger(Tj_hc_outTicketSbl_job.class);
	@Resource
	private Tj_hc_outTicketSblService tj_hc_outTicketSblService ;
	
	public void queryOutTicketSbl(){
		Date date1 = new Date();
		String datePre = DateUtil.dateToString(date1, "yyyyMMdd");
		Date begin = DateUtil.stringToDate(datePre + "071000", "yyyyMMddHHmmss");// 7:00
		Date end = DateUtil.stringToDate(datePre + "231000", "yyyyMMddHHmmss");// 23:00
		if (date1.before(begin) || date1.after(end)) {
			logger.info("7:10-23:10之间可以更新统计表Tj_hc_outTicketSbl_job！");
			return;
		}
		
		DbContextHolder.setDbType("dataSource2");// 设置数据源为备份数据库
		logger.info("统计每半个小时的出票失败率：Tj_hc_outTicketSbl_job自动执行JOB，更新tj_hc_outTicketSbl统计表，更新时间为："+DateUtil.dateToString(date1, "yyyy-MM-dd HH:mm:ss"));
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 0);    //今天的时间
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String create_time = df.format(date);//今天
		//String create_time = "2014-06-26";
		
		List<Map<String, String>> before_map1 = tj_hc_outTicketSblService.queryOutTicketSblBefore(create_time); // 每个前半小时
		List<Map<String, String>> after_map1 = tj_hc_outTicketSblService.queryOutTicketSblAfter(create_time); // 每个后半小时
		List<Map> list1 = new ArrayList<Map>();
		String hour1 = null, day1 = null, sbl = null;
		for (Map map_before1 : before_map1) { // 遍历每个前半小时的数据
			sbl = map_before1.get("sbl").toString();
			hour1 = map_before1.get("hour_stat").toString();
			day1 = map_before1.get("day_stat").toString();
			list1.add(map_before1);// 将每个前半小时的数据加入到list里面
		}
		for (Map map_after1 : after_map1) { // 遍历每个后半小时的数据
			sbl = map_after1.get("sbl").toString();
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
		Map paramMap = new HashMap();
		for (int j = 0; j < list1.size(); j++) {// 循环添加数据集
			sbl = list1.get(j).get("sbl").toString();
			hour1 = list1.get(j).get("hour_stat").toString();
			day1 = list1.get(j).get("day_stat").toString();
			paramMap.put("sbl", sbl);
			paramMap.put("hour_stat", hour1);
			paramMap.put("day_stat", day1);
			DbContextHolder.setDbType("dataSource1");// 设置数据源为运行数据库
			int count = tj_hc_outTicketSblService.queryCount(paramMap);//按照day_stat和hour_stat查询数据是否存在
			if(count == 0){
				//添加到tj_hc_outTicketSbl表中
				tj_hc_outTicketSblService.addToTj_hc_outTicketSbl(paramMap);	
			}else{
				//更新tj_hc_outTicketSbl表中
				tj_hc_outTicketSblService.updateTj_hc_outTicketSbl(paramMap);	
			}
		}
		logger.info("统计每半个小时的出票失败率：Tj_hc_outTicketSbl_job自动执行JOB结束");
	}
	
}
