package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.common.SystemConfInfo;
import com.l9e.transaction.vo.ProductVo;
import com.l9e.transaction.vo.SuitVo;
public interface CommonService {

	public List<ProductVo> queryProductInfoList(ProductVo product);

	public void addOrderEopInfo(Map<String, String> eopInfo);

	public Map<String, String> queryEopInfo(String asp_order_id);

	public void updateOrderEopInfo(Map<String, String> eopInfo);

	public void updateJmOrderEopInfo(Map<String, String> eopInfo);

	public Map<String, String> queryOrderIdByEopId(Map<String, String> map);

	public SystemConfInfo querySysConf(String provinceId);

	public String queryProvinceName(String provinceId);

	public List<Map<String, String>> queryNoticeList(String provinceName);

	public Map<String, String> queryNoticeInfo(String noticeId);

	public Map<String, String> queryOldAreaInfo(String provinceId);

	public String queryInterfaceChannel();

	public List<String> querySysInterfaceUrl();
	
	public List<String> querySysSettingValue(Map<String, String> paramMap);

	public List<Map<String, String>> queryNoticeAllList(String provinceName);

	public String querySysSettingByKey(String key);
	
	public String queryTrainSysSettingByKey(String key);
	//是否投诉
	public SuitVo querySuit(String agentId);
	//更新投诉信息
	public void updateSuit(SuitVo suit);
	//查询停止购票时间
	public String querySysStopTime();

	public List<ProductVo> queryProductInfoYuyueList(ProductVo product);

	public List<Map<String, String>> queryExtNoticeList(String extChannel);

	public Map<String, String> queryExtNoticeInfo(String noticeId);

	public List<Map<String, String>> queryExtNoticeAllList(String extChannel);

	public Map<String, String> querySeatMap(Map<String, String> seatMap);
	
}
