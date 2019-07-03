package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.TradeDao;
import com.l9e.transaction.service.TradeService;
import com.l9e.transaction.vo.TradeVo;

@Service("tradeService")
public class TradeServiceImpl implements TradeService {

	@Resource
	private TradeDao tradeDao;

	@Override
	public void insertIntoTrade(TradeVo trade) {
		tradeDao.insertIntoTrade(trade);
	}

	@Override
	public List<TradeVo> queryTrade(Map<String, Object> map) {
		return tradeDao.queryTrade(map);
	}

	@Override
	public int updateTrade(TradeVo trade) {
		return tradeDao.updateTrade(trade);
	}

	@Override
	public List<String> queryDistinctBatchNo(Map<String, Object> map) {
		return tradeDao.queryDistinctBatchNo(map);
	}

}
