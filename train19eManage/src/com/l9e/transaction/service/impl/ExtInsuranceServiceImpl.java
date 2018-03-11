package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.ExtInsuranceDao;
import com.l9e.transaction.service.ExtInsuranceService;
@Service("extInsuranceService")
public class ExtInsuranceServiceImpl implements ExtInsuranceService {

	@Resource 
	private ExtInsuranceDao extInsuranceDao;

	public List<Map<String, Object>> queryInsuranceList(Map<String, Object> query_Map) {
		return extInsuranceDao.queryInsuranceList(query_Map);
	}

	public int queryInsuranceListCount(Map<String, Object> query_Map) {
		return extInsuranceDao.queryInsuranceListCount(query_Map);
	}

	public List<Map<String, Object>> queryInsuranceInfo(
			Map<String, Object> query_Map) {
		return extInsuranceDao.queryInsuranceInfo(query_Map);
	}

	public void updateInsuranceStatusSendAgain(Map<String, Object> update_Map) {
		extInsuranceDao.updateInsuranceStatusSendAgain(update_Map);
	}
	
	public void updateInsuranceStatusNeedCancel(Map<String, Object> update_Map) {
		extInsuranceDao.updateInsuranceStatusNeedCancel(update_Map);
	}

	public void addLog(Map<String, Object> log_Map) {
		extInsuranceDao.addLog(log_Map);
	}

	public List<Map<String, Object>> queryLog(String order_id) {
		return extInsuranceDao.queryLog(order_id);
	}

}
