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

import com.l9e.transaction.service.RobotCodeService;
import com.l9e.transaction.service.TjPicService;
import com.l9e.util.DateUtil;

@Component("tjRhPicHourJob")
public class TjRhPicHourJob {
	private static final Logger logger = Logger.getLogger(TjRhPicHourJob.class);
	@Resource
	private TjPicService tjPicService;
	
	@Resource
	private RobotCodeService robotService;
	
	
	@SuppressWarnings("unchecked")
	public void queryRhPicHour(){
		int table_count = tjPicService.queryRhPicHourTable_Count();
		
		if(table_count==0){
			logger.info("tjRhPicHourJob自动执行JOB，表中数据为："+table_count+"条，进入第一次执行");
			//List<String> date_List = tj_hc_halfHourService.queryDate_List();
			SimpleDateFormat df1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date(c.getTime().getTime() - 1000 * 60 * 60 * 24));
			String date1 = df1.format(c.getTime()); // 前一天时间

			c = Calendar.getInstance();
			c.setTime(new Date(c.getTime().getTime() - 1000 * 60 * 60 * 24 * 2));
			String date2 = df1.format(c.getTime()); // 前两天时间

			c = Calendar.getInstance();
			c.setTime(new Date(c.getTime().getTime() - 1000 * 60 * 60 * 24 * 3));
			String date3 = df1.format(c.getTime()); // 前三天时间
			
			List date_List = new ArrayList();
			date_List.add(date1);date_List.add(date2);date_List.add(date3);//存放前三天时间的List
			
			for(int i =0;i<date_List.size();i++){
				String create_time = date_List.get(i).toString();
				List<Map<String, String>> hourMap = tjPicService.queryRhPicHour(create_time); // 每个小时打码情况
				for (Map map : hourMap) { // 遍历每个小时的数据
					String hour_stat = (String) map.get("hour_stat");
					String day_stat = (String) map.get("day_stat");
					String code_count = Long.toString((Long)map.get("code_count"));
					
					Map<String,String> pramaMap = new HashMap<String,String>();
					pramaMap.put("day_stat", day_stat);
					pramaMap.put("hour_stat", hour_stat);
					String code_success = tjPicService.queryRhPicHourCodeSuccess(pramaMap);
					String code_fail = tjPicService.queryRhPicHourCodeFail(pramaMap);
					String code_unknown = tjPicService.queryRhPicHourCodeUnknown(pramaMap);
					pramaMap.put("code_success", code_success);
					pramaMap.put("code_fail", code_fail);
					pramaMap.put("code_unknown", code_unknown);
					pramaMap.put("code_count", code_count);
					tjPicService.addToTj_rhPic_hour(pramaMap);
				}
				logger.info("tjRhPicHourJob自动执行JOB完毕");
			}
		}else{
			logger.info("tjRhPicHourJob自动执行JOB，开始往tj_rhPic_hour表中添加数据");
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, 0);    
			Date date = calendar.getTime();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String create_time = df.format(date);
			List<Map<String, String>> hourMap = tjPicService.queryRhPicHour(create_time); // 每个小时打码情况
			for (Map map : hourMap) { // 遍历每个小时的数据
				String hour_stat = (String) map.get("hour_stat");
				String day_stat = (String) map.get("day_stat");
				String code_count = Long.toString((Long)map.get("code_count"));
				
				Map<String,String> pramaMap = new HashMap<String,String>();
				pramaMap.put("day_stat", day_stat);
				pramaMap.put("hour_stat", hour_stat);
				String code_success = tjPicService.queryRhPicHourCodeSuccess(pramaMap);
				String code_fail = tjPicService.queryRhPicHourCodeFail(pramaMap);
				String code_unknown = tjPicService.queryRhPicHourCodeUnknown(pramaMap);
				pramaMap.put("code_success", code_success);
				pramaMap.put("code_fail", code_fail);
				pramaMap.put("code_unknown", code_unknown);
				pramaMap.put("code_count", code_count);
				
				
				int todayCount = tjPicService.queryTodayHourCount(pramaMap);
				if (todayCount == 0) {
					tjPicService.addToTj_rhPic_hour(pramaMap);
				} else {
					tjPicService.updateToTj_rhPic_hour(pramaMap);
				}
				
			}
			logger.info("tjRhPicHourJob自动执行JOB完毕");
		}
	}
	
	
	
	
	
	/**
	 * 登入次数的分时统计
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public void queryRhLoginHour(){
		try {
			Date now = new Date();
			String begin = DateUtil.dateToString(now,DateUtil.DATE_FMT4);
			String end= DateUtil.dateToString(DateUtil.dateAddHour(now, 1),DateUtil.DATE_FMT4);
			
			String dateStr=DateUtil.dateToString(new Date(),DateUtil.DATE_FMT1);
			Map<String,String> param=new HashMap<String,String>();
			param.put("begin", begin);
			param.put("end", end);
			int count=robotService.queryLoginCountById(param);
			logger.info(DateUtil.dateToString(now,DateUtil.DATE_FMT3)+",触发统计,登入次数为"+count);
			param.put("hour_num", now.getHours()+"");
			param.put("login_num", count+"");
			param.put("login_data", dateStr);
			int	noteNum=robotService.selectNoteLogin(param); 
			if(noteNum==0){
				robotService.insertNoteLogin(param);
			}else{
				robotService.updateNoteLogin(param);
			}
		} catch (Exception e) {
			logger.info("分时统计登入次数异常"+e);
			e.printStackTrace();
		}
		
		
	}
}
