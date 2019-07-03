package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.AccountDao;
import com.l9e.transaction.service.AccountService;
import com.l9e.transaction.vo.Account;
import com.l9e.transaction.vo.AccountFilter;
import com.l9e.transaction.vo.Passenger;

@Service("accountService")
public class AccountServiceImpl implements AccountService {

	private static final Logger logger = Logger.getLogger(AccountServiceImpl.class);

	@Resource
	private AccountDao accountDao;

	/**
	 * 获取核验用的账号
	 */
	@Override
	public Account queryAccountForVerify(Map<String, Object> paramMap) {
		Account account = accountDao.queryAccountForVerify(paramMap);
		for (int i = 0; i < 5; i++) {
			if (account != null) {// 获取账号成功
				break;
			}
		}
		if (account != null) {// 获取账号成功
			Map<String, String> beginMap = new HashMap<String, String>();
			beginMap.put("acc_id", account.getAccId());
			beginMap.put("acc_status", Account.VERIFY);
			beginMap.put("old_status", Account.FREE);
			int count = accountDao.udpateAccountStatusByAccId(beginMap);
			if (count == 0) {// 更新账号失败
				logger.error("更新账号开始核验失败，accUsername=" + account.getAccUsername());
				return null;
			}
		}
		return account;
	}

	/**
	 * 查询账号是否在过滤表中存在
	 */
	@Override
	public boolean queryAccoutsDBExist(List<Passenger> userList) {
		Set<String> container = new HashSet<String>();
		String element = null;
		for (Passenger p : userList) {
			element = p.getUser_name() + "/" + p.getCert_no();
			if (!container.contains(element)) {
				container.add(element);
			}
		}
		int count = accountDao.queryAccoutExistInFilter(userList);

		logger.info("db verify count=" + count + ":" + container.size());
		if (count == container.size()) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public int updateAccoutFree(Account account) {
		Map<String, String> endMap = new HashMap<String, String>();
		endMap.put("acc_id", account.getAccId());
		endMap.put("acc_status", Account.FREE);
		endMap.put("old_status", Account.VERIFY);
		int count = accountDao.udpateAccountStatusByAccId(endMap);
		return count;
	}

	@Override
	public void addUserToAccountFilter(List<AccountFilter> afs) {

		int count = 0;
		for (AccountFilter af : afs) {
			count = accountDao.queryAccountFilterCountById(af.getIds_card());
			if (count == 0) {// 不存在则保存到数据库中
				accountDao.addUserToAccountFilter(af);
				logger.info("passenger :" + af.getReal_name() + "(" + af.getIds_card() + ") save into DB!");
			} else {
				logger.info("passenger :" + af.getReal_name() + "(" + af.getIds_card() + ") is already in DB!");
			}
		}
	}

	@Override
	public List<Account> queryAliveAccouts(Map<String, Object> map) {
		List<Account> accounts = accountDao.queryAliveAccouts(map);
		if (accounts != null && accounts.size() > 0) {
			accountDao.updateAccoutLivetimeBatch(accounts);
		}
		return accounts;
	}

	@Override
	public int queryAliveAccoutsCount(String channel) {
		return accountDao.queryAliveAccoutsCount(channel);
	}

	@Override
	public void updateSupplyAccountNum(Map<String, Object> paramMap) {
		List<Account> accounts = accountDao.querySupplyAccouts(paramMap);
		if (accounts != null && accounts.size() > 0) {
			int count = accountDao.updateSupplyAccoutsAlive(accounts);
			logger.info("渠道:" + paramMap.get("channel") + "成功分配待唤醒" + count + "个账号！");
		}
	}

	@Override
	public int updateLimitAccountStop(Account account) {
		return accountDao.updateLimitAccountStop(account);
	}

	@Override
	public void updateWorkerinfoNum() {
		accountDao.updateWorkerinfoNum();
		logger.info("成功清空机器人处理订单数量");
	}

	@Override
	public String queryOrderStatus(String order_id) {
		return accountDao.queryOrderStatus(order_id);
	}

	@Override
	public void updateCPOrderStatusAndMoney(Map<String, Object> paramMap) {
		accountDao.updateCPOrderStatusAndMoney(paramMap);
	}

	@Override
	public void updateAccountActive(Map<String, Object> map) {
		accountDao.updateAccountActive(map);
	}

	@Override
	public Map<String, String> queryAccountByName(Map<String, Object> map) {
		return accountDao.queryAccountByName(map);
	}

	@Override
	public int queryAccoutsCount(Map<String, Object> map) {
		return accountDao.queryAccoutsCount(map);
	}

	@Override
	public List<Account> queryAccount(Map<String, Object> map) {
		List<Account> accountList = accountDao.queryAccount(map);
		if (map.get("modify_status") != null && ((String) map.get("modify_status")).length() > 0) {
			for (Account account : accountList) {
				if (account.getOldPass() == null || account.getOldPass().length() == 0) {
					account.setOldPass(account.getAccPassword());
				}
				account.setModifyStatus("01"); // 将账号更改状态变为准备更改
				if (account.getAccId() != null || account.getAccId().length() > 0) {
					updateModifyStatus(account);
				}
			}
		}
		return accountList;
	}

	public int updateAccount(Account account) {
		return accountDao.updateAccount(account);
	}

	@Override
	public void updateModifyStatus(Account account) {
		accountDao.updateModifyStatus(account);
	}

	@Override
	public int updateBookNum(Map<String, Object> map) {
		return accountDao.updateBookNum(map);
	}

	@Override
	public List<Map<String, String>> queryAccoutPriority(Map<String, Object> map) {
		return accountDao.queryAccoutPriority(map);
	}

	@Override
	public void updateAccountPriority(Map<String, String> map) {
		accountDao.updateAccountPriority(map);
	}

	@Override
	public List<Map<String, String>> queryAccoutWaitList(Map<String, Object> map) {
		return accountDao.queryAccoutWaitList(map);
	}

	@Override
	public void updateAccountStatus(Map<String, String> map) {
		accountDao.updateAccountStatus(map);
	}

	@Override
	public int openAccountByAccId(String accId) {
		return accountDao.openAccountByAccId(accId);
	}

	@Override
	public Map<String, String> querySettingMap() {
		return accountDao.querySettingMap();
	}

	@Override
	public List<String> queryWaitOpenAccountList(Map<String, Object> setMap) {
		return accountDao.queryWaitOpenAccountList(setMap);
	}

	@Override
	public void addAccWhiteInfo(Map<String, Object> updateMap) {
		accountDao.addAccWhiteInfo(updateMap);
	}

	@Override
	public List<Map<String, Object>> queryAccInfoList(Map<String, Object> paramMap) {
		return accountDao.queryAccInfoList(paramMap);
	}

	@Override
	public int updateAccInfo(Map<String, Object> updateMap) {
		return accountDao.updateAccInfo(updateMap);
	}

	@Override
	public void updateAccWhiteInfo(Map<String, Object> updatWhiteMap) {
		accountDao.updateAccWhiteInfo(updatWhiteMap);
	}

	@Override
	public Map<String, Object> queryAccInfoIsExist(Map<String, Object> paramMap) {
		return accountDao.queryAccInfoIsExist(paramMap);
	}

	@Override
	public void addAccIncreInfo(Map<String, Object> addIncreMap) {
		accountDao.addAccIncreInfo(addIncreMap);
	}

	@Override
	public Map<String, Object> queryAccIncreInfo(Map<String, Object> paramMap) {
		return accountDao.queryAccIncreInfo(paramMap);
	}

	@Override
	public void updateAccIncreInfo(Map<String, Object> updatWhiteMap) {
		accountDao.updateAccIncreInfo(updatWhiteMap);
	}

	@Override
	public void updateAccountInfo(Map<String, Object> updateMap) {
		accountDao.updateAccountInfo(updateMap);
	}

	@Override
	public Account selectJdAccount(String status) {
		return accountDao.selectJdAccount(status);
	}

	@Override
	public void updateJdAccount(Integer accountId, String status) {
		accountDao.updateJdAccount(accountId, status);
	}

	@Override
	public void disableJdAccount(Integer accountId, String reason) {
		accountDao.disableJdAccount(accountId, reason);
	}

	@Override
	public void deleteWhiteListByAccount(Integer accountId) {
		accountDao.deleteWhiteListByAccount(accountId);
	}

	@Override
	public Integer deleteWhiteList(Integer accountId, List<String> cardNoList) {
		return accountDao.deleteWhiteList(accountId, cardNoList);
	}
}
