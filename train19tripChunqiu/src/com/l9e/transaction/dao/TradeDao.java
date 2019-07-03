package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.TradeVo;

public interface TradeDao {

	public void insertIntoTrade(TradeVo trade);
	
	public List<TradeVo> queryTrade(Map<String, Object> map);
	
	public int updateTrade(TradeVo trade);
	
	public List<String> queryDistinctBatchNo(Map<String, Object> map);
}
