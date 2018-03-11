package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.JDRefundDao;

/**
 * 京东退票操作
 * @author wangsf01
 *
 */
@Repository("jdRefundDao")
public class JDRefundDaoImpl extends BaseDao implements JDRefundDao{

	/**
	 *  获取按条件查询的京东退票订单数 
	 * @param paramMap
	 * @return
	 */
	@Override
	public int queryJDRefundCounts(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return (Integer)this.getSqlMapClientTemplate().queryForObject("jdRefund.queryJDRefundCounts",paramMap);
	}

	/**
	 * 导出京东退票订单excel
	 * @param paramMap
	 * @return
	 */
	@Override
	public List<Map<String, String>> queryJDRefundExcel(
			Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().queryForList("jdRefund.queryJDRefundExcel",paramMap);
	}

	/**
	 * 查询京东订单退款明细
	 * @param paramMap
	 * @return
	 */
	@Override
	public List<Map<String, String>> queryJDRefundInfo(
			Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().queryForList("jdRefund.queryJDRefundInfo",paramMap);
	}

	/**
	 * 获取按条件查询所有的京东退票订单
	 * @param paramMap
	 * @return
	 */
	@Override
	public List<Map<String, String>> queryJDRefundList(
			Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().queryForList("jdRefund.queryJDRefundList",paramMap);
	}

	@Override
	public List<Map<String, Object>> queryHistroy(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().queryForList("jdRefund.queryHistroy",paramMap);
	}

	@Override
	public void addJDRefundlog(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().insert("jdRefund.addJDRefundlog", paramMap);
		
	}

	@Override
	public void updateRefundOpt(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().update("jdRefund.updateRefundOpt", map);
	}

	@Override
	public void updateOrderstatusToRobot(Map<String, String> updateMap) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().update("jdRefund.updateOrderstatusToRobot", updateMap);
	}

	@Override
	public String selectEopID(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return (String)this.getSqlMapClientTemplate().queryForObject("jdRefund.selectEopID", paramMap);
	}

	@Override
	public void updateJDRefundStatus(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().update("jdRefund.updateJDRefundStatus", paramMap);
	}

	@Override
	public int updateRefundstreamStatus(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().update("jdRefund.updateRefundstreamStatus", paramMap);
	}

}
