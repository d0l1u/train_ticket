package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.CommonDao;
import com.l9e.transaction.vo.Worker;

@Repository("commonDao")
public class CommonDaoImpl extends BaseDao implements CommonDao {

	@Override
	public String querySysSettingByKey(String key) {
		return (String) this.getSqlMapClientTemplate().queryForObject("common.querySysSettingByKey", key);
	}

	@Override
	public Worker queryRandomWorker() {
		return (Worker) this.getSqlMapClientTemplate().queryForObject("common.queryRandomWorker");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryWaitNotify() {
		return this.getSqlMapClientTemplate().queryForList("common.queryWaitNotify");
	}

	@Override
	public void updateRobotStartNotify(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("common.updateRobotStartNotify",map);
	}

	@Override
	public List<Map<String, String>> queryLockOrderList() {
		return this.getSqlMapClientTemplate().queryForList("common.queryLockOrderList");
	}

	@Override
	public void addCpOrderHistory(Map<String, String> map) {
		this.getSqlMapClientTemplate().insert("common.addCpOrderHistory",map);
	}

	@Override
	public void updateCpOrderStatus(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("common.updateCpOrderStatus",map);
	}

	@Override
	public void updateCpOrderNotify(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("common.updateCpOrderNotify",map);
	}

	@Override
	public List<Map<String, String>> queryLockAlterOrderList() {
		return this.getSqlMapClientTemplate().queryForList("common.queryLockAlterOrderList");
	}

	@Override
	public void updateQunarRefundOrderStatus(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("common.updateQunarRefundOrderStatus",map);
	}

	@Override
	public void update19eRefundOrderStatus(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("common.update19eRefundOrderStatus",map);
	}

	@Override
	public List<String> queryPinganMessageList() {
		return this.getSqlMapClientTemplate().queryForList("common.queryPinganMessageList");
	}
	@Override
	public List<Map<String, String>> queryAccountSeatList(Map<String, String> param) {
		return this.getSqlMapClientTemplate().queryForList("common.queryAccountSeatList",param);
	}

	@Override
	public void updateAccountStatusFree(String acc_id) {
		this.getSqlMapClientTemplate().update("common.updateAccountStatusFree",acc_id);
	}
	@Override
	public int updateQunarSysNoticeStaus(Map<String, String> param) {
		return this.getSqlMapClientTemplate().update("common.updateQunarSysNoticeStaus",param);
	}

	@Override
	public List<Map<String, String>> queryExtMerchantList() {
		return this.getSqlMapClientTemplate().queryForList("common.queryExtMerchantList");
	}

	@Override
	public List<String> queryLockBookOrder(Map<String, String> param) {
		return this.getSqlMapClientTemplate().queryForList("common.queryLockBookOrder",param);
	}

	@Override
	public void updateCpOrderinfoNotify(Map<String, String> param) {
		this.getSqlMapClientTemplate().update("common.updateCpOrderinfoNotify",param);
	}

	@Override
	public void insertCpHistory(Map<String, String> param) {
		this.getSqlMapClientTemplate().update("common.insertCpHistory",param);
	}

	@Override
	public int updateElongSysNoticeStaus(Map<String, String> paramElong) {
		return this.getSqlMapClientTemplate().update("common.updateElongSysNoticeStaus",paramElong);
		
	}

	@Override
	public void addCpOrderRefundHistory(Map<String, String> param) {
		this.getSqlMapClientTemplate().insert("common.addCpOrderRefundHistory",param);
	}

	@Override
	public List<Map<String, String>> queryInnerLockOrderList() {
		return this.getSqlMapClientTemplate().queryForList("common.queryInnerLockOrderList");
	}

	@Override
	public void updateLockCpOrderinfoNotify(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("common.updateLockCpOrderinfoNotify",map);
	}

	@Override
	public List<Map<String, String>> query19eEopLockOrderList() {
		return this.getSqlMapClientTemplate().queryForList("common.query19eEopLockOrderList");
	}

	@Override
	public void updateEopLockOrder(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("common.updateEopLockOrder",map);
	}

	@Override
	public Map<String, String> queryCpOrderList(Map<String, String> map) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("common.queryCpOrderList",map);
	}

	@Override
	public List<Map<String, String>> query19eLockOrderList() {
		return this.getSqlMapClientTemplate().queryForList("common.query19eLockOrderList");
	}

	@Override
	public List<Map<String, String>> queryElongLockOrderList() {
		return this.getSqlMapClientTemplate().queryForList("common.queryElongLockOrderList");
	}

	@Override
	public List<Map<String, String>> queryExtLockOrderList() {
		return this.getSqlMapClientTemplate().queryForList("common.queryExtLockOrderList");
	}

	@Override
	public List<Map<String, String>> queryQunarLockOrderList() {
		return this.getSqlMapClientTemplate().queryForList("common.queryQunarLockOrderList");
	}

	@Override
	public List<Map<String, String>> queryChinaCitysInfo(
			Map<String, Integer> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("common.queryChinaCitysInfo",paramMap);
	}

	@Override
	public String queryAccountIdByOrderId(String order_id) {
		return (String)this.getSqlMapClientTemplate().queryForObject("common.queryAccountIdByOrderId",order_id);
	}
}
