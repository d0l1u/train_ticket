package com.l9e.transaction.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 京东预付卡
 * @author wangsf01
 *
 */
public interface JDCardService {
	
	/**
	 *  获取按条件查询的京东预付卡数 
	 * @param paramMap
	 * @return
	 */
	public int queryJDCardCounts(Map<String, Object> paramMap);
	/**
	 * 获取按条件查询所有的京东预付卡
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, String>> queryJDCardList(Map<String, Object> paramMap);
	
	/**
	 * 查询预付卡的总金额和总余额
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, BigDecimal>> queryJDCardMoney();
	
	/**
	 * 新增预付卡信息
	 * @param paramMap
	 * @return
	 */
	public void addJDCardInfo(Map<String, Object> paramMap);
	
	/**
	 * 修改预付卡信息
	 * @param paramMap
	 * @return
	 */
	public void updateJDCardInfo(Map<String, String> paramMap);
	
	/**
	 * 根据ID查询预付卡信息
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> queryJDCardById(Integer cardID);

}