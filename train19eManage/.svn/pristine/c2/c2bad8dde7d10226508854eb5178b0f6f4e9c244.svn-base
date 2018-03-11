package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.AccountDao;
import com.l9e.transaction.dao.SystemDao;
import com.l9e.transaction.service.SystemService;
import com.l9e.transaction.vo.AccountVo;
import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.SystemVo;

@Service("systemService")
public class SystemServiceImpl extends BaseDao implements SystemService{

	
	
	@Resource
	private SystemDao systemDao;
	
	public List<Map<String, String>> querySystemList(
			Map<String, Object> paramMap) {
		return systemDao.querySystemList(paramMap);
	}

	public int querySystemListCount(Map<String, Object> paramMap) {
		return systemDao.querySystemListCount(paramMap);
	}

	public Map<String, String> querySystem(String config_id) {
		return systemDao.querySystem(config_id);
	}

	public void updateSystem(SystemVo system) {
		systemDao.updateSystem(system);
	}

	public void insertSystem(SystemVo system) {
		System.out.println(system.getProvince_id());
		if(systemDao.provinceIsExist(system.getProvince_id()) == 0 ){
			systemDao.insertSystem(system);
		}
	}

	public void deleteSystem(SystemVo system) {
		systemDao.deleteSystem(system);
	}

	public void updateSetting(String setting_value) {
		systemDao.updateSetting(setting_value) ;
		
	}
	
	public String querySetting() {
		return systemDao.querySetting();
	}
	
	public List<AreaVo> getProvince() {
		return systemDao.getProvince();
	}

	public List<AreaVo> getCity(String provinceid) {
		return systemDao.getCity(provinceid);
	}

	public List<AreaVo> getArea(String cityid) {
		return systemDao.getArea(cityid);
	}

	public void updateEstateNot(Map<String,String> modify_Map) {
		systemDao.updateEstateNot(modify_Map);
	}



}
