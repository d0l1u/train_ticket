package com.l9e.transaction.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.l9e.transaction.vo.MeituanVo;

public interface MeituanBookService {

	public int getBookListCount(HashMap<String, Object> paramMap);
	
	public List<MeituanVo> getAllBookList();

	public List<MeituanVo> getBookList(HashMap<String, Object> paramMap);

	// 获得某订单号的详细订单信息
	public HashMap<String, String> getBookOrderInfo(String order_id);

	// 获得某订单号的车票信息
	public List<HashMap<String, String>> getBookCpInfo(String order_id);

	// 获得某订单号的日志信息
	public List<HashMap<String, String>> getBookLog(String order_id);

	// 获得某订单号的通知信息
	public HashMap<String, String> getBookNotify(String order_id);

	//更新通知状态
	public void updateNotify_status(HttpServletRequest request,
			HttpServletResponse response,Map<String,String> paramMap);
	
	//添加日志信息
	public void insertLog(Map<String, String> logMap);
	
	//查询退款订单信息
	public List<Map<String, String>> queryRefundTicket(HashMap<String, Object> map);
	
	//重新通知
	public void updateNotify_Again(HttpServletRequest request,
			HttpServletResponse response,Map<String,String> paramMap);
	
	public String queryRefundTicketName(String cp_id);
	
	//查询预订订单信息(包含乘客信息)
	public List<Map<String, String>> queryQunarBookCp(HashMap<String, Object> map);
}
