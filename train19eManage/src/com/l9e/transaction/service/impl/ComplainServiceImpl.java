package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.ComplainDao;
import com.l9e.transaction.service.ComplainService;
import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.ComplainVo;
@Service("comlpainService")
public class ComplainServiceImpl implements ComplainService{
	@Resource
	private ComplainDao complainDao ;
	
	public List<Map<String,String>> queryComplainList(
			Map<String, Object> paramMap) {
		return complainDao.queryComplainList(paramMap);
	}
	
	public int queryComplainListCount(
			Map<String, Object> paramMap) {
		return complainDao.queryComplainListCount(paramMap);
	}

	public Map<String, String> queryComplainParticularInfo(
			String complain_id) {

		return complainDao.queryComplainParticularInfo(complain_id);
	}

	public void updateComplainInfo(ComplainVo complain) {
		complainDao.updateComplainInfo(complain) ;//修改详细
		complainDao.addComplainHistoryInfo(complain);//增加历史日志
	}
	
	public void deleteComplain(String complain_id) {
		complainDao.deleteComplain(complain_id) ;
	}
	
	public List<Map<String,String>> queryComplainStatCount() {
		return complainDao.queryComplainStatCount();
	}
	
	public List<AreaVo> getProvince() {
		return complainDao.getProvince();
	}

	public List<AreaVo> getCity(
			String provinceid) {
		return complainDao.getCity(provinceid);
	}

	public List<AreaVo> getArea(
			String cityid) {
		return complainDao.getArea(cityid);
	}

	public Map<String, String> querySupervise_nameToArea_no(String string) {
		return complainDao.querySupervise_nameToArea_no(string);
	}

	@Override
	public List<Map<String, Object>> queryHistroyByComplainId(String complainId) {
		return complainDao.queryHistroyByComplainId(complainId);
	}

	

	


}
