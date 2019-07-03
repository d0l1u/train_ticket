package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.CtripAccountDao;
import com.l9e.transaction.service.CtripAccountService;
import com.l9e.transaction.vo.CtripAcc;

@Service("ctripAccountService")
public class CtripAccountServiceImpl implements CtripAccountService {
	
	private static final Logger logger = Logger.getLogger(CtripAccountServiceImpl.class);
	
	@Resource
	private CtripAccountDao ctripAccountDao;

	@Override
	public int modifyCtripAccount(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return ctripAccountDao.updateCtripAccount(paramMap);
	}

	@Override
	public List<CtripAcc> queryCtripAccountList(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return ctripAccountDao.selectCtripAccountList(paramMap);
	}

}
