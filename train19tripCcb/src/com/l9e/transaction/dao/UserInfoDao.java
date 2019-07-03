package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface UserInfoDao {
	
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
	 * 修改代理商的联系人状态
	 * @param map
	 * @return
	 */
	int updateHcUserRegistStatus(Map<String,String> map);
	
	
	/**
	 * 查询出代理商的id和代理商已通过联系人的数量
	 */
	List<Map<String,Object>> queryAgentPassNumAndId();
	
	/**
	 * 根据代理商id的list和订单金额查询订单信息
	 */
	List<Map<String,Object>> queryAgentOrderInfo(Map<String,Object> map);
	
	/**
	 * 添加代理商中奖信息
	 */
	void addAgentwinning(Map<String,Object> agentWinningMap);
	
	/**
	 * 根据代理商id和中奖代理商类型查询其当天是否已经中奖
	 */ 
	List<Map<String,Object>> queryAgentWinningDay(Map<String,Object> map); 
	
	/**
	 * 查询代理商7天之内中奖信息
	 */
	List<Map<String,Object>> queryAgentWinningInfo();
	
	/**
	 * 根据代理商id查询是否是第一次进入引导页面
	 */
	List<Map<String,String>> queryAgentLogin(String agentId);
	
	/**
	 * 如果代理商是第一次登陆 则添加引导表信息
	 * @param map
	 */
	void addAgentLoginInfo (Map<String,String> map);

	void addAgentPassInfo(Map<String, String> param);

	void deleteAgentPass(Map<String, Object> map);

	void deleteAgentPassInfo(Map<String, String> param);

	Integer queryAgentPassTotalNum(String userId);

	List<Map<String, String>> queryLinkInfoList(Map<String, Object> paramMap);

	Integer queryPassNumByCard(Map<String, String> param);
	
	
}
