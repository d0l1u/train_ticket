package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.ElongNoticeVo;
import com.l9e.transaction.vo.ElongOrderNoticeVo;

public interface ElongNoticeDao {
	void updateOrdersNotice(ElongNoticeVo elongNoticeVo);
	List<ElongOrderNoticeVo> getOrderNoticesList();
	List<Map<String, Object>> getRefundNoticesList();
	void insertTrainInterfaceNotices(Map<String,String> map);
	List<Map<String,Object>> querySysNoticeList();
	void updateSysNotice(Map<String,Object> sysNoticeInfo);
	void addNotice(ElongOrderNoticeVo notice);
	String queryNoticeStatusByOrderId(String orderId);
	List<Map<String, Object>> getRefundNoticesListById(String order_id);
	Map<String, Object> queryCpInfoById(String cpId);
	Map<String, Object> queryAlterCpInfoById(String cpId);
	int updateNoticeBegin(ElongOrderNoticeVo orderNoticeVo);
	void updateNoticeTime(ElongOrderNoticeVo orderNoticeVo);
	int updateStartOfflineRefundNotice(Map<String,Object> map);
	int updateBeginRefundNotice(Map<String, Object> map);
	int updateStartNoticeOutSys(Map<String, Object> map);
}
