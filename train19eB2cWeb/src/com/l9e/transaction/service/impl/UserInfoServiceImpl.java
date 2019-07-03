package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.common.LoginUserInfo;
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

	public List<Map<String, String>> queryUserInfoCheckList() {
		return userInfoDao.queryUserInfoCheckList();
	}

	
	public void ProcessingUserCheckData(String passengers) {
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
								logger.info(userInfoMap.get("user_name"));
								logger.info(userInfoMap.get("ids_card"));
								logger.info(userInfoMap.get("user_name"));
								userInfoDao.addUserinfoCheck(userInfoMap);
							}
						}else{
							continue;
						}
					}else{
						continue;
					}
				}
			}
		}

	
	public int updateHcUserRegistStatus(Map<String, String> map) {
		return userInfoDao.updateHcUserRegistStatus(map);
	}

	
	public List<Map<String, Object>> queryAgentPassNumAndId() {
		return userInfoDao.queryAgentPassNumAndId();
	}

	
	public List<Map<String, Object>> queryAgentOrderInfo(Map<String, Object> map) {
		return userInfoDao.queryAgentOrderInfo(map);
	}

	
	public void addAgentwinning(Map<String, Object> agentWinningMap) {
		userInfoDao.addAgentwinning(agentWinningMap);
	}

	
	public List<Map<String, Object>> queryAgentWinningDay(
			Map<String, Object> map) {
		return userInfoDao.queryAgentWinningDay(map);
	}

	
	public List<Map<String, Object>> queryAgentWinningInfo() {
		return userInfoDao.queryAgentWinningInfo();
	}

	
	public void addAgentLoginInfo(Map<String, String> map) {
		userInfoDao.addAgentLoginInfo(map);
	}

	
	public List<Map<String, String>> queryAgentLogin(String agentId) {
		return userInfoDao.queryAgentLogin(agentId);
	}

	
	public void addAgentPassInfo(Map<String, String> param) {
		userInfoDao.addAgentPassInfo(param);
	}

	
	public void deleteAgentPass(Map<String,Object> map) {
		userInfoDao.deleteAgentPass(map);
	}

	
	public Integer queryAgentPassTotalNum(String user_id) {
		return userInfoDao.queryAgentPassTotalNum(user_id);
	}

	
	public Integer queryPassNumByCard(Map<String, String> param) {
		return userInfoDao.queryPassNumByCard(param);
	}

	
	public List<Map<String, String>> queryLinkInfoList(
			Map<String, Object> paramMap) {
		return userInfoDao.queryLinkInfoList(paramMap);
	}

	
	public void updateAgentPassBuyNum(Map<String, String> param) {
		userInfoDao.updateAgentPassBuyNum(param);
	}

	
	public void deleteAgentPassInfo(Map<String, String> param) {
		userInfoDao.deleteAgentPassInfo(param);
	}

	
	public void updateAgentLoginInfo(Map<String, String> map) {
		userInfoDao.updateAgentLoginInfo(map);
	}

	
	public List<Map<String, String>> queryAgentLevelAndId() {
		return userInfoDao.queryAgentLevelAndId();
	}

	
	public List<Map<String, Object>> queryMayBeWinOrderInfo(
			Map<String, Object> goldMap) {
		return userInfoDao.queryMayBeWinOrderInfo(goldMap);
	}


	public void addUserInfo(LoginUserInfo loginUser) {
		userInfoDao.addUserInfo(loginUser);
	}


	public LoginUserInfo queryUserInfo(String linkPhone) {
		return userInfoDao.queryUserInfo(linkPhone);
	}

	public void updateUserPwd(Map<String, String> map) {
		userInfoDao.updateUserPwd(map);
	}

	public Map<String, String> queryUserAllInfo(String userId) {
		return userInfoDao.queryUserAllInfo(userId);
	}

	public void updateUserInfo(Map<String, String> map) {
		userInfoDao.updateUserInfo(map);
	}

	public List<Map<String, String>> queryPassengerList(
			Map<String, String> paramMap) {
		return userInfoDao.queryPassengerList(paramMap);
	}

	public void addPassenger(Map<String, String> paramMap) {
		userInfoDao.addPassenger(paramMap);
	}

	public void deletePassenger(Map<String, String> paramMap) {
		userInfoDao.deletePassenger(paramMap);
	}

	public Map<String, String> queryPassenger(Map<String, String> paramMap) {
		return userInfoDao.queryPassenger(paramMap);
	}

	public void updatePassenger(Map<String, String> paramMap) {
		userInfoDao.updatePassenger(paramMap);
	}

	public void addAddress(Map<String, String> paramMap) {
		userInfoDao.addAddress(paramMap);
	}

	public void deleteAddress(Map<String, String> paramMap) {
		userInfoDao.deleteAddress(paramMap);
	}

	public Map<String, String> queryAddress(Map<String, String> paramMap) {
		return userInfoDao.queryAddress(paramMap);
	}

	public List<Map<String, String>> queryAddressList(
			Map<String, String> paramMap) {
		return userInfoDao.queryAddressList(paramMap);
	}

	public int queryAddressListCount(Map<String, String> paramMap) {
		return userInfoDao.queryAddressListCount(paramMap);
	}

	public void updateAddress(Map<String, String> paramMap) {
		userInfoDao.updateAddress(paramMap);
	}
	
}
