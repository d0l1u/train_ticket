package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.ElongOrderInfo;
import com.l9e.transaction.vo.ElongOrderInfoCp;
import com.l9e.transaction.vo.ElongOrderLogsVo;

import net.sf.json.JSONArray;

public interface ElongOrderService {
	/**通知系统出票接口*/
	void addNotifyCpSys(Map<String,Object> orderInfo,List<Map<String, Object>> cpInfoList,String order_id,int num);
	/**查询重复下单*/
	int getOrderCount(String order_id);
	/**查询订单信息*/
	Map<String, Object> queryOrderInfo(String order_id);
	/**查询订单信息 */
	void insertRefundOrder(Map<String, String> paramMap);
	/**查询订单下所有产品信息*/
	List<Map<String, Object>> querySendOrderCpsInfo(String order_id);
	/**更新订单信息*/
	void updateOrderInfo(Map<String, Object> map,List<Map<String, String>> cpMapList,boolean isNotNotice);
	/**查询退款状态*/
	String queryRefundStatus(Map<String, String> paramMap);
	Map<String, Object> queryrefundInfo(Map<String, String> params);
	void addOrder(ElongOrderInfo orderInfo);
	void updateRefundStatus(Map<String, String> paramMap);
	String queryCpPayMoney(Map<String, String> paramMap);
	String queryNoticeStatusByOrderId(String orderId);
	void insertElongOrderLogs(ElongOrderLogsVo log);
	void addOfflineRefund(Map<String, String> params);
	List<ElongOrderInfoCp> queryOrderCpInfo(String orderId);
	String send(ElongOrderInfo orderInfo, String string);
	void initNotice(String resultStr,String type,String order_id);
	String queryChangeCpPayMoney(Map<String, String> paramMap);
	String queryChangeFromTime(Map<String, String> paramMap);
	ElongOrderInfoCp queryElongCpInfoByCpId(String cp_id);
	Map<String, Object> queryAccountOrderInfo(Map<String, String> cpParam);
	
	
}
