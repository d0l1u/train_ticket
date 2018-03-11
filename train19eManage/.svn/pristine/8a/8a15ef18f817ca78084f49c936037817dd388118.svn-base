package com.l9e.transaction.dao.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.JDCardDao;

@Repository("jdCardDao")
public class JDCardDaoImpl extends BaseDao implements JDCardDao{

	@Override
	public int queryJDCardCounts(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return (Integer)this.getSqlMapClientTemplate().queryForObject("jdCard.queryJDCardCounts", paramMap);
	}

	@Override
	public List<Map<String, String>> queryJDCardList(
			Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().queryForList("jdCard.queryJDCardList", paramMap);
	}

	@Override
	public List<Map<String, BigDecimal>> queryJDCardMoney() {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().queryForList("jdCard.queryJDCardMoney");
	}

	@Override
	public void addJDCardInfo(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().insert("jdCard.addJDCardInfo", paramMap);
	}

	@Override
	public void updateJDCardInfo(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().update("jdCard.updateJDCardInfo", paramMap);
	}

	@Override
	public Map<String, Object> queryJDCardById(Integer cardID) {
		// TODO Auto-generated method stub
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("jdCard.queryJDCardById",cardID);
	}

}
