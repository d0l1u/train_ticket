package com.l9e.transaction.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.DBNoticeVo;
import com.l9e.transaction.vo.DBOrderInfo;
import com.l9e.transaction.vo.DBPassengerInfo;
import com.l9e.transaction.vo.DBRemedyNoticeVo;

public interface OrderService {
	public void addOrder(DBOrderInfo orderInfo);

	public String queryOrderStatusByOrderId(String order_id);
	
	public String queryOutTicketOrderStatusByOrderId(String orderId);

	public DBOrderInfo queryOrderInfo(String orderId);

	//public List<DBPassengerInfo> queryOrderCpsInfo(String orderId);

	public void addNotifyCpSys(DBOrderInfo orderInfo,DBNoticeVo vo);
	
	public String sendOutTicket(DBOrderInfo orderInfo,String type);
	
	public void addOrderResultNotice(DBNoticeVo vo);
	
	/**更新订单状态*/
	int updateOrderStatus(Map<String, String> map);
	/**查询订单状态*/
	public String queryOrderStatus(String orderid);

	public Map<String, Object> queryOutTicketOrderInfo(String orderId);

	public Map<String, Object> queryOutTicketCpInfo(String cpId);

	public void addBookResultNotice(DBNoticeVo vo);

	/**出票系统返回出票结果 更新订单内容 以及通知表*/
	public void updateOrderInfo(List<Map<String, String>> cpMapList, DBOrderInfo orderInfo);
	
	/**取消订单通知*/
	public String cancel(String order_id);
	
	/**支付订单通知*/
	public String pay(String order_id,String pay_money);

	public String queryRefundStatus(Map<String, String> paramMap);

	public void insertRefundOrder(Map<String, String> paramMap);


	Map<String, Object> queryAllChannelNotify(String order_id);

	public String querySeatNo(Map<String, String> cpInfo);

	public List<Map<String, String>> queryPassengerList(
			Map<String, String> param);

	public List<Map<String, String>> queryPassersList(
			HashMap<String, Object> map);

	public int queryPassersCount();

	public void addVerifyTime(Map<String, String> timeMap);

	public String queryVerfiyTimeRatio();

	public void addPhone(Map<String, String> paramMap);
	
	
	
	
}
