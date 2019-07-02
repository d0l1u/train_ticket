package com.l9e.transaction.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.RefundNotifyDao;
@Repository("refundNotifyDao")
public class RefundNotifyDaoImpl extends BaseDao implements RefundNotifyDao {

	@Override
	public void insertIntoNotify(Map<String, Object> map) {
		this.getSqlMapClientTemplate().insert("refundNotify.insertIntoNotify", map);
	}

}
