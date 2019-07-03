package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ElongOrderGoBackDao;

@Repository("ElongOrderGoBackDao")
public class ElongOrderGoBackDaoImpl extends BaseDao implements ElongOrderGoBackDao{

	@Override
	public List<Map<String,Object>> getOrderGoBackIdList() {
		return this.getSqlMapClientTemplate().queryForList("elongGoBack.getOrderGoBackIdList");
	}

	@Override
	public void deleteAllElongCpInfo(String order_id) {
		this.getSqlMapClientTemplate().delete("elongGoBack.deleteAllElongCpInfo", order_id);
	}

	@Override
	public void deleteAllElongNotifyInfo(String order_id) {
		this.getSqlMapClientTemplate().delete("elongGoBack.deleteAllElongNotifyInfo",order_id);
	}

	@Override
	public void deleteAllElongOrderInfo(String order_id) {
		this.getSqlMapClientTemplate().delete("elongGoBack.deleteAllElongOrderInfo",order_id);
	}

	@Override
	public void deleteAllSysCpInfo(String order_id) {
		this.getSqlMapClientTemplate().delete("elongGoBack.deleteAllSysCpInfo",order_id);
	}

	@Override
	public void deleteAllSysNotifyInfo(String order_id) {
		this.getSqlMapClientTemplate().delete("elongGoBack.deleteAllSysNotifyInfo",order_id);
	}

	@Override
	public void deleteAllSysOrderInfo(String order_id) {
		this.getSqlMapClientTemplate().delete("elongGoBack.deleteAllSysOrderInfo", order_id);
	}

	@Override
	public void insertAllElongCpInfo(String order_id) {
		this.getSqlMapClientTemplate().insert("elongGoBack.insertAllElongCpInfo", order_id);
	}

	@Override
	public void insertElongOrderInfo(String order_id) {
		this.getSqlMapClientTemplate().insert("elongGoBack.insertElongOrderInfo",order_id);
	}

	@Override
	public void updateGoBackNotify(Map<String,String> param) {
		this.getSqlMapClientTemplate().update("elongGoBack.updateGoBackNotify",param);
	}

	@Override
	public void updateGoBackOrderInfoStatusFail(String order_id) {
		this.getSqlMapClientTemplate().update("elongGoBack.updateGoBackOrderInfoStatusFail",order_id);
	}

}
