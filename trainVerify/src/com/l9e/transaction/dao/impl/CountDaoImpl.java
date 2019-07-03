package com.l9e.transaction.dao.impl;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.CountDao;
import com.l9e.transaction.vo.Count;

@Repository("countDao")
public class CountDaoImpl extends BaseDao implements CountDao {

	
	public void insertIntoCount(Count count) {
		this.getSqlMapClientTemplate().insert("count.insertIntoCount", count);
	}

}
