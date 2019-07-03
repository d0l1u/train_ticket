package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.ExtSettingVo;

public interface ExtSettingService {

	List<ExtSettingVo> queryExtSettingList(Map<String, Object> paramMap);

	int queryExtSettingListCount(Map<String, Object> paramMap);

	void updateMerchantStatus(Map<String, Object> paramMap, Map<String, String> logMap);

	void addMarchantInfo(Map<String, Object> mapAdd, Map<String, String> logMap);

	String queryMarchantId(String merchantId);

	ExtSettingVo queryMerchantInfo(String merchantId);

	void updateMarchantInfo(Map<String, Object> paramMap, Map<String, String> logMap);

	int queryMarchantLogListCount();

	List<Map<String, Object>> queryMarchantLogList(Map<String, Object> paramMap);

}
