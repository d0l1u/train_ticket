package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.AppComplainDao;
import com.l9e.transaction.service.AppComplainService;
import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.ComplainVo;
@Service("appComplainService")
public class AppComplainServiceImpl implements AppComplainService {

	@Resource
	private AppComplainDao appComplainDao;
	
	public List<Map<String,String>> queryComplainList(
			Map<String, Object> paramMap) {
		return appComplainDao.queryComplainList(paramMap);
	}
	
	public int queryComplainListCount(
			Map<String, Object> paramMap) {
		return appComplainDao.queryComplainListCount(paramMap);
	}

	public Map<String, String> queryComplainParticularInfo(
			String complain_id) {

		return appComplainDao.queryComplainParticularInfo(complain_id);
	}

	public void updateComplainInfo(
			ComplainVo complain) {
		appComplainDao.updateComplainInfo(complain) ;
	}
	
	public void deleteComplain(String complain_id) {
		appComplainDao.deleteComplain(complain_id) ;
	}
	
	public List<Map<String,String>> queryComplainStatCount() {
		return appComplainDao.queryComplainStatCount();
	}
	
	public List<AreaVo> getProvince() {
		return appComplainDao.getProvince();
	}

	public List<AreaVo> getCity(
			String provinceid) {
		return appComplainDao.getCity(provinceid);
	}

	public List<AreaVo> getArea(
			String cityid) {
		return appComplainDao.getArea(cityid);
	}

	public Map<String, String> querySupervise_nameToArea_no(String string) {
		return appComplainDao.querySupervise_nameToArea_no(string);
	}


}
