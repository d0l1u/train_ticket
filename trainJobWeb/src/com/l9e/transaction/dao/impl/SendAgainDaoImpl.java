package com.l9e.transaction.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.SendAgainDao;

@Repository("sendAgainDao")
public class SendAgainDaoImpl extends BaseDao  implements SendAgainDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<String> queryNeedSendAgainOrder(int num) {
		return this.getSqlMapClientTemplate().queryForList("sendAgain.queryNeedSendAgainOrder",num);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> queryNeedToManualOrder(int num) {
		return this.getSqlMapClientTemplate().queryForList("sendAgain.queryNeedToManualOrder",num);
	}

	@Override
	public void updateToManualByOrderId(String orderId) {
		this.getSqlMapClientTemplate().update("sendAgain.updateToManualByOrderId",orderId);
	}

	@Override
	public void updateToSendAgainByOrderId(String orderId) {
		this.getSqlMapClientTemplate().update("sendAgain.updateToSendAgainByOrderId",orderId);
	}

	@Override
	public void updateCpQueueByOrderId(String orderId) {
		this.getSqlMapClientTemplate().update("sendAgain.updateCpQueueByOrderId",orderId);
	}
}
