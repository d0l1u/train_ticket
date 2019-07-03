package com.l9e.transaction.dao.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.CheckPriceDao;

@Repository("checkPriceDao")
public class CheckPriceDaoImpl extends BaseDao implements CheckPriceDao {

	public void addAlipayInfoList(final List<Map<String, Object>> paramList) {
		//this.getSqlMapClientTemplate().insert("checkprice.addAlipayInfoList",paramList);

		try {
			if(paramList!=null){
				this.getSqlMapClientTemplate().execute(new SqlMapClientCallback() { 
					   public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException { 
					    executor.startBatch(); 
					    int batch = 0;
					    for (Map<String, Object> cont : paramList) { 
					     executor.insert("checkprice.addAlipayInfo", cont);
					     batch++; 
					     if(batch==5000){ 
					         executor.executeBatch(); 
					        batch = 0; 
					         } 
					    }  
					    executor.executeBatch(); 
					    return null; 
					   } 
					  }); 
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("报错"+e);
		}		
		
	}

	public void addAlipayBalance(Map<String, Object> paMap) {
		this.getSqlMapClientTemplate().insert("checkprice.addAlipayBalance",
				paMap);
	}

}
