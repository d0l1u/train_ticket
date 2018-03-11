package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.RegisterDao;
import com.l9e.transaction.service.RegisterService;
import com.l9e.transaction.vo.LoginUserVo;

@Service("registerService")
public  class RegisterServiceImpl implements RegisterService{
	
	@Resource 
	private RegisterDao registerDao;
	
	public int queryRegisterListCount(Map<String, Object> paramMap) {
		return registerDao.queryRegisterListCount(paramMap);
	}

	public List<Map<String, Object>> queryRegisterList(Map<String, Object> paramMap) {
		return registerDao.queryRegisterList(paramMap);
	}

	public List<Map<String, String>> queryRegisterExcel(Map<String, Object> paramMap) {
		return registerDao.queryRegisterExcel(paramMap);
	}

	public List<Map<String, Object>> queryRegisterInfo(
			Map<String, Object> paramMap) {
		return registerDao.queryRegisterInfo(paramMap);
	}
	
	public void updateRegisterSuccess(Map<String, Object> updateMap) {
		registerDao.updateRegisterSuccess(updateMap);
		//根据registId查询出该条记录的注册信息
//		Map<String,String> paramMap = registerDao.queryRegister(updateMap.get("regist_id").toString());
//		paramMap.put("opt_person", updateMap.get("opt_person").toString());
//		//将查询出的信息添加到cp_accountinfo表中
//		registerDao.addAccountInfo(paramMap);
	}
	
	public void updateRegisterFail(Map<String, Object> updateMap) {
		registerDao.updateRegisterFail(updateMap);
	}
	public void updateRegisterCheck(Map<String, Object> updateMap) {
		registerDao.updateRegisterCheck(updateMap);
	}

	@Override
	public void addAccountInfo(Map<String, Object> updateMap) {
		//根据registId查询出该条记录的注册信息
		Map<String,String> paramMap = registerDao.queryRegister(updateMap.get("regist_id").toString());
		paramMap.put("opt_person", updateMap.get("opt_person").toString());
		//将查询出的信息添加到cp_accountinfo表中
		registerDao.addAccountInfo(paramMap);
		
	}

	@Override
	public void addRegister(Map<String, String> addMap) {
		registerDao.addRegister(addMap);
	}

	@Override
	public String queryRegisterIdcard(String content) {
		return registerDao.queryRegisterIdcard(content);
	}

	@Override
	public void update12306Register(Map<String, String> paramMap) {
		registerDao.update12306Register(paramMap);
	}

	@Override
	public void updateRegisterSuccessGrade(Map<String, Object> updateGrade) {
		registerDao.updateRegisterSuccess(updateGrade);
		
	}
	
}