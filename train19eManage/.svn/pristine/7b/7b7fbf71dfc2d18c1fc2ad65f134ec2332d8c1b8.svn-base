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

import com.l9e.transaction.dao.ElongBookDao;
import com.l9e.transaction.service.ElongBookService;
import com.l9e.transaction.vo.ElongVo;

@Service("elongBookService")
public class ElongBookServiceImpl implements ElongBookService {

	@Resource
	private ElongBookDao elongBookDao;

	public List<ElongVo> getBookList(HashMap<String, Object> paramMap) {
		return elongBookDao.getBookList(paramMap);
	}
	public List<ElongVo> getBookListCx(HashMap<String, Object> paramMap) {
		return elongBookDao.getBookListCx(paramMap);
	}

	public int getBookListCount(HashMap<String, Object> paramMap) {
		return elongBookDao.getBookListCount(paramMap);
	}
	
	public int getBookListCountCx(HashMap<String, Object> paramMap) {
		return elongBookDao.getBookListCountCx(paramMap);
	}


	public List<ElongVo> getAllBookList() {
		return elongBookDao.getAllBookList();
	}

	// 获得某订单号的详细订单信息
	public HashMap<String, String> getBookOrderInfo(String order_id) {
		return elongBookDao.getBookOrderInfo(order_id);
	}

	// 获得某订单号的车票信息
	public List<HashMap<String, String>> getBookCpInfo(String order_id) {
		return elongBookDao.getBookCpInfo(order_id);
	}

	// 获得某订单号的日志信息
	public List<HashMap<String, String>> getBookLog(String order_id) {
		return elongBookDao.getBookLog(order_id);
	}

	// 获得某订单号的通知信息
	public HashMap<String, String> getBookNotify(String order_id) {
		return elongBookDao.getBookNotify(order_id);
	}

	// 获得某联程订单的联程信息
	public List<HashMap<String, String>> queryLianChengOrderInfo(String order_id) {
		return elongBookDao.queryLianChengOrderInfo(order_id);
	}

	// 获得联程总订单信息
	public HashMap<String, String> queryLianChengTotalOrderInfo(String order_id) {
		return elongBookDao.queryLianChengTotalOrderInfo(order_id);
	}

	// 获得联程订单付款信息
	public HashMap<String, String> queryLianChengPayInfo(String trip_id) {
		return elongBookDao.queryLianChengPayInfo(trip_id);
	}
	
	//更新通知状态
	public void updateNotify_status(HttpServletRequest request,
			HttpServletResponse response,Map<String,String> paramMap){
		int count=	elongBookDao.updateNotify_status(paramMap);
		if(count>0){
			write(response,"true");
		}else{
			write(response,"false");
		}
		
	}
	//添加日志信息
	public void insertLog(Map<String, String> logMap){
		elongBookDao.insertLog(logMap);
	}
	
	public List<Map<String, String>> queryQunarBook(HashMap<String, Object> map) {
		return elongBookDao.queryQunarBook(map);
	}
	
	public List<Map<String, String>> queryQunarBookCp(HashMap<String, Object> map) {
		return elongBookDao.queryQunarBookCp(map);
	}
	
	//查询联程预订订单信息
	public List<Map<String, String>> queryLianChengQunarBook(HashMap<String, Object> map){
		return elongBookDao.queryLianChengQunarBook(map);
	}
	
	//查询退款订单信息
	public List<Map<String, String>> queryRefundTicket(HashMap<String, Object> map){
		return elongBookDao.queryRefundTicket(map);
	}

	//重新通知
	public void updateNotify_Again(HttpServletRequest request,
			HttpServletResponse response,Map<String, String> paramMap) {
		int count1=	 elongBookDao.updateNotify_Again(paramMap);
		int count2=   elongBookDao.updateNotify_Again1(paramMap);
		if(count1>0 || count2>0){
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

	@Override
	public String queryRefundTicketName(String cp_id) {
		return elongBookDao.queryRefundTicketName(cp_id);
		
	}
	
	
	public String queryDbOrder_status(String order_id) {
		return elongBookDao.queryDbOrder_status(order_id);
	}
	
	public String queryDbNotify_status(String order_id) {
		return elongBookDao.queryDbNotify_status(order_id);
	}
	
	@Override
	public String updateCheXiao(Map<String, String> updateMap) {
		String result = "yes";
		try {
			elongBookDao.updateCheXiaoElong(updateMap);
			elongBookDao.insertCheXiaoNotify(updateMap);
		} catch (Exception e) {
			result="no";
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public String updateCheXiaoCp(Map<String, String> updateMap) {
		String result = "yes";
		try {
			elongBookDao.updateCheXiaoCp(updateMap);
		} catch (Exception e) {
			result="no";
			e.printStackTrace();
		}
		return result;
	}
	@Override
	public String updateGotoFailure(Map<String, String> updateMap) {
		String result = "yes";
		try {
			elongBookDao.updateGotoFailure(updateMap);
			elongBookDao.updateGotoNormal(updateMap);
		} catch (Exception e) {
			result="no";
			e.printStackTrace();
		}
		return result;
	}
	@Override
	public String updateGotoNormal(Map<String, String> updateMap) {
		String result = "yes";
		try {
			elongBookDao.updateGotoNormal(updateMap);
		} catch (Exception e) {
			result="no";
			e.printStackTrace();
		}
		return result;
	}
	@Override
	public int queryQunarBookCpCount(HashMap<String, Object> map) {
		return elongBookDao.queryQunarBookCpCount(map);
	}
	
}
