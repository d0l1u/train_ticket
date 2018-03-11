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

import com.l9e.transaction.dao.QuestionOrderDao;
import com.l9e.transaction.service.QuestionOrderService;
@Service("questionOrderService")
public class QuestionOrderServiceImpl implements QuestionOrderService{
	@Resource
	private QuestionOrderDao questionOrderDao;
	@Override
	public void addLog(Map<String, String> log) {
		questionOrderDao.addLog(log);
	}

	@Override
	public void addtrain_question_order(Map<String, Object> paramMap) {
		questionOrderDao.addtrain_question_order(paramMap);
	}

	@Override
	public void deletetrain_question_order(Map<String, Object> paramMap) {
		questionOrderDao.deletetrain_question_order(paramMap);
	}

	@Override
	public List<Map<String, Object>> querytrain_question_order(
			Map<String, Object> paramMap) {
		return questionOrderDao.querytrain_question_order(paramMap);
	}

	@Override
	public void updatetrain_question_order(Map<String, Object> paramMap) {
		questionOrderDao.updatetrain_question_order(paramMap);
	}
	
	@Override
	public void updatequestion_status(HttpServletRequest request, HttpServletResponse response,Map<String, Object> paramMap) {
		int count=questionOrderDao.updatetrain_question_order(paramMap);
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

	@Override
	public int querytrain_question_orderCount(Map<String, Object> paramMap) {
		return questionOrderDao.querytrain_question_orderCount(paramMap);
	}

	@Override
	public HashMap<String, String> getquestionOrderInfo(String question_id) {
		return questionOrderDao.getquestionOrderInfo(question_id);
	}

	@Override
	public List<Map<String, Object>> queryHistroyByQuestion_id(String question_id) {
		return questionOrderDao.queryHistroyByQuestion_id(question_id);
	}

}
