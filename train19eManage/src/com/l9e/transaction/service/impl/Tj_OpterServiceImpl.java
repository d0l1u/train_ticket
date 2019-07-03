package com.l9e.transaction.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.Tj_OpterDao;
import com.l9e.transaction.service.Tj_OpterService;
import com.l9e.util.CreateIDUtil;

@Service("tj_OpterService")
public class Tj_OpterServiceImpl implements Tj_OpterService{
	@Resource
	private Tj_OpterDao tj_OpterDao;
	private static final Logger logger = Logger.getLogger(Tj_OpterServiceImpl.class);
	
	public void operate(Map<String ,Object> map){
		if(!StringUtils.isEmpty(String.valueOf(map.get("userName"))) 
				&&!StringUtils.isEmpty(String.valueOf(map.get("channel")))
				&&!StringUtils.isEmpty(String.valueOf(map.get("type")))){
			logger.info("客服操作记录:"+map.get("userName")+","+map.get("channel")+","+map.get("type"));
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, 0);    //得到当天日期
			Date date = calendar.getTime();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String tj_time = df.format(date);
			map.put("tj_time", tj_time);
			List<String> opter_List = tj_OpterDao.queryAllOpter();//查询所有的高级用户
//			opter_List.add("张俊驰");
//			System.out.println(opter_List.toString());
//			System.out.println("opter_List.contains(map.get(userName))"+opter_List.contains(map.get("userName")));
			if( opter_List.contains(map.get("userName"))){//用户名不为空，且在高级用户列表里
				int countToday = tj_OpterDao.queryCountTodayByName(map);//查询是否有今天记录
				if(countToday == 0 ){
					String tj_id = CreateIDUtil.createID("TJ");
					map.put("tj_id", tj_id);
					try{
						tj_OpterDao.addNumTodayByName(map);//没有记录则添加
						logger.info("添加一条记录："+
								"tj_time="+tj_time+" opt_person="+map.get("userName") +"refund_total_channel="+map.get("channel")
								+"refund_total_type="+map.get("type")+"tj_id="+tj_id);
					}catch (Exception e) {
						logger.error("【添加】考核统计记录失败！"+e);
					}
					
				}else{
					try{
						tj_OpterDao.updateNumTodayByName(map);//有记录则在原来基础上+1
						logger.info("修改考核记录 +1："+
								"tj_time="+tj_time+" opt_person="+map.get("userName") +"refund_total_channel="+map.get("channel")
								+"refund_total_type="+map.get("type"));
					}catch (Exception e) {
						logger.error("【修改】考核统计记录失败！"+e);
					}
				}
			}
		}
	}
}
