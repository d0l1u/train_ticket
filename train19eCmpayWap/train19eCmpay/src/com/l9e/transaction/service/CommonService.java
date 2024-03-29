package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.common.SystemConfInfo;
import com.l9e.transaction.vo.ProductVo;
import com.l9e.transaction.vo.SuitVo;
public interface CommonService {

	public List<ProductVo> queryProductInfoList(ProductVo product);

	public SystemConfInfo querySysConf(String provinceId);

	public String queryProvinceName(String provinceId);

	public List<Map<String, String>> queryNoticeList();

	public Map<String, String> queryNoticeInfo(String noticeId);

	public Map<String, String> queryOldAreaInfo(String provinceId);

	public String queryInterfaceChannel();

	public List<String> querySysInterfaceUrl();
	
	public List<String> querySysSettingValue(Map<String, String> paramMap);

	public List<Map<String, String>> queryNoticeAllList();

	public String querySysSettingByKey(String key);
	//是否投诉
	public SuitVo querySuit(String agentId);
	//更新投诉信息
	public void updateSuit(SuitVo suit);
}
