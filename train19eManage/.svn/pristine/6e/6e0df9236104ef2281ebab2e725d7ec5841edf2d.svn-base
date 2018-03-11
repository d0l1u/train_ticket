package com.l9e.transaction.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.AccounttjDao;
import com.l9e.transaction.service.AccounttjService;
import com.l9e.transaction.vo.AccountStatistics;


@Service("accounttjService")
public class AccounttjServiceImpl implements AccounttjService{
	@Resource
	private AccounttjDao accounttjDao ;

	@Override
	public int queryAccounttjCounts(Map<String, Object> paramMap) {
		return accounttjDao.queryAccounttjCounts(paramMap);
	}

	@Override
	public List<Map<String, String>> queryAccounttjExcel(
			Map<String, Object> paramMap) {
		return accounttjDao.queryAccounttjExcel(paramMap);
	}

	@Override
	public List<Map<String, String>> queryAccounttjList(
			Map<String, Object> paramMap) {
		return accounttjDao.queryAccounttjList(paramMap);
	}

	@Override
	public Integer queryAccountStatisticsTotal(AccountStatistics accountStatistics) {
		return accounttjDao.queryAccountStatisticsTotal(accountStatistics);
	}

	@Override
	public List<AccountStatistics> queryAccountStatistics(AccountStatistics accountStatistics) {
		return accounttjDao.queryAccountStatistics(accountStatistics);
	}

	@Override
	public HashMap<String, String> queryAccountTotals() {
		return accounttjDao.queryAccountTotals();
	}

	@Override
	public Integer queryNewaddAccountTotal(Date date) {
		return accounttjDao.queryNewaddAccountTotal(date);
	}

	@Override
	public Integer queryWhiteListTotal() {
		return accounttjDao.queryWhiteListTotal();
	}

	@Override
	public Integer querySurplusPassengerTotal() {
		return accounttjDao.querySurplusPassengerTotal();
	}

	@Override
	public Integer queryTicketTotal(Date date) {
		return accounttjDao.queryTicketTotal(date);
	}

	@Override
	public Integer queryMatchWhiteListTotal(Date date) {
		return accounttjDao.queryMatchWhiteListTotal(date);
	}

	@Override
	public Integer queryAccountStopTotal(Date date) {
		return accounttjDao.queryAccountStopTotal(date);
	}

	@Override
	public Integer queryAccountOfUpperlimit(Date date) {
		return accounttjDao.queryAccountOfUpperlimit(date);
	}

	@Override
	public Integer queryAccountOfCheckUser(Date date) {
		return accounttjDao.queryAccountOfCheckUser(date);
	}

	@Override
	public Integer queryAccountOfCheckPhone(Date date) {
		return accounttjDao.queryAccountOfCheckPhone(date);
	}

	@Override
	public Integer queryTicketX(Date date, int x) {
		return accounttjDao.queryTicketX(date,x);
	}

	@Override
	public void insertStatistics(AccountStatistics statistics) {
		accounttjDao.insertStatistics(statistics);
	}

	@Override
	public Integer queryAddWhiteListTotal(Date date) {
		return accounttjDao.queryAddWhiteListTotal(date);
	}

	
}
