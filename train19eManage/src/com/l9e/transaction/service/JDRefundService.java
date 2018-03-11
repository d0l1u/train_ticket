package com.l9e.transaction.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 京东退票
 * @author wangsf01
 *
 */
public interface JDRefundService {
	
	/**
	 *  获取按条件查询的京东退票订单数 
	 * @param paramMap
	 * @return
	 */
	public int queryJDRefundCounts(Map<String, Object> paramMap);
	/**
	 * 获取按条件查询所有的京东退票订单
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, String>> queryJDRefundList(Map<String, Object> paramMap);
	/**
	 * 导出京东退票订单excel
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, String>> queryJDRefundExcel(Map<String, Object> paramMap);
	/**
	 * 查询京东订单退款明细
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, String>> queryJDRefundInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询退款日志
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> queryHistroy(Map<String, Object> paramMap);
	
	/**
	 * 增加操作日志 
	 * @param paramMap
	 */
	public void addJDRefundlog(Map<String, String> paramMap);
	
	/**
	 * 更新京东refund表中操作人信息
	 * @param map
	 */
	public void updateRefundOpt(HashMap<String, Object> map);
	
	/**
	 * 机器退票
	 * @param updateMap
	 */
	public void updateOrderstatusToRobot(Map<String, String> updateMap);
	
	/**
	 * 查询EOP表ID
	 * @param paramMap
	 * @return
	 */
	public String selectEopID(Map<String, String> paramMap);
	
	/**
	 * 更新京东退款表状态
	 * @param paramMap
	 */
	public void updateJDRefundStatus(Map<String, String> paramMap);
	
	/**
	 * 更新EOP表状态
	 * @param paramMap
	 */
	public int updateRefundstreamStatus(Map<String, String> paramMap);

}
