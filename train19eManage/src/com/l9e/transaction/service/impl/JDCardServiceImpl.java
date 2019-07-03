package com.l9e.transaction.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.JDCardDao;
import com.l9e.transaction.service.JDCardService;

@Service("jdCardService")
public class JDCardServiceImpl implements JDCardService{

	@Resource
	private JDCardDao jdCardDao;
	@Override
	public int queryJDCardCounts(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return jdCardDao.queryJDCardCounts(paramMap);
	}

	@Override
	public List<Map<String, String>> queryJDCardList(
			Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return jdCardDao.queryJDCardList(paramMap);
	}

	@Override
	public List<Map<String, BigDecimal>> queryJDCardMoney() {
		// TODO Auto-generated method stub
		return jdCardDao.queryJDCardMoney();
	}

	@Override
	public void addJDCardInfo(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		jdCardDao.addJDCardInfo(paramMap);
	}

	@Override
	public void updateJDCardInfo(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		jdCardDao.updateJDCardInfo(paramMap);
	}

	@Override
	public Map<String, Object> queryJDCardById(Integer cardID) {
		// TODO Auto-generated method stub
		return jdCardDao.queryJDCardById(cardID);
	}

}
