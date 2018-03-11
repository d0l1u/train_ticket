package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.ExtSettingVo;

public interface ExtSettingDao {

	List<ExtSettingVo> queryExtSettingList(Map<String, Object> paramMap);

	int queryExtSettingListCount(Map<String, Object> paramMap);

	void updateMerchantStatus(Map<String, Object> paramMap);

	void addMarchantInfo(Map<String, Object> mapAdd);

	String queryMarchantId(String merchantId);

	ExtSettingVo queryMerchantInfo(String merchantId);

	void updateMarchantInfo(Map<String, Object> paramMap);

	void addMarchantLog(Map<String, String> logMap);

	List<Map<String, Object>> queryMarchantLogList(Map<String, Object> paramMap);

	int queryMarchantLogListCount();

}
