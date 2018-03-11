package com.l9e.transaction.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.l9e.transaction.vo.Train;
import com.l9e.transaction.vo.TrainCtrip;
import com.l9e.transaction.vo.TrainSeat;

public class TestDao extends BaseDao {

	private Logger logger = Logger.getLogger(TestDao.class);
	 

	////查询所有的信息
	public List<Map<String, String>> queryFnList(Map<String, String> paramMap) {
		List<Map<String, String>> list = null;
		try {
			list = this.getSqlMapClient().queryForList("test.queryFnList", paramMap);
		} catch (SQLException e) {
			logger.error("[查询所有信息 exception]" + e.getMessage());
		}
		return list;
	}
	
	public List<Map<String, String>> queryAllList(Map<String, String> paramMap) {
		List<Map<String, String>> list = null;
		try {
			list = this.getSqlMapClient().queryForList("test.queryAllList", paramMap);
		} catch (SQLException e) {
			logger.error("[查询所有信息 exception]" + e.getMessage());
		}
		return list;
	}
	
	public List<Map<String, String>> queryList(Map<String, String> paramMap) {
		List<Map<String, String>> list = null;
		try {
			list = this.getSqlMapClient().queryForList("test.queryList", paramMap);
		} catch (SQLException e) {
			logger.error("[查询所有信息 exception]" + e.getMessage());
		}
		return list;
	}
	
	public List<Map<String, String>> queryNfdAll(Map<String, Object> paramMap) {
		List<Map<String, String>> list = null;
		try {
			list = this.getSqlMapClient().queryForList("test.queryNfdAll", paramMap);
		} catch (SQLException e) {
			logger.error("[查询所有信息 exception]" + e.getMessage());
		}
		return list;
	}
	
	public List<Map<String, String>> queryAir2c() {
		List<Map<String, String>> list = null;
		try {
			list = this.getSqlMapClient().queryForList("test.queryAir2c");
		} catch (SQLException e) {
			logger.error("[查询所有信息 exception]" + e.getMessage());
		}
		return list;
	}
	
 
	
	public void addNfdFinal(Map<String, String> paramMap) {
		try {
			this.getSqlMapClient().insert("test.addNfdFinal", paramMap);
			logger.info("新增一条记录");
		} catch (SQLException e) {
			logger.error("[新增信息 exception]" + e.getMessage());
		}
		
	}
	
	public void addMenu(Map<String, Object> paramMap) {
		try {
			this.getSqlMapClient().insert("test.addMenu", paramMap);
			logger.info("新增一条记录");
		} catch (SQLException e) {
			logger.error("[新增信息 exception]" + e.getMessage());
		}
		
	}
	
	public List<Map<String, String>> queryFinalArr() {
		List<Map<String, String>> list = null;
		try {
			list = this.getSqlMapClient().queryForList("test.queryFinalArr");
		} catch (SQLException e) {
			logger.error("[查询所有信息 exception]" + e.getMessage());
		}
		return list;
	}
	
	public List<Map<String, String>> queryFinalDep() {
		List<Map<String, String>> list = null;
		try {
			list = this.getSqlMapClient().queryForList("test.queryFinalDep");
		} catch (SQLException e) {
			logger.error("[查询所有信息 exception]" + e.getMessage());
		}
		return list;
	}
	
	public List<Map<String, String>> queryFinalByDepArr(Map<String, String> paramMap) {
		List<Map<String, String>> list = null;
		try {
			list = this.getSqlMapClient().queryForList("test.queryFinalByDepArr", paramMap);
		} catch (SQLException e) {
			logger.error("[查询所有信息 exception]" + e.getMessage());
		}
		return list;
	}
	
}
