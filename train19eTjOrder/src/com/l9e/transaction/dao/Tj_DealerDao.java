package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

public interface Tj_DealerDao {
	public List<String> queryDealerIdByMouth(
			Map<String, Object> paramMap);

	public String queryAreaNameByDealerId(Map<String, Object> paramMap);

	public String queryPayMoneyByDealerId(Map<String, Object> paramMap);

	public String queryOrderCountByDealerId(Map<String, Object> paramMap);

	public String queryRefundMoneyByDealerId(Map<String, Object> paramMap);

	public String queryRefundCountByDealerId(Map<String, Object> paramMap);

	public void addToTj_Dealer(Map<String, Object> map);
}
