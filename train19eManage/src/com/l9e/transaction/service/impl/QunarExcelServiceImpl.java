package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.QunarExcelDao;
import com.l9e.transaction.service.QunarExcelService;

@Service("qunarExcelService")
public class QunarExcelServiceImpl implements QunarExcelService {

	@Resource
	private QunarExcelDao qunarExcelDao;

	public List<Map<String, String>> queryQunarBook(HashMap<String, Object> map) {
		return qunarExcelDao.queryQunarBook(map);
	}
	
	//查询联程预订订单信息
	public List<Map<String, String>> queryLianChengQunarBook(HashMap<String, Object> map){
		return qunarExcelDao.queryLianChengQunarBook(map);
	}
	
	//查询退款订单信息
	public List<Map<String, String>> queryRefundTicket(HashMap<String, Object> map){
		return qunarExcelDao.queryRefundTicket(map);
	}

}
