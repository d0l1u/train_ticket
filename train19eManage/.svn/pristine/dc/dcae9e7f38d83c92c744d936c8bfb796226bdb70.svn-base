package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.PsOrderDao;
import com.l9e.transaction.dao.TuniuPushTimeoutOrderDao;
import com.l9e.transaction.service.TuniuPushTimeoutOrderService;

@Service("tuniuPushTimeoutOrderService")
public class TuniuPushTimeoutOrderServiceImpl implements TuniuPushTimeoutOrderService{
	
	@Resource
	private TuniuPushTimeoutOrderDao tuniuPushTimeoutOrderDao;
	
	
	@Override
	public int queryTuniuTimeOutListCount(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		
		return tuniuPushTimeoutOrderDao.queryTuniuTimeOutListCount(paramMap);
		 
	}


	@Override
	public List<Map<String, String>> queryTuniuTimeOutList(
			Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return tuniuPushTimeoutOrderDao.queryTuniuTimeOutList(paramMap);
	}


	@Override
	public void changeDealStatus(Map<String, String> map) {
		// TODO Auto-generated method stub
		 tuniuPushTimeoutOrderDao.changeDealStatus(map);
	}
	
	

}
