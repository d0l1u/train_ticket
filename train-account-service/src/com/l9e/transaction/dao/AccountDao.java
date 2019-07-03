package com.l9e.transaction.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.Account;

/**
 * 12306账号持久接口
 * @author licheng
 *
 */
public interface AccountDao {

	/**
	 * 查询一个12306账号
	 * @param params
	 * @return
	 */
	Account selectOneAccount(Map<String, Object> params);
	
	/**
	 * 查询联系人个数小于10的12306账号
	 * @param params
	 * @return
	 */
	List<Account> selectLess10Accounts(Map<String, Object> params);
	
	/**
	 * 查询历史账号
	 * @param passportNo
	 * @return
	 */
	Integer selectFilterAccount(String passportNo);
	
	/**
	 * 查询历史账号列表(换成cp_pass_whitelist白名单表）
	 * @param params
	 * @return
	 */
	//Integer selectFilterAccountList(String passportNo);
	List<Account> selectFilterAccountList(List<String> params);
	
	/**
	 * 查询账号
	 * @param params
	 * @return
	 */
	List<Account> selectAccounts(Map<String, Object> params);
	
	/**
	 * 更新账号信息
	 * @param account
	 */
	int updateAccount(Account account);
	
	/**
	 * 白名单更改账号表信息
	 * @param params
	 */
	int updateAccountInfo(Map<String, Object> params);

	//查询匹配的证件号码
	List<String> queryCertnoList(Map<String, Object> certMap);

	//插入cp_match表一条记录
	void insertCpMatch(Map<String, Object> paramMap);
	
	/**
	 * 根据账号名称查询一个12306账号
	 * @param params
	 * @return
	 */
	Account selectAccountByName(Map<String, Object> paramMap);
	
	/**
	 * 自带账号订单改签退票时，实时更新账号密码
	 * @param params
	 */
	int updateAccountPwd(Map<String, Object> paramMap);

	 /**   
	 * @Title: reset2Free   
	 * @Description: TODO  
	 * @author: taokai
	 * @date: 2017年8月10日 下午3:44:43
	 * @return int
	 */
	int reset2Free();
}
