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

	@Override
	public void addUserinfoCheck(Map<String, String> map) {
		userInfoDao.addUserinfoCheck(map);
		
	}

	@Override
	public int updateUserInfoCheck(Map<String, String> map) {
		return userInfoDao.updateUserInfoCheck(map);
	}

	@Override
	public List<Map<String, String>> queryUserInfoCheckList() {
		return userInfoDao.queryUserInfoCheckList();
	}

	@Override
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

	@Override
	public int updateHcUserRegistStatus(Map<String, String> map) {
		return userInfoDao.updateHcUserRegistStatus(map);
	}

	@Override
	public List<Map<String, Object>> queryAgentPassNumAndId() {
		return userInfoDao.queryAgentPassNumAndId();
	}

	@Override
	public List<Map<String, Object>> queryAgentOrderInfo(Map<String, Object> map) {
		return userInfoDao.queryAgentOrderInfo(map);
	}

	@Override
	public void addAgentwinning(Map<String, Object> agentWinningMap) {
		userInfoDao.addAgentwinning(agentWinningMap);
	}

	@Override
	public List<Map<String, Object>> queryAgentWinningDay(
			Map<String, Object> map) {
		return userInfoDao.queryAgentWinningDay(map);
	}

	@Override
	public List<Map<String, Object>> queryAgentWinningInfo(String agent_type) {
		return userInfoDao.queryAgentWinningInfo(agent_type);
	}

	@Override
	public void addAgentLoginInfo(Map<String, String> map) {
		userInfoDao.addAgentLoginInfo(map);
	}

	@Override
	public List<Map<String, String>> queryAgentLogin(String agentId) {
		return userInfoDao.queryAgentLogin(agentId);
	}

	@Override
	public void addAgentPassInfo(Map<String, String> param) {
		userInfoDao.addAgentPassInfo(param);
	}

	@Override
	public void deleteAgentPass(Map<String,Object> map) {
		userInfoDao.deleteAgentPass(map);
	}

	@Override
	public Integer queryAgentPassTotalNum(String user_id) {
		return userInfoDao.queryAgentPassTotalNum(user_id);
	}

	@Override
	public Integer queryPassNumByCard(Map<String, String> param) {
		return userInfoDao.queryPassNumByCard(param);
	}

	@Override
	public List<Map<String, String>> queryLinkInfoList(
			Map<String, Object> paramMap) {
		return userInfoDao.queryLinkInfoList(paramMap);
	}

	@Override
	public void updateAgentPassBuyNum(Map<String, String> param) {
		userInfoDao.updateAgentPassBuyNum(param);
	}

	@Override
	public void deleteAgentPassInfo(Map<String, String> param) {
		userInfoDao.deleteAgentPassInfo(param);
	}

	@Override
	public void updateAgentLoginInfo(Map<String, String> map) {
		userInfoDao.updateAgentLoginInfo(map);
	}

	@Override
	public List<Map<String, String>> queryAgentLevelAndId() {
		return userInfoDao.queryAgentLevelAndId();
	}

	@Override
	public List<Map<String, Object>> queryMayBeWinOrderInfo(
			Map<String, Object> goldMap) {
		return userInfoDao.queryMayBeWinOrderInfo(goldMap);
	}
	
}
