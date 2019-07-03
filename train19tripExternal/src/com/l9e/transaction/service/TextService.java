package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface TextService {

	List<String> queryOutTicket();

	Map<String, Object> queryOrderInfo(String orderId);

	void updateOrderInfo(Map<String, Object> orderInfo);

	int updateNoticeInfo(Map<String, Object> orderInfo);

	void updateRefundOrderInfo(Map<String, Object> refundInfo);

	List<Map<String,Object>> queryRefundTicket();

	//Map<String, Object> queryRefundOrderInfo(Map<String, String> param);

	List<String> queryOutTicketCpId(String orderId);

	Map<String, Object> queryCpInfo(String orderId);

	void updateCpInfo(Map<String, Object> cpInfo);

	Map<String, Object> queryMerchantinfo(String string);

	List<String> queryYdToOutTicket();

	void updateRefundNoticeInfo(Map<String, String> map);

	int selectCountNumFromNotice(Map<String, Object> orderInfo);

	List<String> queryPaySuccess();

	String selectSeatType(String orderId);

}
