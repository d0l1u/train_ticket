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

import com.l9e.transaction.dao.MeituanBookDao;
import com.l9e.transaction.service.MeituanBookService;
import com.l9e.transaction.vo.MeituanVo;

@Service("meituanBookService")
public class MeituanBookServiceImpl implements MeituanBookService {

	@Resource
	private MeituanBookDao meituanBookDao;

	public List<MeituanVo> getBookList(HashMap<String, Object> paramMap) {
		return meituanBookDao.getBookList(paramMap);
	}

	public int getBookListCount(HashMap<String, Object> paramMap) {
		return meituanBookDao.getBookListCount(paramMap);
	}
	
	public List<MeituanVo> getAllBookList() {
		return meituanBookDao.getAllBookList();
	}

	// 获得某订单号的详细订单信息
	public HashMap<String, String> getBookOrderInfo(String order_id) {
		return meituanBookDao.getBookOrderInfo(order_id);
	}

	// 获得某订单号的车票信息
	public List<HashMap<String, String>> getBookCpInfo(String order_id) {
		return meituanBookDao.getBookCpInfo(order_id);
	}

	// 获得某订单号的日志信息
	public List<HashMap<String, String>> getBookLog(String order_id) {
		return meituanBookDao.getBookLog(order_id);
	}

	// 获得某订单号的通知信息
	public HashMap<String, String> getBookNotify(String order_id) {
		return meituanBookDao.getBookNotify(order_id);
	}

	//添加日志信息
	public void insertLog(Map<String, String> logMap){
		meituanBookDao.insertLog(logMap);
	}
	
	
	//查询退款订单信息
	public List<Map<String, String>> queryRefundTicket(HashMap<String, Object> map){
		return meituanBookDao.queryRefundTicket(map);
	}

	//重新通知
	public void updateNotify_Again(HttpServletRequest request,
			HttpServletResponse response,Map<String, String> paramMap) {
		int count1=	 meituanBookDao.updateNotify_Again(paramMap);
		int count2=   meituanBookDao.updateNotify_Again1(paramMap);
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
	//更新通知状态
	public void updateNotify_status(HttpServletRequest request,
			HttpServletResponse response,Map<String,String> paramMap){
		int count=	meituanBookDao.updateNotify_status(paramMap);
		if(count>0){
			write(response,"true");
		}else{
			write(response,"false");
		}
		
	}
	
	public List<Map<String, String>> queryQunarBookCp(HashMap<String, Object> map) {
		return meituanBookDao.queryQunarBookCp(map);
	}
	
	public String queryRefundTicketName(String cp_id) {
		return meituanBookDao.queryRefundTicketName(cp_id);
		
	}
}
