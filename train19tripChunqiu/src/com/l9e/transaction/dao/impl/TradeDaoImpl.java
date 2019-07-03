package com.l9e.transaction.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.TradeDao;
import com.l9e.transaction.vo.TradeVo;

import common.Logger;

@Repository("tradeDao")
public class TradeDaoImpl extends BaseDao implements TradeDao {

	private static Logger logger = Logger.getLogger(TradeDaoImpl.class);

	// 向交易流水表里添加交易流水
	@Override
	public void insertIntoTrade(TradeVo trade) {
		this.getSqlMapClientTemplate().insert("trade.insertIntoTrade", trade);
	}

	@Override
	public List<TradeVo> queryTrade(Map<String, Object> map) {
		List<TradeVo> tradeList = new ArrayList<TradeVo>();
		tradeList = this.getSqlMapClientTemplate().queryForList(
				"trade.queryTrade", map);
		return tradeList;
	}

	@Override
	public int updateTrade(TradeVo trade) {
		Integer row = this.getSqlMapClientTemplate().update(
				"trade.updateTrade", trade);
		logger.info("更新Trade表受影响的行数为：" + row);
		return row;
	}

	@Override
	public List<String> queryDistinctBatchNo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().queryForList("trade.queryDistinctBatchNo", map);
	}

}
