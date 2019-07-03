package com.l9e.transaction.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.SystemDao;
import com.l9e.transaction.service.SystemService;

@Service("systemService")
public class SystemServiceImpl implements SystemService {
	
	@Resource
	private SystemDao systemDao;

	@Override
	public String getSystemSettingValue(String settingName) {
		return systemDao.selectSettingValue(settingName);
	}

}
