package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.Tj_DealerDao;
import com.l9e.transaction.service.Tj_DealerService;
@Service("tj_Dealer_Service")
public class Tj_DelerServiceImpl implements Tj_DealerService{
	@Resource
	private Tj_DealerDao tj_DealerDao;
	
	public void addToTj_Dealer(Map<String, Object> map) {
		tj_DealerDao.addToTj_Dealer(map);
	}

	public String queryAreaNameByDealerId(Map<String, Object> paramMap) {
		return tj_DealerDao.queryAreaNameByDealerId(paramMap);
	}

	public List<String> queryDealerIdByMouth(
			Map<String, Object> paramMap) {
		return tj_DealerDao.queryDealerIdByMouth(paramMap);
	}

	public String queryOrderCountByDealerId(Map<String, Object> paramMap) {
		return tj_DealerDao.queryOrderCountByDealerId(paramMap);
	}

	public String queryPayMoneyByDealerId(Map<String, Object> paramMap) {
		return tj_DealerDao.queryPayMoneyByDealerId(paramMap);
	}

	public String queryRefundCountByDealerId(Map<String, Object> paramMap) {
		return tj_DealerDao.queryRefundCountByDealerId(paramMap);
	}

	public String queryRefundMoneyByDealerId(Map<String, Object> paramMap) {
		return tj_DealerDao.queryRefundMoneyByDealerId(paramMap);
	}

}
