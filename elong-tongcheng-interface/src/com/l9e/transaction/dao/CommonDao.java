package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.ExternalProductVo;
import com.l9e.transaction.vo.SInfo;

public interface CommonDao {

	public List<ExternalProductVo> queryForeignProductInfo(ExternalProductVo product);
	
	public String queryInterfaceChannel();

	public List<String> querySysInterfaceUrl();

	public List<String> querySysSettingValue(Map<String, String> paramMap);
	
	public String querySysSettingByKey(String key);
	
	public Map<String,String> queryMerchantInfo(String merchant_id);
	
	public void updateMerchantStatus(Map<String, String> paramMap);

	public String queryElongSysValueByName(String settingName);
	
	public SInfo getSInfo(String checi, String sname);
	/**
	 * 根据车站名称查询车站code
	 * @param stationName
	 * @return
	 */
	public String queryStationCode(String stationName);
	
	/**
	 * 根据key值查询后台配置
	 * @param key
	 * @return
	 */
	public String querySysValByKey(String key);
}
