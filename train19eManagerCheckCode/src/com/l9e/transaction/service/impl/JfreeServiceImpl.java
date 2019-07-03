package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.JfreeDao;
import com.l9e.transaction.dao.LoginDao;
import com.l9e.transaction.service.JfreeService;
@Service("jfreeService")
public class JfreeServiceImpl implements JfreeService {
	@Resource
	private JfreeDao jfreeDao;

	@Override
	public List<Map<String, Object>> query15dayUserCode(String optRen) {
		return jfreeDao.query15dayUserCode(optRen);
	}

	@Override
	public int query15dayUserCodeCount(String optRen) {
		return jfreeDao.query15dayUserCodeCount(optRen);
	}

	@Override
	public List<Map<String, String>> query15dayCodePicture() {
		return jfreeDao.query15dayCodePicture();
	}

	@Override
	public List<Map<String, Object>> query3dayCodeSuccess(String date2) {
		return jfreeDao.query3dayCodeSuccess(date2);
	}

	@Override
	public List<Map<String, Object>> queryTodayCodeSuccess(String today) {
		return jfreeDao.queryTodayCodeSuccess(today);
	}

	@Override
	public int queryUserCodeSuccessCount(String optRen) {
		return jfreeDao.queryUserCodeSuccessCount(optRen);
	}

	@Override
	public List<Map<String, Object>> queryUserCodeSuccessList(Map<String,String> paramMap) {
		return jfreeDao.queryUserCodeSuccessList(paramMap);
	}

	@Override
	public List<Map<String, String>> query15dayDepartCodePicture(String department) {
		return jfreeDao.query15dayDepartCodePicture(department);
	}

	@Override
	public List<Map<String, Object>> queryDepartCodeSuccessPicture(
			Map<String, String> paramMap) {
		return jfreeDao.queryDepartCodeSuccessPicture(paramMap);
	}
	
	
}
