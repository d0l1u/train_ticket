package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.TjOrderSaleReportDao;
import com.l9e.transaction.service.TjOrderSaleReportService;
@Service("tjOrderSaleReportService")
public class TjOrderSaleReportServiceImpl implements TjOrderSaleReportService {
	
	private static final Logger logger = Logger.getLogger(TjOrderSaleReportServiceImpl.class);

	@Resource
	private TjOrderSaleReportDao tjOrderSaleReportDao;
	
	public void addToTjOrderSaleReportJob(Map<String, Object> map) {
		tjOrderSaleReportDao.addToTjOrderSaleReportJob(map);
	}

	public List<String> queryDateList() {
		return tjOrderSaleReportDao.queryDateList();
	}

	public List<String> queryDealeiIdList(String createTime) {
		return tjOrderSaleReportDao.queryDealeiIdList(createTime);
	}

	public double queryMonthBxcountMoney10(Map<String, Object> paramMap) {
		return tjOrderSaleReportDao.queryMonthBxcountMoney10(paramMap);
	}

	public double queryMonthBxcountMoney20(Map<String, Object> paramMap) {
		return tjOrderSaleReportDao.queryMonthBxcountMoney20(paramMap);
	}

	public int queryTableCount() {
		return tjOrderSaleReportDao.queryTableCount();
	}

	public double queryThisBxcountMoney10(Map<String, Object> paramMap) {
		return tjOrderSaleReportDao.queryThisBxcountMoney10(paramMap);
	}

	public double queryThisBxcountMoney20(Map<String, Object> paramMap) {
		return tjOrderSaleReportDao.queryThisBxcountMoney20(paramMap);
	}

	public int queryThisOrdercount(Map<String, Object> paramMap) {
		return tjOrderSaleReportDao.queryThisOrdercount(paramMap);
	}

	public double queryThisPaymoney(Map<String, Object> paramMap) {
		return tjOrderSaleReportDao.queryThisPaymoney(paramMap);
	}

	public double queryThisRefundmoney(Map<String, Object> paramMap) {
		return tjOrderSaleReportDao.queryThisRefundmoney(paramMap);
	}

	public int queryThisTicketcount(Map<String, Object> paramMap) {
		return tjOrderSaleReportDao.queryThisTicketcount(paramMap);
	}

	public int queryMonthOrdercount(Map<String, Object> paramMap) {
		return tjOrderSaleReportDao.queryMonthOrdercount(paramMap);
	}

	public int queryMonthTicketcount(Map<String, Object> paramMap) {
		return tjOrderSaleReportDao.queryMonthTicketcount(paramMap);
	}

}
