package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.Station;


public interface CommonDao {

	public List<String> querySysSettingValue(Map<String, String> paramMap);

	public String querySysSettingByKey(String key);

	public String queryMeituanSysValueByName(String settingName);
	
	public Station selectOneStation(Map<String, Object> queryParam);
}
