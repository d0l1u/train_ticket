package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.QunarExcelDao;
@Repository("qunarExcelDao")
public class QunarExcelDaoImpl extends BaseDao implements QunarExcelDao {

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryQunarBook(HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("qunarexcel.queryQunarBook", map);
	}
	
	//查询联程预订订单信息
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryLianChengQunarBook(HashMap<String, Object> map){
		return this.getSqlMapClientTemplate().queryForList("qunarexcel.queryLianChengQunarBook", map);
	}
	
	//查询退款订单信息
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryRefundTicket(HashMap<String, Object> map){
		return this.getSqlMapClientTemplate().queryForList("qunarexcel.queryRefundTicket", map);
	}

}
