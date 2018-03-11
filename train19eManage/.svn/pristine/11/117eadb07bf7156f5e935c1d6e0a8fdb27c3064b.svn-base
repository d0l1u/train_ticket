package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.InsuranceDao;
import com.l9e.transaction.service.InsuranceService;
@Service("insuranceService")
public class InsuranceServiceImpl implements InsuranceService{
	@Resource 
	private InsuranceDao insuranceDao;

	public List<Map<String, Object>> queryInsuranceList(Map<String, Object> query_Map) {
		return insuranceDao.queryInsuranceList(query_Map);
	}

	public int queryInsuranceListCount(Map<String, Object> query_Map) {
		return insuranceDao.queryInsuranceListCount(query_Map);
	}

	public List<Map<String, Object>> queryInsuranceInfo(
			Map<String, Object> query_Map) {
		return insuranceDao.queryInsuranceInfo(query_Map);
	}

	public void updateInsuranceStatusSendAgain(Map<String, Object> update_Map) {
		insuranceDao.updateInsuranceStatusSendAgain(update_Map);
	}
	
	public void updateInsuranceStatusNeedCancel(Map<String, Object> update_Map) {
		insuranceDao.updateInsuranceStatusNeedCancel(update_Map);
	}

	public void addLog(Map<String, Object> log_Map) {
		insuranceDao.addLog(log_Map);
	}

	public List<Map<String, Object>> queryLog(String order_id) {
		return insuranceDao.queryLog(order_id);
	}
}
