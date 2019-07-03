package com.l9e.transaction.service.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.AllInsuranceDao;
import com.l9e.transaction.service.AllInsuranceService;
@Service("allInsuranceService")
public class AllInsuranceServiceImpl implements AllInsuranceService {

	@Resource 
	private AllInsuranceDao allInsuranceDao;

	public List<Map<String, Object>> queryInsuranceList(Map<String, Object> query_Map) {
		return allInsuranceDao.queryInsuranceList(query_Map);
	}

	public int queryInsuranceListCount(Map<String, Object> query_Map) {
		return allInsuranceDao.queryInsuranceListCount(query_Map);
	}

	public List<Map<String, Object>> queryInsuranceInfo(
			Map<String, Object> query_Map) {
		return allInsuranceDao.queryInsuranceInfo(query_Map);
	}

	public void updateInsuranceStatusSendAgain(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> update_Map) {
		int count=	 allInsuranceDao.updateInsuranceStatusSendAgain(update_Map);
		if(count>0){
			write(response,"true");
		}else{
			write(response,"false");
		}
		
	}
	private void write(HttpServletResponse response, String wrStr) {
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(wrStr);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateInsuranceStatusNeedCancel(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> update_Map) {
		int count=	 allInsuranceDao.updateInsuranceStatusNeedCancel(update_Map);
		if(count>0){
			write2(response,"true");
		}else{
			write2(response,"false");
		}
	}
	private void write2(HttpServletResponse response, String wrStr) {
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(wrStr);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addLog(Map<String, Object> log_Map) {
		allInsuranceDao.addLog(log_Map);
	}

	public List<Map<String, Object>> queryLog(String order_id) {
		return allInsuranceDao.queryLog(order_id);
	}
	
    //查询预订订单信息
	public Map<String, String> queryAllBookOrderInfo(String order_id) {
		return allInsuranceDao.queryAllBookOrderInfo(order_id).get(0);
	}

	@Override
	public void plUpdateAgain() {
		allInsuranceDao.plUpdateAgain();
	}

}
