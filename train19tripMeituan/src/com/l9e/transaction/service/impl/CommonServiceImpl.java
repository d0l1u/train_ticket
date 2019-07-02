package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.CommonDao;
import com.l9e.transaction.service.CommonService;

@Service("commonService")
public class CommonServiceImpl implements CommonService {

	@Resource
	private CommonDao commonDao;

	public List<String> querySysSettingValue(Map<String, String> paramMap) {
		return commonDao.querySysSettingValue(paramMap);
	}

	public String querySysSettingByKey(String key) {
		return commonDao.querySysSettingByKey(key);
	}

	@Override
	public String queryMeituanSysValueByName(String settingName) {
		return commonDao.queryMeituanSysValueByName(settingName);
	}

}
