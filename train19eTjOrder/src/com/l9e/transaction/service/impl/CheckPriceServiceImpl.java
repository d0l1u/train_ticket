package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.CheckPriceDao;
import com.l9e.transaction.service.CheckPriceService;

@Service("checkPriceService")
@Deprecated
public class CheckPriceServiceImpl implements CheckPriceService{
	@Resource
	private CheckPriceDao checkPriceDao;
	
	public void addAlipayInfoList(List<Map<String, Object>> paramList) {
		checkPriceDao.addAlipayInfoList(paramList);
	}

	public void addAlipayBalance(Map<String, Object> paMap) {
		checkPriceDao.addAlipayBalance(paMap);
	}
	


}
