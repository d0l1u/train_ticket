package com.l9e.transaction.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.UpdateCheckAutoDao;
import com.l9e.transaction.service.UpdateCheckAutoService;

@Service("updateCheckAutoService")
public class UpdateCheckAutoServiceImpl implements UpdateCheckAutoService{

	@Resource
	private UpdateCheckAutoDao updateCheckAutoDao;
	
	@Override
	public String querySettingStatus(String string) {
		return updateCheckAutoDao.querySettingStatus(string);
	}

	@Override
	public void updateSettingStatus(String string) {
		updateCheckAutoDao.updateSettingStatus(string);
	}

}
