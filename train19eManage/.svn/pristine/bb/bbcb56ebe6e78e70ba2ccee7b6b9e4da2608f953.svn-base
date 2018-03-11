package com.l9e.transaction.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.l9e.transaction.vo.QunarBookVo;

public interface QunarBookService {

	public int getBookListCount(HashMap<String, Object> paramMap);
	
	public int getBookList1Count(HashMap<String, Object> paramMap);

	public List<QunarBookVo> getAllBookList();

	public List<QunarBookVo> getBookList(HashMap<String, Object> paramMap);
	
	//通知成功操作人不为空
	public List<QunarBookVo> getBookList1(HashMap<String, Object> paramMap);

	// 获得某订单号的详细订单信息
	public HashMap<String, String> getBookOrderInfo(String order_id);

	// 获得某订单号的车票信息
	public List<HashMap<String, String>> getBookCpInfo(String order_id);

	// 获得某订单号的日志信息
	public List<HashMap<String, String>> getBookLog(String order_id);

	// 获得某订单号的通知信息
	public HashMap<String, String> getBookNotify(String order_id);

	// 获得某联程订单的联程信息
	public List<HashMap<String, String>> queryLianChengOrderInfo(String order_id);

	// 获得联程总订单信息
	public HashMap<String, String> queryLianChengTotalOrderInfo(String order_id);

	// 获得联程订单付款信息
	public HashMap<String, String> queryLianChengPayInfo(String trip_id);

	//更新通知状态
	public void updateNotify_status(HttpServletRequest request,
			HttpServletResponse response,Map<String, String> paramMap);
	
	//添加日志信息
	public void insertLog(Map<String, String> logMap);
}
