package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.CommonDao;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.vo.ExternalProductVo;

@Service("commonService")
public class CommonServiceImpl implements CommonService {
	
	@Resource
	private CommonDao commonDao;

	@Override
	public List<ExternalProductVo> queryForeignProductInfo(
			ExternalProductVo product) {
		return commonDao.queryForeignProductInfo(product);
	}

	public String queryInterfaceChannel() {
		return commonDao.queryInterfaceChannel();
	}

	public List<String> querySysInterfaceUrl() {
		return commonDao.querySysInterfaceUrl();
	}
	
	public List<String> querySysSettingValue(Map<String, String> paramMap) {
		return commonDao.querySysSettingValue(paramMap);
	}

	public String querySysSettingByKey(String key) {
		return commonDao.querySysSettingByKey(key);
	}

	@Override
	public Map<String, String> queryMerchantInfo(String merchant_id) {
		return commonDao.queryMerchantInfo(merchant_id);
	}

	@Override
	public void updateMerchantStatus(Map<String, String> paramMap) {
		commonDao.updateMerchantStatus(paramMap);
	}

	@Override
	public String getTrainSysSettingValue(String key) {
		return commonDao.getTrainSysSettingValue(key);
	}

}
