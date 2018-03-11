 package com.l9e.transaction.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.AccounttjDao;
import com.l9e.transaction.vo.AccountStatistics;

@Repository("accounttjDao")
public class AccounttjDaoImpl extends BaseDao implements AccounttjDao {

	@Override
	public int queryAccounttjCounts(Map<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("accounttj.queryAccounttjCounts", paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAccounttjExcel(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("accounttj.queryAccounttjExcel", paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAccounttjList(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("accounttj.queryAccounttjList", paramMap);
	}

	@Override
	public Integer queryAccountStatisticsTotal(AccountStatistics accountStatistics) {
		Object object = this.getSqlMapClientTemplate().queryForObject("accountStatistics.queryAccountStatisticsTotal", accountStatistics);
		Integer total = object == null ? 0 : (Integer) object;
		return total;
	}

	@Override
	public List<AccountStatistics> queryAccountStatistics(AccountStatistics accountStatistics) {
		return this.getSqlMapClientTemplate().queryForList("accountStatistics.queryAccountStatistics",accountStatistics);
	}

	@Override
	public HashMap<String, String> queryAccountTotals() {
		return (HashMap<String, String>) this.getSqlMapClientTemplate().queryForObject("accountStatistics.queryAccountTotals");
	}

	@Override
	public Integer queryNewaddAccountTotal(Date date) {
		Object obj = this.getSqlMapClientTemplate().queryForObject("accountStatistics.queryNewaddAccountTotal",date);
		return obj == null?0:(Integer)obj;
	}

	@Override
	public Integer queryWhiteListTotal() {
		Object obj = this.getSqlMapClientTemplate().queryForObject("accountStatistics.queryWhiteListTotal");
		return obj == null?0:(Integer)obj;
	}

	@Override
	public Integer querySurplusPassengerTotal() {
		Object obj = this.getSqlMapClientTemplate().queryForObject("accountStatistics.querySurplusPassengerTotal");
		return obj == null?0:(Integer)obj;
	}

	@Override
	public Integer queryTicketTotal(Date date) {
		Object obj = this.getSqlMapClientTemplate().queryForObject("accountStatistics.queryTicketTotal",date);
		return obj == null?0:(Integer)obj;
	}

	@Override
	public Integer queryMatchWhiteListTotal(Date date) {
		Object obj = this.getSqlMapClientTemplate().queryForObject("accountStatistics.queryMatchWhiteListTotal",date);
		return obj == null?0:(Integer)obj;
	}

	@Override
	public Integer queryAccountStopTotal(Date date) {
		Object obj = this.getSqlMapClientTemplate().queryForObject("accountStatistics.queryAccountStopTotal",date);
		return obj == null?0:(Integer)obj;
	}

	@Override
	public Integer queryAccountOfUpperlimit(Date date) {
		Object obj = this.getSqlMapClientTemplate().queryForObject("accountStatistics.queryAccountOfUpperlimit",date);
		return obj == null?0:(Integer)obj;
	}

	@Override
	public Integer queryAccountOfCheckUser(Date date) {
		Object obj = this.getSqlMapClientTemplate().queryForObject("accountStatistics.queryAccountOfCheckUser",date);
		return obj == null?0:(Integer)obj;
	}

	@Override
	public Integer queryAccountOfCheckPhone(Date date) {
		Object obj = this.getSqlMapClientTemplate().queryForObject("accountStatistics.queryAccountOfCheckPhone",date);
		return obj == null?0:(Integer)obj;
	}

	@Override
	public Integer queryTicketX(Date date, int x) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("date", date);
		map.put("x", x);
		Object obj = this.getSqlMapClientTemplate().queryForObject("accountStatistics.queryTicketX",map);
		return obj == null?0:(Integer)obj;
	}

	@Override
	public void insertStatistics(AccountStatistics statistics) {
		this.getSqlMapClientTemplate().insert("accountStatistics.insertStatistics",statistics);
	}

	@Override
	public Integer queryAddWhiteListTotal(Date date) {
		Object obj = this.getSqlMapClientTemplate().queryForObject("accountStatistics.queryAddWhiteListTotal",date);
		return obj == null?0:(Integer)obj;
	}

}
