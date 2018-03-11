package com.l9e.transaction.service.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.QunarBookDAO;
import com.l9e.transaction.service.QunarBookService;
import com.l9e.transaction.vo.QunarBookVo;

@Service("qunarBookService")
public class QunarBookServiceImpl implements QunarBookService {

	@Resource
	private QunarBookDAO qunarBookDAO;

	public List<QunarBookVo> getBookList(HashMap<String, Object> paramMap) {
		return qunarBookDAO.getBookList(paramMap);
	}
	
	public List<QunarBookVo> getBookList1(HashMap<String, Object> paramMap) {
		return qunarBookDAO.getBookList1(paramMap);
	}

	public int getBookListCount(HashMap<String, Object> paramMap) {
		return qunarBookDAO.getBookListCount(paramMap);
	}
	
	public int getBookList1Count(HashMap<String, Object> paramMap) {
		return qunarBookDAO.getBook1ListCount(paramMap);
	}

	public List<QunarBookVo> getAllBookList() {
		return qunarBookDAO.getAllBookList();
	}

	// 获得某订单号的详细订单信息
	public HashMap<String, String> getBookOrderInfo(String order_id) {
		return qunarBookDAO.getBookOrderInfo(order_id);
	}

	// 获得某订单号的车票信息
	public List<HashMap<String, String>> getBookCpInfo(String order_id) {
		return qunarBookDAO.getBookCpInfo(order_id);
	}

	// 获得某订单号的日志信息
	public List<HashMap<String, String>> getBookLog(String order_id) {
		return qunarBookDAO.getBookLog(order_id);
	}

	// 获得某订单号的通知信息
	public HashMap<String, String> getBookNotify(String order_id) {
		return qunarBookDAO.getBookNotify(order_id);
	}

	// 获得某联程订单的联程信息
	public List<HashMap<String, String>> queryLianChengOrderInfo(String order_id) {
		return qunarBookDAO.queryLianChengOrderInfo(order_id);
	}

	// 获得联程总订单信息
	public HashMap<String, String> queryLianChengTotalOrderInfo(String order_id) {
		return qunarBookDAO.queryLianChengTotalOrderInfo(order_id);
	}

	// 获得联程订单付款信息
	public HashMap<String, String> queryLianChengPayInfo(String trip_id) {
		return qunarBookDAO.queryLianChengPayInfo(trip_id);
	}
	
	//更新通知状态
	public void updateNotify_status(HttpServletRequest request,
			HttpServletResponse response,Map<String, String> paramMap){
	 int count	= qunarBookDAO.updateNotify_status(paramMap);
	 if(count>0){
			write(response,"true");
		}else{
			write(response,"false");
		}
	}
	private void write(HttpServletResponse response, String wrStr) {
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(wrStr);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//添加日志信息
	public void insertLog(Map<String, String> logMap){
		qunarBookDAO.insertLog(logMap);
	}
}
