package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface CommonService {

	public List<String> querySysSettingValue(Map<String, String> paramMap);

	public String querySysSettingByKey(String key);
	
	public String queryMeituanSysValueByName(String settingName);
}
