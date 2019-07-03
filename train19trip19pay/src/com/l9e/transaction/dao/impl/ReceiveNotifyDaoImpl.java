package com.l9e.transaction.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ReceiveNotifyDao;

@Repository("receiveNotifyDao")
public class ReceiveNotifyDaoImpl extends BaseDao implements ReceiveNotifyDao{

	public void updateOrderWithCpNotify(Map<String, String> paraMap) {
		this.getSqlMapClientTemplate().update("receiveNotify.updateOrderWithCpNotify", paraMap);
	}

	public void updateCpOrderWithCpNotify(Map<String, String> cpMap) {
		this.getSqlMapClientTemplate().update("receiveNotify.updateCpOrderWithCpNotify", cpMap);
	}

}
