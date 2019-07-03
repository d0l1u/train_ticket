package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.ExternalProductVo;
public interface CommonService {

	public List<ExternalProductVo> queryForeignProductInfo(ExternalProductVo product);

	public String queryInterfaceChannel();

	public List<String> querySysInterfaceUrl();
	
	public List<String> querySysSettingValue(Map<String, String> paramMap);

	public String querySysSettingByKey(String key);
	
	public String getTrainSysSettingValue(String key);
	
	public Map<String,String> queryMerchantInfo(String merchant_id);
	
	public void updateMerchantStatus(Map<String, String> paramMap);

	public void updateMerchantVerifyNum(String merchantId);

	public Map<String, String> queryCheciPrice(Map<String, String> paramMap);
	
}