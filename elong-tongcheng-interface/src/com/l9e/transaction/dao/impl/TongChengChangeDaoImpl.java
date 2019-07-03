package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.TongChengChangeDao;
import com.l9e.transaction.vo.DBChangeInfo;
import com.l9e.transaction.vo.DBPassengerChangeInfo;
import com.l9e.transaction.vo.TongchengChangeLogVO;

/**
 * 同城同步、异步改签dao实现
 * @author licheng
 *
 */
@Repository("tongChengChangeDao")
public class TongChengChangeDaoImpl extends BaseDao implements TongChengChangeDao {

	/*改签业务*/
	@Override
	public void insertChangeCp(DBPassengerChangeInfo cPassenger) {
		this.getSqlMapClientTemplate().insert("tongCheng.insertChangePassenger", cPassenger);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<DBPassengerChangeInfo> selectChangeCp(Map<String, Object> param) {
		return this.getSqlMapClientTemplate().queryForList("tongCheng.selectChangePassenger", param);
	}


	@Override
	public DBChangeInfo getChangeInfo(Map<String, Object> param) {
		return (DBChangeInfo) this.getSqlMapClientTemplate().queryForObject("tongCheng.selectChangeInfo", param);
	}


	@Override
	public void insertChangeInfo(DBChangeInfo changeInfo) {
		this.getSqlMapClientTemplate().insert("tongCheng.insertChangeInfo", changeInfo);
	}


	@Override
	public DBPassengerChangeInfo selectChangePassenger(Map<String, Object> param) {
		return (DBPassengerChangeInfo) this.getSqlMapClientTemplate().queryForObject("tongCheng.selectChangePassenger", param);
	}


	@Override
	public void updateChangeCp(DBPassengerChangeInfo cPassenger) {
		this.getSqlMapClientTemplate().update("tongCheng.updateChangeCp", cPassenger);
	}


	@Override
	public void updateChangeInfo(DBChangeInfo changeInfo) {
		this.getSqlMapClientTemplate().update("tongCheng.updateChangeInfo", changeInfo);
	}
	
	@Override
	public String selectAccountId(String orderId) {
		return (String) this.getSqlMapClientTemplate().queryForObject("tongCheng.getAccountId", orderId);
	}
	
	
	@Override
	public List<String> selectReqtokens(String orderId) {
		return this.getSqlMapClientTemplate().queryForList("tongCheng.getReqtokens", orderId);
	}
	
	@Override
	public List<DBChangeInfo> selectTimeoutConfirmChange() {
		return this.getSqlMapClientTemplate().queryForList("tongCheng.selectTimeoutConfirmChange");
	}
	
	
	@Override
	public List<DBChangeInfo> selectTimeoutRequestChange() {
		return this.getSqlMapClientTemplate().queryForList("tongCheng.selectTimeoutRequestChange");
	}
	
	

	/*改签通知*/




	@Override
	public List<DBChangeInfo> selectNotifyList() {
		return this.getSqlMapClientTemplate().queryForList("tongCheng.notifyList");
	}


	@Override
	public int updateNotifyBegin(DBChangeInfo changeInfo) {
		return this.getSqlMapClientTemplate().update("tongCheng.notifyBegin", changeInfo);
	}


	@Override
	public void updateNotifyEnd(DBChangeInfo changeInfo) {
		this.getSqlMapClientTemplate().update("tongCheng.notifyEnd", changeInfo);
	}


	@Override
	public DBPassengerChangeInfo selectChangePassengerByCpId(String cp_id) {
		return (DBPassengerChangeInfo) this.getSqlMapClientTemplate().queryForObject("tongCheng.selectChangePassengerByCpId", cp_id);
	}

	/*日志*/
	
	@Override
	public void insertChangeLog(TongchengChangeLogVO log) {
		this.getSqlMapClientTemplate().insert("tongCheng.addLog", log);
	}

	

}
