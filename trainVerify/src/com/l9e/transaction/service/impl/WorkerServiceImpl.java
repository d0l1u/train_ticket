package com.l9e.transaction.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.WorkerDao;
import com.l9e.transaction.service.WorkerService;
import com.l9e.transaction.vo.CardBankVo;
import com.l9e.transaction.vo.CardInfoVo;

@Service("workerService")
public class WorkerServiceImpl implements WorkerService {

	private static final Logger logger = Logger
			.getLogger(WorkerServiceImpl.class);

	@Resource
	private WorkerDao workerDao;

	public void updateWorker(Map<String, Object> map) {
		workerDao.updateWorker(map);

	}

	
	public String queryVerifyCode(String account) {
		return workerDao.queryVerifyCode(account);
	}

	
	public CardInfoVo queryCardInfoVoByComNo(String comNo) {
		// TODO Auto-generated method stub
		return workerDao.queryCardInfoVoByComNo(comNo);
	}

	
	public void insertWorkerCodeInfo(Map<String, String> insertMap) {
		workerDao.insertWorkerCodeInfo(insertMap);
	
	}


	public void insertAlipayCodeInfo(Map<String, String> insertMap) {
		// TODO Auto-generated method stub
		workerDao.insertAlipayCodeInfo(insertMap);
	}


	@Override
	public CardBankVo queryCardBank(Map<String, String> map) { 
		return workerDao.queryCardBank(map);
	}

}
