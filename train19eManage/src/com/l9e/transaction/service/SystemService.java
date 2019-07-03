package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.AccountVo;
import com.l9e.transaction.vo.AcquireVo;
import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.SystemVo;

public interface SystemService {
	List<Map<String, String>> querySystemList(Map<String, Object> paramMap);

	int querySystemListCount(Map<String, Object> paramMap);

	Map<String, String> querySystem(String config_id);

	void updateSystem(SystemVo  system);
	
	void insertSystem(SystemVo  system);
	
	void deleteSystem(SystemVo  system);

	void updateSetting(String setting_value);
	
	String querySetting();
	
	
	/**
	 * 获取省份
	 * @return
	 */
	public List<AreaVo> getProvince();
	
	
	/**
	 * 获取城市
	 * @return
	 */
	public List<AreaVo>  getCity(String provinceid);
	
	
	/**
	 * 获取区县
	 * @return
	 */
	public List<AreaVo>  getArea(String cityid);

	void updateEstateNot(Map<String,String> modify_Map);

	
}
