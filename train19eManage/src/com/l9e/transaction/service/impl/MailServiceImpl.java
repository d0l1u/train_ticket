package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.MailDao;
import com.l9e.transaction.service.MailService;
@Service("mailService")
public class MailServiceImpl implements MailService {
	@Resource
	private  MailDao mailDao;

	@Override
	public List<Map<String, String>> queryMailList(Map<String, Object> paramMap) {
		return mailDao.queryMailList(paramMap);
	}
	@Override
	public List<Map<String, String>> queryMailExcel(Map<String, Object> paramMap) {
		return mailDao.queryMailExcel(paramMap);
	}
	
	@Override
	public int queryMailListCount(Map<String, Object> paramMap) {
		return mailDao.queryMailListCount(paramMap);
	}

	@Override
	public void addMail(Map<String, String> addMap) {
		mailDao.addMail(addMap);
	}

	@Override
	public Map<String, String> queryMailModify(String mailId) {
		return mailDao.queryMailModify(mailId);
	}

	@Override
	public void updateMail(Map<String, String> updateMap) {
		mailDao.updateMail(updateMap);
	}

	@Override
	public String queryMailAddress(String content) {
		return mailDao.queryMailAddress(content);
	}

	
}
