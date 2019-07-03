package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface UserInfoService {

	/**
	 * 根据用户身份证号码获取用户信息
	 */
	
	List<Map<String,String>> getUserIdsCardInfo(Map<String,String> map);
	
	/**
	 * 添加用户身份证信息表数据
	 * @param map
	 */
	void addUserinfoCheck(Map<String,String> map);
	
	/**
	 * 修改用户身份证信息
	 * @param map
	 * @return
	 */
	int updateUserInfoCheck(Map<String,String> map);
	
	/**
	 * 查询待审核并且修改时间为7天之前的数据
	 */
	List<Map<String,String>> queryUserInfoCheckList();
	
	/**
	 * 处理传递过来的身份证数据，
	 */
	void ProcessingUserCheckData(String passengers);
	
}
