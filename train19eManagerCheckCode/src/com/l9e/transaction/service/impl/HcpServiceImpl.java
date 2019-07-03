package com.l9e.transaction.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.HcpDao;
import com.l9e.transaction.dao.LoginDao;
import com.l9e.transaction.service.HcpService;
@Service("hcpService")
public class HcpServiceImpl implements HcpService {
	@Resource
	private HcpDao hcpDao;
	
	@Override
	public int queryAdminCurrentNameCount() {
		return hcpDao.queryAdminCurrentNameCount();
	}

	@Override
	public int queryCodeCountToday() {
		return hcpDao.queryCodeCountToday();
	}

	@Override
	public int queryCodeToday() {
		return hcpDao.queryCodeToday();
	}

	@Override
	public int queryMinutes() {
		return hcpDao.queryMinutes();
	}

	@Override
	public int querySuccess1() {
		return hcpDao.querySuccess1();
	}

	@Override
	public int querySuccess2() {
		return hcpDao.querySuccess2();
	}

	@Override
	public int queryUncode(String channel) {
		return hcpDao.queryUncode(channel);
	}
	
	@Override
	public String codeQunarType() {
		return hcpDao.codeQunarType();
	}


}
