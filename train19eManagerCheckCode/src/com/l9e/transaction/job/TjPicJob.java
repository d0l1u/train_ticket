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

import com.l9e.transaction.service.TjPicService;

@Component("TjPicJob")
public class TjPicJob {
	private static final Logger logger = Logger.getLogger(TjPicJob.class);
	@Resource
	private TjPicService tjPicService;

	public void queryTjPic(){
		int table_count = tjPicService.queryTable_Count();
	
	if(table_count==0){
		logger.info("TjPicJob自动执行JOB，表中数据为："+table_count+"条，进入第一次执行");
		List<String> date_List = tjPicService.queryDate_List();
		for(int i =0;i<date_List.size()-1;i++){
			String create_time = date_List.get(i).toString();
			//String create_time="2014-01-02";
			Map<String,Object>paramMap = new HashMap<String,Object>();
			paramMap.put("create_time", create_time);
			
			//对于用户名不为空（IS NOT NULL）
			List<Map<String, String>> OptrenMap = tjPicService.queryOptrenAndPiccount(create_time);  
			String opt_ren = null,opt_name=null,opt_time=null,channel=null;
			//String channel=null;
			long pic_count=0;
			Integer pic_success=0,pic_fail=0,pic_unkonwn=0,pic_notOpt=0;
			for (Map map : OptrenMap) {  
				opt_ren = map.get("opt_ren").toString();
				pic_count = (Long) map.get("pic_count");
				Map<String,String> queryMap = new HashMap<String,String>();
				queryMap.put("opt_ren", opt_ren);
				queryMap.put("create_time", create_time);
				pic_success = tjPicService.queryPicSuccess(queryMap);
				pic_fail = tjPicService.queryPicFail(queryMap);
				pic_unkonwn = tjPicService.queryPicUnknown(queryMap);
				pic_notOpt = tjPicService.queryPicNotOpt(queryMap);
				//channel = tjPicService.queryChannel(opt_ren);
				if("qunar".equals(opt_ren)){
					channel = "qunar";
				}else if("damatu".equals(opt_ren)){
					channel = "damatu";
				}else{
					channel = "19e";
				}
				opt_name = tjPicService.queryOptname(opt_ren);
				SimpleDateFormat df1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
				opt_time = create_time;
				Map<String,Object> addMap = new HashMap<String,Object>();
				addMap.put("create_time", create_time);
				addMap.put("opt_ren", opt_ren);
				addMap.put("pic_count", pic_count);
				addMap.put("pic_success", pic_success);
				addMap.put("pic_fail", pic_fail);
				addMap.put("pic_unkonwn", pic_unkonwn);
				addMap.put("pic_notOpt", pic_notOpt);
				addMap.put("channel", channel);
				addMap.put("opt_name", opt_name);
				addMap.put("opt_time", opt_time);
				tjPicService.addTjPic(addMap);
			}
			//对于用户名为空（IS NULL）
			Map<String, Object> nullMap = tjPicService.queryNullCodeCount(create_time);
			int nullCount = (Integer) nullMap.get("pic_count");
			int pic_unkonwn1 = tjPicService.queryNullAndUnknownCodeCount(create_time);
			int pic_notOpt1 = tjPicService.queryNullAndNotoptCodeCount(create_time);
			//channel = (String)nullMap.get("channel");
			Map<String,Object> addNullMap = new HashMap<String,Object>();
			addNullMap.put("create_time", create_time);
			addNullMap.put("opt_ren", "wuren");
			addNullMap.put("pic_count", nullCount);
			addNullMap.put("pic_success", 0);
			addNullMap.put("pic_fail", 0);
			addNullMap.put("pic_unkonwn", pic_unkonwn1);
			addNullMap.put("pic_notOpt", pic_notOpt1);
			addNullMap.put("channel", "wuren");
			addNullMap.put("opt_name", "无人打码");
			addNullMap.put("opt_time", create_time);
			tjPicService.addTjPic(addNullMap);
			
			logger.info("TjPicJob自动执行JOB完毕");
		}
		}else{
			logger.info("TjPicJob自动执行JOB，往tj_rh_picture表中添加数据");
//			SimpleDateFormat df1 = new java.text.SimpleDateFormat("yyyy-MM-dd");
//			Calendar c = Calendar.getInstance();
//			c.setTime(new Date(c.getTime().getTime() - 1000 * 60 * 60 * 24));
//			String create_time = df1.format(c.getTime()); // 前一天时间
//			//String create_time="2014-01-17";
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, 0); // 得到
			Date date = calendar.getTime();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String create_time = df.format(date);// 时间
			List<Map<String, String>> OptrenMap = tjPicService.queryOptrenAndPiccount(create_time);  
			String opt_ren = null,opt_name=null,opt_time=null,channel=null;
			//String channel = null;
			long pic_count=0;
			Integer pic_success=0,pic_fail=0,pic_unkonwn=0,pic_notOpt=0;
			for (Map map : OptrenMap) {  
				opt_ren = map.get("opt_ren").toString();
				pic_count = (Long) map.get("pic_count");
				Map<String,String> queryMap = new HashMap<String,String>();
				queryMap.put("opt_ren", opt_ren);
				queryMap.put("create_time", create_time);
				pic_success = tjPicService.queryPicSuccess(queryMap);
				pic_fail = tjPicService.queryPicFail(queryMap);
				pic_unkonwn = tjPicService.queryPicUnknown(queryMap);
				pic_notOpt = tjPicService.queryPicNotOpt(queryMap);
//				channel = tjPicService.queryChannel(opt_ren);
				if("qunar".equals(opt_ren)){
					channel = "qunar";
				}else if("damatu".equals(opt_ren)){
					channel = "damatu";
				}else{
					channel = "19e";
				}
				opt_name = tjPicService.queryOptname(opt_ren);
				opt_time = create_time;
				Map<String,Object> addMap = new HashMap<String,Object>();
				addMap.put("create_time", create_time);
				addMap.put("opt_ren", opt_ren);
				addMap.put("pic_count", pic_count);
				addMap.put("pic_success", pic_success);
				addMap.put("pic_fail", pic_fail);
				addMap.put("pic_unkonwn", pic_unkonwn);
				addMap.put("pic_notOpt", pic_notOpt);
				addMap.put("channel", channel);
				addMap.put("opt_name", opt_name);
				addMap.put("opt_time", opt_time);
				
				int todayCount = tjPicService.queryTodayCount(addMap);
				if (todayCount == 0) {
					tjPicService.addTjPic(addMap);
				} else {
					tjPicService.updateTjPic(addMap);
				}
				
			}
			//对于用户名为空（IS NULL）
			Map<String, Object> nullMap = tjPicService.queryNullCodeCount(create_time);
			long nullCount = (Long) nullMap.get("pic_count");
			int pic_unkonwn1 = tjPicService.queryNullAndUnknownCodeCount(create_time);
			int pic_notOpt1 = tjPicService.queryNullAndNotoptCodeCount(create_time);
			//channel = (String)nullMap.get("channel");
			Map<String,Object> addNullMap = new HashMap<String,Object>();
			addNullMap.put("create_time", create_time);
			addNullMap.put("opt_ren", "wuren");
			addNullMap.put("pic_count", nullCount);
			addNullMap.put("pic_success", 0);
			addNullMap.put("pic_fail", 0);
			addNullMap.put("pic_unkonwn", pic_unkonwn1);
			addNullMap.put("pic_notOpt", pic_notOpt1);
			addNullMap.put("channel", "wuren");
			addNullMap.put("opt_name", "无人打码");
			addNullMap.put("opt_time", create_time);
			
			int todayCount = tjPicService.queryTodayCount(addNullMap);
			if (todayCount == 0) {
				tjPicService.addTjPic(addNullMap);
			} else {
				tjPicService.updateTjPic(addNullMap);
			}
			
			logger.info("TjPicJob自动执行JOB完毕");
		}
	}
}
