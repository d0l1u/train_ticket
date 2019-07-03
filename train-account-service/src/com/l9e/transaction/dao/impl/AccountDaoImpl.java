package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.AccountDao;
import com.l9e.transaction.vo.Account;

@Repository("accountDao")
public class AccountDaoImpl extends BaseDao implements AccountDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Account> selectAccounts(Map<String, Object> params) {
		return getSqlMapClientTemplate().queryForList("account.selectAccount", params);
	}

	@Override
	public Integer selectFilterAccount(String passportNo) {
		return (Integer) getSqlMapClientTemplate().queryForObject("account.selectFilterId", passportNo);
	}

	@Override
	public Account selectOneAccount(Map<String, Object> params) {
		return (Account) getSqlMapClientTemplate().queryForObject("account.selectAccount", params);
	}

	@Override
	public int updateAccount(Account account) {
		return getSqlMapClientTemplate().update("account.updateAccount", account);
	}

	/**
	 * 查询历史账号列表(换成cp_pass_whitelist白名单表）
	 * 
	 * @param params
	 * @return
	 */
	// @Override
	// public List<Account> selectFilterAccountList(String passportNo) {
	// // TODO Auto-generated method stub
	// return
	// getSqlMapClientTemplate().queryForObject("account.selectFilterIdList",
	// passportNo);
	// }

	@Override
	public List<Account> selectFilterAccountList(List<String> params) {
		// TODO Auto-generated method stub
		return getSqlMapClientTemplate().queryForList("account.selectFilterIdList", params);
	}

	/**
	 * 白名单更改账号表信息
	 * 
	 * @param params
	 */
	@Override
	public int updateAccountInfo(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return getSqlMapClientTemplate().update("account.updateAccountInfo", params);
	}

	@Override
	public void insertCpMatch(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().insert("account.insertCpMatch", paramMap);
	}

	@Override
	public List<String> queryCertnoList(Map<String, Object> certMap) {
		return this.getSqlMapClientTemplate().queryForList("account.queryCertnoList", certMap);
	}

	@Override
	public Account selectAccountByName(Map<String, Object> paramMap) {
		return (Account) this.getSqlMapClientTemplate().queryForObject("account.selectAccountByName", paramMap);
	}

	@Override
	public int updateAccountPwd(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().update("account.updateAccountPwd", paramMap);
	}

	/**
	 * 查询联系人个数小于10的12306账号
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public List<Account> selectLess10Accounts(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return getSqlMapClientTemplate().queryForList("account.selectLess10Account", params);
	}

	@Override
	public int reset2Free() {
		return getSqlMapClientTemplate().update("account.reset2Free");
	}

}
