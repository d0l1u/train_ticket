package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.UserInfoDao;
import com.l9e.transaction.service.UserInfoService;

@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

	private static final Logger logger = Logger.getLogger(UserInfoServiceImpl.class);
	@Resource
	private UserInfoDao userInfoDao;

	public List<Map<String, String>> getUserIdsCardInfo(Map<String,String> queryMap) {
		 return userInfoDao.getUserIdsCardInfo(queryMap);
	}

	public void addUserinfoCheck(Map<String, String> map) {
		userInfoDao.addUserinfoCheck(map);
		
	}

	public int updateUserInfoCheck(Map<String, String> map) {
		return userInfoDao.updateUserInfoCheck(map);
	}

	public void ProcessingUserCheckData(String passengers) {
		try {
			String param[]=passengers.split("\\#");
			if(param!=null && param.length>0){
				for(int i=0;i<param.length;i++){
					String userInfos[]=param[i].split("\\|");
					if(userInfos.length==4){
						String userName=userInfos[1];
						String idsCard=userInfos[2];
						String newStatus=userInfos[3];
						if(StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(idsCard) && StringUtils.isNotEmpty(newStatus)){
							Map<String,String> queryMap=new HashMap<String,String>();
							queryMap.put("user_name", userName);
							queryMap.put("ids_card", idsCard);
							List<Map<String,String>> userInfoList= userInfoDao.getUserIdsCardInfo(queryMap);
							if(userInfoList!=null && userInfoList.size()>0){
								String oldStatus=userInfoList.get(0).get("status");
								if("0".equals(oldStatus) || "1".equals(oldStatus) ){
									Map<String,String> updateUserInfoMap=new HashMap<String,String>();
									updateUserInfoMap.put("ids_card", idsCard);
									updateUserInfoMap.put("user_name", userName);
									updateUserInfoMap.put("status", newStatus);
									userInfoDao.updateUserInfoCheck(updateUserInfoMap);
								}
							}else{
								Map<String,String> userInfoMap=new HashMap<String,String>();
								userInfoMap.put("user_name", userName);
								userInfoMap.put("ids_card", idsCard);
								userInfoMap.put("status", newStatus);
								userInfoDao.addUserinfoCheck(userInfoMap);
							}
						}else{
							continue;
						}
					}else{
						continue;
					}
				}
			}else{
				logger.error("分割过后的身份证信息错误！");
				return;
			}
		} catch (Exception e) {
			logger.error("处理身份证信息出现异常！");
			return;
		}
	}
	
}
