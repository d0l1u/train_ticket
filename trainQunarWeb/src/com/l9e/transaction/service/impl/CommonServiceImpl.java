package com.l9e.transaction.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.CommonDao;
import com.l9e.transaction.service.CommonService;

@Service("commonService")
public class CommonServiceImpl implements CommonService {

	@Resource
	private CommonDao commonDao;

	@Override
	public String queryQunarSysValue(String setting_name) {
		return commonDao.queryQunarSysValue(setting_name);
	}

}
