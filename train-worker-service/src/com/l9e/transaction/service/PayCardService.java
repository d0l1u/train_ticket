package com.l9e.transaction.service;

import com.l9e.transaction.vo.PayCard;

/**
 * 支付账号业务接口
 * @author licheng
 *
 */
public interface PayCardService {

	/**
	 * 根据支付机器人的id获取支付账号信息
	 * @param workerId 机器人主键id
	 * @return
	 */
	PayCard getCardByWorkerId(Integer workerId);
	
	/**
	 * 更新支付账号信息
	 * @param payCard 支付账号信息实体
	 */
	void updatePayCard(PayCard payCard);
}
