package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.SystemDao;
import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.SystemVo;

@Repository("systemDao")
public class SystemDaoImpl extends BaseDao implements SystemDao{

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> querySystemList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("system.querySystemList", paramMap);
	}

	@SuppressWarnings("unchecked")
	public int querySystemListCount(Map<String, Object> paramMap) {
		return getTotalRows("system.querySystemListCount", paramMap);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> querySystem(String config_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("system.querySystem", config_id);
	}

	@SuppressWarnings("unchecked")
	public void updateSystem(SystemVo  system) {
		this.getSqlMapClientTemplate().update("system.updateSystem", system);
	}

	@SuppressWarnings("unchecked")
	public void insertSystem(SystemVo  system) {
		this.getSqlMapClientTemplate().insert("system.insertSystem", system);
	}

	@SuppressWarnings("unchecked")
	public void deleteSystem(SystemVo system) {
		this.getSqlMapClientTemplate().delete("system.deleteSystem", system);
	}
	
	@SuppressWarnings("unchecked")
	public int provinceIsExist(String province_id){
		return getTotalRows("system.queryProvinceIsExist", province_id);
	}
	
	@SuppressWarnings("unchecked")
	public void updateSetting(String setting_value) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("setting_value", setting_value);
		this.getSqlMapClientTemplate().update("system.updateSetting", map) ;
		
	}

	@SuppressWarnings("unchecked")
	public String querySetting() {
		return (String) this.getSqlMapClientTemplate().queryForObject("system.querySetting");
	}
	
	@SuppressWarnings("unchecked")
	public List<AreaVo> getProvince() {
		return this.getSqlMapClientTemplate().queryForList("system.getProvince");
	}

	@SuppressWarnings("unchecked")
	public List<AreaVo> getCity(String provinceid) {
		return this.getSqlMapClientTemplate().queryForList("system.getCity", provinceid);
	}

	@SuppressWarnings("unchecked")
	public List<AreaVo> getArea(String cityid) {
		return this.getSqlMapClientTemplate().queryForList("system.getArea", cityid);
	}

	public void updateEstateNot(Map<String,String> modify_Map) {
		this.getSqlMapClientTemplate().update("system.updateEstateNot",modify_Map);
	}





}
