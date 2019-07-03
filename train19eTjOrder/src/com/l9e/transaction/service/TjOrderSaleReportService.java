package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface TjOrderSaleReportService {

	int queryTableCount();

	List<String> queryDateList();

	List<String> queryDealeiIdList(String createTime);

	double queryThisPaymoney(Map<String, Object> paramMap);

	double queryThisRefundmoney(Map<String, Object> paramMap);

	double queryThisBxcountMoney10(Map<String, Object> paramMap);

	double queryThisBxcountMoney20(Map<String, Object> paramMap);

	int queryThisOrdercount(Map<String, Object> paramMap);

	int queryThisTicketcount(Map<String, Object> paramMap);

	void addToTjOrderSaleReportJob(Map<String, Object> map);

	double queryMonthBxcountMoney10(Map<String, Object> paramMap);

	double queryMonthBxcountMoney20(Map<String, Object> paramMap);

	int queryMonthOrdercount(Map<String, Object> paramMap);

	int queryMonthTicketcount(Map<String, Object> paramMap);

}
