package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.AppInsuranceDao;
import com.l9e.transaction.service.AppInsuranceService;
@Service("appInsuranceService")
public class AppInsuranceServiceImpl implements AppInsuranceService {

	@Resource 
	private AppInsuranceDao appInsuranceDao;

	public List<Map<String, Object>> queryInsuranceList(Map<String, Object> query_Map) {
		return appInsuranceDao.queryInsuranceList(query_Map);
	}

	public int queryInsuranceListCount(Map<String, Object> query_Map) {
		return appInsuranceDao.queryInsuranceListCount(query_Map);
	}

	public List<Map<String, Object>> queryInsuranceInfo(
			Map<String, Object> query_Map) {
		return appInsuranceDao.queryInsuranceInfo(query_Map);
	}

	public void updateInsuranceStatusSendAgain(Map<String, Object> update_Map) {
		appInsuranceDao.updateInsuranceStatusSendAgain(update_Map);
	}
	
	public void updateInsuranceStatusNeedCancel(Map<String, Object> update_Map) {
		appInsuranceDao.updateInsuranceStatusNeedCancel(update_Map);
	}

	public void addLog(Map<String, Object> log_Map) {
		appInsuranceDao.addLog(log_Map);
	}

	public List<Map<String, Object>> queryLog(String order_id) {
		return appInsuranceDao.queryLog(order_id);
	}

}
