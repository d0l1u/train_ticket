package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.CommonDao;
import com.l9e.transaction.vo.Worker;

@Repository("commonDao")
public class CommonDaoImpl extends BaseDao implements CommonDao {

	
	public String querySysSettingByKey(String key) {
		return (String) this.getSqlMapClientTemplate().queryForObject("common.querySysSettingByKey", key);
	}

	
	public Worker queryRandomWorker(Map<String,Object> map) {
		return (Worker) this.getSqlMapClientTemplate().queryForObject("common.queryRandomWorker",map);
	}

	@SuppressWarnings("unchecked")
	
	public List<Map<String, String>> queryWaitNotify() {
		return this.getSqlMapClientTemplate().queryForList("common.queryWaitNotify");
	}

	
	public void updateRobotStartNotify(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("common.updateRobotStartNotify",map);
	}

	
	public List<Map<String, String>> queryLockOrderList() {
		return this.getSqlMapClientTemplate().queryForList("common.queryLockOrderList");
	}

	
	public void addCpOrderHistory(Map<String, String> map) {
		this.getSqlMapClientTemplate().insert("common.addCpOrderHistory",map);
	}

	
	public void updateCpOrderStatus(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("common.updateCpOrderStatus",map);
	}

	
	public void updateCpOrderNotify(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("common.updateCpOrderNotify",map);
	}

	
	public List<Map<String, String>> queryLockAlterOrderList() {
		return this.getSqlMapClientTemplate().queryForList("common.queryLockAlterOrderList");
	}

	
	public void updateQunarRefundOrderStatus(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("common.updateQunarRefundOrderStatus",map);
	}

	
	public void update19eRefundOrderStatus(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("common.update19eRefundOrderStatus",map);
	}

	
	public List<String> queryPinganMessageList() {
		return this.getSqlMapClientTemplate().queryForList("common.queryPinganMessageList");
	}
	
	public List<Map<String, String>> queryAccountSeatList(Map<String, String> param) {
		return this.getSqlMapClientTemplate().queryForList("common.queryAccountSeatList",param);
	}

	
	public void updateAccountStatusFree(String acc_id) {
		this.getSqlMapClientTemplate().update("common.updateAccountStatusFree",acc_id);
	}
	
	public int updateQunarSysNoticeStaus(Map<String, String> param) {
		return this.getSqlMapClientTemplate().update("common.updateQunarSysNoticeStaus",param);
	}

	
	public List<Map<String, String>> queryExtMerchantList() {
		return this.getSqlMapClientTemplate().queryForList("common.queryExtMerchantList");
	}

	
	public List<String> queryLockBookOrder(Map<String, String> param) {
		return this.getSqlMapClientTemplate().queryForList("common.queryLockBookOrder",param);
	}

	
	public void updateCpOrderinfoNotify(Map<String, String> param) {
		this.getSqlMapClientTemplate().update("common.updateCpOrderinfoNotify",param);
	}

	
	public void insertCpHistory(Map<String, String> param) {
		this.getSqlMapClientTemplate().update("common.insertCpHistory",param);
	}

	
	public void updateElongRefundOrderStatus(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("common.updateElongRefundOrderStatus",map);
	}

	
	public int updateElongSysNoticeStaus(Map<String, String> paramElong) {
		return this.getSqlMapClientTemplate().update("common.updateElongSysNoticeStaus",paramElong);
		
	}

	
	public Integer queryHeyanWorkNum(Integer worker_type) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("common.queryHeyanWorkNum", worker_type);
	}

	
	public Map<String, String> queryElongSystemSetting(String setting_name) {
		Object object = this.getSqlMapClientTemplate().queryForObject("common.queryElongSystemSetting", setting_name);
		if(object != null){
			return (Map<String, String>)object;
		}else{
			return null;
		}
	}

	
	public Map<String, String> queryQunarSystemSetting(String setting_name) {
		Object object = this.getSqlMapClientTemplate().queryForObject("common.queryQunarSystemSetting", setting_name);
		if(object != null){
			return (Map<String, String>)object;
		}else{
			return null;
		}
	}


	public int updateBalance(Map<String, String> param) {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().update("common.updateBalance",param);
	}
}
