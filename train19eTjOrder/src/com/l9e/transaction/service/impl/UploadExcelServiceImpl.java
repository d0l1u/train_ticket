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

import com.l9e.transaction.dao.UploadExcelDao;
import com.l9e.transaction.service.UploadExcelService;
@Service("uploadExcelService")
public class UploadExcelServiceImpl implements UploadExcelService {

	@Resource
	private UploadExcelDao uploadExcelDao;
	
	
	public List<Map<String,Object>> getExcelList(HashMap<String, Object> paramMap) {
		return uploadExcelDao.getExcelList(paramMap);
	}

	
	public int getExcelListCount(HashMap<String, Object> paramMap) {
		return uploadExcelDao.getExcelListCount(paramMap);
	}

	
	public void insertIntoExcel(HashMap<String, Object> paramMap) {
		uploadExcelDao.insertIntoExcel(paramMap);
	}

	
	public void deleteExcelById(HttpServletRequest request,
			HttpServletResponse response, String excelId) {
		int count =	uploadExcelDao.deleteExcelById(excelId);
		if(count>0 ){
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

	
	public List<Map<String, String>> getElongAlterCpList(String id) {
		return uploadExcelDao.getElongAlterCpList(id);
	}

	
	public List<Map<String, String>> queryTcAlterList(
			HashMap<String, Object> map) {
		return uploadExcelDao.queryTcAlterList(map);
	}
	
	public List<Map<String, String>> queryAlterList(
			HashMap<String, Object> map) {
		return uploadExcelDao.queryAlterList(map);
	}

	
	public List<Map<String, String>> queryTcBookList(HashMap<String, Object> map) {
		return uploadExcelDao.queryTcBookList(map);
	}

	
	public List<Map<String, String>> queryTcRefundList(
			HashMap<String, Object> map) {
		return uploadExcelDao.queryTcRefundList(map);
	}

	
	public List<Map<String, String>> queryAllBook(HashMap<String, Object> map) {
		return uploadExcelDao.queryAllBook(map);
	}


	public List<Map<String, String>> queryElongBookCp(
			HashMap<String, Object> map) {
		return uploadExcelDao.queryElongBookCp(map);
	}


	public List<Map<String, String>> queryElongRefundTicket(
			HashMap<String, Object> map) {
		return uploadExcelDao.queryElongRefundTicket(map);
	}


	public String queryElongRefundTicketName(String string) {
		return uploadExcelDao.queryElongRefundTicketName(string);
	}


	public List<Map<String, String>> queryAppRefundList(
			HashMap<String, Object> map) {
		return uploadExcelDao.queryAppRefundList(map);
	}


	public List<Map<String, String>> queryElongRefundList(
			HashMap<String, Object> map) {
		return uploadExcelDao.queryElongRefundList(map);
	}


	public List<Map<String, String>> queryExtRefundList(
			HashMap<String, Object> map) {
		return uploadExcelDao.queryExtRefundList(map);
	}


	public List<Map<String, String>> queryInnerRefundList(
			HashMap<String, Object> map) {
		return uploadExcelDao.queryInnerRefundList(map);
	}


	public List<Map<String, String>> queryQunarRefundList(
			HashMap<String, Object> map) {
		return uploadExcelDao.queryQunarRefundList(map);
	}


	public List<Map<String, String>> queryl9eRefundList(
			HashMap<String, Object> map) {
		return uploadExcelDao.queryl9eRefundList(map);
	}


	public List<Map<String, String>> queryTestList(HashMap<String, Object> map) {
		return uploadExcelDao.queryTestList(map);
	}


	public List<Map<String, String>> queryTestMap(String string) {
		return uploadExcelDao.queryTestMap(string);
	}


	public List<Map<String, String>> queryGtRefundList(
			HashMap<String, Object> map) {
		return uploadExcelDao.queryGtRefundList(map);
	}


	public void tcAddTcCheck(Map<String, String> addMap) {
		uploadExcelDao.tcAddTcCheck(addMap);
	}


	public List<Map<String, String>> queryMtRefundList(
			HashMap<String, Object> map) {
		return uploadExcelDao.queryMtRefundList(map);
	}


	public List<Map<String, String>> queryXcRefundList(
			HashMap<String, Object> map) {
		return uploadExcelDao.queryXcRefundList(map);
	}




	public List<Map<String, String>> queryMtBookList(HashMap<String, Object> map) {
		return uploadExcelDao.queryMtBookList(map);
	}

	public List<Map<String, String>> queryGtBookList(HashMap<String, Object> map) {
		return uploadExcelDao.queryGtBookList(map);
	}

	public List<Map<String, String>> queryGtRefundNotifyList(HashMap<String, Object> map) {
		return uploadExcelDao.queryGtRefundNotifyList(map);
	}
	
	public List<Map<String, String>> queryGtgjAlterList(HashMap<String, Object> map) {
		return uploadExcelDao.queryGtgjAlterList(map);
	}
	
	public List<Map<String, String>> queryXcBookList(HashMap<String, Object> map) {
		return uploadExcelDao.queryXcBookList(map);
	}

	public List<Map<String, String>> queryXcRefundNotifyList(HashMap<String, Object> map) {
		return uploadExcelDao.queryXcRefundNotifyList(map);
	}


	public List<Map<String, String>> queryTuniuRefundList(HashMap<String, Object> map) {
		return uploadExcelDao.queryTuniuRefundList(map);
	}


	public void addCheckOrderInfo(Map<String, String> m) {
		uploadExcelDao.addCheckOrderInfo(m);
	}


	public List<Map<String, String>> queryTuniuBookList(HashMap<String, Object> map) {
		return uploadExcelDao.queryTuniuBookList(map);
	}


	public List<Map<String, String>> queryTuniuRefundTicket(HashMap<String, Object> map) {
		return uploadExcelDao.queryTuniuRefundTicket(map);
	}


	public List<Map<String, String>> queryCheckTcAlter(HashMap<String, Object> map) {
		return uploadExcelDao.queryCheckTcAlter(map);
	}



}
