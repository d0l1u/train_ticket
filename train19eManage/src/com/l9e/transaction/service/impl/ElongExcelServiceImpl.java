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

import com.l9e.transaction.dao.ElongExcelDao;
import com.l9e.transaction.service.ElongExcelService;
@Service("elongExcelService")
public class ElongExcelServiceImpl implements ElongExcelService {

	@Resource
	private ElongExcelDao elongExcelDao;
	
	@Override
	public List<Map<String,Object>> getExcelList(HashMap<String, Object> paramMap) {
		return elongExcelDao.getExcelList(paramMap);
	}

	@Override
	public int getExcelListCount(HashMap<String, Object> paramMap) {
		return elongExcelDao.getExcelListCount(paramMap);
	}

	@Override
	public void insertIntoExcel(HashMap<String, Object> paramMap) {
		elongExcelDao.insertIntoExcel(paramMap);
	}

	@Override
	public void deleteExcelById(HttpServletRequest request,
			HttpServletResponse response, String excelId) {
		int count =	elongExcelDao.deleteExcelById(excelId);
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

	@Override
	public List<Map<String, String>> getElongAlterCpList(String id) {
		return elongExcelDao.getElongAlterCpList(id);
	}

	@Override
	public List<Map<String, String>> queryTcAlterList(
			HashMap<String, Object> map) {
		return elongExcelDao.queryTcAlterList(map);
	}

	@Override
	public List<Map<String, String>> queryTcBookList(HashMap<String, Object> map) {
		return elongExcelDao.queryTcBookList(map);
	}

	@Override
	public List<Map<String, String>> queryTcRefundList(
			HashMap<String, Object> map) {
		return elongExcelDao.queryTcRefundList(map);
	}

	@Override
	public List<Map<String, String>> queryAllBook(HashMap<String, Object> map) {
		return elongExcelDao.queryAllBook(map);
	}

	@Override
	public void tcAddCheck(Map<String, Object> paramMap) {
		elongExcelDao.tcAddCheck(paramMap);
	}

	@Override
	public int getTcCheckCount(HashMap<String, Object> paramMap) {
		return elongExcelDao.getTcCheckCount(paramMap);
	}

	@Override
	public List<Map<String, Object>> getTcCheckList(
			HashMap<String, Object> paramMap) {
		return elongExcelDao.getTcCheckList(paramMap);
	}

	@Override
	public List<String> getFileNameById(Map<String, String> excel) {
		return elongExcelDao.getFileNameById(excel);
	}

	@Override
	public void addFileNameLogs(Map<String, String> excel) {
		elongExcelDao.addFileNameLogs(excel);
	}


}
