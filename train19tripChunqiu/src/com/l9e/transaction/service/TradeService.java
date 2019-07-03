package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.TradeVo;

public interface TradeService {

	//向交易流水表里添加交易流水
	public void insertIntoTrade(TradeVo trade);
	//根据条件查询交易流水
	public List<TradeVo> queryTrade(Map<String, Object> map);
	//更新交易流水
	public int updateTrade(TradeVo trade);
	
	//获取不重复的退款批次batch_no
	public List<String> queryDistinctBatchNo(Map<String, Object> map);
}
