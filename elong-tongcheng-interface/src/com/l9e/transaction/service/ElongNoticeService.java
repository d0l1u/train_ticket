package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.ElongNoticeVo;
import com.l9e.transaction.vo.ElongOrderNoticeVo;

/**
 * 艺龙通知出票结果/退款结果/出票系统出票 service
 * */
public interface ElongNoticeService {
	/**出票结果通知接口*/
	void addSendOrdersNotice(ElongOrderNoticeVo orderNoticeVo);
	
	/**获取出票结果通知列表*/
	List<ElongOrderNoticeVo> getOrderNoticesList();
	
	/**获取退款结果通知列表*/
	List<Map<String,Object>> getRefundNoticesList();
	
	/**退款通知接口*/
	void sendRefundNotice(Map<String,Object> map);
	
	/** 查询需要通知出票系统出票的订单序列      ----*/
	List<Map<String,Object>> querySysNoticeList();
	
	/**更新出票通知表*/
	void updateSysNotice(Map<String,Object> sysNoticeInfo);
	/**获取线下退款 发送序列信息*/
	List<Map<String, Object>> getOfflineRefundRefundList();

	/**启动线下退款 */
	void sendOfflineRefund(Map<String, Object> map);

	int updateNoticeBegin(ElongOrderNoticeVo orderNoticeVo);

	void updateNoticeTime(ElongOrderNoticeVo orderNoticeVo);

	List<Map<String, Object>> getTcOfflineRefundRefundList();

	int updateStartOfflineRefundNotice(Map<String,Object>  map);

	int updateBeginRefundNotice(Map<String, Object> map);

	int updateStartNoticeOutSys(Map<String, Object> map);
}
