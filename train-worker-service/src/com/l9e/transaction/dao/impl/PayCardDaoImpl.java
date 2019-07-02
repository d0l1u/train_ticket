package com.l9e.transaction.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.PayCardDao;
import com.l9e.transaction.vo.PayCard;

@Repository("payCardDao")
public class PayCardDaoImpl extends BaseDao implements PayCardDao {

	@Override
	public PayCard selectOnePayCard(Map<String, Object> params) {
		return (PayCard) getSqlMapClientTemplate().queryForObject("payCard.selectPayCard", params);
	}

	@Override
	public int updatePayCard(PayCard payCard) {
		return getSqlMapClientTemplate().update("payCard.updatePayCard", payCard);
	}

}
