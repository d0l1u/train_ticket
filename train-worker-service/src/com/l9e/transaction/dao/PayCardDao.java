package com.l9e.transaction.dao;

import java.util.Map;

import com.l9e.transaction.vo.PayCard;

/**
 * 支付账号信息持久接口
 * @author licheng
 *
 */
public interface PayCardDao {

	/**
	 * 查询一个支付账号信息
	 * @param params
	 * @return
	 */
	PayCard selectOnePayCard(Map<String, Object> params);
	
	/**
	 * 更新支付账号信息
	 * @param payCard
	 * @return
	 */
	int updatePayCard(PayCard payCard);
}
