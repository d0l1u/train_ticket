package com.l9e.transaction.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.UserStatToUserinfoService;
/**
 * 
 * @author liht
 *
 */
@Component("userStatToUserinfo")
public class UserStatToUserinfo {
	private static final Logger logger = Logger.getLogger(UserStatToUserinfo.class);
	@Resource
	UserStatToUserinfoService userStatToUserinfoService; 
	
	//统计上月和本月销售金额
	public void queryOrUpdateAndInsertUser(){
		System.out.println("*****************************************************************");
		//取出列表
		List<Map<String,String>>statList= userStatToUserinfoService.queryStatList();
		
		for(int i =0;i<statList.size();i++){
			String queryUser_id = statList.get(i).get("user_id");
			String userList = userStatToUserinfoService.queryUser(queryUser_id);
			if(userList==null){
				userStatToUserinfoService.addUserStat(statList.get(i));
			}else{
				userStatToUserinfoService.updateUserStat(statList.get(i));
			}
		}
	}

}
