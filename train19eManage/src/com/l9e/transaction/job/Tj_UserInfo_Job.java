package com.l9e.transaction.job;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;


import com.l9e.transaction.service.Tj_UserInfoService;
import com.l9e.util.SwitchUtils;

/**
 * 用户量统计自动执行类
 * @author Liht
 *
 */
@Component("tj_userInfo_job")
public class Tj_UserInfo_Job {
	private static final Logger logger = Logger.getLogger(Tj_UserInfo_Job.class);
	@Resource
	private Tj_UserInfoService tj_UserInfoService;
	
	public void queryTj_UserInfo(){
		int table_count = tj_UserInfoService.queryTable_count();
		List<Map<String,String>>area_List = tj_UserInfoService.queryAllArea_nameAndArea_no();
		if(table_count==0){
			logger.info("用户量统计自动执行类，表中为"+table_count+"条数据,开始第一次执行Job");
			//查询全国的各个省名和NO
			List<Map<String,String>>date_List = tj_UserInfoService.queryAll_date();
			for(Map<String,String>area_Map : area_List){
				String area_name=area_Map.get("area_name");
				String area_no=area_Map.get("area_no");
				for(int i=0;i<date_List.size()-1;i++){
					Map<String,String>date_Map = date_List.get(i);
					Map<String,String>query_Map = new HashMap<String,String>();
					String tj_time = date_Map.get("apply_time");
					query_Map.put("apply_time", tj_time);
					query_Map.put("area_no", area_no);
					
					int user_total = tj_UserInfoService.queryUser_total(query_Map);//本省总用户数
					String date_add = SwitchUtils.dateSub(tj_time, 1);
					query_Map.put("date_add", date_add);
					int user_increase =user_total-tj_UserInfoService.queryUser_total_Add(query_Map);//用户增长是数
					
					int active_user = tj_UserInfoService.queryActiveUserNow(query_Map);//当天活跃用户数
					int active_user_total = tj_UserInfoService.queryActiveUserAll(query_Map);//查询总活跃数
					int active_increase = active_user-tj_UserInfoService.queryActiveUser_Add(query_Map);//查询活跃用户增长数
					double user_rate_increase;double active_rate_increase;
					if(user_total==0){
						user_rate_increase=0;
					}else{
						user_rate_increase = (double)user_increase/(double)user_total;//用户增长率
					}
					if(active_user_total==0){
						active_rate_increase=0;
					}else{
						active_rate_increase =(double)active_increase/(double)active_user_total;//活跃用户增长率
					}
					
					Map<String,Object>add_Map = new HashMap<String,Object>();
					add_Map.put("area_no", area_no);
					add_Map.put("area_name", area_name);
					add_Map.put("tj_time", tj_time);
					add_Map.put("user_total", user_total);
					add_Map.put("user_increase", user_increase);
					add_Map.put("user_rate_increase", user_rate_increase);
					add_Map.put("active_user", active_user);
					add_Map.put("active_user_total", active_user_total);
					add_Map.put("active_increase", active_increase);
					add_Map.put("active_increase", active_increase);
					add_Map.put("active_rate_increase", active_rate_increase);
					tj_UserInfoService.addTj_User(add_Map);
				}
			}
		}else{
			for(Map<String,String>area_Map : area_List){
				String area_name=area_Map.get("area_name");
				String area_no=area_Map.get("area_no");
				String apply_time = SwitchUtils.getPreDate();
				Map<String,String>query_Map = new HashMap<String,String>();
				query_Map.put("area_name", area_name);
				query_Map.put("area_no", area_no);
				query_Map.put("apply_time", apply_time);
				
				int user_total = tj_UserInfoService.queryUser_total(query_Map);//本省总用户数
				String date_add = SwitchUtils.dateSub(apply_time, 1);
				query_Map.put("date_add", date_add);
				int user_increase =user_total-tj_UserInfoService.queryUser_total_Add(query_Map);//用户增长是数
				int active_user = tj_UserInfoService.queryActiveUserNow(query_Map);//当天活跃用户数
				int active_user_total = tj_UserInfoService.queryActiveUserAll(query_Map);//查询总活跃数
				int active_increase = active_user-tj_UserInfoService.queryActiveUser_Add(query_Map);//查询活跃用户增长数
				
				double user_rate_increase;double active_rate_increase;
				if(user_total==0){
					user_rate_increase=0;
				}else{
					user_rate_increase = (double)user_increase/(double)user_total;//用户增长率
				}
				if(active_user_total==0){
					active_rate_increase=0;
				}else{
					active_rate_increase =(double)active_increase/(double)active_user_total;//活跃用户增长率
				}
				Map<String,Object>add_Map = new HashMap<String,Object>();
				add_Map.put("area_no", area_no);
				add_Map.put("area_name", area_name);
				add_Map.put("tj_time", apply_time);
				add_Map.put("user_total", user_total);
				add_Map.put("user_increase", user_increase);
				add_Map.put("user_rate_increase", user_rate_increase);
				add_Map.put("active_user", active_user);
				add_Map.put("active_user_total", active_user_total);
				add_Map.put("active_increase", active_increase);
				add_Map.put("active_rate_increase", active_rate_increase);
				tj_UserInfoService.addTj_User(add_Map);
			}
		}
	}
}
