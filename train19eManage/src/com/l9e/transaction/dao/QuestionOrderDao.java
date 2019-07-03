package com.l9e.transaction.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface QuestionOrderDao {
		//查询
	public List<Map<String,Object>> querytrain_question_order(Map<String, Object> paramMap);
		//条数
	int querytrain_question_orderCount(Map<String, Object> paramMap);
		//修改
	public int updatetrain_question_order(Map<String, Object> paramMap);
		//增加
	public void addtrain_question_order(Map<String, Object> paramMap) ;
		//删除
	public void deletetrain_question_order(Map<String, Object> paramMap);
		//添加日志
	public void addLog(Map<String, String> log);
	//问题订单详情
	public HashMap<String, String> getquestionOrderInfo(String question_id);
	//查询操作日志
	List<Map<String, Object>>  queryHistroyByQuestion_id(String question_id);
	
}

