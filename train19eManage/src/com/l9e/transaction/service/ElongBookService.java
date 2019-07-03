package com.l9e.transaction.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.l9e.transaction.vo.ElongVo;

public interface ElongBookService {

	public int getBookListCount(HashMap<String, Object> paramMap);
	
	public int getBookListCountCx(HashMap<String, Object> paramMap);

	public List<ElongVo> getAllBookList();

	public List<ElongVo> getBookList(HashMap<String, Object> paramMap);

	public List<ElongVo> getBookListCx(HashMap<String, Object> paramMap);

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
			HttpServletResponse response,Map<String,String> paramMap);
	
	//添加日志信息
	public void insertLog(Map<String, String> logMap);
	
	
	//查询预订订单信息
	public List<Map<String, String>> queryQunarBook(HashMap<String, Object> map);
	
	//查询预订订单信息(包含乘客信息)
	public List<Map<String, String>> queryQunarBookCp(HashMap<String, Object> map);
	
	//查询联程预订订单信息
	public List<Map<String, String>> queryLianChengQunarBook(HashMap<String, Object> map);
	
	//查询退款订单信息
	public List<Map<String, String>> queryRefundTicket(HashMap<String, Object> map);
	
	//重新通知
	public void updateNotify_Again(HttpServletRequest request,
			HttpServletResponse response,Map<String,String> paramMap);
	
	public String queryRefundTicketName(String cp_id);
	
	//撤销订单
	String updateCheXiao(Map<String, String> updateMap);
	
	String updateCheXiaoCp(Map<String, String> updateMap);
	String queryDbOrder_status(String order_id);
	String queryDbNotify_status(String order_id);

	public String updateGotoNormal(Map<String, String> updateMap);

	public String updateGotoFailure(Map<String, String> updateMap);

	public int queryQunarBookCpCount(HashMap<String, Object> map);
	
	
}
