package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.common.SystemConfInfo;
import com.l9e.transaction.vo.ProductVo;
import com.l9e.transaction.vo.SuitVo;

public interface CommonDao {

	public List<ProductVo> queryProductInfoList(ProductVo product);

	public void addOrderEopInfo(Map<String, String> eopInfo);

	public Map<String, String> queryEopInfo(String asp_order_id);

	public void updateOrderEop(Map<String, String> eopInfo);

	public String queryOrderIdByEopId(String eop_order_id);

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
	
	//投诉
	public SuitVo querySuit(String agentId);
	//更新投诉
	public void updateSuit(SuitVo suit);
	//查询停止购票时间
	public String querySysStopTime();
}
