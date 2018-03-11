package com.l9e.transaction.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.JDRefundDao;
import com.l9e.transaction.service.JDRefundService;

@Service("jdRefundService")
public class JDRefundServiceImpl implements JDRefundService{

	@Resource
	private JDRefundDao jdRefundDao;
	
	/**
	 *  获取按条件查询的京东退票订单数 
	 * @param paramMap
	 * @return
	 */
	@Override
	public int queryJDRefundCounts(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return jdRefundDao.queryJDRefundCounts(paramMap);
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
		return jdRefundDao.queryJDRefundExcel(paramMap);
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
		return jdRefundDao.queryJDRefundInfo(paramMap);
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
		return jdRefundDao.queryJDRefundList(paramMap);
	}

	@Override
	public List<Map<String, Object>> queryHistroy(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return jdRefundDao.queryHistroy(paramMap);
	}

	@Override
	public void addJDRefundlog(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		jdRefundDao.addJDRefundlog(paramMap);
	}

	@Override
	public void updateRefundOpt(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		jdRefundDao.updateRefundOpt(map);
	}

	@Override
	public void updateOrderstatusToRobot(Map<String, String> updateMap) {
		// TODO Auto-generated method stub
		jdRefundDao.updateOrderstatusToRobot(updateMap);
	}

	@Override
	public String selectEopID(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return jdRefundDao.selectEopID(paramMap);
	}

	@Override
	public void updateJDRefundStatus(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		jdRefundDao.updateJDRefundStatus(paramMap);
	}

	@Override
	public int updateRefundstreamStatus(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return jdRefundDao.updateRefundstreamStatus(paramMap);
	}

}
