package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.QuestionOrderDao;

@Repository("questionOrderDao")
public class QuestionOrderDaoImpl extends BaseDao implements QuestionOrderDao{

	//查询
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> querytrain_question_order(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("questionorder.querytrain_question_order",paramMap);
	}
	//修改
	public int updatetrain_question_order(Map<String, Object> paramMap){
		return (Integer)this.getSqlMapClientTemplate().update("questionorder.updatetrain_question_order", paramMap);
	}
	//增加
	public void addtrain_question_order(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().insert("questionorder.addtrain_question_order", paramMap);
	}
	//删除
	public void deletetrain_question_order(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().delete("questionorder.deletetrain_question_order",paramMap);

	}
	@Override
	public void addLog(Map<String, String> log) {
		this.getSqlMapClientTemplate().insert("questionorder.addLog", log);
	}
	@Override
	public int querytrain_question_orderCount(Map<String, Object> paramMap) {
		return (Integer)getTotalRows("questionorder.querytrain_question_orderCount", paramMap);
	}
	
	//获得信息
	@SuppressWarnings("unchecked")
	public HashMap<String, String> getquestionOrderInfo(String question_id) {
		return (HashMap<String, String>) this.getSqlMapClientTemplate().queryForObject("questionorder.getquestionOrderInfo", question_id);
	}
	//查询操作日志
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryHistroyByQuestion_id(String question_id) {
		return this.getSqlMapClientTemplate().queryForList("questionorder.queryHistroyByQuestion_id",question_id);
	}
}
