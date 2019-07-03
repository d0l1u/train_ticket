package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.common.LoginUserInfo;

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
	
	/**
	 * 查询代理商常用乘客信息
	 * @param map
	 */
	public List<Map<String,String>> queryLinkInfoList(Map<String,Object> paramMap);
	/**
	 * 查询代理商已有常用乘客数
	 * @param String
	 */
	Integer queryAgentPassTotalNum(String user_id);
	
	/**
	 * 删除代理商仅买过一次车票的指定数量的常用乘客
	 * @param Integer
	 */
	void deleteAgentPass(Map<String,Object> map);
	
	/**
	 * 根据乘客信息查询该乘客是否存在
	 * @param map
	 */
	Integer queryPassNumByCard(Map<String,String> param);
	
	/**
	 * 添加代理商常用乘客信息
	 * @param map
	 */
	void addAgentPassInfo(Map<String,String> param);
	
	void updateAgentPassBuyNum(Map<String,String> param);

	void deleteAgentPassInfo(Map<String, String> param);

	void updateAgentLoginInfo(Map<String, String> map);

	List<Map<String, String>> queryAgentLevelAndId();

	List<Map<String, Object>> queryMayBeWinOrderInfo(Map<String, Object> goldMap);

	void addUserInfo(LoginUserInfo loginUser);

	LoginUserInfo queryUserInfo(String linkPhone);

	void updateUserPwd(Map<String, String> map);

	Map<String, String> queryUserAllInfo(String userId);

	void updateUserInfo(Map<String, String> map);

	List<Map<String, String>> queryPassengerList(Map<String, String> paramMap);

	void addPassenger(Map<String, String> paramMap);

	void deletePassenger(Map<String, String> paramMap);

	Map<String, String> queryPassenger(Map<String, String> paramMap);

	void updatePassenger(Map<String, String> paramMap);

	void addAddress(Map<String, String> paramMap);

	void deleteAddress(Map<String, String> paramMap);

	Map<String, String> queryAddress(Map<String, String> paramMap);

	List<Map<String, String>> queryAddressList(Map<String, String> paramMap);

	int queryAddressListCount(Map<String, String> paramMap);

	void updateAddress(Map<String, String> paramMap);
}
