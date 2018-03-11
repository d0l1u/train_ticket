package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.ExtSettingDao;
import com.l9e.transaction.service.ExtSettingService;
import com.l9e.transaction.vo.ExtSettingVo;
@Service("extSettingService")
public class ExtSettingServiceImpl implements ExtSettingService {
	@Resource
	private ExtSettingDao extSettingDao;

	@Override
	public List<ExtSettingVo> queryExtSettingList(Map<String, Object> paramMap) {
		return extSettingDao.queryExtSettingList(paramMap);
	}

	@Override
	public int queryExtSettingListCount(Map<String, Object> paramMap) {
		return extSettingDao.queryExtSettingListCount(paramMap);
	}

	@Override
	public void updateMerchantStatus(Map<String, Object> paramMap, Map<String, String> logMap) {
		extSettingDao.updateMerchantStatus(paramMap);
		extSettingDao.addMarchantLog(logMap);
	}

	@Override
	public void addMarchantInfo(Map<String, Object> mapAdd, Map<String, String> logMap) {
		extSettingDao.addMarchantInfo(mapAdd);
		extSettingDao.addMarchantLog(logMap);
	}

	@Override
	public String queryMarchantId(String merchantId) {
		return extSettingDao.queryMarchantId(merchantId);
	}

	@Override
	public ExtSettingVo queryMerchantInfo(String merchantId) {
		return extSettingDao.queryMerchantInfo(merchantId);
	}

	@Override
	public void updateMarchantInfo(Map<String, Object> paramMap, Map<String, String> logMap) {
		extSettingDao.updateMarchantInfo(paramMap);
		extSettingDao.addMarchantLog(logMap);
	}

	@Override
	public List<Map<String, Object>> queryMarchantLogList(
			Map<String, Object> paramMap) {
		return extSettingDao.queryMarchantLogList(paramMap);
	}

	@Override
	public int queryMarchantLogListCount() {
		return extSettingDao.queryMarchantLogListCount();
	}
	
	
}
