package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.OrderInfoBx;
import com.l9e.transaction.vo.OrderInfoCp;
import com.l9e.transaction.vo.OrderInfoPs;
import com.l9e.transaction.vo.RefundVo;

public interface ExtShijiService {

	OrderInfo queryOrderInfo(String order_id);

	OrderInfoPs queryOrderInfoPs(String order_id);
	
	List<Map<String, String>> queryOrderDetailList(String order_id);

	List<OrderInfo> queryOrderList(Map<String, Object> paramMap);

	int queryOrderListCount(Map<String, Object> paramMap);

	List<Map<String, String>> queryRefundStreamList(String order_id);

	Map<String, String> querySpecTimeBeforeFrom(String order_id);
	
	//查询代理商出票成功、出票失败、出票中、退款的订单数据 
	Map<String,Object> queryAgentOrderNum(Map<String,Object> map);

	//根据车票ID查询退票乘客 
	String selectRefundPassengers(String cp_id);

	List<OrderInfo> queryOrderRefundList(Map<String, Object> paramMap);

	int queryExtAccountOrderListCount(Map<String, Object> paramMap);

	List<OrderInfo> queryExtAccountOrderList(Map<String, Object> paramMap);

	List<Map<String, String>> queryRefundList(String orderId);

	List<OrderInfo> queryExtAccountOrderExcelList(Map<String, Object> paramMap);

}
